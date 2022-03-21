<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<xsl:template name="UnMonitoredFunds">
	
		<fo:page-sequence master-reference="AnnualReviewResultFirstAndRestPageLayout">
		
		<!-- Header -->
			<fo:static-content flow-name="xsl-region-before">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="115px" />
					<fo:table-column column-width="459px" />
					<fo:table-column column-width="18px" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell height = "18px" background-color="#C4C7AB" >
								<fo:block></fo:block >
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
						</fo:table-cell>
						<fo:table-cell>
								<fo:block></fo:block>
						</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>
			
			<!-- Footer -->
			<fo:static-content flow-name="xsl-region-after">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="115px" />
					<fo:table-column column-width="459px" />
					<fo:table-column column-width="18px" />
					<fo:table-body>
						<fo:table-row height="4px">
							<fo:table-cell>
								<fo:block>									
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
								</fo:block>
							</fo:table-cell>	
						</fo:table-row>
						<fo:table-row height="36px">
							<fo:table-cell  background-color="#CB5A27" padding-before="14px">
								<fo:block xsl:use-attribute-sets="page_count" start-indent="13px" font-weight="bold">
									PAGE
									<fo:page-number />
									OF
									<fo:page-number-citation-last
										ref-id="terminator"/>
								</fo:block>
								<fo:block xsl:use-attribute-sets="page_count_disclaimer" start-indent="13px">Not valid without all pages.</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="footer_disclaimer"  padding-before="24px" text-align="right">
									FOR PLAN SPONSOR USE ONLY. NOT FOR USE WITH PLAN PARTICIPANTS.
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>	
			
			<!-- Body -->
			<fo:flow flow-name="xsl-region-body">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="89px" />
					<fo:table-column column-width="13px" />
					<fo:table-column column-width="435px" />
					<fo:table-column column-width="8px" />
					<fo:table-column column-width="36px" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block padding-before="20px" xsl:use-attribute-sets="header_block_cell3" text-align="right">
									Your other funds
								</fo:block>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="2">
								<fo:block></fo:block>>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="2">
								<fo:block padding-before="42px" xsl:use-attribute-sets="word_group_font">
									The IPS Manager service analyzes the Funds in your lineup that you added via the use of the service. Therefore, the following additional Funds that you chose to include in your plan’s lineup outside of the service are not analyzed by the IPS Manager service and not evaluated in this specific report.
								</fo:block>
								<fo:block padding-before="20px" xsl:use-attribute-sets="word_group_font">
									John Hancock provides a platform of investments that meets the needs of your plan participants.  For information on all of these Funds, see the Fund sheets available in the Investment Options section of the Plan Sponsor website. 
								</fo:block>
								<fo:block padding-before="20px" >
									<xsl:if test="annual_review_report/UnmonitioredFundInfo/unmonitored_fund_info">
									<fo:table table-layout="fixed" width="100%">
										<fo:table-column column-width="110px"/>
										<fo:table-column column-width="319px"/>
										<fo:table-header>
											<fo:table-row height="17px">
												<fo:table-cell background-color="#CB5A27" xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" font-size="9pt" text-align="left" display-align="center" 
													start-indent="3px" padding-before="3px">
														FUND
													</fo:block>
												</fo:table-cell>
												<fo:table-cell background-color="#CB5A27" xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" font-size="9pt" text-align="left" display-align="center" 
													start-indent="3px" padding-before="3px">
														DETAILS
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-header>		
										
										<fo:table-body>
											
												<xsl:for-each select="annual_review_report/UnmonitioredFundInfo/unmonitored_fund_info">
												
												<xsl:choose>
													<xsl:when test="position() mod 2 = 0">
													<fo:table-row height="33px">
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" background-color="#F7F9F6">
															<fo:block xsl:use-attribute-sets="unmonitored_fund_name" text-align="left"
															start-indent="3px" padding-before="3px">	
																<xsl:choose>
																	<xsl:when test="fundSheetURL">
																		<fo:basic-link>
																			<xsl:attribute name="external-destination">url('<xsl:value-of select="fundSheetURL"/>')</xsl:attribute>
																		 	<xsl:value-of select="fundName" /> 
																	 	</fo:basic-link>
																	</xsl:when>
																	<xsl:otherwise>
																		<xsl:value-of select="fundName"/>
																	</xsl:otherwise>
																</xsl:choose>
															</fo:block>
														</fo:table-cell>	
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" background-color="#F7F9F6">
															<fo:block xsl:use-attribute-sets="unmonitored_fund_description" text-align="left" 
															start-indent="3px" padding-before="3px">
																<xsl:choose>
																<!-- menonra: Code added/updated for SVF project: start -->
																	<xsl:when test="assetClass = 'LCF' or assetClass = 'LSF'" >
																		The selection of this investment option is based on your Asset Allocation preference, which is a non-quantifiable measurement criteria.
																	</xsl:when>
																	<xsl:when test="assetClass = 'LSG'">
																		The funds offered under the Guaranteed Income Feature were manually added to your lineup by the Plan Trustee or designated 3(38) Investment Manager.
																	</xsl:when>
																	<xsl:when test="fundFamilyCateogryCode = 'SVF'">
																		The selection of this investment option is based on capital preservation preference, which is a non-quantifiable measurement criteria.
																	</xsl:when>
																	<xsl:when test="fundFamilyCateogryCode = 'MMF'">
																		The selection of this investment option is based on capital preservation preference, which is a non-quantifiable measurement criteria.
																	</xsl:when>
																<!-- Code added/updated for SVF project: end -->
																	<xsl:otherwise>
																		This fund was manually added to your lineup by the Plan Trustee or designated 3(38) Investment Manager. 
																	</xsl:otherwise>
																</xsl:choose>
															</fo:block>
														</fo:table-cell>		
													</fo:table-row>								
													</xsl:when>
													<xsl:otherwise>
													<fo:table-row height="33px">
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" background-color="#E9E9DE">
															<fo:block xsl:use-attribute-sets="unmonitored_fund_name" text-align="left"
															start-indent="3px" padding-before="3px">
															<xsl:choose>
																<xsl:when test="fundSheetURL">
																	<fo:basic-link>
																			<xsl:attribute name="external-destination">url('<xsl:value-of select="fundSheetURL"/>')</xsl:attribute>
																		 	<xsl:value-of select="fundName" /> 
																	 </fo:basic-link>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="fundName"/>
																</xsl:otherwise>
															</xsl:choose>	
															</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" background-color="#E9E9DE">
															<fo:block xsl:use-attribute-sets="unmonitored_fund_description" text-align="left"
															start-indent="3px" padding-before="3px">
																<xsl:choose>
																<!-- menonra: Code added/updated for SVF project: start -->
																	<xsl:when test="assetClass = 'LCF' or assetClass = 'LSF'" >
																		The selection of this investment option is based on your Asset Allocation preference, which is a non-quantifiable measurement criteria.
																	</xsl:when>
																	<xsl:when test="assetClass = 'LSG'">
																		The funds offered under the Guaranteed Income Feature were manually added to your lineup by the Plan Trustee or designated 3(38) Investment Manager.
																	</xsl:when>
																	<xsl:when test="fundFamilyCateogryCode = 'SVF'">
																		The selection of this investment option is based on capital preservation preference, which is a non-quantifiable measurement criteria.
																	</xsl:when>
																	<xsl:when test="fundFamilyCateogryCode = 'MMF'">
																		The selection of this investment option is based on capital preservation preference, which is a non-quantifiable measurement criteria.
																	</xsl:when>
																<!-- Code added/updated for SVF project: end -->
																	<xsl:otherwise>
																		This fund was manually added to your lineup by the Plan Trustee or designated 3(38) Investment Manager.
																	</xsl:otherwise>
																</xsl:choose>	
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
													</xsl:otherwise>
												</xsl:choose>
												
											</xsl:for-each>
										</fo:table-body>							
									</fo:table>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>