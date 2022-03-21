<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template match="/reportLayout/sections" name="TableOfContentsTemplate">
		<fo:page-sequence master-reference="TableOfContentsLayout" initial-page-number="1">
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before" >
				<fo:block-container xsl:use-attribute-sets="header_block_cell1">
					<fo:block-container  xsl:use-attribute-sets="header_block_cell2">
						<fo:block>
						</fo:block>
					</fo:block-container>
				</fo:block-container>
				
				<fo:block-container xsl:use-attribute-sets="header_block_cell3">
					<fo:block padding-after="12px" start-indent="18px">Table of contents
					</fo:block>
				</fo:block-container>
			</fo:static-content>
		  
		  	<fo:static-content flow-name="xsl-region-after">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="143px"/>
					<fo:table-column column-width="134px"/>
					<fo:table-column column-width="8px"/>
					<fo:table-column column-width="134px"/>
					<fo:table-column column-width="8px"/>
					<fo:table-column column-width="134px"/>
					<fo:table-column column-width="141px"/>
					
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell column-number="2" padding-before="17px">
								<fo:block xsl:use-attribute-sets="fundEvaluator_disclaimer_text">
									As noted throughout this FundEvaluator report, there are very important investment option decisions which only you, as the plan trustee, will be able to make for the plan.  It is your duty as a plan fiduciary to make investment selections that are appropriately suited to your plan and employee workforce, based on their individualized needs.  John Hancock is pleased to present information in this FundEvaluator report which may help you in your selection and monitoring of the Funds offered on the John Hancock investment platform.  The report has been created based on the weighted criteria provided by you or your financial representative.  The information provided is not individualized to the specific 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell column-number="4" padding-before="17px">
								<fo:block xsl:use-attribute-sets="fundEvaluator_disclaimer_text">
									needs of your plan and its participants. Keep the following in mind about the objective rankings you will see when you review the enclosed information about investment options.  The information in this document (including the list of objectively-ranked investment options) is investment data of a general application, based on selected criteria using standard and well accepted calculations and formulae recognized within the investment industry.  It does not constitute any specific investment recommendation, individualized investment advice, or legal advice.  The objective rankings are tools to assist you, but cannot replace your judgment as to whether or how the information should 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell column-number="6" padding-before="17px">
								<fo:block xsl:use-attribute-sets="fundEvaluator_disclaimer_text">
									be applied to the specific and individualized needs and characteristics of your plan and your employee, participant and beneficiary population. The information contained in these documents does not constitute legal or investment advice.  You as a plan fiduciary, must make the final decision about the investment options to make sure that the selection criteria, each investment option, and the investment lineup as a whole are appropriate for the particular needs of your company and its workforce. The preliminary investment options presented in FundEvaluator are based on objective scoring according to the selected criteria that you or your financial representative specified.  
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
		  	</fo:static-content>
			
		  	<fo:flow flow-name="xsl-region-body">
		  		<fo:block/>
		  		<fo:block space-before="42px"/>
			  	<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="143px"/>
					<fo:table-column column-width="372px"/>
					<fo:table-column column-width="46px"/>
					<fo:table-column column-width="141px"/>
					
					<fo:table-body>
						<fo:table-row height="20px">
							<fo:table-cell column-number="2">
								<fo:block xsl:use-attribute-sets="TOC_section_name_text">
									Selecting investment options for your plan – tough choices made easier
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
									<fo:page-number-citation ref-id="SelectingInvestmentOptionsId" font-weight="bold"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<xsl:if test="//section[@sectionId='GIFL']"> 
						<fo:table-row height="30px">
							<fo:table-cell column-number="2">
								<fo:block xsl:use-attribute-sets="TOC_section_name_text">
									Guaranteed Income for Life Select – more certainty for your plan participants
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
									<fo:page-number-citation ref-id="GIFLSelectInformationId" font-weight="bold"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						
						<fo:table-row height="20px">
							<fo:table-cell column-number="2">
								<fo:block xsl:use-attribute-sets="TOC_section_name_text">
									Selected measurement criteria 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
									<fo:page-number-citation ref-id="CustomMeasurementCriteriaId" font-weight="bold"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row height="20px">
							<fo:table-cell column-number="2">
								<fo:block xsl:use-attribute-sets="TOC_section_name_text">
										Asset Class overview
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
									<fo:page-number-citation ref-id="ResultsInvestmentOptionsId" font-weight="bold"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<xsl:if test="//section[@sectionId='IPST']"> 
							<fo:table-row height="20px">
								<fo:table-cell column-number="2">
									<fo:block xsl:use-attribute-sets="TOC_section_name_text">
										Documenting your due diligence with an Investment Policy Statement
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
										<fo:page-number-citation ref-id="DocumentingDueDiligenceId" font-weight="bold"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
						
						<xsl:if test="//section[@sectionId='FRSE'] or //section[@sectionId='FRAV']">
							<fo:table-row height="20px">
								<fo:table-cell column-number="2">
									<fo:block xsl:use-attribute-sets="TOC_section_name_text">
										Ranking methodology 
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
										<fo:page-number-citation ref-id="RankingMethodologyId" font-weight="bold"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
					
						
						
							<fo:table-row height="20px">
								<fo:table-cell column-number="2">
									<fo:block xsl:use-attribute-sets="TOC_section_name_text">
										Investment option rankings
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
										<fo:page-number-citation ref-id="FundRankingId" font-weight="bold"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						
						<xsl:if test="$criteria_Selected_1_3_5_10YR ='Yes'">
						<fo:table-row height="20px">
							<fo:table-cell column-number="2">
								<fo:block xsl:use-attribute-sets="TOC_section_name_text">
									Investment options rankings - supplementary return information 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
									<fo:page-number-citation ref-id="supplementaryReturnId" font-weight="bold"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						</xsl:if>
						
						<fo:table-row height="20px">
							<fo:table-cell column-number="2">
								<fo:block xsl:use-attribute-sets="TOC_section_name_text">
									Performance &amp; expenses
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
									<fo:page-number-citation ref-id="PerformanceExpensesId" font-weight="bold"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<xsl:if test="//section[@sectionId='GLOS']">
							<fo:table-row height="20px">
								<fo:table-cell column-number="2">
									<fo:block xsl:use-attribute-sets="TOC_section_name_text">
										Glossary
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
										<fo:page-number-citation ref-id="glossarySectionId" font-weight="bold"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						 </xsl:if> 
						
						<fo:table-row height="20px">
							<fo:table-cell column-number="2">
								<fo:block xsl:use-attribute-sets="TOC_section_name_text">
									Important notes 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="TOC_section_page_number_text">
									<fo:page-number-citation ref-id="importantNotesId" font-weight="bold"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
		   </fo:flow>
			   
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>