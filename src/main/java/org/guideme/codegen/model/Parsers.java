package org.guideme.codegen.model;

import java.io.File;
import java.io.IOException;

import org.guideme.codegen.code_builder.CodeBlock;
import org.guideme.codegen.code_builder.CodeBlockList;
import org.guideme.codegen.code_builder.CodeBuilder;
import org.guideme.codegen.code_builder.CodeFile;
import org.guideme.codegen.code_builder.CodeFile.CodeFileType;
import org.guideme.codegen.code_builder.Line;
import org.guideme.codegen.code_builder.Method;
import org.guideme.codegen.code_builder.StringSwitch;
import org.guideme.codegen.code_builder.Type;
import org.guideme.codegen.code_builder.Variable;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.ui.debug_shell.DebugShell;
import org.guideme.guideme.util.XMLReaderUtils;

public class Parsers {
	private Parsers() {

	}

	public static void generateParsersClass(String[] packageName, File srcRoot, Model model)
			throws IOException {
		CodeFile ans = new CodeFile(CodeFileType.CLASS, packageName, "Parsers");

		ans.addMethod(generateParseElement(model));

		ans.generate(srcRoot);
	}

	private static Method generateParseElement(Model model) {
		Method ans = new Method(new Type("void"), "parseElement");
		ans.makeStatic();

		ans.addArg(new Variable("org.guideme.guideme.readers.xml_guide_reader.ParserState",
				"parseState"));

		ans.addCodeBlock(
				Line.getFinalAssignment(new Variable("javax.xml.stream.XMLStreamReader", "reader"),
						"parseState.getReader()"));
		ans.addCodeBlock(Line.getFinalAssignment(
				new Variable("org.guideme.guideme.model.Chapter", "chapter"),
				"parseState.getChapter()"));
		ans.addCodeBlock(Line.getFinalAssignment(
				new Variable("org.guideme.guideme.settings.GuideSettings", "guideSettings"),
				"parseState.getGuideSettings()"));
		ans.addCodeBlock(Line.getFinalAssignment(
				new Variable("org.guideme.guideme.model.Guide", "guide"), "parseState.getGuide()"));
		ans.addCodeBlock(Line.getFinalAssignment(
				new Variable("org.guideme.guideme.model.Page", "page"), "parseState.getPage()"));
		ans.addCodeBlock(Line.getFinalAssignment(
				new Variable("org.guideme.guideme.ui.debug_shell.DebugShell", "debugShell"),
				"parseState.getDebugShell()"));
		ans.addCodeBlock(Line.getFinalAssignment(
				new Variable("org.guideme.guideme.settings.AppSettings", "appSettings"),
				"parseState.getAppSettings()"));
		ans.addCodeBlock(Line.getFinalAssignment(new Variable("java.lang.String", "presName"),
				"parseState.getPresName()"));
		ans.addCodeBlock(Line.getFinalAssignment(new Variable("java.lang.String", "tagName"),
				"reader.getName().getLocalPart()"));

		StringSwitch mainSwitch = new StringSwitch("tagName");
		for (Element elem : model.getTopLevelElements()) {
			mainSwitch.addCase(elem.getXmlTag(), elem.generateCallback());
		}
		CodeBlockList tagNotFoundHandler = new CodeBlockList();
		tagNotFoundHandler.addContent(new Line(
				"logger.warn(\"Unhandled tag '{}' at location \\n{}\", tagName, reader.getLocation());"));
		tagNotFoundHandler.addContent(new Line("XMLReaderUtils.getStringContentUntilElementEnd(reader);"));
		mainSwitch.setDefault(tagNotFoundHandler);
		
		ans.addCodeBlock(mainSwitch);
		
		return ans;
	}

}
