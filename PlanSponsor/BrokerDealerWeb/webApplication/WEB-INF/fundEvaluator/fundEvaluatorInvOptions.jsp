<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="com.manulife.pension.bd.web.fundEvaluator.common.FundForInvOption" %>
<%@ page import="com.manulife.pension.bd.web.fundEvaluator.CriteriaVO"%>
<%@ page import="com.manulife.pension.bd.web.fundEvaluator.common.AssetClassForInvOption" %>
<%@ page import="com.manulife.pension.bd.web.fundEvaluator.FundEvaluatorConstants" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="java.util.ArrayList" %>

<%
ArrayList assetClassInvOptionList = (ArrayList) request.getAttribute(BDConstants.ASSSET_CLASS_INV_OPTIONS);
pageContext.setAttribute("assetClassInvOptionList",assetClassInvOptionList,PageContext.PAGE_SCOPE);
String fundEvaluationLocation = (String)request.getAttribute(BDConstants.FUND_EVALUATION_LOCATION);
pageContext.setAttribute("fundEvaluationLocation",fundEvaluationLocation,PageContext.PAGE_SCOPE);
%>

<%-- <c:set var="sortAsc" value="${.}" /> --%>
<c:set var="sortAsc" value="ASC" />
<c:set var="sortDesc" value="DESC" />
<c:set var="viewByRanking" value="viewByRanking" />
<c:set var="previewFunds" value="previewFunds" />
<c:set var="lifeCycleAssetClassId" value="LCF" />
<c:set var="valueBlank" value=""/>

<%-- Detailed Lifestyle and LifeCycle and  Preview investment options --%>
	<DIV class="scrollableTable" id="invOptionsList">
		<div class="report_table">
			<table id="fundsListingTable" class="report_table_content" >
				<%-- Displays the selected criterias names as header along with overall rank order column --%>
				<thead>
					<tr> 
						<th colspan="3">
							&nbsp;
						</th>
						<th class="header_shaded" width="10%">
							<%-- Overall rank column header --%>
							<c:if test="${fundEvaluatorForm.rankingOrder eq sortAsc}" >
								<span class="showScreen">
									<A class="headerLink" onclick="orderByRank('${sortDesc}')" >	
										Overall<BR/>rank									
									</A>
									<img height=1 src="/assets/unmanaged/images/s.gif" width=1 border=0>
									<img src="/assets/unmanaged/images/icons/icon_sort_descending.gif" alt="" width="8" height="8" />
								</span>
								<span class="showPrint">
									Overall<BR/>rank
									<img height=1 src="/assets/unmanaged/images/s.gif" width=1 border=0>
									<img src="/assets/unmanaged/images/icons/icon_sort_descending.gif" alt="" width="8" height="8" />
								</span>
							</c:if>
							<c:if test="${fundEvaluatorForm.rankingOrder eq sortDesc}">
								<span class="showScreen">
									<A class="headerLink" onclick="orderByRank('${sortAsc}')" id="testId">	
										Overall<BR/>rank
									</A>
									<img height="1" src="/assets/unmanaged/images/s.gif" width=1 border=0>
									<img src="/assets/unmanaged/images/icons/icon_sort_ascending.gif" alt="" width="8" height="8" />
								</span>
								<span class="showPrint">
									Overall<BR/>rank
									<img height="1" src="/assets/unmanaged/images/s.gif" width=1 border=0>
								<img src="/assets/unmanaged/images/icons/icon_sort_ascending.gif" alt="" width="8" height="8" />
								</span>
								
							</c:if>
						</th>
						<%-- Displays selected criteria names as headers --%>
						<c:set var="selectedCriteriasCount" value="0" />
						<c:forEach var="criteriaSelectedList" items="${fundEvaluatorForm.theItemList}" >
							<c:if test="${not empty criteriaSelectedList.criteriaSelected}">
								<c:set var="selectedCriteriasCount" value="${selectedCriteriasCount + 1}" />
								<th class="header" width="10%">
									${criteriaSelectedList.measuredBy}
									<%-- if view option is By Measurement --%>
									<c:if test="${(fundEvaluatorForm.viewInvestmentOptionsBy ne viewByRanking && fundEvaluatorForm.assetClassId ne previewFunds) || (fundEvaluatorForm.viewInvOptionsByOnPreview ne viewByRanking && fundEvaluatorForm.assetClassId eq previewFunds)}">
										<c:if test="${criteriaSelectedList.percentageIndRequired}">																	 
											(%)
										</c:if>
									</c:if>
								</th>
							</c:if>
						</c:forEach>
					</tr>
				</thead>
				
				<tbody>
					<%-- Start : if the request is for one asset class or preview (except IDX asset class funds)  --%>
