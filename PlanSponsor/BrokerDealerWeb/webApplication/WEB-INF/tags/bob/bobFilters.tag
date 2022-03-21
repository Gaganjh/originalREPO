<%@ tag body-content="empty"
 import="java.util.ArrayList"
 import="java.util.Map"
 import="java.util.HashMap"
 import="java.util.Collections"
 import="java.util.Date"  
 import="com.manulife.pension.bd.web.BDConstants" 
 import="com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility"
 import="com.manulife.pension.bd.web.navigation.UserMenu"
 import="com.manulife.pension.bd.web.navigation.UserMenuItem"
 import="com.manulife.pension.bd.web.navigation.UserNavigation"
 import="com.manulife.pension.bd.web.navigation.UserNavigationFactory"
 import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" 
 import="com.manulife.pension.platform.web.CommonConstants"
 import="com.manulife.util.render.RenderConstants"
 import="com.manulife.pension.service.security.role.BDFirmRep"
 import="com.manulife.pension.service.security.role.BDRvp"
 import="com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData"
 import="com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm"
  
  import=" com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<%@ taglib tagdir="/WEB-INF/tags/bob" prefix="bob" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ attribute name="filterType" required="true" %>
<%@ attribute name="actionURL" required="true" %>
<%@ attribute name="reportAsOfDtEnabled" required="false" %> 

<jsp:useBean id="blockOfBusinessForm" scope="session" class="com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm" />


<% 

BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
getJspContext().setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%! 
	/**
	* This method is used to generated ID values for "Advanced Filtering" criteria's. 
	* The way the Javascript has been coded is:
	*	When a user clicks "Customize Report" Link, we need to pass values from Quick Filter section to "Advanced Filtering" section.
	*	For this to happen, and for ease of coding, each criteria in "Quick Filtering" section has a styleId declared to it.
	*	Similarly, each of those filtering criteria in "Advanced Filtering" section have been given a styleId which is equivalent to
	*	"adv" + corresponding styleId of criteria in "Quick Filtering" section.
	*
	*	For example: in Quick Filtering criteria, the contract Name has a styleId of "contractName". If we want to pass this value
	*	to "Advance Filtering" section to its corresponding filtering criteria, the javascript looks out for a Element with
	*	name "advcontractName".
	*
	*	The below method is used to prefix "adv" to the styleId's of criteria in "Advanced Filtering" section.
	*/
	public String appendPrefix(String quickFilterID){
   return "adv" + quickFilterID;
} 

%>
<c:if test="${filterType eq 'quickFilter'}">

<% 




	getJspContext().setAttribute("asOfDatesList", BlockOfBusinessUtility.getMonthEndDates());
	getJspContext().setAttribute("quickFilterList", blockOfBusinessForm.getEnabledQuickFiltersList());
	if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_US_OR_NY)) {
		getJspContext().setAttribute("usNyList", BlockOfBusinessUtility.getUSNYList());
	}
	if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_FUND_CLASS)) {
		getJspContext().setAttribute("fundClassList", BlockOfBusinessUtility.getFundClassesList());
	}
	if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_RPV_NAME)) {
		getJspContext().setAttribute("rvpNamesList", BlockOfBusinessUtility.getAllRVPs());
	}
	if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)) {
		getJspContext().setAttribute("bdFirmRepAssociatedFirmsList", BlockOfBusinessUtility.getAssociatedFirmsForBDFirmRep(userProfile));
	}
	if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE)) {
		getJspContext().setAttribute("productTypeList", BlockOfBusinessUtility.getProductTypeList());
	}
