<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="SelectingInvestmentOptionsTemplate">
		<fo:page-sequence master-reference="SelectingInvestmentOptionsLayout">
			
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before" >
				<fo:block-container xsl:use-attribute-sets="header_block_cell1">
					<fo:block-container  xsl:use-attribute-sets="header_block_cell2">
						<fo:block>
						</fo:block>
					</fo:block-container>
				</fo:block-container>
				
				<fo:block-container  xsl:use-attribute-sets="header_block_cell3">
					<fo:block id="SelectingInvestmentOptionsId" padding-after="6px" start-indent="18px">
						<fo:inline font-weight="light">Selecting investment options for your plan</fo:inline>
						<fo:inline xsl:use-attribute-sets="header_block_cell3_desc"> – tough choices made easier</fo:inline>
					</fo:block>
				</fo:block-container>
			</fo:static-content>
			
			<!-- Footer -->
			<fo:static-content flow-name="xsl-region-after" >
				<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="126px"/>
					<fo:table-body>
						<fo:table-row height = "14px">
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="page_count">
									PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminator"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>

			<!-- Vertical Key line -->
			<fo:static-content flow-name="xsl-region-start" >
				<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="128px"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell text-align="right" padding-before="40px">
								<fo:block>
									<fo:external-graphic content-height="432px" content-width="5px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Verticle-rule.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>
			
			<!-- Body -->
			<fo:flow flow-name="xsl-region-body">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="290px"/>
					<fo:table-column column-width="12px"/>
					<fo:table-column column-width="273px"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell padding-left="15px">
								<!-- Word Group -->
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="Light" line-height="1" space-after="6px">
									This document is provided at your request by your Financial Representative and created by FundEvaluator.  FundEvaluator is an easy-to-use tool that provides quantitative information that can be used by the plan fiduciaries or financial representatives to help with their selecting or monitoring of the Funds offered on the John Hancock platform.  This tool is being provided without regard to the individualized needs of any plan, or any participants or beneficiaries.  John Hancock is not undertaking to provide impartial investment advice, or to give advice in a fiduciary capacity in connection with the use of FundEvaluator and any transactions that may follow the use of this tool.  Depending on the Funds selected or recommended by plan fiduciaries (and whether or not any Funds are recommended or selected), John Hancock and its affiliates may receive additional compensation from the Funds, in the form of 12b-1 fees, transfer agent fees, investment management fees, or otherwise.  The total revenue John Hancock and its affiliates receive from a fund advised or sub-advised by John Hancock’s affiliates is higher than those advised or sub-advised exclusively by unaffiliated entities.
                                </fo:block>
                               <fo:block xsl:use-attribute-sets="word_group_style" font-weight="Light" line-height="1" space-after="6px">
                                    FundEvaluator is designed to provide objective financial data, based on the weighted criteria specified by the plan fiduciaries or financial representatives.  John Hancock does not exercise any discretion or make any recommendation regarding the criteria or the application of those criteria.
								</fo:block>
								
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="Light" line-height="1" space-after="6px">
									Establishing and maintaining a 401(k) plan is an increasingly complex process. Among the most important decisions facing you as a fiduciary is the selection of the investment options that will be available to your participants.
								</fo:block>
								
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="Light" line-height="1" space-after="4px">
									Under the Employee Retirement Income Security Act (ERISA), among other responsibilities, plan fiduciaries are required to:
								</fo:block>
								
								<fo:block/>
								<fo:list-block space-after="6px" xsl:use-attribute-sets="word_group_style" font-weight="Light" line-height="1">
									<fo:list-item space-after="3px">
										<fo:list-item-label start-indent="2px">
											<fo:block>
											   &#8226;
											</fo:block>
										</fo:list-item-label>
										<fo:list-item-body start-indent="12px">
											<fo:block>
												act prudently with their employee benefit plan,
											</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									  <fo:list-item space-after="3px">
										<fo:list-item-label start-indent="2px">
											<fo:block>
											   &#8226;
											</fo:block>
										</fo:list-item-label>
										<fo:list-item-body start-indent="12px">
											<fo:block>
												diversify the plan's investments to minimize the risk of large losses,
											</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									
									  <fo:list-item space-after="3px">
										<fo:list-item-label start-indent="2px">
											<fo:block>
											   <xsl:text>&#8226;</xsl:text>
											</fo:block>
										</fo:list-item-label>
										<fo:list-item-body start-indent="12px">
											<fo:block>
												offer a broad range of investment alternatives in their plan, if they want to qualify for relief under section 404(c), and
											</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									
									<fo:list-item space-after="3px">
										<fo:list-item-label start-indent="2px">
											<fo:block>
											   <xsl:text>&#8226;</xsl:text>
											</fo:block>
										</fo:list-item-label>
										<fo:list-item-body start-indent="12px">
											<fo:block>
												monitor the plan’s investments regularly to ensure they are still appropriate for the plan.
											</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									
								</fo:list-block>	
								
								
							</fo:table-cell>
							
							<fo:table-cell><fo:block/></fo:table-cell>
							
							<fo:table-cell>
							
							
							    <fo:block xsl:use-attribute-sets="word_group_style" font-weight="Light" line-height="1" space-after="5px">
									Every organization and every workforce is unique. In selecting investment options for your 401(k) plan, some factors generally considered by plan fiduciaries include:
								</fo:block>
								
								<fo:block>
									<fo:list-block space-after="6px" xsl:use-attribute-sets="word_group_style" line-height="1">
										
										<fo:list-item>
											<fo:list-item-label start-indent="2px">
												<fo:block>
												   <xsl:text>&#8226;</xsl:text>
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="12px">
												<fo:block>
													employee demographics, including employee investment needs,
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
									
										<fo:list-item space-after="3px">
											<fo:list-item-label start-indent="2px">
												<fo:block>&#8226;</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="12px">
												<fo:block>
													the type and number of investment options to be offered,
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
										<fo:list-item space-after="3px">
											<fo:list-item-label start-indent="2px">
												<fo:block>
												   <xsl:text>&#8226;</xsl:text>
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="12px">
												<fo:block>
													the level and type of customer support offered by the investment provider,
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
										<fo:list-item space-after="3px">
											<fo:list-item-label start-indent="2px">
												<fo:block>
												   <xsl:text>&#8226;</xsl:text>
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="12px">
												<fo:block>
													the availability of participant investment education,
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
										<fo:list-item space-after="3px">
											<fo:list-item-label start-indent="2px">
												<fo:block>
												   <xsl:text>&#8226;</xsl:text>
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="12px">
												<fo:block>
													investment costs, and
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
										<fo:list-item>
											<fo:list-item-label start-indent="2px">
												<fo:block>
												   <xsl:text>&#8226;</xsl:text>
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="12px">
												<fo:block>
													potential risks and returns of each option.
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
									</fo:list-block>
								</fo:block>
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="Light" line-height="1" space-after="10px">
									Your company is unique and each of your employees has unique retirement goals, financial circumstances, investment knowledge and comfort with risk. By offering a variety of investment options in your plan, plan fiduciaries may better meet the needs of their workforce, their plan and each of their plan participants.
								</fo:block>
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="Light" line-height="1" space-after="10px">
									John Hancock makes available a platform of separate sub-accounts (“Funds” or “investment options”) for selection by fiduciaries of qualified retirement plans, including participant directed plans.  These sub-accounts provide a diversified line-up of investment options, covering a wide-range of asset classes and investment styles.  The platform of investments is made available without regard to the individualized needs of any plan.  In making the platform available, John Hancock is not undertaking to provide impartial investment advice in a fiduciary capacity.
								</fo:block>
								<fo:block xsl:use-attribute-sets="managing_managers_word_group_style" font-size="9pt" line-height="1">
								    From this pool of sub-accounts, your financial representative has used FundEvaluator  to identify a set of investment options based on the weighted criteria** selected by you or your financial representative.  This report is an objective ranking of available Funds based on these weighted criteria and information available as of <xsl:value-of select="//customization/asOfDateMET"/>.  The criteria and weightings used in ranking investment options are shown on the following pages.
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row height="105px" display-align="after">
							<fo:table-cell number-columns-spanned="3" padding-left="15px">
								<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" space-after="3px">
									John Hancock Life Insurance Company (U.S.A.) and John Hancock Life Insurance Company of New York are collectively referred to as "John Hancock".
								</fo:block>
								<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" space-after="3px">
									* Also referred to as “Funds” – these are insurance company (i) subaccounts investing in underlying mutual funds collective investment trusts or ETFs (also referred to as “underlying funds” or “funds”), or (ii) guaranteed interest accounts.
								</fo:block>
								<fo:block xsl:use-attribute-sets="FSW_disclaimer_style">
									** The criteria represent traditional objective investment measurements which are mathematically calculated based on generally accepted and widely used formulae within the investment industry. These criteria may be used by plan fiduciaries or financial representatives as information tools to help them tailor investment options suitable to the particular needs and characteristics of their plan, its participants, and employee workforce. The criteria are applied in part to data from Morningstar® which is available to the public in its raw state, is not organized in categories that are highly selective, and is not organized or presented to you in a manner which suggests the purchase, holding, or sale of any investment option.
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:flow>		
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>