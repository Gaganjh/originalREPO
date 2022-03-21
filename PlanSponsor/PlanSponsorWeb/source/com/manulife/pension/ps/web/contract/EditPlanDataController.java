package com.manulife.pension.ps.web.contract;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Category;
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
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.util.ContractServiceFeatureUtil;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.plan.valueobject.PlanAutoContributionIncrease;
import com.manulife.pension.service.plan.valueobject.WithdrawalDistributionMethod;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * Action that handles requests for the Edit Plan Data screen.
 * 
 * @author Andrew Dick
 */
@Controller
@RequestMapping(value="/contract")
@SessionAttributes("planDataForm")
public class EditPlanDataController extends BasePlanDataController {

    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(EditPlanDataController.class);
    
    private Category interactionLog = Category.getInstance(ServiceLogRecord.class);
    
    private ServiceLogRecord logRecord = new ServiceLogRecord(EditPlanDataController.class.getName());
    private static final String PLANDATA_VIEW_REDIRECT =  "redirect:/do/contract/planData/view/"; 
    
    protected static Map<String,String> forwards = new HashMap<String,String>();
	static{
	forwards.put("default","/contract/plan/editPlanData.jsp");
	forwards.put("cancel", PLANDATA_VIEW_REDIRECT);
	forwards.put("confirm","redirect:/do/contract/planData/confirm/");
	forwards.put("save", PLANDATA_VIEW_REDIRECT);
	forwards.put("error","/contract/plan/editPlanData.jsp");
	forwards.put("bookmarkDetected", PLANDATA_VIEW_REDIRECT);
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
    @RequestMapping(value="/planData/edit/", method =  RequestMethod.GET)
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
        setPermissions(form, request);
        if (!form.getUserCanEdit()) {
            forwards.get(ACTION_FORWARD_CANCEL);
        }
        form.setEditMode();
        final Integer contractId = getUserProfile(request).getCurrentContract().getContractNumber();
        if (form.getPlanDataUi() == null) {
            // If plan data ui has not been set, this is a bookmark
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuffer("doDefault> Bookmark detected for contract id [")
                        .append(contractId).append("]").toString());
            }
            forwards.get(ACTION_FORWARD_BOOKMARK_DETECTED);
        }
        final Map lookupData = ContractServiceDelegate.getInstance().getLookupData(contractId);
        form.setLookupData(lookupData);

        // Check for validation warnings or errors
        final PlanData data = form.getPlanDataUi().getPlanData();
        if (!refreshLock(data, request)) {
            handleObtainLockFailure(data, request);
            forwards.get(ACTION_FORWARD_CANCEL);
        }
        if (data.doWarningCodesExist() || data.doErrorCodesExist()) {
            handleBusinessErrors(request, data);
        }
        
        setNewParticipantApplyDateDisabled(data, false);
        
        prepareLookupData(lookupData, data);
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
        return forwards.get(ACTION_FORWARD_DEFAULT);
    }

    /**
     * doSave is called when the page 'save' button is pressed.
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
    @RequestMapping(value="/planData/edit/",params="action=save", method =  RequestMethod.POST)
    public final String doSave(@Valid @ModelAttribute ("planDataForm") PlanDataForm form, BindingResult bindingResult,
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
			logger.info("doSave> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        // Verify that user has appropriate permissions
        if (!form.getUserCanEdit()) {
        	forwards.get(ACTION_FORWARD_CANCEL);
        }
        // Get the clean withdrawal request
        final PlanDataUi planDataUi = form.getPlanDataUi();
        refreshLock(planDataUi.getPlanData(), request);
        
        // set Part Time Eligibility true when Hours of service credit method is true for EEDEF and
        // EEROT 
        planDataUi.updatePartTimeEligibilityForEEDEFAndEEROT();
        
        // update eligibility requirements FOR EEROT
        // with same values as EEDEF if contract has both EEDEF and EEROT
        planDataUi.updateEligibilityRequirementsForEEROT();

        // Convert the types
        planDataUi.convertToBean();
        final PlanData planData = planDataUi.getPlanData();
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doSave> Saving with object [\n").append(planData)
                    .append("\n]\n").toString());
        }
       
    	final PlanData result = ContractServiceDelegate.getInstance().validatePlanData(planData);
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doSave> Validated plan data and found [").append(
                    result.getErrorCodes().size()).append("] errors and [").append(
                    result.getWarningCodes().size()).append("] warnings.").toString());
        }

        int contractId=getUserProfile(request).getCurrentContract()
				.getContractNumber();
        String proposalNo=getUserProfile(request).getCurrentContract().getProposalNumber();
		
        if (result.doErrorCodesExist()) {
            // Errors exist - return to input
            handleBusinessErrors(request, result);
            setNewParticipantApplyDateDisabled(result, true);
            final String forward = forwards.get(ACTION_FORWARD_ERROR);
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("handleBusinessErrors> Forwarding to error [")
                        .append(forward).append("].").toString());
            }
            return forward;
        }
        
        // Otherwise check if we have warnings and need to confirm
        if (result.doWarningCodesExist()) {

            // Save a copy of the plan data in the form to prevent modification
            final PlanDataUi ui = new PlanDataUi(result);
            form.setModifiedPlanDataUi((PlanDataUi) ui.clone());

            return forwards.get(ACTION_FORWARD_CONFIRM);
        }

		// if no errors or warnings during validation then update the
		// installment indicator in Apollo TLP1207
		Boolean newInstallmentsIndicator = form.getPlanDataUi().getPlanData()
				.getWithdrawalDistributionMethod().getInstallmentsIndicator();

		WithdrawalDistributionMethod withdrawalDistributionMethod = ContractServiceDelegate
				.getInstance().getWithdrawalDistributionMethods(contractId);

		Boolean oldInstallmentsIndicator = withdrawalDistributionMethod
				.getInstallmentsIndicator();
		//checking the old and new installment indicator status, if both are not equal installment indicator in Apollo TLP1207 

		if (!newInstallmentsIndicator.equals(oldInstallmentsIndicator)) {
			boolean sywInd = ContractServiceDelegate
			.getInstance()
			.isSystematicWithdrawalFeatureONForJTScreen(contractId);
			
			UserProfile userProfile = getUserProfile(request);
			
			Contract contract = userProfile.getCurrentContract();
			boolean isTotalCareContract= contract.isBundledGaIndicator();
			
			String updateSYWIndicator = null;
			if (sywInd && !isTotalCareContract) {
				updateSYWIndicator = ContractServiceDelegate.getInstance()
						.updateSYWInstallmentIndicator(
								contractId,
								Integer.parseInt(proposalNo),
								planData.getWithdrawalDistributionMethod()
										.getInstallmentsIndicator());

			}
				if (null != updateSYWIndicator && !updateSYWIndicator.equalsIgnoreCase("OK")) {
					final PlanData sywUpdateResult = ContractServiceDelegate
							.getInstance().validateSYWIndicatorUpdate(planData,updateSYWIndicator);
					if (sywUpdateResult.doErrorCodesExist()) {
						// Errors exist - return to input
						handleBusinessErrors(request, sywUpdateResult);
						setNewParticipantApplyDateDisabled(result, true);
						final String forward = forwards.get(ACTION_FORWARD_ERROR);
						if (logger.isDebugEnabled()) {
							logger.debug(new StringBuffer(
									"handleBusinessErrors> Forwarding to error [")
									.append(forward).append("].").toString());
						}
						return forward;
					}

				}
			
		}
                
        // No errors or warnings so we can just save
       
        savePlanData(result, request);
       
        // Build the log data
		StringBuilder logData = new StringBuilder();
		logData.append(CONTRACT_NUMBER_LOG_TEXT);
		logData.append(":");
		logData.append(getUserProfile(request).getCurrentContract()
				.getContractNumber());
		logData.append(":");
		logData.append(PLAN_INFORMATION_CHANGE_LOG_TEXT);
		
		// Log the save action
		logWebActivity(this.getClass().getName(), "doSave", "SavePlanData",
				logData.toString(), getUserProfile(request), logger,
				interactionLog, logRecord);
		
        releaseLock(result, request);
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doSave> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get(ACTION_FORWARD_SAVE);
    }

    /**
     * doCancel is called when the page 'cancel' button is pressed.
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
    @RequestMapping(value="/planData/edit/", params="action=cancel", method =  RequestMethod.POST)
    public final String doCancel(@Valid @ModelAttribute ("planDataForm") PlanDataForm form, BindingResult bindingResult,
            final HttpServletRequest request,
            final HttpServletResponse response)throws IOException, ServletException,
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
			logger.info("doCancel> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        final PlanDataUi planDataUi = form.getPlanDataUi();
        releaseLock(planDataUi.getPlanData(), request);
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doCancel> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get(ACTION_FORWARD_CANCEL);
    }
    
    /**
     * Method to set the disable value for the effective date 
     * 
     * @param data
     * @param setDefaultValue
     */
    private void setNewParticipantApplyDateDisabled(PlanData data, 
    		boolean setDefaultValue) {
    	
    	if (setDefaultValue) {
    		data.setNewParticipantApplyDateDisabled(false);
    		return;
    	}
    	
    	// if the new participant effective date, is a past date, then
	    // the effective date text box should be disabled.
	    // PLA.886 a) 2
    	final PlanAutoContributionIncrease planAutoContributionIncrease = 
    	        	data.getPlanAutoContributionIncrease();
	    
    	if (planAutoContributionIncrease != null && 
    			data.getCsfACIOn() && 
    			StringUtils.equals(planAutoContributionIncrease.getAppliesTo(), 
    					PlanAutoContributionIncrease.APPLY_TO_PPT_NEW_PARTICIPANT_CODE)) {
	    	
			if(!ContractServiceFeatureUtil.getCurrentDate().before(
					planAutoContributionIncrease.getNewParticipantApplyDate())) {
				data.setNewParticipantApplyDateDisabled(true);
			}
		}
    }
    
    @InitBinder
    protected void initBinder(HttpServletRequest request,
 			ServletRequestDataBinder  binder) {
      binder.bind(request);
 	  binder.addValidators(psValidatorFWDefault);
 	
  }
}
