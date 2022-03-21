package com.manulife.pension.ps.web.onlineloans;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.LoanDocumentServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.XStreamCustomConverters;
import com.manulife.pension.ps.service.submission.valueobject.LoanLockable;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.onlineloans.displayrules.AbstractDisplayRules;
import com.manulife.pension.ps.web.onlineloans.displayrules.PrintFriendlyConfirmationDisplayRules;
import com.manulife.pension.ps.web.onlineloans.displayrules.PrintFriendlyDisplayRules;
import com.manulife.pension.ps.web.onlineloans.displayrules.PrintFriendlyForParticipantConfirmationDisplayRules;
import com.manulife.pension.ps.web.onlineloans.displayrules.PrintFriendlyForParticipantDisplayRules;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.util.LockManager;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.employee.valueobject.AddressVO;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanActivityRecord;
import com.manulife.pension.service.loan.valueobject.LoanAddress;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.WebLoanSupportDataRetriever;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.SSNRender;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * LoanInitiateAction is the action class for the initiate loan page
 */
public abstract class AbstractLoanController extends PsAutoController {

	private static final String DROPDOWN_LABEL_SELECT = "-select-";

	public static final String ACTION_FORWARD_ERROR = "error";

	public static final String ACTION_FORWARD_LIST = "loanList";

	public static final String ACTION_FORWARD_CONFIRMATION = "redirect:/do/onlineloans/confirmation/";

	public static final String ACTION_FORWARD_DEFAULT = "default";

	public static final String ACTION_FORWARD_INITIATE = "initiate";

	public static final String ACTION_FORWARD_DRAFT = "draft";

	public static final String ACTION_FORWARD_PENDING_REVIEW = "pendingReview";

	public static final String ACTION_FORWARD_PENDING_APPROVAL = "pendingApproval";

	public static final String ACTION_FORWARD_VIEW = "view";

	public static final String ACTION_FORWARD_VIEW_MANAGED_CONTENT = "viewManagedContent";

	public static final String ACTION_FORWARD_LOCK_ERROR = "lockError";

	public static final String ACTION_FORWARD_BACK_TO_PARTICIPANT_ACCOUNT = "toParticipantAccount";

	public static final String ACTION_FORWARD_BACK_TO_LOAN_AND_WITHDRAWAL = "toLoanAndWithdrawal";

	public static final String ACTION_FORWARD_BACK_TO_SEARCH_SUMMARY = "toSearchSummary";

	public static final String PARAM_SUBMISSION_ID = "submissionId";

	public static final String PARAM_CONTRACT_ID = "contractId";

	public static final String PARAM_PARTICIPANT_PROFILE_ID = "participantProfileId";

	protected static final String REQ_USER_ROLE_WITH_PERMISSIONS = "userRoleWithPermissions";

	protected static final String REQ_LOAN_SETTINGS = "loanSettings";

	protected static final String REQ_LOAN_PARTICIPANT_DATA = "loanParticipantData";

	protected static final String REQ_LOAN_PLAN_DATA = "loanPlanData";

	protected static final String REQ_USER_NAMES = "userNames";

	protected static final String REQ_EMPLOYEE_STATUSES = "employeeStatuses";

	protected static final String REQ_LOAN_STATUS_CODES = "loanStatusCodes";

	protected static final String REQ_LOAN_TYPES = "loanTypes";

	protected static final String REQ_LOAN_PAYMENT_FREQUENCIES = "loanPaymentFrequencies";

	protected static final String REQ_PAYMENT_METHODS = "paymentMethods";

	protected static final String REQ_STATES = "states";

	protected static final String REQ_COUNTRIES = "countries";

	protected static final String REQ_BANK_ACCOUNT_TYPES = "bankAccountTypes";

	protected static final String REQ_LOAN_DATA = "loan";

	protected static final String REQ_POST_PATH = "postPath";

	protected static final String REQ_DISPLAY_RULES = "displayRules";

	protected static final String REQ_LOAN_ACTIVITIES = "loanActivities";

	protected static final String REQ_SHOW_APPROVAL_DIALOG = "showApprovalDialog";

	protected static final String REQ_SHOW_LOAN_DOCUMENTS = "showLoanDocuments";

	private static final String XSLT_FILE_KEY_NAME = "OnlineLoans.XSLFile";
	private static final String INCLUDED_XSL_PATH_KEY_NAME = "OnlineLoans.IncludedXSLPath";
	
	/**
	 * This is a static reference to the logger.
	 */
	private static final Logger logger = Logger
			.getLogger(AbstractLoanController.class);

	protected abstract String getCurrentForward();

	protected abstract AbstractDisplayRules getDisplayRules(
			UserProfile userProfile, UserRole userRoleWithPermissions,
			Loan loan, LoanPlanData loanPlanData,
			LoanParticipantData loanParticipantData, LoanSettings loanSettings,
			LoanActivities loanActivities, Map<Integer, UserName> userNames,
			Map<String, String> stateMap, Map<String, String> countryMap);

