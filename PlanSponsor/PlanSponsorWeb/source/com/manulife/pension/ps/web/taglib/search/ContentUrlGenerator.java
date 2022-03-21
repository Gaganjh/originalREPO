package com.manulife.pension.ps.web.taglib.search;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.reference.ContentReference;
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
public class ContentUrlGenerator implements UrlGenerator {
    private static Logger logger= Logger.getLogger(ContentUrlGenerator.class.getName());

    public ContentUrlGenerator() {
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
        ContentReference contentReference= (ContentReference) reference;

        // standalonetools URLs are never used from the content table.

        if (!"StandAloneTool".equals(contentReference.getContentType()) && contentReference.getUrl() != null) {
            return contentReference.getUrl();
        }
		//add here code to get content for how-to, pdf forms, admin guides, legislative guides
        if (logger.isDebugEnabled()) {
            logger.debug("Generating URL for contentType:"+contentReference.getContentType()+", key:"+contentReference.getKey()+", parentKey:"+contentReference.getParentKey());
        }
        ContentUrlSpecification specification= ContentUrlGeneratorConfig.getInstance().getSpecification(contentReference.getContentType());
        if (specification == null) {
            return "javascript:alert('The system was unable to generate proper link for this element!');";
        }
        try {
            PSContext psContext= new PSContext();
            Hashtable queryParams= new Hashtable();
            Hashtable contentIds= new Hashtable();
            ContentUrlSpecification.Param keyParam= specification.getKeyParam();
            ContentUrlSpecification.Param parentKeyParam= specification.getParentParam();
            String className = specification.getClassName();
            

            if (keyParam != null) {
                if (ContentUrlSpecification.ParamType.CONTENT.equals(keyParam.getType())) {
                    contentIds.put(keyParam.getName(), contentReference.getKey());
                } else if (ContentUrlSpecification.ParamType.QUERY.equals(keyParam.getType())) {
                    queryParams.put(keyParam.getName(), contentReference.getKey().toString());
                }
            }
            if (parentKeyParam != null) {
                if (ContentUrlSpecification.ParamType.CONTENT.equals(parentKeyParam.getType())) {
                    contentIds.put(parentKeyParam.getName(), contentReference.getParentKey());
                } else if (ContentUrlSpecification.ParamType.QUERY.equals(parentKeyParam.getType())) {
                    queryParams.put(parentKeyParam.getName(), contentReference.getParentKey().toString());
                }
            }

            //String enrollmentState= properties.getProperty("userprofile.enrollmentState", ""+SecurityConstants.NON_ENROLLED);
            //int iEnrollmentState= Integer.parseInt(enrollmentState);
            //contentIds.put(SecurityConstants.ENROLLED_RESULT, iEnrollmentState == SecurityConstants.ENROLLED ? "Y" : "N");
			
			return psContext.makeCURL(specification.getUrl(), contentIds, queryParams, className, result);

        } catch (SystemException templateContextException) {
            logger.debug("Unable to generate URL", templateContextException);
            return null;
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

