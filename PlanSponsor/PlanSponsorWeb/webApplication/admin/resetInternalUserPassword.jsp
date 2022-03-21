<%-- Prevent the creation of a session --%>
<%@ page session="false" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%-- This is done to load the content. We are not going to use it. --%>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_DEFAULT_EMAIL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Default_Email"/>

<html>
<title>Reset Plan Sponsor Internal User's Password</title>
<body>
<h1>Reset Plan Sponsor Internal User's Password</h1>
<p>
<form method="POST" action="/servlet/ResetInternalUserPassword">
    <table>
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
	  <td colspan="2"><b>=======================================</b></td>
	</tr>	
	<tr>
	  <td><b>Internal User's UserName:</b></td>
	  <td><input type="text" name="iumUserName" maxlength="32" /><td/>
	</tr>
	<tr>
	  <td colspan="2"><input type="submit" name="submit" value="Change Password"/><td/>	
	</tr>
</form>
</p>
<body>
</html>
