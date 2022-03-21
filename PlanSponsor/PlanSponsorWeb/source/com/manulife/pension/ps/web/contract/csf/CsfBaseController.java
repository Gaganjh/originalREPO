package com.manulife.pension.ps.web.contract.csf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApolloBackEndException;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.ContractAssemblyServiceDelegate;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.contract.ContractConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ParticipantACIFeatureUpdateVO;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.util.content.GenericException;

/**
 * Base Action class for the CSF page.
 * 
 * @author Arugunta Puttaiah
 * 
 */
public abstract class CsfBaseController extends PsAutoController {

	public static final String className = CsfBaseController.class.getName();

	public static final Map<String, String> payrollFrequencyMap = 
		new HashMap<String, String>();
	
	public static final Set<String> PostACContractStatus = 
		new HashSet<String>();

	static {
				
		payrollFrequencyMap.put(CsfConstants.PAYROLL_FREQUENCY_WEEKLY_CODE, CsfConstants.PAYROLL_FREQUENCY_WEEKLY);
		payrollFrequencyMap.put(CsfConstants.PAYROLL_FREQUENCY_BI_WEEKLY_CODE, CsfConstants.PAYROLL_FREQUENCY_BI_WEEKLY);
		payrollFrequencyMap.put(CsfConstants.PAYROLL_FREQUENCY_SEMI_MONTHLY_CODE, CsfConstants.PAYROLL_FREQUENCY_SEMI_MONTHLY);
		payrollFrequencyMap.put(CsfConstants.PAYROLL_FREQUENCY_MONTHLY_CODE, CsfConstants.PAYROLL_FREQUENCY_MONTHLY);
		payrollFrequencyMap.put(CsfConstants.PAYROLL_FREQUENCY_UNSPECIFIED_CODE, CsfConstants.PAYROLL_FREQUENCY_UNSPECIFIED);

		PostACContractStatus.add(ContractConstants.ContractStatus.ACTIVE);
		PostACContractStatus.add(ContractConstants.ContractStatus.FROZEN);
		PostACContractStatus.add(ContractConstants.ContractStatus.DISCONTINUED);		
	}

	/**
	 * @param clazz
	 */
	@SuppressWarnings("unchecked")
	public CsfBaseController(Class clazz) {
		super(clazz);
	}

	/**
	 * Retrieves the Lite version of Plan data for the specified contract
	 * 
	 * @param request
	 * @param contractId
	 * @return
	 */
	protected PlanDataLite getPlanDataLite(
			HttpServletRequest request, Integer contractId) {

		// retrieve the PlanDataLite object from the request
		PlanDataLite planDataLite = (PlanDataLite) request
				.getAttribute(CsfConstants.REQ_PLAN_DATA_LITE);

		// if the PlanDataLite object is null, then fetch from database
		if (planDataLite == null) {
			try {
				// get the plan data from database
				planDataLite = ContractServiceDelegate.getInstance().
											getPlanDataLight(contractId);
				
				// set the plan data in request
				request.setAttribute(
						CsfConstants.REQ_PLAN_DATA_LITE, planDataLite);
				
			} catch (SystemException e) {
				throw new NestableRuntimeException(e);
			}
		}
		
		return planDataLite;
	}

	/**
	 * Retrieves the contractProfile from the database and sets in Request
	 * 
	 * @param contractId
	 * @param request
	 */
	protected void setContractProfile(int contractId, HttpServletRequest request) {
		ContractProfileVO contractProfileVO = null;
		
		try {
			// get the contract profile data from database
			contractProfileVO = ContractServiceDelegate.getInstance()
					.getContractProfileDetails(contractId, 
							Environment.getInstance().getSiteLocation());
			
		} catch (SystemException e) {
			logger.error(e);
		}

		// set the contract profile object in the request
		request.setAttribute(Constants.CONTRACT_PROFILE, contractProfileVO);
	}

