<%@page import="java.util.Objects"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%
String userName = (String) request.getSession(false).getAttribute("userId");


%>
<style>
.message_error {
background-position-x : 0px !important;
background-position-y : 0px !important;
}
</style>
<script type="text/javascript">
<!--
function doOnload() {
    var newPassword=document.forms['forgetPasswordForm'].newPassword;
    if (newPassword) {
        try {
        	newPassword.focus();
        } catch (e) {
        }
    }    
}
//-->
</script>


<script type="text/javascript">
var validationStatus = false;
var myProfileUserName;
<% if(Objects.nonNull(userName)) { %>
myProfileUserName = '<%=userName%>';
<% } %>

//if jquery is not already loaded then load the jquery.
if (typeof jQuery == 'undefined' || window.jQuery.fn.jquery == '1.3.2') {
var script = document.createElement('script');
script.src = '/assets/unmanaged/javascript/jquery-1.11.1.min.js';
document.getElementsByTagName('head')[0].appendChild(script);
script.onload = function() {};
}
	$(document)
			.ready(
					function(event) {
						/*code added to auto clear password field if there is a error :starts*/
						var element = document
								.getElementsByClassName("message message_error");
						if (typeof (element) != 'undefined' && element != null) {
							document.getElementById('newPassword').value = "";
							document.getElementById('confirmPassword').value = "";
							document.getElementById("save").disabled = false;
							document.getElementById("save").style.color = "";
						} else {
							document.getElementById("save").disabled = true;
							document.getElementById("save").style.color = "gray";
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
						document.getElementById('newPassword')
								.addEventListener('keydown',
										debounce(makeAjaxCall, 750));
						//$('#newPassword').on('keydown',
						//debounce(makeAjaxCall, 750)); 
						/* code added to fix latency issues: ends*/

						$("#newPassword")
								.keyup(
										function(e) {
											var csrfToken = '${_csrf.token}';
											var keyCodeStatus = e.which;
											if (keyCodeStatus != 16) {
												$('#defaulttxt').show();
												$('#fstmsg').show();
												$('#secmsg').show();
												$('#thmsg').show();
												$('#formsg').show();
												$('#fifthmsg').show();
												$('#seventhmsg').show();
												$('#eithmsg').show();
												$('#ninethmsg').show();
												//The below method does Client validation first and then server validation
												doPasswordValidation(
														document
																.getElementById('newPassword').value,
														myProfileUserName);
											}
										});

						//confirmPassword validation
						$("#confirmPassword")
								.keyup(
										function(e) {
											var newPasswordStr = document
													.getElementById('newPassword').value;
											var confirmPasswordStr = document
													.getElementById('confirmPassword').value;
											var confirmPwdStr = document
													.getElementById('pwdvalidation');
											if (newPasswordStr != confirmPasswordStr) {
												confirmPwdStr.innerHTML = "The Confirm Password field does not match the New Password entered";
												document.getElementById("save").disabled = true;
												document.getElementById("save").style.color = "gray";
											} else {
												confirmPwdStr.innerHTML = "";
												if (!seqConsIND && !isContextSpecificWordsIND && !usernameIND) {
													document
															.getElementById("save").style.color = "#FFFFFF";
													document
															.getElementById("save").disabled = false;
											}
												}
										});
					});
	var isContextSpecificWordsIND = true;
	var seqConsIND = true;
	var usernameIND = true;
	function doServerValidation(csrfToken) {
		var newPasswordTxt = document.getElementById('newPassword').value;
		var confirmPasswordTxt = document.getElementById('confirmPassword').value;
		if ((/\s/.test(document.getElementById("newPassword").value))) {
			alert("Blank Space Are Not Allowed");
		}
		var resultToken;
		var jsonItem = JSON.stringify({
			newPassword : document.getElementById("newPassword").value
		});
		var http = new XMLHttpRequest();
		var url = "/do/forgetPassword/step3/ajaxvalidator/?" + "_csrf="
				+ csrfToken;
		http.open("POST", url, false);
		http.setRequestHeader("Content-type", "application/json");
		http.onreadystatechange = function() {
			if (http.readyState == 4 && http.status == 200) {
				try {
					if (this.responseText.trim().search(/^(\[|\{){1}/) > -1) {
						var parsedData = JSON.parse(this.responseText);
						isContextSpecificWordsIND = parsedData.isContextSpecificWordsIND;
						seqConsIND = parsedData.seqConsIND;
						usernameIND = parsedData.usernameIND;
					}
					if (newPasswordTxt == confirmPasswordTxt) {
						document.getElementById("save").style.color = "#FFFFFF";
						document.getElementById("save").disabled = false;
					} else {
						var confirmPwdStr = document
								.getElementById('pwdvalidation');
						confirmPwdStr.innerHTML = "";
						document.getElementById("save").disabled = true;
						document.getElementById("save").style.color = "gray";

						if (document.getElementById("newPassword").value != ""
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
						
						if (document.getElementById("newPassword").value != ""
							&& seqConsIND != true) {
						$('span.nineth').remove();
						$('#ninethmsg')
								.prepend(
										'<span class ="nineth" style="color: green; font-size: 12px;"> &#10004;</span>');
					} else {
						$('span.nineth').remove();
						$('#ninethmsg')
								.prepend(
										'<span class ="nineth" style="color: red; font-size: 12px;"> &#10008;</span>');
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
			$('span.seven').remove();
			$('#seventhmsg')
					.prepend(
							'<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
		}
		
		if(!validationStatus && seqConsIND == true){
			$('span.nineth').remove();
			$('#ninethmsg').prepend('<span class ="nineth" style="color: red; font-size: 12px;"> &#10008;</span>');
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

		if (document.getElementById("newPassword").value == "") {
			$('span.seven').remove();
			$('#seventhmsg')
					.prepend(
							'<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
			$('span.nineth').remove();
			$('#ninethmsg').prepend('<span class ="nineth" style="color: red; font-size: 12px;"> &#10008;</span>');
			validationStatus = false;
		}

	}

	function makeAjaxCall() {
		var csrfToken = '${_csrf.token}';
		doServerValidation(csrfToken);
	}

	function userNameValidation(uname, pwd) {
		var userNameInd = false;
		if (pwd.indexOf(uname) !== -1
				|| pwd.indexOf(uname.toUpperCase()) !== -1
				|| pwd.indexOf(uname.toLowerCase()) !== -1) {
			userNameInd = true;
		}

		if (pwd.indexOf(uname) !== -1) {
			userNameInd = true;
		}
		if (pwd == "") {
			userNameInd = true;
		}
		return userNameInd;
	}
</script>

<content:contentBean contentId="<%=BDContentConstants.NEW_PASSWORD_HELP_TEXT%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="pwdHelpText" />

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
	<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
<report:formatMessages scope="session"/>	
<bd:form action="/do/forgetPassword/step3" method="post" modelAttribute="forgetPasswordForm" name="forgetPasswordForm">

	<div class="BottomBorder">
	<div class="SubTitle Gold Left"><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></div>
	<div class="GrayLT Right">* = Required Field</div>
	
	</div>
	<div class="inputTextFull">
		<content:getAttribute id='pwdHelpText' attribute='text' />
	</div>
	<div style="height:1px"> </div>
	<div class="label">* New Password:</div>
	
	<div class="inputText">
	  <label>
<form:password path="newPassword" id="newPassword" showPassword="true"  maxlength="64"/>
	  </label>
	  <div style="color: black;">
		</br>
				<div id="defaulttxt" style="display: none;"><span class ="defaulttxt"></span>&nbsp;Passwords MUST meet all of the following criteria before you can continue:</div>
				<div id="fstmsg" style="display: none;"><span class ="one"></span>&nbsp;Be between 8-64 characters</div>
				<div id="secmsg" style="display: none;"><span class ="two"></span>&nbsp;Have at least 1 uppercase letter</div>
				<div id="thmsg" style="display: none;"><span class ="three"></span>&nbsp;Have at least 1 lowercase letter</div>
				<div id="formsg" style="display: none;"><span class ="four"></span>&nbsp;Have at least 1 number</div>
				<div id="fifthmsg" style="display: none;"><span class ="five"></span>&nbsp;Have at least 1 special character</div>
				<div id="seventhmsg" style="display: none;"><span class ="seven"></span>&nbsp;Not contain industry related words</div>
				<div id="eithmsg" style="display: none;"><span class ="eith"></span>&nbsp;Not contain username</div>
				<div id="ninethmsg" style="display: none;"><span class ="nineth"></span>&nbsp;Not contain repeating or sequential characters</div>
	  </div>
	 </div>

		<div class="label">* Confirm Password:</div>

		<div class="inputText">
			<label> <form:password path="confirmedPassword"
					id="confirmPassword" showPassword="true" maxlength="64" />
			</label>
		</div>

		<div class="inputText">
			<p id="pwdvalidation" style="font-size: 11; color: red;"></p>
		</div>

		<input type="hidden" name="action">
   <div class="formButton">
     <input type="button" class="blue-btn next" 
            onmouseover="this.className +=' btn-hover'" 
            onmouseout="this.className='blue-btn next'"
            name="save" value="Save" id ="save"
            onclick="return doProtectedSubmitBtn(document.forgetPasswordForm, 'continue', this)"> 
   </div>
   <div class="formButton">
     <input type="button" class="grey-btn back" 
            onmouseover="this.className +=' btn-hover'" 
            onmouseout="this.className='grey-btn back'"
            name="cancel" value="Cancel" 
            onclick="return doProtectedSubmitBtn(document.forgetPasswordForm, 'cancel', this)"> 
   </div>
</bd:form>
 </div>   

 <layout:pageFooter/>