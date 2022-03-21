package com.manulife.pension.ps.web.controller.spring;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.manulife.pension.ps.web.withdrawal.WebConstants;

public class PsLoggingInterceptor implements HandlerInterceptor {

	
	private static final String SUBMIT_ACTION_ATTRIBUTE= "com.manulife.pension.platform.web.controller.BaseAction.submitAction";
	public static final String ACTION_INPUT = "action";
    public static final String ACTION_BUTTON = "action.";
    public static final int ACTION_NAME_START_INDEX= ACTION_BUTTON.length();
    
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception ex)
			throws Exception {
			
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		HttpSession session = request.getSession(false);
        if (session != null) {
        	session.removeAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION);
        }
		return true;
	}
	
	/**
     * Get the submit action name. The returned name will be processed to be ready to call the invokeMethod().
     */
    protected String getSubmitAction(HttpServletRequest request) throws ServletException {
        String submitAction= (String)request.getAttribute(SUBMIT_ACTION_ATTRIBUTE);
        if (submitAction == null) {
            submitAction= getRealActionName(retrieveActionFromRequest(request));
            request.setAttribute(SUBMIT_ACTION_ATTRIBUTE, submitAction);
        }

        return submitAction;
    }
    
    /**
     * Retrieve the action name from the request as it appear in the request.
     */
	private String retrieveActionFromRequest(HttpServletRequest request) throws ServletException {
        String actionName= null;

        actionName= request.getParameter("action");
        if ((actionName != null) && !("".equals(actionName))) {
            return actionName;
        }
        
        actionName= request.getParameter("task");
        if ((actionName != null) && !("".equals(actionName))) {
            return actionName;
        }

        Enumeration parameterNames= request.getParameterNames();
        searchActionParameter : while (parameterNames.hasMoreElements()) {
            String parameterName= (String)parameterNames.nextElement();

            if (parameterName.startsWith(ACTION_BUTTON)) {
                int dot= parameterName.indexOf('.', ACTION_NAME_START_INDEX);
                if (dot == -1) {
                    actionName= parameterName.substring(ACTION_NAME_START_INDEX);
                } else {
                    actionName= parameterName.substring(ACTION_NAME_START_INDEX, dot);
                }

                return actionName;
            }
        }

        // no action found. We return "Default".
        return "Default";
    }
	
	 /**
     * Transform the name of the action to uppercase the first letter of each word and to remove all spaces.
     */
    private String getRealActionName(String action) {
        StringBuffer realAction= new StringBuffer(action.length());
        int space= -1;
        int currentIndex= 0;
        do {
            space= action.indexOf(' ', currentIndex);
            if (space == -1) {
                realAction.append(Character.toUpperCase(action.charAt(currentIndex)));
                realAction.append(action.substring(currentIndex+1));
            } else {
                realAction.append(Character.toUpperCase(action.charAt(currentIndex)));
                realAction.append(action.substring(currentIndex+1, space));
            }
            currentIndex= space+1;
        } while (space != -1);

        return realAction.toString();
    }
    
}
