package com.manulife.pension.bd.web.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.util.Constants;
import com.manulife.pension.service.contract.valueobject.ContractVO;

/**
 * Utility class to search the contract either by contract name/number 
 * and by user role
 * 
 * @author ayyalsa
 *
 */
public class ContractSearchUtility {

	/**
	 * Map that stores the filter criteria
	 */
	private static Map<String, Object > filtersMap;
	
	public static final String FILTER_DB_SESSION_ID = "sessionIdForDB";
	public static final String FILTER_USER_ROLE = "userRole";
	public static final String FILTER_CONTRACT_NAME = "contractName";
    public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
    public static final String FILTER_USER_PROFILE_ID = "userProfileID";
    public static final String FILTER_CONTRACT_STATUS_CODES = "contractStatusCodes";
    public static final String FILTER_MIMIC_USER_PROFILE_ID = "mimicUserProfileID";
    public static final String FILTER_MIMIC_USER_ROLE = "mimicUserRole";
    public static final String ACTIVE_CONTRACT_STATUS_CODE = "AC";
    public static final String DI_CONTRACT_STATUS_CODE = "DI";
    public static final String IA_CONTRACT_STATUS_CODE = "IA";
	public static final int INTERNAL_USER = 1;
	public static final int EXTERNAL_USER = 2;
	public static final int RVP_USER = 3;
	
	
    /**
     * Searches contract by Contract number
     * 
     * @param request
     * @param userProfile
     * @param contractNumber
     * @return List of contracts
     * @throws SystemException
     */
	public static List<ContractVO> searchByContractNumber(HttpServletRequest request, 
			BDUserProfile userProfile, String contractNumber,List<String> includedContractList,boolean includeIAStatus) 
	throws SystemException {
		
		return getContractDetails(request, 
				userProfile, FILTER_CONTRACT_NUMBER, contractNumber,includedContractList,includeIAStatus);
		
	}
	
	/**
	 *  Searches contract by Contract number
	 *  
	 * @param request
	 * @param userProfile
	 * @param contractNameSearchText
	 * @return List of contracts
	 * @throws SystemException
	 */
	public static List<ContractVO> searchByContractName(HttpServletRequest request, 
			BDUserProfile userProfile, String contractNameSearchText,List<String> includedContractList,boolean includeIAStatus) 
	throws SystemException {
		
		return getContractDetails(request, 
				userProfile, FILTER_CONTRACT_NAME, contractNameSearchText,includedContractList,includeIAStatus);
	}
	
	/**
	 * Retrieves BOB contracts available for this UserProfile
	 * 
	 * @param request
	 * @param userProfile
	 * @param contractReviewReportText
	 * @return List of contracts
	 * @throws SystemException
	 */
	public static List<ContractVO> getBOBContractDetails(
			HttpServletRequest request, BDUserProfile userProfile,
			List<String> includedContractList, boolean includeIAStatus)
			throws SystemException {

		return getContractDetails(request, userProfile,
				Constants.PLAN_REVIEW_REPORT_INDICATOR, BDConstants.YES,
				includedContractList, includeIAStatus);
	}
	
	//JAN 2015 Release ACR Rewrite Changes
	
	/**
	 * Retrieves contract list based on the user profile/role and filters
	 * 
	 * @param request
	 * @param userProfile
	 * @param filterKey
	 * @param filterValue
	 * @return List of Contracts
	 * @throws SystemException
	 */
	private static List<ContractVO> getContractDetails(HttpServletRequest request, 
			BDUserProfile userProfile, String filterKey, String filterValue,
			List<String> includedContractList,boolean includeIAStatus) throws SystemException {
		
		if (userProfile == null) {
			throw new SystemException("UserProfile is null");
		}
		
		List<ContractVO> contractList =  new ArrayList<ContractVO>();
		
		Iterator<String> includedContractsIterator = includedContractList.iterator();		
		while(includedContractsIterator.hasNext()){
		
			// get the contract status
			String contractStatus = (String)includedContractsIterator.next();
			
			// create the filters required for the stored-procedure
			createFiltersMap(userProfile, filterKey, filterValue, contractStatus, request);

			// trigger the stored-procedure call
			List<ContractVO> returnedContractList = getContractList(request);
			
			// add only if, the stored-procedure has returned data 
			if (returnedContractList != null) {
				contractList.addAll(returnedContractList);
			}
		}
		
		return filterIAStatusContractFromResultList(contractList,includeIAStatus);
	}
	
