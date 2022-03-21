<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template match="/customization" name="CoverPageTemplate">
		
		<xsl:variable name="presentedByValue">	
			<xsl:value-of select="//presenterName"/><xsl:if test="//presenterFirmName != ''">,&#160;<xsl:value-of select="//presenterFirmName"/>
			</xsl:if>
		</xsl:variable>
		
		<fo:page-sequence master-reference="CoverPageLayout" force-page-count="no-force">
			
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before" >
				<fo:block start-indent="10px">
					<fo:external-graphic content-height="75px" content-width="160px">
						<xsl:attribute name="src">
							<xsl:value-of select="concat($imagePath,'JHRPS-logo-blue.jpg')"/>
						</xsl:attribute>
					</fo:external-graphic>
				</fo:block>
			</fo:static-content>
			
			
			<!-- Body -->
			<fo:flow flow-name="xsl-region-body">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="164px"/>
					<fo:table-column column-width="0.75px"/>
					<fo:table-column column-width="591px"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell padding-right="16px" column-number="3">
								<fo:block text-align="right">
									<fo:external-graphic content-height="71px" content-width="376px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Covert-TITLE.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row height="5px">
							<fo:table-cell number-columns-spanned="3">
								<fo:block/>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row height="90px">
							<fo:table-cell background-color="#CD5806" text-align="right" padding-right="6px">
								<fo:block/>
								<fo:block xsl:use-attribute-sets="cover_page_default_font" space-before="16.5px">
									Prepared on
									<fo:inline xsl:use-attribute-sets="cover_page_display_data_font">
										<xsl:value-of select="//reportPreparedDate"/>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell background-color="#CD5806" text-align="left" padding-left="6px" padding-right="8px">
								<fo:block/>
								<fo:block-container xsl:use-attribute-sets="cover_page_default_font" wrap-option="no-wrap" overflow="hidden" space-before="16.5px" space-after="5px">
									<fo:block>
									Prepared for 
									<fo:inline xsl:use-attribute-sets="cover_page_display_data_font">
										<xsl:value-of select="$companyName"/>
									</fo:inline>
									</fo:block>
								</fo:block-container>
								
								<fo:block-container xsl:use-attribute-sets="cover_page_default_font" space-after="2px">
									<fo:block>
									Presented by
									</fo:block>
								</fo:block-container>
								<fo:block-container xsl:use-attribute-sets="cover_page_display_data_font" overflow="hidden" wrap-option="wrap" height ="25px">
									<fo:block>
										<xsl:value-of select="$presentedByValue"/>
									</fo:block>
								</fo:block-container>
							</fo:table-cell>
						</fo:table-row> 
						<fo:table-row height="0.75px">
							<fo:table-cell number-columns-spanned="3">
								<fo:block/>
							</fo:table-cell>
						</fo:table-row>
					
						<fo:table-row height="297px">
							<fo:table-cell padding-before="-1.50px">
								<fo:block>
									<xsl:choose>
										<xsl:when test="//coverImage = 'standard'">
											<fo:block-container height="0.5px" width="164px" background-color="#FFFFFF">
												<fo:block/>
											</fo:block-container>
											<fo:block-container height="297px" width="164px" background-color="#6C6E78" display-align="after">
												<fo:block xsl:use-attribute-sets="cover_page_disclaimer_text">
													<fo:block>For Plan Sponsor use only.</fo:block>
													<fo:block>Not for use with plan participants.</fo:block>
												</fo:block>
												<fo:block/>
											</fo:block-container>
										</xsl:when>
										<xsl:when test="//coverImage = 'type1'">
											<fo:external-graphic content-height="297px" content-width="164px">
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath,'Cover-Boardroom-left.jpg')"/>
												</xsl:attribute>
											</fo:external-graphic>
										</xsl:when>
										<xsl:when test="//coverImage = 'type2'">
											<fo:external-graphic content-height="297px" content-width="164px">
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath,'Cover-Busman-hardhat-left.jpg')"/>
												</xsl:attribute>
											</fo:external-graphic>
										</xsl:when>
										<xsl:when test="//coverImage = 'type3'">
											<fo:external-graphic content-height="297px" content-width="164px">
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath,'Cover-StoreManager-left.jpg')"/>
												</xsl:attribute>
											</fo:external-graphic>
										</xsl:when>
									 </xsl:choose>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell><fo:block/></fo:table-cell>
							<fo:table-cell padding-before="-1.5px">
								<fo:block>
									<xsl:choose>
										<xsl:when test="//coverImage = 'standard'">
											<fo:block-container height="0.5px" width="591px" background-color="#FFFFFF">
												<fo:block/>
											</fo:block-container>
											<fo:block-container height="297px" width="591px" background-color="#404A55">
												<fo:block/>
											</fo:block-container>
										</xsl:when>
										<xsl:when test="//coverImage = 'type1'">
											<fo:external-graphic content-height="297px" content-width="591px">
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath,'Cover-Boardroom-right.jpg')"/>
												</xsl:attribute>
											</fo:external-graphic>
									 	</xsl:when>
										<xsl:when test="//coverImage = 'type2'">
											<fo:external-graphic content-height="297px" content-width="591px">
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath,'Cover-Busman-hardhat-right.jpg')"/>
												</xsl:attribute>
											</fo:external-graphic>
									 	</xsl:when>
									 	<xsl:when test="//coverImage = 'type3'">
											<fo:external-graphic content-height="297px" content-width="591px">
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath,'Cover-StoreManager-right.jpg')"/>
												</xsl:attribute>
											</fo:external-graphic>
										</xsl:when>
									 </xsl:choose>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>	
				</fo:table>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>