<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.manulife.pension.util.content.GenericException" %>
<%@ page import="com.manulife.pension.bd.web.BDErrorCodes" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.handler.TransactionType" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryReportData" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.handler.TransactionTypeDescription" %>
<%@ page import="com.manulife.pension.bd.web.bob.transaction.ParticipantTransactionHistoryForm" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@page import="com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType"%>
<%@page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryItem" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />

<script type="text/javascript" >

function submitDates() {
			datesFilterForm = document.getElementById("datesFilterID");
			setFilterFromInput(datesFilterForm.elements['fromDate']);
			setFilterFromInput(datesFilterForm.elements['toDate']);
			doFilter('/do/transaction/participantTransactionHistory');
		}
function submitFilter(txnType) {
			datesFilterForm = document.getElementById("datesFilterID");
			setFilterFromInput(datesFilterForm.elements['fromDate']);
			setFilterFromInput(datesFilterForm.elements['toDate']);
			setFilterFromSelect(txnType);
			doFilter('/do/transaction/participantTransactionHistory');
		}

function doOnload() {
	var lastVisited = "${e:forJavaScriptBlock(param.lastVisited)}";
	var pageNumber = "${param.pageNumber}";
	var sortField = "${e:forJavaScriptBlock(param.sortField)}";
	if ((lastVisited == "true") || (pageNumber != "") || (sortField != "")) {
		location.hash = "participantName";
	}
}

</script>

<input type="hidden" name="pdfCapped" />

<form name="loanRepaymentDetails" method="POST" action="/do/transaction/loanRepaymentDetailsReport/" >
	<input type=hidden name="loanNumber"/>
	<c:if test="${not empty details}">
		<input type=hidden name="maskedSsn" value="<render:ssn property="details.ssn" />"/>
		<input type=hidden name="name" value="value="${details.lastName},${details.firstName}"/>
	</c:if>
	<input type=hidden name="profileId" value="${participantTransactionHistoryForm.profileId}"/>
</form>

