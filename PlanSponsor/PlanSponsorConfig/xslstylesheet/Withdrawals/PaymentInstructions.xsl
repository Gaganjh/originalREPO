<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="PaymentInstructions">

				
		<!--Payment instructions  section -->
		<!-- use forEach tag to display all the recipient's payees  -->
		<xsl:for-each select="payees/withdrawalRequestPayeeUi/withdrawalRequestPayee">
			<fo:block space-before="20px" page-break-inside="avoid">
			
			<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell background-repeat="no-repeat" 
							xsl:use-attribute-sets="table_header_text_font" number-columns-spanned="2">
							<fo:block>
								<xsl:value-of select="$paymentInstructionsTitle_var" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="border_table_layout">
				<fo:table-column column-width="40%" />											
				<fo:table-column column-width="60%" />
				<fo:table-body>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Payment to
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<!-- Display description of paymentTo code -->
								
								<xsl:value-of select="../paymentToDisplay" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<xsl:if test="../showAccountNumber = 'true'">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Account number for rollover
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="rolloverAccountNo" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					<xsl:if test="../showTrusteeForRollover = 'true'">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Name of new plan
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							
							<fo:block>
								<xsl:choose>
									<xsl:when test="rolloverPlanName != ''">
										Trustee of  <xsl:value-of select="rolloverPlanName" /> plan
									</xsl:when>
								</xsl:choose>
							</fo:block>
							
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					<xsl:if test="not($paymentTo_var = 'TR')"> 
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									IRS distribution code for withdrawal
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:variable name="irsDistCode_var">
										<xsl:value-of select="irsDistCode"/>
									</xsl:variable>
									<xsl:for-each select="../../../../../../lookupData/IRS_CODE_WD/item[@key=$irsDistCode_var]">
										<xsl:value-of select="@value"/>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					 </xsl:if> 
					
					<xsl:if test="not(../../../../../wmsiOrPenchecksSelected = 'true')">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style" >
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" >
								Payment method
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block >
								 <xsl:choose>
									<xsl:when test="paymentMethodCode = 'AC'">
										<fo:block padding-after="2px">
											ACH
										</fo:block>
									</xsl:when>
									<xsl:when test="paymentMethodCode = 'WT'">
										<fo:block padding-after="2px">
											Wire
										</fo:block>
									</xsl:when>
									<xsl:when test="paymentMethodCode = 'CH'">
										<fo:block padding-after="2px">
											Check
										</fo:block>
									</xsl:when>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					
					<xsl:if test="paymentMethodCode = 'AC'"> 
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" padding-after="2px">
									Bank account type
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block padding-after="2px">
									 <xsl:choose>
										<xsl:when test="paymentInstruction/bankAccountTypeCode = 'C'">
											Checking
										</xsl:when>
										<xsl:when test="paymentInstruction/bankAccountTypeCode = 'S'">
											Savings
										</xsl:when>
									</xsl:choose>
								</fo:block>
								
							</fo:table-cell>
						</fo:table-row>
					</xsl:if> 
					<xsl:if test="$showStaticContent_var = 'true'">
					<fo:table-row  xsl:use-attribute-sets="border_table_layout" >
                       <fo:table-cell number-columns-spanned="2" space-before="20" xsl:use-attribute-sets="table_data_default_font"
                       border-top-style="solid" border-color="#e9e2c3" border-width="0.1mm" padding-after="2px">
                      		<xsl:for-each select="../../../../../cmaContent/step2PageBeanBody2HeaderList/string">
                      			<fo:block>
                     				<fo:inline font-size="9pt">&#x2022;&#160;</fo:inline>
                     				<fo:inline font-size="8pt">
                     					&#160;&#160;<xsl:value-of select="." />
                     				</fo:inline>
                      			</fo:block>
                      		</xsl:for-each>
                      		<fo:block/>
                     </fo:table-cell>
                     </fo:table-row>
                     </xsl:if>
                        
                     <!-- start Payment information -->
                     <xsl:if test="not(../../../../../wmsiOrPenchecksSelected = 'true')">
                      
                      	<xsl:variable name="countryCode_var">
							<xsl:value-of select="address/countryCode" />
						</xsl:variable>
							
                     <xsl:if test="not(paymentMethodCode = 'CH')"> 
	                     <xsl:choose>
	                     <xsl:when test="$showStaticContent_var = 'true'">
	                     <fo:table-row >
							<fo:table-cell color="#000000" background-color="#CAC8C4" xsl:use-attribute-sets="table_header_text_font"
								number-columns-spanned="2">
								<fo:block>
									<xsl:value-of select="$eftPayeeTitle_var" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:when>
						<xsl:otherwise>
							<fo:table-row >
							<fo:table-cell color="#000000" background-color="#CAC8C4" xsl:use-attribute-sets="table_header_text_font"
								number-columns-spanned="2" border-top-style="solid" border-color="#e9e2c3" border-width="0.1mm">
								<fo:block>
									<xsl:value-of select="$eftPayeeTitle_var" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:otherwise>
						</xsl:choose>
						
						<fo:table-row >
							<fo:table-cell font-size="9pt" xsl:use-attribute-sets="border_right_style">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								 <xsl:choose>
										<xsl:when test="../isFirstRolloverPayee = 'true'">
											Rollover financial institution
										</xsl:when>
										<xsl:otherwise>
											Payee
										</xsl:otherwise>
									</xsl:choose>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block padding-before="5px" space-before="15px"></fo:block>
							</fo:table-cell>
						</fo:table-row>
						 <fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									<xsl:value-of select="../subSectionNameColumn" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:value-of select="../eftOrganizationName" /> 
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									Address line 1
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:value-of select="address/addressLine1" /> 
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									Address line 2
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:value-of select="address/addressLine2" /> 
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									City 
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:value-of select="address/city" /> 
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									State
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									 <xsl:value-of select="address/stateCode" /> 
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									ZIP Code
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
							<xsl:variable name="zipCode1_var">
								<xsl:value-of select="address/zipCode1" />
							</xsl:variable>
							<xsl:variable name="zipCode2_var">
								<xsl:value-of select="address/zipCode2" />
							</xsl:variable>
								<xsl:choose>
									<xsl:when test="$zipCode1_var != '' and $zipCode2_var != '' and $countryCode_var = 'USA'">
										<fo:inline><xsl:value-of select="$zipCode1_var" /></fo:inline>
										<fo:inline>-</fo:inline>
										<fo:inline><xsl:value-of select="$zipCode2_var" /></fo:inline>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="concat($zipCode1_var, $zipCode2_var)" />
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									Country
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:for-each select="../../../../../../lookupData/COUNTRY_COLLECTION_TYPE/item[@key=$countryCode_var]">
										<xsl:value-of select="@value"/>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell font-size="9pt" xsl:use-attribute-sets="table_sub_header_text_bold_font" number-columns-spanned="2">
								<fo:block>
									Bank details
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									Bank/Branch name
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:value-of select="paymentInstruction/bankName" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									ABA / Routing number
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
									<xsl:value-of select="paymentInstruction/bankTransitNumber" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<!-- If showBankAccountNumber is true then display below table row -->
						<xsl:if test="../showBankAccountNumber = 'true'">
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
									Account number
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block>
								 <xsl:choose>
										<xsl:when test="$statusCodePath = 'W1' or $statusCodePath = 'W7'">
											<xsl:value-of select="../../../../../cmaContent/maskedAccountNumber" />
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="paymentInstruction/bankAccountNumber" />
										</xsl:otherwise>
									</xsl:choose>
							
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
								<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font" padding-after="2px">
									Credit party name
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
								<fo:block padding-after="2px">
									<xsl:value-of select="paymentInstruction/creditPartyName" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
                    </xsl:if> 
                     
                     <!-- Check Information Subsection (Shown if Payment method = Check) -->
                     <!-- start Check information  -->
                     
                     <xsl:if test="paymentMethodCode = 'CH'">
                     <xsl:choose>
	                     <xsl:when test="$showStaticContent_var = 'true'">
		                     <fo:table-row >
								<fo:table-cell color="#000000" background-color="#CAC8C4" xsl:use-attribute-sets="table_header_text_font"
									number-columns-spanned="2">
									<fo:block>
										<xsl:value-of select="../../../../../cmaContent/chequePayeeSectionTitle" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							</xsl:when>
							<xsl:otherwise>
								<fo:table-row >
								<fo:table-cell color="#000000" background-color="#CAC8C4" xsl:use-attribute-sets="table_header_text_font"
									number-columns-spanned="2" border-top-style="solid" border-color="#e9e2c3" border-width="0.1mm">
									<fo:block>
										<xsl:value-of select="../../../../../cmaContent/chequePayeeSectionTitle" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							</xsl:otherwise>
						</xsl:choose>
					<fo:table-row >
						<fo:table-cell font-size="9pt" xsl:use-attribute-sets="table_sub_header_text_bold_font">
							<fo:block>
								<xsl:choose>
									<xsl:when test="paymentTo_var = 'TR'">
										Trustee
									</xsl:when>
									<xsl:when test="../isFirstRolloverPayee = 'true'">
										Rollover financial institution
									</xsl:when>
									<xsl:otherwise>
										Participant
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block padding-before="5px" space-before="15px"></fo:block>
						</fo:table-cell>
					</fo:table-row>
	                <fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Name
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="../checkOrganizationName" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Address line 1
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="address/addressLine1" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Address line 2
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="address/addressLine2" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								City
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="address/city" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								State
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="address/stateCode" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								ZIP Code
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
							<xsl:variable name="zipCode1_var">
								<xsl:value-of select="address/zipCode1" />
							</xsl:variable>
							<xsl:variable name="zipCode2_var">
								<xsl:value-of select="address/zipCode2" />
							</xsl:variable>
								<xsl:choose>
									<xsl:when test="$zipCode1_var != '' and $zipCode2_var != '' and $countryCode_var = 'USA'">
										<fo:inline><xsl:value-of select="$zipCode1_var" /></fo:inline>
										<fo:inline>-</fo:inline>
										<fo:inline><xsl:value-of select="$zipCode2_var" /></fo:inline>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="concat($zipCode1_var, $zipCode2_var)" />
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Country
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:for-each select="../../../../../../lookupData/COUNTRY_COLLECTION_TYPE/item[@key=$countryCode_var]">
									<xsl:value-of select="@value"/>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row >
						<fo:table-cell font-size="9pt" xsl:use-attribute-sets="table_sub_header_text_bold_font" number-columns-spanned="2">
							<fo:block>
								Special delivery instructions
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="table_sub_header_text_bold_font" number-columns-spanned="2">
							<fo:block padding-after="2px">
							
							 <xsl:choose>
									<xsl:when test="sendCheckByCourier = 'true'">
										<fo:inline>
											<fo:external-graphic content-height="6px" content-width="6px" >
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')"/>
												</xsl:attribute>
											</fo:external-graphic>
										</fo:inline>
									</xsl:when>
									<xsl:otherwise>
										<fo:inline>
											<fo:external-graphic content-height="6px" content-width="6px" >
												<xsl:attribute name="src">
													<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')"/>
												</xsl:attribute>
											</fo:external-graphic>
										</fo:inline>
									</xsl:otherwise>
								</xsl:choose>
						
							<fo:inline>
								Send check by courier?
							</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					
					<!--If above check box block is selected then the following information should display. -->
					<!-- start -->
					<xsl:if test="sendCheckByCourier = 'true'">
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Courier name
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:variable name="courierCompanyCode_var">
									<xsl:value-of select="courierCompanyCode"/>
								</xsl:variable>
								<xsl:for-each select="../../../../../../lookupData/COURIER_COMPANY/item[@key=$courierCompanyCode_var]">
									<xsl:value-of select="@value"/>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row >
						<fo:table-cell xsl:use-attribute-sets="border_right_style" padding-start="15px">
							<fo:block xsl:use-attribute-sets="table_sub_header_text_bold_font">
								Courier number
							</fo:block>
						</fo:table-cell>
						<fo:table-cell xsl:use-attribute-sets="table_data_default_font">
							<fo:block>
								<xsl:value-of select="courierNo" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:if>
					<!-- end -->
					
					<fo:table-row >
						<fo:table-cell padding-start="20px" xsl:use-attribute-sets="table_data_default_font" number-columns-spanned="2">
							<fo:block>
								<xsl:if test="$showStaticContent_var = 'true'">
									<xsl:if test="$mailChequeToAddressIndicator_var = 'true'">
										<xsl:value-of select="$overrideCsfMailText_var" />
									</xsl:if>	
								</xsl:if>	
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					 <xsl:if test="$mailChequeToAddressIndicator_var = 'true'">
						<fo:table-row >
							<fo:table-cell xsl:use-attribute-sets="table_sub_header_text_bold_font" number-columns-spanned="2">
								<fo:block padding-after="4px">
									 <xsl:choose>
										<xsl:when test="mailCheckToAddress = 'true'">
											<fo:inline>
												<fo:external-graphic content-height="6px" content-width="6px" >
													<xsl:attribute name="src">
														<xsl:value-of select="concat($imagePath_var, 'checkedPlainCheckboxSmall.gif')"/>
													</xsl:attribute>
												</fo:external-graphic>
											</fo:inline>
										</xsl:when>
										<xsl:otherwise>
											<fo:inline>
												<fo:external-graphic content-height="6px" content-width="6px" >
													<xsl:attribute name="src">
														<xsl:value-of select="concat($imagePath_var, 'plaincheckboxSmall.gif')"/>
													</xsl:attribute>
												</fo:external-graphic>
											</fo:inline>
										</xsl:otherwise>
									</xsl:choose>
								
								<fo:inline>
									Send to address above 
								</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					 </xsl:if>
					
					</xsl:if>
					 <!-- end Check information  -->
					 
					  </xsl:if> 
					 <!-- end Payment information -->
					
					</fo:table-body>
				</fo:table>
			
			</fo:block>
			 
			<xsl:variable name="payeeStatus_var">
				<xsl:value-of select="$payeeStatus_count_var - 1"/>
			</xsl:variable>  
			<xsl:if test="$payeeStatus_var &gt; 0">
				<fo:block>
				</fo:block>
			</xsl:if>
			
		 </xsl:for-each> 
			
	</xsl:template>
</xsl:stylesheet>