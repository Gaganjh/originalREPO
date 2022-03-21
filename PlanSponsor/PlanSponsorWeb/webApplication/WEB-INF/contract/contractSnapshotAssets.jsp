<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractSnapshotVO" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%-- Beans used --%>
<%-- <c:set var="contractSnapshot" value="${ContractSnapshotVO}"/> --%>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	ContractSnapshotVO contractSnapshot = (ContractSnapshotVO)request.getAttribute(Constants.CONTRACT_SNAPSHOT);
	pageContext.setAttribute("contractSnapshot",contractSnapshot,PageContext.PAGE_SCOPE); 
%>
<script language="javascript">
function selectedDate()
{
	if (document.contractSnapshotForm.stringDate.selectedIndex == 0)
		document.contractSnapshotForm.isRecentDate.value = "true";
	else
		document.contractSnapshotForm.isRecentDate.value = "false";

	document.contractSnapshotForm.submit();
}
</script>

 <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
      <table width="715" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="356"><img src="/assets/unmanaged/images/s.gif" width="247" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="352"><img src="/assets/unmanaged/images/s.gif" width="123" height="1"></td>
          <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="tablehead">
        <td class="tableheadTD1" colspan="6">
       <form name="contractSnapshotForm" method="GET" action="/do/contract/contractSnapshot/" >
<input type="hidden" name="isRecentDate" />

             	<b>
             	<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>
             	</b> as of
             	
                      <%--     <form:select name="contractSnapshotForm" path="stringDate" onchange="selectedDate()">
             	<form:options items="<%=Constants.CONTRACT_SNAPSHOT_DATES%>" itemValue="id" itemLabel="description"/>
             </form:select> --%>

  		
             <ps:select name="contractSnapshotForm" property="stringDate"
                       onchange="setFilterFromSelect(this);selectedDate();">
              <ps:dateOption name="<%=Constants.USERPROFILE_KEY%>"
                             property="currentContract.contractDates.asOfDate"
                             renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
              <ps:dateOptions name="<%=Constants.USERPROFILE_KEY%>"
                              property="currentContract.contractDates.monthEndDates"
                              renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
            </ps:select>

        </form>
        </td>
        </tr>
        <tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="4" valign="top"><b>Contract assets<sup>1</sup></b></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="50%"><b>Total contract assets<sup>2</sup></b></td>
                <td rowspan="8"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
                <td width="50%" align="center">
                  <div align="right"><b>
                  <render:number property="contractSnapshot.planAssets.totalPlanAssetsAmount" scope="<%=pageContext.REQUEST_SCOPE%>" type="c"/>
                  </b></div>
                </td>
              </tr>
              <tr>
                <td>Allocated assets</td>
                <td align="center">
                  <div align="right">
                   <render:number property="contractSnapshot.planAssets.allocatedAssetsAmount" scope="<%=pageContext.REQUEST_SCOPE%>" type="c"/>
                  </div>
                </td>
              </tr>

<c:if test="${contractSnapshot.planAssets.cashAccountAmount ne '0.00'}">
              <tr>
                <td>Cash account</td>
                <td align="center">
                  <div align="right">
                   <render:number property="contractSnapshot.planAssets.cashAccountAmount" scope="<%=pageContext.REQUEST_SCOPE%>" type="c"/>
                 </div>
                </td>
              </tr>
</c:if>

<c:if test="${contractSnapshot.planAssets.uninvestedAssetsAmount ne '0.00'}">
              <tr>
                <td>Pending transaction</td>
                <td align="center">
                  <div align="right">
                  <render:number property="contractSnapshot.planAssets.uninvestedAssetsAmount" scope="<%=pageContext.REQUEST_SCOPE%>" type="c"/>
                  </div>
                </td>
              </tr>
</c:if>

<c:if test="${contractSnapshotForm.displayPba == true}">
              <tr>
                <td>Personal brokerage account<sup><b>&#134;</b></sup></td>
                <td align="center">
                  <div align="right">
                   <render:number property="contractSnapshot.planAssets.personalBrokerageAccountAmount" scope="<%=pageContext.REQUEST_SCOPE%>" type="c"/>
                  </div>
                </td>
              </tr>
</c:if>

