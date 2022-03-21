<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="BackCoverTemplate">

			<fo:page-sequence master-reference="BackCoverPageLayout">
				<!-- Body -->
				<fo:flow flow-name="xsl-region-body">
					<fo:block padding-before="138px">
					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="36px"/>
						<fo:table-column column-width="280px"/>
						<fo:table-body>
							<fo:table-row height="103px">
								<fo:table-cell number-columns-spanned="2">
									<fo:block/>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block/>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block>
										<fo:external-graphic content-height="46px" content-width="134px">
											<xsl:attribute name="src">
												<xsl:value-of select="concat($imagePath,'JH_SIG-logo_white_pc.jpg')"/>
											</xsl:attribute>
										</fo:external-graphic>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="17px">
								<fo:table-cell number-columns-spanned="2">
									<fo:block/>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block/>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block xsl:use-attribute-sets="assethouse_column_row_name" text-align="left">Please call 800-333-0963 to obtain Fund Sheets for the group annuity investment option subaccounts and to obtain prospectuses for the subaccounts' underlying funds, that are available on request. The prospectuses for the subaccounts’ underlying funds contain complete details on investment objectives, risks, fees, charges and expenses as well as other information about the underlying funds which should be carefully considered before investing.</fo:block>
								</fo:table-cell>
							</fo:table-row> 
							<!--<fo:table-row>
								<fo:table-cell padding-before="4px">
									<fo:block/>
								</fo:table-cell>
								<fo:table-cell padding-before="4px">
								 <fo:block line-height="1" xsl:use-attribute-sets="FSW_disclaimer_style" color="#FFFFFF">A Lifestyle or Lifecycle Portfolio ("Fund") is a "fund of funds" which invests in a number of
underlying funds. The Fund's ability to achieve its investment objective will depend largely on
the ability of the subadviser to select the appropriate mix of underlying funds and on the
underlying funds&apos; ability to meet their investment objectives. There can be no assurance that
either a Fund or the underlying funds will achieve their investment objectives. A Fund is subject
to the same risks as the underlying funds in which it invests. Each Fund invests in underlying
funds which invest in fixed-income securities (including in some cases high yield securities) and
equity securities, including foreign securities and engage in Hedging and Other Strategic
Transactions. To the extent the Fund invests in these securities directly or engages in Hedging
and Other Strategic Transactions, the Fund will be subject to the same risks. As a Fund's asset
mix becomes more conservative, the fund becomes more susceptible to risks associated with
fixed-income securities. For a more complete description of these risks, please review the
underlying fund's prospectus, which is available upon request.</fo:block></fo:table-cell>
							</fo:table-row> -->
							<fo:table-row>
								<fo:table-cell padding-before="4px">
									<fo:block/>
								</fo:table-cell>
								<fo:table-cell padding-before="4px">
								<fo:block line-height="1" xsl:use-attribute-sets="FSW_disclaimer_style" color="#FFFFFF">Group annuity contracts and recordkeeping agreements are issued by John Hancock Life Insurance Company (U.S.A.) (John Hancock USA), Boston, MA (not licensed in New York), and John Hancock Life Insurance Company of New York (John Hancock New York), Valhalla, NY. Product features and availability may differ by state. John Hancock USA and John Hancock New York each make available a platform of investment alternatives to sponsors or administrators of retirement plans without regard to the individualized needs of any plan. Unless otherwise specifically stated in writing, John Hancock USA and John Hancock New York do not, and are not undertaking to, provide impartial investment advice or give advice in a fiduciary capacity.
								</fo:block></fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell padding-before="4px">
									<fo:block/>
								</fo:table-cell>
								<fo:table-cell padding-before="4px">
								<fo:block line-height="1" xsl:use-attribute-sets="FSW_disclaimer_style" color="#FFFFFF">NOT FDIC INSURED. MAY LOSE VALUE. NOT BANK GUARANTEED. </fo:block></fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell padding-before="4px">
									<fo:block/>
								</fo:table-cell>
								<fo:table-cell padding-before="4px">
								<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" color="#FFFFFF">
								<fo:inline font-size="9pt">&#169; </fo:inline>2020 John Hancock. All rights reserved.</fo:block></fo:table-cell>
							</fo:table-row>
							<fo:table-row height="43px">
								<fo:table-cell number-columns-spanned="2">
									<fo:block/>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block/>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block xsl:use-attribute-sets="assethouse_column_row_name" text-align="left" font-size="7pt">PS 16040-GE 04/20-42006</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>	
					</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		<!--</fo:root>-->
	</xsl:template>
</xsl:stylesheet>