<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US"/>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="activityConstants" className="com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_COULD_NOT_CALCULATE_VESTING_CONTENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="couldNotCalculateVestingSectionContent"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_PARTIAL_CONTENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="participantHasPartialStatusText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_PRE_1987_CONTENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="participantHasPre1987MoneyTypeText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_WITHDRAWAL_REASON_IS_FULLY_VESTED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="withdrawalReasonIsFullyVestedText"/>
<content:contentBean type="LayoutPage" 
  contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2}" 
  beanName="step2PageBean" /> 

<tr>
 <td>
   <table width="498" border="0" cellpadding="0" cellspacing="0">
     <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.hasATpaFirm}">
       <!-- Begin Fee Loop -->
       <c:forEach items="${withdrawalForm.withdrawalRequestUi.fees}"
                  var="fee"
                  varStatus="feeStatus">
         <tr valign="top" class="datacell2">
           <td class="datacell2">
            <span class="sectionName">
              <strong>
                TPA withdrawal fee
              </strong>
              <ps:fieldHilight name="withdrawalRequestUi.fees[${feeStatus.index}].value" singleDisplay="true"> 
                   <ps:activityHistory itemNumber="${activityConstants.TPA_FEE_AMOUNT.id}"/>
                </ps:fieldHilight>
              <c:choose>
                	<c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
                	<fmt:formatNumber type="currency"
		                                currencySymbol=""
		                                minFractionDigits="2"
		                                value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.fees[feeStatus.index].value}"/>
                	<ps:displayDescription collection="${tpaTransactionFeeTypes}" keyName="code" keyValue="description" 
				                  key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.fees[feeStatus.index].typeCode}"/>
                	</c:when>
			    	<c:otherwise>
<form:input path="withdrawalRequestUi.fees[${feeStatus.index}].value" maxlength="12" onchange="return handleFeeValueChanged(${feeStatus.index});" cssClass="inputRight" id="feeValueId[${feeStatus.index}]"/>




              <ps:activityHistory itemNumber="${activityConstants.TPA_FEE_TYPE.id}"/>
 <form:select path="withdrawalRequestUi.withdrawalRequest.fees[${feeStatus.index}].typeCode" id="feeValueTypeId[${feeStatus.index}]" onchange="return handleFeeValueTypeChanged(${feeStatus.index});">


                <form:option value="">- select -</form:option>
                <form:options items="${tpaTransactionFeeTypes}" itemValue="code" itemLabel="description"/>
</form:select>
              </c:otherwise>
			   </c:choose>
              </span>
           </td>
         </tr>
       </c:forEach>
      </c:if>
       <!-- End Fee Loop -->
       <tr>
         <td>
           <ul style="margin-left: ${isIE ? '20' : '-20'}px;">
             <c:if test="${(((withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence ne 'PR') || (withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractIssuedStateCode ne 'PR'))
       	     && (withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence ne 'CT'))
       	     }">
     	     	<content:getAttribute beanName="step2PageBean" attribute="body1Header"/>
     	     </c:if>
      	     <c:if test="${(((withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence eq 'PR') 
       	     && (withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractIssuedStateCode eq 'PR'))
       	     || (withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence eq 'CT'))
       	    }">
      	     	<content:getAttribute beanName="step2PageBean" attribute="body3Header"/>
      	     </c:if>
             <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.vestingCouldNotBeCalculatedInd}">
               <content:getAttribute beanName="couldNotCalculateVestingSectionContent" attribute="text"/>
             </c:if>
             <c:if test="${withdrawalForm.withdrawalRequestUi.participantHasPartialStatus}">
               <content:getAttribute beanName="participantHasPartialStatusText" attribute="text"/>
             </c:if>
             <c:if test="${withdrawalForm.withdrawalRequestUi.hasPre1987MoneyTypes}">
               <content:getAttribute beanName="participantHasPre1987MoneyTypeText" attribute="text"/>
             </c:if>
             <c:if test="${withdrawalForm.withdrawalRequestUi.showReasonIsFullyVestedFromPlanSpecialMessage}">
               <content:getAttribute beanName="withdrawalReasonIsFullyVestedText" attribute="text"/>
             </c:if>
            </ul>
          </td>
       </tr>
       <tr>
        <td>
          <ul style="margin-left: ${isIE ? '20' : '-20'}px;">
            <content:getAttribute beanName="step2PageBean" attribute="body1"/>
          </ul>
       </td>
     </tr>
     <tr class="datacell1">
       <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
     </tr>
   </table>
 </td>
</tr>
