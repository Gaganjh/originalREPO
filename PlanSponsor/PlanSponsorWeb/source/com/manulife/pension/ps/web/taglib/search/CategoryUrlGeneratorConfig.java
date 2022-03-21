package com.manulife.pension.ps.web.taglib.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>@version 1.0</p>
 * <p> </p>
 * @author unascribed
 * @version 1.0
 */
public class CategoryUrlGeneratorConfig implements Serializable {
    private static Logger logger= Logger.getLogger(CategoryUrlGeneratorConfig.class);
    private static CategoryUrlGeneratorConfig instance;
    private boolean initialized= false;
    private Map config;

    public CategoryUrlGeneratorConfig() {
    }

    public synchronized static CategoryUrlGeneratorConfig getInstance() {
        if (instance == null) {
            instance= new CategoryUrlGeneratorConfig();
        }

        return instance;
    }

    public CategoryUrlSpecification getSpecification(String contentType, Properties properties) {
        initialize();
        try {
            Iterator iSpecification= ((List)config.get(contentType)).iterator();

            while (iSpecification.hasNext()) {
                CategoryUrlSpecification specification= (CategoryUrlSpecification)iSpecification.next();

                if (specification.match(properties)) {
                    return specification;
                }
            }

            return null;
        } catch (NullPointerException noSpecification) {
            return null;
        }
    }

    public void initialize() {
        InputStream configInputStream= getClass().getResourceAsStream("/CategoryUrlGeneratorConfig.xml");
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
            NodeList configElements= configDocument.getElementsByTagName("categoryUrlSpecification");
            Map newConfig= new TreeMap();
            for (int iConfigElement= 0; iConfigElement < configElements.getLength(); iConfigElement++) {
                Element element= (Element)configElements.item(iConfigElement);
                String category= element.getAttribute("category");
                NodeList urls= element.getElementsByTagName("url");
                List specificationList= new ArrayList(urls.getLength());
                for (int iUrl= 0; iUrl < urls.getLength(); iUrl++) {
                    Element urlElement= (Element)urls.item(iUrl);
                    String url= urlElement.getAttribute("href");

                    CategoryUrlSpecification specification= new CategoryUrlSpecification(category, url);

                    NodeList conditionNodes= urlElement.getElementsByTagName("condition");
                    if (conditionNodes.getLength() != 0) {
                        Element conditionElement= (Element)conditionNodes.item(0);

                        specification.setCondition(conditionElement.getAttribute("operator"),
                                conditionElement.getAttribute("property"),
                                conditionElement.getAttribute("value"));
                    }
                    specificationList.add(specification);
                }

                newConfig.put(category, specificationList);
            }
            config= newConfig;
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
        		if(configInputStream != null){
				configInputStream.close();
        		}
			} catch (IOException ioException) {
				if (logger.isDebugEnabled()) {
	                logger.debug("Unable to initialize", ioException);
	            }
			}
        }
    }
}

