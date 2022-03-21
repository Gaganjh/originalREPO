<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl"%>
<%@ taglib tagdir="/WEB-INF/tags/tpaFandp" prefix="tpaFandp"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>

<%@page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ page trimDirectiveWhitespaces="true" %>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="fapConstants" className="com.manulife.pension.platform.web.fap.constants.FapConstants" />
<mrtl:noCaching />
<%--
	This JSP creates the tab for the Funds & Performance page.
	the code is generic, so that this JSP will create all the tabs based
	on the create objects
--%>
<%-- Renders the Tab navigation --%>

<table width="730" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><tpaFandp:tpaFandpTabs /></td>
	</tr>
</table>

<%-- 
	COLUMN HEADERS
	~~~~~~~~~~~~~~ 
	There are 2 levels are column Headings. Level1 & Level2
		Level1 - Super Headers. Some tabs will not have the super headers(Level1).
				 Sometimes the Level1 headers will be having 2 rows and 
				 each row will be a separate List object
				 
		Level2 - Sub Headers. All the tabs have the Level2 headers
				 Level2 headers will always be in 1 row.
--%>

<%-- get the Level1 Columns List object which in-turn has List of Columns --%>
<c:set var='level1ColumnsKey' value='ColumnHeadingsLevel1' />
<c:set var="level1ColumnsList" value="${fapForm.columnsInfo[level1ColumnsKey]}" />

<%-- get the Level2 Columns list object which in-turn has List of Columns --%>
<c:set var='level2ColumnsKey' value='ColumnHeadingsLevel2' />
<c:set var="level2ColumnsList" value="${fapForm.columnsInfo[level2ColumnsKey]}" />


