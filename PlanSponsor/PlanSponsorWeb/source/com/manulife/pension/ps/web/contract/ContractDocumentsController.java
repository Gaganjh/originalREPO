package com.manulife.pension.ps.web.contract;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.ereports.ContractDocListRequest;
import com.manulife.pension.lp.model.ereports.ContractDocListResponse;
import com.manulife.pension.lp.model.ereports.ContractDocumentInfo;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;


/**
 * This action class is used to navigate to Contract
 * documents page.
 * 
 * @author
 */
@Controller
@RequestMapping( value = "/contract")

public class ContractDocumentsController extends PsController {
	
	
	
	@ModelAttribute("dynaForm")
	public DynaForm populateForm() 
	{ 
		return new DynaForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	private static final String CONTRACT_DOCUMENTS_PAGE = "contractDocuments";
	private static final String HOMEPAGE_FINDER_FORWARD_REDIRECT = "homePageFinderRedirect";
	private static final String INPUT = "input";
   
	static{
		forwards.put(INPUT,"/contract/contractDocuments.jsp");
		forwards.put(CONTRACT_DOCUMENTS_PAGE, "/contract/contractDocuments.jsp");
		forwards.put(HOMEPAGE_FINDER_FORWARD_REDIRECT,"redirect:/WEB-INF/do/home/homePageFinder/");
		} 


	 
	/**	
	 * Default Constructor
	 *
	 */
	public ContractDocumentsController() {
		super(ContractDocumentsController.class);
	}

	/**
	 * This method calls the business delegate and fetches the list of 
	 * Contract amendments and also contract document available for a particular contract number.
	 */
	
	@RequestMapping(value ="/contractDocuments/" , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        }
		UserProfile userProfile = getUserProfile(request);

		if (userProfile.isAllowedContractDocuments()&& checkUserAccess(userProfile)) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("entry -> doExecute");
				}
				Contract contract = userProfile.getCurrentContract();
				// get the list of contract and amendment documents available
				// for this contract number.
				ContractDocumentInfo[] contractDocInfo = getContractDocuments(
						userProfile, contract);
				// populate the values into ContractDocuments (value
				// object)which will be used in ContractDocuments page.
				ContractDocuments contractdocuments = buildContractDocumentsList(contractDocInfo);
				request.setAttribute(Constants.CONTRACT_DOCUMENTS_KEY,
						contractdocuments);
				if (contractdocuments.getAmendmentDocs() != null
						&& contractdocuments.getAmendmentDocs().size() > 0) {
					request.setAttribute(Constants.AMENDMENT_DOCUMENT, contractdocuments
							.getAmendmentDocs());
				}
				if (logger.isDebugEnabled())
					logger.debug(ContractDocumentsController.class.getName()
							+ ":forwarding to Contract Documents Page.");

				if (logger.isDebugEnabled()) {
					logger.debug("exit <- doExecute");
				}
			} catch (SystemException e) {
				List errors = new ArrayList();
				errors.add(new GenericException(
						ErrorCodes.REPORT_FILE_NOT_FOUND));
				setErrorsInRequest(request, errors);
				
				return forwards.get(INPUT);
			} catch (ServiceUnavailableException e) {
				List errors = new ArrayList();
				errors.add(new GenericException(
						ErrorCodes.REPORT_SERVICE_UNAVAILABLE));
				setErrorsInRequest(request, errors);
				
				return forwards.get(CONTRACT_DOCUMENTS_PAGE);
			}
			
			return forwards.get(CONTRACT_DOCUMENTS_PAGE);
		} else {
			//If user has no access to this page or if user navigates to this page
			//through bookmark , return to the HomePage
			
			return HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

	}

	/**
	 * This method builds the lable and value for the amendment list.
	 * @param contractInfos
	 * @return ContractDocuments
	 * @throws SystemException
	 */
	public ContractDocuments buildContractDocumentsList(
			ContractDocumentInfo[] contractInfos) throws SystemException {
		ContractDocuments contractdocuments = new ContractDocuments();

		for (int i = 0; i < contractInfos.length; i++) {
			String pdfFileName = null;
			try {
				pdfFileName = ContractDocumentsHelper
						.buildContractDocPdfFileName(contractInfos[i]);
			} catch (Exception e) {
				throw new SystemException(e, this.getClass().getName(),
						"buildContractDocumentsList",
						"Failed to build PDF: Contract Name ["
								+ contractInfos[i].getContractName() + "]");
			}

			contractdocuments.addDocuments(contractInfos[i].getDocumentType(),
					pdfFileName, ContractDocumentsHelper
							.buildContractLabel(contractInfos[i]));

		}
		
		return contractdocuments;
	}
	
	/**
	 * Returns all the ContractDocuments for this contract.
	 * @param userProfile
	 * @param contract
	 * @return ContractDocumentInfo
	 * @throws SystemException
	 * @throws ServiceUnavailableException
	 */
	public ContractDocumentInfo[] getContractDocuments(UserProfile userProfile,
			Contract contract) throws SystemException, ServiceUnavailableException {

		EReportsServiceDelegate delegate = EReportsServiceDelegate
				.getInstance();
		ContractDocListResponse contractDocListResponse = null;
		
		// prepares request parameters
		ContractDocListRequest contractDocListRequest = new ContractDocListRequest();
		contractDocListRequest.setContractNumber((new Integer(contract
				.getContractNumber())).toString());

		// logg the call to eReports service
		if (logger.isDebugEnabled())
			logger.debug("calling eReports service with the following param: "
								+ ";" + contractDocListRequest.getContractNumber());

		contractDocListResponse = delegate
				.getContractDocList(contractDocListRequest);
		
		return contractDocListResponse.getContractDocList();
	}

	/**
	 * This method checks if user has access to view ContractDocuments.
	 * 
	 * @param contract
	 * @return boolean
	 * @throws SystemException
	 */
	private boolean checkUserAccess(UserProfile userProfile)
			throws SystemException {
		boolean accessPresent = false;
		try {
			Contract contract = userProfile.getCurrentContract();
			ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate
					.getInstance();
			//Check whether contract or amendment document is available for this contract number.
			boolean isContractDocPresent = contractServiceDelegate
					.checkContractHasAmendmentOrContractDocuments((new Integer(contract
							.getContractNumber())).toString());
			if (userProfile.isTPA() || userProfile.isTrustee()) {
			    boolean isPaperConsentGiven = contractServiceDelegate
                .checkContractConsent(contract.getContractNumber());
				if (isPaperConsentGiven && isContractDocPresent) {
					accessPresent = true;
				}
			} else {
				if (isContractDocPresent) {
					accessPresent = true;
				}
			}
		} catch (ApplicationException applicationException) {
			throw new SystemException(applicationException.getMessage());
		}

		return accessPresent;
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	  @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}
