package com.manulife.pension.platform.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;

/**
 * This class represents a DynamicAction that takes its method name from an
 * Form. Specifically, the BaseAutoForm has a property
 * <code>action</code> that determines the method to invoke. This class is
 * taken from EZk and is refactored to inherit (indirectly) from PsAction.
 */
@SuppressWarnings("unchecked")
public abstract class BaseAutoController extends DynamicController {

	/**
	 * ACTION_METHOD_PARAM_TYPES are used for dynamically invoking the
     * action method of the appropriate name.
	 */
	private static final Class[] ACTION_METHOD_PARAM_TYPES = {
			 AutoForm.class,
			HttpServletRequest.class, HttpServletResponse.class};

	/**
	 * Constructor.
	 */
	public BaseAutoController() {
		super(BaseAutoController.class);
	}

	/**
	 * Constructor.
     * 
	 * @param clazz The class used to configure the logger.
	 */
	public BaseAutoController(final Class clazz) {
		super(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getActionMethodParameters() {
		return ACTION_METHOD_PARAM_TYPES;
	}

	/**
     * {@inheritDoc}
	 */
	protected final String getMethodName(
			final ActionForm form, final HttpServletRequest request) {

		AutoForm actionForm = (AutoForm) form;

		String methodName;
        if (actionForm == null || StringUtils.isBlank(actionForm.getAction())) {
            methodName = "doDefault";
        } else {
            methodName = "do"
				+ actionForm.getAction().substring(0, 1).toUpperCase()
				+ actionForm.getAction().substring(1);
		} // fi

		return methodName;
	}

	/**
	 * unsetMethodname removes the action field value from the form.
	 * 
	 * @param mapping The action mapping.
	 * @param form The form (should be a BaseAutoForm).
	 * @param request The HTTP request.
	 */
	protected final void unsetMethodname(
            final ActionForm form, final HttpServletRequest request) {
		AutoForm actionForm = (AutoForm) form;
		actionForm.setAction(null);
	}

	/**
	 * Invokes the default task (the initial page). It uses the common workflow
	 * with validateForm set to true.
	 * 
	 * @see #doCommon(ActionMapping, AutoForm, HttpServletRequest,
	 *      HttpServletResponse, boolean)
	 * 
	 * @param mapping The action mapping.
	 * @param actionForm The action form.
	 * @param request The HTTP request.
	 * @param response The HTTP response.
	 * @return ActionForward The forward to process.
	 * @throws IOException When an IO problem occurs.
	 * @throws ServletException When an Servlet problem occurs.
	 * @throws SystemException When an generic application problem occurs.
	 */
	public String doDefault(
			AutoForm actionForm,ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		return null;
	}
}
