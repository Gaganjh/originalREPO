<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%@ page import="com.manulife.pension.ps.web.Constants,
                 com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.platform.web.util.DataUtility" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationDetailsReportData" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@page import="com.manulife.pension.ps.web.util.Environment"%>

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
	
	InvestmentAllocationDetailsReportData theReport = (InvestmentAllocationDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
}

Integer GLOBAL_DISCLOSURE = ContentConstants.GLOBAL_DISCLOSURE;
String TYPE_MISCELLANEOUS= ContentConstants.TYPE_MISCELLANEOUS;
pageContext.setAttribute("GLOBAL_DISCLOSURE",GLOBAL_DISCLOSURE,PageContext.PAGE_SCOPE);
pageContext.setAttribute("TYPE_MISCELLANEOUS",TYPE_MISCELLANEOUS,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=ContentConstants.FIXED_FOOTNOTE_PBA%>"
                           	type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>"
                          	id="footnotePBA"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_NO_PARTICIPANTS_INVESTED_IN_THE_FUND%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningNoParticipantsInvestedInTheFund"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
    <td width="100%" valign="top">
    <p><content:errors scope="session"/></p>
      <ps:form method="GET" modelAttribute="investmentAllocationPageForm" name="investmentAllocationPageForm" action="/do/investment/investmentAllocationDetailsReport/" >

	  <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="170"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="80"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="80"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="80"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="65"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="4"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" ></td>
        </tr>

        <tr>
          <td class="tableheadTD1" colspan="16">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="tableheadTD"><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> as of
                	<ps:select name="investmentAllocationPageForm" property="<%=InvestmentAllocationReportData.FILTER_ASOFDATE_DETAILS%>"
	                       onchange="setFilterFromSelect(this);doFilter();">
 	                     <ps:dateOption name="<%=Constants.USERPROFILE_KEY%>"
  	                                   	property="currentContract.contractDates.asOfDate"
   	                                  	renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
    	                  <ps:dateOptions name="<%=Constants.USERPROFILE_KEY%>"
     	                                property="currentContract.contractDates.monthEndDates"
      	                                renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
                </ps:select>

                </td>
                <td class="tableheadTDinfo" colspan="3"><b><report:recordCounter report="theReport" label="Participants"/></b></td>
 	            <td align="right" class="tableheadTDinfo"><report:pageCounter report="theReport" formName="investmentAllocationPageForm"/></td>
              </tr>
            </table>
          </td>
        </tr>

        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

          <td rowspan="999" class="whiteBox" valign="top"><br>&nbsp;

<c:if test="${empty param.printFriendly }" >
	<span class="highlight"><b>Fund name</b><br></span>&nbsp;
<c:if test="${theReport.hideOngoingContributions ==false}">
<c:if test="${theReport.summary.fundId !='PBA'}">
<c:if test="${theReport.summary.fundId !='NPB'}">
<c:if test="${theReport.summary.rateType == SIG_PLUS_RATE_TYPE||theReport.summary.rateType == CY1_RATE_TYPE||theReport.summary.rateType == CY2_RATE_TYPE}">
<a href="#fundsheet" onMouseOver='self.status="Fund Sheet"; return true' NAME="${e:forHtmlAttribute(theReport.summary.fundId)}" onClick='FundWindow("<ps:fundLink fundIdProperty="theReport.summary.fundId" fundTypeProperty="theReport.summary.fundType" rateType ="theReport.summary.rateType" productId ="<%=userProfile.getCurrentContract().getProductId()%>" fundSeries ='<%=userProfile.getCurrentContract().getFundPackageSeriesCode()%>' siteLocation='<%= Environment.getInstance().getSiteLocation() %>' />")'><span class="datacell1"><b>${theReport.summary.fundName}</b></span></a>
</c:if>
<c:if test="${theReport.summary.rateType != SIG_PLUS_RATE_TYPE && theReport.summary.rateType != CY1_RATE_TYPE && theReport.summary.rateType != CY2_RATE_TYPE}">
<a href="#fundsheet" onMouseOver='self.status="Fund Sheet"; return true' NAME='${theReport.summary.fundId}' onClick='FundWindow("<ps:fundLink fundIdProperty="theReport.summary.fundId" fundTypeProperty="theReport.summary.fundType" rateType ="userProfile.currentContract.defaultClass" productId ="<%=userProfile.getCurrentContract().getProductId()%>" fundSeries ='<%=userProfile.getCurrentContract().getFundPackageSeriesCode()%>' siteLocation='<%= Environment.getInstance().getSiteLocation() %>' />")'><span class="datacell1"><b>${theReport.summary.fundName}</b></span></a>
</c:if>
</c:if>
<c:if test="${theReport.summary.fundId =='NPB'}">
<span class="datacell1"><b>${theReport.summary.fundName}</b><sup><b>&#134;</b></sup></b></span>
</c:if>
</c:if>
<c:if test="${theReport.summary.fundId =='PBA'}">
<span class="datacell1"><b>${theReport.summary.fundName}</b><sup><b>&#134;</b></sup></b></span>
</c:if>
</c:if>
<c:if test="${theReport.hideOngoingContributions ==true}">
<span class="datacell1"><b>${theReport.summary.fundName}</b></span>
</c:if>
</c:if>
<c:if test="${not empty param.printFriendly }" >
<span class="highlight"><b>${theReport.summary.fundName}</b></span>
</c:if>
              <br>
              <br>

      &nbsp;<span class="highlight"><b>Participants invested: </b></span><b><render:number property='theReport.summary.participantsCount' defaultValue = "0"  pattern="########0"/><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="5" valign="top" class="datacell1">&nbsp;</td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top" class="datacell1"><b>Employee assets ($)</b></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top" class="datacell1"><b>Employer<br>
          assets ($)</b></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" align="right" valign="top" class="datacell1"><b>Total
              <br>
            assets ($)</b></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="5" align="right" valign="top" class="datacell1">Totals</td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top" class="datacell1"><render:number property='theReport.summary.employeeAssetsTotal' defaultValue = "0.00"  pattern="###,###,##0.00"/></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top" class="datacell1"><render:number property='theReport.summary.employerAssetsTotal' defaultValue = "0.00"  pattern="###,###,##0.00"/></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" align="right" valign="top" class="datacell1"><render:number property='theReport.summary.assetsTotal' defaultValue = "0.00"  pattern="###,###,##0.00"/></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="5" valign="top" class="datacell1">&nbsp;</td>
          <td valign="top" class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="datacell1">&nbsp;</td>
          <td valign="top" class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="datacell1">&nbsp;</td>
          <td valign="top" class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top" class="datacell1">&nbsp;</td>
          <td valign="top" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="3" valign="top" class="tablesubhead"><report:sort field="name" direction="asc" formName="investmentAllocationPageForm"><b>Name/SSN</b></report:sort></td>
          <!--<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="tablesubhead"><b>SSN</b></td>-->
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="left" valign="top" class="tablesubhead">
<c:if test="${theReport.hideOngoingContributions ==false}">
            <report:sort field="ongoingContributions" direction="asc" formName="investmentAllocationPageForm"><b>Ongoing
                contributions ?</b></report:sort>
</c:if>
<c:if test="${theReport.hideOngoingContributions ==true}"><b>Ongoing
                contributions ?</b>
</c:if>
          </td>
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top" class="tablesubhead"><report:sort field="employeeAssetsAmount" direction="desc" formName="investmentAllocationPageForm"><b>Employee<br>assets</report:sort>&nbsp;($)</b></td>
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top" class="tablesubhead"><report:sort field="employerAssetsAmount" direction="desc" formName="investmentAllocationPageForm"><b>Employer <br>assets</report:sort>&nbsp;($)</b></td>
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" align="right" valign="top" class="tablesubhead"><report:sort field="totalAssetsAmount" direction="desc" formName="investmentAllocationPageForm"><b>Total<br>assets</report:sort>&nbsp;($)</b></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<%InvestmentAllocationDetailsReportData report = (InvestmentAllocationDetailsReportData)pageContext.getAttribute("theReport");  %>
<% if (report.getDetails().isEmpty()) { %>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="left" valign="top" colspan="12"><content:getAttribute id="warningNoParticipantsInvestedInTheFund" attribute="text"/></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

        <tr>
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="3" class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <!--<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>-->
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="2"  colspan="2" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
        </tr>

<% } else { %>

<% String rowClass="datacell1"; %>
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/> 
<c:set var="participantId" value="${theItem.participantId}"/>
<% String temp = (String) pageContext.getAttribute("indexValue").toString();
String participantId=(String) pageContext.getAttribute("participantId");
%>

<% if (Integer.parseInt(temp) % 2 == 1) {
    rowClass="datacell1";
   } else {
    rowClass="datacell2";
   } %>

        <tr class="<%=rowClass%>">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="3" valign="top" nowrap><ps:link action='<%="/do/participant/participantAccount/?participantId="+participantId%>'>${theItem.name}<br><render:ssn property='theItem.ssn'/></ps:link></td>
          <!--<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" nowrap><render:ssn property='theItem.ssn'/></td>-->
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="left" valign="top">
<c:if test="${theReport.hideOngoingContributions ==false}">
${theItem.ongoingContributions}
</c:if>
<c:if test="${theReport.hideOngoingContributions ==true}">
            n/a
</c:if>
          </td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top" nowrap><render:number property='theItem.employeeAssetsAmount' defaultValue = "0.00"  pattern="###,###,##0.00"/></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top" nowrap><render:number property='theItem.employerAssetsAmount' defaultValue = "0.00"  pattern="###,###,##0.00"/></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" align="right" valign="top" nowrap><render:number property='theItem.totalAssetsAmount' defaultValue = "0.00"  pattern="###,###,##0.00"/></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:forEach>

        <tr class="<%=rowClass%>">
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="3" class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <!--<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>-->
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="2"  colspan="2" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif"  width="5" height="5"></td>
        </tr>
<% } %>
		<tr>
          <td class="databorder" colspan="14"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		<tr>
		  <td colspan="2" align="right" bgcolor="#FFF9F2"></td>
		  <td colspan="13" align="right" ><report:pageCounter report="theReport" arrowColor="black" formName="investmentAllocationPageForm"/></td>
		</tr>

      </table>
      </td>
      </ps:form>
   </tr>


   	<tr>
	   	<td colspan="15" width="100%">
			<br>
			<p class="footnote">
<c:if test="${theReport.summary.fundId == 'PBA'}">
					<content:getAttribute beanName="footnotePBA" attribute="text"/>
</c:if>
<c:if test="${theReport.summary.fundId == 'NPB'}">
					<content:getAttribute beanName="footnotePBA" attribute="text"/>
</c:if>
			</p>
 		</td>
	</tr>


	<tr>
		<td colspan="15">
			<br>
			<p><content:pageFooter beanName="layoutPageBean"/></p>
 			<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 		</td>
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
