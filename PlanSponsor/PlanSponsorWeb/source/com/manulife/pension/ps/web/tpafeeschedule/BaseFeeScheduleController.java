package com.manulife.pension.ps.web.tpafeeschedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.event.IpiHostingCustomScheduleChangeEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.fee.FeeUIHolder;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.fee.util.Constants.FeeScheduleType;
import com.manulife.pension.service.fee.valueobject.FeeScheduleUpdateDetails;
import com.manulife.pension.service.plan.validators.ContractValidator;
import com.manulife.pension.service.plan.validators.ContractValidatorFactory;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the base class of all view/edit/preview user actions.
 * 
 * @author Akhil Khanna
 */

public abstract class BaseFeeScheduleController extends BaseAutoController {

	public static final String GLOBAL_ERROR = "GLOBAL";
	public static final String TPA_STANDARD_FEE_SCHEDULE_EDIT_PAGE = "editTpaStandardFeeData";
	public static final String TPA_STANDARD_FEE_SCHEDULE_PREVIEW_ACTION = "goToPreviewAction";
	public static final String TPA_STANDARD_FEE_SCHEDULE_VIEW_ACTION = "goToViewAction";
	public static final String TPA_STANDARD_FEE_SCHEDULE_EDIT_ACTION = "goToEditAction";
	public static final String SELECT_TPA_FIRM_PAGE = "goToSelectTpaFirmAction";
	public static final String TPA_STANDARD_FEE_SCHEDULE_PREVIEW_PAGE = "previewTpaStandardFeeData";
	public static final String TPA_STANDARD_FEE_SCHEDULE_VIEW_PAGE = "viewTpaStandardFeeData";
	public static final String EDIT_STANDARD_TPA_PAGE = "edit";
	public static final String CHANGE_HISTORY_STANDARD_TPA_PAGE = "changeHistory";
	public static final String CUSTOMIZE_CONTRACT_PAGE = "customizeContract";
	public static final String TPA_STANDARD_FEE_SCHEDULE_VIEW_PAGE_FOR_INTERNAL_USER = "defaultForInternalUser";
	protected static final String TPA_FIRM_ID = "tpaFirmId";

	/**
	 * Constructor.
	 * 
	 * 
	 */

	public BaseFeeScheduleController() {
		super(BaseFeeScheduleController.class);
	}

	public static UserProfile getUserProfile(final HttpServletRequest request) {
		return SessionHelper.getUserProfile(request);
	}

