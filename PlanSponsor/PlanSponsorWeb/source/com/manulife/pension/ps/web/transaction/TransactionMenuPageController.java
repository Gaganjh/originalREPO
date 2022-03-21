package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.taglib.util.QuickReportsList;
import com.manulife.pension.ps.web.tpabob.util.TPACrdQbadTransactionsUtility;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractSummaryVO;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.valueobject.UserInfo;


@Controller
@RequestMapping( value = "/transaction")

public class TransactionMenuPageController extends PsController {
	
	
	@ModelAttribute("dynaForm")
	public DynaForm populateForm() {
		return new DynaForm();
	}
	
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/transactionMenu.jsp"); 
		forwards.put("transactionMenu","/transaction/transactionMenu.jsp");
	}


	public TransactionMenuPageController() {
		super(TransactionMenuPageController.class);
	}

	@RequestMapping(value ="/transactionMenu/", method ={RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
	

		ContractSummaryVO contractSummaryVO = null;
		String showLoans = "true";
		UserProfile userProfile = getUserProfile(request);

		// if there're no current contract, forward them to the home page
		// finder
		if (userProfile.getCurrentContract() == null)
			return Constants.HOMEPAGE_FINDER_FORWARD;

		// retrieve LoansTotalAmount if it wasn't done before,
		// the loan total amount should be set in HomePageAction.
		if (userProfile.getCurrentContract().getLoansTotalAmount() == null) {
			// retrieve total loans amount
			ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate
					.getInstance();
			contractSummaryVO = contractServiceDelegate
					.getContractSummary(userProfile.getCurrentContract()
							.getContractNumber(), userProfile.getRole().isExternalUser());
			if (contractSummaryVO != null) {
				userProfile.getCurrentContract().setLoansTotalAmount(
						new Double(contractSummaryVO.getOutstandingLoans()));
			} else {
				userProfile.getCurrentContract().setLoansTotalAmount(
						new Double(0));
			}

		}
		
		// TRP.8. Don't display the loan link if:
		// Loan feature is disabled AND Outstanding Ballance is 0
		if (!userProfile.getCurrentContract().isLoanFeature()
				&& userProfile.getCurrentContract().getLoansTotalAmount()
						.doubleValue() == 0)
			showLoans = "false";

		request.setAttribute("showLoans", showLoans);
		postExecute(actionForm, request, response);
		return forwards.get("transactionMenu");
	}
	
    /* (non-Javadoc)
     * @see com.manulife.pension.ps.web.controller.PsAction#postExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void postExecute( ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SystemException {
        super.postExecute( form, request, response);
        UserProfile userProfile = getUserProfile(request);
        UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
        		userProfile.getPrincipal());
        boolean restricted = isUnsachedCheckLinkAllowed(loginUserInfo, userProfile.getCurrentContract()
				.getContractNumber());
        boolean showSysLink = isSystematicLinkAllowed(userProfile.getCurrentContract(), userProfile.getCurrentContract()
				.getContractNumber());
        //ISRESTRICTED is to decide whether the Uncashed link to be shown.
        //showSysLink is to decide whether the Systematic withdrawal link to be shown.
        request.setAttribute("ISRESTRICTED", new Boolean(restricted));
        request.setAttribute("showSysLink", new Boolean(showSysLink));
    }
    
    /**
	 * HAT 165 - TPA CRD QBA Transactions Report
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/transactionMenu/", params={"action=generateCrdQbaTransactionReportCsv"}  , method =  {RequestMethod.GET}) 
	public String doGenerateCrdQbaTransactionReportCsv (@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
				
		//To get the Current Contract Number.
		String currentContractNumber = String.valueOf(userProfile.getCurrentContract().getContractNumber());
		Integer contractNumber=Integer.parseInt(currentContractNumber);
			 
		Date asOfDate = new Date();
		List<Integer> contractNumbers=new ArrayList<Integer>();
		contractNumbers.add(contractNumber);
		BaseReportController.streamDownloadData(request, response,
				"text/csv", TPACrdQbadTransactionsUtility.getCsvFileName(asOfDate,contractNumber), 
				TPACrdQbadTransactionsUtility.getCrdQbdTransactionReportCSVFileData(asOfDate, contractNumbers));

		return null ;
	}
    /**
     * This method is to decide whether the Uncashed checks link needs to be shown or not. 
     * The Uncashed check link should not be displayed for IntermediaryContact users.
     * @param userInfo
     * @param contractNumber
     * @return boolean
     */
    private boolean isUnsachedCheckLinkAllowed(UserInfo userInfo, int contractNumber) {
    	
    	if (userInfo.getRole() instanceof InternalUser ||
    		userInfo.getRole() instanceof ThirdPartyAdministrator) {
    		return false;
    	} 
    	return false;
    }
    
    /**
     * This method is to decide whether the Systematic Withdrawal link needs to be shown or not. 
     * @param contract
     * @param contractNumber
     * @return boolean
     */
	private boolean isSystematicLinkAllowed(Contract contract,
			int contractNumber) {

		boolean contractStatus = false;
		try {
			contractStatus = QuickReportsList.getInstance().validateContractStatus(contract);
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
		boolean showSysWdLink = false;
		boolean featurecode = false;
		try {
			featurecode = ContractServiceDelegate.getInstance()
					.isSystematicWithdrawalFeatureON(contractNumber);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		if (contractStatus) {

			boolean reportData = false;
			if (featurecode) {
				showSysWdLink = true;
			} else {
				try {
					reportData = QuickReportsList.getInstance()
							.getPlaDataWithdrawalStatusReasonCode(
									contractNumber);
				} catch (SystemException e) {
					e.printStackTrace();
				}
				if (reportData) {
					showSysWdLink = true;
				}
			}
		}

		return showSysWdLink;

	}
        
    /** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
	 */

	
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
	
}
