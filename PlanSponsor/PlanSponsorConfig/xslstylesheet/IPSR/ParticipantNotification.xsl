<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:variable name="lang.classHeading" select="'Asset Class'" />
	<xsl:variable name="lang.fundHeading" select="'Current Fund'" />
	<xsl:variable name="lang.newFundHeading" select="'New Fund'" />

	<xsl:attribute-set name="header_default_font">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="font-size">11pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="static_content_10pt_bold_white">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		
	</xsl:attribute-set>
		<xsl:attribute-set name="static_content_9pt_white">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="text-align">justify</xsl:attribute>
		
	</xsl:attribute-set>
	<xsl:attribute-set name="static_content">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
		<xsl:attribute name="font-weight">reg</xsl:attribute>
		<xsl:attribute name="text-align">justify</xsl:attribute>
		
	</xsl:attribute-set>
	<xsl:attribute-set name="table_header">
		<xsl:attribute name="border-start-color">#000000</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#000000</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#000000</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-top-color">#000000</xsl:attribute>
		<xsl:attribute name="border-top-style">solid</xsl:attribute>
		<xsl:attribute name="border-top-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-bottom-color">#000000</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">0.2mm</xsl:attribute>
		<xsl:attribute name="color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="background-color">#4F6228</xsl:attribute>
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="column_header">
		<xsl:attribute name="border-start-color">#000000</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#000000</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#000000</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-top-color">#000000</xsl:attribute>
		<xsl:attribute name="border-top-style">solid</xsl:attribute>
		<xsl:attribute name="border-top-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-bottom-color">#000000</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">0.2mm</xsl:attribute>
		<xsl:attribute name="color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		
	</xsl:attribute-set>
	<xsl:attribute-set name="table_cell">
		<xsl:attribute name="border-start-color">#000000</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#000000</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#000000</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-top-color">#000000</xsl:attribute>
		<xsl:attribute name="border-top-style">solid</xsl:attribute>
		<xsl:attribute name="border-top-width">0.2mm</xsl:attribute>
		<xsl:attribute name="border-bottom-color">#000000</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">0.2mm</xsl:attribute>
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
		<xsl:attribute name="font-weight">reg</xsl:attribute>
		<xsl:attribute name="text-align">justify</xsl:attribute>
		
	</xsl:attribute-set>
	
	<xsl:attribute-set name="word_title">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
	</xsl:attribute-set>


	<xsl:include href="ParticipantNotification_en.xsl" />

	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="ParticipantNotificationLayout"
					page-width="8.5in" page-height="11in" margin-top="18px"
					margin-bottom="18px">
					<fo:region-body margin-left="70px" margin-right="70px" />
					<fo:region-before extent="22px" precedence="true" />
					<fo:region-after extent="72px" precedence="true" />
				</fo:simple-page-master>
			</fo:layout-master-set>

			<xsl:call-template name="ParticipantNotification" />

		</fo:root>
	</xsl:template>
</xsl:stylesheet>