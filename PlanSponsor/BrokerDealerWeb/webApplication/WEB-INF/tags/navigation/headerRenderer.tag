<%@ tag body-content="empty" import="java.util.ArrayList"
	import="java.util.Collections"
	import="com.manulife.pension.bd.web.BDConstants"
	import="com.manulife.pension.bd.web.navigation.NavigationHeader"
	import="com.manulife.pension.bd.web.navigation.UserMenuItem"
	import="com.manulife.pension.bd.web.navigation.UserNavigationFactory"
	import="com.manulife.pension.platform.web.CommonConstants"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	NavigationHeader navHeader = UserNavigationFactory.getInstance(
			application).getUserNavigaion(request, application)
			.getNavigationHeader();
	jspContext.setAttribute("navHeader", navHeader);
	String updatepasswordInd = null;
	if(null != request.getSession(false) && 
			null != request.getSession(false).getAttribute("isUpdatePasswordSuccessInd")){
	   updatepasswordInd = (String) request.getSession(false).getAttribute("isUpdatePasswordSuccessInd");
	}
	HttpSession sessions = request.getSession(false);
	String updatepasswordsuccessInd = null;
	if(sessions != null){
	if(updatepasswordInd != null ){
		updatepasswordsuccessInd = updatepasswordInd;	
	}
	else{
		updatepasswordsuccessInd = "false";
	}
	}
	else{
		updatepasswordsuccessInd = "false";
	}
	String referrer = null; 
	if(request.getHeader("referer")!=null){
	referrer = request.getHeader("referer");
	} 
	String refereInd = null;
	if(null!= referrer && referrer.contains("/updatePassword/") ){
		refereInd="true";
	}
	else{
		refereInd="false";
	} 
%>
<c:if test="${not empty navHeader}">
	 <% if (refereInd !="true" || updatepasswordsuccessInd == "true") {%>
	<ul>
	<c:forEach var="link" items="${navHeader.links}">
		<li>
		<c:choose>
		  <c:when test="${not empty link.actionURL}">
				<a href="${link.actionURL}">${link.title}</a>
		  </c:when>
		  <c:otherwise>
		    <c:choose>
		     <c:when test="${link.cmaKey != 0}">
		        ${link.htmlContent}
		     </c:when>
		     <c:otherwise>
		  		${link.title}
		  	 </c:otherwise>
		   	</c:choose>
		  </c:otherwise>
		</c:choose>
		</li>
	</c:forEach>
	</ul>
<%} %>
	<div id="secure_header_user">
	<c:choose>
	 <c:when test="${not empty navHeader.roleText}">
	 <p>${navHeader.welcomeText }</p><p style="margine-top:-3px"> <strong>${navHeader.userNameText}, <br>${navHeader.roleText}</strong>
	 </c:when>
	 <c:otherwise>
		<p>${navHeader.welcomeText }</p><p> <strong>${navHeader.userNameText}</strong>	 
	 </c:otherwise>
	</c:choose>
	<div class="button_regular">
	<a id="logout" href="${navHeader.exitMenu.actionURL}">${navHeader.exitMenu.title}</a>
	</div>
	</div>

</c:if>
