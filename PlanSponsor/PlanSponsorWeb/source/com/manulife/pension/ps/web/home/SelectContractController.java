package com.manulife.pension.ps.web.home;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.report.valueobject.SelectContract;

/**
 * SelectContractAction Action class 
 * This class is used to set the selected contract in the UserProfile
 * 
 * @author Ilker Celikyilmaz
 */
@Controller
@RequestMapping( value = "/home")
@SessionAttributes("selectContractForm")

public class SelectContractController extends PsController {
	
	@ModelAttribute("selectContractForm") 
	public SelectContractForm populateForm() 
	{
		return new SelectContractForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/home/selectContractPage.jsp");
		}

	
	
	public SelectContractController() {
		super(SelectContractController.class);
	} 
		
	@RequestMapping(value ="/selectContract/", method = RequestMethod.GET) 
	public String doExecute(@Valid @ModelAttribute("selectContractForm") SelectContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
	
		//SelectContractForm selectContractForm = (SelectContractForm) form;
		
		if (actionForm.getContractNumber().length() > 0) {
			// Call PSContractService to get the Conract Details
			HttpSession session = request.getSession(false);
			int contractNumber = Integer.parseInt(actionForm.getContractNumber());
			
			if (verifyContract(session, contractNumber)) {
				SelectContractDetailUtil.selectContract(getUserProfile(request), contractNumber);
			}
		}
		//return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD);
		return Constants.HOMEPAGE_FINDER_FORWARD;
	}						                
	
	/**
	 * This method is used to make sure no one 
	 * can set any contract that is not available in their
	 * list.
	 * 
	 * @param session
	 * @param contractNumber
	 * @return Returns true if the contract is available for this user otherwise false.
	 */
	private boolean verifyContract(HttpSession session, int contractNumber)	{
		if (contractNumber != 0) {
			Collection contractList = (Collection) session.getAttribute(Constants.AVAILABLE_CONTRACTS_LIST_KEY);
			if (contractList != null) {
				for (Iterator it=contractList.iterator(); it.hasNext();) {
					SelectContract selectContract = (SelectContract) it.next();
					if (contractNumber == selectContract.getContractNumber()) {
						session.removeAttribute(Constants.AVAILABLE_CONTRACTS_LIST_KEY);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
	 *  * (non-Javadoc) 
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of the CL#137697.
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#doValidate(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.Form,javax.servlet.http.HttpServletRequest)
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	 public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}