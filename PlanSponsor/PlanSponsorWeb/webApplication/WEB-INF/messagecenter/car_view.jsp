<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<ps:form name="carViewForm" modelAttribute="carViewForm" action="/do/mcCarView" method="post">
<c:set scope="request" var="visitedMsgColor" value="gray"/>
<c:set scope="request" var="newMsgColor" value="black"/>

<c:set scope="request" var="visitedActIcon" value="JH-act-now-in-progress-grey.png"/>
<c:set scope="request" var="newActIcon" value="JH-act-now-grey.png"/>

<jsp:include page="car_view_filter.jsp"/>

<br>
<br>

<jsp:include page="car_view_report.jsp"/>

</ps:form>

<c:if test="${not empty param.printFriendly}">
<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
       type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
       id="globalDisclosure"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
	</tr>
</table>
 </c:if>

<script language="javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>

<jsp:include page="addtional_details.jsp" flush="true"/>
