<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="LoanDetails">

	<!--Loan Details section -->
	<fo:block space-before="20px" page-break-inside="avoid">
	
		<fo:table table-layout="fixed" width="100%">
			<fo:table-column column-width="50%" />
			<fo:table-column column-width="50%" />
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell background-repeat="no-repeat"
						xsl:use-attribute-sets="table_header_text_font" number-columns-spanned="2">
						<fo:block>
							<xsl:value-of select="withdrawalRequestUi/cmaContent/loanInformation" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
				
		<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
			<fo:table-column column-width="40%" />											
			<fo:table-column column-width="60%" />
			<fo:table-body>
				<fo:table-row xsl:use-attribute-sets="table_row_text_color">
					<fo:table-cell  xsl:use-attribute-sets="header_border_right_style">
						<fo:block font-size="10pt" xsl:use-attribute-sets="table_sub_header_text_bold_font">
							Loan number
						</fo:block>
					</fo:table-cell>
					<fo:table-cell  xsl:use-attribute-sets="table_sub_header_text_bold_font">
						<fo:block font-size="10pt">
							Outstanding balance ($)
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				<xsl:for-each select="withdrawalRequestUi/withdrawalRequest/loans/withdrawalRequestLoan">
				
					<fo:table-row>
						<fo:table-cell padding-start="5px" xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="3px">
								  Loan #<xsl:value-of select="loanNo" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="4px">
							<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="3px">
								 <xsl:value-of select="outstandingLoanAmount" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				
				</xsl:for-each>
				
				<fo:table-row xsl:use-attribute-sets="border_bottom_style_layout">
					<fo:table-cell padding-start="5px" xsl:use-attribute-sets="border_right_style">
						<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" padding-after="3px">
							 Total
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding-start="4px">
						<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="3px">
							 <xsl:value-of select="withdrawalRequestUi/totalOutstandingLoanAmt" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="border_right_style">
						<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
							What should be done with all outstanding loans?
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
						<xsl:variable name="loanOption_var" >
							<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/loanOption" />
						</xsl:variable>
						<fo:block>
							<xsl:for-each select="lookupData/LOAN_OPTION_TYPE/item[@key=$loanOption_var]">
								<xsl:value-of select="@value"/>
							</xsl:for-each>	
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				<xsl:if test="withdrawalRequestUi/showIrsLoanDistribution = 'true'">
				<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="border_right_style">
						<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" padding-after="2px">
							IRS distribution code for loans
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-after="2px">
						<xsl:variable name="irsDistributionCodeLoanClosure_var" >
							<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/irsDistributionCodeLoanClosure" />
						</xsl:variable>
						<fo:block>
							<xsl:for-each select="lookupData/IRS_CODE_LOAN/item[@key=$irsDistributionCodeLoanClosure_var]">
								<xsl:value-of select="@value"/>
							</xsl:for-each>	
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				</xsl:if>
				
				<fo:table-row xsl:use-attribute-sets="border_bottom_style_layout">
					<fo:table-cell number-columns-spanned="2">
						<fo:block>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				<fo:table-row>
                   	<fo:table-cell xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
                   		<fo:block padding-after="2px" space-after="10px">
                    		 <xsl:if test="$showStaticContent_var = 'true'">
                    		 	<xsl:value-of select="withdrawalRequestUi/cmaContent/step1PageBeanBody3" />
                    		 </xsl:if>   
                   		</fo:block>
                   	</fo:table-cell>
               	</fo:table-row>
               	
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
</xsl:stylesheet>