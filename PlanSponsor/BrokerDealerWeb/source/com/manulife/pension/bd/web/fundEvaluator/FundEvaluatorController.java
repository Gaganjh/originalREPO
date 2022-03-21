package com.manulife.pension.bd.web.fundEvaluator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.fop.apps.FOPException;
import org.apache.log4j.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Document;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.fundEvaluator.common.AssetClassForInvOption;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolGlobalData;
import com.manulife.pension.bd.web.fundEvaluator.common.FundEvaluatorProperties;
import com.manulife.pension.bd.web.fundEvaluator.processor.CoreToolProcessor;
import com.manulife.pension.bd.web.fundEvaluator.session.SessionContext;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.ContractSearchUtility;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWDefault;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.ireports.utilities.CIAFormUtil;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.platform.web.validation.rules.ContractNumberRule;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractDetailsOtherVO;
import com.manulife.pension.service.contract.valueobject.ContractVO;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;


@Controller
@RequestMapping(value ="/fundEvaluator")
@SessionAttributes({"fundEvaluatorForm"})
public class FundEvaluatorController extends FundEvaluatorBaseController {
  
	@ModelAttribute("fundEvaluatorForm") 
	public FundEvaluatorForm populateForm() 
	{
		return new FundEvaluatorForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("selectYourClient","/fundEvaluator/fundEvaluatorSelectYourClient.jsp" );
		forwards.put("selectCriteria","/fundEvaluator/fundEvaluatorSelectCriteria.jsp");
		forwards.put("narrowYourList","/fundEvaluator/fundEvaluatorNarrowYourList.jsp");
		forwards.put("investmentOptionsSelection","/fundEvaluator/fundEvaluatorInvetmentOptionSelection.jsp" );
		forwards.put("customizeReport","/fundEvaluator/fundEvaluatorCustomizeReport.jsp");
		forwards.put("invOptionDetails","/WEB-INF/fundEvaluator/fundEvaluatorInvOptionDetail.jsp" ); 
		forwards.put("udpateInvOptionDetails","/WEB-INF/fundEvaluator/fundEvaluatorFundsAfterUpdate.jsp");
		forwards.put("launchPrintWindow","/fundEvaluator/launchPrintWindow.jsp");
		forwards.put("homePage","redirect:/do/home/"); 
		forwards.put("default","redirect:/do/fundEvaluator/?action=default"); 
		forwards.put("glossary", "/fundEvaluator/glossary.jsp" );
	}

	
    private static final String REPORT_MODE = "report";
    
    private static final String IS_REPORT_GENERATED = "isReportGenerated";

    private static final String FIRM_PARTY_ID = "FirmPartyId=";
    
    private static final String SELECTED_LIFECYCLE_FAMILIES = "selectedLifeCycleFamilies"; 
    
    private static final String SELECTED_LIFESTYLE_FAMILIES = "selectedLifestyleFamilies";
    
    private static final String SELECTED_STABLE_VALUE_FUNDS = "selectedStableValueFunds"; 
    
    private static final String SELECTED_MONEY_MARKET_FUNDS = "selectedMoneyMaketFunds";
    
    private Category interactionLog = Category.getInstance(ServiceLogRecord.class);

    private ServiceLogRecord logRecord = new ServiceLogRecord("FundEvaluatorAction");
    
    public FundEvaluatorController() {
        super(FundEvaluatorController.class);
    }

