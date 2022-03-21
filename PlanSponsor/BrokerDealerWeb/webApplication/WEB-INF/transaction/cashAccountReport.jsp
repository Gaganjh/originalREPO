<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.handler.TransactionType" %>
<%@ page import="com.manulife.pension.bd.web.bob.transaction.CashAccountReportForm" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountReportData" %>

<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountItem" %>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />


<jsp:useBean id="cashAccountReportForm" scope="session" type="com.manulife.pension.bd.web.bob.transaction.CashAccountReportForm" />

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);

CashAccountReportData theReport = (CashAccountReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 


<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<script language="javascript1.2" type="text/javascript">
	var selectRangeLimiter = null;
	
	function doFilterDateRange() {
	  doFilter();
	}
</script>

<c:if test="${not empty theReport}">





	<div id="summaryBox">
	   <h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
	   <span class="name">Current Balance as of: </span>
	   <span class="name">
			<strong><render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"
                        property="bobContext.currentContract.contractDates.asOfDate"/><br /></strong>
	   </span>
	   <span class="name">Current Balance: </span>
	   <span class="name">
			<strong><report:number property="theReport.currentBalance" type="c"/><br /></strong>
	   </span>
	   <span class="name">For Period: </span>
			 
	   <span class="name">
			<strong><render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"
                        property="cashAccountReportForm.fromDateInDateFormat"/></strong>
	   </span>
	   <span class="name"> to </span>
	   <span class="name">
			<strong><render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"
                        property="cashAccountReportForm.toDateInDateFormat"/><br /></strong>
	   </span> 

		<%-- Single Contract --%>
<c:if test="${theReport.hasMultipleContracts ==false}">
		   <span class="name"><br />Opening Balance:</span>
		   		<strong> <report:number property="theReport.openingBalanceForPeriod" type="c"/><br /></strong>
		   <span class="name">Closing Balance: </span>
		   		<strong><report:number property="theReport.closingBalanceForPeriod" type="c"/><br /></strong>
</c:if>
	   <span class="name"><br />Total Debits this Period:</span>
			<strong> <report:number property="theReport.totalDebitsForPeriod" type="c"/><br /></strong>
	   <span class="name">Total Credits this Period: </span>
			<strong><report:number property="theReport.totalCreditsForPeriod" type="c"/></strong>
	</div>
</c:if>

<jsp:include page="/WEB-INF/global/displayContractInfo.jsp"/>
<report:formatMessages scope="request"/>

<input type="hidden" name="pdfCapped" value="false" /><%--  input - name="cashAccountReportForm" --%>

<navigation:contractReportsTab />

<c:if test="${empty theReport}">
	<c:if test="${not empty displayDates}">
		<div class="page_section_subheader controls">
			<h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/></h3>
			<bd:form  method="post" action="/do/bob/transaction/cashAccountReport/" modelAttribute="cashAccountReportForm" name="cashAccountReportForm"
			cssClass="page_section_filter form"	>
			    <p>
				  <label for="kindof_filter">From:</label>
			    </p>
				<bd:select name="cashAccountReportForm" property="fromDate" onchange="setFilterFromSelect(this);">
					<bd:dateOptions name="<%=BDConstants.CASH_ACCOUNT_FROM_DATES %>"
				           	renderStyle="<%=RenderConstants.MEDIUM_STYLE%>"/>
			    </bd:select>
			
				<p>
				 <label for="kindof_filter">To:</label>
				</p>
				<bd:select name="cashAccountReportForm" property="toDate" onchange="setFilterFromSelect(this);">
			       <bd:dateOptions name="<%=BDConstants.CASH_ACCOUNT_TO_DATES %>"
			    			renderStyle="<%=RenderConstants.MEDIUM_STYLE%>"/>
				</bd:select>
								         
				<a class="buttonheader" href="javascript:doFilterDateRange();"><span>Search</span></a>
			</bd:form> 
			<c:if test="${empty requestScope.isError}">
			 <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
		     <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
			</c:if>
		</div>
	</c:if>
</c:if>


<c:if test="${not empty theReport}">
	<div class="page_section_subheader controls">
		<h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/></h3>
	<bd:form method="post" action="/do/bob/transaction/cashAccountReport/" modelAttribute="cashAccountReportForm" name="cashAccountReportForm" cssClass="page_section_filter form">

			
	
		<p><label for="kindof_filter">From:</label></p>
		<bd:select name="cashAccountReportForm" property="fromDate" onchange="setFilterFromSelect(this);">
			<bd:dateOptions name="<%=BDConstants.CASH_ACCOUNT_FROM_DATES%>" renderStyle="<%=RenderConstants.MEDIUM_STYLE%>"/>"
		</bd:select> 
		<p><label for="kindof_filter">To:</label></p>
				
				<bd:select name="cashAccountReportForm" property="toDate" onchange="setFilterFromSelect(this);">
			       <bd:dateOptions name="<%=BDConstants.CASH_ACCOUNT_TO_DATES%>"
			    			renderStyle="<%=RenderConstants.MEDIUM_STYLE%>"/>
				</bd:select>
		
		<a class="buttonheader" href="javascript:doFilterDateRange();"><span>Search</span></a>
			     
		</bd:form>  
			
	<c:if test="${empty requestScope.isError}">
		<a href="javascript://" onClick="doPrintPDF()" class="pdf_btn" title="Create PDF"><content:getAttribute beanName="pdfIcon" attribute="text"/></a>
		<a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_btn" title="Create CSV"><content:getAttribute beanName="csvIcon" attribute="text"/></a>
	</c:if>
	</div>
<c:if test="${empty theReport.details}">
		<table class="report_table_content">
		  <thead>
			<tr>
				  <th width="11%" class="val_str">Transaction Date</th>
				  <th width="23%" class="val_str">Type</th>
				  <th width="20%" class="val_str">Description</th>
				  <th width="11%" class="val_str align_center">Transaction Number</th>
				  <th width="14%" class="val_str align_center">Debits($)</th>
				  <th width="11%" class="val_str align_center">Credits($)</th>
				  <th width="10%" class="val_str align_center">Running Balance($)</th>
		    </tr>
 			  </thead>
		</table>

</c:if>
<c:if test="${not empty theReport.details}">
		<div class="table_controls">
			<div class="table_action_buttons"></div>
			<div class="table_display_info"><strong><report:recordCounter report="theReport" label="Total transactions:" totalOnly="true"/></strong></div>
		</div>

		<div class="report_table">
			<div class="report_table">
			  <table class="report_table_content">
				  <thead>
					<tr>
<c:if test="${theReport.hasMultipleContracts ==false}">
						  <th width="11%" class="val_str">Transaction Date</th>
						  <th width="23%" class="val_str">Type</th>
						  <th width="20%" class="val_str">Description</th>
						  <th width="11%" class="val_str align_center">Transaction Number</th>
						  <th width="14%" class="val_str align_center">Debits($)</th>
						  <th width="11%" class="val_str align_center">Credits($)</th>
						  <th width="10%" class="val_str align_center">Running Balance($)</th>
</c:if>
<c:if test="${theReport.hasMultipleContracts ==true}">
						  <th width="11%" class="val_str">Transaction Date</th>
						  <th width="23%" class="val_str">Type</th>
						  <th width="23%" class="val_str">Description</th>
						  <th width="14%" class="val_str align_center">Transaction Number</th>
						  <th width="16%" class="val_str align_center">Debits($)</th>
						  <th width="13%" class="val_str align_center">Credits($)</th>
</c:if>
				    </tr>
	  			  </thead>
				  <tbody>
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >



<%CashAccountItem theItem = (CashAccountItem)pageContext.getAttribute("theItem"); %>
<c:set var="indexValue" value="${theIndex.index}"/> 
<%String temp = pageContext.getAttribute("indexValue").toString();%>
<% if (Integer.parseInt(temp) % 2 != 0) { %> 
							<tr>
						   <% } else { %>
							<tr class="spec">
						   <% } %>
							  <td class="name">
							  		<render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"
		                         		property="theItem.transactionDate"/>
							  </td>
							  <td class="name">
				          		<%
				          		// this piece of code is kept as Java code to avoid the overhead of
				          		// Map and Link tags
				          		if (!theItem.isComplete() || (!theItem.getType().equals(TransactionType.ALLOCATION)
				          				&& !theItem.getType().equals(TransactionType.LOAN_REPAYMENT) ) ) 
				          		{%>
${theItem.typeDescription1}
				          		<%} else {
					          		StringBuffer queryString = new StringBuffer();
									boolean isDB = bobContext.getCurrentContract().isDefinedBenefitContract();
			
									if (theItem.getType().equals(TransactionType.ALLOCATION) ) { 
									    if (isDB) {
									        queryString.append("/do/bob/transaction/pptContributionDetailsReport/?");
									    } else {
					                	queryString.append("/do/bob/transaction/contributionTransactionReport/?");
					                	
					                	}
					                } else if (theItem.getType().equals(TransactionType.LOAN_REPAYMENT) )
					                {
					                	queryString.append("/do/bob/transaction/loanRepaymentTransactionReport/?");
									}              	
			
					          		queryString.append("task=filter").append("&");
					          		queryString.append("transactionNumber=").append(theItem.getTransactionNumber()).append("&");
					          		queryString.append("transactionDate=").append(theItem.getTransactionDate());
					          		if (isDB) {
					          		   queryString.append("&participantId=0");
					          		}
					          	%>
					          	<a href="<%=queryString.toString()%>"> <%=theItem.getTypeDescription1()%></a>
					          	<%}%>
				          		
<c:if test="${not empty theItem.typeDescription2}">
<br>${theItem.typeDescription2}
</c:if>
							  </td>
							  <td class="date">
							  		<bd:formatDescription item="theItem" linkParticipant="false" width="220" hideTransactionInProgress="true" />
							  </td>
<td class="val_str align_right">${theItem.transactionNumber}</td>
							  <td class="cur">
<c:if test="${not empty theItem.debitAmount}">
						              <report:number property="theItem.debitAmount" type="c" sign="false"/>
</c:if>
							  </td>
							  <td class="cur">
<c:if test="${not empty theItem.creditAmount}">
					                      <report:number property="theItem.creditAmount" type="c" sign="false"/>
</c:if>
							  </td>
<c:if test="${theReport.hasMultipleContracts ==false}">
<c:if test="${not empty theItem.runningBalance}">
									  <td class="cur">
						                  <report:number property="theItem.runningBalance" type="c" sign="false"/>
									  </td>
</c:if>
</c:if>
							</tr>
</c:forEach>
				  </tbody>
			  </table>
			  <div class="report_table_footer"></div>
	        </div><!--.report_table_content-->
			<div class="report_table_footer"></div>
		</div><!--.report_table-->
</c:if>

	<layout:pageFooter/> 
</c:if>            
<script language="javascript1.2" type="text/javascript">
  /*
   * The object must be initialized after the select's are initialized.
   */
  selectRangeLimiter = new SelectRangeLimiter('fromDate', 'toDate');
</script>			
