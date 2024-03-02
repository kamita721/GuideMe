package org.guideme.codegen.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.util.StringUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Element {
	private static Logger logger = LogManager.getLogger();

	private final String name;
	private final String callback;

	private final ArrayList<AttributeSet> attributeSets = new ArrayList<>();
	private final ArrayList<Attribute> attributes = new ArrayList<>();
	private final ArrayList<Element> elements = new ArrayList<>();

	public Element(Node xmlRoot) {
		NodeList nl = xmlRoot.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String tagName = n.getNodeName();
				switch (tagName) {
				case "attributeSet":
					attributeSets.add(AttributeSet.get(n));
					break;
				case "attribute":
					attributes.add(Attribute.getAttribute(n));
					break;
				case "element":
					elements.add(new Element(n));
					break;
				default:
					throw new IllegalStateException(
							"Unexpected tag " + tagName + " under <element>");
				}
			}
		}

		NamedNodeMap nnm = xmlRoot.getAttributes();
		String sName = null;
		String sCallback = null;

		for (int i = 0; i < nnm.getLength(); i++) {
			Node n = nnm.item(i);
			String attrName = n.getNodeName();
			String attrValue = n.getNodeValue();
			switch (attrName) {
			case "name":
				sName = attrValue;
				break;
			case "callback":
				sCallback = attrValue;
				break;
			default:
				throw new IllegalStateException("Unexpected attribtue name " + attrName);
			}
		}

		if (sName == null) {
			throw new IllegalStateException("Element missing name attribute");
		}
		if (sCallback == null) {
			throw new IllegalStateException("Element missing callback attribute");
		}

		name = sName;
		callback = sCallback;

		logger.info("Element {} initialized", name);
	}

	private Set<Attribute> getAllAttributesRecursive() {
		Set<Attribute> ans = new HashSet<>();
		ans.addAll(attributes);
		for (AttributeSet as : attributeSets) {
			ans.addAll(as.getAllAttributesRecursive());
		}
		return ans;
	}

	private Attribute[] getAllAttributesRecursiveSorted() {
		Set<Attribute> attrs = getAllAttributesRecursive();
		Attribute[] ans = attrs.toArray(new Attribute[] {});
		Arrays.sort(ans);
		return ans;
	}

	private String getImplementsPhrase() {
		if (attributeSets.isEmpty()) {
			return "";
		}
		String[] supers = new String[attributeSets.size()];
		for (int i = 0; i < supers.length; i++) {
			supers[i] = attributeSets.get(i).getInterfaceName();
		}
		return "implements " + String.join(", ", supers);
	}

	private String getClassName() {
		return StringUtil.capitalizeFirstChar(name);
	}

	public void generateClass(File srcRoot, String[] packageName) throws IOException {
		CodeBuilder ans = new CodeBuilder(packageName, getClassName());
		ans.addImport("javax.xml.stream.XMLStreamReader;");
		ans.addClassImports(getAllAttributesRecursive());
		ans.addLine();
		ans.addLine("public class %s %s {", getClassName(), getImplementsPhrase());

		for (Attribute attr : getAllAttributesRecursive()) {
			attr.generateFieldDecl(ans);
		}

		ans.addLine();
		ans.addLine("public %s(XMLStreamReader reader) {", getClassName());

		/*
		 * We need the parsers in sorted order so that the text parser (if present) will
		 * be the last one.
		 */
		for (Attribute attr : getAllAttributesRecursiveSorted()) {
			attr.generateParser(ans);
		}

		ans.addLine("}"); // constructor

		for (Attribute attr : getAllAttributesRecursive()) {
			attr.generateMethod(ans);
		}

		ans.addLine("}"); // public class

		ans.generate(srcRoot);
	}

	public String getXmlTag() {
		return name;
	}

	public void generateCallback(CodeBuilder builder) {
		String[] lines = callback.split("\n");
		for (String line : lines) {
			line = line.replace("{}", "reader");
			builder.addLine(line);
		}
	}

}
