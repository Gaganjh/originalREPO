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
		<xsl:attribute name="border-start-color">#004059</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.4mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#004059</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.4mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#004059</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="arial_font.size_10">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">11pt</xsl:attribute>
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

	<xsl:attribute-set name="solid.blue.border123">
		<xsl:attribute name="border-start-color">#ECEAE3</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#ECEAE3</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="header_block_cell3"
		use-attribute-sets="header_default_font">
		<xsl:attribute name="font-size">13pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="header_default_font">
		<xsl:attribute name="font-family">Frutiger45Light</xsl:attribute>
		<xsl:attribute name="color">black</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="frutiger_font.size_12_bold">
		<xsl:attribute name="font-family">Frutiger67BoldCn</xsl:attribute>
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="color">white</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="frutiger_57_font.size_8.5">
		<xsl:attribute name="font-family">Frutiger57Cn</xsl:attribute>
		<xsl:attribute name="font-size">8.5pt</xsl:attribute>
		<xsl:attribute name="color">white</xsl:attribute>
	</xsl:attribute-set>


	<xsl:attribute-set name="frutiger_57_font.size_10">
		<xsl:attribute name="font-family">Frutiger57Cn</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="color">white</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="frutiger_57_font.size_13">
		<xsl:attribute name="font-family">Frutiger57Cn</xsl:attribute>
		<xsl:attribute name="font-size">13pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="page_count">
		<xsl:attribute name="font-family">sans-serif</xsl:attribute>
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="color">#FFFFFF</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="display-align">center</xsl:attribute>
	</xsl:attribute-set>

	<xsl:template match="planReviewReport">


		<!-- 8.5x11 page. -->
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<!--<fo:simple-page-master margin-bottom="1.0cm" margin-left="0.5cm" 
					margin-right="0.5cm" margin-top="1.0cm" master-name="pageLandscapeLayout" 
					page-height="21.59cm" page-width="27.94cm"> -->
				<fo:simple-page-master margin-bottom="18px"
					master-name="CoverPageLayout" page-width="11in" page-height="8.5in"
					margin-top="32px" margin-left="18px">
					<fo:region-body margin-top="125px" />
					<fo:region-before extent="148px" precedence="true" />
					<fo:region-start margin-bottom="18px" extent="127px"
						background-color="#d8d8c0" />
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="CoverPageLayout">

				<fo:static-content flow-name="xsl-region-before">
					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="196px" />
						<fo:table-column column-width="18px" />
						<fo:table-column column-width="456px" />
						<fo:table-column column-width="46px" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block start-indent="20px">
										<fo:external-graphic content-height="60px"
											content-width="150px">
											<xsl:attribute name="src">
											<xsl:value-of select="jhLogoPath" />
										</xsl:attribute>
										</fo:external-graphic>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="3">
									<fo:block start-indent="380px" text-align="right">
										<fo:external-graphic height="60px"
											 alignment-adjust="auto"
											content-height="scale-to-fit" content-width="scale-to-fit">
											<xsl:attribute name="src">
											<xsl:value-of select="planReviewSelectedElement/logoImage" />
										</xsl:attribute>
										</fo:external-graphic>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="3">
									<fo:block xsl:use-attribute-sets="frutiger_57_font.size_13"
										text-align="right" start-indent="234px" padding-before="35px"
										padding-top="35px">
										<xsl:apply-templates select="planReviewSelectedElement/subHeader" />
										<fo:inline>
											<xsl:value-of select="planReviewSelectedElement/contractName" />
											|
											<xsl:value-of select="planReviewSelectedElement/contractNumber" />
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:table table-layout="fixed" width="100%" height="100%">
						<fo:table-column column-width="127px" />
						<fo:table-column column-width="1px" />
						<fo:table-column column-width="588px" />
						<fo:table-column column-width="40px" />
						<fo:table-column column-width="22px" />
						<fo:table-body>
							<fo:table-row height="10px" background-color="rgb(65,75,86)">
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell background-color="#FFFFFF">
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="2">
									<fo:block></fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="79px">
								<fo:table-cell background-color="rgb(65,75,86)">
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell background-color="#FFFFFF">
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell background-color="rgb(65,75,86)">
									<fo:block xsl:use-attribute-sets="frutiger_font.size_12_bold"
										text-align="right" padding-before="3px">
										<fo:inline>Plan Review </fo:inline>
									</fo:block>
									<fo:block xsl:use-attribute-sets="frutiger_57_font.size_10"
										text-align="right" font-size="10pt" padding-before="3px">
										<fo:inline>Prepared On </fo:inline>
										<fo:inline>
											<xsl:value-of select="asOfDate" />
										</fo:inline>
									</fo:block>
									<fo:block xsl:use-attribute-sets="frutiger_57_font.size_10"
										text-align="right" font-size="10pt" padding-before="3px">
										<fo:inline>Period ending </fo:inline>
										<fo:inline>
											<xsl:value-of select="planReviewSelectedElement/toDate" />
										</fo:inline>
									</fo:block>
									<fo:block xsl:use-attribute-sets="frutiger_57_font.size_10"
										text-align="right" font-size="10pt" padding-before="3px">
										<fo:inline>Presented by: </fo:inline>
										<fo:inline>
											<xsl:value-of select="planReviewSelectedElement/presenterName" />
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell background-color="rgb(65,75,86)">
									<fo:block></fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="1px" background-color="#FFFFFF"
									number-columns-spanned="5">
									<fo:block>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell background-color="#FFFFFF">
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="2"
									padding-before="-1.5px" padding-after="-1.5px" margin-bottom="-1.5px">
									<fo:block text-align="center">
										<fo:external-graphic alignment-adjust="auto"
											content-height="scale-to-fit" content-width="scale-to-fit" height="296px">
											<xsl:attribute name="src">
											<xsl:value-of select="planReviewSelectedElement/coverImage" />
										</xsl:attribute>
										</fo:external-graphic>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="46.75px">
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell >
									<fo:block background-color="#FFFFFF"></fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="frutiger_57_font.size_8.5"
									number-columns-spanned="2" background-color="rgb(65,75,86)"
									text-align="right" margin-top="-2px" padding-before="-2px">
									<fo:block padding-before="20px" background-color="rgb(65,75,86)"
										height="48px" margin-top="-1.5px">
										<fo:block text-align="right" end-indent="41px">
											<xsl:choose>
												<xsl:when
													test="planReviewSelectedElement/logoImageUploaded = 'true'">
													<fo:inline>
														John Hancock Retirement Plan Services and
														<xsl:value-of select="planReviewSelectedElement/companyName" />
														are not affiliated and neither are responsible for the
														liabilities of the other.
													</fo:inline>
												</xsl:when>
											</xsl:choose>
											</fo:block>
											<fo:block text-align="right" end-indent="41px">
											<fo:inline>
												FOR PLAN SPONSOR USE ONLY. NOT FOR USE WITH PLAN PARTICIPANTS.
											</fo:inline>
										</fo:block>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

</xsl:stylesheet>