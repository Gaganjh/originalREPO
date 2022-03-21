<%@ tag
	import="com.manulife.pension.service.message.valueobject.MessageCenterComponent"
	import="com.manulife.pension.ps.web.messagecenter.model.MCDetailTabReportModel"
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="sectionPref"
	type="com.manulife.pension.ps.web.messagecenter.model.MCSectionPreference"
	required="true" rtexprvalue="true"%>

<%@ attribute name="name"
	type="java.lang.String"
	required="true" rtexprvalue="true"%>

<c:if test="${sectionPref.primarySortAttribute == name }">
   <c:choose>
      <c:when test="${sectionPref.ascending}">
         <img src="/assets/unmanaged/images/arrow_triangle_up.gif">
      </c:when>
      <c:otherwise>
      	<img src="/assets/unmanaged/images/arrow_triangle_down.gif">
      </c:otherwise>
   </c:choose> 
</c:if> 
