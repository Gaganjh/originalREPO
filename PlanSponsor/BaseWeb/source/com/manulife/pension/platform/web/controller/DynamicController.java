package com.manulife.pension.platform.web.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.util.content.GenericException;

/**
 * This class represents a base class for all dynamic actions. Dynamic actions
 * execute methods based on data in the Form, the Request, or the Action
 * Mapping. There are two major implementations of dynamic action, one is
 * PsAutoAction, the other is ReportAction.
 * 
 * @author Charles Chan
 */
@SuppressWarnings("unchecked")
public abstract class DynamicController extends BaseController {

	/**
	 * Constructor.
	 */
	public DynamicController() {
		super(DynamicController.class);
	}

	/**
	 * Constructor.
	 */
	public DynamicController(Class clazz) {
		super(clazz);
	}

	abstract protected Class[] getActionMethodParameters();

	abstract protected String getMethodName(
			ActionForm actionForm, HttpServletRequest request);

	/**
	 * Returns the forward object from the action mapping.
	 * 
	 * @param mapping
	 *            The Struts action mapping object.
	 * @param task
	 *            The forward task name.
	 * @return The ActionForward object that represents the forward task.
	 */
	protected String findForward(String task) {
		//TODO
		//return mapping.findForward(task);
		return task;
	}

	/**
	 * The main entry point into this Action. It then uses invokeTask method to
	 * dispatch the request to different methods.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doExecute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public String doExecute(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		String forward;
		forward = invokeTask(actionForm, request, response);
		return forward;
	}

	/**
	 * This method invokes the appropriate action methods using retrospection.
	 * It turns the first letter of the task name to capital letter and prefixes
	 * with "do".
	 * 
	 * @param mapping
	 *            ActionMapping object
	 * @param actionForm
	 *            PsAutoForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * 
	 * @return ActionForward
	 * 
	 * @exception ServletException
	 * @exception NoSuchMethodException
	 * @exception IOException
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	private String invokeTask(
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		try {
			Method actionMethod = null;
			Class thisClass = getClass();
			String methodName = getMethodName(actionForm, request);

			if (logger.isDebugEnabled()) {
				logger.debug("Invoking method [" + methodName + "]");
			}

			//
			// Anonymous penetration error caught in doValidate() upon registration is reported in here,
			// to maintain expected page navigation.
			//
			boolean validationErrorFound = false;
			String emsg = (String) request.getSession().getAttribute("error");
			if (!StringUtils.isBlank(emsg)) {
				validationErrorFound = (emsg.compareToIgnoreCase(String.valueOf(CommonErrorCodes.USER_DOES_NOT_EXIST)) == 0);
			}

			if (validationErrorFound == true) {
				//String forward = findForward(mapping, "terms");
				String forward="terms";
				request.getSession().removeAttribute("error");

				List errors = new ArrayList();
				errors.add(new GenericException(CommonErrorCodes.USER_DOES_NOT_EXIST));
				setErrorsInRequest(request, errors);
				//TODO
				//forward = mapping.getInputForward();
				forward="input";
			}

			while (thisClass != null) {
				try {
					actionMethod = thisClass.getDeclaredMethod(methodName,
							getActionMethodParameters());
					break;
				} catch (NoSuchMethodException e) {
					// repeat the same thing with the superclass
					thisClass = thisClass.getSuperclass();
				}
			}
			if (thisClass == null)
				throw new NoSuchMethodException("No such method: " + methodName);

			Object[] params = new Object[]{actionForm, request,
					response};

			Object forward =  actionMethod.invoke(this,params);

			return (String) forward;

		} catch (Throwable e) {
			Throwable causingException = ExceptionHandlerUtility
					.getSystemExceptionOrCausedByException(e);
			if (causingException instanceof SystemException) {
				throw (SystemException) causingException;
			} else {
				throw new SystemException(causingException,
						"Exception calling invokeTask");
			}
		}
	}
}