<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:preserve-space elements="*" />
	<xsl:output method="xml" omit-xml-declaration="yes" />
	
	<!--  Variable Declarations  -->
	<xsl:variable name="loanStatus" select="TruthInLendingNotice/loanStatus" />
	<xsl:variable name="participanInitiatedLoan" select="TruthInLendingNotice/participanInitiatedLoan" />
	<xsl:template match="TruthInLendingNotice">
		<font face="Arial Narrow">
			<h3>TRUTH IN LENDING NOTICE</h3>
			Borrower:
			<xsl:value-of select="participantName" />
			<br />
			Name of Plan:
			<xsl:value-of select="planName" /> (the "Plan")
			<br />
			Name of Employer:
			<xsl:value-of select="contractName" /> (the "Employer")
			<br />
			Date of Loan:
			<xsl:value-of select="loanEffectiveDate" />
			<br />
			<br />
			<table border="1" rules="cols" width="80%" cellpadding="10"
				cellspacing="0">
				<tr>
					<td>ANNUAL PERCENTAGE RATE</td>
					<td>FINANCE CHARGE</td>
					<td>AMOUNT FINANCED</td>
					<td>TOTAL OF PAYMENTS</td>
				</tr>
				<tr>
					<td>The interest cost as a yearly rate</td>
					<td>The amount the loan will cost you</td>
					<td>
						The amount of credit provided to you or on your
						behalf
					</td>
					<td>
						The amount you will have paid after all payments
						as Scheduled
					</td>
				</tr>
				<tr>
					<td align="center">
						<xsl:value-of select="annualPercentageRate" />%
					</td>
					<td align="center">
						$<xsl:value-of select="financeCharge" />
					</td>
					<td align="center">
						$<xsl:value-of select="loanAmount" />
					</td>
					<td align="center">
						$<xsl:value-of select="totalOfPayment" />
					</td>
				</tr>
			</table>
			<br />
			Your Repayment Schedule will be:
			<br />
			<br />
			<table width="80%" cellspacing="0" border="1" rules="all"
				cellpadding="10">
				<tr>
					<td valign="top">Number of Months</td>
					<td valign="top">Amount of Each Repayment</td>
					<td valign="top">
						Frequency and Date of First	Payment
					</td>
				</tr>
				<tr>
					<td align="center">
						<xsl:value-of select="amortizationInMonths" />
					</td>
					<td align="center">
						$<xsl:value-of select="repaymentAmount" />
					</td>
					<td align="center">
						<xsl:value-of select="paymentFrequency" />
						starting on
						<xsl:value-of select="firstPaymentDate" />
					</td>
				</tr>
			</table>
			<p>
				Prepayment: You may, at any time, prepay the entire
				outstanding balance of the loan, plus accrued interest,
				without penalty.
			</p>
			<p>Method of repayment: Payroll deductions</p>
			<p>
				Security : You are required to grant a continuing
				security interest of up to no more than
				<xsl:value-of select="maximumLoanPercentage" />%
				of your vested account balance under the Plan to the
				Trustees to secure repayment of the loan principal and
				accrued interest, if at any time, this loan is found to
				be in default (as defined below).
			</p>
			<p>
				Default: The entire unpaid principal and interest of
				your loan will become due and payable upon (a) your
				termination of employment with the Employer; or (b) your
				failure to make a Scheduled repayment.
				<xsl:value-of select="defaultProvisions" />.
				Upon default, your entire outstanding loan balance,
				including the accrued loan interest, will be deemed
				distributed to you. Such a deemed distribution is
				taxable to you, and if applicable, may be subject to
				certain tax penalties under tax law.
			</p>
			<p>
				See your Non-negotiable Promissory Note and Irrevocable Pledge and
				Assignment for additional information about non-payment,
				default, and required payment in full before the
				Scheduled date.
			</p>
			<p>
				By signing below, you acknowledge having received a copy
				of this Truth In Lending Notice.
			</p>
			<br />
			<br />
            <br />
            <br />
            <br />
			
			
			<xsl:choose>
				<xsl:when test="($loanStatus='L4' or $loanStatus='L6') and $participanInitiatedLoan='Y'">
					Agreed to by <xsl:value-of select="participantNameAO" />
					on
					<xsl:value-of select="loanAcceptedDate" />
				</xsl:when>
				<xsl:otherwise>
					<hr width="70%" align="left" noshade="true" />
					<table width="50%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td align="left">Borrower</td>
							<td align="right">Date</td>
						</tr>
					</table>
				</xsl:otherwise>
			</xsl:choose>
		</font>
	</xsl:template>
</xsl:stylesheet>