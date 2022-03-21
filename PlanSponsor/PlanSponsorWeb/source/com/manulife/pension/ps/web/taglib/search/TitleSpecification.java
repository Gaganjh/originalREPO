package com.manulife.pension.ps.web.taglib.search;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.reference.ContentReference;
import com.manulife.pension.reference.Reference;
import com.manulife.pension.service.searchengine.SearchResult;
import com.manulife.pension.util.log.LogUtility;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>@version 1.0</p>
 * <p> </p>
 * @author unascribed
 * @version 1.0
 */
public class TitleSpecification implements Serializable, Comparable {
    private static final Logger logger= Logger.getLogger(TitleSpecification.class);

    private String contentType;
    private String referenceType;
    private Condition condition;
    private List titleElements;

    public static interface Condition extends Serializable {
        boolean match(Properties properties);
    }

    public static class Equals implements Condition {
        private String property;
        private String value;

        public Equals(String property, String value) {
            this.property= property;
            this.value= value;
        }

        public boolean match(Properties properties) {
            if (properties == null) {
                return false;
            }
            String valueToMatch= properties.getProperty(property);

            return value.equals(valueToMatch);
        }
    }

    public static class NotEquals implements Condition {
        private String property;
        private String value;

        public NotEquals(String property, String value) {
            this.property= property;
            this.value= value;
        }

        public boolean match(Properties properties) {
            if (properties == null) {
                return true;
            }
            String valueToMatch= properties.getProperty(property);

            return !value.equals(valueToMatch);
        }
    }

    private static interface TitleElement extends Serializable {
        String getTitle(Map parameters);
    }

    private static class SearchResultElement implements TitleElement {
        private String property;

        public SearchResultElement(String property) {
            this.property= property;
        }

