package com.manulife.pension.ps.web.census.beneficiary;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.PartyServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.beneficiary.util.BeneficiaryErrorCodes;
import com.manulife.pension.ps.web.census.beneficiary.util.BeneficiaryErrorUtil;
import com.manulife.pension.ps.web.census.beneficiary.util.BeneficiaryUtil;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.census.util.EmployeeSnapshotSecurityProfile;
import com.manulife.pension.ps.web.census.util.ParameterizedActionForward;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.beneficiary.util.BeneficiaryDesignationErrors;
import com.manulife.pension.service.beneficiary.util.BeneficiaryDesignationValidator;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryDesignation;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryDesignationData;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiarySet;
import com.manulife.pension.service.employee.util.EmployeeValidationError.ErrorType;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.NumberRender;

@Controller
@RequestMapping( value = "/census/beneficiary/editBeneficiaryInformation/")
@SessionAttributes({"beneficiaryForm"})

public class EditBeneficiaryController extends PsAutoController {

	@ModelAttribute("beneficiaryForm") 
	public BeneficiaryForm populateForm() 
	{
		return new BeneficiaryForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
	forwards.put("input","/census/beneficiary/editBeneficiaryInformation.jsp");
	forwards.put("default","/census/beneficiary/editBeneficiaryInformation.jsp"); 
	forwards.put("add", "forward:/do/census/beneficiary/editBeneficiaryInformation/");
	forwards.put("save","forward:/do/census/beneficiary/editBeneficiaryInformation/");
	forwards.put("view", "redirect:/do/census/beneficiary/viewBeneficiaryInformation/");}

	private static String  primaryMaximum  = "";
	
	private static String  contingentMaximum = "";
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MM/dd/yyyy", Locale.US);
	
	/**
	 * Constructor
	 */
	public EditBeneficiaryController() {
		super(EditBeneficiaryController.class);
	}

	/**
	 * Get the Beneficiary information for the particular participant 
	 * and display it in Edit  Online Beneficiary Page, if any. otherwise 
	 * display empty single primary beneficiary field

	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return Action forwarded to Edit Online Beneficiary Page
	 * @throws SystemException 
	 */

	
	@RequestMapping(method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("beneficiaryForm") BeneficiaryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");
	       }
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		BeneficiaryForm beneficiaryForm = (BeneficiaryForm) actionForm;
		
		//get the profileID
		String profileId =null;
		String applicationId = "";
		if(beneficiaryForm.getProfileId()!=null){
			profileId  = beneficiaryForm.getProfileId();
		}
		
		List<BeneficiaryDesignation> primaryBeneficiaryList=null;
		
		List<BeneficiaryDesignation> contingentBeneficiaryList=null;
		
		BeneficiarySet beneficiarySet = null;
		// get the current userProfile Object
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		//Get the application id
		BaseEnvironment environment = new BaseEnvironment();
		applicationId = environment.getApplicationId();
		
		String contractNumber =
			String.valueOf(userProfile.getCurrentContract().getContractNumber()); 
		
		 EmployeeSnapshotSecurityProfile securityProfile = new EmployeeSnapshotSecurityProfile(
	                userProfile);
		 
		 long profileID = 0L;
			try {
				// get the profileId from the actionForm
				profileID = Long.parseLong(profileId);
			} catch (NumberFormatException e) {
				throw new SystemException(e, "Unable parse the profileId String "
						+ beneficiaryForm.getProfileId());
			}
		//Set the employee relation information in the beneficiary Page
			Employee employee = new EmployeeServiceFacade().
										getEmployee(profileID, userProfile, new Date(), false);
		if (!(BeneficiaryUtil.isOBDSCSFAvailable(contractNumber)
				&& employee.isParticipant())
				||!securityProfile.isUpdateCensusData()
				|| securityProfile.isContractDI()) {
			return Constants.HOME_URL;
		}
		EmployeeServiceDelegate employeeServiceDelegate = 
								EmployeeServiceDelegate.getInstance(applicationId);
		BeneficiaryDesignationData beneficiaryDesignationData;
		try {
			beneficiaryDesignationData = employeeServiceDelegate.getBeneficiaryDesignationData(profileId, contractNumber);
		} catch (SystemException e) {
			logger.error("Fail to get  Beneficiary information for profileId"+profileId
					+"and contract number"+contractNumber, e);
			throw e;
		}
		
