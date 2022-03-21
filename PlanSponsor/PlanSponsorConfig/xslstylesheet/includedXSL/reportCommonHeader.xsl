<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:attribute-set name="table-borderstyle">
<!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
</xsl:attribute-set>
<xsl:include href="HtmlToFoTransform.xsl" />

	<xsl:template name="reportCommonHeader">

		<fo:block>
			<fo:table>
				<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
				<fo:table-column column-width="60%" xsl:use-attribute-sets="table-borderstyle"/>
				<fo:table-body>
					<fo:table-cell>
						<fo:block>
							<fo:external-graphic content-height="70px" content-width="170px">
								<xsl:attribute name="src">
									<xsl:value-of select="jhLogoPath"/>
								</xsl:attribute>
							</fo:external-graphic>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding-before="0.5cm">
						<fo:block text-align="right" font-family="Georgia" font-size="24pt">
							<xsl:apply-templates select="pageName" />
						</fo:block>
						<xsl:if test="contractName and contractNumber">
							<fo:block text-align="right" font-family="Arial" font-size="16pt" font-weight="bold">
								<fo:inline>
									<xsl:value-of select="contractName" />
								</fo:inline>
								<fo:inline>(</fo:inline>
								<fo:inline>
									<xsl:value-of select="contractNumber" />
								</fo:inline>
								<fo:inline>)</fo:inline>
							</fo:block>
						</xsl:if>
						<xsl:if test="fromDate and toDate">
							<fo:block text-align="right" font-family="Arial" font-size="14pt">
								<fo:inline>
									From
								</fo:inline>
								<fo:inline><xsl:value-of select="fromDate" /></fo:inline>
								<fo:inline>
									To
								</fo:inline>
								<fo:inline><xsl:value-of select="toDate" /></fo:inline>
							</fo:block>
						</xsl:if>
						<xsl:if test="asOfDate">
							<fo:block text-align="right" font-family="Arial" font-size="14pt">
								<fo:inline>
									As of
								</fo:inline>
								<fo:inline><xsl:value-of select="asOfDate" /></fo:inline>
							</fo:block>
						</xsl:if>
					</fo:table-cell>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
		
</xsl:stylesheet>