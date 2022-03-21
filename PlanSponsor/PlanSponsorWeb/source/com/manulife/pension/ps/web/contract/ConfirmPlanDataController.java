package com.manulife.pension.ps.web.contract;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWPlanDataDefault;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.plan.valueobject.WithdrawalDistributionMethod;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * Action that handles requests for the Confirm Plan Data screen.
 * 
 * @author Andrew Dick
 */
@Controller
@RequestMapping(value="/contract")
@SessionAttributes("planDataForm")
public class ConfirmPlanDataController extends BasePlanDataController {

    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(ConfirmPlanDataController.class);
    
    private Category interactionLog = Category.getInstance(ServiceLogRecord.class);
    
    private ServiceLogRecord logRecord = new ServiceLogRecord(ConfirmPlanDataController.class.getName());
    private static final String PLANDATA_VIEW_REDIRECT =  "redirect:/do/contract/planData/view/";
    
    protected static Map<String,String> forwards = new HashMap<String,String>();
	static{
	forwards.put("default","/contract/plan/confirmPlanData.jsp");
	forwards.put("defaultForPrintFriendly", "/contract/plan/confirmPlanData.jsp?printFriendly=true"); 
	forwards.put("accept", PLANDATA_VIEW_REDIRECT);
	forwards.put("error",PLANDATA_VIEW_REDIRECT);
	forwards.put("bookmarkDetected", PLANDATA_VIEW_REDIRECT);
	forwards.put("continueEditing" , "redirect:/do/contract/planData/edit/");
	}
	
	@Autowired
    private PSValidatorFWPlanDataDefault psValidatorFWDefault;
    
    
    @ModelAttribute("planDataForm")
    public PlanDataForm planDataForm(){
    	return new PlanDataForm();
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value="/planData/confirm/", method =  RequestMethod.GET)
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
        // Use the plan data that is already stored in the form
        setPermissions(form, request);
        // Verify that user has appropriate permissions
        if (!form.getUserCanEdit()) {
        	forwards.get( ACTION_FORWARD_ERROR);
        }
        form.setConfirmMode();
        final Integer contractId = getUserProfile(request).getCurrentContract().getContractNumber();
        if ((form.getPlanDataUi() == null) || (form.getModifiedPlanDataUi() == null)) {
            // If plan data ui has not been set, this is a bookmark
            if (logger.isInfoEnabled()) {
                logger.info(new StringBuffer("doDefault> Bookmark detected for contract id [")
                        .append(contractId).append("]").toString());
            }
            forwards.get(ACTION_FORWARD_BOOKMARK_DETECTED);
        }
        final Map lookupData = ContractServiceDelegate.getInstance().getLookupData(contractId);
        form.setLookupData(lookupData);

        // Check for validation warnings (which should exist)
        final PlanData planData = form.getPlanDataUi().getPlanData();
        if (!refreshLock(planData, request)) {
            handleObtainLockFailure(planData, request);
            forwards.get(ACTION_FORWARD_ERROR);
        }
        logger.debug(new StringBuffer("doDefault> Plan data has errors [").append(
                planData.getErrorCodes()).append("] and warnings [").append(
                planData.getWarningCodes()).append("].").toString());
        logger.debug(new StringBuffer("doDefault> Modified plan data has errors [").append(
                form.getModifiedPlanDataUi().getPlanData().getErrorCodes()).append(
                "] and warnings [").append(
                form.getModifiedPlanDataUi().getPlanData().getWarningCodes()).append("].")
                .toString());
        handleBusinessErrors(request, form.getModifiedPlanDataUi().getPlanData());
        prepareLookupData(lookupData, planData);
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
     * doContinueEditing is called when the page 'continue editing' button is pressed.
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
    @RequestMapping(value="/planData/confirm/", params="actionLabel=continue editing", method = {RequestMethod.POST})
    public final String doContinueEditing(@Valid  @ModelAttribute ("planDataForm") PlanDataForm form, BindingResult bindingResult,
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
			logger.info("doContinueEditing> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        // Clear our modified version and continue using standard ui bean
        form.setPlanDataUi(form.getModifiedPlanDataUi());
        form.setModifiedPlanDataUi(null);
        final PlanDataUi planDataUi = form.getPlanDataUi();
        refreshLock(planDataUi.getPlanData(), request);
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doContinueEditing> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get(ACTION_FORWARD_CONTINUE_EDITING);
    }

    /**
     * doAccept is called when the page 'accept' button is pressed.
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
    @RequestMapping(value="/planData/confirm/", params="action=accept", method = {RequestMethod.POST})
    public final String doAccept(@Valid @ModelAttribute ("planDataForm") PlanDataForm form, BindingResult bindingResult,
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
			logger.info("doAccept> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        // Use our stored version from the edit page so that it can't have been altered
        final PlanDataUi ui = form.getModifiedPlanDataUi();
        refreshLock(ui.getPlanData(), request);
        ui.convertToBean();
        final PlanDataUi planData1 = form.getPlanDataUi();
        final PlanData planData = planData1.getPlanData();
        
		int contractId = getUserProfile(request).getCurrentContract()
				.getContractNumber();
		
		String proposalNo = getUserProfile(request).getCurrentContract()
				.getProposalNumber();
	
		Boolean newInstallmentsIndicator = form.getPlanDataUi().getPlanData()
				.getWithdrawalDistributionMethod().getInstallmentsIndicator();

		WithdrawalDistributionMethod withdrawalDistributionMethod = ContractServiceDelegate
				.getInstance().getWithdrawalDistributionMethods(contractId);
		Boolean oldInstallmentsIndicator = withdrawalDistributionMethod
				.getInstallmentsIndicator();
		//checking the old and new installment indicator status, if both are not equal installment indicator in Apollo TLP1207 

		if (!newInstallmentsIndicator.equals(oldInstallmentsIndicator)) {
			
			boolean sywInd = ContractServiceDelegate.getInstance()
					.isSystematicWithdrawalFeatureONForJTScreen(contractId);

			UserProfile userProfile = getUserProfile(request);

			Contract contract = userProfile.getCurrentContract();
			boolean isTotalCareContract = contract.isBundledGaIndicator();

			String updateSYWIndicator = null;
			if (sywInd
					&& !isTotalCareContract) {
				updateSYWIndicator = ContractServiceDelegate.getInstance()
						.updateSYWInstallmentIndicator(
								contractId,
								Integer.parseInt(proposalNo),
								planData.getWithdrawalDistributionMethod()
										.getInstallmentsIndicator());
			}
			if (null != updateSYWIndicator
					&& !updateSYWIndicator.equalsIgnoreCase("OK")) {
				final PlanData sywUpdateResult = ContractServiceDelegate
						.getInstance().validateSYWIndicatorUpdate(
								ui.getPlanData(), updateSYWIndicator);
				if (sywUpdateResult.doErrorCodesExist()) {
					// Errors exist - return to input
					handleBusinessErrors(request, sywUpdateResult);
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

        savePlanData(ui.getPlanData(), request);
        
        // Build the log data
		StringBuffer logData = new StringBuffer();
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
		
        releaseLock(ui.getPlanData(), request);
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doAccept> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forwards.get(ACTION_FORWARD_ACCEPT);
    }
    
    @InitBinder
    protected void initBinder(HttpServletRequest request,
 			ServletRequestDataBinder  binder) {
    	 binder.bind( request);
 	  binder.addValidators(psValidatorFWDefault);
 	
  }
    
}
