<%@ page import="com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<div id="confirmationBox" class="ui-helper-hidden ui-state-highlight">
	<div style="background-color: #455660; border-top: #febe10 4px solid;">
		<ul class="proposal_nav_menu">
			<li><a id="Requested_Printed_copies_sec" class="selected_link">
					<span style="padding-left: 10px"><strong>Print
							confirmation</strong></span>
			</a></li>
		</ul>
	</div>

	<div class=table_action_buttons></div>
	<div class=val_num_cnt
		style="text-align: left; padding-top: 27px; padding-left: 5px; padding-right: 5px; font: 12px verdana, arial, helvetica, sans-serif;">
		Your order has been received and will be fulfilled shortly. Please
		retain the order number(s) in case you have any questions about the
		order. The shipping status can be viewed in the history for this order
		under &#034;I want to&#034;. <br /> <br /> If you have any questions about your
		request please email jhplanreview@jhancock.com or call us at
		1-877-346-8378. <br />
	</div>

	<div class=report_table
		style="width: 95%; padding-left: 10px; padding-right: 10px; padding-top: 27px;">
		<table class=report_table_content
			style="margin-bottom: 0px; padding-top: 27px;">
			<tr>
				<th class="name printtd" style="width: 142px;">Request Date</th>
				<th class="name printtd" style="width: 266px;">Contract Name</th>
				<th class="name printtd" style="width: 61px;">Contract Number</th>
				<th class="name printtd" style="width: 99px;">Month End</th>
				<th class="name printtd">Print Confirmation #</th>
			</tr>
		</table>
	</div>
	<div style="align: left;">
		<div  class="report_table tbod "
			style="height: 300px; width: 95%; padding-left: 10px;">

			<div
				style="overflow-y: auto; width: 100%; max-height: 282px; border-bottom: #cac8c4 1px solid;">
				<table class="report_table_content" style="margin-bottom: 0px;">
					<c:choose>
							<c:when test="${rowIndex % 2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="">
							</c:otherwise>
						</c:choose>
<c:if test="${empty planReviewPrintForm.planPrintRequestedVOList}">
								<td colspan="4">No records to display</td></tr>
</c:if>
<c:if test="${not empty planReviewPrintForm.planPrintRequestedVOList}">

<c:forEach items="${planReviewPrintForm.planPrintRequestedVOList}" var="displayConformation" varStatus="indexValue" >

<c:set var="rowIndex"  value="${indexValue.index}"/>

						
<td class="name printtd" style="width: 147px;">${displayConformation.formattedRequestedDate}</td>

<td class="name printtd" style="width: 272px;">${displayConformation.contractName}</td>

<td class="name printtd" style="width: 67px;">${displayConformation.contractNumber}</td>

<td class="name printtd">${displayConformation.selectedReportMonthEndDate}</td>

<td class="name printtd " style="width: 113px;">${displayConformation.printConfirmNumber}</td>

						</tr>
</c:forEach>
</c:if>
				</table>
			</div>
		</div>
	</div>
	<div class="nextButton" id="enabledBottomNextButton"
		style="padding-top: 20px; padding-left: 11px;">
		<div align="left">
			<input class="blue-btn_big next "
				onmouseover="this.className +=' btn-hover'"
				onmouseout="this.className='blue-btn_big next'"
				onclick="doReturnToPrintPage();" value="Return to print request list"
				type=button name="return">
		</div>
	</div>
</div>

