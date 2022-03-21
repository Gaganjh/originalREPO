<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionItem"%>
<%@ page
	import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionReportData"%>
<%@ page
	import="com.manulife.pension.bd.web.bob.transaction.LoanRepaymentTransactionReportForm"%>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>
<report:formatMessages scope="request"/>

<%
LoanRepaymentTransactionReportData theReport = (LoanRepaymentTransactionReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 

<input type="hidden" name="pdfCapped" />

<c:if test="${not empty theReport}">

<bd:form cssClass="margin-bottom:0;" method="post" modelAttribute="loanRepaymentTransactionReportForm" name="loanRepaymentTransactionReportForm"
	action="/do/bob/transaction/loanRepaymentTransactionReport/">

	<div id="summaryBox">
	<h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
	<span class="redText">Number of Participants: </span><span class="name"><strong>
	<report:number property="theReport.numberOfParticipants"
		defaultValue="0" pattern="########0" /></strong></span><span class="name"><br />
	<br />
	Transaction Date: </span><span class="name"><strong><render:date
		patternIn="yyyy-MM-dd"
		patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"
		property="<%=BDConstants.TRANSACTION_DATE%>" /></strong></span><span class="name"><br />
Transaction Number: </span><span class="name"><strong>${theReport.transactionNumber}<br />

	</strong></span><span class="name"><br />
	Total Repayment Amount: </span><span class="name"><strong>$<report:number
		property="theReport.totalRepaymentAmount" type="c" sign="false" /> <br />
	</strong></span><span class="name">Total Principal: </span><span class="name"><strong>$<report:number
		property="theReport.totalPrincipalAmount" type="c" sign="false" /><br />
	</strong></span><span class="name">Total Interest: </span><span class="name"><strong>$<report:number
		property="theReport.totalInterestAmount" type="c" sign="false" /></strong></span></div>

	<jsp:include page="/WEB-INF/global/displayContractInfo.jsp"/>
	<br />

	<navigation:contractReportsTab />

	<div class="page_section_subheader controls">
		<h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/></h3>
		<c:if test="${empty requestScope.isError}">
		 <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> 
		 <content:image contentfile="image" id="pdfIcon" /> </a>
    	 <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  
    	 title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
		</c:if>
	</div>

	<div class="report_table">
	<div class="table_controls">
	<div class="table_action_buttons"></div>
	<div class="table_display_info"><strong><report:recordCounter
		report="theReport" label="Participants"/></strong></div>
	<div class="table_pagination"><report:pageCounter arrowColor="black" report="theReport" formName="loanRepaymentTransactionReportForm"/></div>
	</div>
	<!--.table_controls-->
	<table class="report_table_content">
		<thead>
			<tr>
				<th class="val_str"><b><report:sort
					field="<%=LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME%>"
					direction="asc" formName="loanRepaymentTransactionReportForm">Name</report:sort></b></th>
				<th class="val_str"><b><report:sort
					field="<%=LoanRepaymentTransactionReportData.SORT_FIELD_LOAN_NUMBER%>"
					direction="desc" formName="loanRepaymentTransactionReportForm">Loan Number</report:sort></b></th>
				<th class="val_str align_center"><b><report:sort
					field="<%=LoanRepaymentTransactionReportData.SORT_FIELD_REPAYMENT%>"
					direction="desc" formName="loanRepaymentTransactionReportForm">Repayment Amount($)</report:sort></b></th>
				<th class="val_str align_center"><b><report:sort
					field="<%=LoanRepaymentTransactionReportData.SORT_FIELD_PRINCIPAL%>"
					direction="desc" formName="loanRepaymentTransactionReportForm" >Principal($)</report:sort></b></th>
				<th class="val_str align_center"><b><report:sort
					field="<%=LoanRepaymentTransactionReportData.SORT_FIELD_INTEREST%>"
					direction="desc" formName="loanRepaymentTransactionReportForm">Interest($)</report:sort></b></th>
			</tr>
		</thead>
		<tbody>
			<%--   detail rows start here   --%>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:choose>
<c:when test="${theIndex.index % 2 ==0}">
<tr>
</c:when>
<c:otherwise> <tr class="spec"> </c:otherwise>
</c:choose>

<% 				
		LoanRepaymentTransactionItem theItem = (LoanRepaymentTransactionItem)pageContext.getAttribute("theItem");
		pageContext.setAttribute("theItem", theItem,PageContext.PAGE_SCOPE);
%>


						<%-- name --%>
						<td valign="top"><bd:link
							action="/do/bob/participant/participantAccount/" paramId="participantId"
							paramName="theItem" paramProperty="participant.id">
${theItem.participant.lastName},
${theItem.participant.firstName}
						</bd:link><br/>
						<render:ssn property="theItem.participant.ssn" /></td>

						<%-- loan number --%>
						<bd:map id="parameterMap">
							<bd:param name="task" value="filter" />
							<bd:param name="loanNumber" valueBeanName="theItem"
								valueBeanProperty="loanNumber" />
							<bd:param name="participantId" valueBeanName="theItem"
								valueBeanProperty="participant.id" />
						</bd:map>
<td valign="top">${theItem.loanNumber}

							<bd:link action="/do/bob/transaction/loanRepaymentDetailsReport/"
								name="parameterMap">(details)
 		  		            </bd:link>
						</td>

						<%-- repayment amount --%>
<td align="right" valign="top"><c:if test="${not empty theItem.repaymentAmount}">

							<%-- reverse amount --%>
							<report:number
								value="<%= Double.toString(theItem.getRepaymentAmount().doubleValue()*-1) %>"
								type="c" sign="false" />
</c:if></td>

						<%-- principal amount --%>
<td valign="top" align="right"><c:if test="${not empty theItem.principalAmount}">

							<%-- reverse amount --%>
							<report:number
								value="<%= Double.toString(theItem.getPrincipalAmount().doubleValue()*-1) %>"
								type="c" sign="false" />
</c:if></td>

						<%-- interest amount --%>
<td colspan="2" valign="top" align="right"><c:if test="${not empty theItem.interestAmount}">

							<%-- reverse amount --%>
							<report:number
								value="<%= Double.toString(theItem.getInterestAmount().doubleValue()*-1) %>"
								type="c" sign="false" />
</c:if></td>

					</tr>
</c:forEach>
</c:if>
		</tbody>

	</table>
	<!--.report_table_content-->
	<div class="table_controls">
	<div class="table_action_buttons"></div>
	<div class="table_display_info"><strong><report:recordCounter
		report="theReport" label="Participants" /></strong></div>
	<div class="table_pagination"><report:pageCounter arrowColor="black" report="theReport" formName="loanRepaymentTransactionReportForm" name="loanRepaymentTransactionReportForm"/></div>
	</div>
	<!--.table_controls--></div>
<layout:pageFooter/>
</bd:form>
</c:if>
