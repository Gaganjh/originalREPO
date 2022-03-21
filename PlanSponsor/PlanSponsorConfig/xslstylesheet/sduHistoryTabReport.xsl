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

	<xsl:template name="split-string">
		<xsl:param name="string" />
		<xsl:param name="splitSize" select="3" />
		<xsl:param name="width" select="35" />
		<xsl:choose>
			<xsl:when test="string-length($string) &gt; $width">
				<xsl:value-of select="substring($string,0,$splitSize)" />
				<xsl:text>​</xsl:text>
				<xsl:call-template name="split-string">
					<xsl:with-param name="string">
						<xsl:value-of select="substring($string,$splitSize)" />
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$string" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="SubmitHistoryTabDetails">

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
					<fo:block space-before="0.5cm">
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

					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="100%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm"
											border-before-style="solid" border-before-color="#febe10" /><!--orange 
											color -->
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
						<fo:table table-layout="fixed">
							<fo:table-column column-width="11%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="12%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="40%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="13%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="14%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="10%"
								xsl:use-attribute-sets="table-borderstyle" />
							<!-- Showing the report table header -->
							<fo:table-header>
								<fo:table-row background-color="#deded8"
									display-align="center">

									<fo:table-cell padding-start="0.1cm"
										xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.2cm"
											padding-after="0.1cm"
											xsl:use-attribute-sets="verdana_font.size_10">
											<fo:inline>Submission Number</fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm"
										text-align="left">
										<fo:block padding-before="0.2cm"
											padding-after="0.1cm"
											xsl:use-attribute-sets="verdana_font.size_10">
											<fo:inline>Submission Date/Time </fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm"
										xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.2cm"
											padding-after="0.1cm"
											xsl:use-attribute-sets="verdana_font.size_10">
											<fo:inline>Document Name</fo:inline>

										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm"
										xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.2cm"
											padding-after="0.1cm"
											xsl:use-attribute-sets="verdana_font.size_10">
											Submitted By
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm"
										xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.2cm"
											padding-after="0.1cm"
											xsl:use-attribute-sets="verdana_font.size_10">
											Role
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-start="0.1cm"
										xsl:use-attribute-sets="solid.blue.border" text-align="left">
										<fo:block padding-before="0.2cm"
											padding-after="0.1cm"
											xsl:use-attribute-sets="verdana_font.size_10">
											Upload Status
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<!-- Showing the report table content -->
							<fo:table-body>
								<xsl:if test="txnDetails">
									<xsl:for-each select="txnDetails/txnDetail">
										<fo:table-row keep-together.within-page="always"
											display-align="center">

											<fo:table-cell padding-before="0.075cm"
												padding-after="0.075cm" padding-start="0.075cm"
												padding-end="0.075cm"
												xsl:use-attribute-sets="solid.blue.border.3sides"
												text-align="left">
												<fo:block overflow="hidden" wrap-option="wrap"
													xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="submissionNo" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-before="0.075cm"
												padding-after="0.075cm" padding-start="0.075cm"
												padding-end="0.075cm"
												xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block overflow="hidden" wrap-option="wrap"
													xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:value-of select="submissionDate" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>

											<fo:table-cell padding-before="0.075cm"
												padding-after="0.075cm" padding-start="0.075cm"
												padding-end="0.075cm"
												xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block overflow="hidden" wrap-option="wrap"
													xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:call-template name="split-string">
															<xsl:with-param name="string">
																<xsl:value-of select="submissionDocName" />
															</xsl:with-param>
														</xsl:call-template>
													</fo:inline>
												</fo:block>
											</fo:table-cell>

											<fo:table-cell padding-before="0.075cm"
												padding-after="0.075cm" padding-start="0.075cm"
												padding-end="0.075cm"
												xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block overflow="hidden" wrap-option="wrap"
													xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:value-of select="submittedBy" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>

											<fo:table-cell padding-before="0.075cm"
												padding-after="0.075cm" padding-start="0.075cm"
												padding-end="0.075cm"
												xsl:use-attribute-sets="solid.blue.border" text-align="left">
												<fo:block overflow="hidden" wrap-option="wrap"
													xsl:use-attribute-sets="verdana_font.size_10">
													<fo:inline>
														<xsl:value-of select="subRole" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>


											<fo:table-cell padding-before="0.075cm"
												padding-after="0.075cm" padding-start="0.075cm"
												padding-end="0.075cm"
												xsl:use-attribute-sets="solid.blue.border.3sides"
												text-align="left">
												<fo:block overflow="hidden" wrap-option="wrap"
													xsl:use-attribute-sets="verdana_font.size_10">
													<xsl:value-of select="uploadStatus" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</xsl:if>
								<fo:table-row keep-with-previous="always">
									<fo:table-cell>
										<fo:block border-before-width="0.13cm"
											border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm"
											border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm"
											border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm"
											border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm"
											border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.13cm"
											border-before-style="solid" border-before-color="#deded8" />
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>

					<!-- Showing Info Messages -->
					<xsl:call-template name="infoMessages" />

					<!-- Showing the PDF capped message and Footer -->
					<xsl:call-template name="reportCommonFooter" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

</xsl:stylesheet>