<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java"
	xmlns:zip="com.manulife.util.render.ZipCodeRender">
	<xsl:template name="WithdrawalDeclarations">

				<!--Declarations  section -->
		<fo:block space-before="20px" page-break-inside="avoid">
				
			<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell background-repeat="no-repeat" 
							xsl:use-attribute-sets="table_header_text_font" number-columns-spanned="2">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/cmaContent/declarationsSectionTitle" /> 
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
				<fo:table-column column-width="40%" />											
				<fo:table-column column-width="60%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="table_sub_header_text_bold_font" number-columns-spanned="2">
							<fo:block>
								The participant has certified:
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
							<fo:block space-after="20px">
						
							  <xsl:choose>
									<xsl:when test="withdrawalRequestUi/hasTaxNoticeDeclaration = 'true'">
										<fo:inline>
											<fo:external-graphic content-height="6px" content-width="6px" >
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')"/>
												</xsl:attribute>
											</fo:external-graphic>
										</fo:inline>
									</xsl:when>
									<xsl:otherwise>
										<fo:inline>
											<fo:external-graphic content-height="6px" content-width="6px" >
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')"/>
												</xsl:attribute>
											</fo:external-graphic>
										</fo:inline>
									</xsl:otherwise>
								</xsl:choose> 
						
							<fo:inline padding-start="5px">
								<xsl:value-of select="withdrawalRequestUi/cmaContent/declarationTaxNoticeText"/>
							</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
							<fo:block padding-after="5" space-after="10px">
							
							<xsl:choose>
									<xsl:when test="withdrawalRequestUi/hasWaitPeriodWaveDeclaration = 'true'">
										<fo:inline>
											<fo:external-graphic content-height="6px" content-width="6px" >
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')"/>
												</xsl:attribute>
											</fo:external-graphic>
										</fo:inline>
									</xsl:when>
									<xsl:otherwise>
										<fo:inline>
											<fo:external-graphic content-height="6px" content-width="6px" >
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')"/>
												</xsl:attribute>
											</fo:external-graphic>
										</fo:inline>
									</xsl:otherwise>
								</xsl:choose>
						
							
							<fo:inline padding-start="5px">
								<xsl:value-of select="withdrawalRequestUi/cmaContent/waitingPeriodText"/>
							</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<xsl:if test="withdrawalRequestUi/showParticipantAgreedLegaleseContent = 'true'">
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
							<fo:block padding-after="5" space-after="10px">
								<fo:inline>
									<fo:external-graphic content-height="6px" content-width="6px" >
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:inline>
								
								<fo:inline>
									The Participant declaration was read and agreed to:
								</fo:inline>
									
							</fo:block>
							
							<fo:block padding-after="5" space-after="10px">
								<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/participantLegaleseInfo/legaleseText"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
							
							<fo:block padding-after="1px" space-after="3px" xsl:use-attribute-sets="table_sub_header_text_bold_font">
								<xsl:choose>
									<xsl:when test="withdrawalRequestUi/isParticipantInitiated = 'true'">
									 <xsl:if test="withdrawalRequestUi/printParticipant = 'false'">
											<xsl:if test="withdrawalRequestUi/showItWasCertifiedLabel = 'true'">
												<fo:inline>
													It was certified that:
												</fo:inline>
											</xsl:if>	
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="withdrawalRequestUi/wmsiOrPenchecksSelected = 'true'">
											<fo:inline>
												It was certified that:
											</fo:inline>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
							
							<fo:block padding-after="5px">
								<xsl:if test="withdrawalRequestUi/wmsiOrPenchecksSelected = 'true'">
								
								<xsl:choose>
										<xsl:when test="withdrawalRequestUi/hasIraProviderDeclaration = 'true'">
											<fo:inline>
												<fo:external-graphic content-height="6px" content-width="6px" >
													<xsl:attribute name="src">
														<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')"/>
													</xsl:attribute>
												</fo:external-graphic>
											</fo:inline>
										</xsl:when>
										<xsl:otherwise>
											<fo:inline>
												<fo:external-graphic content-height="6px" content-width="6px" >
													<xsl:attribute name="src">
														<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')"/>
													</xsl:attribute>
												</fo:external-graphic>
											</fo:inline>
										</xsl:otherwise>
									</xsl:choose>
									
									<fo:inline padding-start="5px">
										<xsl:value-of select="withdrawalRequestUi/cmaContent/iraProviderText"/>
									</fo:inline>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<xsl:if test="withdrawalRequestUi/printParticipant = 'false'">
					<xsl:if test="withdrawalRequestUi/showAtRiskDeclaration = 'true'">
						<fo:table-row>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font"
								number-columns-spanned="2">
								<fo:table table-layout="fixed" width="100%">
									<fo:table-column column-width="3%" />
									<fo:table-column column-width="97%" />
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell>
												<fo:block>
													<xsl:choose>
														<xsl:when
															test="withdrawalRequestUi/hasRiskIndicatorDeclaration = 'true'">
															<fo:inline>
																<fo:external-graphic content-height="6px"
																	content-width="6px">
																	<xsl:attribute name="src">
																		<xsl:value-of
																		select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')" />
																	</xsl:attribute>
																</fo:external-graphic>
															</fo:inline>
														</xsl:when>
														<xsl:otherwise>
															<fo:inline>
																<fo:external-graphic content-height="6px"
																	content-width="6px">
																	<xsl:attribute name="src">
																		<xsl:value-of
																		select="concat($imagePath_var, 'plaincheckboxSmall.gif')" />
																	</xsl:attribute>
																</fo:external-graphic>
															</fo:inline>
														</xsl:otherwise>
													</xsl:choose>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:if test="withdrawalRequestUi/hasRiskIndicatorDeclaration = 'true'">
														<xsl:value-of select="withdrawalRequestUi/atRiskApprovalText" />
														
														<fo:block space-before="15px">
				
															<fo:table table-layout="fixed" width="100%">
																<fo:table-column column-width="3%" />
																<fo:table-column column-width="97%" />
																<fo:table-body>
																	<xsl:for-each select="withdrawalRequestUi/atRiskdetailText/string">
																		<xsl:variable name="numberCount" select="position()" />
																		<fo:table-row>
																			<fo:table-cell>
																				<fo:block padding-before="10px">
																					<xsl:value-of select="$numberCount" />
																					.
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell>
																				<fo:block padding-before="10px">
																					<xsl:value-of select="." />
																				</fo:block>
																			</fo:table-cell>
																		</fo:table-row>
																	</xsl:for-each>
																</fo:table-body>
															</fo:table>
														</fo:block>
														
													</xsl:if>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
				</xsl:if>
					
					<xsl:if test="$showStaticContent_var = 'true'">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
							<fo:block padding-after="4px">
								<xsl:value-of select="withdrawalRequestUi/cmaContent/step2PageBeanBody3Header"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					
					<!--If the request is in Approved state then the following information should be display otherwise no need. -->
					<xsl:if test="withdrawalRequestUi/isRequestApproved = 'true'">
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="table_sub_header_text_bold_font" number-columns-spanned="2">
								<fo:block>
									The approver agreed to: 
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
								<fo:block padding-after="4px">
									 <xsl:value-of select="withdrawalRequestUi/withdrawalRequest/legaleseInfo/legaleseText" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if> 
					
				</fo:table-body>
			</fo:table>
		</fo:block>
			
	</xsl:template>
</xsl:stylesheet>