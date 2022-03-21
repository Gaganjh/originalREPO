<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.PendingWithdrawalDetailsReportData"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalMoneyTypeVO"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<un:useConstants var="cconstants"
	className="com.manulife.pension.ps.web.Constants" />
	
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
PendingWithdrawalDetailsReportData theReport = (PendingWithdrawalDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>
<c:if test="${empty param.printFriendly}">
<script type="text/javascript" >
	var ie4 = (document.all != null);
	var ns6 = (document.getElementById != null);
	var isMac = navigator.userAgent.indexOf("Mac") != -1;
	if (ie4 & isMac) {
	        ns6 = false;
	        ie4 = false;
	}
</script>
</c:if>
	<%-- Beans used --%>

	<%-- This jsp includes the following CMA content --%>
	<content:contentBean contentId="<%=ContentConstants.MULTI_PAYEE_NOTIFICATION%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="MultiPayeeNotify" />


	<%-- Start of report summary --%>
	<c:if test="${empty param.printFriendly}">
	<br/>
		<a href="/do/transaction/pendingTransactionHistoryReport/">Pending withdrawals</a>
	<br/>
	</c:if>
	<p>
		<content:errors scope="request" />
	</p>
	
	<c:if test="${not empty theReport}">
	
	<table id="detailsTable" border="0" cellpadding="0" cellspacing="0">
	<tbody>
	<!-- Section 1: Withdrawal General Information  -->
		<tr>
			<td width="1"></td>
			<td id="totals" width="42"></td>
			<td width="6"></td>
			<td id="itemName" width="40"></td>
			<td width="6"></td>
			<td id="itemSSN" width="40"></td>
			<td width="6"></td>
			<td id="itemEE" width="537"></td>
			<td width="50"></td>
			<td width="1"></td>
		</tr>

<c:forEach items="${theReport.details}" var="withdrawalDetailsItem" varStatus="theIndex" >
		<tr class="tablehead">
				<td class="tableheadTD1" colspan="10" style="padding: 3px;"><strong><content:getAttribute
					beanName="layoutPageBean" attribute="body1Header" /></strong></td>
		</tr>
		<tr class="datacell1">
				<td rowspan="5" class="databorder" width="0">
					<img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
				<td colspan="8" class="whiteBox" valign="top">
				<table width="650" border="0" cellpadding="10">
					<tbody>
						<tr>
							<td><b class="redTextBoldLarge">Payment amount: Pending</b>
							<p><strong>Transaction type:</strong>
${withdrawalDetailsItem.withdrawalGeneralInfoVO.transactionType} <br/>

							<strong>Type of withdrawal:</strong>
${withdrawalDetailsItem.withdrawalGeneralInfoVO.transactionTypeDescription}

							</p>
<c:if test="${userProfile.currentContract.definedBenefitContract ==false}">


								<p><strong>Name: </strong>
${withdrawalDetailsItem.withdrawalGeneralInfoVO.name} <br/>

								<strong>SSN:</strong>
${withdrawalDetailsItem.withdrawalGeneralInfoVO.ssn}</p>

</c:if>
							<p><strong>Expected withdrawal date: </strong><render:date dateStyle="m"
										property="withdrawalDetailsItem.withdrawalGeneralInfoVO.withdrawalDate" />
								</p>
							<p><strong>Transaction number: </strong>
${withdrawalDetailsItem.withdrawalGeneralInfoVO.transactionNumber}</p>

							</td>
						</tr>
					</tbody>
				</table>
				</td>
				<td rowspan="5" class="databorder"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			</tr>

			<!--  Section 2: Withdrawal payee Information  -->
			<tr class="tableheadTD">
				<td colspan="8" class="tableheadTD" style="padding: 3px;"
					valign="top"><strong><content:getAttribute
					beanName="layoutPageBean" attribute="body2Header" /></strong></td>
			</tr>

			<tr class="datacell1">
				<td colspan="8" class="multipayeeSec" valign="top">
				
					<%-- <c:set var="payeeN" value="${withdrawalDetailsItem.withdrawalPayeePaymentVO}"/> --%>
					<c:set var="payeeN" value ="${payeeCount}" />
					 <c:if test="${payeeN > 2}">
					 <br/>
					<table id="notification" width="650" align="center" border="0"	cellpadding="3">
						<tbody>
							<tr>
								<td><span class="notice">Notification:</span> 
								<content:getAttribute beanName="MultiPayeeNotify" attribute="text" /></td>
							</tr>
						</tbody>
					</table>

					</c:if>
				<table width="98%" align="center" border="0" cellpadding="0">
					<tbody>
						<!--  If payee information is available then display values accordingly  -->
						
<c:forEach items="${withdrawalDetailsItem.withdrawalPayeePaymentVO}" var="payee"  varStatus="index" >

							<tr>
								<td  width="93%" id="payment" align="left" valign="top"><br/>
								<c:if test="${payeeN > 1}">
									<b class="redTextBoldLarge"> ${index.count+1}.</b>
								</c:if>
								<b class="redTextBoldLarge">Payment amount: Pending</b></td>
								<td id="multiSubTitle" width="179" align="left" valign="top">&nbsp;</td>
								<td id="multiSubTitle" width="193" align="left" valign="top">&nbsp;</td>
							</tr>
<%
WithdrawalPayeePaymentVO payee=(WithdrawalPayeePaymentVO)pageContext.getAttribute("payee");
pageContext.setAttribute("payee", payee, PageContext.PAGE_SCOPE);
%>
							<tr>
								<td align="left" valign="top"><strong>Payment to: </strong>
${payee.paymentTo} <br/>
<strong>Payment method: </strong>${payee.paymentMethod} <strong><br/>

Bank account: </strong>${payee.accountType}
								</td>
								<td align="left" valign="top">&nbsp;</td>
								<td align="left" valign="top">&nbsp;</td>
							</tr>
							<tr>
								<td id="multiSubTitle3" align="left" valign="top">&nbsp;</td>
								<td align="left" valign="top">&nbsp;</td>
								<td align="left" valign="top">&nbsp;</td>
							</tr>
							<tr>
								<td id="multiSubTitle" width="179" align="left" valign="top">
								Payee address</td>
								<td align="left" valign="top">&nbsp;</td>
								<td align="left" valign="top">&nbsp;</td>
							</tr>
							<tr>
								<td align="left" valign="top">
<p> ${payee.payeeName} <br />
${payee.addressLine1} <br />
<c:if test="${not empty payee.addressLine2}">
${payee.addressLine2}<br/>
</c:if>
<c:if test="${not empty payee.city}">${payee.city},&nbsp;</c:if><c:if test="${not empty payee.state}">${payee.state}&nbsp;</c:if>${payee.zip} <br/>
${payee.country}
								</p>
								</td>
								<td align="left" valign="top">&nbsp;</td>
								<td align="left" valign="top">&nbsp;</td>
							</tr>
							<tr>
								<td id="multiSubTitle2" align="left" valign="top">&nbsp;</td>
								<td align="left" valign="top">&nbsp;</td>
								<td align="left" valign="top">&nbsp;</td>
							</tr>
							<tr>
								<td id="multiSubTitle" width="193" align="left" valign="top">Bank
								details</td>
								<td align="left" valign="top">&nbsp;</td>
								<td align="left" valign="top">&nbsp;</td>
							</tr>
							<tr>
								<td align="left" valign="top"><strong>Bank /
								Branch name: </strong>
${payee.bankBranchName}
								<br/>
								<strong>ABA / Routing number: </strong>
${payee.routingABAnumber}

									<strong><br/>Account number: </strong>
<c:if test="${not empty payee.accountNumber}">
										xxxxxxxxxx
</c:if><strong><br/>
Credit party name: </strong>${payee.creditPayeeName}</td>

								<td align="left" valign="top">&nbsp;</td>
								<td align="left" valign="top">&nbsp;</td>
							</tr>
</c:forEach>
					</tbody>
				</table>
				<br/>
				</td>
			</tr>


	 <!--  Section 3:Withdrawal Information  -->
<c:if test="${not empty withdrawalDetailsItem.withdrawalMoneyTypeVO}">
			<tr class="datacell1">
				<td colspan="8" class="tableheadTD" style="padding: 3px;"
					valign="top"><strong>
					<content:getAttribute beanName="layoutPageBean" attribute="body3Header" /></strong></td>
			</tr>
			<tr class="datacell1">
				<td colspan="8" class="whiteBox" valign="top"><br/>
				<br/>
				<table width="600" align="center" border="0" cellpadding="0"
					cellspacing="0">
					<tbody>
						<tr class="tablesubhead">
							<td rowspan="14" class="greyborder" width="1">
								<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1"></td>
							<td width="241" align="left" height="45"><b>Money type</b></td>
							<td class="dataheaddivider" width="1">
								<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1"></td>
								<td width="187" align="right"><b>Vesting(%)</b></td>
								<td rowspan="14" class="greyborder" width="1" align="right"><span
									class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif"
									alt="" width="1" height="1"></span></td>
						</tr>
<c:forEach items="${withdrawalDetailsItem.withdrawalMoneyTypeVO}" var="moneyType" varStatus="index" >


							<c:set var="rowId" value="${index.count % 2}" />
							<c:if test="${rowId==0}">
								<c:set var="rowClass" value="datacell1" />
</c:if>
							<c:if test="${rowId!=0}">
								<c:set var="rowClass" value="datacell2" />
</c:if>
					
							<tr class="${rowClass}">
<td>${moneyType.moneyType}</td>
								<td class="datadivider">
									<img src="/assets/unmanaged/images/s.gif"	alt="" width="1" height="1"></td>
								<td align="right" ><render:number	property="moneyType.vestingPercentage"  pattern="0.000" scale="3" type="c" sign="false" /></td>
							</tr>
</c:forEach>
						
						<tr class="greyborder">
							<td colspan="7" class="greyborder"><img
								src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1"></td>
						</tr>
					</tbody>
				</table>
				<br/>
				</td>
			</tr>
</c:if>
			<tr><td colspan="10" height="1" class="databorder"></td></tr>

</c:forEach>
	</tbody>
</table>
</c:if>
<br/>
<p><content:pageFooter beanName="layoutPageBean" /></p>
<p class="footnote"><content:pageFootnotes beanName="layoutPageBean" /></p>
<p class="disclaimer"><content:pageDisclaimer
	beanName="layoutPageBean" index="-1" /></p>
<c:if test="${not empty param.printFriendly}" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />

	<table width="725" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute
				beanName="globalDisclosure" attribute="text" /></td>
		</tr>
	</table>
</c:if>
