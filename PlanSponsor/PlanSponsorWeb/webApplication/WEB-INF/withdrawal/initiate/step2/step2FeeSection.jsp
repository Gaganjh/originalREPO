<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

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
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_VESTING_ENGINE_HAS_PROVIDE_PERCENTS}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="vestingEngineHasProvidePercentsText"/>

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
                 <ps:fieldHilight name="withdrawalRequestUi.fees[${feeStatus.index}].value" singleDisplay="true"/> 
                 TPA withdrawal fee
               </strong>
<form:input path="withdrawalRequestUi.fees[${feeStatus.index}].value" maxlength="12" onchange="return handleFeeValueChanged(${feeStatus.index});" cssClass="inputRight" id="feeValueId[${feeStatus.index}]"/>




 <form:select path="withdrawalRequestUi.withdrawalRequest.fees[${feeStatus.index}].typeCode" id="feeValueTypeId[${feeStatus.index}]" onchange="return handleFeeValueTypeChanged(${feeStatus.index});">


                 <form:option value="">- select -</form:option>
                 <form:options items="${tpaTransactionFeeTypes}" itemValue="code" itemLabel="description"/>
</form:select>
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
       	     &&(withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence ne 'CT'))
       	     }">
       	     	<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>
       	     </c:if>
       	     <c:if test="${(((withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence eq 'PR') 
       	     && (withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractIssuedStateCode eq 'PR'))
       	     || (withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence eq 'CT'))
       	    }">
       	     	<content:getAttribute beanName="layoutPageBean" attribute="body3Header"/>
       	     </c:if>
             <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.vestingCouldNotBeCalculatedInd}">
               <content:getAttribute beanName="couldNotCalculateVestingSectionContent" attribute="text"/>
             </c:if>
             <c:if test="${withdrawalForm.withdrawalRequestUi.participantIsConsideredFullyVested}">
               <content:getAttribute beanName="participantHasPartialStatusText" attribute="text"/>
             </c:if>
             <c:if test="${withdrawalForm.withdrawalRequestUi.hasPre1987MoneyTypes}">
               <content:getAttribute beanName="participantHasPre1987MoneyTypeText" attribute="text"/>
             </c:if>
             <c:if test="${withdrawalForm.withdrawalRequestUi.showReasonIsFullyVestedFromPlanSpecialMessage}">
               <li>
                 <content:getAttribute beanName="withdrawalReasonIsFullyVestedText" attribute="text"/>
               </li>
             </c:if>
             <c:if test="${withdrawalForm.withdrawalRequestUi.showVestingProvidedSpecialMessage}">
               <li>
                 <content:getAttribute beanName="vestingEngineHasProvidePercentsText" attribute="text">
                   <content:param
                     ><fmt:formatDate 
                       value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.latestVestingEffectiveDate}" 
                       type="DATE" 
                       pattern="MM/dd/yyyy"
                   /></content:param>
                 </content:getAttribute>
               </li>
             </c:if>
           </ul>
         </td>
       </tr>
       <tr>
       	 <td>
       		 <ul style="margin-left: ${isIE ? '20' : '-20'}px;">
       			 <content:getAttribute beanName="layoutPageBean" attribute="body1"/>
       		 </ul>
         </td>
       </tr>
       <tr class="datacell1">
         <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
       </tr>
     </table>
  </td>
</tr>
