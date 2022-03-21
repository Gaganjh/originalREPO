<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.valueobject.ManageUsersReportData" %>
<%@ page import="com.manulife.pension.ps.web.controller.SecurityManager" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AccessLevelHelper" %>


<%@page import="com.manulife.pension.service.security.role.Trustee"%>
<%@page import="com.manulife.pension.service.security.role.PayrollAdministrator"%>
<%@page import="com.manulife.pension.service.security.role.AdministrativeContact"%>
<%@page import="com.manulife.pension.service.security.role.AuthorizedSignor"%>
<%@page import="com.manulife.pension.service.security.role.PlanSponsorUser"%>


<%@page import="com.manulife.pension.service.security.role.UserRole"%>
<%@page import="com.manulife.pension.service.security.role.NoAccess"%>
<%@page import="com.manulife.pension.service.security.role.IntermediaryContact"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@page import="com.manulife.pension.service.security.valueobject.UserInfo" %>
<%@page import ="com.manulife.pension.service.security.valueobject.ContractPermission" %>
<%@page import ="java.util.Collection" %>
<%@page import ="java.lang.String" %>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_ACTIVE_USERS%>" 
			type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noActiveUsers" />

<content:contentBean contentId="<%=ContentConstants.MESSAGE_CUSTOMIZED_PERMISSIONS%>" 
			type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="customizedPermissionsMsg" />
			
<content:contentBean contentId="<%=ContentConstants.RIA_338_DESIGNATION_IND%>" 
			type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="des338Ind" />
			
<jsp:useBean class="java.lang.String" id="resultsMessageKey" scope="request"/>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<c:set var="contractAddressList" value="${contractAddresses}" scope="page" />


<c:set var="comentsUpdatedByUser" value="${lastUpdatedByUser}" scope="page" />

			
<c:set var="contractComment" value="${contractComment}" scope="page" />

		          
<c:set var="theReport" value="${reportBean}" scope="page" />


<c:set var="businessConverted" value="${userProfile.currentContract.businessConverted}"/>


<c:set var="loggedInUserInfo" value="${reportBean.userInfo}" scope="page" />
<%UserInfo loggedInUserInfo = (UserInfo)pageContext.getAttribute("loggedInUserInfo");%>

<style type="text/css">
* html div#profileCommentsDIV {
	height: expression( this.scrollHeight > 18 ? "41px" : "auto" );
}
div#profileCommentsDIV {
	width:550px;
	min-height:10px;
/*	height: auto !important; */
	max-height: 41px;
	overflow:auto;
}

