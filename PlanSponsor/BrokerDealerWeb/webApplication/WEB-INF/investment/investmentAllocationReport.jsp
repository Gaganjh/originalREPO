<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>

<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.FundCategory"%>
<%@ page import="com.manulife.pension.bd.web.bob.investment.InvestmentAllocationPageForm"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData" %>
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
	String VIEW_BY_ACTIVITY = BDConstants.VIEW_BY_ACTIVITY;
	pageContext.setAttribute("VIEW_BY_ACTIVITY",VIEW_BY_ACTIVITY,PageContext.PAGE_SCOPE);
	String SIG_PLUS_RATE_TYPE = BDConstants.SIGNATURE_PLUS;
	pageContext.setAttribute("SIG_PLUS_RATE_TYPE",SIG_PLUS_RATE_TYPE,PageContext.PAGE_SCOPE);
	String CY1_RATE_TYPE = BDConstants.CY1_RATE_TYPE;
	pageContext.setAttribute("CY1_RATE_TYPE",CY1_RATE_TYPE,PageContext.PAGE_SCOPE);
	String CY2_RATE_TYPE = BDConstants.CY2_RATE_TYPE;
	pageContext.setAttribute("CY2_RATE_TYPE",CY2_RATE_TYPE,PageContext.PAGE_SCOPE);


%>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<content:contentBean contentId="<%=BDContentConstants.BOB_INV_ALLOCATION_SIG_PLUS_DISCLOSURE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        beanName="sigPlusDisclosure"/>

<content:contentBean contentId="<%=BDContentConstants.FIXED_FOOTNOTE_PBA%>" type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" id="footnotePBA" />

<script type="text/javascript">
      function doViewDetails(fundid, fundName, fundClass) {
         document.investmentAllocationPageForm.action = "/do/bob/investment/investmentAllocationDetailsReport/";
	     document.investmentAllocationPageForm.selectedFundID.value = fundid;
	     document.investmentAllocationPageForm.selectedFundName.value = fundName;
	     document.investmentAllocationPageForm.submit();
      }
</script>

<input type="hidden" name="pdfCapped" />


<!--The summary box is displayed in this section-->
<div id="summaryBox">
 <h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
    <table width="100" border="0" cellspacing="0" cellpadding="3">
	 <tr>
		<td align="right" valign="top" nowrap="nowrap"><span
			class="name">Number of Investment Options Selected:</span></td>
		<td valign="top">
		<span class="name">
		     <strong>
${investmentAllocationPageForm.numberOfInvestmentOptionSelected}
		     </strong>
		</span>
		</td>
	 </tr>
   </table>
</div>
<!--End summary box-->

<!--Report Title-->
  <jsp:include page="/WEB-INF/global/displayContractInfoWithRothMessage.jsp"/>
 
<c:if test="${investmentAllocationPageForm.jhiIndicatorFlg == true}">   
	<div id="signaturePlusDisclosure">
		<p><content:getAttribute attribute="text" beanName="sigPlusDisclosure"/></p>
	</div>
</c:if>
  
<!--Error- message box-->  
  <report:formatMessages scope="request"/><br/>


<!--Navigation bar-->
  <navigation:contractReportsTab/>


<!--Section title and Display preferences-->
<div class="page_section_subheader controls">
  <h3><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></h3>
  <form style="margin-bottom:0;" method="get" action="/bob/investment/investmentAllocationReport/"  
				class="page_section_filter form">
         <p><label for="investment_allocation_filter">as of:</label></p>
         <bd:select name="investmentAllocationPageForm" property="asOfDateReport"  onchange="setFilterFromSelect(this);doFilter();">
	        <bd:dateOption name="<%=BDConstants.BOBCONTEXT_KEY%>" property="currentContract.contractDates.asOfDate" renderStyle="<%=RenderConstants.LONG_MDY%>" />
            <bd:dateOptions name="<%=BDConstants.BOBCONTEXT_KEY%>" property="currentContract.contractDates.monthEndDates" renderStyle="<%=RenderConstants.LONG_MDY%>" />
         </bd:select> 
         <p> Organized by:</p>
         <bd:select name="investmentAllocationPageForm" property="organizingOption"  onchange="setFilterFromSelect(this);doFilter();">
	         <bd:option value="<%=BDConstants.VIEW_BY_ASSET_CLASS%>">Asset Class</bd:option>
	         <bd:option value="<%=BDConstants.VIEW_BY_RISK_CATEGORY%>">Risk/Return Category</bd:option>
         </bd:select> 
         <p> View by:</p>
         <bd:select name="investmentAllocationPageForm" property="viewOption"  onchange="setFilterFromSelect(this);doFilter();">
	        <bd:option value="<%=BDConstants.VIEW_BY_ASSET%>">Asset View</bd:option>
	        <bd:option value="<%=BDConstants.VIEW_BY_ACTIVITY%>">Activity View</bd:option>
         </bd:select> 
         &nbsp;
   </form>   
   <c:if test="${empty requestScope.isError}">
    <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
    <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
   </c:if>
