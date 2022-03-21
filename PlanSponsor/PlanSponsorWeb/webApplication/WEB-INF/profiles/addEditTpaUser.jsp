<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ page import="com.manulife.pension.ps.web.profiles.AddEditUserForm"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%> 

<content:contentBean
	contentId="<%=ContentConstants.WARNING_TPA_NO_MORE_REGISTERED_USER%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>"
	id="warningTpaNoMoreRegisteredUser" />

<content:contentBean
	contentId="<%=ContentConstants.WARNING_TPA_LAST_USER_RECEIVE_ILOANS_EMAIL%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>"
	id="warningLastUserReceiveIloansEmail" />


<content:contentBean
	contentId="<%=ContentConstants.WARNING_TPA_LAST_USER_STAFF_PLAN_AND_RECEIVE_ILOANS_EMAIL%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>"
	id="warningLastUserStaffPlanAndReceiveIloansEmail" />

<%
String ADD_EDIT_USER_TPA_FIRM_LIST=Constants.ADD_EDIT_USER_TPA_FIRM_LIST;
pageContext.setAttribute("ADD_EDIT_USER_TPA_FIRM_LIST",ADD_EDIT_USER_TPA_FIRM_LIST,PageContext.PAGE_SCOPE);

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

AddEditUserForm addEditUserForm = (AddEditUserForm)session.getAttribute("addEditUserForm");
pageContext.setAttribute("addEditUserForm",addEditUserForm,PageContext.PAGE_SCOPE);



%>


<c:if test="${ADD_EDIT_USER_TPA_FIRM_LIST}">

<c:set var="firmList" value="${ADD_EDIT_USER_TPA_FIRM_LIST}" scope="request"/> <%-- scope="request" --%>

</c:if>

<%
Boolean emailValue=((AddEditUserForm)pageContext.getAttribute("addEditUserForm")).getEmailPreferences().isShowiLoanEmail();
%>

<script type="text/javascript" >
var submitted = false;
var prefsOpen = false;

