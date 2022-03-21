<%@page import="java.util.Date"%>
<%@ page
	import="com.manulife.pension.service.planReview.report.PlanReviewHistorySummaryReportData"%>
<%@ page
	import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>

<%@ taglib tagdir="/WEB-INF/tags/bob" prefix="bob"%>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
        


<content:contentBean
	contentId="<%=BDContentConstants.PLAN_REVIEW_REQUEST_BOB_LINK%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="PlanReviewRequestLink" />
<content:contentBean
	contentId="<%=BDContentConstants.PLAN_REVIEW_ENTER_SEARCH_CRITERIA%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="enterSearchCriteriaMessage" />
<content:contentBean
	contentId="<%=BDContentConstants.PLAN_REVIEW_HISTORY_NO_RECORDS_TO_DISPLAY_MESSAGE%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="noRecordsToDisplayMessage" />
<content:contentBean
	contentId="<%=BDContentConstants.CREATE_CSV_FILE%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="createCsv" />

<style>
.disabled {
	background-color: #B7B7B7;
	color: white;
}
</style>

<script>


$(document).ready(function(){

if($('#searchBy').val() == 'ContractName' || $('#searchBy').val() == 'ContractNumber'){	
	$("#searchVal").prop("readonly", false);
	}else{
	$('#searchVal').val("") ;
	$("#searchVal").prop("readonly", true);
	}
});

	function disableValue(){	
	if($('#searchBy').val() == 'ContractName' || $('#searchBy').val() == 'ContractNumber'){	
	$("#searchVal").prop("readonly", false);
		}else{
	$('#searchVal').val("") ;
	$("#searchVal").prop("readonly", true);
		}
	}
	
	function doDownloadHistoryCSV() {
		
		if (confirm(' I acknowledge that the accuracy of data downloaded outside of this application is the responsibility of the user and that neither John Hancock nor its affiliates shall be responsible for any inaccuracy resulting from such use.')) {
			document.forms['planReviewReportHistoryForm'].action = "/do/bob/planReview/History/?task=download";
			navigate("planReviewReportHistoryForm");
		}
	}

	/**
	 * Filters the report data again.
	 */
	function doHistoryReportDetails(requestId, activityId) {
		document.forms['planReviewReportHistoryForm'].elements['selectedPlanReviewActivityId'].value = activityId;
		document.forms['planReviewReportHistoryForm'].elements['selectedPlanReviewRequestId'].value = requestId;
		document.forms['planReviewReportHistoryForm'].action = "/do/bob/planReview/History/?task=historyReportDetails";
		navigate("planReviewReportHistoryForm");
	}
	
	function viewAllPlanReviewReports(requestId, selectedMonthEnd) {
		document.forms['planReviewReportHistoryForm'].action = "/do/bob/planReview/History/?task=ViewPlanReviewReportFromHistory";
		document.forms['planReviewReportHistoryForm'].elements['selectedPlanReviewRequestId'].value = requestId;
		navigate("planReviewReportHistoryForm");
	}
	
	function doPlanReviewRequest(action) {
		document.forms['planReviewReportHistoryForm'].action = "/do/bob/planReview/History/?task="
				+ action;
		navigate("planReviewReportHistoryForm");
	}

	function doResetHistory(action) {
		$(".resetFilter").each(function() {
			this.value = "";
		});
		document.forms['planReviewReportHistoryForm'].action = "/do/bob/planReview/History/?task="
			+ action;
	navigate("planReviewReportHistoryForm");	
		
	}
	
	function doSubmitHistory(action) {

		document.forms['planReviewReportHistoryForm'].action = "/do/bob/planReview/History/?task="
				+ action;
		navigate("planReviewReportHistoryForm");
	}
	
	function doReSubmitPlanReviewRequest(activityId, contractNum, contractName, requestedTS, action) {
		if (confirm("Before resubmitting, please ensure the issue that caused the initial request to fail has been resolved. Select OK to continue or Cancel to return.")) {
			document.forms['planReviewReportHistoryForm'].elements['selectedPlanReviewActivityId'].value = activityId;
			document.forms['planReviewReportHistoryForm'].elements['selectedPlanReviewContractId'].value = contractNum;
			document.forms['planReviewReportHistoryForm'].elements['selectedPlanReviewContractName'].value = contractName;
			document.forms['planReviewReportHistoryForm'].elements['selectedPlanReviewRequestedTS'].value = requestedTS;
			document.forms['planReviewReportHistoryForm'].action = "/do/bob/planReview/History/?task="
					+ action;
			navigate("planReviewReportHistoryForm");
		}
	}
	
	function sortSubmit(sortfield, sortDirection) {

		document.forms['planReviewReportHistoryForm'].action = "/do/bob/planReview/History/?task=sort";
		document.forms['planReviewReportHistoryForm'].elements['sortField'].value = sortfield;
		document.forms['planReviewReportHistoryForm'].elements['sortDirection'].value = sortDirection;
		
		navigate("planReviewReportHistoryForm");
	}

	function pagingSubmit(pageNumber) {
		if (document.forms['planReviewReportHistoryForm']) {

			document.forms['planReviewReportHistoryForm'].action = "/do/bob/planReview/History/?task=page";
			document.forms['planReviewReportHistoryForm'].elements['pageNumber'].value = pageNumber;

			navigate("planReviewReportHistoryForm");
		}
	}
