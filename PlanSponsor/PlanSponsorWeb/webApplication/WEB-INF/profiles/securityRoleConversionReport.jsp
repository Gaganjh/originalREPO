<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page
	import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@page
	import="com.manulife.pension.service.security.report.valueobject.SecurityRoleConversionReportData"%>
<%@page
	import="com.manulife.pension.service.security.report.valueobject.SecurityRoleConversionDetails"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page
	import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%
	SecurityRoleConversionReportData theReport = (SecurityRoleConversionReportData) request
			.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport", theReport, PageContext.PAGE_SCOPE);
	UserProfile userProfile = (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile", userProfile, PageContext.PAGE_SCOPE);
%>

<%
	String ASC_DIRECTION = ReportSort.ASC_DIRECTION;
	pageContext.setAttribute("ASC_DIRECTION", ASC_DIRECTION, PageContext.PAGE_SCOPE);
%>





<jsp:useBean id="securityRoleConversionReportForm" scope="request"
	type="com.manulife.pension.ps.web.profiles.SecurityRoleConversionReportForm" />





<content:contentBean
	contentId="<%=ContentConstants.NO_DEFAULT_CONVERTED_CONTRACT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="noDefaultSearchResults" />

<content:contentBean
	contentId="<%=ContentConstants.NO_CONVERTED_CONTRACT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noSearchResults" />

<%
	boolean errors = false;
%>

<c:if test="${empty param.param.printFriendly}">
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
	   calFrom.popup();
	}
	
	function popupCalendarTo() {
	   var v = document.getElementsByName("toDate")[0].value;
	   if (v == null || v.length == 0) {
		var today = new Date();
		today.setDate(today.getDate());
		document.getElementsByName("toDate")[0].value = formatDate(today, true);
	   }
	   calTo.popup();
	}


	function initPage() {
		setupCalendar();
	}
	
	if (window.addEventListener) {
		window.addEventListener('load', initPage, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', initPage);
	}
	
function doReset() {
	document.securityRoleConversionReportForm.task.value = 'default';
	document.securityRoleConversionReportForm.submit();
}

function doDownload() {
	document.securityRoleConversionReportForm.task.value = 'download';
	document.securityRoleConversionReportForm.submit();
	
}
function doSearch() {
	document.securityRoleConversionReportForm.task.value = 'filter';
	document.securityRoleConversionReportForm.submit();
}

</script>
</c:if>
<ps:form modelAttribute="securityRoleConversionReportForm"
	name="securityRoleConversionReportForm"
	action="/do/profiles/securityRoleConversionReport/">
	<input type="hidden" name="task" value="filter" />
	<input type="hidden" name="sortField" />
	<input type="hidden" name="sortDirection" value="${ASC_DIRECTION}" />
	<TABLE cellSpacing=0 cellPadding=0 width=760 border=0>
		<TBODY>
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
			<TR>
				<TD width=30><IMG height=1 src="/assets/unmanaged/images/s.gif"
					width=30></TD>
				<TD width=715>
					<TABLE cellSpacing=0 cellPadding=0 width=525 border=0>
						<TBODY>

							<TR>
								<TD><IMG height=20 src="/assets/unmanaged/images/s.gif"
									width=1>


									<TABLE width="730" border="0" cellpadding="0" cellspacing="0">
										<TBODY>
											<TR>
												<TD colspan="4"></TD>
											</TR>
											<TR>
												<TD colspan="4" valign="middle" bgcolor="#CCCCCC">&nbsp;&nbsp;<b>Search
												</b><IMG src="/assets/unmanaged/images/s.gif" width=1 height=25></TD>
											</TR>

											<TR>
												<TD width="178" valign="top" class="filterSearch"><strong>Contract
														number </strong><br> <c:if test="${not empty param.printFriendly }">
														<form:input path="contractNumber" disabled="true" />
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:input path="contractNumber" />
													</c:if></TD>
												<TD width="279" valign="top" class="filterSearch"><strong>Date
														of change </strong><br> from <c:if
														test="${not empty param.printFriendly }">
														<form:input path="fromDate" disabled="true" maxlength="10"
															size="10" />
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:input path="fromDate" maxlength="10" size="10" />
													</c:if> <strong><img
														src="/assets/unmanaged/images/cal.gif" width="16"
														height="16" onclick="javascript:popupCalendarFrom()"></strong>
													to <c:if test="${not empty param.printFriendly }">
														<form:input path="toDate" disabled="true" maxlength="10"
															size="10" />
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:input path="toDate" maxlength="10" size="10" />
													</c:if> <strong><img
														src="/assets/unmanaged/images/cal.gif" width="16"
														height="16" onclick="javascript:popupCalendarTo()"></strong><br>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)<br>
												</TD>
												<TD width="89" valign="top" class="filterSearch"><strong>Team</strong>
													<strong>code</strong> <br> <c:if
														test="${not empty param.printFriendly }">
														<form:select path="teamCode" disabled="true">
															<form:options items="${securityRoleConversionReportForm.getTeamCodeList()}" itemValue="value"
																			itemLabel="label"/>
														</form:select>
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:select path="teamCode">
															<form:options items="${securityRoleConversionReportForm.getTeamCodeList()}" itemValue="value"
																			itemLabel="label"/>
														</form:select>
													</c:if> <br></TD>
												<TD width="184" valign="bottom" class="filterSearch">
													<div align="center">
														<br>
														<c:if test="${empty param.printFriendly }">
															<input type="button" name="resetBtn" value="reset"
																onclick="doReset();" />&nbsp;&nbsp;&nbsp;
	            							<input type="button" name="searchBtn" value="search"
																onclick="doSearch();" />
														</c:if>
													</div>
												</TD>
											</TR>
										</TBODY>
									</TABLE> <br> <br>



									<TABLE cellSpacing=0 cellPadding=0 width=730 border=0>
										<TBODY>
											<TR>
												<TD width=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD class=name_column width=150><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=175></TD>
												<TD width=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD class=access_level_column width=150><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=175></TD>
												<TD width=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD width=50><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=70></TD>
												<TD width=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD class=password_status_column width=50 colSpan=2><IMG
													height=1 src="/assets/unmanaged/images/s.gif" width=50></TD>
												<TD width=1 class=password_status_column><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD class=password_status_column width=150><IMG
													height=1 src="/assets/unmanaged/images/s.gif" width=175></TD>
												<TD width=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD class=password_status_column width=70><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=70></TD>
												<TD width=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
											</TR>
											<!-- table header -->
											<TR class=tablehead>
												<TD class=tableheadTD1 colSpan="6">&nbsp;</TD>
												<%
													if ( errors == false) {
												%>
												<TD colSpan="6" class=tableheadTD><strong><report:recordCounter
															report="theReport" label="Records" /></strong></TD>
												<TD class=tableheadTD>
													<div align="right">
														<strong><report:pageCounter report="theReport" /></strong>
													</div>
												</TD>
												<%
													} else {
												%>
												<TD colSpan="7" class=tableheadTD>&nbsp;</TD>
												</TD>
												<%
													}
												%>

												<TD class=databorder width=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
											</TR>
											<TR class=tablesubhead>
												<TD width="1" class=databorder><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD vAlign=top width=150><report:sort
														field="<%=SecurityRoleConversionReportData.SORT_FIELD_TEAM_CODE%>"
														direction="asc">
														<B>Team code/</B>
													</report:sort><Br> <B>JH rep </B></TD>
												<TD width="1" vAlign=top class=datadivider><IMG
													height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD vAlign=top noWrap width=150><report:sort
														field="<%=SecurityRoleConversionReportData.SORT_FIELD_CONTRACT_NUMBER%>"
														direction="asc">
														<B>Contract number/</B>
													</report:sort><br>
												<B>contract name </B></TD>
												<TD class=datadivider vAlign=top><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD width=50 vAlign=top noWrap><report:sort
														field="<%=SecurityRoleConversionReportData.SORT_FIELD_CONVERSION_INDICATOR%>"
														direction="asc">
														<b>Conversion indicator</b>
													</report:sort></TD>
												<TD class=datadivider vAlign=top><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD vAlign=top align=left colSpan=2><report:sort
														field="<%=SecurityRoleConversionReportData.SORT_FIELD_DATE_OF_CHANGE%>"
														direction="asc">
														<B>Date of change </B>
													</report:sort></TD>
												<TD width="1" vAlign=top class=datadivider><IMG
													height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD width=150 vAlign=top noWrap><report:sort
														field="<%=SecurityRoleConversionReportData.SORT_FIELD_TPA_FIRM_ID%>"
														direction="asc">
														<b>TPA firm ID/</b>
													</report:sort><Br>
												<b>TPA firm name</b></TD>
												<TD width="1" vAlign=top class=datadivider><IMG
													height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD width="150" align=left vAlign=top><report:sort
														field="<%=SecurityRoleConversionReportData.SORT_FIELD_CLIENT_CONTACT_NUMBER%>"
														direction="asc">
														<b>Number of client contacts</b>
													</report:sort></TD>
												<TD class=databorder width=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
											</TR>
											<!-- report body -->
											<%
												if (theReport.getDetails() == null || theReport.getDetails().size() == 0) { // we have no results
											%>
											<tr class="datacell1">
												<td class="databorder"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												<td valign="top" colspan="12">
													<%
														if (request.getParameter("task") == null) {
													%> <content:getAttribute
														id="noDefaultSearchResults" attribute="text" /> <%
 														} else {
 													%>
													<content:getAttribute id="noSearchResults" attribute="text" />
												</td>
												<%
													}
												%>
												</td>
												<td class="databorder"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
											</tr>
											<%
												}
											%>

											<%
												if (errors == false) {
											%>
											<c:if test="${not empty theReport.details}">
												<c:forEach items="${theReport.details}" var="theItem"
													varStatus="theIndex">
													<c:set var="tempIndex" value="${theIndex.index}"/>
													<%
														Integer theIndex = (Integer) pageContext.getAttribute("tempIndex");
																		if (theIndex.intValue() % 2 == 0) {
													%>
													<tr class="datacell3">
														<%
															} else {
														%>
													
													<tr class="datacell1">
														<%
															}
														%>
														<TD width="1" class=databorder><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></TD>
														<TD vAlign=center width=150>${theItem.teamCode}<Br>
															${theItem.teamName}
														</TD>
														<TD width="1" vAlign=center class=datadivider><IMG
															height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
														<TD vAlign=center width=150>${theItem.contractNumber}<br>
															${theItem.contractName}
														</TD>
														<TD class=datadivider vAlign=center><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></TD>
														<TD width=50 vAlign=center>${theItem.conversionIndicator}</TD>
														<TD class=datadivider vAlign=center><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></TD>
														<TD vAlign=center align=left colSpan=2><fmt:formatDate
																value="${theItem.changeDate}" pattern="MM/dd/yyyy hh:mm" /></TD>
														<TD width="1" vAlign=center class=datadivider><IMG
															height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
														<TD width=150 vAlign=center>${theItem.tpaFirmId}<Br>
															${theItem.tpaFirmName}
														</TD>
														<TD width="1" vAlign=center class=datadivider><IMG
															height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
														<TD width="150" align=left vAlign=center>
															<div align="right">${theItem.clientContactNumber}</div>
														</TD>
														<TD class=databorder vAlign=top width=1><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></TD>
													</tr>
												</c:forEach>
											</c:if>
											<%
												}
											%>

											<TR>
												<TD class=databorder width=1 height=1></TD>
												<TD class=databorder colSpan=13 height=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=5></TD>
											</TR>
											<TR>
												<TD colSpan=6 align=right>&nbsp;</TD>
												<TD colSpan=7 align=right>&nbsp;</TD>
												<TD width="1" align=right><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
											</TR>
											<TR>
												<TD width="1" align=right><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD colSpan=5 align=right>&nbsp;</TD>
												<TD colSpan=7 align=right>
													<div align="left"></div>
												</TD>
												<TD width="1" align=right><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
											</TR>
										</TBODY>
									</TABLE>

									<TABLE cellSpacing=0 cellPadding=0 width=727 border=0>
										<TBODY>
											<TR>
												<TD width=393><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<%
													if (errors == false) {
												%>
												<TD width=217><b><report:recordCounter
															report="theReport" label="Records" /></b></TD>
												<TD width=72>
													<div align="right">
														<report:pageCounter report="theReport" arrowColor="black" />
														<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</div>
												</TD>
												<% } else { %>
												<TD width=217>&nbsp;</TD>
												<TD width=72>&nbsp;</TD>
												<% } %>
											</TR>
										</TBODY>
									</TABLE>
									<div align="right"></div></TD>
							</TR>
						</TBODY>
					</TABLE> <BR> <c:if test="${empty param.printFriendly }">
						<P>
						<div align="left">
							<input name="backButton" type="button" class="button134"
								onClick="location.href='/do/tools/controlReports/'" value="back">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
								name="selectButton" type="button" class="button134"
								onClick="location.href='/do/home/searchContractDetail/'"
								value="select contract">
						</div>
						</P>
					</c:if> <c:if test="${not empty param.printFriendly}">
						<br>
						<content:contentBean
							contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
							type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
							id="globalDisclosure" />
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="100%"><content:getAttribute
										beanName="globalDisclosure" attribute="text" /></td>
							</tr>
						</table>
					</c:if>
				</TD>
			</TR>

		</TBODY>
	</TABLE>
</ps:form>
