package com.manulife.pension.bd.web.bob.contract;

import static com.manulife.pension.platform.web.CommonConstants.CONTRACT_DOCUMENT;
import static com.manulife.pension.platform.web.CommonConstants.MIME_TYPE_PDF;
import static com.manulife.pension.platform.web.CommonErrorCodes.REPORT_FILE_NOT_FOUND;
import static com.manulife.pension.platform.web.CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Init;
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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.bos.ereports.common.ContractDocumentNotFoundException;
import com.manulife.pension.lp.model.ereports.ContractDocFileRequest;
import com.manulife.pension.lp.model.ereports.ContractDocFileResponse;
import com.manulife.pension.lp.model.ereports.ContractDocumentInfo;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;

/**
 * This class is used to render ContractAmendment/Contract document PDF
 * in new window.
 * 
 * @author
 */
@Controller
@RequestMapping(value ="/bob")

public class ContractDocumentsPDFController extends BDController {
	@ModelAttribute("dynaForm")
	public DynaForm populateForm()
	{
	return	new DynaForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/contractDocuments.jsp");
		forwards.put("secondaryWindowError","/global/secondaryWindowError.jsp");
		}

	private static Environment env = Environment.getInstance();

	/**
	 * Default Constructor
	 */
	public ContractDocumentsPDFController() {
		super(ContractDocumentsPDFController.class);
	}

    /**
     * This method calls the business delegate and renders ContractAmendment/Contract document PDF
     * on new window.
     * 
     * @see BaseController#doExecute()
     */
	@RequestMapping(value ="/contract/contractDocumentsPDF/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
			
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		// String forward =super.doExecute( actionForm, request, response);
		List<GenericException> errors = new ArrayList<GenericException>();

		String sitemode = env.getSiteLocation();
		String fileName = request.getParameter(CONTRACT_DOCUMENT);

		// log the action
		if (logger.isDebugEnabled()) {
			logger.debug(fileName);
		}
		try {
			// get the user profile object and set the current contract to null
            BobContext bob = getBobContext(request);
            Contract currentContract = bob.getCurrentContract();
			ContractDocumentInfo contractDocInfo = ContractDocumentsHelper
					.parseContractDocPdfFileName(fileName);
			
			if (logger.isDebugEnabled()) {
				logger.debug(contractDocInfo);
			}

			ContractDocFileResponse fileResponse = getContractDocument(
					currentContract, contractDocInfo);

			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
			response.setHeader("Pragma", "no-cache");
			response.setContentType(MIME_TYPE_PDF);
			response.setContentLength(fileResponse.getLength());
			OutputStream out = response.getOutputStream();
			out.write(fileResponse.getReportFragment());
			out.close();

		} catch (ContractDocumentNotFoundException e) {
			errors.add(new GenericException(REPORT_FILE_NOT_FOUND));
		} catch (ServiceUnavailableException e) {
			errors.add(new GenericException(REPORT_SERVICE_UNAVAILABLE));
		} catch (ParseException e) {
			throw new SystemException(e, getClass().getName(), "doExecute",
					"Failed to get report. Site mode [" + sitemode
							+ "] fileName [" + fileName + "]");
		}

		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
            return forwards.get(BDConstants.SECONDARY_WINDOW_ERROR_FORWARD);
		} else {
			return null;
		}
	}

	/**
	 * This method calls the business delegate and renders
	 * ContractAmendment/Contract document PDF on new window.
	 * 
	 * @param contract
	 * @param contractInfo
	 * @return ContractDocFileResponse
	 * @throws SystemException
	 * @throws ServiceUnavailableException
	 * @throws ContractDocumentNotFoundException
	 */
	public ContractDocFileResponse getContractDocument(Contract contract,
			ContractDocumentInfo contractInfo) throws SystemException,
			ServiceUnavailableException, ContractDocumentNotFoundException {
		
		// overwrite the contract number in any case
		// the one coming from the browser cannot be trusted
        contractInfo.setContractNumber(String.valueOf(contract.getContractNumber()));

		EReportsServiceDelegate delegate = EReportsServiceDelegate
				.getInstance();

		// prepares request parameters
		ContractDocFileRequest contractFileRequest = new ContractDocFileRequest();
		contractFileRequest.setContractNumber((new Integer(contract
				.getContractNumber())).toString());
		contractFileRequest.setDocumentDate(contractInfo
				.getDocumentCreatedDate());
		contractFileRequest.setContractDocType(contractInfo.getDocumentType());
		contractFileRequest
				.setReportKeys(new ContractDocumentInfo[] { contractInfo });

		return delegate.getContractDocFile(contractFileRequest);
	}
	
	/**This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 * 
	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
}
