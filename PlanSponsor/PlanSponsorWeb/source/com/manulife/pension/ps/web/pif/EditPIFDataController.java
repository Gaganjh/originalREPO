package com.manulife.pension.ps.web.pif;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pif.obs.ConcreateObservable;
import com.manulife.pension.ps.web.pif.obs.ContributionsObserver;
import com.manulife.pension.ps.web.pif.obs.EligibilityObserver;
import com.manulife.pension.ps.web.pif.obs.ForfeituresObserver;
import com.manulife.pension.ps.web.pif.obs.GeneralInfoObserver;
import com.manulife.pension.ps.web.pif.obs.LoansObserver;
import com.manulife.pension.ps.web.pif.obs.MoneyTypeObserver;
import com.manulife.pension.ps.web.pif.obs.OtherPlanObserver;
import com.manulife.pension.ps.web.pif.obs.VestingObserver;
import com.manulife.pension.ps.web.pif.obs.WithdrawalsObserver;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.pif.util.PIFConstants.ProcessStatus;
import com.manulife.pension.service.pif.util.PIFDataAssembler;
import com.manulife.pension.service.pif.util.PIFDataMarshaller;
import com.manulife.pension.service.pif.valueobject.EligibilityRequirementsVO;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoSubmissionHistoryVO;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.ServiceLogRecord;


/**
 * Action that handles requests for the Edit Plan Data screen.
 * 
 * @author Rajesh Rajendran
 */
@Controller
@RequestMapping( value ="/contract")
@SessionAttributes({"pifDataForm"})

public class EditPIFDataController extends BasePIFDataController {

    @ModelAttribute("pifDataForm") 
    public PIFDataForm populateForm() 
    {
        return new PIFDataForm();
        }
    public static HashMap<String,String> forwards = new HashMap<String,String>();
    static{
        forwards.put("input","/contract/pic/editPIFData.jsp");
        forwards.put("default", "/contract/pic/editPIFData.jsp"); 
        forwards.put("cancel", "redirect:/do/contract/pic/plansubmission/");
        forwards.put("save","/contract/pic/editPIFData.jsp"); 
        forwards.put("confirm", "redirect:/do/contract/pic/confirm/");
        forwards.put("plansubmission","redirect:/do/contract/pic/plansubmission/"); 
        forwards.put("error","/contract/pic/editPIFData.jsp");
        forwards.put("homePageFinder","redirect:/do/home/homePageFinder/");
        }
    

    
    
    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(EditPIFDataController.class);
    
    private Category interactionLog = Logger.getLogger(ServiceLogRecord.class);
    
    private ServiceLogRecord logRecord = new ServiceLogRecord(EditPIFDataController.class.getName());
    
    private static final String GLOBAL_HOME_PAGE_FINDER = "homePageFinder";  
    
    public final static int DI_DURATION_24_MONTH = 24;
    
    /**
     * {@inheritDoc}
     */
    
     @RequestMapping(value ="/pic/edit/",method =  {RequestMethod.GET,RequestMethod.POST}) 
        public String doDefault(@Valid @ModelAttribute("pifDataForm") PIFDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
        throws IOException,ServletException, SystemException {
   
        logger.info("doDefault> Entry - doDefault.");
        
        if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
            }
        }
        //PIFDataForm form = (PIFDataForm) actionForm;       
        actionForm.setEditMode();
        PIFDataUi pifDataUi = null;  
        String toTab = actionForm.getToTab();