    @RequestMapping(value ="/",  method =  {RequestMethod.GET,RequestMethod.POST}) 
    public String doDefault(@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("default");//if input forward not //available, provided default
    		}
    	}
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault");
        }
      
        resetForm(actionForm,request);
        request.getSession(false).removeAttribute(SELECTED_LIFECYCLE_FAMILIES);
        request.getSession(false).removeAttribute(SELECTED_LIFESTYLE_FAMILIES);
        request.getSession(false).removeAttribute(SELECTED_STABLE_VALUE_FUNDS);
        request.getSession(false).removeAttribute(SELECTED_MONEY_MARKET_FUNDS);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doDefault");
        }
        
        return doSelectYourClient( actionForm, bindingResult, request, response);
    }

   

    /** This method loads step1 page 
     * 
     * @param mapping
     * @param form
     * @param requtest
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(value ="/" ,params={"action=selectYourClient"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSelectYourClient (@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("default");//if input forward not //available, provided default
    		}
    	}
    	
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSelectYourClient");
        }
    	            
        FundEvaluatorActionHelper.populateNewClientInfromation(form, request);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doSelectYourClient");
        }
        return forwards.get(FundEvaluatorConstants.FORWARD_SELECT_YOUR_CLIENT);
    }

    /**
     * This method loads STEP 2 page 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException 
     */
    @RequestMapping(value ="/", params={"action=selectCriteria"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSelectCriteria (@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("default");//if input forward not //available, provided default
    		}
    	}
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSelectCriteria");
        }
        	
        form.setDataModified(true);
        
        List<GenericException> errors = new ArrayList<GenericException>();
        errors.addAll(validateSelectYourClient(form,request));
        if (!errors.isEmpty()) {
            setErrorsInSession(request, errors);
            form.setMsgPresent(true);                             
            FundEvaluatorActionHelper.loadFundClassAndFundMenu(form);
            return forwards.get(FundEvaluatorConstants.FORWARD_SELECT_YOUR_CLIENT);
        }
        
        FundEvaluatorActionHelper.criteriaSelectionRestoreDefaultValues(form,request);        
        String pageAction = form.getPage();
        // if user coming from next page. - i.e criteria selection page
        if (pageAction != null && !pageAction.equalsIgnoreCase(FundEvaluatorConstants.NAVIGATE_TO_PREVIOUS_PAGE)) {        	
            // coming from client info page.            
            if (errors.size() == 0) {            	
                FundEvaluatorActionHelper.loadFundClassAndFundMenu(form);
            }
            
            form.setCritCheck(null);
            form.setCritCheckSlider(null);
            form.setSliderValue(null);
            
            if(form.getLifecycleFundSuites() != null ){
            	 request.getSession(false).removeAttribute(SELECTED_LIFECYCLE_FAMILIES);
            }
            if(form.getLifestyleFundSuites() != null ){
           	 request.getSession(false).removeAttribute(SELECTED_LIFESTYLE_FAMILIES);
           }
            
            if(StringUtils.isNotBlank(form.getStableValueFunds()) ){
           	 request.getSession(false).removeAttribute(SELECTED_STABLE_VALUE_FUNDS);
           }
           if(form.getMoneyMarketFunds() != null ){
          	 request.getSession(false).removeAttribute(SELECTED_MONEY_MARKET_FUNDS);
          }
            
        }else if (pageAction != null && pageAction.equalsIgnoreCase(FundEvaluatorConstants.NAVIGATE_TO_PREVIOUS_PAGE)) {
			request.getSession(false).setAttribute(SELECTED_LIFECYCLE_FAMILIES, form.getLifecycleFundSuites());
			request.getSession(false).setAttribute(SELECTED_LIFESTYLE_FAMILIES, form.getLifestyleFundSuites());
	    	request.getSession(false).setAttribute(SELECTED_STABLE_VALUE_FUNDS, form.getStableValueFunds());
	        request.getSession(false).setAttribute(SELECTED_MONEY_MARKET_FUNDS, form.getMoneyMarketFunds());
        	       	 
        }
        FundEvaluatorActionHelper.populateNarrowYourListWithDefaultValues(form);         
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doSelectCriteria");
        }
        return forwards.get(FundEvaluatorConstants.FORWARD_SELECT_CRITERIA);

    }

   /**
    * This method loads STEP 3 page
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return ActionForward
    * @throws IOException
    * @throws ServletException
    * @throws SystemException
    */
    @RequestMapping(value ="/", params={"action=narrowYourList"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doNarrowYourList (@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("default");//if input forward not //available, provided default
    		}
    	}
    	
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> doNarrowYourList");
        }
    	
    	form.setLifecycleFundSuites((String[]) request.getSession(false).getAttribute(SELECTED_LIFECYCLE_FAMILIES));
    	form.setLifestyleFundSuites((String[]) request.getSession(false).getAttribute(SELECTED_LIFESTYLE_FAMILIES));
		// collect the user selected SVF and MMF funds to carry in navigation.
    	form.setStableValueFunds((String) request.getSession(false).getAttribute(SELECTED_STABLE_VALUE_FUNDS));
    	form.setMoneyMarketFunds((String[]) request.getSession(false).getAttribute(SELECTED_MONEY_MARKET_FUNDS));
		
        String pageAction = form.getPage();
  
        // if user coming from next page. - i.e Asset house page page
        if (pageAction != null && !pageAction.equalsIgnoreCase(FundEvaluatorConstants.NAVIGATE_TO_PREVIOUS_PAGE)) {
        	
        	 List<GenericException> errors = new ArrayList<GenericException>();
             errors.addAll(validateCriteriaSelection(form));
             if (!errors.isEmpty()) {
                 FundEvaluatorActionHelper.criteriaSelectionRestoreDefaultValues(form, request);
                 setErrorsInSession(request, errors);             
                return forwards.get(FundEvaluatorConstants.FORWARD_SELECT_CRITERIA);
             }
        }    
        FundEvaluatorActionHelper.populateNarrowYourListWithDefaultValues(form);         
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doNarrowYourList");
        }

        return forwards.get("narrowYourList");	
    }


    /**
     * Generates a Print Preview window taking HTML content from a dynamically(javascript) generated form on the Preview
     * page, and placing it in the request to be passed to a new window that pops open.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/", params={"action=launchPrintWindow"}, method =  {RequestMethod.GET,RequestMethod.POST}) 
    public String doLaunchPrintWindow ( @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	/*String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }*/
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("default");//if input forward not //available, provided default
    		}
    	}
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doLaunchPrintWindow");
        }
	//System.err.println("MADE IT");
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doLaunchPrintWindow");
        }

        return forwards.get(FundEvaluatorConstants.FORWARD_LAUNCH_PRINT_WINDOW);
    }


   /**
    * Loads STEP 4 - Investment options selection page
    * Displays the asset house class details.
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return ActionForward
    * @throws IOException
    * @throws ServletException
    * @throws SystemException
    */
    @RequestMapping(value ="/" ,params={"action=investmentOptionsSelection"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doInvestmentOptionsSelection (@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("default");//if input forward not //available, provided default
    		}
    	}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doInvestmentOptionsSelection");
        }
        FundEvaluatorForm ievaluatorForm = (FundEvaluatorForm) form; 
       
        Hashtable<String, Collection<String>> selectedAssetClsFundIds;        
        String pageAction = ievaluatorForm.getPage();
        
        // set overlay filter values to default values.
        FundEvaluatorActionHelper.resetOverlayFilterValues(ievaluatorForm);
                
       ArrayList<AssetClassForInvOption> assetClassForInvOptionList = FundEvaluatorUtility.getInvestmentOptionDetails(ievaluatorForm, request);
        if (pageAction != null && !pageAction.equalsIgnoreCase(FundEvaluatorConstants.NAVIGATE_TO_PREVIOUS_PAGE)) {
            selectedAssetClsFundIds =  FundEvaluatorActionHelper.getUserSelectedAssetClsFundsFromSession(request, true);
            FundEvaluatorActionHelper.updateToolSelectedFundsInSession(assetClassForInvOptionList, selectedAssetClsFundIds);
            // store the drop down values from form to session  
			request.getSession(false).setAttribute(SELECTED_LIFECYCLE_FAMILIES, ievaluatorForm.getLifecycleFundSuites());
			request.getSession(false).setAttribute(SELECTED_LIFESTYLE_FAMILIES, ievaluatorForm.getLifestyleFundSuites());
            request.getSession(false).setAttribute(SELECTED_STABLE_VALUE_FUNDS, ievaluatorForm.getStableValueFunds());
            request.getSession(false).setAttribute(SELECTED_MONEY_MARKET_FUNDS, ievaluatorForm.getMoneyMarketFunds());
            
            List<GenericException> errors = new ArrayList<GenericException>();
            errors.addAll(validateNarrowYourList(ievaluatorForm));              
            if (!errors.isEmpty()) {        	
                setErrorsInSession(request, errors);
                FundEvaluatorActionHelper.populateNarrowYourListWithDefaultValues(ievaluatorForm);
                
                return forwards.get(FundEvaluatorConstants.FORWARD_NARROW_YOUR_LIST);	
            }
        } else {
            selectedAssetClsFundIds =  FundEvaluatorActionHelper.getUserSelectedAssetClsFundsFromSession(request, false);
        }
       ievaluatorForm.setAssetClassDetails(FundEvaluatorUtility.initializeAssetClassDetails(assetClassForInvOptionList));
        FundEvaluatorActionHelper.updateTotalFundsSelected(request, ievaluatorForm); 
        
        // set evaluating funds company specific content in request to display on page        
        FundEvaluatorActionHelper.populateNarrowYourListWithDefaultValues(ievaluatorForm);

        // SVF competing funds used by JSP's to handle the Check boxes in the Step4
        List svfCompetingFundsList = (List) FundServiceDelegate.getInstance().getSVFCompetingFunds(Calendar.getInstance().getTime());
        request.setAttribute(BDConstants.SVF_COMPETING_FUNDS, svfCompetingFundsList); 
        
        // SVF funds used by JSP's to handle the Check boxes in the Step4
        request.setAttribute(BDConstants.SVF_FUNDS, CoreToolGlobalData.defaultSelectedStableValueFunds);
		// checks any SVF or SVF competing fund selected by user and updates the form flag values.
		FundEvaluatorActionHelper.checkStableValueAndCompetingFundRule(request, ievaluatorForm);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doInvestmentOptionsSelection");
        }
        
        String[] assetClassIds = 	{"LCV","LCB","LCG",
                 "MCV","MCB","MCG",
                 "SCV","SCB","SCG",
				 "IGV","IGB","IGG",
				 "BAL","SEC","SPE","LCF",
				 "HQS","MQI","LQS","GLS",
				 "FXI","FXM","LQI","GLI",
				 "FXL","MQL","LQL","GLL",
				 "GA3","GA5","G10","LSF","LSG"};
        
			
        java.util.HashMap assetClassMap = ievaluatorForm.getAssetClassDetails();
        java.util.List nonAvailableAssetClassList = new ArrayList();
        for(int i=0; i<assetClassIds.length;i++){
        	if(!assetClassMap.containsKey(assetClassIds[i])){
        		nonAvailableAssetClassList.add(assetClassIds[i]);
        	}
        }
        
        ievaluatorForm.setHasGuranteedAccountsFunds(hasGuranteedAccountsFund(assetClassMap, "GA3") 
       			|| hasGuranteedAccountsFund(assetClassMap, "GA5")
       			|| hasGuranteedAccountsFund(assetClassMap, "G10"));
        request.setAttribute("nonAvailableAssetClassList", nonAvailableAssetClassList);
        
        return forwards.get(FundEvaluatorConstants.FORWARD_INVESTMENT_OPTIONS_SELECTION);
    }
    
    private boolean hasGuranteedAccountsFund(HashMap assetClassMap, String assetClassId) {
    	boolean hasGuranteedAccountsFund = false;
    	
    	if (assetClassMap.containsKey(assetClassId)) {
    		AssetClassDetails assetClassDetails = (AssetClassDetails) assetClassMap.get(assetClassId);
    		if (assetClassDetails.getTotalBaseLineFunds() > 0 )
    			hasGuranteedAccountsFund = true;
    	}
    	return hasGuranteedAccountsFund;
    }

    /**
     * This method loads STEP 5 page
     * @param mapping 
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
   @RequestMapping(value ="/", params={"action=customizeReport"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doCustomizeReport (@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
	   String forward=preExecute(form, request, response);
       if ( StringUtils.isNotBlank(forward)) {
    	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
       }
	   if(bindingResult.hasErrors()){
   		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   		if(errDirect!=null){
   			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   			return forwards.get("default");//if input forward not //available, provided default
   		}
   	}
    	
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCreateReport");
        }
         
        try {
            FundEvaluatorActionHelper.populateDefaultValuesForCustomizeReport(form,request);
		} catch (SecurityServiceException e) {
			throw new SystemException(e,
                    "SecurityServiceException occurred in populateDefaultValuesForCustomizeReport() method: "
                            + e.getMessage());
		} 
		if (logger.isDebugEnabled()) {
            logger.debug("exit -> doCreateReport");
        }
        return forwards.get(FundEvaluatorConstants.FORWARD_CUSTOMIZE_REPORT);
    }

    /**
     * This method resets the FundEvaluatorForm to its default values.
     * @param mapping
     * @param fundEvaluatorForm
     * @param request
     * @return FundEvaluatorForm
     * @throws SystemException
     */    
    private FundEvaluatorForm resetForm(
    		FundEvaluatorForm fundEvaluatorForm, HttpServletRequest request)
            throws SystemException {
    	
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> resetForm");
        }
        try {
            FundEvaluatorForm blankForm = (FundEvaluatorForm) fundEvaluatorForm
                    .getClass().newInstance();
            PropertyUtils.copyProperties(fundEvaluatorForm, blankForm);
            fundEvaluatorForm.reset( request);
        } catch (Exception e) {
            throw new SystemException(e, this.getClass().getName(),
                    "resetForm", "exception in resetting the form");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> resetForm");
        }
        return fundEvaluatorForm;
    }
    
    /** This method will collects the data from the form bean to generate the report.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
      * @throws IOException
     * @throws SystemException 
     */
    
    @RequestMapping(value ="/", params={"action=generateReport"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doGenerateReport (@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("default");//if input forward not //available, provided default
    		}
    	}
    	HttpSession session = request.getSession(false);
    	session.setAttribute(IS_REPORT_GENERATED, false);
        long t1 = System.currentTimeMillis();
        boolean pdfGenerationErrorsOccured = false;
        
       
        List<GenericException> errors = new ArrayList<GenericException>();
        
        errors = validateGenerateReport(form);
        if (!errors.isEmpty()) {//if error - forward to same page with errors
            setErrorsInSession(request, errors);    
            return forwards.get(FundEvaluatorConstants.FORWARD_CUSTOMIZE_REPORT);
        }
        
        else {//if no validation errors - proceed with submit for Pdf file download
        
            try{
                BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
                
                String companyName = form.getCompanyName();                
                String filename = getFileName(companyName);
                FundEvaluatorActionHelper.setContentLocation(form, request);
//                FundEvaluatorVO fundEvaluatorVO = new FundEvaluatorVO();
//                FundEvaluatorActionHelper.populateFundEvaluatorVo(reportForm,fundEvaluatorVO);                
                ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(form, request);           
                
                logFundEvaluatorReportGeneration(form.getContractNumber(),userProfile);
                
                if(StringUtils.isNotEmpty(form.getIncludedOptItem3())
                		|| StringUtils.isNotEmpty(form.getIncludedOptItem6())){
                	
                	List<InputStream> pdfsList = new ArrayList<InputStream>();
                	
                	ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfOutStream.toByteArray());
            		pdfsList.add(inputStream);
            		
            		String password = null;
                	
            		 // Appending IPS Form
    				if(StringUtils.isNotEmpty(form.getIncludedOptItem3())){
    	                password = CIAFormUtil.getPdfFromCMA(pdfsList,
    	                		CommonContentConstants.IPS_TEMPLATE_FILE_ID, request);
                    }
    					
    				// Appending Contract investment administration form
    				if(StringUtils.isNotEmpty(form.getIncludedOptItem6())){
    	                int cmaKeyOfCIAPdf = getCIAFormCMAKey(form);
    	                Location fundEvalLocation = FundEvaluatorActionHelper.getFundEvaluationLocation(form, request);
    	                // Setting PDF based on fund evaluation location
    	                String context = StringUtils.equalsIgnoreCase(fundEvalLocation.getAbbreviation(), CommonConstants.SITEMODE_NY) ? PdfConstants.NY_CIA_FORM : PdfConstants.US_CIA_FORM ;
    	                password = CIAFormUtil.getCIAForm(pdfsList, cmaKeyOfCIAPdf, context, request);
                    }
   					 
            		pdfOutStream = CIAFormUtil.appendForms(pdfsList, password);
                }
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
                response.setContentLength(pdfOutStream.size());
                
                session.setAttribute(IS_REPORT_GENERATED, true);
                
                    ServletOutputStream sos = response.getOutputStream();
                    pdfOutStream.writeTo(sos);
                    sos.flush();
            }catch(Exception exception){
                logger.error("Error occured during doGenerateReport()",exception);
                GenericException genericException = new GenericException(
                        BDErrorCodes.INCOMPLETE_GENEREATE_REPORT);
                errors.add(genericException);
                pdfGenerationErrorsOccured = true;
                setErrorsInSession(request, errors);
            }
            finally {
                if (pdfGenerationErrorsOccured == false) {
                    try {
                        response.getOutputStream().close();
                    } catch (IOException ioException) {
                        logger.error(ioException);
                        GenericException genericException = new GenericException(
                                BDErrorCodes.INCOMPLETE_GENEREATE_REPORT);
                        errors.add(genericException);
                        pdfGenerationErrorsOccured = true;
                        setErrorsInSession(request, errors);
                    }
                long t5 = System.currentTimeMillis();
                logger.debug("#### Overall Time span = " + ((t5-t1)/1000)+" seconds");
                }
            }
            if (pdfGenerationErrorsOccured) {
                return forwards.get(FundEvaluatorConstants.FORWARD_CUSTOMIZE_REPORT);
            }
            else{
                return null;
            }
        }//end of else
    }
   
    /** This method will check whether FundEvaluator Report is generated or not.
     * 	If report generation is complete then response status code is set as 400(request fails)
     * 	This causes the AJAX request to fail and in the failure event waiting message will be closed.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException 
     */
    
    @RequestMapping(value ="/", params={ "action=checkPdfReportGenerated"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doCheckPdfReportGenerated (@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("default");//if input forward not //available, provided default
    		}
    	}
    
     
        boolean isReportGenerated = (Boolean)request.getSession(false).getAttribute(IS_REPORT_GENERATED);
        if (isReportGenerated) {
        	response.setStatus(400);        	
        }
        return null;

    }    
    
 /**
  * Retrieves the asset class details for the detailed, Lifestyle and lifecycle and preview overlay
  * @param mapping
  * @param form
  * @param request
  * @param response
  * @return ActionForward
  * @throws IOException
  * @throws ServletException
  * @throws SystemException
  */  
    @RequestMapping(value ="/", params={ "action=getInvOptionDetails"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doGetInvOptionDetails (@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("default");//if input forward not //available, provided default
    		}
    	}
        
        FundEvaluatorForm ievaluatorForm = (FundEvaluatorForm) form;
        ArrayList<AssetClassForInvOption> assetClassForInvOptionList;
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doGetInvOptionDetails");
        }
        
        String resetFilters = (String)request.getParameter(FundEvaluatorConstants.RESET_FILTERS);
        if(StringUtils.equalsIgnoreCase(resetFilters, BDConstants.YES)) {
            // reset all the filter values to default values.
            FundEvaluatorActionHelper.resetOverlayFilterValues(ievaluatorForm);
        }
        
        String assetClassId = ievaluatorForm.getAssetClassId();
        
        ievaluatorForm.setStableValueFunds((String) request.getSession(false).getAttribute(SELECTED_STABLE_VALUE_FUNDS));
		ievaluatorForm.setMoneyMarketFunds((String[]) request.getSession(false).getAttribute(SELECTED_MONEY_MARKET_FUNDS));
        
        // request for preview or asset class overlay.
        if (assetClassId != null) {
            assetClassForInvOptionList = FundEvaluatorUtility.getInvestmentOptionDetails(ievaluatorForm, request);
                //Check whether user requested for preview overlay or particular asset class funds. 
                if(FundEvaluatorActionHelper.isPreview(ievaluatorForm.getAssetClassId())){
                    // If user requested for preview, which will  all asset class funds.
                    FundEvaluatorActionHelper.updateUserSelectedFundsInAssetClsFunds(assetClassForInvOptionList, ievaluatorForm, request);
                    if(FundEvaluatorConstants.LIST_AVAILABLE_INVESTMENT_OPTIONS.equalsIgnoreCase(ievaluatorForm.getListInvesmentOptionBy())) {
                        // Lists all investment options including user selected funds.
                        request.setAttribute(BDConstants.ASSSET_CLASS_INV_OPTIONS, assetClassForInvOptionList);
                    } else {
                        // List only user selected funds.
                        request.setAttribute(BDConstants.ASSSET_CLASS_INV_OPTIONS, FundEvaluatorActionHelper.getSelectedAssetClsFundsInvOptionList(ievaluatorForm, request));
                    }
                } else {
                    ArrayList<AssetClassForInvOption> assetClasses = new ArrayList<AssetClassForInvOption>();
                    FundEvaluatorActionHelper.updateUserSelectedFundsInAssetClsFunds(assetClassForInvOptionList, ievaluatorForm, request);
                    // Requested for particular asset class funds.
                    if (FundEvaluatorConstants.ASSET_CLASS_ID_LIFESTYLE.equalsIgnoreCase(assetClassId)
                            || FundEvaluatorConstants.ASSET_CLASS_ID_GIFL_SELECT.equalsIgnoreCase(assetClassId)) {
                        assetClasses.add(FundEvaluatorActionHelper.getAssetClassForInvOption(FundEvaluatorConstants.ASSET_CLASS_ID_LIFESTYLE, assetClassForInvOptionList));
                        assetClasses.add(FundEvaluatorActionHelper.getAssetClassForInvOption(FundEvaluatorConstants.ASSET_CLASS_ID_GIFL_SELECT, assetClassForInvOptionList));
                    } else{
                        // for all other asset class funds request including Index funds
                        assetClasses.add(FundEvaluatorActionHelper.getAssetClassForInvOption(assetClassId, assetClassForInvOptionList));
                    }                    
                    request.setAttribute(BDConstants.ASSSET_CLASS_INV_OPTIONS, assetClasses);
                }
           }
        
        Location fundEvalLocation = FundEvaluatorActionHelper.getFundEvaluationLocation(ievaluatorForm, request);
        // Set fund evaluation location
        if(StringUtils.equalsIgnoreCase(fundEvalLocation.getAbbreviation(), CommonConstants.SITEMODE_NY)) {
            request.setAttribute(BDConstants.FUND_EVALUATION_LOCATION, CommonConstants.SITEMODE_NY);
        } else {
            request.setAttribute(BDConstants.FUND_EVALUATION_LOCATION, CommonConstants.SITEMODE_USA);
        }
       
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doGetInvOptionDetails");
        }
        
        return forwards.get(FundEvaluatorConstants.FORWARD_INVESTMENT_OPTION_DETAILS);
    }
                         
    /**
     * Updates the user selected funds in session.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/", params={ "action=updateInvOptionDetails"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doUpdateInvOptionDetails (@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("default");//if input forward not //available, provided default
    		}
    	}
    
   
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doUpdateInvOptionDetails");
        }

        FundEvaluatorForm ievaluatorForm = (FundEvaluatorForm) form;

        FundEvaluatorActionHelper.updateUserSelectedFundsInSession(ievaluatorForm, request);
        FundEvaluatorActionHelper.updateTotalFundsSelected(request, ievaluatorForm);
        // checks any SVF or SVF competing fund selected by user and updates the form flag values.
		FundEvaluatorActionHelper.checkStableValueAndCompetingFundRule(request, ievaluatorForm);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doUpdateInvOptionDetails");
        }
        
        return forwards.get(FundEvaluatorConstants.FORWARD_UPDATE_INV_OPTION_DETAILS);
    }
    
    /**    
     * This method validates Select your client page inputs.
     * 
     * @param form
     * @return List<ValidationError>
     * @throws SystemException
     * 
     */
    private List<GenericException> validateSelectYourClient(FundEvaluatorForm ievaluatorForm,HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validateClientInfo");
        }
        // Get the value of form property includeGIFLSelectFunds and assign it to a local variable to use further
        boolean includeGIFLSelectFunds = ievaluatorForm.isIncludeGIFLSelectFunds();
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request); 
        List<GenericException> errors = new ArrayList<GenericException>(); 
        //The next button will be enabled only if the user enters anything in the contract number field. or 
        //if the user selects the new client button. 
        
        //If  "Existing Client" tab is selected.
        //validation for valid contract number 
        if(StringUtils.isNotEmpty(ievaluatorForm.getClientType()) && 
        		StringUtils.equalsIgnoreCase(FundEvaluatorConstants.CLIENT_EXISTING,ievaluatorForm.getClientType())){
        	
        		ContractNumberRule.getInstance().validate(ievaluatorForm.getContractNumber(),
                    errors, ievaluatorForm.getContractNumber());    
        }
        
        if(StringUtils.isNotEmpty(ievaluatorForm.getClientType()) && 
                StringUtils.equalsIgnoreCase(FundEvaluatorConstants.CLIENT_NEWPLAN,ievaluatorForm.getClientType())){
            // state is required for US funds for new plan
            if (ievaluatorForm.getFundUsa().equals(FundEvaluatorConstants.US_FUNDS)){
                if(StringUtils.isEmpty(ievaluatorForm.getStateSelected())) {
                    GenericException exception = new GenericException(
                            BDErrorCodes.STATE_NOT_SELECTED);
                        errors.add(exception); 
                }
            } 
        }
        
        if(errors.size()==0 && StringUtils.isNotBlank(ievaluatorForm.getContractNumber())){ 
        	List<String> includedList = new ArrayList<String>();
        	includedList.add(BDConstants.ACTIVE_TAB_VAL);
        	includedList.add(BDConstants.PENDING_TAB_VAL);
        	includedList.add(BDConstants.DISCONTINUED_TAB_VAL);   
        	boolean includeIAStatus = true;
        	
	        List<ContractVO> resultingContractList = ContractSearchUtility.
	        				searchByContractNumber(request, userProfile, ievaluatorForm.getContractNumber(),includedList,includeIAStatus);
	      //check if the contract is in BOB
	        if(resultingContractList!=null && resultingContractList.size()==0){
	        	GenericException exception = new GenericException(
	            		BDErrorCodes.CONTRACT_NOT_IN_BOB);
	        		errors.add(exception);
	        }else{        	
	        	ContractVO contractVO = (ContractVO)resultingContractList.get(0);
	        	//check if the contract is Defined Contributions Contract
	        	if(StringUtils.isNotEmpty(contractVO.getProductType())){	
	        		if(!BDConstants.DC.equals(contractVO.getProductType())){
	        			GenericException exception = new GenericException(
	    	            		BDErrorCodes.NOT_DC_CONTRACT);
	    	        		errors.add(exception);
	        		}	 
	        	}
        		if(errors.size()== 0){//check the contract status is valid as per fund evaluator tool.
        			boolean validStatus = false;	        			
        			List<String> contractStatusVerificationList =  FundEvaluatorActionHelper.getContractStatusVerificationList();
        			Iterator<String> contractStatusVerificationListIterator = contractStatusVerificationList.iterator();	
        			Iterator<ContractVO> contractListIterator =  resultingContractList.iterator();
        			//get the contract status from the resulting list and ,compare the status with the 
        			//predefined valid contract status from the verification list(for fund evaluator).
        			while(contractListIterator.hasNext()){
        				ContractVO vo  = (ContractVO)contractListIterator.next();
        				while(contractStatusVerificationListIterator.hasNext()){
        					String preferredStatus =(String) contractStatusVerificationListIterator.next();
        					if(preferredStatus.equals(vo.getContractStatusCode())){
        						validStatus = true;
        						break;
        					}
        					
        				}
        			}
        			
        			if(!validStatus){
        				GenericException exception = new GenericException(
	    	            		BDErrorCodes.CONTRACT_STATUS_IS_NOT_VALID);
	    	        		errors.add(exception);
        			}
        			//Company name will be populated in step 5
        			if(errors.size()== 0 ){
        				if(StringUtils.isBlank(ievaluatorForm.getCompanyName())){
        					ievaluatorForm.setCompanyName(contractVO.getContractName());
        				}
        				
//        				ievaluatorForm.setYourFirm(null);
//        				ievaluatorForm.setYourName(null);
        				String baseClassId = contractVO.getClassId();
        				String contractLocationId  = contractVO.getCompanyId();
                        ievaluatorForm.setContractBaseClass(baseClassId);
                        ievaluatorForm.setContractLocationId(contractLocationId);
                        Contract contractDetails = null;
                        try{
                            contractDetails = ContractServiceDelegate.getInstance().getContractDetails(
                                Integer.parseInt(ievaluatorForm.getContractNumber()), 24);
                            
							ContractDetailsOtherVO details = ContractServiceDelegate.getInstance()
									.getContractDetailsOther(Integer.parseInt(ievaluatorForm.getContractNumber()));

                            String fundPackageSeriesCode = contractDetails.getFundPackageSeriesCode();
                            ievaluatorForm.setNml(contractDetails.isNml());
                            ievaluatorForm.setMerrillFirmFilter(details.isMerrillLynch());
                            ievaluatorForm.setContractBaseFundPackageSeries(fundPackageSeriesCode);
                            // If contract is GIFL V3 enabled, then GIFL V3 funds should be included for analysis
                            includeGIFLSelectFunds = contractDetails.getHasContractGatewayInd() && BDConstants.GIFL_VERSION_03.equals(contractDetails.getGiflVersion());
                            ievaluatorForm.setIncludeGIFLSelectFunds(includeGIFLSelectFunds);
                            ievaluatorForm.setPBAContrat( contractDetails.isPBA());
                        }
                        catch(ContractNotExistException e){
                            throw new SystemException(e, "Problem occured while getting contract base fund package series details" + e.getMessage());
                        }
        			}
        			
        		}
	        }
        }
        // This 'if' is needed irrespective of "Existing Client" or "New plan" tab is selected.
        if (!includeGIFLSelectFunds) {
        	// includedOptItem7 value has been reset if GIFL V3 funds are not to be included so that GIFL check box 
        	// will not all appear in included list and optional list on step5.
        	ievaluatorForm.setIncludedOptItem7("");
        }

        if (StringUtils.isNotEmpty(ievaluatorForm.getFirmFilterSelected())) {
            if (ievaluatorForm.getFirmFilterSelected().equals(FundEvaluatorConstants.NML)) {
                ievaluatorForm.setNml(true);
                ievaluatorForm.setEdwardJones(false);
                ievaluatorForm.setMerrillFirmFilter(false);
            } else if (ievaluatorForm.getFirmFilterSelected().equals(FundEvaluatorConstants.EDWARD_JONES)) {
                ievaluatorForm.setEdwardJones(true);
                ievaluatorForm.setNml(false);
                ievaluatorForm.setMerrillFirmFilter(false);
            } else if (FundEvaluatorConstants.MERRILL_LYNCH.equals(ievaluatorForm.getFirmFilterSelected())) {
            	ievaluatorForm.setMerrillFirmFilter(true); 
            	ievaluatorForm.setNml(false);
                ievaluatorForm.setEdwardJones(false);
            }
        }  else {
        	if (ievaluatorForm.isShowFirmFilter()) {
        		ievaluatorForm.setEdwardJones(false);
               /* ievaluatorForm.setNml(false);*/
        	}
        }
        // NY fund can only have NY state
        if (ievaluatorForm.getFundUsa().equals(FundEvaluatorConstants.NY_FUNDS)){
            ievaluatorForm.setStateSelected(FundEvaluatorConstants.NY_STATE);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> validateClientInfo");
        }
        return errors;
    }
    
    /**
     * This method validates Narrow your List page inputs.
     * @param ievaluatorForm
     * @return List<GenericException>
     * @throws SystemException
     */
	private List<GenericException> validateNarrowYourList(FundEvaluatorForm ievaluatorForm) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validateNarrowYourList");
		}

		List<GenericException> errors = new ArrayList<GenericException>();
		// if compulsory funds is blank or Empty in new business case
		if (StringUtils.isBlank(ievaluatorForm.getCompulsoryFunds())
				|| ((StringUtils.isBlank(ievaluatorForm.getStableValueFunds()) && ievaluatorForm.getMoneyMarketFunds() == null) && StringUtils
						.isBlank(ievaluatorForm.getContractNumber()))
				// if existing business and ContractSelectedSVFAndMMFFunds is empty or SVF or MMF fund list is empty
						
				|| (!StringUtils.isBlank(ievaluatorForm.getContractNumber()) && MapUtils.isEmpty(ievaluatorForm.getContractSelectedSVFAndMMFFunds())&& (StringUtils
						.isBlank(ievaluatorForm.getStableValueFunds()) && ievaluatorForm.getMoneyMarketFunds() == null))) {
			GenericException exception = new GenericException(BDErrorCodes.MISSING_COMPULSORY_FUNDS);
			errors.add(exception);
		}
		if (StringUtils.isBlank(ievaluatorForm.getPreSelectFunds())) {
			GenericException exception = new GenericException(BDErrorCodes.MISSING_TOP_RANK_OPTION);
			errors.add(exception);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> validateNarrowYourList");
		}
		return errors;

	}
    /**
     * This method validates Select Criteria page inputs
     * @param ievaluatorForm
     * @return List<GenericException>
     */
    private List<GenericException> validateCriteriaSelection(FundEvaluatorForm ievaluatorForm){
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> validateCriteriaSelection");
        }
    	
    	ArrayList<GenericException> errors = new ArrayList<GenericException>();
    	if(!ievaluatorForm.isCriteriaSelected()){
            GenericException exception = new GenericException(
            		BDErrorCodes.CRITERIA_NOT_SELECTED);
            errors.add(exception);    		
    	}
    	else if(StringUtils.isNotEmpty(ievaluatorForm.getRemainingPercentage())){
    		if("nonzero".equals(ievaluatorForm.getRemainingPercentage())){
	    		GenericException exception = new GenericException(
	            		BDErrorCodes.REMAINING_PERCENTAGE);
	            errors.add(exception);
    		}
    	}    
    	if (logger.isDebugEnabled()) {
		 logger.debug("exit -> validateCriteriaSelection");
    	}
    	return errors;
    	
    	
    }
    
   
    /**
    *
    * This method validates the mandatory fields before generating the report. 
    * @param ievaluatorForm
    * @return List<GenericException>
    * @throws SystemException
    */    
   private List<GenericException> validateGenerateReport(
		   FundEvaluatorForm ievaluatorForm) throws SystemException {
   	if (logger.isDebugEnabled()) {
           logger.debug("entry -> validateGenerateReport");
       }
   	ArrayList<GenericException> errors = new ArrayList<GenericException>();
   	if ((StringUtils.isEmpty(ievaluatorForm.getCompanyName()) || 
   			StringUtils.isWhitespace(ievaluatorForm.getCompanyName()))
				|| (StringUtils.isEmpty(ievaluatorForm.getYourName()) ||
				StringUtils.isWhitespace(ievaluatorForm.getYourName()))) {
			GenericException exception = new GenericException(
					BDErrorCodes.MISSING_FIELDS_GENEREATE_REPORT);
			errors.add(exception);
		}
   	if (logger.isDebugEnabled()) {
           logger.debug("exit -> validateGenerateReport");
       }
     
       return errors;

   }

    
    
    protected String getFOPConfigFileName() {
        return CommonConstants.FOP_CONFIG_FILE_KEY_NAME;
    }
    
    public String getXSLTFileName() {
        return CommonConstants.FUND_EVALUATOR_XSL_FILE_NAME;
    }
    
    public String getFundEvaluatorIncludedXSLPath() {
        return CommonConstants.FUND_EVALUATOR_INCLUDED_XSL_FILES_PATH;
    }
    
    protected ByteArrayOutputStream prepareXMLandGeneratePDF(AutoForm reportForm,
            HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside prepareXMLandGeneratePDF");
        }

        ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
        
        try {
        	long t2 = System.currentTimeMillis();
        
            CoreToolProcessor processor= new CoreToolProcessor();
            SessionContext sessionContext = new SessionContext();
            
            FundEvaluatorUtility.setSessionContextForReport(sessionContext, (FundEvaluatorForm)reportForm, request);
            
            ArrayList<AssetClassForInvOption>assetClassForInvOptionList= processor.populateInvestmentOptionVOs(sessionContext);
            
            String contractSelectedFunds[] = processor.getContractFundsSelectionStatus(assetClassForInvOptionList);
            
            //broker selected funds could be derived (funds which are in checkedFundsList, but not in toolSelectedFunds list - would be funds selected by Broker). This implementation is not required for per current requirements, but could be used if needed
            String brokerSelectedFunds[] = processor.getBrokerFundsSelectionStatus(assetClassForInvOptionList);
            
            //lifeStyleFunds contains lifestyleFundFamilyCode and LifestyleFundNamesList
            Map<String, ArrayList<String>> lifeStyleFunds = processor.getLifeStyleFunds(assetClassForInvOptionList);
            
            String toolRecommendedFunds[] =  processor.getToolRecommendedFunds(assetClassForInvOptionList);
            
            sessionContext.setLifeStyleFunds(lifeStyleFunds);
            sessionContext.setContractFunds(contractSelectedFunds);
            sessionContext.setAdditionalFunds(brokerSelectedFunds);
            sessionContext.setToolRecommendedFunds(toolRecommendedFunds);
            
            logger.debug("##### SESSIONCONTEXT FOR PDF REPORT = " + sessionContext.toString(REPORT_MODE));
            Object xmlTree = processor.getPDFXmlDocument(sessionContext);
            
            long t3 = System.currentTimeMillis();
            logger.debug("#### Time span required to create XML  #### = " + ((t3-t2)/1000)+" seconds");
            long t4 = System.currentTimeMillis();
            String xsltFileName = getXSLTFileName();
            String configFileName = getFOPConfigFileName();
            String fundEvaluatorIncludedXSLPath = getFundEvaluatorIncludedXSLPath();
            
            if (xmlTree == null || xsltFileName == null) {
                return pdfOutStream;
            }
            
            String xsltfile = FundEvaluatorProperties.get(xsltFileName);
            String configfile = FundEvaluatorProperties.get(configFileName);
            String includedXSLPath = FundEvaluatorProperties.get(fundEvaluatorIncludedXSLPath);
            
            if (xmlTree instanceof Document) {
                pdfOutStream = PDFGenerator.getInstance().generatePDFFromDOM((Document) xmlTree,
                        xsltfile, configfile, includedXSLPath);
            }
            long t5 = System.currentTimeMillis();
            logger.debug("#### Time span required to Transform using XSL  #### = " + ((t5-t4)/1000)+" seconds");
            
        } catch (Exception exception) {
            String message = null;
            if (exception instanceof ContentException) {
                message = "Error occured while retrieveing CMA Content during PDF creation.";
            } else if (exception instanceof ParserConfigurationException) {
                message = "Error occured while creating Document object during PDF creation.";
            } else if (exception instanceof FOPException
                    || exception instanceof TransformerException
                    || exception instanceof IOException) {
                message = "Error occured during PDF generation.";
            } else {
                message = "Error occured during PDF generation.";
            }

            throw new SystemException(exception, message);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting prepareXMLandGeneratePDF");
        }
        return pdfOutStream;
    }
    
    /**
     * Format Company name such that multiple spaces replaced by single space. If Company name already
     * has period in end - we remove it (we add period in end by default)
     */
    private String getFileName(String companyName) {
        
        if(StringUtils.contains(companyName, FundEvaluatorConstants.SINGLE_SPACE)){
            StringBuffer companyNameCharBuf = new StringBuffer();
            int count = companyName.length();
            String prevChar = "";
            for(int i=0; i<count; i++) {
               String CurrentChar = Character.toString(companyName.charAt(i));
               
               if(!(CurrentChar.equals(FundEvaluatorConstants.SINGLE_SPACE) && prevChar.equals(FundEvaluatorConstants.SINGLE_SPACE))) {
                   companyNameCharBuf.append(CurrentChar);
               }
               prevChar = CurrentChar;
            }
            companyName = companyNameCharBuf.toString();            
            
            String []companyNameArray = companyName.split(FundEvaluatorConstants.SINGLE_SPACE);
            StringBuffer companyNameBuf = new StringBuffer();
            for (int i = 0; i < companyNameArray.length; i++) {
                companyNameBuf.append(FundEvaluatorConstants.CO_NAME_SEPERATOR + companyNameArray[i]);
            }
            companyName = companyNameBuf.toString();
        }
        else{
            companyName = FundEvaluatorConstants.CO_NAME_SEPERATOR + companyName;
        }
        if(companyName.endsWith(FundEvaluatorConstants.PERIOD)){
            companyName = companyName.substring(0,companyName.length()-1);
        }
        
        Calendar calender = Calendar.getInstance();
        Date date = calender.getTime();
        String dateString = new SimpleDateFormat(FundEvaluatorConstants.PDF_FILE_NAME_DATE_FORMAT).format(date);
        return FundEvaluatorConstants.PDF_FILE_NAME_PREFIX + dateString + companyName + FundEvaluatorConstants.PDF_FILE_NAME_SUFFIX;
    }
    
    /**
     * This method is used to log the Fund Evaluator Report Generation into MRL.
     * 
     * @param contractNumber String
     * @param userProfile - BDUserProfile object.
     */
    private void logFundEvaluatorReportGeneration(String contractNumber, BDUserProfile userProfile) {
        StringBuffer logData = new StringBuffer();
        Long profileID = userProfile.getBDPrincipal().getProfileId();
        Long firmPartyId = userProfile.getBDPrincipal().getBDUserRole().getGoverningBDFirmPartyId();
        
        logData.append(BDConstants.FUND_EVALUATOR_LOG_USER_PROFILE_ID).append(profileID).append(
                BDConstants.SEMICOLON_SYMBOL);

        logData.append(FIRM_PARTY_ID).append(firmPartyId == null ? "" : firmPartyId).append(
                BDConstants.SEMICOLON_SYMBOL);

        logData.append(BDConstants.FUND_EVALUATOR_LOG_DATE_OF_ACTION).append(new Date()).append(
                BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.FUND_EVALUATOR_LOG_YOUR_CLIENT).append(
        		StringUtils.isEmpty(contractNumber) ? BDConstants.FUND_EVALUATOR_LOG_NEW_PLAN : contractNumber).append(
                BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.FUND_EVALUATOR_LOG_ACTION_TAKEN).append(
                BDConstants.FUND_EVALUATOR_LOG_ACTION_RESULT).append(BDConstants.SEMICOLON_SYMBOL);

        BlockOfBusinessUtility.logWebActivity("doGenerateReport", logData.toString(), userProfile, logger,
                interactionLog, logRecord);
    }

    /** This method is used to show the glossary information.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/" ,params={"action=showGlossary"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doShowGlossary (@Valid @ModelAttribute("fundEvaluatorForm") FundEvaluatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException { 
    
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> doShowGlossary");
        }
        FundEvaluatorForm ievaluatorForm = (FundEvaluatorForm) form;       
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doShowGlossary");
        }
        return forwards.get("glossary"); 
        
	}
    
    /**
     * avoids token generation as this class acts as intermediate for many
     * transactions.
     * 
     * (non-Javadoc)
     * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired(java.lang.String)
     */
   /* @Override
    protected boolean isTokenRequired(String action) {
    	if(StringUtils.equalsIgnoreCase(action, "GetInvOptionDetails")
    			||StringUtils.equalsIgnoreCase(action, "UpdateInvOptionDetails")
    			||StringUtils.equalsIgnoreCase(action, "LaunchPrintWindow")
    			||StringUtils.equalsIgnoreCase(action, "ShowGlossary"))
    		return false;
    	return true;
    }
*/
    /**
     * Returns true if token has to be validated for the particular action call
     * to avoid CSRF vulnerability else false. (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.controller.BaseController#isTokenValidatorEnabled(java.lang.String)
     */
    @Override
    protected boolean isTokenValidatorEnabled(String action) {
    	// avoids methods from validation which ever is not required
    	return StringUtils.isNotEmpty(action)
    			&& (StringUtils.equalsIgnoreCase(action, "SelectYourClient")
    					|| StringUtils.equalsIgnoreCase(action, "SelectCriteria") 
    					|| StringUtils.equalsIgnoreCase(action, "NarrowYourList")
    					|| StringUtils.equalsIgnoreCase(action, "InvestmentOptionsSelection")
    					|| StringUtils.equalsIgnoreCase(action, "GetInvOptionDetails")
    					|| StringUtils.equalsIgnoreCase(action, "UpdateInvOptionDetails")
    					|| StringUtils.equalsIgnoreCase(action, "CustomizeReport")
    					|| StringUtils.equalsIgnoreCase(action, "GenerateReport"))?true:false;
    } 
    
    private int getCIAFormCMAKey(FundEvaluatorForm reportForm) {
    	if(reportForm.isNml()) {
    		return CommonContentConstants.CIA_FRW_IS_NML;
    	} else if (reportForm.isMerrillFirmFilter()) {
    		return CommonContentConstants.CIA_FRW_ML;
    	} else {
    		 return CommonContentConstants.CIA_FRW_IS_NOT_NML;
    	}
	}

    @Autowired
	   private BDValidatorFWDefault  bdValidatorFWDefault;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWDefault);
	}
    
}