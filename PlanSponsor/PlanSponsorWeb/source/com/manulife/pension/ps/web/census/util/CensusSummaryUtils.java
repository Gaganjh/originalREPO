/**
 * 
 */
package com.manulife.pension.ps.web.census.util;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;


/**
 * A utility class providing Web tier utility methods for Census Information masking.
 * 
 * @author Diana Macean
 *
 */
public class CensusSummaryUtils {
    
    private static final Logger log = Logger.getLogger(CensusSummaryUtils.class);
    
    private static boolean isMaskSensitiveInfoInd(String sortOptionCode, CensusSummaryDetails item) {
        boolean maskSensitiveInfoInd = false;
        EmployeeDetailVO vo = null;
        
        try {
            // find employee in CSDB by identifier
            EmployeeServiceDelegate delegate = EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
            if (Constants.SSN_SORT_OPTION_CODE.equals(sortOptionCode)) {
                vo = delegate.getEmployeeByIdentifier(item.getSsn(), item.getContractId(), true);
            } else {
                vo = delegate.getEmployeeByIdentifier(item.getEmployeeNumber(), item.getContractId(), false);    
            }
        } catch (SystemException e) {
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);
        }
        
        if (vo != null) {
            if (Constants.YES.equals(vo.getMaskSensitiveInfoInd()))
                maskSensitiveInfoInd = true;
            else
                maskSensitiveInfoInd = false;  
        }
        
        
        return maskSensitiveInfoInd;
    }
    
    private static boolean isMaskSensitiveInfoInd(CensusSummaryDetails item) {
        boolean maskSensitiveInfoInd = false;
                
        if (item != null) {
            if (Constants.YES.equals(item.getMaskSensitiveInfo()))
                maskSensitiveInfoInd = true;
            else
                maskSensitiveInfoInd = false;  
        }
        
        return maskSensitiveInfoInd;
    }
    
    public static String getMaskedValue(BigDecimal value, CensusSummaryDetails item, UserProfile user, UserInfo userInfo, 
                         boolean forceMask) {
        StringBuffer html = new StringBuffer();
        boolean maskSensitiveInfo = CensusUtils.isMaskSensitiveInformation(user, userInfo, isMaskSensitiveInfoInd(item));
        
        if (value != null && value.compareTo(new BigDecimal(0)) != 0) {
            if ( maskSensitiveInfo || forceMask ) {
                html.append("");
            } else {
                html.append(NumberRender.formatByType(value, "", RenderConstants.CURRENCY_TYPE, false));
            }
        }
        
        return html.toString();
    }
}