<%@ page session="false" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<c:set var="layoutBean" value="${layoutBean}" scope ="request"/>
<c:set var="headerPage" value="${layoutBean.getParam('headerPage')}" scope="page"/> 
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<html>
<head>
<title><content:pageTitle beanName="layoutPageBean" /></title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">


<c:forEach items="${layoutBean.styleSheets}" var="sheets" >
<link rel="stylesheet" href='${sheets}' type="text/css">
</c:forEach>
<script type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>

<script type="text/javascript" >
var ie4 = (document.all != null);
var ns4 = (document.layers != null); // not supported
var ns6 = ((document.getElementById) && (navigator.appName.indexOf('Netscape') != -1));
var isMac = (navigator.appVersion.indexOf("Mac") != -1);

</script>

<c:forEach items="${layoutBean.javascripts}" var="scripts" >
<script type="text/javascript" src='${scripts}'></script>
</c:forEach>
<jsp:include page="/WEB-INF/global/digitalsurvey.jsp" flush="true" />
</head>



<body bgcolor="#FFFFFF" style="background-color: #FFFFFF;" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="765" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td>
	      <jsp:include page="headerPage" flush="true" />  
        </td>
    </tr>
    <tr>
        <td>
           &nbsp;&nbsp;System Error occurred.<br>
			&nbsp;&nbsp;Please try again. If you are still experiencing the same problem
			please call your client account representative. <b>[<%= request.getAttribute("errorCode") %> - <%= request.getAttribute("uniqueErrorId") %>]
        </td>
    </tr>
	<tr>
		<td>&nbsp;&nbsp;<a href="javascript:window.close();">close</a></td>
	</tr>
</table>

</body>

</html>
