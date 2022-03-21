<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
	<xsl:attribute-set name="solid.blue.border.sides">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-before-style">solid</xsl:attribute>
		<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="solid.blue.border.sides.exceptright">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-before-style">solid</xsl:attribute>
		<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="solid.blue.border.sides.exceptleft">
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-before-style">solid</xsl:attribute>
		<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="solid.blue.border.right">
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="solid.blue.border.left">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
			</xsl:attribute-set>
	<xsl:attribute-set name="table-borderstyle">
       <!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
	</xsl:attribute-set>
	<xsl:attribute-set name="fonts_group2.size_12">
		<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
		<xsl:attribute name="font-size">12pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="reg_estimated_title">
		<xsl:attribute name="font-family">georgia, times, serif</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="arial_font.size_12">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">12pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="arial_font.size_11">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">11pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="arial_font.size_10">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="verdana_font.size_10">
		<xsl:attribute name="font-family">Verdana</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="verdana_font.size_9">
		<xsl:attribute name="font-family">Verdana</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:template match="feeDisclosures">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			
					<fo:layout-master-set>
						<fo:simple-page-master margin-bottom="1.0cm"
							margin-left="0.5cm" margin-right="0.5cm" margin-top="1.0cm"
							master-name="pageLandscapeLayout" page-height="21.59cm"
							page-width="35.56cm">
							<fo:region-body margin-bottom="1.0cm" margin-top="0cm" />
							<fo:region-before extent="0cm" />
							<fo:region-after extent="0.5cm" />
						</fo:simple-page-master>
					</fo:layout-master-set>
				
			<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after">
					<fo:table>
						
						<!--
								
								Setting page number count 
								-->
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
										Page
										<fo:page-number />
										of
										<fo:page-number-citation-last
											ref-id="terminator" />
										NOT VALID WITHOUT ALL PAGES
									</fo:block>
								</fo:table-cell>
								 <fo:table-cell>
									<fo:block font-weight="bold" text-align="right" xsl:use-attribute-sets="arial_font.size_10">
										For Plan Sponsor use only. Not for use with plan participants.</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<fo:table>
							<fo:table-column column-width="40%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="60%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body>
							<!--
								
								Setting JH logo 
								-->
								<fo:table-cell>
									<fo:block>
										<fo:external-graphic content-height="8cm" content-width="8cm">
											<xsl:attribute name="src">
									<xsl:value-of select="jhLogoPath" />
								</xsl:attribute>
										</fo:external-graphic>
									</fo:block>
								</fo:table-cell>
								<!--
								
								Setting page name,contract name, as of Date, 
								-->
								<fo:table-cell padding-before="0.5cm">
									<fo:block text-align="right" font-family="Georgia"
										font-size="16pt">
										<xsl:apply-templates select="pageName" />
									</fo:block>
									<xsl:if test="currentContract">
										<fo:block text-align="right" font-family="Georgia"
											font-size="16pt">
											<xsl:apply-templates select="currentContractName" />
											(
											<xsl:apply-templates select="currentContract" />
											)
										</fo:block>
									</xsl:if>
								</fo:table-cell>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block space-before="0.2cm">
					<fo:table>
					<fo:table-body>
					<fo:table-row>
										<fo:table-cell >
											<fo:block border-before-width="0.1cm"
												border-before-style="solid"  />
											<!--
												orange color
											-->
										</fo:table-cell>
									</fo:table-row>
					<fo:table-row color="#a8b8bf"
										keep-with-previous="always">
										<fo:table-cell padding-start="0.2cm">
											<fo:block font-family="Verdana" font-size="16pt">
												Updates to 408(b)(2) Disclosure Information 
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									</fo:table-body>
					</fo:table>
					
					</fo:block>
					<fo:block space-before="0.4cm" padding-start="0.4cm">
					<fo:table>
					<fo:table-body>
					<fo:table-row>
					<fo:table-cell padding-start="0.4cm" padding-before="0.1cm">
										<fo:block border-before-width="0.1cm"/>
											
											<fo:block xsl:use-attribute-sets="arial_font.size_12">
												<xsl:apply-templates select="regDiscText1" />
											</fo:block>
											
										</fo:table-cell>
					</fo:table-row>
					</fo:table-body>
					</fo:table>
					
					</fo:block>
					<fo:block space-before="0.2cm">
					<!--
					Estimated Cost of John Hancock Recordkeeping Services 
					-->
					<fo:table>
					<fo:table-body>
					<fo:table-row>
					
										<fo:table-cell >
										<fo:block color ="#ffffff" background-color="#002D62" space-before="0.9cm" font-family="Verdana" font-size="14pt">
												Summary as of : <xsl:apply-templates select="asOfDate" />
											</fo:block>
											<fo:block  space-before="0.7cm" xsl:use-attribute-sets="arial_font.size_12">
												<xsl:apply-templates select="regDiscText2" />
											</fo:block>
											<fo:block border-before-width="0.1cm"
												border-before-style="solid"  />
											<!--
												orange color
											-->
										</fo:table-cell>
									</fo:table-row>
					<fo:table-row background-color="#a8b8bf" height="28px" 
										keep-with-previous="always" >
										<fo:table-cell padding-start="0.2cm">
											<fo:block  color ="#ffffff" font-size="18pt" xsl:use-attribute-sets="reg_estimated_title">
												<xsl:apply-templates select="totalAandBlableText" /><xsl:apply-templates select="totalAandB" />%
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									</fo:table-body>
					</fo:table>
					</fo:block>
					
					
					<fo:block>
						
						<fo:table>
						<fo:table-column column-width="19%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="20%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="2%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="19%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="20%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
						<fo:table-body>
						
						<xsl:if test="notPreAlignment">
						<fo:table-row  background-color="#f5f4f0" keep-with-previous="always" >
										<fo:table-cell   padding-start="0.2cm" number-columns-spanned="3">
											<fo:block font-family="Verdana" font-size="14pt" font-weight="bold"  >
												<xsl:apply-templates select="totalAintroductionText" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block />
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm" number-columns-spanned="3">
											<fo:block font-family="Verdana" font-weight="bold" font-size="14pt">
												<xsl:apply-templates select="totalBintroductionText" />																						
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
						
						<fo:table-row  display-align="center" background-color="#eceae3"
										keep-with-previous="always">
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block font-family="Verdana" font-size="14pt">
												<fo:inline font-weight="bold"> Description</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block font-family="Verdana" font-size="14pt">
												<fo:inline font-weight="bold"> Method of Payment</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block text-align="center" font-family="Verdana" font-size="14pt" >
												<fo:inline font-weight="bold"> Amount (%)</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block />
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm" >
											<fo:block font-family="Verdana" font-size="14pt">
												<fo:inline font-weight="bold"> Description</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm" >
											<fo:block font-family="Verdana" font-size="14pt">
												<fo:inline font-weight="bold"> Method of Payment</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border.sides" padding-start="0.2cm">
											<fo:block text-align="center" font-family="Verdana" font-size="14pt" >
												<fo:inline font-weight="bold"> Amount (%)</fo:inline>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									
									<fo:table-row  
										keep-with-previous="always">
										<fo:table-cell number-columns-spanned="3" xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block font-family="Verdana" font-size="14pt">
											<fo:table>
											<fo:table-column column-width="39%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="41%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="20%"
									xsl:use-attribute-sets="table-borderstyle" />
											<fo:table-body>
											<xsl:for-each select="sectionElementsA">
											<fo:table-row>
											<fo:table-cell>
											<fo:block  padding-before="0.1cm" padding-after="0.1cm">
												<fo:inline>
													<xsl:value-of select="description" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											<fo:table-cell>
											<fo:block  padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
												<xsl:value-of select="methodOfPayment" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											<fo:table-cell display-align="after">
											<fo:block text-align="right" padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
												<xsl:value-of select="amount" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											</fo:table-row>
											</xsl:for-each>
											</fo:table-body>
											</fo:table>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block />
										</fo:table-cell>
										<fo:table-cell number-columns-spanned="3" xsl:use-attribute-sets="solid.blue.border.sides"  padding-start="0.2cm">
											<fo:block font-family="Verdana" font-size="14pt">
											<fo:table>
											<fo:table-column column-width="39%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="41%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="20%"
									xsl:use-attribute-sets="table-borderstyle" />
											<fo:table-body>
											<xsl:for-each select="sectionElementsB">
											<fo:table-row>
											<fo:table-cell>
											<fo:block  padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
													<xsl:value-of select="description" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											<fo:table-cell>
											<fo:block  padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
												<xsl:value-of select="methodOfPayment" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											<fo:table-cell display-align="after">
											<fo:block  text-align="right" padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
												<xsl:value-of select="amount" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											</fo:table-row>
											</xsl:for-each>
											</fo:table-body>
											</fo:table>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
							</xsl:if>		
									
						<fo:table-row>
										<fo:table-cell number-columns-spanned="7">
											<fo:block border-before-width="0.1cm"
												border-before-style="solid"  />
											<!--
												orange color
											-->
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row display-align="center" background-color="#a8b8bf"
										keep-with-previous="always">
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm" number-columns-spanned="2">
											<fo:block xsl:use-attribute-sets="reg_estimated_title"  color="#ffffff" font-size="14pt"><fo:inline font-weight="bold">A:</fo:inline>
											<xsl:apply-templates select="totalAlableText" />
											</fo:block>	 
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm" >
											<fo:block text-align="right" xsl:use-attribute-sets="reg_estimated_title" color="#ffffff" font-size="14pt" >
												<xsl:apply-templates select="totalA" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block />
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm" number-columns-spanned="2">
											<fo:block xsl:use-attribute-sets="reg_estimated_title"  color="#ffffff" font-size="14pt"><fo:inline font-weight="bold">B:</fo:inline>
											<xsl:apply-templates select="totalBlableText" />
											</fo:block>	 
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides" padding-start="0.2cm" >
											<fo:block text-align="right" xsl:use-attribute-sets="reg_estimated_title" color="#ffffff" font-size="14pt">
												<xsl:apply-templates select="totalB" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									<xsl:if test="notPreAlignment">
									<fo:table-row  
										keep-with-previous="always">
										<fo:table-cell number-columns-spanned="3" xsl:use-attribute-sets="solid.blue.border.left" padding-start="0.2cm">
											<fo:block font-family="Verdana" font-size="14pt">
											<fo:table>
											<fo:table-column column-width="39%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="41%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="20%"
									xsl:use-attribute-sets="table-borderstyle" />
											<fo:table-body>
											
											<fo:table-row>
											<fo:table-cell>
											
											<fo:block/>
											</fo:table-cell>
											<fo:table-cell>
											
											<fo:block/>
											</fo:table-cell>
											<fo:table-cell>
											
											<fo:block/>
											</fo:table-cell>
											</fo:table-row>
											
											</fo:table-body>
											</fo:table>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block />
										</fo:table-cell>
										<fo:table-cell number-columns-spanned="3" xsl:use-attribute-sets="solid.blue.border.sides" padding-start="0.2cm">
											<fo:block font-family="Verdana" font-size="14pt">
											<fo:table>
											<fo:table-column column-width="80%"
									xsl:use-attribute-sets="table-borderstyle" />
									
								<fo:table-column column-width="20%"
									xsl:use-attribute-sets="table-borderstyle" />
											<fo:table-body>
											<xsl:for-each select="revenueAddendumElementsA">
											<fo:table-row>
											<fo:table-cell>
											<fo:block  padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
													<xsl:value-of select="description" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											
											<fo:table-cell display-align="after" >
											<fo:block  text-align="right" padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
												<xsl:value-of select="amount" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											</fo:table-row>
											</xsl:for-each>
											</fo:table-body>
											</fo:table>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									<fo:table-row  
										keep-with-previous="always">
										<fo:table-cell number-columns-spanned="3" xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block font-family="Verdana" font-size="14pt">
											<fo:table>
											<fo:table-column column-width="39%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="41%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="20%"
									xsl:use-attribute-sets="table-borderstyle" />
											<fo:table-body>
											
											<fo:table-row>
											<fo:table-cell>
											
											<fo:block/>
											</fo:table-cell>
											<fo:table-cell>
											
											<fo:block/>
											</fo:table-cell>
											<fo:table-cell>
											
											<fo:block/>
											</fo:table-cell>
											</fo:table-row>
											
											</fo:table-body>
											</fo:table>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block />
										</fo:table-cell>
										<fo:table-cell number-columns-spanned="3" xsl:use-attribute-sets="solid.blue.border.sides" padding-start="0.2cm">
											<fo:block font-family="Verdana" font-size="14pt">
											<fo:table>
											<fo:table-column column-width="80%"
									xsl:use-attribute-sets="table-borderstyle" />
									
								<fo:table-column column-width="20%"
									xsl:use-attribute-sets="table-borderstyle" />
											<fo:table-body>
											<xsl:for-each select="revenueAddendumElementsB">
											<fo:table-row>
											<fo:table-cell>
											<fo:block  padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
													<xsl:value-of select="description" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											
											<fo:table-cell display-align="after" >
											<fo:block  text-align="right" padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
												<xsl:value-of select="amount" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											</fo:table-row>
											</xsl:for-each>
											</fo:table-body>
											</fo:table>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									</xsl:if>
						
						</fo:table-body>
						</fo:table>
						</fo:block>
					
					<!-- Class Zero Phase 2 Changes. Dollar Based section -->
					<xsl:if test="notPreClassZeroPhaseTwo">
					<fo:block space-before="0.2cm">
					<!--
					Dollar Based : Estimated Cost of John Hancock Recordkeeping Services 
					-->
					<fo:table>
					<fo:table-body>
					<fo:table-row>
					
										<fo:table-cell >
											<fo:block border-before-width="0.1cm"
												border-before-style="solid" border-before-color="#febe10" />
											<!--
												orange color
											-->
										</fo:table-cell>
									</fo:table-row>
					<fo:table-row background-color="#a8b8bf" height="28px" 
										keep-with-previous="always" >
										<fo:table-cell padding-start="0.2cm">
											<fo:block  color ="#ffffff" font-size="18pt" xsl:use-attribute-sets="reg_estimated_title">
												<xsl:apply-templates select="dollarBasedSectionHeading" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									</fo:table-body>
					</fo:table>
					</fo:block>
					
					
					<fo:block>
						
						<fo:table>
						<fo:table-column column-width="49%"
									xsl:use-attribute-sets="table-borderstyle" />
						<fo:table-column column-width="31%"
									xsl:use-attribute-sets="table-borderstyle" />
						<fo:table-column column-width="20%"
									xsl:use-attribute-sets="table-borderstyle" />
								
						<fo:table-body>
						
						<fo:table-row  background-color="#f5f4f0" keep-with-previous="always" >
										<fo:table-cell   padding-bottom="0.2cm" padding-start="0.2cm" padding-top="0.2cm" number-columns-spanned="3">
											<fo:block font-family="Verdana" font-size="14pt" font-weight="bold"  >
												<xsl:apply-templates select="dollarBasedSectionIntro" />
											</fo:block>
										</fo:table-cell>
										
									</fo:table-row>
						
						<fo:table-row  display-align="center" background-color="#eceae3"
										keep-with-previous="always">
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-bottom="0.2cm" padding-start="0.2cm" padding-top="0.2cm">
											<fo:block font-family="Verdana" font-size="14pt">
												<fo:inline font-weight="bold"> Description</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-bottom="0.2cm" padding-start="0.2cm" padding-top="0.2cm">
											<fo:block text-align="left" font-family="Verdana" font-size="14pt">
												<fo:inline font-weight="bold"> Method of Payment</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell  xsl:use-attribute-sets="solid.blue.border" padding-bottom="0.2cm" padding-start="0.2cm" padding-top="0.2cm">
											<fo:block text-align="right" font-family="Verdana" font-size="14pt" >
												<fo:inline font-weight="bold"> Amount ($)</fo:inline>
											</fo:block>
										</fo:table-cell>
										
									</fo:table-row>
									
									
									<fo:table-row  
										keep-with-previous="always">
										<fo:table-cell number-columns-spanned="3" xsl:use-attribute-sets="solid.blue.border" padding-start="0.2cm">
											<fo:block font-family="Verdana" font-size="14pt">
											<fo:table>
											<fo:table-column column-width="49%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="31%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="20%"
									xsl:use-attribute-sets="table-borderstyle" />
											<fo:table-body>
											<xsl:for-each select="sectionElementsDB">
											<fo:table-row>
											<fo:table-cell>
											<fo:block  padding-before="0.1cm" padding-after="0.1cm">
												<fo:inline>
													<xsl:value-of select="description" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											<fo:table-cell>
											<fo:block  text-align="left" padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
												<xsl:value-of select="methodOfPayment" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											<fo:table-cell display-align="after">
											<fo:block text-align="right" padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>
												<xsl:value-of select="amount" /><xsl:value-of select="frequency" />
												</fo:inline>
											</fo:block>
											</fo:table-cell>
											</fo:table-row>
											</xsl:for-each>
											</fo:table-body>
											</fo:table>
											</fo:block>
										</fo:table-cell>
										
									</fo:table-row>
									
						</fo:table-body>
						</fo:table>
						</fo:block>
					</xsl:if>
					<!-- Class Zero Phase 2 - Dollar Based Section Ends -->
					
					<fo:block space-before="0.7cm">
					<fo:table>
						<fo:table-column column-width="81%" />
									<fo:table-column column-width="29%"/>
						<fo:table-body>
						
						<fo:table-row   keep-with-previous="always" >
										<fo:table-cell   padding-start="0.2cm" number-columns-spanned="1">
										
											<fo:block font-family="Verdana" font-size="12pt"  >
												<fo:inline font-weight="bold">Number of Funds Selected: </fo:inline><xsl:apply-templates select="fundsSelected" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell   display-align="right" padding-start="0.2cm" number-columns-spanned="1">
											<fo:block font-family="Verdana" font-size="12pt">
												<fo:inline font-weight="bold">Class of Funds: </fo:inline><xsl:apply-templates select="classOfFunds" />																				
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									</fo:table-body>
									</fo:table>
									</fo:block>
									
									<fo:block space-before="0.7cm">
					<fo:table>
						<fo:table-column column-width="60%" />
									<fo:table-column column-width="40%"/>
						<fo:table-body>
						
						<fo:table-row   keep-with-previous="always" >
										<fo:table-cell   padding-start="0.2cm" number-columns-spanned="1">
										
											<fo:block font-family="Verdana" font-size="12pt">
												<fo:inline font-weight="bold">Shown: </fo:inline><xsl:apply-templates select="shown" />
											</fo:block>
										</fo:table-cell>
										
									</fo:table-row>
									</fo:table-body>
									</fo:table>
									</fo:block>
									
									
					<fo:block space-before="0.7cm">
						
							<fo:table>
								<fo:table-column column-width="100%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-body>
								
										
											<fo:table-row>
												<fo:table-cell padding-start="0.4cm"
													padding-before="0.1cm">
													<fo:block xsl:use-attribute-sets="arial_font.size_12">
														<xsl:apply-templates select="regDiscText3" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											
											<fo:table-row>
												<fo:table-cell padding-start="0.4cm"
													padding-before="0.1cm">
													<fo:block xsl:use-attribute-sets="arial_font.size_12">
														<xsl:apply-templates select="fwiDisclosureText" />
													</fo:block>
													<fo:block xsl:use-attribute-sets="arial_font.size_12">
														<xsl:apply-templates select="restrictedFundsText" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
									
								</fo:table-body>
							</fo:table>
					</fo:block>
					
					
					<fo:block space-before="0.2cm">
					<!--
					Investment Information and John Hancock's Indirect Compensation
					-->
					<fo:table>
					<fo:table-body>
					<fo:table-row>
					
										<fo:table-cell >
											<fo:block border-before-width="0.1cm"
												border-before-style="solid"  />
											<!--
												orange color
											-->
										</fo:table-cell>
									</fo:table-row>
					<fo:table-row background-color="#002D62" height="28px" 
										keep-with-previous="always" >
										<fo:table-cell padding-start="0.2cm"
											>
											<fo:block font-size="18pt" xsl:use-attribute-sets="reg_estimated_title">
												<fo:inline color ="#ffffff">Investment Information and John Hancock's Indirect Compensation</fo:inline>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									</fo:table-body>
					</fo:table>
					</fo:block>
					
					
					<fo:block space-before="0.2cm">
						
						<fo:table>
								<fo:table-column column-width="10%" 
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="1%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="19%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="5%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-column column-width="10%"
									xsl:use-attribute-sets="table-borderstyle" />
								
							<fo:table-header display-align="center">
								<fo:table-row background-color="#deded8" 
									display-align="center" >
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right">
											<fo:block />
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right">
										    <xsl:attribute name="number-columns-spanned">
										         2
										    </xsl:attribute>
											<fo:block />
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#deded8" >
											<fo:block text-align="center" 
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													Investment Services
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell background-color="#f5f4f0"  >
											<fo:block />
										</fo:table-cell>
										<fo:table-cell background-color="#f5f4f0" > 
											<fo:block />
										</fo:table-cell>
										<fo:table-cell background-color="#f5f4f0" > 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													Plan Services
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell background-color="#f5f4f0" > 
											<fo:block />
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block />
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right"> 
											<fo:block />
										</fo:table-cell>
										<fo:table-cell> 
											<fo:block />
										</fo:table-cell>
								</fo:table-row>
								
								
								
								<fo:table-row background-color="#deded8" 
									display-align="center" >
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right"> 
											<fo:block />
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right">
										    <xsl:attribute name="number-columns-spanned">
										         2
										    </xsl:attribute> 
											<fo:block />
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right"> 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													(1)
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													(2)
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block />
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													(3)
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block />
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													(4)
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right"> 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													(5)
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell> 
											<fo:block />
										</fo:table-cell>
								</fo:table-row>
								
								
								<fo:table-row background-color="#deded8" 
									display-align="center" >
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right"> 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
												Fund Code
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" > 
										    <xsl:attribute name="number-columns-spanned">
										         2
										    </xsl:attribute>
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													Fund Name
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right"> 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													Underlying Fund Net Cost (%)
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													Revenue From Underlying Fund (%) (12b-1, STA, Other)
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													+
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													Revenue From Sub-account (%)
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													=
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right" background-color="#f5f4f0" > 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													Total Revenue Used Towards Plan Cost (%)
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.right"> 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													Expense Ratio (%)
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell> 
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_9">
												<fo:inline>
													Redemption Fee (%)
												</fo:inline>
												<fo:inline vertical-align="super">N20</fo:inline>
											</fo:block>
										</fo:table-cell>
								</fo:table-row>
								
								
								
							</fo:table-header>
							<fo:table-body>
								<xsl:for-each select="fundDetails">
									<fo:table-row height="0.1cm">
										<fo:table-cell number-columns-spanned="11">
											<fo:block />
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="11">
											<fo:block border-before-width="0.1cm"
												border-before-style="solid"  />
											<!--
												orange color
											-->
										</fo:table-cell>
									</fo:table-row>
																		
									<fo:table-row 
										keep-with-previous="always">
										<xsl:attribute name="background-color">
										<xsl:value-of select="ColorCode" /></xsl:attribute>
										<xsl:attribute name="color">
										<xsl:value-of select="frontcolor" /></xsl:attribute>
										<fo:table-cell padding-start="0.2cm"
											number-columns-spanned="11">
											<fo:block font-family="Verdana" font-size="14pt">
												<xsl:value-of select="fundGroup" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									
									<xsl:for-each select="fundDetail">
									<fo:table-row display-align="center">
									<xsl:if test="fundTxn/fontWeight">
												<xsl:attribute name="font-weight"><xsl:value-of
													select="fundTxn/fontWeight" /></xsl:attribute>
											</xsl:if>
										<!-- <xsl:if test="fundTxn/rowColor">
												<xsl:attribute name="background-color"><xsl:value-of
													select="fundTxn/rowColor" /></xsl:attribute>
											</xsl:if> -->
											<xsl:attribute name="background-color"><xsl:value-of
													select="rowcolorchange" /></xsl:attribute>
										<fo:table-cell 
												xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block  padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="fundTxn/fundId" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides.exceptright" >
											<fo:block text-align="right" padding-before="0.1cm" padding-after="0.1cm" >
												    <xsl:if test="fundTxn/fwiIndicatorSymbol = 'true'" >
												        <xsl:attribute name="font-weight">
												        bold
												        </xsl:attribute>
												        &#8226;
												    </xsl:if>
												    <xsl:if test="fundTxn/merrillRestrictedSymbol = 'true'" >
												        <xsl:attribute name="font-weight">
												        bold
												        </xsl:attribute>
												        &#35;
												    </xsl:if>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides.exceptleft">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" padding-start="0.1cm">
												<xsl:value-of select="fundTxn/fundName" />
												<xsl:if test="fundTxn/footNoteMarkers">
												<fo:inline vertical-align="super">
												<xsl:apply-templates select="fundTxn/footNoteMarkers" />
												</fo:inline>
												</xsl:if>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="fundTxn/underlyingFundNetCost" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="fundTxn/revenueFromUnderlyingFund" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>+</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="fundTxn/revenueFromSubAccount" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>=</fo:inline>	
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="fundTxn/totalRevenueUsedTowardsPlanCosts" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="fundTxn/expenseRatio" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="fundTxn/redemptionFee" />
											</fo:block>
										</fo:table-cell>
										
									</fo:table-row>
									</xsl:for-each>
									</xsl:for-each>
									<fo:table-row font-weight = "bold" display-align="center">
										<fo:table-cell number-columns-spanned="3" xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="right" padding-before="0.1cm" padding-after="0.1cm">
												<fo:inline>Averages:</fo:inline>
												<fo:inline vertical-align="super">N15</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="theReport/averageUnderlyingFundNetCost" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="theReport/averageRevenueFromUnderlyingFund" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>+</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="theReport/averageRevenueFromSubAccount" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
											<fo:inline>=</fo:inline>	
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="theReport/averageTotalRevenueUsedTowardsPlanCosts" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
											<fo:block text-align="center" padding-before="0.1cm" padding-after="0.1cm">
												<xsl:value-of select="theReport/averageExpenseRatio" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.sides">
										<fo:block />
										</fo:table-cell>
										
									</fo:table-row>
							</fo:table-body>
						</fo:table>
						
					</fo:block>
					
					<!-- Showing the page footer-->
					<fo:block space-before="0.7cm">
						
							<fo:table>
								<fo:table-column column-width="100%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-body>
								
										
											<fo:table-row>
												<fo:table-cell padding-start="0.4cm"
													padding-before="0.1cm">
													<fo:block xsl:use-attribute-sets="arial_font.size_12">
														<xsl:apply-templates select="footer" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										
									
								</fo:table-body>
							</fo:table>
					</fo:block>
					<fo:block space-before="0.7cm">
						
							<fo:table>
								<fo:table-column column-width="100%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-body>
								<xsl:if test="tabFootnotes">
										<xsl:for-each select="tabFootnotes">
											<fo:table-row>
												<fo:table-cell padding-start="0.4cm"
													padding-before="0.1cm">
													<fo:block xsl:use-attribute-sets="arial_font.size_12">
														<fo:inline>
														    <xsl:apply-templates select="value" />
														</fo:inline>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:for-each>
									</xsl:if>
									<xsl:if test="ofn">
										<xsl:for-each select="ofn">
											<fo:table-row>
												<fo:table-cell padding-start="0.4cm"
													padding-before="0.1cm">
													<fo:block xsl:use-attribute-sets="arial_font.size_12">
													<fo:inline vertical-align="super"> <xsl:apply-templates select="key" /></fo:inline>
														<fo:inline>
														    <xsl:apply-templates select="value" />
														</fo:inline>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:for-each>
									</xsl:if>
								</fo:table-body>
							</fo:table>
					</fo:block>
					
					<fo:block space-before="0.7cm">
						
							<fo:table>
								<fo:table-column column-width="100%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-body>
								
										
											<fo:table-row>
												<fo:table-cell padding-start="0.4cm"
													padding-before="0.1cm">
													<fo:block xsl:use-attribute-sets="arial_font.size_12">
														<xsl:apply-templates select="footnotes" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										
									
								</fo:table-body>
							</fo:table>
					</fo:block>
					<fo:block space-before="0.5cm" xsl:use-attribute-sets="arial_font.size_11">
						<xsl:apply-templates select="globalDisclosure" />
					</fo:block>
					<fo:block text-align="right" xsl:use-attribute-sets="arial_font.size_11">
						<xsl:apply-templates select="gaCode" />
					</fo:block>
				<fo:block id="terminator" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>