</div>
<!--End of Section title and Display preferences-->

<c:if test="${empty displayDates}">

<%
InvestmentAllocationReportData theReport = (InvestmentAllocationReportData)request.getAttribute(BDConstants.REPORT_BEAN_INVESTMENT);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
String NON_LIFESTYLE_LIFECYCLE = FundCategory.NON_LIFESTYLE_LIFECYCLE ; 
pageContext.setAttribute("NON_LIFESTYLE_LIFECYCLE",NON_LIFESTYLE_LIFECYCLE,PageContext.PAGE_SCOPE);
String LIFECYCLE = FundCategory.LIFECYCLE ; 
pageContext.setAttribute("LIFECYCLE",LIFECYCLE,PageContext.PAGE_SCOPE);
String LIFESTYLE = FundCategory.LIFESTYLE ; 
pageContext.setAttribute("LIFESTYLE",LIFESTYLE,PageContext.PAGE_SCOPE);
String GIFL = FundCategory.GIFL ; 
pageContext.setAttribute("GIFL",GIFL,PageContext.PAGE_SCOPE);
String PBA = FundCategory.PBA ; 
pageContext.setAttribute("PBA",PBA,PageContext.PAGE_SCOPE);
%>


<!--Report Summary Information-->
<div class="report_table">
    <div class="clear_footer"></div>
    <table class="report_table_content">
	 <thead>
		<tr>
			<th  class="val_str">
			  <b><bd:fundSeriesName location="${bobContext.contractSiteLocation}" property ="bobContext.currentContract"/></b>
			</th>
			<th  class="val_str">
			 Options With Assets
			</th>
			<th  class="val_str">
			  Participants Currently Invested
			</th>
			<th  class="cur align_center" >
			 Employee Assets($)
			</th>
			<th  class="cur align_center" >
			 Employer Assets($)
			</th>
			<th  class="cur align_center" >
			 Total Assets($)
			</th>
			<th  class="pct align_center">
			 % Of Total
			</th>
		</tr>
	</thead>
	<tbody>
<c:forEach items="${theReport.allocationTotals}" var="theAllocationTotals" varStatus="theAllocationTotalsIndex" >




			 

         <c:if test="${theAllocationTotalsIndex.index % 2 eq 0}"> 
			  <tr class="spec">
		 </c:if>
		 <c:if test="${theAllocationTotalsIndex.index % 2 ne 0}"> 
			 <tr>
		 </c:if>	
      
<c:if test="${theAllocationTotals.fundCategoryType == NON_LIFESTYLE_LIFECYCLE}">
<td class="name"> ${investmentAllocationPageForm.totalText}</td>
</c:if>
	     
<c:if test="${theAllocationTotals.fundCategoryType == LIFECYCLE}">
		 <td class="name"> <%=InvestmentAllocationPageForm.LIFECYCLE_TEXT%></td>
</c:if>
	     
<c:if test="${theAllocationTotals.fundCategoryType == LIFESTYLE}">
		 <td class="name"> <%=InvestmentAllocationPageForm.LIFESTYLE_TEXT%></td>
</c:if>
		 
<c:if test="${theAllocationTotals.fundCategoryType == GIFL}">
		 <td class="name"> <%=InvestmentAllocationPageForm.GIFL_TEXT%></td>
</c:if>
	     
<c:if test="${theAllocationTotals.fundCategoryType == PBA}">
	     <td class="name"> <%=InvestmentAllocationPageForm.PBA_TEXT%></td>
</c:if>
	     
	     <td class="val_num_cnt">
	        <report:number property="theAllocationTotals.numberOfOptions" defaultValue="0" type="i" />
	     </td>
	     
	     <td class="val_num_cnt">
	        <report:number property="theAllocationTotals.participantsInvested" defaultValue="0" type="i" />
	     </td>
	     
	     <td class="cur">
	        <report:number property="theAllocationTotals.employeeAssets" defaultValue="0.00" pattern="###,###,##0.00" />
	     </td>
	     
	     <td class="cur">
	        <report:number property="theAllocationTotals.employerAssets" defaultValue="0.00" pattern="###,###,##0.00" />
	     </td>
	     
	     <td class="cur">
	        <report:number property="theAllocationTotals.totalAssets" defaultValue="0.00" pattern="###,###,##0.00" />
	     </td>
	     
	     <td class="pct">
	        <report:number property="theAllocationTotals.percentageOfTotal" pattern="###.##%" defaultValue="0" scale="4" />
	     </td>
	   </tr>
