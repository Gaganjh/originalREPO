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
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort" %>
<%@page	import="com.manulife.pension.service.contract.pilot.report.valueobject.PilotContractReportData"%>
<%@page	import="com.manulife.pension.service.contract.pilot.report.valueobject.PilotContractReportDetails"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.pilot.PilotNameLookup" %>
<%
PilotContractReportData theReport = (PilotContractReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

<% String ASC_DIRECTION=ReportSort.ASC_DIRECTION;
pageContext.setAttribute("ASC_DIRECTION",ASC_DIRECTION,PageContext.PAGE_SCOPE);
%>


<jsp:useBean id="pilotContractReportForm" scope="session" type="com.manulife.pension.ps.web.pilot.PilotContractReportForm" />

	
<c:set var="pilot" value="${requestScope.pilotNamesLookup}" />


<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


<content:contentBean
	contentId="56637"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noSearchResults" />

<%
boolean errors = false;
%>

<c:if test="${empty param.printFriendly}">
	<script type="text/javascript">
function doReset() {
	document.pilotContractReportForm.task.value = 'default';
	document.pilotContractReportForm.submit();
}

function doDownload() {
	document.pilotContractReportForm.task.value = 'download';
	document.pilotContractReportForm.submit();
	
}
function doSearch() {
	document.pilotContractReportForm.task.value = 'filter';
	document.pilotContractReportForm.submit();
}

</script>
</c:if>
<ps:form action="/do/pilot/pilotContractReport/" modelAttribute="pilotContractReportForm" name="pilotContractReportForm">
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="sortField"/>
<input type="hidden" name="sortDirection" value="${ASC_DIRECTION}"/>
	<TABLE cellSpacing=0 cellPadding=0 width=760 border=0>
		<TBODY>
			<c:if test="${not empty sessionScope.psErrors}">
				<c:set var="errorsExist" value="${true}" scope="page" />
				<tr>
					<td/>
					<td colspan="2">
						<div id="errordivcs"><content:errors scope="session"/></div>
					</td>
				</tr>
				<%errors=true;%>
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
										<TD colspan="3"></TD>
									</TR>
									<TR>
										<TD colspan="3" valign="middle" bgcolor="#CCCCCC">&nbsp;&nbsp;<b>Search
										</b><IMG src="/assets/unmanaged/images/s.gif" width=1 height=25></TD>
									</TR>

									<TR>
										<TD width="178" valign="top" class="filterSearch"><strong>Contract
										number </strong><br>
										<c:if test="${not empty param.printFriendly }" >
<form:input path="contractNumber" disabled="true"/>
										</c:if> <c:if test="${empty param.printFriendly }" >
<form:input path="contractNumber"/>
										</c:if></TD>
										<TD width="89" valign="top" class="filterSearch"><strong>Team</strong>
										<strong>code</strong> <br>
										<c:if test="${not empty param.printFriendly }" >
											<form:select path="teamCode" disabled="true">
<form:options items="${pilotContractReportForm.teamCodeList}" itemValue="value" itemLabel="label"/>
</form:select>
										</c:if> <c:if test="${empty param.printFriendly }" >
											<form:select path="teamCode">
<form:options items="${pilotContractReportForm.teamCodeList}" itemValue="value" itemLabel="label"/>
</form:select>
										</c:if> <br>
										</TD>
										<TD width="184" valign="bottom" class="filterSearch">
										<div align="center"><br>
										<c:if test="${empty param.printFriendly }" >
											<input type="button" name="resetBtn" value="reset"
												onclick="doReset();" />&nbsp;&nbsp;&nbsp;
	            							<input type="button" name="searchBtn" value="search"
												onclick="doSearch();" />
										</c:if></div>
										</TD>
									</TR>
								</TBODY>
							</TABLE>
							<br>

							<TABLE cellSpacing=0 cellPadding=0 width=730 border=0>
								<TBODY>
									<TR>
										<TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD width=150><IMG height=1	src="/assets/unmanaged/images/s.gif" width=130></TD>
										<TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD width=175><IMG height=1	src="/assets/unmanaged/images/s.gif" width=160></TD>
<c:forEach items="${pilot.pilotNames}" var="pilotName" varStatus="theIndex">
										<TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
</c:forEach>
										<TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD width=150><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD width=227 colspan="5">&nbsp;</TD>
										<TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
									</TR>
									<!-- table header -->
									
									<jsp:useBean id="now" class="java.util.Date" />
									<TR class=tablehead>
										<TD class=tableheadTD1 colSpan="4"><strong>Report as of <fmt:formatDate value="${now}" pattern="MMM dd, yyyy" /> </strong></TD>
										<%
										if (errors == false) {
										%>
										<TD colSpan="${4 + 2 * pilot.pilotNamesSize}" class=tableheadTD><strong><report:recordCounter	report="theReport" label="Records" /></strong></TD>
										<TD class=tableheadTD colspan="5">
										<div align="right"><strong><report:pageCounter
											report="theReport" formName="pilotContractReportForm"/></strong></div>
										</TD>
										<%
										} else {
										%>
										<TD colSpan="${13 + 2 * pilo.pilotNamesSize}" class=tableheadTD>&nbsp;</TD>
										</TD>
										<%
										}
										%>

										<TD class=databorder width=1><IMG height=1
											src="/assets/unmanaged/images/s.gif" width=1></TD>
									</TR>
									
									<TR class=tablesubhead>
										<TD width="1" class=databorder><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD vAlign=top width=150>
										<report:sort field="<%=PilotContractReportData.SORT_FIELD_TEAM_CODE%>"	direction="asc" formName="pilotContractReportForm">
											<B>Team code/</B>
										</report:sort><Br>
										<B>JH rep </B></TD>
										<TD width="1" vAlign=top class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD vAlign=top noWrap width=250><report:sort
											field="<%=PilotContractReportData.SORT_FIELD_CONTRACT_NUMBER%>"
											direction="asc" formName="pilotContractReportForm">
											<B>Contract number/</B>
										</report:sort><br>
										<B>contract name </B></TD>
<c:forEach items="${pilot.pilotNames}" var="pilotName" varStatus="theIndex">
										<TD class=datadivider vAlign=top><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD vAlign=top noWrap><report:sort
											field="${pilotName}"
											direction="asc" formName="pilotContractReportForm">
											<b>${pilotName}</b>
										</report:sort></TD>
</c:forEach>
										<TD width="1" vAlign=top class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD width=250 vAlign=top noWrap><report:sort
											field="<%=PilotContractReportData.SORT_FIELD_TPA_FIRM_ID%>"
											direction="asc" formName="pilotContractReportForm">
											<b>TPA firm ID/</b>
										</report:sort><Br>
										<b>TPA firm name</b></TD>
										<TD width="1" vAlign=top class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD width="177" align=left vAlign=top colspan="5"><report:sort
											field="<%=PilotContractReportData.SORT_FIELD_CLIENT_CONTACT_NUMBER%>"
											direction="asc" formName="pilotContractReportForm">
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
										<td valign="top" colspan="${11 + 2 * pilot.pilotNamesSize}"><content:getAttribute
											id="noSearchResults" attribute="text" /></td>
										<td class="databorder"><img
											src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									</tr>
									<%
									}
									%>

									<% if (errors==false) { %>
									<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >



											<%
											Integer theIndex = (Integer) pageContext.getAttribute("theIndex");	
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
<TD vAlign=center width=150>${theItem.teamCode} <Br>

${theItem.teamName}</TD>
												<TD width="1" vAlign=center class=datadivider><IMG
													height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD vAlign=center width=250>${theItem.contractNumber}<br>

${theItem.contractName}

<c:set var="pilotItem" value="${theItem.pilotEnabledMap}"/>
<c:forEach items="${pilot.pilotNames}" var="pilotName">
<c:set var="pilotValue" value="${pilotItem.pilotName}" /> 
												<TD class=datadivider vAlign=center><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD vAlign=center><% Boolean pilotValue = (Boolean)pageContext.getAttribute("pilotValue");
												if (pilotValue.booleanValue()){%>Yes<%} else {%> No <%}%></TD>
</c:forEach>
												<TD width="1" vAlign=center class=datadivider><IMG	height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD width=250 vAlign=center>${theItem.tpaFirmId}<Br>

${theItem.tpaFirmName}</TD>
												<TD width="1" vAlign=center class=datadivider><IMG	height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD width="177" align=left vAlign=center colspan="5">
<div align="right">${theItem.clientContactNumber}</div>

												</TD>
												<TD class=databorder vAlign=top width=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
											</tr>
</c:forEach>
									</c:if>
									<%}%>
									

									<TR>
										<TD class=databorder width=1 height=1></TD>
										<TD class=databorder colSpan=16 height=1><IMG height=1	src="/assets/unmanaged/images/s.gif" width=5></TD>
									</TR>
									<TR>
										<TD colSpan=6 align=right>&nbsp;</TD>
										<TD colSpan=10 align=right>&nbsp;</TD>
										<TD width="1" align=right><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
									</TR>
									<TR>
										<TD width="1" align=right><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
										<TD colSpan=10 align=right>&nbsp;</TD>
										<TD colSpan=6 align=right>
										<div align="left"></div>
										</TD>
										<TD width="1" align=right><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
									</TR>
								</TBODY>
							</TABLE>

							<TABLE cellSpacing=0 cellPadding=0 width=727 border=0>
								<TBODY>
									<TR>
										<TD width=393><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
										<%
										if (errors == false) {
										%>
										<TD width=217><b><report:recordCounter
											report="theReport" label="Records" /></b></TD>
										<TD >
										<div align="right"><report:pageCounter
											report="theReport" arrowColor="black" formName="pilotContractReportForm"/><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></div>
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
							</TABLE>
							<div align="right"></div>
							</TD>
						</TR>
					</TBODY>
				</TABLE>
				<BR>
				<c:if test="${empty param.printFriendly }" >
					<P>
					<div align="left"><input name="backButton" type="button"
						class="button134" onClick="location.href='/do/tools/controlReports/'" value="back">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
						name="selectButton" type="button" class="button134" onClick="location.href='/do/home/searchContractDetail/'"
						value="select contract"></div>
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
				</c:if></TD>
			</TR>

		</TBODY>
	</TABLE>
</ps:form>
