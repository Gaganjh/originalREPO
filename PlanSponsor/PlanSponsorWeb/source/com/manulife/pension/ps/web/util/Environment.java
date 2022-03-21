package com.manulife.pension.ps.web.util;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.CommonEnvironment;import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.util.log.LogUtility;

/**
 * This class is used to retrieve and cache all
 * the enviroment varibles. This class is also
 * added to Application context to be easily used in
 * jsps.
 *
 */
public class Environment extends CommonEnvironment
{
    private static Environment instance = null;

    static
    {
        try
        {
            instance = new Environment();
        }
        catch(Exception e)
        {
            SystemException se = new SystemException(e, "com.manulife.pension.ps.web.util.Environment", "Environment", "Environment Class can't be instantiated.");
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
            e.printStackTrace();
        }
    }

    /**
     * This constructor makes sure that all the environment
     * added to web.xml
     *
     */
    private Environment() throws Exception
    {
    	super();
    }

    public static Environment getInstance()
    {
        return instance;
    }

 
    /**
     * This method used to retrieve site location
     *  as is recorded in CSDB database
     * @return String
     *      returns US or NY (defined in SecurityConstants)
     */
    public String getDBSiteLocation()
    {
        if(getSiteLocation().toUpperCase().indexOf("US")>=0){
            return Constants.DB_SITEMODE_USA;
        }
        else{
            return Constants.DB_SITEMODE_NY;
        }
    }

    /**
     * This method used to define the error key which is used by CMA
     * @return String
     *      Returns error key
     */
    public String getErrorKey()
    {
        return getNamingVariable(Constants.ERROR_KEY, null);
    }

    /**
     * This method used to define the default report page size
     * @return int
     *      Returns the default report page size
     */
    public String getDefaultPageSize()
    {
        return getStringVariable(Constants.DEFAULT_PAGE_SIZE_KEY);
    }

    /**
     * This method used to define the appropriate URL
     * @return int
     *      Returns the URL of the fundsheet depending on the environment
     */
    public String getFundSheetURL() {
    	return getStringVariable(Constants.FUNDSHEET_URL);
    }

    /**
     * This method used to define the RMI Report server
     * @return String
     *      Returns the RMI Server name
     */
    public String getRMIServerName() {
        return getNamingVariable(Constants.RMI_REPORT_SERVER_NAME, null);
    }

    /**
     * This method used to define the RMI i:File server
     * @return String
     *      Returns the i:file RMI Server name
     */
    public String getRMIiFileServerName() {
        return getNamingVariable(Constants.RMI_IFILE_SERVER_NAME, null);
    }

    /**
     * This method used to define the RMI i:File failover server
     * @return String
     *      Returns the i:file RMI Failover Server name
     */
    public String getRMIiFileServerNameFailover() {
        return getNamingVariable(Constants.RMI_IFILE_SERVER_NAME_FAILOVER, null);
    }

    /**
     * This method used to define the RMI Submission Journal server
     * @return String
     *      Returns the Submission Journal RMI Server name
     */
    public String getRMISubmissionJournalServerName() {
        return getNamingVariable(Constants.RMI_SUBMISSION_JOURNAL_SERVER_NAME, null);
    }

    /**
     * This method used to define the RMI primary STP server
     * @return String
     *      Returns the STP RMI Server name
     */
    public String getRMISTPServerNamePrimary() {
        return getNamingVariable(Constants.RMI_STP_SERVER_NAME_PRIMARY, null);
    }

    /**
     * This method used to define the RMI failover STP server
     * @return String
     *      Returns the STP RMI Server name
     */
    public String getRMISTPServerNameFailover() {
        return getNamingVariable(Constants.RMI_STP_SERVER_NAME_FAILOVER, null);
    }
    
    /**
     * Gets the count of days for a notification to be considered recent
     * since the last user's login date
     *
     * @return int
     */
    public int getNotificationRecentDayCount() {
        return Integer.parseInt(getStringVariable(Constants.NOTIFICATION_RECENT_DAY_COUNT_URI));
    }

    /**
     * Gets the maximum number of recent alerts to be displayed on the home page
     *
     * @return int
     */
    public int getMaxRecentAlertCount() {
        return Integer.parseInt(getStringVariable(Constants.MAX_RECENT_ALERT_COUNT_URI));
    }

    /**
     * Gets the maximum number of recent notifications to be displayed on the home page
     *
     * @return int
     */
    public int getMaxRecentNotificationCount() {
        return Integer.parseInt(getStringVariable(Constants.MAX_RECENT_NOTIFICATION_COUNT_URI));
    }

    /**
     * This method used to define the Company Name 
     * @return String
     *      Returns the Company Name
     */
    public String getCompanyName() {
        return getStringVariable(Constants.COMPANY_NAME);
    }

    /**
     * Gets the maximum size of upload file
     *
     * @return int
     */
    public int getMaxUploadFileSizeKbytes() {
        return getIntVariable(Constants.MAX_FILE_UPLOAD_SIZE_KBYTES, 5120);
    }

    /**
     * This method tells you if it is USA site
     * @return boolean
     */
    public boolean isUSA() {
        return getDBSiteLocation().equals(Constants.DB_SITEMODE_USA);
    }

    /**
     * This method tells you if it is NY site
     * @return boolean
     */
    public boolean isNY() {
        return !getDBSiteLocation().equals(Constants.DB_SITEMODE_USA);
    }

    public boolean isCMANewsLetterAvailable() {
        return getStringVariable(Constants.CMA_NEWSLETTER_AVAILABLE).equals("true");
    }

    public int getMaxNumberOfUsers() {
        return getIntNamingVariable(Constants.MAX_NUMBER_OF_USERS, getSiteLocation(), 100);      
    }
    
    public String getTPAOtherSiteMarketingURL() {
        return getStringVariable(Constants.TPA_OTHER_SITE_MARKETING_URL);       
    }

    public String getPSOtherSiteMarketingURL() {
        return getStringVariable(Constants.PLANSPONSOR_OTHER_SITE_MARKETING_URL);       
    }

    public String getGeneralCARPhoneNumber() {
        return getStringVariable(Constants.GENERAL_CAR_PHONE_NUMBER);
    }
    
    public String getTedFileDownloadDirectory() {
        return getNamingVariable(Constants.TED_FILE_DOWNLOAD_DIRECTORY, null);
    }

    /**
     * This method used to define the EStatements report's page size
     * @return int
     *      Returns the Estatements report page size
     */
    public String getEStatementsPageSize()
    {
        return getStringVariable(Constants.ESTATEMENTS_PAGE_SIZE_KEY);
    }
    
    public boolean getConfirmTPAeDownloads() {      
        return getStringVariable(Constants.CONFIRM_TPA_E_DOWNLOADS).equals("true");
    }
    
    public String getLearningCenterUrl() {      
    	 return getNamingVariable(Constants.LEARNING_CENTER_URL, null);
    }
    
}
