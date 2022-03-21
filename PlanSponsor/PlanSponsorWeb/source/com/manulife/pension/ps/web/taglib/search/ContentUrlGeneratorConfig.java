package com.manulife.pension.ps.web.taglib.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <contentUrlSpecifications>
 *    <contentUrlSpecification contentType="bladibla" url="/bladibla">
 *       <keyParam name="keyparam" type="QUERY|CONTENT"/>
 *       <parentKeyParam name="parentkeyparam" type="QUERY|CONTENT"/>
 *    </contentUrlSpecification>
 * </contentUrlSpecifications>
 *
 * @author Emmanuel Pirsch
 * @version 1.0
 */
public class ContentUrlGeneratorConfig implements Serializable {
    private Logger logger= Logger.getLogger(ContentUrlGeneratorConfig.class);
    private static ContentUrlGeneratorConfig instance;
    private boolean initialized= false;
    private Map config;

    private ContentUrlGeneratorConfig() {
    }

    public synchronized static ContentUrlGeneratorConfig getInstance() {
        if (instance == null) {
            instance= new ContentUrlGeneratorConfig();
        }

        return instance;
    }

    public ContentUrlSpecification getSpecification(String contentType) {
        initialize();

        return (ContentUrlSpecification)config.get(contentType);
    }

    public void initialize() {
        InputStream configInputStream= getClass().getResourceAsStream("/ContentUrlGeneratorConfig.xml");
        initialize(configInputStream);
    }

    public synchronized void initialize(InputStream configInputStream) {
        if (initialized) {
            return;
        }
        try {
            Document configDocument= DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(configInputStream);
            NodeList configElements= configDocument.getElementsByTagName("contentUrlSpecification");
            Map newConfig= new TreeMap();
            for (int iConfigElement= 0; iConfigElement < configElements.getLength(); iConfigElement++) {
                Element element= (Element)configElements.item(iConfigElement);
                String contentType= element.getAttribute("contentType");
                String url= element.getAttribute("url");
                Element keyParam= (Element)element.getElementsByTagName("keyParam").item(0);
                Element parentKeyParam= (Element)element.getElementsByTagName("parentKeyParam").item(0);
                Element classParam = (Element)element.getElementsByTagName("classParam").item(0);

                ContentUrlSpecification specification;
                
                if (parentKeyParam == null) {
                    specification= new ContentUrlSpecification(
                            contentType, url,
                            keyParam.getAttribute("name"),
                            keyParam.getAttribute("type"));
                } else {
                    specification= new ContentUrlSpecification(contentType, url,
                            keyParam.getAttribute("name"),
                            keyParam.getAttribute("type"),
                            parentKeyParam.getAttribute("name"),
                            parentKeyParam.getAttribute("type"));
                }
                
                if(classParam != null){
                	specification.setClassName(classParam.getAttribute("name"));
                }

                newConfig.put(contentType, specification);
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
