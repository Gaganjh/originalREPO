package com.manulife.pension.bd.web.bob.transaction;

import static com.manulife.pension.bd.web.BDConstants.BOB_PAGE_FORWARD;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.InitBinder;	
import org.springframework.web.bind.ServletRequestDataBinder;		
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BDPdfController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.util.DataUtility;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.ps.service.delegate.PartyServiceDelegateAdaptor;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentDetailsItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentDetailsReportData;
import com.manulife.pension.service.account.SystemUnavailableException;
import com.manulife.pension.service.account.valueobject.LoanFundInfoVO;
import com.manulife.pension.service.account.valueobject.LoanGeneralInfoVO;
import com.manulife.pension.service.account.valueobject.LoanHoldings;
import com.manulife.pension.service.account.valueobject.LoanInformation;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.party.valueobject.GeneralInformationValueObject;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * LoanRepaymentDetailsReportAction is used to get the information for the Loan Repayment Details
 * page
 * 
 * @author harlomte
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"loanRepaymentDetailsReportForm"})

public class LoanRepaymentDetailsReportController extends BDPdfController {
	@ModelAttribute("loanRepaymentDetailsReportForm")
	public LoanRepaymentDetailsReportForm populateForm() {
		return new LoanRepaymentDetailsReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/transaction/loanRepaymentDetailsReport.jsp");
		forwards.put("loanRepaymentDetailsPage","/transaction/loanRepaymentDetailsReport.jsp");
	}
	private static Logger logger = Logger
			.getLogger(LoanRepaymentDetailsReportController.class);

	private static final String LOAN_REPAYMENT_DETAILS_PAGE = "loanRepaymentDetailsPage";

	// CSV Export Related
	private static final String DOWNLOAD_COLUMN_HEADING_LOAN_REPAYMENTS = "Date,Activity,Amount ($),Principal ($),Interest ($),Loan Balance ($)";

	private static final String COMMA = BaseReportController.COMMA;
	
	private static final String WHITE_SPACE_CHAR = BaseReportController.WHITE_SPACE_CHAR;	

	private static final String LINE_BREAK = BaseReportController.LINE_BREAK;
	
	private static final String CSV_CONTRACT = "Contract";
	
	private static final String CSV_CANNOT_RETRIEVE_PROFILE_ID = "Cannot retrieve profile ID: ";

    private static final String CSV_MESSAGE_PPT_NULL = " participantId is null and reportForm.getProfileId() returns [";

    private static final String CSV_CLOSING_BRACKET = "]";

    private static final String CSV_MESSAGE_PPT = " participantId is [";

    private static final String CSV_LOAN_NUM = "Loan #";

    private static final String CSV_ORG_LOAN_AMT = "Original loan amount";

    private static final String CSV_LOAN_ISSUE = "Loan Issue";

    private static final String CSV_LOAN_ISSUE_DATE = "Loan issue date";

    private static final String CSV_TRANSFER_AMT = "Transfer Amount";

    private static final String CSV_TRANSFER_DT = "Transfer Date";

    private static final String CSV_TRANSFER_LOAN = "Transfer Loan";

    private static final String CSV_OUTSTANDING_BALNCE = "Outstanding balance as of ";
    
    private static final String CSV_OUTSTANDING_BALNCE_TOTAL = "Outstanding Balance Total";

    private static final String CSV_TYPE_OF_LOAN = "Type of loan";

    private static final String CSV_LAST_PAYMENT_AMT = "Last payment total";

    private static final String CSV_NA = "n/a";

    private static final String CSV_DATE_LAST_PAYMENT = "Date of last payment";

    private static final String CSV_DAYS_SINCE_LAST_PAYMENT = "Days since last payment";

    private static final String CSV_INTEREST_RATE = "Interest rate";

    private static final String CSV_LOAN_MATUREITY_DT = "Loan maturity date";

	private static final String REQUEST_PARAM_CONTRACT_ID = "contractId";

    private static final String REQUEST_PARAM_PARTICIPANT_ID = "participantId";
	
	private static final String REPORT_ID = "LoanRepaymentDetails";

	private static final String SSN_DEFAULT_VALUE = "000000000";

	private static final String RATE_PATTERN = "###,##0.00";

	private static final String XSLT_FILE_KEY_NAME = "LoanRepaymentDetailsReport.XSLFile";
	
	private static final String DOLLAR = "$";
	
	private static final String PERCENT = "%";
	

	/**
	 * constructor
	 */
	public LoanRepaymentDetailsReportController() {
		super(LoanRepaymentDetailsReportController.class);
	}

