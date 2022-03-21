package com.manulife.pension.platform.web.cache;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.view.MutableFootnote;
import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.util.cache.RetryPolicy;
import com.manulife.pension.ireports.util.propertymanager.PropertyManager;
import com.manulife.pension.util.content.Footnotes;

/**
 * This class maintains the Footnote cache.
 * 
 * @author harlomte
 * 
 */
public class FootnoteCacheImpl implements FootnoteCache {

    private static FootnoteCacheImpl instance = null;

    private static Map<String, Map<String, MutableFootnote>> footnotesByCompany = null;
    
    private static int EXPIRY_TIMEOUT_IN_MS = 86400000; // 24 hours

    private static final String DATA_REPOSITORY_TIMEOUT_PROPERTY_KEY = "datarepository.expiry.timeout.in.ms";

    private long expireTime;

    private RetryPolicy retryPolicy;

    static {
        loadProperties();
    }

    /**
     * This method sets the Expiry timeout.
     */
    private static void loadProperties() {
        EXPIRY_TIMEOUT_IN_MS = PropertyManager.getInt(DATA_REPOSITORY_TIMEOUT_PROPERTY_KEY,
                86400000);
    }

    /**
     * Get the RetryPolicy object.
     * 
     * @return
     */
    RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    /**
     * Get the expireTime.
     * 
     * @return
     */
    private long getExpireTime() {
        return expireTime;
    }

    /**
     * Set the expiry time.
     * 
     * @param expireTime
     */
    private void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * This method checks if the current cache is expired or not. This would mean, we need to reload
     * the cache.
     * 
     * @return - true, if the current cache is expired.
     */
    private boolean hasExpired() {
        return System.currentTimeMillis() > instance.getExpireTime();
    }

    /**
     * Get a instance of ReportDataRepository object.
     * 
     * @return
     */
    public static FootnoteCache getInstance() {
        if (instance == null) {
            instance = new FootnoteCacheImpl();
        }
        return instance;
    }

    /**
     * Retrieve All Footnotes. It looks like the cache was needed here. There is no proper way of
     * getting the Footnotes given a symbol. Hence, all the FundFootnotes are being retrieved and
     * stored in cache.
     * 
     * @return
     */
    public Map<String, Map<String, MutableFootnote>> getFootnotes() {
        
        if (footnotesByCompany == null || instance.hasExpired()
                && instance.getRetryPolicy().canRetry()) {
            try {
                reloadFootnotes();
            } catch (RuntimeException e) {
                if (footnotesByCompany == null) { // first time in
                    throw e;
                } else { // has expired continue to use existing one.
                    System.err
                            .println("WARNING: Time cached has expired but not able to get new data.");
                    e.printStackTrace(System.err);
                    if (instance.getRetryPolicy().isTimeToRetry()) {
                        instance.getRetryPolicy().retry();
                    }
                }
            }
        }
        if (footnotesByCompany != null) {
            RetryPolicy policy = instance.getRetryPolicy();
            if (policy.isExceedMaxRetry()) {
                System.err
                        .println("WARNING: Unable to retrieve new data. Time cached has expired and maximum retry count ["
                                + policy.getMaxRetryCount()
                                + "] has reached. Continue to reuse potentially stale data.");
            }
        }
        return footnotesByCompany;
    }

    /**
     * This method reloads the Footnotes. This method retrieves the Footnotes from CMA and puts it
     * in cache.
     */
    @SuppressWarnings("unchecked")
    private void reloadFootnotes() {
        footnotesByCompany = new HashMap<String, Map<String, MutableFootnote>>();

        try {
            Map<String, MutableFootnote> footnotesUSAMap = new LinkedMap();
            MutableFootnote[] footnotesUSA = retrieveFootnotes(Location.USA);
            if (footnotesUSA != null) {
                for (MutableFootnote footnoteUSA : footnotesUSA) {
                    footnotesUSAMap.put(footnoteUSA.getSymbol().trim(), footnoteUSA);
                }
            }
            footnotesByCompany.put(StandardReportsConstants.COMPANY_ID_USA, footnotesUSAMap);

          Map<String, MutableFootnote> footnotesNYMap = new LinkedMap();
            MutableFootnote[] footnotesNY = retrieveFootnotes(Location.NEW_YORK);
            if (footnotesNY != null) {
                for (MutableFootnote footnoteNY : footnotesNY) {
                    footnotesNYMap.put(footnoteNY.getSymbol().trim(), footnoteNY);
                }
            }
            footnotesByCompany.put(StandardReportsConstants.COMPANY_ID_NY, footnotesNYMap);
        } catch (ContentException e) {
            throw new NestableRuntimeException("Problem retrieving data in getFootnotes in "
                    + getClass().getName(), e);
        }
        
        this.retryPolicy = new RetryPolicy();
        instance.setExpireTime(System.currentTimeMillis() + EXPIRY_TIMEOUT_IN_MS);
    }

    /**
     * Retrieve Footnotes specific to a given location.
     * 
     * @param location
     * @return - MutableFootnote[]
     * @throws ContentException
     */
    private MutableFootnote[] retrieveFootnotes(Location location) throws ContentException {
        MutableFootnote[] footnotes = (MutableFootnote[]) BrowseServiceDelegate.getInstance()
                .findContent(ContentTypeManager.instance().FOOTNOTE, location);
        return footnotes == null ? new MutableFootnote[0] : footnotes;
    }
    

    @SuppressWarnings("unchecked")
    public Footnote[] sortFootnotes(String[] symbols, String companyId) {

    	// if cache is null or the time is expired, then reload the cache
    	if (footnotesByCompany == null || instance.hasExpired()
                && instance.getRetryPolicy().canRetry()) {
    		getFootnotes();
    	}

    	// set to default, if company id is null
    	if (StringUtils.isBlank(companyId)) {
    		companyId = StandardReportsConstants.COMPANY_ID_USA;
    	}
    	
    	// get the footnotes for the specified company id
    	Map<String, MutableFootnote> footNotes = footnotesByCompany.get(companyId);
    	
		HashMap<String, String> hash = new HashMap<String, String>();
		HashMap<String, Footnote> hashNotes = new HashMap<String, Footnote>();
		
		for( int i = 0; i < symbols.length; i++ ){
			hash.put( symbols[i], symbols[i] );
		}
		
		Set<String> keySet = footNotes.keySet();
		Iterator<String> it = keySet.iterator();
		String symbol = StringUtils.EMPTY;
		while(it.hasNext()) {
			symbol = it.next();
			if ( hash.containsKey(symbol)){
				hashNotes.put(symbol, footNotes.get(symbol));
			}
		}

		// convert the Map to array and sort it
		Footnote[] notes = (Footnote[])hashNotes.values().toArray(new Footnote[0]);
		Arrays.sort( notes, Footnotes.getInstance() );
		
		return notes;
	}
}
