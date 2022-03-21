<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AccessLevelHelper" %>
<%@ page import="com.manulife.pension.ps.web.profiles.TPAUserContractAccess" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.TpaFirm" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="addEditUserForm" value="${tpaFirmForm}" scope="request"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_ALL_DIRECT_DEBIT_ACCOUNTS_SELECTED%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningAllDirectDebitAccountsSelected"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_REMOVE_DIRECT_DEBIT_PERMISSION_REMOVE_DIRECT_DEBIT_ACCOUNTS%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningRemoveSelectedDirectDebitAccounts"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_REMOVE_ALL_SUBMISSION_PERMISSION%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningRemoveAllSubmissionPermission"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_REMOVE_HIDDEN_DIRECT_DEBIT_ACCOUNT%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningRemoveHiddenAccounts"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_PERMISSIONS%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="messageNoPermissions"/>
                     
<content:contentBean contentId="<%=ContentConstants.REPORTING_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="reportingGroupTitle"/>
<content:contentBean contentId="<%=ContentConstants.PLAN_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="planGroupTitle"/>
<content:contentBean contentId="<%=ContentConstants.CLIENT_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="clientGroupTitle"/>
<content:contentBean contentId="<%=ContentConstants.PARTICIPANT_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantGroupTitle"/>
                     

<script type="text/javascript" >
<c:if test="${addEditUserForm.clientUsersWithReviewWithdrawalPermission}">
var clientUsersWithReviewWithdrawalPermission = true;
</c:if>
<c:if test="${!addEditUserForm.clientUsersWithReviewWithdrawalPermission}">
var clientUsersWithReviewWithdrawalPermission = false;
</c:if>
// changing ApproveWithdrawal -> Signing Authority: Loans project
<c:if test="${addEditUserForm.clientUsersWithSigningAuthorityPermission}">
var clientUsersWithSigningAuthorityPermission = true;
</c:if>
<c:if test="${!addEditUserForm.clientUsersWithSigningAuthorityPermission}">
var clientUsersWithSigningAuthorityPermission = false;
</c:if>
<c:if test="${addEditUserForm.clientUsersWithReviewLoansPermission}">
var clientUsersWithReviewLoansPermission = true;
</c:if>
<c:if test="${!addEditUserForm.clientUsersWithReviewLoansPermission}">
var clientUsersWithReviewLoansPermission = false;
</c:if>

