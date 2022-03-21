package com.manulife.pension.bd.web.util;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.CommonEnvironment;
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
            SystemException se = new SystemException(e, "com.manulife.pension.bd.web.util.Environment", "Environment", "Environment Class can't be instantiated.");
            LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID,se);
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
     * This method used to define the error key which is used by CMA
     * @return String
     *      Returns error key
     */
    public String getErrorKey()
    {
        return getNamingVariable(BDConstants.ERROR_KEY, null);
    }

    /**
     * This method used to define the default report page size
     * @return int
     *      Returns the default report page size
     */
    public String getDefaultPageSize()
    {
        return getStringVariable(BDConstants.DEFAULT_PAGE_SIZE_KEY);
    }

    /**
     * This method used to define the appropriate URL
     * @return int
     *      Returns the URL of the fundsheet depending on the environment
     */
    public String getFundSheetURL() {
    	return getStringVariable(BDConstants.FUNDSHEET_URL);
    }

    /**
     * This method used to define the RMI Report server
     * @return String
     *      Returns the RMI Server name
     */
    public String getRMIServerName() {
        return getNamingVariable(BDConstants.RMI_REPORT_SERVER_NAME, null);
    }

    /**
     * This method used to define the RMI i:File server
     * @return String
     *      Returns the i:file RMI Server name
     */
    public String getRMIiFileServerName() {
        return getNamingVariable(BDConstants.RMI_IFILE_SERVER_NAME, null);
    }

    /**
     * This method used to define the RMI i:File failover server
     * @return String
     *      Returns the i:file RMI Failover Server name
     */
    public String getRMIiFileServerNameFailover() {
        return getNamingVariable(BDConstants.RMI_IFILE_SERVER_NAME_FAILOVER, null);
    }

}
