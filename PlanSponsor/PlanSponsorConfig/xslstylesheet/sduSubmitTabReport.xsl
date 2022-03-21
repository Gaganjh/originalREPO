<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:attribute-set name="table-borderstyle">
		<!--<xsl:attribute name="border-style">solid</xsl:attribute> -->
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
	<xsl:include href="infoMessages.xsl" />
	<xsl:template match="SubmitTabDetails">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- 8.5x11 page. -->
			<xsl:call-template name="pageDefinition" />
			<fo:page-sequence
				master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after">
					<fo:block font-weight="bold"
						xsl:use-attribute-sets="arial_font.size_10">
						Page
						<fo:page-number />
						of
						<fo:page-number-citation-last
							ref-id="terminator" />
						NOT VALID WITHOUT ALL PAGES

					</fo:block>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="reportCommonHeader" />
					<fo:block space-before="0.1cm">
						<fo:table>
							<fo:table-column column-width="100%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm"
										padding-after="0.3cm">
										<fo:block padding-before="0.3cm"
											xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro1Text" />
										</fo:block>
										<fo:block padding-before="0.3cm"
											xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro2Text" />
										</fo:block>
										<fo:block padding-before="0.3cm"
											xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="rothMsg" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block space-before="0.1cm">
						<xsl:if test="txnDetails">
							<xsl:for-each select="txnDetails/txnDetail">
								<fo:block space-before="0.1cm">
									<fo:table>
										<fo:table-column column-width="100%"
											xsl:use-attribute-sets="table-borderstyle" />
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell padding-start="2.5cm"
													text-align="">
													<fo:block font-weight="bold"
														xsl:use-attribute-sets="verdana_font.size_10">

														The following file(s) have been
														successfully submitted.
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
								<fo:table space-before="0.2cm">
									<fo:table-column column-width="35%" />
									<fo:table-column column-width="65%" />


									<fo:table-body>
										<fo:table-row keep-together.within-page="always"
											display-align="center">
											<fo:table-cell padding-start="2.5cm"
												text-align="">
												<fo:block font-weight="bold"
													xsl:use-attribute-sets="verdana_font.size_10">

													Contract
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm"
												text-align="">
												<fo:block
													xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="contractNo" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row keep-together.within-page="always"
											display-align="center">
											<fo:table-cell padding-start="2.5cm"
												text-align="">
												<fo:block font-weight="bold"
													xsl:use-attribute-sets="verdana_font.size_10">

													Submission
													<br />
													<xsl:text>&#xA;</xsl:text>
													number
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm"
												text-align="">
												<fo:block
													xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="submissionNumber" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row keep-together.within-page="always"
											display-align="center">
											<fo:table-cell padding-start="2.5cm"
												text-align="">
												<fo:block font-weight="bold"
													xsl:use-attribute-sets="verdana_font.size_10">

													Date received
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm"
												text-align="">
												<fo:block
													xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="submissionTime" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row keep-together.within-page="always"
											display-align="center">
											<fo:table-cell padding-start="2.5cm"
												text-align="">
												<fo:block font-weight="bold"
													xsl:use-attribute-sets="verdana_font.size_10">

													Submitted by
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.1cm"
												text-align="">
												<fo:block
													xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="submitterName" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row keep-together.within-page="always">						
											<fo:table-cell padding-start="2.5cm"
												text-align="top">
												<fo:block font-weight="bold"
													xsl:use-attribute-sets="verdana_font.size_10">

													File name(s)
												</fo:block>
											</fo:table-cell>
											<xsl:if test="fileNames">
												<fo:table-cell padding-start="0.1cm"
													text-align="">
													<xsl:for-each select="fileNames">
														<fo:block
															xsl:use-attribute-sets="verdana_font.size_10">
																<xsl:value-of select="." />
														</fo:block>
													</xsl:for-each>
												</fo:table-cell>
											</xsl:if>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</xsl:for-each>
						</xsl:if>
					</fo:block>
					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="100%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.2cm"
											border-before-style="solid" border-before-color="#febe10" />
										<!--orange color -->
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
					<fo:block space-before="0.8cm">
						<fo:table>
							<fo:table-column column-width="20%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="80%"
								xsl:use-attribute-sets="table-borderstyle" />
							<!-- Showing the report table content -->
							<fo:table-body>
								<xsl:if test="contracTxnDetails">
									<xsl:for-each
										select="contracTxnDetails/contracTxnDetail">
										<fo:table-row keep-together.within-page="always"
											display-align="center">
											<fo:table-cell padding-start="0.2cm">
												<fo:block
													xsl:use-attribute-sets="verdana_font.size_10">
													Contract
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-start="0.2">
												<fo:block
													xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="contractNum" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:if>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<!-- Showing the PDF capped message and Footer -->
					<xsl:call-template name="reportCommonFooter" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>