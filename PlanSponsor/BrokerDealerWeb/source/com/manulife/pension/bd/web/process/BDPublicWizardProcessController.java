package com.manulife.pension.bd.web.process;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.FrwValidator;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;


/**
 * The super class for public site's wizard process, which check if the user
 * has already logged in. Redirect to home page if the user is logged in 
 * @author guweigu
 *
 */
abstract public class BDPublicWizardProcessController extends BDWizardProcessController {

	protected static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	protected static String HomeRedirect=HomeRedirect1.getPath();
	
	@SuppressWarnings("unchecked")
	protected BDPublicWizardProcessController(Class clazz, Class contextClass,
			String name, ProcessState currentState) {
		super(clazz, contextClass, name, currentState);
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	public String doExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		Collection penErrors = FrwValidator.doValidatePenTestAction(form,  request, CommonConstants.FAIL);
		if (penErrors != null && penErrors.size() > 0) {
			ControllerRedirect forward= new ControllerRedirect(CommonConstants.FAIL);
			return forward.getPath();
		}
		// protect in case the user already log in
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;	
		} else {
			return super.doExecute( form, request, response);
		}
	}

}
