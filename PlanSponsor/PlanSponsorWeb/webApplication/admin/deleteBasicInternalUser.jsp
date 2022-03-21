<%-- Prevent the creation of a session --%>
<%@ page session="false" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%-- This is done to load the content. We are not going to use it. --%>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_DEFAULT_EMAIL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Default_Email"/>

<html>
<title>Delete Basic Internal User User</title>
<body>
<h1>Delete Basic Internal User User</h1>
<p>
<form method="POST" action="/servlet/DeleteBIUser">
	<input type="hidden" name="userRole" value="BIU" />
    <table class="deleteInternalUserForm">
    <tr> 
      <td colspan="2">
      	<font color="red">
      		<%= request.getAttribute("errorText") == null ? "" : request.getAttribute("errorText") %>
      	</font>
      </td>
    </tr>
    <tr>
  	  <td><b>Admin's UserName:</b></td>
  	  <td><input type="text" name="adminUserName" maxlength="32" /><td/>
  	</tr>
  	<tr>
	  <td><b>Admin's Password:</b></td>
	  <td><input type="password" autocomplete="off" name="adminPassword" maxlength="32" /><td/>	
	</tr>
  	<tr>
	  <td colspan="2"><b>===========================================</b></td>
	</tr>	
	<tr>
	  <td><b>Basic Internal User's UserName:</b></td>
	  <td><input type="text" name="userName" maxlength="20" /><td/>
	</tr>
	<tr>
	  <td colspan="2"><input type="submit" name="submit" value="Delete User"/><td/>	
	</tr>
</form>
</p>
<body>
</html>
