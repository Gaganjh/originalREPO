<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" >
	
	<xsl:template name="fundsAndPerformanceGeneric">
			<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after">
					<fo:table>
						<xsl:choose>
							<xsl:when
								test="tabName = 'pricesAndYTD' or tabName = 'performanceAndFees' or tabName = 'morningstar' or tabName = 'fundInformation' or tabName = 'fundCharacteristics1' or tabName = 'fundCharacteristics2' or tabName = 'standardDeviation' ">
								<fo:table-column column-width="55%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="45%"
									xsl:use-attribute-sets="table-borderstyle" />
							</xsl:when>
							<xsl:otherwise>
								<fo:table-column column-width="40%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="60%"
									xsl:use-attribute-sets="table-borderstyle" />
							</xsl:otherwise>
						</xsl:choose>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
										Page
										<fo:page-number />
										of
										<fo:page-number-citation-last
											ref-id="terminator" />
										NOT VALID WITHOUT ALL PAGES
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
										This is for intermediary purposes only. Not for distribution
										to members of the general public</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<fo:table>
							<fo:table-column column-width="40%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="60%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body>
								<fo:table-cell>
									<fo:block>
										<fo:external-graphic content-height="8cm" content-width="8cm">
											<xsl:attribute name="src">
									<xsl:value-of select="jhLogoPath" />
								</xsl:attribute>
										</fo:external-graphic>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding-before="0.5cm">
									<fo:block text-align="right" font-family="Georgia"
										font-size="16pt">
										<xsl:apply-templates select="pageName" />
									</fo:block>
									<fo:block text-align="right" font-family="Georgia"
										font-size="16pt">
										<xsl:apply-templates select="asOfDate" />
									</fo:block>
									<xsl:if test="currentContract">
										<fo:block text-align="right" font-family="Georgia"
											font-size="16pt">
											<xsl:apply-templates select="currentContractName" />
											(
											<xsl:apply-templates select="currentContract" />
											)
										</fo:block>
									</xsl:if>
								</fo:table-cell>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block space-before="0.5cm">
						<xsl:choose>
							<xsl:when test="tabName = 'fundCheck'">
								<fo:table>
									<fo:table-column column-width="55%"
										xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="5%"
										xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="40%"
										xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell padding-start="0.3cm"
												padding-after="0.3cm">
												<fo:block padding-before="0.3cm" padding-after="0.3cm"
													xsl:use-attribute-sets="fonts_group2.size_12">
													<xsl:apply-templates select="intro1Text" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block />
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell padding-start="0.3cm"
												padding-after="0.3cm">
												<fo:block padding-before="0.3cm" padding-after="0.3cm"
													xsl:use-attribute-sets="fonts_group2.size_12">
													<xsl:apply-templates select="intro2Text" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block />
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell padding-start="0.3cm"
												padding-after="0.3cm">
												<fo:block padding-before="0.3cm" padding-after="0.3cm"
													xsl:use-attribute-sets="fonts_group2.size_12">
													<xsl:apply-templates select="tabHeader" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm"
													border-before-style="solid" border-before-color="#febe10"
													background-color="#febe10" padding-start="0.5cm"
													padding-before="0.1cm">
												</fo:block>	
												<!--
													orange color
												-->
												<fo:block padding-start="0.5cm" padding-before="0.2cm"
													xsl:use-attribute-sets="fonts_group2.size_12" font-weight="bold"
													background-color="#eceae3">
													<xsl:apply-templates select="legend/title" />
												</fo:block>
												<fo:block padding-start="0.5cm" padding-before="0.2cm"
													xsl:use-attribute-sets="fonts_group2.size_12"
													background-color="#eceae3">
													<fo:table>
														<fo:table-column column-width="30%"
															xsl:use-attribute-sets="table-borderstyle" />
														<fo:table-column column-width="70%"
															xsl:use-attribute-sets="table-borderstyle" />
														<fo:table-body>
															<xsl:for-each select="legend/key">
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block xsl:use-attribute-sets="fonts_group2.size_12"
																			background-color="#eceae3">
																			<fo:external-graphic>
																				<xsl:attribute name="src">
									                       <xsl:value-of select="image" />
								                            </xsl:attribute>
																			</fo:external-graphic>
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																		<fo:block xsl:use-attribute-sets="fonts_group2.size_12"
																			background-color="#eceae3">
																			<xsl:value-of select="value" />
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</xsl:for-each>
														</fo:table-body>
													</fo:table>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<xsl:if test="hypoethicalText">
											<fo:table-row>
												<fo:table-cell padding-start="0.3cm"
													padding-after="0.3cm">
													<fo:block padding-before="0.3cm" padding-after="0.3cm"
														xsl:use-attribute-sets="fonts_group2.size_12"> Hypothetical returns are shown in
														bold</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block />
												</fo:table-cell>
												<fo:table-cell>
													<fo:block />
												</fo:table-cell>
											</fo:table-row>
										</xsl:if>
										<xsl:if test="feeWaiverDisclosure">
												<fo:table-row>
												<fo:table-cell padding-start="0.3cm"
													padding-after="0.3cm">
													<fo:block padding-before="0.3cm" padding-after="0.3cm"
														xsl:use-attribute-sets="fonts_group2.size_12">
														<xsl:apply-templates select="feeWaiverDisclosure" />
														</fo:block>
													</fo:table-cell>
												<fo:table-cell>
													<fo:block />
												</fo:table-cell>
												<fo:table-cell>
													<fo:block />
												</fo:table-cell>
											</fo:table-row>	
										</xsl:if>
										<xsl:if test="genericOrContractDisclosure">
												<fo:table-row>
												<fo:table-cell padding-start="0.3cm"
													padding-after="0.3cm">
													<fo:block padding-before="0.3cm" padding-after="0.3cm"
														xsl:use-attribute-sets="fonts_group2.size_12">
														<xsl:apply-templates select="genericOrContractDisclosure" />
														</fo:block>
													</fo:table-cell>
												<fo:table-cell>
													<fo:block />
												</fo:table-cell>
												<fo:table-cell>
													<fo:block />
												</fo:table-cell>
											</fo:table-row>	
										</xsl:if>
									</fo:table-body>
								</fo:table>
							</xsl:when>
							<xsl:otherwise>
								<fo:table>
									<fo:table-column column-width="100%"
										xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell padding-start="0.3cm"
												padding-after="0.3cm">
												<fo:block padding-before="0.3cm"
													xsl:use-attribute-sets="fonts_group2.size_12">
													<xsl:apply-templates select="intro1Text" />
												</fo:block>
												<fo:block padding-before="0.3cm"
													xsl:use-attribute-sets="fonts_group2.size_12">
													<xsl:apply-templates select="intro2Text" />
												</fo:block>
												<fo:block padding-before="0.3cm"
													xsl:use-attribute-sets="fonts_group2.size_12">
													<xsl:apply-templates select="tabHeader" />
												</fo:block>
												<xsl:if test="hypoethicalText">
													<fo:block padding-before="0.3cm" padding-after="0.3cm"
														xsl:use-attribute-sets="fonts_group2.size_12"> Hypothetical returns are shown in
														bold</fo:block>
												</xsl:if>
												<xsl:if test="feeWaiverDisclosure">
													<fo:block padding-before="0.3cm" padding-after="0.3cm"
														xsl:use-attribute-sets="fonts_group2.size_12">
														<xsl:apply-templates select="feeWaiverDisclosure" />
														</fo:block>
												</xsl:if>
												<xsl:if test="restrictedFundsDisclosure">
													<fo:block padding-before="0.3cm" padding-after="0.3cm"
														xsl:use-attribute-sets="fonts_group2.size_12">
														<xsl:apply-templates select="restrictedFundsDisclosure" />
														</fo:block>
												</xsl:if>
												<xsl:if test="genericOrContractDisclosure">
													<fo:block padding-before="0.3cm" padding-after="0.3cm"
														xsl:use-attribute-sets="fonts_group2.size_12">
														<xsl:apply-templates select="genericOrContractDisclosure" />
														</fo:block>
												</xsl:if>
											</fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</xsl:otherwise>
						</xsl:choose>
					</fo:block>
					
					<!-- Filters - start-->
					<fo:block space-before="0.5cm">
						<fo:block background-color="#deded8" font-family="Verdana"
							font-size="14pt"> Filters used:</fo:block>
						<fo:table>
							<fo:table-column column-width="20%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="80%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body>
								<xsl:if test="filters/currentView">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm"
											padding-start="0.6cm">
											<fo:block font-weight="bold"> View:</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm"
											padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/currentView" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="filters/currentClass">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm"
											padding-start="0.6cm">
											<fo:block font-weight="bold"> Class:</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm"
											padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/currentClass" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="filters/currentGroupByOption">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm"
											padding-start="0.6cm">
											<fo:block font-weight="bold"> Group by:</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm"
											padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/currentGroupByOption" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<!--  Filters - end-->
					<fo:block space-before="0.2cm">
						<xsl:variable name="number_of_columns">
							<xsl:choose>
								<xsl:when test="tabName = 'fundInformation'">
									11
								</xsl:when>
								<xsl:when test="tabName = 'fundCharacteristics1'">
									13
								</xsl:when>
								<xsl:when test="tabName = 'fundCharacteristics2'">
									13
								</xsl:when>
								<xsl:when test="tabName = 'standardDeviation'">
									9
								</xsl:when>
								<xsl:when test="tabName = 'morningstar'">
									17
								</xsl:when>
								<xsl:when test="tabName = 'pricesAndYTD'">
									17
								</xsl:when>
								<xsl:when test="tabName = 'fundCheck'">
									9
								</xsl:when>
								<xsl:when test="tabName = 'performanceAndFees'">
									20
								</xsl:when>
							</xsl:choose>
						</xsl:variable>
						<fo:table>
							<xsl:if test="tabName = 'pricesAndYTD'">
								<fo:table-column column-width=".7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="21%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="15.3%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.75%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.75%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.75%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.75%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.75%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.75%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								
							</xsl:if>
							<xsl:if test="tabName = 'fundInformation'">
								<fo:table-column column-width="1%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="18%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="11%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="8%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="13%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="9%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="9%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="9%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="9%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="9%"
									xsl:use-attribute-sets="table-borderstyle" />
							</xsl:if>
							<xsl:if test="tabName = 'fundCharacteristics1'">
								<fo:table-column column-width="1%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="18%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="14%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="9%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="6%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="6%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="6%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="9%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="8%"
									xsl:use-attribute-sets="table-borderstyle" />
							</xsl:if>
							<xsl:if test="tabName = 'fundCharacteristics2'">
								<fo:table-column column-width="1%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="18%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="14%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="9%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="6%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="6%"
									xsl:use-attribute-sets="table-borderstyle" />
							</xsl:if>
							<xsl:if test="tabName = 'standardDeviation'">
								<fo:table-column column-width="1%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="23%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="17%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="13%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="8%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="8%"
									xsl:use-attribute-sets="table-borderstyle" />
							</xsl:if>
							<xsl:if test="tabName = 'performanceAndFees'">
								<fo:table-column column-width=".5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="16.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7.6%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="6.0%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4.0%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="2.8%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4.7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="6.0%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="3.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4.7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="6.0%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="6%"
									xsl:use-attribute-sets="table-borderstyle" />
							</xsl:if>
							<xsl:if test="tabName = 'morningstar'">
								<fo:table-column column-width=".5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="18.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="7.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="6%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="4%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="8%"
									xsl:use-attribute-sets="table-borderstyle" />
							</xsl:if>
							<xsl:if test="tabName = 'fundCheck'">
								<fo:table-column column-width=".5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="22.5%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="8%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
								
								<fo:table-column column-width="15%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="14%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="0%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="0%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="0%"
									xsl:use-attribute-sets="table-borderstyle" />
							</xsl:if>
							<fo:table-header>
								<xsl:if test="header1">
									<fo:table-row background-color="#767676"
										display-align="center">
										<xsl:for-each select="header1/header">
											<fo:table-cell padding-start="0.1cm"
												xsl:use-attribute-sets="solid.blue.border.sides">
												<xsl:attribute name="number-columns-spanned">
										    <xsl:value-of select="colSpan" />
										 </xsl:attribute>
												<xsl:if test="rowSpan">
													<xsl:attribute name="number-rows-spanned">
										       <xsl:value-of select="rowSpan" />
										    </xsl:attribute>
												</xsl:if>
												<fo:block text-align="center" padding-before="0.1cm"
													padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:value-of select="name" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>
										</xsl:for-each>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="header2">
									<fo:table-row background-color="#767676"
										display-align="center">
										<xsl:for-each select="header2/header">
											<fo:table-cell padding-start="0.1cm"
												xsl:use-attribute-sets="solid.blue.border.sides">
												<xsl:attribute name="number-columns-spanned">
										    <xsl:value-of select="colSpan" />
										 </xsl:attribute>
												<fo:block text-align="center" padding-before="0.1cm"
													padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_9">
													<fo:inline>
														<xsl:value-of select="name" />
													</fo:inline>
													<xsl:if test="sup">
														<fo:inline vertical-align="super" font-size="8pt">
															<xsl:value-of select="sup" />
														</fo:inline>
													</xsl:if>
												</fo:block>
											</fo:table-cell>
										</xsl:for-each>
									</fo:table-row>
								</xsl:if>
								<fo:table-row background-color="#deded8"
									display-align="center">
									<xsl:for-each select="header3/header">
										<fo:table-cell 
											xsl:use-attribute-sets="solid.blue.border">
											   <xsl:if test="align = 'right'">
													<xsl:attribute name="padding-end">
										            	0.1cm
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="align = 'left'">
													<xsl:attribute name="padding-start">
										        		0.1cm
													</xsl:attribute>
												</xsl:if>
											<xsl:if test="colSpan">
											  <xsl:attribute name="number-columns-spanned">
										           <xsl:value-of select="colSpan" />
										       </xsl:attribute>
											</xsl:if>	
											<fo:block padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<xsl:attribute name="text-align">
										        	<xsl:value-of select="align" />
										        </xsl:attribute>
												<fo:inline>
													<xsl:value-of select="name" />
												</fo:inline>
												<xsl:if test="sup">
													<fo:inline vertical-align="super" font-size="8pt">
														<xsl:value-of select="sup" />
													</fo:inline>
												</xsl:if>
											</fo:block>
										</fo:table-cell>
									</xsl:for-each>
								</fo:table-row>
								<fo:table-row keep-with-previous="always">
									<fo:table-cell number-columns-spanned="{$number_of_columns}">
										<fo:block border-before-width="0.1cm"
											border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-body>
								<xsl:for-each select="reportDetails/reportDetail">
									<fo:table-row height="0.1cm">
										<fo:table-cell number-columns-spanned="{$number_of_columns}">
											<fo:block />
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="{$number_of_columns}">
											<fo:block border-before-width="0.1cm"
												border-before-style="solid" border-before-color="#febe10" />
											<!--
												orange color
											-->
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row background-color="#deded8"
										keep-with-previous="always">
										<fo:table-cell padding-start="0.2cm"
											number-columns-spanned="{$number_of_columns}">
											<fo:block font-family="Verdana" font-size="14pt">
												<xsl:value-of select="fundCategory" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<xsl:for-each select="fundDetail">
										<fo:table-row display-align="center">
											<xsl:if test="./rowColor">
												<xsl:attribute name="background-color"><xsl:value-of
													select="./rowColor" /></xsl:attribute>
											</xsl:if>
											<xsl:if test="./rowFontColor">
												<xsl:attribute name="color"><xsl:value-of
													select="./rowFontColor" /></xsl:attribute>
											</xsl:if>		
											<xsl:if test="./includeFeeWaiverColumn or ./includeRestrictedFund">
												 <fo:table-cell xsl:use-attribute-sets="solid.blue.border.left" >
												         <xsl:if test="showFeeWaiverSymbolRowSpan">
										                     <xsl:attribute name="number-rows-spanned">
										                        <xsl:value-of select="showFeeWaiverSymbolRowSpan" />
										                      </xsl:attribute>
										                </xsl:if>	
														<fo:block text-align="end" padding-before="0.2cm">
														     <xsl:if test="./showFeeWaiverSymbol">
														        &#8226;
														     </xsl:if>
													 </fo:block>
													 <fo:block text-align="end" padding-before="0.2cm">
														     <xsl:if test="./showRestrictedFundSymbol">
														        &#8346;
														     </xsl:if>
													 </fo:block>
												 </fo:table-cell>		
											</xsl:if>

											<xsl:for-each select="value">
												<xsl:choose>
													<xsl:when test="./image">
														<fo:table-cell padding-start="0.1cm">
															
															<xsl:choose>
															  <xsl:when test="../includeFeeWaiverColumn or ../includeRestrictedFund">
															     <xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
						                                         <xsl:attribute name="border-after-style">solid</xsl:attribute>
						                                         <xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
					                                             <xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
						                                         <xsl:attribute name="border-before-style">solid</xsl:attribute>
						                                         <xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
						                                         <xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
						                                         <xsl:attribute name="border-end-style">solid</xsl:attribute>
						                                         <xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
															  </xsl:when>
															  <xsl:otherwise>
															      <xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		                                                          <xsl:attribute name="border-start-style">solid</xsl:attribute>
		                                                          <xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	                                                              <xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		                                                          <xsl:attribute name="border-after-style">solid</xsl:attribute>
		                                                          <xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
															  </xsl:otherwise>
															</xsl:choose>
															<xsl:attribute name="text-align">
										                                             <xsl:value-of
																select="align" />
										                                        </xsl:attribute>
															<fo:block>
																<fo:external-graphic>
																	<xsl:attribute name="src">
								                                     	<xsl:value-of
																		select="image" />
								                                    </xsl:attribute>
																</fo:external-graphic>
															</fo:block>
														</fo:table-cell>
													</xsl:when>
													<xsl:when test="./sup">
														<fo:table-cell padding-start="0.1cm">
															<xsl:choose>
															  <xsl:when test="../includeFeeWaiverColumn or ../includeRestrictedFund">
															     <xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
						                                         <xsl:attribute name="border-after-style">solid</xsl:attribute>
						                                         <xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
					                                             <xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
						                                         <xsl:attribute name="border-before-style">solid</xsl:attribute>
						                                         <xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
						                                         <xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
						                                         <xsl:attribute name="border-end-style">solid</xsl:attribute>
						                                         <xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
															  </xsl:when>
															  <xsl:otherwise>
															      <xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		                                                          <xsl:attribute name="border-start-style">solid</xsl:attribute>
		                                                          <xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	                                                              <xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		                                                          <xsl:attribute name="border-after-style">solid</xsl:attribute>
		                                                          <xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
															  </xsl:otherwise>
															</xsl:choose>
															<xsl:if test="rowSpan">
																<xsl:attribute name="number-rows-spanned">
										                                             <xsl:value-of
																	select="rowSpan" />
										                                        </xsl:attribute>
															</xsl:if>
															<fo:block xsl:use-attribute-sets="verdana_font.size_9">
																<fo:block padding-before="0.1cm">
																	<fo:inline>
																		<xsl:value-of select="name" />
																	</fo:inline>
																	<fo:inline vertical-align="super" font-size="8pt">
																		<xsl:value-of select="sup" />
																	</fo:inline>
																</fo:block>
															</fo:block>
														</fo:table-cell>
													</xsl:when>
													<xsl:otherwise>
														<fo:table-cell>
														    <xsl:choose>
															  <xsl:when test="../includeFeeWaiverColumn or ../includeRestrictedFund">
															     <xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
						                                         <xsl:attribute name="border-after-style">solid</xsl:attribute>
						                                         <xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
					                                             <xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
						                                         <xsl:attribute name="border-before-style">solid</xsl:attribute>
						                                         <xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
						                                         <xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
						                                         <xsl:attribute name="border-end-style">solid</xsl:attribute>
						                                         <xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
															  </xsl:when>
															  <xsl:otherwise>
															      <xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		                                                          <xsl:attribute name="border-start-style">solid</xsl:attribute>
		                                                          <xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	                                                              <xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		                                                          <xsl:attribute name="border-after-style">solid</xsl:attribute>
		                                                          <xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
															  </xsl:otherwise>
															</xsl:choose>
															<xsl:if test="rowSpan">
																<xsl:attribute name="number-rows-spanned">
										                                             <xsl:value-of
																	select="rowSpan" />
										                                        </xsl:attribute>
															</xsl:if>
															<xsl:attribute name="text-align">
										                                             <xsl:value-of
																select="align" />
										                                        </xsl:attribute>
															<xsl:if test="align = 'right'">
																<xsl:attribute name="padding-end">
										                                                     0.03cm
										                                            </xsl:attribute>
															</xsl:if>
															<xsl:if test="align = 'left'">
																<xsl:attribute name="padding-start">
										                                                     0.03cm
										                                            </xsl:attribute>
															</xsl:if>
															<fo:block xsl:use-attribute-sets="verdana_font.size_9">
																<fo:block padding-before="0.1cm">
																	<xsl:if test="bold">
																		<xsl:attribute name="font-weight">bold</xsl:attribute>
																	</xsl:if>
																	<fo:inline>
																		<xsl:value-of select="name" />
																	</fo:inline>
																</fo:block>
															</fo:block>
														</fo:table-cell>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
										</fo:table-row>
										<xsl:if test="Commentary">
											<fo:table-row keep-with-previous="always">
												<xsl:if test="./rowColor">
													<xsl:attribute name="background-color"><xsl:value-of
														select="./rowColor" /></xsl:attribute>
												</xsl:if>
												<xsl:if test="./rowFontColor">
													<xsl:attribute name="color"><xsl:value-of
														select="./rowFontColor" /></xsl:attribute>
												</xsl:if>
												<fo:table-cell padding-start="0.1cm"
													number-columns-spanned="9" xsl:use-attribute-sets="solid.blue.border">
													<fo:block xsl:use-attribute-sets="verdana_font.size_9"
														padding-before="0.1cm">
														<fo:inline font-weight="bold"> Commentary</fo:inline>
														<xsl:if test="Commentary/asOfDate">
															<fo:inline font-weight="bold"> as of :</fo:inline>
														</xsl:if>
														<xsl:if test="Commentary/asOfDate">
															<fo:inline>
																<xsl:value-of select="Commentary/asOfDate" />
															</fo:inline>
														</xsl:if>
														<fo:block>
															<xsl:if test="Commentary/commentaryText">
																<fo:inline>
																	<xsl:value-of select="Commentary/commentaryText" />
																</fo:inline>
															</xsl:if>
														</fo:block>
														<fo:block>
															<xsl:if test="Commentary/reasonText">
																<fo:inline>
																	<xsl:value-of select="Commentary/reasonText" />
																</fo:inline>
															</xsl:if>
														</fo:block>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:if>
										<xsl:if test="disclosureText">
											<fo:table-row keep-with-previous="always">
												<xsl:if test="./rowColor">
													<xsl:attribute name="background-color"><xsl:value-of
														select="./rowColor" /></xsl:attribute>
												</xsl:if>
												<xsl:if test="./rowFontColor">
													<xsl:attribute name="color"><xsl:value-of
														select="./rowFontColor" /></xsl:attribute>
												</xsl:if>
												<fo:table-cell padding-start="0.1cm"
													number-columns-spanned="{$number_of_columns}"
													xsl:use-attribute-sets="solid.blue.border">
													<fo:block xsl:use-attribute-sets="verdana_font.size_9">
														<fo:block padding-before="0.1cm">
															<fo:inline>
																<xsl:value-of select="disclosureText" />
															</fo:inline>
														</fo:block>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:if>
									</xsl:for-each>
								</xsl:for-each>
							</fo:table-body>
						</fo:table>
					</fo:block>
							
				   <!-- Showing the page footer-->
					<fo:block space-before="0.7cm">
						
						<fo:block>
							<fo:table>
								<fo:table-column column-width="100%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-body>
									<xsl:if test="footer">
										<fo:table-row>
											<fo:table-cell padding-start="1cm"
												padding-before="0.1cm">
												<fo:block xsl:use-attribute-sets="arial_font.size_11">
													<xsl:apply-templates select="footer" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="footnotes">
										<fo:table-row>
											<fo:table-cell padding-start="1cm"
												padding-before="0.1cm">
												<fo:block xsl:use-attribute-sets="arial_font.size_11">
													<xsl:apply-templates select="footnotes" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="tabName = 'morningstar'">
										<xsl:if test="tabName = 'morningstar'">
											<xsl:if test="morningstarTabFootnotes">
												<xsl:for-each select="morningstarTabFootnotes">
													<fo:table-row>
														<fo:table-cell padding-start="1cm" padding-before="0.1cm">
															<fo:block xsl:use-attribute-sets="arial_font.size_11">
																<fo:block padding-before="0.1cm">
																	<fo:inline>
																		<xsl:value-of select="morningstarTabFootnotes" />
																	</fo:inline>
																</fo:block>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</xsl:for-each>
											</xsl:if>
										</xsl:if>
									</xsl:if>
									<xsl:if test="tabFootnotes">
										<xsl:for-each select="tabFootnotes">
											<fo:table-row>
												<fo:table-cell padding-start="1cm"
													padding-before="0.1cm">
													<fo:block xsl:use-attribute-sets="arial_font.size_11">
														<fo:inline vertical-align="super" font-size="10pt">
															<xsl:apply-templates select="sup" />
														</fo:inline>
														<fo:inline>
														    <xsl:apply-templates select="value" />
														</fo:inline>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:for-each>
									</xsl:if>
									<xsl:if test="disclaimer">
										<fo:table-row>
											<fo:table-cell padding-start="1cm"
												padding-before="0.1cm">
												<fo:block xsl:use-attribute-sets="arial_font.size_11">
													<xsl:apply-templates select="disclaimer" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block>
					<fo:block space-before="0.5cm" xsl:use-attribute-sets="arial_font.size_11"
						keep-together.within-column="always">
						<xsl:apply-templates select="globalDisclosure" />
					</fo:block>
					<fo:block id="terminator" />
				</fo:flow>
			</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>