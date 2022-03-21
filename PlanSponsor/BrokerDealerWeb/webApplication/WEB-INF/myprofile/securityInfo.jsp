<%@page import="java.util.Objects"%>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserProfile"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%
	BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
	String myUserInfo = userProfile.getBDPrincipal().getUserName();
%>
<content:contentBean contentId="<%=BDContentConstants.EXTERNAL_USER_PASSWORD_SECTION_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="passwordSectionTitle" />

<content:contentBean contentId="<%=BDContentConstants.EXTERNAL_USER_PASSWORD_HELP_TEXT%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="passwordHelpText" />
   
<content:contentBean contentId="<%=BDContentConstants.EXTERNAL_USER_CHALLENGE_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="challengeSectionTitle" />

<content:contentBean contentId="<%=BDContentConstants.EXTERNAL_USER_CURRENT_PWD_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="currentPwdSectionTitle" />

<content:contentBean contentId="<%=BDContentConstants.CANCEL_POP_UP_MESSAGE%>" 
    type="<%=BDContentConstants.TYPE_MESSAGE%>" id="cancelMessage" />
    
<content:contentBean contentId="<%=BDContentConstants.CHALLENGE_QUESTION_SECTION_DESCRIPTION%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="challengeSectionDescription" />

<c:set var="form" value="${myprofileSecurityInfoForm}" scope="page"/>
<utils:cancelProtection name="myprofileSecurityInfoForm" changed="${form.changed}"
     exclusion="['currentPassword']"/>

<%-- somehow sup cause problem is IE. Use this as a workaround --%>
<style type="text/css">
span.super {
    font-size: 10px; 
	height: 0;
	line-height: 1;
	vertical-align: baseline;
	_vertical-align: bottom;
	position: relative;
	bottom: 1ex;
}
</style>

<script type="text/javascript">
<!--
	<%-- This function is to show/hide the create your own question input field
    The question id '0' corresponds the the create your own question --%> 
	function selectChallengeQuestion(selection, questionText, divElement) {
		if (selection.value=='0') {
			divElement.style.display="";
		} else {
			divElement.style.display="none";
			selection.form.elements[questionText].value='';
		}
	}
	<%-- Function to clear the answer fields when the question changes. --%>
	function clearAnswers(selection, elementPrefix) {
		selection.form.elements[elementPrefix+'.answer'].value='';
		selection.form.elements[elementPrefix+'.confirmedAnswer'].value='';
	}

	//-->
</script>

<script type="text/javascript">
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
	
