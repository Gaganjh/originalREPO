<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="serviceFeatureConstants" className="com.manulife.pension.service.contract.util.ServiceFeatureConstants" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_ELIGIBILITY}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="eligibilityText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_NO_ELIGIBILITY_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noMoneyTypesText"/>  
  
<content:contentBean
contentId="${contentConstants.FEE_INFORMATION_FOR_404A5_DISCLOSURE_PURPOSES_FOR_ELIGIBILITY}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="applyFeeInformation404a5DisclosurePurposeForEligibility" />



<script type="text/javascript">
var EEDEF_INDEX = -1;
var EEROT_INDEX = -1;

<c:forEach items="${planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria}" var="moneyTypeEligibilityCriterion" varStatus="moneyTypeEligibilityCriteriaStatus">
  <c:if test="${moneyTypeEligibilityCriterion.moneyTypeId == 'EEDEF'}">
    EEDEF_INDEX = ${moneyTypeEligibilityCriteriaStatus.index};
  </c:if>
  <c:if test="${moneyTypeEligibilityCriterion.moneyTypeId == 'EEROT'}">
    EEROT_INDEX = ${moneyTypeEligibilityCriteriaStatus.index};
</c:if>
</c:forEach>

function evaluateEligibilityComputationPeriod() {
  var hasHoursOfService = false;
  for (var i = 0; i < ${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)}; i++) {
    var serviceCreditingMethodSelectId = "#select_moneyTypeEligibilityCriteria_" + i + "_serviceCreditingMethod";
    if ($(serviceCreditingMethodSelectId).val() == "${planDataConstants.VESTING_SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE}") {
      hasHoursOfService = true;
      break;
    }
  }
  if (! hasHoursOfService) {
    $("input[name='planDataUi.planData.eligibilityComputationPeriodAfterTheInitialPeriod'][value='${planDataConstants.UNSPECIFIED_CODE}']").prop("checked", true);
    $("input[name='planDataUi.planData.eligibilityComputationPeriodAfterTheInitialPeriod']").prop("disabled", true);
  } else {
    $("input[name='planDataUi.planData.eligibilityComputationPeriodAfterTheInitialPeriod']").prop("disabled", false);
  }
}

function evaluateEligibilityCriteriaGrid() {
  var multipleEligibilityRulesForOneSingleMoneyType = $("input[name='planDataUi.planData.multipleEligibilityRulesForOneSingleMoneyType']:checked").val();

  enableImmediateEligibility = new Array(${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)});
  enableHoursOfService = new Array(${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)});
  enableServiceCreditingMethod = new Array(${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)});
  enableMinimumAge = new Array(${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)});
  enablePeriodOfService = new Array(${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)});
  enablePlanEntryFrequency = new Array(${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)});
  enablePartTimeEligibility = new Array(${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)});

  hasMultipleRules = false;
   
  if (multipleEligibilityRulesForOneSingleMoneyType == "${planDataConstants.YES_CODE}") {
    hasMultipleRules = true;
  }
  
  var aeValue = $("input[name='planDataUi.planData.automaticEnrollmentAllowed']:checked").val();

  for (var i = 0; i < ${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)}; i++) {
    enableImmediateEligibility[i] = ! hasMultipleRules;
    enableServiceCreditingMethod[i] = ! hasMultipleRules;
    enableMinimumAge[i] = ! hasMultipleRules;
    enableHoursOfService[i] = ! hasMultipleRules;
    enablePeriodOfService[i] = ! hasMultipleRules;
    enablePlanEntryFrequency[i] = ! hasMultipleRules;
    enablePartTimeEligibility[i] = ! hasMultipleRules;
    if (aeValue == "${planDataConstants.YES_CODE}") {
      if (i == EEDEF_INDEX) {
        enablePlanEntryFrequency[i] = true;
      }
    }
  }

  for (var i = 0; i < ${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)}; i++) {
    var immediateEligibilityCheckboxId = "#planDataUi_moneyTypeEligibilityCriteria_" + i + "_immediateEligibilityIndicator";
    var serviceCreditingMethodSelectId = "#select_moneyTypeEligibilityCriteria_" + i + "_serviceCreditingMethod";
    var minimumAgeSelectId = "#select_moneyTypeEligibilityCriteria_" + i + "_minimumAge";
    var minimumAgeInputId = "#planDataUi_moneyTypeEligibilityCriteria_" + i + "_minimumAge";
    var minimumAgeDivId = "#moneyTypeEligibilityCriteria_" + i + "_minimumAge_div";
    var hoursOfServiceSelectId = "#select_moneyTypeEligibilityCriteria_" + i + "_hoursOfService";
    var hoursOfServiceInputId = "#planDataUi_moneyTypeEligibilityCriteria_" + i + "_hoursOfService";
    var hoursOfServiceDivId = "#moneyTypeEligibilityCriteria_" + i + "_hoursOfService_div";
    var periodOfServiceSelectId = "#select_moneyTypeEligibilityCriteria_" + i + "_periodOfService";
    var periodOfServiceInputId = "#planDataUi_moneyTypeEligibilityCriteria_" + i + "_periodOfService";
    var periodOfServiceDivId = "#moneyTypeEligibilityCriteria_" + i + "_periodOfService_div";
    var periodOfServiceSelectUnitId = "#select_moneyTypeEligibilityCriteria_" + i + "_periodOfServiceUnit";
    var planEntryFrequencySelectId = "#select_moneyTypeEligibilityCriteria_" + i + "_planEntryFrequencyIndicator";
    var moneyTypeId = "#planDataUi_moneyTypeEligibilityCriteria_" + i + "_moneyTypeId";
    var partTimeEligibilityCheckboxId = "#planDataUi_moneyTypeEligibilityCriteria_" + i + "_partTimeEligibilityIndicator";

    if (! enableImmediateEligibility[i]) {
      $(immediateEligibilityCheckboxId).prop("checked",false);
      $(immediateEligibilityCheckboxId).prop("disabled", true);
    } else {
      $(immediateEligibilityCheckboxId).prop("disabled", false);
    }

    if ($(immediateEligibilityCheckboxId).is(":checked")) {
      enableServiceCreditingMethod[i] = false;
      enableMinimumAge[i] = false;
    }
    
    if (! enableServiceCreditingMethod[i]) {
      $(serviceCreditingMethodSelectId).val("${planDataConstants.UNSPECIFIED_CODE}");
      $(serviceCreditingMethodSelectId).prop('disabled', true);
    } else {
      $(serviceCreditingMethodSelectId).prop('disabled', false);
    }
    
    if ($(serviceCreditingMethodSelectId).val() == "${planDataConstants.VESTING_SERVICE_CREDIT_METHOD_ELAPSED_TIME}") {
        enableHoursOfService[i] = false;
        enablePeriodOfService[i] = true;
  	  enablePartTimeEligibility[i] = false;
      } else if ($(serviceCreditingMethodSelectId).val() == "${planDataConstants.UNSPECIFIED_CODE}") {
        enableHoursOfService[i] = false;
        enablePeriodOfService[i] = false;
        enablePartTimeEligibility[i] = false;
	  } else {
         enableHoursOfService[i] = true;
		 enablePeriodOfService[i] = true;
		 enablePartTimeEligibility[i] = true;
	 	}
    
    if (! enableMinimumAge[i]) {
      $(minimumAgeDivId).hide();
      $(minimumAgeSelectId).val("${planDataConstants.UNSPECIFIED_CODE}");
      $(minimumAgeInputId).val("");
      $(minimumAgeSelectId).prop('disabled', true);
    } else {
      $(minimumAgeSelectId).prop('disabled', false);
    }

    if (! enableHoursOfService[i]) {
      $(hoursOfServiceDivId).hide();
      $(hoursOfServiceSelectId).val("${planDataConstants.UNSPECIFIED_CODE}");
      $(hoursOfServiceInputId).val("");
      $(hoursOfServiceSelectId).prop('disabled', true);
    } else {
      $(hoursOfServiceSelectId).prop('disabled', false);
    }

    if (! enablePeriodOfService[i]) {
      $(periodOfServiceDivId).hide();
      $(periodOfServiceSelectId).val("${planDataConstants.UNSPECIFIED_CODE}");
      $(periodOfServiceInputId).val("");
      $(periodOfServiceSelectUnitId).val("");
      $(periodOfServiceSelectId).prop('disabled', true);
    } else {
      $(periodOfServiceSelectId).prop('disabled', false);
    }
  
    if (! enablePlanEntryFrequency[i]) {
      $(planEntryFrequencySelectId).val("${planDataConstants.UNSPECIFIED_CODE}");
      $(planEntryFrequencySelectId).prop("disabled", true);
    } else {
      $(planEntryFrequencySelectId).prop("disabled", false);
    }
    
    <c:if test="${planDataForm.planDataUi.planData.planType != planDataConstants.PLAN_TYPE_457B &&  planDataForm.planDataUi.planData.productId != planDataConstants.PLAN_TYPE_RA457}">
		 if (! enablePartTimeEligibility[i]) {
	         $(partTimeEligibilityCheckboxId).prop("checked", false);
	         $(partTimeEligibilityCheckboxId).prop("disabled", true);
	     } else {
	     	  if( i == EEDEF_INDEX || i == EEROT_INDEX){
	               $(partTimeEligibilityCheckboxId).prop("checked", true);
	               $(partTimeEligibilityCheckboxId).prop("disabled", true);
	           }else{
	           	$(partTimeEligibilityCheckboxId).prop("disabled" ,  false);
	           }
	     }
	 </c:if>
  	 
    if ($(moneyTypeId).val() == "${serviceFeatureConstants.EMPLOYEE_ROTH_CONTRIBUTION}") {
<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
                $(immediateEligibilityCheckboxId).prop("disabled", true);
                $(serviceCreditingMethodSelectId).prop('disabled', true);
                $(minimumAgeSelectId).prop('disabled', true);
                $(minimumAgeInputId).prop('disabled', true);
                $(hoursOfServiceSelectId).prop('disabled', true);
                $(hoursOfServiceInputId).prop('disabled', true);
                $(periodOfServiceSelectId).prop('disabled', true);
                $(periodOfServiceInputId).prop('disabled', true);
                $(periodOfServiceSelectUnitId).prop('disabled', true);
                $(planEntryFrequencySelectId).prop("disabled", true);
                $(partTimeEligibilityCheckboxId).prop("disabled", true);
                continue;
</c:if>
     }
  }
  
  evaluateEligibilityComputationPeriod();
}

