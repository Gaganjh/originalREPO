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
<xsl:include href="HtmlToFoTransform.xsl" />

	<xsl:template match="swdSummary">
	
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- 14x8.5 page.-->
			<xsl:choose>
			<xsl:when test="legalLandscape">
			<fo:layout-master-set>
				<fo:simple-page-master margin-bottom="1.0cm" margin-left="0.5cm" margin-right="0.5cm" margin-top="1.0cm" master-name="pageLandscapeLayout" page-height="21.59cm" page-width="35.56cm">
					<fo:region-body margin-bottom="0.5cm" margin-top="0cm"/>
					<fo:region-before extent="0cm"/>
					<fo:region-after extent="0.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			</xsl:when>
			<xsl:otherwise>
			<!-- 11x8.5 page.-->
			<fo:layout-master-set>
				<fo:simple-page-master margin-bottom="1.0cm" margin-left="0.5cm" margin-right="0.5cm" margin-top="1.0cm" master-name="pageLandscapeLayout" page-height="21.59cm" page-width="27.94cm">
					<fo:region-body margin-bottom="0.5cm" margin-top="0cm"/>
					<fo:region-before extent="0cm"/>
					<fo:region-after extent="0.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			</xsl:otherwise>
		    </xsl:choose>	
				
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
							<fo:table-column column-width="55%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="45%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
									<fo:table-cell padding-start="0.3cm">
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
										<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="body1Text" />
										</fo:block>
										<xsl:if test="rothMsg">
											<fo:block padding-before="0.2cm" padding-after="0.2cm">
												<xsl:apply-templates select="rothMsg"/>
											</fo:block>
										</xsl:if>
									</fo:table-cell>

									<!-- Showing Summary Information -->
									<fo:table-cell padding-start="0.3cm" >
										<fo:table background-color="#eceae3">
											<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell padding-start="0.5cm">
														<fo:block padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" font-weight="bold">
															<xsl:apply-templates select="summaryInfo/subHeader" />
														</fo:block>	
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
										<fo:table padding-start="0.3cm" background-color="#eceae3">
											<fo:table-column column-width="33%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-column column-width="32%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-column column-width="32%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-column column-width="3%" xsl:use-attribute-sets="table-borderstyle"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell number-columns-spanned="2" padding-before="0.2cm">
														<fo:block padding-start="0.3cm" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															<fo:inline>
																Total Participants:
															</fo:inline>
															<fo:inline font-weight="bold">
																<xsl:value-of select="summaryInfo/noOfParticipants" />
															</fo:inline>
														</fo:block>		
													</fo:table-cell>
													<fo:table-cell number-columns-spanned="2"><fo:block /></fo:table-cell>
												</fo:table-row>
												<fo:table-row padding-before="0.1cm">
													<fo:table-cell><fo:block /></fo:table-cell>
													<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
														<fo:block padding-start="0.3cm" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															Total
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-start="0.3cm" padding-before="0.1cm" padding-end="0.1cm">
														<fo:block text-align="right" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
															Average 
														</fo:block>
													</fo:table-cell>
													<fo:table-cell><fo:block /></fo:table-cell>
												</fo:table-row>
												<fo:table-row padding-before="0.1cm">
													<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
														<fo:block text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															Employee Assets:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
														<fo:block padding-start="0.3cm" font-weight="bold" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															<xsl:value-of select="summaryInfo/eeAssetsTotal" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
														<fo:block padding-start="0.3cm" font-weight="bold" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															<xsl:value-of select="summaryInfo/eeAssetsAvg" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell><fo:block /></fo:table-cell>
												</fo:table-row>
												<fo:table-row padding-before="0.1cm">
													<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
														<fo:block padding-start="0.3cm" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															Employer Assets:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
														<fo:block padding-start="0.3cm" font-weight="bold" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															<xsl:value-of select="summaryInfo/erAssetsTotal" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
														<fo:block padding-start="0.3cm" font-weight="bold" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															<xsl:value-of select="summaryInfo/erAssetsAvg" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell><fo:block /></fo:table-cell>
												</fo:table-row>
												<fo:table-row padding-before="0.1cm">
													<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
														<fo:block padding-start="0.3cm" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															Total Assets:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
														<fo:block padding-start="0.3cm" font-weight="bold" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															<xsl:value-of select="summaryInfo/totalAssets" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
														<fo:block padding-start="0.3cm" font-weight="bold" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
															<xsl:value-of select="summaryInfo/totalAssetsAvg" />
														</fo:block>
													</fo:table-cell>
													<fo:table-cell><fo:block /></fo:table-cell>
												</fo:table-row>	
												<xsl:if test="summaryInfo/outstandingLoans">
													<fo:table-row>
														<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
															<fo:block padding-start="0.3cm" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
																Outstanding Loans:
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
															<fo:block padding-start="0.3cm" font-weight="bold" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
																<xsl:value-of select="summaryInfo/outstandingLoans" />
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="0.1cm" padding-end="0.1cm">
															<fo:block padding-start="0.3cm" font-weight="bold" text-align="right" xsl:use-attribute-sets="fonts_group1.size_11">
																<xsl:value-of select="summaryInfo/outstandingLoansAvg" />
															</fo:block>
														</fo:table-cell>
														<fo:table-cell><fo:block /></fo:table-cell>
													</fo:table-row>
												</xsl:if>
											</fo:table-body>
										</fo:table>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					
					
					<!-- Filters - start-->
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
											As of:
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
										<fo:block>
											<xsl:value-of select="filters/asOfDate" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<xsl:if test="filters/quickNamePhrase or filters/customNamePhrase">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												Last Name:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/quickNamePhrase" />
												<xsl:value-of select="filters/customNamePhrase" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="filters/quickDivision or filters/customDivisionPhrase">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												Division:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/quickDivision" />
												<xsl:value-of select="filters/customDivisionPhrase" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="filters/quickAssetsFrom or filters/customAssetsFrom">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												Assets From:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												$<xsl:value-of select="filters/quickAssetsFrom" />
												<xsl:value-of select="filters/customAssetsFrom" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="filters/quickAssetsTo or filters/customAssetsTo">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												Assets To:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												$<xsl:value-of select="filters/quickAssetsTo" />
												<xsl:value-of select="filters/customAssetsTo" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="filters/quickEmpStatus or filters/customEmpStatus">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												Employment Status:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/quickEmpStatus" />
												<xsl:value-of select="filters/customEmpStatus" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>								
								<xsl:if test="filters/quickStatus or filters/customStatus">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												Contribution Status:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/quickStatus" />
												<xsl:value-of select="filters/customStatus" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="filters/quickGatewayChecked or filters/customGatewayChecked">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block font-weight="bold">
												Guaranteed Income feature:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm" padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/quickGatewayChecked" />
												<xsl:value-of select="filters/customGatewayChecked" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<!--  Filters - end-->
					
					<!-- Showing the report -->
					<fo:block space-before="0.2cm">
						<fo:table>
							<xsl:if test="not(descendant::*/outstandingLoans) and not(descendant::*/rothMoney) and not(descendant::*/defaultGateway) and not(descendant::*/division)">
								<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="not(descendant::*/outstandingLoans) and not(descendant::*/rothMoney) and not(descendant::*/defaultGateway) and descendant::*/division">
								<fo:table-column column-width="17%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="not(descendant::*/outstandingLoans) and descendant::*/rothMoney and not(descendant::*/defaultGateway) and not(descendant::*/division)">
								<fo:table-column column-width="16%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="not(descendant::*/outstandingLoans) and descendant::*/rothMoney and not(descendant::*/defaultGateway) and descendant::*/division">
								<fo:table-column column-width="16%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="5%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="not(descendant::*/outstandingLoans) and not(descendant::*/rothMoney) and descendant::*/defaultGateway and not(descendant::*/division)">
								<fo:table-column column-width="17%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="not(descendant::*/outstandingLoans) and not(descendant::*/rothMoney) and descendant::*/defaultGateway and descendant::*/division">
								<fo:table-column column-width="16%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="not(descendant::*/outstandingLoans) and descendant::*/rothMoney and descendant::*/defaultGateway and not(descendant::*/division)">
								<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="6%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="not(descendant::*/outstandingLoans) and descendant::*/rothMoney and descendant::*/defaultGateway and descendant::*/division">
								<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="5%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>		
							</xsl:if>
							<xsl:if test="descendant::*/outstandingLoans and not(descendant::*/rothMoney) and not(descendant::*/defaultGateway) and not(descendant::*/division)">
								<fo:table-column column-width="17%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<xsl:if test="descendant::*/outstandingLoans and not(descendant::*/rothMoney) and not(descendant::*/defaultGateway) and descendant::*/division">
								<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<xsl:if test="descendant::*/outstandingLoans and not(descendant::*/rothMoney) and descendant::*/defaultGateway and not(descendant::*/division)">
								<fo:table-column column-width="17%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="descendant::*/outstandingLoans and not(descendant::*/rothMoney) and descendant::*/defaultGateway and descendant::*/division">
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<xsl:if test="descendant::*/outstandingLoans and descendant::*/rothMoney and not(descendant::*/defaultGateway) and not(descendant::*/division) ">
								<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="6%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<xsl:if test="descendant::*/outstandingLoans and descendant::*/rothMoney and not(descendant::*/defaultGateway) and descendant::*/division ">
								<fo:table-column column-width="11%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="5%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<xsl:if test="descendant::*/outstandingLoans and descendant::*/rothMoney and descendant::*/defaultGateway and not(descendant::*/division) ">
								<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="5%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<xsl:if test="descendant::*/outstandingLoans and descendant::*/rothMoney and descendant::*/defaultGateway and descendant::*/division ">
								<fo:table-column column-width="10%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="9%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="5%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="7%" xsl:use-attribute-sets="table-borderstyle"/>	
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="8%" xsl:use-attribute-sets="table-borderstyle"/>	
							</xsl:if>
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Name
										</fo:block>
									</fo:table-cell>
									<xsl:if test="descendant::*/division">
									   <fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Division
										</fo:block>
									  </fo:table-cell>
									</xsl:if>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Date Of Birth (Age)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employment Status
										</fo:block>
									</fo:table-cell>									
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Contribution Status
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Eligibility Date
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Investment Instruction Type
										</fo:block>
									</fo:table-cell>
									<xsl:if test="descendant::*/rothMoney">
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Roth Money
											</fo:block>
										</fo:table-cell>
									</xsl:if>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employee Assets($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employer Assets($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Total Assets($)
										</fo:block>
									</fo:table-cell>
									<xsl:if test="descendant::*/outstandingLoans">
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="center">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Outstanding Loans($)
											</fo:block>
										</fo:table-cell>
									</xsl:if>
									<xsl:if test="descendant::*/defaultGateway">
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Guaranteed
											</fo:block>
											<fo:block xsl:use-attribute-sets="verdana_font.size_10">
												Income
											</fo:block>
											<fo:block padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												Feature
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
													<fo:inline><xsl:value-of select="lastName" />, </fo:inline>
													<fo:inline><xsl:value-of select="firstName" /></fo:inline>
												</fo:block>
												<fo:block xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="pptSSN" />
												</fo:block>
										</fo:table-cell>
										<xsl:if test="division">
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="division" />
												</fo:block>
											</fo:table-cell>
										</xsl:if>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="dob" />
											</fo:block>
											<xsl:if test="age">
												<fo:block xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>(<xsl:value-of select="age" />)</fo:inline>
												</fo:block>
											</xsl:if>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="employmentStatus" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="status" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="eligibilityDate" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="left">
											<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="investmentInstructionType" />
											</fo:block>
										</fo:table-cell>
										<xsl:if test="rothMoney">
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="rothMoney" />
												</fo:block>
											</fo:table-cell>
										</xsl:if>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="eeAssetsTotal" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="erAssetsTotal" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
											<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="totalAssets" />
											</fo:block>
										</fo:table-cell>
										<xsl:if test="outstandingLoans">
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
												<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="outstandingLoans" />
												</fo:block>
											</fo:table-cell>
										</xsl:if>
										<xsl:if test="defaultGateway">
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="left">
												<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="defaultGateway" />
												</fo:block>
											</fo:table-cell>
										</xsl:if>
									</fo:table-row>
								</xsl:for-each>
									<fo:table-row keep-with-previous="always">
										<fo:table-cell>
											<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<xsl:if test="descendant::*/division">
											<fo:table-cell>
												<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
										</xsl:if>
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
										<fo:table-cell>
											<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<fo:table-cell>
											<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
										</fo:table-cell>
										<xsl:if test="descendant::*/rothMoney">
											<fo:table-cell>
												<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
										</xsl:if>
										<xsl:if test="descendant::*/outstandingLoans">
											<fo:table-cell>
												<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
										</xsl:if>
										<xsl:if test="descendant::*/defaultGateway">
											<fo:table-cell>
												<fo:block border-before-width="0.13cm" border-before-style="solid" border-before-color="#deded8" />
											</fo:table-cell>
										</xsl:if>
									</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>

					<!-- Info message -->
					<xsl:if test="not(txnDetails/txnDetail)">
					<fo:table space-before="0.5cm">
						<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding-start="0.3cm">
									<fo:block font-weight="bold" padding-before="0.2cm" padding-after="0.2cm">Information Message</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell padding-start="0.3cm">
									<fo:block padding-before="0.2cm" padding-after="0.2cm">
										<fo:inline>1</fo:inline>
										<fo:inline>. </fo:inline>
										<fo:inline>Your search criteria produced no results. Please change your search criteria.</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					</xsl:if>
					
					<!-- Showing the PDF capped message and Footer-->
					<xsl:call-template name="reportCommonFooter"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
</xsl:stylesheet>