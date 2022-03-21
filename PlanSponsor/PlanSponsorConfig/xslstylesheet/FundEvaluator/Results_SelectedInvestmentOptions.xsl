<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template name="ResultsSelectedInvestmentOptionsTemplate">
        <fo:page-sequence master-reference="ResultsInvestmentOptionsLayout">
            <!-- Header -->
            <fo:static-content flow-name="xsl-region-before" >
                <fo:block space-after="3px">
                    <fo:block-container xsl:use-attribute-sets="header_block_cell1">
                        <fo:block-container  xsl:use-attribute-sets="header_block_cell2">
                            <fo:block>
                            </fo:block>
                        </fo:block-container>
                    </fo:block-container>
                    <fo:retrieve-marker retrieve-class-name="headerCell3" retrieve-position="first-including-carryover" retrieve-boundary="page-sequence"/>
                </fo:block> 
                
            </fo:static-content>
            
            <!-- Footer -->
            <fo:static-content flow-name="rest_page_footer" >
                <fo:table table-layout="fixed" width="100%">
                <fo:table-column column-width="126px"/>
                    <fo:table-body>
                        <fo:table-row height="40px" display-align="after">
                            <fo:table-cell>
                                <fo:block xsl:use-attribute-sets="page_count">
                                    PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminator"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:static-content>

			<fo:static-content flow-name="last_page_footer" >
                <fo:table table-layout="fixed" width="100%">
                <fo:table-column column-width="126px"/>
                <fo:table-column column-width="575px"/>
                    <fo:table-body>
                        <fo:table-row display-align="after">  
                            <fo:table-cell>
                                <fo:block xsl:use-attribute-sets="page_count">
                                    PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminator"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding-left="10px">
                            	<xsl:if test="$hasGAFunds = 'Yes' and $gaDisclosure != ''">
                           		<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" line-height="2" padding-bottom="-2px">
									<xsl:value-of select="$gaDisclosure"/>
                            	</fo:block><fo:block></fo:block>
                            	</xsl:if>
                            			<fo:block xsl:use-attribute-sets="FSW_disclaimer_style" line-height="1" padding-bottom="-2px">
                              			The total revenue John Hancock receives from any Funds advised or sub-advised by John Hancock's affiliates may be higher than those advised or sub-advised by unaffiliated entities. John Hancock's affiliates provide exclusive advisory and sub-advisory services to Funds. John Hancock and its affiliates may receive additional fees which would be included in the underlying fund's Expense Ratio.
                            			</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:static-content>
            
            <fo:flow flow-name="xsl-region-body">
                <!--  Header for First Page -->
                <fo:block keep-with-next="always">
                    <fo:marker marker-class-name="headerCell3">
                        <fo:block-container  xsl:use-attribute-sets="header_block_cell3">
                            <fo:block id="ResultsInvestmentOptionsId" padding-after="4px" start-indent="18px">
                                Asset Class overview
                            </fo:block>
                        </fo:block-container>
                    </fo:marker>
                </fo:block>
                
                <!--  Header for remaining Pages -->
                <fo:block>
                    <fo:marker marker-class-name="headerCell3">
                        <fo:block-container  xsl:use-attribute-sets="header_block_cell3_continued">
                            <fo:block padding-after="4px" start-indent="18px">
                                Asset Class overview
                            </fo:block>
                        </fo:block-container>
                    </fo:marker>
                </fo:block>
                
                
                <!--This loop added for optimization
                    To identify the selected Asset classes in single navigation over the XML
                    Later these variables will be used wherever the condition has to be checked.-->
        <xsl:for-each select="//customization/selectedAssetClasses">
                <!-- Asset classes under Equity asset house -->
                <xsl:variable name="isLCV_Selected">
                    <xsl:if test="assetClasses[assetClass = 'LCV']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isLCB_Selected">
                    <xsl:if test="assetClasses[assetClass = 'LCB']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isLCG_Selected">
                    <xsl:if test="assetClasses[assetClass = 'LCG']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isMCV_Selected">
                    <xsl:if test="assetClasses[assetClass = 'MCV']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isMCB_Selected">
                    <xsl:if test="assetClasses[assetClass = 'MCB']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isMCG_Selected">
                    <xsl:if test="assetClasses[assetClass = 'MCG']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isSCV_Selected">
                    <xsl:if test="assetClasses[assetClass = 'SCV']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isSCB_Selected">
                    <xsl:if test="assetClasses[assetClass = 'SCB']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isSCG_Selected">
                    <xsl:if test="assetClasses[assetClass = 'SCG']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isIGV_Selected">
                    <xsl:if test="assetClasses[assetClass = 'IGV']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isIGB_Selected">
                    <xsl:if test="assetClasses[assetClass = 'IGB']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isIGG_Selected">
                    <xsl:if test="assetClasses[assetClass = 'IGG']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                
                <!-- Asset classes under Hybrid / INDEX / SECTOR asset house -->
                <xsl:variable name="isSPE_Selected">
                    <xsl:if test="assetClasses[assetClass = 'SPE']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isBAL_Selected">
                    <xsl:if test="assetClasses[assetClass = 'BAL']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
		<xsl:variable name="isLSG_Selected">
		    <xsl:if test="assetClasses[assetClass = 'LSG']">
			<xsl:value-of select="'yes'"/>
		    </xsl:if>
		</xsl:variable>
                <xsl:variable name="isSEC_Selected">
                    <xsl:if test="assetClasses[assetClass = 'SEC']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isUserLifeStyleSelectedFunds">
                    <xsl:if test="assetClasses/isFundSelected[@isUserLifeStyleSelectedFunds = 'no']">
                        <xsl:value-of select="'no'"/>
                    </xsl:if>
                </xsl:variable>
                
                  <xsl:variable name="isUserTargetDateSelectedFunds">
                    <xsl:if test="assetClasses/isFundSelected[@isUserTargetDateSelectedFunds = 'no']">
                        <xsl:value-of select="'no'"/>
                    </xsl:if>
                </xsl:variable>
                
                <!-- Asset classes under Fixed income asset house -->
                <xsl:variable name="isHQS_Selected">
                    <xsl:if test="assetClasses[assetClass = 'HQS']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isFXI_Selected">
                    <xsl:if test="assetClasses[assetClass = 'FXI']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isFXL_Selected">
                    <xsl:if test="assetClasses[assetClass = 'FXL']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isMQI_Selected">
                    <xsl:if test="assetClasses[assetClass = 'MQI']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isFXM_Selected">
                    <xsl:if test="assetClasses[assetClass = 'FXM']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isMQL_Selected">
                    <xsl:if test="assetClasses[assetClass = 'MQL']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isLQS_Selected">
                    <xsl:if test="assetClasses[assetClass = 'LQS']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isLQI_Selected">
                    <xsl:if test="assetClasses[assetClass = 'LQI']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isLQL_Selected">
                    <xsl:if test="assetClasses[assetClass = 'LQL']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isGLS_Selected">
                    <xsl:if test="assetClasses[assetClass = 'GLS']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isGLI_Selected">
                    <xsl:if test="assetClasses[assetClass = 'GLI']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isGLL_Selected">
                    <xsl:if test="assetClasses[assetClass = 'GLL']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                
                <!-- Asset classes under GuarenteedAccounts asset house -->
                <xsl:variable name="isGA3_Selected">
                    <xsl:if test="assetClasses[assetClass = 'GA3']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isGA5_Selected">
                    <xsl:if test="assetClasses[assetClass = 'GA5']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                <xsl:variable name="isG10_Selected">
                    <xsl:if test="assetClasses[assetClass = 'G10']">
                        <xsl:value-of select="'yes'"/>
                    </xsl:if>
                </xsl:variable>
                
                <fo:block/>
                
                 <fo:block xsl:use-attribute-sets="word_group_style" font-weight="Light" line-height="1.15" space-after="15px">
					This chart provides an overview of the Funds that were selected for this report and their corresponding Asset Classes.  For additional details about these Funds, see the sections entitled "Investment Option Rankings" and "Performance &amp; Expenses".
                 </fo:block>
                
                <!-- Equity Asset House table starts -->
                    <fo:table table-layout="fixed" width="100%" space-after="27px">     
                        <fo:table-column column-width="701px"/>
                            <!-- Asset House header -->
                        <fo:table-header>
                            <fo:table-row>
                                <fo:table-cell padding-after="3px">
                                    <fo:block xsl:use-attribute-sets="assethouse_group_name">
                                        EQUITY FUNDS
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                            <!-- Asset House body -->
                        <fo:table-body>
                            <fo:table-row keep-with-previous="always">
                                <fo:table-cell>
                                    <fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="assethouse_table_border">
                                        <fo:table-column column-width="4px"/>
                                        <fo:table-column column-width="141px"/>
                                        <fo:table-column column-width="4px"/>
                                        <fo:table-column column-width="180px"/>
                                        <fo:table-column column-width="4px"/>
                                        <fo:table-column column-width="180px"/>
                                        <fo:table-column column-width="4px"/>
                                        <fo:table-column column-width="180px"/>
                                        <fo:table-column column-width="4px"/>
                                            <!-- Asset House Column names -->
                                        <fo:table-header>
                                            <fo:table-row height="4px">
                                                <fo:table-cell number-columns-spanned="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row>
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="4" xsl:use-attribute-sets="assethouse_column" padding-before="1px">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        VALUE
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell column-number="6" xsl:use-attribute-sets="assethouse_column" padding-before="1px">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        BLEND
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell column-number="8" xsl:use-attribute-sets="assethouse_column" padding-before="1px">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        GROWTH
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-header>
                                            <!-- Asset house table rows -->
                                        <fo:table-body>
                                            <fo:table-row height="4px">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row keep-with-previous="always" keep-together="always">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="2" xsl:use-attribute-sets="assethouse_row">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        LARGE CAP
                                                    </fo:block>
                                                </fo:table-cell>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LCV']/funds) &gt; 0">
                                                    <fo:table-cell column-number="4" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isLCV_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LCV']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>                                        
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LCB']/funds) &gt; 0">
                                                    <fo:table-cell column-number="6" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isLCB_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LCB']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LCG']/funds) &gt; 0">
                                                    <fo:table-cell column-number="8" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isLCG_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LCG']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row height="4px">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row keep-with-previous="always" keep-together="always">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="2" xsl:use-attribute-sets="assethouse_row">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        MID CAP
                                                    </fo:block>
                                                </fo:table-cell>

                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='MCV']/funds) &gt; 0">
                                                    <fo:table-cell column-number="4" xsl:use-attribute-sets="assetbox">
                                                                <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isMCV_Selected = 'yes'">
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='MCV']/funds/fund">
                                                                                        <fo:block>
                                                                                            <xsl:variable name="fundId">
                                                                                                <xsl:value-of select="*|text()"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                                <xsl:if test="@isChecked='yes'">
                                                                                                    <xsl:variable name="nameLen">
                                                                                                        <xsl:value-of select="@longName"/>
                                                                                                    </xsl:variable>
                                                                                                    <xsl:choose>
                                                                                                        <xsl:when test="contains($nameLen,'|')">
                                                                                                            <fo:block>
                                                                                                                &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                            <fo:block>
                                                                                                                <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                        </xsl:when>
                                                                                                        <xsl:otherwise>
                                                                                                            &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                        </xsl:otherwise>
                                                                                                    </xsl:choose>
                                                                                                </xsl:if>
                                                                                            </xsl:for-each>
                                                                                        </fo:block>
                                                                                    </xsl:for-each>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        Not selected
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                </fo:block>
                                                    </fo:table-cell>                                    
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='MCB']/funds) &gt; 0">
                                                    <fo:table-cell column-number="6" xsl:use-attribute-sets="assetbox">
                                                                <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isMCB_Selected = 'yes'">
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='MCB']/funds/fund">
                                                                                        <fo:block>
                                                                                            <xsl:variable name="fundId">
                                                                                                <xsl:value-of select="*|text()"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                                <xsl:if test="@isChecked='yes'">
                                                                                                    <xsl:variable name="nameLen">
                                                                                                        <xsl:value-of select="@longName"/>
                                                                                                    </xsl:variable>
                                                                                                    <xsl:choose>
                                                                                                        <xsl:when test="contains($nameLen,'|')">
                                                                                                            <fo:block>
                                                                                                                &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                            <fo:block>
                                                                                                                <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                        </xsl:when>
                                                                                                        <xsl:otherwise>
                                                                                                            &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                        </xsl:otherwise>
                                                                                                    </xsl:choose>
                                                                                                </xsl:if>
                                                                                            </xsl:for-each>
                                                                                        </fo:block>
                                                                                    </xsl:for-each>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        Not selected
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                </fo:block>
                                                    </fo:table-cell>    
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='MCG']/funds) &gt; 0">
                                                    <fo:table-cell column-number="8" xsl:use-attribute-sets="assetbox">
                                                                <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isMCG_Selected = 'yes'">
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='MCG']/funds/fund">
                                                                                        <fo:block>
                                                                                            <xsl:variable name="fundId">
                                                                                                <xsl:value-of select="*|text()"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                                <xsl:if test="@isChecked='yes'">
                                                                                                    <xsl:variable name="nameLen">
                                                                                                        <xsl:value-of select="@longName"/>
                                                                                                    </xsl:variable>
                                                                                                    <xsl:choose>
                                                                                                        <xsl:when test="contains($nameLen,'|')">
                                                                                                            <fo:block>
                                                                                                                &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                            <fo:block>
                                                                                                                <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                        </xsl:when>
                                                                                                        <xsl:otherwise>
                                                                                                            &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                        </xsl:otherwise>
                                                                                                    </xsl:choose>
                                                                                                </xsl:if>
                                                                                            </xsl:for-each>
                                                                                        </fo:block>
                                                                                    </xsl:for-each>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        Not selected
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row height="4px">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row keep-with-previous="always" keep-together="always">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="2" xsl:use-attribute-sets="assethouse_row">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        SMALL CAP
                                                    </fo:block>
                                                </fo:table-cell>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='SCV']/funds) &gt; 0">
                                                      <fo:table-cell column-number="4" xsl:use-attribute-sets="assetbox">
                                                                <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isSCV_Selected = 'yes'">
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='SCV']/funds/fund">
                                                                                        <fo:block>
                                                                                            <xsl:variable name="fundId">
                                                                                                <xsl:value-of select="*|text()"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                                <xsl:if test="@isChecked='yes'">
                                                                                                    <xsl:variable name="nameLen">
                                                                                                        <xsl:value-of select="@longName"/>
                                                                                                    </xsl:variable>
                                                                                                    <xsl:choose>
                                                                                                        <xsl:when test="contains($nameLen,'|')">
                                                                                                            <fo:block>
                                                                                                                &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                            <fo:block>
                                                                                                                <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                        </xsl:when>
                                                                                                        <xsl:otherwise>
                                                                                                            &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                        </xsl:otherwise>
                                                                                                    </xsl:choose>
                                                                                                </xsl:if>
                                                                                            </xsl:for-each>
                                                                                        </fo:block>
                                                                                    </xsl:for-each>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        Not selected
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                </fo:block>
                                                      </fo:table-cell>
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='SCB']/funds) &gt; 0">
                                                      <fo:table-cell column-number="6" xsl:use-attribute-sets="assetbox">
                                                                <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isSCB_Selected = 'yes'">
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='SCB']/funds/fund">
                                                                                        <fo:block>
                                                                                            <xsl:variable name="fundId">
                                                                                                <xsl:value-of select="*|text()"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                                <xsl:if test="@isChecked='yes'">
                                                                                                    <xsl:variable name="nameLen">
                                                                                                        <xsl:value-of select="@longName"/>
                                                                                                    </xsl:variable>
                                                                                                    <xsl:choose>
                                                                                                        <xsl:when test="contains($nameLen,'|')">
                                                                                                            <fo:block>
                                                                                                                &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                            <fo:block>
                                                                                                                <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                        </xsl:when>
                                                                                                        <xsl:otherwise>
                                                                                                            &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                        </xsl:otherwise>
                                                                                                    </xsl:choose>
                                                                                                </xsl:if>
                                                                                            </xsl:for-each>
                                                                                        </fo:block>
                                                                                    </xsl:for-each>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        Not selected
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                </fo:block>
                                                      </fo:table-cell>
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='SCG']/funds) &gt; 0">
                                                      <fo:table-cell column-number="8" xsl:use-attribute-sets="assetbox">
                                                                <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isSCG_Selected = 'yes'">
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='SCG']/funds/fund">
                                                                                        <fo:block>
                                                                                            <xsl:variable name="fundId">
                                                                                                <xsl:value-of select="*|text()"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                                <xsl:if test="@isChecked='yes'">
                                                                                                <xsl:variable name="nameLen">
                                                                                                    <xsl:value-of select="@longName"/>
                                                                                                </xsl:variable>
                                                                                                <xsl:choose>
                                                                                                    <xsl:when test="contains($nameLen,'|')">
                                                                                                        <fo:block>
                                                                                                            &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                        </fo:block>
                                                                                                        <fo:block>
                                                                                                            <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                        </fo:block>
                                                                                                    </xsl:when>
                                                                                                    <xsl:otherwise>
                                                                                                        &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                    </xsl:otherwise>
                                                                                                </xsl:choose>
                                                                                            </xsl:if>
                                                                                            </xsl:for-each>
                                                                                        </fo:block>
                                                                                    </xsl:for-each>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        Not selected
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                </fo:block>
                                                      </fo:table-cell>
                                                </xsl:if>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row height="4px">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                                                    
                                            <fo:table-row keep-with-previous="always" keep-together="always">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="2" xsl:use-attribute-sets="assethouse_row">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        INTERNATIONAL / GLOBAL
                                                    </fo:block>
                                                </fo:table-cell>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='IGV']/funds) &gt; 0">
                                                    <fo:table-cell column-number="4" xsl:use-attribute-sets="assetbox">
                                                                <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isIGV_Selected = 'yes'">
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='IGV']/funds/fund">
                                                                                        <fo:block>
                                                                                            <xsl:variable name="fundId">
                                                                                                <xsl:value-of select="*|text()"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                                <xsl:if test="@isChecked='yes'">
                                                                                                    <xsl:variable name="nameLen">
                                                                                                        <xsl:value-of select="@longName"/>
                                                                                                    </xsl:variable>
                                                                                                    <xsl:choose>
                                                                                                        <xsl:when test="contains($nameLen,'|')">
                                                                                                            <fo:block>
                                                                                                                &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                            <fo:block>
                                                                                                                <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                        </xsl:when>
                                                                                                        <xsl:otherwise>
                                                                                                            &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                        </xsl:otherwise>
                                                                                                    </xsl:choose>
                                                                                                </xsl:if>
                                                                                            </xsl:for-each>
                                                                                        </fo:block>
                                                                                    </xsl:for-each>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        Not selected
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                </fo:block>
                                                    </fo:table-cell>                                        
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='IGB']/funds) &gt; 0">
                                                    <fo:table-cell column-number="6" xsl:use-attribute-sets="assetbox">
                                                                <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                            <xsl:choose>
                                                                                <xsl:when test="$isIGB_Selected = 'yes'">
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='IGB']/funds/fund">
                                                                                        <fo:block>
                                                                                            <xsl:variable name="fundId">
                                                                                                <xsl:value-of select="*|text()"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                                <xsl:if test="@isChecked='yes'">
                                                                                                    <xsl:variable name="nameLen">
                                                                                                        <xsl:value-of select="@longName"/>
                                                                                                    </xsl:variable>
                                                                                                    <xsl:choose>
                                                                                                        <xsl:when test="contains($nameLen,'|')">
                                                                                                            <fo:block>
                                                                                                                &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                            <fo:block>
                                                                                                                <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                            </fo:block>
                                                                                                        </xsl:when>
                                                                                                        <xsl:otherwise>
                                                                                                            &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                        </xsl:otherwise>
                                                                                                    </xsl:choose>
                                                                                                </xsl:if>
                                                                                            </xsl:for-each>
                                                                                        </fo:block>
                                                                                    </xsl:for-each>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                    <fo:block>
                                                                                        Not selected
                                                                                    </fo:block>
                                                                                </xsl:otherwise>
                                                                            </xsl:choose>
                                                                </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='IGG']/funds) &gt; 0">
                                                    <fo:table-cell column-number="8" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isIGG_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='IGG']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>    
                                            </fo:table-row>                                            
                                            <fo:table-row height="4px" keep-with-previous="always">
                                                <fo:table-cell column-number="1" number-columns-spanned="9">
                                                    <fo:block/>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    <!-- Equity Asset House table Ends -->
                    <fo:block/>
                    
                        <!-- SPECIALTY / SECTOR / BALANCED Asset House Starts -->
                    <fo:table table-layout="fixed" width="100%" space-after="27px">
                        <fo:table-column column-width="100%"/>
                        <fo:table-header>
                            <fo:table-row>
                                <fo:table-cell padding-after="4px">
                                    <fo:block xsl:use-attribute-sets="assethouse_group_name">
                                        SPECIALTY / SECTOR / BALANCED
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell >
                                	<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="assethouse_table_border">
                                		<fo:table-column column-width="100%"/>
                                		<fo:table-header><fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row></fo:table-header>
	                                	<fo:table-body>
	                                	<fo:table-row><fo:table-cell>
		                                    <fo:table table-layout="fixed" width="100%" keep-together="always">
		                                        <fo:table-column column-width="4px"/>
		                                        <fo:table-column column-width="229"/>
		                                        <fo:table-column column-width="4px"/>
		                                        <fo:table-column column-width="229px"/>
		                                        <fo:table-column column-width="4px"/>
		                                        <fo:table-column column-width="228px"/>
		                                        <fo:table-column column-width="4px"/>
		                                            
		                                        <fo:table-body>
		                                        	<fo:table-row height="4px">
		                                                <fo:table-cell column-number="1" number-columns-spanned="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row>
		                                            <fo:table-row keep-with-previous="always">
		                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
		                                                <fo:table-cell column-number="2" xsl:use-attribute-sets="assethouse_column">
		                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name" padding-before="1px">
		                                                        SPECIALTY
		                                                    </fo:block>
		                                                </fo:table-cell>
		                                                <fo:table-cell column-number="4" xsl:use-attribute-sets="assethouse_column">
		                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name" padding-before="1px">
		                                                        SECTOR
		                                                    </fo:block>
		                                                </fo:table-cell>
		                                                <fo:table-cell column-number="6" xsl:use-attribute-sets="assethouse_column">
		                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name" padding-before="1px">
		                                                        BALANCED
		                                                    </fo:block>
		                                                </fo:table-cell>
		                                                <fo:table-cell column-number="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row>
		                                            
		                                            <fo:table-row height="4px">
		                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
		                                                <fo:table-cell column-number="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row>
		                                        
		                                            <fo:table-row keep-with-previous="always" keep-together="always">
		                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
		                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='SPE']/funds) &gt; 0">
		                                                    <fo:table-cell column-number="2" xsl:use-attribute-sets="assetbox">
		                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isSPE_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='SPE']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
		                                                        </fo:block>
		                                                    </fo:table-cell>                                        
		                                                </xsl:if>

		                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='SEC']/funds) &gt; 0">
		                                                    <fo:table-cell column-number="4" xsl:use-attribute-sets="assetbox">
		                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isSEC_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='SEC']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
		                                                        </fo:block>
		                                                    </fo:table-cell>
		                                                </xsl:if>
		                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='BAL']/funds) &gt; 0">
		                                                    <fo:table-cell column-number="6" xsl:use-attribute-sets="assetbox">
		                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isBAL_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='BAL']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
		                                                        </fo:block>
		                                                    </fo:table-cell>                                        
		                                                </xsl:if>
		                                                
		                                                <fo:table-cell column-number="7"><fo:block/></fo:table-cell>    
		                                            </fo:table-row>
		                                            
					                                      <fo:table-row height="4px" keep-with-previous="always">
					                                           <fo:table-cell column-number="1" number-columns-spanned="7">
					                                               <fo:block/>
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
                        </fo:table-body>
                    </fo:table>
                    <!-- Hybrid / INDEX / SECTOR Funds Asset House table Ends -->
                    
	                  <!-- ASSET ALLOCATION Asset House Starts -->
                    <fo:table table-layout="fixed" width="100%" space-after="27px">
                        <fo:table-column column-width="100%"/>
                        <fo:table-header>
                            <fo:table-row>
                                <fo:table-cell padding-after="4px">
                                    <fo:block xsl:use-attribute-sets="assethouse_group_name">
                                        ASSET ALLOCATION
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell >
                                	<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="assethouse_table_border">
                                		<fo:table-column column-width="100%"/>
                                		<fo:table-header><fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row></fo:table-header>
	                                	<fo:table-body>
	                                	<fo:table-row><fo:table-cell>
		                                  
		                                    <fo:table  width="100%" >
		                                    	<fo:table-column column-width="4px"/>
		                                        <fo:table-column column-width="229"/>
		                                        <fo:table-column column-width="4px"/>
		                                        <fo:table-column column-width="229"/>
		                                        <fo:table-column column-width="4px"/>
		                                        <fo:table-column column-width="228"/>
		                                        <fo:table-column column-width="4px"/> 
		                                        <fo:table-header>
		                                            <fo:table-row height="4px">
		                                                <fo:table-cell column-number="1" number-columns-spanned="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row>
		                                            <fo:table-row keep-with-previous="always">
		                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
		                                                <fo:table-cell column-number="2" number-columns-spanned="5" xsl:use-attribute-sets="assethouse_column">
		                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name" padding-before="1px">
		                                                        TARGET DATE
		                                                    </fo:block>
		                                                </fo:table-cell>
		                                                <fo:table-cell column-number="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row>
		                                             <fo:table-row height="4px">
		                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
		                                                <fo:table-cell column-number="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row>
		                                        </fo:table-header>
		                                    	<fo:table-body>
		                                    		
		                                            <fo:table-row>
		                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
		                                                <fo:table-cell column-number="2" number-columns-spanned="5" xsl:use-attribute-sets="assetbox">
		                                                   <!-- layOut 1 starts  -->
		                                                   <xsl:for-each select="ancestor::iEvaluatorReport/customization/selectedAssetClasses/assetClasses/fund/layoutOneTargetDate">
		                                                   		<fo:table width="100%" >
		                                                        	<fo:table-body>
		                                                        		<xsl:for-each select="layoutOneRow">
		                                                        		    <fo:table-row height="2px" keep-with-next="always" >
																				<fo:table-cell>
																					<fo:block/> 
																				</fo:table-cell>
																		    </fo:table-row>
		                                                         			<fo:table-row keep-together.within-column="always" padding-before="1px" >
		                                                        	 			<xsl:for-each select="assetAndFamilyCode">
		                                                          					<xsl:variable name="familyCode">
		                                                                    			<xsl:value-of select="@familyCode"/>
		                                                          					</xsl:variable>
		                                                         					 <xsl:variable name="assetCLS">
		                                                                    			<xsl:value-of select="@assetCLS"/>
		                                                          					</xsl:variable>
		                                                          					<fo:table-cell>
		                                                          						<fo:block xsl:use-attribute-sets="assethouse_fund_name">
		                                                          							<xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetCLS]/funds/fund[@fundFamilyCode=$familyCode]">
		                                                                            		<fo:block>
		                                                                                		<xsl:variable name="fundId">
		                                                                                    		<xsl:value-of select="*|text()"/>
		                                                                                     	</xsl:variable>
		                                                                                     	<xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
		                                                                                     		<xsl:if test="@isChecked='yes'">
	                                                                                               		<xsl:variable name="nameLen">
	                                                                                                   		<xsl:value-of select="@longName"/>
	                                                                                               		</xsl:variable>
                                                                                                        <xsl:choose>
                                                                                                           <xsl:when test="contains($nameLen,'|')">
                                                                                                               <fo:block wrap-option="wrap">
                                                                                                                   &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                               </fo:block>
                                                                                                               <fo:block wrap-option="wrap">
                                                                                                                   <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                               </fo:block>
                                                                                                           </xsl:when>
                                                                                                           <xsl:otherwise>
                                                                                                           
                                                                                                           <fo:block wrap-option="wrap">	
																												&#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                           </fo:block>
                                                                                                               
                                                                                                           </xsl:otherwise>
                                                                                                        </xsl:choose>
		                                                                                             </xsl:if>
		                                                                                        </xsl:for-each>
		                                                                                  	</fo:block>
		                                                                             		</xsl:for-each>
		                                                            					</fo:block> 
		                                                            				</fo:table-cell>
		                                                             			</xsl:for-each>
		                                                            	</fo:table-row>
		                                                            	<fo:table-row height="3px" keep-with-previous="always" >
																			<fo:table-cell>
																				<fo:block/> 
																			</fo:table-cell>
																		</fo:table-row> 
		                                                           	</xsl:for-each>
		                                                         </fo:table-body>
		                                                      </fo:table>
		                                                   </xsl:for-each>
		                                                   <!-- layOut 1 ends  -->
		                                                   
		                                                 <xsl:if test="$isUserTargetDateSelectedFunds = 'no'">
		                                                	 <fo:block  xsl:use-attribute-sets="assethouse_fund_name">
		                                                	 	Not selected
		                                                	 </fo:block>
		                                                 </xsl:if>
		                                                </fo:table-cell>
		                                                <fo:table-cell column-number="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row> 
		                                            <fo:table-row height="4px" keep-with-previous="always">
		                                                <fo:table-cell column-number="1" number-columns-spanned="7">
		                                                    <fo:block/>
		                                                </fo:table-cell>
		                                            </fo:table-row>
		                                        </fo:table-body>
		                                    </fo:table>
	                                    </fo:table-cell></fo:table-row>
	                                    
	                                    <fo:table-row><fo:table-cell>
		                                  
		                                    <fo:table  width="100%" >
		                                    	<fo:table-column column-width="4px"/>
		                                        <fo:table-column column-width="229"/>
		                                        <fo:table-column column-width="4px"/>
		                                        <fo:table-column column-width="229"/>
		                                        <fo:table-column column-width="4px"/>
		                                        <fo:table-column column-width="228"/>
		                                        <fo:table-column column-width="4px"/> 
		                                        <fo:table-header>
		                                            <fo:table-row height="4px">
		                                                <fo:table-cell column-number="1" number-columns-spanned="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row>
		                                            <fo:table-row keep-with-previous="always">
		                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
		                                                <fo:table-cell column-number="2" number-columns-spanned="5" xsl:use-attribute-sets="assethouse_column">
		                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name" padding-before="1px">
		                                                         TARGET RISK
		                                                    </fo:block>
		                                                </fo:table-cell>
		                                                <fo:table-cell column-number="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row>
		                                             <fo:table-row height="4px">
		                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
		                                                <fo:table-cell column-number="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row>
		                                        </fo:table-header>
		                                    	<fo:table-body>
		                                    		
		                                            <fo:table-row  keep-with-previous="always">
		                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
		                                                <fo:table-cell column-number="2" number-columns-spanned="5" xsl:use-attribute-sets="assetbox">
		                                                   <!-- layOut 1 starts  -->
		                                                   <xsl:for-each select="ancestor::iEvaluatorReport/customization/selectedAssetClasses/assetClasses/fund/layoutOneLifeStyle">
		                                                   		<fo:table width="100%" >
		                                                        	<fo:table-body>
		                                                        		<xsl:for-each select="layoutOneRow">
			                                                        		<fo:table-row height="2px" keep-with-next="always" >
																				<fo:table-cell>
																					<fo:block/> 
																				</fo:table-cell>
																			</fo:table-row> 
		                                                         			<fo:table-row keep-together.within-column="always" >
		                                                        	 			<xsl:for-each select="assetAndFamilyCode">
		                                                          					<xsl:variable name="familyCode">
		                                                                    			<xsl:value-of select="@familyCode"/>
		                                                          					</xsl:variable>
		                                                         					 <xsl:variable name="assetCLS">
		                                                                    			<xsl:value-of select="@assetCLS"/>
		                                                          					</xsl:variable>
		                                                          					<fo:table-cell>
		                                                          						<fo:block xsl:use-attribute-sets="assethouse_fund_name">
		                                                          							<xsl:choose>
		                                                          								<xsl:when test="$assetCLS = 'LSG' and $isLSG_Selected = 'yes'">
																																								<fo:block font-weight="bold" space-before="5px">
																																									Guaranteed Income Feature
																																								</fo:block>
																																								<xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LSG']/funds/fund">
																																									<fo:block>
																																										<xsl:variable name="fundId">
																																											<xsl:value-of select="*|text()"/>
																																										</xsl:variable>
																																										<xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
																																											<xsl:if test="@isChecked='yes'">
																																												<xsl:variable name="nameLen">
																																													<xsl:value-of select="@longName"/>
																																												</xsl:variable>
																																												<xsl:choose>
																																													<xsl:when test="contains($nameLen,'|')">
																																														<fo:block wrap-option="wrap">
																																															&#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
																																														</fo:block>
																																														<fo:block wrap-option="wrap">
																																															<xsl:value-of select="substring-after($nameLen,'|')" />
																																														</fo:block>
																																													</xsl:when>
																																													<xsl:otherwise>
																																														<fo:block wrap-option="wrap" width="125x">
																																															&#8226;&#160;<xsl:value-of select="$nameLen"/>
																																														</fo:block>
																																													</xsl:otherwise>
																																												</xsl:choose>
																																											</xsl:if>
																																										</xsl:for-each>
																																									</fo:block>
																																								</xsl:for-each>
		                                                          								</xsl:when>
		                                                          								<xsl:otherwise>
				                                                          							<xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id=$assetCLS]/funds/fund[@fundFamilyCode=$familyCode]">
	                                                                            		<fo:block>
	                                                                                		<xsl:variable name="fundId">
	                                                                                    		<xsl:value-of select="*|text()"/>
	                                                                                     	</xsl:variable>
	                                                                                     	<xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
	                                                                                     		<xsl:if test="@isChecked='yes'">
                                                                                               		<xsl:variable name="nameLen">
                                                                                                   		<xsl:value-of select="@longName"/>
                                                                                               		</xsl:variable>
                                                                                                       <xsl:choose>
                                                                                                          <xsl:when test="contains($nameLen,'|')">
                                                                                                              <fo:block wrap-option="wrap">
                                                                                                                  &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                              </fo:block>
                                                                                                              <fo:block wrap-option="wrap">
                                                                                                                  <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                              </fo:block>
                                                                                                          </xsl:when>
                                                                                                          <xsl:otherwise>
                                                                                                          
                                                                                                          <fo:block wrap-option="wrap" width="125x">	
																																																						&#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                          </fo:block>
                                                                                                              
                                                                                                          </xsl:otherwise>
                                                                                                       </xsl:choose>
	                                                                                             </xsl:if>
	                                                                                        </xsl:for-each>
	                                                                                  	</fo:block>
	                                                                             		</xsl:for-each>
		                                                          								</xsl:otherwise>
		                                                          							</xsl:choose>
		                                                            					</fo:block> 
		                                                            				</fo:table-cell>
		                                                             			</xsl:for-each>
		                                                            	</fo:table-row>
																		<fo:table-row height="3px" keep-with-previous="always" >
																			<fo:table-cell>
																				<fo:block/> 
																			</fo:table-cell>
																		</fo:table-row> 
		                                                           	</xsl:for-each>
		                                                         </fo:table-body>
		                                                      </fo:table>
		                                                   </xsl:for-each>
		                                                   <!-- layOut 1 ends  -->
		                                                   
		                                                <xsl:if test="$isUserLifeStyleSelectedFunds = 'no' and $isLSG_Selected != 'yes'">
		                                                	 <fo:block  xsl:use-attribute-sets="assethouse_fund_name">
		                                                	 	Not selected
		                                                	 </fo:block>
		                                                 </xsl:if>
		                                                </fo:table-cell>
		                                                <fo:table-cell column-number="7"><fo:block/></fo:table-cell>
		                                            </fo:table-row> 
		                                            <fo:table-row height="4px" keep-with-previous="always">
		                                                <fo:table-cell column-number="1" number-columns-spanned="7">
		                                                    <fo:block/>
		                                                </fo:table-cell>
		                                            </fo:table-row>
		                                        </fo:table-body>
		                                    </fo:table>
	                                    </fo:table-cell></fo:table-row>
	                                    
	                                   </fo:table-body>
                                    </fo:table>
	                            </fo:table-cell>
	                         </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                     <fo:block/>
                    <!-- ASSET ALLOCATION Funds Asset House table Ends -->
		                                                
                    <!-- FIXED INCOME FUNDS Asset House table Starts-->
                    <fo:table table-layout="fixed" width="100%" space-after="27px">     
                        <fo:table-column column-width="701px"/>
                            <!-- Asset House header -->
                        <fo:table-header>
                            <fo:table-row>
                                <fo:table-cell padding-after="4px">
                                    <fo:block xsl:use-attribute-sets="assethouse_group_name">
                                        FIXED INCOME FUNDS
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                            <!-- Asset House body -->
                        <fo:table-body>
                            <fo:table-row keep-with-previous="always">
                                <fo:table-cell>
                                    <fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="assethouse_table_border">
                                        <fo:table-column column-width="4px"/>
                                        <fo:table-column column-width="141px"/>
                                        <fo:table-column column-width="4px"/>
                                        <fo:table-column column-width="180px"/>
                                        <fo:table-column column-width="4px"/>
                                        <fo:table-column column-width="180px"/>
                                        <fo:table-column column-width="4px"/>
                                        <fo:table-column column-width="180px"/>
                                        <fo:table-column column-width="4px"/>   
                                            <!-- Asset house column names -->
                                        <fo:table-header>
                                            <fo:table-row height="4px">
                                                <fo:table-cell column-number="1" number-columns-spanned="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row keep-with-previous="always">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="4" xsl:use-attribute-sets="assethouse_column">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name" padding-before="1px">
                                                        SHORT TERM
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell column-number="6" xsl:use-attribute-sets="assethouse_column" padding-before="1px">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        INTERMEDIATE TERM
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell column-number="8" xsl:use-attribute-sets="assethouse_column" padding-before="1px">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        LONG TERM
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-header>                  
                                            <!-- Asset house table rows -->
                                        <fo:table-body>
                                            <fo:table-row height="4px">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                        
                                            <fo:table-row keep-with-previous="always" keep-together="always">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="2" xsl:use-attribute-sets="assethouse_row">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        HIGH QUALITY
                                                    </fo:block>
                                                </fo:table-cell>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='HQS']/funds) &gt; 0">
                                                    <fo:table-cell column-number="4" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isHQS_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='HQS']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>                                        
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='FXI']/funds) &gt; 0">
                                                    <fo:table-cell column-number="6" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isFXI_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='FXI']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='FXL']/funds) &gt; 0">
                                                    <fo:table-cell column-number="8" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isFXL_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='FXL']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row height="4px">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row keep-together="always" keep-with-previous="always">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="2" xsl:use-attribute-sets="assethouse_row">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        MEDIUM QUALITY
                                                    </fo:block>
                                                </fo:table-cell>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='MQI']/funds) &gt; 0">
                                                    <fo:table-cell column-number="4" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isMQI_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='MQI']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>                                        
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='FXM']/funds) &gt; 0">
                                                    <fo:table-cell column-number="6" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isFXM_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='FXM']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>                                                                                                            
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='MQL']/funds) &gt; 0">
                                                    <fo:table-cell column-number="8" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isMQL_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='MQL']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row height="4px">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row keep-together="always" keep-with-previous="always">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="2" xsl:use-attribute-sets="assethouse_row">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        LOW QUALITY
                                                    </fo:block>
                                                </fo:table-cell>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LQS']/funds) &gt; 0">
                                                    <fo:table-cell column-number="4" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isLQS_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LQS']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>                                        
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LQI']/funds) &gt; 0">
                                                    <fo:table-cell column-number="6" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isLQI_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LQI']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LQL']/funds) &gt; 0">
                                                    <fo:table-cell column-number="8" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isLQL_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='LQL']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row height="4px">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row keep-together="always" keep-with-previous="always">
                                                <fo:table-cell column-number="1"><fo:block/></fo:table-cell>
                                                <fo:table-cell column-number="2" xsl:use-attribute-sets="assethouse_row">
                                                    <fo:block xsl:use-attribute-sets="assethouse_column_row_name">
                                                        GLOBAL
                                                    </fo:block>
                                                </fo:table-cell>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='GLS']/funds) &gt; 0">
                                                    <fo:table-cell column-number="4" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isGLS_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='GLS']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>                                        
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='GLI']/funds) &gt; 0">
                                                    <fo:table-cell column-number="6" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isGLI_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='GLI']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <xsl:if test="count(ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='GLL']/funds) &gt; 0">
                                                    <fo:table-cell column-number="8" xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isGLL_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='GLL']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <fo:table-cell column-number="9"><fo:block/></fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row height="4px" keep-with-previous="always">
                                                <fo:table-cell column-number="1" number-columns-spanned="9">
                                                    <fo:block/>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    <!-- FIXED INCOME FUNDS Asset House table ends-->
                    
                    
                    <xsl:if test="$hasGAFunds = 'Yes'">
                    
                    	<xsl:variable name="numberOfGAFunds">
							<xsl:choose>
								<xsl:when test ="$hasGA3Funds = 'Yes' and $hasGA5Funds = 'Yes' and $hasGA10Funds = 'Yes'">
									<xsl:value-of select="'3'"/>
								</xsl:when>
								<xsl:when test ="($hasGA3Funds = 'Yes' and $hasGA5Funds = 'Yes') or 
									($hasGA3Funds = 'Yes' and $hasGA10Funds = 'Yes') or 
									($hasGA5Funds = 'Yes' and $hasGA10Funds = 'Yes')">
									<xsl:value-of select="'2'"/>
								</xsl:when>
								<xsl:otherwise>
	                                 <xsl:value-of select="'1'"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
                                        
                    <!-- Guaranteed Accounts Asset House Starts-->
                    <fo:table table-layout="fixed" width="100%">
                        <fo:table-column column-width="145px"/>
                        <fo:table-column column-width="556px"/>
                        <fo:table-body>
                                <!-- Asset House Header -->
                            <fo:table-row keep-with-next="always">
                                <fo:table-cell column-number="2">
                                    <fo:block xsl:use-attribute-sets="assethouse_group_name" padding-after="3px">
                                        GUARANTEED ACCOUNTS^
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                                <!-- Asset House Rows -->
                            <fo:table-row>
                                <fo:table-cell column-number="2">
                                    <fo:table table-layout="fixed" width="100%">
                                    	<fo:table-column column-width="4px"/>
                                        <fo:table-column column-width="180px"/>
                                    	<xsl:if test="number($numberOfGAFunds) &gt; 1">
                                        	<fo:table-column column-width="4px"/>
                                        	<fo:table-column column-width="180px"/>
                                        </xsl:if>
                                        <xsl:if test="number($numberOfGAFunds) &gt; 2">
	                                        <fo:table-column column-width="4px"/>
	                                        <fo:table-column column-width="180px"/>
                                        </xsl:if>
                                        <fo:table-column column-width="4px"/>
                                        <fo:table-body xsl:use-attribute-sets="assethouse_table_border">
                                            <fo:table-row height="4px">
                                                <fo:table-cell column-number="1" number-columns-spanned="{number($numberOfGAFunds) * 2 + 1}">
                                                    <fo:block/>
                                                </fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row keep-with-previous="always">
                                                <xsl:if test="$hasGA3Funds = 'Yes'">
                                                	<fo:table-cell><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isGA3_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='GA3']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>        
                                                </xsl:if>
                                                <xsl:if test="$hasGA5Funds = 'Yes'">
                                                	<fo:table-cell><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isGA5_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='GA5']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>    
                                                </xsl:if>
                                                <xsl:if test="$hasGA10Funds = 'Yes'">
                                                    <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                                    <fo:table-cell xsl:use-attribute-sets="assetbox">
                                                        <fo:block xsl:use-attribute-sets="assethouse_fund_name">
                                                                    <xsl:choose>
                                                                        <xsl:when test="$isG10_Selected = 'yes'">
                                                                            <xsl:for-each select="ancestor::iEvaluatorReport/fundsByAssetClass/assetHouses/assetHouse/assetClasses/assetClass[@id='G10']/funds/fund">
                                                                                <fo:block>
                                                                                    <xsl:variable name="fundId">
                                                                                        <xsl:value-of select="*|text()"/>
                                                                                    </xsl:variable>
                                                                                    <xsl:for-each select="ancestor::iEvaluatorReport/fundDetails/fund[@id=$fundId]">
                                                                                        <xsl:if test="@isChecked='yes'">
                                                                                            <xsl:variable name="nameLen">
                                                                                                <xsl:value-of select="@longName"/>
                                                                                            </xsl:variable>
                                                                                            <xsl:choose>
                                                                                                <xsl:when test="contains($nameLen,'|')">
                                                                                                    <fo:block>
                                                                                                        &#8226;&#160;<xsl:value-of select="substring-before($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                    <fo:block>
                                                                                                        <xsl:value-of select="substring-after($nameLen,'|')" />
                                                                                                    </fo:block>
                                                                                                </xsl:when>
                                                                                                <xsl:otherwise>
                                                                                                    &#8226;&#160;<xsl:value-of select="$nameLen"/>
                                                                                                </xsl:otherwise>
                                                                                            </xsl:choose>
                                                                                        </xsl:if>
                                                                                    </xsl:for-each>
                                                                                </fo:block>
                                                                            </xsl:for-each>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:block>
                                                                                Not selected
                                                                            </fo:block>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </xsl:if>
                                                <fo:table-cell><fo:block></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row height="4px" keep-with-previous="always">
                                                <fo:table-cell column-number="1" number-columns-spanned="{number($numberOfGAFunds) * 2 + 1}">
                                                    <fo:block/>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    </xsl:if>
                    <!-- Guaranteed Accounts Asset House Ends-->
                </xsl:for-each>
            </fo:flow>
            
        </fo:page-sequence>
    </xsl:template>

</xsl:stylesheet> 