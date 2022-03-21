package com.manulife.pension.platform.web.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.manulife.pension.platform.web.PdfConstants;

/**
 * This is a Helper class for creating a org.w3c.Document Object, creating childElements,
 * adding Text Node to the Document Object, etc.
 * @author HArlomte
 *
 */
public class PDFDocument {

	Document document;
	
	/**
	 * Creates a new Document object.
	 * @throws ParserConfigurationException
	 */
	public PDFDocument() throws ParserConfigurationException {
		DocumentBuilder builder = null;
		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = builder.newDocument();
	}
	
	/**
	 * Creates a RootElement which is appended to the "document" instance variable.
	 * @param rootElementName
	 * @return
	 */
	public Element createRootElement(String rootElementName) {
		Element rootElement = createElement(rootElementName);
		document.appendChild(rootElement);
		return rootElement;
	}
	
	/**
	 * Creates a Element object with the given elementName.
	 * @param elementName
	 * @return
	 */
	public Element createElement(String elementName) {
		return document.createElement(elementName);
	}
	
	/**
	 * Appends a childElement to a parentElement.
	 * @param parentElement
	 * @param childElement
	 */
	public void appendElement(Element parentElement, Element childElement) {
		parentElement.appendChild(childElement);
	}
	
	/**
	 * Creates a Text Node with the given name and adds Text Content to it. This Text Node is appended
	 * to the given parentElement.
	 * @param parentElement
	 * @param childNodeName
	 * @param value
	 */
	public void appendTextNode(Element parentElement, String childNodeName, String value) {
		Node childNode = (Node) createElement(childNodeName);
		childNode.setTextContent(replaceNullValue(value));
		parentElement.appendChild(childNode);
	}

    /**
     * Creates a Text Node with the given name and adds Text Content to it. This Text Node is
     * appended to the given parentElement.
     * 
     * @param parentElement
     * @param childNodeName
     * @param value
     */
    public void appendTextNodeWithAttribute(Element parentElement, String childNodeName,
            String value, String attributeName, String attributeValue) {
        Element childNode = (Element) createElement(childNodeName);
        childNode.setAttribute(attributeName, attributeValue);
        childNode.setTextContent(replaceNullValue(value));
        parentElement.appendChild(childNode);
    }

	/**
	 * Returns the Document object.
	 * @return
	 */
	public Document getDocument() {
		return document;
	}
	
    /**
     * This method replaces null value and "null" string with empty string
     * 
     * @param value
     * @return String
     */
    public static String replaceNullValue(String value) {
        String str = PdfConstants.BLANK_STRING;
        if (value != null && value != PdfConstants.NULL_STRING) {
            str = value;
        }
    	return str;
    }
    
}
	
