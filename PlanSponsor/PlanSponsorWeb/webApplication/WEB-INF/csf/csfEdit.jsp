<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfForm" %>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfConstants" %>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>

<script language="javascript">
function initPage() {
	
var deferrals = "${csfForm.changeDeferralsOnline}";
var planAciAllowed = "${e:forJavaScriptBlock(csfForm.planAciInd)}";
var csfAciAllowed = "${e:forJavaScriptBlock(csfForm.autoContributionIncrease)}";

	if (deferrals == "No" || 
			!(( planAciAllowed == "Y" && csfAciAllowed == "Yes") ||  ( planAciAllowed == "N" && csfAciAllowed == "No"))) {
		hideAttributes("deferralOnline");
	}
	
var deferralType = "${csfForm.deferralType}";
	if (deferralType == "") {
		disableRadio("autoContributionIncrease", true);
	}else{
		disableRadio("autoContributionIncrease", false);
	}
var ecSubSection = "${e:forJavaScriptBlock(csfForm.eligibilityCalculationInd)}";
	if (ecSubSection == "No") {
		hideAttributes("ecSubSection");
	}
var sendServiceSection = "${csfForm.noticeServiceInd}";
	if (sendServiceSection == "No") {
		hidePageElement('noticeTypeSelected');
	}
	
var ae = "${csfForm.autoEnrollInd}";
	if (ae == "No") {
		hideAttributes("ae");
	}
var aci = "${e:forJavaScriptBlock(csfForm.autoContributionIncrease)}";
	if (aci == "No") {
		hideAttributes("aci");
	}
	
var vesting = "${csfForm.vestingPercentagesMethod}";
	if (vesting == "NA") {
		hideAttributes("reportVesting");
	}
var loans = "${csfForm.allowOnlineLoans}";
	if (loans == "No") {
		hideAttributes("loans");
	}
var summaryPlanHighlightAvailable = "${csfForm.summaryPlanHighlightAvailable}";
	if (summaryPlanHighlightAvailable == "No") {
		hideAttributes("sph");
	}
	enableDeferralAmounts();
	
//var withdrawals = "${csfForm.withdrawalInd}";
	//if (withdrawals == "No") {
	//	hideAttributes("withdrawals");
	//}
	
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
<%-- After navbar --%>
<ps:form method="post" 	action="/do/contract/editServiceFeatures/" modelAttribute="csfForm" name="csfForm" >
	
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
							suppressDuplicateMessages="true" />
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
							<td class="boxborder" width="1"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1">
							</td>
							<td><%--EDIT MODE --%>
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tbody>
									<tr>
										<c:if test="${userProfile.getCurrentContract().isDefinedBenefitContract() == true}">
											<td>
											<table border="0" cellpadding="0" cellspacing="0" width="697">
												<tbody>
													<tr class="tablehead">
														<td class="tablehead" width="697" colspan="6" height="10"><b><content:getAttribute
															beanName="planSponsorServicesSectionTitle" attribute="text" /></b></td>
											
													</tr>
													<tr class="tablesubhead">
														<td height="10" width="697" colspan="6" class="tablesubhead"><b> <content:getAttribute
															beanName="electronicTransactionsSubSectionTitle" attribute="text" /> </b></td>
													</tr>
												    <tr class="datacell2">
														<td width="375"><content:getAttribute beanName="concentSubsequentLabel" attribute="text" /></td>
														<td width="20" align="right"><ps:fieldHilight name="consentInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
														<td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
														<td width="55">
<form:radiobutton path="consentInd" value="Yes" />Yes
														</td>
														<td width="55">
<form:radiobutton path="consentInd" value="No" />No
														</td>
														<td width="192">
<form:radiobutton path="consentInd" value="NA" />N/A
														</td>
														<ps:trackChanges name="csfForm" property="consentInd" />
													</tr>
												</tbody>
											</table>
											</td>
</c:if>
										<c:if test="${userProfile.getCurrentContract().isDefinedBenefitContract() == false}">
											<td>
<c:if test="${userProfile.internalUser ==true}">
													<jsp:include flush="true" page="csfPayrollSupportServicesEdit.jsp" /> 
													<jsp:include flush="true" page="csfFinancialTransactionsEdit.jsp" />
													<c:if test="${csfForm.planEligibleForManagedAccounts}">
														<jsp:include flush="true" page="csfManagedAccountsEdit.jsp" />
													</c:if>
													<jsp:include flush="true" page="csfPlanSupportServicesEdit.jsp" /> 
													<jsp:include flush="true" page="csfElectronicTransactionsEdit.jsp" />
</c:if>
<c:if test="${userProfile.internalUser !=true}">
													<jsp:include flush="true" page="csfPayrollSupportServicesConfirm.jsp" /> 
													<jsp:include flush="true" page="csfFinancialTransactionsConfirm.jsp" />
													<c:if test="${csfForm.planEligibleForManagedAccounts}">
														<jsp:include flush="true" page="csfManagedAccountsConfirm.jsp" />
													</c:if>
													<jsp:include flush="true" page="csfPlanSupportServicesConfirm.jsp" /> 
													<jsp:include flush="true" page="csfElectronicTransactionsConfirm.jsp" />
</c:if>
											</td>
</c:if>
										<td width="1" class="boxborder"><img
											src="/assets/unmanaged/images/spacer.gif" border="0"
											height="1" width="1"></td>
									</tr>
									<tr>
										<td colspan="2" class="boxborder" width="698"><img
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
<form:hidden path="action" />
					<table border="0" cellpadding="0" cellspacing="0" width="698">
						<tbody>
							<tr valign="top">
								<td width="35" valign="top">&nbsp;</td>
								<td width="200">
<div align="left"><input type="button" onclick="return doCancel();" name="button" class="button134" title="Return to view" value="<%=CsfConstants.BACK_BUTTON %>" &nbsp;


								</div>
								</td>
								<td width="464">
<div align="right"><input type="button" onclick="return doSave();" name="button" class="button134" title="Save changes" value="<%= CsfConstants.SAVE_BUTTON %>"</div>


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
