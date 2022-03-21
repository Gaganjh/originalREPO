<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="scheduleConstants" className="com.manulife.pension.service.contract.valueobject.VestingSchedule" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_VESTING}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="vestingText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_NO_VESTING_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noMoneyTypesText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_CALCULATE_VESTING_SELECTED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="calculateVestingSelected"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_CALCULATE_VESTING_NOT_SELECTED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="calculateVestingNotSelected"/>

<script type="text/javascript">
function enableDisableVestingSchedule(enable) {
   if (enable == false) {
     for (var i = 0; i < ${fn:length(planDataForm.planDataUi.planData.vestingSchedules)}; i++) {
       var vestingScheduleTypeSelectId = "#planDataUi_vestingSchedules_" + i + "_vestingSchedule_vestingScheduleType";
       if ($(vestingScheduleTypeSelectId).length > 0) {
	       $(vestingScheduleTypeSelectId).val("");
	       $(vestingScheduleTypeSelectId).prop("disabled", true);
	       handleVestingScheduleChanged($(vestingScheduleTypeSelectId)[0], i);
       }
     }
   } else {
     for (var i = 0; i < ${fn:length(planDataForm.planDataUi.planData.vestingSchedules)}; i++) {
       var vestingScheduleTypeSelectId = "#planDataUi_vestingSchedules_" + i + "_vestingSchedule_vestingScheduleType";
       if ($(vestingScheduleTypeSelectId).length > 0) {
	       $(vestingScheduleTypeSelectId).prop("disabled", false);
	       handleVestingScheduleChanged($(vestingScheduleTypeSelectId)[0], i);
       }
     }
   }
}

$(document).ready(function() {
  <c:if test="${planDataForm.editMode}">
  var multipleVestingSchedulesForOneSingleMoneyType = $("input[name='planDataUi.planData.multipleVestingSchedulesForOneSingleMoneyType']:checked").val();
  if (multipleVestingSchedulesForOneSingleMoneyType == "${planDataConstants.YES_CODE}") {
    enableDisableVestingSchedule(false);
  }
  </c:if>
});

</script>

<div id="vestingAndForfeituresTabDivId" class="borderedDataBox">
  <div class="subhead">
    <c:if test="${not param.printFriendly}">
      <div class="expandCollapseIcons" id="vestingAndForfeituresShowIconId" onclick="expandDataDiv('vestingAndForfeitures');">
        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
      </div>
      <div class="expandCollapseIcons" id="vestingAndForfeituresHideIconId" onclick="collapseDataDiv('vestingAndForfeitures');">
        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
      </div>
    </c:if>
    <div class="sectionTitle">
      <content:getAttribute beanName="vestingText" attribute="text"/>
    </div>
    <div class="sectionHighlightIcon" id="vestingAndForfeituresSectionHighlightIconId">
      <ps:sectionHilight name="${uiConstants.SECTION_VESTING}" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
    </div>
    <div class="endDataRowAndClearFloats"></div>
  </div>
  <div id="vestingAndForfeituresDataDivId">
    <div>
      <div class="subsubhead">
        General vesting provisions
      </div>

      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="vestingAndForfeituresLabelColumn">
              100% vesting applies to the following withdrawal reasons            
            </td>
            <td class="dataColumn">
              <form:checkbox path="planDataUi.selectedWithdrawalReasons" 
                             value="${planDataConstants.WITHDRAWAL_REASON_RETIREMENT}" 
                             onclick="setDirtyFlag();"
                             disabled="${disableFields}"/>
              <ps:displayDescription collection="${withdrawalReasons}" keyName="withdrawalReasonCode" keyValue="withdrawalReasonDescription" key="${planDataConstants.WITHDRAWAL_REASON_RETIREMENT}"/>
              <c:if test="${planDataForm.planDataUi.planData.earlyRetirementAllowed}">
                <form:checkbox path="planDataUi.selectedWithdrawalReasons" 
                               value="${planDataConstants.WITHDRAWAL_REASON_EARLY_RETIREMENT}" 
                               onclick="setDirtyFlag();"
                               disabled="${disableFields}"/>
                Early retirement
              </c:if>
              <form:checkbox path="planDataUi.selectedWithdrawalReasons" 
                             value="${planDataConstants.WITHDRAWAL_REASON_DEATH}" 
                             onclick="setDirtyFlag();"
                             disabled="${disableFields}"/>
              <ps:displayDescription collection="${withdrawalReasons}" keyName="withdrawalReasonCode" keyValue="withdrawalReasonDescription" key="${planDataConstants.WITHDRAWAL_REASON_DEATH}"/>
              <form:checkbox path="planDataUi.selectedWithdrawalReasons" 
                             value="${planDataConstants.WITHDRAWAL_REASON_DISABILITY}" 
                             onclick="setDirtyFlag();"
                             disabled="${disableFields}"/>
              Permanent disability 
            </td>
          </tr>
        </table>
      </div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="vestingAndForfeituresLabelColumn">
              <ps:fieldHilight name="planDataUi.planData.vestingServiceCreditMethod" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
              Vesting service crediting method
            </td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
                  <c:forEach items="${vestingServiceCreditMethods}" var="method">
                    <c:choose>
                      <c:when test="${(method.code == planDataConstants.VESTING_SERVICE_CREDIT_METHOD_UNSPECIFIED) and (planDataForm.planDataUi.planData.vestingServiceCreditMethod != planDataConstants.VESTING_SERVICE_CREDIT_METHOD_UNSPECIFIED)}">
                        <%-- Suppress unspecified if method has been selected --%>
                      </c:when>
                      <c:otherwise>
