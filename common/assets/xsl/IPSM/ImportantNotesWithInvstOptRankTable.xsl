<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="ImportantNotesWithInvstOptRankTemplate">
		<fo:page-sequence master-reference="FootNotesLayoutWithInvstOptRank">
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before">
				<fo:table table-layout="fixed" width="100%">
					
					<fo:table-column column-width="115px" />
					<fo:table-column column-width="459px" />
					<fo:table-column column-width="18px" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell height = "18px" background-color="#C4C7AB" >
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
				<fo:table >
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
								<fo:block xsl:use-attribute-sets="footer_disclaimer" padding-before="24px" text-align="right">
									FOR PLAN SPONSOR USE ONLY. NOT FOR USE WITH PLAN PARTICIPANTS.
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block padding-before="26px">
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>
			
			<!-- Body -->
			<fo:flow flow-name="xsl-region-body">
				<fo:block>
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="89px" />
					<fo:table-column column-width="13px" />
					<fo:table-column column-width="435px" />
					<fo:table-column column-width="8px" />
					<fo:table-column column-width="36px" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="2">
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell >
									<fo:block id="ImportantNotesId" xsl:use-attribute-sets="header_block_cell3" text-align="right" padding-before="20px">
										Important notes
									</fo:block>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="2">
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
						<xsl:if test="annual_review_report/isThreeYearCriteriaCode= 'true'">
							<fo:table-row>
								<fo:table-cell number-columns-spanned="2">
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block padding-before="15px" xsl:use-attribute-sets="sub_header_block">
										Investment option rankings - Supplementary return information
									</fo:block>
									<fo:block padding-before="14px" xsl:use-attribute-sets="word_group_font">
										As you have selected one of the 3, 5, or 10-year return criteria as part of your IPS Manager criteria, the following information is provided to show how each Fund that was previously selected through IPS Manager has been ranked, based on a number of different performance return scenarios. Rankings are based solely on the return period shown and do not include any other IPS Manager criteria you have selected.
									</fo:block>
									<fo:block padding-before="14px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
										<fo:inline font-weight="normal">The Fund ranking is based on a scale of 1-100 (1 being the highest quantitative ranking and 100 being the lowest quantitative ranking), and shows how your selected John Hancock Funds rank relative to all mutual funds in the indicated Morningstar category or categories for each return period. For any Fund in your lineup covered under IPS Manager that is identified with a red flag in this review, for your information the corresponding top-ranked Fund in the same asset category is shown below. The data used in this analysis is as of
										<xsl:value-of select="annual_review_report/fundMetricsDate"/>.</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="2">
									<fo:block></fo:block>
								</fo:table-cell>
							</fo:table-row>
							<xsl:if test="annual_review_report/underlyingFundinfo/underlying_funds">
								<fo:table-row>
									<fo:table-cell number-columns-spanned="3">
										<fo:block padding-before="11px">
										<fo:table table-layout="fixed" width="100%">
										<fo:table-column column-width="135px" />
										<fo:table-column column-width="53px" />
										<fo:table-column column-width="51px" />
										<fo:table-column column-width="36px" />
										<fo:table-column column-width="51px" />
										<fo:table-column column-width="36px" />
										<fo:table-column column-width="51px" />
										<fo:table-column column-width="36px" />
										<fo:table-column column-width="51px" />
										<fo:table-column column-width="36px" />
										<fo:table-header>
											<fo:table-row height="46px" display-align="center" background-color="#CC6633" xsl:use-attribute-sets="solid_white_bottom_border">
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" start-indent="3px" font-size="8.5pt">
														Fund Name <fo:inline vertical-align="super" font-size="5.5pt">*2</fo:inline>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" start-indent="4px" padding-before="4px" font-size="8.5pt">
														Morningstar Benchmark Category <fo:inline vertical-align="super" font-size="5.5pt">*7</fo:inline>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" font-size="8.5pt" start-indent="4px" padding-before="4px">
														# of Funds in Category (1 year) 
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" start-indent="4px" padding-before="4px" font-size="8.5pt">
														1 year Rank
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" start-indent="4px" padding-before="4px" font-size="8.5pt">
														# of Funds in Category (3 year) 
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" font-size="8.5pt" start-indent="4px" padding-before="4px">
														3 year Rank 
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" start-indent="4px" padding-before="4px" font-size="8.5pt">
														# of Funds in Category (5 year) 
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" start-indent="4px" padding-before="4px" font-size="8.5pt">
														5 year Rank 
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" font-size="8.5pt" start-indent="4px" padding-before="4px">
														# of Funds in Category (10 year) 
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid_white_border">
													<fo:block xsl:use-attribute-sets="sub_header_block1" font-size="8.5pt" start-indent="4px" padding-before="4px">
														10 year Rank
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-header>
										<fo:table-body>
										<xsl:for-each select="annual_review_report/underlyingFundinfo/underlying_funds">
											<xsl:choose>
												<xsl:when test="position() mod 2 = 0">
													<fo:table-row height="13px" background-color="#FFFFFF" xsl:use-attribute-sets="solid_white_border">
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" border-right-color="white" display-align="center">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" start-indent="3px" display-align="center">
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
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" start-indent="3px" display-align="center">
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
																</xsl:otherwise>
															</xsl:choose>
															
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" display-align="center">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" start-indent="3px" display-align="center">
																		<xsl:value-of select="morningStarName" />
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" start-indent="3px" display-align="center">
																		<xsl:value-of select="morningStarName" />
																	</fo:block>
																</xsl:otherwise>
															</xsl:choose>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" text-align="right" display-align="center">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="oneYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="oneYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:otherwise>
															</xsl:choose>
														</fo:table-cell >
														<fo:table-cell text-align="right" display-align="center" xsl:use-attribute-sets="solid_white_end_border">
															<xsl:if test="oneYearFundRank">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="oneYearFundRank" />
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="oneYearFundRank" />
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
															<xsl:if test="not(oneYearFundRank)">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>	
															</xsl:if>
														</fo:table-cell >
														<fo:table-cell text-align="right" display-align="center" xsl:use-attribute-sets="solid_white_end_border">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="threeYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="threeYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:otherwise>
															</xsl:choose>
														</fo:table-cell>
														<fo:table-cell text-align="right" display-align="center" xsl:use-attribute-sets="solid_white_end_border">
															<xsl:if test="threeYearFundRank">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="threeYearFundRank" />
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="threeYearFundRank" />
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
															<xsl:if test="not(threeYearFundRank)">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
														</fo:table-cell>
														<fo:table-cell text-align="right" display-align="center" xsl:use-attribute-sets="solid_white_end_border">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="fiveYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="fiveYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:otherwise>
															</xsl:choose>
														</fo:table-cell>
														<fo:table-cell text-align="right" display-align="center" xsl:use-attribute-sets="solid_white_end_border">
															<xsl:if test="fiveYearFundRank">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="fiveYearFundRank" />
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="fiveYearFundRank" />
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
															<xsl:if test="not(fiveYearFundRank)">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
														</fo:table-cell>
														<fo:table-cell text-align="right" display-align="center" xsl:use-attribute-sets="solid_white_end_border">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="tenYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="tenYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:otherwise>
															</xsl:choose>
															
														</fo:table-cell>
														<fo:table-cell text-align="right" display-align="center" xsl:use-attribute-sets="solid_white_end_border">
															<xsl:if test="tenYearFundRank">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="tenYearFundRank" />
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="tenYearFundRank" />
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
															<xsl:if test="not(tenYearFundRank)" end-indent="3px">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
														</fo:table-cell>
													</fo:table-row>
												</xsl:when>
												<xsl:otherwise>
													<fo:table-row height="15px" background-color="#E9E9DE" xsl:use-attribute-sets="solid_white_border">
														<fo:table-cell xsl:use-attribute-sets="solid_white_end_border" display-align="center">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" start-indent="3px" display-align="center">
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
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" start-indent="3px" display-align="center">
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
																</xsl:otherwise>
															</xsl:choose>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="solid_white_end_border" display-align="center">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" start-indent="3px" display-align="center">
																		<xsl:value-of select="morningStarName" />
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" start-indent="3px" display-align="center">
																		<xsl:value-of select="morningStarName" />
																	</fo:block>
																</xsl:otherwise>
															</xsl:choose>
														</fo:table-cell>
														<fo:table-cell text-align="right" xsl:use-attribute-sets="solid_white_end_border" display-align="center">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="oneYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="oneYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:otherwise>
															</xsl:choose>
														</fo:table-cell>
														<fo:table-cell text-align="right" xsl:use-attribute-sets="solid_white_end_border" display-align="center">
															<xsl:if test="oneYearFundRank" xsl:use-attribute-sets="solid_white_end_border">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="oneYearFundRank" />
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="oneYearFundRank" />
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
															<xsl:if test="not(oneYearFundRank)" xsl:use-attribute-sets="solid_white_end_border">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
																
															</xsl:if>
														</fo:table-cell>
														<fo:table-cell text-align="right" xsl:use-attribute-sets="solid_white_end_border" display-align="center">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="threeYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="threeYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:otherwise>
															</xsl:choose>
														</fo:table-cell>
														<fo:table-cell text-align="right" xsl:use-attribute-sets="solid_white_end_border" display-align="center">
															<xsl:if test="threeYearFundRank">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="threeYearFundRank" />
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="threeYearFundRank" />
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
															<xsl:if test="not(threeYearFundRank)" xsl:use-attribute-sets="solid_white_end_border">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
														</fo:table-cell>
														<fo:table-cell text-align="right" xsl:use-attribute-sets="solid_white_end_border" display-align="center">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="fiveYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="fiveYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:otherwise>
															</xsl:choose>
														</fo:table-cell>
														<fo:table-cell text-align="right" xsl:use-attribute-sets="solid_white_end_border" display-align="center">
															<xsl:if test="fiveYearFundRank">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="fiveYearFundRank" />
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="fiveYearFundRank" />
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>																
															</xsl:if>
															<xsl:if test="not(fiveYearFundRank)" xsl:use-attribute-sets="solid_white_end_border">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
														</fo:table-cell>
														<fo:table-cell text-align="right" xsl:use-attribute-sets="solid_white_end_border" display-align="center">
															<xsl:choose>
																<xsl:when test="isTopFund = 'true'">
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="tenYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																		<xsl:value-of select="tenYearNoOfFundsInCategoryString" />
																	</fo:block>
																</xsl:otherwise>
															</xsl:choose>
														</fo:table-cell>
														<fo:table-cell text-align="right" xsl:use-attribute-sets="solid_white_end_border" display-align="center">
															<xsl:if test="tenYearFundRank">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="tenYearFundRank" />
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			<xsl:value-of select="tenYearFundRank" />
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
															<xsl:if test="not(tenYearFundRank)" xsl:use-attribute-sets="solid_white_end_border">
																<xsl:choose>
																	<xsl:when test="isTopFund = 'true'">
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_bold_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block xsl:use-attribute-sets="invst_opt_rank_table_contents" end-indent="3px" display-align="center">
																			n/a
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
																
															</xsl:if>
														</fo:table-cell>
													</fo:table-row>
												</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
											
										</fo:table-body>	
									</fo:table>
									</fo:block>
								</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell number-columns-spanned="2">
										<fo:block></fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block padding-before="21px" xsl:use-attribute-sets="word_group_font">
											The total return of some Funds may have benefited from expense waiver or expense reimbursement provisions that were in effect during the ranking periods. Some of these provisions may have had a material effect on total return. You can find information on these provisions in fund prospectuses and annual/semi-annual reports.
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:if>
						</xsl:if>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="2">
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block  padding-before="18px" xsl:use-attribute-sets="footnotes"> 
									<xsl:choose>
										<xsl:when test="/annual_review_report/companyId = '019'">
											John Hancock Life Insurance Company (U.S.A.) and John Hancock Life Insurance Company of New York are collectively referred to as "John Hancock".
										</xsl:when>
										<xsl:when test="/annual_review_report/companyId = '094'">
											John Hancock Life Insurance Company (U.S.A.) and John Hancock Life Insurance Company of New York are collectively referred to as "John Hancock".
										</xsl:when>
									</xsl:choose>
								</fo:block>
								<fo:block  xsl:use-attribute-sets="footnotes" padding-before="3px">
									There is no guarantee that any investment strategy will achieve its objectives.
								</fo:block>
								<fo:block  xsl:use-attribute-sets="footnotes" padding-before="3px">
									Past performance is no guarantee of future results.
								</fo:block>
								<fo:block  xsl:use-attribute-sets="footnotes" padding-before="3px">
									This document is provided by John Hancock for informational purposes only, and while every effort is made to ensure the accuracy of its contents, it should not be relied upon as being tax, legal or financial advice. Neither John Hancock or any of its affiliates, representatives, employees, or agents provides tax, financial or legal advice.
								</fo:block>
								<fo:block xsl:use-attribute-sets="footnotes" padding-before="3px">
									The total revenue John Hancock receives from any Funds advised or sub-advised by John Hancock's affiliates may be higher than those advised or sub-advised by unaffiliated entities. John Hancock's affiliates provide exclusive advisory and sub-advisory services to Funds.  John Hancock and its affiliates may receive additional fees which would be included in the underlying fund's Expense Ratio.
								</fo:block>
								<fo:block xsl:use-attribute-sets="footnotes" padding-before="3px">
									The term “Funds”, refers to sub-accounts investing in underlying mutual funds, offered to qualified retirement plans through a group annuity contract. There can be no assurance that either a Fund or the underlying funds will achieve their investment objectives. A Fund is subject to the same risks as the underlying funds in which it invests, which include the following risks. Stocks can decline due to market, regulatory or economic developments. Investing in foreign securities is subject to certain risks not associated with domestic investing such as currency fluctuations and changes in political and economic conditions. The securities of small capitalization companies are subject to higher volatility than larger, more established companies. High Yield bonds are subject to additional risks such as the increased risk of default (not applicable to Lifestyle Aggressive Portfolio). For a more complete description of these risks, please review the underlying fund's prospectus, which is available upon request. Diversification does not ensure against loss.
								</fo:block>
								<fo:block xsl:use-attribute-sets="footnotes" padding-before="3px">
									A Target Risk  or Target Date Portfolio ("Fund") is a "fund of funds" which invests in a number of underlying funds.  The Fund's ability to achieve its investment objective will depend largely on the ability of the subadviser to select the appropriate mix of underlying funds and on the underlying funds’ ability to meet their investment objectives. There can be no assurance that either a Fund or the underlying funds will achieve their investment objectives. A Fund is subject to the same risks as the underlying funds in which it invests.  Each Fund invests in underlying funds which invest in fixed-income securities (including in some cases high yield securities) and equity securities, including foreign securities and engage in Hedging and Other Strategic Transactions.  To the extent the Fund invests in these securities directly or engages in Hedging and Other Strategic Transactions, the Fund will be subject to the same risks.  As a Fund's asset mix becomes more conservative, the fund becomes more susceptible to risks associated with fixed-income securities.  For a more complete description of these risks, please review the underlying fund's prospectus, which is available upon request.
								</fo:block>
								
								<xsl:if test="annual_review_report/isThreeYearCriteriaCode= 'true'">
									<xsl:if test="annual_review_report/invsOptTableFootNotesList">
										<xsl:for-each select="annual_review_report/invsOptTableFootNotesList/string">
											<fo:block xsl:use-attribute-sets="footnotes" padding-before="3px">
												<xsl:value-of select="." />
											</fo:block>
										</xsl:for-each>	
									</xsl:if>
								</xsl:if>
									<xsl:if test="annual_review_report/footNoteList">
										<xsl:for-each select="annual_review_report/footNoteList/string">
											<fo:block xsl:use-attribute-sets="footnotes" padding-before="3px">
												<xsl:value-of select="." />
											</fo:block>
										</xsl:for-each>	
									</xsl:if>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="2">
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				</fo:block>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>