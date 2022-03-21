<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="GeneralInformation">
			
			<fo:block xsl:use-attribute-sets="header_default_font">
						<fo:external-graphic content-height="100px" content-width="150px">
							<xsl:attribute name="src">
								<xsl:value-of select="concat($imagePath_var,'JH_blue_resized.gif')"/>
							</xsl:attribute>
						</fo:external-graphic>
			</fo:block>
			
			<fo:block xsl:use-attribute-sets="table_data_default_font" padding-before="0.3px">
				<fo:inline><xsl:value-of select="withdrawalRequestUi/withdrawalRequest/contractName"/></fo:inline> | Contract: 
				<fo:inline><xsl:value-of select="withdrawalRequestUi/withdrawalRequest/contractId"/></fo:inline>
			</fo:block>
			
			<xsl:if test="withdrawalRequestUi/isViewAction = 'true'">
			<fo:block xsl:use-attribute-sets="header_default_font" padding-before="3px" space-before="15px" padding-after="4px" space-after="10px">
					<fo:external-graphic content-height="200px" content-width="200px">
						<xsl:attribute name="src">
							<xsl:value-of select="concat($imagePath_var,'view_withdrawal_request_ttl.gif')"/>
						</xsl:attribute>
					</fo:external-graphic>
			</fo:block>
			
			<!-- GIFL 1C Start -->
			<xsl:if test="withdrawalRequestUi/isParticipantGIFLEnabled = 'true'">
			<xsl:if test="withdrawalRequestUi/withdrawalRequest/statusCode = 'W5'
							or withdrawalRequestUi/withdrawalRequest/statusCode = 'W6'">
				<xsl:choose>
					<xsl:when test="withdrawalRequestUi/withdrawalRequest/isParticipantCreated = 'true'">
						<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="1px"
							space-after="6px">
							<xsl:value-of select="withdrawalRequestUi/cmaContent/participantInitiatedRequestMessage" />
     						</fo:block>
					</xsl:when>
					<xsl:otherwise>
						<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="1px"
							space-after="6px">
							<xsl:value-of select="withdrawalRequestUi/cmaContent/psTPAInitiatedRequestMessage" />
     						</fo:block>
					</xsl:otherwise>
				</xsl:choose>
     		</xsl:if>
     		</xsl:if>
			<!-- GIFL 1C end -->
			
			</xsl:if>
			
			<xsl:if test="withdrawalRequestUi/isConfirmAction = 'true'">
			<fo:block xsl:use-attribute-sets="header_default_font" padding-before="3px" space-before="15px"  >
					<fo:external-graphic content-height="200px" content-width="200px">
						<xsl:attribute name="src">
							<xsl:value-of select="concat($imagePath_var,'withdrawal_request_ttl_for_pdf.gif')"/>
						</xsl:attribute>
					</fo:external-graphic>
			</fo:block>
			</xsl:if>
			
			
			<xsl:if test="withdrawalRequestUi/isConfirmAction = 'true'">
				<xsl:if test="withdrawalRequestUi/isRequestSendForReview = 'true'">
					<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="1px"
						space-after="6px">
						<xsl:value-of select="withdrawalRequestUi/cmaContent/confirmationText1" />
      				</fo:block>
     			</xsl:if>
     			<xsl:if test="withdrawalRequestUi/isRequestSendForApprove = 'true'">
    				<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="1px" 
    					space-after="6px">
						<xsl:value-of select="withdrawalRequestUi/cmaContent/confirmationText2" />
     				</fo:block>
    			</xsl:if>
    			<xsl:if test="withdrawalRequestUi/isRequestApproved = 'true'">
    				<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="1px"
    					space-after="6px">
						<xsl:value-of select="withdrawalRequestUi/cmaContent/confirmationText3" />
     				</fo:block>
    			</xsl:if>
			</xsl:if>
			
			
			<fo:block >
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="30%" />
					<fo:table-column column-width="30%" />
					<fo:table-column column-width="40%" />
					<fo:table-body>
						<fo:table-row number-columns-spanned="3">
							<fo:table-cell xsl:use-attribute-sets = "general_info_bold_font">
								<xsl:if test="withdrawalRequestUi/isViewAction = 'true'">
									<fo:block>
										Submission number
		         					</fo:block>
	         					</xsl:if>
	         					<xsl:if test="withdrawalRequestUi/isConfirmAction = 'true'">
	         						<fo:block>
										Submission number:
		         					</fo:block>
	         					</xsl:if>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="submission_info_bold_font" >
								<fo:block>
									 <xsl:value-of select="withdrawalRequestUi/withdrawalRequest/submissionId" /> 
	         					</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row number-columns-spanned="3">
							<xsl:if test="withdrawalRequestUi/isViewAction = 'true'">
								<fo:table-cell xsl:use-attribute-sets = "general_info_bold_font">
									<fo:block>
										Status
		         					</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="submission_info_bold_font">
									<xsl:variable name="statusCode_var">
										<xsl:value-of select='withdrawalRequestUi/withdrawalRequest/statusCode'/>
									</xsl:variable>
									<fo:block>
										<xsl:for-each select="lookupData/WITHDRAWAL_REQUEST_STATUS_ORDERED/item[@key=$statusCode_var]">
											<xsl:value-of select="@value"/>
										</xsl:for-each>
									</fo:block>	
								</fo:table-cell>
							</xsl:if>
							<xsl:if test="withdrawalRequestUi/isConfirmAction = 'true'">
								<fo:table-cell>
									<fo:block/>					
								</fo:table-cell>
								<fo:table-cell>
									<fo:block/>
								</fo:table-cell>
							</xsl:if>
							
						</fo:table-row>
						
						<!--If the request is in Approved state then the following information should be display otherwise no need. -->
						<!-- start -->
						<xsl:if test="withdrawalRequestUi/isRequestApproved = 'true'"> 
						<fo:table-row number-columns-spanned="3">
							<fo:table-cell xsl:use-attribute-sets = "general_info_bold_font">
								<xsl:if test="withdrawalRequestUi/isViewAction = 'true'">
									<fo:block>
										Approved date/time
		         					</fo:block>
	         					</xsl:if>
	         					<xsl:if test="withdrawalRequestUi/isConfirmAction = 'true'">
	         						<fo:block>
										Approved date/time:
		         					</fo:block>
	         					</xsl:if>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="submission_info_bold_font">
								<fo:block>
									 <xsl:value-of select="withdrawalRequestUi/withdrawalRequest/approvedTimestamp" />
	         					</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						<xsl:if test="withdrawalRequestUi/showExpectedProcessingDate = 'true'"> 
						<fo:table-row number-columns-spanned="3">
							<fo:table-cell xsl:use-attribute-sets = "general_info_bold_font">
								<xsl:if test="withdrawalRequestUi/isViewAction = 'true'">
									<fo:block>
										Expected processing date
		         					</fo:block>
	         					</xsl:if>
	         					<xsl:if test="withdrawalRequestUi/isConfirmAction = 'true'">
	         						<fo:block>
										Expected processing date:
		         					</fo:block>
	         					</xsl:if>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="submission_info_bold_font">
								<fo:block>
									 <xsl:value-of select="withdrawalRequestUi/withdrawalRequest/expectedProcessingDate" />
	         					</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if> 
						<!-- end -->
					</fo:table-body>
				</fo:table>
			</fo:block>
	</xsl:template>
</xsl:stylesheet>