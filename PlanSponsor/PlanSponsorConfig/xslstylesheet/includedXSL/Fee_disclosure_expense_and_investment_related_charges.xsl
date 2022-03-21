<?xml version="1.0" encoding="iso-8859-1"?>
	
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template match="b">
		<fo:inline font-weight="bold">
			<xsl:apply-templates select="*|text()" />
		</fo:inline>
	</xsl:template>

	<xsl:template match="strong">
		<fo:inline font-family = "Frutiger77BlackCn" >
			<xsl:apply-templates select="*|text()" />
		</fo:inline>
	</xsl:template>

	<xsl:template match="u">
		<fo:inline text-decoration="underline">
			<xsl:apply-templates select="*|text()" />
		</fo:inline>
	</xsl:template>

	<xsl:template match="p">
		<fo:block>
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>

	<xsl:template match="br">
		<fo:block padding-before="0.1cm">
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>

	<xsl:template match="i">
		<fo:inline font-style="italic">
			<xsl:apply-templates select="*|text()" />
		</fo:inline>
	</xsl:template>

	<xsl:template match="sup">
		<fo:inline vertical-align="super" xsl:use-attribute-sets="frutiger_font.size_7">
			<xsl:apply-templates select="*|text()" />
		</fo:inline>
	</xsl:template>

	<xsl:template match="center">
		<fo:block text-align="center">
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>
	<xsl:template name="fee_section_seven">
		<!-- Header -->
		<fo:page-sequence master-reference="A4" format="i" initial-page-number="0">
			<fo:static-content flow-name="xsl-region-before">
				<fo:block/>
			</fo:static-content>
			
			<!-- Footer -->
			<fo:static-content flow-name="xsl-region-after">
				<fo:table border-collapse="collapse" space-before="5mm">
					<fo:table-column column-width="100%" />
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell padding-start="2mm" padding-right="-7px">
								<fo:block text-align="right" xsl:use-attribute-sets="frutiger_font.size_9_black" color="cmyk(0, 0, 0, 0.8)">
									<fo:page-number />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_8"  padding-right="-7px" color="cmyk(0, 0, 0, 0.8)">
								<fo:block text-align="right">
									<xsl:value-of select="FEE_INFO/ALL_PAGE_FOOTER" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>
			
			<!-- Body -->
			<fo:flow flow-name="xsl-region-body">
				<!-- Expense Ratios and Investment Related Charges Title -->
				<fo:block>
					<fo:table>
						<fo:table-column column-width="10%" />
						<fo:table-column column-width="18%" />
						<fo:table-column column-width="72%" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding-left="4mm" number-columns-spanned="2">
									<fo:block>
										<fo:inline>
											<fo:external-graphic content-height="scale-to-fit" height=".82in" content-width="2in" scaling="non-uniform" >
												<xsl:attribute name="src">
													<xsl:value-of select="FEE_INFO/JH_LOGO_PATH"/>
												</xsl:attribute>
											</fo:external-graphic>
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block xsl:use-attribute-sets="frutiger_font.size_24" padding-right="2mm" padding-top="5mm" color="cmyk(0, 0, 0, 0.8)" text-align="right">
										<xsl:value-of select="FEE_INFO/REPORT_HEADER_TITLE1" />
									</fo:block>																	
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row >
							<fo:table-cell><fo:block>&#160;</fo:block></fo:table-cell>
							<fo:table-cell number-columns-spanned="2">
									<fo:block xsl:use-attribute-sets="frutiger_font.size_24" padding-right="2mm" padding-top="-8mm" color="cmyk(0, 0, 0, 0.8)" text-align="right">
										<fo:inline padding-left="1in">&#160;</fo:inline>
										<fo:inline><xsl:apply-templates select="FEE_INFO/REPORT_HEADER_TITLE2" /></fo:inline>
									</fo:block>																	
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="3">
									<fo:block border-top-style="solid" border-top-width="0.5px" font-size="7pt" border-color="cmyk(0, 0, 0, 1)">&#160; </fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:block>
				<fo:block>
					<fo:table>
						<fo:table-column column-width="48%" />
						<fo:table-column column-width="7%" />
						<fo:table-column column-width="45%" />
						<fo:table-body>
							<!-- Expense Ratios and Investment Related Charges Description -->
							<fo:table-row>
								<fo:table-cell number-columns-spanned="3">
									<fo:block xsl:use-attribute-sets="frutiger_font.size_9" space-before="1mm"  color="cmyk(0, 0, 0, 0.8)">
										<xsl:apply-templates select="FEE_INFO/SECTION5/DESCRIPTION" />
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="frutiger_font.size_9"  space-before="1mm"  color="cmyk(0, 0, 0, 0.8)">
										<xsl:apply-templates select="FEE_INFO/SECTION5/DESCRIPTION1" />
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="frutiger_font.size_9"  space-before="1mm"  color="cmyk(0, 0, 0, 0.8)">
										<xsl:apply-templates select="FEE_INFO/SECTION5/DESCRIPTION2" />
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="frutiger_font.size_9"  space-before="1mm"  color="cmyk(0, 0, 0, 0.8)">
										<xsl:apply-templates select="FEE_INFO/SECTION5/DESCRIPTION3" />
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="frutiger_font.size_9"  space-before="1mm"  color="cmyk(0, 0, 0, 0.8)">
										<xsl:apply-templates select="FEE_INFO/SECTION5/DESCRIPTION4" />
									</fo:block>
									
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row number-columns-spanned="3" >
								<fo:table-cell padding-top="2mm" padding-bottom="2mm">
									<fo:block xsl:use-attribute-sets="frutiger_font.size_11_black" padding-start="2pt" color="cmyk(0, 0, 0, 0.8)" font-size="bold">								
										<xsl:value-of select="FEE_INFO/SECTION5/FUND_CLASS_VALUE" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:block>
				
				
				<!-- Fund details table -->
				<xsl:if test="FEE_INFO/SECTION5/TABLE_HEADER">
				<fo:block>
					<fo:table space-before="3mm" space-after="2mm" border-collapse="collapse">
						<fo:table-column column-width="4%" />
						<fo:table-column column-width="20%" />
						<fo:table-column column-width="31%" />
						<fo:table-column column-width="48%" />
						
						<!-- Fund details table header  -->						
						<fo:table-header>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="3">
									<fo:block font-size="1pt" border-right-width="0.9pt">
										<fo:inline>
											<fo:external-graphic content-height="scale-to-fit" height="2.5mm" content-width="115.3mm" scaling="non-uniform">
											<xsl:attribute name="src">
												<xsl:value-of select="FEE_INFO/SECTION5/IMAGE_BLACK_BAR_ONE"/>
											</xsl:attribute>
										</fo:external-graphic>
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding-left=".2mm">
									<fo:block font-size="1pt">
										<fo:inline>
											<fo:external-graphic content-height="scale-to-fit" height="2.5mm" content-width="90.75mm" scaling="non-uniform">
											<xsl:attribute name="src">
												<xsl:value-of select="FEE_INFO/SECTION5/IMAGE_BLACK_BAR_TWO"/>
											</xsl:attribute>
										</fo:external-graphic>
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row color="cmyk(0, 0, 0, 0)" background-repeat="repeat">
								<xsl:attribute name="background-image">
										<xsl:value-of select="FEE_INFO/SECTION5/IMAGE_BLACK_BAR_THREE"/>
								</xsl:attribute>
								<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" text-align="left"  display-align="after" padding-left=".7mm" padding-after="2mm" border-right-color="#F9F9F9" border-right-style="solid" border-width="0.7pt">
									<fo:block>
										<fo:inline>
											<xsl:value-of select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN1" />
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" text-align="left"  display-align="after" padding-after="2mm" padding-left=".7mm" border-right-color="#F9F9F9" border-right-style="solid" border-width="0.7pt">
									<fo:block>
										<xsl:value-of select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN2" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" text-align="left"  display-align="after" padding-after="2mm" padding-left=".7mm" border-right-color="#F9F9F9" border-right-style="solid" border-width="0.7pt">
									<fo:block>
										<xsl:value-of select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN3" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" display-align="after" border-right-color="#F9F9F9" border-right-style="solid" border-width="0.7pt">
								<fo:table>
									<fo:table-column column-width="20%" />
									<fo:table-column column-width="65%" />
									<fo:table-column column-width="15%" />
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell number-columns-spanned="3" padding-top="2mm" >
												<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
														<xsl:value-of select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN4" />&#xA0;
														<xsl:value-of select="FEE_INFO/SECTION5/AS_OF_DATE" />												
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell >
												<fo:table>
													<fo:table-column column-width="100%" />
													<fo:table-body>
														<fo:table-row>
															<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																<fo:block>
																	<xsl:apply-templates select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN5" />
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
														<fo:table-row>
															<fo:table-cell padding-bottom = "11.43mm" border-right-color="#F9F9F9" border-right-style="solid" border-width="0.7pt">
																<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																	(1)
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
														<fo:table-row>
															<fo:table-cell border-right-color="#F9F9F9" border-right-style="solid" border-width="0.7pt">
																<fo:block text-align="center">
																	<xsl:apply-templates select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN6" />
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
													</fo:table-body>
												</fo:table>
											</fo:table-cell>
											<fo:table-cell >
												<fo:table>
													<fo:table-column column-width="29%" />
													<fo:table-column column-width="10%" />
													<fo:table-column column-width="25%" />
													<fo:table-column column-width="10%" />
													<fo:table-column column-width="26%" />
														<fo:table-body>
															<fo:table-row>
																<fo:table-cell number-columns-spanned="5">
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		<xsl:apply-templates select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN7" />&#xA0;
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<fo:table-row>
																<fo:table-cell>
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		(2)
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell>
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell>
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		(3)
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell>
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell border-right-color="#F9F9F9" border-right-style="solid" border-width="0.7pt">
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		(4)
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
															<fo:table-row>
																<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9"> 
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		<xsl:apply-templates select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN8" />&#xA0;
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell  xsl:use-attribute-sets="frutiger_font.size_9"> 
																	<fo:block text-align="center" xsl:use-attribute-sets="frutiger_font.size_9">
																		+
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" >
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		<xsl:apply-templates select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN9" />&#xA0;
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9"> 
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center" >
																		=
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" border-right-color="#F9F9F9" border-right-style="solid" border-width="0.7pt"> 
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		<xsl:apply-templates  select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN10" />&#xA0;
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
														</fo:table-body>
													</fo:table>
												</fo:table-cell>
												<fo:table-cell >		
								<fo:table>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell padding-bottom = "15.25mm">
												<fo:block  xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
													(5)
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9"  display-align="after" > 
												<fo:block text-align="center">
													<fo:inline>
														<xsl:apply-templates select="FEE_INFO/SECTION5/TABLE_HEADER/COLUMN11" />
													</fo:inline>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>	
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row height="2px">
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<fo:table-row border-bottom-style="dashed" border-top-style="dashed"  border-width="1.2pt" space-before="3mm"  border-color="cmyk(0, 0, 0, 0.50)" 
								              keep-with-next.within-page="always" >
								<fo:table-cell padding-left="10mm" number-columns-spanned="4" padding-before="1mm" padding-after="1mm" xsl:use-attribute-sets="frutiger_font.size_9_black"
									background-color="cmyk(0.57, 0, 0.06, 0.13)" color="cmyk(0, 0, 0, 0)">
									<fo:block>
										<xsl:value-of select="FEE_INFO/SECTION5/FUND_CATEGORYS/CLASS_TYPE/@NAME" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						<xsl:for-each select="FEE_INFO/SECTION5/FUND_CATEGORYS/CLASS_TYPE/FUND_CATEGORY">
								
								<!-- Fund Type  -->
								<fo:table-row border-bottom-style="dashed"  border-width="0.7pt" space-before="5mm"  border-color="cmyk(0, 0, 0, 0.50)"
								              keep-with-next.within-page="always" >
									<fo:table-cell padding-left="10mm" number-columns-spanned="4" padding-before="1mm" padding-after="1mm" background-color="cmyk(0.34, 0, 0.04, 0.08)" 
											xsl:use-attribute-sets="frutiger_font.size_9_bold" color="cmyk(0, 0, 0, 0)">
										<fo:block>
											<xsl:value-of select="@NAME" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<!-- Fund details like code, name, underlying  -->
								<xsl:for-each select="FUNDS/FUND">
									<fo:table-row border-bottom-style="dashed" border-width="0.7pt" color="cmyk(0, 0, 0, 1)" 
													background-color="cmyk(0.04, 0, 0.01, 0.01)" border-color="cmyk(0, 0, 0, 0.50)">
										<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" padding-top="1mm" padding-bottom="1mm" padding-left=".7mm">
											<fo:block>
												<xsl:value-of select="FUND_CODE" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" padding-top="1mm" padding-bottom="1mm" >
											<fo:block>
											    <xsl:apply-templates select="FUND_NAME" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" padding-top="1mm" padding-bottom="1mm" padding-left="1mm">
											<fo:block>
												<xsl:value-of select="FUND_UNDERLYING_NAME" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9_bold" color="cmyk(0, 0, 0, 1)" border-right-color="#e0e0e0" border-right-style="solid">
											<fo:block>
												<fo:table>
													<fo:table-column column-width="20%" border-right-color="#e0e0e0" border-right-style="solid" border-width="0.7pt"/>
													<fo:table-column column-width="19%" />
													<fo:table-column column-width="6.5%"/>
													<fo:table-column column-width="16%" />
													<fo:table-column column-width="6.5%"/>
													<fo:table-column column-width="17%" border-right-color="#e0e0e0" border-right-style="solid" border-width="0.7pt"/>
													<fo:table-column column-width="15%" />
														<fo:table-body>
															<fo:table-row>
																<fo:table-cell  xsl:use-attribute-sets="frutiger_font.size_9_bold" padding-top="1mm" padding-left="1mm">
																	<fo:block text-align="center">
																		<xsl:value-of select="INVESTMENT_SERVICES/UNDERLYING_FUND_NETCOST" />
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-top="1mm" padding-bottom="1mm" >
																	<fo:block text-align="center">
																		<xsl:value-of select="PLAN_SERVICES/REVENUE_FROM_UNDERLYING_FUND" />
																	</fo:block>
																</fo:table-cell>
																
																<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9"> 
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		+
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell padding-top="1mm" padding-bottom="1mm">
																	<fo:block text-align="center">
																		<xsl:value-of select="PLAN_SERVICES/REVENUE_FROM_SUBACCOUNT" />
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9" > 
																	<fo:block xsl:use-attribute-sets="frutiger_font.size_9" text-align="center">
																		=
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell  padding-top="1mm" padding-bottom="1mm">
																	<fo:block text-align="center">
																		<xsl:value-of select="PLAN_SERVICES/TOTAL_REVENUE" />
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell xsl:use-attribute-sets="frutiger_font.size_9_bold" color="cmyk(0, 0, 0, 1)" padding-top="1mm" padding-bottom="1mm">
											<fo:block text-align="center">
												<xsl:value-of select="EXPENSE_RATIO_PER" />
											</fo:block>
										</fo:table-cell>
																					
															</fo:table-row>
															
														</fo:table-body>
													</fo:table>
												</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:for-each>
							</xsl:for-each>
						</fo:table-body>
					</fo:table>
				</fo:block>
				</xsl:if>
				
				<!-- Foot notes content -->
				<xsl:for-each select="FEE_INFO/FOOTER_INFORMATION/FOOTERS/FOOTER"  >
					<fo:block keep-together.within-page="always" xsl:use-attribute-sets="frutiger_font.size_9" padding-after="0.2cm" color="cmyk(0, 0, 0, 0.8)" >
						<xsl:apply-templates select="." />
					</fo:block>
				</xsl:for-each>
				
				<!-- REPORT COPY RIGHT -->
				<fo:block keep-with-next.within-page="always">
					<fo:external-graphic content-height="scale-to-fit" height=".5in" content-width="1.52in" scaling="non-uniform" >
						<xsl:attribute name="src">
							<xsl:value-of select="FEE_INFO/JH_LOGO_PATH"/>
						</xsl:attribute>
					</fo:external-graphic>
				</fo:block>
				<fo:block border-top-style="solid" border-top-width="0.5px" space-before="7pt" font-size="7pt" border-color="cmyk(0, 0, 0, 1)"
								keep-with-next.within-page="always">&#160; </fo:block>
				<fo:block xsl:use-attribute-sets="frutiger_font.size_9" padding-top="2mm" color="cmyk(0, 0, 0, 0.8)"  
						          keep-with-next.within-page="always" keep-together.within-page="always">
							<xsl:apply-templates select="FEE_INFO/FOOTER_INFORMATION/DESCRIPTION1" />
				</fo:block>
				<fo:block xsl:use-attribute-sets="frutiger_font.size_8.5" padding-top="2mm" color="cmyk(0.75, 0.62, 0.49, 0.34)" 
						          keep-with-next.within-page="always" >
							<xsl:value-of select="FEE_INFO/FOOTER_INFORMATION/DESCRIPTION2" />
				</fo:block>
				<fo:block xsl:use-attribute-sets="frutiger_font.size_8.5" padding-top="2mm" color="cmyk(0.75, 0.62, 0.49, 0.34)"  
						          keep-with-next.within-page="always" >
							<xsl:value-of select="FEE_INFO/FOOTER_INFORMATION/COPY_RIGHT" />
				</fo:block>
				<fo:block xsl:use-attribute-sets="frutiger_font.size_8.5" padding-top="2mm" 
								keep-with-next.within-page="always" >
						<fo:table>
							<fo:table-column column-width="30%" />
							<fo:table-column column-width="50%" />
							<fo:table-column column-width="20%" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block text-align="left" color="cmyk(0.75, 0.62, 0.49, 0.34)">
											<xsl:apply-templates select="FEE_INFO/FOOTER_INFORMATION/DOCKET_NUMBER" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block>
										<!--   	<xsl:value-of select="FEE_INFO/ALL_PAGE_FOOTER" /> -->
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block text-align="right" color="cmyk(0.75, 0.62, 0.49, 0.34)">
											<xsl:value-of select="FEE_INFO/FOOTER_INFORMATION/GA_CODE" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>						 		
				</fo:block>
				
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>