<c:forEach items="${assetClassInvOptionList}" var="assetClassForInvOption" varStatus="assetClassIndex">
						<c:set var="fundForInvOptionList" value="${assetClassForInvOption.fundForInvOptionList}" />
						<c:set var="assetClassDetails" value="${fundEvaluatorForm.assetClassDetails}" />
					
							<%-- If asset class does not have funds skip --%>
							<c:if test="${not empty assetClassForInvOption.fundForInvOptionList}">			
								<c:set var="overlayAssetClass" value="brown" />
								<TR class="${overlayAssetClass}">
									<TD noWrap align=right width="10%" height="25">
										&nbsp;
									</TD>
									<%-- Asset class name --%>
									<%-- 3 column added to count is fund check box, fundName & overall rank columns --%>
									<c:set  var="assetClsNameTotalColSpan" value="${selectedCriteriasCount + 3}" />
									<TD colspan="${assetClsNameTotalColSpan}">
										${assetClassForInvOption.description}
										<c:if test="${assetClassForInvOption.id eq lifeCycleAssetClassId}" >
											[all Funds in a suite must be selected ]
										</c:if>
									</TD>
								</TR>
<c:forEach items="${fundForInvOptionList}" var="fundForInvOption" varStatus="fundIndex">
								<c:choose>
									<c:when test="${fundIndex.index eq 0}">
										<c:set var="rowClass" value="spec" />
									</c:when>
									<c:when test="${fundIndex.index % 2 eq 0}">
										<c:set var="rowClass" value="spec" />
									</c:when>
									<c:otherwise>
										<c:set var="rowClass" value="" />
									</c:otherwise>
								</c:choose>
								
								<TR class="${rowClass}">
									<%-- Fund Icons column	--%>
									<TD  align="right" class="noWrap noBorder" style="white-space: nowrap;">
									
									<c:if test="${fundForInvOption.toolSelected}" >
									<span class="legendText" id="${fundForInvOption.fund.fundId}-C">
											<img src="/assets/unmanaged/images/calculatedFundIcon.png" alt="" width="14" height="16" />
									</span>
									</c:if>
									
									<span class="legendText" id="${fundForInvOption.fund.fundId}-D"
									      style=" display: ${ (fundForInvOption.toolSelected || fundForInvOption.contractSelected) && !fundForInvOption.checked ? 'all' :'none' } ">
											<img src="/assets/unmanaged/images/manuallyRemovedFundIcon.png" alt="" width="14" height="16" />
								    </span>
								    
								    <span class="legendText"  id="${fundForInvOption.fund.fundId}-A"
								          style="display: ${!fundForInvOption.toolSelected && !fundForInvOption.contractSelected && fundForInvOption.checked ? 'all' : 'none' } " >
											<img src="/assets/unmanaged/images/manuallyAddedFundIcon.png" alt="" width="14" height="16" />
									</span>
									
									<c:choose>
									<c:when test="${fundEvaluatorForm.PBAContrat && fundForInvOption.PBACompetingFund }">
									
											 <span class="legendText">
												<img src="/assets/unmanaged/images/P_Icon.png" alt="" width="12" height="14" />
											</span>
									</c:when>
									<c:otherwise>
										
										<%-- Fund Icon Closed to new business fund indicator --%>
										<c:if test="${fundForInvOption.closedToNB}" >
											<span class="legendText">
												<img src="/assets/unmanaged/images/minusIcon.png" alt="" width="12" height="14" />
											</span>
										</c:if>
																		
										<%-- Fund Icon Contract Selected fund indicator --%>
										<c:if test="${fundForInvOption.contractSelected && not empty fundEvaluatorForm.contractNumber}" >
											<span class="legendText" id="${fundForInvOption.fund.fundId}-E">
												<img src="/assets/unmanaged/images/eIcon.png" alt="" width="12" height="14" />
											</span>
										</c:if>
									</c:otherwise>	
									
									</c:choose>
									
									</TD>

									<TD width="4%" align="right" class="noBorder">
										<c:if test="${fundForInvOption.checked}">
											<input type="checkbox" name="selectedFunds" value = "${assetClassForInvOption.id}-${fundForInvOption.fund.fundId}-${fundForInvOption.fund.optionalFund}-${fundForInvOption.fund.lifeCycleFundFamilyCode}" 
												CHECKED onclick="processFundSelectionStatus(this)" />
										</c:if>
										<c:if test="${!fundForInvOption.checked}">
											<input type="checkbox" name="selectedFunds" value = "${assetClassForInvOption.id}-${fundForInvOption.fund.fundId}-${fundForInvOption.fund.optionalFund}-${fundForInvOption.fund.lifeCycleFundFamilyCode}" 
												onclick="processFundSelectionStatus(this)" />
										</c:if>
										
									</TD>
									<TD align="left" class="noBorder" style="white-space: nowrap">