function firmSubmit(obj) {
	if (!submitted) {
		var questions = Array();
		var q1 = "Warning! At least one user, either TPA/client, must have review i:withdrawals permission.\n\n Do you want to continue?";
		var q2 = "Are you sure?";
		var q3 = "This action will change the Review i:withdrawals permission to No on all plan sponsor user profiles for this contract."; 
		var q4 = "Warning! There are no web users with signing authority permission.\n\nDo you want to continue?";

		var q7 = "This action will change the Review Loans permission to No on all plan sponsor user profiles for this contract."; 
	
		var reviewElement = document.getElementsByName("contractAccess[0].reviewIWithdrawals")[0];
		if (reviewElement != null) {
			var reviewVal = reviewElement.checked ? "true" : "false";
			// removing the requirement tpf28
			//if ( reviewVal == "false" && reviewVal != originalReviewWithdrawalValue ) {
				//if (  !clientUsersWithReviewWithdrawalPermission) {
				//	questions.push(q1);
				//} else {
				//	questions.push(q2);
				//}
			//}
			if ( reviewVal == "true" && reviewVal != originalReviewWithdrawalValue && clientUsersWithReviewWithdrawalPermission ) {
				questions.push(q3);
			}
		}


		var reviewLoansElement = document.getElementsByName("contractAccess[0].reviewLoans")[0];
		if (reviewLoansElement != null) {
			var reviewLoansVal = reviewLoansElement.checked ? "true" : "false";
			
			if ( reviewLoansVal == "true" && reviewLoansVal != originalReviewLoansValue && clientUsersWithReviewLoansPermission ) {
				questions.push(q7);
			}
		}

		// Changed approve to signing authority : Loans project	
		var signingAuthorityElement = document.getElementsByName("contractAccess[0].signingAuthority")[0];
		if (signingAuthorityElement != null) {
			var signingAuthorityVal = signingAuthorityElement.checked ? "true" : "false";
			if ( signingAuthorityVal == "false" && signingAuthorityVal != originalSigningAuthorityValue && !clientUsersWithSigningAuthorityPermission) {
				questions.push(q4);
			}
		}
	
		var allConfirmed = true;
		for ( var i = 0 ; i < questions.length ; i++ ) {
			allConfirmed = allConfirmed && confirm(questions[i]);
			if ( !allConfirmed ) break;
		}
		if ( allConfirmed ) {
			submitted = doSubmit(obj);
			return submitted;
		} else {
			return false;
		}
	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

var minus_icon = "/assets/unmanaged/images/minus_icon.gif";
var plus_icon = "/assets/unmanaged/images/plus_icon.gif";
var VISIBLE = 1;
var HIDDEN = 2;
var isAdd = false;

function showTPAUsers() {
	var obj = document.getElementById("tpaUserRow");
	if ( obj != null )
			obj.style.display = "";
}
function hideTPAUsers() {
	var obj = document.getElementById("tpaUserRow");
	if ( obj != null )
			obj.style.display = "none";
}

function toggleTable(imageId, tableId) {
	var tableObj = document.getElementById(tableId);
	var imageObj = document.getElementById(imageId);
	if ( tableObj == null || typeof(tableObj) == "undefined" ) return;
	if ( imageObj == null || typeof(imageObj) == "undefined" ) return;
	
	if ( tableObj.style.display == "none" ) {
		setTableDisplay(imageId, tableId, VISIBLE);
	} else {
		setTableDisplay(imageId, tableId, HIDDEN);
	}
}
function setTableDisplay(imageId, tableId, display ) {
	var tableObj = document.getElementById(tableId);
	var imageObj = document.getElementById(imageId);
	if ( tableObj == null || typeof(tableObj) == "undefined" ) return;
	if ( imageObj == null || typeof(imageObj) == "undefined" ) return;
	
	tableObj.style.display = (display == VISIBLE) ? "" : "none";
	imageObj.src = (display == VISIBLE) ? minus_icon : plus_icon;
}
function expandAllFirmSections() {
	setTableDisplay('ReportTableImage', 'ReportTable', VISIBLE);
	setTableDisplay('PlanTableImage', 'PlanTable', VISIBLE);
	setTableDisplay('ClientServicesTableImage', 'ClientServicesTable', VISIBLE);
	setTableDisplay('ParticipantTableImage', 'ParticipantTable', VISIBLE);
	setTableDisplay('PlanServicesTableImage', 'PlanServicesTable', VISIBLE);
}
function collapseAllFirmSections() {
	setTableDisplay('ReportTableImage', 'ReportTable', HIDDEN);
	setTableDisplay('PlanTableImage', 'PlanTable', HIDDEN);
	setTableDisplay('ClientServicesTableImage', 'ClientServicesTable', HIDDEN);
	setTableDisplay('ParticipantTableImage', 'ParticipantTable', HIDDEN);
	setTableDisplay('PlanServicesTableImage', 'PlanServicesTable', HIDDEN);
}

function showHideFromSelect(theSelect, indexId)
{
 	var roleSelected = theSelect.options[theSelect.selectedIndex].value;
 	showHide(roleSelected, indexId, true);
}


function showHide(roleSelected, indexId, isSetDefault)
{
 	var obj = document.getElementById("notif"+indexId);

 	/* showed the "receive notification" permission if
 	 * role selected is "external user manager"
 	 */
  	if (roleSelected != null && roleSelected.indexOf("PSEUM") != -1)
  	{
		//  obj.style.display = "inline"; // commented out because of Netscape problems
		obj.style.visibility = "visible";
		/*
		 * Set default to YES
		 */
		if (isSetDefault) {
			var receiveNotificationRadios =
				document.getElementsByName("contractAccess[" + indexId + "].receiveNotification");
			receiveNotificationRadios[0].checked = true;
			receiveNotificationRadios[1].checked = false;
		}
	} else {
		// obj.style.display = "none"; // commented out because of Netscape problems
		obj.style.visibility = "hidden";
	}
}

function disableRadioButtons(name, state, selectedValue)
{
	var radioButtons = document.getElementsByName(name);
	
	if (radioButtons != null) {
		for (var i = 0; i < radioButtons.length; i++) {
		  if (radioButtons[i].value == selectedValue) {
		    radioButtons[i].checked = true;
		  } else {
		  	radioButtons[i].checked = false;
		  }
	      radioButtons[i].disabled = state;
		}
	}
}



function disableRadioButtonsIniVal(name, state, selectedValue, initValue)
{
	var radioButtons = document.getElementsByName(name);
	var initState = document.getElementsByName(initValue);
	 //var initState = document.getElementById(initValue); - LS make it working in FireFox
	//window.alert( initValue + " state "  +initState[0].value);
    
	if (radioButtons != null) {
		for (var i = 0; i < radioButtons.length; i++) {
		  if (initState != null && initState[0].value == "disabled") {
		      if (radioButtons[i].value == "false") {
		          radioButtons[i].checked = true;
		          
		      }
		  } else { 
		      if (radioButtons[i].value == selectedValue){ 
		          radioButtons[i].checked = true;
		      } else {
		          radioButtons[i].checked = false;
		      }
		  }
		  
		  if (initState != null && initState[0].value == "disabled") {
		      //do nothing
		  } else {
			  radioButtons[i].disabled = state;

		  }
		}
	}
}


function setSubmissionAccess(submissionAccessObj,indexId) {

	var name = submissionAccessObj.name;
	var value = submissionAccessObj.value;
	var prefix = name.substring(0, name.indexOf("."));

	if (value == "true") {
		disableRadioButtons(name, false, "true");
		disableRadioButtonsIniVal(prefix + ".uploadSubmissions", false, "true","uploadSubmissionsIniVal"+ indexId);
		disableRadioButtonsIniVal(prefix + ".cashAccount", false, "true","cashAccountIniVal" + indexId);
		disableRadioButtonsIniVal(prefix + ".directDebit", false, "false","directDebitIniVal"+ indexId);
	} else {
	    // ICE11.2 Edit selects "No" on View Submission permission, where user has
	    // access to sub-functions that editor does not have access to.
	    var permissionsNotShown = document.getElementsByName(prefix + ".permissionsNotShown")[0];
		var accountsNotShownInput = document.getElementsByName(prefix + ".accountsNotShown")[0];
	    var userConfirm = true;
	    var message = "";
	    if (permissionsNotShown != null) {
	    	if (permissionsNotShown.value == "true") {
	    		message = "<content:getAttribute beanName="warningRemoveAllSubmissionPermission" attribute="text"/>";
	    	}
	    }
		if (accountsNotShownInput != null) {
			if (accountsNotShownInput.value == "true") {
				message += "\n" + "<content:getAttribute beanName="warningRemoveHiddenAccounts" attribute="text"/>";
			}
		}

		var directDebitCheckBoxes = document.getElementsByName(prefix + ".selectedDirectDebitAccounts");
		if (directDebitCheckBoxes != null && directDebitCheckBoxes.length > 0) {
			// ICE 11.1 Edit selects "No" on Direct Debit Permissions
			var showMessage = false;
			for (var i = 0; i < directDebitCheckBoxes.length; i++) {
				if (directDebitCheckBoxes[i].checked &&
					directDebitCheckBoxes[i].value != "none") {
					showMessage = true;
					break;
				}
			}
			if (showMessage) {
				message += "\n" + "<content:getAttribute beanName="warningRemoveSelectedDirectDebitAccounts" attribute="text"/>";
			}
		}
		
		if (message.length > 0) {
    		userConfirm = confirm(message);
    		if (!userConfirm) {
    		   disableRadioButtons(name, false, "true");
    		   value = "true"
    		}
	    }

	    if (userConfirm) {
			disableRadioButtons(name, false, "false");
		    disableRadioButtonsIniVal(prefix + ".uploadSubmissions", true, "false", "uploadSubmissionsIniVal" + indexId);
			disableRadioButtonsIniVal(prefix + ".cashAccount", true, "false","cashAccountIniVal"+ indexId);
			disableRadioButtonsIniVal(prefix + ".directDebit", true, "false", "directDebitIniVal"+ indexId);
		}
	}

	var directDebitCheckBoxes = document.getElementsByName(prefix + ".selectedDirectDebitAccounts");

	if (value == "false") {
		for (var i = 0; i < directDebitCheckBoxes.length; i++) {
		    if (directDebitCheckBoxes[i].noAccess &&
		        ! directDebitCheckBoxes[i].wasChecked) {
		        continue;
		    }
			if (! directDebitCheckBoxes[i].noAccess) {
			    directDebitCheckBoxes[i].disabled = (value == "false");
			}
			directDebitCheckBoxes[i].checked = (value == "true");
		}
	}
}

function setDirectDebitAccess(directDebitAccessObj, noMessage) {
	var name = directDebitAccessObj.name;
	var value = directDebitAccessObj.value;
	var prefix = name.substring(0, name.indexOf("."));

	var directDebitCheckBoxes = document.getElementsByName(prefix + ".selectedDirectDebitAccounts");
	var userConfirmed = true;
	var count=0;
	if (!noMessage) {
		if (value == "true") {
			if (directDebitCheckBoxes != null && directDebitCheckBoxes.length > 0) {
				var showMessage = false;
				for (var i = 0; i < directDebitCheckBoxes.length; i++) {
					if (directDebitCheckBoxes[i].value != "none" &&
					    ! directDebitCheckBoxes[i].noAccess) {
						showMessage = true;
						count +=1;
						//break;
					}
				}

				if (showMessage && count > 0 && count==directDebitCheckBoxes.length-1) {				
					var message = "<content:getAttribute beanName="warningAllDirectDebitAccountsSelected" attribute="text"/>";
					userConfirmed = confirm(message);
					if (!userConfirmed) {
						disableRadioButtons(name, false, "false");
						value = "false";
					}
				}
			}
		} else if (value == "false") {
			var message = "";
			if (directDebitCheckBoxes != null && directDebitCheckBoxes.length > 0) {
				// ICE 11.1 Edit selects "No" on Direct Debit Permissions
				var showMessage = false;
				for (var i = 0; i < directDebitCheckBoxes.length; i++) {
					if (directDebitCheckBoxes[i].checked &&
						directDebitCheckBoxes[i].value != "none") {
						showMessage = true;
						break;
					}
				}
				if (showMessage) {
					message = "<content:getAttribute beanName="warningRemoveSelectedDirectDebitAccounts" attribute="text"/>";
				}
			}
			var accountsNotShownInput = document.getElementsByName(prefix + ".accountsNotShown")[0];
			if (accountsNotShownInput != null) {
				if (accountsNotShownInput.value == "true") {
					// ICE 11.1.1 Profile being edited has direct debit account that editor does not have.
					message += "\n" + "<content:getAttribute beanName="warningRemoveHiddenAccounts" attribute="text"/>";
				}
			}
			if (message.length > 0) {
				userConfirmed = confirm(message);
				if (!userConfirmed) {
					disableRadioButtons(name, false, "true");
					value = "true";
				}
			}
		}
	}
	if (userConfirmed) {
		for (var i = 0; i < directDebitCheckBoxes.length; i++) {
	        if (directDebitCheckBoxes[i].noAccess &&
	            ! directDebitCheckBoxes[i].wasChecked) {
		        continue;
		    }
			if (! directDebitCheckBoxes[i].noAccess) {
			    directDebitCheckBoxes[i].disabled = (value == "false");
				directDebitCheckBoxes[i].checked = (value == "true");
			}
			if (value == "false") {
				directDebitCheckBoxes[i].checked = (value == "true");
				// clear any hidden field for the disabled check boxes.
				for (var j = 0; j < directDebitCheckBoxes.length; j++) {
					if (directDebitCheckBoxes[j].type == "hidden" &&
						directDebitCheckBoxes[j].value == directDebitCheckBoxes[i].value) {
						directDebitCheckBoxes[j].value = "none";
					}
				}
			}
			
		}
	}
}

function changeDirectDebitAccount(checkBox) {
	var prefix = checkBox.name.substring(0, checkBox.name.indexOf("."));
	// If the user has de-selected an account we need to make sure that the hidden field is cleared out
	if (!this.checked) {
		var directDebitCheckBoxes = document.getElementsByName(prefix + ".selectedDirectDebitAccounts");
		for (var i = 0; i < directDebitCheckBoxes.length; i++) {
			if (directDebitCheckBoxes[i].type == "hidden" && directDebitCheckBoxes[i].value == checkBox.value) {
				directDebitCheckBoxes[i].value = "none";
			}
		}
	}
}

/**
 * Initialize the radio buttons' states.
 */
function initializeSubmissionAccess(indexId) {
	var prefix = "contractAccess[" + indexId + "]";

	var directDebitCheckBoxes = document.getElementsByName(prefix + ".selectedDirectDebitAccounts");

	if (directDebitCheckBoxes != null) {
		for (var i = 0; i < directDebitCheckBoxes.length; i++) {
		    directDebitCheckBoxes[i].noAccess = directDebitCheckBoxes[i].disabled;
			directDebitCheckBoxes[i].wasChecked = directDebitCheckBoxes[i].checked;
		}
	}

	var radioButtons = document.getElementsByName(prefix + ".submissionAccess");
	var myindex = indexId;
	if (radioButtons != null) {
		for (var i = 0; i < radioButtons.length; i++) {
			if (radioButtons[i].checked && radioButtons[i].value == "false") {
				setSubmissionAccess(radioButtons[i], myindex);
				break;
			}
		}
	}
	radioButtons = document.getElementsByName(prefix + ".directDebit");
	if (radioButtons != null) {
		for (var i = 0; i < radioButtons.length; i++) {
			if (radioButtons[i].checked && radioButtons[i].value == "false") {
				setDirectDebitAccess(radioButtons[i], true);
				break;
			}
		}
	}
}

</script>
<c:forEach items="${addEditUserForm.contractAccesses}" var="contractAccess" varStatus="theIndex" >
	<c:set var="indexValue" value="${theIndex.index}"/> 
<c:set var="contractAccess" value="${contractAccess}"/>
	      	<logicext:if name="contractAccess" property="hasAccess" op="equal" value="false">
				<logicext:then>
					<content:getAttribute beanName="messageNoPermissions" attribute="text" />
				</logicext:then>
			</logicext:if>
		
	<%
		Integer value=Integer.parseInt(pageContext.getAttribute("indexValue").toString());
		String uploadSubmissionsIniValName = "uploadSubmissionsIniVal" + value;
		String viewAllSubmissionsIniValName = "viewAllSubmissionsIniVal" + value;
		String cashAccountIniValName = "cashAccountIniVal" + value;
		String directDebitIniValName = "directDebitIniVal" + value;
	%>
	<%-- The submissionAccessUserCount is the number of users who have access to View submissions
	     minus the current user (if the current user has that permission). --%>
<script type="text/javascript">
var originalReviewWithdrawalValue = "<c:out value="${contractAccess.reviewIWithdrawals}"/>"
var originalSigningAuthorityValue = "<c:out value="${contractAccess.signingAuthority}"/>"
var originalReviewLoansValue = "<c:out value="${contractAccess.reviewLoans}"/>"
</script>

	     
<form:hidden path="contractAccesses[${theIndex.index}].numberOfSelectedDirectDebitAccounts" /><%--  input - indexed="true" name="contractAccess" --%>
<form:hidden path="contractAccesses[${theIndex.index}].permissionsNotShown" /><%--  input - indexed="true" name="contractAccess" --%>
<form:hidden path="contractAccesses[${theIndex.index}].accountsNotShown" /><%--  input - indexed="true" name="contractAccess" --%>
<form:hidden path="contractAccesses[${theIndex.index}].contractNumber" /><%--  input - indexed="true" name="contractAccess" --%>
<br/>
<c:if test="${addEditUserForm.showEverything}">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
				     <tr>
                          <td>
                          	<img src="/assets/unmanaged/images/plus_icon_all.gif" style="CURSOR: pointer" onclick="expandAllFirmSections()"> 
                          	<span style="padding-left:3px;font-size:12pt">/</span>
                          	<img src="/assets/unmanaged/images/minus_icon_all.gif" style="CURSOR: pointer" onclick="collapseAllFirmSections()"> &nbsp;&nbsp; all sections
                      </tr>
                     
                  </table>
		<br/>

	<c:if test="${addEditUserForm.showClientServicesSection}" >
 	   	<table class="box" border="0" cellpadding="0" cellspacing="0" width="412">
      	<tr class="tablehead"  >
      		<td class="tableheadTD1" style="padding:5px;">
       		<img id="ClientServicesTableImage" style="CURSOR: pointer" onclick="toggleTable('ClientServicesTableImage','ClientServicesTable');" src="/assets/unmanaged/images/plus_icon.gif">
         		&nbsp;<b><content:getAttribute beanName="clientGroupTitle" attribute="text"/></b>
         	</td>
    	</tr>
    	</table>
    	<table style="display:none" id="ClientServicesTable" width="412" cellpadding="0" cellspacing="0">

		<%-- LOANS - SIGNING AUTHORITY - BEGIN --%>
		<c:if test="${contractAccess.showSigningAuthority}">		
            <tr valign="top" class="datacell2">
            <td width="219" class="datacell2  borderLeft boldEntry" >&nbsp;&nbsp;<strong>Signing authority </strong></td>
    		<c:if test="${addEditUserForm.enableSigningAuthority}">
		    <td width="75" class="datacell2 " >
<form:radiobutton onclick="showTPAUsers()" path="contractAccesses[${theIndex.index}].signingAuthority" value="true" />yes
		    </td>
		    <td width="50" class="datacell2  borderRight" >
<form:radiobutton onclick="hideTPAUsers()" path="contractAccesses[${theIndex.index}].signingAuthority" value="false" />no
		    </td>
 		    <ps:trackChanges name="addEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="signingAuthority"/>		    
 		    </c:if>
    		<c:if test="${!addEditUserForm.enableSigningAuthority}">
		    <td width="75" class="datacell2 " >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].signingAuthority" value="true" />yes
		    </td>
		    <td width="50" class="datacell2  borderRight" >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].signingAuthority" value="false" />no
		    </td>
 		    </c:if>
		</tr>
		</c:if> 
		<c:if test="${addEditUserForm.showTPAUserList}">
	 <form:hidden path="selectedTPAUsers"/>

