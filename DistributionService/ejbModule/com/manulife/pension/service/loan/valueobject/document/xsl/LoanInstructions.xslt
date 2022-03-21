<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:strip-space elements="*" />

	<!--  Common Attributes -->
	<xsl:attribute-set name="normal-font-size">
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="footerleader">
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
	</xsl:attribute-set>
	<xsl:attribute-set name="numbered-list-label-start-indent">
		<xsl:attribute name="start-indent">8pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="alphabet-list-label-start-indent">
		<xsl:attribute name="start-indent">20pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="numbered-list-body-start-indent">
		<xsl:attribute name="start-indent">20pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="alphabet-list-body-start-indent">
		<xsl:attribute name="start-indent">32pt</xsl:attribute>
	</xsl:attribute-set>

	<!--  Variable Declaration -->
	<xsl:variable name="manulifeCompanyId">
		<xsl:value-of select="LoanForm/manulifeCompanyId" />
	</xsl:variable>
	<xsl:variable name="moneyTypeInd">
		<xsl:value-of select="LoanForm/moneyTypeInd" />
	</xsl:variable>

	<!--  Main template begins -->
	<xsl:template match="LoanForm">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4"
					page-width="8.5in" page-height="11.5in" margin-top="0.4in"
					margin-bottom="0.2in" margin-left="1cm" margin-right="1cm">
					<fo:region-body extent="5cm" />
					<fo:region-before extent="5cm" />
					<fo:region-after extent="5cm" />
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
									<fo:block
										xsl:use-attribute-sets="normal-font-size" font-weight="bold"
										text-align="left">
										Instructions for completing
									</fo:block>
									<fo:block
										xsl:use-attribute-sets="normal-font-size" font-weight="bold"
										text-align="left">
										Loan Request
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block>
						<fo:leader
							xsl:use-attribute-sets="footerleader" />
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold">
						Participant Instructions for completing page1
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-before="8pt" margin-left="10pt">
						Section A - General Information
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="10pt">
						Verify contractholder name, contract number,
						participant's name, social security number and
						address. If participant's address is left blank,
						John Hancock USA will use the address currently
						on record.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-before="8pt" margin-left="10pt">
						Section B - Complete Payment instructions to
						participant Directly
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-before="8pt" margin-left="10pt">
						Section 1 - Electronic Fund Transfer
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="10pt">
						This option is recommended for ALL distributions
						for more timely access to your funds. Choose
						this option for distributions amounts over
						$50,000. We will not deposit into a third party
						account.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="8pt" margin-left="20pt">
						<fo:inline font-weight="bold">
							Direct Deposit
						</fo:inline>
						&#xA0;&#xA0;Your bank requires you to indicate
						whether this is a checking or savings account.
						Provide your bank's name,
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="	85pt">
						&#xA0;&#xA0;ABA routing number (verify with
						bank) and your bank account number.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="8pt" margin-left="62pt">
						<fo:inline font-weight="bold">Wire</fo:inline>
						&#xA0;&#xA0;Provide your banks's name, ABA
						routing number (verify with bank) and your bank
						account number.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="	85pt">
						&#xA0;&#xA0;NOTE: The receiving bank may not
						accept wires or may charge a fee to accept the
						incoming wire, contact your bank 
						&#xA0;&#xA0;if you have any	questions.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						space-before="8pt" margin-left="10pt">
						<fo:inline font-weight="bold">
							Section 2 - Check
						</fo:inline>
						- Allow 5 - 10 business days for mailing.
						Complete mailing address if different from
						participant address in Section A. If left blank,
						John Hancock USA will use the address currently
						on record.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-before="8pt" margin-left="10pt">
						Section C - Participant Signature
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="10pt">
						Ensure that the appropriate signature is on the
						form.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="10pt">
						Your plan may require you to provide supporting
						documents or additional information before your
						request can be processed. Contact your plan
						administrator.
					</fo:block>
					<fo:block>
						<fo:leader
							xsl:use-attribute-sets="footerleader" />
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold">
						Authorized Plan Representative
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-before="8pt" margin-left="10pt">
						Section D - Loan Information
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="10pt">
						Verify total loan amount, loan maturity date and
						loan interest rate.
					</fo:block>
					
					<!--  Section E - Displayed if all the money types have 'Money_type_exclude' indicator equal to 'No'-->
					<xsl:if test="$moneyTypeInd='N' ">
						<fo:block xsl:use-attribute-sets="normal-font-size"
							font-weight="bold" space-before="8pt" margin-left="10pt">
							Section E - Loan Withdrawal Order
						</fo:block>
						<fo:block xsl:use-attribute-sets="normal-font-size"
							margin-left="10pt">
							<xsl:apply-templates select="manulifeCompanyId" />'s 
							Standard Loan Withdrawal Order will be
							used
						</fo:block>
					</xsl:if>
					
					<!--  Section E - Displayed if there is at least one money type that have the "Money_type_exclude" indicator equal to "Yes"  -->
					<xsl:if test="$moneyTypeInd='Y' ">
						<fo:block xsl:use-attribute-sets="normal-font-size"
							font-weight="bold" space-before="8pt" margin-left="10pt">
							Section E - Loan Withdrawal Order
						</fo:block>
						<fo:block xsl:use-attribute-sets="normal-font-size"
							margin-left="10pt">
							Verify the money type to be used and enter
							the amount for each money type.
						</fo:block>
					</xsl:if>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-before="8pt" margin-left="10pt">
						Section F - Third Party Administrator (TPA) Loan
						Issue Fee
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="10pt">
						The fee will be deducted from the Participant's
						account after the loan amount has been removed
						using
						<xsl:apply-templates select="manulifeCompanyId" />'s 
						standard protocol and will be paid to the TPA
						currently on record with
						<xsl:apply-templates select="manulifeCompanyId" />.
						<xsl:apply-templates select="manulifeCompanyId" />
						is not responsible for any uncollected fee
						amounts as a result of insufficient funds. These
						shortages will be reported on the transaction
						and summary confirmations.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-before="8pt" margin-left="10pt">
						Section G - Authorized Plan Representative
						Signature
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="10pt">
						Ensure that the appropriate signature is on the
						form. Any changes to the information provided on
						this form require proper authorization.
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						margin-left="10pt">
						If Section C - Participant's Signature has been
						obtained seperately, certification will be
						provided under the Authorized Plan
						Representative signature section.
					</fo:block>
					<fo:block>
						<fo:leader
							xsl:use-attribute-sets="footerleader">
						</fo:leader>
					</fo:block>
					<fo:block xsl:use-attribute-sets="normal-font-size"
						font-weight="bold" space-after="8pt">
						Notes
					</fo:block>
					
					<!--  Numbered list starts  -->
					<fo:list-block provisional-label-separation="3pt">
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="numbered-list-label-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									1.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="numbered-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									It is the responsibility of the plan
									administrator to ensure that:
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						
						<!--  Alphabet list starts  -->
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="alphabet-list-label-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									a.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="alphabet-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									participant loan qualification
									requirements are met (according to
									the plan document)
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="alphabet-list-label-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									b.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="alphabet-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									each granted loan meets the tax law
									requiremetns and is not treated as a
									distribution
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="alphabet-list-label-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									c.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="alphabet-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									if the participant named above has
									more than one outstanding loan or
									the loan requested on this form is
									used to refinance an outstanding
									loan, the loans collectively, as
									well as each of the prior loan and
									the additional loan, meet the
									applicable tax law requirements and
									are not treated as distributions
									under the participant's plan.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="alphabet-list-label-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									d.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="alphabet-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									any restriction that the plan may
									have on the number of outstanding
									loans that a participant may have at
									any one time is complied with;
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="alphabet-list-label-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									e.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="alphabet-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									each plan loan is not a prohibited
									transaction:
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label
								xsl:use-attribute-sets="alphabet-list-label-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									f.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="alphabet-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									John Hancock Life Insurance Company
									<xsl:call-template
										name="countryName" />
									("<xsl:apply-templates select="manulifeCompanyId" />") 
									will be notified if and when a
									loan is deemed to be a distribution
									for tax purposes.
									<xsl:apply-templates
										select="manulifeCompanyId" />
									is not responsible for preparing and
									filing any tax reporting until and
									unless such notification has been
									provided by the plan administrator
									and if such service is available
									under your contract.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<!--  Alphabet list ends  -->
						<fo:list-item space-before="8pt">
							<fo:list-item-label
								xsl:use-attribute-sets="numbered-list-label-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									2.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="numbered-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									When
									<xsl:apply-templates
										select="manulifeCompanyId" />
									receives a payment, we will reduce
									the Loan Account balance by the
									amount of the repayment.
								</fo:block>
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									Loan repayments will be applied
									first to interest owing on the loan,
									and then to the principal. All loan
									repayments must be remitted through
									your plan trustee. Do not send
									personal checks directly to
									<xsl:apply-templates select="manulifeCompanyId" />.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item space-before="8pt">
							<fo:list-item-label
								xsl:use-attribute-sets="numbered-list-label-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									3.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body
								xsl:use-attribute-sets="numbered-list-body-start-indent">
								<fo:block
									xsl:use-attribute-sets="normal-font-size">
									This loan request does not
									constitute a promissory note or loan
									agreement.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
					</fo:list-block>
					<!--  Numbered list ends  -->

					<!-- Foot Notes section -->					
					<fo:block>
						<fo:footnote>
							<fo:inline />
							<fo:footnote-body>
								<fo:block font-size="7pt"
									display-align="after">
									GP5250US (11/2007)
									<fo:leader
										xsl:use-attribute-sets="footerleader" />
										&#169; 2007 John Hancock Life Insurance
										Company
										<xsl:call-template name="countryName" />
										(<xsl:apply-templates select="manulifeCompanyId" />). All rights reserved
								</fo:block>
							</fo:footnote-body>
						</fo:footnote>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!--  Returns either 'John Hancock USA' or 'John Hancock New York' -->
	<xsl:template match="manulifeCompanyId">
		<xsl:if test="$manulifeCompanyId='USA'">
			<xsl:text>John Hancock USA</xsl:text>
		</xsl:if>
		<xsl:if test="$manulifeCompanyId='NY'">
			<xsl:text>John Hancock New York</xsl:text>
		</xsl:if>
	</xsl:template>
	
	<!--  Returns either 'USA' or 'NY'  -->
	<xsl:template name="countryName">
		<xsl:if test="$manulifeCompanyId='USA'">
			<xsl:text>(U.S.A)</xsl:text>
		</xsl:if>
		<xsl:if test="$manulifeCompanyId='NY'"><xsl:text>(NY)</xsl:text></xsl:if>
	</xsl:template>
</xsl:stylesheet>