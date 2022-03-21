<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<%
SubmissionHistoryReportData theReport = (SubmissionHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%> 








			 
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_HISTORY_COMPLETE_STATUS_FOOTER%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="CompleteText"/>
                     
<content:contentBean contentId="<%=ContentConstants.SEARCH_CRITERIA_RETURNS_NO_RESULTS%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="NoResultsMessage"/>

<SCRIPT
    language="JavaScript">

function submitform()
{
	if(document.getElementsByName("justMine")[0].checked) setFilterFromInput(document.getElementsByName("justMine")[0]);
<c:if test="${viewDocumentUploadHistoryForm.filterSimple !=true}">
	if(document.getElementsByName("filterBySubmission")[1].checked) {
		document.getElementsByName("filterBySubmission")[1].value = false;
		setFilterFromInput(document.getElementsByName("filterBySubmission")[1]);
	}
</c:if>
    doFilter();
}

var submitted = false;
function confirmDelete()
{
	if (submitted == true) {
		window.status = "Transaction already in progress.  Please wait.";
		return false;
	}	
		
	if (confirmDelete.arguments.length != 1) {
		return false;
	}

	var msg = 	"\nAre you sure you want to delete this "
				+ confirmDelete.arguments[0]
				+ " submission?";

	if (confirm(msg)) {
		submitted = true;
		return true;
	} else {	
		return false;
	}
}

function confirmAction()
{
	if (submitted == true) {
		window.status = "Transaction already in progress.  Please wait.";
		return false;
	} else {	
		submitted = true;
		return true;
	}
}

function sortSubmit(sortfield, sortDirection){

<% if (userProfile.getCurrentContract().isDefinedBenefitContract())  { %>	
    if (sortfield == "contribDate") {
      sortfield = '<%=SubmissionHistoryReportData.SORT_PAYROLL_DATE%>';
    }
<% } %>

	document.forms['viewDocumentUploadHistoryForm'].elements['task'].value = "sort";
	document.forms['viewDocumentUploadHistoryForm'].elements['sortField'].value = sortfield;
	document.forms['viewDocumentUploadHistoryForm'].elements['sortDirection'].value = sortDirection;
	document.forms['viewDocumentUploadHistoryForm'].submit();

}

function pagingSubmit(pageNumber){
	if (document.forms['viewDocumentUploadHistoryForm']) {

		document.forms['viewDocumentUploadHistoryForm'].elements['task'].value = "page";
		document.forms['viewDocumentUploadHistoryForm'].elements['pageNumber'].value = pageNumber;

		if (document.forms['viewDocumentUploadHistoryForm']) {
			document.forms['viewDocumentUploadHistoryForm'].submit();
		} else {
			document.forms.viewDocumentUploadHistoryForm.submit();
		}
	}
}

</SCRIPT>
<%
	String sCompanyName = userProfile.getCurrentContract().getCompanyName();
	String sContractNumber = ""+userProfile.getCurrentContract().getContractNumber();
	if (sContractNumber == null) sContractNumber = "";
	if (sCompanyName == null || sContractNumber.length() != 0) sCompanyName = "";
	String completeMessage = "false";
%>
<ps:map id="parameterMap">
	<ps:param name="contractNumber" value="<%=sContractNumber%>"/>
	<ps:param name="companyName" value="<%=sCompanyName%>"/>
</ps:map>

<c:if test="${empty param.printFriendly }" >
<style>
	#actions {width 34}
	#submissionNumber {width : 70}
	#submissionDate {width : 79}
	#type {width : 80}
	#payrollDate (width : 88 )
	#contributionTotal {width : 90}
	#paymentTotal (width : 100)
	#submitterName (width :100)
	#status (94)
</style>
</c:if>

<c:if test="${not empty param.printFriendly }" >
<style>
	#actions {width 1}
	#submissionNumber {width : 70}
	#submissionDate {width : 79}
	#type {width : 80}
	#payrollDate (width : 88 )
	#contributionTotal {width : 09}
	#paymentTotal (width : 100)
	#submitterName (width :100)
	#status (94)
</style>
</c:if>

<p>
	<content:errors scope="session" />
</p>

<ps:form style="margin-bottom:0;"
         method="POST"
         action="/do/tools/viewDocumentUploadHistory/" modelAttribute="viewDocumentUploadHistoryForm" name="viewDocumentUploadHistoryForm">
<!--------------  body  ----------------------->
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="pageNumber"/>
<input type="hidden" name="sortField"/>
<input type="hidden" name="sortDirection"/>


<c:if test="${empty param.printFriendly }" >
<table width="758" border="0" cellspacing="0" cellpadding="0">
</c:if>
<c:if test="${not empty param.printFriendly }" >
<table width="725" border="0" cellspacing="0" cellpadding="0">
</c:if>

	<tr>
		<td>
			<c:if test="${empty param.printFriendly }" >
			<c:if test="${not empty marketClose}">
<p><b>${marketClose}</b></p>
			</c:if>
			</c:if>
		</td>
		<td width="10"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<c:if test="${empty param.printFriendly }" >
		<td width="748" valign="top">
			<table width="748" border="0" cellspacing="0" cellpadding="0">
		</c:if>
		<c:if test="${not empty param.printFriendly }" >
		<td width="715" valign="top">
		<table width="715" border="0" cellspacing="0" cellpadding="0">

		</c:if>

		<tr>
			<c:if test="${empty param.printFriendly }" >
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="actions"><img src="/assets/unmanaged/images/s.gif" width="34" height="1"></td>
					<td class="submissionNumber"><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="submissionDate"><img src="/assets/unmanaged/images/s.gif" width="79" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="type"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="payrollDate"><img src="/assets/unmanaged/images/s.gif" width="88" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="contributionTotal"><img src="/assets/unmanaged/images/s.gif" width="90" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="paymentTotal"><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="submitterName"><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="status"><img src="/assets/unmanaged/images/s.gif" width="94" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			</c:if>

			<c:if test="${not empty param.printFriendly }" >
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="actions"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="submissionNumber"><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="submissionDate"><img src="/assets/unmanaged/images/s.gif" width="79" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="type"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="payrollDate"><img src="/assets/unmanaged/images/s.gif" width="88" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="contributionTotal"><img src="/assets/unmanaged/images/s.gif" width="90" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="paymentTotal"><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="submitterName"><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="status"><img src="/assets/unmanaged/images/s.gif" width="94" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			</c:if>


				</tr>
				<tr class="tablehead">
					<td colspan="14" valign="middle" class="tableheadTD1"  height="25">
						<b>
							 <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>
							 <span class="tableheadTD">
								 <strong><report:recordCounter report="theReport" totalOnly="true" label="Total Records:"/></strong>
							</span>
						</b>
					</td>
					<td colspan="4" align="right" class="tableheadTD">
						<span class="databorder">
							<report:pageCounterViaSubmit report="theReport" arrowColor="white" name="parameterMap" formName="viewDocumentUploadHistoryForm"/>
						</span>
					</td>
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>
				<c:if test="${empty param.printFriendly }" >
				<tr>
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
					<td height="51" colspan="6" class="datacell1">         &nbsp;<strong>Legend</strong>: &nbsp;View <img src="/assets/unmanaged/images/view_icon.gif" width="12" height="12" />           &nbsp;&nbsp;Edit <img src="/assets/unmanaged/images/edit_icon.gif" width="12" height="12" />&nbsp;&nbsp;Copy <img src="/assets/unmanaged/images/copy_icon.gif" width="12" height="12" />&nbsp;&nbsp;Delete <img src="/assets/unmanaged/images/delete_icon.gif" width="12" height="12" /><br />          </td>
					<td valign="bottom" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" border="0" /></td>
					<td height="51" colspan="10" class="datacell1">
						<table border="0" cellpadding="" cellspacing="4" bgcolor="#ffffff" class="datacell1">
<c:if test="${viewDocumentUploadHistoryForm.filterSimple ==true}">
							<tr>
								<td colspan="2">
									Show for
									<form:select path="filterShowDate" onchange="setFilterFromSelect(this);">
<form:options items="${datesCollection}"/>
</form:select>
								</td>
								<td>
									<logicext:if name="<%= Constants.SHOW_JUST_MINE_FILTER %>" op="equal" value="true">
										<logicext:then>
											Just mine&nbsp;
<form:checkbox path="justMine" value="true"/>
										</logicext:then>
										<logicext:else>
											&nbsp;
<input type="hidden" name="justMine" value="true"/>
										</logicext:else>
									</logicext:if>
								</td>
								<td>
<input type="button" onclick="javascript:submitform();" name="subbutt" class="button89x21">search</input>
								</td>
								<td>
									<a href="?filterSimple=false">Advanced Search</a><br>
<c:if test="${viewDocumentUploadHistoryForm.filterActive ==true}">
									<a href="?filterSimple=true">Clear Filters</a>
</c:if>
								</td>
							</tr>
</c:if>
<c:if test="${viewDocumentUploadHistoryForm.filterSimple !=true}">
							<tr>
								<td>
<form:radiobutton path="filterBySubmission" value="true">Submission Date</form:radiobutton>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract())  { %>	
<form:radiobutton path="filterBySubmission" value="false">Contribution Date</form:radiobutton>
<% } else { %>								
<form:radiobutton path="filterBySubmission" value="false">Payroll Date</form:radiobutton>
<% } %>									
								</td>
								<td align="right">
									Submission Type
								</td>
								<td colspan="2">
									<form:select path="filterType" onchange="setFilterFromSelect(this);">
