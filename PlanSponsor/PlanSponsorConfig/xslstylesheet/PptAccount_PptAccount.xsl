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
					<!--Showing the main Report Information -->
					<fo:block space-before="0.5cm">
						 <fo:table>		
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="80%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row background-color="#deded8">
									<fo:table-cell padding-start="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt">
											<xsl:apply-templates select="bodyHeader2" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.2cm">
										<fo:block padding-before="0.1cm" font-family="Verdana" font-size="11pt">
											<fo:inline>
												Organized by:
											</fo:inline>
											<xsl:if test="(organizedBy = '2')">
												<fo:inline>
													Asset Class
												</fo:inline>
											</xsl:if>
											<xsl:if test="(organizedBy = '3')">
												<fo:inline>
													Risk/Return Category
												</fo:inline>
											</xsl:if>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block space-before="0.05cm">
						<fo:table>
							<xsl:if test="descendant::*/eeContrib">
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="6%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<xsl:if test="not(descendant::*/eeContrib)">
							<fo:table-column column-width="30%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="16%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="16%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="16%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<!-- Showing the main Report header-->
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<xsl:if test="descendant::*/eeContrib">
											<xsl:attribute name="number-rows-spanned">2</xsl:attribute>
										</xsl:if>
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Investment Options
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<xsl:if test="descendant::*/eeContrib">
											<xsl:attribute name="number-rows-spanned">2</xsl:attribute>
										</xsl:if>
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Class
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<xsl:if test="descendant::*/eeContrib">
											<xsl:attribute name="number-rows-spanned">2</xsl:attribute>
										</xsl:if>
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Number Of Units
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<xsl:if test="descendant::*/eeContrib">
											<xsl:attribute name="number-rows-spanned">2</xsl:attribute>
										</xsl:if>
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Unit Value($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<xsl:if test="descendant::*/eeContrib">
											<xsl:attribute name="number-rows-spanned">2</xsl:attribute>
										</xsl:if>
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Balance($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<xsl:if test="descendant::*/eeContrib">
											<xsl:attribute name="number-rows-spanned">2</xsl:attribute>
										</xsl:if>
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											% Of Total
										</fo:block>
									</fo:table-cell>
									<xsl:if test="descendant::*/eeContrib">
										<fo:table-cell padding-start="0.1cm" number-columns-spanned="2" xsl:use-attribute-sets="solid.blue.border">
											<fo:block text-align="center" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Ongoing Contributions(%)
											</fo:block>
										</fo:table-cell>
									</xsl:if>
								</fo:table-row>
								<xsl:if test="descendant::*/eeContrib">
								<fo:table-row background-color="#deded8" display-align="center">

									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employee
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employer
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row keep-with-previous="always">
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
								</fo:table-row>
								</xsl:if>
							</fo:table-header>
							<fo:table-body>
							<!-- Show nothing if there is no main Report content-->
							<xsl:if test="not(reportDetails/reportDetail)">
							<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
							</xsl:if>
					<!-- Showing the main Report content-->
					<xsl:for-each select="reportDetails/reportDetail">
								<fo:table-row height="0.1cm">
									<fo:table-cell>
										<xsl:if test="descendant::*/eeContrib">
											<xsl:attribute name="number-columns-spanned">8</xsl:attribute>
										</xsl:if>
										<xsl:if test="not(descendant::*/eeContrib)">
											<xsl:attribute name="number-columns-spanned">6</xsl:attribute>
										</xsl:if>
										<fo:block/>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<xsl:if test="descendant::*/eeContrib">
											<xsl:attribute name="number-columns-spanned">8</xsl:attribute>
										</xsl:if>
										<xsl:if test="not(descendant::*/eeContrib)">
											<xsl:attribute name="number-columns-spanned">6</xsl:attribute>
										</xsl:if>
										<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row background-color="#deded8" keep-with-previous="always">
									<fo:table-cell padding-start="0.2cm">
										<xsl:if test="descendant::*/eeContrib">
											<xsl:attribute name="number-columns-spanned">8</xsl:attribute>
										</xsl:if>
										<xsl:if test="not(descendant::*/eeContrib)">
											<xsl:attribute name="number-columns-spanned">6</xsl:attribute>
										</xsl:if>
										<fo:block font-family="Verdana" font-size="14pt">
											<xsl:value-of select="fundCategory"/>
												<xsl:if test="giflCategory"> * </xsl:if>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
									<xsl:for-each select="fundDetail">
										<fo:table-row display-align="center">	
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
													<xsl:value-of select="fundName" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="fundClass" />
													<xsl:if test="pbFundCategory">-</xsl:if>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:if test="noOfUnits">
															<xsl:value-of select="noOfUnits" />
														</xsl:if>
														<xsl:if test="not(noOfUnits) and not(pbFundCategory)">-</xsl:if>
														<xsl:if test="pbFundCategory">-</xsl:if>
													</fo:inline>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:if test="compositeRate">
															<xsl:value-of select="compositeRate" />%
														</xsl:if>
														<xsl:if test="unitValue">
															<xsl:value-of select="unitValue" />
														</xsl:if>
														<xsl:if test="eeErUnitValue">
															<xsl:value-of select="eeErUnitValue" />
														</xsl:if>
														<xsl:if test="pbFundCategory">-</xsl:if>
													</fo:inline>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:if test="totalBalance">
															<xsl:value-of select="totalBalance" />
														</xsl:if>
													</fo:inline>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:if test="totalPercent">
															<xsl:value-of select="totalPercent" />
														</xsl:if>
													</fo:inline>
												</fo:block>
											</fo:table-cell>
											<xsl:if test="eeContrib">
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>					
														<xsl:value-of select="eeContrib" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>
											</xsl:if>
											<xsl:if test="erContrib">
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:value-of select="erContrib" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>
											</xsl:if>
										</fo:table-row>		
										<fo:table-row>
											<fo:table-cell padding-start="0.3cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													Employee Assets
												</fo:block>
											</fo:table-cell>
											<fo:table-cell  xsl:use-attribute-sets="solid.blue.border.3sides"><fo:block></fo:block></fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:if test="eeNoOfUnits">
														<xsl:value-of select="eeNoOfUnits" />
													</xsl:if>
													<xsl:if test="not(eeNoOfUnits) and not(pbFundCategory)">-</xsl:if>
													<xsl:if test="pbFundCategory">-</xsl:if>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:if test="eeComposite">
														<xsl:value-of select="eeComposite" />%
													</xsl:if>
													<xsl:if test="noEEComposite">-</xsl:if>
													<xsl:if test="eeErUnitValue">
														<xsl:value-of select="eeErUnitValue" />
													</xsl:if>
													<xsl:if test="pbFundCategory">-</xsl:if>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="eeBalance" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">-</fo:block>
											</fo:table-cell>
											<xsl:if test="eeContrib">
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">-</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">-</fo:block>
											</fo:table-cell>
											</xsl:if>
										</fo:table-row>		
										<fo:table-row>
											<fo:table-cell padding-start="0.3cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													Employer Assets
												</fo:block>
											</fo:table-cell>
											<fo:table-cell  xsl:use-attribute-sets="solid.blue.border.3sides"><fo:block></fo:block></fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:if test="erNoOfUnits">
														<xsl:value-of select="erNoOfUnits" />
													</xsl:if>
													<xsl:if test="not(erNoOfUnits) and not(pbFundCategory)">-</xsl:if>
													<xsl:if test="pbFundCategory">-</xsl:if>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:if test="erComposite">
														<xsl:value-of select="erComposite" />%
													</xsl:if>
													<xsl:if test="noERComposite">-</xsl:if>
													<xsl:if test="eeErUnitValue">
														<xsl:value-of select="eeErUnitValue" />
													</xsl:if>
													<xsl:if test="pbFundCategory">-</xsl:if>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block padding-before="0.2cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="erBalance" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">-</fo:block>
											</fo:table-cell>
											<xsl:if test="eeContrib">
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">-</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block text-align="right" padding-before="0.2cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">-</fo:block>
											</fo:table-cell>
											</xsl:if>
										</fo:table-row>	
										<fo:table-row keep-with-previous="always">
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											<xsl:if test="eeContrib">
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											<fo:table-cell>
												<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#cac8c4" />
											</fo:table-cell>
											</xsl:if>
										</fo:table-row>
									</xsl:for-each>	
								
					</xsl:for-each>
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