<%@ page
	import="com.manulife.pension.platform.web.authtoken.AuthTokenUtility"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page
	import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="java.util.Collections"%>
 <%@ page import="java.util.Enumeration"%>
 <%@ page import="java.util.List"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentHelper"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="psw"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>


<psw:contentBean
	contentId="<%=ContentConstants.SESSION_EXPIRY_WARNING_MESSAGE%>"
	type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
	beanName="SessionExpiryWarningMessage" />

<psw:contentBean contentId="<%=ContentConstants.SESSION_EXPIRED_TEXT%>"
	type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
	beanName="SessionExpiredText" />
	
<%
		try {
			// Session Expiry Timeout in minutes.
			int sessionTimeoutMinutes = (session != null) ? session.getMaxInactiveInterval()/60 : 15;
			// Time before session expiration when warning dialog will be displayed
			final int SESSION_EXPIRY_TIME_TO_REACT_MINUTES = 2;

			//  URL to logout, same as login since
			String urlToLogout = "/do/home/Signout/";

			// Session Expiry Warning Title, replace with CMA Content
			String sessionExpiryWarningTitle = "Timeout warning";

			// Session extension error message, replace with CMA content Id
			String sessionManagementErrorMessage = "We are experiencing technical difficulties. Please try login later.";

			// Session Expired Title, replace with CMA Content
			String sessionExpiredModalTitle = "Session expired";
			
			Enumeration<String> attributes = session.getAttributeNames();
			
			List attrsList = Collections.list(attributes);
			
			boolean loggedInInd = attrsList.contains("actionForm") || attrsList.contains("PASSCODE_SESSION_KEY")
					|| attrsList.contains("PSW_SHOW_PASSWORD_METER_IND")
					|| attrsList.contains("challengePasscodeInd");
			
			//Session Expiry Pop-up shows in Challenge Passcode page in ForgotPassword Pages
			String referrer = null; 
			if(request.getHeader("referer")!= null){
			referrer = request.getHeader("referer");
			} 
			String refererInd = null;
			if(null!= referrer && !referrer.contains("/login/passwordResetChallenge/") && !referrer.contains("/login/forgotPasswordPasscodeTransition/") ){
				refererInd="true";
			}
			else{
				refererInd="false";
			}
			
			boolean passwordAuthenticationForm = attrsList.contains("passwordResetAuthenticationForm") && refererInd.equals("true");
	%>

<style>
/* Change style per style user story */
.sessionWarningModal {
	display: none;
	z-index: 100000;
	top: 0;
	left: 0;
	position: absolute;
	overflow: auto;
	width: 100%;
	height: 100%;
	margin-left: 0px;
	margin-top: 0px;
	overflow: auto;
	background: rgba(0, 0, 0, 0.5);
}

@media ( min-width : 992px) {
	.sessionWarningContent {
		background-color: #f3f1f1;
		padding: 0px;
		border: 1px solid #999;
		width: 460px;
		height: 169.5px
		top: 30%;
		text-align: left;
		z-index: 100000;
		margin-top: 25%;
		margin-right: 52%;
		margin-left: 13%;
	}
}

@media ( max-width : 991px) {
	.sessionWarningContent {
		background-color: #f3f1f1;
		padding: 0px;
		border: 1px solid #999;
		width: auto;
		height: auto;
		left: 30%;
		top: 30%;
		text-align: left;
		z-index: 100000;
		border-radius: 6px;
	}
}

.sessionExpiredModal {
	display: none;
	z-index: 100000;
	width: 100%;
	height: 100%;
	position: absolute;
	left: 0%;
	top: 0%;
	margin-left: 0px;
	margin-top: 0px;
	overflow: auto;
	background: rgba(0, 0, 0, 0.5);
}

.sessionExpiredContent {
	margin: 7% auto;
	padding: 0px;
	width: 360px;
	height: 200px;
	left: 30%;
	top: 30%;
	text-align: left;
	z-index: 100000;
	margin-top: 25%;
	margin-right: 56%;
}

.modal-content {
	margin-top: -18.7px;
	background-color: #f3f1f1;
}

.modal-content a {
	text-decoration: underline;
}

.modal-header {
	display: -ms-flexbox;
	display: flex;
	-ms-flex-align: start;
	align-items: flex-start;
	-ms-flex-pack: justify;
	justify-content: space-between;
	padding: 0;
}

.modal-title {
	font-size: 18.5px;
	padding-bottom: 10px;
	padding-left: 12px;
	padding-top: 6px;
	position: relative;
	display: block;
	border-bottom: 1px solid #eee;
	background-color: #002D62;
	color: #FFF;
	font-family: Arial, Helvetica, sans-serif;
}

.modal-body {
	position: relative;
	padding: $modal-inner-padding;
}

.col-xs-12, .col-md-12 {
	clear: both;
}

.modal-bg {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	opacity: 0;
	z-index: 10;
	visibility: hidden;
	transition: background-color$base-duration/2 linear;
}

.modal-footer {
	position: relative;
	display: flex;
	align-items: center;
	justify-content: flex-end;
	width: 100%;
	margin: 0;
	padding: 10px 0 0;
}
.divider{
    height:auto;
    display:inline-block;
    padding: 8.1px;
}
</style>
<script> 
	var sessionTimeoutMinutes = <%=sessionTimeoutMinutes%>; 
	var urlToLogout= '<%=urlToLogout%>';	
	var wcagCompliantTimeToReactMinutes = <%=SESSION_EXPIRY_TIME_TO_REACT_MINUTES%>;  
	var sessionManagementErrorMessage = '<%=sessionManagementErrorMessage%>';  
	var millisecondsPerMinute = 60000;	
	var bufferMilliseconds = 5000;	
	var sessionExpired = false;  
	var sessionExpiryTimeout = null;
	var warningTimeId = null;
	var sessionWarningmilliseconds = null;
	var remainingMinute =  null;
	var remainingSeconds = null;
	var myVar = null;
	var loggedInInd = <%= loggedInInd %>;
	var passwordAuthenticationForm = <%= passwordAuthenticationForm %>;
	
	// Execute the warning only when session timeout is populated.
	if (sessionTimeoutMinutes != undefined && parseInt(sessionTimeoutMinutes) > 0) {
		// if jquery is not already loaded then load the jquery.
		if (typeof jQuery == 'undefined' || window.jQuery.fn.jquery == '1.3.2') {
			var script = document.createElement('script');
			script.src = '/assets/unmanaged/javascript/jquery-3.6.0.min.js';
			document.getElementsByTagName('head')[0].appendChild(script);
			script.onload = function() {
				if(loggedInInd){
				startTimer();
				}
			};
		} else { 
			if(loggedInInd){
			startTimer();
			}
		} 
	}
	
	// Closes session Warning Dialog
	function closeSessionWarningDialog() {
		var sessionWarningModal = document.getElementById('sessionWarningModalDiv');
		sessionWarningModal.style.display = "none";
		document.documentElement.style.overflow = 'scroll';
		document.body.scroll = "yes";
	}
	
	// Shows the Session Expiry Warning dialog.
	function showSessionWarning() {
		if (sessionExpired === true) return;
		window.scrollTo(0, 0);
		var sessionWarningModal = document.getElementById('sessionWarningModalDiv');
		if(loggedInInd && !passwordAuthenticationForm){
			sessionWarningModal.style.display = "block";
			document.documentElement.style.overflow = 'hidden';
			document.body.scroll = "no";
		}else{
			sessionWarningModal.style.display = "none";
		    document.documentElement.style.overflow = 'scroll';
		    document.body.scroll = "yes";
	    }
		sessionWarningmilliseconds = 1000*Math.round((wcagCompliantTimeToReactMinutes*millisecondsPerMinute)/1000); // round to nearest second
		myVar = setInterval(myTimer, 1000);
		$("#continuebtn").trigger("focus");
		
	}
	
	//CountDown timer Function
	function myTimer() {
		var sessionWarnigremainingSec = sessionWarningmilliseconds - 1000;
		var d = new Date(sessionWarningmilliseconds);
		sessionWarningmilliseconds = sessionWarnigremainingSec;
		if(sessionWarningmilliseconds > 0){
		 remainingMinute = d.getUTCMinutes();
		 remainingMinute = d.getUTCSeconds();
		 document.getElementById("sessionWarningCountDown").textContent ="<content:getAttribute attribute='text' beanName='SessionExpiryWarningMessage'>" + "<content:param>"+d.getUTCMinutes()+"</content:param>"+"<content:param>" + d.getUTCSeconds() +"</content:param>" +"</content:getAttribute>" ;
		}
		}
	
	// Extends the session
	function extendSession() {
		sessionWarningmilliseconds =  null;
		$(".sess-png").remove();
		sessionExpired = false;
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			try{
			if (this.readyState == 4 && this.status == 200) {
				var parsedData = $.JSON.parse(this.responseText);
				var result = parsedData.result;
				var resultValue = result.replace(/^\s+|\s+$/g, "");	
				if (resultValue == 'Y') {
					sessionExpired = false;
				} 	
			}
			}catch(e){console.log(e);}
			closeSessionWarningDialog();

		};
		xhttp.open("GET", "/do/home/managesession?action=extendSession", true);
		xhttp.send();
		
		if (sessionExpiryTimeout != null) {
			clearTimeout(sessionExpiryTimeout);
		}
		clearInterval(myVar);
		startSessionTimeoutWarningTimer();
	}
	
	// Extends the session
	function keepSessionAlive(e) {
	    if (sessionExpired === true) return;
		$(".sess-png").remove();
		sessionExpired = false;
		$.get( "/do/home/managesession?action=extendSession", function(data) {});
		if (sessionExpiryTimeout != null) {
			clearTimeout(sessionExpiryTimeout);
		}
		if (warningTimeId != null) {
			clearTimeout(warningTimeId);
		}
		startSessionTimeoutWarningTimer();
	}
	
	//Closes session expired dialog
	function closeSessionExpiredDialog() {
		expireSession();
		if (urlToLogout != undefined) {
			top.location.href=urlToLogout;
		}
	}
	
	// Function to expire session
	function expireSession() {	
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			try{
			if (this.readyState == 4 && this.status == 200) {
				var parsedData = $.JSON.parse(this.responseText);
				var result = parsedData.result;
				var resultValue = result.replace(/^\s+|\s+$/g, "");	
				if (resultValue == 'Y') {
					sessionExpired = true;
				} 	
			}
			}catch(e){}
		};
		xhttp.open("GET", "/do/home/managesession?action=expireSession", true);
		xhttp.send();
	}
	
	// Shows the Session Expired dialog.
	function showSessionExpired() {
		expireSession();
		closeSessionWarningDialog();
		window.scrollTo(0, 0);
		var sessionExpiredModal = document.getElementById('sessionExpiredModalDiv');
		if(loggedInInd && !passwordAuthenticationForm){
			sessionExpiredModal.style.display = "block";
			document.documentElement.style.overflow = 'hidden';
			document.body.scroll = "no";
		}else{
			sessionExpiredModal.style.display = "none";
		    document.documentElement.style.overflow = 'scroll';
		    document.body.scroll = "yes";
	    }
	}
	
	// Starts the session timeout warning and display the messages appropriately.
	function startSessionTimeoutWarningTimer() {
		try {
			var warningTime = (sessionTimeoutMinutes-wcagCompliantTimeToReactMinutes) * millisecondsPerMinute;
			warningTimeId = setTimeout(showSessionWarning, warningTime);
			var sessionExpiredMsg = sessionTimeoutMinutes * millisecondsPerMinute;
			sessionExpiryTimeout = setTimeout(showSessionExpired, sessionExpiredMsg);
		} catch (e) { }
	} 
	
	function startTimer(){
		  $(document).ready(function(){
			  startSessionTimeoutWarningTimer();
			 
		});  
	}
	
	
	
