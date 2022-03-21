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

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.CensusSummaryUtils;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWCensusTemp;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This class models the action of retrieving and presenting a census template for a contract.
 * 
 * @author Diana Macean
 */
@Controller
@RequestMapping( value = "/contract")

public class CensusTemplateReportController extends ReportController {

	@ModelAttribute("censusTemplateForm") 
	public  ReportForm populateForm()
	{
		return new  ReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("censusTemp", "/tools/toolsMenu.jsp");}        

    public static final NumberFormat NUMBER_FORMATTER = new DecimalFormat("00");
    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");

    protected static final String DOWNLOAD_COLUMN_HEADING = "cens.h10,Cont#,SSN#,FirstName,LastName,Initial,NamePrefix,EEID#,Address1,Address2,City,State,ZipCode,Country,"
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
        Contract currentContract = userProfile.getCurrentContract();

        criteria.addFilter("contractNumber", Integer.toString(currentContract.getContractNumber()));

        // if external user, don't display Cancelled employees
        criteria.setExternalUser(userProfile.getRole().isExternalUser());

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateReportCriteria");
        }
    }
	    	 @RequestMapping(value ="/censusTemplate" ,params={"task=download"}  , method = {RequestMethod.GET})	
	    	 public String doDownload (@Valid @ModelAttribute("censusTemplateForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    			     throws SystemException {
	    			 UserProfile userProfile = getUserProfile(request);
	    			     	   if(bindingResult.hasErrors()){
	    			     		  try {
	    			  		        request.setAttribute(Constants.REQUEST_TYPE,WithdrawalWebUtil.getTypeOfRequest(userProfile));
	    			  			} catch (SystemException e) {
	    			  				e.printStackTrace();
	    			  			}
	    			     		     String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	    			     		     if(errDirect!=null){
	    			     		      request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	    			     		     forwards.get("censusTemp");
	    			     		    }
	    			     }
	    			     String forward=super.doDownload( form, request, response);
	    			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    			    }
	    	
	    		    
    /**
     * @see ReportController#getReportId()
     */
    protected String getReportId() {
        return CensusSummaryReportData.TEMPLATE_REPORT_ID;
    }

    /**
     * @see ReportController#getReportName()
     */
    protected String getReportName() {
        return CensusSummaryReportData.TEMPLATE_REPORT_NAME;
    }

    protected String getDefaultSort() {
        return CensusSummaryReportData.DEFAULT_SORT;
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
        return "Census_Template_for_"
                + getUserProfile(request).getCurrentContract().getContractNumber() + "_for_"
                + dateString + CSV_EXTENSION;
    }

    protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws SystemException{

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDownloadData");
        }

        UserProfile user = getUserProfile(request);
        UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(user.getPrincipal());
        Contract currentContract = user.getCurrentContract();

        FunctionalLogger.INSTANCE.log("Download Census File Template", user, getClass(), getMethodName(reportForm, request));
        
        // find the contract sort code
        String sortCode = currentContract.getParticipantSortOptionCode();
        boolean isECEnabled = false;
        try{
            
            ContractServiceFeature csf = com.manulife.pension.delegate.ContractServiceDelegate
            	.getInstance().getContractServiceFeature(currentContract.getContractNumber(),
                ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
            isECEnabled =   ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue();
        }catch (ApplicationException ae) {
            throw new SystemException(ae.getMessage());
        }

        StringBuffer buffer = new StringBuffer();

        // heading and records
        buffer.append(DOWNLOAD_COLUMN_HEADING);
        
        if (!user.isSelectedAccess()) {

            Iterator iterator = report.getDetails().iterator();
            while (iterator.hasNext()) {
                buffer.append(LINE_BREAK);
                CensusSummaryDetails theItem = (CensusSummaryDetails) iterator.next();

                buffer.append("cens.d").append(COMMA);
                buffer.append(currentContract.getContractNumber()).append(COMMA);
                buffer.append(SSNRender.format(theItem.getSsn(), null, false)).append(COMMA);

                buffer.append(escapeField(theItem.getFirstName())).append(COMMA);
                buffer.append(escapeField(theItem.getLastName())).append(COMMA);

                if (theItem.getMiddleInitial() != null)
                    buffer.append(escapeField(theItem.getMiddleInitial()));
                buffer.append(COMMA);

                if (theItem.getNamePrefix() != null)
                    buffer.append(escapeField(theItem.getNamePrefix()));
                buffer.append(COMMA);

                if (theItem.getEmployeeNumber() != null && theItem.getEmployeeNumber().trim().length() > 0) {
                    buffer.append(StringUtils.leftPad(theItem.getEmployeeNumber().trim(), 9, "0"));
                }
                buffer.append(COMMA);

                if (theItem.getAddressLine1() != null)
                    buffer.append(escapeField(theItem.getAddressLine1().trim()));
                buffer.append(COMMA);

                if (theItem.getAddressLine2() != null)
                    buffer.append(escapeField(theItem.getAddressLine2().trim()));
                buffer.append(COMMA);

                if (theItem.getCity() != null)
                    buffer.append(escapeField(theItem.getCity().trim()));
                buffer.append(COMMA);

                if (theItem.getStateCode() != null)
                    buffer.append(escapeField(theItem.getStateCode().trim()));
                buffer.append(COMMA);

                if (theItem.getZipCode() != null) {
                    String zipCode = StringUtils.trim(theItem.getZipCode());
                    if (zipCode.length() > 0) {
                    	buffer.append(zipCode.toUpperCase());
                    }
                }
                buffer.append(COMMA);

                if (theItem.getCountry() != null)
                    buffer.append(escapeField(theItem.getCountry().trim()));
                buffer.append(COMMA);

                if (theItem.getStateOfResidence() != null)
                    buffer.append(escapeField(theItem.getStateOfResidence().trim()));
                buffer.append(COMMA);

                if (theItem.getEmployeeProvidedEmail() != null)
                    buffer.append(escapeField(theItem.getEmployeeProvidedEmail().trim()));
                buffer.append(COMMA);

                if (theItem.getDivision() != null)
                    buffer.append(escapeField(theItem.getDivision().trim()));
                buffer.append(COMMA);

                buffer.append(
                        DateRender.formatByPattern(theItem.getBirthDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                buffer.append(
                        DateRender.formatByPattern(theItem.getHireDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

                if (theItem.getStatus() != null)
                    buffer.append(escapeField(theItem.getStatus().trim()));
                buffer.append(COMMA);

                buffer.append(
                        DateRender.formatByPattern(theItem.getEmployeeStatusDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

                if (theItem.getEligibleToDeferInd() != null)
                    buffer.append(escapeField(theItem.getEligibleToDeferInd().trim()));
                buffer.append(COMMA);
                
                if("Y".equalsIgnoreCase(theItem.getProvidedEligibilityDateInd())){
                    buffer.append(
                        DateRender.formatByPattern(theItem.getEligibilityDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                }else{
                    buffer.append(",");
                }

                if (theItem.getOptOut() != null)
                    buffer.append(escapeField(theItem.getOptOut().trim()));
                buffer.append(COMMA);

                if (theItem.getPlanYTDHoursWorked() != null)
                    buffer.append(escapeField(NumberRender.formatByType(theItem.getPlanYTDHoursWorked(), "", RenderConstants.INTEGER_TYPE)));
                buffer.append(COMMA);

                buffer.append(escapeField(CensusSummaryUtils.getMaskedValue(theItem
                                .getPlanYTDCompensation(), theItem, user, userInfo, false))).append(COMMA);
                buffer.append(DateRender.formatByPattern(theItem.getPlanYTDHoursWorkedEffDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

                buffer.append(
                        escapeField(CensusSummaryUtils.getMaskedValue(
                                theItem.getAnnualBaseSalary(), theItem, user, userInfo, false))).append(COMMA);
                buffer.append(
                        escapeField(NumberRender.formatByPattern(theItem
                                .getBeforeTaxDeferralPercentage(), "", "###.000"))).append(COMMA);
                buffer.append(
                        escapeField(NumberRender.formatByPattern(theItem
                                .getDesigRothDeferralPercentage(), "", "###.000"))).append(COMMA);
                buffer.append(
                        escapeField(NumberRender.formatByType(theItem.getBeforeTaxDeferralAmount(),
                                "", RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
                buffer.append(
                        escapeField(NumberRender.formatByType(theItem.getDesigRothDeferralAmount(),
                                "", RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
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

    /** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations
	 */

	  @Autowired
  private PSValidatorFWCensusTemp  psValidatorFWCensusTemp;

	  @InitBinder
 public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
   binder.bind( request);
   binder.addValidators(psValidatorFWCensusTemp);
}
	
}