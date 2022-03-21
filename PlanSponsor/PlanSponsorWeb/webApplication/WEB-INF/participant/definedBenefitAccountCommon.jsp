<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%-- This jsp includes the following CMA content --%>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPANT_STATEMENT_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Participant_Statement_Link"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPANT_SUMMARY_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Participant_Summary_Link"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_SUMMARY_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Census_Summary_Link"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPANT_HISTORY_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Participant_History_Link"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPANT_WITHDRAWAL_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Participant_Withdrawal_Link"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Participant_Employee_Snapshot_Link"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_ROTH_INFO%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="rothInfo"/>

<content:contentBean contentId="<%=ContentConstants.PS_PARTICIPANT_ACCOUNT_LOAN_DETAIL_TEXT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Loan_Detail_Text"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_DB_PARTICIPANT_ACCOUNT_NO_PARTICIPANTS%>"
                                   type="<%=ContentConstants.TYPE_MESSAGE%>"
                                   id="NoParticipantsMessage"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_OUT_OF_SERVICE_HOURS%>"
                                   type="<%=ContentConstants.TYPE_MESSAGE%>"
                                   id="OutOfServiceOursApollo"/>


<content:contentBean contentId="<%=ContentConstants.FIXED_FOOTNOTE_PBA%>"
                           	type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>"
                          	id="footnotePBA"/>
<%
	boolean hasRoth = false;
	if (userProfile != null){	
		hasRoth = userProfile.getContractProfile().getContract().hasRothNoExpiryCheck();
	}
		
%>	


<script type="text/javascript" >
function selectedDate() {

	//window.alert("As of Date");
	document.participantAccountForm.submit();
}

/**
 * Opens up a new window and perform the same request again (with printFriendly
 * parameter.
 */
function doPrintIt()
{
  window.alert("Printer Friendly Version");
  var reportURL = new URL();
  reportURL.setParameter("task", "print");
  reportURL.setParameter("printFriendly", "true");
  window.open(reportURL.encodeURL(),"","width=720,height=480,menubar,resizable,toolbar,scrollbars,");
}

function goLoanRepaymentDetails(){
	if ( document.participantAccountForm.selectedLoan.value != -1 )  {
		document.loanRepaymentDetails.loanNumber.value = document.participantAccountForm.selectedLoan.value;
	//	window.alert("Changed Loan DD");
	//	window.alert(document.loanRepaymentDetails.loanNumber.value);
	//	window.alert(document.loanRepaymentDetails.maskedSsn.value);
	//	window.alert(document.loanRepaymentDetails.name.value );
	//	window.alert(document.loanRepaymentDetails.profileId.value);
		document.loanRepaymentDetails.submit();
	}
}

function selectMoneyReport(actionUrl) {

	//window.alert("As of Date");
	document.participantAccountForm.action = actionUrl;
	document.participantAccountForm.submit();
}

function doWithdrawalLink(date) {
	//document.withdrawalForm.selectedAsOfDate.value = date;
	document.withdrawalForm.submit()
}
</script>



<c:if test="${empty requestScope.errors}">




<!-- begin the standard header -->


<table width="100%" border="0" cellspacing="0" cellpadding="0">

  <tr>
    <td width="5%" valign="top">
      <c:if test="${empty param.printFriendly}" >
        <img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
      </c:if>
    </td>
    <td width="95%" valign="top">

<form name="statementform" method="POST" action="/do/participant/participantStatements">
<%--<form:hidden path="profileId" value="${details.profileId}"/> --%>
<input type="hidden" name="profileId" value="${details.profileId}"/>
</form>

<form name="withdrawalForm" method="POST" action="/do/withdrawal/beforeProceeding/init/">
<%--<form:hidden path="profileId"  value="${details.profileId}" /> --%>
<%--<form:hidden path="contractId" value="${userProfile.currentContract.contractNumber}" />
<form:hidden path="originator"  value="participantAccount" />
<form:hidden path="selectedAsOfDate"  value="${participantAccountForm.selectedAsOfDate}" />--%>
<input type="hidden" name="profileId" value="${details.profileId}"/>
<input type="hidden" name="contractId" value="${userProfile.currentContract.contractNumber}"/>
<input type="hidden" name="originator" value="participantAccount"/>
<input type="hidden" name="selectedAsOfDate" value="${participantAccountForm.selectedAsOfDate}"/>
</form>

