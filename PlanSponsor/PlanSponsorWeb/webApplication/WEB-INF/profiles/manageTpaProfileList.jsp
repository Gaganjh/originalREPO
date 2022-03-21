<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%> 
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.valueobject.ManageUsersReportData" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ManageUsersReportForm" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ManageUsersReportController" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AccessLevelHelper" %>
<%@ page import="com.manulife.pension.service.security.valueobject.ManageUsersReportData" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.security.valueobject.UserInfo" %>
<%-- Builds manage user Profiles  Table ------------------------------%>
<%@ page import="com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo"%>

<jsp:useBean class="java.lang.String"
		id="resultsMessageKey"
		scope="request"/>

<jsp:useBean class="java.lang.String"
		id="displayShowAllKey"
		scope="request"/>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	ManageUsersReportData theReport = (ManageUsersReportData)request.getAttribute(Constants.REPORT_BEAN);
	UserInfo loggedInUserInfo = (UserInfo)pageContext.getAttribute("loggedInUserInfo");
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>
<%

String FILTER_TPA_FIRM_NAME=ManageUsersReportData.FILTER_TPA_FIRM_NAME;
pageContext.setAttribute("FILTER_TPA_FIRM_NAME",FILTER_TPA_FIRM_NAME,PageContext.PAGE_SCOPE);

String FILTER_TPA_LAST_NAME=ManageUsersReportData.FILTER_TPA_LAST_NAME;
pageContext.setAttribute("FILTER_TPA_LAST_NAME",FILTER_TPA_LAST_NAME,PageContext.PAGE_SCOPE);

String SORT_FIELD_LAST_NAME=ManageUsersReportData.SORT_FIELD_LAST_NAME;
pageContext.setAttribute("SORT_FIELD_LAST_NAME",SORT_FIELD_LAST_NAME,PageContext.PAGE_SCOPE);

String SORT_FIELD_PROFILE_STATUS=ManageUsersReportData.SORT_FIELD_PROFILE_STATUS;
pageContext.setAttribute("SORT_FIELD_PROFILE_STATUS",SORT_FIELD_PROFILE_STATUS,PageContext.PAGE_SCOPE);

String SORT_FIELD_PASSWORD_STATUS=ManageUsersReportData.SORT_FIELD_PASSWORD_STATUS;
pageContext.setAttribute("SORT_FIELD_PASSWORD_STATUS",SORT_FIELD_PASSWORD_STATUS,PageContext.PAGE_SCOPE);

String FILTER_TPA_FIRM_ID=ManageUsersReportData.FILTER_TPA_FIRM_ID;
pageContext.setAttribute("FILTER_TPA_FIRM_ID",FILTER_TPA_FIRM_ID,PageContext.PAGE_SCOPE);
%>
<c:set var="userProfile" value="${userProfile}" scope="session"/>
<c:set var="theReport" value="${reportBean}" scope="page" />
<c:set var="loggedInUserInfo" value="${reportBean.userInfo}" scope="page" />

<logicext:if name="manageUserType" op="equal" value="internal">
	<logicext:then>
<c:set var="postAction" value="/profiles/manageInternalUsers/" scope="page"/>
<c:set var="onSubmitJs" value="submitform();return false;"  scope="page"/>
	</logicext:then>
	<logicext:elseif name="manageUserType" op="equal" value="external">
	
		<logicext:then>
<c:set var="postAction" value="/profiles/manageExternalUsers/" scope="page"/>
<c:set var="onSubmitJs" value="return false;" scope="page"/>
		</logicext:then>
	</logicext:elseif>

	<logicext:elseif name="manageUserType" op="equal" value="tpa">
		<logicext:then>
<c:set var="postAction" value="/do/profiles/manageTpaUsers/"  scope="page"/>
<c:set var="onSubmitJs" value="submitform();return false;"   scope="page"/>
		</logicext:then>
	</logicext:elseif>

</logicext:if>


<style>
.select_column { width: 1 }
.name_column { width: 100 }
.id_column { width: 80 }
.firm_name_column { width: 185 }
.manage_users_column { width: 50 }
.profile_status_column { width: 70 }
.password_status_column { width: 90 }
<c:set var="totalColspan" value="12" />
</style>

<script type="text/javascript">

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

<ps:form cssClass="margin-bottom:0;" method="POST" action="${postAction}" onsubmit="${onSubmitJs}" name="manageUsersReportForm" modelAttribute="manageUsersReportForm">
<table cellspacing=0 cellpadding=0 width=525 border=0>
  <tbody>
  <tr></tr></tbody></table>

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

