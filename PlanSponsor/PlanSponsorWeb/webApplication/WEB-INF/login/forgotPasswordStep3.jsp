<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="/assets/unmanaged/javascript/yui-2.5.1/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="/assets/unmanaged/javascript/yui-2.5.1/event/event-min.js"></script>
<script type="text/javascript" src="/assets/unmanaged/javascript/yui-2.5.1/connection/connection-min.js"></script>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.service.security.role.ExternalUser" %>
<%@ page import="com.manulife.pension.service.security.role.InternalUser" %>
<%@ page import="com.manulife.pension.delegate.EnvironmentServiceDelegate" %>
<%@ page import="com.manulife.pension.service.security.exception.SecurityServiceException"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="psw"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
 		String deapiStatus = null;
		if (null != request.getSession(false) && null != request.getSession(false).getAttribute("Deapi")) {
			deapiStatus = (String) request.getSession(false).getAttribute("Deapi");
		}
%>
<psw:contentBean
	contentId="<%=ContentConstants.DE_API_ERROR_MESSAGE%>"
	type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
	beanName="DeapiErrorMessage" />
<script language="JavaScript1.2" type="text/javascript">

var submitted=false;
var validationStatus = false;
<c:if test="${passwordResetAuthenticationForm.username ne null}">
var myProfileUserName = '${passwordResetAuthenticationForm.username}';
</c:if>

