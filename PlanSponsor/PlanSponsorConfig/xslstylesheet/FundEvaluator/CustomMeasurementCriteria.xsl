<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="CustomMeasurementCriteriaTemplate">
		<fo:page-sequence master-reference="CustomMeasurementCriteriaLayout">
			
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before">
				<fo:block>
					<fo:retrieve-marker retrieve-class-name="firstpage" retrieve-position="first-starting-within-page" retrieve-boundary="page"/>
					<fo:retrieve-marker retrieve-class-name="rest" retrieve-position="first-including-carryover" retrieve-boundary="page-sequence"/>
				</fo:block>
			</fo:static-content>
			
			<!-- Footer -->
			<fo:static-content flow-name="rest_page_footer">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="126px" />
					<fo:table-body>
						<fo:table-row height="40px">
							<fo:table-cell padding-before="14px">
								<fo:block xsl:use-attribute-sets="page_count">
									PAGE
									<fo:page-number />
									OF
									<fo:page-number-citation-last ref-id="terminator" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>
			
			<!-- Footer -->
			<fo:static-content flow-name="last_page_footer" >
				<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="126px"/>
				<fo:table-column column-width="550px" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell padding-before="14px">
								<fo:block xsl:use-attribute-sets="page_count">
									PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminator"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-before="6px" padding-left="10px">
                            			<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" padding-bottom="-2px">
	                            			<fo:inline baseline-shift="super">~</fo:inline> If multiple Morningstar categories are represented in the John Hancock Asset Class that includes the Fund, then the Fund is compared to all of the funds in each of the Morningstar Categories.    
                            			</fo:block>
                            </fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>
			
			<!-- Body -->
			<fo:flow flow-name="xsl-region-body">
				<fo:block keep-with-next="always">
					<fo:marker marker-class-name="firstpage">
						<fo:block-container xsl:use-attribute-sets="header_block_cell1">
						<fo:block-container  xsl:use-attribute-sets="header_block_cell2">
							<fo:block>
							</fo:block>
						</fo:block-container>
						</fo:block-container>
				
						<fo:block-container  xsl:use-attribute-sets="header_block_cell3">
							<fo:block id="CustomMeasurementCriteriaId" padding-after="12px" start-indent="18px">Selected measurement criteria
							</fo:block>
						</fo:block-container>
					</fo:marker>
				</fo:block>
				
				<fo:block>
					<fo:marker marker-class-name="rest"></fo:marker>
					<fo:marker marker-class-name="firstpage"></fo:marker>
			    </fo:block>
					
				<fo:block>
					<fo:marker marker-class-name="firstpage"></fo:marker>
					<fo:marker marker-class-name="rest">
						<fo:block-container xsl:use-attribute-sets="header_block_cell1">
						<fo:block-container  xsl:use-attribute-sets="header_block_cell2">
							<fo:block>
							</fo:block>
						</fo:block-container>
						</fo:block-container>
				
						<fo:block-container  xsl:use-attribute-sets="header_block_cell3_continued">
							<fo:block id="CustomMeasurementCriteriaId" padding-after="4px" start-indent="18px">Selected measurement criteria
							</fo:block>
						</fo:block-container>
					</fo:marker>
				</fo:block>
				
				<fo:block>
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="128px"/>
					<fo:table-column column-width="358px"/>
					<fo:table-column column-width="18px"/>
					<fo:table-column column-width="197px"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell text-align="right" number-rows-spanned="3">
								<fo:block>
									<fo:external-graphic content-height="432px" content-width="5px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Verticle-rule.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell padding-left="15px">
								<!-- Word Group -->
								<fo:block xsl:use-attribute-sets="word_group_style" line-height="1.5" space-after="15px" padding-before="-1px">
									On the following pages, you will find a listing of the investment options in each asset category that most closely match the weighted criteria selected by you or your financial representative.  You can use the quantitative information provided to help you with the selection or monitoring of Funds for your plan.								</fo:block>
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="bold" color="#CD5806" space-before="5px" space-after="6px">
									How this list of investment options was generated
								</fo:block>
								<fo:block xsl:use-attribute-sets="word_group_style" line-height="1.5" space-after="5px" padding-before="-1px">
									Each investment option has been assigned to a Morningstar category based on its underlying mutual fund. The investment option was then ranked by comparing it to all other mutual funds within the same Morningstar<fo:inline baseline-shift="2.2px" font-size="6pt">®</fo:inline> category<fo:inline baseline-shift="super">~</fo:inline> using the weighted measurement criteria that you or your financial representative selected as shown in the chart to the right. The measurement data and rankings are based on the most recent information available from John Hancock and Morningstar<fo:inline baseline-shift="2.2px" font-size="6pt">®</fo:inline>, as of <xsl:value-of select="//customization/asOfDateMET"/>.
								</fo:block>
															
								
								<!-- Glossary Phrase condition check -->
								<xsl:choose>
									<xsl:when test="reportLayout/sections/section[@sectionId = 'GLOS']">
										<!-- <fo:block/> -->
									</xsl:when>
									<xsl:otherwise>
										<fo:block xsl:use-attribute-sets="word_group_style" line-height="1.5" space-before="5px">
											A glossary of terms is available on request.
										</fo:block>
									</xsl:otherwise>
								</xsl:choose>
								<fo:block/>
							</fo:table-cell>
							
							<fo:table-cell number-rows-spanned="2">
								<fo:block/>
							</fo:table-cell>
							
							<!-- PieChart -->
							<fo:table-cell number-rows-spanned="3" >
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="bold" color="#CD5806" space-before="3px" space-after="4px"  padding-start="2px">
										Weighted Measurement Criteria
								</fo:block>
								<fo:block  padding-start="5px">
									<fo:external-graphic content-height="210px" content-width="290px" >
										<xsl:attribute name="src">url('<xsl:value-of select="//criteriaSelections/pieChartElement/pieChartURL"/>')</xsl:attribute>
									</fo:external-graphic>	
								</fo:block>
								
								 
								<fo:block xsl:use-attribute-sets="word_group_style" background-color="#F0ECDA" font-weight="bold" color="#CD5806"  padding-before="10px" 
								padding-after="10px" padding-bottom="10px" 
								space-before="5px" space-after="6px">
								
								
								
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="bold" color="#CD5806" space-before="3px" space-after="4px" >
										&#xA0;Measured By :
								</fo:block>
								
								
								<xsl:for-each select="criteriaSelections/legends/legendRow">
								
								<fo:table table-layout="fixed" width="100%">
								                                                <fo:table-column column-width="6px"/>
																				<fo:table-column column-width="17px"/>
																				<fo:table-column column-width="70px"/>
																				<fo:table-column column-width="5px"/>
																				<fo:table-column column-width="17px"/>
																				<fo:table-column column-width="70px"/>
																				<fo:table-body>
																					<fo:table-row >
																					    <xsl:for-each select="legend">
																					     <fo:table-cell><fo:block></fo:block></fo:table-cell>
																						 <fo:table-cell>
																							<fo:block padding-before="2px">
																								<fo:table table-layout="fixed" width="100%">
																									<fo:table-column column-width="10px"/>
																									<fo:table-body>
																									<fo:table-row height="10px">
																										<xsl:attribute name="background-color">
																											<xsl:value-of select="@color"/>
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
																							<fo:block xsl:use-attribute-sets="word_group_style" padding-before="2px" text-align="left">
																								<xsl:value-of select="@shortName"/>
																							</fo:block>
																						</fo:table-cell>
																					    </xsl:for-each>
																					</fo:table-row>
																				</fo:table-body>
								</fo:table>
								</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row height="5px">
							<fo:table-cell padding-left="15px" column-number="2">
								<fo:block>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<!-- Criteria Subheading and definitions -->
						<fo:table-row>
							<fo:table-cell padding-left="15px" column-number="2">
								<fo:block/>
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="bold" color="#CD5806" space-before="2px" space-after="6px">
									Criteria and weightings used to evaluate Funds
								</fo:block>
								<xsl:for-each select="criteriaSelections/criterions/criterion/criteria[@weight != '0%']">
									<fo:block xsl:use-attribute-sets="word_group_style" font-weight="bold" font-size="8px">
										<xsl:choose>
											<xsl:when test="@shortName = '3 Year Return'">
												<fo:block space-after="5px">
													3 Year Return:&#160;
													<fo:inline font-weight="normal">The change in the unit value of a Fund for the period indicated.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = '5 Year Return'">
												<fo:block space-after="5px">
													5 Year Return:&#160;
													<fo:inline font-weight="normal">The change in the unit value of a Fund for the period indicated.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = '10 Year Return'">
												<fo:block space-after="5px">
													10 Year Return:&#160;
													<fo:inline font-weight="normal">The change in the unit value of a Fund for the period indicated.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = 'Alpha'">
												<fo:block space-after="5px">
													Alpha:&#160;
													<fo:inline font-weight="normal">A measure of performance on a risk-adjusted basis.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = 'Sharpe Ratio'">
												<fo:block space-after="5px">
													Sharpe Ratio:&#160;
													<fo:inline font-weight="normal">A measure of risk-adjusted return.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = 'Information Ratio'">
												<fo:block space-after="5px">
													Information Ratio:&#160;
													<fo:inline font-weight="normal">The level and consistency of a Fund's active return (return in excess of a benchmark).</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = 'R-Squared'">
												<fo:block space-after="5px">
													R-squared:&#160;
													<fo:inline font-weight="normal">A measure of "fit" between a Fund's investment style and that of its benchmark.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = 'Upside Capture'">
												<fo:block space-after="5px">
													Upside Capture:&#160;
													<fo:inline font-weight="normal">Performance in up markets relative to the benchmark.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = 'Downside Capture'">
												<fo:block space-after="5px">
													Downside Capture:&#160;
													<fo:inline font-weight="normal">Performance in down markets relative to the benchmark.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = 'Standard Deviation'">
												<fo:block space-after="5px">
													Standard Deviation:&#160;
													<fo:inline font-weight="normal">The variability of a Fund's returns from its mean.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = 'Beta'">
												<fo:block space-after="5px">
													Beta:&#160;
													<fo:inline font-weight="normal">The sensitivity of a Fund's returns to the returns on its benchmark.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:when test="@shortName = 'Expense Ratio'">
												<fo:block space-after="5px">
													Expense Ratio:&#160;
													<fo:inline font-weight="normal">The total expenses for a Fund.</fo:inline>
												</fo:block>
											</xsl:when>
										</xsl:choose>
									</fo:block>
								</xsl:for-each>	
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				</fo:block>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>