<c:if test="${empty param.printFriendly}" >
      <table width="720" border="0" cellspacing="0" cellpadding="0">
      </c:if>    
<c:if test="${not empty param.printFriendly}" >
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>          
        <tr>
          <td width="497" ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
          <td width="14"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
          <td width="189" ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        </tr>
        <tr>
          <td valign="top">
		  <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
		  <img src="/assets/unmanaged/images/s.gif" width="5" height="1"><img src='<content:pageImage type="pageTitle" beanName="layoutPageBean"/>' alt="Defined Benefit account"><br>
<c:if test="${not empty param.printFriendly}" >
			<%if(hasRoth){ %>
				<br>						
				<br>
				<content:getAttribute  attribute="text" beanName="rothInfo"/>
			<%}%>		  
</c:if>




<c:if test="${empty param.printFriendly}" >
		    <table width="500" border="0" cellspacing="0" cellpadding="0">
              <tr>
				<td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				<td width="295" valign="top">
					<content:getAttribute attribute="introduction1" beanName="layoutPageBean"/>
					<br><br>					
					<content:getAttribute attribute="introduction2" beanName="layoutPageBean"/>
					
					<%if(hasRoth){ %>

						<br>					
						<br>
						<content:getAttribute  attribute="text" beanName="rothInfo"/>
					<%}%>					
					<br><br>

				</td>
				<td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
				<td width="180" valign="top">
<c:if test="${userProfile.currentContract.mta ==false}" >
					 <content:contentBean contentId="<%=ContentConstants.COMMON_HOWTO_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="How_To_Title"/>
					 <content:howToLinks id="howToLinks" layoutBeanName="layoutPageBean"/>	
	                 <%-- Start of How To --%>
					 <ps:howToBox howToLinks="howToLinks" howToTitle="How_To_Title"/>
</c:if>
				</td>
			  </tr>
              <!-- links to participant pages -->			  
			</table>
          </td>
          <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          <td valign="top" class="right">
			  <img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>
	 	      <jsp:include page="/WEB-INF/global/standardreportlistsection.jsp" flush="true" />
 		      <jsp:include page="/WEB-INF/global/standardreporttoolssection.jsp" flush="true" />
      	  </td>
</c:if>
        
</tr>
      </table>
      

<form name="loanRepaymentDetails" method="POST" action="/do/transaction/loanRepaymentDetailsReport/" >
<%--<form:hidden path="loanNumber"/>
<form:hidden path="name" value="${details.firstName} ${details.lastName}"/>--%>
<%-- <form:hidden path="profileId" value="${participantAccountForm.profileId}"/>--%>
   	 <input type=hidden name="loanNumber"/>
<input type=hidden name="name" value="${details.firstName} ${details.lastName}"/>
<input type=hidden name="profileId" value="${participantAccountForm.profileId}"/> 
</form>

<!-- end the standard header -->

<ps:form method="GET" action="/do/db/definedBenefitAccount/" name="participantAccountForm">
<%--<form:hidden path="profileId"/> --%>
<input type="hidden" name="profileId" value="${details.profileId}"/>
   <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>	

<c:if test="${empty param.printFriendly}" >
	<table width="720" border="0" cellspacing="0" cellpadding="0">
</c:if>
<c:if test="${not empty param.printFriendly}" >
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>
 		<tr> 
 			<td width="1"></td>
 			<td width="333"></td>
 			<td width="120"></td>
 			<td width="1"></td>
 			<td width="264"></td> 			
 			<td width="1"></td>
 	   </tr>        
 	   <tr>
 	       <td colspan="6">
      	       <c:if test="${not empty sessionScope.psErrors}">
<c:set var="errorsExist" value="${true}" scope="page" />
      	          <div id="errordivcs"><content:errors scope="session"/></div>
      	       </c:if>
      	   </td>
       </tr> 
 	   <tr>
          <td class="tableheadTD1" colspan="6">
                <b><%-- CMA managed--%><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> </b>as of
