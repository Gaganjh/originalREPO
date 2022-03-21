<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:attribute-set name="table-borderstyle">
	</xsl:attribute-set>

	<xsl:attribute-set name="solid.blue.border">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="solid.blue.border.3sides">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="solid.blue.border.4sides">
		<xsl:attribute name="border-start-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-end-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-end-style">solid</xsl:attribute>
		<xsl:attribute name="border-end-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-after-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-after-style">solid</xsl:attribute>
		<xsl:attribute name="border-after-width">0.1mm</xsl:attribute>
		<xsl:attribute name="border-before-color">#cac8c4</xsl:attribute>
		<xsl:attribute name="border-before-style">solid</xsl:attribute>
		<xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="arial_font.size_10">
		<xsl:attribute name="font-family">Arial</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="verdana_font.size_10">
		<xsl:attribute name="font-family">Verdana</xsl:attribute>
		<xsl:attribute name="font-size">10pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="fonts_group1.size_11">
		<xsl:attribute name="font-family">Verdana, Arial, Helvetica, sans-serif</xsl:attribute>
		<xsl:attribute name="font-size">11pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="fonts_group2.size_12">
		<xsl:attribute name="font-family">Arial, Helvetica, sans-serif</xsl:attribute>
		<xsl:attribute name="font-size">12pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:include href="reportCommonHeader.xsl" />
	<xsl:include href="reportCommonFooter.xsl" />
	<xsl:include href="pageDefinition.xsl" />
	<xsl:template match="withdrawalDetail">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- 8.5x11 page.-->
			<xsl:call-template name="pageDefinition" />
			<fo:page-sequence master-reference="pageLandscapeLayout">
				<fo:static-content flow-name="xsl-region-after">
					<fo:block font-weight="bold" xsl:use-attribute-sets="arial_font.size_10">
						Page
						<fo:page-number />
						of
						<fo:page-number-citation-last ref-id="terminator" />
						NOT VALID WITHOUT ALL PAGES
					</fo:block>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="reportCommonHeader" />

					<fo:block   space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="60%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="40%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm"
											border-before-style="solid" border-before-color="#febe10" />
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-start="0.3cm"
										padding-after="0.3cm">
										<fo:block padding-before="0.3cm"
											xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro1Text" />
										</fo:block>
										<fo:block padding-before="0.3cm"
											xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="intro2Text" />
										</fo:block>
										<fo:block padding-before="0.3cm"
											xsl:use-attribute-sets="fonts_group2.size_12">
											<xsl:apply-templates select="rothMsg" />
										</fo:block>
									</fo:table-cell>
									<!-- Showing Summary Information -->
									<fo:table-cell padding-start="0.3cm">
										<fo:block padding-start="0.3cm" padding-before="0.2cm"
											xsl:use-attribute-sets="fonts_group1.size_11" font-weight="bold"
											background-color="#eceae3">
											<xsl:apply-templates select="summaryInfo/subHeader" />
										</fo:block>
										<fo:block padding-start="0.3cm" padding-before="0.2cm"
											xsl:use-attribute-sets="fonts_group1.size_11"
											background-color="#eceae3">
											<fo:inline >
												Payment Amount:  
												</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/paymentAmount" />
											</fo:inline>
										</fo:block>
										
										<fo:block padding-start="0.3cm"
											xsl:use-attribute-sets="fonts_group1.size_11"
											background-color="#eceae3">
											<fo:inline>
												Type of Withdrawal:
											</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/transactionTypeDescription" />
											</fo:inline>
										</fo:block>
										<xsl:if test="commonIndicator/showLiaWithdrawalMessage = 'true'">
											<fo:block padding-start="0.3cm"
												xsl:use-attribute-sets="fonts_group1.size_11"
												background-color="#eceae3" font-weight="bold">
													<xsl:value-of select="commonIndicator/liaWithdrawalMessage" />
											</fo:block>
										</xsl:if>
										<xsl:if test="summaryInfo/name">
											<fo:block padding-start="0.3cm"
												xsl:use-attribute-sets="fonts_group1.size_11"
												background-color="#eceae3" padding-before="0.2cm">
												<fo:inline>
													Name:
											</fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/name" />
												</fo:inline>
											</fo:block>
										</xsl:if>
										<xsl:if test="summaryInfo/ssn">
											<fo:block padding-start="0.3cm" 
												xsl:use-attribute-sets="fonts_group1.size_11"
												background-color="#eceae3">
												<fo:inline>
													SSN:
											</fo:inline>
												<fo:inline font-weight="bold">
													<xsl:value-of select="summaryInfo/ssn" />
												</fo:inline>
											</fo:block>
										</xsl:if>
										<fo:block padding-start="0.3cm"
											xsl:use-attribute-sets="fonts_group1.size_11"
											background-color="#eceae3" padding-before="0.2cm" >
											<fo:inline>
												Withdrawal Date:
												</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/withdrawalDate" />
											</fo:inline>
										</fo:block>
										<fo:block padding-start="0.3cm"
											xsl:use-attribute-sets="fonts_group1.size_11"
											background-color="#eceae3">
											<fo:inline>
												Transaction Number:
												</fo:inline>
											<fo:inline font-weight="bold">
												<xsl:value-of select="summaryInfo/transactionNumber" />
											</fo:inline>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="100%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm"
											border-before-style="solid" border-before-color="#febe10" />
										<!--
											orange color
										-->
									</fo:table-cell>
								</fo:table-row>

								<fo:table-row background-color="#deded8"
									display-align="left">
									<fo:table-cell padding-start="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt">
											<xsl:apply-templates select="bodyHeader2" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block background-color="#f5f4f0" >
					<xsl:if test="commonIndicator/loanDefault">
					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="6%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="90%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="6%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell><fo:block/></fo:table-cell>
									<fo:table-cell   background-color="#ffffe6" padding-start="1.0cm"  xsl:use-attribute-sets="solid.blue.border.4sides" padding-before="0.5cm">
										<fo:block    space-before="0.2cm" border-before-width="5cm" padding-after="0.2cm" >
												<fo:inline font-weight="bold" >
												Notification:
												</fo:inline>
												<fo:inline>
												<xsl:value-of select="commonIndicator/loanDefault" />
											</fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell><fo:block/></fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					</xsl:if>
					
					<xsl:if test="commonIndicator/pbaInd">
					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="6%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="90%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="6%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell><fo:block/></fo:table-cell>
									<fo:table-cell   background-color="#ffffe6" padding-start="1.0cm"  xsl:use-attribute-sets="solid.blue.border.4sides" padding-before="0.5cm">
										<fo:block    space-before="0.2cm" border-before-width="5cm" padding-after="0.2cm" >
												<fo:inline font-weight="bold" >
												Notification:
												</fo:inline>
												<fo:inline>
												<xsl:value-of select="commonIndicator/pbaDefault" />
											</fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell><fo:block/></fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					</xsl:if>
					
					<xsl:if test="commonIndicator/showLiaWithdrawalNotification = 'true'">
					<fo:block space-before="0.5cm">
						<fo:table>
							<fo:table-column column-width="6%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="90%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="6%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell><fo:block/></fo:table-cell>
									<fo:table-cell   background-color="#ffffe6" padding-start="1.0cm"  xsl:use-attribute-sets="solid.blue.border.4sides" padding-before="0.5cm">
										<fo:block    space-before="0.2cm" border-before-width="5cm" padding-after="0.2cm" >
												<fo:inline font-weight="bold" >
												Notification:
												</fo:inline>
												<fo:inline>
												<xsl:value-of select="commonIndicator/liaWithdrawalNotification" />
											</fo:inline>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell><fo:block/></fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					</xsl:if>
					<xsl:variable name="designRothContrb">
							<xsl:value-of select="commonIndicator/designRoth" />
						</xsl:variable>
						<xsl:variable name="designPre87Roth">
							<xsl:value-of select="commonIndicator/pre87Roth" />
						</xsl:variable>
					<xsl:if test="$designRothContrb != '' or $designPre87Roth != '' " >
					<fo:block space-before="0.5cm">
					<fo:table>
						<fo:table-column column-width="60%"
							xsl:use-attribute-sets="table-borderstyle" />
						<fo:table-column column-width="40%"
							xsl:use-attribute-sets="table-borderstyle" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding-start="0.3cm">
									<fo:block  xsl:use-attribute-sets="fonts_group2.size_12">
										<xsl:if test="commonIndicator/designRoth">
										<fo:inline font-weight="bold" >
												1st Year of Designated Roth Contributions :
												</fo:inline>
												<fo:inline>
												<xsl:value-of select="commonIndicator/designRoth" />
											</fo:inline>
											</xsl:if>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
								<fo:table-row>
								<fo:table-cell padding-start="0.3cm" padding-after="0.3cm">
									<fo:block  xsl:use-attribute-sets="fonts_group2.size_12">
									<xsl:if test="commonIndicator/pre87Roth">
										<fo:inline font-weight="bold" >
												Pre-87 After Tax Employee Contributions Withdrawn:
												</fo:inline>
												<fo:inline>
												<xsl:value-of select="commonIndicator/pre87Roth" />
											</fo:inline>
											</xsl:if>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:block>
				</xsl:if>
				
					<xsl:variable name="webVersion">
							<xsl:value-of select="commonIndicator/webVersion" />
						</xsl:variable>
						
					<fo:block space-before="0.5cm">
						<fo:table>
						<fo:table-header>
						<fo:table-row background-color="#deded8" display-align="left">
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block  padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
										Money Type
							</fo:block>
							</fo:table-cell>
							
							<fo:table-cell padding-start="0.1cm"  xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block  padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:if test="$webVersion = 'V1'" >
									Account Balance($)	
									</xsl:if>
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm"  xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block  padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
									Vesting(%)
							</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.1cm"  xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block  padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
									Available Amount($)
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<xsl:if test="$webVersion = 'V2'" >
							<fo:table-cell padding-start="0.1cm"  xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block  padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
									Withdrawal Amount($)
							</fo:block>
							</fo:table-cell>
							</xsl:if>
						</fo:table-row>
						</fo:table-header>
						<fo:table-body>
						<xsl:for-each select="withdrawalTxn" >
						<fo:table-row  background-color="#f5f4f0" display-align="left">
							<fo:table-cell padding-start="0.1cm"  xsl:use-attribute-sets="solid.blue.border" text-align="left">
							<fo:block  padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="moneyType" />
							</fo:block>
							</fo:table-cell>
							
							<fo:table-cell padding-start="0.1cm"  xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:if test="$webVersion = 'V1'" >
								<xsl:value-of select="accountBalance" />
								</xsl:if>
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="vestingPercentage" />
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="availableAmount" />
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:for-each>
						<fo:table-row  background-color="#f5f4f0" display-align="left">
							<fo:table-cell padding-start="0.1cm"  xsl:use-attribute-sets="solid.blue.border" text-align="left">
							<fo:block  padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<fo:leader leader-pattern="space" leader-length="0.3cm" />
							</fo:block>
							</fo:table-cell>
							
							<fo:table-cell padding-start="0.1cm"  xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<fo:leader leader-pattern="space" leader-length="0.3cm" />
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<fo:leader leader-pattern="space" leader-length="0.3cm" />
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<fo:leader leader-pattern="space" leader-length="0.3cm" />
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<xsl:variable name="rothInd">
							<xsl:value-of select="withdrawalCalc/rothMoneyTpeInd" />
						</xsl:variable>
						<xsl:variable name="mvaInd">
							<xsl:value-of select="withdrawalCalc/mvaInd" />
						</xsl:variable>
						<fo:table-row  background-color="#f5f4f0" display-align="left">
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides"  text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:if test="$webVersion = 'V1'" >
								Total Available Amount
								</xsl:if>
								<xsl:if test="$webVersion = 'V2'" >
								Total Withdrawal Amount
								</xsl:if>
							</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="withdrawalCalc/totalAmt" />
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row background-color="#f5f4f0" display-align="left">
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<fo:leader leader-pattern="space" />
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border"  text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<fo:leader leader-pattern="space" />
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							
							<fo:table-cell   padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:if test="$mvaInd= 'Y'" >
								Market Value Adjustment(MVA)
								</xsl:if>
							</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:if test="$mvaInd= 'Y'" >
								<xsl:value-of select="withdrawalCalc/mvaAmount" />
								</xsl:if>
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<xsl:if test="withdrawalCalc/fundsOnDepositInterest">
							<fo:table-row background-color="#f5f4f0" display-align="left">
								<fo:table-cell padding-start="0.1cm"
									xsl:use-attribute-sets="solid.blue.border" text-align="center">
									<fo:block padding-before="0.1cm" padding-after="0.1cm"
										xsl:use-attribute-sets="verdana_font.size_10">
									</fo:block>
								</fo:table-cell>
								<xsl:if test="$webVersion = 'V1'">
									<fo:table-cell padding-start="0.1cm"
										xsl:use-attribute-sets="solid.blue.border" text-align="right">
										<fo:block padding-before="0.1cm" padding-after="0.1cm"
											xsl:use-attribute-sets="verdana_font.size_10">
							
										</fo:block>
									</fo:table-cell>
								</xsl:if>
								<fo:table-cell padding-start="0.1cm"
									xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
									<fo:block padding-before="0.1cm" padding-after="0.1cm"
										xsl:use-attribute-sets="verdana_font.size_10">
										Funds On Deposit Interest
														</fo:block>
								</fo:table-cell>
								<fo:table-cell padding-start="0.1cm"
									xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
									<fo:block padding-before="0.1cm" padding-after="0.1cm"
										xsl:use-attribute-sets="verdana_font.size_10">
										<xsl:value-of select="withdrawalCalc/fundsOnDepositInterest" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							</xsl:if>
						<xsl:if test="withdrawalCalc/taxableAmount">
						<fo:table-row  background-color="#f5f4f0" display-align="left">
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								Taxable Amount
							</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="withdrawalCalc/taxableAmount" />
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						
						<xsl:if test="withdrawalCalc/serviceTaxAmount">
						<fo:table-row background-color="#f5f4f0" display-align="left">		
											<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								State Tax
							</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="withdrawalCalc/serviceTaxAmount" />
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						
						<xsl:if test="withdrawalCalc/federalTaxAmount">
						<fo:table-row background-color="#f5f4f0" display-align="left">
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								Federal Tax
							</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="withdrawalCalc/federalTaxAmount" />
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						
						<xsl:if test="$rothInd= 'Y'" >
						<xsl:if test="withdrawalCalc/rothTaxableAmount">
						<fo:table-row background-color="#f5f4f0"  display-align="left">
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								Taxable Amount - ROTH
							</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="withdrawalCalc/rothTaxableAmount" />
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						
						<xsl:if test="withdrawalCalc/rothServiceTaxAmount">
						<fo:table-row background-color="#f5f4f0"  display-align="left">
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								State Tax - ROTH
							</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="withdrawalCalc/rothServiceTaxAmount" />
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						
						<xsl:if test="withdrawalCalc/rothFederalTaxAmount">
						<fo:table-row background-color="#f5f4f0"  display-align="left">
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm"  xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								Federal Tax - ROTH
							</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="withdrawalCalc/rothFederalTaxAmount" />
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:if>
						</xsl:if>
						
						
						<fo:table-row background-color="#f5f4f0"  display-align="left">
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
							</fo:block>
							</fo:table-cell>
							<xsl:if test="$webVersion = 'V1'" >
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								
							</fo:block>
							</fo:table-cell>
							</xsl:if>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides"  text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								Total Payment Amount
							</fo:block>
							</fo:table-cell>
							<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
							<fo:block padding-before="0.1cm" padding-after="0.1cm"
								xsl:use-attribute-sets="verdana_font.size_10">
								<xsl:value-of select="withdrawalCalc/totalPaymentAmount" />
							</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<xsl:if test="withdrawalCalc/forfeitedOrUMText1" >
							<fo:table-row background-color="#f5f4f0"  display-align="left">
								<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="center">
								<fo:block padding-before="0.1cm" padding-after="0.1cm"
									xsl:use-attribute-sets="verdana_font.size_10">
								</fo:block>
								</fo:table-cell>
								<xsl:if test="$webVersion = 'V1'" >
								<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border" text-align="right">
								<fo:block padding-before="0.1cm" padding-after="0.1cm"
									xsl:use-attribute-sets="verdana_font.size_10">
									
								</fo:block>
								</fo:table-cell>
								</xsl:if>
								<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides"  text-align="right">
								<fo:block padding-before="0.1cm" padding-after="0.1cm"
									xsl:use-attribute-sets="verdana_font.size_10">
									<xsl:value-of select="withdrawalCalc/forfeitedOrUMText1" />
								</fo:block>
								<fo:block padding-before="0.1cm" padding-after="0.1cm"
									xsl:use-attribute-sets="verdana_font.size_10">
									<xsl:value-of select="withdrawalCalc/forfeitedOrUMText2" />
								</fo:block>
								</fo:table-cell>
								<fo:table-cell padding-start="0.1cm" xsl:use-attribute-sets="solid.blue.border.3sides" text-align="right">
								<fo:block padding-before="0.1cm" padding-after="0.1cm"
									xsl:use-attribute-sets="verdana_font.size_10">
									<xsl:value-of select="withdrawalCalc/forfeitedOrUnvestedERAmount" />
								</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
						
					  </fo:table-body>
					</fo:table>
				  </fo:block>
				</fo:block>
					<fo:block >
						<fo:table>
							<fo:table-column column-width="100%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block border-before-width="0.1cm"
											border-before-style="solid" border-before-color="#febe10" />
										<!--
											orange color
										-->
									</fo:table-cell>
								</fo:table-row>

								<fo:table-row background-color="#deded8"
									display-align="left">
									<fo:table-cell padding-start="0.2cm">
										<fo:block font-family="Verdana" font-size="14pt">
											<xsl:apply-templates select="bodyHeader1" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block   background-color="#f5f4f0">
					<!-- <xsl:if test="commonIndicator/multiPayee">
						<fo:block space-before="0.5cm">
							<fo:table>
								<fo:table-column column-width="6%"
								xsl:use-attribute-sets="table-borderstyle" />
								<fo:table-column column-width="90%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-column column-width="6%" xsl:use-attribute-sets="table-borderstyle"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell><fo:block/></fo:table-cell>
										<fo:table-cell   background-color="#ffffe6" padding-start="1.0cm"  xsl:use-attribute-sets="solid.blue.border.4sides" padding-before="0.5cm">
											<fo:block    space-before="0.2cm" border-before-width="5cm" padding-after="0.2cm" >
												<fo:inline font-weight="bold" >
													Notification:
													</fo:inline>
													<fo:inline >
													<xsl:value-of select="commonIndicator/multiPayee" />
													</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell><fo:block/></fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
						</xsl:if> -->
						<fo:table>
							<fo:table-column column-width="1%"
								xsl:use-attribute-sets="table-borderstyle" />
							<fo:table-column column-width="95%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-column column-width="4%" xsl:use-attribute-sets="table-borderstyle"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell ><fo:block/></fo:table-cell>
									<fo:table-cell  padding-start="0.2cm"  padding-before="0.5cm">
										<xsl:for-each  select="payeeDetails" >
											<fo:block   padding-start="0.3cm"   xsl:use-attribute-sets="fonts_group2.size_12"  >
											<xsl:if test="payeeId"><fo:inline font-weight="bold" >
												<xsl:value-of select="payeeId" />. 
												</fo:inline>
											</xsl:if>
											<fo:inline  font-weight="bold" >
													Payee Name: 
											</fo:inline>
											<fo:inline >
													<fo:leader leader-pattern="space" leader-length="0.3cm" />
											</fo:inline>
											<fo:inline ><xsl:value-of select="payeeName" />
											</fo:inline>
											</fo:block>
											<fo:block   padding-start="1.0cm"   xsl:use-attribute-sets="fonts_group1.size_11"  >
											<xsl:if test="payeeId"><fo:inline >
													<fo:leader leader-pattern="space" leader-length="0.3cm" />
											</fo:inline></xsl:if>
											<fo:inline  font-weight="bold" >
													Payment Amount: 
											</fo:inline>
											<fo:inline ><xsl:value-of select="paymentAmount" />
											</fo:inline>
											</fo:block>
											<fo:block  padding-start="1.0cm"  xsl:use-attribute-sets="fonts_group1.size_11" >
											<xsl:if test="payeeId"><fo:inline >
													<fo:leader leader-pattern="space" leader-length="0.3cm" />
											</fo:inline></xsl:if>
												<fo:inline font-weight="bold">
													Payment Method:
											</fo:inline><fo:inline><xsl:value-of select="paymentMethod" /></fo:inline>
											</fo:block>
											<fo:block  padding-start="1.0cm"   xsl:use-attribute-sets="fonts_group1.size_11"   >
											<xsl:if test="payeeId"><fo:inline >
													<fo:leader leader-pattern="space" leader-length="0.3cm" />
											</fo:inline></xsl:if>
												<fo:inline font-weight="bold">
													Payment To:
											</fo:inline><fo:inline><xsl:value-of select="paymentTo" />
											</fo:inline>
											</fo:block>
											<fo:block  xsl:use-attribute-sets="fonts_group2.size_12"   space-before="0.7cm" >
												<xsl:if test="payeeId"><fo:inline >
													<fo:leader leader-pattern="space"  leader-length="0.3cm"/>
													</fo:inline></xsl:if>
												<fo:inline color= "#607782" >
													Payee Address
											</fo:inline>
											</fo:block>
											<fo:block  xsl:use-attribute-sets="fonts_group1.size_11"  space-before="0.3cm" >
												<xsl:if test="payeeId"><fo:inline >
													<fo:leader leader-pattern="space"  leader-length="0.4cm" />
												</fo:inline></xsl:if>
												<fo:inline>
													 <xsl:value-of select="payeeName" /><fo:leader leader-pattern="space" leader-length="0.1cm"/>
												</fo:inline>
											</fo:block>
											<fo:block   xsl:use-attribute-sets="fonts_group1.size_11" >
												<xsl:if test="payeeId"><fo:inline >
													<fo:leader leader-pattern="space" leader-length="0.4cm" />
												</fo:inline></xsl:if>
												<fo:inline>
													 <xsl:value-of select="addressLine1" /><fo:leader leader-pattern="space" leader-length="0.1cm"/>
												</fo:inline>
											</fo:block>
											<fo:block  xsl:use-attribute-sets="fonts_group1.size_11" >
												<xsl:if test="payeeId"><fo:inline font-weight="bold" color="#990000">
													<fo:leader leader-pattern="space" leader-length="0.4cm" />
												</fo:inline></xsl:if>
												<fo:inline>
													 <xsl:value-of select="addressLine2" /><fo:leader leader-pattern="space" leader-length="0.1cm"/>
												</fo:inline>
												</fo:block>
											<fo:block  xsl:use-attribute-sets="fonts_group1.size_11"  >
												<xsl:if test="payeeId"><fo:inline font-weight="bold" color="#990000">
													<fo:leader leader-pattern="space" leader-length="0.4cm" />
												</fo:inline></xsl:if>
												<xsl:if test="city">
													<fo:inline>
														 <xsl:value-of select="city" />,<fo:leader leader-pattern="space" leader-length="0.1cm"/> 
													</fo:inline>
												</xsl:if>
												<xsl:if test="state">
													<fo:inline><xsl:value-of select="state" /> 
														<fo:leader leader-pattern="space" leader-length="0.1cm"/>
													</fo:inline>
												</xsl:if>
												<fo:inline>
													<xsl:value-of select="zip" /><fo:leader leader-pattern="space" leader-length="0.1cm"/>
												</fo:inline>
											</fo:block>
											<fo:block  xsl:use-attribute-sets="fonts_group1.size_11" >
												<xsl:if test="payeeId"><fo:inline font-weight="bold" color="#990000">
													<fo:leader leader-pattern="space" leader-length="0.4cm" />
												</fo:inline></xsl:if>
												<fo:inline>
													 <xsl:value-of select="country" /><fo:leader leader-pattern="space" leader-length="0.1cm"/>
												</fo:inline>
												</fo:block>
											<fo:block  xsl:use-attribute-sets="fonts_group2.size_12" space-before="0.7cm" >
												<xsl:if test="payeeId"><fo:inline font-weight="bold" color="#990000">
													<fo:leader leader-pattern="space" leader-length="0.3cm" />
												</fo:inline></xsl:if>
												<fo:inline color= "#607782">
													Bank Details
												</fo:inline>
												</fo:block>
											<fo:block   xsl:use-attribute-sets="fonts_group1.size_11" space-before="0.3cm">
												<xsl:if test="payeeId"><fo:inline font-weight="bold" color="#990000">
													<fo:leader leader-pattern="space" leader-length="0.3cm" />
												</fo:inline></xsl:if>
												<fo:inline font-weight="bold">
													Bank / Branch Name:
												</fo:inline><fo:inline><xsl:value-of select="bankBranchName" />
												</fo:inline>
											</fo:block>
											<fo:block xsl:use-attribute-sets="fonts_group1.size_11" >
												<xsl:if test="payeeId"><fo:inline font-weight="bold" color="#990000">
													<fo:leader leader-pattern="space" leader-length="0.3cm" />
												</fo:inline></xsl:if>
												<fo:inline font-weight="bold">
													ABA / Routing Number:
												</fo:inline><fo:inline><xsl:value-of select="routingABAnumber" />
												</fo:inline>
											</fo:block>
											<fo:block padding-after="0.5cm" xsl:use-attribute-sets="fonts_group1.size_11"  >
												<xsl:if test="payeeId"><fo:inline font-weight="bold" color="#990000">
													<fo:leader leader-pattern="space" leader-length="0.3cm" />
												</fo:inline></xsl:if>
												<fo:inline font-weight="bold">
													Credit Party Name:
												</fo:inline>
												<fo:inline><xsl:value-of select="creditPayeeName" />
												</fo:inline>
											</fo:block>
											<xsl:if test="afterTaxRoth">
											<fo:block  xsl:use-attribute-sets="fonts_group2.size_12" space-before="0.7cm" >
												<xsl:if test="payeeId"><fo:inline font-weight="bold" color="#990000">
													<fo:leader leader-pattern="space" leader-length="0.3cm" />
												</fo:inline></xsl:if>
												<fo:inline color= "#607782">
													After-tax Cost Basis
												</fo:inline>
												</fo:block>
												
												<fo:block space-before="0.5cm" space-after="0.5cm">
												<fo:table>
													<fo:table-header>
														<fo:table-row background-color="#deded8" display-align="left">
															<fo:table-cell padding-start="0.1cm"
																xsl:use-attribute-sets="solid.blue.border" text-align="center">
																<fo:block padding-before="0.1cm" padding-after="0.1cm"
																	xsl:use-attribute-sets="verdana_font.size_10">
																	Money Type
																</fo:block>
															</fo:table-cell>
											
															<fo:table-cell padding-start="0.1cm"
																xsl:use-attribute-sets="solid.blue.border" text-align="center">
																<fo:block padding-before="0.1cm" padding-after="0.1cm"
																	xsl:use-attribute-sets="verdana_font.size_10">
																	Net Contributions ($)
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding-start="0.1cm"
																xsl:use-attribute-sets="solid.blue.border" text-align="center">
																<fo:block padding-before="0.1cm" padding-after="0.1cm"
																	xsl:use-attribute-sets="verdana_font.size_10">
																	Net Earnings($)
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding-start="0.1cm"
																xsl:use-attribute-sets="solid.blue.border" text-align="center">
																<fo:block padding-before="0.1cm" padding-after="0.1cm"
																	xsl:use-attribute-sets="verdana_font.size_10">
																	Withdrawal Amount($)
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
													</fo:table-header>
													<fo:table-body>
														<xsl:for-each select="afterTaxRoth">
															<fo:table-row background-color="#f5f4f0" display-align="left">
																<fo:table-cell padding-start="0.1cm"
																	xsl:use-attribute-sets="solid.blue.border" text-align="left">
																	<fo:block padding-before="0.1cm" padding-after="0.1cm"
																		xsl:use-attribute-sets="verdana_font.size_10">
																		<xsl:value-of select="afterTaXMoneyType" />
																	</fo:block>
																</fo:table-cell>
											
																<fo:table-cell padding-start="0.1cm"
																	xsl:use-attribute-sets="solid.blue.border" text-align="right">
																	<fo:block padding-before="0.1cm" padding-after="0.1cm"
																		xsl:use-attribute-sets="verdana_font.size_10">
																			<xsl:value-of select="afterTaxContrAmt" />
																	</fo:block>
																</fo:table-cell>
																	<fo:table-cell padding-start="0.1cm"
																		xsl:use-attribute-sets="solid.blue.border" text-align="right">
																		<fo:block padding-before="0.1cm" padding-after="0.1cm"
																			xsl:use-attribute-sets="verdana_font.size_10">
																			<xsl:value-of select="afterTaxEarnAmt" />
																		</fo:block>
																	</fo:table-cell>
																<fo:table-cell padding-start="0.1cm"
																	xsl:use-attribute-sets="solid.blue.border" text-align="right">
																	<fo:block padding-before="0.1cm" padding-after="0.1cm"
																		xsl:use-attribute-sets="verdana_font.size_10">
																		<xsl:value-of select="afterTaxWdAmt" />
																	</fo:block>
																</fo:table-cell>
															</fo:table-row>
														</xsl:for-each>
													</fo:table-body>
												</fo:table>
											</fo:block>
											</xsl:if>
										</xsl:for-each>
					
									</fo:table-cell>
									<fo:table-cell><fo:block  space-before="0.3cm"/> </fo:table-cell>
								</fo:table-row>
							</fo:table-body>
					
						</fo:table>
					</fo:block>
					
					<!-- Showing the PDF capped message and Footer-->
					<xsl:call-template name="reportCommonFooter"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>


	</xsl:template>
</xsl:stylesheet>
