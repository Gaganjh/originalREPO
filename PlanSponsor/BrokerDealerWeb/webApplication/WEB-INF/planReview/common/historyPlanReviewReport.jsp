<%@ page import="com.manulife.pension.service.planReview.valueobject.PlanReviewRequestVO"%>
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
 <%@ page import="com.manulife.pension.service.planReview.report.PlanReviewReportHistoryReportData" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>

/**
 * Filters the report data again.
 */
function doPrintRequestDetails(requestId,contract)
{
	navigate("planReviewReportHistoryForm");
  var reportURL = new URL();
  reportURL.setParameter("task", "printRequest");
  reportURL.setParameter("contract", contract);
  reportURL.setParameter("requestId", requestId);
  location.href = reportURL.encodeURL();
}
function sortSubmit(sortfield, sortDirection) {

	document.forms['planReviewReportHistoryForm'].action = "/do/bob/blockOfBusiness/planReview/History/?task=sort";
	document.forms['planReviewReportHistoryForm'].elements['sortField'].value = sortfield;
	document.forms['planReviewReportHistoryForm'].elements['sortDirection'].value = sortDirection;
	navigate("planReviewReportHistoryForm","","");
	document.forms['planReviewReportHistoryForm'].submit();

}

function doSubmitHis() {

	//document.getElementById("task").value = "filter";
	document.forms['planReviewReportHistoryForm'].action = "/do/bob/blockOfBusiness/planReview/History/?task=search";
	var temp = $(".selectedValue").val();
	var temp2 = $(".selectedType").val();
	if(temp2 == "contractNumber"){
		document.forms['planReviewReportHistoryForm'].elements['contractNumber'].value = temp;
	}
	if(temp2 == "contractName"){
		document.forms['planReviewReportHistoryForm'].elements['contractName'].value = temp;
	}
	//document.forms['contractReviewReportHistoryForm'].action = "/do/bob/planReview/?task="
		//+ action;
//navigate("contractReviewReportHistoryForm","History","History");
	navigate("planReviewReportHistoryForm");
	//document.forms['planReviewReportHistoryForm'].submit();

}

