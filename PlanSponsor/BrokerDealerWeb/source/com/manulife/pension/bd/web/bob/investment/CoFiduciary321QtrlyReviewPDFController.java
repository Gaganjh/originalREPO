package com.manulife.pension.bd.web.bob.investment;

import static com.manulife.pension.platform.web.CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE;

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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.contract.ContractDocumentsHelper;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;

/**
 * This is the action class for quarterly Wilshire 3(21) Adviser Service Report PDF generation.
 * 
 * @author Sreenivasa Koppula
 * 
 */
@Controller
@RequestMapping( value = "/bob")

public class CoFiduciary321QtrlyReviewPDFController extends BDController{
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/cofid321QtrlyReview.jsp");
		forwards.put("secondaryWindowError","/WEB-INF/global/secondaryWindowError.jsp");
		
		
		}

	/**
	 * Default Constructor
	 */
	public CoFiduciary321QtrlyReviewPDFController() {
		super(CoFiduciary321QtrlyReviewPDFController.class);
	}
	
	/**
     * This method calls the business delegate and renders Wilshire 3(21) Adviser
     * Service document PDF on new window.
     * 
     * @see BaseController#doExecute()
     */
	@RequestMapping(value ="/investment/coFiduciary321QtrlyReviewPDFAction/", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("coFiduciary321QtrlyReviewPageForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
	
		List<GenericException> errors = new ArrayList<GenericException>();

		try {
			// get the user profile object and set the current contract to null
            BobContext bob = getBobContext(request);
            Contract currentContract = bob.getCurrentContract();
            
            //Method to populate the quarterly PDF document for selected contract.
            ContractDocumentsHelper.getPdfContractDocument(request, response, currentContract);
		
		} catch (ServiceUnavailableException e) {
			errors.add(new GenericException(REPORT_SERVICE_UNAVAILABLE));
		} 

		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
            return forwards.get(BDConstants.SECONDARY_WINDOW_ERROR_FORWARD);
		} else {
			return null;
		}
	}
	
	/**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
	
}
