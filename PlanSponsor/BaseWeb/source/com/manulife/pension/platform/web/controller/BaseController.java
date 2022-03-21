package com.manulife.pension.platform.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.platform.web.util.CommonEnvironment;

/**
 * Refactoring the PsAction. Move the PSW specific logic to 
 * PlaformFacade interface so that both PSW and BDW can have
 * different implementation
 * 
 * @author guweigu
 *
 */
 public abstract class BaseController {
	 
	 
	protected Logger logger = null;
	
	public static final String SYSTEM_ERROR_PAGE = "systemErrorPage";
	
	private static final String SUBMIT_ACTION_ATTRIBUTE= "com.manulife.pension.platform.web.controller.BaseAction.submitAction";
	public static final String ACTION_INPUT = "action";
    public static final String ACTION_BUTTON = "action.";
    public static final int ACTION_NAME_START_INDEX= ACTION_BUTTON.length();
    
    @SuppressWarnings("unused")
	private static final Logger LOGGER= Logger.getLogger(BaseController.class);
    
	@SuppressWarnings("rawtypes")
	public BaseController(Class clazz) {
		logger = Logger.getLogger(clazz);
	}

	/*public  String doExecute(
			Form form,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		return null;
	}
*/
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		getApplicationFacade(request).actionPreExecute(request);
		return null;
	}

	protected void postExecute(ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
	}

	protected ApplicationFacade getApplicationFacade(HttpServletRequest request) {
		
		//return (ApplicationFacade) getServlet().getServletContext().getAttribute(CommonConstants.APPLICATION_FACADE_KEY);
		return (ApplicationFacade) request.getServletContext().getAttribute(CommonConstants.APPLICATION_FACADE_KEY);
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

	/**
     * Get the submit action name. The returned name will be processed to be ready to call the invokeMethod().
     */
    protected String getSubmitAction(HttpServletRequest request, ActionForm form) throws ServletException {
        String submitAction= (String)request.getAttribute(SUBMIT_ACTION_ATTRIBUTE);
        if (submitAction == null) {
            submitAction= getRealActionName(retrieveActionFromRequest(request));
            request.setAttribute(SUBMIT_ACTION_ATTRIBUTE, submitAction);
        }

        return submitAction;
    }
	/*public String execute(Form form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		//ActionForward forward = null;
		String forward = null;
        String action = getSubmitAction(request, form);
        if (isTokenValidatorEnabled(action)) { 
    	    // PENTEST: Token validation to prevent CSRF vulnerability
		      String referringPageURL = request.getHeader(CommonConstants.REFERER);
              boolean isReferredFromWarningPage = (referringPageURL != null && referringPageURL.contains(CommonConstants.WARNING));
    	    if(!isTokenValid(request) && !isReferredFromWarningPage){
    		  StringBuffer csrfLogMsg = new StringBuffer();
              csrfLogMsg.append("CSRF forged Request detected: [Page Request: " + request.getRequestURL() + request.getQueryString());
              HttpSession session = request.getSession();
              
              if (session != null) {
            	  BaseUserProfile userProfile = BaseSessionHelper.getBaseUserProfile(request);
              String ipAddress = request.getHeader(CommonConstants.FORWARD_HEADER_KEY);   
              if (ipAddress == null) {   
                              ipAddress = request.getRemoteAddr();   
              } 
                csrfLogMsg.append(" IP Address: " + ipAddress); 
             } 
            csrfLogMsg.append("]");
            LOGGER.error(csrfLogMsg.toString());
			request.getSession().setAttribute("Error Message", CommonConstants.ERROR_RDRCT);
			forward = getApplicationFacade().createLayoutBean(request, mapping.findForward("csrfErrorPage"), mapping);
      		return forward ;
    	}
       
       } 
        
		try {
			forward = preExecute(form, request, response);
			if (forward == null) {
				//Remove XSS errors from session
				//CommonEnvironment.initialize(Environment.getInstance());
				String key = CommonEnvironment.getInstance().getErrorKey();
				HttpSession session = null;
				if(StringUtils.isNotBlank(key)){
					session = request.getSession(false);
					String flag = null;
					Boolean loaded = true;
					if(session != null){
						 flag = (String)session.getAttribute(StringUtils.equalsIgnoreCase("BD", getApplicationFacade(request)
									.getApplicationId())?CommonErrorCodes.ERROR_FRW_VALIDATION:CommonErrorCodes.ERROR_PSWVALIDATION_MASK);
					loaded = (Boolean)session.getAttribute("loaded");
					if(StringUtils.isNotBlank(flag) &&(StringUtils.equalsIgnoreCase(CommonErrorCodes.ERROR_PSWVALIDATION_MASK,flag) && (loaded!= null ? loaded : true)))
								session.removeAttribute(key);
								session.removeAttribute("loaded");
					}
				}
				// first validate
				forward = validate(form,request);
				if (forward == null) {
					forward = doExecute(form,request, response);
				}
			}
			//To avoid token generation in IFrame calls
            if(isTokenRequired(action) && forward != null){
            	// Set token to avoid CSRF vulnerability
            	saveToken(request);
            }
			forward = getApplicationFacade(request).createLayoutBean(request, forward);
			postExecute( form,request, response);
		} catch (SystemException e) {
			logDebug("SystemException caught in PsAction:" 	+ e.getUniqueId(), e);
		
			LogUtility.logSystemException(getApplicationFacade(request).getApplicationId(), e);

			request.setAttribute("errorCode", "1099");
			request.setAttribute("uniqueErrorId", e.getUniqueId());

			// forward to Error Page
			//return mapping.findForward(SYSTEM_ERROR_PAGE);
			return SYSTEM_ERROR_PAGE;
		}

		return forward;
	}
*/
	/*protected boolean isTokenRequired(String action) {

		if (StringUtils.equalsIgnoreCase("BD", getApplicationFacade()
				.getApplicationId()))
			return false;

		return true;
	}*/

	protected boolean isTokenValidatorEnabled(String action) {
		return false;
	}

	/**
	 * This method calls doValidate for doing validation.
	 * 
	 * @param form
	 *            Form objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * 
	 * @return Collection of errors
	 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String validate(ActionForm form,HttpServletRequest request) {

		Collection errors = doValidate(form,request);
		setErrorsInRequest(request, errors);

		if (!errors.isEmpty()) {
			
			if (request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) != null) {
				String forward = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forward;
			}
			//return mapping.getInputForward();
			return "input";
			
		}
		return null;
		
	}

	/**
	 * This is the method to be extended for validation.
	 * 
	 * @param mapping
	 *            TODO
	 * @param form
	 *            Form objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * 
	 * @return Empty Collection
	 */
	@SuppressWarnings("rawtypes")
	protected Collection doValidate(ActionForm form,
			HttpServletRequest request) {
			return new ArrayList();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setErrorsInRequest(HttpServletRequest request,
			Collection errors) {
		// check for errors already in request scope
		Collection e = (Collection) request.getAttribute(CommonEnvironment.getInstance().getErrorKey());
		if (e != null) {
			errors.addAll(e);
			request.removeAttribute(CommonEnvironment.getInstance().getErrorKey());
		}
		request.setAttribute(CommonEnvironment.getInstance().getErrorKey(),errors);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setErrorsInSession(HttpServletRequest request,
			Collection errors) {
		BaseSessionHelper.setErrorsInSession(request, errors);
	}

	/**
     * Returns a non-null String from the given object.
     * 
     * @param s
     *            The object to retrieve string for.
     * @return The string representation of the object (by invoking the
     *         toString() method on the object) or an empty string if the object
     *         is null.
     */
    public static String getCsvString(Object s) {

        if (s == null) {
            return "";
        } else {
            return "\"" + s.toString() + "\"";
        }
    }
    
	/**
	 * Method logDebug is used to log debug message only when debug is enabled  
	 * @param message
	 */
	protected void logDebug(String message){
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}
	
	/**
	 * Method logDebug is used to log debug message only when debug is enabled  
	 * @param message logging mesage
	 * @param t Throwable
	 */
	protected void logDebug(String message, Throwable t) {
		if (logger.isDebugEnabled()) {
			logger.debug(message, t);
		}
	}
}
