package com.manulife.pension.bd.web.bob.contract;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;

import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.util.pdf.FDFDocument;
import com.manulife.util.pdf.FDFField;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;


@Controller
@RequestMapping(value ="/bob")

public class BOBContractFDFController extends BDController {
	@ModelAttribute("dynaForm")
	public  DynaForm populateForm()
	{
		return new  DynaForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/bob/blockOfBusinessActive.jsp");
		forwards.put( "dummy ","/" );

	}

	/**
	 * Constructor
	 */
	public BOBContractFDFController() {
		super(BOBContractFDFController.class);
	} 
	
	 /** 
	 * Action method to populate the open FDF parameters that is passed 
	 * to the PDF
	 * @param mapping
	 * 				ActionMapping
	 * @param form
	 * 				Form
	 * @param request
	 * 				HttpServletRequest
	 * @param response
	 * 				HttpServletResponse
	 * @return ActionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	 @RequestMapping(value ="/ContractFDFAction.fdf",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	

	    if(logger.isDebugEnabled()) {
		    logger.debug("entry FDFAction -> doExecute");
	    }
	 
	    FDFDocument doc = new FDFDocument();
        BobContext bobContext = getBobContext(request);
        Contract currentContract = bobContext.getCurrentContract();

   
	    // Add the contract related general parameters
		doc.addField(new FDFField(CommonConstants.CONTRACT_HOLDER, currentContract.getCompanyName()));
		doc.addField(new FDFField(CommonConstants.CONTRACT_NUMBER, Integer.toString(currentContract.getContractNumber())));
		doc.addField(new FDFField(CommonConstants.AS_OF_DATE, getFormattedAsOfDate(currentContract)));
		doc.addField(new FDFField(BDConstants.CURRENT_DATE, getFormattedAsOfDate(currentContract)));
	    
	    
	    // Set the PDF filename if specified
		if (request.getParameter("pdfForm") != null) {
			doc.setPDFFilename((String) request.getParameter("pdfForm"));
		}

    	byte[] bytes = doc.toFDFFormat().getBytes();
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    	response.setHeader("Pragma", "no-cache");
    	response.setHeader("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
  	  	response.setContentType("application/vnd.fdf");
    	response.getOutputStream().write(bytes);

	    if(logger.isDebugEnabled()) {
		    logger.debug("exit FDFAction <- doExecute");
	    }
	    
	   	return null; 
	}
	
	/**
	 * Formats the contract's as of date to a format MMMM dd, yyyy
	 * @param contract
	 * @return String
	 */
	private String getFormattedAsOfDate(Contract contract) {
		Date asofdate = contract.getContractDates().getAsOfDate();
		return DateRender.formatByPattern(asofdate, "", RenderConstants.EXTRA_LONG_MDY);		
	}
	
	/**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	/**
	 * This method calls doValidate for doing validation.
	 * 
	 * @param form
	 *            Form objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * 
	 * @return Collection of errors
	 */
	
	 @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
	
}