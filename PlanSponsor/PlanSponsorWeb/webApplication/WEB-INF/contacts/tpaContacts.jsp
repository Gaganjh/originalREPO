<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.security.valueobject.UserInfo"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.valueobject.ManageUsersReportData" %>
<%@ page import="com.manulife.pension.ps.web.controller.SecurityManager" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AccessLevelHelper" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_ACTIVE_USERS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noActiveUsers" />

<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_AVAILABLE_USERS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noAvailableUsers" />

<content:contentBean contentId="<%=ContentConstants.MESSAGE_SELECT_SHOW_ALL_CONTACTS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="selectShowAllContacts" />

<content:contentBean contentId="<%=ContentConstants.MANAGE_TPA_FIRM_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="manageTpaFirmLink" />

<content:contentBean contentId="<%=ContentConstants.MANAGE_TPA_USER_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="manageTpaUserLink" />

<content:contentBean contentId="<%=ContentConstants.USER_MANAGEMENT_SUMMARY_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="userManagementSummaryLink" />

<content:contentBean contentId="<%=ContentConstants.TPA_FIRM_PERMISSIONS_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="tpaFirmPermissionsLink" />

<content:contentBean contentId="<%=ContentConstants.MESSAGE_CUSTOMIZED_PERMISSIONS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="msgCustomizedPermission" />

<%-- <c:set var="theReport" value="${Constants.USERPROFILE_KEY}"/> scope="session" type="com.manulife.pension.ps.web.controller.UserProfile"

			
<c:set var="theReport" value="${Constants.REPORT_BEAN}" scope="page"/> scope="request" type="com.manulife.pension.service.security.valueobject.ManageUsersReportData"
 --%>

