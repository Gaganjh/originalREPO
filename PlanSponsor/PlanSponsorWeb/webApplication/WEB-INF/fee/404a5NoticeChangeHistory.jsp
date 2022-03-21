<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem"%>
<%@ page import="com.manulife.pension.ps.service.report.feeSchedule.valueobject.ContractFeeScheduleChangeHistoryReportData"%> 
<%
ContractFeeScheduleChangeHistoryReportData theReport = (ContractFeeScheduleChangeHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
String technicalDifficulties =(String)request.getAttribute(Constants.TECHNICAL_DIFFICULTIES);
pageContext.setAttribute("technicalDifficulties",technicalDifficulties,PageContext.PAGE_SCOPE);
%>






<c:if test="${empty technicalDifficulties}">
	<c:if test="${empty param.printFriendly }" >
		<script language="javascript" type="text/javascript"
			src="/assets/unmanaged/javascript/calendar.js"></script>
		<script type="text/javascript" >
			function doReset() {
				document.forms['noticeInfo404a5ChangeHistoryForm'].task.value = 'default';
				document.noticeInfo404a5ChangeHistoryForm.submit();
			}
			function doBack() {
				document.forms['noticeInfo404a5ChangeHistoryForm'].task.value = "back";
				document.noticeInfo404a5ChangeHistoryForm.submit();
			}
		</script>
	</c:if>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/assets/unmanaged/images/s.gif" width="30"
			height="1"></td>
		<td width="100%" valign="top"><%-- error line --%> &nbsp; <content:errors
			scope="request" /> &nbsp; <ps:form method="POST"
			action="/do/view404a5NoticeInfoChangeHistory/" name="noticeInfo404a5ChangeHistoryForm" modelAttribute="noticeInfo404a5ChangeHistoryForm">
<input type="hidden" name="task" value="filter"/>
			<br>
			<br>
			<ps:isNotInternalOrTpa name ="userProfile" property ="principal.role">
			<content:errors scope="request"/>
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
										src="/assets/unmanaged/images/s.gif" width=125 height=1></TD>
									<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
										height=1></TD>
									<TD><IMG src="/assets/unmanaged/images/s.gif" width=75
										height=1></TD>
									<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
										height=1></TD>
									<TD><IMG src="/assets/unmanaged/images/s.gif" width=265
										height=1></TD>
									<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
										height=1></TD>
									<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
										height=1></TD>
								</TR>
								<TR class=tablehead>
									<td class="tableheadTD1" colSpan="5"><b>Change History</b>
									as of <b> <render:date
										value="<%= (new java.util.Date()).toString() %>"
										patternOut="MMMM dd, yyyy" /></b></td>
									<td class="tableheadTDinfo" colspan="4"> 
									<c:if test="${not empty theReport}"> 
										<b> <report:recordCounter report="theReport"
											label="Submissions" /></b>
									</c:if></td>
									<td align="right" colspan="2" class="tableheadTDinfo">
									<c:if test="${not empty theReport}"> 
										<b><report:pageCounter report="theReport" formName="noticeInfo404a5ChangeHistoryForm" /></b>
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
												<TD width="15%" noWrap><STRONG>Requests from</STRONG></TD>
<TD width="18%" noWrap><form:input path="fromDate" disabled="${param.printFriendly}" maxlength="10" size="10" cssClass="inputAmount" id="filterFromDateId" />



												<c:if test="${empty param.printFriendly}">
													<IMG
														onclick="return handleDateIconClicked(event, 'filterFromDateId', false);"
														src="/assets/unmanaged/images/cal.gif">
												</c:if> <SPAN style="PADDING-LEFT: 4px"> <STRONG>to</STRONG>
												</SPAN></TD>
<TD width="17%" noWrap><form:input path="toDate" disabled="${param.printFriendly}" maxlength="10" size="10" cssClass="inputAmount" id="filterToDateId" />



												<c:if test="${empty param.printFriendly}">
													<IMG
														onclick="return handleDateIconClicked(event, 'filterToDateId', false);"
														src="/assets/unmanaged/images/cal.gif">
												</c:if></TD> 
												<TD width="12%" noWrap><STRONG>User </STRONG></TD>
<TD width="38%"><form:select path="selectedUserId" disabled="${param.printFriendly}" >


													<form:option value=""></form:option>
<form:options items="${noticeInfo404a5ChangeHistoryForm.displayNames}" />


</form:select></TD>
											</TR>
											<TR class=datacell1>
												<TD width="15%" noWrap><STRONG>Type </STRONG></TD>
<TD width="25%" colspan="2" noWrap> <form:select path="selectedFeeType" disabled="${param.printFriendly}">


													<form:option value=""></form:option>
<form:options items="${noticeInfo404a5ChangeHistoryForm.tpaStandardFees}" itemValue="code" itemLabel="description"/>

													<c:if
														test="${ not empty noticeInfo404a5ChangeHistoryForm.tpaCustomFees 
															                 or  not empty noticeInfo404a5ChangeHistoryForm.nonTpaFees}">
														<form:option value=" ">--------------------------------------------------</form:option>
