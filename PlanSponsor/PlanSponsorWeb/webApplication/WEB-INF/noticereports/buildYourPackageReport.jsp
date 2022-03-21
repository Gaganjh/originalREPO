<%-- taglib used --%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.noticereports.AlertsReportForm" %>
<%@ page import="com.manulife.pension.ps.web.noticereports.BuildYourPackageReportController" %>
<%@ page import="com.manulife.pension.ps.service.report.noticereports.valueobject.BuildYourPackageReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.util.piechart.PieChartBean" %>
<%
	BuildYourPackageReportData theReport = (BuildYourPackageReportData)request.getAttribute(Constants.REPORT_BEAN);
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
	 document.forms['alertsReportForm'].action = "/do/noticereports/buildYourPackageReport/?task=reset";
     document.forms['alertsReportForm'].submit();
}


function doSearch(){
	 document.forms['alertsReportForm'].action = "/do/noticereports/buildYourPackageReport/?task=search";
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
</script><ps:form method="POST" action="/do/noticereports/buildYourPackageReport/" modelAttribute="alertsReportForm" name="alertsReportForm">
<input type="hidden" name="task" /><%-- input - name="alertsReportForm" --%>
			
			<TABLE border=0 cellSpacing=0 cellPadding=0 width=760>
				<TBODY>
					<TR>
							<tr>
							<td />
							<td colspan="2">
								<div id="errordivcs">
									<content:errors scope="session" />
								</div>
							</td>
						</tr>
						<TD width=30></TD>
						<TD width=730><TABLE border=0 cellSpacing=0 cellPadding=0 width=525>
								<TBODY>
									<TR>
										<TD>
											<TABLE border=0 cellSpacing=0 cellPadding=0 width="730">
												<TBODY>
													<TR>
														<td><jsp:include page="noticeReportsTabs.jsp" flush="true">
															<jsp:param value="3" name="tabValue" />
															</jsp:include>
														</td>
													</TR>
													<TR>
														<TD class=tablesubhead height=25 colSpan=4></TD>
													</TR>
												</tbody>
											</table>
										</td>
									</tr>
								</tbody>
								</table>
										<table width="100%" border="0" cellSpacing="0" cellPadding="0">
											<TBODY>
													<TR>
														<TD class=filterSearch vAlign=top width=3></td>
										<TD class=filterSearch vAlign=top width=205><STRONG>Contract
										number </STRONG><BR>
										<c:if test="${printFriendly == null }" >
<form:input path="contractNumber" maxlength="7" size="17"/>
										</c:if> <c:if test="${printFriendly != null }" >
<form:input path="contractNumber" disabled="true" maxlength="7" size="17"/>

										</c:if></TD>
										<td valign="top" class="filterSearch"><strong><strong>Date Range</strong><br>
										</strong>from<strong> 
										<c:if test="${printFriendly == null}">
<form:input path="fromDate" maxlength="10" size="10" cssClass="inputField"/>

											<img src="/assets/unmanaged/images/cal.gif" width="16"
												height="16" onclick="javascript:popupCalendarFrom()"></strong>
to<strong> <form:input path="toDate" maxlength="10" size="10" cssClass="inputField"/> <img

											src="/assets/unmanaged/images/cal.gif" width="16" height="16"
											onclick="javascript:popupCalendarTo()"></strong><br>
										</c:if> <c:if test="${printFriendly != null }" >
<form:input path="fromDate" disabled="true" maxlength="10" size="10" cssClass="inputField"/>

											<img src="/assets/unmanaged/images/cal.gif" width="16"
												height="16">
</strong> to<strong> <form:input path="toDate" disabled="true" maxlength="10" size="10" cssClass="inputField"/>

											<img src="/assets/unmanaged/images/cal.gif" width="16"
												height="16"></strong>
											<br>
										</c:if>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
										<br>
										&nbsp;</td>

										<td valign="top" class="filterSearch"><br>
										<div align="middle"><a
											href="javascript:openChangeWindow()"></a>
											 <c:if test="${printFriendly == null}">
											<input type=button value="reset" name="reset"
												onclick="doReset();">
												&nbsp;&nbsp;&nbsp;
<input type="button" onclick="doSearch(); return false;" name="button" value="search"/>

										</c:if> 
										<c:if test="${printFriendly != null}">
											<input type=button value="reset" name="reset"
												disabled="disabled">
												&nbsp;&nbsp;&nbsp;
<input type="button" disabled="true" name="button" value="search"/>

										</c:if> </div>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <br>
										&nbsp;</td>
													</TR>
													<TR>
														<TD class=filterSearch colSpan=4></TD>
													</TR>
												</TBODY>
											</TABLE>
											
											<!-- start table -->
 
											<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
												<TBODY>
													<TR>
														<TD width=1></TD>
														<TD width=258></TD>
														<TD width=1></TD>
														<TD width=90></TD>
														<TD width=1></TD>
														<TD width=258></TD>
														<TD width=1></TD>
														<TD width=90></TD>
														<TD width=1></TD>
													</TR>
													<TR>
														<TD colSpan=9>&nbsp;</TD>
													</TR>
													<!-- Start of body title -->
													<TR class=tablehead>
														<TD colSpan=9><TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
																<TBODY>
																	<TR>
																		<TD class=tableheadTD width="50%">&nbsp;</TD>
																		<TD class=tableheadTDinfo>&nbsp;</TD>
																		<TD class=tableheadTDinfo align=right>&nbsp;</TD>
																	</TR>
																</TBODY>
															</TABLE></TD>
													</TR>
													<!-- End of body title -->
													<TR class=tablesubhead>
														<TD class=databorder width=1></TD>
														<TD class=tablesubhead vAlign=top colspan="7"><B>User Statistics</B></TD>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													<c:if test="${not empty theReport.numberOfContractsUsingMailService}">
														<c:if test="${not empty theReport.newBusinessContractsPercentage}">
													<TR class=datacell3>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 style="padding-left: 5px;">Number of unique contracts using both Download and / or mail service</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>
<c:if test="${theReport.numberOfContractsUsingMailAndDownload !=0}">
 <fmt:formatNumber type = "number" maxIntegerDigits = "3" value = "${theReport.numberOfContractsUsingMailAndDownload}" />
</c:if>
<c:if test="${theReport.numberOfContractsUsingMailAndDownload ==0}">
														0
</c:if>
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 style="padding-left: 5px;">Contract Status - New Business</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>
<c:choose>
  <c:when test="${theReport.newBusinessContractsPercentage == '0'}">
    N/A
  </c:when>
  <c:when test="${theReport.newBusinessContractsPercentage == '0.000'}">
    N/A
  </c:when>
  <c:otherwise>
    ${theReport.newBusinessContractsPercentage}%
  </c:otherwise>
</c:choose>	
														</TD>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													</c:if>
													<c:if test="${theReport.numberOfRepeatContracts != null}">
														<c:if test="${theReport.inforceContractsPercentage != null}">
													<TR class=datacell1>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">Number of repeat contracts using both Download and / or mail service</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1>
<c:if test="${theReport.numberOfRepeatContracts !=0}">
${theReport.numberOfRepeatContracts}
</c:if>
<c:if test="${theReport.numberOfRepeatContracts ==0}">
														0
</c:if>
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">Contract Status - Inforce Business</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1>
<c:choose>
  <c:when test="${theReport.inforceContractsPercentage == '0'}">
    N/A
  </c:when>
  <c:when test="${theReport.inforceContractsPercentage == '0.000'}">
    N/A
  </c:when>
  <c:otherwise>
    ${theReport.inforceContractsPercentage}%
  </c:otherwise>
</c:choose>	
														</TD>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
														</c:if>
													</c:if>
													<c:if test="${theReport.averageNumberOfMailingsPerContract != null}">
														<c:if test="${theReport.userPreferenceMailPercentage != null}">
													<TR class=datacell3>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 valign="top" style="padding-left: 5px;">Average no. of mailings / contract</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>
<c:choose>
  <c:when test="${theReport.averageNumberOfMailingsPerContract == '0'}">
    N/A
  </c:when>
  <c:when test="${theReport.averageNumberOfMailingsPerContract == '0.0'}">
    N/A
  </c:when>
  <c:otherwise>
 <fmt:formatNumber type = "number" maxFractionDigits  = "1" value = "${theReport.averageNumberOfMailingsPerContract}" />
  </c:otherwise>
</c:choose>															

														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 valign="top" style="padding-left: 5px;">User Preference - Mail</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>
<c:choose>
  <c:when test="${theReport.userPreferenceMailPercentage == '0'}">
    N/A
  </c:when>
  <c:when test="${theReport.userPreferenceMailPercentage == '0.000'}">
    N/A
  </c:when>
  <c:otherwise>
    ${theReport.userPreferenceMailPercentage}%
  </c:otherwise>
</c:choose>															
														</TD>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
														</c:if>
													</c:if>
													<c:if test="${theReport.monthsWithMostMailings != null}">
														<c:if test="${theReport.userPreferenceDownloadPercentage != null}">
													<TR class=datacell1>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">Month(s) with most mailings</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD class=datacell1><c:if test="${not empty theReport.monthsWithMostMailings}"><c:forEach items="${theReport.monthsWithMostMailings}" var="mostMailingMonths" varStatus="theIndex" >





${mostMailingMonths}<br/>
</c:forEach>
</c:if>
<c:if test="${empty theReport.monthsWithMostMailings}">
											N/A
</c:if>
											</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">User Preference - Download</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1>
<c:choose>
  <c:when test="${theReport.userPreferenceDownloadPercentage == '0'}">
    N/A
  </c:when>
  <c:when test="${theReport.userPreferenceDownloadPercentage == '0.000'}">
    N/A
  </c:when>
  <c:otherwise>
    ${theReport.userPreferenceDownloadPercentage}%
  </c:otherwise>
</c:choose>															
														</TD>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
														</c:if>
													</c:if>
<c:if test="${theReport.totalCompletedOrders !=0}">
													<TR class=tablesubhead>
														<TD class=databorder width=1></TD>
														<TD class=tablesubhead vAlign=top colspan="3"><B>Order Information</B></TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=tablesubhead vAlign=top colspan="3"><B>Percentage of completed orders by user</B></TD>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
</c:if>
													
<c:if test="${not empty theReport.totalCompletedOrders}">
<c:if test="${theReport.totalCompletedOrders !=0}">
													<TR class=datacell3>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 style="padding-left: 5px;">No. of completed orders</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>
<c:if test="${theReport.totalCompletedOrders !=0}">
 <fmt:formatNumber type = "number" maxIntegerDigits = "3" value = "${theReport.totalCompletedOrders}" />
</c:if>
<c:if test="${theReport.totalCompletedOrders ==0}">
														N/A
</c:if>
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD class=datacell1 colspan="3" rowspan="14" align="center">
<%
	PieChartBean pieChartBean=(PieChartBean)request.getAttribute(BuildYourPackageReportController.BUILD_YOUR_PACKAGE_FREQUENCY_PIECHART);
pageContext.setAttribute("pieChartBean", pieChartBean);
%>
 <c:if test="${not empty pieChartBean}">

												<table>
													<tr>
														<td width=170 align=right style="font-size: 1em;" class="pieChart"><ps:pieChart
																beanName="<%=BuildYourPackageReportController.BUILD_YOUR_PACKAGE_FREQUENCY_PIECHART%>"
																alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
																title="Build Your Package Frequency Chart" /></td>
														   
														<td width=150 align=left>
															<table>
<c:if test="${theReport.ordersByPlanSponsorsPercentage !='0.000'}">
																<tr>
																	<td valign="top">
																		<table border="0" cellpadding="0" cellspacing="0">
																			<tbody>
																				<tr>
																					<td
																						style="background: <%=BuildYourPackageReportController.PS_COLOR_WEDGE_LABEL%>;"
																						height="7" width="7"><img height="7"
																						width="7" src="/assets/unmanaged/images/s.gif" /></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																	<td valign="top"><%=BuildYourPackageReportController.PLAN_SPONSOR%></td>
																</tr>
</c:if>
<c:if test="${theReport.ordersByIntermediaryContactPercentage !='0.000'}">
																<tr>
																	<td valign="top">
																		<table border="0" cellpadding="0" cellspacing="0">
																			<tbody>
																				<tr>
																					<td
																						style="background: <%=BuildYourPackageReportController.INC_COLOR_WEDGE_LABEL%>;"
																						height="7" width="7"><img height="7"
																						width="7" src="/assets/unmanaged/images/s.gif" /></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																	<td valign="top"><%=BuildYourPackageReportController.INTERMEDIARY_CONTACT%></td>
																</tr>
</c:if>
<c:if test="${theReport.ordersByTPAPercentage !='0.000'}">
																<tr>
																	<td valign="top">
																		<table border="0" cellpadding="0" cellspacing="0">
																			<tbody>
																				<tr>
																					<td
																						style="background: <%=BuildYourPackageReportController.TPA_COLOR_WEDGE_LABEL%>;"
																						height="7" width="7"><img height="7"
																						width="7" src="/assets/unmanaged/images/s.gif" /></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																	<td valign="top"><%=BuildYourPackageReportController.TPA%></td>
																</tr>
</c:if>
<c:if test="${theReport.ordersByTotalCarePercentage !='0.000'}">
																<tr>
																	<td valign="top">
																		<table border="0" cellpadding="0" cellspacing="0">
																			<tbody>
																				<tr>
																					<td
																						style="background: <%=BuildYourPackageReportController.TOTAL_CARE_COLOR_WEDGE_LABEL%>;"
																						height="7" width="7"><img height="7"
																						width="7" src="/assets/unmanaged/images/s.gif" /></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																	<td valign="top"><%=BuildYourPackageReportController.TOTAL_CARE%></td>
																</tr>
</c:if>
															</table>
														</td>
													</tr>
												</table>
</c:if>
											
														</TD>
														<TD class=databorder width=1 rowspan="14"><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													
													<c:if test="${theReport.blackWhiteOrdersPercentage != null}">
													<TR class=datacell1>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">Orders in black &amp; white</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1>
														
														
<c:choose>
  <c:when test="${theReport.blackWhiteOrdersPercentage == '0'}">
    N/A
  </c:when>
  <c:when test="${theReport.blackWhiteOrdersPercentage == '0.000'}">
    N/A
  </c:when>
  <c:otherwise>
    ${theReport.blackWhiteOrdersPercentage}%
  </c:otherwise>
</c:choose>														
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
 												    <c:if test="${theReport.orderStapledPercentage != null}">
													<TR class=datacell3>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 style="padding-left: 5px;">Orders stapled label</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>														
<c:choose>
  <c:when test="${theReport.orderStapledPercentage == 0}">
    N/A
  </c:when>
  <c:when test="${theReport.orderStapledPercentage == '0.000'}">
    N/A
  </c:when>
  <c:otherwise>
    ${theReport.orderStapledPercentage}%
  </c:otherwise>
</c:choose>															

														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.bookletOrdersPercentage != null}">
													<TR class=datacell1>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">Orders using booklet envelopes</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1>
