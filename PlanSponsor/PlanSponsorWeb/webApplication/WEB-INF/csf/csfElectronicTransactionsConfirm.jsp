<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfForm"%>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfConstants" %>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<c:set var="checkPayableToCode" value="${userProfile.currentContract.checkPayableToCode}"/>
<c:set var="businessConverted" value="${userProfile.currentContract.businessConverted}"/>
<c:set var="definedBenefit" value="${userProfile.currentContract.definedBenefitContract}"/>

<script language="javascript">
function openLoanFeatures() {
 	PDFWindow('/do/onlineloans/features/?task=print&printFriendly=true');
}
</script>

<table border="0" cellpadding="0" cellspacing="0" width="698">
	<tbody>
		<tr class="tablesubhead">
			<td height="10" colspan="5" class="tablesubhead"><b> <content:getAttribute
				beanName="electronicTransactionsSubSectionTitle" attribute="text" /> </b></td>
		</tr>
		<% int theIndex =0; %>
		<%theIndex++; if (theIndex % 2 == 0) { %>
		<tr class="datacell2">
        <% } else { %>
        <tr class="datacell1">
  		 	<% } %>
			<td width="375"><content:getAttribute beanName="payrollFrequencyLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="planFrequency" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
			<td colspan="2">&nbsp;
				<form:select path="planFrequency" disabled="true">
					<form:option value="<%= CsfConstants.PAYROLL_FREQUENCY_UNSPECIFIED_CODE %>"><%= CsfConstants.PAYROLL_FREQUENCY_UNSPECIFIED %></form:option>
					<form:option value="<%= CsfConstants.PAYROLL_FREQUENCY_WEEKLY_CODE %>"><%= CsfConstants.PAYROLL_FREQUENCY_WEEKLY %></form:option>
					<form:option value="<%= CsfConstants.PAYROLL_FREQUENCY_BI_WEEKLY_CODE %>"><%= CsfConstants.PAYROLL_FREQUENCY_BI_WEEKLY %></form:option>
					<form:option value="<%= CsfConstants.PAYROLL_FREQUENCY_SEMI_MONTHLY_CODE %>"><%= CsfConstants.PAYROLL_FREQUENCY_SEMI_MONTHLY %></form:option>
					<form:option value="<%= CsfConstants.PAYROLL_FREQUENCY_MONTHLY_CODE %>"><%= CsfConstants.PAYROLL_FREQUENCY_MONTHLY %></form:option>
</form:select>
			</td>
		</tr>
		
		<%theIndex++; if (theIndex % 2 == 0) { %>
			<tr class="datacell2">
	        <% } else { %>
	        <tr class="datacell1">
       		 <% } %>
			<td width="375"><content:getAttribute beanName="payrollPathLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="autoPayrollInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
			<td width="55">
<form:radiobutton disabled="true" path="autoPayrollInd" value="Yes" />Yes</td>
			<td width="247">
<form:radiobutton disabled="true" path="autoPayrollInd" value="No" />No</td>
		</tr>
		
<!--  Payroll Feedback Service -->
<%theIndex++;%>
<tr class="<%=theIndex % 2 == 0?"datacell2":"datacell1"%>">
	<td><content:getAttribute beanName="payrollFeedbackServiceLabel" attribute="text" /></td>
	<td width="20" align="right"><ps:fieldHilight name="payrollFeedbackService" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
	<td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	<td colspan="2">
		&nbsp;
		<form:select path="payrollFeedbackService" id="payrollFeedbackService" disabled="true">
			<form:option value="<%= CsfConstants.PAYROLL_FEEDBACK_SERVICE_NOT_AVAILABLE_OPTION_CODE %>">	<content:getAttribute beanName="payrollFeedbackServiceNotAvailableOptionText"	attribute="text" /></form:option>
			<form:option value="<%= CsfConstants.PAYROLL_FEEDBACK_SERVICE_ENABLED_OPTION_CODE %>">			<content:getAttribute beanName="payrollFeedbackServiceEnabledOptionText"		attribute="text" /></form:option>
			<form:option value="<%= CsfConstants.PAYROLL_FEEDBACK_360_SERVICE_ENABLED_OPTION_CODE %>">		<content:getAttribute beanName="payrollFeedback360ServiceEnabledOptionText"		attribute="text" /></form:option>
		</form:select>
	</td>
	<ps:trackChanges name="csfForm" property="payrollFeedbackService" />