<c:if test="${contractSnapshotForm.displayLoan == true}">
              <tr>
                <td>Loan assets</td>
                <td align="center">
                  <div align="right">
                   <render:number property="contractSnapshot.planAssets.loanAssets" scope="<%=pageContext.REQUEST_SCOPE%>" type="c" defaultValue="$0.00"/>
                  </div>
                </td>
              </tr>
</c:if>
            </table>
			            
  <!-- PERA Balance Details -->
              <c:if test="${not empty contractSnapshot.contractPeraDetailsVO}">
              <br/>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
	              <c:if test="${contractSnapshot.contractPeraDetailsVO.contractIsPera == true}">
	              	 <c:if test="${contractSnapshotForm.isRecentDate == true}">
		              <tr>
		                <td width="50%"><b>Total PERA Balance</b></td>
		                <td rowspan="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		                <td width="50%" align="center">
		                  <div align="right"><b>
		                  <render:number property="contractSnapshot.contractPeraDetailsVO.availablePeraBalance" scope="<%=pageContext.REQUEST_SCOPE%>" type="c"/>
		                  </b></div>
		                </td>
		              </tr>
		              <tr>
		                <td>Prior year balance</td>
		                <td align="center">
		                  <div align="right">
		                  <render:number property="contractSnapshot.contractPeraDetailsVO.priorBalance" scope="<%=pageContext.REQUEST_SCOPE%>" type="c" defaultValue="$0.00"/>
		                  </div>
		                </td>
		              </tr>
		              <tr>
		                <td>Current year balance</td>
		                <td align="center">
		                  <div align="right">
		                  <render:number property="contractSnapshot.contractPeraDetailsVO.currentBalance" scope="<%=pageContext.REQUEST_SCOPE%>" type="c" defaultValue="$0.00"/>
		                  </div>
		                </td>
		              </tr>
	              </c:if>
	              <c:if test="${contractSnapshotForm.isRecentDate == false}">
		              <tr>
		                <td width="50%"><b>Total PERA Balance</b></td>
		                <td rowspan="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		                <td width="50%" align="center">
		                  <div align="right">
		                  Not available for selected date
		                  </div>
		                </td>
		              </tr>
		              <tr>
		                <td>Prior year balance</td>
		                <td align="center">
		                  <div align="right">
		                  </div>
		                </td>
		              </tr>
		              <tr>
		                <td>Current year balance</td>
		                <td align="center">
		                  <div align="right">
		                  </div>
		                </td>
		              </tr>
	              </c:if>
	             </c:if>
	             </table>
              </c:if>
            <c:if test="${not empty contractSnapshot.contractPeraDetailsVO}">
             <c:if test="${contractSnapshot.contractPeraDetailsVO.contractIsPera == true}">
             <c:if test="${contractSnapshotForm.isRecentDate == true}">
					<br>
		 			Asset figures current as of close of the previous business day. <br/>PERA balance is current date.
		 			</c:if>
		 			<c:if test="${contractSnapshotForm.isRecentDate == false}">
					<br>
		 			Asset figures current as of close of the previous business day.
		 			</c:if>
		 			</c:if>
	 			<c:if test="${contractSnapshot.contractPeraDetailsVO.contractIsPera == false}">
		            <br>
		 			Asset figures current as of close of the previous business day.
 			</c:if>
 			</c:if>
 			<c:if test="${empty contractSnapshot.contractPeraDetailsVO}">
 			<br>
		 	Asset figures current as of close of the previous business day.
		 	</c:if>
            </td>           
             
             <%-- Blended Assets part Start --%> 
            <td colspan="2" valign="top" align="left">
            
<c:if test="${contractSnapshotForm.displayBlendedAssets == true}">
            <table width="95%" border="0" cellspacing="0" cellpadding="0" align="right">
              <tr>
                <td >Blended Asset Charge <td>
                  <td >
                   <render:number property="contractSnapshot.planAssets.blendedAssetCharge" scope="<%=pageContext.REQUEST_SCOPE%>"  type="d" defaultValue="0.000" pattern="##0.000"/>%
                  </td>
                <td>
                As of 
               <render:date property="contractSnapshot.planAssets.assetChargeAsOfDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" />
               </td>
              </tr>
            
            </table>
</c:if>  
            
             <%-- Blended Assets part End --%> 
			
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
