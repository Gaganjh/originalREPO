package com.manulife.pension.ps.web.plandata;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
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

import com.manulife.pension.common.MessageCategory;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.NoticeServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.contract.csf.CsfConstants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.plandata.util.TPAPlanDataWebUtility;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWError;
import com.manulife.pension.service.common.cache.NoticeLookup;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.service.notices.valueobject.AutomaticContributionVO;
import com.manulife.pension.service.notices.valueobject.ContributionsAndDistributionsVO;
import com.manulife.pension.service.notices.valueobject.InvestmentInformationVO;
import com.manulife.pension.service.notices.valueobject.NoticeDataTransactionHistoryVO;
import com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO;
import com.manulife.pension.service.notices.valueobject.NoticePlanDataVO;
import com.manulife.pension.service.notices.valueobject.NoticePlanVestingMTExcludeVO;
import com.manulife.pension.service.notices.valueobject.SafeHarborVO;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;


/**
 * Action that handles requests for the Edit Plan Data screen.
 * 
 * @author Dheepa Poongol
 */

@Controller
@RequestMapping(value ="/viewNoticePlanData")

public class ViewNoticePlanDataController extends PsAutoController {
	
	@ModelAttribute("planDataForm") 
	public TabPlanDataForm populateForm() 
	{ 
	return	new TabPlanDataForm();
		}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/plandata/editPlanData.jsp");
		forwards.put("default","/plandata/editPlanData.jsp");
		forwards.put("save","/plandata/editPlanData.jsp");
		forwards.put("error","/plandata/editPlanData.jsp");
		forwards.put("goToCustomizeContractSearchPage","redirect:/do/plandata/contractSearch/");
	}

    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(ViewNoticePlanDataController.class);
    public static final String ACTION_FORWARD_DEFAULT = "default";
    public static final String ACTION_FORWARD_SAVE = "save";
    public static final String ACTION_FORWARD_ERROR = "error";
    public static final String TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE = "goToCustomizeContractSearchPage";
    
    protected static final String TASK_KEY = "task";
    protected static final String DEFAULT_TASK = "default";
    protected static final String PRINT_TASK = "print";
    protected static final String PRINT_PDF_TASK = "printPDF";
    protected static final String DOWNLOAD_TASK = "download";
    protected static final String DOWNLOAD_ALL_TASK = "downloadAll";
    protected static final String INSERT = "I";
    protected static final String DELETE = "D";
    protected static final String YES = "Y";
    protected static final String NO = "N";
    protected static final String SAFE_HARBOUR = "SH";
    protected static final String QACA = "QACA";
    protected static final String EACA = "EACA";
    
    private static final String NOTICE_WRAPPER_FORM_NO = "NOTICE_WRAPPER_FORM_NO";
    private static final String AUTO_ENROLLMENT_TYPE = "AUTO_ENROLLMENT_TYPE";
    private static final String SAFE_HARBOR_TYPE = "SAFE_HARBOR_TYPE";
    private static final String NOTICE_MATCH_TYPE = "NOTICE_MATCH_TYPE";
    private static final String NOTICE_MATCH_TYPE_VALUE_1="1";
    private static final String NOTICE_MATCH_TYPE_VALUE_2="2";
    private static final String NOTICE_CONTRIBUTION_OPTION = "NOTICE_CONTRIBUTION_OPTION";
    private static final String NOTICE_STATUS_CHANGE_REASON_CODE = "NOTICE_STATUS_CHANGE_REASON_CODE";
    
    private Category interactionLog = Category.getInstance(ServiceLogRecord.class);
    private ServiceLogRecord record = new ServiceLogRecord("Save");
	private Category generalLog = Category.getInstance(getClass());
    
    /**
     * {@inheritDoc}
     */
	
	 
    @RequestMapping(value ="/planData/edit/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("planDataForm") TabPlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("error");//if input forward not //available, provided default
        	}
        }  
    	
    	Map<String, Map<String,String>> codeMap = ContractServiceDelegate.getInstance().getLookupCodes();
    	
    	BaseSessionHelper.removeErrorsInSession(request);
    	Integer contractId =null;
    	Integer contractIdInSession = null;
    
    	try{
    	   
    	    //CR024 Start
    	    List<LabelValueBean> setMatchAppliesToContribList= setMatchAppliesToContribList();
    	    form.setMatchAppliesToContribList(setMatchAppliesToContribList);
    	    //CR024 End
    	    
    	    if(request.getSession().getAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT)!=null){   	    
        	    
                contractId = Integer.valueOf((request.getSession().getAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT)).toString());
                
                String contractName = request.getSession().getAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT_NAME).toString();           
                    form.setContractId(contractId);
                    form.setContractName(contractName);
                }
                else{
                	
                    return Constants.HOMEPAGE_FINDER_FORWARD;
                }
    	    if(Constants.EXIT_BUTTON.equals(form.getButtonClicked())){
    	        form.reset( request);
    	        releaseLock(contractId, request,  LockHelper.EDIT_PLAN_DATA_PAGE);
                return forwards.get( TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE);
            }
    	    
    	   

    	    UserProfile userProfile = SessionHelper.getUserProfile(request);
            String profileID=null;
            if (userProfile != null) {
                profileID=String.valueOf(userProfile.getPrincipal().getProfileId());                
            }

    	 // if lock already exists, go to edit mode, else go to Home page.
    	    
    	/*	final boolean lockObtained = checkIfUserObtainedLockOnContract(Integer.toString(contractId), request);
    		if (!lockObtained) {
    			handleObtainLockFailure(contractId, request, LockHelper.EDIT_PLAN_DATA_PAGE);
    			return forwards.get(TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE);
    		}*/
    	    
       	 // try to obtain a lock for edit page, if lock not obtained go to view page
    	    if(!userProfile.isInternalUser()) {
    		final boolean lockObtained = obtainLock(contractId, request,LockHelper.EDIT_PLAN_DATA_PAGE);
    		if (!lockObtained) {
    			handleObtainLockFailure(contractId, request, LockHelper.EDIT_PLAN_DATA_PAGE);
    			return forwards.get(TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE);
    		}
    	    }
            form.setEditMode();
            String toTab = form.getToTab();
            if(toTab !=null && toTab!=""){
                form.setSelectedTab(toTab);
            }
            else{
                form.setSelectedTab(Constants.SUMMARY); 
            }
            
            String selectedTab = form.getSelectedTab();
            
            NoticePlanDataController noticePlanDataController = new NoticePlanDataController();
            
            
            NoticePlanCommonVO noticePlanCommonVO = null;
            if(null!=request.getSession().getAttribute(Constants.SELECTED_TPA_CONTRACT_IN_SESSION)){
            	contractIdInSession=Integer.valueOf((request.getSession().getAttribute(Constants.SELECTED_TPA_CONTRACT_IN_SESSION)).toString());
            }
            
            Map csfMap=null;
            if(csfMap == null){
				ContractServiceDelegate service = ContractServiceDelegate.getInstance();
				
				try {
					csfMap = service.getContractServiceFeatures(form.getContractId());
				} catch (ApplicationException ae) {
					throw new SystemException(ae.toString() + "EditPlanDataAction" 
							+ "doDefault" 
							+ ae.getDisplayMessage());
				}
			}
            
          //Notice Generation service - To load the CSF page with selected values
    		ContractServiceFeature noticeGenerationService = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_CD);
    		if(noticeGenerationService !=null){
    		    if(noticeGenerationService.getValue()!=null && noticeGenerationService.getValue()!=""){
    		        boolean value = ContractServiceFeature.internalToBoolean(noticeGenerationService.getValue()).booleanValue();
    		        if(value){
    		            form.setNoticeServiceInd(CsfConstants.CSF_YES);
    		        }
    		        else {
		        		String effectiveDate = noticeGenerationService.getAttributeValue("NEFD");
		        		if(effectiveDate==null || effectiveDate.trim().length()==0) {
		        			form.setNoticeServiceInd(CsfConstants.CSF_NO);
		        		}
		        		else {
		        			form.setNoticeServiceInd(CsfConstants.CSF_NO_DESELECTED);
		        		}
		        	}

    		        String noticeOption = noticeGenerationService.getAttributeValue(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_OPTCD);
    		        
    		        if(noticeOption!=null){
    		        	if(CsfConstants.NOTICE_OPT_404A5.equals(noticeOption.trim())){
    		        		form.setDisplayNoticeType("404a-5 Plan & Investment Notice Only");
        		        	form.setNoticeTypeSelected(noticeOption.trim());
        		        	
    	                }
    	                else if(CsfConstants.NOTICE_OPT_QDIA.equals(noticeOption.trim())){
    	                	form.setDisplayNoticeType("Qualified Default Investment Alternative Only");
    	                	form.setNoticeTypeSelected(noticeOption.trim());
    	                }
    	                else if(CsfConstants.NOTICE_OPT_AUTO.equals(noticeOption.trim())){
    	                	form.setDisplayNoticeType("Automatic Arrangement");
    	                	form.setNoticeTypeSelected(noticeOption.trim());
                        }
                        else if(CsfConstants.NOTICE_OPT_AUTO_QDIA.equals(noticeOption.trim())){
                        	form.setDisplayNoticeType("Automatic Arrangement with Qualified Default Investment Alternative");
                        	form.setNoticeTypeSelected(noticeOption.trim());
                        }
                        else if(CsfConstants.NOTICE_OPT_SH.equals(noticeOption.trim())){
                        	form.setDisplayNoticeType("Safe Harbor");
                        	form.setNoticeTypeSelected(noticeOption.trim());
                        }
                        else if(CsfConstants.NOTICE_OPT_SH_QDIA.equals(noticeOption.trim())){
                        	form.setDisplayNoticeType("Safe Harbor with Qualified Default Investment Alternative");
                        	form.setNoticeTypeSelected(noticeOption.trim());
                        }
    		        }
    		        else{
    		        	form.setDisplayNoticeType(null);
    		        	form.setNoticeTypeSelected(null);
    		        }
    		    } 
    		    else{
    		        form.setNoticeServiceInd(CsfConstants.CSF_NO);
    		    }
    		}
    		else{
    		    form.setNoticeServiceInd(CsfConstants.CSF_NO);
    		}
            
            if(null==request.getSession().getAttribute("noticePlanCommonVO") || 
            		!contractIdInSession.equals(contractId) ) {
            	logger.info("doDefault read notice plan common data from database ");
            	noticePlanCommonVO = ContractServiceDelegate.getInstance().readNoticePlanCommonData(form.getContractId());
            	noticePlanCommonVO.setNoticeType(form.getDisplayNoticeType());
            	request.getSession().setAttribute(Constants.SELECTED_TPA_CONTRACT_IN_SESSION,contractId );
            }
            else{
            	logger.info("doDefault read notice plan common data from session");
            	noticePlanCommonVO = (NoticePlanCommonVO) request.getSession().getAttribute("noticePlanCommonVO");
               }
            
            boolean isValidMoneyTypeSelected = false;
            List<Boolean> hasCompleteVestingSchedule = new ArrayList<Boolean>();
            
            /* add this to a method : this is done to set the collection size of setMoneyTypeExcludeObject in the form to be same
             * as the size of the Vesting schedule collection object*/
            if(null!=noticePlanCommonVO.getVestingSchedules()){
            	Collection<MoneyTypeExcludeObject> moneyTypeExcludeObjectList =  new ArrayList<MoneyTypeExcludeObject>(noticePlanCommonVO.getVestingSchedules().size());
            	for(VestingSchedule vestingSchedule : noticePlanCommonVO.getVestingSchedules()) {
            		moneyTypeExcludeObjectList.add(new MoneyTypeExcludeObject());
            		
            		if(!TPAPlanDataWebUtility.isNullOrEmpty(vestingSchedule.getVestingScheduleType())) {
        				hasCompleteVestingSchedule.add(true);
        				isValidMoneyTypeSelected = true;
        			}
        			else {
        				hasCompleteVestingSchedule.add(false);
        			}
            		
            	}
        	    form.setMoneyTypeExcludeObject(moneyTypeExcludeObjectList);
            }
            form.setShEnablePopUpForEmployerContributions(!isValidMoneyTypeSelected);
            form.setEacaEnablePopUpForEmployerContributions(!isValidMoneyTypeSelected);
            form.setQacaEnablePopUpForEmployerContributions(!isValidMoneyTypeSelected);
            form.setVestingScheduleCompletion(hasCompleteVestingSchedule);
            
            NoticePlanDataVO noticePlanDataVO = new NoticePlanDataVO();
            if(!userProfile.isInternalUser()) {
            noticePlanDataVO = ContractServiceDelegate.getInstance().readNoticePlanData(form.getContractId(), selectedTab);
            noticePlanDataController.clearTabValuesFromForm(form, selectedTab);
            form = noticePlanDataController.setValuesToForm(noticePlanDataVO, noticePlanCommonVO, form, selectedTab);
            form.setNoticePlanDataVO(noticePlanDataVO);  
            }else
            {
            	if(null==request.getSession().getAttribute("noticePlanDataVO") || 
                		!contractIdInSession.equals(contractId) ) {
                	logger.info("doDefault read notice plan from database for internal user");
                	noticePlanDataVO = ContractServiceDelegate.getInstance().readNoticePlanData(form.getContractId(), Constants.ALL_TABS);
                	noticePlanDataController.clearTabValuesFromForm(form, Constants.ALL_TABS);
                	form = noticePlanDataController.setValuesToForm(noticePlanDataVO, noticePlanCommonVO, form, Constants.ALL_TABS);
                    form.setNoticePlanDataVO(noticePlanDataVO);  
                }
                else{
                	logger.info("doDefault read notice plan data from session for internal user");
                	noticePlanDataVO = (NoticePlanDataVO) request.getSession().getAttribute("noticePlanDataVO");
                   }
            }
            if(selectedTab.equalsIgnoreCase(Constants.SAFE_HARBOR)|| selectedTab.equalsIgnoreCase(Constants.AUTOMATIC_CONTRIBUTION))
            {
            	NoticePlanDataVO contributionData = ContractServiceDelegate.getInstance().readNoticePlanData(form.getContractId(), Constants.CONTRIBUTION_AND_DISTRIBUTION);
            	 if(!TPAPlanDataWebUtility.isNull(contributionData)&&!TPAPlanDataWebUtility.isNull(contributionData.getContributionsAndDistributionsVO()))
                 {
                     form.setContriAndDistriDataCompleteInd(contributionData.getContributionsAndDistributionsVO().getDataCompleteInd());
                 }    
            }
            //form.setNoticeTypeSelected("2");
            // to display vesting table            
            List<GenericException> tabDataerrors = new ArrayList<GenericException>();
            tabDataerrors=NoticePlanDataController.validateTabData(noticePlanCommonVO,noticePlanDataVO,selectedTab, form, null );          
            if (tabDataerrors.size() > 0) {
            	logger.error("doValidate Business errors/Validation Errors in form fields");
            setErrorsInSession(request, tabDataerrors);
            }
            logger.info("doDefault> Exiting - doDefault");  
        	request.getSession().setAttribute("noticePlanCommonVO", noticePlanCommonVO);
        	request.getSession().setAttribute("noticePlanDataVO", noticePlanDataVO);
            request.getSession().setAttribute(Constants.TPA_PLAN_DATA_FORM, form);
              
            return forwards.get( ACTION_FORWARD_DEFAULT);
    	}
		catch(Exception e){
		    logger.error("doDefault method Exception"+e.getMessage());
		    throw new SystemException(e, "Error in doDefault method in EditPlanDataAction class");
		}
        
 
    }
    


	/**
     * 
     * action method to forward to view customize contract page
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * 
     * @return ActionForward
     * 
     * @throws SystemException, IOException, ServletException
     */
    
    @RequestMapping(value ="/planData/edit/", params={"action=investmentInformationSave","task=investmentInformationSave"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doInvestmentInformationSave(@Valid @ModelAttribute("planDataForm") TabPlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("error");//if input forward not //available, provided default
        	}
        }  
   
        
       
        String dirtyFlag=form.getDirty();
        final Collection<ValidationError> noChanges = new ArrayList<ValidationError>();
        UserProfile userProfile = SessionHelper.getUserProfile(request);
        Integer contractNumber = form.getContractId();
        if(!userProfile.isInternalUser()) {
    		final boolean lockObtained = obtainLock(contractNumber, request,LockHelper.EDIT_PLAN_DATA_PAGE);
    		if (!lockObtained) {
    			handleObtainLockFailure(contractNumber, request, LockHelper.EDIT_PLAN_DATA_PAGE);
    			return forwards.get(TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE);
    		}
    	    }
        if(null!=dirtyFlag&&dirtyFlag.equalsIgnoreCase(Constants.TRUE))
        {       	
       
        String buttonClicked = form.getButtonClicked();
     
        
        NoticePlanDataController  noticePlanDataController = new NoticePlanDataController();
        NoticePlanDataVO noticePlanDataVO = new NoticePlanDataVO();
        
        NoticePlanCommonVO commonVO = (NoticePlanCommonVO)request.getSession().getAttribute(Constants.NOTICE_PLAN_COMMON_VO);
        
        // validate form data
        List<GenericException> errors = new ArrayList<GenericException>();
        errors=NoticePlanDataController.validateTabData(commonVO, null, Constants.INVESTMENT_INFO, form, buttonClicked);
        final Collection<ValidationError> validationErrors = new ArrayList<ValidationError>(
                errors.size());

        for (GenericException genericException : errors) {
            if (genericException instanceof ValidationError) {
                // It's a ValidationError.
                validationErrors.add((ValidationError) genericException);
            }
        }
        final Collection<ValidationError> displayErrors = getMessasgeByCategory(validationErrors,
                MessageCategory.ERROR);
        final Collection<ValidationError> warnings = getMessasgeByCategory(
                validationErrors, MessageCategory.WARNING);
        final Collection<ValidationError> alerts = getMessasgeByCategory(validationErrors,
                MessageCategory.ALERT);
        
        if (displayErrors!=null && displayErrors.size() > 0) {
            logger.error("doInvestmentInformationSave method has errors while validating the editable fields");
            displayErrors.addAll(warnings);
            setErrorsInSession(request, displayErrors);
            form.setSelectedTab(Constants.INVESTMENT_INFO);         
            return forwards.get( ACTION_FORWARD_ERROR);           
        }
        else
        {
        	
             String profileID=null;
             if (userProfile != null) {
                 profileID=String.valueOf(userProfile.getPrincipal().getProfileId());                
             }
             if(!userProfile.isInternalUser()) {
            NoticePlanDataVO noticePlnDataVO = (NoticePlanDataVO)request.getSession().getAttribute(Constants.NOTICE_PLAN_DATA_VO);
            InvestmentInformationVO oldInvInfoVO = noticePlnDataVO.getInvestmentInformationVO();
            
            // map form data to VO          
            InvestmentInformationVO newInvInfoVO = noticePlanDataController.setFormDataToInvInfoVO(form);
            noticePlanDataVO.setInvestmentInformationVO(newInvInfoVO);
          
           
            // the noticeDataTransactionHistoryVO is created for each field updated by user and added to the collection object
            // it is sent to the save method to be updated in the transaction history table
            Collection<NoticeDataTransactionHistoryVO> historyData = new ArrayList<NoticeDataTransactionHistoryVO>();
            try {
                historyData=noticePlanDataController.compareBeans(newInvInfoVO,oldInvInfoVO,profileID,contractNumber);
            } catch (IllegalAccessException ie) {
                logger.error("doInvestmentInformationSave IllegalAccessException"+ie.getMessage());
                throw new SystemException(ie.getMessage());
            } catch (InvocationTargetException ite) {
                logger.error("doInvestmentInformationSave InvocationTargetException"+ite.getMessage());
                throw new SystemException(ite.getMessage());
            } catch (NoSuchMethodException nsme) {
                logger.error("doInvestmentInformationSave NoSuchMethodException"+nsme.getMessage());
                throw new SystemException(nsme.getMessage());
            }
            form.setSelectedTab(Constants.INVESTMENT_INFO);
            boolean saveSuccess=ContractServiceDelegate.getInstance().saveNoticePlanData(contractNumber, noticePlanDataVO, Constants.INVESTMENT_INFO, historyData);
            if(saveSuccess)
            {
                alerts.add(new ValidationError("saveInvInfoDataSuccess"
                        , ErrorCodes.DATA_SAVED_SUCCESSFULLY,
                        Type.alert));
                //clearing the data only when the save is successful 
                form = noticePlanDataController.clearInvInfoValuesFromForm(form);
            
                NoticePlanDataVO noticePlanVO = ContractServiceDelegate.getInstance().readNoticePlanData(contractNumber, form.getSelectedTab());
                logger.info("Read the latest notice plan data from database after the save and display in page");
                
                form = noticePlanDataController.setValuesToForm(noticePlanVO, commonVO, form, form.getSelectedTab()); 
                request.getSession().setAttribute(Constants.NOTICE_PLAN_DATA_VO,noticePlanVO);
                logInteraction(userProfile, ViewNoticePlanDataController.class.getName()+":doInvestmentInformationSave", "Contract: "+form.getContractId().toString()+" Selected Tab: "+form.getSelectedTab()+" Data: "+printHistoryDataVO(historyData));
            }
             }else
             {
            	 alerts.add(new ValidationError("saveInvInfoDataSuccess"
                         , ErrorCodes.ROLEPLAY_TEMP_SESSION,
                         Type.alert));
            	 form.setDirty("false");
             }
            warnings.addAll(alerts);    
            setErrorsInSession(request, warnings);
            
        }
        }else
        {
        	noChanges.add(new ValidationError("saveFailure", ErrorCodes.NO_CHANGE_ON_SAVE, Type.alert));        	
            setErrorsInSession(request, noChanges);
           	form.setSelectedTab(Constants.INVESTMENT_INFO);        	
           	return forwards.get( ACTION_FORWARD_ERROR);
        }
        return forwards.get( ACTION_FORWARD_SAVE);        
    }
    
    /**
     * 
     * action method to save data from contributionAndDistribution tab
     * 
     * @param mapping
     * @param reportForm
     * @param request
     * @param response
     * 
     * @return ActionForward
     * 
     * @throws SystemException
     */
    
    @RequestMapping(value ="/planData/edit/" ,params={"action=contributionAndDistributionSave","task=contributionAndDistributionSave"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doContributionAndDistributionSave (@Valid @ModelAttribute("planDataForm") TabPlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {


    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("error");//if input forward not //available, provided default
        	}
        }  
    	  
    	String dirtyFlag=form.getDirty();
    	final Collection<ValidationError> noChanges = new ArrayList<ValidationError>();
    	UserProfile userProfile = SessionHelper.getUserProfile(request);
    	Integer contractNumber = form.getContractId();
    	if(!userProfile.isInternalUser()) {
    		final boolean lockObtained = obtainLock(contractNumber, request,LockHelper.EDIT_PLAN_DATA_PAGE);
    		if (!lockObtained) {
    			handleObtainLockFailure(contractNumber, request, LockHelper.EDIT_PLAN_DATA_PAGE);
    			return forwards.get(TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE);
    		}
    	}
    	if(null!=dirtyFlag&&dirtyFlag.equalsIgnoreCase(Constants.TRUE))
    	{      	

    		String buttonClicked = form.getButtonClicked();       
    		NoticePlanDataController noticePlanDataController=new NoticePlanDataController();

    		NoticePlanCommonVO noticePlanCommonVO = (NoticePlanCommonVO)request.getSession().getAttribute(Constants.NOTICE_PLAN_COMMON_VO);
    		NoticePlanDataVO noticePlanDataVO = (NoticePlanDataVO)request.getSession().getAttribute(Constants.NOTICE_PLAN_DATA_VO);


    		// validate form data editable fields
    		List<GenericException> errors = new ArrayList<GenericException>();

    		errors= NoticePlanDataController.validateTabData(noticePlanCommonVO,null,Constants.CONTRIBUTION_AND_DISTRIBUTION, form, buttonClicked);
    		final Collection<ValidationError> validationErrors =new ArrayList<ValidationError>(
    				errors.size());
    		
    		for (GenericException genericException : errors) {

    			if (genericException instanceof ValidationError) {
    				// It's a ValidationError.
    				validationErrors.add((ValidationError) genericException);
    			}
    		}

    		final Collection<ValidationError> displayErrors = getMessasgeByCategory(validationErrors,
    				MessageCategory.ERROR);
    		final Collection<ValidationError> warnings = getMessasgeByCategory(
    				validationErrors, MessageCategory.WARNING);
    		final Collection<ValidationError> alerts = getMessasgeByCategory(validationErrors,
    				MessageCategory.ALERT);

    		if (null!=displayErrors && displayErrors.size() > 0) {
    			logger.error("doContributionAndDistributionSave method errror while validating editable fields");
    			displayErrors.addAll(warnings);
    			setErrorsInSession(request, displayErrors);
    			form.setSelectedTab(Constants.CONTRIBUTION_AND_DISTRIBUTION);        	
    			return forwards.get( ACTION_FORWARD_ERROR);           
    		}else
    		{ 


    			String profileID=null;
    			if (userProfile != null) {
    				profileID=String.valueOf(userProfile.getPrincipal().getProfileId()); 				
    			}
    			if(!userProfile.isInternalUser()) {
    				ContributionsAndDistributionsVO oldContributionsAndDistributionsVO=noticePlanDataVO.getContributionsAndDistributionsVO();
    				// map form data to VO        	
    				ContributionsAndDistributionsVO newContributionsAndDistributionsVO = noticePlanDataController.setFormDataToContributionsAndDistributionsVO(form,noticePlanCommonVO);
    				noticePlanDataVO.setContributionsAndDistributionsVO(newContributionsAndDistributionsVO);           


    				// the noticeDataTransactionHistoryVO is created for each field updated by user and added to the collection object
    				// it is sent to the save method to be updated in the transaction history table
    				//NoticeDataTransactionHistoryVO noticeDataTransactionHistoryVO = new NoticeDataTransactionHistoryVO();
    				Collection<NoticeDataTransactionHistoryVO> historyData = new ArrayList<NoticeDataTransactionHistoryVO>();
    				try {
    					historyData=noticePlanDataController.compareBeans(newContributionsAndDistributionsVO,oldContributionsAndDistributionsVO,profileID,contractNumber);
    				} catch (IllegalAccessException e) {
    					logger.error("doContributionAndDistributionSave IllegalAccessException"+e.getMessage());
    				} catch (InvocationTargetException e) {
    					logger.error("doContributionAndDistributionSave InvocationTargetException"+e.getMessage());
    				} catch (NoSuchMethodException e) {
    					logger.error("doContributionAndDistributionSave NoSuchMethodException"+e.getMessage());
    				}
    				logger.info("doContributionAndDistributionSave method save tab2 data to db");
    				boolean saveSuccess=ContractServiceDelegate.getInstance().saveNoticePlanData(contractNumber, noticePlanDataVO, Constants.CONTRIBUTION_AND_DISTRIBUTION,historyData);
    				if(saveSuccess)
    				{
    					alerts.add(new ValidationError("saveSuccess"
    							, ErrorCodes.DATA_SAVED_SUCCESSFULLY,
    							Type.alert));
    					form.setSelectedTab(Constants.CONTRIBUTION_AND_DISTRIBUTION);

    					NoticePlanDataVO noticePlanVO = new NoticePlanDataVO();
    					logger.info("doDefault read notice plan data from database and display in page");
    					noticePlanVO = ContractServiceDelegate.getInstance().readNoticePlanData(contractNumber, form.getSelectedTab());      

    					form = noticePlanDataController.clearContributionsValuesFromForm(form);
    					form = noticePlanDataController.setValuesToForm(noticePlanVO, noticePlanCommonVO, form, form.getSelectedTab());
    					request.getSession().setAttribute(Constants.NOTICE_PLAN_DATA_VO,noticePlanVO);
    					logInteraction(userProfile, ViewNoticePlanDataController.class.getName()+":doContributionAndDistributionSave", "Contract: "+form.getContractId().toString()+" Selected Tab: "+form.getSelectedTab()+" Data: "+printHistoryDataVO(historyData));
    				}
    			}else
    			{
    				alerts.add(new ValidationError("saveSuccess", ErrorCodes.ROLEPLAY_TEMP_SESSION, Type.alert));
    				form.setDirty("false");
    			}
    			warnings.addAll(alerts);        
    			setErrorsInSession(request, warnings);

    		}
    	}else
    	{
    		noChanges.add(new ValidationError("saveFailure", ErrorCodes.NO_CHANGE_ON_SAVE, Type.alert));        	
    		setErrorsInSession(request, noChanges);
    		form.setSelectedTab(Constants.CONTRIBUTION_AND_DISTRIBUTION);        	
    		return forwards.get( ACTION_FORWARD_ERROR);
    	}
    	return forwards.get( ACTION_FORWARD_SAVE);
    }
    
    
    /**
     * To save the safe harbor form data into the database
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward 
     * @throws SystemException, IOException, ServletException
     * 
     */
    
    @RequestMapping(value ="/planData/edit/", params= {"action=safeHarborSave","task=safeHarborSave"},   method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSafeHarborSave (@Valid @ModelAttribute("planDataForm") TabPlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("error");//if input forward not //available, provided default
        	}
        }  
   
    	 final StopWatch stopWatch = new StopWatch();
         logger.info("doSafeHarborSave----------> Entry - starting timer.");
         stopWatch.start();
         
       
        String dirtyFlag=form.getDirty();
        UserProfile userProfile = SessionHelper.getUserProfile(request);
        Integer contractNumber = form.getContractId();
        if(!userProfile.isInternalUser()) {
    		final boolean lockObtained = obtainLock(contractNumber, request,LockHelper.EDIT_PLAN_DATA_PAGE);
    		if (!lockObtained) {
    			handleObtainLockFailure(contractNumber, request, LockHelper.EDIT_PLAN_DATA_PAGE);
    			return forwards.get(TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE);
    		}
    	    }
        final Collection<ValidationError> noChanges = new ArrayList<ValidationError>();
        if(null!=dirtyFlag&&dirtyFlag.equalsIgnoreCase(Constants.TRUE))
        {       	
       
      
        String buttonClicked = form.getButtonClicked();
        
        String[] arr = request.getParameterValues("excludedMoneyTypename");
        if(!userProfile.isInternalUser()) {
        	if(arr != null){
        		form.setExcludeCount(arr.length);
        	}
        }
        NoticePlanDataController  noticePlanDataController = new NoticePlanDataController();
        NoticePlanDataVO noticePlanDataVO = new NoticePlanDataVO();
        NoticePlanCommonVO commonVO = (NoticePlanCommonVO)request.getSession().getAttribute(Constants.NOTICE_PLAN_COMMON_VO);
        // validate form data
        List<GenericException> errors = new ArrayList<GenericException>();
        errors=NoticePlanDataController.validateTabData(commonVO, null, Constants.SAFE_HARBOR, form, buttonClicked);
        final Collection<ValidationError> validationErrors = new ArrayList<ValidationError>(
                errors.size());

        for (GenericException genericException : errors) {
            if (genericException instanceof ValidationError) {
                // It's a ValidationError.
                validationErrors.add((ValidationError) genericException);
            }
        }
        final Collection<ValidationError> displayErrors = getMessasgeByCategory(validationErrors,
                MessageCategory.ERROR);
        final Collection<ValidationError> warnings = getMessasgeByCategory(
                validationErrors, MessageCategory.WARNING);
        final Collection<ValidationError> alerts = getMessasgeByCategory(validationErrors,
                MessageCategory.ALERT);
        UserProfile userProfiles = SessionHelper.getUserProfile(request);
        int userProfileId=0;
		if (userProfiles != null) {
			userProfileId=(int) userProfiles.getPrincipal().getProfileId(); 				
		}
		
        String profileID=null;
        if (userProfile != null) {
        	profileID=String.valueOf(userProfile.getPrincipal().getProfileId());                
        }
        if (displayErrors!=null && displayErrors.size() > 0) {
            logger.error("doSafeHarborSave method has errors while validating the editable fields");
            if(userProfile.isInternalUser()) {
            	List<NoticePlanVestingMTExcludeVO> updatedExcludedMoneyTypes =new ArrayList<NoticePlanVestingMTExcludeVO>();
            	if(arr!=null){
            		for(String moneyTypeId :arr){
            			NoticePlanVestingMTExcludeVO updatedExcludedMoneyType  = new NoticePlanVestingMTExcludeVO();
            			updatedExcludedMoneyType.setContractId(contractNumber);
            			updatedExcludedMoneyType.setMoneyTypeId(moneyTypeId);
            			updatedExcludedMoneyType.setNoticeTypeCode(SAFE_HARBOUR);
            			updatedExcludedMoneyType.setExcludeInd(false);               
            			updatedExcludedMoneyType.setExcludeIndValue(YES);
            			updatedExcludedMoneyType.setUpdatedByProfileId(userProfileId);
            			updatedExcludedMoneyTypes.add(updatedExcludedMoneyType);
            		}
            		form.getNoticePlanDataVO().setNoticePlanVestingMTExcludeVOShList(updatedExcludedMoneyTypes);
            	}
            }
            displayErrors.addAll(warnings);
            setErrorsInSession(request, displayErrors);
            form.setSelectedTab(Constants.SAFE_HARBOR);         
            return forwards.get( ACTION_FORWARD_ERROR);           
        }
        else
        {   
            
    		
            List<NoticePlanVestingMTExcludeVO> updatedExcludedMoneyTypes =new ArrayList<NoticePlanVestingMTExcludeVO>();
            if(arr!=null){
                for(String moneyTypeId :arr){
                    NoticePlanVestingMTExcludeVO updatedExcludedMoneyType  = new NoticePlanVestingMTExcludeVO();
                    updatedExcludedMoneyType.setContractId(contractNumber);
                    updatedExcludedMoneyType.setMoneyTypeId(moneyTypeId);
                    updatedExcludedMoneyType.setNoticeTypeCode(SAFE_HARBOUR);
                    updatedExcludedMoneyType.setExcludeInd(false);               
                    updatedExcludedMoneyType.setExcludeIndValue(YES);
                    updatedExcludedMoneyType.setUpdatedByProfileId(userProfileId);
                    updatedExcludedMoneyTypes.add(updatedExcludedMoneyType);
                }
            }
            
            NoticePlanDataVO noticePlnDataVO = (NoticePlanDataVO)request.getSession().getAttribute(Constants.NOTICE_PLAN_DATA_VO);
            
            compareVestingScheduleDetails(noticePlnDataVO.getNoticePlanVestingMTExcludeVOShList(), updatedExcludedMoneyTypes , userProfileId );
            compareVestingScheduleDetailsToInsert(noticePlnDataVO.getNoticePlanVestingMTExcludeVOShList(),updatedExcludedMoneyTypes ,  userProfileId );
            noticePlnDataVO.setNoticePlanVestingMTExcludeVOHistoryList(updatedExcludedMoneyTypes);
            SafeHarborVO oldSafeHarborVO = noticePlnDataVO.getSafeHarborVO();
            
            // map form data to VO          
            SafeHarborVO newSafeHarborVO = noticePlanDataController.setFormDataToSafeHarborVO(form, oldSafeHarborVO);
            noticePlanDataVO.setSafeHarborVO(newSafeHarborVO);
            noticePlanDataVO.setNoticePlanVestingMTExcludeVOHistoryList(updatedExcludedMoneyTypes);
            

            // the noticeDataTransactionHistoryVO is created for each field updated by user and added to the collection object
            // it is sent to the save method to be updated in the transaction history table
            Collection<NoticeDataTransactionHistoryVO> historyData = new ArrayList<NoticeDataTransactionHistoryVO>();
            try {
                historyData=noticePlanDataController.compareBeans(newSafeHarborVO,oldSafeHarborVO,profileID,contractNumber);
            } catch (IllegalAccessException ie) {
                logger.error("doSafeHarborSave IllegalAccessException"+ie.getMessage());
                throw new SystemException(ie.getMessage());
            } catch (InvocationTargetException ite) {
                logger.error("doSafeHarborSave InvocationTargetException"+ite.getMessage());
                throw new SystemException(ite.getMessage());
            } catch (NoSuchMethodException nsme) {
                logger.error("doSafeHarborSave NoSuchMethodException"+nsme.getMessage());
                throw new SystemException(nsme.getMessage());
            }
            form.setSelectedTab(Constants.SAFE_HARBOR);
            if(!userProfile.isInternalUser()) {
            	boolean saveSuccess=ContractServiceDelegate.getInstance().saveNoticePlanData(contractNumber, noticePlanDataVO, Constants.SAFE_HARBOR, historyData);
            	if(saveSuccess)
            	{
            		alerts.add(new ValidationError("saveSHDataSuccess", ErrorCodes.DATA_SAVED_SUCCESSFULLY, Type.alert));
            		//clearing the data only when the save is successful 
            		form = noticePlanDataController.clearSHValuesFromForm(form);

            		noticePlanDataVO.setNoticePlanVestingMTExcludeVOHistoryList(null);

            		NoticePlanDataVO noticePlanVO = ContractServiceDelegate.getInstance().readNoticePlanData(contractNumber, form.getSelectedTab());
            		logger.info("read the latest notice plan data from database and display in page");

            		form.setNoticePlanDataVO(noticePlanVO);
            		form = noticePlanDataController.setValuesToForm(noticePlanVO, commonVO, form, form.getSelectedTab());
            		request.getSession().setAttribute(Constants.NOTICE_PLAN_DATA_VO,noticePlanVO);

            		logInteraction(userProfile, ViewNoticePlanDataController.class.getName()+":doSafeHarborSave", "Contract: "+form.getContractId().toString()+" Selected Tab: "+form.getSelectedTab()+" Data: "+printHistoryDataVO(historyData));
            	}
            }else
    		{
    			alerts.add(new ValidationError("saveSHDataSuccess", ErrorCodes.ROLEPLAY_TEMP_SESSION, Type.alert));
    			form.setDirty("false");
    			List <NoticePlanVestingMTExcludeVO> finalList = new ArrayList<NoticePlanVestingMTExcludeVO>();
            	for(NoticePlanVestingMTExcludeVO noticePlanVestingMTExcludeVO : form.getNoticePlanDataVO().getNoticePlanVestingMTExcludeVOHistoryList()) {
            		if(StringUtils.equals(SAFE_HARBOUR ,noticePlanVestingMTExcludeVO.getNoticeTypeCode())) {
            			if(!StringUtils.equals(noticePlanVestingMTExcludeVO.getActionType(), "D")) {
            				finalList.add(noticePlanVestingMTExcludeVO);
            			}
            			else if(StringUtils.equals(noticePlanVestingMTExcludeVO.getActionType(), "D")) {
            				finalList = removeVestingElementFromList(noticePlanVestingMTExcludeVO, finalList);
            			}
            		}
            	}
            	if(finalList!=null){
            		finalList = removeDuplicateElementsFromList(finalList);
            	}
            		form.getNoticePlanDataVO().setNoticePlanVestingMTExcludeVOShList(finalList);
    		}
            warnings.addAll(alerts); 
            setErrorsInSession(request, warnings);
        }
        stopWatch.stop();
        logger.info("doSafeHarborSave> Entry - stopping timer . for Plan Data Pen test");
        }else
        {
        		noChanges.add(new ValidationError("saveFailure", ErrorCodes.NO_CHANGE_ON_SAVE, Type.alert));        	
            setErrorsInSession(request, noChanges);
           	form.setSelectedTab(Constants.SAFE_HARBOR);        	
           	return forwards.get(ACTION_FORWARD_ERROR);
        }
        return forwards.get( ACTION_FORWARD_SAVE);
    }
    
    
    
    /**
     * To compare the vesting details and remove the unchecked items
     *  
     * @param mapping
     * @param List
     * @param List
     * @param ProfileId
     * 
     */
    private void compareVestingScheduleDetails(List<NoticePlanVestingMTExcludeVO> oldItem, List<NoticePlanVestingMTExcludeVO> newItem , int userProfileId ){

        for(NoticePlanVestingMTExcludeVO actualDetails : oldItem) {
            if(!newItem.contains(actualDetails)){
                NoticePlanVestingMTExcludeVO updatedExcludedMoneyType  = new NoticePlanVestingMTExcludeVO();
                updatedExcludedMoneyType = (NoticePlanVestingMTExcludeVO) actualDetails.clone();
                updatedExcludedMoneyType.setActionType(DELETE);
                updatedExcludedMoneyType.setExcludeIndValue(NO);                
                updatedExcludedMoneyType.setUpdatedByProfileId(userProfileId);
                newItem.add(updatedExcludedMoneyType);
            }
        }
    }
    
    
    
    /**
     * To compare the vesting details and identify the checked items
     *  
     * @param mapping
     * @param List
     * @param List
     * @param ProfileId
     * 
     */
    private void compareVestingScheduleDetailsToInsert(List<NoticePlanVestingMTExcludeVO> oldItem, List<NoticePlanVestingMTExcludeVO> newItem , int userProfileId ){
        
        List<NoticePlanVestingMTExcludeVO> modifiedVestingDetailsToInsert = new ArrayList<NoticePlanVestingMTExcludeVO>();
        for(NoticePlanVestingMTExcludeVO actualDetails : newItem) {
            if(!oldItem.contains(actualDetails)){
                NoticePlanVestingMTExcludeVO updatedExcludedMoneyType  = new NoticePlanVestingMTExcludeVO();
                updatedExcludedMoneyType = (NoticePlanVestingMTExcludeVO) actualDetails.clone();
                updatedExcludedMoneyType.setActionType(INSERT);
                updatedExcludedMoneyType.setExcludeIndValue(YES);
                updatedExcludedMoneyType.setExcludeInd(true);
                updatedExcludedMoneyType.setUpdatedByProfileId(userProfileId);
                modifiedVestingDetailsToInsert.add(updatedExcludedMoneyType);
            }
        }
        
        newItem.addAll(modifiedVestingDetailsToInsert);
    }
    
	@Autowired
	private PSValidatorFWError psValidatorFWError;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWError);
	}
    /**
     * 
     * action method to save data from contributionAndDistribution tab
     * 
     * @param mapping
     * @param reportForm
     * @param request
     * @param response
     * 
     * @return ActionForward
     * 
     * @throws SystemException
     */
	  @RequestMapping(value ="/planData/edit/",params= {"action=automaticContributionSave","task=automaticContributionSave"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	  public String doAutomaticContributionSave (@Valid @ModelAttribute("planDataForm") TabPlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	  throws IOException,ServletException, SystemException {
		  if(bindingResult.hasErrors()){
	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        	if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("error");
	        	}
	        }  
       
        String dirtyFlag=form.getDirty();
        final Collection<ValidationError> noChanges = new ArrayList<ValidationError>();
        UserProfile userProfile = SessionHelper.getUserProfile(request);
        Integer contractNumber = form.getContractId();
        if(!userProfile.isInternalUser()) {
    		final boolean lockObtained = obtainLock(contractNumber, request,LockHelper.EDIT_PLAN_DATA_PAGE);
    		if (!lockObtained) {
    			handleObtainLockFailure(contractNumber, request, LockHelper.EDIT_PLAN_DATA_PAGE);
    			return forwards.get(TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE);
    		}
    	    }
        if(null!=dirtyFlag&&dirtyFlag.equalsIgnoreCase(Constants.TRUE))
        {       	
       
        String buttonClicked = form.getButtonClicked();        
        NoticePlanDataController noticePlanDataController=new NoticePlanDataController();
       
        
        NoticePlanCommonVO noticePlanCommonVO = (NoticePlanCommonVO)request.getSession().getAttribute(Constants.NOTICE_PLAN_COMMON_VO);       
        NoticePlanDataVO noticePlanDataVO = (NoticePlanDataVO)request.getSession().getAttribute(Constants.NOTICE_PLAN_DATA_VO);
       // validate form data editable fields
        List<GenericException> errors = new ArrayList<GenericException>();
        errors= NoticePlanDataController.validateTabData(noticePlanCommonVO,null,Constants.AUTOMATIC_CONTRIBUTION, form, buttonClicked);      
        final Collection<ValidationError> validationErrors = new ArrayList<ValidationError>(
                errors.size());

        for (GenericException genericException : errors) {

            if (genericException instanceof ValidationError) {
                // It's a ValidationError.
                validationErrors.add((ValidationError) genericException);
            }
        }
        final Collection<ValidationError> displayErrors = getMessasgeByCategory(validationErrors,
                MessageCategory.ERROR);
        final Collection<ValidationError> warnings = getMessasgeByCategory(
                validationErrors, MessageCategory.WARNING);
        final Collection<ValidationError> alerts = getMessasgeByCategory(validationErrors,
                MessageCategory.ALERT);
        if (null!=displayErrors&&displayErrors.size() > 0) {
        	logger.error("doAutomaticContributionSave method errror while validating editable fields");
        	if(userProfile.isInternalUser()) {
        		List<NoticePlanVestingMTExcludeVO> updatedExcludedMoneyTypes =getExcludedVesting(request, form, contractNumber);
        		if(updatedExcludedMoneyTypes!=null){
        			if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,EACA)){
        				form.getNoticePlanDataVO().setNoticePlanVestingMTExcludeVOEacaList(updatedExcludedMoneyTypes);
        			}
        			else if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,QACA)) {
        				form.getNoticePlanDataVO().setNoticePlanVestingMTExcludeVOQacaList(updatedExcludedMoneyTypes);
        			}
        		}
        	}
        	displayErrors.addAll(warnings);
        	setErrorsInSession(request, displayErrors);
        	form.setSelectedTab(Constants.AUTOMATIC_CONTRIBUTION);          
        	return forwards.get(ACTION_FORWARD_ERROR);           
        }else
        { 
            
            UserProfile userProfiles = SessionHelper.getUserProfile(request);
            int userProfileId=0;
            if (userProfiles != null) {
                userProfileId=(int) userProfiles.getPrincipal().getProfileId();                 
            }
          
            String profileID=null;
            if (userProfile != null) {
                profileID=String.valueOf(userProfile.getPrincipal().getProfileId());                
            }
            
             String noticeTypeCode= form.getAutomaticContributionProvisionType().trim();
            List<NoticePlanVestingMTExcludeVO> updatedExcludedMoneyTypes =new ArrayList<NoticePlanVestingMTExcludeVO>();
            if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,EACA)){
            	String[] arr = request.getParameterValues("eacaExcludedMoneyTypename");          	
                if(null!=arr)   {
            for(String moneyTypeId :arr){
                NoticePlanVestingMTExcludeVO updatedExcludedMoneyType  = new NoticePlanVestingMTExcludeVO();
                updatedExcludedMoneyType.setContractId(contractNumber);
                updatedExcludedMoneyType.setMoneyTypeId(moneyTypeId);
                updatedExcludedMoneyType.setNoticeTypeCode(noticeTypeCode);
                updatedExcludedMoneyType.setExcludeInd(false);              
                updatedExcludedMoneyType.setExcludeIndValue(YES);
                updatedExcludedMoneyType.setUpdatedByProfileId(userProfileId);
                updatedExcludedMoneyTypes.add(updatedExcludedMoneyType);
            			}
                	}
            	}
            if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,QACA)){     
            	String[] arr = request.getParameterValues("qacaExcludedMoneyTypename");	
            	if(null!=arr)   {
                    for(String moneyTypeId :arr){
                        NoticePlanVestingMTExcludeVO updatedExcludedMoneyType  = new NoticePlanVestingMTExcludeVO();
                        updatedExcludedMoneyType.setContractId(contractNumber);
                        updatedExcludedMoneyType.setMoneyTypeId(moneyTypeId);
                        updatedExcludedMoneyType.setNoticeTypeCode(noticeTypeCode);
                        updatedExcludedMoneyType.setExcludeInd(false);              
                        updatedExcludedMoneyType.setExcludeIndValue(YES);
                        updatedExcludedMoneyType.setUpdatedByProfileId(userProfileId);
                        updatedExcludedMoneyTypes.add(updatedExcludedMoneyType);
                    }
                 }
            }
            
            NoticePlanDataVO noticePlnDataVO = (NoticePlanDataVO)request.getSession().getAttribute("noticePlanDataVO");
            if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,QACA)){  
            compareVestingScheduleDetails(noticePlnDataVO.getNoticePlanVestingMTExcludeVOQacaList(), updatedExcludedMoneyTypes , userProfileId );
            compareVestingScheduleDetailsToInsert(noticePlnDataVO.getNoticePlanVestingMTExcludeVOQacaList(),updatedExcludedMoneyTypes ,  userProfileId );
            }
            if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,EACA)){  
                compareVestingScheduleDetails(noticePlnDataVO.getNoticePlanVestingMTExcludeVOEacaList(), updatedExcludedMoneyTypes , userProfileId );
                compareVestingScheduleDetailsToInsert(noticePlnDataVO.getNoticePlanVestingMTExcludeVOEacaList(),updatedExcludedMoneyTypes ,  userProfileId );
                }
           
            AutomaticContributionVO oldAutomaticContributionVO=noticePlnDataVO.getAutomaticEnrollmentVO();
            form.setOldAutomaticContributionVO(oldAutomaticContributionVO);
            // map form data to VO
             AutomaticContributionVO newAutomaticContributionVO = noticePlanDataController.setFormDataToAutomaticContributionVO(form);
            //to set the updated values 
             noticePlanDataVO.setAutomaticEnrollmentVO(newAutomaticContributionVO);
             noticePlanDataVO.setNoticePlanVestingMTExcludeVOHistoryList(updatedExcludedMoneyTypes);
             
           
        // the noticeDataTransactionHistoryVO is created for each field updated by user and added to the collection object
        // it is sent to the save method to be updated in the transaction history table
        Collection<NoticeDataTransactionHistoryVO> historyData = new ArrayList<NoticeDataTransactionHistoryVO>();
        try {
            historyData=noticePlanDataController.compareBeans(newAutomaticContributionVO,oldAutomaticContributionVO,profileID,contractNumber);
        } catch (IllegalAccessException e) {
            logger.error("doAutomaticContributionSave IllegalAccessException"+e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error("doAutomaticContributionSave InvocationTargetException"+e.getMessage());
        } catch (NoSuchMethodException e) {
            logger.error("doAutomaticContributionSave NoSuchMethodException"+e.getMessage());
        }
        
        if(!userProfile.isInternalUser()) {
        logger.info("doAutomaticContributionSave method save tab4 data to db");
        boolean saveSuccess=ContractServiceDelegate.getInstance().saveNoticePlanData(contractNumber, noticePlanDataVO, Constants.AUTOMATIC_CONTRIBUTION,historyData);
        if(saveSuccess)
        {
            alerts.add(new ValidationError("saveAutoTabDataSuccess"
                    , ErrorCodes.DATA_SAVED_SUCCESSFULLY,
                    Type.alert));
        form.setSelectedTab(Constants.AUTOMATIC_CONTRIBUTION);
        NoticePlanDataVO noticePlanVO = new NoticePlanDataVO();
        form = noticePlanDataController.clearAutomaticContributionValuesFromForm(form);
        form.setDirty(Boolean.FALSE.toString());
        noticePlanDataVO.setNoticePlanVestingMTExcludeVOHistoryList(null);
        logger.info("doDefault read notice plan data from database and display in page");
        noticePlanVO = ContractServiceDelegate.getInstance().readNoticePlanData(contractNumber, form.getSelectedTab());
                
        form.setNoticePlanDataVO(noticePlanVO);  
        
        form = noticePlanDataController.setValuesToForm(noticePlanVO, noticePlanCommonVO, form, form.getSelectedTab());
        request.getSession().setAttribute(Constants.NOTICE_PLAN_DATA_VO,noticePlanVO);
        logInteraction(userProfile, ViewNoticePlanDataController.class.getName()+":doAutomaticContributionSave", "Contract: "+form.getContractId().toString()+" Selected Tab: "+form.getSelectedTab()+" Data: "+printHistoryDataVO(historyData));
        }
            }else
            {
            	alerts.add(new ValidationError("saveAutoTabDataSuccess", ErrorCodes.ROLEPLAY_TEMP_SESSION, Type.alert));
            	form.setDirty("false");
            	List <NoticePlanVestingMTExcludeVO> finalList = new ArrayList<NoticePlanVestingMTExcludeVO>();
            	//String provisionType = form.getAutomaticContributionProvisionType().trim();
            	for(NoticePlanVestingMTExcludeVO noticePlanVestingMTExcludeVO : form.getNoticePlanDataVO().getNoticePlanVestingMTExcludeVOHistoryList()) {
            		if(StringUtils.equals(form.getAutomaticContributionProvisionType() ,noticePlanVestingMTExcludeVO.getNoticeTypeCode())) {
            			if(!StringUtils.equals(noticePlanVestingMTExcludeVO.getActionType(), "D")) {
            				finalList.add(noticePlanVestingMTExcludeVO);
            			}
            			else if(StringUtils.equals(noticePlanVestingMTExcludeVO.getActionType(), "D")) {
            				finalList = removeVestingElementFromList(noticePlanVestingMTExcludeVO, finalList);
            			}
            		}
            	}
            	
            	finalList = removeDuplicateElementsFromList(finalList);
            	
            	if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,EACA)){
            		form.getNoticePlanDataVO().setNoticePlanVestingMTExcludeVOEacaList(finalList);
            	}
            	else if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,QACA)) {
            		form.getNoticePlanDataVO().setNoticePlanVestingMTExcludeVOQacaList(finalList);
            	}

            }
        warnings.addAll(alerts);        
        setErrorsInSession(request, warnings);
        }
        }else
        {
        	noChanges.add(new ValidationError("saveFailure", ErrorCodes.NO_CHANGE_ON_SAVE, Type.alert));        	
            setErrorsInSession(request, noChanges);
           	form.setSelectedTab(Constants.AUTOMATIC_CONTRIBUTION);        	
           	return forwards.get( ACTION_FORWARD_ERROR);
        }
        return forwards.get( ACTION_FORWARD_SAVE);
    }
    
    private static List<NoticePlanVestingMTExcludeVO> removeVestingElementFromList(NoticePlanVestingMTExcludeVO element, List<NoticePlanVestingMTExcludeVO> list) {
    	int count = 0;
    	for(NoticePlanVestingMTExcludeVO itr : list) {
    		if(itr.getMoneyTypeId().equals(element.getMoneyTypeId())){
    			list.remove(count);
    			return list;
    		}
    		count++;
    	}
    	return list;
    }
    
    private static List<NoticePlanVestingMTExcludeVO> removeDuplicateElementsFromList(List<NoticePlanVestingMTExcludeVO> list) {
    	List<NoticePlanVestingMTExcludeVO> finalList = new ArrayList<NoticePlanVestingMTExcludeVO>();
    	outer : for(NoticePlanVestingMTExcludeVO itr : list) {
    		for(NoticePlanVestingMTExcludeVO itrFinal : finalList) {
    			if(itr.getMoneyTypeId().equals(itrFinal.getMoneyTypeId())){
    				break outer;
    			}
    		}
    		finalList.add(itr);
    	}
    	return finalList;
    }
 
    /**
     * Gets the messages from the given collection that match the given category.
     * 
     * @param validationErrorCollection The message to search.
     * @param messageCategory The category to filter by.
     * @return Collection - The messages that match.
     */
    private Collection<ValidationError> getMessasgeByCategory(
            final Collection<ValidationError> validationErrorCollection,
            final MessageCategory messageCategory) {

        final Collection<ValidationError> result = new ArrayList<ValidationError>();

        ValidationError.Type validationErrorType;

        switch (messageCategory) {
            case ERROR:
                validationErrorType = ValidationError.Type.error;
                break;
            case WARNING:
                validationErrorType = ValidationError.Type.warning;
                break;
            case ALERT:
                validationErrorType = ValidationError.Type.alert;
                break;
            default:
                throw new RuntimeException("Unable to find a mapping for category: "
                        + messageCategory);
        } // end switch

        for (ValidationError validationError : validationErrorCollection) {
            if (validationError.getType().equals(validationErrorType)) {
                result.add(validationError);
            } // fi
        } // end for

        return result;
    }
  
    /**
     * 
     */
    @RequestMapping(value ="/planData/edit/", params= {"action=refresh","task=refresh"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	  public final String doRefresh (@Valid @ModelAttribute("planDataForm") TabPlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	  throws IOException,ServletException, SystemException {
   
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("error");//if input forward not //available, provided default
        	}
        }  
    	
    	
    	List<GenericException> tabDataerrors = new ArrayList<GenericException>();
    	NoticePlanCommonVO noticePlanCommonVO = null;
    	Integer contractId = form.getContractId();
    	Integer contractIdInSession = null;
    	
    	if(null!=request.getSession().getAttribute(Constants.SELECTED_TPA_CONTRACT_IN_SESSION)){
        	contractIdInSession=Integer.valueOf((request.getSession().getAttribute(Constants.SELECTED_TPA_CONTRACT_IN_SESSION)).toString());
        }
    	
    	if(null==request.getSession().getAttribute("noticePlanCommonVO") || 
        		(contractIdInSession!=null && !contractIdInSession.equals(contractId)) ) {
        	noticePlanCommonVO = ContractServiceDelegate.getInstance().readNoticePlanCommonData(form.getContractId());
        }
    	else{
    		noticePlanCommonVO = (NoticePlanCommonVO) request.getSession().getAttribute("noticePlanCommonVO");
    	}
    	
    	NoticePlanDataVO noticePlanDataVO = new NoticePlanDataVO();
    	if(null==request.getSession().getAttribute("noticePlanDataVO") || 
    			(contractIdInSession!=null && !contractIdInSession.equals(contractId)) ) {
    		noticePlanDataVO = ContractServiceDelegate.getInstance().readNoticePlanData(form.getContractId(), Constants.ALL_TABS);
    	}
    	else{
    		noticePlanDataVO = (NoticePlanDataVO) request.getSession().getAttribute("noticePlanDataVO");
    	}
    	
    	tabDataerrors=NoticePlanDataController.validateTabData(noticePlanCommonVO,noticePlanDataVO,form.getSelectedTab(), form, null );
        
        if (tabDataerrors.size() > 0) {
        	logger.error("doValidate Business errors/Validation Errors in form fields");
        setErrorsInSession(request, tabDataerrors);
        }
    	
        form.setDirty(Boolean.TRUE.toString());
    	return forwards.get( ACTION_FORWARD_DEFAULT);
    	
    }
    
    private void logInteraction(UserProfile userProfile, String methodName, String data) {
    	
		try {
			ServiceLogRecord record = (ServiceLogRecord) this.record.clone();
			record.setMethodName(methodName);
			record.setData(data);
			record.setDate(new Date());
			record.setPrincipalName(userProfile.getPrincipal().getUserName());
			record.setUserIdentity(String.valueOf(userProfile.getPrincipal()
					.getProfileId()));
			this.interactionLog.error(record);
		} catch (CloneNotSupportedException e) {
			this.generalLog.error(e);
			throw new EJBException(e.toString());
		}
	}
    
    private String printHistoryDataVO(Collection<NoticeDataTransactionHistoryVO> historyData) {
    	
    	StringBuffer buffer = new StringBuffer();
    	
    	for(NoticeDataTransactionHistoryVO data : historyData) {
    		buffer.append("Contract ID: "+data.getContractId()+"; ");
    		//buffer.append("Selected Tab: "+data.getSelectedTab()+"; ");
    		buffer.append("Field Name: "+data.getFieldName()+"; ");
    		buffer.append("Field Value: "+data.getFieldValue()+"; ");
    		buffer.append("Entry Type: "+data.getEntryType()+"; ");
    		buffer.append("Updated By: "+data.getUpdatedByProfileId()+"; ");
    		buffer.append("Created: "+data.getCreatedTimeStamp()+"; ");
    		buffer.append("Sequence ID: "+data.getSequenceId()+"; ");
    	}
    	
    	return buffer.toString();
    }
    
    private List<NoticePlanVestingMTExcludeVO> getExcludedVesting( HttpServletRequest request,AutoForm actionForm, Integer contractNumber){
   	 UserProfile userProfiles = SessionHelper.getUserProfile(request);
   	  TabPlanDataForm form = (TabPlanDataForm) actionForm;
        int userProfileId=0;
        if (userProfiles != null) {
            userProfileId=(int) userProfiles.getPrincipal().getProfileId();                 
        }
      
        
         String noticeTypeCode= form.getAutomaticContributionProvisionType().trim();
        List<NoticePlanVestingMTExcludeVO> updatedExcludedMoneyTypes =new ArrayList<NoticePlanVestingMTExcludeVO>();
        if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,EACA)){
        	String[] arr = request.getParameterValues("eacaExcludedMoneyTypename");          	
            if(null!=arr)   {
        for(String moneyTypeId :arr){
       	 NoticePlanVestingMTExcludeVO updatedExcludedMoneyType  = new NoticePlanVestingMTExcludeVO();
            updatedExcludedMoneyType.setContractId(contractNumber);
            updatedExcludedMoneyType.setMoneyTypeId(moneyTypeId);
            updatedExcludedMoneyType.setNoticeTypeCode(noticeTypeCode);
            updatedExcludedMoneyType.setExcludeInd(false);              
            updatedExcludedMoneyType.setExcludeIndValue(YES);
            updatedExcludedMoneyType.setUpdatedByProfileId(userProfileId);
            updatedExcludedMoneyTypes.add(updatedExcludedMoneyType);
        			}
            	}
        	}
        if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,QACA)){     
        	String[] arr = request.getParameterValues("qacaExcludedMoneyTypename");	
        	if(null!=arr)   {
                for(String moneyTypeId :arr){
                    NoticePlanVestingMTExcludeVO updatedExcludedMoneyType  = new NoticePlanVestingMTExcludeVO();
                    updatedExcludedMoneyType.setContractId(contractNumber);
                    updatedExcludedMoneyType.setMoneyTypeId(moneyTypeId);
                    updatedExcludedMoneyType.setNoticeTypeCode(noticeTypeCode);
                    updatedExcludedMoneyType.setExcludeInd(false);              
                    updatedExcludedMoneyType.setExcludeIndValue(YES);
                    updatedExcludedMoneyType.setUpdatedByProfileId(userProfileId);
                    updatedExcludedMoneyTypes.add(updatedExcludedMoneyType);
                }
             }
        }
        return updatedExcludedMoneyTypes;
        
   }
    
    //CR024 Start
    public List<LabelValueBean> setMatchAppliesToContribList() throws SystemException {
    	List<LabelValueBean> setMatchAppliesToContribList = new ArrayList<LabelValueBean>();
     	try{
    	NoticeServiceDelegate.getInstance().noticeLookUpInitilization();
    	setMatchAppliesToContribList.add(new LabelValueBean(NoticeLookup.getInstance().get(NOTICE_MATCH_TYPE,NOTICE_MATCH_TYPE_VALUE_1),NOTICE_MATCH_TYPE_VALUE_1));
    	setMatchAppliesToContribList.add(new LabelValueBean(NoticeLookup.getInstance().get(NOTICE_MATCH_TYPE,NOTICE_MATCH_TYPE_VALUE_2),NOTICE_MATCH_TYPE_VALUE_2));
    	}catch(SystemException e){
    		logger.error(e.getMessage());
    	}
		return setMatchAppliesToContribList;
	}
    //CR024 end
        
	    /*private Map<String, Map<String,String>> getLookupCodes() {
		Map<String, Map<String,String>> lookupCodes = new HashMap<String, Map<String,String>>();
		try {
			lookupCodes.put(NOTICE_WRAPPER_FORM_NO, ContractServiceDelegate.getInstance().getLookupCodes(NOTICE_WRAPPER_FORM_NO));
			lookupCodes.put(AUTO_ENROLLMENT_TYPE, ContractServiceDelegate.getInstance().getLookupCodes(AUTO_ENROLLMENT_TYPE));
			lookupCodes.put(SAFE_HARBOR_TYPE, ContractServiceDelegate.getInstance().getLookupCodes(SAFE_HARBOR_TYPE));
			lookupCodes.put(NOTICE_MATCH_TYPE, ContractServiceDelegate.getInstance().getLookupCodes(NOTICE_MATCH_TYPE));
			lookupCodes.put(NOTICE_CONTRIBUTION_OPTION, ContractServiceDelegate.getInstance().getLookupCodes(NOTICE_CONTRIBUTION_OPTION));
			lookupCodes.put(NOTICE_STATUS_CHANGE_REASON_CODE, ContractServiceDelegate.getInstance().getLookupCodes(NOTICE_STATUS_CHANGE_REASON_CODE));
		}
		catch (SystemException e) {
			logger.error(e.getMessage());
		}
		return lookupCodes;
	}*/
}