</tr>
		
		
<c:if test="${csfForm.isHideConsent ==false}">
				<%theIndex++; if (theIndex % 2 == 0) { %>
					<tr class="datacell2">
			        <% } else { %>
			        <tr class="datacell1">
		        <% } %>
				<td width="375"><content:getAttribute beanName="concentSubsequentLabel" attribute="text" /></td>
				<td width="20" align="right"><ps:fieldHilight name="consentInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				<td width="302" colspan="2">
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td width="55">
<form:radiobutton disabled="true" path="consentInd" value="Yes" />Yes</td>
						<td width="55">
<form:radiobutton disabled="true" path="consentInd" value="No" />No</td>
						<td width="192">
<form:radiobutton disabled="true" path="consentInd" value="NA" />N/A</td>
					</tr>
					</table>
				</td>
				</tr>
</c:if>
<c:if test="${csfForm.loanRecordKeepingInd == 'Yes'}">
				<%theIndex++; if (theIndex % 2 == 0) { %>
						<tr class="datacell2">
				        <% } else { %>
				        <tr class="datacell1">
		        <% } %>
				<td width="375"><content:getAttribute	beanName="onlineLoansLabel" attribute="text" /></td>
				<td width="20" align="right"><ps:fieldHilight name="allowOnlineLoans" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				<td width="55">
<form:radiobutton disabled="true" path="allowOnlineLoans" value="Yes" />Yes
				</td>
				<td width="247">
<form:radiobutton disabled="true" path="allowOnlineLoans" value="No" />No
				</td>
			</tr>
				<% if (theIndex % 2 == 0) { %>
						<tr class="datacell2 loans" id="loansChecksMailedToId" >
				        <% } else { %>
				        <tr class="datacell1 loans" id="loansChecksMailedToId">
			     <% } %>
				<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
					beanName="loanChecksMailedLabel" attribute="text" /> </td>
				<td width="20" align="right"><ps:fieldHilight name="loansChecksMailedTo" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				<td width="302" colspan="2">&nbsp;&nbsp;
<c:if test="${csfForm.loansChecksMailedTo == 'PY'}">Payee</c:if>
<c:if test="${csfForm.loansChecksMailedTo == 'TP'}">TPA</c:if>
<c:if test="${csfForm.loansChecksMailedTo == 'CL'}">Client</c:if>
<c:if test="${csfForm.loansChecksMailedTo == 'TR'}">Trustee</c:if>
				</td>
			</tr>
			<% if (theIndex % 2 == 0) { %>
					<tr class="datacell2 loans" id="whoWillReviewLoanRequestsId" >
			        <% } else { %>
			        <tr class="datacell1 loans" id="whoWillReviewLoanRequestsId">
			     <% } %>
				<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
					beanName="loanRequestReviwerLabel" attribute="text" /> </td>
				<td width="20" align="right">	<ps:fieldHilight name="whoWillReviewLoanRequests" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
<c:if test="${csfForm.tpaFirmExists !=true}">
					<td width="302" colspan="2">&nbsp;&nbsp;
<c:if test="${csfForm.whoWillReviewLoanRequests =='TPA'}">TPA</c:if>
<c:if test="${csfForm.whoWillReviewLoanRequests =='PS'}">Plan Sponsor</c:if>
					</td>
</c:if>
<c:if test="${csfForm.tpaFirmExists ==true}">
					<td width="55">
<form:radiobutton disabled="true" path="whoWillReviewLoanRequests" value="TPA" />TPA</td>
					<td width="247">
<form:radiobutton disabled="true" path="whoWillReviewLoanRequests" value="PS" />Plan Sponsor</td>
</c:if>
			</tr>
			<% if (theIndex % 2 == 0) { %>
					<tr class="datacell2 loans" id="creatorMayApproveLoanRequestsIndId" >
			        <% } else { %>
			        <tr class="datacell1 loans" id="creatorMayApproveLoanRequestsIndId">
			     <% } %>
				<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
					beanName="loanRequestApprovalLabel" attribute="text" /> </td>
				<td width="20" align="right">	<ps:fieldHilight name="creatorMayApproveLoanRequestsInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				<td width="55">
<form:radiobutton disabled="true" path="creatorMayApproveLoanRequestsInd" value="Yes" />Yes</td>
				<td width="247">
<form:radiobutton disabled="true" path="creatorMayApproveLoanRequestsInd" value="No" />No</td>
			</tr>
			<% if (theIndex % 2 == 0) { %>
						<tr class="datacell2 loans" id="allowLoansPackageToGenerateId" >
				        <% } else { %>
				        <tr class="datacell1 loans" id="allowLoansPackageToGenerateId">
			<% } %>
				<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
					beanName="loanPackagesGeneratedLabel" attribute="text" /> </td>
				<td width="20" align="right"><ps:fieldHilight name="allowLoansPackageToGenerate" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				<td width="55">
