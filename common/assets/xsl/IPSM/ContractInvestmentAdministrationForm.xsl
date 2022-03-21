<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template name="ContractInvestmentAdministrationFormTemplate">
        <fo:page-sequence master-reference="ContractInvestmentAdministrationFormLayout" initial-page-number="2" force-page-count="no-force"> 
        	<!-- Footer -->
			<fo:static-content flow-name="xsl-region-after">
                <fo:table table-layout="fixed" width="100%">
	                    <fo:table-body>
	                        <fo:table-row border-bottom-style="solid" border-bottom-width="0.5px">
	                            <fo:table-cell padding-after="2px" width="300px">
	                                <fo:block xsl:use-attribute-sets="interim_docket_section_default_font">
	                                	<xsl:choose>
											<xsl:when test="/annual_review_report/companyId = '019'">
												GP5632US (03/2017) IPSM
											</xsl:when>
											<xsl:when test="/annual_review_report/companyId = '094'">
												GP5632NY (03/2017) IPSM
											</xsl:when>
										</xsl:choose>
	                                </fo:block>
	                            </fo:table-cell>
	                            <fo:table-cell>
	                                <fo:block xsl:use-attribute-sets="interim_docket_section_default_font" text-align="right">
	                                    1
	                                </fo:block>
	                            </fo:table-cell>
	                        </fo:table-row>
	                        <fo:table-row>
	                            <fo:table-cell>
	                                <fo:block xsl:use-attribute-sets="interim_footer_default_font">
										<xsl:choose>
											<xsl:when test="/annual_review_report/companyId = '019'">
												© 2017 John Hancock Life Insurance Company (U.S.A.) (John Hancock USA). All rights reserved.
											</xsl:when>
											<xsl:otherwise>
												© 2017 John Hancock Life Insurance Company of New York, Valhalla, NY. All rights reserved.
											</xsl:otherwise>
										</xsl:choose>	                                   
	                                </fo:block>
	                            </fo:table-cell>
	                        </fo:table-row>
	                    </fo:table-body>
                	</fo:table>
            </fo:static-content>
            
            <fo:flow flow-name="xsl-region-body">            
	            <fo:block>
	            	<fo:table table-layout="fixed" width="100%">
	                    <fo:table-body>
	                        <fo:table-row>
	                            <fo:table-cell width="200px">
	                                <fo:block>
	                                    <fo:external-graphic content-width="135px" content-height="45px">
	                                        <xsl:attribute name="src">
	                                            <xsl:value-of select="concat($imagePath,'JH_SIG_black_300dpi.jpg')"/>
	                                        </xsl:attribute>
	                                    </fo:external-graphic>
	                                </fo:block>
	                            </fo:table-cell>
	                            <fo:table-cell width="350px">
	                        		<fo:block>
			                        	<fo:table table-layout="fixed" width="100%">
					                   		<fo:table-body>												
						                     	<fo:table-row>
						                     		<fo:table-cell>
														<fo:block-container xsl:use-attribute-sets="interim_header_title_font">						
															<xsl:if test="/annual_review_report/companyId = '094'">
																<fo:block font-size="11pt">
																   Accumulated Retirement Account (ARA) Group Annuity
															   </fo:block>	
															   <fo:block>
																	IPS Manager Interim Review
															   </fo:block>															   
															</xsl:if>
															
															<xsl:choose>
																<xsl:when test="/annual_review_report/companyId = '019'">
																	<fo:block>
																		IPS Manager Interim Review
															   		</fo:block>	
																	<fo:block>
																		Contract Investment Administration Form
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:block >
																	   Contract Investment Administration
																   </fo:block>
																</xsl:otherwise>
															</xsl:choose>
															<xsl:if test="/annual_review_report/companyId = '094'">
																<fo:block font-size="11pt" >
																  John Hancock Life Insurance Company of New York
															   </fo:block>																   
															</xsl:if>
														</fo:block-container>
														<xsl:if test="/annual_review_report/companyId = '094'">
															<fo:block xsl:use-attribute-sets="interim_sub_header_font" font-size="9pt">
															   (hereinafter referred to as John Hancock New York or The Company)
														   </fo:block>
													   </xsl:if>
						                         	</fo:table-cell>
					                         	</fo:table-row>					                         	
					                        </fo:table-body>
					                      </fo:table>
				                      </fo:block>
			                      </fo:table-cell>	                            
	                        </fo:table-row>
	                	</fo:table-body>
	            	</fo:table>
	            	
	            	<fo:table table-layout="fixed" width="100%" space-before="10px">
                   		<fo:table-body>
	                     	<fo:table-row>
	                     		<fo:table-cell>
		                           <fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
		                               Fax the completed form to our toll free number 1-866-377-9577
		                           </fo:block>
	                         	</fo:table-cell>
                         	</fo:table-row>
                        </fo:table-body>
                      </fo:table>
                      <fo:table table-layout="fixed" width="100%">
                   		<fo:table-body>
                   			<fo:table-row>                         		
                     			<fo:table-cell>
                           			<fo:block border-width="2px" xsl:use-attribute-sets="line_border_style">                               			
                           			</fo:block>
                         		</fo:table-cell>                         		
                         	</fo:table-row>
                   		</fo:table-body>
                      </fo:table>	            	 
                   	  <fo:table table-layout="fixed" width="100%">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell width="5px">
                   					<fo:block xsl:use-attribute-sets="interim_title_font"> 
                   						1
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                           			<fo:block start-indent="6px" padding-top="3px" xsl:use-attribute-sets="interim_sub_title_font"> 
                   						General Information
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
                   	  <fo:table table-layout="fixed" width="100%">
                   		<fo:table-body>
                   			<fo:table-row height="10px" keep-with-previous="always">
	                     		<fo:table-cell width="430px" padding-start="1px" padding-right="8px" padding-top="9px" xsl:use-attribute-sets="line_border_style">
	                                <fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
	                                    <xsl:value-of select="$companyName"/> (the “Plan”)
	                                </fo:block>
	                            </fo:table-cell>
	                            
								<fo:table-cell width="12px" padding-start="1px">
	                                <fo:block space-after="2px" xsl:use-attribute-sets="interim_word_group_default_font">
	                                    
	                                </fo:block>	                                
	                            </fo:table-cell>
	                            <fo:table-cell width="100px" padding-start="1px" padding-top="9px" xsl:use-attribute-sets="line_border_style">
	                                <fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
	                                    <xsl:value-of select="$contractNumber"/>
	                                </fo:block>	                                
	                            </fo:table-cell>
	                     	</fo:table-row>
	                     	<fo:table-row height="10px" keep-with-previous="always">
	                     		<fo:table-cell padding-start="1px" padding-right="8px">
	                                <fo:block space-after="2px" xsl:use-attribute-sets="interim_footer_default_font">
	                                    Contractholder Name
	                                </fo:block>
	                                
	                            </fo:table-cell>
								
	                            <fo:table-cell padding-start="1px" padding-right="8px">
	                                <fo:block space-after="2px" xsl:use-attribute-sets="interim_word_group_default_font">
	                                    
	                                </fo:block>
	                            </fo:table-cell>
	                            <fo:table-cell padding-start="1px">
	                                <fo:block space-after="2px" xsl:use-attribute-sets="interim_footer_default_font">
	                                    Contract Number
	                                </fo:block>
	                                
	                            </fo:table-cell>
	                     	</fo:table-row>
	                     </fo:table-body>
	                  </fo:table>
					  <fo:table table-layout="fixed" width="100%" space-before="5px">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold"> 
                   						<fo:inline font-weight="normal">This request shall be effective as of the </fo:inline><fo:inline  font-style="italic">later</fo:inline><fo:inline font-weight="normal"> of (i) the effective date noted below or (ii) the day that this form, duly completed and in good order, is received by 
                   						<xsl:choose>
		<xsl:when test="/annual_review_report/companyId = '019'">John Hancock Life Insurance Company (U.S.A.) ("John Hancock USA")</xsl:when>
		<xsl:otherwise>John Hancock Life Insurance Company of New York ("John Hancock New York")</xsl:otherwise></xsl:choose> as per our current </fo:inline>Administrative Guidelines<fo:inline font-weight="normal">.</fo:inline>
                           			</fo:block>
                   				</fo:table-cell>                   				
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
					  <fo:table table-layout="fixed" width="100%" space-before="6px">
                   	  	
                   		<fo:table-body>
                   			<fo:table-row>
                   				<fo:table-cell width="60px">
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font">
                   						Effective Date
                   					</fo:block>
                   				</fo:table-cell>                   				
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="left_line_border_style">
                   					<fo:block >
                   					</fo:block>
                   				</fo:table-cell> 
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="left_line_border_style">
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell> 
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="left_line_border_style">
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell> 
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" >
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="left_line_border_style">
                   					<fo:block>
                   					</fo:block>
                   				</fo:table-cell> 
                   			</fo:table-row>
							<fo:table-row>
                   				<fo:table-cell width="60px">
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font">
                   						
                   					</fo:block>
                   				</fo:table-cell>                   				
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="line_border_style left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>
								<fo:table-cell width="18px" xsl:use-attribute-sets="left_line_border_style">
                   					<fo:block padding-top="2px" >
                   					</fo:block>
                   				</fo:table-cell>                     				
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
					  <fo:table table-layout="fixed" width="100%">
						<fo:table-body>
							<fo:table-row>
                   				<fo:table-cell width="80px">
                   					<fo:block start-indent="75px" xsl:use-attribute-sets="interim_footer_default_font"> 
                   						Month
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="70px">
                   					<fo:block start-indent="48px" xsl:use-attribute-sets="interim_footer_default_font"> 
                   						Day
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="60px">
                   					<fo:block start-indent="30px" xsl:use-attribute-sets="interim_footer_default_font"> 
                   						Year
                           			</fo:block>
                   				</fo:table-cell>
                   			</fo:table-row>
						</fo:table-body>
					  </fo:table>
	                  <fo:table table-layout="fixed" width="100%" space-before="2px">
                   		<fo:table-body>
                   			<fo:table-row>                         		
                     			<fo:table-cell>
                           			<fo:block border-width="2px" xsl:use-attribute-sets="line_border_style">                               			
                           			</fo:block>
                         		</fo:table-cell>                         		
                         	</fo:table-row>
                   		</fo:table-body>
                      </fo:table>					  
	                  <fo:table table-layout="fixed" width="100%">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell width="5px">
                   					<fo:block xsl:use-attribute-sets="interim_title_font"> 
                   						2
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                           			<fo:block start-indent="6px" padding-top="3px" xsl:use-attribute-sets="interim_sub_title_font"> 
                   						Complete this section if you wish to replace investment Funds currently being offered to participants with the Highest Ranking Fund in the same asset class, based on your selected weighted criteria.  Use the Contract Investment Administration Form for other changes.
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table> 	  
                   	  	
                   	  
                   	<xsl:if test="annual_review_report/adminFormFundInstInfo/admin_form_fund_instruction_info">
                   	  <fo:table table-layout="fixed" width="100%" space-before="5px">
                   	  	
                   		<fo:table-header>
                   			<fo:table-row height="20px" display-align="center">
								<fo:table-cell width="102px" padding-start="1px" padding-right="4px" xsl:use-attribute-sets="fund_instruction_box_border_header_style">
                   					<fo:block start-indent="10px" padding-top="2px" text-align="center" xsl:use-attribute-sets="interim_docket_section_default_font" font-weight="bold">
                   						Asset Class
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="160px" padding-start="1px" padding-right="4px" xsl:use-attribute-sets="fund_instruction_box_border_header_style">
                   					<fo:block start-indent="10px" padding-top="2px" text-align="center" xsl:use-attribute-sets="interim_docket_section_default_font" font-weight="bold">
                   						Current Fund
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="160px" padding-start="1px" padding-right="4px" xsl:use-attribute-sets="fund_instruction_box_border_header_style">
                   					<fo:block start-indent="10px" padding-top="2px" text-align="center" xsl:use-attribute-sets="interim_docket_section_default_font" font-weight="bold">
                   						Highest-Ranking Fund in the same asset class
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="120px" padding-top="2px" padding-start="1px" padding-right="8px" xsl:use-attribute-sets="fund_instruction_box_border_header_style" padding-bottom="5px">
                   					<fo:block start-indent="5px" text-align="center" xsl:use-attribute-sets="interim_docket_section_default_font" font-weight="bold">
                   						Replace Current Fund with the Highest-Ranking Fund in the same asset class?
                   					</fo:block>
                   				</fo:table-cell>
                   			</fo:table-row>
                   		</fo:table-header>
                   		<fo:table-body>
	                		<xsl:for-each select="annual_review_report/adminFormFundInstInfo/admin_form_fund_instruction_info">
	                			<xsl:variable name="topRankFundCount">
									<xsl:value-of select="."/>
								</xsl:variable>
								
								<fo:table-row display-align="center" height="15px"> 
									<fo:table-cell>
									<fo:block>
										<fo:table table-layout="fixed" width="100%">
											<fo:table-column column-width="102px" />
											<fo:table-column column-width="30px" />
											<fo:table-column column-width="130px" />
											<fo:table-column column-width="30px" />
											<fo:table-column column-width="130px" />
											<fo:table-column column-width="120px" />
											<fo:table-body>
												<fo:table-row display-align="center">
													<fo:table-cell xsl:use-attribute-sets="fund_instruction_box_border_style" >
														<fo:block space-before="10px">
																																
														</fo:block>
														<fo:block text-align="left" xsl:use-attribute-sets="interim_word_group_default_font" start-indent="3px">
														<xsl:if test="assetClassName">
															<xsl:value-of select="assetClassName" />
															 <xsl:if test="isFromFundBlank = 'true'">
													 	     <fo:inline vertical-align="super" font-size="5.5pt">
                                                                       3
                                                              </fo:inline>
													 	    </xsl:if>
													 	</xsl:if>
													 	</fo:block>
													 	
													</fo:table-cell>
													<fo:table-cell xsl:use-attribute-sets="fund_instruction_box_border_style" >
													     <xsl:if test="isFromFundBlank = 'true'">
													    <xsl:attribute name="number-columns-spanned">2</xsl:attribute>
													    </xsl:if>
														<fo:block text-align="left">
															<xsl:for-each select="currentFundInfo/current_fund_info" >
																<fo:block space-before="10px">
																																
																</fo:block>
																<fo:block xsl:use-attribute-sets="interim_word_group_default_font" start-indent="3px">
																	<xsl:value-of select="fundCode" /> 
																</fo:block>
															</xsl:for-each>
													 	</fo:block>
													</fo:table-cell>
													<xsl:if test="currentFundInfo/current_fund_info and isFromFundBlank = 'false'">
													<fo:table-cell xsl:use-attribute-sets="fund_instruction_box_border_style" >
														<fo:block text-align="left">
															<xsl:for-each select="currentFundInfo/current_fund_info">
																<fo:block space-before="10px">
																																
																</fo:block>
																<fo:block xsl:use-attribute-sets="interim_word_group_default_font" start-indent="3px" >
																	<xsl:value-of select="fundNameWithoutFundSymbol" /> 
																</fo:block>
															</xsl:for-each>
													 	</fo:block>
													</fo:table-cell>
													</xsl:if>
													<fo:table-cell xsl:use-attribute-sets="fund_instruction_box_border_style" >
														<fo:block text-align="left">
														<xsl:for-each select="topRankedFundInfo/top_rank_fund_info">
																<fo:block space-before="10px">
																																
																</fo:block>
															<fo:block xsl:use-attribute-sets="interim_word_group_default_font" start-indent="3px">
																<xsl:value-of select="fundCode" />																
															</fo:block>
														</xsl:for-each>
														</fo:block>
														</fo:table-cell>
														<fo:table-cell xsl:use-attribute-sets="fund_instruction_box_border_style" >
														<fo:block text-align="left">
														<xsl:for-each select="topRankedFundInfo/top_rank_fund_info">
																<fo:block space-before="10px">
																
																</fo:block>
															<fo:block xsl:use-attribute-sets="interim_word_group_default_font" start-indent="3px">
																<xsl:value-of select="fundNameWithoutFundSymbol" />
																<fo:inline vertical-align="super" font-size="5.5pt"><xsl:value-of select="code" /></fo:inline>
																 
															</fo:block>
														</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell xsl:use-attribute-sets="fund_instruction_box_border_style" >
													<fo:block>
														<xsl:for-each select="topRankedFundInfo/top_rank_fund_info">
																<fo:block space-before="10px">
																																
																</fo:block>
															<fo:table width="100%" >
																<fo:table-body>
																	<fo:table-row>
																		<fo:table-cell start-indent="25px" width="38px" padding-top="4px" padding-bottom="2px" >
																			<fo:block>
																				<fo:table table-layout="fixed" width="100%">
																					<fo:table-column column-width="10px"/>
																					<fo:table-body>
																					<fo:table-row height="8px">
																						<fo:table-cell border-width="1px" xsl:use-attribute-sets="fund_instruction_box_border_style top_line_border_style" >
																							<fo:block ></fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																					</fo:table-body>
																				</fo:table>
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell width="20px">
																			<fo:block xsl:use-attribute-sets="interim_word_group_default_font">
																			Yes
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell start-indent="8px" width="20px">
																		    <fo:block>
																				<fo:table table-layout="fixed" width="100%">
																					<fo:table-column column-width="10px"/>
																					<fo:table-body>
																					<fo:table-row height="8px">
																						<fo:table-cell border-width="1px" xsl:use-attribute-sets="fund_instruction_box_border_style top_line_border_style">
																							<fo:block ></fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																					</fo:table-body>
																				</fo:table>
																			</fo:block>
																		</fo:table-cell>	
																		<fo:table-cell start-indent="2px" width="20px">
																			<fo:block xsl:use-attribute-sets="interim_word_group_default_font">
																			No
																			</fo:block>
																		</fo:table-cell>
																	</fo:table-row>
																</fo:table-body>
															</fo:table>
														</xsl:for-each>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								
							</xsl:for-each>
                   		</fo:table-body>
                   	  </fo:table>
                   	  </xsl:if>
                   	  <fo:table table-layout="fixed" width="100%" space-before="2px">
                   		<fo:table-body>
                   			
	                   			<fo:table-row>
	                   				<fo:table-cell width="10px">
	                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font"> 
	                   						<xsl:if test="annual_review_report/redemptionfeeInd = 'true'">
	                   							<fo:inline vertical-align="super" font-size="5.5pt">1</fo:inline>
	                   						</xsl:if>
	                           			</fo:block>
	                           		</fo:table-cell>
									<fo:table-cell>
	                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font"> 
	                   						<xsl:if test="annual_review_report/redemptionfeeInd = 'true'">
	                   							A redemption fee may apply with respect to the transfer of assets from one or more of the eliminated Funds. Additionally, or one or more of the replacement funds may provide for a redemption fee. Visit the Plan Sponsor website for more information.
	                   						</xsl:if>
	                           			</fo:block>
	                           		<fo:block></fo:block>
	                           		</fo:table-cell>
	                   			</fo:table-row>
	                   			<fo:table-row>
	                   				<fo:table-cell width="10px" padding-before="3px">
	                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font">
	                   						<xsl:if test="annual_review_report/tiedTopRankedFundsAvailable = 'true'"> 
	                   							<fo:inline vertical-align="super" font-size="5.5pt">2</fo:inline>
	                   							</xsl:if>
	                           			</fo:block>
	                           			
	                           		</fo:table-cell>
									<fo:table-cell padding-before="3px">
	                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
	                   						<xsl:if test="annual_review_report/tiedTopRankedFundsAvailable = 'true'"> 
	                   							<fo:inline font-weight="normal"> Multiple Top Ranked Funds identified. </fo:inline>Note: Only one Highest-Ranking Fund may be selected for each “Current
