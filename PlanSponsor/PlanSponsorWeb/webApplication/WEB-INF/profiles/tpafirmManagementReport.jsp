<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ page
	import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page
	import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page
	import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page
	import="com.manulife.pension.service.report.valueobject.ReportData"%>
<%
	boolean errors = false;
%>

<%
	ReportData theReport = (ReportData) request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport", theReport, PageContext.PAGE_SCOPE);
%>

<%
	String ASC_DIRECTION = ReportSort.ASC_DIRECTION;
	pageContext.setAttribute("ASC_DIRECTION", ASC_DIRECTION, PageContext.PAGE_SCOPE);
%>




<jsp:useBean id="tpafirmManagementReportForm" scope="session"
	type="com.manulife.pension.ps.web.profiles.TpafirmManagementReportForm" />
<content:contentBean contentId="56666"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noSearchResults" />
<STYLE type="text/css">
.paddingLeft {
	padding-left: 7px;
}
</STYLE>
<%
	String maxlength = Constants.STR_CONTRACT_NUMBER_MAX_LENGTH;
	pageContext.setAttribute("maxlength", maxlength, PageContext.PAGE_SCOPE);
%>

<c:if test="${empty param.param.printFriendly}">
	<script type="text/javascript">
function doReset() {
	document.tpafirmManagementReportForm.elements["changedBy"].value = "";
	document.tpafirmManagementReportForm.elements["contractNumber"].value = "";
	document.tpafirmManagementReportForm.elements["teamCode"].value = "All";
	document.tpafirmManagementReportForm.elements["userType"].value = "Internal";
	
	document.tpafirmManagementReportForm.task.value = 'default';
	document.tpafirmManagementReportForm.submit();
}

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


	function initPage() {
		setupCalendar();
	}
	
	if (window.addEventListener) {
		window.addEventListener('load', initPage, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', initPage);
	}
</script>
</c:if>

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
								width=1> <ps:form method="POST"
									modelAttribute="tpafirmManagementReportForm"
									name="tpafirmManagementReportForm"
									action="/do/profiles/tpafirmManagementReport/">
									<input type="hidden" name="task" value="filter" />
									<input type="hidden" name="sortField" />
									<input type="hidden" name="sortDirection"
										value="${ASC_DIRECTION}" />
									<table width="730" border="0" cellpadding="0" cellspacing="0">
										<tbody>
											<tr>
												<td colspan="5"></td>
											</tr>
											<tr>
												<td colspan="5" bgcolor="#CCCCCC">&nbsp;&nbsp;<b>Search
												</b><img src="/assets/unmanaged/images/s.gif" width=1 height=25>
												</td>
											</tr>
											<tr>
												<td valign="top" class="filterSearch"><strong>Changed
														by </strong>(last name)<br> <c:if
														test="${not empty param.printFriendly }">
														<form:input path="changedBy" disabled="true" />
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:input path="changedBy" />
													</c:if></td>
												<td valign="top" class="filterSearch paddingLeft" nowrap>
													<strong>Contract number</strong><br> <c:if
														test="${not empty param.printFriendly }">
														<form:input path="contractNumber" disabled="true"
															maxlength="${maxlength}" size="7" />
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:input path="contractNumber" maxlength="${maxlength}"
															size="7" />
													</c:if>

												</td>
												<td valign="top" class="filterSearch paddingLeft" nowrap>
													<strong>Date of change</strong><br> from <c:if
														test="${not empty param.printFriendly }">
														<form:input path="fromDate" disabled="true" maxlength="10"
															onclick="javascript:popupCalendarFrom()" readonly="true"
															size="10" cssClass="inputField" />
														<img src="/assets/unmanaged/images/cal.gif" width="16"
															height="16">
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:input path="fromDate" maxlength="10" size="10"
															cssClass="inputField" />
														<img src="/assets/unmanaged/images/cal.gif" width="16"
															height="16" onclick="javascript:popupCalendarFrom()">
													</c:if> <span class="paddingLeft">to</span> <c:if
														test="${not empty param.printFriendly }">
														<form:input path="toDate" disabled="true" maxlength="10"
															onclick="javascript:popupCalendarTo()" readonly="true"
															size="10" cssClass="inputField" />
														<img src="/assets/unmanaged/images/cal.gif" width="16"
															height="16">
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:input path="toDate" maxlength="10" size="10"
															cssClass="inputField" />
														<img src="/assets/unmanaged/images/cal.gif" width="16"
															height="16" onclick="javascript:popupCalendarTo()">
													</c:if> <br>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
												</td>
												<td valign="top" class="filterSearch paddingLeft"><strong>Team
														code</strong><br> <c:if test="${not empty param.printFriendly }">
														<form:select path="teamCode" disabled="true">
															<form:options items="${tpafirmManagementReportForm.teamCodeList}" itemValue="value" itemLabel="label"/>
														</form:select>

													</c:if> <c:if test="${empty param.printFriendly }">
														<form:select path="teamCode">
															<form:options items="${tpafirmManagementReportForm.teamCodeList}" itemValue="value" itemLabel="label"/>
														</form:select>
													</c:if></td>
												<td valign="top" class="filterSearch paddingLeft"><strong>Changed
														by user type </strong> <c:if test="${not empty param.printFriendly }">
														<form:select path="userType" size="1" disabled="true">
															<form:options items="${tpafirmManagementReportForm.userTypeList}" />
														</form:select>
													</c:if> <c:if test="${empty param.printFriendly }">
														<form:select path="userType" size="1">
															<form:options items="${tpafirmManagementReportForm.userTypeList}" />
														</form:select>
													</c:if></td>
											</tr>
											<tr>
												<td colspan="5" valign="bottom" class="filterSearch"><c:if
														test="${not empty param.printFriendly }">
														<div align="right">
															<input type="button" value="reset" name="reset"
																disabled="disabled" onclick="doReset();">
															&nbsp;&nbsp;&nbsp; <input type="submit" disabled="true"
																value="search" />
														</div>
													</c:if> <c:if test="${empty param.printFriendly }">
														<div align="right">
															<a href="javascript:openChangeWindow()"></a> <input
																type="button" value="reset" name="reset"
																onclick="doReset();"> &nbsp;&nbsp;&nbsp; <input
																type="submit" value="search" />
														</div>
													</c:if></td>
											</tr>
										</tbody>
									</table>
									<table cellSpacing=0 cellPadding=0 width=525 border=0>
										<tbody>
											<tr></tr>
										</tbody>
									</table>

									<table cellSpacing=0 cellPadding=0 width=730 border=0>
										<tbody>
											<tr>
												<td width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=name_column width=150><img height=1
													src="/assets/unmanaged/images/s.gif" width=125></td>
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
												<td class=password_status_column><img height=1
													src="/assets/unmanaged/images/s.gif" width=50></td>
												<td class=password_status_column width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=password_status_column>&nbsp;</td>
												<td class=password_status_column width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td class=password_status_column>&nbsp;</td>
												<td width=2><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
											<tr class=tablehead>
												<TD class=tableheadTD1 colSpan="8">&nbsp;</TD>
												<%
													if (errors == false) {
												%>
												<TD colSpan="8" class=tableheadTD><strong><report:recordCounter
															report="theReport" label="Records" /></strong></TD>
												<TD class=tableheadTD>
													<div align="right">
														<strong><report:pageCounter report="theReport" formName="tpafirmManagementReportForm"/></strong>
													</div>
												</TD>
												<%
													} else {
												%>
												<TD colSpan="9" class=tableheadTD>&nbsp;</TD>
												<%
													}
												%>
												<td class=databorder width=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
											<tr class=tablesubhead>
												<td class=databorder><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=150><report:sort
														field="sortByTeamCode"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="tpafirmManagementReportForm">
														<B>Team code/</B>
													</report:sort> <Br>
												</a><B>JH rep </B></td>
												<td width="1" vAlign=top class=datadivider><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top noWrap width=150><report:sort
														field="sortByContractNumber"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="tpafirmManagementReportForm">
														<B>Contract number/</B>
													</report:sort> <br>
												</a><B>contract name </B></td>
												<td class=datadivider vAlign=top><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top noWrap width=150><report:sort
														field="sortByFirmId"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="tpafirmManagementReportForm">
														<B>TPA firm ID/</B>
													</report:sort> <br>
												</a><B>TPA firm name </B></td>
												<td class=datadivider vAlign=top><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td width=115 vAlign=top noWrap><report:sort
														field="sortByChangedBy"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="tpafirmManagementReportForm">
														<B>Changed by</B>
													</report:sort></td>
												<td class=datadivider vAlign=top><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top width=115><report:sort
														field="sortByRole"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="tpafirmManagementReportForm">
														<B>Changed by role</B>
													</report:sort></td>
												<td class=datadivider vAlign=top><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top align=left colSpan=2><report:sort
														field="sortByDate"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="tpafirmManagementReportForm">
														<B>Date of change</B>
													</report:sort></td>
												<td width="1" vAlign=top class=datadivider><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top align=left><report:sort
														field="sortByItem"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="tpafirmManagementReportForm">
														<B>Item</B>
													</report:sort></td>
												<td width="1" vAlign=top class=datadivider><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
												<td width="50" align=left vAlign=top><b>Value</b></td>
												<td class=databorder width=2><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
											<!-- report body -->
											<%
												if (theReport.getDetails() == null || theReport.getDetails().size() == 0) { // we have no results
											%>
											<tr class="datacell1">
												<td class="databorder"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												<td valign="top" colspan="16"><content:getAttribute
														id="noSearchResults" attribute="text" /></td>

												</td>
												<td class="databorder"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
											</tr>
											<%
												}
											%>
											<%
												if (errors == false) {
														int rowIndex = 1;
														String rowClass = "";
											%>
											<c:forEach var="row" items="${theReport.details}">
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
													<td vAlign=center width=150><c:out
															value="${row.teamCode}" /> <br> <c:out
															value="${row.jhRepName}" /></td>
													<td width="1" vAlign=center class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													<td vAlign=center width=150><c:out
															value="${row.contractNumber}" /> <br> <c:out
															value="${row.contractName}" /></td>
													<td class=datadivider vAlign=center><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
													<td width=115 vAlign=center><c:out
															value="${row.tpafirmId}" /> <br> <c:out
															value="${row.tpafirmName}" /></td>
													<td class=datadivider vAlign=center><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
													<td width=115 vAlign=center><c:out
															value="${row.changedByLastName}" />,&nbsp;<c:out
															value="${row.changedByFirstName}" /></td>
													<td class=datadivider vAlign=center><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
													<td colSpan=2 align=left vAlign=center><c:out
															value="${row.userRole.displayName}" /></td>
													<td width="1" vAlign=center class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													<td vAlign=center align=left><fmt:formatDate
															value="${row.createdTs}" type="DATE" pattern="MM/dd/yyyy" /></br>
														<fmt:formatDate value="${row.createdTs}" type="DATE"
															pattern="hh:mm" /></br></td>
													<td width="1" vAlign=center class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													<td vAlign=center align=left nowrap><c:out
															value="${row.itemName}" /></td>
													<td width="1" vAlign=center class=datadivider><img
														height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													<td width="50" align=left vAlign=center>O: <c:if
															test="${row.oldValue == 'N'}">No</c:if>
														<c:if test="${row.oldValue == 'Y'}">Yes</c:if> <Br>
														N: <c:if test="${row.newValue == 'N'}">No</c:if>
														<c:if test="${row.newValue == 'Y'}">Yes</c:if>
													</td>
													<td class=databorder vAlign=top width=2><img height=1
														src="/assets/unmanaged/images/s.gif" width=1></td>
												</tr>
											</c:forEach>
											<%
												}
											%>

											<tr>
												<td class=databorder width=1 height=1></td>
												<td class=databorder colSpan=17 height=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=5></td>
											</tr>
											<tr>
												<td colSpan=17 align=right>&nbsp;</td>
											</tr>
											<tr>
												<TD colSpan="8">&nbsp;</TD>
												<%
													if (errors == false) {
												%>
												<TD colSpan="8"><strong><report:recordCounter
															report="theReport" label="Records" /></strong></TD>
												<TD>
													<div align="right">
														<strong><report:pageCounter report="theReport"
																arrowColor="black" formName="tpafirmManagementReportForm"/></strong>
													</div>
												</TD>
												<%
													} else {
												%>
												<TD colSpan="9">&nbsp;</TD>
												<%
													}
												%>
												<td height=1><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
										</tbody>
									</table></td>
						</tr>
					</tbody>
				</table> </ps:form> <BR> <c:if test="${empty param.printFriendly }">
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
					<BR> If you have any questions regarding the information shown
					here please refer to the 'Getting help' section or contact your
					John Hancock USA Client Account Representative. <BR>
				</P>
				<P class=disclaimer>&nbsp;</P></td>
		</tr>
	</tbody>
</table>


<script type="text/javascript">
</script>
