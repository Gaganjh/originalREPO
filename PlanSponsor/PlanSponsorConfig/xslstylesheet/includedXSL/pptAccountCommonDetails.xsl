<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:attribute-set name="table-borderstyle">
<!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
</xsl:attribute-set>

<xsl:attribute-set name="fonts_group2.size_12">
	<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="arial_font.size_11">
	<xsl:attribute name="font-family">Arial</xsl:attribute>
	<xsl:attribute name="font-size">11pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="verdana_font.size_10">
	<xsl:attribute name="font-family">Verdana</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="colorLC">
	<xsl:attribute name="background-color"><xsl:value-of select="reportSummaryDetail/colorLC" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorAG">
	<xsl:attribute name="background-color"><xsl:value-of select="reportSummaryDetail/colorAG" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorGR">
	<xsl:attribute name="background-color"><xsl:value-of select="reportSummaryDetail/colorGR" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorGI">
	<xsl:attribute name="background-color"><xsl:value-of select="reportSummaryDetail/colorGI" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorIN">
	<xsl:attribute name="background-color"><xsl:value-of select="reportSummaryDetail/colorIN" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorCN">
	<xsl:attribute name="background-color"><xsl:value-of select="reportSummaryDetail/colorCN" /></xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="colorPB">
	<xsl:attribute name="background-color"><xsl:value-of select="reportSummaryDetail/colorPB" /></xsl:attribute>
</xsl:attribute-set>

