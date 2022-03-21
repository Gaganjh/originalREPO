package com.manulife.pension.bd.web.bob.contract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;	
import org.springframework.web.bind.annotation.InitBinder;	
import org.springframework.web.bind.ServletRequestDataBinder;		
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import java.util.HashMap;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import com.manulife.pension.bd.web.FrwValidation;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.ereports.ReportInfo;
import com.manulife.pension.lp.model.ereports.ReportListRequest;
import com.manulife.pension.lp.model.ereports.ReportListResponse;
import com.manulife.pension.lp.model.ereports.RequestConstants;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;

/**
 * ContractStatementsAction Action class 
 * This class is used to forward the users's request to 
 * ContractStatements page
 * 
 * @author Ludmila Stern
 * modified 17/05/2005 Problem log 43844. Setting setStaffPlanAccessAllowed flag;
 */
@Controller
@RequestMapping(value ="/bob")

public class ContractStatementsController extends BDController {
	@ModelAttribute("dynaForm")
	public DynaForm populateForm()
	{ return new DynaForm();
			}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/contractStatements.jsp");
		forwards.put("contractStatements","/contract/contractStatements.jsp");
		}

	private static final String CONTRACT_STATEMENTS_PAGE = "contractStatements";
	private static final String CLIENT_USER_TYPE = "CLIENT";

	public ContractStatementsController() {
		super(ContractStatementsController.class);
	} 

	@RequestMapping(value ="/contract/contractStatements/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
		ContractStatements contractStatements = new ContractStatements();
		String sitemode = getBobContext(request).getContractSiteLocation();

		// get the user profile object and set the current contract to null	
		BDUserProfile userProfile = getUserProfile(request);
		Contract contract = getBobContext(request).getCurrentContract();
		ReportInfo[] reports = null;

		try {
			reports = getReports(userProfile, contract, sitemode);
		} catch(ServiceUnavailableException e) {
			List<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE));
			setErrorsInRequest(request, errors);
			return forwards.get("input");
		}

		contractStatements = buildContractStatementsLists(reports);

		// add contractStatements object to the request
		request.setAttribute(CommonConstants.CONTRACT_STATEMENTS_KEY, contractStatements);
		request.setAttribute("efOptions", contractStatements.getEfOptions());
		request.setAttribute("paOptions", contractStatements.getPaOptions());
		request.setAttribute("saOptions", contractStatements.getSaOptions());
		request.setAttribute("crOptions", contractStatements.getCrOptions());
		request.setAttribute("bpOptions", contractStatements.getBpOptions()); // defined benefit
		request.setAttribute("scOptions", contractStatements.getScOptions()); // Schedule C options.
		// log the action
		if (logger.isDebugEnabled()) {
			logger.debug(contractStatements);
			logger.debug("forwarding to Contract Statements Page.");
		}
		
		if (!contract.isMta() ) 
			request.setAttribute("mtaContract", "mtaContract");
		
		return forwards.get(CONTRACT_STATEMENTS_PAGE);

	}

	public ContractStatements buildContractStatementsLists(ReportInfo[] reportInfos)
		throws SystemException {
		ContractStatements contractStatements = new ContractStatements();

		for (int i = 0; i < reportInfos.length; i++) {
			String pdfFileName = null;
			try {
				pdfFileName = ContractStatementsHelper.buildPdfFileName(reportInfos[i]);
			} catch (Exception e) {
				throw new SystemException(
					e,
					this.getClass().getName(),
					"buildContractStatementsLists",
					"Failed to build PDF: Contract Name ["
						+ reportInfos[i].getContractName()
						+ "]");
			}

			contractStatements.addStatement(
				reportInfos[i].getReportType(),
				pdfFileName,
				ContractStatementsHelper.buildLabel(reportInfos[i]));

		}
		return contractStatements;

	}
	// Makes call to Business Delegate to gets the Array of all the ContractStatements for this contract 

	public ReportInfo[] getReports(
		BDUserProfile userProfile,
		Contract contract,
		String sitemode)
		throws SystemException, ServiceUnavailableException {

		EReportsServiceDelegate delegate = EReportsServiceDelegate.getInstance();
		ReportListResponse reportListResponse = null;
		// prepares request parameters
		ReportListRequest reportListRequest = new ReportListRequest();
		//  Allow tpa staff plan access for all the users. Assuming that
		// users who are not allowed to view the statements for this contract have already been restricted from 
		// from viewing this page.
		
		reportListRequest.setStaffPlanAccessAllowed(true);
		reportListRequest.setContractNumber(String.valueOf(contract.getContractNumber()));
		reportListRequest.setClientId(contract.getClientId());
		if (sitemode.equals(CommonConstants.SITEMODE_USA)) {
			reportListRequest.setCompanyCode(RequestConstants.COMPANY_CODE_USA);
		} else {
			reportListRequest.setCompanyCode(RequestConstants.COMPANY_CODE_NY);
		}
		reportListRequest.setUserType(CLIENT_USER_TYPE);
		// sort by 1. report type asc, 2. report print date desc
		reportListRequest.setSortFields(
			new int[] {
				ReportListRequest.SORT_FIELD_REPORT_TYPE,
				ReportListRequest.SORT_FIELD_REPORT_END_DATE,
				ReportListRequest.SORT_FIELD_REPORT_PRINT_DATE });
		reportListRequest.setSortAscendingOrders(new boolean[] { true,false, false });

		// logg the call to eReports service
		if (logger.isDebugEnabled())	
			logger.debug(
				"calling eReports service with the following param: "
					+ reportListRequest.getClientId()
					+ ";"
					+ reportListRequest.getContractNumber());
					
		reportListResponse = delegate.getReportList(reportListRequest);
		return reportListResponse.getReportList();
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