<form:hidden path="selectedTPAUsersAsString"/> 
			<ps:trackChanges name="addEditUserForm" property="selectedTPAUsersAsString"/>
			<tr id="tpaUserRow" <c:if test="${!contractAccess.signingAuthority}"> style="display:none;" </c:if>>
			<td colspan="3" class="borderLeft borderRight datacell2">
			<c:forEach items="${addEditUserForm.TPAUsers}" var="user">
			<c:if test="${addEditUserForm.enableTPAUserList}">
			<form:checkbox path="selectedTPAUsers" value="${user.profileId}">
${user.profileId}
</form:checkbox>
			</c:if>
			<c:if test="${!addEditUserForm.enableTPAUserList}">
			<form:checkbox path="selectedTPAUsers"  disabled="true" value="${user.profileId}" >
${user.profileId}
</form:checkbox>
			</c:if>
${user.firstName} ${user.lastName}<br/>
			</c:forEach>
			</td></tr>
		</c:if>
		</c:if>		
		
		<%-- LOANS - SIGNING AUTHORITY - END --%>

    
    	<c:if test="${addEditUserForm.showSubmissionsSection}">
    	<tr>
    		<td colspan="3" class="tablesubhead borderRight borderLeft boldEntry" >Submissions </td>
		</tr>
   		<c:if  test="${contractAccess.showUploadSubmissions}">
    	<tr>
    		<td width="219" class="datacell2  borderLeft boldEntry" >&nbsp;&nbsp;Create/upload submissions </td>
    		<c:if  test="${contractAccess.showUploadSubmissions}">
    		<input type="hidden" name="<%=uploadSubmissionsIniValName%>" value="enabled"/>
		    <td width="75" class="datacell2 " >
