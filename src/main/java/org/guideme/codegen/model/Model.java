package org.guideme.codegen.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Model {

	private final ArrayList<AttributeSet> toplevelAttributeSets = new ArrayList<>();
	private final ArrayList<Element> toplevelElements = new ArrayList<>();

	public Model(Node root) {
		if (!root.getNodeName().equals("model")) {
			throw new IllegalStateException("Invalid model.xml. Root element must be <model>.");
		}

		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String tagName = n.getNodeName();
				switch (tagName) {
				case "attributes":
					loadAttributes(n);
					break;
				case "elements":
					loadElements(n);
					break;
				default:
					throw new IllegalStateException("Unexpected top level element name " + tagName);
				}
			}
		}
	}
	
	public Element[] getTopLevelElements() {
		return toplevelElements.toArray(new Element[] {});
	}

	private void loadAttributes(Node attributesRoot) {
		NodeList nl = attributesRoot.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String tagName = n.getNodeName();
				switch (tagName) {
				case "attribute":
					Attribute attr = Attribute.getAttribute(n);
					toplevelAttributeSets.add(new AttributeSet(attr));
					break;
				case "attributeSet":
					toplevelAttributeSets.add(AttributeSet.get(n));
					break;
				default:
					throw new IllegalStateException(
							"Unexpected tag " + tagName + " under <attributes>");
				}
			}
		}
	}

	private void loadElements(Node elementsRoot) {
		NodeList nl = elementsRoot.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				String tagName = n.getNodeName();
				if (tagName.equals("element")) {
					toplevelElements.add(new Element(n));
				} else {
					throw new IllegalStateException(
							"Unexpected tag " + tagName + " under <elements>");

				}
			}
		}
	}

	public void generate(File srcRoot) throws IOException {
		String[] packageName = new String[] {"org", "guideme", "generated", "model"};
		for(AttributeSet attrs : toplevelAttributeSets) {
			attrs.generateInterface(srcRoot, packageName);
		}
		for(Element elem : toplevelElements) {
			elem.generateClass(srcRoot, packageName);
		}
		Parsers.generateParsersClass(packageName, srcRoot, this);
	}
}
