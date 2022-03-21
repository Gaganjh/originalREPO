<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
	<xsl:template name="ParticipantNotification">

		<fo:page-sequence master-reference="ParticipantNotificationLayout">
			<fo:flow flow-name="xsl-region-body">

				<fo:block xsl:use-attribute-sets="header_default_font"
					padding-before="20px">
					IMPORTANT INFORMATION CONCERNING YOUR PLAN’s
					INVESTMENT OPTIONS
				</fo:block>

				<fo:block xsl:use-attribute-sets="header_default_font">
					Please Review
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content"
					padding-before="10px">
					<xsl:value-of select="participant_notification_report/currentDate" />
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content"
					font-style="italic" font-size="8pt" padding-before="10px">
					<fo:inline font-weight="bold">Note:</fo:inline>
					If you are not currently enrolled, are not eligible to contribute
					and/or do not have an account balance in your Plan, parts of the
					information on the following pages may not pertain to you.
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content"
					padding-before="10px">
					<fo:inline>
						The following changes are being made to the Designated Investment
						Alternatives (DIAs) – also known as Funds – available under the
						<xsl:value-of select="participant_notification_report/contractName" />
						. Unless noted otherwise, the changes outlined below will be
					</fo:inline>
					<fo:inline font-weight="bold">
						effective
						<xsl:value-of select="participant_notification_report/iateffectiveDate" />
						.
					</fo:inline>
				</fo:block>

				<fo:block padding-before="10px">
					<xsl:if
						test="participant_notification_report/ipsrFundInstructionInfo/ips_fundinstruction_info">
						<fo:table table-layout="fixed" width="100%">
							<fo:table-column column-width="30%" />
							<fo:table-column column-width="35%" />
							<fo:table-column column-width="35%" />
							<fo:table-header>
								<fo:table-row>
									<fo:table-cell xsl:use-attribute-sets="table_header"
										number-columns-spanned="3">
										<fo:block start-indent="5.4px" end-indent="5.4px"
											xsl:use-attribute-sets="static_content_10pt_bold_white">Details about the Fund(s) being
											removed and replaced
										</fo:block>
										<fo:block start-indent="5.4px" end-indent="5.4px"
											xsl:use-attribute-sets="static_content_9pt_white">The
											following Fund(s) will be removed
											and replaced on the date
											noted above. To learn more about the
											replacement Fund, such
											as its objectives, risks, performance,
											fees and expenses,
											review the Fund sheet included with this
											notification.

										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell xsl:use-attribute-sets="column_header"
										background-color="#632423" number-columns-spanned="2">
										<fo:block start-indent="5.4px" end-indent="5.4px"
											padding-before="4px" padding-after="4px">Fund(s) being removed
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="column_header"
										background-color="#76923C">
										<fo:block start-indent="5.4px" end-indent="5.4px"
											padding-before="4px" padding-after="4px">Replacement Fund(s)
										</fo:block>
									</fo:table-cell>

								</fo:table-row>
								<fo:table-row>
									<fo:table-cell xsl:use-attribute-sets="column_header"
										background-color="#FFEFEF" color="#000000">
										<fo:block start-indent="5.4px" end-indent="5.4px">
											<xsl:value-of select="$lang.classHeading" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="column_header"
										background-color="#FFEFEF" color="#000000">
										<fo:block start-indent="5.4px" end-indent="5.4px">
											<xsl:value-of select="$lang.fundHeading" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell xsl:use-attribute-sets="column_header"
										background-color="#D6E3BC" color="#000000">
										<fo:block start-indent="5.4px" end-indent="5.4px">
											<xsl:value-of select="$lang.newFundHeading" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>

							</fo:table-header>

							<fo:table-body>
								<xsl:for-each
									select="participant_notification_report/ipsrFundInstructionInfo/ips_fundinstruction_info">
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="table_cell">
											<fo:block start-indent="5.4px" end-indent="5.4px">
												<xsl:value-of select="assestClassName" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="table_cell">
											<fo:block start-indent="5.4px" end-indent="5.4px">
												<xsl:if test="currentFundInfo/current_fund_info">
													<xsl:for-each select="currentFundInfo/current_fund_info">
														<fo:block>
															<xsl:value-of select="fundName" />
														</fo:block>
													</xsl:for-each>
												</xsl:if>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="table_cell">
											<fo:block start-indent="5.4px" end-indent="5.4px">
												<xsl:if test="topRankedFundInfo/top_rank_fund_info">
													<xsl:for-each select="topRankedFundInfo/top_rank_fund_info">
														<fo:block>
															<xsl:value-of select="fundName" />
														</fo:block>
													</xsl:for-each>
												</xsl:if>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:for-each>
							</fo:table-body>
						</fo:table>
					</xsl:if>
				</fo:block>
				<fo:block xsl:use-attribute-sets="word_title"
					padding-before="10px">
					What to do if a Fund is being replaced?
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content">
					If you are currently
					invested in a Fund that is being replaced, you have two options:
				</fo:block>
				<fo:block>
					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="5%" />
						<fo:table-column column-width="95%" />
						<fo:table-body>


							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="right" padding-before="6px"
										xsl:use-attribute-sets="static_content" font-weight="bold">
										1)
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block start-indent="10px" padding-before="6px"
										xsl:use-attribute-sets="static_content" font-weight="bold">
										Transfer
										your assets
									</fo:block>
								</fo:table-cell>
							</fo:table-row>

							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="right" xsl:use-attribute-sets="static_content">

									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block start-indent="10px" xsl:use-attribute-sets="static_content">
										You
										may transfer your assets that are in a Fund that is being
										replaced
										to any of the other Fund(s) available under the
										Contract before
										the
										effective date noted. To access a full list
										of the Plan’s Funds, go
										to the “Investment Options” page of
										John Hancock’s participant
										website. Once you’ve selected the
										Fund you’d like to transfer
										your
										assets into, use the tools
										available on the website to make the
										Fund change. Be sure to
										also update the allocation instructions
										that John Hancock
										currently has on file for you accordingly.
										If you
										do not have
										access to this website, call John Hancock at
										1-800-395-1113 (or
										1-800-363-0530 for Spanish).
									</fo:block>
								</fo:table-cell>
							</fo:table-row>

							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="right" padding-before="6px"
										xsl:use-attribute-sets="static_content" font-weight="bold">
										2)
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block start-indent="10px" padding-before="6px"
										xsl:use-attribute-sets="static_content" font-weight="bold">
										Leave the
										assets in the Fund
									</fo:block>
								</fo:table-cell>
							</fo:table-row>

							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="right" xsl:use-attribute-sets="static_content">

									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block start-indent="10px" xsl:use-attribute-sets="static_content">
										If you
										do nothing and leave the assets in the Fund that is
										being
										removed
										then, on the effective date noted, the portion of
										your account
										balance invested in such Fund will be liquidated
										and
										reinvested in
										the corresponding Replacement Fund, as
										described above. Your
										contribution allocation instructions that
										John Hancock
										currently
										has on file for you will also be adjusted
										accordingly.
									</fo:block>
								</fo:table-cell>
							</fo:table-row>


						</fo:table-body>
					</fo:table>
				</fo:block>

				<fo:block xsl:use-attribute-sets="word_title"
					padding-before="10px">
					Important considerations when making investment
					decisions
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content">
					When making investment
					decisions, it is important to review the Fund’s objectives, fees
					and expenses as this information may vary from Fund to Fund. It’s
					also important to carefully consider your personal circumstances,
					current savings, monthly earnings and retirement lifestyle goals
					and risk profile. The principal value of your investment in any
					Fund, as well as your potential rate of return, is not guaranteed
					at any time. Also, neither asset allocation nor diversification
					ensures a profit or protect against a loss. Funds can suffer losses
					at any time and there is no guarantee that any Fund will provide
					adequate income at and through your retirement. Also, past
					performance is no guarantee of future results.
				</fo:block>
				<fo:block xsl:use-attribute-sets="word_title"
					padding-before="10px">
					How to obtain additional information?
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content">
					To learn more about the Funds currently available to the Plan, go
					to the “Investment Options” page of John Hancock’s participant
					website
					<fo:basic-link color="blue" font-weight = "bold">
						<xsl:attribute name="external-destination">url('<xsl:value-of
							select="participant_notification_report/siteUrl" />')</xsl:attribute>
						<xsl:value-of select="participant_notification_report/siteUrl" />
					</fo:basic-link>
					. There, you’ll have access to many resources to help you with your
					investment decisions, such as a listing of all the Funds available
					to the Plan, Fund sheets for each Fund listed, and other important
					fee and performance information. Once the above change has taken
					place, the ”Investment Options” page will be updated as well as the
					“404a-5 Plan &amp; Investment Notice” (404a-5 Notice). Its
					<fo:inline font-style="italic" font-size="9pt">Investment
						Comparative Chart
					</fo:inline>
					section contains information about Fund performance and total
					annual operating expense (TAOE).
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content"
					padding-before="10px">
					If you would like to receive another paper copy of
					the 404a-5 Notice, contact your plan administrator. To obtain paper
					copies of John Hancock’s group annuity investment option Fund
					sheets and prospectuses for the investment option's underlying
					fund, call John Hancock at 1-800-395-1113 (or 1-800-363-0530 for
					Spanish). These documents are available free of charge.
				</fo:block>
				<fo:block xsl:use-attribute-sets="static_content"
					padding-before="10px">
					If you have a question about this letter, contact:
				</fo:block>
				<xsl:choose>
					<xsl:when
						test="participant_notification_report/wordGroup = 'wordGroup2'">

						<fo:block xsl:use-attribute-sets="static_content"
							padding-before="10px">
							<xsl:value-of select="participant_notification_report/contractLongName" />
						</fo:block>
					</xsl:when>
					<xsl:otherwise>
						<fo:block xsl:use-attribute-sets="static_content">
							<xsl:value-of select="participant_notification_report/contactName" />
						</fo:block>
						<fo:block xsl:use-attribute-sets="static_content">
							<xsl:value-of select="participant_notification_report/street" />
						</fo:block>
						<fo:block xsl:use-attribute-sets="static_content">
							<xsl:value-of select="participant_notification_report/cityAndState" />
							<xsl:if test="participant_notification_report/cityAndState">
							<xsl:if test="participant_notification_report/zipCode">,
							</xsl:if>
							</xsl:if>
							<xsl:value-of select="participant_notification_report/zipCode" />
						</fo:block>
						<fo:block xsl:use-attribute-sets="static_content">
							<xsl:value-of select="participant_notification_report/contactNumber" />
						</fo:block>

						<xsl:if test="participant_notification_report/flag = 'true'">
							<fo:block xsl:use-attribute-sets="static_content">
								<xsl:value-of select="participant_notification_report/comments" />
							</fo:block>
						</xsl:if>

					</xsl:otherwise>
				</xsl:choose>
				<fo:block>
					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="50%" />
						<fo:table-column column-width="50%" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left" padding-before="10px"
										xsl:use-attribute-sets="static_content">
										GT-P 24592-GE 12/15-24592
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right" padding-before="10px"
										xsl:use-attribute-sets="static_content">
										GA080714193864
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