<%@ taglib prefix="report" uri="manulife/tags/report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="com.manulife.pension.service.security.role.BundledGaCAR"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.ps.web.controller.SecurityManager" %>
<%@ page import="com.manulife.pension.service.contract.util.ServiceFeatureConstants" %>
<%@ page import="java.util.TreeMap" %>

<%@page import="java.math.BigDecimal"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"%>
<%@page import="com.manulife.pension.service.security.role.TeamLead" %>
<un:useConstants var="constants" className="com.manulife.pension.ps.web.Constants" />

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
String requestType = (String)request.getAttribute(Constants.REQUEST_TYPE);
pageContext.setAttribute("requestType",requestType,PageContext.PAGE_SCOPE);
TreeMap dates =(TreeMap)request.getAttribute(Constants.NEXT_PLAN_ENTRY_DATES);
pageContext.setAttribute("dates",dates,PageContext.PAGE_SCOPE);
Boolean vestingCalculated= (Boolean)request.getAttribute(Constants.VESTING_CALCULATED);
pageContext.setAttribute("vestingCalculated",vestingCalculated,PageContext.PAGE_SCOPE);
Boolean submissionHistoryAccess= (Boolean)request.getAttribute(Constants.SUBMISSION_HISTORY_ACCESS);
pageContext.setAttribute("submissionHistoryAccess",submissionHistoryAccess,PageContext.PAGE_SCOPE);
String withdrawalToolsLinkAccess = (String)Constants.WITHDRAWAL_TOOLS_LINK_ACCESS;
pageContext.setAttribute("withdrawalToolsLinkAccess",withdrawalToolsLinkAccess,PageContext.PAGE_SCOPE);
Boolean fcpContent= (Boolean)request.getAttribute(Constants.IS_FCP_CONTENT);
pageContext.setAttribute("fcpContent",fcpContent,PageContext.PAGE_SCOPE);
%>
<c:set var="definedBenefit" value="${userProfile.currentContract.definedBenefitContract}"/>
<c:set var="contractStatus" value="${userProfile.currentContract.status}"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_UPLOAD_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="uploadSectionTitle"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_CREATION_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="creationSectionTitle"/>
                     
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_COMMUNICATION_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="commounicationSectionTitle"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_PAYROLL_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="payrollSectionTitle"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_UPLOAD%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="uploadTool"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_CONTRIBUTION%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="contributionSpecs"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_CENSUS%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="censusSpecs"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_COMBINATION%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="combinationSpecs"/>
              
<c:if test="${vestingCalculated==true}">
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_VESTING_CALCULATED%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="vestingSpecs"/>                    
</c:if>
<c:if test="${vestingCalculated==false}">
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_VESTING_PROVIDED%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="vestingSpecs"/>
</c:if>
                  
                     

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_MAKE_A_CONTRIBUTION_LINK%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="makeAContribution"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_CONTRIBUTION_TEMPLATE%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="contributionTemplate"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_PRE_CENSUS%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="prepopulatedCensus"/>
                     
<!-- 75155 -->                     
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_PRE_COMBINATION%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="prepopulatedCombination"/>
                                       
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_PRE_VESTING%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="prepopulatedVesting"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_PRE_LONG_TERM_PART_TIME%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="prepopulatedLongTermPartTime"/>
                     
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_COMPANIES%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="participatingPayrollCompanies"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_RETIREMENT_RESOURCE_CENTER%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="retirementResourceCenter"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_ROTH_CALCULATOR%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="rothCalculator"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_LOANSANDWITHDRAWALS%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="loanAndWithdrawalsLink"/>
                     
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_LOANS%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="loansLink"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_WITHDRAWALS%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="withdrawalsLink"/>
                     
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_SUBMIT_A_DOCUMENT_LINK%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="submitDocuments"/>    
                     
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_MAKE_A_FUND_CHANGE%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="makeAFundChange"/>                                

<!--  Auto Enroll content Constants -->
<content:contentBean contentId="<%=ContentConstants.AUTO_ENROLL_EMPLOYEE_LETTER_TEMPLATE%>"
    				 type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
    				 id="employeeLetterTemplate"/>

<content:contentBean contentId="<%=ContentConstants.AUTO_ENROLL_EMPLOYEE_FILE_TO_MERGE%>"
	 				type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
	 				id="employeeFileToMerge"/>

<content:contentBean contentId="<%=ContentConstants.AUTO_ENROLL_INSTRUCTIONS_TO_MERGE%>"
					type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
					id="employeeInstructionsToMerge"/>

<!--  Auto Enroll & Auto increase content Constants -->
<content:contentBean contentId="<%=ContentConstants.EZSTART_AND_EZINCREASE_EMPLOYEE_LETTER_TEMPLATE_AUTO%>"
    				 type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
    				 id="employeeAutoSignUpLetterTemplate"/>

<content:contentBean contentId="<%=ContentConstants.EZSTART_AND_EZINCREASE_EMPLOYEE_LETTER_TEMPLATE_SIGNUP%>"
    				 type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
    				 id="employeeManualSignUpLetterTemplate"/>

<!--  Auto increase content Constants -->
<content:contentBean contentId="<%=ContentConstants.EZINCREASE_EMPLOYEE_LETTER_TEMPLATE_AUTO%>"
    				 type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
    				 id="aciOnlyemployeeAutoSignUpLetterTemplate"/>

<content:contentBean contentId="<%=ContentConstants.EZINCREASE_EMPLOYEE_LETTER_TEMPLATE_SIGNUP%>"
    				 type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
    				 id="aciOnlyemployeeManualSignUpLetterTemplate"/>

<content:contentBean contentId="<%=ContentConstants.PARTICIPANT_MERGE_FILE%>"
	 				type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
	 				id="participantFileToMerge"/>

<content:contentBean contentId="<%=ContentConstants.UPCOMING_EZINCREASE_PARTICIPANT_MERGE_FILE%>"
	 				type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
	 				id="upComingEZincreaseParticipantFileToMergeAuto"/>
	 				
<content:contentBean contentId="<%=ContentConstants.UPCOMING_EZINCREASE_PARTICIPANT_MERGE_FILE_SIGNUP%>"
	 				type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
	 				id="upComingEZincreaseParticipantFileSignup"/>
					    