<c:choose>
  <c:when test="${theReport.bookletOrdersPercentage == '0'}">
    N/A
  </c:when>
  <c:when test="${theReport.bookletOrdersPercentage == '0.000'}">
    N/A
  </c:when>
  <c:otherwise>
    ${theReport.bookletOrdersPercentage}%
  </c:otherwise>
</c:choose>															

														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.numberOfPostageOrders != null}">
													<TR class=datacell3>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 style="padding-left: 5px;">No. of postage orders</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>
<c:choose>
  <c:when test="${theReport.numberOfPostageOrders == 0}">
    N/A
  </c:when>
  <c:otherwise>
    <fmt:formatNumber type = "number" maxIntegerDigits = "3" value = "${theReport.numberOfPostageOrders}" />
  </c:otherwise>
</c:choose>	
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.numberOfBulkOrders != null}">
													<TR class=datacell1>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">No. of bulk orders</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1>
<c:choose>
  <c:when test="${theReport.numberOfBulkOrders == 0}">
    N/A
  </c:when>
  <c:otherwise>
     <fmt:formatNumber type = "number" maxIntegerDigits = "3" value = "${theReport.numberOfBulkOrders}" />
  </c:otherwise>
</c:choose>	
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.sealedOrdersPercentage != null}">
													<TR class=datacell3>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 style="padding-left: 5px;">Bulk orders sealed</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>
