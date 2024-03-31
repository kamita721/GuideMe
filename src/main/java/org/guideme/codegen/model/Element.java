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
import org.guideme.codegen.code_builder.CodeFile;
import org.guideme.codegen.code_builder.FieldDecl;
import org.guideme.codegen.code_builder.Line;
import org.guideme.codegen.code_builder.Method;
import org.guideme.codegen.code_builder.CodeFile.CodeFileType;
import org.guideme.codegen.code_builder.StringSwitch;
import org.guideme.codegen.code_builder.Type;
import org.guideme.codegen.code_builder.Variable;
import org.guideme.guideme.util.StringUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Element {
	private static final Logger LOGGER = LogManager.getLogger();

	private final String name;
	private final String javaName;
	private final String callback;

	private final ArrayList<AttributeSet> attributeSets = new ArrayList<>();
	private final ArrayList<Attribute> attributes = new ArrayList<>();
	private final ArrayList<Element> elements = new ArrayList<>();
	private final ArrayList<Type> extraImports = new ArrayList<>();
	private final String extraBody;

	public Element(Node xmlRoot) {
		NodeList nl = xmlRoot.getChildNodes();
		String sExtraBody = null;
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
				case "extraBody":
					sExtraBody = n.getFirstChild().getTextContent();
					break;
				case "extraImport":
					String toAdd = n.getFirstChild().getTextContent();
					extraImports.add(new Type(toAdd));
					break;
				default:
					throw new IllegalStateException(
							"Unexpected tag " + tagName + " under <element>");
				}
			}
		}
		extraBody = sExtraBody;

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

		LOGGER.info("Element {} initialized", name);
	}

	private Set<Attribute> getAllAttributesRecursive() {
		Set<Attribute> ans = new HashSet<>();
		ans.addAll(attributes);
		ans.forEach(Attribute::setNotThroughInterface);
		for (AttributeSet as : attributeSets) {
			Set<Attribute> toAdd = as.getAllAttributesRecursive();
			toAdd.forEach(Attribute::setIsThroughInterface);
			ans.addAll(toAdd);
		}
		return ans;
	}

	private Attribute[] getAllAttributesRecursiveSorted() {
		Set<Attribute> attrs = getAllAttributesRecursive();
		Attribute[] ans = attrs.toArray(new Attribute[] {});
		Arrays.sort(ans);
		return ans;
	}

	private String getClassName() {
		return StringUtil.capitalizeFirstChar(javaName);
	}

	private String getLowerJavaName() {
		return StringUtil.lowerFirstChar(javaName);
	}

	private void generateSubelementHandler(CodeFile cf) {
		cf.addFieldDecl(FieldDecl.loggerDecl());

		CodeBlockList toAdd = new CodeBlockList();
		toAdd.addImport("javax.xml.stream.XMLStreamConstants");
		toAdd.addImport("org.guideme.guideme.util.XMLReaderUtils");
		toAdd.addImport("org.apache.logging.log4j.LogManager");

		toAdd.addThrowable("javax.xml.stream.XMLStreamException");

		

		toAdd.addLine("int depth = 1;");
		toAdd.addLine("while (depth > 0) {");
		toAdd.addLine(" int eventType = reader.next();");
		toAdd.addLine(" if (eventType == XMLStreamConstants.START_ELEMENT) {");
		toAdd.addLine("  depth++;");
		toAdd.addLine("  String tagName = reader.getName().getLocalPart();");
		StringSwitch switchBlock = new StringSwitch("tagName");
		for (Element child : elements) {
			ArrayList<Attribute> attrs = child.attributes;
			if (attrs.size() != 1) {
				throw new IllegalStateException();
			}

			Attribute attr = attrs.get(0);
			String fieldName = child.getLowerJavaName();
			Line line = new Line("%s = XMLReaderUtils.getStringContentOrDefault(reader, %s);",
					fieldName, attr.getDefaultValue());
			switchBlock.addCase(child.getXmlTag(), line);

			FieldDecl decl = new FieldDecl(new Variable(attr.getType(), fieldName),
					attr.getDefaultValue());

			cf.addFieldDecl(decl);
			cf.addMethod(decl.getter());
			cf.addMethod(decl.setter());
		}

		CodeBlockList tagNotFoundHandler = new CodeBlockList();
		tagNotFoundHandler.addContent(new Line(
				"LOGGER.warn(\"Unhandled tag '{}' at location \\n{}\", tagName, reader.getLocation());"));
		tagNotFoundHandler
				.addContent(new Line("XMLReaderUtils.getStringContentUntilElementEnd(reader);"));
		switchBlock.setDefault(tagNotFoundHandler);

		toAdd.addContent(switchBlock);
		toAdd.addLine("  eventType = reader.next();");
		toAdd.addLine("  if (XMLReaderUtils.isAtElementEnd(reader, \"Settings\")) {");
		toAdd.addLine("   return;");
		toAdd.addLine("  }");
		toAdd.addLine(" }"); // if eventType == START_ELEMENT
		toAdd.addLine(" if (eventType == XMLStreamConstants.END_ELEMENT) {");
		toAdd.addLine("  depth--;");
		toAdd.addLine(" }");
		toAdd.addLine("}"); // while

		cf.constructor.addCodeBlock(toAdd);

	}

	public Method generateElementSerializer() {
		Method ans = new Method(new Type("org.w3c.dom.Element"), "asXml");
		ans.addArg("org.w3c.dom.Document", "doc");

		Attribute[] attrs = getAllAttributesRecursiveSorted();
		if (attrs.length == 0) {
			ans.addLine("return doc.createElement(\"%s\");", getXmlTag());
			return ans;
		}

		ans.addLine("Element ans = doc.createElement(\"%s\");", getXmlTag());

		ans.addImport("org.guideme.guideme.model.ModelConverters");

		for (Attribute attr : getAllAttributesRecursiveSorted()) {
			ans.addLine("ans.setAttribute(\"%s\",ModelConverters.toString(%s));", attr.getXmlName(),
					attr.getJavaName());
		}
		ans.addLine("return ans;");
		return ans;
	}

	public Method getNodeConstructor() {
		Method ans = new Method(null, getClassName());
		ans.addImport("org.apache.logging.log4j.Logger");
		ans.addImport("org.apache.logging.log4j.LogManager");

		ans.addArg("org.w3c.dom.Node", "n");

		ans.addLine("if(!n.getNodeName().equals(\"%s\")){", getXmlTag());
		ans.addLine(
				"LOGGER.warn(\"Error reading state file. Expected element '%s', but got '{}'\", n.getNodeName());",
				getXmlTag());
		ans.addLine("}");

		Attribute[] attrs = getAllAttributesRecursiveSorted();
		if (attrs.length == 0) {
			return ans;
		}
		ans.addImport("org.w3c.dom.NamedNodeMap");

		ans.addLine("NamedNodeMap nnm = n.getAttributes();");
		ans.addLine("for(int i=0; i<nnm.getLength(); i++){");
		ans.addLine("Node child = nnm.item(i);");
		ans.addLine("String attrName = child.getNodeName();");
		ans.addLine("String attrValue = child.getNodeValue();");
		StringSwitch switchBlock = new StringSwitch("attrName");

		for (Attribute attr : getAllAttributesRecursiveSorted()) {
			String key = attr.getXmlName();
			CodeBlockList cb = new CodeBlockList();
			/*
			 * We are passing in the javaName as the default value as well. This works,
			 * because we are in a constructor, so the default value is also the current
			 * value.
			 * 
			 * We need to do this, because we rely on the type of the argument to decide
			 * what converter function to call. In cases where the default value is null,
			 * there is no type information if we supply the value directly.
			 */
			cb.addLine("%s = ModelConverters.fromString(attrValue, %s);", attr.getJavaName(),
					attr.getJavaName());
			switchBlock.addCase(key, cb);
		}

		CodeBlockList attributeNotFound = new CodeBlockList();
		attributeNotFound.addLine("LOGGER.warn(\"Unhandled attribute '{}'\", attrName);");
		switchBlock.setDefault(attributeNotFound);

		ans.addCodeBlock(switchBlock);

		ans.addLine("}");

		return ans;
	}

	public void generateClass(File srcRoot, String[] packageName) throws IOException {
		CodeFile ans = new CodeFile(CodeFileType.CLASS, packageName, getClassName());

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

		if (extraBody != null) {
			ans.addExtraBody(CodeBlockList.fromString(extraBody));
		}

		if (!elements.isEmpty()) {
			generateSubelementHandler(ans);
		}

		ans.constructor.addArg(new Variable("javax.xml.stream.XMLStreamReader", "reader"));

		/*
		 * No deep reason for this. We simply have not implemented the Node
		 * (de)serialization for subelements because nothing needs it.
		 */
		if (elements.isEmpty()) {
			ans.addMethod(generateElementSerializer());
			ans.addMethod(getNodeConstructor());
			ans.addFieldDecl(FieldDecl.loggerDecl());
		}

		ans.generate(srcRoot);
	}

	public String getXmlTag() {
		return name;
	}

	public CodeBlock generateCallback() {
		CodeBlockList ans = new CodeBlockList();

		String[] lines = callback.split("\\\\n");
		for (String line : lines) {
			line = line.replace("{}", "new %s(reader)".formatted(getClassName()));
			line = line.strip();
			if (line.isBlank()) {
				continue;
			}
			if (!line.endsWith(";")) {
				line = line + ";";
			}
			ans.addContent(new Line(line));
		}

		return ans;
	}

}