</script>

<bd:form method="post" action="/do/bob/planReview/History/" modelAttribute="planReviewReportHistoryForm">
<form:hidden path="pageRegularlyNavigated"/>
<form:hidden path="selectedPlanReviewActivityId"/>
<form:hidden path="selectedPlanReviewContractId"/>
<form:hidden path="selectedPlanReviewContractName"/>
<form:hidden path="selectedPlanReviewRequestedTS"/>
<form:hidden path="sortField"/>
<form:hidden path="sortDirection"/>
<form:hidden path="pageNumber"/>
<form:hidden path="selectedPlanReviewRequestId"/>
<input type="hidden" name="requestHistorySummaryReport" value="true"/>

<%
				PlanReviewHistorySummaryReportData theReport = (PlanReviewHistorySummaryReportData)request.getAttribute(BDConstants.REPORT_BEAN);
                pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
                
				BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
				pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE); 
%>

<!-- Included report tag and error message -->
    <report:formatMessages scope="request"/>
    <!--<div id="errordivcs"><content:errors scope="session"/></div><br>--> 
    
	<div id="contentOuterWrapper">
		<div id="contentWrapper">
		
		<div id="rightColumnOverview">
			<h2><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h2>
			<ul>
				<li><a href="javascript://"
						onclick="doPlanReviewRequest('planReviewRequest')"> <content:getAttribute
							id="PlanReviewRequestLink" attribute="text" /></a></li>
			</ul>
		</div>
			<div id="contentTitle">
				<content:getAttribute id="layoutPageBean" attribute="name" />
			</div>
			<%--Layout/intro1--%>
			<c:if test="${not empty layoutPageBean.introduction1}">
			   	<p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>
			
			<%--Layout/Intro2--%>
			<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
			
			<!-- Report Erros/Warning Messages -->
			<!--<report:formatMessages scope="request" suppressDuplicateMessages="true"/>-->

			<div
				class="page_section_subheader controls page_section_subheader_increased_height">
				<table width="100%">
					<tr>

						<td width="10px" style="color: #fff">Search By<br /> 
<form:select path="selectedSearchByField" cssClass="inputField selectedType resetFilter" onchange="disableValue()" id="searchBy">



								<form:option
									value="<%=BDConstants.FILTER_BLANK_TITLE%>"></form:option>
									
									<form:option
									value="<%=BDConstants.PR_HISTORY_SEARCH_BY_CONTRACT_NUMBER%>">Contract number</form:option>
								<form:option
									value="<%=BDConstants.PR_HISTORY_SEARCH_BY_CONTRACT_NAME%>">Contract name</form:option>
</form:select>
							
						</td>

<td width="100px" style="color: #fff">Value<br /> <form:input path="selectedSearchByFieldValue" readonly="true" cssClass="selectedValue resetFilter" id="searchVal" />


						</td>

<td width="15px" style="color: #fff">Month End <br /> <form:select path="selectedReportMonthEndDate" cssClass="resetFilter" >


								<form:option value=""></form:option>
<form:options path="requestedMonthendDateList" itemLabel="label" itemValue="value"  items="${planReviewReportHistoryForm.requestedMonthendDateList}"/>

</form:select></td>

						<td width="110px" style="color: #fff">Requested From <br />
							<div style="float: left; width: 84px">