function removeTpa(tpaId, index) {
  if (!submitted) {
	  var lastRegisteredUser = document.getElementsByName("tpaFirms[" + index + "].lastRegisteredUser")[0].value;
	  var lastUserWithManageUsers = document.getElementsByName("tpaFirms[" + index + "].lastUserWithManageUsers")[0].value;
	  var lastUserWithReceiveILoansEmail = document.getElementsByName("tpaFirms[" + index + "].lastUserWithReceiveILoansEmail")[0].value;
	  var lastUserWithReceiveILoansEmailAndTPAStaffPlan = document.getElementsByName("tpaFirms[" + index + "].lastUserWithReceiveILoansEmailAndTPAStaffPlan")[0].value;
	  var lastUserWithSigningAuthorityContracts = document.getElementsByName("tpaFirms[" + index + "].lastUserWithSigningAuthorityContracts")[0].value;
	  var lastUserWithReviewIWithdrawalsContracts = document.getElementsByName("tpaFirms[" + index + "].lastUserWithReviewIWithdrawalsContracts")[0].value;
	  var lastUserWithReviewLoansContracts = document.getElementsByName("tpaFirms[" + index + "].lastUserWithReviewLoansContracts")[0].value;
	 <%--  <%AddEditUserForm theItem=(AddEditUserForm) (pageContext.getAttribute("addEditUserForm.getEmailPreferences().isShowiLoanEmail()")); %> --%>
	 
	  var showiLoanEmail = <%=emailValue%>;
	  
	  var msgText="";
		
	  if (lastRegisteredUser == "true") {
		msgText = 'Firm(s): ' + tpaId + ' <content:getAttribute id="warningTpaNoMoreRegisteredUser" attribute="text"/>\n';
	  }
	  
	  if ( lastUserWithManageUsers == "true" ) {
		  msgText += "Firm: " + tpaId + " Warning! No users for this firm will have Manage users permission.\n";
	  }
	
	  if (showiLoanEmail) {
	  	if ( lastUserWithReceiveILoansEmail == "true" ) {
			msgText = msgText + 'Firm: ' + tpaId + ' <content:getAttribute id="warningLastUserReceiveIloansEmail" attribute="text"/>\n';
	  	}
	  }
		
	  if (showiLoanEmail) {
	  	if ( lastUserWithReceiveILoansEmailAndTPAStaffPlan == "true" ) {
			msgText = msgText + 'Firm: ' + tpaId + ' <content:getAttribute id="warningLastUserStaffPlanAndReceiveIloansEmail" attribute="text"/>\n';
	  	}
	  }
	
	  if ( lastUserWithReviewIWithdrawalsContracts != null && lastUserWithReviewIWithdrawalsContracts != "") {
		  msgText += "Warning! No users for contract(s) " + lastUserWithReviewIWithdrawalsContracts + " will have review i:withdrawals permission.\n";
	  }
	
	  if ( lastUserWithSigningAuthorityContracts != null && lastUserWithSigningAuthorityContracts != "") {
		  msgText += "Warning! No users for contract(s) " + lastUserWithSigningAuthorityContracts + " will have signing authority permission.\n";
	  }

	  if ( lastUserWithReviewLoansContracts != null && lastUserWithReviewLoansContracts != "") {
		  msgText += "Warning! No users for contract(s) " + lastUserWithReviewLoansContracts + " will have review loans permission.\n";
	  }
	
	  if ( msgText.length > 0) {
		msgText = msgText + 'Are you sure you would like to continue?';
		if (!confirm(msgText)) {
		  return;
		}
	  }
	
	  submitted = true;
	  unhookWarning();
	 	  document.addEditUserForm.action = '${actionPath}?action=removeTpaFirm';
	
	  document.addEditUserForm.removeTpaFirmId.value = tpaId;
	
	  document.addEditUserForm.submit();
  } else {
	  window.status = "Transaction already in progress.  Please wait.";
	  return;
  }
}

function doSaveUser(){
  if (!submitted) {
	var warnings = getWarnings();
	if (warnings != "") {
		warnings += "Do you want to continue with these changes?";
		submitted = confirm(warnings);
	} else {
		submitted = true;
	}
	return submitted;
  } else {
	  window.status = "Transaction already in progress.  Please wait.";
	  return false;
  }
}

