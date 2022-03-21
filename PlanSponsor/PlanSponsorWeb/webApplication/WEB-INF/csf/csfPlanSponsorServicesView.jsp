<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfConstants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.manulife.pension.ps.web.contract.csf.EligibilityCalculationMoneyType"%>

<%
String yesConstant = CsfConstants.CSF_YES;
pageContext.setAttribute("yes", yesConstant, PageContext.PAGE_SCOPE);
%>
<table border="0" cellpadding="0" cellspacing="0"  width="698">

	<!-- Plan Sponsor Services -->
	<tr class="tablehead">
		<td class="tablehead" width="698" colspan="3"><b><content:getAttribute
			beanName="planSponsorServicesSectionTitle" attribute="text" /></b></td>
	</tr>
	<tr>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td height="10" width="696" class="tablesubhead"><b><content:getAttribute
			beanName="planSupportServicesSubSectionTitle" attribute="text" /></b></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	<c:choose>
		<c:when test="${planDataLite.planTypeCode != '457'}">
			<tr class="datacell1">
		</c:when>
		<c:otherwise>
			<tr class="datacell2">
		</c:otherwise>
	</c:choose>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" ><content:getAttribute
			beanName="planHighlights" attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	<c:if test="${csfForm.planSponsorServicesData.summaryPlanHighlightReviewed != '0'}"> 
		<tr class="datacell1">
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<content:getAttribute id="planHighlightsReviewed" attribute="text" /></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
	</c:if>
<!-- Notices EDelivery starts -->
<c:if test="${csfForm.showNoticesEdeliveryInViewPage ==true}">
				<tr class="datacell2">
				<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">					
				</td>
				<td width="696">
				<content:getAttribute beanName="electronicDeliveryForPlanNoticesAndStatementsText" attribute="text" />
				<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  									
						 <c:choose>
						 	<%-- When the EDY/NOIA service is ON   --%>			
							<c:when test="${csfForm.noticeOfInternetAvailability == yes}">
								 <content:getAttribute id="edyNIAOn" attribute="text" />
							</c:when>
							<%-- When the EDY/NOIA service is OFF   --%>	
							<c:otherwise>
								<content:getAttribute id="edyOff" attribute="text" />
							</c:otherwise>
						  </c:choose>		
							<%-- WiredAtWork --%>
							<c:if test="${csfForm.wiredAtWork == yes}">
							<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <content:getAttribute id="edyWAWOn" attribute="text" />
							</c:if>										
				 </td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
</c:if>
<!-- Notices EDelivery ends -->
	
	<!-- Notice Service -->
<c:if test="${csfForm.displayNoticeGeneration ==true}">
		<%-- When the service is ON --%>
<c:if test="${csfForm.planSponsorServicesData.noticeGenerationServiceInd ==true}">
			<tr class="datacell1">
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<%-- This is hard coded. CMA key required --%>
				<td width="696" ><%--SEND Service--%>
				<content:getAttribute id="sendServiceOn" attribute="text" /> </td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
			<tr class="datacell1">
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<%-- This is hard coded. CMA key required --%>
				<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%-- SEND service turned on as of:  --%>
				<content:getAttribute id="sendServiceOnEffDt" attribute="text" >
					<content:param>${csfForm.planSponsorServicesData.noticeServiceSelectedDate}</content:param>
				</content:getAttribute></td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
			<tr class="datacell1">
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<%-- This is hard coded. CMA key required --%>
				<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%-- Notice being generated and mailed: --%>
				<content:getAttribute id="sendServiceOnNoticeTypeSelectedMsg" attribute="text" /></td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
			<tr class="datacell1">
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${csfForm.planSponsorServicesData.noticeOption}</td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
</c:if>
		<%-- When the service is OFF --%>
<c:if test="${csfForm.planSponsorServicesData.noticeGenerationServiceInd !=true}">
		<tr class="datacell1">
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<%-- This is hard coded. CMA key required --%>
			<td width="696" ><%--SEND Service is off --%>
			<content:getAttribute id="sendServiceOff" attribute="text" /> </td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
</c:if>
</c:if>
	
		<!-- Notice Service -->
	<c:if test="${planDataLite.planTypeCode != '457'}"> 
	<!-- Eligibility Calculation Service -->
<c:if test="${csfForm.displayNoticeGeneration ==true}">
		<tr class="datacell2">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
		<tr class="datacell1">
</c:if>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td ><content:getAttribute
			beanName="eligibilityCalculationService" attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>

<c:if test="${csfForm.planSponsorServicesData.isECon ==true}">
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell2">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell1">
</c:if>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" >
			<table>
				<tr>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
						beanName="eligibilityMoneyTypesLabel" attribute="text" /></td>
				</tr>
