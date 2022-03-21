<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.profiles.BankAccount" %>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ page
	import="com.manulife.pension.service.security.valueobject.DefaultRolePermissions"%>
<%@ page
	import="com.manulife.pension.ps.web.profiles.UserPermissionsForm"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.profiles.UserPermissions" %>
<content:contentBean
	contentId="<%=ContentConstants.USER_MANAGEMENT_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitleUserManagement" />

<content:contentBean
	contentId="<%=ContentConstants.REPORTING_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitleReporting" />

<content:contentBean
	contentId="<%=ContentConstants.CLIENT_SERVICES_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitleClientServices" />

<content:contentBean
	contentId="<%=ContentConstants.WARNING_ALL_DIRECT_DEBIT_ACCOUNTS_SELECTED%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>"
	id="warningAllDirectDebitAccountsSelected" />

<content:contentBean
	contentId="<%=ContentConstants.WARNING_REMOVE_DIRECT_DEBIT_PERMISSION_REMOVE_DIRECT_DEBIT_ACCOUNTS%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>"
	id="warningRemoveSelectedDirectDebitAccounts" />

<content:contentBean
	contentId="<%=ContentConstants.WARNING_REMOVE_HIDDEN_DIRECT_DEBIT_ACCOUNT%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>"
	id="warningRemoveHiddenDirectDebitAccounts" />

<content:contentBean
	contentId="<%=ContentConstants.WARNING_SUBMISSION_ACCESS_ONLY%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>"
	id="warningSubmissionAccessOnly" />

<content:contentBean
	contentId="<%=ContentConstants.WARNING_NO_DIRECT_DEBIT_ACCOUNT_SELECTED%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>"
	id="warningNoSelectedDirectDebitAccounts" />

<content:contentBean
	contentId="<%=ContentConstants.WARNING_NO_DIRECT_DEBIT_ACCOUNT_AVAILABLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>"
	id="warningNoAvailableDirectDebitAccounts" />
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
UserPermissionsForm userPermissionsForm = (UserPermissionsForm)session.getAttribute("userPermissionsForm");
pageContext.setAttribute("userPermissionsForm",userPermissionsForm,PageContext.PAGE_SCOPE);
UserPermissions userPermissions= userPermissionsForm.getUserPermissions();
pageContext.setAttribute("userPermissions",userPermissions,PageContext.PAGE_SCOPE);
%>


