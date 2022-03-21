<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.InvestmentOptionVO"%>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantFundSummaryVO" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
	String GIFL_RISK_CATEGORY_CODE = BDConstants.GIFL_RISK_CATEGORY_CODE;
	pageContext.setAttribute("GIFL_RISK_CATEGORY_CODE",GIFL_RISK_CATEGORY_CODE,PageContext.PAGE_SCOPE);
	String GIFL_VERSION_03 = BDConstants.GIFL_VERSION_03;
	pageContext.setAttribute("GIFL_VERSION_03",GIFL_VERSION_03,PageContext.PAGE_SCOPE);

	String TRUE = BDConstants.TRUE;
	pageContext.setAttribute("TRUE",TRUE,PageContext.PAGE_SCOPE);
	String YES = BDConstants.YES;
	pageContext.setAttribute("YES",YES,PageContext.PAGE_SCOPE);
	String DEFAULT_DATE = BDConstants.DEFAULT_DATE;
	pageContext.setAttribute("DEFAULT_DATE",DEFAULT_DATE,PageContext.PAGE_SCOPE);
	String NO = BDConstants.NO;
	pageContext.setAttribute("NO",NO,PageContext.PAGE_SCOPE);
	String NA = BDConstants.NA;
	pageContext.setAttribute("NA",NA,PageContext.PAGE_SCOPE);
	String SIG_PLUS_RATE_TYPE = BDConstants.SIGNATURE_PLUS;
	pageContext.setAttribute("SIG_PLUS_RATE_TYPE",SIG_PLUS_RATE_TYPE,PageContext.PAGE_SCOPE);
	String CY1_RATE_TYPE = BDConstants.CY1_RATE_TYPE;
	pageContext.setAttribute("CY1_RATE_TYPE",CY1_RATE_TYPE,PageContext.PAGE_SCOPE);
	String CY2_RATE_TYPE = BDConstants.CY2_RATE_TYPE;
	pageContext.setAttribute("CY2_RATE_TYPE",CY2_RATE_TYPE,PageContext.PAGE_SCOPE);



%>

<content:contentBean contentId="<%=BDContentConstants.FIXED_FOOTNOTE_PBA_FOR_PARTICIPANT_REPORTS%>" type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" id="footnotePBA" />

<input type="hidden" name="pdfCapped" /><%--  input - name="participantAccountForm" --%>
<!-- Include common -->        
<jsp:include page="/WEB-INF/participant/participantAccountCommon.jsp" flush="true" /> 

<report:formatMessages scope="request"/>
<script type="text/javascript" >   
		// This function is implementedto be executed during onLoad.
	function doOnload() {
		scroll(0, 650);
	}
</script>
  
<!-- 1.0 Page Filter/Sort section -->
<div class="page_section_subheader controls">
	<h3><content:getAttribute beanName="layoutPageBean" attribute="body2Header" /></h3>
	<form method="GET" action="/participant/participantAccount/" class="page_section_filter form" >
       	<p>Organized by</p> 
       	<bd:select name="participantAccountForm" property="fundsOrganizedBy"  onchange="setFilterFromSelect(this);doFilter();">
           	<bd:option value="<%=BDConstants.VIEW_BY_ASSET_CLASS%>">Asset Class</bd:option>
	         <bd:option value="<%=BDConstants.VIEW_BY_RISK_CATEGORY%>">Risk/Return Category</bd:option>
		</bd:select>
	</form>
</div>

<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
<div class="report_table">

<!-- Report Table column headers -->
<table width="918" class="report_table_content">
 	<thead>
   	<tr>
		<th width="20%" rowspan="2" class="val_str">Investment Option</th>
      	<th width="5%" rowspan="2" class="val_str">Class</th>
      	<th width="12.5%" rowspan="2" class="val_str">Number Of Units</th>
      	<th width="12.5%" rowspan="2" class="val_str align_center">Unit Value($)/Interest Rate</th>
      	<th colspan="2"><div align="center">Balance Subtotals($)</div></th>
      	<th width="10%" rowspan="2" class="val_str align_center">Balance($)</th>
      	<th width="10%" rowspan="2" class="val_str align_center">% Of Total</th>
    </tr>
    <tr>
      	<th width="15%" class="sub align_center">Employee</th>
      	<th width="15%" class="sub align_center">Employer</th>
    </tr>
 	</thead>
 	<tbody>
 	</tbody>
</table>


<!-- Records for the Report Table -->
<c:forEach items="${organizedFunds}" var="option" >

<c:set var="optionSize" value="${option.participantFundSummaryArray}"/>
<c:if test="${not empty option.participantFundSummaryArray}">
	<!-- ROW 0: Fund Category -->
	<!-- Fund Category Name -->
	<div class="page_section_subsubheader">
<h4>${option.category.categoryDesc} 
<c:if test="${option.category.categoryCode == GIFL_RISK_CATEGORY_CODE}"> *</c:if></h4>
	</div>
	