</c:forEach>
	</tbody>
</table>
</div>
<!--End of Report Summary Information-->

<br/>

<!--.Start of Main Report-->
<div class="page_section_subheader">
  <h3><content:getAttribute id="layoutPageBean" attribute="body2Header"/></h3>
</div>

<bd:form  cssClass="margin-bottom:0;margin-top:0;" method="GET" action="/do/bob/investment/investmentAllocationReport/"  modelAttribute="investmentAllocationPageForm" name="investmentAllocationPageForm">

  <form:hidden path="selectedFundID"/>
  <form:hidden path="selectedFundName"/>
  <form:hidden path="actionDetail"/>   
     
<div class="report_table">
 <table class="report_table_content">
	<thead>
		<tr>
		    <th width="19%" class="val_str">
		       <report:sort formName="investmentAllocationPageForm" field="option" direction="asc"><bd:fundSeriesName location="${bobContext.contractSiteLocation}" property ="bobContext.currentContract"/></report:sort>
		    </th>
		    <th width="7%" class="val_str align_center">
			    <report:sort formName="investmentAllocationPageForm" field="class" direction="desc">Ticker Symbol</report:sort>
			</th>
			<th width="7%" class="val_str align_center">
			    <report:sort formName="investmentAllocationPageForm" field="class" direction="desc">Class</report:sort>
			</th>
			<th width="17%" class="val_str">
			  <c:if test="${investmentAllocationPageForm.asOfDateReportCurrent eq true}"> 
     		    <report:sort formName="investmentAllocationPageForm" field="participantsInvested" direction="dsc">Participants Invested (Current/Ongoing)</report:sort>
     	     </c:if>
		      <c:if test="${investmentAllocationPageForm.asOfDateReportCurrent eq false}"> 
     		     <report:sort formName="investmentAllocationPageForm" field="participantsInvested" direction="dsc">Participant Invested (Current)</report:sort>
     	      </c:if>
			</th>
			<th width="17%" class="cur align_center">
			    <report:sort formName="investmentAllocationPageForm" field="employeeAssets" direction="desc">Employee Assets($)</report:sort>
			</th>
			<th width="17%" class="cur align_center">
			    <report:sort formName="investmentAllocationPageForm" field="employerAssets" direction="desc">Employer Assets($)</report:sort>
			</th>
			<th width="14%" class="cur align_center">
			    <report:sort formName="investmentAllocationPageForm" field="totalAssets" direction="desc">Total Assets($)</report:sort>
		    </th>
			<th width="7%" class="pct align_center">
			    <report:sort formName="investmentAllocationPageForm" field="percentageOfTotal" direction="dsc">% Of Total</report:sort>
			</th>
		</tr>
	</thead>
 </table>
</div>
</bd:form>

 <%-- iterating through the Map<FundCategory, ArrayList<AllocationDetails>> --%>
<c:forEach items="${theReport.allocationDetails}" var="theAllocationDetails">
	
<c:set var="fundCategory" value="${theAllocationDetails.key}" />

		         
	<%-- fetching the list of AllocationDetails object from the map using the fundCategory key --%>	         
<c:set var="fundDetails" value="${theAllocationDetails.value}"/>

    <!--.Category description-->

	 <div class="report_table">
	
		<div class="page_section_subsubheader"  style="font: normal 12px verdana;" >
<h4>${fundCategory.categoryDesc}</h4>
		</div>
	
	 </div>
	
  <div class="report_table">
	<table class="report_table_content">
		<tbody>
		    <!-- iterating through the ArrayList<AllocationDetails>  -->
<c:forEach items="${fundDetails}" var="theFundDetail" varStatus="theFundDetailsIndex" >



				           
		    <c:if test="${theFundDetailsIndex.index % 2 eq 0}"> 
			     <tr class="spec">
		    </c:if>
		    <c:if test="${theFundDetailsIndex.index % 2 ne 0}"> 
			     <tr>
		   </c:if>   

				<td width="19%" class="name">
<c:if test="${fundCategory.categoryCode !='PB'}">
<c:if test="${investmentAllocationPageForm.asOfDateReportCurrent ==true}">
									<c:if test="${theFundDetail.rateType == SIG_PLUS_RATE_TYPE|| theFundDetail.rateType == CY1_RATE_TYPE || theFundDetail.rateType == CY2_RATE_TYPE}">
										<a href="#fundsheet"
											onMouseOver='self.status="JavaScript needed to open the fund page link"; return true'
											NAME='${theFundDetail.fundId}'
											onClick='FundWindow("<bd:fundLink fundIdProperty="theFundDetail.fundId" fundTypeProperty="theFundDetail.fundType" rateType ="theFundDetail.rateType" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>
											${theFundDetail.fundName} </a>
									</c:if>
									<c:if test="${theFundDetail.rateType != SIG_PLUS_RATE_TYPE && theFundDetail.rateType != CY1_RATE_TYPE && theFundDetail.rateType != CY2_RATE_TYPE}">
										<a href="#fundsheet"
											onMouseOver='self.status="JavaScript needed to open the fund page link"; return true'
											NAME='${theFundDetail.fundId}'
											onClick='FundWindow("<bd:fundLink fundIdProperty="theFundDetail.fundId" fundTypeProperty="theFundDetail.fundType" rateType ="bobContext.currentContract.defaultClass" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>
											${theFundDetail.fundName} </a>
									</c:if>
