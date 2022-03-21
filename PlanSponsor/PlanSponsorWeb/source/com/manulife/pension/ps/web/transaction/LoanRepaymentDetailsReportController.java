package com.manulife.pension.ps.web.transaction;

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
import javax.validation.Valid;

import org.apache.commons.lang.ObjectUtils;
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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.ps.service.delegate.PartyServiceDelegateAdaptor;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentDetailsItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentDetailsReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.ps.web.withdrawal.WebConstants;
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
 * LoanRepaymentDetailsReportAction PsAction class This class is used to get the
 * information for the Loan Repayment Details page
 * 
 * @author drotele
 */
@Controller
@RequestMapping(value ="/transaction")
@SessionAttributes({"loanRepaymentDetailsReportForm"})

public class LoanRepaymentDetailsReportController extends PsController {
	
	
	@ModelAttribute("loanRepaymentDetailsReportForm")
	public LoanRepaymentDetailsReportForm populateForm() {
		return new LoanRepaymentDetailsReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/transaction/loanRepaymentDetailsReport.jsp");
		forwards.put("loanRepaymentDetailsPage","/transaction/loanRepaymentDetailsReport.jsp");
	}

	private static Logger logger = Logger.getLogger(LoanRepaymentDetailsReportController.class);

	private static final String LOAN_REPAYMENT_DETAILS_PAGE = "loanRepaymentDetailsPage";

	// CSV Export Related
	private static final String DOWNLOAD_COLUMN_HEADING_LOAN_REPAYMENTS = "Date,Activity,Amount ($),Principal ($),Interest ($),Loan balance ($)";

	private static final String COMMA = ReportController.COMMA;

	private static final String LINE_BREAK = ReportController.LINE_BREAK;

	private static final String REPORT_ID = "LoanRepaymentDetailsReport";

	private static final String SSN_DEFAULT_VALUE = "000000000";

	private static final String NUMBER_FORMAT_PATTERN = "########0.00";

	private static final String DEFAULT_VALUE_ZERO = "0.00";
	
	private static final String AMOUNT_FORMAT = "########0.00";
	
	private static final String PERCENT = "%";

	/**
	 * constructor
	 */
	public LoanRepaymentDetailsReportController() {
		super(LoanRepaymentDetailsReportController.class);
	}

	private String getTask(HttpServletRequest request) {
		String task = request.getParameter("task");
		if (task == null || task.length() == 0) {
			task = AutoForm.DEFAULT;
		}
		return task;
	}

