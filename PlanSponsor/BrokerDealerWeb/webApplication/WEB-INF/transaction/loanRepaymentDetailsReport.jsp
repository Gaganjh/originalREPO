<%-- Taglibs used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="ps" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Imports used --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.service.account.valueobject.LoanHoldings" %>
<%@ page import="com.manulife.pension.service.account.valueobject.LoanGeneralInfoVO"%>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentDetailsReportData" %>
<%@ page	import="java.util.Set"%>
<%@ page	import="java.util.Iterator"%>
<%@ page	import="java.util.Map"%>
<%@ page	import="java.math.BigDecimal"%>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentDetailsItem"%>

<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>

<%-- Beans Defined --%>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<un:useConstants var="bdConstants" className="com.manulife.pension.bd.web.BDConstants" />
<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="bdContentConstants" className="com.manulife.pension.bd.web.content.BDContentConstants" />

<content:contentBean contentId="${bdContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT}"
        type="${bdContentConstants.TYPE_MISCELLANEOUS}"
        id="csvIcon"/>

<content:contentBean contentId="${bdContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT}"
        type="${bdContentConstants.TYPE_MISCELLANEOUS}"
        id="pdfIcon"/>
<%-- Start of report summary --%>
<%
LoanRepaymentDetailsReportData loanData =(LoanRepaymentDetailsReportData) request.getAttribute(BDConstants.LOAN_REPAYMENT_DETAILS_REPORT_DATA);
pageContext.setAttribute("loanData",loanData,PageContext.PAGE_SCOPE);

%>

<input type="hidden" name="pdfCapped" /><%-- input - name="loanRepaymentDetailsReportForm" --%>

<c:if test="${not empty loanData}">
	<%-- Beans used --%>
	<div id="summaryBox">
	  <h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
		<span class="name">Name:</span>
<span class="name"><strong>${loanData.name}<br /></strong></span>
		<span class="name">SSN:</span>
<span class="name"><strong> ${loanData.maskedSsn}</strong></span>
		<span class="name"><br /><br />
		
		
			Loan #<bd:number type="i" property="loanData.number"/> 
			<strong>
				<ps:map id="parameterMap">
					<ps:param name="profileId" valueBeanName="loanRepaymentDetailsReportForm" valueBeanProperty="profileId"/>
				</ps:map>
			</strong><br/>
	  <span class="name">Type of Loan:</span>
<c:if test="${loanData.loanOriginIndicator == bdConstants.LOAN_TYPE_INTERNAL}">
				<span class="name"><strong>Loan Issue </strong></span>
</c:if>
<c:if test="${loanData.loanOriginIndicator == bdConstants.LOAN_TYPE_TRANSFER}">
				<span class="name"><strong>Transfer Loan </strong></span>
</c:if>
			<br />
<c:if test="${not empty loanData.loanGeneralInfo}">
		<span class="name">Loan Reason:</span>
<c:if test="${not empty loanData.loanGeneralInfo.loanReason}">
		<span class="name"><strong>
<c:if test="${loanData.loanGeneralInfo.loanReason == 'PP'}">

							Purchase of Primary Residence
</c:if>
<c:if test="${loanData.loanGeneralInfo.loanReason == 'HD'}">

							Hardship
</c:if>
<c:if test="${loanData.loanGeneralInfo.loanReason == 'GP'}">

							General Purpose
</c:if>
<c:if test="${loanData.loanGeneralInfo.loanReason != 'PP'}">

<c:if test="${loanData.loanGeneralInfo.loanReason != 'HD'}">

<c:if test="${loanData.loanGeneralInfo.loanReason != 'GP'}">

              	 		 Unspecified
</c:if>
</c:if>
</c:if>
			</strong>
			</span>
			<br />
</c:if>
</c:if>
		<span class="name">Interest Rate:</span>
		<span class="name">
			<strong> <bd:number type="n" scale="2" pattern="###,##0.00" property="loanData.transferRate"/>%</strong>
		</span>
		<span class="name"><br />
<c:if test="${loanData.loanOriginIndicator == bdConstants.LOAN_TYPE_INTERNAL}">
  	    			Loan Issue Date:
</c:if>
			</span>
<c:if test="${loanData.loanOriginIndicator == bdConstants.LOAN_TYPE_TRANSFER}">
  	    		Transfer Date:
</c:if>
		</span>
	    <span class="name"><strong><render:date dateStyle="m" property="loanData.transferDate"/><br /></strong>
		<span class="name">Loan Maturity Date: </span>
		<strong> <render:date dateStyle="m" property="loanData.maturityDate"/> </strong>
		</span>
		 <br/>
		 <br/>
<c:set var="relation" value="${loanData.moneyTypeFunds}" />
         <c:set var="totalAmt" value="0" />	
<c:if test="${not empty loanData.moneyTypeFunds}">
				<table>
					<TR class="tablesubhead" valign="top">
						<TD align="left">Money Type</TD>
						<TD class="datadivider"><IMG width="1" height="1"
							src="/assets/unmanaged/images/s.gif"></TD>
						<TD align="right" nowrap >Amount ($)</TD>
						<TD class="databorder"><IMG width="1" height="1"
							src="/assets/unmanaged/images/s.gif"></TD>
					</TR>
					  <c:forEach var="relationMap" items="${relation}" >
						<TR valign="top">