<form:options items="${submissionTypes}"/>
</form:select>
								</td>
							</tr>
							<tr>
								<td align="right">
									From:
									<form:select path="filterStartDate" onchange="setFilterFromSelect(this);">
<form:options items="${fromMonthsCollection}"/>
</form:select>
								</td>
								<td align="right">
									Submission Status
								</td>
								<td colspan="2">
									<form:select path="filterStatus" onchange="setFilterFromSelect(this);">
<form:options items="${submissionStatuses}"/>
</form:select>
								</td>
							</tr>
							<tr>
								<td align="right">
									To:
									<form:select path="filterEndDate" onchange="setFilterFromSelect(this);">
<form:options items="${toMonthsCollection}"/>
</form:select>
								</td>
								<td>
									<logicext:if name="<%= Constants.SHOW_JUST_MINE_FILTER %>"  op="equal" value="true">
										<logicext:then>
											Just Mine&nbsp;
<form:checkbox path="justMine" value="true"/>
										</logicext:then>
										<logicext:else>
											&nbsp;
<input type="hidden" name="justMine" value="true"/>
										</logicext:else>
									</logicext:if>
								</td>
								<td>
<input type="button" onclick="javascript:submitform();" name="submitButton" class="button89x21">search</input>
								</td>
								<td>
									<a href="?filterSimple=true">Simple Search</a><br>
									<a href="?filterSimple=false">Clear Filters</a>
								</td>
							</tr>
