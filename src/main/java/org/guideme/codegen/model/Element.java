package org.guideme.codegen.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.codegen.code_builder.CodeBlock;
import org.guideme.codegen.code_builder.CodeBlockList;
import org.guideme.codegen.code_builder.CodeBuilder;
import org.guideme.codegen.code_builder.CodeFile;
import org.guideme.codegen.code_builder.Line;
import org.guideme.codegen.code_builder.CodeFile.CodeFileType;
import org.guideme.codegen.code_builder.Method;
import org.guideme.codegen.code_builder.Variable;
import org.guideme.guideme.util.StringUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Element {
	private static Logger logger = LogManager.getLogger();

	private final String name;
	private final String javaName;
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
		String sJavaName = null;
		String sCallback = null;

		for (int i = 0; i < nnm.getLength(); i++) {
			Node n = nnm.item(i);
			String attrName = n.getNodeName();
			String attrValue = n.getNodeValue();
			switch (attrName) {
			case "name":
				sName = attrValue;
				break;
			case "javaName":
				sJavaName = attrValue;
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
		if (sJavaName == null) {
			sJavaName = sName;
		}

		name = sName;
		javaName = sJavaName;
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
		return StringUtil.capitalizeFirstChar(javaName);
	}

	public void generateClass(File srcRoot, String[] packageName) throws IOException {
		CodeFile ans = new CodeFile(CodeFileType.CLASS, packageName, javaName);

		for (AttributeSet as : attributeSets) {
			as.applyToClassFile(ans);
		}

		/*
		 * We need the parsers in sorted order so that the text parser (if present) will
		 * be the last one.
		 */
		for (Attribute attr : getAllAttributesRecursiveSorted()) {
			attr.applyToClassFile(ans);
		}

		ans.constructor.addArg(new Variable("javax.xml.stream.XMLStreamReader", "reader"));

		ans.generate(srcRoot);
	}

	public String getXmlTag() {
		return name;
	}

	public CodeBlock generateCallback() {
		CodeBlockList ans = new CodeBlockList();

		String[] lines = callback.split("\n");
		for (String line : lines) {
			line = line.replace("{}", "new %s(reader)".formatted(getClassName()));
			ans.addContent(new Line(line));
		}

		return ans;
	}

}
