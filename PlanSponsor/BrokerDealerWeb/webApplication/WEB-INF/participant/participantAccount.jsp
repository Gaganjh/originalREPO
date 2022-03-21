<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.InvestmentOptionVO"%>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantFundSummaryVO" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
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

<c:if test="${requestScope.penErrors==true}">
	<report:formatMessages scope="request"/>
</c:if>

<!-- 1.0 Page Filter/Sort section -->
<div class="page_section_subheader controls">
	<h3><content:getAttribute beanName="layoutPageBean" attribute="body2Header" /></h3>
	<form method="get" action="/do/participant/participantAccount/" class="page_section_filter form" >
       	<p>Organized by</p> 
       	<bd:select name="participantAccountForm" property="fundsOrganizedBy"  onchange="setFilterFromSelect(this);doFilter();">
           	<bd:option value="<%=BDConstants.VIEW_BY_ASSET_CLASS%>">Asset Class</bd:option>
	         <bd:option value="<%=BDConstants.VIEW_BY_RISK_CATEGORY%>">Risk/Return Category</bd:option>
		</bd:select>
	</form>
</div>

<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
		<c:set var="widthColumn1" value="20%"></c:set>
		<c:set var="widthColumn2" value="5%"></c:set>
		<c:set var="widthColumn3" value="10%"></c:set>
		<c:set var="widthColumn4" value="15%"></c:set>
		<c:set var="widthColumn5" value="10%"></c:set>
		<c:set var="widthColumn6" value="10%"></c:set>
		<c:set var="widthColumn7" value="15%"></c:set>
		<c:set var="widthColumn8" value="15%"></c:set>
</c:if>
<c:if test="${participantAccountForm.asOfDateCurrent !=true}">
		<c:set var="widthColumn1" value="30%"></c:set>
		<c:set var="widthColumn2" value="10%"></c:set>
		<c:set var="widthColumn3" value="15%"></c:set>
		<c:set var="widthColumn4" value="15%"></c:set>
		<c:set var="widthColumn5" value="15%"></c:set>
		<c:set var="widthColumn6" value="15%"></c:set>
</c:if>

<!-- 2.0 Report Table -->
<!-- 2.1 Report Table column Headers -->
<div class="report_table">
<table width="920" class="report_table_content">
 	<thead>
   	<tr>
		<th width="${widthColumn1}" rowspan="2" class="val_str">Investment Option</th>
      	<th width="${widthColumn2}" rowspan="2" class="val_str">Class</th>
      	<th width="${widthColumn3}" rowspan="2" class="val_str align_center">Number Of Units</th>
      	<th width="${widthColumn4}" rowspan="2" class="val_str align_center">Unit Value($)</th>
      	<th width="${widthColumn5}" rowspan="2" class="val_str align_center">Balance($)</th>
      	<th width="${widthColumn6}" rowspan="2" class="val_str align_center">% Of Total</th>
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
      	<th colspan="2"><div align="center">Ongoing Contributions(%)</div></th>
</c:if>
    </tr>
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
    <tr>
      	<th width="${widthColumn7}" class="sub align_center">Employee</th>
      	<th width="${widthColumn8}" class="sub align_center">Employer</th>
    </tr>
</c:if>
 	</thead>
 	
</table>

<!-- 2.2 Report Table data -->
<c:forEach items="${organizedFunds}" var="option" >
<c:set var ="optionSize" value="${option.participantFundSummaryArray}"/>
<c:if test="${not empty option.participantFundSummaryArray}">
		
	<!-- ROW 0: Fund Category -->
	<!-- Fund Category Name -->
	<%-- CR011 --%>
	<div class="page_section_subsubheader">
<h4>${option.category.categoryDesc}
<c:if test="${option.category.categoryCode eq 'GIFL_RISK_CATEGORY_CODE'}"> *</c:if></h4>
	</div>
	
<c:set var="theFunds" value="${option.participantFundSummaryArray}"/>
		
<c:forEach items="${option.participantFundSummaryArray}" var="fund" >
		<!-- Report Table -->
		<table width="920" class="report_table_content" >
		<tbody>
		<!-- ROW 1: Fund Name & related values -->
			<tr class="spec">
				<!-- COLUMN: FUND NAME -->
				<td width="${widthColumn1}" class="name">
<c:if test="${participantAccountForm.asOfDateCurrent eq true}">
<c:if test="${option.category.categoryCode ne 'PB'}">
<c:if test="${fund.rateType == SIG_PLUS_RATE_TYPE || fund.rateType == CY1_RATE_TYPE || fund.rateType == CY2_RATE_TYPE}">
       	   				<a href="#fundsheet" 
       	   				onMouseOver='self.status="JavaScript needed to open the fund page link"; return true'  
