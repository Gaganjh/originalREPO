<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

 <xsl:template name="ImportantNotesTemplate">
 
   <fo:page-sequence master-reference="ImportantNotesLayout">

		<!-- Header -->
		<fo:static-content flow-name="xsl-region-before" >
			<fo:block>
				<fo:block-container xsl:use-attribute-sets="header_block_cell1">
					<fo:block-container  xsl:use-attribute-sets="header_block_cell2">
						<fo:block>
						</fo:block>
					</fo:block-container>
				</fo:block-container>
				<fo:retrieve-marker retrieve-class-name="headerCell3" retrieve-position="first-including-carryover" retrieve-boundary="page-sequence"/>
			</fo:block>						
		</fo:static-content>
		
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

		<fo:flow flow-name="xsl-region-body">
				
		  	<!--  Header for First Page -->
			<fo:block>
				<fo:marker marker-class-name="headerCell3">
					<fo:block-container  xsl:use-attribute-sets="header_block_cell3">
						<fo:block id="importantNotesId" padding-after="12px" start-indent="18px">
							Important notes
						</fo:block>
					</fo:block-container>
				</fo:marker>
			</fo:block>
			
			<!--  Header for remaining Pages -->
			<fo:block>
				<fo:marker marker-class-name="headerCell3">
					<fo:block-container  xsl:use-attribute-sets="header_block_cell3_continued">
						<fo:block padding-after="4px" start-indent="18px">
							Important notes
						</fo:block>
					</fo:block-container>
				</fo:marker>
			</fo:block>
			
			<fo:block span="all"/><fo:block space-before="25px" span="all"/>
			
			<xsl:for-each select="endNotes/generalFootnotes/footnote">

				<fo:list-block xsl:use-attribute-sets="footnote_style">
					<fo:list-item>
						<fo:list-item-label end-indent="label-end()">
							<fo:block>
								<xsl:value-of select="symbol" />
							</fo:block>
						</fo:list-item-label>

						<fo:list-item-body start-indent="body-start()">
							<fo:block>
								<xsl:apply-templates select="cmaContent" />
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
				<fo:block space-after="3px"/>
				
			</xsl:for-each>
			
			<xsl:for-each select="endNotes/fundFootnotes/footnote">

				<fo:list-block xsl:use-attribute-sets="footnote_style">
					<fo:list-item>
						<fo:list-item-label end-indent="label-end()">
							<fo:block>
								<xsl:value-of select="symbol" />
							</fo:block>
						</fo:list-item-label>

						<fo:list-item-body start-indent="body-start()">
							<fo:block>
								<xsl:apply-templates select="cmaContent" />
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
				<fo:block space-after="3px"/>
			</xsl:for-each>
			
			<fo:block>&#160;
			</fo:block>
			
			<fo:block xsl:use-attribute-sets="footnote_style">
				<xsl:apply-templates select="endNotes/riskDisclosuresForSite/cmaContent"/>
			</fo:block>

			<fo:block xsl:use-attribute-sets="footnote_style">
				<xsl:apply-templates select="endNotes/generalFootnotesForSite/cmaContent"/>
			</fo:block>
	
			<fo:block id="terminator" />
		</fo:flow>
		
   </fo:page-sequence>
 </xsl:template>
</xsl:stylesheet>


