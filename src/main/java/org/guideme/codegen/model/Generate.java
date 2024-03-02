package org.guideme.codegen.model;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.guideme.guideme.util.XMLReaderUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Generate {

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException {
		args = new String[] {
				"/overflow/Documents/programming/guideme/src/main/resources/org/guideme/codegen/model.xml",
				"/overflow/Documents/programming/guideme/src/main/java/" };
		if (args.length != 2) {
			usage();
			return;
		}
		String modelFile = args[0];
		String srcRoot = args[1];

		DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new File(modelFile));
		Element rootElement = doc.getDocumentElement();
		rootElement.normalize();

		Model model = new Model(rootElement);
		
		model.generate(new File(srcRoot));

	}

	public static void usage() {
		System.out.println("Generate <model.xml> </root/of/source/tree>");
	}

}