<table width="525" border="0" cellpadding="0" cellspacing="0">
    <tr>
	<table width="525" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="select_column"><img src="/assets/unmanaged/images/s.gif" height="1"class="select_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="name_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="name_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td class="id_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="id_column"></td>
		<td class="firm_name_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="firm_name_column"></td>
        <td class="manage_users_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="manage_users_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="profile_status_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="profile_status_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td class="password_status_column"><img src="/assets/unmanaged/images/s.gif" height="1" class="password_status_column"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   </tr>
	<tr class="tablehead">
		<td colspan="5" class="tableheadTD1">
			<span class="tableheadTD">
			<b>
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
			</b>
			</span>
		</td>
		<td colspan="4" class="tableheadTD"><report:recordCounter report="theReport" label="Users"/></td>
		<td colspan="3" class="tableheadTD" align="right"><report:pageCounter report="theReport" formName="manageUsersReportForm"/></td>
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>

    <tr class="whiteBox">
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td colspan="11" align="left" style="padding-top: 3px; padding-bottom: 3px">
<form:radiobutton path="filter" value="tpaFirmId"/>
			TPA firm ID&nbsp;&nbsp;
<form:input path="filterValue" cssClass="inputField"/>
            &nbsp;&nbsp;<a href="javascript:submitform()">Search</a>
            <c:if test="${displayShowAllKey ==true}">
            &nbsp;&nbsp;<a href='/do/profiles/manageTpaUsers/?task=showAll'>Show all</a>
</c:if>
			<br/>
<form:radiobutton path="filter" value="tpaFirmName"/>
			TPA firm name
			<br/>

<form:radiobutton path="filter" value="tpaLastName"/>
			Last name
		</td>
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>

	<tr class="tablesubhead">
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="top"><strong>Select </strong></td>
	    <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="top"><b><report:sort field="sortByLastName"  formName="manageUsersReportForm" direction="asc">Name</report:sort></b></td>
	    <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  		<td  class="id_column" valign="top"><strong>TPA firm ID</strong></td>
		<td  class="firm_name_column" valign="top"><strong>TPA firm name</strong></td>
		<td  class="manage_users_column" valign="top"><strong>Manage users</strong></td>
	    <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="top"><b><report:sort field="sortByProfileStatus"  formName="manageUsersReportForm" direction="asc">Profile status</report:sort></b></td>
	    <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="top"><b><report:sort field="sortByPasswordStatus"  formName="manageUsersReportForm" direction="asc">Password status</report:sort></b></td>
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>

	<%-- End of manageProfileList Header -----------------------%>

	<%--   display a message if there are no users available for display --%>

<c:if test="${empty theReport.details}">
	<tr class="datacell1">
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td colspan = "11"  valign="middle">
${resultsMessageKey} <%-- scope="request" --%>
		</td>
  	    <td valign="top" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr class="whiteborder" >
</c:if>
	<%--   detail rows start here   --%>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="details">
<%UserInfo theItem=(UserInfo) (pageContext.getAttribute("loggedInUserInfo")); %>

	<%
	java.lang.String displayRole = null;
	com.manulife.pension.service.security.role.UserRole role = theItem.getRole();
	
        if (role == null || role instanceof com.manulife.pension.service.security.role.NoAccess) {
                displayRole = AccessLevelHelper.NO_ACCESS_STRING;
        } else {
		displayRole = role.getDisplayName();
		}

        Boolean showOperationButtons = true;
        boolean isInternalUser = userProfile.getRole().isInternalUser();

        if (!isInternalUser)
        {
			for (TPAFirmInfo firmInfo : theItem.getTpaFirmsAsCollection()) {
				if ( !firmInfo.getContractPermission().isManageTpaUsers() ) {
					showOperationButtons = true;
					break;
				}
			}
        }
        else
        {
        	showOperationButtons = true;
        }
		pageContext.setAttribute("showOperationButtons",showOperationButtons);
	%>
<c:set var="count" value="${details.index}"/> 
	<% int indexValue = Integer.parseInt(pageContext.getAttribute("count").toString()); 
        if (indexValue % 2 != 0) {%>
        <TR class="datacell1">
        <%} else {%>
        <TR class="datacell2">
        <%}%>
	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		<td valign=top width=37>
          <table width=25 border=0>
            <tbody>
              <tr>
 

<c:set var="viewLink" value="${layoutBean.getParam('viewLink')}"/> 
             <td>

                  <c:if test="${showOperationButtons == true}">
                  <a href="javascript://" onClick="theProfileId='${theItem.getProfileId()}'; return gotoUrl(this, '${viewLink}');">
                    <img height=12 alt=view src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>
                  </a>
                  </c:if>
                </td>
                <logicext:if name="theItem" property="webAccess" op="equal" value="Yes">
             	<logicext:then>
                <logicext:if name="theItem" property="profileStatus" op="notequal" value="Suspended">
                <logicext:then>            
                <td>
