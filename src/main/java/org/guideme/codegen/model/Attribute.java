package org.guideme.codegen.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.util.StringUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Attribute implements ImportProvider, Comparable<Attribute>{
	private static Logger logger = LogManager.getLogger();

	private final String name;
	private final String javaName;
	private final String type;
	private final String defaultValue;
	private final boolean isText;

	private static HashMap<String, Attribute> allAttributes = new HashMap<>();

	public static Attribute getAttribute(Node xmlRoot) {
		NamedNodeMap nnm = xmlRoot.getAttributes();
		Node ref = nnm.getNamedItem("ref");
		if (ref != null) {
			if (nnm.getLength() != 1) {
				throw new IllegalStateException(
						"Attributes containing ref may not contain any other attribute");
			}
			Attribute ans = allAttributes.get(ref.getNodeValue());
			if (ans == null) {
				throw new IllegalStateException(
						"Cannot find referenced attribute " + ref.getNodeValue());
			}
			return ans;
		}
		return new Attribute(xmlRoot);
	}

	private Attribute(Node xmlRoot) {
		NamedNodeMap nnm = xmlRoot.getAttributes();
		String sName = null;
		String sType = null;
		String sDefaultValue = "";
		String sJavaName = null;
		boolean bIsText = false;

		for (int i = 0; i < nnm.getLength(); i++) {
			Node n = nnm.item(i);
			String attrName = n.getNodeName();
			String attrValue = n.getNodeValue();
			switch (attrName) {
			case "name":
				sName = attrValue;
				break;
			case "type":
				sType = attrValue;
				break;
			case "javaName":
				sJavaName = attrValue;
				break;
			case "default":
				sDefaultValue = attrValue;
				break;
			case "textContent":
				bIsText = Boolean.parseBoolean(attrValue);
				break;
			default:
				throw new IllegalStateException("Unexpected attribtue name " + attrName);
			}
		}

		if (sJavaName == null) {
			sJavaName = sName;
		}

		if (sName == null) {
			throw new IllegalStateException("Attribute missing name attribute");
		}
		if (sType == null) {
			throw new IllegalStateException("Attribute missing type attribute");
		}

		name = sName;
		type = sType;
		defaultValue = sDefaultValue;
		javaName = sJavaName;
		isText = bIsText;

		logger.info("Attribute {} initialized", name);
		allAttributes.put(name, this);
	}

	public String getJavaName() {
		return javaName;
	}

	public Set<String> getInterfaceImports() {
		Set<String> ans = new HashSet<>();
		
		if(type.contains(".") && !type.startsWith("java.lang")) {
			ans.add(type);
		}
		return ans;
	}
	public Set<String> getClassImports() {
		Set<String> ans = new HashSet<>();
		
		if(type.contains(".") && !type.startsWith("java.lang")) {
			ans.add(type);
		}
		ans.add("org.guideme.guideme.util.XMLReaderUtils");
		return ans;
	}

	private String getTypeBrief() {
		String[] ss = type.split("\\.");
		return ss[ss.length - 1];
	}

	private String getterName() {
		return "get" + StringUtil.capitalizeFirstChar(javaName);
	}

	public void generateInterfaceMethod(CodeBuilder builder) {
		builder.addLine("public %s %s();", getTypeBrief(), getterName());
	}
	
	public void generateFieldDecl(CodeBuilder builder) {
		builder.addLine("private final %s %s;", getTypeBrief(), getJavaName());
	}
	
	public void generateMethod(CodeBuilder builder) {
		builder.addLine("public %s %s(){", getTypeBrief(), getterName());
		builder.addLine("return %s;", getJavaName());
		builder.addLine("}");
	}
	
	private String getParseMethod() {
		if(isText) {
			return "XmlGuideReader.processText";
		}
		if(type.equals("java.time.LocalTime")) {
			return "XMLReaderUtils.getAttributeLocalTime";
		}
		return "XMLReaderUtils.getAttributeOrDefaultNoNS";
	}
	
	public void generateParser(CodeBuilder builder) {
		builder.addLine("this.%s = %s(reader, \"%s\",%s);", getJavaName(), getParseMethod(), name, defaultValue);
	}

	@Override
	public int compareTo(Attribute o) {
		/*
		 * Because of the way the stream parser works, we need text attributes to be at the end.
		 */
		if(this.isText && o.isText) {
			/*
			 * This will only happen if we try putting 2 text attributes on the same element.
			 * Since this does not actually work, we take this oppurtunity to bail.
			 */
			throw new IllegalStateException("Multiple text attributes are being compared.");
		}
		
		if(this.isText == o.isText) {
			return 0;
		}
		if (this.isText) {
			return 1;
		}
		return -1;
	}

}
