

<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>       
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<un:useConstants var="bdContentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />
<un:useConstants var="fapConstants"
	className="com.manulife.pension.platform.web.fap.constants.FapConstants" />
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />
<un:useConstants var="constants"
	className="com.manulife.pension.service.fund.util.Constants" />
<mrtl:noCaching/>
<%@ page trimDirectiveWhitespaces="true" %>
<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<% 
	pageContext.setAttribute("isMerrillAdvisor", userProfile.isMerrillAdvisor());
    pageContext.setAttribute("isMerrillContract", userProfile.isMerrillContract());
%>

<%--
	This JSP creates the tab for the Funds & Performance page.
	the code is generic, so that this JSP will create all the tabs based
	on the create objects
--%>

<%-- Renders the Tab navigation --%>
<navigation:fapNavigation/>

<div class="clear_footer"></div>

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
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>
<c:if test="${(!fapForm.contractMode) and (fapConstants.CONTRACT_FUNDS eq fapForm.baseFilterSelect)}">
	<c:set var="contractDetails" value="${sessionScope[fapConstants.CONTRACT_DEAILS]}" scope="request"/> 		
</c:if>

<div class="report_table">
	<table class="report_table_content" width="100%" border="0">
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
						rowspan="<c:out value='${columnsByRow.rowSpan}'/>"/>
	
						<c:if test='${!columnsByRow.toggleOption}'>
							<div align="center" >${columnsByRow.name}</div>
						</c:if>
						
						<%-- 
							Some tabs have the toggle options on the levele1 header
							The below loop will embed the toggle options
						 --%>
						<c:if test='${columnsByRow.toggleOption}'>
							<div id="IRtitleLft">
								<div align="center">${columnsByRow.name}</div>
							</div>
							<c:forEach items='${columnsByRow.toggleInfoBeans}' var='toggleInfoBean'>
								<c:choose>
									<c:when test='${toggleInfoBean.optionActive}'>
										<c:set var='optionClass' value='tabImg'/>
										<div id="${optionClass}">	
											${toggleInfoBean.option}
										</div>
									</c:when>
									<c:when test='${!toggleInfoBean.optionActive}'>
										<c:set var='optionClass' value='tabImgNone'/>
										<div id="${optionClass}">	
											<a href="javascript://" onclick="applyFilter('tabsClick${toggleInfoBean.id}', '${toggleInfoBean.actionURL}', 'filter${toggleInfoBean.id}');"> ${toggleInfoBean.option}</a>
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
		    <table class="fi360Legend" id="fi360Legend" >
		     <tbody style="border: 0">
		        <tr style="border: 0"><td colspan="3"><b>Legend for Fi360 :</b></td></tr>
		        <tr style="border: 0"><td colspan="3"><b>NS</b> = Not Scored</td></tr>
		        <tr style="border: 0"><td><span class="fi360firstQuartile"> </span>&nbsp;0-25 - First Quartile</td>
		            <td><span class="fi360thirdQuartile"> </span>&nbsp;51-75 - Third Quartile</li></td>
		        </tr>
		        <tr style="border: 0"><td><span class="fi360secondQuartile"></span>&nbsp;26-50 - Second Quartile</td>
		            <td><span class="fi360fourthQuartile"></span>&nbsp;76-100 - Fourth Quartile</li></td>
		        </tr>
	          </tbody>
            </table>
            <br/>
            </c:if>
		</th>
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
			<th valign="bottom" colspan=4 class="shadedRow cur">
			 Total Return (% Rank in category)<sup>*55</sup>
			</th>
			<th valign="bottom" rowspan=2 class="score">
			 <a href="javascript://" onClick="doColumnSort('10')" class="${level2ColumnsList[10].sortClass}" >
			 Overall<br/>Rating*<br/>(# of<br/>peers)
			 </a>
			</th>
		</c:if>
		<c:if test="${fapForm.showFi360ScorecardMetrics}">
		    <th valign="bottom" colspan=5 class="shadedRow cur">
		     Fi360 Fiduciary Score ® (# of peers)
		    </th>
		</c:if>
		<c:if test="${fapForm.showRpagScorecardMetrics}">
		   <th valign="bottom" colspan=4 class="shadedRow cur">
		    RPAG Score (out of 10)
		   </th>
		</c:if>
		</tr>
		</c:if>

		<%-- CREATE THE LEVEL2 COLUMN HEADERS --%>
		<c:if test="${level2ColumnsList != null}">
		<c:choose>
		<c:when test="${fapForm.tabSelected == 'fundScorecard'}" >
		   <tr class="ignoreBorder">
		</c:when>
		<c:otherwise>
		   <tr>
		</c:otherwise>
		</c:choose>
			<%-- 
				Level2 column headers will always be in a single row.
				So, directly iterate the list to create the headers
			--%>
			<c:forEach items="${level2ColumnsList}" var="level2Columns" varStatus="rowCounter">
				<c:if test="${level2Columns.showHeader}">
				   <th valign="bottom" class="<c:out value='${level2Columns.columnClass}'/> colspan="<c:out value='${level2Columns.rowSpan}'/>">
						<c:choose>
							<c:when test="${level2Columns.sort}">
								<c:choose>
									<c:when test="${fapForm.displayOnlyHeaders}">
										${level2Columns.name}
									</c:when>
									<c:otherwise>
										<a href="javascript://" onClick="doColumnSort('${rowCounter.index}')" class="${level2Columns.sortClass}">
											${level2Columns.name}
										</a>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								${level2Columns.name}
							</c:otherwise>
						</c:choose>
					</th>
				</c:if>
			</c:forEach>
		</tr>
		</c:if>
	</thead>
	
	<tbody>
	
		<c:forEach items='${currentTabObject}' var='currentTabObjectMap'>
		
		<tr class="spec">
			<c:choose>
				<c:when test="${fapForm.tabSelected == 'fundInformation' || fapForm.tabSelected=='pricesAndYTD'}">
					<td colspan="10" class="zero">
			 	</c:when>
		
				<c:when test="${fapForm.tabSelected == 'performanceAndFees' || fapForm.tabSelected=='fundCharacteristics1' || fapForm.tabSelected=='fundCharacteristics2'}">
					<td colspan="13" class="zero">
			 	</c:when>
			 
			 	<c:when test="${fapForm.tabSelected == 'standardDeviation'}">
					<td colspan="9" class="zero">
			 	</c:when>
			  
			  	<c:when test="${fapForm.tabSelected == 'morningstar'}">
					<td colspan="16" class="zero">
			 	</c:when>
			 <c:otherwise>
					<td colspan="21" class="zero">
			 </c:otherwise>
		 </c:choose>
		
				<div class="page_section_subsubheader"><h4><c:out value='${currentTabObjectMap.key}'/></h4></div>
			</td>
		</tr>

		<%--  Iterate through the Value Object List --%>
		<c:forEach items='${currentTabObjectMap.value}' var='tabObject' varStatus='tabObjectStatus'>
		
			<%-- This logic is to differentiate the alternate rows --%>
			<c:choose>
				<c:when test='${(tabObjectStatus.count) % 2 == 1}'>
					<tr class="spec">
				</c:when>
				<c:otherwise>
					<tr class="spec1">
				</c:otherwise>
			</c:choose>
			<c:forEach items="${level2ColumnsList}" var="level2Columns">
				<td <c:if test='${level2Columns.nowrap}'> noWrap </c:if> 
					<c:if test='${!tabObject.closedToNB}'>
						style="color:#000000;" 
					</c:if>
					<c:if test='${tabObject.closedToNB}'>
						id="closed"
					</c:if>
					class="<c:out value='${level2Columns.columnClass}'/> <c:if test='${tabObject.selectedBycontract}'>highlight_fund</c:if>
					<c:if  test="${level2Columns.valueRenderingType == 'Fi360'}">
						<c:if test="${not empty tabObject[level2Columns.key]}">
						<c:set var="fiScore">${tabObject[level2Columns.key]}</c:set>
								 ${tabObject.getFi360ScoreStyle(fiScore)}
						</c:if>
	                </c:if>
					<%-- <c:if  test="${level2Columns.key == 'fundName' && !tabObject.marketIndexFund && (
					                     fapForm.tabSelected=='pricesAndYTD' || fapForm.tabSelected == 'performanceAndFees' || 
				                         fapForm.tabSelected =='morningstar' || fapForm.tabSelected == 'fundScorecard' ||
				                        fapForm.tabSelected =='PerformanceAndFeesMonthly' ||  fapForm.tabSelected =='PerformanceAndFeesQuarterly' ||
				                         fapForm.tabSelected =='standardDeviation' || fapForm.tabSelected =='fundCharacteristics1' ||
				                         fapForm.tabSelected =='fundCharacteristics2' || fapForm.tabSelected =='fundInformation')}">
	                    fwiColumn
	                </c:if> --%>fwiColumn" 
	                
	                  colspan="<c:out value='${level2Columns.rowSpan}'/>"
					<c:if test='${level2Columns.dataRowSpan > 1 && tabObject.rowSpan > 1}'>
						rowSpan="<c:out value='${level2Columns.dataRowSpan}'/>"
					</c:if>>
				
				<c:set var="hypoColumnKey" value='${level2Columns.key}'/>
				  <c:if  test="${fapForm.tabSelected=='pricesAndYTD' || fapForm.tabSelected == 'performanceAndFees' || 
				                         fapForm.tabSelected =='morningstar' || fapForm.tabSelected == 'fundScorecard' ||
				                        fapForm.tabSelected =='PerformanceAndFeesMonthly' ||  fapForm.tabSelected =='PerformanceAndFeesQuarterly' ||
				                         fapForm.tabSelected =='standardDeviation' || fapForm.tabSelected =='fundCharacteristics1' ||
				                         fapForm.tabSelected =='fundCharacteristics2' || fapForm.tabSelected =='fundInformation'}"> 
				<c:choose>
	                <c:when test="${level2Columns.key == 'fundName' && !tabObject.marketIndexFund}">
	                    
	                    <table>
		        	    <tbody>
		            	<tr>	
	                    
		            	
		            		<td align="left" class="fwiIndicator" style="min-width: 7px"
											<c:if test='${level2Columns.dataRowSpan > 1 && tabObject.rowSpan > 1}'> 
	                							rowSpan="<c:out value='${level2Columns.dataRowSpan}'/>"	
	                						</c:if>>
		            		 
									<c:if test="${tabObject.feeWaiverFund && (fapForm.tabSelected=='pricesAndYTD' || fapForm.tabSelected == 'performanceAndFees' || 
				                         fapForm.tabSelected =='morningstar')}">
										<b>&#8226</b>
									</c:if>
									
									<c:if test="${tabObject.restrictedFund && tabObject.feeWaiverFund}">
										<br/>
									</c:if>
									<c:if test = "${(isMerrillAdvisor || isMerrillContract) && tabObject.restrictedFund}" >
										<b><c:out value = "${bdConstants.MERRILL_RESRICTED_FUND_SYMBOL}"></c:out></b>
									</c:if>
										
							</td>
							
										<td class="fwiIndicator <c:out value='${level2Columns.columnClass}'/>" >
	                   
						<c:if test="${(fapForm.contractMode)}">
								<a href="#fundsheet" onMouseOver='self.status="Fund Sheet"; return true'  NAME='${tabObject.fundId}' onClick='FundWindow("<bd:fundLink fundIdProperty="tabObject.fundId" fundTypeProperty="tabObject.fundType" fundClass="${tabObject.fundClassShortName}" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${fapForm.siteLocation}" />")'>
										${tabObject.fundNameWithoutFootNotes}
									</a><SUP>${tabObject.footNoteSymbols}</SUP>
						</c:if>
							<c:if test="${(!fapForm.contractMode) and (fapConstants.CONTRACT_FUNDS eq fapForm.baseFilterSelect) and ( not empty contractDetails)}">
								<c:if test = "${(tabObject.fundClassShortName== 'CX0' || tabObject.fundClassShortName== 'C0P'|| tabObject.fundClassShortName== 'CY1' || tabObject.fundClassShortName== 'CY2' )}">
								<a href="#fundsheet" onMouseOver='self.status="Fund Sheet"; return true'  NAME='${tabObject.fundId}' onClick='FundWindow("<bd:fundLink fundIdProperty="tabObject.fundId" fundTypeProperty="tabObject.fundType" fundClass="${tabObject.fundClassShortName}" fundSeries ="${contractDetails.fundPackageSeriesCode}" productId ="${contractDetails.productId}" siteLocation="${fapForm.siteLocation}" />")'>
										${tabObject.fundNameWithoutFootNotes}
										</a>
								</c:if>
								<c:if test = "${!(tabObject.fundClassShortName== 'CX0' || tabObject.fundClassShortName== 'C0P'|| tabObject.fundClassShortName== 'CY1' || tabObject.fundClassShortName== 'CY2' )}">
								<a href="#fundsheet" onMouseOver='self.status="Fund Sheet"; return true'  NAME='${tabObject.fundId}' onClick='FundWindow("<bd:fundLink fundIdProperty="tabObject.fundId" fundTypeProperty="tabObject.fundType" rateType ="contractDetails.defaultClass" fundSeries ="${contractDetails.fundPackageSeriesCode}" productId ="${contractDetails.productId}" siteLocation="${fapForm.siteLocation}" />")'>
										${tabObject.fundNameWithoutFootNotes}
									</a>
								</c:if><SUP>${tabObject.footNoteSymbols}</SUP>											
							</c:if>
							<c:if test="${(!fapForm.contractMode) and ((fapConstants.CONTRACT_FUNDS ne fapForm.baseFilterSelect) or (empty contractDetails))}">
								<a href="#fundsheet" onMouseOver='self.status="Fund Sheet"; return true'  NAME='${tabObject.fundId}' onClick='FundWindow("<bd:fundLink fundIdProperty="tabObject.fundId" fundTypeProperty="tabObject.fundType" fundClass="${tabObject.fundClassShortName}" siteLocation="${fapForm.siteLocation}" />")'>
								${tabObject.fundNameWithoutFootNotes}
								</a><SUP>${tabObject.footNoteSymbols}</SUP>
							</c:if>
							
							 </td></tr></tbody></table>
					</c:when>
					<c:when test="${level2Columns.key == 'dateIntroduced' && (tabObject.marketIndexFund || tabObject.guaranteedFund)}">
						-
					</c:when>
					<c:when test="${level2Columns.valueRenderingType == 'TEXT' or level2Columns.valueRenderingType == 'RPAG'
					              or level2Columns.valueRenderingType == 'MSTAR'}">
						<c:choose>
							<c:when test="${level2Columns.hypoLogicApplicable && tabObject.hypotheticalInfo[hypoColumnKey]}">
							<c:if test="${empty tabObject[level2Columns.key]}">
																-
							</c:if>
							<c:if test="${not empty tabObject[level2Columns.key]}"> 
							<b> 
							<bd:formatField format="${level2Columns.format}" name="tabObject" property="${level2Columns.key}" />
							</b>
							</c:if>
														</c:when>
														<c:otherwise>
							<c:if test="${empty tabObject[level2Columns.key]}">
																-
							</c:if>
							<c:if test="${not empty tabObject[level2Columns.key]}">
							<bd:formatField format="${level2Columns.format}" name="tabObject" property="${level2Columns.key}"  />
							</c:if>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:when test="${level2Columns.valueRenderingType == 'Fi360'}">
							<c:if test="${empty tabObject[level2Columns.key]}">
														NS
							</c:if>
							<c:if test="${not empty tabObject[level2Columns.key]}">
							<b><bd:formatField format="${level2Columns.format}" name="tabObject" property="${level2Columns.key}" /></b>
							</c:if>
							</c:when>
							<c:otherwise>
							<c:if  test="${ tabObject[level2Columns.key] =='-'}">
									<bd:formatField format="${level2Columns.format}" name="tabObject" property="${level2Columns.key}"/>
							</c:if>
						<c:if test="${ tabObject[level2Columns.key] !='-'}">
							<img src="/assets/unmanaged/images/<bd:formatField format="${level2Columns.format}" name="tabObject" property="${level2Columns.key}"/>">
						</c:if>
					</c:otherwise>
				</c:choose>
				</c:if>
				</td>
			</c:forEach>
			</tr>
			
			<c:if test='${tabObject.rowSpan == 2}'>
				<c:choose>
					<c:when test='${(tabObjectStatus.count) % 2 == 1}'>
						<tr class="spec">
					</c:when>
					<c:otherwise>
						<tr>
					</c:otherwise>
				</c:choose>
				<c:forEach items="${level2ColumnsList}" var="level2Columns">
				   <c:if test="${level2Columns.secondaryKey != null}">
				      <td  <c:if test='${!tabObject.closedToNB}'>style="color:#000000;"</c:if> 
				           class="<c:out value='${level2Columns.columnClass}'/> <c:if test='${tabObject.selectedBycontract}'>highlight_fund</c:if>" >
