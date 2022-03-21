<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

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

<xsl:attribute-set name="solid.blue.border.3sides">
	<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-start-style">solid</xsl:attribute>
	<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-end-style">solid</xsl:attribute>
	<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="arial_font.size_10">
	<xsl:attribute name="font-family">Arial</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="verdana_font.size_10">
	<xsl:attribute name="font-family">Verdana</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="verdana_font.size_7">
	<xsl:attribute name="font-family">Verdana</xsl:attribute>
	<xsl:attribute name="font-size">7pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fonts_group2.size_12">
	<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="arial_font.size_9">
	<xsl:attribute name="font-family">Arial</xsl:attribute>
	<xsl:attribute name="font-size">9pt</xsl:attribute>
</xsl:attribute-set>

<xsl:include href="reportCommonHeader.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />
<xsl:include href="HtmlToFoTransform.xsl" />

<xsl:template match="rateOfReturnCalculator">
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
		
		<!-- 11x8.5 page.-->
			<xsl:call-template name="pageDefinition"/>
		
		<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after" >
					<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
						Page <fo:page-number/> of <fo:page-number-citation-last ref-id="terminator"/> NOT VALID WITHOUT ALL PAGES
					</fo:block>
				</fo:static-content>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="reportCommonHeader"/>
					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
									<xsl:if test="intro1Text">
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro1Text" />
										</fo:block>
										</xsl:if>
										<xsl:if test="intro2Text">
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro2Text" />
										</fo:block>
										</xsl:if>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					<fo:table>
						<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row background-color="#deded8" keep-with-previous="always">
								<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
									<fo:block font-family="Verdana" font-size="14pt">
										Rate of Return for <xsl:choose>
											<xsl:when test="(//productId='DB06') or (//productId='DBNY06')">
												<xsl:value-of select="contractName"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="lastName"/>,<xsl:value-of select="firstName"/>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row keep-with-previous="always">
								<fo:table-cell padding-before="0.1cm" padding-start="0.3cm">
									<fo:block xsl:use-attribute-sets="verdana_font.size_10">
										Rate of Return for the period beginning <xsl:value-of select="startDate"/> and ending <xsl:value-of select="endDate"/> 
										is <xsl:value-of select="rateOfReturn"/>%.
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					
					<!-- Showing the page footer-->
					<xsl:call-template name="reportCommonFooter"/>
					
				</fo:flow>
		
		</fo:page-sequence>
	</fo:root>
</xsl:template>
</xsl:stylesheet>