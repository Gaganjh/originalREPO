<%-- Prevent the creation of a session --%>
<%@ page session="false" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>


<mrtl:noCaching/>

<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />

<c:set var="bodyPage" value="${layoutBean.bodyPage}"/>
<c:set var="headerPage" value="${layoutBean.headerPage}"/>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request"/>
<html>
<head>
<title><content:pageTitle beanName="layoutPageBean" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<c:forEach  items="${layoutBean.styleSheets}" var="sheets">
	<link rel="stylesheet" href="${sheets}"  type="text/css">
</c:forEach>
<script type="text/JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>

<script type="text/javascript">
var ie4 = (document.all != null);
var ns4 = (document.layers != null); // not supported
var ns6 = ((document.getElementById) && (navigator.appName.indexOf('Netscape') != -1));
var isMac = (navigator.appVersion.indexOf("Mac") != -1);

function doOnload() {
// This function is not implemented. It will will implement in the body pages 
// if anything required to be executed during onLoad
}
</script>
<c:forEach  items="${layoutBean.javascripts}" var="scripts">
	<script language="javascript" type="text/javascript" src="${scripts}"></script>
</c:forEach>
</head>

<c:if test="${empty param.printFriendly}">
   <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" margin="0" marginwidth="0" marginheight="0" onload="doOnload();">
   <table width="350" border="0" cellspacing="0" cellpadding="0">
</c:if>

<c:if test="${not empty param.printFriendly}">
   <body bgcolor="#FFFFFF" style="background-color: #FFFFFF;" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>

<tr>
   <td>
	<jsp:include page="${headerPage}" flush="true" />
   </td>
</tr>

<tr>
   <td>
	<jsp:include page="${bodyPage}" flush="true" />
   </td>
</tr>
</table>
<jsp:include page="/WEB-INF/global/digitalsurvey.jsp" flush="true" />
<jsp:include page="/WEB-INF/global/sessionTimeoutWarning.jsp" flush="true" />
</body>
</html>
