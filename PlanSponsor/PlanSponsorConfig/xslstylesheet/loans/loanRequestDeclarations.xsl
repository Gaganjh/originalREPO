<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	
	<xsl:template name="loanRequestDeclarationsSection">
	
	<fo:block space-before="20px" page-break-inside="avoid">
		<fo:table table-layout="fixed" width="100%">
		<fo:table-column column-width="100%" />	
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets="table_header_text_font">
					<fo:block >
						<xsl:value-of select="cmaContent/declarationsSectionTitleText"/> 
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
		</fo:table>
		
		<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
		<fo:table-column column-width="100%" />											
		<fo:table-body>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="table_sub_header_default_bold_font">
					<fo:block >The participant agreed to:</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-start="11px">
					<fo:block padding-before="3px">
						<fo:inline>
							<xsl:choose>
								<xsl:when test="loanForm/truthInLendingNotice/accepted = 'false'">
									<fo:external-graphic content-height="8px" content-width="8px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')" />
										</xsl:attribute>
									</fo:external-graphic>
								</xsl:when>
								<xsl:otherwise>
									<fo:external-graphic content-height="8px" content-width="8px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')" />
										</xsl:attribute>
									</fo:external-graphic>
								</xsl:otherwise>
							</xsl:choose>
						</fo:inline>
						<fo:inline padding-start="6px">Truth In Lending Notice</fo:inline>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-start="11px">
					<fo:block>
						<fo:inline>
							<xsl:choose>
								<xsl:when test="loanForm/promissoryNote/accepted = 'false'">
									<fo:external-graphic content-height="8px" content-width="8px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')" />
										</xsl:attribute>
									</fo:external-graphic>
								</xsl:when>
								<xsl:otherwise>
									<fo:external-graphic content-height="8px" content-width="8px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')" />
										</xsl:attribute>
									</fo:external-graphic>
								</xsl:otherwise>
							</xsl:choose>
						</fo:inline>
						<fo:inline padding-start="6px">Non-negotiable Promissory Note and
							Irrevocable Pledge and Assignment</fo:inline>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			
			<xsl:if test="displayRules/displayParticipantDeclarationCheckbox = 'true'">
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-start="11px" padding-after="2px">
					<fo:block >
						<fo:inline>
							<fo:external-graphic content-height="8px" content-width="8px">
								<xsl:attribute name="src">
									<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')"/>
								</xsl:attribute>
							</fo:external-graphic>
						</fo:inline>
						<fo:inline padding-start="6px">The following authorization: </fo:inline>
					</fo:block>
					<fo:block>
						<xsl:value-of select="loanForm/participantDeclaration/html"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			
			<xsl:if test="displayRules/displayApproverAgreedToLabel = 'true'">
			<fo:table-row  >
				<fo:table-cell xsl:use-attribute-sets="table_sub_header_default_bold_font">
					<fo:block padding-before="8px" >
						The approver agreed to:
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			
			<xsl:if test="displayRules/displayApproverAgreedToText = 'true'">
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-after="15px" padding-before="4px">
					<fo:block >
						<xsl:value-of select="displayRules/loanApprovalPlanSpousalConsentContent"/>
						<xsl:value-of select="displayRules/loanApprovalGenericContent"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			
			<xsl:if test="displayRules/displayAtRiskTransactionCheckbox = 'true'">
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-start="11px" padding-before="3px" padding-after="15px">
					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="3%" />
						<fo:table-column column-width="97%" />		
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
								<xsl:choose>
									<xsl:when test="loanForm/atRiskTransaction/accepted = 'true'">
										<fo:external-graphic content-height="8px" content-width="8px">
											<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')"/>
										</xsl:attribute>
										</fo:external-graphic>
								
									</xsl:when>
									<xsl:otherwise>
										<fo:external-graphic content-height="8px" content-width="8px">
											<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')"/>
											</xsl:attribute>
										</fo:external-graphic>
									</xsl:otherwise>
									</xsl:choose>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
									<xsl:if test="loanForm/atRiskTransactionInd = 'true'">
	                				<xsl:value-of select="loanForm/atRiskTransaction/html"/>
	                				<fo:block space-before="15px">
	                				
	                				<fo:table table-layout="fixed" width="100%">
										<fo:table-column column-width="2%" />
										<fo:table-column column-width="98%" />		
										<fo:table-body>
										<xsl:for-each select="loanForm/detailText/string">
											<xsl:variable name="numberCount" select="position()" />
											<fo:table-row>
												<fo:table-cell>
													<fo:block padding-before="10px">
													<xsl:value-of select="$numberCount"/>.
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block padding-before="10px"><xsl:value-of select="."/></fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:for-each>
										</fo:table-body>
										</fo:table>
	                				
	                			</fo:block>
	                				</xsl:if>
							     </fo:block>
							</fo:table-cell>
					</fo:table-row>
					</fo:table-body>
					</fo:table>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			
			<fo:table-row  padding-before="2px" padding-after="15px">
				<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-start="10px">
					<fo:block >
						<xsl:value-of select="cmaContent/declarationsSectionFooterText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
	</fo:block>
	</xsl:template>
</xsl:stylesheet>