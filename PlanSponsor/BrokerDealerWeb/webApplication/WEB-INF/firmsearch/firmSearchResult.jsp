<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.bd.web.content.ContentRuleMaintenanceForm" %>
        

	{
		"ResultSet" : {
			"Result" : [
				<c:forEach var="firm" items="${contentRuleMaintenanceForm.matchingFirms}" varStatus="status">
					{ 
"firmId" : ${firm.id},
"firmName" : "${firm.firmName}" 
}


 <c:if test="${!status.last}">


					  ,
					</c:if> 
				</c:forEach>
				]
		}
	}
