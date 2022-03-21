package com.manulife.pension.bd.web.bob.blockOfBusiness.util;

import java.net.URL;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.role.BDFinancialRep;
import com.manulife.pension.service.security.role.BDFinancialRepAssistant;
import com.manulife.pension.service.security.role.BDFirmRep;
import com.manulife.pension.service.security.role.BDRvp;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.service.security.role.RIAUser;

/**
 * This is a Helper class to retrieve the Applicable columns for a given tab, user type.
 * 
 * @author harlomte
 * 
 */
public class BOBColumnCriteria {

    private static BOBColumnStore bobColumnStore = null; 
    
    /**
     * This method retrieves all the applicable columns for a given tab, userRole.
     * 
     * @param tabName - The tab Name
     * @param userRole - BDUserRole object
     * @param isMimicMode - whether the user is in mimic mode or not.
     * @return - returns all the columns applicable.
     * @throws SystemException
     */
    public static BOBColumnsApplicableToTab getApplicableColumns(String tabName,
            BDUserRole userRole, Boolean isMimicMode) throws SystemException {
        
        String versionInfo = getVersionInfoForUserType(userRole, isMimicMode);
        BOBColumnsApplicableToTab applicableColumns = getColumnStore()
                .getApplicableColumnsforVersionAndTab(versionInfo, tabName);

        return applicableColumns;
    }

    /**
     * This method applies rules based on userRole, mimicMode to get a final list of applicable
     * columns.
     * 
     * @param userRole - BDUserRole object
     * @param isMimicMode - whether the user is in mimic mode or not.
     * @return
     */
    public static String getVersionInfoForUserType(BDUserRole userRole, Boolean isMimicMode) {
        if (isMimicMode == null) {
            isMimicMode = Boolean.FALSE;
        }

        if (userRole instanceof BDFinancialRep || userRole instanceof BDFinancialRepAssistant) {
            return BDConstants.COLUMNS_VERSION1;
        }
        
        if (userRole instanceof BDFirmRep || userRole instanceof RIAUser) {
            return BDConstants.COLUMNS_VERSION2;
        }
        
        if (BlockOfBusinessUtility.isInternalUserAndNotRVP(userRole) || userRole instanceof BDRvp) {
            if (!isMimicMode) {
                return BDConstants.COLUMNS_VERSION3;
            }
        }
        
        return null;
    }

    /**
     * This method gets the columns info from the XML file.
     * 
     * @return - BOBColumnStore object containing the columns info, obtained from XML file.
     * @throws SystemException
     */
    public static synchronized BOBColumnStore getColumnStore() throws SystemException {
        if (bobColumnStore == null) {
            URL bobColumnFileURL = BOBColumnCriteria.class.getClassLoader().getResource(
                    BDConstants.BOB_COLUMN_CONFIG_FILE);
            
            if (bobColumnFileURL == null) {
                throw new SystemException(BDConstants.UNABLE_TO_GET_BOB_STORE);
            }

            String columnInfoFile = bobColumnFileURL.getPath();

            ApplicationContext filterCtx = new FileSystemXmlApplicationContext(columnInfoFile);

            bobColumnStore = (BOBColumnStore) filterCtx.getBean(BDConstants.ALL_COLUMNS);
        }
        if (bobColumnStore == null) {
            throw new SystemException(BDConstants.UNABLE_TO_GET_BOB_STORE);
        }
        return bobColumnStore;
    }
}

