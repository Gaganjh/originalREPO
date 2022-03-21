package com.manulife.pension.bd.web.bob.transaction;

import static com.manulife.pension.platform.web.CommonConstants.REPORT_BEAN;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.DateRender;

/**
 * This action class is used to retrieve the participant transaction history
 * data and use the loan number from it in order to redirect to the
 * LoanRepaymentDetailsReport page.
 * 
 * This action does not have corresponding jsp pages.
 * 
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"linkToLoanRepaymentDetailsReportForm"})

public class LinkToLoanRepaymentDetailsReportController extends BDReportController {
	@ModelAttribute("linkToLoanRepaymentDetailsReportForm") 
	public LinkToLoanRepaymentDetailsReportForm populateForm() 
	{
		return new LinkToLoanRepaymentDetailsReportForm();
		}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","/transaction/transactionHistoryReport.jsp");
		forwards.put("input","/transaction/transactionHistoryReport.jsp");
		}

	private static final String DEFAULT_SORT_FIELD = TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static final String LOAN_REPAYMENT_DETAILS_PATH = "/do/bob/transaction/loanRepaymentDetailsReport/?task=filter&";
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			LinkToLoanRepaymentDetailsReportForm.DATE_ENTRY_FORMAT);

	private static Logger logger = Logger
			.getLogger(LinkToLoanRepaymentDetailsReportController.class);

	/**
	 * Constructor.
	 */
	public LinkToLoanRepaymentDetailsReportController() {
		super(LinkToLoanRepaymentDetailsReportController.class);
	}

	/**
	 * @see BDReportController#doCommon()
	 */
	
	public String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		
		super.doCommon( reportForm, request, response);

		LinkToLoanRepaymentDetailsReportForm form = (LinkToLoanRepaymentDetailsReportForm) reportForm;
		String transactionNumber = form.getTransactionNumber();

		
		TransactionHistoryReportData report = (TransactionHistoryReportData) request
				.getAttribute(REPORT_BEAN);
		

		if (report != null) {
			Collection<TransactionHistoryItem> details = report.getDetails();
			if (details != null && details.size() > 0) {
				for (Iterator<TransactionHistoryItem> iter = details.iterator(); iter
						.hasNext();) {
					TransactionHistoryItem entry = iter.next();
					if (entry.getTransactionNumber().equalsIgnoreCase(
							transactionNumber)) {
						if (logger.isDebugEnabled()) {
							logger.debug("found a match for transaction number "
									+ transactionNumber);
						}
						/*
						 * retrieve loan number from typeDescription1, it has a
						 * # sign before the loan number
						 */
						String description = entry.getTypeDescription1();
						String loanNumber = description.substring(
								description.lastIndexOf("#") + 1).trim();

						if (loanNumber.length() > 0) {
							try {
								response.sendRedirect(getLoanRepaymentDetailsURL(
										request, loanNumber,
										form.getParticipantId()));
							} catch (IOException ioException) {
								throw new SystemException(ioException,
										"Failed to get the loan number for transaction "
												+ transactionNumber);
							}

						} else {
							throw new SystemException(null,
									"Failed to get the loan number for transaction "
											+ transactionNumber);
						}
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon ");
		}
		return null;
	}

	/**
	 * @see BaseReportController#createReportCriteria()
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		LinkToLoanRepaymentDetailsReportForm theForm = (LinkToLoanRepaymentDetailsReportForm) form;

		String participantId = theForm.getParticipantId();
		AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();
		/*
		 * common log 78460 lookup profileId by participant id and contract
		 * number
		 */
		String profileId = asd.getProfileIdByParticipantIdAndContractNumber(
				participantId, Integer.toString(getBobContext(request)
						.getCurrentContract().getContractNumber()));

		if (profileId == null) {
			throw new SystemException(null,
					"Failed to get the profileId for prtId " + participantId);
		}
		theForm.setProfileId(profileId);

		criteria.addFilter(TransactionHistoryReportData.FILTER_PROFILE_ID,
				theForm.getProfileId());

		criteria.addFilter(TransactionHistoryReportData.FILTER_CONTRACT_NUMBER,
				theForm.getContractNumber());

		Date transactionDate = null;
		/*
		 * reformat from yyyy-MM-dd to MM/dd/yyyy
		 */
		String transactionDateString = DateRender.formatByPattern(
				theForm.getTransactionDate(), "",
				LinkToLoanRepaymentDetailsReportForm.DATE_IN_PATTERN,
				LinkToLoanRepaymentDetailsReportForm.DATE_ENTRY_FORMAT);

		try {
			synchronized (DATE_FORMATTER) {
				transactionDate = DATE_FORMATTER.parse(transactionDateString);
			}
		} catch (Exception e) {
			throw new SystemException(e,
					"Failed to format transactionDate for prtId "
							+ participantId + " trasnaction number "
							+ theForm.getTransactionNumber());
		}
		criteria.addFilter(TransactionHistoryReportData.FILTER_FROM_DATE,
				transactionDate);
		criteria.addFilter(TransactionHistoryReportData.FILTER_TO_DATE,
				transactionDate);
		/*
		 * this is not used, set as trasnactionDate to satisfy server
		 * requirement
		 */
		criteria.addFilter(TransactionHistoryReportData.FILTER_AS_OF_DATE,
				transactionDate);

		List<String> types = new ArrayList<String>();
		types.add(theForm.getTransactionType());
		criteria.addFilter(TransactionHistoryReportData.FILTER_TYPE_LIST, types);
		criteria.addFilter(TransactionHistoryReportData.APPLICATION_ID,
				theForm.getApplication());

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
	 * @see BaseReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
	 * @see BaseReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	/**
	 * @see BaseReportController#getDownloadData()
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		/*
		 * There are no reports to down load for this action
		 */
		return null;
	}

	/**
	 * @see BaseReportController#getReportId()
	 */
	protected String getReportId() {
		return TransactionHistoryReportData.REPORT_ID;
	}

	/**
	 * @see BaseReportController#getReportName()
	 */
	protected String getReportName() {
		/*
		 * there are are no reports for this action
		 */
		return "";
	}

	/**
	 * @see BaseReportController#populateReportForm()
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		LinkToLoanRepaymentDetailsReportForm theForm = (LinkToLoanRepaymentDetailsReportForm) reportForm;
		Contract currentContract = getBobContext(request).getCurrentContract();
		theForm.setContractNumber(String.valueOf(currentContract
				.getContractNumber()));
		theForm.setTransactionNumber(request
				.getParameter(LinkToLoanRepaymentDetailsReportForm.PARAMETER_KEY_TRANSACTION_NUMBER));
		theForm.setParticipantId(request
				.getParameter(LinkToLoanRepaymentDetailsReportForm.PARAMETER_KEY_PRTID));
		theForm.setTransactionDate(request
				.getParameter(LinkToLoanRepaymentDetailsReportForm.PARAMETER_KEY_TRANSACTION_DATE));
		theForm.setTransactionType(request
				.getParameter(LinkToLoanRepaymentDetailsReportForm.PARAMETER_KEY_TRANSACTION_TYPE));

		if (logger.isDebugEnabled()) {
			logger.debug("parameters[ "
					+ LinkToLoanRepaymentDetailsReportForm.PARAMETER_KEY_CONTRACT_NUMBER
					+ " "
					+ theForm.getContractNumber()
					+ LinkToLoanRepaymentDetailsReportForm.PARAMETER_KEY_TRANSACTION_NUMBER
					+ " "
					+ theForm.getTransactionNumber()
					+ LinkToLoanRepaymentDetailsReportForm.PARAMETER_KEY_PRTID
					+ " "
					+ theForm.getParticipantId()
					+ LinkToLoanRepaymentDetailsReportForm.PARAMETER_KEY_TRANSACTION_DATE
					+ " "
					+ theForm.getTransactionDate()
					+ LinkToLoanRepaymentDetailsReportForm.PARAMETER_KEY_TRANSACTION_TYPE
					+ " " + theForm.getTransactionType() + "]");
		}
	}

	/**
	 * returns the loan re-payment details url.
	 * 
	 * @param request
	 * @param loanNumber
	 * @param participantId
	 * @return String
	 */
	private String getLoanRepaymentDetailsURL(HttpServletRequest request,
			String loanNumber, String participantId) {

		String url = null;
		url = LOAN_REPAYMENT_DETAILS_PATH + "loanNumber=" + loanNumber
				+ "&participantId=" + participantId;
		return url;
	}
	@RequestMapping(value ="/transaction/linkToLoanRepaymentDetailsHistoryReport/", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("linkToLoanRepaymentDetailsReportForm") LinkToLoanRepaymentDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		String forward=super.doDefault( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value = "/transaction/linkToLoanRepaymentDetailsHistoryReport/", params = "task=filter", method ={RequestMethod.POST,RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("linkToLoanRepaymentDetailsReportForm") LinkToLoanRepaymentDetailsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/transaction/linkToLoanRepaymentDetailsHistoryReport/", params ="task=page", method = {RequestMethod.POST,RequestMethod.GET} )
	public String doPage(@Valid @ModelAttribute("linkToLoanRepaymentDetailsReportForm") LinkToLoanRepaymentDetailsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/transaction/linkToLoanRepaymentDetailsHistoryReport/", params ="task=sort", method = RequestMethod.GET)
	public String doSort(@Valid @ModelAttribute("linkToLoanRepaymentDetailsReportForm") LinkToLoanRepaymentDetailsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/transaction/linkToLoanRepaymentDetailsHistoryReport/", params ="task=download", method =RequestMethod.GET)
	public String doDownload(@Valid @ModelAttribute("linkToLoanRepaymentDetailsReportForm") LinkToLoanRepaymentDetailsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/transaction/linkToLoanRepaymentDetailsHistoryReport/",params ="task=downloadAll", method = RequestMethod.GET)
	public String doDownloadAll(@Valid @ModelAttribute("linkToLoanRepaymentDetailsReportForm") LinkToLoanRepaymentDetailsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("ContactXSS");
			}
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}		
	

	/** This code has been changed and added  to 
  	 Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
  	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}

	
}
