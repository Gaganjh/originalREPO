<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@tag import="com.manulife.pension.bd.web.myprofile.MyProfileContext"%>
<%@tag import="com.manulife.pension.bd.web.myprofile.MyProfileUtil"%>
<%@tag import="com.manulife.pension.bd.web.myprofile.MyProfileNavigation"%>

<c:set var="navigation" value="<%=MyProfileUtil.getContext(application, request).getNavigation()%>"/>

<div id="page_primary_nav" class="page_nav">
   <ul>
	  <c:forEach var="tab" items="${navigation.tabs}">
	    <c:choose>
	      <c:when test="${tab.id eq navigation.currentTabId}">
	      	<li class="active"><em>${tab.title}</em></li>
	      </c:when>
	      <c:otherwise>
	         <li><a href="${tab.actionURL}">${tab.title}</a></li>
	      </c:otherwise>
	    </c:choose>
	  </c:forEach>		
  </ul>
</div>

<div class="page_nav_footer"></div>