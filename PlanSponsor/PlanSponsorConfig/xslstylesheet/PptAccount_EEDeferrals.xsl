<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:attribute-set name="table-borderstyle">
<!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
</xsl:attribute-set>

<xsl:attribute-set name="solid.blue.border">
	<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-start-style">solid</xsl:attribute>
	<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="solid.blue.border.4sides">
	<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-start-style">solid</xsl:attribute>
	<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-end-style">solid</xsl:attribute>
	<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-before-style">solid</xsl:attribute>
	<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
	<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
	<xsl:attribute name="border-after-style">solid</xsl:attribute>
	<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="arial_font.size_10">
	<xsl:attribute name="font-family">Arial</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="verdana_font.size_10">
	<xsl:attribute name="font-family">Verdana</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:include href="pptAccountCommonDetails.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />
<xsl:include href="infoMessages.xsl" />

	<xsl:template match="pptAccount">
	
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- 8.5x11 page.-->
			<xsl:call-template name="pageDefinition"/>
			<fo:page-sequence master-reference="pageLandscapeLayout">
			
				<fo:static-content flow-name="xsl-region-after" >
					<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
						Page <fo:page-number/> of <fo:page-number-citation-last ref-id="terminator"/> NOT VALID WITHOUT ALL PAGES
					</fo:block>
				</fo:static-content>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="pptAccountCommon"/>
					<!-- Showing Info Messages -->
					<xsl:call-template name="infoMessages"/>	
				
					<!-- Showing the main Report content-->
					<xsl:if test="reportDetail">
						<fo:block space-before="0.1cm">
							<fo:table>
								<fo:table-column column-width="33%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="67%" xsl:use-attribute-sets="table-borderstyle"/>
								<!-- Showing the main report content-->
								<fo:table-header>
									<fo:table-row>
										
										<fo:table-cell padding-start="0.1cm" number-columns-spanned="2" xsl:use-attribute-sets="solid.blue.border.4sides">
											<fo:block padding-before="0.2cm" padding-after="0.3cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:apply-templates select="reportDetail/bodyHeader1" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>	
										<fo:table-row>	
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													Net employee contribution
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													$<xsl:value-of select="reportDetail/eeContrib" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>	
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													Maximum hardship amount
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													$<xsl:value-of select="reportDetail/maximumHardshipAmount" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>										
										<fo:table-row>	
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
												<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													Current Deferral on File
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="reportDetail/deferralContribText" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
								</fo:table-header>
								<fo:table-body>	
								<!-- Show nothing if there is no main Report content-->
								<xsl:if test="not(reportDetail/csfACIOn)">
									<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
								</xsl:if>
								<xsl:if test="reportDetail/csfACIOn">
										<xsl:if test="not(reportDetail/personalRateLimit)">
											<fo:table-row>	
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														Scheduled deferral increase
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														Off
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:if>
										<xsl:if test="reportDetail/personalRateLimit">	
											<fo:table-row>
												<fo:table-cell border-collapse="collapse" number-columns-spanned="2" padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														Scheduled deferral increase
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														Date of Next Increase
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/nextIncreaseDate" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row>	
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														Next Increase
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/nextIncreaseValue" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row>	
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														Personal rate limit
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
													<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="reportDetail/personalRateLimit" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell>
													<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
												</fo:table-cell>
												<fo:table-cell>
													<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
												</fo:table-cell>
											</fo:table-row>
										</xsl:if>
										</xsl:if>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</xsl:if>
					
					<!-- Showing the PDF capped message and Footer-->	
					<xsl:call-template name="reportCommonFooter"/> 
				</fo:flow>
			
			</fo:page-sequence>
			
		</fo:root>
	</xsl:template>
</xsl:stylesheet>