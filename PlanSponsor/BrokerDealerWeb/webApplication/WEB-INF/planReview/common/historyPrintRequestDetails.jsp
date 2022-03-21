<%@ page
	import="com.manulife.pension.service.planReview.report.PlanReviewReportPrintHistoryDetailsReportData"%>
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.platform.web.CommonConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_PRINT_NO_RECORDS_TO_DISPLAY_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="noRecordsToDisplayMessage"/>

<script>

function sortSubmit(sortfield, sortDirection) {

	if ($('.bob_results').val() == 'bob') {
		document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/planReview/HistoryDetails/?task=sort";
		document.forms['planReviewHistoryDetailsReportForm'].elements['sortField'].value = sortfield;
		document.forms['planReviewHistoryDetailsReportForm'].elements['sortDirection'].value = sortDirection;
		navigate("planReviewHistoryDetailsReportForm");
	} else {
		document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/contract/planReview/HistoryDetails/?task=sort";
		document.forms['planReviewHistoryDetailsReportForm'].elements['sortField'].value = sortfield;
		document.forms['planReviewHistoryDetailsReportForm'].elements['sortDirection'].value = sortDirection;
		navigate("planReviewHistoryDetailsReportForm");
	}
}

function pagingSubmit(pageNumber) {
	if (document.forms['planReviewHistoryDetailsReportForm']) {
		
		if ($('.bob_results').val() == 'bob') {
			document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/planReview/HistoryDetails/?task=page";
		} else {
			document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/contract/planReview/HistoryDetails/?task=page";
		}
		
		document.forms['planReviewHistoryDetailsReportForm'].elements['pageNumber'].value = pageNumber;
		navigate("planReviewHistoryDetailsReportForm");
	}
}

</script>


<div>
	<ul class="proposal_nav_menu">
		<li><a class="selected_link exempt"><span style= "padding: 10px !important;" class=step_caption>Print
					Request Details</span></a></li>
	</ul>
</div>

<%
PlanReviewReportPrintHistoryDetailsReportData theReport = (PlanReviewReportPrintHistoryDetailsReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);                
%>
<c:if test="${not empty CommonConstants.REPORT_BEAN}">
<c:if test="${not empty theReport.details}">
	<div id=page_section_subheader>
		<div class="table_controls" style="border-top: #febe10 4px solid">
			<div class="table_action_buttons"></div>
			<div class="table_display_info_abs">
				<strong> <report:recordCounter report="theReport"
						label="Number of Print Requests :" totalOnly="false" />
				</strong>
			</div>
			<div class="table_pagination">
				<strong> <report:pageCounterViaSubmit report="theReport" formName="planReviewHistoryDetailsReportForm"
						arrowColor="black" />
				</strong>
			</div>
		</div>
</div>
</c:if>
</c:if>
<div class=report_table>
	<table class=report_table_content>
		<thead>
			<tr>
				<th class=val_str valign=bottom rowspan=2 width="138px"><report:bdSortLinkViaSubmit
						styleId="sortField" formName="planReviewHistoryDetailsReportForm"
						field="<%= PlanReviewReportPrintHistoryDetailsReportData.SORT_PRINT_REQUESTED_DATE %>"
						direction="<%= ReportSort.DESC_DIRECTION %>">
										Print Request Date
			                             </report:bdSortLinkViaSubmit></th>

				<th class=val_str valign=bottom rowspan=2 width="165px"><report:bdSortLinkViaSubmit
						styleId="sortField" formName="planReviewHistoryDetailsReportForm"
						field="<%= PlanReviewReportPrintHistoryDetailsReportData.SORT_PRINT_CONFIRMATION_NUMBER %>"
						direction="<%= ReportSort.DESC_DIRECTION %>">
										Print Confirmation #
			                              </report:bdSortLinkViaSubmit></th>

				<th class=val_str valign=bottom rowspan=2 width="92px"><report:bdSortLinkViaSubmit
						styleId="sortField" formName="planReviewHistoryDetailsReportForm" 
						field="<%= PlanReviewReportPrintHistoryDetailsReportData.SORT_PRINT_REQUEST_STATUS %>"
						direction="<%= ReportSort.ASC_DIRECTION %>">
										Print Request Status
			                             </report:bdSortLinkViaSubmit></th>

				<th class=val_str valign=bottom rowspan=2 width="92px"><report:bdSortLinkViaSubmit
						styleId="sortField" formName="planReviewHistoryDetailsReportForm"
						field="<%= PlanReviewReportPrintHistoryDetailsReportData.SORT_SHIPPED_DATE %>"
						direction="<%= ReportSort.DESC_DIRECTION %>">Date
						Shipped</report:bdSortLinkViaSubmit></th>

				<th class=val_str valign=bottom rowspan=2 width="119px">
					Shipping Details</th>
			</tr>
		</thead>

		
			
