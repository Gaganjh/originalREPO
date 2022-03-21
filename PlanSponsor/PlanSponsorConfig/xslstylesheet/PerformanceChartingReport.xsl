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

<xsl:attribute-set name="color">
	<xsl:attribute name="padding-start">0.1cm</xsl:attribute>
	<xsl:attribute name="padding-end">0.1cm</xsl:attribute>
	<xsl:attribute name="padding-before">0.1cm</xsl:attribute>
	<xsl:attribute name="padding-after">0.1cm</xsl:attribute>
	<xsl:attribute name="background-color">&#35;<xsl:value-of select="chartLineColor" /></xsl:attribute>
</xsl:attribute-set>

<xsl:include href="reportCommonHeader.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />

	<xsl:template match="performanceChart">
	
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- 8.5x11 page.-->
			<fo:layout-master-set>
				<fo:simple-page-master margin-bottom="1.0cm" margin-left="0.5cm" margin-right="0.5cm" margin-top="1.0cm" master-name="pageLandscapeLayout" page-height="21.59cm" page-width="27.94cm">
					<fo:region-body margin-bottom="0.5cm" margin-top="0cm"/>
					<fo:region-before extent="0cm"/>
					<fo:region-after extent="0.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
				
			<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after" >
					<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
						Page <fo:page-number/> of <fo:page-number-citation-last ref-id="terminator"/> NOT VALID WITHOUT ALL PAGES
					</fo:block>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="reportCommonHeader"/>
					<fo:block space-before="0.2cm">
						<fo:table>
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro1Text" />
										</fo:block>
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro2Text" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					<fo:block space-before="0.2cm" keep-with-next="always">
						<fo:table>
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row keep-with-next="always">
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row background-color="#deded8">
									<fo:table-cell padding-start="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt">
											<xsl:apply-templates select="bodyHeader1" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					
					<!-- Showing the chart -->
					<fo:block text-align="center" space-before="0.2cm">
						<fo:external-graphic>
							<xsl:attribute name="src">url('<xsl:value-of select="lineChartURL" />')</xsl:attribute>
						</fo:external-graphic>		
					</fo:block>

					<!-- Showing the report -->
					<fo:block space-before="0.1cm">
						<fo:table>
							<fo:table-column column-width="5%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="35%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
							
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell number-columns-spanned="2" padding-start="0.1cm">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Fund Name
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Initial Investment
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Ending value
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											% Change
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							
							<fo:table-body>
								<xsl:for-each select="fundDetail">
									<fo:table-row display-align="center">
										<fo:table-cell padding-start="0.4cm" padding-end="0.4cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block>
											<fo:table>
												<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
												<fo:table-body>
												<fo:table-row>
													<fo:table-cell xsl:use-attribute-sets="color"><fo:block/></fo:table-cell>
												</fo:table-row>
												</fo:table-body>
											</fo:table>
										</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<fo:inline>
													<xsl:value-of select="fundName" />
												</fo:inline>
												<xsl:for-each select="footnote">
													<fo:inline vertical-align="super">
														<xsl:value-of select="." />
														<fo:character character="&#32;"/>
													</fo:inline>
												</xsl:for-each>	
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												$ <xsl:value-of select="startValue" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												$ <xsl:value-of select="endValue" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="percentChange" /> %
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:for-each>
								<fo:table-row keep-with-previous="always">
									<fo:table-cell>
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>	
										<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>

					<!-- Showing the page footer-->
					<fo:block space-before="0.7cm">
						<fo:block>
							 <fo:table>
								<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell padding-start="1cm" padding-before="0.1cm">							
											<fo:block xsl:use-attribute-sets="arial_font.size_11">	
												<xsl:apply-templates select="footer" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<xsl:if test="footnotes">
										<fo:table-row>
											<fo:table-cell padding-start="1cm" padding-before="0.1cm">
												<fo:block xsl:use-attribute-sets="arial_font.size_11">		
													<xsl:apply-templates select="footnotes" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>	
									<xsl:if test="fundFootnotes/fundFootnote">
										<fo:table-row>
											<fo:table-cell>
												<fo:table>
													<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
													<fo:table-body>
													<xsl:for-each select="fundFootnotes/fundFootnote">
														<fo:table-row>
															<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
																<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
																	<fo:inline vertical-align="super" keep-with-next="always" >
																		<xsl:value-of select="footnoteSymbol" />
																	</fo:inline>
																	<fo:inline>
																		<xsl:value-of select="footnoteText" />
																	</fo:inline>
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
													</xsl:for-each>
													</fo:table-body>
												</fo:table>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<fo:table-row>
										<fo:table-cell padding-start="1cm" padding-before="0.1cm">																			
											<fo:block xsl:use-attribute-sets="arial_font.size_11">		
												<xsl:apply-templates select="disclaimer" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block>
					<fo:block space-before="0.5cm" xsl:use-attribute-sets="arial_font.size_11">
						<xsl:apply-templates select="globalDisclosure" />
					</fo:block> 
					<fo:block id="terminator" />
		
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
</xsl:stylesheet>