<!-- Upcomming EZincrease anniversary -WORD -->
<content:contentBean contentId="<%=ContentConstants.UPCOMMING_EZGROW_ANNIVERSARY%>"
					type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
					id="upCommingAnniversary"/>
					
<!-- Upcomming EZincrease anniversary -WORD -->
<content:contentBean contentId="<%=ContentConstants.UPCOMMING_EZGROW_ANNIVERSARY_SIGNUP%>"
					type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
					id="upCommingAnniversarySignUp"/>
					
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_AUDIT_PACKAGE%>"
					type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
					id="definedBenefitAuditPackage"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_IAT%>"
					type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
					id="definedBenefitIat"/>
					
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_DEFINED_BENEFIT_UPLOAD%>"
                     			type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     			id="definedBenefitUploadTool"/>
                     			
	<!-- Eligibility Calculation  -->   
    				 
<content:contentBean contentId="<%=ContentConstants.ELIGIBILITY_CALCULATION_EMPLOYEE_LETTER_TEMPLATE%>"
    				 type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
    				 id="employeeEligibilityLetterTemplate"/>
    				 
    <!-- Eligibility Calculation & EZ Increase  -->
                      			
<content:contentBean contentId="<%=ContentConstants.EZINCREASE_ELIGIBILITY_CALCULATION_EMPLOYEE_LETTER_TEMPLATE_AUTO%>"
    				 type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
    				 id="aciECemployeeAutoSignUpLetter"/>
    				 
<content:contentBean contentId="<%=ContentConstants.EZINCREASE_ELIGIBILITY_CALCULATION_EMPLOYEE_LETTER_TEMPLATE_SIGNUP%>"
    				 type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
    				 id="aciECemployeeAutoManualLetter"/>    				                       			

<script type="text/javascript" >
<!--
function openWin(url) {
	options="toolbar=1,status=1,menubar=1,scrollbars=1,resizable=1,width=800,height=450,left=10,top=10";
	newwindow=window.open(url, "general", options);
	if (navigator.appName=="Netscape") {
		newwindow.focus();
	}
}
//-->

function downloadEmployeeLetterCSV(serviceFeature) {
	
	var url = "/do/participant/employeeLetterDownload/?task=download&ext=.csv&sf=";
	if (serviceFeature == 'aci' || serviceFeature == 'upCommingAnniv' ) {
		url = url + serviceFeature;
	} else {
		var npedObj = document.getElementById('npeDates');
		var selIndex = npedObj.selectedIndex;
		var nped = npedObj.options[selIndex].value;
		url = url + serviceFeature + "&nped=" + nped;
	}
	location.replace(url);
}

function openEmployeeLetterPDF(fileName, serviceFeature) {
	var isMac = (navigator.userAgent.indexOf("Mac") != -1);
	var npedObj;
	var selIndex;
	var nped;
	if (serviceFeature == 'ae' || serviceFeature == 'AE_ACI'){
		npedObj = document.getElementById('npeDates');
		selIndex = npedObj.selectedIndex;
		nped = npedObj.options[selIndex].value;
	}
	
	if (isMac) {
		path = location.protocol + "//" + location.host +  fileName;
	} else {
		if (serviceFeature == 'ae' || serviceFeature == 'AE_ACI'){
			path = location.protocol + "//" + location.host + fileName + "#FDF=" + location.protocol + "//" + location.host + "/do/resources/FDFAction.fdf" + "?nped="+ nped;
		} else {
			path = location.protocol + "//" + location.host + fileName + "#FDF=" + location.protocol + "//" + location.host + "/do/resources/FDFAction.fdf";
		}
	}
	PDFWindow(path);		
}
</script>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_PIN_REGENERATION%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="pinRegeneration"/>
                     
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_CONTRACT_PIN_GEN_IN_PROCESSING%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="pinRegenerationInProcessing"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_CONTRACT_PIN_GEN_VIEW_CURRENT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="pinRegenerationView"/>
<content:contentBean contentId="<%=ContentConstants.BENEFICIARY_TRANSFER_PAGE_LINK%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="beneficaryTransferLink"/>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="10"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br>
      <img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    <td width="20"></td>
    <td width="495"></td>
    <td width="15"></td>
    <td width="210"></td>
  </tr>
  <tr>
    <td colspan="5">
	<p>
	     <content:errors scope="request" /> 
	</p>
	 <%-- <p> since form is request scope content errors are handled in request so commented this part
	    <content:errors scope="session" />
	</p>  --%>
    </td>
  </tr>

  <tr>
    <td colspan="2" width="30" valign="top">&nbsp;</td>
    <td colspan="3" width="700">
      	<table width="700" border="0" cellspacing="0" cellpadding="0">
        	<tr>
				<td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
  	        	<td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
				<td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
	        </tr>

	        <tr>
 	         <td valign="top">
			    <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
   	         	<img src="/assets/unmanaged/images/s.gif" width="5" height="1">
				<img src="<content:pageImage type="pageTitle" id="layoutPageBean" path=""/>" />
				<br>
		    	<table width="500" border="0" cellspacing="0" cellpadding="0">
              	 <tr>
					<td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
					<td valign="top" width="495">
   	 	                <content:getAttribute id="layoutPageBean" attribute="introduction1"/>

				    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
   	 	                <tr>
   	 	                	<td valign="bottom"><content:getAttribute id="layoutPageBean" attribute="introduction2"/></td>
   	 	                	
  	   		            </tr>
  	   		            </table>
						<br>
						<img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
				 	 </td>
			 	   </tr>
				</table>
			</td>
			<td>
			</td>
			<td>
		        <c:if test="${not empty requestScope.mcModel}">
		        <%---------------  Start of Message Center box---------------------%>
		        <jsp:include page="../home/messageCenterBox.jsp" flush="true"/>
		        <%---------------- End of message center box -----------------------%> 
		        </c:if>
			</td>
		</tr>
		</table>

	</td>
</tr>

<% int theIndex = 0; %>

