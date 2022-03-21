<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:variable name="isInterim">
		<xsl:value-of select="annual_review_report/isInterim" />
	</xsl:variable>

	<xsl:template name="SummaryAndNextStepsTemplate">
		
		<fo:page-sequence master-reference="SummaryLayout">
		
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
			
			<fo:flow flow-name="xsl-region-body">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="89px" />
					<fo:table-column column-width="436px" />
					<fo:table-column column-width="6px" />
					<fo:table-column column-width="35px" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="2">
								<fo:block id="SummaryId" xsl:use-attribute-sets="header_block_cell3" text-align="right" padding-before="20px">
									Summary
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
								
								<fo:block xsl:use-attribute-sets="sub_header_block" start-indent="13px" padding-before="42px">
									Your IPS Manager Summary
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<xsl:choose>
							<xsl:when test="annual_review_report/replacableFundsInd = 'true'">
								<fo:table-row>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="2">
									<xsl:choose>
										<xsl:when test="annual_review_report/isInterim = 'true'">
											<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
												<fo:inline font-weight="normal">The following chart summarizes the Funds in your lineup that are covered under IPS Manager </fo:inline> and are identified with a red flag in the review. <fo:inline font-weight="normal"> To see how the Fund(s) compare to the other Funds in the corresponding Asset Class, go to the </fo:inline><fo:basic-link color="blue" ><xsl:attribute name="external-destination">url('<xsl:value-of select="annual_review_report/linkToPSWInvstOptionPage"/>')</xsl:attribute>IPS Manager
												</fo:basic-link><fo:inline font-weight="normal"> page of the Plan Sponsor website or speak to your Financial Representative for more details.</fo:inline>
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
												<fo:inline font-weight="normal">The following chart summarizes the Funds in your lineup that are covered under IPS Manager </fo:inline> and are identified with a red flag in the review.  <fo:inline font-weight="normal"> To see how the Fund(s) compare to the other Funds in the corresponding Asset Class, go to the </fo:inline><fo:basic-link color="blue" ><xsl:attribute name="external-destination">url('<xsl:value-of select="annual_review_report/linkToPSWInvstOptionPage"/>')</xsl:attribute>IPS Manager
												</fo:basic-link><fo:inline font-weight="normal"> page of the Plan Sponsor website or speak to your Financial Representative for more details. </fo:inline>
											</fo:block>
										</xsl:otherwise>
									</xsl:choose>
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
										<fo:block padding-before="16px" start-indent="13px" >
										<xsl:if test="annual_review_report/ipsFundInstructionInfo/ips_fund_instruction_info">
											<fo:table table-layout="fixed" width="100%">
												<fo:table-column column-width="145px" />
												<fo:table-column column-width="290px" />
												<fo:table-header>
													<fo:table-row height="17px" display-align="center" 
														background-color="#8B8E57">
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" >
															<fo:block xsl:use-attribute-sets="page_count" font-size="9pt" start-indent="3px">
																Asset class
															</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" >
															<fo:block xsl:use-attribute-sets="page_count" font-size="9pt" start-indent="3px">
																Current Fund
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</fo:table-header>
										
												<fo:table-body>
												<xsl:for-each select="annual_review_report/ipsFundInstructionInfo/ips_fund_instruction_info">
													<fo:table-row height="17px" display-align="center" 
														background-color="#F1F1EA">
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" >
															<fo:block xsl:use-attribute-sets="summary_page_table_contents" start-indent="3px">
																<xsl:value-of select="assestClassName" />
															</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="solid_white_border" >
															<fo:block>
															<xsl:if test="currentFundInfo/current_fund_info">
															 	<xsl:for-each select="currentFundInfo/current_fund_info">
																	<fo:block padding-before="0.1cm" xsl:use-attribute-sets="summary_page_table_contents" start-indent="3px">
																		<fo:basic-link>
																			<xsl:attribute name="external-destination">url('<xsl:value-of select="fundSheetURL"/>')</xsl:attribute>
																		 	<xsl:value-of select="fundName" /> 
																		 </fo:basic-link>
																	</fo:block>
																</xsl:for-each>
													 		</xsl:if>
													 		</fo:block>
														</fo:table-cell>
													</fo:table-row>
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
								<fo:table-row>
									<fo:table-cell>
									<fo:block></fo:block>
									</fo:table-cell>
									<fo:table-cell number-columns-spanned="2">
									<xsl:choose>
										<xsl:when test="annual_review_report/isInterim = 'true'">
											<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
												<fo:inline font-weight="normal">To learn more about the John Hancock Funds available in your plan, please review the Fund Sheets available on the </fo:inline> 
													<fo:basic-link color="blue" ><xsl:attribute name="external-destination">url('<xsl:value-of select="annual_review_report/linkToPSWInvstOptionPage"/>')</xsl:attribute>Contract Investment Options
												</fo:basic-link><fo:inline font-weight="normal"> page of the Plan Sponsor website.</fo:inline>
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
												<fo:inline font-weight="normal">To learn more about the John Hancock Funds available in your plan, please review the Fund Sheets available on the </fo:inline> 
												<fo:basic-link color="blue"><xsl:attribute name="external-destination">url('<xsl:value-of select="annual_review_report/linkToPSWInvstOptionPage"/>')</xsl:attribute> Contract Investment Options
												</fo:basic-link><fo:inline font-weight="normal"> page of the Plan Sponsor website.</fo:inline>
											</fo:block>
										</xsl:otherwise>
									</xsl:choose>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								</fo:table-row>
							</xsl:when>
							<xsl:otherwise>
								<fo:table-row>
									<fo:table-cell>
										<fo:block></fo:block>
									</fo:table-cell>
									<fo:table-cell number-columns-spanned="2">
										<xsl:choose>
											<xsl:when test="annual_review_report/isInterim = 'true'">
												<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
													<fo:inline font-weight="normal">As of <xsl:value-of select="$currentDate"/>, there are</fo:inline> no <fo:inline font-weight="normal">Funds in your investment lineup that have been flagged because of low ranking scores.</fo:inline> 
												</fo:block>
												<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
													<fo:inline font-weight="normal">Other Funds with similar characteristics to the highest ranking fund identified above may also be available under your Plan. To learn more about a top-ranked Fund or the other John Hancock Funds available in your plan, please review the Fund Sheets available in the </fo:inline> 
													<fo:basic-link color="blue" ><xsl:attribute name="external-destination">url('<xsl:value-of select="annual_review_report/linkToPSWInvstOptionPage"/>')</xsl:attribute>Contract Investment Options
													</fo:basic-link><fo:inline font-weight="normal"> page of the Plan Sponsor website.</fo:inline>
												</fo:block>
											</xsl:when>
											<xsl:otherwise>
												<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
													<fo:inline font-weight="normal">As of <xsl:value-of select="$processingDate"/>, there are</fo:inline> no <fo:inline font-weight="normal">Funds in your investment lineup that have been flagged because of low ranking scores.</fo:inline>
												</fo:block>
												<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
													<fo:inline font-weight="normal">Other Funds with similar characteristics to the highest ranking fund identified above may also be available under your Plan. To learn more about a top-ranked Fund or the other John Hancock Funds available in your plan, please review the Fund Sheets available in the </fo:inline> 
													<fo:basic-link color="blue" font-weight="bold" ><xsl:attribute name="external-destination">url('<xsl:value-of select="annual_review_report/linkToPSWInvstOptionPage"/>')</xsl:attribute>Contract Investment Options
													</fo:basic-link><fo:inline font-weight="normal"> page of the Plan Sponsor website.</fo:inline>
												</fo:block>
											</xsl:otherwise>
										</xsl:choose>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block></fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:otherwise>
						</xsl:choose>
			</fo:table-body>
				</fo:table>
			</fo:flow>
		</fo:page-sequence>		
	</xsl:template>
</xsl:stylesheet>