<c:if test="${layoutBean.id == '/participant/definedBenefitAccount.jsp'}">
                    	 <ps:select name="participantAccountForm" property="selectedAsOfDate" onchange="selectedDate();">
	  					  <ps:dateOption 
							  name="<%=Constants.USERPROFILE_KEY%>"  
							  property="currentContract.contractDates.asOfDate" 
							  renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
					
						  <ps:dateOptions 
							  name="<%=Constants.USERPROFILE_KEY%>" 
							  property="currentContract.contractDates.monthEndDates" 
							  renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
			            </ps:select> 
</c:if>
<c:if test="${layoutBean.id != '/participant/definedBenefitAccount.jsp'}">
                      <render:date patternOut="<%= RenderConstants.EXTRA_LONG_MDY %>"
                                         property="userProfile.currentContract.contractDates.asOfDate"/>
</c:if>
          </td>
        </tr>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="333" align="left" valign="top" class="datacell1">
	      <table width="333" border="0" cellpadding="2" cellspacing="0">
            <tr>
                <td width="180" valign="top"><b>Name</b></td>
                <td width="153" align="right" class="highlight">
<b>${details.firstName} ${details.lastName}</b>
                </td>
            </tr>
  	        <%-- Requirement PPR.85 --%>
              <tr>
                <td><b>Total assets</b></td>
                <td align="right" class="highlight"><render:number property="details.totalAssets" type="c"/></td>
              </tr>
              <tr>
                <td><b>Allocated assets</b></td>
                <td align="right" class="highlight"><render:number property="details.allocatedAssets" type="c"/></td>
              </tr>
              <%-- Requirement PPR.95 
              <c:if test="${participantAccountForm.showPba==true}">
              <tr>
                <td><b>Personal brokerage account<sup><b>&#134;</b></sup></b></td>
                <td align="right" class="highlight"><render:number property="details.personalBrokerageAccount" type="c"/></td>
              </tr>
              </c:if>
              n/a for db --%>
              <%-- Requirement PPR.91 --%>
<c:if test="${details.showLoanFeature =='YES'}">
              <tr>
                <td><b>Loan assets</b></td>
                <td align="right" class="highlight"><render:number property="details.loanAssets" type="c"/></td>
              </tr>
</c:if>
              <tr>
                <td colspan="2">
                <%-- Requirement PPR.91 --%>
<c:if test="${participantAccountForm.showLoans ==true}">
                <%-- CMA managed--%>
                <content:getAttribute beanName="Loan_Detail_Text" attribute="text"/>
                <c:if test="${empty param.printFriendly}" >
					
					<c:set var="loanListSize" value="participantAccountForm.loanList"/>
					<br/>
				  <c:if test="${loanListSize gt 2}">
			        <ps:select name="participantAccountForm" property="selectedLoan" onchange="goLoanRepaymentDetails();" value="-1">
<form:options items="${participantAccountForm.loanList}"/>
			        </ps:select>
				  </c:if>
				  <c:if test="${loanListSize==2}">
<input type="hidden" name="selectedLoan" value="${participantAccountForm.selectedLoan}" />
					   <a href="#" onclick="goLoanRepaymentDetails();">View loan details</a>
</c:if>
                </c:if>
</c:if>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
              <tr>
                <td><b>Last contribution date</b></td>
                <td align="right" class="highlight"><render:date property="details.lastContributionDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /></td>
              </tr>
<c:if test="${details.rothFirstDepositYear !=9999}">
					<tr>
                		<td><b>Year of first Roth contribution</b></td>
<td align="right" class="highlight">${details.rothFirstDepositYear}</td>
					</tr>
</c:if>
</c:if>
            </table>
          </td>
          <td width="120" class="datacell1">&nbsp;</td>
          <td class="datadivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td width="264" valign="top" class="datacell1">
          	<table width="264" border="0" cellpadding="0" cellspacing="0">
          	<tr>
          		<td colspan="3"><b>Asset allocation by investment category</b></td>
          	</tr>
          	<tr>
          		<td colspan="3"><ps:pieChart beanName="pieChartBean" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Allocated Assets"/></td>
          	</tr>
