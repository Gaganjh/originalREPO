<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.iloans.IloansHelper"%>
<%@ page import="com.manulife.pension.service.account.valueobject.LoanRequestMoneyType"%>
<content:contentBean
	contentId="<%=ContentConstants.CONFIRMATION_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="confirmationMessage" />
<script language="JavaScript1.2" type="text/javascript">
<!--
	var submitted = false;

	function setButtonAndSubmit(button) {
		if (!submitted) {
			submitted = true;
			document.loanRequestForm.button.value = button;
			document.loanRequestForm.submit();
		} else {
			window.status = "Transaction already in progress.  Please wait.";
		}
	}

	/**
	 * Opens up a new window and perform the same request again (with printFriendly
	 * parameter.
	 */
	function doPrint() {
		var printURL;
		urlquery = location.href.split("?");
		if (urlquery.length > 1) {
			printURL = location.href + "&task=print&printFriendly=true";
		} else {
			printURL = location.href + "?task=print&printFriendly=true";
		}
		window.open(printURL, "",
				"width=720,height=480,resizable,toolbar,scrollbars,");
	}
	-->
</script>

<%@ page
	import="com.manulife.pension.ps.web.iloans.LoanRequestForm"%>
<%@ page
	import="com.manulife.pension.service.account.valueobject.LoanPayrollFrequency"%>
<%@ page
	import="com.manulife.pension.service.account.valueobject.LoanRequestData"%>
<jsp:useBean id="loanRequestForm" scope="session" type="com.manulife.pension.ps.web.iloans.LoanRequestForm" />

<br>
<content:errors scope="session" />
<br>
<ps:form method="POST" modelAttribute="loanRequestForm" name="loanRequestForm" action="/do/iloans/loanRequestConfirmation/">

	

<form:hidden path="button" />
	<table width="650" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="113"><img src="/assets/unmanaged/images/s.gif"
				width="80" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="463"><img src="/assets/unmanaged/images/s.gif"
				width="250" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="113"><img src="/assets/unmanaged/images/s.gif"
				width="80" height="1" /></td>
			<td width="4"><img src="/assets/unmanaged/images/s.gif"
				width="4" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="tablehead">
			<td class="tableheadTD1" colspan="8"><b><content:getAttribute
						beanName="layoutPageBean" attribute="body1Header" /></b></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td colspan="6" rowspan="13"><span class="content">
					<TABLE border="0" cellspacing="0" cellpadding="0" width="100%"
						height="72">
<c:if test="${loanRequestForm.tpaInitiated ==false}">

							<TR>
								<TD HEIGHT="15" COLSPAN="3" class="boldtext"><p
										class="highlightBold">

										<content:getAttribute beanName="confirmationMessage"
											attribute="text" />
							</tr>
</c:if>
						<tr>
<TD HEIGHT="15" COLSPAN="3">Date: ${loanRequestForm.appDate}</TD>

						</TR>
					</TABLE>
			</span>
				<table width="100%" border="0" cellpadding="0">
					<tr>
						<td colspan="6" align="left"><strong>Participant
								information</strong></span></td>
					</tr>
					<tr>
						<td align="left" colspan="3"><strong>Contract</strong></td>
<td width="104" align="left">${loanRequestForm.contractNumber}</td>

						<td width="130" align="left"><strong>Contract name</strong></td>
<td width="205" align="left">${loanRequestForm.contractName} <%-- filter="false" --%></td>

					</tr>
					<tr>
						<td align="left" colspan="3"><strong>SSN</strong></span></td>
						<td width="104" align="left"><render:ssn
								property="loanRequestForm.participantSSN" /></td>
						<td width="130" align="left"><strong>Participant
								name</strong></td>
<td width="205" align="left">${loanRequestForm.participantName}</td>

					</tr>
					<tr>
						<td colspan="3" align="left"><strong>Total
								participant balance</strong></td>
						<td width="104" align="left">$<render:number
								property="loanRequestForm.totalParticipantBalance"
								defaultValue="0.00" pattern="###,###,##0.00" /></td>
						<td width="130" align="left"><strong>Legally
								married</strong></td>
