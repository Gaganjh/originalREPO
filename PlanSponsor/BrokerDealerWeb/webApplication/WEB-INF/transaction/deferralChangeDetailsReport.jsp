<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>


<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsDeferralReportData" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsDeferral" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);

TransactionDetailsDeferralReportData theReport = (TransactionDetailsDeferralReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 
<jsp:useBean id="deferralForm" scope="session" type="com.manulife.pension.bd.web.bob.transaction.DeferralChangeDetailsReportForm" />
<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<input type="hidden" name="deferralChangeDetailsForm.pdfCapped" />




<c:if test="${not empty details}">
	<%--#secondary_nav--%>
	<div id="summaryBox">
		<h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
		<span class="name">Transaction Type:</span>
		<span class="name"><strong> Deferral Update <br/></strong></span>
		<span class="name">Name:</span>
<span class="name"><strong>${details.fullName}<br/></strong></span>
		<span class="name">SSN:</span><span class="name"><strong><render:ssn property="details.ssn" /></strong></span>
	</div>
	<jsp:include page="/WEB-INF/global/displayContractInfo.jsp"/>
	<report:formatMessages scope="request"/>
	<%--#page_secondary_nav--%>
	<navigation:contractReportsTab/>

	<div class="page_section_subheader controls">
		<h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/></h3>
		<bd:form style="margin-bottom:0;" method="post" action="/bob/transaction/deferralChangeDetailsReport/" styleClass="page_section_filter form">
<p valign="middle">&nbsp;as of ${deferralForm.transactionDateFormatted}</p>
		</bd:form>
		<c:if test="${empty requestScope.isError}">
		 <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
         <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
		</c:if>
	</div>
	<%--#report_table begin--%>
	<div class="report_table">
		<div class="clear_footer"></div>
		<table class="report_table_content">
			<%--.report_table_header--%>
			<thead>
				<tr>
					<th width="13%" class="val_str" nowrap="nowrap">Requested Date&nbsp;</th>
				    <th width="27%" class="val_str">Item Changed</th>
				    <th width="10%" class="val_str align_center">Value Before </th>
				    <th width="10%" class="val_str align_center">Value Requested </th>
				    <th width="10%" class="val_str align_center">Value Updated </th>
				    <th width="10%" class="val_str">Status</th>
				    <th width="20%" class="val_str">Changed By </th>
				 </tr>
			</thead>
			<tbody>
        		<%-- Detail rows --%>
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/> 
<%String temp = pageContext.getAttribute("indexValue").toString();
TransactionDetailsDeferral itemVal = (TransactionDetailsDeferral)pageContext.getAttribute("theItem");
%>
<% if (Integer.parseInt(temp) % 2 != 0) { %> 
						<tr>
				   	<% } else { %>
						<tr class="spec">
				   	<% } %>
					<% if (itemVal.genSecondLine()) { %>
						<td width="13%" class="date" rowspan="2">
					<%}else{ %>
						<td width="13%" class="date">
					<%}%>
${theItem.requestedDate}</td>
<td width="27%" class="name">${theItem.itemChanged} <%-- filter="false" --%></td>
<td width="10%" class="pct">${theItem.valueBefore} <%-- filter="false" --%></td>
<td width="10%" class="pct">${theItem.valueRequested} <%-- filter="false" --%></td>
<td width="10%" class="pct">${theItem.valueUpdated} <%-- filter="false" --%></td>
<td width="10%" class="date">${theItem.status}</td>
					<%if(userProfile.isInternalUser()){%>
						<td width="20%" class="name" title="<%=itemVal.getProcessedByInternal()%>" >
					<%}else{ %>
						<td width="20%" class="name">
					<%} %>
${theItem.changedBy}</td>
				</tr>
				<% if (itemVal.genSecondLine()) { %>
					<tr class="spec">
<td colspan="6" class="name"><strong>${theItem.webComments} <%-- filter="false" --%></strong></td>
				</tr>
				<% } %>
</c:forEach>
			</tbody>
		</table>
	</div><%--.report_table--%>
	<layout:pageFooter/> <%--#footnotes--%>
</c:if> <%--#details_tag--%>