<tr>
		<td width="10"><img src="/assets/unmanaged/images/spacer.gif" width="10" height="1" border="0"></td>
		<td colspan="2" width="495">
			<table width="495" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td width="80"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td width="327"><img src="/assets/unmanaged/images/s.gif" width="327" height="1"></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td width="80"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
                      <td width="5"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                      <td width="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                    <tr class="tablehead">
                      <td class="tableheadTD1" colspan="4"><b> <content:getAttribute id="uploadSectionTitle" attribute="title"/></b>
                      </td>
                      <td></td>
                      <td colspan="3" class="tableheadTD" align="center"><b>&nbsp;</b></td>
                    </tr>
                 	<% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td align="center">&nbsp;</td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					  <td align="left" valign="top">
					   		<c:if test="${submissionHistoryAccess==true}">
									<content:getAttribute id="makeAContribution" attribute="text">
		                  				<content:param>/do/tools/submissionHistory/</content:param>
									</content:getAttribute>
</c:if>
							<%-- For now, let's share the description text between i:File and
							     Submission. We will check with business if this is okay or not --%>
							<c:if test="${definedBenefit eq true}">
								<content:getAttribute id="definedBenefitUploadTool" attribute="description" />
</c:if>
							<c:if test="${definedBenefit ne true}">
							<content:getAttribute id="uploadTool" attribute="description" />
</c:if>
					  </td>
                      <td></td>
                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>

				<c:if test="${!requestScope.isSDUPageSuppressed}">
				<% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td align="center">&nbsp;</td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					  <td align="left" valign="top">
					  <a id="submitDocumentsPage" href="/do/tools/secureDocumentUpload/submit/">
								<content:getAttribute id="submitDocuments" attribute="linkText"/>
			  		  </a><br>
					 	<content:getAttribute id="submitDocuments" attribute="description"/>
					  </td>
                      <td></td>
                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
				</c:if>
				
				<!--  Make a Fund Change Link - START-->
				<c:if test = "${fcpContent eq true}">
				<% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td align="center">&nbsp;</td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					  <td align="left" valign="top">
					  <a id="makeAFundChangePage" href="/do/tools/fundChangePortal/" target="_blank">
								<content:getAttribute id="makeAFundChange" attribute="linkText"/>
			  		  </a><br>
					 	<content:getAttribute id="makeAFundChange" attribute="description"/>
					  </td>
                      <td></td>
                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                    </c:if>
                    <!--  Make a Fund Change Link - END-->

                   <c:if test="${requestScope[withdrawalToolsLinkAccess]}">        
                      <!-- Withdrawals -->
                      <ps:withdrawalLink contractId = '<%=userProfile.getCurrentContract().getContractNumber()%>' linkType='<%=Constants.LINKTYPE_LIST_PSW%>' >
                           <% if (theIndex++ % 2 == 0) { %>
                           <tr class="datacell1">
                           <% } else { %>
                           <tr class="datacell2">
                           <% } %>
                           <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                           <td align="center">&nbsp;</td>
                           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
     					  <td align="left" valign="top">
     					  <!-- check the request type -->
     					  <c:if test="${requestType=='loanAndWithdrawals'}">
     					 	 <a id="requestListPage" href="<content:getAttribute id="loanAndWithdrawalsLink" attribute="url"/>">
								<content:getAttribute id="loanAndWithdrawalsLink" attribute="linkText"/>
							</a>
							<br>
     							<content:getAttribute id="loanAndWithdrawalsLink" attribute="description" />
</c:if>
     						
     					  <c:if test="${requestType=='loanOnly'}">
     					  	<a id="requestListPage" href="<content:getAttribute id="loanAndWithdrawalsLink" attribute="url"/>">
								<content:getAttribute id="loansLink" attribute="linkText"/>
							</a>  
     						<br>
     							<content:getAttribute id="loansLink" attribute="description" />
</c:if>
     						
     					  <c:if test="${requestType=='withdrawalOnly'}"> 
     						 <a id="requestListPage" href="<content:getAttribute id="loanAndWithdrawalsLink" attribute="url"/>">
								<content:getAttribute id="withdrawalsLink" attribute="linkText"/>
							</a>  
     						<br>
     							<content:getAttribute id="withdrawalsLink" attribute="description" />
</c:if>
     					
     					  </td>
                           <td></td>
                           <td colspan="2" align="center" valign="middle">&nbsp;</td>
                           <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                           </tr>
                      </ps:withdrawalLink>    
                  </c:if>
				
        <!-- file creation tools starts -->
		<c:if test="${definedBenefit ne true}">
				
                    <tr class="tablehead">
                      <td class="tableheadTD" colspan="4" >&nbsp;
						<b><content:getAttribute id="creationSectionTitle" attribute="title"/></b></td>
                	  <td></td>
                      <td colspan="3" class="tableheadTD" align="center"><b>&nbsp;</b></td>
                    </tr>
                    
<c:if test="${userProfile.isBeforeCAStatusAccessOnly() ==false}">
                    <!-- How To Prepare A Contribution File -->
                    <% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td align="center">
							<a href="#" onclick="PDFWindow('<content:getAttribute id="contributionSpecs" attribute="url"/>');return false;">
									<img src="/assets/unmanaged/images/icon_acrobat.gif" border="0"><br>
									<content:getAttribute id="contributionSpecs" attribute="linkText"/>
							</a>

					  </td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

                      <td align="left" valign="top">

							<a href="#" onclick="PDFWindow('<content:getAttribute id="contributionSpecs" attribute="url"/>');return false;">
									<content:getAttribute id="contributionSpecs" attribute="title"/>
							</a>
							<br>
							<content:getAttribute id="contributionSpecs" attribute="description" />

                      <td></td>

                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
</c:if>

					
					<!-- How To Prepare A Combination File -->
<c:if test="${userProfile.isBeforeCAStatusAccessOnly() ==false}">
					<% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                  <td align="center">
							<a href="#" onclick="PDFWindow('<content:getAttribute id="combinationSpecs" attribute="url"/>');return false;">
									<img src="/assets/unmanaged/images/icon_acrobat.gif" border="0"><br>
									<content:getAttribute id="combinationSpecs" attribute="linkText"/>
																
							</a>
					  </td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

                      <td align="left" valign="top">
							<a href="#" onclick="PDFWindow('<content:getAttribute id="combinationSpecs" attribute="url"/>');return false;">
									<content:getAttribute id="combinationSpecs" attribute="title"/>						
							</a>
							<br>
							<content:getAttribute id="combinationSpecs" attribute="description" />
				      </td>
                      <td></td>

                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
