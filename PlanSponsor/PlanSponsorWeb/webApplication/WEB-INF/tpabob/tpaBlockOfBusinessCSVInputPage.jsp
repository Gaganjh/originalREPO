<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.service.security.valueobject.UserInfo" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<un:useConstants var="psWebConstants" className="com.manulife.pension.ps.web.Constants"/>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%> 
<c:set var="tpaBlockOfBusinessForm" value="${tpaBlockOfBusinessForm}" />




<c:set var="tabs" value="${Constants.TPA_BOB_TABS_LIST}" scope="request" />

<c:set var="actionURL" value="/do/tpabob/tpaBlockOfBusiness/" />
<c:set var="doStr" value="/do"/>
<c:set var="spaceStr" value="" />

<c:forEach items="${tabs}" var="tabItem" >
	<%-- enabled means it is the "selected tab".--%>
	<c:if test="${tabItem.isEnabled}">
		<%-- the actionURL contains string like "/do/tpabob/tpaBlockOfBusiness/". 
		But, we need only "/tpabob/tpaBlockOfBusiness/". So, below code also replaces the "/do".--%>
		<c:set var="actionURL" value="${fn:replace(tabItem.actionURL, doStr , spaceStr)}" />
	</c:if>
</c:forEach>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td valign="top" align="left">
			<img src="/assets/unmanaged/images/s.gif" width="10" height="1">
		</td>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<ps:form modelAttribute="tpaBlockOfBusinessForm" name="tpaBlockOfBusinessForm" action="${actionURL}" >
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>TPA name:
										<% if (userProfile.isInternalUser()) {%>
											<strong>${sessionScope['tpaUserInfo'].firstName} ${sessionScope['tpaUserInfo'].lastName}</strong> 
										<%	} else {%>
											<strong>${userProfile.principal.firstName} ${userProfile.principal.lastName}</strong>
										<%	} %>
									</td>
									<td style="text-align:right">As of: 
				                      <render:date patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>"
	                                         property="tpaBlockOfBusinessForm.selectedDateInDateFormat"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
					</tr>
					<tr>
						<td class="tableheadTD1" colspan="3">
							<b><content:getAttribute id="layoutPageBean" attribute="body1Header"/>&nbsp;</b>
						</td>
					</tr>
					<tr>
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td>
							<table width="348" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="24"></td>
									<td width="324"></td>
								</tr>
								<tr class="tablesubhead">
									<td colspan="2"><strong>General</strong></td>
								</tr>
								<tr class="datacell1">
<td><input type="hidden" name="task" value="download"/>
<form:checkbox path="contractNameCheckBoxInd" disabled="true" value="true"/></td>
									<td>Contract Name</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="contractNumberCheckBoxInd" disabled="true" value="true"/></td>
									<td>Contract Number</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="tpaFirmIDCheckBoxInd" value="true"/></td>
									<td>TPA Firm ID</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="brokerNameCheckBoxInd" value="true"/></td>
									<td>Broker Name</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="jhClientRepNameCheckBoxInd" value="true"/></td>
									<td>JH Client Representative</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="contractStatusCheckBoxInd" value="true"/></td>
									<td>Contract Status</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="contractEffDtCheckBoxInd" value="true"/></td>
									<td>Contract Effective Date</td>		
								</tr>
								<tr class="tablesubhead">
									<td colspan="2"><strong>Assets and Lives</strong></td>
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="livesCheckBoxInd" value="true"/></td>
									<td>Lives</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="allocatedAssetsCheckBoxInd" value="true"/></td>
									<td>Allocated Assets</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="loanAssetsCheckBoxInd" value="true"/></td>
									<td>Loan Assets</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="cashAccountBalanceCheckBoxInd" value="true"/></td>
									<td>Cash account balance</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="pbaAssetsCheckBoxInd" value="true"/></td>
									<td>PBA Assets</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="totalContractAssetsCheckBoxInd" value="true"/></td>
									<td>Total Contract Assets</td>		
								</tr>
								<tr class="tablesubhead">
									<td colspan="2"><strong>Contract Service Features</strong></td>
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="ezStartCheckBoxInd" value="true"/></td>
									<td>EZstart</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="ezIncreaseCheckBoxInd" value="true"/></td>
									<td>Scheduled Deferral Increase</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="directMailCheckBoxInd" value="true"/></td>
									<td>Direct Mail</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="giflCheckBoxInd" value="true"/></td>
									<td>${psWebConstants.COL_GIFL_FEATURE_TITLE_SC}</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="managedAccountCheckBoxInd" value="true"/></td>
									<td>Managed Accounts</td>		
								</tr>								
								<tr class="datacell2">
