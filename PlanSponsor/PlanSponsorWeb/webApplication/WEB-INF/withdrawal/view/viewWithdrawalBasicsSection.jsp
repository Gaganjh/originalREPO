<%-- Withdrawal Basics Information Section R/O JSP Fragment --%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />

<%-- Bean Definition for CMA Content --%>
<content:contentBean 
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_BASIC_INFORMATION}" 
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  beanName="basicInformation"/>

<content:contentBean type="LayoutPage" 
    contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_1}" 
    beanName="step1PageBean" /> 
    
<content:contentBean
  contentId="${contentConstants.LAST_PROCESSED_CONTRIBUTION_DATE_COMMENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="lastProcessedDateCommentText"/>

<c:set scope="request" var="withdrawalTypes" value="${withdrawalForm.lookupData['ONLINE_WITHDRAWAL_REASONS']}"/>
<c:set scope="request" var="hardshipTypes" value="${withdrawalForm.lookupData['HARDSHIP_REASONS']}"/>
<c:set scope="request" var="paymentToTypes" value="${withdrawalForm.lookupData['PAYMENT_TO_TYPE']}"/>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td class="tableheadTD1">
      <div style="padding-top:5px;padding-bottom:5px">
        <b><content:getAttribute attribute="text" beanName="basicInformation" /></b>
      </div>
    </td>
    <td class="tablehead" style="text-align:right" nowrap>
      &nbsp;
    </td>
  </tr>
</table>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td>
    
      <table border="0" cellpadding="0" cellspacing="0" width="498">
        <%-- Request Date --%>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Request date</strong></td>
          <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">
            <fmt:formatDate value="${withdrawalRequest.requestDate}" type="DATE" pattern="MM/dd/yyyy"/>
          </td>
        </tr>
        <%-- Expiration Date --%>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Expiration date</strong></td>
          <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">
            <fmt:formatDate value="${withdrawalRequest.expirationDate}" type="DATE" pattern="MM/dd/yyyy"/>
          </td>
        </tr>
        <%-- Withdrawal Reason --%>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Type of withdrawal</strong></td>
          <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">
            <ps:displayDescription collection="${withdrawalTypes}" keyName="code" keyValue="description" 
              key="${withdrawalRequest.reasonCode}"/>
          </td>
        </tr>
        
        <c:if test="${withdrawalRequestUi.showHardshipReasonAndHardshipDescription}">
        	
          <tr class="datacell1" valign="top">
            <td class="sectionNameColumn"><strong>Hardship reason</strong></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="indentedValueColumn">
          	  <ps:displayDescription collection="${hardshipTypes}" keyName="code" keyValue="description" 
                key="${withdrawalRequest.reasonDetailCode}"/>
		    </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td class="sectionNameColumn"><strong>Hardship description</strong></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="indentedValueColumn">${withdrawalRequest.reasonDescriptionForDisplay}</td>
          </tr>
          
        </c:if>
                <%-- Participant leaving plan --%>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Participant leaving plan?</strong></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn"> 
            <c:choose>
              <c:when test="${withdrawalRequest.participantLeavingPlanInd}">Yes</c:when>
              <c:otherwise>No</c:otherwise>	
	        </c:choose>
          </td>
        </tr>
        <%-- WMSI / PenChecks --%>
        <c:if test="${withdrawalRequestUi.showWmsiPenchecks}">
        
          <tr class="datacell1" valign="top">
	        <td class="sectionNameColumn"><strong>IRA provider</strong></td>
	        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	        <td class="indentedValueColumn">
  		      <c:choose>
  			      <c:when test="${withdrawalRequest.iraServiceProviderCode==requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}">Wealth Management Systems, Inc.</c:when>
              <c:when test="${withdrawalRequest.iraServiceProviderCode==requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}">PenChecks</c:when>
              <c:when test="${withdrawalRequest.iraServiceProviderCode==requestConstants.IRA_SERVICE_PROVIDER_NEITHER_CODE}">Other</c:when>
              <c:otherwise></c:otherwise>	
            </c:choose>
    	 		</td>
        </tr>
		</c:if>
        
        <%-- Termination Date --%>
        <c:if test="${withdrawalRequestUi.showDisabilityDate}">
          <tr class="datacell1" valign="top">
            <td class="sectionNameColumn"><strong>Disability date </strong></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="indentedValueColumn">
              <fmt:formatDate value="${withdrawalRequest.disabilityDate}" type="DATE" pattern="MM/dd/yyyy"/>
            </td>
          </tr>
        </c:if>
        
        <c:if test="${withdrawalRequestUi.showRetirementDate}">
          <tr class="datacell1" valign="top">
            <td class="sectionNameColumn"><strong>Retirement date </strong></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="indentedValueColumn">
              <fmt:formatDate value="${withdrawalRequest.retirementDate}" type="DATE" pattern="MM/dd/yyyy"/>
            </td>
          </tr>
        </c:if>
    
        <c:if test="${withdrawalRequestUi.showTerminationDate}">
          <tr class="datacell1" valign="top">
            <td class="sectionNameColumn"><strong>Termination date </strong></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="indentedValueColumn">
              <fmt:formatDate value="${withdrawalRequest.terminationDate}" type="DATE" pattern="MM/dd/yyyy"/>
            </td>
          </tr>
        </c:if>
        
        <%-- Last processed contribution date --%>
        <c:if test="${withdrawalRequestUi.withdrawalRequest.showFinalContributionDate}">
          <tr class="datacell1" valign="top">
            <td class="sectionNameColumn"><strong>Last contribution payroll ending date</strong></td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="indentedValueColumn">
              <fmt:formatDate value="${withdrawalRequest.mostRecentPriorContributionDate}" type="DATE" pattern="MM/dd/yyyy"/> 
            </td>
          </tr> 
        </c:if>
        
        <%-- Final contribution date --%>
        <c:if test="${withdrawalRequestUi.withdrawalRequest.showFinalContributionDate}">
          <tr class="datacell1" valign="top">
            <td class="sectionNameColumn"><strong>Final contribution date</strong></td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="indentedValueColumn">
              <fmt:formatDate value="${withdrawalRequest.finalContributionDate}" type="DATE" pattern="MM/dd/yyyy"/>
            </td>
          </tr>
          <tr>
            <td id="basicFinalContributionDateCommentId" colspan="3" class="sectionCommentText">
              <content:getAttribute beanName="lastProcessedDateCommentText" attribute="text"/>
            </td>
          </tr>
        </c:if>
        
        <%-- Payment to --%>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Payment to</strong></td>
          <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">
            <ps:displayDescription collection="${paymentToTypes}" keyName="code" keyValue="description" 
              key="${withdrawalRequest.paymentTo}"/>
          </td>
        </tr>
        


        <%-- CMA content --%>
        <tr>
          <td colspan="3">
          
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
              <tr class="datacell1" valign="top">
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              </tr>
<c:if test="${withdrawalRequestUi.showStaticContent}">
              <tr class="datacell1" valign="top" >
                <td class="sectionCommentText"><content:getAttribute beanName="step1PageBean" attribute="body2"/></td>
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