</c:if>
                    
                    <!-- DI Applies only to Census -->
                    <c:if test="${contractStatus ne 'DI'}">									
					<!-- How To Prepare A Census File -->
					<% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                  <td align="center">
							<a href="#" onclick="PDFWindow('<content:getAttribute id="censusSpecs" attribute="url"/>');return false;">
									<img src="/assets/unmanaged/images/icon_acrobat.gif" border="0"><br>
									<content:getAttribute id="censusSpecs" attribute="linkText"/>
							</a>
					  </td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

                      <td align="left" valign="top">
							<a href="#" onclick="PDFWindow('<content:getAttribute id="censusSpecs" attribute="url"/>');return false;">
									<content:getAttribute id="censusSpecs" attribute="title"/>
							</a>
							<br>
							<content:getAttribute id="censusSpecs" attribute="description" />

				      </td>
                      <td></td>

                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                    
                    	
                    <!-- How To Prepare A Vesting File -->
                    <c:if test="${vestingAccess==true}">
                    
					<% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                  <td align="center">
							<a href="#" onclick="PDFWindow('<content:getAttribute id="vestingSpecs" attribute="url"/>');return false;">
									<img src="/assets/unmanaged/images/icon_acrobat.gif" border="0"><br>
									<content:getAttribute id="vestingSpecs" attribute="linkText"/>
							</a>
					  </td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

                      <td align="left" valign="top">
							<a href="#" onclick="PDFWindow('<content:getAttribute id="vestingSpecs" attribute="url"/>');return false;">
									<content:getAttribute id="vestingSpecs" attribute="title"/>
							</a>
							<br>
							<content:getAttribute id="vestingSpecs" attribute="description" />
							<br>
				      </td>
                      <td></td>

                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
</c:if>
</c:if>
                    
					<logicext:if name="userProfile" property="allowedUploadSubmissions" op="equal" value="true">
			        <logicext:then>
					
<c:if test="${userProfile.beforeCAStatusAccessOnly ==false}">
                    <% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		              <td align="center">
							<a href="<content:getAttribute id="contributionTemplate" attribute="url"/>">
									<img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
									<content:getAttribute id="contributionTemplate" attribute="linkText"/>
							</a>
					  </td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td align="left" valign="top">
	  						<a href="<content:getAttribute id="contributionTemplate" attribute="url"/>">
									<content:getAttribute id="contributionTemplate" attribute="title"/>
							</a>
							<br>
							<content:getAttribute id="contributionTemplate" attribute="description" />

				  	  </td>
                      <td></td>
		              <td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
</c:if>
					
					
					<!--Combination File Template Download -->
<c:if test="${userProfile.beforeCAStatusAccessOnly ==false}">
					<% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					      <td align="center">
							<a href="/do/contract/combinationTemplate?task=download&ext=.csv">
								<img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
								<content:getAttribute id="prepopulatedCombination" attribute="linkText"/> 
							</a>
					</td>

					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

					<td align="left" valign="top">
	  						<a href="/do/contract/combinationTemplate?task=download&ext=.csv">
								<content:getAttribute id="prepopulatedCombination" attribute="title"/>							
							</a>
							<br>
							<content:getAttribute id="prepopulatedCombination" attribute="description" /> 
							
					</td>

					<td></td>

					<td colspan="2" align="center" valign="middle">&nbsp;</td>
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
</c:if>
					 <!-- DI Applies only to Census-->
					<c:if test="${contractStatus ne 'DI'}">
                    <% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					      <td align="center">
							<a href="/do/contract/censusTemplate?task=download&ext=.csv">
								<img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
								<content:getAttribute id="prepopulatedCensus" attribute="linkText"/>
							</a>
					</td>

					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

					<td align="left" valign="top">
	  						<a href="/do/contract/censusTemplate?task=download&ext=.csv">
								<content:getAttribute id="prepopulatedCensus" attribute="title"/>
							</a>
							<br>
							<content:getAttribute id="prepopulatedCensus" attribute="description" />
					</td>

					<td></td>

					<td colspan="2" align="center" valign="middle">&nbsp;</td>
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					
					
