<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template name="ContractInvestmentFormTemplate">                   
        <!-- Investment Selection Form -->
        <fo:page-sequence master-reference="ContractInvestmentFormLayout" initial-page-number="1" force-page-count="no-force">
        	
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
											<xsl:otherwise>
												GP5632NY (03/2017) IPSM
											</xsl:otherwise>
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
	            <fo:block id="contractInvestmentFormId">
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
															<xsl:if test="/annual_review_report/companyId = '094'" >
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
																<fo:block font-size="11pt">
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
	            	
                      <fo:table table-layout="fixed" width="100%" space-before="40px">
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
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" padding-before="20px" font-weight="bold"> 
                   						<fo:inline font-weight="normal">This form is provided as part of the IPS Manager review report (the “report”) run on </fo:inline> <xsl:value-of select="annual_review_report/currentDate"/>. <fo:inline font-weight="normal">Complete this form if you want to make changes to the Funds available under your contract following your review. See the review report for full details regarding the rankings of the Funds previously selected through the use of IPS Manager.</fo:inline>  
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>
                   			<!-- <fo:table-row>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" padding-before="20px"> 
                   						                          			
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row> -->
                   			<fo:table-row>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" padding-before="4px"> 
                   						For your convenience, if you want to only make changes to one or more of the Funds identified with a red indicator in the report and replace them with the highest ranking Fund within the same John Hancock Life Insurance Company <xsl:choose>
		<xsl:when test="/annual_review_report/companyId = '019'">(U.S.A.) ("John Hancock USA")</xsl:when>
		<xsl:otherwise>of New York ("John Hancock New York")</xsl:otherwise></xsl:choose> asset class, identified in the report as of the date it was prepared and calculated based on your IPS Manager selection criteria (the “Highest-Ranking Fund”),complete Section 2. Note: The Funds with a red indicator are those which, as of the date the report was prepared and based on your IPS Manager weighted criteria, have held a ranking between 51 and 100 for the last four consecutive quarters as of the date the report was prepared.                            			
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>
                   			<!-- <fo:table-row>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" padding-before="4px" font-weight="bold"> 
                   						Note<fo:inline font-weight="normal">: The Funds with a red indicator are those which, based on your IPS Manager selection criteria, have held a ranking between 51 and 100 for the last four consecutive quarters as of the date the report was prepared.</fo:inline>                           			
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row> -->
							<fo:table-row>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold" padding-before="4px">
                   						<xsl:choose>
    	               						<xsl:when test="/annual_review_report/companyId = '019'">
    	               							For any change requests other than those described above, complete the form entitled “Contract Investment Administration Form” available on the Plan Sponsor website, <fo:basic-link color="blue"><xsl:attribute name="external-destination">url('www.jhpensions.com/er')</xsl:attribute>www.jhpensions.com/er</fo:basic-link>, or contact your Client Account Representative to obtain copies.
    	               						</xsl:when>
    	               						<xsl:otherwise>
    	               							For any change requests other than those described above, complete the form entitled “Contract Investment Administration Form” available on the Plan Sponsor website, <fo:basic-link color="blue"><xsl:attribute name="external-destination">url('www.jhnypensions.com/er')</xsl:attribute>www.jhnypensions.com/er</fo:basic-link>, or contact your Client Account Representative to obtain copies. 
    	               						</xsl:otherwise>
	                   					</xsl:choose>                   						                         			
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>
							<fo:table-row>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" padding-before="2px"> 
                   						You should specify the effective date for the Fund changes. When determining the effective date you should allow for sufficient time for you to deliver a notification of these changes to your Plan participants and beneficiaries on a timely basis. When your request is processed, investment instructions for future contributions into a replaced Fund will be changed to the new replacement Fund.  			
                           			</fo:block>
                           		</fo:table-cell>
                   			</fo:table-row>
                   		</fo:table-body>
                      </fo:table>        
                      <fo:table table-layout="fixed" width="100%" space-before="30px">
                      	<fo:table-body>
                   			<fo:table-row>
                   				<fo:table-cell width="158px">
	                     			<fo:block></fo:block>
	                     		</fo:table-cell>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_header_default_font">
										Important Information Before you Begin
                   					</fo:block>
                   				</fo:table-cell>
                   			</fo:table-row>
                   		</fo:table-body>
                      </fo:table> 
                      <fo:table table-layout="fixed" width="100%" space-before="28px">
                      	<fo:table-body>
                   			<fo:table-row>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" font-weight="bold"> 
                   						Section 2 – Direction for IPS Manager Interim Review Investment Selection                               			
                           			</fo:block>
                   				</fo:table-cell>
                   			</fo:table-row>
                  			<fo:table-row>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font"> 
                   						Check the appropriate box if you elect to remove a Current Fund and replace it with the Highest-Ranking Fund in the same asset class.			
                           			</fo:block>
                   				</fo:table-cell>
                  			</fo:table-row>
                   		</fo:table-body>
                      </fo:table>
                      <fo:table table-layout="fixed" width="100%" space-before="10px">
                      	<fo:table-body>
                   			<fo:table-row>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" >
                   						By checking “Yes”,
                   					</fo:block>
                   				</fo:table-cell>
                   			</fo:table-row>
                   		</fo:table-body>
                      </fo:table>
                      <fo:table table-layout="fixed" width="100%">
                   		<fo:table-body>
	                     	<fo:table-row>                   				
                   				<fo:table-cell width="10px">
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font"> 
                   						1.
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font"> 
                   						The Highest Ranking Fund you select will be added to your Contract investment line-up as a replacement Fund and will be subject to the IPS Manager review on a go-forward basis
                           			</fo:block>
                   				</fo:table-cell>
                  			</fo:table-row>
                  			<fo:table-row>	                     		
                   				<fo:table-cell width="10px">
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font"> 
                   						2.
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font"> 
                   						The assets invested in the “Current Fund” will be moved to the replacement Fund you select
                           			</fo:block>
                   				</fo:table-cell>
                  			</fo:table-row>
                  			<fo:table-row>                   				
                   				<fo:table-cell width="10px">
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font"> 
                   						3.
                           			</fo:block>
                   				</fo:table-cell>
                   				<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font"> 
                   						The “Current Fund” will be removed from your Contract investment line-up
                           			</fo:block>
                   				</fo:table-cell>
                  			</fo:table-row>												
	                     </fo:table-body>
	                    </fo:table>	 
						<fo:table table-layout="fixed" width="100%">
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
                   					<fo:block xsl:use-attribute-sets="interim_word_group_default_font" padding-before="10px"> 
                   						If you do not wish to replace a Current Fund by the Highest-Ranking Fund in the same asset class, check “No” or leave the boxes blank.                  			
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