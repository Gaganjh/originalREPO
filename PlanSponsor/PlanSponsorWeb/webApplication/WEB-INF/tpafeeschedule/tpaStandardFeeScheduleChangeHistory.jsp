<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page
	import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page
	import="com.manulife.pension.ps.service.report.feeSchedule.valueobject.TpaStandardFeeScheduleChangeHistoryReportData"%>
	<%
		TpaStandardFeeScheduleChangeHistoryReportData theReport = (TpaStandardFeeScheduleChangeHistoryReportData) request
					.getAttribute(Constants.REPORT_BEAN);
			pageContext.setAttribute("theReport", theReport, PageContext.PAGE_SCOPE);
			String technicalDifficulties =(String)request.getAttribute(Constants.TECHNICAL_DIFFICULTIES);
			pageContext.setAttribute("technicalDifficulties",technicalDifficulties,PageContext.PAGE_SCOPE);
	%>



<c:if test="${empty  technicalDifficulties}">
	<c:if test="${empty param.printFriendly }">
		<script language="javascript" type="text/javascript"
			src="/assets/unmanaged/javascript/calendar.js"></script>
		<script type="text/javascript">
			function doReset() {
				 document.forms['tpaStandardFeeScheduleChangeHistoryReportForm'].task.value = '';
				document.tpaStandardFeeScheduleChangeHistoryReportForm
						.submit();
			}
			function doBack() {
				 document.forms['tpaStandardFeeScheduleChangeHistoryReportForm'].task.value = 'back';
				document.tpaStandardFeeScheduleChangeHistoryReportForm
						.submit();
			}
			function doBlockScheduleReport() {
				var reportURL = new URL("/do/tpa/blockScheduleReport/");
				reportURL.setParameter("selectedTpaId", document
						.getElementById("selectedTpaId").value);
				location.href = reportURL.encodeURL();
			}
		</script>

	</c:if>
</c:if>
<%-- technical difficulties --%>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/assets/unmanaged/images/s.gif" width="30"
			height="1"></td>
		<td width="100%" valign="top">
			<%-- error line --%>
			<div id="messagesBox" class="messagesBox">
				<%-- Override max height if print friendly is on so we don't scroll --%>
				<ps:messages scope="request"
					maxHeight="${param.printFriendly ? '1000px' : '100px'}"
					suppressDuplicateMessages="true" />
			</div>
			<DIV style="DISPLAY: block" id=basic></DIV> <%-- technical difficulties --%>
			<ps:form method="POST"
				modelAttribute="tpaStandardFeeScheduleChangeHistoryReportForm"
				name="tpaStandardFeeScheduleChangeHistoryReportForm"
				action="/do/changeHistoryTpaStandardFeeSchedule/">
				<form:hidden path="tpaFirmId" id="selectedTpaId" />
				<%--  input - name="tpaStandardFeeScheduleChangeHistoryReportForm" --%>
