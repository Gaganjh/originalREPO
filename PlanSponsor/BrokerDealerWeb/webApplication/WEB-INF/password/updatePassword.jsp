<%@page import="com.manulife.pension.service.security.bd.dao.BDProfileDao"%>
<%@page import="com.manulife.pension.service.security.bd.valueobject.BDUserProfileValueObject"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserProfile"%>
<%@page import="java.util.Objects"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="/assets/unmanaged/javascript/yui-2.5.1/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="/assets/unmanaged/javascript/yui-2.5.1/event/event-min.js"></script>
<script type="text/javascript" src="/assets/unmanaged/javascript/yui-2.5.1/connection/connection-min.js"></script>
<script type="text/javascript" src="/assets/unmanaged/javascript/jquery-1.11.1.min.js"></script>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<content:contentBean contentId="<%=BDContentConstants.CHANGE_PASSWORD_SUCCESS%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="confirmationMessage" />

<content:contentBean contentId="<%=BDContentConstants.NEW_PASSWORD_HELP_TEXT%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="pwdHelpText" />

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>
<%
	String deapiStatus = null;
	if (null != request.getSession(false) && null != request.getSession(false).getAttribute("Deapi")) {
		deapiStatus = (String) request.getSession(false).getAttribute("Deapi");
	}
	
	BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
	String myUserInfo = userProfile.getBDPrincipal().getUserName();
	// Check the FRW userRole as Internal /External
	boolean isExternalUser = Boolean.FALSE;
	if (null != request.getSession(false) && null != request.getSession(false).getAttribute("isFRWExternalUser")) {
		isExternalUser = (Boolean)request.getSession(false).getAttribute("isFRWExternalUser");
	}
%>
<content:contentBean
	contentId="<%=BDContentConstants.DE_API_ERROR_MESSAGE%>"
	type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
	beanName="DeapiErrorMessage" />


<style>

.pd-btm-10 {
padding-bottom : 12px!important

}

</style>
<script type="text/javascript">
<!--
function doOnload() {
  try {
          var firstInput=document.forms['updatePasswordForm'].elements[0];
          if (firstInput) {
	       	firstInput.focus();
           }
      } catch (e) {      
      }    
}
//-->
</script>

<c:if test="${requestScope.success}">
	<script type="text/javascript">
	<!--
	   YAHOO.util.Event.onDOMReady(confirmation)
	   
	   var confirmationMessage='<content:getAttribute beanName="confirmationMessage" attribute="text" filter="true"/>';
	   function confirmation() {
		   	alert(confirmationMessage);
		   	document.updatePasswordForm.action.value='continue';		   
		   	document.updatePasswordForm.submit();
	   }	   
	//-->
	</script>
</c:if>

<script type="text/javascript">
<!--
<c:choose>
  <c:when test="${not requestScope.success}">  
     function doSave(obj) {
		return doProtectedSubmitBtn(document.updatePasswordForm, 'change', obj);
     }
     function doCancel(obj) {
 		return doProtectedSubmitBtn(document.updatePasswordForm, 'cancel', obj);
     }	
  </c:when>
  <c:otherwise>
  function doSave(obj) {
		return false;
   }
   function doCancel(obj) {
		return false;
   }	
  </c:otherwise>
</c:choose>		
//-->
</script>