<td width="205" align="left"> 
<c:if test="${loanRequestForm.legallyMarried == ''}">

					  not available
</c:if> 
<c:if test="${loanRequestForm.legallyMarried != ''}">

${loanRequestForm.legallyMarried}
</c:if></td>
					</tr>
					<tr>
						<td height="17" colspan="3" align="left"><strong>Type
								of loan</strong></td>
<td width="104" height="17" align="left">${loanRequestForm.reqLoanReasonCode}</td>

						<td width="130" height="17" align="left"><strong>Confirmation
								number</strong></td>
<td width="205" height="17" align="left">${loanRequestForm.confirmationNumber}</td>

					</tr>
					<tr>
						<td height="17" colspan="3" align="left" valign="top"><strong>Reason
								for loan</strong></td>
<td height="17" colspan="3" align="left">${loanRequestForm.reasonForLoan}</td>

					</tr>
				</table>
				<table width="100%">
					<tbody>
						<tr>
							<td colspan="7" align="left" height="57"><strong>TPA
									calculations </strong></td>
						</tr>
						<tr>
							<td height="17" colspan="4" align="left" valign="top"><strong>Calculated
									Vested Balance</strong></td>
							<td width="5" align="left" height="17"><br class="style1">
							</td>
							<td colspan="2" align="left" height="17"><strong>Calculated
									Max. Loan Available</strong></td>
						</tr>
						<tr>
							<td colspan="5" valign="top">
								<table width="100%" cellpadding="0" cellspacing="0" border="0">
									<tr valign="top">
										<td width="134" align="left"><strong>Money Type</strong></td>
										<td width="80"><strong>Dollars</strong></td>
										<td width="49"><strong>Vesting %</strong></td>
										<td width="48"><strong>Exclude</strong></td>
										<td width="5" align="left">&nbsp;</td>
									</tr>
<c:forEach var="moneyType" items="${loanRequestForm.getLoanRequestMoneyTypes()}">


										<tr>
											<td width="134" align="left">&nbsp;</td>
											<td align="right" width="80">&nbsp;</td>
											<td width="49" align="right">&nbsp;</td>
											<td width="48" align="center">&nbsp;</td>
											<td width="5" align="left">&nbsp;</td>
										</tr>

										<tr>
<td width="134" align="left"><strong>${moneyType.moneyTypeName}</strong></td>

											<td align="right" width="67">$<render:number
													property="moneyType.balanceAmt" defaultValue="0.00"
													pattern="###,###,##0.00" /></td>
											<td width="49" align="right"><render:number
													property="moneyType.vestingPct" defaultValue="0.00"
													pattern="###,###,##0.00" /></td>
<c:if test="${moneyType.moneyTypeExcluded ==true}">

												<td width="48" align="center">X</td>
</c:if>
<c:if test="${moneyType.moneyTypeExcluded ==false}">

												<td width="48" align="center">&nbsp;</td>
</c:if>
											<td width="5" align="left">&nbsp;</td>
										</tr>
</c:forEach>
									<tr>
										<td width="134" align="left">&nbsp;</td>
										<td align="right" width="80">&nbsp;</td>
										<td width="49" align="right">&nbsp;</td>
										<td width="48" align="center">&nbsp;</td>
										<td width="5" align="left">&nbsp;</td>
									</tr>
									<tr>
										<td width="134" align="left"><strong>OTHER MONEY
												TYPES</strong></td>
										<td align="right" width="80">$<render:number
												property="loanRequestForm.otherMoneyTypeBalanceAmt"
												defaultValue="0.00" pattern="###,###,##0.00" /></td>
										<td width="49" align="right"><render:number
												property="loanRequestForm.otherMoneyTypeBalanceVestingPct"
												defaultValue="0.00" pattern="###,###,##0.00" /></td>
<c:if test="${loanRequestForm.otherMoneyTypeBalanceExcluded ==true}">

											<td width="48" align="center">X</td>
</c:if>
<c:if test="${loanRequestForm.otherMoneyTypeBalanceExcluded ==false}">

											<td width="48" align="center">&nbsp;</td>