Fund” on this form.<fo:inline font-weight="normal"> If you wish to add both Highest-Ranking Funds, the additional one may be submitted by completing the
separate Contract Investment Administration form. The Fund added using this latter form will not be subject to the IPS Manager review.
Checking both boxes on this form will result in the delay of the above request, as this form will not be considered in good order upon
receipt at <xsl:choose>
		<xsl:when test="/annual_review_report/companyId = '019'">John Hancock USA</xsl:when><xsl:otherwise>John Hancock New York</xsl:otherwise></xsl:choose>.</fo:inline>
	                   							</xsl:if>
	                           			</fo:block>
	                           			
	                           		</fo:table-cell>
	                   			</fo:table-row>		
	                   			<fo:table-row>
	                   				<fo:table-cell width="10px" padding-before="3px">
	                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font">
	                   					        <xsl:if test="annual_review_report/isFromFundBlank = 'true'"> 
	                   							<fo:inline vertical-align="super" font-size="5.5pt">3</fo:inline>
	                   							</xsl:if>
	                           			</fo:block>
	                           			
	                           		</fo:table-cell>
									<fo:table-cell padding-before="3px">
	                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold">
	                   							<xsl:if test="annual_review_report/isFromFundBlank = 'true'"> 
	                   							<fo:inline font-weight="normal"> By showing this blank asset class, John Hancock is not in any way providing impartial investment advice giving advice in a fiduciary capacity in connection with the use of this service and any transactions that may follow the use of this service.</fo:inline>
	                           			        </xsl:if>
	                           			</fo:block>
	                           			
	                           		</fo:table-cell>
	                   			</fo:table-row>									
                   		</fo:table-body>
                   	  </fo:table>
                   	  
                   	  <fo:table table-layout="fixed" width="100%" space-before="2px">
                   		<fo:table-body>
                   			<fo:table-row>                         		
                     			<fo:table-cell>
                           			<fo:block border-width="2px" xsl:use-attribute-sets="line_border_style">                               			
                           			</fo:block>
                         		</fo:table-cell>                         		
                         	</fo:table-row>
                   		</fo:table-body>
                      </fo:table>					  
	                  <fo:table table-layout="fixed" width="100%">
                   		<fo:table-body>
	                     	<fo:table-row>
                   				<fo:table-cell width="5px">
                   					<fo:block xsl:use-attribute-sets="interim_title_font"> 
                   						3
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                           			<fo:block start-indent="6px" padding-top="3px" xsl:use-attribute-sets="interim_sub_title_font"> 
                   						Authorization and Certifications
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>                   			
                   		</fo:table-body>
                   	  </fo:table>
                   	</fo:block>
					<fo:block>
					
					<fo:block keep-with-next.within-page="always" xsl:use-attribute-sets="interim_footer_default_font" space-before="2px" font-size="9pt">
						<xsl:choose>
							<xsl:when test="/annual_review_report/companyId = '019'">
								<fo:block>
									I, the undersigned hereby authorize John Hancock USA to implement the investment changes requested herein above. I understand and acknowledge that (i) other Funds are available under the Plan with risk and return characteristics similar to the Funds identified in the IPS Manager review report as having the highest ranking based on my selected IPS Manager weighted criteria, and where information regarding those other Funds is located; (ii) the identification of any Funds with the red, yellow or green indicator or as a Highest-Ranking Fund in the IPS Manager review report is based on the weighted criteria set by me or, if applicable, my designated 3(38) Investment Manager, and that such identification is provided for information only to assist me (or, if applicable my designated 3(38) Investment Manager) in monitoring the Funds offered under my Plan; (iii) investment changes not covered by this form may be requested by completing a separate Contract Investment Administration Form; and (iv) I (and, if applicable my designated 3(38) Investment Manager) are solely responsible for determining whether or when it is appropriate to remove, retain or replace a Fund available to my Plan.  I further acknowledge that, in providing the IPS Manager Service, John Hancock USA is not undertaking to provide impartial investment advice or to give advice in a fiduciary capacity to me or my Plan, and that this service is provided without regard to the individualized needs of my Plan, its participants or beneficiaries.
								</fo:block>
								<fo:block padding-before="3px">
									In my role as the authorized fiduciary of the Plan, I have determined that the Fund changes requested herein above are appropriate for my Plan. I further agree to assume responsibility for communicating the changes requested on this Form to the Plan participants and beneficiaries. In particular, I will inform Plan participants and beneficiaries of the changes applicable to their accounts, provide them with information regarding the eliminated Funds, the replacement Funds, as well as the redemption fee that may be applicable to one or more of such Funds. I will also direct them to the John Hancock USA website or to its toll-free participant phone number if they wish to make alternate selections to the Fund replacements requested herein, transfer monies from the eliminated Funds, and/or change their investment instructions for their ongoing contributions.
								</fo:block>
								<fo:block padding-before="3px"> 
									On behalf of the Plan sponsor, the Plan and its related trust, I hereby (i) represent that John Hancock USA is entitled to rely on the directions provided in this form, (ii) represent that I (and, if applicable my designated 3(38) Investment Manager) are independent of, and not affiliated or under common control with, John Hancock or any of its affiliates. and (iii) agree to indemnify and hold harmless John Hancock USA, its affiliates, and each of their agents, employees, officers and directors from and against any and all claims, suits, losses, damages, costs, charges, counsel fees, payments, expenses, and liability arising out of, or attributable to, John Hancock USA implementation of the directions provided in this form. I further acknowledge and agree that any participant queries received by John Hancock USA pertaining to the requested changes should be directed to the undersigned for clarification.
							    </fo:block>
							</xsl:when>
							<xsl:otherwise>
								<fo:block>
									I, the undersigned hereby authorize John Hancock New York to implement the investment changes requested herein above. I understand and acknowledge that (i) other Funds are available under the Plan with risk and return characteristics similar to the Funds identified in the IPS Manager review report as having the highest ranking based on my selected IPS Manager weighted criteria, and where information regarding those other Funds is located; (ii) the identification of any Funds with the red, yellow or green indicator or as a Highest-Ranking Fund in the IPS Manager review report is based on the weighted criteria set by me or, if applicable, my designated 3(38) Investment Manager, and that such identification is provided for information only to assist me (or, if applicable my designated 3(38) Investment Manager) in monitoring the Funds offered under my Plan; (iii) investment changes not covered by this form may be requested by completing a separate Contract Investment Administration Form; and (iv) I (and, if applicable my designated 3(38) Investment Manager) are solely responsible for determining whether or when it is appropriate to remove, retain or replace a Fund available to my Plan.  I further acknowledge that, in providing the IPS Manager Service, John Hancock New York is not undertaking to provide impartial investment advice or to give advice in a fiduciary capacity to me or my Plan, and that this service is provided without regard to the individualized needs of my Plan, its participants or beneficiaries.
								</fo:block>
								<fo:block padding-before="3px">
									In my role as the authorized fiduciary of the Plan, I have determined that the Fund changes requested herein above are appropriate for my Plan. I further agree to assume responsibility for communicating the changes requested on this Form to the Plan participants and beneficiaries. In particular, I will inform Plan participants and beneficiaries of the changes applicable to their accounts, provide them with information regarding the eliminated Funds, the replacement Funds, as well as the redemption fee that may be applicable to one or more of such Funds. I will also direct them to the John Hancock New York website or to its toll-free participant phone number if they wish to make alternate selections to the Fund replacements requested herein, transfer monies from the eliminated Funds, and/or change their investment instructions for their ongoing contributions.
								</fo:block>
								<fo:block padding-before="3px"> 
									On behalf of the Plan sponsor, the Plan and its related trust, I hereby (i) represent that John Hancock New York is entitled to rely on the directions provided in this form, (ii) represent that I (and, if applicable my designated 3(38) Investment Manager) are independent of, and not affiliated or under common control with, John Hancock or any of its affiliates. and (iii) agree to indemnify and hold harmless John Hancock New York, its affiliates, and each of their agents, employees, officers and directors from and against any and all claims, suits, losses, damages, costs, charges, counsel fees, payments, expenses, and liability arising out of, or attributable to, John Hancock New York implementation of the directions provided in this form. I further acknowledge and agree that any participant queries received by John Hancock New York pertaining to the requested changes should be directed to the undersigned for clarification.
								</fo:block>
							</xsl:otherwise>
							</xsl:choose>
					</fo:block>						                   	  
                   	</fo:block>
								
					<fo:block padding-before="25px">
                   	  <fo:table table-layout="fixed" width="100%">
                   		<fo:table-body>
                   			 <fo:table-row>
                   				<fo:table-cell width="220px" padding-right="12px">
                   					<fo:block xsl:use-attribute-sets="interim_footer_default_font top_line_border_style">
                   						Signature of Trustee/Authorized Named Fiduciary 
                   					</fo:block>
                   				</fo:table-cell>                   				
                   				<fo:table-cell width="210px" padding-right="12px">
                   					<fo:block text-align="left" xsl:use-attribute-sets="interim_footer_default_font top_line_border_style">
                   						Name - please print
                   					</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell width="110px">
                   					<fo:block text-align="left" xsl:use-attribute-sets="interim_footer_default_font top_line_border_style">
                   						Date
                   					</fo:block>
                   				</fo:table-cell>                   				
                   			</fo:table-row> 
                   		</fo:table-body>
                   	  </fo:table>
	            </fo:block>
	            <fo:block id="contractInvestmentAdministrationFormId">
	            </fo:block>
	        </fo:flow>
        </fo:page-sequence>
    </xsl:template>
</xsl:stylesheet>