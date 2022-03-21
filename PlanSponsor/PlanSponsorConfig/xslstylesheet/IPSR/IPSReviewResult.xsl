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

	<xsl:attribute-set name="solid.blue.border.3sides">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="solid.blue.border.last">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.8mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="arial_font.size_10">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="verdana_font.size_10">
		<xsl:attribute name="font-family">Verdana</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="fonts_group1.size_11">
		<xsl:attribute name="font-family">Verdana, Arial, Helvetica, sans-serif</xsl:attribute>
		<xsl:attribute name="font-size">11pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="fonts_group2.size_12">
		<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
		<xsl:attribute name="font-size">12pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:variable name="fundInfoIcon">
		<xsl:value-of select="ips_review_results/fundInfoIconPath"/>
	</xsl:variable>
	
	<xsl:include href="reportCommonHeader.xsl" />
	<xsl:include href="pageDefinition.xsl" />
	<xsl:include href="reportCommonFooter.xsl" />
	
	<xsl:template match="ips_review_results">
		<!-- TODO: Auto-generated template -->
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			
			<xsl:call-template name="pageDefinition"/>
			
			<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after">
					<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
						Page <fo:page-number/> of <fo:page-number-citation-last ref-id="terminator"/> NOT VALID WITHOUT ALL PAGES
					</fo:block>
				</fo:static-content>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="reportCommonHeader"/>
					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="60%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block></fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block></fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm" padding-after="0.3cm" number-columns-spanned="2">
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:value-of select="intro1Text" />
										</fo:block>
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:value-of select="intro2Text" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row background-color="#deded8">
									<fo:table-cell>
										<fo:block font-family="Verdana" font-size="14pt">
											<xsl:value-of select="resultsPageSubHeader" /> <xsl:value-of select="asOfDate" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block space-before="0.05cm">
						<fo:table>
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Asset Class
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Current Fund
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Top-ranked Fund
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Actions selected by Trustee
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-body>
								<xsl:for-each select="fundInstructions/fund_instruction">
									<fo:table-row display-align="center">
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<fo:inline font-weight="bold"><xsl:value-of select="assetClassName" /></fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
											<xsl:for-each select="currentFundInfo/current_fund_info">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="fundName" />
													<xsl:if test="fundInformation">
														<fo:inline font-size="1pt">
														<fo:external-graphic content-width="12px" content-height="8px">
															<xsl:attribute name="src">
																<xsl:value-of select="$fundInfoIcon"/>
															</xsl:attribute>
														</fo:external-graphic>
														</fo:inline>
													</xsl:if>				
												</fo:block>											
											</xsl:for-each>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
											<xsl:for-each select="topFundInfo/top_fund_info">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="fundName" />
													<xsl:if test="fundInformation">
														<fo:inline font-size="1pt">
														<fo:external-graphic content-width="12px" content-height="8px">
															<xsl:attribute name="src">
																<xsl:value-of select="$fundInfoIcon"/>
															</xsl:attribute>
														</fo:external-graphic>
														</fo:inline>
													</xsl:if>
												</fo:block>
											</xsl:for-each>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
											<xsl:for-each select="actionIndicators/string">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10" font-weight="bold">
													<xsl:value-of select="." />
												</fo:block>
											</xsl:for-each>
										</fo:table-cell>
									</fo:table-row>
								</xsl:for-each>
								<fo:table-row background-color="#cac8c4" height="0px">
									<fo:table-cell xsl:use-attribute-sets="solid.blue.border.last" >
										<fo:block>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="solid.blue.border.last" >
										<fo:block>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="solid.blue.border.last" >
				 						<fo:block>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="solid.blue.border.last" >
				 						<fo:block>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						<fo:table space-before="0.40cm">
							<fo:table-column column-width="100%"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-start="0.1cm">
										<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10" text-align="right">
											<xsl:value-of select="iatEffectiveDateText" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									
									<fo:table-cell padding-start="0.1cm">
										<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10" text-align="right">
											Effective Date: <fo:inline font-weight="bold"><xsl:value-of select="iatProcessingDate" /></fo:inline>
										</fo:block>										
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<xsl:call-template name="reportCommonFooter"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>