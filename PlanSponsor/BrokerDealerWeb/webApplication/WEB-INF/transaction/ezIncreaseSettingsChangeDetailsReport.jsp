<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>


<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.EmployeeChangeHistoryACISettingsItem" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsACIReportData" %>

<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>







<jsp:useBean id="theForm" scope="session" type="com.manulife.pension.bd.web.bob.transaction.EziReportForm" />

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />





<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="csvIcon" />

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="pdfIcon" />

<input type="hidden" name="pdfCapped" /><%--  input - name="eziReportForm" --%>
<%
TransactionDetailsACIReportData theReport = (TransactionDetailsACIReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

<c:if test="${not empty theReport}">


</c:if>
<c:if test="${not empty details}">
	<div id="summaryBox">
		<h1><content:getAttribute id="layoutPageBean"
								  attribute="subHeader" />
		</h1>
		Transaction Type: <strong>JH EZincrease service change <br/></strong>
		<span class="name">Name:</span>
		<span class="name">
<strong>${details.fullName}<br/></strong></span>
		<span class="name">SSN: </span>
		<span class="name"><strong><render:ssn property="details.ssn" /></strong></span>
	</div>
	<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />
	<report:formatMessages scope="request" />
	<%--#page_secondary_nav--%>
	<navigation:contractReportsTab />
	<div class="page_section_subheader controls">
		<h3><content:getAttribute id="layoutPageBean"
								  attribute="body1Header" /></h3>
		<bd:form style="margin-bottom:0;" 
				 method="post"
				 action="/bob/transaction/eziReport/"
				 styleClass="page_section_filter form">
			<p valign="middle">&nbsp;as of 
${theForm.transactionDateFormatted}</p>

		</bd:form> 
		<c:if test="${empty requestScope.isError}">
		 <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  
		 		title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> 
		 		<content:image contentfile="image" id="pdfIcon" /> </a>
   		 <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  
   		 		title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> 
    			<content:image contentfile="image" id="csvIcon" /></a>
		</c:if>
	</div>
	<div class="report_table">
		<div class="clear_footer"></div>
		<table class="report_table_content"
			summary="ezIncrease Settings Change Details Report">
			<thead>
				<tr>
					<th width="35%" class="val_str">Item Changed</th>
					<th width="12%" class="val_str align_center">Value Before</th>
					<th width="12%" class="val_str align_center">Value After</th>
					<th width="41%" class="val_str">Changed By</th>
				</tr>
			</thead>
			<tbody>
				<%-- Detail rows --%>
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >



<c:set var="indexValue" value="${theIndex.index}"/> 
<%String temp = pageContext.getAttribute("indexValue").toString();
EmployeeChangeHistoryACISettingsItem theItem = (EmployeeChangeHistoryACISettingsItem)pageContext.getAttribute("theItem");%>
<% if (Integer.parseInt(temp) % 2 != 0) { %> 
							<tr>
						<% } else {%>
							<tr class="spec">
						<% } %>
<td width="35%" class="name">${theItem.itemChanged} <%-- filter="false" --%></td>

<td width="12%" class="date align_right">${theItem.valueBefore} <%-- filter="false" --%></td>

<td width="12%" class="date align_right">${theItem.valueAfter} <%-- filter="false" --%></td>

						<% if (userProfile.isInternalUser()) { %>
							<td colspan="2" title="<%=theItem.getProcessedByInternal()%>">
						<% } else { %>
							<td colspan="2">
						<% } %> 
${theItem.changedBy}</td>
					</tr>
</c:forEach>
			</tbody>
		</table>
	</div>
</c:if><%--.report_table--%>
<layout:pageFooter/><%--#footnotes--%>
