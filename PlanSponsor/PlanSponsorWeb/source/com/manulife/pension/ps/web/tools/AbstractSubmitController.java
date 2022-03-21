package com.manulife.pension.ps.web.tools;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.common.MoneyBean;
import com.manulife.pension.lp.model.gft.CashAccount;
import com.manulife.pension.lp.model.gft.DirectDebitAccount;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.lp.model.gft.PaymentAccount;
import com.manulife.pension.lp.model.gft.PaymentInstruction;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.common.util.log.FileUploadEventLog;
import com.manulife.pension.ps.common.util.log.SubmissionEventLog;
import com.manulife.pension.ps.service.submission.util.SubmissionEventLogFactory;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessIFileException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.contract.valueobject.ContractPaymentInfoVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.util.FormatUtils;
import com.manulife.pension.util.log.LogEventException;
import com.manulife.pension.util.log.LogUtility;

/**
 *
 * Handles the submission upload process. This is a adapted from FileUploadAction.
 * This is the parent class for the 4 upload actions.
 *
 * @author Tony Tomasone
 *
 */
public abstract class AbstractSubmitController extends PsAutoController {

	protected static final String INPUT = "input";
	
	protected static final String REFRESH = "refresh";

	protected static final String CONFIRMATION = "confirm";

	protected static final String CONFIRMATION_PAGE = "confirmPage";

	protected static final String TOOLS = "tools";

	protected static final String SUBMISSION_HISTORY = "subHistory";

	// 16 days which is 15 days after the current date.
	protected static final int DELTA_END_DAYS = 16;

	protected static final int MAX_FILE_NAME_LENGTH = 256;
	protected static final int MAX_FILE_NAME_LENGTH_VESTING = 200;

	private static final int MAX_WIDTH = 9999;

	private static int BANK_CLOSE_HOUR = 16;

	private static int BANK_CLOSE_MINUTE = 0;

	private static int validChars[][] = {
		{32,33},
		{35,41},
		{43,46},
		{48,57},
		{59,59},
		{61,61},
		{64,91},
		{93,123},
		{125,126}
	};

	private static char SUB_CHAR = 'x';
	/**
	 * Constructor.
	 */
	public AbstractSubmitController(Class clazz) {
		super(clazz);
	}

	/**
	 * Forward to the confirmation page. This action is needed because it's the
	 * result of a REDIRECT after a POST.
	 *
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public String doConfirm(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		//TODO
		//ActionForward forward = findForward( CONFIRMATION_PAGE);
		String forward = findForward(CONFIRMATION_PAGE);
		UserProfile userProfile = getUserProfile(request);

		// lets check the permissions
		if(!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()) {
			//TODO
			//forward = findForward( TOOLS);
			forward=findForward( TOOLS);
		}

		return forward;
	}

	/**
	 * Simply refresh the page. This action is used when we try to perform a
	 * REDIRECT after a POST.
	 *
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return The input forward.
	 */
	public String doRefresh(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doRefresh");
		}

		UserProfile userProfile = getUserProfile(request);