	/**
	 * CSF. 133 Method to validate the user has the permission to Edit the CSF values and
	 * the current contract is valid for the editing or not.
	 * 
	 * User can not edit the CSF values, if he doesn't have the permission or
	 * contract is not allowed for editing.
	 * 
	 * @param userProfile
	 * @param currentContract
	 * @return
	 */
	protected boolean isAllowedEditing(
			UserProfile userProfile, Contract currentContract) {
		
		boolean isAllowedEditing = false;

		if (userProfile.getRole().hasPermission(PermissionType.EDIT_SERVICE_FEATURES) 
				&&	userProfile.isInternalUser()) {
				if (currentContract.getStatus().equals("PS")
						|| currentContract.getStatus().equals("DC")
						|| currentContract.getStatus().equals("PC")
						|| currentContract.getStatus().equals("AC")
						|| currentContract.getStatus().equals("CA")) {
					isAllowedEditing = true;
			} 
		}

		return isAllowedEditing;
	}
	
	/**
	 * Method to save the Changed Contract Service Features
	 * 
	 * @param request
	 * @return
	 * @throws ApolloBackEndException 
	 * @throws SecurityServiceException 
	 */
	@SuppressWarnings("unchecked")
	public Collection<GenericException> saveContractServiceFeatureData(CsfForm csfForm, 
			HttpServletRequest request) throws SystemException, ApolloBackEndException, SecurityServiceException{

		// get the user profile
		UserProfile userProfile =  getUserProfile(request);

		// get the changed csf and attributes as a collection from session
		Collection<ContractServiceFeature> changedCsfCollection = 
			(Collection<ContractServiceFeature>) request.getSession(
					false).getAttribute(CsfConstants.CHANGED_CSF_COLLECTION);
		
		// get the ACI object from session
		ParticipantACIFeatureUpdateVO paf = 
			(ParticipantACIFeatureUpdateVO) request.getSession(
					false).getAttribute(CsfConstants.PARTICIPANT_ACI_FEATURE_VO);

		CsfDataHelper csfDataHelper =  CsfDataHelper.getInstance();
		if(changedCsfCollection == null){
			if(paf == null){
				paf = getParticipantACIFeatureUpdateVO(request);
			}
			changedCsfCollection = 
				csfDataHelper.getChangedContractServiceFeatures(
						userProfile.getCurrentContract(), 
						csfForm, userProfile.getPrincipal(), paf);
		}
		Collection<GenericException> errors = new ArrayList<GenericException>();
		
		csfDataHelper.validateOtherContractServiceFeatureData(csfForm, request,	changedCsfCollection, 
				errors, errors, true);
		/*
		 *  if the changed csf collection object has size > 0 then,
		 *  perform the save operation 
		 */
		if(changedCsfCollection .size() > 0 && errors.isEmpty()){

			ContractAssemblyServiceDelegate.getInstance(
					GlobalConstants.PSW_APPLICATION_ID)
					.saveCSFAndTriggerECRequest(changedCsfCollection, paf);
		}

		return errors;
	}

	/**
	 * Method to get the ParticipantACIFeatureUpdateVO
	 * 
	 * @param request
	 * @return
	 */
	protected ParticipantACIFeatureUpdateVO getParticipantACIFeatureUpdateVO(
			HttpServletRequest request){

		UserProfile userProfile =  getUserProfile(request);

		ParticipantACIFeatureUpdateVO paf = new ParticipantACIFeatureUpdateVO();

		// determine the User Id type based on user role 
		if(userProfile.getPrincipal().getRole().isExternalUser()) {
			paf.setUserIdType(Constants.EXTERNAL_USER_ID_TYPE);
		} else {
			paf.setUserIdType(Constants.INTERNAL_USER_ID_TYPE);
		}

		// set the user id
		paf.setUserId(
				Long.toString(userProfile.getPrincipal().getProfileId()));
		
		// set the contract number
		paf.setContractNumber(
				userProfile.getCurrentContract().getContractNumber());

		return paf;
	}
	