	private String getTask(HttpServletRequest request) {
		String task = request.getParameter(BDConstants.TASK_KEY);
		if (task == null || task.length() == 0) {
			task = AutoForm.DEFAULT;
		}
		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseAction #doExecute(
	 *      org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 *  
	 */
	
	@RequestMapping(value ="/transaction/loanRepaymentDetailsReport/",  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doExecute(@Valid @ModelAttribute("loanRepaymentDetailsReportForm") LoanRepaymentDetailsReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)){
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
			
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doExecute");
		}
		int contractNumber = 0;
		String profileId = null;
		LoanRepaymentDetailsReportData reportData = null;
		LoanRepaymentDetailsReportForm reportForm = null;

		try {
			// get the contract number
			BobContext bobContext = getBobContext(request);

			String contractId = request.getParameter(REQUEST_PARAM_CONTRACT_ID);
			if (StringUtils.trimToNull(contractId) != null) {
				contractNumber = Integer.parseInt(contractId);
			} else if (bobContext != null) {
				contractNumber = bobContext.getCurrentContract().getContractNumber();
			}

			// get other input parameters for the delegate
			reportForm = (LoanRepaymentDetailsReportForm) actionForm;
			// get the ProfileId
			profileId = retrieveProfileId(request, reportForm);

			// populate SSN and UserName
			retrieveNameAndSSN(reportForm, contractNumber, profileId);

			// get the LoanHoldings
			LoanHoldings loanHoldings = getParticipantLoanList(contractNumber, profileId);

			// check the AccountService Return codes and status
			if (loanHoldings != null) {
				// get the system status
				reportData = getParticipantLoanDetails(reportForm, loanHoldings);
				populateCappingCriteria(reportData, reportForm, null);
				// check for the Download action
				if (BDConstants.DOWNLOAD_TASK.equals(getTask(request))) {
					return doDownload(reportData, request, response);
				} else if (BDConstants.PRINT_PDF_TASK.equals(getTask(request))) {
					return doPrintPDF(reportForm, reportData, null, request, response);
				}
			}
		} catch (SystemUnavailableException e) {
			List<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(BDErrorCodes.OUT_OF_SERVICE_HOURS));
			BDSessionHelper.setErrorsInSession(request, errors);
			request.setAttribute(BDConstants.IS_ERROR, Boolean.TRUE);
			return forwards.get("input");
		} catch (SystemException e) {
			// Log the system exception.
			LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID, e);

			// Show user friendly message.
			List<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(BDErrorCodes.TECHNICAL_DIFFICULTIES));
			BDSessionHelper.setErrorsInSession(request, errors);
			return forwards.get("input");
		}
		// set the LoanRepayment details as an attribute
		if (reportData == null) {
			// create an empty one if it doesn't exist
			reportData = new LoanRepaymentDetailsReportData();
		}
		// set the report data attribute
		request.setAttribute(BDConstants.LOAN_REPAYMENT_DETAILS_REPORT_DATA, reportData);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doExecute");
		}
		// forward to the loan repayment page
		return forwards.get("input");

	}
		
