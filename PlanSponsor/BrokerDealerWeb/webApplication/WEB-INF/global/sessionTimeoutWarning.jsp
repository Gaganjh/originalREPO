 <%@ page import="com.manulife.pension.platform.web.authtoken.AuthTokenUtility"%>
 <%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="frw" %> 
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%> 
 <%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
 <%@ page import="org.apache.log4j.Logger"%>
 <%@ page import="java.util.Collections"%>
 <%@ page import="java.util.Enumeration"%>
 <%@ page import="java.util.List"%>
 <%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="bdw" %> 
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
 
 
<bdw:contentBean
	contentId="<%=BDContentConstants.SESSION_EXPIRY_WARNING_MESSAGE%>"
	type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
	beanName="SessionExpiryWarningMessage" />
<bdw:contentBean
	contentId="<%=BDContentConstants.SESSION_EXPIRED_TEXT %>"
	type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
	beanName="SessionExpiredText" /> 
	
 <%

 try {
		// Session Expiry Timeout in minutes.
		int sessionTimeoutMinutes = (session != null) ? session.getMaxInactiveInterval()/60 : 15;
		// Time before session expiration when warning dialog will be displayed
		final int SESSION_EXPIRY_TIME_TO_REACT_MINUTES = 2;
		
		//  URL to logout, same as login since
		String urlToLogout = "/do/logout/";

		// Session Expiry Warning Title, replace with CMA Content
		String sessionExpiryWarningTitle = "Timeout Warning";  
		
		// Session extension error message, replace with CMA content Id
		String sessionManagementErrorMessage = "We are experiencing technical difficulties. Please try login later.";
		
		// Session Expired Title, replace with CMA Content
		String sessionExpiredModalTitle = "Session Expired";
				
		Enumeration<String> attributes = session.getAttributeNames();
				
		List l = Collections.list(attributes);
		
		boolean loggedInd = l.contains("BDLoginValueObject") 
				         || l.contains("PASSCODE_SESSION_KEY") 
				         || l.contains("bd.mimicSession")
				         || l.contains("FRW_SHOW_PASSWORD_METER_IND");
				         
			
						
%>

<style>
/* Change style per style user story */
.sessionWarningModal {
	display: none;
    background: rgba(0, 0, 0, 0.5);
	position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
	height: 100%;
	transform: translate(-50%, -50%);
	overflow: auto;
	z-index: 100000;
  }

@media ( min-width : 992px) {
	.sessionWarningContent {
		background-color: #f3f1f1;
		margin: 7% auto;
		padding: 0px;
		width: 470px;
		height: 135px !important;
		left: 30%;
		top: 30%;
		text-align: left;
		z-index: 100000;
		margin-top: 20%;
		margin-right: 52%;
		margin-left: 27%;
		
	}
}

@media ( max-width : 991px) {
	.sessionWarningContent {
		background-color: #f3f1f1;
		padding: 0px;
		width: auto;
		height: auto;
		left: 30%;
		top: 30%;
		text-align: left;
		z-index: 100000;
		margin-top: 20%;
		margin-right: 52%;
		margin-left: 27%;
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
	margin-top: 20%;
	margin-right: 56%;
	margin-left: 30%;
}

.modal-content {
	margin-top: 0px;
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
	font-size: 11.5px !important;
	padding-bottom: 10px !important;
	padding-left: 20px !important;
	padding-top: 6px !important;
	position: relative;
	display: block;
	border-bottom: 1px solid #eee;
	background-color: #004059;
	font-family: Arial, Helvetica, sans-serif !important;
	color: #FFF !important;
}

.modal-body {
	position: relative;
	padding: $modal-inner-padding;
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
	padding-bottom: 10px !important;
}
.buttonheadernew{
    display: block;
    line-height: 9px;
    background-color: transparent;
    background-image: url(/assets/unmanaged/images/buttons/button_header_both_span_left.gif);
    background-repeat: no-repeat;
    padding-top: 6px;
    padding-right: 8px;
    padding-bottom: 4px;
    padding-left: 12px;
    color: #000000;
    border-radius: 105px;
    width: 150px;
}
.buttonregularnew{
    display: block;
    line-height: 9px;
    background-color: transparent;
    background: transparent url(/assets/unmanaged/images/buttons/button_regular_both_right.gif) no-repeat scroll top right;
    background-repeat: no-repeat;
    padding-top: 6px;
    padding-right: 8px;
    padding-bottom: 4px;
    padding-left: 12px;
    color: #FFF;
    border-radius: 65px;
    width: 110px;
        }
</style>
<script> 
	var sessionTimeoutMinutes = <%= sessionTimeoutMinutes %>; 
	var urlToLogout= '<%= urlToLogout %>';	
	var wcagCompliantTimeToReactMinutes = <%= SESSION_EXPIRY_TIME_TO_REACT_MINUTES %>;  
	var sessionManagementErrorMessage = '<%= sessionManagementErrorMessage %>';  
	var millisecondsPerMinute = 60000;	
	var bufferMilliseconds = 5000;	
	var sessionExpired = false;  
	var sessionExpiryTimeout = null;
	var warningTimeId = null;
	var sessionWarningmilliseconds = null;
	var remainingMinute =  null;
	var remainingSeconds = null;
	var myVar = null;
	var loggedInd = <%= loggedInd %>;
	
	
	if (sessionTimeoutMinutes != undefined && parseInt(sessionTimeoutMinutes) > 0) {
		// if jquery is not already loaded then load the jquery.
		if (typeof jQuery == 'undefined' || window.jQuery.fn.jquery == '1.3.2') {
			var script = document.createElement('script');
			script.src = '/assets/unmanaged/javascript/jquery-1.11.1.min.js';
			document.getElementsByTagName('head')[0].appendChild(script);
			script.onload = function() {
				startTimer();
			};
		} else { 
			startTimer();
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
		if(!loggedInd){
			sessionWarningModal.style.display = "none";
		    document.documentElement.style.overflow = 'scroll';
		    document.body.scroll = "yes";
		    clearTimeout(sessionExpiryTimeout);
	    }else{
	    	sessionWarningModal.style.display = "block";
		    document.documentElement.style.overflow = 'hidden';
		    document.body.scroll = "no";
		    
	    }
		sessionWarningmilliseconds = 1000*Math.round((wcagCompliantTimeToReactMinutes*millisecondsPerMinute)/1000); // round to nearest second
		myVar = setInterval(myTimer, 1000);

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
		sessionExpired = false;
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			try{
			if (this.readyState == 4 && this.status == 200) {
				var parsedData = $.parseJSON(this.responseText);
				var result = parsedData.result;
				var resultValue = result.replace(/^\s+|\s+$/g, "");	
				if (resultValue == 'Y') {
					sessionExpired = false;
				} 	
			}
			}catch(e){}
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
				var parsedData = $.parseJSON(this.responseText);
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
		if(!loggedInd){
			sessionExpiredModal.style.display = "none";
		    document.documentElement.style.overflow = 'scroll';
		    document.body.scroll = "yes";
		  }else{
	    	sessionExpiredModal.style.display = "block";
		    document.documentElement.style.overflow = 'hidden';
		    document.body.scroll = "no";
		    
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
	
	    document.addEventListener('DOMContentLoaded',function(){
	        document.getElementById('continuebtn').addEventListener('click',function(){
	        	extendSession();
	        },false);
	    },false);
	    
	    document.addEventListener('DOMContentLoaded',function(){
	        document.getElementById('logoutbtn').addEventListener('click',function(){
	        	closeSessionExpiredDialog();
	        },false);
	    },false);
	    
	    document.addEventListener('DOMContentLoaded',function(){
	        document.getElementById('okbtn').addEventListener('click',function(){
	        	closeSessionExpiredDialog();
	        },false);
	    },false);

	
</script>

<%-- Session Expiry Warning Modal --%>
<div id="sessionWarningModalDiv" class="sessionWarningModal"
	aria-hidden="true" data-keyboard="false" data-backdrop="static"
	tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="sessionWarningContent">
		<div class="modal-content">
			<div class="modal-body">
				<h3 class="modal-title" style=" font-weight: bold;"><%=sessionExpiryWarningTitle%></h3>
				<div class="row">
					<div style="padding-left: 1%;">
						<div>
							<p id="sessionWarningCountDown" style="padding-left: 18px; font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: normal !important; color: #767676 !important;" > </p>
						</div>
					</div>
				</div>
				<!-- <br /> -->
				<div class="row" style="padding-top: 20px;">
					<div class="modal-footer">
					<div class="button_regular">
					<a id="logoutbtn" href="#" style = "text-decoration:none;">Logout</a>
                    </div>
							&nbsp;&nbsp;
					<div class="button_regular">
					<a id="continuebtn" href="#" style = "text-decoration:none;" >Continue Working</a>
                    </div>
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
		    <div class="modal-body">
			<h3 class="modal-title" style="font-weight: bold;"><%=sessionExpiredModalTitle%></h3>
			<div class="row" style="margin-left: 0px;">
				<div>
				     <div>
				     <p style="padding-left: 21px; font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: normal; color: #767676;">
				<content:getAttribute attribute="text" beanName="SessionExpiredText" />
				     </p>
					</div>
				</div>
			</div>
			<br />
			<div class="row">
				<div class="modal-footer" style="padding-bottom: 10px; width: 85px; padding-left: 260px;">
					<div class="button_regular">
					<a id="okbtn" href="#" style = "text-decoration:none;" onclick="javascript:closeSessionExpiredDialog();">OK</a>
                    </div>	
				</div>
				</div>
			   </div>
			</div>
		</div>
	</div>
</div>
<% } catch (Exception e) {
		try {
				Logger logger = Logger.getLogger("sessionTimeoutWarning");
				StringBuffer msg = new StringBuffer("An exception has occurred in the /global/sessionTimeoutWarning.jsp");
				msg.append("exception: " + e);
				System.out.print(msg.toString());
				logger.error(msg.toString());
		} catch (Exception f) {}
	} 
%> 
