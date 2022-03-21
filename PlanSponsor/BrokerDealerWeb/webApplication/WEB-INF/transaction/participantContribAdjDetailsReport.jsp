<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsContribAdjReportData"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsFund"%>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.service.account.valueobject.FundGroup"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />
<report:formatMessages scope="request" />

<%-- Start of report summary --%>
<%
	TransactionDetailsContribAdjReportData theReport = (TransactionDetailsContribAdjReportData)request.getAttribute(BDConstants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%> 


<c:if test="${not empty theReport}">
     <content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

       <content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>


	<%-- Beans used --%>
	
<input type="hidden" name="pdfCapped" /><%--  input - name="participantContribAdjDetailsForm" --%>

	<div id="summaryBox">
		<h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
<c:if test="${bobContext.currentContract.definedBenefitContract ==false}">
			<span class="name">Name:</span>
			<span class="name">
<strong>${participantContribAdjDetailsForm.lastName}, ${participantContribAdjDetailsForm.firstName} </strong>
			</span> 
			<span class="name"><br/>SSN:</span>
			<span class="name">
				<strong>
${participantContribAdjDetailsForm.ssn}<br/><br/>
				</strong>
			</span> 
</c:if>
 		
 		<span class="name">Transaction Date:</span>
 		<span class="name">
 			<strong><render:date dateStyle="m" property="theReport.transactionDate"/><br/></strong>
 		</span> 
	
		<% if (theReport.displayPayrollEndDate()) { %>
<c:if test="${bobContext.currentContract.definedBenefitContract ==false}">
				<span class="name">Payroll Ending:</span>
</c:if>

<c:if test="${bobContext.currentContract.definedBenefitContract !=false}">
	    		<span class="name">Contribution Date:</span>
</c:if>

    		<span class="name">
    			<strong><render:date dateStyle="m" property="theReport.payrollEndDate" /><br /></strong>
			</span>
		<% } %>

		<span class="name">Total Amount:</span>
		<span class="name">
			<strong><report:number property="theReport.totalAmount" type="c" defaultValue="0.00"/><br /><br/></strong>
		</span>

		<span class="name">Transaction Number:</span>
		<span class="name">
<strong>${theReport.transactionNumber}<br/></strong>
		</span>
	</div>

	<jsp:include page="/WEB-INF/global/displayContractInfo.jsp"/>

	<navigation:contractReportsTab />

	<div class="page_section_subheader controls">
		<h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/></h3>
		<c:if test="${empty requestScope.isError}">
		 <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
    	 <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
   		</c:if>
	</div>

	<div class="report_table">
		<table class="report_table_content">
		<thead>
			<tr>
				<th width="35%" class="val_str">Investment Option </th>
				<th width="30%" class="val_str">Money Type </th>
				<th width="15%" class="val_str align_center">Amount($) </th>
				<th width="10%" class="val_str align_center">Unit Value </th>
				<th width="10%" class="val_str align_center">Number Of Units </th>
			</tr>
		</thead>
		<tbody>
		

<c:forEach items="${theReport.details}" var="category" varStatus="detailsIndex">

				<tr class="spec">
<td width="35%" class="name"><B>${category.groupName}</B></td>
					<td width="30%" class="name">&nbsp;</td>
					<td width="15%" class="cur">&nbsp;</td>
					<td width="10%" class="cur">&nbsp;</td>
					<td width="10%" class="cur">&nbsp;</td>
				</tr>

<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex">
<%  TransactionDetailsFund fund = (TransactionDetailsFund)pageContext.getAttribute("fund");
	pageContext.setAttribute("fund",fund,PageContext.PAGE_SCOPE);
	System.out.print("fund"+fund);
%>

					<tr>
							<td width="35%" class="name">${fund.name}</td>
							<td width="30%" class="name">${fund.moneyTypeDescription}</td>
							<td width="15%" class="cur"><report:number property="fund.amount" type="c" sign="false" /></td>
							<td width="10%" class="cur">${fund.displayPsUnitValue}</td>
						<td width="10%" class="cur">
						
						<% if (fund.isGuaranteedAccount()) { %>
						&#8211;						
						<%}else if(fund.getNumberOfUnits()!= null && fund.getNumberOfUnits().doubleValue() != 0){ %>
						<report:number property="fund.numberOfUnits"  sign="false" scale="6" /> 
						<% }%>
						</td>
					</tr>
</c:forEach>
</c:forEach>
			<tr>
				<td width="35%" class="name">
					<div align="right"><b>Total amount:</b></div>
				</td>
				<td width="30%" class="name">&nbsp;</td>
				<td width="15%" class="cur"><report:number property="theReport.totalAmount" type="c" sign="false" defaultValue="0.00"/></td>
				<td width="10%" class="cur">&nbsp;</td>
				<td width="10%" class="cur">&nbsp;</td>
			</tr>
		</tbody>
		</table>
	</div>

<layout:pageFooter/>

</c:if>