</c:if>
					</logicext:then>
					</logicext:if>

					<c:if test="${contractStatus ne 'DI'}">
					<c:if test="${vestingAccess==true}">
					<% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					      <td align="center">
							<a href="/do/contract/vestingTemplate?task=download&ext=.csv">
								<img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
								<content:getAttribute id="prepopulatedVesting" attribute="linkText"/>
							</a>
					</td>

					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

					<td align="left" valign="top">
	  						<a href="/do/contract/vestingTemplate?task=download&ext=.csv">
								<content:getAttribute id="prepopulatedVesting" attribute="title"/>
							</a>
							<br>
							<content:getAttribute id="prepopulatedVesting" attribute="description" />
					</td>

					<td></td>

					<td colspan="2" align="center" valign="middle">&nbsp;</td>
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
</c:if>
</c:if>
					
					<!--LTPT Information File Template Download -->
					<logicext:if name="userProfile" property="allowedUploadSubmissions" op="equal" value="true">
				        <logicext:then>
							<c:if test="${contractStatus ne 'DI'}">
								<c:if test="${longTermPartTimeInfoTemplateAccess eq 'Y'}">
									<%
										if (theIndex++ % 2 == 0) {
									%>
									<tr class="datacell1">
										<%
											} else {
										%>
									<tr class="datacell2">
										<%
											}
										%>
									<td class="databorder"><img	src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td align="center">
										<a href="/do/contract/longTermPartTimeInformationTemplate?task=download&ext=.csv"> 
											<img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
											<content:getAttribute id="prepopulatedLongTermPartTime" attribute="linkText" />
										</a>
									</td>
		
									<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td align="left" valign="top">
										<a href="/do/contract/longTermPartTimeInformationTemplate?task=download&ext=.csv">
											<content:getAttribute id="prepopulatedLongTermPartTime" attribute="title" />
										</a> 
										<br> <content:getAttribute id="prepopulatedLongTermPartTime" attribute="description" />
									</td>
		
									<td></td>
		
									<td colspan="2" align="center" valign="middle">&nbsp;</td>
									<td class="databorder"><img	src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								</tr>
								</c:if>
							</c:if>
						</logicext:then>
					</logicext:if>
					<%
						boolean isAEFeatureOn = ServiceFeatureConstants.YES
									.equals((String) request.getAttribute(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE));
							boolean isAuto = ServiceFeatureConstants.YES
									.equals((String) request.getAttribute(ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO));
							boolean isSignUp = ServiceFeatureConstants.YES
									.equals((String) request.getAttribute(ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP));
							boolean isECFeatureOn = ServiceFeatureConstants.YES
									.equals((String) request.getAttribute(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF));
					%>
								<!-- auto-enrollment feature -->
								<% if (isAEFeatureOn || isECFeatureOn ){ %>

					<%} %>
					<% if (isAEFeatureOn && !isAuto && !isSignUp) { %>
					
					
					
					
							
						<tr class="tablehead">
	                      <td class="tableheadTD" colspan="4" >&nbsp;
							<b><content:getAttribute id="commounicationSectionTitle" attribute="title"/></b></td>
	                	  <td></td>
	                      <td colspan="3" class="tableheadTD" align="center"><b>&nbsp;</b></td>
	                    </tr>
	                    
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td > </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	                     <td align="left" valign="top">
								Select a date to customize the auto-enrollment communication tools listed below:
								
								<select name="npeDates" id="npeDates"> 
<c:forEach items="${dates}" var="element">
<option value="${element.key}">${element.value}</option>
</c:forEach>
								</select>
					      </td>
	                      <td></td>

	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td align="center">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeLetterTemplate" attribute="url"/>');return false;">
										<img src="/assets/generalimages/icon_word.gif" border="0"><br>
										<content:getAttribute id="employeeLetterTemplate" attribute="linkText"/>
								</a>
						  </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	                     <td align="left" valign="top">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeLetterTemplate" attribute="url"/>');return false;">
										<content:getAttribute id="employeeLetterTemplate" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="employeeLetterTemplate" attribute="description" />

					      </td>
	                      <td></td>

	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
							  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<td align="center">
								<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
								<a href="#" onclick="downloadEmployeeLetterCSV('ae');return false;">
								
										<img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
										<content:getAttribute id="employeeFileToMerge" attribute="linkText"/>
								</a>
						</td>

						<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<td align="left" valign="top">
						<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
						<a href="#" onclick="downloadEmployeeLetterCSV('ae');return false;">
		  						<content:getAttribute id="employeeFileToMerge" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="employeeFileToMerge" attribute="description"/>
						</td>
						<td></td>
						<td colspan="2" align="center" valign="middle">&nbsp;</td>
							  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
						<c:if test="${not empty employeeInstructionsToMerge}">   
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td align="center">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeInstructionsToMerge" attribute="url"/>');return false;">
										<img src="/assets/unmanaged/images/icon_acrobat.gif" border="0"><br>
										<content:getAttribute id="employeeInstructionsToMerge" attribute="linkText"/>
								</a>
						  </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	                     <td align="left" valign="top">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeInstructionsToMerge" attribute="url"/>');return false;">
									<content:getAttribute id="employeeInstructionsToMerge" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="employeeInstructionsToMerge" attribute="description"/>

					      </td>
	                      <td></td>

	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    </c:if>
<!--  auto enrollment & auto contribution increase  -->	                    
					<% } else if (isAuto || isSignUp) { %>
<!--  Auto Enroll & Auto increase content Constants -->	

						<tr class="tablehead">
	                      <td class="tableheadTD" colspan="4" >&nbsp;
							<b><content:getAttribute id="commounicationSectionTitle" attribute="title"/></b>
							</td>
	                	  <td></td>
	                      <td colspan="3" class="tableheadTD" align="center"><b>&nbsp;</b></td>
	                    </tr>	
	                     <% if (isECFeatureOn && !isAEFeatureOn) { %>
	                     <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td > </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                     <td align="left" valign="top">
								Select a plan entry date to customize the communication tools listed below:
								<select name="npeDates" id="npeDates"> 
								<c:forEach items="${dates}" var="element">
								<option value="${element.key}">${element.value}</option>
								</c:forEach>
								</select>
					      </td>
	                      <td></td>
	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    <%} %>
	                    			
				  <% if (isAEFeatureOn) { %>
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td > </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                     <td align="left" valign="top">
								Select a date to customize the auto-enrollment communication tools listed below:
								<select name="npeDates" id="npeDates"> 
<c:forEach items="${dates}" var="element">
<option value="${element.key}">${element.value}</option>
</c:forEach>
								</select>
					      </td>
	                      <td></td>
	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                       <% if (isAuto) {%>
	                       <input type="hidden" name="isAciSignupAuto" value="true"/>
		                   	<td align="center">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeAutoSignUpLetterTemplate" attribute="url"/>');return false;">
									<img src="/assets/generalimages/icon_word.gif" border="0"><br>
									<content:getAttribute id="employeeAutoSignUpLetterTemplate" attribute="linkText"/>
								</a>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td align="left" valign="top">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeAutoSignUpLetterTemplate" attribute="url"/>');return false;">
									<content:getAttribute id="employeeAutoSignUpLetterTemplate" attribute="title"/>
								</a><br>
								<content:getAttribute id="employeeAutoSignUpLetterTemplate" attribute="description" />
							</td>
					      <%} else {%>
		                   	<td align="center">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeManualSignUpLetterTemplate" attribute="url"/>');return false;">
									<img src="/assets/generalimages/icon_word.gif" border="0"><br>
									<content:getAttribute id="employeeManualSignUpLetterTemplate" attribute="linkText"/>
								</a>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td align="left" valign="top">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeManualSignUpLetterTemplate" attribute="url"/>');return false;">
									<content:getAttribute id="employeeManualSignUpLetterTemplate" attribute="title"/>
								</a><br>
								<content:getAttribute id="employeeManualSignUpLetterTemplate" attribute="description" />
							</td>
					      <%} %>
	                      <td></td>
	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
							  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td align="center">
								<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
								<a href="#" onclick="downloadEmployeeLetterCSV('aeACI');return false;">
										<img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
										<content:getAttribute id="employeeFileToMerge" attribute="linkText"/>
								</a>
						</td>
						<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td align="left" valign="top">
						<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
						<a href="#" onclick="downloadEmployeeLetterCSV('aeACI');return false;">
		  						<content:getAttribute id="employeeFileToMerge" attribute="title"/>
						</a><br>
								<content:getAttribute id="employeeFileToMerge" attribute="description"/>
						</td>
						<td></td>
						<td colspan="2" align="center" valign="middle">&nbsp;</td>
							  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
	                <%} %>
	                <% if (!isAEFeatureOn) { %>
	                 <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                       <% if (isAuto) {%>
	                       <input type="hidden" name="isAciSignupAuto" value="true"/>
		                   	<td align="center">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="aciECemployeeAutoSignUpLetter" attribute="url"/>');return false;">
									<img src="/assets/generalimages/icon_word.gif" border="0"><br>
									<content:getAttribute id="aciECemployeeAutoSignUpLetter" attribute="linkText"/>
								</a>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td align="left" valign="top">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="aciECemployeeAutoSignUpLetter" attribute="url"/>');return false;">
									<content:getAttribute id="aciECemployeeAutoSignUpLetter" attribute="title"/>
								</a><br>
								<content:getAttribute id="aciECemployeeAutoSignUpLetter" attribute="description" />
							</td>
					      <%} else {%>
		                   	<td align="center">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="aciECemployeeAutoManualLetter" attribute="url"/>');return false;">
									<img src="/assets/generalimages/icon_word.gif" border="0"><br>
									<content:getAttribute id="aciECemployeeAutoManualLetter" attribute="linkText"/>
								</a>
							</td>
							<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td align="left" valign="top">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="aciECemployeeAutoManualLetter" attribute="url"/>');return false;">
									<content:getAttribute id="aciECemployeeAutoManualLetter" attribute="title"/>
								</a><br>
								<content:getAttribute id="aciECemployeeAutoManualLetter" attribute="description" />
							</td>
					      <%} %>
	                      <td></td>
	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                
	                 <%} %>
	                <% if (isECFeatureOn && !isAEFeatureOn) { %>
	                   
	              
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
							  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td align="center">
								<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
								<a href="#" onclick="downloadEmployeeLetterCSV('EC');return false;">
										<img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
										<content:getAttribute id="employeeFileToMerge" attribute="linkText"/>
								</a>
						</td>
						<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td align="left" valign="top">
						<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
						<a href="#" onclick="downloadEmployeeLetterCSV('EC');return false;">
		  						<content:getAttribute id="employeeFileToMerge" attribute="title"/>
						</a><br>
								<content:getAttribute id="employeeFileToMerge" attribute="description"/>
						</td>
						<td></td>
						<td colspan="2" align="center" valign="middle">&nbsp;</td>
							  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
	                    
	                   
	                <%} %>

	                  <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                          <% if (isAuto) {%>
		                  <td align="center">
							<a href="#" onclick="PDFWindow('<content:getAttribute id="aciOnlyemployeeAutoSignUpLetterTemplate" attribute="url"/>');return false;">
								<img src="/assets/generalimages/icon_word.gif" border="0"><br>
								<content:getAttribute id="aciOnlyemployeeAutoSignUpLetterTemplate" attribute="linkText"/>
							</a>
						  </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                      <td align="left" valign="top">
						  	<a href="#" onclick="PDFWindow('<content:getAttribute id="aciOnlyemployeeAutoSignUpLetterTemplate" attribute="url"/>');return false;">
								<content:getAttribute id="aciOnlyemployeeAutoSignUpLetterTemplate" attribute="title"/>
							</a><br>
							<content:getAttribute id="aciOnlyemployeeAutoSignUpLetterTemplate" attribute="description" />
					      </td>
					      <%} else { %>
					      <td align="center">
							<a href="#" onclick="PDFWindow('<content:getAttribute id="aciOnlyemployeeManualSignUpLetterTemplate" attribute="url"/>');return false;">
								<img src="/assets/generalimages/icon_acrobat.gif" border="0"><br>
								<content:getAttribute id="aciOnlyemployeeManualSignUpLetterTemplate" attribute="linkText"/>
							</a>
						  </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                      <td align="left" valign="top">
						  	<a href="#" onclick="PDFWindow('<content:getAttribute id="aciOnlyemployeeManualSignUpLetterTemplate" attribute="url"/>');return false;">
								<content:getAttribute id="aciOnlyemployeeManualSignUpLetterTemplate" attribute="title"/>
							</a><br>
							<content:getAttribute id="aciOnlyemployeeManualSignUpLetterTemplate" attribute="description" />
					      </td>
					      <%} %>
	                      <td></td>
	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    <%if(isECFeatureOn && !isAEFeatureOn){ %>
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td align="center">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeEligibilityLetterTemplate" attribute="url"/>');return false;">
										<img src="/assets/generalimages/icon_word.gif" border="0"><br>
										<content:getAttribute id="employeeEligibilityLetterTemplate" attribute="linkText"/>
								</a>
						  </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	                     <td align="left" valign="top">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeEligibilityLetterTemplate" attribute="url"/>');return false;">
										<content:getAttribute id="employeeEligibilityLetterTemplate" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="employeeEligibilityLetterTemplate" attribute="description" />

					      </td>
	                      <td></td>

	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    <% } %>
	                    
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td align="center">
								<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
								<a href="#" onclick="downloadEmployeeLetterCSV('aci');return false;">
								
										<img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
										<content:getAttribute id="participantFileToMerge" attribute="linkText"/>
								</a>
						</td>
						<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td align="left" valign="top">
						<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
						<a href="#" onclick="downloadEmployeeLetterCSV('aci');return false;">
		  						<content:getAttribute id="participantFileToMerge" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="participantFileToMerge" attribute="description"/>
						</td>
						<td></td>
						<td colspan="2" align="center" valign="middle">&nbsp;</td>
							  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
						<c:if test="${not empty employeeInstructionsToMerge}">
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td align="center">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeInstructionsToMerge" attribute="url"/>');return false;">
										<img src="/assets/unmanaged/images/icon_acrobat.gif" border="0"><br>
										<content:getAttribute id="employeeInstructionsToMerge" attribute="linkText"/>
								</a>
						  </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                     <td align="left" valign="top">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeInstructionsToMerge" attribute="url"/>');return false;">
									<content:getAttribute id="employeeInstructionsToMerge" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="employeeInstructionsToMerge" attribute="description"/>
					      </td>
	                      <td></td>
	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    </c:if>
	                    
	                     <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td align="center">
		                        <% if (isAuto) {%>
								<a href="#" onclick="PDFWindow('<content:getAttribute id="upCommingAnniversary" attribute="url"/>');return false;">
										<img src="/assets/generalimages/icon_word.gif" border="0"><br>
										<content:getAttribute id="upCommingAnniversary" attribute="linkText"/>
								</a>
								<% } else { %>
								<a href="#" onclick="PDFWindow('<content:getAttribute id="upCommingAnniversarySignUp" attribute="url"/>');return false;">
										<img src="/assets/generalimages/icon_word.gif" border="0"><br>
										<content:getAttribute id="upCommingAnniversarySignUp" attribute="linkText"/>
								</a>
								<% } %>
						  </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                     <td align="left" valign="top">
	                            <% if (isAuto) {%>
								<a href="#" onclick="PDFWindow('<content:getAttribute id="upCommingAnniversary" attribute="url"/>');return false;">
									<content:getAttribute id="upCommingAnniversary" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="upCommingAnniversary" attribute="description"/>
								<% } else { %>
								<a href="#" onclick="PDFWindow('<content:getAttribute id="upCommingAnniversarySignUp" attribute="url"/>');return false;">
									<content:getAttribute id="upCommingAnniversarySignUp" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="upCommingAnniversarySignUp" attribute="description"/>
								<% } %>
					      </td>
	                      <td></td>
	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    
	                    <!--  Upcomming anniversary template - Merge File-->
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td align="center">
								<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
								<a href="#" onclick="downloadEmployeeLetterCSV('upCommingAnniv');return false;">
								       <img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
								        <% if (isAuto) { %>
										<content:getAttribute id="upComingEZincreaseParticipantFileToMergeAuto" attribute="linkText"/>
										<% } else { %>
										<content:getAttribute id="upComingEZincreaseParticipantFileSignup" attribute="linkText"/>
										<% } %>
								</a>
						</td>
						<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td align="left" valign="top">
						<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
						<a href="#" onclick="downloadEmployeeLetterCSV('upCommingAnniv');return false;">
						        <% if (isAuto) { %>
		  						<content:getAttribute id="upComingEZincreaseParticipantFileToMergeAuto" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="upComingEZincreaseParticipantFileToMergeAuto" attribute="description"/>
								<% } else { %>
								<content:getAttribute id="upComingEZincreaseParticipantFileSignup" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="upComingEZincreaseParticipantFileSignup" attribute="description"/>
								<% } %>
						</td>
						<td></td>
						<td colspan="2" align="center" valign="middle">&nbsp;</td>
							  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
					<% }else if (isECFeatureOn){ %>
					
					<tr class="tablehead">
	                      <td class="tableheadTD" colspan="4" >&nbsp;
							<b><content:getAttribute id="commounicationSectionTitle" attribute="title"/></b></td>
	                	  <td></td>
	                      <td colspan="3" class="tableheadTD" align="center"><b>&nbsp;</b></td>
	                    </tr>
	                    
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td > </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	                     <td align="left" valign="top">
								Select a plan entry date to customize the communication tools listed below:
								
								<select name="npeDates" id="npeDates"> 
<c:forEach items="${dates}" var="element">
<option value="${element.key}">${element.value}</option>
</c:forEach>
								</select>
					      </td>
	                      <td></td>

	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td align="center">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeEligibilityLetterTemplate" attribute="url"/>');return false;">
										<img src="/assets/generalimages/icon_word.gif" border="0"><br>
										<content:getAttribute id="employeeEligibilityLetterTemplate" attribute="linkText"/>
								</a>
						  </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	                     <td align="left" valign="top">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeEligibilityLetterTemplate" attribute="url"/>');return false;">
										<content:getAttribute id="employeeEligibilityLetterTemplate" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="employeeEligibilityLetterTemplate" attribute="description" />

					      </td>
	                      <td></td>

	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
							  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<td align="center">
								<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
								<a href="#" onclick="downloadEmployeeLetterCSV('EC');return false;">
								
										<img src="/assets/unmanaged/images/icon_excel.gif" border="0"><br>
										<content:getAttribute id="employeeFileToMerge" attribute="linkText"/>
								</a>
						</td>

						<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						<td align="left" valign="top">
						<!-- <a href="/do/participant/employeeLetterDownload/?task=download&ext=.csv"> -->
						<a href="#" onclick="downloadEmployeeLetterCSV('EC');return false;">
		  						<content:getAttribute id="employeeFileToMerge" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="employeeFileToMerge" attribute="description"/>
						</td>
						<td></td>
						<td colspan="2" align="center" valign="middle">&nbsp;</td>
							  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
	                    
	                    <c:if test="${not empty employeeInstructionsToMerge}">                    
	                    <% if (theIndex++ % 2 == 0) { %>
	                    <tr class="datacell1">
	                    <% } else { %>
	                    <tr class="datacell2">
	                    <% } %>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td align="center">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeInstructionsToMerge" attribute="url"/>');return false;">
										<img src="/assets/unmanaged/images/icon_acrobat.gif" border="0"><br>
										<content:getAttribute id="employeeInstructionsToMerge" attribute="linkText"/>
								</a>
						  </td>
	                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	                     <td align="left" valign="top">
								<a href="#" onclick="PDFWindow('<content:getAttribute id="employeeInstructionsToMerge" attribute="url"/>');return false;">
									<content:getAttribute id="employeeInstructionsToMerge" attribute="title"/>
								</a>
								<br>
								<content:getAttribute id="employeeInstructionsToMerge" attribute="description"/>

					      </td>
	                      <td></td>

	                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
	                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    </tr>
	                    </c:if>
					
					<% } %>
