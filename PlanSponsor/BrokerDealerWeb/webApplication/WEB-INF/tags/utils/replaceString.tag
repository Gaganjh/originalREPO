<%@ attribute name="from" type="java.lang.String" rtexprvalue="true" required="true" %>
<%@ attribute name="to" type="java.lang.String" rtexprvalue="true" required="true" %>
<%@ attribute name="val" type="java.lang.String" rtexprvalue="true" required="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<% %>

<c:choose>
	<c:when test="${val == from}">
		${to}
	</c:when>
	<c:otherwise>
		${val}
	</c:otherwise>
</c:choose>