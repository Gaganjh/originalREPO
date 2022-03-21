<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
	<xsl:template name="CommonPageFooter">

	<!-- Loan request specific page footer & disclaimer -->
		<fo:block space-before="20px">
			<fo:table table-layout="fixed" width="100%">
				<fo:table-body>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets = "table_data_default_font">
							<fo:block padding-before="20px">
							  <xsl:if test="cmaContent/accountBalanceFootnoteText != ''"> 
								 <xsl:value-of select="cmaContent/accountBalanceFootnoteText" />
							  </xsl:if> 
         					</fo:block>
         					<fo:block space-before="10px"></fo:block>
						</fo:table-cell>
					</fo:table-row>
					<xsl:if test="displayRules/printFriendly = 'true'">
						<fo:table-row keep-with-previous="always"> 
							<fo:table-cell xsl:use-attribute-sets = "table_data_default_font">
	         					<fo:block>
	         						<xsl:value-of select="cmaContent/globalDisclosure"></xsl:value-of>
	         					</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
				</fo:table-body>
			</fo:table>
		</fo:block>

	</xsl:template>
</xsl:stylesheet>