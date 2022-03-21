<%@ page import="com.manulife.pension.util.BaseEnvironment" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="org.apache.log4j.Logger"%>
<%
	BaseEnvironment environment = new BaseEnvironment();
	String webAnalyticsReportSuites = "";
	try{
		webAnalyticsReportSuites = environment.getNamingVariable(BDConstants.OMNITURE_WEB_ANALYTICS_CAPTURE_DESTINATION, null);
	}
	catch (Exception e){
	    Logger logger = Logger.getLogger("omnitureWebAnalytics.jsp");
        logger.info("Namespace webAnalyticsDestination not defined");
	}
    pageContext.setAttribute("webAnalyticsDestination", webAnalyticsReportSuites.toLowerCase(), PageContext.PAGE_SCOPE);
%>
<input type="hidden" value="<%=webAnalyticsReportSuites%>" id="webAnalyticsDestination" />
<script type="text/javascript">
    function getWebAnalyticsDestination(){
    	var wad = document.getElementById("webAnalyticsDestination").value  || '';
        return wad;
    }
</script>
<c:if test="${not empty webAnalyticsDestination}">
	<c:if test="${not empty webAnalyticsDestination}">  
		<!-- SiteCatalyst code version: H.17.
	Copyright 1997-2008 Omniture, Inc. More info available at
	http://www.omniture.com -->
		<script language="JavaScript" type="text/javascript"
			src="/assets/unmanaged/javascript/s_code.js"></script>
		<script language="JavaScript" type="text/javascript">
		<!--
			/* You may give each page an identifying name, server, and channel on
			the next lines. */
			s.pageName = document.title
			s.server = "Financial Representative"
			s.channel = ""
			s.pageType = ""
			s.referrer = ""
			s.prop1 = "" 
			s.prop2 = ""
			s.prop3 = ""
			s.prop4 = ""
			s.prop5 = ""
			s.prop6 = ""
			s.prop7 = ""
			s.prop8 = ""
			s.prop9 = ""
			s.prop10 = ""
			/* Conversion Variables */
			s.campaign = ""
			s.state = ""
			s.zip = ""
			s.events = ""
			s.products = ""
			s.purchaseID = ""
			s.eVar1 = ""
			s.eVar2 = ""
			s.eVar3 = ""
			s.eVar4 = ""
			s.eVar5 = ""
			s.eVar6 = ""
			s.eVar7 = ""
			s.eVar8 = ""
			s.eVar9 = ""
			s.eVar10 = ""
			/************* DO NOT ALTER ANYTHING BELOW THIS LINE ! **************/
			var s_code = s.t();
			if (s_code)
				document.write(s_code)
		//-->
		</script>
		<script language="JavaScript" type="text/javascript">
		<!--
			if (navigator.appVersion.indexOf('MSIE') >= 0)
				document.write(unescape('%3C') + '\!-' + '-')
		//-->
		</script>
		<!--/DO NOT REMOVE/-->
		<!-- End SiteCatalyst code version: H.17. -->
</c:if>
</c:if>
