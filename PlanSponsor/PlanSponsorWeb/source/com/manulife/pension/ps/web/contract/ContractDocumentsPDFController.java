package com.manulife.pension.ps.web.contract;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.bos.ereports.common.ContractDocumentNotFoundException;
import com.manulife.pension.lp.model.ereports.ContractDocFileRequest;
import com.manulife.pension.lp.model.ereports.ContractDocFileResponse;
import com.manulife.pension.lp.model.ereports.ContractDocumentInfo;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.HomePageForm;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWNull;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;

/**
 * This class is used to render ContractAmendment/Contract document PDF
 * in new window.
 * 
 * @author
 */

@Controller
@RequestMapping( value = "/contract")

public class ContractDocumentsPDFController extends PsController {
	
	
	@ModelAttribute(" homePageForm") public  HomePageForm populateForm() {return new  HomePageForm();}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	private static Environment env = Environment.getInstance();

	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";
	private static final String SEC_WINDOW_ERROR = "secondaryWindowError";
	static{
		forwards.put(SEC_WINDOW_ERROR,"/WEB-INF/global/secondaryWindowError.jsp");
	}
	/**
	 * Default Constructor
	 */
	public ContractDocumentsPDFController() {
		super(ContractDocumentsPDFController.class);
	}

	/**
	 * This method calls the business delegate and renders ContractAmendment/Contract document PDF
	 * on new window.
	 */
	
	@RequestMapping(value ="/contractDocumentsPDF/", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("homePageForm") HomePageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
        	}
        }
	
		List errors = new ArrayList();

		String sitemode = env.getSiteLocation();
		String fileName = request.getParameter(Constants.CONTRACT_DOCUMENT);

		// logg the action
		if (logger.isDebugEnabled()) {
			logger.debug(fileName);
		}
		try {
			// get the user profile object and set the current contract to null
			UserProfile userProfile = getUserProfile(request);
			Contract currentContract = userProfile.getCurrentContract();
			ContractDocumentInfo contractDocInfo = ContractDocumentsHelper
					.parseContractDocPdfFileName(fileName);
			if (logger.isDebugEnabled()) {
				logger.debug(contractDocInfo);
			}

			ContractDocFileResponse fileResponse = getContractDocument(
					currentContract, contractDocInfo);

			response.setContentType(Constants.MIME_TYPE_PDF);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setContentLength(fileResponse.getLength());
			OutputStream out = response.getOutputStream();
			out.write(fileResponse.getReportFragment());
			out.close();

		} catch (ContractDocumentNotFoundException e) {
			errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
		} catch (ServiceUnavailableException e) {
			errors.add(new GenericException(
					ErrorCodes.REPORT_SERVICE_UNAVAILABLE));
		} catch (ParseException e) {
			throw new SystemException(e, getClass().getName(), "doExecute",
					"Failed to get report. Site mode [" + sitemode
							+ "] fileName [" + fileName + "]");
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
		contractInfo.setContractNumber((new Integer(contract
				.getContractNumber())).toString());

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
	
	/*
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWNull psValidatorFWNull;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWNull);
	}

}