<form:options items="${noticeInfo404a5ChangeHistoryForm.customFees}" itemValue="code" itemLabel="description"/>

													</c:if>
													<form:option value=" ">--------------------------------------------------</form:option>													
<form:options items="${noticeInfo404a5ChangeHistoryForm.pbaStandardFees}" itemValue="code" itemLabel="description"/>

													<c:if
														test="${ not empty noticeInfo404a5ChangeHistoryForm.pbaCustomFees}">
														<form:option value=" ">--------------------------------------------------</form:option>
<form:options items="${noticeInfo404a5ChangeHistoryForm.pbaCustomFees}" itemValue="code" itemLabel="description"/>

													</c:if>
</form:select></TD>
												<TD width="10%" noWrap>&nbsp;</TD>
												<c:if test="${empty param.printFriendly }" >
													<TD width="38%"><SPAN style="FLOAT: left"> <INPUT
														value="search" type="submit" name="Submit" /> <INPUT
														onclick=doReset(); value="reset" type="button"
														name="reset" /> </SPAN></TD>
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
										field="changeDate" direction="asc" formName="noticeInfo404a5ChangeHistoryForm">
										<strong>Date & Time</strong>
									</report:sort></TD>
									<TD class=dataheaddivider height=25><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									<TD height=25 vAlign=center align=left><report:sort
										field="userName" direction="asc" formName="noticeInfo404a5ChangeHistoryForm">
										<strong>User</strong>
									</report:sort></TD>
									<TD class=dataheaddivider height=25><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									<TD height=25 vAlign=center align=left><report:sort
										field="type" direction="asc" formName="noticeInfo404a5ChangeHistoryForm">
										<strong>Type</strong>
									</report:sort></TD>
									<TD class=dataheaddivider height=25><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									<TD height=25 vAlign=center align=right><report:sort
										field="changedValue" direction="asc" formName="noticeInfo404a5ChangeHistoryForm">
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
								<c:if test="${not empty theReport}">
<c:if test="${empty theReport.details}">
										<content:contentBean
											contentId="<%=ContentConstants.CHANGE_HISTORY_NO_DATA_MESSAGE%>"
											type="<%=ContentConstants.TYPE_MESSAGE%>" id="NoDataMessage" />
										<tr class="datacell1">
											<td class="databorder"><img
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
											<td colspan="11"><content:getAttribute
												id="NoDataMessage" attribute="text" /></td>
											<td class="databorder"><img
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
										</tr>
</c:if>
								</c:if>
								<c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >

<c:set var="tempIndex" value="${theIndex.index}"/>
											<%
												Integer theIndex = (Integer)pageContext.getAttribute("tempIndex");
														if (theIndex.intValue() % 2 != 0) {
													%>
											<tr class="datacell1">
												<%
															} else {
														%>
											
											<tr class="datacell2">
												<%
															}
														%>
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
<TD class=paymentTotal vAlign=center align=left style="word-break:break-all;">${theItem.changedType}</TD>

												<TD class=datadivider><IMG
													src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD class=status align=right>${theItem.formttedValue}</TD>

												<TD class=datadivider><IMG
													src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD class=status align=left style="word-break:break-all;" >${theItem.specialNotes}</TD>

												<TD class=databorder><IMG
													src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											</TR>

</c:forEach>
</c:if>
								</c:if>
								<TR>
									<TD class=databorder colSpan=12><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								</TR>
								<TR class="">
									<td class="" colSpan="5"></td>
									<td class="" colspan="4">
									<c:if test="${not empty theReport}"> 
										<b> <report:recordCounter report="theReport"
											label="Submissions" /></b>
									</c:if></td>
									<td align="right" colspan="2" class=""><c:if test="${not empty theReport}"> 

										<b><report:pageCounter report="theReport" formName="noticeInfo404a5ChangeHistoryForm" /></b>
									</c:if></td>
									<TD></TD>
								</TR>
								<TR>
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
			</ps:isNotInternalOrTpa>
			<ps:isInternalOrTpa name ="userProfile" property ="principal.role">
			<TABLE border=0 cellSpacing=0 cellPadding=0 width=720>
					<TBODY>
						<TR>
							<TD colSpan=9><TABLE border=0 cellSpacing=0 cellPadding=0
									width=700>
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
												src="/assets/unmanaged/images/s.gif" width=100 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
											<TD class=paymentTotal><IMG
												src="/assets/unmanaged/images/s.gif" width=150 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=70
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=70
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>	
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=195
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
										</TR>
										<TR class=tablehead>
											<td class="tableheadTD1" colSpan="5"><b>Change History</b> as of <b>
                                            <render:date  value="<%= (new java.util.Date()).toString() %>" patternOut="MMMM dd, yyyy" /></b></td>
											<td class="tableheadTDinfo" colspan="5">
											   <c:if test="${not empty theReport}">
													<b> <report:recordCounter report="theReport" label="Submissions" /></b>
												</c:if></td>
											<td align="right" colspan="3" class="tableheadTDinfo">
											   <c:if test="${not empty theReport}"> 
													<b><report:pageCounter report="theReport" formName="noticeInfo404a5ChangeHistoryForm" /></b>
												</c:if>
										  </td>
										  <TD class=databorder><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
										<TR class=datacell1>
											<TD class=databorder><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD colSpan=12>
												<TABLE border=0 width="100%">
													<TBODY>
														<TR class=datacell1>
															<TD width="15%" noWrap>
															     <STRONG>Requests from</STRONG>
															</TD>
															<TD width="35%" noWrap>
