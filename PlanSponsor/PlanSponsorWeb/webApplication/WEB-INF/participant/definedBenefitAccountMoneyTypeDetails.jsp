<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@page import="com.manulife.pension.ps.web.util.Environment"%>
<%@page import="com.manulife.pension.ps.service.participant.valueobject.InvestmentOptionVO"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

   <!-- Inlcude common -->        
   

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<jsp:include page="/WEB-INF/participant/definedBenefitAccountCommon.jsp" flush="true" /> 
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
     <c:if test="${empty requestScope.errors}">        
   
		<script languange="javascript">   
		// This function is implementedto be executed during onLoad.
		// PPR. 
		function doOnload() {
		  //var tabs = document.getElementById("tabs");
		  //alert("tabs="+tabs.style.top);
		  scroll(0,450);
		}
		</script>   
   
        <% int style=1; %> 
        
       
        <tr>
          <td colspan="14" height="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>


        <tr>
		 <ps:isInternalOrTpa name ="userProfile" property ="principal.role">
         	<td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${participantAccountForm.showNetContribEarnings ==true}">
			<!--if after tax money - display the after tax money tab-->             
				<c:if test="${empty param.printFriendly }" >
            		<td colspan="16" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccount/');"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeSummary/');"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeDetails/');"></a><img src="/assets/unmanaged/images/money_type_details_2.gif" width="118" height="30" border ="0"></a></td>
          		</c:if>  
          		<c:if test="${not empty param.printFriendly }" >  
            		<td colspan="16" valign="bottom" class="box"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/money_type_details_2.gif" width="118" height="30"></td>          
          		</c:if>
</c:if>
<c:if test="${participantAccountForm.showNetContribEarnings ==false}">
				<!--if no after tax money - suppress the after tax money tab-->            
				<c:if test="${empty param.printFriendly }" >
            		<td colspan="12" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccount/');"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeSummary/');"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeDetails/');"></a><img src="/assets/unmanaged/images/money_type_details_2.gif" width="118" height="30" border ="0"></a></td>
          		</c:if>  
          		<c:if test="${not empty param.printFriendly }" >  
            		<td colspan="12" valign="bottom" class="box"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/money_type_details_2.gif" width="118" height="30"></td>          
          		</c:if>
</c:if>
          <td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </ps:isInternalOrTpa>
		
		<ps:isNotInternalOrTpa name ="userProfile" property ="principal.role">
        	<td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${participantAccountForm.showNetContribEarnings ==true}">
			<!--if after tax money - display the after tax money tab-->              	
				<c:if test="${empty param.printFriendly }" >
            		<td colspan="12" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccount/');"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeSummary/');"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeDetails/');"></a><img src="/assets/unmanaged/images/money_type_details_2.gif" width="118" height="30" border ="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountNetContribEarnings/');"><img src="/assets/unmanaged/images/after_tax_tab_off.gif" width="118" height="30" border="0"></a></td>
          		</c:if>  
          		<c:if test="${not empty param.printFriendly }" >  
            		<td colspan="12" valign="bottom" class="box"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/money_type_details_2.gif" width="118" height="30"><img src="/assets/unmanaged/images/after_tax_tab_off.gif" width="118" height="30" border="0"></td>          
          		</c:if>
</c:if>
<c:if test="${participantAccountForm.showNetContribEarnings ==false}">
				<!--if no after tax money - suppress the after tax money tab-->            	
				<c:if test="${empty param.printFriendly }" >
            		<td colspan="12" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccount/');"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeSummary/');"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeDetails/');"></a><img src="/assets/unmanaged/images/money_type_details_2.gif" width="118" height="30" border ="0"></a></td>
          		</c:if>  
          		<c:if test="${not empty param.printFriendly }" >  
            		<td colspan="12" valign="bottom" class="box"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/money_type_details_2.gif" width="118" height="30"></td>          
          		</c:if>
