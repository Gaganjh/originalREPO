<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="DocumentingDueDiligenceTemplate">
		<fo:page-sequence master-reference="DocumentingDueDiligenceLayout">
			
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before" >
				<fo:block-container xsl:use-attribute-sets="header_block_cell1">
					<fo:block-container  xsl:use-attribute-sets="header_block_cell2">
						<fo:block>
						</fo:block>
					</fo:block-container>
				</fo:block-container>
				
				<fo:block-container  xsl:use-attribute-sets="header_block_cell3">
					<fo:block id="DocumentingDueDiligenceId" padding-after="6px" start-indent="18px">
						<fo:inline font-weight="light">Documenting your due diligence</fo:inline>
						<fo:inline xsl:use-attribute-sets="header_block_cell3_desc"> with an Investment Policy Statement</fo:inline>
					</fo:block>
				</fo:block-container>
			</fo:static-content>
			
			<!-- Footer -->
			<fo:static-content flow-name="xsl-region-after" >
				<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="126px"/>
					<fo:table-body>
						<fo:table-row height = "14px">
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="page_count">
									PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminator"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>

			<!-- Body -->
			<fo:flow flow-name="xsl-region-body">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="128px"/>
					<fo:table-column column-width="291px"/>
					<fo:table-column column-width="284px"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell text-align="right">
								<fo:block>
									<fo:external-graphic content-height="432px" content-width="5px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Verticle-rule.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell padding-left="15px">
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="light" line-height="1.5">
									<!-- Word Group -->
									<fo:block space-after="13px" padding-before="-1px">
										As a plan fiduciary, you are responsible for providing appropriate options to meet the varied investment needs of your employees. An Investment Policy Statement (IPS) demonstrates that you have made important fiduciary decisions to prudently select and monitor these investment options, and to select the criteria by which you will do so. This provides a clear framework for making decisions regarding future changes to the investment options available in your plan.
									</fo:block>
									
									<fo:block space-after="13px">
										An IPS template is included at the end of this report. If you choose, it can be used as a starting point to build a complete IPS for your plan. It is not intended to be an IPS that is individualized for the particular needs of your plan. The IPS should be customized to document any fiduciary or investment guidelines that are important and unique for your plan. The IPS, along with this entire report, can be retained in your plan's due diligence file.
									</fo:block>
									
									<fo:block space-after="13px">
										In furnishing the IPS template for your use, John Hancock is not undertaking to provide impartial investment advice or to give advice in a fiduciary capacity or legal advice to you or your plan.
									</fo:block>
									
									<!-- Dynamic Phrase A -->
									<xsl:if test="//reportLayout/sections/section[@sectionId='IVSF']">
										<fo:block>
											For your convenience, a Contract Investment Administration form has been included at the end of this report. You can use this form if you want to make any changes to the Funds currently available to your plan.
										</fo:block>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>