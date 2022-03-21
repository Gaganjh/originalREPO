package com.manulife.pension.bd.web.process;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessController;
import com.manulife.pension.platform.web.process.ProcessContext;
import com.manulife.pension.platform.web.process.ProcessState;

/**
 * Abstract Process action deals with process flow and implement the
 * Redirect-After-Post pattern
 * 
 * @author guweigu
 * 
 */
abstract public class BDAbstractProcessController extends BDController implements
		ProcessController {

	@SuppressWarnings("unchecked")
	final private Class processContextClass;
	final protected String processName;
	final protected ProcessState currentState;
	public String REDIRECT="redirect:";

	@SuppressWarnings("unchecked")
	protected BDAbstractProcessController(Class clazz, Class processContextClass,
			String processName, ProcessState currentState) {
		super(clazz);
		this.processContextClass = processContextClass;
		this.processName = processName;
		this.currentState = currentState;
	}

	/**
	 * Validate the current request is in the right state. If not, force it to
	 * the start state of the process. If it is the right state, call doProcess
	 * if it is a post. Otherwise call doInput to rendering the page
	 */
	public String doExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (!isValidState(request)) {
			ProcessContext context = getProcessContext(request);
			ProcessState startState = context.getStartState();

			String url = startState.getUrl();
			// wrong state, remove the ProcessContext and redirect to
			// startState's URL
			request.getSession().removeAttribute(processName);
			ControllerRedirect redirect1 = new ControllerRedirect(url);
		  return REDIRECT+redirect1.getPath();
			//return url;
		}
		if (StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
			ProcessState nextState = doProcess( form, request, response);
			ControllerRedirect redirect = new ControllerRedirect(nextState.getUrl());
			// if the process has ended, then no need to set the current state
			if (!nextState.hasProcessEnded()) {
				getProcessContext(request).setCurrentState(nextState);
			} else {
				endProcess(request);
			}
			return REDIRECT+redirect.getPath();
		} else {
			return doInput( form, request, response);
		}
	}

	public String doExecuteInput( 
			HttpServletRequest request)
			throws IOException, ServletException, SystemException {
		if (!isValidState(request)) {
			ProcessContext context = getProcessContext(request);
			ProcessState startState = context.getStartState();

			String url = startState.getUrl();
			// wrong state, remove the ProcessContext and redirect to
			// startState's URL
			request.getSession().removeAttribute(processName);
			ControllerRedirect redirect1 = new ControllerRedirect(url);
		  return REDIRECT+redirect1.getPath();
			//return url;
		}
		return null;
	}
	public String doCancelContinue(ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		ProcessState nextState = doProcess( form, request, response);
		ControllerRedirect redirect = new ControllerRedirect(nextState.getUrl());
		// if the process has ended, then no need to set the current state
		if (!nextState.hasProcessEnded()) {
			getProcessContext(request).setCurrentState(nextState);
		} else {
			endProcess(request);
		}
		return REDIRECT+redirect.getPath();
		
	}
	/**
	 * Get the process context from the session
	 */
	public ProcessContext getProcessContext(HttpServletRequest request)
			throws SystemException {
		ProcessContext context = BDProcessContextHelper.getProcessContext(
				request, processName);
		if (context == null) {
			context = getNewProcessContext();
			BDProcessContextHelper.setProcessContext(request, processName,
					context);
		}
		return context;
	}

	/**
	 * Check if the request is in the valid state
	 * 
	 * @param request
	 * @return
	 */
	protected boolean isValidState(HttpServletRequest request)
			throws SystemException {
		ProcessContext context = getProcessContext(request);
		ProcessState current = context.getCurrentState();
		if ((current != null && current.isSameState(getState()))
				|| (current == null && getState().isSameState(
						context.getStartState()))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Clean up the process
	 * 
	 * @param request
	 */
	protected void endProcess(HttpServletRequest request) {
		BDProcessContextHelper.clearProcessContext(request, processName);
	}

	/**
	 * Apply input and forward to the page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	protected String doInput( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		//TODO
		//return mapping.getInputForward();
		return "input";
	}

	/**
	 * Get the state that corresponds this action
	 */
	final public ProcessState getState() {
		return currentState;
	}

	/**
	 * create a new process context
	 * 
	 * @return
	 */
	protected ProcessContext getNewProcessContext() throws SystemException {
		try {
			return (ProcessContext) this.processContextClass.newInstance();
		} catch (Exception e) {
			logger.error("Fail to create the ProcessContext : "
					+ processContextClass.getName(), e);
			throw new SystemException(e,
					"Process context can not be instantiated: "
							+ processContextClass.getName());
		}
	}

	/**
	 * handle the submit
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	abstract protected ProcessState doProcess(
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws SystemException;
}