NAME='${fund.fundId}' onClick='FundWindow("<bd:fundLink fundIdProperty="fund.fundId" fundTypeProperty="fund.fundType" rateType = "fund.rateType" fundSeries ="<%=bobContext.getCurrentContract().getFundPackageSeriesCode()%>" productId ="<%=bobContext.getCurrentContract().getProductId()%>" siteLocation="${bobContext.contractSiteLocation}" />")'>

${fund.fundName}
        	   			</a>
        	   			</c:if>
        	   			 <c:if test="${fund.rateType != SIG_PLUS_RATE_TYPE && fund.rateType != CY1_RATE_TYPE && fund.rateType != CY2_RATE_TYPE}">
        	   			 <a href="#fundsheet" 
       	   				onMouseOver='self.status="JavaScript needed to open the fund page link"; return true'  
NAME='${fund.fundId}' onClick='FundWindow("<bd:fundLink fundIdProperty="fund.fundId" fundTypeProperty="fund.fundType" rateType = "bobContext.currentContract.defaultClass" fundSeries ="<%=bobContext.getCurrentContract().getFundPackageSeriesCode()%>" productId ="<%=bobContext.getCurrentContract().getProductId()%>" siteLocation="${bobContext.contractSiteLocation}" />")'>

${fund.fundName}
        	   			</a>
        	   			</c:if>
</c:if>
  	
<c:if test="${option.category.categoryCode eq 'PB'}">
${fund.fundName}
</c:if>
</c:if>
				
<c:if test="${participantAccountForm.asOfDateCurrent eq false}">
${fund.fundName}
</c:if>
				</td>
				
				<!-- COLUMN: FUND CLASS -->
				<td width="${widthColumn2}" class="val_num_cnt align_center">
<c:if test="${option.category.categoryCode ne 'PB'}">
${fund.fundClass}
</c:if>
<c:if test="${option.category.categoryCode eq 'PB'}">
				-
</c:if>
				
				</td>
				
				<!-- COLUMN: NUMBER OF UNITS HELD -->
				<td width="${widthColumn3}" class="val_num_cnt align_right">
<c:if test="${option.category.categoryCode ne'PB'}">
<c:if test="${fund.fundTotalNumberOfUnitsHeld eq '0'}">
							-
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld ne '0'}">
			          		<report:number property="fund.fundTotalNumberOfUnitsHeld" defaultValue = "0.00" 
			          					pattern="###,###,##0.000000" scale="6"/>
</c:if>
</c:if>
<c:if test="${option.category.categoryCode eq 'PB'}">
						-
</c:if>
				</td>
				
				<!-- COLUMN: UNITS VALUE -->
				<td width="${widthColumn4}" class="cur">
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
					          				<report:number property="fund.fundUnitValue" defaultValue = "0.00"  pattern="###,###,##0.00" />
			</c:if>
			</c:if>
									
			<c:if test="${fund.fundTotalNumberOfUnitsHeld ne '0'}">
						    			<report:number property="fund.fundUnitValue" defaultValue = "0.00"  pattern="###,###,##0.00"/>
			</c:if>
		</c:otherwise>
	</c:choose>
</c:if>
					
<c:if test="${option.category.categoryCode eq 'PB'}">
						-
</c:if>
				</td>
				
				<!-- COLUMN: BALANCE -->
				<td width="${widthColumn5}" class="cur">
					<report:number property="fund.fundTotalBalance" defaultValue = "0.00"  pattern="###,###,##0.00"/>
				</td>
				
				<!-- COLUMN: % OF TOTAL -->
				<td width="${widthColumn6}" class="pct">
					<report:number property="fund.fundTotalPercentageOfTotal" pattern="##0.00%" defaultValue="0.00%" scale="4" />
				</td>
				
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
					<!-- COLUMN: ONGOING CONTRIBUTION-EMPLOYEE -->
					<td width="${widthColumn7}" class="pct">
						<report:number property="fund.employeeOngoingContributions" pattern="##0.00%" defaultValue="0.00%" scale="4" />
					</td>
				
					<!-- COLUMN: ONGOING CONTRIBUTION-EMPLOYER -->
					<td width="${widthColumn8}" class="pct">
						<report:number property="fund.employerOngoingContributions" pattern="##0.00%" defaultValue="0.00%" scale="4" />
					</td>
</c:if>
				
			</tr>
			
		<!-- ROW 2: Employee Assets & related values -->
			<tr >
				<!-- COLUMN: EMPLOYEE ASSETS LABEL -->
				<td width="${widthColumn1}" class="name employee_assets">Employee Assets</td>
				<td width="${widthColumn2}" class="val_num_cnt"></td>
				
				<!-- COLUMN: # OF UNITS -->
				<td width="${widthColumn3}" class="val_num_cnt align_right">
