<%@page session="false" %>
<%@page contentType="text/html; charset=utf-8" %>
<%@page import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.pagelayout.StyleSheet" %>

<%@taglib tagdir="/WEB-INF/tags/helps" prefix="helps"%>
<%@taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>

<mrtl:noCaching/>
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" />
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    BDLayoutBean layout = (BDLayoutBean) request.getAttribute(BDConstants.LAYOUT_BEAN);
%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <!-- CMA <%=layout.getContentId()%> -->
    <head>
    <script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>
    
    <script type="text/javascript">
	window.onload = function() {
		$('.message').addClass('messagePenTest').removeClass('message');
		$('.message_error').addClass('message_errorPenTest').removeClass('message_error');
	}
</script>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
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

<c:set var="strPageFooter"> 
    <content:pageFooter beanName="layoutPageBean"/>
</c:set>
<c:set var="strPageFootnotes"> 
    <content:pageFootnotes beanName="layoutPageBean"/>
</c:set>
<c:set var="strPageDisclaimer"> 
    <content:pageDisclaimer beanName="layoutPageBean" index="-1"/>
</c:set>

<title><content:pageTitle beanName="layoutPageBean" /></title>
</head>
<body id="<%=layout.getBodyId()%>" >
<div id="page_wrapper">
    <navigation:siteHeader/>
    <navigation:menuRenderer/>
     <report:formatMessages scope="request"/>
    <div id="page_content" class="background-img">
            <div id="contentOuterWrapper">
                <div id="contentWrapper">
                    <c:if test="${not empty layoutPageBean.introduction1}">
                        <p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
                    </c:if>
                    <c:if test="${not empty layoutPageBean.introduction2}">
                        <p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
                    </c:if>                
                    <c:if test="${not empty layoutPageBean.body1Header}">
                        <p><strong><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></strong></p>
                    </c:if>
                    <c:if test="${not empty layoutPageBean.body1}">
                        <p><content:getAttribute beanName="layoutPageBean" attribute="body1"/></p>
                    </c:if>
                </div><%-- End of contentWrapper --%>
            </div><%-- End of contentOuterWrapper --%>
            <div class="footnotes">
                <c:if test="${not empty strPageFooter}">
                    <p><content:pageFooter beanName="layoutPageBean"/></p>
                </c:if>
                <c:if test="${not empty strPageFootnotes}">
                    <p><content:pageFootnotes beanName="layoutPageBean"/></p>
                </c:if>
                <c:if test="${not empty strPageDisclaimer}">
                    <p><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
                </c:if>             
                <div class="footnotes_footer"></div>
            </div>  <!--#footnotes-->
            <jsp:include page="<%=layout.getFooter()%>"/>
    </div> <!--#page_content-->
</div> <!--#page_wrapper-->
<div id="page_wrapper_footer">&nbsp;</div>
</body>
</html>
