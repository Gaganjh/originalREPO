<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
	
	<xsl:attribute-set name="header_default_font">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="color">#8B2500</xsl:attribute>
		<xsl:attribute name="font-size">14pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="general_info_bold_font">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute> 
	</xsl:attribute-set>
	
	<xsl:attribute-set name="submission_info_bold_font" use-attribute-sets="assethouse_group_name"> 
	<xsl:attribute name="font-size">9pt</xsl:attribute>
	<xsl:attribute name="color">#C55927</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="assethouse_group_name">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>	
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="color">#CD5806</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="border_table_layout">
		<xsl:attribute name="border-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-left-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute> 
	</xsl:attribute-set>
	
	<xsl:attribute-set name="table_header_text_font">
		<xsl:attribute name="font-family">Frutiger 45 Light</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="padding-start">6px</xsl:attribute>
		<xsl:attribute name="space-before">15px</xsl:attribute>
		<xsl:attribute name="space-after">15px</xsl:attribute>
		<xsl:attribute name="padding-before">4px</xsl:attribute>
		<xsl:attribute name="padding-after">2px</xsl:attribute>
		<xsl:attribute name="background-color">#8EA5BD</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="header_border_right_style">
		<xsl:attribute name="border-color">#ffffff</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.1mm</xsl:attribute>
		<xsl:attribute name="padding-start">6px</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="page_count">
		<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="height">14px</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="page_level_footer_text" >
		<xsl:attribute name="font-family">Arial Narrow</xsl:attribute>
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>	
		<xsl:attribute name="color">#000000</xsl:attribute>
		<xsl:attribute name="border-top-style">solid</xsl:attribute>
		<xsl:attribute name="border-top-width">0.5px</xsl:attribute>
		<xsl:attribute name="start-indent">70px</xsl:attribute>
		<xsl:attribute name="end-indent">70px</xsl:attribute>
	</xsl:attribute-set>
	
	</xsl:stylesheet>