<c:if test="${not empty theReport.details}">
				<tbody>
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:set var="rowIndex" value="${theIndex.index}"/> 



						<c:choose>
							<c:when test="${rowIndex % 2 == 0}">
								<tr class="disabled forIterate">
							</c:when>
							<c:otherwise>
								<tr class="spec disabled forIterate">
							</c:otherwise>
						</c:choose>
<td class="name " width="222px">${theItem.formatedPrintRequestedDate}</TD>

<td class="name " width="200px">${theItem.printConfirmationNumber}</TD>


<c:if test="${theItem.resubmitable ==false}">
							<td class="name " width="112x"
title="${theItem.resubmitTitleText}">${theItem.printRequestStatus}</td>

</c:if>

<c:if test="${theItem.resubmitable ==true}">
							<td class="name " width="112x"
								title="${theItem.resubmitTitleText}">
${theItem.printRequestStatus}&nbsp;
								<a class="reSubmit" href="javascript://"
								onclick="doReSubmitPlanReviewRequest('${theItem.printActivityId}','${theItem.contractId}','${theItem.contractName}','${theItem.printRequestedDate}', 'reSubmitPlanReviewPrintRequest')"><img
									src="/assets/unmanaged/images/resubmitted.gif" width="10"
									height="12" border="0"></a></td>
</c:if>
<td class="name " width="112x">${theItem.formatedPrintShippedDate}</td>

	
							<td class="name " width="115px" style="text-align: center;" align="center"><a href="javascript://"
								Style="height: 17px; width: 17px; border: 0px;"
								onClick="doShippingDetails('${theItem.printActivityId}');"> <img
									src="/assets/unmanaged/images/icon_print.gif" border="0"
									height="17" width="17">
							</a></td>
						</tr>
</c:forEach>
					</tbody>
					</table>
</c:if>
<c:if test="${empty theReport.details}">
					</table>
					<div class="message message_info">
					    <dl>
					    <dt>Information Message</dt>
					      
					     	<dd>1.&nbsp;<content:getAttribute id="noRecordsToDisplayMessage" attribute="text"/></dd>
					     
					    </dl>
				  	</div>
</c:if>
			
		<!-- To be tested	<c:if test="${empty  CommonConstants.REPORT_BEAN}">
				</table>
				<div class="message message_info">
					    <dl>
					    <dt>Information Message</dt>
					      
					     	<dd>1.&nbsp;<content:getAttribute id="noRecordsToDisplayMessage" attribute="text"/></dd>
					     
					    </dl>
				  	</div>
			</c:if> -->
		<div>
		<c:if test="${not empty CommonConstants.REPORT_BEAN}">
<c:if test="${not empty theReport.details}">
				<div id=page_section_subheader>
					<div class="table_controls">
						<div class="table_action_buttons"></div>
						<div class="table_display_info_abs">
							<strong> <report:recordCounter report="theReport"
									label="Number of Print Requests :" totalOnly="false" />
							</strong>
						</div>
						<div class="table_pagination">
							<strong> <report:pageCounterViaSubmit report="theReport" formName="planReviewHistoryDetailsReportForm"
									arrowColor="black" />
							</strong>
						</div>
					</div>
				</div>
</c:if>
		</c:if>
	</div>
	<br/>
</div>

	<div Style="float: right;">
		<input class="blue-btn_big next"
			onmouseover="this.className +=' btn-hover'" id="blue-btn_big"
			onmouseout="this.className='blue-btn_big next'"
			onclick="doBackToMainHistory();" value="Back to Main History"
			type=button name=Back title='Cancel'>
	</div>
	<DIV id="shippingPanel"
		style="visibility: hidden; BACKGROUND-COLOR: #FBF9EE;"></DIV>
	<DIV id="viewDisableReasonPanel"
		style="visibility: hidden; BACKGROUND-COLOR: #FBF9EE;"></DIV>
