<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:strip-space elements="*" />

	<!--  Common Attributes  -->
	<xsl:attribute-set name="normal-font-size">
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="small-font">
		<xsl:attribute name="font-size">7pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="leader-attr">
		<xsl:attribute name="leader-pattern">rule</xsl:attribute>
		<xsl:attribute name="rule-thickness">0.5pt</xsl:attribute>
		<xsl:attribute name="leader-length">100%</xsl:attribute>
		<xsl:attribute name="space-before.optimum">0pt</xsl:attribute>
		<xsl:attribute name="space-after.optimum">24pt</xsl:attribute>
		<xsl:attribute name="color">black</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table-layout">
		<xsl:attribute name="border-collapse">collapse</xsl:attribute>
		<xsl:attribute name="table-layout">fixed</xsl:attribute>
		<xsl:attribute name="width">100%</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="check-box-attr">
		<xsl:attribute name="font-family">sans-serif</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="border">0.5pt solid black</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table-cell-attr">
		<xsl:attribute name="border-top-style">none</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-left-style">solid</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">thin</xsl:attribute>
		<xsl:attribute name="border-left-width">thin</xsl:attribute>
		<xsl:attribute name="border-right-width">thin</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table-border">
		<xsl:attribute name="border">0.5pt solid black</xsl:attribute>
	</xsl:attribute-set>

	<!--  Variable Declarations  -->
	<xsl:variable name="manulifeCompanyId">
		<xsl:value-of select="LoanForm/manulifeCompanyId" />
	</xsl:variable>
	<xsl:variable name="paymentMethodCode">
		<xsl:value-of select="LoanForm/paymentMethodCode" />
	</xsl:variable>
	<xsl:variable name="bankAccountTypeCode">
		<xsl:value-of select="LoanForm/bankAccountTypeCode" />
	</xsl:variable>
	<xsl:variable name="moneyTypeInd">
		<xsl:value-of select="LoanForm/moneyTypeInd" />
	</xsl:variable>
	<xsl:variable name="loanAmountDisplayInd">
		<xsl:value-of select="LoanForm/loanAmountDisplayInd" />
	</xsl:variable>
	<xsl:variable name="tpaLoanIssueFee">
		<xsl:value-of select="LoanForm/tpaLoanIssueFee" />
	</xsl:variable>
	<xsl:variable name="spousalConsentReqdInd">
		<xsl:value-of select="LoanForm/spousalConsentReqdInd" />
	</xsl:variable>
	<xsl:variable name="bundledGaIndicator">
		<xsl:value-of select="LoanForm/bundledGaIndicator" />
	</xsl:variable>

	<xsl:template match="LoanForm">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4"
					page-width="8.5in" page-height="11in" margin-top="0.5in"
					margin-bottom="0.2in" margin-left="1cm" margin-right="1cm">
					<fo:region-body extent="5cm" />
					<fo:region-before extent="5cm" />
					<fo:region-after extent="5cm" />
					<fo:region-start extent="5cm" />
					<fo:region-end extent="5cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="A4">
				<fo:flow flow-name="xsl-region-body">
					<fo:table xsl:use-attribute-sets="table-layout">
						<fo:table-column column-width="85mm" />
						<fo:table-column column-width="110mm" />
						<!-- JohnHancock Logo  -->
						<fo:table-body>
							<fo:table-row height="5mm">
								<fo:table-cell>
								<fo:block>
									<fo:external-graphic
										height="40px">
										<xsl:attribute name="src">
											url('<xsl:value-of select="logoPath" />')
										</xsl:attribute>
									</fo:external-graphic>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block space-after="3mm"
										font-size="11pt" font-weight="bold" font-style="normal"
										text-align="left">
										Loan Request
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:list-block
						provisional-distance-between-starts="3.45in"
						provisional-label-separation="3pt">
						<fo:list-item>
							<fo:list-item-label start-indent="3.35in"
								end-indent="3.35in">
								<fo:block font-size="8pt">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								start-indent="body-start()">
								<fo:block
									xsl:use-attribute-sets="small-font">
									To complete this form please read
									the instruction page attached to
									this form.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label start-indent="3.35in"
								end-indent="label-end()">
								<fo:block font-size="8pt">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								start-indent="body-start()">
								<fo:block
									xsl:use-attribute-sets="small-font">
									Participant completes page 1 of this
									form.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<xsl:if	test="$bundledGaIndicator='N'">
							<fo:list-item>
								<fo:list-item-label start-indent="3.35in"
									end-indent="label-end()">
									<fo:block font-size="8pt">
										&#x2022;
									</fo:block>
								</fo:list-item-label>
								<fo:list-item-body
									start-indent="body-start()">
									<fo:block
										xsl:use-attribute-sets="small-font">
										Plan Representative reviews page 1
										and completes page 2 of this form.
									</fo:block>
								</fo:list-item-body>
							</fo:list-item>
						</xsl:if>
					</fo:list-block>
					<fo:block>
						<fo:leader xsl:use-attribute-sets="leader-attr" />
					</fo:block>
					<fo:block space-after="3mm"
						xsl:use-attribute-sets="normal-font-size">
						Submission ID:
						<xsl:value-of select="submissionId" />
					</fo:block>

					<!-- Section - A -->
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-after="3mm" font-weight="bold" font-style="normal">
						Section A - General Information
						<xsl:apply-templates select="data/checked" />
					</fo:block>
					<fo:table xsl:use-attribute-sets="table-layout">
						<fo:table-column column-width="80%" />
						<fo:table-column column-width="20%" />
						<fo:table-body>
							<fo:table-row height="10mm">
								<fo:table-cell xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Contractholder Name
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<fo:table
											xsl:use-attribute-sets="table-layout">
											<fo:table-column
												column-width="114mm" />
											<fo:table-column
												column-width="40mm" />
											<fo:table-body>
												<fo:table-row
													height="5mm">
													<fo:table-cell>
														<fo:block
															xsl:use-attribute-sets="normal-font-size">
															The Trustees
															of
															<xsl:value-of
																select="planName" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block
															xsl:use-attribute-sets="normal-font-size"
															text-align="right" margin-right="2pt">
															Plan (the
															"Plan")
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Contract Number
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="contractId" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:table xsl:use-attribute-sets="table-layout"
						space-before="3pt">
						<fo:table-column column-width="80%" />
						<fo:table-column column-width="20%" />
						<fo:table-body>
							<fo:table-row height="10mm">
								<fo:table-cell xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Participant Name (Last Name,
										First Name, Initial)
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="participantName" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Social Security Number
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of select="ssn" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:table xsl:use-attribute-sets="table-layout"
						space-before="3pt">
						<fo:table-column column-width="80%" />
						<fo:table-column column-width="20%" />
						<fo:table-body>
							<fo:table-row height="10mm" >
								<fo:table-cell border-top-style="none" xsl:use-attribute-sets="table-cell-attr"
									number-columns-spanned="2">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Participant Address - Number,
										Street, Apt, City, State,
										ZipCode
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="participantAddress" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block>
						<fo:leader xsl:use-attribute-sets="leader-attr" />
					</fo:block>

					<!--  Section B - Paynment Instructions -->
					<fo:block space-after="3mm"
						xsl:use-attribute-sets="normal-font-size" font-weight="bold"
						font-style="normal">
						Section B - Payment Instructions to Participant
						Directly
					</fo:block>

					<!--  Section 1 - Electronic fund Transfer Information  -->
					<fo:block space-after="3mm"
						xsl:use-attribute-sets="normal-font-size" font-weight="bold"
						font-style="normal">
						Section 1 - Electronic fund Transfer Information
					</fo:block>

					<!--  Check boxes will be populated according to the paymentMethodCode  -->
					<fo:table xsl:use-attribute-sets="table-layout">
						<fo:table-column column-width="8pt" />
						<fo:table-column column-width="160pt" />
						<fo:table-column column-width="8pt" />
						<fo:table-column column-width="70pt" />
						<fo:table-column column-width="8pt" />
						<fo:table-column column-width="170pt" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding="0pt">
									<xsl:if
										test="$paymentMethodCode='AC' ">
										<fo:block
											xsl:use-attribute-sets="check-box-attr">
											x
										</fo:block>
									</xsl:if>
									<xsl:if
										test="$paymentMethodCode!='AC' ">
										<fo:block
											xsl:use-attribute-sets="check-box-attr">
											&#xA0;
										</fo:block>
									</xsl:if>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="left"
										margin-left="2mm">
										Direct Deposit to my (select
										one)
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="0pt">
									<xsl:if
										test="$paymentMethodCode='AC' and $bankAccountTypeCode='C' ">
										<fo:block
											xsl:use-attribute-sets="check-box-attr">
											x
										</fo:block>
									</xsl:if>
									<xsl:if
										test="$paymentMethodCode!='AC' or $bankAccountTypeCode!='C' ">
										<fo:block
											xsl:use-attribute-sets="check-box-attr">
											&#xA0;
										</fo:block>
									</xsl:if>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="left"
										margin-left="2mm">
										Checking or
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="0pt">
									<xsl:if
										test="$paymentMethodCode='AC' and $bankAccountTypeCode='S' ">
										<fo:block
											xsl:use-attribute-sets="check-box-attr">
											x
										</fo:block>
									</xsl:if>
									<xsl:if
										test="$paymentMethodCode!='AC' or $bankAccountTypeCode!='S'">
										<fo:block
											xsl:use-attribute-sets="check-box-attr">
											&#xA0;
										</fo:block>
									</xsl:if>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="left"
										margin-left="2mm">
										Savings Account
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block font-size="8pt" space-before="3pt"
						space-after="2pt">
						OR
					</fo:block>
					<fo:table xsl:use-attribute-sets="table-layout">
						<fo:table-column column-width="8pt" />
						<fo:table-column column-width="180mm" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding="0pt">
									<xsl:if
										test="$paymentMethodCode='WT' ">
										<fo:block
											xsl:use-attribute-sets="check-box-attr">
											x
										</fo:block>
									</xsl:if>
									<xsl:if
										test="$paymentMethodCode!='WT' ">
										<fo:block
											xsl:use-attribute-sets="check-box-attr">
											&#xA0;
										</fo:block>
									</xsl:if>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="left"
										margin-left="2mm">
										Wire - Verify with the receiving
										bank if they accept wires and/or
										charge a fee.
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block space-before="3mm"></fo:block>

					<!--  Bank Details  -->
					<fo:table xsl:use-attribute-sets="table-layout">
						<fo:table-column column-width="100%" />
						<fo:table-body>
							<fo:table-row height="9mm">
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Bank Name
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of select="bankName" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="3pt">
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="9mm">
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Bank ABA/Routing Number
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="abaRoutingNumber" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="3pt">
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="9mm">
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Bank Acount Number
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="bankAccountNumber" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block space-before="3mm"></fo:block>

					<!--  Section 2 - Check Information  -->
					<fo:table table-layout="fixed">
						<fo:table-column column-width="8pt" />
						<fo:table-column column-width="140pt" />
						<fo:table-column column-width="300pt" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding="0pt">
									<xsl:if
										test="$paymentMethodCode='CH' ">
										<fo:block
											xsl:use-attribute-sets="check-box-attr">
											x
										</fo:block>
									</xsl:if>
									<xsl:if
										test="$paymentMethodCode!='CH' ">
										<fo:block
											xsl:use-attribute-sets="check-box-attr">
											&#xA0;
										</fo:block>
									</xsl:if>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="left"
										margin-left="2mm" font-weight="bold">
										Section 2 - Check Information
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="left">
										- For distribution amounts over
										$50,000, use electronic fund
										transfer.
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block space-before="1mm" space-after="1mm"></fo:block>

					<!--  Mailing Address  -->
					<fo:table table-layout="fixed">
						<fo:table-column column-width="100%" />
						<fo:table-body>
							<fo:table-row height="27mm">
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Mailing address (complete if
										different from participant
										address in Section A) - Number,
										Street, Apt., State, Zip Code
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="distributionAddress" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block>
						<fo:leader xsl:use-attribute-sets="leader-attr" />
					</fo:block>

					<!--  Section C - Participant Signature  -->
					<fo:block space-after="3mm"
						xsl:use-attribute-sets="normal-font-size" font-weight="bold"
						font-style="normal">
						Section C - Participant Signature
					</fo:block>
					<fo:block space-before="3mm" space-after="3mm"
						xsl:use-attribute-sets="normal-font-size" font-weight="bold">
						For your protection, state law, where
						applicable, requires the following sentence to
						appear on this form. Any person who knowingly
						presents false or fraudulent claim for the
						payment of a loss is guilty of a crime and may
						be subject to fines and confinement in the state
						prison.
					</fo:block>
					<fo:table xsl:use-attribute-sets="table-layout">
						<fo:table-column column-width="40%" />
						<fo:table-column column-width="0.5%" />
						<fo:table-column column-width="39%" />
						<fo:table-column column-width="0.5%" />
						<fo:table-column column-width="20%" />
						<fo:table-body>
							<fo:table-row height="9mm">
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Signature of Participant
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Name - please print
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Date
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					
					<!-- Foot Notes -->
					<fo:block>
						<fo:footnote>
							<fo:inline />
							<fo:footnote-body>
								<fo:table
									xsl:use-attribute-sets="table-layout">
									<fo:table-column column-width="100mm" />
									<fo:table-column column-width="95mm" />
									<fo:table-body>
										<fo:table-row height="2mm">
											<fo:table-cell>
												<fo:block
													xsl:use-attribute-sets="small-font" text-align="left">
													GP5250US (11/2007)
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block xsl:use-attribute-sets="small-font"
													text-align="right">
													Page
													<fo:page-number />
													of
													<fo:page-number-citation
													ref-id="theEnd" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
								<fo:block
									xsl:use-attribute-sets="small-font" display-align="after">
									<fo:leader
										xsl:use-attribute-sets="leader-attr" />
									&#169; 2007 John Hancock Life Insurance
									Company
									<xsl:choose>
										<xsl:when
											test="$manulifeCompanyId='USA' ">
											(U.S.A) (John Hancock USA).
										</xsl:when>
										<xsl:otherwise>
											(NY) (John Hancock New York).
										</xsl:otherwise>
									</xsl:choose>
									All rights reserved
								</fo:block>
							</fo:footnote-body>
						</fo:footnote>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>

			<!--  Second Page  -->
			<fo:page-sequence master-reference="A4">
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<fo:leader xsl:use-attribute-sets="leader-attr" />
					</fo:block>
					<fo:table xsl:use-attribute-sets="table-layout">
						<fo:table-column column-width="80%" />
						<fo:table-column column-width="20%" />

						<fo:table-body>
							<fo:table-row height="9mm">
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Participant Name (Last Name,
										First Name, Initial)
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of
											select="participantName" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Social Security Number
									</fo:block>
									<fo:block font-weight="bold"
										xsl:use-attribute-sets="normal-font-size" space-before="3pt"
										margin-left="2pt">
										<xsl:value-of select="ssn" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block>
						<fo:leader xsl:use-attribute-sets="leader-attr" />
					</fo:block>

					<!--  Section D - Loan Information  -->
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-after="3mm">
						Section D - Loan Information
					</fo:block>
					<fo:table xsl:use-attribute-sets="table-layout">
						<fo:table-column column-width="16mm" />
						<fo:table-column column-width="25mm" />
						<fo:table-column column-width="20mm" />
						<fo:table-column column-width="15mm" />
						<fo:table-column column-width="15mm" />
						<fo:table-column column-width="15mm" />
						<fo:table-column column-width="20mm" />
						<fo:table-column column-width="25mm" />
						<fo:table-column column-width="15mm" />
						<fo:table-column column-width="20mm" />
						<fo:table-body>
							<fo:table-row height="9mm">
								<fo:table-cell display-align="center">
									<fo:block
										xsl:use-attribute-sets="small-font">
										Total Amount of Loan
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr"
									display-align="center">
									<fo:block
										xsl:use-attribute-sets="normal-font-size" margin-left="2pt">
										$<xsl:value-of select="loanAmount" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block
										xsl:use-attribute-sets="small-font" text-align="left"
										margin-left="8pt">
										Loan
									</fo:block>
									<fo:block
										xsl:use-attribute-sets="small-font" text-align="left"
										margin-left="8pt">
										Maturity Date
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" border-right-style="Solid" 
										border-right-width="thin" text-align="center" 
										space-before="3pt">
										Month
									</fo:block>
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="center"
										space-before="1mm">
										<xsl:value-of
											select="finalPaymentDate/month">
										</xsl:value-of>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell border-right-style="Solid" border-right-width="thin" 
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" text-align="center"
										space-before="3pt">
										Day
									</fo:block>
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="center"
										space-before="1mm">
										<xsl:value-of
											select="finalPaymentDate/day">
										</xsl:value-of>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" text-align="center"
										space-before="3pt">
										Year
									</fo:block>
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="center"
										space-before="1mm">
										<xsl:value-of
											select="finalPaymentDate/year">
										</xsl:value-of>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block
										xsl:use-attribute-sets="small-font" text-align="left"
										margin-left="8pt">
										Loan
									</fo:block>
									<fo:block
										xsl:use-attribute-sets="small-font" text-align="left"
										margin-left="8pt">
										Interest Rate
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr"
									display-align="center">
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="right">
										<xsl:value-of select="loanInterestRate" />%
									</fo:block>
								</fo:table-cell>
								<fo:table-cell display-align="center">
									<fo:block
										xsl:use-attribute-sets="small-font" text-align="center">
										Loan Type
									</fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr"
									display-align="center">
									<fo:block
										xsl:use-attribute-sets="normal-font-size" text-align="center">
										<xsl:value-of select="loanType" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block>
						<fo:leader xsl:use-attribute-sets="leader-attr" />
					</fo:block>

					<!--  Section E - Displayed if all the money types have 'Money_type_exclude' indicator equal to 'No'-->
					<xsl:if test="$moneyTypeInd='N' ">
						<fo:block
							xsl:use-attribute-sets="normal-font-size" font-weight="bold"
							space-after="3mm">
							Section E - Loan Withdrawal Order
						</fo:block>
						<fo:block
							xsl:use-attribute-sets="normal-font-size">
							<xsl:apply-templates
								select="manulifeCompanyId" />'s 
							Standard Loan Withdrawl Order will be
							used
						</fo:block>
					</xsl:if>

					<!--  Section E - Displayed if there is at least one money type that have the "Money_type_exclude" indicator equal to "Yes"  -->
					<xsl:if test="$moneyTypeInd='Y' ">
						<fo:block
							xsl:use-attribute-sets="normal-font-size" font-weight="bold"
							space-after="3mm">
							Section E - Loan Withdrawl Order
						</fo:block>

						<!-- Money types to be used table  -->
						<fo:table xsl:use-attribute-sets="table-layout"
							space-before="3mm" space-after="3mm">
							<fo:table-column column-width="45mm" />
							<fo:table-column column-width="40mm" />
							<fo:table-column column-width="40mm" />
							<fo:table-body>
								<fo:table-row height="5mm">
									<fo:table-cell
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="normal-font-size"
											text-align="left">
											Money Types to be used
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										xsl:use-attribute-sets="table-border"
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="small-font" text-align="center"
											font-weight="bold">
											Money Type
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										xsl:use-attribute-sets="table-border"
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="small-font" text-align="center"
											font-weight="bold">
											Amount
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<xsl:if
									test="$loanAmountDisplayInd='N' ">
									<xsl:for-each
										select="moneyTypesInc">
										<fo:table-row height="9mm">
											<fo:table-cell><fo:block></fo:block></fo:table-cell>
											<fo:table-cell
												xsl:use-attribute-sets="table-border"
												display-align="center">
												<fo:block
													xsl:use-attribute-sets="small-font" text-align="center">
													<xsl:value-of
														select="moneyType" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell
												xsl:use-attribute-sets="table-border"
												display-align="center">
												<fo:block
													xsl:use-attribute-sets="small-font" margin-left="3pt">
													$
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:if>
								<xsl:if
									test="$loanAmountDisplayInd='Y' ">
									<fo:table-row height="9mm">
										<fo:table-cell>
											<fo:block>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell
											xsl:use-attribute-sets="table-border"
											display-align="center">
											<fo:block
												xsl:use-attribute-sets="small-font" text-align="center">
												<xsl:value-of
													select="moneyTypesInc" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell
											xsl:use-attribute-sets="table-border"
											display-align="center">
											<fo:block
												xsl:use-attribute-sets="small-font" margin-left="3pt">
												$<xsl:value-of select="loanAmount" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<fo:table-row height="9mm">
									<fo:table-cell><fo:block></fo:block></fo:table-cell>
									<fo:table-cell
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="small-font" text-align="right"
											margin-right="2pt">
											Total
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="small-font" margin-left="3pt">
											$<xsl:if test="$loanAmountDisplayInd='Y'">
												<xsl:value-of
													select="loanAmount" />
											</xsl:if>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>

						<!--  Money types to be excluded table -->
						<fo:table
							xsl:use-attribute-sets="table-layout">
							<fo:table-column column-width="45mm" />
							<fo:table-column column-width="40mm" />
							<fo:table-column column-width="40mm" />
							<fo:table-column column-width="40mm" />
							<fo:table-body>
								<fo:table-row height="5mm">
									<fo:table-cell
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="normal-font-size"
											text-align="left">
											Money Types to be excluded
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										xsl:use-attribute-sets="table-border"
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="small-font" text-align="center"
											font-weight="bold">
											Money Type
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										xsl:use-attribute-sets="table-border"
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="small-font" text-align="center"
											font-weight="bold">
											Money Type
										</fo:block>
									</fo:table-cell>
									<fo:table-cell
										xsl:use-attribute-sets="table-border"
										display-align="center">
										<fo:block
											xsl:use-attribute-sets="small-font" text-align="center"
											font-weight="bold">
											Money Type
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<xsl:for-each select="moneyTypesExc">
									<fo:table-row height="7mm">
										<fo:table-cell><fo:block></fo:block></fo:table-cell>
										<fo:table-cell
											xsl:use-attribute-sets="table-border"
											display-align="center">
											<fo:block
												xsl:use-attribute-sets="small-font" text-align="center">
												<xsl:value-of
													select="moneyType1" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell
											xsl:use-attribute-sets="table-border"
											display-align="center">
											<fo:block
												xsl:use-attribute-sets="small-font" text-align="center">
												<xsl:value-of
													select="moneyType2" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell
											xsl:use-attribute-sets="table-border"
											display-align="center">
											<fo:block
												xsl:use-attribute-sets="small-font" text-align="center">
												<xsl:value-of
													select="moneyType3" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:for-each>
							</fo:table-body>
						</fo:table>
					</xsl:if>
					<fo:block space-before="3mm">
						<fo:leader xsl:use-attribute-sets="leader-attr" />
					</fo:block>

					<!--  Section F - Third Party Administrator -->
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-after="3mm">
						Section F - Third Party Administrator (TPA) Loan
						Issue Fee
					</fo:block>

					<!-- Displayed if tpaLoanIssueFee=0 or tpaLoanIssueFee=null -->
					<xsl:if
						test="$tpaLoanIssueFee='0.00' or $tpaLoanIssueFee=''">
						<fo:block xsl:use-attribute-sets="small-font">
							No fee will be applied
						</fo:block>
					</xsl:if>

					<!-- Displayed if tpaLoanIssueFee>0 -->
					<xsl:if test="$tpaLoanIssueFee &gt; '0'">
						<fo:block xsl:use-attribute-sets="small-font">
							Loan fee of $<xsl:value-of select="tpaLoanIssueFee" />
							will be applied
						</fo:block>
					</xsl:if>
					<fo:block space-before="3mm">
						<fo:leader xsl:use-attribute-sets="leader-attr" />
					</fo:block>

					<!--  Section - G  -->
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-after="3mm">
						Section G - Authorized Plan Representative
						Signature
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-after="3mm">
						If the participant fails to sign Section C -
						Participant Signature (page 1 of this form), the
						authorized Plan representative below certifies,
						under penalties of perjury, that based on the
						plan sponsor's record, the number shown on this
						form is the correct taxpayer identification
						number (Social Security Number) of the
						participant and that the information provided is
						correct.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-after="3mm">
						I hereby certify that the requested loan is not
						a taxable or a deemed distribution under the
						Internal Revenue Code, and that the loan
						compiles with the plan document and all
						pertinent laws.
						<xsl:if test="$spousalConsentReqdInd='N' ">
							I certify also that the loan is not a
							prohibited transaction as defined in the
							Internal Revenue Code Section 4975 or under
							the Employee Retirement Income Security Act.
						</xsl:if>
						<xsl:if test="$spousalConsentReqdInd='Y' ">
							I certify also that spousal consent (as
							required by Internal Revenue Code Section
							417) for married participants has been
							properly obtained and that the loan is not a
							prohibited transaction as defined in the
							Internal Revenue Code Section 4975 or under
							the Employee Retirement Income Security Act.
						</xsl:if>
						<xsl:if
							test="$spousalConsentReqdInd='U' or $spousalConsentReqdInd='' ">
							I certify also that spousal consent (as
							required by Internal Revenue Code Section
							417), if applicable, has been properly
							obatined and that the loan is not a
							prohibited transaction as defined in the
							Internal Revenue Code Section 4975 or under
							the Employee Retirement Income Security Act.
						</xsl:if>
						I direct
						<xsl:apply-templates select="manulifeCompanyId" />
						to pay to the Third Party Administrator the
						above referenced fee, which will be deducted
						from the participant's account at the time of
						the distribution. I understand and agree that
						this fee will be deducted and held in
						<xsl:apply-templates select="manulifeCompanyId" />
						general business account until paid to the Third
						Party Administrator. I hereby represent that the
						fee is authorized under the terms of the plan
						and that, in my fiduciary capacity, I have
						determined that the requested fee is a
						reasonable expense. The undersigned, on behalf
						of the Plan sponsor, the Plan and its related
						trust, agrees to hold harmless and indemnify
						<xsl:apply-templates select="manulifeCompanyId" />,
						its employees, agents or affiliates for acting
						on the instructions provided herein.
					</fo:block>
					<fo:table xsl:use-attribute-sets="table-layout">
						<fo:table-column column-width="40%" />
						<fo:table-column column-width="0.5%" />
						<fo:table-column column-width="39%" />
						<fo:table-column column-width="0.5%" />
						<fo:table-column column-width="20%" />						
						<fo:table-body>
							<fo:table-row height="9mm">
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Signature of Authorized Plan
										Representative
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Name - please print
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell
									xsl:use-attribute-sets="table-cell-attr">
									<fo:block
										xsl:use-attribute-sets="small-font" space-before="3pt"
										margin-left="2pt">
										Date
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					
					<!-- Foot Notes -->
					<fo:block>
						<fo:footnote>
							<fo:inline />
							<fo:footnote-body>
								<fo:table
									xsl:use-attribute-sets="table-layout">
									<fo:table-column column-width="100mm" />
									<fo:table-column column-width="95mm" />
									<fo:table-body>
										<fo:table-row height="2mm">
											<fo:table-cell>
												<fo:block
													xsl:use-attribute-sets="small-font" text-align="left">
													GP5250US (11/2007)
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block xsl:use-attribute-sets="small-font"
													text-align="right">
													Page
													<fo:page-number />
													of
													<fo:page-number-citation
													ref-id="theEnd" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
								<fo:block
									xsl:use-attribute-sets="small-font" display-align="after">
									<fo:leader
										xsl:use-attribute-sets="leader-attr" />
									&#169; 2007 John Hancock Life Insurance
									Company
									<xsl:choose>
										<xsl:when
											test="$manulifeCompanyId='USA' ">
											(U.S.A) (John Hancock USA).
										</xsl:when>
										<xsl:otherwise>
											(NY) (John Hancock New York).
										</xsl:otherwise>
									</xsl:choose>
									All rights reserved
								</fo:block>
							</fo:footnote-body>
						</fo:footnote>
					</fo:block>
					<fo:block id="theEnd" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<xsl:template match="manulifeCompanyId">
		<xsl:if test="$manulifeCompanyId='USA'">
			<xsl:text>John Hancock USA</xsl:text>
		</xsl:if>
		<xsl:if test="$manulifeCompanyId='NY'">
			John Hancock New York
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>