<td><form:checkbox path="sendServiceCheckBoxInd" value="true"/></td>
									<td>SEND Service</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="johnHancockPassiveTrusteeCheckBoxInd" value="true"/></td>
									<td>John Hancock Trustee Service</td>		
								</tr>
								<!-- RPSSO-124653 Start -->
								<tr class="datacell2">
<td><form:checkbox path="sfcCheckBoxInd" value="true"/></td>
									<td>Signature Fiduciary Connect</td>		
								</tr>	
								<tr class="datacell1">
<td><form:checkbox path="pepCheckBoxInd" value="true"/></td>
									<td>Pooled Employer Plan</td>		
								</tr>
								<!-- RPSSO-124653 End -->
								<tr class="tablesubhead">
									<td colspan="2"><strong>Others</strong></td>
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="pamCheckBoxInd" value="true"/></td>
									<td>Participant Address Management</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="onlineBeneficiaryDsgnCheckBoxInd" value="true"/></td>
									<td>Online Beneficiary Designation </td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="onlineWithdrawalsCheckBoxInd" value="true"/></td>
									<td>Online Withdrawals</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="sysWithdrawalCheckBoxInd" value="true"/></td>
									<td>Systematic Withdrawals</td>		
								</tr> 
								
									<tr class="datacell2">
<td><form:checkbox path="installmentsCheckBoxInd" value="true"/></td>
									<td>Installments</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="vestingPercentageCheckBoxInd" value="true"/></td>
									<td>Vesting Percentage</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="vestingOnStatementsCheckBoxInd" value="true"/></td>
									<td>Vesting on Statements</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="permittedDisparityCheckBoxInd" value="true"/></td>
									<td>Permitted Disparity</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="fswCheckBoxInd" value="true"/></td>
									<td>Fiduciary Standards Warranty</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="dioCheckBoxInd" value="true"/></td>
									<td>Default investment option(s)</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="tpaSigningAuthorityCheckBoxInd" value="true"/></td>
									<td>TPA Signing Authority </td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="cowCheckBoxInd" value="true"/></td>
									<td>Contracts on Web</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="payRollPathCheckBoxInd" value="true"/></td>
									<td>Payroll Path</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="payrollFeedbackCheckBoxInd" value="true"/></td>
									<td>Payroll Feedback</td>		
								</tr>
								<tr class="datacell2">
<td><form:checkbox path="planHighlightsCheckBoxInd" value="true"/></td>
									<td>Plan Highlights</td>		
								</tr>
								<tr class="datacell1">
<td><form:checkbox path="planHighlightsReviewedCheckBoxInd" value="true"/></td>
									<td>Plan Highlights Reviewed</td>		
								</tr>
								<tr>
									<td colspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								</tr>
							</table>
						</td>
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					<tr>
						<td colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
					</tr>
					<tr>
						<td colspan="3" style="text-align:right;">
							<input name="Button" type="Button" value="generate" onClick="generateCSV(this.form)"/>
							<input name="Button" type="Button" value="cancel" onClick="cancelCSV()"/>
						</td>
					</tr>
					<tr>
						<td colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
					</tr>
				</ps:form>
			</table>

		</td>
	</tr>
</table>
