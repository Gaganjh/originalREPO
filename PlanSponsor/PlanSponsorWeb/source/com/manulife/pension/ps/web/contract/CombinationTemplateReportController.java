package com.manulife.pension.ps.web.contract;

import java.math.BigDecimal;
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

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.contract.valueobject.CombinationTemplateReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantCombination;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWCombiTemp;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * This class models the action of retrieving and presenting a Combination 
 * template for a contract.
 * 
 * @author vishnu
 */
@Controller
@RequestMapping( value = "/contract")

public class CombinationTemplateReportController extends ReportController {

	@ModelAttribute("combinationTemplateForm")
	public  ReportForm populateForm() 
	{
		return new  ReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("combiTemp"," /tools/toolsMenu.jsp");
		}

	public static final NumberFormat NUMBER_FORMATTER = new DecimalFormat("00");
 	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");
 	
 	protected static final String DOWNLOAD_COLUMN_HEADING = "comb.h10,Cont#,SSN#,FirstName,LastName,Initial,NamePrefix,EEID#,Address1,Address2,City,State,ZipCode,Country,"
        + "StateRes,ERProvEmail,Division,BirthDate,HireDate,EmplStat,EmplStatDate,EligInd,EligDate,OptOutInd,YTDHrs,PlanYTDComp,YTDHrsWkCompDt,"
        + "BaseSalary,BfTxDefPct,DesigRothPct,BfTxFltDoDef,DesigRothAmt";
 	
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

