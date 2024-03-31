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

/*
 * To recreate the generated code files, edit the hardcoded paths below
 * modelFile should point to model.xml, which describes the generated files to be built.
 * 
 * srcRoot should point to the src/main/java directory of the source tree.
 */
public class Generate {

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException {

		String modelFile = "/overflow/Documents/programming/guideme/src/main/resources/org/guideme/codegen/model.xml";
		String srcRoot = "/overflow/Documents/programming/guideme/src/main/java/";

		DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new File(modelFile));
		Element rootElement = doc.getDocumentElement();
		rootElement.normalize();

		Model model = new Model(rootElement);
		
		model.generate(new File(srcRoot));

	}

}
