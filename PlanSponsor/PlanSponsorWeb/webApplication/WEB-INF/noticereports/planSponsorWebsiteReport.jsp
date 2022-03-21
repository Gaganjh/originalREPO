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
	import="com.manulife.pension.ps.web.noticereports.PlanSponsorWebsiteReportController"%>
<%@ page
	import="com.manulife.pension.ps.service.report.noticereports.valueobject.PlanSponsorWebsiteReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.noticereports.valueobject.PlanSponsorWebsiteReportMonthVisitsVO" %>
<%
PlanSponsorWebsiteReportData theReport = (PlanSponsorWebsiteReportData)request.getAttribute(Constants.REPORT_BEAN);
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
	 document.forms['alertsReportForm'].action = "/do/noticereports/planSponsorWebsiteReport/?task=reset";
     document.forms['alertsReportForm'].submit();
}


function doSearch(){
	 document.forms['alertsReportForm'].action = "/do/noticereports/planSponsorWebsiteReport/?task=search";
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


<ps:form method="POST" action="/do/noticereports/planSponsorWebsiteReport/" modelAttribute="alertsReportForm" name="alertsReportForm">
<input type="hidden" name="task" /><%-- input - name="alertsReportForm" --%>

	<TABLE border=0 cellSpacing=0 cellPadding=0 width=760>
		<TBODY>
			<tr>
				<td />
				<td colspan="2">
				<div id="errordivcs"><content:errors scope="session" /></div>
				</td>
			</tr>
			<TR>
				<TD width=30><img height=1 src="/assets/unmanaged/images/s.gif"
					width=30></TD>
				<td width="730" colSpan="2">
				<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
					<TBODY>
						<TR>
							<TD>
							<TABLE border=0 cellSpacing=0 cellPadding=0 width="730">
								<TBODY>
									<tr>
										<td><jsp:include page="noticeReportsTabs.jsp"
											flush="true">
											<jsp:param value="5" name="tabValue" />
										</jsp:include></td>
									</tr>
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
										</strong>from<strong> <c:if test="${printFriendly == null}">
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
											href="javascript:openChangeWindow()"></a> <c:if test="${printFriendly == null}">
											<input type=button value="reset" name="reset"
												onclick="doReset();">
												&nbsp;&nbsp;&nbsp;
<input type="button" onclick="doSearch(); return false;" name="button" value="search"/>

										</c:if> <c:if test="${printFriendly != null }" >
											<input disabled="disabled" type=button value="reset" name="reset"
												onclick="doReset();">
													&nbsp;&nbsp;&nbsp;
<input type="submit" disabled="true" value="search"/>
										</c:if></div>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <br>
										&nbsp;</td>
									</TR>
								</TBODY>
							</TABLE>

							<!-- start table -->

							<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
								<TBODY>
									<TR>
										<TD width=1></TD>
										<TD width=250></TD>
										<TD width=1></TD>
										<TD width=105></TD>
										<TD width=1></TD>
										<TD width=105></TD>
										<TD width=1></TD>
										<TD width=127></TD>
										<TD width=1></TD>
										<TD width=105></TD>
										<TD width=1></TD>
									</TR>
									<TR>
										<TD colSpan=11>&nbsp;</TD>
									</TR>
									<!-- Start of body title -->
									<TR class=tablehead>
										<TD colSpan=11>
										<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
											<TBODY>
												<TR>
													<TD class=tableheadTD width="50%">&nbsp;</TD>
													<TD class=tableheadTDinfo>&nbsp;</TD>
													<TD class=tableheadTDinfo align=right>&nbsp;</TD>
												</TR>
											</TBODY>
										</TABLE>
										</TD>
									</TR>

									<TR class=datacell1 height="25">
										<TD class=databorder width=1></TD>
										<TD bgcolor="#e6e6e6" colSpan=9 style="padding-left: 5px;"><b>Usage Statistics</b></TD>
										<TD class=databorder width=1></TD>
									</TR>

									<!-- End of body title -->
									<TR class=tablesubhead>
										<TD class=databorder width=1></TD>
										<TD vAlign=middle rowspan="3"></TD>
										<TD class=dataheaddivider vAlign=bottom width=1><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD vAlign=top align="center" colspan="7"><B>Users</B></TD>
										<TD class=databorder width=1><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									</TR>

									<TR>
										<TD class=databorder width=1></TD>
										<TD class=dataheaddivider colSpan=8><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=databorder width=1></TD>
									</TR>

									<TR class=tablesubhead>
										<TD class=databorder width=1></TD>
										<TD class=dataheaddivider vAlign=bottom width=1><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=tablesubhead vAlign=top align="center"><B>Plan
										Sponsor</B></TD>
										<TD class=dataheaddivider vAlign=bottom width=1><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=tablesubhead vAlign=top align="center"><B>TPA</B></TD>
										<TD class=dataheaddivider vAlign=bottom width=1><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=tablesubhead vAlign=top align="center"><B>Intermediary
										Contact</B></TD>
										<TD class=dataheaddivider vAlign=bottom width=1><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=tablesubhead vAlign=top align="center"><B>TotalCare TPA</B></TD>
										<TD class=databorder width=1><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									</TR>
									<c:if test="${theReport != null }">
<c:if test="${not empty theReport.visitorsUsageList}">
<c:forEach items="${theReport.visitorsUsageList}" var="theItem" varStatus="theIndex" >

 <c:set var="indexValue" value="${theIndex.index}"/> 
 <% 
  String indexVal = pageContext.getAttribute("indexValue").toString();
 
 %>


												<% if (Integer.parseInt(indexVal) % 2 == 0) { %>
												<tr class="datacell3">
													<% } else { %>
												
												<tr class="datacell1">
													<% } %>


													<td class=databorder><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=198 style="padding-left: 5px;">${theItem.statisticDescription}</td>

													<td width="1" vAlign=top class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 5px;"><fmt:formatNumber pattern="#,###" value="${theItem.planSponsorCount}" /></td>

													<td width="1" vAlign=top class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 5px;"><fmt:formatNumber pattern="#,###" value="${theItem.tpaCount}" /></td> 

													<td width="1" vAlign=top class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 5px;"><fmt:formatNumber pattern="#,###" value="${theItem.intermediaryContactCount}" /></td>

													<td width="1" vAlign=top class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 5px;"><fmt:formatNumber pattern="#,###" value="${theItem.totalCareCount}" /></td>

													<td class=databorder width=1><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
												</tr>
</c:forEach>
</c:if>

										<TR class=datacell1>
										<c:if test="${not empty theReport.monthWithMostVisits}">
<c:set var="pswReportMonths" value="${theReport.monthWithMostVisits}" />


											<TD class=databorder width=1><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD class=datacell1 valign="top" style="padding-left: 5px;">${theReport.monthWithMostVisits.description}</TD>

											<TD class=datadivider width=1><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD vAlign=top width=111 align=right style="padding-right: 5px;">
<c:if test="${not empty theReport.monthWithMostVisits.planSponsorMonths}">
<c:forEach items="${theReport.monthWithMostVisits.planSponsorMonths}" var="pswMonths" varStatus="theIndex" >





${pswMonths}<br/>
</c:forEach>
</c:if>
<c:if test="${empty theReport.monthWithMostVisits.planSponsorMonths}">
											N/A
</c:if></TD>
											<TD class=datadivider width=1><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD vAlign=top width=111 align=right style="padding-right: 5px;">
<c:if test="${not empty theReport.monthWithMostVisits.tpaMonths}">
<c:forEach items="${theReport.monthWithMostVisits.tpaMonths}" var="tpaMonths" varStatus="theIndex" >




${tpaMonths}<br/>
</c:forEach>
</c:if>
<c:if test="${empty theReport.monthWithMostVisits.tpaMonths}">
											N/A
</c:if></TD>
											<TD class=datadivider width=1><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD vAlign=top width=111 align=right style="padding-right: 5px;">
<c:if test="${not empty theReport.monthWithMostVisits.intermediaryContactMonths}">
<c:forEach items="${theReport.monthWithMostVisits.intermediaryContactMonths}" var="incMonths" varStatus="theIndex" >





${incMonths}<br/>
</c:forEach>
</c:if>
<c:if test="${empty theReport.monthWithMostVisits.intermediaryContactMonths}">
											N/A
</c:if></TD>
											<TD class=datadivider width=1><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD vAlign=top width=111 align=right style="padding-right: 5px;">
<c:if test="${not empty theReport.monthWithMostVisits.totalCareMonths}">
<c:forEach items="${theReport.monthWithMostVisits.totalCareMonths}" var="totalCareMonths" varStatus="theIndex" >





${totalCareMonths}<br/>
</c:forEach>
</c:if>
<c:if test="${empty theReport.monthWithMostVisits.totalCareMonths}">
											N/A
</c:if>
											</TD>
											<TD class=databorder width=1><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>

                                     </c:if>
										</TR>
										<TR class=datacell1 height="25">
											<TD class=databorder width=1></TD>
											<TD bgcolor="#e6e6e6" colSpan=9 style="padding-left: 5px;"><b>Pages Visited</b></TD>
											<TD class=databorder width=1></TD>
										</TR>
<c:if test="${not empty theReport.pagesVisitedList}">
<c:forEach items="${theReport.pagesVisitedList}" var="theItem" varStatus="theIndex" >
 <c:set var="indexValue" value="${theIndex.index}"/> 
 <% 
  String indexVal = pageContext.getAttribute("indexValue").toString();
 
 %>



												<% if (Integer.parseInt(indexVal) % 2 == 0) { %>
												<tr class="datacell1">
													<% } else { %>
												
												<tr class="datacell3">
													<% } %>


													<td class=databorder><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=198 style="padding-left: 5px;">${theItem.pageName}</td>

													<td width="1" vAlign=top class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 5px;"><fmt:formatNumber pattern="#,###" value="${theItem.planSponsorCount}" /> </td>

													<td width="1" vAlign=top class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 5px;"><fmt:formatNumber pattern="#,###" value="${theItem.tpaCount}" /></td>

													<td width="1" vAlign=top class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 5px;"><fmt:formatNumber pattern="#,###" value="${theItem.intermediaryContactCount}" /></td>

													<td width="1" vAlign=top class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
<td vAlign=top width=111 align=right style="padding-right: 5px;"><fmt:formatNumber pattern="#,###" value="${theItem.totalCareCount}" /></td>

													<td class=databorder width=1><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
												</tr>
</c:forEach>
</c:if>
									</c:if>
										<!-- End of Last line -->
										<TR>
											<TD class=databorder colSpan=11><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
										<TR>
											<TD colSpan=11>
											</TD>
										</TR>
								</TBODY>
							</TABLE>
							<c:if test="${printFriendly == null }" >
								<P>
								<div align="left"><input name="backButton" type="button"
									class="button134"
									onClick="document.location='/do/tools/controlReports/'"
									value="control reports"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input name="searchContractButton" type="button"
									class="button134"
									onClick="document.location='/do/home/searchContractDetail/'"
									value="select contract"></div>
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
