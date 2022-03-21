<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page
	import="com.manulife.pension.ps.service.report.profiles.valueobject.AddressChangesExternalReportData"%>
<%@ page
	import="com.manulife.pension.service.report.valueobject.ReportSort"%>
	<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>





<%
AddressChangesExternalReportData theReport = (AddressChangesExternalReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);


%>

<% String ASC_DIRECTION=ReportSort.ASC_DIRECTION;
pageContext.setAttribute("ASC_DIRECTION",ASC_DIRECTION,PageContext.PAGE_SCOPE);
%>


<jsp:useBean id="addressChangesReportForm" scope="session" type="com.manulife.pension.ps.web.profiles.AddressChangesReportForm" />





<content:contentBean
	contentId="<%=ContentConstants.NO_DEFAULT_CONVERTED_CONTRACT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="noDefaultSearchResults" />

<content:contentBean contentId="<%=ContentConstants.NO_ADDRESS_CHANGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noSearchResults" />

<%
	boolean errors = false;
%>

<c:if test="${empty param.printFriendly}">
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
	document.addressChangesReportForm.task.value = 'default';
	document.addressChangesReportForm.submit();
}

function doDownload() {
	document.addressChangesReportForm.task.value = 'download';
	document.addressChangesReportForm.submit();
	
}
function doSearch() {
	document.addressChangesReportForm.task.value = 'filter';
	document.addressChangesReportForm.submit();
}

</script>
</c:if>
<ps:form modelAttribute="addressChangesReportForm" name="addressChangesReportForm" action="/do/profiles/addressChangesInternal/">
	
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="sortField"/>
<input type="hidden" name="sortDirection" value="${ASC_DIRECTION}"/>

	<TABLE cellSpacing=0 cellPadding=0 width=760 border=0>
		<TBODY>
			<c:if test="${not empty sessionScope.psErrors}">
<c:set var="errorsExist" value="${true}" scope="page" />

				<tr>
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
												<TD width="190" valign="top" class="filterSearch"><strong>Changed
														by </strong>(last name) <br> 
														<c:if test="${not empty param.printFriendly}">
<form:input path="changedBy" disabled="true"/>
													</c:if> <c:if test="${empty param.printFriendly }" >
<form:input path="changedBy"/>
													</c:if></TD>
												<TD width="156" valign="top" class="filterSearch"><strong>Role
												</strong> <br> <c:if test="${not empty param.printFriendly }" >
														<form:select path="userType" size="1" disabled="true">
<form:options items="${addressChangesReportForm.userTypeList}" itemValue="value" itemLabel="label"/>
</form:select>
													</c:if> <c:if test="${empty param.printFriendly }" >
														<form:select path="userType" size="1">
<form:options items="${addressChangesReportForm.userTypeList}" itemValue="value" itemLabel="label"/>
</form:select>
													</c:if></TD>

												<TD colspan="2" valign="top" class="filterSearch"><strong>Date
														of change </strong><br> from 
														<c:if test="${not empty param.printFriendly}">
<form:input path="fromDate" disabled="true" maxlength="10" size="10"/>

													</c:if> <c:if test="${empty param.printFriendly }" >
<form:input path="fromDate" maxlength="10" size="10"/>
													</c:if> <strong><img
														src="/assets/unmanaged/images/cal.gif" width="16"
														height="16" onclick="javascript:popupCalendarFrom()"></strong>
													to <c:if test="${not empty param.printFriendly }" >
<form:input path="toDate" disabled="true" maxlength="10" size="10"/>

													</c:if> <c:if test="${empty param.printFriendly }" >
<form:input path="toDate" maxlength="10" size="10"/>
													</c:if> <strong><img
														src="/assets/unmanaged/images/cal.gif" width="16"
														height="16" onclick="javascript:popupCalendarTo()"></strong><br>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)<br>
												</TD>
											</TR>
											<TR>
												<TD valign="top" class="filterSearch"><strong>Changed
														by Team code</strong> <br> 
														<c:if test="${not empty param.printFriendly}">
														<form:select path="teamCode" size="1" disabled="true">
