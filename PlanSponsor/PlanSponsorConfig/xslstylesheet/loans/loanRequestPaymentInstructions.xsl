<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java"
	xmlns:zip="com.manulife.util.render.ZipCodeRender">
	
	<xsl:template name="loanRequestPaymentInstructionsSection">
	<fo:block space-before="20px" page-break-inside="avoid">
	<fo:table table-layout="fixed" width="100%">
		<fo:table-column column-width="100%" />
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets="table_header_text_font">
					<fo:block>
						<xsl:value-of select="cmaContent/paymentInstructionsSectionTitleText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
	
	<xsl:variable name="paymentMethodCode">
		<xsl:value-of select="loan/recipient/payees/loanPayee/paymentMethodCode"/>
	</xsl:variable>
	<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
		<fo:table-column column-width="30%" />											
		<fo:table-column column-width="70%" />
		<fo:table-body>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" >
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">Payment method </fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style" >
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:for-each select="paymentMethods/item[@key=$paymentMethodCode]">
							<xsl:value-of select="@value"/>
						</xsl:for-each> 
					</fo:block>
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="1px"  padding-after="2px">
						<xsl:value-of select="cmaContent/wireChargesContentText"/> 
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="table_data_border_style" >
					<fo:block>Payee information </fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="table_data_with_no_border">
					<fo:block> </fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="4px">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">Name </fo:block>
				</fo:table-cell>
				<fo:table-cell padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<fo:inline><xsl:value-of select="loanParticipantData/firstName"/>&#160;</fo:inline>
						<fo:inline><xsl:value-of select="loanParticipantData/middleInitial"/>&#160;</fo:inline>
						<fo:inline><xsl:value-of select="loanParticipantData/lastName"/></fo:inline>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="4px">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">Address line 1 </fo:block>
				</fo:table-cell>
				<fo:table-cell padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="loan/recipient/address/addressLine1"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell  xsl:use-attribute-sets="border_right-style" padding-start="4px">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font" >Address line 2 </fo:block>
				</fo:table-cell>
				<fo:table-cell padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px"> 
						<xsl:value-of select="loan/recipient/address/addressLine2"/>  
					</fo:block>
				</fo:table-cell>
			</fo:table-row >
			<fo:table-row >
					<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="4px">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font"> City </fo:block>
					</fo:table-cell>
					<fo:table-cell padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="loan/recipient/address/city"/>   
					</fo:block>
					</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="4px">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">State </fo:block>
				</fo:table-cell>
				<fo:table-cell padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="displayRules/stateName"/> 
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="4px">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font"> Country  </fo:block>
				</fo:table-cell>
				<fo:table-cell padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="displayRules/countryName"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="4px">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">Zip code </fo:block>
				</fo:table-cell>
				<fo:table-cell padding-start="1px" padding-after="2px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:variable name="zipCode1_var">
							<xsl:value-of select="loan/recipient/address/zipCode1" />
						</xsl:variable>
						<xsl:variable name="zipCode2_var">
							<xsl:value-of select="loan/recipient/address/zipCode2" />
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$zipCode1_var != '' and $zipCode2_var != '' and displayRules/countryUSA = 'true'">
								<xsl:value-of select="zip:format(concat($zipCode1_var, $zipCode2_var), '')"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="concat($zipCode1_var, $zipCode2_var)" />
							</xsl:otherwise>
						</xsl:choose>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			
			<xsl:if test="$paymentMethodCode = 'AC' or $paymentMethodCode = 'WT'">
				<fo:table-row >
					<fo:table-cell xsl:use-attribute-sets="table_data_border_style" >
						<fo:block>Bank information </fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="table_data_with_no_border">
						<fo:block> </fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row >
					<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="4px">
						<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">ABA/Routing number  </fo:block>
					</fo:table-cell>
					<fo:table-cell padding-start="1px">
						<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
							<xsl:value-of select="displayRules/abaRountingNumber"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row >
					<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="4px">
						<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">Bank name </fo:block>
					</fo:table-cell>
					<fo:table-cell padding-start="1px">
						<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
							<xsl:value-of select="loan/recipient/payees/loanPayee/paymentInstruction/bankName"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row >
					<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="4px">
						<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">Account number  </fo:block>
					</fo:table-cell>
					<fo:table-cell padding-start="1px">
						<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
							<xsl:choose>
								<xsl:when test="displayRules/showMaskedAccountNumber = 'true'">
									<xsl:value-of select="maskedAccountNumber"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="loan/recipient/payees/loanPayee/paymentInstruction/bankAccountNumber"/>	
								</xsl:otherwise>
							</xsl:choose>
							
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:if test="$paymentMethodCode = 'AC'">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="4px">
							<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font" > Account type </fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="1px" padding-after="2px">
							<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
								<xsl:variable name="bankAccountTypeCode">
									<xsl:value-of select="loan/recipient/payees/loanPayee/paymentInstruction/bankAccountTypeCode"/>
								</xsl:variable>
								<xsl:for-each select="bankAccountTypes/item[@key=$bankAccountTypeCode]">
									<xsl:value-of select="@value"/>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
			</xsl:if>
			
			<fo:table-row >
				<fo:table-cell  number-columns-spanned="2" padding-start="1px" padding-after="2px">
					<fo:block xsl:use-attribute-sets="table_data_default_font">
						<xsl:value-of select="cmaContent/paymentInstructionsFooterText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
	</fo:block>
	</xsl:template>
</xsl:stylesheet>