</c:if>
          <td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </ps:isNotInternalOrTpa>	
		
		</tr>  
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="24%" colspan="4" valign="bottom" class="tablesubhead"><b>Investment option</b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="1" valign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="85" align="right" valign="bottom" class="tablesubhead"><b>Number<br> 
          of units</b></td>
          <td align="right" valign="bottom" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td width="95" align="right" valign="bottom" class="tablesubhead"><b>Unit value($) /<br> 
          interest rate</b></td>
          <td width="1" align="right" valign="bottom" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>          
          <td width="98" align="right" valign="bottom" class="tablesubhead"><b>Balance($)</b></td>
          <td width="1" align="right" valign="bottom" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td width="88" align="right" valign="bottom" class="tablesubhead"><b>Percentage <br>
          of total(%)</b></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

<c:if test="${participantAccountForm.hasInvestments ==false}">
<tr>
  <td height="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  <td height="1" width="24%" colspan="4"></td>
  <td height="1" width="1"></td>
  <td height="1" width="85" align="right"></td>
  <td height="1" width="1" align="right"></td>
  <td height="1" width="95" align="right"></td>
  <td height="1" width="1" align="right"></td>          
  <td width="98" align="right" ></td>
  <td height="1" width="1" align="right"></td>
  <td height="1" width="88" align="right"></td>
  <td height="1" class="databorder"></td>
</tr>
<tr>		
<td class="databorder" width="1"></td>
<td colspan="12">
<content:getAttribute id="NoParticipantsMessage" attribute="text"/>
</td>
<td class="databorder" width="1"></td>
</tr>
</c:if>
<c:forEach items="${groups}" var="option" >
<%-- <c:set var="optionSize" value="option.participantFundSummaryArray" />  --%>
<c:if test="${not empty option.participantFundSummaryArray}">

<tr>
  <td height="1" width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  <td height="1" width="24%" colspan="4"></td>
  <td height="1" width="1"></td>
  <td height="1" width="85" align="right"></td>
  <td height="1" width="1" align="right"></td>
  <td height="1" width="95" align="right"></td>
  <td height="1" width="1" align="right"></td>          
  <td width="98" align="right" ></td>
  <td height="1" width="1" align="right"></td>
  <td height="1" width="88" align="right"></td>
  <td height="1" width="1" class="databorder"></td>
</tr>

<% if(style==1) style=2;else style=1; %>
        <tr>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td  width="24%" colspan="4" class="datacell<%=style%>"><b>
          
          
${option.category.categoryDesc}
          
          <img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="85" align="right" class="datacell<%=style%>"></td>
          <td width="1" align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="95" align="right" class="datacell<%=style%>"></td>
          <td width="1" align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>          
          <td width="98" align="right" class="datacell<%=style%>"></td>
          <td width="1" align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="88" align="right" class="datacell<%=style%>"></td>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

<c:set var="theFunds" value="${option.participantFundSummaryArray}"/>

<c:forEach items="${theFunds}" var="fund" >
<% if(style==1) style=2;else style=1; %>
        <tr>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td  width="30%" colspan="4" class="datacell<%=style%>">
<c:if test="${empty param.printFriendly }" >
<c:if test="${option.category.categoryCode !=PB}">

<a href="#fundsheet" onMouseOver='self.status="JavaScript needed to open the fund page link"; return true' NAME='${fund.fundId}' onClick='FundWindow("<ps:fundLink fundIdProperty="fund.fundId" fundTypeProperty="fund.fundType" rateType = "userProfile.currentContract.defaultClass" fundSeries ="<%=userProfile.getCurrentContract().getFundPackageSeriesCode()%>" productId ="<%=userProfile.getCurrentContract().getProductId()%>" siteLocation="<%= Environment.getInstance().getSiteLocation() %>" />")'>${fund.fundName}</a>
</c:if>
<c:if test="${option.category.categoryCode ==PB}">
${fund.fundName}
</c:if>
</c:if>

<c:if test="${not empty param.printFriendly }" >
${fund.fundName}
</c:if>
		  <img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" class="datacell<%=style%>">
<c:if test="${option.category.categoryCode !=PB}">
<c:if test="${fund.fundTotalNumberOfUnitsHeld ==0}">
					  &nbsp;
</c:if>
<c:if test="${fund.fundTotalNumberOfUnitsHeld !=0}">
			          <render:number property="fund.fundTotalNumberOfUnitsHeld" defaultValue = "0.00"  pattern="###,###,##0.000000" scale="6"/>
</c:if>
</c:if>
<c:if test="${option.category.categoryCode ==PB}">
			&nbsp;
