package org.guideme.codegen.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.codegen.code_builder.CodeBuilder;
import org.guideme.codegen.code_builder.CodeFile;
import org.guideme.codegen.code_builder.CodeFile.CodeFileType;
import org.guideme.guideme.util.StringUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AttributeSet {
	private static Logger logger = LogManager.getLogger();

	private ArrayList<Attribute> attributes = new ArrayList<>();
	private ArrayList<AttributeSet> subsets = new ArrayList<>();

	private final String name;

	private static HashMap<String, AttributeSet> allAttributeSets = new HashMap<>();

	public AttributeSet(Attribute singleton) {
		attributes.add(singleton);
		name = singleton.getJavaName();
	}

	public static AttributeSet get(Node xmlRoot) {
		NamedNodeMap nnm = xmlRoot.getAttributes();
		Node ref = nnm.getNamedItem("ref");
		if (ref != null) {
			if (nnm.getLength() != 1) {
				throw new IllegalStateException(
						"AttributeSets containing ref may not contain any other attribute");
			}
			AttributeSet ans = allAttributeSets.get(ref.getNodeValue());
			if (ans == null) {
				throw new IllegalStateException(
						"Cannot find referenced AttributeSet " + ref.getNodeValue());
			}
			return ans;
		}
		return new AttributeSet(xmlRoot);
	}

	private AttributeSet(Node xmlRoot) {
		NodeList nl = xmlRoot.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String tagName = n.getNodeName();
				switch (tagName) {
				case "attribute":
					attributes.add(Attribute.getAttribute(n));
					break;
				case "attributeSet":
					subsets.add(AttributeSet.get(n));
					break;
				default:
					throw new IllegalStateException(
							"Unexpected tag " + tagName + " under <attributesSet>");
				}
			}
		}

		NamedNodeMap nnm = xmlRoot.getAttributes();
		String sName = null;

		for (int i = 0; i < nnm.getLength(); i++) {
			Node n = nnm.item(i);
			String attrName = n.getNodeName();
			String attrValue = n.getNodeValue();
			switch (attrName) {
			case "name":
				sName = attrValue;
				break;
			default:
				throw new IllegalStateException("Unexpected attribtue name " + attrName);
			}
		}

		if (sName == null) {
			throw new IllegalStateException("AttributeSet missing name attribute");
		}
		name = sName;

		logger.info("AttributeSet {} initialized", name);
		allAttributeSets.put(name, this);
	}

	public String getInterfaceName() {
		return StringUtil.capitalizeFirstChar(name);
	}

	private String getExtendsPhrase() {
		if (subsets.isEmpty()) {
			return "";
		}
		String[] supers = new String[subsets.size()];
		for (int i = 0; i < supers.length; i++) {
			supers[i] = subsets.get(i).getInterfaceName();
		}
		return "extends " + String.join(", ", supers);
	}

	public void generateInterface(File srcRoot, String[] packageName) throws IOException {
		CodeFile ans = new CodeFile(CodeFileType.INTERFACE, packageName, getInterfaceName());

		for(Attribute attr : attributes) {
			attr.applyToInterfaceFile(ans);
		}

		for(AttributeSet as : subsets) {
			ans.addInterface(as.getInterfaceName());
		}

		ans.generate(srcRoot);
	}

	public void applyToClassFile(CodeFile cf) {
		cf.addInterface(getInterfaceName());
	}

	public Set<Attribute> getAllAttributesRecursive() {
		HashSet<Attribute> ans = new HashSet<>();
		ans.addAll(attributes);
		for (AttributeSet as : subsets) {
			ans.addAll(as.getAllAttributesRecursive());
		}
		return ans;
	}
}
