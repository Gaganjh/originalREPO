<%-- Prevent the creation of a session --%>
<%@ page session="false" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%-- This is done to load the content. We are not going to use it. --%>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_DEFAULT_EMAIL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Default_Email"/>

<html>
<title>Add Plan Sponsor Basic Internal User</title>
<body>
<h1>Add Plan Sponsor Basic Internal User</h1>
<p>
<form method="POST" action="/servlet/AddBIUser">
    <input type="hidden" name="userRole" value="BIU" />
    <table class="addInternalUserForm">
    <tr> 
      <td colspan="2">
      	<font color="red">
      		<%= request.getAttribute("errorText") == null ? "" : request.getAttribute("errorText") %>
      	</font>
      </td>
    </tr>    
    <tr>
  	  <td><b>Admin's UserName:</b></td>
  	  <td><input type="text" name="adminUserName" maxlength="20" /><td/>
  	</tr>
  	<tr>
	  <td><b>Admin's Password:</b></td>
	  <td><input type="password" autocomplete="off" name="adminPassword" maxlength="32" /><td/>	
	</tr>
  	<tr>
	  <td colspan="2"><b>==================================================</b></td>
	</tr>	
	<tr>
	  <td><b>Basic Internal User's UserName:</b></td>
	  <td><input type="text" name="userName" maxlength="20" /><td/>
	</tr>
	<tr>
	  <td><b>Basic Internal User's First Name:</b></td>
	  <td><input type="text" name="firstName" maxlength="32" /><td/>
	</tr>
	<tr>
	  <td><b>Basic Internal User's LastName:</b></td>
	  <td><input type="text" name="lastName" maxlength="32" /><td/>
	</tr>
	<tr>
	  <td><b>Basic Internal User's Email:</b></td>
	  <td><input type="text" name="email" maxlength="70" /><td/>
	</tr>
	<tr>
	  <td><b>Basic Internal User's Employee Number:</b></td>
	  <td><input type="text" name="employeeNumber" maxlength="9" /><td/>
	</tr>
	<tr>	
	  <td colspan="2"><input type="submit" name="submit" value="Add user"/><td/>	
	</tr>
</form>
</p>
<body>
</html>
