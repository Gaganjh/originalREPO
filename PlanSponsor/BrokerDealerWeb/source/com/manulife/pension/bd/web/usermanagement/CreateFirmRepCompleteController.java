package com.manulife.pension.bd.web.usermanagement;

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

import com.manulife.pension.bd.web.process.BDWizardProcessController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.process.ProcessState;

/**
 * The firm rep creation complete action
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value ="/createFirmRep")

public class CreateFirmRepCompleteController extends BDWizardProcessController {
	@ModelAttribute("createFirmRepForm") 
	public CreateFirmRepForm populateForm() 
	{
		return new CreateFirmRepForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/usermanagement/createFirmRep.jsp");
		forwards.put("fail","redirect:/do/createFirmRep/create");
		
		}

	public CreateFirmRepCompleteController() {
		super(CreateFirmRepCompleteController.class,
				CreateFirmRepProcessContext.class,
				CreateFirmRepProcessContext.ProcessName,
				CreateFirmRepProcessContext.CompleteState);
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
	 
    protected String doInput(@Valid @ModelAttribute("createFirmRepForm") CreateFirmRepForm form, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		String forward = super.doInput( form, request, response);
		endProcess(request);
		return forward;
	}
    @RequestMapping(value = "/complete", method =  {RequestMethod.GET})
		public String doExecute(@Valid @ModelAttribute("createFirmRepForm") CreateFirmRepForm form,BindingResult bindingResult,
				HttpServletRequest request, HttpServletResponse response)
						throws IOException, ServletException, SystemException {
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if(errDirect!=null){
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get("fail");
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
		return CreateFirmRepProcessContext.CancelState;
	}
	  @Autowired
	  private BDValidatorFWInput bdValidatorFWInput;
	   
	  @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	   binder.bind( request);
	   binder.addValidators(bdValidatorFWInput);
	 }
}
