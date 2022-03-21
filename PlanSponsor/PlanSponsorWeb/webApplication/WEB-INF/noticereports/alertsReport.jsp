<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.noticereports.AlertsReportForm" %>
<%@ page import="com.manulife.pension.ps.web.noticereports.AlertsReportController" %>
<%@ page import="com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportUserStatsVO" %>
<%@ page import="com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportFreqVO" %>
<%@ page import="com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportDistributionVO" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />



<%
	AlertsReportData theReport = (AlertsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
AlertsReportForm theForm = (AlertsReportForm)session.getAttribute("alertsReportForm");
pageContext.setAttribute("theForm",theForm,PageContext.PAGE_SCOPE);
%>
<c:set var="printFriendly" value="${param.printFriendly}" />

<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_CONTROL_REPORTS_COMMON_PAGE_TITLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="pageTitle"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_CONTROL_REPORTS_COMMON_PAGE_FROM_DATE_FORMAT_ERROR%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="fromDateFormatError"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_CONTROL_REPORTS_COMMON_PAGE_TO_DATE_FORMAT_ERROR%>"
                          	type="<%=ContentConstants.TYPE_MESSAGE%>"
                         	id="toDateFormatError"/>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/report.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/input.js"></script>
<c:if test="${printFriendly != null}" >
<script>
	$(document).ready(function(){
		$(".pieChart img").removeAttr("title").removeAttr("alt").removeAttr("alt");
	});
</script>
</c:if>

<script type="text/javascript" >
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
	document.alertsReportForm.task.value = 'default';
	document.alertsReportForm.submit();
}