		 // if external user, don't display Cancelled employees
        criteria.setExternalUser(userProfile.getRole().isExternalUser());
        
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}		
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return CombinationTemplateReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return CombinationTemplateReportData.REPORT_NAME;
	}

	/**
	 * @return Default Sort.
	 */
	protected String getDefaultSort() {
		return "name";
	}

	/**
	 * @return Default Sort Direction
	 */
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
 		return "Combination_Template_for_" + getUserProfile(request).getCurrentContract().getContractNumber() +
 				"_for_" + dateString + CSV_EXTENSION;
	}
 	
 	/**
	 * @see ReportController#DownloadData()
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}
		
        UserProfile user = getUserProfile(request);
        UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(user.getPrincipal());
        Contract currentContract = user.getCurrentContract();
        
        FunctionalLogger.INSTANCE.log("Download Combination File Template", user, getClass(), getMethodName( reportForm, request));
        
		StringBuffer buffer = new StringBuffer();
		CombinationTemplateReportData combinationData = (CombinationTemplateReportData) report;
		StringBuffer emptyCombinationBuffer = null;
		
		//this flag to remove the EERC - Employee Roll-Over Contribution.
		boolean eercFlag = false;
		
		// heading and records
        buffer.append(DOWNLOAD_COLUMN_HEADING);
        buffer.append(COMMA); 
        
        // Fill in the header
		Iterator columnLabels = combinationData.getColumnLabels().iterator();
		while (columnLabels.hasNext()) {
			String label = columnLabels.next().toString();		
			if(!label.equals("EERC")){				
			buffer.append(label);
			if (columnLabels.hasNext()) {
				buffer.append(COMMA);
			}
			}else{
				eercFlag = true;
			}
		}
        
		//To fill the Common Data.
        if (!getUserProfile(request).isSelectedAccess() && getUserProfile(request).isSubmissionAccess()) {
		
    		Iterator items = report.getDetails().iterator();
    		int counter=0;
    		while (items.hasNext()) { 
    			
    			buffer.append(LINE_BREAK);     			
    			ParticipantCombination combination = (ParticipantCombination) items.next();    			
    			buffer.append("comb.d").append(COMMA);
                buffer.append(currentContract.getContractNumber()).append(COMMA); 
                
                if(combination.getSsn()!=null){
                	buffer.append(combination.getSsn()).append(COMMA);                
                }                
                if(!StringUtils.isEmpty(combination.getFirstName())){
                	buffer.append(escapeField(combination.getFirstName())).append(COMMA);                                	
                }
                if(!StringUtils.isEmpty(combination.getFirstName())){
                	buffer.append(escapeField(combination.getLastName())).append(COMMA);
                }
                if (combination.getMiddleInitial() != null){
                	buffer.append(escapeField(combination.getMiddleInitial()));
                }                    
                buffer.append(COMMA);
                if (combination.getNamePrefix() != null){
                	  buffer.append(escapeField(combination.getNamePrefix()));
                }                  
                buffer.append(COMMA);
                if (combination.getEmployeeNumber() != null && combination.getEmployeeNumber().trim().length() > 0) {
                    buffer.append(StringUtils.leftPad(combination.getEmployeeNumber().trim(), 9, "0"));
                }
                buffer.append(COMMA);
                if (combination.getAddressLine1() != null){
                	 buffer.append(escapeField(combination.getAddressLine1().trim()));
                }                   
                buffer.append(COMMA);
                if (combination.getAddressLine2() != null){
                	 buffer.append(escapeField(combination.getAddressLine2().trim()));
                }                   
                buffer.append(COMMA);
                if (combination.getCity() != null){
                	  buffer.append(escapeField(combination.getCity().trim()));
                }                  
                buffer.append(COMMA);
                if (combination.getStateCode() != null){
                	buffer.append(escapeField(combination.getStateCode().trim()));
                }
                buffer.append(COMMA);
                if (combination.getZipCode() != null) {
                    String zipCode = StringUtils.trim(combination.getZipCode());
                    if (zipCode.length() > 0) {
                    	buffer.append(zipCode.toUpperCase());
                    }
                }
                buffer.append(COMMA);
                if (combination.getCountry() != null){
                    buffer.append(escapeField(combination.getCountry().trim()));
                }
                buffer.append(COMMA);
                if (combination.getStateOfResidence() != null){
                	 buffer.append(escapeField(combination.getStateOfResidence().trim()));
                }                   
                buffer.append(COMMA);
                if (combination.getEmployeeProvidedEmail() != null){
                    buffer.append(escapeField(combination.getEmployeeProvidedEmail().trim()));
                }
                buffer.append(COMMA);
                if (combination.getDivision() != null){
                	buffer.append(escapeField(combination.getDivision().trim()));
                }                    
                buffer.append(COMMA);
                buffer.append(
                        DateRender.formatByPattern(combination.getBirthDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                buffer.append(
                        DateRender.formatByPattern(combination.getHireDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                if (combination.getStatus() != null){
                	buffer.append(escapeField(combination.getStatus().trim()));
                }                    
                buffer.append(COMMA);
                buffer.append(
                        DateRender.formatByPattern(combination.getEmployeeStatusDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                if (combination.getEligibleToDeferInd() != null){
                	buffer.append(escapeField(combination.getEligibleToDeferInd().trim()));
                }                    
                buffer.append(COMMA);                
                if("Y".equalsIgnoreCase(combination.getProvidedEligibilityDateInd())){
                    buffer.append(
                        DateRender.formatByPattern(combination.getEligibilityDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                }else{
                    buffer.append(",");
                }
                if (combination.getOptOut() != null){
                	buffer.append(escapeField(combination.getOptOut().trim()));
                }                    
                buffer.append(COMMA);
                if (combination.getPlanYTDHoursWorked() != null && combination.getPlanYTDHoursWorkedEffDate() != null){
                	buffer.append(escapeField(NumberRender.formatByType(combination.getPlanYTDHoursWorked(), "", RenderConstants.INTEGER_TYPE)));
                }                  
                buffer.append(COMMA);
                buffer.append(escapeField(getMaskedValue(combination
                                .getPlanYTDCompensation(), combination, user, userInfo, false))).append(COMMA);
                buffer.append(DateRender.formatByPattern(combination.getPlanYTDHoursWorkedEffDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                buffer.append(
                        escapeField(getMaskedValue(
                        		combination.getAnnualBaseSalary(), combination, user, userInfo, false))).append(COMMA);
                buffer.append(
                        escapeField(NumberRender.formatByPattern(combination
                                .getBeforeTaxDeferralPercentage(), "", "###.000"))).append(COMMA);
                buffer.append(
                        escapeField(NumberRender.formatByPattern(combination
                                .getDesigRothDeferralPercentage(), "", "###.000"))).append(COMMA);
                buffer.append(
                        escapeField(NumberRender.formatByType(combination.getBeforeTaxDeferralAmount(),
                                "", RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
                buffer.append(
                        escapeField(NumberRender.formatByType(combination.getDesigRothDeferralAmount(),
                                "", RenderConstants.CURRENCY_TYPE, false))).append(COMMA);    			    			
                
                // Contribution Fields			    			
        		buffer.append(combinationData.getTransactionNumber()).append(COMMA);
    			buffer.append(combinationData.getFormattedDate()).append(COMMA);
    			
    			// Only fill in the buffer the first time
    			if (emptyCombinationBuffer == null) {
    				emptyCombinationBuffer = new StringBuffer();
    
    			// Skip the last comma, since it will be added in the loans section
    			if(combination.getMoneyTypeAmounts() != null){    			
    				int moneyTypeSize = combination.getMoneyTypeAmounts().size();
    					if(eercFlag){
    						moneyTypeSize--;
    					}
						for (int i=1; i<moneyTypeSize; i++) {
							emptyCombinationBuffer.append(COMMA);
						}
    				}
    			}
    				    			
    			// Empty Combination amounts for each money type (no trailing comma)
    			buffer.append(emptyCombinationBuffer);
    			
    			// Loans (commas pre-pended)
    			if(combination.getLoanAmounts()!= null){
    				Iterator loans = combination.getLoanAmounts().keySet().iterator();
        			while (loans.hasNext()) {
        				long loanId = ((Integer) loans.next()).longValue();
        				String loanIdString = null;
        				synchronized (NUMBER_FORMATTER) {
        					loanIdString = NUMBER_FORMATTER.format(loanId);
        				}
        				buffer.append(COMMA).append(loanIdString).append(COMMA).append(ZERO_AMOUNT_STRING);
        			}
    			}
    			    			
    			// Fill-in the rest of the columns until max no. of loans per contract
    			if(combination.getLoanAmounts() != null){
    				int actualLoanCount = combination.getLoanAmounts().size();
        			if (actualLoanCount < combinationData.getMaxLoanCount()) {
        				for (int i = 0; i < (combinationData.getMaxLoanCount() - actualLoanCount); i++) {
        					buffer.append(COMMA).append(COMMA);
        				}
        			}
    			}    			
        	}    		
        }
		
        if (logger.isDebugEnabled()) {
				logger.debug("exit <- populateDownloadData");
		}
				
		return buffer.toString().getBytes();
	}
	
    private String escapeField(String field) {
		if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}
    
    /**
     * 
     * @param value
     * @param item
     * @param user
     * @param userInfo
     * @param forceMask
     * @return Masked Value
     */
    public String getMaskedValue(BigDecimal value, ParticipantCombination item, UserProfile user, UserInfo userInfo, 
            boolean forceMask) {
    	if (logger.isDebugEnabled()) {
			logger.debug("entry -> getMaskedValue");
		}
		StringBuffer html = new StringBuffer();
		boolean maskSensitiveInfo = CensusUtils.isMaskSensitiveInformation(
				user, userInfo, isMaskSensitiveInfoInd(item));

		if (value != null && value.compareTo(new BigDecimal(0)) != 0) {
			if (maskSensitiveInfo || forceMask) {
				html.append("");
			} else {
				html.append(NumberRender.formatByType(value, "",
						RenderConstants.CURRENCY_TYPE, false));
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getMaskedValue");
		}
		
		return html.toString();
	}
    
    private boolean isMaskSensitiveInfoInd(ParticipantCombination item) {
		boolean maskSensitiveInfoInd = false;

		if (item != null) {
			if (Constants.YES.equals(item.getMaskSensitiveInfo()))
				maskSensitiveInfoInd = true;
			else
				maskSensitiveInfoInd = false;
		}

		return maskSensitiveInfoInd;
    }
   	    		
	@RequestMapping(value = "/combinationTemplate", params = {"task=download"}, method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("combinationTemplateForm") ReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		UserProfile userProfile = getUserProfile(request);
		if (bindingResult.hasErrors()) {
			try {
				request.setAttribute(Constants.REQUEST_TYPE, WithdrawalWebUtil.getTypeOfRequest(userProfile));
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("combiTemp");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    	    	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */   
	
	

	@Autowired
	private PSValidatorFWCombiTemp psValidatorFWCombiTemp;
	
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWCombiTemp);
	}
}