<form:radiobutton path="contractAccesses[${theIndex.index}].uploadSubmissions" value="true" />yes
			</td>
		    <td width="50" class="datacell2  borderRight" >
<form:radiobutton path="contractAccesses[${theIndex.index}].uploadSubmissions" value="false" />no
		    </td>
		    </c:if>
    		<c:if  test="${!contractAccess.showUploadSubmissions}">
    		<input type="hidden" name="<%=uploadSubmissionsIniValName%>" value="disabled"/>
		    <td width="75" class="datacell2 " >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].uploadSubmissions" value="true" />yes
			</td>
		    <td width="50" class="datacell2  borderRight" >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].uploadSubmissions" value="false" />no
		    </td>
		    </c:if>
 		    <ps:trackChanges name="addEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="uploadSubmissions"/>		    
		</tr>
		</c:if>
   		<c:if  test="${contractAccess.showCashAccount}">
    	<tr>
    		<td width="219" class="datacell2  borderLeft boldEntry" >&nbsp;&nbsp;Cash Account </td>
    		<c:if  test="${contractAccess.showCashAccount}">
    	<input type="hidden" name="<%=cashAccountIniValName%>" value="enabled"/>
		    <td width="75" class="datacell2 " >
<form:radiobutton path="contractAccesses[${theIndex.index}].cashAccount" value="true" />yes
			</td>
		    <td width="50" class="datacell2  borderRight" >
