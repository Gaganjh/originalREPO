<%-- Withdrawal Personal Information R/O JSP Fragment --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="webConstants" className="com.manulife.pension.ps.web.Constants" />

<%-- Bean Definition for CMA Content --%>
<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_PERSONAL_INFORMATION}"
  beanName="personalInformation"/>

<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
    contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_EE_SNAPSHOT}"
    beanName="employeeSnapshot"/>

<content:contentBean type="LayoutPage" 
  contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_1}" 
  beanName="step1PageBean" /> 
 <content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_SPOUSAL_CONSENT_MAY_BE_REQUIRED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentMayBeRequiredText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_SPOUSAL_CONSENT_MAY_BE_REQUIRED_IF_MARRIED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentMayBeRequiredIsMarriedText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_SPOUSAL_CONSENT_MUST_BE_OBTAINED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentMustBeObtainedText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_SPOUSAL_CONSENT_IS_NOT_REQUIRED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentIsNotRequiredText"/>

<script type="text/javascript" src="/assets/unmanaged/javascript/jquery-3.6.0.min.js"></script>
<script language="JavaScript1.2" type="text/javascript">

function setSpousalConsentText(legallyMarriedInd) {
	  $("#spousalConsentText").text('');
	  if (legallyMarriedInd == 'N') {
	    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentIsNotRequiredText" escapeJavaScript="true" attribute="text"/>');
	  } else if (legallyMarriedInd == 'Y') {
	    <c:if test="${empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.spousalConsentRequired}">
	    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentMayBeRequiredIsMarriedText" escapeJavaScript="true" attribute="text"/>');
	    </c:if>
	    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.spousalConsentRequired eq true}">
	    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentMustBeObtainedText" escapeJavaScript="true" attribute="text"/>');
	    </c:if>
	  } else {
	    <c:if test="${empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.spousalConsentRequired or withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.spousalConsentRequired eq true}">
	    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentMayBeRequiredText" escapeJavaScript="true" attribute="text"/>');
	    </c:if>
	  }
	}

	$(document).ready(function() {
	  setSpousalConsentText('${withdrawalForm.withdrawalRequestUi.legallyMarriedInd}');
	});
</script>
<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td class="tableheadTD1">
      <div style="padding-top:5px;padding-bottom:5px">
        <b><content:getAttribute attribute="text" beanName="personalInformation" /></b>
      </div>
    </td>
    
    <td class="tablehead" style="text-align:right" nowrap>
      
<%-- Show only on View Withdrawal Request page. --%>
<c:if test="${pageId == 'viewRequest'}">
    
  <c:if test="${empty param.printFriendly}">
    <c:if test="${withdrawalRequestUi.isRequestPending}">

    <script type="text/javascript">
    
      var isTpa = false;
      <ps:isTpa name="${webConstants.USERPROFILE_KEY}" property="role">
        isTpa = true;
      </ps:isTpa>
      
      
      function doEmployeeSnapshot(empProfileId) {
      
        if (isTpa) {
          var baseUrl = "/do/census/tpaViewEmployeeSnapshot/";
        } else {
          var baseUrl = "/do/census/viewEmployeeSnapshot/";
        }
        
      	var printUrl = baseUrl + "?profileId=" + empProfileId + "&printFriendly=true";
      	window.open(printUrl,"","width=720,height=480,resizable,toolbar,scrollbars,");
      }
    </script>
    <b>  
      <a href="javascript:doEmployeeSnapshot(<c:out value="${withdrawalRequestUi.employeeProfileId}"/>)">
        <content:getAttribute attribute="text" beanName="employeeSnapshot" />
      </a>
    </b>      
    </c:if>    
  </c:if>
 
</c:if>    
      
      &nbsp;
    </td>
  </tr>
</table>
  
<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td>
   
      <table border="0" cellpadding="0" cellspacing="0" width="498">
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Name</strong></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">
            <c:out value="${withdrawalRequest.firstName}"/>&nbsp;
            <c:out value="${withdrawalRequest.lastName}"/>
          </td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>SSN</strong></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn"><render:ssn><c:out value="${withdrawalRequest.participantSSN}"/></render:ssn></td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Contract number</strong></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn"><c:out value="${withdrawalRequest.contractId}"/></td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Contract name</strong></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn"><c:out value="${withdrawalRequest.contractName}"/></td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>State of residence</strong></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">
            <c:choose>
              <c:when test="${withdrawalRequest.participantStateOfResidence eq 'ZZ'}">
                ZZ&nbsp;Outside US
              </c:when>
              <c:otherwise>
                <c:out value="${withdrawalRequest.participantStateOfResidence}" />&nbsp;
                <ps:displayDescription collection="${states}" keyName="code" keyValue="description" 
                  key="${withdrawalRequest.participantStateOfResidence}"/>
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Date of birth</strong></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn"><fmt:formatDate value="${withdrawalRequest.birthDate}" type="DATE" pattern="MM/dd/yyyy"/></td>
        </tr>
        <c:if test="${withdrawalRequest.contractInfo.spousalConsentRequired ne false}">
        
         <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Legally Married</strong><br><span id="spousalConsentText"></span></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          
          <td class="indentedValueColumn" style="vertical-align:top;"><c:if test="${not empty withdrawalRequest.legallyMarriedInd}">
                   <c:out value="${withdrawalRequest.legallyMarriedInd eq 'Y'? 'Yes' : 'No'}"/>
                 </c:if></td>
        </tr>
        
        </c:if>
               
        
        <tr>
        
        
          <td colspan="3">
        
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
              <tr class="datacell1" valign="top">
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              </tr>
<c:if test="${withdrawalRequestUi.showStaticContent}">
              <tr class="datacell1" valign="top">
                <td class="sectionCommentText"><content:getAttribute beanName="step1PageBean" attribute="body1"/></td>
              </tr>
</c:if>              
            </table>
          
          </td>
        </tr>
        
      </table>
     
    </td>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  </tr>
  <tr>
    <td colspan="3">
   
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
          <td rowspan="2" width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" height="5" width="5"></td>
        </tr>
        <tr>
          <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
      </table>
      
    </td>
  </tr>
</table>