function onAutomaticEnrollmentAllowedChanged() {

  var enable = true;
  var automaticEnrollmentAllowed = $("input[name='planDataUi.planData.automaticEnrollmentAllowed']:checked").val();
  if (automaticEnrollmentAllowed != "${planDataConstants.YES_CODE}") {
    enable = false;
  }

  var deferralPercentageForAutomaticEnrollmentInputId = "#planDataUi_deferralPercentageForAutomaticEnrollment";
  
  if (enable == false) {
    $("input[name='planDataUi.planData.isNinetyDayOrShorterWithdrawalElectionOffered'][value='${planDataConstants.UNSPECIFIED_CODE}']").prop("checked", "checked");
    $("input[name='planDataUi.planData.isNinetyDayOrShorterWithdrawalElectionOffered']").prop("disabled", "disabled");
    $(deferralPercentageForAutomaticEnrollmentInputId).val("");
    $(deferralPercentageForAutomaticEnrollmentInputId).prop("disabled", "disabled");
  } else {
    $("input[name='planDataUi.planData.isNinetyDayOrShorterWithdrawalElectionOffered']").prop("disabled");
    $(deferralPercentageForAutomaticEnrollmentInputId).prop("disabled");
  }
  onNinetyDayOrShorterWithdrawalElectionChanged();
  showOrHideAutomaticEnrollmentEffectiveDate();
  evaluateEligibilityCriteriaGrid();
}

function onNinetyDayOrShorterWithdrawalElectionChanged() {
  var checked = false;

  <c:if test="${planDataForm.planDataUi.planData.isNinetyDayOrShorterWithdrawalElectionOffered == planDataConstants.YES_CODE}">
  checked = true;
  </c:if>

  <c:if test="${planDataForm.editMode or planDataForm.confirmMode}">                           
  checked = $("input[name='planDataUi.planData.isNinetyDayOrShorterWithdrawalElectionOffered'][value='${planDataConstants.YES_CODE}']").is(":checked");
  </c:if>
  
  if (checked) {
    $("#withdrawalReasonNE").show();
  } else {
    $("#withdrawalReasonNE").hide();
  }
}

function showOrHideAutomaticEnrollmentEffectiveDate() {
  var enable = true;
  var automaticEnrollmentAllowed = $("input[name='planDataUi.planData.automaticEnrollmentAllowed']:checked").val();
  if (automaticEnrollmentAllowed != "${planDataConstants.YES_CODE}") {
    enable = false;
  }
  var aeEffectiveDateSpanId = "#planDataUi_automaticEnrollmentEffectiveDate_span";
  
  if (enable == false) {
    $(aeEffectiveDateSpanId).hide();
  } else {
    $(aeEffectiveDateSpanId).show();
  }
}