<form:input path="requestedFromDate" maxlength="10" readonly="true" size="8" cssClass="resetFilter" id="regFromDate" />


							</div>
							<div style="float: left">
								<utils:btnCalendar dateField="regFromDate"
									calendarcontainer="calendarcontainer1" datefields="datefields1"
									calendarpicker="calendarpicker1" />
							</div>
						</td>

						<td width="110px" style="color: #fff">Requested To <br />
							<div style="float: left; width: 84px">
<form:input path="requestedToDate" maxlength="10" readonly="true" size="8" cssClass="resetFilter" id="regToDate" />


							</div>
							<div style="float: left">
								<utils:btnCalendar dateField="regToDate"
									calendarcontainer="calendarcontainer" datefields="datefields"
									calendarpicker="calendarpicker" />
							</div>
						</td>

						<td width="140px" style="color: #fff">Print Confirmation No.<br />
<form:input path="printConfirmNumber" maxlength="6" size="10" cssClass="inputField resetFilter" /></td>


						<td width="140px" style="vertical-align: middle">
							<div class="button_header" id="quickFilterSubmitDiv"
								style="margin: 15px 4px 2px 2px; float: left; vertical-align: middle">
									
								<input type="button"
									style="WIDTH: 50px; MARGIN-LEFT: 0px; font-size: 1em"
									value="Reset" onclick="doResetHistory('reset')">
							</div>
						<div class="button_header" id="quickFilterSubmitDiv"
								style="margin: 15px 4px 2px 2px; float: left; vertical-align: middle">							
									<input type="button"
									style="WIDTH: 50px; MARGIN-LEFT: 0px; font-size: 1em"
									value="Search" onclick="doSubmitHistory('filter')">
							</div>
						</td>
						<td width="7px" style="vertical-align: middle">
						
						<c:if test="${empty requestScope.isError}"> 
						<a
							href="javascript://"
							onclick="doDownloadHistoryCSV();return false;"
							style="margin-right: 3px;" class="csv_icon"
							title="<content:getAttribute id="createCsv" attribute="text"/>"> <img
								src="/assets/generalimages/csv_btn.gif" border="0">
						</a></c:if></td>

					</tr>
				</table>
				<style>
					@media all and (-webkit-min-device-pixel-ratio:0) {
					div.page_section_subheader td{
						padding: 2px !important;
	                  }
					}
				</style>
			</div>
	<c:if test="${not empty theReport}">
		<c:if test="${not empty theReport.details}">
					<div class="table_controls">
						<div class="table_action_buttons"></div>
						<div class="table_display_info_abs">
							<strong><report:recordCounter report="theReport"
									label="Contracts" totalOnly="false" /></strong>
						</div>
						<div class="table_pagination">
							<strong>
							<report:pageCounterViaSubmit report="theReport"  formName="planReviewReportHistoryForm" arrowColor="black"/>
							</strong>
						</div>
					</div>
		</c:if>
</c:if>

			<div class=report_table>
				<table class=report_table_content>
					<thead>
						<tr>
							<!-- Table HEADER to display for Internal User -->
<c:if test="${planReviewReportHistoryForm.externalUserView == false}">

								<th class=val_str valign=bottom rowspan=2 width="138px">
								
								<report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm"
										field="<%= PlanReviewHistorySummaryReportData.SORT_REQUESTED_DATE %>"
										direction="<%= ReportSort.DESC_DIRECTION %>">
										Requested Date
			                             </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="165px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm" 
										field="<%= PlanReviewHistorySummaryReportData.SORT_CONTRACT_NAME %>">
										Contract Name
			                             </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="92px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm" 
										field="<%= PlanReviewHistorySummaryReportData.SORT_CONTRACT_NUMBER %>">
										Contract Number
			                             </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="92px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm" 
										field="<%= PlanReviewHistorySummaryReportData.SORT_REPORT_MONTHEND_DATE %>"
										page="">
										 Month End
										  </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="119px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm" 
										field="<%= PlanReviewHistorySummaryReportData.SORT_REQUESTED_BY %>">
										Requested By
										 </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="129px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm" 
										field="<%= PlanReviewHistorySummaryReportData.SORT_REQUESTED_FOR %>">
										Requested For
										 </report:bdSortLinkViaSubmit></th>


								<th class=val_str valign=bottom rowspan=2 width="92px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm" 
										field="<%= PlanReviewHistorySummaryReportData.SORT_REQUEST_STATUS %>">
										Status
										 </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="92px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm"
										field="<%= PlanReviewHistorySummaryReportData.SORT_OUTPUT_TYPE %>">
										Output Type
										 </report:bdSortLinkViaSubmit></th>
