<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:attribute-set name="table-borderstyle">
<!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
</xsl:attribute-set>

<xsl:attribute-set name="arial_font.size_11">
	<xsl:attribute name="font-family">Arial</xsl:attribute>
	<xsl:attribute name="font-size">11pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="verdana_font.size_10">
	<xsl:attribute name="font-family">Verdana</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:include href="HtmlToFoTransform.xsl" />

	<xsl:template name="reportCommonFooter">
	
		<xsl:if test="pdfCapped">
			<fo:block space-before="0.3cm" xsl:use-attribute-sets="verdana_font.size_10">
				<xsl:value-of select="pdfCapped" />
			</fo:block>
		</xsl:if>
		
		<!-- Showing the page footer-->
		<fo:block space-before="0.7cm">
			<fo:block>
				 <fo:table>
					<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
					<fo:table-body>
						<xsl:if test="footnoteGifl">
							<fo:table-row>
								<fo:table-cell padding-start="1cm" padding-before="0.1cm">								
									<fo:block xsl:use-attribute-sets="arial_font.size_11">	
									<fo:inline vertical-align="center">
											<xsl:text>* </xsl:text>
										</fo:inline>
										<xsl:apply-templates select="footnoteGifl" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
						<xsl:if test="footnoteMA">
							<fo:table-row>
								<fo:table-cell padding-start="1cm" padding-before="0.1cm">								
									<fo:block xsl:use-attribute-sets="arial_font.size_11">	
									<fo:inline vertical-align="super"> 
									<xsl:text>&#167;</xsl:text>
									</fo:inline>
										<xsl:apply-templates select="footnoteMA" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
						<xsl:if test="multipleRothfootnote">
							<fo:table-row>
								<fo:table-cell padding-start="1cm" padding-before="0.1cm">								
									<fo:block xsl:use-attribute-sets="arial_font.size_11">	
										<xsl:apply-templates select="multipleRothfootnote" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
						<xsl:if test="footnoteRoth">
							<fo:table-row>
								<fo:table-cell padding-start="1cm" padding-before="0.1cm">								
									<fo:block xsl:use-attribute-sets="arial_font.size_11">	
										<xsl:apply-templates select="footnoteRoth" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
						<xsl:if test="footnotePBA">
							<fo:table-row>
								<fo:table-cell padding-start="1cm" padding-before="0.1cm">								
									<fo:block xsl:use-attribute-sets="arial_font.size_11">	
										<fo:inline vertical-align="super">
											<xsl:text>&#8224;</xsl:text>
										</fo:inline>
										<fo:inline keep-with-previous="always">
											<xsl:apply-templates select="footnotePBA" />
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>	
						<fo:table-row>
							<fo:table-cell padding-start="1cm" padding-before="0.1cm">							
								<fo:block xsl:use-attribute-sets="arial_font.size_11">	
									<xsl:apply-templates select="footer" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<xsl:if test="footnoteBB">
							<fo:table-row>
								<fo:table-cell padding-start="1cm" padding-before="0.1cm">
									<fo:block xsl:use-attribute-sets="arial_font.size_11">		
										<xsl:apply-templates select="footnoteBB" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>	
						<xsl:if test="footnotes">
							<fo:table-row>
								<fo:table-cell padding-start="1cm" padding-before="0.1cm">
									<fo:block xsl:use-attribute-sets="arial_font.size_11">		
										<xsl:apply-templates select="footnotes" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>	
						<xsl:if test="contractSummaryGiflFootnote">
							<fo:table-row>
								<fo:table-cell padding-start="1cm" padding-before="0.1cm">
									<fo:block xsl:use-attribute-sets="arial_font.size_11">		
										<xsl:apply-templates select="contractSummaryGiflFootnote" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
						<xsl:if test="withdrawalMsg">
							<fo:table-row>
								<fo:table-cell padding-start="1cm" padding-before="0.1cm">
									<fo:block xsl:use-attribute-sets="arial_font.size_11">		
										*<xsl:apply-templates select="withdrawalMsg" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>	
						<fo:table-row>
							<fo:table-cell padding-start="1cm" padding-before="0.1cm">																			
								<fo:block xsl:use-attribute-sets="arial_font.size_11">		
									<xsl:apply-templates select="disclaimer" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</fo:block>
		<fo:block space-before="0.5cm" xsl:use-attribute-sets="arial_font.size_11">
			<xsl:apply-templates select="globalDisclosure" />
		</fo:block> 
		<fo:block id="terminator" />
	
	</xsl:template>
		
</xsl:stylesheet>