</script>
<%-- Session Expiry Warning Modal --%>
<div id="sessionWarningModalDiv" class="sessionWarningModal"
	aria-hidden="true" data-keyboard="false" data-backdrop="static"
	tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="sessionWarningContent">
		<div class="modal-content">
			<div class="modal-body">
				<h3 class="modal-title"><%=sessionExpiryWarningTitle%></h3>
				<div class="row" style="margin: 12px;">
					<div class="col-md-12">
						<h2>
							<p id="sessionWarningCountDown" style="font-family: Arial, Helvetica, sans-serif; font-size:11px;"></p>
						</h2>
					</div>
				</div>
				<br />
				<div class="row" style="margin: 12px;">
					<div class="modal-footer">
						<button type="button" class="button100Lg"
							style="background-size: 110px; width: 110px;"
							onClick="javascript:closeSessionExpiredDialog();">logout</button>
							<div class="divider"></div>
						<input type="button" value="continue working"
							class="button100Lg" name="continuebtn" id="continuebtn"
							style="background-size: 110px; width: 110px;"
						 onClick="javascript:extendSession();" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<%-- Session Expired Modal --%>
<div id="sessionExpiredModalDiv" class="sessionExpiredModal"
	aria-hidden="true" data-keyboard="false" data-backdrop="static"
	tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="sessionExpiredContent">
		<div class="modal-content">
			<h3 class="modal-title"><%=sessionExpiredModalTitle%></h3>
			<div class="row" style="margin-left: 13px;">
				<div class="col-md-12">
					<h2><p style="font-family: Arial, Helvetica, sans-serif; font-size:11px;">
					<content:getAttribute attribute="text" beanName="SessionExpiredText" />
					</p></h2>
				</div>
			</div>
			<br/>
			<div class="row">
				<div class="modal-footer">
					<input type="button" value="ok" class="button100Lg"
						data-dismiss="modal"
						onclick="javascript:closeSessionExpiredDialog();" />
						<div class="divider"></div>
				</div>
				<div class="divider"></div>
			</div>
		</div>
	</div>
</div>
<%
	} catch (Exception e) {
		try {
			Logger logger = Logger.getLogger("sessionTimeoutWarning");
			StringBuffer msg = new StringBuffer(
					"An exception has occurred in the /global/sessionTimeoutWarning.jsp");
			msg.append("exception: " + e);
			System.out.print(msg.toString());
			logger.error(msg.toString());
		} catch (Exception f) {
		}
	}
%>
