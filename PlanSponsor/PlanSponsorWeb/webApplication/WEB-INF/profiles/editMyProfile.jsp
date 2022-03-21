<%-- taglib used --%>
<%@page import="java.util.Objects"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="cont" uri="manulife/tags/content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.profiles.EditMyProfileForm" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.security.role.ExternalUser" %>
<%@ page import="com.manulife.pension.service.security.role.InternalUser" %>
<%@ page import="com.manulife.pension.delegate.EnvironmentServiceDelegate" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="psw"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>


<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
com.manulife.pension.service.security.valueobject.UserInfo myUserInfo = com.manulife.pension.delegate.SecurityServiceDelegate
.getInstance().getUserInfo(((UserProfile) pageContext.getAttribute("userProfile")).getPrincipal());

String deapiStatus = null;
if (null != request.getSession(false) && null != request.getSession(false).getAttribute("Deapi")) {
	deapiStatus = (String) request.getSession(false).getAttribute("Deapi");
}
%>
<psw:contentBean
	contentId="<%=ContentConstants.DE_API_ERROR_MESSAGE%>"
	type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
	beanName="DeapiErrorMessage" />
<c:set var="theForm" value="${editMyProfileForm}" scope="session"/>
<c:set scope="page" var="checkvalue" value="${theForm.getContractAccesses().size()}"/>
<style>
#WzTtDiV {
	width: 720px !important;
}
#WzBoDy table tbody td{
	white-space: normal !important;
}
</style>

<script  type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>

<script type="text/javascript" >
var validationStatus = false;
var myProfileUserName;
<% if(Objects.nonNull(myUserInfo.getUserName())) { %>
	myProfileUserName = '<%=myUserInfo.getUserName()%>';
<% } %>
var submitted = false;
var DEBUG = false;

var plusIcon = "/assets/unmanaged/images/plus_icon.gif";
var minusIcon = "/assets/unmanaged/images/minus_icon.gif";