<form:radiobutton path="contractAccesses[${theIndex.index}].cashAccount" value="false" />no
		    </td>
		    </c:if>
    		<c:if  test="${!contractAccess.showCashAccount}">
    				<input type="hidden" name="<%=cashAccountIniValName%>" value="disabled"/>
		    <td width="75" class="datacell2 " >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].cashAccount" value="true" />yes
			</td>
		    <td width="50" class="datacell2  borderRight" >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].cashAccount" value="false" />no
		    </td>
		    </c:if>
 		    <ps:trackChanges name="addEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="cashAccount"/>		    
		</tr>
		</c:if>	
   		<c:if  test="${contractAccess.showDirectDebit}">
    	<tr>
    		<td width="219" class="datacell2  borderLeft boldEntry" >&nbsp;&nbsp;Direct debit </td>
    		<c:if  test="${addEditUserForm.enableDirectDebit}">
    		<input type="hidden" name="<%=directDebitIniValName%>" value="enabled"/>
		    <td width="75" class="datacell2 " >
<form:radiobutton onclick="setDirectDebitAccess(this, false);" path="contractAccesses[${theIndex.index}].directDebit" value="true" />yes
			</td>
		    <td width="50" class="datacell2  borderRight" >
<form:radiobutton onclick="setDirectDebitAccess(this, false);" path="contractAccesses[${theIndex.index}].directDebit" value="false" />no
		    </td>
		    </c:if>
    		<c:if  test="${!addEditUserForm.enableDirectDebit}">
    		<input type="hidden" name="<%=directDebitIniValName%>" value="disabled"/>
		    <td width="75" class="datacell2 " >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].directDebit" value="true" />yes
			</td>
		    <td width="50" class="datacell2  borderRight" >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].directDebit" value="false" />no
		    </td>
		    </c:if>
 		    <ps:trackChanges name="addEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="directDebit"/>		    
		</tr>