%>

	<bd:form id="quickFilterForm" method="post" action="${actionURL}" cssClass="page_section_filter form small_top_margin" modelAttribute="blockOfBusinessForm" name="blockOfBusinessForm">

		<input type="hidden" name="task" value="filter"/>
		<%-- This property is used to see if the Quick Filter was submitted or Advanced Filtering was submitted. --%>
		<input type="hidden" name="fromQuickFilter" value="true"/>
		
		<table id="font_size_1em">
			<tr class="padding_left_zero">
				<td><p><label for="kindof_filter">As of </label></p></td>
				<td><p><label for="kindof_filter">Search</label></p></td>
				<td><p><label for="kindof_filter"> &nbsp;</label></p></td>
			</tr>
			
			<tr style="height:30px;">
				<td class="padding_left_zero">
					<c:if test="${empty reportAsOfDtEnabled || reportAsOfDtEnabled eq 'true'}">
						<c:set var="asOfDatesList" value="${pageScope.asOfDatesList}"/>
						<bd:select name="blockOfBusinessForm" property="asOfDateSelected" onchange="setAsOfDateInAdvFilterForm()">
							<bd:dateOptions name="asOfDatesList"
						           	renderStyle="<%=RenderConstants.MEDIUM_STYLE%>"/>
					    </bd:select>
						
					</c:if>
					<c:if test="${!empty reportAsOfDtEnabled && reportAsOfDtEnabled eq 'false'}">
						<%//Date asOfDateChosen = new Date(Long.parseLong(blockOfBusinessForm.getAsOfDateSelected())); 
							Date asOfDate = BlockOfBusinessUtility.getMonthEndDates().get(0);%>
						<p><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>"	
								value="<%=asOfDate.toString()%>" /></p> 
					</c:if>
				</td>
				
				<td>
					<form:select id="quickFilterSelected" path="quickFilterSelected"  onchange="showFilterExpression('true');">
					
					                <form:options items="${pageScope.quickFilterList}"  itemValue="value"
													itemLabel="label"/>
					</form:select>
				</td>
				<td>
					<%-- The below are the Filtering Expressions, of which only one is shown based on the Filter Category selected.--%>
					<input id="<%=BDConstants.FILTER_BLANK_CODE%>" type="text" readOnly="true" value="" size="10" style="display:none;"/>
					
					<%if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_CONTRACT_NAME)) { %>
					<form:input  id="<%=BlockOfBusinessReportData.FILTER_CONTRACT_NAME%>" path="quickFilterContractName" maxlength="30" cssStyle="display:none"/>
					<% } %>
					
					<%if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER)) { %>
						<form:input  id="<%=BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER%>" path="quickFilterContractNumber" maxlength="7" size="15" cssStyle="display:none;float:none"/>
					<% } %>
					
					<%if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_RPV_NAME)) { %>
					       <form:select id="<%=BlockOfBusinessReportData.FILTER_RPV_NAME%>" path="quickFilterRvpSelected" disabled="false" cssStyle="display:none; max-width:260px;float:none">
								<form:option value=""></form:option>
								<c:forEach items="${pageScope.rvpNamesList}" var="rvpName">
					                <form:option value="${rvpName.id}" >${rvpName.lastName}, &nbsp;${rvpName.firstName}</form:option>
					      		</c:forEach>
					       </form:select>
					<% } %>
					
					<%if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)) { %>
						<%if (userProfile.getRole() instanceof BDFirmRep) { %>
						   <form:select  id="<%=BlockOfBusinessReportData.FILTER_BDFIRM_NAME%>" path="quickFilterFirmIDSelected" disabled="false" style="display:none;float:none">
								<form:option value="">Select</form:option>
								<c:forEach items="${pageScope.bdFirmRepAssociatedFirmsList}" var="bdFirmInfo">
									<form:option value="${bdFirmInfo.id}" >${bdFirmInfo.firmName}</form:option>
								</c:forEach>
						   </form:select>
						<%} else if (userProfile.getRole() instanceof BDRvp){
							String rvpPartyID = BlockOfBusinessUtility.getRVPPartyId(userProfile);
							getJspContext().setAttribute("rvpPartyID", rvpPartyID);
						%>
							<bob:bobFirmSearch firmSearchDivID="quickFilterFirmSearchDIV" inputIDForFirmID="quickFilterFirmIDSelected" inputNameForFirmID="quickFilterFirmIDSelected" inputValueForFirmID="${blockOfBusinessForm.quickFilterFirmIDSelected}" IDfirmSearchContainer="firmSearchContainer1" inputIDForFirmName="<%=BlockOfBusinessReportData.FILTER_BDFIRM_NAME%>" inputNameForFirmName="quickFilterFirmNameSelected" inputValueForFirmName="${blockOfBusinessForm.quickFilterFirmNameSelected}" rvpId="${rvpPartyID}"/>	
						<%} else {%>
							<bob:bobFirmSearch firmSearchDivID="quickFilterFirmSearchDIV" inputIDForFirmID="quickFilterFirmIDSelected" inputNameForFirmID="quickFilterFirmIDSelected" inputValueForFirmID="${blockOfBusinessForm.quickFilterFirmIDSelected}" IDfirmSearchContainer="firmSearchContainer1" inputIDForFirmName="<%=BlockOfBusinessReportData.FILTER_BDFIRM_NAME%>" inputNameForFirmName="quickFilterFirmNameSelected" inputValueForFirmName="${blockOfBusinessForm.quickFilterFirmNameSelected}"/> 
						<%} %>
					<% } %>
			       
			       <%if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE)) { %>
					   <form:select id="<%=BlockOfBusinessReportData.FILTER_PRODUCT_TYPE%>"  path="quickFilterProductTypeSelected" disabled="false" style="display:none;float:none">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.productTypeList}" var="productType">
								<form:option value="${productType.value}" >${productType.label}</form:option>
							</c:forEach>
					   </form:select>
					<% } %>
			       
			       
					<%if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_US_OR_NY)) { %>
					   <form:select id="<%=BlockOfBusinessReportData.FILTER_US_OR_NY%>" path="quickFilterUsOrNySelected" disabled="false" style="display:none;float:none">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.usNyList}" var="usNyInfo">
								<form:option value="${usNyInfo.value}" >${usNyInfo.label}</form:option>
							</c:forEach>
					   </form:select>
					<% } %>
			       
					<%if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_FUND_CLASS)) { %>
					   <form:select id="<%=BlockOfBusinessReportData.FILTER_FUND_CLASS%>" path="quickFilterFundClassSelected" disabled="false" style="display:none;float:none">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.fundClassList}" var="fundClass">
								<form:option value="${fundClass.code}" >${fundClass.description}</form:option>
							</c:forEach>
						</form:select>
					<% } %>
				</td>
			</tr>
		</table>
	</bd:form>
