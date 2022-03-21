package com.manulife.pension.platform.web.util;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.util.BaseEnvironment;

public class CommonEnvironment extends BaseEnvironment {
	private static CommonEnvironment instance = null;
	
	synchronized public static void initialize(CommonEnvironment env) {
		instance = env;
	}
	
	public static CommonEnvironment getInstance() {
		if (instance == null) {
			throw new IllegalStateException("The Enviornment instance is not initialized!");
		}
		return instance;
	}
	
	/**
	 * This constructor makes sure that all the environment added to web.xml
	 * 
	 */
	protected CommonEnvironment() throws Exception {
		super();
	}

	/**
	 * This method used to define the error key which is used by CMA
	 * 
	 * @return String Returns error key
	 */
	public String getErrorKey() {
		return getNamingVariable(CommonConstants.ERROR_KEY, null);
	}


    /**
     * Gets the domain for the website
     *
     * @return String
     */
    public String getSiteDomain() {
        return getNamingVariable(CommonConstants.SITE_DOMAIN, getSiteLocation());
    }
    
	/**
	 * This method used to define the default report page size
	 * 
	 * @return int Returns the default report page size
	 */
	public String getDefaultPageSize() {
		return getStringVariable(CommonConstants.DEFAULT_PAGE_SIZE_KEY);
	}

	/**
	 * This method used to define the appropriate URL
	 * 
	 * @return int Returns the URL of the fundsheet depending on the environment
	 */
	public String getFundSheetURL() {
		return getStringVariable(CommonConstants.FUNDSHEET_URL);
	}

	/**
	 * This method used to define the RMI Report server
	 * 
	 * @return String Returns the RMI Server name
	 */
	public String getRMIServerName() {
		return getNamingVariable(CommonConstants.RMI_REPORT_SERVER_NAME, null);
	}

	/**
	 * This method used to define the RMI i:File server
	 * 
	 * @return String Returns the i:file RMI Server name
	 */
	public String getRMIiFileServerName() {
		return getNamingVariable(CommonConstants.RMI_IFILE_SERVER_NAME, null);
	}

	/**
	 * This method used to define the RMI i:File failover server
	 * 
	 * @return String Returns the i:file RMI Failover Server name
	 */
	public String getRMIiFileServerNameFailover() {
		return getNamingVariable(CommonConstants.RMI_IFILE_SERVER_NAME_FAILOVER, null);
	}


	/**
	 * This method used to define the Company Name
	 * 
	 * @return String Returns the Company Name
	 */
	public String getCompanyName() {
		return getStringVariable(CommonConstants.COMPANY_NAME);
	}
	
    /**
     * This method used to define the site's mode.
     * @return String
     *      returns usa or ny (defined in SecurityConstants)
     */
    public String getSiteLocation()
    {
        return getStringVariable(CommonConstants.SITE_LOCATION);
    }

    /**
     * Gets the protocol for the website
     * 
     * @return String
     */
    public String getSiteProtocol() {
        return getNamingVariable(CommonConstants.SITE_PROTOCOL, null);
    }

    /**
     * The URL for the Data Impact site
     */
    public String getStatementsURL()
    {
        return getNamingVariable(CommonConstants.STATEMENTS_URL, getSiteLocation());
    }

    /**
     * The current domain
     */
    public String getCookieDomain()
    {
        return getNamingVariable(CommonConstants.COOKIE_DOMAIN, getSiteLocation());
    }
    
}
