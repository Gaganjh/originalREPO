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

<xsl:attribute-set name="verdana_font.size_7">
	<xsl:attribute name="font-family">Verdana</xsl:attribute>
	<xsl:attribute name="font-size">7pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fonts_group2.size_12">
	<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="arial_font.size_9">
	<xsl:attribute name="font-family">Arial</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
</xsl:attribute-set>

<xsl:include href="reportCommonHeader.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />
<xsl:include href="HtmlToFoTransform.xsl" />

	<xsl:template match="contractInformation">
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
						<fo:table>
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
						<fo:table space-before="0.1cm">
							<fo:table-column column-width="49.75%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="0.5%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="49.75%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
									<fo:table-cell><fo:block/></fo:table-cell>
									<fo:table-cell border-collapse="collapse" number-rows-spanned="3" background-color="#eceae3">
										<!-- Table1 inside start-->
										<fo:table>
											<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell>
														<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row background-color="#deded8" keep-with-previous="always">
													<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
														<fo:block font-family="Verdana" font-size="14pt">
															<xsl:apply-templates select="reportDetail/contractOptionsSectionTitle" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-start="0.3cm">
														<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
															Contract Features
														</fo:block>
														
														<xsl:for-each select="reportDetail/feature">		
																<fo:list-block>
																	<fo:list-item padding-after="-0.1cm">
																		<fo:list-item-label start-indent="0.2cm">
																			<fo:block padding-after="-0.1cm" padding-before="0.05cm">
																				<xsl:text>&#8226;</xsl:text>
																			 </fo:block>
																		</fo:list-item-label>
																		<fo:list-item-body start-indent="0.6cm">
																			<fo:block padding-after="-0.15cm" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
																				<xsl:value-of select="." />
																			</fo:block>
																		</fo:list-item-body>
																	</fo:list-item>
																</fo:list-block>
														</xsl:for-each>
													</fo:table-cell>
												</fo:table-row>
												<xsl:if test="reportDetail/sendServiceInd">
													<fo:table-row keep-with-previous="always">
														<fo:table-cell padding-start="0.3cm">
															<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
																SEND Service
															</fo:block>
															
																	<fo:list-block>
																		<fo:list-item padding-after="-0.1cm">
																			<fo:list-item-label start-indent="0.2cm">
																				<fo:block padding-after="-0.1cm" padding-before="0.05cm">
																					<xsl:text>&#8226;</xsl:text>
																				 </fo:block>
																			</fo:list-item-label>
																			<fo:list-item-body start-indent="0.6cm">
																				<fo:block padding-after="-0.15cm" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
																					Newly Eligible, Annual
																				</fo:block>
																			</fo:list-item-body>
																		</fo:list-item>
																	</fo:list-block>
																	
																	<fo:list-block>
																		<fo:list-item padding-after="-0.1cm">
																			<fo:list-item-label start-indent="0.2cm">
																				<fo:block padding-after="-0.1cm" padding-before="0.05cm">
																					<xsl:text>&#8226;</xsl:text>
																				 </fo:block>
																			</fo:list-item-label>
																			<fo:list-item-body start-indent="0.6cm">
																				<fo:block padding-after="-0.15cm" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
																					Annual Fee: <xsl:value-of select="reportDetail/sendServiceFee" /> as of <xsl:value-of select="reportDetail/sendServiceEffectiveDate" />
																																								
																			    </fo:block>
																			</fo:list-item-body>
																		</fo:list-item>
																	</fo:list-block>
																	
														</fo:table-cell>
													</fo:table-row>
												</xsl:if>
												<xsl:if test="reportDetail/giflFeaturesSectionTitle">
													<fo:table-row keep-with-previous="always">
														<fo:table-cell padding-start="0.3cm">
															<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/giflFeaturesSectionTitle" />
															</fo:block>

															<xsl:for-each select="reportDetail/giflFeature">		
																	<fo:list-block>
																		<fo:list-item padding-after="-0.1cm">
																			<fo:list-item-label start-indent="0.2cm">
																				<fo:block padding-after="-0.1cm" padding-before="0.05cm">
																					<xsl:text>&#8226;</xsl:text>
																				 </fo:block>
																			</fo:list-item-label>
																			<fo:list-item-body start-indent="0.6cm">
																				<fo:block padding-after="-0.15cm" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
																					<xsl:value-of select="." />
																				</fo:block>
																			</fo:list-item-body>
																		</fo:list-item>
																	</fo:list-block>
															</xsl:for-each>	
														</fo:table-cell>														
													</fo:table-row>	
												</xsl:if>
												<xsl:if test="reportDetail/managedAccount">
													<fo:table-row keep-with-previous="always">
														<fo:table-cell padding-after="0.2cm" padding-start="0.3cm">
															<fo:block font-weight="bold" xsl:use-attribute-sets="verdana_font.size_8">
																Managed Accounts
															</fo:block>
															<xsl:for-each select="reportDetail/managedAccount">		
																	<fo:list-block>
																		<fo:list-item padding-after="-0.1cm">
																			<fo:list-item-label start-indent="0.2cm">
																				<fo:block padding-after="-0.1cm" padding-before="0.05cm">
																					<xsl:text>&#8226;</xsl:text>
																				 </fo:block>
																			</fo:list-item-label>
																			<fo:list-item-body start-indent="0.6cm">
																				<fo:block padding-after="-0.15cm" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
																					<xsl:value-of select="." />
																				</fo:block>
																			</fo:list-item-body>
																		</fo:list-item>
																	</fo:list-block>
															</xsl:for-each>	
														</fo:table-cell>
													</fo:table-row>
												</xsl:if>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-start="0.3cm">
														<fo:block font-weight="bold" xsl:use-attribute-sets="verdana_font.size_8">
															Investments
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-start="0.3cm">
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">		
															<fo:inline>
																Number of Funds Available:
															</fo:inline>
															<fo:inline>
																<xsl:value-of select="reportDetail/availableFunds" />
															</fo:inline>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-start="0.3cm">
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">		
															<fo:inline>
																Number of Funds Selected:
															</fo:inline>
															<fo:inline>
																<xsl:value-of select="reportDetail/selectedFunds" />
															</fo:inline>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-start="0.3cm">
														<fo:block padding-before="0.2cm" xsl:use-attribute-sets="verdana_font.size_8">
															Default Investment Option(s)
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-start="0.3cm">
														<xsl:if test="not(reportDetail/defaultInvestment)">
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																No default investment option currently selected. 
																Please contact your Client Account Representative 
																to change the default investment option for your contract.
															</fo:block>
														</xsl:if>
														<xsl:if test="reportDetail/defaultInvestment">
															<xsl:for-each select="reportDetail/defaultInvestment">	
																<fo:list-block>
																	<fo:list-item padding-after="-0.1cm">
																		<fo:list-item-label start-indent="0.2cm">
																			<fo:block font-weight="bold" padding-after="-0.1cm" padding-before="0.05cm">
																			   <xsl:text>&#8226;</xsl:text>
																			 </fo:block>
																		</fo:list-item-label>
																		<fo:list-item-body start-indent="0.6cm">
																			<fo:block padding-after="-0.15cm" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
																				<xsl:if test="fundName">
																					<fo:inline>
																						<xsl:value-of select="fundName" />
																					</fo:inline>
																					<xsl:text>&#32;</xsl:text>
																				</xsl:if>
																				<fo:inline>
																					<xsl:value-of select="fundPercentage" />%
																				</fo:inline>
																			</fo:block>
																		</fo:list-item-body>
																	</fo:list-item>
																</fo:list-block>
															</xsl:for-each>
														</xsl:if>
														</fo:table-cell>
												</fo:table-row>											
												
												<xsl:if test="reportDetail/showFudiciaryWarranty">
													<fo:table-row keep-with-previous="always">
														<fo:table-cell padding-start="0.3cm">
															<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
																Fiduciary Standards Warranty (FSW)
															</fo:block>															
															<fo:block padding-after="-0.15cm" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_8"><xsl:value-of select="reportDetail/warrantyMet" /></fo:block>
														</fo:table-cell>
													</fo:table-row>
												</xsl:if>
												<xsl:if test="reportDetail/accessChannel">
													<fo:table-row keep-with-previous="always">
														<fo:table-cell padding-start="0.3cm">
															<fo:block font-weight="bold" padding-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_8">
																Access Channels
															</fo:block>
															<xsl:for-each select="reportDetail/accessChannel">
															<fo:list-block>
																<fo:list-item padding-after="-0.1cm">
																	<fo:list-item-label start-indent="0.2cm">
																		<fo:block padding-after="-0.1cm" padding-before="0.05cm">
																			<xsl:text>&#8226;</xsl:text>
																		 </fo:block>
																	</fo:list-item-label>
																	<fo:list-item-body start-indent="0.6cm">
																		<fo:block padding-after="-0.15cm" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="." />
																		</fo:block>
																	</fo:list-item-body>
																</fo:list-item>
															</fo:list-block>
															</xsl:for-each>
														</fo:table-cell>
													</fo:table-row>
												</xsl:if>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-start="0.3cm">
														<fo:block padding-before="0.3cm" padding-after="0.2cm" xsl:use-attribute-sets="verdana_font.size_8">
															<fo:inline font-weight="bold" >
																Direct Debit Selected 
															</fo:inline>
															<xsl:if test="not(reportDetail/directDebitSelected)">
																<fo:inline>
																	No
																</fo:inline>
															</xsl:if>
															<xsl:if test="reportDetail/directDebitSelected">
																<fo:inline>
																	Yes
																</fo:inline>
															</xsl:if>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
										<!-- Table2 inside end-->
										<!-- Table3 inside start-->
										<fo:table space-before="0.1cm">
											<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
											<fo:table-row>
													<fo:table-cell>
														<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#ffffff" /><!--white color -->
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell>
														<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row background-color="#deded8" keep-with-previous="always">
													<fo:table-cell padding-before="0.1cm" padding-start="0.2cm">
														<fo:block font-family="Verdana" font-size="14pt">
															<xsl:apply-templates select="reportDetail/moneyTypesAndSourcesSectionTitle" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row background-color="#eceae3" keep-with-previous="always">
													<fo:table-cell padding-start="0.2cm">
														<!-- small table1 inside start-->
														<fo:table>
														<fo:table-column column-width="30%" xsl:use-attribute-sets="table-borderstyle"/>
														<fo:table-column column-width="70%" xsl:use-attribute-sets="table-borderstyle"/>
														<fo:table-header>
															<fo:table-row>
																<fo:table-cell padding-before="0.2cm">
																	<fo:block font-weight="bold" xsl:use-attribute-sets="verdana_font.size_8">
																		Code
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm">
																	<fo:block font-weight="bold" xsl:use-attribute-sets="verdana_font.size_8">
																		Money Type(s)
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
														</fo:table-header>
														<fo:table-body>
															<xsl:for-each select="reportDetail/moneyType">
																<fo:table-row>
																	<fo:table-cell padding-before="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="shortName" />
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="longName" />
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:for-each>
														</fo:table-body>
														</fo:table>	
														<!-- small table1 inside - end-->
														<fo:block>
															<fo:leader leader-pattern="rule" leader-length="95%"/>
														</fo:block>
														<!-- small table2 inside start-->
														<fo:table>
														<fo:table-column column-width="30%" xsl:use-attribute-sets="table-borderstyle"/>
														<fo:table-column column-width="70%" xsl:use-attribute-sets="table-borderstyle"/>
														<fo:table-header>
															<fo:table-row>
																<fo:table-cell padding-before="0.2cm">
																	<fo:block font-weight="bold" xsl:use-attribute-sets="verdana_font.size_8">
																		Code
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm">
																	<fo:block font-weight="bold" xsl:use-attribute-sets="verdana_font.size_8">
																		Money Source(s)
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
														</fo:table-header>
														<fo:table-body>
															<xsl:for-each select="reportDetail/moneySource">
																<fo:table-row>
																	<fo:table-cell padding-before="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="shortName" />
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="longName" />
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:for-each>
														</fo:table-body>
														</fo:table>	
														<!-- small table2 inside - end-->
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
										<!-- Table3 inside end-->
										<!-- Table4 inside start-->
										
										<!-- Table4 inside end-->
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row keep-with-previous="always">
									<fo:table-cell padding-start="0.2cm" background-color="#deded8">
										<fo:table>
											<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell padding-start="0.2cm">
														<fo:block padding-before="0.1cm" font-family="Verdana" font-size="14pt">
															<xsl:apply-templates select="reportDetail/contactSectionTitle"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:table-cell>
									<fo:table-cell><fo:block></fo:block></fo:table-cell>
								</fo:table-row>
								<fo:table-row keep-with-previous="always">
									<fo:table-cell background-color="#eceae3">
										<fo:table>
											<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
														<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
															Mailing Address:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/addressLine1"/>
														</fo:block>
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/addressLine2"/>
														</fo:block>
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<fo:inline>
																<xsl:if test="reportDetail/city">
																<xsl:value-of select="reportDetail/city"/>
																<xsl:if test="reportDetail/completeStateCode">, </xsl:if>
																</xsl:if>
															</fo:inline>
															<fo:inline>		
																<xsl:if test="reportDetail/stateCode">
																	<xsl:value-of select="reportDetail/stateCode"/>
																	<xsl:if test="reportDetail/zipCode">, </xsl:if>
																</xsl:if>
																<xsl:if test="reportDetail/zipCode"><xsl:value-of select="reportDetail/zipCode"/></xsl:if>
															</fo:inline>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<xsl:if test="reportDetail/generalPhoneNo">
													<fo:table-row>
														<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
															<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																Discontinued Contract:
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																For assistance
															</fo:block>
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/generalPhoneNo"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</xsl:if>
												<xsl:if test="not(reportDetail/generalPhoneNo)">
													<fo:table-row>
														<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
															<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																Client Account Rep.:
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/carName"/>
															</fo:block>
															<xsl:if test="reportDetail/pptTollFreeNo">
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/pptTollFreeNo"/>
															</fo:block>
															</xsl:if>
															<xsl:if test="reportDetail/carPhoneNo">
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/carPhoneNo"/>
															</fo:block>
															</xsl:if>
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/carEmail"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</xsl:if>
												<fo:table-row>
													<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
														<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
															Relationship Manager Contact:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/rmName"/>
														</fo:block>
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/rmPhone"/>
														</fo:block>
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/rmEmail"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
														<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
															TPA Contact:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/tpaContactName"/>
														</fo:block>
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/tpaFirmName"/>
														</fo:block>
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/tpaPhone"/>
														</fo:block>
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/tpaEmail"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
														<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
															Plan Sponsor Contact:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/psContactName"/>
														</fo:block>
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/psPhone"/>
														</fo:block>
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/psEmail"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<xsl:if test="reportDetail/noOfParticipants">
													<fo:table-row>
														<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
															<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																Participant Toll Free Services:
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
															<fo:block/>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell font-weight="lighter" padding-before="0.2cm" padding-end="0.2cm">
															<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/pptTollFreeServiceLabel"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/pptTollFreeServiceNo"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
															<fo:block/>
														</fo:table-cell>
														<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/pptTollFreeServiceNoinSpanish"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell font-weight="lighter" padding-before="0.2cm" padding-end="0.2cm">
															<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/pptRolloverEducationSpecialistLabel"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/pptRolloverEducationSpecialistNo"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell font-weight="lighter" padding-before="0.2cm" padding-end="0.2cm">
															<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/pptConsolidationSpecialistPhoneLabel"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/pptConsolidationSpecialistPhoneNo"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
															<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																Enrollment Form Fax:
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<xsl:value-of select="reportDetail/enrollFormFaxNo"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</xsl:if>
												<fo:table-row>
													<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm" padding-after="0.2cm">
														<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
															Other Form Fax:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.2cm" padding-start="0.2cm" padding-after="0.2cm">
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/otherFormFaxNo"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell background-color="#ffffff" height="0.1cm" border-collapse="collapse" number-columns-spanned="2">
														<fo:block />
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell border-collapse="collapse" number-columns-spanned="2">
														<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row background-color="#deded8" keep-with-previous="always">
													<fo:table-cell padding-start="0.2cm" border-collapse="collapse" number-columns-spanned="2">
														<fo:block padding-before="0.1cm" font-family="Verdana" font-size="14pt">
															<xsl:apply-templates select="reportDetail/assetsTitle"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												
												
												<fo:table-row keep-with-previous="always">
													<fo:table-cell background-color="#eceae3" number-columns-spanned="2">
														<fo:table>
															<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
															<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
															<fo:table-body>
															<fo:table-row>
																<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																	<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Total Contract Assets:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/totalAssets"/>
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<fo:table-row>
																<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																	<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Assets in Cash Account:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/cashAccountAmt"/>
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<xsl:if test="reportDetail/uninvestedAmt">
																<fo:table-row>
																	<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																		<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																			Pending Transaction:
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="reportDetail/uninvestedAmt"/>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:if>
															<fo:table-row>
																<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																	<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Assets Allocated:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/allocatedAmt"/>
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<xsl:if test="reportDetail/loanAssets">
																<fo:table-row>
																	<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																		<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																			Loan Assets:
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="reportDetail/loanAssets"/>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:if>
															<xsl:if test="reportDetail/pbaAccountAmt">
																<fo:table-row>
																	<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																		<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																			PBA Assets:
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="reportDetail/pbaAccountAmt"/>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:if>
															<xsl:if test="reportDetail/noOfParticipants">
																<fo:table-row>
																	<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																		<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																			Participants:
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="reportDetail/noOfParticipants"/>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:if>
															<xsl:if test="reportDetail/asOfDate">
																<fo:table-row>
																	<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																		<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																			As of:
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="reportDetail/asOfDate"/>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:if>
															<xsl:if test="reportDetail/assetChargeAsOfDate">
																<fo:table-row>
																	<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm" padding-after="0.2cm">
																		<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																			Blended Asset Charge (%):
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm" padding-start="0.2cm" padding-after="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																				<fo:inline>
																					<xsl:value-of select="reportDetail/blendedAssetCharge"/>
																				</fo:inline>
																				<fo:inline>
																					as of
																				</fo:inline>
																				<fo:inline>
																					<xsl:value-of select="reportDetail/assetChargeAsOfDate"/>
																				</fo:inline>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:if>
															</fo:table-body>
														</fo:table>
													</fo:table-cell>
												</fo:table-row>
												
												<fo:table-row>
													<fo:table-cell background-color="#ffffff" height="0.1cm" border-collapse="collapse" number-columns-spanned="2">
														<fo:block />
													</fo:table-cell>
												</fo:table-row>
												
												<fo:table-row>
													<fo:table-cell border-collapse="collapse" number-columns-spanned="2">
														<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row background-color="#deded8" keep-with-previous="always">
													<fo:table-cell padding-start="0.2cm" border-collapse="collapse" number-columns-spanned="2">
														<fo:block padding-before="0.1cm" font-family="Verdana" font-size="14pt">
															<xsl:apply-templates select="reportDetail/payrollAllocationTitle"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell background-color="#eceae3" number-columns-spanned="2">
														<fo:table>
															<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
															<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
															<fo:table-body>
															<fo:table-row>
																<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																	<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:if test="not(reportDetail/noOfParticipants)">
																		Last Allocation:
																		</xsl:if>
																		<xsl:if test="reportDetail/noOfParticipants">
																		Last Payroll Allocation:
																		</xsl:if>
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/lastAllocationAmt"/>
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<fo:table-row>
																<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																	<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Invested Date:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/lastContribDate"/>
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<fo:table-row>
																<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm" padding-after="0.2cm">
																	<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Applicable as of:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm" padding-after="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/lastPayrollDate"/>
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															</fo:table-body>
														</fo:table>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell background-color="#ffffff" height="0.1cm" border-collapse="collapse" number-columns-spanned="2">
														<fo:block />
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell border-collapse="collapse" number-columns-spanned="2">
														<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row background-color="#deded8" keep-with-previous="always">
													<fo:table-cell padding-start="0.2cm" border-collapse="collapse" number-columns-spanned="2">
														<fo:block padding-before="0.1cm" font-family="Verdana" font-size="14pt">
															<xsl:apply-templates select="reportDetail/keyDatesSectionTitle"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell background-color="#eceae3" number-columns-spanned="2">
														<fo:table>
															<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
															<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
															<fo:table-body>
															<fo:table-row>
																<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																	<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Contract Effective:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/contractEffectiveDate"/>
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<fo:table-row>
																<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm">
																	<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Plan Year End:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/contractYearEndDate"/>
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<xsl:if test="reportDetail/transferDate">
															<fo:table-row>
																<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-end="0.2cm" padding-after="0.2cm">
																	<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Guaranteed Acct. Transfer**:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm" padding-after="0.2cm">
																	<xsl:for-each select="reportDetail/transferDate">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="."/>
																		</fo:block>
																	</xsl:for-each>
																</fo:table-cell>
															</fo:table-row>
															</xsl:if>
															</fo:table-body>
														</fo:table>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row background-color="#ffffff" >
													<fo:table-cell height="0.1cm" border-collapse="collapse" number-columns-spanned="2">
														<fo:block />
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell border-collapse="collapse" number-columns-spanned="2">
														<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row background-color="#deded8" keep-with-previous="always">
													<fo:table-cell border-collapse="collapse" number-columns-spanned="2" padding-start="0.2cm">
														<fo:block padding-before="0.1cm" font-family="Verdana" font-size="14pt">
															<xsl:apply-templates select="reportDetail/accessCodeSectionTitle"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-before="0.2cm" number-columns-spanned="2" padding-start="0.1cm">
														<fo:block xsl:use-attribute-sets="verdana_font.size_8">
															<xsl:value-of select="reportDetail/accessCodeMsg"/>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell number-columns-spanned="2">
														<fo:table>
															
															<fo:table-body>
															<fo:table-row>
																<fo:table-cell font-weight="bold" padding-before="0.2cm" padding-start="0.3cm">
																	<fo:block text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Enrollment access number:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/accessCode"/>
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															</fo:table-body>
														</fo:table>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell number-columns-spanned="2">
														<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#ffffff" /><!--white color -->
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell number-columns-spanned="2">
														<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row background-color="#deded8" keep-with-previous="always">
													<fo:table-cell padding-before="0.1cm" padding-start="0.2cm" number-columns-spanned="2">
														<fo:block font-family="Verdana" font-size="14pt">
															<xsl:apply-templates select="reportDetail/stmtDetailsSectionTitle" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row background-color="#eceae3" keep-with-previous="always">
													<fo:table-cell padding-start="0.2cm" number-columns-spanned="2">
														<!-- small table inside start-->
														<fo:table space-before="0.1cm">
														<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
														<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
														<fo:table-body>
															<fo:table-row>
																<fo:table-cell padding-before="0.2cm" padding-end="0.2cm">
																	<fo:block font-weight="bold" text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Basis:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/basis" />
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<fo:table-row>
																<fo:table-cell padding-before="0.2cm" padding-end="0.2cm">
																	<fo:block font-weight="bold" text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																		Last Printed:
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																	<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																		<xsl:value-of select="reportDetail/lastPrintDate" />
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<xsl:if test="reportDetail/deliveryMethod">
																<fo:table-row>
																	<fo:table-cell padding-before="0.2cm" padding-end="0.2cm">
																		<fo:block font-weight="bold" text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																			Delivery Method:
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="reportDetail/deliveryMethod" />
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
																<fo:table-row>
																	<fo:table-cell padding-before="0.2cm" padding-end="0.2cm">
																		<fo:block font-weight="bold" text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																			Statement Type:
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8"><xsl:if test="reportDetail/stmtType">
																			<xsl:value-of select="reportDetail/stmtType" /></xsl:if><xsl:if test="not(reportDetail/stmtType)">No Statement</xsl:if>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
																<fo:table-row>
																	<fo:table-cell padding-before="0.2cm" padding-end="0.2cm">
																		<fo:block font-weight="bold" text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																			Permitted Disparity:
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm" padding-start="0.2cm">
																		<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:value-of select="reportDetail/permitDisparity" />
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
																<fo:table-row>
																	<fo:table-cell padding-before="0.2cm" padding-after="0.2cm" padding-end="0.2cm">
																		<fo:block font-weight="bold" text-align="right" xsl:use-attribute-sets="verdana_font.size_8">
																			Vesting Shows on Statements:
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell padding-before="0.2cm" padding-after="0.2cm" padding-start="0.2cm">
																		<fo:block padding-before="0.34cm" xsl:use-attribute-sets="verdana_font.size_8">
																			<xsl:if test="reportDetail/vestingShownOnStmt">
																				Yes
																			</xsl:if>
																			<xsl:if test="not(reportDetail/vestingShownOnStmt)">
																				No
																			</xsl:if>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:if>
														</fo:table-body>
														</fo:table>	
														<!-- small table inside - end-->
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:table-cell>
									<fo:table-cell><fo:block/></fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
	
					<!-- Showing the page footer-->
					<xsl:call-template name="reportCommonFooter"/>
		
				</fo:flow>
			
			</fo:page-sequence>
			
		</fo:root>
	</xsl:template>
	
</xsl:stylesheet>