function updateEEROT(field, moneyType, outputField) {
<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
	if(moneyType == 'EEDEF') {
	    var inputIdEEROT = "#planDataUi_moneyTypeEligibilityCriteria_" + EEROT_INDEX + outputField;
	    $(inputIdEEROT).val(field.value);
	    $(inputIdEEROT).prop('disabled', true);
	}
</c:if>
}

function updateEEROTPeriodOfSerivce(periodOfServiceInput, periodOfServiceSelection, moneyType) {
<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
	if(moneyType == 'EEDEF') {
	    var inputIdEEROT = "#planDataUi_moneyTypeEligibilityCriteria_" + EEROT_INDEX + "_periodOfService";
	    var selectIdEEROT = "#select_moneyTypeEligibilityCriteria_" + EEROT_INDEX + "_periodOfServiceUnit";
	    $(inputIdEEROT).val(periodOfServiceInput.value);
	    $(inputIdEEROT).prop('disabled', true);
	    $(selectIdEEROT).val(periodOfServiceSelection.value);
	    $(selectIdEEROT).prop('disabled', true);
	}
</c:if>
}

function updateValue(inputValue, divId, inputId) {
	   if (inputValue == "${planDataConstants.UNSPECIFIED_CODE}") {
		   inputValue = "";
       }
       if (inputValue == "OTHER") {
           $(divId).show();
           $(inputId).val("");
       } else {
          $(divId).hide();
          $(inputId).val(inputValue);
       }
}

function resetPlanEntryFrequency() {
<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
	   var planEntryFrequencyIndicatorEEDEF = "#select_moneyTypeEligibilityCriteria_"+ EEDEF_INDEX +"_planEntryFrequencyIndicator";
	   var planEntryFrequencyIndicatorEEROT = "#select_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_planEntryFrequencyIndicator";
	   $(planEntryFrequencyIndicatorEEROT).val($(planEntryFrequencyIndicatorEEDEF).val());
</c:if>
}    

function updateValues(inputValue, divId, inputId, selectUnitId) {
	if (inputValue == "OTHER") {
        $(divId).show();
        $(inputId).val("");
        $(selectUnitId).val("");
      } else if (inputValue == "${planDataConstants.UNSPECIFIED_CODE}") {
        $(divId).hide();
        $(inputId).val("");
        $(selectUnitId).val("");				        
      } else {
        $(divId).hide();
        $(inputId).val(inputValue);
        if (inputValue.length > 0) {
            var unit = inputValue.substring(inputValue.length - 1);
            var pos = inputValue.substring(0, inputValue.length - 1);
            $(inputId).val(pos);
            $(selectUnitId).val(unit);
        }
     }
}

$(document).ready(function() {

  <c:if test="${planDataForm.confirmMode}">
  showOrHideAutomaticEnrollmentEffectiveDate();
  </c:if>
                             
  <c:if test="${planDataForm.editMode}">                           
    evaluateEligibilityCriteriaGrid();
    onAutomaticEnrollmentAllowedChanged();
    var serviceCreditingMethodSelectId = null;
   
    <c:forEach items="${planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria}" var="moneyTypeEligibilityCriterion" varStatus="moneyTypeEligibilityCriteriaStatus">
      serviceCreditingMethodSelectId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_serviceCreditingMethod";
      $(serviceCreditingMethodSelectId).on("change",function() {
    	setDirtyFlag();  
<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
<c:if test="${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId ==serviceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL}">

          var serviceCreditingMethodEEROT = "#select_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_serviceCreditingMethod";
          var serviceCreditingMethodEEDEF = "#select_moneyTypeEligibilityCriteria_"+ EEDEF_INDEX +"_serviceCreditingMethod";
          $(serviceCreditingMethodEEROT).val($(serviceCreditingMethodEEDEF).val());
</c:if>
</c:if>
        evaluateEligibilityCriteriaGrid();
      });
    </c:forEach>
  </c:if>

  onNinetyDayOrShorterWithdrawalElectionChanged();
});

</script>

<div id="eligibilityAndParticipationTabDivId" class="borderedDataBox">
  <div class="subhead">
    <c:if test="${not param.printFriendly}">
      <div class="expandCollapseIcons" id="eligibilityAndParticipationShowIconId" onclick="expandDataDiv('eligibilityAndParticipation');">
        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
      </div>
      <div class="expandCollapseIcons" id="eligibilityAndParticipationHideIconId" onclick="collapseDataDiv('eligibilityAndParticipation');">
        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
      </div>
    </c:if>
    <div class="sectionTitle">
      <content:getAttribute beanName="eligibilityText" attribute="text"/>
    </div>
    <div class="sectionHighlightIcon" id="eligibilityAndParticipationSectionHighlightIconId">
      <ps:sectionHilight name="${uiConstants.SECTION_ELIGIBILITY_AND_PARTICIPATION}" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
    </div>
    <div class="endDataRowAndClearFloats"></div>
  </div>
  <div id="eligibilityAndParticipationDataDivId">
    <div class="evenDataRow">  
        <table class="dataTable">   
            <tr>
               <td class="eligibilityAndParticipationExtendedLabelColumn"> 
               <ps:fieldHilight name="planDataUi.planData.multipleEligibilityRulesForOneSingleMoneyType" singleDisplay="true" className="errorIcon" displayToolTip="true"/>                    
                    Does the plan have two or more different eligibility rules for any single money type?
                </td>
                <td class="dataColumn">
                    <c:choose>
                      <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">                           
<form:radiobutton disabled="${disableFields}" onclick="evaluateEligibilityCriteriaGrid()" path="planDataUi.planData.multipleEligibilityRulesForOneSingleMoneyType" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="evaluateEligibilityCriteriaGrid();resetPlanEntryFrequency();" path="planDataUi.planData.multipleEligibilityRulesForOneSingleMoneyType" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



<form:radiobutton disabled="${disableFields}" onclick="evaluateEligibilityCriteriaGrid();resetPlanEntryFrequency();" path="planDataUi.planData.multipleEligibilityRulesForOneSingleMoneyType" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}



                      </c:when>
                      <c:otherwise>
                        ${planDataForm.planDataUi.multipleEligibilityRulesForOneSingleMoneyTypeDisplay}
                      </c:otherwise>
                    </c:choose>
                </td>
            </tr> 
        </table>
    </div>  
    
