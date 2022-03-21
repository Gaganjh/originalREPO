<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java"
	xmlns:BD="com.manulife.pension.ps.web.iloans.util.BigDecimalFormatter">
	
	<xsl:template name="loanRequestCalculateMaxLoanAvailableSection">
	<fo:block space-before="20px" page-break-inside="avoid">
		<fo:table table-layout="fixed" width="100%">
		<fo:table-column column-width="100%" />
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets="table_header_text_font">
					<fo:block>
						<xsl:value-of select="cmaContent/calcualteMaxAvailableForLoanSectionTitleText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
		</fo:table>
	
		<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout" keep-with-previous="always">
		<fo:table-column column-width="74.5%" />
		<fo:table-column column-width="0.5%" />
		<fo:table-column column-width="24.5%" />
		<fo:table-column column-width="0.5%" />
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell>
					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="17%" />
						<fo:table-column column-width="20%" />
						<fo:table-column column-width="20%" />
						<fo:table-column column-width="17%" />
						<fo:table-column column-width="11%" />
						<fo:table-column column-width="15%" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="table_cell_bold_border_style" number-columns-spanned="6">
									<fo:block>Vested account balance</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-before="10px" padding-end="3px">
									<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
										Money type
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="3px">
									<fo:block text-align="right" xsl:use-attribute-sets="table_sub_header_default_bold_font">
										<xsl:value-of select="displayRules/accountBalanceLabel"/>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-before="10px" padding-end="3px">
									<fo:block text-align="right" xsl:use-attribute-sets="table_sub_header_default_bold_font">
										Vesting (%)
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="3px">
									<fo:block text-align="right" xsl:use-attribute-sets="table_sub_header_default_bold_font">
										Vested balance ($)
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-before="10px" padding-end="3px">
									<fo:block text-align="right" xsl:use-attribute-sets="table_sub_header_default_bold_font">
										Exclude
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="3px">
									<fo:block text-align="right" xsl:use-attribute-sets="table_sub_header_default_bold_font">
										Available amount ($)
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<xsl:for-each select="moneyTypesWithAccountBalance/loanMoneyType">
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<fo:table-row xsl:use-attribute-sets="table_cell_background_style">
											<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-after="5px">
												<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="1px">
													<xsl:value-of select="contractMoneyTypeShortName"/>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="3px" padding-after="5px">
												<fo:block xsl:use-attribute-sets="table_data_default_font" text-align="right" padding-before="1px">
													<xsl:value-of select="accountBalance"/>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="3px" padding-after="5px">
												<fo:block xsl:use-attribute-sets="table_data_default_font" text-align="right" padding-before="1px">
													<xsl:value-of select="BD:format(vestingPercentage, 3, 3)" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="3px" padding-after="5px">
												<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="1px">
													<xsl:variable name="moneyTypeId_var">
														<xsl:value-of select="moneyTypeId" />
													</xsl:variable>
													<xsl:for-each
														select="../../loanMoneyTypeCalculation/vestedBalanceMap/item[@key=concat('vestedBalance_', $moneyTypeId_var)]">
														<xsl:value-of select="@value" />
													</xsl:for-each>
												</fo:block>
											</fo:table-cell>
	
											<xsl:choose>
												<xsl:when test="excludeIndicator = 'false'">
													<fo:table-cell padding-after="5px" xsl:use-attribute-sets="table_data_default_font">
														<fo:block padding-before="1px" text-align="center">
															<fo:external-graphic content-height="8px" content-width="8px">
																<xsl:attribute name="src">
																	<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')" />
																</xsl:attribute>
															</fo:external-graphic>
														</fo:block>
													</fo:table-cell>
												</xsl:when>
												<xsl:otherwise>
													<fo:table-cell padding-after="5px" xsl:use-attribute-sets="table_data_default_font">
														<fo:block padding-before="1px" text-align="center">
															<fo:external-graphic content-height="8px" content-width="8px">
																<xsl:attribute name="src">
																	<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')" />
																</xsl:attribute>
															</fo:external-graphic>
														</fo:block>
													</fo:table-cell>
												</xsl:otherwise>
											</xsl:choose>
											<fo:table-cell padding-end="3px" padding-after="5px">
												<fo:block xsl:use-attribute-sets="table_data_default_font" text-align="right" padding-before="1px">
													<xsl:variable name="moneyTypeId_var">
														<xsl:value-of select="moneyTypeId" />
													</xsl:variable>
													<xsl:for-each select="../../loanMoneyTypeCalculation/availableBalanceMap/item[@key=concat('availableBalance_', $moneyTypeId_var)]">
														<xsl:value-of select="@value" />
													</xsl:for-each>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:when>
									<xsl:otherwise>
										<fo:table-row space-before="5px">
											<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-after="5px">
												<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="1px">
													<xsl:value-of select="contractMoneyTypeShortName"/>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="3px" padding-after="5px">
												<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="1px">
													<xsl:value-of select="accountBalance"/>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="3px" padding-after="5px">
												<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="1px">
													<xsl:value-of select="BD:format(vestingPercentage, 3, 3)" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="3px" padding-after="5px">
												<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="1px">
													<xsl:variable name="moneyTypeId_var">
														<xsl:value-of select="moneyTypeId" />
													</xsl:variable>
													<xsl:for-each
														select="../../loanMoneyTypeCalculation/vestedBalanceMap/item[@key=concat('vestedBalance_', $moneyTypeId_var)]">
														<xsl:value-of select="@value" />
													</xsl:for-each>
												</fo:block>
											</fo:table-cell>
											<xsl:choose>
												<xsl:when test="excludeIndicator = 'false'">
													<fo:table-cell xsl:use-attribute-sets="table_data_default_font" border-color="#e9e2c3" border-right-style="solid" border-width="0.1mm">
														<fo:block padding-before="1px" text-align="center" padding-after="5px">
															<fo:external-graphic content-height="8px" content-width="8px">
																<xsl:attribute name="src">
																	<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')" />
																</xsl:attribute>
															</fo:external-graphic>
														</fo:block>
													</fo:table-cell>
												</xsl:when>
												<xsl:otherwise>
													<fo:table-cell xsl:use-attribute-sets="table_data_default_font" border-color="#e9e2c3" border-right-style="solid" border-width="0.1mm">
														<fo:block padding-before="1px" text-align="center" padding-after="5px">
															<fo:external-graphic content-height="8px" content-width="8px">
																<xsl:attribute name="src">
																	<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')" />
																</xsl:attribute>
															</fo:external-graphic>
														</fo:block>
													</fo:table-cell>
												</xsl:otherwise>
											</xsl:choose>
											<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="3px" padding-after="5px">
												<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="1px">
													<xsl:variable name="moneyTypeId_var">
														<xsl:value-of select="moneyTypeId" />
													</xsl:variable>
													<xsl:for-each
														select="../../loanMoneyTypeCalculation/availableBalanceMap/item[@key=concat('availableBalance_', $moneyTypeId_var)]">
														<xsl:value-of select="@value" />
													</xsl:for-each>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
							<xsl:choose>
								<xsl:when test="count(loan/moneyTypes/loanMoneyType) mod 2 != 0">
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" border-bottom-style="solid" padding-after="3px" padding-before="3px">
											<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">Total</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" border-bottom-style="solid" padding-end="3px" padding-before="3px" padding-after="3px">
											<fo:block xsl:use-attribute-sets="table_data_default_font" text-align="right">
												<xsl:value-of select="loanMoneyTypeCalculation/totalAccountBalance"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" border-bottom-style="solid">
											<fo:block xsl:use-attribute-sets="table_data_default_font"></fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" border-bottom-style="solid" padding-end="3px" padding-before="3px" padding-after="3px">
											<fo:block xsl:use-attribute-sets="table_data_default_font" text-align="right">
												<xsl:value-of select="loanMoneyTypeCalculation/totalVestedBalance"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" border-bottom-style="solid">
											<fo:block xsl:use-attribute-sets="table_data_default_font"></fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" border-bottom-style="solid" padding-end="3px" padding-before="3px" padding-after="3px">
											<fo:block xsl:use-attribute-sets="table_data_default_font" text-align="right">
												<xsl:value-of select="loanMoneyTypeCalculation/totalAvailableBalance"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:when>
								<xsl:otherwise>
									<fo:table-row xsl:use-attribute-sets="table_cell_background_style">
										<fo:table-cell xsl:use-attribute-sets="table_sub_header_default_bold_font" padding-after="3px">
											<fo:block>Total</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="3px" padding-before="3px" padding-after="3px">
											<fo:block xsl:use-attribute-sets="table_data_default_font" text-align="right">
												<xsl:value-of select="loanMoneyTypeCalculation/totalAccountBalance"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
											<fo:block></fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="3px" padding-before="3px" padding-after="3px">
											<fo:block xsl:use-attribute-sets="table_data_default_font" text-align="right">
												<xsl:value-of select="loanMoneyTypeCalculation/totalVestedBalance"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
											<fo:block></fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="3px" padding-before="3px" padding-after="3px">
											<fo:block xsl:use-attribute-sets="table_data_default_font" text-align="right">
												<xsl:value-of select="loanMoneyTypeCalculation/totalAvailableBalance"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:otherwise>
							</xsl:choose>
						</fo:table-body>
					</fo:table>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block></fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="60%" />
						<fo:table-column column-width="40%" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="table_cell_bold_border_style" number-columns-spanned="2">
									<fo:block>Maximum loan permitted </fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="table_sub_header_default_bold_font" padding-start="11px">
									<fo:block>
										Highest loan balance in the last 12 months
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-end="3px" padding-before="6px">
									<fo:block text-align="right" padding-before="3px">
										$<xsl:value-of select="loan/maxBalanceLast12Months"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="table_sub_header_default_bold_font" padding-start="11px">
									<fo:block padding-before="5px">
										Outstanding loans
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-end="3px">
									<fo:block text-align="right" padding-before="6px">
										<xsl:value-of select="loan/outstandingLoansCount"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="table_sub_header_default_bold_font" padding-start="11px">
									<fo:block padding-before="5px">
										Outstanding balance
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-end="3px">
									<fo:block text-align="right" padding-before="6px">
										$<xsl:value-of select="loan/currentOutstandingBalance"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="2" padding-start="8px">
									<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font"
										padding-before="5px">Maximum loan permitted </fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="2">
									<fo:block text-align="center" xsl:use-attribute-sets="table_data_default_font">
										<xsl:value-of select="loanMoneyTypeCalculation/maxLoanAvailableSpan"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<xsl:if test="not(loanForm/displayIRSlabel = 'true')">
							<fo:table-row>
								<fo:table-cell number-columns-spanned="2" padding-start="8px" padding-before="5px">
									<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
										<fo:inline padding-end="1px">
											<xsl:choose>
												<xsl:when test="loan/applyIrs10KDollarRuleInd = 'false'">
													<fo:external-graphic content-height="6px" content-width="6px">
													<xsl:attribute name="src">
														<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')" />
													</xsl:attribute>
													</fo:external-graphic>
												</xsl:when>
												<xsl:otherwise>
													<fo:external-graphic content-height="6px" content-width="6px">
													<xsl:attribute name="src">
														<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')" />
													</xsl:attribute>
													</fo:external-graphic>
												</xsl:otherwise>
											</xsl:choose>
										</fo:inline>
										<fo:inline>
											Apply $10,000 IRS rule
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							</xsl:if>
						</fo:table-body>
					</fo:table>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block></fo:block>
				</fo:table-cell>
			</fo:table-row>
	
			<fo:table-row>
				<fo:table-cell padding-after="15px" number-columns-spanned="4">
					<fo:block>
						<xsl:value-of select="cmaContent/calculateMaxLoanAvailableFooterText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
	</fo:block>
			
	</xsl:template>
</xsl:stylesheet>