var pwdStat;
$(document).ready(function(event) {
			document.getElementById('confirmNewPassword').disabled=true;
			document.getElementById("confirmNewPassword").style.color = "gray";
			
			/* code added to fix latency issues: starts*/ 
			function debounce(func, wait, immediate) {
					var timeout;
					return function() {
						var context = this, args = arguments;
						var later = function() {
							timeout = null;
							if (!immediate) func.apply(context, args);
						};
						var callNow = immediate && !timeout;
						clearTimeout(timeout);
						timeout = setTimeout(later, wait);
						if (callNow) func.apply(context, args);
					};
				};	
				
				$('#newPassword').on('keydown',
						  debounce(makeAjaxCall, 750)); 
				 /* code added to fix latency issues: ends*/ 
				 
			$("#newPassword").on("keyup",function(e) {
			 var csrfToken ='${_csrf.token}';
			 var keyCodeStatus = e.which;
			 if(keyCodeStatus != 16) {
				 	$('#defaulttxt').show();
				 	$('#fstmsg').show();
					$('#secmsg').show();
					$('#thmsg').show();
					$('#formsg').show();
					$('#fifthmsg').show();
					$('#sixthmsg').show();
					$('#seventhmsg').show();
					$('#eithmsg').show();
					//The below method does Client validation first and then server validation
					doPasswordValidation(document.getElementById('newPassword').value,myProfileUserName);
			}
    });
				
			//confirmPassword validation
			$("#confirmNewPassword").on("keyup",function(e) {
					 var newPasswordStr = document.getElementById('newPassword').value;
					 var confirmPasswordStr = document.getElementById('confirmNewPassword').value;
					 var confirmPwdStr = document.getElementById('pwdvalidation');
					 var pwdValidationRow = document.getElementById('pwdvalidationrow');
					 if(newPasswordStr != confirmPasswordStr){
						 $('#pwdvalidationrow').show();
						 confirmPwdStr.innerHTML = "The Confirm Password field does not match the New Password entered";
					 }
					 else{
						 $('#pwdvalidationrow').hide();
						 confirmPwdStr.innerHTML = "";
					 }
		    });
});
var isContextSpecificWordsIND = true;
var deApiStatus;
function doServerValidation(csrfToken){
		if((/\s/.test(document.getElementById("newPassword").value))){
			alert("Blank Space Are Not Allowed");
		}
		var resultToken ;
		var jsonItem = JSON.stringify({newPassword : document.getElementById("newPassword").value});
		var http = new XMLHttpRequest();
		var url = "/do/profiles/editMyProfile/ajaxvalidator/?"+"_csrf="+csrfToken;
		http.open("POST", url, false);
		http.setRequestHeader("Content-type", "application/json");
		http.onreadystatechange = function() {
	     if(http.readyState == 4 && http.status == 200) {
	       try{
			if (this.responseText.trim().search(/^(\[|\{){1}/) > -1) {
				var parsedData = JSON.parse(this.responseText);
				resultToken = parsedData.score;
				isContextSpecificWordsIND = parsedData.isContextSpecificWordsIND;
				deApiStatus = parsedData.Deapi;
				if(deApiStatus == 'down'){
					$('#deapistatusdiv').show();
					}
				else{
					$('#deapistatusdiv').hide();
				} 
				}
			
			if(resultToken >=0)
			{
				$('span.six').remove();
				$('#sixthmsg').prepend('<span class ="six" style="color: green; font-size: 12px;"> &#10004;</span>');
				$('span.seven').remove();
				$('#seventhmsg').prepend('<span class ="seven" style="color: green; font-size: 12px;"> &#10004;</span>');
				
					if(resultToken < 2)
					{
						pwdStat = false;
							$('span.six').remove();
							$('#sixthmsg').prepend('<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
					}else{
						validationStatus = true;
						pwdStat = true;
						$('span.six').remove();
						$('#sixthmsg').prepend('<span class ="six" style="color: green; font-size: 12px;"> &#10004;</span>');
						document.getElementById('confirmNewPassword').disabled=false;
						document.getElementById("confirmNewPassword").style.color = "";
					}
				
			}else{
				$('span.six').remove();
				$('#sixthmsg').prepend('<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
				
				var confirmPwdStr = document.getElementById('pwdvalidation');
				$('#pwdvalidationrow').hide();
				confirmPwdStr.innerHTML = "";
				document.getElementById('confirmNewPassword').disabled=true;
				
				if(document.getElementById("newPassword").value!="" && isContextSpecificWordsIND != true)
					{
						$('span.seven').remove();
						$('#seventhmsg').prepend('<span class ="seven" style="color: green; font-size: 12px;"> &#10004;</span>');
				}else{
						$('span.seven').remove();
						$('#seventhmsg').prepend('<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
						validationStatus = false;
				} 
			}
				
	       }catch(e){
	    	   console.log("Error: "+e);
	       }
	     }
	 }
	 http.send(jsonItem);  
	} 
		
	function doPasswordValidation(newPassword,userName){
		var csrfToken ='${_csrf.token}';
		
		if(!validationStatus && isContextSpecificWordsIND == true){
		$('span.six').remove();
		$('#sixthmsg').prepend('<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
		$('span.seven').remove();
		$('#seventhmsg').prepend('<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
		}
		 
		validationStatus =  true;
			if (newPassword.length < 8) {
				$('span.one').remove();
				$('#fstmsg').prepend('<span class ="one" style="color: red; font-size: 12px;"> &#10008;</span>');	
				validationStatus = false;
			} else {
				$('span.one').remove();
				$('#fstmsg').prepend('<span class ="one" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			if (newPassword.search(/[a-z]/) < 0) {
				$('span.three').remove();
				$('#thmsg').prepend('<span class ="three" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			} else {
				$('span.three').remove();
				$('#thmsg').prepend('<span class ="three" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			if (newPassword.search(/[A-Z]/) < 0) {
				$('span.two').remove();
				$('#secmsg').prepend('<span class ="two" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			} else {
				$('span.two').remove();
				$('#secmsg').prepend('<span class ="two" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			if (newPassword.search(/[0-9]/) < 0) {
				$('span.four').remove();
				$('#formsg').prepend('<span class ="four" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			} else {
				$('span.four').remove();
				$('#formsg').prepend('<span class ="four" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			if(userNameValidation(userName,newPassword)){
				$('span.eith').remove();
				$('#eithmsg').prepend('<span class ="eith" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			}
			else{
				$('span.eith').remove();
				$('#eithmsg').prepend('<span class ="eith" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
				var pattern = new RegExp(".*[$&+,:;=\\\\_?@#|/'<>.^*()%!-].*"); 
			if (!pattern.test(newPassword)) {
				$('span.five').remove();
				$('#fifthmsg').prepend('<span class ="five" style="color: red; font-size: 12px;"> &#10008;</span>');	
				validationStatus = false;
			}
			else{
				$('span.five').remove();
				$('#fifthmsg').prepend('<span class ="five" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			 if(document.getElementById("newPassword").value ==""){
				 	$('span.six').remove();
					$('#sixthmsg').prepend('<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');	
				 	$('span.seven').remove();
					$('#seventhmsg').prepend('<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
					validationStatus = false;
				 }
		}
		
		function makeAjaxCall(){
			var csrfToken ='${_csrf.token}'; 
				doServerValidation(csrfToken);
		}
		
		function userNameValidation(uname,pwd){
			var userNameInd = false;
		
					if (pwd.indexOf(uname)!== -1 || pwd.indexOf(uname.toUpperCase())!== -1 || pwd.indexOf(uname.toLowerCase())!== -1) {
						userNameInd = true;
			}

			if (pwd.indexOf(uname)!== -1) {
				userNameInd = true;
			}
			if(pwd == ""){
				userNameInd = true;
			}
			return userNameInd;
		}
	
function expandSection(cid) {
	document.getElementById(cid).style.display=(document.getElementById(cid).style.display!="block")? "block" : "none"
	document.getElementById(cid+"img").src=(document.getElementById(cid).style.display=="block")? minusIcon : plusIcon;
}

function expandAll() {
	expandAllSections();
	setAllIcons(minusIcon);
}

function contractAll() {
	contractAllSections();
	setAllIcons(plusIcon);
}

function setAllIcons(iconImg) {
	var numContractAccesses1 ='<% Integer.parseInt(pageContext.getAttribute("checkvalue").toString());%>';
	console.log("kfsfsdfsdfdf"+numContractAccesses1);
	for (var i = 0; i < numContractAccesses1; i++) {
		imgElement = document.getElementById("sc" + i + "img");
		if (imgElement != null) {
			imgElement.src = iconImg;
		}
	}
}

function findElement (form, fieldName) {
  var found = false;
  for (var i = 0; i < form.elements.length; i++)
    if ((found = form.elements[i].name == fieldName))
      break;
  return found ? true :false;
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

function changed(theForm){
	if(theForm.newPassword.value != "")
		return true;
	if(theForm.confirmNewPassword.value != "")
		return true;
	if(theForm.firstName.value != theForm.oldFirstName.value)
		return true;
	if(theForm.lastName.value != theForm.oldLastName.value)
		return true;
	if(theForm.email.value != theForm.oldEmail.value)
		return true;
//tracking the changes for newly added phone, fax and extension and special attribue fields
	if(document.getElementById("phAreaCode") != null && document.getElementById("phAreaCode") != undefined
		&& document.getElementById("phPrefix") != null && document.getElementById("phPrefix") != undefined
		&& document.getElementById("phSuffix") != null && document.getElementById("phSuffix") != undefined) {
		var newPhoneValue = document.getElementById("phAreaCode").value 
				+ document.getElementById("phPrefix").value + document.getElementById("phSuffix").value;
		if(theForm.oldTelephoneNumber.value != newPhoneValue) {
			return true;
		}	
	}
	if(document.getElementById("phExtn") != null && document.getElementById("phExtn") != undefined) {
		if(theForm.oldTelephoneExtension.value != theForm.telephoneExtension.value) {
			return true;
		}
	}
	if(document.getElementById("faxAreaCode") != null && document.getElementById("faxAreaCode") != undefined
		&& document.getElementById("faxPrefix") != null && document.getElementById("faxPrefix") != undefined 
		&& document.getElementById("faxSuffix") != null && document.getElementById("faxSuffix") != undefined) {
		var newFaxValue = document.getElementById("faxAreaCode").value 
				+ document.getElementById("faxPrefix").value + document.getElementById("faxSuffix").value;
		if(theForm.oldFaxNumber.value != newFaxValue) {
			return true;
		}	
	}
	if(findElement(theForm, "challengeQuestion")) {
		if(theForm.challengeQuestion.value != theForm.oldChallengeQuestion.value)
			return true;
		if(theForm.challengeAnswer.value != theForm.oldChallengeAnswer.value)
			return true;
		if(theForm.challengeAnswer.value != theForm.verifyChallengeAnswer.value)
			return true;
	}


	var numContractAccesses ='<% Integer.parseInt(pageContext.getAttribute("checkvalue").toString());%>';
	for (var i = 0; i < numContractAccesses; i++) {
		var emailNewsletterValue = getRadioValue("contractAccesses[" + i + "].emailNewsletter");
		if (emailNewsletterValue != null) {
			var oldEmailNewsletterElement = document.getElementsByName("contractAccesses[" + i + "].oldEmailNewsletter")[0];
			if (emailNewsletterValue != oldEmailNewsletterElement.value) {
				return true;
			}
		}
		var receiveILoanEmailValue = getRadioValue("contractAccesses[" + i + "].receiveILoanEmail");
		if (receiveILoanEmailValue != null) {
			var oldReceiveILoanEmailElement = document.getElementsByName("contractAccesses[" + i + "].oldReceiveILoanEmail")[0];
			if (receiveILoanEmailValue != oldReceiveILoanEmailElement.value) {
				return true;
			}
		}
		var primaryContactValue = getRadioValue("contractAccesses[" + i + "].primaryContact");
		if (primaryContactValue != null) {
			var oldPrimaryContactElement = 
					document.getElementsByName("contractAccesses[" + i + "].oldPrimaryContact")[0];
			if (primaryContactValue != oldPrimaryContactElement.value) {
				return true;
			}
		}
		var mailRecepientValue = getRadioValue("contractAccesses[" + i + "].mailRecepient");
		if (mailRecepientValue != null) {
			var oldMailRecepientElement = 
					document.getElementsByName("contractAccesses[" + i + "].oldMailRecepient")[0];
			if (mailRecepientValue != oldMailRecepientElement.value) {
				return true;
			}
		}
		var trusteeMailRecepientValue = getRadioValue("contractAccesses[" + i + "].trusteeMailRecepient");
		if (trusteeMailRecepientValue != null) {
			var oldTrusteeMailRecepientElement = 
					document.getElementsByName("contractAccesses[" + i + "].oldTrusteeMailRecepient")[0];
			if (trusteeMailRecepientValue != oldTrusteeMailRecepientElement.value) {
				return true;
			}
		}
	}

	return false;
}


function doSubmit(theForm)
{
	if (!submitted) {
		// displaying the warnig messages for special attributes 
			var numContractAccesses2 = '<% Integer.parseInt(pageContext.getAttribute("checkvalue").toString());%>';
		var warningString = "";
		for (var i = 0; i < numContractAccesses2; i++) {
			var contractElement = document.getElementsByName("contractAccesses[" + i + "].contractNumber");
			if(contractElement != null) {
				var primaryContactElement = 
						document.getElementsByName("contractAccesses[" + i + "].primaryContact");
				var primaryContactFirst = 
						document.getElementsByName("contractAccesses[" + i + "].primaryContactFirstName");
				var primaryContactLast = 
						document.getElementsByName("contractAccesses[" + i + "].primaryContactLastName");
				if (primaryContactElement != null && primaryContactElement[0] != null 
					  && primaryContactElement[0].checked) {
					if((primaryContactLast[0] != null && primaryContactLast[0].value != "") 
						|| (primaryContactFirst[0] != null && primaryContactFirst[0].value != "")) {
							warningString += "Warning! " 
								+ primaryContactFirst[0].value + " " 
								+ primaryContactLast[0].value 
								+ " is currently designated as the Primary Contact for contract " 
								+ contractElement[0].value + 
								".\nSelect OK to replace the primary contact or Cancel to change the current profile information.\n";
					}
				}
				
				var mailRecepientElement = 
						document.getElementsByName("contractAccesses[" + i + "].mailRecepient");
				var mailRecepientFirst = 
						document.getElementsByName("contractAccesses[" + i + "].clientMailFirstName");
				var mailRecepientLast = 
						document.getElementsByName("contractAccesses[" + i + "].clientMailLastName");
				if (mailRecepientElement != null && mailRecepientElement[0] != null 
						&& mailRecepientElement[0].checked) {
					if((mailRecepientLast[0] != null && mailRecepientLast[0].value != "") 
							|| (mailRecepientFirst[0]!= null && mailRecepientFirst[0].value != "")) {
						warningString += "Warning! " 
							+ mailRecepientFirst[0].value + " " 
							+ mailRecepientLast[0].value 
							+ " is currently designated as the Mail Recipient for contract " 
							+ contractElement[0].value + 
							".\nSelect OK to replace the mail recipient or Cancel to change the current profile information.\n";
					}
				}
				
				var trusteeMailRecepientElement = 
						document.getElementsByName("contractAccesses[" + i + "].trusteeMailRecepient");
				var trusteeMailRecepientFirst = 
						document.getElementsByName("contractAccesses[" + i + "].trusteeMailFirstName");
				var trusteeMailRecepientLast = 
						document.getElementsByName("contractAccesses[" + i + "].trusteeMailLastName");
				if (trusteeMailRecepientElement != null && trusteeMailRecepientElement[0] != null 
						&& trusteeMailRecepientElement[0].checked) {
					if((trusteeMailRecepientLast[0] != null && trusteeMailRecepientLast[0].value != "") 
							|| (trusteeMailRecepientFirst[0] != null && trusteeMailRecepientFirst[0].value != "")) {
						warningString += "Warning! " 
							+ trusteeMailRecepientFirst[0].value + " " 
							+ trusteeMailRecepientLast[0].value 
							+ " is currently designated as the Trustee Mail Recipient for contract " 
							+ contractElement[0].value + 
							".\nSelect OK to replace the trustee mail recipient or Cancel to change the current profile information.\n";
					}
				}
			}
		}

		if(findElement(theForm, "challengeQuestion")) {
			if(theForm.challengeQuestion.value != theForm.oldChallengeQuestion.value) {
				if(theForm.challengeAnswer.value == "") {
					warningString += window.confirm("Warning! You have changed your challenge question but not your challenge answer. Select OK to continue or Cancel to return.");
				} 
			} else if(theForm.challengeAnswer.value != "") {
				if(theForm.challengeQuestion.value == theForm.oldChallengeQuestion.value) {
					warningString += window.confirm("Warning! You have changed your challenge answer but not your challenge question. Select OK to continue or Cancel to return.");
				}
			}
		}
		if(warningString != "" && warningString == "false") {
		   submitted = false;
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
	var form = document.forms["editMyProfileForm"];
	return changed(form);
}

function clicked_checkbox(hidden, option) {
	if (option.checked) {
   		hidden.value = "<%=Constants.YES%>";
 	} else {
      	hidden.value = "<%=Constants.NO%>";
		}
	}

	registerTrackChangesFunction(isFormChanged);
</script>

<%-- This jsp includes the following CMA content --%>
<content:contentBean
	contentId="<%=ContentConstants.LAYOUT_EDIT_MY_PROFILE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="layoutPageBean" />
<content:contentBean contentId="<%=ContentConstants.CHANNEL_CHOICE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="channelChoice" />
<content:contentBean contentId="<%=ContentConstants.MOBILE_INFO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="mobileToolTip" />

<c:set var="userProfile" value="${userProfile}" scope="session"/>
<c:set var="role" value="${userProfile.role}" scope="session"/>
<ps:form cssClass="margin-bottom:0;" method="POST"
	action="/do/profiles/editMyProfile/" name="editMyProfileForm" modelAttribute="editMyProfileForm">


	<table width="470" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="2"><strong><font color="#CC6600">*</font></strong>
				Required Information <content:errors scope="request" />
				<content:errors scope="session" />
				<div id="deapistatusdiv" class="redText" style="display: none;">
		         <content:getAttribute attribute="text"	beanName="DeapiErrorMessage">
				 </content:getAttribute>
		         </div>
				</td>
		</tr>

		<tr>
			<td width="525"><img src="/assets/unmanaged/images/s.gif"
				width="525" height="1"></td>
			<td width="20"><img src="/assets/unmanaged/images/s.gif"
				width="20" height="1"></td>
			<td width="180"><img src="/assets/unmanaged/images/s.gif"
				width="180" height="1"></td>
		</tr>

		<tr>

			<%-- 1st cell is for profile details --%>
			<td><br>
				<table width="525" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="1"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
						<td width="200"><img src="/assets/unmanaged/images/s.gif"
							width="153" height="1"></td>
						<td width="489"><img src="/assets/unmanaged/images/s.gif"
							width="153" height="1"></td>
						<td width="4"><img src="/assets/unmanaged/images/s.gif"
							width="4" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
					</tr>
					<tr class="tablehead">
						<td class="tableheadTD1" colspan="5"><strong><content:getAttribute
									id="layoutPageBean" attribute="body1Header" /></strong></td>
					</tr>
					<tr class="datacell1">
						<td rowspan="1" class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr class="datacell1">
									<td class="datacell1" width="135"><ps:label fieldId="firstName"
											mandatory="true">First name</ps:label></td>
<td colspan="2" align="left" class="datacell1"><form:input path="firstName" maxlength="30" size="50"/> <script

											type="text/javascript">
												document.forms["editMyProfileForm"].firstName
														.focus();
</script><form:hidden path="oldFirstName"/></td>
								</tr>
								<tr class="datacell1">
									<td class="datacell1"><ps:label fieldId="lastName"
											mandatory="true">Last name</ps:label></td>
<td colspan="2" class="datacell1"><form:input path="lastName" maxlength="30" size="50"/><form:hidden path="oldLastName"/></td>


								</tr>
								<tr class="datacell1">
									<td class="datacell1"><ps:label fieldId="email"
											mandatory="true">Primary Email</ps:label></td>
<td colspan="2" class="datacell1"><form:input path="email" maxlength="70" size="50"/><form:hidden path="oldEmail"/></td>


								</tr>
								<tr class="datacell1">
									<td class="datacell1"><ps:label fieldId="secondaryEmail"
											mandatory="false">Secondary Email</ps:label></td>
<td colspan="2" class="datacell1"><form:input path="secondaryEmail" maxlength="70" size="50"/><form:hidden path="oldSecondaryEmail"/></td>


								</tr>
								<!-- Adding the phone, extension and fax field -->
								<ps:isExternal name="userProfile" property="role">
									<tr class="datacell1">
										<td class="datacell1"><ps:label fieldId="telephoneNumber"
												mandatory="false">Telephone number</ps:label></td>
<td colspan="2" class="datacell1"><form:input path="telephoneNumber.areaCode" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField" id="phAreaCode"/>
 -<form:input path="telephoneNumber.phonePrefix" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField" id="phPrefix"/> -
 <form:input path="telephoneNumber.phoneSuffix" maxlength="4" onkeyup="return autoTab(this, 4, event);" size="4" cssClass="inputField" id="phSuffix"/> 
 <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">ext.
  <form:input path="telephoneExtension" maxlength="8" onkeyup="return autoTab(this, 8, event);" size="8" cssClass="inputField" id="phExtn"/> 
  <form:hidden path="oldTelephoneNumber"/>
  <form:hidden path="oldTelephoneExtension"/></td>


									</tr>

									<tr class="datacell1">
										<td class="datacell1"><ps:label fieldId="faxNumber"
												mandatory="false">Fax number</ps:label></td>
<td colspan="2" class="datacell1"><form:input path="faxNumber.areaCode" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField" id="faxAreaCode"/> 
-<form:input path="faxNumber.faxPrefix" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField" id="faxPrefix"/>
 -<form:input path="faxNumber.faxSuffix" maxlength="4" onkeyup="return autoTab(this, 4, event);" size="4" cssClass="inputField" id="faxSuffix"/> 
  <form:hidden path="oldFaxNumber"/></td>

									</tr>
								</ps:isExternal>

								
							</table>
						</TD>
						<TD class=databorder><IMG height=1
							src="/assets/unmanaged/images/s.gif" width=1></TD>
					</TR>
					<TR class=whiteborder>
						<TD colspan="5" class=databorder><IMG height=1
							src="/assets/unmanaged/images/s.gif" width=1></TD>
					</TR>
					</TBODY>
</TABLE> <br /> 

			<TD><IMG height=1 src="/assets/unmanaged/images/s.gif" width=20></TD>
			<TD vAlign=top>&nbsp;</TD>
		</TR>
		</TBODY>
	</TABLE>
		
	<%-- table 2 --%>
	
	<table width="700" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="525"><img src="/assets/unmanaged/images/s.gif"
				width="525" height="1"></td>
			<td width="20"><img src="/assets/unmanaged/images/s.gif"
				width="20" height="1"></td>
			<td width="180"><img src="/assets/unmanaged/images/s.gif"
				width="180" height="1"></td>
		</tr>

		<tr>

			<%-- 1st cell is for profile details --%>
			<td>
				<table width="525" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="1"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
						<td width="200"><img src="/assets/unmanaged/images/s.gif"
							width="153" height="1"></td>
						<td width="489"><img src="/assets/unmanaged/images/s.gif"
							width="153" height="1"></td>
						<td width="4"><img src="/assets/unmanaged/images/s.gif"
							width="4" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
					</tr>
					<tr>
					<td colspan="8" class="tableheadTD1">
						<content:contentBean contentId="<%=ContentConstants.NEW_SECTION_TITLE_EDIT_PAGE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
									beanName="NEW_SECTION_TITLE_EDIT_PAGE" />
							 <strong> <content:getAttribute beanName="NEW_SECTION_TITLE_EDIT_PAGE" attribute="title"/> </strong> 
							
						</td>
						</tr>
					<tr class="datacell1">
						<td rowspan="1" class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								
								<tr class="datacell1">
										<td  class="datacell1" width="135"><ps:label fieldId="mobileNumber"
												mandatory="false">Mobile number</ps:label></td>
												
<td colspan="2" class="datacell1"><form:input path="mobileNumber.areaCode" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField" id="phoneAreaCode"/> 
-<form:input path="mobileNumber.phonePrefix" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField" id="phonePrefix"/>
 -<form:input path="mobileNumber.phoneSuffix" maxlength="4" onkeyup="return autoTab(this, 4, event);" size="4" cssClass="inputField" id="phoneSuffix"/> 
 <c:set var="saveMouseOverText">
            Tip('<cont:getAttribute beanName="mobileToolTip" attribute="text" escapeJavaScript="true"/>')
         </c:set>
<img src="/assets/generalimages/info.gif" width="12" height="12"
										onmouseover="${saveMouseOverText}"
										onmouseout="UnTip()" /></td>
								</tr>
								<tr valign="top" class="datacell1">
								<tr valign="top" class="datacell1">
									<td colspan="2"><ps:label
											fieldId="passcodeDeliveryPreference" mandatory="true">
											<content:getAttribute beanName="channelChoice"
												attribute="text" />
										</ps:label></td>
								</tr>
								<tr>
									<td align="right">&nbsp;</td>
									<td><form:radiobutton disabled="false"
											path="passcodeDeliveryPreference" value="SMS" />Text message to mobile number</td>
								</tr>
								<tr>
									<td align="right">&nbsp;</td>
									<td><form:radiobutton disabled="false"
											path="passcodeDeliveryPreference" value="VOICE_TO_MOBILE" />Voice message to mobile number</td>
										
								</tr>
								<tr>
									<td align="right">&nbsp;</td>
									<td><form:radiobutton disabled="false"
											path="passcodeDeliveryPreference" value="VOICE_TO_PHONE" />Voice message to telephone number</td>
								</tr>
								<tr>
									<td align="right">&nbsp;</td>
									<td><form:radiobutton disabled="false"
											path="passcodeDeliveryPreference" value="EMAIL" />Email</td>
								</tr>
								
								<tr class="datacell1">
									<td class="datacell1"><strong>Username</strong></td>
<td colspan="2" class="datacell1">${userProfile.name}</td>

								</tr>
								<tr class="datacell1">
									<td class="datacell1"><ps:label fieldId="newPassword"
											mandatory="false">New password</ps:label></td>
									<td colspan="2" class="datacell1"><strong><form:password id="newPassword"  path="newPassword" size="50" maxlength="64" /> </strong>
									
									</td>
								</tr>
								
								<tr class="datacell1">
									<td class="datacell1"><ps:label
											fieldId="confirmNewPassword" mandatory="false">Confirm new password</ps:label></td>
									<td colspan="2" class="datacell1"><strong><form:password path="confirmNewPassword" size="50" maxlength="64" style="color: black;" /> </strong></td>
								</tr>
								<tr class="datacell1" style="display: none;" id="pwdvalidationrow">
								<td>&nbsp;</td>
								<td colspan="2">
								<p id="pwdvalidation" style="font-size: 11;" class="redText"></p>
								</td>
								</tr>
								<tr class="datacell1">
								<td width="39%"></td>
								<td width="61%">
							<div id="defaulttxt" style="display: none; margin-inline-start: 3px;"><span class ="defaulttxt"></span>Passwords MUST meet all of the following criteria before you can continue:</div>
							<div id="sixthmsg" style="display: none; margin-inline-start: 1px;"><span class ="six"></span>&nbsp;Not be something that someone can easily guess </br><div id="sixrowinlinediv"><span class ="sixspaninline"></span>&nbsp;&nbsp;&nbsp;&nbsp;eg "password"</div></div>
							<div id="fstmsg" style="display: none;"><span class ="one"></span>&nbsp;Be between 8-64 characters</div>
							<div id="secmsg" style="display: none;"><span class ="two"></span>&nbsp;Have at least 1 uppercase letter</div>
							<div id="thmsg" style="display: none;"><span class ="three"></span>&nbsp;Have at least 1 lowercase letter</div>
							<div id="formsg" style="display: none;"><span class ="four"></span>&nbsp;Have at least 1 number</div>
							<div id="fifthmsg" style="display: none;"><span class ="five"></span>&nbsp;Have at least 1 special character</div>
							<div id="seventhmsg" style="display: none;"><span class ="seven"></span>&nbsp;Not contain industry related words</div>
							<div id="eithmsg" style="display: none;"><span class ="eith"></span>&nbsp;Not contain username</div>
							
								</td>
								</tr>
								<ps:isNotInternalOrTpa name="userProfile" property="role">
									<tr class="datacell1">
										<td class="datacell1"><ps:label
												fieldId="challengeQuestion" mandatory="true">Challenge question</ps:label></td>
<td colspan="2" class="datacell1"><form:input path="challengeQuestion" maxlength="100" size="50"/><form:hidden path="oldChallengeQuestion"/></td>


									</tr>
									<tr class="datacell1">
										<td class="datacell1"><ps:label fieldId="challengeAnswer"
												mandatory="false">Answer</ps:label></td>
										<td colspan="2" class="datacell1"><form:password path="challengeAnswer"  size="50" maxlength="32" /><form:hidden path="oldChallengeAnswer"/> </td>

									</tr>
									<tr class="datacell1">

										<td class="datacell1"><ps:label
												fieldId="verifyChallengeAnswer" mandatory="false">Confirm answer</ps:label></td>

										<td colspan="2" class="datacell1"><form:password path="verifyChallengeAnswer" size="50" maxlength="32" /></td>
									</tr>
								</ps:isNotInternalOrTpa>
								<tr class="datacell1">
									<td class="datacell1"><ps:label fieldId="currentPassword"
											mandatory="true">Current password</ps:label></td>
									<td colspan="2" class="datacell1"><strong><form:password path="currentPassword" size="50" maxlength="64" /></strong></td>
								</tr>
								<tr class="datacell1">
									<td class="datacell1"><strong>Profile last
											updated </strong></td>

									<td><render:date property="theForm.profileLastUpdatedTS"
											patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>"
defaultValue="" /> <c:if test="${not empty theForm.profileLastUpdatedBy}">by: ${theForm.getProfileLastUpdatedBy()}

</c:if> <c:if test="${theForm.profileLastUpdatedByInternal ==true}"> at <ps:companyName />

</c:if></td>


 <form:hidden path="challengeQuestionRequired"/>
 
 
 
								</tr>
								<tr><td>&nbsp;</td></tr>
  		
						<tr valign="top" class="datacell1">
							<td colspan="2">
								<content:contentBean contentId="<%=ContentConstants.MOBILE_CONSENT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
									beanName="MOBILE_CONSENT" />
										<content:getAttribute beanName="MOBILE_CONSENT" attribute="text"/>
							</td>
					</tr>		
								
									
									</table>
						</TD>
						<TD class=databorder><IMG height=1
							src="/assets/unmanaged/images/s.gif" width=1></TD>
					</TR>
					<TR class=whiteborder>
						<TD colspan="5" class=databorder><IMG height=1
							src="/assets/unmanaged/images/s.gif" width=1></TD>
					</TR>
					</TBODY>
</TABLE> <br /> <c:if test="${editMyProfileForm.isEmailPreferenceShown() ==true}">
					  <jsp:include page="myProfileEmailPreference.jsp"></jsp:include> 
</c:if></TD>

			<TD><IMG height=1 src="/assets/unmanaged/images/s.gif" width=20></TD>
			<TD vAlign=top>&nbsp;</TD>
		</TR>
		</TBODY>
	</TABLE>
	
	
	<table width="525" border="0" cellspacing="0" cellpadding="0">
		<tr>
<td align="right"><input type="submit" class="button100Lg" name="action" onclick="return doCancelChanges(editMyProfileForm);" value="cancel"  /><%--  - property="actionLabel" --%>


&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="submit" class="button100Lg" name="action" onclick="return doSubmit(editMyProfileForm);" value="save" id="submit" /><%--  - property="actionLabel" --%> <script


					type="text/javascript">
						var onenter = new OnEnterSubmit('action', 'save');
						onenter.install();
					</script></td>
		</tr>
	</table>

</ps:form>
<script>
	<% if(null!=deapiStatus && deapiStatus.equals("down")){  
		%>
		document.getElementById("deapistatusdiv").style.display = "block";
		<%} else {%> 
		document.getElementById("deapistatusdiv").style.display = "none";;
	<% } %>
</script>