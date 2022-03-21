package com.manulife.pension.ps.web.taglib.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.manulife.pension.reference.Reference;

/**
 * Provide access to the TitleGenerator configuration. This class will
 * load the configration from an XML file.
 *
 * @author Emmanuel Pirsch
 */
public class TitleGeneratorConfig implements Serializable {
    private static final Logger logger= Logger.getLogger(TitleGeneratorConfig.class);
    private static TitleGeneratorConfig instance;

    private boolean initialized= false;
    private Map config;

    private TitleGeneratorConfig() {
    }

    public static synchronized TitleGeneratorConfig getInstance() {
        if (instance == null) {
            instance= new TitleGeneratorConfig();
        }

        return instance;
    }

    public TitleSpecification getSpecification(String contentType, Reference reference, Properties properties) {
        initialize();
        try {
            Iterator iSpecification= ((List)config.get(contentType)).iterator();

            while (iSpecification.hasNext()) {
                TitleSpecification specification= (TitleSpecification)iSpecification.next();

                if (specification.match(properties, reference)) {
                    return specification;
                }
            }

            return null;
        } catch (NullPointerException noSpecification) {
            if (logger.isDebugEnabled()) {
                logger.debug("No specification for specified contentType : "+contentType, noSpecification);
            }
            return null;
        }
    }
    public void initialize() {
        InputStream configInputStream= getClass().getResourceAsStream("/TitleGeneratorConfig.xml");
        initialize(configInputStream);
    }

    public synchronized void initialize(InputStream configInputStream) {
        if (initialized) {
            return;
        }
        try {
            DocumentBuilderFactory documentBuilderFactory= DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            documentBuilderFactory.setIgnoringComments(true);
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);

            Document configDocument= documentBuilderFactory.newDocumentBuilder().parse(configInputStream);
            NodeList configElements= configDocument.getElementsByTagName("title");
            Map newConfig= new TreeMap();
            for (int iConfigElement= 0; iConfigElement < configElements.getLength(); iConfigElement++) {
                Element element= (Element)configElements.item(iConfigElement);
                String contentType= element.getAttribute("contentType");
                String referenceType= element.getAttribute("referenceType");

                if (logger.isDebugEnabled()) {
                    logger.debug("Configuring... contentType: "+contentType+", referenceType: "+referenceType+".");
                }
                TitleSpecification specification= new TitleSpecification(contentType, referenceType);

                NodeList childs= element.getChildNodes();
                for (int iChild= 0; iChild < childs.getLength(); iChild++) {
                    Node node= childs.item(iChild);
                    String nodeName= node.getNodeName();
                    if ("condition".equals(nodeName)) {
                        Element condition= (Element)node;
                        specification.setCondition(condition.getAttribute("operator"),
                                condition.getAttribute("property"),
                                condition.getAttribute("value"));
                    } else if ("searchResult".equals(nodeName)) {
                        Element searchResult= (Element)node;
                        specification.addSearchResultElement(searchResult.getAttribute("property"));
                    } else if ("reference".equals(nodeName)) {
                        Element reference= (Element)node;
                        specification.addReferenceElement(reference.getAttribute("property"));
                    } else if ("content".equals(nodeName)) {
                        Element content= (Element)node;
                        specification.addContentElement(content.getAttribute("key"),
                                content.getAttribute("type"),
                                content.getAttribute("property"),
                                content.getAttribute("method"));
                    } else if ("text".equals(nodeName)) {
                        StringBuffer text= new StringBuffer();
                        NodeList textNodes= node.getChildNodes();
                        for (int iTextNode= 0; iTextNode < textNodes.getLength(); iTextNode++) {
                            Node textNode= textNodes.item(iTextNode);
                            if ("#text".equals(textNode.getNodeName())) {
                                text.append(textNode.getNodeValue());
                            }
                        }
                        specification.addTextElement(text.toString());
                    }
                }

                List specificationList= (List)newConfig.get(contentType);
                if (specificationList == null) {
                    specificationList= new LinkedList();
                    newConfig.put(contentType, specificationList);
                }
                specificationList.add(specification);

            }
            config= newConfig;
            initialized= true;
        } catch (ParserConfigurationException parserConfigurationException)  {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to initialize", parserConfigurationException);
            }
        } catch (IOException ioException) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to initialize", ioException);
            }
        } catch (SAXException saxException) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to initialize", saxException);
            }
        }finally{
        	try {
        		if(configInputStream != null)
        			configInputStream.close();
			} catch (IOException ioException) {
				 if (logger.isDebugEnabled()) {
		                logger.debug("Unable to close the stream", ioException);
		            }
			}
        }
    }
}