<form:input path="fromDate" disabled="${param.printFriendly}" maxlength="10" size="10" cssClass="inputAmount" id="filterFromDateId" />





																	<c:if test="${empty param.printFriendly}">
																	<IMG
																		onclick="return handleDateIconClicked(event, 'filterFromDateId', false);"
																		src="/assets/unmanaged/images/cal.gif">
															      	</c:if> <SPAN style="PADDING-LEFT: 4px">
															      	<STRONG>to</STRONG>
															    </SPAN>
<form:input path="toDate" disabled="${param.printFriendly}" maxlength="10" size="10" cssClass="inputAmount" id="filterToDateId" />





																	<c:if test="${empty param.printFriendly}">
																	<IMG
																		onclick="return handleDateIconClicked(event, 'filterToDateId', false);"
																		src="/assets/unmanaged/images/cal.gif">
																    </c:if>
																</TD>
															<TD width="12%" noWrap><STRONG>User </STRONG></TD>
															<TD width="38%">
<form:select path="selectedUserId" disabled="${param.printFriendly}" >



																	<form:option value=""></form:option>
<form:options items="${noticeInfo404a5ChangeHistoryForm.displayNames}" />


</form:select>
															</TD>
														</TR>
														<TR class=datacell1>
														    <TD width="15%" noWrap><STRONG>Standard Schedule Applied</STRONG></TD>
															<TD width="35%">
<form:select path="standardScheduleApplied" disabled="${param.printFriendly}" >



																	<form:option value=""></form:option>
																	<form:option value="Y">Y</form:option>
																	<form:option value="N">N</form:option>
</form:select>
															</TD>
															<TD width="12%" noWrap><STRONG>Type </STRONG></TD>
															<TD width="38%" colspan="2" noWrap>
 <form:select path="selectedFeeType" disabled="${param.printFriendly}">


																	<form:option value=""></form:option>
<form:options items="${noticeInfo404a5ChangeHistoryForm.tpaStandardFees}" itemValue="code" itemLabel="description"/>

																	<c:if test="${ not empty noticeInfo404a5ChangeHistoryForm.tpaCustomFees 
															                 or  not empty noticeInfo404a5ChangeHistoryForm.nonTpaFees}">
														               <form:option value=" ">--------------------------------------------------</form:option>
<form:options items="${noticeInfo404a5ChangeHistoryForm.customFees}" itemValue="code" itemLabel="description"/>
													                </c:if>	
													                <form:option value=" ">--------------------------------------------------</form:option>													
<form:options items="${noticeInfo404a5ChangeHistoryForm.pbaStandardFees}" itemValue="code" itemLabel="description"/>
																	<c:if test="${ not empty noticeInfo404a5ChangeHistoryForm.pbaCustomFees}">
																		<form:option value=" ">--------------------------------------------------</form:option>
<form:options items="${noticeInfo404a5ChangeHistoryForm.pbaCustomFees}" itemValue="code" itemLabel="description"/>
																	</c:if>							