<!-- Eligibility Requirements  -->    
    <div class="subsubhead">Eligibility Requirements</div> 
    <div class="evenDataRow">
    <table class="dataTable" style="border-bottom-width: 1px;"> 
        <tbody> 
        <c:if test="${planDataForm.planDataUi.planData.planType !=planDataConstants.PLAN_TYPE_457B &&  planDataForm.planDataUi.planData.productId != planDataConstants.PlAN_TYPE_RA457}">
          <tr> 
            <td width="125" valign="top" style="border-left-width: 0; class="dataColumn"><strong>Money Type</strong></td> 
            <td width="61" align="center" valign="top" class="dataColumn"><strong>Immediate<br/> 
              Eligibility</strong></td> 
            <td width="100" valign="top" class="dataColumn"><strong>Service Crediting<br/> 
              Method</strong></td> 
            <td width="72" align="center" valign="top" class="dataColumn"><strong>Minimum Age</strong></td> 
            <td width="78" align="center" valign="top" class="dataColumn"><strong>Hours of Service</strong></td> 
            <td width="90" valign="top" class="dataColumn"><strong>Period of Service</strong></td> 
            <td width="71" valign="top" class="dataColumn"><strong>Plan Entry Frequency</strong></td>
             <td align="center" valign="top" class="dataColumn"><strong>Part Time Eligibility</strong></td> 
          </tr>
          </c:if>
          <c:if test="${planDataForm.planDataUi.planData.planType ==planDataConstants.PLAN_TYPE_457B || planDataForm.planDataUi.planData.productId == planDataConstants.PLAN_TYPE_RA457}">
          <tr> 
            <td width="125" valign="top" style="border-left-width: 0; class="dataColumn"><strong>Money Type</strong></td> 
            <td width="61" align="center" valign="top" class="dataColumn"><strong>Immediate<br/> 
              Eligibility</strong></td> 
            <td width="115" valign="top" class="dataColumn"><strong>Service Crediting<br/> 
              Method</strong></td> 
            <td width="88" align="center" valign="top" class="dataColumn"><strong>Minimum Age</strong></td> 
            <td width="88" align="center" valign="top" class="dataColumn"><strong>Hours of Service</strong></td> 
            <td width="120" valign="top" class="dataColumn"><strong>Period of Service</strong></td> 
            <td valign="top" class="dataColumn"><strong>Plan Entry Frequency</strong></td> 
          </tr>
          </c:if>
        <c:choose>
          <c:when test="${empty planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria}">
            <tr class="evenDataRow">
              <td class="textData" colspan="7" style="border-left-width: 0; border-right-width: 0;">
                <content:getAttribute beanName="noMoneyTypesText" attribute="text"/>
              </td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach items="${planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria}" var="moneyTypeEligibilityCriterion" varStatus="moneyTypeEligibilityCriteriaStatus">
              <c:set var="disableEEROT" value="false" scope="page"/>
<form:hidden path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].moneyTypeEligibilityCriterion.moneyTypeId" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_moneyTypeId"/>

<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
<c:if test="${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId ==serviceFeatureConstants.EMPLOYEE_ROTH_CONTRIBUTION}">

                    <c:set var="disableEEROT" value="true" scope="page"/>
</c:if>
</c:if>
              <tr class="${(moneyTypeEligibilityCriteriaStatus.count % 2 == 0) ? 'evenDataRow' : 'oddDataRow'}">
                <td class="dataColumn" style="border-left-width: 0;" title="${moneyTypeEligibilityCriterion.contractMoneyTypeLongName}">
                  <ps:fieldHilight name="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].contractMoneyTypeShortName" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
                  ${moneyTypeEligibilityCriterion.contractMoneyTypeShortName}
                </td>
		<!-- immediate eligibility -->
                <td class="dataColumn" align="center">
                <ps:fieldHilight name="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].immediateEligibilityIndicator" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
                    <script type="text/javascript">
                    $(document).ready(function() {
                        immediateEligibilityId = "#planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_immediateEligibilityIndicator";
                        <c:if test="${disableFields or disableEEROT}">
                        $(immediateEligibilityId).prop("disabled", true); 
                        </c:if>
                        

                         <!-- disable minimum age, hours of service, period of service when immediate eligibility is checked -->
                        $(immediateEligibilityId).on("click",function() {
<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
<c:if test="${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId ==serviceFeatureConstants.EMPLOYEE_ROTH_CONTRIBUTION}">

                            <c:set var="disableEEROT" value="true" scope="page"/>
</c:if>
</c:if>
<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
<c:if test="${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId ==serviceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL}">

                     	          var immediateEligibilityIdEEROT = "#planDataUi_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_immediateEligibilityIndicator";
                     	          var immediateEligibilityIdEEDEF = $("input[name='planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].immediateEligibilityIndicator']:checked").val();
                                  if (immediateEligibilityIdEEDEF == "on") {
                                 	 $(immediateEligibilityIdEEROT).prop("checked", true);
                                  } else {
                                 	 $(immediateEligibilityIdEEROT).prop("checked", false);
                                  }
</c:if>
</c:if>
                            evaluateEligibilityCriteriaGrid();
                        });
                    });
                    </script>
<form:checkbox path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].immediateEligibilityIndicator" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_immediateEligibilityIndicator" onclick="setDirtyFlag();" disabled="${disableFields}"/>



	              </c:when>
	              <c:otherwise>
<form:checkbox path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].immediateEligibilityIndicator" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_immediateEligibilityIndicator" disabled="true"/>


	              </c:otherwise>
	            </c:choose>
                </td>
		<!-- service crediting method -->
                <td class="dataColumn">
                <ps:fieldHilight name="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].serviceCreditingMethod" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
 	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
	                   <script type="text/javascript">
                    $(document).ready(function() {
                    	servicecreditingId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_serviceCreditingMethod";
                        <c:if test="${disableFields or disableEEROT}">
                        $(servicecreditingId).prop("disabled", true); 
                        </c:if>
                    });
                    </script>
 <form:select path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].serviceCreditingMethod" style="width: 115px" id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_serviceCreditingMethod" disabled="${disableFields}">



	                  <form:options items="${eligibilityCreditMethods}" itemValue="code" itemLabel="description"/>  