<c:choose>
  <c:when test="${theReport.sealedOrdersPercentage == 0}">
    N/A
  </c:when>
    <c:when test="${theReport.sealedOrdersPercentage == '0.000'}">
    N/A
  </c:when>
  <c:otherwise>
     ${theReport.sealedOrdersPercentage}%
  </c:otherwise>
</c:choose>
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.averageNumberOfPagesPerOrder != null}">
													<TR class=datacell1>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">Average no. of pages/order</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1>
<c:choose>
  <c:when test="${theReport.averageNumberOfPagesPerOrder == 0}">
    N/A
  </c:when>
    <c:when test="${theReport.averageNumberOfPagesPerOrder == '0.0'}">
    N/A
  </c:when>
  <c:otherwise>
      <fmt:formatNumber type = "number" maxFractionDigits = "1" value = "${theReport.averageNumberOfPagesPerOrder}" />
  </c:otherwise>
</c:choose>
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.averageNumberOfPartipicantsPerOrder != null}">
													<TR class=datacell3>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 style="padding-left: 5px;">Average no. of participants/order</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>
<c:choose>
  <c:when test="${theReport.averageNumberOfPartipicantsPerOrder == 0}">
    N/A
  </c:when>
    <c:when test="${theReport.averageNumberOfPartipicantsPerOrder == '0.0'}">
    N/A
  </c:when>
  <c:otherwise>
     <fmt:formatNumber type = "number" maxFractionDigits  = "1" value = "${theReport.averageNumberOfPartipicantsPerOrder}" />
  </c:otherwise>
