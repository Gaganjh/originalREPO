<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:attribute-set name="table-borderstyle">
<!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
</xsl:attribute-set>

<xsl:attribute-set name="solid.blue.border">
	<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-start-style">solid</xsl:attribute>
	<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="solid.blue.border.3sides">
	<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-start-style">solid</xsl:attribute>
	<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-end-style">solid</xsl:attribute>
	<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="arial_font.size_10">
	<xsl:attribute name="font-family">Arial</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="verdana_font.size_10">
	<xsl:attribute name="font-family">Verdana</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fonts_group1.size_11">
	<xsl:attribute name="font-family">Verdana, Arial, Helvetica, sans-serif</xsl:attribute>
	<xsl:attribute name="font-size">11pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fonts_group2.size_12">
	<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:variable name="loanType" select="loanRepaymentDetails/summaryInfo/loanType" />

<xsl:include href="reportCommonHeader.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />

	<xsl:template match="loanRepaymentDetails">
	
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- 8.5x11 page.-->
			<xsl:call-template name="pageDefinition"/>
				
			<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after" >
					<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
						Page <fo:page-number/> of <fo:page-number-citation-last ref-id="terminator"/> NOT VALID WITHOUT ALL PAGES
					</fo:block>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="reportCommonHeader"/>
					<fo:block space-before="0.5cm">
						 <fo:table>
							<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro1Text" />
										</fo:block>
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro2Text" />
										</fo:block>
									</fo:table-cell>

									<!-- Showing Summary Information -->
									<fo:table-cell padding-start="0.3cm"> <!--light grey color -->
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group2.size_12" font-weight="bold" background-color="#eceae3">
											<xsl:apply-templates select="summaryInfo/subHeader" />
										</fo:block>
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Name:
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/pptName" />
											</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												SSN: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/pptSSN" />
											</fo:inline>	
										</fo:block>
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>Loan #</fo:inline>
											<fo:inline><xsl:value-of select="summaryInfo/loanNumber" /></fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm"  xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>Type of Loan:</fo:inline>
											<fo:inline font-weight="bold"><xsl:value-of select="summaryInfo/loanType" /></fo:inline>
										</fo:block>
										
										<fo:block padding-start="0.3cm"  xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>Loan Reason:</fo:inline>
											<fo:inline font-weight="bold"><xsl:value-of select="summaryInfo/loanReason" /></fo:inline>
										</fo:block>
										
										
										<fo:block padding-start="0.3cm"  xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>Interest Rate:</fo:inline>
											<fo:inline font-weight="bold"><xsl:value-of select="summaryInfo/interest" /></fo:inline>
										</fo:block>
										
										<fo:block padding-start="0.3cm"  xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline><xsl:value-of select="summaryInfo/issueDateLabel" /></fo:inline>
											<fo:inline font-weight="bold"><xsl:value-of select="summaryInfo/issueDate" /></fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm"  xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>Loan Maturity Date:</fo:inline>
											<fo:inline font-weight="bold"><xsl:value-of select="summaryInfo/maturityDate" /></fo:inline>
										</fo:block>
										
										<fo:block></fo:block>
								<xsl:if test="moneyTypes/moneyTypeInfo">		
							<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">		
						 <fo:table>
							<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell   text-align="left">
										<fo:block xsl:use-attribute-sets="fonts_group1.size_11">
											Money Type
										</fo:block>
									</fo:table-cell>
									<fo:table-cell  text-align="right">
										<fo:block  xsl:use-attribute-sets="fonts_group1.size_11">
											Amount ($)
										</fo:block>
									</fo:table-cell>
									</fo:table-row>
								<xsl:for-each select="moneyTypes/moneyTypeInfo">
									<fo:table-row background-color="#eceae3" display-align="center">
										<fo:table-cell  text-align="left">
											<fo:block xsl:use-attribute-sets="fonts_group1.size_11">
												<xsl:value-of  select="moneyTypeName" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell   text-align="right">
											<fo:block  xsl:use-attribute-sets="fonts_group1.size_11">
												<fo:inline font-weight="bold"><xsl:value-of select="moneyTypeAmt" /></fo:inline>
											</fo:block>
										</fo:table-cell>
										</fo:table-row>
								</xsl:for-each>
								
								<fo:table-row>
									<fo:table-cell   text-align="left">
										<fo:block xsl:use-attribute-sets="fonts_group1.size_11">
											Total
										</fo:block>
									</fo:table-cell>
									<fo:table-cell   text-align="right">
										<fo:block  xsl:use-attribute-sets="fonts_group1.size_11">
											<fo:inline font-weight="bold"><xsl:value-of select="summaryInfo/totalMtyAmt" /></fo:inline>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
									<fo:table-row>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
								</fo:table-row>
					
						</fo:table-body>
				</fo:table>
				</fo:block>
				
				<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>  
											</fo:inline>
											<fo:inline font-weight="bold"> 
											</fo:inline>
				</fo:block>  
				</xsl:if>
					<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group2.size_12" font-weight="bold" background-color="#eceae3">
											<xsl:apply-templates select="summaryInfo/bodyHeader2" />
										</fo:block>
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Outstanding Balance As Of:
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/inquiryDate" />
											</fo:inline>
										</fo:block>  
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Outstanding Balance Total: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/outstandingBalance" />
											</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm"  xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Last Payment Total: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/lastRepaymentAmt" />
											</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Days Since Last Payment:
											</fo:inline>
											<xsl:if test="summaryInfo/daysSinceLastPayment">
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/daysSinceLastPayment" />
											</fo:inline>
											</xsl:if>
											<xsl:if test="not(summaryInfo/daysSinceLastPayment)">
												<fo:inline font-weight="bold">
													n/a
												</fo:inline>
											</xsl:if>
										</fo:block>
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Date of Last Payment:
											</fo:inline>
											<xsl:if test="summaryInfo/lastRepaymentDate">
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/lastRepaymentDate" />
												</fo:inline>
											</xsl:if>
											<xsl:if test="not(summaryInfo/lastRepaymentDate)">
												<fo:inline font-weight="bold">
													n/a
												</fo:inline>
											</xsl:if>
										</fo:block>
									</fo:table-cell>
									</fo:table-row>
									</fo:table-body>
									</fo:table>
									
			</fo:block>
					
					<!-- Showing Info Messages -->
					<xsl:if test="not(txnDetails)">
						<fo:block font-weight="bold" padding-before="1cm" padding-after="0.2cm">Information Message</fo:block>
						<fo:block padding-before="0.2cm" padding-after="0.2cm">
							There are no loan repayment details for this loan.
						</fo:block>
					</xsl:if>
					
					
					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row background-color="#deded8">
									<fo:table-cell padding-start="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt">
											<xsl:apply-templates select="bodyHeader1" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					
					<!-- Showing the report -->
					<fo:block space-before="0.1cm">
						<fo:table>
							<fo:table-column column-width="17%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="17%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="17%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="17%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="17%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
							
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Date
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Activity
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Amount($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Principal($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Interest($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Loan Balance($)
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							
							<fo:table-body>
								<xsl:for-each select="txnDetails/txnDetail">
									<fo:table-row display-align="center">
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="transferDate" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="typeDesc" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="amt" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="principal" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="interest" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="balance" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:for-each>
								<fo:table-row keep-with-previous="always">
									<fo:table-cell>
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>	
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>	
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>

					<!-- Showing the PDF capped message and Footer-->
					<xsl:call-template name="reportCommonFooter"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
</xsl:stylesheet>