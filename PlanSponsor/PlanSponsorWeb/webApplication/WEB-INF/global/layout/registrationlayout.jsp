<%@ page contentType="text/html; charset=utf-8" %>
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

<html>
<head>
<title><content:pageTitle beanName="layoutPageBean" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<c:forEach items="${layoutBean.styleSheets}" var="sheets" >
<link rel="stylesheet" href="${sheets}" type="text/css">
</c:forEach>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>
<script type="text/javascript">
var ie4 = (document.all != null);
var ns4 = (document.layers != null); // not supported
var ns6 = ((document.getElementById) && (navigator.appName.indexOf('Netscape') != -1));
var isMac = (navigator.appVersion.indexOf("Mac") != -1);
</script>

<c:forEach items="${layoutBean.javascripts}" var="scripts" >
<script type="text/javascript" src="${scripts}"></script>
</c:forEach>
<title><content:pageTitle beanName="layoutPageBean" /></title>
</head>


<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="history.go(1);">
  <!-- top nav area -->
  <table width="765" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
	  <jsp:include page="${headerPage}" flush="true" />		
    </td>
  </tr>
  </table>
<br/>
  <table width="765" border="0" cellspacing="0" cellpadding="0" class="fixedTable">
  
	<jsp:include page="${bodyPage}" flush="true" />
	
  </table>
  <!-- footer table -->
  <table cellpadding="0" cellspacing="0" border="0" width="765" class="fixedTable" height="25">
	<jsp:include page="${footerPage}" flush="true" />
  </table>
  <!--// end footer table -->
   <jsp:include page="/WEB-INF/global/digitalsurvey.jsp" flush="true" />
   <jsp:include page="/WEB-INF/global/sessionTimeoutWarning.jsp" flush="true" />
</body>
</html>




