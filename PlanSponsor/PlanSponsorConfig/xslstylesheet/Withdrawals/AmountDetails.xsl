<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java"
	xmlns:BD="com.manulife.pension.ps.web.iloans.util.BigDecimalFormatter">
	<xsl:template name="WithdrawalAmount">
		
		<!--Start Amount details section -->
		<fo:block space-before="20px" page-break-inside="avoid">
			<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell background-repeat="no-repeat"  
							xsl:use-attribute-sets="table_header_text_font" number-columns-spanned="2">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/cmaContent/withdrawalAmountSectionTitle" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			
			<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
				<fo:table-body>
					<fo:table-row >
						<fo:table-cell>
							<fo:block>
								<xsl:variable name="amountTypeCode_var">
									<xsl:value-of select='withdrawalRequestUi/withdrawalRequest/amountTypeCode'/>
								</xsl:variable>
								<fo:inline xsl:use-attribute-sets="table_cell_first_inline_withdrawal_default_bold_font">
									Enter amount as 
								</fo:inline>
								<fo:inline xsl:use-attribute-sets="table_data_default_font" space-after="15px">
									<xsl:for-each select="lookupData/WITHDRAWAL_AMOUNT_TYPE/item[@key=$amountTypeCode_var]">
										<xsl:value-of select="@value"/>
									</xsl:for-each>
								</fo:inline>
								<!-- If amount type Code is equals to WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE('SA') 
								    then display below two fields  -->
								<xsl:if test="$amountTypeCode_var = 'SA'"> 
									<fo:inline xsl:use-attribute-sets="table_cell_second_inline_withdrawal_default_bold_font">
										Amount 
									</fo:inline>
									<fo:inline xsl:use-attribute-sets="table_data_default_font" padding-start="5px">
										$<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/withdrawalAmount" />
									</fo:inline>
								 </xsl:if> 
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			
			<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
					
					<xsl:choose>
					<xsl:when test=" $showPortionOfAvailableAmtPercentColumn_var = 'true' and 
										$showAvailableAmountColumn_var = 'true'">
						<fo:table-column column-width="15%" />											
						<fo:table-column column-width="15%" />
						<fo:table-column column-width="15%" />	
						<fo:table-column column-width="17%" />
						<fo:table-column column-width="18%" />	
						<fo:table-column column-width="20%" />	
					</xsl:when>
					<xsl:when test=" $showAvailableAmountColumn_var = 'true' and 
											$showPortionOfAvailableAmtPercentColumn_var = 'false'">
						<fo:table-column column-width="20%" />											
						<fo:table-column column-width="20%" />
						<fo:table-column column-width="18%" />	
						<fo:table-column column-width="20%" />
						<fo:table-column column-width="22%" />			
					</xsl:when>
					<xsl:when test=" $showPortionOfAvailableAmtPercentColumn_var = 'true' and 
										$showAvailableAmountColumn_var = 'false'">
						<fo:table-column column-width="20%" />											
						<fo:table-column column-width="20%" />
						<fo:table-column column-width="18%" />	
						<fo:table-column column-width="20%" />	
						<fo:table-column column-width="22%" />	
					</xsl:when>
					<xsl:otherwise>
						<fo:table-column column-width="25%" />											
						<fo:table-column column-width="25%" />
						<fo:table-column column-width="20%" />	
						<fo:table-column column-width="30%" />	
					</xsl:otherwise>
					</xsl:choose>										
					
					
				<fo:table-body>
					<fo:table-row xsl:use-attribute-sets="table_row_text_color" >
						<fo:table-cell xsl:use-attribute-sets="table_cell_border_style" padding-start="6px">
							<fo:block>Money type</fo:block>
						</fo:table-cell>
						<fo:table-cell  xsl:use-attribute-sets="table_text_right_align_font" >
							<fo:block >
								<fo:inline>
									Account balance ($)
								</fo:inline>
								<xsl:if test="withdrawalRequestUi/hasPbaOrLoans = 'true'">
									<fo:inline font-size="5pt" baseline-shift="super">^</fo:inline> 
								</xsl:if>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell  xsl:use-attribute-sets="table_text_right_align_font">
							<fo:block >Vesting (%)</fo:block>
						</fo:table-cell>
						
						<xsl:if test=" $showAvailableAmountColumn_var = 'true'">
							<fo:table-cell  xsl:use-attribute-sets="table_text_right_align_font">
								<fo:block>
									Available amount ($)
								</fo:block>
							</fo:table-cell>
						</xsl:if>
						
						<xsl:if test="$showPortionOfAvailableAmtPercentColumn_var = 'false'">
							<fo:table-cell  xsl:use-attribute-sets="table_text_right_align_font" 
							border-right-style="solid" border-color="#cac8c4">
								<fo:block>
									Requested amount ($)
								</fo:block>
							</fo:table-cell>
						</xsl:if>
						
						<xsl:if test="$showPortionOfAvailableAmtPercentColumn_var = 'true'">
							<fo:table-cell  xsl:use-attribute-sets="table_text_right_align_font">
								<fo:block>
									Requested amount ($)
								</fo:block>
							</fo:table-cell>
						</xsl:if>
							
						<xsl:if test=" $showPortionOfAvailableAmtPercentColumn_var = 'true'">
							<fo:table-cell  xsl:use-attribute-sets="table_text_right_align_font" 
								border-right-style="solid" border-color="#cac8c4">
								<fo:block >Portion of available amount (%)</fo:block>
							</fo:table-cell>
						</xsl:if>
					</fo:table-row>
					
					<xsl:for-each select="withdrawalRequestUi/withdrawalRequest/moneyTypes/withdrawalRequestMoneyType">
					<xsl:choose>
					<xsl:when test="position() mod 2=0"> 
						<fo:table-row background-color="#e9e2c3" >
							<fo:table-cell color="#000000" xsl:use-attribute-sets="border_right_style">
								<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="3px">
									<xsl:value-of select="moneyTypeName" /> 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font" >
								<fo:block>
								<xsl:value-of select="totalBalance" /> 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font" >
								<fo:block><xsl:value-of select="BD:format(vestingPercentage, 3, 3)" /> </fo:block>
							</fo:table-cell>
							
							<!-- Available amount -->
							<xsl:if test=" $showAvailableAmountColumn_var = 'true'">
								<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font" >
									<fo:block>
										<xsl:value-of select="availableWithdrawalAmount" /> 
									</fo:block>
								</fo:table-cell>
							</xsl:if>
							
							<!-- Requested amount -->
							<xsl:if test=" $showPortionOfAvailableAmtPercentColumn_var = 'false'">
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font_layout">
								<fo:block>
									<xsl:value-of select="withdrawalAmount" />  
								</fo:block>
							</fo:table-cell>  
							</xsl:if>
							
							<xsl:if test="$showPortionOfAvailableAmtPercentColumn_var = 'true'">
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font">
								<fo:block>
									<xsl:value-of select="withdrawalAmount" />  
								</fo:block>
							</fo:table-cell>  
							</xsl:if>
							
							<!-- Portion of available amount -->	
							<xsl:if test=" $showPortionOfAvailableAmtPercentColumn_var = 'true'">
								<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font_layout" >
								<fo:block>
								<!-- hard coded 100.00 % for participant initiated requests -->
									<xsl:if test="$isParticipantInitiated_var = 'true'">
										100.00 
									</xsl:if>
									<xsl:if test="$isParticipantInitiated_var = 'false'">
										<xsl:value-of select="withdrawalPercentage" />  
									</xsl:if>
								</fo:block>
								</fo:table-cell>  
							</xsl:if>
						
						</fo:table-row>
						</xsl:when>
						<xsl:otherwise>
						<fo:table-row >
							<fo:table-cell color="#000000" xsl:use-attribute-sets="border_right_style">
								<fo:block xsl:use-attribute-sets="table_data_default_font" padding-after="3px">
									<xsl:value-of select="moneyTypeName" /> 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font" >
								<fo:block>
								<xsl:value-of select="totalBalance" /> 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font" >
								<fo:block><xsl:value-of select="BD:format(vestingPercentage, 3, 3)" /> </fo:block>
							</fo:table-cell>
							
							<!-- Available amount -->
							<xsl:if test=" $showAvailableAmountColumn_var = 'true'">
								
								<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font" >
									<fo:block>
										<xsl:value-of select="availableWithdrawalAmount" /> 
									</fo:block>
								</fo:table-cell>
							</xsl:if>
								
							<!-- Requested amount -->
							<xsl:if test="$showPortionOfAvailableAmtPercentColumn_var = 'false'">
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font_layout">
								<fo:block>
									<xsl:value-of select="withdrawalAmount" />  
								</fo:block>
							</fo:table-cell>  
							</xsl:if>
							<xsl:if test=" $showPortionOfAvailableAmtPercentColumn_var = 'true'">
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font">
								<fo:block>
									<xsl:value-of select="withdrawalAmount" />  
								</fo:block>
							</fo:table-cell>  
							</xsl:if>
								
							<!-- Portion of available amount -->	
							<xsl:if test=" $showPortionOfAvailableAmtPercentColumn_var = 'true'">
								<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font_layout" >
								<fo:block>
								<!-- hard coded 100.00 % for participant initiated requests -->
									<xsl:if test="$isParticipantInitiated_var = 'true'">
										100.00 
									</xsl:if>
									<xsl:if test="$isParticipantInitiated_var = 'false'">
										<xsl:value-of select="withdrawalPercentage" />  
									</xsl:if>
								</fo:block>
								</fo:table-cell>  
							</xsl:if>
							
						</fo:table-row>
						</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
					
					<fo:table-row >
						<fo:table-cell color="#000000" xsl:use-attribute-sets="border_right_style">
							<fo:block></fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font">
							<fo:block font-weight = "bold">Total:</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font">
							<fo:block></fo:block>
						</fo:table-cell>
						
						<xsl:if test=" $showAvailableAmountColumn_var = 'true'">
							<!-- Available amount -->
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font" >
								<fo:block color="#C55927" font-weight="bold">
								<xsl:value-of select="$totalAvailableWithdrawalAmount_var" />
								</fo:block>
							</fo:table-cell>
						</xsl:if>
							
						<!-- Requested amount -->
						<xsl:if test="$showPortionOfAvailableAmtPercentColumn_var = 'false'">
						<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font_layout" >
							<fo:block color="#C55927" font-weight="bold">
							<xsl:if test="$showTotalRequestedWithdrawalAmount_var = 'true'">
								<xsl:value-of select="$totalRequestedWithdrawalAmount_var" />
							</xsl:if>
							</fo:block>
						</fo:table-cell>  
						</xsl:if>
						
						<xsl:if test="$showPortionOfAvailableAmtPercentColumn_var = 'true'">
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font" >
							<fo:block color="#C55927" font-weight="bold">
							<xsl:if test="$showTotalRequestedWithdrawalAmount_var = 'true'">
								<xsl:value-of select="$totalRequestedWithdrawalAmount_var" />
							</xsl:if>
							</fo:block>
							</fo:table-cell>  
						</xsl:if>				
						
						<!-- Portion of available amount -->
						<xsl:if test=" $showPortionOfAvailableAmtPercentColumn_var = 'true'">
							<fo:table-cell xsl:use-attribute-sets="withdrawal_text_right_align_font_layout" >
								<fo:block></fo:block>
							</fo:table-cell>  
						</xsl:if>
					</fo:table-row>
					
					</fo:table-body>
				</fo:table>
				
				
				<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
				<fo:table-body>
					<!-- Start TPA transaction fee -->
					<xsl:if test="withdrawalRequestUi/withdrawalRequest/contractInfo/hasATpaFirm = 'true'">
						<xsl:for-each select="withdrawalRequestUi/fees/withdrawalRequestFeeUi/withdrawalRequestFee">
							<fo:table-row >
								<fo:table-cell xsl:use-attribute-sets="table_row_background-color">
									<!-- use forEach tag to display all TPA withdrawal fees -->
									<xsl:variable name="typeCode_var" >
										<xsl:value-of select="typeCode" />
									</xsl:variable>
									<fo:block>
										<fo:inline xsl:use-attribute-sets="table_sub_header_text_bold_font" padding-start="4px">
											TPA withdrawal fee
										</fo:inline>
										
										<xsl:choose>
											<xsl:when test="value != ''">
												<fo:inline xsl:use-attribute-sets="table_cell_inline_withdrawal_default_font">
													<xsl:value-of select="value" />&#160;
												</fo:inline>
												<fo:inline font-family="Frutiger 45 Light" font-size="8pt">
													<xsl:for-each select="../../../../lookupData/TPA_TRANSACTION_FEE_TYPE/item[@key=$typeCode_var]">
														<xsl:value-of select="@value"/>
													</xsl:for-each>	
												</fo:inline>
												
											</xsl:when>
											<xsl:otherwise>
												<fo:inline xsl:use-attribute-sets="table_cell_inline_withdrawal_default_font">
													None
												</fo:inline>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					<!-- End Fee Loop -->
					
					<!-- start -->
						<xsl:if test="$showStaticContent_var = 'true'">
						<fo:table-row>
							<fo:table-cell border-bottom-style="solid" border-color="#e9e2c3" border-width="0.1mm">
								<fo:table>
									<fo:table-column column-width="3.3%"/>
									<fo:table-column column-width="96.7%"/>
										<fo:table-body>
										<xsl:for-each select="withdrawalRequestUi/cmaContent/step2PageBeanBody1HeaderList/string">
											<fo:table-row>
												<fo:table-cell padding-end="2mm" padding-start="2.5mm" padding-before="3mm">
													<fo:block xsl:use-attribute-sets="square_check_box_style">&#x274F;</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block xsl:use-attribute-sets="table_data_default_font">
														<xsl:value-of select="." /> 
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:for-each>
											
										<xsl:if test="withdrawalRequestUi/participantHasPartialStatus = 'true'">
											<fo:table-row>
												<fo:table-cell padding-end="2mm" padding-start="2.5mm" padding-before="3mm">
													<fo:block xsl:use-attribute-sets="square_check_box_style">
														&#x274F;
													</fo:block> 
												</fo:table-cell>
												<fo:table-cell>
													<fo:block xsl:use-attribute-sets="table_data_default_font">
														<xsl:value-of select="withdrawalRequestUi/cmaContent/participantHasPartialStatusText" /> 
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:if>
										<xsl:if test="withdrawalRequestUi/hasPre1987MoneyTypes = 'true'"> 
											<fo:table-row>
												<fo:table-cell padding-end="2mm" padding-start="2.5mm" padding-before="3mm">
													<fo:block xsl:use-attribute-sets="square_check_box_style">
														&#x274F;
													</fo:block> 
												</fo:table-cell>
												<fo:table-cell>
													<fo:block xsl:use-attribute-sets="table_data_default_font">
														<xsl:value-of select="withdrawalRequestUi/cmaContent/participantHasPre1987MoneyTypeText" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:if>
										<xsl:if test="withdrawalRequestUi/showReasonIsFullyVestedFromPlanSpecialMessage = 'true'"> 
											<fo:table-row>	
												<fo:table-cell padding-end="2mm" padding-start="2.5mm" padding-before="3mm">
													<fo:block xsl:use-attribute-sets="square_check_box_style">
													</fo:block>
												</fo:table-cell>								
												<fo:table-cell>
													<fo:block xsl:use-attribute-sets="table_data_default_font">
														<xsl:value-of select="withdrawalRequestUi/cmaContent/withdrawalReasonIsFullyVestedText"/>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</xsl:if>
										<fo:table-row>
											<fo:table-cell padding-after="2mm" padding-end="2mm" padding-start="2.5mm" padding-before="3mm">
													<fo:block xsl:use-attribute-sets="square_check_box_style">
														&#x274F;
													</fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block xsl:use-attribute-sets="table_data_default_font">
													<xsl:value-of select="withdrawalRequestUi/cmaContent/step2PageBeanBody1"/>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
					<!-- end -->
					
						<xsl:if test="withdrawalRequestUi/showOptionForUnvestedAmount = 'true'">
							<fo:table-row>
		                       	<fo:table-cell>
		                       		<fo:block>
		                       		</fo:block>
		                       	</fo:table-cell>
		                   	</fo:table-row>
						</xsl:if>
					</xsl:if>
					<!-- End TPA transaction fee -->
					
					<xsl:if test="withdrawalRequestUi/showOptionForUnvestedAmount = 'true'"> 
					<fo:table-row>
                       	<fo:table-cell>
                       		<xsl:variable name="unvestedAmountOptionCode_var" >
								<xsl:value-of select="withdrawalRequestUi/withdrawalRequest/unvestedAmountOptionCode" />
							</xsl:variable>
                       		<fo:block>
								<fo:inline xsl:use-attribute-sets="table_sub_header_text_bold_font" padding-start="4px">
									Option for unvested money
								</fo:inline>
								<fo:inline xsl:use-attribute-sets="table_cell_inline_default_font">
									<xsl:for-each select="lookupData/OPTIONS_FOR_UNVESTED_AMOUNTS/item[@key=$unvestedAmountOptionCode_var]">
										<xsl:value-of select="@value"/>
									</xsl:for-each>	
								</fo:inline>
                       		</fo:block>
                       	</fo:table-cell>
                   	</fo:table-row>
                   	</xsl:if> 
                   	
                   	<!-- Start Tax Withholding section -->
                   	<xsl:if test="withdrawalRequestUi/showTaxWitholdingSection = 'true'">
                   	<fo:table-row>
						<fo:table-cell color="#000000" background-color="#CAC8C4" xsl:use-attribute-sets="table_header_text_font" padding-start="1px">
							<fo:block>
								<xsl:value-of select="withdrawalRequestUi/cmaContent/taxWithholdingSectionTitle" />  
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								<fo:inline xsl:use-attribute-sets="table_cell_first_inline_withdrawal_default_bold_font">
									Federal tax rate
								</fo:inline>
								<fo:inline xsl:use-attribute-sets="table_data_default_font" space-after="15px" padding-start="10px">
									<xsl:value-of select="BD:format(withdrawalRequestUi/federalTaxPercent, 4, 4)" />% of taxable withdrawal amount
								</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<xsl:if test="withdrawalRequestUi/stateTaxPercent != ''">
						<fo:table-row >
							<fo:table-cell>
								<fo:block>
									<fo:inline xsl:use-attribute-sets="table_cell_first_inline_withdrawal_default_bold_font">
										State tax rate 
									</fo:inline>
									<xsl:if test="withdrawalRequestUi/stateTaxType = 'W'">
										<fo:inline xsl:use-attribute-sets="table_data_default_font" space-after="15px" padding-start="20px">
											<xsl:value-of select="BD:format(withdrawalRequestUi/stateTaxPercent, 4, 4)" />% of taxable withdrawal amount
										</fo:inline>
									</xsl:if>
									<xsl:if test="withdrawalRequestUi/stateTaxType = 'F'"> 
										<fo:inline xsl:use-attribute-sets="table_data_default_font" space-after="15px" padding-start="20px">
											<xsl:value-of select="BD:format(withdrawalRequestUi/stateTaxPercent, 4, 4)" />% of federal tax
										</fo:inline>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
					</xsl:if>
					<!-- End Tax Withholding section -->
				</fo:table-body>
			</fo:table>	
		</fo:block>
		<!--End Amount details section -->
	</xsl:template>
</xsl:stylesheet>