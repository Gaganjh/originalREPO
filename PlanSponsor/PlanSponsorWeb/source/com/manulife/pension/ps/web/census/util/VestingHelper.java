package com.manulife.pension.ps.web.census.util;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.census.PswLastUpdatedDetailImpl;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.employee.valueobject.BaseEmployeeInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.vesting.LastUpdatedDetail;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.service.vesting.VestingInputDescription;
import com.manulife.pension.service.vesting.VestingRetriever;
import com.manulife.pension.util.TimestampUtils;

public class VestingHelper {

    public static final Logger logger = Logger.getLogger(VestingHelper.class);
    
    // Mapper to map NonVYOS source channel code values to new values used by UI
    private static final NonVYOSSourceCodeMapper nonVYOSSourceCodeMapper = new NonVYOSSourceCodeMapper();

    // Mapper to map VYOS source channel code values to new values used by UI
    private static final VYOSSourceCodeMapper vyosSourceCodeMapper = new VYOSSourceCodeMapper();
    
    //SimpleDateFormat is converted to FastDateFormat to make it thread safe
    private static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("MM/dd/yyyy");
    
    /**
     * Returns converted (new) source code for the given old source code.

     * @param oldSourceCode
     * @param vestingType
     * @return
     */
    private static String getConvertedSourceCode(String oldSourceCode, VestingType vestingType) {
        String newSourceCode = oldSourceCode;
        if (VestingType.VYOS.equals(vestingType) || VestingType.APPLY_LTPT_CREDITING.equals(vestingType)) {
            newSourceCode = vyosSourceCodeMapper.getConvertedValue(oldSourceCode);
        } else {
            newSourceCode = nonVYOSSourceCodeMapper.getConvertedValue(oldSourceCode);
        }

        return newSourceCode;
    }
    
