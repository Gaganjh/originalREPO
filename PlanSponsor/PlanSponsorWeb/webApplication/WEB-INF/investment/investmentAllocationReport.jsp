<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.Constants,
                 com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.FundCategory" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData" %>
<%@page import="com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails" %>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
String SIG_PLUS_RATE_TYPE = Constants.RATE_TYPE_CX0;
pageContext.setAttribute("SIG_PLUS_RATE_TYPE",SIG_PLUS_RATE_TYPE,PageContext.PAGE_SCOPE);
String CY1_RATE_TYPE=Constants.RATE_TYPE_CY1;
pageContext.setAttribute("CY1_RATE_TYPE",CY1_RATE_TYPE,PageContext.PAGE_SCOPE);
String CY2_RATE_TYPE=Constants.RATE_TYPE_CY2;
pageContext.setAttribute("CY2_RATE_TYPE",CY2_RATE_TYPE,PageContext.PAGE_SCOPE);



if(request.getAttribute(Constants.REPORT_BEAN) != null){
	
	InvestmentAllocationReportData theReport = (InvestmentAllocationReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
}
	String SITEMODE_USA=Constants.SITEMODE_USA;
	String SITEMODE_NY=Constants.SITEMODE_NY;
	String LIFECYCLE=FundCategory.LIFECYCLE;
	String LIFESTYLE=FundCategory.LIFESTYLE;
	String NON_LIFESTYLE_LIFECYCLE=FundCategory.NON_LIFESTYLE_LIFECYCLE;
	String GIFL=FundCategory.GIFL;
	String PBA=FundCategory.PBA;
	String VIEW_BY_ACTIVITY=Constants.VIEW_BY_ACTIVITY;
	Integer GLOBAL_DISCLOSURE = ContentConstants.GLOBAL_DISCLOSURE;
	String TYPE_MISCELLANEOUS= ContentConstants.TYPE_MISCELLANEOUS;
	pageContext.setAttribute("GLOBAL_DISCLOSURE",GLOBAL_DISCLOSURE,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("TYPE_MISCELLANEOUS",TYPE_MISCELLANEOUS,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("SITEMODE_USA",SITEMODE_USA,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("SITEMODE_NY",SITEMODE_NY,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("NON_LIFESTYLE_LIFECYCLE",NON_LIFESTYLE_LIFECYCLE,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("LIFECYCLE",LIFECYCLE,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("LIFESTYLE",LIFESTYLE,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("GIFL",GIFL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("PBA",PBA,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("VIEW_BY_ACTIVITY",VIEW_BY_ACTIVITY,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=ContentConstants.FIXED_FOOTNOTE_PBA%>"
                           	type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>"
                          	id="footnotePBA"/>


<script type="text/javascript" >
	function doViewDetails(fundid,fundName){
        document.investmentAllocationPageForm.action = "/do/investment/investmentAllocationDetailsReport/";
		document.investmentAllocationPageForm.selectedFundID.value = fundid;
		document.investmentAllocationPageForm.selectedFundName.value = fundName;
		document.investmentAllocationPageForm.submit();
	}
</script>
<%@ page import="com.manulife.pension.ps.web.investment.InvestmentAllocationPageForm"%>

<% String rowClass = "datacell2"; %>
<% String dataDivider = "dataheaddivider"; %>

<c:set var="singleQuot" value="\'"/>
<c:set var="singleQuotEscaped" value="\\\'"/>
												
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
    <td width="92%" valign="top">

	  <content:errors scope="session" /><br>
	  <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" width="170" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" width="65" height="1"></td>
          <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

<ps:form modelAttribute="investmentAllocationPageForm" name="investmentAllocationPageForm" method="GET" action="/do/investment/investmentAllocationReport/" >

   		<input type=hidden name="selectedFundID"/>
   		<input type=hidden name="selectedFundName"/>
   		<input type=hidden name="actionDetail"/>

        <tr class="tablehead">
          <td class="tableheadTD1" colspan="16"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b> as
            of  &nbsp;<b>
            <ps:isNotInternalOrTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
            	<ps:select name="investmentAllocationPageForm" property="asOfDateReport"
                       onchange="setFilterFromSelect(this);doFilter();">
                      <ps:dateOption name="<%=Constants.USERPROFILE_KEY%>"
                                     property="currentContract.contractDates.asOfDate"
                                     renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
                      <ps:dateOptions name="<%=Constants.USERPROFILE_KEY%>"
                                      property="currentContract.contractDates.monthEndDates"
                                      renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
                </ps:select>
			</ps:isNotInternalOrTpa>
            <ps:isInternalOrTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
	            <ps:select name="investmentAllocationPageForm" property="asOfDateReport"
                       onchange="setFilterFromSelect(this);">
                      <ps:dateOption name="<%=Constants.USERPROFILE_KEY%>"
                                     property="currentContract.contractDates.asOfDate"
                                     renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
                      <ps:dateOptions name="<%=Constants.USERPROFILE_KEY%>"
                                      property="currentContract.contractDates.monthEndDates"
                                      renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
                </ps:select>
				&nbsp;
            	<ps:select name="investmentAllocationPageForm" property="viewOption"
                       onchange="setFilterFromSelect(this);">
                      <ps:option value="<%=Constants.VIEW_BY_ASSET%>">Asset view</ps:option>
                      <ps:option value="<%=Constants.VIEW_BY_ACTIVITY%>">Activity view</ps:option>
                </ps:select>
				&nbsp;
				<c:if test="${empty param.printFriendly}" >
<input type="button" onclick="doFilter(); return false;" name="viewOptionSubmit" value="search"></input>
				</c:if>
			</ps:isInternalOrTpa>
          </td>

        </tr>

<c:if test="${not empty displayDates}">
        <tr>
	        <td colspan="16"><img src="/assets/unmanaged/images/s.gif" width="1" height="0"></td>
	 	</tr>
</table>
</c:if>


<c:if test ="${empty displayDates}">
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

          <td valign="top"><b>
<c:if test="${environment.siteLocation == SITEMODE_USA}">

		   <ps:fundSeriesName location="USA" property ="userProfile.currentContract"/></b></td>

</c:if>

<c:if test="${environment.siteLocation == SITEMODE_NY}">

		   <ps:fundSeriesName location="NY" property ="userProfile.currentContract"/></b></td>

</c:if>

          <td class="datadivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td valign="top"><b>Options with assets</b></td>
          <td class="datadivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td valign="top"><b>Participants invested</b></td>
          <td class="datadivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td valign="top" align="right"><b>Employee assets ($)</b></td>
          <td class="datadivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td valign="top" align="right"><b>Employer assets ($)</b></td>
          <td class="datadivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td valign="top" align="right"><b>Total assets ($)</b></td>
          <td class="datadivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td colspan="2" valign="top" align="right"><b>% of total</b></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>


<% String totalsRowClass = "datacell2"; %>
<% String totalsDataDivider = "dataheaddivider"; %>
<c:forEach items="${theReport.getAllocationTotals()}" var="theAllocationTotals" varStatus="theAllocationTotalsIndex" >
<c:set var="tempValue" value="${theAllocationTotalsIndex.index}"/> 
<% String temp = pageContext.getAttribute("tempValue").toString();
 if (Integer.parseInt(temp) % 2 == 1) {
      totalsRowClass="datacell1";
      totalsDataDivider="datadivider";
 } else {
      totalsRowClass="datacell2";
      totalsDataDivider="dataheaddivider";
 } %>

	<tr class="<%=totalsRowClass%>">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${theAllocationTotals.fundCategoryType == NON_LIFESTYLE_LIFECYCLE}">

<td valign="top">${investmentAllocationPageForm.totalText} </td>
</c:if>
<c:if test="${theAllocationTotals.fundCategoryType == LIFECYCLE}">
                    <td valign="top"><%=InvestmentAllocationPageForm.LIFECYCLE_TEXT%></td>
</c:if>

<c:if test="${theAllocationTotals.fundCategoryType == LIFESTYLE}">
                    <td valign="top"><%=InvestmentAllocationPageForm.LIFESTYLE_TEXT%></td>
</c:if>
<c:if test="${theAllocationTotals.fundCategoryType == GIFL}">
                    <td valign="top"><%=InvestmentAllocationPageForm.GIFL_TEXT%></td>
</c:if>

<c:if test="${theAllocationTotals.fundCategoryType == PBA}" >
                    <td valign="top"><%=InvestmentAllocationPageForm.PBA_TEXT%></td>
</c:if>

          <td class="<%=totalsDataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><span class="highlight"><b><render:number property="theAllocationTotals.numberOfOptions" defaultValue="0" type="i"/></b></span><br>
          </td>
          <td class="<%=totalsDataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><span class="highlight"><b><render:number property="theAllocationTotals.participantsInvested" defaultValue="0" type="i"/></b></span><br>
          </td>
          <td class="<%=totalsDataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right"><span class="highlight"><b><render:number property="theAllocationTotals.employeeAssets" defaultValue="0.00" pattern="###,###,##0.00"/></b></span><br>
          </td>
          <td class="<%=totalsDataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right"><span class="highlight"><b><render:number property="theAllocationTotals.employerAssets" defaultValue="0.00" pattern="###,###,##0.00"/></b></span><br>
          </td>
          <td class="<%=totalsDataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="highlight" align="right"><b><render:number property="theAllocationTotals.totalAssets" defaultValue="0.00" pattern="###,###,##0.00"/></b></td>
          <td class="<%=totalsDataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top" align="right"><b><span class="highlight"><render:number property="theAllocationTotals.percentageOfTotal" pattern="###.##%" defaultValue="0" scale="4" /></span></b><br>
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

</c:forEach>

         <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="14" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
          <td colspan="14"><img src="/assets/unmanaged/images/s.gif" height="10"></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
        </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">

        <tr class="tablehead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="tableheadTD">
            <report:sort field="option" formName="investmentAllocationPageForm" direction="asc">
              <b>Investment option</b>
            </report:sort>
          </td>
          <td class="dataheaddivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>

<c:if test="${investmentAllocationPageForm.asOfDateReportCurrent ==true}">
     		<td valign="top"><b>
     		<report:sort field="participantsInvested" formName="investmentAllocationPageForm" direction="dsc">Participants invested (current/ongoing)</b></report:sort></td>
</c:if>
<c:if test="${investmentAllocationPageForm.asOfDateReportCurrent ==false}">
     		<td valign="top"><b><report:sort field="participantsInvested" formName="investmentAllocationPageForm" direction="asc">Participant invested (current)</b></report:sort></td>
</c:if>

          <td class="dataheaddivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td valign="top" align="right" style="color: white;"><b><report:sort field="employeeAssets" formName="investmentAllocationPageForm" direction="dsc">Employee assets</report:sort> ($)</b></td>
          <td class="dataheaddivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td valign="top" align="right" style="color: white;"><b><report:sort field="employerAssets" formName="investmentAllocationPageForm" direction="dsc">Employer assets</report:sort> ($)</b></td>
          <td class="dataheaddivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td valign="top" align="right" style="color: white;"><b><report:sort field="totalAssets" formName="investmentAllocationPageForm" direction="dsc">Total assets</report:sort> ($)</b></td>
          <td class="dataheaddivider" valign="top"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td colspan="2" valign="top" align="right"><b><report:sort field="percentageOfTotal" formName="investmentAllocationPageForm"  direction="dsc">% of total</report:sort></b></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

<!-- working up here -->

<c:forEach items="${theReport.getAllocationDetails()}" var="theAllocationDetails">
<c:set var="fundCategory" value="${theAllocationDetails.key}" /> 


        <tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td valign="top" colspan="12" bgColor='${fundCategory.getBgColor()}'><font color='${fundCategory.getFontColor()}'><b>${fundCategory.categoryDesc}<b></font></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<c:set var="fundDetails" value="${theAllocationDetails.value}"/>
<c:forEach items="${fundDetails}" var="theFundDetail" varStatus="theFundDetailsIndex" >
<c:set var="indexValue" value="${theFundDetailsIndex.index}"/> 
<% String temp = pageContext.getAttribute("indexValue").toString();
if (Integer.parseInt(temp) % 2 == 1) {
	rowClass = "datacell1";
	dataDivider = "datadivider";
 } else {
    rowClass="datacell2";
	dataDivider = "dataheaddivider";
 } %>

    <tr class="<%=rowClass%>">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top">
<c:if test="${empty param.printFriendly }" >
<c:if test="${fundCategory.categoryCode !='PB'}">
<c:if test="${investmentAllocationPageForm.asOfDateReportCurrent ==true}">
<c:if test="${theFundDetail.rateType == SIG_PLUS_RATE_TYPE||theFundDetail.rateType == CY1_RATE_TYPE||theFundDetail.rateType == CY2_RATE_TYPE}">
<a href="#fundsheet" onMouseOver='self.status="JavaScript needed to open the fund page link"; return true' NAME='${theFundDetail.getFundId()}' onClick='FundWindow("<ps:fundLink fundIdProperty="theFundDetail.fundId" fundTypeProperty="theFundDetail.fundType" rateType ="theFundDetail.rateType" fundSeries = "${userProfile.getCurrentContract().getFundPackageSeriesCode()}" productId ="${userProfile.getCurrentContract().getProductId()}" siteLocation="<%= Environment.getInstance().getSiteLocation() %>" />")'>${theFundDetail.fundName}</a>
</c:if>
<c:if test="${theFundDetail.rateType != SIG_PLUS_RATE_TYPE && theFundDetail.rateType != CY1_RATE_TYPE && theFundDetail.rateType != CY2_RATE_TYPE}">
<a href="#fundsheet" onMouseOver='self.status="JavaScript needed to open the fund page link"; return true' NAME='${theFundDetail.getFundId()}' onClick='FundWindow("<ps:fundLink fundIdProperty="theFundDetail.fundId" fundTypeProperty="theFundDetail.fundType" rateType ="userProfile.currentContract.defaultClass" fundSeries = "${userProfile.getCurrentContract().getFundPackageSeriesCode()}" productId ="${userProfile.getCurrentContract().getProductId()}" siteLocation="<%= Environment.getInstance().getSiteLocation() %>" />")'>${theFundDetail.fundName}</a>
</c:if>
</c:if>
<c:if test="${investmentAllocationPageForm.asOfDateReportCurrent ==false}">
${theFundDetail.fundName}
</c:if>
</c:if>
<c:if test="${fundCategory.categoryCode =='PB'}">
${theFundDetail.fundName}
			<sup><b>&#134;</b></sup></b>
</c:if>
</c:if>

<c:if test="${not empty param.printFriendly }" >
${theFundDetail.fundName}
</c:if>
	      </td>

     <td class="<%=dataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

<c:if test="${investmentAllocationPageForm.asOfDateReportCurrent ==true}">
	<td valign="top">
	<c:set var="fundDetail" value="${theFundDetail}"/> 
	<% AllocationDetails details = (AllocationDetails) pageContext.getAttribute("fundDetail");%>	
	 <% if ((details.getParticipantsInvestedCurrent() + details.getParticipantsInvestedFuture()) == 0) { %>
          0
	<%} else if (details.getParticipantsInvestedCurrent() >= 0) { %> 
		<c:if test="${empty param.printFriendly }" >
<c:if test="${investmentAllocationPageForm.viewOption == VIEW_BY_ACTIVITY}">
          		${theFundDetail.getParticipantsInvestedCurrent()}/${theFundDetail.getParticipantsInvestedFuture()}&nbsp;
</c:if><c:set var="documentNameEscaped" value="${fn:replace(theFundDetail.getFundName(), singleQuot, singleQuotEscaped)}"/>
<c:if test="${investmentAllocationPageForm.viewOption != VIEW_BY_ACTIVITY}">
          		<a href="javascript:doViewDetails('${theFundDetail.getFundId()}','${fn:replace(theFundDetail.getFundName(), singleQuot, singleQuotEscaped)}')">${theFundDetail.getParticipantsInvestedCurrent()}/${theFundDetail.getParticipantsInvestedFuture()}&nbsp;(Details)</a>
</c:if>
		</c:if>
		<c:if test="${not empty param.printFriendly }" >
${theFundDetail.participantsInvestedCurrent}/${theFundDetail.participantsInvestedFuture}&nbsp;
		</c:if>
<% } %>
</c:if>

<c:if test="${investmentAllocationPageForm.asOfDateReportCurrent ==false}">
		<td valign="top">
		<c:if test="${theFundDetail.participantsInvestedCurrent > 0}">
			<c:if test="${empty param.printFriendly }" >
<c:if test="${investmentAllocationPageForm.viewOption == VIEW_BY_ACTIVITY}">
				   	${theFundDetail.getParticipantsInvestedCurrent()}&nbsp;
</c:if>
<c:if test="${investmentAllocationPageForm.viewOption != VIEW_BY_ACTIVITY}">
			   		<a href="javascript:doViewDetails('${theFundDetail.getFundId()}','${fn:replace(theFundDetail.getFundName(), singleQuot, singleQuotEscaped)}')">${theFundDetail.getParticipantsInvestedCurrent()}&nbsp;(Details)</a>
</c:if>
			</c:if>
		    <c:if test="${not empty param.printFriendly }" >
${theFundDetail.participantsInvestedCurrent}
		    </c:if>
		</c:if>
		</td>
</c:if>

          <td class="<%=dataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right"><render:number property="theFundDetail.employeeAssets" defaultValue="0" pattern="###,###,##0.00"/><br></td>
          <td class="<%=dataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right"><render:number property="theFundDetail.employerAssets" defaultValue="0" pattern="###,###,##0.00"/><br></td>
          <td class="<%=dataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right"><render:number property="theFundDetail.totalAssets" defaultValue="0" pattern="###,###,##0.00"/><br></td>
          <td class="<%=dataDivider%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top" align="right"><render:number property="theFundDetail.percentageOfTotal" pattern="###.##%" defaultValue="0" scale="4" /><br></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:forEach>

</c:forEach>
        <tr class="<%=rowClass%>">
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td class="lastrow"><img src=/assets/unmanaged/images/s.gif width="1" height="1"></td>
          <td class="<%=dataDivider%>"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="<%=dataDivider%>"><img src=/assets/unmanaged/images/s.gif width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="<%=dataDivider%>"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="<%=dataDivider%>"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="<%=dataDivider%>"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="2"  colspan="2" width="5" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
        </tr>

        <tr>
          <td colspan="12" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr>
	      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td><img src="/assets/unmanaged/images/s.gif" width="170" height="1"></td>
		  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
		  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
		  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
		  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td><img src="/assets/unmanaged/images/s.gif" width="65" height="1"></td>
		  <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
		  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

		<tr>
	   		<td width="1%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
	   		<td colspan="13" width="100%">
				<br>
				<p class="footnote">
<c:if test="${userProfile.currentContract.PBA ==true}">
						<content:getAttribute beanName="footnotePBA" attribute="text"/>
</c:if>
				</p>
  			</td>
		</tr>

		<tr>
			<td colspan="13">
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
		</tr>

	</table>
    </td>

</c:if>
    </ps:form>
  </tr>
</table>

<c:if test="${not empty param.printFriendly }" >
	<content:contentBean contentId="${GLOBAL_DISCLOSURE}"
        type="${TYPE_MISCELLANEOUS}"
        id="globalDisclosure"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
