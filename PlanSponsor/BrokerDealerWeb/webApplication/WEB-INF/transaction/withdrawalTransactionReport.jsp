
<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.util.content.GenericException"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.CompletedWithdrawalDetailsReportData"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalMoneyTypeVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalCalculatedInfoVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%-- Beans used --%>

<%
CompletedWithdrawalDetailsReportData theReport = (CompletedWithdrawalDetailsReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

String YES = BDConstants.YES;
pageContext.setAttribute("YES",YES,PageContext.PAGE_SCOPE);
String NO = BDConstants.NO;
pageContext.setAttribute("NO",NO,PageContext.PAGE_SCOPE);
%>


<%-- This jsp includes the following CMA content --%>
<content:contentBean
	contentId="<%=BDContentConstants.CORRECTION_INDICATOR_MESSAGE%>"
	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="correctionIND" />
	
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>



<c:if test="${not empty theReport}">
<c:if test="${theReport.txnCorrectionIndicator == NO}">

<c:forEach items="${theReport.details}" var="withdrawalDetailsItem" varStatus="theIndex" >


				<bd:form  method="post"
							action="/do/bob/transaction/withdrawalDetailsReport/" modelAttribute="transactionDetailsWithdrawalForm" name="transactionDetailsWithdrawalForm">
							
<c:if test="${transactionDetailsWithdrawalForm.showLiaWithdrawalMessage ==true}">
								<content:contentBean
									contentId="<%=BDContentConstants.MISCELLANEOUS_WITHDRAWAL_AMOUNT_FOR_LIA%>"
									type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" beanName="liaWithdrawalMessage" />
</c:if>
		
						<div id="summaryBox">
							<h1><content:getAttribute id="layoutPageBean"
								attribute="subHeader" />
							</h1>
							<span> 
								Payment	Amount: 
								<strong><render:number	property="withdrawalDetailsItem.withdrawalGeneralInfoVO.paymentAmount"
								type="c" sign="true" /></strong>
							</span>
							<br/>
							<div>
								
							<br/>
								Type of Withdrawal:
<strong>${withdrawalDetailsItem.withdrawalGeneralInfoVO.transactionTypeDescription}</strong>


<c:if test="${transactionDetailsWithdrawalForm.showLiaWithdrawalMessage ==true}">
									<br/>
										<b class="redText"><content:getAttribute beanName="liaWithdrawalMessage" attribute="text" /> </b>
</c:if>
							</div>
<c:if test="${bobContext.currentContract.definedBenefitContract ==false}">


							<br/>
							<span class="name">
								Name:
<strong>${withdrawalDetailsItem.withdrawalGeneralInfoVO.name} </strong>

								<br/>
							
								SSN:
<strong>${withdrawalDetailsItem.withdrawalGeneralInfoVO.maskedSSN}

								</strong>
							</span>
							<br/>
</c:if>
							<br/>
							<span class="name">
							Withdrawal Date:
								<strong><render:date
								dateStyle="m"
								property="withdrawalDetailsItem.withdrawalGeneralInfoVO.withdrawalDate" /></strong>
							<strong>
							<br/>
							</strong>
							</span>
							Transaction Number:
<strong>	${withdrawalDetailsItem.withdrawalGeneralInfoVO.transactionNumber}</strong>


						</div>
		
					</bd:form>
</c:forEach> 
</c:if>

	</c:if>
	<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />
	<report:formatMessages scope="request" />
	<c:if test="${not empty theReport}">
<c:if test="${theReport.txnCorrectionIndicator == YES}">

			<div class="table_controls_footer"></div>
				<div class="message message_error">
					<dl>
						<dt>Message</dt>
						<dd>
							<content:getAttribute beanName="correctionIND"
							attribute="text" />
						</dd>
					</dl>
				</div>
</c:if>
	</c:if>
	<navigation:contractReportsTab />
	<c:if test="${not empty theReport}">
<c:if test="${theReport.txnCorrectionIndicator == NO}">

			<bd:form  method="post"
				action="/do/bob/transaction/withdrawalDetailsReport/" modelAttribute="transactionDetailsWithdrawalForm" name="transactionDetailsWithdrawalForm">
					<!--  Section 2:Include Withdrawal Information Details -->
				<jsp:include
					page="/WEB-INF/transaction/withdrawalInformationDetails.jsp"
					flush="true" />
				<!--  Section 3:Include Withdrawal Payee Payment Instruction -->
				<jsp:include
					page="/WEB-INF/transaction/withdrawalPayeeInformation.jsp"
					flush="true" />
			
			</bd:form>
</c:if>
	</c:if>

	<layout:pageFooter/>
