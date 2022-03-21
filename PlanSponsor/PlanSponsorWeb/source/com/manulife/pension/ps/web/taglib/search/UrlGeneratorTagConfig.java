package com.manulife.pension.ps.web.taglib.search;



import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.util.log.LogUtility;

/**
 * This class is used to load configuration information for the
 * {@link UrlGeneratorTag UrlGeneratorTag}.
 * <p>
 * This class instanciate the singleton design pattern.
 *
 */
public class UrlGeneratorTagConfig {
    static class PropertyDefinition {
        private String propertyName;
        private HttpRetriever retriever;

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName= propertyName;
        }

        public HttpRetriever getRetriever() {
            return retriever;
        }

        public void setRetriever(HttpRetriever retriever) {
            this.retriever= retriever;
        }

        public String getValue(HttpSession session, HttpServletRequest request) {
            return retriever.getValue(session, request);
        }
    }

    static abstract class HttpRetriever {
        private String attributeName;
        private String attributePropertyName;

        public abstract String getValue(HttpSession session, HttpServletRequest request);

        public HttpRetriever(String attributeName, String attributePropertyName) {
            this.attributeName= attributeName;
            this.attributePropertyName= attributePropertyName;
        }

        public String getAttributeName() {
            return attributeName;
        }
        public String getAttributePropertyName() {
            return attributePropertyName;
        }
    }

    static class SessionRetriever extends HttpRetriever {
        public SessionRetriever(String attributeName, String attributePropertyName) {
            super(attributeName, attributePropertyName);
        }

        public String getValue(HttpSession session, HttpServletRequest request) {
            if (session == null) {
                return null;
            }
            try {
                Object attribute= session.getAttribute(getAttributeName());
                Object value= PropertyUtils.getProperty(attribute, getAttributePropertyName());

                return value.toString();
            }catch (IllegalAccessException ex) {
                String message = "Unable to access the \""+getAttributePropertyName()+"\" property in session attribute \""+getAttributeName()+"\""+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "SessionRetriever.getValue", message));
            }catch (InvocationTargetException ex) {
                String message = "Unable to invoke the \""+getAttributePropertyName()+"\" property in session attribute \""+getAttributeName()+"\""+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "SessionRetriever.getValue", message));
            }catch (NoSuchMethodException ex) {
                String message = "The \""+getAttributePropertyName()+"\" property does not exists in session attribute \""+getAttributeName()+"\""+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "SessionRetriever.getValue", message));
            }
            return "";
        }
    }

    static class RequestRetriever extends HttpRetriever {
        public RequestRetriever(String attributeName, String attributePropertyName) {
            super(attributeName, attributePropertyName);
        }

        public String getValue(HttpSession session, HttpServletRequest request) {
            if (request == null) {
                return null;
            }
            try {
                Object attribute= request.getAttribute(getAttributeName());
                Object value= PropertyUtils.getProperty(attribute, getAttributePropertyName());

                return value.toString();
            }catch (IllegalAccessException ex) {
                String message = "Unable to access the \""+getAttributePropertyName()+"\" property in request attribute \""+getAttributeName()+"\"" + ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "RequestRetriever.getValue", message));
            }catch (InvocationTargetException ex) {
                String message = "Unable to invoke the \""+getAttributePropertyName()+"\" property in request attribute \""+getAttributeName()+"\"" + ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "RequestRetriever.getValue", message));
            }catch (NoSuchMethodException ex) {
                String message = "The \""+getAttributePropertyName()+"\" property does not exists in request attribute \""+getAttributeName()+"\"" + ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "RequestRetriever.getValue", message));
            }
            return "";
        }
    }
    private static Logger logger= Logger.getLogger(UrlGeneratorTagConfig.class);
    private static UrlGeneratorTagConfig instance;

    private boolean initialized= false;
    private List propertyDefinitions;

    private UrlGeneratorTagConfig() {
    }

    public synchronized static UrlGeneratorTagConfig getInstance() {
        if (instance == null) {
            instance= new UrlGeneratorTagConfig();
        }

        return instance;
    }

    public Properties getProperties(HttpSession session, HttpServletRequest request) {
        initialize();
        Properties properties= new Properties();
        for (Iterator iPropertyDefinition= propertyDefinitions.iterator(); iPropertyDefinition.hasNext();) {
            PropertyDefinition propertyDefinition= (PropertyDefinition)iPropertyDefinition.next();
            String propertyValue= propertyDefinition.getValue(session, request);
            if (propertyValue != null) {
                properties.setProperty(propertyDefinition.getPropertyName(), propertyValue);
            }
        }

        return properties;
    }

    public void initialize() {
        initialize(getClass().getResourceAsStream("/UrlGeneratorTagConfig.xml"));
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
            NodeList configElements= configDocument.getElementsByTagName("property");
            propertyDefinitions= new ArrayList(configElements.getLength());

            for (int iConfigElement= 0; iConfigElement < configElements.getLength(); iConfigElement++) {
                Element propertyElement= (Element)configElements.item(iConfigElement);
                String propertyName= propertyElement.getAttribute("name");
                PropertyDefinition propertyDefinition= new PropertyDefinition();
                propertyDefinition.setPropertyName(propertyName);

                NodeList sessionElements= propertyElement.getElementsByTagName("session");
                if (sessionElements.getLength() > 0) {
                    Element sessionElement= (Element)sessionElements.item(0);
                    SessionRetriever sessionRetriever= new SessionRetriever(sessionElement.getAttribute("attribute"),
                            sessionElement.getAttribute("property"));
                    propertyDefinition.setRetriever(sessionRetriever);
                }
                NodeList requestElements= propertyElement.getElementsByTagName("request");
                if (requestElements.getLength() > 0) {
                    Element requestElement= (Element)requestElements.item(0);
                    RequestRetriever requestRetriever= new RequestRetriever(requestElement.getAttribute("attribute"),
                            requestElement.getAttribute("property"));
                    propertyDefinition.setRetriever(requestRetriever);
                }

                propertyDefinitions.add(propertyDefinition);
            }
        } catch (ParserConfigurationException ex) {
            String message = "Unable to initialize parser" + ex;
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "initialize", message));
        } catch (FactoryConfigurationError ex) {
            String message = "Unable to initialize parser" + ex;
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "initialize", message));
        } catch (IOException ex) {
            String message = "Unable to read input stream" + ex;
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "initialize", message));
        } catch (SAXException ex) {
            String message = "Unable to parse XML stream" + ex;
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "initialize", message));
        }finally{
        	try {
        		if(configInputStream!= null)
        			configInputStream.close();
			} catch (IOException ex) {
				String message = "Unable to read input stream" + ex;
	            LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "UrlGeneratorTagConfig", "initialize", message));
			}
        }

        initialized= true;
    }
}