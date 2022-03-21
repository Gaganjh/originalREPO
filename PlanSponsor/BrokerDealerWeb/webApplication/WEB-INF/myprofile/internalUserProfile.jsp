<%@page import="java.util.Objects"%>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserProfile"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>

<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%
	BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
	String myUserInfo = userProfile.getBDPrincipal().getUserName();
	String deapiStatus = null;
	if (null != request.getSession(false) && null != request.getSession(false).getAttribute("Deapi")) {
		deapiStatus = (String) request.getSession(false).getAttribute("Deapi");
	}
%>
<content:contentBean
	contentId="<%=BDContentConstants.DE_API_ERROR_MESSAGE%>"
	type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
	beanName="DeapiErrorMessage" />
<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PERSONAL_SECTION_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="personalSectionTitle" />

<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PASSWORD_SECTION_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="passwordSectionTitle" />

<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PASSWORD_HELP_TEXT%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="passwordHelpText" />

<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_LICENSE_SECTION_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="licenseSectionTitle" />

<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_LICENSE_TEXT%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="licenseText" />

<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PREFERENCE_SECTION_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="preferenceSectionTitle" />

<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PREFERENCE_SECTION_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="preferenceSectionTitle" />

<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_PREFERENCES_SECTION_TITLE%>"
   type="<%=BDContentConstants.TYPE_MESSAGE%>" id="preferencesSectionTitle" />
   
   <c:set var="form" value="${myprofileInternalForm}" scope="page"/>

<utils:cancelProtection name="myprofileInternalForm" changed="${form.changed}"
	exclusion="['currentPassword']"/>


<div id="contentFull">
	<layout:pageHeader nameStyle="h1"/>

<c:if test="${form.success}">
 <utils:info contentId="<%=BDContentConstants.INTERNAL_USER_UPDATE_SUCCESS_TEXT%>"/>
</c:if>

<c:if test="${form.licenseWarning}">
 <utils:info contentId="<%=BDContentConstants.INTERNAL_USER_LICENSE_INFO%>"/>
</c:if>

<report:formatMessages scope="request"/>
  <div id="deapistatusdiv" class="message message_error" style="display: none; color:red;">
         <content:getAttribute attribute="text"	beanName="DeapiErrorMessage">
		 </content:getAttribute>
         </div>
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


