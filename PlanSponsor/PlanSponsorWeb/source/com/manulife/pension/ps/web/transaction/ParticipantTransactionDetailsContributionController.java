package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ParticipantListVO;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsContributionReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.Principal;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This action class handles the participant transaction details contribution
 * page.
 * 
 * @author Maria Lee
 */
@Controller
@RequestMapping(value = "/transaction")
@SessionAttributes({"participantContributionDetailsForm"})

public class ParticipantTransactionDetailsContributionController extends AbstractTransactionReportController {

	@ModelAttribute("participantContributionDetailsForm")
	public ParticipantTransactionDetailsContributionForm populateForm() {
		return new ParticipantTransactionDetailsContributionForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/transaction/pptContributionDetailsReport.jsp");
		forwards.put("default", "/transaction/pptContributionDetailsReport.jsp");
		forwards.put("filter", "/transaction/pptContributionDetailsReport.jsp");
		forwards.put("page", "/transaction/pptContributionDetailsReport.jsp");
		forwards.put("print", "/transaction/pptContributionDetailsReport.jsp");
		forwards.put("PartXSS", "redirect:/do/transaction/pptContributionDetailsReport/");
	}

	private static Logger logger = Logger.getLogger(ParticipantTransactionDetailsContributionController.class);

	private static final String DEFAULT_SORT_FIELD = TransactionDetailsContributionReportData.SORT_FIELD_RISK_CATEGORY;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;

	private static final String NUMBER_FORMAT_PATTERN = "########0.00";
	private static final String CURRENCY = "$";

	private static final String DOWNLOAD_SUMMARY_COLUMN_HEADINGS = "Last name, First name, SSN, Transaction date, Payroll ending";
	private static final String DB_DOWNLOAD_SUMMARY_COLUMN_HEADINGS = "Transaction date, Contribution date";

	private static final String DOWNLOAD_COLUMN_HEADINGS = "Investment Option, Money Type, Amount($), Unit Value, Number of Units";

	/**
	 * Constructor.
	 */
	public ParticipantTransactionDetailsContributionController() {
		super(ParticipantTransactionDetailsContributionController.class);
	}

