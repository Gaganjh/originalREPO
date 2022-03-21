<%--  Imports  --%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>

<%--  Tag Libraries  --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>

<%
	ContractInformationReportData theReport = (ContractInformationReportData)request.getAttribute(BDConstants.REPORT_BEAN);
  pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
  %>


<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_MONEY_TYPES_AND_SOURCES_SECTION_TITLE%>"
                           	type="<%=BDContentConstants.TYPE_MESSAGE%>"
                          	id="moneyTypesAndSourcesSectionTitle"/>

<h4><content:getAttribute beanName="moneyTypesAndSourcesSectionTitle" attribute="text"/></h4>
<table class="overview_table_alt">
	<thead>
		<tr>
			<th>Code</th>
			<th>Money Type(s)</th>
		</tr>
	</thead>
	<tbody>
<c:forEach items="${theReport.moneyTypes}" var="moneyType">
			<tr>
<td>${moneyType.contractShortName}</td>
<td>${moneyType.contractLongName}</td>
			</tr>
</c:forEach>
		<tr>
			<td colspan="2">
			<hr />
			</td>
		</tr>
		<tr>
			<th>Code</th>
			<th>Money Source(s)</th>
		</tr>
<c:forEach items="${theReport.moneySources}" var="moneySource">
			<tr>
<td>${moneySource.contractShortName}</td>
<td>${moneySource.contractLongName}</td>
			</tr>
</c:forEach>
	</tbody>
</table>
