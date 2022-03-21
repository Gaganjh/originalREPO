<%@page session="false" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.bd.web.home.HomePageHelper" %>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>
<%@ taglib tagdir="/WEB-INF/tags/home" prefix="home" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<script type="text/javascript">

<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}


function doOnload() {
	var username = "<%= request.getAttribute("COOKIE_USER_NAME") %>";
	var rememberMe = "<%= request.getAttribute("REMEMBER_ME_ATT") %>";
	var uidBox=document.getElementById("username_input");
    var passwordBox = document.getElementById("password_input");
 
	if(username && username != "null") {
		document.getElementById("username_input").value = username;
		if(passwordBox) {
    		try {
            	passwordBox.focus();
        	} catch (e) {
        	}
    	}
	}else {
		if(uidBox) {
    		try {
            	uidBox.focus();
        	} catch (e) {
        	}
    	}
	}
	if (rememberMe && rememberMe != "null") {
		document.getElementById("rememberMe_check").checked = "checked";
	}

}
var clicked = false;

function preSubmit() {
	if (!clicked) {
		clicked=true;
		// Add rsa device print
		document.getElementById('devicePrintRSA').value = encode_deviceprint();
		return true;
	} else {
		window.status = "Transaction already in progress ... please wait.";
		return false;
	}
}




//-->
</script>

<content:contentBean contentId="<%=BDContentConstants.PROMOTIONAL_INFO_CONTENT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="promotionalInfo" />
<content:contentBean contentId="<%=BDContentConstants.REMEMBER_USERNAME_CHECKBOX_LABEL %>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="rememberMeLabel" />

<%
    // move the possible errors from session into request
    BDSessionHelper.moveMessageIntoRequest(request);
%>

<layout:pageHeader nameStyle="h1"/>

<report:formatMessages scope="request"/>

<div class="page_section" id="promo_actions_access">
    <c:choose>
        <c:when test="${not empty promotionalInfo.text}">
            <div class="page_module" id="flash_promo">
                ${promotionalInfo.text}
            </div>
        </c:when>
        <c:otherwise>
            <div class="page_module" id="news_updates" style="margin-right:9px">
                <home:newsEvents/>
            </div>      
        </c:otherwise>
    </c:choose>     
    
    <!-- I Want To Section -->
    <div class="page_module" id="user_actions">
        <h2><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></h2>
        <p><content:getAttribute attribute="body1" beanName="layoutPageBean"/></p>
    </div>

    <div class="page_module" id="user_access">
        <h2><content:getAttribute attribute="body2Header" beanName="layoutPageBean"/></h2>
        <p><content:getAttribute attribute="body2" beanName="layoutPageBean"/></p>
        <form action="/login/loginServlet" id="account_login" method="post" onSubmit="return preSubmit();">
            <fieldset>
                <p><label for="username_input">Username:</label></p>
                <input type="text" id="username_input" name="userName"  maxlength="20" autocomplete="off" tabindex="1"/>
                <p><label for="password_input">Password:</label></p>
                <input type="password" id="password_input" name="password" maxlength="64" tabindex="2"/>
            </fieldset>
            <p class="forgot_password_register"><a href="/do/forgetPassword/step1">Forgot Password?</a></p>
            <p class="forgot_password_register" style="padding-right:2px;vertical-align:middle;">
				<input type="checkbox" id="rememberMe_check" name="rememberMe" tabindex="3"/>
				<label for="rememberMer_check"><content:getAttribute attribute="text" beanName="rememberMeLabel"/></label>							
			</p>
			<input id="devicePrintRSA" type="hidden"  name="devicePrintRSA">
            <div class="button_login"><input type="submit" value="Login" tabindex="4"/></div>
            <c:if test="${param.nextURL ne null}">
	            <input id="nextURL" type="hidden"  name="nextURL" value=<%= request.getParameter("nextURL") %>>
	        </c:if>
			<content:errors scope="request" />
        </form>                                 
    </div>
    
    <div class="clear_footer"></div>                
</div>
    
<div class="page_section" id="news_events_search">
  <div id="outerWrapper">
    <div id="contentWrapper">
        <div id="rightColumn1">
       		<c:if test="${param.nextURL eq null}"> 
	            <div class="page_module_register">
	                <a href="/do/registerExternalBroker/start" onmouseout="MM_swapImgRestore()" 
	                    onmouseover="MM_swapImage('Register','','/assets/unmanaged/images/register_on.gif',1)">
	                <img src="/assets/unmanaged/images/register_off.gif" alt="Register" name="Register" width="233" height="80" border="0" id="Register" /></a>
	            </div>
	        </c:if>
            <c:if test="${not empty layoutPageBean.layer1}">    
                <div class="page_module" id="market_commentary">
                    <home:ourInvestmentStory layer="${layoutPageBean.layer1}" isPublicHome="true"/>
                </div>
            </c:if>             
        </div>
        <c:if test="${not empty promotionalInfo.text}">
            <div id="leftColumn1">
                <div class="page_module" id="news_updates">
                    <home:newsEvents/>
                </div>
            </div>
        </c:if>
   
    <div id="middle">
        <div class="page_module" id="market_commentary">
            <h2><content:getAttribute attribute="body3Header" beanName="layoutPageBean"/> </h2>
            <%=HomePageHelper.MARKET_WATCH_INFO%>
        </div>
    </div>
    <br class="clearFloat" />
    <script>
		
        var flashVer = "";
        try {
            flashVer = GetSwfVer();
        } catch (e) {
        }
        try {            
            var wwwUsageText = "SCRNRES=" + screen.width + "x" + screen.height + "&SCRNAVL=" + screen.availWidth + "x" + screen.availHeight + "&SCRNCLRS=" + screen.colorDepth + "&SCRNPXLS=" + screen.pixelDepth + "&JAVASUP=" + navigator.javaEnabled() + "&COOKIES=" + navigator.cookieEnabled + "&FLASHVER=" + flashVer;
            document.write("<img src='/assets/unmanaged/images/WebTrendsTracker.gif?" + wwwUsageText + "' width='1' height='1'>");
        } catch (e) {
        }
    </script>    
    </div>
  </div>
</div>
<layout:pageFooter/>