</c:if>

							<!-- Table HEADER to display for External User -->
<c:if test="${planReviewReportHistoryForm.externalUserView == true}">

								<th class=val_str valign=bottom rowspan=2 width="138px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm"
										field="<%= PlanReviewHistorySummaryReportData.SORT_REQUESTED_DATE %>"
										direction="<%= ReportSort.DESC_DIRECTION %>">
										Requested Date
			                             </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="165px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm"
										field="<%= PlanReviewHistorySummaryReportData.SORT_CONTRACT_NAME %>">
										Contract Name
			                             </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="110px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm"
										field="<%= PlanReviewHistorySummaryReportData.SORT_CONTRACT_NUMBER %>">
										Contract Number
			                             </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="110px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm"
										field="<%= PlanReviewHistorySummaryReportData.SORT_REPORT_MONTHEND_DATE %>">
										 Month End
										  </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="119px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm"
										field="<%= PlanReviewHistorySummaryReportData.SORT_REQUESTED_BY %>">
										Requested By
										 </report:bdSortLinkViaSubmit></th>
								<th class=val_str valign=bottom rowspan=2 width="100px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm"
										field="<%= PlanReviewHistorySummaryReportData.SORT_REQUEST_STATUS %>">
										Status
										 </report:bdSortLinkViaSubmit></th>

								<th class=val_str valign=bottom rowspan=2 width="100px"><report:bdSortLinkViaSubmit styleId="sortField" formName="planReviewReportHistoryForm"
										field="<%= PlanReviewHistorySummaryReportData.SORT_OUTPUT_TYPE %>">
										Output Type
										 </report:bdSortLinkViaSubmit></th>
</c:if>
						</tr>

					</thead>

<tbody>
 <c:if test="${not empty theReport}">

<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:set var="rowIndex" value="${theIndex.index}"/>


<c:if test="${theItem.viewDisabledRecord ==true}">

												<!-- Need to specify a disabled color style -->
												<tr class="spec disabled forIterate">
</c:if>
<c:if test="${theItem.viewDisabledRecord !=true}">

										<c:choose>
											<c:when test="${rowIndex % 2 == 0}">
												<tr class="forIterate">
											</c:when>
											<c:otherwise>
												<tr class="spec forIterate">
											</c:otherwise>
										</c:choose>
</c:if>

									<!-- Reprts which are completed status -->
									<c:if test="${theItem.status eq 'Resubmitted' or theItem.status eq 'Complete'}">
										<!-- Reprts which are in view disabled -->
<c:if test="${theItem.viewDisabledRecord ==true}">

<td class="name " width="222px"> ${theItem.formatedRequstedDate}</TD>

<td class="name " width="200px">${theItem.contractName}</TD>


<td class="name " width="112x">${theItem.contractId}</td>


<td class="name " width="112x">${theItem.formatedMonthEndDate}</td>


<td class="name " width="113x" title= "${theItem.requestedByUserProfileId }">${theItem.requestedByUserName}</td>

											<%-- <c:if test="${planReviewReportHistoryForm.externalUserView=='false'}">  --%>
<c:if test="${planReviewReportHistoryForm.externalUserView ==false}">

<td class="name " width="113x">${theItem.requestedForUserName}</td>

</c:if>
<td class="name " width="113x"  title="${theItem.viewDisabledText}">${theItem.status}</td>

											<td class="name " width="113x">
${theItem.outputType}</td>
</c:if> 

										<!-- Reprts which are in view status -->
<c:if test="${theItem.viewDisabledRecord !=true}">

											<td class="name " width="222px"><a href="javascript://"
												onclick="viewAllPlanReviewReports('${theItem.requestId}','${theItem.requestedDate}')"
title="View all Plan Review Reports in this request"> ${theItem.formatedRequstedDate}</a></TD>

<td class="name " width="200px">${theItem.contractName}</TD>


<td class="name " width="112x">${theItem.contractId}</td>


<td class="name " width="112x">${theItem.formatedMonthEndDate}</td>


<td class="name " width="113x" title= "${theItem.requestedByUserProfileId }">${theItem.requestedByUserName}</td>