</c:if>
          </td>
          <td align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" class="datacell<%=style%>">
<c:if test="${option.category.categoryCode !=PB}">
<c:if test="${fund.fundTotalNumberOfUnitsHeld ==0}">
<c:if test="${fund.fundTotalCompositeRate !=0}">
				  <render:number property="fund.fundTotalCompositeRate" pattern="###.00" defaultValue="0" scale="4" />%
</c:if>
<c:if test="${fund.fundTotalCompositeRate ==0}">
		          <render:number property="fund.fundUnitValue" defaultValue = "0.00"  pattern="###,###,##0.00" />
</c:if>
</c:if>
			
<c:if test="${fund.fundTotalNumberOfUnitsHeld !=0}">
			          <render:number property="fund.fundUnitValue" defaultValue = "0.00"  pattern="###,###,##0.00"/>
</c:if>
</c:if>
<c:if test="${option.category.categoryCode ==PB}">
			&nbsp;
</c:if>
          </td>
          <td align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" class="datacell<%=style%>"><render:number property="fund.fundTotalBalance" defaultValue = "0.00"  pattern="###,###,##0.00"/></td>
          <td align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>          
	      <td align="right" class="datacell<%=style%>"><render:number property="fund.fundTotalPercentageOfTotal" pattern="##0.00%" defaultValue="0.00%" scale="4" /></td>          
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

    <!-- Money Type Details for each fund -->      
<c:set var="theMoneyTypeDetails" value="${fund.fundMoneyTypeDetails}"/>
<c:forEach items="${theMoneyTypeDetails}" var="moneyTypeDetails" >
<% if(style==1) style=2;else style=1; %>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="4" class="datacell<%=style%>">&nbsp;&nbsp;<span class="highlight">${moneyTypeDetails.moneyTypeName}</span><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" class="datacell<%=style%>">
	  <span class="highlight">
<c:if test="${option.category.categoryCode !=PB}">
<c:if test="${moneyTypeDetails.numberOfUnitsHeld !=0}">
		    <render:number property="moneyTypeDetails.numberOfUnitsHeld" defaultValue = "0.00"  pattern="###,###,##0.000000" scale="6"/>
</c:if>
</c:if>
<c:if test="${option.category.categoryCode ==PB}">
			&nbsp;
</c:if>
	  </span>
 	  </td>
          <td align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" class="datacell<%=style%>">
	  <span class="highlight">
<c:if test="${option.category.categoryCode !=PB}">
<c:if test="${moneyTypeDetails.numberOfUnitsHeld ==0}">
<c:if test="${moneyTypeDetails.compositeRate !=0}">
				  &nbsp;
</c:if>
</c:if>
		
<c:if test="${moneyTypeDetails.numberOfUnitsHeld !=0}">
			          <render:number property="fund.fundUnitValue" defaultValue = "0.00"  pattern="###,###,##0.00" />
</c:if>
</c:if>
<c:if test="${option.category.categoryCode ==PB}">
			&nbsp;
</c:if>
	  </span>
	  </td>
          <td align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="datacell<%=style%>" align="right"></td>
          <td align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="1" class="datacell<%=style%>" align="right">&nbsp;</td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:forEach>
</c:forEach>
</c:if>
</c:forEach>
        <tr>
          <td class="databorder" colspan="13"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
      </table>
      <br>
    </td>
    <td><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	</tr>
    <tr>
	   		<td width="1%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
	   		<td >
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote">
 					<content:pageFootnotes beanName="layoutPageBean"/><br>
<c:if test="${participantAccountForm.showPba ==true}">
						<content:getAttribute beanName="footnotePBA" attribute="text"/><br>
</c:if>
				</p>
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
	   		<td  ></td> 			
	</tr>
</table>
<c:if test="${not empty param.printFriendly }" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="5%">&nbsp;</td>
			<td width="95%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
</c:if>
</c:if>
<c:if test="${not empty requestScope.errors}">
	<table width="715" border="0" cellspacing="0" cellpadding="0">
	<tr>	
		<td>
		&nbsp;
		</td>
	</tr>
	<tr>
		<td> 
		  <content:errors scope="session"/>
		</td>
	</tr>
	<tr>	
		<td>
		&nbsp;
		</td>
	</tr>
	</table>
</c:if>