	protected Integer getContractId(HttpServletRequest request) {
		Integer contractId = null;
		String contractIdStr = StringUtils.trimToNull(request.getParameter(PARAM_CONTRACT_ID));

		if (StringUtils.isBlank(contractIdStr)) {
			UserProfile userProfile = getUserProfile(request);
			Contract contract = userProfile.getCurrentContract();
			contractId = contract.getContractNumber();
		} else {
			try {
				contractIdStr = contractIdStr.replace("#", "");
				contractId = org.apache.commons.lang.math.NumberUtils
						.createInteger(contractIdStr);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return contractId;
	}
	
	protected Loan getLoan(final AutoForm actionForm,
			final HttpServletRequest request) throws SystemException {

		/*
		 * First, check  if there is already an loan object in the request. It's
		 * safe to use the request's loan because it's gone after each request.
		 */
		Loan loan = (Loan) request.getAttribute(REQ_LOAN_DATA);
		if (loan != null) {
			return loan;
		}

		UserProfile userProfile = getUserProfile(request);
		LoanForm form = (LoanForm) actionForm;
		/*
		 * Get submission ID from the request parameter. This is the safest
		 * place to get the submission ID. If it's not found in the request, get
		 * it from the Action Form. In the initiate action form, the submission
		 * ID is always cleared to avoid loading the form from the previous
		 * edit.
		 */
		String submissionId = StringUtils.trimToNull(request.getParameter(PARAM_SUBMISSION_ID));
		if (!StringUtils.isBlank(submissionId)) {
			submissionId = submissionId.replace("#", "");
		}
		String formSubmissionId = String.valueOf(form.getSubmissionId());

		Integer contractId = getContractId(request);

		if (!ObjectUtils.equals(submissionId, formSubmissionId)) {
			form.clear();
		}

		if (submissionId == null || contractId == null) {
			return null;
		} else {
			form.setSubmissionId(new Integer(submissionId));
			form.setContractId(contractId);
			loan = LoanServiceDelegate.getInstance().read(
					(int) userProfile.getPrincipal().getProfileId(),
					contractId, new Integer(submissionId));
			loan.setDataRetriever(new WebLoanSupportDataRetriever());
		}

		if (loan != null) {
			
			// Need to set the BGA indicator as well as the Contract TPA permission to validate them in back-end
			populateAdditionalLoanData(contractId, request, loan);
			request.setAttribute(REQ_LOAN_DATA, loan);
		}

		return loan;
	}

	@Override
	protected String preExecute(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SystemException {

		//ActionForward forward = super.preExecute( actionForm, request,response);
		String forward = super.preExecute( actionForm, request,response);
		if (forward != null) {
			return forward;
		}

		LoanForm form = (LoanForm) actionForm;
		UserProfile userProfile = getUserProfile(request);
		UserRole userRoleWithPermissions = LoanActionForwardHelper
				.getUserRoleWithPermissions(userProfile, getContractId(request));
		/*
		 * Save user role permission object so that we don't have to retrieve
		 * it again later in the request.
		 */
		request.setAttribute(REQ_USER_ROLE_WITH_PERMISSIONS,
				userRoleWithPermissions);
		Loan loan = getLoan(form, request);

		if (loan == null) {
			/*
			 * If the loan cannot be read, probably due to an invalid submission
			 * ID/contract ID combination, return to the list page.
			 */
			//return mapping.findForward(ACTION_FORWARD_LIST);
			return ACTION_FORWARD_LIST;
		}

		/*
		 * Check other permissions and accessible contracts to determine if we
		 * are okay to stay in this action.
		 */
		LoanSettings loanSettings = LoanServiceDelegate.getInstance()
				.getLoanSettings(loan.getContractId());

		//forward = LoanActionForwardHelper.getActionForwardIfLoanNotAccessible(getCurrentForward(), mapping, form, request, userProfile,userRoleWithPermissions, loanSettings, loan);
		forward = LoanActionForwardHelper.getActionForwardIfLoanNotAccessible(getCurrentForward(),  form, request, userProfile,userRoleWithPermissions, loanSettings, loan);

		/*
		 * Save loan setting object so that we don't have to retrieve it again
		 * later in the request.
		 */
		request.setAttribute(REQ_LOAN_SETTINGS, loanSettings);
		
		if (forward != null) {
			return forward;
		}

		return null;
	}

	@Override
	protected void postExecute(ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		super.postExecute( actionForm, request, response);

		/*
		 * Construct a user-friendly URL post path. This will make all URL
		 * bookmarkable.
		 */
		//TODO
		//String postPath = mapping.getPath();
		String postPath=new UrlPathHelper().getPathWithinApplication(request);
		LoanForm form = (LoanForm) actionForm;
		List<String> params = new ArrayList<String>();
		if (form.getSubmissionId() != null) {
			params.add(PARAM_SUBMISSION_ID + "=" + form.getSubmissionId());
		}
		if (form.getContractId() != null) {
			params.add(PARAM_CONTRACT_ID + "=" + form.getContractId());
		}
		if (ACTION_FORWARD_INITIATE.equals(getCurrentForward())) {
			if (form.getParticipantProfileId() != null) {
				params.add(PARAM_PARTICIPANT_PROFILE_ID + "="
						+ form.getParticipantProfileId());
			}
		}
		if (params.size() > 0) {
			postPath += "?";
			for (int i = 0; i < params.size(); i++) {
				postPath += params.get(i);
				if (i != params.size() - 1) {
					postPath += "&";
				}
			}
		}

		request.setAttribute(REQ_POST_PATH, postPath);
	}

	public  String doDefault(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		return null;
	}

	public String doPrint(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		Loan loan = getLoan(actionForm, request);
		updateRequest(actionForm, request, false, loan, false, false);
		//ActionForward forward = mapping.findForward(ACTION_FORWARD_DEFAULT);
		return ACTION_FORWARD_DEFAULT;
	}
	
	public String doPrintPDF(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		Loan loan = getLoan(actionForm, request);
		updateRequest(actionForm, request, false, loan, false, false);
		return super.doPrintPDF( actionForm, request, response);
	}
	
	public String doPrintForParticipant(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		Loan loan = getLoan(actionForm, request);
		updateRequest(actionForm, request, false, loan, false, false);
		//ActionForward forward = mapping.findForward(ACTION_FORWARD_DEFAULT);
		return ACTION_FORWARD_DEFAULT;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BasePdfAutoAction#getXSLTFileName()
	 */
	protected String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BasePdfAutoAction#getIncludedXSLPath()
	 */
	public String getIncludedXSLPath() {
	       return INCLUDED_XSL_PATH_KEY_NAME;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BasePdfAutoAction#getFilename(com.manulife.pension.platform.web.controller.AutoForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getFilename(AutoForm actionForm, HttpServletRequest request) {
		/**
		 * Print for Participant 	-> LI_PPT_<CN#>_<LASTNAME>_YYYYMMDD.pdf
		 * Print Report				-> LI_RPT_<CN#>_<LASTNAME>_YYYYMMDD.pdf 
		 */
		StringBuffer filename = new StringBuffer();
		LoanForm form = (LoanForm)actionForm;
		
		filename.append("LI").append(Constants.UNDERSCORE);
		if (getPrintParticipant(request)) {
			filename.append(Constants.PRINT_FOR_PARTICIPANT_ID).append(Constants.UNDERSCORE);
		} else {
			filename.append(Constants.PRINT_REPORT_ID).append(Constants.UNDERSCORE);
		}
		filename.append(form.getContractId()).append(Constants.UNDERSCORE);
		filename.append( form.getLastName()).append(Constants.UNDERSCORE);
		filename.append(DateFormatUtils.format(new Date(), "yyyyMMdd"));
		filename.append(Constants.PDF_FILE_NAME_EXTENSION);
		
		return filename.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BasePdfAutoAction#prepareXMLFromReport(com.manulife.pension.platform.web.controller.AutoForm, com.manulife.pension.service.report.valueobject.ReportData, javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	public Object prepareXMLFromReport(AutoForm actionForm, ReportData report, 
			HttpServletRequest request) throws ParserConfigurationException, SystemException, ContentException {
		LoanForm form = (LoanForm) actionForm;
		LoanData loanData = new LoanData();
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		LoanPlanData loanPlanData = (LoanPlanData) request.getAttribute(REQ_LOAN_PLAN_DATA);
		LoanMoneyTypeCalculation loanMoneyTypeCalculation = new LoanMoneyTypeCalculation();
		
		AbstractDisplayRules displayRules = (AbstractDisplayRules) request.getAttribute(REQ_DISPLAY_RULES);
		Map<String, String> loanStatusCodes = (HashMap<String, String>) request.getAttribute(REQ_LOAN_STATUS_CODES);
		Map<String, String> loanTypes = (HashMap<String, String>) request.getAttribute(REQ_LOAN_TYPES);
		Map<String, String> employeeStatuses = (HashMap<String, String>)request.getAttribute(REQ_EMPLOYEE_STATUSES);		
		
		loanData.setLoanForm((LoanForm)actionForm);
		loanData.setUserNames((HashMap<Integer, UserName>) request.getAttribute(REQ_USER_NAMES));
		loanData.setLoanParticipantData((LoanParticipantData)request.getAttribute(REQ_LOAN_PARTICIPANT_DATA));
		loanData.setMoneyTypesWithAccountBalance(loan.getMoneyTypesWithAccountBalance());
		loanData.setLoan(loan);
		loanData.setLoanPlanData(loanPlanData);
		loanData.setDisplayRules(setDisplayRulesForPrintPDF(displayRules));
		loanData.setLoanActivities((LoanActivities)request.getAttribute(REQ_LOAN_ACTIVITIES));
		loanData.setCurrentLoanParameter(loan.getCurrentLoanParameter());
		loanData.setLoanStatus(loanStatusCodes.get(loan.getStatus()));
		loanData.setLoanType(loanTypes.get(loan.getLoanType()));
		loanData.setLoanPaymentFrequencies((HashMap<String, String>)request.getAttribute(REQ_LOAN_PAYMENT_FREQUENCIES));
		loanData.setPaymentMethods((HashMap<String, String>)request.getAttribute(REQ_PAYMENT_METHODS));
		loanData.setStates((HashMap<String, String>)request.getAttribute(REQ_STATES));
		loanData.setCountries((HashMap<String, String>)request.getAttribute(REQ_COUNTRIES));
		loanData.setBankAccountTypes((HashMap<String, String>)request.getAttribute(REQ_BANK_ACCOUNT_TYPES));
		loanData.setEmployeeStatus(employeeStatuses.get(form.getEmploymentStatusCode()));
		loanData.setFormattedSSN(SSNRender.format(form.getSsn(), StringUtils.EMPTY, false));
		loanData.setFormattedMaskedSSN(SSNRender.format(form.getSsn(), StringUtils.EMPTY, true));
		loanMoneyTypeCalculation.recalculateBalances(false, loan, loanPlanData);
		loanData.setLoanMoneyTypeCalculation(loanMoneyTypeCalculation);
		loanData.setPrintParticipant(getPrintParticipant(request));
		loanData.setMaskedAccountNumber(Constants.MASK_ACCOUNT_NUMBER);
		
		loanData.getCmaContent().setSpousalConsentMayBeRequiredText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.SPOUSAL_CONSENT_MAY_BE_REQUIRED)));
		loanData.getCmaContent().setSpousalConsentMayBeRequiredIsMarriedText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.SPOUSAL_CONSENT_MAY_BE_REQUIRED_IF_MARRIED)));
		loanData.getCmaContent().setSpousalConsentMustBeObtainedText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.SPOUSAL_CONSENT_MUST_BE_OBTAINED)));
		loanData.getCmaContent().setSpousalConsentIsNotRequiredText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.SPOUSAL_CONSENT_IS_NOT_REQUIRED))); 
		loanData.getCmaContent().setParticipantInfoTitleText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.PARTICIPANT_INFORMATION_SECTION_TITLE))); 
		loanData.getCmaContent().setParticipantInfoFooterText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.PARTICIPANT_INFORMATION_SECTION_FOOTER)));
		loanData.getCmaContent().setPageContentNotFinalDisclaimer(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.PAGE_CONTENT_NOT_FINAL_DISCLAIMER))); 
		loanData.getCmaContent().setGiflMsgExternalUserInitiated(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.GIFL_MSG_EXTERNAL_USER_INITIATED))); 
		loanData.getCmaContent().setGiflMsgParticipantInitiated(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.GIFL_MSG_PARTICIPANT_INITIATED))); 
		loanData.getCmaContent().setGlobalDisclosure(getCMAContentAttributeText(
				String.valueOf(ContentConstants.GLOBAL_DISCLOSURE))); 
		loanData.getCmaContent().setCalcualteMaxAvailableForLoanSectionTitleText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.CACULATE_MAX_AVAILABLE_FOR_LOAN_SECTION_TITLE))); 
		loanData.getCmaContent().setVestingExplanationLinkText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.VESTING_EXPLANATION_LINK)));
		loanData.getCmaContent().setCalculateMaxLoanAvailableFooterText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.CACULATE_MAX_LOAN_AVAILABLE_SECTION_FOOTER))); 
		loanData.getCmaContent().setDeclarationsSectionTitleText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.DECLARATIONS_SECTION_TITLE))); 
		loanData.getCmaContent().setDeclarationsSectionFooterText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.DECLARATIONS_SECTION_FOOTER)));
		loanData.getCmaContent().setLoanCalcualtionsTitleText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.LOAN_CACULATIONS_TITLE))); 
		loanData.getCmaContent().setLoanDetalsTitleText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.LOAN_DETAILS_SECTION_TITLE))); 
		loanData.getCmaContent().setDefaultProvisionExplanationText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.DEFAULT_PROVISION_EXPLANATION))); 
		loanData.getCmaContent().setNoteToparticipantPrintContentText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.NOTE_TO_PARTICIPANT_PRINT_CONTENT))); 
		loanData.getCmaContent().setViewNotesSectionFooterText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.VIEW_NOTES_SECTION_FOOTER))); 
		loanData.getCmaContent().setPaymentInstructionsSectionTitleText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.PAYMENT_INSTRUCTIONS_SECTION_TITLE))); 
		loanData.getCmaContent().setWireChargesContentText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.WIRE_CHARGES_CONTENT))); 
		loanData.getCmaContent().setPaymentInstructionsFooterText(getCMAContentAttributeText(
				String.valueOf(LoanContentConstants.PAYMENT_INSTRUCTIONS_FOOTER))); 
		
		String accountBalanceFootnoteCmaKey = displayRules.getAccountBalanceFootnoteCmaKey();
		if (StringUtils.isNotBlank(accountBalanceFootnoteCmaKey)){
			loanData.getCmaContent().setAccountBalanceFootnoteText(getCMAContentAttributeText(
					displayRules.getAccountBalanceFootnoteCmaKey()));
		}
		
		XStream stream = new XStream(new DomDriver());
		stream.setMode(XStream.NO_REFERENCES);
		stream.setMarshallingStrategy(new CustomTreeMarshallingStrategy());
		
    	XStreamCustomConverters customConverters = XStreamCustomConverters.getInstance();
		stream.registerConverter(customConverters.new MapConverter());
		stream.registerConverter(customConverters.new DateConverterX());
		stream.registerConverter(customConverters.new TimestampConverterX());
		stream.registerConverter(customConverters.new BigDecimalConverterX());
		stream.alias("loanData", com.manulife.pension.ps.web.onlineloans.LoanData.class);
		stream.alias("loanNote", com.manulife.pension.service.loan.valueobject.LoanNote.class);
		stream.alias("loanPayee", com.manulife.pension.service.loan.valueobject.LoanPayee.class);
		stream.alias("userName", com.manulife.pension.service.withdrawal.valueobject.UserName.class);
		stream.alias("loanActivityRecord", com.manulife.pension.service.loan.valueobject.LoanActivityRecord.class);
		stream.alias("loanActivitySummary", com.manulife.pension.service.loan.valueobject.LoanActivitySummary.class);
		stream.alias("loanMoneyType", com.manulife.pension.service.loan.valueobject.LoanMoneyType.class);
		
		String xml = stream.toXML(loanData);
		return xml;
  	}
	  
	/**
	 * Creates the loanDisplayRules object
	 * 
	 * @param displayRules
	 * @return loanDisplayRules
	 * @throws SystemException
	 */
	private LoanDisplayRules setDisplayRulesForPrintPDF(AbstractDisplayRules displayRules) throws SystemException {
		
		LoanDisplayRules loanDisplayRules = new LoanDisplayRules();
		
		loanDisplayRules.setDisplayPageContentNotFinalDisclaimer(displayRules.isDisplayPageContentNotFinalDisclaimer());
		loanDisplayRules.setDisplayGiflMsgExternalUserInitiated(displayRules.isDisplayGiflMsgExternalUserInitiated());
		loanDisplayRules.setDisplayGiflMsgParticipantInitiated(displayRules.isDisplayGiflMsgParticipantInitiated());
		loanDisplayRules.setDisplaySubmissionNumber(displayRules.isDisplaySubmissionNumber());
		loanDisplayRules.setDisplaySubmissionStatus(displayRules.isDisplaySubmissionStatus());
		loanDisplayRules.setDisplaySubmissionProcessingDates(displayRules.isDisplaySubmissionProcessingDates());
		loanDisplayRules.setDisplayNotesViewSection(displayRules.isDisplayNotesViewSection());
		loanDisplayRules.setDisplayPaymentInstructionSection(displayRules.isDisplayPaymentInstructionSection());
		loanDisplayRules.setShowDeclarationsSection(displayRules.isShowDeclarationsSection());
		loanDisplayRules.setDisplayNotesEditSection(displayRules.isDisplayNotesEditSection());
		loanDisplayRules.setDisplayParticipantDeclarationCheckbox(displayRules.isDisplayParticipantDeclarationCheckbox());
		loanDisplayRules.setDisplayApproverAgreedToLabel(displayRules.isDisplayApproverAgreedToLabel());
		loanDisplayRules.setDisplayAtRiskTransactionCheckbox(displayRules.isDisplayAtRiskTransactionCheckbox());
		loanDisplayRules.setDisplayLoanCalculationBlankColumn(displayRules.isDisplayLoanCalculationBlankColumn());
		loanDisplayRules.setDisplayLoanCalculationEditable(displayRules.isDisplayLoanCalculationEditable());
		loanDisplayRules.setDisplayLoanCalculationAcceptedColumn(displayRules.isDisplayLoanCalculationAcceptedColumn());
		loanDisplayRules.setDisplayLoanCalculationReviewedColumn(displayRules.isDisplayLoanCalculationReviewedColumn());
		loanDisplayRules.setDisplayLoanCalculationOriginalColumn(displayRules.isDisplayLoanCalculationOriginalColumn());
		loanDisplayRules.setLoanAmountDisplayOnlyRecalculated(displayRules.isLoanAmountDisplayOnlyRecalculated());
		loanDisplayRules.setLoanAmountDisplayOnly(displayRules.isLoanAmountDisplayOnly());
		loanDisplayRules.setDisplayTpaLoanIssueFee(displayRules.isDisplayTpaLoanIssueFee());
		loanDisplayRules.setDisplayDefaultProvisionExplanation(displayRules.isDisplayDefaultProvisionExplanation());
		loanDisplayRules.setDisplayNotesToAdministrators(displayRules.isDisplayNotesToAdministrators());
		loanDisplayRules.setDisplayNoteToParticipantPrintContentText(displayRules.isDisplayNoteToParticipantPrintContentText());
		loanDisplayRules.setDisplayViewNotesSectionFooter(displayRules.isDisplayViewNotesSectionFooter());
		loanDisplayRules.setDisplayMiddleInitial(displayRules.isDisplayMiddleInitial());
		loanDisplayRules.setMaskSsn(displayRules.isMaskSsn());
		loanDisplayRules.setDisplayLegallyMarried(displayRules.isDisplayLegallyMarried());
		loanDisplayRules.setDisplaySpousalConsentText(displayRules.isDisplaySpousalConsentText());
		loanDisplayRules.setCountryUSA(displayRules.isCountryUSA());
		loanDisplayRules.setShowMaskedAccountNumber(displayRules.isShowMaskedAccountNumber());
		loanDisplayRules.setShowBankInformationAsEditable(displayRules.isShowBankInformationAsEditable());
		loanDisplayRules.setLoanCalculationEditable(displayRules.isLoanCalculationEditable());
		loanDisplayRules.setDisplayApproverAgreedToText(displayRules.isDisplayApproverAgreedToText());
		loanDisplayRules.setLoanAmountEditable(displayRules.isLoanAmountEditable());
		
		loanDisplayRules.setAccountBalanceLabel(displayRules.getAccountBalanceLabel());
		loanDisplayRules.setLoanApprovalPlanSpousalConsentContent(displayRules.getLoanApprovalPlanSpousalConsentContent());
		loanDisplayRules.setLoanApprovalGenericContent(displayRules.getLoanApprovalGenericContent());
		loanDisplayRules.setLoanCalculationOriginalColumnHeader(displayRules.getLoanCalculationOriginalColumnHeader());	
		loanDisplayRules.setLoanCalculationEditableColumnHeader(displayRules.getLoanCalculationEditableColumnHeader());
		loanDisplayRules.setLoanCalculationAcceptedColumnHeader(displayRules.getLoanCalculationAcceptedColumnHeader());
		loanDisplayRules.setParticipantLabelText(displayRules.getParticipantLabelText());
		loanDisplayRules.setLoanCalculationReviewedColumnHeader(displayRules.getLoanCalculationReviewedColumnHeader());
		loanDisplayRules.setStateName(displayRules.getStateName());
		loanDisplayRules.setCountryName(displayRules.getCountryName());
		loanDisplayRules.setAbaRountingNumber(displayRules.getAbaRountingNumber());
		loanDisplayRules.setVestingPercentageActivityHistoryDisplayMap(displayRules.getVestingPercentageActivityHistoryDisplayMap());
		
		return loanDisplayRules;
	}

	/**
	 * This method provides access to the {@link WithdrawalServiceDelegate}.
	 * 
	 * @return WithdrawalServiceDelegate The withdrawal service delegate.
	 */
	protected WithdrawalServiceDelegate getWithdrawalServiceDelegate() {
		return WithdrawalServiceDelegate.getInstance();
	}

	protected void updateRequest(final AutoForm actionForm,
			final HttpServletRequest request, boolean refreshForm,
			final Loan loan, boolean showInitializationMessage,
			boolean showInitialMessage) throws SystemException {

		UserProfile userProfile = getUserProfile(request);
		LoanForm form = (LoanForm) actionForm;
		Integer contractId = loan.getContractId();
		Integer submissionId = loan.getSubmissionId();
		
		ContractPermission contractPermission = SecurityServiceDelegate.getInstance().getTpaFirmContractPermission(contractId);
		UserRole userRoleWithPermissions = getUserRoleWithPermissions(request);
		
    if (userProfile != null && userProfile.getContractProfile() != null && userProfile.getContractProfile().getContract() != null) {
      loan.setBundledContract(userProfile.getContractProfile().getContract().isBundledGaIndicator());
    } else {
      loan.setBundledContract(ContractServiceDelegate.getInstance().isBundledGaContract(contractId));
    }
		loan.setSigningAuthorityForContractTpaFirm(contractPermission.isSigningAuthority());
		loan.setDeclartionSectionDisplayed(LoanDisplayHelper.isShowDeclarationsSection(loan, userRoleWithPermissions));
		form.setDisplayIRSlabel(loan.isBundledContract() && loan.isSigningAuthorityForContractTpaFirm());

		/*
		 * Get submission ID from the request parameter. This is the safest
		 * place to get the submission ID. If it's not found in the request, get
		 * it from the Action Form. In the initiate action form, the submission
		 * ID is always cleared to avoid loading the form from the previous
		 * edit.
		 */

		Integer participantProfileId = loan.getParticipantProfileId();

		LoanParticipantData loanParticipantData = loan.getLoanParticipantData();
		LoanPlanData loanPlanData = loan.getLoanPlanData();
		LoanActivities loanActivities = null;

		if (submissionId != null) {
			Integer participantUserProfileId = null;
			if (loan.getCreatedByRoleCode().equals(
					LoanConstants.USER_ROLE_PARTICIPANT_CODE)) {
				participantUserProfileId = loan.getCreatedId();
			}
			loanActivities = LoanServiceDelegate.getInstance().readActivities(
					Integer.valueOf(Long.toString(userProfile.getPrincipal()
							.getProfileId())), contractId,
					String.valueOf(submissionId), participantUserProfileId,
					loanParticipantData.getFirstName(),
					loanParticipantData.getLastName());
			doLoanActivityOverrides(loanActivities, loan, loanPlanData,
					loanParticipantData);
		} else {
			loanActivities = new LoanActivities(null, contractId, loan
					.getParticipantProfileId(), loanParticipantData
					.getFirstName(), loanParticipantData.getLastName());
		}

		LoanSettings loanSettings = loan.getLoanSettings();

		/*
		 * Build the list of user profile IDs and get their names.
		 */
		Set<Integer> userProfileIds = new HashSet<Integer>();

		/*
		 * Obtain user profile IDs from the notes.
		 */
		if (loan.getPreviousAdministratorNotes() != null) {
			for (Note note : loan.getPreviousAdministratorNotes()) {
				userProfileIds.add(note.getCreatedById());
			}
		}
		if (loan.getPreviousParticipantNotes() != null) {
			for (Note note : loan.getPreviousParticipantNotes()) {
				userProfileIds.add(note.getCreatedById());
			}
		}

		/*
		 * Reviewed parameters may be entered by participant if it was just sent
		 * for review. So we need to make sure we are not looking for
		 * participant info.
		 */
		if (loan.getReviewedParameter() != null
				&& loan.getReviewedParameter().getLastUpdatedById() != null) {
			boolean readProfileInfo = true;
			if (LoanConstants.USER_ROLE_PARTICIPANT_CODE.equals(loan
					.getCreatedByRoleCode())) {
				if (loan.getCreatedId().equals(
						loan.getReviewedParameter().getLastUpdatedById())) {
					readProfileInfo = false;
				}
			}
			if (readProfileInfo) {
				userProfileIds.add(loan.getReviewedParameter()
						.getLastUpdatedById());
			}
		}

		/*
		 * If the original parameter is created by a participant, we cannot use
		 * it because we will not be able to find his/her entry in the
		 * USER_PROFILE table.
		 */
		if (!LoanConstants.USER_ROLE_PARTICIPANT_CODE.equals(loan
				.getCreatedByRoleCode())) {
			if (loan.getOriginalParameter() != null
					&& loan.getOriginalParameter().getLastUpdatedById() != null) {
				userProfileIds.add(loan.getOriginalParameter()
						.getLastUpdatedById());
			}
		}

		Map<Integer, UserName> userNames = getUserNamesForIds(userProfileIds);

		AbstractDisplayRules displayRules = null;
		
		boolean printFriendly = Boolean.valueOf(
				request.getParameter("printFriendly")!= null ? 
						request.getParameter("printFriendly") : "false");
		boolean printParticipant = Boolean.valueOf(
				request.getParameter("printParticipant")!= null ? 
						request.getParameter("printParticipant") : "false");
		
		if ("print".equalsIgnoreCase(form.getActionLabel()) ||
				("printPDF".equalsIgnoreCase(form.getActionLabel()) && printFriendly && !printParticipant)) {
		    if (form.isShowConfirmation()) {
	            displayRules = new PrintFriendlyConfirmationDisplayRules(
	                    userProfile, userRoleWithPermissions, loan, 
	                    loanPlanData, loanParticipantData, 
	                    loanSettings, loanActivities, userNames, LoanLookups
	                            .getInstance().getStateList(), LoanLookups.getInstance()
	                            .getCountryList());
		    } else {
	            displayRules = new PrintFriendlyDisplayRules(
	                    userProfile, userRoleWithPermissions, loan, 
	                    loanPlanData, loanParticipantData, 
	                    loanSettings, loanActivities, userNames, LoanLookups
	                            .getInstance().getStateList(), LoanLookups.getInstance()
	                            .getCountryList());
		    }
        } else if ("printForParticipant".equalsIgnoreCase(form.getActionLabel()) ||
        		("printPDF".equalsIgnoreCase(form.getActionLabel()) && printParticipant)) {
            if (form.isShowConfirmation()) {
                displayRules = new PrintFriendlyForParticipantConfirmationDisplayRules(
                        userProfile, userRoleWithPermissions, loan, 
                        loanPlanData, loanParticipantData,
                        loanSettings, loanActivities, userNames, LoanLookups
                                .getInstance().getStateList(), LoanLookups.getInstance()
                                .getCountryList());
            } else {
                displayRules = new PrintFriendlyForParticipantDisplayRules(
                        userProfile, userRoleWithPermissions, loan, 
                        loanPlanData, loanParticipantData,
                        loanSettings, loanActivities, userNames, LoanLookups
                                .getInstance().getStateList(), LoanLookups.getInstance()
                                .getCountryList());
            }
        } else {
            displayRules = getDisplayRules(userProfile, userRoleWithPermissions, 
                    loan, loanPlanData, loanParticipantData,
                    loanSettings, loanActivities, userNames, LoanLookups.getInstance()
                            .getStateList(), LoanLookups.getInstance().getCountryList());
        }

		if (!displayRules.isEditMode()) {
		    // If View mode, set the effectiveDate and maturityDate back 
		    // to their SDB value as we don't want to show the recalculated 
		    // values.
		    loan.setEffectiveDate(loan.getEffectiveDateOriginalDBValue());
		    loan.setMaturityDate(loan.getMaturityDateOriginalDBValue());
		}

		request.setAttribute(REQ_USER_NAMES, userNames);
		request.setAttribute(REQ_LOAN_PARTICIPANT_DATA, loanParticipantData);
		request.setAttribute(REQ_LOAN_DATA, loan);
		request.setAttribute(REQ_LOAN_PLAN_DATA, loanPlanData);
		request.setAttribute(REQ_DISPLAY_RULES, displayRules);
		request.setAttribute(REQ_LOAN_ACTIVITIES, loanActivities);

		request.setAttribute(REQ_LOAN_STATUS_CODES, LoanLookups.getInstance()
				.getOnlineLoansStatusList());
		request.setAttribute(REQ_LOAN_TYPES, LoanLookups.getInstance()
				.getLoanTypesList());
		request.setAttribute(REQ_LOAN_PAYMENT_FREQUENCIES, LoanLookups
				.getInstance().getLoanPaymentFrequencyList());
		request.setAttribute(REQ_PAYMENT_METHODS, LoanLookups.getInstance()
				.getPaymentMethodList());
		request.setAttribute(REQ_STATES, LoanLookups.getInstance()
				.getStateList());
        Map<String, String> countryList = LoanLookups.getInstance().getCountryList();
		request.setAttribute(REQ_COUNTRIES, countryList);
		request.setAttribute(REQ_BANK_ACCOUNT_TYPES, LoanLookups.getInstance()
				.getBankAccountTypeList());

		Map<String, String> employeeStatuses = EmployeeServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID)
				.getEmployeeStatusList();
		request.setAttribute(REQ_EMPLOYEE_STATUSES, employeeStatuses);

		if (refreshForm) {
			/*
			 * If we need to refresh the form, that means we also need to save
			 * the token so that we can compare later.
			 */
			////TODO saveToken(request);
			form.clear();
			form.parseLoan(loan, loanPlanData);
			form.parseParticipantData(loanParticipantData);
			form.parseContract(loanPlanData);
			saveOriginalCheckBoxValues(form);
            
			if (displayRules.isDisplayPaymentInstructionSection()) {
				/*
				 * If we are display payment instruction section, we need to
				 * initialize the form.
				 */
				LoanPayee requestPayee = null;
				LoanAddress requestAddress = null;
				if (loan.getRecipient() != null) {
					if (loan.getRecipient().getPayees() != null) {
						requestPayee = (LoanPayee) loan.getRecipient()
								.getPayees().iterator().next();
					}
					if (loan.getRecipient().getAddress() != null) {
						requestAddress = (LoanAddress) loan.getRecipient()
								.getAddress();
					}
				}

				if (requestAddress != null) {
					form.setAddressLine1(requestAddress.getAddressLine1());
					form.setAddressLine2(requestAddress.getAddressLine2());
					form.setCity(requestAddress.getCity());
					form.setCountry(requestAddress.getCountryCode());
					form.getStateCode().setValue(requestAddress.getStateCode());
					form.getZipCode().setValue(requestAddress.getZipCode());

				} else {
					EmployeeServiceDelegate employeeServiceDelegate = EmployeeServiceDelegate
							.getInstance(Constants.PS_APPLICATION_ID);
					AddressVO participantAddress = employeeServiceDelegate
							.getAddressByProfileId(Long
									.valueOf(participantProfileId), contractId);
					if (participantAddress != null) {
						form.setAddressLine1(participantAddress
								.getAddressLine1());
						form.setAddressLine2(participantAddress
								.getAddressLine2());
						form.setCity(participantAddress.getCity());
	                    // Convert the country name to its country code.
	                    form.setCountry(GlobalConstants.COUNTRY_CODE_USA);
	                    if (!StringUtils.isBlank(participantAddress.getCountry())) {
	                        for (Map.Entry<String, String> countryEntry : countryList.entrySet()) {
	                            if (countryEntry.getValue().equalsIgnoreCase(participantAddress.getCountry())) {
	                                form.setCountry(countryEntry.getKey());
	                                break;
	                            }
	                        }
	                    }
						form.getStateCode().setValue(
								participantAddress.getStateCode());
						form.getZipCode().setValue(
								participantAddress.getZipCode());
					} else {
						// Employee address does not yet exist on CSDB, so
						// default
						// country to USA.
						form.setCountry(GlobalConstants.COUNTRY_CODE_USA);
					}
				}

				if (requestPayee != null) {
					form
							.setPaymentMethod(requestPayee
									.getPaymentMethodCode() != null ? requestPayee
									.getPaymentMethodCode()
									: Payee.ACH_PAYMENT_METHOD_CODE);
				} else {
					form.setPaymentMethod(Payee.ACH_PAYMENT_METHOD_CODE);
				}

				if (displayRules.isShowAddressDataAsEditable()) {
					Map<String, String> states = displayRules.getStatesMap();
					List<LabelValueBean> statesLabelValueBeans = new ArrayList<LabelValueBean>();
					statesLabelValueBeans.add(new LabelValueBean(
							DROPDOWN_LABEL_SELECT, ""));
					for (Map.Entry<String, String> entry : states.entrySet()) {
						statesLabelValueBeans.add(new LabelValueBean(entry
								.getValue(), entry.getKey()));
					}
					form.getLookupData().put("states", statesLabelValueBeans);

					EnvironmentServiceDelegate environmentServiceDelegate = EnvironmentServiceDelegate
							.getInstance(Constants.PS_APPLICATION_ID);
					List<com.manulife.pension.service.environment.valueobject.LabelValueBean> countries = new ArrayList<com.manulife.pension.service.environment.valueobject.LabelValueBean>(
							environmentServiceDelegate.getCountriesForLookup());
					countries
							.add(
									0,
									new com.manulife.pension.service.environment.valueobject.LabelValueBean(
											DROPDOWN_LABEL_SELECT, ""));
					form.getLookupData().put("countries", countries);
				}
			}
			form.storeClonedForm();
		} else if ("print".equalsIgnoreCase(form.getActionLabel())
		        || "printForParticipant".equalsIgnoreCase(form.getActionLabel())) { 
		    /* refreshForm == false, but still need to parse the VOs to
		     * populate the form with certain info that is needed for displaying
		     * the print friendly versions of the page.
		     */ 
			form.parseLoan(loan, loanPlanData);
			form.parseParticipantData(loanParticipantData);
			form.parseContract(loanPlanData);
			saveOriginalCheckBoxValues(form);
		} else {
		    // Restore original check box values for display only fields.
		    if (displayRules.isShowTruthInLendingNoticeAsDisabled()) {
		        form.getTruthInLendingNotice().setAccepted(form.
		                getTruthInLendingNoticeAcceptedOriginalValue());
		    }
            if (displayRules.isShowPromissoryNoteAsDisabled()) {
                form.getPromissoryNote().setAccepted(form.
                        getPromissoryNoteAcceptedOriginalValue());
            }
            if (displayRules.isDisplayParticipantDeclarationCheckbox()) {
                form.getParticipantDeclaration().setAccepted(form.
                        getParticipantDeclarationAcceptedOriginalValue());
            }
            if (displayRules.isShowAtRiskTransactionCheckBoxAsDisabled()) {
                form.getAtRiskTransaction().setAccepted(form.
                        getAtRiskTransactionAcceptedOriginalValue());
            }
		}

		List<LoanMessage> errors = new ArrayList<LoanMessage>();
		errors.addAll(loan.getMessages());
		errors.addAll(loan.getErrors());

		Set<ValidationError> validationErrors = LoanMessageHelper
				.toValidationError(errors, showInitializationMessage,
						showInitialMessage);
		setErrorsInSession(request, validationErrors);
		/*
		 * Reset ignore warning so that we can redisplay any warning.
		 */
		form.setIgnoreWarning(false);
	}

	protected void saveOriginalCheckBoxValues(LoanForm form) {
	    form.setTruthInLendingNoticeAcceptedOriginalValue(
	            form.getTruthInLendingNotice().isAccepted());
	    form.setPromissoryNoteAcceptedOriginalValue(
	            form.getPromissoryNote().isAccepted());
	    form.setParticipantDeclarationAcceptedOriginalValue(
	            form.getParticipantDeclaration().isAccepted());
	    form.setAtRiskTransactionAcceptedOriginalValue(
	            form.getAtRiskTransaction().isAccepted());
	}

	/**
	 * Perform the appropriate overrides to the LoanActivities records.
	 * 
	 * @param loanActivities
	 * @param loan
	 * @param loanPlanData
	 * @param loanParticipantData
	 */
	protected void doLoanActivityOverrides(LoanActivities loanActivities,
			Loan loan, LoanPlanData loanPlanData,
			LoanParticipantData loanParticipantData) {
		/*
		 * If the loan status is pending review, pending acceptance or pending
		 * approval, override the system of record values in the LoanActiviites
		 * entries retrieved from the SDB Activity History tables, with the
		 * current system of record values from the CSDB.
		 */
		if (LoanStateEnum.PENDING_REVIEW.getStatusCode().equals(
				loan.getStatus())
				|| LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode().equals(
						loan.getStatus())
				|| LoanStateEnum.PENDING_APPROVAL.getStatusCode().equals(
						loan.getStatus())) {
			overrideLoanActivitySystemOfRecordValues(loanActivities, loan,
					loanPlanData, loanParticipantData);
		}
	}

	/**
	 * Override the system of record values in the LoanActiviites entries
	 * retrieved from the SDB Activity History tables, with the current system
	 * of record values from the CSDB.
	 * 
	 * @param loanActivities
	 * @param loan
	 * @param loanPlanData
	 * @param loanParticipantData
	 */
	protected void overrideLoanActivitySystemOfRecordValues(
			LoanActivities loanActivities, Loan loan,
			LoanPlanData loanPlanData, LoanParticipantData loanParticipantData) {
		BigDecimal vestingPercentage;

		for (LoanActivityRecord activityRecord : loanActivities
				.getActivityRecords()) {

			if (LoanField.TPA_LOAN_ISSUE_FEE.getFieldName().equals(
					activityRecord.getFieldName())) {
				BigDecimal fee = loanPlanData.getContractLoanSetupFeeAmount();
				if (fee != null) {
					activityRecord.setSystemOfRecordValue(fee.setScale(
							LoanConstants.AMOUNT_SCALE).toString());
				} else {
					activityRecord.setSystemOfRecordValue(null);
				}

			} else if (LoanField.MONEY_TYPE_VESTING_PERCENTAGE_PREFIX
					.getFieldName().equals(activityRecord.getFieldName())
					&& activityRecord.getSubItemName() != null) {
				EmployeeVestingInformation vestingInfo = loan
						.getEmployeeVestingInformation();
				MoneyTypeVestingPercentage moneyTypeVestingPercentage = (MoneyTypeVestingPercentage) vestingInfo
						.getMoneyTypeVestingPercentages().get(
								activityRecord.getSubItemName());
				if (moneyTypeVestingPercentage != null) {
	                vestingPercentage = moneyTypeVestingPercentage.getPercentage();
	                if (vestingPercentage != null) {
	                    activityRecord.setSystemOfRecordValue(vestingPercentage
	                            .setScale(LoanConstants.VESTING_PERCENTAGE_SCALE)
	                            .toString());
	                } else {
	                    activityRecord.setSystemOfRecordValue(null);
	                }
				} else {
                    activityRecord.setSystemOfRecordValue(null);
				}
				
			} else if (LoanField.ADDRESS_LINE1.getFieldName().equals(
					activityRecord.getFieldName())) {
				activityRecord.setSystemOfRecordValue(loanParticipantData
						.getAddressLine1());

			} else if (LoanField.ADDRESS_LINE2.getFieldName().equals(
					activityRecord.getFieldName())) {
				activityRecord.setSystemOfRecordValue(loanParticipantData
						.getAddressLine2());

			} else if (LoanField.CITY.getFieldName().equals(
					activityRecord.getFieldName())) {
				activityRecord.setSystemOfRecordValue(loanParticipantData
						.getCity());

			} else if (LoanField.STATE.getFieldName().equals(
					activityRecord.getFieldName())) {
				activityRecord.setSystemOfRecordValue(loanParticipantData
						.getStateCode());

			} else if (LoanField.COUNTRY.getFieldName().equals(
					activityRecord.getFieldName())) {
			    if (StringUtils.isNotEmpty(loanParticipantData.getCountry())) {
	                activityRecord.setSystemOfRecordValue(loanParticipantData
	                        .getCountry());
			    } else {
			        // If the country code is blank or null, it's considered
			        // to be 'USA'
			        activityRecord.setSystemOfRecordValue(
			                GlobalConstants.COUNTRY_CODE_USA);
			    }
			} else if (LoanField.ZIP_CODE.getFieldName().equals(
					activityRecord.getFieldName())) {
				activityRecord.setSystemOfRecordValue(loanParticipantData
						.getZipCode());
			}
		}
	}

	/**
	 * Save the messages in the session. This method is used to display initial
	 * messages.
	 * 
	 * @param request
	 * @param errors
	 */
	protected void setMessagesInSession(HttpServletRequest request,
			Collection<ValidationError> errors) {
		Collection<GenericException> sessionErrors = SessionHelper
				.getErrorsInSession(request);
		if (sessionErrors == null) {
			sessionErrors = new HashSet<GenericException>();
		}
		for (ValidationError error : errors) {
			boolean found = false;
			for (GenericException sessionError : sessionErrors) {
				if (sessionError.getErrorCode() == error.getErrorCode()) {
					found = true;
					break;
				}
			}
			if (!found) {
				sessionErrors.add((GenericException) error);
			}
		}
		SessionHelper.setErrorsInSession(request, sessionErrors);
	}

	/**
	 * doExit is called when the page 'exit' button is pressed.
	 * 
	 * @param mapping
	 *            The action mapping.
	 * @param actionForm
	 *            The action form.
	 * @param request
	 *            The HTTP request.
	 * @param response
	 *            The HTTP response.
	 * @return ActionForward The forward to process.
	 * @throws IOException
	 *             When an IO problem occurs.
	 * @throws ServletException
	 *             When an Servlet problem occurs.
	 * @throws SystemException
	 *             When an generic application problem occurs.
	 */
	public String doExit(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		////TODO resetToken(request);
		final LoanForm form = (LoanForm) actionForm;
		releaseLock(request, form);
		//return mapping.findForward(ACTION_FORWARD_LIST);
		return ACTION_FORWARD_LIST;
	}

	/**
	 * doSaveAndExit is called when the page 'save & exit' button is pressed.
	 * 
	 * @param mapping
	 *            The action mapping.
	 * @param actionForm
	 *            The action form.
	 * @param request
	 *            The HTTP request.
	 * @param response
	 *            The HTTP response.
	 * @return ActionForward The forward to process.
	 * @throws IOException
	 *             When an IO problem occurs.
	 * @throws ServletException
	 *             When an Servlet problem occurs.
	 * @throws SystemException
	 *             When an generic application problem occurs.
	 */
	public String doSaveAndExit(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		/*//TODO if (!isTokenValid(request)) {
			//return mapping.findForward(ACTION_FORWARD_LIST);
			return ACTION_FORWARD_LIST;
		}*/

		final LoanForm form = (LoanForm) actionForm;
		// Save the current work.
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				false, false);
		loan = LoanServiceDelegate.getInstance().save(loan);
		return getRequestListForward( actionForm, request, loan);
	}

	/**
	 * doDelete is called when the page 'Delete' button is pressed.
	 * 
	 * @param mapping
	 *            The action mapping.
	 * @param actionForm
	 *            The action form.
	 * @param request
	 *            The HTTP request.
	 * @param response
	 *            The HTTP response.
	 * @return ActionForward The forward to process.
	 * @throws IOException
	 *             When an IO problem occurs.
	 * @throws ServletException
	 *             When an Servlet problem occurs.
	 * @throws SystemException
	 *             When an generic application problem occurs.
	 */
	public String doDelete(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {

	/*//TODO	if (!isTokenValid(request)) {
			//return mapping.findForward(ACTION_FORWARD_LIST);
			return ACTION_FORWARD_LIST;
		}
*/
		final LoanForm form = (LoanForm) actionForm;
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				false, false);
		loan = LoanServiceDelegate.getInstance().delete(loan);
		return getRequestListForward( actionForm, request, loan);
	}

	/**
	 * Change the loan's status to loan package.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doLoanPackage(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
//TODO
		/*if (!isTokenValid(request)) {
			return mapping.findForward(ACTION_FORWARD_LIST);
		}*/

		LoanForm form = (LoanForm) actionForm;
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				false, true);
		loan = LoanServiceDelegate.getInstance().loanPackage(loan);
		//ActionForward forward = getConfirmationForward( actionForm,request, loan);
		return getConfirmationForward( actionForm,
				request, loan);
	}

	/**
	 * A general method to retrieve the forward based on the loan result. In
	 * general, we forward an OK result to the confirmation page and an error
	 * result to the error page.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param loanResult
	 * @return
	 * @throws SystemException
	 */
	protected String getConfirmationForward(
			final AutoForm actionForm,
			final HttpServletRequest request, Loan loan) throws SystemException {
		//ActionForward forward = null;
		String forward=null;
		if (loan.isOK()) {
			////TODO resetToken(request);
			LoanForm form = (LoanForm) actionForm;
			releaseLock(request, form);
			form.setShowConfirmation(true);
			ControllerRedirect redirect = new ControllerRedirect(ACTION_FORWARD_CONFIRMATION);
			redirect.addParameter(PARAM_SUBMISSION_ID, loan.getSubmissionId());
			redirect.addParameter(PARAM_CONTRACT_ID, loan.getContractId());
			forward = redirect.getPath();
		} else {
			updateRequest(actionForm, request, false, loan, false, false);
			//forward = mapping.findForward(ACTION_FORWARD_ERROR);
			forward=ACTION_FORWARD_ERROR;
		}
		return forward;
	}

	/**
	 * Forward to the approval dialog page if the loan result is OK.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param loanResult
	 * @return
	 * @throws SystemException
	 */
	protected String getApprovalForward(
			final AutoForm actionForm,
			final HttpServletRequest request, Loan loan) throws SystemException {
		//ActionForward forward = null;
		String forward=null;
		updateRequest(actionForm, request, false, loan, false, false);
		if (loan.isOK()) {
			request.setAttribute(REQ_SHOW_APPROVAL_DIALOG, "true");
			//forward = mapping.findForward(ACTION_FORWARD_DEFAULT);
			forward=ACTION_FORWARD_DEFAULT;
		} else {
			//forward = mapping.findForward(ACTION_FORWARD_ERROR);
			forward=ACTION_FORWARD_ERROR;
		}
		return forward;
	}

	/**
	 * Open a new loan document page if the loan result is OK.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param loanResult
	 * @return
	 * @throws SystemException
	 */
	protected String getPrintLoanDocumentsForward(
			 final AutoForm actionForm,
			final HttpServletRequest request, Loan loan) throws SystemException {
		//ActionForward forward = null;
		String forward=null;
		if (loan.isOK()) {
			////TODO resetToken(request);
			updateRequest(actionForm, request, true, loan, false, false);
			request.setAttribute(REQ_SHOW_LOAN_DOCUMENTS, "true");
			//forward = mapping.findForward(ACTION_FORWARD_DEFAULT);
			forward=ACTION_FORWARD_DEFAULT;
		} else {
			updateRequest(actionForm, request, false, loan, false, false);
			//forward = mapping.findForward(ACTION_FORWARD_ERROR);
			forward=ACTION_FORWARD_ERROR;
		}
		return forward;
	}

	/**
	 * If the loan result is OK, returns a forward to the Loans & Withdrawal
	 * request list page. Otherwise, returns to the error page. This method is
	 * called upon Save & Exit.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param loanResult
	 * @return
	 * @throws SystemException
	 */
	protected String getRequestListForward(
			final AutoForm actionForm,
			final HttpServletRequest request, Loan loan) throws SystemException {
		//ActionForward forward = null;
		String forward = null;
		if (loan.isOK()) {
			////TODO resetToken(request);
			final LoanForm form = (LoanForm) actionForm;
			releaseLock(request, form);
			//forward = mapping.findForward(ACTION_FORWARD_LIST);
			forward=ACTION_FORWARD_LIST;
		} else {
			updateRequest(actionForm, request, false, loan, false, false);
			//forward = mapping.findForward(ACTION_FORWARD_ERROR);
			forward=ACTION_FORWARD_ERROR;
		}
		return forward;
	}

	public final String doSendForReview(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
//TODO
		/*if (!isTokenValid(request)) {
			return mapping.findForward(ACTION_FORWARD_LIST);
		}*/

		final LoanForm form = (LoanForm) actionForm;
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				false, false);
		loan = LoanServiceDelegate.getInstance().sendForReview(loan);
		//ActionForward forward = getConfirmationForward( actionForm,request, loan);
		String forward = getConfirmationForward( actionForm,request, loan);
		return forward;
	}

	public final String doSendForApproval(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
//TODO
		/*if (!isTokenValid(request)) {
			return mapping.findForward(ACTION_FORWARD_LIST);
		}
*/
		final LoanForm form = (LoanForm) actionForm;
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				false, false);
		loan = LoanServiceDelegate.getInstance().sendForApproval(loan);
		//ActionForward forward = getConfirmationForward( actionForm,request, loan);
		String forward = getConfirmationForward( actionForm,request, loan);
		return forward;
	}

	public final String doSendForAcceptance(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
//TODO
		/*if (!isTokenValid(request)) {
			return mapping.findForward(ACTION_FORWARD_LIST);
		}*/

		final LoanForm form = (LoanForm) actionForm;
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				false, false);
		loan = LoanServiceDelegate.getInstance().sendForAcceptance(loan);
		//ActionForward forward = getConfirmationForward( actionForm,request, loan);
		String forward = getConfirmationForward( actionForm,request, loan);
		return forward;
	}

	private LoanLockable getLoanLockable(final HttpServletRequest request,
			final LoanForm actionForm) {
		UserProfile userProfile = getUserProfile(request);
		Integer userProfileId = new Long(userProfile.getPrincipal()
				.getProfileId()).intValue();
		Integer contractId = Integer.valueOf(actionForm.getContractId());
		Integer submissionId = Integer.valueOf(actionForm.getSubmissionId());
		LoanLockable lockable = new LoanLockable(submissionId, contractId,
				userProfileId);
		return lockable;
	}

	protected void releaseLock(final HttpServletRequest request,
			final LoanForm actionForm) throws SystemException {

		/*
		 * Nothing to release if the form is new.
		 */
		if (actionForm.getSubmissionId() == null
				|| actionForm.getContractId() == null) {
			return;
		}

		LoanLockable lockable = getLoanLockable(request, actionForm);
		Lock lock = SubmissionServiceDelegate.getInstance().checkLock(lockable,
				true);
		if (lock != null) {
			lockable.setLock(lock);
			boolean unlocked = LockManager.getInstance(
					request.getSession(false)).release(lockable);
			if (!unlocked) {
				logger.debug("AbstractLoanAction -- problem unlocking");
			}
		} else {
			logger.debug("AbstractLoanAction -- lock is null!");
		}
	}

	/**
	 * Refresh the lock on the submission case.
	 * 
	 * @param request
	 * @param submissionId
	 * @param contractId
	 * @param submissionCaseTypeCode
	 * @return
	 */
	protected String acquireLockOrErrorForward(
			HttpServletRequest request, LoanForm form) {

		/*
		 * Nothing to lock if the form is new.
		 */
		if (form.getSubmissionId() == null || form.getContractId() == null) {
			return null;
		}

		LoanLockable lockable = getLoanLockable(request, form);
		UserProfile userProfile = getUserProfile(request);
		String userId = String.valueOf(userProfile.getPrincipal()
				.getProfileId());
		boolean locked = LockManager.getInstance(request.getSession(false))
				.lock(lockable, userId);

		if (!locked) {
			Collection<ValidationError> lockError = new ArrayList<ValidationError>(
					1);
			lockError.add(new ValidationError("LOCKED",
					ErrorCodes.WITHDRAWAL_LOCKED));
			setErrorsInSession(request, lockError);
			//TODO
//			return findForward( ACTION_FORWARD_LOCK_ERROR);
			return findForward(ACTION_FORWARD_LOCK_ERROR);
		}

		return null;
	}

	public final String doDeny(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
//TODO
		/*if (!isTokenValid(request)) {
			return mapping.findForward(ACTION_FORWARD_LIST);
		}*/

		LoanForm form = (LoanForm) actionForm;
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				false, false);
		loan = LoanServiceDelegate.getInstance().decline(loan);
		//ActionForward forward = getRequestListForward( actionForm,request, loan);
		String forward = getRequestListForward( actionForm,request, loan);
		return forward;
	}

	public final String doApprove(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
//TODO
		/*if (!isTokenValid(request)) {
			return mapping.findForward(ACTION_FORWARD_LIST);
		}*/

		final LoanForm form = (LoanForm) actionForm;
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				false, false);
		loan = LoanServiceDelegate.getInstance().validateApprove(loan);
		//ActionForward forward = getApprovalForward( actionForm,request, loan);
		String forward = getApprovalForward( actionForm,request, loan);
		return forward;
	}

	public String doPrintLoanDocuments(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
//TODO
		/*if (!isTokenValid(request)) {
			return mapping.findForward(ACTION_FORWARD_LIST);
		}*/

		final LoanForm form = (LoanForm) actionForm;
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				false, false);
		loan = LoanServiceDelegate.getInstance().printLoanDocument(loan);
		//ActionForward forward = getPrintLoanDocumentsForward(actionForm, request, loan);
		String forward = getPrintLoanDocumentsForward(actionForm, request, loan);
		return forward;
	}
	
	public String doPrintLoanDocumentsReview(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
//TODO
		/*if (!isTokenValid(request)) {
			return mapping.findForward(ACTION_FORWARD_LIST);
		}*/

		final LoanForm form = (LoanForm) actionForm;
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				false, false);
		loan = LoanServiceDelegate.getInstance().printLoanDocumentReview(loan);
		//ActionForward forward = getPrintLoanDocumentsForward(actionForm, request, loan);
		String forward = getPrintLoanDocumentsForward(actionForm, request, loan);
		return forward;
	}

	public final String doAgree(
			final AutoForm actionForm,
			final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException, SystemException {
/*//TODO
		if (!isTokenValid(request)) {
			return mapping.findForward(ACTION_FORWARD_LIST);
		}
*/
		final LoanForm form = (LoanForm) actionForm;
		Loan loan = (Loan)request.getAttribute(REQ_LOAN_DATA);
		loan = form.toLoan(loan, getUserProfile(request).getPrincipal(),
				true, false);
		
		UserRole userRoleWithPermissions = LoanActionForwardHelper
                .getUserRoleWithPermissions(getUserProfile(request), getContractId(request));
		
	    loan.setUserRole(userRoleWithPermissions);
	    
		loan = LoanServiceDelegate.getInstance().approve(loan);
		//ActionForward forward = getConfirmationForward( actionForm,request, loan);
		String forward = getConfirmationForward( actionForm,request, loan);
		return forward;
	}

	private String getLoanDocumentFileName(Loan loan) {
		return "loanDocument_" + loan.getSubmissionId() + Constants.PDF_FILE_NAME_EXTENSION;
	}

	public String doPrintLoanDocumentsPdf(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		byte[] downloadData = null;
		Loan loan = getLoan(actionForm, request);
		UserProfile userProfile = getUserProfile(request);
		Integer userProfileId = new Long(userProfile.getPrincipal()
				.getProfileId()).intValue();

		downloadData = getLoanDocumentsData(userProfileId, loan.getContractId(),
                        loan.getSubmissionId());
		if (downloadData != null && downloadData.length > 0) {
			ReportController.streamDownloadData(request, response,
					"application/pdf", getLoanDocumentFileName(loan),
					downloadData);
		}
		return null;
	}

	protected byte[] getLoanDocumentsData(Integer userProfileId, 
	        Integer contractId, Integer submissionId) throws SystemException {
        return LoanDocumentServiceDelegate.getInstance()
            .getLoanDocuments(userProfileId, contractId, submissionId, false);
	}
	
	public String doShowLoanPackagePdf(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		byte[] downloadData = null;
		Loan loan = getLoan(actionForm, request);
		UserProfile userProfile = getUserProfile(request);
		Integer userProfileId = new Long(userProfile.getPrincipal()
				.getProfileId()).intValue();

		downloadData = LoanDocumentServiceDelegate.getInstance()
				.getLoanPackage(userProfileId, loan.getContractId(),
						loan.getSubmissionId());
		if (downloadData != null && downloadData.length > 0) {
			ReportController.streamDownloadData(request, response,
					"application/pdf", getLoanDocumentFileName(loan),
					downloadData);
		}
		return null;
	}

	/**
	 * Retrieve user information from the given list of IDs.
	 * 
	 * @param profileIds
	 * @return
	 * @throws SystemException
	 */
	private Map<Integer, UserName> getUserNamesForIds(
			Collection<Integer> profileIds) throws SystemException {

		Map<Integer, UserName> userNames = getWithdrawalServiceDelegate()
				.getUserNamesForIds(profileIds);
		for (Map.Entry<Integer, UserName> entry : userNames.entrySet()) {
			UserName userName = entry.getValue();
			if (userName.getRole() == null) {
				try {
                    UserInfo uinfo = SecurityServiceDelegate.getInstance()
                            .getUserProfileByProfileId(
                                    userName.getProfileId().longValue());
                    String role = "";
                    
                    if(uinfo.getRole().isTPA()) {
        				role = LoanConstants.USER_ROLE_TPA_DISPLAY_NAME;
        			} else if (uinfo.getRole() instanceof ExternalUser) {
        				role = LoanConstants.USER_ROLE_PLAN_SPONSOR_DISPLAY_NAME;
        			} else if (uinfo.getRole().isInternalUser()){
        				role = LoanConstants.USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME;
        			}
					userName.setRole(role);
				} catch (SystemException e) {
					throw new SystemException(e, 
					        "Cannot retrieve user info for userProfileId: " 
					        + userName.getProfileId());
				}
			}
		}
		return userNames;
	}

	/**
	 * Populate additional attributes of Loan.
	 * 
	 * @param loan
	 * @return 
	 * @throws SystemException
	 */
	protected void populateAdditionalLoanData(Integer contractId,
			final HttpServletRequest request, Loan loan) throws SystemException {
		ContractPermission contractPermission = SecurityServiceDelegate
				.getInstance().getTpaFirmContractPermission(contractId);
		loan.setBundledContract(ContractServiceDelegate.getInstance()
				.isBundledGaContract(contractId));
		loan.setSigningAuthorityForContractTpaFirm(contractPermission
				.isSigningAuthority());
		loan.setDeclartionSectionDisplayed(LoanDisplayHelper
				.isShowDeclarationsSection(loan,
						getUserRoleWithPermissions(request)));
	}
	
	/**
	 * Retrieves the UserRole from request. If the object is not available in
	 * request, LoanActionForwardHelper will be called to get the value and the
	 * same will be set to the request
	 * 
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	private UserRole getUserRoleWithPermissions(final HttpServletRequest request)
			throws SystemException {
		UserRole userRoleWithPermissions = (UserRole) request
				.getAttribute(REQ_USER_ROLE_WITH_PERMISSIONS);

		if (userRoleWithPermissions == null) {
			UserProfile userProfile = getUserProfile(request);
			userRoleWithPermissions = LoanActionForwardHelper
					.getUserRoleWithPermissions(userProfile,
							getContractId(request));
			// Save user role permission object so that we don't have to
			// retrieve
			// it again later in the request.
			request.setAttribute(REQ_USER_ROLE_WITH_PERMISSIONS,
					userRoleWithPermissions);
		}

		return userRoleWithPermissions;
	}
	
	/**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of
	 * the CL#137697.
	 */
	@Autowired
    private PSValidatorFWDefault psValidatorFWDefault;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.addValidators(psValidatorFWDefault);
	}
	
}
