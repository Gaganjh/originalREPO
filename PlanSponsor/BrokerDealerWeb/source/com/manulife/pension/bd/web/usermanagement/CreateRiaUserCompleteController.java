/**
 * 
 */
package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;	
import org.springframework.web.bind.annotation.InitBinder;	
import org.springframework.web.bind.ServletRequestDataBinder;		
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import java.util.HashMap;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;


import com.manulife.pension.bd.web.FrwValidation;
import com.manulife.pension.bd.web.process.BDWizardProcessController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWNull;
import com.manulife.pension.bd.web.validation.pentest.FrwValidator;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.validator.ValidationError;

/**
 * This class is for RiaUser creation complete action
 * 
 * @author narintr
 *
 */
@Controller
@RequestMapping(value ="/createRiaUser")

public class CreateRiaUserCompleteController extends BDWizardProcessController {
	@ModelAttribute("createRiaUserForm") 
	public CreateRiaUserForm populateForm()
	{
		return new CreateRiaUserForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/usermanagement/createRiaUser.jsp");
		}

	public CreateRiaUserCompleteController() {
		super(CreateRiaUserCompleteController.class,
				CreateRiaUserProcessContext.class,
				CreateRiaUserProcessContext.ProcessName,
				CreateRiaUserProcessContext.CompleteState);
	}
	
	/**
	 * Call the super's doInput to populate the form, but
	 * also end the process
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	 
	    protected String doInput( @ModelAttribute("createRiaUserForm") CreateRiaUserForm form, HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		String forward = super.doInput( form, request, response);
		endProcess(request);
		return forward;
	}
	    @RequestMapping(value ="/complete", method =  {RequestMethod.POST,RequestMethod.GET})
		public String doExecute(@Valid @ModelAttribute("createRiaUserForm") CreateRiaUserForm form,BindingResult bindingResult,
				HttpServletRequest request, HttpServletResponse response)
						throws IOException, ServletException, SystemException {
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if(errDirect!=null){
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
				}
			}
			String forward=super.doExecute( form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
		}
	/**
	 * This should not happen, since no continue button is enabled at this
	 */
	@Override
	protected ProcessState doContinue( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		return CreateRiaUserProcessContext.CancelState;
	}
	

	@Override
	protected ProcessState doProcess( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		
		List<ValidationError> errors = new ArrayList<ValidationError>();

		if (FrwValidator.getInstance().validateSanitizeCatalogedFormFields(form, errors, request) == false) {	
			CreateRiaUserProcessContext context = (CreateRiaUserProcessContext) getProcessContext(request);
			context.setChanged(true);
	        context.setCompleted(false);
	        setErrorsInSession(request, errors);
		    return getState();
		}
		return super.doProcess( form, request, response);
	}

	
	@Autowired
	private BDValidatorFWNull bdValidatorFWNull;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWNull);
	}
}
