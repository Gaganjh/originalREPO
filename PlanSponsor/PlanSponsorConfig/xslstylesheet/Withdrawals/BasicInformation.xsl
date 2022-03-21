<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="BasicInformation">
					
		<!--Basic information section -->
		<fo:block space-before="20px" page-break-inside="avoid">
		
			<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell background-repeat="no-repeat" 
							xsl:use-attribute-sets="table_header_text_font" number-columns-spanned="2">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/cmaContent/basicInformation" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
				<fo:table-column column-width="40%" />											
				<fo:table-column column-width="60%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Request date
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/requestDate" /> 
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Expiration date
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/expirationDate" /> 
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="border_right_style" >
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Type of withdrawal
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<xsl:variable name="reasonCode_var" >
								<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/reasonCode" />
							</xsl:variable>
							<fo:block>
								<xsl:for-each select="lookupData/ONLINE_WITHDRAWAL_REASONS/item[@key=$reasonCode_var]">
									<xsl:value-of select="@value"/>
								</xsl:for-each>	
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<!-- if block start -->
					<!-- If showHardshipReasonAndHardshipDescription is true from withdrawalRequestUi object 
						 then display Hardship reason and description -->
					<xsl:if test="withdrawalRequestUi/showHardshipReasonAndHardshipDescription = 'true'"> 
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="border_right_style" >
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Hardship reason
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<xsl:variable name="reasonDetailCode_var" >
								<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/reasonDetailCode" />
							</xsl:variable>
							<fo:block>
								<xsl:for-each select="lookupData/HARDSHIP_REASONS/item[@key=$reasonDetailCode_var]">
									<xsl:value-of select="@value"/>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="border_right_style" >
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Hardship description
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/reasonDescriptionForDisplay" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					<!-- if block end -->
					
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Participant leaving plan?
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
							 	<xsl:choose>
									<xsl:when test="withdrawalRequestUi/withdrawalRequest/participantLeavingPlanInd = 'true'">
										Yes
									</xsl:when>
									<xsl:otherwise>
										No
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<xsl:if test="withdrawalRequestUi/showWmsiPenchecks = 'true'"> 
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" >
								IRA provider
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" >
							<fo:block>
								 <xsl:choose>
									<xsl:when test="withdrawalRequestUi/withdrawalRequest/iraServiceProviderCode = 'W'">
										Wealth Management Systems, Inc.
									</xsl:when>
									<xsl:when test="withdrawalRequestUi/withdrawalRequest/iraServiceProviderCode = 'P'">
										PenChecks
									</xsl:when>
									<xsl:when test="withdrawalRequestUi/withdrawalRequest/iraServiceProviderCode = 'N'">
										Other
									</xsl:when>
									<xsl:otherwise>
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					<xsl:if test="withdrawalRequestUi/showDisabilityDate = 'true'"> 
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" >
								Disability date
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" >
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/disabilityDate" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					<xsl:if test="withdrawalRequestUi/showRetirementDate = 'true'">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" >
								Retirement date
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" >
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/retirementDate" /> 
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					<xsl:if test="withdrawalRequestUi/showTerminationDate = 'true'">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" >
								Termination date
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" >
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/terminationDate" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					<xsl:if test="withdrawalRequestUi/showFinalContributionDate = 'true'">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" >
								Last contribution payroll ending date
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" >
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/mostRecentPriorContributionDate" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					<xsl:if test="withdrawalRequestUi/showFinalContributionDate = 'true'">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" >
								Final contribution date
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" >
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/finalContributionDate" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<fo:table-row >
                       	<fo:table-cell xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
                       		<fo:block padding-after="3px" space-after="10px">
                       			<xsl:value-of select="withdrawalRequestUi/cmaContent/lastProcessedDateCommentText" />
                       		</fo:block>
                       	</fo:table-cell>
                   	</fo:table-row>
					</xsl:if>
					
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" >
								Payment to
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" >
							<xsl:variable name="paymentTo_var" >
								<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/paymentTo" />
							</xsl:variable>
							<fo:block>
								<xsl:for-each select="lookupData/PAYMENT_TO_TYPE/item[@key=$paymentTo_var]">
									<xsl:value-of select="@value"/>
								</xsl:for-each>	
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row xsl:use-attribute-sets="border_bottom_style_layout">
						<fo:table-cell padding-before="2px" space-after="5px" number-columns-spanned="2">
							<fo:block>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<fo:table-row >
                       	<fo:table-cell xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
                       		<fo:block padding-after="3px" space-after="10px">
                        		<xsl:if test="$showStaticContent_var = 'true'">
                       				<xsl:value-of select="withdrawalRequestUi/cmaContent/step1PageBeanBody2" />
                       			</xsl:if>
                       		</fo:block>
                       	</fo:table-cell>
                   	</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
</xsl:stylesheet>