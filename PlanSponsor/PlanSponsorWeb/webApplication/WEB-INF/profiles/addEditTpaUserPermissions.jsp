<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.profiles.BankAccount" %>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.pension.service.security.valueobject.DefaultRolePermissions" %>
<%@ page import="com.manulife.pension.ps.web.profiles.UserPermissionsForm" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.profiles.UserPermissions" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditUserForm" %>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

UserPermissionsForm userPermissionsForm = (UserPermissionsForm)session.getAttribute("userPermissionsForm");
UserPermissions userPermissions= userPermissionsForm.getUserPermissions();
pageContext.setAttribute("userPermissions",userPermissions,PageContext.PAGE_SCOPE);

AddEditUserForm addEditUserForm=(AddEditUserForm)session.getAttribute("addEditUserForm");
pageContext.setAttribute("addEditUserForm",addEditUserForm,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=ContentConstants.USER_MANAGEMENT_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitleUserManagement"/>

<content:contentBean contentId="<%=ContentConstants.REPORTING_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitleReporting"/>

<content:contentBean contentId="<%=ContentConstants.PLAN_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitlePlanServices"/>

<content:contentBean contentId="<%=ContentConstants.CLIENT_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitleClientServices"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_ALL_DIRECT_DEBIT_ACCOUNTS_SELECTED%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningAllDirectDebitAccountsSelected"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_REMOVE_DIRECT_DEBIT_PERMISSION_REMOVE_DIRECT_DEBIT_ACCOUNTS%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningRemoveSelectedDirectDebitAccounts"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_SUBMISSION_ACCESS_ONLY%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningSubmissionAccessOnly"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_NO_DIRECT_DEBIT_ACCOUNT_SELECTED%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoSelectedDirectDebitAccounts"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_NO_DIRECT_DEBIT_ACCOUNT_AVAILABLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoAvailableDirectDebitAccounts"/>




<c:set var="addEditUserForm" value="${addEditUserForm}" scope="session" />

<script type="text/javascript" >
	var submitted = false;

	if (window.addEventListener) {
		window.addEventListener('load', initPage, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', initPage);
	}

	function initPage() {}

	function setDefaultWithdrawals() {
		value = getRadioValue("userPermissions.reviewWithdrawals");

		setRadioValue("userPermissions.viewAllWithdrawals", "true"); // always true

		if (value == "false") {
			setRadioValue("userPermissions.initiateAndViewMyWithdrawals", "false");
		} else {
			setRadioValue("userPermissions.initiateAndViewMyWithdrawals", "true");
		}
	}

	function setDefaultLoans() {
		value = getRadioValue("userPermissions.reviewLoans");

		setRadioValue("userPermissions.viewAllLoans", "true"); // always true

		if (value == "false") {
			setRadioValue("userPermissions.initiateLoans", "false");
		} else {
			setRadioValue("userPermissions.initiateLoans", "true");
		}
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
		directDebitCheckBoxes = document.getElementsByName("selectedDirectDebitAccounts");
		if (directDebitCheckBoxes != null && directDebitCheckBoxes.length > 0) {
			if (value == "true") {
				// If direct debit is set to yes, display warning confirmation, then set all direct debit accounts to enabled and checked
				allowAction = confirm("<content:getAttribute beanName="warningAllDirectDebitAccountsSelected" attribute="text"/>");
				if (allowAction) {
					for (var i = 0; i < directDebitCheckBoxes.length; i++) {
						directDebitCheckBoxes[i].checked = true;
						directDebitCheckBoxes[i].disabled = false;
					}
				}
			} else {
				// If direct debit is set to no, display warning confirmation, then set all direct debit accounts to disabled and unchecked
				if (isCheckboxSelected(directDebitCheckBoxes)) {
					allowAction = confirm("<content:getAttribute beanName="warningRemoveSelectedDirectDebitAccounts" attribute="text"/>");
				}
				if (allowAction) {
					for (var i = 0; i < directDebitCheckBoxes.length; i++) {
						directDebitCheckBoxes[i].checked = false;
						directDebitCheckBoxes[i].disabled = true;
					}
				}
			}
		} else {
			if (value == "true") {
				// Show a warning if direct debit is set to yes but there are no contracts for this firm with direct debit accounts
				if (document.userPermissionsForm.firmContractsHaveDirectDebitAccounts.value == "false") {
					allowAction = confirm("Warning! There are no direct debit accounts selected for the TPA Firm.  Please update the TPA Firm to identify the available direct debit accounts.");
				}
			}
		}
		return allowAction;
	}

	function getBooleanValue(value) {
		return (value == "<%= DefaultRolePermissions.TRUE %>" || value == "<%= DefaultRolePermissions.YES %>") ? "true" : "false";
	}

	function isCheckboxSelected(checkBoxes) {
		var checkboxSelected = false;
		for (var i = 0; i < checkBoxes.length; i++) {
			if (checkBoxes[i].checked && checkBoxes[i].value != "<%= userPermissions.NO_DIRECT_DEBIT_ACCOUNT %>") {
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

	function setRadioValue(radioName, newValue) {
			var radios = document.getElementsByName(radioName);
			for (var i = 0; radios != null && i < radios.length; ++i) {
				if (radios[i].name == radioName) {
					radios[i].checked = (radios[i].value == newValue);
				}
			}
	}

	function disableRadio(radioName, disable) {
			var radioButtons = document.getElementsByName(radioName);
			for (var i = 0; radioButtons != null && i < radioButtons.length; i++) {
				radioButtons[i].disabled = disable;
			}
	}

	function setRadioValue(radioName, newValue, defaultValue) {
			var radios = document.getElementsByName(radioName);
			for (var i = 0; radios != null && i < radios.length; ++i) {
				if (radios[i].name == radioName) {
					radios[i].checked = (radios[i].value == newValue);
				}
			}
	}

	function disableRadio(radioName, disable, defaultValue) {
			var radioButtons = document.getElementsByName(radioName);
			for (var i = 0; radioButtons != null && i < radioButtons.length; i++) {
				radioButtons[i].disabled = disable;
			}
	}

	function doSave(theForm) {
		if (!submitted) {
			submitted = true;
			var warningMessage = "";

			// Warning if all permissions are set to "no"
			var internalUser = false;
<c:if test="${userProfile.internalUser ==true}">
				internalUser = true;
</c:if>

			if(getRadioValue("userPermissions.tpaStaffPlanAccess") != "true" &&
				getRadioValue("userPermissions.downloadReports") != "true" &&
				getRadioValue("userPermissions.editPlanData") != "true" &&
				getRadioValue("userPermissions.createUploadSubmissions") != "true" &&
				getRadioValue("userPermissions.cashAccount") != "true" &&
				getRadioValue("userPermissions.directDebit") != "true" &&
				getRadioValue("userPermissions.initiateAndViewMyWithdrawals") != "true" &&
				getRadioValue("userPermissions.reviewWithdrawals") != "true" &&
				getRadioValue("userPermissions.signingAuthority") != "true" &&
				getRadioValue("userPermissions.updateCensusData") != "true" &&
				getRadioValue("userPermissions.initiateLoans") != "true" &&
				getRadioValue("userPermissions.reviewLoans") != "true" &&
				getRadioValue("userPermissions.viewSalary") != "true") {
				// Content for this?
				warningMessage += "Warning! You have not granted any permissions for this user.\n";
			}

			// Warning if View Submissions is selected but no sub-pemissions
			if (getRadioValue("userPermissions.createUploadSubmissions") != "true" &&
				getRadioValue("userPermissions.cashAccount") != "true" &&
				getRadioValue("userPermissions.directDebit") != "true") {
				warningMessage += "<content:getAttribute beanName="warningSubmissionAccessOnly" attribute="text"/>\n";
			}

			// SPF58 Warning if Firm will No Longer Have Any TPA user with Manage Users permission = “yes”
<c:if test="${userPermissions.showManageUsers ==true}">
			if (document.userPermissionsForm.lastUserWithManageUsers.value == "true" && getRadioValue("userPermissions.manageUsers") == "false") {
				warningMessage += "Warning! No users for this firm will have Manage users permission.\n";
			}
</c:if>

			// SPF57 Warning If No Remaining Receive i:loans Email Receivers with TPA Staff Plan access
<c:if test="${addEditUserForm.emailPreferences.showiLoanEmail ==true}">
			if (document.userPermissionsForm.lastUserWithReceiveILoansEmailAndTPAStaffPlan.value == "true" && getRadioValue("userPermissions.tpaStaffPlanAccess") == "false") {
				warningMessage += "Warning! No users for this firm will receive i:loans emails and have TPA staff plan access permission.\n";
			}
</c:if>

			// SPF55 Display warning if contract does not have a profile (Client or TPA) with Review i:withdrawals Permission (SRW-26)
<c:if test="${userPermissions.showReviewWithdrawals ==true}">
			var reviewWDWContracts = document.userPermissionsForm.lastUserWithReviewIWithdrawalsContracts.value;
			if (reviewWDWContracts != null && reviewWDWContracts != "" && getRadioValue("userPermissions.reviewWithdrawals") == "false") {
				warningMessage += "Warning! No users for contract(s) " + reviewWDWContracts + " will have Review i:withdrawals permission.\n";
			}
</c:if>

			// SPF55 Display warning if contract does not have a profile (Client or TPA) with Review i:withdrawals Permission (SRW-26)
<c:if test="${userPermissions.showReviewLoans ==true}">
			var reviewLoansContracts = document.userPermissionsForm.lastUserWithReviewLoansContracts.value;
			if (reviewLoansContracts != null && reviewLoansContracts != "" && getRadioValue("userPermissions.reviewLoans") == "false") {
				warningMessage += "Warning! No users for contract(s) " + reviewLoansContracts + " will have Review loans permission.\n";
			}
</c:if>

			// SPF56 Display warning if contract does not have a profile (Client or TPA) with Signing Authority (Approve i:withdrawals @before) Permission (SRW-34)
			// change from approve withdrawal to signing authority : Loans project
<c:if test="${userPermissions.showSigningAuthority ==true}">
			var signingAuthContracts = document.userPermissionsForm.lastUserWithSigningAuthorityContracts.value;
			if (signingAuthContracts != null && signingAuthContracts != "" && getRadioValue("userPermissions.signingAuthority") == "false") {
				warningMessage += "Warning! No users for contract(s) " + signingAuthContracts + " will have Signing Authority permission.\n";
			}
</c:if>

			document.userPermissionsForm.action.value = 'continue';

			if (warningMessage != "") {
				submitted = confirm(warningMessage + "Do you want to continue with these changes?");
			}
			if (submitted) document.userPermissionsForm.submit();
			return submitted;
		} else {
			 window.status = "Transaction already in progress.  Please wait.";
			 return false;
		}
 	}

    function doTPACancel(theForm) {
		if (!submitted) {
			submitted = true;
		   document.userPermissionsForm.action.value = 'cancel';
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

	registerTrackChangesFunction(isFormChanged);
</script>

<ps:form method="POST" action="/do/profiles/tpaFirmUserPermissions/" name="userPermissionsForm" modelAttribute="userPermissionsForm">
<form:hidden path="action" value=""/>
<form:hidden path="lastUserWithManageUsers"/>
<form:hidden path="lastUserWithReceiveILoansEmailAndTPAStaffPlan"/>
<form:hidden path="lastUserWithSigningAuthorityContracts"/>
<form:hidden path="lastUserWithReviewIWithdrawalsContracts"/>
<form:hidden path="firmContractsHaveDirectDebitAccounts"/>
<form:hidden path="lastUserWithReviewLoansContracts"/>

<TABLE cellSpacing=0 cellPadding=0 width=760 border=0>
  <TBODY>
  <TR>
    <TD>
      <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
        <TR>
          <TD><IMG height=1
            src="/assets/unmanaged/images/s.gif"
            width=30><BR/><IMG height=1
            src="/assets/unmanaged/images/s.gif"></TD>
          <TD width=500><BR/>

			<jsp:include flush="true" page="userPermissionsHeader.jsp"></jsp:include>

            <BR/>

<c:if test="${userPermissions.showUserManagementSection ==true}">
           <TABLE class=box style="CURSOR: pointer" onclick="expandSection('sc1')" cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR class=tablehead>
                <TD class=tableheadTD1 colSpan=3><img id="sc1img" src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<b><content:getAttribute beanName="groupTitleUserManagement" attribute="text"/></b></TD>
              </TR>
            </TBODY>
            </TABLE>
            <DIV class=switchcontent id=sc1>
            <TABLE class=box cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR>
                <TD class=boxborder width=1><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD>
                <TD>
                  <TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
                    <TBODY>
<% String TRUE = DefaultRolePermissions.TRUE; 
String.valueOf(DefaultRolePermissions.TRUE);
pageContext.setAttribute("TRUE", TRUE,PageContext.PAGE_SCOPE); %> 
<c:if test="${userPermissions.showManageUsers ==true}">
                      <tr valign="top" class="datacell2">
	                    <td width="219">
							   <strong>Manage users </strong>
	                    </td>
<c:if test="${userPermissions.manageUsersDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.manageUsers" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.manageUsers" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.manageUsers"/>
</c:if>
                      </tr>
</c:if>

                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>TPA staff plan access </strong></td>
<c:if test="${userPermissions.tpaStaffPlanAccessDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.tpaStaffPlanAccess" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.tpaStaffPlanAccess" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.tpaStaffPlanAccess"/>
</c:if>
                      </tr>

                    </TBODY>
                  </TABLE>
                </TD>
                <TD class=boxborder width=1><IMG height=1src="/assets/unmanaged/images/s.gif" width=1></TD>
              </TR>
              <TR>
                <TD colSpan=3>
                  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                      <TD width="1" class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                  <TD class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                  </TR></TBODY></TABLE></TD></TR></TBODY></TABLE></DIV>&nbsp;<BR/>
</c:if>

<c:if test="${userPermissions.showReportingSection ==true}">
            <TABLE class=box style="CURSOR: pointer" onclick="expandSection('sc2')" cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR class=tablehead>
                <TD class=tableheadTD1 colSpan=3><img id="sc2img" src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<B><content:getAttribute beanName="groupTitleReporting" attribute="text"/></B></TD>
              </TR>
             </TBODY>
            </TABLE>
            <DIV class=switchcontent id=sc2>
            <TABLE class=box cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR>
                <TD class=boxborder width=1><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD>
                <TD width=644><TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
                  <TBODY>

<c:if test="${userPermissions.showDownloadReports ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>Download reports - full SSN </strong></td>
<c:if test="${userPermissions.downloadReportsDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.downloadReports" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.downloadReports" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.downloadReports"/>
</c:if>
                      </tr>
</c:if>

                  </TBODY>
                </TABLE>
                 </TD>
                <TD width=1 bgcolor="#D9DAE8" class=boxborder><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD></TR>
              <TR>
                <TD colSpan=3>
                  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                      <TD width="1" class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                      <TD class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                      </TR></TBODY></TABLE></TD></TR></TBODY></TABLE></DIV><BR/>
</c:if>
<c:if test="${userPermissions.showClientServicesSection ==true}">
            <TABLE class=box style="CURSOR: pointer" onclick="expandSection('sc4')" cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR class=tablehead>
                <TD class=tableheadTD1 colSpan=3><img id="sc4img" src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<B><content:getAttribute beanName="groupTitleClientServices" attribute="text"/></B></TD>
              </TR>
             </TBODY>
            </TABLE>
            <DIV class=switchcontent id=sc4>
            <TABLE class=box cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR>
                <TD class=boxborder width=1><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD>
                <TD width=733><TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
                  <TBODY>

<c:if test="${userPermissions.showClientServicesSection ==true}">
					<%-- Signing Authority - Loans - Begin --%>
<c:if test="${userPermissions.showSigningAuthority ==true}">
                        <tr valign="top" class="datacell2">
            			<td width="219"><strong>Signing authority </strong></td>
<c:if test="${userPermissions.signingAuthorityDefault !='TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.signingAuthority" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.signingAuthority" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.signingAuthority"/>
</c:if>
                      </tr>
</c:if>
					<%-- Signing Authority - Loans - End --%>  

                    <tr valign="top">
                      <td colspan="3" class="tablesubhead"><b>Submissions</b></td>
                      </tr>


<c:if test="${userPermissions.showCreateUploadSubmissions ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Create/upload submissions	</strong></td>
<c:if test="${userPermissions.createUploadSubmissionsDefault !='TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.createUploadSubmissions" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.createUploadSubmissions" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.createUploadSubmissions"/>
</c:if>
                      </tr>
</c:if>


<c:if test="${userPermissions.showCashAccount ==true}">
                      </tr>
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Cash account </strong></td>
<c:if test="${userPermissions.cashAccountDefault !='TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.cashAccount" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.cashAccount" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.cashAccount"/>
</c:if>
                      </tr>
</c:if>

<c:if test="${userPermissions.showDirectDebit ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Direct debit </strong></td>
<c:if test="${userPermissions.directDebitDefault !='TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="disableDirectDebit(this.value)" path="userPermissions.directDebit" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="disableDirectDebit(this.value)" path="userPermissions.directDebit" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.directDebit"/>
</c:if>
                      </tr>

<c:if test="${not empty userPermissions.directDebitAccounts}">
	  		<tr valign="top" class="datacell2">
		       		<td align="left" nowrap colspan="3">
		       			<%--
		       			This hidden property is used to ensure that the browser sends back something
		       			for the checkboxes value even when none of the checkboxes is selected. Without
		       			this hidden property, the browser will not send anything back when no checkbox is
		       			selected.
		       			--%>
<input type="hidden" name="selectedDirectDebitAccounts" /> <%-- name="userPermissions" --%>
<input type="hidden" name="userPermissions.selectedDirectDebitAccountIdsAsString" /><%-- input - name="userPermissionsForm" --%>
	 		    		<ps:trackChanges name="userPermissionsForm" property="userPermissions.selectedDirectDebitAccountIdsAsString"/>

<c:forEach items="${userPermissions.directDebitAccounts}" var="account" >
							&nbsp;&nbsp;&nbsp;&nbsp;
							<%
							BankAccount checkValue=(BankAccount)pageContext.getAttribute("account");
							%>
 <c:if test="${account.noAccess ==false}">
 <form:checkbox path="selectedDirectDebitAccounts" value="${account.primaryKey}">
${account.primaryKey}
									</form:checkbox>
</c:if>
<c:if test="${account.noAccess !=false}">
									 <form:checkbox path="selectedDirectDebitAccounts" value="${account.primaryKey}">
${account.primaryKey}
									</form:checkbox>
									 <%
									if (userPermissions.getSelectedDirectDebitAccountsAsList().contains(checkValue)) {
									%>
<input type="hidden" name="selectedDirectDebitAccounts" value="<%= checkValue.getPrimaryKey() %>"/>
									<%
									}
									%> 
</c:if>
${account.label}<br/>
</c:forEach>

					</td>
	    	</tr>
</c:if>

</c:if>

</c:if>

<c:if test="${userPermissions.showReviewLoans ==true}">
                     <tr valign="top">
                       	<td colspan="3" class="tablesubhead"><b>Loans</b></td>
                     </tr>
<c:if test="${userPermissions.showViewAllLoans ==true}">
                     	<tr valign="top" class="datacell2">
                        <td width="219"><strong>View all loans</strong></td>
<c:if test="${userPermissions.viewAllLoansDefault =='TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewAllLoans" value="true" /> yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewAllLoans" value="false" /> no</td>
</c:if>
<c:if test="${userPermissions.viewAllLoansDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllLoans" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllLoans" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.viewAllLoans"/>
</c:if>
                      	</tr>
</c:if>
                      
<c:if test="${userPermissions.showInitiateLoans ==true}">
                     	 <tr valign="top" class="datacell2">
                        	<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Initiate loans </strong></td>
<c:if test="${userPermissions.initiateLoansDefault =='TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.initiateLoans" value="true" /> yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.initiateLoans" value="false" /> no</td>
</c:if>
<c:if test="${userPermissions.initiateLoansDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateLoans" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateLoans" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.initiateLoans"/>
</c:if>
                      </tr>
</c:if>
<c:if test="${userPermissions.showReviewLoans ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review loans </strong></td>
<c:if test="${userPermissions.reviewLoansDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="setDefaultLoans()" path="userPermissions.reviewLoans" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="setDefaultLoans()" path="userPermissions.reviewLoans" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.reviewLoans"/>
</c:if>
                      </tr>
</c:if>
</c:if>

<c:if test="${userPermissions.showReviewWithdrawals ==true}">
                      <tr valign="top">
                        <td colspan="3" class="tablesubhead"><b>i:withdrawals</b></td>
                      </tr>

<c:if test="${userPermissions.showViewAllWithdrawals ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>View all i:withdrawals </strong></td>
<c:if test="${userPermissions.viewAllWithdrawalsDefault == 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewAllWithdrawals" value="true" /> yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewAllWithdrawals" value="false" /> no</td>
</c:if>
<c:if test="${userPermissions.viewAllWithdrawalsDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllWithdrawals" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewAllWithdrawals" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.viewAllWithdrawals"/>
</c:if>
                      </tr>
</c:if>

                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Initiate i:withdrawals</strong></td>
<c:if test="${userPermissions.initiateAndViewMyWithdrawalsDefault == 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.initiateAndViewMyWithdrawals" value="true" /> yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.initiateAndViewMyWithdrawals" value="false" /> no</td>
</c:if>
<c:if test="${userPermissions.initiateAndViewMyWithdrawalsDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateAndViewMyWithdrawals" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.initiateAndViewMyWithdrawals" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.initiateAndViewMyWithdrawals"/>
</c:if>
                      </tr>

<c:if test="${userPermissions.showReviewWithdrawals ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review i:withdrawals </strong></td>
<c:if test="${userPermissions.reviewWithdrawalsDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton onclick="setDefaultWithdrawals()" path="userPermissions.reviewWithdrawals" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton onclick="setDefaultWithdrawals()" path="userPermissions.reviewWithdrawals" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.reviewWithdrawals"/>
</c:if>
                      </tr>
</c:if>
</c:if>

<c:if test="${userPermissions.showClientServicesSection ==true}">
                    <tr valign="top">
                      <td colspan="3" class="tablesubhead"><strong>Census Management</strong></td>
                    </tr>

<c:if test="${userPermissions.showUpdateCensusData ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>Update census data </strong></td>
<c:if test="${userPermissions.updateCensusDataDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.updateCensusData" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.updateCensusData" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.updateCensusData"/>
</c:if>
                      </tr>
</c:if>

<c:if test="${userPermissions.showViewSalary ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>View salary </strong></td>
<c:if test="${userPermissions.viewSalaryDefault == 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewSalary" value="true" /><b><u>&nbsp;yes</u></b></td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="userPermissions.viewSalary" value="false" />&nbsp;no</td>
</c:if>
<c:if test="${userPermissions.viewSalaryDefault != 'TRUE'}">
<td width="75" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewSalary" value="true" />&nbsp;yes</td>
<td width="50" align="left" nowrap="nowrap"><form:radiobutton path="userPermissions.viewSalary" value="false" />&nbsp;no</td>
								<ps:trackChanges name="userPermissionsForm" property="userPermissions.viewSalary"/>
</c:if>
                      </tr>
</c:if>

</c:if>

                  </TBODY>
                </TABLE></TD>
                <TD class=boxborder width=1><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD></TR>
              <TR>
                <TD colSpan=3>
                  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                      <TD width="1" class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                  <TD class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                  </TR></TBODY></TABLE></TD></TR></TBODY></TABLE></DIV><BR/>
</c:if>

                <BR/>
                <BR/>
            <TABLE cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR>
                <TD width=175 align=right><div align="center">
                   <INPUT class="button134" name="cancelButton"
                          type="submit" value="cancel"
                          onclick="return doTPACancel(this.form);"></TD>
                <TD align=right width=175>&nbsp;</TD>
                <TD width=175 align=right><div align="center">
                   <INPUT class="button134" name="saveButton"
                    type="button" value="continue"
                    onclick="return doSave(this.form);"></TD>
			</TR></TBODY></TABLE>
                      </A></TD>
          <TD width=260>&nbsp;</TD><!-- end main content table --></TR>
        <TR>
          <TD>&nbsp;</TD>
          <TD colSpan=2>&nbsp;</TD></TR></TBODY>
          </TABLE>
          </TABLE>

</ps:form>
