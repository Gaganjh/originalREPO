package com.manulife.pension.ps.web.userguide;

import java.io.IOException;
import java.util.HashMap;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWLandingPage;
import com.manulife.pension.service.security.role.permission.PermissionType;

@Controller
@RequestMapping( value = "/contentpages")
public class LandingPageController extends PsController 
{
	@ModelAttribute("landingpagForm")
	public DynaForm populateForm() {
		return new DynaForm();
	}
//	public LandingPageForm populateForm() {
//		return new LandingPageForm();
//	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {

		forwards.put("landingpage", "/contentpages/userguide/landingpage.jsp");
	}	
	
	private static final String LANDING_PAGE = "landingpage";
	private static final String AUTO_SIGNUP_IND = "autoSignupInd";
	//private static final String PARENT_ID_FOR_FIDUCIARY_PAGE = "75";
	//private static final String PRE_SIGNATURE_PRODUCT_IND = "Y";
	
	public LandingPageController()
	{
		super(LandingPageController.class);
	} 
	
	@RequestMapping(value ="/userguide/landingpage/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("landingpagForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
		 if(StringUtils.isNotBlank(forward)) {
			return  StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		 }
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(LANDING_PAGE);//if input forward not //available, provided default
             //forwards.get(LANDING_PAGE);//if input forward not //available, provided default
        	}
        } 
		
		//LandingPageForm inputForm = (LandingPageForm) form;

		UserProfile up =(UserProfile) request.getSession(false).getAttribute(
				Constants.USERPROFILE_KEY);
		
		String parentId = request.getParameter("parentId");
		String autoSignup = ContractServiceDelegate.getInstance().determineSignUpMethod(up.getCurrentContract().getContractNumber());
		request.getSession(false).setAttribute(AUTO_SIGNUP_IND, autoSignup);
		/*
		// request for Fiduciary page
		if (PARENT_ID_FOR_FIDUCIARY_PAGE.equals(parentId)) {
			int disclosureRoadMapContent = disclosureRoadMap(up);
			request.setAttribute(Constants.DISCLOSURE_ROADMAP_PARAMETER, disclosureRoadMapContent);
		}*/
		
		//We are allowing users with selected access permission to access this 
		//page via securityInfo.xml. However, we need to restrict them from 
		//accessing the Admin Guides and Fiduciary Guides
		if(up.getPrincipal().getRole().hasPermission(PermissionType.SELECTED_ACCESS) && 
				("73".equals(parentId) || "75".equals(parentId)))
			return forwards.get("homePageFinder");
					
		return forwards.get(LANDING_PAGE);
	}	
	
	/**
	 * return the content id for the contract
	 * 
	 * @param userProfile
	 *            UserProfile
	 * @return int
	 * @throws SystemException
	 */	
	/*
	private int disclosureRoadMap(UserProfile userProfile) throws SystemException {
		
		BusinessParameterValueObject businessParameter = null;
		AdditionalContractData additionalContractData = null;
		boolean preSignatureProductInd = false;
		Date proposalPrintDate = null;
		int disclosureRoadMapContent;
		Date erisa408B2EffectiveDate = null;
		
		try {
			additionalContractData = AccountServiceDelegate.getInstance().getAdditionalContractData(
							String.valueOf(userProfile.getCurrentContract().getContractNumber()));
			preSignatureProductInd = (PRE_SIGNATURE_PRODUCT_IND.equalsIgnoreCase(
							additionalContractData.getPreSignatureProductInd())) ? true	: false;
			proposalPrintDate = additionalContractData.getProposalPrintDate();			
			businessParameter = PartyServiceDelegate.getInstance().getBuisnessParameterValueObject();
			erisa408B2EffectiveDate = businessParameter.getErisa408B2EffectiveDate();

			if (preSignatureProductInd) {
				disclosureRoadMapContent = ContentConstants.DISCLOSURE_ROADMAP_WITH_PRE_SIGNATURE;
			} else if (proposalPrintDate.compareTo(erisa408B2EffectiveDate) >= 0) {
				disclosureRoadMapContent = ContentConstants.DISCLOSURE_ROADMAP_WITH_POST_SIGNATURE_NEW_BUSINESS_VERSION;
			} else {
				disclosureRoadMapContent = ContentConstants.DISCLOSURE_ROADMAP_WITH_POST_SIGNATURE_INFORCE_VERSION;
			}
		} catch (Exception exception) {
			throw new SystemException(exception,
					"Error thrown while calling disclosureRoadMap in LandingPageAction "
							+ exception.toString());
		}

		return disclosureRoadMapContent;
	} */
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#137697.
	 */
	@Autowired
	   private PSValidatorFWLandingPage  psValidatorFWLandingPage;

	@InitBinder  
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWLandingPage);
	}
	
}
