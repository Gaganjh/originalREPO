package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.security.SystemUser;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.domain.Withdrawal;
import com.manulife.pension.service.withdrawal.helper.PayeeFieldDef;
import com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef;
import com.manulife.pension.service.withdrawal.valueobject.Activity;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * Created on Jan 22, 2007 - snowdde
 * 
 * This class is the action used to show activity history for withdrawals
 */

@Controller
@RequestMapping( value = "/withdrawal")
public class ActivityHistoryController extends BaseWithdrawalController {
	@ModelAttribute(" activityHistoryForm")
	public  ActivityHistoryForm populateForm()
	{
		return new  ActivityHistoryForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("default","/withdrawal/activityhistory/activityHistory.jsp");
	} 
	
	
	
	private static final String CLASS_NAME = ActivityHistoryController.class
			.getName();
	
	private static final String TPA = "TPA";
	private static final String PLAN_SPONSOR = "Plan Sponsor";
	private static final String JH = "John Hancock Representative";

	
	 @RequestMapping(value ="/activityHistory/",  method =  {RequestMethod.GET}) 
		public String doDefault(@Valid @ModelAttribute("withdrawalActivityHistoryForm") ActivityHistoryForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 
		 if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if(errDirect!=null){
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
				}
			}
	
		WithdrawalRequest wr = null;
		Withdrawal wd = null;
		WithdrawalServiceDelegate delegate = getWithdrawalServiceDelegate();
		

		Integer submissionId = new Integer(request.getParameter("submissionId"));
		wr = delegate.readWithdrawalRequestForView(submissionId,
				getUserProfile(request).getPrincipal());
		//This variable is to identify whether the current logged in user is internal user or not.
		boolean isCurrentUserInternal = getUserProfile(request).getPrincipal().getRole().isInternalUser();
 		wd = new Withdrawal(wr);
 		actionForm.setActivityHistory(wd.getWithdrawalRequest()
				.getActivityHistory());
		Collection<Activity> activities = formatForDisplay(wd
				.getWithdrawalRequest().getActivityHistory().getActivities());
		translateDeclarations(activities);
		translateUserNames(request, wd.getWithdrawalRequest(), isCurrentUserInternal);
		translateCodes(activities);
		translateSummaries(wd.getWithdrawalRequest().getActivityHistory()
				.getSummaries());