<form:options items="${addressChangesReportForm.teamCodeList}" itemValue="value" itemLabel="label"/>
</form:select>
													</c:if> <c:if test="${empty param.printFriendly }" >
														<form:select path="teamCode" size="1">
<form:options items="${addressChangesReportForm.teamCodeList}" itemValue="value" itemLabel="label"/>
</form:select>
													</c:if> <br></TD>
												<TD colspan="2" valign="top" class="filterSearch"><strong>Contract
														number </strong><br> <c:if test="${not empty param.printFriendly }" >
<form:input path="contractNumber" disabled="true" maxlength="7" size="7"/>

													</c:if> <c:if test="${empty param.printFriendly }" >
<form:input path="contractNumber" maxlength="7" size="7"/>

													</c:if></TD>
												<TD colspan="1" valign="bottom" class="filterSearch"
													align="right">
													<c:if test="${empty param.printFriendly}">
														<input type="button" name="resetBtn" value="reset"
															onclick="doReset();" />&nbsp;&nbsp;&nbsp;
	            							<input type="button" name="searchBtn" value="search"
															onclick="doSearch();" />
													</c:if></TD>
											</TR>

											</TR>
										</TBODY>
									</TABLE> <br />

									<TABLE cellSpacing=0 cellPadding=0 width=760 border=0>
										<TBODY>

											<tr class=tablehead>


												<%
													if (errors == false) {
												%>
												<td colSpan="10" class=tableheadTD align="right">

													<div>
														<b><report:recordCounter report="theReport"
																label="Records" /></b>
													</div>
												</td>
												<td colSpan="7" class=tableheadTD>
													<div align="right">
														<b><report:pageCounter report="theReport"
																arrowColor="white" formName="addressChangesReportForm" /></b>
													</div>
												</td>
												<%
													} else {
												%>
												<td class=tableheadTD1 colSpan="17">&nbsp;</td>
												<%
													}
												%>
												<td width="1" class=databorder><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>

											<tr class=tablesubhead>
												<td class=databorder><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
												<td vAlign=top noWrap width=75><a> <report:sort
															field="sortByChangeByTeamCode" direction="asc" formName="addressChangesReportForm">
															<B>Changed by Team code</B>
														</report:sort>
												</a></td>
												<td width="1" vAlign=top class=datadivider><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>

												<td vAlign=top nowrap="nowrap" width=75><a> <report:sort
															field="sortByUserName" direction="asc" formName="addressChangesReportForm">
															<B>Changed by <br>Name/Role
															</B>
														</report:sort>
												</a></td>
												<td vAlign=top class=datadivider><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>

												<td vAlign=top noWrap width=150><report:sort
														field="sortByContractNumber"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="addressChangesReportForm">
														<B>Contract number/</B>
													</report:sort> <br>
												</a><B>contract name </B></td>
												<td vAlign=top class=datadivider><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>

												<td vAlign=top noWrap width=50><report:sort
														field="sortByTeamCode"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="addressChangesReportForm">
														<B>Team code/</B>
													</report:sort> <Br>
												</a><B>JH rep </B></td>
												<td class=datadivider vAlign=top><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>

												<td vAlign=top align=left colSpan=2><report:sort
														field="sortByDate"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="addressChangesReportForm">
														<B>Date of change</B>
													</report:sort></td>
												<td class=datadivider vAlign=top width="1">
												<td valign="top" align="left"><report:sort
														field="sortByItem"
														direction="<%=ReportSort.ASC_DIRECTION%>" formName="addressChangesReportForm">
														<b>Address</b>
													</report:sort></td>
												<td class="datadivider" valign="top" width="1"><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>

												<td width="450" align=left vAlign=top><b>Old</b></td>
												<td width="1" vAlign=top class=datadivider><img
													height=1 src="/assets/unmanaged/images/s.gif" width=1></td>

												<td width="450" align=left vAlign=top><b>New</b></td>
												<td width="1" class=databorder><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>


											<%-- report body --%>
											<%
												int rowIndex = 1;
													String rowClass = "";
											%>
