<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<ps:form action="/do/messagecenter/history" method="post" modelAttribute="messageHistoryForm" name="messageHistoryForm">

<jsp:include page="history_filter.jsp"/>

<br>
<br>

<jsp:include page="history_report.jsp"/>

</ps:form>

<c:if test="${not empty param.printFriendly}">
<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
       type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
       id="globalDisclosure"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<content:errors scope="session"/>
	<tr>
		<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
	</tr>
</table>
 </c:if>

<script language="javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<jsp:include page="addtional_details.jsp" flush="true"/>
