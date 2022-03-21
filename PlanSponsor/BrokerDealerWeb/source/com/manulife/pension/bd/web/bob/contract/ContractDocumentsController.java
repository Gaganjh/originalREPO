package com.manulife.pension.bd.web.bob.contract;

import static com.manulife.pension.bd.web.BDConstants.BOB_PAGE_FORWARD;
import static com.manulife.pension.platform.web.CommonConstants.AMENDMENT_DOCUMENT;
import static com.manulife.pension.platform.web.CommonConstants.CONTRACT_DOCUMENTS_KEY;
import static com.manulife.pension.platform.web.CommonErrorCodes.REPORT_FILE_NOT_FOUND;
import static com.manulife.pension.platform.web.CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.lp.model.ereports.ContractDocListRequest;
import com.manulife.pension.lp.model.ereports.ContractDocListResponse;
import com.manulife.pension.lp.model.ereports.ContractDocumentInfo;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;

/**
 * This action class is used to navigate to Contract documents page.
 * 
 * @author Siby Thomas
 */



@Controller
@RequestMapping(value ="/bob/contract")
public class ContractDocumentsController extends BDController {
	@ModelAttribute("dynaForm")
	public DynaForm populateForm() 
	{
		return new DynaForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/contractDocuments.jsp");
		forwards.put("contractDocuments","/contract/contractDocuments.jsp");
		forwards.put("bobPage","redirect:/do/bob/blockOfBusiness/Active/");
	}
	private static final String CONTRACT_DOCUMENTS_PAGE = "contractDocuments";
    
	/**	
	 * Default Constructor
	 *
	 */
	public ContractDocumentsController() {
		super(ContractDocumentsController.class);
	}