/*	 @RequestMapping(value = "/transaction/loanRepaymentDetailsReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
		public String doPrintPDF(@Valid @ModelAttribute("loanRepaymentDetailsReportForm") LoanRepaymentDetailsReportForm form,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, SystemException {
			 String forward = preExecute(form, request, response);
		    	if(StringUtils.isNotBlank(forward)) {
		    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
		    	}
			if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// default
				}
			}
			 forward = super.doPrintPDF(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}*/
	/**
	 * Retrieves SSN and User Name if it doesn't exist in the form
	 * 
	 * @param reportForm
	 */
	private void retrieveNameAndSSN(LoanRepaymentDetailsReportForm reportForm,
			int contractNumber, String profileId) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> retrieveNameAndSSN");
        }
		// retrieve and mask SSN

		// get SSN and (or) Name using ezk's PartyService
		GeneralInformationValueObject giVO = null;
		PartyServiceDelegateAdaptor psd = new PartyServiceDelegateAdaptor();
		giVO = psd.getGeneralInformation(null, profileId, String
				.valueOf(contractNumber));
		if (giVO != null) {
			// get SSN
			if (giVO.getSsn() != null) {
				reportForm.setMaskedSsn(SSNRender.format(giVO.getSsn(),
						SSN_DEFAULT_VALUE, true));
			}
			// Get name
			if (giVO.getFirstname() != null && giVO.getLastname() != null) {
				reportForm.setName(giVO.getLastname() + COMMA + WHITE_SPACE_CHAR
						+ giVO.getFirstname());
			}
		}
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> retrieveNameAndSSN");
        }

	}

	/**
	 * Retrieves Profile ID. It could be at the form or mapping based on whether
	 * participantId is provided.
	 * 
	 * @param reportForm
	 * @return
	 */
	private String retrieveProfileId(HttpServletRequest request,
			LoanRepaymentDetailsReportForm reportForm) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> retrieveProfileId");
        }

		String participantId = (String) ObjectUtils.defaultIfNull(request
				.getParameter(REQUEST_PARAM_PARTICIPANT_ID), BDConstants.SPACE_SYMBOL);
		String profileId = reportForm.getProfileId();

		if (participantId.length() > 0) {
			AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();
			profileId = asd.getProfileIdByParticipantId(participantId);
			reportForm.setProfileId(profileId);
		}

		if (StringUtils.isEmpty(profileId)) {
			StringBuffer message = new StringBuffer(CSV_CANNOT_RETRIEVE_PROFILE_ID);
			if (request.getParameter(REQUEST_PARAM_PARTICIPANT_ID) == null) {
				message
						.append(CSV_MESSAGE_PPT_NULL + reportForm.getProfileId()
                        + CSV_CLOSING_BRACKET);
			} else {
				message.append(CSV_MESSAGE_PPT + participantId + CSV_CLOSING_BRACKET);
			}
			throw new SystemException(new IllegalArgumentException(),
					getClass().getName(), "retrieveProfileId", message
							.toString());
		}

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> retrieveProfileId");
        }

		return profileId;
	}

    /**
     * Get the Loan Holdings.
     * 
     * @param contractNumber
     * @param profildId
     * @return LoanHoldings
     */
	protected LoanHoldings getParticipantLoanList(int contractNumber,
			String profileId) throws SystemException,
			SystemUnavailableException {

		LoanHoldings loanHoldings = null;

		AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();
		loanHoldings = asd.getLoanHoldings(null, profileId, contractNumber);

		return loanHoldings;
	}

    /**
     * Get the Participant loan Details.
     * 
     * @param form
     * @param loanHoldings
     * @return LoanRepaymentDetailsReportData VO
     */
	private LoanRepaymentDetailsReportData getParticipantLoanDetails(
			LoanRepaymentDetailsReportForm form, LoanHoldings loanHoldings) {

		LoanInformation loanInformation = null;
		int loanNumber = -1; // in case form doesn't pass any

		// get the loan number from the form
        if (!StringUtils.isEmpty(form.getLoanNumber())) {
			loanNumber = Integer.parseInt(form.getLoanNumber());
		}

		// retrive loans from the loanHoldings VO
		LoanInformation[] loans = loanHoldings.getLoans();
		if (loans != null) {
			// get the particular loan
            for (int loanCount = 0; loanCount < loans.length; loanCount++) {
                loanInformation = loans[loanCount];
				if (loanInformation.getNumber() == loanNumber) {
					break;
				}
			}
		}
		return populateValueObject(form, loanHoldings, loanInformation);
	}

    /**
     * Populate the VO object and return it.
     * 
     * @param form
     * @param loanHoldings
     * @param loanInfo
     * @return LoanRepaymentDetailsReportData VO
     */
	private LoanRepaymentDetailsReportData populateValueObject(
			LoanRepaymentDetailsReportForm form, LoanHoldings loanHoldings,
			LoanInformation loanInfo) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateValueObject");
        }

		LoanRepaymentDetailsReportData reportData = new LoanRepaymentDetailsReportData();

		// populate the header
		reportData.setMaskedSsn(form.getMaskedSsn());
		reportData.setName(form.getName());
		reportData.setReturnCode(loanHoldings.getReturnCode());
		reportData.setInquiryDate(loanHoldings.getInquiryDate());

		// loan exists
		if (loanInfo != null) {
			reportData.setNumber(loanInfo.getNumber());
			reportData.setStatus(loanInfo.getStatus());
		
			reportData.setRemainingPrincipal(loanInfo.getRemainingPrincipal());
			reportData.setOutstandingBalanceAmount(loanInfo
					.getOutstandingBalanceAmount());
			reportData
					.setLoanOriginIndicator(loanInfo.getLoanOriginIndicator());
			reportData.setExpenseMargin(loanInfo.getExpenseMargin());
			reportData.setActivityReturnCode(loanInfo.getActivityReturnCode());
			reportData.setPrincipalPaidWithinLast12Months(loanInfo
					.getPrincipalPaidWithinLast12Months());

			Map<String,BigDecimal> moneyBasedFundInfo = new TreeMap<String,BigDecimal>();
			BigDecimal tempVal;
			int amoritizationMonths;
			int monthDiff;
			if(loanInfo.getLoanGeneralInfo()!=null)
			{
			reportData.setLoanGeneralInfo(loanInfo.getLoanGeneralInfo());
			Calendar calIssueDate = Calendar.getInstance();
			Calendar calMaturityDate = Calendar.getInstance();
			// default will be Gregorian in US Locales
			calIssueDate.setTime(loanInfo.getLoanGeneralInfo().getLoanBeginDate());
			calMaturityDate.setTime(loanInfo.getLoanGeneralInfo().getLoanMaturityDate());
			int diffYear = calMaturityDate.get(Calendar.YEAR) - calIssueDate.get(Calendar.YEAR);
			 amoritizationMonths = diffYear * 12 + calMaturityDate.get(Calendar.MONTH) - calIssueDate.get(Calendar.MONTH);
			reportData.setAmoritizationPeriod(amoritizationMonths);
			reportData.setTransferDate(loanInfo.getLoanGeneralInfo().getLoanBeginDate());
			reportData.setTransferAmount(loanInfo.getTransferAmount());
			reportData.setTransferRate(loanInfo.getLoanGeneralInfo().getLoanInterestRate());
			reportData.setMaturityDate(loanInfo.getLoanGeneralInfo().getLoanMaturityDate());

			if(loanInfo.getLoanGeneralInfo().getLoanFundInfoList()!=null)
			{
				Iterator it= loanInfo.getLoanGeneralInfo().getLoanFundInfoList().iterator();
				LoanFundInfoVO  fundvo;
			
				while(it.hasNext())
				{
					fundvo = (LoanFundInfoVO)it.next();
					tempVal=BigDecimal.ZERO;
					String clientLongName =fundvo.getClientLongName().trim();
					if(moneyBasedFundInfo.get(clientLongName) !=null)
					{
						tempVal =moneyBasedFundInfo.get(clientLongName);
						tempVal= tempVal.add(fundvo.getLoanfundAmt());
								
						moneyBasedFundInfo.put(clientLongName,tempVal);
					}
					else {
						moneyBasedFundInfo.put(clientLongName,fundvo.getLoanfundAmt());
					}
				}
			}
			}
			reportData.setMoneyTypeFunds(moneyBasedFundInfo);
			LoanRepaymentDetailsItem lastRepaymentItem = null;

			//populate the detail items
			LoanRepaymentDetailsItem[] items = null;
			int sizeOfActivities = loanInfo.getActivities().length;
			if (sizeOfActivities > 0) {
				Date lastRepaymentItemDate = null;
				items = new LoanRepaymentDetailsItem[sizeOfActivities];
				for (int activityCount = 0; activityCount < sizeOfActivities; activityCount++) {
					LoanRepaymentDetailsItem item = new LoanRepaymentDetailsItem();
					item.setDate(loanInfo.getActivities()[activityCount].getDate());
                    item.setType(loanInfo.getActivities()[activityCount].getType());
                    BigDecimal expenseMargin = loanInfo.getActivities()[activityCount]
							.getExpenseMargin();
					item.setExpenseMargin(expenseMargin);

					if (BDConstants.LOAN_ACTIVITY_REPAYMENT.equals(loanInfo
							.getActivities()[activityCount].getType())) {
						item
								.setTypeDesc(BDConstants.LOAN_ACTIVITY_REPAYMENT_DESC);

						item.setAmount(loanInfo.getActivities()[activityCount].getAmount());
                        item.setPrincipal(loanInfo.getActivities()[activityCount]
								.getPrincipal());
						BigDecimal interest = loanInfo.getActivities()[activityCount]
								.getInterest();

						if (expenseMargin != null) {
							item.setInterest(interest.add(expenseMargin));
						} else {
							item.setInterest(interest);
						}
					} else if (BDConstants.LOAN_ACTIVITY_DEFAULTED
							.equals(loanInfo.getActivities()[activityCount].getType())) {
						item
								.setTypeDesc(BDConstants.LOAN_ACTIVITY_DEFAULTED_DESC);
					} else {
						item.setTypeDesc(BDConstants.SPACE_SYMBOL);
					}

					item.setBalance(loanInfo.getActivities()[activityCount].getBalance());
                    items[sizeOfActivities - activityCount - 1] = item;

					if (BDConstants.LOAN_ACTIVITY_REPAYMENT
							.equals(item.getType())) {
						if (lastRepaymentItem == null
								|| item.getDate().compareTo(
										lastRepaymentItemDate) > 0) {
							lastRepaymentItem = item;
							lastRepaymentItemDate = item.getDate();
						}
					}
				}
			}

            /*
             * Populate summary fields based on last loan repayment date/amount found in detail
             * section.
             */
			if (lastRepaymentItem != null) {
				reportData.setDaysSinceLastPayment(DataUtility.daysBetween(
						lastRepaymentItem.getDate(), new Date()));
				reportData.setLastRepaymentDate(lastRepaymentItem.getDate());
				reportData
						.setLastRepaymentAmount(lastRepaymentItem.getAmount());
			} else {
				reportData.setLastRepaymentAmount(new BigDecimal(0d));
				reportData.setLastRepaymentDate(null);
				reportData.setDaysSinceLastPayment(0);
			}

			reportData.setItems(items);
		}
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateValueObject");
        }


		return reportData;
	}

    /**
     * This method returns the CSV representation of the report.
     * 
     * @param mapping
     * @param reportData
     * @param request
     * @param response
     * @return ActionForward
     * @throws ServletException
     */
	private String doDownload(LoanRepaymentDetailsReportData reportData,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		
		String strAsOfDate = DateRender.format(reportData.getInquiryDate(),
				BDConstants.DATE_FORMAT_MMDDYYYY);
		String reportFileName = REPORT_ID + BDConstants.NO_RULE + getFormatInquiryDate(strAsOfDate) + BaseReportController.CSV_EXTENSION;
		
		byte[] downloadData = getDownloadData(request, reportData);
		if (downloadData != null) {
			BaseReportController.streamDownloadData(request, response, BaseReportController.CSV_TEXT,
					reportFileName, downloadData);
		}
		return null;
	}

	/**
	 * Creates the .CSV file based on the LoanRepaymentDetailsReportData object
	 * 
	 * @param out
	 * @param report
	 */
	private byte[] getDownloadData(HttpServletRequest request,
			LoanRepaymentDetailsReportData reportData) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}
		// get the content objects
		Content message = null;
		Content message1 = null;
		StringBuffer buffer = new StringBuffer();
		Contract currentContract = getBobContext(request).getCurrentContract();
		if(currentContract != null) {
        buffer.append(CSV_CONTRACT).append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(escapeField(
				currentContract.getCompanyName())).append(LINE_BREAK);
		} else {
	    buffer.append(CSV_CONTRACT).append(COMMA).append(
                    request.getParameter(REQUEST_PARAM_CONTRACT_ID)).append(COMMA).append(
					BDConstants.SPACE_SYMBOL).append(LINE_BREAK);
		}
		
		try {
			LoanGeneralInfoVO generalVO = reportData.getLoanGeneralInfo();
			message = ContentCacheManager.getInstance().getContentById(
					BDContentConstants.TRANSACTION_LOAN_REPAYMENT_LAYOUT_PAGE,
					ContentTypeManager.instance().LAYOUT_PAGE);
			
			message1 = ContentCacheManager.getInstance().getContentById(
					BDContentConstants.TRANSACTION_DETAILS_LOAN_REPAYMENT_LAYOUT_PAGE,
					ContentTypeManager.instance().LAYOUT_PAGE);

			buffer.append(
					ContentUtility.getContentAttribute(message1, BDContentConstants.SUB_HEADER))
					.append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			// Loanholder's information
            buffer.append(CSV_LOAN_NUM) .append(COMMA).append(reportData.getNumber())
					.append(LINE_BREAK);
			buffer.append("Name").append(COMMA).append(escapeField(reportData.getName())).append(LINE_BREAK);
			buffer.append("SSN").append(COMMA).append(reportData.getMaskedSsn()).append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			if(generalVO!=null)
			{
				String loanReason = "Unspecified";
				String beginDateLabel = "Transfer date";
				String loanTypeName = "Transfer Loan";
				if("LI".equalsIgnoreCase(generalVO.getTypeOfLoan())){
					beginDateLabel = "Loan issue date";
					loanTypeName = "Loan Issue";

				}
				buffer.append("Type of loan" ).append(COMMA).append(loanTypeName).append(COMMA);
				buffer.append(beginDateLabel).append(COMMA).append(
						DateRender.format(generalVO.getLoanBeginDate(),
								RenderConstants.MEDIUM_YMD_SLASHED)).append(LINE_BREAK);
				if("PP".equalsIgnoreCase(generalVO.getLoanReason())){
					loanReason = "Purchase of Primary Residence";
				}else if("HD".equalsIgnoreCase(generalVO.getLoanReason())){
					loanReason = "Hardship";
				}else if("GP".equalsIgnoreCase(generalVO.getLoanReason())){
					loanReason = "General Purpose";
				}
				buffer.append("Loan reason" ).append(COMMA).append(loanReason).append(COMMA);

				buffer.append("Loan maturity date" ).append(COMMA).append(
						DateRender.format(generalVO.getLoanMaturityDate(),
								RenderConstants.MEDIUM_YMD_SLASHED)).append(LINE_BREAK);
				buffer.append("Interest rate").append(COMMA).append(NumberRender.formatByPattern(generalVO.getLoanInterestRate(),
						BDConstants.DEFAULT_VALUE_ZERO,
						BDConstants.AMOUNT_FORMAT)).append(PERCENT).append(LINE_BREAK);



				/*if(!generalVO.getTypeOfLoan().isEmpty() && "LI".equalsIgnoreCase(generalVO.getTypeOfLoan())){
					buffer.append("Amortization Period" ).append(COMMA).append(reportData.getAmoritizationPeriod()+" months").append(LINE_BREAK);
				}*/
				buffer.append(LINE_BREAK);
			}
			if(!reportData.getMoneyTypeFunds().isEmpty())
			{
				buffer.append("Money Type" ).append(COMMA).append( "Amount ($)").append(LINE_BREAK);
				Set entrySet = reportData.getMoneyTypeFunds().entrySet();
				Iterator it = entrySet.iterator();
				BigDecimal totalAmt = BigDecimal.ZERO;
				while (it.hasNext()) {
					Map.Entry me = (Map.Entry) it.next();
					totalAmt = totalAmt.add((BigDecimal) me.getValue());
					buffer.append(me.getKey()  +COMMA + 
							me.getValue()).append(LINE_BREAK);
				}
				buffer.append("Total" ).append(COMMA).append(totalAmt);
			}
			buffer.append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			buffer.append(ContentUtility.getContentAttribute(message1, BDContentConstants.BODY2_HEADER)).append(LINE_BREAK);
			
			
			// Outstanding balance / type of loan
			buffer.append(CSV_OUTSTANDING_BALNCE).append("")
					.append(DateRender.format(reportData.getInquiryDate(),
									RenderConstants.MEDIUM_YMD_SLASHED))
					.append(COMMA).append(DOLLAR)
					.append(NumberRender.formatByPattern(reportData
									.getOutstandingBalanceAmount(),
									BDConstants.DEFAULT_VALUE_ZERO,
                                    BDConstants.AMOUNT_FORMAT))
					.append(COMMA) ;
      
			// last payment date / original amount
			Date lastPaymentDate = reportData.getLastRepaymentDate();
			String lastPaymentDateString = CSV_NA;
            String daysSinceLastPaymentString = CSV_NA;
			
			if (lastPaymentDate != null) {
				lastPaymentDateString = DateRender.format(lastPaymentDate,
						RenderConstants.MEDIUM_YMD_SLASHED);
				daysSinceLastPaymentString = String.valueOf(reportData
						.getDaysSinceLastPayment());
			}
			 buffer.append(CSV_DAYS_SINCE_LAST_PAYMENT).append(COMMA).append(
						daysSinceLastPaymentString).append(LINE_BREAK);
			  buffer.append(CSV_LAST_PAYMENT_AMT)
              .append(COMMA)
              .append(DOLLAR)
              .append(NumberRender.formatByPattern(reportData.getLastRepaymentAmount(),
								BDConstants.DEFAULT_VALUE_ZERO, BDConstants.AMOUNT_FORMAT))
				.append(COMMA);
			buffer.append(CSV_DATE_LAST_PAYMENT).append(COMMA).append(
					lastPaymentDateString).append(LINE_BREAK);

			buffer.append(LINE_BREAK);
			buffer.append(
					ContentUtility.getContentAttribute(message1, BDContentConstants.BODY1_HEADER))
					.append(LINE_BREAK);
			buffer.append(DOWNLOAD_COLUMN_HEADING_LOAN_REPAYMENTS).append(
					LINE_BREAK);

			// Loan Repayment Detail Data
			int sizeOfActivities = 0;
			if (reportData.getItems() != null) {
				sizeOfActivities = reportData.getItems().length;
			}
			if (sizeOfActivities > 0) {
				for (int activityCount = 0; activityCount < sizeOfActivities; activityCount++) {
					buffer.append(LINE_BREAK);
					LoanRepaymentDetailsItem item = reportData.getItems()[activityCount];
					String repaymentDate = BDConstants.SPACE_SYMBOL;

					// append to the buffer
					if (item.getDate() != null) {
						repaymentDate = DateRender.format(item.getDate(),
								RenderConstants.MEDIUM_YMD_SLASHED);
					}

					buffer.append(repaymentDate).append(COMMA);
					buffer.append(item.getTypeDesc()).append(COMMA);
					if (item.getAmount() != null) {
						buffer.append(
								NumberRender.formatByPattern(item.getAmount(),
								        BDConstants.DEFAULT_VALUE_ZERO,
										BDConstants.AMOUNT_FORMAT)).append(COMMA);
					} else {
						buffer.append(COMMA);
					}

					if (item.getPrincipal() != null) {
						buffer.append(
								NumberRender.formatByPattern(item
										.getPrincipal(),
                                        BDConstants.DEFAULT_VALUE_ZERO,
										BDConstants.AMOUNT_FORMAT)).append(
                                COMMA);
					} else {
						buffer.append(COMMA);
					}

					if (item.getInterest() != null) {
						buffer.append(
								NumberRender.formatByPattern(
										item.getInterest(),
                                        BDConstants.DEFAULT_VALUE_ZERO,
										BDConstants.AMOUNT_FORMAT)).append(
                                COMMA);
					} else {
						buffer.append(COMMA);
					}

					buffer.append(
							NumberRender.formatByPattern(item.getBalance(),
							        BDConstants.DEFAULT_VALUE_ZERO,
                                    BDConstants.AMOUNT_FORMAT))
							.append(COMMA);
				}
			} 
		} catch (ContentException e) {
			throw new SystemException(e, getClass().getName(),
					"populateDownloadData", "Something wrong with CMA");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected String preExecute(final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException, SystemException {
        // Get the existing value.
    	// Wei: This seems to be related to withdrawals, comment it out
        //final Object lastPage = request.getSession().getAttribute(
        //        WebConstants.LAST_ACTIVE_PAGE_LOCATION);

        // This operation resets/clears the last active page location.
        final String forwardResult = super.preExecute(form, request, response);

        // Put the previously the existing value back.
        //request.getSession().setAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION, lastPage);

        BobContextUtils.setUpBobContext(request);

        BobContext bob = BDSessionHelper.getBobContext(request);
        if (bob == null || bob.getCurrentContract() == null) {
        	return forwards.get(BOB_PAGE_FORWARD);
        }
        if (bob.getCurrentContract().getCompanyCode().equals(
				GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
			ApplicationHelper.setRequestContentLocation(request,
					Location.NEW_YORK);
		}
        BobContextUtils.setupProfileId(request);
        
        return forwardResult;
    }

    /**
     * @See BDPdfAction#prepareXMLFromReport()
     */
    @Override
    public Document prepareXMLFromReport(BaseForm reportForm, Object reportData, ParticipantAccountVO participantAccountVO,
            HttpServletRequest request) throws ParserConfigurationException {
        
        LoanRepaymentDetailsReportForm form = (LoanRepaymentDetailsReportForm) reportForm;
        LoanRepaymentDetailsReportData data = (LoanRepaymentDetailsReportData) reportData;
        int rowCount = 1;
        int maxRowsinPDF;

        PDFDocument doc = new PDFDocument();
                        
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.LOAN_REPAYMENT_DETAILS_PATH, request);
        
        Element rootElement = doc.createRootElement(BDPdfConstants.LOAN_REPAYMENT_DETAILS);
        
        setIntroXMLElements(layoutPageBean, doc, rootElement, request);
        
        // Summary Info
        setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean);
        
        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);
        
        int noOfRows = getNumberOfRowsInReport(reportData);

        if (noOfRows > 0) {
        	        	
            //Repayment Transaction Details - start
            Element txnDetailsElement = doc.createElement(BDPdfConstants.TXN_DETAILS);
            Element txnDetailElement;
            LoanRepaymentDetailsItem[] items = data.getItems();
            maxRowsinPDF = form.getCappedRowsInPDF();
            for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
                txnDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
                LoanRepaymentDetailsItem theItem = items[i];
                setReportDetailsXMLElements(doc, txnDetailElement, theItem);
                doc.appendElement(txnDetailsElement, txnDetailElement);
                rowCount++;
            }
            doc.appendElement(rootElement, txnDetailsElement);
            // Transaction Details - end
        }

        if (form.getPdfCapped()) {
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
        }
        Location location = ApplicationHelper.getRequestContentLocation(request);
        setFooterXMLElements(layoutPageBean, doc, rootElement, location);
        
        return doc.getDocument();
    }
    
    /**
     * @See BDPdfAction#populateCappingCriteria()
     */
    @Override
    protected void populateCappingCriteria(Object reportData, BaseForm form, ParticipantAccountVO participantAccountVO) {

        LoanRepaymentDetailsReportForm reportForm = (LoanRepaymentDetailsReportForm) form;
        reportForm.setPdfCapped(false);
        
        if (getNumberOfRowsInReport(reportData) > getMaxCappedRowsInPDF()) {
            reportForm.setCappedRowsInPDF(getMaxCappedRowsInPDF());
            reportForm.setPdfCapped(true);
        } 
        else {
            reportForm.setCappedRowsInPDF(getNumberOfRowsInReport(reportData));
        }
        
    }

    /**
     * @See BDPdfAction#getNumberOfRowsInReport()
     */
    @Override
    public Integer getNumberOfRowsInReport(Object reportData) {
        LoanRepaymentDetailsReportData data = (LoanRepaymentDetailsReportData) reportData;
        int noOfRows = 0;
        if(data.getItems() != null) {
            noOfRows = data.getItems().length;
        }
        return noOfRows;
    }
    
    /**
     * @See BDPdfAction#getXSLTFileName()
     */
    public String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }

    /**
     * This method sets summary information XML elements
     * 
     * @param doc
     * @param rootElement
     * @param data
     * @param layoutPageBean
     */
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement, LoanRepaymentDetailsReportData data, 
                 LayoutPage layoutPageBean) {
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);
    	String strAsOfDate = DateRender.format(data.getInquiryDate(),
				BDConstants.DATE_FORMAT_MMDDYYYY);
        
        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);
        LoanGeneralInfoVO genInfoVO = data.getLoanGeneralInfo();
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_NAME, data.getName());
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_SSN, data.getMaskedSsn());
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.LOAN_NUMBER, String.valueOf(data
                .getNumber()));
        String loanReason = "Unspecified";
		String beginDateLabel = "Transfer Date:";
		String loanTypeName = "Transfer Loan";
        if(genInfoVO!=null){
		if("LI".equalsIgnoreCase(genInfoVO.getTypeOfLoan())){
			beginDateLabel = "Loan Issue Date:";
			loanTypeName = "Loan Issue";
			
		}
		if("PP".equalsIgnoreCase(genInfoVO.getLoanReason())){
			loanReason = "Purchase of Primary Residence";
		}else if("HD".equalsIgnoreCase(genInfoVO.getLoanReason())){
			loanReason = "Hardship";
		}else if("GP".equalsIgnoreCase(genInfoVO.getLoanReason())){
			loanReason = "General Purpose";
		}
        }
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.LOAN_TYPE, loanTypeName);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.LOAN_REASON, loanReason);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.INTEREST, 
        		NumberRender.format(genInfoVO.getLoanInterestRate(), null, RATE_PATTERN, RenderConstants.PADDING_NONE, 2, BigDecimal.ROUND_HALF_DOWN, 1, Boolean.TRUE)+PERCENT);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.ISSUE_DATE_LABEL,beginDateLabel);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.ISSUE_DATE, 
                DateRender.formatByStyle(genInfoVO.getLoanBeginDate(), null, RenderConstants.MEDIUM_STYLE));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.MATURITY_DATE, 
                DateRender.formatByStyle(genInfoVO.getLoanMaturityDate(), null, RenderConstants.MEDIUM_STYLE));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.AMORT_PERIOD, String.valueOf(data.getAmoritizationPeriod()));
        
        
        if(!data.getMoneyTypeFunds().isEmpty())  {


        	Element moneyTypeListElement = doc.createElement(BDPdfConstants.MONEYTYPEINFODETAILS);
        	Element moneyTypeElement;
        	LoanRepaymentDetailsItem[] items = data.getItems();
        	Set entrySet = data.getMoneyTypeFunds().entrySet();
        	Iterator it = entrySet.iterator();
        	BigDecimal totalAmt = BigDecimal.ZERO;
        	while (it.hasNext()) {
        		Map.Entry me = (Map.Entry) it.next();
        		totalAmt = totalAmt.add((BigDecimal) me.getValue());
        		if(me.getKey()!=null && me.getValue()!=null)
        		{
        			moneyTypeElement = doc.createElement(BDPdfConstants.MONEYTYPEINFO);
        			doc.appendTextNode(moneyTypeElement, BDPdfConstants.MONEY_TYPE_NAME,  me.getKey().toString());

        			doc.appendTextNode(moneyTypeElement, BDPdfConstants.MONEY_TYPE_AMOUNT,  
        					NumberRender.formatByType(me.getValue(), BDConstants.NO_RULE, RenderConstants.DECIMAL_TYPE));


        			doc.appendElement(moneyTypeListElement, moneyTypeElement);

        		}
        		doc.appendElement(rootElement, moneyTypeListElement);

        	}
        	
        	 doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_MTY_AMT, 
        			 NumberRender.formatByType(totalAmt, BDConstants.NO_RULE, RenderConstants.DECIMAL_TYPE));
        }
        String bodyHeader2 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY2_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER2, summaryInfoElement, doc, bodyHeader2);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.INQUIRY_DATE, 
        DateRender.formatByStyle(data.getInquiryDate(), null, RenderConstants.MEDIUM_STYLE));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.OUTSTANDING_BALANCE,  
                NumberRender.formatByType(data.getOutstandingBalanceAmount(), null, RenderConstants.CURRENCY_TYPE));
        
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.LAST_REPAYMENT_AMT,  
                NumberRender.formatByType(data.getLastRepaymentAmount(), null, RenderConstants.CURRENCY_TYPE));
        if (data.getLastRepaymentDate() != null) {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.DAYS_SINCE_LAST_REPAYMENT,  
                    NumberRender. formatByType(data.getDaysSinceLastPayment(), BDConstants.ZERO_STRING, RenderConstants.PADDING_NONE, 0, BigDecimal.ROUND_HALF_DOWN, 1));
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.LAST_REPAYMENT_DATE, 
                    DateRender.formatByStyle(data.getLastRepaymentDate(), null, RenderConstants.MEDIUM_STYLE));
        }
       /* doc.appendTextNode(summaryInfoElement, BDPdfConstants.LOAN_TYPE, data
                .getLoanOriginIndicator());
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TRANSFER_DATE, 
                DateRender.formatByStyle(data.getTransferDate(), null, RenderConstants.MEDIUM_STYLE));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TRANSFER_AMT,  
                NumberRender.formatByType(data.getTransferAmount(), null, RenderConstants.CURRENCY_TYPE));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TRANSFER_RATE,  
                NumberRender.format(data.getTransferRate(), null, RATE_PATTERN, RenderConstants.PADDING_NONE, 2, BigDecimal.ROUND_HALF_DOWN, 1, Boolean.TRUE));
       
        */
        doc.appendElement(rootElement, summaryInfoElement);
    }
    
    /**
     * This method sets report details XML elements
     * 
     * @param doc
     * @param txnDetailElement
     * @param theItem
     */
    private void setReportDetailsXMLElements(PDFDocument doc, Element txnDetailElement, LoanRepaymentDetailsItem theItem) {
        if (theItem != null) {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TRANSFER_DATE, 
                        DateRender.formatByStyle(theItem.getDate(), null, RenderConstants.MEDIUM_STYLE));
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TYPE_DESC, theItem
                    .getTypeDesc());
            doc.appendTextNode(txnDetailElement, BDPdfConstants.AMT,  
                    NumberRender.formatByType(theItem.getAmount(), BDConstants.NO_RULE, RenderConstants.DECIMAL_TYPE));
            doc.appendTextNode(txnDetailElement, BDPdfConstants.BALANCE,  
                    NumberRender.formatByType(theItem.getBalance(), null, RenderConstants.DECIMAL_TYPE));
            doc.appendTextNode(txnDetailElement, BDPdfConstants.INTEREST,  
                    NumberRender.formatByType(theItem.getInterest(), BDConstants.NO_RULE, RenderConstants.DECIMAL_TYPE));
            doc.appendTextNode(txnDetailElement, BDPdfConstants.PRINCIPAL,  
                    NumberRender.formatByType(theItem.getPrincipal(), BDConstants.NO_RULE, RenderConstants.DECIMAL_TYPE));
        }
    }
    
    /**
     * To format the date
     * @param String inquiryDate
     * @return String inquiryDate
     */
    private String getFormatInquiryDate(String inquiryDate){

    	if(StringUtils.isNotEmpty(inquiryDate) && inquiryDate.contains("/")){
    		return StringUtils.remove(inquiryDate, "/");
    	}
    	return inquiryDate;
    }
    
    /** This code has been changed and added  to 
	 /	Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
	
	// don't want excel to think the , is the next field
	private String escapeField(String field) {
		if(field.indexOf(",") != -1 ) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else 	{
			return field;
		}
	}
}
























































































































































































































































































































































































































































































































































































































































































































































































































































































