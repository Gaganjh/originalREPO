<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:strip-space elements="*" />
	<!--  Common Attributes  -->
	<xsl:attribute-set name="table-borderstyle">
 		<!--<xsl:attribute name="border-style">solid</xsl:attribute>-->
	</xsl:attribute-set>
	<xsl:attribute-set name="normal-font-size">
		<xsl:attribute name="font-size">9pt</xsl:attribute>
		<xsl:attribute name="font-style">normal</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="section-title-style">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-style">normal</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="section-header-style">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-style">normal</xsl:attribute>
		<xsl:attribute name="font-size">11pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="section-data-style">
		<xsl:attribute name="font-style">normal</xsl:attribute>
		<xsl:attribute name="font-size">9pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table-header-style">
		<xsl:attribute name="font-style">normal</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table-border">
		<xsl:attribute name="border">0.5pt solid black</xsl:attribute>
		<xsl:attribute name="border-collapse">collapse</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table-cell-padding">
	  <xsl:attribute name="padding-left">2pt</xsl:attribute>
	  <xsl:attribute name="padding-right">2pt</xsl:attribute>
	  <xsl:attribute name="padding-top">2pt</xsl:attribute>
	  <xsl:attribute name="padding-bottom">2pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table-no-border">
		<xsl:attribute name="border">0pt solid black</xsl:attribute>
		<xsl:attribute name="border-collapse">collapse</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table-cell-style">
		<xsl:attribute name="border-top-style">none</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-left-style">solid</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">thin</xsl:attribute>
		<xsl:attribute name="border-left-width">thin</xsl:attribute>
		<xsl:attribute name="border-right-width">thin</xsl:attribute>
		<xsl:attribute name="padding-left">2pt</xsl:attribute>
	  	<xsl:attribute name="padding-right">2pt</xsl:attribute>
	  	<xsl:attribute name="padding-top">5pt</xsl:attribute>
	  	<xsl:attribute name="padding-bottom">4pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="table-cell-style-MT">
		<xsl:attribute name="border-top-style">none</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-left-style">solid</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">thin</xsl:attribute>
		<xsl:attribute name="border-left-width">thin</xsl:attribute>
		<xsl:attribute name="border-right-width">thin</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding-left">2pt</xsl:attribute>
	  	<xsl:attribute name="padding-right">2pt</xsl:attribute>
	  	<xsl:attribute name="padding-top">5pt</xsl:attribute>
	  	<xsl:attribute name="padding-bottom">4pt</xsl:attribute>
	</xsl:attribute-set>


	<xsl:attribute-set name="table-cell-style-small-font">
		<xsl:attribute name="border-top-style">none</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-left-style">solid</xsl:attribute>
		<xsl:attribute name="border-right-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">thin</xsl:attribute>
		<xsl:attribute name="border-left-width">thin</xsl:attribute>
		<xsl:attribute name="border-right-width">thin</xsl:attribute>
		<xsl:attribute name="font-size">6pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="block-style-very-small-font">
		<xsl:attribute name="font-size">7pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="block-style-small-font">
		<xsl:attribute name="font-size">8pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="block-style-italic-small-font">
		<xsl:attribute name="font-style">italic</xsl:attribute>
		<xsl:attribute name="font-size">6.5pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="empty-style">
		<xsl:attribute name="font-style">normal</xsl:attribute>
		<xsl:attribute name="color">white</xsl:attribute>
		<xsl:attribute name="font-size">5pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="section-space-style">
		<xsl:attribute name="font-style">normal</xsl:attribute>
		<xsl:attribute name="color">white</xsl:attribute>
		<xsl:attribute name="font-size">15pt</xsl:attribute>
	</xsl:attribute-set>

	<!--  Main template begins -->
	<xsl:template match="/sph">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"
			xsl:use-attribute-sets="normal-font-size">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4"
					page-width="8.5in" page-height="11in" margin-top="10mm"
					margin-bottom="0mm" margin-left="1cm" margin-right="0cm">
					<xsl:if test="header/planNameInUpperCase = 'true'">
									<xsl:if test = "string-length(header/planName) &lt;  67">
									<fo:region-body margin-left="1cm" margin-top="1.5cm" margin-right="1.7cm" margin-bottom="2cm" />					
					                <fo:region-before extent="1cm" />
					                <fo:region-after extent="2cm" />
					                <fo:region-start extent="1cm" />
					                <fo:region-end extent="1.7cm" />
					                </xsl:if>
					                <xsl:if test = "string-length(header/planName) >=  67">
									<fo:region-body margin-left="1cm" margin-top="2cm" margin-right="1.7cm" margin-bottom="2cm" />					
					                <fo:region-before extent="1cm" />
					                <fo:region-after extent="2cm" />
					                <fo:region-start extent="1cm" />
					                <fo:region-end extent="1.7cm" />
					                </xsl:if>
					</xsl:if>
					<xsl:if test="header/planNameInUpperCase != 'true'">
						           <xsl:if test = "string-length(header/planName) &lt;  83">
									<fo:region-body margin-left="1cm" margin-top="1.5cm" margin-right="1.7cm" margin-bottom="2cm" />					
					                <fo:region-before extent="1cm" />
					                <fo:region-after extent="2cm" />
					                <fo:region-start extent="1cm" />
					                 <fo:region-end extent="1.7cm" />
					               </xsl:if>
					               <xsl:if test = "string-length(header/planName) >=  83">
									<fo:region-body margin-left="1cm" margin-top="2cm" margin-right="1.7cm" margin-bottom="2cm" />					
					                <fo:region-before extent="1cm" />
					                <fo:region-after extent="2cm" />
					                <fo:region-start extent="1cm" />
					                <fo:region-end extent="1.7cm" />
					               </xsl:if>
					</xsl:if>
					<!-- <fo:region-end extent="0.5in" background-repeat="no-repeat">
						<xsl:attribute name="background-image">
						  url('<xsl:value-of select="header/pdfimagepath" />')
						</xsl:attribute>
					</fo:region-end>
					--> 
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="A4">

				<!-- Header -->

				<fo:static-content flow-name="xsl-region-before">
					<fo:table border-collapse="collapse" space-before="15mm">
					<xsl:attribute name="background-image">
						  url('<xsl:value-of select="header/pdfimagepath" />')
						</xsl:attribute>
						<fo:table-column column-width="15.77cm" xsl:use-attribute-sets="table-borderstyle"/>
						<fo:table-column column-width="2cm" xsl:use-attribute-sets="table-borderstyle"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding-start="2mm" padding-before="2mm">
									<fo:block space-before="3mm" space-after="3mm"
										xsl:use-attribute-sets="section-header-style">
										<xsl:value-of select="header/planName" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding-start="2mm" padding-before="2mm">
									<fo:block font-style="normal" text-align="right">
										<xsl:value-of select="header/pageNumber" />&#160;<fo:page-number />&#160;<xsl:value-of select="header/pageNumberOf" />&#160;<fo:page-number-citation ref-id="endofdoc" />&#160; 
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell padding-start="2mm" number-columns-spanned="2">
									<fo:block border-top-style="solid" border-top-width="0.5px">&#160;</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>

				<!-- Footer -->

				<fo:static-content flow-name="xsl-region-after">
					<fo:table border-collapse="collapse" space-before="5mm" >
					<xsl:attribute name="background-image">
						  url('<xsl:value-of select="header/pdfimagepath" />')
						</xsl:attribute>
						<fo:table-column column-width="5cm" xsl:use-attribute-sets="table-borderstyle"/>
						<fo:table-column column-width="12.77cm" xsl:use-attribute-sets="table-borderstyle"/>

						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding-start="2mm"  padding-before="2mm" number-columns-spanned="2">
									<fo:block border-top-style="solid" border-top-width="0.5px">&#160;</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell padding-before="-3mm" padding-start="2mm">
									<fo:block>
									<xsl:value-of select="footNotesSection/version" /></fo:block>
								</fo:table-cell>
								<fo:table-cell padding-before="-3mm" padding-start="2mm">
									<fo:block text-align="right" xsl:use-attribute-sets="block-style-small-font">
									<xsl:value-of select="footNotesSection/contractNumber" /> &#160;	</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>


				<!-- Body -->


				<fo:flow flow-name="xsl-region-body">
					<fo:table border-collapse="collapse" space-before="5mm">
						<fo:table-column column-width="3.25cm" />
						<fo:table-column column-width="14.75cm"/>
						<fo:table-body>
							<!-- Introductory Paragraph -->
							<fo:table-row>
								<fo:table-cell number-columns-spanned="2">
									<fo:block>
										<xsl:value-of select="header/introductory" />
									</fo:block>
									<fo:block xsl:use-attribute-sets="empty-style">
										empty
									</fo:block>
									<fo:block xsl:use-attribute-sets="empty-style">
										empty
									</fo:block>
									<fo:block xsl:use-attribute-sets="empty-style">
										empty
									</fo:block>
								</fo:table-cell>
							</fo:table-row>

							<!-- Eligibility Section -->
							<fo:table-row>
								<fo:table-cell padding="2pt">
									<fo:block xsl:use-attribute-sets="section-title-style">
										<xsl:value-of select="pageSectionText/eligibility" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="section-data-style" padding="2pt">
									<!--  <xsl:apply-templates select="eligibilitySection" /> -->
									<xsl:variable name="eligibilitySectionSpecified_var">
										<xsl:value-of select="eligibilitySection/eligibilitySectionSpecified" />
									</xsl:variable>
									<xsl:variable name="eligibilityExcludedEmployeeSpecified_var">
										<xsl:value-of
											select="eligibilitySection/eligibilityExcludedEmployeeSpecified" />
									</xsl:variable>
									<xsl:variable name="multipleEligibilityRulesForOneSingleMoneyType_var">
										<xsl:value-of select="eligibilitySection/multipleEligibilityRulesForOneSingleMoneyType" />
									</xsl:variable>
									
									<xsl:if test="$multipleEligibilityRulesForOneSingleMoneyType_var ='Y'">
											<fo:block>
												<xsl:for-each select="messages/message">
													<xsl:if test="@id='multipleEligibilityRulesForOneSingleMoneyType_MSG'">
														<xsl:value-of select="*|text()" />
													</xsl:if>
												</xsl:for-each>
											</fo:block>
											<fo:block xsl:use-attribute-sets="section-space-style">
												empty
											</fo:block>
									</xsl:if>
									
									<xsl:if test="$eligibilityExcludedEmployeeSpecified_var='N' and $eligibilitySectionSpecified_var='N' and $multipleEligibilityRulesForOneSingleMoneyType_var ='N'">
											<fo:block>
												<xsl:for-each select="messages/message">
													<xsl:if test="@id='eligibilitySectionSpecifiedMSG'">
														<xsl:value-of select="*|text()" />
													</xsl:if>
												</xsl:for-each>
											</fo:block>
											<fo:block xsl:use-attribute-sets="section-space-style">
												empty
											</fo:block>
									</xsl:if>
									<xsl:if test="$multipleEligibilityRulesForOneSingleMoneyType_var ='N'">
									 <xsl:if test="$eligibilitySectionSpecified_var='Y' or $eligibilityExcludedEmployeeSpecified_var='Y'">
																						
												<xsl:if test="$eligibilityExcludedEmployeeSpecified_var='Y'">
													<!-- eligibility exclusions Section -->
														<xsl:variable name="noExcludedEmployees_var">
																<xsl:value-of select="eligibilitySection/noExcludedEmployees"/>
														</xsl:variable>
														<xsl:variable name="showExcludedGrid_var">
																<xsl:value-of select="eligibilitySection/showExcludedGrid"/>
														</xsl:variable>
														<xsl:variable name="otherOption_var">
																	<xsl:value-of select="eligibilitySection/otherOption"/>
														</xsl:variable>
														
														<xsl:if test="$otherOption_var='Y'">
															<fo:block>
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='eligibilityExcludedEmployeesOtherMSG'">
																			<xsl:value-of select="*|text()"/>
																	</xsl:if>
																</xsl:for-each>
															</fo:block>
														</xsl:if>
														
														<xsl:if test="$noExcludedEmployees_var='N'">
															<xsl:if test="$showExcludedGrid_var='N'">
															<fo:block>
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='eligibilityExcludedEmployeesMSG'">
																			<xsl:value-of select="*|text()"/>
																	</xsl:if>
																</xsl:for-each>
															</fo:block>
															<fo:block xsl:use-attribute-sets="empty-style">
														    	empty
															</fo:block>
															</xsl:if>
					
														</xsl:if>
														
														<!--  eligibility exclusions Section Table display starts -->
														<xsl:if test="$showExcludedGrid_var='Y'">
														
																<xsl:variable name="showUnion_var">
																	<xsl:value-of select="eligibilitySection/showUnion"/>
																</xsl:variable>
																<xsl:variable name="showNonResidentAliens_var">
																	<xsl:value-of select="eligibilitySection/showNonResidentAliens"/>
																</xsl:variable>
																<xsl:variable name="showHighlyCompensated_var">
																	<xsl:value-of select="eligibilitySection/showHighlyCompensated"/>
																</xsl:variable>
																<xsl:variable name="showLeased_var">
																	<xsl:value-of select="eligibilitySection/showLeased"/>
																</xsl:variable>
																<xsl:variable name="showPartTimeOrTemporary_var">
																	<xsl:value-of select="eligibilitySection/showPartTimeOrTemporary"/>
																</xsl:variable>
																
																
																<fo:block>
																	<xsl:value-of select="tableHeaderSection/exclutionsAboveTableMSG"/>
																</fo:block>
																
																							
																<fo:table border-collapse="collapse" space-before="2mm"	xsl:use-attribute-sets="table-border">
																		<fo:table-column column-width="5.70cm"	xsl:use-attribute-sets="table-border" />
																		<xsl:if test="$showUnion_var='Y'">
																			<fo:table-column column-width="2.25cm"	xsl:use-attribute-sets="table-border" />
																		</xsl:if>
																		<xsl:if test="$showNonResidentAliens_var='Y'">
																			<fo:table-column column-width="2.25cm"	xsl:use-attribute-sets="table-border" />
																		</xsl:if>
																		<xsl:if test="$showHighlyCompensated_var='Y'">
																			<fo:table-column column-width="2.25cm"	xsl:use-attribute-sets="table-border" />
																		</xsl:if>
																		<xsl:if test="$showLeased_var='Y'">
																			<fo:table-column column-width="2cm"	xsl:use-attribute-sets="table-border" />
																		</xsl:if>
																		<xsl:if test="$showPartTimeOrTemporary_var='Y'">
																			<fo:table-column column-width="2cm"	xsl:use-attribute-sets="table-border" />
																		</xsl:if>
																		<fo:table-column />
																	
																	<fo:table-body>
																		<fo:table-row>
																				<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																					<fo:block xsl:use-attribute-sets="table-header-style">
																						<xsl:value-of select="tableHeaderSection/moneyType" />
																					</fo:block>
																				</fo:table-cell>
																					
																					<xsl:if test="$showUnion_var='Y'">
																					<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																						<fo:block xsl:use-attribute-sets="table-header-style" text-align="center">
																							<xsl:value-of select="tableHeaderSection/union" />
																						</fo:block>
																					</fo:table-cell>
																					</xsl:if>
																					
																					<xsl:if test="$showNonResidentAliens_var='Y'">
																					<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																						<fo:block xsl:use-attribute-sets="table-header-style" text-align="center">
																							<xsl:value-of select="tableHeaderSection/nonresidentAliens" />
																						</fo:block>
																					</fo:table-cell>
																					</xsl:if>
																					
																					<xsl:if test="$showHighlyCompensated_var='Y'">
																					<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																						<fo:block xsl:use-attribute-sets="table-header-style" text-align="center">
																							<xsl:value-of select="tableHeaderSection/highlyCompensated" />
																						</fo:block>
																					</fo:table-cell>
																					</xsl:if>
																					
																					<xsl:if test="$showLeased_var='Y'">
																					<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																						<fo:block xsl:use-attribute-sets="table-header-style" text-align="center">
																							<xsl:value-of select="tableHeaderSection/leased" />	
																						</fo:block>
																					</fo:table-cell>
																					</xsl:if>
															
																					
																		</fo:table-row>
																		<!-- For loop starts-->
																		<xsl:for-each select="eligibilitySection/exclusionTable">
																			<fo:table-row>
																					<fo:table-cell xsl:use-attribute-sets="table-cell-style-MT" display-align="center" >
																						<fo:block>
																							<xsl:value-of select="./moneyType/text()" /> 
																						</fo:block>
																					</fo:table-cell>
																					
																					<xsl:if test="$showUnion_var='Y'">
																						<fo:table-cell xsl:use-attribute-sets="table-cell-style-MT" text-align="center" display-align="center" >
																							<fo:block>
																								<xsl:value-of select="./union/text()" />
																							</fo:block>
																						</fo:table-cell>
																					</xsl:if>
																					
																					<xsl:if test="$showNonResidentAliens_var='Y'">
																						<fo:table-cell xsl:use-attribute-sets="table-cell-style-MT" text-align="center" display-align="center" >
																							<fo:block>
																								<xsl:value-of select="./nonResidentAliens/text()" />
																							</fo:block>
																						</fo:table-cell>
																					</xsl:if>
																					
																					
																					<xsl:if test="$showHighlyCompensated_var='Y'">
																						<fo:table-cell xsl:use-attribute-sets="table-cell-style-MT" text-align="center" display-align="center" >
																							<fo:block>
																								<xsl:value-of select="./highlyCompensated/text()" />
																							</fo:block>
																						</fo:table-cell>
																					</xsl:if>
																						
																					<xsl:if test="$showLeased_var='Y'">
																						<fo:table-cell xsl:use-attribute-sets="table-cell-style-MT" text-align="center" display-align="center" >
																							<fo:block>
																								<xsl:value-of select="./leased/text()" />
																							</fo:block>
																						</fo:table-cell>
																					</xsl:if>
																					
																					<xsl:if test="$showPartTimeOrTemporary_var='Y'">
																						<fo:table-cell xsl:use-attribute-sets="table-cell-style-MT" text-align="center" display-align="center" >
																							<fo:block>
																								<xsl:value-of select="./partTimeOrTemporary/text()" />
																							</fo:block>
																						</fo:table-cell>
																					</xsl:if>
																	
																					
													  						</fo:table-row>
																		</xsl:for-each>
																		<!-- for loop ends -->			
																		
							       									</fo:table-body>
							       									
							     								</fo:table>
							     								<fo:block xsl:use-attribute-sets="empty-style">
																	   	empty
																</fo:block>
																<!-- Added for PRODUCTION FIX on Nov 27 - 2009 -->
																<fo:block>
																	<xsl:variable name="otherOptionSelectedFoaAnyMT_var">
																		<xsl:value-of select="eligibilitySection/otherOptionSelectedAnyMoneyType"/>
																	</xsl:variable>		
																	<xsl:if test="$otherOptionSelectedFoaAnyMT_var='Y'">
																			<xsl:for-each select="messages/message">
																				<xsl:if test="@id='eligibilityExcludedEmployeeOtherSelected_MSG'">
																					<xsl:value-of select="*|text()" />
																				</xsl:if>
																			</xsl:for-each>
																			<fo:block xsl:use-attribute-sets="empty-style">
																	   			empty
																			</fo:block>
																	</xsl:if>
																</fo:block>
																
																
														</xsl:if>
														<!--  eligibility exclusions Section Table display Ends -->
													</xsl:if> <!--  End if for No Exculded Eligibilty Specified check -->
													<!-- Paragraph 2 -->
														<fo:block>		
																<xsl:variable name="eligibilityExcludedEmployeeParagraph_2_1_var">
																	<xsl:value-of select="eligibilitySection/eligibilityExcludedEmployeeParagraph_2_1" />
																</xsl:variable>
																<xsl:if test="$eligibilityExcludedEmployeeParagraph_2_1_var='Y'">
																		<xsl:for-each select="messages/message">
																			<xsl:if test="@id='eligibilityExcludedEmployeeParagraph_2_1_MSG'">
																				<xsl:value-of select="*|text()" />&#160;</xsl:if>
																		</xsl:for-each>
																</xsl:if>
													
													
															<!--  Message display starts -->
					
															<xsl:variable name="allIdentical_var">
																<xsl:value-of select="eligibilitySection/allIdentical" />
															</xsl:variable>
															<xsl:variable name="eligibilityParagraph_1_1_var">
																<xsl:value-of select="eligibilitySection/eligibilityParagraph_1_1" />
															</xsl:variable>
					
															
					
															<xsl:if test="$eligibilityParagraph_1_1_var!='N'">
																<fo:block>
																	<xsl:for-each select="messages/message">
																		<xsl:if test="@id='eligibilityParagraph_1_1'">
																			<xsl:value-of select="*|text()" />
																		</xsl:if>
																	</xsl:for-each>
																</fo:block>
																
															</xsl:if>
					
															<xsl:if test="$allIdentical_var='Y'">
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='eligibilitySection'">
																		<xsl:value-of select="*|text()" />
																	</xsl:if>
																</xsl:for-each>
																
															</xsl:if>
					
															<!--  Message display ends -->
					
															<xsl:if test="$eligibilitySectionSpecified_var='Y'">
					
																<!--  Table display starts -->
																<xsl:if test="$allIdentical_var='N'">
																	<!--  Getting value in variable starts -->
					
																	<xsl:variable name="showMinimumAge_var">
																		<xsl:value-of select="eligibilitySection/showMinimumAge" />
																	</xsl:variable>
					
																	<xsl:variable name="showHoursOfService_var">
																		<xsl:value-of select="eligibilitySection/showHoursOfService" />
																	</xsl:variable>
					
																	<xsl:variable name="showLengthOfService_var">
																		<xsl:value-of select="eligibilitySection/showLengthOfService" />
																	</xsl:variable>
																	<xsl:variable name="showEligibilty_var">
																		<xsl:value-of select="eligibilitySection/showEligibilty" />
																	</xsl:variable>
																	<xsl:variable name="colSpan_var">
																		<xsl:value-of select="eligibilitySection/colSpan" />
																	</xsl:variable>
					
					
																	<!--  Getting value in variable ends -->
					
																		<xsl:for-each select="messages/message">
																			<xsl:if test="@id='eligibilityRequiredLead'">
																				<xsl:value-of select="*|text()" />
																			</xsl:if>
																		</xsl:for-each>
					
					
																	<fo:table border-collapse="collapse" space-before="2mm"
																		xsl:use-attribute-sets="table-border">
																		<fo:table-column column-width="5.70cm"
																			xsl:use-attribute-sets="table-border" />
																		<xsl:if test="$showMinimumAge_var='Y'">
																			<fo:table-column column-width="1.75cm"
																				xsl:use-attribute-sets="table-border" />
																		</xsl:if>
																		<xsl:if test="$showHoursOfService_var='Y'">
																			<fo:table-column column-width="1.75cm"
																				xsl:use-attribute-sets="table-border" />
																		</xsl:if>
																		<xsl:if test="$showLengthOfService_var='Y'">
																			<fo:table-column column-width="2.5cm"
																				xsl:use-attribute-sets="table-border" />
																		</xsl:if>
																		<xsl:if test="$showMinimumAge_var!='Y'">
																			<fo:table-column column-width="2cm"
																				xsl:use-attribute-sets="table-border" />
																		</xsl:if>
																		<fo:table-column />
					
																		<fo:table-body>
					
																			<fo:table-row>
					
																				<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																					<fo:block xsl:use-attribute-sets="table-header-style">
																						<xsl:value-of select="tableHeaderSection/moneyType" />
																					</fo:block>
																				</fo:table-cell>
																				<xsl:if test="$showEligibilty_var='Y'">
																					<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																						<fo:block xsl:use-attribute-sets="table-header-style"
																							text-align="center">
																							<xsl:value-of select="tableHeaderSection/eligibility" />
																						</fo:block>
																					</fo:table-cell>
																				</xsl:if>
																				<xsl:if test="$showMinimumAge_var='Y'">
																					<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																						<fo:block xsl:use-attribute-sets="table-header-style"
																							text-align="center">
																							<xsl:value-of select="tableHeaderSection/minimmumAge" />
																						</fo:block>
																					</fo:table-cell>
																				</xsl:if>
																				<xsl:if test="$showHoursOfService_var='Y'">
																					<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																						<fo:block xsl:use-attribute-sets="table-header-style"
																							text-align="center">
																							<xsl:value-of select="tableHeaderSection/hoursOfService" />
																						</fo:block>
																					</fo:table-cell>
																				</xsl:if>
																				<xsl:if test="$showLengthOfService_var='Y'">
																					<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																						<fo:block xsl:use-attribute-sets="table-header-style"
																							text-align="center">
																							<xsl:value-of select="tableHeaderSection/periodOfService" />
																						</fo:block>
																					</fo:table-cell>
																				</xsl:if>
																			</fo:table-row>
																			<!-- For loop starts-->
					
																			<xsl:for-each select="eligibilitySection/listOfMoneyType">
					
																				<xsl:variable name="skipRow_var">
																					<xsl:value-of select="./skipRow/text()" />
																				</xsl:variable>
																				<xsl:if test="$skipRow_var='N'">
																					<fo:table-row>
					
																						<fo:table-cell xsl:use-attribute-sets="table-cell-style-MT">
																							<fo:block>
																							<xsl:value-of select="./moneyType/text()" /> 
																						</fo:block>
																					</fo:table-cell>
																					
																					<xsl:variable name="EligibilityChecked_var">
																						<xsl:value-of select="./EligibilityChecked/text()" />
																					</xsl:variable>
																				
																					
																					<xsl:if test="$showEligibilty_var='N'">
																					<xsl:if test="$EligibilityChecked_var='immediate'">
																						<xsl:if test="$colSpan_var='3'">
																							<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" display-align="center" number-columns-spanned="3" >																	
																								<fo:block vertical-align="middle">
																									<xsl:value-of select="./eligibilityCheckedText/text()" />
																								</fo:block>
																							</fo:table-cell>
																						</xsl:if>
																						<xsl:if test="$colSpan_var='2'">
																							<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" display-align="center" number-columns-spanned="1" >																	
																								<fo:block>
																									<xsl:value-of select="./eligibilityCheckedText/text()" />
																								</fo:block>
																							</fo:table-cell>
																						</xsl:if>
																						<xsl:if test="$colSpan_var='1'">
																							<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" display-align="center" number-columns-spanned="2" >																	
																								<fo:block>
																									<xsl:value-of select="./eligibilityCheckedText/text()" />
																								</fo:block>
																							</fo:table-cell>
																						</xsl:if>
																						<xsl:if test="$colSpan_var='0'">
																							<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center"  display-align="center" number-columns-spanned="3" >																	
																								<fo:block>
																									<xsl:value-of select="./eligibilityCheckedText/text()" />
																								</fo:block>
																							</fo:table-cell>
																						</xsl:if>
																					</xsl:if>
																					</xsl:if>
																					
																					<xsl:if test="$showEligibilty_var='Y'">
																					<xsl:if test="$EligibilityChecked_var='immediate'"> 
																							<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center"  display-align="center" >
																								<fo:block>
																									<xsl:value-of select="./onlyEligibilityCheckedText/text()" />
																								</fo:block>
																							</fo:table-cell>
																					</xsl:if>
																					</xsl:if>
																					 
																					<xsl:if test="$EligibilityChecked_var!='immediate'"> 
																						<xsl:if test="$showMinimumAge_var='Y'">
																							<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" display-align="center" >
																								<fo:block>
																									<xsl:value-of select="./minimumAge/text()" />
																								</fo:block>
																							</fo:table-cell>
																						</xsl:if>
																						
																						<xsl:if test="$showHoursOfService_var='Y'">
																							<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" display-align="center" >
																								<fo:block>
																									<xsl:value-of select="./hoursOfService/text()" />
																								</fo:block>
																							</fo:table-cell>
																						</xsl:if>
																						
																						<xsl:if test="$showLengthOfService_var='Y'">
																							<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" display-align="center" >
																								<fo:block>
																									<xsl:value-of select="./lengthOfService/text()" />
																								</fo:block>
																							</fo:table-cell>
																						</xsl:if>
																					</xsl:if>
													  						</fo:table-row>
													  						</xsl:if>
																		</xsl:for-each>
																		
																		<!-- for loop ends -->			
							       									</fo:table-body>
							     								</fo:table>
							     								<fo:block xsl:use-attribute-sets="empty-style">
														    			empty
																</fo:block>
																
															</xsl:if>
														<!--  Table display ends -->
														<!-- 2.4.3.3	Paragraph 3 -->
														
														</xsl:if>
														<xsl:variable name="otherOption2_var">
																<xsl:value-of select="eligibilitySection/otherOptionSelectedAnyMoneyType"/>
														</xsl:variable>
														<xsl:variable name="isMoneyTypeMissingInTheTable_var">
																<xsl:value-of select="eligibilitySection/isMoneyTypeMissingInTheTable"/>
														</xsl:variable>
														<xsl:variable name="isMoneyTypeWihtHoursGreaterThanZero_var">
																<xsl:value-of select="eligibilitySection/isMoneyTypeWihtHoursGreaterThanZero"/>
														</xsl:variable>	
														
														<xsl:choose>
															<xsl:when test="$isMoneyTypeWihtHoursGreaterThanZero_var='Y'">
																<fo:block>
																	<xsl:for-each select="messages/message">
																		<xsl:if test="@id='eligibilityMoneyTypeHoursGreaterThanZero'">
																			<xsl:value-of select="*|text()"/>
																		</xsl:if>
																	</xsl:for-each>
																</fo:block>
															</xsl:when>
															<xsl:otherwise>
																<xsl:if test="$otherOption2_var='Y' or $isMoneyTypeMissingInTheTable_var='Y'">
																	<fo:block>
																		<xsl:for-each select="messages/message">
																			<xsl:if test="@id='eligibilityExcludedEmployeesOtherMSG2'">
																				<xsl:value-of select="*|text()"/>
																			</xsl:if>
																		</xsl:for-each>
																	</fo:block>
																</xsl:if>
															</xsl:otherwise>
														</xsl:choose>
														
														<fo:block xsl:use-attribute-sets="section-space-style">empty</fo:block>
																												
													</fo:block> 
								
									</xsl:if> <!--  multipleEligibilityRulesForOneSingleMoneyType_var checking if ends here -->
								  </xsl:if>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block xsl:use-attribute-sets="section-title-style">
										<!--empty-->
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block>
										<!--empty-->
									</fo:block>
								</fo:table-cell>
							</fo:table-row>	
							
							<!-- If the Plan Entry freq is empty , do not show this section -->
							<xsl:variable name="planEntryFrequency_chk_var">
											<xsl:value-of select="entryDatesSection/planEntryFrequency"/>
							</xsl:variable>
							<xsl:variable name="isPlanHasAutoEnrollment_var">
											<xsl:value-of select="entryDatesSection/isPlanHasAutoEnrollment"/>
							</xsl:variable>
							
							<xsl:if test="$planEntryFrequency_chk_var!='Empty'"> 
							
								<fo:table-row>
									<fo:table-cell padding="2pt">
											<fo:block xsl:use-attribute-sets="section-title-style">
												<xsl:value-of select="pageSectionText/entryDates" />
										    </fo:block>
									</fo:table-cell>
									<fo:table-cell padding="2pt">
										<fo:block>
											<xsl:variable name="planEntryFrequency_var">
												<xsl:value-of select="entryDatesSection/planEntryFrequency"/>
											</xsl:variable>
											<xsl:variable name="entryDatesSpecifiedAnyMoneyType_var">
												<xsl:value-of select="entryDatesSection/entryDatesSpecifiedAnyMoneyType"/>
											</xsl:variable>
										
											<!-- <xsl:if test="$planEntryFrequency_var!='Complex'"> --> 
												<xsl:for-each select="messages/message">
													<xsl:if test="@id='entryDatesSection'">
														<xsl:value-of select="*|text()"/>
													</xsl:if>
												</xsl:for-each>
											<!-- </xsl:if> -->
											
											<!-- *************Table else -->
										
											<xsl:if test="$planEntryFrequency_var='Complex'"> 	
												
													<!-- 3.1.4 --><!-- Once you become eligible and join the plan, contributions will begin on the next: -->
												<xsl:variable name="planEntryDatesTableLead_var">
														<xsl:value-of select="tableHeaderSection/planEntryDatesTableLead" />
												</xsl:variable>
												<xsl:if test="$planEntryDatesTableLead_var!=''">
													<fo:block>
														<xsl:value-of select="tableHeaderSection/planEntryDatesTableLead" />
													</fo:block>
												</xsl:if>
																									
												<fo:table border-collapse="collapse" space-before="2mm"	xsl:use-attribute-sets="table-border" table-layout="fixed">
														<fo:table-column column-width="5.70cm"	xsl:use-attribute-sets="table-border" />
														<fo:table-column column-width="8.75cm"/>
			
													<fo:table-body>
														<fo:table-row>
																<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																	<fo:block xsl:use-attribute-sets="table-header-style">
																		<xsl:value-of select="tableHeaderSection/moneyType" />
																	</fo:block>
																</fo:table-cell>
																<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center" >
																	<fo:block xsl:use-attribute-sets="table-header-style">
																		<xsl:value-of select="tableHeaderSection/planEntryDates" />
																	</fo:block>
																</fo:table-cell>
														</fo:table-row>
														
														<!-- For loop -->
														<xsl:for-each select="entryDatesSection/typeAndEntry">
															<fo:table-row keep-with-previous="always">
																	<fo:table-cell xsl:use-attribute-sets="table-cell-style-MT" display-align="center">
																		<fo:block>
																			<xsl:value-of select="./moneyType/text()" /> 
																		</fo:block>
																	</fo:table-cell>
									
																	<fo:table-cell xsl:use-attribute-sets="table-cell-style" >
																		<fo:block>
																			<xsl:value-of select="./planEntryDates/text()" />
																		</fo:block>
																	</fo:table-cell>
									  						</fo:table-row>
														</xsl:for-each>
														<!-- for loop ends -->
														
			       									</fo:table-body>
			     								</fo:table>
			     								
			     								<xsl:if test="$entryDatesSpecifiedAnyMoneyType_var='Y'"> 
				     								<fo:block xsl:use-attribute-sets="empty-style">
										    			empty
													</fo:block>	
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='entryDatesSpecifiedAnyMoneyType'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
											    </xsl:if> 
			     								
											</xsl:if>
												<!-- ****************table else ends -->
																				
											<xsl:if test="$isPlanHasAutoEnrollment_var='Y'">
												<fo:block xsl:use-attribute-sets="empty-style">
										    		empty
												</fo:block>	
												<fo:block>
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='automaticEnrollmentFeatureMSG'">
															<xsl:value-of select="*|text()" />
														</xsl:if>
													</xsl:for-each>
												</fo:block>
											</xsl:if>
										</fo:block>
										<fo:block xsl:use-attribute-sets="section-space-style">
										    	empty
										</fo:block>	
									</fo:table-cell>
								</fo:table-row>
							  </xsl:if>  <!--  End of Plan Entry frq empty check ends -->	
							  <!-- Your Contributions Specified or unspecified  -->
							<xsl:variable name="yourContributionsMTSpecified_var">
								<xsl:value-of select="yourContributionsSection/yourContributionsMTSpecified"/>
							</xsl:variable>
							<!-- all paragraph_ empty display  -->
							<xsl:variable name="allParagraphEmpty_var">
								<xsl:value-of select="yourContributionsSection/allParagraphEmpty"/>
							</xsl:variable>	
							<xsl:if test="$yourContributionsMTSpecified_var='Y'"> 
									<fo:table-row>
										<fo:table-cell padding="2pt">
											<fo:block xsl:use-attribute-sets="section-title-style">
												<xsl:value-of select="pageSectionText/yourContributions" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="2pt">
											<fo:block>		
											<xsl:variable name="paragraph_1_1_var">
													<xsl:value-of select="yourContributionsSection/paragraph_1_1"/>
											</xsl:variable>	
											<xsl:variable name="paragraph_1_2_var">
													<xsl:value-of select="yourContributionsSection/paragraph_1_2"/>
											</xsl:variable>	
											<xsl:variable name="paragraph_1_3_var">
													<xsl:value-of select="yourContributionsSection/paragraph_1_3"/>
											</xsl:variable>	
											<xsl:variable name="paragraph_2_1_var">
													<xsl:value-of select="yourContributionsSection/paragraph_2_1"/>
											</xsl:variable>
											<xsl:variable name="paragraph_3_1_var">
													<xsl:value-of select="yourContributionsSection/paragraph_3_1"/>
											</xsl:variable>
											<xsl:variable name="paragraph_3_2_var">
													<xsl:value-of select="yourContributionsSection/paragraph_3_2"/>
											</xsl:variable>	
											<xsl:variable name="paragraph_4_var">
													<xsl:value-of select="yourContributionsSection/paragraph_4"/>
											</xsl:variable>
											
											
																				
												<fo:block>								
												<xsl:if test="$yourContributionsMTSpecified_var='N'"> 
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='yourContributionsMTSpecified_MSG'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
													<fo:block xsl:use-attribute-sets="empty-style">
												    	empty
													</fo:block>	
												</xsl:if>
												</fo:block>
												<!--  Specified = Y only we can display other data -->
												<xsl:if test="$yourContributionsMTSpecified_var='Y'">
														
													<xsl:if test="$allParagraphEmpty_var='Y'"> 
														<xsl:for-each select="messages/message">
															<xsl:if test="@id='yourContributionsMTSpecified_MSG'">
																<xsl:value-of select="*|text()"/>
															</xsl:if>
														</xsl:for-each>
														<fo:block xsl:use-attribute-sets="empty-style">
										   					empty
														</fo:block>	
													</xsl:if>  
													<!-- paragraph_1_1 display  -->
													<fo:block>															
															<xsl:if test="$paragraph_1_1_var='Y'"> 
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='yourContributions_1_1'">
																		<xsl:value-of select="*|text()"/>&#160;
																	</xsl:if>
																</xsl:for-each>
															</xsl:if>
															<!-- paragraph_1_2 display  -->
															<xsl:if test="$paragraph_1_2_var='Y'"> 
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='yourContributions_1_2'">
																		<xsl:value-of select="*|text()"/>&#160;
																	</xsl:if>
																</xsl:for-each>
															</xsl:if>										
															<!-- paragraph_1_3 display  -->
															<xsl:if test="$paragraph_1_3_var='Y'"> 
																<xsl:for-each select="messages/message">
																	<xsl:if test="@id='yourContributions_1_3'">
																		<xsl:value-of select="*|text()"/>&#160;
																	</xsl:if>
																</xsl:for-each>
															</xsl:if>		
														</fo:block>
	
														<!-- paragraph_2_1 display  -->
														<fo:block>
														<xsl:if test="$paragraph_2_1_var='Y'">
															<fo:block xsl:use-attribute-sets="empty-style">
											   					empty
															</fo:block> 
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='yourContributions_2_1'">
																	<xsl:value-of select="*|text()"/>
																</xsl:if>
															</xsl:for-each>
														</xsl:if>										
													</fo:block>
													<!-- paragraph_3_1 display  -->
													<fo:block>
														<xsl:if test="$paragraph_3_1_var='Y'"> 
															<fo:block xsl:use-attribute-sets="empty-style">
											   					empty
															</fo:block> 
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='yourContributions_3_1'">
																	<xsl:value-of select="*|text()"/>&#160;
																</xsl:if>
															</xsl:for-each>
														</xsl:if>										
														
														<!-- paragraph_3_2 display  -->
														
																						
														<xsl:if test="$paragraph_3_2_var='Y'"> 
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='yourContributions_3_2'">
																	<xsl:value-of select="*|text()"/>
																</xsl:if>
															</xsl:for-each>
														</xsl:if>										
													</fo:block>
													<!-- paragraph_4_1 display  -->
													
													<fo:block>
																							
														<xsl:if test="$paragraph_4_var='Y'"> 
															<xsl:if test="$paragraph_1_2_var='Y' or $paragraph_1_1_var='Y' or $paragraph_1_3_var='Y' or $paragraph_2_1_var='Y' or $paragraph_3_1_var='Y' or $paragraph_3_2_var='Y'"> 
															    <fo:block xsl:use-attribute-sets="empty-style">
												   					 empty
														        </fo:block>
														     </xsl:if>
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='yourContributions_4'">
																	<xsl:value-of select="*|text()"/>
																</xsl:if>
															</xsl:for-each>
														</xsl:if>
													</fo:block>
												</xsl:if>
											</fo:block>
											<fo:block xsl:use-attribute-sets="section-space-style">
										   		empty
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
							</xsl:if> <!-- Your Contribution Unspecified check ends -->
							<!--  Your Employer Contribution MT not specified And Rules are not specified skip this section -->
							 <xsl:variable name="isEmployerMoneyTypeSpecified_var">
								<xsl:value-of select="yourERContributionsSection/isEmployerMoneyTypeSpecified"/>
							</xsl:variable>
							<xsl:variable name="isContributionRulesAreSpecified_var">
								<xsl:value-of select="yourERContributionsSection/isContributionRulesAreSpecified"/>
							</xsl:variable>
							<xsl:variable name="isContributionRuleNotSpecifiedForAnyMoneyType_var">
								<xsl:value-of select="yourERContributionsSection/isContributionRuleNotSpecifiedForAnyMoneyType"/>
							</xsl:variable>
							<xsl:variable name="isContributionRuleNotSpecifiedForAllAnyMoneyType_var">
								<xsl:value-of select="yourERContributionsSection/isContributionRuleNotSpecifiedForAllAnyMoneyType"/>
							</xsl:variable>
							
							<xsl:if test="$isEmployerMoneyTypeSpecified_var!='N'">
									<fo:table-row>
										<fo:table-cell padding="2pt">
											<fo:block xsl:use-attribute-sets="section-title-style">
												<xsl:value-of select="pageSectionText/yourERContributions" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="2pt">
												<fo:block>
												<xsl:if test="$isEmployerMoneyTypeSpecified_var='Y' and $isContributionRulesAreSpecified_var='N' and $isContributionRuleNotSpecifiedForAnyMoneyType_var='N' and $isContributionRuleNotSpecifiedForAllAnyMoneyType_var='N'">
													<fo:block>
														<xsl:for-each select="messages/message">
															<xsl:if test="@id='yourERContributionsNotSpecifiedAllMoneyType'">
																<xsl:value-of select="*|text()"/>
															</xsl:if>
														</xsl:for-each>
													</fo:block>
													<fo:block xsl:use-attribute-sets="section-space-style">
												    	empty
													</fo:block>	
													<fo:block xsl:use-attribute-sets="section-space-style">
												    	empty
													</fo:block>	
												</xsl:if>
												<xsl:if test="$isEmployerMoneyTypeSpecified_var='Y' and $isContributionRuleNotSpecifiedForAllAnyMoneyType_var='Y'">
													<fo:block>
														<xsl:for-each select="messages/message">
															<xsl:if test="@id='yourERContributionsNotSpecifiedAllMoneyType'">
																<xsl:value-of select="*|text()"/>
															</xsl:if>
														</xsl:for-each>
													</fo:block>
													<fo:block xsl:use-attribute-sets="section-space-style">
												    	empty
													</fo:block>	
													<fo:block xsl:use-attribute-sets="section-space-style">
												    	empty
													</fo:block>	
												</xsl:if>
												<!-- Table for MoneyType Employer Contribution -->
												<xsl:if test="$isEmployerMoneyTypeSpecified_var='Y' and $isContributionRulesAreSpecified_var='Y'">
												
													<xsl:variable name="yourERContributionTableLead_var">
															<xsl:value-of select="tableHeaderSection/yourERContributionTableLead" /> <!-- 5.1.1 -->
													</xsl:variable>
													<xsl:if test="$yourERContributionTableLead_var!=''">
														<fo:block>
															<xsl:value-of select="tableHeaderSection/yourERContributionTableLead" /> <!-- 5.1.1 -->
														</fo:block>
													</xsl:if>
													<fo:table border-collapse="collapse" space-before="2mm"	xsl:use-attribute-sets="table-border">
															<fo:table-column column-width="5.70cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="8.75cm"	/>
				
														    <fo:table-header>
															<fo:table-row>
																	<fo:table-cell xsl:use-attribute-sets="table-cell-style" display-align="center">
																		<fo:block xsl:use-attribute-sets="table-header-style">
																			<xsl:value-of select="tableHeaderSection/moneyType" />
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell xsl:use-attribute-sets="table-cell-style" >
																		<fo:block xsl:use-attribute-sets="table-header-style">
																			<xsl:value-of select="tableHeaderSection/yourERContribution" />
																		</fo:block>
																	</fo:table-cell>
															</fo:table-row>
															</fo:table-header>
															<fo:table-body>
															<!-- For loop -->
															<xsl:for-each select="yourERContributionsSection/employerContributions">
																<fo:table-row >
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style-MT"  >
																			<fo:block>
																				<xsl:value-of select="./moneyType/text()" /> 
																			</fo:block>
																		</fo:table-cell>
										
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style" >
																			<fo:block>
																					<xsl:value-of select="./contributionDesc/text()" />
																			</fo:block>
																		</fo:table-cell>
										  						</fo:table-row>
															</xsl:for-each>
															<!-- for loop ends -->
															
				       									</fo:table-body>
				     								</fo:table>
				     								
				     								<xsl:if test="$isEmployerMoneyTypeSpecified_var='Y' and $isContributionRuleNotSpecifiedForAnyMoneyType_var='Y' and $isContributionRuleNotSpecifiedForAllAnyMoneyType_var='N'">
				     									<fo:block xsl:use-attribute-sets="empty-style" keep-with-next.within-page="always" >
											   				empty
														</fo:block>	
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='yourERContributionsNotSpecifiedAnyMoneyType'">
																<fo:block keep-with-next.within-page="always">
																	<xsl:value-of select="*|text()"/>
																</fo:block>
																</xsl:if>
															</xsl:for-each>
													</xsl:if>
													<fo:block xsl:use-attribute-sets="section-space-style">
											   				empty
													</fo:block>	
												</xsl:if>
													<!-- ****************table else ends -->
												
											</fo:block>
											
										</fo:table-cell>
									</fo:table-row>
								</xsl:if> <!-- Your Employer Contribution MT not specified and Rules are not specified IF ENDS-->
							<fo:table-row>
								<fo:table-cell padding="2pt">
									<fo:block xsl:use-attribute-sets="section-title-style">
										<xsl:value-of select="pageSectionText/vesting" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="2pt">
									<fo:block>
										<xsl:variable name="vestingSectionSpecified_var">
											<xsl:value-of select="vestingSection/vestingSectionSpecified"/>
										</xsl:variable>
										<xsl:variable name="twoOrMoreVestingSchedulesForAnySingleMT_var">
											<xsl:value-of select="vestingSection/twoOrMoreVestingSchedulesForAnySingleMT"/>
										</xsl:variable>
										<xsl:variable name="vestingParagraph_1_1_var">
											<xsl:value-of select="vestingSection/vestingParagraph_1_1"/>
										</xsl:variable>
										<xsl:variable name="vestingParagraph_1_2_var">
											<xsl:value-of select="vestingSection/vestingParagraph_1_2"/>
										</xsl:variable>
										<xsl:variable name="vestingParagraph_1_3_var">
											<xsl:value-of select="vestingSection/vestingParagraph_1_3"/>
										</xsl:variable>
										<xsl:variable name="vestingParagraph_1_4_var">
											<xsl:value-of select="vestingSection/vestingParagraph_1_4"/>
										</xsl:variable>
										<xsl:variable name="vestingParagraph_2_var">
											<xsl:value-of select="vestingSection/vestingParagraph_2"/>
										</xsl:variable>
										<xsl:variable name="isAllYearsOfServiceSpecified100Percent_var">
											<xsl:value-of select="vestingSection/isAllYearsOfServiceSpecified100Percent"/>
										</xsl:variable>
										<xsl:variable name="showZeroYr_var">
											<xsl:value-of select="vestingSection/showZeroYr"/>
										</xsl:variable>
										
										
										<xsl:variable name="isEmployerVestingSchedulesIdentical_var">
											<xsl:value-of select="vestingSection/isEmployerVestingSchedulesIdentical"/>
										</xsl:variable>
										
										
												<xsl:if test="$twoOrMoreVestingSchedulesForAnySingleMT_var='Y'">
													<xsl:for-each select="messages/message">
															<xsl:if test="@id='vestingSection_1_1'">
																<xsl:value-of select="*|text()"/>
															</xsl:if>
													</xsl:for-each>
																							
												</xsl:if>
												
										<xsl:if test="$twoOrMoreVestingSchedulesForAnySingleMT_var='N'">
										
												<!-- <xsl:if test="$vestingSectionSpecified_var='N'">
													<xsl:for-each select="messages/message">
															<xsl:if test="@id='vestingSection_1_1'">
																<xsl:value-of select="*|text()"/>
															</xsl:if>
													</xsl:for-each>
														<fo:block xsl:use-attribute-sets="empty-style">
													   		empty
														</fo:block>										
												</xsl:if> -->
												<xsl:if test="$vestingParagraph_1_1_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='vestingSection_1_1'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
													<fo:block xsl:use-attribute-sets="empty-style">
													   		empty
													</fo:block>									
												</xsl:if>
									
												<fo:block>
												<xsl:if test="$vestingParagraph_1_2_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='vestingSection_1_2'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
													<fo:block xsl:use-attribute-sets="empty-style">
													   		empty
													</fo:block>																								
												</xsl:if>
												</fo:block>
												
												<fo:block>
												<xsl:if test="$vestingParagraph_1_3_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='vestingSection_1_3'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
																								
												</xsl:if>
												</fo:block>
												
												<fo:block>
												<xsl:if test="$vestingParagraph_1_4_var='Y' or $isAllYearsOfServiceSpecified100Percent_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='vestingSection_1_4'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
																						
												</xsl:if>
												</fo:block>
												<!-- <xsl:if test="$isAllYearsOfServiceSpecified100Percent_var='Y'">
														<xsl:value-of select="vestingSection/allYearsOfServiceSpecified100PercentMsg" />
															<fo:block xsl:use-attribute-sets="empty-style">empty</fo:block>	
												</xsl:if> -->
										
									<xsl:if test="$vestingSectionSpecified_var='Y'">
												
												<xsl:if test="$isEmployerVestingSchedulesIdentical_var='Y' and $isAllYearsOfServiceSpecified100Percent_var='N'">
													<fo:block>
														<xsl:value-of select="tableHeaderSection/vestingOneScheduleTableLead" />
													</fo:block>
													<fo:table border-collapse="collapse" space-before="2mm"	xsl:use-attribute-sets="table-no-border">
															<fo:table-column column-width="3cm"	xsl:use-attribute-sets="table-no-border" />
															<fo:table-column column-width="3cm"	xsl:use-attribute-sets="table-no-border" />
														
														<fo:table-body>
															<fo:table-row>
																	<fo:table-cell>
																		<fo:block text-align="center" text-decoration="underline">
																			 <xsl:value-of select="tableHeaderSection/yearOfServiceForOneSchedule" />
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																		<fo:block text-align="center" text-decoration="underline">
																			<xsl:value-of select="tableHeaderSection/percentVestedForOneSchedule" />
																		</fo:block>
																	</fo:table-cell>
															</fo:table-row>
															
															<xsl:for-each select="vestingSection/employerContributionsIdentical">
																<fo:table-row>
																		<fo:table-cell padding-start="1.25cm" text-align="left">
																			<fo:block>
																				<xsl:value-of select="./yearOfService/text()" /> 
																			</fo:block>
																		</fo:table-cell>										
																		<fo:table-cell text-align="center">
																			<fo:block>
																				<xsl:value-of select="./percentVested/text()" />
																			</fo:block>
																		</fo:table-cell>
										  						</fo:table-row>
															</xsl:for-each>
														</fo:table-body>
				     								</fo:table>		
													
												
												</xsl:if>
												
												<xsl:if test="$isEmployerVestingSchedulesIdentical_var='N' and $isAllYearsOfServiceSpecified100Percent_var='N'">
													<fo:block keep-with-next.within-page="always">
														<xsl:value-of select="tableHeaderSection/vestingMultipleScheduleTableLead" />
													</fo:block>												
													<fo:table border-collapse="collapse" space-before="2mm"	xsl:use-attribute-sets="table-border">
															<fo:table-column column-width="5.70cm"	xsl:use-attribute-sets="table-border" />
															<xsl:if test="$showZeroYr_var='Y'">
																<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															</xsl:if>
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />
															<fo:table-column column-width="1.25cm"	xsl:use-attribute-sets="table-border" />												
				
														 <fo:table-header>
															<fo:table-row keep-with-next="always">
																	<fo:table-cell xsl:use-attribute-sets="table-cell-style" number-rows-spanned="2" display-align="center" >
																		<fo:block xsl:use-attribute-sets="table-header-style">
																			<xsl:value-of select="tableHeaderSection/moneyType" />
																		</fo:block>
																	</fo:table-cell>
																	<xsl:if test="$showZeroYr_var='Y'">
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" display-align="center" number-columns-spanned="7">
																			<fo:block xsl:use-attribute-sets="table-header-style" >
																				<xsl:value-of select="tableHeaderSection/percentVestedForMultipleSchedule" />
																			</fo:block>
																		</fo:table-cell>
																	</xsl:if>
																	<xsl:if test="$showZeroYr_var='N'">
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" display-align="center" number-columns-spanned="6">
																			<fo:block xsl:use-attribute-sets="table-header-style" >
																				<xsl:value-of select="tableHeaderSection/percentVestedForMultipleSchedule" />
																			</fo:block>
																		</fo:table-cell>
																	</xsl:if>
															</fo:table-row>
															<fo:table-row keep-with-next="always">
															<xsl:if test="$showZeroYr_var='Y'">
																<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																			<fo:block xsl:use-attribute-sets="table-header-style">
																				<fo:inline font-weight="bold">0</fo:inline>
																			</fo:block>
																</fo:table-cell>
															</xsl:if>
															<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																		<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">1</fo:inline>
																		</fo:block>
															</fo:table-cell>
															<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																		<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">2</fo:inline>
																		</fo:block>
															</fo:table-cell>
															<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																		<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">3</fo:inline>
																		</fo:block>
															</fo:table-cell>
															<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																		<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">4</fo:inline>
																		</fo:block>
															</fo:table-cell>
															<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																		<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">5</fo:inline>
																		</fo:block>
															</fo:table-cell>
															<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																		<fo:block xsl:use-attribute-sets="table-header-style">
																			<fo:inline font-weight="bold">6+</fo:inline>
																		</fo:block>
															</fo:table-cell>
															
															</fo:table-row>										
														</fo:table-header>	
														<fo:table-body>
															<xsl:for-each select="vestingSection/employerContributions">
																<fo:table-row keep-together.within-page="always">
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style-MT" display-align="center">
																			<fo:block>
																				<xsl:value-of select="./moneyType/text()" /> 
																			</fo:block>
																		</fo:table-cell>										
																		<xsl:if test="$showZeroYr_var='Y'">
																			<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																				<fo:block>
																					<xsl:value-of select="./percentVested0/text()" />
																				</fo:block>
																			</fo:table-cell>
																		</xsl:if>
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																			<fo:block>
																				<xsl:value-of select="./percentVested1/text()" />
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																			<fo:block>
																				<xsl:value-of select="./percentVested2/text()" />
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																			<fo:block>
																				<xsl:value-of select="./percentVested3/text()" />
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																			<fo:block>
																				<xsl:value-of select="./percentVested4/text()" />
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																			<fo:block>
																				<xsl:value-of select="./percentVested5/text()" />
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell xsl:use-attribute-sets="table-cell-style" text-align="center" >
																			<fo:block>
																				<xsl:value-of select="./percentVested6/text()" />
																			</fo:block>
																		</fo:table-cell>
										  						</fo:table-row>
															</xsl:for-each>
														</fo:table-body>
				     								</fo:table>				
													<fo:block xsl:use-attribute-sets="empty-style">
										   				empty
													</fo:block>							
												</xsl:if>								
										</xsl:if>
										
												<fo:block>
												<xsl:if test="$vestingParagraph_2_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='vestingSection_2'">
															<xsl:value-of select="*|text()"/>
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												</fo:block>
									</xsl:if> <!--  end if  twoOrMoreVestingSchedulesForAnySingleMT_Var checking -->		
									</fo:block>
									<fo:block xsl:use-attribute-sets="section-space-style">
											empty
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell padding="2pt">
									<fo:block xsl:use-attribute-sets="section-title-style">
										<xsl:value-of select="pageSectionText/loans" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="2pt">
									<fo:block>
									
										<xsl:variable name="loanSpecified_var">
											<xsl:value-of select="loansSection/loanSpecified"/>
										</xsl:variable>
										
										<xsl:if test="$loanSpecified_var='N'">
														<xsl:for-each select="messages/message">
															<xsl:if test="@id='loansSentence_0'">
																	<xsl:value-of select="*|text()"/>&#160;
															</xsl:if>
														</xsl:for-each>
										</xsl:if>
										<xsl:if test="$loanSpecified_var='U'">
															<xsl:for-each select="messages/message">
																<xsl:if test="@id='loansSentence_0'">
																		<xsl:value-of select="*|text()"/>&#160;
																</xsl:if>
															</xsl:for-each>
										</xsl:if>
									
									<xsl:if test="$loanSpecified_var='Y'">
									
												<xsl:variable name="loansSentence_1_var">
													<xsl:value-of select="loansSection/loansSentence_1"/>
												</xsl:variable>
												<xsl:if test="$loansSentence_1_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='loansSentence_1'">
																<xsl:value-of select="*|text()"/>&#160;
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
												<xsl:variable name="loansSentence_2_var">
													<xsl:value-of select="loansSection/loansSentence_2"/>
												</xsl:variable>
												<xsl:if test="$loansSentence_2_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='loansSentence_2'">
																<xsl:value-of select="*|text()"/>&#160;
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
												<xsl:variable name="loansSentence_3_var">
													<xsl:value-of select="loansSection/loansSentence_3"/>
												</xsl:variable>
												<xsl:if test="$loansSentence_3_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='loansSentence_3'">
																<xsl:value-of select="*|text()"/>&#160;
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
												<xsl:variable name="loansSentence_4_var">
													<xsl:value-of select="loansSection/loansSentence_4"/>
												</xsl:variable>
												<xsl:if test="$loansSentence_4_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='loansSentence_4'">
																<xsl:value-of select="*|text()"/>&#160;
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
												<xsl:variable name="loansSentence_5_var">
													<xsl:value-of select="loansSection/loansSentence_5"/>
												</xsl:variable>
												<xsl:if test="$loansSentence_5_var='Y'">
													<xsl:for-each select="messages/message">
														<xsl:if test="@id='loansSentence_5'">
																<xsl:value-of select="*|text()"/>&#160;
														</xsl:if>
													</xsl:for-each>
												</xsl:if>
												
										</xsl:if> <!-- loanSpecified - Y if ENDs -->
									</fo:block>
									<fo:block xsl:use-attribute-sets="section-space-style">
											empty
									</fo:block>	
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row keep-together.within-page="always" >
								<fo:table-cell padding="2pt">
									<fo:block xsl:use-attribute-sets="section-title-style">
										<xsl:value-of select="pageSectionText/withdrawals" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="2pt">
									<fo:block>
										<xsl:variable name="withdrawalSentence_1_1_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_1_1"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_1_1_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_1_1'">
													<xsl:value-of select="*|text()"/>&#160;
												</xsl:if>
											</xsl:for-each>
										</xsl:if>

										<xsl:variable name="withdrawalSentence_1_2_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_1_2"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_1_2_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_1_2'">
													<xsl:value-of select="*|text()"/>&#160;
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									
										<xsl:variable name="withdrawalSentence_1_3_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_1_3"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_1_3_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_1_3'">
													<xsl:value-of select="*|text()"/>
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									</fo:block>
									<!-- 2nd paragraph -->
									<fo:block xsl:use-attribute-sets="empty-style">
								   				empty
									</fo:block>									
									<fo:block>
										<xsl:variable name="withdrawalSentence_2_1_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_2_1"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_2_1_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_2_1'">
													<xsl:value-of select="*|text()"/>&#160;
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									
										<xsl:variable name="withdrawalSentence_2_2_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_2_2"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_2_2_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_2_2'">
													<xsl:value-of select="*|text()"/>&#160;
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
								
										<xsl:variable name="withdrawalSentence_2_3_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_2_3"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_2_3_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_2_3'">
													<xsl:value-of select="*|text()"/>
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="empty-style">
								   				empty
									</fo:block>									
									
									<fo:block xsl:use-attribute-sets="block-style-small-font">
										<xsl:variable name="withdrawalSentence_3_1_var">
											<xsl:value-of select="withdrawalSection/withdrawalSentence_3_1"/>
										</xsl:variable>
										
										<xsl:if test="$withdrawalSentence_3_1_var='Y'">
											<xsl:for-each select="messages/message">
												<xsl:if test="@id='withdrawalSentence_3_1'">
													<xsl:value-of select="*|text()"/>
												</xsl:if>
											</xsl:for-each>
											<fo:block xsl:use-attribute-sets="section-space-style">
										   		empty
											</fo:block>											
										</xsl:if>
									</fo:block>
									
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row keep-together.within-page="always" >
								<fo:table-cell padding="2pt">
									<fo:block xsl:use-attribute-sets="section-title-style">
										<xsl:value-of select="pageSectionText/investmentOptions" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="2pt"> 
									<fo:block>
										<xsl:for-each select="messages/message">
												<xsl:if test="@id='investmentParagraph'">
													<xsl:value-of select="*|text()"/>
												</xsl:if>
											</xsl:for-each>
									</fo:block>
									<fo:block>
										<xsl:for-each select="messages/message">
												<xsl:if test="@id='investmentParagraph_2'">
													<xsl:value-of select="*|text()"/>
												</xsl:if>
											</xsl:for-each>
									</fo:block>
									<fo:block xsl:use-attribute-sets="section-space-style">
										   		empty
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row keep-together.within-page="always" >
								<fo:table-cell padding="2pt">
									<fo:block xsl:use-attribute-sets="section-title-style">
										<xsl:value-of select="pageSectionText/reportingAndChanges" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding="2pt">
									<fo:block>
											<xsl:for-each select="messages/message">
													<xsl:if test="@id='reportingParagraph_1'">
														<xsl:value-of select="*|text()"/>
													</xsl:if>
											</xsl:for-each>
											<xsl:for-each select="messages/message">
													<xsl:if test="@id='reportingParagraph_2'">
														<xsl:value-of select="*|text()"/>
													</xsl:if>
											</xsl:for-each>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block id="endofdoc"></fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	
</xsl:stylesheet>