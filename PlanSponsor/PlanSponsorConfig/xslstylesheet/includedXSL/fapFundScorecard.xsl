<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
 	>
	<xsl:template name="fundsAndPerformanceScorecard">
			<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after">
					<fo:table>
						<fo:table-column column-width="40%"
							xsl:use-attribute-sets="table-borderstyle" />
						<fo:table-column column-width="60%"
							xsl:use-attribute-sets="table-borderstyle" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block  xsl:use-attribute-sets="arial_font.size_9">
										Page
										<fo:page-number />
										of
										<fo:page-number-citation-last
											ref-id="terminator" />
										NOT VALID WITHOUT ALL PAGES
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block xsl:use-attribute-sets="arial_font.size_9" text-align="right">
								For Plan Sponsor use only. Not for use with plan participants.</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<fo:table>
							<fo:table-column column-width="40%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="60%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body>
								<fo:table-cell>
									<fo:block>
										<fo:external-graphic content-height="8cm" content-width="8cm">
											<xsl:attribute name="src">
									<xsl:value-of select="jhLogoPath" />
								</xsl:attribute>
										</fo:external-graphic>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding-before="0.5cm">
									<fo:block text-align="right" font-family="Georgia"
										font-size="16pt">
										JH Signature Fund Scorecard as of 
									</fo:block>
									<fo:block text-align="right" font-family="Georgia"
										font-size="16pt">
										<xsl:apply-templates select="JhScorecardAsOfDate" />
									</fo:block>
									<xsl:if test="currentContract">
										<fo:block text-align="right" font-family="Georgia"
											font-size="16pt">
											<xsl:apply-templates select="currentContractName" />
											(
											<xsl:apply-templates select="currentContract" />
											)
										</fo:block>
									</xsl:if>
								</fo:table-cell>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block space-before="0.1cm">
							<fo:table>
									<fo:table-column column-width="100%"
										xsl:use-attribute-sets="table-borderstyle" />
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell padding-start="0.3cm"
												padding-after="0.3cm">
												<fo:block padding-before="0.3cm"
													xsl:use-attribute-sets="fonts_group2.size_10">
													<xsl:apply-templates select="intro1Text" />
												</fo:block>
												<fo:block padding-before="0.3cm"
													xsl:use-attribute-sets="fonts_group2.size_10">
													<xsl:apply-templates select="intro2Text" />
												</fo:block>
												<fo:block padding-before="0.3cm"
													xsl:use-attribute-sets="fonts_group2.size_10">
													<xsl:apply-templates select="tabHeader" />
												</fo:block>
												<xsl:if test="feeWaiverDisclosure">
													<fo:block padding-before="0.3cm" padding-after="0.3cm"
														xsl:use-attribute-sets="fonts_group2.size_10">
														<xsl:apply-templates select="feeWaiverDisclosure" />
													</fo:block>
												</xsl:if>
												<xsl:if test="fundScorecardDisclosure">
													<fo:block padding-before="0.3cm" padding-after="0.3cm"
														xsl:use-attribute-sets="fonts_group2.size_10">
														<xsl:apply-templates select="fundScorecardDisclosure" />
													</fo:block>
												</xsl:if>
											</fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
					</fo:block>
					
					<!-- Filters - start-->
					<fo:block space-before="0.01cm">
						<fo:block background-color="#deded8" font-family="Verdana"
							font-size="10pt">
							 Filters used:</fo:block>
						<fo:table>
							<fo:table-column column-width="20%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="80%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body font-size="10pt">
								<xsl:if test="filters/currentView">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm"
											padding-start="0.6cm">
											<fo:block font-weight="bold"> View:</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm"
											padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/currentView" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="filters/currentGroupByOption">
									<fo:table-row>
										<fo:table-cell padding-before="0.1cm"
											padding-start="0.6cm">
											<fo:block font-weight="bold"> Group by:</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-before="0.1cm"
											padding-start="0.6cm">
											<fo:block>
												<xsl:value-of select="filters/currentGroupByOption" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<!--  Filters - end-->
					<fo:block space-before="0.2cm">
					   <xsl:variable name="number_of_morningstar_columns">
					   <xsl:choose>
								<xsl:when test="showMorningstarScore = 'true'">
									5
								</xsl:when>
								<xsl:otherwise>
								    0
								</xsl:otherwise>
					   </xsl:choose>
					   </xsl:variable>
					   <xsl:variable name="number_of_fi360_columns">
					   <xsl:choose>
								<xsl:when test="showfi360Score = 'true'">
									5
								</xsl:when>
								<xsl:otherwise>
								    0
								</xsl:otherwise>
					   </xsl:choose>
					   </xsl:variable>
					  <xsl:variable name="number_of_rpag_columns">
					   <xsl:choose>
								<xsl:when test="showRpagScore = 'true'">
									4
								</xsl:when>
								<xsl:otherwise>
								    0
								</xsl:otherwise>
					   </xsl:choose>
					   </xsl:variable>
					   <xsl:variable name="number_of_columns">
							<xsl:value-of select="7 + $number_of_morningstar_columns + $number_of_fi360_columns + $number_of_rpag_columns"/>
						</xsl:variable>
						<xsl:variable name="show_fi360_Score">
						   <xsl:value-of select="showfi360Score"/>
						</xsl:variable>
						<fo:table width="100%">
						
						<xsl:call-template name="fapTableStructures" />
						
						<fo:table-header>
								<xsl:if test="header1">
									<fo:table-row background-color="#767676"
										display-align="center">
										<xsl:for-each select="header1/header">
											<fo:table-cell padding-start="0.1cm"
												xsl:use-attribute-sets="solid.blue.border.sides">
												<xsl:attribute name="number-columns-spanned">
										    <xsl:value-of select="colSpan" />
										 </xsl:attribute>
												<xsl:if test="rowSpan">
													<xsl:attribute name="number-rows-spanned">
										       <xsl:value-of select="rowSpan" />
										    </xsl:attribute>
												</xsl:if>
												<fo:block text-align="center" padding-before="0.1cm"
													padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
													<fo:inline>
														<xsl:value-of select="name" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>
										</xsl:for-each>
									</fo:table-row>
								</xsl:if>
								<xsl:if test="header2">
									<fo:table-row background-color="#767676"
										display-align="center">
										<xsl:for-each select="header2/header">
											<fo:table-cell padding-start="0.1cm"
												xsl:use-attribute-sets="solid.blue.border.sides">
												<xsl:attribute name="number-columns-spanned">
												      <xsl:choose>
												      <xsl:when test="name != ''">
												        <xsl:value-of select="$number_of_columns - 7"/>
												      </xsl:when>
												      <xsl:otherwise>
												         <xsl:value-of select="colSpan" />
												      </xsl:otherwise>
												      </xsl:choose>
										        </xsl:attribute>
												<fo:block text-align="center" padding-before="0.1cm"
													padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
													<fo:inline>
														<xsl:value-of select="name" />
													</fo:inline>
													<xsl:if test="sup">
														<fo:inline vertical-align="super" font-size="8pt">
															<xsl:value-of select="sup" />
														</fo:inline>
													</xsl:if>
												</fo:block>
											</fo:table-cell>
										</xsl:for-each>
									</fo:table-row>
								</xsl:if>
								
								<xsl:call-template name="fundScoreCardHeader"/>
								
								<fo:table-row background-color="#deded8"
									display-align="center">
									<xsl:for-each select="header3/header">
										<fo:table-cell 
											xsl:use-attribute-sets="solid.blue.border">
											   <xsl:if test="align = 'right'">
													<xsl:attribute name="padding-end">
										            	0.1cm
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="align = 'left'">
													<xsl:attribute name="padding-start">
										        		0.1cm
													</xsl:attribute>
												</xsl:if>
											<xsl:if test="colSpan">
											  <xsl:attribute name="number-columns-spanned">
										           <xsl:value-of select="colSpan" />
										       </xsl:attribute>
											</xsl:if>	
											<fo:block padding-before="0.1cm" padding-after="0.1cm"
												xsl:use-attribute-sets="verdana_font.size_8">
												<xsl:attribute name="text-align">
										        	<xsl:value-of select="align" />
										        </xsl:attribute>
												<fo:inline>
													<xsl:value-of select="name" />
												</fo:inline>
												<xsl:if test="sup">
													<fo:inline vertical-align="super" font-size="7pt">
														<xsl:value-of select="sup" />
													</fo:inline>
												</xsl:if>
											</fo:block>
										</fo:table-cell>
									</xsl:for-each>
								</fo:table-row>
								<fo:table-row keep-with-previous="always">
									<fo:table-cell number-columns-spanned="{$number_of_columns}">
										<fo:block border-before-width="0.1cm"
											border-before-style="solid" border-before-color="#cac8c4" />
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-body>
								<xsl:for-each select="reportDetails/reportDetail">
								    <fo:table-row>
								    <fo:table-cell number-columns-spanned="{$number_of_columns}">
								    <fo:block>
								    <fo:table width="100%" table-layout="fixed">
								    <xsl:call-template name="fapTableStructures"/>
								    <fo:table-header>
									<fo:table-row height="0.1cm">
										<fo:table-cell number-columns-spanned="{$number_of_columns}">
											<fo:block />
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row keep-with-next="always" >
										<fo:table-cell number-columns-spanned="{$number_of_columns}">
											<fo:block border-before-width="0.1cm"
												border-before-style="solid" border-before-color="#febe10" />
											<!--
												orange color
											-->
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row background-color="#deded8"
										keep-with-previous="always" keep-with-next="always">
										<fo:table-cell padding-start="0.2cm"
											number-columns-spanned="{$number_of_columns}">
											<fo:block font-family="Verdana" font-size="12pt">
												<xsl:value-of select="fundCategory" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									</fo:table-header>
									<fo:table-body>
									<xsl:for-each select="fundDetail">
										<fo:table-row display-align="center"  keep-together.within-page="always">
											<xsl:call-template name="rowColorTemplate" />
											<xsl:if test="./includeFeeWaiverColumn">
												 <fo:table-cell xsl:use-attribute-sets="solid.blue.border.left" >
												         <xsl:if test="$showMorningstarScoreVar = 'true' or  $showfi360ScoreVar = 'true' ">
										                     <xsl:attribute name="number-rows-spanned">
										                        <xsl:value-of select="showFeeWaiverSymbolRowSpan" />
										                      </xsl:attribute>
										                </xsl:if>	
														<fo:block text-align="end" padding-before="0.2cm">
														     <xsl:if test="./showFeeWaiverSymbol">
														        &#8226;
														     </xsl:if>
													 </fo:block>
												 </fo:table-cell>		
											</xsl:if>	

											<xsl:for-each select="value">
												<xsl:choose>
													<xsl:when test="./sup">
														<fo:table-cell padding-start="0.03cm">
															<xsl:call-template name="rowBorderColorTemplate" />
															<xsl:call-template name="fundScoreCardRowSpan" />
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<fo:block padding-before="0.1cm">
																	<fo:inline>
																		<xsl:value-of select="name" />
																	</fo:inline>
																	<fo:inline vertical-align="super" font-size="7pt">
																		<xsl:value-of select="sup" />
																	</fo:inline>
																</fo:block>
															</fo:block>
														</fo:table-cell>
													</xsl:when>
													<xsl:otherwise>
														<fo:table-cell>
															<xsl:if test="Fi360Style">
															    <xsl:call-template name="fi360ScoreColor" />
															</xsl:if>    
															<xsl:call-template name="rowBorderColorTemplate" />
															<xsl:call-template name="fundScoreCardRowSpan" />
															<xsl:attribute name="text-align">
										                                             <xsl:value-of
																select="align" />
										                                        </xsl:attribute>
															<xsl:if test="align = 'right'">
																<xsl:attribute name="padding-end">
										                                                     0.03cm
										                                            </xsl:attribute>
															</xsl:if>
															<xsl:if test="align = 'left'">
																<xsl:attribute name="padding-start">
										                                                     0.03cm
										                                            </xsl:attribute>
															</xsl:if>
															<fo:block xsl:use-attribute-sets="verdana_font.size_8">
																<fo:block padding-before="0.1cm">
																	<xsl:if test="bold or boldScore">
																		<xsl:attribute name="font-weight">bold</xsl:attribute>
																	</xsl:if>
																	<fo:inline>
																		<xsl:value-of select="name" />
																	</fo:inline>
																</fo:block>
															</fo:block>
														</fo:table-cell>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
										</fo:table-row>
										<xsl:call-template name="fundScoreCardSecondaryRow" />
									</xsl:for-each>
									</fo:table-body>
									</fo:table>
									</fo:block>
									</fo:table-cell>
									</fo:table-row>
								</xsl:for-each>
							</fo:table-body>
						</fo:table>
					</fo:block>
							
				   <!-- Showing the page footer-->
					<fo:block space-before="0.7cm">
						
						<fo:block>
							<fo:table>
								<fo:table-column column-width="100%"
									xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-body>
									<xsl:if test="footer">
										<fo:table-row>
											<fo:table-cell padding-start="1cm"
												padding-before="0.1cm">
												<fo:block xsl:use-attribute-sets="arial_font.size_9">
													<xsl:apply-templates select="footer" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="footnotes">
										<fo:table-row>
											<fo:table-cell padding-start="1cm"
												padding-before="0.1cm">
												<fo:block xsl:use-attribute-sets="arial_font.size_9">
													<xsl:apply-templates select="footnotes" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="morningstarTabFootnotes">
										<xsl:for-each select="morningstarTabFootnotes">
											<fo:table-row>
												<fo:table-cell padding-start="1cm" padding-before="0.1cm">
													<fo:block xsl:use-attribute-sets="arial_font.size_9">
															    <xsl:apply-templates select="morningstarTabFootnotes" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:for-each>
									</xsl:if>
									<xsl:if test="tabFootnotes">
										<xsl:for-each select="tabFootnotes">
											<fo:table-row>
												<fo:table-cell padding-start="1cm"
													padding-before="0.1cm">
													<fo:block xsl:use-attribute-sets="arial_font.size_9">
														<fo:inline vertical-align="super" font-size="8pt">
															<xsl:apply-templates select="sup" />
														</fo:inline>
														<fo:inline>
														    <xsl:apply-templates select="value" />
														</fo:inline>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:for-each>
									</xsl:if>
									<xsl:if test="disclaimer">
										<fo:table-row>
											<fo:table-cell padding-start="1cm"
												padding-before="0.1cm">
												<fo:block xsl:use-attribute-sets="arial_font.size_9">
													<xsl:apply-templates select="disclaimer" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block>
					<fo:block space-before="0.5cm" xsl:use-attribute-sets="arial_font.size_9"
						keep-together.within-column="always">
						<xsl:apply-templates select="globalDisclosure" />
					</fo:block>
					<fo:block id="terminator" />
				</fo:flow>
			</fo:page-sequence>
	</xsl:template>
	
	<xsl:template name="fi360ScoreColor">
	<xsl:attribute name="background-color">
            <xsl:choose>
                <xsl:when test="Fi360Style/text() = 'fi360firstQuartile'">#00FF00</xsl:when>
                <xsl:when test="Fi360Style/text() = 'fi360secondQuartile'">#C8FF96</xsl:when>
                <xsl:when test="Fi360Style/text() = 'fi360thirdQuartile'">#FFFF00</xsl:when>
                <xsl:when test="Fi360Style/text() = 'fi360fourthQuartile'">#FF0000</xsl:when>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template name="rowColorTemplate">
		<xsl:if test="./rowColor">
			<xsl:attribute name="background-color"><xsl:value-of select="./rowColor" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="./rowFontColor">
			<xsl:attribute name="color"><xsl:value-of select="./rowFontColor" /></xsl:attribute>
		</xsl:if>
    </xsl:template>

	<xsl:template name="rowBorderColorTemplate">
		<xsl:choose>
			<xsl:when test="../includeFeeWaiverColumn">
				<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
				<xsl:attribute name="border-after-style">solid</xsl:attribute>
				<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
				<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
				<xsl:attribute name="border-before-style">solid</xsl:attribute>
				<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
				<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
				<xsl:attribute name="border-end-style">solid</xsl:attribute>
				<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
				<xsl:attribute name="border-start-style">solid</xsl:attribute>
				<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
				<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
				<xsl:attribute name="border-after-style">solid</xsl:attribute>
				<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
    
	<xsl:template name="fundScoreCardRowSpan">
	   <xsl:if test="$showMorningstarScoreVar = 'true' or  $showfi360ScoreVar = 'true' ">
		<xsl:if test="../rowSpan and not(secondaryValue)">
			<xsl:attribute name="number-rows-spanned">
				<xsl:value-of select="../rowSpan" />
			</xsl:attribute>
		</xsl:if>
		</xsl:if>
	</xsl:template>	
	
   <xsl:template name="fundScoreCardSecondaryRow">
	<xsl:if test="$showMorningstarScoreVar = 'true' or $showfi360ScoreVar = 'true' ">
		<fo:table-row keep-with-previous="always">
			<xsl:if test="./rowColor">
				<xsl:attribute name="background-color"><xsl:value-of select="./rowColor" /></xsl:attribute>
			</xsl:if>
			<xsl:if test="./rowFontColor">
				<xsl:attribute name="color"><xsl:value-of select="./rowFontColor" /></xsl:attribute>
			</xsl:if>
			<xsl:for-each select="value">
				<xsl:if test="secondaryValue">
					<fo:table-cell>
						<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
						<xsl:attribute name="border-after-style">solid</xsl:attribute>
						<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
						<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
						<xsl:attribute name="border-before-style">solid</xsl:attribute>
						<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
						<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
						<xsl:attribute name="border-end-style">solid</xsl:attribute>
						<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
						<xsl:attribute name="text-align">
			              <xsl:value-of select="align" />
			            </xsl:attribute>
						<xsl:if test="align = 'right'">
							<xsl:attribute name="padding-end">
			                   0.03cm
			                </xsl:attribute>
						</xsl:if>
						<xsl:if test="align = 'left'">
							<xsl:attribute name="padding-start">
			                   0.03cm
			                </xsl:attribute>
						</xsl:if>
						<fo:block xsl:use-attribute-sets="verdana_font.size_8">
							<fo:block padding-before="0.1cm">
								<xsl:if test="boldScore">
									<xsl:attribute name="font-weight">bold</xsl:attribute>
								</xsl:if>
								<fo:inline>
									<xsl:value-of select="secondaryValue" />
								</fo:inline>
							</fo:block>
						</fo:block>
					</fo:table-cell>
				</xsl:if>
			</xsl:for-each>
		</fo:table-row>
	</xsl:if>
   </xsl:template>
    
    <xsl:template name="fundScoreCardHeader">
		<fo:table-row background-color="#767676" display-align="center">
			<fo:table-cell padding-start="0.1cm" background-color="white"
				xsl:use-attribute-sets="solid.blue.border.sides">
				<xsl:attribute name="number-columns-spanned">
											           7
					</xsl:attribute>
				<xsl:attribute name="number-rows-spanned">
											           2
					</xsl:attribute>
				<fo:block text-align="left"></fo:block>
				<xsl:if test="$showfi360ScoreVar = 'true'">	
				<fo:block text-align="left">Legend for Fi360</fo:block>
				<fo:block text-align="center" padding-before="0.1cm"
					padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
					<fo:table table-layout="fixed" width="300px">
						<fo:table-column column-width="4%" />
						<fo:table-column column-width="45%" />
						<fo:table-column column-width="2%" />
						<fo:table-column column-width="4%" />
						<fo:table-column column-width="45%" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="5">
									<fo:block text-align="left"></fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="5">
									<fo:block padding-before="2px" padding-after="2px"
										text-align="left" xsl:use-attribute-sets="verdana_font.size_8">
										<fo:inline font-weight="bold">NS = </fo:inline>
										<fo:inline> Not Scored
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block padding-before="2px">
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="10px" />
											<fo:table-body>
												<fo:table-row height="10px">
													<xsl:attribute name="background-color">
																#00FF00
													</xsl:attribute>
													<fo:table-cell>
														<fo:block></fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block padding-before="2px" text-align="left"
										xsl:use-attribute-sets="verdana_font.size_8">
										&#160; 0-25 - First Quartile
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block padding-before="2px">
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="10px" />
											<fo:table-body>
												<fo:table-row height="10px">
													<xsl:attribute name="background-color">
																#FFFF00
													</xsl:attribute>
													<fo:table-cell>
														<fo:block></fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block padding-before="2px" text-align="left"
										xsl:use-attribute-sets="verdana_font.size_8">
										&#160;51-75 - Third Quartile
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block padding-before="2px">
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="10px" />
											<fo:table-body>
												<fo:table-row height="10px">
													<xsl:attribute name="background-color">
															#C8FF96
													</xsl:attribute>
													<fo:table-cell>
														<fo:block></fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block padding-before="2px" text-align="left"
										xsl:use-attribute-sets="verdana_font.size_8">
										&#160;26-50 - Second Quartile
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block padding-before="2px">
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="10px" />
											<fo:table-body>
												<fo:table-row height="10px">
													<xsl:attribute name="background-color">
															#FF0000
													</xsl:attribute>
													<fo:table-cell>
														<fo:block></fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block padding-before="2px" text-align="left"
										xsl:use-attribute-sets="verdana_font.size_8">
										&#160;76-100 - Fourth Quartile
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:block>
				</xsl:if>
			</fo:table-cell>
			<xsl:if test="$showMorningstarScoreVar = 'true'">
			<fo:table-cell padding-start="0.1cm" background-color="white"
				xsl:use-attribute-sets="solid2.blue.border.sides">
				<xsl:attribute name="number-columns-spanned">
											           5
											         </xsl:attribute>
				<fo:block text-align="center" xsl:use-attribute-sets="verdana_font.size_8">
					<fo:external-graphic content-height="90px"
						content-width="150px">
						<xsl:attribute name="src">
						        <xsl:value-of select="concat($imagePath,'MorningStar.jpg')"/>
							</xsl:attribute>
					</fo:external-graphic>
				</fo:block>
			</fo:table-cell>
			</xsl:if>
			<xsl:if test="$showfi360ScoreVar = 'true'">
			<fo:table-cell padding-start="0.1cm" background-color="white"
				xsl:use-attribute-sets="solid2.blue.border.sides">
				<xsl:attribute name="number-columns-spanned">
											           5
											         </xsl:attribute>
				<fo:block text-align="center"  xsl:use-attribute-sets="verdana_font.size_8">
					<fo:external-graphic content-height="70px"
						content-width="100px">
						<xsl:attribute name="src">
						        <xsl:value-of select="concat($imagePath,'fi360.png')"/>
							</xsl:attribute>
					</fo:external-graphic>
				</fo:block>
			</fo:table-cell>
			</xsl:if>
			<xsl:if test="$showRpagScoreVar = 'true'">
			<fo:table-cell padding-start="0.1cm" background-color="white"
				xsl:use-attribute-sets="solid2.blue.border.sides">
				<xsl:attribute name="number-columns-spanned">
											           4
											         </xsl:attribute>
				<fo:block text-align="center"  xsl:use-attribute-sets="verdana_font.size_8">
					<fo:external-graphic content-height="70px"
						content-width="70px">
						<xsl:attribute name="src">
						        <xsl:value-of select="concat($imagePath,'RPAG.jpg')"/>
							</xsl:attribute>
					</fo:external-graphic>
				</fo:block>
			</fo:table-cell>
			</xsl:if>
		</fo:table-row>
		<fo:table-row background-color="#767676" display-align="center">
			<xsl:if test="$showMorningstarScoreVar = 'true'">
			<fo:table-cell padding-start="0.1cm"
				xsl:use-attribute-sets="solid.blue.border.sides">
				<xsl:attribute name="number-columns-spanned">
						4
					</xsl:attribute>
				<fo:block text-align="center" padding-before="0.1cm"
					padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
					Total Return (% Rank in category)
					<fo:inline vertical-align="super" font-size="7pt">*55</fo:inline>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding-start="0.1cm"
				xsl:use-attribute-sets="solid.blue.border.sides">
				<xsl:attribute name="number-rows-spanned">
						 2
					</xsl:attribute>
				<fo:block text-align="center" padding-before="0.1cm"
					padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
					Overall Rating* (# of peers)
				</fo:block>
			</fo:table-cell>
			</xsl:if>
			<xsl:if test="$showfi360ScoreVar = 'true'">
			<fo:table-cell padding-start="0.1cm"
				xsl:use-attribute-sets="solid.blue.border.sides">
				<xsl:attribute name="number-columns-spanned">
						5
					</xsl:attribute>
				<fo:block text-align="center" padding-before="0.1cm"
					padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
					Fi360 Fiduciary Score ® (# of peers)
				</fo:block>
			</fo:table-cell>
			</xsl:if>
			<xsl:if test="$showRpagScoreVar = 'true'">
			<fo:table-cell padding-start="0.1cm"
				xsl:use-attribute-sets="solid.blue.border.sides">
				<xsl:attribute name="number-columns-spanned">
						4
					</xsl:attribute>
				<fo:block text-align="center" padding-before="0.1cm"
					padding-after="0.1cm" xsl:use-attribute-sets="verdana_font.size_8">
					RPAG Score (out of 10)
				</fo:block>
			</fo:table-cell>
			</xsl:if>
		</fo:table-row>
    </xsl:template> 
	
	<xsl:template name="fapTableStructures">
	  <xsl:choose>
	  <xsl:when test="$showMorningstarScoreVar = 'true' and $showfi360ScoreVar = 'true' and $showRpagScoreVar = 'true'">
		    <fo:table-column column-width="1%"
				xsl:use-attribute-sets="table-borderstyle" />
		    <fo:table-column column-width="13%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="7%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="3.5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="3.5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8.4%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="4%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="4%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="4%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="4%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="4%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="3.4%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="3.4%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="3.4%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="3.4%"
				xsl:use-attribute-sets="table-borderstyle" />
	   </xsl:when>
	   <xsl:when test="$showMorningstarScoreVar = 'true' and $showfi360ScoreVar = 'true' and $showRpagScoreVar = 'false'">
	         <fo:table-column column-width="1%"
				xsl:use-attribute-sets="table-borderstyle" />
	         <fo:table-column column-width="15%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="6%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="4.5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="4.5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="11%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
	   </xsl:when>
	   <xsl:when test="($showMorningstarScoreVar = 'true' and $showfi360ScoreVar = 'false' and $showRpagScoreVar = 'true')
	                or ($showMorningstarScoreVar = 'false' and $showfi360ScoreVar = 'true' and $showRpagScoreVar = 'true')">
	        <fo:table-column column-width="1%"
				xsl:use-attribute-sets="table-borderstyle" />
	        <fo:table-column column-width="17%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="9%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="7%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="11%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
	   </xsl:when>
	   <xsl:when test="($showMorningstarScoreVar = 'true' and $showfi360ScoreVar = 'false' and $showRpagScoreVar = 'false')
	                or ($showMorningstarScoreVar = 'false' and $showfi360ScoreVar = 'true' and $showRpagScoreVar = 'false')">
	         <fo:table-column column-width="1%"
				xsl:use-attribute-sets="table-borderstyle" />
	         <fo:table-column column-width="21%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="9%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="7%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="5%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="6%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="11%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
	   </xsl:when>
	   <xsl:otherwise>
	        <fo:table-column column-width="1%"
				xsl:use-attribute-sets="table-borderstyle" />
	        <fo:table-column column-width="25%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="10%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="6%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="7%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="11%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
			<fo:table-column column-width="8%"
				xsl:use-attribute-sets="table-borderstyle" />
	   </xsl:otherwise>
	  </xsl:choose>
    </xsl:template>
	
</xsl:stylesheet>