</c:if>

<c:if test="${filterType eq 'advancedFilter'}">

<% 



	getJspContext().setAttribute("usNyList", BlockOfBusinessUtility.getUSNYList());
	getJspContext().setAttribute("fundClassList", BlockOfBusinessUtility.getFundClassesList());
	getJspContext().setAttribute("statesList", BlockOfBusinessUtility.getStatesList());
	
	if(blockOfBusinessForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_CSF_FEATURE)) {
		getJspContext().setAttribute("csfList", BlockOfBusinessUtility.getCSFFeatures());
	}
	if(blockOfBusinessForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_RPV_NAME)) {
		getJspContext().setAttribute("rvpNamesList", BlockOfBusinessUtility.getAllRVPs());
	}
	if(blockOfBusinessForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)) {
		getJspContext().setAttribute("bdFirmRepAssociatedFirmsList", BlockOfBusinessUtility.getAssociatedFirmsForBDFirmRep(userProfile));
	}
	if(blockOfBusinessForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_SALES_REGION)) {
		getJspContext().setAttribute("salesRegionList", BlockOfBusinessUtility.getAllSalesRegions());
	}
	if(blockOfBusinessForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_SALES_DIVISION)) {
		getJspContext().setAttribute("salesDivisionList", BlockOfBusinessUtility.getAllSalesDivisions());
	}
	if(blockOfBusinessForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE)) {
		getJspContext().setAttribute("productTypeList", BlockOfBusinessUtility.getProductTypeList());
	}
%>

	<bd:form id="advancedFilterForm" method="post" action="${actionURL}" modelAttribute="blockOfBusinessForm" name="blockOfBusinessForm">
		<input type="hidden" name="task" value="filter"/>
		<input type="hidden" name="fromQuickFilter" value="false"/>
		<input type="hidden" name="asOfDateSelected"/>
		<table width="100%">

<%--
A user will not have all the advanced filters shown. In this scenario, we want to fit the advanced filters 
properly so that each row shows the applicable 3 filters. For this reason, we need to determine which of 
the filters are applicable for the user and based on that, build rows containing 3 filters per row.

