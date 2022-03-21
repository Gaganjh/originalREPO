<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="reporttag" tagdir="/WEB-INF/tags/report"%>
        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>

<mrtl:noCaching/>
<%-- If there are any errors/warnings/Informations to be displayed--%>
<c:if test='${fapForm.messagesExist}'>
	{
		"errors": [
			{
				"messages" : "<reporttag:fandpReportErrors><reporttag:reportErrors/></reporttag:fandpReportErrors>"
			}
		]

		<c:if test='${fapForm.filterResultsId != null}'>
			,
		</c:if>
	<c:if test='${fapForm.filterResultsId == null}'>
		}
	</c:if>
</c:if>

<c:if test='${fapForm.warningsExist}'>
	{
		"warnings": [
			{
				"messages" : "<report:formatMessages scope="request"/>"
			}
		]

		<c:if test='${fapForm.filterResultsId != null}'>
			,
		</c:if>
	<c:if test='${fapForm.filterResultsId == null}'>
		}
	</c:if>
</c:if>

<c:choose>

	<c:when test="${fapForm.filterResultsId =='toggleURL'}"> 
		{
			"toggle" : [
				{
					"URL" 	: "/do/fap/${fapForm.selectedCompanyName}"
				}
			]
		}
	</c:when>
	
	<c:when test="${fapForm.filterResultsId =='reloadPage'}"> 
		{
			"reload" : "true"
		}
	</c:when>

	<c:when test="${fapForm.filterResultsId =='sessionExpired'}"> 
		{
			"sessionExpired" : "true"
		}
	</c:when>

	<c:when test="${fapForm.filterResultsId =='fundInfoAndSelectQueryOptions'}"> 
		{
			"baseFundArray" : [
				<c:forEach var="fund" items="${fundValueObject}"  varStatus="fundListStatus">
					{ 
						"fundId" 			: "${fund.fundId}",
						"fundName" 			: "${fund.fundNameWithoutFootNotes}",
						"managerName"		: "${fund.managerName}",
						"assetClass"		: "${fund.assetClass}"
					}
					<c:if test="${!fundListStatus.last}">
							,
					</c:if> 
				</c:forEach>
			],

			"optionalFilterSelect" : [
				<c:forEach var="optionalFilterBean" items="${fapForm.optionalFilterList}" varStatus="optionalFilterListStatus">
					{
						"label" : "${optionalFilterBean.label}",
						"value" : "${optionalFilterBean.value}"
						}
						 <c:if test="${!optionalFilterListStatus.last}">
						,
					</c:if> 
				</c:forEach>
			]
		}
	</c:when>

	<c:when test="${fapForm.filterResultsId =='innerSelectOptions'}"> 
		{
			"optionsList" : [
				<c:forEach var="innerOptions" items="${fapForm.advanceFilterInnerOptionList}" varStatus="advanceFilterInnerOptionListStatus">
					{
						"label" : "${innerOptions.label}",
						"value" : "${innerOptions.value}"
					}
					<c:if test="${!advanceFilterInnerOptionListStatus.last}">
						,
					</c:if> 
				</c:forEach>
			]
		}
	</c:when>

	<c:when test="${fapForm.filterResultsId =='shortListinnerSelectOptions'}"> 
		{
			"optionsList" : {
				<c:forEach var="innerOptions" items="${fapForm.shortListOptionList}" varStatus="shortListOptionListStatus">
					'${innerOptions.key}" :[
					
					<c:forEach var="element" items="${innerOptions.value}" varStatus="valueStatus">
						{
							"label" : "${element.label}",
							"value" : "${element.value}"
							} 
							<c:if test="${!valueStatus.last}">
							,
						</c:if>
					</c:forEach>
					]
					<c:if test="${!shortListOptionListStatus.last}">
						,
					</c:if>
				</c:forEach>
			}
		}
	</c:when>

	<c:when test="${fapForm.filterResultsId =='fundIds'}">
		<c:if test='${!fapForm.warningsExist}'>
		 {
		</c:if> 
			"filterFundIds" : [	
				<c:forEach var="fundId" items="${fapForm.filteredFundIds}" varStatus="filteredFundIdsStatus">
					{
						"fundId" : "${fundId}"
					}
					<c:if test="${!filteredFundIdsStatus.last}">
						,
					</c:if> 
				</c:forEach>
			]
		}
	</c:when>

   <c:when test="${fapForm.filterResultsId =='reports'}"> 
		{
			<c:choose>
				<c:when test="${!empty fapForm.reportList}">
					"reportList" : [
						<c:forEach var="report" items="${fapForm.reportList}" varStatus="reportListStatus">
							{
								"key" 	: "${report.key}",
								"value" : "${report.value}"
							}
							<c:if test="${!reportListStatus.last}">
								,
							</c:if> 
						</c:forEach>
					]
				</c:when>
				<c:otherwise>
					"reportList" : "No results"
				</c:otherwise>
			</c:choose>
		}
	</c:when>
	
	<c:when test="${fapForm.filterResultsId =='sloshBoxSortingResults'}"> 
		{
			"queryResultsValues" : [
			<c:forEach var="fundId" items="${fapForm.sortedQueryResultsValues}" varStatus="sortedQueryResultsValuesStatus">
				{
					"fundId" : "${fundId}"
				}
				<c:if test="${!sortedQueryResultsValuesStatus.last}">
					,
				</c:if> 
			</c:forEach>
			],
			
			"selectedFundsValues" : [
				<c:forEach var="fundId" items="${fapForm.sortedSelectedFundsValues}" varStatus="sortedSelectedFundsValuesStatus">
				{
					"fundId" : "${fundId}"
				}
				<c:if test="${!sortedSelectedFundsValuesStatus.last}">
					,
				</c:if> 
			</c:forEach>
			]
		}
	</c:when>
	
	<c:when test="${fapForm.filterResultsId =='showAlert'}"> 
		{
			"alertMessage" : "${fapForm.alertMessage}"
		}
	</c:when>
    
    	<c:when test="${fapForm.filterResultsId =='showConfirmation'}"> 
		{
			"confirmationMessage" : "${fapForm.alertMessage}"
		}
	</c:when>
</c:choose>