</c:if>
						</table>
					</td>
					<td width="1" valign="top" class="databorder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1" border="0" />
					</td>
				</tr>
				</c:if>
				<tr class="tablesubhead">
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="actions"></td>
					<td valign="top" align="left" class="submissionNumber">
						<b>
							<report:sortLinkViaSubmit field="<%=SubmissionHistoryReportData.SORT_SUBMISSION_ID%>" direction="asc" formName="viewDocumentUploadHistoryForm">Submission<br/>number</report:sortLinkViaSubmit>
						</b>
					</td>
					<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td valign="top" align="left" class="submissionDate">
						<b>
							<report:sortLinkViaSubmit field="<%=SubmissionHistoryReportData.SORT_SUBMISSION_DATE%>" direction="desc" formName="viewDocumentUploadHistoryForm">Submission<br/>date/time</report:sortLinkViaSubmit>
						</b>
					</td>
					<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td valign="top" align="left" class="type">
						<b><report:sortLinkViaSubmit field="<%=SubmissionHistoryReportData.SORT_TYPE%>" direction="asc" formName="viewDocumentUploadHistoryForm">Submission<br/>type</report:sortLinkViaSubmit></b>
					</td>
					<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td valign="top" align="left" class="payrollDate">
						<b>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract())  { %>							
							<report:sortLinkViaSubmit field="contribDate" direction="desc">Contribution date</report:sortLinkViaSubmit>
<% } else { %>
							<report:sortLinkViaSubmit field="<%=SubmissionHistoryReportData.SORT_PAYROLL_DATE%>" direction="desc" formName="viewDocumentUploadHistoryForm">Payroll date</report:sortLinkViaSubmit>
<% } %>							
						</b>
					</td>
					<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td valign="top" align="right" class="contributionTotal">
						<b>Contribution<br/>total ($)</b>
					</td>
					<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td valign="top" align="right" class="paymentTotal">
						<b>Payment<br/>total ($)</b>
					</td>
					<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td valign="top" align="left" class="submitterName">
						<b>
							<report:sortLinkViaSubmit field="<%=SubmissionHistoryReportData.SORT_SUBMITTER_NAME%>" direction="asc" formName="viewDocumentUploadHistoryForm">Submitted by</report:sortLinkViaSubmit>
						</b>
					</td>
					<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td valign="top" colspan="2" align="left" class="status">
						<b><report:sortLinkViaSubmit field="<%=SubmissionHistoryReportData.SORT_USER_STATUS%>" direction="asc" formName="viewDocumentUploadHistoryForm">Status</report:sortLinkViaSubmit></b>
					</td>
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>