<c:set var="theFunds" value="${option.participantFundSummaryArray}"/>
<c:forEach items="${theFunds}" var="fund" >
		<table width="918" class="report_table_content" >
		<tbody>
		<!-- ROW 1: Fund Name & related values -->
		<tr class="spec">
			<!-- COLUMN 1: FUND NAME -->
			<td width="20%" class="name">
<c:if test="${option.category.categoryCode ne 'PB'}">
<c:if test="${fund.rateType == SIG_PLUS_RATE_TYPE || fund.rateType == CY1_RATE_TYPE || fund.rateType == CY2_RATE_TYPE}">
<a href="#fundsheet" onMouseOver='self.status="JavaScript needed to open the fund page link"; return true' NAME='${fund.fundId}' onClick='FundWindow("<bd:fundLink fundIdProperty="fund.fundId" fundTypeProperty="fund.fundType" rateType = "fund.rateType" fundSeries ="<%=bobContext.getCurrentContract().getFundPackageSeriesCode()%>" productId ="<%=bobContext.getCurrentContract().getProductId()%>" siteLocation="${bobContext.contractSiteLocation}" />")'>${fund.fundName}</a>
</c:if>
 <c:if test="${fund.rateType != SIG_PLUS_RATE_TYPE && fund.rateType != CY1_RATE_TYPE && fund.rateType != CY2_RATE_TYPE}">
 <a href="#fundsheet" onMouseOver='self.status="JavaScript needed to open the fund page link"; return true' NAME='${fund.fundId}' onClick='FundWindow("<bd:fundLink fundIdProperty="fund.fundId" fundTypeProperty="fund.fundType" rateType = "bobContext.currentContract.defaultClass" fundSeries ="<%=bobContext.getCurrentContract().getFundPackageSeriesCode()%>" productId ="<%=bobContext.getCurrentContract().getProductId()%>" siteLocation="${bobContext.contractSiteLocation}" />")'>${fund.fundName}</a>
 </c:if>
</c:if>
			
<c:if test="${option.category.categoryCode eq 'PB'}">
${fund.fundName}
</c:if>
			</td>
			
			<!-- COLUMN 2: FUND CLASS -->
			<td width="5%" class="val_num_cnt align_center">
<c:if test="${option.category.categoryCode ne 'PB'}">
${fund.fundClass}
</c:if>
<c:if test="${option.category.categoryCode eq 'PB'}">
				-
</c:if>
			</td>
			
			<!-- COLUMN 3: NUMBER OF UNITS HELD -->
			<td width="12.5%" class="val_num_cnt align_right">
<c:if test="${option.category.categoryCode ne'PB'}">
<c:if test="${fund.fundTotalNumberOfUnitsHeld ne'0'}">
						-
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld ne '0'}">
			        	<report:number property="fund.fundTotalNumberOfUnitsHeld" defaultValue = "0.00"  pattern="<%=BDConstants.AMOUNT_FORMAT_SIX_DECIMALS%>" scale="6"/>
</c:if>
</c:if>
				
<c:if test="${option.category.categoryCode eq 'PB'}">
					-
</c:if>
			</td>
			
			<!-- COLUMN 4: UNITS VALUE -->
			<td width="12.5%" class="cur">
<c:if test="${option.category.categoryCode ne 'PB'}">
	<c:choose>
		<c:when test="${fund.svgifFlg == true}">
			-
		</c:when>
		<c:otherwise>
			<c:if test="${fund.fundTotalNumberOfUnitsHeld eq '0'}">
			<c:if test="${fund.fundTotalCompositeRate ne '0'}">
							  			<report:number property="fund.fundTotalCompositeRate" pattern="###.00" defaultValue="0" scale="4" />%
			</c:if>
			<c:if test="${fund.fundTotalCompositeRate eq '0'}">
					        	  		<report:number property="fund.fundUnitValue" defaultValue = "0.00"  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>" />
			</c:if>
			</c:if>
						
			<c:if test="${fund.fundTotalNumberOfUnitsHeld ne '0'}">
						        	<report:number property="fund.fundUnitValue" defaultValue = "0.00"  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</c:if>
		</c:otherwise>
	</c:choose>
</c:if>
				
<c:if test="${option.category.categoryCode eq 'PB'}">
					-
</c:if>
			</td>
			
			<!-- COLUMN 5: BALANCE SUBTOTALS-EMPLOYEE -->
			<td width="15%" class="pct">
				<report:number property="fund.employeeBalance" defaultValue = "0.00"  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
			
			<!-- COLUMN 6: ONGOING CONTRIBUTION-EMPLOYER -->
			<td width="15%" class="pct">
				<report:number property="fund.employerBalance" defaultValue = "0.00"  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
			
			<!-- COLUMN 7: BALANCE -->
			<td width="10%" class="cur">
				<report:number property="fund.fundTotalBalance" defaultValue = "0.00"  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
			
			<!-- COLUMN 8: % OF TOTAL -->
			<td width="10%" class="pct">
				<report:number property="fund.fundTotalPercentageOfTotal" pattern="##0.00%" defaultValue="0.00%" scale="4" />
			</td>
		</tr>

	<!-- Money Type Details for each fund -->      
