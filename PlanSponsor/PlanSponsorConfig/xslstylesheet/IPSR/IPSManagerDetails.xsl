<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:attribute-set name="table-borderstyle">
		<!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
	</xsl:attribute-set>

	<xsl:attribute-set name="solid.blue.border">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="solid.blue.border.3sides">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="solid.blue.border.last">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.8mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="arial_font.size_10">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="verdana_font.size_10">
		<xsl:attribute name="font-family">Verdana</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="fonts_group2.size_12">
		<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
		<xsl:attribute name="font-size">12pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:variable name="fundInfoIcon">
		<xsl:value-of select="ips_review_report/fundInfoIconPath"/>
	</xsl:variable>
	
	<xsl:include href="pageDefinition.xsl" />
	<xsl:include href="reportCommonFooter.xsl" />
	<xsl:include href="reportCommonHeader.xsl" />

	<xsl:template match="ips_review_report">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- 8.5x11 page.-->
			<xsl:call-template name="pageDefinition" />
			<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after">
					<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
						Page
						<fo:page-number />
						of
						<fo:page-number-citation-last ref-id="terminator" />
						NOT VALID WITHOUT ALL PAGES
					</fo:block>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="reportCommonHeader"/>
					<fo:block space-before="0.5cm">
							<fo:table table-layout="fixed" width="100%">
								<fo:table-column column-width="100%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
											<fo:block padding-before="0.3cm" padding-after="0.3cm"
												xsl:use-attribute-sets="fonts_group2.size_12">
												<xsl:apply-templates select="intro1Text" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
											<fo:block padding-before="0.3cm" padding-after="0.3cm"
												xsl:use-attribute-sets="fonts_group2.size_12">
												<xsl:apply-templates select="intro2Text" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
					</fo:block>
					<fo:block space-before="0.5cm">
						<fo:table table-layout="fixed" width="100%">
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table> 
						<fo:table table-layout="fixed" width="100%">		
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row background-color="#deded8">
									<fo:table-cell padding-start="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt">
											<xsl:apply-templates select="bodyHeader1" /> <xsl:value-of select="asOfDate" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						<fo:table space-before="0.1cm" table-layout="fixed" width="100%">
							<fo:table-column column-width="48.75%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="0.5%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="50.75%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
									<fo:table-cell><fo:block/></fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
								<fo:table-cell border-collapse="collapse">
										<!-- Table1 inside start-->
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
												<fo:table-row background-color="#deded8" keep-with-previous="always">
													<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
														<fo:block font-family="Verdana" font-size="14pt">
															<xsl:apply-templates select="ipsManagerDetailsSectionTitle" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
													<fo:block padding-before="10px"></fo:block>
														<fo:block xsl:use-attribute-sets="verdana_font.size_10">
															<fo:inline>
																<xsl:value-of select="ipsAssistServiceText" />
															</fo:inline>
															<fo:inline padding-start="0.1cm" font-weight="bold">
																<xsl:choose>
																	<xsl:when test="ipsServiceAvailable = 'true'">
																		Active
																	</xsl:when>
																	<xsl:otherwise>
																		Not active 
																	</xsl:otherwise>
																</xsl:choose>
																<!-- <xsl:if test="ipsServiceAvailable">
																	Activated
																</xsl:if>
																<xsl:if test="not(ipsServiceAvailable)">
																	Not activated 
																</xsl:if> -->
															</fo:inline>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
														<fo:block xsl:use-attribute-sets="verdana_font.size_10">
															<fo:inline>
																<xsl:value-of select="ipsScheduleAnnualReviewDateText" />
															</fo:inline>
															<fo:inline padding-start="0.1cm" font-weight="bold">
																<xsl:choose>
																	<xsl:when test="ipsServiceAvailable = 'true'">
																		<xsl:value-of select="ipsAnnualReviewDate" />
																	</xsl:when>
																	<xsl:otherwise>
																		n/a
																	</xsl:otherwise>
																</xsl:choose>
																<!-- <xsl:if test="ipsServiceAvailable">
																	<xsl:value-of select="ipsAnnualReviewDate" />
																</xsl:if>
																<xsl:if test="not(ipsServiceAvailable)">
																	n/a
																</xsl:if> -->
															</fo:inline>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row keep-with-previous="always">
													<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
														<fo:block xsl:use-attribute-sets="verdana_font.size_10">
																<xsl:value-of select="ipsServiceReviewDateText" />
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
										<!-- Table1 inside end-->
									</fo:table-cell>
									<fo:table-cell><fo:block/></fo:table-cell>
									<fo:table-cell border-collapse="collapse" >
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
											<fo:table-row background-color="#deded8" keep-with-previous="always">
													<fo:table-cell padding-before="0.1cm" padding-start="0.3cm" number-columns-spanned="2" >
														<fo:block font-family="Verdana" font-size="14pt">
															<xsl:apply-templates select="ipsCriteriaAndWeightingsSectionTitle" />
														</fo:block>
													</fo:table-cell>
											</fo:table-row>
											<xsl:choose>
												<xsl:when test="ipsServiceAvailable = 'true'">
													<fo:table-row keep-with-previous="always">
													<fo:table-cell>
														<!-- Table1 inside start-->
														<fo:block padding-before="10px">
															<fo:table table-layout="fixed" width="100%">
																<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
																<fo:table-column column-width="65%" xsl:use-attribute-sets="table-borderstyle"/>
																<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
																<fo:table-body>
																	<xsl:for-each select="ipsCriteriaAndWeightingList/criteria_weighting_info">
																		<fo:table-row height="18px">
																			<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
																				<fo:block>
																					<fo:table table-layout="fixed" width="100%">
																							<fo:table-column column-width="10px"/>
																							<fo:table-body>
																								<fo:table-row height="9px">
																									<xsl:attribute name="background-color">
																										<xsl:value-of select="colorCode"/>
																									</xsl:attribute>
																									<fo:table-cell>
																									<fo:block></fo:block>
																								</fo:table-cell>
																							</fo:table-row>
																						</fo:table-body>
																					</fo:table>
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell padding-before="0.1cm" padding-start="0.2cm">
																				<fo:block xsl:use-attribute-sets="verdana_font.size_10">
																					<xsl:value-of select="criteriaDesc"/>
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
																				<fo:block xsl:use-attribute-sets="verdana_font.size_10">
																					<xsl:value-of select="weighting"/>%
																				</fo:block>
																			</fo:table-cell>
																		</fo:table-row>
																	</xsl:for-each>
																		<fo:table-row>
																			<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
																				<fo:block></fo:block>
																			</fo:table-cell>
																			<fo:table-cell padding-before="0.1cm" padding-start="0.3cm" font-weight="bold">
																				<fo:block>
																					Total
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell padding-before="0.1cm" padding-start="0.3cm" font-weight="bold">
																				<fo:block>
																					<xsl:value-of select="totalweighting"/>%
																				</fo:block>
																			</fo:table-cell>
																		</fo:table-row>
																	</fo:table-body>
																</fo:table>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell>
															<fo:table table-layout="fixed" width="100%">
																<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
																<fo:table-body>
																	<fo:table-row keep-with-previous="always">
																		<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
																			<fo:block start-indent="10px">
																				<fo:external-graphic content-height="114px" content-width="116px">
																					<xsl:attribute name="src">url('<xsl:value-of select="pieChartURL"/>')</xsl:attribute>
																				</fo:external-graphic>
																			</fo:block>
																		</fo:table-cell>
																	</fo:table-row>
																</fo:table-body>
															</fo:table>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row keep-with-previous="always">
														<fo:table-cell padding-before="0.5cm" padding-start="4.5cm" number-columns-spanned="2">
															<fo:block>
																Last modified on: <xsl:value-of select="lastModifiedDate"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</xsl:when>
												<xsl:otherwise>
													<fo:table-row keep-with-previous="always">
														<fo:table-cell padding-before="0.1cm" padding-start="0.3cm" number-columns-spanned="2">
															<fo:block xsl:use-attribute-sets="verdana_font.size_10">
																<xsl:apply-templates select="ipsServiceDeactivated" />
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</xsl:otherwise>
											</xsl:choose>
										</fo:table-body>
										</fo:table>
											
										<!-- Table1 inside end-->
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
						<fo:block space-before="0.5cm">
							<fo:table table-layout="fixed" width="100%">
								<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table> 
							<fo:table table-layout="fixed" width="100%">		
								<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-body>
									<fo:table-row background-color="#deded8" >
										<fo:table-cell padding-start="0.1cm" >
											<fo:block font-family="Verdana" font-size="14pt">
												<xsl:apply-templates select="ipsReviewReportSectionTitle" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
						</fo:table>
						<fo:table space-before="0.1cm" table-layout="fixed" width="100%">
							<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-header>
									<fo:table-row background-color="#deded8">
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left" >
											<fo:block xsl:use-attribute-sets="verdana_font.size_10">
												View Results
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block xsl:use-attribute-sets="verdana_font.size_10">
												Review Status
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block xsl:use-attribute-sets="verdana_font.size_10">
												Notices
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-header>
								<fo:table-body>
								<xsl:if test="ipsReviewReportDetailsNotAvailable">
									<fo:table-row>
												<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
													<fo:block xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="ipsNoCurrentOrPreviousReport"/>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
													<fo:block>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
													<fo:block>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
									</xsl:if>
										<xsl:for-each select="ipsReviewReportDetailsList/ips_review_report_details">
											<fo:table-row>
												<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides" padding-start="0.1cm" padding-before="0.1cm" padding-after="0.1cm">
													<fo:block xsl:use-attribute-sets="verdana_font.size_10">
														<!--  IPS Review Report for -->
														<xsl:value-of select="viewResultsDisplay"/>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides" padding-start="0.1cm" padding-before="0.1cm" padding-after="0.1cm">
													<fo:block xsl:use-attribute-sets="verdana_font.size_10" color="#808000">
														<xsl:value-of select="reviewRequestStatus"/>
														<xsl:if test="showNoFundMatchingTresholdIcon = 'true'">
														    <fo:inline font-size="1pt" padding-start="1mm">
														    <fo:external-graphic content-width="12px" content-height="8px">
															<xsl:attribute name="src">
																<xsl:value-of select="$fundInfoIcon"/>
															</xsl:attribute>
														    </fo:external-graphic>
														   </fo:inline>
													   </xsl:if>
													</fo:block>
												</fo:table-cell>
												<xsl:choose>
													<xsl:when test="isParticipantNoticationAvailable = 'true'">
														<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides" padding-start="0.1cm" padding-before="0.1cm" padding-after="0.1cm">
															<fo:block xsl:use-attribute-sets="verdana_font.size_10">
																	Participant Notification
															</fo:block>
															
														</fo:table-cell>
													</xsl:when>
													<xsl:otherwise>
														<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides" >
															<fo:block>
															</fo:block>
														</fo:table-cell>
													</xsl:otherwise>
												</xsl:choose>
											</fo:table-row>
										</xsl:for-each>
										<fo:table-row background-color="#cac8c4" height="0px">
											<fo:table-cell xsl:use-attribute-sets="solid.blue.border.last" >
												<fo:block>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="solid.blue.border.last" >
												<fo:block>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="solid.blue.border.last" >
						 						<fo:block>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
				
				<xsl:call-template name="reportCommonFooter"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>