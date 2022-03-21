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
<xsl:attribute-set name="fonts_group1.size_11">
	<xsl:attribute name="font-family">Verdana, Arial, Helvetica, sans-serif</xsl:attribute>
	<xsl:attribute name="font-size">11pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="fonts_group2.size_12">
	<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>
<xsl:include href="pptAccountCommonDetails.xsl" />
<xsl:include href="reportCommonFooter.xsl" />

<xsl:include href="pageDefinition.xsl" />
<xsl:include href="infoMessages.xsl" />

	<xsl:template match="swdSummary">
	
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
			
					<!-- Showing Info Messages -->
					<xsl:call-template name="reportCommonHeader"/>	
					
					<!--Showing the main Report Information 
					<xsl:if test="asOfDateReportCurrent">-->
					<fo:block space-before="0.05cm">
					
					
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
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="body1Text" />
										</fo:block>
										
									</fo:table-cell>

								</fo:table-row>
											</fo:table-body>	
					
					
					</fo:table>
						<xsl:if test="filters/filtersUsed">
					<fo:block space-before="0.5cm">
						<fo:block background-color="#deded8" font-family="Verdana" font-size="14pt">
							Filters used:
						</fo:block>
				  	<fo:table>
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="75%" xsl:use-attribute-sets="table-borderstyle"/>  
							<fo:table-body>
							<fo:table-row>
									<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
										<fo:block font-weight="bold">
										
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
										<fo:block>
									
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							<xsl:if test="filters/lastName">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												Last Name:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/lastName" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
							 	<xsl:if test="filters/withdrawalStatus">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												Withdrawal Status:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/withdrawalStatus" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="filters/withdrawalType">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												Withdrawal Type:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/withdrawalType" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="filters/SSN">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												SSN:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/SSN" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if> 
								
						</fo:table-body>
						</fo:table>  
					
						
						
					</fo:block>
					</xsl:if> 
					
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
											<xsl:apply-templates select="pageName" />
										</fo:block>
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
					
					
					<fo:block space-before="0.1cm">
						<fo:table>
													
								<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="11%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							<!-- Showing the main Report header-->
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Name
										</fo:block>
									</fo:table-cell>
										
												
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block text-align="left" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Withdrawal Status
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block text-align="left" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Withdrawal Type
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block text-align="left" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Setup Date
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block text-align="left" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Calculation Method
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block text-align="left" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Frequency
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block text-align="left" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Last Payment Date
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block text-align="center" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Last Payment Amount($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block text-align="center" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Total Current Assets ($)
										</fo:block>
									</fo:table-cell>
									
									
									
									
								</fo:table-row>	
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
								</fo:table-row>
								</fo:table-header>
								<fo:table-body>
							
									<xsl:for-each select="txnDetails/txnDetail">
								<!-- Showing the main Report content-->
										<fo:table-row keep-together.within-page="always" display-align="center">
									
									
									
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline><xsl:value-of select="lastName" />, </fo:inline>
													<fo:inline><xsl:value-of select="firstName" /></fo:inline>
												</fo:block>
												<fo:block xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="pptSSN" />
												</fo:block>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="wdStatus" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<fo:inline>	<xsl:value-of select="wdType" /></fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="setDate" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="calcMethod" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="frequency" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="lastPayDate" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="lastPayAmount" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="totalAssets" />
											</fo:block>
										</fo:table-cell>
									
									
									
									</fo:table-row>	
								</xsl:for-each>		
									
								
									
									<fo:table-row keep-with-previous="always">
									
										<fo:table-cell>
											<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<fo:table-cell>
											<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<fo:table-cell>
											<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<fo:table-cell>
											<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<fo:table-cell>
											<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<fo:table-cell>
											<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
							</fo:block>
						</fo:block>
						<!--</xsl:if>-->
			
					<!-- Showing the PDF capped message and Footer -->
					<xsl:call-template name="reportCommonFooter"/>	
				</fo:flow>
			
			</fo:page-sequence>
			
		</fo:root>
	</xsl:template>
</xsl:stylesheet>