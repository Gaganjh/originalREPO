package com.manulife.pension.ps.web.census.beneficiary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.CensusConstants;
import com.manulife.pension.ps.web.census.beneficiary.util.BeneficiaryUtil;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.census.util.ParameterizedActionForward;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryDesignation;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryDesignationData;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiarySet;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.BaseEnvironment;

/**
 * ViewBeneficiaryAction is Used to View the Beneficiary Information.
 *
 * @author Manjunath
 */
@Controller
@RequestMapping(value = "/census/beneficiary")
@SessionAttributes({"beneficiaryForm"})

public class ViewBeneficiaryController extends PsAutoController {

	@ModelAttribute("beneficiaryForm")
	public BeneficiaryForm populateForm()
	{
		return new BeneficiaryForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	static{
		forwards.put("default","/census/beneficiary/viewBeneficiaryInformation.jsp");
		forwards.put("view","redirect:/do/census/viewEmployeeSnapshot/");
		forwards.put("edit","redirect:/do/census/beneficiary/editBeneficiaryInformation/");
		forwards.put("challengePasscode", "redirect:/do/passcodeTransition/");
		}

	private static EmployeeServiceFacade serviceFacade = new EmployeeServiceFacade();

	/**
	 * doDefault is for the default access of the view action
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 *
	 * @throws IOException, ServletException, SystemException
	 * @return ActionForward
	 */  

	@RequestMapping(value="/viewBeneficiaryInformation/",method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("beneficiaryForm") BeneficiaryForm actionForm,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> ViewBeneficiaryAction -> doDefault");
		}
		List<BeneficiaryDesignation> primaryBeneficiaryList=null;
		
		List<BeneficiaryDesignation> contingentBeneficiaryList=null;
		
		// get the current userProfile Object
		UserProfile userProfile = SessionHelper.getUserProfile(request);


		//To get the Current Contract number.
		String contractNumber = String.valueOf(userProfile.getCurrentContract().getContractNumber());
		
		//Beneficiary Form
		BeneficiaryForm beneficiaryForm = (BeneficiaryForm) actionForm; 
		
		//To get the profile Id from Form.
		String profileId = beneficiaryForm.getProfileId();

		long profileID = 0L;
		try {
			// get the profileId from the actionForm
			profileID = Long.parseLong(profileId);
		} catch (NumberFormatException e) {
			throw new SystemException(e, "Unable parse the profileId String "
					+ beneficiaryForm.getProfileId());
		}
		
		//Set the employee relation information in the beneficiary Page
		Employee employee = serviceFacade.getEmployee(profileID, userProfile, new Date(), false);
		
		// To check whether OBDS csf indicator is ON/OFF
		if (!(BeneficiaryUtil.isOBDSCSFAvailable(contractNumber)
				&& BeneficiaryUtil.isAccessibleForEmployee(employee, userProfile))) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
	

		// it will check logged in user does not have the Update Census Data
		// permission or the Contract Status is equal to Discontinued
		// then do not display edit button
		beneficiaryForm.setAllowedToEdit(userProfile
				.isAllowedUpdateCensusData()
				&& !Contract.STATUS_CONTRACT_DISCONTINUED.equals(userProfile
						.getCurrentContract().getStatus()));
		

		//get Beneficiary data from Employee Service Delegate.
		BeneficiaryDesignationData beneficiaryDesignationData = EmployeeServiceDelegate
				.getInstance(new BaseEnvironment().getApplicationId())
				.getBeneficiaryDesignationData(profileId, contractNumber);

		//set the Employee information (First Name, Last Name, Last Updated user and date.
		BeneficiarySet beneficiarySet = beneficiaryDesignationData.getBeneficiarySet();
		
		BeneficiaryUtil.setEmployeeInformation(userProfile, beneficiaryForm,
				beneficiarySet, employee);
		primaryBeneficiaryList =  new ArrayList<BeneficiaryDesignation>();
		contingentBeneficiaryList = new ArrayList<BeneficiaryDesignation>();
		if(beneficiaryDesignationData != null){
			//set Beneficiary relationship Map into 
			BeneficiaryUtil.getBeneficiaryRelationshipAndType(beneficiaryForm);
			//set the primary beneficiary information into form.
			if (beneficiaryDesignationData
					.getPrimaryOrContingentBeneficiaryList(BeneficiaryDesignationData.PRIMARY_BENEFICIARY_TYPE_CODE) != null) {
				
				primaryBeneficiaryList = beneficiaryDesignationData
						.getPrimaryOrContingentBeneficiaryList(BeneficiaryDesignationData.PRIMARY_BENEFICIARY_TYPE_CODE);

				//this flag is to check whether to display primary beneficiary information or not.
				beneficiaryForm.setPrimaryBeneficiaryExists(true);
			}else{
				beneficiaryForm.setPrimaryBeneficiaryExists(false);
			}

			//set the Contingent beneficiary information into form.
			if (beneficiaryDesignationData
					.getPrimaryOrContingentBeneficiaryList(BeneficiaryDesignationData.CONTINGENT_BENEFICIARY_TYPE_CODE) != null) {
				//this flag is check whether to display Contingent beneficiary information or not.
				contingentBeneficiaryList = beneficiaryDesignationData
						.getPrimaryOrContingentBeneficiaryList(BeneficiaryDesignationData.CONTINGENT_BENEFICIARY_TYPE_CODE);
				
				beneficiaryForm.setContingentBeneficiaryExists(true);
			} else {
				beneficiaryForm.setContingentBeneficiaryExists(false);
			}
		}
		//Set the primary Beneficiary into the form 
		if(primaryBeneficiaryList != null){
			beneficiaryForm.setPrimaryBeneficiaries(primaryBeneficiaryList);
		}
		//Set the contingent Beneficiary into the form 
		if(contingentBeneficiaryList!=null){
			beneficiaryForm.setContingentBeneficiaries(contingentBeneficiaryList);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exits -> ViewBeneficiaryAction -> doDefault");
		}
		return forwards.get(Constants.DEFAULT);
	}