</form:select>
															</TD>
														</TR>
														<c:if test="${empty param.printFriendly }" >
														<TR class=datacell1>
														    <TD width="15%" noWrap></TD>
															<TD width="35%"></TD>
															<TD width="10%" noWrap>&nbsp;</TD>
															<c:if test="${empty param.printFriendly }" >
																<TD width="38%"><SPAN style="FLOAT: left">
																<INPUT value="search" type="submit" name="Submit" />
															    <INPUT onclick=doReset(); value="reset" type="button" name="reset" />
															    </SPAN></TD>
															</c:if>
														</TR>
														</c:if>
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
											<TD height=25 vAlign=center align=left>
											   <report:sort field="changeDate" direction="asc" formName="noticeInfo404a5ChangeHistoryForm">
													<strong>Date & Time</strong>
												</report:sort>
											</TD>
											<TD class=dataheaddivider height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD height=25 vAlign=center align=left>
											    <report:sort field="userName" direction="asc" formName="noticeInfo404a5ChangeHistoryForm">
													<strong>User</strong>
												</report:sort>
											</TD>
											<TD class=dataheaddivider height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD height=25 vAlign=center align=left>
											    <report:sort field="type" direction="asc" formName="noticeInfo404a5ChangeHistoryForm">
													<strong>Type</strong>
												</report:sort>
											</TD>
											<TD class=dataheaddivider height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD height=25 vAlign=center align=right>
											    <report:sort field="changedValue" direction="asc" formName="noticeInfo404a5ChangeHistoryForm">
													<strong>Value</strong>
												</report:sort>
											</TD>
											<TD class=dataheaddivider height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD height=25 vAlign=center align=right>
											    <report:sort field="standardScheduleApplied" direction="asc" formName="noticeInfo404a5ChangeHistoryForm">
													<strong>Standard Schedule Applied</strong>
												</report:sort>
											</TD>
											<TD class=dataheaddivider height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD height=25 vAlign=center align=left>
											    <strong>Special Notes</strong>
											</TD>
											<TD class=databorder height=25><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>

										<%-- Message line if there are no detail items --%>
										<c:if test="${not empty theReport}">
<c:if test="${empty theReport.details}">
												<content:contentBean
													contentId="<%=ContentConstants.CHANGE_HISTORY_NO_DATA_MESSAGE%>"
													type="<%=ContentConstants.TYPE_MESSAGE%>"
													id="NoDataMessage" />
												<tr class="datacell1">
													<td class="databorder"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td colspan="12">
													      <content:getAttribute id="NoDataMessage" attribute="text" />
													</td>
													<td class="databorder"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
</c:if>
										</c:if>
										<c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >

<c:set var="tempIndex" value="${theIndex.index}"/>



													<%
													Integer theIndex = (Integer)pageContext.getAttribute("tempIndex");
														if (theIndex.intValue() % 2 != 0) {
													%>
													<tr class="datacell1">
														<%
															} else {
														%>
													<tr class="datacell2">
														<%
															}
														%>
														<TD class=databorder><IMG
															src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=actions align=middle></TD>
														<TD class=payrollDate align=left>
														        <render:date property="theItem.changedDate" patternOut="MM/dd/yyyy hh:mm:ss a" />
														</TD>
														<TD class=datadivider><IMG
															src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=payrollDate align=left>
${theItem.userName}
													    </TD>
														<TD class=datadivider><IMG
															src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=paymentTotal vAlign=center align=left >
${theItem.changedType}
														</TD>
														<TD class=datadivider><IMG
															src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=status align=right>
${theItem.formttedValue}
													    </TD>
													    <TD class=datadivider><IMG
															src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=status align=right>
${theItem.standardScheduleAppliedValue}
													    </TD>
														<TD class=datadivider><IMG
															src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD class=status align=left style="word-break:break-all;">
${theItem.specialNotes}
													    </TD>
														<TD class=databorder><IMG
															src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>

</c:forEach>
</c:if>
										</c:if>
										<TR>
											<TD class=databorder colSpan=14><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
										<TR class="">
											<td class="" colSpan="5"></td>
											<td class="" colspan="4">
											   <c:if test="${not empty theReport}">
													<b> <report:recordCounter report="theReport" label="Submissions" /></b>
												</c:if></td>
											<td align="right" colspan="5" class="">
											   <c:if test="${not empty theReport}"> 
													<b><report:pageCounter report="theReport" formName="noticeInfo404a5ChangeHistoryForm"/></b>
												</c:if>
										  </td>
										  <TD ></TD>
										</TR>
										<TR>
										</TR>
									</TBODY>
								</TABLE></TD>
						</TR>
						<TR>
							<TD></TD>
							<TD>&nbsp;</TD>
						</TR>
					</TBODY>
			</TABLE>
			</ps:isInternalOrTpa>
			<br>
			<TABLE border=0 cellSpacing=0 cellPadding=0 width=700>
				<tr>
					<td width="410" align="right"></td>
					<c:if test="${empty param.printFriendly }" >
<td width="280" align="right"><input type="button" onclick="return doBack();" name="button" class="button134" value="back"/></td>


					</c:if>
				</tr>
			</table>
		</ps:form> <c:if test="${not empty param.printFriendly }" >
			<content:contentBean
				contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
				type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
				id="globalDisclosure" />
			<table border=0 cellSpacing=0 cellPadding=0 width=730>
				<tr>
					<td width="100%">
					<p><content:pageFooter beanName="layoutPageBean" /></p>
					</td>
				</tr>
				<tr>
					<td width="100%"><content:getAttribute
						beanName="globalDisclosure" attribute="text" /></td>
				</tr>
			</table>
		</c:if></td>
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
