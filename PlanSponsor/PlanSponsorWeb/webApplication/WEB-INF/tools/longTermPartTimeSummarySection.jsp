<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper"%>
<%@ page import="com.manulife.pension.service.contract.valueobject.StatementPairVO"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.LongTermPartTimeDetailsReportData"%>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.LongTermPartTimeDetailItem"%>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem"%>

<%
	boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;
%>
<%
	LongTermPartTimeDetailsReportData theReport = (LongTermPartTimeDetailsReportData) request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport", theReport, PageContext.PAGE_SCOPE);
	UserProfile userProfile = (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile", userProfile, PageContext.PAGE_SCOPE);
%>

<c:set var="submissionItem" value="${theReport.longTermPartTimeData}" />

<%
	SubmissionHistoryItem submissionItem = (SubmissionHistoryItem) pageContext.getAttribute("submissionItem");
	SubmissionHistoryItemActionHelper helper = SubmissionHistoryItemActionHelper.getInstance();
	boolean viewAllowed = helper.isViewAllowed(submissionItem, userProfile);
%>

<table width="350" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<td><img src="/assets/unmanaged/images/s.gif" width="149" height="1" /></td>
		<td><img src="/assets/unmanaged/images/s.gif" width="195" height="1" /></td>
		<td><img src="/assets/unmanaged/images/s.gif" width="4"	height="1" /></td>
		<td><img src="/assets/unmanaged/images/s.gif" width="1"	height="1" /></td>
	</tr>
	<tr class="tablehead" height="25">
		<td class="tableheadTD1" colspan="5">
			<c:if test="${ not empty layoutPageBean.body1Header}">
				<b><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></b>
			</c:if>
		</td>
	</tr>
	<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<td class="datacell1"><b>Contract:</b></td>
		<td class="highlight" colspan="2"><strong>${userProfile.currentContract.companyName}&nbsp;
				${userProfile.currentContract.contractNumber}</strong></td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	</tr>
	<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<td class="datacell1"><b>Submission Number:</b></td>
		<td class="highlight" colspan="2"><strong>${submissionItem.submissionId}</strong></td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	</tr>
	<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<%
			if (userProfile.isInternalUser()) {
		%>
		<td class="datacell1"
			title="${submissionItem.systemStatus} <%-- ignore="true" --%> - <%=helper.getSystemStatusDescription(submissionItem)%>">
			<%
				} else {
			%>
		<td class="datacell1">
			<%
				}
			%> <b>Status:</b>
		</td>
		<td class="highlight" colspan="2"><strong><%=helper.getDisplayStatus(submissionItem)%></strong></td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	</tr>
	<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<td class="datacell1" class="submitterName"><b>Submitted by:</b></td>
		<%
			if (!userProfile.isInternalUser()) {
		%>
		<td class="highlight" colspan="2"><strong>
			<c:if test="${submissionItem.internalSubmitter !=true}">
				${submissionItem.submitterName} <%-- ignore="true" --%>
			</c:if>
			<c:if test="${submissionItem.internalSubmitter ==true}">
				John Hancock Representative
			</c:if>
		</strong></td>
		<%
			} else {
		%>
		<c:if test="${submissionItem.internalSubmitter !=true}">
			<td class="highlight" colspan="2" title="${submissionItem.submitterID} <%-- ignore="true" --%>"><strong>
					${submissionItem.submitterName} <%-- ignore="true" --%>
			</strong></td>
		</c:if>
		<c:if test="${submissionItem.internalSubmitter ==true}">
			<td class="highlight" colspan="2" title="${submissionItem.submitterName} <%-- ignore="true" --%>"><strong>
					John Hancock Representative</strong></td>
		</c:if>
		<%
			}
		%>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>

	</tr>

	<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<td class="datacell1" valign="bottom"><b>Submitter Email:</b></td>
		<td class="highlight" valign="bottom" colspan="2">
			<c:if test="${empty submissionItem.submitterEmail}">
				<strong>Not available</strong>
			</c:if>
			<c:if test="${not empty submissionItem.submitterEmail}">
				<strong>${submissionItem.submitterEmail}</strong>
			</c:if></td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	</tr>

	<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<td class="datacell1" valign="bottom"><b>Number of records:</b></td>
		<td class="highlight" valign="bottom" colspan="2"><strong>${submissionItem.numberOfRecords}</strong></td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	</tr>

	<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<td class="datacell1" valign="bottom"><b>Submission date:</b></td>
		<td class="highlight" valign="bottom" colspan="2">
			<strong><render:date property="submissionItem.submissionDate" patternOut="MM/dd/yyyy hh:mm a" defaultValue="" /></strong>
		</td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	</tr>

	<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<td class="lastrow" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	</tr>
	<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
		<td class="lastrow" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		<td colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow">
			<img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" />
		</td>
	</tr>
	<tr>
		<td class="databorder" colspan="3" valign="bottom">
			<img src="/assets/unmanaged/images/s.gif" width="1" height="1" />
		</td>
	</tr>
</table>