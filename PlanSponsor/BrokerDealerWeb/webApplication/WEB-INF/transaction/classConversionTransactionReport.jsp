<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsClassConversionReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.ClassConversionDetailsFund" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund" %>

<%-- Beans used --%>
<%
TransactionDetailsClassConversionReportData theReport = (TransactionDetailsClassConversionReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>



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

<input type="hidden" name="pdfCapped" /><%--  input - name="transactionDetailsClassConversionForm" --%>

<div id="summaryBox">
<h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
<span class="name">Transaction Type:</span> 
<span class="name"><strong>Class Conversion</strong></span><br /><br />

<span class="name">Name:</span> 
<span class="name"><strong>${theReport.participantName}</strong></span><br />

<span class="name">SSN:</span>
<span class="name"><strong>${theReport.participantSSN}</strong></span><br /><br />

<span class="name">Invested Date:</span>
<span class="name"><strong><render:date dateStyle="m" property="theReport.transactionDate" /></strong></span><br />

<span class="name">Request Date:</span>
<span class="name"><strong><render:date dateStyle="m" property="theReport.requestDate" /></strong></span><br /><br />

<span class="name">Total Amount Transferred Out:</span>
<span class="name">
<strong><report:number property="theReport.totalAmount" type="c" sign="true" /></strong><br />
</span>

<span class="name">Total Amount Transferred In:</span>
<span class="name">
<strong><report:number property="theReport.totalToAmount" type="c" sign="true" /></strong><br />
</span>

<span class="name">Transaction Number:</span>
<span class="name"><strong>${theReport.transactionNumber}</strong></span><br />

<span class="name">Submission Method: </span>
<span class="name"><strong>${theReport.mediaCode}</strong></span><br />

<span class="name">Source of Transfer:</span>
<span class="name"><strong>${theReport.sourceOfTransfer}</strong></span><br />

</div>

<!--Report Title-->
<jsp:include page="/WEB-INF/global/displayContractInfo.jsp"/>

<!--Error- message box-->  
<report:formatMessages scope="request"/><br>

<!--Navigation bar-->
<navigation:contractReportsTab />

<div class="page_section_subheader controls">
<h3><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></h3>

<c:if test="${empty requestScope.isError}">
	<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
	<a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
</c:if>

</div>

<div class="report_table">
<table class="report_table_content">
	<thead>
		<tr>
			<th width="26%" class="val_str">Investment Option</th>
			<th width="12%" class="cur">Transfer Out($)</th>
			<th width="12%" class="cur">Unit Value</th>
			<th width="13%" class="cur">Number Of Units</th>
            <th width="12%" class="cur">Transfer In($)</th>
            <th width="12%" class="cur">Unit Value</th>
			<th width="13%" class="cur">Number Of Units</th>
		</tr>
	</thead>
	<tbody>
    <% 
    int rowIndex = 1;
    String rowClass="";
    %>
<c:forEach items="${theReport.transferFromsAndTos}" var="category" >



     <%    
             if (rowIndex % 2 == 0) {
                      rowClass="";
               } else {
                      rowClass="spec";
               }
               rowIndex++;
      %>
    <tr class="<%=rowClass%>">
<td width="26%" class="name"><b>${category.groupName}</b></td>
    <td width="12%" class="name">&nbsp;</td>
	<td width="12%" class="name">&nbsp;</td>
	<td width="13%" class="name">&nbsp;</td>
	<td width="12%" class="name">&nbsp;</td>		
    <td width="12%" class="name">&nbsp;</td>		
    <td width="13%" class="name">&nbsp;</td>		
    </tr>
    
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >

          
 <%
ClassConversionDetailsFund fund = (ClassConversionDetailsFund)pageContext.getAttribute("fund");
%>


    <%   
    if (rowIndex % 2 == 0) {
               rowClass="";
       } else {
               rowClass="spec";
       }
        rowIndex++;
    %> 
   <tr class="<%=rowClass%>">
<td width="26%" class="name">${fund.name}</td>
    <% if (fund.getAmount() != null) { %>
    <td width="12%" class="cur"><report:number property="fund.amount" scale="2" sign="true"/></td>
    <td width="12%" class="cur"><report:number property="fund.displayUnitValue" scale="2" sign="true" /></td>
	<td width="13%" class="cur"><report:number property="fund.displayNumberOfUnits" scale="6" sign="true" /></td>
	<td width="12%" class="cur"><report:number property="fund.toAmount" type="c" sign="false"/></td>
    <td width="12%" class="cur"><report:number property="fund.displayToUnitValue" scale="2" sign="true" /></td>
    <td width="13%" class="cur"><report:number property="fund.displayToNumberOfUnits" scale="6" sign="false" /></td>
     <% } %>
    </tr>
</c:forEach>
</c:forEach>
    </tbody>
</table>
</div>

<br />

<div class="page_section_subheader controls">
<h3><content:getAttribute id="layoutPageBean" attribute="body2Header" /></h3>
</div>
<div class="report_table">
<table class="report_table_content">
	<thead>
		<tr>
			<th width="30%" class="val_str">Investment Option </th>
			<th width="30%" class="val_str">Money Type </th>
			<th width="10%" class="cur pct ">Amount($) </th>
			<th width="15%" class="cur">Unit Value </th>
			<th width="15%" class="cur pct ">Number Of Units </th>
		</tr>
	</thead>
	<tbody>
        <% rowIndex = 1; %>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="category" >



        <%    
              if (rowIndex % 2 == 0) {
                      rowClass="";
               } else {
                      rowClass="spec";
               }
               rowIndex++;
        %>

		<tr class="<%=rowClass%>">
<td width="30%" class="name"><B>${category.groupName}</B></td>
		<td width="30%" class="name">&nbsp;</td>
		<td width="10%" class="name">&nbsp;</td>
		<td width="15%" class="name">&nbsp;</td>
		<td width="15%" class="name">&nbsp;</td>			
		</tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >




           <%    
                  if (rowIndex % 2 == 0) {
                          rowClass="";
                   } else {
                          rowClass="spec";
                   }
                   rowIndex++;
            %>
           <tr class="<%=rowClass%>">
<td width="30%" class="name">${fund.name}</td>
<td width="30%" class="name">${fund.moneyTypeDescription}</td>
		   <td width="10%" class="cur pct "><report:number  property="fund.amount" sign="true" /></td>
 <%
 TransactionDetailsFund fund = (TransactionDetailsFund)pageContext.getAttribute("fund");
%>
		   <td width="15%" class="cur">
			<% if (fund.displayUnitValue()) { %> 
			<report:number property="fund.displayUnitValue" scale="2" sign="true" />
			<% if (!fund.displayNumberOfUnits()) { %>
            &#37; 
			<%
			   }
 		      }
            %>
			</td>

			<td width="15%" class="cur pct ">
			<%
				if (fund.displayNumberOfUnits()) {
			%>
           <report:number property="fund.displayNumberOfUnits" scale="6" sign="true" />
            <%
				}
			%>
			</td>

		    </tr>
</c:forEach>
</c:forEach>
</c:if>
	</tbody>
</table>
</div>
<!--.report_table-->

<layout:pageFooter/>
