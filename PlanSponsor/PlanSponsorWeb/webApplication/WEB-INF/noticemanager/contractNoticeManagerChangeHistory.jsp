<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>

<%-- Imports --%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.*"%>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<%@ page
	import="com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription"%>

<%@ page
	import="com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO"%>


<%@ page
	import="com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentHistoryReportData"%>




<%@ page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%-- CMA Contents  --%>
<content:contentBean
	contentId="<%=ContentConstants.NMC_HISTORY_FROM_DATE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="fromdate" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_HISTORY_TO_DATE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="todate" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_HISTORY_USERNAME%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="username" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_HISTORY_ACTION%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="actionType" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_HISTORY_DOCUMENT%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="Documentname" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_HISTORY_DATE_MSG%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="calErrorMsg" />


<script type="text/javascript">
	/**
	 * Method to set the task in the form and submit the from.
	 * Called when the user clicks the Search button	
	 */
	 var calErrorMsg = '<content:getAttribute id="calErrorMsg" attribute="text" filter="true"/>'
	 var dateClickedId=""
	function doPendingSearch() {
		//document.pendingWithdrawalSummaryForm.task.value = 'filter';

		document.contractnotifychangehistory.task.value = 'search';
		document.contractnotifychangehistory.submit();
	}

	function doPendingReset() {
		//document.pendingWithdrawalSummaryForm.task.value = 'filter';

		document.contractnotifychangehistory.task.value = 'reset';
		document.contractnotifychangehistory.submit();
	}
	function doPendingBack() {
		//document.pendingWithdrawalSummaryForm.task.value = 'filter';

		document.contractnotifychangehistory.task.value = 'back';
		document.contractnotifychangehistory.submit();
	}
	function datefor() {
		var formattedDate = new Date().dateFormat('mmm dd, yyyy');

		document.getElementById("time").innerHTML = formattedDate;
	}

	var backLabel = 'Click here to return to the previous screen.  '

	function enableButtons() {
		if (document.getElementById('backButton')) {
			document.getElementById('backButton').disabled = false;
		} 
	}

	// Registers our function with the functions that are run on load.
	if (typeof (runOnLoad) == "function") {
		runOnLoad(enableButtons);
	}

	function doSubmit(action) {
		document.getElementsByName("action")[0].value = action;
		document.contractnotifychangehistory.submit();
	}
</script>
<script language="javascript" type="text/javascript"
	src="/assets/unmanaged/javascript/changeHistoryCalendar.js"></script>