</form:select>
	              </c:when>
	              <c:otherwise>
          			${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].serviceCreditingMethodDisplay}	              
	              </c:otherwise>
		        </c:choose>
                </td>
		<!-- minimum age -->
                <td class="dataColumn" align="center">
 	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
                    <script type="text/javascript">
                    $(document).ready(function() {
                      // show/suppress the input field.
                      var divId = "#moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_minimumAge_div";
                      var inputId = "#planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_minimumAge";
                      var selectId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_minimumAge";

                      <c:if test="${disableFields or disableEEROT}">
                      $(selectId).prop("disabled", true); 
                      </c:if>
                      
                      <c:if test="${not disableFields or  not disableEEROT }">
                      $(selectId).on("change",function () {
				        var value = $(selectId).val();
				        updateValue(value, divId, inputId);
				      
<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
<c:if test="${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId ==serviceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL}">

  	                     var minimumAgeEEROT = "#select_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_minimumAge";
  	                     $(minimumAgeEEROT).val($(selectId).val());
  	                     var valueEEROT = $(minimumAgeEEROT).val();
  	                     var divIdEEROT = "#moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_minimumAge_div";
  	                     var inputIdEEROT = "#planDataUi_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_minimumAge";
  	                     updateValue(valueEEROT, divIdEEROT, inputIdEEROT);
</c:if>
</c:if>
                      });
                      </c:if>

                      var value = String.prototype.trim.call( $(inputId).val() == null ? "" : $(inputId).val() );
                      if (value == "") {
                        value = "${planDataConstants.UNSPECIFIED_CODE}"; // Unspecified.
                      }
                      
                      if (<c:forEach var="minimumAge" items="${eligibilityMinimumAges}" varStatus="counter">
   	            		    <c:if test="${minimumAge.code != 'OTHER' and minimumAge.code != planDataConstants.UNSPECIFIED_CODE}">
  	            		    value == "${minimumAge.code}" ||
  	            		    </c:if>
	            		  </c:forEach>
	            		  value == "${planDataConstants.UNSPECIFIED_CODE}"
	            	      ) {
                        $(divId).hide();
                        $(selectId).val(value);
                      } else {
                        $(selectId).val("OTHER");
                        $(divId).show();
                      }
                    });
                    </script>
                    <ps:fieldHilight name="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].minimumAge" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
	                <select onchange="setDirtyFlag();"
	                        style="width: 88px"
	                        id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_minimumAge">
	            		<c:forEach var="minimumAge" items="${eligibilityMinimumAges}">
	            		    <option value="${minimumAge.code}">
	            		      <c:choose>
  	            		        <c:when test="${fn:endsWith(minimumAge.description, '.5')}">
  	            		          ${fn:substringBefore(minimumAge.description, '.5')}&#189
	            		        </c:when>
	            		        <c:otherwise>
	            		          ${minimumAge.description}
	            		        </c:otherwise>
	            		      </c:choose>
	            		    </option>
	            		</c:forEach>
	                </select>
	                <div id="moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_minimumAge_div"
	                     style="display: none">
<form:input path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].minimumAge" disabled="${disableFields}" onblur="validateEligibiliyCriteriaMinimumAge(this); updateEEROT(this, '${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId}', '_minimumAge');" onchange="setDirtyFlag();" size="5" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_minimumAge"/>





	                </div>
	              </c:when>
	              <c:otherwise>
        		      <c:choose>
        		        <c:when test="${fn:endsWith(planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].minimumAge, '.5')}">
        		          ${fn:substringBefore(planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].minimumAge, '.5')}&#189
        		        </c:when>
        		        <c:otherwise>
        		          ${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].minimumAge}
        		        </c:otherwise>
        		      </c:choose>
	              </c:otherwise>
		        </c:choose>
                </td>
		<!-- hours of service -->
                <td class="dataColumn" align="center">
 	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
                    <script type="text/javascript">
                    $(document).ready(function() {
                      // show/suppress the input field.
                      var divId = "#moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_hoursOfService_div";
                      var inputId = "#planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_hoursOfService";
                      var selectId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_hoursOfService";

                      <c:if test="${disableFields or disableEEROT}">
                      $(selectId).prop("disabled", true); 
                      </c:if>
                      
                      <c:if test="${not disableFields or  not disableEEROT}">
                      $(selectId).on("change",function () {
				        var value = $(selectId).val();
				        updateValue(value, divId, inputId);

<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
<c:if test="${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId ==serviceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL}">

	  	                     var hoursOfServiceEEROT = "#select_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_hoursOfService";
	  	                     $(hoursOfServiceEEROT).val($(selectId).val());
	  	                     var valueEEROT = $(hoursOfServiceEEROT).val();
	  	                     var divIdEEROT = "#moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_hoursOfService_div";
	  	                     var inputIdEEROT = "#planDataUi_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_hoursOfService";
	 				         updateValue(valueEEROT, divIdEEROT, inputIdEEROT);
</c:if>
</c:if>
                     
				        
				      });
                      </c:if>

                      var value = String.prototype.trim.call( $(inputId).val() == null ? "" : $(inputId).val() );
                      if (value == "") {
                        value = "${planDataConstants.UNSPECIFIED_CODE}"; // Unspecified.
                      }
                      if (<c:forEach var="hoursOfService" items="${eligibilityHoursOfServices}">
   	            		    <c:if test="${hoursOfService.code != 'OTHER' and hoursOfService.code != planDataConstants.UNSPECIFIED_CODE}">
  	            		    value == "${hoursOfService.code}" ||
  	            		    </c:if>
	            		  </c:forEach>
	            		  value == "${planDataConstants.UNSPECIFIED_CODE}"
                          ) {
                        $(divId).hide();
                        $(selectId).val(value);
                      } else {
                        $(selectId).val("OTHER");
                        $(divId).show();
                      }
                    });
                    </script>
                    <ps:fieldHilight name="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].hoursOfService" singleDisplay="true" displayToolTip="true" className="errorIcon"/>                   
	                <select onchange="setDirtyFlag();"
                            style="width: 88px"
	                        id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_hoursOfService">
	                  	<c:forEach var="hoursOfService" items="${eligibilityHoursOfServices}">
	            		    <option value=${hoursOfService.code}>${hoursOfService.description}</option>
	            		</c:forEach>
	                </select>
	                <div id="moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_hoursOfService_div"
	                     style="display: none">
