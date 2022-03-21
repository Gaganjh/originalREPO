<%--  Imports  --%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%--  Tag Libraries  --%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>


<%
	ContractInformationReportData theReport = (ContractInformationReportData)request.getAttribute(BDConstants.REPORT_BEAN);
  pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
  %>


<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_KEY_DATES_SECTION_TITLE%>"
                           	type="<%=BDContentConstants.TYPE_MESSAGE%>"
                          	id="keyDatesSectionTitle"/>

<h4><content:getAttribute beanName="keyDatesSectionTitle" attribute="text"/></h4>
<table class="overview_table">
	<tbody>
		<tr>
			<th>Contract Effective:</th>
			<td>
			    <render:date property="theReport.contractProfileVo.contractEffectiveDate" defaultValue="" />
            </td>
		</tr>
		<tr>
			<th>Plan Year End:</th>
			<td>
			    <render:date property="theReport.contractProfileVo.contractYearEndDate" defaultValue="" patternOut="MMMM dd" />
            </td>
		</tr>
<c:if test="${theReport.contractSummaryVo.isContractHasGAFunds ==true}">
<c:if test="${not empty theReport.contractProfileVo.guaranteedAccountTransferDates}">
				<tr>
					<th>Guaranteed Acct. Transfer**:</th>
					<td>
<c:forEach items="${theReport.contractProfileVo.guaranteedAccountTransferDates}" var="transferDate">

						       <render:date property="transferDate" defaultValue="" patternOut="MMMM dd" /><br/>
</c:forEach>
	                </td>
				</tr>
</c:if>
</c:if>
	</tbody>
</table>