<c:if test="${empty tabObject[level2Columns.secondaryKey]}">
							-
</c:if>
<c:if test="${not empty tabObject[level2Columns.secondaryKey]}">
${tabObject[level2Columns.secondaryKey]} <%-- filter="false" format="${level2Columns.secondaryKeyFormat}" --%>
</c:if>
					 </td>
				   </c:if>
				</c:forEach>
				</tr>
			</c:if>
	
			<c:if test='${!empty tabObject.fundDisclosureText}'>
				<c:choose>
					<c:when test='${(tabObjectStatus.count) % 2 == 1}'>
						<tr class="spec">
					</c:when>
					<c:otherwise>
						<tr>
					</c:otherwise>
				</c:choose>
					<td colspan="${fn:length(level2ColumnsList)}" <c:if test='${!tabObject.closedToNB}'>style="color:#000000;"</c:if> <c:if test='${tabObject.selectedBycontract}'>class="highlight_fund"</c:if> >
						${tabObject.fundDisclosureText}
					</td>
				</tr>
			</c:if>
		</c:forEach>
		</c:forEach>
	</tbody>
</table>
</div>

<div class="report_table_footer">
	<div class="footnotes">
<c:if test="${fapForm.allFundListModified ==true}">
			<content:contentBean contentId="${bdContentConstants.MODIFIED_LINE_UP_DISCLAIMER_ALL_FUNDS}" 
			type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="allFundsModifiedLineupDisclaimer"/>
			<p><br><content:getAttribute attribute="text" beanName="allFundsModifiedLineupDisclaimer"/></p>
