<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.valueobject.ManageUsersReportData" %>
<%@ page import="com.manulife.pension.ps.web.controller.SecurityManager" %>
<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page import="com.manulife.pension.service.security.valueobject.UserInfo" %>
        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%-- This jsp includes the following CMA content --%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

String SHOW_EDIT_TPA_FIRM=(String)request.getAttribute(Constants.SHOW_EDIT_TPA_FIRM);
pageContext.setAttribute("SHOW_EDIT_TPA_FIRM",SHOW_EDIT_TPA_FIRM,PageContext.PAGE_SCOPE);
%>
<%
Object message = session.getAttribute(Constants.DELETE_PAYROLL_EMAIL_MESSAGE);
if(message != null){
	%>
	<font color="#990000">
	<%
out.println(message);
	%></font>
	<%
session.removeAttribute(Constants.DELETE_PAYROLL_EMAIL_MESSAGE);
}%>
<content:contentBean
    contentId="<%=ContentConstants.MANAGE_USER_PROFILES%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Manage_User_Profiles"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_NO_USERS_SELECTED%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningNoUser"/>

<c:set var="userProfile" value="${userProfile}" scope="session"/> <%-- scope="session" type="com.manulife.pension.ps.web.controller.UserProfile" --%>

<c:set var="loggedInUserInfo" value="${reportBean.userInfo}" scope="page" />


<script type="text/javascript">
var submitted=false;

function doSubmit(href) {
	if (!submitted) {
		submitted = true;
		window.location.href=href;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return;
	 }
}

function doAnchorSubmit(theAnchor, theUrl) {
	if (!submitted) {
		submitted = true;
		theAnchor.href=theUrl;
		return true;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function doCsvSubmit(theAnchor, theUrl) {
	if (!submitted) {
		theAnchor.href=theUrl;
		return true;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

var theProfileId = null;
function gotoUrl(theAnchor, theUrl) {
	if (!submitted) {
		submitted = true;
		if (theProfileId != null) {
			theAnchor.href = theUrl+ '?profileId=' + theProfileId + '&fromTPAContactsTab=false';
			writeError("");
		} else {
			writeError("<content:getAttribute beanName="warningNoUser" attribute="text"/>");
		}
		return true;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function writeError(text) {
	var contentString;
	if (text.length > 0 )
		contentString = '<table id="psErrors"><tr><td class="redText"><ul><li>' +
			text + '</li></ul></td></tr></table>';
	else
		contentString = '';

	document.getElementById('errordivcs').innerHTML = contentString;

	if (text!="" && text!=" " &&text.length>0) {
		location.href='#TopOfPage';
	}
}

</script>

	    <a name="TopOfPage"></a>
        <table width="625" border="0" cellspacing="0" cellpadding="0">
		 	<tr>
        		<td><div id="errordivcs"><content:errors scope="session"/></div></td>
    		</tr>
			<tr><td>
            <%-- Start of manageMyProfile --------------------------------------------------------------%>

            <jsp:include page="/WEB-INF/profiles/myProfile.jsp" flush="true"/>

            <%-- End of manageMyProfile ------------------------------------------------------------------%>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td align="right">
                    	<input type="button" class="button150" onclick="doSubmit('/do/profiles/addTpaUser/')" value="add TPA user">
                    	&nbsp;&nbsp;&nbsp;&nbsp;
	                    <ps:isExternal name="userInfo" property="role">
							<input type="button" class="button150" onclick="doSubmit('/do/profiles/editMyProfile/')" value="edit my profile" size= "50">
						</ps:isExternal>
		            	<ps:isNotExternal name="userInfo" property="role">
							<input type="button" class="button150" onclick="doSubmit('/do/password/changePasswordInternal/')" value="change password" size= "50">
						</ps:isNotExternal>
                    </td>
                </tr>
            </table>

            <ps:isInternalUser name="userProfile" property="principal.role">
            <%--<a href="javascript://" onclick="return doAnchorSubmit(this, '/do/profiles/manageExternalUsersByInternal/');">Manage external users</a> --%>
    	        
	            <br>
	           <c:if test="${SHOW_EDIT_TPA_FIRM == true}">
		       		<a href="javascript://" onclick="return doAnchorSubmit(this, '/do/profiles/editTpaFirm/');">Manage TPA firm</a>
	            <br>
					<a href="javascript://" onclick="return doCsvSubmit(this, '/do/profiles/tpaUserManagementSummary/?task=download&ext=.csv');">TPA User Management Summary</a>
					<br/>
					<a href="javascript://" onclick="return doCsvSubmit(this, '/do/profiles/clientTpaFirmPermissions/?task=download&ext=.csv');">Client Authorization of TPA Firm Permissions</a>
					<br/>
</c:if>
            </ps:isInternalUser>
            <ps:isTpa name="userProfile" property="principal.role">
          		<a href="javascript://" onclick="return doCsvSubmit(this, '/do/profiles/tpaUserManagementSummary/?task=download&ext=.csv');">TPA User Management Summary</a>
	            <br/>
	            <a href="javascript://" onclick="return doCsvSubmit(this, '/do/profiles/clientTpaFirmPermissions/?task=download&ext=.csv');">Client Authorization of TPA Firm Permissions</a>
	            <br/>
            </ps:isTpa>

<c:set var="manageUserType" value="tpa" scope="request" />
			<jsp:include page="/WEB-INF/profiles/manageTpaProfileList.jsp" flush="true"/>

           <table width="180" border="0" cellspacing="0" cellpadding="0" class="box">
                <tr>
                    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td width="178"><img src="/assets/unmanaged/images/s.gif" width="178" height="1"></td>
                    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                </tr>
            </table>
            </td>
            </tr>
        </table>