<xsl:include href="reportCommonHeader.xsl" />

	<xsl:template name="pptAccountCommon">

		<xsl:call-template name="reportCommonHeader"/>
		<fo:block space-before="0.5cm">
			<fo:table>
				<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
							<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
								<xsl:apply-templates select="intro1Text" />
							</fo:block>
							<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
								<xsl:apply-templates select="intro2Text" />
							</fo:block>
							<fo:block padding-before="0.3cm" xsl:use-attribute-sets="fonts_group2.size_12">
								<xsl:apply-templates select="rothMsg" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>

		<!--Showing Report Summary Information -->
		<fo:block space-before="0.5cm">
			<fo:table>
				<fo:table-column column-width="100%" xsl:use-attribute-sets="table-borderstyle"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block border-before-width="0.1cm" border-before-style="solid" border-before-color="#febe10" /><!--orange color -->
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
			<fo:table space-before="0.05cm">		
				<fo:table-column column-width="27%" xsl:use-attribute-sets="table-borderstyle"/>
				<fo:table-column column-width="23%" xsl:use-attribute-sets="table-borderstyle"/>
				<fo:table-column column-width="1%" xsl:use-attribute-sets="table-borderstyle"/>
				<fo:table-column column-width="49%" xsl:use-attribute-sets="table-borderstyle"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right"  xsl:use-attribute-sets="verdana_font.size_10">		
								Name:	
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block  padding-before="0.1cm"  xsl:use-attribute-sets="verdana_font.size_10">		
								<fo:inline>
									<xsl:value-of select="reportSummaryDetail/lastName" />
								</fo:inline>
								<fo:inline>, </fo:inline>
								<fo:inline>
									<xsl:value-of select="reportSummaryDetail/firstName" />
								</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell><fo:block/></fo:table-cell>
						<fo:table-cell number-rows-spanned="17" padding-start="0.2cm" background-color="#eceae3">	
							<fo:block font-weight="bold" padding-before="0.2cm" padding-start="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">		
									Asset Allocation by Risk/Return Category
							</fo:block>
							<fo:block padding-before="0.1cm" text-align="center">
								<fo:external-graphic>
									<xsl:attribute name="src">url('<xsl:value-of select="reportSummaryDetail/pieChartURL" />')</xsl:attribute>
								</fo:external-graphic>	
								
							</fo:block>
							<fo:block  xsl:use-attribute-sets="verdana_font.size_10">
								<fo:table space-before="0.1cm">
									<fo:table-column column-width="5%" xsl:use-attribute-sets="table-borderstyle"/>
									<fo:table-column column-width="55%" xsl:use-attribute-sets="table-borderstyle"/>
									<fo:table-column column-width="27%" xsl:use-attribute-sets="table-borderstyle"/>
									<fo:table-column column-width="12%" xsl:use-attribute-sets="table-borderstyle"/>
									<fo:table-body>
									<xsl:if test="reportSummaryDetail/totalAssetsLC">
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="colorLC">
											<fo:block/>
											</fo:table-cell>
											
											<fo:table-cell padding-start="0.3cm">
												<fo:block>	
													Target Date
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm">
												<fo:block text-align="right">
													<xsl:value-of select="reportSummaryDetail/totalAssetsLC" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-end="0.1cm">
												<fo:block text-align="right"> 
													<xsl:value-of select="reportSummaryDetail/totalPercentLC" />
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
										<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="colorAG">
										<fo:block/>
										</fo:table-cell>
										<fo:table-cell padding-start="0.3cm">
											<fo:block>	
												Aggressive Growth
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right">
												<xsl:value-of select="reportSummaryDetail/totalAssetsAG" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right"> 
												<xsl:value-of select="reportSummaryDetail/totalPercentAG" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="colorGR">
										<fo:block/>
										</fo:table-cell>
										<fo:table-cell padding-start="0.3cm">
											<fo:block>	
												Growth
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right">
												<xsl:value-of select="reportSummaryDetail/totalAssetsGR" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right"> 
												<xsl:value-of select="reportSummaryDetail/totalPercentGR" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="colorGI">
										<fo:block/>
										</fo:table-cell>
										<fo:table-cell padding-start="0.3cm">
											<fo:block>	
												Growth and Income
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right">
												<xsl:value-of select="reportSummaryDetail/totalAssetsGI" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right"> 
												<xsl:value-of select="reportSummaryDetail/totalPercentGI" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="colorIN">
										<fo:block/>
										</fo:table-cell>
										<fo:table-cell padding-start="0.3cm">
											<fo:block>	
												Income
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right">
												<xsl:value-of select="reportSummaryDetail/totalAssetsIN" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right"> 
												<xsl:value-of select="reportSummaryDetail/totalPercentIN" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="colorCN">
										<fo:block/>
										</fo:table-cell>
										<fo:table-cell padding-start="0.3cm">
											<fo:block>	
												Conservative
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right">
												<xsl:value-of select="reportSummaryDetail/totalAssetsCN" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right"> 
												<xsl:value-of select="reportSummaryDetail/totalPercentCN" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
									</fo:table-row>
									<xsl:if test="reportSummaryDetail/pbaAccount">
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="colorPB">
										<fo:block/>
										</fo:table-cell>
										<fo:table-cell padding-start="0.3cm">
											<fo:block>	
												Personal brokerage account
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right">
												<xsl:value-of select="reportSummaryDetail/totalAssetsPB" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding-end="0.1cm">
											<fo:block text-align="right"> 
												<xsl:value-of select="reportSummaryDetail/totalPercentPB" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell height="0.2cm"><fo:block/></fo:table-cell>
									</fo:table-row>
									</xsl:if>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
								SSN:	
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">		
								<xsl:value-of select="reportSummaryDetail/pptSSN" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
								Date of Birth:	
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">		
								<xsl:value-of select="reportSummaryDetail/dob" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
								<xsl:if test="reportSummaryDetail/age">
									Age:
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/age">
									<xsl:value-of select="reportSummaryDetail/age" />
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row space-after="0.3cm">
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/employmentStatus">
								Employment Status:
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/employmentStatus">
								<xsl:value-of select="reportSummaryDetail/employmentStatus" />
								</xsl:if>
								<xsl:if test="reportSummaryDetail/employmentStatusEffectiveDate">
								<xsl:value-of select="reportSummaryDetail/employmentStatusEffectiveDate" />
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row space-after="0.3cm">
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/status">
								Contribution Status:
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/status">
								<xsl:value-of select="reportSummaryDetail/status" />
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row space-after="0.3cm">
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
							<xsl:if test="reportSummaryDetail/pptGIFLSelect">
								Guaranteed Income feature
							</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.12cm" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/pptGIFLSelect">
								<xsl:value-of select="reportSummaryDetail/pptGIFLSelect" />
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>					
						<!-- SIP MA code --> 
					<xsl:if test="reportSummaryDetail/managedAccount">	
					<fo:table-row>
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">	
							Managed Accounts<fo:inline vertical-align="sup" padding-left="-3pt" baseline-shift="4pt" font-size="8pt">§</fo:inline>:
						</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" padding-top = "5pt" xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="reportSummaryDetail/managedAccount" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					<xsl:if test="not(reportSummaryDetail/managedAccount)">	
					<fo:table-row>
						<fo:table-cell background-color="#eceae3"><fo:block></fo:block></fo:table-cell>
						<fo:table-cell><fo:block ></fo:block></fo:table-cell>
					</fo:table-row>
					</xsl:if>
					<fo:table-row>
						<fo:table-cell padding-before="0.3cm" background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
								Total Assets:
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-before="0.3cm" padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">		
								<xsl:value-of select="reportSummaryDetail/totalAssets" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row> 
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
								Allocated Assets:
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">		
								<xsl:value-of select="reportSummaryDetail/allocatedAssets" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
							<xsl:if test="reportSummaryDetail/pbaAccount">
								Personal brokerage account:
							</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">		
							<xsl:if test="reportSummaryDetail/pbaAccount">
								<xsl:value-of select="reportSummaryDetail/pbaAccount" />
							</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell padding-after="0.4cm" background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/loanAssets">
								Loan assets:
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">		
							<xsl:if test="reportSummaryDetail/loanAssets">
								<xsl:value-of select="reportSummaryDetail/loanAssets" />
							</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/status">
									Default Date of Birth: 
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">	
							<xsl:if test="reportSummaryDetail/status">
							<xsl:if test="not(reportSummaryDetail/dob = 'January 1, 1980')">
								No
							</xsl:if>
							<xsl:if test="(reportSummaryDetail/dob = 'January 1, 1980')">
								Yes
							</xsl:if>
							</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<fo:table-row>
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">		
							<xsl:if test="reportSummaryDetail/status">
								Investment Instruction Type:  
							</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.52cm" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/status">
								<xsl:value-of select="reportSummaryDetail/investmentInstructionType" />
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/status">
								Last Contribution Date: 
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/status">
								<xsl:value-of select="reportSummaryDetail/lastContribDate" />
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/status">
								Automated Rebalance?: 
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/status">
								<xsl:value-of select="reportSummaryDetail/autoRebalanceInd" />
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell padding-before="0.1cm" background-color="#eceae3">
							<fo:block font-weight="bold" padding-before="0.1cm" text-align="right" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/status">
								<xsl:if test="reportSummaryDetail/rothFirstDepositYear">
								Year of first Roth contribution:  
								</xsl:if>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-before="0.1cm" padding-start="0.2cm" background-color="#eceae3">
							<fo:block padding-before="0.1cm" xsl:use-attribute-sets="verdana_font.size_10">	
								<xsl:if test="reportSummaryDetail/status">
								<xsl:if test="reportSummaryDetail/rothFirstDepositYear">
									<xsl:value-of select="reportSummaryDetail/rothFirstDepositYear" />
								</xsl:if>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
								
	</xsl:template>
	
</xsl:stylesheet>