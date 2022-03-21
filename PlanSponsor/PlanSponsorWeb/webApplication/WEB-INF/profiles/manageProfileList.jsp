<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.valueobject.ManageUsersReportData" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ManageUsersReportForm" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ManageUsersReportController" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AccessLevelHelper" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import ="com.manulife.pension.service.security.valueobject.UserInfo" %> 
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%-- Builds manage user Profiles  Table ------------------------------%>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_ACTIVE_USERS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noActiveUsers" />

<jsp:useBean class="java.lang.String"
		id="resultsMessageKey"
		scope="request"/>

<jsp:useBean class="java.lang.String"
		id="displayShowAllKey"
		scope="request"/>



<%
ManageUsersReportData theReport = (ManageUsersReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserInfo loggedInUserInfo = (UserInfo)request.getAttribute(theReport.userInfo);
pageContext.setAttribute("loggedInUserInfo",loggedInUserInfo,PageContext.PAGE_SCOPE);

String filterEmployeeNumber=ManageUsersReportData.FILTER_EMPLOYEE_NUMBER;
String filterEmployeeLastName=ManageUsersReportData.FILTER_EMPLOYEE_LAST_NAME;
String filterTpaFirmId=ManageUsersReportData.FILTER_TPA_FIRM_ID ;
String filterTpaFirmName=ManageUsersReportData.FILTER_TPA_FIRM_NAME ;
String filterTpaLastName=ManageUsersReportData.FILTER_TPA_LAST_NAME;
pageContext.setAttribute("filterEmployeeNumber",filterEmployeeNumber,PageContext.PAGE_SCOPE);
pageContext.setAttribute("filterEmployeeLastName",filterEmployeeLastName,PageContext.PAGE_SCOPE);
pageContext.setAttribute("filterTpaFirmId",filterTpaFirmId,PageContext.PAGE_SCOPE);
pageContext.setAttribute("filterTpaFirmName",filterTpaFirmName,PageContext.PAGE_SCOPE);
pageContext.setAttribute("filterTpaLastName",filterTpaLastName,PageContext.PAGE_SCOPE);
%>




<script language=javascript type='text/javascript'>

function submitform() {
	var filters = document.getElementsByName("filter");
	var i;
	for (i = 0; i < filters.length; i++) {
		 if (filters[i].checked) {
			 setFilterFromInput(filters[i]);
		}
	}
	setFilterFromInput(document.getElementsByName("filterValue")[0]);

	doFilter();
}

</script>

<logicext:if name="manageUserType" op="equal" value="internal">
	<logicext:then>
<c:set var="postAction" value="/profiles/manageInternalUsers/" />
<c:set var="onSubmitJs" value="$submitform();return false;" />
	</logicext:then>
	<logicext:elseif name="manageUserType" op="equal" value="external">
		<logicext:then>
<c:set var="postAction" value="/profiles/manageExternalUsers/" />
<c:set var="onSubmitJs" value="return false;" />
		</logicext:then>
	</logicext:elseif>

	<logicext:elseif name="manageUserType" op="equal" value="tpa">
		<logicext:then>
<c:set var="postAction" value="/profiles/manageTpaUsers/" />
<c:set var="onSubmitJs" value="submitform();return false;" />
		</logicext:then>
	</logicext:elseif>
</logicext:if>


<jsp:useBean id="postAction" type="java.lang.String"/>
<jsp:useBean id="onSubmitJs" type="java.lang.String"/>

<style>
<logicext:if name="manageUserType" op="equal" value="internal">
	<logicext:or name="manageUserType" op="equal" value="external"/>
	<logicext:then>
.select_column { width: 1 }
.name_column { width: 120 }
.role_column { width: 120 }
.id_column { width: 78 }
.manage_users_column { width: 50 }
.web_access_column { width: 50 }
.profile_status_column { width: 70 }
.password_status_column { width: 70 }
<c:set var="totalColspan" value="${12}" />
	</logicext:then>
	<logicext:else>
.select_column { width: 20; }
.name_column { width: 100; }
.id_column { width: 80; }
.firm_name_column {width:185;}
.role_column { width: 90 }
.password_status_column { width:90; }
<c:set var="totalColspan" value="${12}" />
	</logicext:else>
</logicext:if>
</style>

<jsp:useBean id="totalColspan" type="java.lang.String"/>

<ps:form style="margin-bottom:0;" method="POST" action="<%= postAction %>" onsubmit="<%= onSubmitJs %>" name="manageUsersReportForm" modelAttribute="manageUsersReportForm">
<table cellspacing=0 cellpadding=0 width=525 border=0>
  <tbody>
  <tr></tr></tbody></table>

<ps:permissionAccess permissions="EXMN,INMN,TUMN">
<logicext:if name="manageUserType" op="equal" value="internal">
  <logicext:then>
    <table width="525" border="0">
      <tr>
        <td width="40"><strong>Legend:</strong></td>
        <td width="12"><img src="/assets/unmanaged/images/view_icon.gif" width="12" height="12"></td>
        <td width="25">View </td>
        <td width="12"><img src="/assets/unmanaged/images/edit_icon.gif" width="12" height="12"></td>
        <td width="25">Edit </td>
        <td width="12"><img src="/assets/unmanaged/images/delete_icon.gif" width="12" height="12"></td>
        <td width="25">Delete </td>
        <td width="12"><img src="/assets/unmanaged/images/manage_icon.gif" width="12" height="12"></td>
        <td width="115">Manage Password </td>
        <td width="12">&nbsp;</td>
        <td width="103">&nbsp;</td>
        <td width="12">&nbsp;</td>
        <td width="115">&nbsp;</td>
      </tr>
    </table>
  </logicext:then>
  <logicext:else>
    <table width=525 border=0>
      <tbody>
        <tr>
          <td width=40><strong>Legend:</strong></td>
          <td width=12><img height=12 src="/assets/unmanaged/images/view_icon.gif" width=12></td>
          <td width=25>View </td>
          <td width=12><img height=12 src="/assets/unmanaged/images/edit_icon.gif" width=12></td>
          <td width=25>Edit </td>
          <td width=12><img height=12 src="/assets/unmanaged/images/delete_icon.gif" width=12></td>
          <td width=25>Delete </td>
          <td width=12><img height=12 src="/assets/unmanaged/images/manage_icon.gif" width=12></td>
          <td width=25>Manage Password </td>
          <td width=12><img height=12 src="/assets/unmanaged/images/suspend_icon.gif" width=12></td>
          <td width=25>Suspend Access </td>
          <td width=12><img height=12 src="/assets/unmanaged/images/unsuspend_icon.gif" width=12></td>
          <td width=25>Unsuspend Access </td>
        </tr>
      </tbody>
    </table>
  </logicext:else>
</logicext:if>
</ps:permissionAccess>

<table width="525" border="0" cellpadding="0" cellspacing="0">
    <tr>
	<logicext:if name="manageUserType" op="equal" value="internal">
		<logicext:or name="manageUserType" op="equal" value="external"/>
	<logicext:then>
	<table width="525" border="0" cellpadding="0" cellspacing="0">
    <tr>
    	<ps:permissionAccess permissions="EXMN,INMN,TUMN">
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="select_column"><img src="/assets/unmanaged/images/s.gif" height="1"class="select_column"></td>
        </ps:permissionAccess>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="name_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="name_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="role_column"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <logicext:if name="manageUserType" op="equal" value="external">
        <logicext:then>
        <ps:isInternalUser name="loggedInUserInfo" property="role">
        <td class="id_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="id_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </ps:isInternalUser>
        </logicext:then>
        </logicext:if>
        <td class="manage_users_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="manage_users_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="web_access_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="web_access_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="profile_status_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="profile_status_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="password_status_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="password_status_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
 	</logicext:then>
	<logicext:else>
	<table width="700" border="0" cellpadding="0" cellspacing="0">
    <tr>
	    <ps:permissionAccess permissions="EXMN,INMN,TUMN">
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="select_column"><img src="/assets/unmanaged/images/s.gif" height="1"class="select_column"></td>
        </ps:permissionAccess>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="name_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="name_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td class="id_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="id_column"></td>
		<td class="firm_name_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="id_column"></td>
        <td class="role_column"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="password_status_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="password_status_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   	</logicext:else>
</logicext:if>
   </tr>
    <tr class="tablehead">
    <ps:permissionAccess permissions="EXMN,INMN,TUMN">
        <td colspan="8" class="tableheadTD1"><span class="tableheadTD"><b>
    </ps:permissionAccess>
    <ps:notPermissionAccess permissions="EXMN,INMN,TUMN">
        <td colspan="5" class="tableheadTD1"><span class="tableheadTD"><b>
    </ps:notPermissionAccess>
	<ps:isNotTpaum name ="userProfile" property ="role" >
		<content:getAttribute id="layoutPageBean" attribute="body2Header">
			<content:param>
${userProfile.currentContract.contractNumber}
			</content:param>
		</content:getAttribute>
	</ps:isNotTpaum>

	<ps:isTpaum name ="userProfile" property ="principal.role" >
	<content:getAttribute id="layoutPageBean" attribute="body2Header"/>
	</ps:isTpaum>
		</b></span></td>
		<logicext:if name="manageUserType" op="equal" value="external">
        <logicext:then>
        <ps:isInternalUser name="loggedInUserInfo" property="role">
		<td colspan="4" class="tableheadTD"><report:recordCounter report="theReport" label="Users"/></td>
		<td colspan="4" class="tableheadTD" align="right"><report:pageCounter report="theReport" formName="manageUsersReportForm"/></td>
    	</ps:isInternalUser>
    	<ps:isExternal name="loggedInUserInfo" property="role">
		<td colspan="4" class="tableheadTD"><report:recordCounter report="theReport" label="Users"/></td>
		<td colspan="2" class="tableheadTD" align="right"><report:pageCounter report="theReport" formName="manageUsersReportForm"/></td>
    	</ps:isExternal>
    	</logicext:then>
    	<logicext:else>
		<td colspan="4" class="tableheadTD"><report:recordCounter report="theReport" label="Users"/></td>
		<td colspan="2" class="tableheadTD" align="right"><report:pageCounter report="theReport" formName="manageUsersReportForm"/></td>
    	</logicext:else>
    	</logicext:if>
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>

	<logicext:if name="manageUserType" op="equal" value="internal">
		<logicext:then>
    <tr class="whiteBox">
   	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td colspan="12" align="left" style="padding-top: 3px; padding-bottom: 3px">
<form:radiobutton path="filter" value="${filterEmployeeNumber}"/>
			Employee number&nbsp;&nbsp;
<form:input path="filterValue" cssClass="inputField"/>
	        &nbsp;&nbsp;<a href="javascript:submitform()">Search</a>
            <c:if test="${requestScope.displayShowAllKey==true}">
	        &nbsp;&nbsp;<a href='/do/profiles/manageInternalUsers/?task=showAll'>Show all</a>
</c:if>
			<br/>
<form:radiobutton path="filter" value="${filterEmployeeLastName}"/>
 			Name
		</td>
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
		</logicext:then>
    	<logicext:elseif name="manageUserType" op="equal" value="tpa">
			<logicext:then>
    <tr class="whiteBox">
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td colspan="12" align="left" style="padding-top: 3px; padding-bottom: 3px">
<form:radiobutton path="filter" value="${filterTpaFirmId}"/>
			TPA firm ID&nbsp;&nbsp;
<form:input path="filterValue" cssClass="inputField"/>
            &nbsp;&nbsp;<a href="javascript:submitform()">Search</a>
            <c:if test="${requestScope.displayShowAllKey==true}">
            &nbsp;&nbsp;<a href='/do/profiles/manageTpaUsers/?task=showAll'>Show all</a>
</c:if>
			<br/>
<form:radiobutton path="filter" value="${filterTpaFirmName}"/>
			TPA firm name
			<br/>

<form:radiobutton path="filter" value="${filterTpaLastName}"/>
			Last name
		</td>
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	        </logicext:then>
        </logicext:elseif>
	</logicext:if>

	<tr class="tablesubhead">
		<ps:permissionAccess permissions="EXMN,INMN,TUMN">
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="top"><strong>Select </strong></td>
	    <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    </ps:permissionAccess>
	    <ps:notPermissionAccess permissions="EXMN,INMN,TUMN">
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    </ps:notPermissionAccess>
	    <td valign="top"><b><report:sort field="<%=ManageUsersReportData.SORT_FIELD_LAST_NAME%>" direction="asc" formName="manageUsersReportForm">Name</report:sort></b></td>
	    <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<logicext:if name="manageUserType" op="equal" value="internal">
	    	<logicext:then>
			    <td valign="top" nowrap><b><report:sort field="<%=ManageUsersReportData.SORT_FIELD_USER_ROLE%>" direction="asc" formName="manageUsersReportForm">Role</report:sort></b></td>
	    		<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td valign="top"><b><report:sort field="<%=ManageUsersReportData.SORT_FIELD_EMPLOYEE_NUMBER%>" direction="asc" formName="manageUsersReportForm">Employee number</report:sort></b></td>
			</logicext:then>
			<logicext:elseif name="manageUserType" op="equal" value="external">
				<logicext:then>
			    <td valign="top" nowrap><b><report:sort field="<%=ManageUsersReportData.SORT_FIELD_USER_ROLE%>" direction="asc" formName="manageUsersReportForm">Role</report:sort></b></td>
			    <logicext:if name="manageUserType" op="equal" value="external">
                <logicext:then>
                <ps:isInternalUser name="loggedInUserInfo" property="role">
	    		<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    		<td valign="top"><b><report:sort field="<%=ManageUsersReportData.SORT_FIELD_USERNAME%>" direction="asc" formName="manageUsersReportForm">Username</report:sort></b></td>
	    		</ps:isInternalUser>
	    		</logicext:then>
	    		</logicext:if>
				</logicext:then>
	    	</logicext:elseif>
			<logicext:elseif name="manageUserType" op="equal" value="tpa">
				<logicext:then>
	    		<td  class="id_column" valign="top"><strong>TPA firm ID</strong></td>
				<td  class="firm_name_column" valign="top"><strong>TPA firm name</strong></td>
				<td  class="role_column" valign="top"><strong>Role</strong></td>
				</logicext:then>
			</logicext:elseif>
		</logicext:if>
	    <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="top"><b><report:sort field="<%=ManageUsersReportData.SORT_FIELD_MANAGE_USERS%>" direction="asc" formName="manageUsersReportForm">Manage users</report:sort></b></td>
	    <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="top"><b><report:sort field="<%=ManageUsersReportData.SORT_FIELD_WEB_ACCESS%>" direction="asc" formName="manageUsersReportForm">Web access</report:sort></b></td>
	    <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="top"><b><report:sort field="<%=ManageUsersReportData.SORT_FIELD_PROFILE_STATUS%>" direction="asc" formName="manageUsersReportForm">Profile status</report:sort></b></td>
	    <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="top"><b><report:sort field="<%=ManageUsersReportData.SORT_FIELD_PASSWORD_STATUS%>" direction="asc" formName="manageUsersReportForm">Password status</report:sort></b></td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>

	<%-- End of manageProfileList Header -----------------------%>

	<%--   display a message if there are no users available for display --%>

<c:if test="${empty theReport.details}">
	<tr class="datacell1">
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td colspan = "13"  valign="middle">
			<c:if test="${empty resultsMessageKey}">
				<content:getAttribute id="noActiveUsers" attribute="text"/>
</c:if>
			<c:if test="${not empty resultsMessageKey}">
${resultsMessageKey} <%-- scope="request" --%>
</c:if>
		</td>
  	    <td valign="top" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr class="whiteborder" >
</c:if>
	<%--   detail rows start here   --%>
<c:if test="${not empty theReport.details}">

<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >




	<%
	java.lang.String displayRole = null;
	com.manulife.pension.service.security.role.UserRole role = theItem.getRole();
        if (role == null || role instanceof com.manulife.pension.service.security.role.NoAccess) {
                displayRole = AccessLevelHelper.NO_ACCESS_STRING;
        } else {
			displayRole = role.getDisplayName();
			if (role instanceof com.manulife.pension.service.security.role.IntermediaryContact && theItem.getRoleType() != null) {
				displayRole += " (" + theItem.getRoleType().getDisplayName() + ")";
			}
		}
	%>

	<% if (theIndex.intValue() % 2 == 0) { %>
	<tr class="datacell1">
	    <% } else { %>
	<tr class="datacell2">
	    <% } %>

	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		<ps:permissionAccess permissions="EXMN,INMN,TUMN">
		<td valign=top width=37>
          <table width=25 border=0>
            <tbody>
            <ps:isExternal name="loggedInUserInfo" property="role">
			<ps:manageProfile name="theItem" property="role">

			<logicext:if name="theItem" property="userName" op="notEqual" value="<%=userProfile.getName()%>">
			<logicext:then>
			<%
			 if ( !theItem.getRole().toString().equals(com.manulife.pension.service.security.role.IntermediaryContact.ID) ||
					 (!loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(com.manulife.pension.service.security.role.AdministrativeContact.stringID) &&
							 !loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(com.manulife.pension.service.security.role.AuthorizedSignor.ID))) {
			 %>
              <tr>
                <td>
<c:set var="viewLink" value="${layoutBean.param(viewLink)}"/>
                  <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; return gotoUrl(this, '<%=viewLink%>');">
                    <img height=12 alt=view src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>
                  </a>
                </td>
                <td>
                <logicext:if name="theItem" property="webAccess" op="equal" value="Yes">
                <logicext:then>
                <logicext:if name="theItem" property="profileStatus" op="notequal" value="Suspended">
                <logicext:then>
<c:set var="managePasswordLink" value="${layoutBean.param(managePasswordLink)}"/>
                  <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; return gotoUrl(this, '<%=managePasswordLink%>');">
                    <img src="/assets/unmanaged/images/manage_icon.gif" alt="manage password" width=12 height=12 border="0">
                  </a>
                </logicext:then>
                </logicext:if>
                </logicext:then>
                </logicext:if>
                </td>
              </tr>
              <tr>
                <logicext:if name="theItem" property="profileStatus" op="notequal" value="Suspended">
                <logicext:then>
                <td>
<c:set var="editLink" value="${layoutBean.param(editLink)}"/>
                  <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; theClientUserAction='editUser'; return gotoUrl(this, '<%=editLink%>');">
                    <img height=12 alt=edit src="/assets/unmanaged/images/edit_icon.gif" width=12 border=0>
                  </a>
                </td>
                </logicext:then>
                </logicext:if>
                <td>
<c:set var="deleteLink" value="${layoutBean.param(deleteLink)}"/>
                  <ps:linkAccessible path="/do/profiles/deleteProfile/">
                    <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; return gotoUrl(this, '<%=deleteLink%>');">
                      <img height=12 alt=delete src="/assets/unmanaged/images/delete_icon.gif" width=12 border=0>
                    </a>
	              </ps:linkAccessible>
                </td>
              <logicext:if name="theItem" property="webAccess" op="equal" value="Yes">
              <logicext:then>
              <logicext:if name="theItem" property="profileStatus" op="notequal" value="Suspended">
              <logicext:then>
              </tr>
              <tr>
                <td>
<c:set var="suspendLink" value="${layoutBean.param(suspendLink)}"/>
                  <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; return gotoUrl(this, '<%=suspendLink%>');">
                    <img src="/assets/unmanaged/images/suspend_icon.gif" alt="suspend access" width=12 height=12 border="0">
                  </a>
                </td>
              </logicext:then>
              <logicext:else>
                <td>
<c:set var="unsuspendLink" value="${layoutBean.param(unsuspendLink)}"/>
                  <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; return gotoUrl(this, '<%=unsuspendLink%>');">
                    <img src="/assets/unmanaged/images/unsuspend_icon.gif" alt="unsuspend access" width=12 height=12 border="0">
                  </a>
                </td>
              </logicext:else>
              </logicext:if>
              </logicext:then>
              </logicext:if>
              </tr>
             <% } %>
			</logicext:then>
			</logicext:if>
            </ps:manageProfile>
            </ps:isExternal>
            <ps:isInternalUser name="loggedInUserInfo" property="role">
			  <tr>
                <td>
<c:set var="viewLink" value="${layoutBean.param(viewLink)}"/>
                  <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; return gotoUrl(this, '<%=viewLink%>');">
                    <img height=12 alt=view src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>
                  </a>
                </td>
                <td>
                <logicext:if name="theItem" property="webAccess" op="equal" value="Yes">
                <logicext:then>
                <logicext:if name="theItem" property="profileStatus" op="notequal" value="Suspended">
                <logicext:then>
<c:set var="managePasswordLink" value="${layoutBean.param(managePasswordLink)}"/>
                  <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; return gotoUrl(this, '<%=managePasswordLink%>');">
                    <img src="/assets/unmanaged/images/manage_icon.gif" alt="manage password" width=12 height=12 border="0">
                  </a>
                </logicext:then>
                </logicext:if>
                </logicext:then>
                </logicext:if>
                </td>
              </tr>
              <tr>
                <logicext:if name="theItem" property="profileStatus" op="notequal" value="Suspended">
                <logicext:then>
                <td>
<c:set var="editLink" value="${layoutBean.param(editLink)}"/>
                  <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; theClientUserAction='editUser'; return gotoUrl(this, '<%=editLink%>');">
                    <img height=12 alt=edit src="/assets/unmanaged/images/edit_icon.gif" width=12 border=0>
                  </a>
                </td>
                </logicext:then>
                </logicext:if>
                <td>
                <ps:manageProfile name="theItem" property="role">
<c:set var="deleteLink" value="${layoutBean.param(deleteLink)}"/>
                  <ps:linkAccessible path="/do/profiles/deleteProfile/">
                  	<c:if test="${theReport.deleteMap[theItem.profileId] == true}">
                    <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; return gotoUrl(this, '<%=deleteLink%>');">
                      <img height=12 alt=delete src="/assets/unmanaged/images/delete_icon.gif" width=12 border=0>
                    </a>
                    </c:if>
	              </ps:linkAccessible>
	              </ps:manageProfile>
                </td>
              <logicext:if name="theItem" property="webAccess" op="equal" value="Yes">
              <logicext:then>
              <logicext:if name="theItem" property="profileStatus" op="notequal" value="Suspended">
              <logicext:then>
              </tr>
              <tr>
                <td>
<c:set var="suspendLink" value="${layoutBean.param(suspendLink)}"/>
                  <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; return gotoUrl(this, '<%=suspendLink%>');">
                    <img src="/assets/unmanaged/images/suspend_icon.gif" alt="suspend access" width=12 height=12 border="0">
                  </a>
                </td>
              </logicext:then>
              <logicext:else>
                <td>
<c:set var="unsuspendLink" value="${layoutBean.param(unsuspendLink)}"/>
                  <a href="javascript://" onClick="theUserid='<%=StringEscapeUtils.escapeJavaScript(theItem.getUserName())%>'; return gotoUrl(this, '<%=unsuspendLink%>');">
                    <img src="/assets/unmanaged/images/unsuspend_icon.gif" alt="unsuspend access" width=12 height=12 border="0">
                  </a>
                </td>
              </logicext:else>
              </logicext:if>
              </logicext:then>
              </logicext:if>
              </tr>
    		</ps:isInternalUser>
            </tbody>
          </table>
        </td>
        <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</ps:permissionAccess>

	    <td valign="center"><render:name firstName="<%=theItem.getFirstName()%>" lastName="<%=theItem.getLastName()%>" style="f" defaultValue=""/></td>
	    <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		<logicext:if name="manageUserType" op="equal" value="internal">
	    	<logicext:then>
			    <td valign="center"><%=displayRole%></td>
	    		<td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td valign="center">${theItem.employeeNumber}</td>
			</logicext:then>
			<logicext:elseif name="manageUserType" op="equal" value="external">
				<logicext:then>
			    <td valign="center"><%=displayRole%></td>
                <logicext:if name="manageUserType" op="equal" value="external">
                <logicext:then>
                <ps:isInternalUser name="loggedInUserInfo" property="role">
	    		<td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td valign="center">${theItem.userName}</td>
                </ps:isInternalUser>
                </logicext:then>
                </logicext:if>
				</logicext:then>
			</logicext:elseif>
			<logicext:elseif name="manageUserType" op="equal" value="tpa">
				<logicext:then>
				<td  colspan = "3"  valign="center">
				<table border="0" width="100%" cellpadding="2" cellspacing="1">

<c:forEach items="${theItem.tpaFirms}" var="tpaFirm">

				<tr  valign="center">
<td class="id_column">${tpaFirm.id}</td>
<td class="firn_name_column">${tpaFirm.name}</td>
					<td class="role_column"><ps:tpaFirmAcess name="tpaFirm" property="contractPermission.role"/></td>
				</tr>

</c:forEach>
				</table>
				</td>
				</logicext:then>
			</logicext:elseif>
		</logicext:if>
	    <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="center">
<div align="center">${theItem.externalUserManager}</div>
	    </td>
	    <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="center">
<div align="center">${theItem.webAccess}</div>
	    </td>
	    <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="center">
	    <div align="center">
${theItem.profileStatus}
        </div>
	    </td>
	    <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="center">
 	    <div align="center">
			<ps:passwordState name="theItem" property="passwordState"/>
 	    </div>
	    </td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
</c:forEach>

	<% if (theReport.getDetails().size() % 2 == 1) { %>
	<tr class="whiteborder" />
	<% } else { %>
	<tr class="beigeborder" />
	<% } %>
</c:if>

	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <logicext:if name="manageUserType" op="equal" value="external">
        <logicext:then>
        <ps:isInternalUser name="loggedInUserInfo" property="role">
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </ps:isInternalUser>
        </logicext:then>
        </logicext:if>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td  class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr>

	    <logicext:if name="manageUserType" op="equal" value="external">
        <logicext:then>
        <ps:isInternalUser name="loggedInUserInfo" property="role">
    	<td class="databorder" colspan="17"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	</ps:isInternalUser>
    	<ps:isExternal name="loggedInUserInfo" property="role">
    	<td class="databorder" colspan="15"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	</ps:isExternal>
    	</logicext:then>
    	<logicext:else>
    	<td class="databorder" colspan="15"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	</logicext:else>
    	</logicext:if>
	</tr>
    <tr>
        <logicext:if name="manageUserType" op="equal" value="external">
        <logicext:then>
        <ps:isInternalUser name="loggedInUserInfo" property="role">
    	<td colspan="16" align="right"><report:pageCounter report="theReport" arrowColor="black" formName="manageUsersReportForm"/></td>
    	</ps:isInternalUser>
    	<ps:isExternal name="loggedInUserInfo" property="role">
    	<td colspan="14" align="right"><report:pageCounter report="theReport" arrowColor="black" formName="manageUsersReportForm"/></td>
    	</ps:isExternal>
    	</logicext:then>
    	<logicext:else>
    	<td colspan="14" align="right"><report:pageCounter report="theReport" arrowColor="black" formName="manageUsersReportForm"/></td>
    	</logicext:else>
    	</logicext:if>
    </tr>


</table>

</ps:form>