	/**
	 * doBack is for back button action. Redirect to previews page.
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 *
	 * @return ActionForward
	 */
	@RequestMapping(value="/viewBeneficiaryInformation/",params={"action=back"}, method = {RequestMethod.POST})
	public String doBack(@Valid @ModelAttribute("beneficiaryForm") BeneficiaryForm actionForm,
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
			logger.debug("entry -> ViewBeneficiaryAction -> doBack");
		}

		// get the profileID
		String profileId = actionForm.getProfileId();

		// create the parameterized forward
		String f = forwards.get(Constants.BACK_TO_VIEW_PAGE);
		 ParameterizedActionForward back = new ParameterizedActionForward(f);
		

		// add the profileId as parameter
		back.addParameter(Constants.PROFILE_ID, profileId);

		// add the source parameter
		back.addParameter(Constants.SOURCE, CensusConstants.CENSUS_SUMMARY_PAGE);

		if (logger.isDebugEnabled()) {
			logger.debug("exits -> ViewBeneficiaryAction -> doBack");
		}
		return back.getPath();
	}

	/**
	 * doEdit is for Edit button action. Redirect to Edit Beneficiary Information page.
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 *
	 * @return ActionForward
	 */
	@RequestMapping(value="/viewBeneficiaryInformation/",params={"action=edit"}, method = {RequestMethod.POST})
	public String doEdit(@Valid @ModelAttribute("beneficiaryForm") BeneficiaryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
	       }
		}
		UserProfile userProfile = getUserProfile(request);
		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(userProfile.getPrincipal());
		boolean isExternalUser = userProfile.getRole() instanceof ExternalUser;
		request.getSession(false).setAttribute("challengeRequestFrom", "editBeneficiaryInformationPage");
		if (isExternalUser && userInfo.getSsn().equals(actionForm.getEmployeeSSN()) && null != request.getSession(false).getAttribute(Constants.CHALLENGE_PASSCODE_IND)) {
				boolean isChallengedAlready = (Boolean) request.getSession(false)
						.getAttribute(Constants.CHALLENGE_PASSCODE_IND);
				
				if (isChallengedAlready) {
					//request.getSession().setAttribute("challengeRequestFrom", "editBeneficiaryInformationPage");
					request.getSession(false).setAttribute("employeeProfileId",
							Long.parseLong(actionForm.getProfileId()));
					return forwards.get("challengePasscode");
				}
		}
		return forwards.get(Constants.EDIT_BENEFICIARY_INFORMATION_PAGE);
	}
	
	/** 
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations.
	 **/
	
	@Autowired
	   private PSValidatorFWDefault  psValidatorFWDefault;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWDefault);
	}
	
}
