<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="WithdrawalPageFooter">

	<!-- Withdrawal request specific page footer & disclaimer -->
				<fo:block space-before="10px" page-break-inside="avoid">
					<fo:table table-layout="fixed" width="100%">
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets = "table_data_default_font">
								
									<xsl:for-each select="withdrawalRequestUi/cmaContent/footnotes/string">
										<fo:block space-before="12px">
											<xsl:value-of select="." /> 
										</fo:block>
									</xsl:for-each>
		         					<fo:block space-before="2px"></fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row >
								<fo:table-cell xsl:use-attribute-sets = "table_data_default_font">
									<fo:block>
										<xsl:if test="withdrawalRequestUi/hasBothPbaAndLoans = 'true'"> 
											<xsl:value-of select="withdrawalRequestUi/cmaContent/accountBalanceFootnotePbaAndLoan" /> 
										</xsl:if>
		         					</fo:block>
		         					<fo:block space-before="5px"></fo:block>
		         					<fo:block>
										<xsl:if test="withdrawalRequestUi/hasPbaOnly = 'true'"> 
											<xsl:value-of select="withdrawalRequestUi/cmaContent/accountBalanceFootnotePbaOnly" />
										</xsl:if>
		         					</fo:block>
		         					<fo:block space-before="5px"></fo:block>
		         					<fo:block>
										<xsl:if test="withdrawalRequestUi/hasLoansOnly = 'true'"> 
											<xsl:value-of select="withdrawalRequestUi/cmaContent/accountBalanceFootnoteLoanOnly" />
										</xsl:if>
		         					</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							
							<fo:table-row >
								<fo:table-cell xsl:use-attribute-sets = "table_data_default_font">
									
									<xsl:for-each select="withdrawalRequestUi/cmaContent/disclaimer/string">
										<fo:block space-before="12px">
											<xsl:value-of select="." /> 
										</fo:block>
									</xsl:for-each>
									<fo:block/>
		         					
								</fo:table-cell>
							</fo:table-row>
							
						</fo:table-body>
					</fo:table>
				</fo:block>

	</xsl:template>
</xsl:stylesheet>