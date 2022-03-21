<%-- Prevent the creation of a session --%>
<%@ page session="false" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>

<mrtl:noCaching/>
 
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />

<c:set var="headerPage" value="${layoutBean.headerPage}" scope="page" />
<c:set var="bodyPage" value="${layoutBean.bodyPage}" scope="page" />
<c:set var="footerPage" value="${layoutBean.footerPage}" scope="page" />

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<content:contentBean contentId="<%=ContentConstants.WARNING_SECONDARY_USERS%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningSecondaryUsers"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_USERS%>"
				type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningUsers"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_CANNOT_PRINT_USING_BROWSER_PRINT%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="message"/>

<c:set var="indentSize" value="${layoutBean.getParam('bodyIndentSize')}" />




<html>
<head>
<title><content:pageTitle beanName="layoutPageBean" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">


<c:forEach items="${layoutBean.styleSheets}" var="sheets" >
 <link href=${sheets} rel="stylesheet">
</c:forEach>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>
<script language="javascript">
var ie4 = (document.all != null);
var ns4 = (document.layers != null); // not supported
var ns6 = ((document.getElementById) && (navigator.appName.indexOf('Netscape') != -1));
var isMac = (navigator.appVersion.indexOf("Mac") != -1);

function doOnload() {
// This function is not implemented. It will will implement in the body pages 
// if anything required to be executed during onLoad
}

</script>

<c:forEach items="${layoutBean.javascripts}" var="scripts" >
<script language="javascript" type="text/javascript" src=${scripts}></script>
</c:forEach>

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

<c:if test="${empty param.printFriendly}" >
   <% 
   // this should probably be in a tag, but since nobody else is going to use it....
   //LS - CL 50355 remove registration message , have to remove attribute here 
   //Object obj = request.getSession(false).getAttribute("registered");
   Object obj1 = request.getSession(false).getAttribute("displayWarning");

   if( obj1 != null && ((Boolean)obj1).equals(new Boolean(true)))
   {  
     request.getSession(false).removeAttribute("displayWarning"); 
   %>
    <body class="yui-skin-sam"  bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="alert('<content:getAttribute beanName="warningUsers" attribute="text"/>');"> 
   <%
   }
   else
   {%>
	<body class="yui-skin-sam" bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="doOnload();"> 
  <%}%>
<c:if test="${not empty layoutBean.getParam('printNotAllowed')}">
  <div class="always-print never-show">
             <content:getAttribute beanName="message" attribute="text"/>
  </div>
  <div class="never-print">
</c:if>
 <table width="760" border="0" cellspacing="0" cellpadding="0">
</c:if>


<c:if test="${not empty param.printFriendly }" >
<body bgcolor="#FFFFFF" style="background-color: #FFFFFF;" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<c:if test="${not empty layoutBean.getParam('printNotAllowed')}">
  <div class="always-print never-show">
             <content:getAttribute beanName="message" attribute="text"/>
  </div>
 <div class="never-print">
</c:if>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>
 <tr>
   <td>
    <jsp:include page="${headerPage}" flush="true" />  
   </td>
 </tr>
</table>

<%-- body table --%>
<c:if test="${empty param.printFriendly }" >
<table width="760" border="0" cellspacing="0" cellpadding="0">
</c:if>

<c:if test="${not empty param.printFriendly }" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>

 	<tr>
  		<td width="${indentSize}">&nbsp;</td>
  		
<c:if test="${empty param.printFriendly }" >
  		<td width="715">
</c:if>

<c:if test="${not empty param.printFriendly }" >
  		<td width="670">
</c:if>
   			<jsp:include page="${bodyPage}" flush="true" />

<c:if test="${not empty layoutBean.getParam('includeDisclaimer')}">
<c:set var="disclaimerJsp" value="${layoutBean.getParam('includeDisclaimer')}" />



                    <jsp:include page="${disclaimerJsp}" flush="true"/>
</c:if>
  		</td>
 	</tr>

</table>
<%-- end body table --%>

<!-- footer table -->
<c:if test="${empty param.printFriendly}" >
<table width="760" border="0" cellspacing="0" cellpadding="0" height="25">
</c:if>

<c:if test="${not empty param.printFriendly}" >
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="25">
</c:if>

    <jsp:include page="${footerPage}"  flush="true" />
</table>
<!--// end footer table -->

<c:if test="${not empty layoutBean.getParam('printNotAllowed')}">
</div>
</c:if>
 <jsp:include page="/WEB-INF/global/digitalsurvey.jsp" flush="true" />
 <jsp:include page="/WEB-INF/global/sessionTimeoutWarning.jsp" flush="true" />
</body>
</html>
