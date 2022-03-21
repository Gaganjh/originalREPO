<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<xsl:template name="CoverPageTemplate" >
		
		<fo:page-sequence master-reference="CoverPageLayout">
			
			<fo:static-content flow-name="xsl-region-before">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="196px" />
					<fo:table-column column-width="354px" />
					<fo:table-column column-width="6px" />
					<fo:table-column column-width="36px" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell>
								<fo:block start-indent="18px" >
									<fo:external-graphic content-height="55px" content-width="150px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'JH_RPS_Logo-193x81.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="2">
								<fo:block start-indent="240px" text-align="text">
									<fo:external-graphic content-height="55px" content-width="105px" alignment-adjust="right">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'IPSManager_Logo-115x55.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="3">
								<xsl:choose>
									<xsl:when test="annual_review_report/isInterim = 'true'">
										<fo:block xsl:use-attribute-sets="header_block_cell3" font-size="28pt" 
											padding-before="50px" text-align="right">
											Interim Review Report 
										</fo:block>
									</xsl:when>
									<xsl:otherwise>
										<fo:block xsl:use-attribute-sets="header_block_cell3" font-size="28pt" 
											start-indent="291px" padding-before="50px">
											Annual Review Report
										</fo:block>
									</xsl:otherwise>
								</xsl:choose>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>
			
			<fo:static-content flow-name="xsl-region-after">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="128px" />
					<fo:table-column column-width="1px" />
					<fo:table-column column-width="428px" />
					<fo:table-column column-width="20px" />
					<fo:table-column column-width="18px" />
					<fo:table-body>
						<fo:table-row height="1px" background-color="#FFFFFF">
							<fo:table-cell number-columns-spanned="4">
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row height="98px">
							<fo:table-cell background-color="#CB5A27">
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell background-color="#FFFFFF">
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell background-color="#CB5A27">
								<xsl:choose>
									<xsl:when test="annual_review_report/isInterim = 'true'">
										<fo:block xsl:use-attribute-sets="page_count" text-align="right" font-size="8pt" padding-before="28px">
											Interim review report for:   <fo:inline xsl:use-attribute-sets="sub_header_block2">
																	<xsl:value-of select="annual_review_report/contractName"/>
																</fo:inline>
										</fo:block>
										<fo:block xsl:use-attribute-sets="page_count" text-align="right" font-size="8pt" padding-before="3px">
											Contract number:   <fo:inline xsl:use-attribute-sets="sub_header_block2">
															<xsl:value-of select="annual_review_report/contractNumber"/>
													   </fo:inline>
										</fo:block>
										<fo:block xsl:use-attribute-sets="page_count" text-align="right" font-size="8pt" padding-before="3px">
											Prepared on:   <fo:inline xsl:use-attribute-sets="sub_header_block2"><xsl:value-of select="annual_review_report/currentDate"/></fo:inline>
										</fo:block>
									</xsl:when>
									<xsl:otherwise>
										<fo:block xsl:use-attribute-sets="page_count" text-align="right" font-size="8pt" padding-before="28px">
											Annual review report for:   <fo:inline xsl:use-attribute-sets="sub_header_block2">
																	<xsl:value-of select="annual_review_report/contractName"/>
																</fo:inline>
										</fo:block>
										<fo:block xsl:use-attribute-sets="page_count" text-align="right" font-size="8pt" padding-before="3px">
											Contract number:   <fo:inline xsl:use-attribute-sets="sub_header_block2">
															<xsl:value-of select="annual_review_report/contractNumber"/>
													   </fo:inline>
										</fo:block>
										<fo:block xsl:use-attribute-sets="page_count" text-align="right" font-size="8pt" padding-before="3px">
											Prepared on:   <fo:inline xsl:use-attribute-sets="sub_header_block2"><xsl:value-of select="annual_review_report/processingDate"/></fo:inline>
										</fo:block>
									</xsl:otherwise>
								</xsl:choose>							
								
							</fo:table-cell>
							<fo:table-cell background-color="#CB5A27">
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block ></fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="footer_disclaimer"  padding-before="10px" text-align="right">
									FOR PLAN SPONSOR USE ONLY. NOT FOR USE WITH PLAN PARTICIPANTS.
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
						
					</fo:table-body>
				</fo:table>
			</fo:static-content> 
			
			<fo:flow flow-name="xsl-region-body">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="128px" />
					<fo:table-column column-width="1px" />
					<fo:table-column column-width="448px" />
					<fo:table-column column-width="18px" />
					<fo:table-body>
						<fo:table-row height="468px">
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell background-color="#FFFFFF">
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell >
								<fo:block>
									<fo:external-graphic content-height="467px" content-width="448px" 
										scaling="non-uniform"> 
										<xsl:choose>
											<xsl:when test="annual_review_report/isInterim = 'true'">
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath,'PS19986-GE_IPSInterimReviewReportIMAGE.jpg')"/>
												</xsl:attribute>
											</xsl:when>
											<xsl:otherwise>
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath,'CoverPageImage-449x469.jpg')"/>
												</xsl:attribute>
											</xsl:otherwise>
										</xsl:choose>
										<!--  <xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'CoverPageImage-449x469.jpg')"/>
										</xsl:attribute>-->
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:flow>
			
		</fo:page-sequence>	
	</xsl:template>
</xsl:stylesheet>