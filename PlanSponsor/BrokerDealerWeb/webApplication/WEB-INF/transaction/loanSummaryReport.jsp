<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.platform.web.content.CommonContentConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryItem" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<%
LoanSummaryReportData theReport = (LoanSummaryReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 





<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<content:contentBean contentId="<%=CommonContentConstants.MISCELLANEOUS_LOAN_SUMMARY_NO_LOANS%>"
                                   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="NoLoansMessage"/>


<input type="hidden" name="pdfCapped" /><%--  input - name="loanSummaryReportForm" --%>
<!--The summary box is displayed in this section-->
<div id="summaryBox">
	<h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>		  

	<span class="name">Balance of Outstanding Loans :</span>
	<span class="name"><strong><report:number property="theReport.outstandingBalance" type="c"/><br/></strong></span>
	  
	<span class="name">Number of Loans:</span>
	<span class="name"><strong><report:number property="theReport.numLoans" type="i" /><br/></strong></span>
	  
	<span class="name">Number of Participants with Loans:</span>
	<span class="name"><strong><report:number property="theReport.numParticipants" type="i"/></strong></span>
		  
</div>
<!--End summary box-->

<!--Report Title-->
<jsp:include page="/WEB-INF/global/displayContractInfo.jsp"/>

<!--Error- message box-->  
<report:formatMessages scope="request"/>

<!--Navigation bar-->
<navigation:contractReportsTab />	

<!-- Report Section -->
<div class="page_section_subheader controls">
	<!-- Report Title Bar  -->			
	<h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/></h3>
	<bd:form  method="POST" action="/bob/transaction/loanSummaryReport/" cssClass="page_section_filter form" >
	<!-- Report As of Date -->
	<p> as of <render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" property="theReport.asOfDate"/></p>
	</bd:form>
	<!-- PDF & CSV icons -->
	<c:if test="${empty requestScope.isError}">
	 <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
     <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
	</c:if>
</div>

<!--Main Report-->
<div class="report_table">
<bd:form  method="POST" action="/do/bob/transaction/loanSummaryReport/" modelAttribute="loanSummaryReportForm" name="loanSummaryReportForm" > 

<!--Display the paging info -->
<c:if test="${not empty theReport.details}">
    <div class="table_controls">
	    <div class="table_action_buttons"></div>
	    <div class="table_display_info">
			<strong><report:recordCounter report="theReport" label="Loans"/></strong>
		</div>
	    <div class="table_pagination">
	    	<strong><report:pageCounter report="theReport" arrowColor="black" formName="loanSummaryReportForm" name="loanSummaryReportForm"/></strong>
	    </div>
	    <div class="table_controls_footer"></div> 
    </div>     
</c:if>
<!--End of paging info--> 
  
	<table class="report_table_content">
	             <thead>
                 <tr>
                   <th width="10%" class="val_str"><report:sort field="<%=LoanSummaryItem.SORT_NAME%>" direction="asc" formName="loanSummaryReportForm"><b>Name</b></report:sort></th>
                   <th width="10%" class="val_str"><report:sort field="<%=LoanSummaryItem.SORT_LOAN_NUMBER%>" direction="desc" formName="loanSummaryReportForm">Loan Number</report:sort></th>
                   <th width="11%" class="val_str align_center"><report:sort field="<%=LoanSummaryItem.SORT_ISSUE_DATE%>" direction="desc" formName="loanSummaryReportForm">Issue Date*</report:sort></th>
                   <th width="9%" class="val_str align_center"><report:sort field="<%=LoanSummaryItem.SORT_INTEREST_RATE%>" direction="desc" formName="loanSummaryReportForm">Interest Rate(%)</report:sort></th>
				   <th width="11%" class="val_str align_center"><report:sort field="<%=LoanSummaryItem.SORT_ORIGINAL_LOAN_AMT%>" direction="desc" formName="loanSummaryReportForm">Original Loan Amount($)*</report:sort></th>
                   <th width="10%" class="val_str align_center"><report:sort field="<%=LoanSummaryItem.SORT_OUTSTANDING_BALANCE%>" direction="desc" formName="loanSummaryReportForm">Outstanding Balance($)</report:sort></th>
                   <th width="11%" class="val_str align_center"><report:sort field="<%=LoanSummaryItem.SORT_LAST_REPAYMENT_AMT%>" direction="desc" formName="loanSummaryReportForm">Last Payment($)</report:sort></th>
                   <th width="11%" class="val_str align_center"><report:sort field="<%=LoanSummaryItem.SORT_LAST_REPAYMENT_DATE%>" direction="desc" formName="loanSummaryReportForm">Last Payment Date</report:sort></th>
                   <th width="10%" class="val_str align_center"><report:sort field="<%=LoanSummaryItem.SORT_MATURITY_DATE%>" direction="desc" formName="loanSummaryReportForm">Maturity Date</report:sort></th>
                   <th width="12%" class="val_str"><report:sort field="<%=LoanSummaryItem.SORT_ALERT%>" direction="desc" formName="loanSummaryReportForm">Alert</report:sort></th>
                 </tr>
               </thead>
	           <tbody>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >




		        <tr>                  
		          	<td class="val_str">
			          	<bd:link action="/do/bob/participant/participantAccount/" paramId="profileId" paramName="theItem" paramProperty="profileId">
${theItem.name}
						</bd:link><br/>
			                <render:ssn property="theItem.ssn"/>
					</td>
					<td class="val_str">
						<bd:map id="parameterMap">
							<bd:param name="task" value="filter"/>
							<bd:param name="loanNumber" valueBeanName="theItem" valueBeanProperty="loanNumber"/>
							<bd:param name="profileId" valueBeanName="theItem" valueBeanProperty="profileId"/>
						</bd:map> 		
						<bd:link action="/do/bob/transaction/loanRepaymentDetailsReport/" name="parameterMap">
${theItem.loanNumber}
							(details)
						</bd:link>
					</td>
		            <td class="date align_center">
		            	<render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" property="theItem.creationDate"/>
					</td> 
		            <td class="cur">
		              <report:number property="theItem.interestRate" pattern="###.##"/>
					</td>
					<td class="cur"> 
					  <report:number property="theItem.loanAmt" type="c" sign="false"/>
					</td>
		            <td class="cur">
				       <report:number property="theItem.outstandingBalance" type="c" sign="false"/>
					</td>
		            <td class="cur"> 
<c:if test="${not empty theItem.lastRepaymentAmt}">
<c:choose><c:when test="${theItem.isNoRepayment()}">n/a</c:when>
<c:otherwise><report:number property="theItem.lastRepaymentAmt" type="c" sign="false"/></c:otherwise>
</c:choose>
							<%--  <% if (theItem.isNoRepayment()) { %>   
									n/a
		                  	 <% } else { %>
			   		  				<report:number property="theItem.lastRepaymentAmt" type="c" sign="false"/>
							 <% } %> --%>
</c:if>
					</td>
		            <td class="date align_center">
<c:if test="${not empty theItem.lastRepaymentDate}">
<c:choose><c:when test="${theItem.isNoRepayment()}">n/a</c:when>
<c:otherwise>
<bd:map id="parameterMap">
						  		<bd:param name="task" value="filter"/>
								<bd:param name="transactionNumber" valueBeanName="theItem" valueBeanProperty="lastRepaymentTransactionNo"/>
								<bd:param name="transactionDate" valueBeanName="theItem" valueBeanProperty="lastRepaymentDate"/>
						  	</bd:map> 
		
		  				  	<bd:link action="/do/bob/transaction/loanRepaymentTransactionReport/" name="parameterMap"> 
								<render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" property="theItem.lastRepaymentDate"/>
							</bd:link>
</c:otherwise>
</c:choose>
					<%--   <% if (theItem.isNoRepayment()) { %>
	                        n/a
	                  <% } else { %> --%>
	   		  		  
						  	<%-- <bd:map id="parameterMap">
						  		<bd:param name="task" value="filter"/>
								<bd:param name="transactionNumber" valueBeanName="theItem" valueBeanProperty="lastRepaymentTransactionNo"/>
								<bd:param name="transactionDate" valueBeanName="theItem" valueBeanProperty="lastRepaymentDate"/>
						  	</bd:map> 
		
		  				  	<bd:link action="/bob/transaction/loanRepaymentTransactionReport/" name="parameterMap"> 
								<render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.lastRepaymentDate"/>
							</bd:link>
						<% } %> --%>
</c:if>
					</td>
		            <td class="date align_center">
		            	<render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.maturityDate"/>
					</td>
		            <td class="val_str">
<c:forEach items="${theItem.alerts}" var="theAlert" >
${theAlert}<br>
</c:forEach>
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
	         <strong><report:recordCounter report="theReport" label="Loans" /></strong>
	    </div>
	    <div class="table_pagination">
		    <report:pageCounter report="theReport" arrowColor="black" formName="loanSummaryReportForm" name="loanSummaryReportForm" />
	    </div>
	    <div class="table_controls_footer"></div> 
	</div> 
</c:if>
<!--End of paging info-->

 
</bd:form>	
</div>	
<!--End of main report-->

<!--#footnotes-->
<layout:pageFooter/> 