        if(StringUtils.isNotEmpty(toTab)){
            //while navigating through the tabs     
            actionForm.setSelectedTab(toTab);
            pifDataUi = actionForm.getPifDataUi();
            PlanInfoVO planInfoVO = actionForm.getPlanInfoVO();

            // Convert the types
            if(pifDataUi != null){
                pifDataUi.setSave(false);
                //Convert the bean types using observer pattern.
                ConcreateObservable observable = createObservableObjects(pifDataUi,planInfoVO);
                observable.notifyToBeanObservers(pifDataUi,planInfoVO);
                observable.notifyFromBeanObservers(pifDataUi,planInfoVO);
            }
            if(actionForm.getSubmissionId() != null){ 
                UserProfile userProfile = SessionHelper.getUserProfile(request);
                if(!userProfile.isInternalUser()) {
                    final boolean lockObtained = obtainLock(actionForm.getSubmissionId(), request);
                    if (!lockObtained) {
                        handleObtainLockFailure(actionForm.getSubmissionId(), request);
                        return forwards.get( GLOBAL_HOME_PAGE_FINDER);
                    }
                }
            }            

            //reset the toTab
            actionForm.setToTab(null);
        }else{
            //while loading the page initially
            
            pifDataUi = new PIFDataUi();
            actionForm.setPlanInfoVO(new PlanInfoVO());
            
            final UserProfile userProfile = SessionHelper.getUserProfile(request);
            //when submission id is not available
            if(actionForm.getSubmissionId() == null){  
                if(userProfile.isInternalUser()){
                    //if internal user
                    return forwards.get( GLOBAL_HOME_PAGE_FINDER);
                }else {
                    //If external user
                    return forwards.get( ACTION_FORWARD_PIF_SUBMISSION);
                }
            }
            if(!userProfile.isInternalUser()) {
                final boolean lockObtained = obtainLock(actionForm.getSubmissionId(), request);
                if (!lockObtained) {
                    handleObtainLockFailure(actionForm.getSubmissionId(), request);
                    return forwards.get( GLOBAL_HOME_PAGE_FINDER);
                }
            }
            
            PlanInfoSubmissionHistoryVO  planInfoSubmissionHistoryVO  = 
                ContractServiceDelegate.getInstance().getPIFSubmissionData(actionForm.getSubmissionId());
            
            //get the initial plan information VO and set in the PIFDataUi
            PlanInfoVO iniPlanInfoVO =  actionForm.getPlanInfoVO();
            String initialPlanInfoXMLData = planInfoSubmissionHistoryVO.getDraftPIFData();
            iniPlanInfoVO = PIFDataMarshaller.convertXMLToPIFData(initialPlanInfoXMLData); 
            pifDataUi.setInitialPlanInfoVO(iniPlanInfoVO);
            
            String planInfoXMLData = planInfoSubmissionHistoryVO.getFinalPIFData();
            PlanInfoVO planInfoVO = PIFDataMarshaller.convertXMLToPIFData(planInfoXMLData);           
            
            Integer contractId = planInfoVO.getGeneralInformations().getContractNumber();
            final Map lookupData = ContractServiceDelegate.getInstance().getLookupData(contractId);
            actionForm.setLookupData(lookupData);
            
            //On load clear the Disclaimer check box value
            planInfoVO.setAuthorizationIndicator(false);
            
            actionForm.setPlanInfoVO(planInfoVO);

            //convertFromBean(pifDataUi);
            ConcreateObservable observable = createObservableObjects(pifDataUi,planInfoVO);
            observable.notifyFromBeanObservers(pifDataUi,planInfoVO);            
            actionForm.setPifDataUi(pifDataUi);
            
            //Retrieve the acknowledgment disclosure content          
            Location location = Location.USA;
            Contract currentContract = null;
            try{
                currentContract = ContractServiceDelegate.getInstance().getContractDetails(
                    contractId, EnvironmentServiceDelegate.getInstance()
                    .retrieveContractDiDuration(userProfile.getRole(), 0, null));
            } catch (ContractNotExistException e) {
                throw new SystemException(e, "Error while retrieving contract details.");
            }
            if (GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY.equalsIgnoreCase(currentContract.getCompanyCode())){
                location = Location.NEW_YORK;
            }            
            try {               
                Content acknowledgmentDisclosureContent = ContentCacheManager.getInstance()
                    .getContentById(ContentConstants.ACKNOWLEDGEMENT_TEXT_FOR_TPA_USER,
                            ContentTypeManager.instance().DISCLAIMER, location);
                actionForm.setAcknowledgmentText(ContentUtility.getContentAttribute(acknowledgmentDisclosureContent, "text"));
            } catch (ContentException exp) {
                throw new SystemException(exp, "Error while retrieving CMA content");
            }   
            actionForm.setSelectedTab(GENERAL_INFORMATION_TAB);
        }