	/**This method will iterate the resulting list and eliminates the Contrtact VO with "IA" status from the list.
	 * @param returnedContractList
	 * @param includeIAStatus
	 * @return
	 */
	private static List<ContractVO> filterIAStatusContractFromResultList(List<ContractVO> returnedContractList,boolean includeIAStatus){
		List<ContractVO> contractListWithoutIAStaus = new ArrayList<ContractVO>();
		if(!includeIAStatus){
			Iterator<ContractVO> includedContractsIterator = returnedContractList.iterator();
			while(includedContractsIterator.hasNext()){
				
				ContractVO contractVO = (ContractVO)includedContractsIterator.next();
				if(contractVO != null && !IA_CONTRACT_STATUS_CODE.equals(contractVO.getContractStatusCode())){					
					contractListWithoutIAStaus.add(contractVO);					
				}
			}
			return contractListWithoutIAStaus;
		}		
		else{
			return returnedContractList;
		}		
	}
	
	/**
	 * Retrieves contract list based on the user profile/role and filters
	 * @return List of Contracts
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	private static List<ContractVO> getContractList(HttpServletRequest request) throws SystemException {
		Map<String, Object> resultsMap = 
			ContractServiceDelegate.getInstance().getBOBContractList(filtersMap);
		
		if (resultsMap != null && !resultsMap.isEmpty()) {
			BigDecimal dbSessionId = (BigDecimal) resultsMap.get(FILTER_DB_SESSION_ID);
			if (dbSessionId != null) {
				request.getSession().setAttribute(FILTER_DB_SESSION_ID, dbSessionId);
			}
			return (List<ContractVO>)resultsMap.get("contractData");
		}
		return null;
	}
	
	/**
	 * Creates filter based on the user values
	 *  
	 * @param userProfile
	 * @param filterKey
	 * @param filterValue
	 * @param contractStatusCode
	 * @param request
	 */
	private static void createFiltersMap(BDUserProfile userProfile, String filterKey, 
			String filterValue, String contractStatusCode, HttpServletRequest request) {
		
		if (filtersMap == null) {
			filtersMap = new HashMap<String, Object>();
		} else {
			filtersMap.clear();
		}
		
		BigDecimal dbSessionId = (BigDecimal)request.getSession().getAttribute(FILTER_DB_SESSION_ID);
		
		// put the values to the Map
		filtersMap.put(FILTER_DB_SESSION_ID, dbSessionId);
		filtersMap.put(FILTER_USER_PROFILE_ID, userProfile.getBDPrincipal().getProfileId());
		filtersMap.put(FILTER_USER_ROLE, userProfile.getRole().getRoleType().getUserRoleCode());
		filtersMap.put(FILTER_CONTRACT_STATUS_CODES, contractStatusCode);
		filtersMap.put(filterKey, filterValue);
		
		if (userProfile.isInMimic()) {
			 BDUserProfile mimickingUserProfile = getMimickingUserProfile(request);
			filtersMap.put(FILTER_MIMIC_USER_PROFILE_ID, mimickingUserProfile.getBDPrincipal()
                    .getProfileId());
			filtersMap.put(FILTER_MIMIC_USER_ROLE, 
					mimickingUserProfile.getRole().getRoleType().getUserRoleCode());
		}
	}
	
	/**
	 * Returns the type of the user
	 * 
	 * @param userProfile
	 * @param request
	 * @return user type code
	 */
	private static int getUserType(BDUserProfile userProfile, HttpServletRequest request) {
		
		if (userProfile.isInMimic()) {
			BDUserProfile mimickingUserProfile = getMimickingUserProfile(request);
			if (BDUserProfileHelper.isRvp(mimickingUserProfile)){
				return RVP_USER;
			} else if (BDUserProfileHelper.isInternalUser(mimickingUserProfile)){
				return INTERNAL_USER;
			} 
		
		} else {
			if (BDUserProfileHelper.isRvp(userProfile)){
				return RVP_USER;
			} else if (BDUserProfileHelper.isInternalUser(userProfile)){
				return INTERNAL_USER;
			} 
		
		}
		return EXTERNAL_USER;
	}
	
	/**
     * This method would return the UserProfile object of the mimicking user.
     * 
     * @return - BDUserProfile object of the mimicking user.
     */
    @SuppressWarnings("unchecked")
    public static BDUserProfile getMimickingUserProfile(HttpServletRequest request) {
        Map<String, Object> mimickingUserSession = (Map<String, Object>) request.getSession(false)
                .getAttribute(BDConstants.ATTR_MIMICKING_SESSION);
        if (mimickingUserSession == null) {
            return null;
        }
        
        BDUserProfile mimickingInternalUserProfile = (BDUserProfile) mimickingUserSession
                .get(BDConstants.USERPROFILE_KEY);

        return mimickingInternalUserProfile;
    }
}
