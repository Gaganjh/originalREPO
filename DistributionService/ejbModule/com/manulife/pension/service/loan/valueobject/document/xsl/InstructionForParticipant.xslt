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

	<!--  Variable Declarations  -->
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
					<fo:block xsl:use-attribute-sets="normal-font-size" font-weight="bold"
						text-align="center">
						LOAN PACKAGE INSTRUCTIONS FOR THE PARTICIPANT
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size" space-before="20pt">
						This loan package contains a number of forms.
						See below for instructions on each form
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size" space-before="8pt">
						Review, complete any required information, sign
						and date the following forms:
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
								<fo:block xsl:use-attribute-sets="normal-font-size"
									font-weight="bold">
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
								<fo:block xsl:use-attribute-sets="normal-font-size"
									font-weight="bold">
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
								<fo:block xsl:use-attribute-sets="normal-font-size">
									<fo:inline font-weight="bold">
										Amortization Schedule:
									</fo:inline>
									Review the Schedule of payments to
									be paid on your loan once approved
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
									<fo:block xsl:use-attribute-sets="normal-font-size">
										<fo:inline font-weight="bold">
											Consent of Spouse:
										</fo:inline>
										Your Plan Administrator will
										give you the form. Note that the
										form requires your spouse's
										signature as well as a Notary
										Public or the Plan
										Administrator.
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
								<fo:block font-weight="bold"
									xsl:use-attribute-sets="normal-font-size">
									Loan Request Form
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
								<fo:block xsl:use-attribute-sets="normal-font-size">
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
								<fo:block xsl:use-attribute-sets="normal-font-size">
									Section A - General Information -
									Verify your name and social security
									number, make changes if applicable.
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
								<fo:block xsl:use-attribute-sets="normal-font-size">
									Section B - Payment Instructions to
									Participant Directly - Complete
									either Section 1 Electronic Fund
									Transfer Information or Section 2
									Check Information. If both are
									completed we will use the
									instructions provided in Section 1.
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
								<fo:block xsl:use-attribute-sets="normal-font-size">
									Section C - Participant Signature -
									Ensure that the appropriate
									signature is on the form.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
					</fo:list-block>
					<fo:block xsl:use-attribute-sets="normal-font-size" space-before="8pt"
						font-weight="bold">
						IMPORTANT:
					</fo:block>
					<xsl:if test="$tpaLoanIssueFee &gt; '0'">
						<fo:block xsl:use-attribute-sets="normal-font-size" space-before="8pt">
							<fo:inline font-weight="bold">
								TPA Loan Issue Fee:
							</fo:inline>
							The Third Party Administrator for your plan
							charges a loan issue fee of
							$<xsl:value-of select="tpaLoanIssueFee" />
							for each loan issued.
							<xsl:apply-templates
								select="manulifeCompanyId" />
							will automatically deduct the loan issue fee
							from your account after the loan has been
							issued.
							<xsl:apply-templates
								select="manulifeCompanyId" />
							will forward the loan issue fee directly to
							the Third Party Administrator.
						</fo:block>
					</xsl:if>
					<xsl:if test="($loanType='Hardship')">
						<fo:block xsl:use-attribute-sets="normal-font-size" space-before="8pt">
							<fo:inline font-weight="bold">
								Hardship Loan:
							</fo:inline>
							You have indicated that you are taking a
							loan as a hardship distribution. You must
							provide documentation (i.e. purchase offer,
							notice of eviction or foreclose, medical
							bills, tuition bills) with the above
							documents to support this, in order for your
							loan to be processed.
						</fo:block>
					</xsl:if>
					<fo:block xsl:use-attribute-sets="normal-font-size" space-before="8pt">
						Return the following forms to your Plan
						Administrator for review and authorization:
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
								<fo:block xsl:use-attribute-sets="normal-font-size">
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
								<fo:block xsl:use-attribute-sets="normal-font-size">
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
								<fo:block xsl:use-attribute-sets="normal-font-size">
									Amortization Schedule
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
									<fo:block xsl:use-attribute-sets="normal-font-size">
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
								<fo:block xsl:use-attribute-sets="normal-font-size">
									Loan Request form
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
								<fo:block xsl:use-attribute-sets="normal-font-size">
									Loan Package Instructions for the
									Plan Administrator
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<xsl:if test="($loanType='Hardship')">
							<fo:list-item>
								<fo:list-item-label
									xsl:use-attribute-sets="outer-list-label-start-indent">
									<fo:block xsl:use-attribute-sets="normal-font-size">
									&#x2022;
									</fo:block>
								</fo:list-item-label>
								<fo:list-item-body
									xsl:use-attribute-sets="outer-list-body-start-indent">
									<fo:block xsl:use-attribute-sets="normal-font-size">
										Documentation to support hardship
										distribution as described above
									</fo:block>
								</fo:list-item-body>
							</fo:list-item>
						</xsl:if>
					</fo:list-block>
					<fo:block xsl:use-attribute-sets="normal-font-size" space-before="8pt">
						Your Plan Administrator will review and
						authorize these forms and forward the loan
						request to
						<xsl:apply-templates select="manulifeCompanyId" />
						for processing and will provide you a copy.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size" space-before="8pt">
						NOTE: The information contained in this
						instruction sheet and the accompanying forms is
						valid until
						<xsl:value-of select="loanStatusCreateDate" />.
						Ensure that all completed forms are forwarded
						to your Plan Administrator prior to this date.
						Failure to do so will require a new request for
						plan information and loan package.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size" space-before="20pt">
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