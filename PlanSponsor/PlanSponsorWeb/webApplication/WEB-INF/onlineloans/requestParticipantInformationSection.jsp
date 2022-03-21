<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="/WEB-INF/render.tld" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanFields" className="com.manulife.pension.service.loan.LoanField" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />
<un:useConstants var="loanConstants" className="com.manulife.pension.service.loan.domain.LoanConstants" />

<content:contentBean 
  contentId="${loanContentConstants.EMPLOYEE_SNAPSHOT_LINK}" 
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="employeeSnapshotLinkText"/>
<content:contentBean
  contentId="${loanContentConstants.SPOUSAL_CONSENT_MAY_BE_REQUIRED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentMayBeRequiredText"/>
<content:contentBean
  contentId="${loanContentConstants.SPOUSAL_CONSENT_MAY_BE_REQUIRED_IF_MARRIED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentMayBeRequiredIsMarriedText"/>
<content:contentBean
  contentId="${loanContentConstants.SPOUSAL_CONSENT_MUST_BE_OBTAINED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentMustBeObtainedText"/>
<content:contentBean
  contentId="${loanContentConstants.SPOUSAL_CONSENT_IS_NOT_REQUIRED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentIsNotRequiredText"/>
<content:contentBean
  contentId="${loanContentConstants.PARTICIPANT_INFORMATION_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="participantInfoTitleText"/>
<content:contentBean
  contentId="${loanContentConstants.PARTICIPANT_INFORMATION_SECTION_FOOTER}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="participantInfoFooterText"/>

<script language="JavaScript1.2" type="text/javascript">

function doPrintFiendlyEmplyoeeSnapshot(){
printURLEmplyoeeSnapshot = location.protocol+"//"+location.host+"/do/census/viewEmployeeSnapshot/?profileId=${loanForm.participantProfileId}&source=participantAccount&printFriendly=true";
  window.open(printURLEmplyoeeSnapshot,"","width=720,height=480,resizable,toolbar,scrollbars,");
}

function setSpousalConsentText(legallyMarriedInd) {
  $("#spousalConsentText").text('');
  if (legallyMarriedInd == 'N') {
    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentIsNotRequiredText" escapeJavaScript="true" attribute="text"/>');
  } else if (legallyMarriedInd == 'Y') {
    <c:if test="${empty loanPlanData.spousalConsentReqdInd}">
    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentMayBeRequiredIsMarriedText" escapeJavaScript="true" attribute="text"/>');
    </c:if>
    <c:if test="${loanPlanData.spousalConsentReqdInd == 'Y'}">
    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentMustBeObtainedText" escapeJavaScript="true" attribute="text"/>');
    </c:if>
  } else {
    <c:if test="${empty loanPlanData.spousalConsentReqdInd or loanPlanData.spousalConsentReqdInd == 'Y'}">
    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentMayBeRequiredText" escapeJavaScript="true" attribute="text"/>');
    </c:if>
  }
}

$(document).ready(function() {
  setSpousalConsentText("${e:forJavaScriptBlock(loanForm.legallyMarried)}");
});

</script>

  <table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
     <tr>
       <td class="tableheadTD1">
             <b><content:getAttribute attribute="text" beanName="participantInfoTitleText">
             </content:getAttribute></b>
       </td>
       <td width="254" align="right" class="tablehead">
         <c:if test="${displayRules.displayEmployeeSnapshotLink}">
           <a href="#" onclick="doPrintFiendlyEmplyoeeSnapshot()">
             <content:getAttribute attribute="text" beanName="employeeSnapshotLinkText">
             </content:getAttribute>
           </a>
         </c:if>
         <c:if test="${not displayRules.displayEmployeeSnapshotLink}">
           &nbsp;
         </c:if>
       </td>
     </tr>

  </table>

  <table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
			
     <tr>
		 
       <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
       <td><table border="0" cellpadding="0" cellspacing="0" width="498">
           
             
             <tr valign="top">
               <td class="datacell1"><strong>Name</strong></td>
               <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

               <td class="highlightBold" valign="top">
                 <span class="highlight">
                   <c:out value="${loanForm.firstName}"/>
			       <c:if test="${displayRules.displayMiddleInitial}">
				     <c:out value="${loanForm.middleInitial}"/>
                   </c:if>
                   <c:out value="${loanForm.lastName}"/>
                 </span>
               </td>
               <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
               <td class="datacell1"><strong>Contract number </strong></td>
               <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
               <td class="highlightBold" valign="top"><c:out value="${loanForm.contractId}"/></td>
             </tr>
             <tr valign="top">

               <td class="datacell1" width="122"><strong>SSN</strong></td>
               <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
			   <td width="109" class="highlightBold" valign="bottom">
				<c:choose>
				  <c:when test="${displayRules.maskSsn}">
                    <render:ssn property="loanForm.ssn" />
				  </c:when>
				  <c:otherwise>
				    <render:ssn property="loanForm.ssn" useMask="false"/>
				  </c:otherwise>
				</c:choose>
			   </td>			   
			   
               <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
               <td width="109" class="datacell1"><strong>Contract name </strong></td>
               <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
               <td width="155" class="highlightBold" valign="bottom"><c:out value="${loanForm.contractName}"/></td>

             </tr>
             <tr valign="top">
               <td class="datacell1"><strong>Employment status </strong></td>
               <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
               <td class="highlightBold" valign="bottom">${employeeStatuses[loanForm.employmentStatusCode]}</td>
               <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
               <td>&nbsp;</td>
               <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

               <td>&nbsp;</td>
             </tr>
		 <c:if test="${displayRules.displayLegallyMarried}">
		   <tr valign="top">
	           <td class="datacell1">
                 <ps:fieldHilight name="${loanFields.LEGALLY_MARRIED_IND.fieldName}" singleDisplay="true"/>
                 <strong>Legally married </strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td>
			 <c:choose>
			   <c:when test="${displayRules.showLegallyMarriedAsEditable}">
	               <ps:trackChanges escape="true" property="legallyMarried" name="loanForm"/>
<form:radiobutton onclick="setSpousalConsentText('Y')" path="legallyMarried" value="Y"/>Yes

<form:radiobutton onclick="setSpousalConsentText('N')" path="legallyMarried" value="N"/>No

			   </c:when>
			   <c:otherwise>
                 <c:if test="${not empty loan.legallyMarriedInd}">
                   <c:out value="${loan.legallyMarriedInd ? 'Yes' : 'No'}"/>
                 </c:if> 
			   </c:otherwise>
			 </c:choose>
		     </td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

                 <td>&nbsp;</td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td>&nbsp;</td>
		   </tr>

           <c:if test="${displayRules.displaySpousalConsentText}">
             <tr valign="top">
               <td colspan="7" class="datacell1"><span id="spousalConsentText"></span></td>
             </tr>
           </c:if>
         </c:if>

         <tr valign="top">
           <td colspan="7" class="datacell1">
	   	     <content:getAttribute attribute="text" beanName="participantInfoFooterText">
                 </content:getAttribute>
           </td>
         </tr>
       </table>
    </td>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  </tr>
  <tr>
    <tr>
        <td colspan="3" class="boxborder"><img
            src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
  </tr>
  </table>
