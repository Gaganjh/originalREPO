package com.manulife.pension.bd.web.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.myprofile.WebBrokerEntityProfileHelper;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.validator.ValidationError;

/**
 * Step2 of the migration process. Confirm the personal information of
 * a broker entity
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value ="/migration")
@SessionAttributes({"migrationForm"})

public class MigrationStep2Controller extends BDPublicWizardProcessController {
	@ModelAttribute("migrationForm") 
	public MigrationForm populateForm() 
	{
		return new MigrationForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static {
		forwards.put("input", "/migration/step2.jsp");
	}
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
	public MigrationStep2Controller() {
		super(MigrationStep2Controller.class, MigrationProcessContext.class,
				MigrationProcessContext.PROCESS_NAME,
				MigrationProcessContext.Step2State);
	}

	@Override
	protected ProcessState doContinue( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		MigrationForm f = (MigrationForm) form;
		List<ValidationError> errors = new ArrayList<ValidationError>();
		WebBrokerEntityProfileHelper.validateBrokerEntityProfile(errors, f
				.getBrokerEntityProfilesList(),f.getAddressFlagMap());
		if (errors.size() == 0) {
			MigrationProcessContext context = (MigrationProcessContext) getProcessContext(request);
			context.setUpdateBrokerEntities(f.getBrokerEntityProfilesList());
			context.setPrimaryBrokerEntityPartyId(f.getPrimaryBrokerPartyId());
			// clear the form from the session
			 request.getSession().removeAttribute("migrationForm");
			return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
		} else {
			setErrorsInSession(request, errors);
			return getState();
		}
	}

	@Override
	protected ProcessState doCancel( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		// clear the form from the session
		request.getSession().removeAttribute("migrationForm");
		return super.doCancel( form, request, response);
	}
	/*@RequestMapping(value = "/step2", method =  {RequestMethod.POST,RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("migrationForm") MigrationForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("input");//if input forward not //available, provided default
			}
		}
		String forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}*/
	
	@RequestMapping(value = "/step2", method = { RequestMethod.GET })
	protected String doInput(
			@Valid @ModelAttribute("migrationForm") MigrationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not //available, provided default
			}
		}

		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doExecuteInput(request);

		if (forward == null) {
			String forward1 = super.doInput(form, request, response);
			return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1);
		}
		return forward;
	}

	@RequestMapping(value = "/step2", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(
			@Valid @ModelAttribute("migrationForm") MigrationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/step2", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(
			@Valid @ModelAttribute("migrationForm") MigrationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}

		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	
}
