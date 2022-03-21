<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.content.ContentHelper"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.ParticipantBenefitBaseInformationReportData"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.ParticipantBenefitBaseInformationReportData" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantBenefitBaseDetails" %>
<%@ page import="com.manulife.pension.ps.web.participant.ParticipantBenefitBaseInformationForm" %>
<%@ page import="com.manulife.pension.service.account.valueobject.ParticipantGiflData" %>
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_BENEFIT_BASE_BATCH_OUT_OF_DATE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="benefitBaseBatchOutOfDate" />

<%
String NA=Constants.NA;
pageContext.setAttribute("NA",NA,PageContext.PAGE_SCOPE);
String Date=Constants.DEFAULT_DATE;
pageContext.setAttribute("Date",Date,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
ParticipantBenefitBaseDetails benefitDetails = (ParticipantBenefitBaseDetails)request.getAttribute(Constants.BENEFIT_DETAILS);
pageContext.setAttribute("benefitDetails",benefitDetails,PageContext.PAGE_SCOPE);
ParticipantGiflData giflDetails =(ParticipantGiflData)request.getAttribute(Constants.ACCOUNT_DETAILS);
pageContext.setAttribute("giflDetails",giflDetails,PageContext.PAGE_SCOPE);
String technicalDifficulties =(String)request.getAttribute(Constants.TECHNICAL_DIFFICULTIES);
pageContext.setAttribute("technicalDifficulties",technicalDifficulties,PageContext.PAGE_SCOPE);

%>
<%
ParticipantBenefitBaseInformationReportData  theReport=(ParticipantBenefitBaseInformationReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>
<%
ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm=(ParticipantBenefitBaseInformationForm)session.getAttribute("participantBenefitBaseInformationForm");
pageContext.setAttribute("participantBenefitBaseInformationForm",participantBenefitBaseInformationForm,PageContext.PAGE_SCOPE);
%>
	
<c:if test="${participantBenefitBaseInformationForm.showLIADetailsSection ==true}">
	<content:contentBean
		contentId="<%=ContentConstants.MISCELLANEOUS_LIA_SELECTION_DATE_FIELD_LABEL%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
		id="liaSelectionDateFieldLabel" />
		
	<content:contentBean
		contentId="<%=ContentConstants.MISCELLANEOUS_LIA_SPOUSAL_OPTION_FIELD_LABEL%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
		id="spousalOptionFieldLabel" />
	
	<content:contentBean
		contentId="<%=ContentConstants.MISCELLANEOUS_LIA_PERCENTAGE_FIELD_LABEL%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
		id="liaPercentageFieldLabel" />
		
	<content:contentBean
		contentId="<%=ContentConstants.MISCELLANEOUS_ANNUAL_LIA_AMOUNT_FIELD_LABEL%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
		id="annualLIAAmountFieldLabel" />
		
	<content:contentBean
		contentId="<%=ContentConstants.MISCELLANEOUS_LIA_PAYMENT_FREQUENCY_FIELD_LABEL%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
		id="paymentFrequencyFieldLabel" />
		
	<content:contentBean
		contentId="<%=ContentConstants.MISCELLANEOUS_LIA_ANNIVERSARY_DATE_FIELD_LABEL%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
		id="liaAnniversaryDateFieldLabel" />
	
	<content:contentBean
		contentId="<%=ContentConstants.MISCELLANEOUS_BENEFIT_BASE_LIA_MESSAGE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
		id="partcipantApplicableToLiaMessage" />
</c:if>

<c:if test="${not empty theReport}">



</c:if>

<c:if test="${empty technicalDifficulties}">
<c:if test="${empty param.printFriendly }" >
	<script  type="text/javascript"
		src="/assets/unmanaged/javascript/calendar.js"></script>
<script type="text/javascript" >

   function doReset(nextAction) {
	   	document.participantBenefitBaseInformationForm.task.value = nextAction;
  	 	document.participantBenefitBaseInformationForm.action="/do/participant/participantBenefitBaseInformation/";
	 		return true;
		}	
   	   	
   function submitFilter() {
			setFilterFromInput(document.forms['participantBenefitBaseInformationForm'].elements['fromDate']);
			setFilterFromInput(document.forms['participantBenefitBaseInformationForm'].elements['toDate']);
			doFilter('/do/participant/participantBenefitBaseInformation');
		}
   function submitDates() {
			setFilterFromInput(document.forms['participantBenefitBaseInformationForm'].elements['fromDate']);
			setFilterFromInput(document.forms['participantBenefitBaseInformationForm'].elements['toDate']);
			doFilter('/do/participant/participantBenefitBaseInformation');
   }
	
   function doOnload() {
			var lastVisited = "<%=request.getParameter("lastVisited")%>";
			var pageNumber = "<%=request.getParameter("pageNumber")%>";
			var sortField = "<%=request.getParameter("sortField")%>";
			if ((lastVisited == "true") || (pageNumber != "null") || (sortField != "null")) {
				location.hash = "participantName";
  			}
  }
</script>

</c:if>
</c:if> 
<%-- technical difficulties --%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/assets/unmanaged/images/s.gif" width="30"
			height="1"></td>
		<td width="100%" valign="top">
<c:if test="${participantBenefitBaseInformationForm.bbBatchDateLessThenETL == 'Y'}">
			<table   border="0" cellspacing="5" cellpadding="0"><tr><td  class="redTextBold">
				<content:getAttribute  beanName="benefitBaseBatchOutOfDate" attribute="text"/></td></tr>
			</table>
</c:if>
<c:if test="${participantBenefitBaseInformationForm.showLIADetailsSection ==true}">
			<table   border="0" cellspacing="5" cellpadding="0"><tr><td  class="redTextBold">
				<content:getAttribute  beanName="partcipantApplicableToLiaMessage" attribute="text"/></td></tr>
			</table>
</c:if>
<c:if test="${empty param.printFriendly}">
        &nbsp;
<a href="/do/participant/participantAccount/?profileId=${e:forHtmlAttribute(participantBenefitBaseInformationForm.profileId)}">
			Participant Account </a>
			<br>
        &nbsp;
        <a href="/do/participant/participantSummary"> Participant
			Summary </a>
			<br>
        &nbsp;
<a href="/do/transaction/participantTransactionHistory/?profileId=${e:forHtmlAttribute(participantBenefitBaseInformationForm.profileId)}">
			Participant Transaction History </a>
			<br>
		</c:if> 
		<%-- error line --%>
		&nbsp;
		<content:errors scope="session" />
		&nbsp;
		 
<%-- technical difficulties --%>
<c:if test="${empty technicalDifficulties}">

			<%-- Start Benefit Base information --%>

			<ps:form cssClass="margin-bottom:0;" method="POST" modelAttribute="participantBenefitBaseInformationForm" name="participantBenefitBaseInformationForm"
				action="/do/participant/participantBenefitBaseInformation/">

<input type="hidden" name="task" value="default"/>
<form:hidden path="profileId"/>
			 
				<table width="707" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="1"></td>
						<td width="256"></td>
						<td width="449"></td>
						<td width="1"></td>
					</tr>
					<tr class="tablehead">
						<td class="tableheadTD1" colspan="9"><b>
						<content:getAttribute id="layoutPageBean" attribute="body1Header"/> </b>as of <render:date
							patternOut="<%= RenderConstants.EXTRA_LONG_MDY %>"
							property="participantBenefitBaseInformationForm.asOfDate" /></td>
					</tr>

					<tr>
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="256" align="left" valign="top" class="datacell1">
						<table width="256" border="0" cellpadding="2" cellspacing="0">
							<tr id="participantName">
								<td width="126" valign="top"><strong>Name:</strong></td>
<td width="130">${benefitDetails.name}
								</td>
							</tr>
							<tr>
								<td valign="top"><strong>SSN:</strong></td>
								<td><render:ssn property="benefitDetails.ssn" /></td>
							</tr>
<c:if test="${not empty benefitDetails.dateOfBirth}">
							<tr>
								<td valign="top"><strong>Date of birth:</strong></td>
								<td><render:date property="benefitDetails.dateOfBirth"
									patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" /></td>
							</tr>
</c:if>
							<tr>
<c:if test="${participantBenefitBaseInformationForm.showFootnote == 'Y'}">
									<td valign="top"><strong>Benefit Base*:</strong></td>
</c:if>
<c:if test="${participantBenefitBaseInformationForm.showFootnote == 'N'}">
									<td valign="top"><strong>Benefit Base:</strong></td>
</c:if>
								<td><render:number
									property="giflDetails.giflBenefitBaseAmt" type="c" /></td>
							</tr>
							<tr>
								<td valign="top"><strong>Market value:</strong></td>
								<td>
<c:if test="${giflDetails.displayGiflDeselectionDate != Date}">
									<render:number value="0" type="c" />
</c:if>
									
<c:if test="${giflDetails.displayGiflDeselectionDate == Date}">
									<render:number
										property="benefitDetails.marketValueGoFunds" type="c" />
</c:if>
								</td>
							</tr>
						</table>
						</td>

						<td width="449" align="left" valign="top" class="datacell1">
						<table border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td width="321"><strong>Selection date:</strong></td>
								<td width="128" align="left"><render:date
									property="giflDetails.giflSelectionDate"
									patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" /></td>
							</tr>
<c:if test="${giflDetails.displayGiflDeselectionDate != Date}">
							<tr>
							<td><strong>Deactivation date:</strong></td>
							<td align="left"><render:date
									property="giflDetails.giflDeselectionDate"
									patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" />
							</td>
							</tr>
</c:if>
							<tr>
								<td><strong>Activation date:</strong></td>
								<td align="left">
${giflDetails.displayGiflActivationDate}
								</td>
							</tr>
							<!-- This should be hide for valid LIA anniversary date -->
<c:if test="${participantBenefitBaseInformationForm.showLIADetailsSection ==false}">
								<tr>
									<td><strong>Anniversary date:</strong></td>
									<td align="left">
${giflDetails.displayGiflNextStepUpDate}
									</td>
								</tr>
</c:if>
							<tr>
								<td nowrap="nowrap"><strong>Minimum holding period expiry date:</strong></td>
								<td align="left">
${giflDetails.displayGiflHoldingPeriodExpDate}
								</td>
							</tr>
							<%-- Trading Expiration Date & Rate for Last Income Enhancement Fields will be displayed only if the contract has GIFL version 3 --%>
<c:if test="${userProfile.contractProfile.contract.hasContractGatewayInd == true}">
							
									<%-- Trading Expiration Date will only be displayed  if the trade expiration is in effect and this date should be after GIFL selection activation date--%>
<c:if test="${participantBenefitBaseInformationForm.showTradingExpirationDate == 'Y'}">
										<tr>
											<td><strong>Trading restriction expiry date:</strong></td>
											<td align="left">
${participantBenefitBaseInformationForm.displayTradingExpirationDate}
											</td>
										</tr>	
</c:if>
<c:if test="${userProfile.contractProfile.contract.giflVersion == 'G03'}">
								<tr>
									<td><strong>Rate for last Income Enhancement:</strong></td>
<td align="left">${giflDetails.displayRateForLastIncomeEnhancement}</td>
								</tr>	
</c:if>
</c:if>

							<tr>
								<td>
								<%-- Last Income Enhancement will be displayed only if the GIFL version is 3 else Last Step-Up Date will be displayed--%>
<c:if test="${userProfile.contractProfile.contract.hasContractGatewayInd == true}">
<c:if test="${userProfile.contractProfile.contract.giflVersion == 'G03'}">
											<strong>Last Income Enhancement date:</strong>
</c:if>
<c:if test="${userProfile.contractProfile.contract.giflVersion != 'G03'}">
											<strong>Last Step-Up date:</strong>
</c:if>
</c:if>
								</td>
								<td align="left">
${giflDetails.displayGiflLastStepUpDate}
								</td>
							</tr>	
							<tr>
								<td nowrap="nowrap">
								<%-- "Value changed at last Income Enhancement date" will be displayed only if the GIFL version is 3 else "Value changed at last Step-Up date" will be displayed--%>								
<c:if test="${userProfile.contractProfile.contract.hasContractGatewayInd == true}">
<c:if test="${userProfile.contractProfile.contract.giflVersion == 'G03'}">
												<strong>Value changed at last Income Enhancement:</strong></td>
</c:if>
<c:if test="${userProfile.contractProfile.contract.giflVersion != 'G03'}">
											<strong>Value changed at last Step-Up:</strong></td>
</c:if>
</c:if>
								<td>
<c:if test="${giflDetails.displayGiflLastStepUpChangeAmt != NA}">
									<render:number
										property="giflDetails.giflLastStepUpChangeAmt" type="c" />
</c:if>
<c:if test="${giflDetails.displayGiflLastStepUpChangeAmt == NA}">
${giflDetails.displayGiflLastStepUpChangeAmt}
</c:if>
								</td>
							</tr>
						</table>
						</td>
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					<tr>
						<td class="databorder" colspan="4"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
				</table>

				<br>
				<%-- End Account information --%>
				
				<%-- Start LIA information --%>
<c:if test="${participantBenefitBaseInformationForm.showLIADetailsSection ==true}">
				<br>
				<table width="720" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="1"></td>
						<td width="353"></td>
						<td width="352"></td>
						<td width="1"></td>
					</tr>
					<tr class="tablehead">
						<td class="tableheadTD1" colspan="9"><b>
							<content:getAttribute id="layoutPageBean" attribute="body2Header"/> </b>
						</td>
					</tr>

					<tr>
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="353" align="left" valign="top" class="datacell1">
						<table width="353" border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td width="200" valign="top">
								<strong><content:getAttribute id="liaSelectionDateFieldLabel"
										attribute="text" /></strong></td>
								<td width="150" align="left"><render:date
									property="participantBenefitBaseInformationForm.liaSelectionDate"
									patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" /></td>
							</tr>
							<tr>
								<td width="200" valign="top">
								<strong><content:getAttribute id="spousalOptionFieldLabel"
										attribute="text" /></strong></td>
<td width="150">${participantBenefitBaseInformationForm.liaIndividualOrSpousalOption}
								</td>
							</tr>
							<tr>
								<td width="200" valign="top">
								<strong><content:getAttribute id="liaPercentageFieldLabel"
										attribute="text" /></strong></td>
<td width="150">${participantBenefitBaseInformationForm.liaPercentage}
								</td>
							</tr>
						</table>
						</td>

						<td width="352" align="left" valign="top" class="datacell1">
						<table border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td width="170" valign="top">
								<strong><content:getAttribute id="annualLIAAmountFieldLabel"
										attribute="text" /></strong></td>
<td width="170"><c:if test="${not empty participantBenefitBaseInformationForm.liaAnnualAmount}"><render:number
										property="participantBenefitBaseInformationForm.liaAnnualAmount" type="c" /></c:if>
								</td>
							</tr>
							<tr>
								<td width="170" valign="top">
								<strong><content:getAttribute id="paymentFrequencyFieldLabel"
										attribute="text" /></strong></td>
<td width="170">${participantBenefitBaseInformationForm.liaFrequencyCode}
<c:if test="${not empty participantBenefitBaseInformationForm.liaPeriodicAmt}"> - <render:number
												property="participantBenefitBaseInformationForm.liaPeriodicAmt" type="c" /></c:if>
								</td>
							</tr>
							<tr>
								<td width="170" valign="top">
								<strong><content:getAttribute id="liaAnniversaryDateFieldLabel"
										attribute="text" /></strong></td>
								<td width="170"><render:date
									property="participantBenefitBaseInformationForm.liaAnniversaryDate"
									patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" />
								</td>
							</tr>
						</table>
						</td>
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					<tr>
						<td class="databorder" colspan="4"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
				</table>
				<br>
</c:if>
				<%-- End LIA information --%>	
<c:if test="${empty Constants.OUT_OF_SERVICE_HOURS}">
					<%-- Benefit base transaction details --%>
					<br>
					<table width="720" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>

							<c:if test="${empty param.printFriendly }" >

								<td width="70"><img src="/assets/unmanaged/images/s.gif"
									width="70" height="1"></td>
							</c:if>

							<c:if test="${not empty param.printFriendly }" >
								<td width="50"><img src="/assets/unmanaged/images/s.gif"
									width="50" height="1"></td>
							</c:if>

							<td width="1"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>

							<c:if test="${empty param.printFriendly }" >
								<td width="70"><img src="/assets/unmanaged/images/s.gif"
									width="70" height="1"></td>
							</c:if>

							<c:if test="${not empty param.printFriendly }" >
								<td width="70"><img src="/assets/unmanaged/images/s.gif"
									width="70" height="1"></td>
							</c:if>

							<td width="1"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>
							<td width="70"><img src="/assets/unmanaged/images/s.gif"
								width="70" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>
							<td width="70"><img src="/assets/unmanaged/images/s.gif"
								width="70" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>
							<td width="70"><img src="/assets/unmanaged/images/s.gif"
								width="70" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>
							<td width="70"><img src="/assets/unmanaged/images/s.gif"
								width="70" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>
							<td width="70"><img src="/assets/unmanaged/images/s.gif"
								width="70" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>
							<td width="70"><img src="/assets/unmanaged/images/s.gif"
								width="70" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>
							<td width="40"><img src="/assets/unmanaged/images/s.gif"
								width="40" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>
						</tr>

					<c:if test="${empty theReport}">
								<tr>
									<td colspan="19">
									<table width="720" border="0" cellspacing="0" cellpadding="0">
										<tr class="tablehead">
											<td class="tableheadTD1" valign="middle" width="381"><span
												class=""> <b><content:getAttribute id="layoutPageBean" attribute="body3Header"/></b>&nbsp;from&nbsp;
												<c:if test="${ not empty param.printFriendly}"> 
												<input readonly type="text" name="fromDate" size="10" maxlength="10" 
value="${e:forHtmlAttribute(participantBenefitBaseInformationForm.fromDate)}" class="inputAmount">

											</c:if> <c:if test="${empty param.printFriendly}" >
												<input type="text" name="fromDate" size="10" maxlength="10" tabindex="30"
value="${e:forHtmlAttribute(participantBenefitBaseInformationForm.fromDate)}" class="inputAmount">&nbsp;

								   <a href="javascript:calFromDate.popup();" > <img
													src="/assets/unmanaged/images/cal.gif" width="15" 
													height="15" border="0" tabindex="31"
													alt="Use the Calendar to pick the date"> </a>
											</c:if> &nbsp;&nbsp;to&nbsp; <c:if test="${ not empty param.printFriendly}"> 
												<input readonly  type="text" name="toDate" size="10" maxlength="10"
value="${e:forHtmlAttribute(participantBenefitBaseInformationForm.toDate)}" class="inputAmount">

											</c:if> <c:if test="${empty param.printFriendly }" >
												<input type="text" name="toDate" size="10" maxlength="10"  tabindex="32"
value="${e:forHtmlAttribute(participantBenefitBaseInformationForm.toDate)}" class="inputAmount">&nbsp;

                    			<a href="javascript:calToDate.popup();"> <img
													src="/assets/unmanaged/images/cal.gif" width="15"
													height="15" border="0" tabindex="33"
													alt="Use the Calendar to pick the date"> </a>
											</c:if> <br>
											<span class="disclaimer"> <c:if test="${ empty param.printFriendly}"> 
												<img src="/assets/unmanaged/images/s.gif" width="115"
													height="1">
                      (mm/dd/yyyy)
                      <img src="/assets/unmanaged/images/s.gif"
													width="55" height="1">
                      (mm/dd/yyyy)
                      </c:if> <c:if test="${not empty param.printFriendly }" >
												<img src="/assets/unmanaged/images/s.gif" width="114"
													height="1">
                      (mm/dd/yyyy)
                      <img src="/assets/unmanaged/images/s.gif"
													width="32" height="1">
                      (mm/dd/yyyy)
                      </c:if> </span></td>
											<td class="tableheadTD" width="95"><c:if test="${empty param.printFriendly}"> 
<input type="button" onclick="submitDates(); return false;" name="submit" tabindex="34" value="search"/>


											</c:if></td>
											<td class="tableheadTD" width="224"></td>
										</tr>
									</table>
									</td>
								</tr>
							</table>
							</c:if>
					
		<c:if test="${ not empty theReport}"> 
			<tr>
<c:if test="${userProfile.internalUser ==true}">
				<td colspan="19">
</c:if>
<c:if test="${userProfile.internalUser !=true}">
				<td colspan="17">
</c:if>
				<table border="0" cellspacing="0" cellpadding="0">
					<tr class="tablehead">
<c:if test="${userProfile.internalUser ==true}">
							<td class="tableheadTD1" valign="middle" width="381">
</c:if>
<c:if test="${userProfile.internalUser !=true}">
							<td class="tableheadTD1" valign="middle" width="475">
</c:if>
						<span class=""> <b><content:getAttribute id="layoutPageBean" attribute="body3Header"/> </b>from&nbsp; <c:if test="${not empty param.printFriendly}" >
							<input readonly="readonly" type="text" name="fromDate" size="10" maxlength = "10"
value="${e:forHtmlAttribute(participantBenefitBaseInformationForm.fromDate)}" class="inputAmount">

						</c:if> <c:if test="${empty param.printFriendly }" >
							<input type="text" name="fromDate" size="10" maxlength = "10" 
value="${e:forHtmlAttribute(participantBenefitBaseInformationForm.fromDate)}" class="inputAmount">&nbsp;

                   <a href="javascript:calFromDate.popup();"> <img
								src="/assets/unmanaged/images/cal.gif" width="15" height="15"
								border="0" alt="Use the Calendar to pick the date"></a>
						</c:if> &nbsp;&nbsp;to&nbsp; <c:if test="${not empty param.printFriendly }" >
							<input readonly="readonly" type="text" name="toDate" size="10" maxlength = "10"
value="${e:forHtmlAttribute(participantBenefitBaseInformationForm.toDate)}" class="inputAmount">

						</c:if> <c:if test="${empty param.printFriendly }" >
							<input type="text" name="toDate" size="10" maxlength = "10" 
value="${e:forHtmlAttribute(participantBenefitBaseInformationForm.toDate)}" class="inputAmount">&nbsp;

                  <a href="javascript:calToDate.popup();"> <img
								src="/assets/unmanaged/images/cal.gif" width="15" height="15"
								border="0" alt="Use the Calendar to pick the date"> </a>
						</c:if> <br>
						<span class="disclaimer"> <c:if test="${empty param.printFriendly }" >							<img src="/assets/unmanaged/images/s.gif" width="115" height="1">
                      (mm/dd/yyyy)
              <img src="/assets/unmanaged/images/s.gif"	width="55" height="1">
                      (mm/dd/yyyy)
              </c:if> <c:if test="${not empty param.printFriendly }" >
							<img src="/assets/unmanaged/images/s.gif" width="114" height="1">
                      (mm/dd/yyyy)
              <img src="/assets/unmanaged/images/s.gif"	width="32" height="1">
                      (mm/dd/yyyy)
                      </c:if> </span></td>
					   <td class="tableheadTD" width="80"><c:if test="${empty param.printFriendly }" >
<input type="button" onclick="submitFilter(); return false;" name="submit1" value="search"/>

						</c:if></td>
						
						<td class="tableheadTD" width="190" valign="middle"><b><report:recordCounter
							report="theReport" label="Transactions" /></b></td>
						<td class="tableheadTD" width="90" valign="bottom" align="right">
						<report:pageCounter  formName="participantBenefitBaseInformationForm" report="theReport" /></td>
					</tr>
				</table>
				</td>
			</tr>

			<%-- Detail column header row  --%>
			<tr class="tablesubhead">
				<td class="databorder"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

				<%-- Transaction effective date --%>
				<td align="left" valign="top"><report:sort field="<%=ParticipantBenefitBaseInformationReportData.SORT_FIELD_EFFECTIVE_DATE %>" direction="asc" formName="participantBenefitBaseInformationForm">
					<strong> Transaction effective date</strong>
				</report:sort></td>
				<td class="dataheaddivider"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

				<%-- Associated transaction number --%>
				<td align="left" valign="top"><strong> Associated transaction number</strong></td>
				<td class="dataheaddivider"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

				<%-- Transaction type --%>
				<td align="left" valign="top"  nowrap="nowrap"><strong> Transaction type</strong></td>
				<td class="dataheaddivider"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

				<%-- Market value before transaction ($) --%>
				<td align="left" valign="top"><strong>Market value before transaction ($)</strong></td>
				<td class="dataheaddivider"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

				<%-- Transaction amount($) --%>
				<td align="left" valign="top"><strong> Transaction amount($)</strong></td>
				<td class="dataheaddivider"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

				<%-- Benefit base change ($) --%>
				<td align="left" valign="top"><strong> Benefit base change ($)</strong></td>
				<td class="dataheaddivider"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

				<%-- Resulting benefit base($) --%>
				<td align="left" valign="top"><strong> Resulting benefit base($)</strong></td>
				<td class="dataheaddivider"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

				<%-- MHP reset --%>
<c:if test="${userProfile.internalUser ==true}">
					<td align="left" valign="top"><strong> MHP reset</strong></td>
					<td class="dataheaddivider"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
<c:if test="${userProfile.internalUser !=true}">
					<td align="left" valign="top"><strong> MHP reset</strong></td>
					<td class="databorder"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
				<%-- SIL --%>
<c:if test="${userProfile.internalUser ==true}">
					<td align="left" valign="top"><strong> SIL</strong></td>
					<td width="1" class="databorder"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
			</tr>
			<%-- Message line if there are no detail items --%>
			
<c:if test="${empty theReport.details}">
					<content:contentBean
						contentId="<%=ContentConstants.NO_TRANSACTIONS_MESSAGE_FOR_BENEFIT_BASE_PAGE%>"
						type="<%=ContentConstants.TYPE_MESSAGE%>"
						id="NoTransactionsMessage" />
<c:if test="${userProfile.internalUser ==true}">
						<tr class="datacell1">
							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td colspan="17"><content:getAttribute
								id="NoTransactionsMessage" attribute="text" /></td>
							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
</c:if>
<c:if test="${userProfile.internalUser !=true}">
						<tr class="datacell1">
							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td colspan="15"><content:getAttribute
								id="NoTransactionsMessage" attribute="text" /></td>
							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
</c:if>
					
</c:if>


			<%-- Detail rows --%>
			
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" ><%-- CCAT: Extra attributes for tag c:forEach - type="com.manulife.pension.service.report.participant.transaction.valueobject. ParticipantBenefitBaseInformationDataItem" --%>
 <c:set var="indexValue" value="${theIndex.index}"/> 
				<% 				
				    String temp = pageContext.getAttribute("indexValue").toString();
	
					if (Integer.parseInt(temp) % 2 == 0) {
					%>
					<tr class="datacell1">
						<%
						} else {
						%>
					
					<tr class="datacell2">
						<%
						}
						%>
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<%-- Transaction effective date --%>
						<td align="left" valign="top"><render:date dateStyle="m"
							property="theItem.transactionEffectiveDate" /></td>
						<td class="datadivider"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<%-- Associated transaction number --%>
<td align="left" valign="top"><c:if test="${theItem.transactionNumber !=0}">

${theItem.transactionNumber}
</c:if> <c:if test="${theItem.transactionNumber ==0}">

</c:if></td>
						<td class="datadivider"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<%-- Transaction type --%>
<td align="left" valign="top" nowrap="nowrap">${theItem.transactionType}</td>

						<td class="datadivider"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<%-- Market value before transaction ($) --%>
<td align="right" valign="top">${theItem.marketValueBeforeTransaction}</td>

						<td class="datadivider"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<%-- Transaction amount($) --%>
<td align="right" valign="top">${theItem.transactionAmount}</td>

						<td class="datadivider"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<%-- Benefit base change ($) --%>
<td align="right" valign="top">${theItem.benefitBaseChangeAmount}</td>

						<td class="datadivider"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<%-- Resulting benefit base($) --%>
						<td align="right" valign="top"><render:number
								property="theItem.benefitBaseAmount" type="d" /></td>
						<td class="datadivider"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<%-- MHP reset --%>
<c:if test="${userProfile.internalUser ==true}">
<td align="left" valign="top">${theItem.holdingPeriodInd}</td>

							<td class="datadivider"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
<c:if test="${userProfile.internalUser !=true}">
<td align="left" valign="top">${theItem.holdingPeriodInd}</td>

							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
						<%-- SIL --%>
<c:if test="${userProfile.internalUser ==true}">
<td align="left" valign="top">${theItem.validatedSILNumber}</td>

							<td width="1" class="databorder"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
					</tr>
</c:forEach>

			
</c:if>
<c:if test="${userProfile.internalUser ==true}">
				<td class="databorder" colspan="20"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<tr>
					<td colspan="18" align="right"><report:pageCounter formName="participantBenefitBaseInformationForm"
							report="theReport" arrowColor="black" /></td>
				</tr>
</c:if>
<c:if test="${userProfile.internalUser !=true}">
				<td class="databorder" colspan="16"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<tr>
					<td colspan="16" align="right"><report:pageCounter  formName="participantBenefitBaseInformationForm"
							report="theReport" arrowColor="black" /></td>
				</tr>
</c:if>
</table>
<br/>
<br/>
	</c:if> <%-- Report Bean --%>

</c:if> <%-- Out of Service error --%>

<c:if test="${participantBenefitBaseInformationForm.showFootnote == 'Y'}">
	<content:contentBean
		contentId="<%=ContentHelper.getContentIdByVersion(Constants.PSW_BB_FOOTNOTE,userProfile)%>"
		type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="benefitBaseFootnote" />
		<p class="footnote"><content:getAttribute id="benefitBaseFootnote" attribute="text" /></p>
</c:if>

<p><content:pageFooter beanName="layoutPageBean" /></p>
<p class="footnote"><content:pageFootnotes beanName="layoutPageBean" /></p>
<p class="disclaimer"><content:pageDisclaimer
	beanName="layoutPageBean" index="-1" /></p>
</ps:form>
<c:if test="${empty Constants.OUT_OF_SERVICE_HOURS}" >

<script type="text/javascript" >
	  <!--  create calendar object(s) just after form tag closed
        var calFromDate = new calendar(document.forms['participantBenefitBaseInformationForm'].elements['fromDate']);
        calFromDate.year_scroll = true;
        calFromDate.time_comp = false;
        var calToDate = new calendar(document.forms['participantBenefitBaseInformationForm'].elements['toDate']);
        calToDate.year_scroll = true;
        calToDate.time_comp = false;
        
        -->
 </script>
 
</c:if> <%-- Out of Service error --%>
 
</c:if>  
<%-- technical difficulties --%>
</td>
</tr>
</table>

<c:if test="${not empty param.printFriendly }" >
<br/>
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute id="globalDisclosure"
				attribute="text" /></td>
		</tr>
	</table>
</c:if>
