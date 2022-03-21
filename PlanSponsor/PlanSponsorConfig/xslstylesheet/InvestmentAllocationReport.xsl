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

<xsl:include href="reportCommonHeader.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />

	<xsl:template match="investmentAllocation">

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
							<fo:table-column column-width="60%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
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
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="rothMsg" />
										</fo:block>
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="sigPlusMsg" />
										</fo:block>
									</fo:table-cell>
									
									<!-- Showing Summary Information -->
									<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
										<fo:block font-weight="bold" padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<xsl:apply-templates select="summaryInfo/subHeader" />
										</fo:block>
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Number of Investment Options Selected: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/noOfInvestmentOption" />
											</fo:inline>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
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
							</fo:table-body>
						</fo:table>
						<fo:table>		
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row background-color="#deded8">
									<fo:table-cell padding-start="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt">
											<xsl:apply-templates select="bodyHeader1" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.2cm">
										<fo:block padding-before="0.1cm" font-family="Verdana" font-size="11pt">
											<fo:inline>
												Organized by:
											</fo:inline>
											<xsl:if test="(organizingOption = '2')">
												<fo:inline>
													Asset Class
												</fo:inline>
											</xsl:if>
											<xsl:if test="(organizingOption = '3')">
												<fo:inline>
													Risk/Return Category
												</fo:inline>
											</xsl:if>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.2cm">
										<fo:block padding-before="0.1cm" font-family="Verdana" font-size="11pt">
											<fo:inline>
												View by:
											</fo:inline>
											<xsl:if test="(viewOption = '0')">
												<fo:inline>
													Asset View
												</fo:inline>
											</xsl:if>
											<xsl:if test="(viewOption = '1')">
												<fo:inline>
													Activity View
												</fo:inline>
											</xsl:if>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block space-before="0.05cm">
						<fo:table>
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							<!-- Showing the report summary header-->
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											<xsl:value-of select="fundSeriesName" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Options With Assets
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Participants Currently Invested
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employee Assets ($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employer Assets ($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Total Assets ($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											 % Of Total
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<!-- Showing the report summary content-->
							<fo:table-body>
								<xsl:for-each select="reportSummaryDetails/reportSummaryDetail">
									<fo:table-row display-align="center">	
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="investmentOptionText" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="noOfOptions" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="pptInvested" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="totalEEAssets" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="totalERAssets" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="totalAssets" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="totalPercent" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>		
								</xsl:for-each>
								<fo:table-row keep-with-previous="always">
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					<!--Showing the main Report Information -->
					<fo:block space-before="0.4cm">
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
					<fo:block space-before="0.05cm">
						<fo:table>
							<fo:table-column column-width="18%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="16%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
							<!-- Showing the main report header-->
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											<xsl:value-of select="fundSeriesName" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Ticker Symbol
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Class
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
										<xsl:if test="(descendant::*/asOfDateReportCurrent)">
											Participants Invested (Current/Ongoing)
										</xsl:if>
										<xsl:if test="not(descendant::*/asOfDateReportCurrent)">
											Participants Invested (Current)
										</xsl:if>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employee Assets ($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employer Assets ($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Total Assets ($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											 % Of Total
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>						
								<fo:table-body>	
									<xsl:for-each select="reportDetails/reportDetail/allocationDetails">
										<fo:table-row keep-with-next="always">
											<fo:table-cell padding-before="0.2cm">
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" />
											</fo:table-cell>
											<fo:table-cell padding-before="0.2cm">
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" />
											</fo:table-cell>
											<fo:table-cell padding-before="0.2cm">
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" />
											</fo:table-cell>
											<fo:table-cell padding-before="0.2cm">
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" />
											</fo:table-cell>
											<fo:table-cell padding-before="0.2cm">
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" />
											</fo:table-cell>
											<fo:table-cell padding-before="0.2cm">
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" />
											</fo:table-cell>
											<fo:table-cell padding-before="0.2cm">
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" />
											</fo:table-cell>
											<fo:table-cell padding-before="0.2cm">
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" />
											</fo:table-cell>
										</fo:table-row>
										
										<fo:table-row display-align="center" background-color="#deded8"  	 keep-with-next="always">
											<fo:table-cell padding-start="0.2cm" border-collapse="collapse" number-columns-spanned="8">
												<fo:block font-family="Verdana" font-size="14pt">
													<xsl:value-of select="fundCategory" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<xsl:for-each select="allocationDetail">
											<fo:table-row display-align="center">	
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
													<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<fo:inline>
															<xsl:value-of select="fundName" />
														</fo:inline>
														<xsl:if test="pbFundCategory">
															<fo:inline baseline-shift="super">
																<xsl:text>&#8224;</xsl:text>
															</fo:inline>
														</xsl:if>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
													<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="tickerSymbol" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
													<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="fundClass" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
													<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<fo:inline>
														<xsl:if test="asOfDateReportCurrent">
															<xsl:if test="pptInvestedCurrent = '0'">
																<xsl:value-of select="pptInvestedCurrent" />
															</xsl:if>
															<xsl:if test="not(pptInvestedCurrent = '0')">
																<xsl:if test="pptInvestedCurrent &gt; '0'">
																<xsl:value-of select="pptInvestedCurrent" />/<xsl:value-of select="pptInvestedFuture" /></xsl:if>
															</xsl:if>
														</xsl:if>
														<xsl:if test="not(asOfDateReportCurrent)">
															<xsl:if test="pptInvestedCurrent">
																<xsl:value-of select="pptInvestedCurrent" />
															</xsl:if>
														</xsl:if>
														</fo:inline>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="totalEEAssets" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="totalERAssets" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="totalAssets" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="totalPercent" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>		
										</xsl:for-each>
										</xsl:for-each>
										<fo:table-row keep-with-previous="always">
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
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