<%-- Tag Libraries used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>

<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%-- Imports used --%>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.manulife.pension.util.content.GenericException" %>
<%@ page import="com.manulife.pension.bd.web.BDErrorCodes" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@page import="com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType"%>
<%@page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%-- Constant Files used--%>
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />
<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />
<un:useConstants var="reportData"
	className="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData" />
<un:useConstants var="type"
	className="com.manulife.pension.ps.service.report.transaction.handler.TransactionType" />

<%-- Beans used --%>
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>



<content:contentBean
	contentId="${contentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="csvIcon" />

<content:contentBean
	contentId="${contentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="pdfIcon" />
	
	
<%
TransactionHistoryReportData theReport = (TransactionHistoryReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>




<jsp:include page="/WEB-INF/global/displayContractInfoWithRothMessage.jsp" />
<report:formatMessages scope="request"/>
<input type="hidden" name="pdfCapped" /><%--  input - name="transactionHistoryReportForm" --%>

<%-- Error message box --%>


<%-- Navigation Tab --%>
<navigation:contractReportsTab />

<%-- Suppress the page if errors are present --%>
<c:if test="${empty theReport}">
	<c:if test="${not empty displayDates}">
		<div class="page_section_subheader controls">
		<h3><content:getAttribute id="layoutPageBean" attribute="body1Header" /></h3>
		<form action="/bob/transaction/transactionHistoryReport/" class="page_section_filter form">
		<p><label for="fromDate">from</label></p>
		<bd:select name="transactionHistoryReportForm" property="fromDate" onchange="setFilterFromSelect(this);">
			<bd:dateOptions name="${bdConstants.TXN_HISTORY_FROM_DATES}" renderStyle="${renderConstants.LONG_MDY}" />
		</bd:select>
		<p><label for="kindof_filter">to</label></p>
		<bd:select name="transactionHistoryReportForm" property="toDate" onchange="setFilterFromSelect(this);">
			<bd:dateOptions name="${bdConstants.TXN_HISTORY_TO_DATES}" renderStyle="${renderConstants.LONG_MDY}" />
		</bd:select> 
        <a class="buttonheader" href="javascript:doFilter();"><span>Search</span></a>
		</form>
		</div>
	</c:if>
</c:if>
 
<%-- Display the page completely if no errors are there --%>
<c:if test="${not empty theReport}">



	<div class="page_section_subheader controls">
	<h3><content:getAttribute id="layoutPageBean" attribute="body1Header" /></h3>
	<form action="/do/bob/transaction/transactionHistoryReport/" class="page_section_filter form">
	<p><label for="fromDate">from</label></p>
	<bd:select name="transactionHistoryReportForm" property="fromDate" onchange="setFilterFromSelect(this);">
		<bd:dateOptions name="${bdConstants.TXN_HISTORY_FROM_DATES}" renderStyle="${renderConstants.LONG_MDY}" />
	</bd:select>
	<p><label for="kindof_filter">to</label></p>
	<bd:select name="transactionHistoryReportForm" property="toDate" onchange="setFilterFromSelect(this);">
		<bd:dateOptions name="${bdConstants.TXN_HISTORY_TO_DATES}" renderStyle="${renderConstants.LONG_MDY}" />
	</bd:select> 
    <a class="buttonheader" href="javascript:doFilter();"><span>Search</span></a>
	</form>
	<c:if test="${empty requestScope.isError}">
	 <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> 
	 		<content:image contentfile="image" id="pdfIcon" /> </a>
     <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> 
    		<content:image contentfile="image" id="csvIcon" /></a>
	</c:if>
    </div>

	<div class="report_table">
	
	<bd:form method="get" modelAttribute="transactionHistoryReportForm" name="transactionHistoryReportForm" action="/do/bob/transaction/transactionHistoryReport/">

		<%-- Paging information --%>
		<div class="table_controls">
		<div class="table_action_buttons"></div>
		<div class="table_display_info">
             <strong><report:recordCounter report="theReport" label="Transactions" /></strong>
        </div>
		<div class="table_pagination">
		     <report:pageCounter report="theReport" arrowColor="black" name="transactionHistoryReportForm" formName="transactionHistoryReportForm" />
        </div>
		</div>

		<table width="918" class="report_table_content" id="participants_table">
			<thead>
				<tr>
					<th width="14%" class="val_str">
					    <report:sort field="${reportData.SORT_FIELD_DATE}" direction="desc"  formName="transactionHistoryReportForm">Transaction Date</report:sort>
					</th>
					<th width="22%" class="val_str">
                        Type 
                        <bd:select name="transactionHistoryReportForm" property="transactionType" onchange="setFilterFromSelect(this);doFilter();">
						           <bd:options collection="transactionTypes" property="value" labelProperty="label" />
					    </bd:select>
                    </th>
					<th width="32%" class="val_str">Description</th>
					<th width="15%" class="val_str align_center">
					    <report:sort field="${reportData.SORT_FIELD_AMOUNT}" direction="desc"  formName="transactionHistoryReportForm">Amount($)</report:sort>
					</th>
					<th width="17%" class="val_str align_center">
					    <report:sort field="${reportData.SORT_FIELD_NUMBER}" direction="desc"  formName="transactionHistoryReportForm">Transaction Number</report:sort>
					</th>
				</tr>
			</thead>
			<tbody>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >





                       <c:if test="${theIndex.index % 2 == 0}"> 
						 <tr class="spec">
					   </c:if>
					   <c:if test="${theIndex.index % 2 != 0}"> 
						 <tr>
					   </c:if>	

							<%-- transaction date --%>
							<td class="name">
							<render:date patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" property="theItem.transactionDate" /></td>

							<%-- type line 1 --%>
<c:if test="${bobContext.currentContract.definedBenefitContract ==false}">
								<td class="name"><report:formatTransactionTypeLine1 item="theItem" />
</c:if>
<c:if test="${bobContext.currentContract.definedBenefitContract !=false}">
								<td class="name"><report:formatTransactionTypeLine1 dbContract="true" item="theItem" />
</c:if>

							<%-- type line 2 --%>
<c:if test="${not empty theItem.typeDescription2}">
								<br>
${theItem.typeDescription2}
</c:if>
							</td>

							<%-- description line 1, 2 & 3 --%>
							<td class="name" style="width : 300px"  >
                            <bd:formatDescription item="theItem" linkParticipant="true" showBobParticipantAccountLink="true" />
                            </td>

							<%-- amount --%>
							<td class="cur">
<c:if test="${theItem.type == type.ADJUSTMENT}">
<c:if test="${theItem.amount ==0}">
                                        n/a
</c:if>
<c:if test="${theItem.amount !=0.0}">
                                              <report:number property="theItem.amount" type="c" sign="false"/>
</c:if>
</c:if>
<c:if test="${theItem.type !=type.ADJUSTMENT}">
                                       <report:number property="theItem.amount" type="c" sign="false"/>
</c:if>
						   </td>

						   <%-- transaction number --%>
						  <td class="cur">
${theItem.transactionNumber}
                          </td>

</c:forEach>
</c:if>
			</tbody>
		</table>
<c:if test="${empty theReport.details}">
					<% 	List<GenericException> errors = new ArrayList<GenericException>();
						errors.add(new GenericExceptionWithContentType(BDContentConstants.MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED,
								ContentTypeManager.instance().MESSAGE,false));
						pageContext.setAttribute(BDConstants.INFO_MESSAGES, errors);
					%>
			
				   	<report:formatMessages scope="page"/>
</c:if>
		
        <%--.report_table end --%>

        <%--.paging info --%>
<c:if test="${not empty theReport.details}">
		<div class="table_controls">
		<div class="table_action_buttons"></div>
		<div class="table_display_info">
             <strong><report:recordCounter report="theReport" label="Transactions" /></strong>
        </div>
		<div class="table_pagination">
		     <report:pageCounter report="theReport" arrowColor="black" name="transactionHistoryReportForm"  formName="transactionHistoryReportForm"/>
        </div>
		</div>
</c:if>

	</bd:form>

    </div>
	

    <%--.footnotes --%>
	<layout:pageFooter/>
	<%-- #footnotes --%>

</c:if>