<script type="text/javascript" >
	var submitted = false;

	selectedAccessFields=new Array();

	if (window.addEventListener) {
		window.addEventListener('load', initPage, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', initPage);
	}

	function initPage() {
		var inc=0
		var alltags=document.all? document.all : document.getElementsByTagName("*")
		for (i=0; i<alltags.length; i++){
			if (alltags[i].className=="selectedAccessField")
				selectedAccessFields[inc++]=alltags[i]
		}

		var selectedAccessValue = getRadioValue("userPermissions.selectedAccess");
		suppressSelectedAccessFields(selectedAccessValue);

		if (getRadioValue("userPermissions.viewSubmissions") == "false") {
			disableSubmissions();
		} else {
			if (getRadioValue("userPermissions.directDebit") == "false") {
				disableDirectDebitAccounts("false");
			}
		}

		if (getRadioValue("userPermissions.viewAllWithdrawals") == "false") {
			disableWithdrawals();
		}
	}

	function suppressSelectedAccessFields(suppress) {
		inc=0

		while (selectedAccessFields[inc]){
			if (suppress == "true") {
				selectedAccessFields[inc].style.display="none";
			} else {
				selectedAccessFields[inc].style.display="block";
			}
			inc++
		}

		if (suppress == "true") {
			var defaultValue;

defaultValue = "${userPermissions.manageUsersDefault}";
			setRadioValue("userPermissions.manageUsers", "false", defaultValue);

defaultValue = "${userPermissions.downloadReportsDefault}";
			setRadioValue("userPermissions.downloadReports", "false", defaultValue);

defaultValue = "${userPermissions.employerStatementsDefault}";
			setRadioValue("userPermissions.employerStatements", "false", defaultValue);

defaultValue = "${userPermissions.viewAllUsersSubmissionsDefault}";
			setRadioValue("userPermissions.viewAllUsersSubmissions", "false", defaultValue);

defaultValue = "${userPermissions.updateCensusDataDefault}";
			setRadioValue("userPermissions.updateCensusData", "false", defaultValue);

defaultValue = "${userPermissions.viewSalaryDefault}";
			setRadioValue("userPermissions.viewSalary", "false", defaultValue);

defaultValue = "${userPermissions.noticeManagerDefault}";
			setRadioValue("userPermissions.noticeManager", "false", defaultValue);
		}

		<% if (!userPermissions.isShowSubmissionsSection() && !userPermissions.isShowWithdrawalsSection() && userPermissions.isShowEmployeeManagementSection()) {%>
		var clientServicesSection = document.getElementById("clientServicesDIV");
		if (suppress == "true") {
			clientServicesSection.style.display="none";
		} else {
			clientServicesSection.style.display="block";
		}
		<% } %>
	}

	function setSubmissions() {
		if (getRadioValue("userPermissions.viewSubmissions") == "false") {
			// If "View Submissions" is changed to no, disable and set all submission permissions to no
			disableSubmissions();
		} else {
			// If "View Submissions" is changed to yes enable submission permissions and, if business converted, set to the defaults, otherwise set to values below
			enableSubmissions();
		}
	}

	function setWithdrawals() {
		if (getRadioValue("userPermissions.viewAllWithdrawals") == "false") {
			// If "View all i:withdrawals" is set to no, disable and set Review and Approve permissions to no
			disableWithdrawals();
		} else {
			// If "View all i:withdrawals" is set to yes, enable and set Review and Approve permissions to defaults
			enableWithdrawals();
		}
	}

	function disableSubmissions() {
		var defaultValue;

		if (disableDirectDebitAccounts("false")) {
defaultValue = "${userPermissions.createUploadSubmissionsDefault}";
			setRadioValue("userPermissions.createUploadSubmissions", "false", defaultValue);
			disableRadio("userPermissions.createUploadSubmissions", true, defaultValue);

defaultValue = "${userPermissions.viewAllUsersSubmissionsDefault}";
			setRadioValue("userPermissions.viewAllUsersSubmissions", "false", defaultValue);
			disableRadio("userPermissions.viewAllUsersSubmissions", true, defaultValue);

defaultValue = "${userPermissions.cashAccountDefault}";
			setRadioValue("userPermissions.cashAccount", "false", defaultValue);
			disableRadio("userPermissions.cashAccount", true, defaultValue);

defaultValue = "${userPermissions.directDebitDefault}";
			setRadioValue("userPermissions.directDebit", "false", defaultValue);
			disableRadio("userPermissions.directDebit", true, defaultValue);
		} else {
defaultValue = "${userPermissions.viewSubmissionsDefault}";
			setRadioValue("userPermissions.viewSubmissions", "true", defaultValue);
		}
	}

	function enableSubmissions() {
		var defaultValue;

defaultValue = "${userPermissions.createUploadSubmissionsDefault}";
<c:if test="${userPermissionsForm.businessConverted ==true}">
			setRadioValue("userPermissions.createUploadSubmissions", getBooleanValue(defaultValue), defaultValue);
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">
			setRadioValue("userPermissions.createUploadSubmissions", "true", defaultValue);
</c:if>
		disableRadio("userPermissions.createUploadSubmissions", false, defaultValue);

defaultValue = "${userPermissions.viewAllUsersSubmissionsDefault}";
		if (getRadioValue("userPermissions.selectedAccess") == "true") {
			setRadioValue("userPermissions.viewAllUsersSubmissions", "false", defaultValue);
		} else {
<c:if test="${userPermissionsForm.businessConverted ==true}">
			setRadioValue("userPermissions.viewAllUsersSubmissions", getBooleanValue(defaultValue), defaultValue);
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">
			setRadioValue("userPermissions.viewAllUsersSubmissions", "true", defaultValue);
</c:if>
		}
		disableRadio("userPermissions.viewAllUsersSubmissions", false, defaultValue);

defaultValue = "${userPermissions.cashAccountDefault}";
<c:if test="${userPermissionsForm.businessConverted ==true}">
			setRadioValue("userPermissions.cashAccount", getBooleanValue(defaultValue), defaultValue);
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">
			setRadioValue("userPermissions.cashAccount", "true", defaultValue);
</c:if>
		disableRadio("userPermissions.cashAccount", false, defaultValue);

defaultValue = "${userPermissions.directDebitDefault}";
<c:if test="${userPermissionsForm.businessConverted ==true}">
			setRadioValue("userPermissions.directDebit", getBooleanValue(defaultValue), defaultValue);
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">
			setRadioValue("userPermissions.directDebit", "false", defaultValue);
</c:if>
		disableRadio("userPermissions.directDebit", false, defaultValue);

		if (getRadioValue("userPermissions.directDebit") == "true") {
			disableDirectDebitAccounts("true");
		}
	}

	function disableWithdrawals() {
		var defaultValue;

defaultValue = "${userPermissions.reviewWithdrawalsDefault}";
		setRadioValue("userPermissions.reviewWithdrawals", "false", defaultValue);
		disableRadio("userPermissions.reviewWithdrawals", true, defaultValue);
	}

	function enableWithdrawals() {
		var defaultValue;

defaultValue = "${userPermissions.reviewWithdrawalsDefault}";
		setRadioValue("userPermissions.reviewWithdrawals", getBooleanValue(defaultValue), defaultValue);
		disableRadio("userPermissions.reviewWithdrawals", false, defaultValue);
	}

	function disableDirectDebit(value) {
		if (!disableDirectDebitAccounts(value)) {
var defaultValue = "${userPermissions.directDebitDefault}";
			var resetValue = (value == "true" ? "false" : "true");
			setRadioValue("userPermissions.directDebit", resetValue, defaultValue);
		}
	}

	function disableDirectDebitAccounts(value) {
		allowAction = true;
		var directDebitElements = document.getElementsByName("userPermissions.selectedDirectDebitAccounts");
		var directDebitCheckBoxes = new Array();
		var directDebitHiddens = new Array();
		for (var i = 0; i < directDebitElements.length; i++) {
			if (directDebitElements[i].type == "checkbox") {
				directDebitCheckBoxes.push(directDebitElements[i]);
			} else {
				directDebitHiddens.push(directDebitElements[i]);
			}
		}
		if (directDebitCheckBoxes.length > 0) {
			if (value == "true") {
				// If direct debit is set to yes, display warning confirmation, then set all direct debit accounts to enabled and checked
				<%for (int i = 0; i < userPermissions.getDirectDebitAccounts().size(); i++) {
				if (!userPermissions.getDirectDebitAccounts().get(i)
						.isNoAccess()) {%>
				allowAction = confirm("<content:getAttribute beanName="warningAllDirectDebitAccountsSelected" attribute="text"/>");
						<%break;
				}
			}%>
				if (allowAction) {
					var checkboxIndex = 0;
					<%for (int i = 0; i < userPermissions.getDirectDebitAccounts().size(); i++) {
				if (!userPermissions.getDirectDebitAccounts().get(i)
						.isNoAccess()) {%>
							directDebitCheckBoxes[checkboxIndex].checked = true;
							directDebitCheckBoxes[checkboxIndex].disabled = false;
						<%}%>
						checkboxIndex++;
					<%}%>
				}
			} else {
				// If direct debit is set to no, display warning confirmation, then set all direct debit accounts to disabled and unchecked
				if (isCheckboxSelected(directDebitCheckBoxes)) {
					var warnings = "<content:getAttribute beanName="warningRemoveSelectedDirectDebitAccounts" attribute="text"/>";
					for (var i = 0; i < directDebitCheckBoxes.length; i++) {
						if (directDebitCheckBoxes[i].disabled && directDebitCheckBoxes[i].checked) {
							warnings += "\n<content:getAttribute beanName="warningRemoveHiddenDirectDebitAccounts" attribute="text"/>";
							break;
						}
					}
					allowAction = confirm(warnings);
				}
				if (allowAction) {
					for (var i = 0; i < directDebitCheckBoxes.length; i++) {
						directDebitCheckBoxes[i].checked = false;
						directDebitCheckBoxes[i].disabled = true;
					}
					for (var i = 1; i < directDebitHiddens.length; i++) {
						directDebitHiddens[i].value = "";
					}
				}
			}
		}
		return allowAction;
	}

	function getBooleanValue(value) {
		return (value == "<%=DefaultRolePermissions.TRUE%>" || value == "<%=DefaultRolePermissions.YES%>") ? "true" : "false";
	}

	function isCheckboxSelected(checkBoxes) {
		var checkboxSelected = false;
		for (var i = 0; i < checkBoxes.length; i++) {
			if (checkBoxes[i].checked && checkBoxes[i].value != "<%=userPermissions.NO_DIRECT_DEBIT_ACCOUNT%>") {
				checkboxSelected = true;
				break;
			}
		}
		return checkboxSelected;
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

	function setRadioValue(radioName, newValue, defaultValue) {
		if (defaultValue != "<%=DefaultRolePermissions.TRUE%>" && defaultValue != "<%=DefaultRolePermissions.NOT_APPLICABLE%>") {
			var radios = document.getElementsByName(radioName);
			for (var i = 0; radios != null && i < radios.length; ++i) {
				if (radios[i].name == radioName) {
					radios[i].checked = (radios[i].value == newValue);
				}
			}
		}
	}

	function disableRadio(radioName, disable, defaultValue) {
		if (defaultValue != "<%=DefaultRolePermissions.TRUE%>" && defaultValue != "<%=DefaultRolePermissions.NOT_APPLICABLE%>
	") {
			var radioButtons = document.getElementsByName(radioName);
			for (var i = 0; radioButtons != null && i < radioButtons.length; i++) {
				radioButtons[i].disabled = disable;
			}
		}
	}

	function doContinue(theForm) {
		if (!submitted) {
			var warningMessage = "";

			// Warning if all permissions are set to "no" (if internal there's a warning if manage users = yes or no and all others are no)
			var internalUser = false;
<c:if test="${userProfile.internalUser ==true}">
			internalUser = true;
</c:if>
			// Changing the approveWithdrawals to signingAuthority : Loans project
			if ((internalUser || getRadioValue("userPermissions.manageUsers") != "true")
					&& getRadioValue("userPermissions.selectedAccess") != "true"
					&& getRadioValue("userPermissions.editContractServiceFeatures") != "true"
					&& getRadioValue("userPermissions.downloadReports") != "true"
					&& getRadioValue("userPermissions.employerStatements") != "true"
					&& getRadioValue("userPermissions.viewPlanData") != "true"
					&& getRadioValue("userPermissions.viewSubmissions") != "true"
					&& getRadioValue("userPermissions.createUploadSubmissions") != "true"
					&& getRadioValue("userPermissions.viewAllUsersSubmissions") != "true"
					&& getRadioValue("userPermissions.cashAccount") != "true"
					&& getRadioValue("userPermissions.directDebit") != "true"
					&& getRadioValue("userPermissions.initiateAndViewMyWithdrawals") != "true"
					&& getRadioValue("userPermissions.viewAllWithdrawals") != "true"
					&& getRadioValue("userPermissions.reviewWithdrawals") != "true"
					&& getRadioValue("userPermissions.signingAuthority") != "true"
					&& getRadioValue("userPermissions.updateCensusData") != "true"
					&& getRadioValue("userPermissions.viewAllLoans") != "true"
					&& getRadioValue("userPermissions.initiateLoans") != "true"
					&& getRadioValue("userPermissions.reviewLoans") != "true"
					&& getRadioValue("userPermissions.viewSalary") != "true"
					&& getRadioValue("userPermissions.noticeManager") != "true") {
				// Content for this?
				warningMessage += "Warning! You have not granted any permissions for this user.\n";
			}

			// Warning if View Submissions is selected but no sub-pemissions
			if (getRadioValue("userPermissions.viewSubmissions") == "true"
					&& getRadioValue("userPermissions.createUploadSubmissions") != "true"
					&& getRadioValue("userPermissions.viewAllUsersSubmissions") != "true"
					&& getRadioValue("userPermissions.cashAccount") != "true"
					&& getRadioValue("userPermissions.directDebit") != "true") {
				warningMessage += "<content:getAttribute beanName="warningSubmissionAccessOnly" attribute="text"/>\n";
			}

			// Warning if Direct debit = "yes" but no accounts selected
			if (getRadioValue("userPermissions.directDebit") == "true") {
				directDebitCheckBoxes = document
						.getElementsByName("userPermissions.selectedDirectDebitAccounts");
				if (directDebitCheckBoxes != null
						&& directDebitCheckBoxes.length > 0) {
					if (!isCheckboxSelected(directDebitCheckBoxes)) {
						warningMessage += "<content:getAttribute beanName="warningNoSelectedDirectDebitAccounts" attribute="text"/>\n";
					}
				} else {
					warningMessage += "<content:getAttribute beanName="warningNoAvailableDirectDebitAccounts" attribute="text"/>\n";
				}
			}

			// Warning if contract does not have a Client profile with web access = yes, selected access = no and manage users = yes
<c:if test="${userPermissions.showManageUsers ==true}">
			if (document.userPermissionsForm.lastUserWithManageUsers.value == "true"
					&& (getRadioValue("userPermissions.manageUsers") == "false" || getRadioValue("userPermissions.selectedAccess") == "true")) {
				warningMessage += "Warning! No client users for this contract will have Manage users permission.\n";
			}
</c:if>

			// Warning if contract does not have a Client or TPA profile with submissions access permission
<c:if test="${userPermissions.showViewSubmissions ==true}">
			if (document.userPermissionsForm.lastUserWithSubmissionsAccess.value == "true"
					&& (getRadioValue("userPermissions.viewSubmissions") == "false" || getRadioValue("userPermissions.selectedAccess") == "true")) {
				warningMessage += "Warning! No client users for this contract will have Submissions Access permission.\n";
			}
</c:if>

			// Warning if contract does not have a Client or TPA profile with signing authority (approve i:withdrawals @before) permission
			// Changing the approveIWithdrawal to signingAuthority : Loans project
<c:if test="${userPermissions.showSigningAuthority ==true}">
			if (document.userPermissionsForm.lastUserWithSigningAuthority.value == "true"
					&& (getRadioValue("userPermissions.signingAuthority") == "false" || getRadioValue("userPermissions.selectedAccess") == "true")) {
				warningMessage += "Warning! No client users for this contract will have Signing Authority permission.\n";
			}
</c:if>

			// Warning if contract does not have a Client or TPA profile with review i:withdrawals permission
			// This will happen when who will review withdrawal request = "TPA"
<c:if test="${userPermissions.showReviewWithdrawals ==true}">
			if (document.userPermissionsForm.lastUserWithReviewIWithdrawals.value == "true"
					&& (getRadioValue("userPermissions.reviewWithdrawals") == "false" || getRadioValue("userPermissions.selectedAccess") == "true")) {
				warningMessage += "Warning! No client users for this contract will have Review i:withdrawals permission.\n";
			}
</c:if>

			// Warning if contract does not have a Client or TPA profile with review i:withdrawals permission
			// This will happen when who will review withdrawal request = "TPA"
<c:if test="${userPermissions.showReviewWithdrawals ==true}">
			if (document.userPermissionsForm.lastClientUserWithReviewIWithdrawals.value == "true"
					&& (getRadioValue("userPermissions.reviewWithdrawals") == "false" || getRadioValue("userPermissions.selectedAccess") == "true")) {
				warningMessage += "Warning! No client users for this contract will have Review i:withdrawals permission.\n";
			}
</c:if>

			// Warning if contract does not have a Client or TPA profile with review loans permission
			// This will happen when who will review loan requests = "TPA"
<c:if test="${userPermissions.showReviewLoans ==true}">
			if (document.userPermissionsForm.lastUserWithReviewLoans.value == "true"
					&& (getRadioValue("userPermissions.reviewLoans") == "false" || getRadioValue("userPermissions.selectedAccess") == "true")) {
				warningMessage += "Warning! No client users for this contract will have Review loans permission.\n";
			}
</c:if>

			// Warning if contract does not have a Client profile with review loans permission 
			// This will happen when who will review loan requests = "Plan Sponsor"
<c:if test="${userPermissions.showReviewLoans ==true}">
			if (document.userPermissionsForm.lastClientUserWithReviewLoans.value == "true"
					&& (getRadioValue("userPermissions.reviewLoans") == "false" || getRadioValue("userPermissions.selectedAccess") == "true")) {
				warningMessage += "Warning! No client users for this contract will have Review loans permission.\n";
			}
</c:if>

			if (warningMessage != "") {
				submitted = confirm(warningMessage
						+ "Do you want to continue with these changes?");
			} else {
				submitted = true;
			}

			return submitted;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
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
		// Need to update the hidden direct debit account field
		directDebitCheckBoxes = document
				.getElementsByName("userPermissions.selectedDirectDebitAccounts");
		var selectedIds = "";
		if (directDebitCheckBoxes != null && directDebitCheckBoxes.length > 0) {
			for (var i = 0; i < directDebitCheckBoxes.length; i++) {
				if (directDebitCheckBoxes[i].checked) {
					selectedIds += directDebitCheckBoxes[i].value + ",";
				}
			}
			var selectedIdsField = document
					.getElementsByName("userPermissions.selectedDirectDebitAccountIdsAsString");
			selectedIdsField[0].value = selectedIds;
		}

		return changeTracker.hasChanged();
	}

	registerTrackChangesFunction(isFormChanged);
</script>

<ps:form method="POST" action="/do/profiles/userPermissions/" name="userPermissionsForm" modelAttribute="userPermissionsForm">

<form:hidden path="lastUserWithManageUsers"/>
<form:hidden path="lastUserWithReviewIWithdrawals"/>
<form:hidden path="lastClientUserWithReviewIWithdrawals"/>
<form:hidden path="lastUserWithReviewLoans"/>
<form:hidden path="lastClientUserWithReviewLoans"/>

	<!-- Changing the approveIWithdrawal to signingAuthority : Loans project -->
	<form:hidden path="lastUserWithSigningAuthority"/>
	<form:hidden path="lastUserWithSubmissionsAccess"/>

	<TABLE cellSpacing=0 cellPadding=0 width=760 border=0>
		<TBODY>
			<TR>
				<TD>
					<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
						<TBODY>
							<TR>
								<TD><IMG height=1 src="/assets/unmanaged/images/s.gif"
									width=30><BR />
								<IMG height=1 src="/assets/unmanaged/images/s.gif"></TD>
								<TD width=500><BR /> <jsp:include flush="true"
										page="userPermissionsHeader.jsp"></jsp:include> <BR />

									<TABLE class=box style="CURSOR: pointer"
										onclick="expandSection('sc1')" cellSpacing=0 cellPadding=0
										width=412 border=0>
										<TBODY>
											<TR class=tablehead>
												<TD class=tableheadTD1 colSpan=3><img id="sc1img"
													src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<b><content:getAttribute
															beanName="groupTitleUserManagement" attribute="text" /></b></TD>
											</TR>
										</TBODY>
									</TABLE>
									<DIV class=switchcontent id=sc1>
										<TABLE class=box cellSpacing=0 cellPadding=0 width=412
											border=0>
											<TBODY>
												<TR>
													<TD class=boxborder width=1><IMG height=1
														src="/assets/unmanaged/images/s.gif" width=1></TD>
													<TD>

														<DIV class=selectedAccessField>
															<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
																<TBODY>
<c:if test="${userPermissions.showManageUsers ==true}">

																		<tr valign="top" class="datacell2">
																			<td width="219"><strong>Manage users </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.manageUsersDefault == 'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.manageUsers" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.manageUsers" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.manageUsersDefault != 'TRUE'}">


<c:if test="${userPermissions.manageUsersDefault == 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.manageUsers" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.manageUsers" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.manageUsersDefault != 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.manageUsers" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.manageUsers" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.manageUsers" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.manageUsers" value="false" /> no</td>



</c:if>
																			<ps:trackChanges name ="userPermissionsForm"
																				property="userPermissions.manageUsers" />
																		</tr>
																</TBODY>
</c:if>
															</TABLE>
														</DIV>

														<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
															<TBODY>
<c:if test="${userPermissions.showSelectedAccess ==true}">

																	<tr valign="top" class="datacell2">
																		<td width="219"><strong>Selected access
																		</strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.selectedAccessDefault == 'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.selectedAccess" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.selectedAccess" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.selectedAccessDefault != 'TRUE'}">


<c:if test="${userPermissions.selectedAccessDefault == 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="suppressSelectedAccessFields(this.value)" path="userPermissions.selectedAccess" value="true" /><b><u>




																								yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="suppressSelectedAccessFields(this.value)" path="userPermissions.selectedAccess" value="false" />




																						no</td>
</c:if>
<c:if test="${userPermissions.selectedAccessDefault != 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="suppressSelectedAccessFields(this.value)" path="userPermissions.selectedAccess" value="true" />




																						yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="suppressSelectedAccessFields(this.value)" path="userPermissions.selectedAccess" value="false" /><b><u>




																								no</u></b></td>
</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="suppressSelectedAccessFields(this.value)" path="userPermissions.selectedAccess" value="true" />




																				yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="suppressSelectedAccessFields(this.value)" path="userPermissions.selectedAccess" value="false" />




																				no</td>
</c:if>
																		<ps:trackChanges  name ="userPermissionsForm"
																			property="userPermissions.selectedAccess" />
																	</tr>
</c:if>
															</TBODY>
														</TABLE>

													</TD>
													<TD class=boxborder width=1><IMG height=1src=
														"/assets/unmanaged/images/s.gif" width=1></TD>
												</TR>
												<TR>
													<TD colSpan=3>
														<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
															<TBODY>
																<TR>
																	<TD width="1" class=boxborder><IMG height=1
																		src="/assets/unmanaged/images/s.gif" width=1></TD>
																	<TD class=boxborder><IMG height=1
																		src="/assets/unmanaged/images/s.gif" width=1></TD>
																</TR>
															</TBODY>
														</TABLE>
													</TD>
												</TR>
											</TBODY>
										</TABLE>
</DIV>&nbsp;<BR /> <c:if test="${userPermissions.showReportingSection ==true}">

										<DIV class=selectedAccessField>
											<TABLE class=box style="CURSOR: pointer"
												onclick="expandSection('sc2')" cellSpacing=0 cellPadding=0
												width=412 border=0>
												<TBODY>
													<TR class=tablehead>
														<TD class=tableheadTD1 colSpan=3><img id="sc2img"
															src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<B><content:getAttribute
																	beanName="groupTitleReporting" attribute="text" /></B></TD>
													</TR>
												</TBODY>
											</TABLE>
											<DIV class=switchcontent id=sc2>
												<TABLE class=box cellSpacing=0 cellPadding=0 width=412
													border=0>
													<TBODY>
														<TR>
															<TD class=boxborder width=1><IMG height=1
																src="/assets/unmanaged/images/s.gif" width=1></TD>
															<TD width=644><TABLE cellSpacing=0 cellPadding=0
																	width=410 border=0>
																	<TBODY>

<c:if test="${userPermissions.showDownloadReports ==true}">

																			<tr valign="top" class="datacell2">
																				<td width="219"><strong>Download
																						reports - full SSN </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.downloadReportsDefault == 'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.downloadReports" value="true" /><b><u>



																									yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.downloadReports" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.downloadReportsDefault != 'TRUE'}">


<c:if test="${userPermissions.downloadReportsDefault == 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.downloadReports" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.downloadReports" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.downloadReportsDefault != 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.downloadReports" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.downloadReports" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.downloadReports" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.downloadReports" value="false" /> no</td>



</c:if>
																				<ps:trackChanges name="userPermissionsForm"
																					property="userPermissions.downloadReports" />
																			</tr>
</c:if>

<c:if test="${userPermissions.showEmployerStatements ==true}">

																			<tr valign="top" class="datacell2">
																				<td width="219"><strong>Employer
																						statements </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.employerStatementsDefault == 'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.employerStatements" value="true" /><b><u>



																									yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.employerStatements" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.employerStatementsDefault != 'TRUE'}">


<c:if test="${userPermissions.employerStatementsDefault == 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.employerStatements" value="true" /> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.employerStatements" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.employerStatementsDefault != 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.employerStatements" value="true" />yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.employerStatements" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.employerStatements" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.employerStatements" value="false" /> no</td>



</c:if>
																				<ps:trackChanges name="userPermissionsForm"
																					property="userPermissions.employerStatements" />
																			</tr>
</c:if>

																	</TBODY>
																</TABLE></TD>
															<TD width=1 bgcolor="#D9DAE8" class=boxborder><IMG
																height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
														</TR>
														<TR>
															<TD colSpan=3>
																<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
																	<TBODY>
																		<TR>
																			<TD width="1" class=boxborder><IMG height=1
																				src="/assets/unmanaged/images/s.gif" width=1></TD>
																			<TD class=boxborder><IMG height=1
																				src="/assets/unmanaged/images/s.gif" width=1></TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
													</TBODY>
												</TABLE>
											</DIV>
											<BR />
										</DIV>
</c:if>

									<DIV id=clientServicesDIV>
<c:if test="${userPermissions.showClientServicesSection ==true}">

											<TABLE class=box style="CURSOR: pointer"
												onclick="expandSection('sc4')" cellSpacing=0 cellPadding=0
												width=412 border=0>
												<TBODY>
													<TR class=tablehead>
														<TD class=tableheadTD1 colSpan=3><img id="sc4img"
															src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<B><content:getAttribute
																	beanName="groupTitleClientServices" attribute="text" /></B></TD>
													</TR>
												</TBODY>
											</TABLE>
											<DIV class=switchcontent id=sc4>
												<TABLE class=box cellSpacing=0 cellPadding=0 width=412
													border=0>
													<TBODY>
														<TR>
															<TD class=boxborder width=1><IMG height=1
																src="/assets/unmanaged/images/s.gif" width=1></TD>
															<TD width=733><TABLE cellSpacing=0 cellPadding=0
																	width=410 border=0>
																	<TBODY>
																		<!-- Signing Authority introduction : Loans project  -->
<c:if test="${userPermissions.showSigningAuthority ==true}">

																			<tr valign="top" class="datacell2">
																				<td width="219"><strong>Signing
																						authority </strong></td>
<c:if test="${userPermissions.signingAuthorityDefault == 'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.signingAuthority" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.signingAuthority" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.signingAuthorityDefault != 'TRUE'}">


<c:if test="${userPermissions.signingAuthorityDefault == 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.signingAuthority" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.signingAuthority" value="false" />no</td>



</c:if>
<c:if test="${userPermissions.signingAuthorityDefault != 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.signingAuthority" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.signingAuthority" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
																				<ps:trackChanges name="userPermissionsForm"
																					property="userPermissions.signingAuthority" />
																			</tr>
</c:if>
																	</TBODY>
																</TABLE> <!-- Signing Authority introduction : Loans project - END -->
																<DIV class="selectedAccessField">
																	<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
																		<TBODY>
																			<!-- Signing Authority introduction : Loans project - END -->
<c:if test="${userPermissions.showNoticeManager ==true}">

																				<tr valign="top" class="datacell2">
																					<td width="219"><strong>Access to
																							Notice Manager </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.noticeManagerDefault == 'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.noticeManager" value="true" /><b><u>



																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.noticeManager" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.noticeManagerDefault !='TRUE'}">


<c:if test="${userPermissions.noticeManagerDefault == 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.noticeManager" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.noticeManager" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.noticeManagerDefault != 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.noticeManager" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.noticeManager" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.noticeManager" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.noticeManager" value="false" /> no</td>



</c:if>
																					<ps:trackChanges name="userPermissionsForm"
																						property="userPermissions.noticeManager" />
																				</tr>
</c:if>
																		</TBODY>
																	</TABLE>
																</DIV>
																<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
																	<TBODY>
<c:if test="${userPermissions.showSubmissionsSection ==true}">

																			<tr valign="top">
																				<td colspan="3" class="tablesubhead"><b>Submissions</b></td>
																			</tr>

<c:if test="${userPermissions.showViewSubmissions ==true}">

																				<tr valign="top" class="datacell2">
																					<td width="219"><strong>View
																							submissions </strong></td>

<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.viewSubmissionsDefault == 'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewSubmissions" value="true" /><b><u>



																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewSubmissions" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.viewSubmissionsDefault != 'TRUE'}">


<c:if test="${userPermissions.viewSubmissionsDefault == 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="setSubmissions()" path="userPermissions.viewSubmissions" value="true" /><b><u>



																											yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="setSubmissions()" path="userPermissions.viewSubmissions" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.viewSubmissionsDefault != 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="setSubmissions()" path="userPermissions.viewSubmissions" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="setSubmissions()" path="userPermissions.viewSubmissions" value="false" /><b><u>



																											no</u></b></td>
</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="setSubmissions()" path="userPermissions.viewSubmissions" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="setSubmissions()" path="userPermissions.viewSubmissions" value="false" /> no</td>



</c:if>
																					<ps:trackChanges name="userPermissionsForm"
																						property="userPermissions.viewSubmissions" />
																				</tr>
</c:if>

<c:if test="${userPermissions.showCreateUploadSubmissions ==true}">

																				<tr valign="top" class="datacell2">
																					<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Create/upload
																							submissions </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.createUploadSubmissionsDefault == 'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.createUploadSubmissions" value="true" /><b><u>



																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.createUploadSubmissions" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.createUploadSubmissionsDefault != 'TRUE'}">


<c:if test="${userPermissions.createUploadSubmissionsDefault == 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.createUploadSubmissions" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.createUploadSubmissions" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.createUploadSubmissionsDefault !=  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.createUploadSubmissions" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.createUploadSubmissions" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.createUploadSubmissions" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.createUploadSubmissions" value="false" /> no</td>



</c:if>
																					<ps:trackChanges name="userPermissionsForm"
																						property="userPermissions.createUploadSubmissions" />
																				</tr>
</c:if>
																	</TBODY>
																</TABLE>

																<DIV class=selectedAccessField>
																	<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
																		<TBODY>
<c:if test="${userPermissions.showViewAllUsersSubmissions ==true}">

																				<tr valign="top" class="datacell2">
																					<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;View
																							all users' submissions </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.viewAllUsersSubmissionsDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewAllUsersSubmissions" value="true" /><b><u>



																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewAllUsersSubmissions" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.viewAllUsersSubmissionsDefault != 'TRUE'}">


<c:if test="${userPermissions.manageUsersDefault == 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllUsersSubmissions" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllUsersSubmissions" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.manageUsersDefault !=  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllUsersSubmissions" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllUsersSubmissions" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllUsersSubmissions" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllUsersSubmissions" value="false" /> no</td>



</c:if>
																					<ps:trackChanges name="userPermissionsForm"
																						property="userPermissions.viewAllUsersSubmissions" />
																				</tr>
</c:if>
																		</TBODY>
																	</TABLE>
																</DIV>

																<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
																	<TBODY>
<c:if test="${userPermissions.showCashAccount ==true}">

																			<tr valign="top" class="datacell2">
																				<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Cash
																						account </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.cashAccountDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.cashAccount" value="true" /><b><u>



																									yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.cashAccount" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.cashAccountDefault !=  'TRUE'}">


<c:if test="${userPermissions.cashAccountDefault ==  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.cashAccount" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.cashAccount" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.cashAccountDefault != 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.cashAccount" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.cashAccount" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.cashAccount" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.cashAccount" value="false" /> no</td>



</c:if>
																				<ps:trackChanges name="userPermissionsForm"
																					property="userPermissions.cashAccount" />
																			</tr>
</c:if>

<c:if test="${userPermissions.showDirectDebit ==true}">

																			<tr valign="top" class="datacell2">
																				<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Direct
																						debit </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.directDebitDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.directDebit" value="true" /><b><u>



																									yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.directDebit" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.directDebitDefault !=  'TRUE'}">


<c:if test="${userPermissions.directDebitDefault ==  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="disableDirectDebit(this.value)" path="userPermissions.directDebit" value="true" /><b><u>




																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="disableDirectDebit(this.value)" path="userPermissions.directDebit" value="false" /> no</td>




</c:if>
<c:if test="${userPermissions.directDebitDefault != 'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="disableDirectDebit(this.value)" path="userPermissions.directDebit" value="true" /> yes</td>




<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="disableDirectDebit(this.value)" path="userPermissions.directDebit" value="false" /><b><u>




																										no</u></b></td>
</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="disableDirectDebit(this.value)" path="userPermissions.directDebit" value="true" /> yes</td>




<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="disableDirectDebit(this.value)" path="userPermissions.directDebit" value="false" /> no</b></td>




</c:if>
																				<ps:trackChanges name="userPermissionsForm"
																					property="userPermissions.directDebit" />
																			</tr>
																	</TBODY>
																</TABLE>

																<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
																	<TBODY>
<c:if test="${not empty userPermissions.directDebitAccounts}">

																			<tr valign="top" class="datacell2">
																				<td align="left" nowrap colspan="3">
																					<%--
		       			This hidden property is used to ensure that the browser sends back something
		       			for the checkboxes value even when none of the checkboxes is selected. Without
		       			this hidden property, the browser will not send anything back when no checkbox is
		       			selected.
--%> <input type="hidden" name="userPermissions.selectedDirectDebitAccounts" />


<input type="hidden" name="userPermissions.selectedDirectDebitAccountIdsAsString" /><%--  input - name="userPermissionsForm" --%>

																					<ps:trackChanges name="userPermissionsForm"
																						property="userPermissions.selectedDirectDebitAccountIdsAsString" />

<c:forEach items="${userPermissions.directDebitAccounts}" var="account" >
<%
							BankAccount checkValue=(BankAccount)pageContext.getAttribute("account");
							%>

							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <c:if test="${account.noAccess ==false}">
 <form:checkbox path="userPermissions.selectedDirectDebitAccounts" value="${ account.primaryKey}">
${account.primaryKey}
																							</form:checkbox>
</c:if>
<c:if test="${account.noAccess !=false}">

																							 <form:checkbox path="userPermissions.selectedDirectDebitAccounts" value="${ account.primaryKey}">
${account.primaryKey}
																							</form:checkbox>
																							<%
																								if (userPermissions.getSelectedDirectDebitAccountsAsList().contains(checkValue)) {
																							%>
<input type="hidden" name="userPermissions.selectedDirectDebitAccounts" />


																							<%
																								} else {
																							%>
<input type="hidden" name="userPermissions.selectedDirectDebitAccounts" />


																							<%
																								}
																							%>
</c:if> 
${account.label}
																						<br />
</c:forEach>

																				</td>
																			</tr>
</c:if>
																	</TBODY>
																</TABLE>

																<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
																	<TBODY>

</c:if>

</c:if>
<c:if test="${userPermissions.showLoansPermissions ==true}">

																			<tr valign="top">
																				<td colspan="3" class="tablesubhead"><b>Loans</b></td>
																			</tr>

<c:if test="${userPermissions.showInitiateLoans ==true}">

																				<tr valign="top" class="datacell2">
																					<td width="219"><strong>Initiate
																							loans </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.initiateLoansDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.initiateLoans" value="true" /><b><u>



																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.initiateLoans" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.initiateLoansDefault !=  'TRUE'}">


<c:if test="${userPermissions.initiateLoansDefault ==  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateLoans" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateLoans" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.initiateLoansDefault !=  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateLoans" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateLoans" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateLoans" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateLoans" value="false" /> no</td>



</c:if>
																					<ps:trackChanges name ="userPermissionsForm"
																						property="userPermissions.initiateLoans" />
																				</tr>
</c:if>

<c:if test="${userPermissions.showViewAllLoans ==true}">

																				<tr valign="top" class="datacell2">
																					<td width="219"><strong>View all
																							loans </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.viewAllLoansDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewAllLoans" value="true" /><b><u>



																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewAllLoans" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.viewAllLoansDefault !=  'TRUE'}">


<c:if test="${userPermissions.viewAllLoansDefault ==  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllLoans" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllLoans" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.viewAllLoansDefault !=  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllLoans" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllLoans" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllLoans" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllLoans" value="false" /> no</td>



</c:if>
																					<ps:trackChanges name ="userPermissionsForm" name="userPermissionsForm"
																						property="userPermissions.viewAllLoans" />
																				</tr>
</c:if>

<c:if test="${userPermissions.showReviewLoans ==true}">

																				<tr valign="top" class="datacell2">
																					<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review
																							loans </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.reviewLoansDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.reviewLoans" value="true" /><b><u>



																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.reviewLoans" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.reviewLoansDefault !=  'TRUE'}">


<c:if test="${userPermissions.reviewLoansDefault ==  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewLoans" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewLoans" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.reviewLoansDefault !=  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewLoans" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewLoans" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewLoans" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewLoans" value="false" /> no</td>



</c:if>
																					<ps:trackChanges name ="userPermissionsForm"
																						property="userPermissions.reviewLoans" />
																				</tr>
</c:if>

</c:if>

<c:if test="${userPermissions.showWithdrawalsSection ==true}">

																			<tr valign="top">
																				<td colspan="3" class="tablesubhead"><b>i:withdrawals</b></td>
																			</tr>

<c:if test="${userPermissions.showInitiateAndViewMyWithdrawals ==true}">


																				<tr valign="top" class="datacell2">
																					<td width="219"><strong>Initiate
																							i:withdrawals </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.initiateAndViewMyWithdrawalsDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.initiateAndViewMyWithdrawals" value="true" /><b><u>



																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.initiateAndViewMyWithdrawals" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.initiateAndViewMyWithdrawalsDefault !=  'TRUE'}">


<c:if test="${userPermissions.initiateAndViewMyWithdrawalsDefault ==  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateAndViewMyWithdrawals" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateAndViewMyWithdrawals" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.initiateAndViewMyWithdrawalsDefault !=  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateAndViewMyWithdrawals" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateAndViewMyWithdrawals" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateAndViewMyWithdrawals" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateAndViewMyWithdrawals" value="false" /> no</td>



</c:if>
																					<ps:trackChanges name ="userPermissionsForm"
																						property="userPermissions.initiateAndViewMyWithdrawals" />
																				</tr>
</c:if>

<c:if test="${userPermissions.showViewAllWithdrawals ==true}">

																				<tr valign="top" class="datacell2">
																					<td width="219"><strong>View all
																							i:withdrawals </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.viewAllWithdrawalsDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewAllWithdrawals" value="true" /><b><u>



																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewAllWithdrawals" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.viewAllWithdrawalsDefault !=  'TRUE'}">


<c:if test="${userPermissions.viewAllWithdrawalsDefault ==  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="setWithdrawals()" path="userPermissions.viewAllWithdrawals" value="true" /><b><u>



																											yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="setWithdrawals()" path="userPermissions.viewAllWithdrawals" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.viewAllWithdrawalsDefault !=  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="setWithdrawals()" path="userPermissions.viewAllWithdrawals" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="setWithdrawals()" path="userPermissions.viewAllWithdrawals" value="false" /><b><u>



																											no</u></b></td>
</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="setWithdrawals()" path="userPermissions.viewAllWithdrawals" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="setWithdrawals()" path="userPermissions.viewAllWithdrawals" value="false" /> no</td>



</c:if>
																					<ps:trackChanges name ="userPermissionsForm"
																						property="userPermissions.viewAllWithdrawals" />
																				</tr>
</c:if>

<c:if test="${userPermissions.showReviewWithdrawals ==true}">

																				<tr valign="top" class="datacell2">
																					<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review
																							i:withdrawals </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.reviewWithdrawalsDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.reviewWithdrawals" value="true" /><b><u>



																										yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.reviewWithdrawals" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.reviewWithdrawalsDefault != 'TRUE'}">


<c:if test="${userPermissions.reviewWithdrawalsDefault ==  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewWithdrawals" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewWithdrawals" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.reviewWithdrawalsDefault !=  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewWithdrawals" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewWithdrawals" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewWithdrawals" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.reviewWithdrawals" value="false" /> no</td>



</c:if>
																					<ps:trackChanges name ="userPermissionsForm"
																						property="userPermissions.reviewWithdrawals" />
																				</tr>
</c:if>

</c:if>
																	</TBODY>
</TABLE> <c:if test="${userPermissions.showEmployeeManagementSection ==true}">

																	<DIV class=selectedAccessField>
																		<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
																			<TBODY>
																				<tr valign="top">
																					<td colspan="3" class="tablesubhead"><b>Employee
																							Management</b></td>
																				</tr>

<c:if test="${userPermissions.showUpdateCensusData ==true}">

																					<tr valign="top" class="datacell2">
																						<td width="219"><strong>Update
																								census data </strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.updateCensusDataDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.updateCensusData" value="true" /><b><u>



																											yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.updateCensusData" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.updateCensusDataDefault !=  'TRUE'}">


<c:if test="${userPermissions.updateCensusDataDefault ==  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.updateCensusData" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.updateCensusData" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.updateCensusDataDefault !=  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.updateCensusData" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.updateCensusData" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.updateCensusData" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.updateCensusData" value="false" /> no</td>



</c:if>
																						<ps:trackChanges name ="userPermissionsForm"
																							property="userPermissions.updateCensusData" />
																					</tr>
</c:if>

<c:if test="${userPermissions.showViewSalary ==true}">

																					<tr valign="top" class="datacell2">
																						<td width="219"><strong>View salary
																						</strong></td>
<c:if test="${userPermissionsForm.businessConverted ==true}">

<c:if test="${userPermissions.viewSalaryDefault ==  'TRUE'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewSalary" value="true" /><b><u>



																											yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewSalary" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.viewSalaryDefault !=  'TRUE'}">


<c:if test="${userPermissions.viewSalaryDefault ==  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewSalary" value="true" /><b><u> yes</u></b></td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewSalary" value="false" /> no</td>



</c:if>
<c:if test="${userPermissions.viewSalaryDefault !=  'Y'}">


<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewSalary" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewSalary" value="false" /><b><u> no</u></b></td>



</c:if>
</c:if>
</c:if>
<c:if test="${userPermissionsForm.businessConverted !=true}">

<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewSalary" value="true" /> yes</td>



<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewSalary" value="false" /> no</td>



</c:if>
																						<ps:trackChanges name ="userPermissionsForm"
																							property="userPermissions.viewSalary" />
																					</tr>
</c:if>

																			</TBODY>
																		</TABLE>
																	</DIV>
</c:if></TD>
															<TD class=boxborder width=1><IMG height=1
																src="/assets/unmanaged/images/s.gif" width=1></TD>
														</TR>
														<TR>
															<TD colSpan=3>
																<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
																	<TBODY>
																		<TR>
																			<TD width="1" class=boxborder><IMG height=1
																				src="/assets/unmanaged/images/s.gif" width=1></TD>
																			<TD class=boxborder><IMG height=1
																				src="/assets/unmanaged/images/s.gif" width=1></TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
													</TBODY>
												</TABLE>
											</DIV>
											<BR />
</c:if>
									</DIV> <BR /> <BR />
									<TABLE cellSpacing=0 cellPadding=0 width=412 border=0>
										<TBODY>
											<TR>
												<TD width=175 align=right><div align="center">
														<INPUT class="button134" name="action" type="submit"
															value="cancel"
															onclick="return doCancelChanges(this.form);"></TD>
												<TD align=right width=175>&nbsp;</TD>
												<TD width=175 align=right><div align="center">
														<INPUT class="button134" name="action" type="submit"
															value="continue"
															onclick="return doContinue(this.form);"></TD>
											</TR>
										</TBODY>
									</TABLE> </A></TD>
								<TD width=260>&nbsp;</TD>
								<!-- end main content table -->
							</TR>
							<TR>
								<TD>&nbsp;</TD>
								<TD colSpan=2>&nbsp;</TD>
							</TR>
						</TBODY>
					</TABLE> </ps:form>