        // this is protect the double submissions. 
       /* if (!isTokenValid(request)) {
            saveToken(request); 
        }*/

        logger.info("doDefault> Exiting - doDefault");
        return forwards.get( ACTION_FORWARD_DEFAULT);
 
    }
    
    /**
     * 
     * This method is used to process adding the plan information form. It access the CSDB and populates
     * values to plan information form.
     *  
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */ 
    @RequestMapping(value ="/pic/edit/", params={"action=addPlanInfo"} , method =  {RequestMethod.POST}) 
    public String doAddPlanInfo(@Valid @ModelAttribute("pifDataForm") PIFDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {

        if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
            }
        }
        
        logger.debug("entry -> doAddPlanInfo");

        //PIFDataForm form = (PIFDataForm) actionForm;
        actionForm.setEditMode();
        Integer contractId = actionForm.getContractId();
        
        final Map lookupData = ContractServiceDelegate.getInstance()
            .getLookupData(contractId);
        actionForm.setLookupData(lookupData);
        
        
        PIFDataUi pifDataUi = new PIFDataUi();
        actionForm.setPlanInfoVO(new PlanInfoVO());
        
        
        // get the plan data and populate it into plan information VO
        PlanData planData = ContractServiceDelegate.getInstance().readPlanData(contractId);
        
        PlanInfoVO iniPlanInfoVO =  actionForm.getPlanInfoVO();
        iniPlanInfoVO = PIFDataAssembler.getInstance().papulatePIFDataFromPlanData(
                iniPlanInfoVO, planData, lookupData);
        // Populate TPA details
        iniPlanInfoVO.getGeneralInformations().setTpaFirmId(actionForm.getTpaFirmId());
        iniPlanInfoVO.getGeneralInformations().setTpaFirmName(actionForm.getTpaFirmName());
        iniPlanInfoVO.getGeneralInformations().setContractName(actionForm.getContractName());
        
        actionForm.setInitialPlanInfoVO(iniPlanInfoVO);
        
        String planInfoXMLData = PIFDataMarshaller.convertPIFDataToXMLData(iniPlanInfoVO);
        PlanInfoVO planInfoVO = PIFDataMarshaller.convertXMLToPIFData(planInfoXMLData);
        
        actionForm.setPlanInfoVO(planInfoVO);
        
        prepareLookupData(lookupData, planData);
        
        //convertFromBean(pifDataUi);
        pifDataUi.setInitialPlanInfoVO(iniPlanInfoVO);
        ConcreateObservable observable = createObservableObjects(pifDataUi,planInfoVO);
        observable.notifyFromBeanObservers(pifDataUi,planInfoVO);
        
        actionForm.setSelectedTab(GENERAL_INFORMATION_TAB);
        actionForm.setPifDataUi(pifDataUi);
        
        //Retrieve the acknowledgment disclosure content          
        Location location = Location.USA;
        Contract currentContract = null;
        try{
            UserProfile userProfile = SessionHelper.getUserProfile(request);
            currentContract = ContractServiceDelegate.getInstance().getContractDetails(
                contractId,EnvironmentServiceDelegate.getInstance()
                .retrieveContractDiDuration(userProfile.getRole(), 0, null));
        } catch (ContractNotExistException e) {
            throw new SystemException(e, "Error while retrieving contract details.");
        }
        
        if (GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY.equalsIgnoreCase(currentContract.getCompanyCode())){
            location = Location.NEW_YORK;
        }            
        try {               
            Content acknowledgmentDisclosureContent = ContentCacheManager.getInstance()
                .getContentById(ContentConstants.ACKNOWLEDGEMENT_TEXT_FOR_TPA_USER,
                        ContentTypeManager.instance().DISCLAIMER, location);
            actionForm.setAcknowledgmentText(ContentUtility.getContentAttribute(acknowledgmentDisclosureContent, "text"));
        } catch (ContentException exp) {
            throw new SystemException(exp, "Error while retrieving CMA content");
        } 
        
        String forward = forwards.get(ACTION_FORWARD_DEFAULT);

        // this is protect the double submissions. 
       /* if (!isTokenValid(request)) {
            saveToken(request); 
        }*/
        logger.debug("exit -> doAddPlanInfo");
        
        return forward; 
        
    }   
   
    /**
     * 
     * This method is used to process cancel the plan information changes, 
     * that undo all updates made on the plan information draft by the TPA user since the last save. 
     * And the TPA user will be taken to the Plan Information landing page.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */ 
    @RequestMapping(value ="/pic/edit/", params={"actionLabel=cancel & exit"} , method =  {RequestMethod.POST}) 
    public String doCancelAndExit(@Valid @ModelAttribute("pifDataForm") PIFDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
   
        if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
            }
        }
        
        logger.debug("entry -> doCancelAndExit");
        //PIFDataForm form = (PIFDataForm) actionForm;
        
        if(actionForm.getSubmissionId() != null){
            releaseLock(actionForm.getSubmissionId(), request);
        }
        
        //reset the toTab
        actionForm.setToTab(null);
        
        String forward = forwards.get(ACTION_FORWARD_CANCEL);

        logger.debug("exit -> doCancelAndExit");
        return forward; 
        
    }     
    
    /**
     * 
     * This method is used to process saving the plan information form 
     * that save all updates made on the plan information draft by the TPA user to the database. 
     * And the TPA user will be taken to the Plan Information landing page.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */ 
    @RequestMapping(value ="/pic/edit/", params={"actionLabel=save & exit"} , method =  {RequestMethod.POST}) 
    public String doSaveAndExit(@Valid @ModelAttribute("pifDataForm") PIFDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    
        if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
            }
        }
        
        logger.debug("entry -> doSaveAndExit");
        
        String forward = null;
        List<GenericException> errors = new ArrayList<GenericException>();      

        UserProfile userProfile = SessionHelper.getUserProfile(request);
        String profileId = String.valueOf(userProfile.getPrincipal().getProfileId());
        //PIFDataForm form = (PIFDataForm) actionForm;
        Integer submissionId = actionForm.getSubmissionId();        
        String fromTab = actionForm.getFromTab();
        if(StringUtils.isNotEmpty(fromTab)){
            actionForm.setSelectedTab(fromTab);
        }else{
            actionForm.setSelectedTab(GENERAL_INFORMATION_TAB);
        }
        
        PIFDataUi pifDataUi = actionForm.getPifDataUi();
        PlanInfoVO planInfoVO = actionForm.getPlanInfoVO();
        
        // Convert to PlanInfoVO bean types
        pifDataUi.setSave(false);
        ConcreateObservable observable = createObservableObjects(pifDataUi,planInfoVO);
        observable.notifyToBeanObservers(pifDataUi,planInfoVO);
        
        // get the plan data and save it to DB
        planInfoVO =  actionForm.getPlanInfoVO();
        PlanInfoSubmissionHistoryVO submissionHistory = new PlanInfoSubmissionHistoryVO();
        
        String planInfoXMLData = PIFDataMarshaller.convertPIFDataToXMLData(planInfoVO);
        submissionHistory.setFinalPIFData(planInfoXMLData);
        submissionHistory.setContractNumber(planInfoVO.getGeneralInformations().getContractNumber());
        submissionHistory.setCreatedUserProfileId(profileId);
        submissionHistory.setLastUpdatedUserProfileId(profileId);
        submissionHistory.setLastSubmittedUserProfileId(profileId);
        submissionHistory.setProcessStatusCode(ProcessStatus.DRAFT.getCode());
        
        if(submissionId==null){   
            Integer contractId = actionForm.getContractId();
            String userName = ContractServiceDelegate.getInstance().checkSubmissionForContract(contractId);
            if(userName == null){
                String iniPlanInfoXMLData = PIFDataMarshaller.convertPIFDataToXMLData(actionForm.getInitialPlanInfoVO());
                submissionHistory.setDraftPIFData(iniPlanInfoXMLData);          
                submissionId = ContractServiceDelegate.getInstance().insertPIFData(submissionHistory);
                // After saving set the submission Id in form to avoid the duplicate insertion 
                actionForm.setSubmissionId(submissionId);               
            }else{
                // Show error message.
                Object[] params = {userName};
                errors.add(new GenericException(ErrorCodes.DRAFT_IS_CURRENTLY_BEING_EDITED,params));  
                if (errors.size() > 0) {
                    request.setAttribute(Environment.getInstance().getErrorKey(), errors);
                }  
                return forwards.get(ACTION_FORWARD_DEFAULT); 
            }

        } else {
            //check for lock
            final boolean lockObtained = obtainLock(submissionId, request);
            if (!lockObtained) {
                handleObtainLockFailure(submissionId, request);
                return forwards.get( GLOBAL_HOME_PAGE_FINDER);
            }
            submissionHistory.setSubmissionId(submissionId);
            ContractServiceDelegate.getInstance().updatePIFData(submissionHistory);
        }
        
        actionForm.setDirty("false");
        
        // Build the log data
        StringBuffer logData = new StringBuffer();
        logData.append(CONTRACT_NUMBER_LOG_TEXT);
        logData.append(":");
        logData.append(planInfoVO.getGeneralInformations().getContractNumber());
        logData.append(":");
        logData.append(PLAN_INFORMATION_CHANGE_LOG_TEXT);
        
        // Log the save action
        logWebActivity(this.getClass().getName(), "doSaveExit", "insertPIFData",
                logData.toString(), getUserProfile(request), logger,
                interactionLog, logRecord);
        
        //release the obtained lock
        releaseLock(submissionId, request);
        
        // reset the token, as we forward to a JSP 
        //saveToken(request);
        
        //reset the toTab
        actionForm.setToTab(null);
        forward = forwards.get(ACTION_FORWARD_PIF_SUBMISSION);

        logger.debug("exit -> doSaveAndExit");
        return forward; 
        
    }     
    
    
    /**
     * 
     * This method is used to process saving the plan information form 
     * that save all updates made on the plan information draft by the TPA user to the database. 
     * And the TPA user will continue to remain on the same tab.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */ 
    @RequestMapping(value ="/pic/edit/", params={"actionLabel=save"} , method =  {RequestMethod.POST}) 
    public String doSave(@Valid @ModelAttribute("pifDataForm") PIFDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    
    
    
        if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
            }
        }
        
        
        
        logger.debug("entry -> doSave");
        String forward = null;
        List<GenericException> errors = new ArrayList<GenericException>();          
        
        UserProfile userProfile = SessionHelper.getUserProfile(request);
        String profileId = String.valueOf(userProfile.getPrincipal().getProfileId());
        
        //PIFDataForm form = (PIFDataForm) actionForm;
        Integer submissionId = actionForm.getSubmissionId();
        String fromTab = actionForm.getFromTab();
        if(StringUtils.isNotEmpty(fromTab)){
            actionForm.setSelectedTab(fromTab);
        }else{
            actionForm.setSelectedTab(GENERAL_INFORMATION_TAB);
        }
        
        PIFDataUi pifDataUi = actionForm.getPifDataUi();
        
        // Convert to PlanInfoVO bean types
        pifDataUi.setSave(false);
        ConcreateObservable observable = createObservableObjects(pifDataUi,actionForm.getPlanInfoVO());
        observable.notifyToBeanObservers(pifDataUi,actionForm.getPlanInfoVO());
        
        // get the plan data and save it to DB
        PlanInfoVO planInfoVO =  actionForm.getPlanInfoVO();
        PlanInfoSubmissionHistoryVO submissionHistory = new PlanInfoSubmissionHistoryVO();
        
        String planInfoXMLData = PIFDataMarshaller.convertPIFDataToXMLData(planInfoVO);
        submissionHistory.setFinalPIFData(planInfoXMLData);
        submissionHistory.setContractNumber(planInfoVO.getGeneralInformations().getContractNumber());
        submissionHistory.setCreatedUserProfileId(profileId);
        submissionHistory.setLastUpdatedUserProfileId(profileId);
        submissionHistory.setLastSubmittedUserProfileId(profileId);
        submissionHistory.setProcessStatusCode(ProcessStatus.DRAFT.getCode());
        
        if(submissionId==null){
            Integer contractId = actionForm.getContractId();
            String userName = ContractServiceDelegate.getInstance().checkSubmissionForContract(contractId);
            if(userName == null){
                String iniPlanInfoXMLData = PIFDataMarshaller.convertPIFDataToXMLData(actionForm.getInitialPlanInfoVO());
                submissionHistory.setDraftPIFData(iniPlanInfoXMLData);
                submissionId = ContractServiceDelegate.getInstance().insertPIFData(submissionHistory);
                // After saving set the submission Id in form to avoid the duplicate insertion 
                actionForm.setSubmissionId(submissionId);               
            }else{
                // Show error message.
                Object[] params = {userName};
                errors.add(new GenericException(ErrorCodes.DRAFT_IS_CURRENTLY_BEING_EDITED,params));  
                if (errors.size() > 0) {
                    request.setAttribute(Environment.getInstance().getErrorKey(), errors);
                }                
                return forwards.get(ACTION_FORWARD_DEFAULT);                
            }

        } else {
            //check for lock
            final boolean lockObtained = obtainLock(submissionId, request);
            if (!lockObtained) {
                handleObtainLockFailure(submissionId, request);
                return forwards.get( GLOBAL_HOME_PAGE_FINDER);
            }
            
            submissionHistory.setSubmissionId(submissionId);
            ContractServiceDelegate.getInstance().updatePIFData(submissionHistory);
        }
        
        actionForm.setDirty("false");
        
        // Build the log data
        StringBuffer logData = new StringBuffer();
        logData.append(CONTRACT_NUMBER_LOG_TEXT);
        logData.append(":");
        logData.append(planInfoVO.getGeneralInformations().getContractNumber());
        logData.append(":");
        logData.append(PLAN_INFORMATION_CHANGE_LOG_TEXT);
        
        // Log the save action
        logWebActivity(this.getClass().getName(), "doSave", "insertPIFData",
                logData.toString(), getUserProfile(request), logger,
                interactionLog, logRecord);
                
        // Convert to PIFDataForm bean types
        observable.notifyFromBeanObservers(pifDataUi,actionForm.getPlanInfoVO());
        
        // reset the token, as we forward to a JSP 
        //saveToken(request);
        
        forward = forwards.get(ACTION_FORWARD_SAVE);
        
        logger.debug("exit -> doSave");
        return forward; 
        
    } 
    
    /**
     * 
     * This method is used to process submit the plan information form 
     * that save all updates made on the plan information draft by the TPA user to the database.
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */ 
    @RequestMapping(value ="/pic/edit/", params={"actionLabel=submit"} , method =  {RequestMethod.POST}) 
    public String doSubmit(@Valid @ModelAttribute("pifDataForm") PIFDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
   
        
        if(bindingResult.hasErrors()){
            String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
            if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
            }
        }
        // this is protect the double submit. If double submit happens,
        // then redirect to Edit Page
       /* if (!(isTokenValid(request))) {
            return forwards.get(ACTION_FORWARD_DEFAULT);
        }*/
        
        logger.debug("entry -> doSubmit");
        String forward = null;
        List<GenericException> errors = new ArrayList<GenericException>();          
        
        UserProfile userProfile = SessionHelper.getUserProfile(request);
        String profileId = String.valueOf(userProfile.getPrincipal().getProfileId());
        
        //PIFDataForm form = (PIFDataForm) actionForm;
        Integer submissionId = actionForm.getSubmissionId();
        String fromTab = actionForm.getFromTab();
        if(StringUtils.isNotEmpty(fromTab)){
            actionForm.setSelectedTab(fromTab);
        }else{
            actionForm.setSelectedTab(GENERAL_INFORMATION_TAB);
        }
        
        PIFDataUi pifDataUi = actionForm.getPifDataUi();
        
        // Convert to PlanInfoVO bean types
        pifDataUi.setSave(true);
        ConcreateObservable observable = createObservableObjects(pifDataUi,actionForm.getPlanInfoVO());
        observable.notifyToBeanObservers(pifDataUi,actionForm.getPlanInfoVO());
        
        PlanInfoVO planInfoVO = actionForm.getPlanInfoVO();
        
        //set Part Time Eligibility true when Hours of service credit method is true for EEDEF and
        // EEROT 
        for (EligibilityRequirementsVO eligibilityRequirements : planInfoVO.getEligibility()
                .getEligibilityRequirements()) {
        	if(planInfoVO.getGeneralInformations().isIs457Plan()) {
        		eligibilityRequirements.setPartTimeEligibilityIndicator(null);
        	}else {
        		if ((StringUtils.equals("EEDEF", eligibilityRequirements.getMoneyTypeId())
                    || StringUtils.equals("EEROT", eligibilityRequirements.getMoneyTypeId()))
                    && (!eligibilityRequirements.getImmediateEligibilityIndicator() && StringUtils
                            .equals("H", eligibilityRequirements.getServiceCreditingMethod()))) {
                eligibilityRequirements.setPartTimeEligibilityIndicator(true);
        		}
        	}
        }
        
        // get the plan data and save it to DB
        PlanInfoSubmissionHistoryVO submissionHistory = new PlanInfoSubmissionHistoryVO();
        
        String planInfoXMLData = PIFDataMarshaller.convertPIFDataToXMLData(planInfoVO);
        submissionHistory.setFinalPIFData(planInfoXMLData);
        submissionHistory.setContractNumber(planInfoVO.getGeneralInformations().getContractNumber());
        submissionHistory.setCreatedUserProfileId(profileId);
        submissionHistory.setLastUpdatedUserProfileId(profileId);
        submissionHistory.setLastSubmittedUserProfileId(profileId);
        submissionHistory.setProcessStatusCode(ProcessStatus.SUBMITTED.getCode());
        
        if(submissionId==null){
            Integer contractId = actionForm.getContractId();
            String userName = ContractServiceDelegate.getInstance().checkSubmissionForContract(contractId);
            if(userName == null){
                String iniPlanInfoXMLData = PIFDataMarshaller.convertPIFDataToXMLData(actionForm.getInitialPlanInfoVO());
                submissionHistory.setDraftPIFData(iniPlanInfoXMLData);
                submissionId = ContractServiceDelegate.getInstance().insertPIFData(submissionHistory);
                // After saving set the submission Id in form to avoid the duplicate insertion 
                actionForm.setSubmissionId(submissionId);               
            }else{
                // Show error message.
                Object[] params = {userName};
                errors.add(new GenericException(ErrorCodes.DRAFT_IS_CURRENTLY_BEING_EDITED,params));  
                if (errors.size() > 0) {
                    request.setAttribute(Environment.getInstance().getErrorKey(), errors);
                }                
                return forwards.get(ACTION_FORWARD_DEFAULT);                
            }            
        } else {
            //check for lock
            final boolean lockObtained = obtainLock(submissionId, request);
            if (!lockObtained) {
                handleObtainLockFailure(submissionId, request);
                return forwards.get( GLOBAL_HOME_PAGE_FINDER);
            }
            
            submissionHistory.setSubmissionId(submissionId);
            ContractServiceDelegate.getInstance().updatePIFData(submissionHistory);
        }
        
        actionForm.setDirty("false");
        
        // Build the log data
        StringBuffer logData = new StringBuffer();
        logData.append(CONTRACT_NUMBER_LOG_TEXT);
        logData.append(":");
        logData.append(planInfoVO.getGeneralInformations().getContractNumber());
        logData.append(":");
        logData.append(PLAN_INFORMATION_CHANGE_LOG_TEXT);
        
        // Log the save action
        logWebActivity(this.getClass().getName(), "doSubmit", "insertPIFData",
                logData.toString(), getUserProfile(request), logger,
                interactionLog, logRecord);
        
        //release the obtained lock
        releaseLock(submissionId, request); 
        
        // Convert to PIFDataForm bean types
        observable.notifyFromBeanObservers(pifDataUi,actionForm.getPlanInfoVO());
            
        
        //resetToken(request);
        
        forward = forwards.get(ACTION_FORWARD_CONFIRM);

        logger.debug("exit -> doSubmit");
        return forward; 
        
    }   
    
    /**
     * Create the observable and observer objects.
     * 
     * @param PIFDataUi
     * @return ConcreateObservable
     */
    private ConcreateObservable createObservableObjects(PIFDataUi pifDataUi,PlanInfoVO planinfoVO){
        ConcreateObservable observable = new ConcreateObservable();
        //List<PIFMoneyType> list = pifDataUi.getPermittedMoneyTypes();
        List<PIFMoneyType> permittedMoneyTypes = new ArrayList<PIFMoneyType>();
        permittedMoneyTypes.addAll(planinfoVO.getPifMoneyType().getPermittedEmployeeMoneyTypes());
        permittedMoneyTypes.addAll(planinfoVO.getPifMoneyType().getPermittedRolloverMoneyTypes());
        permittedMoneyTypes.addAll(planinfoVO.getPifMoneyType().getPermittedEmployerMoneyTypes());
        
        //set all the initial values to Observers
        GeneralInfoObserver generalInfoObserver = new GeneralInfoObserver(planinfoVO.getGeneralInformations());
        MoneyTypeObserver moneyTypeObserver=new MoneyTypeObserver(planinfoVO.getPifMoneyType());
        ContributionsObserver contributionsObserver = new ContributionsObserver(planinfoVO.getContributions());
        LoansObserver loansObserver = new LoansObserver(planinfoVO.getLoans());
        EligibilityObserver eligibilityObserver = new EligibilityObserver(planinfoVO.getEligibility());
        ForfeituresObserver forfeituresObserver = new ForfeituresObserver(planinfoVO.getForfeitures());
        VestingObserver vestingObserver = new VestingObserver(planinfoVO.getVesting());
        WithdrawalsObserver withdrawalsObserver = new WithdrawalsObserver(planinfoVO.getWithdrawals());
        OtherPlanObserver otherPlanObserver = new OtherPlanObserver(planinfoVO.getOtherInformation());
        
        //Add observers to observable
        observable.addObserver(generalInfoObserver);
        observable.addObserver(moneyTypeObserver);
        observable.addObserver(contributionsObserver);
        observable.addObserver(loansObserver); 
        observable.addObserver(eligibilityObserver); 
        observable.addObserver(forfeituresObserver); 
        observable.addObserver(vestingObserver); 
        observable.addObserver(withdrawalsObserver); 
        observable.addObserver(otherPlanObserver); 
        
        // invoke the update method to update the values based on List<PIFMoneyType> list
        observable.update(permittedMoneyTypes);
                        
        // Once updated get all the values back and set to main VO
        planinfoVO.setGeneralInformations(generalInfoObserver.getGeneralInformations());
        planinfoVO.setPifMoneyType(moneyTypeObserver.getPlanInfoMoneyTypeVO());
        planinfoVO.setContributions(contributionsObserver.getContributions());
        planinfoVO.setEligibility(eligibilityObserver.getEligibility());
        planinfoVO.setVesting(vestingObserver.getPlanInfoVestingVO());
        planinfoVO.setForfeitures(forfeituresObserver.getForfeitures());
        planinfoVO.setWithdrawals(withdrawalsObserver.getPlanInfoWithdrawalsVO());
        planinfoVO.setLoans(loansObserver.getLoans());
        planinfoVO.setOtherInformation(otherPlanObserver.getPlanOtherInfoVO()); 
        
        return observable;
    }
    /* (non-Javadoc)
     *  This code has been changed and added to validate form and request against penetration attack, prior to other validations.
     *  
     * @see com.manulife.pension.platform.web.controller.BaseAction#doValidate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
     */

    @Autowired
    private PSValidatorFWInput psValidatorFWInput;

    @InitBinder
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.bind(request);
        binder.addValidators(psValidatorFWInput);
    }
}
