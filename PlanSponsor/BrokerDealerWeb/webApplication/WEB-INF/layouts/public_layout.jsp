<%@page session="false" %>
<%@page contentType="text/html; charset=utf-8" %>

<%@page import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.pagelayout.StyleSheet" %>

<%@taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>

<mrtl:noCaching/>

<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" />
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%
    BDLayoutBean layout = (BDLayoutBean) request
    .getAttribute(BDConstants.LAYOUT_BEAN);
%>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta http-equiv="CACHE-CONTROL" content="NO-CACHE"/>
        <meta http-equiv="PRAGMA" content="NO-CACHE"/>
        <script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>

        <script type="text/javascript"> 
            function doOnload() {
                // This function is not implemented. It will will implement in the body pages 
                // if anything required to be executed during onLoad
            }
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
    <body id="<%=layout.getBodyId()%>" onload="doOnload();">

    <div id="page_wrapper">
        
      <div id="site_header">
            <a href="/do/home/" style="cursor: pointer"><h1>John Hancock: Broker Dealer<img alt="John Hancock logo" src="/assets/unmanaged/images/jh_logo_print.gif" /></h1>
            </a>
           <div id="contactus_link">
              <a href="/do/contactUs/" class="contactus">Contact Us</a>
           </div>
      </div>
        <navigation:menuRenderer/>

        <div id="page_content">
             <jsp:include page="<%=layout.getBody()%>"/>
             <jsp:include page="<%=layout.getFooter()%>"/>
        </div> <!--#page_content-->

        </div> <!--#page_wrapper-->
    
        <div id="page_wrapper_footer">&nbsp;</div> <!--NOTE: To end the page at the bottom-->
        
    </body>
  </html>
