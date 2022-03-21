<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
		 
	<xsl:template name="RequestNotesViewDetails">
	
	<fo:block space-before="20px" page-break-inside="avoid">
		<fo:table table-layout="fixed" width="90%">
			<fo:table-column column-width="90%" />	
			<fo:table-body>
			<fo:table-row>
				<fo:table-cell xsl:use-attribute-sets="table_header_text_font">
					<fo:block>Notes</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</fo:table-body>
		</fo:table>
		
		<fo:table table-layout="fixed" width="90%" xsl:use-attribute-sets="border_table_layout">
			<fo:table-column column-width="90%" />											
			<fo:table-body>
			<fo:table-row >
				<fo:table-cell  xsl:use-attribute-sets="table_sub_header_default_bold_font" >
					<fo:block>Notes to participant</fo:block>
				</fo:table-cell>
			</fo:table-row>
			
			<xsl:for-each select="loan/previousParticipantNotes/loanNote">
			<fo:table-row >
				<fo:table-cell >
					<fo:block>
						<xsl:variable name="userId">
							<xsl:value-of select="createdById"/>
						</xsl:variable>
						
						<xsl:for-each select="../../../userNames/item[@key=$userId]">
							<fo:inline xsl:use-attribute-sets = "submission_info_bold_font" padding-start="1px">
								<xsl:value-of select="firstName"/>&#160;
							</fo:inline>
							<fo:inline xsl:use-attribute-sets = "submission_info_bold_font">
								<xsl:value-of select="lastName"/>&#160;
							</fo:inline>
						</xsl:for-each>
						<fo:inline xsl:use-attribute-sets = "submission_info_bold_font">
							<xsl:value-of select="created"/>&#160;
						</fo:inline>
						<fo:inline xsl:use-attribute-sets = "table_data_default_font">
							<xsl:value-of select="note"/>
						</fo:inline>	
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:for-each>
			
			<xsl:if test="displayRules/displayNotesToAdministrators = 'true'"> 
			<fo:table-row >
				<fo:table-cell  xsl:use-attribute-sets="table_sub_header_default_bold_font" >
					<fo:block padding-before="5px">Notes to administrators</fo:block>
				</fo:table-cell>
			</fo:table-row>
			
			<xsl:for-each select="loan/previousAdministratorNotes/loanNote">
			<fo:table-row >
				<fo:table-cell>
					<fo:block>
						<xsl:variable name="userId">
							<xsl:value-of select="createdById"/>
						</xsl:variable>
						
						<xsl:for-each select="../../../userNames/item[@key=$userId]">
							<fo:inline xsl:use-attribute-sets="submission_info_bold_font" padding-start="1px">
								<xsl:value-of select="firstName"/>&#160;
							</fo:inline>
							<fo:inline xsl:use-attribute-sets="submission_info_bold_font">
								<xsl:value-of select="lastName"/>&#160;
							</fo:inline>
						</xsl:for-each>
						<fo:inline xsl:use-attribute-sets="submission_info_bold_font">
							<xsl:value-of select="created"/>&#160;
						</fo:inline>
						<fo:inline xsl:use-attribute-sets = "table_data_default_font">
							<xsl:value-of select="note"/>
						</fo:inline>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:for-each>
			</xsl:if>
			
			<xsl:if test="displayRules/displayNoteToParticipantPrintContentText = 'true'">
			<fo:table-row >
				<fo:table-cell padding-start="1px" padding-after="2px" >
					<fo:block xsl:use-attribute-sets = "table_data_default_font" padding-before="1.5px">
						<xsl:value-of select="cmaContent/noteToparticipantPrintContentText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
			
			<xsl:if test="displayRules/displayViewNotesSectionFooter = 'true'">
			<fo:table-row>
				<fo:table-cell padding-start="1px" padding-after="2px" >
					<fo:block xsl:use-attribute-sets = "table_data_default_font" padding-before="1.5px"> 
						<xsl:value-of select="cmaContent/viewNotesSectionFooterText"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			</xsl:if>
		</fo:table-body>
	</fo:table>
	</fo:block>
	</xsl:template>
</xsl:stylesheet>