<form:radiobutton disabled="true" path="allowLoansPackageToGenerate" value="Yes" />Yes</td>
				<td width="247">
<form:radiobutton disabled="true" path="allowLoansPackageToGenerate" value="No" />No</td>
			</tr>
</c:if>
		<c:if test="${definedBenefit == false}">
			
			<%theIndex++; if (theIndex % 2 == 0) { %>
				<tr class="datacell2">
		        <% } else { %>
		        <tr class="datacell1">
	        <% } %>
				<td width="375"><content:getAttribute beanName="iwithdrawalsLabel" attribute="text" /></td>
				<td width="20" align="right"><ps:fieldHilight name="withdrawalInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
<td width="302" colspan="2">&nbsp;&nbsp;${csfForm.withdrawalInd}</td>

			</tr>
			<% if (theIndex % 2 == 0) { %>
				<tr class="datacell2 withdrawals" id="specialTaxNoticeId">
				<% } else { %>
				<tr class="datacell1 withdrawals" id="specialTaxNoticeId">
			<% } %>
				<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<content:getAttribute beanName="jhIrsTaxLabel" attribute="text" /> </td>
				<td align="right" width="20"><ps:fieldHilight name="specialTaxNotice" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				<td width="55"> 
<form:radiobutton disabled="true" path="specialTaxNotice" value="Yes" />Yes</td>
				<td width="247">
<form:radiobutton disabled="true" path="specialTaxNotice" value="No" />No</td>
			</tr>
			
			<% if (theIndex % 2 == 0) { %>
				<tr class="datacell2 withdrawals" id="checksMailedToId">
				<% } else { %>
				<tr class="datacell1 withdrawals" id="checksMailedToId">
			<% } %>
				<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute beanName="withdrawalChecksMailedLabel" attribute="text" /></td>
				<td width="20" align="right"><ps:fieldHilight name="checksMailedTo" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				<td colspan="2" width="302">&nbsp;&nbsp;
<c:if test="${csfForm.checksMailedTo == 'PY'}">Payee</c:if>
<c:if test="${csfForm.checksMailedTo == 'TP'}">TPA</c:if>
<c:if test="${csfForm.checksMailedTo == 'CL'}">Client</c:if>
<c:if test="${csfForm.checksMailedTo == 'TR'}">Trustee</c:if>
				</td>
			</tr>
			
			<% if (theIndex % 2 == 0) { %>
		        <tr class="datacell2 withdrawals" id="whoWillReviewWithdrawalsId">
		        <% } else { %>
		        <tr class="datacell1 withdrawals" id="whoWillReviewWithdrawalsId">
	        <% } %>
				<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<content:getAttribute beanName="withdrawalRequestReviewerLabel" attribute="text" /></td>
				<td width="20" align="right"><ps:fieldHilight name="whoWillReviewWithdrawals" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				
				<td width="302" colspan="2">
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
<c:if test="${userProfile.currentContract.bundledGaIndicator ==false}">
						<td width="28%">
<form:radiobutton disabled="true" path="whoWillReviewWithdrawals" value="NR" />No Review
						</td>
</c:if>
<c:if test="${csfForm.tpaFirmExists !=true}">
						<td>
<form:radiobutton disabled="true" path="whoWillReviewWithdrawals" value="PS" />Plan Sponsor
						</td>
</c:if>
<c:if test="${csfForm.tpaFirmExists ==true}">
						<td width="15%">
<form:radiobutton disabled="true" path="whoWillReviewWithdrawals" value="TPA" />TPA
						</td>
						<td width="57%">
<form:radiobutton disabled="true" path="whoWillReviewWithdrawals" value="PS" />Plan Sponsor
						</td>
</c:if>
					</tr>
					</table>
				</td>
			</tr>	
			
			   <% if (theIndex % 2 == 0) { %>
		        <tr class="datacell2 withdrawals" id="creatorMayApproveIndId">
		        <% } else { %>
		        <tr class="datacell1 withdrawals" id="creatorMayApproveIndId">
	        <% } %>
				<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute beanName="withdrawRequestApproverLabel" attribute="text" /></td>
				<td width="20" align="right"><ps:fieldHilight name="creatorMayApproveInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				<td width="60">
<form:radiobutton disabled="true" path="creatorMayApproveInd" value="Yes" />Yes
				</td>
				<td width="242">
<form:radiobutton disabled="true" path="creatorMayApproveInd" value="No" />No
				</td>
			</tr>
</c:if>
	</tbody>
</table>
