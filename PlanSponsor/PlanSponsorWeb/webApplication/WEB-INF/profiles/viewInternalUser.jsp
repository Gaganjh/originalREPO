<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.service.security.utility.SecurityConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ManagePasswordForm" %>


<%@ page import="com.manulife.pension.ps.web.profiles.ManageInternalUserHelper" %>
<%@ page import="com.manulife.pension.service.security.BDUserRoleType" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AccessLevelHelper" %>
<%@ page import="com.manulife.pension.service.security.role.RelationshipManager" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditUserForm" %>

<% 
AddEditUserForm theForm = (AddEditUserForm)request.getAttribute("addEditUserForm");
pageContext.setAttribute("theForm",theForm,PageContext.REQUEST_SCOPE);
String AcessLevelHelper=AccessLevelHelper.NO_ACCESS;
pageContext.setAttribute("AcessLevelHelper",AcessLevelHelper,PageContext.PAGE_SCOPE);

String RelationshipManager = com.manulife.pension.service.security.role.RelationshipManager.ID;
pageContext.setAttribute("RelationshipManager",RelationshipManager,PageContext.PAGE_SCOPE);



String BDConstantsUserRoleType=BDUserRoleType.RVP.getUserRoleCode();
pageContext.setAttribute("BDConstantsUserRoleType",BDConstantsUserRoleType,PageContext.PAGE_SCOPE);

String LOCKED_PASSWORD_STATUS=SecurityConstants.LOCKED_PASSWORD_STATUS;
pageContext.setAttribute("LOCKED_PASSWORD_STATUS",LOCKED_PASSWORD_STATUS,PageContext.PAGE_SCOPE);

String BUTTON_LABEL_RESET_PASSWORD=ManagePasswordForm.BUTTON_LABEL_RESET_PASSWORD;
pageContext.setAttribute("BUTTON_LABEL_RESET_PASSWORD",BUTTON_LABEL_RESET_PASSWORD,PageContext.PAGE_SCOPE);

String BUTTON_LABEL_UNLOCK_PASSWORD= ManagePasswordForm.BUTTON_LABEL_UNLOCK_PASSWORD;
pageContext.setAttribute("BUTTON_LABEL_UNLOCK_PASSWORD",BUTTON_LABEL_UNLOCK_PASSWORD,PageContext.PAGE_SCOPE);
%>

<script type="text/javascript" >
var submitted = false;

