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

<xsl:include href="reportCommonHeader.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />

	<xsl:template match="contributionTransaction">

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
					<xsl:call-template name="reportCommonHeader"/>
					
					<fo:block space-before="0.5cm">
						 <fo:table>
							<fo:table-column column-width="60%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="40%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" />
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro1Text" />
										</fo:block>
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro2Text" />
										</fo:block>
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="rothMsg" />
										</fo:block>
									</fo:table-cell>

									<!-- Showing Summary Information -->
									<fo:table-cell padding-start="0.3cm">
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" font-weight="bold" background-color="#eceae3">
											<xsl:apply-templates select="summaryInfo/subHeader" />
										</fo:block>
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>
													Transaction Type: 
												</fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/txnType" />
												</fo:inline>
										</fo:block>		
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>
													Payroll Ending Date: 
												</fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/payrollEndDate" />
												</fo:inline>	
										</fo:block>
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Number of Participants: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/noOfParticipants" />
											</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Invested Date: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/investedDate" />
											</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Transaction Number: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/txnNumber" />
											</fo:inline>
										</fo:block>
										<xsl:if test="summaryInfo/contributionEEAmt">
											<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>
												 Employee Contributions: 
												</fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/contributionEEAmt" />
												</fo:inline>
											</fo:block>
										</xsl:if>
										<xsl:if test="summaryInfo/contributionERAmt">
											<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>
												 Employer Contributions:
												</fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/contributionERAmt" />
												</fo:inline>
											</fo:block>
										</xsl:if>
										<xsl:if test="summaryInfo/totalAmt">
											<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>
													Total Contributions:
												</fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/totalAmt" />
												</fo:inline>
											</fo:block>
										</xsl:if>
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
								
								<fo:table-row background-color="#deded8" display-align="left">
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
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="left">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Money Type
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Amount($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Money Type
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Amount($)
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-body>
							<!-- Show nothing if there is no money type report-->
							<xsl:if test="not(moneyTypes/moneyType)">
							<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
							</xsl:if>
							<!-- Showing the money type report-->
								<xsl:for-each select="moneyTypes/moneyType">
								<fo:table-row display-align="left">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											<xsl:value-of select="moneyTypeLeft/moneyTypeDesc" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											<xsl:value-of select="moneyTypeLeft/moneyAmt" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											<xsl:value-of select="moneyTypeRight/moneyTypeDesc" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
										<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											<xsl:value-of select="moneyTypeRight/moneyAmt" />
										</fo:block>
									</fo:table-cell>	
								</fo:table-row>	
								</xsl:for-each>	
							</fo:table-body>
						</fo:table>	
					</fo:block>
					
					<fo:block space-before="0.2cm">
						 <fo:table>
							<fo:table-column xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row background-color="#deded8">
									<fo:table-cell padding-start="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt" >
											<xsl:apply-templates select="bodyHeader2" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>

					<!-- Showing the report -->
					<fo:block space-before="0.1cm">
						 <fo:table>
						 	<xsl:if test="summaryInfo/contributionEEAmt and summaryInfo/contributionERAmt">
								<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="summaryInfo/contributionEEAmt or summaryInfo/contributionERAmt">
								<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="not(summaryInfo/contributionEEAmt) and not(summaryInfo/contributionERAmt)">
								<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Name
										</fo:block>
									</fo:table-cell>
									<xsl:if test="summaryInfo/contributionEEAmt">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employee Contributions($)
										</fo:block>
									</fo:table-cell>
									</xsl:if>
									<xsl:if test="summaryInfo/contributionERAmt">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employer Contributions($)
										</fo:block>
									</fo:table-cell>
									</xsl:if>
									<xsl:if test="summaryInfo/contributionEEAmt and summaryInfo/contributionERAmt">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Total Contributions($)
										</fo:block>
									</fo:table-cell>
									</xsl:if>
								</fo:table-row>
							</fo:table-header>
							
							<fo:table-body>
								<xsl:for-each select="txnDetails/txnDetail">
									<fo:table-row display-align="center">
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block keep-with-next="always" padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="pptName" />
												</fo:block>
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="pptSSN" />
												</fo:block>
										</fo:table-cell>
										<xsl:if test="ancestor::*/summaryInfo/contributionEEAmt">
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="contributionEEAmt" />
											</fo:block>
										</fo:table-cell>
										</xsl:if>
										<xsl:if test="ancestor::*/summaryInfo/contributionERAmt">
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="contributionERAmt" />
											</fo:block>
										</fo:table-cell>
										</xsl:if>
										<xsl:if test="ancestor::*/summaryInfo/contributionEEAmt and ancestor::*/summaryInfo/contributionERAmt">
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="totalAmt" />
											</fo:block>
										</fo:table-cell>
										</xsl:if>
									</fo:table-row>
								</xsl:for-each>
									<fo:table-row keep-with-previous="always">
										<fo:table-cell>
											<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<xsl:if test="summaryInfo/contributionEEAmt">
										<fo:table-cell>
											<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										</xsl:if>
										<xsl:if test="summaryInfo/contributionERAmt">
										<fo:table-cell>
											<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										</xsl:if>
										<xsl:if test="summaryInfo/contributionEEAmt and summaryInfo/contributionERAmt">
										<fo:table-cell>
											<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										</xsl:if>
									</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>

					<!-- Showing the PDF capped message and Footer-->
					<xsl:call-template name="reportCommonFooter"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
</xsl:stylesheet>