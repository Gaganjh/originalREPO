package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.service.environment.valueobject.BankBranch;


/**
 * WithdrawalStep2Action is the action class for the withdrawal entry step 2 page.
 * 
 * @author Dennis_Snowdon
 * @author glennpa
 * @version 1.1.2.8 2006/08/29 19:02:36
 */
@Controller
@RequestMapping( value = "/withdrawal")
@SessionAttributes({"bankLookupForm"})

public class WithdrawalBankLookupController extends BaseWithdrawalController {
	
	@ModelAttribute("bankLookupForm")
	public AutoForm populateForm()
	{
		return new AutoForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	public static final String PARAM_ABA_ROUTING_NUMBER = "abaRoutingNumber";

	public static final String PARAM_PAYMENT_METHOD_CODE = "paymentMethodCode";


    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(WithdrawalBankLookupController.class);



    
    @RequestMapping(value = "/bankNamesList/",  method =  {RequestMethod.POST,RequestMethod.GET})
   	public String doDefaul(@Valid @ModelAttribute("bankLookupForm") AutoForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {   	
        return null;
    }
    
    
   
    
    @RequestMapping(value = "/bankNamesList/ajax/",  method =  {RequestMethod.POST})
   	public String doAjaxPost(@Valid @ModelAttribute("bankLookupForm") AutoForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
      logger.info("doAjaxPost> Entry - starting timer.");
      String abaRoutingNumber = "";
   	  String paymentMethodCode = "";
    
	  final String jsonObj = IOUtils.toString(request.getInputStream());
		String jsonText = "[" + jsonObj + "]";
		JsonParser parser = new JsonParser();
		JsonArray jsonArr = (JsonArray) parser.parse(jsonText);
		if (StringUtils.isNotEmpty(jsonObj)) {
			for (int i = 0; i < jsonArr.size(); i++) {
				JsonObject obj = (JsonObject) jsonArr.get(i);
				abaRoutingNumber = (obj.get(PARAM_ABA_ROUTING_NUMBER)).getAsString();
				paymentMethodCode = (obj.get(PARAM_PAYMENT_METHOD_CODE)).getAsString();
				
			}
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
		
		logger.info("doAjaxPost> end - ending timer.");            
        return null;
    }
}