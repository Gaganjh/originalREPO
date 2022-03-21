<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.StatementPairVO" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem" %>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData" %>
<%@ page import="com.manulife.pension.ps.web.tools.EditContributionDetailsForm" %>
<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<%-- copy the form from in to the page scope --%>

<c:set var="theReport" value="${editContributionDetailsForm.theReport}" />
<%
EditContributionDetailsForm editContributionDetailsForm = (EditContributionDetailsForm)request.getAttribute("requestForm");
pageContext.setAttribute("editContributionDetailsForm",editContributionDetailsForm,PageContext.PAGE_SCOPE);


ContributionDetailsReportData theReport = (ContributionDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
ContributionDetailItem submissionItem = theReport.getContributionData();
pageContext.setAttribute("submissionItem",submissionItem,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
SubmissionHistoryItemActionHelper helper = SubmissionHistoryItemActionHelper.getInstance();
boolean viewAllowed = helper.isViewAllowed(submissionItem,userProfile);

%>
<table width="500" border="0" cellpadding="0" cellspacing="0">
<tr>
  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
  <td><img src="/assets/unmanaged/images/s.gif" width="99" height="1" /></td>
  <td><img src="/assets/unmanaged/images/s.gif" width="99" height="1" /></td>
  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
  <td><img src="/assets/unmanaged/images/s.gif" width="99" height="1" /></td>
  <td><img src="/assets/unmanaged/images/s.gif" width="98" height="1" /></td>
  <td><img src="/assets/unmanaged/images/s.gif" width="98" height="1" /></td>
  <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<tr class="tablehead" height="25">
  <td class="tableheadTD1" colspan="9"><report:errorIcon errors="<%=theReport%>" codes="DI,CF,NC,OT,JC,SO,KC"/><b>
  <c:if test="${ not empty layoutPageBean.body1Header}">
  	<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> -
</c:if>
${userProfile.currentContract.contractNumber} ${userProfile.currentContract.companyName}</b></td>
</tr>
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1" colspan="7" >
		<b>Submission Number&nbsp;</b>&nbsp;<report:errorIcon errors="<%=theReport%>" codes="JT"/>
<strong class="highlight">${submissionItem.submissionId}</strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
<% if (userProfile.isInternalUser()) { %>
<td class="datacell1" colspan="7" title="${submissionItem.systemStatus}" <%=helper.getSystemStatusDescription(submissionItem)%>">
<% } else { %>
	<td class="datacell1" colspan="7">
<% } %>
		<b>Status&nbsp;</b>&nbsp;
		<strong class="highlight"><%= helper.getDisplayStatus(submissionItem) %></strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1" colspan="7" class="submitterName">
<c:if test="${submissionItem.systemStatus ==14}">
		<b>Saved by&nbsp;</b>&nbsp;
</c:if>
<c:if test="${submissionItem.systemStatus !=14}">
<c:if test="${submissionItem.systemStatus !=97}">
			<b>Submitted by&nbsp;</b>&nbsp;
</c:if>
</c:if>
<c:if test="${submissionItem.systemStatus !=97}">
		<% if (userProfile.isInternalUser()) { %>
<c:if test="${submissionItem.internalSubmitter !=true}">
<strong class="highlight" title="${submissionItem.submitterID}">
				<% if ( ("I".equals(submissionItem.getSubmitterType())) && ("AL".equals(submissionItem.getApplicationCode()))) {	%>
				  	<%=Constants.AWAITING_PAYMENT_PAYROLL_COMPANY%>
			    <% } else { %>
${submissionItem.submitterName} <%-- ignore="true" --%>
			    <% } %>
 			    </strong>
</c:if>
<c:if test="${submissionItem.internalSubmitter ==true}">
<strong class="highlight" title="${submissionItem.submitterName}" >
 			    John Hancock Representative
 			    </strong>
</c:if>
		<% } else { %>
			<strong class="highlight">
<c:if test="${submissionItem.internalSubmitter !=true}">
				<% if ( ("I".equals(submissionItem.getSubmitterType())) && ("AL".equals(submissionItem.getApplicationCode()))) {	%>
				  	<%=Constants.AWAITING_PAYMENT_PAYROLL_COMPANY%>
			    <% } else { %>
${submissionItem.submitterName} <%-- ignore="true" --%>
			    <% } %>
</c:if>
<c:if test="${submissionItem.internalSubmitter ==true}">
				John Hancock Representative
</c:if>
			</strong>
		<% } %>
</c:if>

	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>

<%---- middle of first section  ---%>
<tr>
  <td class="databorder" colspan="9"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<%---- middle of first section  ---%>

<tr class="datacell1" valign="center">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()) { %>		
	<td class="datacell1" valign="bottom">
		<b>Employer contribution</b>
	</td>
	<td class="datacell1" valign="bottom">
		<strong class="highlight" id="erParticipantTotal"><render:number property="submissionItem.employerContributionTotal" type="c"/></strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1">
		<b>Contribution date</b>
	</td>	
<% } else { %>
	<td class="datacell1">
		<b>Number of participants</b>
	</td>
	<td class="datacell1">
		<report:errorIcon errors="<%=theReport%>" codes="NE"/>
<strong class="highlight" id="numberOfParticipants">${submissionItem.numberOfParticipants}</strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1">
		<b>Payroll date</b>
	</td>
<% } %>
	<td class="datacell1" colspan="3">
		<report:errorIcon errors="<%=theReport%>" codes="ED,FP,MD,PD,MP,MU"/>
		<strong class="highlight">


			<logicext:if name="contract" property="status" op="equal" value="CA">
			<logicext:and name="userProfile" property="internalUser" op="equal" value="false"/>
				<logicext:then>
<form:input path="payrollEffectiveDate" disabled="true" size="9"/>
				</logicext:then>
				<logicext:else>
<form:input path="payrollEffectiveDate" onchange="javascript:validatePayrollDateSelection();" size="9"/>
					<a href="javascript:payrollCal.popup();"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>
				</logicext:else>
			</logicext:if>
		</strong>
		&nbsp;(mm/dd/yyyy)
		<ps:trackChanges name="editContributionDetailsForm" property="payrollEffectiveDate"/>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>

<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()) { %>		
	<td class="datacell1"  valign="bottom">
		<b>Total contributions
<c:if test="${form.showLoans ==true}">
			and loans
</c:if>
		</b>
	</td>
	<td class="datacell1" valign="bottom">
		<report:errorIcon errors="<%=theReport%>" codes="LA,NG,ZA"/>
		<strong class="highlight" id="totalContributionsAndLoans"><render:number property="submissionItem.submissionTotal" type="c"/></strong>
	</td>
<% } else { %>	
	<td class="datacell1">
		<b>Employee contribution</b>
	</td>
	<td class="datacell1" valign="bottom">
		<strong class="highlight" id="eeParticipantTotal"><render:number property="submissionItem.employeeContributionTotal" type="c"/></strong>
	</td>
<% } %>	
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1">
		<b>Submission Type</b>
	</td>
	<td class="datacell1" colspan="3">
		<report:errorIcon errors="<%=theReport%>" codes="MS"/>
		<form:select path="moneySourceID" onchange="changingMoneySource(this);populateParticipantTotals();">
