<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:strip-space elements="*" />

	<!--  Common Attributes  -->
	<xsl:attribute-set name="normal-font-size">
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="cash-flow-table-header-attr">
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-top-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">thin</xsl:attribute>
		<xsl:attribute name="border-top-width">thin</xsl:attribute>
		<xsl:attribute name="display-align">center</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="amort-sch-header-attr">
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">thin</xsl:attribute>
		<xsl:attribute name="display-align">center</xsl:attribute>
	</xsl:attribute-set>

	<!--  Variable Declarations  -->
	<xsl:variable name="finalPaymentAmount">
		<xsl:value-of select="AmortizationSchedule/finalPaymentAmount" />
	</xsl:variable>
	<xsl:variable name="paymentAmount">
		<xsl:value-of select="AmortizationSchedule/paymentAmount" />
	</xsl:variable>
	<xsl:variable name="isFinalPaymentDifferent">
		<xsl:value-of select="AmortizationSchedule/isFinalPaymentDifferent" />
	</xsl:variable>

	<!--  Main template begins -->
	<xsl:template match="AmortizationSchedule">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4"
					page-width="8.5in" page-height="11in" margin-top="0.5in"
					margin-bottom="0.5in" margin-left="1cm" margin-right="1cm">
					<fo:region-body extent="5cm" margin-top="0.5in" />
					<fo:region-before extent="5cm" />
					<fo:region-after extent="5cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="A4">
				<fo:static-content flow-name="xsl-region-before">
					<fo:table border-collapse="collapse"
						table-layout="fixed">
						<fo:table-column column-width="100mm" />
						<fo:table-column column-width="95mm" />
						<fo:table-body>
							<fo:table-row height="2mm">
								<fo:table-cell>
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="left">
										<xsl:value-of
											select="participantName" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block
										xsl:use-attribute-sets="normal-font-size"
										text-align="right">
										<xsl:value-of
											select="contractName" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold">
						AMORTIZATION SCHEDULE
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="9pt">
						Contract Name:
						<xsl:value-of select="contractName" />
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="3pt">
						Participant Loan for:
						<xsl:value-of select="participantName" />
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="3pt">
						Social Security Number:
						<xsl:value-of select="socialSecurityNumber" />
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="3pt">
						Employee Number:
						<xsl:value-of select="employeeNumber" />
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="9pt">
						Compound Period:
						<xsl:value-of select="paymentFrequency" />
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="9pt">
						Nominal Interest Rate:
						<xsl:value-of select="loanInterestRate" />%
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="3pt">
						Effective Annual Rate:
						<xsl:value-of select="effectiveAnnualRate" />%
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="3pt">
						Periodic Rate:
						<xsl:value-of select="periodicRate" />%
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="3pt">
						Daily Rate:
						<xsl:value-of select="dailyRate" />%
					</fo:block>

					<!--  CASH FLOW DATA Table  -->
					<fo:block font-size="10pt" space-before="12pt"
						space-after="12pt">
						CASH FLOW DATA
					</fo:block>
					<fo:table border-collapse="collapse"
						table-layout="fixed">
						<fo:table-column column-width="10mm" />
						<fo:table-column column-width="40mm" />
						<fo:table-column column-width="25mm" />
						<fo:table-column column-width="30mm" />
						<fo:table-column column-width="30mm" />
						<fo:table-column column-width="30mm" />
						<fo:table-column column-width="30mm" />
						<fo:table-body>
							<fo:table-row height="10mm">
								<fo:table-cell
									xsl:use-attribute-sets="cash-flow-table-header-attr">
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="cash-flow-table-header-attr">
									<fo:block text-align="left"
										font-weight="bold" xsl:use-attribute-sets="normal-font-size"
										space-before="3pt" margin-left="2pt">
										Event
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="cash-flow-table-header-attr">
									<fo:block text-align="left"
										font-weight="bold" xsl:use-attribute-sets="normal-font-size"
										space-before="3pt" margin-left="2pt">
										Start Date
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="cash-flow-table-header-attr">
									<fo:block text-align="right"
										font-weight="bold" xsl:use-attribute-sets="normal-font-size"
										space-before="3pt" margin-left="2pt">
										Amount
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="cash-flow-table-header-attr">
									<fo:block text-align="right"
										font-weight="bold" xsl:use-attribute-sets="normal-font-size"
										space-before="3pt" margin-left="2pt">
										Number
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="cash-flow-table-header-attr">
									<fo:block text-align="right"
										font-weight="bold" xsl:use-attribute-sets="normal-font-size"
										space-before="3pt" margin-left="2pt">
										Period
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="cash-flow-table-header-attr">
									<fo:block text-align="right"
										font-weight="bold" xsl:use-attribute-sets="normal-font-size"
										space-before="3pt" margin-left="2pt">
										End Date
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="10mm">
								<fo:table-cell display-align="center"
									xsl:use-attribute-sets="normal-font-size">
									<fo:block space-before="3pt"
										margin-left="2pt">
										1
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="left"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										Loan
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="left"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="loanEffectiveDate" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="loanAmount" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										1
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="left"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="10mm">
								<fo:table-cell display-align="center">
									<fo:block text-align="left"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										2
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="left"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										Repayment amount
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="left"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="firstPaymentDate" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="paymentAmount" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<xsl:choose>
										<xsl:when
											test="$isFinalPaymentDifferent='N'">
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size" space-before="3pt"
												margin-left="2pt">
												<xsl:value-of select="numberOfPayments" />
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size" space-before="3pt"
												margin-left="2pt">
												<xsl:value-of select="numberOfPaymentsLessOne" />
											</fo:block>
										</xsl:otherwise>
									</xsl:choose>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="paymentFrequency" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<xsl:choose>
										<xsl:when
											test="$isFinalPaymentDifferent='N'">
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size" space-before="3pt"
												margin-left="2pt">
												<xsl:value-of
													select="finalPaymentDate" />
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size" space-before="3pt"
												margin-left="2pt">
												<xsl:value-of
													select="secondLastPaymentDate" />
											</fo:block>
										</xsl:otherwise>
									</xsl:choose>
								</fo:table-cell>
							</fo:table-row>
							<xsl:if
								test="$isFinalPaymentDifferent='Y'">
								<fo:table-row height="10mm">
									<fo:table-cell
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="normal-font-size" space-before="3pt"
											margin-left="2pt">
											3
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										display-align="center">
										<fo:block text-align="left"
											xsl:use-attribute-sets="normal-font-size" space-before="3pt"
											margin-left="2pt">
											Payment
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										display-align="center">
										<fo:block text-align="left"
											xsl:use-attribute-sets="normal-font-size" space-before="3pt"
											margin-left="2pt">
											<xsl:value-of
												select="finalPaymentDate" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										display-align="center">
										<fo:block text-align="right"
											xsl:use-attribute-sets="normal-font-size" space-before="3pt"
											margin-left="2pt">
											<xsl:value-of
												select="finalPaymentAmount" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										display-align="center">
										<fo:block text-align="right"
											xsl:use-attribute-sets="normal-font-size" space-before="3pt"
											margin-left="2pt">
											1
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										display-align="center">
										<fo:block text-align="right"
											xsl:use-attribute-sets="normal-font-size" space-before="3pt"
											margin-left="2pt">
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										display-align="center">
										<fo:block text-align="right"
											xsl:use-attribute-sets="normal-font-size" space-before="3pt"
											margin-left="2pt">
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:if>
						</fo:table-body>
					</fo:table>

					<!--  Normal Amortization Table  -->
					<fo:block font-size="10pt" space-before="20pt"
						space-after="12pt">
						AMORTIZATION SCHEDULE-Normal Amortization
					</fo:block>
					<fo:table border-collapse="collapse"
						table-layout="fixed">
						<fo:table-column column-width="20mm" />
						<fo:table-column column-width="35mm" />
						<fo:table-column column-width="35mm" />
						<fo:table-column column-width="35mm" />
						<fo:table-column column-width="35mm" />
						<fo:table-column column-width="35mm" />
						<fo:table-body>
							<fo:table-row height="10mm">
								<fo:table-cell
									xsl:use-attribute-sets="amort-sch-header-attr">
									<fo:block
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="amort-sch-header-attr">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" font-weight="bold"
										space-before="3pt" margin-left="2pt">
										Date
									</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="right"
									xsl:use-attribute-sets="amort-sch-header-attr">
									<fo:block
										xsl:use-attribute-sets="normal-font-size" font-weight="bold"
										space-before="3pt" margin-left="2pt">
										Payment
									</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="right"
									xsl:use-attribute-sets="amort-sch-header-attr">
									<fo:block
										xsl:use-attribute-sets="normal-font-size" font-weight="bold"
										space-before="3pt" margin-left="2pt">
										Interest
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="amort-sch-header-attr">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" font-weight="bold"
										space-before="3pt" margin-left="2pt">
										Principal
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="amort-sch-header-attr">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" font-weight="bold"
										space-before="3pt" margin-left="2pt">
										Balance
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="10mm">
								<fo:table-cell display-align="center">
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="right"
										space-before="3pt" margin-left="2pt">
										Loan
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="loanEffectiveDate" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="loanAmount" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<xsl:for-each select="loanPayment">

								<!--  If summaryInd = 'Y' then display the summary line  -->
								<xsl:variable name="summaryInd">
									<xsl:value-of select="summaryInd" />
								</xsl:variable>
								<fo:table-row>
									<xsl:if test="$summaryInd='N' ">
										<fo:table-cell>
											<fo:block
												xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
												text-align="right">
												<xsl:value-of
													select="count" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size"
												padding-top="5pt">
												<xsl:value-of
													select="paymentDate" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size"
												padding-top="5pt">
												<xsl:value-of
													select="paymentAmount" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size"
												padding-top="5pt">
												<xsl:value-of
													select="paymentAmountInterest" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size"
												padding-top="5pt">
												<xsl:value-of
													select="paymentAmountPrincipal" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size"
												padding-top="5pt">
												<xsl:value-of
													select="loanPrincipalRemaining" />
											</fo:block>
										</fo:table-cell>
									</xsl:if>
									<xsl:if test="$summaryInd='Y' ">
										<fo:table-cell>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size" padding-bottom="12pt"
												padding-top="9pt" font-weight="bold">
												<xsl:value-of
													select="totalLabel" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size" padding-bottom="12pt"
												padding-top="9pt">
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size" padding-bottom="12pt"
												padding-top="9pt" font-weight="bold">
												<xsl:value-of
													select="totalPayment" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size" padding-bottom="12pt"
												padding-top="9pt" font-weight="bold">
												<xsl:value-of
													select="totalInterest" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block text-align="right"
												xsl:use-attribute-sets="normal-font-size" padding-bottom="12pt"
												padding-top="9pt" font-weight="bold">
												<xsl:value-of
													select="totalPrincipal" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block
												xsl:use-attribute-sets="normal-font-size" padding-bottom="12pt"
												padding-top="9pt">
											</fo:block>
										</fo:table-cell>
									</xsl:if>
								</fo:table-row>
							</xsl:for-each>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
										font-weight="bold">
										 Grand Totals
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size"
										padding-top="5pt">
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
										font-weight="bold">
										<xsl:value-of
											select="grandTotals/grandtotalPayment" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
										font-weight="bold">
										<xsl:value-of
											select="grandTotals/grandTotalInterest" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size" padding-top="5pt"
										font-weight="bold">
										<xsl:value-of
											select="grandTotals/grandTotalPrincipal" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right"
										xsl:use-attribute-sets="normal-font-size"
										space-before="5pt">
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>