</c:if>
										<td width="5" align="left">&nbsp;</td>
									</tr>
									<tr>
										<td width="134" align="left"><strong>Vested
												Acct. Balance</strong></td>
										<td align="right" width="80">$<render:number
												property="loanRequestForm.appVestedAccountBalance"
												defaultValue="0.00" pattern="###,###,##0.00" /></td>
										<td width="49" align="right">&nbsp;</td>
										<td width="48" align="center">&nbsp;</td>
										<td width="5" align="left">&nbsp;</td>
									</tr>

								</table>
							</td>
							<td colspan="2">
								<table width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td width="138" align="left">&nbsp;</td>
										<td width="143" align="right">&nbsp;</td>
									</tr>
									<tr>
										<td width="138" align="left"><strong>Highest
												Loan Balance in last 12 months</strong></td>
										<td width="143" align="right">$<render:number
												property="loanRequestForm.highestLoanBalanceLast12Months"
												defaultValue="0.00" pattern="###,###,##0.00" /></td>
									</tr>
									<tr>
										<td width="138" align="left"><strong>Number of
												outstanding loans</strong></td>
<td width="143" align="right">${loanRequestForm.numberOfOutstandingLoansCount}</td>


									</tr>
									<tr>
										<td width="138" align="left"><strong>Current
												outstanding balance</strong></td>
										<td width="143" align="right">$<render:number
												property="loanRequestForm.currentOutstandingLoanBalance"
												defaultValue="0.00" pattern="###,###,##0.00" /></td>
									</tr>
									<tr>
										<td width="138" align="left"><strong>Max. Loan
												Available</strong></td>
										<td width="143" align="right">$<render:number
												property="loanRequestForm.appMaxLoanAvailable"
												defaultValue="0.00" pattern="###,###,##0.00" /></td>
									</tr>

								</table>
							</td>
						</tr>
						<tr>
							<td align="left" colspan="7" class="boldtext">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" align="left"><strong class="highlightBold">Participant
									Modeled Information</strong></td>
							<td width="5" align="left">&nbsp;</td>
							<td colspan="2" align="left"><strong class="highlightBold">TPA
									Approved Information</strong></td>
						</tr>
						<tr>
							<td colspan="2" align="left">&nbsp;</td>
							<td colspan="2" align="right">&nbsp;</td>
							<td width="5" align="left">&nbsp;</td>
							<td width="138" align="left"><strong>Max.
									Amortization Period</strong></td>
<td width="143" align="right"><c:if test="${empty loanRequestForm.maxAmortizationYears}">n/a</c:if>

<c:if test="${not empty loanRequestForm.maxAmortizationYears}">

${loanRequestForm.maxAmortizationYears} years

</c:if></td>
						</tr>
						<tr>
							<td colspan="2" align="left"><strong>Estimated
									Interest Rate</strong></td>
<td colspan="2" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

${loanRequestForm.reqLoanInterestRate}%

</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">n/a

</c:if></td>
							<td width="5" align="left">&nbsp;</td>
							<td width="138" align="left"><strong>Loan Interest
									Rate</strong></td>
<td width="143" align="right"><c:if test="${empty loanRequestForm.appLoanInterestRate}">n/a</c:if>

<c:if test="${not empty loanRequestForm.appLoanInterestRate}">

${loanRequestForm.appLoanInterestRate}%

</c:if></td>
						</tr>
						<tr>
							<td colspan="2" align="left"><strong>Repayment
									Frequency</strong></td>
<td colspan="2" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

${loanRequestForm.reqPaymentFrequency}

</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">n/a

</c:if></td>
							<td width="5" align="left">&nbsp;</td>
							<td width="138" align="left"><strong>Repayment
									Frequency</strong></td>
<td width="143" align="right">${loanRequestForm.appPaymentFrequency}</td>

						</tr>
						<tr>
							<td colspan="2" align="left"><strong>Est. Max.
									Loan Available</strong></td>