	public String doCommon(BaseReportForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		String forward = super.doCommon(actionForm, request, response);

		 ParticipantTransactionDetailsContributionForm theForm =(ParticipantTransactionDetailsContributionForm) actionForm;

		ParticipantAccountVO participantAccountVO = null;
		ParticipantAccountDetailsVO participantDetailsVO = null;

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		int contractNumber = currentContract.getContractNumber();
		String productId = userProfile.getCurrentContract().getProductId();

		Principal principal = getUserProfile(request).getPrincipal();
		String profileId = theForm.getProfileId();
		if (currentContract.isDefinedBenefitContract()) {
			// page is navigated to DIRECTLY from transation history page, need
			// to lookup profileId
			// since DB only has one(fake)participant per contract, we will just
			// look it up via contact #
			ContractServiceDelegate csDelegate = ContractServiceDelegate.getInstance();
			ParticipantListVO pListVO = csDelegate.getParticipantList(contractNumber, null);
			ParticipantVO participantVO = (ParticipantVO) pListVO.getParticipants().iterator().next(); // only
																										// one
																										// in
																										// DB
			profileId = participantVO.getProfileId();
		}
		participantAccountVO = ParticipantServiceDelegate.getInstance().getParticipantAccount(principal, contractNumber,
				productId, profileId, null, false, false);
		participantDetailsVO = participantAccountVO.getParticipantAccountDetailsVO();

		theForm.setSsn(SSNRender.format(participantDetailsVO.getSsn(),"xxx-xx-"));
		theForm.setSsnUnmasked(participantDetailsVO.getSsn());
		theForm.setFirstName(participantDetailsVO.getFirstName());
		theForm.setLastName(participantDetailsVO.getLastName());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon ");
		}
		return forward;
	}

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		ParticipantTransactionDetailsContributionForm theForm = (ParticipantTransactionDetailsContributionForm) form;
		Contract currentContract = getUserProfile(request).getCurrentContract();

		// Retrieve profileId using participantId if the profileId was not
		// provided
		if (theForm.getProfileId() == null || theForm.getProfileId().length() == 0) {

			if (theForm.getParticipantId() != null && theForm.getParticipantId().length() > 0) {
				AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();

				// common log 78460 lookup profileId by particiapnt id and
				// contract number
				theForm.setProfileId(asd.getProfileIdByParticipantIdAndContractNumber(theForm.getParticipantId(),
						Integer.toString(currentContract.getContractNumber())));
			}

			if (theForm.getProfileId() == null || theForm.getProfileId().length() == 0) {
				throw new SystemException(null, this.getClass().getName(), "populateReportCriteria",
						"Failed to get the profileId");
			}
		}

		criteria.addFilter(TransactionDetailsContributionReportData.FILTER_CONTRACT_NUMBER,
				String.valueOf(currentContract.getContractNumber()));
		criteria.addFilter(TransactionDetailsContributionReportData.FILTER_PROFILE_ID, theForm.getProfileId());
		criteria.addFilter(TransactionDetailsContributionReportData.FILTER_TRANSACTION_NUMBER,
				theForm.getTransactionNumber());
		criteria.addFilter(TransactionDetailsContributionReportData.APPLICATION_ID,
				TransactionDetailsContributionReportData.PS_APPLICATION_ID);

		if (logger.isDebugEnabled()) {
			criteria.toString();
			logger.debug("exit -> populateReportCriteria");
		}
	}

	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		super.populateReportForm(reportForm, request);
		ParticipantTransactionDetailsContributionForm theForm = (ParticipantTransactionDetailsContributionForm) reportForm;

		// Obtain the profileId or if not available the participantId
		String profileId = request.getParameter(ParticipantTransactionDetailsContributionForm.PARAMETER_KEY_PROFILE_ID);
		String participantId = request
				.getParameter(ParticipantTransactionDetailsContributionForm.PARAMETER_KEY_PARTICIPANT_ID);

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		int contractNumber = currentContract.getContractNumber();

		if (currentContract.isDefinedBenefitContract()) {
			// page is navigated to DIRECTLY from transation history page, need
			// to lookup profileId
			// since DB only has one(fake)participant per contract, we will just
			// look it up via contact #
			ContractServiceDelegate csDelegate = ContractServiceDelegate.getInstance();
			try {
				ParticipantListVO pListVO = csDelegate.getParticipantList(contractNumber, null);
				ParticipantVO participantVO = (ParticipantVO) pListVO.getParticipants().iterator().next(); // only
																											// one
																											// in
																											// DB
				profileId = participantVO.getProfileId();
			} catch (SystemException se) {
				// log (sends general tech error message to page)
				logger.error("Problem attempting to find profileId in DefinedBenefit contract case", se);
			}
		}

		if (profileId != null && profileId.length() > 0) {
			theForm.setProfileId(profileId);
			theForm.setParticipantId(null);
		} else if (participantId != null && participantId.length() > 0) {
			theForm.setParticipantId(participantId);
			theForm.setProfileId(null);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportForm");
		}

	}

	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		// default sort is risk category
		ParticipantTransactionDetailsContributionForm theForm = (ParticipantTransactionDetailsContributionForm) form;
		String sortField = theForm.getSortField();
		String sortDirection = theForm.getSortDirection();

		criteria.insertSort(sortField, sortDirection);

		// add additional sort criteria websrtno and monty type description
		criteria.insertSort(TransactionDetailsContributionReportData.SORT_FIELD_WEBSRTNO, ReportSort.ASC_DIRECTION);

		criteria.insertSort(TransactionDetailsContributionReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION,
				ReportSort.ASC_DIRECTION);

		if (logger.isDebugEnabled()) {
			logger.debug("populateSortCriteria: inserting sort with field:" + sortField + " and direction: "
					+ sortDirection);
		}
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		TransactionDetailsContributionReportData data = (TransactionDetailsContributionReportData) report;
		StringBuffer buffer = new StringBuffer();
		ParticipantTransactionDetailsContributionForm form = (ParticipantTransactionDetailsContributionForm) reportForm;

		Contract currentContract = getUserProfile(request).getCurrentContract();
		// SSE S024 determine wheather the ssn should be masked on the csv
		// report
		boolean maskSSN = true;// set the mask ssn flag to true as a default
		try {
			maskSSN = ReportDownloadHelper.isMaskedSsn(getUserProfile(request), currentContract.getContractNumber());

		} catch (SystemException se) {
			logger.error(se);
			// log exception and output blank ssn
		}

		buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(COMMA)
				.append(currentContract.getCompanyName()).append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		// summary column heading
		if (currentContract.isDefinedBenefitContract()) {
			buffer.append(DB_DOWNLOAD_SUMMARY_COLUMN_HEADINGS);
		} else {
			buffer.append(DOWNLOAD_SUMMARY_COLUMN_HEADINGS);
		}

		if (data.getMoneySourceDescription().length() > 0) {
			buffer.append(COMMA).append("Contribution type");
		}

		if (data.isHasEmployeeContribution()) {
			buffer.append(COMMA).append("Employee contributions");
		}
		if (data.isHasEmployerContribution()) {
			buffer.append(COMMA).append("Employer contributions");
		}
		if (data.isHasEmployeeContribution() && data.isHasEmployerContribution()) {
			buffer.append(COMMA).append("Total amount");
		}
		buffer.append(COMMA).append("Transaction number").append(LINE_BREAK);

		// summary details
		if (currentContract.isDefinedBenefitContract() == false) {
			buffer.append(form.getLastName()).append(COMMA).append(form.getFirstName()).append(COMMA)
					// .append(form.getSsn())
					.append(SSNRender.format(form.getSsnUnmasked(), "", maskSSN)).append(COMMA);
		}

		buffer.append(DateRender.format(data.getTransactionDate(), RenderConstants.MEDIUM_YMD_SLASHED)).append(COMMA)
				.append(DateRender.format(data.getPayrollEndDate(), RenderConstants.MEDIUM_YMD_SLASHED));

		if (data.getMoneySourceDescription().length() > 0) {
			buffer.append(COMMA).append(data.getMoneySourceDescription());
		}

		if (data.isHasEmployeeContribution()) {
			buffer.append(COMMA).append(CURRENCY).append(NumberRender.formatByPattern(data.getContributionEEAmount(),
					ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
		}
		if (data.isHasEmployerContribution()) {
			buffer.append(COMMA).append(CURRENCY).append(NumberRender.formatByPattern(data.getContributionERAmount(),
					ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
		}
		if (data.isHasEmployeeContribution() && data.isHasEmployerContribution()) {
			buffer.append(COMMA).append(CURRENCY).append(NumberRender.formatByPattern(data.getTotalContribution(),
					ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
		}
		buffer.append(COMMA).append(data.getTransactionNumber()).append(LINE_BREAK);

		buffer.append(LINE_BREAK);

		// detail table column heading
		buffer.append(DOWNLOAD_COLUMN_HEADINGS).append(LINE_BREAK);

		// individual line items
		Iterator it1 = report.getDetails().iterator();
		while (it1.hasNext()) {
			FundGroup fundGroup = (FundGroup) it1.next();
			buffer.append(fundGroup.getGroupName()).append(LINE_BREAK);
			Fund funds[] = fundGroup.getFunds();
			for (int i = 0; i < funds.length; i++) {
				TransactionDetailsFund fund = (TransactionDetailsFund) funds[i];
				buffer.append(fund.getName()).append(COMMA).append(fund.getMoneyTypeDescription()).append(COMMA)
						.append(NumberRender.formatByPattern(fund.getAmount(), ZERO_AMOUNT_STRING,
								NUMBER_FORMAT_PATTERN))
						.append(COMMA).append(fund.getDisplayPsUnitValue()).append(COMMA)
						.append(fund.getDisplayPsNumberOfUnits()).append(LINE_BREAK);
			}
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- populateDownloadData");
		return buffer.toString().getBytes();
	}

	protected String getReportId() {
		return TransactionDetailsContributionReportData.REPORT_ID;
	}

	protected String getReportName() {
		return TransactionDetailsContributionReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	@RequestMapping(value = "/pptContributionDetailsReport/", method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("PartXSS");
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value = "/pptContributionDetailsReport/", params = "task=filter", method = {RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("PartXSS");
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value = "/pptContributionDetailsReport/", params = "task=page", method = {RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("PartXSS");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value = "/pptContributionDetailsReport/", params ="task=print", method = {RequestMethod.GET})
	public String doPrint(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("PartXSS");
			}
		}
		String forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}
	@RequestMapping(value = "/pptContributionDetailsReport/", params ="task=download", method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("PartXSS");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#137697.
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}

}