<!-- end  auto enrollment & auto contribution increase feature -->					

</c:if>

<!-- additional tools starts -->

				<tr class="tablehead">
                      <td class="tableheadTD" colspan="4">&nbsp;
                      <b><content:getAttribute id="payrollSectionTitle" attribute="title" /></b></td>

                <td></td>
                      <td colspan="3" class="tableheadTD" align="center"><b>&nbsp;</b></td>
                    </tr>

		<!-- list of participating payroll vendors only for non defined benefit contracts  -->
		<c:if test="${definedBenefit ne true}">

                    <% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

                <td align="center">
							<a href="#" onclick="PDFWindow('<content:getAttribute id="participatingPayrollCompanies" attribute="url"/>');return false;">
									<img src="/assets/unmanaged/images/icon_acrobat.gif" border="0"><br>
									<content:getAttribute id="participatingPayrollCompanies" attribute="linkText"/>
							</a>
				</td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td align="left" valign="top">

	  						<a href="#" onclick="PDFWindow('<content:getAttribute id="participatingPayrollCompanies" attribute="url"/>');return false;">
									<content:getAttribute id="participatingPayrollCompanies" attribute="title"/>
							</a>
							<br>
							<content:getAttribute id="participatingPayrollCompanies" attribute="description" />

						</td>
                      <td></td>

                		<td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