</c:if>
<c:if test="${investmentAllocationPageForm.asOfDateReportCurrent ==false}">
${theFundDetail.fundName}
</c:if>
</c:if>
<c:if test="${fundCategory.categoryCode =='PB'}">
${theFundDetail.fundName}<sup><b>&#134;</b></sup>
</c:if>
				</td>
		   
			<td  width="7%" class="val_num_cnt align_center">
			 <c:if test="${theFundDetail.tickerSymbol==''}">
		             -
		          </c:if>
		          <c:if test="${theFundDetail.tickerSymbol!=''}">
		        ${theFundDetail.tickerSymbol}
		             </c:if>
		        
		    </td>
		    
		    <td  width="7%" class="val_num_cnt align_center">
<c:if test="${fundCategory.categoryCode =='PB'}">
		             -
</c:if>
<c:if test="${fundCategory.categoryCode !='PB'}">
${theFundDetail.fundClass}
</c:if>
		    </td>

<c:if test="${investmentAllocationPageForm.asOfDateReportCurrent ==true}">
		    <td  width="17%" class="val_num_cnt">

               <c:if test="${theFundDetail.participantsInvestedCurrent +  theFundDetail.participantsInvestedFuture eq 0}"  >0</c:if>
               <c:if test="${theFundDetail.participantsInvestedCurrent +  theFundDetail.participantsInvestedFuture ne 0}"  >
                  <c:if test="${theFundDetail.participantsInvestedCurrent ge 0}">
<c:if test="${investmentAllocationPageForm.viewOption == VIEW_BY_ACTIVITY}">
${theFundDetail.participantsInvestedCurrent}/${theFundDetail.participantsInvestedFuture}&nbsp;
</c:if>
<c:if test="${investmentAllocationPageForm.viewOption != VIEW_BY_ACTIVITY}">
<a href="javascript:doViewDetails('${theFundDetail.fundId}','${theFundDetail.fundName}')">${theFundDetail.participantsInvestedCurrent}/${theFundDetail.participantsInvestedFuture}&nbsp;(details)</a>
</c:if>
                  </c:if>
               </c:if>
</c:if>

<c:if test="${investmentAllocationPageForm.asOfDateReportCurrent ==false}">
			 <td width="18%" class="val_num_cnt">
				  <c:if test="${theFundDetail.participantsInvestedCurrent gt 0}">
<c:if test="${investmentAllocationPageForm.viewOption == VIEW_BY_ACTIVITY}">
${theFundDetail.participantsInvestedCurrent}&nbsp;
</c:if>
<c:if test="${investmentAllocationPageForm.viewOption != VIEW_BY_ACTIVITY}">
<a href="javascript:doViewDetails('${theFundDetail.fundId}','${theFundDetail.fundName}')">${theFundDetail.participantsInvestedCurrent}&nbsp;(details)</a>
</c:if>
</c:if>
			 </td>
</c:if>

			<td  width="17%" class="cur">
			    <report:number property="theFundDetail.employeeAssets" defaultValue="0" pattern="###,###,##0.00" /><br/>
			</td>

			<td  width="17%" class="cur">
			    <report:number property="theFundDetail.employerAssets" defaultValue="0" pattern="###,###,##0.00" /><br/>
			</td>

			<td  width="14%" class="cur">
			   <report:number property="theFundDetail.totalAssets" defaultValue="0" pattern="###,###,##0.00" /><br/>
			</td>

			<td  width="7%" class="pct">
			   <report:number property="theFundDetail.percentageOfTotal" pattern="###.##%" defaultValue="0" scale="4" /><br/>
			</td>
		 </tr>
</c:forEach>
	 </tbody>
	</table>
   </div>
</c:forEach>
</c:if>  

<!-- End of Main Report -->

<!-- FootNotes and Disclaimer -->
<div class="footnotes">
		<dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
<c:if test="${bobContext.currentContract.PBA ==true}">
			  <dl><dd><content:getAttribute beanName="footnotePBA" attribute="text"/></dd></dl>
</c:if>
        <dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
		<div class="footnotes_footer"></div>
</div> 
<!--end of footnotes-->