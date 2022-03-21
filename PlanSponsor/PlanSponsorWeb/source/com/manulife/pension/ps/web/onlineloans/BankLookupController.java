package com.manulife.pension.ps.web.onlineloans;

import java.io.IOException;
import java.util.Iterator;
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

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.environment.valueobject.BankBranch;

@Controller
@RequestMapping(value ="/onlineloans")
@SessionAttributes({"bankLookupForm"})

/**
 * This action processes the bank lookup request. It requires two parameters:
 * abaRoutingNumber and paymentMethodCode. The result is a list of bank
 * information in JSON format. The bank information object has two attributes,
 * the first one is the aba number and the second one is the name of the bank.
 */
public class BankLookupController extends PsAutoController {

	
	@ModelAttribute("bankLookupForm")
	public AutoForm populateForm()
	{
		return new AutoForm();
	}

	public static final String PARAM_ABA_ROUTING_NUMBER = "abaRoutingNumber";

	public static final String PARAM_PAYMENT_METHOD_CODE = "paymentMethodCode";

	
	@RequestMapping(value ="/bankLookup/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("bankLookupForm") AutoForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		return null;
	}
	@RequestMapping(value ="/bankLookup/" ,params={"action=lookup"}, method =  {RequestMethod.GET}) 
	public String doLookup (@Valid @ModelAttribute("bankLookupForm") AutoForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	

		String abaRoutingNumber = request
				.getParameter(PARAM_ABA_ROUTING_NUMBER);
		if (StringUtils.isBlank(abaRoutingNumber)) {
			return null;
		}
		String paymentMethodCode = request
				.getParameter(PARAM_PAYMENT_METHOD_CODE);
		if (StringUtils.isBlank(paymentMethodCode)) {
			return null;
		}

		EnvironmentServiceDelegate environmentServiceDelegate = EnvironmentServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID);

		List<BankBranch> banks = environmentServiceDelegate
				.getBanksByAbaRoutingNumber(paymentMethodCode, abaRoutingNumber);

		StringBuffer jsonResult = new StringBuffer("[");

		for (Iterator<BankBranch> it = banks.iterator(); it.hasNext();) {
			BankBranch bank = it.next();
			jsonResult.append("{\"aba\":\"").append(abaRoutingNumber).append(
					"\",\"name\":\"").append(bank.getBankName()).append("\"}");
			if (it.hasNext()) {
				jsonResult.append(",");
			}
		}

		jsonResult.append("]");

		response.setHeader("Cache-Control", "must-revalidate");
		response.setContentType("application/json");
		byte[] jsonResultBytes = jsonResult.toString().getBytes();
		response.setContentLength(jsonResultBytes.length);

		try {
			response.getOutputStream().write(jsonResultBytes);
		} catch (IOException ioException) {
			throw new SystemException(ioException,
					"Exception writing json result.");
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException ioException) {
				throw new SystemException(ioException,
						"Exception closing output stream.");
			}
		}

		return null;
	}
	
		
	/**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 */
	 @Autowired
	   private PSValidatorFWDefault  psValidatorFWDefault;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWDefault);
	}
	
}