</c:if>
                    
		<!-- activity planner only for non defined benefit contracts  -->
		<c:if test="${definedBenefit ne true}">

			<c:if test="${not empty retirementResourceCenter}">
                    <% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

                <td align="center">
							<a href="#" onclick="ActivityPlannerWindow('<content:getAttribute id="retirementResourceCenter" attribute="url"/>');return false;">
									<img src="/assets/unmanaged/images/icon_online.gif" border="0"><br>
									<content:getAttribute id="retirementResourceCenter" attribute="linkText"/>
							</a>
				</td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td align="left" valign="top">

	  						<a href="#" onclick="ActivityPlannerWindow('<content:getAttribute id="retirementResourceCenter" attribute="url"/>');return false;">
									<content:getAttribute id="retirementResourceCenter" attribute="title"/>
							</a>
							<br>
							<content:getAttribute id="retirementResourceCenter" attribute="description" />
						</td>
                      <td></td>

                		<td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                    </c:if>
</c:if>
                    
		<!-- 401k roth calculator only for non defined benefit contracts  -->
		<c:if test="${definedBenefit ne true}">

                    <% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

                <td align="center">
							<a href="#" onclick="RothWindow('<content:getAttribute id="rothCalculator" attribute="url"/>');return false;">
									<img src="/assets/unmanaged/images/icon_online.gif" border="0"><br>
									<content:getAttribute id="rothCalculator" attribute="linkText"/>
							</a>
				</td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td align="left" valign="top">

	  						<a href="#" onclick="RothWindow('<content:getAttribute id="rothCalculator" attribute="url"/>');return false;">
									<content:getAttribute id="rothCalculator" attribute="title"/>
							</a>
							<br>
							<content:getAttribute id="rothCalculator" attribute="description" />

						</td>
                      <td></td>

                		<td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
