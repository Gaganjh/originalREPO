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
 * This is the servlet to add Internal User Manager (by System Admin).
 * 
 */
public class AddIUMUserServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(this.getClass());
	private static String INTERNAL_USER_MANAGER = (new InternalUserManager()).toString(); 
	private static String RELATIONSHIP_MANAGER = (new RelationshipManager()).toString(); 


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
	 * This method process the request and Add the user with Internal Uset Manager or
	 * Relationship Manager role. This servlet used by both add user pages.
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
		String firstName = request.getParameter("firstName");				
		String lastName = request.getParameter("lastName");				
		String email = request.getParameter("email");				
		String employeeNumber = request.getParameter("employeeNumber");			
		String userRole = request.getParameter("userRole");

		// validate inputs
		if ( adminUserName == null || adminUserName.trim().length() == 0)
			errorText = "Administrator's Username is not valid.<br>";
		
		if ( adminPassword == null || adminPassword.trim().length() == 0)
			errorText = errorText + "Administrator's Password can't be empty.<br>";				
		
		if ( userName == null || userName.trim().length() == 0)
			errorText = errorText + "User Name can't be empty.<br>";				

		if ( firstName == null || firstName.trim().length() == 0)
			errorText = errorText + "First Name can't be empty.<br>";				

		if ( lastName == null ||lastName.trim().length() == 0)
			errorText = errorText + "Last Name can't be empty.<br>";				

		if ( email == null || email.trim().length() == 0)
			errorText = errorText + "Email address can't be empty.<br>";				

		if ( employeeNumber == null || employeeNumber.trim().length() == 0)
			errorText = errorText + "Employee Number can't be empty.<br>";				

		
		if ( errorText.trim().length() == 0 )
		{
			// login System Admin
			LoginPSValueObject lv = null;
			try {
				lv = SecurityServiceDelegate.getInstance().loginPS(adminUserName, adminPassword,
						Environment.getInstance().getSiteLocation(), new PsRequestDetails(request));

				if (!(lv.getPrincipal().getRole() instanceof SystemAdministrator))
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
					// add User
					UserInfo userInfo = new UserInfo();
					userInfo.setUserName(userName);	
					userInfo.setFirstName(firstName);
					userInfo.setLastName(lastName);					
					userInfo.setEmail(email);															
					userInfo.setEmployeeNumber(employeeNumber);	
					userInfo.setWebAccessInd(true);
					if ( INTERNAL_USER_MANAGER.equals(userRole) )
						userInfo.setRole(new InternalUserManager());
					else if ( RELATIONSHIP_MANAGER.equals(userRole) )
						userInfo.setRole(new RelationshipManager());
					else
						userInfo.setRole(new BasicInternalUser());
								
					SecurityServiceDelegate.getInstance().addUser(lv.getPrincipal(), userInfo,
							Environment.getInstance().getSiteLocation());
				}				
				catch (UserNotFoundException e)
				{
					errorText = "User does not Exist in the system.";
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
			
			RequestDispatcher rd = null;
			if ( INTERNAL_USER_MANAGER.equals(userRole) )
				rd = getServletContext().getRequestDispatcher("/admin/addInternalUserManager.jsp");
			else if ( RELATIONSHIP_MANAGER.equals(userRole) )
				rd = getServletContext().getRequestDispatcher("/admin/addRelationshipManager.jsp");
			else {
        rd = getServletContext().getRequestDispatcher("/admin/addBasicInternalUser.jsp");
      }
			rd.forward(request,response);
		}
		else
		{
			PrintWriter out = response.getWriter();
      
			if ( INTERNAL_USER_MANAGER.equals(userRole) )
				out.println("<html><title>Add Internal User Manager Confirmation</title>");
			else if ( RELATIONSHIP_MANAGER.equals(userRole) )
				out.println("<html><title>Add Relationship Manager Confirmation</title>");
      else 
        out.println("<html><title>Add Basic Internal User Confirmation</title>");

      out.println("<body>");
			
			if ( INTERNAL_USER_MANAGER.equals(userRole) ) {
				out.println("<b>" + userName + "</b> Internal User Manager user is created.<br><br>");
				out.println("<a href=\"/admin/addInternalUserManager.jsp\">Add Another Internal User Manager</a>");
			} else if ( RELATIONSHIP_MANAGER.equals(userRole) ) {
				out.println("<b>" + userName + "</b> Relationship Manager user is created.<br><br>");
				out.println("<a href=\"/admin/addRelationshipManager.jsp\">Add Another Relationship Manager</a>");
			} else {
				out.println("<b>" + userName + "</b> Basic Internal user is created.<br><br>");
				out.println("<a href=\"/admin/addBasicInternalUser.jsp\">Add Another Basic Internal User</a>");
			}
			
			out.println("</body>");
			out.println("</html>");
			
			out.flush();
			out.close();
		}
		
		
		if ( logger.isDebugEnabled() ) 
			logger.debug("exit <- processRequest");
	}
}
