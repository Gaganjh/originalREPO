<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.security.role.ExternalUser" %>
<%@ page import="com.manulife.pension.service.security.role.InternalUser" %>
<%@ page import="com.manulife.pension.delegate.EnvironmentServiceDelegate" %>
<%@ page import="com.manulife.pension.service.security.exception.SecurityServiceException"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="cont" uri="manulife/tags/content" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="psw"%>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<% 
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

String deapiStatus = null;
if (null != request.getSession(false) && null != request.getSession(false).getAttribute("Deapi")) {
	deapiStatus = (String) request.getSession(false).getAttribute("Deapi");
}

%>
<psw:contentBean
	contentId="<%=ContentConstants.DE_API_ERROR_MESSAGE%>"
	type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
	beanName="DeapiErrorMessage" />
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

 <script language="JavaScript1.2" type="text/javascript">

var validationStatus = false;
var submitted=false;

function setButtonAndSubmit(button) {
	
	if (!submitted) {
		submitted=true;
		document.registerForm.action.value = button;
		document.registerForm.submit();
	} else {
		window.status = "Transaction already in progress.  Please wait.";
	}
}



var pwdStat;


		$(document)
				.ready(
						function(event) {
							/*code added to auto clear password field if there is a error  :starts*/
							var element = document.getElementById('psErrors');
							if (typeof (element) != 'undefined'
									&& element != null) {
								document.getElementById('password').value = "";
								document.getElementById('confirmPassword').value = "";
								document.getElementById("continue").disabled = false;
								document.getElementById("continue").style.color = "";
							} else {

								document.getElementById("continue").disabled = true;
								document.getElementById("continue").style.color = "gray";

							}
							/*code added to auto clear password field  :ends*/
							
							/* code added to fix latency issues: starts*/
							function debounce(func, wait, immediate) {
								var timeout;
								return function() {
									var context = this, args = arguments;
									var later = function() {
										timeout = null;
										if (!immediate)
											func.apply(context, args);
									};
									var callNow = immediate && !timeout;
									clearTimeout(timeout);
									timeout = setTimeout(later, wait);
									if (callNow)
										func.apply(context, args);
								};
							}
							;

							$('#password').on('keydown',
									debounce(makeAjaxCall, 750));
							/* code added to fix latency issues: ends*/

							$("#password")
									.on("keyup",
											function(e) {
												var keyCodeStatus = e.which;
												if (keyCodeStatus != 16) {
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
													doPasswordValidation(
															document
																	.getElementById('password').value,
															document
																	.getElementById("userName").value);
												}
											});

							//confirmPassword validation
							$("#confirmPassword")
									.on("keyup",
											function(e) {
												var newPasswordStr = document
														.getElementById('password').value;
												var confirmPasswordStr = document
														.getElementById('confirmPassword').value;
												var confirmPwdStr = document
														.getElementById('pwdvalidation');
												var pwdValidationRow = document
														.getElementById('pwdvalidationrow');
												if (newPasswordStr != confirmPasswordStr) {
													$('#pwdvalidationrow')
															.show();
													confirmPwdStr.innerHTML = "The Confirm Password field does not match the New Password entered";
													document
															.getElementById("continue").disabled = true;
													document
															.getElementById("continue").style.color = "gray";
												} else {
													$('#pwdvalidationrow')
															.hide();
													confirmPwdStr.innerHTML = "";
													if (pwdStat) {
														document
																.getElementById("continue").style.color = "#FFFFFF";
														document
																.getElementById("continue").disabled = false;
													}
												}
											});
						});
		var isContextSpecificWordsIND = true;
		var deApiStatus;
		function doServerValidation(csrfToken) {
			if ((/\s/.test(document.getElementById("password").value))) {
				alert("Blank Space Are Not Allowed");
			}
			var resultToken;
			var jsonItem = JSON.stringify({
				newPassword : document.getElementById("password").value,
				userName : document.getElementById("userName").value
			});
			var http = new XMLHttpRequest();
			var url = "/do/registration/register/ajaxvalidator/?" + "_csrf="
					+ csrfToken;
			http.open("POST", url, false);
			http.setRequestHeader("Content-type", "application/json");
			http.onreadystatechange = function() {
				if (http.readyState == 4 && http.status == 200) {
					try {
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

						if (resultToken >= 0) {
							$('span.six').remove();
							$('#sixthmsg')
									.prepend(
											'<span class ="six" style="color: green; font-size: 12px;"> &#10004;</span>');
							$('span.seven').remove();
							$('#seventhmsg')
									.prepend(
											'<span class ="seven" style="color: green; font-size: 12px;"> &#10004;</span>');

							if (resultToken < 2) {
								pwdStat = false;
								$('span.six').remove();
								$('#sixthmsg')
										.prepend(
												'<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
							} else {
								validationStatus = true;
								pwdStat = true;
								$('span.six').remove();
								$('#sixthmsg')
										.prepend(
												'<span class ="six" style="color: green; font-size: 12px;"> &#10004;</span>');
							}

						} else {
							$('span.six').remove();
							$('#sixthmsg')
									.prepend(
											'<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');

							var confirmPwdStr = document
									.getElementById('pwdvalidation');
							$('#pwdvalidationrow').hide();
							confirmPwdStr.innerHTML = "";
							document.getElementById("continue").disabled = true;
							document.getElementById("continue").style.color = "gray";

							if (document.getElementById("password").value != ""
									&& isContextSpecificWordsIND != true) {
								$('span.seven').remove();
								$('#seventhmsg')
										.prepend(
												'<span class ="seven" style="color: green; font-size: 12px;"> &#10004;</span>');
							} else {
								$('span.seven').remove();
								$('#seventhmsg')
										.prepend(
												'<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
								validationStatus = false;
							}
						}

					} catch (e) {
						console.log("Error: " + e);
					}
				}
			}
			http.send(jsonItem);
		}

		function doPasswordValidation(newPassword, userName) {
			var csrfToken = '${_csrf.token}';

			if (!validationStatus && isContextSpecificWordsIND == true) {
				$('span.six').remove();
				$('#sixthmsg')
						.prepend(
								'<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
				$('span.seven').remove();
				$('#seventhmsg')
						.prepend(
								'<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
			}

			validationStatus = true;
			if (newPassword.length < 8) {
				$('span.one').remove();
				$('#fstmsg')
						.prepend(
								'<span class ="one" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			} else {
				$('span.one').remove();
				$('#fstmsg')
						.prepend(
								'<span class ="one" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			if (newPassword.search(/[a-z]/) < 0) {
				$('span.three').remove();
				$('#thmsg')
						.prepend(
								'<span class ="three" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			} else {
				$('span.three').remove();
				$('#thmsg')
						.prepend(
								'<span class ="three" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			if (newPassword.search(/[A-Z]/) < 0) {
				$('span.two').remove();
				$('#secmsg')
						.prepend(
								'<span class ="two" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			} else {
				$('span.two').remove();
				$('#secmsg')
						.prepend(
								'<span class ="two" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			if (newPassword.search(/[0-9]/) < 0) {
				$('span.four').remove();
				$('#formsg')
						.prepend(
								'<span class ="four" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			} else {
				$('span.four').remove();
				$('#formsg')
						.prepend(
								'<span class ="four" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			if (userNameValidation(userName, newPassword)) {
				$('span.eith').remove();
				$('#eithmsg')
						.prepend(
								'<span class ="eith" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			} else {
				$('span.eith').remove();
				$('#eithmsg')
						.prepend(
								'<span class ="eith" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			var pattern = new RegExp(".*[$&+,:;=\\\\_?@#|/'<>.^*()%!-].*");
			if (!pattern.test(newPassword)) {
				$('span.five').remove();
				$('#fifthmsg')
						.prepend(
								'<span class ="five" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			} else {
				$('span.five').remove();
				$('#fifthmsg')
						.prepend(
								'<span class ="five" style="color: green; font-size: 12px;"> &#10004;</span>');
			}
			if (document.getElementById("password").value == "") {
				$('span.six').remove();
				$('#sixthmsg')
						.prepend(
								'<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
				$('span.seven').remove();
				$('#seventhmsg')
						.prepend(
								'<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
				validationStatus = false;
			}
		}

		function makeAjaxCall() {
			var csrfToken = '${_csrf.token}';
			doServerValidation(csrfToken);
		}

		function userNameValidation(uname, pwd) {
			var userNameInd = false;

			if (pwd.indexOf(uname)!== -1 || pwd.indexOf(uname.toUpperCase())!== -1
					|| pwd.indexOf(uname.toLowerCase())!== -1) {
				userNameInd = true;
			}

			if (pwd.indexOf(uname)!== -1) {
				userNameInd = true;
			}
			if (pwd == "") {
				userNameInd = true;
			}
			return userNameInd;
		}
	</script>


<table width="765" border="0" cellpadding="0" cellspacing="0">
	
	<tbody>
		<tr>
			<td width="15%"><img height="8" src="/assets/unmanaged/images/s.gif" width="10" border="0"></td>
			<td width="50%" valign="top" class="greyText"> <img src="/assets/unmanaged/images/s.gif" width="402" height="23"><br>
				<img src="<content:pageImage type="pageTitle" beanName="layoutPageBean"/>" alt="<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>" width="215" height="34"><br>		
				<br>
				<table width="425" border="0" cellspacing="0" cellpadding="0">
					<tbody>
						<tr>
							<td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
							<td valign="top" width="495">
									<b><content:getAttribute beanName="layoutPageBean" attribute="subHeader"/></b>
									<br>
									<content:getAttribute attribute="introduction1" beanName="layoutPageBean"/>
									<br><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="left">
								<content:errors scope="session" />
								<div id="deapistatusdiv" class="redText" style="display: none;">
						         <content:getAttribute attribute="text"	beanName="DeapiErrorMessage">
								 </content:getAttribute>
						         </div>
							</td>
						</tr>
					</tbody>
				</table>
				<img src="/assets/unmanaged/images/s.gif" width="1" height="20"> <br>
				<table width="435" border="0" cellpadding="0" cellspacing="0">
				<ps:form method="POST" modelAttribute="registerForm" name="registerForm" action="/do/registration/tparegister/" >
				<form:hidden path="action"/>
				<tbody>
					<tr>
						<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="113"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="463"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="113"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					<tr class="tablehead">
						<td colspan="8" class="tableheadTD1">
							<strong> <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> </strong>
						</td>
					</tr>
					<tr class="datacell1">
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan="6" align="center"><table border="0" cellpadding="3" cellspacing="0" class="datacell1">
						<table>
							<tbody>
							<tr valign="top" class="datacell1">
								<td width="168"><strong> First name</strong></td>
								<td width="250">${registerForm.firstName}</td>
							</tr>
							<tr valign="top" class="datacell1">
								<td width="168"><strong> Last name</strong></td>
								<td>${registerForm.lastName}</td>
							</tr>
							<tr valign="top" class="datacell1">
								<td><ps:label fieldId="email" mandatory="true">Email</ps:label></td>
								<td>
									<form:input path="email" size="40" cssClass="inputField"/>
								</td>
							</tr>
				<!--		   <tr valign="top" class="datacell1"> -->
							<tr valign="top" class="datacell1">
								<td><ps:label fieldId="phone">Telephone number</ps:label></td>
								<td>
									<form:input path="phone.areaCode" cssClass="inputField" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/> - 
									<form:input path="phone.phonePrefix" cssClass="inputField" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/> -
									<form:input path="phone.phoneSuffix" cssClass="inputField" onkeyup="return autoTab(this, 4, event);" size="4" maxlength="4"/> ext <form:input path="ext" cssClass="inputField" onkeyup="return autoTab(this, 8, event);" size="8" maxlength="8"/>			                    
								</td>
							</tr>
							<tr valign="top" class="datacell1">
								<td><ps:label fieldId="fax">Fax number</ps:label></td>
								<td>
									<form:input path="fax.areaCode" cssClass="inputField" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/> - 
									<form:input path="fax.faxPrefix" cssClass="inputField" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/> -
									<form:input path="fax.faxSuffix" cssClass="inputField" onkeyup="return autoTab(this, 4, event);" size="4" maxlength="4"/>
								</td>
							</tr>						
												
						</tbody>
						</table>
						</td>
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>

					<tr class="datacell1">
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
						<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan="2" rowspan="2" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
					</tr>
					<tr>
						<td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
						</tbody></table>
						<br>
						<%-- here it goes the second table --%>
						
						
				<table width="435" border="0" cellpadding="0" cellspacing="0">
				
				<tbody>
					<tr>
						<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="113"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="463"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="113"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					<tr class="tablehead">
						<td colspan="8" class="tableheadTD1">
						<content:contentBean contentId="<%=ContentConstants.NEW_SECTION_TITLE_REGISTRATION%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
									beanName="NEW_SECTION_TITLE_REGISTRATION" />
							 <strong> <content:getAttribute beanName="NEW_SECTION_TITLE_REGISTRATION" attribute="title"/> </strong> 
							
						</td>
					</tr>
					<tr class="datacell1">
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan="6" align="center"><table border="0" cellpadding="3" cellspacing="0" class="datacell1">
						<table>
							<tbody>
							<tr valign="top" class="datacell1">
								<td><ps:label fieldId="mobile">Mobile number</ps:label></td>
								<td>
							<content:contentBean contentId="<%=ContentConstants.MOBILE_INFO%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
									beanName="mobileToolTip" />
								<form:input path="mobile.areaCode" cssClass="inputField" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/> - 
			                    <form:input path="mobile.phonePrefix" cssClass="inputField" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/> -
			                    <form:input path="mobile.phoneSuffix" cssClass="inputField" onkeyup="return autoTab(this, 4, event);" size="4" maxlength="4"/>
								<c:set var="saveMouseOverText">
            						Tip('<cont:getAttribute beanName="mobileToolTip" attribute="text" escapeJavaScript="true"/>')
         						</c:set>
								<img src="/assets/generalimages/info.gif" width="12" height="12" onmouseover="${saveMouseOverText}"
										onmouseout="UnTip()" /> 
							</td>							
							</tr>
							<tr valign="top" class="datacell1">
							<td colspan="2">
								<ps:label fieldId="passcodeDeliveryPreference" mandatory="true"><content:contentBean
																		contentId="<%=ContentConstants.CHANNEL_CHOICE%>"
																		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
																		beanName="channelSelection" />
																	<content:getAttribute beanName="channelSelection" attribute="text" />
																							
																				</ps:label>
							</td>
						</tr>
						<tr><td align="right"></td><td><form:radiobutton disabled="false" path="passcodeDeliveryPreference"  value="SMS" checked="${empty registerForm.passcodeDeliveryPreference or registerForm.passcodeDeliveryPreference == 'SMS' ? 'checked' : '' }"/>Text message to mobile number</td></tr>
						<tr><td align="right"></td><td><form:radiobutton disabled="false" path="passcodeDeliveryPreference"  value="VOICE_TO_MOBILE" />Voice message to mobile number</td></tr>
						<tr><td align="right"></td><td><form:radiobutton disabled="false" path="passcodeDeliveryPreference"  value="VOICE_TO_PHONE" />Voice message to telephone number</td></tr>								
						<tr><td align="right"></td><td><form:radiobutton disabled="false" path="passcodeDeliveryPreference"  value="EMAIL" />Email</td></tr>
						
							<tr valign="top" class="datacell1">
							<td width="168"><ps:label fieldId="userName" mandatory="true">Username</ps:label></td>
							<td>
								 <form:input path="userName" id ="userName" autocomplete="off" cssClass="inputField" size="20"/>
							</td>
						</tr>
							<tr valign="top" class="datacell1">
								<td width="168"><ps:label fieldId="password" mandatory="true">Password</ps:label></td>
								<td>
									<form:password path="password" id="password" cssClass="inputField" size="32" maxlength="64" value="${registerForm.password}" />
									<!-- (5 to 32 characters, numbers &amp; letters only) -->
								</td>
							</tr>
						
				<!--		   <tr valign="top" class="datacell1"> -->
							<tr valign="top" class="datacell1">
							<td width="168"><ps:label fieldId="confirmPassword" mandatory="true">Confirm password</ps:label></td>
							<td>
								 <form:password path="confirmPassword" id="confirmPassword" cssClass="inputField" size="32" maxlength="64"/>
							</td>
						</tr>
						<tr class="datacell1" style="display: none;" id="pwdvalidationrow">
						<td>&nbsp;</td>
						<td colspan="2">
						<p id="pwdvalidation" style="font-size: 11;" class="redText"></p>
						</td>
						</tr>
						<tr class="datacell1">
						<td style="width:39%;"></td>
			            <td style="width:61%;">
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
						
							<%--consent content starts --%>
						<tr valign="top" class="datacell1">
							<td colspan="2">
								<content:contentBean contentId="<%=ContentConstants.MOBILE_CONSENT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
									beanName="MOBILE_CONSENT" />
										<content:getAttribute beanName="MOBILE_CONSENT" attribute="text"/>
							</td>
					</tr>
					<%--consent content ends --%>					
												
						</tbody>
						</table>
						</td>
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>

					<tr class="datacell1">
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
						<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan="2" rowspan="2" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
					</tr>
					<tr>
						<td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
						</tbody></table>
						<br>
						
						<%-- ends --%>
						
						
						
						<table width="425" border="0" cellspacing="0" cellpadding="0">
					<tbody><tr align="center">
						<td width="112">&nbsp;</td>
						<td width="139">&nbsp;</td>
						<td width="144">
							<input type="submit" class="button100Lg" disabled="disabled" id="continue" value="continue" onclick="javascript:setButtonAndSubmit('continue');return false;"/>
						</td>
					</tr>
					</tbody>
					</table>
				</td>

				
				
				
			<td width="5%" height="312" valign="top" class="fixedTable"><img src="/assets/unmanaged/images/spacer.gif" width="20" height="1"></td>
			<td width="15%" height="312" valign="top" class="fixedTable">
				<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" />
				<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer2" />
				<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer3" />
			</td>
		</tr>
		<tr>
		   <td><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
		   <td colspan="3">
			 <br>
			 <p><content:pageFooter beanName="layoutPageBean"/></p>
			 <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
			 <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
		   </td>
		</tr>
		
	</tbody>
	</ps:form>
	
	
</table>
<script>
	setFocusOnFirstInputField("registerForm");
	<% if(null!=deapiStatus && deapiStatus.equals("down")){  
		%>
		document.getElementById("deapistatusdiv").style.display = "block";
		<%} else {%> 
		document.getElementById("deapistatusdiv").style.display = "none";;
	<% } %>
</script>