<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 

<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionItem" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ParticipantVO" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData.MoneyTypeAmount" %>

<report:formatMessages scope="request"/>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<%
ContributionTransactionReportData theReport = (ContributionTransactionReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

<input type="hidden" name="pdfCapped" /><%--  input - name="contributionTransactionReportForm" --%>

<c:if test="${not empty theReport}">


<bd:form cssClass="margin-bottom:0;" method="post" modelAttribute="contributionTransactionReportForm" name="contributionTransactionReportForm" action="/do/bob/transaction/contributionTransactionReport/">
		
  <div id="summaryBox">
		    <h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
			<span class="name">
			Transaction type:<strong>Contribution</strong><br />
			
			Payroll Ending Date:<strong>
			<render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theReport.payrollEndingDate"/></strong><br />

	        Number of Participants:<strong>
<c:if test="${not empty theReport.numberOfParticipants}">
			 <report:number property="theReport.numberOfParticipants" type="i" />
</c:if><br /></strong>
			
			Invested Date:<strong><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theReport.transactionDate"/></strong><br /><br />

Transaction Number: <strong>${e:forHtmlContent(theReport.transactionNumber)}</strong><br />
	        <br />

<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${not empty theReport.totalEmployeeContribution}">
	        Employee Contributions: <strong><report:number property="theReport.totalEmployeeContribution" type="c"/></strong><br />
</c:if>
</c:if>

<c:if test="${theReport.hasEmployerContribution ==true}">
<c:if test="${not empty theReport.totalEmployerContribution}">
	         Employer Contributions: <strong><report:number property="theReport.totalEmployerContribution" type="c"/></strong><br />
</c:if>
</c:if>

<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${theReport.hasEmployerContribution ==true}">
<c:if test="${not empty theReport.totalContribution}">
	        Total Contributions:<strong><report:number property="theReport.totalContribution" type="c"/></strong><br />
</c:if>
</c:if>
</c:if>
		  </span>
 	</div>

<jsp:include page="/WEB-INF/global/displayContractInfoWithRothMessage.jsp"/>



<navigation:contractReportsTab />
		
<div class="page_section_subheader controls">
	<h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/></h3>

	<c:if test="${empty requestScope.isError}"> 
		<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
	    <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
	</c:if>
</div>	
	  
<div id="page_section_container_cont_det">		
	<table width="100%">
		<thead>
			<tr>
				<th width="21%" >Money Type </th>
				<th width="27%">Amount($)</th>
				<th width="32%">Money Type </th>
				<th width="20%">Amount($)</th>
			</tr>
<c:if test="${not empty theReport.moneyTypes}">
	           <c:set var="num" value="${fn:length(theReport.moneyTypes)/2}" />
	           <c:forEach begin="0" end="${num - 1}" var="i" step="1">
	             <c:set scope="page" var="theItem" value="${ theReport.moneyTypes[i] }"/>
	             <c:set scope="page" var="theItemR" value="${ theReport.moneyTypes[i+num] }"/>
	             <tr>
<td class="name" width="21%">${theItem.longDescription}</td>
	                <td class="cur" width="27%">
<c:if test="${not empty theItem.amount}">
					 <report:number property="theItem.amount" type="c" sign="false" defaultValue=""/>
</c:if></td>
<td class="cur" width="32%">${theItemR.longDescription}</td>
	                <td class="cur" width="20%">
<c:if test="${not empty theItemR.amount}">
					 <report:number property="theItemR.amount" type="c" sign="false" defaultValue=""/>
</c:if></td>
	              </tr>
	           </c:forEach>
</c:if>
		</thead>
    </table>
</div>
<div class="page_section_subheader controls">
	<h3><content:getAttribute id="layoutPageBean" attribute="body2Header"/></h3>
</div>

<div class="table_controls">
<div class="table_action_buttons"></div>
<div class="table_display_info">
<strong>
<report:recordCounter report="theReport" label="Participants"/>
</strong>
</div>
<div class="table_pagination">
  <strong><report:pageCounter arrowColor="black" report="theReport" formName="contributionTransactionReportForm"/> </strong>
  </div>
</div>

<div class="report_table">
	<div class="clear_footer"></div>
<div class="report_table">
	<div class="clear_footer"></div>
	  <table class="report_table_content">
      <thead>
		 <tr>
			<th width="14%" class="val_str">
			<report:sort field="<%=ContributionTransactionReportData.SORT_FIELD_NAME%>" direction="asc" formName="contributionTransactionReportForm">Name</report:sort></th>
<c:if test="${theReport.hasEmployeeContribution ==true}">
            <th width="14%" class="val_str align_center">
			<report:sort field="<%=ContributionTransactionReportData.SORT_FIELD_EMPLOYEE_CONTRIBUTION%>" direction="desc" formName="contributionTransactionReportForm">Employee Contributions($)</report:sort></th>
</c:if>
<c:if test="${theReport.hasEmployerContribution ==true}">
            <th width="14%" class="val_str align_center">
			<report:sort field="<%=ContributionTransactionReportData.SORT_FIELD_EMPLOYER_CONTRIBUTION%>" direction="desc" formName="contributionTransactionReportForm">Employer Contributions($)</report:sort></th>
</c:if>
<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${theReport.hasEmployerContribution ==true}">
            <th width="14%" class="val_str align_center">
			<report:sort field="<%=ContributionTransactionReportData.SORT_FIELD_TOTAL_CONTRIBUTION%>" direction="desc" formName="contributionTransactionReportForm">Total Contributions($)</report:sort></th>
</c:if>
</c:if>
        </tr>
	 </thead>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >


			<tbody>
				<tr class="spec">
				 <td width="14%" class="name">
				 <bd:link action="/do/bob/participant/participantAccount/" paramId="participantId" paramName="theItem" paramProperty="participant.id">
${theItem.participant.wholeName}
					     </bd:link><br/> 
				         <render:ssn property="theItem.participant.ssn"/></td>
<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${not empty theItem.employeeContribution}">
					<td width="14%" class="cur">
					<report:number property="theItem.employeeContribution" type="c" sign="false"/>
					</td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployerContribution ==true}">
					<td width="14%" class="cur">
<c:if test="${not empty theItem.employerContribution}">
					<report:number property="theItem.employerContribution" type="c" sign="false"/>
</c:if></td>
</c:if>

<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${theReport.hasEmployerContribution ==true}">
					     <td width="14%" class="cur">
<c:if test="${not empty theItem.totalContribution}">
							  <report:number property="theItem.totalContribution" type="c" sign="false"/>
</c:if>
					    </td>
</c:if>
</c:if>
			        </tr>		
			        </tbody>
</c:forEach>
</c:if>
	
</table>
 
<div class="report_table_footer"></div>
<div class="table_controls">
<div class="table_action_buttons"></div>
<div class="table_display_info">
<strong>
<report:recordCounter report="theReport" label="Participants"/>
</strong>
</div>
<div class="table_pagination">
  <strong><report:pageCounter arrowColor="black" report="theReport" formName="contributionTransactionReportForm"/> </strong>
  </div>
</div>
 </div>
</div>

<layout:pageFooter/>
</bd:form>

</c:if>
