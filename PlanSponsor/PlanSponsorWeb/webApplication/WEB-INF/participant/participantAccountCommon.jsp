
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants"/>
<un:useConstants var="webConstants" className="com.manulife.pension.ps.web.Constants"/>
<un:useConstants var="withdrawalWebConstants" className="com.manulife.pension.ps.web.withdrawal.WebConstants"/>

<%-- This jsp includes the following CMA content --%>

<%
ParticipantAccountDetailsVO details = (ParticipantAccountDetailsVO)request.getAttribute("details");
pageContext.setAttribute("details",details,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPANT_STATEMENT_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Participant_Statement_Link"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPANT_SUMMARY_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Participant_Summary_Link"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_SUMMARY_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Census_Summary_Link"/>
<%-- Gateway Phase 1 Start benefit base information link --%>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPANT_BENEFITBASE_INFORMATION_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Benefitbase_Information_Link"/>
<%-- Gateway Phase 1 End --%>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPANT_HISTORY_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Participant_History_Link"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Participant_Employee_Snapshot_Link"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_ROTH_INFO%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="rothInfo"/>

<content:contentBean contentId="<%=ContentConstants.PS_PARTICIPANT_ACCOUNT_LOAN_DETAIL_TEXT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Loan_Detail_Text"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_PARTICIPANT_ACCOUNT_NO_PARTICIPANTS%>"
                                   type="<%=ContentConstants.TYPE_MESSAGE%>"
                                   id="NoParticipantsMessage"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_TECHNICAL_DIFFICULTIES%>"
                                   type="<%=ContentConstants.TYPE_MESSAGE%>"
                                   id="TechnicalDifficulties"/>


<content:contentBean contentId="<%=ContentConstants.FIXED_FOOTNOTE_PBA%>"
                           	type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>"
                          	id="footnotePBA"/>
<content:contentBean contentId="${contentConstants.MISCELLANEOUS_PARTICIPANT_WITHDRAWAL_LINK}" type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="participantWithdrawalLink"/>
<content:contentBean contentId="${contentConstants.CREATE_LOAN_REQUEST_LINK}" type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="loanLink"/>
                            
<%
	boolean hasRoth = false;
	if (userProfile != null){	
		hasRoth = userProfile.getContractProfile().getContract().hasRothNoExpiryCheck();
	}
		
%>	

<script type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript" >
function selectedDate() {

	//window.alert("As of Date");
	document.participantAccountForm.submit();
}

function tooltip(DefInvesValue)
{
	if(DefInvesValue == "TR")
	Tip('Instructions were provided by Trustee - Mapped');
	else if(DefInvesValue == "PR")
	Tip('Instructions prorated - participant instructions incomplete / incorrect');
	else if(DefInvesValue == "PA")
	Tip('Participant Provided');
	else if(DefInvesValue == "DF")
	Tip('Default investment option was used');
	else if(DefInvesValue == "MA")
	Tip('Managed Accounts');
	else
	UnTip();
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

<form name="statementform" method="GET" action="/do/participant/participantStatementResults">
<input type="hidden" name="task" value="fetchStatements"/>
<input type="hidden" name="profileId" value="${details.profileId}"/>
<input type="hidden" name="lastName" value="${details.lastName}" />
<input type="hidden" name="firstName" value="${details.firstName}" />
</form>

<c:if test="${empty param.printFriendly}">
      <table width="700" border="0" cellspacing="0" cellpadding="0">
      </c:if>    
<c:if test="${not empty param.printFriendly}">
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
		  <img src="/assets/unmanaged/images/s.gif" width="5" height="1"><img src='<content:pageImage type="pageTitle" beanName="layoutPageBean"/>' alt="Participant Account"><br>
<c:if test="${not empty param.printFriendly}">
			<%if(hasRoth){ %>
				<br>						
				<br>
				<content:getAttribute  attribute="text" beanName="rothInfo"/>
			<%}%>		  
</c:if>




<c:if test="${empty param.printFriendly}">
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
<c:if test="${userProfile.currentContract.mta ==false}">
					 <content:contentBean contentId="<%=ContentConstants.COMMON_HOWTO_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="How_To_Title"/>
					 <content:howToLinks id="howToLinks" layoutBeanName="layoutPageBean"/>	
	                 <%-- Start of How To --%>
					 <ps:howToBox howToLinks="howToLinks" howToTitle="How_To_Title"/>
</c:if>
				</td>
			  </tr>
<!-- links to participant pages -->			  
			  <tr>
				<td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
			  	<td vAlign=top><strong>Participant level </strong><br>
			  		<content:getAttribute attribute="text" beanName="Participant_Statement_Link">
						<content:param>javascript:document.statementform.submit();</content:param>
			      	</content:getAttribute>
			  	<br>
			  		<content:getAttribute attribute="text" beanName="Participant_History_Link">
<content:param>/do/transaction/participantTransactionHistory/?profileId=${participantAccountForm.profileId}
						</content:param>
				    </content:getAttribute>
			
					<!-- <a href="/do/census/viewEmployeeSnapshot/?profileId="${participantAccountForm.profileId}"&source=<%=CensusConstants.PARTICIPANT_ACCOUNT_PAGE%>">View this participant's census snapshot</a>      -->
<c:if test="${details.participantInd ==true}">
					<br>
					<content:getAttribute attribute="text" beanName="Participant_Employee_Snapshot_Link">
<content:param>/do/census/viewEmployeeSnapshot/?profileId=${participantAccountForm.profileId}&source=<%=CensusConstants.PARTICIPANT_ACCOUNT_PAGE%>
						</content:param>
				    </content:getAttribute>
</c:if>
			
				  <c:if test="${participantAccountForm.showCreateWithdrawalRequestLink}">
				  <br>
				      <%-- Link into the withdrawals module for initiating a new withdrawal --%>
				      <content:getAttribute attribute="text" beanName="participantWithdrawalLink">
					<content:param>/do/withdrawal/beforeProceedingGatewayInit/?${withdrawalWebConstants.PROFILE_ID_PARAMETER}=${details.profileId}&${withdrawalWebConstants.CONTRACT_ID_PARAMETER}=${userProfile.currentContract.contractNumber}&${withdrawalWebConstants.ORIGINATOR_PARAMETER}=${withdrawalWebConstants.PARTICIPANT_ACCOUNT_ORIGINATOR}</content:param>
				      </content:getAttribute>
				  </c:if>
				  
				  <c:if test="${participantAccountForm.showLoanCreateLink}">
				  <br>
				  	 <%-- Link for initiating a new loan --%>
				  	<content:getAttribute attribute="text" beanName="loanLink">
					<content:param>/do/onlineloans/initiate/?participantProfileId=${details.profileId}&${withdrawalWebConstants.ORIGINATOR_PARAMETER}=${withdrawalWebConstants.PARTICIPANT_ACCOUNT_ORIGINATOR}&${withdrawalWebConstants.CONTRACT_ID_LOAN_PARAMETER}=${userProfile.currentContract.contractNumber }</content:param>
				     </content:getAttribute>				     
				  </c:if> 

				<%-- CR 21 Benefit Base link --%>
					<%-- link to Benefit base information start --%>
					<c:if test="${userProfile.currentContract.hasContractGatewayInd}">
<c:if test="${not empty details.participantGIFLIndicator}">
						<br>
							<content:getAttribute attribute="text" beanName="Benefitbase_Information_Link">
<content:param>/do/participant/participantBenefitBaseInformation/?profileId=${participantAccountForm.profileId}
							</content:param>
							</content:getAttribute>
</c:if>
					</c:if>
				<%-- Gateway Phase 1 End --%>
			  	</td>
			  	<td>&nbsp;</td>
			  	<td vAlign=top><strong>Contract level </strong><br>
			  		<content:getAttribute attribute="text" beanName="Participant_Summary_Link">
						<content:param>/do/participant/participantSummary</content:param>
				    </content:getAttribute>
				<br>
					<content:getAttribute attribute="text" beanName="Census_Summary_Link">
						<content:param>/do/census/censusSummary/</content:param>
				    </content:getAttribute>
					
			  	</td>
			  </tr>
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

<ps:form name="loanRepaymentDetails" method="POST" action="/do/transaction/loanRepaymentDetailsReport/" modelAttribute="loanRepaymentDetailsReportForm" >
  <form:hidden path="loanNumber" value="${participantAccountForm.selectedLoan}"/>
  <form:hidden path="name" value="${details.lastName},${details.firstName}"/>
<form:hidden path="profileId" value="${participantAccountForm.profileId}"/>
</ps:form>


<ps:form method="GET" action="/do/participant/participantAccount/" modelAttribute="participantAccountForm" name="participantAccountForm">
<form:hidden path="profileId"/>
   <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>	

<c:if test="${empty param.printFriendly}" >
	<table width="700" border="0" cellspacing="0" cellpadding="0">
</c:if>
<c:if test="${not empty param.printFriendly}" >
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>
 		<tr> 
 			<td width="1"></td>
 			<td width="209" colspan="4"></td>
 			<td width="1"></td> 			

 			<td width="80"></td>
 			<td width="1"></td>
 			<td width="81"></td> 			
 			<td width="1"></td>
 			<td width="81"></td>

 			<td width="1"></td>

 			<td width="80"></td> 			
 			<td width="1"></td>
 			<td width="81"></td>
 			<td width="1"></td> 			
 			<td width="81"></td>

 			<td width="1"></td>
 	   </tr>        
 	   <tr>
 	   
 	       <td colspan="14">
      	       <c:if test="${not empty sessionScope.psErrors}">
<c:set var="errorsExist" value="${true}" scope="page" />
      	       <div id="errordivcs"><content:errors scope="session"/></div>
      	       </c:if>
      	   </td>
       </tr> 
 	   <tr>
          <td class="tableheadTD1" colspan="18">
                <b><!-- CMA managed --><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> </b>as of
<c:if test="${layoutBean.id == '/participant/participantAccount.jsp'}">
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
<c:if test="${layoutBean.id != '/participant/participantAccount.jsp'}">
                      <render:date patternOut="<%= RenderConstants.EXTRA_LONG_MDY %>"
                                         property="userProfile.currentContract.contractDates.asOfDate"/>
                                         
</c:if>
          </td>
        </tr>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="209" colspan="4" align="left" valign="top" class="datacell1">
	      <table width="209" border="0" cellpadding="2" cellspacing="0">
            <tr>
                <td width="105" valign="top"><b>Name</b></td>
<td class="highlight" width="104"><b>${details.lastName}, ${details.firstName}</b></td>
            </tr>
            <tr>
              <td valign="top"><b>SSN </b></td>
              <td><render:ssn property="details.ssn" /></td>
              </tr>
            <tr>
              <td  valign="top"><b>Date of birth</b></td>
              <td class="highlight">
  	            	<render:date property="details.birthDate" defaultValue="January 1, 1980" patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" />
              </td>
            </tr>
            <!-- Requirement PPR.110 -->
<c:if test="${participantAccountForm.showAge ==true}">
            <tr>
              <td valign="top"><b>Age</b></td>
<td class="highlight">${details.age}</td>
            </tr>
</c:if>
	        <tr>
              <td valign="top"><b>Address</b></td>
              <td class="highlight">&nbsp;</td>
            </tr>
           <!--  Requirement PPR.99 -->
<c:if test="${not empty details.addressLine1}">
	        <tr>
<td colspan="2">${details.addressLine1}</td>
            </tr>
            <tr>
<td colspan="2">${details.addressLine2}</td>
            </tr>
            <tr>
<td colspan="2">${details.cityName}, ${details.stateCode}, ${details.zipCode}</td>
            </tr>
</c:if>
<c:if test="${empty details.addressLine1}">
			<tr>
              <td colspan="2">No available address information.</td>
            </tr>
</c:if>
  	       <!--  Requirement PPR.85 -->
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
     	   <!--  CL 110234 Begin -->
            <tr>
              <td valign="top"><b>Employment status</b></td>
<td class="highlight">${details.employmentStatus}
&nbsp; ${details.effectiveDate}
			  </td>
            </tr>
           <!--  CL 110234 End -->

            <tr>
              <td valign="top"><b>Contribution status</b></td>			<!-- CL 110234 -->
<td class="highlight">${details.employeeStatus}</td>            </tr>          
    
</c:if>
			<!-- GIFL 1C  -->
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
				<c:choose>
					<c:when test="${userProfile.currentContract.hasContractGatewayInd}">
	           			<tr>
	          				<td valign="top"><b>${webConstants.COL_GIFL_FEATURE_TITLE_SC}</b></td>
<td class="highlight">${details.participantGIFLIndicatorAsSelect}</td>
           				</tr>
					</c:when>
					<c:otherwise>
						<c:if test = "${details.participantGIFLIndicator != null}">
							<tr>
	          					<td valign="top"><b>${webConstants.COL_GIFL_FEATURE_TITLE_SC}</b></td>
<td class="highlight">${details.participantGIFLIndicatorAsSelect}</td>
           					</tr>
						</c:if>
					</c:otherwise>
				</c:choose>
</c:if>
         <c:if test="${participantAccountForm.asOfDateCurrent and participantAccountForm.showManagedAccount }">
					<tr>
						<td valign="top"><b>Managed Accounts<sup>&#167;</sup></b></td>
						<td class="highlight">${details.managedAccountStatusValue}</td>
					</tr>
		  </c:if>
          </table>
	      </td>
          <td class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="244" colspan="5" rowspan="1" align="left" valign="top" class="datacell1">
            <table border="0" cellpadding="2" cellspacing="0">
              <tr>
                <td width="121" ><b>Total assets</b></td>
                <td width="123" align="right" class="highlight"><render:number property="details.totalAssets" type="c"/></td>
              </tr>
              <tr>
                <td><b>Allocated assets</b></td>
                <td align="right" class="highlight"><render:number property="details.allocatedAssets" type="c"/></td>
              </tr>
             <!--  Requirement PPR.95 -->
<c:if test="${participantAccountForm.showPba ==true}">
              <tr>
                <td><b>Personal brokerage account<sup><b>&#134;</b></sup></b></td>
                <td align="right" class="highlight"><render:number property="details.personalBrokerageAccount" type="c"/></td>
              </tr>
</c:if>
             <!--  Requirement PPR.91 -->
<c:if test="${details.showLoanFeature =='YES'}">
              <tr>
                <td><b>Loan assets</b></td>
                <td align="right" class="highlight"><render:number property="details.loanAssets" type="c"/></td>
              </tr>
</c:if>
              <tr>
                <td colspan="2">
               <!--  Requirement PPR.91 -->
<c:if test="${participantAccountForm.showLoans ==true}">
               <!--  CMA managed -->
                <content:getAttribute beanName="Loan_Detail_Text" attribute="text"/>
                <c:if test="${empty param.printFriendly}" >
					
					<c:set var ="loanListSize" value="${participantAccountForm.loanList.size()}"/>
					<br/>
					 <c:if test="${loanListSize gt '2'}">
				  
				  <form:select name="participantAccountForm" path="selectedLoan" onchange="goLoanRepaymentDetails();" >

 <form:options items="${participantAccountForm.loanList}" itemLabel="label" itemValue="value"/> 

						
						
</form:select>
			     
				  </c:if>
				  <c:if test="${loanListSize eq '2'}">
				  <form:hidden path="selectedLoan" value="${participantAccountForm.selectedLoan}"/>
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
                <td><b>Default date of birth</b></td>
                <td align="right" class="highlight">
<c:if test="${empty details.birthDate}">Yes</c:if>
<c:if test="${not empty details.birthDate}">No</c:if>
                </td>
              </tr>
              <c:if test="${not empty param.printFriendly}" >
					<tr>
	                <td><b>Investment instruction type</b></td>
<td align="right" class="highlight">${details.investmentInstructionType}</td>
	                </tr>
			  </c:if>
			  <c:if test="${empty param.printFriendly}" >             
              	<tr>
                <td><b>Investment instruction type</b></td>
<td align="right" class="highlight"><div onmouseover="tooltip('${details.investmentInstructionType}')" onmouseout="UnTip()">${details.investmentInstructionType}</div></td>
              	</tr>
              </c:if>
              <tr>
                <td><b>Last contribution date</b></td>
                <td align="right" class="highlight"><render:date property="details.lastContributionDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /></td>
              </tr>
              <tr>
                <td><b>Automated rebalance?</b></td>
<td align="right" class="highlight">${details.automaticRebalanceIndicator}</td>
              </tr>
<c:if test="${details.rothFirstDepositYear !='9999'}">
					<tr>
                		<td><b>Year of first Roth contribution</b></td>
<td align="right" class="highlight">${details.rothFirstDepositYear}</td>
					</tr>
</c:if>
</c:if>
            </table>
          </td>
          <td class="datadivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td width="244" colspan="5" rowspan="1" valign="top" class="datacell1">
          	<table width="244" border="0" cellpadding="0" cellspacing="0">
          	<tr>
          		<td colspan="5"><b>Asset allocation by investment category</b></td>
          	</tr>
          	<tr>
          		<td colspan="5"><ps:pieChart beanName="pieChartBean" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Allocated Assets"/></td>
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
		  <td>&nbsp;</td>
		  <td align="right"><render:number property="assets.percentageTotalByRisk(LC)" pattern="##0%" defaultValue="0%"/></td>
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
	          <td>&nbsp;</td>
	          <td align="right"><render:number property="assets.percentageTotalByRisk(AG)" pattern="##0%" defaultValue="0%"/></td>
            </tr>
            <tr valign="top">
	          <td>
	            <table border="0" cellpadding="0" cellspacing="0">
	              <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_GROWTH %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	           	</table>
	          </td>
	          <td>Growth</td>
	          <td align="right"><render:number property="assets.totalAssetsByRisk(GR)" type="c" /></td>
	          <td>&nbsp;</td>
	          <td align="right"><render:number property="assets.percentageTotalByRisk(GR)" pattern="##0%" defaultValue="0%"/></td>
            </tr>
            <tr valign="top">
	           <td>
	             <table border="0" cellpadding="0" cellspacing="0">
	               <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_GROWTH_INCOME %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	             </table>
	           </td>
	           <td>Growth & income</td>
	           <td align="right"><render:number property="assets.totalAssetsByRisk(GI)" type="c" /></td>
	           <td>&nbsp;</td>
	           <td align="right"><render:number property="assets.percentageTotalByRisk(GI)" pattern="##0%" defaultValue="0%"/></td>
            </tr>
            <tr valign="top">
	           <td>
	             <table border="0" cellpadding="0" cellspacing="0">
	           	   <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_INCOME %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	           	 </table>
	           </td>
	           <td>Income</td>
	           <td align="right"><render:number property="assets.totalAssetsByRisk(IN)" type="c" /></td>
	           <td>&nbsp;</td>
	           <td align="right"><render:number property="assets.percentageTotalByRisk(IN)" pattern="##0%" defaultValue="0%"/></td>
           	</tr>
           	<tr valign="top">
	           <td>
	             <table border="0" cellpadding="0" cellspacing="0">
	           	   <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_CONSERVATIVE %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	           	 </table>
	           </td>
	           <td>Conservative</td>
	           <td align="right"><render:number property="assets.totalAssetsByRisk(CN)" type="c" /></td>
	           <td>&nbsp;</td>
	           <td align="right"><render:number property="assets.percentageTotalByRisk(CN)" pattern="##0%" defaultValue="0%"/></td>
           	</tr>
           	<!-- Requirement PPR.95 -->
<c:if test="${participantAccountForm.showPba ==true}">
           	<tr valign="top">
	           <td>
	             <table border="0" cellpadding="0" cellspacing="0">
	           	   <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_PBA %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
	           	 </table>
	            </td>
	            <td>Personal brokerage <br/> account</td>
	            <td align="right"><render:number property="assets.totalAssetsByRisk(PB)" type="c" /></td>
	            <td>&nbsp;</td>
	            <td align="right"><render:number property="assets.percentageTotalByRisk(PB)" pattern="##0%" defaultValue="0%"/></td>
           	</tr>
</c:if>
          	</table>
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
     </ps:form>
</c:if>
     <div id="tabs"></div>             
