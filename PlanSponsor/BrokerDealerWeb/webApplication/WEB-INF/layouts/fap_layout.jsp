<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.pagelayout.StyleSheet" %>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>



<content:contentBean contentId="<%=BDContentConstants.MESSAGE_CANNOT_PRINT_USING_BROWSER_PRINT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="message"/>

<mrtl:noCaching/>

<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" />
<c:set var="fapLayoutPageBean" value="${layoutBean.layoutPageBean}" scope="session" />


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<% BDLayoutBean layout = (BDLayoutBean) request.getAttribute(BDConstants.LAYOUT_BEAN); %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta http-equiv="CACHE-CONTROL" content="NO-CACHE"/>
        <meta http-equiv="PRAGMA" content="NO-CACHE"/>
       	<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>

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
        
        <title><content:pageTitle beanName="fapLayoutPageBean" /></title>

       <style type="text/css" media="screen">
              .never-show {
                     display: none;
              }
       </style>
       <style type="text/css" media="print">
              .never-print {
                     display: none;
              }
              .always-print {
                     display: table-cell;
              }
       </style>
    </head>
    
    <body id="<%=layout.getBodyId()%>" onload = "javascript:viewFundsBy(this)">
        <div class="always-print never-show">
             <content:getAttribute beanName="message" attribute="text"/>
        </div>
        <div class="never-print">
        <div id="page_wrapper">
            <navigation:siteHeader/>
            <navigation:menuRenderer/>
            <div id="page_content">
                <jsp:include page="/WEB-INF/fap/fapBodyHeader.jsp"/>
                <div id="messages"></div>       

               <% if(layout.getContractReportMenuId() != null) { %>
                <!--Navigation bar -->
                <navigation:contractReportsTab />   
               <% } %>
    

                <jsp:include page="/WEB-INF/fap/fapFilter.jsp"/>
                <div id="fapTabContainer"><script>applyDefaultFilter(); </script> </div>
                <jsp:include page="<%=layout.getFooter()%>"/>
             </div>
         </div>
         <div id="page_wrapper_footer">&nbsp;</div>
         </div>
    </body>
</html>