<c:if test="${option.category.categoryCode ne 'PB'}">
<c:if test="${fund.fundTotalNumberOfUnitsHeld eq '0'}">
							-
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld ne '0'}">
							<report:number property="fund.employeeNumberOfUnitsHeld" defaultValue = "0.00" 
										pattern="###,###,##0.000000" scale="6"/>
</c:if>
</c:if>
<c:if test="${option.category.categoryCode eq 'PB'}">
						-
</c:if>
				</td>
				
				<!-- COLUMN: UNITS VALUE -->
				<td width="${widthColumn4}" class="cur">
<c:if test="${option.category.categoryCode ne 'PB'}">
<c:if test="${fund.fundTotalNumberOfUnitsHeld eq '0'}">
<c:if test="${fund.employeeCompositeRate ne '0'}">
				  				<report:number property="fund.employeeCompositeRate" pattern="###.00" defaultValue="0" scale="4" />%
</c:if>
<c:if test="${fund.employeeCompositeRate eq '0'}">
				  				-
</c:if>
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld != '0'}">
							<report:number property="fund.fundUnitValue" defaultValue = "0.00"  pattern="###,###,##0.00" />
</c:if>
</c:if>
<c:if test="${option.category.categoryCode eq 'PB'}">
						-
</c:if>
				</td>
				
				<!-- COLUMN: BALANCE -->
				<td width="${widthColumn5}" class="cur">
					<report:number property="fund.employeeBalance" defaultValue = "0.00"  pattern="###,###,##0.00"/>
				</td>
				
				<td width="${widthColumn6}" class="pct">-</td>
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
				<td width="${widthColumn7}" class="pct">-</td>
				<td width="${widthColumn8}" class="pct">-</td>
</c:if>
				
			</tr>
			
		<!-- ROW 3: Employer Assets & related values -->
			<tr  class="spec">
				<!-- COLUMN: EMPLOYER ASSETS LABEL -->
				<td width="${widthColumn1}"  class="name employer_assets">Employer Assets</td>
				<td width="${widthColumn2}" class="val_num_cnt"></td>
				
				<!-- COLUMN: # OF UNITS -->
				<td width="${widthColumn3}" class="val_num_cnt align_right">
<c:if test="${option.category.categoryCode ne 'PB'}">
<c:if test="${fund.fundTotalNumberOfUnitsHeld eq '0'}">
						  	-
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld ne '0'}">
							<report:number property="fund.employerNumberOfUnitsHeld" defaultValue = "0.00" 
										pattern="###,###,##0.000000" scale="6"/>
</c:if>
</c:if>
<c:if test="${option.category.categoryCode eq 'PB'}">
						-
</c:if>
				</td>

				<!-- COLUMN: UNITS VALUE -->
				<td width="${widthColumn4}" class="cur">
<c:if test="${option.category.categoryCode ne 'PB'}">
<c:if test="${fund.fundTotalNumberOfUnitsHeld eq '0'}">
<c:if test="${fund.employerCompositeRate ne '0'}">
				  				<report:number property="fund.employerCompositeRate" pattern="###.00" defaultValue="0" scale="4" />%
</c:if>
<c:if test="${fund.employeeCompositeRate eq '0'}">
				  				-
</c:if>
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld ne '0'}">
			          		<report:number property="fund.fundUnitValue" defaultValue = "0.00"  pattern="###,###,##0.00"/>
</c:if>
</c:if>
<c:if test="${option.category.categoryCode eq 'PB'}">
						-
</c:if>
				</td>
				
				<!-- COLUMN: BALANCE -->
				<td width="${widthColumn5}" class="cur">
					<report:number property="fund.employerBalance" defaultValue = "0.00"  pattern="###,###,##0.00"/>
				</td>
				<td width="${widthColumn6}" class="pct">-</td>
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
				<td width="${widthColumn7}" class="pct">-</td>
				<td width="${widthColumn8}" class="pct">-</td>
</c:if>
				
			</tr>
		</tbody>
		</table>
		<!-- Report Table End-->
</c:forEach>
</c:if>
</c:forEach>
</div>

<!-- FootNotes and Disclaimer -->
<div class="footnotes">
			<%-- CR011 --%>
<c:if test="${participantAccountForm.showGiflFootnote ==true}">
			<content:contentBean
				contentId="<%=BDContentConstants.BDW_PA_GIFL_FOOTNOTE%>"
				type="<%=BDContentConstants.TYPE_DISCLAIMER%>" id="participantGiflFootnote" />
					<p class="footnote">*<content:getAttribute id="participantGiflFootnote" attribute="text" /></p>
</c:if>
<c:if test="${ participantAccountForm.asOfDateCurrent and participantAccountForm.showManagedAccount }">
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
<!--end of footnotes-->
