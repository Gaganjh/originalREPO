package com.manulife.pension.ps.web.admin;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.security.exception.FailedAlmostNTimesException;
import com.manulife.pension.service.security.exception.FailedNTimesException;
import com.manulife.pension.service.security.exception.IncorrectPasswordException;
import com.manulife.pension.service.security.exception.InsufficientPrivilegesException;
import com.manulife.pension.service.security.exception.LockedUserException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNotFoundException;
import com.manulife.pension.service.security.exception.UserNotRegisteredException;
import com.manulife.pension.service.security.passcode.PsRequestDetails;
import com.manulife.pension.service.security.role.SystemAdministrator;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;
import com.manulife.pension.service.security.valueobject.UserInfo;



/**
 * @author Ilker Celikyilmaz
 * 
 * This is the servlet to reset Internal User's password (by System Admin).
 * 
 */
public class ResetIUMPasswordServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(this.getClass());


	public void init(ServletConfig config) throws ServletException
	{
		super.init ( config );
	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request,	HttpServletResponse response)
	 * 
	 */
	public void doPost(HttpServletRequest request,	HttpServletResponse response)
		throws ServletException, IOException 
	{
		processRequest(request, response);
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request,	HttpServletResponse response)
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 	
	{
		processRequest(request, response);
	}


	/**
	 * This method process the request.
	 * 
	 * @param request
	 * 		HttpServletRequest object
	 * @param response
	 * 		HttpServletResponse object
	 */
	public void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 	
	{
		if ( logger.isDebugEnabled() ) 
			logger.debug("enter -> processRequest");
		String errorText = "";
		String adminUserName = request.getParameter("adminUserName");
		String adminPassword = request.getParameter("adminPassword");		
		String iumUserName = request.getParameter("iumUserName");				

		// validate inputs
		if ( adminUserName == null || adminUserName.trim().length() == 0)
			errorText = "Administrator's Username is not valid.<br>";
		
		if ( adminPassword == null || adminPassword.trim().length() == 0)
			errorText = errorText + "Administrator's Password can't be empty.<br>";				
		
		if ( iumUserName == null || iumUserName.trim().length() == 0)
			errorText = errorText + "Internal user's User Name can't be empty.<br>";				
		
		if ( errorText.trim().length() == 0 )
		{
			// login System Admin
			LoginPSValueObject lv = null;
			try {
				lv = SecurityServiceDelegate.getInstance().loginPS(adminUserName, adminPassword,
						Environment.getInstance().getSiteLocation(), new PsRequestDetails(request));
				if ( !(lv.getPrincipal().getRole() instanceof SystemAdministrator) )
					errorText = "This user is not authorized to perform this operation using this screen.";

			} 
			catch (UserNotFoundException e)
			{
				errorText = "Admin User does not Exist in the system.";
			}
			catch (IncorrectPasswordException e)
			{
				errorText = "Admin User's Password is not correct.";
			}
			catch (FailedAlmostNTimesException e)
			{
				errorText = "Admin User's password is not correct (failed 3 times).";
			}
			catch (FailedNTimesException e)
			{
				errorText = "Admin User's account is locked.";
			}
			catch (UserNotRegisteredException e)
			{
				errorText = "Admin User is not registered.";
			}
			catch (LockedUserException e)
			{
				errorText = "Admin User is already Locked.";
			}
			catch (SecurityServiceException e)
			{
				errorText = e.getMessage();	
			}
			catch (SystemException e)
			{
				errorText = e.getMessage();	
			}
			
			if ( errorText.trim().length() == 0 ) 
			{
				try {
					// reset IUM's password
					UserInfo userInfo = new UserInfo();
					userInfo.setUserName(iumUserName);				
					SecurityServiceDelegate.getInstance().resetPassword(lv.getPrincipal(),
							userInfo, Environment.getInstance().getSiteLocation());
				}				
				catch (UserNotFoundException e)
				{
					errorText = "Internal User does not Exist in the system.";
				}
				catch (InsufficientPrivilegesException e)
				{
					errorText = "You don't have sufficent priviligies.";
				}				
				catch (SecurityServiceException e)
				{
					errorText = e.getMessage();	
				}
				catch (SystemException e) 
				{
					errorText = e.getMessage();	
				}  
			}		 
		
		}
		
		if ( errorText.trim().length() > 0 )
		{
			request.setAttribute("errorText", errorText);
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/admin/resetInternalUserPassword.jsp");
			rd.forward(request,response);
		}
		else
		{
			PrintWriter out = response.getWriter();
			out.println("<html><title>Reset Internal User's Password Confirmation</title>");
			out.println("<body>");
			
			out.println("<b>" + iumUserName + "</b> user's password is reset.<br><br>");
			out.println("<a href=\"/admin/resetInternalUserPassword.jsp\">Reset Another Internal User's Password</a>");
			
			out.println("</body>");
			out.println("</html>");
			
			out.flush();
			out.close();
		}
		
		
		if ( logger.isDebugEnabled() ) 
			logger.debug("exit <- processRequest");
	}
}
