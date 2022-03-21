<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.CompletedWithdrawalDetailsReportData"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalMoneyTypeVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalCalculatedInfoVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

	<%-- Beans used --%>
	
<%
CompletedWithdrawalDetailsReportData theReport = (CompletedWithdrawalDetailsReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
String VERSION_1 =BDConstants.VERSION_1;
pageContext.setAttribute("VERSION_1",VERSION_1,PageContext.PAGE_SCOPE);
String YES = BDConstants.YES;
pageContext.setAttribute("YES",YES,PageContext.PAGE_SCOPE);
%> 




<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>


<c:if test="${transactionDetailsWithdrawalForm.showLiaWithdrawalNotification ==true}">
		<content:contentBean
			contentId="<%=BDContentConstants.MESSAGE_WITHDRAWAL_PARTICPANT_APPLICABLE_TO_LIA%>"
			type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="liaParticipantNotify" />
</c:if>
	<content:contentBean
		contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="csvIcon" />
	<content:contentBean
		contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="pdfIcon" />

	<%-- This jsp includes the following CMA content --%>
	<content:contentBean
		contentId="<%=BDContentConstants.MESSAGE_LOAN_DEFAULT_NOTIFICATION%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
		id="LoanDefaultNotify" />
	
	<content:contentBean
		contentId="<%=BDContentConstants.MISCELLANEOUS_PBA_NOTIFICATION%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
		id="pbaNotification" />
		<c:if test="${not empty theReport}">
<!--  Section 3:Withdrawal Information  -->
<c:forEach items="${theReport.details}" var="withdrawalDetailsItem" varStatus="theIndex" >


		<div class="page_section_subheader_wd">
			<!--  Section 3:Section Header  -->
			<h3>
				<content:getAttribute id="layoutPageBean"
				attribute="body2Header" />
<input type="hidden" name="pdfCapped"/>
				<a href="javascript://"
				onClick="doPrintPDF()" class="pdf_icon"
				title="<content:getAttribute beanName="pdfIcon" attribute="text"/>">
					<content:image contentfile="image" id="pdfIcon" />
				 </a>
				 <a	href="javascript://" onClick="doDownloadCSV();return false;"
				 class="csv_icon" title="<content:getAttribute beanName="csvIcon"  attribute="text"/>">
					<content:image contentfile="image" id="csvIcon" /> </a>
			</h3>
			
			
				
		
		</div>
		
		<!--  Section 3:Loan Default Indication Message  -->
<c:if test="${theReport.loanDefaultIndicator == YES}">

			<div id="payees">
				<table id="notification" width="650" align="center" border="0"
				cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<td>
								<span class="notice">Notification:</span> 
									<content:getAttribute
									beanName="LoanDefaultNotify" attribute="text" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
</c:if>
		
		<!--  Section 3: PBA Notification message -->
<c:if test="${theReport.pbaDisbursementIndicator == PBA_DISBURSEMENT_INDICATOR }"> 

			<div id="payees">
				<table id="notification" width="650" align="center" border="0"
				cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<td>
								<span class="notice">Notification:</span> 
									<content:getAttribute
									beanName="pbaNotification" attribute="text" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
</c:if>
		
		<!--  Section 3: LIA Notification message -->
<c:if test="${transactionDetailsWithdrawalForm.showLiaWithdrawalNotification ==true}">
					<div id="payees">
				<table id="notification" width="650" align="center" border="0"
				cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<td>
								<span class="notice">Notification:</span> 
									<content:getAttribute
									beanName="liaParticipantNotify" attribute="text" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
</c:if>
<c:if test="${withdrawalDetailsItem.withdrawalGeneralInfoVO.rothStartedYears !=9999}">

									<p> 1st Year of Designated Roth Contributions : 
${withdrawalDetailsItem.withdrawalGeneralInfoVO.rothStartedYears}

										</p>
</c:if>
<c:if test="${withdrawalDetailsItem.withdrawalGeneralInfoVO.pre87Present ==true}">

									<p>Pre-87 After Tax 	Employee Contributions Withdrawn:<render:number
										property="withdrawalDetailsItem.withdrawalGeneralInfoVO.pre87Amount"
										type="c" sign="true" />
									</p>
				
</c:if>
	
		<!--  Section 3:Money Type Section -->
		<div class="report_table">
			<table id="participants_table" class="report_table_content"
			width="100%">
<c:if test="${theReport.webDisplayVersion != VERSION_1}">

					<thead>
						<tr class="val_str">
							<th class="val_str align_center" width="40%">
								Money Type
							</th>
							<th class="val_str align_center" width="30%">
								&nbsp;
							</th>
							<th class="val_str align_center" width="30%">
								Withdrawal Amount($)
							</th>
						</tr>
					</thead>
</c:if>
	
<c:if test="${theReport.webDisplayVersion == VERSION_1}">
					<thead>
						<tr class="val_str">
							<th class="val_str align_center" width="40%">
								Money Type
							</th>
							<th class="val_str align_center" width="20%">
								Account	Balance($)
							</th>
							<th class="val_str align_center" width="20%">
								Vesting(%)
							</th>
							<th class="val_str align_center" width="20%">
								Available Amount($)
							</th>
						</tr>
					</thead>
</c:if>
	
				<tbody>
<c:if test="${not empty withdrawalDetailsItem.withdrawalMoneyTypeVO}">

<c:if test="${theReport.webDisplayVersion != VERSION_1}">

<c:forEach items="${withdrawalDetailsItem.withdrawalMoneyTypeVO}" var="moneyType" >


	
								<tr class="spec">
									<td class="name">
${moneyType.moneyType}

									</td>
									<td class="datacell1" align="right" valign="top">
										<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
									</td>
									<td class="cur">
									<c:if test="${not empty moneyType.availableAmount}">
										<render:number	property="moneyType.availableAmount" 
										type="c" sign="false" />
</c:if>
									</td>
								</tr>
</c:forEach>
</c:if>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

<c:forEach items="${withdrawalDetailsItem.withdrawalMoneyTypeVO}" var="moneyType" >


								<tr class="spec">
									<td class="name">
${moneyType.moneyType}

									</td>
									<td class="cur">
									<c:if test="${not empty moneyType.accountBalance}">
										<render:number	property="moneyType.accountBalance" 
										type="c" sign="false" />
</c:if>
									</td>
									<td class="datacell1" align="right" valign="top">
									<c:if test="${not empty moneyType.vestingPercentage}">
										<render:number property="moneyType.vestingPercentage" scale="3" pattern="0.000"/>
</c:if>
									</td>
									<td class="cur">
									<c:if test="${not empty moneyType.availableAmount}">
										<render:number property="moneyType.availableAmount" type="c" sign="false" />
</c:if>
									</td>
								</tr>
</c:forEach>
</c:if>
</c:if>
					
					<!--  Section 3: Blank money type row display -->
						<tr class="spec">
                    		<td class="name">
                    			&nbsp;
                    		</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

                   		    <td class="name">
                   		    	<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
                   		    </td>
</c:if>
                    		<td class="datacell1" align="right" valign="top">
                    			<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
                    		</td>
                  	   		<td class="cur">
                  	   			<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
                  	   		</td>
                 		</tr>
	
<c:if test="${not empty theReport.withdrawalCalculatedInfoVO}">

						<tr class="name">
								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
<c:if test="${theReport.webDisplayVersion != VERSION_1}">

								<td class="datacell1" align="right" valign="top">
									<strong>Total Withdrawal Amount</strong>
								</td>
</c:if>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="datacell1" align="right" valign="top">
									<strong>Total Available Amount</strong>
								</td>
</c:if>
								<td class="cur">
									<render:number property="theReport.withdrawalCalculatedInfoVO.totalAmount"
									type="c" sign="false" />
								</td>
						</tr>
						<tr class="spec">
								<td class="name">
									&nbsp;
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
<c:if test="${theReport.withdrawalCalculatedInfoVO.mvaAppliesIndicator == YES }" > 


								<td class="datacell1" align="right" valign="top">
									Market Value Adjustment(MVA)
								</td>
								<td class="cur">
									<render:number
									property="theReport.withdrawalCalculatedInfoVO.mvaAmount"
									type="c" sign="false" />
								</td>
</c:if>
<c:if test="${theReport.withdrawalCalculatedInfoVO.mvaAppliesIndicator ==N}">


								<td class="datacell1" align="right" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<td class="cur">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
						</tr>
<c:if test="${not empty theReport.withdrawalCalculatedInfoVO.fundsOnDepositInterest}">

						<tr class="spec">
								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
								<td class="datacell1" align="right" valign="top">
									Funds On Deposit Interest
								</td>
								<td class="cur">
									<render:number
									property="theReport.withdrawalCalculatedInfoVO.fundsOnDepositInterest"
									type="c" sign="false" />
								</td>
						</tr>
</c:if>
	
<c:if test="${bobContext.currentContract.definedBenefitContract ==false}">

						<tr class="name">
								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
								<td class="datacell1" align="right" valign="top">
									Taxable	Amount
								</td>
								<td class="cur">
									<render:number
									property="theReport.withdrawalCalculatedInfoVO.taxableAmount"
									type="c" sign="false" />
								</td>
						</tr>
						<tr class="spec">
								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
								<td class="datacell1" align="right" valign="top">
									State Tax
								</td>
								<td class="cur">
									<render:number
									property="theReport.withdrawalCalculatedInfoVO.stateTaxAmount"
									type="c" sign="false" />
								</td>
						</tr>
						<tr class="name">
								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
								<td class="datacell1" align="right" valign="top">
									Federal Tax
								</td>
								<td class="cur">
									<render:number
									property="theReport.withdrawalCalculatedInfoVO.federalTaxAmount"
									type="c" sign="false" />
								</td>
						</tr>
<c:if test="${theReport.withdrawalCalculatedInfoVO.rothMoneyIndicator ==Y}">

						<tr class="spec">
								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
								<td class="datacell1" align="right" valign="top">
									Taxable	Amount - ROTH
								</td>
								<td class="cur">
									<render:number
									property="theReport.withdrawalCalculatedInfoVO.rothTaxableAmount"
									type="c" sign="false" />
								</td>
						</tr>
						<tr class="name">
	
								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
								<td class="datacell1" align="right" valign="top">
									State Tax -	ROTH
								</td>
								<td class="cur">
									<render:number
									property="theReport.withdrawalCalculatedInfoVO.rothStateTaxAmount"
									type="c" sign="false" />
								</td>
						</tr>
						<tr class="spec">
								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
								<td class="datacell1" align="right" valign="top">
									Federal Tax - ROTH
								</td>
								<td class="cur">
									<render:number
									property="theReport.withdrawalCalculatedInfoVO.rothFederalTaxAmount"
									type="c" sign="false" />
								</td>
						</tr>
</c:if>
</c:if>
						<tr class="name">
								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
								<td class="datacell1" align="right" valign="top">
									<strong>Total Payment Amount</strong>
								</td>
								<td class="cur">
									<render:number
									property="theReport.withdrawalCalculatedInfoVO.totalPaymentAmount"
									type="c" sign="false" />
								</td>
						</tr>
						
<c:if test="${not empty theReport.withdrawalCalculatedInfoVO.forfeitedOrUMText1}">
<c:if test="${not empty theReport.withdrawalCalculatedInfoVO.forfeitedOrUMText2}">
						<tr class="name">
								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
<c:if test="${theReport.webDisplayVersion == VERSION_1}">

								<td class="name">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
</c:if>
								<td class="datacell1" align="right" valign="top">
${theReport.withdrawalCalculatedInfoVO.forfeitedOrUMText1ForFRW}<br>
${theReport.withdrawalCalculatedInfoVO.forfeitedOrUMText2ForFRW}
								</td>
								<td class="cur" valign="top">
									<render:number
									property="theReport.withdrawalCalculatedInfoVO.forfeitedOrUnvestedERAmount"
									type="c" sign="false" />
								</td>
						</tr>
</c:if>
</c:if>
</c:if>
				</tbody>
			</table>
		</div>
</c:forEach>

	</c:if>
