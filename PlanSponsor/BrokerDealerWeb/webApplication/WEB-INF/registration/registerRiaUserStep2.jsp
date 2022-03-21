<%@page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.bd.web.util.BDWebCommonUtils" %>
<style>
.message_error {
background-position-x : 0px !important;
background-position-y : 0px !important;
}
</style>
<content:contentBean contentId="<%=BDContentConstants.CANCEL_POP_UP_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="cancelMessage" />
<content:contentBean contentId="<%=BDContentConstants.FIRM_REP_ASSISTANT_CONTACT_INFO_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="contactSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.FIRM_REP_MESSAGE_CENTER_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="messageCenterText" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_PREFERENCES_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="preferencesSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_TERMS_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="termsSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_RIA_TERMS_TEXT%>" type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>" id="termsText" />

<c:set var="phoneNumber" value="${registerRiaUserStep2Form.creationVO.phoneNumber}" /> 

<script type="text/javascript">
<!--
function doOnload() {
    var address1=document.forms['registerRiaUserStep2Form'].elements['address.address1'];
    if (address1) {
        try {
        	address1.focus();
        } catch (e) {
        }
    }    
}
//-->
</script>


<script type="text/javascript">
var validationStatus = false;
//if jquery is not already loaded then load the jquery.
if (typeof jQuery == 'undefined' || window.jQuery.fn.jquery == '1.3.2') {
var script = document.createElement('script');
script.src = '/assets/unmanaged/javascript/jquery-1.11.1.min.js';
document.getElementsByTagName('head')[0].appendChild(script);
script.onload = function() {};
}
$(document).ready(function(event) {
			document.getElementById("continue").disabled = true;
			document.getElementById("continue").style.color = "gray";
				
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
				document.getElementById('newPassword').addEventListener('keydown', debounce(makeAjaxCall, 750));
				// $('#newPassword').on('keydown',
						  //debounce(makeAjaxCall, 750)); 
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
				 doPasswordValidation(document.getElementById('newPassword').value,document.getElementById("userName").value);
			}
    });
				
			//confirmPassword validation
			$("#confirmPassword").keyup(function(e) {
					 var newPasswordStr = document.getElementById('newPassword').value;
					 var confirmPasswordStr = document.getElementById('confirmPassword').value;
					 var confirmPwdStr = document.getElementById('pwdvalidation');
					 if(newPasswordStr != confirmPasswordStr){
						confirmPwdStr.innerHTML = "The Confirm Password field does not match the New Password entered";
						document.getElementById("continue").disabled = true;
						document.getElementById("continue").style.color = "gray";
					 }
					 else{
						confirmPwdStr.innerHTML = "";
						document.getElementById("continue").style.color = "#FFFFFF";
						document.getElementById("continue").disabled = false;
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
		var jsonItem = JSON.stringify({newPassword : document.getElementById("newPassword").value,userName : document.getElementById("userName").value});
		var http = new XMLHttpRequest();
		var url = "/do/registerRiaUser/step2/ajaxvalidator/?"+"_csrf="+csrfToken;
		http.open("POST", url, false);
		http.setRequestHeader("Content-type", "application/json");
		http.onreadystatechange = function() {
	     if(http.readyState == 4 && http.status == 200) {
	       try{
			var resultnew = this.responseText;
			if (this.responseText.trim().search(/^(\[|\{){1}/) > -1) {
				var parsedData = JSON.parse(this.responseText);
				isContextSpecificWordsIND = parsedData.isContextSpecificWordsIND;
				seqConsIND = parsedData.seqConsIND;
			}
				var confirmPwdStr = document.getElementById('pwdvalidation');
				confirmPwdStr.innerHTML = "";
				document.getElementById("continue").disabled = true;
				document.getElementById("continue").style.color = "gray";
				
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
				$('span.nineth').remove();
				$('#ninethmsg').prepend('<span class ="nineth" style="color: red; font-size: 12px;"> &#10008;</span>');
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


<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>
  
<c:set var="form" value="${requestScope.registerRiaUserStep2Form}" scope="page"/>
<div id="content">
<bd:form action="/do/registerRiaUser/step2" modelAttribute="registerRiaUserStep2Form" name="registerRiaUserStep2Form">

<input type="hidden" name="action"/>
<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
<report:formatMessages scope="session"/>
<div class="BottomBorder">
<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="contactSectionTitle"/></div>
<div class="GrayLT Right">* = Required Field</div></div>
  <div class="regSection">
   <div style="height:1px"> </div>    
	<div class="OverFlow">    
	    <div class="label"><b>Name:</b></div>
		<div class="inputText"><label>${form.creationVO.firstName} ${form.creationVO.lastName}</label></div>
		<div class="label"><b>Telephone #:</b></div>
		<div class="inputText"><label>${form.creationVO.phoneNumber}</label></div>
		<div class="label"><b>Email:</b></div>
		<div class="inputText"><label>${form.creationVO.emailAddress}</label></div>
		<div class="label"><b>View Statements For:</b></div>
				<c:forEach var="firmName" items="${form.sortedBDFirmNames}">
					<div class="inputText"><label>${firmName}</label></div>
				</c:forEach>
		</div>
 </div> <!-- regSection -->
<br class="clearFloat" /> 
<userprofile:userIdPasswd/>
<br class="clearFloat" />
<userprofile:challenges form="${form}"/>
<br class="clearFloat" />  
<div class="BottomBorder">
	<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="termsSectionTitle"/></div>
</div>
<div class="regSection">
	<div class="inputTextFull">
		<div class="terms"
					style="line-height: 1em !important; overflow: scroll !important; min-height: 9em !important; max-height: 9em !important;">
			<content:getAttribute attribute="body2" beanName="termsText" />
		</div>
	</div>
	<br />
	<div>
<form:checkbox path="acceptDisclaimer"/>
		* Yes, I have read and I agree to the terms and conditions.
	</div>
</div>

    <div class="formButtons">
   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="continue" value="Register" id="continue"
	        onclick="return doProtectedSubmitBtn(document.registerRiaUserStep2Form, 'continue', this)">
    </div> 

    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Cancel"
             onclick="return doRegistrationCancel(document.registerRiaUserStep2Form, '<content:getAttribute id='cancelMessage' attribute='text' />', this)"> 
    </div>
    </div>
</bd:form>

<layout:pageFooter/>
</div>