<%-- <c:if test="${not empty Constants.REPORT_BEAN} }"> --%>
<%
PlanDocumentHistoryReportData theReport = (PlanDocumentHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

%>
<c:set var="printFriendly" value="${param.printFriendly}" />

<%-- </c:if> --%>
<ps:form method="post"
	action="/do/noticemanager/contractnoticechangehistory/" modelAttribute="contractnotifychangehistory" name="contractnotifychangehistory">
	<TABLE border=0 cellSpacing=0 cellPadding=0 width="730">
		<TBODY>
			<TR>
				<TD width=30><IMG
											src="/assets/unmanaged/images/s.gif" width="30" height=1></TD>
				<TD width=700>
				<DIV id=messagesBox class=messagesBox></DIV>
<DIV style="DISPLAY: block" id=basic>
<form:hidden path="task" />
<INPUT

					id=contractNo type=hidden name=contractId> <INPUT id=IdCon
					value=null type=hidden name=IdCon>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="100%" valign="top">
						<p><c:if test="${not empty requestScope.psErrors}">
<c:set var="errorsExist" value="${true}" scope="page" />

							<div id="errordivcs"><content:errors scope="request" /></div>
						</c:if></p>

						</td>
					</tr>
				</table>
				<TABLE border=0 cellSpacing=0 cellPadding=0 width=700>
					<TBODY>

						<TR>
							<TD valign="top" width="700">
							<TABLE border="0" cellSpacing="0" cellPadding="0" width="700">
								<TBODY>
									<tr class="tablehead">
										<td class="tableheadTD1" colspan="6" valign="middle"
align="left" height="30"><b>Change history </b>as of ${contractnotifychangehistory.todayDate}</td>

										<td class="tableheadTD" colspan="4" valign="middle"
											align="right"><c:if test="${theReport !=null}"> 
											<B> <report:recordCounter report="theReport"
												label="Changes" /></B>
										</c:if></td>
										<td class="tableheadTD" colspan="4" valign="middle"
											align="right"><c:if test="${theReport!=null}"> 
											<report:pageCounter report="theReport" arrowColor="black" formName="contractnotifychangehistory" />
										</c:if></TD>
									</tr>

									<TR class=datacell1>
										<TD class=databorder width="1"><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD colSpan=12>
										<TABLE border="0" width="100%">
											<TBODY>
												<TR class=datacell1>
													<TD width="120" noWrap><STRONG><content:getAttribute
														attribute="text" beanName="fromdate" /></STRONG></TD>
													<TD width="120" noWrap valign="middle"><c:if test="${printFriendly == null}">

<form:input path="fromDate" maxlength="10" size="10" >

${contractnotifychangehistory.fromDate}

</form:input>
														<img
															onclick="dateClickedId ='fromdate'; handleDateIconClicked(event,'fromDate');"
															src="/assets/unmanaged/images/cal.gif"
															alt="Use the Calendar to pick the date" />
													</c:if> <c:if test="${printFriendly !=null}" >
<form:input path="fromDate" disabled="true" maxlength="10" size="10" >

${contractnotifychangehistory.fromDate}

</form:input>
														<img src="/assets/unmanaged/images/cal.gif"
															alt="Use the Calendar to pick the date" />
													</c:if></TD>
													<TD width="60" align="left">
													<p><span style="PADDING-LEFT: 4px"><STRONG><content:getAttribute
														attribute="text" beanName="todate" /></STRONG> </SPAN></p>
													</TD>
													<TD width="120" noWrap><c:if test="${printFriendly == null}">

<form:input path="toDate" maxlength="10" size="10" >

${contractnotifychangehistory.toDate}

</form:input>
														<img
															onclick="dateClickedId ='todate'; handleDateIconClicked(event, 'toDate');"
															src="/assets/unmanaged/images/cal.gif"
															alt="Use the Calendar to pick the date" />
													</c:if> <c:if test="${printFriendly != null}" >
<form:input path="toDate" disabled="true" maxlength="10" size="10" >

${contractnotifychangehistory.toDate}

</form:input>
														<img src="/assets/unmanaged/images/cal.gif"
															alt="Use the Calendar to pick the date" />
													</c:if></TD>
													<TD width="80" align="center" noWrap><STRONG><content:getAttribute
														attribute="text" beanName="username" /> </STRONG></TD>
													<TD width="100"><c:if test="${printFriendly == null}">
														<form:select styleId="userId" path="userId">
															<form:option value=""></form:option>
															<c:forEach var="relationMap"
																items="${contractnotifychangehistory.userProfileDetails}">
<c:if test="${relationMap.value !=None}">

																	<form:option value="${relationMap.key}">
																		<c:out
																			value="${relationMap.value.lookupCode}  ${relationMap.value.lookupDesc}" />
																	</form:option>
</c:if>
															</c:forEach>
</form:select>
													</c:if> <c:if test="${printFriendly != null}" >
														<form:select path="userId" disabled="true">
															<form:option value=""></form:option>
															<c:forEach var="relationMap"
																items="${contractnotifychangehistory.userProfileDetails}">
<c:if test="${relationMap.value !=None}">

																	<form:option value="${relationMap.key}">
																		<c:out
																			value="${relationMap.value.lookupCode}  ${relationMap.value.lookupDesc}" />
																	</form:option>
</c:if>
															</c:forEach>
</form:select>
													</c:if></TD>

													<TD><STRONG><content:getAttribute
														attribute="text" beanName="actionType" /> </STRONG></TD>
													<TD colspan="4"><c:if test="${printFriendly == null}">
														<form:select path="actionType">
															<form:option value="">-Select-</form:option>
															<form:option value="CHNG">Changed</form:option>
															<form:option value="CHRP">Changed & Replaced</form:option>
															<form:option value="DEL">Deleted</form:option>
															<form:option value="REPL">Replaced</form:option>
															<form:option value="UPLD">Uploaded</form:option>
</form:select>
													</c:if> <c:if test="${printFriendly !=null}" >
														<form:select disabled="true" path="actionType">
															<form:option value="">-Select-</form:option>
															<form:option value="CHNG">Changed</form:option>
															<form:option value="CHRP">Changed & Replaced</form:option>
															<form:option value="DEL">Deleted</form:option>
															<form:option value="REPL">Replaced</form:option>
															<form:option value="UPLD">Uploaded</form:option>
</form:select>
													</c:if></TD>
												</TR>
												<TR>

													<TD width="120" align="left" noWrap><STRONG><content:getAttribute
														attribute="text" beanName="Documentname" /> </STRONG></TD>
													<TD width="120" colspan="3"><c:if test="${printFriendly == null}">
<form:input path="documentName" maxlength="40" size="37" id="toDate">

</form:input>
													</c:if> <c:if test="${printFriendly != null}" >
<form:input path="documentName" disabled="true" maxlength="40" size="37" id="toDate">

</form:input>
													</c:if></TD>

													<TD>&nbsp;</TD>
													<TD>&nbsp;</TD>
													<TD>&nbsp;</TD>
													<TD>&nbsp;</TD>
													<TD>&nbsp;</TD>
													<TD>
													<p><SPAN style="FLOAT: left"> <c:if test="${printFriendly== null}">
<input type="button" onclick="doPendingSearch(); return false;" name="button" value="search"/>


													</c:if> <c:if test="${printFriendly != null}" >
<input type="button" onclick="return false;" name="button" value="search"/>

													</c:if> <c:if test="${printFriendly == null}" >
<input type="button" onclick="doPendingReset(); return false;" name="button" value="reset"/>


													</c:if> <c:if test="${printFriendly != null}" >
<input type="button" onclick="return false;" name="button" value="reset"/>

													</c:if> </SPAN>
													</TD>
												</TR>

											</TBODY>
										</TABLE>
										</TD>
										<TD class=databorder width="1"><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									</TR>
									<TR class=tablesubhead>
										<TD class=databorder height=25><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=actions></TD>
										<TD class=payrollDate height=25 vAlign=middle align=left
											style="padding-left: 4px;"><b><report:sort
											field="<%=PlanDocumentHistoryReportData.ACTION_DATE_FIELD%>"
											direction="desc" formName="contractnotifychangehistory">Date & time</report:sort></b></TD>
										<TD class=dataheaddivider height=25><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=payrollDate height=25 vAlign=middle align=left
											style="padding-left: 4px;"><b><report:sort
											field="<%=PlanDocumentHistoryReportData.USER_FIRST_NAME_FIELD%>"
											direction="asc" formName="contractnotifychangehistory">User name</report:sort></b></TD>
										<TD class=dataheaddivider height=25><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=contributionTotal height=25 width=90 vAlign=middle
											align=left style="padding-left: 4px;"><b><report:sort
											field="<%=PlanDocumentHistoryReportData.ACTION_TAKEN_FIELD%>"
											direction="asc" formName="contractnotifychangehistory">Action</report:sort></b></TD>
										<TD class=dataheaddivider height=25><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=status height=25 vAlign=middle align=left
											style="padding-left: 4px;"><b><report:sort
											field="<%=PlanDocumentHistoryReportData.DOCUMENT_NAME%>"
											direction="asc" formName="contractnotifychangehistory">Document name</report:sort></b></TD>
										<TD class=dataheaddivider height=25><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=status height=25 vAlign=middle align=left
											style="padding-left: 4px;"><B><report:sort
											field="<%=PlanDocumentHistoryReportData.REVISED_DOCUMENT_FIELD%>"
											direction="asc" formName="contractnotifychangehistory">Revised document name</report:sort></B></TD>
										<TD class=dataheaddivider height=25><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										<TD class=status height=25 vAlign=middle align=left
											style="padding-left: 4px;"><B><report:sort
											field="<%=PlanDocumentHistoryReportData.POST_TO_PPT_FIELD%>"
											direction="asc" formName="contractnotifychangehistory">Participant website</report:sort></B></TD>
										<TD class=databorder height=25><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									</TR>

									<%-- Message line if there are no detail items --%>
									<c:if test="${theReport!=null }">
<c:if test="${empty theReport.planNoticeDocumentChangeHistorys}">

											<content:contentBean
												contentId="<%=ContentConstants.CHANGE_HISTORY__PAGE_NO_DATA_MESSAGE%>"
												type="<%=ContentConstants.TYPE_MESSAGE%>" id="NoDataMessage" />

											<tr class="datacell1">
												<td class="databorder"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												<td colspan="12"><content:getAttribute
													id="NoDataMessage" attribute="text" /></td>
												<td class="databorder"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
											</tr>

</c:if>
									</c:if>
									<c:if test="${theReport!=null}">
<c:if test="${theReport.planNoticeDocumentChangeHistorys!=null}">
<c:forEach items="${theReport.planNoticeDocumentChangeHistorys}" var="theItem" varStatus="theIndex" >
 <c:set var="indexValue" value="${theIndex.index}"/> 
 <% 
  String indexVal = pageContext.getAttribute("indexValue").toString();
 %>


												<TR class=datacell1>
													<%
																	if (Integer.parseInt(indexVal) % 2 == 0) {
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
															<TD class=actions align=center></TD>
															<TD class=payrollDate align=left
																style="padding-left: 4px;"><render:date
																patternOut="MM/dd/yyyy hh:mm:ss a"
																property="theItem.changedDate" /></TD>
															<TD class=datadivider><IMG
																src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<TD class=payrollDate align=left
style="padding-left: 4px;">${theItem.changedUserName}</TD>

															<TD class=datadivider><IMG
																src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<TD class=paymentTotal vAlign="middle" align=left
style="padding-left: 4px;">${theItem.planNoticeDocumentChangeTypeDetail.lookupDesc}


															</TD>
															<TD class=datadivider><IMG
																src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<TD class=status align=left style="padding-left: 4px;">
<c:if test="${empty theItem.replacedfileName}">
${theItem.documentName}
</c:if> <c:if test="${not empty theItem.replacedfileName}">

${theItem.replacedfileName}
</c:if></TD>
															<TD class=datadivider><IMG
																src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<TD class=status align=left style="padding-left: 4px;">
<c:if test="${not empty theItem.replacedfileName}">

${theItem.documentName}
</c:if></TD>
															<TD class=datadivider><IMG
																src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<TD class=status align="center"
style="padding-left: 4px;">${theItem.changedPPT}</TD>

															<TD class=databorder><IMG
																src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														</TR>
</c:forEach>
</c:if>
									</c:if>

									<%-- CMA Managed --%>
									<c:if test="${empty theReport}"> 
										<TR class=datacell1 height=25>

											<TD class=databorder><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD class=actions colspan="12"><content:getAttribute
												id="retirementResourceCenter" attribute="title" /></TD>
											<TD class=databorder><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
									</c:if>

									<TR>
										<TD class=databorder colSpan=14><IMG
											src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									</TR>
									<tr>
										<td  colspan="6" valign="middle"
											align="left" ><IMG src="/assets/unmanaged/images/s.gif"
											width=1 height=1></td>
										<td class="dark_grey_color" colspan="4" valign="middle"
											align="right"><c:if test="${theReport != null}"> 
											<B><report:recordCounter report="theReport"
												label="Changes" /></B>
										</c:if></td>
										<td class="dark_grey_color" colspan="4" valign="middle"
											align="right"><c:if test="${theReport != null}">
											<report:pageCounter report="theReport" arrowColor="black" formName="contractnotifychangehistory" />
										</c:if></TD>
									</tr>
								</TBODY>
							</TABLE>
							</TD>
							<TD width=10><IMG border=0
								src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
							<TD vAlign=top width=190 align=center><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=25> <IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=5> <IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=5></TD>
						</TR>
						<TR>
							<TD></TD>
							<TD width=10><IMG border=0
								src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
							<TD>&nbsp;</TD>
						</TR>
					</TBODY>
				</TABLE>
				<c:if test="${printFriendly == null}" >
					<TABLE border=0 cellSpacing=0 cellPadding=0 width=715>
						<tr>
							<td width="565" align="right">&nbsp;</td>
<td width="150" align="left"><input type="button" onclick="doPendingBack(); return false;" name="button3" class="button134" id="backButton" value="back"/></td>



						</tr>
					</table>
				</c:if> <BR/>
				</DIV>
				</TD>
			</TR>
		</TBODY>
	</TABLE>
</ps:form>
<c:if test="${printFriendly!= null }" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute
				beanName="globalDisclosure" attribute="text" /></td>
		</tr>
	</table>
</c:if>
<%-- footer table --%>

<script>
	$(document).ready(function(){
		var toDateTemp = $("#toDateId").val();
		var fromDateTemp = $("#fromDateId").val();
		$(document).on("click",function(){
			if(fromDateTemp != $("#fromDateId").val() || toDateTemp != $("#toDateId").val()){
				toDateTemp = $("#toDateId").val();
				fromDateTemp = $("#fromDateId").val();
				var html = "<option value=''></option>";;
				$.ajax({
					url: window.location.pathname  + "?task=fetchUsers&toDate="+ toDateTemp +"&fromDate=" + fromDateTemp,
					success:function(result){
						if(result.charAt(0) == "{"){
							var temp = result.substring(1, result.length-1);
							//console.log(temp);
							temp = temp.split(",");
							for(var i = 0 ; i < temp.length; i++){
								//console.log(temp[i]);
								var record = temp[i].split("=");
								if("" != record[0]){
									html += '<option value="'+ record[0] +'">'+ record[1] +'</option>'
								}
							}
							//console.log(html);
							$("#userId").html(html);
						}
				  }});
			}
		});


		//console.log(toDateTemp + " From: " + fromDateTemp);
		
	});
</script>
<style>
#userId {
	width: 85px;
}
</style>