function getWarnings() {
	var warnings = "";
	var firmCount = <%=addEditUserForm.getTpaFirms().size()%>;
	var webAccessValue = getRadioValue("webAccess");
	
	for (var i = 0; i < firmCount; i++) {
		var tpaIdElement = document.getElementsByName("tpaFirm[" + i + "].id")[0];
		
		if (tpaIdElement != null) {
			// Warning if firm has all permissions set to no
			if (webAccessValue == "true" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].tpaStaffPlanAccess") == "false" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].reportDownload") == "false" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].updateCensusData") == "false" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].viewSalary") == "false" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].editApprovePlan") == "false" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].submissionAccess") == "false" &&
				(getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].originalSubmissionAccess") == "true" ||
				(getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].userPermissions.createUploadSubmissions") == "false" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].userPermissions.viewAllUsersSubmissions") == "false" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].userPermissions.cashAccount") == "false" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].userPermissions.directDebit") == "false")) &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].reviewIWithdrawals") == "false" &&
				(getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].originalReviewIWithdrawals") == "true" ||
				(getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].userPermissions.initiateAndViewMyWithdrawals") == "false") &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].userPermissions.signingAuthority") == "false") && 
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].userPermissions.initiateLoans") == "false" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].userPermissions.viewAllLoans") == "false" &&
				getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].userPermissions.reviewLoans") == "false") {
					warnings += "Warning! You have not granted any permissions for firm " + tpaIdElement.value +".\n";
			}
			
			// Warning if contract does not have a Client or TPA profile with manage users permission
			if (document.getElementsByName("tpaFirm[" + i + "].lastUserWithManageUsers")[0].value == "true") {
				var manageUsersValue = getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].manageUsers");
				if (manageUsersValue == "false" || webAccessValue == "false") {
					warnings += "Warning! No users for firm " + tpaIdElement.value +" will have Manage Users permission.\n";
				}
			}
	
			// Warning if contract does not have a Client or TPA profile with Receive i:loans email preference
			if (document.getElementsByName("tpaFirm[" + i + "].lastUserWithReceiveILoansEmail")[0].value == "true") {
				var receiveILoansEmailValue = getRadioValue("emailPreferences.receiveiLoads");
				var showiLoanEmail = emailValue;
				if (showiLoanEmail) {
					if (receiveILoansEmailValue=="false" || webAccessValue == "false") {
						warnings += "Warning! No users for firm " + tpaIdElement.value +" will receive i:loans emails.\n";
					}
				}
			}
	
			// Warning if contract does not have a Client or TPA profile with Receive i:loans and TPA Staff Plan Access permission
			if (document.getElementsByName("tpaFirm[" + i + "].lastUserWithReceiveILoansEmailAndTPAStaffPlan")[0].value == "true") {
				var tpaStaffPlanValue = getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].tpaStaffPlanAccess");
				var receiveILoansEmailValue = getRadioValue("emailPreferences.receiveiLoads");
				var showiLoanEmail = emailValue;
				if (showiLoanEmail) {
					if (tpaStaffPlanValue == "false" || receiveILoansEmailValue=="false" || webAccessValue == "false") {
						warnings += "Warning! No users for firm " + tpaIdElement.value +" will receive i:loans emails and have TPA staff plan access permission.\n";
					}
				}
			}
	
			var reviewWDWContracts = document.getElementsByName("tpaFirm[" + i + "].lastUserWithReviewIWithdrawalsContracts")[0].value;
			if (reviewWDWContracts != null && reviewWDWContracts != "") {
				var reviewWithdrawals = getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].reviewIWithdrawals");
				if (reviewWithdrawals == "false" || webAccessValue == "false") {
					warnings += "Warning! No users for contract(s) " + reviewWDWContracts +" will have Review i:withdrawals permission.\n";
				}
			}
			
			// last user with review Loans
			var reviewLoansContracts = document.getElementsByName("tpaFirm[" + i + "].lastUserWithReviewLoansContracts")[0].value;
			if (reviewLoansContracts != null && reviewLoansContracts != "") {
				var reviewLoans = getRadioOrHiddenValue("tpaFirm[" + i + "].contractAccesses[0].reviewLoans");
				if (reviewLoans == "false" || webAccessValue == "false") {
					warnings += "Warning! No users for contract(s) " + reviewLoansContracts +" will have Review loans permission.\n";
				}
			}

			// Approve withdrawals -> Signing Authority : Loans project
			var signingAuthorityContracts = document.getElementsByName("tpaFirm[" + i + "].lastUserWithSigningAuthorityContracts")[0].value;
			if (signingAuthorityContracts != null && signingAuthorityContracts != "") {
				var signingAuthority = document.getElementsByName("tpaFirm[" + i + "].contractAccesses[0].userPermissions.signingAuthority")[0].value;
				if (signingAuthority == "false" || webAccessValue == "false") {
					warnings += "Warning! No users for contract(s) " + signingAuthorityContracts +" will have Signing Authority permission.\n";
				}
			}
		}
	}
	return warnings;
}

function getRadioOrHiddenValue(elementName) {
		var elementValue;
		var elements = document.getElementsByName(elementName);
		if (elements.length == 1) {
			elementValue = elements[0].value;
		} else {
			elementValue = getRadioValue(elementName);
		}
		return elementValue;
}

function getRadioValue(radioName) {
	var radioButtons = document.getElementsByName(radioName);
	if (radioButtons != null && radioButtons.length > 0) {
		var i=0;
		while (i < radioButtons.length && !radioButtons[i].checked) {
			i++;
		}
		return radioButtons[i].value;
	}
}

