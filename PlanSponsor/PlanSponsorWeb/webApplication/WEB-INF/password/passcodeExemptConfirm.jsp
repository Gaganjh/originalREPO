<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ManagePasscodeExemptionForm" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="myForm2" value="${USERINFO_KEY}" />

  <ps:form cssClass="margin-bottom:0;"
           method="POST" modelAttribute="managePasscodeExemptionForm" name="managePasscodeExemptionForm"
           action="/do/passcode/managePasscodeExempt/">
             			
<!-- <input type="hidden" name="action"/>
<input type="hidden" name="userName"/>
<input type="hidden" name="firstName"/>
<input type="hidden" name="lastName"/>
<input type="hidden" name="email"/>
<input type="hidden" name="fromPSContactTab"/>
<input type="hidden" name="fromTPAContactsTab"/> -->

<form:hidden path="userName" />	
<form:hidden path="firstName" />	
<form:hidden path="lastName" />	
<form:hidden path="email" />	
<form:hidden path="fromPSContactTab" />	
<form:hidden path="fromTPAContactsTab" />


        <table width="525" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td>
            <content:getAttribute beanName="layoutPageBean" attribute="body1">
              <content:param>
<b>${e:forHtmlContent(myForm2.firstName)}</b>
              </content:param>
               <content:param>
<b>${e:forHtmlContent(myForm2.lastName)}</b>
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
<td><input type="submit" class="button150" name="action" value="<%= ManagePasscodeExemptionForm.BUTTON_LABEL_FINISH %>" >


            			 </td>
          </tr>
        </table>

  </ps:form>