<form:input path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].hoursOfService" disabled="${disableFields}" onblur="validateEligibiliyCriteriaHoursOfService(this); updateEEROT(this, '${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId}', '_hoursOfService');" onchange="setDirtyFlag();" size="5" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_hoursOfService"/>





	                </div>
	              </c:when>
	              <c:otherwise>
	                ${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].hoursOfService}
	              </c:otherwise>
		        </c:choose>
                </td>
		<!-- period of service  -->
                <td class="dataColumn">
 	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
                    <script type="text/javascript">
                    $(document).ready(function() {
                      // show/suppress the input field.
                      var divId = "#moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfService_div";
                      var inputId = "#planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfService";
                      var selectId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfService";
                      var selectUnitId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfServiceUnit";

                      <c:if test="${disableFields or disableEEROT}">
                      $(selectId).prop("disabled", true); 
                      </c:if>
                      
                      <c:if test="${not disableFields or  not disableEEROT}">
                      $(selectId).on("change",function () {
				        var value = $(selectId).val();
				        updateValues(value, divId, inputId, selectUnitId);

<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
<c:if test="${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId ==serviceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL}">

		  	                     var periodOfServiceEEROT = "#select_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_periodOfService";
		  	                     $(periodOfServiceEEROT).val($(selectId).val());
		  	                     var valueEEROT = $(periodOfServiceEEROT).val();
		  	                     var divIdEEROT = "#moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_periodOfService_div";
		  	                     var inputIdEEROT = "#planDataUi_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_periodOfService";
		  	                     var selectUnitIdEEROT = "#select_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_periodOfServiceUnit";
		  	                     updateValues(valueEEROT, divIdEEROT, inputIdEEROT, selectUnitIdEEROT);
</c:if>
</c:if>
				      });
                      </c:if>

					  if ($(inputId).val().length > 0) {
	                      var value = String.prototype.trim.call( $(inputId).val() == null ? "" : $(inputId).val() );
                          if (value == "") {
                            value = "${planDataConstants.UNSPECIFIED_CODE}"; // Unspecified.
                          } else {
	                        value += $(selectUnitId).val();
	                      }
	                      if (<c:forEach var="periodOfService" items="${eligibilityPeriodOfServices}">
   	            		    <c:if test="${periodOfService.code != 'OTHER' and periodOfService.code != planDataConstants.UNSPECIFIED_CODE}">
  	            		    value == "${periodOfService.code}" ||
  	            		    </c:if>
	            		  </c:forEach>
	            		  value == "${planDataConstants.UNSPECIFIED_CODE}"
	            		  ) {
	                        $(divId).hide();
	                        $(selectId).val(value);
	                      } else {
	                        $(selectId).val("OTHER");
	                        $(divId).show();
	                      }
	                  }
                    });
                    </script>
                    <ps:fieldHilight name="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].periodOfService" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
	                <select onchange="setDirtyFlag();"
	                        style="width: 115px"
	                        id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfService">
	                    <c:forEach var="periodOfService" items="${eligibilityPeriodOfServices}">
	            		    <option value=${periodOfService.code}>${periodOfService.description}</option>
	            		</c:forEach>
	                </select>
	                <div id="moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfService_div"
	                     style="display: none">
<form:input path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].periodOfService" disabled="${disableFields}" onblur="validateEligibiliyCriteriaPeriodOfService(this, $('#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfServiceUnit').val()); updateEEROT(this, '${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId}', '_periodOfService');" onchange="setDirtyFlag();" size="3" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfService"/>





 <form:select path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].periodOfServiceUnit" onchange="setDirtyFlag();validateEligibiliyCriteriaPeriodOfService(document.getElementById('planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfService'), $('#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfServiceUnit').val()); updateEEROTPeriodOfSerivce(document.getElementById('planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfService'), this, '${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId}');" disabled="${disableFields}" style="width: 60px" id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_periodOfServiceUnit">





	                      <form:option value="">Select</form:option>	                      
		                  <form:options items="${eligibilityPeriodOfServiceUnits}" itemValue="code" itemLabel="description"/>
</form:select>
	                </div>
	              </c:when>
	              <c:otherwise>
	                ${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].periodOfServiceAndUnitDisplay}
	              </c:otherwise>
		        </c:choose>
                </td>
		<!-- plan entry frequency -->
                <td class="dataColumn">
                <ps:fieldHilight name="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].planEntryFrequencyIndicator" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
                <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
	                <script type="text/javascript">
                    $(document).ready(function() {
                    	 var selectId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_planEntryFrequencyIndicator";
                    	<c:if test="${disableEEROT}">
                        $(selectId).prop("disabled", true); 
                        </c:if>

                        <c:if test="${not disableEEROT}">
                        $(selectId).on("change",function () {
<c:if test="${planDataForm.planDataUi.hasBothEEDEFAndEEROTMoneyTypes ==true}">
<c:if test="${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].moneyTypeEligibilityCriterion.moneyTypeId ==serviceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL}">

     		  	                  var multipleEligibilityRulesForOneSingleMoneyType = $("input[name='planDataUi.planData.multipleEligibilityRulesForOneSingleMoneyType']:checked").val();
     		  	                  if (multipleEligibilityRulesForOneSingleMoneyType == "${planDataConstants.YES_CODE}") {
     		  	                      hasMultipleRules = true;
     		  	                  }
     		  	                  if(!hasMultipleRules) {
        		  	                 var planEntryFrequencyIndicatorEEROT = "#select_moneyTypeEligibilityCriteria_"+ EEROT_INDEX +"_planEntryFrequencyIndicator";
     		  	                     $(planEntryFrequencyIndicatorEEROT).val($(selectId).val()); 
     		  	                  }
</c:if>
</c:if>
                            
                        });
                        </c:if>
                    });
                    </script>
 <form:select path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].planEntryFrequencyIndicator" id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_planEntryFrequencyIndicator" disabled="${disableFields}" onchange="setDirtyFlag();">



	                  <form:options items="${eligibilityPlanEntryFrequencies}" itemValue="code" itemLabel="description"/>  
</form:select>
	              </c:when>
	              <c:otherwise>
	                ${planDataForm.planDataUi.moneyTypeEligibilityCriteria[moneyTypeEligibilityCriteriaStatus.index].planEntryFrequencyIndicatorDisplay}
	              </c:otherwise>
		        </c:choose>
                </td>
		<!-- Part Time Eligibility -->
		 <c:if test="${planDataForm.planDataUi.planData.planType !=planDataConstants.PLAN_TYPE_457B &&  planDataForm.planDataUi.planData.productId != planDataConstants.PLAN_TYPE_RA457}">
                <td class="dataColumn" align="center">
                <ps:fieldHilight name="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].partTimeEligibilityIndicator" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
                    <script type="text/javascript">
                    $(document).ready(function() {
                    	partTimeEligibilityId = "#planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_partTimeEligibilityIndicator";
                        <c:if test="${disableFields or disableEEROT}">
                        $(partTimeEligibilityId).prop("disabled", true); 
                        </c:if>
                    });
                    </script>
<form:checkbox path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].partTimeEligibilityIndicator" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_partTimeEligibilityIndicator" onclick="setDirtyFlag();" disabled="${disableFields}"/>
	              </c:when>
	              <c:otherwise>
<form:checkbox path="planDataUi.moneyTypeEligibilityCriteria[${moneyTypeEligibilityCriteriaStatus.index}].partTimeEligibilityIndicator" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriteriaStatus.index}_partTimeEligibilityIndicator" onclick="setDirtyFlag();" disabled="true"/>
	              </c:otherwise>
	            </c:choose>
                </td>
                </c:if>
			  </tr>
			</c:forEach>
		  </c:otherwise>
		</c:choose>
        </tbody> 
      </table> 

     
