package org.guideme.codegen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.codegen.code_builder.CodeBlock;
import org.guideme.codegen.code_builder.CodeBlockEmpty;
import org.guideme.codegen.code_builder.CodeFile;
import org.guideme.codegen.code_builder.FieldDecl;
import org.guideme.codegen.code_builder.Line;
import org.guideme.codegen.code_builder.Method;
import org.guideme.codegen.code_builder.Type;
import org.guideme.codegen.code_builder.Variable;
import org.guideme.guideme.util.StringUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Attribute implements Comparable<Attribute>{
	private static final Logger LOGGER = LogManager.getLogger();

	private final String name;
	private final String javaName;
	private final Type type;
	private final String defaultValue;
	private final boolean isText;
	private final int sortOrder;
	
	private boolean isThroughInterface = false;

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
		int iSortOrder = 0;

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
			case "sortOrder":
				iSortOrder = Integer.parseInt(attrValue);
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
		type = new Type(sType);
		defaultValue = sDefaultValue;
		javaName = sJavaName;
		isText = bIsText;
		sortOrder = iSortOrder;

		LOGGER.info("Attribute {} initialized", name);
		allAttributes.put(name, this);
	}

	public String getJavaName() {
		return StringUtil.lowerFirstChar(javaName);
	}

	private String getJavaNameCaps() {
		return StringUtil.capitalizeFirstChar(javaName);
	}

	public String getXmlName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}

	public void setIsThroughInterface() {
		isThroughInterface = true;
	}
	
	public void setNotThroughInterface() {
		isThroughInterface = false;
	}
	
	private FieldDecl getMainDecl() {
		return new FieldDecl(new Variable(type, getJavaName()), getDefaultValue());
	}

	public FieldDecl[] generateFieldDecls() {
		List<FieldDecl> ans = new ArrayList<>();
		ans.add(getMainDecl());

		if (defaultValue.contains("comonFunctions")) {
			ans.add(new FieldDecl(new Variable(new Type("ComonFunctions"), "comonFunctions"),
					"ComonFunctions.getComonFunctions()"));
		}

		return ans.toArray(new FieldDecl[] {});
	}

	public Method[] generateMethods() {
		ArrayList<Method> ans = new ArrayList<>();

		FieldDecl decl = getMainDecl();
		ans.add(decl.getter());
		ans.add(decl.setter());
		if (type.isType("java.util.ArrayList")) {
			Type innerType = type.getGenericParameter();
			Method arrayAdd = new Method(Type.VOID, "add" + getJavaNameCaps());
			arrayAdd.addArg(new Variable(innerType, "toAdd"));
			arrayAdd.addCodeBlock(new Line("%s.add(toAdd);", getJavaName()));
			ans.add(arrayAdd);

			Method arrayCount = new Method(new Type("int"),
					"get%sCount".formatted(getJavaNameCaps()));
			arrayCount.addCodeBlock(new Line("return %s.size();", getJavaName()));
			ans.add(arrayCount);
		}

		if(isThroughInterface) {
			ans.forEach(Method::makeOverride);
		}
		
		return ans.toArray(new Method[] {});
	}

	private String getParseMethod() {
		if (isText) {
			return "XmlGuideReader.processText";
		}
		if (type.isType("java.time.LocalTime")) {
			return "XMLReaderUtils.getAttributeLocalTimeDefaultable";
		}
		return "XMLReaderUtils.getAttributeOrDefaultNoNS";
	}

	public String getDefaultValue() {
		if (type.isType("java.lang.String")) {
			return '"' + defaultValue + '"';
		}
		if (type.isType("java.util.ArrayList") && defaultValue.isBlank()) {
			return "new ArrayList<>()";
		}
		if (defaultValue.isBlank()) {
			return null;
		}
		return defaultValue;
	}

	public CodeBlock getConstructorFragment() {
		if (name.isBlank()) {
			return new CodeBlockEmpty();
		}

		CodeBlock ans = new Line("this.%s = %s(reader, \"%s\",%s);", getJavaName(),
				getParseMethod(), name, getDefaultValue());

		if (getParseMethod().contains("XMLReaderUtils")) {
			ans.addImport(new Type("org.guideme.guideme.util.XMLReaderUtils"));
		}

		ans.addImport(type.getTypeAbstract());

		if (defaultValue.contains("ComonFunctions")) {
			ans.addImport(new Type("org.guideme.guideme.scripting.functions.ComonFunctions"));
		}
		if (defaultValue.contains("SWT")) {
			ans.addImport(new Type("org.eclipse.swt.SWT"));
		}

		if (isText) {
			ans.addImport(new Type("org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader"));
			ans.addThrowable(new Type("javax.xml.stream.XMLStreamException"));
		}

		return ans;
	}

	public void applyToClassFile(CodeFile cf) {
		cf.constructor.addCodeBlock(getConstructorFragment());
		cf.addMethods(generateMethods());
		cf.addFieldDecls(generateFieldDecls());
		
		if(getDefaultValue()!= null && getDefaultValue().contains("ArrayList")) {
			cf.addImport("java.util.ArrayList");
		}
	}

	public void applyToInterfaceFile(CodeFile cf) {
		for (Method m : generateMethods()) {
			cf.addMethod(m.asAbstract());
		}
	}

	private int getSortOrder() {
		return sortOrder;
	}
	
	private int getMetaSortOrder() {
		if(this.isText) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public int compareTo(Attribute o) {
		int ans = getMetaSortOrder() - o.getMetaSortOrder();
		if(ans == 0) {
			ans = getSortOrder() - o.getSortOrder();
		}
		return ans;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		if(other == this) {
			return true;
		}
		if(other.getClass() != this.getClass()) {
			return false;
		}
		
		boolean ans = true;
		Attribute o = (Attribute) other;
		ans &= o.name.equals(name);
		ans &= o.javaName.equals(javaName);
		ans &= o.type.equals(type);
		ans &= o.defaultValue.equals(defaultValue);
		ans &= o.isText == isText;
		ans &= o.sortOrder == sortOrder;
		ans &= o.isThroughInterface == isThroughInterface;
		
		return ans;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	

}