<script type="text/javascript">
var deapiScoreStatus =  false;
var validationStatus = false;
var myProfileUserName;
<% if(Objects.nonNull(myUserInfo)) { %>
myProfileUserName = '<%=myUserInfo%>';
<% } %>
//if jquery is not already loaded then load the jquery.
if (typeof jQuery == 'undefined' || window.jQuery.fn.jquery == '1.3.2') {
var script = document.createElement('script');
script.src = '/assets/unmanaged/javascript/jquery-1.11.1.min.js';
document.getElementsByTagName('head')[0].appendChild(script);
script.onload = function() {};
}
var pwdStat;

	$(document)
			.ready(
					function(event) {
						/*code added to auto clear password field if there is a error  :starts*/	
						<%if(!isExternalUser){%>
						document.getElementById("sixthmsg").style.display = "block";
						document.getElementById("ninethmsg").style.display = "none";
						<% } else {%>
						document.getElementById("sixthmsg").style.display = "none";
						document.getElementById("ninethmsg").style.display = "block";
						<% } %>
						var element = document.getElementsByClassName('message message_error');
						if (typeof (element) != 'undefined' && element != null && element.length!=0) {
							document.getElementById('newPassword').value = "";
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
						$('#newPassword').on('keydown',
								debounce(makeAjaxCall, 750));
						/* code added to fix latency issues: ends*/

						$("#newPassword")
								.keyup(
										function(e) {
											$('span.sixspaninline').remove();
											$('#sixrowinlinediv')
													.prepend(
															'<span class ="sixspaninline">&nbsp;&nbsp;&nbsp;</span>');
											var keyCodeStatus = e.which;
											if (keyCodeStatus != 16) {
												//The below method does Client validation first and then server validation
												doPasswordValidation(
														document
																.getElementById('newPassword').value,
														myProfileUserName);
											}
										});

						/* //on change event		
						 $("#newPassword").change(function(){
							 var csrfToken ='${_csrf.token}';  
							 if(deapiScoreStatus){
							    	doServerValidation(csrfToken);
							    }
						  }); */

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
												document
														.getElementById("continue").style.color = "gray";
												document
														.getElementById("continue").disabled = true;
											} else {
												confirmPwdStr.innerHTML = "";
												if ((pwdStat && !seqConsIND) || (validationStatus == true && seqConsIND == false && isContextSpecificWordsIND == false && usernameIND == false)) {
													document
															.getElementById("continue").style.color = "#FFFFFF";
													document
															.getElementById("continue").disabled = false;
												}
												
											}
											if (newPasswordStr == ""
													&& confirmPasswordStr == "") {
												document
														.getElementById("continue").disabled = true;
												document
														.getElementById("continue").style.color = "gray";
											}
										});
					});
	var isContextSpecificWordsIND = true;
	var deApiStatus;
	var seqConsIND = true ;
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
		var url = "/do/updatePassword/ajaxvalidator/?" + "_csrf=" + csrfToken;
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
						seqConsIND = parsedData.seqConsIND;
						usernameIND = parsedData.usernameIND;
						if (deApiStatus == 'down') {
							$('#deapistatusdiv').show();
						} else {
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
						if (resultToken == 0 || resultToken == 1) {
							pwdStat = false;
							$('span.six').remove();
							$('#sixthmsg')
									.prepend(
											'<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
							deapiScoreStatus = false;
						} else {
							validationStatus = true;
							deapiScoreStatus = true;
							pwdStat = true;
							$('span.six').remove();
							$('#sixthmsg')
									.prepend(
											'<span class ="six" style="color: green; font-size: 12px;"> &#10004;</span>');
							if (pwdStat == true
									&& newPasswordTxt == confirmPasswordTxt) {
								document.getElementById("continue").style.color = "#FFFFFF";
								document.getElementById("continue").disabled = false;
							}
						}

					} else {
						$('span.six').remove();
						$('#sixthmsg')
								.prepend(
										'<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
						pwdStat = false;
						var confirmPwdStr = document
								.getElementById('pwdvalidation');
						confirmPwdStr.innerHTML = "";
						document.getElementById("continue").style.color = "gray";
						document.getElementById("continue").disabled = true;

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
			$('span.six').remove();
			$('#sixthmsg')
					.prepend(
							'<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
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
			deapiScoreStatus = false;
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
			$('span.six').remove();
			$('#sixthmsg')
					.prepend(
							'<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
			$('span.seven').remove();
			$('#seventhmsg')
					.prepend(
							'<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
			$('span.nineth').remove();
			$('#ninethmsg').prepend('<span class ="nineth" style="color: red; font-size: 12px;"> &#10008;</span>');
			validationStatus = false;
			deapiScoreStatus = false;
		}

		/* if(validationStatus && !deapiScoreStatus){
			   doServerValidation(csrfToken);
		} */
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

<div id="content">
	<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>

	<%
    	BDSessionHelper.moveMessageIntoRequest(request);
	%>
<bd:form action="/do/updatePassword/" modelAttribute="updatePasswordForm" name="updatePasswordForm">
<report:formatMessages scope="request"/>

	<div id="deapistatusdiv" class="redText" style="display: none;color:red;">
         <content:getAttribute attribute="text"	beanName="DeapiErrorMessage">
		 </content:getAttribute>
         </div>
         <div class="BottomBorder">
	<div class="SubTitle Gold Left"><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></div>
	<div class="GrayLT Right">* = Required Field</div>
	</div>
	<div class="inputTextFull">
		<content:getAttribute id='pwdHelpText' attribute='text' />
	</div>
	<div class="label">* New Password:</div>
	<div class="inputText" style="width: 100%;">
	  <label>
<form:password path="password" id="newPassword" showPassword="true" disabled="${updatePasswordForm.disabled}" maxlength="64" style="margin-top: 14px;"/>
	  </label>
	  <div style="color: black;">
		</br>
				<div id="defaulttxt"><span class ="defaulttxt"></span>&nbsp;Passwords MUST meet all of the following criteria before you can continue:</div></br>
				<div id="sixthmsg" style="margin-inline-start: 1px;display:none;"><span class ="six"></span>&nbsp;Not be something that someone can easily guess</br><div id="sixrowinlinediv"><span class ="sixspaninline"></span>&nbsp;eg "password"</div></div>
				<div id="fstmsg"><span class ="one"></span>&nbsp;Be between 8-64 characters</div>
				<div id="secmsg"><span class ="two"></span>&nbsp;Have at least 1 uppercase letter</div>
				<div id="thmsg"><span class ="three"></span>&nbsp;Have at least 1 lowercase letter</div>
				<div id="formsg"><span class ="four"></span>&nbsp;Have at least 1 number</div>
				<div id="fifthmsg"><span class ="five"></span>&nbsp;Have at least 1 special character</div>
				<div id="seventhmsg"><span class ="seven"></span>&nbsp;Not contain industry related words</div>
				<div id="eithmsg"><span class ="eith"></span>&nbsp;Not contain username</div>
				<div id="ninethmsg" style="display: none;"><span class ="nineth"></span>&nbsp;Not contain repeating or sequential characters</div>
				
	  </div>
	 </div>
	    <div class="label">* Confirm Password:</div>
	<div class="inputText">
	  <label>
<form:password path="confirmedPassword" id = "confirmPassword" showPassword="true" disabled="${updatePasswordForm.disabled}" maxlength="64"/>
	  </label>
	<br />
	</div>
	<div class="inputText">
					<p id="pwdvalidation" style="font-size: 11;color: red;"></p>
			</div>
<input type="hidden" name="action"/>

 <c:choose>
        <c:when test="${not updatePasswordForm.disabled}"> 
		    <div class="formButton pd-btm-10">
		      <input type="button" class="blue-btn next" 
		             onmouseout="this.className='blue-btn next'"
		             name="continue" value="Continue" id="continue"
		             onclick="return doSave(this)" disabled="true"> 
		    </div>
        </c:when>
        <c:otherwise>      
		    <div class="formButton">
		      <input type="button" class="disabled-grey-btn next" 
		             name="continue" value="Continue" id="continue"
		             disabled="true">
		    </div> 
        </c:otherwise>
      </c:choose>
		   
      
</bd:form>
 </div>   
<script>
<% if(null!=deapiStatus && deapiStatus.equals("down")){  
	%>
	document.getElementById("deapistatusdiv").style.display = "block";
	<%} else {%> 
	document.getElementById("deapistatusdiv").style.display = "none";;
	<% } %>
</script>