<form:options items="${editContributionDetailsForm.contractMoneySources}" itemLabel="label" itemValue="value"/>
</form:select>
		<c:if test="${trackChanges == true}">
			<ps:trackChanges name="editContributionDetailsForm" property="moneySourceID"/>
</c:if>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>

<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>		
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1" valign="bottom">
		<b>Employer contribution</b>
	</td>
	<td class="datacell1" valign="bottom">
		<strong class="highlight" id="erParticipantTotal"><render:number property="submissionItem.employerContributionTotal" type="c"/></strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1" colspan="4" rowspan="<c:if test="${editContributionDetailsForm.showLoans == true}">3</c:if><c:if test="${editContributionDetailsForm.showLoans != true}">2</c:if>">
<c:if test="${editContributionDetailsForm.displayGenerateStatementSection ==true}">
<c:if test="${not empty editContributionDetailsForm.statementDates}">
		<table align="center">
			<tr>
				<td colspan="2" align="left"><b>Participant Statements</b></td>
			</tr>
			<tr>
				<td colspan="2" align="left">Statements for the following quarters have not been created for participants.</td>
			</tr>

<c:forEach items="${editContributionDetailsForm.statementDates}" var="item" varStatus="theIndex" >
				<tr>
					<td colspan="2" align="left">&nbsp;
						<span class="highlightBold"><render:date property="item.startDate" patternOut="MMM-d-yyyy" defaultValue=""/> to
						<render:date property="item.endDate" patternOut="MMM-d-yyyy" defaultValue=""/>
						</span>
					</td>
				</tr>
</c:forEach>
		</table>
</c:if>

		<span class="content"><b style="color:blue">Is this your last contribution for the above quarter?</b><br><br>
			<table>
				<tr>
<td><form:radiobutton path="lastPayroll" value="C"/>Yes&nbsp;&nbsp;&nbsp;</td>
				<td><b>If "Yes", statements will run after this allocation is complete.</b></td>
				</tr>
				<tr><td><br></td></tr>
				<tr>
<td><form:radiobutton path="lastPayroll" value="S"/>No&nbsp;&nbsp;&nbsp;</td>
				<td><b>If "No", we will continue to wait for your last contribution for the quarter.</b></td>
				</tr>
			</table>
		</span>
			<ps:trackChanges name="editContributionDetailsForm" property="lastPayroll"/>
			<br>
</c:if>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<c:if test="${editContributionDetailsForm.showLoans ==true}">
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1"  valign="bottom">
		<b>Loan repayments</b>
	</td>
	<td class="datacell1" valign="bottom">
		<report:errorIcon errors="<%=theReport%>" codes="LF"/>
		<strong class="highlight" id="loanParticipantTotal"><render:number property="submissionItem.loanRepaymentTotal" type="c"/></strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
</c:if>
<% } %>

<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>		
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1"  valign="bottom">
		<b>Total contributions
<c:if test="${editContributionDetailsForm.showLoans ==true}">
			and loans
</c:if>
		</b>
	</td>
	<td class="datacell1" valign="bottom">
		<report:errorIcon errors="<%=theReport%>" codes="LA,NG,ZA"/>
		<strong class="highlight" id="totalContributionsAndLoans"><render:number property="submissionItem.submissionTotal" type="c"/></strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<% } %>

<tr class="datacell1">
  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
  <td class="lastrow" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
  <td class="lastrow" colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<tr class="datacell1">
  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
  <td class="lastrow" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
  <td class="lastrow" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
  <td  colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
</tr>
<tr>
  <td class="databorder" colspan="7"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
</table>

