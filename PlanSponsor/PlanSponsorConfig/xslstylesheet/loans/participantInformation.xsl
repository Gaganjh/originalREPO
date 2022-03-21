<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<xsl:template name="ParticipantInformation">
	<fo:block space-before="20px" page-break-inside="avoid">
	
	<fo:table table-layout="fixed" width="90%">
		<fo:table-column column-width="90%" />
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets="table_header_text_font">
					<fo:block>
						<xsl:value-of select="cmaContent/participantInfoTitleText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
	
	<fo:table table-layout="fixed" width="90%" xsl:use-attribute-sets="border_table_layout">
		<fo:table-column column-width="20%" />											
		<fo:table-column column-width="20%" />
		<fo:table-column column-width="20%" />											
		<fo:table-column column-width="30%" />
		<fo:table-body>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						Name
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="1px">
					<fo:block xsl:use-attribute-sets="submission_info_bold_font" padding-before="3px"> 
						<fo:inline><xsl:value-of select="loanForm/firstName"/>&#160;</fo:inline>
						<xsl:if test="displayRules/displayMiddleInitial = 'true'">
							<fo:inline><xsl:value-of select="loanForm/middleInitial"/>&#160;</fo:inline>
						</xsl:if>
						<fo:inline><xsl:value-of select="loanForm/lastName"/></fo:inline>	
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font" >
						Contract number 
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="submission_info_bold_font" padding-start="1px">
					<fo:block padding-before="3px">
						<xsl:value-of select="loanForm/contractId"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						SSN
					</fo:block>
				</fo:table-cell>
				<xsl:choose>
					<xsl:when test="displayRules/maskSsn = 'true'">
						<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="1px">
							<fo:block xsl:use-attribute-sets="submission_info_bold_font" padding-before="3px">
								<xsl:value-of select="formattedMaskedSSN"/>   
							</fo:block>
						</fo:table-cell>
					</xsl:when>
					<xsl:otherwise>
						<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="1px">
							<fo:block xsl:use-attribute-sets="submission_info_bold_font" padding-before="3px">
								<xsl:value-of select="formattedSSN"/>   
							</fo:block>
						</fo:table-cell>
					</xsl:otherwise>
				</xsl:choose>
				<fo:table-cell xsl:use-attribute-sets="border_right-style">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font" >
						Contract name 
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="submission_info_bold_font" padding-start="1px">
					<fo:block padding-before="3px">
					  <xsl:value-of select="loanForm/contractName"/>   
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row >
				<fo:table-cell xsl:use-attribute-sets="border_right-style">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font">
						Employment status  
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="1px">
					<fo:block xsl:use-attribute-sets="submission_info_bold_font" padding-before="3px">
						<xsl:value-of select="employeeStatus"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="border_right-style">
					<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font" >
					</fo:block>
				</fo:table-cell>
				<fo:table-cell xsl:use-attribute-sets="submission_info_bold_font" padding-start="1px">
					<fo:block padding-before="3px">
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			
			 <xsl:if test="displayRules/displayLegallyMarried = 'true'">
			 
			 <fo:table-row >
					<fo:table-cell xsl:use-attribute-sets="border_right-style">
						<fo:block xsl:use-attribute-sets="table_sub_header_default_bold_font" >
							Legally married  
						</fo:block>
					</fo:table-cell>
					<xsl:if test="loan/legallyMarriedInd = 'true'">
						<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="1px">
							<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
								Yes
							</fo:block>
						</fo:table-cell>
					</xsl:if>
					<xsl:if test="loan/legallyMarriedInd = 'false'">
						<fo:table-cell xsl:use-attribute-sets="border_right-style" padding-start="1px">
							<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
								No
							</fo:block>
						</fo:table-cell>
					</xsl:if>
					<fo:table-cell xsl:use-attribute-sets="border_right-style">
						<fo:block xsl:use-attribute-sets="submission_info_bold_font"> 
						</fo:block>
					</fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="submission_info_bold_font">
						<fo:block padding-before="3px">
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				<xsl:if test="displayRules/displaySpousalConsentText = 'true'">
				<fo:table-row >
					<fo:table-cell number-columns-spanned="4" padding-after="2px" padding-start="1px">
						<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:choose>
							<xsl:when test="loan/legallyMarriedInd">
								<xsl:choose>
								<xsl:when test="loan/legallyMarriedInd = 'false'">
										<xsl:value-of select="cmaContent/spousalConsentIsNotRequiredText"/>
								</xsl:when>
								<xsl:otherwise>
								<xsl:if test="loanPlanData/spousalConsentReqdInd = '' or not(loanPlanData/spousalConsentReqdInd)">
										<xsl:value-of select="cmaContent/spousalConsentMayBeRequiredIsMarriedText"/>
								</xsl:if>
								<xsl:if test="loanPlanData/spousalConsentReqdInd = 'Y'">
										<xsl:value-of select="cmaContent/spousalConsentMustBeObtainedText"/>
								</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
							
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="loanPlanData/spousalConsentReqdInd = '' or not(loanPlanData/spousalConsentReqdInd)
												or loanPlanData/spousalConsentReqdInd = 'Y'">
								<xsl:value-of select="cmaContent/spousalConsentMayBeRequiredText"/>
								</xsl:if>
							</xsl:otherwise>
							
						</xsl:choose>
							
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				</xsl:if>
				</xsl:if>
			
			 
			<fo:table-row >
				<fo:table-cell number-columns-spanned="4" padding-after="2px" padding-start="1px">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="3px">
						<xsl:value-of select="cmaContent/participantInfoFooterText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
	</fo:block>
	</xsl:template>
</xsl:stylesheet>