	/**
	 * Check for a contract, if the vesting information is available.
	 * 
	 * @param contractId
	 * @return
	 */
	public static boolean isVestingInformationAvailable(int contractId)
			throws SystemException {
		ContractServiceFeature csf = null;
		try {
			csf = ContractServiceDelegate.getInstance()
					.getContractServiceFeature(contractId,
							ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
		} catch (ApplicationException e) {
			throw new SystemException(e, "VestingHelper",
					"isVestingInformationAvailable",
					"Fail to get the contract service feature for contract : "
							+ contractId);
		}
		if (ServiceFeatureConstants.CALCULATED.equals(csf.getValue())) {
			//final PlanData planData = ContractServiceDelegate
            //.getInstance().readPlanData(contractId);
			final PlanDataLite planDataLite = ContractServiceDelegate.getInstance().getPlanDataLight(
	                new Integer(contractId));
			final String creditingMethod = planDataLite.getVestingServiceCreditMethod();
			if (!StringUtils.isEmpty(creditingMethod)
					&& !VestingConstants.CreditingMethod.UNSPECIFIED
							.equals(creditingMethod)) {
				return true;
			}
		}
		return false;
	}
    
    /**
     * Generic method to build the audit info using logged in user profile and 
     * employee info change history from the vesting engine/param table
     * 
     * @param info
     * @param userProfile - logged in user profile
     * @param vestingType
     * @return
     */
    public static String getVestingAuditInfo(PswLastUpdatedDetailImpl info, UserProfile userProfile, VestingType vestingType) 
    {

        final String JHR = "John Hancock Representative";

        // Lookup the user who last changed the info.
        UserInfo userInfo = null;
        if (StringUtils.isNotEmpty(info.getUserId())) {
            try {
                try {
                    final Long profileId = Long.valueOf(StringUtils.trim(info.getUserId()));
                    userInfo = SecurityServiceDelegate.getInstance().searchByProfileId(null,
                            profileId);
                } catch (NumberFormatException numberFormatException) {
                    // ignore the NumberFormatException
                } catch (SecurityServiceException securityServiceException) {
                    throw new RuntimeException(securityServiceException);
                } // end try/catch
            } catch (Exception exception) {
                // We basically ignore any lookup issue and use the userInfo as null.
                logger.debug("Exception Searching with method searchByProfileId using userId["
                        + StringUtils.trim(info.getUserId()) + "]", exception);
                
                userInfo = null;
            } // end try/catch
        }    
        
        StringBuffer auditInfo = new StringBuffer();

        boolean isHistoryEmpty = false;

        // Populate source (Web site, Vesting File, Census File)
        String source = "";
        if (StringUtils.isNotEmpty(info.getSource())) {
            source = info.getSource();
        } else if (StringUtils.isNotEmpty(info.getChannel())) {
            source = getConvertedSourceCode(info.getChannel(), vestingType);
        }
        if (!StringUtils.isEmpty(source)) { 
            auditInfo.append("via ").append(CensusLookups.getInstance()
                                    .getLookupValue(CensusLookups.SourceChannelCode, source));
        }
        
        if (StringUtils.isEmpty(source) && info.getUserId() == null){
            isHistoryEmpty = true;
        }

        // Populate user info
        if (userInfo != null && userInfo.getRole().isTPA()) {
            auditInfo.append(" by TPA User ");
        } else if (!isHistoryEmpty) {
            auditInfo.append(" by ");
        }

        if (userInfo != null && userInfo.getRole().isExternalUser()) {
            // changedBy is external user
            auditInfo.append(userInfo.getFirstName()).append(" ").append(userInfo.getLastName());
        } else if (!isHistoryEmpty) {
            // Either we don't have userInfo for changeby user OR changedBy user is an internal one
            // display its userId (if its not empty). If logged in user is an internal one then return
            // userId of changedBy user along with JHR
            if (userProfile.getRole().isInternalUser() && StringUtils.isNotEmpty(info.getUserId())) {
                auditInfo.append(StringUtils.trimToEmpty(info.getUserId())).append(", ");
            } 
            auditInfo.append(JHR);
        }
        
        // Populate last updated timestamp
        if (info.getTimestamp() != null) {
            auditInfo.append(" on ").append(DATE_FORMATTER.format(info.getTimestamp()));
        }
        
        if (auditInfo.length() > 0) {
            auditInfo = new StringBuffer("Provided ").append(auditInfo);
        }
        return auditInfo.toString();

    }

    /**
     * Build the audit info using logged in user profile and 
     * employee info change history from the param table
     * 
     * @param info
     * @param userProfile - logged in user profile
     * @param vestingType
     * @return
     */
    public static String getVestingAuditInfo(BaseEmployeeInfo info, UserProfile userProfile, VestingType vestingType)
     {
     
        PswLastUpdatedDetailImpl auditInfo = new PswLastUpdatedDetailImpl(
                info.getLastUpdatedTime() != null ? 
                        new Timestamp(info.getLastUpdatedTime().getTime()) : null,
                info.getLastUpdatedUserId(), 
                info.getLastUpdatedUserIdType(), info.getSource(), "");
        
        return getVestingAuditInfo(auditInfo, userProfile, vestingType);
    }
    
    /**
     * Build the audit info using logged in user profile and 
     * employee info change history from the vesting engine
     * If vyos was calculated display "Calculated by John Hancock"
     * instead of any audit info
     * 
     * @param info
     * @param userProfile - logged in user profile
     * @param vestingType
     * @return
     */
    public static String getVestingAuditInfo(VestingInputDescription info, UserProfile userProfile,
            VestingType vestingType) {
        return getVestingAuditInfo(null, info, userProfile, vestingType);
    }

    /**
     * Build the audit info using logged in user profile and <i>either from employee info change
     * history from the vesting engine OR from employee info change history from the param table</i>.
     * If vyos was calculated display "Calculated by John Hancock" instead of any audit info
     * 
     * @param employeeInfo
     * @param info
     * @param userProfile - logged in user profile
     * @param vestingType
     * @return
     */
    public static String getVestingAuditInfo(BaseEmployeeInfo employeeInfo,
            VestingInputDescription info, UserProfile userProfile, VestingType vestingType)
             {
        
        final String CALCULATED = "Calculated by John Hancock";
        PswLastUpdatedDetailImpl auditInfo = null;

        if (info != null) {
            if (vestingType.VYOS.name().equals(vestingType.name())) {
                // check if vyos was calculated or provided
                if (info.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_CALCULATED ||
                    info.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_CALCULATED_UNUSED) {
                    return CALCULATED;
                }
            }
            
            if (employeeInfo != null) {
                auditInfo = new PswLastUpdatedDetailImpl(TimestampUtils
                        .convertToTimestamp(employeeInfo.getLastUpdatedTime()), employeeInfo
                        .getLastUpdatedUserId(), employeeInfo.getLastUpdatedUserIdType(),
                        employeeInfo.getSource(), "");
            } else if (info.getLastUpdatedDetail() != null) {
                LastUpdatedDetail lastUpdatedDetail = info.getLastUpdatedDetail();
                auditInfo = new PswLastUpdatedDetailImpl(TimestampUtils
                        .convertToTimestamp(lastUpdatedDetail.getTimestamp()), lastUpdatedDetail
                        .getUserId(), lastUpdatedDetail.getUserType(), lastUpdatedDetail
                        .getChannel(), "");
            }
            
       }

        return (auditInfo != null) ? getVestingAuditInfo(auditInfo, userProfile, vestingType) : "";
    }
    public static String getVestingAuditInfoForLTPTCrediting(BaseEmployeeInfo employeeInfo, 
    		UserProfile userProfile, VestingType vestingType){
        
    	
            PswLastUpdatedDetailImpl auditInfo = null;
            
            if (employeeInfo != null) {
                auditInfo = new PswLastUpdatedDetailImpl(TimestampUtils
                        .convertToTimestamp(employeeInfo.getLastUpdatedTime()), employeeInfo
                        .getLastUpdatedUserId(), employeeInfo.getLastUpdatedUserIdType(),
                        employeeInfo.getSource(), "");
            } 

        return (auditInfo != null) ? getVestingAuditInfo(auditInfo, userProfile, vestingType) : "";
    }
}
