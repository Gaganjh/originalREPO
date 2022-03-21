<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:attribute-set name="table-borderstyle">
<!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
</xsl:attribute-set>

	<xsl:template name="infoMessages">
	
		<xsl:if test="infoMessages/message">
			<fo:table space-before="0.5cm">
				<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell padding-start="0.3cm">
						<fo:block font-weight="bold" padding-before="0.2cm" padding-after="0.2cm">Information Message</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<xsl:for-each select="infoMessages/message">
						<fo:table-row>
							<fo:table-cell padding-start="0.3cm">
								<fo:block padding-before="0.2cm" padding-after="0.2cm">
									<fo:inline>
										<xsl:value-of select="messageNum"/>
									</fo:inline>
									<fo:inline>. </fo:inline>
									<fo:inline>
										<xsl:apply-templates select="messageText"/>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
		</xsl:if>
	
	</xsl:template>
		
</xsl:stylesheet>