function setButtonAndSubmit(button) {
	
	if (!submitted) {
		submitted=true;
		document.passwordResetAuthenticationForm.button.value = button;
		document.passwordResetAuthenticationForm.submit();
	} else {
		window.status = "Transaction already in progress.  Please wait.";
	}
}
var pwdStat;

	$(document)
			.ready(
					function(event) {

						/*code added to auto clear password field if there is a error :starts*/
						var element = document.getElementById('psErrors');

						if (typeof (element) != 'undefined' && element != null) {

							document.getElementById('newPassword').value = "";
							document.getElementById('confirmPassword').value = "";
							document.getElementById("cont").disabled = false;
							document.getElementById("cont").style.color = "";
						} else {

							document.getElementById("cont").disabled = true;
							document.getElementById("cont").style.color = "gray";
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
																.getElementById('newPassword').value,
														myProfileUserName);
											}
										});

						//confirmPassword validation
						$("#confirmPassword")
								.on("keyup",
										function(e) {
											var newPasswordStr = document
													.getElementById('newPassword').value;
											var confirmPasswordStr = document
													.getElementById('confirmPassword').value;
											var confirmPwdStr = document
													.getElementById('pwdvalidation');
											var pwdValidationRow = document
													.getElementById('pwdvalidationrow');
											if (newPasswordStr != confirmPasswordStr) {
												$('#pwdvalidationrow').show();
												confirmPwdStr.innerHTML = "The Confirm Password field does not match the New Password entered";
												document.getElementById("cont").disabled = true;
												document.getElementById("cont").style.color = "gray";
											} else {
												$('#pwdvalidationrow').hide();
												confirmPwdStr.innerHTML = "";
												if (pwdStat) {
													document
															.getElementById("cont").style.color = "#FFFFFF";
													document
															.getElementById("cont").disabled = false;
												}
											}
										});
					});
	var isContextSpecificWordsIND = true;
	var deApiStatus;
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
		var url = "/do/login/passwordReset/ajaxvalidator/?" + "_csrf="
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
							if (pwdStat == true
									&& newPasswordTxt == confirmPasswordTxt) {
								document.getElementById("cont").style.color = "#FFFFFF";
								document.getElementById("cont").disabled = false;
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
						$('#pwdvalidationrow').hide();
						confirmPwdStr.innerHTML = "";
						document.getElementById("cont").disabled = true;
						document.getElementById("cont").style.color = "gray";

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
		if (document.getElementById("newPassword").value == "") {
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


<TABLE width=550 border=0 cellPadding=0 cellSpacing=0>
  
  <TR>
    <!-- column 1 (15 + 135 + 15 = 165) -->
    <TD width="10"><IMG height=8 
      src="/assets/unmanaged/images/s.gif" width=10 border=0></TD>
    <TD width="100" vAlign=top>
      <TABLE id=column1 cellSpacing=0 cellPadding=0 border=0>
        <TBODY>
          <TR vAlign=top>
            <TD><IMG src="/assets/unmanaged/images/s.gif" width=100 height=1></TD>
          </TR>
          <TR>
            <TD class=greyText>&nbsp;</TD>
          </TR>
        </TBODY>
      </TABLE>
    </TD>
    <TD width="15"><IMG src="/assets/unmanaged/images/s.gif" width=15 border=0 height=1></TD>
    <!-- end column 1 -->
    <!-- column 2 (375) -->
    <TD width="425" vAlign=top class=greyText> 
      <table width="425" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
          <td valign="top" width="495">
				<font color="#CC6600">*</font> Required Information
          </td>
        </tr>
      </table>
	  <content:errors scope="session"/> 
	  <div id="deapistatusdiv" class="redText" style="display: none;">
         <content:getAttribute attribute="text"	beanName="DeapiErrorMessage">
		 </content:getAttribute>
         </div>

	  <ps:form method="POST"  modelAttribute="passwordResetAuthenticationForm" name="passwordResetAuthenticationForm"  action="/do/login/passwordReset/" >

	    	
      <table width="425" border="0" cellpadding="0" cellspacing="0">
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
          <td colspan="7" class="tableheadTD1"><strong> <content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></strong></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="6"><table width="100%" border="0" cellspacing="5px" cellpadding="0">
             <tr class="datacell1">
               <td class="datacell1"><strong>Name</strong></td>
<td class="datacell1">${passwordResetAuthenticationForm.name}</td>
             </tr>
            <tr class="datacell1">
               <td class="datacell1"><strong>Username</strong></td>
<td class="datacell1">${passwordResetAuthenticationForm.username}</td>
             </tr>             
             <tr class="datacell1">
               <td width="39%" class="datacell1"><strong>New password <font color="#CC6600">*</font></strong></td>
               <td width="61%" class="datacell1"><strong>
                   <form:password path="newPassword" id="newPassword" cssClass="inputField" size="42" maxlength="64" value="${passwordResetAuthenticationForm.newPassword}"/>
                     
             </tr>
			
             <tr class="datacell1">
               <td class="datacell1"><strong>Confirm new password <font color="#CC6600">*</font></strong></td>
               <td class="datacell1"><strong>
                  <form:password path="confirmPassword" id="confirmPassword" cssClass="inputField" size="42" maxlength="64" value="${passwordResetAuthenticationForm.confirmPassword}"/>
                    </strong></td>
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
				<div id="sixthmsg" style="display: none; margin-inline-start: 1px;"><span class ="six"></span>&nbsp;Not be something that someone can easily</br><div id="sixrowinlinediv"><span class ="sixspaninline"></span>&nbsp;&nbsp;&nbsp;&nbsp;guess eg "password"</div></div>
				<div id="fstmsg" style="display: none;"><span class ="one"></span>&nbsp;Be between 8-64 characters</div>
				<div id="secmsg" style="display: none;"><span class ="two"></span>&nbsp;Have at least 1 uppercase letter</div>
				<div id="thmsg" style="display: none;"><span class ="three"></span>&nbsp;Have at least 1 lowercase letter</div>
				<div id="formsg" style="display: none;"><span class ="four"></span>&nbsp;Have at least 1 number</div>
				<div id="fifthmsg" style="display: none;"><span class ="five"></span>&nbsp;Have at least 1 special character</div>
				<div id="seventhmsg" style="display: none;"><span class ="seven"></span>&nbsp;Not contain industry related words</div>
				<div id="eithmsg" style="display: none;"><span class ="eith"></span>&nbsp;Not contain username</div>
				
			</td>
			</tr>
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
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
          <td  colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
        </tr>
        <tr>
          <td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
      </table>
      <br>
      <table width="425" border="0" cellspacing="0" cellpadding="0">
        <tr align="center">
          <td width="112">&nbsp;</td>
          <td width="139">            
<input type="submit" class="button100Lg"  onclick="javascript:setButtonAndSubmit('cancel');return false;" value="cancel" name="cont"/>
          </td>
          <td width="144">
<input type="submit" class="button100Lg" id="cont" onclick="javascript:setButtonAndSubmit('continue');return false;" value="continue" name="cont" />
          </td>
<form:hidden path="button"/>
            <script type="text/javascript" >
				var onenter = new OnEnterSubmit('cont', 'continue');
				onenter.install();
			  </script>
        </tr>
      </table>
 	</ps:form>
      <br>
	  <p><content:pageFooter beanName="layoutPageBean"/></p>
 	  <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 	  <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
    </TD>
  </TR>
</TABLE>
<script>
	setFocusOnFirstInputField("passwordResetAuthenticationForm");
		<% if(null!=deapiStatus && deapiStatus.equals("down")){  
			%>
			document.getElementById("deapistatusdiv").style.display = "block";
			<%} else {%> 
			document.getElementById("deapistatusdiv").style.display = "none";;
		<% } %>
</script>
