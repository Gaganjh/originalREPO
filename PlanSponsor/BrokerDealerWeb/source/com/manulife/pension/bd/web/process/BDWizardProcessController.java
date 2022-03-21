package com.manulife.pension.bd.web.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.process.ProcessState;

/**
 * Wizard type of process action that support continue and cancel actions The
 * action parameter should be either 'continue' or 'cancel'
 * 
 * @author guweigu
 * 
 */
abstract public class BDWizardProcessController extends BDAbstractProcessController {

	@SuppressWarnings("unchecked")
	protected BDWizardProcessController(Class clazz, Class contextClass,
			String name, ProcessState currentState) {
		super(clazz, contextClass, name, currentState);
	}

	/**
	 * If the action is 'continue',update the Process context from the submitted 
	 * then invoke doContinue. Otherwise invoke the doCacnel without updating
	 * the process context
	 * 
	 */
	@Override
	protected ProcessState doProcess( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {

		String action = request.getParameter("action");

		if (StringUtils.equals(action, BDWizardProcessContext.ACTION_CONTINUE)) {
			BDWizardProcessContext context = (BDWizardProcessContext) getProcessContext(request);
			context.updateContext(form);
			//return doContinue(mapping, form, request, response);
			return doContinue( form, request, response);
		} else if (StringUtils.equals(action,
				BDWizardProcessContext.ACTION_CANCEL)) {
			//return doCancel(mapping, form, request, response);
			return doCancel( form, request, response);
		}
		throw new SystemException("Action not supported = " + action);
	}

	/**
	 * Update the form from the ProcessContext before forwarding to the input
	 * jsp
	 */
	@Override
	protected String doInput( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		BDWizardProcessContext context = (BDWizardProcessContext) getProcessContext(request);
		context.populateForm(form);
		//return super.doInput(mapping, form, request, response);
		return super.doInput( form, request, response);

	}

	/**
	 * Process the cancel action. End the process which cleans the Process
	 * context from the session
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	protected ProcessState doCancel(ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		ProcessState nextState = getState().getNext(BDWizardProcessContext.ACTION_CANCEL);
		if (nextState.hasProcessEnded()) {
			endProcess(request);
		}
		return nextState;
	}

	/**
	 * Process the continue action
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	abstract protected ProcessState doContinue(
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws SystemException;

}