<c:set var="theMoneyTypeDetails" value="${fund.fundMoneyTypeDetails}"/>
   
<c:forEach items="${theMoneyTypeDetails}" var="moneyTypeDetails" varStatus="moneyTypeIndex" >

<c:set var="moneyTypeIndex" value="${moneyTypeIndex.index}"/>

  
		   <c:if test="${moneyTypeIndex % 2 ne 0}"> 
			     <tr class="spec">
		   </c:if>
		   <c:if test="${moneyTypeIndex % 2 eq 0}"> 
				 <tr>
		    </c:if>	

			<!-- COLUMN 1: FUND NAME -->
			<td width="20%" class="name">
${moneyTypeDetails.moneyTypeName}
			</td>
			
			<!-- COLUMN 2: FUND CLASS -->
			<td width="5%" class="val_num_cnt align_center"></td>
			
			<!-- COLUMN 3: NUMBER OF UNITS HELD -->
			<td width="12.5%" class="val_num_cnt align_right">
<c:if test="${option.category.categoryCode ne 'PB'}">
<c:if test="${moneyTypeDetails.numberOfUnitsHeld ne '0'}">
		    			<report:number property="moneyTypeDetails.numberOfUnitsHeld" defaultValue = "0.00"  pattern="<%=BDConstants.AMOUNT_FORMAT_SIX_DECIMALS%>" scale="6"/>
</c:if>
<c:if test="${moneyTypeDetails.numberOfUnitsHeld eq '0'}">
                         -
</c:if>
</c:if>
				
<c:if test="${option.category.categoryCode eq 'PB'}">
					-
</c:if>
			</td>
			
			<!-- COLUMN 4: UNITS VALUE -->
			<td width="12.5%" class="cur">
<c:if test="${option.category.categoryCode ne 'PB'}">
<c:if test="${moneyTypeDetails.numberOfUnitsHeld eq '0'}">
<c:if test="${moneyTypeDetails.compositeRate ne '0'}">
				  			-
</c:if>
</c:if>
		
<c:if test="${moneyTypeDetails.numberOfUnitsHeld ne '0'}">
			        	<report:number property="fund.fundUnitValue" defaultValue = "0.00"  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>" />
</c:if>
</c:if>
				
<c:if test="${option.category.categoryCode eq 'PB'}">
					-
</c:if>
			</td>
			
			<!-- COLUMN 5: BALANCE SUBTOTALS-EMPLOYEE -->
			<td width="15%" class="pct">
<c:if test="${moneyTypeDetails.moneyType eq 'EE'}">
            		<report:number property="moneyTypeDetails.balance" defaultValue = "0.00"  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
</c:if>
<c:if test="${moneyTypeDetails.moneyType ne 'EE'}">
            		-
</c:if>
			</td>
			
			<!-- COLUMN 6: ONGOING CONTRIBUTION-EMPLOYER -->
			<td width="15%" class="pct">
<c:if test="${moneyTypeDetails.moneyType eq 'ER'}">
            		<report:number property="moneyTypeDetails.balance" defaultValue = "0.00"  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
</c:if>
<c:if test="${moneyTypeDetails.moneyType ne 'ER'}">
            		-
</c:if>
			</td>
			
			<!-- COLUMN 7: BALANCE -->
			<td width="10%" class="cur">-</td>
			
			<!-- COLUMN 8: % OF TOTAL -->
			<td width="10%" class="pct">-</td>
		</tr>
</c:forEach>
		</tbody>
		</table>
</c:forEach>
</c:if>
</c:forEach>
</div>
</c:if>

<!-- FootNotes and Disclaimer -->
<div class="footnotes">
			<%-- CR011 --%>
<c:if test="${participantAccountForm.showGiflFootnote ==true}">
			<content:contentBean
				contentId="<%=BDContentConstants.BDW_PA_GIFL_FOOTNOTE%>"
				type="<%=BDContentConstants.TYPE_DISCLAIMER%>" id="participantGiflFootnote" />
					<p class="footnote">*<content:getAttribute id="participantGiflFootnote" attribute="text" /></p>
</c:if>

<c:if test="${participantAccountForm.asOfDateCurrent and participantAccountForm.showManagedAccount }">
			<content:contentBean
				contentId="<%=BDContentConstants.MA_FOOTNOTE%>"
				type="<%=BDContentConstants.TYPE_DISCLAIMER%>" id="participantMAFootnote" />
			<p class="footnote"><content:getAttribute id="participantMAFootnote" attribute="text" /></p>
</c:if>

<c:if test="${participantAccountForm.showPba ==true}">
			  <dl><dd><content:getAttribute beanName="footnotePBA" attribute="text"/></dd></dl>
</c:if>
		<dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
        <dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
		<div class="footnotes_footer"></div>
</div> 
