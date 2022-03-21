<%@page import="java.util.Objects"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import = "com.manulife.pension.delegate.EnvironmentServiceDelegate" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.role.ExternalUser" %>
<%@ page import="com.manulife.pension.service.security.role.InternalUser" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="psw"%>
<%
	UserProfile userProfile = (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile", userProfile, PageContext.PAGE_SCOPE);
	com.manulife.pension.service.security.valueobject.UserInfo myUserInfo = com.manulife.pension.delegate.SecurityServiceDelegate
	.getInstance().getUserInfo(((UserProfile) pageContext.getAttribute("userProfile")).getPrincipal());
	request.setAttribute("userInfo", myUserInfo);
	
	// start changes for US 44837
		
		boolean changePasswordFlag = Boolean.FALSE;
		if (null != session.getAttribute(Constants.PASSWORD_CHANGE_NEW_USER)) {
	changePasswordFlag = (Boolean) session.getAttribute(Constants.PASSWORD_CHANGE_NEW_USER);
		}
		String businessParamIndicator = null;
		EnvironmentServiceDelegate envservice = null;
		
		if (null != session.getAttribute(Constants.PSW_SHOW_PASSWORD_METER_IND)) {
	businessParamIndicator = (String) session.getAttribute(Constants.PSW_SHOW_PASSWORD_METER_IND);
		}else{
	envservice = EnvironmentServiceDelegate.getInstance();
	try{
	businessParamIndicator = envservice.getBusinessParam(Constants.PSW_SHOW_PASSWORD_METER_IND);
	}catch(Exception e){
		

	}
		}
		
		boolean isExternalUser = myUserInfo.getRole() instanceof ExternalUser;
		
		
		boolean isInternalUser = myUserInfo.getRole() instanceof InternalUser;
		
	    boolean businessIndicator = ("EXT".equals(businessParamIndicator) && isExternalUser);
	   
	    businessIndicator = ("INT".equals(businessParamIndicator) && isInternalUser) || businessIndicator;
	   
	    businessIndicator = ("ALL".equals(businessParamIndicator)) || businessIndicator;
	    String deapiStatus = null;
		if (null != request.getSession(false) && null != request.getSession(false).getAttribute("Deapi")) {
			deapiStatus = (String) request.getSession(false).getAttribute("Deapi");
		}

	// End changes for US 44837
%>

<c:set var="businessIndicator" value="<%=businessIndicator %>" scope="session"/>
<psw:contentBean
	contentId="<%=ContentConstants.DE_API_ERROR_MESSAGE%>"
	type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
	beanName="DeapiErrorMessage" />
	
<script type="text/javascript">
var deapiScoreStatus =  false;
var validationStatus = false;
var myProfileUserName;
<% if(Objects.nonNull(myUserInfo.getUserName())) { %>
	myProfileUserName = '<%=myUserInfo.getUserName()%>';
<% } %>
var submitted=false;
function setButtonAndSubmit(button) {

	if (!submitted) {
		submitted=true;
		document.updatePasswordForm.button.value = button;
		document.updatePasswordForm.submit();
	} else {
		window.status = "Transaction already in progress.  Please wait.";
	}
}


<%if(businessIndicator){%> 
var pwdStat;
$(document).ready(function(event) {
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
		 
		document.getElementById("continue").disabled = true;  
		document.getElementById("continue").style.color = "gray";
				$("#newPassword").on("keyup",function(e) {
			 $('span.sixspaninline').remove();
			 $('#sixrowinlinediv').prepend('<span class ="sixspaninline">&nbsp;&nbsp;&nbsp;</span>');
			 var keyCodeStatus = e.which;
			 if(keyCodeStatus != 16) {
				//The below method does Client validation first and then server validation
				 doPasswordValidation(document.getElementById('newPassword').value,myProfileUserName);
			}
    });
		/* //on change event		
		 $("#newPassword").change(function(){
			 var csrfToken ='${_csrf.token}';  
			 if(deapiScoreStatus){
			    	doServerValidation(csrfToken);
			    }
			  });	 */		
	
			  
	//confirmPassword validation
	$("#confirmPassword").on("keyup",function(e) {
		var newPasswordStr = document.getElementById('newPassword').value;
			 var confirmPasswordStr = document.getElementById('confirmPassword').value;
			 var confirmPwdStr = document.getElementById('pwdvalidation');
			 var pwdValidationRow = document.getElementById('pwdvalidationrow');
			
			 if(newPasswordStr != confirmPasswordStr){
				$('#pwdvalidationrow').show();
				confirmPwdStr.innerHTML = "The Confirm Password field does not match the New Password entered";
				document.getElementById("continue").disabled = true;
				document.getElementById("continue").style.color = "gray";
			 }
			 else{
				$('#pwdvalidationrow').hide();
				confirmPwdStr.innerHTML = "";
				if(pwdStat){
				document.getElementById("continue").style.color = "#FFFFFF";
				document.getElementById("continue").disabled = false;
				}
			 }
			 if(newPasswordStr =="" && confirmPasswordStr ==""){
				 document.getElementById("continue").disabled = true;
				 document.getElementById("continue").style.color = "gray";
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
	    var resultToken ;
		var jsonItem = JSON.stringify({newPassword : document.getElementById("newPassword").value});
		var http = new XMLHttpRequest();
		var url = "/do/password/updatePassword/ajaxvalidator/?"+"_csrf="+csrfToken;
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
							deapiScoreStatus = false;
					}else{
						validationStatus = true;
						deapiScoreStatus = true;
						pwdStat = true;
						$('span.six').remove();
						$('#sixthmsg').prepend('<span class ="six" style="color: green; font-size: 12px;"> &#10004;</span>');
						if(pwdStat == true && newPasswordTxt == confirmPasswordTxt){
							document.getElementById("continue").style.color = "#FFFFFF";
							document.getElementById("continue").disabled = false;
						}
					}
			}else{
				$('span.six').remove();
				$('#sixthmsg').prepend('<span class ="six" style="color: red; font-size: 12px;"> &#10008;</span>');
				
				pwdStat = false;
				var confirmPwdStr = document.getElementById('pwdvalidation');
				$('#pwdvalidationrow').hide();
				confirmPwdStr.innerHTML = "";
				document.getElementById("continue").style.color = "gray";
				document.getElementById("continue").disabled = true;
				
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
 <%}%> 


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
			deapiScoreStatus = false;
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
				deapiScoreStatus = false;
			 }
		/* if(validationStatus && !deapiScoreStatus){
			doServerValidation(csrfToken);
		} */
	}
	function makeAjaxCall(){
		var csrfToken ='${_csrf.token}'; 
		
			doServerValidation(csrfToken);
		
	}
	function userNameValidation(uname,pwd){
		var userNameInd = false;
	
				if (pwd.indexOf(uname)!== -1 || pwd.indexOf(uname.toUpperCase())!== -1 || pwd.indexOf(uname.toLowerCase())!== -1)  {
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
<%
   String actionUrl = "/do/password/updatePassword/";
   
    %>
	<ps:form method="POST" modelAttribute="updatePasswordForm" name="updatePasswordForm" action="<%= actionUrl %>">

  <br>
  <%-- table with 3 columns --%>
  <table width="700" border="0" cellspacing="0" cellpadding="0" aria-describedby="Update Password Guidelines">

    <tr>
  <%-- error line --%>
      <td>&nbsp;<strong><font style="color:#CC6600;"> *</font></strong> Required Information <br><br>
         <content:errors scope="session" />
         <div id="deapistatusdiv" class="redText" style="display: none;">
         <content:getAttribute attribute="text"	beanName="DeapiErrorMessage">
		 </content:getAttribute> 
         </div>
      </td>
      <td colspan="2">&nbsp;</td>
    </tr>
	
    <tr>
      <td width="525"><img src="/assets/unmanaged/images/s.gif" width="525" height="1" alt="gif image"></td>
      <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1" alt="gif image"></td>
      <td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1" alt="gif image"></td>
    </tr>
	<tr>
	  <td>


<%-- detail table starts --%>
        <table width="525" border="0" cellpadding="0" cellspacing="0" aria-describedby="Update Password Guidelines">
          <tr>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
            <td width="80"><img src="/assets/unmanaged/images/s.gif" width="113" height="1" alt="gif image"></td>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
            <td width="327"><img src="/assets/unmanaged/images/s.gif" width="250" height="1" alt="gif image"></td>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
            <td width="80"><img src="/assets/unmanaged/images/s.gif" width="80" height="1" alt="gif image"></td>
            <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1" alt="gif image"></td>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
          </tr>
    	  <tr class="tablehead">
            <td class="tableheadTD1" colspan="8"><strong>
				<% if (true) { %>
		              <content:getAttribute id="layoutPageBean" attribute="body1Header"/> </strong>
				<% } else { %>
					Update Password
				<% } %>
            </td>
     	  </tr>

    	  <tr class="datacell1">
            <td class="databorder" align="left"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
            <td colspan="6" align="center" >
              <table width="100%" border="0" cellspacing="0" cellpadding="3" aria-describedby="Update Password Guidelines">



                <tr>
                  <td valign="top"><ps:label fieldId="newPassword" mandatory="false"><strong>New password</strong></ps:label>
                    <strong><font color="#CC6600"> *</font></strong></td>
                  <td valign="top">
                  <form:password path="newPassword" id="newPassword" size="42" maxlength="64"/>
                  
              	  </td>
              	 
				 
                </tr>
				
            	<tr>
              	  <td valign="top"><ps:label fieldId="confirmPassword" mandatory="false"><strong>Confirm new password</strong></ps:label>
                    <strong><font color="#CC6600"> *</font></strong></td>
              	  <td valign="top">
              	   <form:password path="confirmPassword"  size="42" maxlength="64" />
                 	<%-- <ps:password name="changePasswordForm" path="confirmPassword" size="42" maxlength="32"/> --%>
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
				<div id="defaulttxt" style="margin-inline-start: 3px;"><span class ="defaulttxt"></span>Passwords MUST meet all of the following criteria before you can continue:</div></br>
				<div id="sixthmsg" style="margin-inline-start: 1px;"><span class ="six"></span>&nbsp;Not be something that someone can easily guess</br><div id="sixrowinlinediv"><span class ="sixspaninline"></span>&nbsp;eg "password"</div></div>
				<div id="fstmsg"><span class ="one"></span>&nbsp;Be between 8-64 characters</div>
				<div id="secmsg"><span class ="two"></span>&nbsp;Have at least 1 uppercase letter</div>
				<div id="thmsg"><span class ="three"></span>&nbsp;Have at least 1 lowercase letter</div>
				<div id="formsg"><span class ="four"></span>&nbsp;Have at least 1 number</div>
				<div id="fifthmsg"><span class ="five"></span>&nbsp;Have at least 1 special character</div>
				<div id="seventhmsg"><span class ="seven"></span>&nbsp;Not contain industry related words</div>
				<div id="eithmsg"><span class ="eith"></span>&nbsp;Not contain username</div>
			</td>
			</tr>
            	

				

          	  </table>
        	</td>
        	<td class="databorder"  align="left"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      	  </tr>

      	  <tr class="whiteborder">
            <td class="databorder" width="1" ><img src="/assets/unmanaged/images/s.gif" width="1" height="4" alt="gif image"></td>
            <td width="1" class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
            <td width="1" class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
            <td width="1" class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
            <td width="1" class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
            <td width="1" class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
            <td rowspan="2"  colspan="2" ><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" alt="gif image"></td>
          </tr>
          <tr>
            <td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
          </tr>
    	</table>
    	<br>
    	<table width="525" border="0" cellspacing="0" cellpadding="0" aria-describedby="Update Password Guidelines">
          <tr align="center">
            <td width="132" align="right">
			
				<input type="button"
								onclick="javascript:setButtonAndSubmit('continue');return false;"
								name="action" id="continue" class="button100Lg" value="continue"
								disabled="true" />  <form:hidden path="button" /> 

        	</td>
      	  </tr>
    	</table>

  	  </td>
      <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1" alt="gif image"></td>
      <td width="180"><content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" /> </td>
  	</tr>
  </table>
</ps:form>
<script>
	setFocusOnFirstInputField("updatePasswordForm");
	<% if(null!=deapiStatus && deapiStatus.equals("down")){  
	%>
	document.getElementById("deapistatusdiv").style.display = "block";
	<%} else {%> 
	document.getElementById("deapistatusdiv").style.display = "none";;
	<% } %>
</script>
