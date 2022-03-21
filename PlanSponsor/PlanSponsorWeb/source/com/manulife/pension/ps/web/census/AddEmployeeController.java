/**
 * 
 */
package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.util.HashMap;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWError;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.security.role.permission.PermissionType;

/**
 * Action to handle add employee activity.
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value = "/census")
@SessionAttributes({"addEmployeeForm"})

public class AddEmployeeController extends CommonEmployeeSnapshotController  {

	@ModelAttribute("addEmployeeForm") 
	public EditEmployeeSnapshotForm populateForm()
	{
		return new EditEmployeeSnapshotForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/census/addEmployee.jsp");
		forwards.put("addEmployee","/census/addEmployee.jsp");
		forwards.put("viewEmployeeSnapshot","redirect:/do/census/viewEmployeeSnapshot/");
		forwards.put("censusSummary","redirect:/do/census/censusSummary/");
		forwards.put("eligibilitySummary","redirect:/do/census/employeeEnrollmentSummary");
		forwards.put("addressSummary","redirect:/do/participant/participantAddresses");
		forwards.put("participantAccount","redirect:/do/participant/participantAccount/");
		forwards.put("censusVesting","redirect:/do/census/censusVesting/"); 
		forwards.put("deferral","redirect:/do/census/deferral");
	}

	
    /*f
     * Default action is setup the form bean and go to the page display.
     * 
     * @see com.manulife.pension.ps.web.controller.PsAutoAction#doDefault(org.apache.struts.action.ActionMapping,
     *      com.manulife.pension.ps.web.controller.PsAutoForm,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	
	@RequestMapping(value="/addEmployee/",method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("addEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
        UserProfile user = getUserProfile(request);
        Contract currentContract = user.getCurrentContract();
        
        if (!user.getRole().hasPermission(PermissionType.UPDATE_CENSUS_DATA) ||
                Contract.STATUS_CONTRACT_DISCONTINUED.equals(user.getCurrentContract().getStatus())) {
           
        	return  Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
        
		int contractNumber = user.getCurrentContract().getContractNumber();
                
        EditEmployeeSnapshotForm form = (EditEmployeeSnapshotForm) actionForm;
        form.setProfileId(null);
        // this has to be called before form.setUp();
		form.setShowVesting(false);
        Employee employee = getEmployeeForUI(new Employee());
        // set the employment status to 'A' as default
        // this way the change tracking will not fail...
        employee.getEmployeeDetailVO().setEmploymentStatusCode(EMPLOYMENT_STATUS_ACTIVE);
        setParticipationSection(contractNumber,employee,form,currentContract);
        form.setUp(employee, getUserProfile(request));
        // store the cloned form for change tracking
        form.storeClonedForm();
        // save values for change tracking on each save attempt 
        form.saveInputValues();
        
        form.setExpandBasic(true);
        form.setExpandEmployment(true);
        form.setExpandContact(true);
        form.setExpandParticipation(true);
        String forward= forwardToInput(request, form, State.Normal);
        return StringUtils.contains(forward, '/')?forward:forwards.get(forward);
        //return forwardToInput(request, form, State.Normal);
    }
	
	@RequestMapping(value ="/addEmployee/" ,params={"action=saveIgnoreWarning"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSaveIgnoreWarning(@Valid @ModelAttribute("addEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doSaveIgnoreWarning(actionForm, request, response);
		return StringUtils.contains(forward, '/')?forward:forwards.get(forward);   
	}
	
	@RequestMapping(value ="/addEmployee/" ,params={"action=continueEdit"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doContinueEdit(@Valid @ModelAttribute("addEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doContinueEdit(actionForm,  request, response);
		return StringUtils.contains(forward, '/')?forward:forwards.get(forward);   
	}
	
	@RequestMapping(value ="/addEmployee/" ,params={"action=save"}, method =  {RequestMethod.POST}) 
	public String doSave(@Valid @ModelAttribute("addEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doSave(actionForm, request, response, false);
		return StringUtils.contains(forward, '/')?forward:forwards.get(forward);   
	}
	
	@RequestMapping(value ="/addEmployee/" ,params={"action=cancel"}, method = {RequestMethod.POST}) 
	public String doCancel(@Valid @ModelAttribute("addEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doCancel(actionForm, bindingResult, request, response);
		return StringUtils.contains(forward, '/')?forward:forwards.get(forward);   
	}

	
	
    /**
     * The action to reset the user input.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value ="/addEmployee/",params={"action=reset"}, method =  {RequestMethod.POST}) 
	public String doReset (@Valid @ModelAttribute("addEmployeeForm") EditEmployeeSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		actionForm.setUp(getEmployeeForUI(new Employee()), getUserProfile(request));
		actionForm.setSpecialPageType(null);
        //return mapping.findForward(ADD_EMPLOYEE_PAGE);
        return forwards.get(ADD_EMPLOYEE_PAGE);
    }
	@Autowired
    private PSValidatorFWError psValidatorFWError;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.addValidators(psValidatorFWError);
	}
}
