<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.password.ChangePasswordForm" %>

<%
	ChangePasswordForm changePasswordForm = (ChangePasswordForm)session.getAttribute("changePasswordForm");
	pageContext.setAttribute("changePasswordForm",changePasswordForm,PageContext.PAGE_SCOPE);
%>

<c:set var="changePasswordForm" value="${changePasswordForm}" scope="page" />


<script language="JavaScript1.2" type="text/javascript">
<!--

var submitted=false;

function setButtonAndSubmit(button) {

	if (!submitted) {
		submitted=true;
		document.changePasswordForm.button.value = button;
		document.changePasswordForm.submit();
	} else {
		window.status = "Transaction already in progress.  Please wait.";
	}
}

-->
</script>
<% 
   String actionUrl = "/do/password/changePasswordConfirmation/";
   if (!changePasswordForm.isPasswordReset()) { 
	actionUrl="/do/password/changePasswordConfirmationInternal/";
   } 
   %>

<ps:form method="POST" modelAttribute="changePasswordForm" name="changePasswordForm" action="<%= actionUrl %>">

<%-- table with 3 columns --%>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
      <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
      <td width="65" ><img src="/assets/unmanaged/images/s.gif" width="65" height="1"></td>
    </tr>
    <tr>
      <td valign="top">
         <table width="525" border="0" cellpadding="0" cellspacing="0">
         <content:errors scope="session"/>
           <tr>
              <td><content:getAttribute beanName="layoutPageBean" attribute="body1">
<content:param>${changePasswordForm.userFullName}</content:param>
<content:param>${changePasswordForm.emailAddress}</content:param>
          		</content:getAttribute>
              </td>
          </tr>
         </table>
         <BR><br><br>

        <table cellSpacing=0 cellPadding=0 width=525 border=0>
        	<tbody>
              <tr align=middle>
                <td width=131>&nbsp;</td>
                <td width=131>
<input type="button" onclick="javascript:window.print()" name="abc" class="button100Lg" value="print"/>


                </td>
                <td width=132>
<input type="button" onclick="javascript:setButtonAndSubmit('finished');return false;" name="fin" class="button100Lg" value="finish"/>
<form:hidden path="button" />
                </td>
              </tr>
            </tbody>
         </table>
       </td>
       <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
       <td vAlign=top>&nbsp;</td>
	 </tr>
  </table>

</ps:form>