<TD align="left" class="datacell1">${relationMap.key}</TD>
							<TD class="datadivider"><IMG width="1" height="1"
								src="/assets/unmanaged/images/s.gif"></TD>
							<TD class="datacell1"  align="right"><STRONG><render:number type="d" property="relationMap.value"/></STRONG></TD>
							<TD class="databorder"><IMG width="1" height="1"
								src="/assets/unmanaged/images/s.gif"></TD>
						</TR>
						  <c:set var="totalAmt" value="${totalAmt+relationMap.value}" />	
					</c:forEach>
					<TR valign="top">
						<TD align="left" class="datacell1">Total</TD>
						<TD class="datadivider"><IMG width="1" height="1"
							src="/assets/unmanaged/images/s.gif"></TD>
						<TD class="datacell1" align="right"><STRONG><render:number type="d" property="totalAmt"/></STRONG></TD>
						<TD class="databorder"><IMG width="1" height="1"
							src="/assets/unmanaged/images/s.gif"></TD>
					</TR>
				</table>
				<br/>
</c:if>
			
			<h1><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></h1>
		<span class="name">Outstanding Balance As Of:
		</span>
		<span class="name">
			<strong> <render:date dateStyle="m" property="loanData.inquiryDate"/><br /></strong>
		</span>
		<span class="name">Outstanding Balance Total: </span>
		<span class="name">
			<strong> <bd:number type="c" property="loanData.outstandingBalanceAmount"/> <br /></strong>
			</span>
			<span class="name">Last Payment Total: </span>
			<span class="name"><strong><bd:number type="c" property="loanData.lastRepaymentAmount"/> </strong></span>
			<span class="name"><br />Days Since Last Payment: </span>
			
			<strong>
				<span class="name">
<c:if test="${not empty loanData.lastRepaymentDate}">
						<strong><bd:number type="n" scale="0" defaultValue="0" property="loanData.daysSinceLastPayment"/></strong>
</c:if>
<c:if test="${empty loanData.lastRepaymentDate}">
						n/a 
</c:if>
				</span>
			</strong>
			<span class="name"><br/>Date of Last Payment: </span>
			<span class="name">
				<strong> 
<c:if test="${empty loanData.lastRepaymentDate}">
						n/a
</c:if>
<c:if test="${not empty loanData.lastRepaymentDate}">
						<render:date dateStyle="m" property="loanData.lastRepaymentDate"/> 
</c:if>
				</strong>
			</span>
			
			
	</div>
	 <br />
</c:if>
    <jsp:include page="/WEB-INF/global/displayContractInfo.jsp"/>
<c:if test="${not empty loanData}">
	<%-- message line if there are no detail items --%>
<c:if test="${empty loanData.items}">
		<div class="message message_info">
			<dl>
				<dt>Information Message</dt>
				<dd>There are no loan repayment details for this loan.</dd>
			</dl>
		</div>
</c:if>
			
</c:if>
<bd:formatMessages scope="session"/>
	<navigation:contractReportsTab />
			
<c:if test="${not empty loanData}">

	<div class="page_section_subheader controls">
		<h3><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></h3>
		<c:if test="${empty requestScope.isError}">
			<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
	        <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
		</c:if>
	</div>

	<div class="report_table">
		<div class="clear_footer"></div>
	    <table class="report_table_content">
			<thead>
				<tr>
				  <th width="17%" class="val_str">Date</th>
				  <th width="14%" class="val_str">Activity</th>
				  <th width="17%" class="val_str align_center">Amount($)</th>
				  <th width="18%" class="val_str align_center">Principal($)</th>
				  <th width="17%" class="val_str align_center">Interest($)</th>
				  <th width="17%" class="val_str align_center">Loan Balance($)</th>
			    </tr>
			</thead>
			<tbody>
<c:if test="${not empty loanData.items}">
<c:forEach items="${loanData.items}" var="theItem" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/> 
<%String temp = pageContext.getAttribute("indexValue").toString();
LoanRepaymentDetailsItem theItem = (LoanRepaymentDetailsItem)pageContext.getAttribute("theItem");
%>
	<% if (Integer.parseInt(temp) % 2 != 0) { %> 
							<tr class="datacell1">
						<%
							} else {
						%>
							<tr class="datacell2">
						<%
							}
						%>
								<td class="name"><render:date dateStyle="m" property="theItem.date"/></td>
								<td class="name"><%=theItem.getTypeDesc()%></td>
								<td class="cur"><bd:number type="d" property="theItem.amount" defaultValue="-"/></td>
								<td class="cur"><bd:number type="d" property="theItem.principal" defaultValue="-"/></td>
								<td class="cur"><bd:number type="d" property="theItem.interest" defaultValue="-"/></td>
								<td class="cur"><bd:number type="d" property="theItem.balance"/></td> 
							</tr>
</c:forEach>
</c:if>
			</tbody>
		</table>
	  </div>   

	<layout:pageFooter/> 
</c:if>