$(document).ready(function(event) {
	
	if(document.getElementById('confirmPassword').value ==""){
		document.getElementById('confirmPassword').disabled=true;
		document.getElementById("confirmPassword").style.color = "gray";
		document.getElementById('newPassword').value = ""; 
	}
	else
	{
		document.getElementById('confirmPassword').disabled=false;
		document.getElementById("confirmPassword").style.color = "";
	} 
	/*code added to auto clear password field if there is a error  :starts*/	
	var element = document.getElementsByClassName('message message_error');
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
		 
 	$("#newPassword").keyup(function(e) {
 var csrfToken ='${_csrf.token}';
 var keyCodeStatus = e.which;
 if(keyCodeStatus != 16) {
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
	 doPasswordValidation(document.getElementById('newPassword').value,myProfileUserName);
}
});
			
			//confirmPassword validation
			$("#confirmPassword").keyup(function(e) {
					 var newPasswordStr = document.getElementById('newPassword').value;
					 var confirmPasswordStr = document.getElementById('confirmPassword').value;
					 var confirmPwdStr = document.getElementById('pwdvalidation');
					 if(newPasswordStr != confirmPasswordStr){
						 confirmPwdStr.innerHTML = "The Confirm Password field does not match the New Password entered";
					 }
					 else{
						 confirmPwdStr.innerHTML = "";
					 }
		    });
});
var isContextSpecificWordsIND = true;
var seqConsIND = true;
 function doServerValidation(csrfToken){
	 	if((/\s/.test(document.getElementById("newPassword").value))){
			alert("Blank Space Are Not Allowed");
		}
	 	var resultToken ;
		var jsonItem = JSON.stringify({newPassword : document.getElementById("newPassword").value});
		var http = new XMLHttpRequest();
		var url = "/do/myprofile/security/ajaxvalidator/?"+"_csrf="+csrfToken;
		http.open("POST", url, false);
		http.setRequestHeader("Content-type", "application/json");
		http.onreadystatechange = function() {
	     if(http.readyState == 4 && http.status == 200) {
	       try{
			if (this.responseText.trim().search(/^(\[|\{){1}/) > -1) {
				var parsedData = JSON.parse(this.responseText);
				isContextSpecificWordsIND = parsedData.isContextSpecificWordsIND;
				seqConsIND = parsedData.seqConsIND;
			}
				var confirmPwdStr = document.getElementById('pwdvalidation');
				confirmPwdStr.innerHTML = "";
				document.getElementById('confirmPassword').disabled=false;
				document.getElementById("confirmPassword").style.color = "";
				
				if(document.getElementById("newPassword").value!="" && isContextSpecificWordsIND != true)
				{
					$('span.seven').remove();
					$('#seventhmsg').prepend('<span class ="seven" style="color: green; font-size: 12px;"> &#10004;</span>');
				}else{
					$('span.seven').remove();
					$('#seventhmsg').prepend('<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
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
			$('span.seven').remove();
			$('#seventhmsg').prepend('<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
		}
		
		if(!validationStatus && seqConsIND == true){
			$('span.nineth').remove();
			$('#ninethmsg').prepend('<span class ="nineth" style="color: red; font-size: 12px;"> &#10008;</span>');
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
			 	$('span.seven').remove();
				$('#seventhmsg').prepend('<span class ="seven" style="color: red; font-size: 12px;"> &#10008;</span>');
				$('span.nineth').remove();
				$('#ninethmsg').prepend('<span class ="nineth" style="color: red; font-size: 12px;"> &#10008;</span>');
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
</script>



<div id="contentFull">
	<layout:pageHeader nameStyle="h1"/>

<bd:form action="/do/myprofile/security" modelAttribute="myprofileSecurityInfoForm" name="myprofileSecurityInfoForm">

<c:if test="${form.success}">
 <utils:info contentId="<%=BDContentConstants.EXTERNAL_USER_SECURITY_UPDATE_SUCCESS_TEXT%>"/> 
</c:if>

<report:formatMessages scope="session"/>
	<userprofile:myprofileTab/>
	
<input type="hidden" name="action"/>

	<div class="BottomBorder">
		<div class="SubTitle Gold Left">
		     <content:getAttribute attribute="text" beanName="passwordSectionTitle"/>
		</div>
	</div>
	
	<div class="label">New Password:</div>
	
	<div class="inputText">
	  <label>
<form:password path="newPassword" id="newPassword" showPassword="true" cssClass="input" maxlength="64"/>
	  </label>
	  <br/>
	  <content:getAttribute attribute="text" beanName="passwordHelpText"/>
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

		<div class="label">Confirm New Password:</div>

		<div class="inputText">
			<label> <form:password path="confirmedPassword"
					id="confirmPassword" showPassword="true" cssClass="input"
					maxlength="64" />
			</label>
		</div>

		<div class="inputText">
			<p id="pwdvalidation" style="font-size: 11; color: red;"></p>
		</div>

		<div class="BottomBorder">
	  <div class="SubTitle Gold Left">
	    <content:getAttribute attribute="text" beanName="challengeSectionTitle"/>
	  </div>
	</div>
		
	<p><content:getAttribute attribute="text" beanName="challengeSectionDescription"/></p>
	
	<div class="label">Current 1<span class="super">st</span> Question:</div>
	<div class="inputText"><br />
	  ${e:forHtmlContent(form.currentChallengeQuestions[0])} &nbsp;
	</div>
	<div class="label">Current 2<span class="super">nd</span> Question:</div>	  
	
	<div class="inputText"> 
	  ${e:forHtmlContent(form.currentChallengeQuestions[1])} &nbsp;
	</div>
	 	
	<userprofile:challengeInput questionNum="1" value="${form.challenge1}" update="true"/>
	
	<userprofile:challengeInput questionNum="2" value="${form.challenge2}" update="true"/>

	<div class="BottomBorder">
		<div class="SubTitle Gold Left">
		     <content:getAttribute attribute="text" beanName="currentPwdSectionTitle"/>
		</div>
	</div>
	<div class="label">Current Password:</div>
	<div class="inputText">
	  <label>
<form:password path="currentPassword" showPassword="true" cssClass="input" maxlength="64"/>
	  </label>
	</div>  

   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="save" value="Save" id="save"
	        onclick="return doProtectedSubmitBtn(document.myprofileSecurityInfoForm, 'save', this)">
        </div> 

    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Close" id="save"
             onclick="return doCancelBtn(document.myprofileSecurityInfoForm, this)"> 
    </div>
 </bd:form>	
 <br class="clearFloat" />
</div>

<layout:pageFooter/>