<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:set var="theIndex" value="${theIndex.index}"/>
<% String temp = pageContext.getAttribute("theIndex").toString(); %>
<%
SubmissionHistoryItem theItem=(SubmissionHistoryItem)pageContext.getAttribute("theItem");
pageContext.setAttribute("theItem",theItem,pageContext.PAGE_SCOPE);
%>



					<% if (Integer.parseInt(temp) % 2 == 0) { %>
				<tr class="datacell1">
					<% } else { %>
				<tr class="datacell2">
					<% } %>
					<%
						SubmissionHistoryItemActionHelper helper = SubmissionHistoryItemActionHelper.getInstance();
						boolean viewAllowed = helper.isViewAllowed(theItem,userProfile);
					%>
							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td align="center" class="actions">
								<c:if test="${empty param.printFriendly }" >
									<report:actions profile="userProfile" item="theItem" action="icons"/>
								</c:if>
							</td>
							<td align="left" nowrap="nowrap" class="submissionNumber">
								<%
									if (viewAllowed) {
								%>
								<c:if test="${empty param.printFriendly }" >
									<report:actions profile="userProfile" item="theItem" action="view"/>
								</c:if>
${theItem.submissionId}
								<c:if test="${empty param.printFriendly }" >
								</a>
								</c:if>
								<% } else { %>
${theItem.submissionId}
								<% } %>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td align="left" class="submissionDate">
								<% if (theItem.isDraft()) { %>
									not submitted
								<% } else if (theItem.isFileLoaderCase()) { %>
									<render:date property="theItem.submissionDate" patternOut="MM/dd/yyyy" defaultValue=""/>
								<% } else { %>
									<render:date property="theItem.submissionDate" patternOut="MM/dd/yyyy'<br>'hh:mm a" defaultValue=""/>
								<% } %>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td align="left" class="type">
								<%
									if ( helper != null ) {
								%>
									<%= helper.getDisplayType(theItem) %>
								<% } %>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td align="left" class="payrollDate">
<c:if test="${theItem.type !=A}">
<c:if test="${theItem.type !=M}">
<c:if test="${theItem.type !=E}">
											<% if ( ( "C".equals(theItem.getType()) || "X".equals(theItem.getType()) ) && theItem.getPayrollDate() == null ) { %>
												Not Available
											<% } else { %>
												<render:date property="theItem.payrollDate" patternOut="MM/dd/yyyy" defaultValue=""/>
											<% } %>
</c:if>
</c:if>
</c:if>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td align="right" valign="center" class="contributionTotal">
<c:if test="${theItem.type !=A}">
<c:if test="${theItem.type !=M}">
<c:if test="${theItem.type !=P}">
<c:if test="${theItem.type !=E}">
<c:if test="${theItem.type !=V}">
<c:if test="${theItem.type !=Z}">
												<% if (theItem.getContributionTotal() == null) { %>
													Not Available
												<% } else { %>
													<render:number type="c" property="theItem.contributionTotal"/>
												<% } %>
</c:if>
</c:if>
</c:if>
</c:if>
</c:if>
</c:if>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td align="right" valign="center" class="paymentTotal">
<c:if test="${theItem.type !=A}">
<c:if test="${theItem.type !=M}">
<c:if test="${theItem.type !=E}">
<c:if test="${theItem.type !=V}">
<c:if test="${theItem.type !=Z}">
											<%
												if (viewAllowed) {
											%>
												<c:if test="${empty param.printFriendly }" >
													<report:actions profile="userProfile" item="theItem" action="view"/>
												</c:if>
											<% } %>
											<% if (theItem.getPaymentTotal() != null) { %>
												<% if (theItem.isZeroContribution() || Constants.BIG_DECIMAL_ZERO.compareTo(theItem.getPaymentTotal()) == 0 ) { %>
													Not available
												<% } else { %>
													<render:number type="c" property="theItem.paymentTotal"/>
												<% } %>
											<% } %>
											<%
												if (viewAllowed) {
											%>
												<c:if test="${empty param.printFriendly }" >
												</a>
												</c:if>
											<% } %>