<td colspan="2" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

			  $<render:number property="loanRequestForm.reqMaxLoanAvailable"
										defaultValue="0.00" pattern="###,###,##0.00" />
</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">n/a

</c:if></td>
							<td width="5" align="left">&nbsp;</td>
							<td width="138" align="left"><strong>Max. Loan
									Available</strong></td>
							<td width="143" align="right">$<render:number
									property="loanRequestForm.appMaxLoanAvailable"
									defaultValue="0.00" pattern="###,###,##0.00" /></td>
						</tr>
						<tr>
							<td colspan="2" align="left"><strong>Requested
									Amount</strong></td>
<td colspan="2" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

			  
			  $<render:number property="loanRequestForm.reqLoanAmount"
										defaultValue="0.00" pattern="###,###,##0.00" />
</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">n/a

</c:if></td>
							<td width="5" align="left">&nbsp;</td>
							<td width="138" align="left"><strong>Loan Amount</strong></td>
							<td width="143" align="right">$<render:number
									property="loanRequestForm.appLoanAmount" defaultValue="0.00"
									pattern="###,###,##0.00" /></td>
						</tr>
						<tr>
							<td colspan="2" align="left"><strong>Amortization
									Period</strong></td>
<td align="right" colspan="2"><c:if test="${loanRequestForm.tpaInitiated ==false}">

${loanRequestForm.reqAmortizationPeriod}

</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">n/a

</c:if></td>
							<td align="left" width="5">&nbsp;</td>
							<td width="138" align="left"><strong>Amortization
									Period</strong></td>
<td align="right" width="143">${loanRequestForm.appAmortizationPeriod}</td>

						</tr>
						<tr>
							<td colspan="2" align="left"><strong>Repayment
									Amount</strong></td>
<td colspan="2" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

			  $<render:number property="loanRequestForm.reqPaymentAmount"
										defaultValue="0.00" pattern="###,###,##0.00" />
</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">n/a

</c:if></td>

							<td width="5" align="left">&nbsp;</td>
							<td width="138" align="left"><strong>Repayment
									Amount</strong></td>
							<td width="143" align="right">$<render:number
									property="loanRequestForm.appPaymentAmount" defaultValue="0.00"
									pattern="###,###,##0.00" />


							</td>
						</tr>
						<tr>
							<td colspan="2" align="left"><strong>Request Date</strong></td>
<td colspan="2" align="right">${loanRequestForm.reqDate}</td>

							<td width="5" align="left">&nbsp;</td>
							<td width="138" align="left"><strong>Plan Info
									Expiry Date</strong></td>
<td width="143" align="right">${loanRequestForm.planInfoExpiryDate}</td>

						</tr>
						<tr>
							<td align="left" colspan="7" class="boldtext">&nbsp;</td>
						</tr>
					</tbody>
				</table>
				<table width="100%" border="0">
					<tr>
						<td align="left" valign="top"><strong>Default
								provision</strong></td>
						<td></td>
<td colspan="8">${loanRequestForm.defaultProvision}</td>

					</tr>
					<tr>
						<td align="left">&nbsp;</td>
						<td></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="left"><strong>Proceed with Loan</strong></td>
						<td></td>
<td>${loanRequestForm.approveLoan}</td>
						<td>&nbsp;</td>
						<td class="boldtext">&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td class="boldtext">&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td colspan="10" align="left" class="boldtext">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="10" align="left">Participant loan package will
							include all necessary forms with instructions.<br> Generic
							versions can be found in the Account | Forms section of this Web
							site.
						</td>
					</tr>
					<tr>
						<td colspan="10" align="left" class="boldtext">&nbsp;</td>
					</tr>
					<tr>
						<td align="left"><strong>TPA Loan Issue Fee</strong></td>
						<td width="0"></td>
						<td width="75">$<render:number
								property="loanRequestForm.loanSetUpFee" defaultValue="0.00"
								pattern="###,###,##0.00" /></td>
						<td width="10">&nbsp;</td>
						<td width="197"><strong>Spousal consent required</strong></td>
						<td width="13">&nbsp;</td>
