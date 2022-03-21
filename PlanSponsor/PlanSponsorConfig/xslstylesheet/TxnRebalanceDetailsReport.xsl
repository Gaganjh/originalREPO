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

<xsl:attribute-set name="arial_font.size_11">
	<xsl:attribute name="font-family">Arial</xsl:attribute>
	<xsl:attribute name="font-size">11pt</xsl:attribute>
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
<xsl:include href="infoMessages.xsl" />

	<xsl:template match="rebalanceDetails">
	
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- 8.5x11 page.-->
			<xsl:call-template name="pageDefinition"/>	
				
			<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after" >
					<fo:block font-weight="bold" font-size="11pt" xsl:use-attribute-sets="arial_font.size_11">
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
										<xsl:if test="notDBContract">
											<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>
													Name: 
												</fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/pptName" />
												</fo:inline>
											</fo:block>
											<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>
													SSN: 
												</fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/pptSSN" />
												</fo:inline>
											</fo:block>
										</xsl:if>
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
												<fo:inline>
													Invested Date: 
												</fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/investedDate" />
												</fo:inline>	
										</fo:block>
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Request Date: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/requestDate" />
											</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm" padding-before="0.2cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Transaction number: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/txnNumber" />
											</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm" xsl:use-attribute-sets="fonts_group1.size_11" background-color="#eceae3">
											<fo:inline>
												Submission Method: 
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/submissionMethod" />
											</fo:inline>
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
							<xsl:if test="notDBContract">
							<fo:table-column column-width="36%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="18%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<fo:table-column column-width="64%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="18%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="18%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="left">
									<fo:table-cell  padding-start="0.1cm">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Investment Option
										</fo:block>
									</fo:table-cell>
									<xsl:if test="notDBContract">
										<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													Employee Assets ($)
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													% Of Account 
											</fo:block>
										</fo:table-cell>
									</xsl:if>
									<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employer Assets ($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border.3sides">
									<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
										% Of Account 
									</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-body>
								<xsl:for-each select="beforeChangeFundDetails/fundDetail">
									<fo:table-row display-align="center">
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block font-weight="bold" padding-before="0.1cm" text-align="left" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="fundGroup" />
											</fo:block>
										</fo:table-cell>
										<xsl:if test="ancestor::*/notDBContract">
											<fo:table-cell xsl:use-attribute-sets="solid.blue.border"><fo:block></fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="solid.blue.border"><fo:block></fo:block></fo:table-cell>
										</xsl:if>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border"><fo:block></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides"><fo:block></fo:block></fo:table-cell>
										
									</fo:table-row>
										<xsl:for-each select="fundTxn">
											<fo:table-row display-align="center">
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block padding-before="0.1cm" text-align="left" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="fundName" />
													</fo:block>
												</fo:table-cell>
												<xsl:if test="ancestor::*/notDBContract">
													<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
														<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
															<xsl:if test="fundEEAmt">
																<xsl:value-of select="fundEEAmt" />
															</xsl:if>
															<xsl:if test="not(fundEEAmt)">-</xsl:if>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
														<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
															<xsl:if test="fundEEPercent">
																<xsl:value-of select="fundEEPercent" />
															</xsl:if>
															<xsl:if test="not(fundEEPercent)">-</xsl:if>
														</fo:block>	
													</fo:table-cell>
												</xsl:if>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:if test="fundERAmt">
														<xsl:value-of select="fundERAmt" />
													</xsl:if>
													<xsl:if test="not(fundERAmt)">-</xsl:if>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:if test="fundERPercent">
															<xsl:value-of select="fundERPercent" />
														</xsl:if>
														<xsl:if test="not(fundERPercent)">-</xsl:if>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:for-each>
								</xsl:for-each>
									<fo:table-row display-align="center">
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block font-weight="bold" padding-before="0.1cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												Total Amount:
											</fo:block>
										</fo:table-cell>
										<xsl:if test="notDBContract">
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block font-weight="bold" padding-before="0.1cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:if test="beforeChangeFundDetails/txnTotalEEAmt">
														<xsl:value-of select="beforeChangeFundDetails/txnTotalEEAmt" />
													</xsl:if>
													<xsl:if test="not(beforeChangeFundDetails/txnTotalEEAmt)">-</xsl:if>	
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block font-weight="bold" padding-before="0.1cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:if test="beforeChangeFundDetails/txnTotalEEPercent">
														<xsl:value-of select="beforeChangeFundDetails/txnTotalEEPercent" />
													</xsl:if>
													<xsl:if test="not(beforeChangeFundDetails/txnTotalEEPercent)">-</xsl:if>	
												</fo:block>
											</fo:table-cell>
										</xsl:if>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block font-weight="bold" padding-before="0.1cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:if test="beforeChangeFundDetails/txnTotalERAmt">
														<xsl:value-of select="beforeChangeFundDetails/txnTotalERAmt" />
												</xsl:if>
												<xsl:if test="not(beforeChangeFundDetails/txnTotalERAmt)">-</xsl:if>	
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
											<fo:block font-weight="bold" padding-before="0.1cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:if test="beforeChangeFundDetails/txnTotalERPercent">
													<xsl:value-of select="beforeChangeFundDetails/txnTotalERPercent" />
												</xsl:if>
												<xsl:if test="not(beforeChangeFundDetails/txnTotalERPercent)">-</xsl:if>	
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
							</fo:table-body>
						</fo:table>	
					</fo:block>
					
					<fo:block space-before="0.2cm">
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
											<xsl:apply-templates select="bodyHeader2" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					<fo:block space-before="0.1cm">
						<fo:table>
							<xsl:if test="notDBContract">
							<fo:table-column column-width="36%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="18%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							</xsl:if>
							<fo:table-column column-width="64%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="18%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="18%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="left">
									<fo:table-cell padding-start="0.1cm">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Investment Option
										</fo:block>
									</fo:table-cell>
									<xsl:if test="notDBContract">
										<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:if test="notDBContract">
													Employee Assets ($)
												</xsl:if>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border">
											<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
													% Of Account 
											</fo:block>
										</fo:table-cell>
									</xsl:if>
									<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Employer Assets ($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border.3sides">
									<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
										% Of Account 
									</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-body>
								<xsl:for-each select="afterChangeFundDetails/fundDetail">
									<fo:table-row display-align="center">
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block font-weight="bold" padding-before="0.1cm" text-align="left" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="fundGroup" />
											</fo:block>
										</fo:table-cell>
										<xsl:if test="ancestor::*/notDBContract">
											<fo:table-cell xsl:use-attribute-sets="solid.blue.border"><fo:block></fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="solid.blue.border"><fo:block></fo:block></fo:table-cell>
										</xsl:if>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border"><fo:block></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides"><fo:block></fo:block></fo:table-cell>
										
									</fo:table-row>
										<xsl:for-each select="fundTxn">
											<fo:table-row display-align="center">
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block padding-before="0.1cm" text-align="left" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="fundName" />
													</fo:block>
												</fo:table-cell>
												<xsl:if test="ancestor::*/notDBContract">
													<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
														<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
															<xsl:if test="fundEEAmt">
																<xsl:value-of select="fundEEAmt" />
															</xsl:if>
															<xsl:if test="not(fundEEAmt)">-</xsl:if>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
														<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
															<xsl:if test="fundEEPercent">
																<xsl:value-of select="fundEEPercent" />
															</xsl:if>
															<xsl:if test="not(fundEEPercent)">-</xsl:if>
														</fo:block>	
													</fo:table-cell>
												</xsl:if>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:if test="fundERAmt">
														<xsl:value-of select="fundERAmt" />
													</xsl:if>
													<xsl:if test="not(fundERAmt)">-</xsl:if>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:if test="fundERPercent">
															<xsl:value-of select="fundERPercent" />
														</xsl:if>
														<xsl:if test="not(fundERPercent)">-</xsl:if>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:for-each>
								</xsl:for-each>
									<fo:table-row display-align="center">
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block font-weight="bold" padding-before="0.1cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												Total Amount:
											</fo:block>
										</fo:table-cell>
										<xsl:if test="notDBContract">
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block font-weight="bold" padding-before="0.1cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:if test="afterChangeFundDetails/txnTotalEEAmt">
														<xsl:value-of select="afterChangeFundDetails/txnTotalEEAmt" />
													</xsl:if>
													<xsl:if test="not(afterChangeFundDetails/txnTotalEEAmt)">-</xsl:if>	
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
												<fo:block font-weight="bold" padding-before="0.1cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:if test="afterChangeFundDetails/txnTotalEEPercent">
														<xsl:value-of select="afterChangeFundDetails/txnTotalEEPercent" />
													</xsl:if>
													<xsl:if test="not(afterChangeFundDetails/txnTotalEEPercent)">-</xsl:if>	
												</fo:block>
											</fo:table-cell>
										</xsl:if>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block font-weight="bold" padding-before="0.1cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:if test="afterChangeFundDetails/txnTotalERAmt">
														<xsl:value-of select="afterChangeFundDetails/txnTotalERAmt" />
												</xsl:if>
												<xsl:if test="not(afterChangeFundDetails/txnTotalERAmt)">-</xsl:if>	
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
											<fo:block font-weight="bold" padding-before="0.1cm" padding-after="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:if test="afterChangeFundDetails/txnTotalERPercent">
														<xsl:value-of select="afterChangeFundDetails/txnTotalERPercent" />
												</xsl:if>
												<xsl:if test="not(afterChangeFundDetails/txnTotalERPercent)">-</xsl:if>	
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
							</fo:table-body>
						</fo:table>	
					</fo:block>
					
					<!-- Showing Info Messages -->
					<xsl:call-template name="infoMessages"/>	
					
					<fo:block space-before="0.2cm">
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
											<xsl:apply-templates select="bodyHeader3" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					
					<!-- Showing the report -->
					<fo:block space-before="0.1cm">
						<fo:table>
						<xsl:if test="descendant::*/comments">
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="20%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="15%" xsl:use-attribute-sets="table-borderstyle"/>
						</xsl:if>
						<xsl:if test="not(descendant::*/comments)">
							<fo:table-column column-width="30%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="25%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="14%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="13%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="18%" xsl:use-attribute-sets="table-borderstyle"/>
						</xsl:if>
							
							<fo:table-header>
								<fo:table-row background-color="#deded8" display-align="center">
									<fo:table-cell padding-start="0.1cm">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Investment Option
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Money Type
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Amount ($)
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Unit Value
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="center" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Number Of Units
										</fo:block>
									</fo:table-cell>
									<xsl:if test="descendant::*/comments">
									<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
										<fo:block padding-before="0.1cm" padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
											Comments
										</fo:block>
									</fo:table-cell>
									</xsl:if>
								</fo:table-row>
							</fo:table-header>
							
							<fo:table-body>
								<xsl:for-each select="fundDetails/fundDetail">
									<fo:table-row display-align="center">
										<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
											<fo:block font-weight="bold" padding-before="0.1cm" text-align="left" xsl:use-attribute-sets="verdana_font.size_10">
												<xsl:value-of select="fundGroup" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border"><fo:block></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border"><fo:block></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border"><fo:block></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides"><fo:block></fo:block></fo:table-cell>
										<xsl:if test="descendant::*/comments">
										<fo:table-cell xsl:use-attribute-sets="solid.blue.border.3sides"><fo:block></fo:block></fo:table-cell>
										</xsl:if>
									</fo:table-row>
										<xsl:for-each select="fundTxn">
											<fo:table-row display-align="center">
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block padding-before="0.1cm" text-align="left" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="fundName" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block padding-before="0.1cm" text-align="left" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="moneyType" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="fundAmt" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="psUnitValue" />
														<xsl:if test="not(psNumberOfUnits)">%</xsl:if>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell padding-end="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
													<fo:block padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="psNumberOfUnits" />
													</fo:block>
												</fo:table-cell>
												<xsl:if test="comments">
												<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides">
													<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">
														<xsl:value-of select="comments" />
													</fo:block>
												</fo:table-cell>
												</xsl:if>
											</fo:table-row>
										</xsl:for-each>
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
										<xsl:if test="descendant::*/comments">
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