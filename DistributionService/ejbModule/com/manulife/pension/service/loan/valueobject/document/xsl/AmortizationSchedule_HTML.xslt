<?xml version="1.0" ?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:preserve-space elements="*" />
	<xsl:output method="xml" omit-xml-declaration="yes"
		encoding="us-ascii" />
	<xsl:template match="AmortizationSchedule">
		<font face="Arial Narrow">
			AMORTIZATION SCHEDULE
			<br />
			<br />
			Contract Name:
			<xsl:value-of select="contractName" />
			<br />
			Participant Loan for:
			<xsl:value-of select="participantName" />
			<br />
			Social Security Number:
			<xsl:value-of select="socialSecurityNumber" />
			<br />
			Employee Number:
			<xsl:value-of select="employeeNumber" />
			<br />
			<br />
			Compound Period:
			<xsl:value-of select="paymentFrequency" />
			<br />
			<br />
			Nominal Interest Rate:
			<xsl:value-of select="loanInterestRate" />%
			<br />
			Effective Annual Rate:
			<xsl:value-of select="effectiveAnnualRate" />%
			<br />
			Periodic Rate:
			<xsl:value-of select="periodicRate" />%
			<br />
			Daily Rate:
			<xsl:value-of select="dailyRate" />%
			<br />
			<br />
			CASH FLOW DATA
			<br />
			<br />
			<xsl:variable name="finalPaymentAmount">
				<xsl:value-of select="finalPaymentAmount" />
			</xsl:variable>
			<xsl:variable name="paymentAmount">
				<xsl:value-of select="paymentAmount" />
			</xsl:variable>
			<xsl:variable name="isFinalPaymentDifferent">
				<xsl:value-of select="isFinalPaymentDifferent" />
			</xsl:variable>
			
			<table width="80%" cellspacing="0" cellpadding="10">
				<tr>
					<td style="border-bottom-style: solid; border-top-style: solid; border-width: thin; width:5%">
						&#160;
					</td>
					<td style="border-bottom-style: solid; border-top-style: solid; border-width: thin; width:15%">
						<b>Event</b>
					</td>
					<td style="border-bottom-style: solid; border-top-style: solid; border-width: thin; width:10%">
						<b>Start Date</b>
					</td>
					<td align="right" style="border-bottom-style: solid; border-top-style: solid; border-width: thin; width:10%">
						<b>Amount</b>
					</td>
					<td align="right" style="border-bottom-style: solid; border-top-style: solid; border-width: thin; width:10%">
						<b>Number</b>
					</td>
					<td align="right" style="border-bottom-style: solid; border-top-style: solid; border-width: thin; width:10%">
						<b>Period</b>
					</td>
					<td align="right" style="border-bottom-style: solid; border-top-style: solid; border-width: thin; width:15%">
						<b>End Date</b>
					</td>
				</tr>
				<tr>
					<td style="width:5%">1</td>
					<td style="width:15%">Loan</td>
					<td style="width:10%">
						<xsl:value-of select="loanEffectiveDate" />
					</td>
					<td align="right" style="width:10%">
						<xsl:value-of select="loanAmount" />
					</td>
					<td align="right" style="width:10%">1</td>
					<td align="right" style="width:10%"></td>
					<td align="right" style="width:15%"></td>
				</tr>
				<tr>
					<td style="width:5%">2</td>
					<td style="width:15%">Repayment amount</td>
					<td style="width:10%">
						<xsl:value-of select="firstPaymentDate" />
					</td>
					<td align="right" style="width:10%">
						<xsl:value-of select="paymentAmount" />
					</td>
					<td align="right" style="width:10%">
						<xsl:choose>
							<xsl:when
								test="$isFinalPaymentDifferent='N'">
								<xsl:value-of select="numberOfPayments" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of
									select="numberOfPaymentsLessOne" />
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td align="right" style="width:10%">
						<xsl:value-of select="paymentFrequency" />
					</td>
					<td align="right" style="width:15%">
						<xsl:choose>
							<xsl:when
								test="$isFinalPaymentDifferent='N'">
								<xsl:value-of select="finalPaymentDate" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of
									select="secondLastPaymentDate" />
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				<xsl:if test="$isFinalPaymentDifferent='Y'">
					<tr>
						<td style="width:5%">3</td>
						<td style="width:15%">Payment</td>
						<td style="width:10%">
							<xsl:value-of select="finalPaymentDate" />
						</td>
						<td align="right" style="width:10%">
							<xsl:value-of select="finalPaymentAmount" />
						</td>
						<td align="right" style="width:10%">1</td>
						<td align="right" style="width:10%"></td>
						<td align="right" style="width:15%"></td>
					</tr>
				</xsl:if>
			</table>
			<br />
			<br />
			AMORTIZATION SCHEDULE-Normal Amortization
			<br />
			<br />
			<table width="80%" cellspacing="0" cellpadding="10">
				<tr>
					<td align="right" style="width:10%; border-bottom-style: solid; border-width: thin;">
						&#160;
					</td>
					<td align="right" style="width:10%; border-bottom-style: solid; border-width: thin;">
						<b>Date</b>
					</td>
					<td align="right" style="width:10%; border-bottom-style: solid; border-width: thin;">
						<b>Payment</b>
					</td>
					<td align="right" style="width:10%; border-bottom-style: solid; border-width: thin;">
						<b>Interest</b>
					</td>
					<td align="right" style="width:10%; border-bottom-style: solid; border-width: thin;">
						<b>Principal</b>
					</td>
					<td align="right" style="width:10%; border-bottom-style: solid; border-width: thin;">
						<b>Balance</b>
					</td>
				</tr>
				<tr>
					<td align="right"
						style="width:10%;">Loan
					</td>
					<td align="right"
						style="width:10%;">
						<xsl:value-of select="loanEffectiveDate" />
					</td>
					<td align="right"
						style="width:10%;">
					</td>
					<td align="right"
						style="width:10%;">
					</td>
					<td align="right"
						style="width:10%;">
					</td>
					<td align="right"
						style="width:10%;">
						<xsl:value-of select="loanAmount" />
					</td>
				</tr>
				<xsl:for-each select="loanPayment">
					<xsl:variable name="summaryInd">
						<xsl:value-of select="summaryInd" />
					</xsl:variable>
					<tr>
						<xsl:if test="$summaryInd='N' ">
							<td style="width:10%" align="right">
								<xsl:value-of select="count" />
							</td>
							<td align="right" style="width:10%">
								<xsl:value-of select="paymentDate" />
							</td>
							<td align="right" style="width:10%">
								<xsl:value-of select="paymentAmount" />
							</td>
							<td align="right" style="width:10%">
								<xsl:value-of
									select="paymentAmountInterest" />
							</td>
							<td align="right" style="width:10%">
								<xsl:value-of
									select="paymentAmountPrincipal" />
							</td>
							<td align="right" style="width:10%">
								<xsl:value-of
									select="loanPrincipalRemaining" />
							</td>
						</xsl:if>
						<xsl:if test="$summaryInd='Y' ">
							<td align="right" style="width:10%">
								<b>
									<xsl:value-of select="totalLabel" />
								</b>
							</td>
							<td align="right" style="width:10%"></td>
							<td align="right" style="width:10%">
								<b>
									<xsl:value-of select="totalPayment" />
								</b>
							</td>
							<td align="right" style="width:10%">
								<b>
									<xsl:value-of
										select="totalInterest" />
								</b>
							</td>
							<td align="right" style="width:10%">
								<b>
									<xsl:value-of
										select="totalPrincipal" />
								</b>
							</td>
							<td align="right" style="width:10%"></td>
						</xsl:if>
					</tr>
				</xsl:for-each>
				<tr>
					<td style="width:5%" align="right">
						<b>Grand Totals</b>
					</td>
					<td></td>
					<td align="right" style="width:10%">
						<b>
							<xsl:value-of
								select="grandTotals/grandtotalPayment" />
						</b>
					</td>
					<td align="right" style="width:10%">
						<b>
							<xsl:value-of
								select="grandTotals/grandTotalInterest" />
						</b>
					</td>
					<td align="right" style="width:10%">
						<b>
							<xsl:value-of
								select="grandTotals/grandTotalPrincipal" />
						</b>
					</td>
					<td align="right" style="width:10%"></td>
				</tr>
			</table>
		</font>
	</xsl:template>
</xsl:stylesheet>