<c:if test="${not empty contractAccess.directDebitAccounts}">
  		<tr valign="top">
		       		<td class="datacell2 borderLeft borderRight" align="left" nowrap colspan="3">
		       			<%--
		       			This hidden property is used to ensure that the browser sends back something
		       			for the checkboxes value even when none of the checkboxes is selected. Without
		       			this hidden property, the browser will not send anything back when no checkbox is
		       			selected.
		       			--%>
<form:hidden path="contractAccesses[${theIndex.index}].selectedDirectDebitAccounts"/>		       		
<form:hidden path="contractAccesses[${theIndex.index}].selectedDirectDebitAccountIdsAsString"/> 

	 		    		<ps:trackChanges name="addEditUserForm" property="contractAccesses[${theIndex.index}].selectedDirectDebitAccountIdsAsString"/>
 <c:forEach items="${contractAccess.directDebitAccounts}" var="account" >

							&nbsp;&nbsp;
							<c:if test="${account.noAccess == false && addEditUserForm.enableDirectDebitAccounts}">
									<form:checkbox name="addEditUserForm"
									               path="contractAccesses[${theIndex.index}].selectedDirectDebitAccounts"  onchange='changeDirectDebitAccount(this)' value="${account.primaryKey}">
${account.primaryKey}
									</form:checkbox>
							</c:if>
							<c:if test="${account.noAccess == true || !addEditUserForm.enableDirectDebitAccounts}">
							
									<form:checkbox name="addEditUserForm"
												   disabled="true"
									               path="contractAccesses[${theIndex.index}].selectedDirectDebitAccounts" onchange='changeDirectDebitAccount(this)' value="${account.primaryKey}">
