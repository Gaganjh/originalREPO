<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template name="InvestmentSelectionFormTemplate">
        <!-- Information Page -->
        <fo:page-sequence master-reference="InformationPageLayout" force-page-count="no-force">
            <!-- Header -->
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
                                Information for completing the Investment Selection Form
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row height="26px" border-bottom-style="solid" border-bottom-width="0.5px">
                        <fo:table-cell number-columns-spanned="2">
                            <fo:block/>
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
                                    <xsl:when test="$companyCode = 'USA'">GP3517-FE (04/2009)</xsl:when>
                                    <xsl:otherwise>GP3517-NY-FE (04/2009)</xsl:otherwise>
                                </xsl:choose>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell padding-before="2px">
                            <fo:block xsl:use-attribute-sets="IVSF_footer_text">
                                © 2011 John Hancock. All rights reserved.
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
                </fo:table>
            </fo:static-content>
            
            <fo:flow flow-name="xsl-region-body">
                <fo:block xsl:use-attribute-sets="information_page_word_group">
                    <fo:block space-after="11px">
                        Contributions under a group annuity contract issued by John Hancock Life Insurance Company (
                        <xsl:choose>
                            <xsl:when test="$companyCode = 'USA'">U.S.A.</xsl:when>
                            <xsl:otherwise>New York</xsl:otherwise>
                        </xsl:choose>
                        ) ("John Hancock <xsl:value-of select="$companyCode"/>") may be allocated to investment options that are: (a) sub-accounts ("Funds") and which invest solely in shares of an underlying mutual fund or collected trust, or (b) Guaranteed Interest Accounts ("Guaranteed Accounts").
                    </fo:block>
                    <fo:block space-after="11px">
						Use this form to select investment options to offer your participants under your plan's contract. You may elect to offer all or only some of the investment options available.
						<xsl:if test="//fundLineUp/includesGIFL = 'yes'">
							<xsl:choose>
								<xsl:when test="$contractNumber = ''">
									The Funds for Guaranteed Income for Life Select can not be added with this form. Please refer to the Recordkeeping Agreement (RKA) if you wish to add Guaranteed Income for Life Select to your contract.
								</xsl:when>
								<xsl:otherwise>
									The Funds for Guaranteed Income for Life Select can not be added with this form. To change the Funds you have available with Guaranteed Income for Life Select, you must complete an Investment Replacement Funds - Guaranteed Income for Life Select form.
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						Prior to making any selections, it's important to carefully review the information in the Fund Information Guide, available from your John Hancock <xsl:value-of select="$companyCode"/> representative, which provides detailed information on the investment objectives, policies, fees, expense ratios and investment returns for all of the investment options available. For information on all investment options available to your plan, visit our website 
                        <fo:inline xsl:use-attribute-sets="website_address_style">
                            <xsl:choose>
                                <xsl:when test="$companyCode = 'USA'">www.jhpensions.com/er</xsl:when>
                                <xsl:otherwise>www.jhnypensions.com/er</xsl:otherwise>
                            </xsl:choose>.
                        </fo:inline>
                    </fo:block>
                    <fo:block space-after="11px">   
                        The placement of investment options according to investment categories and potential risk/return shows John Hancock <xsl:value-of select="$companyCode"/>'s assessment of those options relative to one another and should not be used to compare these investment options with other investment options available outside of a John Hancock <xsl:value-of select="$companyCode"/> group annuity contract. John Hancock <xsl:value-of select="$companyCode"/> determines peer groups and indexes based on what we believe is the closest match in terms of investment objectives, policies, processes and style. Each investment option's investment category and risk/return spectrum placement is subject to change. Moreover, there can be no assurance that any investment option will experience less volatility than another. This information is not investment advice.
                    </fo:block>
                    <fo:block space-after="11px">   
                        When selecting investment options, it is important to note that allocating assets to only one or a small number of the investment options (other than Target Date and Lifestyle options) should not be considered a balanced investment program. In particular, allocating assets to a small number of options concentrated in particular business or market sectors may subject participants to increased risk and volatility. Examples of business or market sectors where this risk may be particularly high include: a) technology-related businesses, including Internet-related businesses, b) small cap securities, and c) foreign securities.
                    </fo:block>
                    <fo:block space-after="11px">   
                        Investment options which are shaded are only available under certain limited circumstances. Please consult your John Hancock <xsl:value-of select="$companyCode"/> representative to determine if your contract includes these investment options.
                    </fo:block>
                    <fo:block space-after="11px">   
                        Please forward your properly completed and signed form to your client account representative. Your changes will take effect on the same market day if John Hancock <xsl:value-of select="$companyCode"/> receives it properly completed and signed, and accompanied by any additional completed and signed forms as applicable, on or before the close of the New York Stock Exchange on a market day. If received after that time, the changes will be processed on the next market day. You may also specify a future date for changes.
                    </fo:block>
                    <fo:block space-after="35px">   
                        If you have any questions and for more information, please call our toll-free service line at 1-800-
                        <xsl:choose>
                            <xsl:when test="$companyCode = 'USA'">333-0963</xsl:when>
                            <xsl:otherwise>574-5557</xsl:otherwise>
                        </xsl:choose>
                         and speak with your John Hancock <xsl:value-of select="$companyCode"/> client account representative.
                    </fo:block>
                    <fo:block text-align="center" font-weight="bold" font-size="11pt">
                        Continued on next page
                    </fo:block>
                    
                    
                    <fo:list-block>
                        <fo:list-item space-after="15px">
                            <fo:list-item-label>
                                <fo:block font-weight="bold">
                                    A.
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="10px">
                                <fo:block>
                                    <fo:block font-weight="bold" keep-with-next="always">
                                        Important Information regarding FUNDS
                                    </fo:block>
                                    <fo:list-block>
                                        <fo:list-item space-after="6px" space-before="4px">
                                            <fo:list-item-label>
                                                <fo:block start-indent="23px">
                                                    &#8226;
                                                </fo:block>
                                            </fo:list-item-label>
                                            <fo:list-item-body start-indent="33px">
                                                <fo:block>
                                                    Some fund companies charge redemption fees for fund shares sold within a specified period of time. Please review the Fund Information Guide or visit our website for more information. In addition, inter-account transfers are subject to our short-term trading policy, which is described in our latest administrative guidelines, available at 
                                                    <fo:inline xsl:use-attribute-sets="website_address_style">
                                                        <xsl:choose>
                                                            <xsl:when test="$companyCode = 'USA'">www.jhpensions.com/er</xsl:when>
                                                            <xsl:otherwise>www.jhnypensions.com/er</xsl:otherwise>
                                                        </xsl:choose>.
                                                    </fo:inline>
                                                </fo:block>
                                            </fo:list-item-body>
                                        </fo:list-item>
                                        <fo:list-item space-after="6px">
                                            <fo:list-item-label>
                                                <fo:block start-indent="23px">
                                                    &#8226;
                                                </fo:block>
                                            </fo:list-item-label>
                                            <fo:list-item-body start-indent="33px">
												<fo:block>By selecting a suite of Target Date Portfolios, all portfolios associated with that suite are added to your contract. Each Target Date Portfolio has an associated target date based on the year in which participants plan to retire and cease making contributions.  The asset mix of each Target Date Portfolio ‘glides’ over time, with a goal of keeping the asset allocation appropriate based on the portfolio’s investment strategy and its target date. Portfolios with dates farther in the future are invested more aggressively, with a larger allocation to equities. As the portfolios approach and pass the target date, they are invested more conservatively, with an increased allocation fixed income.  The principal value of a participant’s investment in these Portfolios is not guaranteed at any time, including at or after the target date.</fo:block>
                                            </fo:list-item-body>
                                        </fo:list-item>
                                        <fo:list-item space-after="6px">
                                            <fo:list-item-label>
                                                <fo:block start-indent="23px">
                                                    &#8226;
                                                </fo:block>
                                            </fo:list-item-label>
                                            <fo:list-item-body start-indent="33px">
                                                <fo:block>
                                                    To designate or change default investment option(s) to be used to allocate contributions when there are missing participant allocation instructions, you must also complete a Default Investment Selection form - GP5090
                                                    <xsl:choose>
                                                        <xsl:when test="$companyCode = 'USA'">US</xsl:when>
                                                        <xsl:otherwise>NY</xsl:otherwise>
                                                    </xsl:choose>.
                                                </fo:block>
                                            </fo:list-item-body>
                                        </fo:list-item>
                                    </fo:list-block>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                        <fo:list-item space-after="15px">
                            <fo:list-item-label>
                                <fo:block font-weight="bold">
                                       B.
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="10px">
                                <fo:block>
                                    <fo:block font-weight="bold" keep-with-next="always">
                                        Important Information regarding John Hancock STABLE VALUE FUND
                                    </fo:block>
                                    <fo:list-block>
                                        <fo:list-item space-after="6px" space-before="4px">
                                            <fo:list-item-label>
                                                <fo:block start-indent="23px">
                                                    &#8226;
                                                </fo:block>
                                            </fo:list-item-label>
                                            <fo:list-item-body start-indent="33px">
                                                <fo:block>
                                                    To add this Fund, existing plans must also submit a completed Investment Change-Stable Value Fund form - GP4767
                                                    <xsl:choose>
                                                        <xsl:when test="$companyCode = 'USA'">US</xsl:when>
                                                        <xsl:otherwise>NY</xsl:otherwise>
                                                    </xsl:choose>
                                                    . It is important to review this form, prior to adding the Stable Value Fund.
                                                </fo:block>
                                            </fo:list-item-body>
                                        </fo:list-item>
                                    </fo:list-block>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </fo:list-block>
                </fo:block> 
                
            </fo:flow>
        </fo:page-sequence>
            
            <!-- Investment Selection Form -->
        <fo:page-sequence master-reference="InvestmentSelectionFormLayout" initial-page-number="1">
            
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
                                    Investment Selection Form by Asset Class
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row height="26px">
                            <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5px">
                                <fo:block>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell display-align="after" border-bottom-style="solid" border-bottom-width="0.5px">
                                <fo:block xsl:use-attribute-sets="IVSF_form_instruction">
                                    <fo:retrieve-marker retrieve-class-name="FormInstruction" retrieve-position="first-starting-within-page" retrieve-boundary="page"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
                
                <fo:table space-before="4px" table-layout="fixed" width="100%">
                    <fo:table-column column-width="60%"/>
                    <fo:table-column column-width="40%"/>
                    <fo:table-body>
                        <fo:table-row height="10px" keep-with-previous="always">
                            <fo:table-cell padding-start="1px" padding-right="8px" xsl:use-attribute-sets="contract_box_border_style">
                                <fo:block space-after="2px" xsl:use-attribute-sets="information_page_word_group" font-size="7pt">
                                    Contractholder name (employer)
                                </fo:block>
                                <fo:block-container overflow="hidden" wrap-option="no-wrap">
                                <fo:block xsl:use-attribute-sets="IREP_section_title" border-top-style="none" start-indent="50px">
                                    <xsl:value-of select="$companyName"/>
                                </fo:block>
                                </fo:block-container>
                            </fo:table-cell>
                            
                            <fo:table-cell padding-start="1px" xsl:use-attribute-sets="contract_box_border_style">
                                <fo:block space-after="2px" xsl:use-attribute-sets="information_page_word_group" font-size="7pt">
                                    Contract number
                                </fo:block>
                                <fo:block xsl:use-attribute-sets="IREP_section_title" border-top-style="none" start-indent="50px">
                                    <xsl:value-of select="$contractNumber"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
                
                 <fo:block space-before="3px">
                    <fo:retrieve-marker retrieve-class-name="SubSection" retrieve-position="first-starting-within-page" retrieve-boundary="page"/>
                    <fo:block xsl:use-attribute-sets="investment_options_box">
                        <fo:retrieve-marker retrieve-class-name="InvestmentOptions" retrieve-position="first-including-carryover" retrieve-boundary="page-sequence"/>                       
                    </fo:block>
                 </fo:block>
            </fo:static-content>
            
            <fo:static-content flow-name="blank_last_page_header" >
                
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
                                    Investment Selection Form by Asset Class
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row height="26px">
                            <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5px">
                                <fo:block>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell display-align="after" border-bottom-style="solid" border-bottom-width="0.5px">
                                <fo:block xsl:use-attribute-sets="IVSF_form_instruction">
                                    <fo:retrieve-marker retrieve-class-name="FormInstruction" retrieve-position="first-starting-within-page" retrieve-boundary="page"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
                        
                <fo:table space-before="4px" table-layout="fixed" width="100%">
                    <fo:table-column column-width="60%"/>
                    <fo:table-column column-width="40%"/>
                    <fo:table-body>
                        <fo:table-row height="10px" keep-with-previous="always">
                            <fo:table-cell padding-start="1px" padding-right="8px" xsl:use-attribute-sets="contract_box_border_style">
                                <fo:block space-after="2px" xsl:use-attribute-sets="information_page_word_group" font-size="7pt">
                                    Contractholder name (employer)
                                </fo:block>
                                <fo:block-container overflow="hidden" wrap-option="no-wrap">
                                <fo:block xsl:use-attribute-sets="IREP_section_title" border-top-style="none" start-indent="50px">
                                    <xsl:value-of select="$companyName"/>
                                </fo:block>
                                </fo:block-container>
                            </fo:table-cell>
                            
                            <fo:table-cell padding-start="1px" xsl:use-attribute-sets="contract_box_border_style">
                                <fo:block space-after="2px" xsl:use-attribute-sets="information_page_word_group" font-size="7pt">
                                    Contract number
                                </fo:block>
                                <fo:block xsl:use-attribute-sets="IREP_section_title" border-top-style="none" start-indent="50px">
                                    <xsl:value-of select="$contractNumber"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:static-content>

            <!-- Footer -->
            <fo:static-content flow-name="xsl-region-after">
                <fo:block xsl:use-attribute-sets="IVSF_disclosure_text" space-after="3px" line-height="1"> 
                    <fo:list-block>
                        <fo:list-item>
                            <fo:list-item-label start-indent="3px">
                                <fo:block>
                                    <fo:inline baseline-shift="sub">*</fo:inline>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="9px">
                                <fo:block>
                                     None of the Index Funds or the underlying portfolios are sponsored, endorsed, managed, advised, sold or promoted by any of the respective companies that sponsor the broad-based securities market index, and none of these companies make any representation regarding the advisability of investing in the Index Fund.
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </fo:list-block>
                </fo:block>
                
                <fo:table table-layout="fixed" width="100%">
                    <fo:table-column column-width="50%"/>
                    <fo:table-column column-width="50%"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5px">
                                <fo:block xsl:use-attribute-sets="IVSF_footer_text">
                                    <xsl:choose>
                                        <xsl:when test="$companyCode = 'USA'">GP3517-FE (09/2010)</xsl:when>
                                        <xsl:otherwise>GP3517-NY-FE (09/2010)</xsl:otherwise>
                                    </xsl:choose>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5px">
                                <fo:block xsl:use-attribute-sets="IVSF_footer_text" font-size="7pt" text-align="right">
                                    PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminatorISF"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell padding-before="2px">
                                <fo:block xsl:use-attribute-sets="IVSF_footer_text">
                                    © 2011 John Hancock. All rights reserved.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:static-content>
            
            <fo:static-content flow-name="last_page_footer">
                <fo:block xsl:use-attribute-sets="IVSF_disclosure_text" space-after="3px" line-height="1"> 
                    <fo:list-block>
                        <fo:list-item>
                            <fo:list-item-label start-indent="3px">
                                <fo:block>
                                    <fo:inline baseline-shift="sub">*</fo:inline>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="9px">
                                <fo:block>
                                     None of the Index Funds or the underlying portfolios are sponsored, endorsed, managed, advised, sold or promoted by any of the respective companies that sponsor the broad-based securities market index, and none of these companies make any representation regarding the advisability of investing in the Index Fund.
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </fo:list-block>
                </fo:block>
                
                <!-- Authorization Section  -->
                <fo:block space-before="18px" space-after="2px" keep-with-next="always" id="terminatorISF">
                    <fo:block xsl:use-attribute-sets="investment_options_box" space-after="2px">
                        Authorization                       
                    </fo:block>
                    
                    <fo:block xsl:use-attribute-sets="IVSF_word_group" space-after="2px" keep-with-next="always">
                        I acknowledge that I have received a description of the investment objectives and policies for each investment option that I have selected. I understand that the value of an investment in John Hancock <xsl:value-of select="$companyCode"/>'s sub-accounts, including Lifestyle Funds, will increase or decrease to reflect the investment experience of the underlying securities and, when redeemed, may be worth more or less than the original cost. Past performance is no guarantee of future results and John Hancock <xsl:value-of select="$companyCode"/> does not guarantee these values.
                    </fo:block>
                    
                    <fo:table table-layout="fixed" width="100%">
                        <fo:table-column column-width="210px"/>
                        <fo:table-column column-width="13px"/>
                        <fo:table-column column-width="45px"/>
                        <fo:table-column column-width="28px"/>
                        <fo:table-column column-width="190px"/>
                        <fo:table-column column-width="10px"/>
                        <fo:table-column column-width="72px"/>
                        <fo:table-body>
                            <fo:table-row height="25px" keep-with-previous="always">
                                <fo:table-cell padding-start="1px" xsl:use-attribute-sets="contract_box_border_style" >
                                    <fo:block xsl:use-attribute-sets="IVSF_authorization_text">
                                        Signed at &#160;&#160;&#160;&#160;&#160;City<fo:leader leader-pattern="space" leader-length="100px"/>State
                                    </fo:block>
                                </fo:table-cell>
                                
                                <fo:table-cell column-number="3" padding-start="1px" xsl:use-attribute-sets="contract_box_border_style">
                                    <fo:block xsl:use-attribute-sets="IVSF_authorization_text">
                                        This
                                    </fo:block>
                                </fo:table-cell>
                                
                                <fo:table-cell column-number="5" padding-start="1px" xsl:use-attribute-sets="contract_box_border_style">
                                    <fo:block xsl:use-attribute-sets="IVSF_authorization_text">
                                        Day of
                                    </fo:block>
                                </fo:table-cell>
                                
                                <fo:table-cell column-number="7" padding-start="1px" xsl:use-attribute-sets="contract_box_border_style">
                                    <fo:block xsl:use-attribute-sets="IVSF_authorization_text">
                                        Year
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row height="3px">
                                <fo:table-cell>
                                    <fo:block/>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row height="25px" keep-with-previous="always">
                                <fo:table-cell padding-start="1px" xsl:use-attribute-sets="contract_box_border_style" number-columns-spanned="3">
                                    <fo:block xsl:use-attribute-sets="IVSF_authorization_text">
                                        Signature of trustee/authorized named fiduciary
                                    </fo:block>
                                </fo:table-cell>
                                
                                <fo:table-cell column-number="5" padding-start="1px" xsl:use-attribute-sets="contract_box_border_style" number-columns-spanned="3">
                                    <fo:block xsl:use-attribute-sets="IVSF_authorization_text">
                                        Name - please print
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:block>
                
                <fo:table table-layout="fixed" width="100%">
                    <fo:table-column column-width="50%"/>
                    <fo:table-column column-width="50%"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5px">
                                <fo:block xsl:use-attribute-sets="IVSF_footer_text">
                                    <xsl:choose>
                                        <xsl:when test="$companyCode = 'USA'">GP3517-FE (09/2010)</xsl:when>
                                        <xsl:otherwise>GP3517-NY-FE (09/2010)</xsl:otherwise>
                                    </xsl:choose>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5px">
                                <fo:block xsl:use-attribute-sets="IVSF_footer_text" font-size="7pt" text-align="right">
                                    PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminatorISF"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell padding-before="2px">
                                <fo:block xsl:use-attribute-sets="IVSF_footer_text">
                                    © 2011 John Hancock. All rights reserved.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:static-content>
        
            <fo:flow flow-name="xsl-region-body">
                
                <!-- markers for header -->
                <fo:block keep-with-next="always">
                    <fo:marker marker-class-name="FormInstruction">
                        <fo:block>
                            &#8226;&#160;&#160;To complete this form, please read the instruction page attached to this form
                        </fo:block>
                    </fo:marker>
                    
                    <fo:marker marker-class-name="SubSection">
                        <fo:block xsl:use-attribute-sets="IVSF_word_group" space-after="3px">
                            <fo:block space-after="5px">
                                <fo:inline font-weight="bold">Initial Fund Selection</fo:inline> - Enrollment forms giving participant investment direction must accompany the initial contribution. Check investment options to be included in the group annuity contract.
                            </fo:block>
                            <fo:block space-after="2px">
                                <fo:inline font-weight="bold">Add Funds Later</fo:inline> - Investment options added will be in addition to those already selected. Your new selections will be effective on the date on which John Hancock <xsl:value-of select="$companyCode"/> receives this request. If the Fund or Fund(s) provided for under your group annuity contract are not on this form, please check with your client account representative. Check investment options to be added later.
                            </fo:block>
                            <fo:block space-after="3px">
                                <fo:inline font-weight="bold">Delete Funds from current Fund Selection</fo:inline> - To delete Funds from current Fund selections, please complete the Investment Replacement Funds form GP4973
                                <xsl:choose>
                                    <xsl:when test="$companyCode = 'USA'">US</xsl:when>
                                    <xsl:otherwise>NY</xsl:otherwise>
                                </xsl:choose>.
                            </fo:block>
                            
                            <fo:block font-weight="bold" background-color="#DEDEDE" padding-before="3px" border-top-style="solid" border-top-width="0.5px">
                                Investment option(s) which are shaded are only available under certain limited circumstances. Please contact your John Hancock <xsl:value-of select="$companyCode"/> representative to determine if your contract includes this investment option.
                            </fo:block>
                        </fo:block>
                    </fo:marker>
                    
                    <fo:marker marker-class-name="InvestmentOptions">
                        INVESTMENT OPTIONS
                    </fo:marker>
                </fo:block>
                
                <fo:block>
                    <fo:marker marker-class-name="InvestmentOptions">INVESTMENT OPTIONS (continued)</fo:marker>
                </fo:block>
                
                <!-- Document Flow starts -->
                    
                <!--looping over 4 Asset house Ids like EQU, HYB, FXI, GUA -->
                <xsl:for-each select="fundsByAssetClass/assetHouses/assetHouse">

                    <!-- looping over Asset Classes of each Asset Group -->
                    <xsl:for-each select="assetClasses/assetClass">
                    <xsl:if test="@id != 'LSG' and @id != 'GA3' and @id != 'GA5' and @id != 'G10' ">	<!-- GIFL funds should not be shown -->                                          
                      <xsl:if test="count(fundsBySortOrder) &gt; 0">
                        <fo:table space-after="8px" table-layout="fixed" width="100%">
                            <fo:table-column column-width="30px"/>
                            <fo:table-column column-width="30px"/>
                            <fo:table-column column-width="20px"/>
                            <fo:table-column column-width="200px"/>
                            
                            <!-- Aseet Class Headers  -->
                            <fo:table-header>
                                <fo:table-row>
                                    <fo:table-cell number-columns-spanned="4">
                                        <fo:block xsl:use-attribute-sets="IVSF_asset_class_name">
                                            <xsl:value-of select="@name" />
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>

                            <fo:table-body xsl:use-attribute-sets="IVSF_asset_box">
                                <!-- Below choose selects between LCF type table row vs normal table row.
                                     LCF table row should not show each fund info.
                                     normal table rows have 4 cells each -->
                                <xsl:choose>
                                    <xsl:when test="@id ='LCF'">
                                        <fo:table-row keep-together.within-column = "always">
                                            <fo:table-cell xsl:use-attribute-sets="IVSF_asset_box_cell">
                                                <fo:block text-align="center">
                                                    <xsl:choose>
                                                        <xsl:when test="ancestor::iEvaluatorReport/customization/selectedAssetClasses/assetClasses/assetClass[@isRLSelected = 'yes']">
                                                            <fo:external-graphic>
                                                                <xsl:attribute name="src">
                                                                    <xsl:value-of select="concat($imagePath,'checkedPlainCheckboxSmall.gif')"/>
                                                                </xsl:attribute>
                                                            </fo:external-graphic>
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <fo:external-graphic>
                                                                <xsl:attribute name="src">
                                                                    <xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
                                                                </xsl:attribute>
                                                            </fo:external-graphic>
                                                        </xsl:otherwise>
                                                    </xsl:choose>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell column-number="2" number-columns-spanned="3">
                                                <fo:block xsl:use-attribute-sets="IVSF_disclosure_text" font-style="normal" line-height="1" padding-before="1px">
                                                    <fo:inline font-size="9pt" font-weight="normal">Retirement Living Portfolios - </fo:inline>
													<fo:block>Plans that select a suite of Target Date Portfolios will automatically add all portfolios associated with that suite to the plan. Each Retirement Living Portfolio has an associated target date based on the year in which participants plan to retire and no longer make contributions.  These Portfolios are designed to take participants through retirement. The Portfolios are managed with increased equity exposure providing more potential growth and income opportunity to take participants through their years living in retirement.  The principal value of a participant’s investment in these Portfolios is not guaranteed at any time, including at or after the target date.</fo:block>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                        
                                        <fo:table-row height="5px">
                                        	<fo:table-cell number-columns-spanned="4" xsl:use-attribute-sets="IVSF_asset_box_cell">
                                        		<fo:block/>
                                        	</fo:table-cell>
                                        </fo:table-row>
                                        
                                        <fo:table-row keep-together.within-column = "always">
                                            <fo:table-cell xsl:use-attribute-sets="IVSF_asset_box_cell">
                                                <fo:block text-align="center">
                                                    <xsl:choose>
                                                        <xsl:when test="ancestor::iEvaluatorReport/customization/selectedAssetClasses/assetClasses/assetClass[@isRCSelected = 'yes']">
                                                            <fo:external-graphic>
                                                                <xsl:attribute name="src">
                                                                    <xsl:value-of select="concat($imagePath,'checkedPlainCheckboxSmall.gif')"/>
                                                                </xsl:attribute>
                                                            </fo:external-graphic>
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <fo:external-graphic>
                                                                <xsl:attribute name="src">
                                                                    <xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
                                                                </xsl:attribute>
                                                            </fo:external-graphic>
                                                        </xsl:otherwise>
                                                    </xsl:choose>
                                                </fo:block>
                                            </fo:table-cell>
                                            
                                            <fo:table-cell column-number="2" number-columns-spanned="3">
                                                <fo:block xsl:use-attribute-sets="IVSF_disclosure_text" font-style="normal" line-height="1" padding-before="1px">
                                                    <fo:inline font-size="9pt" font-weight="normal">Retirement Choices Portfolios - </fo:inline>
													<fo:block>Plans that select a suite of Target Date Portfolios will automatically add all portfolios associated with that suite to the plan. Each Retirement Choices Portfolio has an associated target date based on the year in which participants plan to retire and no longer make contributions.  These Portfolios are designed to take participants to their retirement. The Portfolios are managed to help deliver lower volatility through reduced equity exposure in the years leading up to the target date, giving participants the opportunity to consider another investment strategy that may be better suited to meeting their income needs for their years in retirement.  The principal value of a participant’s investment in these Portfolios is not guaranteed at any time, including at or after the target date.</fo:block>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </xsl:when>
                                    
                                    <xsl:otherwise>
                                        <!-- Each row containing fund info -->
                                        <!-- looping over all funds within an Asset Classes -->
                                        <xsl:for-each select="fundsBySortOrder/fund">
                                            <xsl:variable name="fundId">
                                                <xsl:value-of select="*|text()"/>
                                            </xsl:variable>
                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                <fo:table-row keep-together.within-column = "always">
                                                    <xsl:attribute name="display-align">
                                                        <xsl:choose>
                                                            <xsl:when test="$fundId ='MSV' or $fundId ='NMY'">
                                                                <xsl:value-of select="'before'"/>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <xsl:value-of select="'center'"/>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:attribute>
                                                        
                                                    <fo:table-cell xsl:use-attribute-sets="IVSF_asset_box_cell">
                                                        <fo:block xsl:use-attribute-sets="IVSF_word_group" text-align="center">
                                                            <xsl:value-of select="@id" />
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell xsl:use-attribute-sets="IVSF_asset_box_cell">
                                                        <fo:block xsl:use-attribute-sets="IVSF_word_group" text-align="center">
                                                            <xsl:value-of select="@fundCode" />
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    
                                                    <fo:table-cell xsl:use-attribute-sets="IVSF_asset_box_cell">
                                                        <fo:block xsl:use-attribute-sets="IVSF_word_group" text-align="center">
                                                            <xsl:choose>
                                                                <xsl:when test="@isChecked = 'yes'">
                                                                    <fo:external-graphic>
                                                                        <xsl:attribute name="src">
                                                                            <xsl:value-of select="concat($imagePath,'checkedPlainCheckboxSmall.gif')"/>
                                                                        </xsl:attribute>
                                                                    </fo:external-graphic>
                                                                </xsl:when>
                                                                <xsl:otherwise>
                                                                    <fo:external-graphic>
                                                                        <xsl:attribute name="src">
                                                                            <xsl:value-of select="concat($imagePath,'plaincheckboxSmall.gif')"/>
                                                                        </xsl:attribute>
                                                                    </fo:external-graphic>
                                                                </xsl:otherwise>
                                                            </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    
                                                    <fo:table-cell>
                                                        <xsl:choose>
                                                            <!-- Stable value funds must be shaded and includes a disclaimer. -->
                                                            <xsl:when test="$fundId ='MSV' or $fundId ='NMY'">
                                                                <fo:block background-color="#DEDEDE" xsl:use-attribute-sets="IVSF_disclosure_text" font-style="normal" line-height="1">
                                                                    <fo:inline font-size="9pt" font-weight="normal"><xsl:value-of select="@name" /> - </fo:inline>
                                                                    Plans that select the Stable Value Fund are restricted from selecting any fixed-income investment options that are deemed to be ‘Competing’. These 'Competing' Funds are marked with **. Existing Plans must submit a completed "Investment Change - Stable Value Fund" form.
                                                                </fo:block>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <fo:block xsl:use-attribute-sets="IVSF_word_group">
                                                                    <xsl:choose>
                                                                        <!-- Closed to New Business funds must be shaded -->
                                                                        <xsl:when test="@isClosedToNB ='yes'">
                                                                            <fo:block background-color="#DEDEDE">
                                                                                <xsl:if test="@isIndex = 'yes'">
                                                                                    *
                                                                                </xsl:if>
                                                                                <xsl:value-of select="@name" />
                                                                            </fo:block>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                <xsl:if test="@isIndex = 'yes'">
                                                                                    *
                                                                                </xsl:if>
                                                                                <xsl:value-of select="@name" />
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                                </fo:block>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </xsl:for-each> 
                                        </xsl:for-each>
                                    </xsl:otherwise>
                                </xsl:choose>
                            <!--End of each row containing fund info -->
                            </fo:table-body>
                        </fo:table>
                     </xsl:if> <!-- end if (check if the asset class has no funds -->
                    </xsl:if>
                        <!--End loop : looping Asset Classes of each Asset Group-->
                    </xsl:for-each>
        
                    <!--End loop : looping over all Asset Houses -->
                </xsl:for-each>
                
            </fo:flow>
        </fo:page-sequence>
    </xsl:template>
</xsl:stylesheet>