	@RequestMapping(value={"/loanRepaymentDetailsReport/","/tpaLoanRepaymentDetailsReport/"}, method =  {RequestMethod.GET,RequestMethod.POST})
	public String doExecute(@Valid @ModelAttribute("loanRepaymentDetailsReportForm") LoanRepaymentDetailsReportForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) throws SystemException, ServletException, IOException {
		
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
		
		int contractNumber = 0;
		String profileId = null;
		LoanRepaymentDetailsReportData reportData = null;
		LoanRepaymentDetailsReportForm reportForm = null;

		try {
			// get the contract number
			UserProfile userProfile = getUserProfile(request);

			String contractId = request.getParameter("contractId");
			if(contractId != null && contractId.length() > 0) {
				contractNumber = Integer.parseInt(contractId);
			} else if (userProfile != null) {
				contractNumber = userProfile.getCurrentContract()
						.getContractNumber();
			}

			// get other input parameters for the delegate
			reportForm = (LoanRepaymentDetailsReportForm) form;

			// get the ProfileId
			profileId = retrieveProfileId(request, reportForm);

			// populate SSN and UserName
			retrieveNameAndSSN(reportForm, contractNumber, profileId);

			// get the LoanHoldings
			LoanHoldings loanHoldings = getParticipantLoanList(contractNumber,
					profileId);

			// check the AccountService Return codes and status
			if (loanHoldings != null) {
				// get the system status
				reportData = getParticipantLoanDetails(reportForm, loanHoldings);
				// check for the Download action
				if ("download".equals(getTask(request))) {
					return doDownload(reportData, request, response);
				}
			}
		} catch (SystemUnavailableException e) {
			List errors = new ArrayList();
			errors.add(new GenericException(ErrorCodes.OUT_OF_SERVICE_HOURS));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get("input");
		} catch (SystemException e) {
			// Log the system exception.
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);

			// Show user friendly message.
			List errors = new ArrayList();
			errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get("input");
		}
		// set the LoanRepayment details as an attribute
		if (reportData == null) {
			// create an empty one if it doesn't exist
			reportData = new LoanRepaymentDetailsReportData();
		}
		// set the report data attribute
		request.setAttribute(Constants.LOAN_REPAYMENT_DETAILS_REPORT_DATA,
				reportData);
		// forward to the loan repayment page
		return forwards.get(LOAN_REPAYMENT_DETAILS_PAGE);

	}

	/**
	 * Retrieves SSN and User Name if it doesn't exist in the form
	 * 
	 * @param reportForm
	 */
	private void retrieveNameAndSSN(LoanRepaymentDetailsReportForm reportForm,
			int contractNumber, String profileId) throws SystemException {

		// retrieve and mask SSN

		// get SSN and (or) Name using ezk's PartyService
		GeneralInformationValueObject giVO = null;
		PartyServiceDelegateAdaptor psd = new PartyServiceDelegateAdaptor();
		giVO = psd.getGeneralInformation(null, profileId, String
				.valueOf(contractNumber));
		if (giVO != null) {
			// get SSN
			if (giVO.getSsn() != null) {
				reportForm.setSsn(giVO.getSsn());
				reportForm.setMaskedSsn(SSNRender.format(giVO.getSsn(),
						SSN_DEFAULT_VALUE, true));
			}
			// Get name
			if (giVO.getFirstname() != null && giVO.getLastname() != null) {
				reportForm.setName(giVO.getLastname() + ", "
						+ giVO.getFirstname());
			}
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

		String participantId = (String) ObjectUtils.defaultIfNull(request
				.getParameter("participantId"), "");
		String profileId = reportForm.getProfileId();

		if (participantId.length() > 0) {
			AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();
            // common log 78460 lookup profileId by particiapnt id and contract number
            profileId = asd.getProfileIdByParticipantIdAndContractNumber(
                    participantId, Integer.toString(getUserProfile(request).getCurrentContract().getContractNumber()));
			reportForm.setProfileId(profileId);
		}

		if (profileId == null || profileId.length() == 0) {
			StringBuffer message = new StringBuffer(
					"Cannot retrieve profile ID: ");
			if (request.getParameter("participantId") == null) {
				message
						.append(" participantId is null and reportForm.getProfileId() returns ["
								+ reportForm.getProfileId() + "]");
			} else {
				message.append(" participantId is [" + participantId + "]");
			}
			throw new SystemException(new IllegalArgumentException(),
					getClass().getName(), "retrieveProfileId", message
							.toString());
		}

		return profileId;
	}

	/**
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
	 * @param form
	 * @param loanHoldings
	 * @return LoanRepaymentDetailsReportData VO
	 */
	private LoanRepaymentDetailsReportData getParticipantLoanDetails(
			LoanRepaymentDetailsReportForm form, LoanHoldings loanHoldings) {

		LoanInformation loanInformation = null;
		int loanNumber = -1; // in case form doesn't pass any

		// get the loan number from the form
		if (form.getLoanNumber() != null && !("".equals(form.getLoanNumber()))) {
			loanNumber = Integer.parseInt(form.getLoanNumber());
		}

		// retrive loans from the loanHoldings VO
		LoanInformation[] loans = loanHoldings.getLoans();
		if (loans != null) {
			// get the particular loan
			for (int i = 0; i < loans.length; i++) {
				loanInformation = loans[i];
				if (loanInformation.getNumber() == loanNumber) {
					break;
				}
			}
		}
		return populateValueObject(form, loanHoldings, loanInformation);
	}

	/**
	 * @param form
	 * @param loanHoldings
	 * @param loanInfo
	 * @return LoanRepaymentDetailsReportData VO
	 */
	private LoanRepaymentDetailsReportData populateValueObject(
			LoanRepaymentDetailsReportForm form, LoanHoldings loanHoldings,
			LoanInformation loanInfo) {

		//		System.out.println("in populateValueObject");

		LoanRepaymentDetailsReportData reportData = new LoanRepaymentDetailsReportData();

		// populate the header
		reportData.setMaskedSsn(form.getMaskedSsn());
		reportData.setSsn(form.getSsn());
		reportData.setName(form.getName());
		reportData.setReturnCode(loanHoldings.getReturnCode());
		reportData.setInquiryDate(loanHoldings.getInquiryDate());

		// loan exists
		if (loanInfo != null) {
			reportData.setNumber(loanInfo.getNumber());
			reportData.setStatus(loanInfo.getStatus());
			reportData.setTransferDate(loanInfo.getTransferDate());
			reportData.setTransferAmount(loanInfo.getTransferAmount());
			reportData.setTransferRate(loanInfo.getTransferRate());
			reportData.setMaturityDate(loanInfo.getMaturityDate());
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
					else{
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
				for (int i = 0; i < sizeOfActivities; i++) {
					LoanRepaymentDetailsItem item = new LoanRepaymentDetailsItem();
					item.setDate(loanInfo.getActivities()[i].getDate());
					item.setType(loanInfo.getActivities()[i].getType());
					BigDecimal expenseMargin = loanInfo.getActivities()[i]
							.getExpenseMargin();
					item.setExpenseMargin(expenseMargin);

					if (Constants.LOAN_ACTIVITY_REPAYMENT.equals(loanInfo
							.getActivities()[i].getType())) {
						item
								.setTypeDesc(Constants.LOAN_ACTIVITY_REPAYMENT_DESC);

						item.setAmount(loanInfo.getActivities()[i].getAmount());
						item.setPrincipal(loanInfo.getActivities()[i]
								.getPrincipal());
						BigDecimal interest = loanInfo.getActivities()[i]
								.getInterest();

						if (expenseMargin != null) {
							item.setInterest(interest.add(expenseMargin));
						} else {
							item.setInterest(interest);
						}
					} else if (Constants.LOAN_ACTIVITY_DEFAULTED
							.equals(loanInfo.getActivities()[i].getType())) {
						item
								.setTypeDesc(Constants.LOAN_ACTIVITY_DEFAULTED_DESC);
					} else {
						item.setTypeDesc("");
					}

					item.setBalance(loanInfo.getActivities()[i].getBalance());
					items[sizeOfActivities - i - 1] = item;

					if (Constants.LOAN_ACTIVITY_REPAYMENT
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
			 * Defect 008524: Populate summary fields based on last loan
			 * repayment date/amount found in detail section.
			 */
			if (lastRepaymentItem != null) {
				reportData.setDaysSinceLastPayment(daysBetween(
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
		return reportData;
	}

	private int daysBetween(Date d1, Date d2) {
		int secondsInOneDay = 1000 * 60 * 60 * 24;
		return (int) ((d2.getTime() - d1.getTime()) / secondsInOneDay);
	}

	/**
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

		//System.out.println("in doDownload");

		String reportFileName = REPORT_ID + ReportController.CSV_EXTENSION;
		byte[] downloadData = getDownloadData(request, reportData);
		if (downloadData != null) {
			ReportController.streamDownloadData(request, response, ReportController.CSV_TEXT,
					reportFileName, downloadData);
		}
		// do not iterate with Struts
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

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateDownloadData");
		// get the content objects
		Content message = null;
		StringBuffer buffer = new StringBuffer();
		Contract currentContract = getUserProfile(request).getCurrentContract();
		if(currentContract != null) {
        buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(escapeField(
				currentContract.getCompanyName())).append(LINE_BREAK);
		} else {
			//FIXME is companyName mandatory ? 
	        buffer.append("Contract").append(COMMA).append(
					request.getParameter("contractId")).append(COMMA).append(
					"").append(LINE_BREAK);
		}

		try {

			message = ContentCacheManager.getInstance().getContentById(
					ContentConstants.TRANSACTION_LOAN_REPAYMENT_LAYOUT_PAGE,
					ContentTypeManager.instance().LAYOUT_PAGE);
			LoanGeneralInfoVO generalVO = reportData.getLoanGeneralInfo();
			// Loanholder's information
			buffer.append(
					ContentUtility.getContentAttribute(message, "body1Header")).append(COMMA).append(escapeField(reportData.getName())).append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			buffer.append("Loan #").append(COMMA).append(reportData.getNumber())
					.append(LINE_BREAK);
		
			String beginDateLabel = "Transfer date";
			String loanTypeName = "Transfer Loan";
			String loanReason = "Unspecified";
			if("LI".equalsIgnoreCase(generalVO.getTypeOfLoan())){
				beginDateLabel = "Loan issue date";
				loanTypeName = "Loan Issue";
				
			}
			
			if("PP".equalsIgnoreCase(generalVO.getLoanReason())){
				loanReason = "Purchase of Primary Residence";
			}else if("HD".equalsIgnoreCase(generalVO.getLoanReason())){
				loanReason = "Hardship";
			}else if("GP".equalsIgnoreCase(generalVO.getLoanReason())){
				loanReason = "General Purpose";
			}
			buffer.append("Type of loan").append(COMMA).append(escapeField(loanTypeName)).append(COMMA);
			buffer.append("Loan reason").append(COMMA).append(escapeField(loanReason)).append(LINE_BREAK);
		
			buffer.append("Interest rate" ).append(COMMA).append(NumberRender.formatByPattern(generalVO.getLoanInterestRate(),
					DEFAULT_VALUE_ZERO,
                    AMOUNT_FORMAT)).append(PERCENT).append(COMMA);
			
			buffer.append(beginDateLabel).append(COMMA).append(
					DateRender.format(generalVO.getLoanBeginDate(),
                            RenderConstants.MEDIUM_YMD_SLASHED)).append(LINE_BREAK);
			buffer.append("Loan maturity date" ).append(COMMA).append(
					DateRender.format(generalVO.getLoanMaturityDate(),
                            RenderConstants.MEDIUM_YMD_SLASHED)).append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			/*if("LI".equalsIgnoreCase(generalVO.getTypeOfLoan())){
				buffer.append("Amoritization Period" ).append(COMMA).append( reportData.getAmoritizationPeriod() +" months");
				buffer.append(LINE_BREAK);
			}*/
			if(!reportData.getMoneyTypeFunds().isEmpty())
			{
				buffer.append("Money type" ).append(COMMA).append( "Amount ($)").append(LINE_BREAK);
				Set entrySet = reportData.getMoneyTypeFunds().entrySet();
				Iterator it = entrySet.iterator();
				BigDecimal totalAmt = BigDecimal.ZERO;
				while (it.hasNext()) {
					Map.Entry me = (Map.Entry) it.next();
					totalAmt = totalAmt.add((BigDecimal) me.getValue());
					buffer.append(me.getKey()  ).append(COMMA).append( me.getValue()).append(LINE_BREAK);
				}
				buffer.append("Total" ).append(COMMA).append(totalAmt);
			}
			buffer.append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			buffer.append(
					ContentUtility.getContentAttribute(message, "body2Header")).append(LINE_BREAK);
			//SSE024, mask ssn if no download report full ssn permission
	        boolean maskSSN = true;
			// Outstanding balance / type of loan
			buffer.append(
					"Outstanding balance as of "
							+ DateRender.format(reportData.getInquiryDate(),
									RenderConstants.MEDIUM_YMD_SLASHED)
						).append(COMMA).append(
							 NumberRender.formatByPattern(reportData
									.getOutstandingBalanceAmount(),
									DEFAULT_VALUE_ZERO, NUMBER_FORMAT_PATTERN))
					.append(COMMA);
			// last payment / issue date
			buffer.append(
					"Last payment total amount"
							).append(COMMA).append(
							 NumberRender.formatByPattern(reportData
									.getLastRepaymentAmount(),
									DEFAULT_VALUE_ZERO, NUMBER_FORMAT_PATTERN)
							+ COMMA
							/*+ titleLoanIssue
							+ COMMA
							+ DateRender.format(reportData.getTransferDate(),
									RenderConstants.MEDIUM_YMD_SLASHED))*/
					).append(LINE_BREAK);
			// last payment date / original amount
			Date lastPaymentDate = reportData.getLastRepaymentDate();
			String lastPaymentDateString = "n/a";
			String daysSinceLastPaymentString = "n/a";
			
			if (lastPaymentDate != null) {
				lastPaymentDateString = DateRender.format(lastPaymentDate,
						RenderConstants.MEDIUM_YMD_SLASHED);
				daysSinceLastPaymentString = String.valueOf(reportData
						.getDaysSinceLastPayment());
			}
			
			buffer.append("Date of last payment").append(COMMA).append(
					lastPaymentDateString).append(COMMA);
					

			// since last payment / interest
			buffer.append("Days since last payment").append(COMMA).append(
					daysSinceLastPaymentString).append(COMMA).
					append(LINE_BREAK);
			
			
			
			buffer.append(COMMA).append(LINE_BREAK);
			buffer.append("Repayment details")
					.append(LINE_BREAK);
			buffer.append(DOWNLOAD_COLUMN_HEADING_LOAN_REPAYMENTS).append(
					LINE_BREAK);

			// Loan Repayment Detail Data
			int sizeOfActivities = 0;
			if (reportData.getItems() != null) {
				sizeOfActivities = reportData.getItems().length;
			}
			if (sizeOfActivities > 0) {
				for (int i = 0; i < sizeOfActivities; i++) {
					buffer.append(LINE_BREAK);
					LoanRepaymentDetailsItem item = reportData.getItems()[i];
					String repaymentDate = "";

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
										DEFAULT_VALUE_ZERO,
										NUMBER_FORMAT_PATTERN)).append(COMMA);
					} else {
						buffer.append(COMMA);
					}

					if (item.getPrincipal() != null) {
						buffer.append(
								NumberRender.formatByPattern(item
										.getPrincipal(), DEFAULT_VALUE_ZERO,
										NUMBER_FORMAT_PATTERN)).append(COMMA);
					} else {
						buffer.append(COMMA);
					}

					if (item.getInterest() != null) {
						buffer.append(
								NumberRender.formatByPattern(
										item.getInterest(), DEFAULT_VALUE_ZERO,
										NUMBER_FORMAT_PATTERN)).append(COMMA);
					} else {
						buffer.append(COMMA);
					}

					buffer.append(
							NumberRender.formatByPattern(item.getBalance(),
									DEFAULT_VALUE_ZERO, NUMBER_FORMAT_PATTERN))
							.append(COMMA);
				}
			} else {
				Content noLoanMessage = ContentCacheManager.getInstance()
						.getContentById(
								ContentConstants.MESSAGE_NO_LOAN_REPAYMENTS,
								ContentTypeManager.instance().MESSAGE);
				buffer.append(
						ContentUtility.getContentAttribute(noLoanMessage,
								"text")).append(LINE_BREAK);
			}
		} catch (ContentException e) {
			throw new SystemException(e, getClass().getName(),
					"populateDownloadData", "Something wrong with CMA");
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- populateDownloadData");

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
        final Object lastPage = request.getSession().getAttribute(
                WebConstants.LAST_ACTIVE_PAGE_LOCATION);

        // This operation resets/clears the last active page location.
        final String forwardResult = super.preExecute(form, request, response);

        // Put the previously the existing value back.
        request.getSession().setAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION, lastPage);

        return forwardResult;
    }
    
    /** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
	 */
    @Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
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