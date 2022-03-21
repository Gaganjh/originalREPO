<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsContributionReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsFund" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>



<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

 <content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<report:formatMessages scope="request" />

<input type="hidden" name="pdfCapped" /><%--  input - name="participantContributionDetailsForm" --%>

<c:if test="${not empty bdConstants.REPORT_BEAN}">

	<%-- Beans used --%>
<%
TransactionDetailsContributionReportData theReport = (TransactionDetailsContributionReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 


	<div id="summaryBox">
		<h1><content:getAttribute id="layoutPageBean"
			attribute="subHeader" />
		</h1>
	<%
	if (!bobContext.getCurrentContract().isDefinedBenefitContract()) {
	%> 
		<span class="name">Name:</span><span class="name"><strong>
${participantContributionDetailsForm.lastName}, ${participantContributionDetailsForm.firstName} </strong></span><span


			class="name"><br />
SSN: </span><span class="name"><strong> ${participantContributionDetailsForm.ssn}</strong></span><span

			class="name"><br />
		<br />
	<%
	}
	%> 
	Transaction Date:</span><span class="name"><strong> <render:date
		dateStyle="m" property="theReport.transactionDate" /><br />
	</strong></span> 
	 <%
	 if (!bobContext.getCurrentContract().isDefinedBenefitContract()) {
	 %> 
	 	<logicext:if name="theReport" property="hasPayrollEndDate"
		op="equal" value="true">
			<logicext:then>
				<span class="name">Payroll Ending:</span>
				<span class="name"><strong><render:date dateStyle="m"
					property="theReport.payrollEndDate" /></strong></span>
				<span class="name"></span><br />
			</logicext:then>
		</logicext:if> 
	<%
 	} else {
 	%>
	  <logicext:if name="theReport" property="hasPayrollEndDate"
			op="equal" value="true">
			<logicext:then>
				<span class="name">Contribution Date:</span>
				<span class="name"><strong><render:date dateStyle="m"
					property="theReport.payrollEndDate" /></strong></span><br />
			</logicext:then>
		</logicext:if> 
	<%
 	}
 	%> 
    <br />
 	<logicext:if name="theReport" property="moneySourceDescription"
		op="notEqual" value="">
		<logicext:then>
			<span class="name"> Contribution Type:</span>
<span class="name"><strong>${theReport.moneySourceDescription}</strong></span>

		</logicext:then>
		<logicext:else>
			<span class="name">&nbsp</span>;
			  			</logicext:else>
	</logicext:if> <br />
	
<c:if test="${theReport.hasEmployeeContribution ==true}">

		<span class="name">Contribution - Employee: </span>
		<span class="name"><strong> <report:number
			property="theReport.contributionEEAmount" type="c" /><br />
		</strong> </span>
</c:if>
	
<c:if test="${theReport.hasEmployerContribution ==true}">

		<span class="name">Contribution - Employer:</span>
		<span class="name"><strong> <report:number
			property="theReport.contributionERAmount" type="c" /><br /></strong></span>

</c:if>
	<br/>	
	<c:if test="${theReport.hasEmployeeContribution ==true && theReport.hasEmployerContribution==true}">
	
			<span class="name">Total Amount: </span>
			<span class="name"><strong><report:number
				property="theReport.totalContribution" type="c" />
			</strong></span>
			<br />
	</c:if>

<span class="name">Transaction Number:</span> <span class="name"><strong>${e:forHtmlContent(theReport.transactionNumber)}</strong></span></div>


	<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />

	<navigation:contractReportsTab />

	<div class="page_section_subheader controls">

	<h3><content:getAttribute id="layoutPageBean"
		attribute="body1Header" />
	</h3>
	<c:if test="${empty requestScope.isError}">
		<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
        <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
	</c:if>

	</div>
	
	<div class="report_table">
		<table class="report_table_content">
			<thead>
				<tr>
					<th width="35%" class="val_str">Investment Option</th>
					<th width="30%" class="val_str">Money Type </th>
					<th width="15%" class="val_str align_center">Amount($) </th>
					<th width="10%" class="val_str align_center">Unit Value </th>
					<th width="10%" class="val_str align_center">Number Of Units</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="category" varStatus="detailsIndex" >


						<tr class="spec">
<td class="name"><B>${category.groupName}</B></td>

							<td class="name">&nbsp;</td>
							<td class="cur">&nbsp;</td>
							<td class="cur">&nbsp;</td>
							<td class="cur">&nbsp;</td>
						</tr>
	
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >


	
							<tr>
<td width="35%" class="name">${fund.name}</td>

<td width="30%" class="name">${fund.moneyTypeDescription}</td>

								<td width="15%" class="cur"><report:number
									property="fund.amount" type="c" sign="false" /></td>
								<td width="10%" class="cur">
<c:if test="${fund.unitValue ==0}">
									  	-
</c:if>
<c:if test="${fund.unitValue !=0}">
										<report:number property="fund.unitValue" type="c" defaultValue="-" scale="2" sign="false"/>
</c:if>
						 	<c:choose><c:when test="${fund.isGuaranteedAccount()}">&#37;</c:when>
							</c:choose>	 
							<%-- 		<%
									TransactionDetailsFund transactionDetailsFund=new TransactionDetailsFund();
							   	  	boolean fundcheck=transactionDetailsFund.isGuaranteedAccount();  
									if (fund.isisGuaranteedAccount()) { %>
									&#37;
									<% } %> --%> 
									</td>
								<td width="10%" class="cur">
<c:if test="${fund.numberOfUnits ==0}">
									  	-
</c:if>
<c:if test="${fund.numberOfUnits !=0}">
										<report:number property="fund.numberOfUnits"  defaultValue="-" scale="6" sign="false"/>
</c:if>
								</td> 
							</tr>
</c:forEach>
</c:forEach>
	
					<tr>
						<td width="35%" class="name">
						<div align="right"><b>Total Amount:</b></div>
						</td>
						<td width="30%" class="name">&nbsp;</td>
						<td width="15%" class="cur"><report:number
							property="theReport.totalContribution" type="c" sign="false" /></td>
						<td width="10%" class="cur">&nbsp;</td>
						<td width="10%" class="cur">&nbsp;</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>

<layout:pageFooter/>
</c:if>