<!-- first plan entry date -->
    <div class="evenDataRow">
      <table class="dataTable" width="100%">
        <tr>
          <td class="eligibilityAndParticipationLabelColumn" style="border-top:1px solid #CCCCCC"">
            <ps:fieldHilight name="planDataUi.firstPlanEntryDateString" singleDisplay="true" className="errorIcon" displayToolTip="true"/>          
            First plan entry date
          </td>
          <td class="dataColumn" style="border-top:1px solid #CCCCCC">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.firstPlanEntryDateString" disabled="${disableFields}" maxlength="5" onblur="validatePlanEntryDate(this)" onchange="setDirtyFlag();" size="5"/>





              </c:when>
              <c:otherwise>
                ${planDataForm.planDataUi.planData.firstPlanEntryDate.data}
              </c:otherwise>
            </c:choose>
            <c:if test="${planDataForm.editMode or planDataForm.confirmMode or not empty planDataForm.planDataUi.planData.firstPlanEntryDate}">
              (mm&frasl;dd)
            </c:if>
          </td>
        </tr>
      </table>
    </div>
    
<%-- eligible employee plan entry date --%>

	<div class = "evenDataRow">
	  <table class="dataTable">
		<tr valign="top">
		  <td class = "eligibilityAndParticipationLabelColumn">
		  <ps:fieldHilight name="planDataUi.planData.planEntryDateBasis" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
			An eligible employee who has satisfied the eligibility requirements<br>
			will enter the plan on the plan entry date that... 				
		  </td>
		  <td class="dataColumn">
			<c:choose>
			  <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.planEntryDateBasis" value="${planDataConstants.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_COINCIDENT_NEXT_CODE}"/>${planDataConstants.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_COINCIDENT_NEXT_STRING}<br>


<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.planEntryDateBasis" value="${planDataConstants.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_NEXT_CODE}"/>${planDataConstants.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_NEXT_STRING}<br>


<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.planEntryDateBasis" value="${planDataConstants.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_NEAREST_CODE}"/>${planDataConstants.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_NEAREST_STRING}


			    <br>
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.planEntryDateBasis" value="${planDataConstants.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_OTHER_CODE}"/>${planDataConstants.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_OTHER_STRING}


			    <br>			    
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.planEntryDateBasis" value="${planDataConstants.ELIGIBLE_EMPLOYEE_PLAN_ENTRY_DATE_UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}


			  </c:when>
			  <c:otherwise>
			  	${planDataForm.planDataUi.planEntryDateBasisDisplay }
			  </c:otherwise>			
			</c:choose>
		  </td>		
		</tr>
	  </table>	
    </div>

<%-- eligibility computation period --%>
    <div class="evenDataRow">
      <table class="dataTable">
        <tr valign="top">
          <td class="eligibilityAndParticipationLabelColumn">
          <ps:fieldHilight name="planDataUi.planData.eligibilityComputationPeriodAfterTheInitialPeriod" singleDisplay="true" className="errorIcon" displayToolTip="true"/>                    
          The eligibility computation period after the initial eligibility<br>computation period shall...
          </td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.eligibilityComputationPeriodAfterTheInitialPeriod" value="${planDataConstants.ELIGIBILITY_COMPUTATION_PERIOD_SHIFT_TO_PLAN_YEAR_CODE}"/>${planDataConstants.ELIGIBILITY_COMPUTATION_PERIOD_SHIFT_TO_PLAN_YEAR_STRING}


                <br>                    
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.eligibilityComputationPeriodAfterTheInitialPeriod" value="${planDataConstants.ELIGIBILITY_COMPUTATION_PERIOD_BASED_ON_ANNIVERSARY_OF_EMPLOYEE_FIRST_COMPLETES_HOS_CODE}"/>Be based on each anniversary of the date the Employee<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;first completes an Hour of Service


                <br>                    
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.eligibilityComputationPeriodAfterTheInitialPeriod" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}


               </c:when>
              <c:otherwise>              
                ${planDataForm.planDataUi.eligibilityComputationPeriodAfterTheInitialPeriodDisplay}
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </div>

<%-- rollovers delayed until eligibility reqt met --%>    
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="eligibilityAndParticipationLabelColumn">
          Must rollovers be delayed until eligibility requirements are met?
          </td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.rolloversDelayedUntilEligibilityReqtMet" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}


<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.rolloversDelayedUntilEligibilityReqtMet" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}


<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.rolloversDelayedUntilEligibilityReqtMet" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}


              </c:when>
              <c:otherwise>
                ${planDataForm.planDataUi.rolloversDelayedUntilEligibilityReqtMetDisplay}
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </div>
   </div>
   
<!-- excluded employees -->
    <div class="subsubhead">Excluded Employees</div> 
    <div class="evenDataRow"> 
      <table class="dataTable"> 
        <tbody> 
          <tr> 
            <td width="125" valign="top" style="border-left-width: 0; class="dataColumn"><strong>Money Type</strong></td> 
            <td width="85" valign="top" align="center" class="dataColumn"><strong>Union</strong></td> 
            <td width="85" valign="top" align="center" class="dataColumn"><strong>Nonresident Aliens</strong></td> 
            <td width="85" valign="top" align="center" class="dataColumn"><strong>Highly Compensated</strong></td> 
            <td width="85" valign="top" align="center" class="dataColumn"><strong>Leased</strong></td> 
            <td width="85" valign="top" align="center" class="dataColumn"><strong>Other</strong></td>
            <td width="140" valign="top" class="dataColumn">&nbsp;</td>
          </tr> 
        <c:choose>
          <c:when test="${empty planDataForm.planDataUi.planData.moneyTypeExcludedEmployees}">
            <tr class="evenDataRow">
              <td class="textData" colspan="6" style="border-left-width: 0; border-right-width: 0;">
                 <content:getAttribute beanName="noMoneyTypesText" attribute="text"/>
              </td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach items="${planDataForm.planDataUi.planData.moneyTypeExcludedEmployees}" var="moneyTypeExcludedEmployee" varStatus="moneyTypeExcludedEmployeesStatus">
              <tr class="${(moneyTypeExcludedEmployeesStatus.count % 2 == 0) ? 'evenDataRow' : 'oddDataRow'}">
                <td class="dataColumn" style="border-left-width: 0;" title="${moneyTypeExcludedEmployee.contractMoneyTypeLongName}">
                  <ps:fieldHilight name="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].moneyTypeId" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
                  ${moneyTypeExcludedEmployee.contractMoneyTypeShortName}
                </td>
           <!-- union  -->
                <td align="center" class="dataColumn">
	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:checkbox path="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].union" onclick="setDirtyFlag();" disabled="${disableFields}"/>


	              </c:when>
	              <c:otherwise>
