package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData;
import com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionParticipant;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractStatementInfoVO;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.StatementPairVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.NumberRender;

/**
 * 
 * @author Tony Tomasone
 *  Modified by Surendra Anem on Oct 1st 2009
 *  	- removed instance variables,"formattedDate" and "trackingNumber" 
 *  	to avoid race condition issues(CL: 102715&102716)
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"viewContributionDetailsForm"})

public class ViewContributionDetailsNewController extends ReportController {
	
	@ModelAttribute("viewContributionDetailsForm") 
	public ViewContributionDetailsForm populateForm() 
	{
		return new ViewContributionDetailsForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/tools/viewContributionDetailsNew.jsp"); 
		forwards.put("tools","redirect:/do/tools/toolsMenu/"); 
		forwards.put("default","/tools/viewContributionDetailsNew.jsp"); 
		forwards.put("sort","/tools/viewContributionDetailsNew.jsp");
		forwards.put("filter","/tools/viewContributionDetailsNew.jsp");
		forwards.put("page","/tools/viewContributionDetailsNew.jsp");
		forwards.put("print","/tools/viewContributionDetailsNew.jsp");
		forwards.put("history","redirect:/do/tools/submissionHistory/");
	}

	private static String DEFAULT_SORT = SubmissionParticipant.SORT_RECORD_NUMBER;
	private static String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	public static final NumberFormat FORMATTER = new DecimalFormat("00");
	public static final Integer ZERO = new Integer(0);
	public static final BigDecimal BIG_ZERO = new BigDecimal(0).setScale(2);
	public static final Integer NINETY_NINE = new Integer(99);
	private static final String DRAFT_STATUS 	= "14";
	private static final String COPIED_STATUS 	= "97";
	protected static final String TOOLS = "tools";
	private static final Date highDate = new GregorianCalendar(9999,Calendar.DECEMBER,31).getTime();
    private static final String HISTORY = "history";
	
	/**
	 * Constructor.
	 */
	public ViewContributionDetailsNewController() {
		super(ViewContributionDetailsNewController.class);
	}

	
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	protected String getReportId() {
		return ContributionDetailsReportData.REPORT_ID;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return ContributionDetailsReportData.REPORT_NAME;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria, com.manulife.pension.ps.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
        // get the user profile object 
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		ViewContributionDetailsForm contributionDetailsForm = (ViewContributionDetailsForm) form;
		
		criteria.addFilter(
				ContributionDetailsReportData.FILTER_FIELD_1,
				new Integer(currentContract.getContractNumber())
		);
		
		// we expect the submission number to be present in the request.
		String subNo = request.getParameter("subNo");
		if (subNo == null || subNo.length() == 0) 
			subNo = contributionDetailsForm.getSubNo();
		if (subNo == null || subNo.length() == 0)
			subNo = (String)request.getSession().getAttribute("subNo");
		
		contributionDetailsForm.setSubNo(subNo);
        request.getSession().setAttribute("subNo", subNo);
        
		criteria.addFilter(
				ContributionDetailsReportData.FILTER_FIELD_2,
				new Integer(contributionDetailsForm.getSubNo())
		);

		contributionDetailsForm.setViewMode(true);


	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#doCommon(org.apache.struts.action.ActionMapping, com.manulife.pension.ps.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	 
	protected String doCommon ( BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		ViewContributionDetailsForm form = (ViewContributionDetailsForm) reportForm;
		// lets check the permissions
		UserProfile userProfile = getUserProfile(request);

		// TODO check if this submission does not belong to this user
		// then they must have view all submissions to see
		// the submission
		// for now confirm that they have submission access
		if(
				userProfile!=null && !userProfile.isInternalUser() && !userProfile.isSubmissionAccess()
		) {
			return forwards.get( TOOLS);
		}
		
		// we expect the submission number to be present in the request.
		String subNo = request.getParameter("subNo");
		if (subNo == null || subNo.length() == 0) 
			subNo = form.getSubNo();
		if (subNo == null || subNo.length() == 0)
			subNo = (String)request.getSession().getAttribute("subNo");
		// if we can't find it, redirect the user to the submission history page
		if (subNo == null) {
			return forwards.get( HISTORY);
		}
		 
		String forward = super.doCommon(reportForm, request, response);
		ContributionDetailsReportData theReport = (ContributionDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
		
				 
		ContributionDetailItem theItem = null;
        
        if (theReport == null) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.SUBMISSION_HAS_NO_VALID_DATA));
            setErrorsInSession(request, errors);
            return forwards.get( HISTORY);
        } else {
            theItem = theReport.getContributionData();
        }
        
        /**
		 * contribution total set into session attribute.	 
		 */
		 ContributionDetailItem contributionItem=theReport.getContributionData();		 
		 BigDecimal totalContribution=contributionItem.getContributionTotal();
		 if(totalContribution!=null){
			 request.getSession().setAttribute("totalContribution", totalContribution);
		 }
		 
        // ensure this user is allowed to view this submission
        if (!userProfile.isAllowedToViewAllSubmissions()) {
			long submitterId = 0;
		    try {
		    	submitterId = Long.parseLong(theItem.getSubmitterID());
		    } catch (NumberFormatException e) {
				return forwards.get( HISTORY);
		    }
		    if (userProfile.getPrincipal().getProfileId() != submitterId) {
		    	return forwards.get( HISTORY);
		    }	
		}

        // ensure the submission is viewable
        SubmissionHistoryItemActionHelper helper = SubmissionHistoryItemActionHelper.getInstance();
		if (!(helper.isViewAllowed(theItem,userProfile))) {
	    	return forwards.get( HISTORY);
	    }	
			
		if (theReport != null && 
			theReport.getContributionData() != null &&
			theReport.getContributionData().getSubmissionPaymentItem() != null) {
			// show the payment section
			form.setDisplayPaymentInstructionSection(true);
		}
		
        form.setTheReport(theReport);
		Contract contract = userProfile.getCurrentContract();
		
		// Get the statement info for the contract
		ContractServiceDelegate contractService = ContractServiceDelegate.getInstance();
		
		ContractStatementInfoVO statementInfoVO 
			= contractService.getContractStatementInfo(contract.getContractNumber());

		if (statementInfoVO.isStatementsOutstanding()) {
			List statementDates = contractService.getStatementDates(contract.getContractNumber());
			for (int i = 0; i < statementDates.size(); i++) {
				StatementPairVO statementPair = (StatementPairVO)statementDates.get(i);
				// if the start date of the statment period is the dummy date, set the start date to 
				// contract effective date
				if (statementPair.getStartDate().equals(highDate)) {
					statementPair.setStartDate(contract.getEffectiveDate());
				}	
			}
			statementInfoVO.setStatementDates(statementDates);
			form.setStatementDates(statementInfoVO.getStatementDates());
		}		
		ContributionDetailItem contributionDetail = theReport.getContributionData();

		// We need to check the lock before verifying if 
		// edit is available below 
		Lock lock = SubmissionServiceDelegate.getInstance().checkLock(contributionDetail,true);
		contributionDetail.setLock(lock);

		// check rules for update button
		if (contributionDetail != null) {
			form.setEditFunctionAvailable(helper.isEditAllowed(contributionDetail, userProfile));
		} else {
			form.setEditFunctionAvailable(false);
		}
		// loans display requirements SP4.1.1.2
		if (
				!contract.isLoanFeature() &&
				(contract.getLoansTotalAmount() == null ||
						contract.getLoansTotalAmount().doubleValue() <= 0d) &&
						contributionDetail.getLoanRepaymentTotal().compareTo(new BigDecimal(0d)) <= 0
		) form.setShowLoans(false);

        ArrayList formContractMoneyTypes = new ArrayList();
        Iterator contractMoneyTypes = theItem.getContractMoneyTypes().iterator();
        while (contractMoneyTypes.hasNext()) {
            MoneyTypeVO moneyType = (MoneyTypeVO)contractMoneyTypes.next();
            formContractMoneyTypes.add(new LabelValueBean(moneyType.getContractShortName().trim(),moneyType.getId().trim()));
        }
        form.setContractMoneyTypes(formContractMoneyTypes);

		return forward; 
	}
	
	@RequestMapping(value ="/viewContributionNew/", method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("viewContributionDetailsForm") ViewContributionDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
	       }
		}
	       String forward=super.doDefault( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	
	@RequestMapping(value ="/viewContributionNew/", params={"task=page"}, method =  {RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("viewContributionDetailsForm") ViewContributionDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
	       }
		}
	       String forward=super.doPage( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/viewContributionNew/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("viewContributionDetailsForm") ViewContributionDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
	       }
		}
	       String forward=super.doSort( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/viewContributionNew/",params={"task=download"}  , method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("viewContributionDetailsForm") ViewContributionDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
	       }
		}
	       String forward=super.doDownload( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }

	
	
	/**
	 * @see ReportController#getPageSize(HttpServletRequest)
	 */
	protected int getPageSize(HttpServletRequest request) {
        UserProfile profile = getUserProfile(request);
        return profile.getPreferences()
                .getInt(UserPreferenceKeys.REPORT_PAGE_SIZE,
                        super.getPageSize(request));
    }

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDownloadData(com.manulife.pension.ps.web.report.BaseReportForm, com.manulife.pension.service.report.valueobject.ReportData, javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		StringBuffer buffer = new StringBuffer();
		ContributionDetailsReportData reportData = 
				(ContributionDetailsReportData) report;
		
		// Fill in the header
		Iterator columnLabels = reportData.getColumnLabels().iterator();
		while (columnLabels.hasNext()) {
			buffer.append(columnLabels.next());
			if (columnLabels.hasNext()) {
				buffer.append(COMMA);
			}
		}

		String formattedDate = reportData.getFormattedDate();
		request.setAttribute(Constants.FORMATTED_DATE, formattedDate);
		StringBuffer emptyContributionBuffer = null;
		
		Iterator items = report.getDetails().iterator();
		while (items.hasNext()) {
			buffer.append(LINE_BREAK);
			buffer.append(reportData.getTransactionNumber()).append(COMMA);
			buffer.append(reportData.getContractNumber()).append(COMMA);
			
			SubmissionParticipant participant = (SubmissionParticipant) items.next();
			buffer.append(participant.getIdentifier()).append(COMMA);
			buffer.append(QUOTE).append(participant.getName()).append(QUOTE).append(COMMA);

			buffer.append(formattedDate);
			if (reportData.getContributionData().getAllocationMoneyTypes() != null 
					&& reportData.getContributionData().getAllocationMoneyTypes().size() > 0) {
				buffer.append(COMMA);
			}

			Map contributions = participant.getMoneyTypeAmounts();
			for (Iterator itr = reportData.getContributionData().getAllocationMoneyTypes().iterator();  itr.hasNext(); ){
				MoneyTypeHeader moneyType = (MoneyTypeHeader)itr.next();
				String moneyTypeKey = moneyType.getKey();
				BigDecimal contributionAmount = (BigDecimal)contributions.get(moneyTypeKey);
				if (null != contributionAmount&& (contributionAmount.compareTo(BIG_ZERO) > 0
						|| contributionAmount.compareTo(BIG_ZERO) < 0)) {
					String displayAmount = NumberRender.formatByPattern(contributionAmount, 
							ZERO_AMOUNT_STRING, "#########.##;-#########.##", 2, BigDecimal.ROUND_UNNECESSARY);
					buffer.append(displayAmount);
				} 
				if (itr.hasNext()) {
					buffer.append(COMMA);
				}
			}	
			
			// Loans (commas pre-pended)
			Iterator loans = participant.getLoanAmounts().keySet().iterator();
			Map loanMap = participant.getLoanAmounts();
			while (loans.hasNext()) {
				String loanKey = (String)loans.next();
				int endIndex = loanKey.indexOf("/");
				Integer loanId = new Integer(loanKey.substring(0, endIndex));
				String formattedLoanId = "";
				if (loanId.compareTo(ZERO) != 0 && loanId.compareTo(NINETY_NINE) != 0) {
					synchronized (FORMATTER) {
						formattedLoanId = FORMATTER.format(loanId.longValue());
					}
				}	
				BigDecimal repaymentAmount = (BigDecimal)loanMap.get(loanKey);
				String displayAmount = "";
				if (null != repaymentAmount && repaymentAmount.compareTo(BIG_ZERO) > 0) {	
					displayAmount = NumberRender.formatByPattern(repaymentAmount, 
							ZERO_AMOUNT_STRING, "#########.##;-#########.##", 2, BigDecimal.ROUND_UNNECESSARY);
				}
				buffer.append(COMMA).append(formattedLoanId).append(COMMA).append(displayAmount);
			}
			
			// Fill-in the rest of the columns until max no. of loans per contract
			int actualLoanCount = participant.getLoanAmounts().size();
			if (actualLoanCount < reportData.getMaxLoanCount()) {
				for (int i = 0; i < (reportData.getMaxLoanCount() - actualLoanCount); i++) {
					buffer.append(COMMA).append(COMMA);
				}
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}
				
		return buffer.toString().getBytes();
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getFileName(javax.servlet.http.HttpServletRequest)
	 */
	protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		UserProfile userProfile = getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		//getting formatted date from request scope and appending as part of file name.
		//to fix common log ids : 102715&102716 
		Object attribute = request.getAttribute(Constants.FORMATTED_DATE);
		String formattedDate = attribute!=null? attribute.toString():"";
		//end common log fix
		return "Contribution_Template_for_" + String.valueOf(contractId) 
				+ "_for_" + formattedDate + CSV_EXTENSION;
	}
	
	/**
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}