<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template name="PerformanceAndExpensesRiskCategoryTemplate">

            <fo:page-sequence master-reference="PerformanceAndExpensesLayout">
                <fo:static-content flow-name="xsl-region-before" >
                    <fo:block>
                        
                       <fo:retrieve-marker retrieve-class-name="firstpage" retrieve-position="first-starting-within-page" retrieve-boundary="page"/>
                        <fo:retrieve-marker retrieve-class-name="rest" retrieve-position="first-including-carryover" retrieve-boundary="page-sequence"/>
                
                     </fo:block>
                </fo:static-content>
                <fo:static-content flow-name="xsl-region-after" >
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
                    
                    <fo:table table-layout="fixed" width="100%">
                        <fo:table-column column-width="46px"/>
                        <fo:table-column column-width="665px"/>
                        <fo:table-body>
                        <fo:table-row height="5px">
                            <fo:table-cell>
                                <fo:block>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block xsl:use-attribute-sets="performance_table_disclaimer">
                                    The performance data presented represents past performance. Past performance is no guarantee of future results and current performance may be lower or higher than the performance quoted. An investment in a sub-account will fluctuate in value to reflect the value of the underlying portfolio and, when redeemed, may be worth more or less than original cost. Performance does not reflect any applicable contract-level or certain participant-level charges, fees for guaranteed benefits if elected by participant, or any redemption fees imposed by an underlying mutual fund company. These charges, if included, would otherwise reduce the total return for a participant&apos;s account. For month-end performance call 1-877-346-8378.
                                	<xsl:if test = "dynamicPerformanceDisclaimer/isContentIncluded[@isIncluded = 'yes']">
                            		The Expense Ratios of the Funds shown below do not reflect changes to the sub-account charges that had taken place on 
                           			<xsl:value-of select="dynamicPerformanceDisclaimer/alignmentEffectiveDateElement"/>.  For details of the changes, please refer to the Fund Sheets.
                            		</xsl:if>
                                </fo:block>
                                <fo:block xsl:use-attribute-sets="performance_table_disclaimer">
                            	 	<xsl:value-of select="dynamicPerformanceDisclaimer/fwiDisclosureTextElement"/>
                           		 </fo:block>
		                         <fo:block xsl:use-attribute-sets="performance_table_disclaimer">
		                         	 <xsl:value-of select="dynamicPerformanceDisclaimer/jhiDisclosureTextElement"/>
		                         </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    
                    <fo:block start-indent="46px" xsl:use-attribute-sets="legend_icon_text_style" space-before="7px">
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
                    
                    <fo:block space-before="0.1cm">
                    
                        <fo:block space-before="0.1cm">

                            <fo:block id="PerformanceExpensesId">
                                 
                                 <fo:block keep-with-next="always">
                                    <fo:marker marker-class-name="firstpage">
                                        
                                        <fo:block-container xsl:use-attribute-sets="header_block_cell1" start-indent="46px">
                                            <fo:block-container  xsl:use-attribute-sets="header_block_cell2">
                                                <fo:block>
                                                </fo:block>
                                            </fo:block-container>
                                        </fo:block-container>
                                        
                                        <fo:block-container display-align="before" xsl:use-attribute-sets="header_block_cell3" start-indent="46px">
                                            <fo:block padding-before="8px" start-indent="18px">Performance &#38; expenses 
                                                <fo:inline xsl:use-attribute-sets="header_block_cell3_desc">
                                                by risk category
                                                </fo:inline>
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
                                    
                                        <fo:block-container xsl:use-attribute-sets="header_block_cell1" start-indent="46px">
                                            <fo:block-container  xsl:use-attribute-sets="header_block_cell2">
                                                <fo:block>
                                                </fo:block>
                                            </fo:block-container>
                                        </fo:block-container>
                                        
                                        <fo:block-container display-align="before" xsl:use-attribute-sets="header_block_cell3_continued" start-indent="46px">
                                            <fo:block padding-before="3px" start-indent="18px">Performance &#38; expenses 
                                                <fo:inline xsl:use-attribute-sets="header_block_cell3_desc_continued">
                                                by risk category
                                                </fo:inline>
                                            </fo:block>
                                        </fo:block-container>
                                        
                                        <fo:block start-indent="46px" space-before="10px" xsl:use-attribute-sets="legend_icon_text_style">
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
                            
                            <fo:block space-before="0.1cm">
                                <fo:table table-layout="fixed" width="100%">
                                                                    
                                <fo:table-column column-width="63px" xsl:use-attribute-sets="table_border_style"/>  
                                <fo:table-column column-width="2px" xsl:use-attribute-sets="table_border_style"/>                               
                                <fo:table-column column-width="178px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="96px" xsl:use-attribute-sets="table_border_style"/>      
                                <fo:table-column column-width="35px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="1.4px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="1.4px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="34px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="1.4px" xsl:use-attribute-sets="table_border_style"/>
                                
                               	<fo:table-column column-width="45px" xsl:use-attribute-sets="table_border_style"/>
								<fo:table-column column-width="15px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="1.4px" xsl:use-attribute-sets="table_border_style"/>
                                <fo:table-column column-width="94px" xsl:use-attribute-sets="table_border_style"/>
                    
                                    
                                    <!-- Showing the report table header-->
                                    <fo:table-header>
    
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell>
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_1_Class_column_header_cell" number-columns-spanned="3">
                                                <fo:block>
                                                <xsl:value-of select="$fundClass"/>     
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_2_column_header_cell" text-align="center" number-columns-spanned="8">
                                                <fo:block>
                                                Hypothetical returns are shown in bold
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_2_column_header_cell" number-columns-spanned="2">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                                
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_2_column_header_cell">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                        
                                        <fo:table-row display-align="center">
                                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_1_column_header_cell" number-columns-spanned="3">
                                                <fo:block>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_1_column_header_cell" number-columns-spanned="3">
                                                <fo:block display-align="before">
                                                Performance as of <xsl:value-of select="customization/asOfDateRor"/>
                                                <fo:inline font-family="Arial Narrow" font-size="5pt" baseline-shift="super">*1</fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_1_column_header_cell" number-columns-spanned="4">
                                                <fo:block display-align="before">
                                                Performance as of <xsl:value-of select="customization/asOfDateRorQe"/>
                                                <fo:inline font-family="Arial Narrow" font-size="5pt" baseline-shift="super">*1</fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_1_column_header_cell" number-columns-spanned="2">
                                                <fo:block display-align="before">
                                                As of <xsl:value-of select="customization/asOfDateExpRatioQe"/>
                                                <fo:inline font-family="Arial Narrow" font-size="5pt" baseline-shift="super">*6</fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_1_column_header_cell">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                        <fo:table-row display-align="center" keep-with-previous="always">
                                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_1_column_header_cell_bottomAligned">
                                                <fo:block display-align="after">
                                                Investment Options
                                                <fo:inline font-family="Arial Narrow" font-size="5pt" baseline-shift="super">*2</fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_1_column_header_cell_bottomAligned">
                                                <fo:block>
                                                Sub-Adviser/Manager&#160;&#160; (Fund Company)
                                                <fo:inline font-family="Arial Narrow" font-size="5pt"  baseline-shift="super">*3</fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_1_column_header_cell_bottomAligned">
                                                <fo:block>
                                                Inception Date
                                                <fo:inline font-family="Arial Narrow" font-size="5pt" baseline-shift="super">*10</fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_2_column_header_cell" text-align="center">
                                                <fo:block>
                                                1 month
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_2_column_header_cell" text-align="center">
                                                <fo:block>
                                                3 month
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_2_column_header_cell" text-align="center">
                                                <fo:block>
                                                YTD
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_2_column_header_cell" text-align="center">
                                                <fo:block>
                                                1 year
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell text-align="center" xsl:use-attribute-sets="performance_table_level_2_column_header_cell">
                                                <fo:block>
                                                3 year
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell text-align="center" xsl:use-attribute-sets="performance_table_level_2_column_header_cell">
                                                <fo:block>
                                                5 year
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_2_column_header_cell" padding-start="0cm" padding-end="0cm">
                                                <fo:block>
                                                10yr/ <fo:inline font-style="italic">Since Inception</fo:inline>
                                                <fo:inline font-family="Arial Narrow" font-size="5pt" baseline-shift="super">*10</fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_2_column_header_cell" text-align="center" number-columns-spanned="2">
                                                <fo:block>
                                                Expense Ratio
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_level_1_column_header_cell_bottomAligned" text-align="center">
                                                <fo:block>
                                                Morningstar Benchmark Category
                                                <fo:inline font-family="Arial Narrow" font-size="5pt"  baseline-shift="super">*7</fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                        </fo:table-row>
                                        
                                    </fo:table-header>
                        
                                    <!-- Showing the report table content-->
                                    <fo:table-body>        

                                      <xsl:for-each select="fundsByInvestmentCategory/investmentCategories/investmentCategory">
                                        <xsl:if test="count(funds/fund) &gt; 0">
                                        	<xsl:variable name="currentInvestmentCategory">
                                        		<xsl:value-of select="@name"/>
                                        	</xsl:variable>
                                            <fo:table-row>
                                                <fo:table-cell>                                                     
                                                    <xsl:attribute name="number-columns-spanned">19</xsl:attribute>
                                                    <fo:table table-layout="fixed" width="100%">
                                        
                                					<fo:table-column column-width="63px" xsl:use-attribute-sets="table_border_style"/>  
                                					<fo:table-column column-width="2px" xsl:use-attribute-sets="table_border_style"/>                               
                                					<fo:table-column column-width="178px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="96px" xsl:use-attribute-sets="table_border_style"/>      
                                					<fo:table-column column-width="35px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="1.4px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="1.4px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="30px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="34px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="1.4px" xsl:use-attribute-sets="table_border_style"/>

                                					<fo:table-column column-width="60px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="1.4px" xsl:use-attribute-sets="table_border_style"/>
                                					<fo:table-column column-width="94px" xsl:use-attribute-sets="table_border_style"/>
                                                        
                                                        
                                                        <fo:table-header>
                                                        
                                                            <fo:table-row height="13px">
                                                            
                                                                <fo:table-cell padding-start="0.1cm">
                                                                    <fo:block>
                                                                    </fo:block>
                                                                </fo:table-cell>
                                                                <fo:table-cell><fo:block/></fo:table-cell>
                                                                
                                                                    <fo:table-cell padding-end="0.2cm" background-color="#E1A825" xsl:use-attribute-sets="performance_table_asset_class_title">
                                                                    <xsl:attribute name="number-columns-spanned">16</xsl:attribute>
                                                                    <xsl:attribute name="background-color"><xsl:value-of select="@colorCode"/></xsl:attribute>
                                                                    <xsl:attribute name="color"><xsl:value-of select="@fontColor"/></xsl:attribute>
                                                                        <fo:block>
                                                                            <xsl:value-of select="@name" />
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                
                                                            </fo:table-row>
                                                        </fo:table-header>

                                                        <fo:table-body>
                                            
                                                            <xsl:for-each select="funds/fund">
                                                                <xsl:variable name="test_var">
                                                                    <xsl:value-of select="."/>
                                                                </xsl:variable>
                                                                <xsl:variable name="isLastFund">
                                                                    <xsl:if test="position() = last()">yes</xsl:if>
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
                                                                    <xsl:variable name="isFeeWaiverIndicator_var">
                                                                        <xsl:value-of select="@isFeeWaiverIndicator"/>
                                                                    </xsl:variable>
                                                                    <xsl:variable name="isRor1monthHypothetical_var">
                                                                        <xsl:value-of select="fundMetrics/ror/ror1month/@isHypothetical"/>
                                                                    </xsl:variable>
                                                                    <xsl:variable name="isRor3monthHypothetical_var">
                                                                        <xsl:value-of select="fundMetrics/ror/ror3month/@isHypothetical"/>
                                                                    </xsl:variable>
                                                                    <xsl:variable name="isRorYtdHypothetical_var">
                                                                        <xsl:value-of select="fundMetrics/ror/rorYtd/@isHypothetical"/>
                                                                    </xsl:variable>
                                                                    <xsl:variable name="isRor1yrQeHypothetical_var">
                                                                        <xsl:value-of select="fundMetrics/rorQe/ror1yrQe/@isHypothetical"/>
                                                                    </xsl:variable>
                                                                    <xsl:variable name="isRor3yrQeHypothetical_var">
                                                                        <xsl:value-of select="fundMetrics/rorQe/ror3yrQe/@isHypothetical"/>
                                                                    </xsl:variable>
                                                                    <xsl:variable name="isRor5yrQeHypothetical_var">
                                                                        <xsl:value-of select="fundMetrics/rorQe/ror5yrQe/@isHypothetical"/>
                                                                    </xsl:variable>
                                                                    <xsl:variable name="isRor10yrQeHypothetical_var">
                                                                        <xsl:value-of select="fundMetrics/rorQe/ror10yrQe/@isHypothetical"/>
                                                                    </xsl:variable>
                                                                    <xsl:variable name="isRor10yrQeAvailable_var">
                                                                        <xsl:value-of select="fundMetrics/rorQe/ror10yrQe"/>
                                                                    </xsl:variable>
                                                                    <xsl:variable name="isToolSelected_var">
																        <xsl:value-of select="@isToolSelected"/>
															        </xsl:variable>
                                                            
                                                            		<xsl:if test="(@name != '3-Year Compound' and @name != '5-Year Compound' and @name != '10-Year Compound')">
                                                                    <fo:table-row display-align="center" height="12px">
                                                                        <fo:table-cell padding-after="-0.15cm" text-align="right">
                                                                            <fo:block>
                                                                               <xsl:if test="$isToolSelected_var = 'yes'">
																					<fo:external-graphic content-height="10px" content-width="10px">
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
                                                                       <fo:table-cell padding-start="0.1cm"  text-align="center" >
                                                                        	<fo:block text-align="center" font-family="Symbol">
                                                                        		<xsl:if test="$isFeeWaiverIndicator_var = 'yes'">
    																					&#8226;
                                                                        		</xsl:if>
                                                                        	</fo:block>
                                                                     	</fo:table-cell>
                                                                        <fo:table-cell padding-start="0.15cm" xsl:use-attribute-sets="solid_blue_border_1BottomSide_performance_table">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <fo:block xsl:use-attribute-sets="performance_table_body_default_font">
                                                                                 		<xsl:value-of select="@name" />
                                                                                        <fo:inline baseline-shift="super" font-family="Arial" font-size="5pt">
                                                                                            <xsl:for-each select="footnotes/footnote-symbol">
                                                                                                <xsl:value-of select="." />
                                                                                                <xsl:if test="position() != last()">,</xsl:if>
                                                                                            </xsl:for-each>
                                                                                        </fo:inline>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell xsl:use-attribute-sets="solid_blue_border_1BottomSide_performance_table">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                                                <xsl:value-of select="@assetManager" />
                                                                            </fo:block>
                                                                        </fo:table-cell>

                                                                        <fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid_blue_border_1BottomSide_performance_table">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <fo:block xsl:use-attribute-sets="performance_table_body_default_font" text-align="center">
                                                                                <xsl:value-of select="@fundDateIntroduced" />
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell background-color="#000000">
                                                                            <fo:block></fo:block>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_monthEnd_1month">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isRor1monthHypothetical_var = 'true'">
                                                                                    <fo:block xsl:use-attribute-sets="performance_table_cell_ror_monthEnd_hypothetical">
                                                                                        <xsl:value-of select="fundMetrics/ror/ror1month" />
                                                                                    </fo:block>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        <xsl:value-of select="fundMetrics/ror/ror1month" />
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_monthEnd_3month">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isRor3monthHypothetical_var = 'true'">
                                                                                    <fo:block xsl:use-attribute-sets="performance_table_cell_ror_monthEnd_hypothetical">
                                                                                        <xsl:value-of select="fundMetrics/ror/ror3month" />
                                                                                    </fo:block>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        <xsl:value-of select="fundMetrics/ror/ror3month" />
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_monthEnd_Ytd">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isRorYtdHypothetical_var = 'true'">
                                                                                    <fo:block xsl:use-attribute-sets="performance_table_cell_ror_monthEnd_hypothetical">
                                                                                        <xsl:value-of select="fundMetrics/ror/rorYtd" />
                                                                                    </fo:block>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        <xsl:value-of select="fundMetrics/ror/rorYtd" />
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell background-color="#000000">
                                                                            <fo:block></fo:block>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_1yr">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isRor1yrQeHypothetical_var = 'true'">
                                                                                    <fo:block xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_hypothetical">
                                                                                        <xsl:value-of select="fundMetrics/rorQe/ror1yrQe" />
                                                                                    </fo:block>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        <xsl:value-of select="fundMetrics/rorQe/ror1yrQe" />
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_3yr">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isRor3yrQeHypothetical_var = 'true'">
                                                                                    <fo:block xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_hypothetical">
                                                                                        <xsl:value-of select="fundMetrics/rorQe/ror3yrQe" />
                                                                                    </fo:block>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        <xsl:value-of select="fundMetrics/rorQe/ror3yrQe" />
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_5yr">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isRor5yrQeHypothetical_var = 'true'">
                                                                                    <fo:block xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_hypothetical">
                                                                                        <xsl:value-of select="fundMetrics/rorQe/ror5yrQe" />
                                                                                    </fo:block>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        <xsl:value-of select="fundMetrics/rorQe/ror5yrQe" />
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_10yr">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isRor10yrQeAvailable_var != 'n/a'">
                                                                                    <xsl:choose>
                                                                                        <xsl:when test="$isRor10yrQeHypothetical_var = 'true'">
                                                                                            <fo:block xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_hypothetical">
                                                                                                <xsl:value-of select="fundMetrics/rorQe/ror10yrQe" />
                                                                                            </fo:block>
                                                                                        </xsl:when>
                                                                                        <xsl:otherwise>
                                                                                            <fo:block>
                                                                                                <xsl:value-of select="fundMetrics/rorQe/ror10yrQe" />
                                                                                            </fo:block>
                                                                                        </xsl:otherwise>
                                                                                    </xsl:choose>   
                                                                                </xsl:when>
                                                                            
                                                                                <xsl:otherwise>
                                                                                    
                                                                                    <fo:block xsl:use-attribute-sets="performance_table_cell_ror_sinceInception">
                                                                                        <xsl:value-of select="fundMetrics/rorQe/rorSinceInceptionQe" />
                                                                                    </fo:block>
                                                                                    
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell background-color="#000000">
                                                                            <fo:block></fo:block>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell xsl:use-attribute-sets="performance_table_cell_expRatio_total">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-after-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            <fo:block>
                                                                                <xsl:value-of select="fundMetrics/expRatioQe/aicRatio" />
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell background-color="#000000">
                                                                            <fo:block></fo:block>
                                                                        </fo:table-cell>
                                                                        
                                                                        <fo:table-cell xsl:use-attribute-sets="performance_table_cell_morningStar">
                                                                        <xsl:if test="$isLastFund = 'yes'">
                                                                            <xsl:attribute name="border-bottom-style">
                                                                                none
                                                                            </xsl:attribute>
                                                                        </xsl:if>
                                                                            
                                                                          <fo:block>
                                                                              <xsl:value-of select="@fundMorningstarCategory" />
                                                                          </fo:block>
                                                                            
                                                                        </fo:table-cell>
                                                                
                                                                
                                                                    </fo:table-row>
                                                        			</xsl:if>
                                                        			
                                                             		 
                                                                    <xsl:if test="moneyMarket7DayYield">
                                                                        <fo:table-row display-align="center" keep-with-previous.within-page="always">
                                                                            <fo:table-cell padding-start="0.3cm">
                                                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                                                </fo:block>
                                                                            </fo:table-cell>
                                                                            <fo:table-cell padding-start="0.3cm" number-columns-spanned="17">
                                                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" text-align="center">
                                                                                    <xsl:value-of select="moneyMarket7DayYield" />
                                                                                </fo:block>
                                                                            </fo:table-cell>
                                                                        </fo:table-row>
                                                                    </xsl:if>
																	<xsl:if test="stableValueFundsCreditRating">
                                                                            <fo:table-row display-align="center" keep-with-previous.within-page="always">
                                                                                <fo:table-cell padding-start="0.3cm">
                                                                                    <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                                <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                                                                <fo:table-cell padding-start="0.3cm" number-columns-spanned="16">
                                                                                    <fo:block xsl:use-attribute-sets="performance_table_body_default_font" text-align="center">
                                                                                        <xsl:value-of select="stableValueFundsCreditRating" />
                                                                                    </fo:block>
                                                                                </fo:table-cell>
                                                                            </fo:table-row>
                                                                        </xsl:if>
                                                                    <!-- <xsl:if test="stableValueFwdLookingCreditRate">
                                                                        <fo:table-row display-align="center" keep-with-previous.within-page="always">
                                                                            <fo:table-cell padding-start="0.3cm">
                                                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                                                </fo:block>
                                                                            </fo:table-cell>
                                                                            <fo:table-cell padding-start="0.3cm" number-columns-spanned="22">
                                                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" text-align="center">
                                                                                    <xsl:value-of select="stableValueFwdLookingCreditRate" />
                                                                                </fo:block>
                                                                            </fo:table-cell>
                                                                        </fo:table-row>
                                                                    </xsl:if> -->
                                                                </xsl:for-each> 
                                                                <xsl:if test="$isLastFund = 'yes' and $currentInvestmentCategory != 'CONSERVATIVE'">
                                                                    <fo:table-row height="5px" keep-with-previous="always">
                                                                        <fo:table-cell number-columns-spanned="18"><fo:block/></fo:table-cell>
                                                                    </fo:table-row>
                                                                </xsl:if>                       
                                                            </xsl:for-each>
                                                
                                                        </fo:table-body>
                                                        
                                                    </fo:table>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </xsl:if>
                                      </xsl:for-each>   
                                      
                                      <!-- Guaranteed Accounts Section START -->
                                     <xsl:if test="$hasGAFunds = 'Yes'">
                                    <xsl:for-each select="fundDetails/guaranteedFundInterestRates">
                                                
                                        <fo:table-row display-align="center" keep-with-previous.within-page="always">
                                            
                                            
                                            <fo:table-cell padding-after="-0.15cm" text-align="right">
                                                <fo:block>
                                                <xsl:if test="$showContractFundIconForGuaranteed != 'yes' and $showCheckedFundIconForGuaranteed = 'yes'">
                                                    <fo:external-graphic content-height="10px" content-width="10px">
                                                        <xsl:attribute name="src">
                                                            <xsl:value-of select="concat($imagePath,'manuallyAddedFundIcon.png')"/>
                                                        </xsl:attribute>
                                                    </fo:external-graphic>
                                                </xsl:if>
                                                <xsl:if test="$showContractFundIconForGuaranteed = 'yes' and $showCheckedFundIconForGuaranteed != 'yes'">
                                                    <fo:external-graphic content-height="10px" content-width="10px">
                                                        <xsl:attribute name="src">
                                                            <xsl:value-of select="concat($imagePath,'manuallyRemovedFundIcon.png')"/>
                                                        </xsl:attribute>
                                                    </fo:external-graphic>
                                                </xsl:if>
                                                <xsl:if test="$showContractFundIconForGuaranteed = 'yes'">
                                                    <fo:external-graphic content-height="10px" content-width="10px">
                                                        <xsl:attribute name="src">
                                                            <xsl:value-of select="concat($imagePath,'ICON-existing.jpg')"/>
                                                        </xsl:attribute>
                                                    </fo:external-graphic>
                                                </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                            <fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid_blue_border_1BottomSide_performance_table">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font">
                                                    Guaranteed Accounts
                                                    <fo:inline baseline-shift="super" font-family="Arial" font-size="5pt">
                                                        <xsl:for-each select="footnotes/footnote-symbol">
                                                            <xsl:value-of select="." />
                                                            <xsl:if test="position() != last()">,</xsl:if>
                                                        </xsl:for-each>
                                                        </fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="solid_blue_border_1BottomSide_performance_table">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                n/a
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="solid_blue_border_1BottomSide_performance_table">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell text-align="right"  xsl:use-attribute-sets="performance_table_cell_ror_monthEnd_3month" number-columns-spanned="3">
                                                <fo:block padding-end="0.1cm" xsl:use-attribute-sets="performance_table_body_default_font" >
                                                annual rate of&#160;
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_1yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_3yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                <xsl:if test="$hasGA3Funds = 'Yes'">
                                               		3-year
                                                </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_5yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                <xsl:if test="$hasGA5Funds = 'Yes'">
                                                	5-year
                                                </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell  xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_10yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                 <xsl:if test="$hasGA10Funds = 'Yes'">
                                                	10-year
                                                </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell  xsl:use-attribute-sets="performance_table_cell_expRatio_total" number-columns-spanned="2">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell  xsl:use-attribute-sets="performance_table_cell_morningStar">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" text-align="center">
                                                n/a
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                        </fo:table-row>
                                        
                                        <xsl:for-each select="previous">
                                        <fo:table-row display-align="center" keep-with-previous.within-page="always">
                                            
                                            <fo:table-cell padding-end="0.1cm" text-align="right">
                                                <fo:block>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                            <fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid_blue_border_1BottomSide_performance_table">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font">
                                                        <xsl:value-of select="@month" />
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="solid_blue_border_1BottomSide_performance_table" number-columns-spanned="2">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                        
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                        
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_monthEnd_3month" number-columns-spanned="3">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_1yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_3yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                 <xsl:if test="$hasGA3Funds = 'Yes'">
                                                    <xsl:value-of select="@threeYear" />
                                                 </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_5yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                    <xsl:if test="$hasGA5Funds = 'Yes'">
                                                    	<xsl:value-of select="@fiveYear" />
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell  xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_10yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                    <xsl:if test="$hasGA10Funds = 'Yes'">
                                                    	<xsl:value-of select="@tenYear" />
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell  xsl:use-attribute-sets="performance_table_cell_expRatio_total" number-columns-spanned="2">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell  xsl:use-attribute-sets="performance_table_cell_morningStar">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" text-align="center">
                                                n/a
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                        </fo:table-row>
                                        </xsl:for-each>     
                                        <xsl:for-each select="current">
                                        <fo:table-row display-align="center" keep-with-previous.within-page="always">
                                            
                                            
                                            <fo:table-cell padding-end="0.1cm" text-align="right">
                                                <fo:block>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                            <fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid_blue_border_1BottomSide_performance_table">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font">
                                                        <xsl:value-of select="@month" />
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="solid_blue_border_1BottomSide_performance_table" number-columns-spanned="2">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                        
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_monthEnd_1month">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_monthEnd_3month" number-columns-spanned="2">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_1yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_3yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                    <xsl:if test="$hasGA3Funds = 'Yes'">
                                                    	<xsl:value-of select="@threeYear" />
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_5yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                    <xsl:if test="$hasGA5Funds = 'Yes'">
                                                    	<xsl:value-of select="@fiveYear" />
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell  xsl:use-attribute-sets="performance_table_cell_ror_quaterEnd_10yr">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                    <xsl:if test="$hasGA10Funds = 'Yes'">
                                                    	<xsl:value-of select="@tenYear" />
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            
                                            
                                            <fo:table-cell  xsl:use-attribute-sets="performance_table_cell_expRatio_total" number-columns-spanned="2">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" >
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell background-color="#000000">
                                                <fo:block></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell  xsl:use-attribute-sets="performance_table_cell_morningStar">
                                                <fo:block xsl:use-attribute-sets="performance_table_body_default_font" text-align="center">
                                                n/a
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                        </fo:table-row>
                                        </xsl:for-each> 
                                
                                    </xsl:for-each>
                                    </xsl:if>
                                       <!-- Guaranteed Accounts Section - END -->
                                      
                                      
									<xsl:if test="fundDetails/simpleAvgExpRatio">
	                             	<fo:table-row display-align="after" keep-with-previous="always">
	                                    <fo:table-cell column-number="8" number-columns-spanned="7" text-align="right">
                                            <fo:block xsl:use-attribute-sets="performance_table_body_default_font">
                                               Average Expense Ratio of Selected Funds<fo:inline baseline-shift="super">+</fo:inline>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" column-number="16" number-columns-spanned="2">
                                            <fo:block xsl:use-attribute-sets="performance_table_body_default_font">
                                               <xsl:value-of select="fundDetails/simpleAvgExpRatio" />
                                            </fo:block>
                                        </fo:table-cell>
	                             	</fo:table-row>	
									</xsl:if>
									<xsl:if test="fundDetails/avgExpenseRatio">
                                    <fo:table-row display-align="after" keep-with-previous="always">
	                                    <fo:table-cell column-number="8" number-columns-spanned="7" text-align="right">
                                            <fo:block xsl:use-attribute-sets="performance_table_body_default_font">
                                               Weighted Average Expense Ratio of Selected Funds<fo:inline baseline-shift="super">++</fo:inline>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center" column-number="16" number-columns-spanned="2">
                                            <fo:block xsl:use-attribute-sets="performance_table_body_default_font">
                                               <xsl:value-of select="fundDetails/avgExpenseRatio" />
                                            </fo:block>
                                        </fo:table-cell>
	                             	</fo:table-row>	
									</xsl:if>

									<fo:table-row display-align="after" keep-with-previous="always">
	                                    <fo:table-cell>
                                            <fo:block>&#160;</fo:block>
                                        </fo:table-cell>
	                             	</fo:table-row>									
																	
									<fo:table-row display-align="after" keep-with-previous="always">
									<fo:table-cell text-align="right" number-columns-spanned="2">
                                            <fo:block>
											</fo:block>
                                    </fo:table-cell>
										
	                                <fo:table-cell text-align="left" number-columns-spanned="17">
                                            <xsl:if test="fundDetails/simpleAvgExpRatio">
                                            <fo:block xsl:use-attribute-sets="performance_footnote_word_text_style" space-after="7px">
                                                +The Average Expense Ratio is determined based on the simple average of the Expense Ratio for the Funds listed above and may vary depending on your final Fund selection.
                                            </fo:block>
											</xsl:if>
											<xsl:if test="fundDetails/avgExpenseRatio">
                                            <fo:block xsl:use-attribute-sets="performance_footnote_word_text_style">
                                                ++The Weighted Average Expense Ratio is determined based on a weighted average of the Expense Ratios for the Funds listed above and may vary depending on your final Fund selections. The calculation of the Weighted Average Expense Ratio assumes 80% of expected assets are invested in asset allocation funds and 20% in non-asset allocation funds. This is based on the asset distribution of new group annuity contracts issued by John Hancock.
                                            </fo:block>
											</xsl:if>
                                    </fo:table-cell>
	                             	</fo:table-row>
									
                                    </fo:table-body>                                    

                                </fo:table>
                            </fo:block>
                        </fo:block>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
    </xsl:template>
</xsl:stylesheet>