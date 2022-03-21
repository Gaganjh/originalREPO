<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<xsl:template name="ExecutiveSummaryTemplate">
		
		<fo:page-sequence master-reference="CommonPageLayout">
			
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="115px" />
					<fo:table-column column-width="459px" />
					<fo:table-column column-width="18px" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell height = "18px" background-color="#C4C7AB">
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
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="115px" />
					<fo:table-column column-width="444px" />
					<fo:table-column column-width="33px" />
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
								<fo:block xsl:use-attribute-sets="page_count" start-indent="13px">
									PAGE
									<fo:page-number />
									OF
									<fo:page-number-citation-last
										ref-id="terminator" />
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
				<fo:table table-layout="fixed" width="100%" >
					<fo:table-column column-width="444px" />
					<fo:table-column column-width="3px" />
					<fo:table-column column-width="30px" />
					<fo:table-body >
						<fo:table-row>
							<fo:table-cell number-columns-spanned="2">
								<fo:block xsl:use-attribute-sets="header_block_cell3" padding-before="20px" text-align="right">
									Executive summary of findings
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
									<xsl:choose>
										<xsl:when test="annual_review_report/isInterim = 'true'">
											<fo:block padding-before="42px" start-indent="13px" xsl:use-attribute-sets="word_group_font">
												The IPS Manager service has been designed to perform a review of the investment options you have selected for your plan using the service. This review is based on a quantitative analysis, completed using your review criteria, to help you determine if the Funds in your lineup are still among the top-ranked choices for your plan.
											</fo:block>
				
											<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font">
												This report includes the results of your latest IPS Manager review, detailing the results of your plan’s review. 
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block padding-before="42px" start-indent="13px" xsl:use-attribute-sets="word_group_font"> 
												The IPS Manager service has been designed to perform an annual review of the investment options you have selected for your plan using the service. This review is based on a quantitative analysis, completed using your review criteria, to help you determine if the Funds in your lineup are still among the top-ranked choices for your plan.
											</fo:block>
				
											<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font">
												This report includes the results of your latest IPS Manager review. It is designed to help you with the ongoing monitoring and management of your plan’s investment options, and identifies Funds which had consistently held below median rankings during the annual review period, based on your current IPS criteria.
											</fo:block>
										</xsl:otherwise>
									</xsl:choose>
									
									<fo:block padding-before="14px" xsl:use-attribute-sets="sub_header_block" start-indent="13px">
									Your Plan’s Investments
									</fo:block>
									<xsl:choose>
										<xsl:when test="annual_review_report/isInterim = 'true'">
											<xsl:choose>
												<xsl:when test="annual_review_report/replacableFundsInd = 'true'">
													<fo:block padding-before="12px" start-indent="13px" padding-right="53px" xsl:use-attribute-sets="word_group_font">
														As of <xsl:value-of select="annual_review_report/currentDate"/>, there are Funds in your investment lineup that have been flagged because of their ranking scores. 
														See Section B for full details.
													</fo:block>
												</xsl:when>
												<xsl:when test="annual_review_report/allFundsAreInGoodStanding = 'true'">
													<fo:block padding-before="12px" start-indent="13px" padding-right="53px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
														<fo:inline font-weight="normal">As of <xsl:value-of select="annual_review_report/currentDate"/>, </fo:inline>no <fo:inline font-weight="normal">Funds in your investment lineup have been flagged because of their ranking scores.</fo:inline>
													</fo:block>
												</xsl:when>
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<xsl:choose>
												<xsl:when test="annual_review_report/replacableFundsInd = 'true'">
													<fo:block padding-before="12px" start-indent="13px" padding-right="53px" xsl:use-attribute-sets="word_group_font">
														As of <xsl:value-of select="annual_review_report/processingDate"/>, there are Funds in your investment lineup that have been flagged because of their ranking scores. 
														See Section B for full details.
													</fo:block>
												</xsl:when>
												<xsl:when test="annual_review_report/allFundsAreInGoodStanding = 'true'">
													<fo:block padding-before="12px" start-indent="13px" padding-right="53px" line-height="1.5" xsl:use-attribute-sets="word_group_font" font-weight="bold">
														<fo:inline font-weight="normal">As of <xsl:value-of select="annual_review_report/processingDate"/>, there are </fo:inline>no <fo:inline font-weight="normal">Funds in your investment lineup that have been flagged because of their ranking scores.</fo:inline>
													</fo:block>
												</xsl:when>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
									
									
									<fo:block padding-before="16px" xsl:use-attribute-sets="sub_header_block" start-indent="13px" >
									This report includes:
									</fo:block>
									
									<fo:block start-indent="4px" padding-before="3px">
										<fo:table>
											<fo:table-column column-width="414px" />
											<fo:table-body >
												<fo:table-row>
													<fo:table-cell  height="86px" width="426px" background-color="#E9E9DE">
														<fo:block padding-before="8px" xsl:use-attribute-sets="contents" start-indent="9px">
														A. Your IPS Manager criteria ....
														Page <fo:page-number-citation ref-id="IPSManagerReviewCriteriaId"/>
														</fo:block>
														<xsl:choose>
															<xsl:when test="annual_review_report/isInterim = 'true'">
																<fo:block padding-before="8px" xsl:use-attribute-sets="contents" start-indent="9px">
																	B. Your complete review results .... 
																	Page <fo:page-number-citation ref-id="AnnualReviewResultsId"/>
																</fo:block>
															</xsl:when>
															<xsl:otherwise>
																<fo:block padding-before="8px" xsl:use-attribute-sets="contents" start-indent="9px">
																	B. Your complete annual review results .... 
																	Page <fo:page-number-citation ref-id="AnnualReviewResultsId"/>
																</fo:block>
															</xsl:otherwise>
														</xsl:choose>
														
														<xsl:choose>
															<xsl:when test="annual_review_report/ipsFundInstructionInfo/ips_fund_instruction_info">
																<fo:block padding-before="8px" xsl:use-attribute-sets="contents" start-indent="9px">
																	C. Summary .... 
																	Page <fo:page-number-citation ref-id="SummaryId"/>
																</fo:block>
																<xsl:choose>
																	<xsl:when test="annual_review_report/nextStepSectionAvailable = 'true'">
																		<fo:block padding-before="8px" xsl:use-attribute-sets="contents" start-indent="9px">
																			D. If you want to make a change  ....
																			Page <fo:page-number-citation ref-id="NextStepsId" />
																		</fo:block>
																		<fo:block padding-before="8px" xsl:use-attribute-sets="contents" padding-after="8px" start-indent="9px">
																			E. Important Notes .... 
																			Page <fo:page-number-citation ref-id="ImportantNotesId" />
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:block padding-before="8px" xsl:use-attribute-sets="contents" padding-after="8px" start-indent="9px">
																			D. Important Notes .... 
																			Page <fo:page-number-citation ref-id="ImportantNotesId" />
																		</fo:block>
																	</xsl:otherwise>
																</xsl:choose>
																
															</xsl:when>
															<xsl:otherwise>
																<fo:block padding-before="8px" xsl:use-attribute-sets="contents" padding-after="8px" start-indent="9px">
																	C. Important Notes .... 
																	Page <fo:page-number-citation ref-id="ImportantNotesId"/>
																</fo:block >
															</xsl:otherwise>
														</xsl:choose>
														
													</fo:table-cell>	
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
									<fo:block-container padding-before="16px" xsl:use-attribute-sets="word_group_font">
										<xsl:choose>
											<xsl:when test="annual_review_report/isInterim = 'true'">
												<fo:block start-indent="13px" font-weight="bold">
													<fo:inline font-weight="normal">To learn more about IPS Manager, refer to the How it Works Guide, available on the IPS Manager page of the </fo:inline> 
													<fo:basic-link color="blue">
														<xsl:attribute name="external-destination">url('<xsl:value-of select="annual_review_report/linkToPSWIPSManagerLandingPage"/>')</xsl:attribute>Plan Sponsor Website</fo:basic-link>.
												</fo:block>
											</xsl:when>
											<xsl:otherwise>
												<fo:block start-indent="13px" font-weight="bold">
													<fo:inline font-weight="normal">To learn more about IPS Manager, refer to the How it Works Guide, available on the IPS Manager page of the </fo:inline>
													<fo:basic-link color="blue" >
														<xsl:attribute name="external-destination">url('<xsl:value-of select="annual_review_report/linkToPSWIPSManagerLandingPage"/>')</xsl:attribute>Plan Sponsor Website</fo:basic-link>.
												</fo:block>
											</xsl:otherwise>
										</xsl:choose>
										
									</fo:block-container>
									<xsl:choose>
										<xsl:when test="annual_review_report/isInterim = 'true'">
											<fo:block start-indent="13px" xsl:use-attribute-sets="disclaimer" padding-before="15px">
												The IPS Manager service is provided without regard to the individualized needs of your Plan, or any of the Plan’s participants or beneficiaries.  John Hancock is not undertaking to provide impartial investment advice, or to give advice in a fiduciary capacity in connection with the use of this service and any transactions that may follow the use of this service.  Depending on the Funds selected or recommended by the Plan fiduciaries (and whether or not any Funds are recommended or selected), John Hancock and its affiliates may receive additional compensation from the Funds, in the form of 12b-1 fees, transfer agent fees, investment management fees, or otherwise.  The total revenue John Hancock and its affiliates receive from a fund advised or sub-advised by John Hancock’s affiliates is higher than those advised or sub-advised exclusively by unaffiliated entities.  
											</fo:block>
											<fo:block start-indent="13px" xsl:use-attribute-sets="disclaimer" padding-before="5px">
												For purposes of the IPS Manager service, the terms “monitor”, “monitored”, “analyze”, “analyzed”, “analysis”, or “review” simply refer to the periodic application of the IPS Manager tool to conduct a calculation that results in a quantitative report. 
											</fo:block>
											<fo:block start-indent="13px" xsl:use-attribute-sets="disclaimer" padding-before="5px">
												This service (including, without limitation, the objective rankings of the Funds, the “watch lists”, “warning lists”) and other information or data contained in the online summary and detailed reports provided as part of the review process are tools to assist you and cannot replace your judgment or determination as to whether or how they should be applied to the specific and individualized needs and characteristics of your plan and your employee, participant, and beneficiary population.  As a Responsible Plan Fiduciary, the Trustee and/or designated 3(38) Investment Manager are ultimately responsible as a fiduciary for selecting and monitoring the investment options on behalf of the Plan, including determining if the selected IPS Manager service settings and the investment lineup made available by John Hancock are appropriate for the Plan, its participants and beneficiaries, or determining whether or how to use the reports (including the “warning lists” and “watch lists”) provided by the service.
											</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<fo:block start-indent="13px" xsl:use-attribute-sets="disclaimer" padding-before="15px">
												The IPS Manager service is provided without regard to the individualized needs of your Plan, or any of the Plan’s participants or beneficiaries.  John Hancock is not undertaking to provide impartial investment advice, or to give advice in a fiduciary capacity in connection with the use of this service and any transactions that may follow the use of this service.  Depending on the Funds selected or recommended by the Plan fiduciaries (and whether or not any Funds are recommended or selected), John Hancock and its affiliates may receive additional compensation from the Funds, in the form of 12b-1 fees, transfer agent fees, investment management fees, or otherwise.  The total revenue John Hancock and its affiliates receive from a fund advised or sub-advised by John Hancock’s affiliates is higher than those advised or sub-advised exclusively by unaffiliated entities.
											</fo:block>
											<fo:block start-indent="13px" xsl:use-attribute-sets="disclaimer" padding-before="5px">
												For purposes of the IPS Manager service, the terms “monitor”, “monitored”, “analyze”, “analyzed”, “analysis”, or “review” simply refer to the periodic application of the IPS Manager tool to conduct a calculation that results in a quantitative report.  
											</fo:block>
											<fo:block start-indent="13px" xsl:use-attribute-sets="disclaimer" padding-before="5px">
												This service (including, without limitation, the objective rankings of the Funds, the “watch lists”, “warning lists”) and other information or data contained in the online summary and detailed reports provided as part of the review process are tools to assist you and cannot replace your judgment or determination as to whether or how they should be applied to the specific and individualized needs and characteristics of your plan and your employee, participant, and beneficiary population.  As a Responsible Plan Fiduciary, the Trustee and/or designated 3(38) Investment Manager are ultimately responsible as a fiduciary for selecting and monitoring the investment options on behalf of the Plan, including determining if the selected IPS Manager service settings and the investment lineup made available by John Hancock are appropriate for the Plan, its participants and beneficiaries, or determining whether or how to use the reports (including the “warning lists” and “watch lists”) provided by the service.
											</fo:block>
										</xsl:otherwise>
									</xsl:choose>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="2">
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
	
</xsl:stylesheet>