<c:if test="${planReviewReportHistoryForm.externalUserView ==false}">

<td class="name " width="113x">${theItem.requestedForUserName}</td>

</c:if>
<td class="name " width="113x">${theItem.status}</td>

											<td class="name " width="113x">

		<a href="javascript://" onclick="doHistoryReportDetails('${theItem.requestId}', '${theItem.activityId}')"
				title="View details of the plan review for this contract only.">
${theItem.outputType}</a>

											</td> 
</c:if>
									</c:if>


									<!-- Reprts which are not in Complete status -->
									<c:if test="${theItem.status ne 'Resubmitted' and theItem.status ne 'Complete'}">

<td class="name " width="222px">${theItem.formatedRequstedDate}</TD>

<td class="name " width="200px">${theItem.contractName}</TD>

<td class="name " width="112x">${theItem.contractId}</td>


<td class="name " width="112x">${theItem.formatedMonthEndDate}</td>


<td class="name " width="113x" title= "${theItem.requestedByUserProfileId }">${theItem.requestedByUserName}</td>

<c:if test="${planReviewReportHistoryForm.externalUserView ==false}">

<td class="name " width="113x">${theItem.requestedForUserName}</td>

</c:if>

										<c:if test="${theItem.resubmitEnable eq 'false' and theItem.status eq 'Incomplete'}">
											
												<td class="name " width="113x"
											title="This request is incomplete. Our support team has been notified and is working to resolve the issue. For support, please email jhplanreview@jhancock.com or call 1-877-346-8378.">
${theItem.status} </td>

												
										</c:if>	
										<c:if test="${theItem.resubmitEnable eq 'false' and theItem.status eq 'In Progress'}">
												<td class="name " width="113x">
${theItem.status} </td>

										</c:if>		
<c:if test="${theItem.resubmitEnable ==true}">

											 	<td class="name " width="113x"
											title="${theItem.resubmitMouseHoverText}">
${theItem.status}
												<a class="reSubmit" href="javascript://"
													onclick="doReSubmitPlanReviewRequest('${theItem.activityId}','${theItem.contractId}','${theItem.contractName}','${theItem.formatedRequstedDate}','reSubmitPlanReviewRequest')"><img
													src="/assets/unmanaged/images/resubmitted.gif" width="10"
													height="12" border="0"></a> </td>
</c:if>

										<td class="name " width="113x">
${theItem.outputType}

										</td>

									</c:if>
									</tr>
</c:forEach>
</c:if>
 </c:if> 
					</tbody>
				</table>
				
				<c:if test="${not empty theReport}">
<c:if test="${empty theReport.details}">
								<div class="message message_info">
								    <dl>
								    <dt>Information Message</dt>
								    
								     	<dd>1.&nbsp;<content:getAttribute id="noRecordsToDisplayMessage" attribute="text"/></dd>
								     	
								    </dl>
							  	</div>
</c:if>
				</c:if>
				
				<c:if test="${ empty theReport}">
							<div class="message message_info">
								    <dl>
								    <dt>Information Message</dt>
								      
								     	<dd>1.&nbsp;<content:getAttribute id="enterSearchCriteriaMessage" attribute="text"/></dd>
								     
								    </dl>
							  	</div>
				</c:if>
			</div>
		</div>
<c:if test="${not empty theReport}">		
<c:if test="${not empty theReport.details}">
				<div class="table_controls">
					<div class="table_action_buttons"></div>
					<div class="table_display_info_abs">
						<strong><report:recordCounter report="theReport"
									label="Contracts" totalOnly="false" /></strong>
					</div>
					<div class="table_pagination">
						<strong><report:pageCounterViaSubmit arrowColor="black" formName="planReviewReportHistoryForm"
								report="theReport" /> </strong>
					</div>
				</div>
</c:if>
</c:if>
	</div>
</bd:form>

<c:set var="footNotes" value="${layoutBean.layoutPageBean.footnotes}"/>

<div class="footnotes">
    <div class="footer"><content:pageFooter beanName="layoutPageBean"/></div> 
    <br>    
    <c:if test="${not empty footNotes}"> 
    <dl>
      <dd><content:pageFootnotes beanName="layoutPageBean"/></dd> 
    </dl>
    </c:if>  
	<dl>
	<dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd>
	</dl>
	<div class="footnotes_footer"></div>
</div>
