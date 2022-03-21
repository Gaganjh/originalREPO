<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="FundRankingAddditonalCriteriaTemplate">
			
		<fo:page-sequence master-reference="FundRankingLayout">
		
			<fo:static-content flow-name="xsl-region-before" >
				<fo:block>
					<fo:retrieve-marker retrieve-class-name="firstpage" retrieve-position="first-starting-within-page" retrieve-boundary="page"/>
					<fo:retrieve-marker retrieve-class-name="rest" retrieve-position="first-including-carryover" retrieve-boundary="page-sequence"/>
														
				</fo:block>	
			</fo:static-content>
			
			<fo:static-content flow-name="last_page_footer" >
				<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="46px" xsl:use-attribute-sets="table_border_style"/>
				<fo:table-column column-width="126px" xsl:use-attribute-sets="table_border_style"/>
					<fo:table-body>
						<fo:table-row height = "14px">
							<fo:table-cell padding-before="14px"><fo:block/></fo:table-cell>
							<fo:table-cell padding-before="14px">
								<fo:block xsl:use-attribute-sets="page_count">
									PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminator"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>

			<fo:flow flow-name="xsl-region-body">
				
				<fo:block id="FundRankingId">
					 
					<fo:block keep-with-next="always">
						<fo:marker marker-class-name="firstpage">
							
							<fo:block-container xsl:use-attribute-sets="header_block_cell1" start-indent="46px">
								<fo:block-container  xsl:use-attribute-sets="header_block_cell2">
									<fo:block>
									</fo:block>
								</fo:block-container>
							</fo:block-container>
							
							<fo:block-container display-align="before" xsl:use-attribute-sets="header_block_cell3" start-indent="46px">
								<fo:block padding-before="8px" start-indent="18px">Investment option rankings
								</fo:block>
							</fo:block-container>
							
							<fo:block space-before="9px" xsl:use-attribute-sets="legend_icon_text_style" start-indent="46px">
							
								<xsl:if test="$showCalculatedFund = 'yes'">
									<fo:external-graphic padding-after="-5px" content-width="15px"  content-height="scale-to-fit" height="15px"  scaling="uniform" >
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'calculatedFundIcon.png')"/>
										</xsl:attribute>
									</fo:external-graphic>
									<fo:inline>&#160;<xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/calculatedFundLabel"/></fo:inline>&#160;&#160;&#160;&#160;&#160;&#160;
								</xsl:if>
								<xsl:if test="$showManuallyAddedFund = 'yes'">
									<fo:external-graphic padding-after="-5px" content-height="15px" content-width="15px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'manuallyAddedFundIcon.png')"/>
										</xsl:attribute>
									</fo:external-graphic>
									<fo:inline>&#160;<xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/manuallyAddedFundLabel"/></fo:inline>&#160;&#160;&#160;&#160;&#160;&#160;
								</xsl:if>
								<xsl:if test="$showManuallyRemovedFund = 'yes'">
									<fo:external-graphic padding-after="-5px" content-height="15px" content-width="15px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'manuallyRemovedFundIcon.png')"/>
										</xsl:attribute>
									</fo:external-graphic>
									<fo:inline>&#160;<xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/manuallyRemovedFundLabel"/></fo:inline>&#160;&#160;&#160;&#160;&#160;&#160;
								</xsl:if>
								<xsl:if test="$showContractFundIcon = 'yes'">
									<fo:external-graphic padding-after="-5px" content-height="15px" content-width="15px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'ICON-existing.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
									<fo:inline>&#160;<xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/contractFundIconLabel"/></fo:inline>&#160;&#160;&#160;&#160;&#160;&#160;
								</xsl:if>
								<xsl:if test="$showClosedToNBIcon = 'yes'">
									<fo:external-graphic padding-after="-5px" content-height="15px" content-width="15px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'ICON-closed.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
									<fo:inline>&#160;<xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/closedToNBIconLabel"/></fo:inline>&#160;&#160;&#160;&#160;&#160;&#160;
								</xsl:if>
								<fo:inline padding-after="-5px">n/a – <xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/notApplicableIconLabel"/></fo:inline>															
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
						
							<fo:block-container xsl:use-attribute-sets="header_block_cell1" start-indent="46px">
								<fo:block-container xsl:use-attribute-sets="header_block_cell2">
									<fo:block>
									</fo:block>
								</fo:block-container>
							</fo:block-container>
							
							<fo:block-container display-align="before" xsl:use-attribute-sets="header_block_cell3_continued" start-indent="46px">
								<fo:block padding-before="3px" start-indent="18px">Investment option rankings
								</fo:block>
							</fo:block-container>
							
							<fo:block space-before="11px" xsl:use-attribute-sets="legend_icon_text_style" start-indent="46px">
								<xsl:if test="$showCalculatedFund = 'yes'">
									<fo:external-graphic padding-after="-5px" content-width="15px"  content-height="scale-to-fit" height="15px"  scaling="uniform" >
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'calculatedFundIcon.png')"/>
										</xsl:attribute>
									</fo:external-graphic>
									<fo:inline>&#160;<xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/calculatedFundLabel"/></fo:inline>&#160;&#160;&#160;&#160;&#160;&#160;
								</xsl:if>
								<xsl:if test="$showManuallyAddedFund = 'yes'">
									<fo:external-graphic padding-after="-5px" content-height="15px" content-width="15px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'manuallyAddedFundIcon.png')"/>
										</xsl:attribute>
									</fo:external-graphic>
									<fo:inline>&#160;<xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/manuallyAddedFundLabel"/></fo:inline>&#160;&#160;&#160;&#160;&#160;&#160;
								</xsl:if>
								<xsl:if test="$showManuallyRemovedFund = 'yes'">
									<fo:external-graphic padding-after="-5px" content-height="15px" content-width="15px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'manuallyRemovedFundIcon.png')"/>
										</xsl:attribute>
									</fo:external-graphic>
									<fo:inline>&#160;<xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/manuallyRemovedFundLabel"/></fo:inline>&#160;&#160;&#160;&#160;&#160;&#160;
								</xsl:if>
								<xsl:if test="$showContractFundIcon = 'yes'">
									<fo:external-graphic padding-after="-5px" content-height="15px" content-width="15px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'ICON-existing.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
									<fo:inline>&#160;<xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/contractFundIconLabel"/></fo:inline>&#160;&#160;&#160;&#160;&#160;&#160;
								</xsl:if>
								<xsl:if test="$showClosedToNBIcon = 'yes'">
									<fo:external-graphic padding-after="-5px" content-height="15px" content-width="15px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'ICON-closed.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
									<fo:inline>&#160;<xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/closedToNBIconLabel"/></fo:inline>&#160;&#160;&#160;&#160;&#160;&#160;
								</xsl:if>
								<fo:inline padding-after="-5px">n/a – <xsl:value-of select="/iEvaluatorReport/customization/legendIconsToInclude/iconLabels/notApplicableIconLabel"/></fo:inline>															
							</fo:block>
						</fo:marker>
					</fo:block>

				</fo:block>
				
				<fo:block space-before="18px">
					<xsl:variable name="numberOfSelectedCriterion_var">
						<xsl:value-of select="criteriaSelections/numberOfSelectedCriterion"/>
					</xsl:variable>
					<fo:table space-after="18px" table-layout="fixed" width="100%">
						<fo:table-column column-width="44px" xsl:use-attribute-sets="table_border_style"/>
						<fo:table-column column-width="2px" xsl:use-attribute-sets="table_border_style"/>
						<fo:table-column column-width="192px" xsl:use-attribute-sets="table_border_style"/>
						<fo:table-column column-width="40px" xsl:use-attribute-sets="table_border_style"/>
						
						<xsl:for-each select="criteriaSelections/criterions/criterion/criteria">
							<xsl:variable name="test_var">
								<xsl:value-of select="@weight"/>
							</xsl:variable> 
							<!--Only if Weight is non zero we should add that criteria. If weight is zero then ignore-->						
							<xsl:if test="$test_var!='0%'">
							
								<fo:table-column column-width="38px" xsl:use-attribute-sets="table_border_style"/>
								<fo:table-column column-width="38px" xsl:use-attribute-sets="table_border_style"/>
								<fo:table-column column-width="4px" xsl:use-attribute-sets="table_border_style"/>					
							</xsl:if>
						</xsl:for-each>
						
						<!-- Showing the report table header-->
						<fo:table-header>
							
							<fo:table-row>
							
								<fo:table-cell><fo:block/></fo:table-cell>
								<fo:table-cell><fo:block/></fo:table-cell>
								
								<fo:table-cell display-align="center" padding-before="0.1cm" padding-after="0.1cm" >
									<fo:block xsl:use-attribute-sets="fund_ranking_class_name">
										<xsl:value-of select="$fundClass"/>								
									</fo:block>
									<fo:block  xsl:use-attribute-sets="fund_ranking_overall_rank"> 
										as of <xsl:value-of select="//customization/asOfDateMET"/>
									</fo:block>
								</fo:table-cell>
								
								<fo:table-cell padding-before="0.1cm" padding-after="0.1cm" text-align="center" xsl:use-attribute-sets="overall_rank_bg_color">
									<fo:block xsl:use-attribute-sets="fund_ranking_overall_rank">
										Overall Rank
									</fo:block>
								</fo:table-cell>
								
								<xsl:for-each select="criteriaSelections/criterions/criterion/criteria">
									<xsl:variable name="shortName_var">
										<xsl:value-of select="@shortName"/>
									</xsl:variable>
									<xsl:variable name="test_var">
										<xsl:value-of select="@weight"/>
									</xsl:variable>
									<!--Only if Weight is non zero we should add that criteria. If weight is zero then ignore-->
									<xsl:if test="$test_var!='0%'">
										<fo:table-cell><fo:block/></fo:table-cell>		
										<fo:table-cell><fo:block/></fo:table-cell>		
										<fo:table-cell><fo:block/></fo:table-cell>
									</xsl:if>
								</xsl:for-each>
							
							</fo:table-row>
							
						</fo:table-header>
									
							
						<!-- Showing the report table content-->
						<fo:table-body>
			
							<xsl:for-each select="fundsByAssetClass/assetHouses/assetHouse">

									<xsl:variable name="isLastAssetHouse_var">
										<xsl:if test="position() = last()">yes</xsl:if>
									</xsl:variable>

									<xsl:for-each select="assetClasses/assetClass">
                                      <xsl:if test="count(fundsBySortOrder) &gt; 0">									
										<xsl:variable name="assetClassID_var">
											<xsl:value-of select="@id"/>
										</xsl:variable>
								
										<!-- Suppress Index asset class -->
											<xsl:variable name="isLastAssetClass_var">
												<xsl:if test="position() = last()">yes</xsl:if>
											</xsl:variable>
											
									   <xsl:for-each select="ancestor::iEvaluatorReport/criteriaSelections/criterions/criterion">	
									   
									       <xsl:variable name="criteriaCount_var">
									               <xsl:value-of select="count(./criteria)" />
									       </xsl:variable>	
									       
									       <xsl:variable name="criteriaRow_var">
											<xsl:value-of select="@row"/>
								          </xsl:variable>
											
											<fo:table-row height="10px">
												<fo:table-cell>
												
													<xsl:attribute name="number-columns-spanned">
														<xsl:if test="$numberOfSelectedCriterion_var = 1">7</xsl:if>
														<xsl:if test="$numberOfSelectedCriterion_var = 2">8</xsl:if>
														<xsl:if test="$numberOfSelectedCriterion_var = 3">13</xsl:if>
														<xsl:if test="$numberOfSelectedCriterion_var = 4">16</xsl:if>
														<xsl:if test="$numberOfSelectedCriterion_var = 5">19</xsl:if>
														<xsl:if test="$numberOfSelectedCriterion_var = 6">22</xsl:if>
													</xsl:attribute>
												
													<fo:table table-layout="fixed" width="100%">
														<fo:table-column column-width="44px" xsl:use-attribute-sets="table_border_style"/>
														<fo:table-column column-width="2px" xsl:use-attribute-sets="table_border_style"/>
														<fo:table-column column-width="192px" xsl:use-attribute-sets="table_border_style"/>
														<fo:table-column column-width="40.5px" xsl:use-attribute-sets="table_border_style"/>
																
														<xsl:for-each select="ancestor::iEvaluatorReport/criteriaSelections/criterions/criterion/criteria">
															<xsl:variable name="test_var">
																<xsl:value-of select="@weight"/>
															</xsl:variable>
															<!--Only if Weight is non zero we should add that criteria. If weight is zero then ignore-->
															<xsl:if test="$test_var!='0%'">
																<fo:table-column column-width="38px" xsl:use-attribute-sets="table_border_style"/>
																<fo:table-column column-width="38px" xsl:use-attribute-sets="table_border_style"/>
																<fo:table-column column-width="4px" xsl:use-attribute-sets="table_border_style"/>
															</xsl:if>
														</xsl:for-each>
														<fo:table-header>
														
														 <fo:table-row>
																<fo:table-cell><fo:block/></fo:table-cell>
																<fo:table-cell><fo:block/></fo:table-cell>								
																<fo:table-cell><fo:block/></fo:table-cell>
																<fo:table-cell  xsl:use-attribute-sets="overall_rank_bg_color"><fo:block/></fo:table-cell>		
																
																<xsl:for-each select="./criteria">
																	<xsl:variable name="shortName_var">
																		<xsl:value-of select="@shortName"/>
																	</xsl:variable>
																	<xsl:variable name="test_var">
																		<xsl:value-of select="@weight"/>
																	</xsl:variable>
																	<!--Only if Weight is non zero we should add that criteria. If weight is zero then ignore-->
																	<xsl:if test="$test_var!='0%'">
																		<fo:table-cell display-align="after" padding-before="0.1cm" padding-after="0.1cm">
																			<fo:block text-align="center" font-weight="bold" xsl:use-attribute-sets="performance_table_body_default_font" >
																				<xsl:choose>
																					<xsl:when test="$shortName_var = '1 Year Return'">
																						<fo:block><xsl:value-of select= "substring($shortName_var,0,7)"/> 
																						</fo:block>
																						<fo:block><xsl:value-of select= "substring($shortName_var,7,13)"/>^
																						</fo:block>
																					</xsl:when>												
																					<xsl:when test="$shortName_var = '3 Year Return'">
																						<fo:block><xsl:value-of select= "substring($shortName_var,0,7)"/> 
																						</fo:block>
																						<fo:block><xsl:value-of select= "substring($shortName_var,7,13)"/>^
																						</fo:block>
																					</xsl:when>
																					<xsl:when test="$shortName_var = '5 Year Return'">
																						<fo:block><xsl:value-of select= "substring($shortName_var,0,7)"/> 
																						</fo:block>
																						<fo:block><xsl:value-of select= "substring($shortName_var,7,13)"/>^
																						</fo:block>
																					</xsl:when>
																					<xsl:when test="$shortName_var = '10 Year Return'">
																						<fo:block><xsl:value-of select= "substring($shortName_var,0,8)"/> 
																						</fo:block>
																						<fo:block><xsl:value-of select= "substring($shortName_var,8,13)"/>^
																						</fo:block>
																					</xsl:when>													
																					<xsl:when test="$shortName_var = 'Sharpe Ratio'">
																						<fo:block><xsl:value-of select= "substring($shortName_var,0,7)"/>
																						</fo:block>
																						<fo:block><xsl:value-of select= "substring($shortName_var,7,12)"/>
																						</fo:block>
																					</xsl:when>
																					<xsl:otherwise>
																						<xsl:value-of select= "$shortName_var"/>
																					</xsl:otherwise>
																				</xsl:choose>
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell display-align="after" padding-before="0.1cm" padding-after="0.1cm" >
																			<fo:block text-align="center" font-weight="bold" xsl:use-attribute-sets="performance_table_body_default_font" >
																				Rank
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																	</xsl:if>
																</xsl:for-each>
							                                </fo:table-row>		
																
															<fo:table-row display-align="center" height="13px">
															
																<fo:table-cell><fo:block/></fo:table-cell>
																<fo:table-cell><fo:block/></fo:table-cell>
																
																<fo:table-cell padding-start="0.1cm">	
																	<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																
																	<fo:block text-align="left" xsl:use-attribute-sets="assethouse_column_row_name">		
																		<fo:inline>	
																			<xsl:value-of select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetClassID_var]/@nameInCaps" />
																		</fo:inline>
																	</fo:block>
																</fo:table-cell>
																
																<fo:table-cell>
																	<xsl:attribute name="background-color">#CFB17B</xsl:attribute>
																	<fo:block>
																	</fo:block>
																</fo:table-cell>
																
																<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																	<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																	<fo:block/>
																</fo:table-cell>	
																<fo:table-cell><fo:block/></fo:table-cell>
																
																<xsl:if test="$criteriaCount_var = 2">
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																</xsl:if>
																
																<xsl:if test="$criteriaCount_var = 3">
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block/>
																		</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																	
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																	<fo:block/>
																	</fo:table-cell>
																	
																	<fo:table-cell><fo:block/></fo:table-cell>
																</xsl:if>
																
																<xsl:if test="$criteriaCount_var = 4">
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block/>
																	</fo:table-cell>
																	
																	<fo:table-cell><fo:block/></fo:table-cell>
																	
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block/>
																	</fo:table-cell>
																	
																	<fo:table-cell><fo:block/></fo:table-cell>
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																	<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																</xsl:if>
																
																<xsl:if test="$criteriaCount_var = 5">
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																	<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																</xsl:if>
																
																<xsl:if test="$criteriaCount_var = 6">
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																		<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																	<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																		<xsl:attribute name="background-color">#E1A825</xsl:attribute>
																	<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell><fo:block/></fo:table-cell>
																</xsl:if>
															</fo:table-row>
														</fo:table-header>

														<fo:table-body>
															<xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetClassID_var]/funds/fund">
																<xsl:variable name="test_var">
																	<xsl:value-of select="."/>
																</xsl:variable>
															
											
																<xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$test_var]">
																	<xsl:variable name="isClosedToNB_var">
																		<xsl:value-of select="@isClosedToNB"/>
																	</xsl:variable>
																	<xsl:variable name="isContractFund_var">
																		<xsl:value-of select="@isContractFund"/>
																	</xsl:variable>
																	<xsl:variable name="isChecked_var">
																		<xsl:value-of select="@isChecked"/>
																	</xsl:variable>
																	<xsl:variable name="isToolSelected_var">
																        <xsl:value-of select="@isToolSelected"/>
															        </xsl:variable>
																	<!--<xsl:variable name="measure_var">
																	<xsl:value-of select="@measure"/>
																	</xsl:variable> -->

																	<fo:table-row display-align="center">
																		<fo:table-cell padding-before="-0.05cm" padding-after="-0.15cm" text-align="right">
																			<fo:block>
																				<xsl:if test="$isToolSelected_var = 'yes'">
																				<fo:external-graphic content-height="11px" content-width="10px">
																					<xsl:attribute name="src">
																						<xsl:value-of select="concat($imagePath,'calculatedFundIcon.png')"/>
																					</xsl:attribute>
																				</fo:external-graphic>
																			</xsl:if>
																			<xsl:if test="$isContractFund_var != 'yes' and $isToolSelected_var != 'yes' and $isChecked_var = 'yes'">
																				<fo:external-graphic content-height="10px" content-width="10px">
																					<xsl:attribute name="src">
																						<xsl:value-of select="concat($imagePath,'manuallyAddedFundIcon.png')"/>
																					</xsl:attribute>
																				</fo:external-graphic>
																			</xsl:if>
																			<xsl:if test="( $isContractFund_var = 'yes' or $isToolSelected_var = 'yes' ) and $isChecked_var != 'yes'">
																				<fo:external-graphic content-height="10px" content-width="10px">
																					<xsl:attribute name="src">
																						<xsl:value-of select="concat($imagePath,'manuallyRemovedFundIcon.png')"/>
																					</xsl:attribute>
																				</fo:external-graphic>
																			</xsl:if>
																			<xsl:if test="$isClosedToNB_var = 'yes'">
																				<fo:external-graphic content-height="10px" content-width="10px">
																					<xsl:attribute name="src">
																						<xsl:value-of select="concat($imagePath,'ICON-closed.jpg')"/>
																					</xsl:attribute>
																				</fo:external-graphic>
																			</xsl:if>
																			<xsl:if test="$isContractFund_var = 'yes'">
																				<fo:external-graphic content-height="10px" content-width="10px">
																					<xsl:attribute name="src">
																						<xsl:value-of select="concat($imagePath,'ICON-existing.jpg')"/>
																					</xsl:attribute>
																				</fo:external-graphic>
																			</xsl:if>
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell><fo:block></fo:block></fo:table-cell>
																		<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid_blue_border_1BottomSide_fundrank">
																		
																			<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>				
																			<fo:block xsl:use-attribute-sets="fund_ranking_fund_detail">
																				<xsl:value-of select="@name" />
																			</fo:block>
																		</fo:table-cell>
																		<!--Now looping for metrics details about that fund-->
																		<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid_blue_border_1BottomSide_fundrank">
																			<xsl:attribute name="background-color">#C9C8C2</xsl:attribute>
																			<fo:block text-align="center" xsl:use-attribute-sets="fund_ranking_fund_detail">
																				<xsl:value-of select="fundMetrics/overall/@displayRanking" />
																			</fo:block>
																		</fo:table-cell>
																		<xsl:for-each select="ancestor::iEvaluatorReport/criteriaSelections/criterions/criterion[@row=$criteriaRow_var]/criteria">
																			<xsl:variable name="shortName_var">
										                                         <xsl:value-of select="@shortName"/>
									                                        </xsl:variable>
																			<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid_blue_border_1BottomSide_fundrank">
																			<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																				<fo:block text-align="center" xsl:use-attribute-sets="fund_ranking_fund_detail">
																				<!--<xsl:choose>
																				<xsl:when test="$measure_var = '1 Year Return' or $measure_var = '3 Year Return' or $measure_var = '5 Year Return' or $measure_var = '10 Year Return'">
																				<xsl:value-of select="@measure" />
																				<fo:inline font-family="Arial Narrow"
																				font-size="5pt" baseline-shift="super">^</fo:inline>
																				</xsl:when>
																				<xsl:otherwise><xsl:value-of select="@measure" /></xsl:otherwise>
																				</xsl:choose>-->
																				<xsl:value-of select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$test_var]/fundMetrics/rankings/criteria[@shortname=$shortName_var]/result/@measure" />
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid_blue_border_1BottomSide_fundrank">
																			<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																				<fo:block text-align="center" xsl:use-attribute-sets="fund_ranking_fund_detail">
																					<xsl:value-of select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$test_var]/fundMetrics/rankings/criteria[@shortname=$shortName_var]/result/@rank" />
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell height="10px"><fo:block/></fo:table-cell>
																		</xsl:for-each>
																	</fo:table-row>
													
																</xsl:for-each>		

															</xsl:for-each>
															
															<!--Only if Morningstar node exists, show morning star stuff-->

															<xsl:if test="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetClassID_var]/benchmarkIndex/morningStarBenchmarkIndex/percentileMax">
															
																<xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetClassID_var]/benchmarkIndex">
														
																	<fo:table-row display-align="center" height="13px">
																		<fo:table-cell>
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block></fo:block></fo:table-cell>
																		<fo:table-cell xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																			<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																			<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																			<xsl:attribute name="background-color">#C9C8C2</xsl:attribute>
																			<xsl:attribute name="border-before-color">#C9C8C2</xsl:attribute>
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																			<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																			<xsl:attribute name="number-columns-spanned">2</xsl:attribute>
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell>
																			<fo:block/>
																		</fo:table-cell>
																		<xsl:if test="$criteriaCount_var = 2">
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																		</xsl:if>
																		<xsl:if test="$criteriaCount_var = 3">
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																		</xsl:if>
																		<xsl:if test="$criteriaCount_var = 4">
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																		</xsl:if>
																		<xsl:if test="$criteriaCount_var = 5">
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																		</xsl:if>
																		<xsl:if test="$criteriaCount_var = 6">
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																			<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="fundrank_bg_border_1TopSide">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>
																				<xsl:attribute name="border-before-color">#F5E4C2</xsl:attribute>
																				<fo:block/>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																		</xsl:if>
																	</fo:table-row>
																		
																	<fo:table-row display-align="center">
																		<fo:table-cell>
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block></fo:block></fo:table-cell>
																		<fo:table-cell padding-start="0.1cm" >
																		<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block xsl:use-attribute-sets="performance_table_body_default_font" font-weight="bold">
																			Morningstar® Category Maximum
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell>
																			<xsl:attribute name="background-color">#C9C8C2</xsl:attribute>
																			<fo:block>
																			</fo:block>
																		</fo:table-cell>
																		<xsl:for-each select="ancestor::iEvaluatorReport/criteriaSelections/criterions/criterion[@row=$criteriaRow_var]/criteria">
																		<xsl:variable name="shortName_var">
												                             <xsl:value-of select="@shortName"/>
											                            </xsl:variable>
																			<fo:table-cell padding-start="0.1cm" >
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																					<fo:block text-align="center" xsl:use-attribute-sets="performance_table_body_default_font" font-weight="bold">
																						<xsl:value-of select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetClassID_var]/benchmarkIndex/morningStarBenchmarkIndex[@criteriaName=$shortName_var]/percentileMax/@value" />
																					</fo:block>
																			</fo:table-cell>
																			<fo:table-cell padding-start="0.1cm" >
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																					<fo:block text-align="center" xsl:use-attribute-sets="performance_table_body_default_font" font-weight="bold">
																						<!-- <xsl:value-of select="@percentile" /> -->
																					</fo:block>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																		</xsl:for-each>
																	</fo:table-row>
																	<fo:table-row display-align="center" keep-with-previous="always">
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell><fo:block></fo:block></fo:table-cell>
																		<fo:table-cell padding-start="0.1cm" >
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block xsl:use-attribute-sets="performance_table_body_default_font" font-weight="bold">
																			Morningstar® Category Median
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell>
																			<xsl:attribute name="background-color">#C9C8C2</xsl:attribute>
																			<fo:block/>
																		</fo:table-cell>
																		<xsl:for-each select="ancestor::iEvaluatorReport/criteriaSelections/criterions/criterion[@row=$criteriaRow_var]/criteria">
																		<xsl:variable name="shortName_var">
												                             <xsl:value-of select="@shortName"/>
											                            </xsl:variable>
																			<fo:table-cell padding-start="0.1cm" >
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																				<fo:block text-align="center" xsl:use-attribute-sets="performance_table_body_default_font" font-weight="bold">
																					<xsl:value-of select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetClassID_var]/benchmarkIndex/morningStarBenchmarkIndex[@criteriaName=$shortName_var]/median/@value" />
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell padding-start="0.1cm" >
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																				<fo:block text-align="center" xsl:use-attribute-sets="performance_table_body_default_font" font-weight="bold">
																					<!-- <xsl:value-of select="@percentile" /> -->
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																		</xsl:for-each>
																	</fo:table-row>
																	<fo:table-row display-align="center" keep-with-previous="always">
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell><fo:block></fo:block></fo:table-cell>
																		<fo:table-cell padding-start="0.1cm" >
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block xsl:use-attribute-sets="performance_table_body_default_font" font-weight="bold">
																			Morningstar® Category Minimum
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell xsl:use-attribute-sets="overall_rank_bg_color">
																			<xsl:attribute name="background-color">#C9C8C2</xsl:attribute>
																			<fo:block/>
																		</fo:table-cell>
																		<xsl:for-each select="ancestor::iEvaluatorReport/criteriaSelections/criterions/criterion[@row=$criteriaRow_var]/criteria">
															        	   <xsl:variable name="shortName_var">
										                                      <xsl:value-of select="@shortName"/>
									                                        </xsl:variable>
																			<fo:table-cell padding-start="0.1cm" >
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																				<fo:block text-align="center" xsl:use-attribute-sets="performance_table_body_default_font" font-weight="bold">
																					<xsl:value-of select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetClassID_var]/benchmarkIndex/morningStarBenchmarkIndex[@criteriaName=$shortName_var]/percentileMin/@value" />
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																				<fo:block text-align="center" xsl:use-attribute-sets="performance_table_body_default_font" font-weight="bold">
																					<!-- <xsl:value-of select="@percentile" /> -->
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell><fo:block/></fo:table-cell>
																		</xsl:for-each>
																	</fo:table-row>
																</xsl:for-each>	
															
																<!--Benchmark Index Phrase-->
															
																<fo:table-row keep-with-previous="always">
																	<fo:table-cell><fo:block/></fo:table-cell>
																	<fo:table-cell><fo:block></fo:block></fo:table-cell>
																	<fo:table-cell padding-start="0.1cm" padding-before="0.3cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																		<fo:block text-align="left" font-style="italic" xsl:use-attribute-sets="fund_ranking_fund_detail">	
																			<fo:inline>Benchmark index: </fo:inline>
																			<fo:inline>
																				<xsl:value-of select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetClassID_var]/categoryFootnote/benchmarkName" />
																			</fo:inline>
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																		<xsl:attribute name="background-color">#C9C8C2</xsl:attribute>
																		<fo:block>
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																		<fo:block/>
																	</fo:table-cell>
																					
																	<fo:table-cell><fo:block/></fo:table-cell>
																	<xsl:if test="$criteriaCount_var = 2">
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																	</xsl:if>
																	<xsl:if test="$criteriaCount_var = 3">
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																	</xsl:if>
																	<xsl:if test="$criteriaCount_var = 4">
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																	</xsl:if>
																	<xsl:if test="$numberOfSelectedCriterion_var = 5">
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																	</xsl:if>
																	<xsl:if test="$criteriaCount_var = 6">
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																		<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
																				<xsl:attribute name="background-color">#F5E4C2</xsl:attribute>	
																			<fo:block/>
																		</fo:table-cell>
																		<fo:table-cell><fo:block/></fo:table-cell>
																	</xsl:if>
																</fo:table-row>
															</xsl:if>
															
															<xsl:if test="$isLastAssetHouse_var != 'yes' or $isLastAssetClass_var != 'yes'">
																<fo:table-row height="13px" keep-with-previous="always">
																	<fo:table-cell>
																		<xsl:attribute name="number-columns-spanned">3</xsl:attribute>
																		<fo:block/>
																	</fo:table-cell>
																	<fo:table-cell background-color="#D9D8DA">
																		<fo:block>
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell border-start-color="#FFFFFF" border-start-width="0.2px" border-start-style="solid">
																		<xsl:attribute name="number-columns-spanned">
														
																		<xsl:if test="$numberOfSelectedCriterion_var = 1">3</xsl:if>
																		<xsl:if test="$numberOfSelectedCriterion_var = 2">6</xsl:if>
																		<xsl:if test="$numberOfSelectedCriterion_var = 3">9</xsl:if>
																		<xsl:if test="$numberOfSelectedCriterion_var = 4">12</xsl:if>
																		<xsl:if test="$numberOfSelectedCriterion_var = 5">15</xsl:if>
																		<xsl:if test="$numberOfSelectedCriterion_var = 6">18</xsl:if></xsl:attribute>
																	
																		<fo:block/>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:if>
														</fo:table-body>
													</fo:table>
												</fo:table-cell>
											</fo:table-row>
											</xsl:for-each>
                                      </xsl:if>
									</xsl:for-each>
							</xsl:for-each>	
							<xsl:if test="$criteria_Selected_1_3_5_10YR ='Yes'">
							<fo:table-row height="10px" keep-with-previous="always">
										<fo:table-cell><fo:block></fo:block></fo:table-cell>
										<fo:table-cell><fo:block></fo:block></fo:table-cell>
										<fo:table-cell>
											<xsl:attribute name="number-columns-spanned">
												<xsl:if test="$numberOfSelectedCriterion_var = 1">5</xsl:if>
												<xsl:if test="$numberOfSelectedCriterion_var = 2">8</xsl:if>
												<xsl:if test="$numberOfSelectedCriterion_var = 3">11</xsl:if>
												<xsl:if test="$numberOfSelectedCriterion_var = 4">14</xsl:if>
												<xsl:if test="$numberOfSelectedCriterion_var = 5">17</xsl:if>
												<xsl:if test="$numberOfSelectedCriterion_var = 6">20</xsl:if>
											</xsl:attribute>
												<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" padding-before="15px">
                            							^ Refer to the Investment options rankings – "supplementary return information" section of this report for additional 1, 3, 5, 10 year return rankings.
                            						
                            					</fo:block>
										</fo:table-cell>
							</fo:table-row>	
							</xsl:if>				
						</fo:table-body>
					</fo:table>
				</fo:block>			
			</fo:flow>
		</fo:page-sequence>
		
	</xsl:template>
</xsl:stylesheet>