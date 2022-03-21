package com.manulife.pension.ps.web.controller.spring;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


public class HttpSessionUtility {
	private static final String EMPTY_VALUE = "";
    private static final Logger logger = Logger.getLogger(PsAccessDeniedHandler.class);
    private static final String JSESSIONID = "JSESSIONID";
    private static final String LOGIN_PAGE_DIRECT = "/home/public_home.jsp";
	  /**
   * If the user is unknown we want to ensure that if there WAS a session created by spring at any
   * point, that we are invalidating that session and removing the JSESSIONID before forwarding to
   * the login page.
   * 
   * @param request
   * @param response
   * @throws IOException
   * @throws ServletException
   */
  public static void handleUnknownUser(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {
      if (logger.isDebugEnabled()) {
          logger.debug("Redirecting to Login Page");
      }
      HttpSession session = request.getSession(false);
      if (session != null) {
          invalidateSessionAndCookie(request, response);
      } else {
          Cookie[] cookies = request.getCookies();
          boolean sanitizeCookieInResponse = false;
          if (cookies != null) {
              for (Cookie cookie : cookies) {
                  if (cookie.getName().equalsIgnoreCase(JSESSIONID)) {
                      sanitizeCookieInResponse = true;
                  }
                  if (sanitizeCookieInResponse == true) {
                      invalidateSessionAndCookie(request, response);
                  }
              }
          }
      }
      // Bypass to avoid creating a new session on the login page:
      RequestDispatcher rd =request.getRequestDispatcher(LOGIN_PAGE_DIRECT);
      rd.forward(request, response);
  }

  /**
   * Invalidates the session (which will still leave a JSESSIONID cookie behind) and overwrites
   * the JSESSIONID cookie with a blank value, so that the session id may not be re-used for
   * hacking into an account.
   * 
   * You must avoid hitting spring after calling this or spring mappings will recreate the session
   * again, and put a new JSESSIONID cookie on the browser, which will show on the login page.
   * 
   * @param request
   * @param response
   */
  private static void invalidateSessionAndCookie(HttpServletRequest request, HttpServletResponse response) {
      HttpSession session = request.getSession(false);
      if (session != null) {
          session.invalidate();
      }
      removeSessionCookie(request, response);
  }
  
  /**
   * Removes the JSESSIONID cookie from the response by overwriting it with a dud.
   * @param request
   * @param response
   */
  private static void removeSessionCookie(HttpServletRequest request, HttpServletResponse response) {      
      Cookie deadCookie = new Cookie(JSESSIONID, EMPTY_VALUE);
      deadCookie.setPath("/");
      deadCookie.setMaxAge(0);
      response.setContentType("text/html");
      response.addCookie(deadCookie);
  }
}
