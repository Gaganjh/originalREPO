<%@page session="false"%>
<%@ page import="com.manulife.pension.util.BaseEnvironment"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>

<%
	BaseEnvironment environment = new BaseEnvironment();
	String feedbackButtonSwitch = "";
	String madeliaScriptURL = "";
	try{
		feedbackButtonSwitch = environment.getNamingVariable(Constants.MADELIA_FEEDBACK_BUTTON_SWITCH, null);
		madeliaScriptURL = environment.getNamingVariable(Constants.MADELIA_SCRIPT_URL, null);
	}
	catch (Exception e){
	    Logger logger = Logger.getLogger("digitalsurvey.jsp");
        logger.info("Namespace Madeliascript not defined");
	}
    pageContext.setAttribute("feedbackButtonSwitch", feedbackButtonSwitch.toLowerCase(), PageContext.PAGE_SCOPE);
    pageContext.setAttribute("madeliaFeedBackButtonURL", madeliaScriptURL.toLowerCase(), PageContext.PAGE_SCOPE);
%>

<c:if
	test="${(not empty feedbackButtonSwitch ) && (not empty madeliaFeedBackButtonURL)}">
	
	<c:if test="${feedbackButtonSwitch}">
		<script type="text/javascript" 
		src="<%=madeliaScriptURL%>" async>
		</script>
	</c:if>
</c:if>
