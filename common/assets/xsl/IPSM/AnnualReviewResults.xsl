<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="AnnualReviewResultsTemplate">
		
		<fo:page-sequence master-reference="AnnualReviewResultFirstAndRestPageLayout">
			
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="115px" />
					<fo:table-column column-width="459px" />
					<fo:table-column column-width="18px" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell height = "18px" background-color="#C4C7AB">
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
								<fo:block id="AnnualReviewResultsId" xsl:use-attribute-sets="header_block_cell3" padding-before="20px" text-align="right">
									<xsl:choose>
										<xsl:when test="annual_review_report/isInterim = 'true'">
											Your complete review results
										</xsl:when>
										<xsl:otherwise>
											Your complete annual review results
										</xsl:otherwise>
									</xsl:choose>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="2">
								<fo:block></fo:block>
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
								<xsl:choose>
									<xsl:when test="annual_review_report/isInterim = 'true'">
										<fo:block padding-before="42px" xsl:use-attribute-sets="word_group_font">
											Using your selected IPS Manager criteria, each Fund that you previously selected through the use of IPS Manager, has been ranked by comparing it to all other mutual funds within the corresponding Morningstar category or categories. The rankings are calculated as of the end of each quarter in the annual review period. Through the use of different colored flag symbols*, this report identifies  those Funds which had consistently ranked below the median ranking fund in the respective Morningstar category or categories based on the IPS Manager criteria that you selected. The results of these calculations are displayed in the chart below.
										</fo:block>
									</xsl:when>
									<xsl:otherwise>
										<fo:block padding-before="42px" xsl:use-attribute-sets="word_group_font">
											Using your selected IPS Manager criteria, each Fund that you previously selected through the use of IPS Manager, has been ranked by comparing it to all other mutual funds within the corresponding Morningstar category or categories. The rankings are calculated as of the end of each quarter in the annual review period. Through the use of different colored flag symbols*, this annual report identifies  those Funds which had consistently ranked below the median ranking fund in the respective Morningstar category or categories based on the IPS Manager criteria that you selected. The results of these calculations are displayed in the chart below.
										</fo:block>
									</xsl:otherwise>
								</xsl:choose>		
								
								<fo:block padding-before="20px">
									<fo:table table-layout="fixed" width="100%">
										<fo:table-column column-width="68px"/>
										<fo:table-column column-width="365px"/>
										<fo:table-body>
											<fo:table-row height="19px"  xsl:use-attribute-sets="solid_white_bottom_border" background-color="#414B56">
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" start-indent="4px" padding-before="4px" font-size="9pt">
														Flag
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" font-size="9pt" start-indent="4px" padding-before="4px" text-align="left">
														Description
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row height="19px" background-color="#ECEDEE" xsl:use-attribute-sets="solid_white_bottom_border">
												<fo:table-cell xsl:use-attribute-sets="solid_white_end_border" background-color="#FFFFFF">
													<fo:block text-align="center">
														<fo:external-graphic content-height="17px" content-width="30px">
															<xsl:attribute name="src">
																<xsl:value-of select="concat($imagePath,'IPSManager-REDFlag.jpg')"/>
															</xsl:attribute>
														</fo:external-graphic>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_end_border">
													<fo:block padding-before="3px"  padding-left="1px" xsl:use-attribute-sets="word_group_font" start-indent="4px" font-size="8pt" text-align="left">
														Investment options which have had a ranking of 51 to 100 for four consecutive quarters
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row height="19px" background-color="#ECEDEE" xsl:use-attribute-sets="solid_white_bottom_border">
												<fo:table-cell xsl:use-attribute-sets="solid_white_end_border" background-color="#FFFFFF">
													<fo:block text-align="center" >
														<fo:external-graphic content-height="17px" content-width="30px">
															<xsl:attribute name="src">
																<xsl:value-of select="concat($imagePath,'IPSManager-YELLOWFlag.jpg')"/>
															</xsl:attribute>
														</fo:external-graphic>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_end_border">
													<fo:block padding-before="3px"  xsl:use-attribute-sets="word_group_font" font-size="8pt" start-indent="4px" text-align="left">
														Investment options which have had a ranking of 51 to 100 for three consecutive quarters
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row height="19px" background-color="#ECEDEE" xsl:use-attribute-sets="solid_white_bottom_border">
												<fo:table-cell xsl:use-attribute-sets="solid_white_end_border" background-color="#FFFFFF">
													<fo:block text-align="center" >
														<fo:external-graphic content-height="17px" content-width="30px">
															<xsl:attribute name="src">
																<xsl:value-of select="concat($imagePath,'IPSManager-GREENFlag.jpg')"/>
															</xsl:attribute>
														</fo:external-graphic>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_end_border">
													<fo:block padding-before="3px"  xsl:use-attribute-sets="word_group_font" start-indent="4px" font-size="8pt" text-align="left">
														All other Funds that do not meet the above descriptions
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											
										</fo:table-body>
									</fo:table>
								</fo:block>	
							</fo:table-cell>
							
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row>
							<fo:table-cell number-columns-spanned="3">
								<fo:block padding-before="28px">
									<xsl:if test="annual_review_report/ipsReviewAnalysisInfo/ips_review_analysis_info">
									<fo:table table-layout="fixed" width="100%">
										<fo:table-column column-width="186px" />
										<fo:table-column column-width="62px" />
										<fo:table-column column-width="62px" />
										<fo:table-column column-width="62px" />
										<fo:table-column column-width="62px" />
										<fo:table-column column-width="101px" />
										
										<fo:table-header>
											
											<fo:table-row height = "18px" display-align="center" xsl:use-attribute-sets="solid_white_border"
											background-color="#CB5A27">
												<fo:table-cell number-columns-spanned="6">
													<fo:block xsl:use-attribute-sets="sub_header_block1" font-size="9pt" start-indent="3px" text-align="left">
														RANKING ANALYSIS RESULTS
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											
											<fo:table-row height = "42px" display-align="center">
												<fo:table-cell background-color="#CB5A27" xsl:use-attribute-sets="solid_white_border">
													<fo:block></fo:block>
												</fo:table-cell>
												<fo:table-cell number-columns-spanned="4" xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="word_group_font" text-align="center">
														<fo:table table-layout="fixed" width="100%">
															<fo:table-column column-width="62px" />
															<fo:table-column column-width="62px" />
															<fo:table-column column-width="62px" />
															<fo:table-column column-width="62px" />
															
															<fo:table-body>
																<fo:table-row height = "17px">
																	<fo:table-cell number-columns-spanned="3" xsl:use-attribute-sets="solid_white_border" 
																	background-color="#E2E3D5"> 
																		<fo:block xsl:use-attribute-sets="analysis_review_display_table" text-align="center">
																			Previous Three Quarters
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell xsl:use-attribute-sets="solid_white_border" background-color="#DADBC8">
																		<fo:block xsl:use-attribute-sets="analysis_review_display_table" text-align="center">
																			Last Quarter
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
																<fo:table-row height = "24px">
																	<fo:table-cell xsl:use-attribute-sets="solid_white_border" background-color="#E2E3D5">
																		<fo:block xsl:use-attribute-sets="quarter_end_dates" text-align="center">
																			Period Ending <xsl:value-of select="annual_review_report/fourthQuarterEndDate" />
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell xsl:use-attribute-sets="solid_white_border" background-color="#E2E3D5">
																		<fo:block xsl:use-attribute-sets="quarter_end_dates" text-align="center">
																			Period Ending <xsl:value-of select="annual_review_report/thirdQuarterEndDate" />
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell xsl:use-attribute-sets="solid_white_border" background-color="#E2E3D5">
																		<fo:block xsl:use-attribute-sets="quarter_end_dates" text-align="center">
																			Period Ending <xsl:value-of select="annual_review_report/secondQuarterEndDate" />
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell xsl:use-attribute-sets="solid_white_border" background-color="#DADBC8 ">
																		<fo:block xsl:use-attribute-sets="quarter_end_dates" text-align="center">
																			Period Ending <xsl:value-of select="annual_review_report/lastQuarterEndDate" />
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</fo:table-body>
														</fo:table>
													</fo:block>
												</fo:table-cell>
												
												<fo:table-cell xsl:use-attribute-sets="solid_white_border" background-color="#CB5A27">
													<fo:block xsl:use-attribute-sets="sub_header_block1" font-size="9pt" text-align="center" font-weight="bold">
														
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-header>
										
										<fo:table-body>
											<xsl:for-each select="annual_review_report/ipsReviewAnalysisInfo/ips_review_analysis_info">
												<fo:table-row background-color="#F1F1EA" height="15px" display-align="center">
													<fo:table-cell number-columns-spanned="3">
														<fo:block xsl:use-attribute-sets="annual_report_table_contents" text-align="left" start-indent="3px" color="#6D6F71">
															John Hancock Asset Class: <xsl:value-of select="assestClassName" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell number-columns-spanned="3">
														<fo:block xsl:use-attribute-sets="annual_report_table_contents" text-align="right" color="#6D6F71" end-indent="3px">
															Morningstar Category: <xsl:value-of select="morningStarName" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always" display-align="center" height="15px" xsl:use-attribute-sets="solid_white_bottom_border">
													<fo:table-cell background-color="#FFFFFF" xsl:use-attribute-sets="solid_white_end_border">
														<fo:block xsl:use-attribute-sets="annual_report_table_contents" text-align="left" start-indent="3px" color="#58595B" font-size="9pt">
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
													<fo:table-cell background-color="#E2E3D5" xsl:use-attribute-sets="solid_white_end_border">
														<fo:block xsl:use-attribute-sets="annual_report_table_contents" text-align="center" color="#58595B" font-size="9pt">
															<xsl:value-of select="fourthQuarterRank" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell background-color="#E2E3D5" xsl:use-attribute-sets="solid_white_end_border">
														<fo:block xsl:use-attribute-sets="annual_report_table_contents" text-align="center" color="#58595B" font-size="9pt">
															<xsl:value-of select="thirdQuarterRank" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell background-color="#E2E3D5" xsl:use-attribute-sets="solid_white_end_border">
														<fo:block xsl:use-attribute-sets="annual_report_table_contents" text-align="center"  color="#58595B" font-size="9pt">
															<xsl:value-of select="secondQuarterRank" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell background-color="#DADBC8" xsl:use-attribute-sets="solid_white_end_border">
														<fo:block xsl:use-attribute-sets="annual_report_table_contents" text-align="center" color="#58595B" font-size="9pt">
															<xsl:value-of select="firstQuarterRank" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell background-color="#FFFFFF">
														<fo:block text-align="center">
															<xsl:choose>
																<xsl:when test="thresholdIdicator = 'FR' ">
																	<fo:external-graphic content-height="10px" content-width="16px">
																		<xsl:attribute name="src">
																			<xsl:value-of select="concat($imagePath,'IPSManager-REDFlag.jpg')"/>
																		</xsl:attribute>
																	</fo:external-graphic>
																</xsl:when>
																<xsl:when test="thresholdIdicator = 'WA' ">
																	<fo:external-graphic content-height="10px" content-width="16px">
																		<xsl:attribute name="src">
																			<xsl:value-of select="concat($imagePath,'IPSManager-YELLOWFlag.jpg')"/>
																		</xsl:attribute>
																	</fo:external-graphic>
																</xsl:when>
																<xsl:when test="thresholdIdicator = 'GS' ">
																	<fo:external-graphic content-height="10px" content-width="16px">
																		<xsl:attribute name="src">
																			<xsl:value-of select="concat($imagePath,'IPSManager-GREENFlag.jpg')"/>
																		</xsl:attribute>
																	</fo:external-graphic>
																</xsl:when>
															</xsl:choose>
														</fo:block>
													</fo:table-cell>
												</fo:table-row> 
											</xsl:for-each>
											<fo:table-row keep-with-previous="always" >
											<fo:table-cell number-columns-spanned="6">
													<fo:block padding-before="20px" start-indent="102px" xsl:use-attribute-sets="disclaimer">
														Past Performance is no guarantee of future results.
													</fo:block>
													<fo:block padding-before="5px" start-indent="102px" xsl:use-attribute-sets="disclaimer">
														There is no guarantee that any investment strategy will achieve its objectives.
													</fo:block>
													<!-- <fo:block padding-before="10px" start-indent="102px" xsl:use-attribute-sets="disclaimer" font-weight="bold">
														<fo:inline font-weight="normal">*The use of a red flag symbol or a yellow flag symbol is meant only to reflect that the quantitative score of the Fund selected has fallen below the median score of the funds in the corresponding Morningstar category or categories for the prior three or four consecutive quarters. It is not intended to suggest any qualitative implications of the Fund. The sole use of this score is</fo:inline> not <fo:inline font-weight="normal">to determine if a Fund should be removed or is imprudent or insufficient for the identified needs of the plan.</fo:inline>
													</fo:block> -->
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
										
									</fo:table>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="3">
								<fo:block>
									<fo:table table-layout="fixed" width="100%">
										<fo:table-column column-width="186px" />
										<fo:table-column column-width="62px" />
										<fo:table-column column-width="62px" />
										<fo:table-column column-width="62px" />
										<fo:table-column column-width="62px" />
										<fo:table-column column-width="101px" />
										
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell number-columns-spanned="6">
													<fo:block padding-before="10px" start-indent="102px" xsl:use-attribute-sets="disclaimer" font-weight="bold">
														<fo:inline font-weight="normal">*The use of the flag symbols is meant only to reflect that the quantitative ranking of the Fund selected has fallen below the median score of the funds in the corresponding Morningstar category or categories for the prior three or four consecutive quarters. The information is provided to help facilitate your review and is not intended to suggest any qualitative implications of the Fund. The sole use of this score or the symbols is</fo:inline> not <fo:inline font-weight="normal">to determine if a Fund should be removed or is imprudent or insufficient for the identified needs of the plan.  In identifying any Fund with a symbol, John Hancock does not make any recommendation regarding the selection of the Fund for – or removal of the Fund from – your plan’s investment line-up.  The plan’s Responsible Plan Fiduciary, the Trustee and/or designated 3(38) Investment Manager are ultimately responsible as a fiduciary for selecting, monitoring, and managing the investment options on behalf of their plan, and for determining whether or how to use the information provided in the reports.</fo:inline>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
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