function doSearch(){
	document.alertsReportForm.task.value = 'search';
	document.alertsReportForm.submit();
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

	
</script>

<ps:form method="POST" modelAttribute="alertsReportForm" name="alertsReportForm" action="/do/noticereports/alertsReport/">

<input type="hidden" name="task" /><%--  input - name="alertsReportForm" --%>

<table cellSpacing=0 cellPadding=0 width=760 border=0>
	<tbody>
  		<tr>
			<td/>
			<td colspan="2">
				<div id="errordivcs"><content:errors scope="session"/></div>
			</td>
		</tr>
		<tr>
			<td width=30><img height=1 src="/assets/unmanaged/images/s.gif" width=30></td>
    		<td width=730 colspan="2" >
				<A name=TopOfPage></A>
     							<table cellSpacing=0 cellPadding=0 width=730 border=0>
									<tbody>
										<tr><td><jsp:include page="noticeReportsTabs.jsp" flush="true">
													<jsp:param value="1" name="tabValue" />
												</jsp:include>
											 </td>
									    </tr>
									    <tr>
											<TD class=boxborder width=1><IMG border=0 src="/assets/spacer.gif" width=1 height=1></TD>
											<TD> 
												<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
													<TBODY>
														<TR>
																<td class="tablesubhead" colspan="7" height="23"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
														</tr>
														<tr>
																<td width="200"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
																<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
																<td width="120"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
																<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
																<td width="220"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
																<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
																<td width="120"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
														</tr>
													</tbody>
												</table> 
											</td>
										</tr>
									</tbody>
								</table>
								<!-- Common Section -- search by contract and filter dates -->
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td width="153" valign="top" class="filterSearch">
											<strong>Contract number</strong><br>
											<c:if test="${printFriendly == null }" >
<form:input path="contractNumber" maxlength="7" size="17"/>
											</c:if>														 
											<c:if test="${printFriendly != null }" >
<form:input path="contractNumber" disabled="true" maxlength="7" size="17"/>
											</c:if>
										</td>
										<td valign="top" class="filterSearch">
											<strong><strong>Date Range</strong><br>
								              						</strong>from<strong>
											<c:if test="${printFriendly == null }" >
<form:input path="fromDate" maxlength="10" size="10" cssClass="inputField"/>
								              						<img src="/assets/unmanaged/images/cal.gif" width="16" height="16" onclick="javascript:popupCalendarFrom()"></strong> to<strong>
<form:input path="toDate" maxlength="10" size="10" cssClass="inputField"/>
											<img src="/assets/unmanaged/images/cal.gif" width="16" height="16" onclick="javascript:popupCalendarTo()"></strong><br>
											</c:if>
											<c:if test="${printFriendly != null }" >
<form:input path="fromDate" disabled="true" maxlength="10" size="10" cssClass="inputField"/>
												<img src="/assets/unmanaged/images/cal.gif" width="16" height="16"></strong> to<strong>
<form:input path="toDate" disabled="true" maxlength="10" size="10" cssClass="inputField"/>
												<img src="/assets/unmanaged/images/cal.gif" width="16" height="16"></strong><br>
											</c:if>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
											<br>&nbsp;
										</td>
										<td  valign="top" class="filterSearch">
										    <br>
											<div align="middle"><a href="javascript:openChangeWindow()"></a>
												<c:if test="${printFriendly == null }" >
								                   					<input type=button value="reset" name="reset" onclick="doReset();">
												&nbsp;&nbsp;&nbsp;
<input type="button" onclick="doSearch(); return false;" name="button" value="search"/>
												</c:if>
												<c:if test="${printFriendly != null }" >
								                   					<input type=button value="reset" name="reset" disabled="disabled">
												&nbsp;&nbsp;&nbsp;
<input type="button" disabled="true" name="button" value="search"/>
												</c:if>
								          							</div>
								          							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								          							<br>&nbsp;
										</td>
									</tr>										  
								</table>
								<!--  Report Section -->
								<c:if test="${theReport != null}">
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<TR>
											<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD width=262><IMG src="/assets/unmanaged/images/s.gif"  height=1></TD>
											<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD width=83><IMG src="/assets/unmanaged/images/s.gif"  height=1></TD>
											<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD width=262><IMG src="/assets/unmanaged/images/s.gif" height=1></TD>
											<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD width=83><IMG src="/assets/unmanaged/images/s.gif" height=1></TD>
											<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
										<TR>
											<TD colSpan=9>&nbsp;</TD>
										</TR>
										<tr class=tablehead>
											<td colspan=15>&nbsp;</td>
										</tr>
													<tr class=tablesubhead>
														<td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=198 style="padding-left: 4px;"><B>Users</B></td>
														<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=111 align=right><B>No. of Unique users setting alerts</B></td>
														<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=111 align=right><B>Average alerts/user</B></td>
														<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=111 align=right><B>Total alerts (Urgent + Normal)</B></td>
														<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=111 align=right><B>Urgent alerts</B></td>
														<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=111 align=right><B>Normal alerts</B></td>
														<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=111 align=right><B>No. of deleted alerts</B></td>
														<td class=databorder width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													</tr>
<c:if test="${not empty theReport.alertUsersStatsList}">
<c:forEach items="${theReport.alertUsersStatsList}" var="theItem" varStatus="theIndex" >

 <c:set var="indexValue" value="${theIndex.index}"/> 
 <%
  	String indexVal = pageContext.getAttribute("indexValue").toString();
   int theIndex = Integer.parseInt(indexVal);
   AlertsReportUserStatsVO theItem = (AlertsReportUserStatsVO)pageContext.getAttribute("theItem");
  %>
																<%
																	if (theIndex % 2 == 0) {
																%>
												        <tr class="datacell1">
														<%
															} else {
														%>
												        <tr class="datacell2">
														<%
															}
														%>
														
														<%
																													if (theItem.getUsers().equals("Totals")) {
																												%> 
															<td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=198 style="padding-left: 4px;"><B>${theItem.users}</B></td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right class=orangeText style="padding-right: 4px;"><fmt:formatNumber pattern="#,###" value="${theItem.numberOfAlertUsers}" /></td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right class=orangeText style="padding-right: 4px;"><fmt:formatNumber pattern="#,###.##" value="${theItem.averageNumberOfAlertsPerUser}" /></td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right class=orangeText style="padding-right: 4px;"> <fmt:formatNumber pattern="#,###" value="${theItem.totalAlertSetUp}"/></td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right class=orangeText style="padding-right: 4px;"> <fmt:formatNumber pattern="#,###" value="${theItem.urgentAlerts}" /></td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right class=orangeText style="padding-right: 4px;"> <fmt:formatNumber pattern="#,###" value="${theItem.normalAlerts}" /></td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right class=orangeText style="padding-right: 4px;"> <fmt:formatNumber pattern="#,###" value="${theItem.numberOfDeletedAlerts}" /></td>
															<td class=databorder width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
																						
														<%
																																					} else {
																																				%>
								                          	<td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=198 style="padding-left: 4px;">${theItem.users}</td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 4px;">  <fmt:formatNumber pattern="#,###" value="${theItem.numberOfAlertUsers}" /></td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
															<td vAlign=top width=111 align=right style="padding-right: 4px;">
<c:if test="${not empty theItem.averageNumberOfAlertsPerUser}">
 <fmt:formatNumber pattern="#,###.##" value="${theItem.averageNumberOfAlertsPerUser}" />
</c:if>
<c:if test="${empty theItem.averageNumberOfAlertsPerUser}">
																	0
</c:if>
															</td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 4px;"><fmt:formatNumber pattern="#,###" value="${theItem.totalAlertSetUp}" /></td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 4px;"><fmt:formatNumber pattern="#,###" value="${theItem.urgentAlerts}" /></td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 4px;"> <fmt:formatNumber pattern="#,###" value ="${theItem.normalAlerts}" /></td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 4px;"><fmt:formatNumber pattern="#,###" value="${theItem.numberOfDeletedAlerts}" /></td>
															<td class=databorder width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<%
															}
														%>
								                        </tr>
</c:forEach>
</c:if>
												<!-- Alerts Frequency List -->
												<c:if test="${not empty AlertsReportAction.ALERT_FREQUENCY_PIECHART }">
													<tr class=tablesubhead>
														<td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=198><B>Alert frequencies</B></td>
														<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=111 align=right><B>Number of alerts/frequency</B></td>
														<c:if test="${not empty AlertsReportAction.ALERT_FREQUENCY_PIECHART }">
														<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=446 colspan=9 align=center><B>Alert frequency percentage of total</B></td>
</c:if>
														<c:if test="${empty AlertsReportAction.ALERT_FREQUENCY_PIECHART }">
														<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
														<td vAlign=top width=446 colspan=9 align=center><B></B></td>
</c:if>
														<td class=databorder width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													</tr>
<c:if test="${not empty theReport.alertFrequencyStatsList}">
<c:forEach items="${theReport.alertFrequencyStatsList}" var="theItem" varStatus="theIndex" >
 <c:set var="indexValue" value="${theIndex.index}"/> 
 <%
  	String indexVal = pageContext.getAttribute("indexValue").toString();
   int theIndex = Integer.parseInt(indexVal);
  %>

																				<%
																					if (theIndex % 2 == 0) {
																				%>
												        <tr class="datacell1">
														<%
															} else {
														%>
												        <tr class="datacell2">
														<%
															}
														%>
															<td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=198 style="padding-left: 4px;">${theItem.frequency}</td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
															<td vAlign=top width=111 align=right style="padding-right: 4px;">
<c:if test="${not empty theItem.numberOfAlertsPerFrequency}">
 <fmt:formatNumber pattern="#,###" value="${theItem.numberOfAlertsPerFrequency}" />
</c:if>
<c:if test="${empty theItem.numberOfAlertsPerFrequency}">
																	0
</c:if>
															</td>
															<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
															<%
																if (theIndex  == 0)  {
															%>
															<td class="datacell1" colspan="9" rowspan="5" align="center">
																<c:if test="${not empty AlertsReportAction.ALERT_FREQUENCY_PIECHART }"> 	
																		<table>
																		   <tr>
																		   		<td width=200 align=right style="font-size: 1em;" class="pieChart">
																					<ps:pieChart beanName="<%=AlertsReportController.ALERT_FREQUENCY_PIECHART%>"
																					alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
																					title="Alert Frequency Chart" />
																				</td>
																				<!--  Pie Chart Legend Suppress where wedge does not have data -->
																				<td width=100 align=left>
																					<table>
<c:if test="${not empty theReport.alertFrequencyStatsList}">
<c:forEach items="${theReport.alertFrequencyStatsList}" var="item" varStatus="index" >
type="com.manulife.pension.ps.service.report.noticereports.valueobject.AlertsReportFreqVO"
<%
	AlertsReportFreqVO item = (AlertsReportFreqVO)pageContext.getAttribute("item");
%>


<c:if test="${not empty item.numberOfAlertsPerFrequency}">
																										<tr>
																											<td valign="top">
																												<table border="0" cellpadding="0" cellspacing="0">
																													<tbody>
																														<tr>
																															<td
																																style="background: <%=AlertsReportController.getWedgeColor(item.getFrequency())%>;"
																																height="7" width="7"><img height="7" width="7" src="/assets/unmanaged/images/s.gif"/></td>
																														</tr>
																													</tbody>
																												</table>
																											</td>
<td valign="top">${item.frequency}</td>
																										</tr>
</c:if>
</c:forEach>
</c:if>
						 														</table>
																				</td>
																		   </tr>
																		</table>
</c:if>
															<% } %>
															</td>
															<td class=databorder width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
								                        </tr>
</c:forEach>
</c:if>
</c:if>
												</table>
												<table width="100%" border="0" cellpadding="0" cellspacing="0">
												<!--  Distribution months -->
											
											 <tr class=tablehead>
											    <td colspan=16 ><B>Distribution month selected by users</B></td>
											    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
											</tr>
											 <tr class=tablesubhead>
												<td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=222 colspan=3 style="text-align: center;"><B>Q1</B></td>
												<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=222 colspan=3 style="text-align: center;"><B>Q2</B></td>
												<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=222 colspan=3 style="text-align: center;"><B>Q3</B></td>
												<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=222 colspan=3 style="text-align: center;"><B>Q4</B></td>
												<td class=databorder width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
											<tr><td class="databorder" style="background: #FFF none repeat scroll 0% 0%;" colspan="16"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td></tr>
<c:if test="${not empty theReport.alertMonthlyDistributionList}">
											<tr class=tablesubhead>
												<td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=111 ><B>Month</B></td>
												<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=111 ><B>Notice Distribution by Due Date</B></td>
												<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=111 ><B>Month</B></td>
												<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=111 ><B>Notice Distribution by Due Date</B></td>
												<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=111 ><B>Month</B></td>
												<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=111 ><B>Notice Distribution by Due Date</B></td>
												<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=111 ><B>Month</B></td>
												<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=111 ><B>Notice Distribution by Due Date</B></td>
												<td class=databorder width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
<c:forEach items="${theReport.alertMonthlyDistributionList}" var="theItem" varStatus="theIndex" >

 <c:set var="indexValue" value="${theIndex.index}"/> 
 <% 
  String indexVal = pageContext.getAttribute("indexValue").toString();
 int theIndex = Integer.parseInt(indexVal);
 
 %>
		
															<% if (theIndex % 4 == 0 && theIndex != 4 && theIndex < 12) { 
																	if (theIndex != 0) { %>
																	    <td class=databorder width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
																        </tr>
																	<% }
															%>
														        <tr class="datacell1">
														     <% } else if (theIndex % 4 == 0 && theIndex == 4 && theIndex < 12) { %>
														        <td class=databorder width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
																 </tr>
														        <tr class="datacell2">
														     <% } %> 	
																	
															<% if (theIndex % 4 == 0 && theIndex < 12) { %>
															    <td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
																<td vAlign=top width=111 style="padding-left: 4px;">
<c:set var="monthname" >${theItem.month}</c:set>
																<c:set var= "monthvalue" value="${fn:substring(monthname,0,3)}"/>
<c:out value="${monthvalue}"/>&nbsp;${theItem.year}</td>
																<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
																<td vAlign=top width=111 align=right style="padding-right: 4px;">
																	<c:if test="${ theItem.monthInSearchRange ==true}">
																 <fmt:formatNumber pattern="#,###" value="${theItem.numberOfAlerts}" />
</c:if>
																</td>
														    <% }  else if (theIndex < 12) { %> 
															
																<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
																<td vAlign=top width=111 style="padding-left: 4px;">
<c:set var="monthname" >${theItem.month}</c:set>
																<c:set var= "monthvalue" value="${fn:substring(monthname,0,3)}"/>
<c:out value="${monthvalue}"/>&nbsp;${theItem.year}</td>
																<td width="1" vAlign=top class=datadivider><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
																<td vAlign=top width=111 align=right style="padding-right: 4px;">
																	<c:if test="${ theItem.monthInSearchRange ==true}"> 	
 <fmt:formatNumber pattern="#,###" value="${theItem.numberOfAlerts}" />
</c:if>
																</td>
								                        	<% } 
															
															    if (theIndex == 11) { %> 	
								                        			<td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
								                        	<% }  %> 
																	
</c:forEach>
							                        <tr><td class=databorder colspan=16><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td></tr>
</c:if>
												
 										 
									</table>
									</c:if>
				<BR>
				<c:if test="${printFriendly == null }" >
      			<P><div align="left">
                    <input name="backButton" type="button" class="button134" onClick="document.location='/do/tools/controlReports/'" value="control reports">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="searchContractButton" type="button" class="button134" onClick="document.location='/do/home/searchContractDetail/'" value="select contract">
                </div></P>
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
                
			</td>
		</tr>
	</tbody>
</table>

</ps:form>