</c:choose>
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.particpantCount != null}">
													<TR class=datacell1>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">Total no. of participants</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1>
<c:if test="${theReport.particpantCount !=0}">
 <fmt:formatNumber type = "number" maxIntegerDigits = "3" value = "${theReport.particpantCount}" />
</c:if>
<c:if test="${theReport.particpantCount ==0}">
														N/A
</c:if>

														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.averageCostPerOrder != null}">
													<TR class=datacell3>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 style="padding-left: 5px;">Average cost/order</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>

<c:choose>
  <c:when test="${theReport.averageCostPerOrder == 0}">
    N/A
  </c:when>
    <c:when test="${theReport.averageCostPerOrder == '0.00'}">
    N/A
  </c:when>
  <c:otherwise>
      $<fmt:formatNumber type = "number" maxFractionDigits  = "2" value = "${theReport.averageCostPerOrder}" />
  </c:otherwise>
</c:choose>
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.totalOrderCosts != null}">
													<TR class=datacell1>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">Total Cost</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1>
<c:choose>
  <c:when test="${theReport.totalOrderCosts == 0}">
    N/A
  </c:when>
    <c:when test="${theReport.totalOrderCosts == '0.00'}">
    N/A
  </c:when>
  <c:otherwise>
      $<fmt:formatNumber type = "number" maxFractionDigits  = "2" value = "${theReport.totalOrderCosts}" />
  </c:otherwise>
