<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_BASIC_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="basicInformationSectionTitle"/>
<content:contentBean
  contentId="${contentConstants.LAST_PROCESSED_CONTRIBUTION_DATE_COMMENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="lastProcessedDateCommentText"/>
<form:hidden path="withdrawalRequestUi.withdrawalRequest.robustDateChangedAfterVesting" id="robustDateChangedAfterVestingIndicatorId"/>
<div style="padding-top:10px;padding-bottom:10px;">
  <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td class="tableheadTD1" colspan="3">
        <div style="padding-top:5px;padding-bottom:5px">
          <span style="padding-right:2px" id="basicInformationShowIcon" onclick="showBasicInformationSection();">
            <img src="/assets/unmanaged/images/plus_icon.gif" width="12" height="12" border="0">
          </span>
          <span style="padding-right:2px" id="basicInformationHideIcon" onclick="hideBasicInformationSection();">
            <img src="/assets/unmanaged/images/minus_icon.gif" width="12" height="12" border="0">
          </span>
          <b><content:getAttribute beanName="basicInformationSectionTitle" attribute="text"/></b>
        </div>
      </td>
    </tr>
    <tr>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="basicInformationTable">
          <tr class="datacell1" valign="top">
            <td class="longSectionNameColumn">
              <strong>
                <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.reasonCode" singleDisplay="true"/>
                Type of withdrawal
              </strong>
            </td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="shortIndentedValueColumn">
 <form:select id="withdrawalReasonId" cssClass="mandatory" onchange="return handleWithdrawalReasonChanged();" path="withdrawalRequestUi.withdrawalRequest.reasonCode">



                <form:option value="">- select -</form:option>
                <form:options items="${withdrawalReasons}" itemValue="code" itemLabel="description"/>               
</form:select>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td>
              <span id="basicHardshipReasonCol1Id" class="longSectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                <strong>
                  <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.reasonDetailCode" singleDisplay="true"/>
                  Hardship reason
                </strong>
              </span>
            </td>
            <td class="datadivider">
              <span id="basicHardshipReasonCol2Id">
                <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
              </span>
            </td>
            <td>
              <span id="basicHardshipReasonCol3Id" class="shortIndentedValueColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
 <form:select id="hardshipReasonId" onchange="return handleHardshipReasonChanged();" path="withdrawalRequestUi.withdrawalRequest.reasonDetailCode">


                  <form:option value="">- select -</form:option>
                  <form:options items="${hardshipTypes}" itemValue="code" itemLabel="description"/>               
</form:select>
              </span>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td>
              <span id="basicHardshipExplanationCol1Id" class="longSectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                <strong>
                  <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.hardshipReasonExplanation" singleDisplay="true"/>
                  Hardship explanation
                </strong>
              </span>
            </td>
            <td class="datadivider" width="1">
              <span id="basicHardshipExplanationCol2Id">
                <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
              </span>
            </td>
            <td>
              <span id="basicHardshipExplanationCol3Id" class="shortIndentedValueColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
<form:textarea path="withdrawalRequestUi.withdrawalRequest.reasonDescription" onchange="return handleHardshipExplanationChanged();" id="hardshipExplanationId"/>


              </span>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td>
              <span id="basicIraServiceProviderCol1Id" class="longSectionNameColumn" style="vertical-align: top; padding-left: ${isIE ? '2' : '4'}px;">
                <strong>
                	<ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" singleDisplay="true"/>
                	IRA provider
                </strong>
              </span>
            </td>
            <td class="datadivider">
              <span id="basicIraServiceProviderCol2Id">
                <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
              </span>
            </td>
            <td>
              <div id="basicIraServiceProviderCol3Id" class="shortValueColumn" style="width: ${isIE ? '283' : '287'}px;">
          
                        <%--    <input type="radio" name="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" value="${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}"
                            class="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}"  onclick="handleWmsiPaychecksChanged();"/>PenChecks
                <br/>
                <input type="radio" name="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" value="${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}" class="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}"  onclick="handleWmsiPaychecksChanged();"/>Wealth Management Systems, Inc.
                <br/>
                <input type="radio" onclick="handleWmsiPaychecksChanged();" name="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" class="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" value="${requestConstants.IRA_SERVICE_PROVIDER_NEITHER_CODE}"/>Other --%>
                <form:radiobutton onclick="handleWmsiPaychecksChanged();" path="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" cssClass="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" value="${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}"/>PenChecks



                <br/>
<form:radiobutton onclick="handleWmsiPaychecksChanged();" path="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" cssClass="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" value="${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}"/>Wealth Management Systems, Inc.



                <br/>
<form:radiobutton onclick="handleWmsiPaychecksChanged();" path="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" cssClass="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" value="${requestConstants.IRA_SERVICE_PROVIDER_NEITHER_CODE}"/>Other
                
              </div>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td class="longSectionNameColumn"><strong>Participant leaving plan?<a href="javascript:openChangeWindow()"></a></strong></td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="shortIndentedValueColumn">
              <span id="participantLeavingPlanEditableSpanId">