<c:set var="managePasswordLink" value="${layoutBean.getParam('managePasswordLink')}"/>
                  <c:if test="${showOperationButtons}">
                  <a href="javascript://" onClick="theProfileId='${theItem.getProfileId()}'; return gotoUrl(this, '${managePasswordLink}');">
                    <img src="/assets/unmanaged/images/manage_icon.gif" alt="manage password" width=12 height=12 border="0">
                  </a>
                  </c:if>
                </td>
                </logicext:then>
                </logicext:if>
                </logicext:then>
                </logicext:if>
              </tr>
              <tr>
                <logicext:if name="theItem" property="profileStatus" op="notequal" value="Suspended">
                <logicext:then>
                <td>
<c:set var="editLink" value="${layoutBean.getParam('editLink')}"/>
                  <c:if test="${showOperationButtons}">
                  <a href="javascript://" onClick="theProfileId='${theItem.getProfileId()}'; return gotoUrl(this, '${editLink}');">
                    <img height=12 alt=edit src="/assets/unmanaged/images/edit_icon.gif" width=12 border=0>
                  </a>
                  </c:if>
                </td>
                </logicext:then>
                </logicext:if>
                <td>
<c:set var="deleteLink" value="${layoutBean.getParam('deleteLink')}"/>
                  <ps:linkAccessible path="/do/profiles/deleteTpaProfile/">
	                <c:if test="${showOperationButtons}">
                    <a href="javascript://" onClick="theProfileId='${theItem.getProfileId()}'; return gotoUrl(this, '${deleteLink}');">
                      <img height=12 alt=delete src="/assets/unmanaged/images/delete_icon.gif" width=12 border=0>
                    </a>
                  </c:if>
	              </ps:linkAccessible>
                </td>
              <logicext:if name="theItem" property="webAccess" op="equal" value="Yes">
              <logicext:then>
              <logicext:if name="theItem" property="profileStatus" op="notequal" value="Suspended">
              <logicext:then>			 
              </tr>
              <tr>
                <td>
<c:set var="suspendLink" value="${layoutBean.getParam('suspendLink')}"/>
                  <c:if test="${showOperationButtons}">
                  <a href="javascript://" onClick="theProfileId='${theItem.getProfileId()}'; return gotoUrl(this, '${suspendLink}');">
                    <img src="/assets/unmanaged/images/suspend_icon.gif" alt="suspend access" width=12 height=12 border="0">
                  </a>
                  </c:if>
                </td>
              </logicext:then>
              <logicext:else>
                <td>
<c:set var="unsuspendLink" value="${layoutBean.getParam('unsuspendLink')}"/>
                  <c:if test="${showOperationButtons}">
                  <a href="javascript://" onClick="theProfileId='${theItem.getProfileId()}'; return gotoUrl(this, '${unsuspendLink}');">
                    <img src="/assets/unmanaged/images/unsuspend_icon.gif" alt="unsuspend access" width=12 height=12 border="0">
                  </a>
                  </c:if>
                </td>
              </logicext:else>
              </logicext:if>
              </logicext:then>
              </logicext:if>
              </tr>
            </tbody>
          </table>
        </td>
        <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	    <td valign="center"><render:name firstName='${theItem.firstName}' lastName='${theItem.getLastName()}' style="f" defaultValue=""/></td>
	    <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

				<td  colspan = "3"  valign="center">
				<table border="0" width="100%" cellpadding="2" cellspacing="1">

<c:forEach items="${theItem.tpaFirms}" var="tpaFirm">

				<tr valign="center">
<td class="id_column">${tpaFirm.id}</td>
<td class="firm_name_column">${tpaFirm.name}</td>
					<td class="manage_users_column">&nbsp;&nbsp;&nbsp;&nbsp;<c:if test="${tpaFirm.contractPermission.manageTpaUsers}">Yes</c:if>
					<c:if test="${!tpaFirm.contractPermission.manageTpaUsers}">No</c:if></td>
				</tr>

</c:forEach>
				</table>
				</td>

	    <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="center">
<div align="center">${theItem.profileStatus}</div>
	    </td>
	    <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td valign="center">
	    <div align="center"><ps:passwordState name="theItem" property="passwordState"/></div>
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
<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	</tr>
	<tr>
    	<td class="databorder" colspan="13"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
    <tr>
    	<td colspan="12" align="right"><report:pageCounter report="theReport"  formName="manageUsersReportForm" arrowColor="black"/></td>
    </tr>

</table>
</table>
</ps:form>


