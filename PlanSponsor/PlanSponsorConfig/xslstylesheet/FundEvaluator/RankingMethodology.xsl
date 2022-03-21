<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="RankingMethodologyTemplate">
		<!-- Fund Menu Variable -->
		<xsl:variable name="fundMenu">
			<xsl:choose>
				<xsl:when test="$contractNumber = ''">
					<xsl:value-of select="//fundLineUp/fundMenu"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="//fundLineUp/contract/@contractBaseFundPackageSeries"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<fo:page-sequence master-reference="RankingMethodologyLayout">
			
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before" >
				<fo:block-container xsl:use-attribute-sets="header_block_cell1">
					<fo:block-container  xsl:use-attribute-sets="header_block_cell2">
						<fo:block>
						</fo:block>
					</fo:block-container>
				</fo:block-container>
				
				<fo:block-container  xsl:use-attribute-sets="header_block_cell3">
					<fo:block id="RankingMethodologyId" padding-after="12px" start-indent="18px">Ranking methodology
					</fo:block>
				</fo:block-container>
			</fo:static-content>
			
			<!-- Footer -->
			<fo:static-content flow-name="last_page_footer" >
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="126px"/>
					<fo:table-column column-width="288px"/>
					<fo:table-column column-width="13px"/>
					<fo:table-column column-width="273px"/>
					<fo:table-body>
						<fo:table-row height = "39px">
							<fo:table-cell display-align="after" padding-before="15px">
								<fo:block xsl:use-attribute-sets="page_count">
									PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminator"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-left="14px"  display-align="after" number-columns-spanned="3">
								<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" space-after="4px" line-height="1">
									Past performance is no guarantee of future results. 
								</fo:block>
								<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" line-height="1">	
									^Calculated top-ranked fund denotes the Funds that are identified by FundEvaluator as having the highest quantitative ranking relative to the other Funds in the same asset category(ies) offered on the John Hancock investment platform, based on the weighted criteria provided by you or your financial representative.  The FundEvaluator tool uses standard and well accepted calculations and formulae recognized within the investment industry.
									
									<xsl:for-each select="//criteriaSelections/criterions/criterion/criteria">
										<xsl:if test="@shortName = 'Expense Ratio' and @weight != '0%'">
												When ranking the Expense Ratio, any front-end sales loads have not been considered.
										</xsl:if>
									</xsl:for-each>
									<xsl:if test="$criteria_Selected_1_3_5_10YR ='Yes'">
												When ranking the Return, any waived fees have been considered. 
									</xsl:if>
								</fo:block>									
								<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" line-height="1">
									* For detailed information regarding the Fund’s returns, see the report in the Performance and Expenses section of this document. The Fund’s returns reflect the performance of the underlying mutual fund in which the Fund invests and any applicable charge against Fund assets; it does not reflect any applicable contract or participant-level recordkeeping charges.
								</fo:block>
								<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" line-height="1">
									<fo:inline baseline-shift="super">~</fo:inline> If multiple Morningstar categories are represented in the John Hancock Asset Class that includes the Fund, then the Fund is compared to all of the funds in each of the Morningstar Categories.
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>

			<!-- Body -->
			<fo:flow flow-name="xsl-region-body">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="128px"/>
					<fo:table-column column-width="290px"/>
					<fo:table-column column-width="13px"/>
					<fo:table-column column-width="273px"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell number-rows-spanned="4" text-align="right" padding-right="-0.5px" padding-before="1.2px">
								<fo:block>
									<fo:external-graphic content-height="91px" content-width="5px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Pg6-Vertical-rule-bottom.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell padding-left="14px" number-columns-spanned="3">
								<!-- Word Group -->
								<fo:block xsl:use-attribute-sets="word_group_style" line-height="1.5" space-after="15px">
									The following pages provide a detailed breakdown of the ranking of each investment option shown in the "Investment Option Rankings" section of this report. In addition to the overall rank, the individual investment option is ranked based on each selected measurement criterion shown. For details on the selected measurements and how the ranking is generated, refer to the "Selected measurement criteria" section. 
								</fo:block>
								<fo:block xsl:use-attribute-sets="word_group_style" line-height="1.5" space-after="10px">
									<xsl:choose>
										<xsl:when test="//reportLayout/sections/section[@sectionId='FRSE']">
											Included are the Funds within the <xsl:value-of select="$fundMenu"/> (<xsl:value-of select="$fundClass"/>) that were listed in the FundEvaluator results section. 
										</xsl:when>
										<xsl:when test="//reportLayout/sections/section[@sectionId='FRAV']">
											Included are all available Funds in the <xsl:value-of select="$fundMenu"/> (<xsl:value-of select="$fundClass"/>).  
										</xsl:when>
									</xsl:choose>
									<xsl:if test="$contractNumber != ''">
										The Funds that are already available in your plan are also highlighted in the report, to let you know how they rank, comparatively.
									</xsl:if>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row height="26px">
							<fo:table-cell number-columns-spanned="3">
								<fo:block>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row>
							<fo:table-cell column-number="2" padding-left="14px" padding-before="6px">
								<fo:block xsl:use-attribute-sets="sub_header_style">
									How to read the Fund Rankings
								</fo:block>
							</fo:table-cell>
							<fo:table-cell column-number="4">
								<fo:block/>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row height="5px">
							<fo:table-cell number-columns-spanned="3">
								<fo:block>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row>							
							<fo:table-cell number-columns-spanned="4">
								<fo:block text-align="right">
									<fo:external-graphic content-height="267px" content-width="702px" scaling="non-uniform">
										<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath,'FundEval-HowtoreadFundRankings-0317webMAX.jpg')" />
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>