<table width="730" border="0" cellpadding="1" cellspacing="1">
	<thead>

		<%-- CREATE THE LEVEL1 COLUMN HEADERS --%>
		<c:if test="${level1ColumnsList != null}">
			<%-- 
				Iterate through the Level1 List, where each value will be 
				another List having the columns info 
			--%>
			<c:forEach items="${level1ColumnsList}" var="level1ColumnsByRow">

				<tr class="shadedRow">
					<%-- 
					Iterate through the columns info list and embed the columns 
				--%>
					<c:forEach items="${level1ColumnsByRow}" var="columnsByRow">
						<td valign="bottom"
							class="<c:out value='${columnsByRow.columnClass}'/>"
							colspan="<c:out value='${columnsByRow.colSpan}'/>"
							rowspan="<c:out value='${columnsByRow.rowSpan}'/>" />

						<c:if test="${!columnsByRow.toggleOption}">
							<div align="center" style="padding: 2px;">${columnsByRow.name}</div>
						</c:if>

						<%-- 
							Some tabs have the toggle options on the levele1 header
							The below loop will embed the toggle options
						 --%>
						<c:if test="${columnsByRow.toggleOption}">
							<div id="IRtitleLft">
								<div align="center">${columnsByRow.name}</div>
							</div>
							<c:forEach items='${columnsByRow.toggleInfoBeans}'
								var='toggleInfoBean'>
								<c:choose>
									<c:when test='${toggleInfoBean.optionActive}'>
										<c:set var='optionClass' value='tabImg' />
										<div id="${optionClass}">${toggleInfoBean.option}</div>
									</c:when>
									<c:when test='${!toggleInfoBean.optionActive}'>
										<c:set var='optionClass' value='tabImgNone' />
										<div id="${optionClass}">
											<a href="javascript://"
												onclick="applyFilter('tabsClick${toggleInfoBean.id}', '${toggleInfoBean.actionURL}', 'filter${toggleInfoBean.id}');">
												${toggleInfoBean.option}</a>
										</div>
									</c:when>
								</c:choose>
							</c:forEach>
						</c:if>
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</c:if>

		<c:if test="${fapForm.tabSelected == 'fundScorecard'}">
			<tr>
				<th rowspan=2 colspan=6 class="jh_scorecard_header_row">
				<c:if test="${fapForm.showFi360ScorecardMetrics}">
						<table class="fi360Legend">
							<tbody style="border: 0">
								<tr style="border: 0">
									<td colspan="2"><b>Legend for Fi360 :</b></td>
								</tr>
								<tr style="border: 0">
									<td colspan="2"><b>NS</b> = Not Scored</td>
								</tr>
								<tr style="border: 0">
									<td><span class="fi360firstQuartile"> </span>&nbsp;0-25 -
										First Quartile</td>
									<td><span class="fi360thirdQuartile"> </span>&nbsp;51-75 -
										Third Quartile
										</li></td>
								</tr>
								<tr style="border: 0">
									<td><span class="fi360secondQuartile"></span>&nbsp;26-50 -
										Second Quartile</td>
									<td><span class="fi360fourthQuartile"></span>&nbsp;76-100
										- Fourth Quartile
										</li></td>
								</tr>
							</tbody>
						</table>
						<br />
					</c:if></th>
				<c:if test="${fapForm.showMorningstarScorecardMetrics}">
					<th valign="bottom" colspan=5 class="mornigstarLogo"></th>
				</c:if>
				<c:if test="${fapForm.showFi360ScorecardMetrics}">
					<th valign="bottom" colspan=5 class="fi360Logo"></th>
				</c:if>
				<c:if test="${fapForm.showRpagScorecardMetrics}">
					<th valign="bottom" colspan=4 class="rpagLogo"></th>
				</c:if>
			</tr>

			<tr class="jh_scorecard_header_row_small">
				<c:if test="${fapForm.showMorningstarScorecardMetrics}">
					<th valign="bottom" colspan=4 class="shadedRow cur">Total
						Return (% Rank in category)<sup>*55</sup>
					</th>
					<th valign="bottom" rowspan=2 class="unshaded_mstar_header score">
						<a href="javascript://" onClick="doColumnSort('10')"
						class="${level2ColumnsList[10].sortClass}"> Overall<br />Rating*<br />(#
							of<br />peers)
					</a>
					</th>
				</c:if>
				<c:if test="${fapForm.showFi360ScorecardMetrics}">
					<th valign="bottom" colspan=5 class="shadedRow cur">Fi360
						Fiduciary Score ® (# of peers)</th>
				</c:if>
				<c:if test="${fapForm.showRpagScorecardMetrics}">
					<th valign="bottom" colspan=4 class="shadedRow cur">RPAG Score
						(out of 10)</th>
				</c:if>
			</tr>
		</c:if>

		<%-- CREATE THE LEVEL2 COLUMN HEADERS --%>
		<c:if test="${level2ColumnsList != null}">
			<tr class="tablesubhead" valign="bottom">
				<%-- 
				Level2 column headers will always be in a single row.
				So, directly iterate the list to create the headers
			--%>
				<c:forEach items="${level2ColumnsList}" var="level2Columns"
					varStatus="rowCounter">
					<c:if test="${level2Columns.showHeader}">
						<th valign="bottom"
							class="<c:out value='${level2Columns.columnClass}'/>"><c:choose>
								<c:when test="${level2Columns.sort}">
									<c:choose>
										<c:when test="${fapForm.displayOnlyHeaders}">
										${level2Columns.name}
									</c:when>
										<c:otherwise>
											<a href="javascript://"
												onClick="doColumnSort('${rowCounter.index}')"
												class="${level2Columns.sortClass}">
												${level2Columns.name} </a>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
								${level2Columns.name}
							</c:otherwise>
							</c:choose></th>
					</c:if>
				</c:forEach>
			</tr>
		</c:if>
	</thead>

	<tbody>

		<%-- 
		TAB BODY
		~~~~~~~~
		Each tab has its own set of column headers to be displayed. The Level2 column headers Object,
		will have  the key which would map to the attribute of the value object.
		
		So, iterate the Level2 column headers List object along with the value object. Use the key 
		of the Level2 columns object to retrieve the data from the Value Object.		
	--%>

		<%-- 
			Iterate the HashMap which would have the key as "Group Name" and 
			value as the List Objects. (this object would differ based on the current tab.
			Each tab would have its own Value Object)
		--%>
		<c:forEach items='${currentTabObject}' var='currentTabObjectMap'>

			<%-- 
			This row will create the group headers. 
			For an example: If the grouping is by Asset Class, then it would be the Asset class name
		--%>
		<tr>
				<td colspan="20" class="groupHeaders"><b>
				<c:out value='${currentTabObjectMap.key}' /></b></td>
			</tr>

			<%--  Iterate through the Value Object List --%>
			<c:forEach items='${currentTabObjectMap.value}' var='tabObject'
				varStatus='tabObjectStatus'>

				<%-- This logic is to differentiate the alternate rows --%>
				<c:choose>
					<c:when test='${(tabObjectStatus.count) % 2 == 1}'>
						<tr class="datacell2">
					</c:when>
					<c:otherwise>
						<tr class="datacell1">
					</c:otherwise>
				</c:choose>

				<%-- 
				Iterate the Level2 column headers List object. The key attribute of the ColumnInfoBean object 
				of the Level2 columns will be used retrieve the data from the Value Object.
				
				For an example: If the Level2 column key is "tickerSymbol" then it would call the 
				getTickerSymbol() in the value object
			--%>
				<c:forEach items="${level2ColumnsList}" var="level2Columns">
					<td <c:if test="${level2Columns.nowrap}"> noWrap </c:if>
						<c:if test="${tabObject.closedToNB}">
						id="closed" style="color:#767676;" 
					</c:if>
						class="<c:out value='${level2Columns.columnClass}'/>  
					 <c:if  test="${level2Columns.valueRenderingType == 'Fi360'}">
							<c:if test="${not empty tabObject[level2Columns.key]}">
					      			<c:set var="fiScore">
									${tabObject[level2Columns.key]}
					     			</c:set>
					    		 	${tabObject.getFi360ScoreStyle(fiScore)}
							</c:if>
	                 </c:if> 
						"
						<c:if test="${level2Columns.dataRowSpan > 1 && tabObject.rowSpan > 1}">
						rowSpan="<c:out value='${level2Columns.dataRowSpan}'/>"
					</c:if>>

						<c:set var="hypoColumnKey" value="${level2Columns.key}" /> <c:choose>
							<c:when
								test="${level2Columns.key == 'fundName' && !tabObject.marketIndexFund}">
								<c:if
									test="${fapForm.tabSelected=='pricesAndYTD' || fapForm.tabSelected == 'performanceAndFees' || 
				                         fapForm.tabSelected =='morningstar' || fapForm.tabSelected =='fundScorecard' ||
				                         fapForm.tabSelected =='PerformanceAndFeesMonthly' ||  fapForm.tabSelected =='PerformanceAndFeesQuarterly'}">
									<table>
										<tbody>
											<tr>
												<c:choose>
													<c:when test="${tabObject.feeWaiverFund}">
														<td align="left" class="fwiIndicator"
															<c:if test="${level2Columns.dataRowSpan > 1 && tabObject.rowSpan > 1}"> 
	                							rowSpan="<c:out value='${level2Columns.dataRowSpan}'/>"	
	                						</c:if>><b>&#8226</b>
														</td>
														<td
															class="fwiIndicator <c:out value='${level2Columns.columnClass}'/>">
													</c:when>
													<c:otherwise>
														<td align="left" class="fwiIndicator"
															<c:if test="${level2Columns.dataRowSpan > 1 && tabObject.rowSpan > 1}">
													rowSpan="<c:out value='${level2Columns.dataRowSpan}'/>"
												</c:if>>&nbsp;
														</td>
														<td
															class="fwiIndicator <c:out value='${level2Columns.columnClass}'/>">
													</c:otherwise>
												</c:choose>
												</c:if>
												<a href="#fundsheet"
													onMouseOver='self.status="Fund Sheet"; return true'
													NAME='${tabObject.fundId}'
													onClick='FundWindow("<ps:fundLink fundIdProperty="tabObject.fundId" fundTypeProperty="tabObject.fundType" fundClass="${tabObject.fundClassShortName}" siteLocation="<%= Environment.getInstance().getSiteLocation() %>" />")'>
													${tabObject.fundNameWithoutFootNotes} </a>
												<SUP>${tabObject.footNoteSymbols}</SUP>
												<c:if
													test="${fapForm.tabSelected=='pricesAndYTD' || fapForm.tabSelected == 'performanceAndFees' || 
				                         fapForm.tabSelected =='morningstar' || fapForm.tabSelected =='fundScorecard' ||
				                         fapForm.tabSelected =='PerformanceAndFeesMonthly' ||  fapForm.tabSelected =='PerformanceAndFeesQuarterly'}">
													</td>
											</tr>
										</tbody>
									</table>
								</c:if>	
							</c:when>
							<c:when
								test="${level2Columns.key == 'dateIntroduced' && (tabObject.marketIndexFund || tabObject.guaranteedFund)}">
						-
					</c:when>
							<c:when
								test="${level2Columns.valueRenderingType == 'TEXT' or level2Columns.valueRenderingType == 'RPAG'
					               or level2Columns.valueRenderingType == 'MSTAR' }">
								<c:choose>
									<c:when
										test="${level2Columns.hypoLogicApplicable && tabObject.hypotheticalInfo[hypoColumnKey]}">
										<c:if test="${empty tabObject[level2Columns.key]}">
									-
								</c:if>
										<c:if test="${not empty  tabObject[level2Columns.key]}">
											<b><ps:formatField format="${level2Columns.format}" name="tabObject" property="${level2Columns.key}" /> <%-- filter="false"  --%></b>
										</c:if>
									</c:when>
									<c:otherwise>
										<c:if test="${empty  tabObject[level2Columns.key]}">
									-
								</c:if>
										<c:if test="${not empty  tabObject[level2Columns.key]}">
									<ps:formatField format="${level2Columns.format}" name="tabObject" property="${level2Columns.key}" /> <%-- filter="false" format="${level2Columns.format}" --%>
										</c:if>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${level2Columns.valueRenderingType == 'Fi360'}">
								<c:if test="${empty  tabObject[level2Columns.key]}">
										NS
								</c:if>
								<c:if test="${not empty  tabObject[level2Columns.key]}">
									<b><ps:formatField format="${level2Columns.format}" name="tabObject" property="${level2Columns.key}" /><%-- filter="false" format="${level2Columns.format}" --%></b>
								</c:if>
							</c:when>
							<c:otherwise>
								<c:if test="${tabObject[level2Columns.key] == '-'}">
										<ps:formatField format="${level2Columns.format}" name="tabObject" property="${level2Columns.key}" /> <%-- format="${level2Columns.format}" --%>
								</c:if>
								<c:if test="${tabObject[level2Columns.key] !='-'}">

									<img src="/assets/unmanaged/images/<ps:formatField format="${level2Columns.format}" name="tabObject" property="${level2Columns.key}"/>">
								</c:if>
							</c:otherwise>
						</c:choose>
					</td>
				</c:forEach>
				</tr>

				<c:if test="${tabObject.rowSpan == '2'}">
					<c:choose>
						<c:when test="${(tabObjectStatus.count) % 2 == 1}">
							<tr class="datacell2">
						</c:when>
						<c:otherwise>
							<tr class="datacell1">
						</c:otherwise>
					</c:choose>
					<c:forEach items="${level2ColumnsList}" var="level2Columns">
						<c:if test="${level2Columns.secondaryKey != null}">
							<td
								<c:if test="${!tabObject.closedToNB}">style="color:#000000;"</c:if>
								class="<c:out value='${level2Columns.columnClass}'/> <c:if test="${tabObject.selectedBycontract}">highlight_fund</c:if>">
								<c:if test="${empty tabObject[level2Columns.secondaryKey]}">
							-
							</c:if> <c:if test="${not empty tabObject[level2Columns.secondaryKey]}">
							<ps:formatField  name="tabObject" property="${level2Columns.secondaryKey}" format="${level2Columns.secondaryKeyFormat}"/>
							 
								</c:if>
							</td>
						</c:if>
					</c:forEach>
					</tr>
				</c:if>

				<c:if test="${!empty tabObject.fundDisclosureText}">
					<c:choose>
						<c:when test='${(tabObjectStatus.count) % 2 == 1}'>
							<tr class="datacell2">
						</c:when>
						<c:otherwise>
							<tr class="datacell1">
						</c:otherwise>
					</c:choose>
					<td colspan="${fn:length(level2ColumnsList)}"
						<c:if test="${tabObject.closedToNB}">style="color:#767676;"</c:if>>
						${tabObject.fundDisclosureText}</td>
					</tr>
				</c:if>

			</c:forEach>
		</c:forEach>
	</tbody>
