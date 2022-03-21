package com.manulife.pension.bd.web.login;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.home.HomePageHelper;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.pagelayout.LayoutStore;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.util.LoginCookieEncryptorDecryptor;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

/**
 * The login servlet to front-end the login process. It uses the LoginHandler to
 * do the login logic
 * 
 * @author guweigu
 * 
 */
public class LoginServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(LoginServlet.class);
	
	public static final String USERNAME_PARAM = "userName";
	public static final String PASSWORD_PARAM = "password";
	public static final String REMEMBER_ME_PARAM = "rememberMe";
	
	private static final String SYSTEM_ERROR_PAGE = "/error.jsp";
	
	private static final int TEN_YEARS_IN_SECONDS = 10 * 365 * 24 * 60 * 60;
	private static final int DELETE_COOKIE = 0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LOGIN_PAGE = "/home/public_home.jsp";
	private static final String LOGIN_PAGE_LAYOUT = "/WEB-INF/layouts/public_layout.jsp";
	private String errorKey;

	public LoginServlet() {
		errorKey = Environment.getInstance().getErrorKey();
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter(USERNAME_PARAM);
		String password = request.getParameter(PASSWORD_PARAM);
		String rememberMe = request.getParameter(REMEMBER_ME_PARAM);
		String devicePrint = request.getParameter(CommonConstants.DEVICE_PRINT);
		String encryptedUserName = LoginCookieEncryptorDecryptor.encrypt(LoginCookie.userNameToEncrypt(userName));
		
		try {
			List<ValidationError> errors = LoginHandler.getInstance(devicePrint).doLogin(
					userName, password, request, response, null);

			if (errors.size() > 0) {
				handleErrors(getServletContext(), request, response, errors);
			} else {
				
				LoginCookie userNameCookie;
				if("on".equals(rememberMe)){ 
					// persisting cookie in users hard disk to remember Logged-in user
					// 			for at least 10years in future
					userNameCookie = new LoginCookie(encryptedUserName, TEN_YEARS_IN_SECONDS);
				}else{
					// Need not remember logged in User - delete cookie
					userNameCookie = new LoginCookie(encryptedUserName, DELETE_COOKIE);
				}
				response.addCookie(userNameCookie);
				boolean changePasswordFlag = Boolean.FALSE;
				BDUserProfile profile = BDSessionHelper.getUserProfile(request);
				BDPrincipal principal = null;
				if(null != profile){
					principal = profile.getBDPrincipal();
				}
				String businessParamValue = null;
				
				EnvironmentServiceDelegate service = null; 
	    		
	    		
	    		try {
	    			
	    			service = EnvironmentServiceDelegate
		    				.getInstance();
	    			businessParamValue = service.getBusinessParam(BDConstants.FRW_SHOW_PASSWORD_METER_IND);
	    			
	    		    request.getSession(false).setAttribute(BDConstants.FRW_SHOW_PASSWORD_METER_IND,businessParamValue);
	    			
	    		} catch (SystemException e) {
	    			log.error("Fail to retrieve "+BDConstants.FRW_SHOW_PASSWORD_METER_IND+" businessParamValue", e);
	    		}
				try {
					changePasswordFlag = BDUserSecurityServiceDelegate.getInstance().checkChangePasswordForNewUser(principal, 
							password);
					changePasswordFlag = changePasswordFlag || checkUsernamePasswordSequence(userName,password);
					} catch (SecurityServiceException e) {
					    log.error("Login fails with system exception", e);
					}

				HttpSession session = request.getSession(false);
				if (session.getAttribute(BDConstants.USERID_KEY) != null) {
					session.setAttribute("changePasswordFlag",changePasswordFlag);
					session.setAttribute(BDConstants.FRW_SHOW_PASSWORD_METER_IND,businessParamValue);
					response.sendRedirect("/do/stepupTransition/");
				} else{
					request.getSession(false).setAttribute("changePasswordFlag",changePasswordFlag);
					getServletContext()
					.getRequestDispatcher(URLConstants.PostLogin).forward(
							request, response);
				}

			}
		} catch (SystemException e) {
			log.error("Login fails with system exception", e);
			getServletContext().getRequestDispatcher(SYSTEM_ERROR_PAGE)
					.forward(request, response);
		}
	}

	private void handleErrors(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			List<ValidationError> errors) throws IOException, ServletException {
		
		request.setAttribute(errorKey, errors);
		LayoutStore layoutStore = ApplicationHelper
				.getLayoutStore(servletContext);
		BDLayoutBean bean = layoutStore.getLayoutBean(LOGIN_PAGE, request);
		request.setAttribute(BDConstants.LAYOUT_BEAN, bean);
		String encryptedUserName = HomePageHelper.getCookieValue(request.getCookies(), LoginCookie.getLoginCookieName());
		if (StringUtils.isNotEmpty(encryptedUserName)) {
			String username = LoginCookie.parseUserName(LoginCookieEncryptorDecryptor.decrypt(encryptedUserName));
			request.setAttribute(BDConstants.LOGIN_NAME_FROM_COOKIE_ATTRIBUTE, username);
			request.setAttribute(BDConstants.LOGIN_REMEBER_ME_ATTRIBUTE, BDConstants.LOGIN_REMEMBER_ME_ON);
		}
		BDSessionHelper.invalidateSession(request, response, false);
		RequestDispatcher rd = request.getRequestDispatcher(LOGIN_PAGE_LAYOUT);		
		rd.forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	private Boolean checkUsernamePasswordSequence(String uname, String pwd){
		  Boolean contains=false;
		  
		  for (String seq: uname.split("/[\\@\\-\\.\\_]/g")){ 
		     if(pwd.contains(seq)|| pwd.contains(seq.toUpperCase())|| pwd.contains(seq.toLowerCase())){
		        contains=true;
		        break;
		     }
		  }
		  
		  if(pwd.contains(uname)){
			  contains = true;
		  }
		  return contains;
		}
}