${account.primaryKey}
									</form:checkbox> 
							</c:if>
							<%--<%
									if (contractAccess.getSelectedDirectDebitAccountsAsList().contains(account)) {
									%>
<input type="hidden" name="<%= "contractAccess[" + value + "].selectedDirectDebitAccounts" %>"/>


									<%
									}
									%> --%>
${account.label}<br/>
</c:forEach> 
					</td>
		    	</tr>
</c:if>
		</c:if>

		
	<c:if test="${addEditUserForm.showLoansSection}">		
    	<tr>
    		<td colspan="3" class="tablesubhead borderRight borderLeft boldEntry" >Loans</td>
		</tr>
		<c:if test="${contractAccess.showInitiateLoans}">		
    	<tr>
    		<td width="219" class="datacell2  borderLeft boldEntry" >Initiate loans </td>
    		<c:if test="${addEditUserForm.enableInitiateLoans}">
		    	<td width="75" class="datacell2 " >
<form:radiobutton path="contractAccesses[${theIndex.index}].initiateLoans" value="true" />yes
		    	</td>
		    	<td width="50" class="datacell2  borderRight" >
<form:radiobutton path="contractAccesses[${theIndex.index}].initiateLoans" value="false" />no
		    	</td>
 		    	<ps:trackChanges name="addEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="initiateLoans"/>
 		    </c:if>
    		<c:if test="${!addEditUserForm.enableInitiateLoans}">
		    	<td width="75" class="datacell2 " >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].initiateLoans" value="true" />yes
		    	</td>
		    	<td width="50" class="datacell2  borderRight" >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].initiateLoans" value="false" />no
		    	</td>
 		    </c:if>
		</tr>
		</c:if>
		<c:if test="${contractAccess.showReviewLoans}">		
    	<tr>
    		<td width="219" class="datacell2  borderLeft boldEntry" >Review loans</td>
    		<c:if test="${addEditUserForm.enableReviewLoans}">
		    	<td width="75" class="datacell2 " >
<form:radiobutton path="contractAccesses[${theIndex.index}].reviewLoans" value="true" />yes
		    	</td>
		    	<td width="50" class="datacell2  borderRight" >
<form:radiobutton path="contractAccesses[${theIndex.index}].reviewLoans" value="false" />no
		    	</td>
 		    	<ps:trackChanges name="addEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="reviewLoans"/>		    
 		    </c:if>
    		<c:if test="${!addEditUserForm.enableReviewLoans}">
		    	<td width="75" class="datacell2 " >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].reviewLoans" value="true" />yes
		    	</td>
		    	<td width="50" class="datacell2  borderRight" >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].reviewLoans" value="false" />no
		    	</td>
 		    </c:if>
		</tr>
		</c:if>
	</c:if>
		
	<c:if test="${addEditUserForm.showiWithdrawalsSection}">		
    	<tr>
    		<td colspan="3" class="tablesubhead borderRight borderLeft boldEntry" >i:withdrawals</td>
		</tr>
		<c:if test="${contractAccess.showInitiateIWithdrawals}">		
    		<tr>
    			<td width="219" class="datacell2  borderLeft boldEntry" >Initiate i:withdrawals </td>
    				<c:if test="${addEditUserForm.enableInitiateWithdrawals}">
		   				<td width="75" class="datacell2 " >
