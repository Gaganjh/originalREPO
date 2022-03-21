<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%> 
<%-- imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditClientUserForm"%>
<%@ page import="com.manulife.pension.service.security.valueobject.DefaultRolePermissions" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditClientUserForm" %>

<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
 
String ADD_EDIT_USER_CONTRACT_LIST= Constants.ADD_EDIT_USER_CONTRACT_LIST;
pageContext.setAttribute("ADD_EDIT_USER_CONTRACT_LIST",ADD_EDIT_USER_CONTRACT_LIST,PageContext.PAGE_SCOPE);

AddEditClientUserForm clientAddEditUserForm=(AddEditClientUserForm)session.getAttribute("clientAddEditUserForm");
pageContext.setAttribute("clientAddEditUserForm",clientAddEditUserForm,PageContext.PAGE_SCOPE);
%>

<logicext:if name="layoutBean" property="param(task)" op="equal" value="add">
	<logicext:then>
<c:set var="actionPath" value="/do/profiles/addUser/" />
	</logicext:then>
	<logicext:elseif name="layoutBean" property="param(task)" op="equal" value="edit">
		<logicext:then>
<c:set var="actionPath" value="/do/profiles/editUser/" />
		</logicext:then>
	</logicext:elseif>
	<logicext:else>
<c:set var="actionPath" value="/do/profiles/addContract/" />
	</logicext:else>
</logicext:if>

<%-- <jsp:useBean id="actionPath" type="java.lang.String" /> --%>
<content:contentBean
    contentId="<%=ContentConstants.SPECI_SIGN_WARN_MESS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="specimensign"/>
<form:hidden path="clientAddEditUserForm.loginUserRole" />

<content:contentBean contentId="<%=ContentConstants.WARNING_ATLEAST_ONE_TRUSTEE_MAIL_RECIPIENT%>" type="<%=ContentConstants.TYPE_MESSAGE%>"  id="warningAtleastOneTrusteeMailRecipient" />
<content:contentBean contentId="<%=ContentConstants.MORE_THAN_ONE_TRUSTEE_MAIL_RECIPIENT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="moreThanOneTrusteeMailRecipient" />
<content:contentBean contentId="<%=ContentConstants.MORE_THAN_ONE_PPT_STATEMENT_CONSULTANT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="moreThanOnePptStatementConsultant" />
<content:contentBean contentId="<%=ContentConstants.INVALID_ROLE_FOR_TRUSTEE_MAIL_RECIPIENT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidRoleForTrusteeMailRecipient" />
<content:contentBean contentId="<%=ContentConstants.WARNING_ICC_DESIGNATE_AVAILABLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningIccDesignateAvailable" />
<content:contentBean contentId="<%=ContentConstants.WARNING_SEND_SERVICE_DESIGNATE_AVAILABLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningSendServiceDesignateAvailable" />

<c:if test="${ADD_EDIT_USER_CONTRACT_LIST}">
<c:set var="contractList" value="${ADD_EDIT_USER_CONTRACT_LIST}" scope="request"/> <%-- scope="request" --%>
</c:if>

<script type="text/javascript"	src="/assets/unmanaged/javascript/layer_functions.js"></script>
<script type="text/javascript">

var submitted=false;
var curTop = 0;
var curRight = 0;
var wranSignMess = '<content:getAttribute beanName="specimensign" attribute="text"/>';
var maxCharForComments = 500;
function showRoleDef() {
	contractAllSections();
	//show or hide role
	if (document.getElementById('roleDef').style.display == 'none')
		document.getElementById('roleDef').style.display = 'block';
	else
		document.getElementById('roleDef').style.display = 'none';

}
/*
 * Delete contract function will issue a server side delete contract action
 */