</c:if>
<c:if test="${fapForm.contractFundListModified ==true}">
			<content:contentBean contentId="${bdContentConstants.MODIFIED_LINE_UP_DISCLAIMER_CONTRACT_FUNDS}" 
			type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="contractFundsModifiedLineupDisclaimer"/>
			<p><br><content:getAttribute attribute="text" beanName="contractFundsModifiedLineupDisclaimer"/></p>
</c:if>
<c:if test="${fapForm.tabSelected == fapConstants.MORNINGSTAR_TAB_ID}">
			<content:contentBean contentId="${bdContentConstants.MORNINGSTAR_TAB_FOOTNOTE}" 
			type="${bdContentConstants.TYPE_PAGEFOOTNOTE}" beanName="MorningstarFootNote"/>
			<p><br><content:getAttribute attribute="text" beanName="MorningstarFootNote"/></p>
</c:if>
		<p><content:pageFooter beanName="fapLayoutPageBean"/></p>
		<p><content:pageFootnotes beanName="fapLayoutPageBean"/></p>
<c:if test="${fapForm.tabSelected == fapConstants.MORNINGSTAR_TAB_ID}">
			<jsp:include page="/WEB-INF/fap/fapMorningstarFootNotes.jsp"></jsp:include>
</c:if>
<c:if test="${fapForm.tabSelected == fapConstants.FUNDSCORECARD_TAB_ID}">
			<content:contentBean contentId="${bdContentConstants.MORNINGSTAR_TAB_FOOTNOTE}" 
			type="${bdContentConstants.TYPE_PAGEFOOTNOTE}" beanName="MorningstarFootNote"/>
			<p><br><content:getAttribute attribute="text" beanName="MorningstarFootNote"/></p>
			<jsp:include page="/WEB-INF/fap/fapMorningstarFootNotes.jsp"></jsp:include>
</c:if>
		<p><bd:fundFootnotes symbols="symbolsArray" companyId="${fapForm.companyId}"/></p>		
		<p><content:pageDisclaimer beanName="fapLayoutPageBean" index="-1"/></p>
		<div class="footnotes_footer"></div>
	</div>
</div>
