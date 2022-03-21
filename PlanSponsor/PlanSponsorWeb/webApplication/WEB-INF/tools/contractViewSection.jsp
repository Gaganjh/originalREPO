<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.StatementPairVO" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.handler.MoneySourceDescription" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem"%>
<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<%-- copy the form from the request in to the page scope --%>
<jsp:useBean id="viewContributionDetailsForm" scope="session" type="com.manulife.pension.ps.web.tools.ViewContributionDetailsForm" />

<%
ContributionDetailsReportData theReport = (ContributionDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%> 

<c:set var="submissionItem" value="${theReport.contributionData}" />

<%
	SubmissionHistoryItemActionHelper helper = SubmissionHistoryItemActionHelper.getInstance();
	boolean viewAllowed = helper.isViewAllowed(theReport.getContributionData(),userProfile);
	ContributionDetailItem submissionItemObj = (ContributionDetailItem) pageContext.getAttribute("submissionItem");
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
${userProfile.currentContract.companyName} ${userProfile.currentContract.contractNumber}</b></td>
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
<td class="datacell1" colspan="7" title="${submissionItem.systemStatus}" <%-- ignore="true" --%> - <%=helper.getSystemStatusDescription(submissionItemObj)%>">
<% } else { %>
	<td class="datacell1" colspan="7">
<% } %>
		<b>Status&nbsp;</b>&nbsp;
		<strong class="highlight"><%= helper.getDisplayStatus(submissionItemObj) %></strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
<% if (!userProfile.isInternalUser()) { %>
	<td class="datacell1" colspan="7" class="submitterName">
<c:if test="${submissionItem.systemStatus ==14}">
		<b>Saved by&nbsp;</b>&nbsp;
</c:if>
<c:if test="${submissionItem.systemStatus !=14}">
		<b>Submitted by&nbsp;</b>&nbsp;
</c:if>
<c:if test="${submissionItem.internalSubmitter !=true}">
		<strong class="highlight">
		<% if ( ("I".equals(submissionItemObj.getSubmitterType())) && ("AL".equals(submissionItemObj.getApplicationCode()))) {	%>
		  	<%=Constants.AWAITING_PAYMENT_PAYROLL_COMPANY%>
	    <% } else { %>
${submissionItem.submitterName} <%-- ignore="true" --%>
	    <% } %>

		</strong>
</c:if>
<c:if test="${submissionItem.internalSubmitter ==true}">
		<strong class="highlight">John Hancock Representative</strong>
</c:if>
<% } else { %>
<c:if test="${submissionItem.internalSubmitter !=true}">
<td class="datacell1" colspan="7" class="submitterName" title="${submissionItem.submitterID}" <%-- ignore="true" --%>">
<c:if test="${submissionItem.systemStatus ==14}">
		  <b>Saved by&nbsp;</b>&nbsp;
</c:if>
<c:if test="${submissionItem.systemStatus !=14}">
		  <b>Submitted by&nbsp;</b>&nbsp;
</c:if>
	  <strong class="highlight">
	  <% if ( ("I".equals(submissionItemObj.getSubmitterType())) && ("AL".equals(submissionItemObj.getApplicationCode()))) {	%>
		  <%=Constants.AWAITING_PAYMENT_PAYROLL_COMPANY%>
	  <% } else { %>
${submissionItem.submitterName} <%-- ignore="true" --%>
	  <% } %>
	  </strong>
</c:if>
<c:if test="${submissionItem.internalSubmitter ==true}">
<td class="datacell1" colspan="7" class="submitterName" title="${submissionItem.submitterName}">
<c:if test="${submissionItem.systemStatus ==14}">
	  	  <b>Saved by&nbsp;</b>&nbsp;
</c:if>
<c:if test="${submissionItem.systemStatus !=14}">
		  <b>Submitted by&nbsp;</b>&nbsp;
</c:if>
	  <strong class="highlight">John Hancock Representative</strong>
</c:if>
	</td>
<% } %>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>

</tr>

<c:if test="${submissionItem.systemStatus !=14}">

<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1" colspan="7" >
	<b>Submitter Email</b>&nbsp;
	<strong class="highlight">
	<% if ( submissionItemObj.getSubmitterEmail() != null ) {%>
${submissionItem.submitterEmail} <%-- ignore="true" --%>
	<%}else{%>
	Not provided
	<%}%>
	</strong></td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td> 
</tr>

</c:if>

<% if ( submissionItemObj.isLocked() ) {%>
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1" colspan="7" class="submitterName">
		<b>In Use by &nbsp;</b>&nbsp;
<% if (userProfile.isInternalUser()) { %>
<c:if test="${submissionItem.lockedByInternalUser !=true}">
<span title="${submissionItem.lock.userId}"><strong class="highlight">${submissionItem.lock.userName} <%-- ignore="true" --%></strong></span>
</c:if>
<c:if test="${submissionItem.lockedByInternalUser ==true}">
<span title="${submissionItemObj.lock.userName}"><strong class="highlight">John Hancock Representative</strong>
</c:if>
<% } else { %>
<c:if test="${submissionItem.lockedByInternalUser !=true}">
<strong class="highlight">${submissionItem.lock.userName} <%-- ignore="true" --%></strong>
</c:if>
<c:if test="${submissionItem.lockedByInternalUser ==true}">
		<strong class="highlight">John Hancock Representative</strong>
</c:if>
<% } %>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<% } %>
<%---- middle of first section  ---%>
<tr>
  <td class="databorder" colspan="9"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<%---- middle of first section  ---%>