		//Get the beneficiary maximum count 
		getBeneficiaryMaximumCount();
		primaryBeneficiaryList =  new ArrayList<BeneficiaryDesignation>();
		contingentBeneficiaryList = new ArrayList<BeneficiaryDesignation>();
		if(beneficiaryDesignationData != null){
			if(beneficiaryDesignationData.getBeneficiaryDesignationList().size()>0){
				beneficiaryForm.setBeneficiaryRecordExisting(true);
			}
			primaryBeneficiaryList = beneficiaryDesignationData.getPrimaryOrContingentBeneficiaryList(
					BeneficiaryDesignationData.PRIMARY_BENEFICIARY_TYPE_CODE);
			contingentBeneficiaryList =	beneficiaryDesignationData.getPrimaryOrContingentBeneficiaryList(
					BeneficiaryDesignationData.CONTINGENT_BENEFICIARY_TYPE_CODE);
			
			for(BeneficiaryDesignation beneficiaryDesignation:beneficiaryDesignationData.getBeneficiaryDesignationList()){
				String sharePerc = NumberRender.formatByPattern(beneficiaryDesignation.
						getSharePct(), null, Constants.DECIMAL_PATTERN, 2, BigDecimal.ROUND_HALF_UP);
				beneficiaryDesignation.setSharePct(new BigDecimal(sharePerc));
			}

			beneficiarySet = beneficiaryDesignationData.getBeneficiarySet();
			
			BeneficiaryUtil.
						setEmployeeInformation(userProfile,beneficiaryForm, beneficiarySet, employee);
		}
		//Set the primary Beneficiary into the form 
		if(primaryBeneficiaryList != null){
			beneficiaryForm.setPrimaryBeneficiaries(primaryBeneficiaryList);
		}
		//Set the contingent Beneficiary into the form 
		if(contingentBeneficiaryList!=null){
			beneficiaryForm.setContingentBeneficiaries(contingentBeneficiaryList);
		}
		//Get the beneficiary relationship and type
		BeneficiaryUtil.getBeneficiaryRelationshipAndType(beneficiaryForm);
		
