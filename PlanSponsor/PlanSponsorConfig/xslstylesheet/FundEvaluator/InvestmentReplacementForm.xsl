<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="InvestmentReplacementFormTemplate">
		<fo:page-sequence master-reference="InvestmentReplacementFormLayout">
			
			<fo:static-content flow-name="xsl-region-before" >
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="38%"/>
					<fo:table-column column-width="62%"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									<fo:external-graphic content-width="122px" content-height="54px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'JHRPS-logo-blue.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell display-align="center">
								<fo:block xsl:use-attribute-sets="IVSF_form_title">
									Investment Replacement Funds
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row height="20px" border-bottom-style="solid" border-bottom-width="0.5px">
							<fo:table-cell number-columns-spanned="2">
								<fo:block/>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				
				<fo:table table-layout="fixed" width="100%" space-before="4px">
					<fo:table-column column-width="407px"/>
					<fo:table-column column-width="162px"/>
					<fo:table-body>
						<fo:table-row height="10px">
							<fo:table-cell padding-start="1px" xsl:use-attribute-sets="contract_box_border_style">
								<fo:block space-after="8px" xsl:use-attribute-sets="information_page_word_group" font-size="7pt">
									Contractholder Name
								</fo:block>
								<fo:table table-layout="fixed" width="100%">
									<fo:table-column column-width="17%"/>
									<fo:table-column column-width="65%"/>
									<fo:table-column column-width="18%"/>
									
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell padding-start="5px">
												<fo:block xsl:use-attribute-sets="IREP_section_title" border-top-style="none">
													The Trustees of
												</fo:block>
											</fo:table-cell>
											<fo:table-cell padding-right="8px">
											<fo:block-container overflow="hidden" wrap-option="no-wrap">
												<fo:block xsl:use-attribute-sets="IREP_section_title" border-top-style="none">
													<xsl:value-of select="$companyName"/>
												</fo:block>
											</fo:block-container>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block xsl:use-attribute-sets="IREP_section_title" border-top-style="none">
													Plan (the "Plan")
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:table-cell>
							
							<fo:table-cell padding-start="1px" xsl:use-attribute-sets="contract_box_border_style">
								<fo:block space-after="8px" xsl:use-attribute-sets="information_page_word_group" font-size="7pt">
									Contract Number
								</fo:block>
								<fo:block xsl:use-attribute-sets="IREP_section_title" border-top-style="none">
									<xsl:value-of select="$contractNumber"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>
			

			<!-- Footer -->
			<fo:static-content flow-name="xsl-region-after">
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="100%"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell border-bottom-style="solid" border-bottom-width="0.5px">
								<fo:block xsl:use-attribute-sets="IVSF_footer_text">
									<xsl:choose>
										<xsl:when test="$companyCode = 'USA'">GP4973-FE (09/2010)</xsl:when>
										<xsl:otherwise>GP4973-NY-FE (09/2010)</xsl:otherwise>
									</xsl:choose>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell padding-before="2px">
								<fo:block xsl:use-attribute-sets="IVSF_footer_text">
									© 2014 John Hancock. All rights reserved. 
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>
		
			<fo:flow flow-name="xsl-region-body">
				<fo:block xsl:use-attribute-sets="IREP_section_title" space-before="1px">
					Section A - Direction for Investment Replacement Funds
				</fo:block>
					
				<fo:table table-layout="fixed" width="100%" space-before="2px">
					<fo:table-column column-width="60px"/>
					<fo:table-column column-width="32"/>
					<fo:table-column column-width="32"/>
					<fo:table-column column-width="43"/>
					
					<fo:table-body>
						<fo:table-row height="26px">
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="IREP_section_title" border-top-style="none">
									Effective Date
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style" text-align="center">
								<fo:block xsl:use-attribute-sets="information_page_word_group" font-size="7pt">
									Month
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style" text-align="center">
								<fo:block xsl:use-attribute-sets="information_page_word_group" font-size="7pt">
									Day
								</fo:block>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style" text-align="center">
								<fo:block xsl:use-attribute-sets="information_page_word_group" font-size="7pt">
									Year
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				
				<fo:block xsl:use-attribute-sets="IVSF_word_group" line-height="1" space-before="3px">
					<fo:block space-after="2px">
						As the authorized fiduciary of the Plan named above (the "Plan"), I hereby direct and authorize the John Hancock Life Insurance Company (
						<xsl:choose>
							<xsl:when test="$companyCode = 'USA'">U.S.A.</xsl:when>
							<xsl:otherwise>New York</xsl:otherwise>
						</xsl:choose>
						)("John Hancock <xsl:value-of select="$companyCode"/>") to make the following changes to the investment funds currently offered to our participants under the above contract (the "Contract").
					</fo:block>
					
					<fo:block space-after="2px">
						I hereby direct John Hancock <xsl:value-of select="$companyCode"/> to (1) transfer all assets invested in the eliminated funds listed below to the respective replacement funds selected below, and (2) substitute all investment instructions for future contributions into the eliminated funds by the respective replacement funds selected below. The above transfer and change shall be effective as of the later of the Effective Date noted above or the day that this form, duly completed and in good order, is received by John Hancock <xsl:value-of select="$companyCode"/>. In addition, I also direct John Hancock <xsl:value-of select="$companyCode"/> to update all participant accounts under the Contract to reflect the changes requested herein.
					</fo:block>
					
					<fo:block space-after="1px">
						I understand that the following terms and conditions apply to my requested changes:
					</fo:block>
					
					<fo:list-block>
						<fo:list-item space-after="2px">
							<fo:list-item-label start-indent="15px">
								<fo:block>
									a)
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body start-indent="25px">
								<fo:block>
									I have reviewed the fund sheets of the eliminated funds and the replacement funds; and agree that both the transfer of assets and the foregoing investment instruction changes will be effective on the later of the Effective Date shown above or the day that this Form, duly completed and in good order, is received by John Hancock <xsl:value-of select="$companyCode"/>.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item space-after="2px">
							<fo:list-item-label start-indent="15px">
								<fo:block>
									b)
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body start-indent="25px">
								<fo:block>
									I understand that a redemption fee may apply with respect to the transfer of assets from one or more of the eliminated funds, and that one or more of there placement funds may provide for a redemption fee. 
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item space-after="2px">
							<fo:list-item-label start-indent="15px">
								<fo:block>
									c)
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body start-indent="25px">
								<fo:block>
									If John Hancock Stable Value Fund is being eliminated, the requested transfer may be subject to either a 12 month delay or a market value adjustment by the underlying collective investment trust. I have selected one of the options in Section B below.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item space-after="2px">
							<fo:list-item-label start-indent="15px">
								<fo:block>
									d)
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body start-indent="25px">
								<fo:block>
									If I select a suite of Lifecycle Portfolios as the replacement funds, all portfolios associated with that suite will be used as replacement funds. Each participant’s balances in the eliminated funds and/or future contributions will be placed in the newly added lifecycle fund that corresponds to, or is closest to, the year in which the participant attains the age 67. You agree to provide accurate dates of birth for the affected participants as of the effective date indicated above. Failure to do so will result in the investment of such participant’s transferred balance and/or future contributions in the most conservative portfolio in the selected suite of Lifecycle Portfolios.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
						<fo:list-item space-after="2px">
							<fo:list-item-label start-indent="15px">
								<fo:block>
									e)
								</fo:block>
							</fo:list-item-label>
							<fo:list-item-body start-indent="25px">
								<fo:block>
									If one or more of the eliminated fund(s) is currently selected as a Default Investment option(s), I must complete the Default Investment Option Form to update my default investment option for my contract.
								</fo:block>
							</fo:list-item-body>
						</fo:list-item>
					</fo:list-block>
				</fo:block>
				
				<fo:table table-layout="fixed" width="100%" space-after="4px">
					<fo:table-column column-width="25px"/>
					<fo:table-column column-width="183px"/>
					<fo:table-column column-width="79px"/>
					<fo:table-column column-width="180px"/>
					<fo:table-column column-width="79px"/>
					<fo:table-column column-width="18px"/>
					
					<fo:table-body>
						<fo:table-row text-align="center" space-after="1px">
							<fo:table-cell column-number="2">
								<fo:block xsl:use-attribute-sets="IVSF_footer_text" font-weight="bold">
									Name of Fund Being Eliminated
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="IVSF_footer_text" font-weight="bold">
									3-Letter Fund Code
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="IVSF_footer_text" font-weight="bold">
									Replaced By Name of Fund:
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="IVSF_footer_text" font-weight="bold">
									3-Letter Fund Code
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row height="15px">
							<fo:table-cell column-number="2" xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row height="1px">
							<fo:table-cell><fo:block/></fo:table-cell>
						</fo:table-row>
						<fo:table-row height="15px">
							<fo:table-cell column-number="2" xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row height="1px">
							<fo:table-cell><fo:block/></fo:table-cell>
						</fo:table-row>
						<fo:table-row height="15px">
							<fo:table-cell column-number="2" xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row height="1px">
							<fo:table-cell><fo:block/></fo:table-cell>
						</fo:table-row>
						<fo:table-row height="15px">
							<fo:table-cell column-number="2" xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell xsl:use-attribute-sets="contract_box_border_style">
								<fo:block/>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				
				<fo:block xsl:use-attribute-sets="IVSF_word_group" line-height="1" space-after="5px" start-indent="10px">
					<fo:external-graphic>
						<xsl:attribute name="src">
							<xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
						</xsl:attribute>
					</fo:external-graphic>
					&#160;See attached - If you require additional space for fund information, attach a separate sheet with all of the information above.
				</fo:block>
				<fo:block xsl:use-attribute-sets="IREP_section_title">
					<fo:block space-after="1px">
						Section B -  Direction for Stable Value Fund Transfer - Refer to the fund's Declaration of Trust and Offering Memorandum for further details
					</fo:block>
					<fo:block space-after="1px">
						If you have elected to eliminate the Stable Value Fund, select one of the following:
					</fo:block>
				</fo:block>
				
				<fo:list-block>
					<fo:list-item space-after="2px">
						<fo:list-item-label start-indent="10px">
							<fo:block>
								<fo:external-graphic>
									<xsl:attribute name="src">
										<xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
									</xsl:attribute>
								</fo:external-graphic>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="25px">
							<fo:block xsl:use-attribute-sets="IVSF_word_group" line-height="1" padding-before="2px">
								A put - If you elect the put option, the existing assets invested in the Stable Value Fund will be subject to a 12-month hold from the effective date. They will not be transferred to the new investment fund that you indicated above. Your Client Account Representative will notify you when the put expires and you can make your selection to transfer the asset to a new fund at that time.
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
					<fo:list-item>
						<fo:list-item-label start-indent="10px">
							<fo:block>
								<fo:external-graphic>
									<xsl:attribute name="src">
										<xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
									</xsl:attribute>
								</fo:external-graphic>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body start-indent="25px">
							<fo:block xsl:use-attribute-sets="IVSF_word_group" line-height="1" padding-before="2px">
								A Market Value Adjustment - Before selecting this option, please contact your Client Account Representative for the current Market Value to Book Value ratio. This ratio will change daily, your applicable ratio will be calculated on the Effective Date of the transfer. Your request will be processed within 5 business days.
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</fo:list-block>
				
				<!-- Authorization Section -->
				<fo:block space-after="3px" space-before="6px" xsl:use-attribute-sets="IREP_section_title">
					Section C - Authorization
				</fo:block>
				
				<fo:block space-after="5px" xsl:use-attribute-sets="IVSF_word_group" line-height="1">
					I hereby agree to the terms and conditions stated above. In addition, I acknowledge that, if one or more of the replacement fund(s) selected above are not already available under the Contract, I have received a description of the investment objectives and policies for each such investment option, and John Hancock <xsl:value-of select="$companyCode"/> is hereby authorized to add them to the Contract.
				</fo:block>
				
				<fo:block space-after="13px" xsl:use-attribute-sets="IVSF_word_group" line-height="1">
					In my role as the authorized fiduciary of the Plan, I agree to assume responsibility for communicating the changes requested on this Form to the Plan participants. In particular, I will inform Plan participants of the changes applicable to their accounts, provide them with information regarding the eliminated funds, the replacement funds, as well as the redemption fee that may be applicable to one or more of such funds. I will also direct them to the John Hancock <xsl:value-of select="$companyCode"/> website or to its toll-free participant phone number if they wish to make alternate selections to the fund replacements requested herein, transfer monies from the eliminated funds, and/or change their ongoing investment instructions. 
				</fo:block>
				
				<fo:block space-after="5px" xsl:use-attribute-sets="IVSF_word_group" line-height="1">
					I hereby represent to John Hancock <xsl:value-of select="$companyCode"/> that I have the authority to make the changes requested on this Form. On behalf of the sponsor of the Plan, the Plan and its related trust, I agree to hold harmless John Hancock <xsl:value-of select="$companyCode"/>, its affiliates, and their agents, employees, officers and directors from and against any and all claims for losses, damages, or penalties for complying with the instructions provided on this Form. I further acknowledge and agree that any participant queries received by John Hancock <xsl:value-of select="$companyCode"/> pertaining to these changes should be directed to the undersigned for clarification.
				</fo:block>
						 	
			 	<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="216px"/>
					<fo:table-column column-width="3px"/>
					<fo:table-column column-width="216px"/>
					<fo:table-column column-width="3px"/>
					<fo:table-column column-width="129px"/>
					<fo:table-body>
						<fo:table-row height="25px">
							<fo:table-cell padding-start="1px" xsl:use-attribute-sets="contract_box_border_style">
								<fo:block xsl:use-attribute-sets="IVSF_authorization_text">
									Signature of Trustee/Authorized Named Fiduciary
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell column-number="3" padding-start="1px" xsl:use-attribute-sets="contract_box_border_style">
								<fo:block xsl:use-attribute-sets="IVSF_authorization_text">
									Name - please print
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell column-number="5" padding-start="1px" xsl:use-attribute-sets="contract_box_border_style">
								<fo:block xsl:use-attribute-sets="IVSF_authorization_text">
									Date
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>