<form:radiobutton path="contractAccesses[${theIndex.index}].initiateIWithdrawals" value="true" />yes
		    			</td>
		    			<td width="50" class="datacell2  borderRight" >
<form:radiobutton path="contractAccesses[${theIndex.index}].initiateIWithdrawals" value="false" />no
		    			</td>
 		    			<ps:trackChanges name="addEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="initiateIWithdrawals"/>
 		    		</c:if>
    				<c:if test="${!addEditUserForm.enableInitiateWithdrawals}">
		    			<td width="75" class="datacell2 " >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].initiateIWithdrawals" value="true" />yes
		    			</td>
		    			<td width="50" class="datacell2  borderRight" >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].initiateIWithdrawals" value="false" />no
		    			</td>
 		    		</c:if>
			</tr>
		</c:if>
		<c:if test="${contractAccess.showReviewIWithdrawals}">		
    		<tr>
    			<td width="219" class="datacell2  borderLeft boldEntry" >Review i:withdrawals</td>
    				<c:if test="${addEditUserForm.enableReviewWithdrawals}">
		    			<td width="75" class="datacell2 " >
<form:radiobutton path="contractAccesses[${theIndex.index}].reviewIWithdrawals" value="true" />yes
		    			</td>
		    			<td width="50" class="datacell2  borderRight" >
<form:radiobutton path="contractAccesses[${theIndex.index}].reviewIWithdrawals" value="false" />no
		    			</td>
 		    			<ps:trackChanges name="addEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="reviewIWithdrawals"/>		    
 		    		</c:if>
    				<c:if test="${!addEditUserForm.enableReviewWithdrawals}">
		    			<td width="75" class="datacell2 " >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].reviewIWithdrawals" value="true" />yes
		    			</td>
		    			<td width="50" class="datacell2  borderRight" >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].reviewIWithdrawals" value="false" />no
		   				</td>
 		    		</c:if>
				</tr>
		</c:if>
	</c:if>
		
		<c:if test="${addEditUserForm.showCensusManagementSection}">		
	    	<tr>
    			<td colspan="3" class="tablesubhead borderRight borderLeft boldEntry" >Census Management</td>
			</tr>
 			<c:if test="${contractAccess.showUpdateCensusData}">		
    			<tr>
    				<td width="219" class="datacell2  borderLeft boldEntry" >Update census data</td>
		    		<td width="75" class="datacell2 " >
<form:radiobutton path="contractAccesses[${theIndex.index}].updateCensusData" value="true" />yes
		    		</td>
		    		<td width="50" class="datacell2  borderRight" >
<form:radiobutton path="contractAccesses[${theIndex.index}].updateCensusData" value="false" />no
		    		</td>
 		    		<ps:trackChanges name="addEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="updateCensusData"/>		    
				</tr>			
			</c:if>																											
		</c:if>	
		
		<tr><td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td></tr>																													
	</table>
	<br/>
	
	</c:if>
	
	<table class="box" border="0" cellpadding="0" cellspacing="0" width="412">
      	<tr class="tablehead"  >
      		<td class="tableheadTD1" style="padding:5px;">
       		<img id="PlanServicesTableImage" style="CURSOR: pointer" onclick="toggleTable('PlanServicesTableImage','PlanServicesTable');" src="/assets/unmanaged/images/plus_icon.gif">
         		&nbsp;<b><content:getAttribute beanName="planGroupTitle" attribute="text"/></b>
         	</td>
    	</tr>
    	</table>
    	<table style="display:none" id="PlanServicesTable" width="412" cellpadding="0" cellspacing="0">
    			
    			<tr>
    			<td width="219" class="datacell2  borderLeft boldEntry" >404a-5 individual expense management</td>
    				<c:if test="${addEditUserForm.enableFeeAccess404A5}">
		    			<td width="75" class="datacell2 " >
<form:radiobutton path="contractAccesses[${theIndex.index}].feeAccess404A5" value="false" />yes
		    			</td>
		    			<td width="50" class="datacell2  borderRight" >
<form:radiobutton path="contractAccesses[${theIndex.index}].feeAccess404A5" value="true" />no
		    			</td>
 		    			<ps:trackChanges name="addEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="feeAccess404A5"/>		    
 		    		</c:if>
    				<c:if test="${!addEditUserForm.enableFeeAccess404A5}">
		    			<td width="75" class="datacell2 " >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].feeAccess404A5" value="false" />yes
		    			</td>
		    			<td width="50" class="datacell2  borderRight" >
<form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].feeAccess404A5" value="true" />no
		   				</td>
 		    		</c:if>
				</tr>			
				
			<tr><td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td></tr>
    	</table>
   
     	<script type="text/javascript" >
        	initializeSubmissionAccess('value');
        </script>
	</c:if>
</c:forEach>