<input type="hidden" name="task" value="filter"/>

				<br>
				<br>
				<TABLE border=0 cellSpacing=0 cellPadding=0 width=720>
					<TBODY>

						<TR>
							<TD colSpan=9>
								<TABLE border=0 cellSpacing=0 cellPadding=0 width=700>
									<TBODY>

										<TR>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
											<TD class=actions><IMG
												src="/assets/unmanaged/images/s.gif" width=10 height=1></TD>
											<TD class=payrollDate><IMG
												src="/assets/unmanaged/images/s.gif" width=130 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
											<TD class=contributionTotal align=left><IMG
												src="/assets/unmanaged/images/s.gif" width=120 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
											<TD class=paymentTotal><IMG
												src="/assets/unmanaged/images/s.gif" width=150 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=50
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=255
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
										</TR>

										<TR class=tablehead>
											<td class="tableheadTD1" colSpan="5"><b>Change
													History</b> as of <b><render:date
														patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>"
														property="tpaStandardFeeScheduleChangeHistoryReportForm.asOfDate" /></b></td>

											<td class="tableheadTDinfo" colspan="4"><c:if
													test="${not empty theReport.details}">
													<b> <report:recordCounter report="theReport"
															label="Submissions"  /></b>
												</c:if></td>
											<td align="right" colspan="2" class="tableheadTDinfo"><c:if
													test="${not empty theReport.details}">
													<b><report:pageCounter report="theReport"  formName="tpaStandardFeeScheduleChangeHistoryReportForm"/></b>
												</c:if></td>

											<TD class=databorder><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
										<TR class=datacell1>
											<TD class=databorder><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD colSpan=10>
												<TABLE border=0 width="100%">
													<TBODY>
														<TR class=datacell1>
															<TD width="15%" noWrap><STRONG>Requests
																	from</STRONG></TD>
															<TD width="18%" noWrap><form:input path="fromDate"
																	disabled="${param.printFriendly}" maxlength="10"
																	size="10" cssClass="inputAmount" id="filterFromDateId" />
																<c:if test="${empty param.printFriendly}">
																	<IMG
																		onclick="return handleDateIconClicked(event, 'filterFromDateId', false);"
																		src="/assets/unmanaged/images/cal.gif">
																</c:if> <SPAN style="PADDING-LEFT: 4px"><STRONG>to</STRONG>
															</SPAN></TD>
															<TD width="17%" noWrap><form:input path="toDate"
																	disabled="${param.printFriendly}" maxlength="10"
																	size="10" cssClass="inputAmount" id="filterToDateId" />
																<c:if test="${empty param.printFriendly}">
																	<IMG
																		onclick="return handleDateIconClicked(event, 'filterToDateId', false);"
																		src="/assets/unmanaged/images/cal.gif">
																</c:if></TD>
															<TD width="12%" noWrap><STRONG>User</STRONG></TD>
															
															<TD width="38%"><form:select path="userName"
																	disabled="${param.printFriendly}">

																	<form:option value=""></form:option>
																	<form:options items="${tpaStandardFeeScheduleChangeHistoryReportForm.userNames}" />

																</form:select></TD>
														</TR>
														<TR class=datacell1>


															<TD width="15%" noWrap><STRONG>Type </STRONG></TD>
															<TD width="25%" colspan="2" noWrap><form:select
																	path="feeType" disabled="${param.printFriendly}">

																	<form:option value=""></form:option>
																	<form:options items="${tpaStandardFeeScheduleChangeHistoryReportForm.feeTypes}"  />

																</form:select></TD>

															<TD width="10%" noWrap>&nbsp;</TD>
															<c:if test="${empty param.printFriendly }">
																<TD width="38%"><SPAN style="FLOAT: left"><INPUT
																		value="search" type="submit" name="Submit" /> <INPUT
																		onclick=doReset(); value="reset" type="submit"
																		name="reset" /></SPAN></TD>
															</c:if>
														</TR>
													</TBODY>
												</TABLE>
											</TD>
											<TD class=databorder><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
										<TR class=tablesubhead>
											<TD class=databorder height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD class=actions></TD>
											<TD height=25 vAlign=center align=left><report:sort
													field="changeDate" direction="desc" formName="tpaStandardFeeScheduleChangeHistoryReportForm">
													<strong>Date & Time</strong>
												</report:sort></TD>
											<TD class=dataheaddivider height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD height=25 vAlign=center align=left><report:sort
													field="userName" direction="asc" formName="tpaStandardFeeScheduleChangeHistoryReportForm">
													<strong>User</strong>
												</report:sort></TD>
											<TD class=dataheaddivider height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD height=25 vAlign=center align=left><report:sort
													field="type" direction="asc" formName="tpaStandardFeeScheduleChangeHistoryReportForm">
													<strong>Type</strong>
												</report:sort></TD>
											<TD class=dataheaddivider height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD height=25 vAlign=center align=right><report:sort
													field="changedValue" direction="asc" formName="tpaStandardFeeScheduleChangeHistoryReportForm">
													<strong>Value</strong>
												</report:sort></TD>
											<TD class=dataheaddivider height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD height=25 vAlign=center align=left><strong>Special
													Notes</strong></TD>
											<TD class=databorder height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>

										<%-- Message line if there are no detail items --%>
											<c:if test="${empty theReport.details}">
												<content:contentBean
													contentId="<%=ContentConstants.CHANGE_HISTORY_NO_DATA_MESSAGE%>"
													type="<%=ContentConstants.TYPE_MESSAGE%>"
													id="NoDataMessage" />

												<tr class="datacell1">
													<td class="databorder"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td colspan="11"><content:getAttribute
															id="NoDataMessage" attribute="text" /></td>
													<td class="databorder"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>

											</c:if>
											<c:if test="${not empty theReport.details}">
												<c:forEach items="${theReport.details}" var="theItem"
													varStatus="theIndex">

													<c:choose>
														<c:when test="${theIndex.index % 2 == 0}">
															<tr class="datacell1">
														</c:when>
														<c:otherwise>
															<tr class="datacell2">
														</c:otherwise>
													</c:choose>


													<TD class=databorder><IMG
														src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD class=actions align=middle></TD>
													<TD class=payrollDate align=left><render:date
															property="theItem.changedDate"
															patternOut="MM/dd/yyyy hh:mm:ss a" /></TD>
													<TD class=datadivider><IMG
														src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD class=payrollDate align=left>${theItem.userName}</TD>

													<TD class=datadivider><IMG
														src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD class=paymentTotal vAlign=center align=left>
														${theItem.changedType}</TD>

													<TD class=datadivider><IMG
														src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD class=status align=right>${theItem.formttedValue}</TD>

													<TD class=datadivider><IMG
														src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD class=status align=left style="word-break: break-all;">
														${theItem.specialNotes}</TD>
													<TD class=databorder><IMG
														src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>

												</c:forEach>
											</c:if>

										<TR>
											<TD class=databorder colSpan=12><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
										<TR>
											<TD colSpan=6><IMG src="/assets/unmanaged/images/s.gif"
												width=1 height=1></TD>
											<TD vAlign=center colSpan=4><c:if
													test="${not empty theReport.details}">
													<b> <report:recordCounter report="theReport"
															label="Submissions" /></b>
												</c:if></TD>
											<td colspan="2" align="right"><c:if
													test="${not empty theReport.details}">
													<report:pageCounter report="theReport" arrowColor="black" formName="tpaStandardFeeScheduleChangeHistoryReportForm"/>
												</c:if></td>

											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
										</TR>
									</TBODY>
								</TABLE>
							</TD>
						</TR>
						<TR>
							<TD></TD>
							<TD>&nbsp;</TD>
						</TR>
					</TBODY>
				</TABLE>
				<br>

				<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
					<tr>
						<td width="410" align="right"></td>

						<c:if test="${empty param.printFriendly }">
							<td width="280" align="right"><input type="submit"
								onclick="return doBack();" name="button" class="button134"
								value="back" /></td>


						</c:if>
					</tr>
				</table>
				<c:if test="${not empty param.printFriendly}">
					<content:contentBean
						contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
						type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
						id="globalDisclosure" />
					<table border=0 cellSpacing=0 cellPadding=0 width=730>
						<tr>
							<td width="100%">
								<p>
									<content:pageFooter beanName="layoutPageBean" />
								</p>
							</td>
						</tr>
						<tr>
							<td width="100%">&nbsp;</td>
						</tr>
						<tr>
							<td width="100%"><content:getAttribute
									beanName="globalDisclosure" attribute="text" /></td>
						</tr>
					</table>
				</c:if>
			</ps:form>
		</td>
	</tr>
</table>
<c:if test="${empty param.printFriendly}">
	<table cellpadding="0" cellspacing="0" border="0" width="730"
		class="fixedTable" height="">
		<tr>
			<td width="30">&nbsp;</td>
			<td width="700"><content:pageFooter beanName="layoutPageBean" /></td>
		</tr>
	</table>
</c:if>
