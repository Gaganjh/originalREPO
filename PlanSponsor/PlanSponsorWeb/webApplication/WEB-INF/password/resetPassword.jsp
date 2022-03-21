<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ManagePasswordForm" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%-- Define a user info object --%>

<c:set var="myForm2" value="${USERINFO_KEY}" />


<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

  <ps:form cssClass="margin-bottom:0;"
           method="POST" modelAttribute="managePasswordForm" name="managePasswordForm"
           action="/do/password/managePassword/">

  			
<!-- <input type="hidden" name="action" value=""/> -->
<form:hidden path="userName" />	
<form:hidden path="firstName" />	
<form:hidden path="lastName" />	
<form:hidden path="email" />
<form:hidden path="fromPSContactTab" />	
<form:hidden path="fromTPAContactsTab" />	


        <table width="525" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td height="27"><b>Reset password confirmation</b></td>
          </tr>
          <tr>
            <td>
            <content:getAttribute beanName="layoutPageBean" attribute="body1">
              <content:param>
${myForm2.firstName}
${myForm2.lastName}
              </content:param>
              <content:param>
${myForm2.email}
			  </content:param>
            </content:getAttribute>
          </td>
          </tr>
        </table>
        <br>
        <table width="525" border="0" cellspacing="0" cellpadding="0">
          <tr align="center">
            <td width="131">&nbsp;</td>
            <td width="131">&nbsp;</td>
<td width="131"><input type="button" onclick="javascript:print(); return false;" name="action" class="button100Lg" value="print"/></td>
<td width="132"><input type="submit" class="button100Lg" name="action" value="<%= ManagePasswordForm.BUTTON_LABEL_FINISH %>"></td>
          </tr>
        </table>

  </ps:form>