</c:if>
</c:if>
</c:if>
</c:if>
</c:if>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<% if (userProfile.isInternalUser()) { %>
								 <% if ((("I".equals(theItem.getSubmitterType()) && "FL".equals(theItem.getApplicationCode()))
									        || theItem.isInternalSubmitter())) { %>
<td align="left" class="submitterName" title='${theItem.submitterName}'> <%-- ignore="true" --%>&nbsp;&nbsp;&nbsp;">
								 <% } else { %>
<td align="left" class="submitterName" title='${theItem.submitterID}'> <%-- ignore="true" --%>&nbsp;&nbsp;&nbsp;">
								 <% } %>
							<% } else { %>
								<td align="left" class="submitterName">
							<% } %>
							<% if ((("I".equals(theItem.getSubmitterType()) && "FL".equals(theItem.getApplicationCode()))
									|| theItem.isInternalSubmitter())) { %>
								John Hancock Representative
							<% } else if ( ("I".equals(theItem.getSubmitterType())) && ("AL".equals(theItem.getApplicationCode()))) {	%>
								<%=Constants.AWAITING_PAYMENT_PAYROLL_COMPANY%>
							<% } else { %>
${theItem.submitterName} <%-- ignore="true" --%>
							<% } %>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<% if (userProfile.isInternalUser()) { %>
<td colspan="2" align="left" class="status" title='${theItem.systemStatus} <%-- ignore="true" --%> - <%=helper.getSystemStatusDescription(theItem)%>'>
						<% } else { %>
							<td colspan="2" align="left" class="status">
						<% } %>
						<%
							if ( helper != null ) {
								if (helper.getDisplayStatus(theItem).startsWith("Complete*")) {
									completeMessage = "true";
								}
						%>
								
	                        <!--to get Awaiting Payment -->
							<% if(theItem.getSystemStatus().equals(Constants.AWAITING_PAYMENT_STATUS)){%>
							<report:actions profile="userProfile" item="theItem" action="view"/>
							<%}%>
	                        
										<%= helper.getDisplayStatus(theItem) %>							
	
							<% if(theItem.getSystemStatus().equals(Constants.AWAITING_PAYMENT_STATUS)){%>
							</a>
							<%}%>

						<!--to get Awaiting Payment end-->			
						<% } %>
							</td>
							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>

</c:forEach>
</c:if>
<c:if test="${empty theReport.details}">
				<tr class="datacell1">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						  <td valign="top" colspan="17">
						  	<b><content:getAttribute id="NoResultsMessage" attribute="text"/></b>
						  </td>
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>
</c:if>
				<tr class="whiteborder">
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
					<td colspan="2" rowspan="2" class="whiteBox"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
				</tr>
				<tr>
					<td class="databorder" colspan="17"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>
				<tr>
					<td colspan="17" align="right">
						<report:pageCounterViaSubmit report="theReport" arrowColor="black" name="parameterMap" formName="viewDocumentUploadHistoryForm"/>
					</td>
				</tr>
			</table>
		</td>
		<td width="10"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
		<td width="190" align="center" valign="top">
			<img src="/assets/unmanaged/images/s.gif" width="1" height="25">
			<img src="/assets/unmanaged/images/s.gif" width="1" height="5">
			<content:rightHandLayerDisplay layerName="layer1" beanName="layoutPageBean" />
			<img src="/assets/unmanaged/images/s.gif" width="1" height="5">
		</td>
	</tr>

	<tr>
		<td>
<c:set var="showCompleteMessage" value="${completeMessage}"/>
			<c:if test="${showCompleteMessage==true}">
				<br>
				<content:getAttribute id="CompleteText" attribute="text"/>
</c:if>
		</td>
		<td width="10"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
		<td>&nbsp;</td>
	</tr>


</table>
</ps:form>