<form:radiobutton onclick="handleParticipantLeavingPlanChanged();" path="withdrawalRequestUi.participantLeavingPlanInd" id="leavingyes" cssClass="mandatory" value="true"/> Yes
<form:radiobutton onclick="handleParticipantLeavingPlanChanged();" path="withdrawalRequestUi.participantLeavingPlanInd" id="leavingno" cssClass="mandatory" value="false"/> No
              </span>
              <span id="participantLeavingPlanReadOnlySpanId">
                Yes
              </span>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
             <td>
               <span id="basicTerminationDateCol1Id" class="longSectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                 <strong>
                   <ps:fieldHilight name="withdrawalRequestUi.terminationDate" singleDisplay="true"/>
                   Termination date
                 </strong>
               </span>
             </td>
             <td class="datadivider">
               <span id="basicTerminationDateCol2Id"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></span>
             </td>
             <td>
               <span id="basicTerminationDateCol3Id" class="shortIndentedValueColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
<form:input path="withdrawalRequestUi.terminationDate" maxlength="10" onchange="return handleTerminationDateChanged();" cssClass="mandatory" id="terminationDateId"/>




                 <img onclick="return handleDateIconClicked(event, 'terminationDateId', true, true);" src="/assets/unmanaged/images/cal.gif" border="0">
                 (mm/dd/yyyy)
               </span>
             </td>
          </tr>
          <tr class="datacell1" valign="top">
             <td>
               <span id="basicRetirementDateCol1Id" class="longSectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                 <strong>
                   <ps:fieldHilight name="withdrawalRequestUi.retirementDate" singleDisplay="true"/>
                   Retirement date
                 </strong>
               </span>
             </td>
             <td class="datadivider">
               <span id="basicRetirementDateCol2Id">
                 <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
               </span>
             </td>
             <td>
               <span id="basicRetirementDateCol3Id" class="shortIndentedValueColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
<form:input path="withdrawalRequestUi.retirementDate" maxlength="10" onchange="return handleRetirementDateChanged();" cssClass="mandatory" id="retirementDateId"/>




                 <img onclick="return handleDateIconClicked(event, 'retirementDateId', true, true);" src="/assets/unmanaged/images/cal.gif" border="0">
                 (mm/dd/yyyy)
               </span>
             </td>
          </tr>
          <tr class="datacell1" valign="top">
             <td>
               <span id="basicDisabilityDateCol1Id" class="longSectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                 <strong>
                   <ps:fieldHilight name="withdrawalRequestUi.disabilityDate" singleDisplay="true"/>
                   Disability date
                 </strong>
               </span>
             </td>
             <td class="datadivider">
               <span id="basicDisabilityDateCol2Id">
                 <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
               </span>  
	           </td>
             <td>
               <span id="basicDisabilityDateCol3Id" class="shortIndentedValueColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
<form:input path="withdrawalRequestUi.disabilityDate" maxlength="10" onchange="return handleDisabilityDateChanged();" cssClass="mandatory" id="disabilityDateId"/>




                 <img onclick="return handleDateIconClicked(event, 'disabilityDateId', true, true);" src="/assets/unmanaged/images/cal.gif" border="0">
                 (mm/dd/yyyy)
               </span>
             </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td nowrap="nowrap">
              <span id="basicLastContributionPayrollEndingDateCol1Id" class="longSectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                <strong>Last contribution payroll ending date</strong>
              </span>
            </td>
            <td class="datadivider">
              <span id="basicLastContributionPayrollEndingDateCol2Id">
                <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
              </span>
            </td>
            <td>
              <span id="basicLastContributionPayrollEndingDateCol3Id" class="shortIndentedValueColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
              ${withdrawalForm.withdrawalRequestUi.mostRecentPriorContributionDate}
              </span>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td>
              <span id="basicFinalContributionDateCol1Id" class="longSectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                <strong>
                  <ps:fieldHilight name="withdrawalRequestUi.finalContributionDate" singleDisplay="true"/>
                  Final contribution date
                </strong>
              </span>
            </td>
            <td class="datadivider">
              <span id="basicFinalContributionDateCol2Id">
                <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
              </span>
            </td>
            <td>
              <span id="basicFinalContributionDateCol3Id" class="shortIndentedValueColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
<form:input path="withdrawalRequestUi.finalContributionDate" maxlength="10" onchange="return handleFinalContributionDateChanged();" cssClass="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" id="finalContributionDateId"/>




                <img onclick="return handleDateIconClicked(event, 'finalContributionDateId', true, false);" src="/assets/unmanaged/images/cal.gif" border="0">
                (mm/dd/yyyy)
              </span>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td colspan="3">
              <span id="basicFinalContributionDateCommentId" class="indentedValue" style="padding-left: ${isIE ? '2' : '4'}px;">
                <content:getAttribute beanName="lastProcessedDateCommentText" attribute="text"/>
              </span>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td class="longSectionNameColumn">
              <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.paymentTo" singleDisplay="true"/>
              <strong>Payment to</strong>
            </td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="shortIndentedValueColumn">
 <form:select id="paymentToId" cssClass="mandatory" onchange="return handlePaymentToChanged();" path="withdrawalRequestUi.withdrawalRequest.paymentTo">



                <c:if test="${fn:length(paymentToTypes) > 1}">                           
                  <form:option value="">- select -</form:option>
                </c:if>
                <form:options items="${paymentToTypes}" itemValue="code" itemLabel="description"/>  
</form:select>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td colspan="3" class="indentedValue">
              <content:getAttribute beanName="layoutPageBean" attribute="body2"/>
            </td>
          </tr>
        </table>
      </td>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3">
        <div id="basicInformationFooter">
          <table border="0" cellspacing="0" width="100%">
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
        </div>
      </td>
    </tr>
  </table>
</div>