function doDelete(number){
	if (!submitted) {
		// first find the contractAccesses index
		var i = 0;
		var userProfileId = document.clientAddEditUserForm.profileId.value;
		while(document.getElementsByName("contractAccesses[" + i + "].contractNumber")[0].value != number) { i++; }
		var contractNumberElement = document.getElementsByName("contractAccesses[" + i + "].contractNumber")[0];
		var warnings = "";

		// Warning if contract does not have a Client or TPA profile with manage users permission
		if (document.getElementsByName("contractAccesses[" + i + "].lastUserWithManageUsers")[0].value == "true") {
			warnings += "Contract " + contractNumberElement.value +" does not have any users with Manage Users permission.\n";
		}

		// Warning if contract does not have a Client or TPA profile with submissions access permission
		if (document.getElementsByName("contractAccesses[" + i + "].lastUserWithSubmissionsAccess")[0].value == "true") {
			warnings += "Contract " + contractNumberElement.value +" does not have any users with  Submissions Access permission.\n";
		}

		// Warning if contract does not have a Client or TPA profile with signing authority (approve i:withdrawals @ before) permission
		if (document.getElementsByName("contractAccesses[" + i + "].lastUserWithSigningAuthority")[0].value == "true") {
			warnings += "Contract " + number +" does not have any users with Signing Authority permission.\n";
		}

		// Warning if contract does not have a Client or TPA profile with review i:withdrawals permission
		// This message can come when who Will Review Withdrawal Requests = "TPA"
		if (document.getElementsByName("contractAccesses[" + i + "].lastUserWithReviewIWithdrawals")[0].value == "true") {
			warnings += "Contract " + number +" does not have any users with Review i:withdrawals permission.\n";
		}

		// Warning if contract does not have a Client or TPA profile with review loans permission
		// This message can come when who Will Review Loan Requests = "TPA"
		if (document.getElementsByName("contractAccesses[" + i + "].lastUserWithReviewLoansPermission")[0].value == "true") {
			warnings += "Contract " + number +" does not have any users with Review loans permission.\n";
		}

		// Warning if contract does not have a Client profile with review i:withdrawals permission
		// This message can come when who Will Review Withdrawal Requests = "Plan Sponsor"
		if (document.getElementsByName("contractAccesses[" + i + "].lastClientUserWithReviewIWithdrawals")[0].value == "true") {
			warnings += "Contract " + number +" does not have any client users with Review i:withdrawals permission.\n";
		}

		// Warning if contract does not have a Client profile with review loans permission
		// This message can come when who Will Review Loan Requests = "Plan Sponsor"
		if (document.getElementsByName("contractAccesses[" + i + "].lastClientUserWithReviewLoans")[0].value == "true") {
			warnings += "Contract " + number +" does not have any client users with Review loans permission.\n";
		}

		// Check contract does have a Trustee Mail Recipient assigned
		var trusteeMailRecipientProfilId = document.getElementsByName("contractAccesses[" + i + "].trusteeMailRecipientProfileId")[0].value;
		var trusteeMailRecipient = getRadioValue("contractAccesses[" + i + "].trusteeMailRecepient");
		if (userProfileId == trusteeMailRecipientProfilId && trusteeMailRecipient == "true") {
			var warMsg = '<content:getAttribute beanName="warningAtleastOneTrusteeMailRecipient" attribute="text"/>';
			warMsg = warMsg.replace(/\{0\}/, contractNumberElement.value);
			warnings += "Warning! " + warMsg + "\n";
		}

		if (confirm(warnings + "Warning! Delete contract number " + number + " from this profile?")){
			submitted = true;
			document.clientAddEditUserForm.selectedContractNumber.value = number;
			document.clientAddEditUserForm.action = '${actionPath}?action=deleteContract';
			document.clientAddEditUserForm.submit();
		} else {
			return false;
		}
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function doChangePermissions(number){
	if (!submitted) {
		submitted = true;
		document.clientAddEditUserForm.selectedContractNumber.value = number;
		document.clientAddEditUserForm.action = '${actionPath}?action=changePermissions';
		document.clientAddEditUserForm.submit();
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}
/*
 * Change web access field
 * Issue a server side call for change web access field
 */
function changeWebAccess(){
	if (!submitted) {
		//alert(document.clientAddEditUserForm.webAccess[0].value);
		if (document.clientAddEditUserForm.webAccess[0].value=='Yes' && document.clientAddEditUserForm.webAccess[1].checked){
			if (confirm("You have changed the web access for this profile to 'No'. Please select OK to continue or Cancel to return to the page."))
			{
				submitted = true;
				document.clientAddEditUserForm.action = '${actionPath}?action=changeWebAccess';
				document.clientAddEditUserForm.submit();
			} else {
				document.clientAddEditUserForm.webAccess[0].checked="checked";
				return;
			}
		}
		if (document.clientAddEditUserForm.webAccess[1].value=='No' && document.clientAddEditUserForm.webAccess[0].checked){
			if (confirm("Warning! You have changed the profile to has web access."))
			{
				submitted = true;
				document.clientAddEditUserForm.action = '${actionPath}?action=changeWebAccess';
				document.clientAddEditUserForm.submit();
			} else {
				document.clientAddEditUserForm.webAccess[1].checked="checked";
				return;
			}
		}
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return;
	 }
}

var roleOptionIndex;

function storeCurrentRole(obj) {
	roleOptionIndex=obj.selectedIndex;
}

/*
 * Change contract role will issue a server side action
 */
function changeRole(obj){
	if (!submitted) {
		var position1 = obj.name.indexOf('[');
		var position2 = obj.name.indexOf(']');
		var index = obj.name.substring(position1+1,position2);
		var contractNumber = "contractNumber" + index;
		var number = document.getElementById(contractNumber).value;
		var iccPermission = document.getElementsByName("contractAccesses[" + index + "].iccDesignate")[0];
		var doChange = false;
		var roleLabel = document.getElementsByName("contractAccesses[" + index + "].planSponsorSiteRole.label")[0];
		var optionElement = obj.options[obj.selectedIndex]
		if (obj.options[roleOptionIndex].value == ""){
			doChange = true;
		} else if((null != iccPermission) && (('PPY' == optionElement.value) || ('INC' == optionElement.value)) && (true == iccPermission.checked)){
			doChange = true;
		} else {
			var warnMessage = "The profile role has been changed for contract " + number + ". All permissions will be set to system defaults. Select OK to continue or Cancel to return.";
			doChange = confirm(warnMessage);
		}
		if (doChange)	{
			// change the role label (used on the confirmation page)
			roleLabel.value = optionElement.text;
            submitted = true;
    		document.clientAddEditUserForm.selectedContractNumber.value = number;
    		document.clientAddEditUserForm.action = '${actionPath}?action=changeRole';
    		document.clientAddEditUserForm.submit();
		} else {
			// revert back to original role
			obj.selectedIndex = roleOptionIndex;
		}
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function addContract(){
	if (!submitted) {
		//alert(document.clientAddEditUserForm1.contractToAdd.value);
		if (document.clientAddEditUserForm.contractToAdd.value=='select one') {
			alert("Please select a valid contract!");
		 } else {
			submitted = true;
			document.clientAddEditUserForm.action = '${actionPath}?action=addContract';
			document.clientAddEditUserForm.submit();
		 }
	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
	 }
}

/*
 * Change manage users
 */
function changeManageUsers(obj){
	var position = obj.name.indexOf('[');
	var number = obj.name.charAt(++position);

}

/*
 * Change selected access
 */
function changeSelectedAccess(obj){
	var position = obj.name.indexOf('[');
	var number = obj.name.charAt(++position);

	var manageUserDiv = "manageUserDiv" + number;
	var manageUsersStr = "contractAccesses[" + number +"].manageUsers";

	if (obj.value=='true')
	{

		// Need to set selected permission values to "No"
		var editContractServiceFeaturesDefault = "contractAccesses[" + number +"].userPermissions.editContractServiceFeaturesDefault";
		if (document.getElementsByName(editContractServiceFeaturesDefault)[0].value != "<%= DefaultRolePermissions.TRUE %>") {
			var editContractServiceFeatures = "contractAccesses[" + number +"].userPermissions.editContractServiceFeatures";
			document.getElementsByName(editContractServiceFeatures)[0].value = "false";
		}

		var downloadReportsDefault = "contractAccesses[" + number +"].userPermissions.downloadReportsDefault";
		if (document.getElementsByName(downloadReportsDefault)[0].value != "<%= DefaultRolePermissions.TRUE %>") {
			var downloadReports = "contractAccesses[" + number +"].userPermissions.downloadReports";
			document.getElementsByName(downloadReports)[0].value = "false";
		}

		var employerStatementsDefault = "contractAccesses[" + number +"].userPermissions.employerStatementsDefault";
		if (document.getElementsByName(employerStatementsDefault)[0].value != "<%= DefaultRolePermissions.TRUE %>") {
			var employerStatements = "contractAccesses[" + number +"].userPermissions.employerStatements";
			document.getElementsByName(employerStatements)[0].value = "false";
		}

		var viewAllUsersSubmissionsDefault = "contractAccesses[" + number +"].userPermissions.viewAllUsersSubmissionsDefault";
		if (document.getElementsByName(viewAllUsersSubmissionsDefault)[0].value != "<%= DefaultRolePermissions.TRUE %>") {
			var viewAllUsersSubmissions = "contractAccesses[" + number +"].userPermissions.viewAllUsersSubmissions";
			document.getElementsByName(viewAllUsersSubmissions)[0].value = "false";
		}

		var updateCensusDataDefault = "contractAccesses[" + number +"].userPermissions.updateCensusDataDefault";
		if (document.getElementsByName(updateCensusDataDefault)[0].value != "<%= DefaultRolePermissions.TRUE %>") {
			var updateCensusData = "contractAccesses[" + number +"].userPermissions.updateCensusData";
			document.getElementsByName(updateCensusData)[0].value = "false";
		}

		var viewSalaryDefault = "contractAccesses[" + number +"].userPermissions.viewSalaryDefault";
		if (document.getElementsByName(viewSalaryDefault)[0].value != "<%= DefaultRolePermissions.TRUE %>") {
			var viewSalary = "contractAccesses[" + number +"].userPermissions.viewSalary";
			document.getElementsByName(viewSalary)[0].value = "false";
		}
	}
}

function doSaveUser(){
	if (!submitted) {
var loginUserRole = '${clientAddEditUserForm.loginUserRole}';
		var warnings = getWarnings(loginUserRole);
		if (warnings != "") {
			warnings += "Do you want to continue with these changes?";
			submitted = confirm(warnings);
			if(submitted) { //set max chars 500 for comments
				if(document.clientAddEditUserForm.comments) {
					setMaxLength(document.clientAddEditUserForm.comments, maxCharForComments);
				}	
			}	
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

function getWarnings(loginUserRole) {
	var warnings = "";
	var loginUserRoleValue = loginUserRole;
	var userProfileId = document.clientAddEditUserForm.profileId.value;

	var webAccessValue = getRadioValue("webAccess");

	var i = 0;
	var contractNumberElement;
	while((contractNumberElement = document.getElementsByName("contractAccesses[" + i + "].contractNumber")[0]) != null) {
		// Check the primary contact
		var primaryContactInd = getRadioValue("contractAccesses[" + i + "].primaryContact");
		var primaryContactProfileId = document.getElementsByName("contractAccesses[" + i + "].primaryContactProfileId")[0].value;
		if (primaryContactInd == "true" && primaryContactProfileId > 0 && userProfileId != primaryContactProfileId) {
			var primaryContactName = document.getElementsByName("contractAccesses[" + i + "].primaryContactName")[0].value;
			warnings += "Warning! " + primaryContactName + " is currently designated as the Primary Contact for contract " + contractNumberElement.value + 
						".\nSelect OK to replace the primary contact or Cancel to change the current profile information.\n";
		}

		// Check the mail recipient
		var mailRecipientInd = getRadioValue("contractAccesses[" + i + "].mailRecepient");
		var mailRecipientProfileId = document.getElementsByName("contractAccesses[" + i + "].mailRecipientProfileId")[0].value;
		if (mailRecipientInd == "true" && mailRecipientProfileId > 0 && userProfileId != mailRecipientProfileId) {
			var mailRecipientName = document.getElementsByName("contractAccesses[" + i + "].mailRecipientName")[0].value;
			warnings += "Warning! " + mailRecipientName + " is currently designated as the Mail Recipient for contract " + contractNumberElement.value + 
						".\nSelect OK to replace the mail recipient or Cancel to change the current profile information.\n";
		}

		var trusteeMailRecipient = getRadioValue("contractAccesses[" + i + "].trusteeMailRecepient");
		// Check contract does have a Trustee Mail Recipient assigned
		var trusteeMailRecipientProfilId = document.getElementsByName("contractAccesses[" + i + "].trusteeMailRecipientProfileId")[0].value;
		if ((trusteeMailRecipientProfilId == 0 && trusteeMailRecipient != "true") || (userProfileId == trusteeMailRecipientProfilId && trusteeMailRecipient != "true")) {
			var warMsg = '<content:getAttribute beanName="warningAtleastOneTrusteeMailRecipient" attribute="text"/>';
			warMsg = warMsg.replace(/\{0\}/, contractNumberElement.value);
			warnings += "Warning! " + warMsg + "\n";
		}
		
		// Check the Trustee mail recipient
		var trusteeMailRecipientProfilId = document.getElementsByName("contractAccesses[" + i + "].trusteeMailRecipientProfileId")[0].value;
		if (trusteeMailRecipient == "true" && trusteeMailRecipientProfilId > 0 && userProfileId != trusteeMailRecipientProfilId) {
			var trusteeMailRecipientName = document.getElementsByName("contractAccesses[" + i + "].trusteeMailRecipientName")[0].value;
			var warMsg = '<content:getAttribute beanName="moreThanOneTrusteeMailRecipient" attribute="text"/>';
			warMsg = warMsg.replace(/\{0\}/, trusteeMailRecipientName);
			warMsg = warMsg.replace(/\{1\}/, contractNumberElement.value);
			warnings += "Warning! " + warMsg + "\n";
		}
		
		// Check the Participant Staement Consultant		
		var participantStatementConsultant = getRadioValue("contractAccesses[" + i + "].statementIndicator");
		var participantStatementConsultantProfilId = document.getElementsByName("contractAccesses[" + i + "].participantStatementConsultantProfileId")[0].value;
		if (participantStatementConsultant == "true" && participantStatementConsultantProfilId > 0 && userProfileId != participantStatementConsultantProfilId) {
			var participantStatementConsultantName = document.getElementsByName("contractAccesses[" + i + "].participantStatementConsultantName")[0].value;
			var warMsg = '<content:getAttribute beanName="moreThanOnePptStatementConsultant" attribute="text"/>';
			warMsg = warMsg.replace(/\{0\}/, participantStatementConsultantName);
			warMsg = warMsg.replace(/\{1\}/, contractNumberElement.value);
			warnings += "Warning! " + warMsg + "\n";
		}

		// Check the icc Designate
		var iccDesignateInd = getRadioValue("contractAccesses[" + i + "].iccDesignate");
		var iccDesignateProfileId = document.getElementsByName("contractAccesses[" + i + "].iccDesignateProfileId")[0].value;
		if (iccDesignateInd == "true" && iccDesignateProfileId > 0 && userProfileId != iccDesignateProfileId) {	        
			var iccDesignateName = document.getElementsByName("contractAccesses[" + i + "].iccDesignateName")[0].value;
			var warMsg = '<content:getAttribute beanName="warningIccDesignateAvailable" attribute="text"/>';
			warMsg = warMsg.replace(/\{0\}/, iccDesignateName);
			warMsg = warMsg.replace(/\{1\}/, contractNumberElement.value);
			warnings += "Warning! " + warMsg + "\n"; 
		}
		
		// Check the SEND Service Designate
		var sendServiceDesignateInd = getRadioValue("contractAccesses[" + i + "].sendServiceDesignate");
		var sendServiceDesignateProfileId = document.getElementsByName("contractAccesses[" + i + "].sendServiceDesignateProfileId")[0].value;
		if (sendServiceDesignateInd == "true" && sendServiceDesignateProfileId > 0 && userProfileId != sendServiceDesignateProfileId) {	        
			var sendServiceDesignateName = document.getElementsByName("contractAccesses[" + i + "].sendServiceDesignateName")[0].value;
			var warMsg = '<content:getAttribute beanName="warningSendServiceDesignateAvailable" attribute="text"/>';
			warMsg = warMsg.replace(/\{0\}/, sendServiceDesignateName);
			warMsg = warMsg.replace(/\{1\}/, contractNumberElement.value);
			warnings += "Warning! " + warMsg + "\n"; 
		}
		
		var selectedAccessValue;
		var selectedAccessElements = document.getElementsByName("contractAccesses[" + i + "].selectedAccess");
		if (selectedAccessElements.length == 1) {
			selectedAccessValue = selectedAccessElements[0].value;
		} else {
			selectedAccessValue = getRadioValue("contractAccesses[" + i + "].selectedAccess");
		}

		// Warning if contract does not have a Client or TPA profile with manage users permission
		if (document.getElementsByName("contractAccesses[" + i + "].lastUserWithManageUsers")[0].value == "true") {
			var manageUsersValue;
			var manageUsersElements = document.getElementsByName("contractAccesses[" + i + "].manageUsers");
			if (manageUsersElements.length == 1) {
				manageUsersValue = manageUsersElements[0].value;
			} else {
				manageUsersValue = getRadioValue("contractAccesses[" + i + "].manageUsers");
			}

			if (webAccessValue == "No" || manageUsersValue == "false" || selectedAccessValue == "true") {
				warnings += "Warning! No client users for contract " + contractNumberElement.value +" will have Manage Users permission.\n";
			}
		}

		// Warning if contract does not have a Client or TPA profile with Submissions Access permission
		if (document.getElementsByName("contractAccesses[" + i + "].lastUserWithSubmissionsAccess")[0].value == "true") {
			if (webAccessValue == "No" || document.getElementsByName("contractAccesses[" + i + "].userPermissions.viewSubmissions")[0].value == "false" || selectedAccessValue == "true") {
				warnings += "Warning! No client users for contract " + contractNumberElement.value +" will have Submissions Access permission.\n";
			}
		}

		// Warning if contract does not have a Client or TPA profile with signing authority (approve i:withdrawals permission)
		if (document.getElementsByName("contractAccesses[" + i + "].lastUserWithSigningAuthority")[0].value == "true") {
			if (webAccessValue == "No" || document.getElementsByName("contractAccesses[" + i + "].userPermissions.signingAuthority")[0].value == "false" || selectedAccessValue == "true") {
				warnings += "Contract " + contractNumberElement.value +" will no longer have a user with Signing Authority permission.\n";
			}
		}

		// Warning if contract does not have a Client or TPA profile with review i:withdrawals permission
		if (document.getElementsByName("contractAccesses[" + i + "].lastUserWithReviewIWithdrawals")[0].value == "true") {
			if (webAccessValue == "No" || document.getElementsByName("contractAccesses[" + i + "].userPermissions.reviewWithdrawals")[0].value == "false" || selectedAccessValue == "true") {
				warnings += "Contract " + contractNumberElement.value +" will no longer have a user with Review i:withdrawals permission.\n";
			}
		}

		// Warning if contract does not have a Client or TPA profile with review loans permission
		if (document.getElementsByName("contractAccesses[" + i + "].lastUserWithReviewLoansPermission")[0].value == "true") {
			if (webAccessValue == "No" || document.getElementsByName("contractAccesses[" + i + "].userPermissions.reviewLoans")[0].value == "false" || selectedAccessValue == "true") {
				warnings += "Contract " + contractNumberElement.value +" will no longer have a user with Review loans permission.\n";
			}
		}

		// Warning if contract does not have a Client profile with review i:withdrawals permission
		// This message can come when who Will Review Withdrawal Requests = "Plan Sponsor"
		if (document.getElementsByName("contractAccesses[" + i + "].lastClientUserWithReviewIWithdrawals")[0].value == "true") {
			if (webAccessValue == "No" || document.getElementsByName("contractAccesses[" + i + "].userPermissions.reviewWithdrawals")[0].value == "false" || selectedAccessValue == "true") {
				warnings += "Contract " + contractNumberElement.value +" will no longer have a user with Review i:withdrawals permission.\n";
			}
		}

		// Warning if contract does not have a Client profile with review loans permission
		// This message can come when who Will Review Loan Requests = "Plan Sponsor"
		if (document.getElementsByName("contractAccesses[" + i + "].lastClientUserWithReviewLoans")[0].value == "true") {
			if (webAccessValue == "No" || document.getElementsByName("contractAccesses[" + i + "].userPermissions.reviewLoans")[0].value == "false" || selectedAccessValue == "true") {
				warnings += "Contract " + contractNumberElement.value +" will no longer have a user with Review loans permission.\n";
			}
		}				
		//new message		
		<%-- <%if (clientAddEditUserForm.getChanges().isChanged())			
		{%> --%>
		
			if( loginUserRoleValue == "Trustee" )
			{		
			 	 var selectedRole = document.getElementsByName("contractAccesses[" + i + "].planSponsorSiteRole.value")[i].value;
			 	 var clonedRoleLength = document.getElementsByName("clonedForm.contractAccesses[" + i + "].planSponsorSiteRole.value").length;
			 	 if ( clonedRoleLength != 0 )
				  {	
				  	var clonedRole = document.getElementsByName("clonedForm.contractAccesses[" + i + "].planSponsorSiteRole.value")[i].value;
					if (selectedRole == 'TRT' || (selectedRole == 'AUS' && clonedRole != 'TRT'))
					{
						warnings += wranSignMess +" "+ contractNumberElement.value + "\n";
					}			
				  }
				 else 
				  {
				  	if (selectedRole == 'TRT' || selectedRole == 'AUS')
					{
						warnings += wranSignMess +" "+ contractNumberElement.value + "\n";
					}			
				  }
			}
		<%-- <%}%> --%>

		i++;
	}
	return warnings;
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

function setMaxLength(Object, MaxLen)
{
  if(Object != null && Object != undefined && Object.value.length >= MaxLen) {
  	Object.value = Object.value.substring(0, MaxLen);
  }	
}

function isFormChanged()
{
	return changeTracker.hasChanged();
}

registerTrackChangesFunction(isFormChanged);

<%
if (request.getAttribute("duplicateSSNWarning") != null) {
	String warn = (String) request.getAttribute("duplicateSSNWarning");
%>
function doOnload() {
	if(confirm("<%=warn%>")) {
		submitted = true;
		document.clientAddEditUserForm.ignoreSSNWarning.value = 'true';
		document.clientAddEditUserForm.action = '${actionPath}?action=save';
		document.clientAddEditUserForm.submit();
	}
}
<%}%>

</script>

<ps:form cssClass="margin-bottom:0;" method="POST"
	action="${actionPath}" name="clientAddEditUserForm" modelAttribute="clientAddEditUserForm">
<form:hidden path="selectedContractNumber"/>
<form:hidden path="profileId"/>
<form:hidden path="ignoreSSNWarning"/>
<form:hidden path="addStaggingContact"/>
<form:hidden path="staggingContactId"/>
	<table border="0" cellpadding="0" cellspacing="0" width="700">
		<tbody>
			<tr>
				<td width="475"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td width="5"><img src="/assets/unmanaged/images/s.gif"	height="1" width="1"></td>
				<td width="170"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
			</tr>
			<tr>
				<td><span class="highlight">*</span>Required Information
				<%-- error section --%>
				<p><div id="errordivcs"><content:errors scope="session" /></div></p>
				</td>
			</tr>
			<tr>
				<td><img src="/assets/unmanaged/images/s.gif" height="1" width="465"></td>
			</tr>
			<tr>
				<td width="475">
				<table border="0" cellpadding="0" cellspacing="0" width="450">
					<tbody>
						<tr>
							<td>
							<table border="0" cellpadding="0" cellspacing="0" width="450">
								<tbody>
									<tr class="tablehead">
										<td class="tableheadTD1" colspan="3"><b><content:getAttribute id="layoutPageBean" attribute="body1Header" /></b></td>
									</tr>
									<tr class="datacell1">
										<td class="databorder"><img	src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td class="datacell1" align="center">
										<table border="0" cellpadding="3" cellspacing="0" width="450">
											<tbody>
												<%-- include profile common section --%>
												<jsp:include flush="true" page="addEditProfileCommonSection.jsp" />
												<tr class="datacell1">
													<td>
													<%-- CEX8 SSN Label --%>
													<logicext:if name="clientAddEditUserForm" property="webAccess" op="equal" value="Yes">
														<logicext:then>
															<ps:label fieldId="ssn" mandatory="true">Social security number</ps:label>
														</logicext:then>
														<logicext:else>
															<ps:label fieldId="ssn" mandatory="false">Social security number</ps:label>
														</logicext:else>
													</logicext:if>
													</td>
													<td colspan="2"><span class="bodytext">
													<font face="Verdana, Arial, Geneva, sans-serif" size="1">
													<%--checking add page or edit page--%>
													<logicext:if name="layoutBean" property="param(task)" op="equal" value="add">
														<%-- add page ssn empty --%>
														<logicext:then>
														<c:if test="${userProfile.role.roleId ne 'ICC'}">
																					<form:password path="ssn.digits[0]" showPassword="true"
																						cssClass="inputField" size="3" maxlength="3" />
																					<form:password path="ssn.digits[1]" showPassword="true"
																						cssClass="inputField" size="2" maxlength="2" />
																					<form:password path="ssn.digits[2]" showPassword="true"
																						autocomplete="off" cssClass="inputField"
																						size="4" maxlength="4" />
																						</c:if>
																						<c:if test="${userProfile.role.roleId eq 'ICC'}">
																					<form:password path="ssn.digits[0]" showPassword="true"
																						cssClass="inputField" size="3" maxlength="3" />
																					<form:password path="ssn.digits[1]" showPassword="true"
																						cssClass="inputField" size="2" maxlength="2" />
																					<form:input path="ssn.digits[2]" 
																						autocomplete="off" cssClass="inputField"
																						size="4" maxlength="4" />
																						</c:if>
															<ps:trackChanges name="clientAddEditUserForm" property="ssn.digits[0]" /> <ps:trackChanges name="clientAddEditUserForm" property="ssn.digits[1]" /> <ps:trackChanges name="clientAddEditUserForm" property="ssn.digits[2]" />
														</logicext:then>
														<%-- edit page ssn need mark the first two digits SCC8  --%>
														<logicext:else>
																					<c:if test="${userProfile.role.roleId ne 'ICC'}">
																						<form:password path="ssn.digits[0]"  value="${clientAddEditUserForm.ssn.digits[0]}"
																							showPassword="true" cssClass="inputField"
																							size="3" maxlength="3" />
																						<form:password path="ssn.digits[1]"   value="${clientAddEditUserForm.ssn.digits[1]}"
																							showPassword="true" cssClass="inputField"
																							size="2" maxlength="2" />
																						<form:password path="ssn.digits[2]"   value="${clientAddEditUserForm.ssn.digits[2]}"
																							showPassword="true" autocomplete="off"
																							cssClass="inputField" size="4" maxlength="4" />
																					</c:if>
																					<c:if test="${userProfile.role.roleId eq 'ICC'}">
																						<form:password path="ssn.digits[0]"   value="${clientAddEditUserForm.ssn.digits[0]}"
																							showPassword="true" cssClass="inputField"
																							size="3" maxlength="3" />
																						<form:password path="ssn.digits[1]"   value="${clientAddEditUserForm.ssn.digits[1]}"
																							showPassword="true" cssClass="inputField"
																							size="2" maxlength="2" />
																						<form:input path="ssn.digits[2]"   value="${clientAddEditUserForm.ssn.digits[2]}"
																							autocomplete="off" cssClass="inputField" size="4"
																							maxlength="4" />
																					</c:if>
																					<ps:trackChanges name="clientAddEditUserForm" property="ssn.digits[0]" /> <ps:trackChanges name="clientAddEditUserForm" property="ssn.digits[1]" /> <ps:trackChanges name="clientAddEditUserForm" property="ssn.digits[2]" />
														</logicext:else>
													</logicext:if>
													</font></span>
													</td>
												</tr>
												<%-- edit page web access begin --%>
												<%-- display web access field rule scc104 --%>
														<logicext:if name="clientAddEditUserForm" property="canManageAllContracts" op="equal" value="true">
															<logicext:then>
															<tr class="datacell1">
																<td valign="top"><strong>Web access<span class="highlight">*</span> </strong></td>
																<td width="65" align="left" nowrap="nowrap"><form:radiobutton path="webAccess" value="Yes" onclick="changeWebAccess();" />yes</td>
<td width="160" align="left" nowrap="nowrap"><form:radiobutton onclick="changeWebAccess();" path="webAccess" value="No"/>no
																<ps:trackChanges name="clientAddEditUserForm" property="webAccess" /></td>
															</tr>
															</logicext:then>
														</logicext:if>
												<%-- web access field end --%>
												<%-- user name & password just for edit --%>
												<logicext:if name="layoutBean" property="param(task)" op="equal" value="addContract">
												<logicext:then>
												<%-- SCC9 user name & password --%>
												<logicext:if name="clientAddEditUserForm" property="webAccess"	op="equal" value="Yes">
													<logicext:then>
													    <%-- Username should be displayed for all the external users --%>
														<ps:isExternal name="userProfile" property="role">
															<ps:permissionAccess permissions="EXMN">
																<tr class="datacell1">
																	<td valign="top"><strong>Username</strong></td>
<td colspan="2" valign="top">${clientAddEditUserForm.userName}</td>
																</tr>
															</ps:permissionAccess>
														</ps:isExternal>
														<tr class="datacell1">
															<td valign="top"><strong>Password status</strong></td>
															<td colspan="2" valign="top"><ps:passwordState	name="clientAddEditUserForm" property="passwordState" /></td>
														</tr>
													</logicext:then>
												</logicext:if>
												</logicext:then>
												</logicext:if>
												
												<ps:isInternalUser name="userProfile" property="role">
												<tr class="datacell1">
													<td valign="top">
														<ps:label fieldId="contractToAdd">Comments</ps:label>
													</td>
													<td colspan="2" valign="top">
<form:textarea path="comments" cols="30" onblur="setMaxLength(this, maxCharForComments)" onkeyup="setMaxLength(this, maxCharForComments)" rows="3" />
													</td>
												</tr>
												<script type="text/javascript">
													changeTracker.trackElement('comments', document.clientAddEditUserForm.comments.value);
												</script>
												</ps:isInternalUser>
												
												<%-- add contract control drop down or entry box ICE3.1 --%>
												<%-- external user manager gets a drop down list --%>
												<ps:isExternal name="userProfile" property="role">
												<ps:permissionAccess permissions="EXMN">
												<c:if test="${not empty ADD_EDIT_USER_CONTRACT_LIST}">
												<c:if test="${not empty ADD_EDIT_USER_CONTRACT_LIST}">
													
															<tr class="datacell1">
																<td valign="top">&nbsp;</td>
																<td valign="top">&nbsp;</td>
																<td valign="top">&nbsp;</td>
															</tr>
															<tr class="datacell1">
																<td valign="top"><ps:label fieldId="contractToAdd">Add contract number</ps:label></td>
																<td colspan="2" valign="top">
																<form:select path="contractToAdd">
																<form:options items="${contractList}" itemValue="value" itemLabel="label" />
</form:select>
																<a style="CURSOR: pointer; color: #000099; text-decoration: underline;" onclick="addContract();">Add</a>
																</td>
															</tr>
</c:if>
													</c:if>
												</ps:permissionAccess>
												</ps:isExternal>
												<%-- internal gets a text entry box --%>
												<ps:isInternalUser name="userProfile" property="role">
												<ps:permissionAccess permissions="EXMN">
													<tr class="datacell1">
														<td valign="top"><ps:label fieldId="contractToAdd">Add contract number</ps:label></td>
														<td colspan="2" valign="top">
														
<form:input path="contractToAdd" maxlength="7" size="10" cssClass="inputField"/>
														<a style="CURSOR: pointer; color: #000099; text-decoration: underline;" onclick="addContract();">Add</a>
														</td>
													</tr>
												</ps:permissionAccess>
												</ps:isInternalUser>
												<%-- role definitions link and icon scc105 --%>
												<logicext:if name="clientAddEditUserForm" property="ownBusnessConvertedContract" op="equal" value="true">
													<logicext:then>
														<tr class="datacell1">
															<td valign="top">&nbsp;</td>
															<td colspan="2" valign="middle" style="CURSOR: pointer"	onclick="showRoleDef()">
															<div align="right">Role definitions&nbsp;<img src="/assets/unmanaged/images/layer_icon.gif" width="16" height="9"></div></td>
														</tr>
													</logicext:then>
												</logicext:if>
											</tbody>
										</table>
										</td>
										<td class="databorder"><img	src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									</tr>
									<%-- include contract access item --%>
									<jsp:include flush="true" page="contractAccessItem.jsp" />
									<tr><td class="databorder" colspan="3"><img	src="/assets/unmanaged/images/s.gif" height="1" width="1"></td></tr>
								</tbody>
							</table>
							<p>
							<table border="0" cellpadding="0" cellspacing="0" width="450">
								<tbody>
									<tr>
										<td colspan="2" align="center">
<input type="submit" class="button100Lg" onclick="return doCancelChanges(clientAddEditUserForm);" name="action" value="cancel" /><%-- property="actionLabel" --%>
										</td>
										<td width="175" align="center">
<input type="submit" class="button100Lg" onclick="return doSaveUser();" name="action" value="save" /><%-- property="actionLabel" --%></td>
										<script type="text/javascript" >
											var onenter = new OnEnterSubmit('action', 'save');
											onenter.install();
										</script>
									</tr>
								</tbody>
							</table>
							</p>
							</td>
						</tr>
					</tbody>
				</table>
				</td>
				<td colspan="2" align="left" valign="top">
				<jsp:include flush="true" page="roledefinition.jsp" />
				</td>
			</tr>
		</tbody>
	</table>
</ps:form>
<script type="text/javascript" >
document.forms["clientAddEditUserForm"].firstName.focus();
</script>


