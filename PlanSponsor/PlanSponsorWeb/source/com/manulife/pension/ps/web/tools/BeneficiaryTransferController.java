package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.BeneficiaryServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.report.valueobject.SelectContract;
import com.manulife.pension.service.contract.report.valueobject.SelectContractReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.service.security.utility.ConversionUtility;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

/**
 * Action class for Beneficiary Internal Transfer 
 * @author Tamilarasu Krishnamoorthy
 *
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"beneficiaryTransferForm"})

public class BeneficiaryTransferController extends PsAutoController {
	
	@ModelAttribute("beneficiaryTransferForm")
	public BeneficiaryTransferForm populateForm()
	{
		return new BeneficiaryTransferForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/tools/beneficiaryTransfer.jsp");
		forwards.put( "default","/tools/beneficiaryTransfer.jsp");
		forwards.put( "confirm","/tools/beneficiaryTransferConfirm.jsp");
		forwards.put( "print","/tools/beneficiaryTransferConfirm.jsp"); 
		forwards.put( "cancel","redirect:/do/tools/toolsMenu/");
		forwards.put( "continue","redirect:/do/tools/toolsMenu/");
		}

	private final static String DI_DURATION_24_MONTH = "24";
	private final static String OLD_CONTRACT = "oldContract";
	
	/**
	 * Constructor
	 */
	public BeneficiaryTransferController() {
		super(BeneficiaryTransferController.class);
	}

	/**
	 * Default action on Internal beneficiary transfer page.
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return Action forwarded to Edit  Beneficiary Internal Transfer Page
	 * @throws SystemException 
	 */
	@RequestMapping(value ="/beneficiaryTransfer/" , method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("beneficiaryTransferForm") BeneficiaryTransferForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		} 
		
		String message = "";

		BeneficiaryTransferForm  beneficiaryTransferForm 
			= (BeneficiaryTransferForm) actionForm;

		UserProfile userProfile = SessionHelper.getUserProfile(request);
		//Forward the user to home page, if the user is not a superCar or Team lead
		if(!(userProfile.isSuperCar()||userProfile.getRole() instanceof TeamLead || userProfile.getRole() instanceof BundledGaCAR)){
			return new ControllerRedirect(Constants.HOME_URL).getPath();
		}
		String newContractId =
			String.valueOf(userProfile.getCurrentContract().getContractNumber()) != null ? String.valueOf(userProfile.getCurrentContract().getContractNumber()).trim() : StringUtils.EMPTY;
		String newContractName =
			String.valueOf(userProfile.getCurrentContract().getCompanyName()) != null ? String.valueOf(userProfile.getCurrentContract().getCompanyName()).trim() : StringUtils.EMPTY;
		beneficiaryTransferForm.setNewContract(newContractId);
		beneficiaryTransferForm.setNewContractName(newContractName);
		beneficiaryTransferForm.setOldContract(StringUtils.EMPTY);
		
		message = ContentHelper.getContentText(
				ContentConstants.BENEFICIARY_TRANSFER_CONFIRM_MESSAGE, 
				ContentTypeManager.instance().MESSAGE, null);
		beneficiaryTransferForm.setMessage(message);
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doDefault");
		}
		// to handle double submissions
		//TODO saveToken(request);
		return forwards.get(Constants.BENEFICIARY_TRANSFER_DEFAULT);
	}
	
	/**
	 * Validate the From contract.
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/beneficiaryTransfer/",params={"action=contractValidate"}   , method =  {RequestMethod.POST}) 
	public String doContractValidate (@Valid @ModelAttribute("beneficiaryTransferForm") BeneficiaryTransferForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  

    	// this is protect the double submit. If double submit happens,
    	// then redirect to beneficiary Transfer page
       /* //TODO if (!(isTokenValid(request,true))) {
            return forwards.get(Constants.BENEFICIARY_TRANSFER_DEFAULT);
        }*/
		List<ValidationError> errors = null;
		errors = new ArrayList<ValidationError>();
		ValidationError vError =null;
		SelectContractReportData report = null;
		//BeneficiaryTransferForm  beneficiaryTransferForm = (BeneficiaryTransferForm) actionForm; 
		form.setValidContract(true);
		String oldContractId = form.getOldContract().trim();

		
		if(StringUtils.isEmpty(oldContractId)){
			vError = new ValidationError(OLD_CONTRACT,ErrorCodes.ERROR_BENEFICIARY_TRANSFER_FROM_CONTRACT_BLANK,Type.error);
			errors.add(vError);

		}  else if(!isValidContractId(oldContractId,request,report,form)){

			vError = new ValidationError(OLD_CONTRACT,ErrorCodes.ERROR_BENEFICIARY_TRANSFER_FROM_CONTRACT_INVALID,Type.error);
			errors.add(vError);
		}	
		// Adds a new token, as we forward to a JSP 
    	////TODO saveToken(request);
		//check for error list greater than zero and set it in session
		if(errors.size()>0){
			super.setErrorsInSession(request, errors);
			form.setValidContract(false);
		}
		return forwards.get("input");
	}
	
	/**
	 * Method used to transfer Old contract participant beneficiary record 
	 * to new contract participant beneficiary information.
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return  ActionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/beneficiaryTransfer/",params={"action=transfer"}   , method =  {RequestMethod.POST}) 
	public String doTransfer (@Valid @ModelAttribute("beneficiaryTransferForm") BeneficiaryTransferForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doTransfer");
		}
		int count=0;
		Integer insertXRowCount = 0;
		String insertRowCount = "";
		String applicationId="";

		//BeneficiaryTransferForm  beneficiaryTransferForm = (BeneficiaryTransferForm) actionForm; 

		String oldContractId = form.getOldContract().trim();
		String newContractId = form.getNewContract();
		
		BaseEnvironment baseEnvironment= new BaseEnvironment();
		insertRowCount = baseEnvironment.getNamingVariable(
				Constants.BENEFICIARY_TRANSFER_COMMIT_EVERYXROWS, null);
		try{
			insertXRowCount=Integer.parseInt(insertRowCount);
		}
		catch(NumberFormatException e){
			logger.error("Fail to get  beneficiary.transfer.commit.everyxrows information " +
						 "from name space binding variable", e);
			insertXRowCount = 100;
		}
		applicationId = baseEnvironment.getApplicationId();

		BeneficiaryServiceDelegate beneficiaryServiceDelegate = 
			BeneficiaryServiceDelegate.getInstance(applicationId);

		count = beneficiaryServiceDelegate.transferBeneficiaryRecord(oldContractId,newContractId,insertXRowCount);
		form.setRecordCount(count);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doTransfer");
		}
		return forwards.get(Constants.CONFIRM);
	}
	
	/**
	 * Get the from contract data for validation and data population on the Internal Beneficiary transfer page.
	 * @param request
	 * @param beneficiaryTransferForm
	 * @param oldContractId
	 * @return
	 * @throws SystemException
	 */
	private SelectContractReportData getSelectContractReportData(
			HttpServletRequest request,BeneficiaryTransferForm beneficiaryTransferForm,
			String oldContractId) throws SystemException {
		ReportCriteria reportCriteria = populateReportCriteria(request,beneficiaryTransferForm);
		ReportServiceDelegate service = ReportServiceDelegate.getInstance();
		 SelectContractReportData report = null;
		try {
			report =(SelectContractReportData) service.getReportData(reportCriteria);

		} catch (ReportServiceException e) {
			logger.error("Fail to get  contract  information for contract number"+oldContractId, e);
			throw new SystemException(
					e,
					"Fail to get  contract  information for contract number"+oldContractId);
		}
		return report;
	}
	
	/**
	 * Method to populate the report criteria to validate the from contract.
	 * @param request
	 * @param beneficiaryTransferForm
	 * @return
	 */
	private ReportCriteria populateReportCriteria(HttpServletRequest request,BeneficiaryTransferForm  beneficiaryTransferForm ) {

		// default sort criteria
		ReportCriteria criteria = new ReportCriteria(SelectContractReportData.REPORT_ID);
		UserProfile userProfile = getUserProfile(request);
		criteria.setPageSize(20);
		criteria.addFilter(SelectContractReportData.FILTER_CLIENT_ID, String.valueOf(userProfile
				.getPrincipal().getProfileId()));
		criteria.addFilter(SelectContractReportData.FILTER_SITE_LOCATION, Environment
				.getInstance().getDBSiteLocation());
		criteria.addFilter(SelectContractReportData.FILTER_USER_ROLE, ConversionUtility.convertToStoredProcRole(userProfile.getRole()));

		criteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING, "C.CONTRACT_ID="+ beneficiaryTransferForm.getOldContract());
		criteria.insertSort(Constants.TPA_CONTRACT_NUMBER_SORT_FIELD, ReportSort.ASC_DIRECTION);
		
		return criteria;
	}
	
	/**
	 * 
	 * Validate whether the old contract given in beneficiary internal transfer page is valid 
	 * @param contract
	 * @param request
	 * @param beneficiaryTransferForm
	 * @return boolean
	 * @throws SystemException 
	 */
	@SuppressWarnings("unchecked")
	private boolean isValidContractId(String contractId,
			HttpServletRequest request,SelectContractReportData report,
			BeneficiaryTransferForm  beneficiaryTransferForm) throws SystemException{
		if (contractId == null) {
			return false;
		}
		try {
			new BigDecimal(contractId);
		} catch (NumberFormatException e) {
			return false;
		}
		report = getSelectContractReportData(request, beneficiaryTransferForm, contractId);

		if(report!=null && report.getDetails().isEmpty()){
			return false;
		}
		List<SelectContract> details = (List<SelectContract>) report.getDetails();
		if(details !=null){
			for(SelectContract selectContract:details){
				beneficiaryTransferForm.setOldContractName(selectContract.getContractName());
			}
		}
		return true;
	}
	
	
	/**
	 * Forward the page to edit beneficiary internal transfer page.
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/beneficiaryTransfer/", params={"action=print"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrint (@Valid @ModelAttribute("beneficiaryTransferForm") BeneficiaryTransferForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		
		return forwards.get(Constants.PRINT);
	}
	
	/**
	 * Forward the page to edit beneficiary internal transfer page.
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/beneficiaryTransfer/", params={"action=cancel"} , method =  {RequestMethod.POST}) 
	public String doCancel (@Valid @ModelAttribute("beneficiaryTransferForm") BeneficiaryTransferForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		
		return forwards.get(Constants.CANCEL);
	}
	
	/**
	 * Forward the page to tools page.
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
	@RequestMapping(value ="/beneficiaryTransfer/", params={"action=continue"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doContinue (@Valid @ModelAttribute("beneficiaryTransferForm") BeneficiaryTransferForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
	
		
		return forwards.get(Constants.CONTINUE);
	}
	
	/**
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