		// lets check the permissions
		if(!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()) {
			//TODO
			//return findForward( TOOLS);
			return findForward( TOOLS);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doRefresh");
		}
		//TODO
		//return mapping.getInputForward();
		return "input";
	}

	protected void setupUserProfile(UserProfile userProfile, AutoForm form)
			throws SystemException, UnableToAccessIFileException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> setupUserProfile");

		IFileConfig ifileConfig = new IFileConfig();

		Calendar calendar = adjustDate4pm(null);

		int currentYear = calendar.get(Calendar.YEAR);

		//set it up for the 1st time

		//setup years
		ArrayList years = new ArrayList();

		years.add(String.valueOf(currentYear));
		calendar.add(Calendar.DATE, 15);
		if (calendar.get(Calendar.YEAR) > currentYear) {
			years.add(String.valueOf(currentYear));
		}
		ifileConfig.setYears(years);

		setupAccounts(userProfile, ifileConfig, form);

		//set it up in userProfile
		userProfile.getContractProfile().setIfileConfig(ifileConfig);

		if (logger.isDebugEnabled())
			logger.debug("exit <- setupUserProfile");
	}

	/**
	 * Insert the method's description here. Creation date: (8/23/2001 11:47:59
	 * AM)
	 *
	 * @return boolean
	 */
	protected void setupAccounts(UserProfile userProfile, IFileConfig ifileConfig, AutoForm form)
			throws SystemException {

		SubmissionUploadForm theForm = (SubmissionUploadForm) form;

		ArrayList accounts = new ArrayList();
		ArrayList paymentAccounts = new ArrayList();

		//first reset old values
		ifileConfig.setCashAccountPresent(false);
		ifileConfig.setDirectDebitAccountPresent(false);
		ifileConfig.setPaymentAccounts(paymentAccounts);
		ifileConfig.setAccounts(accounts);

		//check permissions
		if (!(userProfile.isAllowedDirectDebit() || userProfile
				.isAllowedCashAccount()))
			return;

		ContractServiceDelegate service = ContractServiceDelegate.getInstance();

		// retrieve direct deposit accounts
		if (userProfile.isAllowedDirectDebit()) {
		    /*
		     * Setup direct debit accounts.
		     */
		    boolean hasDirectDebitAccounts = false;
		    List directDebitAccounts = userProfile.getRole().getDirectDebitAccounts();
		    for (Iterator it = directDebitAccounts.iterator(); it.hasNext();) {
		        DirectDebitAccount account = (DirectDebitAccount)it.next();
		        /*
		         * Only active accounts are considered.
		         */
		        if (account.isActive()) {
		            hasDirectDebitAccounts = true;
		            paymentAccounts.add(account);
		        }
		    }

			ifileConfig.setDirectDebitAccountPresent(hasDirectDebitAccounts);
		}

		ContractPaymentInfoVO paymentInfoVO
			= service.getContractPaymentInfo(userProfile.getCurrentContract().getContractNumber());

		theForm.setPaymentInfo(paymentInfoVO);

		// add a CashAccount if it is neccessary
		if (userProfile.isAllowedCashAccount()) {
			paymentAccounts.add(new CashAccount(Environment.getInstance().isNY()));
		}

		// map it
		for (int i = 0; i < paymentAccounts.size(); i++) {
			PaymentAccount paccount = (PaymentAccount) paymentAccounts.get(i);
			PaymentAccountBean acc = new PaymentAccountBean(
					SubmissionUploadForm.PAYMENT_INSTRUCTION_ACCOUNT_PREFIX
							+ String.valueOf(i), FormatUtils.formatAccountName(
							paccount, MAX_WIDTH), paccount.getAccountType(), "0.00");
			accounts.add(acc);

			if (paccount instanceof CashAccount) {
				ifileConfig.setCashAccountPresent(true);
			}
		}

		//set it in user profile
		ifileConfig.setPaymentAccounts(paymentAccounts);
		ifileConfig.setAccounts(accounts);
	}

	protected void logUploadEvent(GFTUploadDetail result, Date startTime, Date endTime, Principal principal, String methodName) {

			FileUploadEventLog eventLog = new FileUploadEventLog();
			eventLog.setPrincipal(principal);
			eventLog.setClassName(this.getClass().getName());
			eventLog.setMethodName(methodName);
			eventLog.setLogData(prepareLogData(result, startTime, endTime));
			eventLog.setUserName(result.getUserName());
			try {
				eventLog.log();
			} catch (LogEventException e) {
				SystemException se = new SystemException(e, this.getClass().getName(),
					methodName, "Problem occurred during logging File Upload Event" + " user name: " +
					result.getUserName());
			throw ExceptionHandlerUtility.wrap(se);
		}
	}

	/**
	 * @param form
	 * @param userProfile
	 * @return
	 */
	protected static GFTUploadDetail getGFTUploadDetail(UserProfile userProfile) {

		GFTUploadDetail gftUploadDetail = new GFTUploadDetail();
		gftUploadDetail.setUserSSN(String.valueOf(userProfile.getPrincipal().getProfileId()));
		gftUploadDetail.setUserName(userProfile.getPrincipal().getFirstName() + " " + userProfile.getPrincipal().getLastName().trim());

		gftUploadDetail.setUserType(isTPARole(userProfile)? GFTUploadDetail.USER_TYPE_TPA : GFTUploadDetail.USER_TYPE_CLIENT);
		gftUploadDetail.setUserTypeID(userProfile.getClientId());

		//TODO: this should be client name
		gftUploadDetail.setUserTypeName("");
		gftUploadDetail.setUserLocation(Environment.getInstance().isNY() ? GFTUploadDetail.USER_LOCATION_NY : GFTUploadDetail.USER_LOCATION_USA);
		gftUploadDetail.setUploadStartTimestamp(new Date());
		gftUploadDetail.setContractNumber(Integer.toString(userProfile.getCurrentContract().getContractNumber()));
		gftUploadDetail.setContractName(userProfile.getCurrentContract().getCompanyName());

		return gftUploadDetail;
	}

	/**
	 * @return boolean
	 */
	protected static boolean isTPARole(UserProfile userProfile) {
		 return userProfile.getPrincipal().getRole() instanceof ThirdPartyAdministrator;
	}

	protected static PaymentInstruction[] retrievePaymentInstructions(SubmissionUploadForm form, UserProfile userProfile)
			throws SystemException {

		PaymentInstruction[] result = null;

		if (userProfile.isAllowedCashAccount() || userProfile.isAllowedDirectDebit()) {
			result = new PaymentInstruction[0];
			if ( userProfile.getContractProfile().getIfileConfig() == null ) {
				SystemException se = new SystemException(new Exception(), AbstractSubmitController.class.getName(), "retrievePaymentInstructions", "ifileConfig is null in UserProfile, cannot determine payment accounts");
				throw se;
			}
			List paymentAccounts = userProfile.getContractProfile().getIfileConfig().getPaymentAccounts();
			if (paymentAccounts != null && paymentAccounts.size() > 0) {
				List instructions = new ArrayList();

				for (int i=0; i<paymentAccounts.size(); i++) {
					double contributionDoubleValue = 0;
					double billDoubleValue = 0;
					double creditDoubleValue = 0;
					try {
						contributionDoubleValue = NumberFormat.getCurrencyInstance().parse("$"+form.getAmounts(i)).doubleValue();
						billDoubleValue = NumberFormat.getCurrencyInstance().parse("$"+form.getBillAmounts(i)).doubleValue();
						creditDoubleValue = NumberFormat.getCurrencyInstance().parse("$"+form.getCreditAmounts(i)).doubleValue();
					} catch (ParseException e) {
						//this shouldn't really happen here as we validate before, but...
						SystemException se = new SystemException(e, AbstractSubmitController.class.getName(), "retrievePaymentInstructions", e.getMessage());
						LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
					}
					if (contributionDoubleValue>0) {
						instructions.add(new PaymentInstruction((PaymentAccount)paymentAccounts.get(i), new MoneyBean(contributionDoubleValue),PaymentInstruction.CONTRIBUTION));
					}
					if (billDoubleValue>0) {
						instructions.add(new PaymentInstruction((PaymentAccount)paymentAccounts.get(i), new MoneyBean(billDoubleValue),PaymentInstruction.BILL_PAYMENT));
					}
					if (creditDoubleValue>0) {
						instructions.add(new PaymentInstruction((PaymentAccount)paymentAccounts.get(i), new MoneyBean(creditDoubleValue),PaymentInstruction.TEMPORARY_CREDIT));
					}
				}
				if (!instructions.isEmpty()) {
					result = new PaymentInstruction[instructions.size()];
					instructions.toArray(result);
				}
			}
		}
		return result;
	}

	protected static Date retrieveRequestedEffectiveDate(SubmissionUploadForm form, UserProfile userProfile) {
		Date result = null;
		SimpleDateFormat dateParser = new SimpleDateFormat("MM/dd/yyyy");
		if (userProfile.isAllowedCashAccount() || userProfile.isAllowedDirectDebit()) {
			try {
				result = dateParser.parse(form.getRequestEffectiveDate());

				//check for 4pm and weekend
				Calendar calendar = Calendar.getInstance();
				Calendar now = Calendar.getInstance();
				calendar.setTime(result);
				if (calendar.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
					//readjust again for 4pm due to 3:55 situation when user waits and we skip 4pm
					now = adjustDate4pm(now);
					return now.getTime();
				}
			} catch(ParseException e){
				//this should not happen here
				SystemException se = new SystemException(e, AbstractSubmitController.class.getName(), "retrieveRequestedEffectiveDate",
						"For some reason, requested effective date is not in expected format: "
								+ form.getRequestEffectiveDate());
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			}
		}
		return result;
	}

	protected static Calendar adjustDate4pm(Calendar calendar) {
		if (calendar == null) {
			calendar = Calendar.getInstance();
		}

		calendar.add(Calendar.MINUTE, 60-BANK_CLOSE_MINUTE); //60 minutes
		calendar.add(Calendar.HOUR_OF_DAY, 23-BANK_CLOSE_HOUR); //23 hrs, + 60 minutes
		return calendar;
	}


	protected abstract String prepareLogData(GFTUploadDetail result,Date startTime, Date endTime);

	/**
	 * Filters the special characters out of the file name
	 * @param fileName
	 * @return
	 */
	protected static String filterFileName(String fileName) {
		if (fileName == null) return null;

		for (int i = 0; i < fileName.length(); i++) {
			int charValue = fileName.charAt(i);
			boolean isValid = false;
			for (int j = 0; j < validChars.length; j++) {
				if (charValue >= validChars[j][0] && charValue <= validChars[j][1]) {
					isValid = true;
					continue;
				}
			}
			// replace it with a lower case x if its not valid
			if(!isValid) fileName = fileName.replace(fileName.charAt(i),SUB_CHAR);
		}
		return fileName;
	}

	/**
	 * ensures date has valid four digit year
	 * @return boolean isYearValid;
	 */
	protected static boolean validateYear(String dateString) {

		if (null == dateString) return false;

		boolean isYearValid = true;

		String year = dateString.substring(dateString.lastIndexOf("/")+1);
		if (year.length() != 4 || Integer.parseInt(year) < 2000) {
			isYearValid = false;
		}

		return isYearValid;
	}

	/**
	 * log an event using the parameter logType as the log event class
	 * @param principal
	 * @param className
	 * @param methodName
	 * @param logData
	 * @param logType
	 */

	protected static void logEvent(Principal principal, String className, String methodName,
			String logData, Class logType) {
		try {
			SubmissionEventLog eventLog = SubmissionEventLogFactory.getInstance().createEventLog(logType);
			logEvent(eventLog, principal, className, methodName, logData);
		} catch (SystemException e) {
			SystemException se = new SystemException(e, className,
					methodName, "Problem occurred during logging; user name: " +
					principal.getUserName());
			throw ExceptionHandlerUtility.wrap(se);
		}
	}

	/**
	 * log an event using the default log event class
	 * @param principal
	 * @param className
	 * @param methodName
	 * @param logData
	 */
	protected static void logEvent(Principal principal, String className, String methodName,
			String logData) {

		try {
			SubmissionEventLog eventLog = SubmissionEventLogFactory.getInstance().createEventLog(FileUploadEventLog.class);
			logEvent(eventLog, principal, className, methodName, logData);
		} catch (SystemException e) {
			SystemException se = new SystemException(e, className,
					methodName, "Problem occurred during logging; user name: " +
					principal.getUserName());
			throw ExceptionHandlerUtility.wrap(se);
		}
	}

	private static void logEvent(SubmissionEventLog eventLog, Principal principal, String className,
			String methodName, String logData) {
		eventLog.setPrincipal(principal);
		eventLog.setClassName(className);
		eventLog.setMethodName(methodName);
		eventLog.setUserName(principal.getUserName());
		eventLog.setLogData(logData);
		try {
			eventLog.log();
		} catch (LogEventException e) {
			SystemException se = new SystemException(e, className,
					methodName, "Problem occurred during logging; user name: " +
				principal.getUserName());
			throw ExceptionHandlerUtility.wrap(se);
		}
	}

}