<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()) { %>	
	<td class="datacell1" valign="bottom">
		<b>Employer contribution</b>
	</td>
	<td class="datacell1" valign="bottom">
		<strong class="highlight"><render:number property="submissionItem.employerContributionTotal" type="c"/></strong>
	</td>    
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1">
		<b>Contribution date</b>
	</td>	
<% } else { %>
	<td class="datacell1" valign="bottom">
		<b>Number of participants</b>
	</td>
	<td class="datacell1" valign="bottom">
		<report:errorIcon errors="<%=theReport%>" codes="NE"/>
<strong class="highlight">${submissionItem.numberOfParticipants}</strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1">
		<b>Payroll date</b>
	</td>
<% } %>	
	<td class="datacell1" colspan="3">
		<report:errorIcon errors="<%=theReport%>" codes="ED,FP,MD,PD,MP,MU"/>
		<strong class="highlight">
<c:if test="${empty submissionItem.payrollDate}">
		         Not Available
</c:if>
<c:if test="${not empty submissionItem.payrollDate}">
		         <render:date property="submissionItem.payrollDate" patternOut="MMMM dd, yyyy" defaultValue=""/>
</c:if>
		</strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>

<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()) { %>		
	<td class="datacell1" valign="bottom">
		<b>Total contributions
<c:if test="${viewContributionDetailsForm.showLoans ==true}">
			and loans
</c:if>
		</b>
	</td>
	<td class="datacell1" valign="bottom">
		<report:errorIcon errors="<%=theReport%>" codes="LA,NG,ZA"/>
		<strong class="highlight"><render:number property="submissionItem.submissionTotal" type="c"/></strong>
	</td>    
<% } else { %>
	<td class="datacell1" valign="bottom">
		<b>Employee contribution</b>
	</td>
	<td class="datacell1"  valign="bottom">
		<strong class="highlight"><render:number property="submissionItem.employeeContributionTotal" type="c"/></strong>
	</td>
<% } %>		
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1">
		<b>Submission Type</b>
	</td>
	<td class="datacell1" colspan="3">
		<report:errorIcon errors="<%=theReport%>" codes="MS"/>
		<% String moneySourceDescription = (String) submissionItemObj.getContractMoneySources().get(submissionItemObj.getMoneySourceID());
		   if (null == moneySourceDescription) {
		       moneySourceDescription = (String) MoneySourceDescription.getViewDescription(submissionItemObj.getMoneySourceID());
		   }
		   if (null == moneySourceDescription) {
				moneySourceDescription = submissionItemObj.getMoneySourceID();
		   }
		%>
		<strong class="highlight">
			<%=moneySourceDescription%>
		</strong>
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
		<strong class="highlight"><render:number property="submissionItem.employerContributionTotal" type="c"/></strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1" colspan="4">

<c:if test="${theReport.contributionData.zeroContributionFile !=true}">
<c:if test="${submissionItem.generateStatementsIndicator ==true}">
<c:if test="${not empty viewContributionDetailsForm.statementDates}">
			<table align="center">
				<tr>
					<td colspan="2" align="left"><b>Participant Statements</b></td>
				</tr>
				<tr>
					<td colspan="2" align="left">The following statements have not been created for your participants.</td>
				</tr>

<c:forEach items="${viewContributionDetailsForm.statementDates}" var="item" varStatus="theIndex" >
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
</c:if>
			<br>
<c:if test="${not empty submissionItem.generateStatementsIndicator}">
			<span class="content">
				<b>Would you like to generate these statements?&nbsp;&nbsp;</b>
				<strong class="highlight">
<c:if test="${submissionItem.generateStatementsIndicator ==true}">
					Yes
</c:if>
<c:if test="${submissionItem.generateStatementsIndicator !=true}">
					No
</c:if>
				</strong>
			</span>
</c:if>
</c:if>
		<br>

			</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
<% } %>

<c:if test="${viewContributionDetailsForm.showLoans ==true}">
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1"  valign="bottom">
		<b>Loan repayments</b>
	</td>
	<td class="datacell1" valign="bottom">
		<report:errorIcon errors="<%=theReport%>" codes="LF"/>
		<strong class="highlight"><render:number property="submissionItem.loanRepaymentTotal" type="c"/></strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1">
		&nbsp;
	</td>
	<td class="datacell1">
		&nbsp;
	</td>
	<td class="datacell1" colspan="2">
		&nbsp;
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</tr>
</c:if>

<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>		
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1" valign="bottom">
		<b>Total contributions
<c:if test="${viewContributionDetailsForm.showLoans ==true}">
			and loans
</c:if>
		</b>
	</td>
	<td class="datacell1" valign="bottom">
		<report:errorIcon errors="<%=theReport%>" codes="LA,NG,ZA"/>
		<strong class="highlight"><render:number property="submissionItem.submissionTotal" type="c"/></strong>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	<td class="datacell1">
		&nbsp;
	</td>
	<td class="datacell1">
		&nbsp;
	</td>
	<td class="datacell1" colspan="2">
		&nbsp;
	</td>
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
