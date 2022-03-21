<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="ParticipantInformation">

		<!--Participant information section -->
		<fo:block space-before="15px" page-break-inside="avoid">
			<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell background-repeat="no-repeat"
							xsl:use-attribute-sets="table_header_text_font" number-columns-spanned="2">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/cmaContent/personalInformation" />
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
								Name
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<fo:inline><xsl:value-of select="withdrawalRequestUi/withdrawalRequest/firstName" /></fo:inline>
								<fo:inline>&#160;&#160;</fo:inline>
								<fo:inline><xsl:value-of select="withdrawalRequestUi/withdrawalRequest/lastName" /></fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								SSN
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/participantMaskedSSN" /> 
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Contract number
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								 <xsl:value-of select="withdrawalRequestUi/withdrawalRequest/contractId" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Contract name
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/contractName" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								State of residence
							</fo:block>
						</fo:table-cell>
						
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<xsl:variable name="participantStateOfResidenceCode_var" >
								<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/participantStateOfResidence" />
							</xsl:variable>
							<fo:block>
								<xsl:choose>
									<xsl:when test="withdrawalRequestUi/withdrawalRequest/participantStateOfResidence = 'ZZ'">
										ZZ Outside US
									</xsl:when>
									<xsl:otherwise>
										<fo:inline><xsl:value-of select="$participantStateOfResidenceCode_var" /></fo:inline>
										<fo:inline>&#160;</fo:inline>
										<fo:inline>
										<xsl:for-each select="lookupData/USA_STATE_WITH_MILITARY_TYPE/item[@key=$participantStateOfResidenceCode_var]">
										<xsl:value-of select="@value"/>
										</xsl:for-each>	
										</fo:inline>
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row xsl:use-attribute-sets="border_bottom_style_layout">
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" padding-after="3px">
								Date of birth
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-after="3px">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/birthDate" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row >
                       	<fo:table-cell xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
                       		<fo:block padding-after="3px" space-after="10px">
	                       		<xsl:if test="$showStaticContent_var = 'true'">
	                       			<xsl:value-of select="withdrawalRequestUi/cmaContent/step1PageBeanBody1" />
	                       		</xsl:if>
                       		</fo:block>
                       	</fo:table-cell>
                   	</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
</xsl:stylesheet>