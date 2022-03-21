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

<report:formatMessages scope="request"/>
<%-- Constant Files used--%>
<un:useConstants var="bdConstants" className="com.manulife.pension.bd.web.BDConstants" />


<%-- Beans used --%>
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>


<%-- Java scripts used --%>
<script type="text/javascript" >   
		// This function is implemented to be executed during onLoad.
	function doOnload() {
		scroll(0, 650);
	}
</script>

<input type="hidden" name="pdfCapped" /><%--  input - name="participantAccountForm" --%>

<%-- Include common --%>        
<jsp:include page="/WEB-INF/participant/definedBenefitAccountCommon.jsp" flush="true" /> 

<%-- 1.0 Page Filter/Sort section --%>
<div class="page_section_subheader controls">
<h3><content:getAttribute beanName="layoutPageBean" attribute="body2Header" /></h3>
<form method="GET" action="/bob/db/definedBenefitAccount/" class="page_section_filter form">
<p>organized by</p>
<bd:select name="participantAccountForm" property="fundsOrganizedBy" onchange="setFilterFromSelect(this);doFilter();">
	<bd:option value="${bdConstants.VIEW_BY_ASSET_CLASS}">Asset class</bd:option>
	<bd:option value="${bdConstants.VIEW_BY_RISK_CATEGORY}">Risk/Return category</bd:option>
</bd:select>
</form>
</div>

<c:if test="${participantAccountForm.asOfDateCurrent ==true}">

	<div class="report_table"><%-- Report Table column headers --%>
	<table width="920" class="report_table_content">
		<thead>
			<tr>
				<th width="240" rowspan="2" class="val_str">Investment Option</th>
				<th width="120" rowspan="2" class="val_str align_center">Class</th>
				<th width="140" rowspan="2" class="cur align_center">Number Of Units</th>
				<th width="140" rowspan="2" class="cur align_center">Unit Value($)/Interest Rate</th>
				<th width="140" rowspan="2" class="cur align_center">Balance($)</th>
				<th width="140" rowspan="2" class="cur align_center">Percentage Of Total(%)</th>
			</tr>
		</thead>
	</table>

	<%-- If no investment display the message --%> 
<c:if test="${participantAccountForm.hasInvestments ==false}">
		<table width="918" class="report_table_content">
			<tbody>
				<tr>
					<td width="920"><content:getAttribute
						id="NoParticipantsMessage" attribute="text" /></td>
				</tr>
			</tbody>
		</table>
</c:if>
	
<c:if test="${participantAccountForm.hasInvestments ==true}">

		<%-- Records for the Report Table --%>
<c:forEach items="${organizedFunds}" var="option" >



		<%-- <c:set var ="optionSize" value="${option.participantFundSummaryArray}"/> --%>

			<c:if test="${not empty option.participantFundSummaryArray}">
				<%-- Fund Category Name --%>
				<div class="page_section_subsubheader">
<h4>${option.category.categoryDesc}</h4>
				</div>

<c:set var="theFunds" value="${option.participantFundSummaryArray}"/>

<c:forEach items="${theFunds}" var="fund" varStatus="theIndex" >




					<table width="920" class="report_table_content">
						<tbody>
							
						        <tr class="spec">
								<%-- COLUMN 1: FUND NAME --%>
								<td width="240" class="name">
                                   <a href="#fundsheet"
									  onMouseOver='self.status="JavaScript needed to open the fund page link"; return true'
									  NAME='${fund.fundId}'
									  onClick='FundWindow("<bd:fundLink fundIdProperty="fund.fundId" fundTypeProperty="fund.fundType" rateType ="bobContext.currentContract.defaultClass" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>

${fund.fundName} </a></td>

								<%-- COLUMN 2: FUND CLASS --%>
								<td width="120" class="val_num_cnt align_center">
${fund.fundClass}
                                </td>

								<%-- COLUMN 3: NUMBER OF UNITS HELD --%>
								<td width="140" class="cur">
<c:if test="${fund.fundTotalNumberOfUnitsHeld ==0}">
						              -
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld !=0}">
										<report:number property="fund.fundTotalNumberOfUnitsHeld"
											           defaultValue="0.00"
											           pattern="###,###,##0.000000"
                                                       scale="6" />
</c:if>
                                </td>

								<%-- COLUMN 4: UNITS VALUE --%>
								<td width="140" class="cur">
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
												           pattern="${bdConstants.AMOUNT_FORMAT_TWO_DECIMALS}" />
</c:if>
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld !=0}">
										<report:number property="fund.fundUnitValue"
											           defaultValue="0.00"
											           pattern="${bdConstants.AMOUNT_FORMAT_TWO_DECIMALS}" />
</c:if>
                               </td>

								<%-- COLUMN 7: BALANCE --%>
								<td width="140" class="cur">
								    <report:number property="fund.fundTotalBalance" 
								                   defaultValue="0.00"
									               pattern="${bdConstants.AMOUNT_FORMAT_TWO_DECIMALS}" />
                                </td>

								<%-- COLUMN 8: % OF TOTAL --%>
								<td width="140" class="pct">
								    <report:number property="fund.fundTotalPercentageOfTotal" 
								                   pattern="##0.00%"
									               defaultValue="0.00%" 
									               scale="4" />
                                </td>
							</tr>

							<%-- Money Type Details for each fund --%>
<c:set var="theMoneyTypeDetails" value="${fund.fundMoneyTypeDetails}"/>
<% int theIndexs=0; %>
<c:forEach items="${theMoneyTypeDetails}" var="moneyTypeDetails" varStatus="theIndex" >

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

									<%-- COLUMN 1: FUND NAME --%>
									<td width="240" class="name">
${moneyTypeDetails.moneyTypeName}
                                    </td>

									<%-- COLUMN 2: FUND CLASS --%>
									<td width="120" class="val_num_cnt"></td>

									<%-- COLUMN 3: NUMBER OF UNITS HELD --%>
									<td width="140" class="cur">
<c:if test="${moneyTypeDetails.numberOfUnitsHeld !=0}">
											<report:number property="moneyTypeDetails.numberOfUnitsHeld"
												           defaultValue="0.00"
												           pattern="${bdConstants.AMOUNT_FORMAT_SIX_DECIMALS}"
												           scale="6" />
</c:if>
                                    </td>

									<%-- COLUMN 4: UNITS VALUE --%>
									<td width="140" class="cur">
<c:if test="${moneyTypeDetails.numberOfUnitsHeld ==0}">
<c:if test="${moneyTypeDetails.compositeRate !=0}">
				  			                    -
</c:if>
</c:if>
<c:if test="${moneyTypeDetails.numberOfUnitsHeld !=0}">
											<report:number property="fund.fundUnitValue"
												           defaultValue="0.00"
												           pattern="${bdConstants.AMOUNT_FORMAT_TWO_DECIMALS}" />
</c:if>
                                    </td>

									<%-- COLUMN 7: BALANCE --%>
									<td width="140" class="cur">-</td>

									<%-- COLUMN 8: % OF TOTAL --%>
									<td width="140" class="pct">-</td>

								</tr>
</c:forEach>
						</tbody>
					</table>
</c:forEach>
</c:if>
</c:forEach>
</c:if></div>
</c:if>

<!-- FootNotes and Disclaimer -->
<layout:pageFooter/>
<!--end of footnotes-->
