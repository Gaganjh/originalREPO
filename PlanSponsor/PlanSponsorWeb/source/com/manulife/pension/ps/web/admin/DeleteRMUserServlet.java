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
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.service.security.role.BasicInternalUser;
import com.manulife.pension.service.security.role.SystemAdministrator;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;
import com.manulife.pension.service.security.valueobject.UserInfo;



/**
 * @author Ilker Celikyilmaz
 * 
 * This is the servlet to delete the Relationship Manager/Internal user Manager (by System Admin).
 * 
 */
public class DeleteRMUserServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(this.getClass());
	private static String RELATIONSHIP_MANAGER = (new RelationshipManager()).toString();
	private static String BASIC_INTERNAL_USER = (new BasicInternalUser()).toString();

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
	 * This method process the request to delete the RM user
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
		String userName = request.getParameter("userName");	
		String userRole = request.getParameter("userRole");

		// validate inputs
		if ( adminUserName == null || adminUserName.trim().length() == 0)
			errorText = "Administrator's Username is not valid.<br>";
		
		if ( adminPassword == null || adminPassword.trim().length() == 0)
			errorText = errorText + "Administrator's Password can't be empty.<br>";				
		
		if ( userName == null || userName.trim().length() == 0){
			if ( RELATIONSHIP_MANAGER.equals(userRole) )
				errorText = errorText + "Relationship Manager's User Name can't be empty.<br>";
			else if ( BASIC_INTERNAL_USER.equals(userRole) )
				errorText = errorText + "Basic Internal User's User Name can't be empty.<br>";
			else
				errorText = errorText + "Internal User Manager's User Name can't be empty.<br>";
		}
			
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
					// Delete RM user
					UserInfo userInfo = new UserInfo();
					userInfo.setUserName(userName);
					// Setting the role specific to the type of user deleted
					if ( RELATIONSHIP_MANAGER.equals(userRole) )
						userInfo.setRole(new RelationshipManager());
					else if ( BASIC_INTERNAL_USER.equals(userRole) )
						userInfo.setRole(new BasicInternalUser());
					else
						userInfo.setRole(new InternalUserManager());
					
					SecurityServiceDelegate.getInstance().deleteUser(lv.getPrincipal(), userInfo,
							Environment.getInstance().getSiteLocation());
				}				
				catch (UserNotFoundException e)
				{
					if ( RELATIONSHIP_MANAGER.equals(userRole) )
						errorText = "Relationship Manager user does not Exist in the system.";
					else if ( BASIC_INTERNAL_USER.equals(userRole) )
						errorText = "Basic Internal User does not Exist in the system.";
					else
						errorText = "Internal User Manager user does not Exist in the system.";
				}
				catch (InsufficientPrivilegesException e)
				{
					errorText = "You don't have sufficient privileges.";
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
			RequestDispatcher rd = null;
			request.setAttribute("errorText", errorText);
			if ( RELATIONSHIP_MANAGER.equals(userRole) )
				rd = getServletContext().getRequestDispatcher("/admin/deleteRelationshipManager.jsp");
			else if ( BASIC_INTERNAL_USER.equals(userRole) )
				rd = getServletContext().getRequestDispatcher("/admin/deleteBasicInternalUser.jsp");
			else
				rd = getServletContext().getRequestDispatcher("/admin/deleteInternalUserManager.jsp");
			rd.forward(request,response);
		}
		else
		{
			PrintWriter out = response.getWriter();
			
			// Here we get the success page specific to the type of user deleted
			if ( RELATIONSHIP_MANAGER.equals(userRole) )
				out.println("<html><title>Deletion of Relationship Manager's Confirmation</title>");
			else if ( BASIC_INTERNAL_USER.equals(userRole) )
				out.println("<html><title>Deletion of Basic Internal User's Confirmation</title>");
			else
				out.println("<html><title>Deletion of Internal User Manager's Confirmation</title>");
			out.println("<body>");
			
			out.println("<b>" + userName + "</b> user is being deleted.<br><br>");
			if ( RELATIONSHIP_MANAGER.equals(userRole) )
				out.println("<a href=\"/admin/deleteRelationshipManager.jsp\">Delete Another Relationship Manager</a>");
			if ( BASIC_INTERNAL_USER.equals(userRole) )
				out.println("<a href=\"/admin/deleteBasicInternalUser.jsp\">Delete Another Basic Internal User</a>");
			else
				out.println("<a href=\"/admin/deleteInternalUserManager.jsp\">Delete Another Internal User Manager</a>");
			
			out.println("</body>");
			out.println("</html>");
			
			out.flush();
			out.close();
		}
		
		
		if ( logger.isDebugEnabled() ) 
			logger.debug("exit <- processRequest");
	}
}
