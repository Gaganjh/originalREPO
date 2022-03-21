package com.manulife.pension.bd.web.bob.contract;

import static com.manulife.pension.bd.web.BDConstants.BOB_PAGE_FORWARD;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWContractPlan;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;

/**
 * This class is used to render ContractAmendment/Contract document PDF
 * in new window.
 * 
 * @author Puttaiah Arugunta
 */

@Controller
@RequestMapping(value ="/bob")

public class ContractPlanHighlightsController extends BDController {
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm()
	{ 
		return new DynaForm();
	}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/contractInformation.jsp");
		forwards.put("bobToContractInformation","redirect:/do/bob/contract/contractInformation/");}

	/* Logger */
	private Logger logger = Logger.getLogger(ContractPlanHighlightsController.class);

	/**
	 * Default Constructor
	 */
	public ContractPlanHighlightsController() {
		super(ContractPlanHighlightsController.class);
	}


	/**
	 * 
	 * The preExecute method has been overridden to see if the contractNumber is coming as part of
     * request parameter. If the contract Number is coming as part of request parameter, the
     * BobContext will be setup with contract information of the contract number passed in the
     * request parameter.
     * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException, SystemException {

		BobContext bob = BDSessionHelper.getBobContext(request) ;
		
		if (bob == null || bob.getCurrentContract() == null) {
			return forwards.get(BOB_PAGE_FORWARD);
		}

		if (bob.getCurrentContract().getCompanyCode().equals(
				GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
			ApplicationHelper.setRequestContentLocation(request,
					Location.NEW_YORK);
		}
		return null;
	}

	/**
	 * 
	 * The doExecute method has been overridden to get the Plan Highlights PDF, once the 
	 * Contract Service Feature is enabled for the given contract. 
     * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	

	@RequestMapping(value ="/contract/planHighlights/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		//preExecute(actionForm, request, response);
		String forward=preExecute(actionForm, request, response);
		 if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		 }
		if(bindingResult.hasErrors()){
			
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("bobToContractInformation");//if input forward not //available, provided default
	       }
		}
	

		if (logger.isDebugEnabled()) {
			logger.debug(">>> inside ContractPlanHighlightsAction.doExecute Method");
		}

		BobContext bob = getBobContext(request);
		Contract contract = bob.getCurrentContract();
		
		Location location = Location.USA;
		String compCde = contract.getCompanyCode();
		if(Long.valueOf(compCde).equals(Long.valueOf(
				GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY))){
			location = Location.NEW_YORK;
		}
		String forwardPage = null;
		byte[] downloadData = null;		
		final Integer contractId = new Integer(contract.getContractNumber());
		
		if (logger.isDebugEnabled()) {
			logger.debug("--- Site Loaction ---- "+location.getAbbreviation());
			logger.debug("--- Comapny Code ---- "+compCde);
			logger.debug("--- Contract Id ---- "+contractId);
		}
		try{
			if(isCSFfeatureAvailable(contractId)){
				String headerFooterImagePath = null; 
				java.net.URL url=	getClass().getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX + 
						CommonConstants.HEADER_FOOTER_IMAGE);
				if(url != null){
					headerFooterImagePath = url.toExternalForm();
				}
				if (logger.isDebugEnabled()) {
					logger.info("Header Footer Image path for the PH PDF is "+headerFooterImagePath);
				}
				downloadData = ContractServiceDelegate.getInstance().generateSphPdf(
						contractId, location, headerFooterImagePath);

				if (downloadData != null && downloadData.length > 0) {
					BaseReportController.streamDownloadData(request, response,
							"application/pdf",  contractId+".pdf", downloadData);
				}
			}else 
				//forwardPage = BOB_PAGE_FORWARD;
				forwardPage = "bobToContractInformation";
		}catch(ApplicationException e){
			logger.error(e);
			throw new SystemException(e.getMessage());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("<<<<< Leaving ContractPlanHighlightsAction.doExecute Method");
		}
		return forwards.get(forwardPage);
	}
	
	/**
	 * Method to verify whether Contract Service Feature is available for a contract or not.
	 * @param contractId
	 * @return  isCSFfeatureAvailable boolean
	 * @throws SystemException
	 * @throws ApplicationException
	 */
	private boolean isCSFfeatureAvailable(Integer contractId) 
				throws SystemException,ApplicationException{
		boolean isCSFfeatureAvailable = false;
			ContractServiceFeature summaryPlanHighlightAvailable =
				ContractServiceDelegate.getInstance().getContractServiceFeature(
						contractId,
						ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_AVAILABLE);

			if (summaryPlanHighlightAvailable != null) {
				 boolean isSummaryPlanHighlightAvailable = ContractServiceFeature.internalToBoolean(
	 						summaryPlanHighlightAvailable.getValue()).booleanValue();
	 			 boolean isSummaryPlanHighlightReviewed = ContractServiceFeature
	 				        .internalToBoolean(summaryPlanHighlightAvailable.getAttributeValue(ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_REVIEWDED)).booleanValue();
				isCSFfeatureAvailable = isSummaryPlanHighlightAvailable && isSummaryPlanHighlightReviewed;
			}
		return isCSFfeatureAvailable;
	}
	
	/**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	
	@Autowired
	   private BDValidatorFWContractPlan  bdValidatorFWContractPlan;
      
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWContractPlan);
	}
}
