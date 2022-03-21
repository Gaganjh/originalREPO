<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="GlossaryTemplate">
		<fo:page-sequence master-reference="GlossaryLayout">
			
			<!-- Header -->
			<fo:static-content flow-name="xsl-region-before" >
				<fo:block>
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
			<fo:static-content flow-name="xsl-region-after" >
				<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="126px"/>
					<fo:table-body>
						<fo:table-row height = "14px">
							<fo:table-cell>
								<fo:block xsl:use-attribute-sets="page_count">
									PAGE <fo:page-number/> OF <fo:page-number-citation-last ref-id="terminator"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>

			<!-- Body -->
			<fo:flow flow-name="xsl-region-body">
			
				<!--  Header for First Page -->
				<fo:block>
					<fo:marker marker-class-name="headerCell3">
						<fo:block-container  xsl:use-attribute-sets="header_block_cell3">
							<fo:block id="glossarySectionId" padding-after="12px" start-indent="18px">
								Glossary
							</fo:block>
						</fo:block-container>
					</fo:marker>
				</fo:block>
				
				<!--  Header for remaining Pages -->
				<fo:block>
					<fo:marker marker-class-name="headerCell3">
						<fo:block-container  xsl:use-attribute-sets="header_block_cell3_continued">
							<fo:block padding-after="4px" start-indent="18px">
								Glossary
							</fo:block>
						</fo:block-container>
					</fo:marker>
				</fo:block>
				
				<fo:table table-layout="fixed" width="100%" space-before="25px">
					<fo:table-column column-width="128px"/>
					<fo:table-column column-width="14px"/>
					<fo:table-column column-width="275.5px"/>
					<fo:table-column column-width="12px"/>
					<fo:table-column column-width="275.5px"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell text-align="right">
								<fo:block>
									<fo:external-graphic content-height="432px" content-width="7px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Verticle-rule.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell column-number="3" padding-before="4px">
								<fo:block padding-before="-1px">
								
								    <fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										3-year, 5-year, 10-year Return
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										The change in the unit value of a Fund for the period indicated. It includes the reinvestment of all distributions made by the underlying investment and any change in the reported value of the underlying investment less applicable fees. For periods greater than one year, the total return is annualized. Refer to the "Investment options rankings - Supplementary return information" in the Important Notes section of this report for additional 1, 3, 5, and 10 year return rankings.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Alpha
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A measure of performance on a risk-adjusted basis. Alpha takes the volatility of a mutual fund and compares its risk-adjusted performance to a benchmark index. The excess return of the fund relative to the return of the benchmark index is a fund's alpha.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Benchmark
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A standard, usually a market index or peer group average, to which the performance of a Fund is compared for evaluative purposes. It is the responsibility of the plan fiduciary to determine the appropriate benchmarks for evaluating the investment options offered in a qualified retirement plan.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Beta
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										Beta represents the sensitivity of a Fund's returns to the returns on its benchmark. A Fund that has a beta greater than 1.0 with respect to its benchmark has been more volatile than its benchmark. A Fund with a beta less than 1.0 has been less volatile than its benchmark.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Competing Investment Option
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										Group annuity contracts that elect a Stable Value option must exclude certain competing investment options. Competing refers to any investment vehicle available under the group annuity contract which offers a low risk of loss of principal. Competing investment options 
									</fo:block>
									
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell column-number="5" padding-before="4px">
								<fo:block padding-before="-1px">
								    <fo:block xsl:use-attribute-sets="Glossary_term-definition">
										are, but are not limited to, Guaranteed Interest Accounts, GICs, money market Funds or any other Funds as determined by the underlying mutual fund company or John Hancock.
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Default Investment Option
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A Fund selected by the plan trustee, where participants who do not select a Fund in which to invest, are defaulted.
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Downside Capture
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										Performance in down markets relative to the benchmark. A ratio of 50% means the Fund's value fell half as much as the benchmark during down markets.
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										ERISA
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										The Employee Retirement Income Security Act of 1974, as amended. A comprehensive piece of federal legislation designed to regulate the provision of private employer-provided retirement and welfare benefits.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										ERISA Section 404(c)
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A section of ERISA under which the plan fiduciaries may be protected from liability when participants or beneficiaries are given an opportunity to exercise investment control over assets in an individual account by choosing from among a broad range of investment alternatives, and the participants or beneficiaries in fact direct the investment of their accounts. If all of the requirements of 404(c) are met, plan fiduciaries may not be liable for any loss that results from a participant's exercise of control over the investments in his or her account.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Exchange Traded Funds (ETFs)
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
									Exchange Traded Funds (ETFs) are funds that track indexes like the NASDAQ-100 Index, S&amp;P 500, Dow Jones, etc. When you buy shares 
									</fo:block>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<!-- 2nd Page -->	
						<fo:table-row>
							<fo:table-cell text-align="right">
								<fo:block>
									<fo:external-graphic content-height="457px" content-width="7px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Verticle-rule.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell column-number="3" padding-before="4px">
								<fo:block padding-before="-1px">
								     <fo:block xsl:use-attribute-sets="Glossary_term-definition">
								        of an ETF, you are buying shares of a portfolio that tracks the yield and return of its native index.
								     </fo:block>
								    <fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Expense Ratio
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										Expenses for a specific unit class for investment options available under a John Hancock group annuity contract. The Expense Ratio ("ER") shown represents the total annual operating expenses for the investment options made available by John Hancock. It is made up of John Hancock's (i) "Revenue from Sub-account", and (ii) the expenses of the underlying fund (based on expense ratios reported in the most recent prospectuses available as of the date of printing; "FER"). In the case where an underlying fund has either waived a portion of, or capped, its fees, the FER used to determine the ER of the sub-account that invests in the underlying fund is the net expense ratio of the underlying fund. "Underlying fund" or "fund" refers to the underlying mutual fund, collective trust, or exchanged traded fund ("ETF") in which the investment option invests.
										</fo:block>
										<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										The FER is determined by the underlying fund and is subject to fluctuation. Any change in the FER of an underlying fund will affect the Expense Ratio of the investment option which invests in the underlying fund.
										</fo:block>
										<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										The ER applies daily at a rate equivalent to the annual rate shown, and may vary to reflect changes in the expenses of an underlying fund and other factors.
										</fo:block>
										<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										For Expense Ratio information current as of the most recent quarter end, please refer to the monthly "Return and Fees" listing available from John Hancock upon request. For more information, please contact your financial representative.
										</fo:block>
								</fo:block>	
							</fo:table-cell>
							
							<fo:table-cell column-number="5" padding-before="4px">
								<fo:block padding-before="-1px">
							    <fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
									Excess Return
								</fo:block>
								<fo:block xsl:use-attribute-sets="Glossary_term-definition">
									The return on a Fund or a benchmark in excess of the risk-free rate. In this report, the risk-free rate is represented by the return on the Salomon Brothers Three-Month Domestic Treasury Bill Index.
								</fo:block>
								<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Fixed Income Terms
								</fo:block>
								<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										<fo:block space-after="9px">
											Fixed income Funds in this report are categorized as short term, intermediate term and long term, based on the duration of the underlying investment portfolio:
										</fo:block>
								        <fo:list-block>
											<fo:list-item space-after="6px">
												<fo:list-item-label>
													<fo:block color="#CD5806">&#8226;</fo:block>
												</fo:list-item-label>
												<fo:list-item-body start-indent="12px" end-indent="20px">
													<fo:block>
														Long term portfolios generally have a duration of 6 years or greater,
													</fo:block>
												</fo:list-item-body>
											</fo:list-item>
											<fo:list-item space-after="6px">
												<fo:list-item-label>
													<fo:block color="#CD5806">&#8226;</fo:block>
												</fo:list-item-label>
												<fo:list-item-body start-indent="12px" end-indent="20px">
													<fo:block>
														Intermediate term portfolios generally have a duration of 3.5 years to 6 years,
													</fo:block>
												</fo:list-item-body>
											</fo:list-item>
											<fo:list-item>
												<fo:list-item-label>
													<fo:block color="#CD5806">&#8226;</fo:block>
												</fo:list-item-label>
												<fo:list-item-body start-indent="12px" end-indent="20px">
													<fo:block>
														Short term portfolios generally have a duration of less than 3.5 years.
													</fo:block>
												</fo:list-item-body>
											</fo:list-item>
										</fo:list-block>
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Index
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A measure of value changes in representative groupings of stocks or bonds. Indexes are used primarily for comparative performance measurement and as a gauge of movements in financial markets. An example is the Standard &amp; Poor's<fo:inline baseline-shift="2.2px" font-size="6pt">®</fo:inline> 500 Stock Composite Index (S&amp;P 500<fo:inline baseline-shift="2.2px" font-size="6pt">®</fo:inline>).
									</fo:block>
								
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Index Fund
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A portfolio of securities that closely matches an established index like the S&amp;P 500<fo:inline baseline-shift="2.2px" font-size="6pt">®</fo:inline>. Index Funds offer diversification because the decision of which securities to invest in is mainly automatic, being predetermined by the index itself.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Information Ratio
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										The Information Ratio measures the level and consistency of a Fund's active return (return in excess of a benchmark). The ratio is calculated by dividing a Fund's alpha by its active risk (annualized standard 
									</fo:block>
									
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<!-- 3rd Page-->
						<fo:table-row>
							<fo:table-cell text-align="right">
								<fo:block>
									<fo:external-graphic content-height="457px" content-width="7px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Verticle-rule.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell column-number="3" padding-before="4px">
								<fo:block padding-before="-1px">
								
								<fo:block xsl:use-attribute-sets="Glossary_term-definition">
								deviation of the active return). The Fund with the highest information ratio showed the greatest level of consistency in beating its benchmark. Information ratios calculated for each Fund are based on data from the 36 months prior to the date of this report.
								</fo:block>
								
								<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										International/Global Funds
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										International Funds generally own securities issued by companies or governments outside of the United States whereas, Global Funds invest in securities issued by companies and governments throughout the world, including the United States.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Investment Policy Statement
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A set of guidelines for selecting, monitoring and evaluating the performance of the investment options in your company's plan.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Investment Style
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										<fo:block space-after="8px">
											Funds investing predominantly in stocks are typically classified as being managed according to one of three investment styles:
										</fo:block>
										<fo:block space-after="8px" font-weight="bold">
											Growth <fo:inline font-weight="normal">&#8211; these Funds buy stocks of companies expected to have higher than anticipated growth in earnings that will result in higher stock prices.</fo:inline>
										</fo:block>
										<fo:block space-after="8px" font-weight="bold">
											Value <fo:inline font-weight="normal">&#8211; these Funds buy stocks of companies the asset manager believes are undervalued relative to their current market price.</fo:inline>										</fo:block>
										<fo:block font-weight="bold">
											Blend <fo:inline font-weight="normal">&#8211; these Funds buy both value and growth stocks.</fo:inline>
										</fo:block>
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Market Capitalization
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A company's market capitalization equals the number of company shares outstanding multiplied by its stock price. Funds that invest in stocks are classified as "Large-cap" which generally refers to Funds investing in stocks of companies with market capitalizations of more than $10 billion, "Mid-cap", companies between $2 billion and $10 billion and "Small-cap", companies of less than $2 billion.
									</fo:block>
									
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell column-number="5" padding-before="4px">
								<fo:block padding-before="-1px">
								<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Morningstar<fo:inline baseline-shift="2.2px" font-size="6pt">®</fo:inline> Category
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										Morningstar<fo:inline baseline-shift="2.2px" font-size="6pt">®</fo:inline>, Inc. assigns a category based on the underlying securities in each Fund's portfolio. Morningstar<fo:inline baseline-shift="2.2px" font-size="6pt">®</fo:inline> Categories are designed to help investors and investment professionals make meaningful comparisons between Funds. 
									</fo:block>
								<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Plan Fiduciary
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										For the purposes of this report, plan fiduciary describes the employer and any designated person or committee responsible for the selection and monitoring of the investment options offered under the plan. There are other ways in which a person or entity can be a fiduciary of an employee benefit plan under ERISA.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										R-squared
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A measure of "fit" between a Fund's investment style and that of its benchmark. It can range from 0% to 100% and is measured using the returns of the Fund and its benchmark for the prior 36 months as of the date of this report. Funds with a high R-squared generally exhibit similar volatility patterns to their benchmark, indicating they are probably representative of the asset class or style defined by the benchmark.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Redemption Fees
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										The investment manager of the underlying mutual fund imposes a redemption fee for mutual fund shares sold within a specified period of time, which will be deducted from the value of a participant's account. Please refer to the Investment Comparative Chart (ICC) or Fund sheet for details. 
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Sector
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										Some Funds concentrate their investments in stocks of companies from a single industry or "sector," such as utilities, health care, real estate or technology.
									</fo:block>
									
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<!-- 4th Page -->
						<fo:table-row>
							<fo:table-cell text-align="right">
								<fo:block>
									<fo:external-graphic content-height="457px" content-width="7px">
										<xsl:attribute name="src">
											<xsl:value-of select="concat($imagePath,'Verticle-rule.jpg')"/>
										</xsl:attribute>
									</fo:external-graphic>
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell column-number="3" padding-before="4px">
								<fo:block padding-before="-1px">
								<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Sharpe Ratio
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										In the context of this report, the Sharpe Ratio is a measure of the relationship of reward to risk. A higher ratio indicates an investment strategy with lower risk related to return. It is measured by dividing a Fund's average annualized excess return by the annualized standard deviation of its excess return (prior 36 months used as of the date of this report).
									</fo:block>	
								<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Standard Deviation
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A measure of the variability of a Fund's returns from its mean. It is used to gauge dispersion from the Fund's mean return over a three-year period. A Fund with a high standard deviation can be expected to have a wider range of annual returns. In contrast, a Fund with relatively low standard deviation can be expected to have a narrow range of annual returns.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Sub-Account
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										A sub-account ("Fund") is an account within a separate account established or maintained by an insurance company that operates apart from, and is insulated from, the general assets or liabilities of the company. These accounts pool the contributions of more than one participant and invest them in securities or in an underlying mutual Fund.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Total Return
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										The change in the unit value of a Fund for the period indicated. It includes the reinvestment of all distributions made by the underlying investment and any change in the reported value of the underlying investment less applicable fees. For periods greater than one year, the total return is annualized.
									</fo:block>
									
									<fo:block xsl:use-attribute-sets="Glossary_term-sub-header">
										Upside Capture
									</fo:block>
									<fo:block xsl:use-attribute-sets="Glossary_term-definition">
										Performance in up markets relative to the benchmark. A ratio of 50% means the Fund's value increased half as much as the benchmark during up markets.
									</fo:block>	
								</fo:block>
							</fo:table-cell>
							
							<fo:table-cell column-number="5" padding-before="4px">
								<fo:block padding-before="-1px">
								
								</fo:block>
								</fo:table-cell>
								</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>
</xsl:stylesheet>