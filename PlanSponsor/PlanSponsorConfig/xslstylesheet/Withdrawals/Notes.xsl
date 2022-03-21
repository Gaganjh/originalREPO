<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="WithdrawalNotes">

		
				<!--Notes  section -->
		<fo:block space-before="20px" page-break-inside="avoid">
				
			<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell background-repeat="no-repeat" 
							xsl:use-attribute-sets="table_header_text_font" number-columns-spanned="2">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/cmaContent/notesSectionTitle" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
				<fo:table-column column-width="40%" />											
				<fo:table-column column-width="60%" />
				<fo:table-body>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="table_cell_withdrawal_Notes_section_font_layout">
							<fo:block>
							<xsl:choose>
								<xsl:when test="withdrawalRequestUi/isParticipantInitiated = 'true'">
										To participant if denying
									</xsl:when>
									<xsl:otherwise>
										To participant
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="withdrawal_Notes_section_font_layout" >
							<fo:block>
							 <xsl:if test="count(withdrawalRequestUi/withdrawalRequest/readOnlyAdminToParticipantNotes/withdrawalRequestNote) &gt; 0">
							 <xsl:for-each select="withdrawalRequestUi/withdrawalRequest/readOnlyAdminToParticipantNotes/withdrawalRequestNote">
							 <xsl:variable name="createdById_var">
								<xsl:value-of select="createdById" />
							</xsl:variable>
							 	<fo:block>
							 		<xsl:for-each select="../../../../userNames/item[@key=$createdById_var]">
							 			<fo:inline xsl:use-attribute-sets="submission_info_bold_font">
							 				<xsl:value-of select="firstName"/>&#160;
							 			</fo:inline>
							 			<fo:inline xsl:use-attribute-sets="submission_info_bold_font">
							 				<xsl:value-of select="lastName"/>&#160;
							 			</fo:inline>
							 		</xsl:for-each>
								<fo:inline xsl:use-attribute-sets="submission_info_bold_font"><xsl:value-of select="created" /></fo:inline>
								<fo:inline>&#160;</fo:inline>
							 	<fo:inline><xsl:value-of select="note" /></fo:inline>
								</fo:block>
							 </xsl:for-each>
							 </xsl:if> 
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<xsl:if test="withdrawalRequestUi/printParticipant = 'false'">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="table_cell_withdrawal_Notes_section_font_layout">
							<fo:block>
								For administrator(s) only
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="withdrawal_Notes_section_font_layout">
						
							<fo:block>
								 <xsl:if test="count(withdrawalRequestUi/withdrawalRequest/readOnlyAdminToAdminNotes/withdrawalRequestNote) &gt; 0">
								 <xsl:for-each select="withdrawalRequestUi/withdrawalRequest/readOnlyAdminToAdminNotes/withdrawalRequestNote">
								  <xsl:variable name="createdById_var">
								<xsl:value-of select="createdById" />
								</xsl:variable>
								 	<fo:block>
								 	
								 	<xsl:for-each select="../../../../userNames/item[@key=$createdById_var]">
							 			<fo:inline xsl:use-attribute-sets="submission_info_bold_font">
							 				<xsl:value-of select="firstName"/>&#160;
							 			</fo:inline>
							 			<fo:inline xsl:use-attribute-sets="submission_info_bold_font">
							 				<xsl:value-of select="lastName"/>&#160;
							 			</fo:inline>
							 		</xsl:for-each>
								 	
									<fo:inline xsl:use-attribute-sets="submission_info_bold_font"><xsl:value-of select="created" /></fo:inline>
									<fo:inline>&#160;</fo:inline>
								 	<fo:inline><xsl:value-of select="note" /></fo:inline>
								 	</fo:block>
								 </xsl:for-each>
								 </xsl:if> 
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					
					<fo:table-row >
                       	<fo:table-cell padding-start="4px" number-columns-spanned="2">
                       		<fo:block xsl:use-attribute-sets="table_data_default_font" >
	                       		<xsl:if test="withdrawalRequestUi/printParticipant = 'false'">
	                       		<xsl:if test="$showStaticContent_var = 'true'">
	                       			<xsl:if test="not(withdrawalRequestUi/isParticipantInitiated = 'true')">
	                       				<xsl:value-of select="withdrawalRequestUi/cmaContent/notesSpecialContent" /> 
	                       			</xsl:if>
	                       		</xsl:if>
	                       		</xsl:if>
                       		</fo:block>
                       	</fo:table-cell>
                   	</fo:table-row>
                   	
                   	<fo:table-row  xsl:use-attribute-sets="border_table_layout" >
                       	<fo:table-cell padding-start="4px" number-columns-spanned="2">
                       		<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="5px">
	                    		<xsl:if test="withdrawalRequestUi/printParticipant = 'false'">
	                    		<xsl:if test="$showStaticContent_var = 'true'">
		                    		<xsl:value-of select="withdrawalRequestUi/cmaContent/step2PageBeanBody3Header" />  
	                   			</xsl:if>
	                   			</xsl:if>
	                   		</fo:block>
                       	</fo:table-cell>
                   	</fo:table-row>
                   
                      	
				</fo:table-body>
			</fo:table>
		</fo:block>
			
	</xsl:template>
</xsl:stylesheet>