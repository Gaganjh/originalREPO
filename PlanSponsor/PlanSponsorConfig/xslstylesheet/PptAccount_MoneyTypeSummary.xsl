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

<xsl:attribute-set name="solid.blue.border.4sides">
	<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-start-style">solid</xsl:attribute>
	<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-end-style">solid</xsl:attribute>
	<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-before-style">solid</xsl:attribute>
	<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
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

<xsl:include href="pptAccountCommonDetails.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />
<xsl:include href="infoMessages.xsl" />

	<xsl:template match="pptAccount">
	
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
					<xsl:call-template name="pptAccountCommon"/>
					<!-- Showing Info Messages -->
					<xsl:call-template name="infoMessages"/>	
					
					<!--Showing the main Report Information -->
					<xsl:if test="asOfDateReportCurrent">
					<fo:block space-before="0.2cm">
						<fo:table>
							<xsl:if test="showLoanFeature">
								<fo:table-column column-width="33%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="33%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="34%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<xsl:if test="not(showLoanFeature)">
								<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="60%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<!-- Showing the main Report header-->
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Money Types
										</fo:block>
									</fo:table-cell>
									<xsl:if test="not(showLoanFeature)">
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
											<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Total Assets($)
											</fo:block>
										</fo:table-cell>
									</xsl:if>
									<xsl:if test="showLoanFeature">
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
											<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Total Assets Excluding Loans($)
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
											<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Loan Assets($)
											</fo:block>
										</fo:table-cell>
									</xsl:if>
								</fo:table-row>	
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
									<xsl:if test="showLoanFeature">
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
									</xsl:if>
								</fo:table-row>
							</fo:table-header>
					
							<fo:table-body>
								<!-- Show nothing if there is no main Report content-->
								<xsl:if test="not(reportDetail)">
									<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
								</xsl:if>
								<xsl:if test="reportDetail">	
								        <fo:table-row height="0.4cm">
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.4sides">
											<xsl:if test="showLoanFeature">
											<xsl:attribute name="number-columns-spanned">3</xsl:attribute>
										</xsl:if>
										<xsl:if test="not(showLoanFeature)">
											<xsl:attribute name="number-columns-spanned">2</xsl:attribute>
										</xsl:if>
										<fo:block/>
										</fo:table-cell>
									  </fo:table-row>
										<fo:table-row display-align="center">	
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides" text-align="left">
												<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													Employee contributions
												</fo:block>
											</fo:table-cell>
											<xsl:if test="not(reportDetail/totalContribLoan)">
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides" text-align="right">
													<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/eeContrib" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
											<xsl:if test="reportDetail/totalContribLoan">
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides" text-align="right">
													<fo:block text-align="right" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/eeContrib" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides" text-align="right">
													<fo:block text-align="right" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/eeContribLoan" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
										</fo:table-row>
									<xsl:for-each select="reportDetail/eeMoneyType">	
										<fo:table-row>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="moneyType" />
												</fo:block>
											</fo:table-cell>
											<xsl:if test="not(ancestor::*/totalContribLoan)">
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="balance" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
											<xsl:if test="ancestor::*/totalContribLoan">
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
															<xsl:value-of select="balance" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
															<xsl:value-of select="loanBalance" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
										</fo:table-row>	
									</xsl:for-each>
									<fo:table-row height="0.4cm">
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.4sides">
										<xsl:if test="showLoanFeature">
											<xsl:attribute name="number-columns-spanned">3</xsl:attribute>
										</xsl:if>
										<xsl:if test="not(showLoanFeature)">
											<xsl:attribute name="number-columns-spanned">2</xsl:attribute>
										</xsl:if>
											<fo:block/>
										</fo:table-cell>
									</fo:table-row>
										<fo:table-row display-align="center">	
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides" text-align="left">
												<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													Employer contributions
												</fo:block>
											</fo:table-cell>
											<xsl:if test="not(reportDetail/totalContribLoan)">
												
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/erContrib" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
											<xsl:if test="reportDetail/totalContribLoan">
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block text-align="right" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/erContrib" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block text-align="right" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/erContribLoan" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
										</fo:table-row>
									<xsl:for-each select="reportDetail/erMoneyType">	
										<fo:table-row>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides" text-align="left">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="moneyType" />
												</fo:block>
											</fo:table-cell>
											<xsl:if test="not(ancestor::*/totalContribLoan)">
												
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="balance" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
											<xsl:if test="ancestor::*/totalContribLoan">
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
															<xsl:value-of select="balance" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
															<xsl:value-of select="loanBalance" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
										</fo:table-row>	
									</xsl:for-each>
									<fo:table-row height="0.4cm">
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.4sides">
											<xsl:if test="showLoanFeature">
											<xsl:attribute name="number-columns-spanned">3</xsl:attribute>
										</xsl:if>
										<xsl:if test="not(showLoanFeature)">
											<xsl:attribute name="number-columns-spanned">2</xsl:attribute>
										</xsl:if>
										<fo:block/>
										</fo:table-cell>
									</fo:table-row>
								
										<fo:table-row display-align="center">	
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
												<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													Total
												</fo:block>
											</fo:table-cell>
											<xsl:if test="not(reportDetail/totalContribLoan)">
												
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block text-align="right" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/totalContrib" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
											<xsl:if test="reportDetail/totalContribLoan">
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block text-align="right" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/totalContrib" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block text-align="right" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/totalContribLoan" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											<xsl:if test="showLoanFeature">
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											</xsl:if>
										</fo:table-row>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:block>
						</xsl:if>
					
					<!-- Showing the PDF capped message and Footer-->	
					<xsl:call-template name="reportCommonFooter"/> 		
				</fo:flow>
			
			</fo:page-sequence>
			
		</fo:root>
	</xsl:template>
</xsl:stylesheet>