<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<xsl:template name="generalInformation">

	<fo:block xsl:use-attribute-sets="header_default_font">
		<fo:external-graphic content-height="100px" content-width="150px">
			<xsl:attribute name="src">
				<xsl:value-of select="concat($imagePath_var,'JH_blue_resized.gif')"/>
			</xsl:attribute>
		</fo:external-graphic>
	</fo:block>
	
	<fo:block xsl:use-attribute-sets="table_data_default_font">
		<fo:inline><xsl:value-of select="loanForm/contractName"/></fo:inline>| Contract: 
		<fo:inline><xsl:value-of select="loanForm/contractId"/></fo:inline>
	</fo:block>
	
	<xsl:if test="loanForm/showConfirmation = 'false'">
		<fo:block xsl:use-attribute-sets="header_default_font" padding-before="8px">
			<fo:external-graphic content-height="200px" content-width="200px">
				<xsl:attribute name="src">
					<xsl:value-of select="concat($imagePath_var,'view_loan_request_ttl.gif')"/>
				</xsl:attribute>
			</fo:external-graphic>
		</fo:block>
	</xsl:if>
	
	<xsl:if test="loanForm/showConfirmation = 'true'">
		<fo:block xsl:use-attribute-sets="header_default_font" padding-before="8px">
			<fo:external-graphic content-height="200px" content-width="200px">
				<xsl:attribute name="src">
					<xsl:value-of select="concat($imagePath_var,'loan_request_ttl.gif')"/>
				</xsl:attribute>
			</fo:external-graphic>
		</fo:block>
	</xsl:if>
	
	<fo:block space-before="25px">
		<fo:table table-layout="fixed" width="100%">
		<fo:table-body>
			<xsl:if test="displayRules/displayPageContentNotFinalDisclaimer = 'true'">
			<fo:table-row>
				<fo:table-cell >
					<fo:block xsl:use-attribute-sets = "table_data_default_font">
						<xsl:value-of select="cmaContent/pageContentNotFinalDisclaimer"/>  
     				</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			
			<xsl:if test="displayRules/displayGiflMsgExternalUserInitiated = 'true'">
			<fo:table-row>
				<fo:table-cell>
					<fo:block xsl:use-attribute-sets = "table_data_default_font">
						<xsl:value-of select="cmaContent/giflMsgExternalUserInitiated"/>  
     				</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			
			<xsl:if test="displayRules/displayGiflMsgParticipantInitiated = 'true'">
			<fo:table-row>
				<fo:table-cell>
					<fo:block xsl:use-attribute-sets = "table_data_default_font">
						<xsl:value-of select="cmaContent/giflMsgParticipantInitiated"/>  
     				</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
		
			<xsl:if test="displayRules/displaySubmissionNumber = 'true'"> 
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets = "submission_info_bold_font">
					<fo:block padding-before="1px">
						<fo:inline>Submission number:&#160;</fo:inline> 
						<fo:inline><xsl:value-of select="loan/submissionId"/></fo:inline> 
       				</fo:block>
				</fo:table-cell>
			</fo:table-row>
		
			<xsl:if test="displayRules/displaySubmissionStatus = 'true'">
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets = "submission_info_bold_font">
					<fo:block>
						<fo:inline>Status:&#160;</fo:inline> 
						<fo:inline><xsl:value-of select="loanStatus"/></fo:inline> 
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			
		 	<xsl:if test="displayRules/displaySubmissionProcessingDates = 'true'"> 
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets = "submission_info_bold_font">
					<fo:block>
						<fo:inline>Approved date/time:&#160;</fo:inline> 
						<fo:inline><xsl:value-of select="loanActivities/summaries/loanActivitySummary/created"/></fo:inline> 
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets = "submission_info_bold_font">
					<fo:block>
						<fo:inline>Expected processing date:&#160;</fo:inline> 
						<fo:inline><xsl:value-of select="loan/effectiveDate"/></fo:inline> 
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			</xsl:if> 
		</fo:table-body>
		</fo:table>
	</fo:block>
	</xsl:template>
</xsl:stylesheet>