In the below code, I am getting the applicable advanced filters into a Map "advFiltersUsedStatusMap".
I am running thru a for loop thru all the applicable advanced filters, finding 3 applicable filters. Once I
find the first 3 applicable filters, I am printing them as a row, and then, continuing to find the next set of
3 applicable filters.  
--%>
			<%
			Map<String, Boolean> advFiltersUsedStatusMap = blockOfBusinessForm.getAdvancedFilterUsedStatusMap();
			
			for (int advFiltersCount=0; advFiltersCount< advFiltersUsedStatusMap.size() ; advFiltersCount++) {
			    int filtersAllowedPerRow=0;
			%>
				<tr>			        
			
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_CONTRACT_NAME) != null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_CONTRACT_NAME)) { %>
					<td>Contract Name:</td>
					<td><form:input id="<%=appendPrefix(BlockOfBusinessReportData.FILTER_CONTRACT_NAME)%>" path="contractName" maxlength="30"/></td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_CONTRACT_NAME, Boolean.TRUE);
				  } 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER)!= null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER)) { %>
					<td>Contract Number:</td>
					<td><form:input id="<%=appendPrefix(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER)%>" path="contractNumber" maxlength="7"/>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER, Boolean.TRUE);
				  } 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_CONTRACT_STATE) != null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_CONTRACT_STATE)) { %>
					<td>Contract State:</td>
					<td>
				       <form:select  path="contractState" disabled="false">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.statesList}" var="state">
				                <form:option value="${state.code}" >${state.code}</form:option>
				      		</c:forEach>
				       </form:select>
					</td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_CONTRACT_STATE, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM) != null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM)) { %>
					<td>Total Assets From:&nbsp;</td>
					<td><form:input  id="assetRangeFromID" path="assetRangeFrom" maxlength="17"/></td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_ASSET_RANGE_TO)!= null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_ASSET_RANGE_TO)) { %>
					<td>To: &nbsp;</td>
					<td><form:input id="assetRangeToID" path="assetRangeTo" maxlength="17"/></td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_ASSET_RANGE_TO, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_FINANCIAL_REP_NAME)!= null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_FINANCIAL_REP_NAME)) { %>
					<td>Financial Rep Name:</td>
					<td><form:input  path="financialRepName" maxlength="60"/></td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_FINANCIAL_REP_NAME, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE) != null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE)) { %>
					<td>Product Type:</td>
					<td>
				       <form:select id="<%=appendPrefix(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE)%>"  path="productType" disabled="false">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.productTypeList}" var="productType">
								<form:option value="${productType.value}" >${productType.label}</form:option>
							</c:forEach>
				       </form:select>
					</td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_US_OR_NY) != null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_US_OR_NY)) { %>
					<td>US / NY:</td>
					<td>
				       <form:select id="<%=appendPrefix(BlockOfBusinessReportData.FILTER_US_OR_NY)%>" path="usNySelected" disabled="false">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.usNyList}" var="usNyInfo">
								<form:option value="${usNyInfo.value}" >${usNyInfo.label}</form:option>
							</c:forEach>
				       </form:select>
					</td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_US_OR_NY, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_FUND_CLASS) != null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_FUND_CLASS)) { %>
					<td>Class:</td>
					<td>
				       <form:select id="<%=appendPrefix(BlockOfBusinessReportData.FILTER_FUND_CLASS)%>"  path="fundClassSelected" disabled="false">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.fundClassList}" var="fundClass">
				                <form:option value="${fundClass.code}" >${fundClass.description}</form:option>
				      		</c:forEach>
				       </form:select>
					</td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_FUND_CLASS, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_RPV_NAME) != null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_RPV_NAME)) { %>
					<td>RVP Name:</td>
					<td>
				       <form:select id="<%=appendPrefix(BlockOfBusinessReportData.FILTER_RPV_NAME)%>" path="rvpSelected" disabled="false">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.rvpNamesList}" var="rvpName">
				                <form:option value="${rvpName.id}" >${rvpName.lastName}, &nbsp;${rvpName.firstName}</form:option>
				      		</c:forEach>
				       </form:select>
					</td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_RPV_NAME, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_SALES_REGION) != null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_SALES_REGION)) { %>
					<td>Region:</td>
					<td>
				       <form:select  path="salesRegionSelected" disabled="false">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.salesRegionList}" var="salesRegion">
				                <form:option value="${salesRegion.id}" >${salesRegion.name}</form:option>
				      		</c:forEach>
				       </form:select>
					</td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_SALES_REGION, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_SALES_DIVISION) != null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_SALES_DIVISION)) { %>
					<td>Division:</td>
					<td>
				       <form:select  path="salesDivisionSelected" disabled="false">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.salesDivisionList}" var="salesDivision">
				                <form:option value="${salesDivision.id}" >${salesDivision.name}</form:option>
				      		</c:forEach>
				       </form:select>
					</td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_SALES_DIVISION, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
				<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_CSF_FEATURE) != null && 
				        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_CSF_FEATURE)) { %>
					<td>Contract Service Features:</td>
					<td>
				       <form:select style="width:231px" Id="<%=appendPrefix(BlockOfBusinessReportData.FILTER_CSF_FEATURE)%>"  path="csfFeatureSelected" disabled="false">
							<form:option value=""></form:option>
							<c:forEach items="${pageScope.csfList}" var="csfFeature">
				                <form:option value="${csfFeature.value}" >${csfFeature.label}</form:option>
				      		</c:forEach>
				      		<c:if test = "${userProfile.isUBSAdvisor()}" >
								<form:option value="ubs321" >UBS RPA 3(21)</form:option>
							</c:if>
				      		<c:if test = "${userProfile.isRJAdvisor()}"  >
								<form:option value="rjames338" >Raymond James 3(38) Retirement Plan Solution Service</form:option>
							</c:if>   
							           
				       </form:select>
					</td>
				<%	advFiltersCount++;
					filtersAllowedPerRow++;
					advFiltersUsedStatusMap.put(BlockOfBusinessReportData.FILTER_CSF_FEATURE, Boolean.TRUE);
					} 
			      if(filtersAllowedPerRow==3) {
				%>
			        </tr>
			    <%  advFiltersCount--;  
					continue;
			      }
				%>
			<%
			}
			%>

			<%
				if (advFiltersUsedStatusMap.size() != 0 && advFiltersUsedStatusMap.size() % 3 != 0) {
			%>
					</tr>
			<%
				}
			%>

			<%--The Firm Name filter criteria should always be in its own row spanning all the 3 columns  --%>
			<%if(advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)!= null && 
			        !advFiltersUsedStatusMap.get(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)) { %>
			    <tr>
					<td>Firm Name:</td>  
					<td colspan="5">
						<%if (userProfile.getRole() instanceof BDFirmRep) { %>
						   <form:select Id="<%=appendPrefix(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)%>"  path="firmIDSelected" disabled="false">
								<form:option value="">Select</form:option>
								<c:forEach items="${pageScope.bdFirmRepAssociatedFirmsList}" var="bdFirmInfo">
									<form:option value="${bdFirmInfo.id}" >${bdFirmInfo.firmName}</form:option>
								</c:forEach>
						   </form:select>
						<%} else if (userProfile.getRole() instanceof BDRvp){
							String rvpPartyID = BlockOfBusinessUtility.getRVPPartyId(userProfile);
							getJspContext().setAttribute("rvpPartyID", rvpPartyID);
						%>
							<bob:bobFirmSearch firmSearchDivID="firmSearchDivID" inputIDForFirmID="firmIDSelected" inputNameForFirmID="firmIDSelected" inputValueForFirmID="${blockOfBusinessForm.firmIDSelected}" IDfirmSearchContainer="firmSearchContainer2" inputIDForFirmName="<%=appendPrefix(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)%>" inputNameForFirmName="firmNameSelected" inputValueForFirmName="${blockOfBusinessForm.firmNameSelected}" variableName="oAC2" rvpId="${rvpPartyID}" prepopulatedFirmNameVar="prepopulatedFirmName2"/>	
						<%} else {%>
							<bob:bobFirmSearch firmSearchDivID="firmSearchDivID" inputIDForFirmID="firmIDSelected" inputNameForFirmID="firmIDSelected" inputValueForFirmID="${blockOfBusinessForm.firmIDSelected}" IDfirmSearchContainer="firmSearchContainer2" inputIDForFirmName="<%=appendPrefix(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)%>" inputNameForFirmName="firmNameSelected" inputValueForFirmName="${blockOfBusinessForm.firmNameSelected}" variableName="oAC2" prepopulatedFirmNameVar="prepopulatedFirmName2"/>
						<%} %>
					</td>
				</tr>
			<%} %>
			
		</table>
		<div class="selection_input">
			<div class="button_search"><input type="button" value="Reset" id="cancel_customization" onClick="resetBOB(this.form);" /></div>
			<div class="button_search"><input type="button" value="Submit" id="apply_customization" onClick="submitBOB(this.form);" /></div>
		</div>
	</bd:form><!--#report__adv_customization-->
</c:if>