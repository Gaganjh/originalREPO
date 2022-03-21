<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.SecurityManager" %>

<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- This jsp includes the following CMA content --%>
<%@page import="com.manulife.pension.ps.web.profiles.ManageUsersReportController"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo" %>
<%@ page import="com.manulife.pension.service.security.valueobject.UserInfo" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

TPAFirmInfo tpaFirm = (TPAFirmInfo)request.getAttribute(ManageUsersReportController.TPA_FIRM_INFO_KEY);
pageContext.setAttribute("tpaFirm",tpaFirm,PageContext.PAGE_SCOPE);

UserInfo userInfoBean =(UserInfo)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("userInfoBean",userInfoBean,PageContext.PAGE_SCOPE);

UserInfo userInfo =(UserInfo)request.getAttribute("userInfoBean.userInfo");
pageContext.setAttribute("userInfo",userInfo,PageContext.PAGE_SCOPE);

String showedit=(String)request.getAttribute(Constants.SHOW_EDIT_TPA_FIRM);
pageContext.setAttribute("showedit",showedit,PageContext.PAGE_SCOPE);
%>
<content:contentBean
    contentId="<%=ContentConstants.MANAGE_USER_PROFILES%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="Manage_User_Profiles"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_NO_USERS_SELECTED%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningNoUser"/>







<script  type="text/javascript">
var submitted=false;

function doSubmit(href) {
	if (!submitted) {
		submitted = true;
		window.location.href=href;
		return true;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
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

var theUserid = null;
var theClientUserAction = null;
function gotoUrl(theAnchor, theUrl) {
	if (!submitted) {
		submitted = true;
		if (theUserid != null) {
			theAnchor.href = theUrl+ '?userName=' + theUserid + (theClientUserAction == null ? "" : "&clientUserAction=" + theClientUserAction);
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
     <table width="525" border="0" cellspacing="0" cellpadding="0">
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
            		<ps:manageProfile name="userProfile" property="principal.role" value="<%=SecurityManager.MANAGE_EXTERNAL%>">
                    <ps:isExternal name="userInfo" property="role">
						<input type="button" class="button150" onclick="return doSubmit('/do/profiles/addUser/')" value="add profile" >
					</ps:isExternal>
	            	<ps:isNotExternal name="userInfo" property="role">
						<input type="button" class="button150" onclick="return doSubmit('/do/profiles/addUser/')" value="add external user" >
					</ps:isNotExternal>
					&nbsp;&nbsp;&nbsp;&nbsp;
					</ps:manageProfile>
                    <ps:isExternal name="userInfo" property="role">
						<input type="button" class="button150" onclick="return doSubmit('/do/profiles/editMyProfile/')" value="edit my profile" >
					</ps:isExternal>
	            	<ps:isNotExternal name="userInfo" property="role">
						<input type="button" class="button150" onclick="return doSubmit('/do/password/changePasswordInternal/')" value="change password" >
					</ps:isNotExternal>
 					</td>
                </tr>
            </table>
            <%-- profile manager section --%>
			<ps:permissionAccess permissions="BUSC">
	            <a href="javascript://" onclick="return doAnchorSubmit(this, '/do/tools/businessConversionStandardHeader/?from=manageUser');">Security role conversion</a> <br>
			</ps:permissionAccess>
			<ps:isAnyPilotAvailable>
           	<ps:permissionAccess permissions="PLMN">
				<a href="javascript://" onclick="return doAnchorSubmit(this, '/do/pilot/pilotContractStandardHeader/?from=manageUser');">Manage pilots</a> <br>
			</ps:permissionAccess>
			</ps:isAnyPilotAvailable>
      		<ps:permissionAccess permissions="EXMN">
           	<ps:isInternalUser name="userInfo" property="role">
           		<a href="javascript://" onclick="return doAnchorSubmit(this, '/do/profiles/manageTpaUsers/');">Manage TPA users</a><br>
      		</ps:isInternalUser>
            <ps:notPermissionAccess permissions="SELA">
       		<c:if test="${showedit == true}">
          		<a href="javascript://" onclick="return doAnchorSubmit(this, '/do/profiles/editTpaFirm/');">Manage TPA firm</a>
	            <br>
</c:if>
            </ps:notPermissionAccess>
            </ps:permissionAccess>

            <ps:permissionAccess permissions="EUVW,EXMN">
            <ps:notPermissionAccess permissions="SELA">
            	<br>
<c:set var="manageUserType" value="${external}" scope="request" />
	            <jsp:include page="/WEB-INF/profiles/manageProfileList.jsp" flush="true"/>
   			</ps:notPermissionAccess>
			</ps:permissionAccess>

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




