<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page
	import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page
	import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page
	import="com.manulife.pension.ps.service.report.profiles.valueobject.UserManagementChangesExternalReportData"%>


<jsp:useBean id="userManagementChangesReportForm" scope="request"
	type="com.manulife.pension.ps.web.profiles.UserManagementChangesReportForm" />



<%
	UserManagementChangesExternalReportData theReport = (UserManagementChangesExternalReportData) request
			.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport", theReport, PageContext.PAGE_SCOPE);
%>

<%
	String ASC_DIRECTION = ReportSort.ASC_DIRECTION;
	pageContext.setAttribute("ASC_DIRECTION", ASC_DIRECTION, PageContext.PAGE_SCOPE);
%>

<%
	boolean errors = false;
%>

<script type="text/javascript">
	var calFrom, calTo;

	// Copied and modified from calendar.js
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

//		var fromDateValue = document.getElementsByName("fromDate")[0].value;
//		var fromDate = cal_prs_monthday(fromDateValue, true);

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
		var today = new Date();
		today.setDate(today.getDate());
		document.getElementsByName("fromDate")[0].value = formatDate(today, true);
	   }
	   calFrom.popup_no_alert();
	}

	function popupCalendarTo() {
	   var v = document.getElementsByName("toDate")[0].value;
	   if (v == null || v.length == 0) {
		var today = new Date();
		today.setDate(today.getDate());
		document.getElementsByName("toDate")[0].value = formatDate(today, true);
	   }
	   calTo.popup_no_alert();
	}

	function doReset() {
		document.userManagementChangesReportForm.task.value = 'default';
		document.userManagementChangesReportForm.submit();
	}

	function initPage() {
		setupCalendar();
	}

	if (window.addEventListener) {
		window.addEventListener('load', initPage, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', initPage);
	}
	
	/**
	* This function is used to trim the blank spaces.
	*/
	String.prototype.trim = function(){ 
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	// From struts 1.1,  
	function isValidDate(day, month, year) {
	    if (month < 1 || month > 12) {
	              return false;
	    }
	    if (day < 1 || day > 31) {
	        return false;
	    }
	    if ((month == 4 || month == 6 || month == 9 || month == 11) &&
	        (day == 31)) {
	        return false;
	    }
	    if (month == 2) {
	        var leap = (year % 4 == 0 &&
	                   (year % 100 != 0 || year % 400 == 0));
	        if (day>29 || (day == 29 && !leap)) {
	            return false;
	        }
	    }
	    if (year < 1000) {
	    	return false;
	    }
	    
	    return true;
	}
	
	function getDateDifference(fromDate, toDate) {   
	    var date1 = new Date(fromDate);
		var date2 = new Date(toDate);
		var diffDays = parseInt((date2 - date1) / (1000 * 60 * 60 * 24)); 
		return diffDays;
	} 
	
	function validateSearch() {
		var isValid = true;
		var dateExp = new RegExp("(^\\d{1,2})/(\\d{1,2})/(\\d{4})$");
		var contractNumber = (document.getElementsByName("contractNumber")[0].value).trim();		
		var fromDateValue = document.getElementsByName("fromDate")[0].value;
		var toDateValue = document.getElementsByName("toDate")[0].value
		var matchesFrom = dateExp.exec(fromDateValue);
		var matchesTo = dateExp.exec(toDateValue);
	    if ( matchesFrom == null || matchesTo == null) {   
	    	isValid = false;
	    	alert("Incorrect Date Format.");
	    }		
	    if (!isValidDate(matchesFrom[2], matchesFrom[1], matchesFrom[3])) {
	    	isValid = false;
	    	alert("Incorrect From Date");
	    } else if(!isValidDate(matchesTo[2], matchesTo[1], matchesTo[3])) {
	    	isValid = false;
	    	alert("Incorrect To Date");
	    } else if(contractNumber == "" && getDateDifference(fromDateValue, toDateValue) > 7) {
			isValid = false;
			alert("If Date difference is more than 7 days? Please provide Contract Number.");
		}		
		return isValid;
	} 

</script>
<%
	String formActionName = "/profiles/userManagementChanges"
			+ (userManagementChangesReportForm.isExternalReport() ? "External" : "Internal") + "/";
	String actionName = "/do" + formActionName;
%>
<ps:form method="POST" action="${formActionName}"  name="userManagementChangesReportForm" modelAttribute="userManagementChangesReportForm"
	onsubmit="return validateSearch();">

	<table cellSpacing=0 cellPadding=0 width=760 border=0>
		<tbody>
			<c:if test="${not empty sessionScope.psErrors}">
				<c:set var="errorsExist" value="${true}" scope="page" />
				<tr>
					<td />
					<td colspan="2">
						<div id="errordivcs">
							<content:errors scope="session" />
						</div>
					</td>
				</tr>
				<%
					errors = true;
				%>
			</c:if>
			<tr>
				<td width=30><img height=1 src="/assets/unmanaged/images/s.gif"
					width=30></td>
				<td width=715><A name=TopOfPage></A>
					<table cellSpacing=0 cellPadding=0 width=525 border=0>
						<tbody>
							<tr>
								<td><img height=20 src="/assets/unmanaged/images/s.gif"
									width=1> 
									<input type="hidden" name="task" value="filter" />
									<form:hidden path="pageNumber" /> 
									<form:hidden path="sortField" /> 
									<form:hidden path="sortDirection" value="${ASC_DIRECTION}" />

									<table width="730" border="0" cellpadding="0" cellspacing="0">
										<tbody>
											<tr>
												<td colspan="4"></td>
											</tr>
											<tr>
												<td colspan="4" bgcolor="#CCCCCC">&nbsp;&nbsp;<b>Search
												</b><img src="/assets/unmanaged/images/s.gif" width=1 height=25>
												</td>
											</tr>
											<tr>
												<c:if test="${userManagementChangesReportForm.externalReport ==false}">
													<td width="140" valign="top" class="filterSearch"><strong>Changed
															by </strong>(last name) <br> <c:if
															test="${empty param.printFriendly }">
															<form:input path="changedBy" />
														</c:if> <c:if test="${not empty param.printFriendly }">
															<form:input path="changedBy" disabled="true" />
														</c:if></td>
													<td>
														<table width="256" border="0" cellpadding="0"
															cellspacing="0">
															<td width="103" valign="top" class="filterSearch"><strong>Contract
																	number</strong><br> <c:if
																	test="${empty param.printFriendly }">
																	<form:input path="contractNumber" maxlength="7"
																		size="7" />
																</c:if> <c:if test="${not empty param.printFriendly }">
																	<form:input path="contractNumber" disabled="true"
																		maxlength="7" size="7" />
																</c:if></td>
															<td width="153" valign="top" class="filterSearch"><strong>Action</strong><br>
																<c:if test="${empty param.printFriendly }">
																	<form:select path="action" size="1">
																		<form:options items="${userManagementChangesReportForm.getActionList()}" itemValue="value"
																			itemLabel="label" />
																	</form:select>
																</c:if> <c:if test="${not empty param.printFriendly }">
																	<form:select disabled="true" path="action" size="1">
																		<form:options items="${userManagementChangesReportForm.getActionList()}" itemValue="value"
																			itemLabel="label" />
																	</form:select>
																</c:if></td>
														</table>
													</td>
												</c:if>
												<c:if test="${userManagementChangesReportForm.externalReport ==true}">
													<td width="145" valign="top" class="filterSearch"><strong>Contract
															number</strong><br> <c:if
															test="${empty param.printFriendly }">
															<form:input path="contractNumber" maxlength="7" size="7" />
														</c:if> <c:if test="${not empty param.printFriendly }">
															<form:input path="contractNumber" disabled="true"
																maxlength="7" size="7" />
														</c:if></td>
													<td width="251" valign="top" class="filterSearch"><strong>Action</strong><br>
														<c:if test="${empty param.printFriendly }">
															<form:select path="action" size="1">
																<form:options items="${userManagementChangesReportForm.getActionList()}" itemValue="value"
																	itemLabel="label" />
															</form:select>
														</c:if> <c:if test="${not empty param.printFriendly }">
															<form:select disabled="true" path="action" size="1">
																<form:options items="${userManagementChangesReportForm.getActionList()}" itemValue="value"
																	itemLabel="label" />
															</form:select>
														</c:if></td>
												</c:if>

												<td width="169" rowspan="2" valign="top"
													class="filterSearch"><strong>User role</strong><br>
													<c:if test="${empty param.printFriendly }">
														<form:checkbox path="trusteeSelected"/>&nbsp;Trustee (TR)&nbsp;
														<br>
													</c:if> <c:if test="${not empty param.printFriendly }">
														<form:checkbox path="trusteeSelected" disabled="true"/>&nbsp;Trustee (TR)&nbsp;
														<br>
													</c:if> <input type="hidden" name="trusteeSelected" value="false">
													<c:if test="${empty param.printFriendly }">
														<form:checkbox path="authorizedSignorSelected"/>&nbsp;Authorized signer (AS)&nbsp;
														<br>
													</c:if> <c:if test="${not empty param.printFriendly }">
														<form:checkbox path="authorizedSignorSelected"
															disabled="true"/>&nbsp;Authorized signer (AS)&nbsp;
														<br>
													</c:if> <input type="hidden" name="authorizedSignorSelected"
													value="false"> <c:if
														test="${empty param.printFriendly }">
														<form:checkbox path="administrativeContactSelected"/>&nbsp;Administrative contact (AC)&nbsp;
														<br>
													</c:if> <c:if test="${not empty param.printFriendly }">
														<form:checkbox path="administrativeContactSelected"
															disabled="true"/>&nbsp;Administrative contact (AC)&nbsp;
														<br>
													</c:if> <input type="hidden" name="administrativeContactSelected"
													value="false"> <c:if
														test="${userManagementChangesReportForm.externalReport ==false}">
														<c:if test="${empty param.printFriendly }">
															<form:checkbox path="payrollAdministratorSelected"/>&nbsp;Payroll administrator (PA)&nbsp;
															<br>
														</c:if>
														<c:if test="${not empty param.printFriendly }">
															<form:checkbox path="payrollAdministratorSelected"
																disabled="true"/>&nbsp;Payroll administrator (PA)&nbsp;
															<br>
														</c:if>
														<input type="hidden" name="payrollAdministratorSelected"
															value="false">
													</c:if></td>
												<td width="165" rowspan="2" valign="top"
													class="filterSearch"><Br> <c:if
														test="${userManagementChangesReportForm.externalReport ==true}">
														<c:if test="${empty param.printFriendly }">
															<form:checkbox path="payrollAdministratorSelected"/>&nbsp;Payroll administrator (PA)&nbsp;
															<br>
														</c:if>
														<c:if test="${not empty param.printFriendly }">
															<form:checkbox path="payrollAdministratorSelected"
																disabled="true"/>&nbsp;Payroll administrator (PA)&nbsp;
															<br>
														</c:if>
														<input type="hidden" name="payrollAdministratorSelected"
															value="false">
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:checkbox path="intermediaryContactSelected"/>&nbsp;Intermediary contact (IC)&nbsp;
														<br>
													</c:if> <c:if test="${not empty param.printFriendly }">
														<form:checkbox path="intermediaryContactSelected"
															disabled="true"/>&nbsp;Intermediary contact (IC)&nbsp;
														<br>
													</c:if> <input type="hidden" name="intermediaryContactSelected"
													value="false"> <c:if
														test="${empty param.printFriendly }">
														<form:checkbox path="planSponsorUserSelected"/>&nbsp;Plan sponsor user (PS)&nbsp;
														<br>
													</c:if> <c:if test="${not empty param.printFriendly }">
														<form:checkbox path="planSponsorUserSelected"
															disabled="true"/>&nbsp;Plan sponsor user (PS)&nbsp;
														<br>
													</c:if> <input type="hidden" name="planSponsorUserSelected"
													value="false"> <c:if
														test="${empty param.printFriendly }">
														<form:checkbox path="tpaSelected"/>&nbsp;TPA (TP)&nbsp;
													</c:if> <c:if test="${not empty param.printFriendly }">
														<form:checkbox path="tpaSelected" disabled="true"/>&nbsp;TPA (TP)&nbsp;
													</c:if> <input type="hidden" name="tpaSelected" value="false">
												</td>
											</tr>
											<tr>
												<td width="145" valign="top" class="filterSearch"><c:if
														test="${userManagementChangesReportForm.externalReport ==true}">
														<strong>Team code </strong>
														<br>
													</c:if> <c:if test="${userManagementChangesReportForm.externalReport ==false}">
														<strong>Changed by team code </strong>
														<br>
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:select path="teamCode" size="1">
															<form:options items="${userManagementChangesReportForm.getTeamCodeList()}" itemValue="value"
																			itemLabel="label" />
														</form:select>
													</c:if> <c:if test="${not empty param.printFriendly }">
														<form:select disabled="true" path="teamCode" size="1">
															<form:options items="${userManagementChangesReportForm.getTeamCodeList()}" itemValue="value"
																			itemLabel="label" />
														</form:select>
													</c:if></td>
												<td valign="top" class="filterSearch"><strong><strong>Date
															of change</strong><br> </strong>from<strong> 
															<c:if test="${empty param.printFriendly }">
															<form:input path="fromDate" maxlength="10" size="10"
																cssClass="inputField" />
															<img src="/assets/unmanaged/images/cal.gif" width="16"
																height="16" onclick="javascript:popupCalendarFrom()">
															</strong>
													to<strong> <form:input path="toDate"
															maxlength="10" size="10" cssClass="inputField" /> <img
														src="/assets/unmanaged/images/cal.gif" width="16"
														height="16" onclick="javascript:popupCalendarTo()"></strong><br>
													</c:if>
													<c:if test="${not empty param.printFriendly }">
														<form:input path="fromDate" disabled="true" maxlength="10"
															size="10" cssClass="inputField" />
														<img src="/assets/unmanaged/images/cal.gif" width="16"
															height="16">
														</strong> to<strong> <form:input path="toDate"
																disabled="true" maxlength="10" size="10"
																cssClass="inputField" /> <img
															src="/assets/unmanaged/images/cal.gif" width="16"
															height="16"></strong>
														<br>
													</c:if>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
												</td>
											</tr>
											<tr>
												<td colspan="4" valign="bottom" class="filterSearch">
													<div align="right">
														<a href="javascript:openChangeWindow()"></a>
														<c:if test="${empty param.printFriendly }">
															<input type=button value="reset" name="reset"
																onclick="doReset();">
													&nbsp;&nbsp;&nbsp;
<input type="submit" value="search" />
														</c:if>
														<c:if test="${not empty param.printFriendly }">
															<input disabled="true" type=button value="reset"
																name="reset" onclick="doReset();">
														&nbsp;&nbsp;&nbsp;
<input type="submit" disabled="true" value="search" />
														</c:if>
													</div>
												</td>
											</tr>
										</tbody>
									</table>
									<table cellSpacing=0 cellPadding=0 width=525 border=0>
										<tbody>
											<tr></tr>
										</tbody>
									</table>

									<table cellSpacing=0 cellPadding=0 width=760 border=0>
										<tbody>
											<tr>
												<td width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=name_column width=150><img height=1
													src="/assets/unmanaged/images/s.gif" width=125></td>
												<c:if test="${userManagementChangesReportForm.externalReport ==false}">
													<td width=1><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
													<td class=name_column width=150><img height=1
														src="/assets/unmanaged/images/s.gif" width=125></td>
												</c:if>
												<td width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=access_level_column width=150><img height=1
													src="/assets/unmanaged/images/s.gif" width=100></td>
												<td width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td width=115><img height=1
													src="/assets/unmanaged/images/s.gif" width=115></td>
												<td width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=id_column width=115><img height=1
													src="/assets/unmanaged/images/s.gif" width=100></td>
												<td width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=password_status_column width=50 colSpan=2><img
													height=1 src="/assets/unmanaged/images/s.gif" width=50></td>
												<td width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=password_status_column width=50>&nbsp;</td>
												<td width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=password_status_column width=50>&nbsp;</td>
												<td width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=password_status_column width=225><img
													height=1 src="/assets/unmanaged/images/s.gif" width=200></td>
												<td class=password_status_column width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=password_status_column width=200>&nbsp;</td>
												<td width=2><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
											<tr class=tablehead>
												<td class=tableheadTD1 colSpan=7>&nbsp;</td>
												<c:if test="${userManagementChangesReportForm.externalReport ==false}">
													<td colSpan=10 class=tableheadTD><strong><report:recordCounter
																report="theReport" label="Records" /></strong></td>
												</c:if>
												<c:if test="${userManagementChangesReportForm.externalReport ==true}">
													<td colSpan=8 class=tableheadTD><strong><report:recordCounter
																report="theReport" label="Records" /></strong></td>
												</c:if>
												<td colSpan=4 class=tableheadTD><div align="right">
														<strong><report:pageCounter report="theReport"
																arrowColor="white" formName="userManagementChangesReportForm" /></strong>
													</div></td>
												<td class=databorder width=2><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
											<tr class=tablesubhead>
												<td class=databorder><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<c:if test="${userManagementChangesReportForm.externalReport ==false}">
													<td vAlign=top width=150><report:sort
															field="sortByChangeByTeamCode" direction="asc" formName="userManagementChangesReportForm">
															<B>Changed by team code</B>
														</report:sort></td>
												</c:if>

												<c:if test="${userManagementChangesReportForm.externalReport ==true}">
													<td vAlign=top width=150><report:sort
															field="sortByChangeByTeamCode" direction="asc" formName="userManagementChangesReportForm">
															<b>Team code/</b>
														</report:sort> <Br>
													<b>JH rep </B></td>
												</c:if>
												<td width="1" vAlign=top class=datadivider><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>

												<c:if test="${userManagementChangesReportForm.externalReport ==false}">
													<td width=150 vAlign=top noWrap><b><report:sort
																field="sortByUserName" direction="asc" formName="userManagementChangesReportForm">Changed by </report:sort></b>
													</td>
													<td class=datadivider vAlign=top><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>

													<td vAlign=top noWrap width=115><B><report:sort
																field="sortByContractNumber" direction="asc" formName="userManagementChangesReportForm">Contract number</report:sort>/<br>contract
															name</B></td>
													<td class=datadivider vAlign=top><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>

													<td vAlign=top width=150><report:sort
															field="sortByTeamCode" direction="asc" formName="userManagementChangesReportForm">
															<b>Team code/</b>
														</report:sort> <Br>
													<b>JH rep </B></td>
													</td>
												</c:if>
												<c:if test="${userManagementChangesReportForm.externalReport ==true}">
													<td vAlign=top noWrap width=150><B><report:sort
																field="sortByContractNumber" direction="asc" formName="userManagementChangesReportForm">Contract number</report:sort>/<br>contract
															name</B></td>
													<td class=datadivider vAlign=top><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
													<td width=115 vAlign=top noWrap><b><report:sort
																field="sortByUserName" direction="asc" formName="userManagementChangesReportForm">Changed by </report:sort></b>
													</td>
												</c:if>

												<td class=datadivider vAlign=top><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=115><b><report:sort
															field="sortByUserRole" direction="asc" formName="userManagementChangesReportForm">User role</report:sort>/<Br>user
														name</b></td>
												<td class=datadivider vAlign=top><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top align=left colSpan=2><B><report:sort
															field="sortByDate" direction="asc" formName="userManagementChangesReportForm">Dateof change</report:sort></B>
												</td>
												<td width="1" vAlign=top class=datadivider><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top align=left><b><report:sort
															field="sortByAction" direction="asc" formName="userManagementChangesReportForm">Action</report:sort></b>
												</td>
												<td width="1" vAlign=top class=datadivider><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top align=left><b><report:sort
															field="sortByItem" direction="asc" formName="userManagementChangesReportForm">Item</report:sort></b></td>
												<td width="1" vAlign=top class=datadivider><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td width="225" align=left vAlign=top><b>Value</b></td>
												<td width="1" vAlign=top class=datadivider><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td width="200" align=left vAlign=top><b>Attribute</b>
												</td>
												<td class=databorder width=2><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>


											<%
												int rowIndex = 1;
													String rowClass = "";
											%>
											<c:if test="${empty theReport.details }">
												<tr class="<%=rowClass%>">
													<td class=databorder><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
													<c:if test="${userManagementChangesReportForm.externalReport ==true}">
														<td vAlign=center colspan="18" width="100%">No
															profiles changes made for the current search condition</td>
													</c:if>
													<c:if test="${userManagementChangesReportForm.externalReport ==false}">
														<td vAlign=center colspan="20" width="100%">No
															profiles changes made for the current search condition</td>
													</c:if>
													<td class=databorder vAlign=top width=2><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
												</tr>
											</c:if>
											<c:forEach items="${theReport.details}" var="historyEvent">
												<%
													if (rowIndex % 2 == 0) {
																rowClass = "datacell1";
															} else {
																rowClass = "datacell3";
															}
															rowIndex++;
												%>
												<tr class="<%=rowClass%>">
													<td class=databorder><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>

													<c:if test="${userManagementChangesReportForm.externalReport ==false}">
														<td vAlign=top width=150>
															${historyEvent.changedByTeamCode}</td>
													</c:if>

													<c:if test="${userManagementChangesReportForm.externalReport ==true}">
														<td vAlign=top width=150>${historyEvent.teamCode}<br>
															${historyEvent.jhRepName}
														</td>
													</c:if>
													<td width="1" vAlign=center class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													<!-- begin -->
													<c:if test="${userManagementChangesReportForm.externalReport ==false}">
														<td width=150 vAlign=top noWrap>
															${historyEvent.changedByLastName},
															${historyEvent.changedByFirstName}</td>
														<td class=datadivider vAlign=top><img height=1
															src="/assets/unmanaged/images/s.gif" width=1></td>

														<td vAlign=top noWrap width=115><c:if
																test="${historyEvent.contractNumber !=0}">
${historyEvent.contractNumber}
</c:if> <br> ${historyEvent.contractName}</td>
														<td class=datadivider vAlign=top><img height=1
															src="/assets/unmanaged/images/s.gif" width=1></td>

														<td vAlign=top width=150>${historyEvent.teamCode}<br>
															${historyEvent.jhRepName}
														</td>
														</td>
													</c:if>
													<c:if test="${userManagementChangesReportForm.externalReport ==true}">
														<td vAlign=top noWrap width=150><c:if
																test="${historyEvent.contractNumber !=0}">
${historyEvent.contractNumber}
</c:if> <br> ${historyEvent.contractName}</td>
														<td class=datadivider vAlign=top><img height=1
															src="/assets/unmanaged/images/s.gif" width=1></td>
														<td width=115 vAlign=top noWrap><c:choose>
																<c:when
																	test="${(historyEvent.actionCode eq 'ETP') or (historyEvent.actionCode eq 'ECL')}">
																	<c:set var="profileId"
																		value="${historyEvent.profileId}" scope="page" />
																	<c:set var="changedByProfileId"
																		value="${historyEvent.changedByProfileId}"
																		scope="page" />
																	<c:if test="${profileId eq changedByProfileId}">
																		<c:if test="${not empty historyEvent.lastName}">
${historyEvent.lastName},
</c:if>
${historyEvent.firstName}
										</c:if>
																	<c:if test="${profileId ne changedByProfileId}">
${historyEvent.changedByLastName}, ${historyEvent.changedByFirstName}
										</c:if>
																</c:when>
																<c:otherwise>
${historyEvent.changedByLastName}, ${historyEvent.changedByFirstName}
                    					</c:otherwise>
															</c:choose></td>
													</c:if>
													<td class=datadivider vAlign=center><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
													<td width=115 vAlign=center><c:if
															test="${empty historyEvent.roleCode}">
													     N/A
</c:if> <c:if test="${not empty historyEvent.roleCode}">
${historyEvent.roleCode}
</c:if> <Br> <c:if test="${not empty historyEvent.lastName}">
${historyEvent.lastName},
</c:if> ${historyEvent.firstName}</td>
													<td class=datadivider vAlign=center><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
													<td colSpan=2 align=left vAlign=center><fmt:formatDate
															value="${historyEvent.createdTs}"
															pattern="MM/dd/yyyy hh:mm" /></td>
													<td width="1" vAlign=center class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													<td vAlign=center align=left>
														${historyEvent.actionName}</td>
													<td width="1" vAlign=center class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													<td vAlign=center align=left><c:if
															test="${historyEvent.itemName !=Unknown}">
${historyEvent.itemName}
</c:if></td>
													<td width="1" vAlign=center class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													<td width="225" align=left vAlign=center><c:if
															test="${historyEvent.newValue != ''}"> 
O: ${historyEvent.oldValueDisplayName}
														<Br>
N: ${historyEvent.newValueDisplayName}
</c:if></td>
													<td width="1" vAlign=center class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													<td width="200" align=left vAlign=center>
														${historyEvent.attribute}</td>
													<td class=databorder vAlign=top width=2><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
												</tr>
											</c:forEach>


											<tr>
												<td class=databorder width=1 height=1></td>
												<c:if test="${userManagementChangesReportForm.externalReport ==false}">
													<td class=databorder colSpan=23 height=1><img height=1
														src="/assets/unmanaged/images/s.gif" width=5></td>
												</c:if>
												<c:if test="${userManagementChangesReportForm.externalReport ==true}">
													<td class=databorder colSpan=21 height=1><img height=1
														src="/assets/unmanaged/images/s.gif" width=5></td>
												</c:if>

											</tr>
											<tr>
												<td colspan="22">&nbsp;</td>
											</tr>
											<tr>
												<TD colSpan="7">&nbsp;</TD>
												<c:if test="${userManagementChangesReportForm.externalReport ==false}">
													<TD colSpan="10"><strong><report:recordCounter
																report="theReport" label="Records" /></strong></TD>
												</c:if>
												<c:if test="${userManagementChangesReportForm.externalReport ==true}">
													<TD colSpan="8"><strong><report:recordCounter
																report="theReport" label="Records" /></strong></TD>
												</c:if>
												<TD colSpan="4"><div align="right">
														<strong><report:pageCounter report="theReport"
																arrowColor="black" formName="userManagementChangesReportForm" /></strong>
													</div></TD>
												<td height=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
										</tbody>
									</table></td>
							</tr>
						</tbody>
					</table> <BR> <c:if test="${empty param.printFriendly }">
						<P>
						<div align="left">
							<input name="backButton" type="button" class="button134"
								onClick="document.location='/do/tools/controlReports/'"
								value="back"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
								name="searchContractButton" type="button" class="button134"
								onClick="document.location='/do/home/searchContractDetail/'"
								value="select contract">
						</div>
						</P>
					</c:if>
					<P class=footnote>
						<BR> If you have any questions regarding the information
						shown here please refer to the 'Getting help' section or contact
						your John Hancock USA Client Account Representative. <BR>
					</P>
					<P class=disclaimer>&nbsp;</P></td>
			</tr>
		</tbody>
	</table>

</ps:form>
