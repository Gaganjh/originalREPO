<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalMoneyTypeVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalCalculatedInfoVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.CompletedWithdrawalDetailsReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%-- Beans used --%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
CompletedWithdrawalDetailsReportData theReport = (CompletedWithdrawalDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

String VERSION_1 =Constants.VERSION_1;
pageContext.setAttribute("VERSION_1",VERSION_1,PageContext.PAGE_SCOPE);
String YES = Constants.YES;
pageContext.setAttribute("YES",YES,PageContext.PAGE_SCOPE);

String PBA_DISBURSEMENT_INDICATOR = Constants.PBA_DISBURSEMENT_INDICATOR;
pageContext.setAttribute("YES",YES,PageContext.PAGE_SCOPE);

%>




<c:if test="${transactionDetailsWithdrawalForm.showLiaWithdrawalNotification ==true}">
	<content:contentBean
		contentId="<%=ContentConstants.MESSAGE_WITHDRAWAL_PARTICPANT_APPLICABLE_TO_LIA%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="liaParticipantNotify" />
</c:if>

<%-- This jsp includes the following CMA content --%>
<content:contentBean
contentId="<%=ContentConstants.LOAN_DEFAULT_NOTIFICATION%>"
type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="LoanDefaultNotify" />

<content:contentBean
contentId="<%=ContentConstants.MISCELLANEOUS_PBA_NOTIFICATION%>"
type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="pbaNotification" />



<!--  Section 3:Withdrawal Information  -->
<c:forEach items="${theReport.details}" var="withdrawalDetailsItem" varStatus="theIndex" >


	
	<!--  Section 3:Section Title -->
	<tr class="datacell1">
		<td colspan="8" class="tableheadTD" style="padding: 3px;" valign="top">
			<strong>
				<content:getAttribute beanName="layoutPageBean" attribute="body3Header" />
			</strong>
		</td>
	</tr>
	<tr class="datacell1">
		<td colspan="8" class="whiteBox" valign="top">
			<br />
			<!--  Section 3:Loan Default notification Message-->
			<!--  Check for loan default indicator and display notification message -->
	<c:if test="${theReport.loanDefaultIndicator == YES }">
				<table id="notification" width="650" align="center" border="0"cellpadding="3">
				<tbody>
					<tr>
						<td>
							<span class="notice">Notification:</span>
							<content:getAttribute beanName="LoanDefaultNotify" attribute="text" />
						</td>
					</tr>
				</tbody>
				</table>
				<br/>
	</c:if>
			
			
			<!--  Section 3:PBA Default notification Message-->
			<!--  Check for PBA Disbursement indicator and display notification message -->
	<c:if test="${theReport.pbaDisbursementIndicator == PBA_DISBURSEMENT_INDICATOR}">
				<table id="notification" width="650" align="center" border="0"cellpadding="3">
				<tbody>
					<tr>
						<td>
							<span class="notice">Notification:</span>
							<content:getAttribute beanName="pbaNotification" attribute="text" />
						</td>
					</tr>
				</tbody>
				</table>
				<br/>
	</c:if>
			
			
			<!--  Section 3:LIA Default notification Message-->
			<!--  Check for participant applicable to LIA and display notification message -->
	<c:if test="${transactionDetailsWithdrawalForm.showLiaWithdrawalNotification ==true}">
					<table id="notification" width="650" align="center" border="0"cellpadding="3">
						<tbody>
							<tr>
								<td>
									<span class="notice">Notification:</span>
									<content:getAttribute beanName="liaParticipantNotify" attribute="text" />
								</td>
							</tr>
						</tbody>
					</table>
				<br/>
				<br/> 
	</c:if>
			
	<c:if test="${withdrawalDetailsItem.withdrawalGeneralInfoVO.rothStartedYears !=9999}">

				<div style="padding-left: 20px;">
					<label class="col-sm-4 control-label">1st Year of Designated Roth Contributions : </label>
		${withdrawalDetailsItem.withdrawalGeneralInfoVO.rothStartedYears}

				</div>
	</c:if>
	<c:if test="${withdrawalDetailsItem.withdrawalGeneralInfoVO.pre87Present ==true}">

				<div style="padding-left: 20px;">
					<label class="col-sm-4 control-label">Pre-87 After Tax 	Employee Contributions Withdrawn: </label>
					<render:number
						property="withdrawalDetailsItem.withdrawalGeneralInfoVO.pre87Amount"
						type="c" sign="true" />

				</div>
 		<br />
	</c:if>
		<br /> <!--  Section 3:Money Type Section -->
			<table width="600" align="center" border="0" cellpadding="0" cellspacing="0">
			<tbody>
				<!--  Section 3:Money Type Table Heading-->
				<tr class="tablesubhead">
					<td rowspan="35" class="greyborder" width="1">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td width="241" align="left" height="45">
						<b>Money type</b>
					</td>
					<td class="dataheaddivider" width="1">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					
	<c:if test="${theReport.webDisplayVersion != VERSION_1}">					<td width="162" align="right">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="dataheaddivider" width="1" align="right">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td width="191" align="right">
						<b>Withdrawal amount($)</b>
					</td>
					<td rowspan="35" class="greyborder" width="1" align="right">
							<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
	</c:if>
	
	<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td width="132" align="right">
						<b>Account balance($)</b>
					</td>
					<td class="dataheaddivider" width="1" align="right">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td width="187" align="right">
						<b>Vesting(%)</b>
					</td>
					<td class="dataheaddivider" width="1" align="right">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td width="135" align="right">
						<b>Available amount($)</b>
					</td>
					<td rowspan="35" class="greyborder" width="1" align="right">
							<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
	</c:if>
				</tr>
				
	<c:if test="${not empty withdrawalDetailsItem.withdrawalMoneyTypeVO}">
	<c:forEach items="${withdrawalDetailsItem.withdrawalMoneyTypeVO}" var="moneyType" varStatus="index" >



				<!--  Section 3:Checking Index value and set datacell1 and datacell2 for alternative row color for money type-->
				 <c:set var="rowId" value="${index.index % 2}"/> 
				<c:if test="${rowId==0}">
					<c:set var="rowClass" value="datacell1" />
				</c:if>
				<c:if test="${rowId!=0}">
					<c:set var="rowClass" value="datacell2" />
				</c:if>
		
				<tr class="${rowClass}">
					<td   height="10" valign="top">
				${moneyType.moneyType}
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
				
		<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td align="right" valign="top">
		<c:if test="${not empty moneyType.accountBalance}">
							<render:number property="moneyType.accountBalance" type="c" sign="false" />
		</c:if>
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<c:if test="${not empty moneyType.vestingPercentage}">
							<render:number property="moneyType.vestingPercentage"  scale="3" pattern="0.000" type="c" sign="false" />
						</c:if>
					</td>
		</c:if>
				
		<c:if test="${theReport.webDisplayVersion != VERSION_1}">
					<td valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
		</c:if>
				
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
		<c:if test="${not empty moneyType.availableAmount}">
							<render:number property="moneyType.availableAmount" type="c"
							sign="false" />
		</c:if>
					</td>
				</tr>
	</c:forEach>
	</c:if>
			<!--  Checking Index value and set datacell1 and datacell2 for blank money type row color -->
	<c:set var="index" value="${withdrawalDetailsItem.totalNumberOfMoneyTypes}"/>
			<c:set  var="rowId" value="${index%2}" />
			<c:if test="${rowId==0}">
					<c:set var="rowClass" value="datacell1" />
			</c:if>
			<c:if test="${rowId!=0}">
					<c:set var="rowClass" value="datacell2" />
			</c:if>
			
			<!--  Section 3: Blank money type row display -->
				<tr class="${rowClass}">
                	<td  height="10" valign="top">
                		<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
                	</td>
			<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
                	<td class="datadivider">
                		<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
                	</td>
               	    <td valign="top">
               	    	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
               	    </td>
			</c:if>
                	<td class="datadivider">
                		<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
                	</td>
               	    <td valign="top">
               	    	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
               	    </td>
                	<td class="datadivider">
                		<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
                	</td>
               	    <td align="right" valign="top">
               	    	<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
               	    </td>
               </tr>
					
			<c:if test="${not empty theReport.withdrawalCalculatedInfoVO}">
				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datacell1" align="right" valign="top">
						<strong>Total available amount</strong>
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number property="theReport.withdrawalCalculatedInfoVO.totalAmount"
						type="c" sign="false" />
					</td>
			</c:if>
					
			<c:if test="${theReport.webDisplayVersion != VERSION_1}">
					<td class="datacell1" align="right" valign="top">
						<strong>Total withdrawal amount</strong>
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number property="theReport.withdrawalCalculatedInfoVO.totalAmount"
						type="c" sign="false" />
					</td>
			</c:if>
			    </tr>

				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			</c:if>
			<c:if test="${theReport.withdrawalCalculatedInfoVO.mvaAppliesIndicator == YES }">
					<td class="datacell1" align="right" valign="top">
						Market value adjustment(MVA)
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number property="theReport.withdrawalCalculatedInfoVO.mvaAmount"
								type="c" sign="false" />
					</td>
			</c:if>
			<c:if test="${theReport.withdrawalCalculatedInfoVO.mvaAppliesIndicator == YES }">
					<td class="datacell1" align="right" valign="top">
					<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			</c:if>
			    </tr>
			    
			<c:if test="${not empty theReport.withdrawalCalculatedInfoVO.fundsOnDepositInterest}">

				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			</c:if>
					<td class="datacell1" align="right" valign="top">
						Funds on deposit interest
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number
							property="theReport.withdrawalCalculatedInfoVO.fundsOnDepositInterest"
							type="c" sign="false" />
					</td>
				</tr>
			</c:if>
				
			<c:if test="${userProfile.currentContract.definedBenefitContract ==false}">

					
				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
				</c:if>
				
					<td class="datacell1" align="right" valign="top">
						Taxable	amount
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number
							property="theReport.withdrawalCalculatedInfoVO.taxableAmount"
							type="c" sign="false" />
					</td>
				</tr>

				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
		<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
		</c:if>
					<td class="datacell1" align="right" valign="top">
						State tax
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number
							property="theReport.withdrawalCalculatedInfoVO.stateTaxAmount"
							type="c" sign="false" />
					</td>
				</tr>

				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
	<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
		</c:if>
					<td class="datacell1" align="right" valign="top">
						Federal tax
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number
							property="theReport.withdrawalCalculatedInfoVO.federalTaxAmount"
							type="c" sign="false" />
					</td>
				</tr>

		<c:if test="${theReport.withdrawalCalculatedInfoVO.mvaAppliesIndicator == YES }">


				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
		<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
				</c:if>
				
					<td align="right" valign="top">
						Taxable amount - ROTH
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number
							property="theReport.withdrawalCalculatedInfoVO.rothTaxableAmount"
							type="c" sign="false" />
					</td>
				</tr>
					
				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			</c:if>
				
					<td align="right" valign="top">
						State tax - ROTH
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number
							property="theReport.withdrawalCalculatedInfoVO.rothStateTaxAmount"
							type="c" sign="false" />
					</td>
				</tr>
				
				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					</c:if>
				
					<td align="right" valign="top">
						Federal tax - ROTH
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number
							property="theReport.withdrawalCalculatedInfoVO.rothFederalTaxAmount"
							type="c" sign="false" />
					</td>
				</tr>
			</c:if>
				</c:if>
				
				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
				</c:if>
				
					<td class="datacell1" align="right" valign="top">
						<strong>Total payment amount</strong>
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number
							property="theReport.withdrawalCalculatedInfoVO.totalPaymentAmount"
							type="c" sign="false" />
					</td>
				</tr>	
				
			<c:if test="${not empty theReport.withdrawalCalculatedInfoVO.forfeitedOrUMText1}">
			<c:if test="${not empty theReport.withdrawalCalculatedInfoVO.forfeitedOrUMText2}">
				<tr class="datacell1">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			<c:if test="${theReport.webDisplayVersion ==VERSION_1}">
					<td class="datacell1" valign="top">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
			</c:if>
				
					<td class="datacell1" align="right" valign="top">
					${theReport.withdrawalCalculatedInfoVO.forfeitedOrUMText1}<br>
					${theReport.withdrawalCalculatedInfoVO.forfeitedOrUMText2}
					</td>
					<td class="datadivider">
						<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
					</td>
					<td align="right" valign="top">
						<render:number
							property="theReport.withdrawalCalculatedInfoVO.forfeitedOrUnvestedERAmount"
							type="c" sign="false" />
					</td>
				</tr>	
			</c:if>
			</c:if>
			</c:if>
			<tr class="greyborder">
				<td colspan="7" class="greyborder" width="1" height="1">
				</td>
			</tr>
			</tbody>
			</table>
			<br/>
		</td>
	</tr>
	<tr>
		<td colspan="10" class="databorder" width="1" height="1">
		</td>
	</tr>
</c:forEach>
