<%@ attribute name="layer" type="com.manulife.pension.content.view.MutableLayer" rtexprvalue="true" required="true"%>
<%@ attribute name="isPublicHome" type="java.lang.Boolean" rtexprvalue="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ tag import="com.manulife.pension.bd.web.util.JspHelper"%>
<%
 try {
%>

<c:choose>
	<c:when test="${isPublicHome}">
		<h2>${layer.title }</h2>
	</c:when>
	<c:otherwise>
		<h4>${layer.title }</h4>
	</c:otherwise>
</c:choose>
<ul>
	<li>${layer.text }</li>
</ul>
<div class="clear_footer"></div>

<%
 } catch (Exception e) {
	 JspHelper.log("ourInvestmentStory.tag", e, "fails");
 }
%>