<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<!-- fonts -->
	<xsl:attribute-set name="frutiger_font.size_7">
		<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
		<xsl:attribute name="font-size">7pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_8">
		<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_8.5">
		<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
		<xsl:attribute name="font-size">8.5pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_10">
		<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_9">
		<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_9_bold">
		<xsl:attribute name="font-family">Frutiger67BoldCn</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_10_bold">
		<xsl:attribute name="font-family">Frutiger65Bold</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_9_black">
		<xsl:attribute name="font-family">Frutiger77BlackCn</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.italic_9_bold">
		<xsl:attribute name="font-family">Frutiger66BoldItalic</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_11">
		<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
		<xsl:attribute name="font-size">11pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_11_black">
		<xsl:attribute name="font-family">Frutiger77BlackCn</xsl:attribute>
		<xsl:attribute name="font-size">11pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_13">
		<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
		<xsl:attribute name="font-size">13pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_16">
		<xsl:attribute name="font-family">Frutiger47LightCn</xsl:attribute>
		<xsl:attribute name="font-size">16pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="frutiger_font.size_24">
		<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
		<xsl:attribute name="font-size">24pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:include href="Fee_disclosure_expense_and_investment_related_charges.xsl" />

	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
		
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4" page-width="8.5in" page-height="11in" margin-top="7mm" margin-bottom="0mm" margin-left="23px" margin-right="23px">
					<fo:region-body margin-left=".5cm" margin-top="1cm" margin-right=".5cm" margin-bottom="2cm" />
					<fo:region-before extent="1.5cm" />
					<fo:region-after extent="1.5cm" />
					<fo:region-start extent=".5cm" />
					<fo:region-end extent=".7cm" />
				</fo:simple-page-master>				
			</fo:layout-master-set>
			
			<xsl:call-template name="fee_section_seven" />
		</fo:root>
	</xsl:template>
	
</xsl:stylesheet>