        public String getTitle(Map parameters) {
            try {
                Object searchResult= parameters.get("SearchResultElement");
                return PropertyUtils.getProperty(searchResult, property).toString();
            }
            catch (IllegalAccessException ex) {
                String message = "Unable to access property \""+property+"\" in SearchResult object"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "SearchResultElement.getTitle", message));
                return "";
            }catch (InvocationTargetException ex) {
                String message = "Unable to invoke property \""+property+"\" in SearchResult object"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "SearchResultElement.getTitle", message));
                return "";
            }catch (NoSuchMethodException ex) {
                String message = "Property \""+property+"\" does not exists in SearchResult object"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "SearchResultElement.getTitle", message));
                return "";
            } catch (NullPointerException ex) {
                String message = "Property \""+property+"\" is null in SearchResult object"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "SearchResultElement.getTitle", message));
                return "";
            }
        }
    }

    private static class ReferenceElement implements TitleElement {
        private String property;

        public ReferenceElement(String property) {
            this.property= property;
        }

        public String getTitle(Map parameters) {
            try {
                Object searchResult= parameters.get("ReferenceElement");
                return PropertyUtils.getProperty(searchResult, property).toString();
            }
            catch (IllegalAccessException ex) {
                String message = "Unable to access property \""+property+"\" in Reference object"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "ReferenceElement.getTitle", message));
                return "";
            }catch (InvocationTargetException ex) {
                String message = "Unable to invoke property \""+property+"\" in Reference object"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "ReferenceElement.getTitle", message));
                return "";
            }catch (NoSuchMethodException ex) {
                String message = "Property \""+property+"\" does not exists in Reference object"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "ReferenceElement.getTitle", message));
                return "";
            } catch (NullPointerException ex) {
                String message = "Property \""+property+"\" is null in SearchResult object"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "ReferenceElement.getTitle", message));
                return "";
            }

        }
    }

    private static class ContentElement implements TitleElement {
        private ContentType type;
        private Integer key;
        private String property;
        private String method;

        public ContentElement(String key, String type, String property, String method) {
            try {
                try {
                    Field keyField= ContentConstants.class.getField(key);
                    this.key= new Integer(keyField.getInt(null));
                } catch (NoSuchFieldException ex) {
                	if (logger.isDebugEnabled()) {
            			logger.debug("NoSuchFieldException :: key = " + key);
        			}
                    key= null;
                }
                this.property= property;
                this.type= ContentTypeManager.instance().valueOf(type);
                this.method= method;
            }catch (SecurityException ex) {
                String message = "ContentElement: key \""+key+"\" is not a valid content identifier (it should be present in ContentConstants class)"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "ContentElement.constructor", message));
            }catch (IllegalAccessException ex) {
                String message = "ContentElement: key \""+key+"\" is not a valid content identifier (it should be present in ContentConstants class)"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "ContentElement.constructor", message));
            }catch (IllegalArgumentException ex) {
                String message = "ContentElement: key \""+key+"\" is not a valid content identifier (it should be present in ContentConstants class)"+ ex;
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "TitleSpecification", "ContentElement.constructor", message));
            }
        }

        public String getTitle(Map parameters) {
            try {
                BrowseServiceDelegate browseServiceDelegate= BrowseServiceDelegate.getInstance();

                Object content= null;
                if ("key".equals(method)) {
					if(key != null)
                    content= browseServiceDelegate.findContent(key.intValue(), type);
                } else if ("findContent".equals(method)) {
                	//Force the lookup of content via content key
                	Object refObj = parameters.get("ReferenceElement");
                	if(refObj instanceof ContentReference) {
                		ContentReference ref = (ContentReference)refObj;
                		content= browseServiceDelegate.findContent(ref.getKey().intValue(), type);
                	} 
                
                    
                } else if ("mostRecent".equals(method)){
                    try {
                        content= browseServiceDelegate.getMostRecent(type, 1)[0];
                    } catch (ArrayIndexOutOfBoundsException noContentFound) {
                        content= null;
                    }
                } else if ("parentKey".equals(method)) {
                    Integer parentKey= ((ContentReference)parameters.get("ReferenceElement")).getParentKey();
                    if (parentKey != null) {
                        content= browseServiceDelegate.findContent(parentKey.intValue(), type);
                    }
                } 
                
                if (logger.isDebugEnabled()) {
                    if (content == null) {
                        logger.debug("no content found for key: "+key+", type: "+type+", property: "+property+", method: "+method);
                    }
                }
                
                return content != null ? PropertyUtils.getProperty(content, property).toString() : "";
            }
            catch (IllegalAccessException ex) {
                if (logger.isDebugEnabled()) {
            		logger.debug("Unable to access property \""+property+"\" in Content object", ex);
        		}
                return "";
            }catch (InvocationTargetException ex) {
                if (logger.isDebugEnabled()) {
            		logger.debug("Unable to invoke property \""+property+"\" in Content object", ex);
        		}
                return "";
            }catch (NoSuchMethodException ex) {
                if (logger.isDebugEnabled()) {
            		logger.debug("Property \""+property+"\" does not exists in Content object", ex);
        		}
                return "";
            }catch (ContentException ex) {
                if (logger.isDebugEnabled()) {
            		logger.debug("Property \""+property+"\" does not exists in Content object", ex);
        		}
                return "";
            }
        }
    }

    private static class TextElement implements TitleElement {
        private String text;

        public TextElement(String text) {
            this.text= text;
        }

        public String getTitle(Map parameters) {
            return text;
        }
    }

    public TitleSpecification(String contentType, String referenceType) {
        this.contentType= contentType;
        this.referenceType= referenceType;
        this.titleElements= new LinkedList();
    }

    public String getContentType() {
        return contentType;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public boolean equals(Object o) {
        return false;
    }

    public boolean equals(TitleSpecification other) {
        return this.contentType.equals(other.contentType);
    }

    public int compareTo(Object o) {
        TitleSpecification other= (TitleSpecification)o;
        return this.contentType.compareTo(other.contentType);
    }

    public int hashCode() {
        return contentType.hashCode();
    }

    public boolean match(Properties properties, Reference reference) {
        if (referenceType != null && referenceType.trim().length() > 0) {
            if (!reference.getClass().getName().equals(referenceType)) {
                return false;
            }
        }
        return condition == null ? true : condition.match(properties);
    }

    public void setCondition(String operator, String property, String value) {
        if ("equals".equals(operator)) {
            this.condition= new Equals(property, value);
        } else if ("notEquals".equals(operator)) {
            this.condition= new NotEquals(property, value);
        } else {
            throw new IllegalArgumentException("Operator \""+operator+"\" is not supported");
        }
    }

    public void addSearchResultElement(String property) {
        titleElements.add(new SearchResultElement(property));
    }

    public void addReferenceElement(String property) {
        titleElements.add(new ReferenceElement(property));
    }

    public void addContentElement(String key, String type, String property, String method) {
        titleElements.add(new ContentElement(key, type, property, method));
    }

    public void addTextElement(String text) {
        titleElements.add(new TextElement(text));
    }

    public String getTitle(SearchResult searchResult, Reference reference, String requestUrl) {
        StringBuffer title= new StringBuffer();
        Map parameters= new HashMap();
        parameters.put("SearchResultElement", searchResult);
        parameters.put("ReferenceElement", reference);

        for (Iterator iTitleElement= titleElements.iterator(); iTitleElement.hasNext(); ) {
            TitleElement titleElement= (TitleElement)iTitleElement.next(); 
            title.append(titleElement.getTitle(parameters));
        }


        return title.toString();
    }
}