</c:choose>
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.amountPaidByJohnHancock != null}">
													<TR class=datacell3>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3 style="padding-left: 5px;">Amount paid by John Hancock</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell3>
<c:choose>
  <c:when test="${theReport.amountPaidByJohnHancock == 0}">
    N/A
  </c:when>
    <c:when test="${theReport.amountPaidByJohnHancock == '0.000'}">
    N/A
  </c:when>
  <c:otherwise>
  $<fmt:formatNumber type = "number" maxFractionDigits  = "2" value = "${theReport.amountPaidByJohnHancock}" />
  </c:otherwise>
</c:choose>
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
													<c:if test="${theReport.amountPaidByJohnHancockTotalCarePercentage != null}">
													<TR class=datacell1>
														<TD class=databorder width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1 style="padding-left: 5px;">Percentage of 'Paid by John Hancock' for TotalCare</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=datacell1>


<c:choose>
  <c:when test="${theReport.amountPaidByJohnHancockTotalCarePercentage == 0}">
    N/A
  </c:when>
    <c:when test="${theReport.amountPaidByJohnHancockTotalCarePercentage == '0.000'}">
    N/A
  </c:when>
  <c:otherwise>
${theReport.amountPaidByJohnHancockTotalCarePercentage}%
  </c:otherwise>
</c:choose>
														</TD>
														<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													</c:if>
</c:if>
</c:if>
													<!-- End of Last line -->
													<TR>
														<TD class=databorder colSpan=9><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>
													<TR>
														<TD colSpan=9>
															<P></P>
															</TD>
													</TR>
												</TBODY>
											</TABLE>
 
											
					<c:if test="${printFriendly == null }" >
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
					<P></P>
					<c:if test="${not empty param.printFriendly}">
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
		
