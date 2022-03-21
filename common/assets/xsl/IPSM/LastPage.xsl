<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="LastPage">
	<fo:page-sequence master-reference="LastPageLayout" force-page-count="no-force">
	<fo:flow flow-name="xsl-region-body">
				<fo:block start-indent="17px" padding-before="272px">
					<fo:external-graphic content-height="38px" content-width="116px">
						<xsl:attribute name="src">
							<xsl:value-of select="concat($imagePath,'JH_SIG_white-on-green.jpg')"/>
						</xsl:attribute>
					</fo:external-graphic>
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content_font1" start-indent="17px" padding-before="23px" end-indent="345px">
						To obtain group annuity investment option Fund sheets and prospectuses for each sub-account’s underlying investment vehicle call 1-800-574-5557 for a plan sponsor with a plan domiciled in New York, for a plan sponsor with a plan domiciled in all other states call 1-800-333-0963. These documents contain complete details on investment objectives, risks, fees, charges and expenses as well as other information about the underlying investment vehicle, which should be carefully considered. Please read these documents carefully prior to investing.
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content_font2" start-indent="17px" padding-before="11px" end-indent="345px">
						JH Signature<fo:inline vertical-align="super" font-size="5.5pt" >TM</fo:inline> is a trademark for the service package for the Group Annuity Contracts and recordkeeping agreements issued by John Hancock Life Insurance Company (U.S.A.).			
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content_font2" start-indent="17px" padding-before="4px" end-indent="345px">
						Group annuity contracts and recordkeeping agreements are issued by: John Hancock Life Insurance Company (U.S.A.) (“John Hancock USA”), Boston, MA (not licensed in New York) and John Hancock Life Insurance Company of New York (“John Hancock NY”), Valhalla, NY. Product features and availability may differ by state.  John Hancock USA and John Hancock NY each make available a platform of investment alternatives to sponsors or administrators of retirement plans without regard to the individualized needs of any plan.  Unless otherwise specifically stated in writing, John Hancock USA and John Hancock NY do not, and are not undertaking to, provide impartial investment advice or give advice in a fiduciary capacity.
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content_font2" start-indent="17px" padding-before="4px" end-indent="345px">
						NOT FDIC INSURED | MAY LOSE VALUE | NOT BANK GUARANTEED 
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content_font2" start-indent="17px" padding-before="4px" end-indent="345px">
						© 2017 John Hancock. All rights reserved.
				</fo:block>
				<fo:table table-layout="fixed" width="100%"  >
					<fo:table-column column-width="85px" />
					<fo:table-column column-width="50px" />
					<fo:table-column column-width="330px" />
					<fo:table-column column-width="127px" />
					<fo:table-body>
					<fo:table-row height="70px">
							<fo:table-cell number-columns-spanned="4">
								<fo:block>									
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell >
								<xsl:choose>
									<xsl:when test="annual_review_report/isInterim = 'true'">
										<fo:block xsl:use-attribute-sets="static_content_font1" start-indent="17px" font-size="8pt">
											PS 19986-GE
										</fo:block>
									</xsl:when>
									<xsl:otherwise>
										<fo:block xsl:use-attribute-sets="static_content_font1" start-indent="17px" font-size="8pt">
											PS 19374-GE
										</fo:block>		
									</xsl:otherwise>
								</xsl:choose>
							</fo:table-cell>
							<fo:table-cell>
								<xsl:choose>
									<xsl:when test="annual_review_report/isInterim = 'true'">
										<fo:block xsl:use-attribute-sets="static_content_font2">
											03/17-31283
										</fo:block>
									</xsl:when>
									<xsl:otherwise>
										<fo:block xsl:use-attribute-sets="static_content_font2">
											03/17-31282
										</fo:block>
									</xsl:otherwise>
								</xsl:choose>
							</fo:table-cell>
							<fo:table-cell padding-left="40px">
								<fo:block xsl:use-attribute-sets="static_content_font2">
									FOR PLAN SPONSOR USE ONLY. NOT FOR USE WITH PLAN PARTICIPANTS.
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-left="40px">
								<xsl:choose>
									<xsl:when test="annual_review_report/isInterim = 'true'">
										<fo:block xsl:use-attribute-sets="static_content_font2" end-indent="17px">
											GA102616325520
										</fo:block>
									</xsl:when>
									<xsl:otherwise>
										<fo:block xsl:use-attribute-sets="static_content_font2" end-indent="17px">
											GA102616325519
										</fo:block>
									</xsl:otherwise>
								</xsl:choose>
								
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				<fo:block id="terminator" />
				</fo:flow>
				</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>