	/**
	 * 
	 * @param contractId
	 * @param request
	 * @param lockKey
	 * 
	 * @throws SystemException
	 */
	protected void releaseLock(final int id, final HttpServletRequest request,
			final String lockKey) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer(
					"releaseLock> Releasing lock for plan [").append(id)
					.append("].").toString());
		}
		LockServiceDelegate.getInstance().releaseLock(lockKey, lockKey + id);
	}

	/**
	 * 
	 * @param contractId
	 * @param request
	 * @param lockKey
	 * 
	 * @throws SystemException
	 */
	protected void handleObtainLockFailure(final int id,
			final HttpServletRequest request, final String lockKey)
			throws SystemException {

		UserInfo lockOwnerUserInfo = null;
		try {
			lockOwnerUserInfo = SecurityServiceDelegate.getInstance()
					.searchByProfileId(
							getUserProfile(request).getPrincipal(),
							LockServiceDelegate.getInstance().getLockInfo(
									lockKey, lockKey + id)
									.getLockUserProfileId());
		} catch (final Exception exception) {
			logger.warn(exception);
		}
		final String[] lockOwnerDisplayName =  getLockOwnerDisplayName(getUserProfile(request), lockOwnerUserInfo);

		final Collection<ValidationError> errors = new ArrayList<ValidationError>();
		errors.add(new ValidationError(new String[] { GLOBAL_ERROR },
				ErrorCodes.FEE_SCHEDULE_EDIT_ERROR_MESSAGE,
				lockOwnerDisplayName,
				ValidationError.Type.warning));
		if (logger.isDebugEnabled()) {
			logger
					.debug(new StringBuffer(
							"handleObtainLockFailure> Generated errors because of lock failure [")
							.append(errors).append("].").toString());
		}
		setErrorsInSession(request, errors);
	}

	/**
	 * 
	 * @param contractId
	 * @param request
	 * @param lockKey
	 * 
	 * @return boolean
	 * 
	 * @throws SystemException
	 */
	protected boolean obtainLock(final int id,
			final HttpServletRequest request, final String lockKey)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug(new StringBuffer(
					"obtainLock> Obtaining lock for plan [").append(id).append(
					"].").toString());
		}
		return LockServiceDelegate.getInstance().lock(lockKey, lockKey + id,
				getUserProfile(request).getPrincipal().getProfileId());
	}

	/**
	 * 
	 * @param contractId
	 * @param request
	 * @param lockKey
	 * 
	 * @return boolean
	 * 
	 * @throws SystemException
	 */
	protected boolean isLockObtained(final int contractId,
			final HttpServletRequest request, final String lockKey)
			throws SystemException {

		UserInfo lockOwnerUserInfo = null;
		try {
			lockOwnerUserInfo = SecurityServiceDelegate.getInstance()
					.searchByProfileId(
							getUserProfile(request).getPrincipal(),
							LockServiceDelegate.getInstance().getLockInfo(
									lockKey, lockKey + contractId)
									.getLockUserProfileId());
		} catch (final Exception exception) {
			logger.warn(exception);
		}
		if (lockOwnerUserInfo == null
				|| lockOwnerUserInfo.getProfileId() != getUserProfile(request)
						.getPrincipal().getProfileId()) {
			return false;
		}

		return true;
	}

	/**
	 * Method to check if the User has Tpa User Manager permissions
	 * 
	 * @param userInfo
	 * @param tpaStandardFeeScheduleForm
	 * @return isTpaUserManager
	 * @throws SystemException
	 */

	protected boolean isTpaUserManager(UserInfo userInfo,
			String tpaFirmId)
			throws SystemException {
		boolean isTpaUserManager = false;

		for (TPAFirmInfo tpaFirm : userInfo.getTpaFirmsAsCollection()) {
			if (Integer.parseInt(tpaFirmId) == tpaFirm.getId()) {
				Collection<PermissionType> permissions = tpaFirm
						.getContractPermission().getRole().getPermissions();
				for (PermissionType perm : permissions) {
					if (StringUtils
							.equals(
									PermissionType.getPermissionCode(perm),
									PermissionType
											.getPermissionCode(PermissionType.MANAGE_TPA_USERS))) {
						isTpaUserManager = true;
						break;
					}
				}

				break;
			}
		}
		return isTpaUserManager;
	}

	/**
	 * Method to trigger IpiHostingCustomScheduleChangeEvent for 
	 * Custom fee schedule changes
	 * 
	 * @param clazz
	 * @param methodName
	 * @param contractId
	 * @param userProfileId
	 * @param isResetToStandardScedule
	 * @throws SystemException
	 */
	protected void fireIpiHostingCustomScheduleChangeEvent(Class clazz,
			String methodName, int contractId, long userProfileId,
			boolean isResetAndSchedulesAreEqual) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> fireIpiHostingCustomScheduleChangeEvent");
		}

		IpiHostingCustomScheduleChangeEvent CustScheduleChangeEvent = new IpiHostingCustomScheduleChangeEvent(
				clazz.getName(), methodName);
		CustScheduleChangeEvent.setContractId(contractId);
		CustScheduleChangeEvent.setInitiator(userProfileId);
		CustScheduleChangeEvent
				.setIsResetAndSchedulesAreEqualInd(isResetAndSchedulesAreEqual ? Constants.YES
						: Constants.NO);
		EventClientUtility.getInstance(Environment.getInstance().getAppId())
				.prepareAndSendJMSMessage(CustScheduleChangeEvent);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- fireIpiHostingCustomScheduleChangeEvent");
		}
	}

	protected boolean checkIfUserObtainedLockOnTpaFirm(String tpaFirmId,
			HttpServletRequest request) throws SystemException {
		// If TPA id is not in form then this request is from book mark
		if (StringUtils.isBlank(tpaFirmId)) {
			return false;
		}
		// check lock already exists or not.
		final boolean lockObtained = isLockObtained(
				Integer.parseInt(tpaFirmId), request,
				LockHelper.EDIT_TPA_STANDARD_SCHEDULE_PAGE);

		return lockObtained;
	}

	protected boolean checkIfUserObtainedLockOnContract(String ContractId,
			HttpServletRequest request) throws SystemException {
		// If TPA id is not in form then this request is from book mark
		if (StringUtils.isBlank(ContractId)) {
			return false;
		}
		// check lock already exists or not.
		final boolean lockObtained = isLockObtained(Integer
				.parseInt(ContractId), request,
				LockHelper.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);

		return lockObtained;
	}
	
	protected List<FeeUIHolder> removeBlankFeeObjects(List<FeeUIHolder> fees) {
		List<FeeUIHolder> feeList = new ArrayList<FeeUIHolder>();
		for(FeeUIHolder feeUi : fees) {
			if(StringUtils.isNotEmpty(feeUi.getFeeCode()) ||  !feeUi.isEmpty()){
				feeList.add(feeUi);
			}
		}
		return feeList;
	}
	
	/**
	 *  load plan provisions to the respective features.
	 */
	protected void loadPlanProvisionsToTpaStandardFees(int contractId,
			List<FeeUIHolder> tpaStandardFees)
			throws SystemException {
		for (FeeUIHolder fee : tpaStandardFees) {
			if (StringUtils.isNotBlank(fee.getContractCustomizedFeeVO()
					.getValidatorClass())) {
				ContractValidator validator = ContractValidatorFactory
						.getInstance().getContractValidatorInstance(
								fee.getContractCustomizedFeeVO()
										.getValidatorClass());

				fee.setDisabled(!validator.validate(contractId));
			}
		}

	}
	
	public static String[] getLockOwnerDisplayName(UserProfile loggedInUser, UserInfo lockOwnerUserInfo) {
        
		String firstName = StringUtils.EMPTY;
		String lastName = StringUtils.EMPTY;

        if (lockOwnerUserInfo != null) {
            if (lockOwnerUserInfo.getRole().isExternalUser()) {
            	firstName = lockOwnerUserInfo.getFirstName();
            	lastName = lockOwnerUserInfo.getLastName();
            } else {
                if (loggedInUser.getRole().isExternalUser()) {
                	firstName = LockHelper.JH_REP_LABEL;
                } else {
                	firstName = lockOwnerUserInfo.getFirstName();
                	lastName = new StringBuffer(lockOwnerUserInfo.getLastName()).append(" ")
                            .append(LockHelper.JH_REP_LABEL).toString();
                }
            }
        } else {
        	firstName = LockHelper.JH_REP_LABEL;
        } 

        return new String[] {firstName, lastName};
    }
	
	
	/**
	 * get last update details
	 * 
	 * @param form
	 * @throws SystemException
	 * @throws NumberFormatException
	 */
	protected void setLastUpdateDetails(UserProfile loggedInUser,
			BaseFeeScheduleForm form, FeeScheduleType feeScheduleType)
			throws SystemException {
		String lstUpdatedUserId = StringUtils.EMPTY;
		FeeScheduleUpdateDetails feeScheduleDetails = FeeServiceDelegate
				.getInstance(CommonConstants.PS_APPLICATION_ID)
				.getFeeScheuleUpdateDetails(form.getSelectedContract(),
						String.valueOf(form.getTpaId()), feeScheduleType);

		if (feeScheduleDetails != null) {
			form.setLastUpdateDate(feeScheduleDetails.getLastUpdatedTs() == null ? null
					: new Date(feeScheduleDetails.getLastUpdatedTs().getTime()));
			UserInfo updatedUserInfo = SecurityServiceDelegate
					.getInstance()
					.getUserProfileByProfileId(
							new Long(feeScheduleDetails.getLastUpdatedUserId()));
			if (loggedInUser.getRole().isExternalUser()) {
				if (Constants.SYSTEM_USER_PROFILE_ID.equals(feeScheduleDetails
						.getLastUpdatedUserId())) {
					lstUpdatedUserId = Constants.ADMINISTRATION;
				} else if (updatedUserInfo == null) {
					lstUpdatedUserId = StringUtils.EMPTY;
				} else if (updatedUserInfo.getRole().isInternalUser()) {
					lstUpdatedUserId = Constants.JOHN_HANCOCK_REPRESENTATIVE;
				} else {
					lstUpdatedUserId = updatedUserInfo.getFirstName()
							.concat(" ").concat(updatedUserInfo.getLastName());
				}
			} else {
				if (Constants.SYSTEM_USER_PROFILE_ID.equals(feeScheduleDetails
						.getLastUpdatedUserId())) {
					lstUpdatedUserId = Constants.ADMINISTRATION;
				} else if (updatedUserInfo != null) {
					lstUpdatedUserId = updatedUserInfo.getFirstName()
							.concat(" ").concat(updatedUserInfo.getLastName());
				} else {
					lstUpdatedUserId = StringUtils
							.trimToEmpty(feeScheduleDetails
									.getLastUpdatedUserName());
				}
			}
		}

		form.setLastUpdatedUserId(lstUpdatedUserId);

	}
	
	protected Map<Integer, String> getAllTpaFirmsForTpaLoginUser(
			HttpServletRequest request) throws SystemException {

		UserProfile userProfile = getUserProfile(request);
		Map<Integer, String> tpaFirmMap = new TreeMap<Integer, String>();
		
			String companyCode = Constants.COMPANY_NAME_NY
					.equalsIgnoreCase(Environment.getInstance()
							.getSiteLocation()) ? Constants.COMPANY_ID_NY
					: Constants.COMPANY_ID_US;

			tpaFirmMap = TPAServiceDelegate.getInstance()
					.retrieveTpaFirmsByTPAUserProfileId(
							userProfile.getPrincipal().getProfileId(),
							companyCode);
		
		return tpaFirmMap;
	}
	
	/**
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations
	 */

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.bind( request);
		binder.addValidators(psValidatorFWInput);
	} 
}
