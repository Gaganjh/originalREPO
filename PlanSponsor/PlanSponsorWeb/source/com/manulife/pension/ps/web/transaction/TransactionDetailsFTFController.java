package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFTFReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.StringUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

@Controller
@RequestMapping( value = "/transaction")
@SessionAttributes({"fundToFundTransactionReportForm"})

public class TransactionDetailsFTFController extends ReportController {
		
	@ModelAttribute("fundToFundTransactionReportForm")
	public  TransactionDetailsFTFForm populateForm()
	{
		return new  TransactionDetailsFTFForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/fundToFundTransactionReport.jsp");
		forwards.put("default","/transaction/fundToFundTransactionReport.jsp");
		forwards.put("sort","/transaction/fundToFundTransactionReport.jsp");
		forwards.put("page","/transaction/fundToFundTransactionReport.jsp"); 
		forwards.put("print","/transaction/fundToFundTransactionReport.jsp");
		forwards.put("filter","/transaction/fundToFundTransactionReport.jsp");
		forwards.put("fundtofund","/transaction/fundToFundTransactionReport/");
		}

	
	private static final String NUMBER_FORMAT_PATTERN = "########0.00";
	private static final String DEFAULT_VALUE_ZERO = "0.00";

	private static final DecimalFormat twoDecimals = new DecimalFormat("0.00");
	
	//synchronized method to avoid race condition. 
	public static synchronized String formatPercentageFormatter(Double value) { 
        return twoDecimals.format(value); 
    }
	
