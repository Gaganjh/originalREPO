<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:attribute-set name="assethouse_group_name">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>	
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="color">#CD5806</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_data_default_font">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-start">1px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_sub_header_default_bold_font">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="padding-start">2px</xsl:attribute>
		<xsl:attribute name="padding-before">3px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="border_right-style">
		<xsl:attribute name="border-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.2mm</xsl:attribute>
		<xsl:attribute name="padding-start">1px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="border_right_with_no_padding">
		<xsl:attribute name="border-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_data_border_style" use-attribute-sets="table_sub_header_default_bold_font">
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="background-color">#cccccc</xsl:attribute>
		<xsl:attribute name="padding-start">2px</xsl:attribute>
		<xsl:attribute name="border-color">#ffffff</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.2mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="sub_header_border_right_style">
		<xsl:attribute name="border-color">#e9e2c3</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.2mm</xsl:attribute>
		<xsl:attribute name="padding-start">1px</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table_cell_background_style" >
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="background-color">#e9e2c3</xsl:attribute>
		
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_cell_bold_border_style" use-attribute-sets="table_sub_header_default_bold_font">
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="background-color">#cccccc</xsl:attribute>
		<xsl:attribute name="padding-start">1px</xsl:attribute>
		<xsl:attribute name="border-color">#ffffff</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_data_with_no_border" use-attribute-sets="table_sub_header_default_bold_font">
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="background-color">#cccccc</xsl:attribute>
		<xsl:attribute name="padding-start">2px</xsl:attribute>
		<xsl:attribute name="border-color">#cccccc</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.2mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:variable name="imagePath_var">
		<xsl:value-of select='loanData/unmangedImagePath'/>
	</xsl:variable>
	
	<xsl:include href="../CommonReport.xsl" />
	<xsl:include href="generalInformation.xsl" />
	<xsl:include href="requestNotesViewDetails.xsl" />
	<xsl:include href="participantInformation.xsl" />
	<xsl:include href="loanDetails.xsl" />
	<xsl:include href="calculateMaxLoanAvailablity.xsl" />
	<xsl:include href="requestLoanCalculation.xsl" />
	<xsl:include href="loanRequestPaymentInstructions.xsl" />
	<xsl:include href="loanRequestDeclarations.xsl" />
	<xsl:include href="../CommonPageFooter.xsl" />

	<xsl:template match="loanData">
	
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
		<fo:layout-master-set>
			<fo:simple-page-master master-name="ViewLoanRequestLayout"
				page-width="8.5in" page-height="11in" margin-top="18px"
				margin-bottom="18px">
				<fo:region-body margin-left="70px" margin-right="70px" margin-bottom="18px"/>
				<fo:region-before extent="22px" precedence="true" />
				<fo:region-after extent="72px" precedence="true" />
			</fo:simple-page-master>
		</fo:layout-master-set>
		
		<fo:page-sequence master-reference="ViewLoanRequestLayout">
			<fo:static-content flow-name="xsl-region-after">
				<fo:block xsl:use-attribute-sets="page_count" padding-before="60px" start-indent="510px">
					Page <fo:page-number/> of <fo:page-number-citation-last ref-id="terminator"/>
				</fo:block>
				<fo:block xsl:use-attribute-sets="page_level_footer_text">
                    <fo:inline>&#169;&#160;</fo:inline>
					<fo:inline>2012 John Hancock. All rights reserved.</fo:inline>
                </fo:block>
			</fo:static-content>
			
			<fo:flow flow-name="xsl-region-body">
				 <!-- Call template to display General Information-->
				<xsl:call-template name="generalInformation" />
				<!-- The following template will call when ${displayRules.displayNotesViewSection} is true.-->
				<xsl:if test="displayRules/displayNotesViewSection = 'true'">
					<xsl:call-template name="RequestNotesViewDetails" />
				</xsl:if>
				<!-- Call template to display Participant information details Section-->
				<xsl:call-template name="ParticipantInformation" />
				<!-- Call template to display loan details Section-->
				<xsl:call-template name="loanDetailsSection" />
				<!-- Call template to display Calculate maximum amount available for loan Section-->
				<xsl:call-template name="loanRequestCalculateMaxLoanAvailableSection" />
				<!-- Call template to display Loan calculations Section-->
				<xsl:call-template name="requestLoanCalculationSection" />
				<xsl:if test="displayRules/displayPaymentInstructionSection = 'true'">
					<xsl:call-template name="loanRequestPaymentInstructionsSection" />
				</xsl:if>
				<xsl:if test="displayRules/showDeclarationsSection = 'true'">
					<xsl:call-template name="loanRequestDeclarationsSection" />
				</xsl:if>
				<xsl:call-template name="CommonPageFooter" />
				<fo:block id="terminator" />
			</fo:flow>
		</fo:page-sequence>
	</fo:root>
	</xsl:template>
</xsl:stylesheet>