		beneficiaryForm.storeClonedForm();
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doDefault");
		}
		request.getSession(false).setAttribute("challengeRequestFrom", "editBeneficiaryInformationPage");
		return forwards.get(Constants.DEFAULT);
	}

	/**
	 * Add the primary or contingent beneficiary in edit beneficiary page,
	 * when user click add link in the page.
	 * Display error message if it reaches maximum count.
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(params={"action=add"}, method = {RequestMethod.POST})
	public String doAdd(@Valid @ModelAttribute("beneficiaryForm") BeneficiaryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doAdd");
		}

		List<ValidationError> errors = null;
		Integer emptyBeneficiaryCount = Integer.valueOf(0);
		BeneficiaryForm  beneficiaryForm 
				= (BeneficiaryForm) actionForm;
		errors = new ArrayList<ValidationError>();
		
		//check for the beneficiary type it try to add .
		if(beneficiaryForm.getBeneficiaryType() != null ){
			if(BeneficiaryDesignationData.PRIMARY_BENEFICIARY_TYPE_CODE.equalsIgnoreCase(
					beneficiaryForm.getBeneficiaryType())){
				//Iterate the primary beneficiary list and get the empty beneficiary count
				for(BeneficiaryDesignation beneficiaryDesignation:beneficiaryForm.getPrimaryBeneficiaries()){
					if(beneficiaryForm.isBeneficiaryEmpty(beneficiaryDesignation)){
						emptyBeneficiaryCount++;
					}
				}
					validateBeneficiaryMaxCount(emptyBeneficiaryCount,Integer.valueOf(primaryMaximum),
							BeneficiaryDesignationData.PRIMARY_KEY, errors);
				if(errors.size() == 0){ 
					((BeneficiaryForm)actionForm).addPrimaryBeneficiary();
				}

			}else if(BeneficiaryDesignationData.CONTINGENT_BENEFICIARY_TYPE_CODE.
					equalsIgnoreCase(beneficiaryForm.getBeneficiaryType())){
				//Iterate the contingent beneficiary list and get the empty beneficiary count
				for(BeneficiaryDesignation beneficiaryDesignation:beneficiaryForm.getContingentBeneficiaries()){
					if(beneficiaryForm.isBeneficiaryEmpty(beneficiaryDesignation) 
							|| beneficiaryDesignation.isBeneficiaryDeleted()){
						emptyBeneficiaryCount++;
					}
				}
				
				validateBeneficiaryMaxCount(emptyBeneficiaryCount,Integer.valueOf(contingentMaximum),
						BeneficiaryDesignationData.CONTINGENT_KEY, errors);
				if(errors.size() == 0){ 
					((BeneficiaryForm)actionForm).addContingentBeneficiary();
				}

			}
		}
		//check for error list greater than zero and set it in session
		if(errors.size()>0){
			super.setErrorsInSession(request, errors);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doAdd");
		}
		return forwards.get("input");
	}


	/**
	 * Get the maximum count of both primary and contingent beneficiary
	 * @throws SystemException
	 */
	private void getBeneficiaryMaximumCount() throws SystemException {
		// BUSINESS_PARAM Table will be invoked only if any of the values are not present.
		if(StringUtils.isEmpty(primaryMaximum) || StringUtils.isEmpty(contingentMaximum)){
			Map<String, String> beneficiaryCount = 
				PartyServiceDelegate.getInstance().getBusinessParmMap();
			primaryMaximum = 
					beneficiaryCount.get(BeneficiaryDesignationData.PRIMARY_BENEFICIARY_MAX_NO);
			contingentMaximum = 
					beneficiaryCount.get(BeneficiaryDesignationData.CONTINGENT_BENEFICIARY_MAX_NO);
		}
	}


	/**
	 * validate the beneficiary information return from the form and 
	 * if validation success,it save the participant data into the CSDB table
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException 
	 * @throws SystemException
	 * @throws Exception 
	 */
	
	@RequestMapping(params={"action=save"}, method = {RequestMethod.POST})
	public String doSave(@Valid @ModelAttribute("beneficiaryForm") BeneficiaryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
	       }
		}
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSave");
		}

		
		List<BeneficiaryDesignation> primarybeneficiary = null;
		 List<BeneficiaryDesignation> beneficiaryInformationList = null;
		List<BeneficiaryDesignation> contingentbeneficiary = null;
		BeneficiarySet beneficiarySet = null;
		Integer contingentNo = Integer.valueOf(0);
		Integer primaryNo = Integer.valueOf(0);
		Integer deletedBeneficiaryCount = Integer.valueOf(0);
		String createdUserIdType=null;

		BeneficiaryForm beneficiaryForm =
			(BeneficiaryForm)actionForm;
		// get the profileID
		String profileId =
			beneficiaryForm.getProfileId();
		// get the current userProfile Object
		UserProfile userProfile =
			SessionHelper.getUserProfile(request);

		Integer contractNumber =
			userProfile.getCurrentContract().getContractNumber();


		beneficiaryInformationList=new ArrayList<BeneficiaryDesignation>();
		
		beneficiarySet=new BeneficiarySet();
		primarybeneficiary = 
			(java.util.List<BeneficiaryDesignation>) beneficiaryForm.getPrimaryBeneficiaries();
		contingentbeneficiary = 
			(java.util.List<BeneficiaryDesignation>) beneficiaryForm.getContingentBeneficiaries();
		
		//Iterate and Remove the primary beneficiary information 
		if(primarybeneficiary != null && !primarybeneficiary.isEmpty())
		{
			for(BeneficiaryDesignation beneficiaryDesignation:primarybeneficiary){ 
				if(!beneficiaryForm.isBeneficiaryEmpty(beneficiaryDesignation)){
					beneficiaryDesignation.setFirstName(beneficiaryDesignation.getFirstName().trim());
					beneficiaryDesignation.setLastName(beneficiaryDesignation.getLastName().trim());
					if(BeneficiaryDesignationData.OTHER_RELATIONSHIP_CODE.
							equals(beneficiaryDesignation.getRelationshipCode())){
						beneficiaryDesignation.setOtherRelationshipDesc(beneficiaryDesignation.getOtherRelationshipDesc().trim());
					}
					beneficiaryInformationList.add(beneficiaryDesignation);
					
					if(beneficiaryDesignation.isBeneficiaryDeleted()){
						deletedBeneficiaryCount++;
					}else{
						primaryNo++;	
					}
				}
			}
			
		}
		if(contingentbeneficiary != null && !contingentbeneficiary.isEmpty()){
			for(BeneficiaryDesignation beneficiaryDesignation:contingentbeneficiary){ 
				if(!beneficiaryForm.isBeneficiaryEmpty(beneficiaryDesignation)){
					beneficiaryDesignation.setFirstName(beneficiaryDesignation.getFirstName().trim());
					beneficiaryDesignation.setLastName(beneficiaryDesignation.getLastName().trim());
					if(BeneficiaryDesignationData.OTHER_RELATIONSHIP_CODE.
							equals(beneficiaryDesignation.getRelationshipCode())){
						beneficiaryDesignation.setOtherRelationshipDesc(beneficiaryDesignation.getOtherRelationshipDesc().trim());
					}
					beneficiaryInformationList.add(beneficiaryDesignation);
					
					if(beneficiaryDesignation.isBeneficiaryDeleted()){
						deletedBeneficiaryCount++;
					}else{
						contingentNo++;
					}

				}
			}
		}
		//Final beneficiaryDesignation data to get stored
		BeneficiaryDesignationData beneficiaryDesignationData =  
			new BeneficiaryDesignationData(beneficiaryInformationList, beneficiarySet);
		//Set the beneficiary type description 
		beneficiaryDesignationData.setBeneficiaryTypeDescription(beneficiaryForm.getBeneficiaryTypeMap());

		//code to validate form 
		List<ValidationError> errors = 
			new ArrayList<ValidationError>();
		//set the error, if primary beneficiary count exceeds the maximum limit
		validateBeneficiaryMaxCount(primaryNo-1,Integer.valueOf(primaryMaximum),
				BeneficiaryDesignationData.PRIMARY_KEY, errors);
		//set the error, if contingent beneficiary count exceeds the maximum limit
		validateBeneficiaryMaxCount(contingentNo-1,Integer.valueOf(contingentMaximum),
				BeneficiaryDesignationData.CONTINGENT_KEY, errors);
		//Get beneficiaryDesignationErrors from validateBeneficiaryDesignationData method 
		//and if any error, convert to ValidateError using  BeneficiaryErrorUtil SetErrorInValidationFormat() method
		
		BeneficiaryDesignationValidator beneficiaryDesignationValidator =
											new BeneficiaryDesignationValidator();
		BeneficiaryDesignationErrors beneficiaryDesignationErrors = null;
		if(!(beneficiaryForm.isBeneficiaryRecordExisting() 
				&& (beneficiaryInformationList.size() == deletedBeneficiaryCount))){
			beneficiaryDesignationErrors = 
				beneficiaryDesignationValidator.validateBeneficiaryDesignationData(beneficiaryDesignationData);
		}
											
		if(beneficiaryDesignationErrors != null && beneficiaryDesignationErrors.size()>0){
			BeneficiaryErrorUtil.getInstance().setErrorInValidationFormat(beneficiaryDesignationErrors, errors);
		}
		//Set the error in session, if error occur and forward to edit page
		if(errors.size() > 0){
			super.setErrorsInSession(request, errors);
			return forwards.get("input");			
		}else{
			try {
			Integer beneficiaryNo = Integer.valueOf(1);
			String applicationId ="";
			 List<BeneficiaryDesignation> confirmedBeneficiaryInformationList = new ArrayList<BeneficiaryDesignation>();

			//Rearrange the beneficiaryNo for deleted and empty beneficiary numbering
			 for(BeneficiaryDesignation beneficiaryDesignation:beneficiaryInformationList){ 
				 String birthDate=null;
				 if(!beneficiaryDesignation.isBeneficiaryDeleted() ){
					 if(BeneficiaryDesignationData.PRIMARY_BENEFICIARY_TYPE_CODE.
							 equalsIgnoreCase(beneficiaryDesignation.getBeneficiaryTypeCode())){
						 //Set the reassigned primary number
						 beneficiaryDesignation.setBeneficiaryNo(beneficiaryNo);
						 //If the relation is not 'Other',Set the otherRelationfield to empty.
						 if(!BeneficiaryDesignationData.OTHER_RELATIONSHIP_CODE.
								 equals(beneficiaryDesignation.getRelationshipCode())){
							 beneficiaryDesignation.setOtherRelationshipDesc(StringUtils.EMPTY);
						 }
						 //Set the string property of date to Date property,if it is not blank
						 birthDate = beneficiaryDesignation.getStringBirthDate();
						 if(StringUtils.isNotBlank(birthDate)){
							 setBirthDate(beneficiaryDesignation, birthDate);
						 }else{
							 beneficiaryDesignation.setBirthDate(null);
						 }
						 //set the share percentage in BigDecimal format
						 beneficiaryDesignation.setSharePct(
								 	new BigDecimal(beneficiaryDesignation.getStringSharePct()));
						 confirmedBeneficiaryInformationList.add(beneficiaryDesignation);
						 beneficiaryNo++;
					 }else if(BeneficiaryDesignationData.CONTINGENT_BENEFICIARY_TYPE_CODE.
							 equalsIgnoreCase(beneficiaryDesignation.getBeneficiaryTypeCode())){
						 //Set the reassigned contingent number
						 beneficiaryDesignation.setBeneficiaryNo(beneficiaryNo);
						 //If the relation is not 'Other',Set the otherRelationfield to empty.
						 if(!BeneficiaryDesignationData.OTHER_RELATIONSHIP_CODE.
								 equals(beneficiaryDesignation.getRelationshipCode())){
							 beneficiaryDesignation.setOtherRelationshipDesc(StringUtils.EMPTY);
						 }
						 //Set the string property of date to Date property,if it is not blank
						 birthDate = beneficiaryDesignation.getStringBirthDate();
						 if(StringUtils.isNotBlank(birthDate)){
							 setBirthDate(beneficiaryDesignation, birthDate);
						 }else{
							 beneficiaryDesignation.setBirthDate(null);
						 }
						 //set the share percentage in BigDecimal format
						 beneficiaryDesignation.setSharePct(
								 	new BigDecimal(beneficiaryDesignation.getStringSharePct()));
						 
						 confirmedBeneficiaryInformationList.add(beneficiaryDesignation);
						 beneficiaryNo++;
					 }
				 }
			 }
			//Set the updated list in beneficiary Designation list
				beneficiaryDesignationData.setBeneficiaryDesignationList(confirmedBeneficiaryInformationList);
			
			//Calculating current time stamp
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

			UserRole role=userProfile.getRole();
			if(role.isInternalUser()){
				createdUserIdType=BeneficiaryDesignationData.PWI_USER_ID_TYPE;
			}else if(role.isExternalUser()){
				createdUserIdType=BeneficiaryDesignationData.PWE_USER_ID_TYPE;
			}
			String createdUserId = String.valueOf(userProfile.getPrincipal().getProfileId());
			beneficiarySet=	new BeneficiarySet(contractNumber,new BigDecimal(profileId),currentTimestamp,
					BeneficiaryDesignationData.PSW_SOURCE_CHANNEL_CODE,
					BeneficiaryDesignationData.BM_SOURCE_FUNCTION_CODE,createdUserId,createdUserIdType,null, null);
			
			beneficiaryDesignationData.setBeneficiarySet(beneficiarySet);
			//Get the application id
			BaseEnvironment environment = new BaseEnvironment();
			applicationId = environment.getApplicationId();
			EmployeeServiceDelegate employeeServiceDelegate = 
				EmployeeServiceDelegate.getInstance(applicationId);
			
				employeeServiceDelegate.saveBeneficiaryDesignationData(beneficiaryDesignationData);
			} catch (SystemException e) {
				logger.error("Fail to update  Beneficiary information for profileId"
						+profileId+"and contract number"+contractNumber, e);
				throw e;
			}
			
			// add the profileId as parameter
			request.getSession(false).setAttribute(Constants.PROFILE_ID, profileId);
			// add the action as parameter
			request.getSession(false).setAttribute(Constants.ACTION, Constants.DEFAULT);

			if (logger.isDebugEnabled()) {
				logger.debug("exit -> doSave");
			}
			
			return forwards.get(Constants.VIEW);
			}
			
	}

	/**
	 * set the beneficiary date in the Date property of BeneficiaryDesignation
	 * @param beneficiaryDesignations
	 * @param birthDate
	 */
	private void setBirthDate(BeneficiaryDesignation beneficiaryDesignations,
			String birthDate) {
		try {
			synchronized (dateFormat) {
				beneficiaryDesignations.setBirthDate(dateFormat.parse(birthDate));	
			}
		} catch (ParseException e) {
			 logger.error("BirthDate Parsing failed ", e);
		}
	}

	
	/**
	 * Method to validate whether the given beneficiary count is reached to maximum or not.
	 * If yes, it will populate an error message
	 * 
	 * @param currentBeneficiaryCount
	 * @param maxBeneficiaryCount
	 * @param beneficiaryType 
	 * @param errorList
	 */
	private void validateBeneficiaryMaxCount(Integer currentBeneficiaryCount,
			Integer maxBeneficiaryCount, String beneficiaryType, List<ValidationError> errorList) {
		if (currentBeneficiaryCount >= maxBeneficiaryCount){
			ValidationError vError = new ValidationError(beneficiaryType,
					BeneficiaryErrorCodes.ERROR_ADD_MAXIMUM_BENEFICIARY,
					BeneficiaryErrorUtil.ErrorTypeMap.get(ErrorType.error));
			vError.setParams(new Object[]{0,beneficiaryType,maxBeneficiaryCount});
			errorList.add(vError);
		}
	}
	


	/**
	 * Method to Cancel the current Edit page request and redirects to the View page.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(params={"action=cancel"}, method = {RequestMethod.POST})
	public String doCancel(@Valid @ModelAttribute("beneficiaryForm") BeneficiaryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
	       }
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCancel");
		}
		
		// get the profileID
		String profileId = actionForm.getProfileId();

		// create the parameterized forward
		ParameterizedActionForward viewForward = new ParameterizedActionForward(forwards.get(Constants.VIEW));

		viewForward.addParameter(Constants.PROFILE_ID, profileId);
	
		


		if (logger.isDebugEnabled()) {
			logger.debug("exists -> doCancel");
		}
		return viewForward.getPath();
	}
	

	/** 
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations.
	 **/
	
	@Autowired
	   private PSValidatorFWDefault  psValidatorFWDefault;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWDefault);
	}
	
}
