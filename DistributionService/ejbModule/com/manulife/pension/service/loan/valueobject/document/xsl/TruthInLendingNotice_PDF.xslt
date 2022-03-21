<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:strip-space elements="*" />
	
	<!--  Common Attributes  -->
	<xsl:attribute-set name="normal-font-size">
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table-border">
		<xsl:attribute name="border">0.5pt solid black</xsl:attribute>
	</xsl:attribute-set>
	
	<!--  Variable Declarations  -->
	<xsl:variable name="loanStatus" select="TruthInLendingNotice/loanStatus" />
	<xsl:variable name="participanInitiatedLoan" select="TruthInLendingNotice/participanInitiatedLoan" />
	
	<!--  Main template begins -->
	<xsl:template match="TruthInLendingNotice">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4"
					page-width="8.5in" page-height="11in" margin-top="0.5in"
					margin-bottom="0.5in" margin-left="1cm" margin-right="1cm">
					<fo:region-body extent="5cm" />
					<fo:region-before extent="5cm" />
					<fo:region-after extent="5cm" />
					<fo:region-start extent="5cm" />
					<fo:region-end extent="5cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="A4">
				<fo:flow flow-name="xsl-region-body">
						<fo:block space-before="3mm" space-after="3mm"
							xsl:use-attribute-sets="normal-font-size" font-weight="bold"
							font-style="normal">
							TRUTH IN LENDING NOTICE
						</fo:block>
						<fo:block
							xsl:use-attribute-sets="normal-font-size">
							Borrower:
							<xsl:value-of select="participantName" />
							("You")
						</fo:block>
						<fo:block xsl:use-attribute-sets="normal-font-size"
							space-before="1mm">
							Name of Plan:
							<xsl:value-of select="planName" />
							(the "Plan")
						</fo:block>
						<fo:block xsl:use-attribute-sets="normal-font-size"
							space-before="1mm">
							Name of Employer:
							<xsl:value-of select="contractName" />
							(the "Employer")
						</fo:block>
						<fo:block space-after="3mm" space-before="1mm"
							xsl:use-attribute-sets="normal-font-size">
							Date of Loan:
							<xsl:value-of select="loanEffectiveDate" />
						</fo:block>
						<fo:table border-collapse="collapse"
							xsl:use-attribute-sets="table-border">
							<fo:table-column column-width="40mm"
								xsl:use-attribute-sets="table-border" />
							<fo:table-column column-width="35mm" />
							<fo:table-column column-width="45mm"
								xsl:use-attribute-sets="table-border" />
							<fo:table-column column-width="50mm" />
							<fo:table-body>
								<fo:table-row height="40pt">
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="6pt"
											margin-left="2mm" margin-right="3mm">
											ANNUAL PERCENTAGE RATE
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="6pt"
											margin-left="2mm" margin-right="5mm">
											FINANCE CHARGE
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="6pt"
											margin-left="2mm">
											AMOUNT FINANCED
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="6pt"
											margin-left="2mm" margin-right="2mm">
											TOTAL OF PAYMENTS
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row height="50pt">
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
											text-align="left" margin-left="2mm">
											The interest cost as a
											yearly rate
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
											text-align="left" margin-left="2mm" margin-right="4mm">
											The amount the loan will
											cost you
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
											text-align="left" margin-left="2mm" margin-right="4mm">
											The amount of credit
											provided to you or on your
											behalf
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
											text-align="left" margin-left="2mm" margin-right="2mm">
											The amount you will have
											paid after all payments as
											scheduled
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row height="40pt">
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
											margin-left="2mm" text-align="center">
											<xsl:value-of select="annualPercentageRate" />%
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
											margin-left="2mm" text-align="center">
											$<xsl:value-of select="financeCharge" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
											margin-left="2mm" text-align="center">
											$<xsl:value-of select="loanAmount" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block
											xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
											margin-left="2mm" text-align="center">
											$<xsl:value-of select="totalOfPayment" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						<fo:block xsl:use-attribute-sets="normal-font-size"
							space-before="5mm" space-after="5mm">
							Your Repayment Schedule will be:
						</fo:block>
						<fo:table border-collapse="collapse">
							<fo:table-column column-width="50mm" />
							<fo:table-column column-width="60mm" />
							<fo:table-column column-width="60mm" />
							<fo:table-body>
								<fo:table-row height="18pt">
									<fo:table-cell display-align="center"
										xsl:use-attribute-sets="table-border">
										<fo:block
											xsl:use-attribute-sets="normal-font-size" text-align="center"
											space-before="2mm" space-after="2mm" margin-left="2mm">
											Number of Months
										</fo:block>
									</fo:table-cell>
									<fo:table-cell display-align="center"
										xsl:use-attribute-sets="table-border">
										<fo:block
											xsl:use-attribute-sets="normal-font-size" text-align="center"
											space-before="2mm" space-after="2mm" margin-left="2mm">
											Amount of Each Repayment
										</fo:block>
									</fo:table-cell>
									<fo:table-cell display-align="center"
										xsl:use-attribute-sets="table-border">
										<fo:block
											xsl:use-attribute-sets="normal-font-size" text-align="center"
											space-before="2mm" space-after="2mm" margin-left="2mm">
											Frequency and Date of First
											Payment
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row height="9mm">
									<fo:table-cell
										xsl:use-attribute-sets="table-border"
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="normal-font-size" text-align="center">
											<xsl:value-of
												select="amortizationInMonths" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										xsl:use-attribute-sets="table-border"
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="normal-font-size" text-align="center">
											$<xsl:value-of select="repaymentAmount" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										xsl:use-attribute-sets="table-border"
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="normal-font-size" text-align="center">
											<xsl:value-of
												select="paymentFrequency" />
											starting on
											<xsl:value-of
												select="firstPaymentDate" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						<fo:block space-before="3mm" space-after="3mm"
							xsl:use-attribute-sets="normal-font-size">
							Prepayment: You may, at any time, prepay the
							entire outstanding balance of the loan, plus
							accrued interest, without penalty.
						</fo:block>
						<fo:block space-before="3mm" space-after="3mm"
							xsl:use-attribute-sets="normal-font-size">
							Method of repayment: Payroll deductions
						</fo:block>
						<fo:block space-before="3mm" space-after="3mm"
							xsl:use-attribute-sets="normal-font-size">
							Security : You are required to grant a
							continuing security interest of up to no
							more than
							<xsl:value-of select="maximumLoanPercentage" />%
							of your vested account balance under the
							Plan to the Trustees to secure repayment of
							the loan principal and accrued interest, if
							at any time, this loan is found to be in
							default (as defined below).
						</fo:block>
						<fo:block space-before="3mm" space-after="3mm"
							xsl:use-attribute-sets="normal-font-size">
							Default: The entire unpaid principal and
							interest of your loan will become due and
							payable upon (a) your termination of
							employment with the Employer; or (b) your
							failure to make a scheduled repayment.
							<xsl:value-of select="defaultProvisions" />.
							Upon default, your entire outstanding loan
							balance, including the accrued loan
							interest, will be deemed distributed to you.
							Such a deemed distribution is taxable to
							you, and if applicable, may be subject to
							certain tax penalties under tax law.
						</fo:block>
						<fo:block space-before="3mm" space-after="3mm"
							xsl:use-attribute-sets="normal-font-size">
							See your Non-negotiable Promissory Note and Irrevocable
							Pledge and Assignment for additional
							information about non-payment, default, and
							required payment in full before the
							scheduled date.
						</fo:block>
						<fo:block space-before="3mm" space-after="3mm"
							xsl:use-attribute-sets="normal-font-size">
							By signing below, you acknowledge having
							received a copy of this Truth In Lending
							Notice.
						</fo:block>
						
						<xsl:choose>
							<xsl:when
								test="($loanStatus='L4' or $loanStatus='L6') and $participanInitiatedLoan='Y'">
								<fo:block
									xsl:use-attribute-sets="normal-font-size" space-before="18mm">
									Agreed to by
									<xsl:value-of select="participantNameAO" />
									on
									<xsl:value-of select="loanAcceptedDate" />
								</fo:block>
							</xsl:when>
							<xsl:otherwise>
								<fo:block font-size="11pt" space-before="18mm">
									<fo:leader leader-pattern="rule"
										rule-thickness="0.5pt" leader-length="70%" top="0pt">
									</fo:leader>
								</fo:block>
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Borrower&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;
									&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;
									&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;
									&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;
									&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;&#xA0;
									Date
								</fo:block>
							</xsl:otherwise>
						</xsl:choose>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>