<td width="95">${loanRequestForm.spousalConsent}</td>

						<td width="39" class="boldtext">&nbsp;</td>
						<td width="9">&nbsp;</td>
						<td width="47">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="10" align="left" class="boldtext">&nbsp;</td>
					</tr>
					<tr>
						<td width="119" align="left"><strong>Additional
								comments</strong></td>
						<td width="0"></td>
<td colspan="8">${loanRequestForm.additionalComments}</td>

					</tr>
				</table></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<ps:roundedCorner numberOfColumns="8" emptyRowColor="white" />
		</tr>

	</table>
	<br>
	<br>
	<table width="650" border="0" cellspacing="1" cellpadding="1">
		<tr>
			<td width="390" align="right"></td>
			<td width="130" align="right"></td>
			<td width="130" align="right"></td>
		</tr>
		<tr>

			<c:if test="${empty param.printFriendly }" >
<c:if test="${loanRequestForm.tpaInitiated ==false}">

<c:if test="${loanRequestForm.loanRequestApprovable ==true}">

<td colspan="2" align="right"><input type="button" onclick="javascript:setButtonAndSubmit('modify');return false;" name="cont" class="button100Lg" value="modify"/>


						</td>
<td><input type="button" onclick="javascript:setButtonAndSubmit('send');return false;" name="cont" class="button134" value="send to Participant"/>


						</td>
</c:if>
<c:if test="${loanRequestForm.loanRequestApprovable !=true}">

<td colspan="3" align="right"><input type="button" onclick="javascript:setButtonAndSubmit('viewLoanRequest');return false;" name="cont" class="button150" value="i:loans home"/>


						</td>
</c:if>
</c:if>

<c:if test="${loanRequestForm.tpaInitiated ==true}">

<c:if test="${loanRequestForm.approveLoan ==No}">

<td colspan="2" align="right"><input type="button" onclick="javascript:setButtonAndSubmit('modify');return false;" name="cont" class="button100Lg" value="modify"/></td>


<td align="right"><c:if test="${loanRequestForm.modified ==false}">

<input type="button" onclick="javascript:setButtonAndSubmit('viewLoanRequest');return false;" name="cont" class="button150" value="i:loans home"/>


</c:if> <c:if test="${loanRequestForm.modified ==true}">

<input type="button" onclick="javascript:setButtonAndSubmit('saveAndExit');return false;" name="cont" class="button150" value="save & exit"/>


</c:if></td>
</c:if>
<c:if test="${loanRequestForm.approveLoan !='No'}">

<td align="right"><input type="button" onclick="javascript:setButtonAndSubmit('modify');return false;" name="cont" class="button100Lg" value="modify"/></td>


<td align="right"><c:if test="${loanRequestForm.modified ==false}">

<input type="button" onclick="javascript:setButtonAndSubmit('viewLoanRequest');return false;" name="cont" class="button150" value="i:loans home"/></td>


</c:if>

<c:if test="${loanRequestForm.modified ==true}">
<input type="button" onclick="javascript:setButtonAndSubmit('saveAndExit');return false;" name="cont" class="button150" value="save & exit"/>


</c:if>
				</td>

<td align="right"><input type="button" onclick="javascript:setButtonAndSubmit('continue');return false;" name="cont" class="button100Lg" value="continue"/></td>



</c:if>
</c:if>
			</c:if>
		</tr>
	</table>
	<table width="650" border="0" cellspacing="1" cellpadding="1">
		<tr>
			<td width="390" align="right"></td>
			<td width="130" align="right"></td>
			<td width="130" align="right"></td>
		</tr>
		<tr>
			<td colspan="3"><br>
				<p>
					<content:pageFooter beanName="layoutPageBean" />
				</p>
				<p class="footnote">
					<content:pageFootnotes beanName="layoutPageBean" />
				</p>
				<p class="disclaimer">
					<content:pageDisclaimer beanName="layoutPageBean" index="-1" />
				</p></td>
		</tr>
	</table>
</ps:form>
<%--
<script>
	setFocusOnFirstInputField("viewLoanRequestsForm");
</script>
--%>
