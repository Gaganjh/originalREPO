package com.manulife.pension.ps.web.taglib.search;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.reference.CategoryReference;
import com.manulife.pension.reference.Reference;
import com.manulife.pension.service.searchengine.SearchResult;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>@version 1.0</p>
 * <p> </p>
 * @author unascribed
 * @version 1.0
 */
public class CategoryUrlGenerator implements UrlGenerator {
    private static Logger logger= Logger.getLogger(CategoryUrlGenerator.class.getName());

    public CategoryUrlGenerator() {
    }

    public String generateUrl(Reference reference, Properties properties) {
        return generateUrl(reference, (String)null, properties, null);
    }
    
    public String generateUrl(Reference reference, String baseUrl, Properties properties) {
        return generateUrl(reference, baseUrl, properties, null);
    }
    
    public String generateUrl(Reference reference, Properties properties, SearchResult result) {
        return generateUrl(reference, (String)null, properties, result);
    }

    public String generateUrl(Reference reference, String baseUrl, Properties properties, SearchResult result) {
        CategoryReference categoryReference= (CategoryReference) reference;

        if (logger.isDebugEnabled()) {
            logger.debug("Generating URL for category:"+categoryReference.getCategory());
        }
        CategoryUrlSpecification specification= CategoryUrlGeneratorConfig.getInstance().getSpecification(categoryReference.getCategory(), properties);
        if (specification == null) {
            return "javascript:alert('The system was unable to generate proper link for this element!');";
        }
        try {
            PSContext psContext= new PSContext();
            Hashtable queryParams= new Hashtable();
            Hashtable contentIds= new Hashtable();

            //String enrollmentState= properties.getProperty("userprofile.enrollmentState", ""+SecurityConstants.NON_ENROLLED);
            //int iEnrollmentState= Integer.parseInt(enrollmentState);
            //contentIds.put(SecurityConstants.ENROLLED_RESULT, iEnrollmentState == SecurityConstants.ENROLLED ? "Y" : "N");

            return psContext.makeCURL(specification.getUrl(), contentIds, queryParams, null ,result);
        } catch (SystemException aException) {
        	//the url could not be generated, but we want the page to continue displaying
            if (logger.isDebugEnabled()) {
            	logger.debug("Unable to generate URL", aException);
        	}
            return "javascript:alert('The system was unable to generate proper link for this element!');";
        }
    }
    

    public URL generateUrl(Reference reference, URL url, Properties properties) {
        try {
            return new URL(generateUrl(reference, url.toString(), properties));
        } catch (MalformedURLException malformedUrl) {
            if (logger.isDebugEnabled()) {
            	logger.debug("Unable to generate a URL for the specified reference!", malformedUrl);
        	}
            return null;
        }
    }
}