<form:radiobutton disabled="${disableFields}" onclick="handleVestingServiceCreditMethodClicked('${method.code}')" path="planDataUi.planData.vestingServiceCreditMethod" value="${method.code}"/>${method.description}



                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <c:if test="${planDataForm.planDataUi.planData.vestingServiceCreditMethod != planDataConstants.UNSPECIFIED_CODE}">
                  	<ps:displayDescription collection="${vestingServiceCreditMethods}" keyName="code" keyValue="description" key="${planDataForm.planDataUi.planData.vestingServiceCreditMethod}"/>
                  </c:if>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="vestingAndForfeituresLabelColumn">
              <ps:fieldHilight name="planDataUi.planData.hoursOfService" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
              Vesting hours of service (if applicable)
            </td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.planData.hoursOfService" disabled="${(planDataForm.editMode && (planDataForm.planDataUi.planData.vestingServiceCreditMethod == planDataConstants.VESTING_SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE)) ? 'false' : 'true'}" maxlength="4" onblur="validateHoursOfService(this)" onchange="setDirtyFlag();" size="4" cssClass="numericInput" id="hoursOfServiceTextId"/>

<form:hidden path="planDataUi.planData.hoursOfService" id="hoursOfServiceHiddenId" disabled="${(planDataForm.editMode && (planDataForm.planDataUi.planData.vestingServiceCreditMethod == planDataConstants.VESTING_SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE)) ? 'true' : 'false'}" />


                </c:when>
                <c:otherwise>
                  <fmt:formatNumber groupingUsed="true" value="${planDataForm.planDataUi.planData.hoursOfService}"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
<%-- vesting computation period --%>      
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="vestingAndForfeituresLabelColumn" valign="top">
              <ps:fieldHilight name="planDataUi.planData.vestingComputationPeriod" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
              The vesting computation period shall be ...
            </td>
            <td class="dataColumn" valign="top">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.vestingComputationPeriod" value="${planDataConstants.VESTING_COMPUTATION_PERIOD_PLAN_YEAR_CODE}"/>${planDataConstants.VESTING_COMPUTATION_PERIOD_PLAN_YEAR_STRING}


                   <br>
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.vestingComputationPeriod" value="${planDataConstants.VESTING_COMPUTATION_BASED_ON_HOS_FIRST_AND_EACH_ANNIVERSARY_THEREOF_CODE}"/>the date an Employee first performs an Hour of Service<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;and each anniversary thereof


                   <br>
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.vestingComputationPeriod" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}


                </c:when>
                <c:otherwise>
                  ${planDataForm.planDataUi.vestingComputationPeriodDisplay}
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
 
 <%-- multiple vesting schedules --%>      
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="vestingAndForfeituresLabelColumn">
              <ps:fieldHilight name="planDataUi.planData.multipleVestingSchedulesForOneSingleMoneyType" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
              Does the plan have two or more vesting schedules for any single money type?
            </td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:radiobutton disabled="${disableFields}" onclick="enableDisableVestingSchedule(false)" path="planDataUi.planData.multipleVestingSchedulesForOneSingleMoneyType" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="enableDisableVestingSchedule(true)" path="planDataUi.planData.multipleVestingSchedulesForOneSingleMoneyType" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



