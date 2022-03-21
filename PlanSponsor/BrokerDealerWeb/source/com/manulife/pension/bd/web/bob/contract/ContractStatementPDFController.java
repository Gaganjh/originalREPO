package com.manulife.pension.bd.web.bob.contract;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.bos.ereports.common.ReportFileNotFoundException;
import com.manulife.pension.lp.model.ereports.ReportFileRequest;
import com.manulife.pension.lp.model.ereports.ReportFileResponse;
import com.manulife.pension.lp.model.ereports.ReportInfo;
import com.manulife.pension.lp.model.ereports.RequestConstants;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;

/**
 * ContractStatementsAction Action class This class is used to forward the
 * users's request to ContractStatements page
 * 
 * @author Ludmila Stern
 */
@Controller
@RequestMapping(value ="/bob")

public class ContractStatementPDFController extends BDController {
	@ModelAttribute("dynaForm")
	public DynaForm populateForm()
	{
	return	new DynaForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/contractStatements.jsp");
		forwards.put("secondaryWindowError","/global/secondaryWindowError.jsp");
		}

	private static final String CLIENT_USER_TYPE = "CLIENT";
	public ContractStatementPDFController() {
		super(ContractStatementPDFController.class);
	}
	

	@RequestMapping(value ="/contract/contractStatementsPDF/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

		List<GenericException> errors = new ArrayList<GenericException>();

		String sitemode = getBobContext(request).getContractSiteLocation();
		String fileName = request.getParameter("statement");
		

		// logg the action
		if (logger.isDebugEnabled()) {
			logger.debug(fileName);
		}

		try {
			// get the user profile object and set the current contract to null
			BDUserProfile userProfile = getUserProfile(request); 
			Contract currentContract =  getBobContext(request).getCurrentContract();
			ReportInfo reportInfo = ContractStatementsHelper
					.parsePdfFileName(fileName);
			if (logger.isDebugEnabled()) {
				logger.debug(reportInfo);
			}

			ReportFileResponse fileResponse = getReport(userProfile,
					currentContract, sitemode, reportInfo);

			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
			response.setHeader("Pragma", "no-cache");
			response.setContentType(CommonConstants.MIME_TYPE_PDF);
			response.setContentLength(fileResponse.getLength());
			OutputStream out = response.getOutputStream();
			out.write(fileResponse.getReportFragment());
			out.close();

		} catch (ReportFileNotFoundException e) {
			errors.add(new GenericException(CommonErrorCodes.REPORT_FILE_NOT_FOUND));
		} catch (ServiceUnavailableException e) {
			errors.add(new GenericException(
					CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE));
		} catch (ParseException e) {
			throw new SystemException(e, getClass().getName(), "doExecute",
					"Failed to get report. Site mode [" + sitemode
							+ "] fileName [" + fileName + "]");
		}

		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
			return forwards.get(CommonConstants.SECONDARY_WINDOW_ERROR_FORWARD);
		} else {
			return null;
		}
	}

	public ReportFileResponse getReport(BDUserProfile userProfile,
			Contract contract, String sitemode, ReportInfo reportInfo)
			throws SystemException, ServiceUnavailableException,
			ReportFileNotFoundException {
		// complete the ReportInfo
		// overwrite the contract number in any case
		// the one coming from the browser cannot be trusted
		reportInfo
				.setContractNumber(String.valueOf(contract.getContractNumber()));

		EReportsServiceDelegate delegate = EReportsServiceDelegate
				.getInstance();

		// prepares request parameters
		ReportFileRequest reportRequest = new ReportFileRequest();
		reportRequest.setCompanyCode(sitemode.equals(CommonConstants.SITEMODE_USA)
				? RequestConstants.COMPANY_CODE_USA
				: RequestConstants.COMPANY_CODE_NY);
		reportRequest.setCompressFile(false);
		reportRequest.setReportKeys(new ReportInfo[]{reportInfo});
		reportRequest.setStaffPlanAccessAllowed(false);
		reportRequest.setClientId(contract.getClientId());
		reportRequest.setUserType(CLIENT_USER_TYPE);

		return delegate.getReportFile(reportRequest);

	}
	
	/**This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 * 
	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;

	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
	
}