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

<xsl:attribute-set name="arial_font.size_11">
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

<xsl:include href="infoMessages.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="HtmlToFoTransform.xsl" />

	<xsl:template match="brokerListing">
	
		<!-- 8.5x11 page.-->
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
					<fo:simple-page-master margin-bottom="1.0cm" margin-left="0.5cm" margin-right="0.5cm" margin-top="1.0cm" master-name="pageLandscapeLayout" page-height="21.59cm" page-width="27.94cm">
						<fo:region-body margin-bottom="0.5cm" margin-top="0cm"/>
						<fo:region-before extent="0cm"/>
						<fo:region-after extent="0.5cm"/>
					</fo:simple-page-master>
			</fo:layout-master-set>
				
			<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after" >
					<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
						Page <fo:page-number/> of <fo:page-number-citation-last ref-id="terminator"/> NOT VALID WITHOUT ALL PAGES
					</fo:block>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<fo:table>
							<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="60%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-cell>
									<fo:block>
										<fo:external-graphic content-height="8cm" content-width="8cm">
											<xsl:attribute name="src">
												<xsl:value-of select="jhLogoPath"/>
											</xsl:attribute>
										</fo:external-graphic>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding-before="0.5cm">
									<fo:block text-align="right" font-family="Georgia" font-size="24pt">
										<xsl:value-of select="pageName" />
									</fo:block>
									<fo:block text-align="right" font-family="Arial" font-size="16pt" font-weight="bold">
										<fo:inline>Report As Of: </fo:inline>
										<fo:inline>
											<xsl:value-of select="asOfDate" />
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
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
									</fo:table-cell>

									<!-- Showing Summary Information -->
									<fo:table-cell padding-start="0.3cm" background-color="#eceae3"> <!--light grey color -->
										<fo:block padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" font-weight="bold">
											<xsl:apply-templates select="summaryInfo/subHeader" />
										</fo:block>
										<fo:block padding-before="0.2cm"/> 
										<xsl:if test="summaryInfo/internalUserInfo">
											<fo:block xsl:use-attribute-sets="fonts_group1.size_11">
												<fo:inline>User Name: </fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/internalUserInfo/userName" />
												</fo:inline>
											</fo:block>
										</xsl:if>
										<xsl:if test="summaryInfo/bdFirmRepInfo">
											<xsl:if test="summaryInfo/bdFirmRepInfo/associatedFirmNames">
												<fo:table>
													<fo:table-column column-width="35%" xsl:use-attribute-sets="table-borderstyle"/>
													<fo:table-column column-width="65%" xsl:use-attribute-sets="table-borderstyle"/>
													<fo:table-body>
														<fo:table-row>
															<fo:table-cell>
																<fo:table>
																	<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
																	<fo:table-body>
																		<fo:table-row>
																			<fo:table-cell>
																				<fo:block xsl:use-attribute-sets="fonts_group1.size_11">
																					<fo:inline>Summary: </fo:inline>
																				</fo:block>
																			</fo:table-cell>
																		</fo:table-row>
																	</fo:table-body>
																</fo:table>																				
															</fo:table-cell>
															<fo:table-cell>
																<fo:table>
																	<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
																	<fo:table-body>
																		<fo:table-row>
																			<fo:table-cell>
																				<fo:block xsl:use-attribute-sets="fonts_group1.size_11">
																					<xsl:for-each select="summaryInfo/bdFirmRepInfo/associatedFirmNames/firmName">
																						<fo:block font-weight="bold">
																							<xsl:value-of select="text()"/>
																						</fo:block>
																					</xsl:for-each>
																				</fo:block>
																			</fo:table-cell>
																		</fo:table-row>
																	</fo:table-body>
																</fo:table>																				
															</fo:table-cell>
														</fo:table-row>
													</fo:table-body>
												</fo:table>
											</xsl:if>
										</xsl:if>
										<fo:block space-before="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11">
											<fo:inline>
												Total Contract Assets: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/totalContractAssets" />
											</fo:inline>
										</fo:block>
										<fo:block xsl:use-attribute-sets="fonts_group1.size_11">
											<fo:inline>
												Total Number of Contracts:
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/totalNumOfContracts" />
											</fo:inline>
										</fo:block>
										<fo:block xsl:use-attribute-sets="fonts_group1.size_11">
											<fo:inline>
												Total Number of Financial Representatives: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/totalNumOfFinancialReps" />
											</fo:inline>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					<!--Filters Used Info -->
					<xsl:if test="filtersUsed">
						<fo:block space-before="0.5cm">
							<fo:block background-color="#deded8" font-family="Verdana" font-size="14pt">
								Filters Used:
							</fo:block>
							<fo:table>
								<fo:table-column column-width="30%" />
								<fo:table-column column-width="70%" />
							
								<fo:table-body>
									<xsl:for-each select="filtersUsed/filter">
										<fo:table-row>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="filterTitle"/>
													<xsl:text>: </xsl:text>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="filterValue"/>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</fo:table-body>
							</fo:table>
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
								
								<fo:table-row background-color="#deded8" display-align="left">
									<fo:table-cell padding-start="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt">
											<!-- <xsl:apply-templates select="bodyHeader1" /> -->Report
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>

					<!-- Main Report Details -->
					<fo:block space-before="0.1cm">
						<fo:table>
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="5%" xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle" />

							<fo:table-header>
								<fo:table-row background-color="#deded8">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">Financial Representative</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">Firm Name</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">City</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">State</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">ZIP Code</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">Producer Code</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">Number Of Contracts</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block text-align="center" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">Total Assets($)</fo:block>
									</fo:table-cell>
								</fo:table-row> 
							</fo:table-header>
							<fo:table-body>
								<xsl:for-each select="reportDetails/reportDetail">
									<fo:table-row>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="financialRepName"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="firmName"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="city"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="stateName"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="zipCode"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="producerCode"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="numOfContracts"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
											<fo:block text-align="right" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="totalAssets"/>
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
					
					<!-- Showing Info Messages -->
					<xsl:call-template name="infoMessages"/>	
					
					<!-- Showing the PDF capped message and Footer-->
					<xsl:call-template name="reportCommonFooter"/>

				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
</xsl:stylesheet>