</style>
<script language=javascript type='text/javascript'>
	var submitted = false;
	
	var theProfileId = null;
	var theClientUserAction = null;
	var maxCharForComments = 500;
	function gotoUrl(theAnchor, theUrl) {
		if(!discardChanges()){
			return false;	
		}
		if (!submitted) {
			submitted = true;
			if (theProfileId != null) {
				theAnchor.href = theUrl+ '?profileId=' + theProfileId + (theClientUserAction == null ? "" : "&clientUserAction=" + theClientUserAction) + '&fromPSContactTab=true';
			} 
			
			return true;
		 } else {
			 window.status = "Transaction already in progress.  Please wait.";
			 return false;
		 }
	}
	
  
	function setMaxLength(Object, MaxLen)
	{
		var textVal = Object.value;
		var newLineCount = textVal.replace(/[^\n]/g, '').length;
		MaxLen -= newLineCount;
	    if(Object.value.length >= MaxLen) {
	 	 	Object.value = Object.value.substring(0, MaxLen);
	    }	
	}
	
	function doSubmit(href) {
		if(!discardChanges()){
			return false;	
		}
		
		if (!submitted) {
			submitted = true;
			window.location.href=href;
			return true;
		 } else {
			 window.status = "Transaction already in progress.  Please wait.";
			 return false;
		 }
	}
		
	function doDeleteStagingContact(theAnchor, contactId) {
		if (!submitted) {
			var actionStatus = confirm("Selected contact will be permanently removed,  Continue?");
			if(actionStatus == true) {
				submitted = true;
				var linkObj = document.getElementById("removeStaggingLink" + contactId);
				linkObj.href = "/do/contacts/planSponsor/?task=deleteStagingContact&contactId=" + contactId;
				return true;
			} else {
				return false;
			}
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}
	
	function doAddContact(theAnchor,  contactId) {
		if (!submitted) {
			submitted = true;
			var linkObj = document.getElementById("addStaggingLink" + contactId);
			linkObj.href = "/do/profiles/addUser/?contactId=" + contactId;
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}
	
	function doSaveComment() {
		if (!submitted) {
			submitted = true;
			setMaxLength(document.planSponsorContactsReportForm.contractComments, maxCharForComments);
			document.forms['planSponsorContactsReportForm'].task.value = "updateContractComments"; 
			document.forms['planSponsorContactsReportForm'].submit();
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}
	
	function doEditComments() {
		if (!submitted) {
				submitted = true;
				document.forms['planSponsorContactsReportForm'].task.value = "editContractComments"; 
				document.forms['planSponsorContactsReportForm'].submit();
				return true;
			} else {
				window.status = "Transaction already in progress.  Please wait.";
				return false;
			}
	}
	
	function doEditCommentsCancel() {

		if(!discardChanges()){
			return false;	
		}
		
		if (!submitted) {
				submitted = true;
				document.forms['planSponsorContactsReportForm'].task.value = "cancelContractComments"; 
				document.forms['planSponsorContactsReportForm'].submit();
				return true;
			} else {
				window.status = "Transaction already in progress.  Please wait.";
				return false;
			}
	}
	
	// Register the event.
	if (window.addEventListener) {	
		window.addEventListener('load', protectLinksFromCancel, false);
	} else if (window.attachEvent) {	
		window.attachEvent('onload', protectLinksFromCancel);
	}	
	
	var confirmMessage = "All Changes made on this page will be lost. Please click Ok to continue."
	var  tabName = "contactsPS";
	registerTrackChangesFunction(isContractCommentsChanged, tabName, confirmMessage);
		
	function isContractCommentsChanged(){
		return document.forms['planSponsorContactsReportForm'].task.value == "editContractComments";
	}

</script>
<div id="errordivcs"><content:errors scope="session"/></div>

<ps:form cssClass="margin-bottom:0;" method="POST" modelAttribute="planSponsorContactsReportForm" name="planSponsorContactsReportForm" action="/do/contacts/planSponsor/">
<%--  input - name="planSponsorContactsReportForm" --%>
<form:hidden path="task" />

<%-- Setting the action column flag --%>
<c:set var="isActionColumnRequired" value="false"/>
<c:set var="colspan_display" value="13"/>
<ps:isNotTpa name="userInfo" property="role">
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
	<c:set var="riaDesignation338Ind" value="${theItem.riaDesignation338Ind}"/>


			<logicext:if name="theItem" property="userName" op="notEqual" value="<%=userProfile.getName()%>">
			<logicext:then>
				<c:choose>
					<c:when test = "${businessConverted eq false}">
						<ps:isInternalUser name="userProfile" property="role">
							<c:if test="${userProfile.role.roleId ne 'PRM' or userProfile.role.roleId ne 'BIU'}">
								<ps:notPermissionAccess permissions="INMN">
									<c:set var="isActionColumnRequired" value="true"/>	
								</ps:notPermissionAccess>
							</c:if>
						</ps:isInternalUser>
						<ps:isExternal name="loggedInUserInfo" property="role">
							<ps:permissionAccess permissions="EXMN">
								<c:if test="${!ps:hasPermission(theItem.role, 'EXMN')}">
										<c:set var="isActionColumnRequired" value="true"/>
								</c:if>
							</ps:permissionAccess>
						</ps:isExternal>							
					</c:when>
					<c:otherwise>
						<%-- Logged in user is Internal user --%>
						<ps:isInternalUser name="loggedInUserInfo" property="role">
							<ps:permissionAccess permissions="EXMN,EXTA">
								<c:set var="isActionColumnRequired" value="true"/>										
							</ps:permissionAccess>
							<c:if test="${userProfile.role.roleId eq 'PLC'}">
								<c:set var="isActionColumnRequired" value="true"/>	
							</c:if>				
						</ps:isInternalUser>
						<ps:isExternal name="loggedInUserInfo" property="role">
							<ps:permissionAccess permissions="EXMN">
								<%								
								UserInfo theItem = (UserInfo)pageContext.getAttribute("theItem");
								 if ( loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(Trustee.ID) 
										 || ( ((loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(AuthorizedSignor.ID)) || (loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(AdministrativeContact.stringID)) ) 
										 	&& ((theItem.getRole().toString().equals(AdministrativeContact.stringID))
										 	|| (theItem.getRole().toString().equals(PayrollAdministrator.ID))) )
									 ) {
								 %>
									<c:set var="isActionColumnRequired" value="true"/>				 
								 <% }%>	
							</ps:permissionAccess>
						</ps:isExternal>
					</c:otherwise>
				</c:choose>
			</logicext:then>
			</logicext:if>			
</c:forEach>
</c:if>
</ps:isNotTpa>
<c:if test="${not empty param.printFriendly }" >
	<c:set var="colspan_display" value="11"/>
</c:if>
<c:if test="${!isActionColumnRequired}">
	<c:set var="colspan_display" value="11"/>
</c:if>

<ps:isInternalUser name="userProfile" property="role">
<content:getAttribute id="customizedPermissionsMsg" attribute="text"/><br>
</ps:isInternalUser>

<ps:isInternalUser name="userProfile" property="role">
	<c:if test="${riaDesignation338Ind}">
		<content:getAttribute id="des338Ind" attribute="text"/><br>
	</c:if>
</ps:isInternalUser>

<DIV id="errordivcs"><content:errors scope="session"/></DIV><br>
<%-- TAB section --%>
  <jsp:include page="ContactsTab.jsp" flush="true">
 	<jsp:param value="2" name="tabValue"/>
  	<jsp:param value="${tpaFirmAccessForContract}" name="tpaFirmAccessForContract"/>
  </jsp:include>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="beigeborder2">
   	<table width="100%" border="0" cellspacing="0" cellpadding="0">    		
			<%-- Comments section --%>
			<ps:isInternalUser name="userProfile" property="principal.role">
				<tr class="datacell1">
					<td colspan="${colspan_display}" >
						<logicext:if name="planSponsorContactsReportForm" property="task" op="equal" value="editContractComments" >
							<logicext:then>
								<div id="editComments" style="display:">
							</logicext:then>
							<logicext:else>	
								<div id="editComments" style="display:none">
							</logicext:else>	
						</logicext:if>
									<b>Comments:</b><br/>
<form:textarea path="contractComments" cols="100" onblur="setMaxLength(this, maxCharForComments)" onkeyup="setMaxLength(this, maxCharForComments)" rows="3" cssStyle="overflow-x: hidden;" /><%-- name="planSponsorContactsReportForm" --%>
									<br/>
									<div style="PADDING-TOP:30px; PADDING-bottom:10px; float:right">
										<span style="PADDING-left: 25px; PADDING-right:5px">
											<input type="button" class="button100Lg"  onclick="doEditCommentsCancel()" value="Cancel" />
										</span>
										<span style="PADDING-left: 25px;PADDING-right: 5px">
											<input type="button" class="button100Lg" onclick="return doSaveComment();" value="Save" /> 
										</span>
									</div>
								</div>
							<logicext:if name="planSponsorContactsReportForm" property="task" op="notEqual" value="editContractComments" >
								<logicext:then>
								<div id="viewComments" style="display:">
								</logicext:then>
								<logicext:else>
									<div id="viewComments" style="display:none">
								</logicext:else>
							</logicext:if>
								<table>
									<tr>
										<td valign="top">
											<b>Comments</b>
										</td>
										<td>
											<c:if test="${not empty contractComment}">
											<b class="highlight">${comentsUpdatedByUser}</b> 
											</c:if>
											:<br/>
											<c:choose>
												<c:when test="${not empty contractComment}">
													<div style="width:610px;height:41px;overflow:auto;">
													${contractComment}
													</div>
												</c:when>
												<c:otherwise>
													<div style="width:610px;height:15px;overflow:auto;">
													No comments
													</div>
												</c:otherwise>
											</c:choose>
											<c:if test="${empty param.printFriendly }" >
												<div style="text-align:right;padding-right:0px; padding-bottom:10px; padding-top:20px" >
												<ps:permissionAccess permissions="EXMN,EXTA">
													<input type="button" class="button100Lg"  onclick="doEditComments()" value="edit comments" >
												</ps:permissionAccess>	
												</div>
											</c:if>
										</td>
									</tr>
								</table>
								</div>
					</td>
				</tr>
			<tr>
		  		<td class="datadivider" colspan="${colspan_display}"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			</ps:isInternalUser>	
			
			<%-- Contacts list --%>
			<tr class="datacell1">
				<td colspan="${colspan_display}" >
					<table width="700" border="0">
						<tr>
							<td width="45" valign="top">
								<strong>Legend:</strong>
							</td>
							<td width="20" valign="top">
								<img src="/assets/unmanaged/images/icon_ps_mail.gif">
							</td>
							<td width="55" valign="top">Client mail recipient </td>
							<td width="20" valign="top">
								<img src="/assets/unmanaged/images/icon_trustee_mail.gif">
							</td>
							<td width="73" valign="top">Trustee / Responsible Plan Fiduciary mail recipient </td>
							<td width="16" valign="top">
								<img src="/assets/unmanaged/images/icon_telephone.gif">
							</td>
							<td width="92" valign="top">Primary contact </td>
							<td width="18" valign="top">
								<img src="/assets/unmanaged/images/icon_authorized_signer.gif">
							</td>
							<td width="98" valign="top">Signature received - authorized signer </td>
							<td width="18" valign="top">
								<img src="/assets/unmanaged/images/icon_trustee.gif">
							</td>
							<td width="111" valign="top">Signature received - trustee </td>
							<td width="12" valign="top">
								<img src="/assets/unmanaged/images/icon_restricted.gif">
							</td>
							<td width="58" valign="top">No web access </td>
							
							<td width="18" valign="top">
							    <img src="/assets/unmanaged/images/icon_direct_debit.gif"></td>
							<td width="50" valign="top">Direct debit </td>
							
							<td width="18" valign="top">
						       <img src="/assets/unmanaged/images/icon_icc_designate.gif"></td>
						   	<td width="50" valign="top">Investment Comparative Chart Designate </td>
						   							   	
						   	<td width="18" valign="top">
						       <img src="/assets/unmanaged/images/icon_send_service_designate.gif"></td>
						   	<td width="50" valign="top">SEND Service Notice Contact</td>
						</tr>
					</table>					
				</td>
			</tr>
			<tr class="datacell1">
				<td height="25" colspan="${colspan_display}" class="tableheadTD">
					<b><content:getAttribute id="layoutPageBean" attribute="body1Header"/></b>
				</td>
            </tr>
			
			<tr class="tablesubhead">
			   <c:if test="${empty param.printFriendly }" >
			   <c:if test="${isActionColumnRequired}">
                <td width="35" class="pgNumBack"><B>Action</B></td>
                <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                </c:if>
               </c:if>
                <td width="142" class="pgNumBack">
					<b>
						<report:sort field="<%=ManageUsersReportData.SORT_FIELD_LAST_NAME%>" formName="planSponsorContactsReportForm"  direction="asc">
							Contact name
						</report:sort>
					</b></td>
                <td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="142" class="pgNumBack">
					<b>
						<report:sort field="<%=ManageUsersReportData.SORT_FIELD_USER_ROLE%>"  formName="planSponsorContactsReportForm"  direction="asc">
							Role
						</report:sort>
					</b>
				</td>
                <td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="90" class="pgNumBack"><B>Special<Br>attributes </B></td>
                <td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="200" class="pgNumBack"><B>Email address </B></td>
                <td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="150" class="pgNumBack"><B>Phone</B></td>
                <td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="120" class="pgNumBack"><B>Fax</B></td>
            </tr>
			
			
			<%--   display a message if there are no users available for display --%>

					<c:if test="${empty theReport.details}">
						<tr class="datacell1">

							<td colspan="${colspan_display}" valign="middle"><c:if
									test="${empty resultsMessageKey }">
									<content:getAttribute id="noActiveUsers" attribute="text" />
								</c:if> 
					<c:if test="${not empty resultsMessageKey }">
								${resultsMessageKey} 
					</c:if></td>
						</tr>

					</c:if>






					<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >

                    
                    <c:set var="indexValue" value="${theIndex.index}"/> 
				<% 				
					UserInfo theItem = (UserInfo)pageContext.getAttribute("theItem");
				    String temp = pageContext.getAttribute("indexValue").toString();
						java.lang.String displayRole = null;
						UserRole role = theItem.getRole();
							if (role == null || role instanceof NoAccess) {
									displayRole = AccessLevelHelper.NO_ACCESS_STRING;
							} else {
								displayRole = role.getDisplayName();
								if (role instanceof IntermediaryContact && theItem.getRoleType() != null) {
									displayRole += " (" + theItem.getRoleType().getDisplayName() + ")";
								}
							}
					%>	
					
					
				<% if (Integer.parseInt(temp) % 2 == 0) { %>
				<tr class="datacell1">
						<% } else { %>
				<tr class="datacell2">
				<% } %>
				<c:if test="${empty param.printFriendly}" >
					<c:if test="${isActionColumnRequired}">
					<td >
						<%-- Action icons will be displayed if logged in user is NOT TPA user --%>
						<ps:isNotTpa name="userInfo" property="role">
						<logicext:if name="theItem" property="userName" op="notEqual" value="<%=userProfile.getName()%>">
						<logicext:then>
							<table width="100%"  cellpadding="0" cellspacing="0">
								<%-- System converted contract --%>
								<c:if test = "${businessConverted eq false}">
									<ps:isInternalUser name="userProfile" property="role">
									<c:if test="${userProfile.role.roleId ne 'PRM' or userProfile.role.roleId ne 'BIU'}">
									<ps:notPermissionAccess permissions="INMN">	
										<tr>
											<td class="candyButtonIconLeftColumn">
<c:set var="viewLink" value="${layoutBean.getParam('viewLink')}"/>
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${viewLink}');">
													<img height=12 alt="View" title="View" src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>
												</a>
											</td>
											<td class="candyButtonIcon">
												<c:if test="${theItem.webAccess eq 'Yes' && theItem.profileStatus ne 'Suspended'}" >
<c:set var="managePasswordLink" value="${layoutBean.getParam('managePasswordLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${managePasswordLink}');">
														<img src="/assets/unmanaged/images/manage_icon.gif" alt="Manage password" title="Manage password" width=12 height=12 border="0">
													</a>
												</c:if>
											</td>
										</tr>
										<tr>
											<td class="candyButtonIconLeftColumn">
												<c:if test="${theItem.profileStatus ne 'Suspended'}">
<c:set var="editLink" value="${layoutBean.getParam('editLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; theClientUserAction='editUser'; return gotoUrl(this, '${editLink}');">
														<img width="12" height="12" alt="edit" title="edit" src="/assets/unmanaged/images/edit_icon.gif"  border="0">
													</a>
												</c:if>
											</td>
											<td class="candyButtonIcon">
<c:set var="deleteLink" value="${layoutBean.getParam('deleteLink')}"/>
												<ps:linkAccessible path="/do/profiles/deleteProfile/">
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${deleteLink}');">
														<img height=12 alt="Delete" title="Delete" src="/assets/unmanaged/images/delete_icon.gif" width=12 border=0>
													</a>
												</ps:linkAccessible>
											</td>
										</tr>
										<tr>
											<td class="candyButtonIconLeftColumn">
												<c:if test="${theItem.webAccess eq 'Yes' && theItem.profileStatus ne 'Suspended'}" >
<c:set var="suspendLink" value="${layoutBean.getParam('suspendLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${suspendLink}');">
														<img src="/assets/unmanaged/images/suspend_icon.gif" alt="Suspend" title="Suspend" width=12 height=12 border="0">
													</a> 
												</c:if>	
											</td>
											<td class="candyButtonIcon">
												<c:if test="${theItem.profileStatus eq 'Suspended'}" >
<c:set var="unsuspendLink" value="${layoutBean.getParam('unsuspendLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${unsuspendLink}');">
														<img src="/assets/unmanaged/images/unsuspend_icon.gif" alt="Unsuspend" title="Unsuspend" width=12 height=12 border="0">
													</a>
												</c:if>	
											</td>
										</tr>
									</ps:notPermissionAccess>
									</c:if>
									</ps:isInternalUser>
									
									<%-- System converted contract and login user is External user --%>
									<ps:isExternal name="loggedInUserInfo" property="role">
										<ps:permissionAccess permissions="EXMN">
											<c:if test="${!ps:hasPermission(theItem.role, 'EXMN')}">
										<tr>
											<td class="candyButtonIconLeftColumn">
<c:set var="viewLink" value="${layoutBean.getParam('viewLink')}"/>
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${viewLink}');">
													<img height=12 alt="View" title="View" src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>
												</a>
											</td>
											<td class="candyButtonIcon">
												<c:if test="${theItem.webAccess eq 'Yes' && theItem.profileStatus ne 'Suspended'}" >
<c:set var="managePasswordLink" value="${layoutBean.getParam('managePasswordLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${managePasswordLink}');">
														<img src="/assets/unmanaged/images/manage_icon.gif" alt="Manage password" title="Manage password" width=12 height=12 border="0">
													</a>
												</c:if>
											</td>
										</tr>
										<tr>
											<td class="candyButtonIconLeftColumn">
												<c:if test="${theItem.profileStatus ne 'Suspended'}">
<c:set var="editLink" value="${layoutBean.getParam('editLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; theClientUserAction='editUser'; return gotoUrl(this, '${editLink}');">
														<img width="12" height="12" alt="edit" title="edit" src="/assets/unmanaged/images/edit_icon.gif"  border="0">
													</a>
												</c:if>
											</td>
											<td class="candyButtonIcon">
<c:set var="deleteLink" value="${layoutBean.getParam('deleteLink')}"/>
												<ps:linkAccessible path="/do/profiles/deleteProfile/">
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${deleteLink}');">
														<img height=12 alt="Delete" title="Delete" src="/assets/unmanaged/images/delete_icon.gif" width=12 border=0>
													</a>
												</ps:linkAccessible>
											</td>
										</tr>
										
										<tr>
											<td class="candyButtonIconLeftColumn">
												<c:if test="${theItem.webAccess eq 'Yes' && theItem.profileStatus ne 'Suspended'}" >
<c:set var="suspendLink" value="${layoutBean.getParam('suspendLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${suspendLink}');">
														<img src="/assets/unmanaged/images/suspend_icon.gif" alt="Suspend" title="Suspend" width=12 height=12 border="0">
													</a> 
												</c:if>	
											</td>
											<td class="candyButtonIcon">
												<c:if test="${theItem.profileStatus eq 'Suspended'}" >
<c:set var="unsuspendLink" value="${layoutBean.getParam('unsuspendLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${unsuspendLink}');">
														<img src="/assets/unmanaged/images/unsuspend_icon.gif" alt="Unsuspend" title="Unsuspend" width=12 height=12 border="0">
													</a>
												</c:if>	
											</td>
										</tr>
										</c:if>
										</ps:permissionAccess>
									</ps:isExternal>

								</c:if>	
								

								<%-- Business converted contract --%>
								<c:if test = "${businessConverted eq true}">
									<%-- Logged in user is Internal user --%>
									<ps:isInternalUser name="loggedInUserInfo" property="role">
									<c:if test="${userProfile.role.roleId eq 'PLC'}">
										<tr>
											<td class="candyButtonIconLeftColumn">
<c:set var="viewLink" value="${layoutBean.getParam('viewLink')}"/>
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${viewLink}');">
													<img height=12 alt="View" title="View" src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>
												</a>
											</td>
											<td class="candyButtonIcon">
													<c:set var="exemptLink" value="${layoutBean.getParam('exemptLink')}"/>
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${exemptLink}');">
													<img height=12 alt="View" title="Passcode exemption" src="/assets/unmanaged/images/icon_passcode_exemption.gif" width=12 border=0>
												</a>									
												
											</td>
											</tr><tr>
											<td class="candyButtonIconLeftColumn">
											<c:if test="${theItem.profileStatus ne 'Suspended'}">
<c:set var="editLink" value="${layoutBean.getParam('editLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; theClientUserAction='editUser'; return gotoUrl(this, '${editLink}');">
														<img width="12" height="12" alt="edit" title="edit" src="/assets/unmanaged/images/edit_icon.gif"  border="0">
													</a>
												</c:if>

											</td>
										  </tr>
									 </c:if>
										<ps:permissionAccess permissions="EXMN,EXTA">
										<tr>
											<td class="candyButtonIconLeftColumn">
<c:set var="viewLink" value="${layoutBean.getParam('viewLink')}"/>
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${viewLink}');">
													<img height=12 alt="View" title="View" src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>
												</a>
												
											</td>
											<td class="candyButtonIcon">
												<c:if test="${theItem.webAccess eq 'Yes' && theItem.profileStatus ne 'Suspended'}" >
<c:set var="managePasswordLink" value="${layoutBean.getParam('managePasswordLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${managePasswordLink}');">
														<img src="/assets/unmanaged/images/manage_icon.gif" alt="Manage password" title="Manage password" width=12 height=12 border="0">
													</a>
												</c:if>
											</td>
										</tr>
										<tr>
											<td class="candyButtonIconLeftColumn">
												<c:if test="${theItem.profileStatus ne 'Suspended'}">
<c:set var="editLink" value="${layoutBean.getParam('editLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; theClientUserAction='editUser'; return gotoUrl(this, '${editLink}');">
														<img width="12" height="12" alt="edit" title="edit" src="/assets/unmanaged/images/edit_icon.gif"  border="0">
													</a>
												</c:if>
											</td>
											<%-- CPS.146 Rule implementation for delete button --%>
											<c:set var="isDeleteAllowed" value="true"/>
											<ps:notPermissionAccess permissions="EXTA">
												<%-- check whether the contact is Trustee or Authorized signor in any contracts --%>
<c:forEach items="${theItem.contractPermissions}" var="contactContractPermission" >
												<% 
												ContractPermission contactContractPermission = (ContractPermission)pageContext.getAttribute("contactContractPermission");
												if(contactContractPermission.getRole() != null && contactContractPermission.getRole().toString().equals(Trustee.ID) 
														|| contactContractPermission.getRole().toString().equals(AuthorizedSignor.ID)) {
												%>
													<c:set var="isDeleteAllowed" value="false"/>
												<% } %>
</c:forEach>
											</ps:notPermissionAccess>
											
											<c:if test="${isDeleteAllowed}">												
												<td class="candyButtonIcon">
<c:set var="deleteLink" value="${layoutBean.getParam('deleteLink')}"/>
													<ps:linkAccessible path="/do/profiles/deleteProfile/">
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${deleteLink}');">
														<img height=12 alt="Delete" title="Delete" src="/assets/unmanaged/images/delete_icon.gif" width=12 border=0>
													</a>
													</ps:linkAccessible>
												</td>
											</c:if>
										</tr>
										<tr>
											<td class="candyButtonIconLeftColumn">
												<c:if test="${theItem.webAccess eq 'Yes' && theItem.profileStatus ne 'Suspended'}" >
<c:set var="suspendLink" value="${layoutBean.getParam('suspendLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${suspendLink}');">
														<img src="/assets/unmanaged/images/suspend_icon.gif" alt="Suspend" title="Suspend" width=12 height=12 border="0">
													</a> 
												</c:if>	
											</td>
											<td class="candyButtonIcon">
												<c:if test="${theItem.profileStatus eq 'Suspended'}" >
<c:set var="unsuspendLink" value="${layoutBean.getParam('unsuspendLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${unsuspendLink}');">
														<img src="/assets/unmanaged/images/unsuspend_icon.gif" alt="Unsuspend" title="Unsuspend" width=12 height=12 border="0">
													</a>
												</c:if>	
											</td>
										</tr>
										<c:if test="${userProfile.role.roleId eq 'ICC'}">
										<tr>
											<td class="candyButtonIconLeftColumn" style="border-top: 1px solid grey; padding-top: 2">
												<c:if test="${theItem.profileStatus ne 'Suspended'}">
<c:set var="addContractLink" value="${layoutBean.getParam('addContractLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; theClientUserAction='addContract'; return gotoUrl(this, '${addContractLink}');">
														<img width="12" height="12" alt="Action on Contract" title="Action on Contract" src="/assets/unmanaged/images/aIcon.gif"  border="0">
													</a>
												</c:if>
											</td>
											<td class="candyButtonIcon" style="border-top: 1px solid grey; padding-top: 2">

<c:set var="viewContractInfoLink" value="${layoutBean.getParam('viewContractInfoLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${viewContractInfoLink}');">
														<img width="12" height="12" alt="Listing of Contracts" title="Listing of Contracts" src="/assets/unmanaged/images/list_icon.gif"  border="0">
													</a>

											</td>
										</tr>
										</c:if>
										</ps:permissionAccess>
									</ps:isInternalUser>
									
									<%-- Logged in user is External user --%>
									<ps:isExternal name="loggedInUserInfo" property="role">
									<ps:permissionAccess permissions="EXMN">	
									 
									 <%
									 if ( loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(Trustee.ID) ||  (  ( (loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(AuthorizedSignor.ID))||(loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(AdministrativeContact.stringID)) ) && ( (theItem.getRole().toString().equals(AdministrativeContact.stringID))||(theItem.getRole().toString().equals(PayrollAdministrator.ID))  ) )
										 ) {
									 %>
									 	<tr>
											<td class="candyButtonIconLeftColumn">
												
<c:set var="viewLink" value="${layoutBean.getParam('viewLink')}"/>
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${viewLink}');">
													<img height=12 alt="View" title="View" src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>
												</a>
												
											</td>
											<td class="candyButtonIcon">
												<c:if test="${theItem.webAccess eq 'Yes' && theItem.profileStatus ne 'Suspended'}" >
<c:set var="managePasswordLink" value="${layoutBean.getParam('managePasswordLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${managePasswordLink}');">
														<img src="/assets/unmanaged/images/manage_icon.gif" alt="Manage password" title="Manage password" width=12 height=12 border="0">
													</a>
												</c:if>
											</td>
										</tr>
										<tr>
											<td class="candyButtonIconLeftColumn">
												<c:if test="${theItem.profileStatus ne 'Suspended'}">
<c:set var="editLink" value="${layoutBean.getParam('editLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; theClientUserAction='editUser'; return gotoUrl(this, '${editLink}');">
														<img width="12" height="12" alt="edit" title="edit" src="/assets/unmanaged/images/edit_icon.gif"  border="0">
													</a>
												</c:if>
											</td>
											<td class="candyButtonIcon">
<c:set var="deleteLink" value="${layoutBean.getParam('deleteLink')}"/>
												<ps:linkAccessible path="/do/profiles/deleteProfile/">
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${deleteLink}');">
														<img height="12" alt="Delete" title="Delete" src="/assets/unmanaged/images/delete_icon.gif" width=12 border=0>
													</a>
												</ps:linkAccessible>
											</td>
										</tr>
										
										<tr>
											<td class="candyButtonIconLeftColumn">
												<c:if test="${theItem.webAccess eq 'Yes' && theItem.profileStatus ne 'Suspended'}" >
<c:set var="suspendLink" value="${layoutBean.getParam('suspendLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${suspendLink}');">
														<img src="/assets/unmanaged/images/suspend_icon.gif" alt="Suspend" title="Suspend" width=12 height=12 border="0">
													</a> 
												</c:if>	
											</td>
											<td class="candyButtonIcon">
												<c:if test="${theItem.profileStatus eq 'Suspended'}" >
<c:set var="unsuspendLink" value="${layoutBean.getParam('unsuspendLink')}"/>
													<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${unsuspendLink}');">
														<img src="/assets/unmanaged/images/unsuspend_icon.gif" alt="Unsuspend" title="Unsuspend" width=12 height=12 border="0">
													</a>
												</c:if>	
											</td>
										</tr>
										<% } %>
									</ps:permissionAccess>
									</ps:isExternal>
								</c:if>
							
							</table>
							</logicext:then>
						</logicext:if>					
						</ps:isNotTpa>
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					</c:if>
				</c:if>					
					<td valign="center" >
						<render:name firstName="<%=theItem.getFirstName()%>" lastName="<%=theItem.getLastName()%>" style="f" defaultValue=""/><ps:isInternalUser name="userProfile" property="role"><c:if test="${theItem.customizedPermissions}">*</c:if></ps:isInternalUser>
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valign="center"><%=displayRole%>
						<c:if test="${theItem.riaDesignation338Ind && not empty theItem.roleType && theItem.roleType.id eq 'RIA'}">+</c:if>
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valign="center" width="90">
						
									<c:if test="${!theItem.webAccessInd}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_restricted.gif" alt="No web access" title="No web access"/>
									</c:if>
								
									<c:if test="${theItem.clientMailRecipient}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_ps_mail.gif" alt="Client mail recipient" title="Client mail recipient"/>
									</c:if>
								
									<c:if test="${theItem.trusteeMailRecipient}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_trustee_mail.gif" alt="Trustee mail recipient" title="Trustee mail recipient"/>
									</c:if>
								
									<c:if test="${theItem.signReceivedAuthSigner}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_authorized_signer.gif" alt="Signature received - authorized signer" title="Signature received - authorized signer"/>
									</c:if>
								
									<c:if test="${theItem.signReceivedTrustee}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_trustee.gif" alt="Signature received - trustee" title="Signature received - trustee"/>
									</c:if>
								
									<c:if test="${theItem.primaryContact}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_telephone.gif" alt="Primary contact" title="Primary contact"/>
									</c:if>
									
									<c:if test="${theItem.statementRecipient}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/statement_indicator.gif" alt="Participant Statement Consultant" title="Participant Statement Consultant"/>
									</c:if>
									
									<c:if test="${theItem.directDebit}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_direct_debit.gif" alt="Direct debit" title="Direct debit"/>
									</c:if> 
									
									<c:if test="${theItem.iccDesignate}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_icc_designate.gif" alt="ICC designate" title="ICC designate"/>
									</c:if> 
									
									<c:if test="${theItem.sendServiceDesignate}">
												<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_send_service_designate.gif" alt="SEND Service Notice Contact" title="SEND Service Notice Contact"/>
									</c:if>
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valign="center">
<c:if test="${not empty theItem.email}">
						<c:if test="${empty param.printFriendly }" >
							<a href="mailto:${theItem.email}">
						</c:if>
								${theItem.email}
						<c:if test="${empty param.printFriendly }" >								
							</a>
						</c:if>
</c:if>
<c:if test="${not empty theItem.secondaryEmail}">
						<c:if test="${empty param.printFriendly }" >
						</c:if><br/>
								${theItem.secondaryEmail}
						<c:if test="${empty param.printFriendly }" >								
						</c:if>
</c:if>
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valign="center">
					<c:if test="${theItem.phoneNumber!=''}" >
						 	<render:phone property="theItem.phoneNumber"  />
</c:if>
<c:if test="${not empty theItem.phoneExtension}">
							<br/>ext. ${theItem.phoneExtension}
</c:if>
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valing="center" >
					<c:if test="${theItem.fax!=''}" >
							<render:fax property="theItem.fax" /> 
</c:if>
					</td>
				</tr>
				
				<ps:isInternalUser name="userInfo" property="role">
				<c:if test="${not empty theItem.contactCommentText}">
					<tr>
		  				<td class="datadivider" colspan="${colspan_display}"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
					</tr>	
					 <% if (Integer.parseInt(temp) % 2 == 0) { %>
					<tr class="datacell1">
							<% } else { %>
					<tr class="datacell2">
					<% } %> 
					<c:if test="${empty param.printFriendly }" >
						<td>
							<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
						</td>
						<td class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</c:if>
						<td valign="top">
							<strong>Comments</strong>
						</td>
						<td class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<c:if test="${empty param.printFriendly }" >
							<td colspan="${(colspan_display - 4)}" >
						</c:if>
						<c:if test="${not empty param.printFriendly }" >
							<td colspan="${(colspan_display - 2)}" >
						</c:if>
							<b class="highlight">${theItem.contactComment.displayLastUpdatedUser}</b><br/>
							<div id="profileCommentsDIV">
									${theItem.contactCommentText}
							</div>
						</td>
					</tr>
				</c:if>
				</ps:isInternalUser>
			<tr>
		  		<td class="datadivider" colspan="${colspan_display}"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
</c:forEach>
</c:if>
			<tr height="45" class="datacell1">
				<td colspan="${colspan_display}" >
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>	
	
			 <ps:isInternalUser name="loggedInUserInfo" property="role">
			<c:if test="${not empty stagingContactList}" >
					<jsp:include page="/WEB-INF/contacts/stagingContactsList.jsp" flush="true"/>
					<tr class="datacell1">
						<td colspan="${colspan_display}" height="15">
							<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
						</td>
					</tr>
</c:if>
			</ps:isInternalUser> 		
			<tr>
				<td colspan="${colspan_display}" >
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tbody>
							<tr>
								<td height="25" colspan="${(colspan_display - 4)}" class="tableheadTD">
									<b><content:getAttribute id="layoutPageBean" attribute="body3Header"/></b>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="${colspan_display}">
					<table class="addressTable1" border="0" cellspacing="0" cellpadding="0" width="100%">
						<thead class="pgNumBack">
						  <th class="pgNumBack" style="border-left-width: 0;"></th>
						  <c:forEach items="${contractAddressList}" var="address" varStatus="addressStatus">
							<th style="border-right-width: ${addressStatus.last ? '0' : '1px'}" class="pgNumBack">${address.header}</th>
						  </c:forEach>
						</thead>
						<tbody>
						  <tr class="datacell1">
							<td class="addressLabelColumn" style="border-left-width: 0;">Line 1</td>
							<c:forEach items="${contractAddressList}" var="address" varStatus="addressStatus">
							  <td style="border-right-width: ${addressStatus.last ? '0' : '1px'}">${address.line1}</td>
							</c:forEach>
						  </tr>
						  <c:if test="${not planSponsorContactsReportForm.suppressLine2}">
							<tr class="datacell2">
							  <td class="addressLabelColumn" style="border-left-width: 0;">Line 2</td>
							  <c:forEach items="${contractAddressList}" var="address" varStatus="addressStatus">
								<td style="border-right-width: ${addressStatus.last ? '0' : '1px'}">${address.line2}</td>
							  </c:forEach>
							</tr>
						  </c:if>
						  
						  <c:if test="${not planSponsorContactsReportForm.suppressLine2}">
							<tr class="datacell1">
						  </c:if>	
						  <c:if test="${planSponsorContactsReportForm.suppressLine2}">
							<tr class="datacell2">
						  </c:if>	
							
							<td class="addressLabelColumn" style="border-left-width: 0;">City</td>
							<c:forEach items="${contractAddressList}" var="address" varStatus="addressStatus">
							  <td style="border-right-width: ${addressStatus.last ? '0' : '1px'}">${address.city}</td>
							</c:forEach>
						  </tr>
						  
						  <c:if test="${not planSponsorContactsReportForm.suppressLine2}">
							<tr class="datacell2">
						  </c:if>
						  <c:if test="${planSponsorContactsReportForm.suppressLine2}">
							<tr class="datacell1">
						  </c:if>
							<td class="addressLabelColumn" style="border-left-width: 0;">State</td>
							<c:forEach items="${contractAddressList}" var="address" varStatus="addressStatus">
							  <td style="border-right-width: ${addressStatus.last ? '0' : '1px'}">${address.stateName}</td>
							</c:forEach>
						  </tr>
						  
						  <c:if test="${not planSponsorContactsReportForm.suppressLine2}">
							<tr class="datacell1">
						 </c:if>
						 <c:if test="${planSponsorContactsReportForm.suppressLine2}">
							<tr class="datacell2">
						 </c:if>
							<td class="addressLabelColumn" style="border-left-width: 0;">Zip code</td>
							<c:forEach items="${contractAddressList}" var="address" varStatus="addressStatus">
							  <td style="border-right-width: ${addressStatus.last ? '0' : '1px'}">${address.zipCodeDisplay}</td>
							</c:forEach>
						  </tr>
						
						 <%--  <c:if test="${not planSponsorContactsReportForm.suppressLine2}">
							<tr class="datacell2">
						 </c:if>
						 <c:if test="${planSponsorContactsReportForm.suppressLine2}">
							<tr class="datacell1">
						 </c:if>
							<td class="addressLabelColumn">Courier Attn</td>
							<c:forEach items="${contractAddressList}" var="address" varStatus="addressStatus">
							  <td style="border-right-width: ${addressStatus.last ? '0' : '1px'}">${address.attention}</td>
							</c:forEach>
						  </tr> --%>
						
						</tbody>
					</table>
					
				</td>
			</tr>
			
    	</table>
		</td>
		</tr>
		</table>

		<c:if test="${empty param.printFriendly }" >
			<ps:isExternal name="loggedInUserInfo" property="role">
			<ps:permissionAccess permissions="EXMN">
			<% if (loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(AdministrativeContact.stringID) ||
			(loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(PlanSponsorUser.ID))) {%>
			
			<div style="PADDING-TOP: 30px;float:right">
				<span style="PADDING-left: 25px;PADDING-right: 5px">
					<input type="button" class="button100Lg" onclick="return doSubmit('/do/profiles/addUser/')" value="add contact" >
				</span>
			</div>
			
			<% } %>
			</ps:permissionAccess>
			</ps:isExternal>
			<ps:isExternal name="loggedInUserInfo" property="role">
			<ps:permissionAccess permissions="EXMN">
			<% if( (!(loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(AdministrativeContact.stringID))) && (!(loggedInUserInfo.getContractPermission(userProfile.getCurrentContract().getContractNumber()).getRole().toString().equals(PlanSponsorUser.ID))  ) ){%>
			<div style="PADDING-TOP: 30px;float:right">
				<span style="PADDING-left: 25px;PADDING-right: 5px">
					<input type="button" class="button100Lg" onclick="return doSubmit('/do/profiles/addUser/')" value="add contact" >
				</span>
				<span style="PADDING-left: 25px;PADDING-right: 5px">
					<input type="button" class="button100Lg" onclick="return doSubmit('/do/editContractAddress/')" value="edit addresses" >
				</span>
			</div>
			<% } %>
			</ps:permissionAccess>
			</ps:isExternal>
			<ps:isInternalUser name="loggedInUserInfo" property="role">
			<ps:permissionAccess permissions="EXMN,EXTA">
			<div style="PADDING-TOP: 30px;float:right">
				<span style="PADDING-left: 25px;PADDING-right: 5px">
					<input type="button" class="button100Lg" onclick="return doSubmit('/do/profiles/addUser/')" value="add contact" >
				</span>
				<span style="PADDING-left: 25px;PADDING-right: 5px">
					<input type="button" class="button100Lg" onclick="return doSubmit('/do/editContractAddress/')" value="edit" >
				</span>
			</div>
			</ps:permissionAccess>
			</ps:isInternalUser>
		</c:if>
</ps:form>
