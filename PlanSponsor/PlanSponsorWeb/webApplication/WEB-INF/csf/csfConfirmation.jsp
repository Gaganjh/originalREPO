<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfForm" %>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfConstants" %>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="definedBenefit" value="${userProfile.currentContract.definedBenefitContract}"/>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>

<script language="javascript">
function initPage() {
	
var deferrals = "${csfForm.changeDeferralsOnline}";
	if (deferrals == "No") {
		hidePageElement('defaultDeferralSection');
	}
	
var ecSubSection = "${csfForm.eligibilityCalculationInd}";
	if (ecSubSection == "No") {
		hidePageElement('eligibilityMoneyTypesId');
	}
	
var sendServiceSection = "${csfForm.noticeServiceInd}";
	if (sendServiceSection == "No") {
		hidePageElement('noticeTypeSelected');
	}
var ae = "${csfForm.autoEnrollInd}";
	if (ae == "No") {
		hidePageElement('initialEnrollmentDateId');
		hidePageElement('directMailIndId');
	}
var aci = "${csfForm.autoContributionIncrease}";
	if (aci == "No") {
		hidePageElement('aciAnniversaryId');
		hidePageElement('increaseAnniversaryId');
	}
	
var vesting = "${csfForm.vestingPercentagesMethod}";
	if (vesting == "NA") {
		hidePageElement('vestingDataOnStatementId');
	}
var loans = "${csfForm.allowOnlineLoans}";
	if (loans == "No") {
		hidePageElement('loansChecksMailedToId');
		hidePageElement('whoWillReviewLoanRequestsId');
		hidePageElement('creatorMayApproveLoanRequestsIndId');
		hidePageElement('allowLoansPackageToGenerateId');
	}
var autoSignup = "${csfForm.aciSignupMethod}";
	if (autoSignup == "A") {
		hidePageElement("deferralLimitPercentId");
	}else{
		hidePageElement("deferralLimitPercentAutoId");
	}
var summaryPlanHighlightAvailable = "${csfForm.summaryPlanHighlightAvailable}";
	if (summaryPlanHighlightAvailable == "No") {
		hideAttributes("sph");
	}
	enableDeferralAmounts();
	disableACIAttributes(true);
	supressWithdrawalChildSection();
}

/**
 * Supresses the Withdrawal Child attributes if the parent withdrawal ind is Off
*/
function supressWithdrawalChildSection() {
var withdrawalInd = "${csfForm.withdrawalInd}";
	if  (withdrawalInd == 'No') {
		hidePageElement('specialTaxNoticeId');
		hidePageElement('checksMailedToId');
		hidePageElement('whoWillReviewWithdrawalsId');
		hidePageElement('creatorMayApproveIndId');
	} 
}