<% 
ManageUsersReportData theReport = (ManageUsersReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

UserInfo userInfo = (UserInfo)request.getAttribute(Constants.USERINFO_KEY);
pageContext.setAttribute("userInfo",userInfo,PageContext.PAGE_SCOPE);

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>

			
<c:set var="viewLink" value="${layoutBean.getParam('viewLink')}"/>

<c:set var="exemptLink" value="${layoutBean.getParam('exemptLink')}"/>

<c:set var="managePasswordLink" value="${layoutBean.getParam('managePasswordLink')}"/>

<c:set var="editLink" value="${layoutBean.getParam('editLink')}"/>

<c:set var="deleteLink" value="${layoutBean.getParam('deleteLink')}"/>

<c:set var="suspendLink" value="${layoutBean.getParam('suspendLink')}"/>

<c:set var="unsuspendLink" value="${layoutBean.getParam('unsuspendLink')}"/>
			

<script  type="text/javascript">
	var submitted = false;

	var theProfileId = null;
	
	function gotoUrl(theAnchor, theUrl) {
		if(!discardChanges()){
			return false;	
		}	
		
		if (!submitted) {
			submitted = true;
			if (theProfileId != null) {
				theAnchor.href = theUrl+ '?profileId=' + theProfileId + '&fromTPAContactsTab=true';
			}
			
			return true;
		 } else {
			 window.status = "Transaction already in progress.  Please wait.";
			 return false;
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

	function doAddContact(theAnchor,  contactId) {
		if (!submitted) {
			submitted = true;
			theAnchor.href = "/do/profiles/addUser/?contactId=" + contactId;
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}
	
	function doSaveComment() {
		if (!submitted) {
			submitted = true;
			commentsMaxLengthCheck();
			document.forms['tpaContactsReportForm'].task.value = "updateTpaComments"; 
			document.forms['tpaContactsReportForm'].submit();
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}
	
	function doEditComments() {
		if (!submitted) {
			submitted = true;
			document.forms['tpaContactsReportForm'].task.value = "editTpaComments"; 
			document.forms['tpaContactsReportForm'].submit();
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
			document.forms['tpaContactsReportForm'].task.value = "editCommentsCancel"; 
			document.forms['tpaContactsReportForm'].submit();
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}
	
	function doShowContacts() {
		if (!submitted) {
			submitted = true;
			document.forms['tpaContactsReportForm'].task.value = "common"; 
			document.forms['tpaContactsReportForm'].showAllContactsInd.value = "Y";			
			document.forms['tpaContactsReportForm'].submit();
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}
	function doShowContacts1() {
		if (!submitted) {
			submitted = true;
			document.forms['tpaContactsReportForm'].showAllContactsInd.value = "N";			
			document.forms['tpaContactsReportForm'].submit();
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}

	function commentsMaxLengthCheck() {
		var MaxLen = 500;
		var tpaCommentsValue = document.forms['tpaContactsReportForm'].tpaComments.value;
		var newLineCount = tpaCommentsValue.replace(/[^\n]/g, '').length;
		MaxLen -= newLineCount;
		if(tpaCommentsValue.length > MaxLen){
			document.forms['tpaContactsReportForm'].tpaComments.value = tpaCommentsValue.substring(0,MaxLen);
		}
	}


	// Register the event.
	if (window.addEventListener) {	
		window.addEventListener('load', protectLinksFromCancel, false);
	} else if (window.attachEvent) {	
		window.attachEvent('onload', protectLinksFromCancel);
	}	
	
	var confirmMessage = "All Changes made on this page will be lost. Please click Ok to continue."
	var  tabName = "contactsTPA";
	registerTrackChangesFunction(isTPACommentsChanged, tabName, confirmMessage);
		
	function isTPACommentsChanged(){
		return document.forms['tpaContactsReportForm'].task.value == "editTpaComments";
	}

</script>


<ps:form cssClass="margin-bottom:0;" method="POST" action="/do/contacts/thirdPartyAdministrator/" name="tpaContactsReportForm" modelAttribute="tpaContactsReportForm" >

<form:hidden path="task"/>
<form:hidden path="showAllContactsInd"/>

<ps:isInternalUser name="userProfile" property="role">
  <content:getAttribute id="msgCustomizedPermission" attribute="text"/><br>
</ps:isInternalUser>

<DIV id="errordivcs"><content:errors scope="session"/></DIV><br>

<c:set var="col_span" scope="page">
	<c:if test="${not empty param.printFriendly}" >11</c:if>
	<c:if test="${empty param.printFriendly}" >
		<c:if test="${tpaContactsReportForm.displayActionColumnInd}">13</c:if>
		<c:if test="${!tpaContactsReportForm.displayActionColumnInd}">11</c:if>
	</c:if>
</c:set>

<%-- TAB section --%>
  <jsp:include page="ContactsTab.jsp" flush="true">
 	<jsp:param value="3" name="tabValue"/>
  	<jsp:param value="${tpaFirmAccessForContract}" name="tpaFirmAccessForContract"/>
  </jsp:include>
   	<table width="100%" border="0" cellspacing="0" cellpadding="0">    		 
				<tr class="datacell1">
					<td colspan="${col_span}" class="beigeborder2">
						<%-- Links section --%>
						<c:if test="${empty param.printFriendly}" >
							<table>
								<tr>
								<c:if test="${userProfile.role.planSponsor || userProfile.role.internalUser}">
									<ps:permissionAccess permissions="EXMN">
										<td width="100">
											<A href="/do/profiles/editTpaFirm/">
												<content:getAttribute id="manageTpaFirmLink" attribute="text"/>
											</A>
										</td>
										<ps:isInternalUser name="userProfile" property="role">
											<ps:permissionAccess permissions="TUMN">
												<td width="8">|<img src="/assets/unmanaged/images/spacer.gif" border="0" height="0.5" width="1"></td>
											</ps:permissionAccess>
										</ps:isInternalUser>
									</ps:permissionAccess>
								</c:if>
								<ps:isInternalOrTpa name="userProfile" property="role">
									<ps:permissionAccess permissions="TUMN" tpaFirmId="${tpaFirmInfo.id}">
										<td width="110">
											<A href="/do/profiles/manageTpaUsers/">
												<content:getAttribute id="manageTpaUserLink" attribute="text"/>
											</A>
										</td>
										
										<td width="8">|<img src="/assets/unmanaged/images/spacer.gif" border="0" height="0.5" width="1"></td>
										
										<td width="170">
											<A href="/do/profiles/tpaUserManagementSummary/?task=download&ext=.csv">
												<content:getAttribute id="userManagementSummaryLink" attribute="text"/>
											</A>
										</td>
										
										<td width="8">|<img src="/assets/unmanaged/images/spacer.gif" border="0" height="0.5" width="1"></td>
								
										<td width="250">
											<A href="/do/profiles/clientTpaFirmPermissions/?task=download&ext=.csv'">
												<content:getAttribute id="tpaFirmPermissionsLink" attribute="text"/>
											</A>
										</td>
									</ps:permissionAccess>
								</ps:isInternalOrTpa>
								</tr>
								
								<tr><td>&nbsp;</td></tr>
							</table>
						</c:if>
						
						<%-- Comments section --%>
						<table width="100%">
							<%-- Plan Sponsor Comments --%>
							<ps:isInternalUser name="userProfile" property="role">
								<tr>
									<td colspan="2">
										<div style="padding-bottom:4px">
											<b>Plan Sponsor Comments:</b>
											<b class="highlight">${contractComment.displayLastUpdatedUser} </b>
										</div>
									</td>
								</tr>
								<tr>
									<td width="20%">&nbsp;</td>
									<td width="80%" style="padding-bottom:20px">
										<c:if test="${empty param.printFriendly}" >
											<div style="width:100%;height:40px;overflow:auto;">
												${contractComment.commentUIText}
											</div>
										</c:if>
										<c:if test="${not empty param.printFriendly}" >
											<div style="width:100%;">
												${contractComment.commentUIText}
											</div>
										</c:if>
									</td>	
								</tr>
							</ps:isInternalUser>
							
							<%-- TPA Firm name --%>
							<tr>
								<td class="tablesubhead" colspan="2">
									<b><content:getAttribute id="layoutPageBean" attribute="body1Header"/> </b>${tpaFirmInfo.name}(${tpaFirmInfo.id})
								</td>
							</tr>
						</table>
						<table width="100%">
							<%-- TPA Comments --%>
							<ps:isInternalUser name="userProfile" property="role">							
							
							<tr>
								<td style="padding-top:16px" colspan="2">
									<div style="padding-bottom:4px">
										<b>TPA Firm comments:</b>
										<logicext:if name="tpaContactsReportForm" property="task" op="notEqual" value="editTpaComments" >
										<logicext:then>
											<b class="highlight">${tpaComment.displayLastUpdatedUser} </b>
										</logicext:then>
										</logicext:if>
									</div>
								</td>
							</tr>
							<tr>
								<logicext:if name="tpaContactsReportForm" property="task" op="equal" value="editTpaComments" >
									<logicext:then>
										<td colspan="2">
<form:textarea path="tpaComments" cols="100" onblur="commentsMaxLengthCheck()" onkeyup="commentsMaxLengthCheck()" rows="3" />
											<br/>
											<div style="PADDING-TOP:30px; PADDING-bottom:10px; float:right">
												<span style="PADDING-left: 25px; PADDING-right:5px">
													<input type="button" class="button100Lg"  onclick="doEditCommentsCancel()" value="cancel" />
												</span>
												<span style="PADDING-left: 25px;PADDING-right: 5px">
													<input type="button" class="button100Lg" onclick="return doSaveComment();" value="save" /> 
												</span>
											</div>
										</td>
									</logicext:then>
									<logicext:else>
										<td width="17%">&nbsp;</td> 
										<td width="83%">
											<c:if test="${empty param.printFriendly}" >
												<div style="width:100%;height:40px;overflow:auto;">
													${tpaComment.commentUIText}
												</div>
												<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
												<div style="text-align:right;padding-right:5px; padding-bottom:10px; padding-top:20px" >
													<input type="button" class="button100Lg"  onclick="doEditComments()" value="edit comments" >
												</div>
												</ps:isNotJhtc>
											</c:if>	
											<c:if test="${not empty param.printFriendly}" >
												<div style="width:100%;padding-bottom:20px">
													${tpaComment.commentUIText}
												</div>
											</c:if>	
										</td>											
									</logicext:else>
								</logicext:if>
							</tr>
							</ps:isInternalUser>
						</table>
					</td>
				</tr>
			
			<%-- Contacts Report --%>
			<tr class="datacell1">
				<td colspan="${col_span}" class="beigeborder2">
					<table width="700" border="0">
						<tr>
							<td width="45" valign="top">
								<strong>Legend:</strong>
							</td>
							<td width="18" valign="top">
								<img src="/assets/unmanaged/images/icon_tpa_contact.gif">
							</td>
							<td width="98" valign="top">TPA contact</td>
							<td width="18" valign="top">
								<img src="/assets/unmanaged/images/icon_authorized_signer.gif">
							</td>
							<td width="100" valign="top">Signature received - authorized signer </td>
							<td width="18" valign="top">
								<img src="/assets/unmanaged/images/icon_signing_authority.gif">
							</td>
							<td width="98" valign="top">Signing authority </td>
							<td width="12" valign="top">
								<img src="/assets/unmanaged/images/icon_restricted.gif">
							</td>
							<td width="82" valign="top">No web access </td>
							
							<td width="12" valign="top">
							    <img src="/assets/unmanaged/images/icon_direct_debit.gif">
							<td width="50" valign="top">Direct debit </td>
							
						</tr>
					</table>					
				</td>
			</tr>	
			 <tr>
				<td height="25" colspan="${col_span}" class="tableheadTD">
				
					<b><content:getAttribute id="layoutPageBean" attribute="body2Header"/></b>
					<c:if test="${empty param.printFriendly}" >
						<c:if test="${!userProfile.role.planSponsor && !tpaContactsReportForm.noActiveUsers}">
							<c:if test="${tpaContactsReportForm.showAllContactsInd == 'Y'}"> 

							 <!-- <a href="javascript:doShowContacts1();">Show contract contacts</a> -->
								  <a href="/do/contacts/thirdPartyAdministrator/">Show contract contacts</a> 
							</c:if>
							 <c:if test="${tpaContactsReportForm.showAllContactsInd != 'Y'}">
							|<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
								<a href="javascript:doShowContacts();">Show all contacts</a>
							</c:if> 
						</c:if>
					</c:if>
				</td>
            </tr> 
			
			<tr class="tablesubhead">
				<td class="databorder" width="1">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1"/>
					</td>
				<c:if test="${empty param.printFriendly}" >
<c:if test="${tpaContactsReportForm.displayActionColumnInd ==true}">
		                <td width="36" class="pgNumBack"><B>Action</B></td>
		                <td width="0.1%" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
</c:if>
                </c:if>
                <td width="20%" class="pgNumBack">
					<b>
						<report:sort field="<%=ManageUsersReportData.SORT_FIELD_LAST_NAME%>" formName="tpaContactsReportForm" direction="asc">
							Contact name
						</report:sort>
					</b>
				</td>
                <td width="0.1%" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="11%" class="pgNumBack"><B>Special<Br>attributes </B></td>
                <td width="0.1%" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="30%" class="pgNumBack"><B>Email address </B></td>
                <td width="0.1%" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <c:if test="${empty param.printFriendly}" >
					<logicext:if name="tpaContactsReportForm" property="displayActionColumnInd" op="equal" value="true">
						<logicext:then>
							<td width="18%" class="pgNumBack"><B>Phone</B></td>
						</logicext:then>
						<logicext:else>
							<td width="20%" class="pgNumBack"><B>Phone</B></td>
						</logicext:else>
					</logicext:if>
				</c:if>
				<c:if test="${not empty param.printFriendly}" >
					<td width="20%" class="pgNumBack"><B>Phone</B></td>
				</c:if>
                <td width="0.1%" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <c:if test="${empty param.printFriendly}" >
					<logicext:if name="tpaContactsReportForm" property="displayActionColumnInd" op="equal" value="true">
						<logicext:then>
							<td width="18%" class="pgNumBack"><B>Fax</B></td>
						</logicext:then>
						<logicext:else>
							<td width="20%" class="pgNumBack"><B>Fax</B></td>
						</logicext:else>
					</logicext:if>
				</c:if>
				<c:if test="${not empty param.printFriendly}" >
					<td width="20%" class="pgNumBack"><B>Fax</B></td>
				</c:if>
				<td class="databorder" width="1">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1"/>
					</td>
            </tr>
			
			
			<%--   display a message if there are no users available for display --%>

<c:if test="${empty theReport.details}">
				<tr class="datacell1">
					<td colspan = "${col_span}"  valign="middle">
					<logicext:if name="tpaContactsReportForm" property="noActiveUsers" op="equal" value="true" >
						<logicext:then>
							<content:getAttribute id="noActiveUsers" attribute="text"/>
						</logicext:then>
						<logicext:else>
							<c:if test="${tpaContactsReportForm.noAvailableUsers}">
								<c:if test="${userProfile.role.planSponsor}">									
									<content:getAttribute id="noAvailableUsers" attribute="text"/>									
								</c:if>
								<c:if test="${!userProfile.role.planSponsor}">
									<content:getAttribute id="selectShowAllContacts" attribute="text"/>
								</c:if>
							</c:if>
						</logicext:else>
					</logicext:if>
					</td>
				</tr>
</c:if>
			
			<%-- Report Data --%>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex">

                    
                    <c:set var="indexValue" value="${theIndex.index}"/> 
				<% 				
					UserInfo theItem = (UserInfo)pageContext.getAttribute("theItem");
				    String temp = pageContext.getAttribute("indexValue").toString();%>
				 <% if (Integer.parseInt(temp) % 2 == 0) { %>
				<tr class="datacell1">
						<% } else { %>
				<tr class="datacell2">
				<% } %>
					<td class="databorder" width="1">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1"/>
					</td>
					<c:if test="${empty param.printFriendly}" >
<c:if test="${tpaContactsReportForm.displayActionColumnInd ==true}">
						<td>
						<c:if test="${userProfile.role.roleId ne 'PLC'}">
							<c:if test="${!(userProfile.role.TPA && theItem.tpaUserManagerInd)}">
								<table width="100%"  cellpadding="0" cellspacing="0">
									<tr>
										<td class="candyButtonIconLeftColumn">
											<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${viewLink}');">
												<img height=12 title="View" src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>
											</a>
										</td>
										<td class="candyButtonIcon">
											<c:if test="${theItem.webAccess eq 'Yes' && theItem.profileStatus ne 'Suspended'}" >
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${managePasswordLink}');">
													<img src="/assets/unmanaged/images/manage_icon.gif" title="Manage password" width=12 height=12 border="0">
												</a>
											</c:if>
										</td>
									</tr>
									<tr>
										<td class="candyButtonIconLeftColumn">
											<c:if test="${theItem.profileStatus ne 'Suspended'}">
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${editLink}');">
													<img width="12" height="12" title="Edit" src="/assets/unmanaged/images/edit_icon.gif"  border="0">
												</a>
											</c:if>
										</td>
										<td class="candyButtonIcon">
											<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${deleteLink}');">
												<img height=12 title="Delete" src="/assets/unmanaged/images/delete_icon.gif" width=12 border=0>
											</a>
										</td>
									</tr>
									<tr>
										<td class="candyButtonIconLeftColumn">
											<c:if test="${theItem.webAccess eq 'Yes' && theItem.profileStatus ne 'Suspended'}" >
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${suspendLink}');">
													<img src="/assets/unmanaged/images/suspend_icon.gif" title="Suspend" width=12 height=12 border="0">
												</a>
											</c:if>	
										</td>
										<td class="candyButtonIcon">
											<c:if test="${theItem.profileStatus eq 'Suspended'}" >
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${unsuspendLink}');">
													<img src="/assets/unmanaged/images/unsuspend_icon.gif" title="Unsuspend" width=12 height=12 border="0">
												</a>
											</c:if>	
										</td>
									</tr>
								</table>
							</c:if>
						</c:if>
						<c:if test="${userProfile.role.roleId eq 'PLC'}">
								<table width="100%"  cellpadding="0" cellspacing="0">
									<tr>
										<td class="candyButtonIconLeftColumn">
											<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${viewLink}');">
												<img height=12 title="View" src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>
											</a>
										</td>
										<td class="candyButtonIcon">
											<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${exemptLink}');">
												<img height=12 title="Passcode exemption" src="/assets/unmanaged/images/icon_passcode_exemption.gif" width=12 border=0>
											</a>
										</td>
									</tr>
									<tr>
									
										<td class="candyButtonIconLeftColumn">
											<c:if test="${theItem.profileStatus ne 'Suspended'}">
												<a href="javascript://" onClick="theProfileId='<%=StringEscapeUtils.escapeJavaScript(String.valueOf(theItem.getProfileId()))%>'; return gotoUrl(this, '${editLink}');">
													<img width="12" height="12" title="Edit" src="/assets/unmanaged/images/edit_icon.gif"  border="0">
												</a>
											</c:if>
										</td>
									</tr>
								</table>
						</c:if>
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
					<td>
						
									<c:if test="${theItem.signReceivedAuthSigner}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_authorized_signer.gif" alt="Signature received - authorized signer" />
									</c:if>
								
									<c:if test="${theItem.tpaContactWithSigningAuthority}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_signing_authority.gif" alt="Signing authority" />
									</c:if>
								
									<c:if test="${!theItem.webAccessInd}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_restricted.gif" alt="No web access" />
									</c:if>
								
									<c:if test="${theItem.primaryContact}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_tpa_contact.gif" alt="TPA contact" />
									</c:if>
									
									<c:if test="${theItem.directDebit}">
										<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="3"><img src="/assets/unmanaged/images/icon_direct_debit.gif" alt="Direct debit"/>
									</c:if>
									
								
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valign="center">
<c:if test="${not empty theItem.email}">
							<c:if test="${empty param.printFriendly}" >
								<a href="mailto:${theItem.email}">
							</c:if>
									${theItem.email}
							<c:if test="${empty param.printFriendly}" >
								</a>
							</c:if>
</c:if>
<c:if test="${not empty theItem.secondaryEmail}">
							</br><c:if test="${empty param.printFriendly}" >
							</c:if>
									${theItem.secondaryEmail}
							<c:if test="${empty param.printFriendly}" >
							</c:if>
</c:if>
						
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valign="center">
					<c:if test="${theItem.phoneNumber!='' }">
							<render:phone property="theItem.phoneNumber"/>
</c:if>
						<c:if test="${theItem.phoneExtension!='' }">
						   <br/>ext. ${theItem.phoneExtension}
</c:if>
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valign="center">
						<c:if test="${theItem.fax!='' }">
							<render:phone property="theItem.fax"/>
</c:if>
					</td>
					<td class="databorder" width="1">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1"/>
					</td>
				</tr>
				
</c:forEach>
</c:if>
			
			<tr height="45" class="datacell1">
				<td colspan="${col_span}" class="beigeborder2">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
			
			<%-- Address section --%>
			<tr>
				<td colspan="${col_span}" class="beigeborder2">
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tbody>
							<tr>
								<td height="25" class="tableheadTD">
									<b><content:getAttribute id="layoutPageBean" attribute="body3Header"/></b>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="${col_span}" class="beigeborder2">
					<table class="addressTable1">
						<tbody>
						  <tr class="datacell1">
							<td class="addressLabelColumn" width="15%">Attention to</td>
							<td width="85%">${tpaFirmInfo.mailRecipientName}</td>
						  </tr>
						  <tr class="datacell2">
							<td class="addressLabelColumn">Line 1</td>
							<td>${tpaFirmInfo.addressLine1}</td>
						  </tr>
						  <tr class="datacell1">
							<td class="addressLabelColumn">Line 2</td>
							<td>${tpaFirmInfo.addressLine2}</td>
						  </tr>
						  <tr class="datacell2">
							<td class="addressLabelColumn">City</td>
							<td>${tpaFirmInfo.city}</td>
						  </tr>
						  
						  <tr class="datacell1">
							<td class="addressLabelColumn">State</td>
							<td>${tpaFirmInfo.state}</td>
						  </tr>
						  
						  <tr class="datacell2">
							<td class="addressLabelColumn">ZIP code</td>
							<td>${tpaFirmInfo.zipCodeDisplay}</td>
						  </tr>
						</tbody>
					</table>
				</td>
			</tr>

    	</table>
		
		<c:if test="${empty param.printFriendly}" >
		<%-- Condition to display Add Contact button is similar to that of displaying Action Icons.
			 Hence using the same property --%>
		<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
<c:if test="${tpaContactsReportForm.displayActionColumnInd ==true}">
				<div style="PADDING-TOP: 30px;float:right">
					<span style="PADDING-left: 25px;PADDING-right: 5px">
						<input type="button" class="button100Lg" onclick="doSubmit('/do/profiles/addTpaUser/?fromTPAContactsTab=true')" value="add contact" >
					</span>
				</div>
</c:if>
		</ps:isNotJhtc>
		</c:if>
</ps:form>		
		
