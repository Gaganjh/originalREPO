package com.manulife.pension.ps.web.taglib.search;


import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>@version 1.0</p>
 * <p> </p>
 * @author unascribed
 * @version 1.0
 */
public class ContentUrlSpecification implements Serializable, Comparable {
    public static interface ParamType {
        public static final String CONTENT = "CONTENT";
        public static final String QUERY = "QUERY";
        public static final String CLASS = "CLASS";
    }

    public class Param implements Serializable {
        private String name;
        private String type;

        public Param(String name, String type) {
            this.name= name;
            this.type= type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

    private String contentType;
    private String url;
    private String className = null;
    private Param keyParam;
    private Param parentKeyParam;

    public ContentUrlSpecification(String contentType, String url,
                                   String keyParamName, String keyParamType,
                                   String parentKeyParamName, String parentKeyParamType) {
        this.contentType= contentType;
        this.url= url;
        this.keyParam= new Param(keyParamName, keyParamType);
        this.parentKeyParam= parentKeyParamType != null ? new Param(parentKeyParamName, parentKeyParamType) : null;
    }

    public ContentUrlSpecification(String contentType, String url,
                                   String keyParamName, String keyParamType) {
        this(contentType, url, keyParamName, keyParamType, null, null);
    }

    public String getContentType() {
        return contentType;
    }

    public String getUrl() {
        return url;
    }
    
    public String getClassName() {
    	return className;
	}
	
	public void setClassName(String name){
		className = name;
	}

    public Param getKeyParam() {
        return keyParam;
    }

    public Param getParentParam() {
        return parentKeyParam;
    }

    public boolean equals(Object o) {
        return false;
    }

    public boolean equals(ContentUrlSpecification other) {
        return this.contentType.equals(other.contentType);
    }

    public int compareTo(Object o) {
        ContentUrlSpecification other= (ContentUrlSpecification)o;
        return this.contentType.compareTo(other.contentType);
    }

    public int hashCode() {
        return contentType.hashCode();
    }
}