</script>
<%
PlanReviewReportHistoryReportData theReport = (PlanReviewReportHistoryReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

<c:if test="${not empty theReport}">


</c:if>
<bd:form method="post" action="/bob/planReviewReportHistoryForm/History/">
	<div id="contentOuterWrapper">
	<div id="contentWrapper">
	<div id="rightColumn1" style="margin-top: 0px">
	${layoutPageBean.layer1.text}</div>
	<div id="contentTitle"><content:getAttribute id="layoutPageBean"
		attribute="name" /></div>
	<div><BR />
	<BR />
	</div>
	<div><br />
	<br />
	<br />
	</div>
	<div style="font: 1.6em/ 1.6em Georgia,"TimesNewRoman", Times, serif;font-weight:normal">
	<h1>Plan Review History</h1>
	</div>
	<div id="page_section_subheader">
	<div
		class="page_section_subheader controls page_section_subheader_increased_height">
	<table  width="100%">
		<tr>

			<td width="10%" style="color: #fff">Contract Number <br />
<form:select path="planReviewReportHistoryForm" cssClass="inputField selectedType" >

				<form:option value="<%=BDConstants.PR_HISTORY_SEARCH_BY_CONTRACT_NUMBER %>">Contract Number</form:option>
				<form:option value="<%=BDConstants.PR_HISTORY_SEARCH_BY_CONTRACT_NAME %>">Contract Name</form:option>
</form:select></td>

			
			
			
			
			<td width="15%" style="color: #fff">Value <br />
<input type="text" name="contractName" style="display: none" /> <input type="hidden" name="contractNumber" class="contractNumberInput"/>

<input type="hidden" name="contractName" class="contractNameInput"/> <input type="text" id="tempInput" class="inputField selectedValue" value="" size="10" maxlength="100" /></td>



			<td width="15%" style="color: #fff">Month-end <br />
<form:select path="planReviewReportHistoryForm" >

<form:options items="${planReviewReportHistoryForm.requestedMonthendDateList}"/>

</form:select></td>

			<!-- Calender funtionality yet to complete -->
			<td width="15%" style="color: #fff">Requests from <br />
<div style="float: left; width: 90px"><form:input path="fromDate" maxlength="10" size="8" id="regFromDate" /></div>


			<div style="float: left"><utils:btnCalendar
				dateField="regFromDate" calendarcontainer="calendarcontainer"
				datefields="datefields" calendarpicker="calendarpicker" /></div>
			</td>

			<td width="15%" style="color: #fff">Requests to <br />
<div style="float: left; width: 90px"><form:input path="toDate" maxlength="10" size="8" id="regToDate" /></div>


			<div style="float: left"><utils:btnCalendar
				dateField="regToDate" calendarcontainer="calendarcontainer1"
				datefields="datefields1" calendarpicker="calendarpicker1" /></div>
			</td>

			<td width="16%" style="color: #fff">Print Conf No. <br />
<form:input path="printConfirmNum" maxlength="10" size="10" cssClass="inputField" /></td>



<input type="hidden" name="task" id="task" />

				
<input type="hidden" name="pageRegularlyNavigated" id="pageRegularlyNavigated" />


			<td width="10%" style="vertical-align: middle">
			<div class="button_header" id="quickFilterSubmitDiv"
				style="margin: 15px 4px 2px 2px; float: left; vertical-align: middle">
			<input type="button"
				style="WIDT H: 50px; MARGIN-LEFT: 0px; font-size: 1em"
				value="Search" onclick="doSubmitHis('history')"></div>
			</td>

			<td align="right" valign="middle"></td>

		</tr>
	</table>
	</div>

<c:if test="${not empty theReport.details}">
		<div class="table_controls">
		<div class="table_action_buttons"></div>
		<div class="table_display_info_abs"><strong><report:recordCounter
			report="theReport" label="Contracts" totalOnly="false" /></strong></div>
		<div class="table_pagination"><strong><report:pageCounter
			arrowColor="black" report="theReport" formName="planReviewReportHistoryForm" /> </strong></div>
		</div>
</c:if>

	<div class=report_table>
	<table class=report_table_contents >
		<thead>
			<tr>
				<th class=val_str valign=bottom rowspan=2 width="138px"><report:sort
					field="requestedDate" direction="desc" formName="planReviewReportHistoryForm">
										Requested Date
			                             </report:sort></th>

				<th class=val_str valign=bottom rowspan=2 width="165px"><report:sort
					field="contractName" direction="desc" formName="planReviewReportHistoryForm">
										Contract Name
			                             </report:sort></th>

				<th class=val_str valign=bottom rowspan=2 width="92px"><report:sort
					field="contractNumber" direction="desc" formName="planReviewReportHistoryForm">
										Contract Number
			                             </report:sort></th>

				<th class=val_str valign=bottom rowspan=2 width="92px"><report:sort
					field="selectedReportMonthEndDate" direction="desc" formName="planReviewReportHistoryForm">
										 Month End
										  </report:sort></th>

				<th class=val_str valign=bottom rowspan=2 width="119px"><report:sort
					field="requestedBy" direction="desc" formName="planReviewReportHistoryForm">
										Requested By
										 </report:sort></th>

				<th class=val_str valign=bottom rowspan=2 width="129px"><report:sort
					field="requestedFor" direction="desc" formName="planReviewReportHistoryForm">
										Requested For
										 </report:sort></th>

				<th class=val_str valign=bottom rowspan=2 width="92px"><report:sort
					field="status" direction="desc" formName="planReviewReportHistoryForm">
										Status
										 </report:sort></th>

				<th class=val_str valign=bottom rowspan=2 width="92px"><report:sort
					field="outputType" direction="desc" formName="planReviewReportHistoryForm">
										Output Type
										 </report:sort></th>
			</tr>

		</thead>

		<div
			style="overflow-y: auto; width: 100%; max-height: 770px; border-bottom: #cac8c4 1px solid;">
		<tr class=report_table_content id="reportTable1">
		<tbody>
			<c:if test="${not empty BDConstants.REPORT_BEAN} }">
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" >


						
							<c:choose>
							
								<c:when test="${rowIndex % 2 == 0}">
									<tr>
								</c:when>
								<c:otherwise>
									<tr class="spec">
								</c:otherwise>
							</c:choose>


							<td class="name " width="222px"><a href="javascript://"
onclick="doOpenHistoryDetails('historyDetails')"> ${theItem.requestedDate}</a></TD>


							<td class="name " width="200px"><a href="javascript://"
								onclick="doPrintRequestDetails('${theItem.requestedId}','${resultItem.contractNumber}')">
${theItem.contractName} </a></TD>
							
<td class="name " width="112x">${theItem.contractNumber}</td>


<td class="name " width="112x">${theItem.selectedReportMonthEndDate}</td>


<td class="name " width="113x">${theItem.requestedBy}</td>

								
<td class="name " width="113x">${theItem.requestedFor}</td>


<td class="name " width="113x">${theItem.status}</td>


<td class="name " width="113x">${theItem.outputType}</td>


</c:forEach>
</c:if>
			</c:if>
		</tbody>
	</table>
	</tr>
	</div>
	</div>

<c:if test="${not empty theReport.details}">
		<div class="table_controls">
		<div class="table_action_buttons"></div>
		<div class="table_display_info_abs"><strong><report:recordCounter
			report="theReport" label="Contracts" /></strong></div>
		<div class="table_pagination"><strong><report:pageCounter
			arrowColor="black" report="theReport" formName="planReviewReportHistoryForm" /> </strong></div>
		</div>
</c:if></div>
	</div>
</bd:form>

<div class="footnotes">

<dl>
	<dd><content:pageFooter beanName="layoutPageBean" /></dd>
</dl>

<dl>
	<dd><content:pageDisclaimer beanName="layoutPageBean" index="-1" /></dd>
</dl>
<div class="footnotes_footer"></div>
</div>