    /**
     * This method calls the business delegate and fetches the list of Contract amendments and also
     * contract document available for a particular contract number.
     * 
     * @see BaseController#doExecute()
     */
	@RequestMapping(value ="/contractDocuments/", method ={RequestMethod.POST,RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm DynaForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(DynaForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
        		
        if (checkUserAccess(request)) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("entry -> doExecute");
				}
				
				Contract contract = getBobContext(request).getCurrentContract();
				
                 // get the list of contract and amendment documents available for this contract
                // number.
                 
                ContractDocumentInfo[] contractDocInfo = getContractDocuments(contract);
				
                 // populate the values into ContractDocuments (value object)which will be used in
                // ContractDocuments page.
                 
				ContractDocuments contractdocuments = buildContractDocumentsList(contractDocInfo);
				request.setAttribute(CONTRACT_DOCUMENTS_KEY,
						contractdocuments);
				if (contractdocuments.getAmendmentDocs() != null
						&& contractdocuments.getAmendmentDocs().size() > 0) {
					request.setAttribute(AMENDMENT_DOCUMENT, contractdocuments
							.getAmendmentDocs());
				}
				if (logger.isDebugEnabled())
					logger.debug(ContractDocumentsController.class.getName()
							+ ":forwarding to Contract Documents Page.");

				if (logger.isDebugEnabled()) {
					logger.debug("exit <- doExecute");
				}
			} catch (SystemException e) {
			    List<GenericException> errors = new ArrayList<GenericException>();
				errors.add(new GenericException(
						REPORT_FILE_NOT_FOUND));
				setErrorsInRequest(request, errors);
				
				return forwards.get("input");
			} catch (ServiceUnavailableException e) {
				List<GenericException> errors = new ArrayList<GenericException>();
				errors.add(new GenericException(
						REPORT_SERVICE_UNAVAILABLE));
				setErrorsInRequest(request, errors);
				
				return forwards.get(CONTRACT_DOCUMENTS_PAGE);
			}
			
			return forwards.get(CONTRACT_DOCUMENTS_PAGE);
		} else {
			
             // If user has no access to this page or if user navigates to this page through book
            // mark , return to the bob page
             
            return forwards.get(BOB_PAGE_FORWARD);
		}

	}
	
	
	/**
     * The preExecute method has been overriden to see if the contractNumber is coming as part of
     * request parameter. If the contract Number is coming as part of request parameter, the
     * BobContext will be setup with contract information of the contract number passed in the
     * request parameter.
     * 
     */
    protected String preExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException, SystemException {
        
        super.preExecute( form, request, response);

        BobContextUtils.setUpBobContext(request);
        
        BobContext bob = BDSessionHelper.getBobContext(request);
        if (bob == null || bob.getCurrentContract() == null) {
            return forwards.get(BOB_PAGE_FORWARD);
        }
        
        if (bob.getCurrentContract().getCompanyCode().equals(
				GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
			ApplicationHelper.setRequestContentLocation(request,
					Location.NEW_YORK);
		}

        BobContextUtils.setupProfileId(request);
        
        return null;
    }
	

    /**
     * This method builds the label and value for the amendment list.
     * 
     * @param contractInfos
     * @return ContractDocuments
     * @throws SystemException
     */
	public ContractDocuments buildContractDocumentsList(
			ContractDocumentInfo[] contractInfos) throws SystemException {
		ContractDocuments contractdocuments = new ContractDocuments();

		for (ContractDocumentInfo contractInfo : contractInfos) {
			String pdfFileName = null;
			try {
				pdfFileName = ContractDocumentsHelper
						.buildContractDocPdfFileName(contractInfo);
			} catch (Exception e) {
				throw new SystemException(e, this.getClass().getName(),
						"buildContractDocumentsList",
						"Failed to build PDF: Contract Name ["
								+ contractInfo.getContractName() + "]");
			}
			contractdocuments.addDocuments(contractInfo.getDocumentType(),
					pdfFileName, ContractDocumentsHelper
							.buildContractLabel(contractInfo));
		}
		return contractdocuments;
	}

    /**
     * Returns all the ContractDocuments for this contract.
     * 
     * @param contract
     * @return ContractDocumentInfo
     * @throws SystemException
     * @throws ServiceUnavailableException
     */
	public ContractDocumentInfo[] getContractDocuments(
			Contract contract) throws SystemException, ServiceUnavailableException {

		EReportsServiceDelegate delegate = EReportsServiceDelegate
				.getInstance();
		ContractDocListResponse contractDocListResponse = null;
		
		// prepares request parameters
		ContractDocListRequest contractDocListRequest = new ContractDocListRequest();
		contractDocListRequest.setContractNumber((new Integer(contract
				.getContractNumber())).toString());

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
     * @param userProfile
     * @param contract
     * @return boolean
     */
	private boolean checkUserAccess(HttpServletRequest request)
	{
		boolean accessPresent = false;
		
	    BDUserProfile userProfile = getUserProfile(request);
        BobContext bob = getBobContext(request);
        Contract contract = null;
        if (bob == null || bob.getCurrentContract() == null) {
            return false;
        }
        
        // return to bob page if the page is tried to acces via a bookmark
        if (request.getParameter("contractDocuments") == null
                || request.getParameter("contractDocuments").equals("false")) {
            return false;
        }
        
//        if (contractDocumentsForm.getContractDocumentsLink() == null
//        		 || "false".equalsIgnoreCase(contractDocumentsForm.getContractDocumentsLink())) {
//            return false;
//        }
        
        contract = bob.getCurrentContract();
        
		try {
			ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate
					.getInstance();
			boolean isContractDocPresent = contractServiceDelegate
					.checkContractHasAmendmentOrContractDocuments((new Integer(contract
							.getContractNumber())).toString());
			boolean isPaperConsentGiven = contractServiceDelegate.checkContractConsent(contract
                    .getContractNumber());
            if (isContractDocPresent) {
                if (userProfile.isInternalUser() || isPaperConsentGiven) {
                    accessPresent = true;
                }
            }
		} catch (ApplicationException applicationException) {
			return false;
		} catch (SystemException systemException) {
            return false;
        }
		return accessPresent;
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
