<%@page session="false" %>
<%@page contentType="text/html; charset=utf-8" %>

<%@page import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.pagelayout.StyleSheet" %>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>
<%@page import="com.manulife.pension.bd.web.ApplicationHelper"%>
<%@page import="com.manulife.pension.bd.web.controller.SecurityManager" %>
<%@page import="com.manulife.pension.bd.web.controller.AuthorizationSubject" %>
<%@page import="com.manulife.pension.bd.web.navigation.URLConstants" %>

<%@taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mrtl:noCaching/>

<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" />
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%
    BDLayoutBean layout = (BDLayoutBean) request.getAttribute(BDConstants.LAYOUT_BEAN);
	pageContext.setAttribute("layout",layout,PageContext.PAGE_SCOPE);
	// changes for defect 8610
	AuthorizationSubject subject = ApplicationHelper
            .getAuthorizationSubject((HttpServletRequest) request);
    SecurityManager securityManager = ApplicationHelper
            .getSecurityManager(application);
    boolean isContactUsLink = false;
    if (securityManager.isUserAuthorized(subject,
            URLConstants.ContactUs)) {
        isContactUsLink = true;
    }
    // end changes for defect 8610
%>


<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta http-equiv="CACHE-CONTROL" content="NO-CACHE"/>
        <meta http-equiv="PRAGMA" content="NO-CACHE"/>
        
       	<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>

        <script type="text/javascript"> 
        window.onload=function() {
            document.getElementById('logout_link').style.display = 'none';
            var element = document.getElementById('logout_link');
            if (typeof (element) != null && typeof (element) != 'undefined') {
            var location = window.location.pathname;
            var logoutid = document.getElementById('logout_link')
            logoutid.style.display = "none";
            if(location == '/do/updatePassword/'){
           	 logoutid.style.display = "block";
            }else{
           	 logoutid.style.display = "none";
            } 
            }
        };
        
        </script>
        
        <%if (layout.getJavascripts() != null) {
            for (String javaScriptV : layout.getJavascripts()) {
        %>
            <script type="text/javascript" src="<%=javaScriptV %>"></script>
        <%  }
          }
        %>
        <%if(layout.getStylesheets() != null) {
            for (StyleSheet styleSheet: layout.getStylesheets()) {
        %>  
            <link rel="stylesheet" type="text/css" 
                <%if (styleSheet.getMedia() != null) { %>
                    media="<%=styleSheet.getMedia()%>" 
                <%}%> href="<%=styleSheet.getHref()%>" />
        <%  }
          }
        %>
        <!--[if lte IE 6]><link rel="stylesheet" type="text/css" media="screen" href="/assets/unmanaged/stylesheet_nbdw/ie6.css" /><![endif]-->
        <!--[if gte IE 7]><link rel="stylesheet" type="text/css" media="screen" href="/assets/unmanaged/stylesheet_nbdw/ie7.css" /><![endif]-->
            
        <title><content:pageTitle beanName="layoutPageBean" /></title>
    </head>
    <body>
        <div id="outerWrapper">
            <div id="site_header">
                <h1>John Hancock: Broker Dealer<img alt="John Hancock logo" src="/assets/unmanaged/images/jh_logo_print.gif" /></h1>
                <div id="logout_link" style="padding-top: 25px;display:none;">
                <%if(isContactUsLink){%>
                <a id="contactus" href="/do/contactUs/" style="color: white;font-size: 11px;padding-left: 575px;">Contact Us |</a>
                <%}%>
                <a id="logout" href="/do/logout/" style="color: white;margin-top: -13px;padding-left: 70.5%;font-weight: normal;">Logout</a>
                </div>
            </div>
            <div id="topNavigationSub"></div>
            
            <div id="contentWrapper">
                 <jsp:include page="<%=layout.getBody()%>"/>
             <br class="clearFloat"/>
            </div>
            <jsp:include page="<%=layout.getFooter()%>"/>
        </div>
        <div id="outerFooterWrapper">&nbsp;</div>
     </body> 
</html>