		return forwards.get(ACTION_FORWARD_DEFAULT);
	}

	private void translateSummaries(List<WithdrawalActivitySummary> summaries) {
		Map<String, String> actionMap = new HashMap<String, String>();
		actionMap.put(WithdrawalActivitySummary.ACTION_CODE_APPROVED, "Approved");
		actionMap.put(WithdrawalActivitySummary.ACTION_CODE_DELETED, "Deleted");
		actionMap.put(WithdrawalActivitySummary.ACTION_CODE_DENIED, "Denied");
		actionMap.put(WithdrawalActivitySummary.ACTION_CODE_EXPIRED, "Expired");
		actionMap.put(WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_APPROVAL,
				"Sent For Approval");
		actionMap.put(WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_REVIEW,
				"Sent For Review");

		for (WithdrawalActivitySummary summary : summaries) {
			summary.setActionName(actionMap.get(summary.getActionCode()));
		}

	}

	private void translateUserNames(HttpServletRequest request,
			WithdrawalRequest wr, boolean isCurrentUserInternal) throws SystemException {
		Map<Integer, UserInfo> userInfoMap = new HashMap<Integer, UserInfo>();
		try {
			for (ActivitySummary summary : wr.getActivityHistory()
					.getSummaries()) {
				if (userInfoMap.get(summary.getCreatedById()) == null) {
					userInfoMap.put(summary.getCreatedById(), new UserInfo());
				}
			}
			for (Activity activity : wr.getActivityHistory().getActivities()) {
				if (activity.getShowUserIdAndLastUpdated()) {
					if (userInfoMap.get(activity.getLastUpdateUserProfileId()) == null) {
						userInfoMap.put(activity.getLastUpdateUserProfileId(),
								new UserInfo());
					}
				}
			}
			
			final int systemProfileId = SystemUser.SUBMISSION.getProfileId();
			final int systemPowProfileId = SystemUser.DATABASE.getProfileId();
			
			for (Integer id : userInfoMap.keySet()) {

				// pass in null for principal - no restrictions.
				if ((id.intValue() == systemProfileId) || (id.intValue() == systemPowProfileId)) {
					continue;
				}
				
				UserInfo uinfo = SecurityServiceDelegate.getInstance()
				.searchByProfileId(null, id.longValue());
				//CL 135724  i:withdrawal activity history error
				if (uinfo == null) {
					uinfo = SecurityServiceDelegate.getInstance()
							.getUserDetailsFromDatabase(null, id.longValue());
				}
				userInfoMap.put(id, uinfo);
	

			}
			for (WithdrawalActivitySummary summary : wr.getActivityHistory()
					.getSummaries()) {
				if (summary.getCreatedById().intValue() == systemProfileId) {
					summary.setUserName("System");
				} else if(summary.getCreatedById().intValue() == systemPowProfileId) {
					summary.setUserName(wr.getFirstName() + " " 
										+ wr.getLastName()
										+ ", "
										+ "Participant");
				} else {
					UserInfo uinfo = userInfoMap.get(summary.getCreatedById());
					String userName = getUserDetails(uinfo, isCurrentUserInternal);
					
					if(uinfo.getRole().isInternalUser() && isCurrentUserInternal){
						String[] userArray = userName.split(",");
						summary.setInternalUserName(userArray[0]);
						summary.setUserName(StringUtils.trim(userArray[1]));
					}else {
						summary.setUserName(userName);
					}
				}
			}
			for (Activity activity : wr.getActivityHistory().getActivities()) {
			
				if (activity.getShowUserIdAndLastUpdated()) {
					if (activity.getLastUpdateUserProfileId().intValue() == systemProfileId) {
						activity.setChangedBy("System");
					} else if(activity.getLastUpdateUserProfileId().intValue() == systemPowProfileId) {
						activity.setChangedBy("Participant/data");
					} else {
						UserInfo uinfo = userInfoMap.get(activity
								.getLastUpdateUserProfileId());
						String userName = getUserDetails(uinfo, isCurrentUserInternal);
						
						if(uinfo.getRole().isInternalUser() && isCurrentUserInternal){
							String[] userArray = userName.split(",");
							activity.setInternalUserName(userArray[0]);
							activity.setChangedBy(StringUtils.trim(userArray[1]));
						}else {
							activity.setChangedBy(userName);
						}
					}
				}
			}
		} catch (SecurityServiceException e) {
			throw new SystemException(e, "failed to get activity history");
		}

	}

	private String getUserDetails(UserInfo uinfo, boolean isCurrentUserInternal) {
		StringBuffer sb = new StringBuffer(256);
		if (uinfo.getRole().isInternalUser()) {
			//This variable is to identify whether the current logged in user is internal user or not
			if(isCurrentUserInternal){
				sb.append(uinfo.getFirstName()).append(" ").append(
						uinfo.getLastName()).append(",");
				sb.append(JH);
			} else{
				sb.append(JH);
			}
		} else {
			sb.append(uinfo.getFirstName()).append(" ").append(
					uinfo.getLastName()).append(", ");
			if (uinfo.getRole().isTPA()) {
				sb.append(TPA);
			} else {
				sb.append(PLAN_SPONSOR);
			}
		}
		return sb.toString();
	}

	private void translateDeclarations(Collection<Activity> activities)
			throws SystemException {
		try {
			Miscellaneous decl1 = (Miscellaneous) ContentCacheManager
					.getInstance()
					.getContentById(
							ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_TAX_NOTICE_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS);
			Miscellaneous decl2 = (Miscellaneous) ContentCacheManager
					.getInstance()
					.getContentById(
							ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_IRA_PROVIDER_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS);
			Miscellaneous decl3 = (Miscellaneous) ContentCacheManager
					.getInstance()
					.getContentById(
							ContentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_WAITING_PERIOD_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS);
			Miscellaneous decl4 = (Miscellaneous) ContentCacheManager
					.getInstance()
					.getContentById(
							ContentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_APPROVE_DECLARATION_RISK_INDICATOR_TEXT, 
							ContentTypeManager.instance().MISCELLANEOUS);
			Map<String, String> declMap = new HashMap<String, String>();
			declMap.put(WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE,
					decl1.getTitle());
			declMap
					.put(
							WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE,
							decl2.getText());
			declMap
					.put(
							WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE,
							decl3.getText());
			declMap
					.put(
							WithdrawalRequestDeclaration.AT_RISK_TRANSACTION_TYPE_CODE,
							decl4.getText());
			
			for (Activity activity : activities) {
				if (activity.getItemNo().equals(
						WithdrawalFieldDef.DYN_DECLARATION_TYPE)) {
					activity.setItemName(declMap.get(activity
							.getSecondaryName()));
				}
			}
		} catch (ContentException e1) {
			throw new SystemException(e1, "failed to call content service");
		}

	}

	/**
	 * This method is used to format values to be displayed to the user.
	 * 
	 * @param activities
	 * @return
	 */
	public Collection<Activity> formatForDisplay(Collection<Activity> activities) {

		boolean payee = false;
		Collection<Activity> formattedActivities = new ArrayList<Activity>();
		for (Iterator iter = activities.iterator(); iter.hasNext();) {
			Activity element = (Activity) iter.next();
			payee = element.getItemNo().equals(
					WithdrawalFieldDef.DYN_PAYEE_TYPE);
			if (payee) {
				PayeeFieldDef field = PayeeFieldDef
						.getFieldFromItemNumber(element.getSecondaryNo());
				if (field == PayeeFieldDef.P_BANK_TRANSIT_NUMBER) {
					element
							.setOriginalValue(StringUtils
									.leftPad(
											element.getOriginalValue(),
											WithdrawalRequestPayee.BANK_ABA_NUMBER_LENGTH,
											"0"));
					element
							.setCurrentValue(StringUtils
									.leftPad(
											element.getCurrentValue(),
											WithdrawalRequestPayee.BANK_ABA_NUMBER_LENGTH,
											"0"));

				}
			}

			formattedActivities.add(element);

		}
		return formattedActivities;
	}

	public void translateCodes(Collection<Activity> activities)
			throws SystemException {

		Map lookupData;
		try {
			lookupData = WithdrawalServiceDelegate.getInstance().getLookupData(
					null, StringUtils.EMPTY, getAllLookupKeys());
		} catch (SystemException e) {
			throw new SystemException(e, CLASS_NAME, "populateNames",
					"failed to call environment service");
		}

		Collection<DeCodeVO> iraCodes = new ArrayList<DeCodeVO>();
		Collection<DeCodeVO> payMethodCodes = new ArrayList<DeCodeVO>();
		Collection<DeCodeVO> bankTypeCodes = new ArrayList<DeCodeVO>();
		Collection<DeCodeVO> booleanCodes = new ArrayList<DeCodeVO>();

		iraCodes.add(new DeCodeVO(
				WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE, "Other"));
		iraCodes.add(new DeCodeVO(
				WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE,
				"PenChecks"));
		iraCodes.add(new DeCodeVO(
				WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE,
				"Wealth Management Systems, Inc. "));

		payMethodCodes.add(new DeCodeVO(
				WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, "ACH"));
		payMethodCodes.add(new DeCodeVO(
				WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, "Wire"));
		payMethodCodes.add(new DeCodeVO(
				WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, "Check"));

		bankTypeCodes.add(new DeCodeVO(
				WithdrawalRequestPayee.CHECKING_ACCOUNT_TYPE_CODE, "Checking"));
		bankTypeCodes.add(new DeCodeVO(
				WithdrawalRequestPayee.SAVINGS_ACCOUNT_TYPE_CODE, "Savings"));

		booleanCodes.add(new DeCodeVO("true", "Yes"));
		booleanCodes.add(new DeCodeVO("false", "No"));

		for (Activity activity : activities) {
			boolean moneyType = activity.getItemNo().equals(
					WithdrawalFieldDef.DYN_MONEY_TYPE);
			boolean declaration = activity.getItemNo().equals(
					WithdrawalFieldDef.DYN_DECLARATION_TYPE);
			boolean payee = activity.getItemNo().equals(
					WithdrawalFieldDef.DYN_PAYEE_TYPE);
			boolean wd = !moneyType && !declaration && !payee;
			boolean isWithdrawlStateOfResidence = false;
			boolean systemOfRecord = false;
			Collection<DeCodeVO> theList = null;
			Collection<LabelValueBean> countryList = null;
			boolean found = false;

			if (wd) {
				WithdrawalFieldDef field = WithdrawalFieldDef
						.getFieldFromItemNumber(activity.getItemNo());
				if (field == WithdrawalFieldDef.STATE_OF_RESIDENCE) {
					isWithdrawlStateOfResidence = true;
					theList = (Collection<DeCodeVO>) lookupData
							.get(CodeLookupCache.USA_STATE_WITH_MILITARY_TYPE);
				} else if (field == WithdrawalFieldDef.HARDSHIP_REASON) {
					theList = (Collection) lookupData
							.get(CodeLookupCache.HARDSHIP_REASONS);
				} else if (field == WithdrawalFieldDef.IRA_PROVIDER) {
					theList = iraCodes;
				} else if (field == WithdrawalFieldDef.OUTSTANDING_LOANS_OPTION) {
					theList = (Collection<DeCodeVO>) lookupData
							.get(CodeLookupCache.LOAN_OPTION_TYPE);
				} else if (field == WithdrawalFieldDef.IRS_DIST_CODE_FOR_LOANS) {
					theList = (Collection<DeCodeVO>) lookupData
							.get(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS);
				} else if (field == WithdrawalFieldDef.AMOUNT_TYPE_CODE) {
					theList = (Collection<DeCodeVO>) lookupData
							.get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE);
				} else if (field == WithdrawalFieldDef.TPA_FEE_TYPE) {
					theList = (Collection<DeCodeVO>) lookupData
							.get(CodeLookupCache.TPA_TRANSACTION_FEE_TYPE);
				} else if (field == WithdrawalFieldDef.OPTION_FOR_UNVESTED_AMOUNTS) {
					theList = (Collection<DeCodeVO>) lookupData
							.get(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS);
				} else if (field == WithdrawalFieldDef.TEN99R_COUNTRY) {
					countryList = (Collection<LabelValueBean>) lookupData
							.get(CodeLookupCache.COUNTRY_COLLECTION_TYPE);
				} else if (field.getType().equals(
						WithdrawalFieldDef.Type.BOOLEAN)) {
					theList = booleanCodes;
				}
				systemOfRecord = field.getSystemOfRecord();
			} else if (payee) {
				PayeeFieldDef field = PayeeFieldDef
						.getFieldFromItemNumber(activity.getSecondaryNo());

				if (field == PayeeFieldDef.P_IRS_DIST_CODE) {
					theList = (Collection<DeCodeVO>) lookupData
							.get(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS);
				} else if (field == PayeeFieldDef.P_PAYMENT_METHOD_CODE) {
					theList = payMethodCodes;
				} else if (field == PayeeFieldDef.P_BANKACCOUNT_TYPE_CODE) {
					theList = bankTypeCodes;
				} else if (field == PayeeFieldDef.P_COUNTRY) {
					countryList = (Collection<LabelValueBean>) lookupData
							.get(CodeLookupCache.COUNTRY_COLLECTION_TYPE);
				} else if (field.getType().equals(
						WithdrawalFieldDef.Type.BOOLEAN)) {
					theList = booleanCodes;
				}
			}

			if (theList != null) {
				String newCurrent = null;
				String newOriginal = null;
				String newSOR = null;
				String oldCurrent = activity.getCurrentValue();
				String oldOriginal = activity.getOriginalValue();
				String oldSOR = activity.getSystemOfRecordValue();
				if (isWithdrawlStateOfResidence
						&& StringUtils
								.equals(
										oldCurrent,
										WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US)) {
					newCurrent = oldCurrent + " - " + "Outside USA";

				} else {
					for (DeCodeVO vo : theList) {
						if (vo.getCode().equals(oldCurrent)) {
							if (isWithdrawlStateOfResidence) {
								newCurrent = oldCurrent + " - "
										+ vo.getDescription();
							} else {
								newCurrent = vo.getDescription();
							}
							found = true;
							break;
						}
					}
					if (!found) {
						newCurrent = oldCurrent;
					}

				}
				if (isWithdrawlStateOfResidence
						&& StringUtils
								.equals(
										oldOriginal,
										WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US)) {
					newOriginal = oldOriginal + " - " + "Outside USA";

				} else {
					found = false;
					for (DeCodeVO vo : theList) {
						if (vo.getCode().equals(oldOriginal)) {
							if (isWithdrawlStateOfResidence) {
								newOriginal = oldOriginal + " - "
										+ vo.getDescription();
							} else {
								newOriginal = vo.getDescription();
							}
							found = true;
							break;

						}
					}
					if (!found) {
						newOriginal = oldOriginal;
					}
				}

				if (systemOfRecord) {
					if (isWithdrawlStateOfResidence
							&& StringUtils
									.equals(
											oldSOR,
											WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US)) {
						newSOR = oldSOR + " - " + "Outside USA";

					} else {
						found = false;
						for (DeCodeVO vo : theList) {
							if (vo.getCode().equals(oldSOR)) {
								if (isWithdrawlStateOfResidence) {
									newSOR = oldSOR + " - "
											+ vo.getDescription();
								} else {
									newSOR = vo.getDescription();
								}
								found = true;
								break;
							}
						}
						if (!found) {
							newSOR = oldSOR;
						}
					}
					activity.setSystemOfRecordValue(newSOR);
				}
				activity.setOriginalValue(newOriginal);
				activity.setCurrentValue(newCurrent);
			}
			if (countryList != null) {
				String newCurrent = null;
				String newOriginal = null;
				String newSOR = null;
				String oldCurrent = activity.getCurrentValue();
				String oldOriginal = activity.getOriginalValue();
				String oldSOR = activity.getSystemOfRecordValue();
				found = false;
				for (LabelValueBean vo : countryList) {
					if (vo.getValue().equals(oldCurrent)) {
						newCurrent = vo.getLabel();
						found = true;
						break;
					}
				}
				if (!found) {
					newCurrent = oldCurrent;
				}
				found = false;
				for (LabelValueBean vo : countryList) {
					if (vo.getValue().equals(oldOriginal)) {
						newOriginal = vo.getLabel();
						found = true;
						break;
					}
				}
				if (!found) {
					newOriginal = oldOriginal;
				}
				if (systemOfRecord) {
					found = false;
					for (LabelValueBean vo : countryList) {
						if (vo.getValue().equals(oldSOR)) {
							newSOR = vo.getLabel();
							found = true;
							break;
						}
					}
					activity.setSystemOfRecordValue(newSOR);
					if (!found) {
						newSOR = oldSOR;
					}
				}
				activity.setOriginalValue(newOriginal);
				activity.setCurrentValue(newCurrent);
			}
		}
	}
	@RequestMapping(value ="/activityHistory/", params= {"action=delete"}, method =  {RequestMethod.POST}) 
	public String doDelete(@Valid @ModelAttribute("withdrawalActivityHistoryForm") ActivityHistoryForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
			}
		}
		      String forward=super.doDelete( actionForm, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	@RequestMapping(value ="/activityHistory/", params= {"action=printPDF"}, method =  {RequestMethod.GET}) 
	public String doPrintPDF(@Valid @ModelAttribute("withdrawalActivityHistoryForm") ActivityHistoryForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
			}
		}
		      String forward=super.doPrintPDF( actionForm, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	
}
