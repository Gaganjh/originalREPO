<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="InvestmentSupplementarySelectedTemplate">
		<fo:page-sequence master-reference="InvestmentOptionSupplementaryLayout">

			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before">
				<fo:block>
					<fo:retrieve-marker retrieve-class-name="firstpage" retrieve-position="first-starting-within-page" retrieve-boundary="page"/>
					<fo:retrieve-marker retrieve-class-name="rest" retrieve-position="first-including-carryover" retrieve-boundary="page-sequence"/>
				</fo:block>
			</fo:static-content>
			
			<!-- Footer -->
			<fo:static-content flow-name="last_page_footer">
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

            <!-- Body -->
			<fo:flow flow-name="xsl-region-body">
			<!--  Header for First Page -->
				<fo:block>
					<fo:marker marker-class-name="firstpage">
						<fo:block-container xsl:use-attribute-sets="header_block_cell1">
								<fo:block-container  xsl:use-attribute-sets="header_block_cell2">
									<fo:block>
									</fo:block>
								</fo:block-container>
						</fo:block-container>
							
						<fo:block-container xsl:use-attribute-sets="header_block_cell3">
							<fo:block id="supplementaryReturnId" padding-before="8px" padding-after="12px" start-indent="18px">
								Investment option rankings – 
								<fo:inline xsl:use-attribute-sets="header_block_cell3_desc">
                                           supplementary return information
                                </fo:inline>		
							</fo:block>
						</fo:block-container>
						
						<fo:block xsl:use-attribute-sets="performance_table_disclaimer" space-before="7px" >
                      		Using a scale of 1 to 100, the following chart shows how the selected investment options rank relative to all mutual funds in the indicated category, based on 1, 3, 5 and 10 year returns. The number of funds in the Morningstar category for each period is also shown. Data is as of <xsl:value-of select="//customization/asOfDateMET"/>.  
                		</fo:block>
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
								<fo:block-container xsl:use-attribute-sets="header_block_cell2">
									<fo:block>
									</fo:block>
								</fo:block-container>
							</fo:block-container>
							
							<fo:block-container xsl:use-attribute-sets="header_block_cell3_continued">
								<fo:block id="supplementaryReturnId" padding-before="3px" start-indent="18px">
									Investment option rankings – 
									<fo:inline xsl:use-attribute-sets="header_block_cell3_desc_continued">
                                      supplementary return information
									</fo:inline>								
							    </fo:block>
							</fo:block-container>
					   </fo:marker>
				</fo:block>
				
				<fo:block space-before="32px">
					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="24%" />
						<fo:table-column column-width="15%" />
						<fo:table-column column-width="15%" />
						<fo:table-column column-width="15%" />
						<fo:table-column column-width="15%" />
						<fo:table-column column-width="16%" />

						<fo:table-header>
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_1_Class_column_header_cell">
									<fo:block>
										<xsl:value-of select="$fundClass" />
									</fo:block>
									<fo:block space-before="1px">
									</fo:block>
									<fo:block>
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="70%" />											
											<fo:table-column column-width="30%" />

											<fo:table-body>
												<fo:table-row display-align="center">
													<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_1_column_header_cell">
														<fo:block text-align="left">
															Investment Options<fo:inline font-size="5pt" baseline-shift="super" font-weight="light">*2</fo:inline>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell background-color="#FFFFFF">
														<fo:block></fo:block>
													</fo:table-cell >
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
								
								<fo:table-cell xsl:use-attribute-sets="fund_ranking_overall_rank">
									<fo:block text-align="center">
										Morningstar Benchmark
									</fo:block>
									<fo:block space-before="1px">
									</fo:block>
									<fo:block>
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="100%" />							
											
											<fo:table-body>
												<fo:table-row display-align="center">
													
													<fo:table-cell xsl:use-attribute-sets="fund_ranking_overall_rank">
														<fo:block text-align="center">
															Category<fo:inline font-size="5pt" font-weight="light" baseline-shift="super">*7</fo:inline>
														</fo:block>
													</fo:table-cell> 
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
									<fo:block>1 YEAR RETURN
							         </fo:block>
									<fo:block space-before="2px">
									</fo:block>								    
									<fo:block>
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="70%" />
											<fo:table-column column-width="30%" />

											<fo:table-body>
												<fo:table-row>
													<fo:table-cell xsl:use-attribute-sets = "fund_ranking_overall_rank">
														<fo:block># of Funds in Cat.                                 
														</fo:block>
													</fo:table-cell>
													<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_Class_column_header_cell">
														<fo:block>Rank
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
									<fo:block>3 YEAR RETURN
							         </fo:block>
									 <fo:block space-before="2px">
									</fo:block>

									<fo:block>
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="70%" />
											<fo:table-column column-width="30%" />

											<fo:table-body>
												<fo:table-row>
													<fo:table-cell xsl:use-attribute-sets = "fund_ranking_overall_rank">
														<fo:block># of Funds in Cat. </fo:block>
													</fo:table-cell>
													<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_Class_column_header_cell">
														<fo:block>Rank
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
									<fo:block>5 YEAR RETURN
							         </fo:block>
									 <fo:block space-before="2px">
									</fo:block>
									<fo:block>
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="70%" />
											<fo:table-column column-width="30%" />
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell xsl:use-attribute-sets = "fund_ranking_overall_rank">
														<fo:block># of Funds in Cat.
														</fo:block>
													</fo:table-cell>
													<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_Class_column_header_cell">
														<fo:block>Rank
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
									<fo:block>10 YEAR RETURN
							         </fo:block>
									 <fo:block space-before="2px">
									</fo:block>
									<fo:block>
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="70%" />
											<fo:table-column column-width="30%" />
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell xsl:use-attribute-sets = "fund_ranking_overall_rank">
														<fo:block># of Funds in Cat.
														</fo:block>
													</fo:table-cell>
													<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_Class_column_header_cell">
														<fo:block>Rank
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
						<xsl:for-each select="customization/selectedAssetClasses/assetClasses/assetClass">
								<xsl:variable name="assetClassID_var">
									<xsl:value-of select="."/>
								</xsl:variable>
									<xsl:variable name="isLastAssetClassID_var">
										<xsl:if test="position() = last()">yes</xsl:if>
									</xsl:variable>
								
								<xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetClassID_var]">
									<xsl:if test="count(fundsBySortOrder) &gt; 0">
											<fo:table-row>
												<fo:table-cell>
												<xsl:attribute name="number-columns-spanned">6</xsl:attribute>
                                        			<fo:block>
                                        				<fo:table table-layout="fixed" width="100%">
															<fo:table-column column-width="24%" />
															<fo:table-column column-width="15%" />
															<fo:table-column column-width="15%" />
															<fo:table-column column-width="15%" />
															<fo:table-column column-width="15%" />
															<fo:table-column column-width="16%" />
																<fo:table-header>
																	<fo:table-row>
																		<fo:table-cell padding-end="0.2cm" background-color="#E1A825" xsl:use-attribute-sets="performance_table_asset_class_title">
																		<xsl:attribute name="number-columns-spanned">2</xsl:attribute>
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
												
																		<fo:block text-align="left" xsl:use-attribute-sets="assethouse_column_row_name">		
																			<fo:inline>	
																				<xsl:value-of select="@nameInCaps" />
																			</fo:inline>
																		</fo:block>
																		</fo:table-cell>
																		<fo:table-cell>
																		<xsl:attribute name="height">13px</xsl:attribute>
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
													
																		<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																			<fo:table>
																			<fo:table-column column-width="70%" />
																			<fo:table-column column-width="30%" />
																				<fo:table-body>
																					<fo:table-row>
																						<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
																						<xsl:attribute name="height">13px</xsl:attribute>
																							<fo:block>&#160;&#160;</fo:block>
																						</fo:table-cell>
																						<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_3_column_header_cell">
																						<xsl:attribute name="height">13px</xsl:attribute>
																							<fo:block>&#160;</fo:block>
																						</fo:table-cell>
																				</fo:table-row>
																				</fo:table-body>
																		</fo:table>
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																	<xsl:attribute name="height">13px</xsl:attribute>
																	<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																			<fo:table table-layout="fixed" width="100%">
																			<fo:table-column column-width="70%" />
																			<fo:table-column column-width="30%" />
																				<fo:table-body>
																					<fo:table-row  display-align="center">
																						<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
																						<xsl:attribute name="height">13px</xsl:attribute>
																							<fo:block>&#160;</fo:block>
																						</fo:table-cell>
																						<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_3_column_header_cell">
																						<xsl:attribute name="height">13px</xsl:attribute>
																							<fo:block>&#160;</fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																				</fo:table-body>
																			</fo:table>
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																	<xsl:attribute name="height">13px</xsl:attribute>
																	<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																			<fo:table table-layout="fixed" width="100%">
																			<fo:table-column column-width="70%" />
																			<fo:table-column column-width="30%" />
																				<fo:table-body>
																					<fo:table-row  display-align="center">
																						<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
																						<xsl:attribute name="height">13px</xsl:attribute>
																							<fo:block>&#160;</fo:block>
																						</fo:table-cell>
																						<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_3_column_header_cell">
																						<xsl:attribute name="height">13px</xsl:attribute>
																							<fo:block>&#160;</fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																				</fo:table-body>
																			</fo:table>
																		</fo:block>
																</fo:table-cell>	
																<fo:table-cell>
																<xsl:attribute name="height">13px</xsl:attribute>
																<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																	<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																		<fo:table table-layout="fixed" width="100%">
																		<fo:table-column column-width="70%" />
																		<fo:table-column column-width="30%" />
																			<fo:table-body>
																				<fo:table-row  display-align="center">
																					<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
																					<xsl:attribute name="height">13px</xsl:attribute>
																						<fo:block>&#160;</fo:block>
																					</fo:table-cell>
																					<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_3_column_header_cell">
																					<xsl:attribute name="height">13px</xsl:attribute>
																						<fo:block>&#160;</fo:block>
																					</fo:table-cell>
																				</fo:table-row>
																			</fo:table-body>
																		</fo:table>
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
														</fo:table-header>
									 				<fo:table-body>
								
											<xsl:for-each select="funds/fund">
												<xsl:variable name="test_var">
													<xsl:value-of select="." />
												</xsl:variable>
												<xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$test_var and @isChecked='yes']">
								
													<fo:table-row display-align="center">
														<fo:table-cell padding-start="0.1cm"
															xsl:use-attribute-sets="solid_blue_border_1BottomSide_fundrank">
															<xsl:attribute name="number-columns-spanned">1</xsl:attribute>
															<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
								
															<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																<fo:table table-layout="fixed" width="100%">
																	<fo:table-column column-width="80%" />
																	<fo:table-column column-width="20%" />
								
																	<fo:table-body>
																		<fo:table-row  display-align="center">
																			<fo:table-cell>
																				<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																					<xsl:value-of select="@name" />
																				</fo:block>
																			</fo:table-cell>
								
																			
																		</fo:table-row>
																	</fo:table-body>
																</fo:table>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-start="0.1cm"
															xsl:use-attribute-sets="solid_blue_border_1BottomSide_fundrank">
								
															<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
								
															<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																<fo:table table-layout="fixed" width="100%">
																	<fo:table-column column-width="100%" />
																	<fo:table-body>
																		<fo:table-row  display-align="center">
																			<fo:table-cell>
																				<xsl:choose>
																					<xsl:when test="@fundMorningstarCategory !=null or @fundMorningstarCategory !=''">
																						<fo:block text-align="center">
																							<xsl:value-of select="@fundMorningstarCategory " />
																						</fo:block>
																					</xsl:when>
																					<xsl:otherwise>
																						<fo:block text-align="center">n/a</fo:block>
																					</xsl:otherwise>
																				</xsl:choose>
																			</fo:table-cell>
																		</fo:table-row>
																	</fo:table-body>
																</fo:table>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="solid_blue_border_1BottomSide_supplementary">
								
															<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
								
															<!-- showing the list of 1 YEAR RETURN values -->
															<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																<fo:table table-layout="fixed" width="100%">
																	<fo:table-column column-width="70%" />
																	<fo:table-column column-width="30%" />
								
																	<fo:table-body>
																		<fo:table-row  display-align="center">
																			<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
																				<fo:block><xsl:value-of select="@oneYearNumberofFundsInCategory" /></fo:block>																				
																			 </fo:table-cell>
																			<fo:table-cell
																				xsl:use-attribute-sets="Investment_Supplementary_table_level_3_column_header_cell">
																				<xsl:choose>
																					<xsl:when test="@oneYearFundRank !=null or @oneYearFundRank !=''">
																						<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																							<xsl:value-of select="@oneYearFundRank" />
																						</fo:block>
																					</xsl:when>
																					<xsl:otherwise>
																						<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">n/a</fo:block>
																					</xsl:otherwise>
																				</xsl:choose>
																			</fo:table-cell>
																		</fo:table-row>
																	</fo:table-body>
																</fo:table>
															</fo:block>
														</fo:table-cell>
														
														<!-- showing the list of 3 YEAR RETURN values -->
														<fo:table-cell xsl:use-attribute-sets="solid_blue_border_1BottomSide_supplementary">								
															<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
								
															<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																<fo:table table-layout="fixed" width="100%">
																	<fo:table-column column-width="70%" />
																	<fo:table-column column-width="30%" />
								
																	<fo:table-body>
																		<fo:table-row display-align="center">
																			<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
																				<fo:block><xsl:value-of select="@threeYearNumberofFundsInCategory" /></fo:block>
																			</fo:table-cell>
																			<fo:table-cell
																				xsl:use-attribute-sets="Investment_Supplementary_table_level_3_column_header_cell">
																				<xsl:choose>
																					<xsl:when
																						test="@threeYearFundRank !=null or @threeYearFundRank !=''">
																						<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																							<xsl:value-of select="@threeYearFundRank" />
																						</fo:block>
																					</xsl:when>
																					<xsl:otherwise>
																						<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">n/a</fo:block>
																					</xsl:otherwise>
																				</xsl:choose>
																			</fo:table-cell>
																		</fo:table-row>
																	</fo:table-body>
																</fo:table>
															</fo:block>
														</fo:table-cell>
														
														<!-- showing the list of 5 YEAR RETURN values -->
														<fo:table-cell xsl:use-attribute-sets="solid_blue_border_1BottomSide_supplementary">
								
															<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
								
															<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																<fo:table table-layout="fixed" width="100%">
																	<fo:table-column column-width="70%" />
																	<fo:table-column column-width="30%" />
																	<fo:table-body>
																		<fo:table-row  display-align="center">
																			<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
																				<fo:block><xsl:value-of select="@fiveYearNumberofFundsInCategory" /></fo:block>
																			</fo:table-cell>
																			<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_3_column_header_cell">
																				<xsl:choose>
																					<xsl:when test="@fiveYearFundRank !=null or @fiveYearFundRank !=''">
																						<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																							<xsl:value-of select="@fiveYearFundRank" />
																						</fo:block>
																					</xsl:when>
																					<xsl:otherwise>
																						<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">n/a</fo:block>
																					</xsl:otherwise>
																				</xsl:choose>
																			</fo:table-cell>
																		</fo:table-row>
																	</fo:table-body>
																</fo:table>
															</fo:block>
														</fo:table-cell>
														
														<!-- showing the list of 10 YEAR RETURN values -->														
														<fo:table-cell xsl:use-attribute-sets="solid_blue_border_1BottomSide_supplementary">
								
															<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
								
															<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																<fo:table table-layout="fixed" width="100%">
																	<fo:table-column column-width="70%" />
																	<fo:table-column column-width="30%" />
								
																	<fo:table-body>
																		<fo:table-row  display-align="center">
																			<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_2_column_header_cell">
																				<fo:block><xsl:value-of select="@tenYearNumberofFundsInCategory" /></fo:block>
																			</fo:table-cell>
																			<fo:table-cell xsl:use-attribute-sets="Investment_Supplementary_table_level_3_column_header_cell">
																				<xsl:choose>
																					<xsl:when test="@tenYearFundRank !=null or @tenYearFundRank !=''">
																						<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																							<xsl:value-of select="@tenYearFundRank" />
																						</fo:block>
																					</xsl:when>
																					<xsl:otherwise>
																						<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">n/a</fo:block>
																					</xsl:otherwise>
																				</xsl:choose>
																			</fo:table-cell>
																		</fo:table-row>
																	</fo:table-body>
																</fo:table>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:if test="$isLastAssetClassID_var != 'yes'">
												<fo:table-row height="13px" keep-with-previous="always">
													<fo:table-cell border-start-color="#FFFFFF" border-start-width="0.2px" border-start-style="solid">
														<xsl:attribute name="number-columns-spanned">
															6
														</xsl:attribute>
													<fo:block/>
												</fo:table-cell>
												</fo:table-row>
											</xsl:if>
										</fo:table-body>
									  </fo:table>
									</fo:block>
								  </fo:table-cell>
								</fo:table-row>
							  </xsl:if>
							</xsl:for-each>
						</xsl:for-each>
						<fo:table-row keep-with-previous="always">
                            <fo:table-cell>
                            <xsl:attribute name="number-columns-spanned">6</xsl:attribute>
                            	<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" line-height="1" padding-before="15px">
                            		<xsl:if test="$criteria_Selected_1_3_5_10YR ='Yes'">
	                           			The total return of some funds may have benefited from expense waiver or expense reimbursement provisions that were in effect during the ranking periods. Some of these provisions may have had a material effect on total return. You can find information on these provisions in fund prospectuses and annual and semi-annual reports.    
                            		</xsl:if>											
                            	</fo:block>								
                            </fo:table-cell>
                        </fo:table-row>
					</fo:table-body>
					</fo:table>
				</fo:block>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>