<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="Withdrawal1099R">
	

	<xsl:if test="../../withdrawalRequest/paymentTo != 'TR'">	
	<!--1099R information  section -->
		<xsl:for-each select="withdrawalRequestRecipient">
			<fo:block space-before="20px" page-break-inside="avoid">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="50%" />
					<fo:table-column column-width="50%" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell background-repeat="no-repeat" 
								xsl:use-attribute-sets="table_header_text_font" number-columns-spanned="2">
								<fo:block>
									<xsl:value-of select="$tax1099rTitle_var" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				
				<xsl:variable name="countryCode_var">
					<xsl:value-of select="address/countryCode" />
				</xsl:variable>
									
				<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
					<fo:table-column column-width="40%" />											
					<fo:table-column column-width="60%" />
					<fo:table-body>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" >
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									Name
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<fo:inline><xsl:value-of select="firstName" /></fo:inline>
									<fo:inline>&#160;&#160;</fo:inline>
									<fo:inline><xsl:value-of select="lastName" /></fo:inline>
									
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" >
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									Address line 1
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:value-of select="address/addressLine1" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" >
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									Address line 2
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:value-of select="address/addressLine2" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" >
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									City 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:value-of select="address/city" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									State
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:value-of select="address/stateCode" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" >
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									ZIP Code
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<xsl:variable name="zipCode1_var">
									<xsl:value-of select="address/zipCode1" />
								</xsl:variable>
								<xsl:variable name="zipCode2_var">
									<xsl:value-of select="address/zipCode2" />
								</xsl:variable>
								<fo:block>
									<xsl:choose>
										<xsl:when test="$zipCode1_var != '' and $zipCode2_var != '' and $countryCode_var = 'USA'">
											<fo:inline><xsl:value-of select="$zipCode1_var" /></fo:inline>
											<fo:inline>-</fo:inline>
											<fo:inline><xsl:value-of select="$zipCode2_var" /></fo:inline>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="concat($zipCode1_var, $zipCode2_var)" />
										</xsl:otherwise>
									</xsl:choose>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" >
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" padding-after="2px">
									Country
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-after="2px">
								<fo:block>
									<xsl:for-each select="../../../../lookupData/COUNTRY_COLLECTION_TYPE/item[@key=$countryCode_var]">
										<xsl:value-of select="@value"/>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<xsl:if test="../showParticipantUsCitizenRow = 'true'"> 
						<xsl:if test="../nonUsPayeeExists = 'true'">
						<xsl:if test=" not(../../../withdrawalRequest/wmsiOrPenchecksSelected = 'true')"> 
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" >
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" padding-after="2px">
									Is participant a U.S. citizen?
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font" padding-after="2px">
							
								<fo:block>
								<xsl:choose>
										<xsl:when test="usCitizenInd = 'true'">
											Yes
										</xsl:when>
										<xsl:when test="usCitizenInd = 'false'">
											No
										</xsl:when>
										<xsl:otherwise>
										
										</xsl:otherwise>
									</xsl:choose>
								</fo:block>
								
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						</xsl:if>
						</xsl:if>
						
						<fo:table-row  xsl:use-attribute-sets="border_bottom_style_layout">
							<fo:table-cell number-columns-spanned="2">
								<fo:block>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row >
		                   	<fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="table_data_default_font">
		                   		<fo:block padding-after="5px" space-after="20px">
		                    		<xsl:if test="$showStaticContent_var = 'true'">
										<xsl:value-of select="../../../cmaContent/step2PageBeanBody2" />
		                    		</xsl:if>    
		                   		</fo:block>
		                   	</fo:table-cell>
		               	</fo:table-row>
	               	
					</fo:table-body>
				</fo:table>
			</fo:block>
		</xsl:for-each>
		</xsl:if>	
	</xsl:template>
</xsl:stylesheet>