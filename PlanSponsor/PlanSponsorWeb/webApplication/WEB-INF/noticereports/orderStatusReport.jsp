<%-- taglib used --%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page
	import="com.manulife.pension.ps.web.noticereports.AlertsReportForm"%>
<%@ page
	import="com.manulife.pension.ps.web.noticereports.OrderStatusReportController"%>
<%@ page
	import="com.manulife.pension.ps.service.report.noticereports.valueobject.OrderStatusReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.noticereports.valueobject.OrderStatusReportVO"%>
<%@ page import="com.manulife.util.piechart.PieChartBean" %>


<%
	OrderStatusReportData theReport = (OrderStatusReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
AlertsReportForm theForm = (AlertsReportForm)session.getAttribute("alertsReportForm");
pageContext.setAttribute("theForm",theForm,PageContext.PAGE_SCOPE);
%> 
<c:set var="printFriendly" value="${param.printFriendly}" />

<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_CONTROL_REPORTS_COMMON_PAGE_FROM_DATE_FORMAT_ERROR%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="fromDateFormatError"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_CONTROL_REPORTS_COMMON_PAGE_TO_DATE_FORMAT_ERROR%>"
                          	type="<%=ContentConstants.TYPE_MESSAGE%>"
                         	id="toDateFormatError"/>

<c:if test="${printFriendly != null }" >
<script>
	$(document).ready(function(){
		$(".pieChart img").removeAttr("title").removeAttr("alt").removeAttr("alt");
	});
</script>
</c:if>
<script type=text/javascript> 
var calFrom, calTo;
//Copied and modified from calendar.js
function cal_prs_monthday (str_date, suppressMessage) {
	var arr_date = str_date.split('/');

	if (arr_date.length != 2) return (suppressMessage ? null : cal_error ("Invalid date format: '" + str_date + "'.\nFormat accepted is mm/dd."));

	if (!arr_date[1]) return (suppressMessage ? null : cal_error ("Invalid date format: '" + str_date + "'.\nNo day of month value can be found."));
	if (!RE_NUM.exec(arr_date[1])) return (suppressMessage ? null : cal_error ("Invalid day of month value: '" + arr_date[1] + "'.\nValue must be numeric."));
	if (!arr_date[0]) return (suppressMessage ? null : cal_error ("Invalid date format: '" + str_date + "'.\nNo month value can be found."));
	if (!RE_NUM.exec(arr_date[0])) return (suppressMessage ? null : cal_error ("Invalid month value: '" + arr_date[0] + "'.\nValue must be numeric."));

	var dt_date = new Date();
	dt_date.setDate(1);

	if (arr_date[0] < 1 || arr_date[0] > 12) return (suppressMessage ? null : cal_error ("Invalid month value: '" + arr_date[0] + "'.\nAllowed range is 01-12."));
	dt_date.setMonth(arr_date[0]-1);

	var dt_numdays = new Date((new Date()).getYear(), arr_date[0], 0);
	dt_date.setDate(arr_date[1]);
	if (dt_date.getMonth() != (arr_date[0]-1)) return (suppressMessage ? null : alert ("Invalid day of month value: '" + arr_date[1] + "'.\nAllowed range is 01-"+dt_numdays.getDate()+"."));

	return (dt_date);
}

function formatDate(dateVal, includeYear) {
	var month = dateVal.getMonth() + 1;
	var day = dateVal.getDate();
	var dateStr = (month<10 ? "0" + month : month) + "/" + (day<10 ? "0" + day : day) + (includeYear ? "/" + dateVal.getFullYear() : "");
	return dateStr;
}

function setupCalendar() {
	var startDate = new Date();
	startDate.setDate(startDate.getDate() - 730);
	startDate.setHours(0);
	startDate.setMinutes(0);
	startDate.setSeconds(0);
	startDate.setMilliseconds(0);

	var endDate = new Date();
	endDate.setHours(23);
	endDate.setMinutes(59);
	endDate.setSeconds(59);
	endDate.setMilliseconds(999);

	var validDates = new Array();
	var nextDate = new Date(startDate);
	while(nextDate <= endDate) {
		var validDate = new Date(nextDate);
		validDates.push(validDate);
		nextDate.setDate(nextDate.getDate() + 1);
	}

	calFrom = new calendar(document.getElementsByName("fromDate")[0], startDate.valueOf(), endDate.valueOf(), validDates);
	calFrom.year_scroll = false;
	calFrom.time_comp = false;

	calTo = new calendar(document.getElementsByName("toDate")[0], startDate.valueOf(), endDate.valueOf(), validDates);
	calTo.year_scroll = false;
	calTo.time_comp = false;
}

function popupCalendarFrom() {
	var v = document.getElementsByName("fromDate")[0].value;
	if (v == null || v.length == 0) {
		setDefaultFromDate("fromDate");
	}
	
	var errorMessage ='<content:getAttribute beanName="fromDateFormatError" attribute="text"/>';
	
	if (validateDate(v, errorMessage)) {
		   calFrom.popup();
	}
	else {
		setDefaultFromDate("fromDate");
	}
}

//Default From date will be a constant taken from property file
function setDefaultFromDate(dateField) {
	
var defaultFromDate ='${theForm.defaultFromDate}';
	if (defaultFromDate){
		document.getElementsByName(dateField)[0].value = defaultFromDate;
	} else {
		document.getElementsByName(dateField)[0].value = "03/01/2015";
	}
	
}

function setDefaultDate(dateField) {
	var today = new Date();
	document.getElementsByName(dateField)[0].value = formatDate(today, true);
}

function popupCalendarTo() {
var v = document.getElementsByName("toDate")[0].value;
if (v == null || v.length == 0) {
	   setDefaultDate("toDate");
}

var errorMessage ='<content:getAttribute beanName="toDateFormatError" attribute="text"/>';
if (validateDate(v, errorMessage)) {
	   calTo.popup();
}
else {
	   setDefaultDate("toDate");
}
}

function doReset() {
	 document.forms['alertsReportForm'].action = "/do/noticereports/orderStatusReport/?task=reset";
     document.forms['alertsReportForm'].submit();
}


function doSearch(){
	 document.forms['alertsReportForm'].action = "/do/noticereports/orderStatusReport/?task=search";
     document.forms['alertsReportForm'].submit();
}

function initPage() {
	setupCalendar();
}

if (window.addEventListener) {
	window.addEventListener('load', initPage, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', initPage);
}


function validateDate (str_date, errorMessage) {
	var arr_date = str_date.split('/');

	if (arr_date.length != 3 || arr_date[2].length != 4) {
		
		if (!errorMessage){
			alert("Please enter dates in the format mm/dd/yyyy or select a date from the calendar.");
		} else {
			alert(errorMessage);
		}
		return false;
	}
	
	if (!arr_date[1]) {
		alert ("Invalid date format: '" + str_date + "'.\nNo day of month value can be found.");
		return false;
	}
	
	if (!RE_NUM.exec(arr_date[1])) {
		alert ("Invalid day of month value: '" + arr_date[1] + "'.\nValue must be numeric.");
		return false;
	}
	
	if (!arr_date[0]) {
		alert ("Invalid date format: '" + str_date + "'.\nNo month value can be found.");
		return false;
	}
	
	if (!RE_NUM.exec(arr_date[0])) {
		alert ("Invalid month value: '" + arr_date[0] + "'.\nValue must be numeric.");
		return false;
	}
	
	if (!arr_date[2]) {
		alert ("Invalid date format: '" + str_date + "'.\nNo year value can be found.");
		return false;
	}
	
	if (!RE_NUM.exec(arr_date[2])) {
		alert ("Invalid year value: '" + arr_date[2] + "'.\nValue must be numeric.");
		return false;
	}

	var dt_date = new Date();
	dt_date.setDate(1);

	if (arr_date[0] < 1 || arr_date[0] > 12) {
		alert ("Invalid month value: '" + arr_date[0] + "'.\nAllowed range is 01-12.");
		return false;
	}
	
	dt_date.setMonth(arr_date[0]-1);
	 
	if (arr_date[2] < 100) arr_date[2] = Number(arr_date[2]) + (arr_date[2] < NUM_CENTYEAR ? 2000 : 1900);
	dt_date.setFullYear(arr_date[2]);

	var dt_numdays = new Date(arr_date[2], arr_date[0], 0);
	dt_date.setDate(arr_date[1]);
	if (dt_date.getMonth() != (arr_date[0]-1)) {
		 alert ("Invalid day of month value: '" + arr_date[1] + "'.\nAllowed range is 01-"+dt_numdays.getDate()+".");
		 return false;
	}
	
	return true;
}

$(document).ready(function(){
	var total=0;
	$(".count").each(function e(){
		total+=parseInt($(this).html());
	});
	$(".total").text(total);
	
});
</script>
<ps:form method="POST" modelAttribute="alertsReportForm" name="alertsReportForm" action="/do/noticereports/orderStatusReport/">
<input type="hidden" name="task" /><%--  input - name="alertsReportForm" --%>
	<TABLE border=0 cellSpacing=0 cellPadding=0 width=760>
		<tr>
			<td />
			<td colspan="2">
				<div id="errordivcs">
					<content:errors scope="session" />
				</div>
			</td>
		</tr>
		<tr>
			<td width=30><img height=1 src="/assets/unmanaged/images/s.gif"
				width=30></td>
			<td width=730 colspan="2"><A name=TopOfPage></A>
				<table cellSpacing=0 cellPadding=0 width=730 border=0>
					<tbody>
						<tr>
							<td><jsp:include page="noticeReportsTabs.jsp" flush="true">
									<jsp:param value="4" name="tabValue" />
								</jsp:include></td>
						</tr>
						<tr>
							<TD class=boxborder width=1><IMG border=0
								src="/assets/spacer.gif" width=1 height=1></TD>
							<TD>
								<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
									<TBODY>
										<TR>
											<td class="tablesubhead" colspan="7" height="23"><img
												src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										</tr>
										<tr>
											<td width="200"><img
												src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
											<td width="1"><img src="/assets/unmanaged/images/s.gif"
												height="1" width="1"></td>
											<td width="120"><img
												src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
											<td width="1"><img src="/assets/unmanaged/images/s.gif"
												height="1" width="1"></td>
											<td width="220"><img
												src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
											<td width="1"><img src="/assets/unmanaged/images/s.gif"
												height="1" width="1"></td>
											<td width="120"><img
												src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table> <!-- Common Section -- search by contract and filter dates -->
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="3" class="filterSearch" vAlign="top" />
						<td width="205" valign="top" class="filterSearch"><strong>Contract
								number</strong><br> <c:if test="${printFriendly == null }" >
<form:input path="contractNumber" maxlength="7" size="17" />

							</c:if>
							<c:if test="${printFriendly != null }" >
<form:input path="contractNumber" disabled="true" maxlength="7" size="17"/>
							</c:if>
						</td>
						<td width="329" valign="top" class="filterSearch"><strong><strong>Date Range</strong><br> </strong>from<strong>
						 <c:if test="${printFriendly == null}">
<form:input path="fromDate" maxlength="10" size="10" cssClass="inputField" />

									<img src="/assets/unmanaged/images/cal.gif" width="16"
										height="16" onclick="javascript:popupCalendarFrom()"></strong> to<strong>
<form:input path="toDate" maxlength="10" size="10" cssClass="inputField" /> <img

								src="/assets/unmanaged/images/cal.gif" width="16" height="16"
								onclick="javascript:popupCalendarTo()">
						</strong><br> </c:if> <c:if test="${printFriendly != null }" >
<form:input path="fromDate" disabled="true" maxlength="10" size="10" cssClass="inputField" />


								<img src="/assets/unmanaged/images/cal.gif" width="16"
									height="16">
</strong> to<strong> <form:input path="toDate" disabled="true" maxlength="10" size="10" cssClass="inputField" /> <img


									src="/assets/unmanaged/images/cal.gif" width="16" height="16"></strong>
								<br>
							</c:if>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
							<br>&nbsp;</td>
						<td width="184" valign="top" class="filterSearch"><br>
							<div align="middle">
								<a href="javascript:openChangeWindow()"></a> &nbsp;&nbsp;&nbsp;
								<c:if test="${printFriendly == null }" >
<input type="button" onclick="return doReset()" name="button1" value="reset"/>

													&nbsp;&nbsp;&nbsp;
<input type="button" onclick="return doSearch()" name="button2" value="search"/>

								</c:if>
								<c:if test="${printFriendly != null }" >
<input type="button" disabled="true" name="button1" value="reset"/>

													&nbsp;&nbsp;&nbsp;
<input type="button" disabled="true" name="button2" value="search"/>

								</c:if>
							</div> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <br>&nbsp;
						</td>
					</tr>
				</table>

				<table border=0 cellSpacing=0 cellPadding=0 width="730">
					<TBODY>
						<TR>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif"
								width=1 height=1></TD>
							<TD width=220><IMG src="/assets/unmanaged/images/s.gif"
								width=220 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif"
								width=1 height=1></TD>
							<TD width=130><IMG src="/assets/unmanaged/images/s.gif"
								width=130 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif"
								width=1 height=1></TD>
							<TD width=125><IMG src="/assets/unmanaged/images/s.gif"
								width=125 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif"
								width=1 height=1></TD>
							<TD width=125><IMG src="/assets/unmanaged/images/s.gif"
								width=125 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif"
								width=1 height=1></TD>
							<TD width=125><IMG src="/assets/unmanaged/images/s.gif"
								width=125 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif"
								width=1 height=1></TD>
						</TR>
						<TR>
							<TD colSpan=11>&nbsp;</TD>
						</TR>
						<!-- Start of body title -->
						<tr class="tablehead">
							<td colspan="11">
								<table border="0" cellpadding="0" cellspacing="0" width="100%">
									<tbody>
										<tr>
											<td class="tableheadTD" width="50%">&nbsp;</td>
											<td class="tableheadTDinfo">&nbsp;</td>
											<td class="tableheadTDinfo" align="right">&nbsp;</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
							<!-- End of body title -->
						<TR class=tablesubhead>
							<TD class=databorder width=1></TD>
							<TD class=tablesubhead vAlign=top align="center"><B>Status type</B></TD>
							<TD class=datadivider width=1><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD class=tablesubhead vAlign=top align="center"><B>No.
									of orders</B></TD>
							<TD class=datadivider width=1><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<c:if test="${theReport.suppressStatusByPercentageGraphTitle !=false}">
							<TD class=tablesubhead vAlign=top align="center" colspan="5"><B>Status
									by percentage</B></TD>
</c:if>
<c:if test="${theReport.suppressStatusByPercentageGraphTitle !=true}">
									<TD class=tablesubhead vAlign=top colspan="5">
</c:if>
									
							<TD class=databorder width=1><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						</TR>
						<c:if test="${not empty theReport != null}">
<c:if test="${not empty theReport.orderStatusReportVOList}">

<c:forEach items="${theReport.orderStatusReportVOList}" var="theItem" varStatus="theIndex" >
 <c:set var="indexValue" value="${theIndex.index}"/> 
 <%
  	String indexVal = pageContext.getAttribute("indexValue").toString();
   OrderStatusReportVO theItem = (OrderStatusReportVO)pageContext.getAttribute("theItem");
  %>


									<%
										if (Integer.parseInt(indexVal) % 2 == 0) {
									%>
									<tr class="datacell3">
										<%
											} else {
										%>
									
									<tr class="datacell1">
										<%
											}
										%>
										<TD class=databorder width=1><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD align="left" style="padding-left: 5px;">${theItem.orderStatusType}</TD>
										<TD class=datadivider width=1><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD class="count" align="right" style="padding-right: 3px;"><fmt:formatNumber pattern="#,###" value="${theItem.totalOrderStatusCount}" /> </TD>

										<TD class=datadivider width=1><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<%
											if (theItem.getOrderStatusType()
																									.equals("Initiated")) {
										%>
										<td vAlign=top width=446 colspan=5 rowspan=6 class="datacell1">
										<%
											PieChartBean pieChartBean=(PieChartBean)request.getAttribute(OrderStatusReportController.ORDER_STATUS_FREQUENCY_PIECHART);
										pageContext.setAttribute("pieChartBean", pieChartBean);
										%>
 <c:if test="${not empty pieChartBean}">

												<table>
													<tr>
														<td width=220 align=right style="font-size: 1em;" class="pieChart"><ps:pieChart
																beanName="<%=OrderStatusReportController.ORDER_STATUS_FREQUENCY_PIECHART%>"
																alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
																title="Order Status Frequency Chart" /></td>
														<!--  Pie Chart Legend -->
														<td width=230 align=left>
															<table>
<c:if test="${theReport.suppressInitiatedLegend !=false}">
																<tr>
																	<td valign="top">
																		<table border="0" cellpadding="0" cellspacing="0">
																			<tbody>
																				<tr>
																					<td
																						style="background: <%=OrderStatusReportController.ORDER_STATUS_INITIATED_COLOR_WEDGE_LABEL%>;"
																						height="7" width="7"><img height="7"
																						width="7" src="/assets/unmanaged/images/s.gif" /></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																	<td valign="top"><%=OrderStatusReportData.INITIATED_STATUS%></td>
																</tr>
</c:if>
<c:if test="${theReport.suppressErrorInvalidlegend !=false}">
																<tr>
																	<td valign="top">
																		<table border="0" cellpadding="0" cellspacing="0">
																			<tbody>
																				<tr>
																					<td
																						style="background: <%=OrderStatusReportController.ORDER_STATUS_ERROR_IN_FILE_COLOR_WEDGE_LABEL%>;"
																						height="7" width="7"><img height="7"
																						width="7" src="/assets/unmanaged/images/s.gif" /></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																	<td valign="top"><%=OrderStatusReportData.ERROR_INVALID_LEGEND%></td>
																</tr>
</c:if>
<c:if test="${theReport.suppressNotCompelteLegend !=false}">
																<tr>
																	<td valign="top">
																		<table border="0" cellpadding="0" cellspacing="0">
																			<tbody>
																				<tr>
																					<td
																						style="background: <%=OrderStatusReportController.ORDER_STATUS_NOTCOMPLETED_COLOR_WEDGE_LABEL%>;"
																						height="7" width="7"><img height="7"
																						width="7" src="/assets/unmanaged/images/s.gif" /></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																	<td valign="top"><%=OrderStatusReportData.NOT_COMPLETED_STATUS%></td>
																</tr>
</c:if>
<c:if test="${theReport.suppressInProgressLegend !=false}">
																<tr>
																	<td valign="top">
																		<table border="0" cellpadding="0" cellspacing="0">
																			<tbody>
																				<tr>
																					<td
																						style="background: <%=OrderStatusReportController.ORDER_STATUS_INPROGRESS_COLOR_WEDGE_LABEL%>;"
																						height="7" width="7"><img height="7"
																						width="7" src="/assets/unmanaged/images/s.gif" /></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																	<td valign="top"><%=OrderStatusReportData.IN_PROGRESS_STATUS%></td>
																</tr>
</c:if>
<c:if test="${theReport.suppressCancelledLegend !=false}">
																
																<tr>
																	<td valign="top">
																		<table border="0" cellpadding="0" cellspacing="0">
																			<tbody>
																				<tr>
																					<td
																						style="background: <%=OrderStatusReportController.ORDER_STATUS_CANCELLED_COLOR_WEDGE_LABEL%>;"
																						height="7" width="7"><img height="7"
																						width="7" src="/assets/unmanaged/images/s.gif" /></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																	<td valign="top"><%=OrderStatusReportData.CANCELLED_STATUS%></td>
																</tr>
</c:if>
<c:if test="${theReport.suppressCompleteLegend !=false}">
																<tr>
																	<td valign="top">
																		<table border="0" cellpadding="0" cellspacing="0">
																			<tbody>
																				<tr>
																					<td
																						style="background: <%=OrderStatusReportController.ORDER_STATUS_COMPLETED_COLOR_WEDGE_LABEL%>;"
																						height="7" width="7"><img height="7"
																						width="7" src="/assets/unmanaged/images/s.gif" /></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																	<td valign="top"><%=OrderStatusReportData.COMPLETED_STATUS%></td>
																</tr>
</c:if>
															</table>
														</td>
													</tr>
												</table>
</c:if>
										</td>
										<%
											}
										%>
										<TD class=databorder width=1><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									</TR>
</c:forEach>
							
						<TR>
							<TD class=databorder width=1><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD class=datacell3 style="padding-left: 5px;"><b>Total</b></TD>
							<TD class=datadivider width=1><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD class="total orangeText datacell3" align="right" style="padding-right: 3px;"></TD>
							<TD class=datadivider width=1><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD class=datacell1 colspan="5"><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD class=databorder width=1><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						</TR>
</c:if>
						</c:if>

						<!-- End of Last line -->
						<TR>
							<TD class=databorder colSpan=11><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						</TR>
						<TR>
							<TD colSpan=11><BR>
								<P></P>
								</TD>
						</TR>
					</TBODY>
				</TABLE> <c:if test="${printFriendly == null }" >
					<P>
					<div align="left">
						<input name="backButton" type="button" class="button134"
							onClick="document.location='/do/tools/controlReports/'"
							value="control reports"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
							name="searchContractButton" type="button" class="button134"
							onClick="document.location='/do/home/searchContractDetail/'"
							value="select contract">
					</div>
					</P>
				</c:if>
				<c:if test="${printFriendly != null}">
		    			<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure"/>
		   				<table  border=0 cellSpacing=0 cellPadding=0 width=730>
						    <tr>
						    	<td width="100%"><p><content:pageFooter beanName="layoutPageBean" /></p></td>
						    </tr>
						    <tr>
						    	<td width="100%">&nbsp;</td>
						    </tr>
						    <tr>
						    	<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
						    </tr>
		    			</table>
		    		</c:if>
		    		</TD>
		</TR>
		</TBODY>
	</TABLE>
</ps:form>