function doSubmit() {
	if (!submitted) {
		submitted = true;
		return true;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function doSubmitHref(href) {
	if (!submitted) {
		submitted = true;
		window.location.href = href;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

</script>

<div id="errordivcs"><content:errors scope="session" /></div>

<table width="100%" border="0" cellspacing="0" cellpadding="0">

<tr>
	<td width="550" valign="top">
      <table width=550 cellSpacing=0 cellPadding=0 border=0>
           <tbody>
              <tr>
                <td width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
                <td width=519><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
                <td width=4><img height=1 src="/assets/unmanaged/images/s.gif" width=4></td>
                <td width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
              </tr>

<%-- table header  --%>
              <tr class=tablehead>
                <td class=tableheadTD1 colSpan=4><strong>
                	<content:getAttribute id="layoutPageBean" attribute="body1Header"/>
                	</strong></td>
              </tr>

              <tr class=datacell1>
                <td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
                <td colspan=2>
                  <table class=datacell1 cellSpacing=0 cellPadding=0 width=100% border=0>
                    <tbody>
                    <tr class=datacell1>
                      <td width=200 class=datacell1 valign=top><strong>First name</strong></td>
                      <td width=319 class=datacell1 valign=top>
${theForm.firstName}</td></tr>
                    <tr class=datacell1>
                      <td valign=top><strong>Last name</strong></td>
                      <td valign=top>
${theForm.lastName}</td></tr>
                    <tr class=datacell1>
                      <td valign=top><strong>Primary Email </strong></td>
                      <td valign=top>
${theForm.email}</td></tr>
<c:if test="${not empty theForm.secondaryEmail}">
					<tr class=datacell1>
                      <td valign=top><strong>Secondary Email </strong></td>
                      <td valign=top>
${theForm.secondaryEmail}</td></tr>
</c:if>
					<tr class=datacell1>
                      <td valign=top><strong>Username</strong></td>
                      <td valign=top>
${theForm.userName}</td></tr>
                    <tr class=datacell1>
                      <td valign=top><strong>Employee number</strong></td>
                      <td valign=top>
${theForm.employeeNumber}</td></tr>
                    <tr class=datacell1>
                      <td valign=top><strong>Password status </strong></td>
                      <td valign=top>
					  	<ps:passwordState name="theForm" property="passwordState"/></td></tr>
                    <tr class=datacell1>
                      <td valign=top><strong>User status</strong></td>
                      <td valign=top>
${theForm.profileStatus}</td></tr>
                    <tr class=datacell1>
                      <td valign=top><strong>Plan sponsor access level</strong></td>
                      <td valign=top>
                      	<ps:formatUserRole name="theForm"
                					   	   property="planSponsorSiteRole" /></td></tr>
<c:if test="${addEditUserForm.planSponsorSiteRole ==RelationshipManager}">
   					<tr class=datacell1>
                      <td class=datacell1><strong>Associated RM Block of Business*</strong></td>
                      <td class=datacell1 vAlign=top>
${addEditUserForm.rmDisplayName}
					  </td>
					</tr>
</c:if>
<c:if test="${theForm.planSponsorSiteRole !=AcessLevelHelper}">
<c:if test="${theForm.planSponsorSiteRole !=PTC}">
                        <tr class=datacell1>
                          <td valign=top><strong>Produce Participant Fee Change Notice</strong></td>
<td valign=top>${theForm.accessIPIHypotheticalTool}</td></tr>
                        <tr class=datacell1>
                          <td valign=top><strong>Access to 408b2 Supplemental Disclosure Auto Regeneration</strong></td>
<td valign=top>${theForm.access408DisclosureRegen}</td></tr>
</c:if>
</c:if>
           			<tr class=datacell1>
                      <td valign=top><strong>Participant access level</strong></td>
                      <td valign=top>
					  	<%--${theForm.participantSiteRole}</td></tr> --%>
					  	<ps:formatUserRole name="theForm"
                					   	   property="participantSiteRole"
                					   	   site="ezk" /></td></tr>
           			<tr class=datacell1>
                      <td valign=top><strong>Broker Dealer access level</strong></td>
                      <td valign=top>
<c:set var="bdRoleCode" value="${addEditUserForm.brokerDealerSiteRole}" /> 
<% String role=pageContext.getAttribute("bdRoleCode").toString();
pageContext.setAttribute("role",role);
%>
					  	<%=ManageInternalUserHelper.getBDWRoleName(role) %> 
					  </td>
                    </tr>
<c:if test="${addEditUserForm.brokerDealerSiteRole =='BDConstantsUserRoleType'}">
   					<tr class=datacell1>
                      <td class=datacell1><strong>RVP</strong></td>
                      <td class=datacell1 vAlign=top>
${addEditUserForm.rvpDisplayName}
					  </td>
					</tr>
</c:if>
<c:if test="${addEditUserForm.brokerDealerSiteRole !=AcessLevelHelper}">
   					<tr class=datacell1>
                      <td class=datacell1><strong>License verified</strong></td>
                      <td class=datacell1 vAlign=top>
${addEditUserForm.bdLicenceVerified}
					  </td>
					</tr>
</c:if>
				    </tbody>

				    </tbody>
				  </table>
				</td>
                <td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
              </tr>
              <tr class=whiteborder>
                <td class=databorder><img height=4 src="/assets/unmanaged/images/s.gif" width=1></td>
                <td class=whiteborder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
                <td class=whiteborder colSpan=2 rowSpan=2>
					<img height=5 src="/assets/unmanaged/images/box_lr_corner.gif" width=5></td>
              </tr>
              <tr>
                <td class=databorder colSpan=2><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
			  </tr>
			</tbody>
	  </table><br>
	  <table cellSpacing=0 cellPadding=0 width=550 border=0>
       <tbody>
        <ps:form cssClass="margin-bottom:0;"
                 method="POST"
                 action="/do/password/manageInternalPassword/" name="managePasswordForm" modelAttribute="managePasswordForm">
    		<tr align=middle>

				<td width="25%">
					<div align="left"><input name="backButton" value="back"
						onClick="doSubmitHref('/do/profiles/manageUsers/')"
						class="button134" type="button"></div>
				</td>
<c:if test="${theForm.passwordState== LOCKED_PASSWORD_STATUS}">
           
              	<td width="25%">
<input type="submit" class="button134" onclick="return doSubmit();" name="action" value="${BUTTON_LABEL_UNLOCK_PASSWORD}"/> 

                </td>
          	</c:if>
			<c:if test="${theForm.passwordState!=LOCKED_PASSWORD_STATUS}">
            	<td width="25%">&nbsp;</td>
         	</c:if>
<%String userName = StringEscapeUtils.escapeJavaScript(theForm.getUserName()); 
pageContext.setAttribute("userName",userName);
%>

                <td width="25%">
                	<input type=button class="button134" value="edit this user"
    	 				onclick="doSubmitHref('/do/profiles/editInternalUser/?userName=${userName}')" >
                </td>
                <td width="25%">
<input type="submit" class="button134" onclick="return doSubmit();" name="action" value="${BUTTON_LABEL_RESET_PASSWORD}"/>

                </td>
             </tr>
<form:hidden path="userName" value="${theForm.userName}"/><%--  input - name="theForm" --%>
<form:hidden path="firstName" value="${theForm.firstName}"/><%--  input - name="theForm" --%>
<form:hidden path="lastName" value="${theForm.lastName}"/><%--  input - name="theForm" --%>
<form:hidden path="email" value="${theForm.email}"/><%--  input - name="theForm" --%>
<input type="hidden" name="secondaryEmail" /><%--  input - name="theForm" --%>
      	</ps:form>
       </tbody>
      </table>
    </td>
</tr>
</table>








