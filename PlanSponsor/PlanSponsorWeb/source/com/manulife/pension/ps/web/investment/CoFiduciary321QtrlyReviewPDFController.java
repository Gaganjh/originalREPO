package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.util.ArrayList;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.contract.ContractDocumentsHelper;
import com.manulife.pension.platform.web.investment.CoFiduciary321QtrlyReviewPageForm;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;

/**
 * This class is used to render CoFid321 Contract document PDF
 * in new window.
 * 
 * @author Sreenivasa Koppula
 */
@Controller
@RequestMapping( value = "/investment")

public class CoFiduciary321QtrlyReviewPDFController extends PsController {

	@ModelAttribute("coFiduciary321QtrlyReviewPageForm") 
	public CoFiduciary321QtrlyReviewPageForm populateForm() 
	{
		return new CoFiduciary321QtrlyReviewPageForm();
		} 
		public static HashMap<String,String> forwards = new HashMap<String,String>();
		static{
			forwards.put("secondaryWindowError","/WEB-INF/global/secondaryWindowError.jsp");
		}

	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";
	
	/**
	 * Default Constructor
	 */
	public CoFiduciary321QtrlyReviewPDFController() {
		super(CoFiduciary321QtrlyReviewPDFController.class);
	}
	
	/**
	 * This method calls the business delegate and renders Contract document PDF
	 * on new window.
	 */
	@RequestMapping(value ="/coFiduciary321QtrlyReviewPDFAction/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("coFiduciary321QtrlyReviewPageForm") CoFiduciary321QtrlyReviewPageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}

		List errors = new ArrayList();

		try {
			// get the user profile object and set the current contract to null
			UserProfile userProfile = getUserProfile(request);
			Contract currentContract = userProfile.getCurrentContract();
			
			//Method to populate the quarterly PDF document for selected contract.
			ContractDocumentsHelper.getPdfContractDocument(request, response, currentContract);

		} catch (ServiceUnavailableException e) {
			errors.add(new GenericException(
					ErrorCodes.REPORT_SERVICE_UNAVAILABLE));
		} 

		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
			request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
					.getInstance().getPageBean(EMPTY_LAYOUT_ID));

			return forwards.get(Constants.SECONDARY_WINDOW_ERROR_FORWARD);
		} else {

			return null;
		}
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}
	
}