<c:if test="${participantAccountForm.showLifecycle ==true}">
  	        <tr valign="top">
		      <td>
			    <table border="0" cellpadding="0" cellspacing="0">
			       <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_LIFECYCLE %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
			    </table>
		      </td>
		      <td>Target Date</td>
		      <td align="right"><render:number property="assets.totalAssetsByRisk(LC)" type="c"/></td>
              <td width="20%" align="right" valign="top">
              	  <render:number property="assets.percentageTotalByRisk(LC)" pattern="##0%" defaultValue="0%"/>
              </td>
            </tr>
</c:if>
          	
          	
            <tr valign="top">
	          <td>
	           	 <table border="0" cellpadding="0" cellspacing="0">
	           	   <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_AGRESSIVE %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	           	 </table>
	          </td>
	          <td>Aggressive growth</td>
	          <td align="right"><render:number property="assets.totalAssetsByRisk(AG)" type="c"/></td>
              <td align="right" valign="top">
						<render:number property="assets.percentageTotalByRisk(AG)" pattern="##0%" defaultValue="0%"/>
              </td>
            </tr>
            <tr valign="top">
	          <td>
	            <table border="0" cellpadding="0" cellspacing="0">
	              <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_GROWTH %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	           	</table>
	          </td>
	          <td>Growth</td>
	          <td align="right"><render:number property="assets.totalAssetsByRisk(GR)" type="c" /></td>
              <td align="right" valign="top">
				  <render:number property="assets.percentageTotalByRisk(GR)" pattern="##0%" defaultValue="0%"/>
              </td>
            </tr>
            <tr valign="top">
	           <td>
	             <table border="0" cellpadding="0" cellspacing="0">
	               <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_GROWTH_INCOME %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	             </table>
	           </td>
	           <td>Growth & income</td>
	           <td align="right"><render:number property="assets.totalAssetsByRisk(GI)" type="c" /></td>
               <td align="right" valign="top">
				   <render:number property="assets.percentageTotalByRisk(GI)" pattern="##0%" defaultValue="0%"/>						
               </td>
            </tr>
            <tr valign="top">
	           <td>
	             <table border="0" cellpadding="0" cellspacing="0">
	           	   <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_INCOME %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	           	 </table>
	           </td>
	           <td>Income</td>
	           <td align="right"><render:number property="assets.totalAssetsByRisk(IN)" type="c" /></td>
               <td align="right" valign="top">
					<render:number property="assets.percentageTotalByRisk(IN)" pattern="##0%" defaultValue="0%"/>						
               </td>
           	</tr>
           	<tr valign="top">
	           <td>
	             <table border="0" cellpadding="0" cellspacing="0">
	           	   <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_CONSERVATIVE %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	           	 </table>
	           </td>
	           <td>Conservative</td>
	           <td align="right"><render:number property="assets.totalAssetsByRisk(CN)" type="c" /></td>
               <td align="right" valign="top">
				   <render:number property="assets.percentageTotalByRisk(CN)" pattern="##0%" defaultValue="0%"/>						
               </td>
           	</tr>
           	<%-- Requirement PPR.95 --%>
<c:if test="${participantAccountForm.showPba ==true}">
           	<tr valign="top">
	           <td>
	             <table border="0" cellpadding="0" cellspacing="0">
	           	   <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_PBA %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	           	 </table>
	            </td>
	            <td>Personal brokerage account</td>
	            <td align="right"><render:number property="assets.totalAssetsByRisk(PB)" type="c" /></td>
                <td align="right" valign="top">
					<render:number property="assets.percentageTotalByRisk(PB)" pattern="##0%" defaultValue="0%"/>						
              	</td>
           	</tr>
</c:if>
          	</table>
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
     </ps:form>
</c:if>
     <div id="tabs"></div>
     <tr>
          <td height="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="10"class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
     </tr>             
</table>
<c:if test="${empty param.printFriendly}" >
	<table width="720" border="0" cellspacing="0" cellpadding="0">
</c:if>
<c:if test="${not empty param.printFriendly}" >
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>

