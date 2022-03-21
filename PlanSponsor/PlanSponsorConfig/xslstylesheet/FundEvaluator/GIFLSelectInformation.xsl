<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="GIFLSelectInformationTemplate">

		<fo:page-sequence master-reference="GIFLSelectInformationLayout">
			
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before" >
				<fo:block-container xsl:use-attribute-sets="header_block_cell1">
					<fo:block-container  xsl:use-attribute-sets="header_block_cell2">
						<fo:block>
						</fo:block>
					</fo:block-container>
				</fo:block-container>
				
				<fo:block-container  xsl:use-attribute-sets="header_block_cell3">
					<fo:block id="GIFLSelectInformationId" padding-after="6px" start-indent="18px">
						<fo:inline>Guaranteed Income for Life Select</fo:inline>
					</fo:block>
				</fo:block-container>
			</fo:static-content>
			
			<!-- Footer -->
			<fo:static-content flow-name="xsl-region-after" >
				<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="126px"/>
					<fo:table-body>
						<fo:table-row height = "14px">
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="page_count">
									PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminator"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>

			<!-- Body -->
			<fo:flow flow-name="xsl-region-body">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="128px"/>
					<fo:table-column column-width="290px"/>
					<fo:table-column column-width="13px"/>
					<fo:table-column column-width="273px"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell text-align="right" padding-right="1px" padding-before="1.2px">
								<fo:block>
									<fo:external-graphic content-height="115px" content-width="5px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Verticle-rule.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell padding-left="14px">
								<!-- Word Group -->
								<fo:block xsl:use-attribute-sets="word_group_style" line-height="1.5" space-after="10px">
									American families today share a number of very real concerns as they save for retirement. There are three questions that are universal:
								</fo:block>
								<fo:block xsl:use-attribute-sets="word_group_style" font-weight="Light" line-height="1.5">
									<fo:list-block space-after="10px">
										<fo:list-item space-after="3px">
											<fo:list-item-label start-indent="2px">
												<fo:block>
												   &#8226;
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="12px">
												<fo:block>
													Will I have enough to retire?
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
										<fo:list-item space-after="3px">
											<fo:list-item-label start-indent="2px">
												<fo:block>
												   &#8226;
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="12px">
												<fo:block>
													Will my savings last through retirement?
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
										<fo:list-item space-after="3px">
											<fo:list-item-label start-indent="2px">
												<fo:block>
												   <xsl:text>&#8226;</xsl:text>
												</fo:block>
											</fo:list-item-label>
											<fo:list-item-body start-indent="12px">
												<fo:block>
													What if something goes wrong?
												</fo:block>
											</fo:list-item-body>
										</fo:list-item>
									</fo:list-block>
									Guaranteed Income for Life Select is a unique feature for 401(k) plans that provides participants with 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="word_group_style" line-height="1.5" space-after="15px">
									predictable, certain retirement income, while protecting the Benefit Base from market events.
								</fo:block>
								<fo:block xsl:use-attribute-sets="word_group_style" line-height="1.5">
									<xsl:choose>
										<xsl:when test="$contractNumber = ''">
											Guaranteed Income for Life Select is available to your plan. Speak to your Financial Representative to see if this feature might be suitable for your plan. Refer to the rider and our product guide for complete details. The Select Funds have been included as part of this report.
										</xsl:when>
										<xsl:otherwise>
											You have already elected Guaranteed Income for Life Select as part of your plan.  
										</xsl:otherwise>
									</xsl:choose>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row height="12px">
							<fo:table-cell number-columns-spanned="4">
								<fo:block>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row>
							<fo:table-cell number-columns-spanned="4">
								<fo:block>
									<fo:external-graphic content-height="230px" content-width="702px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Pg18-GIFL-Select-1404x435-webmax.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row height="10px">
							<fo:table-cell number-columns-spanned="4">
								<fo:block>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row>
							<fo:table-cell text-align="right">
								<fo:block>
									<fo:external-graphic content-height="68px" content-width="5px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Pg6-Vertical-rule-bottom.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell padding-before="3px" padding-after="-3px" padding-left="14px" number-columns-spanned="3" display-align="after">
								<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" space-after="4px" line-height="1">
									Although the Guaranteed Income for Life Select feature provides a guaranteed income base as well as guaranteed minimum withdrawal benefits, the investment options available under the feature are variable investments and may lose value.  Guarantees of withdrawals provided under the feature are supported by the issuer's general account and are subject to the claims paying ability of the issuer, which does not relate to the investment performance or safety of the investment options to which the feature applies.  Before the Lifetime Income Date, withdrawals (including loans and transfers out of this feature) will reduce the benefit base in the same proportion that the withdrawals reduces the market value of investments in this feature, or by the amount of the withdrawal, if greater. However, after the Lifetime Income Date, this reduction will only apply when withdrawals during any year beginning after such date (or anniversary thereof) exceeds the Lifetime Income Amount. The guarantees provided are contingent on the Plan trustee’s election to continue maintaining its Group Annuity Contract with John Hancock or the election of a participant to rollover his or her benefits to a recipient rollover vehicle available from John Hancock upon a termination event.
								</fo:block>									
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>