	protected String getDefaultSort() {
		return TransactionDetailsFTFReportData.SORT_FIELD_WEBSRTNO;
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}
	
		
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm, HttpServletRequest request) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		
		TransactionDetailsFTFForm form = (TransactionDetailsFTFForm)actionForm;

		String transactionNumber = (String)request.getParameter("transactionNumber");
		if (transactionNumber == null || transactionNumber.equals("")) {
			transactionNumber = form.getTransactionNumber();
		}
		
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		String contractNumber = String.valueOf(currentContract.getContractNumber());
		if (contractNumber == null || contractNumber.equals("") || contractNumber.equals("0")) {
			contractNumber = form.getContractNumber();
		}
		
		String participantId = (String)request.getParameter("pptId"); // new reference form.
		if (participantId == null || participantId.equals("")) {
			participantId = (String)request.getParameter("participantId"); // do legacy check incase a link was missed 
		if (participantId == null || participantId.equals("")) {
				participantId = form.getPptId();
			}			
		}
		
		if (userProfile.getCurrentContract().isDefinedBenefitContract()) {
			criteria.addFilter(TransactionDetailsFTFReportData.CONTRACT_TYPE_DB, Boolean.TRUE);
		}
		
		criteria.addFilter(TransactionDetailsFTFReportData.FILTER_TRANSACTION_NUMBER, transactionNumber);
		criteria.addFilter(TransactionDetailsFTFReportData.FILTER_PARTICIPANT_ID, participantId);
		criteria.addFilter(TransactionDetailsFTFReportData.FILTER_CONTRACT_NUMBER, contractNumber);
		

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}		
	}

	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm actionForm, HttpServletRequest request) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		// default sort is risk category			
		TransactionDetailsFTFForm form = (TransactionDetailsFTFForm) actionForm;
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();
		
		criteria.insertSort(sortField, sortDirection);
		
		// add additional sort criteria websrtno and monty type description
		criteria.insertSort(TransactionDetailsFTFReportData.SORT_FIELD_WEBSRTNO, ReportSort.ASC_DIRECTION);
		criteria.insertSort(TransactionDetailsFTFReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION, ReportSort.ASC_DIRECTION);
								
		
								
		if (logger.isDebugEnabled()) {
			logger.debug("populateSortCriteria: inserting sort with field:"+sortField+" and direction: " + sortDirection);
		}
	}
	
	
	public TransactionDetailsFTFController() {
		super(TransactionDetailsFTFController.class);
	}
	
	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TransactionDetailsFTFReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return TransactionDetailsFTFReportData.REPORT_NAME;
	}

	 
	public String doCommon(BaseReportForm form, HttpServletRequest request, HttpServletResponse response) 
	throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		
		String forward=null;
		forward = super.doCommon( form, request, response);
		TransactionDetailsFTFReportData report = (TransactionDetailsFTFReportData)request.getAttribute(Constants.REPORT_BEAN);				
		
		//In case of Errors, report can be null.
		if ((report)!=null && (report.getDetails().size() != 0)) {
			//form.setReport(report);
			//form.setTransactionNumber(report.getTransactionNumber());
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon");
		}
						
		return forward;
	}
		
	
	
	 @RequestMapping(value ="/fundToFundTransactionReport/" ,params={"task=filter"}  , method =  {RequestMethod.GET}) 
	    public String doFilter (@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
	    	if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		              ControllerForward forward = new ControllerForward("refresh",
		       				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
		       		return "redirect:" + forward.getPath();
		       }
			}
		       String forward=super.doFilter( form, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	    
	@RequestMapping(value = "/fundToFundTransactionReport/", params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
	     				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
	     		return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	    	
	@RequestMapping(value = "/fundToFundTransactionReport/", params = { "task=sort" }, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
	     				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
	     		return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value = "/fundToFundTransactionReport/", params = "task=download" , method = RequestMethod.GET)
	public String doDownload(@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
	     				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
	     		return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	    	
	@RequestMapping(value = "/fundToFundTransactionReport/", params = { "task=dowanloadAll" }, method = {
			RequestMethod.GET })
	public String doDownloadAll(
			@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
	     				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
	     		return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}	
		
	@RequestMapping(value = "/fundToFundTransactionReport/", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
	     				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
	     		return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	} 	

	@RequestMapping(value = "/fundToFundTransactionReport/", params = "task=print", method = RequestMethod.GET)
	public String doPrint(
			@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
	     				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
	     		return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doPrint(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);

	}
	
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		
		TransactionDetailsFTFReportData theReport = (TransactionDetailsFTFReportData)report;
		UserProfile userProfile = getUserProfile(request);
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> getDownloadData");
		// get the content objects
		StringBuffer buffer = new StringBuffer();

		// Title
		buffer.append("Transaction details").append(LINE_BREAK+LINE_BREAK);
		buffer.append("Fund to fund transfer").append(LINE_BREAK+LINE_BREAK);
		

	    Contract currentContract = getUserProfile(request).getCurrentContract();
	    buffer.append("Contract number:").append(COMMA).append(currentContract.getContractNumber());
	    buffer.append(LINE_BREAK);
	    buffer.append("Company name:").append(COMMA).append(currentContract.getCompanyName());
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		
		buffer.append("Fund to fund transfer summary:").append(LINE_BREAK);
		buffer.append("Transaction type:").append(COMMA).append("Inter-account transfer - Fund to fund transfer").append(LINE_BREAK);
		
		if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) {
		buffer.append("Name:").append(COMMA).append(theReport.getParticipantName()).append(LINE_BREAK);
		//SSE024, mask ssn if no download report full ssn permission
        boolean maskSSN = true;
		try{
        	maskSSN =ReportDownloadHelper.isMaskedSsn(userProfile, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }		
		
		//buffer.append("SSN:").append(COMMA).append(theReport.getParticipantSSN()).append(LINE_BREAK);
		buffer.append("SSN:").append(COMMA).append(SSNRender.format(theReport.getParticipantUnmaskedSSN(), "",maskSSN)).append(LINE_BREAK);
		}
		
		buffer.append("Invested date:").append(COMMA).append(
				DateRender.format(theReport.getTransactionDate(),RenderConstants.MEDIUM_YMD_SLASHED))
				.append(LINE_BREAK);
		buffer.append("Request date:").append(COMMA).append(
				DateRender.format(theReport.getRequestDate(),RenderConstants.MEDIUM_YMD_SLASHED))
				.append(LINE_BREAK);
		buffer.append("Total amount:").append(COMMA).append(theReport.getTotalAmount()).append(LINE_BREAK);
		buffer.append("Transaction number:").append(COMMA).append(theReport.getTransactionNumber()).append(LINE_BREAK);
		buffer.append("Submission method:").append(COMMA).append(theReport.getMediaCode()).append(LINE_BREAK);
		buffer.append("Source of transfer:").append(COMMA).append(theReport.getSourceOfTransfer()).append(LINE_BREAK);

		// Titles - Transferred from section
		buffer.append(LINE_BREAK).append(LINE_BREAK);
		buffer.append("Transferred from:").append(LINE_BREAK);
		buffer.append(" ").append(COMMA);		
		buffer.append("Risk category:").append(COMMA);		
		buffer.append("Investment option:").append(COMMA);
		if (theReport.doFromMoneyTypesExist()) {
			buffer.append("Money Type:").append(COMMA);
		}
		buffer.append("Amount ($):").append(COMMA);
		buffer.append("% Out:").append(LINE_BREAK).append(LINE_BREAK);
		
		List fromList = theReport.getTransferFroms();
		Iterator it = fromList.iterator();
		while (it.hasNext()) {
			FundGroup category = (FundGroup)it.next();;
			if (category != null) {
				Object o[] = category.getFunds();
				for (int i=0; i < o.length; i++) {
					buffer.append(" ").append(COMMA);		
					buffer.append(category.getGroupName()).append(COMMA);
					TransactionDetailsFund fund = (TransactionDetailsFund)o[i];
					if (fund != null) {
						buffer.append(fund.getName()).append(COMMA);
						if (theReport.doFromMoneyTypesExist()) {
							buffer.append(fund.getMoneyTypeDescription()).append(COMMA);
						}
						buffer.append(fund.getAmount()).append(COMMA);
						BigDecimal percent = fund.getPercentage();
						percent.setScale(2, BigDecimal.ROUND_HALF_UP);
						String pct = formatPercentageFormatter(percent.doubleValue());
						buffer.append(pct);
					}
					buffer.append(LINE_BREAK);
				}
			}
		}
		// Total line
		buffer.append(COMMA).append(COMMA).append("Total:").append(COMMA);
		if (theReport.doFromMoneyTypesExist()) {
			buffer.append(COMMA);
		}		
		buffer.append(theReport.getTotalFromAmount());
		
		// Titles - Transferred to section
		buffer.append(LINE_BREAK).append(LINE_BREAK);
		buffer.append("Transferred to:").append(LINE_BREAK);
		buffer.append(" ").append(COMMA);		
		buffer.append("Risk category:").append(COMMA);		
		buffer.append("Investment option:").append(COMMA);
		buffer.append("Amount ($):").append(COMMA);
		buffer.append("% In:").append(LINE_BREAK).append(LINE_BREAK);
		
		List toList = theReport.getTransferTos();
		it = toList.iterator();
		while (it.hasNext()) {
			FundGroup category = (FundGroup)it.next();;
			if (category != null) {
				Object o[] = category.getFunds();
				for (int i=0; i < o.length; i++) {
					buffer.append(" ").append(COMMA);		
					buffer.append(category.getGroupName()).append(COMMA);
					TransactionDetailsFund fund = (TransactionDetailsFund)o[i];
					if (fund != null) {
						buffer.append(fund.getName()).append(COMMA);
						buffer.append(fund.getAmount()).append(COMMA);
						BigDecimal percent = fund.getPercentage();
						percent.setScale(2, BigDecimal.ROUND_HALF_UP);
						String pct = formatPercentageFormatter(percent.doubleValue());
						buffer.append(pct);
					}
					buffer.append(LINE_BREAK);
				}	
			}
		} 
		// Total line
		buffer.append(COMMA).append(COMMA).append("Total:").append(COMMA);
		buffer.append(theReport.getTotalToAmount()).append(COMMA);
		
		BigDecimal percent = theReport.getTotalToPct();
		percent.setScale(2, BigDecimal.ROUND_HALF_UP);
		String pct = formatPercentageFormatter(percent.doubleValue());

		buffer.append(pct).append(LINE_BREAK);


		// Insert Redemption Fees / MVA as needed
		if (theReport.getRedemptionFees().doubleValue() > (double)0.0) {
			buffer.append(LINE_BREAK);
			String message = getMiscContent(ContentConstants.MESSAGE_REDEMPTION_FEE_APPLED, theReport.getRedemptionFees().abs().negate());
			buffer.append(message);
		}
		if (theReport.getMva().doubleValue() > (double)0.0) {
			buffer.append(LINE_BREAK);
			String message = getMiscContent(ContentConstants.MESSAGE_MVA_APPLIED, theReport.getMva().abs().negate());
			buffer.append(message);
		}
		
		// Titles - Details section
		buffer.append(LINE_BREAK).append(LINE_BREAK);
		buffer.append("Transfer Details:").append(LINE_BREAK);
		buffer.append(" ").append(COMMA);		
		buffer.append("Risk category:").append(COMMA);		
		buffer.append("Investment option:").append(COMMA);
		buffer.append("Money Type:").append(COMMA);
		buffer.append("Amount ($):").append(COMMA);
		buffer.append("Unit value:").append(COMMA);
		buffer.append("Number of units:").append(COMMA);
		buffer.append("Comments:").append(LINE_BREAK).append(LINE_BREAK);
		
		Collection detailsList = theReport.getDetails();
		it = detailsList.iterator();
		while (it.hasNext()) {
			FundGroup category = (FundGroup)it.next();;
			if (category != null) {
				Object o[] = category.getFunds();
				for (int i=0; i < o.length; i++) {
					buffer.append(" ").append(COMMA);		
					buffer.append(category.getGroupName()).append(COMMA);
					TransactionDetailsFund fund = (TransactionDetailsFund)o[i];
					if (fund != null) {
						buffer.append(fund.getName()).append(COMMA);
						buffer.append(fund.getMoneyTypeDescription()).append(COMMA);
						buffer.append(fund.getAmount()).append(COMMA);
						if (fund.getUnitValue().doubleValue() == (double)0.0) {
							buffer.append(" ").append(COMMA);
						} else {
							buffer.append(fund.getDisplayUnitValue()).append(COMMA);
						}
						if (fund.getNumberOfUnits().doubleValue() == (double)0.0) {
							buffer.append(" ").append(COMMA);
						} else {
							buffer.append(fund.getNumberOfUnits()).append(COMMA);
						}
						buffer.append(fund.getComments());
					}
					buffer.append(LINE_BREAK);
				}
			}
		}
		

		if (logger.isDebugEnabled())
			logger.debug("exit <- getDownloadData");

		return buffer.toString().getBytes();
	}

	/**
	 * @param message_redemption_fee_appled
	 * @return
	 */
	public static String getMiscContent(int contentId, BigDecimal amount) {
		String text = "";
		try {
        	ContentType miscType = ContentTypeManager.instance().valueOf(ContentConstants.TYPE_MISCELLANEOUS);
            if (miscType != null) {
                Content content = ContentCacheManager.getInstance().getContentById(contentId, miscType);
                if (content != null) {
                	// Format amount
            		String contentParams [] = new String[1];
            		contentParams [0] = NumberRender.formatByPattern(amount, DEFAULT_VALUE_ZERO, NUMBER_FORMAT_PATTERN );
            		// Substitute content
            		Miscellaneous mscBean = (Miscellaneous) content;
        			text = StringUtility.substituteParams(mscBean.getText(), (Object[])contentParams);        	
            	}
            }
        } catch(Exception exception) {
        	logger.error(exception);
        }
		return text;
	}
	
	/** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder	
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}
