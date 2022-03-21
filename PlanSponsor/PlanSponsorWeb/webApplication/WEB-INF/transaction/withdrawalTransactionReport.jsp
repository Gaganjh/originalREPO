<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalMoneyTypeVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalCalculatedInfoVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.CompletedWithdrawalDetailsReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
CompletedWithdrawalDetailsReportData theReport = (CompletedWithdrawalDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

String YES = Constants.YES;
pageContext.setAttribute("YES",YES,PageContext.PAGE_SCOPE);
String NO = Constants.NO;
pageContext.setAttribute("NO",NO,PageContext.PAGE_SCOPE);
%>

<%-- Beans used --%>



	
<c:if test="${transactionDetailsWithdrawalForm.showLiaWithdrawalMessage ==true}">
	<content:contentBean
		contentId="<%=ContentConstants.MISCELLANEOUS_WITHDRAWAL_AMOUNT_FOR_LIA%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="liaWithdrawalMessage" />
</c:if>


<%-- This jsp includes the following CMA content --%>
<content:contentBean
	contentId="<%=ContentConstants.CORRECTION_INDICATOR%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="correctionIND" />
<content:contentBean
	contentId="<%=ContentConstants.LOAN_DEFAULT_NOTIFICATION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="LoanDefaultNotify" />
<content:contentBean
	contentId="<%=ContentConstants.MULTI_PAYEE_NOTIFICATION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="MultiPayeeNotify" />


<%-- Start of report summary --%>
<c:if test="${empty param.printFriendly}" >
	<ps:map id="parameterMap">
		<ps:param name="participantId" 
			valueBeanName="transactionDetailsWithdrawalForm"
			valueBeanProperty="pptId" />
	</ps:map>
	
<c:if test="${userProfile.currentContract.definedBenefitContract ==false}">

		<ps:link action="/do/transaction/participantTransactionHistory/"
			name="parameterMap">
			Participant transaction history
		</ps:link>
</c:if>
	<br/>
	<a href="/do/transaction/transactionHistoryReport/">
		Transaction	history
	</a>
	<br/>
	<br/>
	<br/>
</c:if>
	<%--  Populate error Message and Correction Indicator Message--%>
	<p class="redText" >
		<content:errors scope="request" /> 
		<c:if test="${not empty theReport}">


<c:if test="${theReport.txnCorrectionIndicator == YES }">

				<content:getAttribute beanName="correctionIND" attribute="text" />
</c:if>
		</c:if>
	</p>

<c:if test="${not empty theReport}">
<c:if test="${theReport.txnCorrectionIndicator == NO}">
		<%--  I need check here the correction indicator for presenting this three section --%>
	<table id="detailsTable" border="0" cellpadding="0" cellspacing="0">
		<tbody>
			<!-- Section 1: Withdrawal General Information  -->
			<tr>
				<td width="1">
				</td>
				<td id="totals" width="42">
				</td>
				<td width="6">
				</td>
				<td id="itemName" width="40">
				</td>
				<td width="6">
				</td>
				<td id="itemSSN" width="40">
				</td>
				<td width="6">
				</td>
				<td id="itemEE" width="537">
				</td>
				<td width="50">
				</td>
				<td width="0">
				</td>
			</tr>
			
<c:forEach items="${theReport.details}" var="withdrawalDetailsItem" varStatus="theIndex" >


					
				<tr class="tablehead">
					<td class="tableheadTD1" style="padding: 3px;" colspan="10">
						<strong>
							<content:getAttribute beanName="layoutPageBean" attribute="body1Header" />
						</strong>
					</td>
				</tr>
				<tr class="datacell1">
					<td rowspan="5" class="databorder" width="0">
					<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td colspan="8" class="whiteBox" valign="top">
					<table width="650" border="0" cellpadding="10">
						<tbody>
						<tr>
							<td>
								<b class="redTextBoldLarge">
								Payment amount:
								<render:number	property="withdrawalDetailsItem.withdrawalGeneralInfoVO.paymentAmount"
										type="c" sign="true" /> </b>
								
								<p>
									
									<strong>Type of withdrawal:</strong>
${withdrawalDetailsItem.withdrawalGeneralInfoVO.transactionTypeDescription}

										
<c:if test="${transactionDetailsWithdrawalForm.showLiaWithdrawalMessage ==true}">
										<br/>
											<b class="redText"><content:getAttribute beanName="liaWithdrawalMessage" attribute="text" /> </b>
</c:if>
								</p>
								
<c:if test="${userProfile.currentContract.definedBenefitContract ==false}">


									<p>
										<strong>Name: </strong> 
${withdrawalDetailsItem.withdrawalGeneralInfoVO.name}

										<br/>
										<strong>SSN: </strong> 
${withdrawalDetailsItem.withdrawalGeneralInfoVO.maskedSSN}

									</p>	
</c:if>
										
								<p>
									<strong>Withdrawal date: </strong> 
									<render:date dateStyle="m"
										property="withdrawalDetailsItem.withdrawalGeneralInfoVO.withdrawalDate" />
								</p>
								<p>
									<strong>Transaction number: </strong>
${withdrawalDetailsItem.withdrawalGeneralInfoVO.transactionNumber}

								</p>
							</td>
						</tr>
						</tbody>
					</table>
					</td>
					<td rowspan="5" class="databorder" width="1" height="1">
					<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
				</tr>
</c:forEach>
			<!--  Section 3:Withdrawal Information  -->
			<jsp:include page="/WEB-INF/transaction/withdrawalInformationDetails.jsp" flush="true" />
			<!--  Section 2:Withdrawal Payee Payment Instruction -->
			<jsp:include page="/WEB-INF/transaction/withdrawalPayeeInformation.jsp" flush="true" />
		</tbody>
	</table>
</c:if>

<br/>

<p>
	<content:pageFooter beanName="layoutPageBean" />
</p>
<p class="footnote">
	<content:pageFootnotes beanName="layoutPageBean" />
</p>
<p class="disclaimer">
	<content:pageDisclaimer beanName="layoutPageBean" index="-1" />
</p>
</c:if>
<c:if test="${not empty param.printFriendly }" >
	<content:contentBean
	contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />

		<table width="760" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%">
					<content:getAttribute
					beanName="globalDisclosure" attribute="text" />
				</td>
			</tr>
		</table>
</c:if>
