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

<xsl:attribute-set name="verdana_font.size_8">
	<xsl:attribute name="font-family">Verdana</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fonts_group2.size_12">
	<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="arial_font.size_9">
	<xsl:attribute name="font-family">Arial</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="verdana_font.size_10">
	<xsl:attribute name="font-family">Verdana</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:include href="reportCommonHeader.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />
<xsl:include href="HtmlToFoTransform.xsl" />

	<xsl:template match="benefitBase">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- 11x8.5 page.-->
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
						<fo:table table-layout="auto">
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro1Text" />
										</fo:block>
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro2Text" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					<xsl:if test="messageText">
						<fo:table table-layout="auto" space-before="0.5cm">
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm">
									<fo:block font-weight="bold" padding-before="0.2cm" padding-after="0.2cm">Information Message</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm">
										<fo:block padding-before="0.2cm" padding-after="0.2cm">
											<fo:inline>1. </fo:inline>
											<fo:inline>
												<xsl:apply-templates select="messageText"/>
											</fo:inline>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</xsl:if>
			
					<!--Showing Report Summary Information -->
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
						<fo:table table-layout="auto" space-before="0.1cm">
							<fo:table-column column-width="49.5%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="1%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="49.5%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-after="0.1cm" background-color="#eceae3">
										<!-- Table1 inside start-->
										<fo:table>
											<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
												
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Name:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/name" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															SSN:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/pptSSN" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<xsl:if test="summaryInfo/dob">
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Date of Birth:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/dob" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												</xsl:if>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
														<xsl:if test="showFootnote">
															Benefit Base*:
														</xsl:if>
														<xsl:if test="not(showFootnote)">
															Benefit Base:
														</xsl:if>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/amt" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Market Value:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/marketValue" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												
											</fo:table-body>
										</fo:table>
									</fo:table-cell>
										<!-- Table1 inside end-->
									<fo:table-cell><fo:block/></fo:table-cell>
										<!-- Table3 inside start-->
									<fo:table-cell padding-after="0.1cm" background-color="#eceae3">
										<fo:table>
											<fo:table-column column-width="74%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-column column-width="26%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
												
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Selection Date:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/selectionDate" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<xsl:if test="summaryInfo/deselectionDate">
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Deactivation Date:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/deselectionDate" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												</xsl:if>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Activation Date:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/activationDate" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<xsl:if test="summaryInfo/showLiaSummaryInfo = 'false'">
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Anniversary Date:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/nextStepUpDate" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												</xsl:if>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Minimum Holding Period Expiry Date:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/holdingPeriodExpDate" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<xsl:if test="summaryInfo/tradeRestrictionDate">
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Trading Restriction Expiry Date:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/tradeRestrictionDate" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												</xsl:if>
												<xsl:if test="summaryInfo/lastIncomeEnhancementRate">
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Rate for Last Income Enhancement:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/lastIncomeEnhancementRate" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												</xsl:if>
												
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:if test="summaryInfo/lastIncomeEnhancementRate">
																Last Income Enhancement Date:
															</xsl:if>
															<xsl:if test="not(summaryInfo/lastIncomeEnhancementRate)">
																Last Step-Up Date:
															</xsl:if>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/lastStepUpDate" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:if test="summaryInfo/lastIncomeEnhancementRate">
																Value Changed at Last Income Enhancement:
															</xsl:if>
															<xsl:if test="not(summaryInfo/lastIncomeEnhancementRate)">
																Value Changed at Last Step-Up Date:
															</xsl:if>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/lastStepUpAmt" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												
											</fo:table-body>
										</fo:table>
										<!-- Table3 inside end-->
									</fo:table-cell>
								</fo:table-row>
								
									
							</fo:table-body>
						</fo:table>
					</fo:block>
		
		<xsl:if test="summaryInfo/showLiaSummaryInfo = 'true'">					
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
										<xsl:apply-templates select="bodyHeader3" />                     
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
		
		
		<fo:table table-layout="auto" space-before="0.1cm">
							<fo:table-column column-width="49.5%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="1%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="49.5%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-after="0.1cm" background-color="#eceae3">
										<!-- Table1 inside start-->
										<fo:table>
											<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/liaSelectionDateLabel" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
													<xsl:value-of select="summaryInfo/liaSelectionDateValue" />              
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/spousalOptionLabel" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/spousalOptionValue" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/liaPercentageLabel" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
	 								<xsl:value-of select="summaryInfo/liaPercentageValue" />                
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												
											</fo:table-body>
										</fo:table>
									</fo:table-cell>
										<!-- Table1 inside end-->
									<fo:table-cell><fo:block/></fo:table-cell>
										<!-- Table3 inside start-->
									<fo:table-cell padding-after="0.1cm" background-color="#eceae3">
										<fo:table>
											<fo:table-column column-width="74%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-column column-width="26%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/annualLiaAmountLabel" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
													<xsl:value-of select="summaryInfo/annualLiaAmountValue" />              
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/paymentFrequencyLabel" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
					 								<xsl:value-of select="summaryInfo/paymentFrequencyValue" />                
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell text-align="right" padding-end="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="summaryInfo/liaAnniversaryDateLabel" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:block padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
													<xsl:value-of select="summaryInfo/liaAnniversaryDateValue" />                
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
										<!-- Table3 inside end-->
									</fo:table-cell>
								</fo:table-row>
								
									
							</fo:table-body>
						</fo:table>
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
											<xsl:apply-templates select="bodyHeader2" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					<fo:block space-before="0.1cm">
						<fo:table>
							<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="6%" xsl:use-attribute-sets="table-borderstyle"/>
							<!-- Showing the report table header-->
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Transaction Effective Date
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Associated Transaction Number
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Transaction Type
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Market Value Before Transaction($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Transaction Amount($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Benefit Base Change($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Resulting Benefit Base ($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											MHP Reset
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<!-- Showing the report table content-->
							<fo:table-body>
								<xsl:if test="txnDetails">
									<xsl:for-each select="txnDetails/txnDetail">
										<fo:table-row keep-together.within-page="always" display-align="center">
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:value-of select="txnDate" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="txnNumber" />
												</fo:block>
											</fo:table-cell>										
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:block>
														<xsl:value-of select="txnType" />
													</fo:block>
											</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="marketValue" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="txnAmt" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="bbChangeAmt" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="amt" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="holdingPeriodInd" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:if>
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
					
					<xsl:if test="msgNoTxn">
						<fo:table table-layout="auto" space-before="0.5cm">
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm">
									<fo:block font-weight="bold" padding-before="0.2cm" padding-after="0.2cm">Information Message</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm">
										<fo:block padding-before="0.2cm" padding-after="0.2cm">
											<fo:inline>1. </fo:inline>
											<fo:inline>
												<xsl:apply-templates select="msgNoTxn"/>
											</fo:inline>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</xsl:if>
					
					<xsl:call-template name="reportCommonFooter"/> 
					
					
		
				</fo:flow>
			
			</fo:page-sequence>
			
		</fo:root>
	</xsl:template>
	
</xsl:stylesheet>