function doRefresh() {
	if (!submitted) {
		submitted = true;
		// STA.17 re-eval email display rules.(redo via css if possible)
		unhookWarning();
		document.addEditUserForm.action='${actionPath}?action=reload';
		document.addEditUserForm.submit();
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	}
}

function doChangePermissions(firmNumber){
	if (!submitted) {
		submitted = true;
		document.addEditUserForm.tpaFirmId.value = firmNumber;
		document.addEditUserForm.action.value = 'changePermissions';
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	}
}

function addTpa() {
	if (!submitted) {
		submitted = true;
		unhookWarning();
		document.addEditUserForm.action = '${actionPath}?action=addTpaFirm';
		document.addEditUserForm.submit();
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return;
	}
}

function doCancelChanges(theForm) {
	if (!submitted) {
		submitted = doCancel(theForm);
		return submitted;
	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function isFormChanged() {
	return changeTracker.hasChanged();
}

function clicked_checkbox(hidden, option) {
   if (option.checked) {
      hidden.value = "<%=Constants.YES%>";
   } else {
      hidden.value = "<%=Constants.NO%>";
   }
}


function toggleSection() {
	var section = document.all["prefs"].style;
	if (prefsOpen == false) {
		section.display = "block";
		prefsOpen = true;
		document.addEditUserForm.tabOpen.value  = "true";
	} else {
	    section.display = "none";
		prefsOpen = false;
		document.addEditUserForm.tabOpen.value ="false";
	}
}

function changeWebAccess() {
document.addEditUserForm.action.value = "changeWebAccess";
document.addEditUserForm.tabOpen.value ="false";
document.addEditUserForm.submit();

}

registerTrackChangesFunction(isFormChanged);


<%String warn = "nothing";
			if (request.getAttribute("WARNINGS") != null) {
				warn = (String) request.getAttribute("WARNINGS")
						+ "\\n\\nIgnore issues and continue with save?";%>

function doOnload() {

   var res = confirm("<%=warn%>");
   
   if (res == true) {
   	   document.addEditUserForm.ignoreWarnings.value = 'true';
   	   document.addEditUserForm.action.value = 'save';
   	   document.addEditUserForm.submit();
   }
}

<%}%>

function unhookWarning() {
  window.onunload=null;
}

window.onunload = function() {
// alert("Profile will remain locked.");
}

</script>



<logicext:if name="layoutBean" property="param(task)" op="equal"
	value="add">
	<logicext:then>
<c:set var="actionPath" value="/do/profiles/addTpaUser/"  />
	</logicext:then>
	<logicext:else>
<c:set var="actionPath" value="/do/profiles/editTpaUser/"  />
	</logicext:else>
</logicext:if>


<ps:form cssClass="margin-bottom:0;" method="POST" action="${actionPath}" onsubmit="unhookWarning()" name="addEditUserForm" modelAttribute="addEditUserForm">

<form:hidden path="removeTpaFirmId"/>
<%-- <form:hidden path="action"/>
<form:hidden path="removeTpaFirmId"/>
<form:hidden path="tabOpen"/>
<form:hidden path="ignoreWarnings"/>
<form:hidden path="fromTPAContactsTab"/>
 --%>
	<table width="700" border="0" cellspacing="0" cellpadding="0">

		<tr>
			<%-- error line --%>
			<td><strong><font color="#CC6600">*</font></strong> Required
				Information
				<p>
				<div id="errordivcs">
					<content:errors scope="session" />
				</div>
				</p></td>
		</tr>
		<tr>
			<td width="525"><img src="/assets/unmanaged/images/s.gif"
				width="525" height="1"></td>
		</tr>
		<tr>

			<td>

				<table>
				

					<tr>
						<td>
							<%-- main table, one on left of screen --%>
							<table width="425" border="0" cellpadding="0" cellspacing="0">
							
								<tr>
									<td width="1"><img src="/assets/unmanaged/images/s.gif"
										width="1" height="1"></td>
									<td width="113"><img src="/assets/unmanaged/images/s.gif"
										width="113" height="1"></td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif"
										width="1" height="1"></td>
									<td width="250"><img src="/assets/unmanaged/images/s.gif"
										width="250" height="1"></td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif"
										width="1" height="1"></td>
									<td width="80"><img src="/assets/unmanaged/images/s.gif"
										width="80" height="1"></td>
									<td width="4"><img src="/assets/unmanaged/images/s.gif"
										width="4" height="1"></td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif"
										width="1" height="1"></td>
								</tr>
								<tr class="tablehead">
									<td class="tableheadTD1" colspan="8"><b> <content:getAttribute
												id="layoutPageBean" attribute="body1Header" /></b></td>
								</tr>
								<tr class="datacell1">
									<td class="databorder"><img
										src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td colspan="6" align="center">
										<%-- user profile starts  --%>
										<c:if test="${userProfile.role.roleId eq 'PLC'}">
										<table width="100%" border="0" cellspacing="0" cellpadding="3">
										
<jsp:include page="viewProfileSectionMobile.jsp" flush="true" />
</table>
</c:if>
<c:if test="${userProfile.role.roleId ne 'PLC'}">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
											<tr class="datacell1">
												<td width="46%" valign="top"><ps:label
														fieldId="firstName" mandatory="true">First name</ps:label>
												</td>
<td valign="top">
<form:input path="firstName" maxlength="30" size="50" cssClass="inputField" value="${addEditUserForm.firstName}"/>
<ps:trackChanges name="addEditUserForm"

														property="firstName" /></td>
											</tr>
											<tr class="datacell1">
												<td width="46%" valign="top"><ps:label
														fieldId="lastName" mandatory="true">Last name</ps:label></td>
<td valign="top"><form:input path="lastName" maxlength="30" size="50" cssClass="inputField" value="${addEditUserForm.lastName}"/></td>

												<ps:trackChanges name="addEditUserForm" property="lastName" />
											</tr>

											<tr>
												<td><logicext:if name="addEditUserForm"
														property="webAccess" op="equal" value="true">
														<logicext:then>
															<ps:label fieldId="ssn" mandatory="true">Social security number
															</ps:label>
														</logicext:then>
														<logicext:else>
															<ps:label fieldId="ssn">Social security number</ps:label>
														</logicext:else>
													</logicext:if></td>
												<td><span class="bodytext"> <font
														face="Verdana, Arial, Geneva, sans-serif" size="1">
															<logicext:if name="layoutBean" property="param(task)"
																op="equal" value="add">
																<logicext:then>
																<c:if test="${userProfile.role.roleId eq 'ICC'}">
																			<form:password path="ssn.digits[0]"
																				showPassword="true" id="ssn1"
																				value="${addEditUserForm.ssn.digits[0]}"
																				cssClass="inputField" size="3" maxlength="3" />
																			<form:password path="ssn.digits[1]"
																				showPassword="true" id="ssn2"
																				value="${addEditUserForm.ssn.digits[1]}"
																				cssClass="inputField" size="2" maxlength="2" />
																			<form:input path="ssn.digits[2]" autocomplete="off"
																				cssClass="inputField" size="4" maxlength="4"
																				value="${addEditUserForm.ssn.digits[2]}" />
																		</c:if>
																		<c:if test="${userProfile.role.roleId ne 'ICC'}">
																			<form:password path="ssn.digits[0]"
																				value="${addEditUserForm.ssn.digits[0]}"
																				cssClass="inputField" size="3" maxlength="3" />
																			<form:password path="ssn.digits[1]"
																				value="${addEditUserForm.ssn.digits[1]}"
																				cssClass="inputField" size="2" maxlength="2" />
																			<form:password path="ssn.digits[2]"
																				value="${addEditUserForm.ssn.digits[2]}"
																				cssClass="inputField" size="4" maxlength="4"
																				value="${addEditUserForm.ssn.digits[2]}" />
																		</c:if>
																</logicext:then>
																<logicext:else>
																		<c:if test="${userProfile.role.roleId eq 'ICC'}">
																			<form:password path="ssn.digits[0]"
																				showPassword="true" id="ssn1"
																				value="${addEditUserForm.ssn.digits[0]}"
																				cssClass="inputField" size="3" maxlength="3" />
																			<form:password path="ssn.digits[1]"
																				showPassword="true" id="ssn2"
																				value="${addEditUserForm.ssn.digits[1]}"
																				cssClass="inputField" size="2" maxlength="2" />
																			<form:input path="ssn.digits[2]" autocomplete="off"
																				cssClass="inputField" size="4" maxlength="4"
																				value="${addEditUserForm.ssn.digits[2]}" />
																		</c:if>
																		<c:if test="${userProfile.role.roleId ne 'ICC'}">
																			<form:password path="ssn.digits[0]"
																				value="${addEditUserForm.ssn.digits[0]}"
																				cssClass="inputField" size="3" maxlength="3" />
																			<form:password path="ssn.digits[1]"
																				value="${addEditUserForm.ssn.digits[1]}"
																				cssClass="inputField" size="2" maxlength="2" />
																			<form:password path="ssn.digits[2]"
																				value="${addEditUserForm.ssn.digits[2]}"
																				cssClass="inputField" size="4" maxlength="4"
																				value="${addEditUserForm.ssn.digits[2]}" />
																		</c:if>


																	</logicext:else>
															</logicext:if>
													</font>
												</span></td>
												<ps:trackChanges name="addEditUserForm" property="ssn.digits[0]" />
												<ps:trackChanges name="addEditUserForm" property="ssn.digits[1]" />
												<ps:trackChanges name="addEditUserForm" property="ssn.digits[2]" />
											</tr>
											<tr class="datacell1">
												<td width="46%" valign="top"><ps:label
														fieldId="webAccess" mandatory="true">Web access</ps:label>
												</td>
												<td valign="top"><logicext:if name="addEditUserForm"
														property="fromTPAContactsTab" op="equal" value="true">
														<logicext:then>
<form:radiobutton disabled="true" onclick="changeWebAccess();" path="webAccess" value="true"/>Yes

<form:radiobutton disabled="true" onclick="changeWebAccess();" path="webAccess" value="false"/>No

   					</logicext:then>
														<logicext:else>
															<ps:isInternalUser name="userProfile" property="role">
<form:radiobutton onclick="changeWebAccess();" path="webAccess" value="true"/>Yes

<form:radiobutton onclick="changeWebAccess();" path="webAccess" value="false"/>No

		              		<ps:trackChanges name="addEditUserForm" property="webAccess" />
															</ps:isInternalUser>
															<ps:isTpa name="userProfile" property="role">
<form:radiobutton disabled="true" onclick="changeWebAccess();" path="webAccess" value="true"/>Yes

<form:radiobutton disabled="true" onclick="changeWebAccess();" path="webAccess" value="false"/>No

		              	</ps:isTpa>
														</logicext:else>
													</logicext:if></td>
											</tr>

											<tr class="datacell1">
												<td valign="top"><logicext:if name="addEditUserForm"
														property="webAccess" op="equal" value="true">
														<logicext:then>
															<ps:label fieldId="email" mandatory="true">Primary Email</ps:label>
														</logicext:then>
														<logicext:else>
															<ps:label fieldId="email">Primary Email</ps:label>
														</logicext:else>
													</logicext:if></td>
<td valign="top"><form:input path="email" size="50" cssClass="inputField" value="${addEditUserForm.email}"/> <ps:trackChanges name="addEditUserForm"

														property="email" /></td>
											</tr>
<c:if test="${not empty addEditUserForm.secondaryEmail}">

												<tr class="datacell1">
													<td valign="top"><ps:label fieldId="email">Secondary Email</ps:label>
													</td>
<td valign="top">${addEditUserForm.secondaryEmail}</td>

												</tr>
</c:if>
											<tr class="datacell1">
												<td valign="top"><ps:label fieldId="phone">Telephone number</ps:label>
												</td>
<td valign="top">
<c:if test="${addEditUserForm.profileStatus ne 'Registered'}">
<form:input path="phone.areaCode" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField" value="${addEditUserForm.phone.areaCode}"/> - 
<form:input path="phone.phonePrefix" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField" value="${addEditUserForm.phone.phonePrefix}"/> - 
<form:input path="phone.phoneSuffix" maxlength="4" onkeyup="return autoTab(this, 4, event);" size="4" cssClass="inputField" value="${addEditUserForm.phone.phoneSuffix}"/> ext
 <form:input path="ext" maxlength="8" onkeyup="return autoTab(this, 8, event);" size="8" cssClass="inputField" value="${addEditUserForm.ext}"/> 
 <ps:trackChanges name="addEditUserForm" property="phone.areaCode" /> 
 <ps:trackChanges name="addEditUserForm" property="phone.phonePrefix" /> 
 <ps:trackChanges name="addEditUserForm" property="phone.phoneSuffix" /> 
 <ps:trackChanges name="addEditUserForm" property="ext" />
 </c:if>
 <c:if test="${addEditUserForm.profileStatus eq 'Registered'}">
	<c:if test="${addEditUserForm.phone != ''}">
		<render:phone property="addEditUserForm.phone" />
 <c:if test="${not empty addEditUserForm.ext}">
		<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">ext.${addEditUserForm.ext}</c:if>
 </c:if>
 </c:if>
 </td>
											</tr>
											<tr class="datacell1">
												<td valign="top"><ps:label fieldId="fax">Fax number</ps:label>
												</td>
<td valign="top"><form:input path="fax.areaCode" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField"/> - <form:input path="fax.faxPrefix" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField"/> - <form:input path="fax.faxSuffix" maxlength="4" onkeyup="return autoTab(this, 4, event);" size="4" cssClass="inputField"/> <ps:trackChanges name="addEditUserForm" property="fax.areaCode" />

													<ps:trackChanges name="addEditUserForm" property="fax.faxPrefix" /> <ps:trackChanges name="addEditUserForm"
														property="fax.faxSuffix" /></td></tr>


												<c:if test="${layoutBean.getParam('task') =='edit'}">
													<tr class="datacell1">
														<td width="200" valign="top"><ps:label
																fieldId="mobileNumber" mandatory="false">Mobile number</ps:label>
														</td>
														<td colspan="2" valign="top">${addEditUserForm.mobile}</td>
													</tr>

													<ps:isTpa name="userProfile" property="role">
														<ps:permissionAccess permissions="TUMN">
															<tr class="datacell1">
																<td valign="top"><ps:label fieldId="userName">Username</ps:label>
																</td>
																<td valign="top">${addEditUserForm.userName}</td>

															</tr>
														</ps:permissionAccess>
													</ps:isTpa>
													<tr>
														<td><strong> Password status </strong></td>
														<td><ps:passwordState name="addEditUserForm"
																property="passwordState" /></td>
													</tr>

												</c:if>

												<tr id="addTpaFirmFeature">

												<ps:isNotTpaum name="userProfile" property="principal.role">
													<%-- internal gets a text entry box --%>
													<td valign="top"><c:if test="${layoutBean.getParam('task') =='edit'}">
															<ps:label fieldId="tpaFirmId">Add TPA firm ID</ps:label>
														</c:if> <c:if test="${layoutBean.getParam('task') !='edit'}">
															<ps:label fieldId="tpaFirmId" mandatory="true">Add TPA firm ID</ps:label>
														</c:if></td>
													<td valign="top"><form:input path="tpaFirmId"/> <a
														style="CURSOR: pointer; color: #000099; text-decoration: underline;"
														onclick="addTpa();">Add TPA firm</a></td>
												</ps:isNotTpaum>

												<%-- TPAUM  gets a drop down list if not all the firms are already displayed on the form--%>
												<ps:isTpaum name="userProfile" property="principal.role">
													<c:if test="${not empty firmList}">
															<td valign="top">
															<c:if test="${layoutBean.getParam('task') == 'edit'}">
															
																	<ps:label fieldId="tpaFirmId">Add TPA firm ID</ps:label>
																</c:if> <c:if test="${layoutBean.getParam('task') != 'edit'}">
																	<ps:label fieldId="tpaFirmId" mandatory="true">Add TPA firm ID</ps:label>
																</c:if></td>
															<td valign="top">
															<form:select path="tpaFirmId">
																<form:options items="${firmList}" itemValue="value" itemLabel="label" />
															
															</form:select> <a
																style="CURSOR: pointer; color: #000099; text-decoration: underline;"
																onclick="addTpa();">Add TPA firm</a></td>
																</c:if>
												</ps:isTpaum>


											</tr>
											<tr>
												<td></td>
												<logicext:if name="addEditUserForm" property="webAccess"
													op="equal" value="true">
													<logicext:then>
														<!--  <td align="right" onclick="toggleSection();"><a
															href="#">Email Preferences</a>&nbsp;<img
															src="/assets/unmanaged/images/layer_icon.gif" width="17"
															height="9"></td>	-->
															
															
															<td align="right">&nbsp;</td>
													</logicext:then>

												</logicext:if>
											</tr>

										</table>
										</c:if>
									</td>
									<td class="databorder"><img
										src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								</tr>

								<%
									// signal for java to gen warning on cancel
									if (addEditUserForm.isGenerateChangeTrackingMessage()) {
									
								%> 
								<input type="hidden" name="warningFlag" value="notBogus" />
								<script type="text/javascript" > changeTracker.trackElement('warningFlag','bogus'); </script>
								<%
									} %>
							
<c:if test="${userProfile.role.roleId ne 'PLC'}">
<c:if test="${not empty addEditUserForm.undeletedTpaFirms}">
										<jsp:include page="addEditTpaUserAccessSection.jsp"
										flush="true" />
</c:if>
</c:if>
<c:if test="${userProfile.role.roleId eq 'PLC'}">
<jsp:include page="viewTpaUserAccessSectionNonCollapsibleMobile.jsp"
										flush="true" />

</c:if>

								<ps:roundedCorner numberOfColumns="8" emptyRowColor="white"
									oddRowColor="beige" evenRowColor="white" name="addEditUserForm"
									property="visibleTpaFirms" />

							</table>

						</td>
						<td>&nbsp;</td>
						<td valign="top"><jsp:include
								page="addEditEmailPreferences.jsp" flush="true" /></td>
					</tr>

				</table>


				<p>
				<table width="454" border="0" cellspacing="0" cellpadding="0">
					<tr>

<td align="left"><input type="submit" class="button100Lg" onclick="return doCancelChanges('addEditUserForm');" name="action" value="cancel" /></td>
<td align="right"><input type="submit" class="button100Lg" onclick="return doSaveUser();" name="action" value="save" /></td>


						<script type="text/javascript">
			var onenter = new OnEnterSubmit('action', 'save');
			onenter.install();
			</script></td>
					</tr>
				</table>
				</p>
			</td>
		</tr>
	</table>

</ps:form>

<script type="text/javascript" >
 document.forms["addEditUserForm"].firstName.focus();
<c:if test="${addEditUserForm.fromTPAContactsTab ==true}">
 <logicext:if name="layoutBean" property="param(task)" op="equal" value="edit">
	<logicext:then>
		document.getElementById("addTpaFirmFeature").style.display = 'none';
	</logicext:then>
 </logicext:if>
</c:if>
</script>
