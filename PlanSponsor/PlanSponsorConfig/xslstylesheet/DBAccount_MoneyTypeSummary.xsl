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

<xsl:include href="dbAccountCommonDetails.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />
<xsl:include href="infoMessages.xsl" />


	<xsl:template match="dbAccount">
	
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
					<xsl:call-template name="dbAccountCommon"/>
					<!-- Showing Info Messages -->
					<xsl:call-template name="infoMessages"/>	
					<!--Showing the main Report Information -->
					<xsl:if test="asOfDateReportCurrent">
					<fo:block space-before="0.2cm">
						<fo:table>
							<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="60%" xsl:use-attribute-sets="table-borderstyle"/>
							<!-- Showing the main Report header-->
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Money Types
										</fo:block>
									</fo:table-cell>	
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
										<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Total Assets($)
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
							</fo:table-header>
					
					<fo:table-body>
								<!-- Show nothing if there is no main Report content-->
								<xsl:if test="not(reportDetail)">
									<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
								</xsl:if>
								<xsl:if test="reportDetail">	
								    <fo:table-row height="0.5cm"><fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="solid.blue.border.4sides"><fo:block /></fo:table-cell></fo:table-row>
								<!-- Showing the main Report content-->
									<fo:table-row display-align="center">	
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides" text-align="left">
											<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Employer contributions
											</fo:block>
										</fo:table-cell>	
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides" text-align="right">
											<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="reportDetail/erContrib" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<xsl:for-each select="reportDetail/erMoneyType">	
										<fo:table-row>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="moneyType" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides" text-align="right">
												<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="balance" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>	
									</xsl:for-each>
									<fo:table-row height="0.5cm"><fo:table-cell number-columns-spanned="2" xsl:use-attribute-sets="solid.blue.border.4sides"><fo:block /></fo:table-cell></fo:table-row>
									<fo:table-row>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
											<fo:block font-weight="bold" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Total
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.4sides">
											<fo:block text-align="right" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="reportDetail/totalContrib" />
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