	/**
	 * * Method to validate the user has the permission to Edit the CSF values
	 * and the current contract is valid for the editing or not.
	 * 
	 * User can not edit the CSF values, if he doesn't have the permission or
	 * contract is not allowed for editing.
	 * 
	 * If Internal User, Contract status must be PS, DC, PC, or CA. External
	 * Users will be alloved to view AC, DI, CF
	 * 
	 * @param userProfile
	 * @param currentContract
	 * @return
	 */
	protected boolean isAllowedPageAccess(UserProfile userProfile,
			Contract currentContract) {
		
		boolean canAccessPage = false;
		
		// If Internal User, Contract status must be PS, DC, PC, or CA
		if (userProfile.isInternalUser()) {
			if (currentContract.getStatus().equals("PS")
					|| currentContract.getStatus().equals("DC")
					|| currentContract.getStatus().equals("PC")
					|| currentContract.getStatus().equals("AC")
					|| currentContract.getStatus().equals("DI")
					|| currentContract.getStatus().equals("CF")
					|| currentContract.getStatus().equals("CA")) {
				// good
				canAccessPage = true;
			} else {
				logger.warn("Internal user cannot view/edit Contract status"
						+ currentContract.getStatus());
			}
		
		// External Users will be allowed to view AC, DI, CF
		} else {
			if (currentContract.getStatus().equals("AC")
					|| currentContract.getStatus().equals("DI")
					|| currentContract.getStatus().equals("CF")) {
				// good
				canAccessPage = true;
			} else {
				logger.warn("External user cannot view Contract status"
						+ currentContract.getStatus());
			}
		}
		return canAccessPage;
	}
	
	/**
	 * Method to load service feature data
	 * @param csfForm
	 * @param csfMap 
	 * @param currentContract
	 * @param planDataLite
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	protected void loadCSFDataToForm(CsfForm csfForm, Map csfMap, Contract currentContract, 
			PlanDataLite planDataLite, boolean isAlreadyLoaded)	throws SystemException {
		if(!isAlreadyLoaded){
			
			if(csfMap == null){
				ContractServiceDelegate service = ContractServiceDelegate.getInstance();
				
				try {
					csfMap = service.getContractServiceFeatures(currentContract.getContractNumber());
				} catch (ApplicationException ae) {
					throw new SystemException(ae.toString() + className 
							+ "loadContractServiceFeatureData" 
							+ ae.getDisplayMessage());
				}
			}
			
			CsfDataHelper.getInstance().loadContractServiceFeatureData( 
					currentContract, csfMap, csfForm, planDataLite);
			
			if (PostACContractStatus.contains(currentContract.getStatus())) {
				csfForm.setIpsServiceSuppressed(false);
				csfForm.setCoFidFeatureSuppressed(false);
			} else {
				PostACContractStatus.contains(true);
			}
			
			csfForm.storeClonedForm();
		}
	}

	/**
	 * Return the edit page forward
	 * 
	 * @return forward as String
	 */
	public String getEditForwardName() {
		return CsfConstants.CSF_ELECTRON_CONTRACT_SERVICES_EDIT_PAGE;
	}

	/**
	 * Return the view page forward
	 * 
	 * @return forward as String
	 */
	public String getViewForwardName() {
		return CsfConstants.CSF_ELECTRON_CONTRACT_SERVICES_PAGE;
	}

	/**
	 * Return the confirm page forward
	 * 
	 * @return forward as String
	 */
	public String getConfForwardName() {
		return CsfConstants.CSF_ELECTRON_CONTRACT_SERVICES_CONFIRMATION_PAGE;
	}

	/**
	 * Get the current date with time set as 0
	 * 
	 * @return date
	 */
	public static Date getCurrentDate() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);

		return cal.getTime();
	}
}