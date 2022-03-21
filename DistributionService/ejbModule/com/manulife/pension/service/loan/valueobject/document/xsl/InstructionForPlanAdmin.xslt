<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:strip-space elements="*" />
	
	<!--  Common Attributes  -->
	<xsl:attribute-set name="normal-font-size">
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="outer-list-label-start-indent">
		<xsl:attribute name="start-indent">8pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="outer-list-body-start-indent">
		<xsl:attribute name="start-indent">20pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="inner-list-label-start-indent">
		<xsl:attribute name="start-indent">20pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="inner-list-body-start-indent">
		<xsl:attribute name="start-indent">32pt</xsl:attribute>
	</xsl:attribute-set>

	<!-- Variable Declarations -->
	<xsl:variable name="spousalConsentReqdInd">
		<xsl:value-of select="LoanInstruction/spousalConsentReqdInd" />
	</xsl:variable>
	<xsl:variable name="legallyMarriedInd">
		<xsl:value-of select="LoanInstruction/legallyMarriedInd" />
	</xsl:variable>
	<xsl:variable name="tpaLoanIssueFee">
		<xsl:value-of select="LoanInstruction/tpaLoanIssueFee" />
	</xsl:variable>
	<xsl:variable name="loanType">
		<xsl:value-of select="LoanInstruction/loanType" />
	</xsl:variable>
	<xsl:variable name="sectionEVerADisplayInd">
		<xsl:value-of select="LoanInstruction/sectionEVerADisplayInd" />
	</xsl:variable>
	<xsl:variable name="sectionEVerBDisplayInd">
		<xsl:value-of select="LoanInstruction/sectionEVerBDisplayInd" />
	</xsl:variable>
	<xsl:variable name="tpaFirmName">
		<xsl:value-of select="LoanInstruction/tpaFirmName" />
	</xsl:variable>
	<xsl:variable name="manulifeCompanyId">
		<xsl:value-of select="LoanInstruction/manulifeCompanyId" />
	</xsl:variable>

	<!--  Main template begins -->
	<xsl:template match="LoanInstruction">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4"
					page-width="8.5in" page-height="11in" margin-top="0.5in"
					margin-bottom="0.5in" margin-left="1cm" margin-right="1cm">
					<fo:region-body extent="5cm" />
					<fo:region-before extent="5cm" />
					<fo:region-after extent="5cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="A4">
				<fo:flow flow-name="xsl-region-body">
					<fo:block xsl:use-attribute-sets="normal-font-size"
						text-align="center" font-weight="bold">
						LOAN PACKAGE INSTRUCTIONS FOR THE PLAN
						ADMINISTRATOR
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="20pt">
						Attached is the loan package from the
						<xsl:value-of select="contractName" />
						for
						<xsl:value-of select="participantName" />
						for the loan amount of $<xsl:value-of select="loanAmount" />
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						 	 padding-top="10pt">
						In addition to this sheet, you should have
						received a signed copy of each of the following
						forms from the Participant:
					</fo:block>
					<fo:list-block provisional-label-separation="3pt">
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="outer-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="outer-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Non-negotiable Promissory Note and Irrevocable
									Pledge and Assignment
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="outer-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="outer-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Truth In Lending Notice
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="outer-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="outer-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Amortization Schedule (does not
									require a signature)
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<xsl:if
							test="($spousalConsentReqdInd='Y' or $spousalConsentReqdInd='U' or $spousalConsentReqdInd='') and $legallyMarriedInd='Y' ">
							<fo:list-item>
								<fo:list-item-label
									xsl:use-attribute-sets="outer-list-label-start-indent">
									<fo:block xsl:use-attribute-sets="normal-font-size">
										&#x2022;
									</fo:block>
								</fo:list-item-label>
								<fo:list-item-body
									xsl:use-attribute-sets="outer-list-body-start-indent">
									<fo:block
										xsl:use-attribute-sets="normal-font-size">
										Consent of Spouse
									</fo:block>
								</fo:list-item-body>
							</fo:list-item>
						</xsl:if>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="outer-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="outer-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Loan request form:
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
					</fo:list-block>
					<fo:list-block provisional-label-separation="3pt">
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="inner-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="inner-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									The Participant completes page 1 of
									the loan request form and the Plan
									Administrator completes page 2.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="inner-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="inner-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Read the Instructions for completing
									a loan request and the note section
									at the bottom of that page.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="inner-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="inner-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Section D - Loan Information. Verify
									total amount of loan, loan maturity
									date and loan interest rate.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<xsl:if test="($sectionEVerADisplayInd='Y')">
							<fo:list-item>
								<fo:list-item-label
									xsl:use-attribute-sets="inner-list-label-start-indent">
									<fo:block xsl:use-attribute-sets="normal-font-size">
										&#x2022;
									</fo:block>
								</fo:list-item-label>
								<fo:list-item-body
									xsl:use-attribute-sets="inner-list-body-start-indent">
									<fo:block
										xsl:use-attribute-sets="normal-font-size">
										Section E - Loan Withdrawal
										Order.
										<xsl:apply-templates
											select="manulifeCompanyId" />
										Standard Loan Withdrawal Order
										will be used.
									</fo:block>
								</fo:list-item-body>
							</fo:list-item>
						</xsl:if>
						<xsl:if test="($sectionEVerBDisplayInd='Y')">
							<fo:list-item>
								<fo:list-item-label
									xsl:use-attribute-sets="inner-list-label-start-indent">
									<fo:block xsl:use-attribute-sets="normal-font-size">
										&#x2022;
									</fo:block>
								</fo:list-item-label>
								<fo:list-item-body
									xsl:use-attribute-sets="inner-list-body-start-indent">
									<fo:block
										xsl:use-attribute-sets="normal-font-size">
										Section E - Loan Withdrawal
										Order. Verify the money types to
										be used and enter the amount for
										each money type.
									</fo:block>
								</fo:list-item-body>
							</fo:list-item>
						</xsl:if>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="inner-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="inner-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Section F - Third Party
									Administrator (TPA) Loan Issue Fee.
									The fee will be automatically
									inputted onto the form.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="inner-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="inner-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Section G - Authorized Plan
									Representative Signature. Ensure
									that the appropriate signature is on
									the form.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<xsl:if test="($loanType='Hardship')">
							<fo:list-item>
								<fo:list-item-label
									xsl:use-attribute-sets="inner-list-label-start-indent">
									<fo:block xsl:use-attribute-sets="normal-font-size">
										&#x2022;
									</fo:block>
								</fo:list-item-label>
								<fo:list-item-body
									xsl:use-attribute-sets="inner-list-body-start-indent">
									<fo:block
										xsl:use-attribute-sets="normal-font-size">
										Note: The participant has
										indicated that the reason for
										the loan is a hardship
										distribution. The participant
										must include documentation to
										support this (i.e. purchase
										offer, notice of eviction or
										foreclose, medical bills,
										tuition bills) in order for the
										loan to be processed. Review
										documentation to ensure that it
										complies with the provisions in
										your Plan Document.
									</fo:block>
								</fo:list-item-body>
							</fo:list-item>
						</xsl:if>
					</fo:list-block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="10pt">
						Make 2 copies of all forms.
					</fo:block>
					<fo:list-block provisional-label-separation="3pt">
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="outer-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="outer-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Retain the original copy of all
									paperwork for the plan records
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="outer-list-label-start-indent">
								<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="outer-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Return one set of copies to the
									Participant.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<xsl:if test="($tpaFirmName!='')">
							<fo:list-item>
								<fo:list-item-label
									xsl:use-attribute-sets="outer-list-label-start-indent">
									<fo:block xsl:use-attribute-sets="normal-font-size">
										&#x2022;
									</fo:block>
								</fo:list-item-label>
								<fo:list-item-body
									xsl:use-attribute-sets="outer-list-body-start-indent">
									<fo:block
										xsl:use-attribute-sets="normal-font-size">
										The second set of copies should
										be sent to
										<xsl:value-of
											select="tpaFirmName" />
									</fo:block>
								</fo:list-item-body>
							</fo:list-item>
						</xsl:if>
					</fo:list-block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="10pt">
						Send the completed loan request form to your
						client account representative at
						<xsl:apply-templates select="manulifeCompanyId" />
						to have the requested loan amount distributed.
						The form may be faxed to (866)377-9577, or
						mailed to the following address:
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="20pt" space-before="10pt">
						<xsl:apply-templates select="manulifeCompanyId" />
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="20pt">
						Group Pensions
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="20pt">
						P.O. Box 600
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="20pt">
						Buffalo, NY 14210-0600
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="10pt">
						Remember to set up payroll deduction for the
						loan repayments.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="10pt">
						NOTE: The information contained in this
						instruction sheet and the accompanying forms is
						valid until <xsl:value-of select="loanStatusCreateDate" />.
						Ensure that all completed
						forms are forwarded to you by the Participant
						prior to this date. Failure to do so will
						require a new request for plan information and
						loan package by the Participant.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="20pt">
						Print Date
						<xsl:value-of select="currentDate" />
					</fo:block>
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