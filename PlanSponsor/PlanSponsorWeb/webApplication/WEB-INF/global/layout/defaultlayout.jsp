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
<content:contentBean contentId="<%=ContentConstants.WARNING_USERS%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningUsers"/>

<html>
<head>
<title><content:pageTitle beanName="layoutPageBean" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">





<c:forEach var="sheets" items="${layoutBean.styleSheets}" >
  <link href=${sheets} rel="stylesheet">
</c:forEach>



<script  type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script> 

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

<c:forEach var="scripts"  items="${layoutBean.javascripts}" >
<script  src=${scripts} language="javascript" type="text/javascript" ></script> 
</c:forEach>

<style>
// Change the background opacity when a modal dialog is shown. it may not work all the time.
// A more guaranteed approach is to set the opacity of the <Panel_ID>_mask element.
.yui-skin-sam .mask {
    opacity: 0;
}
</style>

</head>

<c:if test="${empty param.printFriendly }" >
   <% 
   // this should probably be in a tag, but since nobody else is going to use it....
   Object obj1 = request.getSession(false).getAttribute("displayWarning");
   
   if(obj1 != null && ((Boolean)obj1).equals(new Boolean(true)))
   {
   		request.getSession(false).removeAttribute("displayWarning");  
   %>
   <body class="yui-skin-sam" bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" margin="0" marginwidth="0" marginheight="0" onLoad="alert('<content:getAttribute beanName="warningUsers" attribute="text"/>');doOnload();"> 
   <%
   }
   else
   {%>
	<body class="yui-skin-sam" bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" margin="0" marginwidth="0" marginheight="0" onLoad="doOnload();">
   <%}%>
<%--	<table width="760" border="0" cellspacing=> --%>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
</c:if>


<c:if test="${not empty param.printFriendly }" >
	<body class="yui-skin-sam" bgcolor="#FFFFFF" style="background-color: #FFFFFF;" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>
<tr>
  <td>
    <jsp:include page="${headerPage}" flush="true" />
  </td>
</tr>
</table>

<c:if test="${empty param.printFriendly }" >
<table width="760" border="0" cellspacing="0" cellpadding="0">
</c:if>





<c:if test="${not empty param.printFriendly }" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>
  <tr>
	<td>
	<jsp:include page="${bodyPage}" flush="true" />
    </td>
  </tr>
</table>
<!-- footer table -->
<c:if test="${empty param.printFriendly }" >
<table width="760" border="0" cellspacing="0" cellpadding="0" height="25">
</c:if>

<c:if test="${not empty param.printFriendly }" >
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="25">
</c:if>

    <jsp:include page="${footerPage}"  flush="true" />
</table>
<!--// end footer table -->
 <jsp:include page="/WEB-INF/global/sessionTimeoutWarning.jsp" flush="true" /> 
 <jsp:include page="/WEB-INF/global/digitalsurvey.jsp" flush="true" /> 
</body>
</html>
