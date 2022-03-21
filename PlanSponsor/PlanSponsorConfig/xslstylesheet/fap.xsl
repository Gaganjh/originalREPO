<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
 	>
	<xsl:attribute-set name="table-borderstyle">
       <!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
	</xsl:attribute-set>
	<xsl:attribute-set name="solid.blue.border">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="solid.blue.border.left">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-before-style">solid</xsl:attribute>
		<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="solid.blue.border.right">
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-before-style">solid</xsl:attribute>
		<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="solid.blue.border.sides">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-before-style">solid</xsl:attribute>
		<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="solid2.blue.border.sides">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.3mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-before-style">solid</xsl:attribute>
		<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.3mm</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table-borderstyle">
       <!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
	</xsl:attribute-set>
	<xsl:attribute-set name="fonts_group2.size_12">
		<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
		<xsl:attribute name="font-size">12pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="fonts_group2.size_10">
		<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="arial_font.size_11">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">11pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="arial_font.size_10">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="arial_font.size_9">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="verdana_font.size_10">
		<xsl:attribute name="font-family">Verdana</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="verdana_font.size_9">
		<xsl:attribute name="font-family">Verdana</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="verdana_font.size_8">
		<xsl:attribute name="font-family">Verdana</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:include href="HtmlToFoTransform.xsl" />
	<xsl:include href="infoMessages.xsl" />
	<xsl:include href="fapFundScorecard.xsl" />
	<xsl:include href="fapGeneric.xsl" />
	
	<xsl:variable name="imagePath">
	  <xsl:value-of select='/fundsAndPerformance/imagePath'/>
    </xsl:variable>
    
    <xsl:variable name="showMorningstarScoreVar">
	  <xsl:value-of select='/fundsAndPerformance/showMorningstarScore'/>
    </xsl:variable>
    
    <xsl:variable name="showfi360ScoreVar">
	  <xsl:value-of select='/fundsAndPerformance/showfi360Score'/>
    </xsl:variable>
    
    <xsl:variable name="showRpagScoreVar">
	  <xsl:value-of select='/fundsAndPerformance/showRpagScore'/>
    </xsl:variable>
	
	<xsl:template match="fundsAndPerformance">
	
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<xsl:choose>
		<xsl:when
			test="tabName = 'pricesAndYTD' or tabName = 'performanceAndFees' or tabName = 'morningstar' or tabName = 'fundInformation' or tabName = 'fundCharacteristics1' or tabName = 'fundCharacteristics2' or tabName = 'standardDeviation' ">
			<fo:layout-master-set>
				<fo:simple-page-master margin-bottom="1.0cm"
					margin-left="0.5cm" margin-right="0.5cm" margin-top="1.0cm"
					master-name="pageLandscapeLayout" page-height="21.59cm"
					page-width="35.56cm">
					<fo:region-body margin-bottom="1.0cm" margin-top="0cm" />
					<fo:region-before extent="0cm" />
					<fo:region-after extent="0.5cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
		</xsl:when>
		<xsl:otherwise>
			<fo:layout-master-set>
				<fo:simple-page-master margin-bottom="1.0cm"
					margin-left="0.5cm" margin-right="0.5cm" margin-top="1.0cm"
					master-name="pageLandscapeLayout" page-height="21.59cm"
					page-width="27.94cm">
					<fo:region-body margin-bottom="1.0cm" margin-top="0cm" />
					<fo:region-before extent="0cm" />
					<fo:region-after extent="0.5cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
		</xsl:otherwise>
	</xsl:choose>
	
	<xsl:choose>
		<xsl:when test="tabName = 'fundScorecard'">
			<xsl:call-template name="fundsAndPerformanceScorecard" />
		</xsl:when>
		<xsl:otherwise>
			<xsl:call-template name="fundsAndPerformanceGeneric" />
		</xsl:otherwise>
	</xsl:choose>
	
	</fo:root>
	
    </xsl:template>
	
</xsl:stylesheet>