<form:checkbox path="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].union" disabled="true"/>

	              </c:otherwise>
	            </c:choose>
                </td>
           <!-- non resident aliens -->
                <td align="center" class="dataColumn">
	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:checkbox path="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].nonResidentAliens" onclick="setDirtyFlag();" disabled="${disableFields}"/>


	              </c:when>
	              <c:otherwise>
<form:checkbox path="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].nonResidentAliens" disabled="true"/>

	              </c:otherwise>
	            </c:choose>
                </td>
           <!-- highly compensated -->
                <td align="center" class="dataColumn">
	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:checkbox path="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].highlyCompensated" onclick="setDirtyFlag();" disabled="${disableFields}"/>


	              </c:when>
	              <c:otherwise>
<form:checkbox path="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].highlyCompensated" disabled="true"/>

	              </c:otherwise>
	            </c:choose>
                </td>
           <!-- leased -->
                <td align="center" class="dataColumn">
	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:checkbox path="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].leased" onclick="setDirtyFlag();" disabled="${disableFields}"/>


	              </c:when>
	              <c:otherwise>
<form:checkbox path="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].leased" disabled="true"/>

	              </c:otherwise>
	            </c:choose>
                </td>
           <!-- other -->
                <td align="center" class="dataColumn">
	            <c:choose>
	              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:checkbox path="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].other" onclick="setDirtyFlag();" disabled="${disableFields}"/>


	              </c:when>
	              <c:otherwise>
<form:checkbox path="planDataUi.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].other" disabled="true"/>

	              </c:otherwise>
	            </c:choose>
                </td>
           <!-- dummy column --> 
                <td class="dataColumn">&nbsp;</td>
             </tr>                
			</c:forEach>
		  </c:otherwise>
		</c:choose>
        </tbody> 
      </table> 
    </div> 
 <!-- auto enrollment -->   
 <div class="subsubhead">Automatic Enrollment</div>  
    <div class="evenDataRow">  
    	<table class="dataTable"> 
            <tr>
              <td class="eligibilityAndParticipationLabelColumn"> 
                 <ps:fieldHilight name="planDataUi.planData.automaticEnrollmentAllowed" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
                 <ps:fieldHilight name="planDataUi.automaticEnrollmentEffectiveDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
                 Does the plan provide for automatic enrollment?
              </td>
              <td class="dataColumn">
                <c:choose>
                  <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">                           
<form:radiobutton disabled="${disableFields}" onclick="onAutomaticEnrollmentAllowedChanged()" path="planDataUi.planData.automaticEnrollmentAllowed" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}<span id="planDataUi_automaticEnrollmentEffectiveDate_span" style="">, as of




<form:input path="planDataUi.automaticEnrollmentEffectiveDate" disabled="${disableFields}" maxlength="10" onblur="validateAutomaticEnrollmentEffectiveDate(this)" onchange="setDirtyFlag();" size="10" id="automaticEnrollmentEffectiveDateId"/>






	                  <c:if test="${planDataForm.editMode}">
	                    <img onclick="return handleDateIconClicked(event, 'automaticEnrollmentEffectiveDateId');" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
	                  </c:if>
                    </span>

<form:radiobutton disabled="${disableFields}" onclick="onAutomaticEnrollmentAllowedChanged()" path="planDataUi.planData.automaticEnrollmentAllowed" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



<form:radiobutton disabled="${disableFields}" onclick="onAutomaticEnrollmentAllowedChanged()" path="planDataUi.planData.automaticEnrollmentAllowed" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}


                  </c:when>
                  <c:otherwise>
                    ${planDataForm.planDataUi.automaticEnrollmentAllowedDisplay}${planDataForm.planDataUi.automaticEnrollmentEffectiveDateDisplay}
                  </c:otherwise>
                </c:choose>
              </td>
        </tr> 
      </table>
    </div>
<!-- 90 day (or shorter) withdrawal election --> 
    <div class="evenDataRow">  
    	<table class="dataTable">   
            <tr>
              <td class="eligibilityAndParticipationLabelColumn"> 
              	 <TABLE width="100%"  style = 'border:collapse' cellspacing=0 cellpadding=0>
					<tr>
						<td>
				 <ps:fieldHilight name="planDataUi.planData.isNinetyDayOrShorterWithdrawalElectionOffered" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
                  Does the plan allow automatic contributions withdrawals<br>
	              (with an election period of 30 to 90 days)?
						</td>
					</tr>
					<tr height='5px'>
						<td></td>
					</tr>
					<tr>	
						<td>
							<content:getAttribute id="applyFeeInformation404a5DisclosurePurposeForEligibility" attribute="text"/><br>	
						</td>	
					</tr>
				</table>
              </td>
              <td class="dataColumn" style="vertical-align: top" >
                <c:choose>
                  <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">                           
<form:radiobutton disabled="${disableFields}" onclick="onNinetyDayOrShorterWithdrawalElectionChanged()" path="planDataUi.planData.isNinetyDayOrShorterWithdrawalElectionOffered" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="onNinetyDayOrShorterWithdrawalElectionChanged()" path="planDataUi.planData.isNinetyDayOrShorterWithdrawalElectionOffered" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



<form:radiobutton disabled="${disableFields}" onclick="onNinetyDayOrShorterWithdrawalElectionChanged()" path="planDataUi.planData.isNinetyDayOrShorterWithdrawalElectionOffered" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}



                  </c:when>
                  <c:otherwise>
                    ${planDataForm.planDataUi.isNinetyDayOrShorterWithdrawalElectionOfferedDisplay}
                  </c:otherwise>
                </c:choose>
              </td>
        </tr> 
      </table>
    </div>
<!-- default deferral percentage for AE -->
  <div class="evenDataRow">
    <table class="dataTable">
      <tr>
        <td class="eligibilityAndParticipationLabelColumn">
          <ps:fieldHilight name="planDataUi.deferralPercentageForAutomaticEnrollment" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
          &nbsp;&nbsp;&nbsp;Initial default deferral percentage for automatic enrollment
        </td>
        <td class="dataColumn">
          <c:choose>
            <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.deferralPercentageForAutomaticEnrollment" disabled="${disableFields}" maxlength="5" onblur="validateDefaultDeferralPercentageForAutomaticEnrollment(this, $('#planDataUi_planData_intendsToMeetIrcQualifiedAutomaticContributionArrangement').is(':checked'))" onchange="setDirtyFlag();" size="5" cssClass="numericInput" id="planDataUi_deferralPercentageForAutomaticEnrollment"/>







              %
            </c:when>
            <c:otherwise>
              <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_PERCENTAGE_RATE_SCALE}" value="${planDataForm.planDataUi.planData.deferralPercentageForAutomaticEnrollment}"/>${empty planDataForm.planDataUi.planData.deferralPercentageForAutomaticEnrollment ? '' : '%'}
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </table>
  </div>

  </div>
</div>
