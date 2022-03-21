package com.manulife.pension.ps.web.contract;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.contract.valueobject.ContributionTemplateReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantContribution;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWContriTemp;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * This class models the action of retrieving and presenting a contribution 
 * template for a contract.
 * 
 * @author Adrian Robitu
 */
@Controller
@RequestMapping( value = "/contract")

public class ContributionTemplateReportController extends ReportController {

	@ModelAttribute("contributionTemplateForm") 
	public  ReportForm populateForm() 
	{
		return new  ReportForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("contriTemp", "/tools/toolsMenu.jsp");
		}

	
	public static final NumberFormat NUMBER_FORMATTER = new DecimalFormat("00");
 	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");
 	
	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		
		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter("contractNumber", new Integer(userProfile.getCurrentContract().getContractNumber()));

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}		
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return ContributionTemplateReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return ContributionTemplateReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return "name";
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see ReportController#getFileName()
	 */
 	protected String getFileName(HttpServletRequest request) {
 		String dateString = null;
 		synchronized (DATE_FORMATTER) {
 			dateString = DATE_FORMATTER.format(new Date());
		}
 		return "Contribution_Template_for_" + getUserProfile(request).getCurrentContract().getContractNumber() +
 				"_for_" + dateString + CSV_EXTENSION;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}
		
		final UserProfile userProfile = getUserProfile(request);
		
		//FunctionalLogger.INSTANCE.log("Download Contribution File Template", userProfile, getClass(), getMethodName(null, reportForm, request));
		
		StringBuffer buffer = new StringBuffer();
		ContributionTemplateReportData contributionData = 
				(ContributionTemplateReportData) report;

		// Fill in the header
		Iterator columnLabels = contributionData.getColumnLabels().iterator();
		while (columnLabels.hasNext()) {
			buffer.append(columnLabels.next());
			if (columnLabels.hasNext()) {
				buffer.append(COMMA);
			}
		}
		
		StringBuffer emptyContributionBuffer = null;
        
        if (! userProfile.isSelectedAccess()) {
		
    		Iterator items = report.getDetails().iterator();
    		while (items.hasNext()) {
    			buffer.append(LINE_BREAK);
    			buffer.append(contributionData.getTransactionNumber()).append(COMMA);
    			buffer.append(contributionData.getContractNumber()).append(COMMA);
    			
    			ParticipantContribution contribution = (ParticipantContribution) items.next();
    			buffer.append(contribution.getIdentifier()).append(COMMA);
    			buffer.append(QUOTE).append(contribution.getName()).append(QUOTE).append(COMMA);
    
    			buffer.append(contributionData.getFormattedDate()).append(COMMA);
    
    			// Only fill in the buffer the first time
    			if (emptyContributionBuffer == null) {
    				emptyContributionBuffer = new StringBuffer();
    
    				// Skip the last comma, since it will be added in the loans section
    				for (int i=1; i<contribution.getMoneyTypeAmounts().size(); i++) {
    					emptyContributionBuffer.append(COMMA);
    				}
    			}
    
    			// Empty contribution amounts for each money type (no trailing comma)
    			buffer.append(emptyContributionBuffer);
    			
    			// Loans (commas pre-pended)
    			Iterator loans = contribution.getLoanAmounts().keySet().iterator();
    			while (loans.hasNext()) {
    				long loanId = ((Integer) loans.next()).longValue();
    				String loanIdString = null;
    				synchronized (NUMBER_FORMATTER) {
    					loanIdString = NUMBER_FORMATTER.format(loanId);
    				}
    				buffer.append(COMMA).append(loanIdString).append(COMMA).append(ZERO_AMOUNT_STRING);
    			}
    			
    			// Fill-in the rest of the columns until max no. of loans per contract
    			int actualLoanCount = contribution.getLoanAmounts().size();
    			if (actualLoanCount < contributionData.getMaxLoanCount()) {
    				for (int i = 0; i < (contributionData.getMaxLoanCount() - actualLoanCount); i++) {
    					buffer.append(COMMA).append(COMMA);
    				}
    			}
    		}
        }
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}
				
		return buffer.toString().getBytes();
	}
	
		
	@RequestMapping(value ="/contributionTemplate", params={"task=download"}  , method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("contributionTemplateForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		UserProfile userProfile = getUserProfile(request);
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		try {
    				request.setAttribute(Constants.REQUEST_TYPE,WithdrawalWebUtil.getTypeOfRequest(userProfile));
    			} catch (SystemException e) {
    				e.printStackTrace();
    			}
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("contriTemp");//if input forward not //available, provided default
        	}
        }
		String forward=super.doDownload( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}	
		
	/** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations
	 */
	
	 @Autowired
	   private PSValidatorFWContriTemp  psValidatorFWContriTemp;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWContriTemp);
	}
	 
}