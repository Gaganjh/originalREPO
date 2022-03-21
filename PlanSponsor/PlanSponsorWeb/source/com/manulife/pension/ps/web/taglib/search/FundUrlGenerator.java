package com.manulife.pension.ps.web.taglib.search;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.reference.FundReference;
import com.manulife.pension.reference.Reference;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.searchengine.SearchResult;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>@version 1.0</p>
 * <p> </p>
 * @author unascribed
 * @version 1.0
 */

public class FundUrlGenerator implements UrlGenerator {
    private static Logger logger= Logger.getLogger(FundUrlGenerator.class.getName());

    public FundUrlGenerator() {
    }

    public String generateUrl(Reference reference, Properties properties) {
        return generateUrl(reference, (String)null, properties);
    }
    
    public String generateUrl(Reference reference, Properties properties, SearchResult result) {
        return generateUrl(reference, (String)null, properties);
    }

    public String generateUrl(Reference reference, String baseUrl, Properties properties) {
        FundReference fundReference= (FundReference)reference;

        //System.out.println("in FundUrlGenerator: "+ fundReference.getFundSeries() + " rateType " +fundReference.getRateType());
        String fundSheetURL = null;
        
		try {
			fundSheetURL = FundServiceDelegate.getInstance().getFundSheetURL(
					fundReference.getProductId(),
					fundReference.getFundSeries().trim(),
					fundReference.getType().trim(), fundReference.getId(),
					properties.getProperty("contractDefaultClassCD"),
					Environment.getInstance().getSiteLocation());
		} catch (SystemException e) {
			if (logger.isDebugEnabled()) {
			logger.debug("Unable to generate a fund sheet URL ", e);
			}
		}
		return fundSheetURL;
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
