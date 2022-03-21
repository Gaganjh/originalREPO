<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:include href="../CommonReport.xsl"/>
	
	<xsl:attribute-set name="table_data_default_font">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="padding-before">3px</xsl:attribute>
		<xsl:attribute name="space-before">20px</xsl:attribute>
		<xsl:attribute name="padding-start">6px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="assethouse_group_name">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>	
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="color">#CD5806</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_sub_header_text_bold_font" > 
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="padding-start">6px</xsl:attribute>
		<xsl:attribute name="padding-before">5px</xsl:attribute>
		<xsl:attribute name="space-before">20px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="withdrawal_text_right_align_font">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="text-align">right</xsl:attribute>
		<xsl:attribute name="padding-before">3px</xsl:attribute>
		<xsl:attribute name="space-before">15px</xsl:attribute>
		<xsl:attribute name="border-color">#e9e2c3</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
		<xsl:attribute name="padding-after">3px</xsl:attribute>
		<xsl:attribute name="padding-end">3px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="withdrawal_bold_text_right_align_font">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="text-align">right</xsl:attribute>
		<xsl:attribute name="padding-start">4px</xsl:attribute>
		<xsl:attribute name="padding-before">5px</xsl:attribute>
		<xsl:attribute name="space-before">20px</xsl:attribute>
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="border-color">#ffffff</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
		<xsl:attribute name="padding-end">3px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="border_right_style">
		<xsl:attribute name="border-color">#e9e2c3</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
		<xsl:attribute name="padding-start">6px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="square_check_box_style">
		<xsl:attribute name="font-family">ZapfDingbats</xsl:attribute>
		<xsl:attribute name="font-size">2pt</xsl:attribute>
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="background-color">#000000</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="withdrawal_Notes_section_font_layout">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="padding-start">4px</xsl:attribute>
		<xsl:attribute name="padding-before">3px</xsl:attribute>
		<xsl:attribute name="padding-after">3px</xsl:attribute>
		<xsl:attribute name="space-before">20px</xsl:attribute>
		<xsl:attribute name="border-color">#e9e2c3</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="border_bottom_style_layout">
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-color">#e9e2c3</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_cell_border_style" use-attribute-sets="table_sub_header_text_bold_font">
		<xsl:attribute name="border-color">#ffffff</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
		<xsl:attribute name="padding-start">4px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="header_border_left_style">
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="background-color">#cccccc</xsl:attribute>
		<xsl:attribute name="border-color">#ffffff</xsl:attribute>
		<xsl:attribute name="border-left-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
		<xsl:attribute name="padding-start">4px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_text_right_align_font" use-attribute-sets="table_cell_border_style">
		<xsl:attribute name="text-align">right</xsl:attribute>
		<xsl:attribute name="padding-end">3px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_row_text_color" >
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="background-color">#cccccc</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_row_background-color" >
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="background-color">#e9e2c3</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_row_withdrawal_default_font" use-attribute-sets="table_data_default_font">
		<xsl:attribute name="number-columns-spanned">6</xsl:attribute>
		<xsl:attribute name="padding-start">6px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_cell_inline_withdrawal_default_font" use-attribute-sets="table_data_default_font">
		<xsl:attribute name="padding-start">90px</xsl:attribute>
		<xsl:attribute name="space-after">15px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_cell_first_inline_withdrawal_default_bold_font" use-attribute-sets="table_sub_header_text_bold_font">
		<xsl:attribute name="padding-start">4px</xsl:attribute>
		<xsl:attribute name="padding-after">25px</xsl:attribute>
		<xsl:attribute name="space-after">15px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_cell_second_inline_withdrawal_default_bold_font" use-attribute-sets="table_sub_header_text_bold_font">
		<xsl:attribute name="padding-start">10px</xsl:attribute>
		<xsl:attribute name="start-indent">20px</xsl:attribute>
		<xsl:attribute name="padding-after">20px</xsl:attribute>
		<xsl:attribute name="space-after">15px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_cell_withdrawal_Notes_section_font_layout" use-attribute-sets="withdrawal_Notes_section_font_layout">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="withdrawal_text_right_align_font_layout">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="text-align">right</xsl:attribute>
		<xsl:attribute name="padding-before">3px</xsl:attribute>
		<xsl:attribute name="space-before">15px</xsl:attribute>
		<xsl:attribute name="padding-after">3px</xsl:attribute>
		<xsl:attribute name="padding-end">3px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_cell_inline_default_font" use-attribute-sets="table_data_default_font">
		<xsl:attribute name="padding-start">60px</xsl:attribute>
		<xsl:attribute name="space-after">15px</xsl:attribute>
	</xsl:attribute-set>
	

	
	<!-- Global Variables -->
	<xsl:variable name="statusPath">
		<xsl:value-of select='withdrawalData/lookupData/WITHDRAWAL_REQUEST_STATUS_ORDERED'/>
	</xsl:variable>
	<xsl:variable name="statusCodePath">
		<xsl:value-of select='withdrawalData/withdrawalRequestUi/withdrawalRequest/statusCode'/>
	</xsl:variable>
		<xsl:variable name="paymentTo_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/withdrawalRequest/paymentTo" />
	</xsl:variable>
	<xsl:variable name="eftOrganizationName_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/recipients/withdrawalRequestRecipientUi/payees/WithdrawalRequestPayeeUi/eftOrganizationName"/>
	</xsl:variable>

	<xsl:variable name="showStaticContent_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/showStaticContent"/>
	</xsl:variable>
	<xsl:variable name="showAvailableAmountColumn_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/showAvailableWithdrawalAmountColumn"/>
	</xsl:variable>
	<xsl:variable name="showPortionOfAvailableAmtPercentColumn_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/showRequestedWithdrawalAmountPercentColumn"/>
	</xsl:variable>
	<xsl:variable name="totalRequestedWithdrawalAmount_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/totalRequestedWithdrawalAmount"/>
	</xsl:variable>
	<xsl:variable name="showTotalRequestedWithdrawalAmount_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/showTotalRequestedWithdrawalAmount"/>
	</xsl:variable>
	<xsl:variable name="totalAvailableWithdrawalAmount_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/totalAvailableWithdrawalAmount"/>
	</xsl:variable>
	<xsl:variable name="isParticipantInitiated_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/isParticipantInitiated"/>
	</xsl:variable>
	<xsl:variable name="imagePath_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/unmangedImagePath"/>
	</xsl:variable>
	<xsl:variable name="paymentInstructionsTitle_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/cmaContent/paymentInstructionsSectionTitle"/>
	</xsl:variable>
	<xsl:variable name="tax1099rTitle_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/cmaContent/tax1099rSectionTitle"/>
	</xsl:variable>
	<xsl:variable name="eftPayeeTitle_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/cmaContent/eftPayeeSectionTitle"/>
	</xsl:variable>
	<xsl:variable name="overrideCsfMailText_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/cmaContent/overrideCsfMailText"/>
	</xsl:variable>
	<xsl:variable name="mailChequeToAddressIndicator_var">
		<xsl:value-of select="withdrawalData/withdrawalRequestUi/withdrawalRequest/contractInfo/mailChequeToAddressIndicator"/>
	</xsl:variable>
	<xsl:variable name="payeeStatus_count_var">
		<xsl:value-of select="count(withdrawalData/withdrawalRequestUi/recipients/withdrawalRequestRecipientUi/payees/withdrawalRequestPayeeUi/withdrawalRequestPayee)"/>
	</xsl:variable>
	

	<xsl:include href="GeneralInformation.xsl"/>
	<xsl:include href="ParticipantInformation.xsl"/>
	<xsl:include href="BasicInformation.xsl"/>
	<xsl:include href="LoanDetails.xsl"/>
	<xsl:include href="AmountDetails.xsl"/>
	<xsl:include href="PaymentInstructions.xsl"/>
	<xsl:include href="1099RInformation.xsl"/>
	<xsl:include href="Notes.xsl"/>
	<xsl:include href="Declarations.xsl"/>
	<xsl:include href="WithdrawalPageFooter.xsl"/>


	<xsl:template match="withdrawalData">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="WithdrawalLayout"
					page-width="8.5in" page-height="11in" margin-top="18px"
					margin-bottom="18px">
					<fo:region-body margin-left="70px" margin-right="70px" margin-bottom="20px"/>
					<fo:region-before extent="22px" precedence="true" />
					<fo:region-after extent="72px" precedence="true" />
				</fo:simple-page-master>
			</fo:layout-master-set>
					
			<fo:page-sequence master-reference="WithdrawalLayout">
				<fo:static-content flow-name="xsl-region-after">
					<fo:block xsl:use-attribute-sets="page_count" padding-before="60px" start-indent="490px">
						Page <fo:page-number/> of <fo:page-number-citation-last ref-id="terminator"/>
					</fo:block>
					<fo:block xsl:use-attribute-sets="page_level_footer_text">
                        <fo:inline>&#169;&#160;</fo:inline>
						<fo:inline>2012 John Hancock. All rights reserved.</fo:inline>
                    </fo:block>
				</fo:static-content>
			
				<fo:flow flow-name="xsl-region-body">
					
					<!-- Call templates of included XSL -->
					
					<xsl:call-template name="GeneralInformation"/>
					<!-- Call templates of included Participant Information XSL -->
					<xsl:call-template name="ParticipantInformation"/>
					<!-- Call templates of included Basic Information XSL -->
					<xsl:call-template name="BasicInformation"/>
					
					
					<!-- Call templates of included LoanDe tails XSL -->
					<xsl:if test="count(withdrawalRequestUi/withdrawalRequest/loans/withdrawalRequestLoan) &gt; 0">
						<xsl:call-template name="LoanDetails"/>
					</xsl:if> 
					
					<!-- Call templates of included Withdrawal Amount XSL -->
					<xsl:call-template name="WithdrawalAmount"/>
					
					<!-- Here we have to use 'For Each' tag to display all recipients from withdrawalRequestUi -->
					<!-- Begin Recipient Loop -->
					
					<xsl:for-each select="withdrawalRequestUi/recipients/withdrawalRequestRecipientUi">
					
						<!-- Call templates of included Payment Instructions XSL -->
						<xsl:call-template name="PaymentInstructions"/>
						<!-- Call templates of included Withdrawal 1099R XSL -->
						<xsl:call-template name="Withdrawal1099R"/>
						
					</xsl:for-each>
					
					<!-- End Recipient Loop -->
					
					<!-- Call templates of included Notes XSL -->
					<xsl:call-template name="WithdrawalNotes"/>
					<!-- Call templates of included Declarations XSL -->
					<xsl:call-template name="WithdrawalDeclarations"/>
					<!-- Call templates of included Withdrawal page footer XSL -->
					<xsl:call-template name="WithdrawalPageFooter"/>
					
					
					<fo:block id="terminator" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
</xsl:stylesheet>