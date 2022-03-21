<%-- Imports used --%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.InvestmentOptionVO"%>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantFundSummaryVO"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%-- Tag Libraries used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render" prefix="render"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>


<%-- Constant Files used--%>
<un:useConstants var="bdConstants" className="com.manulife.pension.bd.web.BDConstants" />


<%-- Beans used --%>
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>


<input type="hidden" name="pdfCapped" /><%--  input - name="participantAccountForm" --%>


<%-- Include common --%>
<jsp:include page="/WEB-INF/participant/definedBenefitAccountCommon.jsp" flush="true" />
<c:if test="${requestScope.penErrors==true}">
	<report:formatMessages scope="request"/>
</c:if>
<%-- 1.0 Page Filter/Sort section --%>
<div class="page_section_subheader controls">
<h3><content:getAttribute beanName="layoutPageBean" attribute="body2Header" /></h3>
<form method="get" action="/do/bob/db/definedBenefitAccount/" class="page_section_filter form">
<p>organized by</p>
<bd:select name="participantAccountForm" property="fundsOrganizedBy" onchange="setFilterFromSelect(this);doFilter();">
	<bd:option value="${bdConstants.VIEW_BY_ASSET_CLASS}">Asset Class</bd:option>
	<bd:option value="${bdConstants.VIEW_BY_RISK_CATEGORY}">Risk/Return category</bd:option>
</bd:select>
</form>
</div>

<!-- 2.0 Report Table -->
<!-- 2.1 Report Table column Headers -->
<div class="report_table">
<table width="920" class="report_table_content">
	<thead>
		<tr>
			<th width="220" rowspan="2" class="val_str">Investment Option</th>
			<th width="100" rowspan="2" class="val_str align_center">Class</th>
			<th width="120" rowspan="2" class="cur align_center">Number Of Units</th>
			<th width="120" rowspan="2" class="cur align_center">Unit Value($)/Interest Rate</th>
			<th width="120" rowspan="2" class="cur align_center">Balance($)</th>
			<th width="120" rowspan="2" class="cur align_center">Percentage Of Total(%)</th>
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
				<th width="120" rowspan="2" class="cur align_center">Ongoing Contributions(%)</th>
</c:if>
		</tr>
	</thead>
	
</table>

   <%-- 2.2 Report Table data --%> 
<c:forEach items="${organizedFunds}" var="option" >

		<%-- <c:set var ="optionSize" value="${option.participantFundSummaryArray}"/> --%>
	<c:if test="${not empty option.participantFundSummaryArray}">

		<%-- ROW 0: Fund Category --%>
		<%-- Fund Category Name --%>
		<div class="page_section_subsubheader">
<h4>${option.category.categoryDesc}</h4>
		</div>

<c:set var="theFunds" value="${option.participantFundSummaryArray}"/>

		<%-- Report Table --%>
		<table width="920" class="report_table_content">
			<tbody>
			<%
		int theIndexs = 0;
	%>
<c:forEach items="${theFunds}" var="fund" varStatus="theIndex" >

					<%-- ROW 1: Fund Name & related values --%>
					     <%
					if (theIndexs++ % 2 == 0) {
				%>
						 <tr class="spec">
					  <%
						} else {
					%>
						 <tr>
					  <%
						}
					%>

						<!-- COLUMN: FUND NAME -->
						<td width="210" class="name">
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
							<a href="#fundsheet"
							   onMouseOver='self.status="JavaScript needed to open the fund page link"; return true'
							   NAME='${fund.fundId}'
							   onClick='FundWindow("<bd:fundLink fundIdProperty="fund.fundId" fundTypeProperty="fund.fundType" rateType ="bobContext.currentContract.defaultClass" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>
${fund.fundName} </a>
</c:if>
<c:if test="${participantAccountForm.asOfDateCurrent ==false}">
${fund.fundName}
</c:if>
                        </td>

						<%-- COLUMN: FUND CLASS --%>
						<td width="100" class="val_num_cnt align_center">
${fund.fundClass}
                        </td>

						<%-- COLUMN: NUMBER OF UNITS HELD --%>
						<td width="115" class="cur">
<c:if test="${option.category.categoryCode !=PB}">
<c:if test="${fund.fundTotalNumberOfUnitsHeld ==0}">
							         -
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld !=0}">
								          <report:number property="fund.fundTotalNumberOfUnitsHeld"
									                     defaultValue="0.00" 
									                     pattern="###,###,##0.000000"
									                     scale="6" />
</c:if>
</c:if>
                        </td>

						<%-- COLUMN: UNITS VALUE --%>
						<td width="120" class="cur">
<c:if test="${option.category.categoryCode !=PB}">
<c:if test="${fund.fundTotalNumberOfUnitsHeld ==0}">
<c:if test="${fund.fundTotalCompositeRate !=0}">
									<report:number property="fund.fundTotalCompositeRate"
										           pattern="###.00" 
										           defaultValue="0" 
										           scale="4" />%
</c:if>
<c:if test="${fund.fundTotalCompositeRate ==0}">
									<report:number property="fund.fundUnitValue"
										           defaultValue="0.00"
										            pattern="###,###,##0.00" />
</c:if>
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld !=0}">
								<report:number property="fund.fundUnitValue" 
								               defaultValue="0.00"
									           pattern="###,###,##0.00" />
</c:if>
</c:if>
                        </td>

						<%-- COLUMN: BALANCE --%>
						<td width="120" class="cur">
						    <report:number property="fund.fundTotalBalance" 
						                   defaultValue="0.00"
							               pattern="###,###,##0.00" />
                        </td>

						<%-- COLUMN: % OF TOTAL --%>
						<td width="120" class="pct">
						    <report:number property="fund.fundTotalPercentageOfTotal" 
						                   pattern="##0.00%"
							               defaultValue="0.00%"
							               scale="4" />
                        </td>

						<%-- COLUMN: % ONGOING CONTRIBUTION --%>
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
							<td width="120" class="pct">
							    <report:number property="fund.employerOngoingContributions" 
							                   pattern="##0.00%"
								               defaultValue="0.00%" 
								               scale="4" />
                            </td>
</c:if>
					</tr>
</c:forEach>
			</tbody>
		</table>
		<%-- Report Table End--%>
</c:if>
</c:forEach>
</div>

<!-- FootNotes and Disclaimer -->
<layout:pageFooter/>
<!--end of footnotes-->