<form:radiobutton disabled="${disableFields}" onclick="enableDisableVestingSchedule(true)" path="planDataUi.planData.multipleVestingSchedulesForOneSingleMoneyType" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}



                </c:when>
                <c:otherwise>
                  ${planDataForm.planDataUi.multipleVestingSchedulesForOneSingleMoneyTypeDisplay}
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>     
      
      <div class="subsubhead">Vesting schedule</div>
    </div>
    <div>
      <%-- We are using inline styles to remove leftmost and rightmost borders to prevent double width (containing div has a border) --%>
      <table class="vestingSchedule">
        <thead class="evenDataRow">
          <th style="border-left-width: 0;"/>
          <th/>
          <th colspan="8" style="border-right-width: 0;">Completed years of service</th>
        </thead>
        <thead class="evenDataRow">
          <th style="border-left-width: 0; border-top-width: 1px;">Money Type</th>
          <th style="border-top-width: 1px;">Vesting Schedule</th>
          <c:forEach begin="0" end="${scheduleConstants.YEARS_OF_SERVICE}" var="year" varStatus="yearStatus">
            <th style="border-right-width: ${yearStatus.last ? '0' : '1px'}; border-top-width: 1px;">${year}</th>
          </c:forEach>
        </thead>
        <c:choose>
          <c:when test="${empty planDataForm.planDataUi.planData.vestingSchedules}">
            <tr class="oddDataRow">
              <td class="textData" colspan="${scheduleConstants.YEARS_OF_SERVICE + 3}" style="border-left-width: 0; border-right-width: 0;">
                <content:getAttribute beanName="noMoneyTypesText" attribute="text"/>
              </td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach items="${planDataForm.planDataUi.planData.vestingSchedules}" var="vestingSchedule" varStatus="vestingScheduleStatus">
              <tr class="${(vestingScheduleStatus.count % 2 == 0) ? 'evenDataRow' : 'oddDataRow'}">
                <td class="textData" style="border-left-width: 0;">
                  <ps:fieldHilight name="planDataUi.vestingSchedules[${vestingScheduleStatus.index}].vestingSchedule.vestingScheduleType" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
                  <span onmouseover="Tip('${vestingSchedule.moneyTypeLongName}&nbsp;(${vestingSchedule.moneyTypeShortName})')" onmouseout="UnTip()">
                    ${vestingSchedule.moneyTypeShortName}
                  </span>
                </td>
                <td class="textData" nowrap="nowrap">
                  <c:choose>
                    <c:when test="${planDataForm.confirmMode or (planDataForm.editMode and not vestingSchedule.isFullyVested)}">
 <form:select path="planDataUi.vestingSchedules[${vestingScheduleStatus.index}].vestingSchedule.vestingScheduleType" onchange="return handleVestingScheduleChanged(this, ${vestingScheduleStatus.index});" id="planDataUi_vestingSchedules_${vestingScheduleStatus.index}_vestingSchedule_vestingScheduleType" disabled="${disableFields}">



                        <form:option value="">Select one</form:option>
                        <form:options items="${vestingSchedules}" itemValue="code" itemLabel="description"/>  
</form:select>
                    </c:when>
                    <c:otherwise>
                      ${vestingSchedule.vestingScheduleDescription}
                    </c:otherwise>
                  </c:choose>
                </td>
                <c:forEach items="${vestingSchedule.schedules}" var="vestedAmount" varStatus="vestedAmountStatus">
                  <td class="numericData" style="border-right-width: ${vestedAmountStatus.last ? '0' : '1px'}" nowrap="nowrap">
                    <ps:fieldHilight name="planDataUi.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" singleDisplay="true" displayToolTip="true"/>
                    <c:choose>
                      <c:when test="${planDataForm.editMode}">
                        <c:choose>
                          <c:when test="${vestingSchedule.isFullyVested}">
<form:input path="planDataUi.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" disabled="true" maxlength="7" size="4" cssClass="numericInput"/>


<form:hidden path="planDataUi.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount"/>
                         
                          </c:when>
                          <c:otherwise>
<form:input path="planDataUi.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" disabled="${not planDataForm.planDataUi.vestingSchedules[vestingScheduleStatus.index].schedules[vestedAmountStatus.index].isFieldEditable}" maxlength="7" onblur="validateVestingSchedulePercent(this, '${vestingSchedule.moneyTypeId}', '${vestedAmountStatus.index}')" onchange="setDirtyFlag();" size="4" cssClass="numericInput" id="vestedAmount[${vestingScheduleStatus.index}][${vestedAmountStatus.index}]TextId"/>

<form:hidden path="planDataUi.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount"
                                         disabled="${planDataForm.planDataUi.vestingSchedules[vestingScheduleStatus.index].schedules[vestedAmountStatus.index].isFieldEditable}"
                                         id="vestedAmount[${vestingScheduleStatus.index}][${vestedAmountStatus.index}]HiddenId"/>
                        

                          </c:otherwise>
                        </c:choose>
                        %
                      </c:when>
                      <c:when test="${planDataForm.confirmMode}">
<form:input path="planDataUi.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" disabled="true" maxlength="7" size="4" cssClass="numericInput"/>




                        %
                      </c:when>
                      <c:otherwise>
                        <c:if test="${not empty vestingSchedule.vestingScheduleDescription}">
                          <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${scheduleConstants.PERCENTAGE_SCALE}" value="${vestedAmount.amount}"/>${empty vestedAmount.amount ? '' : '%'}
                        </c:if>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </c:forEach>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </table>
    </div>
  </div>
</div>
