<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="NextStepsSection">
	
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
								<fo:block id="NextStepsId" xsl:use-attribute-sets="header_block_cell3" text-align="right" padding-before="20px" >
									If you want to make a change
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
						<xsl:choose>
							<xsl:when test="annual_review_report/isInterim = 'true'">
								<fo:table-row>
									<fo:table-cell>
										<xsl:choose>
											<xsl:when test="annual_review_report/ipsTableAvailable = 'true'">
												<!-- <fo:block padding-before="42px"  start-indent="13px" xsl:use-attribute-sets="word_group_font">
													Based on your review results, if you would like to replace a Fund that has received a red flag with the indicated highest-ranked Fund from the <fo:inline xsl:use-attribute-sets="italic">Ranking Analysis Results</fo:inline> chart in this report, you can:
												</fo:block>	 -->
												<fo:block padding-before="42px" start-indent="13px" xsl:use-attribute-sets="italic">
													<fo:inline xsl:use-attribute-sets="word_group_font">Based on your selected quantitative criteria, if you would like to replace a Fund that has been identified with a red flag in the review results with the highest-ranked Fund in the same asset category, you can:</fo:inline>
												</fo:block>
												<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font">
													<fo:table>
														<fo:table-column column-width="4px"/>
														<fo:table-column column-width="440px"/>
														<fo:table-body>
															<fo:table-row>
																<fo:table-cell>
																	<fo:block xsl:use-attribute-sets="word_group_font">
																		•
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell>
																	<fo:block start-indent="18px" xsl:use-attribute-sets="word_group_font" >
																		Approve and submit the Fund changes using the attached form.
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															</fo:table-body>
														</fo:table>
														</fo:block>
											
														<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="sub_header_block">
															Additionally, there are some important things to keep in mind: 
														</fo:block>
														<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font">
														<fo:table>
															<fo:table-column column-width="4px"/>
															<fo:table-column column-width="440px"/>
															<fo:table-body>
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block xsl:use-attribute-sets="word_group_font">
																			•
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																		<fo:block start-indent="18px" xsl:use-attribute-sets="word_group_font">
																			If you choose to replace a Fund with a different Fund than the highest-ranked Fund in the same asset category, this transaction can be requested at any time by completing a Contract Investment Administration Form.
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block xsl:use-attribute-sets="word_group_font">
																			•
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																		<fo:block start-indent="18px" xsl:use-attribute-sets="word_group_font">
																			It is your responsibility to determine the participants and beneficiaries in your Plan who should receive notification of any Fund change. The notification needs to be forwarded to and received by them at least 30 days prior to the effective date of the requested changes and to ensure the information in the notification meets any notification and disclosure requirements that your Plan may be subject to in connection with the Fund changes 
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block xsl:use-attribute-sets="word_group_font">
																			•
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																		<fo:block start-indent="18px" xsl:use-attribute-sets="word_group_font">
																			A Sample Participant Notification template can be found on the<fo:inline font-weight="bold"> IPS Manager </fo:inline>page of the Plan Sponsor website. 
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</fo:table-body>
														</fo:table>
													</fo:block>
												</xsl:when>
												<xsl:otherwise>
													<fo:block></fo:block>
												</xsl:otherwise>
											</xsl:choose>
									</fo:table-cell>
									<fo:table-cell number-columns-spanned="2">
										<fo:block></fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:when>
							<xsl:otherwise>
								<fo:table-row>
									<fo:table-cell>
											<xsl:choose>
												<xsl:when test="annual_review_report/ipsTableAvailable = 'true'">
										
													<fo:block padding-before="42px" start-indent="13px" xsl:use-attribute-sets="italic">
														<fo:inline xsl:use-attribute-sets="word_group_font">Based on your selected quantitative criteria, if you would like to replace a Fund that has been identified with a red flag in the annual review results with the highest-ranked Fund in the same asset category, you can:</fo:inline>
													</fo:block>	
													<fo:block padding-before="13px" start-indent="13px" xsl:use-attribute-sets="word_group_font">
														<fo:table>
															<fo:table-column column-width="4px"/>
															<fo:table-column column-width="440px"/>
															<fo:table-body>
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block xsl:use-attribute-sets="word_group_font">
																			•
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																		<fo:block start-indent="18px" xsl:use-attribute-sets="word_group_font" font-weight="bold">
																			<fo:inline font-weight="normal">Approve and submit the Fund changes online* via the</fo:inline> 
																			<fo:basic-link color="blue">
											 									<xsl:attribute name="external-destination">url('<xsl:value-of select="annual_review_report/linkToPSWIPSManagerLandingPage"/>')</xsl:attribute> IPS Manager 
											 								</fo:basic-link><fo:inline font-weight="normal"> page on the Plan Sponsor website until 
											 								<xsl:value-of select="annual_review_report/expiryDate"/>.</fo:inline>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block xsl:use-attribute-sets="word_group_font">
																			•
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																		<fo:block start-indent="18px" xsl:use-attribute-sets="word_group_font">
																			Access participant notification tools via the Plan Sponsor website to assist you with communicating^ the upcoming changes.
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block xsl:use-attribute-sets="word_group_font">
																			•
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																		<fo:block start-indent="18px" xsl:use-attribute-sets="word_group_font">
																			After <xsl:value-of select="annual_review_report/expiryDate"/>, Fund changes can be submitted by completing a Contract Administration Form. The participant notification tool is not available for Fund changes submitted via this method.
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</fo:table-body>
														</fo:table>
														</fo:block>
														<fo:block-container absolute-position="fixed" top="645px" left="146px" right="33px" xsl:use-attribute-sets="disclaimer">
															<fo:block>
																*If you choose to replace a Fund with a different Fund than the highest-ranked Fund in the same asset category, this transaction can be requested at any time by completing a Contract Administration Form. The participant notification tool is not available for Fund changes submitted via this method.
															</fo:block>
															<fo:block padding-before="5px">
																^It is your responsibility to determine the participants and beneficiaries in your Plan who should receive notification of any Fund change. The notification needs to be forwarded to and received by them at least 30 days prior to the effective date of the requested changes and to ensure the information in the notification meets any notification and disclosure requirements that your Plan may be subject to in connection with the Fund changes.
															</fo:block>
														</fo:block-container>
												</xsl:when>
												<xsl:otherwise>
													<fo:block></fo:block>
												</xsl:otherwise>
											</xsl:choose>
									</fo:table-cell>
									<fo:table-cell number-columns-spanned="2">
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