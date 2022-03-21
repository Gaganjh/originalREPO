<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="YourIpsManagerCriteriaTemplate">
				
		<fo:page-sequence master-reference="CommonPageLayout">
			
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="115px" />
					<fo:table-column column-width="459px" />
					<fo:table-column column-width="18px" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell height = "18px" background-color="#C4C7AB">
								<fo:block></fo:block>
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
										ref-id="terminator" />
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
				<fo:table table-layout="fixed" width="100%" >
					<fo:table-column column-width="13px" />
					<fo:table-column column-width="426px" />
					<fo:table-column column-width="4px" />
					<fo:table-column column-width="34px" />
					<fo:table-body >
						<fo:table-row>
							<fo:table-cell number-columns-spanned="3">
								<fo:block id="IPSManagerReviewCriteriaId" xsl:use-attribute-sets="header_block_cell3" padding-before="20px" text-align="right">
									Your IPS Manager criteria
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							
							<fo:table-cell number-columns-spanned="2">
								<fo:block padding-before="42px" xsl:use-attribute-sets="word_group_font">
									<xsl:choose>
										<xsl:when test="annual_review_report/isInterim = 'true'">
											The following criteria, as previously selected by you, were used to perform the quantitative analysis and rankings in this review.
										</xsl:when>
										<xsl:otherwise>
											The following criteria, as previously selected by you, were used to perform the quantitative analysis and rankings in this annual review.
										</xsl:otherwise>
									</xsl:choose>
								</fo:block>								
								<fo:block padding-before="17px">
									<fo:table table-layout="fixed" width="100%">
										<fo:table-column column-width="213px"/>
										<fo:table-column column-width="210px"/>
										<fo:table-column column-width="3px"/>
										<fo:table-body>
											<fo:table-row height="17px" background-color="#CB5A27">
												<fo:table-cell>
													<fo:block padding-before="3px" xsl:use-attribute-sets="sub_header_block1" font-size="9pt" 
													start-indent="3px" text-align="left">
														CRITERIA &amp; WEIGHTINGS
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block padding-before="3px" xsl:use-attribute-sets="static_content_font2" text-align="right">
														As of: <xsl:value-of select="annual_review_report/latestIpsCriteriaDate"/>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block></fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
								
								<fo:block padding-before="15px">
									<fo:table table-layout="fixed" width="100%">
										<fo:table-column column-width="131px"/>
										<fo:table-column column-width="151px"/>
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell height="104px">
													<fo:block padding-before="3px">
														<fo:table table-layout="fixed" width="100%">
															<fo:table-column column-width="131px"/>
															<fo:table-body>
															
																<fo:table-row height="15px">
																	<fo:table-cell>
																		<xsl:for-each select="annual_review_report/ipsCriteriaInfo/ips_criteria_weighting_info">
																		<fo:block padding-before="2px">
																			<fo:table table-layout="fixed" width="100%">
																				<fo:table-column column-width="20px"/>
																				<fo:table-column column-width="70px"/>
																				<fo:table-column column-width="25px"/>
																				<fo:table-body>
																					<fo:table-row >
																						<fo:table-cell>
																							<fo:block padding-before="2px">
																								<fo:table table-layout="fixed" width="100%">
																									<fo:table-column column-width="10px"/>
																									<fo:table-body>
																									<fo:table-row height="10px">
																										<xsl:attribute name="background-color">
																											<xsl:value-of select="colorCode"/>
																										</xsl:attribute>
																										<fo:table-cell>
																											<fo:block></fo:block>
																										</fo:table-cell>
																									</fo:table-row>
																									</fo:table-body>
																								</fo:table>
																							</fo:block>
																						</fo:table-cell>
																						<fo:table-cell>
																							<fo:block xsl:use-attribute-sets="criteria_display_table" padding-before="2px" text-align="left">
																								<xsl:value-of select="criteriaName"/>
																							</fo:block>
																						</fo:table-cell>
																						<fo:table-cell>
																							<fo:block xsl:use-attribute-sets="criteria_display_table" text-align="right" padding-before="2px">
																								<xsl:value-of select="criteriaWeighting"/>%
																							</fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																				</fo:table-body>
																			</fo:table>
																		</fo:block>
																		<fo:block font-size="2pt">
																			<fo:external-graphic content-width="116px" content-height="0.1px"
																				scaling="non-uniform">
																				<xsl:attribute name="src">
																					<xsl:value-of select="concat($imagePath,'HorizontalKeyline-111x1.jpg')"/>
																				</xsl:attribute>
																			</fo:external-graphic> 
																		</fo:block>
																		</xsl:for-each>
																		<fo:block>
																			<fo:table table-layout="fixed" width="100%">
																				<fo:table-column column-width="20px"/>
																				<fo:table-column column-width="70px"/>
																				<fo:table-column column-width="25px"/>
																				<fo:table-body>
																					<fo:table-row>
																						<fo:table-cell>
																							<fo:block></fo:block>
																						</fo:table-cell>
																						<fo:table-cell>
																							<fo:block xsl:use-attribute-sets="criteria_display_table" text-align="left" padding-before="2px">
																								TOTAL
																							</fo:block>
																						</fo:table-cell>
																						<fo:table-cell>
																							<fo:block xsl:use-attribute-sets="criteria_display_table" text-align="right" padding-before="2px">
																								<xsl:value-of select="annual_review_report/totalWeighting"/>%
																							</fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																				</fo:table-body>
																				
																			</fo:table>
																		</fo:block>
																		<fo:block font-size="2pt">
																			<fo:external-graphic content-height="0.1px" content-width="116px" 
																				scaling="non-uniform">
																				<xsl:attribute name="src">
																					<xsl:value-of select="concat($imagePath,'HorizontalKeyline-111x1.jpg')"/>
																				</xsl:attribute>
																			</fo:external-graphic> 
																		</fo:block>
																	</fo:table-cell>	
																</fo:table-row>
															
															</fo:table-body>
														</fo:table>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell height="114px">
													<fo:block start-indent="35px">
														<fo:external-graphic content-height="184px" content-width="166px">
															<xsl:attribute name="src">url('<xsl:value-of select="annual_review_report/pieChartUrl"/>')</xsl:attribute>
														</fo:external-graphic>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
								
								<fo:block padding-before="35px" xsl:use-attribute-sets="sub_header_block">
									About your criteria
								</fo:block>
								
								<xsl:for-each select="annual_review_report/ipsCriteriaInfo/ips_criteria_weighting_info">
								
								<fo:block padding-before="15px" xsl:use-attribute-sets="word_group_font" font-weight ="bold">
									<xsl:if test="criteriaCode='3YRROR'">
										3-Year Return<fo:inline font-weight ="normal">: The change in the unit value of a Fund for the period indicated. It includes the reinvestment of all distributions made by the underlying investment and any change in the reported value of the underlying investment less applicable fees. For periods greater than one year, the total return is annualized. Refer to the "Investment options rankings - Supplementary return information" in the Important Notes section of this report for additional 1, 3, 5, and 10 year return rankings.</fo:inline>
									</xsl:if>
									<xsl:if test="criteriaCode='5YRROR'">
										5-Year Return<fo:inline font-weight ="normal">: The change in the unit value of a Fund for the period indicated. It includes the reinvestment of all distributions made by the underlying investment and any change in the reported value of the underlying investment less applicable fees. For periods greater than one year, the total return is annualized. Refer to the "Investment options rankings - Supplementary return information" in the Important Notes section of this report for additional 1, 3, 5, and 10 year return rankings.</fo:inline>
									</xsl:if>
									<xsl:if test="criteriaCode='10YRROR'">
										10-Year Return<fo:inline font-weight ="normal">: The change in the unit value of a Fund for the period indicated. It includes the reinvestment of all distributions made by the underlying investment and any change in the reported value of the underlying investment less applicable fees. For periods greater than one year, the total return is annualized. Refer to the "Investment options rankings - Supplementary return information" in the Important Notes section of this report for additional 1, 3, 5, and 10 year return rankings.</fo:inline>
									</xsl:if>
									<xsl:if test="criteriaCode='ALPHA'">
										Alpha<fo:inline font-weight ="normal">: A measure of performance on a risk-adjusted basis. Alpha takes the volatility of a mutual fund and compares its risk-adjusted performance to a benchmark index. The excess return of the Fund relative to the return of the benchmark index is a Fund's alpha.</fo:inline>
									</xsl:if>
									<xsl:if test="criteriaCode='SHARAT'">
										Sharpe Ratio<fo:inline font-weight ="normal">: In the context of this report, the Sharpe Ratio is a measure of the relationship of reward to risk. A higher ratio indicates an investment strategy with lower risk related to return. It is measured by dividing a Fund's average annualized excess return by the annualized standard deviation of its excess return (prior 36 months used as of the date of this report).</fo:inline>
									</xsl:if>
									<xsl:if test="criteriaCode='INFRAT'">
										Information Ratio<fo:inline font-weight ="normal">: The Information Ratio measures the level and consistency of a Fund's active return (return in excess of a benchmark). The ratio is calculated by dividing a Fund's alpha by its active risk (annualized standard deviation of the active return). The Fund with the highest information ratio showed the greatest level of consistency in beating its benchmark. Information ratios calculated for each Fund are based on data from the 36 months prior to the date of this report.</fo:inline>
									</xsl:if>
									<xsl:if test="criteriaCode='RSQRD'">
										R-Squared<fo:inline font-weight ="normal">: A measure of "fit" between a Fund's investment style and that of its benchmark. It can range from 0% to 100% and is measured using the returns of the Fund and its benchmark for the prior 36 months as of the date of this report. Funds with a high R-squared generally exhibit similar volatility patterns to their benchmark, indicating they are probably representative of the asset class or style defined by the benchmark.</fo:inline>
									</xsl:if>
									<xsl:if test="criteriaCode='UPSIDE'">
										Upside Capture<fo:inline font-weight ="normal">: Performance in up markets relative to the benchmark. A ratio of 50% means the Fund's value increased half as much as the benchmark during up markets.</fo:inline>
									</xsl:if>
									<xsl:if test="criteriaCode='DNSIDE'">
										Downside Capture<fo:inline font-weight ="normal">: Performance in down markets relative to the benchmark. A ratio of 50% means the Fund's value fell half as much as the benchmark during down markets.</fo:inline>
									</xsl:if>
									<xsl:if test="criteriaCode='STDDEV'">
										Standard Deviation<fo:inline font-weight ="normal">: A measure of the variability of a Fund's returns from its mean. It is used to gauge dispersion from the Fund's mean return over a three-year period. A Fund with a high standard deviation can be expected to have a wider range of annual returns. In contrast, a Fund with relatively low standard deviation can be expected to have a narrow range of annual returns.</fo:inline>
									
									</xsl:if>
									<xsl:if test="criteriaCode='BETA'">
										Beta<fo:inline font-weight ="normal">: Represents the sensitivity of a Fund's returns to the returns on its benchmark. A Fund that has a beta greater than 1.0 with respect to its benchmark has been more volatile than its benchmark. A Fund with a beta less than 1.0 has been less volatile than its benchmark.</fo:inline>
									</xsl:if>
									<xsl:if test="criteriaCode='EXPRAT'">
										Expense Ratio (ER)<fo:inline font-weight ="normal">: Expenses for a specific unit class for investment options available under a John Hancock group annuity contract. The Expense Ratio ("ER") shown represents the total annual operating expenses for the investment options made available by John Hancock. It is made up of John Hancock's (i) "Revenue from Sub-account", and (ii) the expenses of the underlying fund (based on expense ratios reported in the most recent prospectuses available as of the date of printing; "FER"). In the case where an underlying fund has either waived a portion of, or capped, its fees, the FER used to determine the ER of the sub-account that invests in the underlying fund is the net expense ratio of the underlying fund. "Underlying fund" or "fund" refers to the underlying mutual fund, collective trust, or exchanged traded fund ("ETF") in which the investment option invests.</fo:inline>
													 <fo:block font-weight="normal"  padding-before="5px"> The FER is determined by the underlying fund and is subject to fluctuation. Any change in the FER of an underlying fund will affect the Expense Ratio of the investment option which invests in the underlying fund.</fo:block>
													 <fo:block font-weight="normal"  padding-before="5px"> The ER applies daily at a rate equivalent to the annual rate shown, and may vary to reflect changes in the expenses of an underlying fund and other factors.</fo:block>
													 <fo:block font-weight="normal"  padding-before="5px"> For Expense Ratio information current as of the most recent quarter end, please refer to the monthly "Return and Fees" listing available from John Hancock upon request. For more information, please contact your financial representative.</fo:block>
									</xsl:if>
									<!-- <xsl:value-of select="criteriaDescription"/> -->
								</fo:block>
								</xsl:for-each>
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