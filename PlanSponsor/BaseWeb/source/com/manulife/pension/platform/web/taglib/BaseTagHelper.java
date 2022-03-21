package com.manulife.pension.platform.web.taglib;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import com.manulife.pension.platform.web.taglib.util.Constants;
import com.manulife.pension.platform.web.taglib.util.SelectTag;



/**
 * This a helper static class for JSP tag operations within PS project
 * 
 * @author Jeff Saremi
 */
public class BaseTagHelper {

	private static final String PRINT_FRIENDLY_KEY = "printFriendly";
	private static final String MONTHLY_KEY = "monthly";
	private static int pageContextScope = PageContext.SESSION_SCOPE;
	private static int pageContextScopeab = PageContext.REQUEST_SCOPE;
	/**
	 * Convenient method to check whether the page is in print friendly mode or
	 * not.
	 * 
	 * @param request
	 *            The current request object.
	 * @return true if the page is in print friendly mode, false if otherwise.
	 */
	public static boolean isPrintFriendly(ServletRequest request) {
		return "true"
				.equalsIgnoreCase(request.getParameter(PRINT_FRIENDLY_KEY));
	}

	/**
	 * Convenient method to check whether the page is in print friendly mode or
	 * not.
	 * 
	 * @param pageContext
	 *            The current page context object.
	 * @return true if the page is in print friendly mode, false if otherwise.
	 */
	public static boolean isPrintFriendly(PageContext pageContext) {
		return "true".equalsIgnoreCase(pageContext.getRequest().getParameter(
				PRINT_FRIENDLY_KEY));
	}
	
	/**
	 * Convenient method to check whether the page opted with Monthly or Quarterly
	 * 
	 * @param request
	 *            The current request object.
	 * @return true if the page is in print friendly mode, false if otherwise.
	 */
	public static boolean isMonthly(HttpServletRequest request) {
		String montly = (String)request.getSession().getAttribute(MONTHLY_KEY);
		return "true"
				.equalsIgnoreCase(montly);
	}

	/**
	 * Obtains the enclosing Struts select tag. This method is typically used
	 * by the option/options tag to obtain the selected value.
	 * 
	 * @param pageContext
	 *            The current page context object.
	 * @return The Struts Select Tag.
	 * @throws JspException
	 *             If pageContext cannot find the select tag.
	 */
	public static SelectTag getEnclosingSelectTag(PageContext pageContext)
			throws JspException {

		SelectTag selectTag = (SelectTag) pageContext
				.getAttribute(Constants.SELECT_KEY);

		if (selectTag == null) {
			throw new JspException("Cannot found select tag");
		}

		return selectTag;
	}

	/**
	 * Obtains the action form in the current request
	 * 
	 * @param pageContext
	 *            The current page context.
	 * @return The Form associated with the current request (or session).
	 * @throws JspException
	 *             if RequestUtils throws JspException.
	 * @throws IllegalStateException
	 *             if the Form cannot be found.
	 */
	

	public static com.manulife.pension.ezk.web.ActionForm getSpringForm(PageContext pageContext,String actionForm)
			throws JspException {
		
		Object bean = pageContext.getAttribute(actionForm, pageContextScope);
		if (bean == null) {
			bean = pageContext.getAttribute(actionForm, pageContextScopeab);
		}
		if (bean == null) {
			throw new IllegalStateException("Action form is not found!");
		}
		else
		return (com.manulife.pension.ezk.web.ActionForm) bean;
	}

	
	
	public static String getPublicUrl(String actionUrl) {
		String url;
		try {
			RE regexp = new RE("/WEB-INF/");
			url = regexp.subst(actionUrl, "/");
		} catch (RESyntaxException e) {
			// this should NEVER happen.
			throw new RuntimeException("Invalid Regular Expression Syntax");
		}
		return url;
	}
}
