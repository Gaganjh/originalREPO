<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!--  Attribute set Declarations  -->
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

<xsl:variable name="txnRunningBalance" select="contractCashAccount/txnDetails/txnDetail/txnRunningBalance" />

<xsl:include href="reportCommonHeader.xsl" />
<xsl:include href="reportCommonFooter.xsl" />
<xsl:include href="pageDefinition.xsl" />
<xsl:include href="infoMessages.xsl" />
	
	<xsl:template match="contractCashAccount">

		
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
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
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
									</fo:table-cell>
									
									<!-- Showing Summary Information -->
									<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" font-weight="bold" background-color="#eceae3">
											<xsl:apply-templates select="summaryInfo/subHeader" />
										</fo:block>
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>Current Balance as of: </fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/currentBalanceAsOfDate" />
											</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>Current Balance: </fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/currentBalance" />
												</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>For Period: </fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="fromDate" />
											</fo:inline>
											<fo:inline> to </fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="toDate" />
											</fo:inline>
										</fo:block>
										<xsl:if test="summaryInfo/openingBalance">
											<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>Opening Balance: </fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/openingBalance" />
												</fo:inline>
											</fo:block>
											<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>Closing Balance: </fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/closingBalance" />
												</fo:inline>
											</fo:block>
										</xsl:if>
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>Total Debits this Period: </fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/totalDebits" />
											</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm" padding-after="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>Total Credits this Period: </fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/totalCredits" />
											</fo:inline>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>

					<!-- Showing Info Messages -->
					<xsl:call-template name="infoMessages"/>	
					
					<xsl:if test="not(infoMessages)">
					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						<fo:table>		
							<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="50%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row background-color="#deded8">
									<fo:table-cell padding-start="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt">
											<xsl:apply-templates select="bodyHeader1" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-end="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt" text-align="right">
											<fo:inline>
											Total transactions: 
											</fo:inline>
											<fo:inline>
												<xsl:value-of select="totalTransactions" />
											</fo:inline>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					</xsl:if>
					
						<fo:block space-before="0.1cm">
							<fo:table>
								<xsl:if test="$txnRunningBalance">
									<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
									<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
									<fo:table-column column-width="28%" xsl:use-attribute-sets="table-borderstyle"/>
									<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
									<fo:table-column column-width="11%" xsl:use-attribute-sets="table-borderstyle"/>
									<fo:table-column column-width="11%" xsl:use-attribute-sets="table-borderstyle"/>
									<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
								</xsl:if>
								<xsl:if test="not($txnRunningBalance)">
								<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="18%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="28%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
								</xsl:if>
								<!-- Showing the report table header-->
								<fo:table-header>
									<fo:table-row background-color="#deded8" display-align="center">
										<fo:table-cell padding-start="0.1cm" text-align="left">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Transaction Date
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Type
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Description
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Transaction Number
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Debits ($)
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Credits ($)
											</fo:block>
										</fo:table-cell>
										<xsl:if test="$txnRunningBalance">
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													Running Balance ($)
												</fo:block>
											</fo:table-cell>
										</xsl:if>
									</fo:table-row>
								</fo:table-header>
								<!-- Showing the report table content-->
								<fo:table-body>
									<xsl:for-each select="txnDetails/txnDetail">
										<fo:table-row display-align="center">
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:value-of select="txnDate" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="txnTypeDescription1" />
												</fo:block>
												<fo:block xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="txnTypeDescription2" />
												</fo:block>
											</fo:table-cell>										
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:for-each select="txnDescriptions/txnDescription">
														<fo:block>
															<xsl:value-of select="." />
														</fo:block>
													</xsl:for-each>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="txnNumber" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="txnDebits" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="txnCredits" />
											</fo:block>
											</fo:table-cell>
											<xsl:if test="txnRunningBalance">
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="txnRunningBalance" />
													</fo:block>
												</fo:table-cell>
											</xsl:if>
										</fo:table-row>
									</xsl:for-each>
									<fo:table-row>
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
										<fo:table-cell>
											<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<xsl:if test="$txnRunningBalance">
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