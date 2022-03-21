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
	BDLayoutBean layout = (BDLayoutBean) request.getAttribute(BDConstants.LAYOUT_BEAN);
pageContext.setAttribute("layout",layout,PageContext.PAGE_SCOPE);
%>


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
		<%	}
		  }
		%>
			
		<title><content:pageTitle beanName="layoutPageBean" /></title>
	</head>
	
	<body id="<%=layout.getBodyId()%>">
		<jsp:include page="<%=layout.getBody()%>"/>
	</body>
</html>