<%-- <c:set var="layoutPageBean" value="layoutBean.layoutPageBean" scope="request"/> --%>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_TRANSACTION_HISTORY_WITHDRAWAL_MESSAGE%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="WithdrawalMessage"/>

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>
<%
TransactionHistoryReportData theReport = (TransactionHistoryReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 
<c:if test="${not empty theReport}">

</c:if>

<c:if test="${not empty details}">
<c:set var="details" value="${details}" scope="page" />

<div id="summaryBox">
	<h1><content:getAttribute id="layoutPageBean" beanName="layoutPageBean" attribute="subHeader" /></h1>
	<span id="participantName" class="name"> Name:</span>
	<span class="name">
<strong>${details.lastName},${details.firstName}<br />
</strong>
	</span>

	<span class="name">SSN:</span>
	<span class="name">
   		<strong><render:ssn property="details.ssn" /><br /></strong>
	</span>
</div>
</c:if>

<jsp:include page="/WEB-INF/global/displayContractInfoWithRothMessage.jsp"/>

<report:formatMessages scope="request"/>

<navigation:contractReportsTab />
	<div class="page_section_subheader controls">
		<h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/>&nbsp;</h3>
	 
		<c:if test="${empty theReport}">
			<c:if test="${not empty displayDates}">
				<c:set var="showSummarySection" value="true"/>
			</c:if>
		</c:if>
	
		<c:if test="${not empty theReport}">
			<c:set var="showSummarySection" value="true"/>
		</c:if>
	
		<c:if test="${showSummarySection eq true}">
			<bd:form cssClass="page_section_filter form" id="datesFilterID" modelAttribute="participantTransactionHistoryForm" name="participantTransactionHistoryForm" method="POST" action="/do/bob/transaction/participantTransactionHistory/">
				<p>from: </p>
				<input type="text" id="startDate" name="fromDate" size="9" value="${e:forHtmlAttribute(participantTransactionHistoryForm.fromDate)}" />
				<utils:btnCalendar  dateField="startDate" calendarcontainer="calendarcontainer" datefields="datefields"  calendarpicker="calendarpicker" calendarcontainer="calendarcontainer"/>
	
				 <p> to: </p>
				<input name="toDate" type="text" id="endDate" size="9" value="${e:forHtmlAttribute(participantTransactionHistoryForm.toDate)}"/>
				<utils:btnCalendar  dateField="endDate" calendarcontainer="calendarcontainer1" datefields="datefields1"  calendarpicker="calendarpicker1"/>
				<p> (mm/dd/yyyy)</p> 
				<a class="buttonheader" href="javascript:submitDates();"><span>Search</span></a>
			</bd:form>
	
			<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
            <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
		</c:if>
	</div>
	
	<bd:form   method="POST" modelAttribute="participantTransactionHistoryForm" name="participantTransactionHistoryForm" action="/do/bob/transaction/participantTransactionHistory/">
		<div class="report_table">
		
			<c:if test="${empty theReport}">
				<table class="report_table_content">
				<thead>
						<tr>
							<th width="16%" class="val_str">
								<report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE%>" direction="asc" formName="participantTransactionHistoryForm">
									Transaction Date
								</report:sort>
							</th>
		
							<th width="26%" class="val_str">
								<report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_TRANSACTION_TYPE%>" direction="asc" formName="participantTransactionHistoryForm">
		              				Type&nbsp;
		              			</report:sort> <br/>
		            			<bd:select property="transactionType" onchange="submitFilter(this);" name="participantTransactionHistoryForm">
		              				<bd:options collection="transactionTypes" property="value" labelProperty="label" />
		            			</bd:select>
							</th>
		
							<th width="18%" class="val_str align_center">Amount($)</th>
		
							<th width="22%" class="val_str align_center">
								<report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_PAYROLL_ENDING_DATE%>"direction="desc" formName="participantTransactionHistoryForm">
									Payroll Ending
								</report:sort>
							</th>
		
							<th width="18%" class="val_str align_center">Transaction Number</th>
						</tr>
			 		</thead> 
			  </table>
			
			</c:if>
			
		
	
				<c:if test="${not empty theReport}">
					<c:if test="${not empty theReport.details}">
					<div class="table_controls">
						<div class="table_action_buttons"></div>
						<div class="table_display_info_abs">
							<strong><report:recordCounter report="theReport" label="Transactions"/></strong>
						</div>
						<div class="table_pagination">
			  				<strong><report:pageCounter arrowColor="black" report="theReport" formName="participantTransactionHistoryForm" name="participantTransactionHistoryForm"/> </strong>
						</div>
					</div>
				</c:if>
	
				<table class="report_table_content">
					<thead>
						<tr>
							<th width="16%" class="val_str">
								<report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE%>" direction="asc" formName="participantTransactionHistoryForm">
									Transaction Date
								</report:sort>
							</th>
		
							<th width="26%" class="val_str">
								<report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_TRANSACTION_TYPE%>" direction="asc" formName="participantTransactionHistoryForm">
		              				Type&nbsp;
		              			</report:sort> <br/>
		            			<bd:select property="transactionType" onchange="submitFilter(this);" name="participantTransactionHistoryForm">
		              				<bd:options collection="transactionTypes" property="value" labelProperty="label" />
		            			</bd:select>
							</th>
		
							<th width="18%" class="val_str align_center">Amount($)</th>
		
							<th width="22%" class="val_str align_center">
								<report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_PAYROLL_ENDING_DATE%>"direction="desc" formName="participantTransactionHistoryForm">
									Payroll Ending
								</report:sort>
							</th>
		
							<th width="18%" class="val_str align_center">Transaction Number</th>
						</tr>
			 		</thead> 
					<c:if test="${not empty theReport.details}">
						<tbody>
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<%
TransactionHistoryItem theItem=(TransactionHistoryItem)pageContext.getAttribute("theItem");
pageContext.setAttribute("theItem",theItem,PageContext.PAGE_SCOPE);
%>
		        
								<%-- WITHDRAWAL --%>     
<c:if test="${theItem.type =='WD'}">
									<tr class="spec">
										<td width="16%" class="name">
											<render:date dateStyle="m" property="theItem.transactionDate"/>
										</td>
										
										<td width="26%" class="name">
											<c:if test="${theItem.displayChequeAmount != '-'}">*</c:if>
											<report:formatParticipantTransactionType item="theItem" />
										</td>
										
										<td width="18%">&nbsp;</td>

										<td width="22%" class="val_str align_center">
											<% if (theItem.displayPayrollDate()) { %>
												<render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.payrollEndingDate"/>
										  	<% } %>
									  	</td>
					
										<td width="18%" class="cur">
											<c:if test="${theItem.transactionNumber !=0}">
												${theItem.transactionNumber}
											</c:if>>
										</td>
									</tr>
									
									<tr>
										<td>&nbsp;</td>
						                <td><%= ParticipantTransactionHistoryForm.WITHDRAWAL_AMOUNT %></td>
										<td class="cur">
											<report:number property="theItem.amount" defaultValue="" pattern="###,###,##0.00;(###,###,##0.00)"/>
										</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									<c:if test="${theItem.displayChequeAmount !='-'}">
										<tr>
											<td>&nbsp;</td>
							                <td><%= ParticipantTransactionHistoryForm.DISTRIBUTION_AMOUNT %></td>
											<td class="cur">
												<report:number property="theItem.chequeAmount" defaultValue="" pattern="###,###,##0.00;(###,###,##0.00)"/>
											</td>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
									</c:if>
								</c:if>
								<%-- END WITHDRAWAL --%>
			
						        <c:if test="${theItem.type !='WD'}">
	
									<c:set var="isAllocationInstructionTxn" value="false" />
									<c:if test="${theItem.type eq 'CE' or theItem.type eq 'NE'}">
										<c:set var="isAllocationInstructionTxn" value="true" />
									</c:if>
	
									<c:set var="isDeferralTxn" value="false" />
									<c:if test="${theItem.type eq 'AC1' or theItem.type eq 'AC2'}">
										<c:set var="isDeferralTxn" value="true" />
									</c:if>
	
									<tr class="spec">
										<td width="16%" class="name">
											<render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.transactionDate"/>
										</td>
										
										<td width="26%" class="name">
						                    <report:formatParticipantTransactionType item="theItem" />
						                    <%-- Type description line 2 --%>
						                    <c:if test="${not empty theItem.typeDescription2}">
												<br>${theItem.typeDescription2}
 										   </c:if>				
										</td>
										
										<td width="18%" class="cur">
											<c:if test="${isAllocationInstructionTxn eq true or isDeferralTxn eq true}">
												-
											</c:if>
											<c:if test="${isAllocationInstructionTxn eq false and isDeferralTxn eq false}">
												<report:number property="theItem.amount" defaultValue="" pattern="###,###,##0.00;(###,###,##0.00)"/> 
											</c:if>
										</td>
										
										<td width="22%" class="val_str align_center">
											<% if (theItem.displayPayrollDate()) { %>
												<render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.payrollEndingDate"/>
										  	<% } %>
									  	</td>
										<td width="18%" class="cur">
											<c:if test="${isAllocationInstructionTxn eq true or isDeferralTxn eq true}">
												-
											</c:if>
											<c:if test="${isAllocationInstructionTxn eq false and isDeferralTxn eq false}">
												${theItem.transactionNumber}
											</c:if>
										</td>
									</tr>
						        </c:if>
							</c:forEach>
						</tbody>
					</c:if>
				</table>
				<c:if test="${empty theReport.details}">
					<% 	List<GenericException> errors = new ArrayList<GenericException>();
						errors.add(new GenericExceptionWithContentType(BDContentConstants.MESSAGE_NO_RESULTS_FOR_SEARCH_CRITERIA,
								ContentTypeManager.instance().MESSAGE,false));
						pageContext.setAttribute(BDConstants.INFO_MESSAGES, errors);
					%>
			
				    <report:formatMessages scope="page"/> 
</c:if>
			 
<c:if test="${not empty theReport.details}">
					<div class="table_controls">
						<div class="table_action_buttons"></div>
						<div class="table_display_info_abs">
							<strong><report:recordCounter report="theReport" label="Transactions"/></strong>
						</div>
						<div class="table_pagination">
							<strong><report:pageCounter arrowColor="black" report="theReport" formName="participantTransactionHistoryForm" name="participantTransactionHistoryForm"/> </strong>
						</div>
					</div>
</c:if>
			</c:if>
		</div>
	</bd:form>
	
	<div class="footnotes">
	
		<dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
		<c:if test="${not empty theReport}">
<c:if test="${theReport.hasWithdrawlDistribution ==true}">
				<dl><dd>*<content:getAttribute id="WithdrawalMessage" attribute="text" /></dd></dl>
</c:if>
		</c:if>
		<dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
		<div class="footnotes_footer"></div>
	</div>