<c:if test="${empty theReport.details}">

												<tr class="datacell1">
													<td class="databorder"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="top" colspan="16"><content:getAttribute
															id="noSearchResults" attribute="text" /></td>
													<td class="databorder" colspan="1"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
</c:if>
											<c:if test="${not empty theReport.details}">

<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >


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
<td width=75 vAlign=center>${theItem.changedByTeamCode}</td>

														<td class=datadivider vAlign=center><img height=1
															src="/assets/unmanaged/images/s.gif" width=1></td>

<td width=150 vAlign=top nowrap="nowrap">${theItem.changedByLastName}, ${theItem.changedByFirstName} <br>


${theItem.changedByRoleCode}
														</td>
														<td class=datadivider vAlign=top><img height=1
															src="/assets/unmanaged/images/s.gif" width=1></td>

<td vAlign=top width=150><c:if test="${theItem.contractNumber !=0}">

${theItem.contractNumber}
</c:if> <br> ${theItem.contractName}</td>

														<td class=datadivider vAlign=top><img height=1
															src="/assets/unmanaged/images/s.gif" width=1></td>

<td vAlign=top width=150>${theItem.teamCode}<br> ${theItem.jhRepName}</td>


														<td class=datadivider vAlign=center><img height=1
															src="/assets/unmanaged/images/s.gif" width=1></td>

														<td colSpan=2 align=left vAlign=top><fmt:formatDate
																value="${theItem.createdTs}" pattern="MM/dd/yyyy hh:mm" />
														</td>
														<td class=datadivider vAlign=top width="1"><img
															height=1 src="/assets/unmanaged/images/s.gif" width=1></td>

<td align=left vAlign=top>${theItem.itemName}</td>

														<td class="datadivider" valign="center" width="1">
														<td width="125" align=left vAlign=center><c:if
																test="${not empty theItem.oldValue}">
																<c:forTokens var="token" items="${theItem.oldValue}"
																	delims="~">
																	<c:out value="${token}" />
																	<br>
																</c:forTokens>
															</c:if></td>
														<td width="1" vAlign=center class=datadivider><img
															height=1 src="/assets/unmanaged/images/s.gif" width=1></td>

														<td width="200" align=left vAlign="center"><c:if
																test="${not empty theItem.newValue}">
																<c:forTokens var="token" items="${theItem.newValue}"
																	delims="~">
																	<c:out value="${token}" />
																	<br>
																</c:forTokens>
															</c:if></td>
														<td class=databorder vAlign=top width="1"><img
															height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
													</tr>
</c:forEach>
											</c:if>
											<%-- report body--%>
											<tr>
												<td class=databorder colspan="18"><img height=1
													src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
										</TBODY>
									</TABLE>
									<TABLE cellSpacing=0 cellPadding=0 width=760 border=0>
										<TBODY>
											<TR>
												<TD width=350><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<%
													if (errors == false) {
												%>
												<TD width=310><b><report:recordCounter
															report="theReport" label="Records" /></b></TD>
												<TD width=20>
													<div>
														<b><report:pageCounter report="theReport"
																arrowColor="black" formName="addressChangesReportForm"/><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></b>
													</div>
												</TD>
												<%
													} else {
												%>
												<TD width=217>&nbsp;</TD>
												<TD width=72>&nbsp;</TD>
												<%
													}
												%>
											</TR>
										</TBODY>
									</TABLE></TD>
							</TR>
						</TBODY>
					</TABLE> <BR> <c:if test="${empty param.printFriendly }" >
						<P>
						<div align="left">
							<input name="backButton" type="button" class="button134"
								onClick="location.href='/do/tools/controlReports/'" value="back">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input name="searchContractButton"
								type="button" class="button134"
								onClick="document.location='/do/home/searchContractDetail/'"
								value="select contract">

						</div>
						</P>
					</c:if> <c:if test="${not empty param.param.printFriendly}">
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
		</TBODY>
	</TABLE>
</ps:form>