<c:forEach items="${csfForm.planSponsorServicesData.eligibilityServiceMoneyTypes}" var="eligibityServiceMoneyTypeId" varStatus="index" >



						<tr>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
${eligibityServiceMoneyTypeId.moneyTypeDescription}</td>
						</tr>
</c:forEach>
			</table>
			</td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
</c:if>
	</c:if>
	<!-- JH Ezstart -->
<c:if test="${csfForm.displayNoticeGeneration ==true}">
		<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
		<tr class="datacell2">
</c:if>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" ><content:getAttribute
			beanName="jhEZstart" attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
<c:if test="${csfForm.planSponsorServicesData.isEzStartOn ==true}">

<c:if test="${not empty csfForm.planSponsorServicesData.initialEnrollmentParam}">
<c:if test="${csfForm.displayNoticeGeneration ==true}">
				<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
				<tr class="datacell2">
</c:if>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<content:getAttribute id="initialEnrollmentDate" attribute="text">
						<content:param>${csfForm.planSponsorServicesData.initialEnrollmentParam}</content:param>
				</content:getAttribute> </td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
</c:if>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2">
</c:if>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<content:getAttribute id="directMailEnrollment" attribute="text" /></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
</c:if>

	<!-- JH EZIncrease -->
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell2">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell1">
</c:if>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td ><content:getAttribute beanName="jhEZincrease"
			attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>

<c:if test="${csfForm.planSponsorServicesData.isEzIncreaseOn ==true}">

	<c:if test="${0 != csfForm.planSponsorServicesData.firstScheduledIncrease}">
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell2">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell1">
</c:if>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			<content:getAttribute beanName="firstScheduledIncrease" attribute="text" >
<c:if test="${not empty csfForm.planSponsorServicesData.firstSchedParam}">
					<content:param>${csfForm.planSponsorServicesData.firstSchedParam}</content:param>
</c:if>
			</content:getAttribute></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
	</c:if>
<c:if test="${csfForm.planSponsorServicesData.isEzIncreaseCustomized ==true}">

<c:if test="${csfForm.displayNoticeGeneration ==true}">
				<tr class="datacell2">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
				<tr class="datacell1">
</c:if>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i><content:getAttribute
					beanName="customizedLabel" attribute="text" /></i></td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
				<tr class="datacell2">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
				<tr class="datacell1">
</c:if>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
					beanName="initialIncreaseAnniversaryDate" attribute="text" /></td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
</c:if>

</c:if>


	<!-- Vesting calculation -->
<c:if test="${csfForm.displayNoticeGeneration ==true}">
		<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
		<tr class="datacell2">
</c:if>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" ><content:getAttribute id="vesting"
			attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>

<c:if test="${csfForm.planSponsorServicesData.isVestingLabelSurpressed ==true}">


<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2">
</c:if>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i><content:getAttribute
				beanName="customizedLabel" attribute="text" /></i></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2">
</c:if>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
				id="reportVestingPercentages" attribute="text" /></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
</c:if>

	<!-- IPS -->
<c:if test="${csfForm.ipsServiceSuppressed ==false}">
<c:if test="${csfForm.planSponsorServicesData.ipsService == yes}">
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell2">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell1">
</c:if>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" ><content:getAttribute id="investmentPolicyStatementService"
				attribute="text" /></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell2">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell1">
</c:if>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;			
				<content:getAttribute id="annualReviewServiceProcessingDate" attribute="text">
				<c:if test="${ not empty csfForm.planSponsorServicesData.annualReviewProcessingDateContentParam}">
					<content:param>${csfForm.planSponsorServicesData.annualReviewProcessingDateContentParam}</content:param>
				</c:if>
			</content:getAttribute>
			</td>
				
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>	
</c:if>
</c:if>
	
	<!-- Display Co-Fid Feature if the contract has CoFid  -->
<c:if test="${csfForm.coFidFeatureSuppressed ==false}">
<c:forEach items="${csfForm.coFidServiceFeatureDetails}" var="coFidServiceFeatureDetail">
<c:if test="${coFidServiceFeatureDetail.selectedServiceProvider ==true}">
<c:if test="${csfForm.displayNoticeGeneration ==true}">
				<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
				<tr class="datacell2">
</c:if>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<td width="696" ><content:getAttribute id="coFiduciaryViewPage"
					attribute="text" ><content:param>${coFidServiceFeatureDetail.coFidServiceProviderDescription}</content:param></content:getAttribute>
					</td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
			
			<!-- Display selected Wilshire 3(21) Investment Profile -->
<c:if test="${csfForm.displayNoticeGeneration ==true}">
				<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
				<tr class="datacell2">