</c:if>

                <!-- defined benefit audit package -->
		<c:if test="${definedBenefit eq true}">
                    <% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td align="center">
					  	<a href="<content:getAttribute id="definedBenefitAuditPackage" attribute="url"/>">
							<img src="/assets/unmanaged/images/icon_zip.gif" border="0"><br>
							<content:getAttribute id="definedBenefitAuditPackage" attribute="linkText"/>
			 			</a>
				      </td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

                      <td align="left" valign="top">
			  <a href="<content:getAttribute id="definedBenefitAuditPackage" attribute="url"/>">
			  	<content:getAttribute id="definedBenefitAuditPackage" attribute="linkText"/>
			  </a>
			<br>
			  <content:getAttribute id="definedBenefitAuditPackage" attribute="description" />
                      <td></td>

                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>                
</c:if>

                <!-- defined benefit iat -->
		<c:if test="${definedBenefit eq true}">
                    <% if (theIndex++ % 2 == 0) { %>
                    <tr class="datacell1">
                    <% } else { %>
                    <tr class="datacell2">
                    <% } %>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td align="center">                          
			  <a href="javascript:openPDF('<content:getAttribute id="definedBenefitIat" attribute="url"/>');" return true"> 
				<img src="/assets/unmanaged/images/icon_acrobat.gif" border="0"><br>
				<content:getAttribute id="definedBenefitIat" attribute="linkText"/>
			 </a>

		      </td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

                      <td align="left" valign="top">
			  <a href="javascript:openPDF('<content:getAttribute id="definedBenefitIat" attribute="url"/>');" return true"> 
				<content:getAttribute id="definedBenefitIat" attribute="title"/>
			  </a>
			<br>
			  <content:getAttribute id="definedBenefitIat" attribute="description" />
                      <td></td>

                      <td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>                
</c:if>

   
  
  <%-- Beneficiary Transfer Page --%>
  <c:if test="${definedBenefit ne true}">
  <% if(userProfile.isSuperCar() || userProfile.getRole() instanceof TeamLead || userProfile.getRole() instanceof BundledGaCAR ) {%> 
  		   <% if (theIndex++ % 2 == 0) { %>
           <tr class="datacell1">
           <% } else { %>
           <tr class="datacell2">
           <% } %>
           <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
           <td align="center" >
           
           <a href="/do/tools/beneficiaryTransfer/">
           <img src="/assets/unmanaged/images/icon_online.gif" border="0"><br>
           <content:getAttribute id="beneficaryTransferLink" attribute="linkText"/>
           </a>
           </td>
           
           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
           <td align="left" valign="top">
           
           <a href="/do/tools/beneficiaryTransfer/">
           <content:getAttribute id="beneficaryTransferLink" attribute="title"/>
           </a>
           <br>
           
           <content:getAttribute id="beneficaryTransferLink" attribute="description"/>
           
           </td>
           <td></td>

                		<td colspan="2" align="center" valign="middle">&nbsp;</td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
            <%} %>
</c:if>
                      <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td rowspan="2"  colspan="2" width="5" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
                    </tr>
                    <tr>
                      <td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
			    	<tr>
						<td><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
			    		<td colspan=5 > &nbsp;
						
						</td>
					</tr>
					<tr>
				   		<td><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
				    	<td colspan="5" >
							<p><content:pageFooter id="layoutPageBean"/></p>
 							<p class="footnote"><content:pageFootnotes id="layoutPageBean"/></p>
 							<p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>
 						</td>
 					</tr>

            </table>
          </td>
    <!-- column 3 HELPFUL HINT -->
    <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
    <td width="210" align="center" valign="top">
      <img src="/assets/unmanaged/images/s.gif" width="1" height="25">
      
      <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
      <content:rightHandLayerDisplay layerName="layer1" beanName="layoutPageBean" />
      <img src="/assets/unmanaged/images/s.gif" width="1" height="5">

			</td>
			<!--// end column 3 -->
        </tr>
      </table>
	  <img src="/assets/unmanaged/images/s.gif" width="1" height="20">
    </td>
  </tr>
</table>
