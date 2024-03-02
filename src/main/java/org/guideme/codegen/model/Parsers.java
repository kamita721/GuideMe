package org.guideme.codegen.model;

import java.io.File;
import java.io.IOException;

public class Parsers {
	private Parsers() {
		
	}
	
	public static void generateParsersClass(String[] packageName, File srcRoot, Model model) throws IOException {
		CodeBuilder ans = new CodeBuilder(packageName, "Parsers");

		generateParseElement(ans, model);

		ans.generate(srcRoot);
	}

	private static void generateParseElement(CodeBuilder builder, Model model) {
		builder.addImport("javax.xml.stream.XMLStreamReader");
		builder.addImport("java.io.IOException");
		builder.addImport("javax.xml.stream.XMLStreamException");
		builder.addImport("org.guideme.guideme.model.Chapter");
		builder.addImport("org.guideme.guideme.model.Guide");
		builder.addImport("org.guideme.guideme.model.Page");
		builder.addImport("org.guideme.guideme.readers.xml_guide_reader.ParserState");
		builder.addImport("org.guideme.guideme.settings.GuideSettings");
		builder.addImport("org.guideme.guideme.ui.debug_shell.DebugShell");
				
		builder.addLine("public static void parseElement(ParserState parseState) throws XMLStreamException, IOException {");
		
		builder.addLine("final XMLStreamReader reader = parseState.getReader();");
		builder.addLine("final Chapter chapter = parseState.getChapter();");
		builder.addLine("final GuideSettings guideSettings = parseState.getGuideSettings();");
		builder.addLine("final Guide guide = parseState.getGuide();");
		builder.addLine("final Page page = parseState.getPage();");
		builder.addLine("final DebugShell debugShell = parseState.getDebugShell();");
		builder.addLine("final AppSettings appSettings = parseState.getAppSettings();");
		builder.addLine("final String presName = parseState.getPresName();");
		builder.addLine();
		builder.addLine("String tagName = reader.getName().getLocalPart();");
		
		builder.addLine("switch(tagName){");
		for(Element elem : model.getTopLevelElements()) {
			builder.addLine("case \"%s\":", elem.getXmlTag());
			elem.generateCallback(builder);
			builder.addLine("break;");
		}
		builder.addLine("}");
		
		builder.addLine("}"); //public static void parseElement
		
	}
}
