<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:attribute-set name="table-borderstyle">
<!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
</xsl:attribute-set>

<xsl:attribute-set name="fonts_group2.size_12">
	<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="arial_font.size_11">
	<xsl:attribute name="font-family">Arial</xsl:attribute>
	<xsl:attribute name="font-size">11pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="verdana_font.size_10">
	<xsl:attribute name="font-family">Verdana</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="colorLC">
	<xsl:attribute name="background-color"><xsl:value-of select="colorLC" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorAG">
	<xsl:attribute name="background-color"><xsl:value-of select="colorAG" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorGR">
	<xsl:attribute name="background-color"><xsl:value-of select="colorGR" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorGI">
	<xsl:attribute name="background-color"><xsl:value-of select="colorGI" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorIN">
	<xsl:attribute name="background-color"><xsl:value-of select="colorIN" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorCN">
	<xsl:attribute name="background-color"><xsl:value-of select="colorCN" /></xsl:attribute>
</xsl:attribute-set>

<xsl:include href="reportCommonHeader.xsl" />

	<xsl:template name="dbAccountCommon">

			<xsl:call-template name="reportCommonHeader"/>
			<fo:block space-before="0.5cm">
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
	
			<!--Showing Report Summary Information -->
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
							<fo:table-cell padding-start="0.2cm">
								<fo:block font-family="Verdana" font-size="14pt">
									<xsl:apply-templates select="bodyHeader1" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				<fo:table space-before="0.05cm">		
					<fo:table-column column-width="27%" xsl:use-attribute-sets="table-borderstyle"/>
					<fo:table-column column-width="23%" xsl:use-attribute-sets="table-borderstyle"/>
					<fo:table-column column-width="1%" xsl:use-attribute-sets="table-borderstyle"/>
					<fo:table-column column-width="49%" xsl:use-attribute-sets="table-borderstyle"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell background-color="#eceae3">
								<fo:block font-weight="bold" padding-before="0.1cm" text-align="right"  xsl:use-attribute-sets="verdana_font.size_10">		
									Name:	
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
								<fo:block  padding-before="0.1cm"  xsl:use-attribute-sets="verdana_font.size_10">		
									<fo:inline>
										<xsl:value-of select="firstName" />
										<fo:character character="&#32;"/>
									</fo:inline>
									<fo:inline>
										<xsl:value-of select="lastName" />
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell><fo:block/></fo:table-cell>
							<fo:table-cell number-rows-spanned="4" padding-start="0.2cm" background-color="#eceae3">	
								<fo:block font-weight="bold" padding-before="0.2cm" padding-start="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">		
										Asset Allocation by Risk/Return Category
								</fo:block>
								<fo:block padding-before="0.1cm" text-align="center">
									<fo:external-graphic>
										<xsl:attribute name="src">url('<xsl:value-of select="pieChartURL" />')</xsl:attribute>
									</fo:external-graphic>	
									
								</fo:block>
								<fo:block  xsl:use-attribute-sets="verdana_font.size_10">
									<fo:table space-before="0.1cm">
										<fo:table-column column-width="5%" xsl:use-attribute-sets="table-borderstyle"/>
										<fo:table-column column-width="55%" xsl:use-attribute-sets="table-borderstyle"/>
										<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
										<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
										<fo:table-body>
										<xsl:if test="totalAssetsLC">
											<fo:table-row>
												<fo:table-cell xsl:use-attribute-sets="colorLC">
												<fo:block/>
												</fo:table-cell>
												
												<fo:table-cell padding-start="0.3cm">
													<fo:block>	
														Target Date
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.2cm">
													<fo:block text-align="right">
														<xsl:value-of select="totalAssetsLC" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm">
													<fo:block text-align="right"> 
														<xsl:value-of select="totalPercentLC" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row>
											<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
											</fo:table-row>
										</xsl:if>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="colorAG">
											<fo:block/>
											</fo:table-cell>
											<fo:table-cell padding-start="0.3cm">
												<fo:block>	
													Aggressive Growth
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.2cm">
												<fo:block text-align="right">
													<xsl:value-of select="totalAssetsAG" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm">
												<fo:block text-align="right"> 
													<xsl:value-of select="totalPercentAG" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="colorGR">
											<fo:block/>
											</fo:table-cell>
											<fo:table-cell padding-start="0.3cm">
												<fo:block>	
													Growth
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.2cm">
												<fo:block text-align="right">
													<xsl:value-of select="totalAssetsGR" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm">
												<fo:block text-align="right"> 
													<xsl:value-of select="totalPercentGR" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="colorGI">
											<fo:block/>
											</fo:table-cell>
											<fo:table-cell padding-start="0.3cm">
												<fo:block>	
													Growth &amp; Income
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.2cm">
												<fo:block text-align="right">
													<xsl:value-of select="totalAssetsGI" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm">
												<fo:block text-align="right"> 
													<xsl:value-of select="totalPercentGI" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="colorIN">
											<fo:block/>
											</fo:table-cell>
											<fo:table-cell padding-start="0.3cm">
												<fo:block>	
													Income
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.2cm">
												<fo:block text-align="right">
													<xsl:value-of select="totalAssetsIN" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm">
												<fo:block text-align="right"> 
													<xsl:value-of select="totalPercentIN" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="colorCN">
											<fo:block/>
											</fo:table-cell>
											<fo:table-cell padding-start="0.3cm">
												<fo:block>	
													Conservative
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.2cm">
												<fo:block text-align="right">
													<xsl:value-of select="totalAssetsCN" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm">
												<fo:block text-align="right"> 
													<xsl:value-of select="totalPercentCN" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
										</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell background-color="#eceae3">
								<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
									Total Assets:
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
								<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">		
									<xsl:value-of select="totalAssets" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row> 
							<fo:table-cell background-color="#eceae3">
								<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
									Allocated Assets:
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
								<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">		
									<xsl:value-of select="allocatedAssets" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell background-color="#eceae3">
								<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
									<xsl:if test="lastContribDate">
										Last Contribution Date:  
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
								<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
									<xsl:if test="lastContribDate">
										<xsl:value-of select="lastContribDate" />
									</xsl:if>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
					
	</xsl:template>
	
</xsl:stylesheet>