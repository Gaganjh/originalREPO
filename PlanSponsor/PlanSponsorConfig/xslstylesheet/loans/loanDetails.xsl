<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<xsl:template name="loanDetailsSection">
	
	<fo:block space-before="20px" page-break-inside="avoid">
		<fo:table table-layout="fixed" width="100%">
		<fo:table-column column-width="100%" />
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets="table_header_text_font">
					<fo:block>
						<xsl:value-of select="cmaContent/loanDetalsTitleText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
		</fo:table>
		
		<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
		<fo:table-column column-width="30%" />											
		<fo:table-column column-width="25%" />
		<fo:table-column column-width="30%" />											
		<fo:table-column column-width="15%" />
		<fo:table-body>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						Loan type
					</fo:block>
				</fo:table-cell>
				<fo:table-cell padding-start="1px" number-columns-spanned="3">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						 <xsl:value-of select="loanType"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						Loan reason
					</fo:block>
					<fo:block font-size="8pt" font-family="Frutiger 45 Light" font-weight="bold">
						(max. 250 characters)
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  padding-start="1px" number-columns-spanned="3">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="loan/loanReason"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style"  >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						Request date  
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="loanForm/requestDate"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style" >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						Expiration date 
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="loan/expirationDate"/> 
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font" >
						Estimated loan start date   
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="loanForm/startDate"/> 
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style" >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font" >
						Estimated loan maturity date  
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="loanForm/maturityDate"/>   
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font" >
						Next payroll date    
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="loan/firstPayrollDate"/>  
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font" >
					Maximum amortization period   
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<fo:inline><xsl:value-of select="loan/maximumAmortizationYears"/>&#160;&#160;</fo:inline>
						<fo:inline>years</fo:inline>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			
			<xsl:if test="displayRules/displayTpaLoanIssueFee = 'true'">
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						TPA loan issue fee    
					</fo:block>
				</fo:table-cell>
				<fo:table-cell padding-start="1px" number-columns-spanned="3">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						$<xsl:value-of select="loanForm/tpaLoanFee"/>   
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						Default provision    
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
					<fo:block></fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="table_sub_header_default_bold_font" >
					<fo:block></fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
					<fo:block></fo:block>
				</fo:table-cell>
			</fo:table-row>
			
			<xsl:if test="displayRules/displayDefaultProvisionExplanation = 'true'">
			<fo:table-row >
				<fo:table-cell number-columns-spanned="4"  padding-after="2px" padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="1.5px">
						<xsl:value-of select="cmaContent/defaultProvisionExplanationText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			
			<fo:table-row >
				<fo:table-cell number-columns-spanned="4"  padding-after="2px" padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="1.5px">
						<xsl:value-of select="loan/defaultProvision"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
		</fo:table>
	</fo:block>
	</xsl:template>
</xsl:stylesheet>