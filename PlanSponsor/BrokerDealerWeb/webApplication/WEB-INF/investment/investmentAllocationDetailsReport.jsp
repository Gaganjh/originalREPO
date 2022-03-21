<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bd-logicext.tld" prefix="logicext"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationDetailsReportData" %> 
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetailsReportDetailVO" %> 
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<%
InvestmentAllocationDetailsReportData theReport = (InvestmentAllocationDetailsReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

    
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=BDContentConstants.FIXED_FOOTNOTE_PBA%>"
                     type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>"
                     id="footnotePBA"/>

<content:contentBean contentId="<%=BDContentConstants.WARNING_NO_PARTICIPANTS_INVESTED_IN_THE_FUND%>"
                     type="<%=BDContentConstants.TYPE_MESSAGE%>"
                     id="warningNoParticipantsInvestedInTheFund"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<content:contentBean contentId="<%=BDContentConstants.BOB_INV_ALLOCATION_SIG_PLUS_DISCLOSURE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        beanName="sigPlusDisclosure"/>

<input type="hidden" name="pdfCapped" />
  <!--Error- message box-->  
  <report:formatMessages scope="request"/><br/>
  
<!--The summary box is displayed in this section-->
<div id="summaryBox">
     <h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
	 <span class="name"> 
	 Fund Name: 
     <strong> 
<c:if test="${theReport.hideOngoingContributions ==false}">
	    <logicext:if name="theReport" property="summary.fundId" op="equal" value="PBA">
	     <logicext:or name="theReport" property="summary.fundId" op="equal" value="NPB" />
	      <logicext:then>
<span class="datacell1"><b>${theReport.summary.fundName}</b><sup><b>&#134;</b></sup></b></span>
	      </logicext:then>
	      <logicext:else>
<a href="#fundsheet" onMouseOver='self.status="Fund Sheet"; return true' NAME="${e:forHtmlAttribute(theReport.summary.fundId)}" onClick='FundWindow("<bd:fundLink fundIdProperty="theReport.summary.fundId" fundTypeProperty="theReport.summary.fundType" rateType ="bobContext.currentContract.defaultClass" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>${theReport.summary.fundName}</a>
	      </logicext:else>
	   </logicext:if>
</c:if>
<c:if test="${theReport.hideOngoingContributions ==true}">
			<span class="datacell1">
<b>${theReport.summary.fundName}</b>
			</span>
</c:if>
	 </strong><br/>
	 Class:
	  <logicext:if name="theReport" property="summary.fundId" op="equal" value="PBA">
	     <logicext:or name="theReport" property="summary.fundId" op="equal" value="NPB" />
	      <logicext:then>
	         <strong> Not applicable </strong>
	      </logicext:then>
	      <logicext:else>
<strong> ${theReport.summary.fundClass}</strong>
	      </logicext:else>  
	  </logicext:if>  
	  <br />
     Number of Participants Invested: <strong><report:number property='theReport.summary.participantsCount' defaultValue = "0"  pattern="########0"/></strong><br />
	 <br/>
	 Employee Assets: <strong><report:number property='theReport.summary.employeeAssetsTotal' defaultValue = "$0.00"   type="c"  /></strong><br />
	 Employer Assets: <strong><report:number property='theReport.summary.employerAssetsTotal'  defaultValue = "$0.00"   type="c"  /></strong><br />
	 Total Assets: <strong><report:number property='theReport.summary.assetsTotal' defaultValue = "$0.00"   type="c"  /></strong>
	 </span>
</div>
<!--End summary box-->
    
<!--Report Title-->
<jsp:include page="/WEB-INF/global/displayContractInfoWithRothMessage.jsp"/>
 
<c:if test="${theReport.jhiIndicatorFlg == true}">   
	<div id="signaturePlusDisclosure">
		<p><content:getAttribute attribute="text" beanName="sigPlusDisclosure"/></p>
	</div>
</c:if>

<!--Navigation bar-->
<navigation:contractReportsTab />

<!--Section title and Display preferences-->
<div class="page_section_subheader controls">
	 <h3><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> </h3>
	 <bd:form method="post" action="/do/bob/investment/investmentAllocationDetailsReport/" modelAttribute="investmentAllocationPageForm" name="investmentAllocationPageForm"
				cssClass="page_section_filter form">
          <p> as of  </p>
		  <bd:select name="investmentAllocationPageForm" property="<%=InvestmentAllocationReportData.FILTER_ASOFDATE_DETAILS%>"
	                 onchange="setFilterFromSelect(this);doFilter();">
 	                 <bd:dateOption name="<%=BDConstants.BOBCONTEXT_KEY%>"
  	                                property="currentContract.contractDates.asOfDate"
   	                                renderStyle="<%=RenderConstants.LONG_MDY%>"/>
    	             <bd:dateOptions name="<%=BDConstants.BOBCONTEXT_KEY%>"
     	                             property="currentContract.contractDates.monthEndDates"
      	                             renderStyle="<%=RenderConstants.LONG_MDY%>"/>
          </bd:select>
	</bd:form>
  <c:if test="${empty requestScope.isError}"> 	 
    <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
    <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
  </c:if>
</div>
<!--End of Section title and Display preferences-->

<!--Main Report-->
<bd:form  method="get" action="/do/bob/investment/investmentAllocationDetailsReport/" modelAttribute="investmentAllocationPageForm" name="investmentAllocationPageForm">
<div class="report_table">

<!--Display the paging info -->
<c:if test="${not empty theReport.details}">
    <div class="table_controls">
    <div class="table_action_buttons"></div>
    <div class="table_display_info">
         <strong><report:recordCounter report="theReport" label="Participants" /></strong>
    </div>
    <div class="table_pagination">
	    <report:pageCounter formName="investmentAllocationPageForm" report="theReport" arrowColor="black" />
    </div>
    <div class="table_controls_footer"></div> 
    </div>     
</c:if>
<!--End of paging info--> 

	<table class="report_table_content">
		<thead>
			<tr >
				<th width="14%" class="val_str">
				    <report:sort formName="investmentAllocationPageForm" field="name" direction="asc">Name</report:sort>
				</th>
				<th width="14%" class="val_str">
<c:if test="${theReport.hideOngoingContributions ==false}">
					 <report:sort formName="investmentAllocationPageForm" field="ongoingContributions" direction="desc">Ongoing Contributions</report:sort>
</c:if>
<c:if test="${theReport.hideOngoingContributions ==true}">
					 Ongoing Contributions 
</c:if>
				</th>
				<th width="14%" class="cur align_center">
				   <report:sort formName="investmentAllocationPageForm" field="employeeAssetsAmount" direction="desc">Employee Assets($)</report:sort>
				</th>
				<th width="14%" class="cur align_center">
				    <report:sort formName="investmentAllocationPageForm" field="employerAssetsAmount" direction="desc">Employer Assets($)</report:sort>
				</th>
				<th width="14%" class="cur align_center">
				    <report:sort formName="investmentAllocationPageForm" field="totalAssetsAmount" direction="desc">Total Assets($)</report:sort>
				</th>
			</tr>
		</thead>
		<tbody>
<c:if test="${empty theReport.details}">
			<tr class="spec">
				<td colspan="5"><content:getAttribute id="warningNoParticipantsInvestedInTheFund" attribute="text" /></td>
			</tr>
</c:if>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<%
AllocationDetailsReportDetailVO theItem =(AllocationDetailsReportDetailVO)pageContext.getAttribute("theItem");
%>

			   <c:if test="${theIndex.index % 2 eq 0}"> 
			          <tr class="spec">
		        </c:if>
		        <c:if test="${theIndex.index % 2 ne 0}"> 
			          <tr>
		        </c:if>	

				<td width="14%" class="name">
				    <bd:link action='<%="/do/bob/participant/participantAccount/?participantId="
                                            + theItem.getParticipantId()%>'>
${theItem.name}<br/>
					</bd:link>
					<render:ssn property='theItem.ssn' />
				</td>
				<td width="14%" class="date">
<c:if test="${theReport.hideOngoingContributions ==false}">
${theItem.ongoingContributions}
</c:if>
<c:if test="${theReport.hideOngoingContributions ==true}">
			        	    -
</c:if>
				</td>
				<td width="14%" class="cur">
					<report:number property='theItem.employeeAssetsAmount' defaultValue="0.00" pattern="###,###,##0.00" />
				</td>
				<td width="14%" class="cur">
					<report:number property='theItem.employerAssetsAmount' defaultValue="0.00" pattern="###,###,##0.00" />
				</td>
			    <td width="14%" class="cur">
					<report:number property='theItem.totalAssetsAmount' defaultValue="0.00" pattern="###,###,##0.00" />
				</td>
		  </tr>
</c:forEach>
</c:if>
	  </tbody>
  </table>

<!--Display the paging info -->
<c:if test="${not empty theReport.details}">
    <div class="table_controls">
    <div class="table_action_buttons"></div>
    <div class="table_display_info">
         <strong><report:recordCounter report="theReport" label="Participants" /></strong>
    </div>
    <div class="table_pagination">
	    <report:pageCounter formName="investmentAllocationPageForm" report="theReport" arrowColor="black" />
    </div>
    <div class="table_controls_footer"></div> 
    </div>  
</c:if>
<!--End of paging info-->

 </div>	
</bd:form>	
<!--End of main report-->	

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