</table>

<div class="report_table_footer">
	<div class="footnotes">
		<c:if test="${fapForm.allFundListModified ==true}">
			<content:contentBean contentId="${contentConstants.MODIFIED_LINE_UP_DISCLAIMER_ALL_FUNDS}"
				type="${contentConstants.TYPE_MISCELLANEOUS}"
				beanName="allFundsModifiedLineupDisclaimer" />
			<p>
				<br>
				<content:getAttribute attribute="text" beanName="allFundsModifiedLineupDisclaimer" />
			</p>
		</c:if>
		<c:if test="${fapForm.contractFundListModified ==true}">
			<content:contentBean contentId="${contentConstants.MODIFIED_LINE_UP_DISCLAIMER_CONTRACT_FUNDS}"
				type="${contentConstants.TYPE_MISCELLANEOUS}"
				beanName="contractFundsModifiedLineupDisclaimer" />
			<p>
				<br>
				<content:getAttribute attribute="text" beanName="contractFundsModifiedLineupDisclaimer" />
			</p>
		</c:if>

		<c:if
			test="${fapForm.tabSelected ==fapConstants.MORNINGSTAR_TAB_ID}">
			</br>
			<content:contentBean contentId="${contentConstants.MORNINGSTAR_TAB_FOOTNOTE}"
				type="${contentConstants.TYPE_PAGEFOOTNOTE}"
				beanName="MorningstarFootNote" />
			<p>
				<content:getAttribute attribute="text" beanName="MorningstarFootNote" />
			</p>
		</c:if>
		<p>
			<content:pageFooter beanName="layoutPageBean" />
		</p>
		<p>
			<content:pageFootnotes beanName="layoutPageBean" />
		</p>
		<c:if
			test="${fapForm.tabSelected ==fapConstants.MORNINGSTAR_TAB_ID}">
			<jsp:include page="/WEB-INF/fap/tpaFandpMorningstarFootNotes.jsp"></jsp:include>
		</c:if>
		<c:if
			test="${fapForm.tabSelected ==fapConstants.FUNDSCORECARD_TAB_ID}">
			<content:contentBean contentId="${contentConstants.MORNINGSTAR_TAB_FOOTNOTE}"
				type="${contentConstants.TYPE_PAGEFOOTNOTE}"
				beanName="MorningstarFootNote" />
			<p>
				<br>
				<content:getAttribute attribute="text" beanName="MorningstarFootNote" />
			</p>
			<jsp:include page="/WEB-INF/fap/tpaFandpMorningstarFootNotes.jsp"></jsp:include>
		</c:if>
		<ps:fundFootnotes symbols="symbolsArray" />

		<p class="disclaimer">
			<content:pageDisclaimer beanName="layoutPageBean" index="-1" />
		</p>

		<div class="footnotes_footer"></div>
	</div>
</div>