<A class="fundSheetLink showScreen" href="#fundsheet" onMouseOver='self.status="Fund Sheet"; return true' NAME='${fundForInvOption.fund.fundName}' onclick='FundWindow("<bd:fundLink fundIdProperty="fundForInvOption.fund.fundId" fundTypeProperty="fundForInvOption.fund.fundType" fundClass="${fundForInvOption.classShortName}"  siteLocation="${fundEvaluationLocation}" />")'>
											${fundForInvOption.fund.fundName}
										</A>
										<div class="showPrint">
											${fundForInvOption.fund.fundName}
										</div>
									</TD>
									
									<%-- End : for selected criteria values display --%>
									<TD class="cur_shaded">
										${fundForInvOption.overallDisplayRank}
									</TD>
									
									<%-- Start : for selected percentile criteria values display --%>
									<c:if test="${(fundEvaluatorForm.viewInvestmentOptionsBy eq viewByRanking && fundEvaluatorForm.assetClassId ne previewFunds) || (fundEvaluatorForm.viewInvOptionsByOnPreview eq viewByRanking && fundEvaluatorForm.assetClassId eq previewFunds)}">
<c:forEach items="${fundForInvOption.resultPercentileConvertedToRankCriteria}" var="criterias">
											<TD id="percentileCriteriaId" class="cur" >
												${criterias}
											</TD>
</c:forEach>
									</c:if>
									<%-- End : for selected criteria values display --%>
									
									<%-- Start : for selected value criteria values display --%>
									<c:if test="${(fundEvaluatorForm.viewInvestmentOptionsBy ne viewByRanking && fundEvaluatorForm.assetClassId ne previewFunds) || (fundEvaluatorForm.viewInvOptionsByOnPreview ne viewByRanking && fundEvaluatorForm.assetClassId eq previewFunds)}">
<c:forEach items="${fundForInvOption.resultValueCriteria}" var="criterias">
											<TD id="valueCriteriaId" class="cur">
												${criterias}
											</TD>
</c:forEach>
									</c:if>
								</TR>
</c:forEach>
							</c:if>
</c:forEach>
				</tbody>
			</table>
		</div>
	</DIV>

