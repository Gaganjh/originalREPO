<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java"
	xmlns:BD="com.manulife.pension.ps.web.iloans.util.BigDecimalFormatter">
	
	<xsl:template name="requestLoanCalculationSection">
	<fo:block space-before="20px" page-break-inside="avoid">
	<fo:table table-layout="fixed" width="100%">
		<fo:table-column column-width="100%" />
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets="table_header_text_font">
					<fo:block>
						<xsl:value-of select="cmaContent/loanCalcualtionsTitleText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
	<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
		<fo:table-column column-width="22%" />											
		<fo:table-column column-width="32%" />
		<fo:table-column column-width="30%" />											
		<fo:table-column column-width="16%" />
		<fo:table-body>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="table_data_border_style" padding-end="3px">
					<fo:block text-align="right"></fo:block>
				</fo:table-cell>
				<xsl:if test="displayRules/displayLoanCalculationEditable = 'true'">
				<fo:table-cell xsl:use-attribute-sets="table_data_border_style" padding-end="3px">
					<fo:block text-align="right">
						<xsl:value-of select="displayRules/loanCalculationEditableColumnHeader"/>
					</fo:block>
				</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationAcceptedColumn = 'true'">
				<fo:table-cell xsl:use-attribute-sets="table_data_border_style" padding-end="3px">
					<fo:block text-align="right">
						<xsl:value-of select="displayRules/loanCalculationAcceptedColumnHeader"/>
					</fo:block>
				</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationReviewedColumn = 'true'">
				<fo:table-cell xsl:use-attribute-sets="table_data_border_style" padding-end="3px">
					<fo:block text-align="right">
						<xsl:value-of select="displayRules/loanCalculationReviewedColumnHeader"/>
					</fo:block>
				</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationOriginalColumn = 'true'">
				<fo:table-cell xsl:use-attribute-sets="table_data_border_style" padding-end="3px">
					<fo:block text-align="right">
						<xsl:value-of select="displayRules/loanCalculationOriginalColumnHeader"/>
					</fo:block>
				</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationBlankColumn = 'true'">
				<fo:table-cell xsl:use-attribute-sets="table_data_with_no_border" padding-end="3px">
					<fo:block text-align="right"></fo:block>
				</fo:table-cell>
				</xsl:if>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						Maximum loan permitted 
					</fo:block>
				</fo:table-cell>
				
				<xsl:if test="displayRules/displayLoanCalculationEditable = 'true'">
					<xsl:choose>
						<xsl:when test="displayRules/loanCalculationEditable = 'true'">
							<fo:table-cell  xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px" >
								<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
									<xsl:value-of select="loanMoneyTypeCalculation/maxLoanAvailableSpan"/> 
								</fo:block>
							</fo:table-cell>
						</xsl:when>
						<xsl:otherwise>
							<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
								<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
									$<xsl:value-of select="currentLoanParameter/maximumAvailable"/> 
								</fo:block>
							</fo:table-cell>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				
				<xsl:if test="displayRules/displayLoanCalculationAcceptedColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px" >
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							$<xsl:value-of select="loan/acceptedParameter/maximumAvailable"/> 
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationReviewedColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px" >
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							$<xsl:value-of select="loan/reviewedParameter/maximumAvailable"/> 
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationOriginalColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							$<xsl:value-of select="loan/originalParameter/maximumAvailable"/> 
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationBlankColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font"></fo:block>
					</fo:table-cell>
				</xsl:if>
			</fo:table-row>
			<fo:table-row xsl:use-attribute-sets="table_cell_background_style" >
				<fo:table-cell xsl:use-attribute-sets="table_sub_header_default_bold_font" >
					<fo:block >
						Requested amount  
					</fo:block>
				</fo:table-cell>
				<xsl:if test="displayRules/displayLoanCalculationEditable = 'true'">
					<xsl:if test="displayRules/loanAmountDisplayOnly = 'true'">
						<fo:table-cell padding-end="1px">
							<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
								$<xsl:value-of select="currentLoanParameter/loanAmount"/> 
							</fo:block>
						</fo:table-cell>
					</xsl:if>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationAcceptedColumn = 'true'">
					<fo:table-cell padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						 	$<xsl:value-of select="loan/acceptedParameter/loanAmount"/> 
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationReviewedColumn = 'true'">
					<fo:table-cell padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						 	$<xsl:value-of select="loan/reviewedParameter/loanAmount"/> 
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationOriginalColumn = 'true'">
					<fo:table-cell padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						 	$<xsl:value-of select="loan/originalParameter/loanAmount"/> 
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationBlankColumn = 'true'">
					<fo:table-cell padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						</fo:block>
					</fo:table-cell>
				</xsl:if>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" >
					<fo:block  xsl:use-attribute-sets="table_sub_header_default_bold_font">
						Amortization period  
					</fo:block>
				</fo:table-cell>
				<xsl:if test="displayRules/displayLoanCalculationEditable = 'true'">
					<xsl:if test="displayRules/loanCalculationEditable = 'false'">
						<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
							<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							<fo:inline><xsl:value-of select="format-number(floor(currentLoanParameter/amortizationMonths div 12),'#')"/>&#160;year(s)&#160;</fo:inline>
							<fo:inline><xsl:value-of select="currentLoanParameter/amortizationMonths mod 12"/>&#160;month(s)</fo:inline>
							</fo:block>
						</fo:table-cell>
					</xsl:if>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationAcceptedColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						<fo:inline><xsl:value-of select="format-number(floor(loan/acceptedParameter/amortizationMonths div 12)
						, '#')"/>&#160;year(s)&#160;</fo:inline>
						<fo:inline><xsl:value-of select="loan/acceptedParameter/amortizationMonths mod 12"/>&#160;month(s)</fo:inline>
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationReviewedColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						<fo:inline><xsl:value-of select="format-number(floor(loan/reviewedParameter/amortizationMonths div 12)
						, '#')"/>&#160;year(s)&#160;</fo:inline>
						<fo:inline><xsl:value-of select="loan/reviewedParameter/amortizationMonths mod 12"/>&#160;month(s)</fo:inline>
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationOriginalColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						<fo:inline><xsl:value-of select="format-number(floor(loan/originalParameter/amortizationMonths div 12)
						, '#')"/>&#160;year(s)&#160;</fo:inline>
						<fo:inline><xsl:value-of select="loan/originalParameter/amortizationMonths mod 12"/>&#160;month(s)</fo:inline>
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationBlankColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font">
						</fo:block>
					</fo:table-cell>
				</xsl:if>
			</fo:table-row>
			<fo:table-row xsl:use-attribute-sets="table_cell_background_style" >
				<fo:table-cell xsl:use-attribute-sets="table_sub_header_default_bold_font" >
					<fo:block>
						Payment amount   
					</fo:block>
				</fo:table-cell>
				<xsl:if test="displayRules/displayLoanCalculationEditable = 'true'">
					<xsl:if test="displayRules/loanCalculationEditable = 'false'">
						<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
							<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
								$<xsl:value-of select="currentLoanParameter/paymentAmount"/>
							</fo:block>
						</fo:table-cell>
					</xsl:if>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationAcceptedColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						$<xsl:value-of select="loan/acceptedParameter/paymentAmount"/>
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationReviewedColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						$<xsl:value-of select="loan/reviewedParameter/paymentAmount"/>
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationOriginalColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						$<xsl:value-of select="loan/originalParameter/paymentAmount"/>
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationBlankColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						</fo:block>
					</fo:table-cell>
				</xsl:if>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						Payment frequency     
					</fo:block>
				</fo:table-cell>
				<xsl:if test="displayRules/displayLoanCalculationEditable = 'true'">
					<xsl:if test="displayRules/loanCalculationEditable = 'false'">
						<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
							<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							<xsl:variable name="paymentFrequency">
								<xsl:value-of select="currentLoanParameter/paymentFrequency"/>
							</xsl:variable>
							<xsl:for-each select="loanPaymentFrequencies/item[@key=$paymentFrequency]">
								<xsl:value-of select="@value"/>
							</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</xsl:if>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationAcceptedColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							<xsl:variable name="paymentFrequency">
								<xsl:value-of select="loan/acceptedParameter/paymentFrequency"/>
							</xsl:variable>
							<xsl:for-each select="loanPaymentFrequencies/item[@key=$paymentFrequency]">
								<xsl:value-of select="@value"/>
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationReviewedColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							<xsl:variable name="paymentFrequency">
								<xsl:value-of select="loan/reviewedParameter/paymentFrequency"/>
							</xsl:variable>
							<xsl:for-each select="loanPaymentFrequencies/item[@key=$paymentFrequency]">
								<xsl:value-of select="@value"/>
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationOriginalColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							<xsl:variable name="paymentFrequency">
								<xsl:value-of select="loan/originalParameter/paymentFrequency"/>
							</xsl:variable>
							<xsl:for-each select="loanPaymentFrequencies/item[@key=$paymentFrequency]">
								<xsl:value-of select="@value"/>
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				<xsl:if test="displayRules/displayLoanCalculationBlankColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						</fo:block>
					</fo:table-cell>
				</xsl:if>
			</fo:table-row>
			<fo:table-row xsl:use-attribute-sets="table_cell_background_style" >
				<fo:table-cell xsl:use-attribute-sets="table_sub_header_default_bold_font" >
					<fo:block >
						Interest rate    
					</fo:block>
				</fo:table-cell>
				
				<xsl:if test="displayRules/displayLoanCalculationEditable = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							<xsl:value-of select="BD:format(currentLoanParameter/interestRate, 3, 3)"/>%
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				
				<xsl:if test="displayRules/displayLoanCalculationAcceptedColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							<xsl:value-of select="BD:format(loan/acceptedParameter/interestRate, 3, 3)"/>%
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				
				<xsl:if test="displayRules/displayLoanCalculationReviewedColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							<xsl:value-of select="BD:format(loan/reviewedParameter/interestRate, 3, 3)"/>%
						</fo:block>
					</fo:table-cell>
				</xsl:if>
					
				<xsl:if test="displayRules/displayLoanCalculationOriginalColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
							<xsl:value-of select="BD:format(loan/originalParameter/interestRate, 3, 3)"/>%
						</fo:block>
					</fo:table-cell>
				</xsl:if>
				
				<xsl:if test="displayRules/displayLoanCalculationBlankColumn = 'true'">
					<fo:table-cell xsl:use-attribute-sets="sub_header_border_right_style" padding-end="1px">
						<fo:block text-align="right" xsl:use-attribute-sets="table_data_default_font" padding-before="2px" padding-after="3px">
						</fo:block>
					</fo:table-cell>
				</xsl:if>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
	</fo:block>
	</xsl:template>
</xsl:stylesheet>