if (window.addEventListener) {
	window.addEventListener('load', initPage, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', initPage);
}
</script>
 <jsp:include flush="true" page="csfEditCMAKeys.jsp"></jsp:include>	
 <jsp:include flush="true" page="csfViewCMAKeys.jsp"></jsp:include>	
 <jsp:include flush="true" page="csfPageContentHeader.jsp"></jsp:include>

<ps:form method="post" action="/do/contract/confServiceFeatures/" modelAttribute="csfForm" name="csfForm">
	<table border="0" cellpadding="0" cellspacing="0" width="760">
		<tbody>
			
			<tr>
				<td>&nbsp;</td>
				<td valign="top" width="727">
				<table>
					<tr>
						<td colspan="2">
						<div class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
						<ps:messages scope="session"
							maxHeight="${param.printFriendly ? '1000px' : '100px'}"
							suppressDuplicateMessages="true" showOnlyWarningContent="true" />
						</div>
						</td>
					</tr><tr><td colspan="2">&nbsp;</td></tr>
				</table>
				<table class="box" border="0" cellpadding="0" cellspacing="0"
					width="698">
					<tbody>
					 	<c:if test="${userProfile.internalUser}">
							<tr>
								<jsp:include flush="true" page="navigationTabBar.jsp">
									<jsp:param name="selectedTab" value=""/>
								</jsp:include>
							</tr>
						</c:if>
						<tr>
							<td>
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tbody>
									<tr>
										<td width="1" class="boxborder"><img
											src="/assets/unmanaged/images/spacer.gif" border="0"
											height="1" width="1"></td>
										<c:if test="${definedBenefit == true}">
										<td>
											<table border="0" cellpadding="0" cellspacing="0" width="697">
												<tbody>
												<tr class="tablehead">
														<td class="tablehead" width="697" colspan="6" height="10"><b><content:getAttribute
															beanName="planSponsorServicesSectionTitle" attribute="text" /></b></td>
											
													</tr>
													<tr class="tablesubhead">
														<td height="10" colspan="6" class="tablesubhead"><b> <content:getAttribute
															beanName="electronicTransactionsSubSectionTitle" attribute="text" /> </b></td>
													</tr>
												    <tr class="datacell1">
														<td width="390"><content:getAttribute beanName="concentSubsequentLabel" attribute="text" /></td>
														<td width="5" align="right"><ps:fieldHilight name="consentInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
														<td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
														<td width="55">
<form:radiobutton disabled="true" path="consentInd" value="Yes" />Yes
														</td>
														<td width="55">
<form:radiobutton disabled="true" path="consentInd" value="No" />No
														</td>
														<td width="191">
<form:radiobutton disabled="true" path="consentInd" value="NA" />N/A
														</td>
														<ps:trackChanges name="csfForm" property="consentInd" />
													</tr>
												</tbody>
											</table>
										</td>
</c:if>
										<c:if test="${definedBenefit == false}">
										<td>
												<jsp:include flush="true" page="csfPayrollSupportServicesConfirm.jsp" /> 
												<jsp:include flush="true" page="csfFinancialTransactionsConfirm.jsp" />
												<c:if test="${csfForm.planEligibleForManagedAccounts}">
														<jsp:include flush="true" page="csfManagedAccountsConfirm.jsp" />
												</c:if>
												<jsp:include flush="true" page="csfPlanSupportServicesConfirm.jsp" /> 
												<jsp:include flush="true" page="csfElectronicTransactionsConfirm.jsp" />
</td></c:if>
										<td width="1" class="boxborder"><img
											src="/assets/unmanaged/images/spacer.gif" border="0"
											height="1" width="1"></td>
									</tr>
									<tr>
										<td colspan="3">
										<table border="0" cellpadding="0" cellspacing="0" width="100%">
											<tbody>
												<tr>
													<td class="boxborder"><img
											src="/assets/unmanaged/images/spacer.gif" border="0"
											height="1" width="1"></td>
												</tr>
											</tbody>
										</table>
										</td>
									</tr>
								</tbody>
							</table>
							</td>
						</tr>

					</tbody>
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="2"><c:if test="${empty param.printFriendly}">
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tbody>
							<tr>
								<td>&nbsp;</td>
								<td align="right">&nbsp;</td>
							</tr>
						</tbody>
					</table>
<input type="hidden" name="action"/>
					<table border="0" cellpadding="0" cellspacing="0" width="698">
						<tbody>
							<tr valign="top">
								<td width="35" valign="top">&nbsp;</td>
								<td width="200">
<div align="left"><input type="submit" class="button134" onclick="return doContinueEdit();" title="Continue Editing" value="continue editing" />


								</div>
								</td>
								<td width="463">
<div align="right"><input type="button" onclick="return doAccept();" name="button" class="button134" title="Accept" value="accept"/></div>


								</td>
							</tr>
						</tbody>
					</table>

					<br>
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tbody>
							<tr>
								<td align="right" width="80%">&nbsp;</td>
								<td align="right" width="20%">&nbsp;</td>
							</tr>
						</tbody>
					</table>
				</c:if></td>
			</tr>
		</tbody>
	</table>
</ps:form>