</c:if>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;			
					<content:getAttribute id="coFidWilshireAdviserServiceViewPage" attribute="text">
					<content:param>${csfForm.selectedInvestmentProfile}</content:param>
				</content:getAttribute>
				</td>
					
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>	
</c:if></c:forEach>
</c:if>

	<!-- Electronic Transactions -->
	<tr>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td height="10"  class="tablesubhead"><b><content:getAttribute
			id="electronicTransactionsSubSectionTitle" attribute="title" /> </b></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	<tr class="datacell2">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" >
			<content:getAttribute id="payrollFrequency" attribute="text">
			<c:if test="${ not empty csfForm.planSponsorServicesData.payrollFreqParam}">
				<content:param>${csfForm.planSponsorServicesData.payrollFreqParam}</content:param>
			</c:if>
		</content:getAttribute>
		</td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	<tr class="datacell1">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" ><content:getAttribute
			id="allowPayrollPathSubmissions" attribute="title" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	
	<!-- Payroll Feedback Service -->
	<tr class="datacell2">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" >
			<content:getAttribute id="payrollFeedbackServiceContent" attribute="text" />
		</td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	
	<%
		String cellFormat = "dataCell1";
		int rowCount = 0;
	%>
<c:if test="${csfForm.isHideConsent ==false}">
		<%
			rowCount = rowCount + 1;
				if ((rowCount % 2) > 0)
					cellFormat = "datacell1";
				else
					cellFormat = "datacell2";
		%>
		<tr class="<%=cellFormat%>">
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" height="10" ><content:getAttribute
				id="consentSubsequentAmendments" attribute="text" /></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
</c:if>
<c:if test="${csfForm.participantServicesData.isJHdoesLoanRK ==true}">
	<%
		rowCount = rowCount + 1;
		if ((rowCount % 2) > 0)
			cellFormat = "datacell1";
		else
			cellFormat = "datacell2";
	%>
	<tr class="<%=cellFormat%>">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" ><content:getAttribute
			id="onlineLoans" attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
<c:if test="${csfForm.planSponsorServicesData.isOnlineLoansAllowed ==true}">
		<tr class="<%=cellFormat%>">
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<content:getAttribute id="loanChecksMailed" attribute="text" >
				<content:param>${csfForm.planSponsorServicesData.loanMailedParam}</content:param>
			</content:getAttribute></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
<c:if test="${csfForm.planSponsorServicesData.isLoanLabelSurpressed ==true}">

			<tr class="<%=cellFormat%>">
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i><content:getAttribute
					beanName="customizedLabel" attribute="text" /></i></td>
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
			</tr>
			<c:if test="${0 != csfForm.planSponsorServicesData.loanApprover}">
				<tr class="<%=cellFormat%>">
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
						id="loanApprover" attribute="text" /></td>
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
				</tr>
			</c:if>
			<c:if test="${0 != csfForm.planSponsorServicesData.loanPackagesGenerated}">
				<tr class="<%=cellFormat%>">
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
						id="loanPackagesGenerated" attribute="text" /></td>
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
				</tr>
			</c:if>
			<c:if test="${0 != csfForm.planSponsorServicesData.loanPriorApproval}">
				<tr class="<%=cellFormat%>">
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
						id="loanPriorApproval" attribute="text" /></td>
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
				</tr>
			</c:if>
</c:if>
</c:if>
</c:if>
	<%
		rowCount = rowCount + 1;
		if ((rowCount % 2) > 0)
			cellFormat = "datacell1";
		else
			cellFormat = "datacell2";
	%>
	<tr class="<%=cellFormat%>">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" ><content:getAttribute
			id="iWithdrawals" attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
<c:if test="${csfForm.planSponsorServicesData.isIWithdrawalsAllowed ==true}">
		<tr class="<%=cellFormat%>">
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<content:getAttribute id="withdrawalIRSSpecialTaxNotices" attribute="text" /></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
		<tr class="<%=cellFormat%>">
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<content:getAttribute id="withdrawalChecksMailed" attribute="text">
				<content:param>${csfForm.planSponsorServicesData.withdrawMailedParam}</content:param>
			</content:getAttribute></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
<c:if test="${csfForm.planSponsorServicesData.isWithdrawalLabelSurpressed ==true}">
			<tr class="<%=cellFormat%>">
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i><content:getAttribute
					beanName="customizedLabel" attribute="text" /></i></td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
			<c:if test="${0 != csfForm.planSponsorServicesData.withdrawalPriorApproval}">
				<tr class="<%=cellFormat%>">
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
						id="withdrawalPriorApproval" attribute="text" /></td>
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
				</tr>
			</c:if>
			<c:if test="${0 != csfForm.planSponsorServicesData.withdrawalApproval}">
				<tr class="<%=cellFormat%>">
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
						id="withdrawalApproval" attribute="text" /></td>
					<td width="1" class="boxborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
				</tr>
			</c:if>
</c:if>
</c:if>
	
</table>
