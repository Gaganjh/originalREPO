<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:strip-space elements="*" />

	<!--  Common Attributes  -->
	<xsl:attribute-set name="normal-font-size">
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	
	<!--  Variable Declarations  -->
	<xsl:variable name="loanStatus" select="PromissaryNote/loanStatus" />
	<xsl:variable name="participanInitiatedLoan" select="PromissaryNote/participanInitiatedLoan" />

	<!--  Main template begins -->
	<xsl:template match="PromissaryNote">
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
					<fo:block space-after="5mm" font-weight="bold"
						xsl:use-attribute-sets="normal-font-size">
						NON-NEGOTIABLE PROMISSORY NOTE AND IRREVOCABLE PLEDGE AND
						ASSIGNMENT
					</fo:block>
					<fo:block space-after="5mm"
						xsl:use-attribute-sets="normal-font-size" text-align="left">
						For value received, I,
						<xsl:value-of select="participantName" />, the undersigned borrower, promise to pay to
						<xsl:value-of select="planName" />
						(the "Plan"), or any assignee of this Non-negotiable Promissory Note and Irrevocable Pledge
						and Assignment ("Note"), the principal sum of
						$<xsl:value-of select="loanAmount" />,
						together with interest at the rate of
						<xsl:value-of select="loanInterestRate" />%
						per annum on the unpaid balance in
						the installments and at the times specified in the Amortization Schedule attached to this
						Note as Appendix A. Each installment payment is referred to as a "Scheduled Repayment".
						I hereby authorize the Employer to deduct the Scheduled Repayments required hereunder 
						from my employment compensation and to transmit the Scheduled Repayments to the Trustees 
						of the Plan in payment of this Note until the entire principal and interest thereon will 
						have been paid in full.  (The Scheduled Repayments shall be remitted by the Trustees to 
						John Hancock Life Insurance Company (U.S.A.) ("John Hancock"), the recordkeeper for the 
						Plan.)
					</fo:block>
					<fo:block space-after="5mm"
						xsl:use-attribute-sets="normal-font-size" text-align="left">
						The entire unpaid principal balance of this
						Note and all accrued interest shall become due and payable upon the earlier of
						(a) my termination of employment with
						<xsl:value-of select="contractName" />
						(the "Employer") or (b) my failure to make a
						scheduled repayment (each being a "Reportable Event").
						<xsl:value-of select="defaultProvision" />.
						Upon the occurrence of any Reportable Event, the amount of the outstanding loan balance 
						will be reported as a taxable deemed distribution to me from the Plan on IRS Form 1099-R and 
						subject to such tax penalties and income tax as may be applicable.
					</fo:block>
					<fo:block space-after="5mm"
						xsl:use-attribute-sets="normal-font-size" text-align="left">
						I hereby irrevocably assign and grant to the Trustees of the
						Plan a security interest of up to
						<xsl:value-of select="maximumLoanPercentage" />%
						of the vested amount of my account in the Plan
						to secure the repayment of all amounts due under
						this Note. No benefits of the Plan shall be
						distributed to me or to my designated
						beneficiary(ies) until this Note has been paid
						in full or otherwise fully satisfied. Should I
						fail to make full repayment on this Note upon
						the occurrence of a Reportable Event (including, without limitation, upon the termination 
						of my employment with the Employer for any reason), I authorize the Trustees to reduce the 
						amount otherwise distributable to me or to my designated beneficiary(ies) by the amount of 
						the outstanding indebtedness, together with any accrued interest thereon.
					</fo:block>
					<fo:block space-after="5mm"
						xsl:use-attribute-sets="normal-font-size" text-align="left">
						The terms of this Note apply to, inure to the benefit of, and bind all parties hereto, 
						their heirs, legatees, devisees, administrators, executors, successors and assigns. 
						This Note is non-negotiable.  The merger of the Plan with or into another qualified plan 
						or the transfer of my entire account balance, including this Note, to another plan shall 
						not constitute a negotiation of this Note.
					</fo:block>
					<fo:block space-after="5mm"
						xsl:use-attribute-sets="normal-font-size" text-align="left">
						The undersigned hereby waives diligence, presentment, protest and demand and also notice of 
						protest, demand, nonpayment, and dishonor of this Note.
					</fo:block>
					<fo:block space-after="5mm"
						xsl:use-attribute-sets="normal-font-size" text-align="left">
					    The administration, recordkeeping and accounting for payments on this Note and reporting 
					    of deemed distributions, if any, is, and shall be, performed by John Hancock, which is 
					    headquartered in Massachusetts. Accordingly, this Note shall be governed by the laws of the 
					    State of Massachusetts. <xsl:value-of select="participantName" />, the Plan and any assignee
					    of this Note, hereby irrevocably submit to the jurisdiction of the courts of the State of 
					    Massachusetts and hereby waive any and all venue and jurisdictional objections, whether 
					    personal or subject matter, thereto, and also consents to service of process by any means 
					    authorized pursuant to Massachusetts law. This Note may be prepaid in full at any time without penalty.
					</fo:block>
					<fo:block space-after="5mm"
						xsl:use-attribute-sets="normal-font-size" text-align="left"
						font-style="italic" font-weight="bold">
						I acknowledge that the terms and conditions of
						this Non-negotiable Promissory Note and Irrevocable Pledge and Assignment 
						are expressly subject to the
						provisions of the Plan, the administrative
						procedures established by the Plan Administrator
						from time to time, and my completed loan request
						form.
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
								rule-thickness="0.5pt" leader-length="70%" top="0pt" />
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