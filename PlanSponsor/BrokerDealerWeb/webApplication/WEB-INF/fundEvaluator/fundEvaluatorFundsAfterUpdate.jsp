<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.owasp.encoder.Encode"%>
{	
	"assetClasses" : [
		<c:forEach var="assetClass" items="${fundEvaluatorForm.assetClassDetails}" varStatus="assetClassStatus" >
			<c:set var="assetClassInfo" value="${assetClass.value}" />
				{
					"assetClassId" : "${assetClassInfo.assetClassId}",
					"selectedFunds" : "${assetClassInfo.totalFundsSelected}" }
					<c:if test="${!assetClassStatus.last}">
						,
					</c:if> 
		</c:forEach>
	],
	"assetClassId": "${fundEvaluatorForm.assetClassId}",
	"totalSelectedFunds":"${fundEvaluatorForm.totalSelectedFunds}",
	"selectedAnySVF": "${fundEvaluatorForm.selectedAnySVF}",
	"selectedAnySVFCompeting": "${fundEvaluatorForm.selectedAnySVFCompeting}"	
} 