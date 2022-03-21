<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="InvestmentPolicyStatementTemplate">
		<fo:page-sequence master-reference="InvestmentPolicyStatementLayout">
		
			<fo:static-content flow-name="xsl-region-after">
				<fo:retrieve-marker retrieve-class-name="FooterSection" retrieve-position="first-starting-within-page" retrieve-boundary="page"/>
			</fo:static-content>
			
			<fo:flow flow-name="xsl-region-body">
				<fo:block xsl:use-attribute-sets="form_title" space-after="43px" span="all">
					Investment Policy Statement
				</fo:block>
				<fo:block span="all"/>
				
				<fo:block xsl:use-attribute-sets="section_title" space-after="6px">
					Purpose
				</fo:block>
				
				<fo:block xsl:use-attribute-sets="IPS_word_group" space-after="13px">
					The purpose of the <xsl:value-of select="$companyName"/> plan is to help employees build a financially secure future by providing a source of retirement income. The purpose of this Investment Policy Statement is to establish investment principles, document the plan’s investment objectives, and create performance guidelines for evaluating investment decisions.
				</fo:block>
				
				<fo:block xsl:use-attribute-sets="IPS_word_group" space-after="13px">
					It is the intent of the plan to satisfy the requirements under ERISA Section 404(c) and the Department of Labor (“DOL”) regulations there under, which limit the liability of plan fiduciaries for investment losses resulting solely from participant direction.
				</fo:block>
				
				<fo:block xsl:use-attribute-sets="IPS_word_group" space-after="13px" line-height="1">
					<fo:list-block>
						<fo:list-item>
							<fo:list-item-label>
								<fo:block>
									<fo:external-graphic>
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body start-indent="20px">
								<fo:block>
									If your plan does not intend to satisfy the ERISA Section 404(c) requirements, check this box.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
					</fo:list-block>
				</fo:block>
				
				<fo:block xsl:use-attribute-sets="section_title" space-after="9px">
					I.&#160;&#160;&#160;&#160;&#160;Statement of investment objectives
				</fo:block>
				
				<fo:block xsl:use-attribute-sets="IPS_word_group" space-after="19px">
					<fo:list-block>
						<fo:list-item space-after="15px">
							<fo:list-item-label>
								<fo:block xsl:use-attribute-sets="section_title">
									A.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body start-indent="20px">
								<fo:block>
									To provide a broad range of designated investment alternatives (at least three) with varying investment characteristics and degrees of risk and to provide plan participants with the opportunity to:
									<fo:list-block>
										<fo:list-item space-after="6px" space-before="6px">
											<fo:list-item-label>
												<fo:block xsl:use-attribute-sets="section_title">
													1.
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="35px">
												<fo:block>
													Materially affect the potential return on the amounts in their individual accounts
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
										<fo:list-item space-after="6px">
											<fo:list-item-label>
												<fo:block xsl:use-attribute-sets="section_title">
													2.
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="35px">
												<fo:block>
													Control the degree of risk to which such amounts are subject
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
										<fo:list-item space-after="6px">
											<fo:list-item-label>
												<fo:block xsl:use-attribute-sets="section_title">
													3.
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="35px">
												<fo:block>
													Achieve, overall, a portfolio with aggregate risk and return characteristics within the range appropriate for each participant
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
									</fo:list-block>
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item>
							<fo:list-item-label>
								<fo:block xsl:use-attribute-sets="section_title">
									   B.
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body start-indent="20px">
								<fo:block>
									To prudently select and monitor investments and investment providers and to incur only expenses that are reasonable based on the quality of the services provided and the nature and extent of the services rendered.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
					</fo:list-block>
				</fo:block>
				
				<fo:block xsl:use-attribute-sets="section_title" space-after="6px">
					II.&#160;Investment Selection
				</fo:block>
				
				<fo:block xsl:use-attribute-sets="IPS_word_group" space-after="15px">
					Having considered alternatives, the responsible plan fiduciary (“RPF”) (the company or its designated committee) has selected a group annuity contract with John Hancock as the principal plan provider to meet the purpose, objective and criteria set forth in this Investment Policy Statement and accompanying documentation.
				</fo:block>
				
				<fo:block xsl:use-attribute-sets="IPS_word_group" space-after="15px">
					 The initial selection of investment options for this Plan may be based on the analysis of, and the weight given to, the criteria listed below to the extent selected for use by the plan. These criteria are guidelines only and will be applied with discretion. Additional items such as plan demographics and level of participant investment sophistication, among others, will also need to be considered.
				</fo:block>
				
				<fo:block xsl:use-attribute-sets="IPS_word_group" space-after="25px">
					Assets in the group annuity contract will be placed in investment options managed by professional managers. Investment options in a variety of risk categories and asset classes will be made available. Participants will have the opportunity to choose among these designated investment alternatives to create an individual investment mix designed to meet their own retirement objectives.
				</fo:block>
				
				<!-- Criterion table -->
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="135px"/>
					<fo:table-column column-width="81px"/>
					<fo:table-column column-width="39px"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
								<fo:block xsl:use-attribute-sets="measurement_column_headers">
									Criterion
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
								<fo:block xsl:use-attribute-sets="measurement_column_headers">
									Measurement
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
								<fo:block xsl:use-attribute-sets="measurement_column_headers">
									Weight
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<xsl:for-each select="criteriaSelections/criterions/criterion/criteria">
							<fo:table-row>
								<xsl:choose>
									<xsl:when test="@shortName = '3 Year Return'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												3-year performance
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												3 Year Return
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									<xsl:when test="@shortName = '5 Year Return'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												5-year performance
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												5 Year Return
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									<xsl:when test="@shortName = '10 Year Return'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												10-year performance
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												10 Year Return
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									
									<xsl:when test="@shortName = 'Alpha'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group" padding-end="2px">
												Actual vs. expected performance
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Alpha
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									
									<xsl:when test="@shortName = 'Sharpe Ratio'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group" padding-end="2px">
												Risk-adjusted performance (using T-bill return)
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Sharpe Ratio
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									
									<xsl:when test="@shortName = 'Information Ratio'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group" padding-end="2px">
												Risk-adjusted performance (using benchmark return)
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Information Ratio
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									
									<xsl:when test="@shortName = 'R-Squared'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group" padding-end="2px">
												Correspondence to stated investment style
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												R-Squared
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									
									<xsl:when test="@shortName = 'Upside Capture'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style" padding-end="3px">
											<fo:block xsl:use-attribute-sets="IPS_word_group" padding-end="2px">
												Performance in up markets relative to the benchmark
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Upside Capture
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									
									<xsl:when test="@shortName = 'Downside Capture'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group" padding-end="2px">
												Performance in down markets relative to the benchmark
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Downside Capture
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									
									<xsl:when test="@shortName = 'Standard Deviation'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Portfolio risk
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Standard Deviation
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									
									<xsl:when test="@shortName = 'Beta'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Systematic risk
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Beta
											</fo:block>
										</fo:table-cell>
									</xsl:when>
									
									<xsl:when test="@shortName = 'Expense Ratio'">
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Fund level fees
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
											<fo:block xsl:use-attribute-sets="IPS_word_group">
												Expense Ratio
											</fo:block>
										</fo:table-cell>
									</xsl:when>
								</xsl:choose>
								
								<fo:table-cell xsl:use-attribute-sets="measurement_table_style">
									<fo:block xsl:use-attribute-sets="IPS_word_group" text-align="right">
										<xsl:value-of select="@weight"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</fo:table-body>
				</fo:table>
				
				
				<fo:block xsl:use-attribute-sets="section_title" space-after="11px"  keep-with-next="always">
					III.&#160;Ongoing Monitoring of Investment Options
				</fo:block>
				
				<fo:block xsl:use-attribute-sets="IPS_word_group" space-after="15px">
					In order to maintain continued compliance with the objectives of this Investment Policy Statement, periodic reviews of the investment options will be conducted by RPF.
				</fo:block>
				
				<fo:list-block keep-with-previous="always"  xsl:use-attribute-sets="IPS_word_group">
					<fo:list-item space-after="9px">
						<fo:list-item-label>
							<fo:block xsl:use-attribute-sets="section_title">
								&#8226;
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="20px">
							<fo:block>
								The RPF will determine what information will be used to review the investment options available to the plan, what information will be retained and how it will be retained. This may include a review of a number of different quantitative metrics, such as long term return and risk-adjusted return relative to an appropriate benchmark, as well as criteria such as portfolio composition.
							</fo:block>							
						</fo:list-item-body>
					</fo:list-item>
					<fo:list-item space-after="9px">
						<fo:list-item-label>
							<fo:block xsl:use-attribute-sets="section_title">
								&#8226;
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="20px">
							<fo:block>
								Performance reviews and other applications or materials available through John Hancock may be used to assist in the review process and may become a part of the records maintained regarding the selection and monitoring process. These may include, but are not limited to, FundCheck and FundEvaluator.
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
					<fo:list-item>
						<fo:list-item-label>
							<fo:block xsl:use-attribute-sets="section_title">
								&#8226;
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="20px">
							<fo:block>
								RPF will also evaluate the available investment options relative to changing plan demographics, and such other factors determined to be appropriate by the RPF from time to time.
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
				
				
				<xsl:if test="//reportLayout/sections/section[@sectionId='DIOS']">
					<fo:block xsl:use-attribute-sets="section_title" space-before="25px" space-after="9px" keep-with-previous ="always">
						IV.&#160;Selection of the Plan’s Default Investment Option:
					</fo:block>
					
					<fo:block xsl:use-attribute-sets="IPS_word_group" space-after="15px" break-after="column">
						In selecting the plan’s default investment option, the RPF may decide to select an investment option or suite of investment options that qualify as a Qualified Default Investment Alternative. Upon consideration of the investment options available and the needs of the plan participants, selection of the plan’s default investment option is indicated below. Any additional factors taken into account by the RPF in making this selection are described and appended to this document.
					</fo:block>
					
					<fo:block>
						<fo:list-block keep-with-previous="always" xsl:use-attribute-sets="IPS_word_group">
							<fo:list-item space-after="9px" keep-with-next="always">
								<fo:list-item-label>
									<fo:block xsl:use-attribute-sets="section_title">
										(A)
									</fo:block>
								</fo:list-item-label>
								<fo:list-item-body>
								<fo:block start-indent="20px" space-after="6px">
									One of the Target Date suites so each default enrolled participant whose date of birth information is available can be placed into a portfolio that corresponds to, or is closest to, the year in which the participant attains age 67. Each portfolio will contain a mix of equity and fixed income.
								</fo:block>
								<fo:table table-layout="fixed" width="100%">
									<fo:table-column column-width="80%"/>
									<fo:table-column column-width="20%"/>
									<fo:table-body>
											<xsl:for-each select="reportLayout/fundSuiteName/lifecycleFundSuits/lifecycleFundSuiteName">
												<fo:table-row>
													<fo:table-cell padding-start="20px">
														<fo:block space-after="6px">
														  <xsl:value-of select="@fundSuite"/>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell display-align="after" text-align="center">
														<fo:block>
															<fo:external-graphic>
																<xsl:attribute name="src">
																	<xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
																</xsl:attribute>
															</fo:external-graphic>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</xsl:for-each>
									</fo:table-body>
								</fo:table>
							</fo:list-item-body>
							</fo:list-item>
							<fo:list-item space-after="9px">
								<fo:list-item-label>
									<fo:block xsl:use-attribute-sets="section_title">
										(B)
									</fo:block>
								</fo:list-item-label>
								<fo:list-item-body>
								
									<fo:block start-indent="20px" space-after="6px">
										A Lifestyle portfolio.
									</fo:block>
									<fo:block  space-after="6px" start-indent="20px">
									<xsl:for-each select="reportLayout/fundSuiteName/lifestyleFundSuits/lifestylefundSuiteName">
										<fo:block  space-after="6px" >
											<xsl:value-of select="@fundSuite"/>
										</fo:block>
										<fo:table table-layout="fixed" width="100%" >
										<fo:table-column column-width="80%"/>
										<fo:table-column column-width="20%"/>
											<fo:table-body>
												<xsl:for-each select="lifestyleFundNameGroup/lifestylefundName">
													<fo:table-row>
	                                                      <fo:table-cell >
															<fo:block start-indent="10px" keep-with-previous="always">
	                                                        	<xsl:value-of select="@fundName"/>
														 	</fo:block>
														 </fo:table-cell>
														 <fo:table-cell display-align="after" text-align="start">
															<fo:block>
																<fo:external-graphic>
																<xsl:attribute name="src">
																	<xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
																</xsl:attribute>
															</fo:external-graphic>
															</fo:block>
														 </fo:table-cell>
	                                         			</fo:table-row>
                                         			</xsl:for-each>
                                         	</fo:table-body>
                                         </fo:table>               
									</xsl:for-each>
									</fo:block>		
									</fo:list-item-body>
							</fo:list-item>
							<fo:list-item space-after="9px" keep-with-previous="always">
								<fo:list-item-label>
									<fo:block xsl:use-attribute-sets="section_title">
										(C)
									</fo:block>
								</fo:list-item-label>
								<fo:list-item-body>
									<fo:table table-layout="fixed" width="100%">
										<fo:table-column column-width="15%"/>
										<fo:table-column column-width="85%"/>	
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell padding-start="20px" number-columns-spanned="2">
													<fo:block>
														Another investment option available to the plan:
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell padding-start="20px" padding-before="10px">
													<fo:block>
														<fo:external-graphic>
															<xsl:attribute name="src">
																<xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
															</xsl:attribute>
														</fo:external-graphic>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-before="10px">
													<fo:block border-bottom-style="solid" border-bottom-width="0.5px">&#160;</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:list-item-body>
							</fo:list-item>
						</fo:list-block>
					<!-- 
						<fo:block>
							<fo:external-graphic content-width="260px" content-height="12px">
								<xsl:attribute name="src">
									<xsl:value-of select="concat($imagePath,'Pg7-Horizontal-rule.jpg')"/>
								</xsl:attribute>
							</fo:external-graphic>
						</fo:block> -->
					</fo:block>
				</xsl:if>
				
				<fo:block>
					<fo:marker marker-class-name="FooterSection">
						<fo:block xsl:use-attribute-sets="IPS_word_group measurement_table_style" padding-after="1px">
							Date<fo:leader leader-pattern="space" leader-length="250px"/>Signature
						</fo:block>
						<fo:block xsl:use-attribute-sets="IPS_word_group" text-align="right" font-size="8pt" space-after="10px">
							(Of the responsible plan fiduciary)<fo:leader leader-pattern="space" leader-length="50px"/>
						</fo:block>
						<fo:block xsl:use-attribute-sets="IPS_word_group">
							<fo:external-graphic>
								<xsl:attribute name="src">
									<xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
								</xsl:attribute>
							</fo:external-graphic>
							&#160;&#160;Check this box if this IPS includes additional pages expanding on existing sections or adding new sections.
						</fo:block>
					</fo:marker>
				</fo:block>
			</fo:flow>
			
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>