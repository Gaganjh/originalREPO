package com.manulife.pension.bd.web.bob.contract;


import static com.manulife.pension.platform.web.CommonErrorCodes.REPORT_FILE_NOT_FOUND;

import java.io.IOException;
import java.io.OutputStream;
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
import com.manulife.pension.documents.model.DocumentFileOutput;
import com.manulife.pension.documents.model.FeeDisclosureDocumentInfo;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.order.delegate.OrderServiceDelegate;
import com.manulife.pension.util.content.GenericException;

/**
 * This class is used to read the generated fee disclosure pdf
 * and to show it to the user 
 * 
 * @author Eswar
 * 
 */
@Controller
@RequestMapping(value ="/bob")

public class FeeDisclosurePdfController extends BDController {
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/bob/blockOfBusinessActive.jsp");
		}

	public FeeDisclosurePdfController() {
		super(FeeDisclosurePdfController.class);
	}
	
	

	
	@RequestMapping(value ="/feeDiscolsure/feeDisclosurePdfReport/",method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
		
		if (logger.isDebugEnabled()) {
			logger.debug(" entered FeeDisclosurePdfAction.doDefault");
		}

		List<GenericException> errors = new ArrayList<GenericException>();
		int contractNumber = 0;
		
		try {
			
			BobContext bobContext = getBobContext(request);
		    Contract currentContract = bobContext.getCurrentContract();
			contractNumber = currentContract.getContractNumber();
			
			FeeDisclosureDocumentInfo documentInfo = OrderServiceDelegate
					.getInstance().getInforceFeeDisclosurePdfDetails(contractNumber, 24);
			
			if(	documentInfo == null) {
				throw new SystemException("Fee Inforce PDF not generated for contract # " + contractNumber);
			}
			
			EReportsServiceDelegate delegate = EReportsServiceDelegate.getInstance();
			DocumentFileOutput fileOutput = delegate.getDocument(documentInfo);
			
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
			response.setHeader("Pragma", "no-cache");
			response.setContentType(CommonConstants.MIME_TYPE_PDF);
			response.setContentLength(fileOutput.getLength());
			OutputStream out = response.getOutputStream();
			out.write(fileOutput.getReportFragment());
			out.flush();
			out.close();
			
		} catch (Exception e) {
			logger.error("Error getting fee disclosure inforce pdf for contract # "+ contractNumber);
			errors.add(new GenericException(REPORT_FILE_NOT_FOUND));
			setErrorsInRequest(request, errors);
            return forwards.get(BDConstants.SECONDARY_WINDOW_ERROR_FORWARD);
		} 
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit FeeDisclosurePdfAction.doDefault");
		}
		
		return null;
	}
	
	/**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
	
}