var pwdStat;
$(document).ready(function(event) {
	
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
		 if( e.which != 9 ) {
	 document.getElementById("save").disabled = true;
	 document.getElementById("save").style.color = "gray";
		 }
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
			$("#confirmPassword").keyup(function(e) {
					 var newPasswordStr = document.getElementById('newPassword').value;
					 var confirmPasswordStr = document.getElementById('confirmPassword').value;
					 var confirmPwdStr = document.getElementById('pwdvalidation');
					 if(newPasswordStr != confirmPasswordStr){
						confirmPwdStr.innerHTML = "The Confirm Password field does not match the New Password entered";
						document.getElementById("save").disabled = true;
						document.getElementById("save").style.color = "gray";
					 }
					 else{
						 confirmPwdStr.innerHTML = "";
						if(pwdStat){
							document.getElementById("save").style.color = "#FFFFFF";
							document.getElementById("save").disabled = false;
							}
					 }
		    });
});
var isContextSpecificWordsIND = true;
var deApiStatus;
 function doServerValidation(csrfToken){
	 	var newPasswordTxt = document.getElementById('newPassword').value;
	    var confirmPasswordTxt = document.getElementById('confirmPassword').value;
	 	if((/\s/.test(document.getElementById("newPassword").value))){
			alert("Blank Space Are Not Allowed");
		}
	 	if(pwdStat == true && newPasswordTxt == confirmPasswordTxt){
			document.getElementById("save").style.color = "#FFFFFF";
			document.getElementById("save").disabled = false;
		}
	 	var resultToken ;
		var jsonItem = JSON.stringify({newPassword : document.getElementById("newPassword").value});
		var http = new XMLHttpRequest();
		var url = "/do/myprofile/internal/ajaxvalidator/?"+"_csrf="+csrfToken;
		http.open("POST", url, false);
		http.setRequestHeader("Content-type", "application/json");
		http.onreadystatechange = function() {
	     if(http.readyState == 4 && http.status == 200) {
	       try{
			if (this.responseText.trim().search(/^(\[|\{){1}/) > -1) {
				var parsedData = JSON.parse(this.responseText);
				resultToken =  parsedData.score;
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
					}
				
			}else{
				$('span.six').remove();
				$('#sixthmsg').prepend('<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
				var confirmPwdStr = document.getElementById('pwdvalidation');
				confirmPwdStr.innerHTML = "";
				
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
</script>


<bd:form action="/do/myprofile/internal" modelAttribute="myprofileInternalForm" name="myprofileInternalForm">

<input type="hidden" name="action" value="save"/>
	<div class="BottomBorder">
	  <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="personalSectionTitle"/></div>
	</div>
	<div class="label">First Name:</div>
	<div class="inputText">
	  <label>
	      <c:out value="${form.firstName}"/>
	  </label>
	</div>
	<div class="label">Last Name:</div>
	<div class="inputText">
	  <label>
	      <c:out value="${form.lastName}"/>
	  </label>
	</div>

	<div class="label">Email:</div>
	<div class="inputText">
	  <label>
	      <c:out value="${form.emailAddress}"/>
	  </label>
	</div>

	<div class="BottomBorder">
		<div class="SubTitle Gold Left">
		     <content:getAttribute attribute="text" beanName="passwordSectionTitle" />
		</div>
	</div>
    <div class="label">Current Password:</div>
	<div class="inputText">
	  <label>
<form:password path="currentPassword" showPassword="true" cssClass="input" maxlength="64"/>
	  </label>
	</div>  
	<div class="label">New Password:</div>
	
	<div class="inputText">
	  <label>
<form:password path="newPassword" id="newPassword" showPassword="true" cssClass="input" maxlength="64"/>
	  </label>
	  <br />
	  <content:getAttribute attribute="text" beanName="passwordHelpText"/>
	  <div style="color: black;">
		</br>
			<div id="defaulttxt" style="display: none;"><span class ="defaulttxt"></span>&nbsp;Passwords MUST meet all of the following criteria before you can continue:</div>
			<div id="sixthmsg" style="display: none; margin-inline-start: 1px;"><span class ="six"></span>&nbsp;Not be something that someone can easily guess</br><div id="sixrowinlinediv"><span class ="sixspaninline"></span>&nbsp;&nbsp;&nbsp;&nbsp;eg "password"</div></div>
			<div id="fstmsg" style="display: none;"><span class ="one"></span>&nbsp;Be between 8-64 characters</div>
			<div id="secmsg" style="display: none;"><span class ="two"></span>&nbsp;Have at least 1 uppercase letter</div>
			<div id="thmsg" style="display: none;"><span class ="three"></span>&nbsp;Have at least 1 lowercase letter</div>
			<div id="formsg" style="display: none;"><span class ="four"></span>&nbsp;Have at least 1 number</div>
			<div id="fifthmsg" style="display: none;"><span class ="five"></span>&nbsp;Have at least 1 special character</div>
			<div id="seventhmsg" style="display: none;"><span class ="seven"></span>&nbsp;Not contain industry related words</div>
			<div id="eithmsg" style="display: none;"><span class ="eith"></span>&nbsp;Not contain username</div>
	  </div>
	 </div>   
	
    <div class="label">Confirm New Password:</div>
    
	<div class="inputText">
	  <label>
<form:password path="confirmedPassword" id="confirmPassword" showPassword="true" cssClass="input" maxlength="64"/>
	  </label>
	 <br />
	 <div class="inputText" style="margin-left: -2.5px;">
					<p id="pwdvalidation" style="font-size: 11;color: red;"></p>
			</div>
	</div>
	
	<div class="BottomBorder">
		<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="licenseSectionTitle"/></div>
	</div>
	<div class="report_table">
	    <p>
	    <content:getAttribute attribute="text" beanName="licenseText"/>
	    </p>
		<label>
<form:radiobutton path="producerLicense" id="yes" value="true"/>
			Yes
		</label>
	    <label>
<form:radiobutton path="producerLicense" id="no" value="false"/>
		    No 
	    </label>
	</div>

	<div class="BottomBorder">
	<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="preferencesSectionTitle"/></div>
	</div>
	
	<div class="label">Default Fund Listing:<br /></div>
	<div class="inputText">
	  <label>
<form:radiobutton path="defaultSiteLocation" id="USA" value="USA"/>
		  USA
	  </label>
	  <label>
<form:radiobutton path="defaultSiteLocation" id="NY" value="NY"/>
		  New York
	  </label>
	   <br/>
	</div>

   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="save" value="Save" id="save"
	        onclick="return doProtectedSubmitBtn(document.myprofileInternalForm, 'save', this)">
        </div> 

    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Close" id="save"
             onclick="return doCancelBtn(document.myprofileInternalForm, this)"> 
    </div>
</bd:form>
</div> <!--contentFull  -->

 <layout:pageFooter/>
<script>
<% if(null!=deapiStatus && deapiStatus.equals("down")){  
	%>
	document.getElementById("deapistatusdiv").style.display = "block";
	<%} else {%> 
	document.getElementById("deapistatusdiv").style.display = "none";;
<% } %>
</script>