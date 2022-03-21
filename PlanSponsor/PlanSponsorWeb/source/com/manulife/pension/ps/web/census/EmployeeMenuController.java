
package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;

/**
 * Participant Menu Action class 
 * This class is used to forward the users's request to 
 * the Participant Menu page
 * 
 * @author Simona Stoicescu
 */
@Controller
@RequestMapping(value ="/census/employeeMenu/")

public class EmployeeMenuController extends PsController 
{
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("employeeMenu","/census/employees.jsp");
		}

	

	private static final String EMPLOYEE_MENU_PAGE = "employeeMenu";
	
	public EmployeeMenuController()
	{
		super(EmployeeMenuController.class);
	} 
	@RequestMapping( method =  {RequestMethod.GET}) 
	public String doExecute (@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if ( logger.isDebugEnabled() )
			logger.debug(EmployeeMenuController.class.getName()+":forwarding to Employee Menu Page.");
		
		int contractNumber = 0;
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		if(userProfile != null) {
			contractNumber = userProfile.getCurrentContract().getContractNumber();
		}
		
		return forwards.get(EMPLOYEE_MENU_PAGE);	
		
		
	}						                
}
