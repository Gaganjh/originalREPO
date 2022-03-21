package com.manulife.pension.ps.web.contract;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
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

import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.ps.web.withdrawal.WebConstants;
import com.manulife.pension.service.contract.common.PlanMessage;
import com.manulife.pension.service.contract.common.PlanMessageType;
import com.manulife.pension.service.contract.util.ContractServiceFeatureUtil;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;

/**
 * Action that handles requests for the View Plan Data screen.
 * 
 * @author Andrew Dick
 */
@Controller
@RequestMapping(value="/contract")
@SessionAttributes("planDataForm")
public class ViewPlanDataController extends BasePlanDataController {

    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(ViewPlanDataController.class);
    
    public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
	forwards.put("default","/contract/plan/viewPlanData.jsp");
	forwards.put("defaultForPrintFriendly","/contract/plan/viewPlanData.jsp?printFriendly=true");
	forwards.put("edit","redirect:/do/contract/planData/edit/");
	forwards.put("error","/contract/plan/viewPlanData.jsp");
	forwards.put("homePageFinder","redirect:/do/home/homePageFinder/");
	}
    
    @Autowired
    private PSValidatorFWDefault psValidatorFWDefault;
    
    
    @ModelAttribute("planDataForm")
    public PlanDataForm planDataForm(){
    	return new PlanDataForm();
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value="/planData/view/", method = RequestMethod.GET)
    public final String doDefault(@Valid @ModelAttribute ("planDataForm") PlanDataForm form, BindingResult bindingResult,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException, ServletException,
            SystemException {
    	
    	if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(ACTION_FORWARD_DEFAULT);//if input forward not //available, provided default
            }
       }


 		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doDefault> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        //final PlanDataForm form = (PlanDataForm) actionForm;
        setPermissions(form, request);
        form.setViewMode();
        final Integer contractId = getUserProfile(request).getCurrentContract().getContractNumber();
        final PlanData data = getPlanData(contractId);
        
        if (data.getFirstPlanEntryDate() == null) {
            data.removeFirstPlanEntryDateErrorMessage();
        }
        
        final PlanDataUi ui = new PlanDataUi(data);
        form.setPlanDataUi(ui);
        
        LoanSettings loanSettings = LoanServiceDelegate.getInstance().getLoanSettings(contractId) ;
		if(loanSettings.isAllowOnlineLoans() && loanSettings.isLrk01()){
			form.setAllowOnlineLoans(true);
		}  
        
        final Map lookupData = ContractServiceDelegate.getInstance().getLookupData(contractId);
        prepareLookupData(lookupData, data);
        form.setLookupData(lookupData);
        
        //skip Employer Tax Identification Message from Validation Warnings
        if(data.doWarningCodesExist()){
        	removeEmployerTaxIdentificationWarnings(data);
        }

        // Check for validation warnings or errors
        if (data.doWarningCodesExist() || data.doErrorCodesExist()) {
            handleBusinessErrors(request, data);
        }

        final String forward;
        // Check if this request is supposed to be print friendly
        final String printFriendly = (String) request.getAttribute(WebConstants.PRINTFRIENDLY_KEY);
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doDefault> Found print friendly key [").append(
                    printFriendly).append("] in request.").toString());
        }
        if (StringUtils.isNotBlank(printFriendly)) {
            logger.debug("doDefault> Request is print friendly - using print friendly forward.");
            forward = forwards.get(ACTION_FORWARD_DEFAULT_FOR_PRINT_FRIENDLY);
        } else {
            // Use standard forward
            logger.debug("doDefault> Request is not print friendly - using default forward.");
            forward = forwards.get(ACTION_FORWARD_DEFAULT);
        }
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doDefault> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forward;
    }

    /**
     * doEdit is called when the page 'edit' button is pressed.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When an Servlet problem occurs.
     * @throws SystemException When an generic application problem occurs.
     */
    @RequestMapping(value="/planData/view/", params="action=edit", method = {RequestMethod.POST})
    public final String doEdit(@ModelAttribute ("planDataForm") PlanDataForm form, BindingResult bindingResult,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException, ServletException,
            SystemException {
    	
    	if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(ACTION_FORWARD_DEFAULT);//if input forward not //available, provided default
            }
       }

		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doEdit> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        //final PlanDataForm form = (PlanDataForm) actionForm;
        // Verify that user has appropriate permissions
        if (!form.getUserCanEdit()) {
            return forwards.get(ACTION_FORWARD_ERROR);
        }
        
        final Contract currentContract = getUserProfile(request).getCurrentContract();
        
        final Integer contractId = currentContract.getContractNumber();
        
        final PlanData data = getPlanData(contractId);
        
        data.setContractStatus(currentContract.getStatus());
        
        setDisableAttrForUpdateOnPlanData(currentContract.getStatus(), data);
        
        if (data.getFirstPlanEntryDate() == null) {
            data.removeFirstPlanEntryDateErrorMessage();
        }
        
        final boolean lockObtained = obtainLock(data, request);
        if (!lockObtained) {
            handleObtainLockFailure(data, request);
            return forwards.get(ACTION_FORWARD_ERROR);
        }

        final PlanDataUi ui = new PlanDataUi(data);
        form.setPlanDataUi(ui);
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doEdit> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get(ACTION_FORWARD_EDIT);
    }
    
    /**
     * Returns a byte array that contains the SPH PDF document.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */
    
    @RequestMapping(value="/planData/view/", params="actionLabel=viewSphPdf", method = {RequestMethod.GET, RequestMethod.POST})
    public final String doViewSphPdf(@ModelAttribute ("planDataForm") PlanDataForm form, BindingResult bindingResult,
            final HttpServletRequest request,
            final HttpServletResponse response) throws SystemException {

    	if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(ACTION_FORWARD_DEFAULT);//if input forward not //available, provided default
            }
       }
		byte[] downloadData = null;
		final UserProfile userProfile = getUserProfile(request);
		
		ContractServiceDelegate delegate= ContractServiceDelegate.getInstance();
		final Integer contractId = userProfile.getCurrentContract().getContractNumber();
		boolean summaryHighlightsAvailable = false;
		boolean summaryHighlightsReviewed = false;
		try {
			ContractServiceFeature serviceFeature = delegate
					.getContractServiceFeature(contractId, ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_AVAILABLE);
			summaryHighlightsAvailable = ContractServiceFeature.internalToBoolean(serviceFeature.getValue()).booleanValue();
			summaryHighlightsReviewed = ContractServiceFeature
					.internalToBoolean(serviceFeature.getAttributeValue(ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_REVIEWDED)).booleanValue();
		} catch (ApplicationException exception) {
			throw new SystemException(exception, "Unable to retrive service feature for contract id " + contractId);
		}
		if (userProfile.getRole() instanceof InternalUser
				|| userProfile.getRole() instanceof ThirdPartyAdministrator) {
			if(!summaryHighlightsAvailable){
				return forwards.get("homePageFinder");
			}
		} else {
			if(!summaryHighlightsAvailable || !summaryHighlightsReviewed){
				return forwards.get("homePageFinder");
			}
		}
		
		Location location = Location.valueOfForAbbreviation(CommonEnvironment
				.getInstance().getSiteLocation());
		String headerFooterImagePath = null; 
		java.net.URL url=	getClass().getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX + 
				CommonConstants.HEADER_FOOTER_IMAGE);
		if(url != null){
			headerFooterImagePath = url.toExternalForm();
		}
		if (logger.isDebugEnabled()) {
            logger.info("Header Footer Image path for the PH PDF is "+headerFooterImagePath);
        }
		downloadData = ContractServiceDelegate.getInstance().generateSphPdf(
				contractId, location, headerFooterImagePath);
		if (downloadData != null && downloadData.length > 0) {
			ReportController.streamDownloadData(request, response,
					"application/pdf",  "sph_" + contractId + Constants.PDF_FILE_NAME_EXTENSION, downloadData);
		}
		return null;
	}
	
	 /**
	 * Sets whether it is needed to disable certain plan attributes for update on PlanData or not
	 * 
     * @param contract Contract
     * @param data PlanData
	 * @throws SystemException
	 */
	private void setDisableAttrForUpdateOnPlanData(String status, PlanData data) throws SystemException {
		
        if ((Contract.STATUS_ACTIVE_CONTRACT.equals(status) || Contract.STATUS_CONTRACT_FROZEN.equals(status)) && 
        		(ContractServiceFeatureUtil.isFreezePeriod(data.getPayrollCutOffDays(), data.getContractAnniversaryDate(), true))) {
        	data.setDisableAttrForUpdate(true);
        }
        
	}
	
	 /**
     * remove the Tax Identification not unique warning message
     * 
     * @param none
     * @param none
     * @return none
     */
    public void removeEmployerTaxIdentificationWarnings(PlanData plandata) {
        
        Collection<PlanMessage> planMessages = plandata.getWarningCodes();
        if (planMessages != null) {
            for (PlanMessage planMessage:planMessages) {
                if (planMessage.getPlanMessageType() == PlanMessageType.EMPLOYER_TAX_IDENTIFICATION_NUMBER_PLAN_NUMBER_NOT_UNIQUE) {
                    planMessages.remove(planMessage);
                    break;
                }
            }
        }        
    }
    
   @InitBinder
   protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder  binder) {
	  binder.addValidators(psValidatorFWDefault);
	
 }

}
