<%-- Prevent the creation of a session --%>
<%@ page session="false" %>

<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.platform.web.CommonConstants" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
<!--
function openWin(url) {
	options="toolbar=1,status=1,menubar=1,scrollbars=1,resizable=1,width=800,height=450,left=10,top=10";
	newwindow=window.open(url, "general", options);
	if (navigator.appName=="Netscape") {
		newwindow.focus();
	}
}
//-->
</script>

<c:if test="${empty param.printFriendly }" >
	<div id="page_footer">
		<ul>
			<li><a href="javascript:openWin('https://www.johnhancock.com/accessibility.html');">Accessibility</a></li>
			<li><a href="javascript:openWin('https://www.johnhancock.com/legal.html');">Legal</a></li>
			<li><a href="javascript:openWin('https://www.johnhancock.com/privacysecurity.html');">Privacy &amp; Security</a></li>
			<li><a href="javascript:openWin('https://www.jhannuities.com/marketing/default.aspx');">Annuities</a></li>
			<li><a href="javascript:openWin('https://adviser.jhfunds.com/login.aspx');">JH Funds</a></li>
			<li><a href="javascript:openWin('https://www.jhinvestments.com/529');">College Savings</a></li>
			<li><a href="javascript:openWin('https://www.johnhancock.com');">John Hancock</a></li>
			<li><a href="/do/recommendedSettings/">Recommended Settings</a></li>
		</ul>

		<div id="page_footer_footer">
			<content:contentBean contentId="<%=BDContentConstants.BD_GLOBAL_DISCLOSURE%>"
				type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
				id="globalDisclosure" />
			<p><content:getAttribute beanName="globalDisclosure" attribute="text" /></p>
		</div> <!-- # page_footer_footer -->
	</div><!-- # page_footer -->

</c:if>
<jsp:include page="/WEB-INF/global/sessionTimeoutWarning.jsp" flush="true" />