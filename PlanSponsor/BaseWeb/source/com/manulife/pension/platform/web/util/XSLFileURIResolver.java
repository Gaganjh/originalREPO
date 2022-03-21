package com.manulife.pension.platform.web.util;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

/**
 * Class to resolve included XSL files URI
 * 
 * @author Ramkumar
 *
 */
public class XSLFileURIResolver implements URIResolver {
    
    private String base;
    
    /**
     * Constructor
     * @param base
     */
    public XSLFileURIResolver(String base) {
        this.base = base;
    }

    /**
     * @See URIResolver#resolve()
     * This method resolves URI of included XSL files.
     */
    public Source resolve(String href, String base) {
    	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.base + href);
        Source source = new StreamSource(inputStream);
        return source;
    }

}
