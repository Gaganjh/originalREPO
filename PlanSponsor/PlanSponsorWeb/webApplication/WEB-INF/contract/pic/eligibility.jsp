<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="serviceFeatureConstants" className="com.manulife.pension.service.contract.util.ServiceFeatureConstants" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />

<content:contentBean
  contentId="<%=ContentConstants.MESSAGE_ON_SELECTION_OF_AUTOMATIC_ENTROLLMENT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
  id="automaticEnrollmentSelectionText" /> 
<content:contentBean
  contentId="<%=ContentConstants.FEE_INFORMATION_FOR_404A5_DISCLOSURE_PURPOSES_FOR_ELIGIBILITY%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
  id="applyFeeInformation404a5DisclosurePurposeForEligibility" />
<content:contentBean
  contentId="<%=ContentConstants.MISCELLANEOUS_PLAN_HEADING_ELIGIBILITY%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
  id="eligibilityText"/>
<content:contentBean
  contentId="<%=ContentConstants.MISCELLANEOUS_PLAN_NO_ELIGIBILITY_MONEY_TYPES%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
  id="noMoneyTypesText"/>  

<content:contentBean 
	contentId="80988" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="eligibilityRulePermittedText" />   
  
<script type="text/javascript">
var EEDEF_INDEX = -1;
var EEROT_INDEX = -1;
<c:forEach items="${pifDataForm.planInfoVO.eligibility.eligibilityRequirements}" var="moneyTypeEligibilityCriterion" varStatus="moneyTypeEligibilityCriteriaStatus">
  <c:if test="${moneyTypeEligibilityCriterion.moneyTypeId == 'EEDEF'}">
    EEDEF_INDEX = ${moneyTypeEligibilityCriteriaStatus.index};
  </c:if>
  <c:if test="${moneyTypeEligibilityCriterion.moneyTypeId == 'EEROT'}">
  EEROT_INDEX = ${moneyTypeEligibilityCriteriaStatus.index};
</c:if>

</c:forEach>

function evaluateEligibilityCriteriaGrid() { 
  var multipleEligibilityRulesForOneSingleMoneyType = $("input[name='planInfoVO.eligibility.hasMultipleEligibilityRules']:checked").val();

  enableImmediateEligibility = new Array(${fn:length(pifDataForm.planInfoVO.eligibility.eligibilityRequirements)});
  enableHoursOfService = new Array(${fn:length(pifDataForm.planInfoVO.eligibility.eligibilityRequirements)});
  enableServiceCreditingMethod = new Array(${fn:length(pifDataForm.planInfoVO.eligibility.eligibilityRequirements)});
  enableMinimumAge = new Array(${fn:length(pifDataForm.planInfoVO.eligibility.eligibilityRequirements)});
  enablePeriodOfService = new Array(${fn:length(pifDataForm.planInfoVO.eligibility.eligibilityRequirements)});
  enablePlanEntryFrequency = new Array(${fn:length(pifDataForm.planInfoVO.eligibility.eligibilityRequirements)});
  enablePartTimeEligibility = new Array(${fn:length(planDataForm.planDataUi.planData.moneyTypeEligibilityCriteria)});

  hasMultipleRules = false;
   
  if (multipleEligibilityRulesForOneSingleMoneyType == "${planDataConstants.YES_CODE}") {
    hasMultipleRules = true;
  }
  
  for (var i = 0; i < ${fn:length(pifDataForm.planInfoVO.eligibility.eligibilityRequirements)}; i++) {
    enableImmediateEligibility[i] = ! hasMultipleRules;
    enableServiceCreditingMethod[i] = ! hasMultipleRules;
    enableMinimumAge[i] = ! hasMultipleRules;
    enableHoursOfService[i] = ! hasMultipleRules;
    enablePeriodOfService[i] = ! hasMultipleRules;
    enablePlanEntryFrequency[i] = ! hasMultipleRules;
    enablePartTimeEligibility[i] = ! hasMultipleRules;
    if (i == EEDEF_INDEX) {
		enableImmediateEligibility[i] = true;
		enableServiceCreditingMethod[i] = true;
		enableMinimumAge[i] = true;
		enableHoursOfService[i] = true;	  
		enablePeriodOfService[i] = true;
		enablePlanEntryFrequency[i] = true;
		enablePartTimeEligibility[i] = true;
    }
  }

  for (var i = 0; i < ${fn:length(pifDataForm.planInfoVO.eligibility.eligibilityRequirements)}; i++) {
    var immediateEligibilityCheckboxId = "#planDataUi_moneyTypeEligibilityCriteria_" + i + "_immediateEligibilityIndicator";
    var serviceCreditingMethodSelectId = "#select_moneyTypeEligibilityCriteria_" + i + "_serviceCreditingMethod";
	var serviceCreditingMethodHiddenId = "#select_moneyTypeEligibilityCriteria_" + i + "_serviceCreditingMethodHiddenId";
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
    var planEntryFrequencyHiddenId = "#select_moneyTypeEligibilityCriteria_" + i + "_planEntryFrequencyIndicatorHiddenId";	
    var moneyTypeId = "#planDataUi_moneyTypeEligibilityCriteria_" + i + "_moneyTypeId";
    var partTimeEligibilityCheckboxId = "#planDataUi_moneyTypeEligibilityCriteria_" + i + "_partTimeEligibilityIndicator";
    if (! enableImmediateEligibility[i]) {
      //$(immediateEligibilityCheckboxId).prop("checked", "");
	  if($(immediateEligibilityCheckboxId).is(':checked')){
		$(immediateEligibilityCheckboxId).prop("checked", "");
		immediateEligibilityHiddenId = document.getElementById("immediateEligibilityHidden_"+i);
		immediateEligibilityHiddenId.value = 'false';
	  }		  
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
	  $(serviceCreditingMethodHiddenId).val("${planDataConstants.UNSPECIFIED_CODE}");
    } else {
      $(serviceCreditingMethodSelectId).prop("disabled", false);
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
      $(minimumAgeSelectId).prop("disabled", false);
    }

    if (! enableHoursOfService[i]) {
      $(hoursOfServiceDivId).hide();
      $(hoursOfServiceSelectId).val("${planDataConstants.UNSPECIFIED_CODE}");
      $(hoursOfServiceInputId).val("");
      $(hoursOfServiceSelectId).prop('disabled', true);
    } else {
      $(hoursOfServiceSelectId).prop("disabled", false);
    }

    if (! enablePeriodOfService[i]) {
      $(periodOfServiceDivId).hide();
      $(periodOfServiceSelectId).val("${planDataConstants.UNSPECIFIED_CODE}");
      $(periodOfServiceInputId).val("");
      $(periodOfServiceSelectUnitId).val("");
      $(periodOfServiceSelectId).prop('disabled', true);
    } else {
      $(periodOfServiceSelectId).prop("disabled", false);
    }
  
    if (! enablePlanEntryFrequency[i]) {
      $(planEntryFrequencySelectId).val("${planDataConstants.UNSPECIFIED_CODE}");
      $(planEntryFrequencySelectId).prop("disabled", true);
	  $(planEntryFrequencyHiddenId).val("${planDataConstants.UNSPECIFIED_CODE}");
    } else {
      $(planEntryFrequencySelectId).prop("disabled", false);
    }
	
    <c:if test="${pifDataForm.planInfoVO.generalInformations.is457Plan != true}">
		if (! enablePartTimeEligibility[i]) {
		      $(partTimeEligibilityCheckboxId).prop("checked", "");
		      $(partTimeEligibilityCheckboxId).prop("disabled", true);
		  	  partTimeEligibilityHiddenId = document.getElementById("partTimeEligibilityHidden_"+i);
		  	  partTimeEligibilityHiddenId.value = 'false';
		} else {
			if( i == EEDEF_INDEX || i == EEROT_INDEX){
				 $(partTimeEligibilityCheckboxId).prop("checked", "checked");
				$(partTimeEligibilityCheckboxId).prop("disabled", true);
		    }else{
		     $(partTimeEligibilityCheckboxId).prop("disabled", false);
		    }
		}
    </c:if>
	
	var firstPlanEntryDateId = "#eligibility_firstPlanEntryDate";
	var firstPlanEntryDateHiddenId = "#eligibility_firstPlanEntryDateHiddenId";
	var planEntryDateBasisCNId = "#eligibility_planEntryDateBasis_CN";
	var planEntryDateBasisNEId = "#eligibility_planEntryDateBasis_NE";	
	var planEntryDateBasisNRId = "#eligibility_planEntryDateBasis_NR";	
	var planEntryDateBasisOTId = "#eligibility_planEntryDateBasis_OT";
	var planEntryDateBasisHiddenId = "#eligibility_planEntryDateBasisHiddenId";	
	var eligibilityComputationPeriodPId = "#eligibility_eligibilityComputationPeriod_P";
	var eligibilityComputationPeriodAId = "#eligibility_eligibilityComputationPeriod_A";
	var eligibilityComputationPeriodHiddenId = "#eligibility_eligibilityComputationPeriodHiddenId";
	var eligibilityReqtMetYesId = "#planInfoVO_eligibility_rolloversDelayedUntilEligibilityReqtMet_selectedIndicator_yes";
	var eligibilityReqtMetNoId = "#planInfoVO_eligibility_rolloversDelayedUntilEligibilityReqtMet_selectedIndicator_no";
	var eligibilityReqtMetNaId = "#planInfoVO_eligibility_rolloversDelayedUntilEligibilityReqtMet_selectedIndicator_na";
	var eligibilityReqtMetHiddenId = "#eligibility_rolloversDelayedUntilEligibilityReqtMetHiddenId";
	if (multipleEligibilityRulesForOneSingleMoneyType == "${planDataConstants.YES_CODE}") {
      $(firstPlanEntryDateId).val("");
      $(firstPlanEntryDateId).prop("disabled", true);	 
	  $(firstPlanEntryDateHiddenId).val("");	  
	  
	  $(planEntryDateBasisCNId).prop("checked", "");
      $(planEntryDateBasisCNId).prop("value", "");
      $(planEntryDateBasisCNId).prop("disabled", true);

	  $(planEntryDateBasisNEId).prop("checked", "");
      $(planEntryDateBasisNEId).prop("value", "");
      $(planEntryDateBasisNEId).prop("disabled", true);

	  $(planEntryDateBasisNRId).prop("checked", "");
      $(planEntryDateBasisNRId).prop("value", "");
      $(planEntryDateBasisNRId).prop("disabled", true);

	  $(planEntryDateBasisOTId).prop("checked", "");
      $(planEntryDateBasisOTId).prop("value", "");
      $(planEntryDateBasisOTId).prop("disabled", true);
	  $(planEntryDateBasisHiddenId).val("UN");
	  
	  $(eligibilityComputationPeriodPId).prop("checked", "");
      $(eligibilityComputationPeriodPId).val("");
      $(eligibilityComputationPeriodPId).prop("disabled", true);

	  $(eligibilityComputationPeriodAId).prop("checked", "");
      $(eligibilityComputationPeriodAId).val("");
      $(eligibilityComputationPeriodAId).prop("disabled", true);
	  $(eligibilityComputationPeriodHiddenId).val("U");	  

	  $(eligibilityReqtMetYesId).prop("checked", "");
      $(eligibilityReqtMetYesId).val("");
      $(eligibilityReqtMetYesId).prop("disabled", true);	

	  $(eligibilityReqtMetNoId).prop("checked", "");
      $(eligibilityReqtMetNoId).val("");
      $(eligibilityReqtMetNoId).prop("disabled", true);	

	  $(eligibilityReqtMetNaId).prop("checked", "");
      $(eligibilityReqtMetNaId).val("");
      $(eligibilityReqtMetNaId).prop("disabled", true);
	  $(eligibilityReqtMetHiddenId).val("U");

	}else{
	  $(firstPlanEntryDateId).prop("disabled", false);
	  $(planEntryDateBasisCNId).prop("disabled", false);
	  $(planEntryDateBasisNEId).prop("disabled", false);
	  $(planEntryDateBasisNRId).prop("disabled", false);
	  $(planEntryDateBasisOTId).prop("disabled", false);
	  $(eligibilityComputationPeriodPId).prop("disabled", false);
	  $(eligibilityComputationPeriodAId).prop("disabled", false);	  
	  $(eligibilityReqtMetYesId).prop("disabled", false);
	  $(eligibilityReqtMetNoId).prop("disabled", false);
	  $(eligibilityReqtMetNaId).prop("disabled", false);
	  
      $(planEntryDateBasisCNId).prop("value", "CN");
      $(planEntryDateBasisNEId).prop("value", "NE");
      $(planEntryDateBasisNRId).prop("value", "NR");
      $(planEntryDateBasisOTId).prop("value", "OT");
      $(eligibilityComputationPeriodPId).prop("value", "P");
      $(eligibilityComputationPeriodAId).prop("value", "A");
      $(eligibilityReqtMetYesId).prop("value", "Y");
      $(eligibilityReqtMetNoId).prop("value", "N");
      $(eligibilityReqtMetNaId).prop("value", "NA");

	}
	
  }
  
  evaluateExcludedEmployeeCriteriaGrid();
}

var EXCLUDED_EEDEF_INDEX = -1;

<c:forEach items="${pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees}" var="moneyTypeExcludedEmployee" varStatus="moneyTypeExcludedEmployeeStatus">
  <c:if test="${moneyTypeExcludedEmployee.moneyTypeId == 'EEDEF'}">
  EXCLUDED_EEDEF_INDEX = ${moneyTypeExcludedEmployeeStatus.index};
  </c:if>
</c:forEach>

function evaluateExcludedEmployeeCriteriaGrid() { 	
	
  var multipleEligibilityRulesForOneSingleMoneyType = $("input[name='planInfoVO.eligibility.hasMultipleEligibilityRules']:checked").val();

  enableUnion= new Array(${fn:length(pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees)});
  enableNonResident= new Array(${fn:length(pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees)});
  enableHighlyComp= new Array(${fn:length(pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees)});
  enableLeased= new Array(${fn:length(pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees)});
  enableOther= new Array(${fn:length(pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees)});

  hasMultipleRules = false;
   
  if (multipleEligibilityRulesForOneSingleMoneyType == "${planDataConstants.YES_CODE}") {
    hasMultipleRules = true;
  }
  
  for (var i = 0; i < ${fn:length(pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees)}; i++) {
	enableUnion[i] = ! hasMultipleRules;
	enableNonResident[i] = ! hasMultipleRules;
	enableHighlyComp[i] = ! hasMultipleRules;
	enableLeased[i] = ! hasMultipleRules;
	enableOther[i] = ! hasMultipleRules;
    if (i == EXCLUDED_EEDEF_INDEX) {
    	enableUnion[i] = true;
		enableNonResident[i] = true;
		enableHighlyComp[i] = true;
		enableLeased[i] = true;	  
		enableOther[i] = true;
    }	
  }

  for (var i = 0; i < ${fn:length(pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees)}; i++) {
    var unionCheckboxId= "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_" + i + "_union";
    var nonResidentCheckboxId= "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_" + i + "_nonResidentAliens";
    var highlyCompCheckboxId= "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_" + i + "_highlyCompensated";
    var leasedCheckboxId= "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_" + i + "_leased";
    var otherCheckboxId= "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_" + i + "_other";

    if (! enableUnion[i]) {
      //$(unionCheckboxId).prop("checked", "");
	  if($(unionCheckboxId).is(':checked')){
		//$(unionCheckboxId).trigger('click');
		$(unionCheckboxId).prop("checked", "");
		unionCheckboxHiddenId = document.getElementById("moneyTypeExcludedEmployeeUnion_"+i);
		unionCheckboxHiddenId.value = 'false';		
	  }
      $(unionCheckboxId).prop("disabled", true);
	  $("input[name='moneyTypeExcludedEmployees_union']").prop("checked", "");
	  $("input[name='moneyTypeExcludedEmployees_union']").prop("disabled", true);
    } else {
      $(unionCheckboxId).prop("disabled", false);
	  $("input[name='moneyTypeExcludedEmployees_union']").prop("disabled", false);	  
    }
    
    if (! enableNonResident[i]) {
      //$(nonResidentCheckboxId).prop("checked", "");
	  if($(nonResidentCheckboxId).is(':checked')){
		//$(nonResidentCheckboxId).trigger('click');
		$(nonResidentCheckboxId).prop("checked", "");
		nonResidentCheckboxHiddenId = document.getElementById("moneyTypeExcludedEmployeeNonResidentAliens_"+i);
		nonResidentCheckboxHiddenId.value = 'false';		
	  }	  
      $(nonResidentCheckboxId).prop("disabled", true);
	  $("input[name='moneyTypeExcludedEmployees_nonResidentAliens']").prop("checked", "");	  
	  $("input[name='moneyTypeExcludedEmployees_nonResidentAliens']").prop("disabled", true);
    } else {
      $(nonResidentCheckboxId).prop("disabled", false);
	  $("input[name='moneyTypeExcludedEmployees_nonResidentAliens']").prop("disabled", false);	  
    }
    
    if (! enableHighlyComp[i]) {
      //$(highlyCompCheckboxId).prop("checked", "");
	  if($(highlyCompCheckboxId).is(':checked')){
		//$(highlyCompCheckboxId).trigger('click');
		$(highlyCompCheckboxId).prop("checked", "");
		highlyCompCheckboxHiddenId = document.getElementById("moneyTypeExcludedEmployeeHighlyCompensated_"+i);
		highlyCompCheckboxHiddenId.value = 'false';		
	  }	  
      $(highlyCompCheckboxId).prop("disabled", true);
	  $("input[name='moneyTypeExcludedEmployees_highlyCompensated']").prop("checked", "");		  
	  $("input[name='moneyTypeExcludedEmployees_highlyCompensated']").prop("disabled", true);	
    } else {
      $(highlyCompCheckboxId).prop("disabled", false);
	  $("input[name='moneyTypeExcludedEmployees_highlyCompensated']").prop("disabled", false);	  
    }

    if (! enableLeased[i]) {
      //$(leasedCheckboxId).prop("checked", "");
	  if($(leasedCheckboxId).is(':checked')){
		//$(leasedCheckboxId).trigger('click');
		$(leasedCheckboxId).prop("checked", "");
		leasedCheckboxHiddenId = document.getElementById("moneyTypeExcludedEmployeeLeased_"+i);
		leasedCheckboxHiddenId.value = 'false';
	  }		  
      $(leasedCheckboxId).prop("disabled", true);
	  $("input[name='moneyTypeExcludedEmployees_leased']").prop("checked", "");	  
	  $("input[name='moneyTypeExcludedEmployees_leased']").prop("disabled", true);
    } else {
      $(leasedCheckboxId).prop("disabled", false);
	  $("input[name='moneyTypeExcludedEmployees_leased']").prop("disabled", false);	  
    }

    if (! enableOther[i]) {
      //$(otherCheckboxId).prop("checked", "");
	  if($(otherCheckboxId).is(':checked')){
		//$(otherCheckboxId).trigger('click');
		$(otherCheckboxId).prop("checked", "");
		otherCheckboxHiddenId = document.getElementById("moneyTypeExcludedEmployeeOther_"+i);
		otherCheckboxHiddenId.value = 'false';		
	  }		  
      $(otherCheckboxId).prop("disabled", true);
	  $("input[name='moneyTypeExcludedEmployees_other']").prop("checked", "");	  
	  $("input[name='moneyTypeExcludedEmployees_other']").prop("disabled", true);	
    } else {
      $(otherCheckboxId).prop("disabled", false);
	  $("input[name='moneyTypeExcludedEmployees_other']").prop("disabled", false);	  
    }
   
  }
}

function onAutomaticEnrollmentAllowedChanged() {
  var enable = true;
  var automaticEnrollmentAllowed = $("input[name='planInfoVO.eligibility.isAutomaticEnrollmentAllowed']:checked").val();
  if (automaticEnrollmentAllowed != "${planDataConstants.YES_CODE}") {
    enable = false;
  }

  var deferralPercentageForAutomaticEnrollmentInputId = "#planDataUi_deferralPercentageForAutomaticEnrollment";
  var automaticEnrollmentEffectiveDateIdInputId = "#automaticEnrollmentEffectiveDateId";
  
  if (enable == false) {
    $(automaticEnrollmentEffectiveDateIdInputId).val("");
    $("input[name='planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed'][value='${planDataConstants.YES_CODE}']").prop("checked", "");
    $("input[name='planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed'][value='${planDataConstants.NO_CODE}']").prop("checked", "");
    $("input[name='planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed']").prop("disabled", true);
    $(deferralPercentageForAutomaticEnrollmentInputId).val("");
    $(deferralPercentageForAutomaticEnrollmentInputId).prop("disabled", true);
  } else {
    $("input[name='planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed']").prop("disabled", false);
    $(deferralPercentageForAutomaticEnrollmentInputId).prop("disabled", false);
  }
  onNinetyDayOrShorterWithdrawalElectionChanged();
  showOrHideAutomaticEnrollmentEffectiveDate();
  evaluateEligibilityCriteriaGrid();
}

function onNinetyDayOrShorterWithdrawalElectionChanged() {
  var checked = false;

  <c:if test="${pifDataForm.planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed == planDataConstants.YES_CODE}">
  checked = true;
  </c:if>

 <%--comment by rajenra <c:if test="${pifDataForm.editMode or pifDataForm.confirmMode}"> --%>
  checked = $("input[name='planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed'][value='${planDataConstants.YES_CODE}']").is(":checked");
  <%-- </c:if> --%>
  
  if (checked) {
    $("#withdrawalReasonNE").show();
  } else {
    $("#withdrawalReasonNE").hide();
  }
}

function showOrHideAutomaticEnrollmentEffectiveDate() {
  var enable = true;
  var automaticEnrollmentAllowed = $("input[name='planInfoVO.eligibility.isAutomaticEnrollmentAllowed']:checked").val();
  if (automaticEnrollmentAllowed != "${planDataConstants.YES_CODE}") {
    enable = false;
  }
  var aeEffectiveDateSpanId = "#planDataUi_automaticEnrollmentEffectiveDate_span";
  var aeEffectiveDateMsgSpanId = "#planDataUi_automaticEnrollmentEffectiveDate_msg_span";
  
  if (enable == false) {
    $(aeEffectiveDateSpanId).hide();
	$(aeEffectiveDateMsgSpanId).hide();
  } else {
    $(aeEffectiveDateSpanId).show();
	$(aeEffectiveDateMsgSpanId).show();
  }
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

  <%--<c:if test="${pifDataForm.confirmMode}">
  showOrHideAutomaticEnrollmentEffectiveDate();
  </c:if> --%>
                             
  <c:if test="${pifDataForm.editMode}">                         
    evaluateEligibilityCriteriaGrid();
    onAutomaticEnrollmentAllowedChanged();
    var serviceCreditingMethodSelectId = null;
   
<c:forEach items="${pifDataForm.planInfoVO.eligibility.eligibilityRequirements}" var="moneyTypeEligibilityCriterion" varStatus="count" >
      serviceCreditingMethodSelectId = "#select_moneyTypeEligibilityCriteria_${count.index}_serviceCreditingMethod";
      $(serviceCreditingMethodSelectId).on("change",function() {
	  //Deleted the EEROT dependency
        evaluateEligibilityCriteriaGrid();
      });
</c:forEach>
	showOrHideEligibilityRuleMsg();
  </c:if> 

  onNinetyDayOrShorterWithdrawalElectionChanged();
  
	<c:forEach items="${pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees}" var="moneyTypeExcludedEmployee" varStatus="moneyTypeExcludedEmployeesStatus">
	<c:if test="${moneyTypeExcludedEmployee.selectedMoneyType == 'true'}">  
	<!-- union  -->
	  moneyTypeExcludedEmployeesUnionId = "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_union";
	  $(moneyTypeExcludedEmployeesUnionId).on("click", function() { 
		excludedEmployeesUnionId = "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_union";
		moneyTypeExcludedEmployeeUnionId = document.getElementById("moneyTypeExcludedEmployeeUnion_"+${moneyTypeExcludedEmployeesStatus.index}); 	  
		if ($(moneyTypeExcludedEmployeesUnionId).is(':checked')) {
			$(excludedEmployeesUnionId).prop('checked', true);
			moneyTypeExcludedEmployeeUnionId.value = 'true';
		} else {
			$(excludedEmployeesUnionId).prop('checked', false);
			moneyTypeExcludedEmployeeUnionId.value = 'false';
		}
	  });

	  <!-- non resident aliens -->
	  moneyTypeExcludedEmployeesNonResidentAliensId = "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_nonResidentAliens";
	  $(moneyTypeExcludedEmployeesNonResidentAliensId).on("click", function() { 
		excludedEmployeesNonResidentAliensId = "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_nonResidentAliens";
		moneyTypeExcludedEmployeeNonResidentAliensId = document.getElementById("moneyTypeExcludedEmployeeNonResidentAliens_"+${moneyTypeExcludedEmployeesStatus.index}); 	  
		if ($(moneyTypeExcludedEmployeesNonResidentAliensId).is(':checked')) {
			$(excludedEmployeesNonResidentAliensId).prop('checked', true);
			moneyTypeExcludedEmployeeNonResidentAliensId.value = 'true';
		} else {
			$(excludedEmployeesNonResidentAliensId).prop('checked', false);
			moneyTypeExcludedEmployeeNonResidentAliensId.value = 'false';
		}
	  });	

	<!-- highly compensated -->
	  moneyTypeExcludedEmployeesHighlyCompensatedId = "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_highlyCompensated";
	  $(moneyTypeExcludedEmployeesHighlyCompensatedId).on("click", function() { 
		excludedEmployeesHighlyCompensatedId = "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_highlyCompensated";
		moneyTypeExcludedEmployeeHighlyCompensatedId = document.getElementById("moneyTypeExcludedEmployeeHighlyCompensated_"+${moneyTypeExcludedEmployeesStatus.index}); 	  
		if ($(moneyTypeExcludedEmployeesHighlyCompensatedId).is(':checked')) {
			$(excludedEmployeesHighlyCompensatedId).prop('checked', true);
			moneyTypeExcludedEmployeeHighlyCompensatedId.value = 'true';
		} else {
			$(excludedEmployeesHighlyCompensatedId).prop('checked', false);
			moneyTypeExcludedEmployeeHighlyCompensatedId.value = 'false';
		}
	  });	

	  <!-- leased -->
	  moneyTypeExcludedEmployeesLeasedId = "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_leased";
	  $(moneyTypeExcludedEmployeesLeasedId).on("click", function() { 
		excludedEmployeesLeasedId = "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_leased";
		moneyTypeExcludedEmployeeLeasedId = document.getElementById("moneyTypeExcludedEmployeeLeased_"+${moneyTypeExcludedEmployeesStatus.index}); 	  
		if ($(moneyTypeExcludedEmployeesLeasedId).is(':checked')) {
			$(excludedEmployeesLeasedId).prop('checked', true);
			moneyTypeExcludedEmployeeLeasedId.value = 'true';
		} else {
			$(excludedEmployeesLeasedId).prop('checked', false);
			moneyTypeExcludedEmployeeLeasedId.value = 'false';
		}
	  });

	<!-- other -->
	  moneyTypeExcludedEmployeesOtherId = "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_other";
	  $(moneyTypeExcludedEmployeesOtherId).on("click", function() { 
		excludedEmployeesOtherId = "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_other";
		moneyTypeExcludedEmployeeOtherId = document.getElementById("moneyTypeExcludedEmployeeOther_"+${moneyTypeExcludedEmployeesStatus.index}); 	  
		if ($(moneyTypeExcludedEmployeesOtherId).is(':checked')) {
			$(excludedEmployeesOtherId).prop('checked', true);
			moneyTypeExcludedEmployeeOtherId.value = 'true';
		} else {
			$(excludedEmployeesOtherId).prop('checked', false);
			moneyTypeExcludedEmployeeOtherId.value = 'false';
		}
	  });	  
	  
	</c:if>
	</c:forEach>
});

function showOrHideEligibilityRuleMsg() {
  var enable = true;
  var EligibilityRulePermitted = $("input[name='planInfoVO.eligibility.hasMultipleEligibilityRules']:checked").val();
  if (EligibilityRulePermitted != "${planDataConstants.YES_CODE}") {
    enable = false;
  }
  var EligibilityRulePermittedMessgeSpanId = "#planInfoVO_pifMoneyType_eligibilityRulePermitted_span";
  
  if (enable == false) {
    $(EligibilityRulePermittedMessgeSpanId).hide();
  } else {
    $(EligibilityRulePermittedMessgeSpanId).show();
  }
}

</script>
<div id="eligibilityTabDivId" class="borderedDataBox">
<!--start table content -->
	<table width="729" class="dataTable">
		<TR><TD class=subhead>	
			<DIV class=sectionTitle>
				<c:if test="${pifDataForm.confirmMode}">
					<content:getAttribute beanName="eligibilityText" attribute="text"/>
				</c:if>
			</DIV>
		</TD></TR>
	</table>
		<DIV id=eligibilityAndParticipationDataDivId>
		<DIV class=evenDataRow>
		<TABLE width="729" class=dataTable>
		<TBODY>
		<TR>
			<TD style="BORDER-TOP: #cccccc 1px solid" class=eligibilityAndParticipationLabelColumn>
				Does the plan have two or more different eligibility <br/>rules for any single money type? 
			</TD>
			<TD style="BORDER-TOP: #cccccc 1px solid" class=dataColumn>
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">		
				<table><tr><td>
<form:radiobutton disabled="${disableFields}" onclick="showOrHideEligibilityRuleMsg();setDirtyFlag();evaluateEligibilityCriteriaGrid()" path="planInfoVO.eligibility.hasMultipleEligibilityRules" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="showOrHideEligibilityRuleMsg();setDirtyFlag();evaluateEligibilityCriteriaGrid();" path="planInfoVO.eligibility.hasMultipleEligibilityRules" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}


				</td><td width="300px">
				<span id="planInfoVO_pifMoneyType_eligibilityRulePermitted_span"
					 style="display: none">
					 <font color="red"><b>
					 <br><content:getAttribute beanName="eligibilityRulePermittedText" attribute="text"/> 
					 </b></font>
				</span>
				</td></tr></table>
			  </c:when>
			  <c:otherwise>
				<c:choose>
				  <c:when test="${pifDataForm.planInfoVO.eligibility.hasMultipleEligibilityRules == 'Y' }">	
					${uiConstants.YES}
				  </c:when>
				  <c:when test="${pifDataForm.planInfoVO.eligibility.hasMultipleEligibilityRules == 'N' }">	
					${uiConstants.NO}
				  </c:when>				  
				  <c:otherwise>	
				  </c:otherwise>
				</c:choose>	
			  </c:otherwise>
			</c:choose>	
			</TD>
		</TR></TBODY></TABLE>
		</DIV>
		<!-- Eligibility Requirements  -->
		<DIV class=evenDataRow>
		<table width="729" class="dataTable" style="border-bottom-width: 1px;"> 
			<tbody> 
			  <TR><TD class=subsubhead colspan="8">Eligibility Requirements - specify the eligibility and plan entry frequency for each money type</TD></TR>			
			  <c:if test="${pifDataForm.planInfoVO.generalInformations.is457Plan != true}">
			  <tr> 
				<td width="95" valign="top" class="dataColumn"><strong>Money Type</strong></td> 
				<td width="90" align="center" valign="top" class="dataColumn"><strong>Plan Entry Frequency</strong></td>
				<td width="60" align="center" valign="top" class="dataColumn"><strong>Immediate<br/> 
				  Eligibility</strong></td> 
				<td width="90" align="center" valign="top" class="dataColumn"><strong>Minimum Age</strong></td> 				  
				<td width="90" align="center" valign="top" class="dataColumn"><strong>Service Crediting Method</strong></td> 
				<td width="90" align="center" valign="top" class="dataColumn"><strong>Hours of Service</strong></td> 
				<td width="90" align="center" valign="top" class="dataColumn"><strong>Period of Service</strong></td> 
				 <td width="85" align="center" valign="top" class="dataColumn"><strong>Part Time Eligibility</strong></td> 
			  </tr>
			  </c:if>
			  <c:if test="${pifDataForm.planInfoVO.generalInformations.is457Plan == true}">
			  <tr>
			  	<td width="125" valign="top" class="dataColumn"><strong>Money Type</strong></td> 
				<td width="100" align="center" valign="top" class="dataColumn"><strong>Plan Entry Frequency</strong></td>
				<td width="60" align="center" valign="top" class="dataColumn"><strong>Immediate<br/> 
				  Eligibility</strong></td> 
				<td width="100" align="center" valign="top" class="dataColumn"><strong>Minimum Age</strong></td> 				  
				<td width="100" align="center" valign="top" class="dataColumn"><strong>Service Crediting Method</strong></td> 
				<td width="100" align="center" valign="top" class="dataColumn"><strong>Hours of Service</strong></td> 
				<td width="107" align="center" valign="top" class="dataColumn"><strong>Period of Service</strong></td>
			  
			  </tr>
			  </c:if>
			<c:choose>
			  <c:when test="${empty pifDataForm.planInfoVO.eligibility.eligibilityRequirements}">
				<tr class="evenDataRow">
				  <td class="textData" colspan="7" style="border-left-width: 0; border-right-width: 0;">
					<content:getAttribute beanName="noMoneyTypesText" attribute="text"/>
				  </td>
				</tr>
			  </c:when>
			  <c:otherwise>
			  <c:set var="rowCount" value="0" scope="page" />
				<c:forEach items="${pifDataForm.planInfoVO.eligibility.eligibilityRequirements}" var="moneyTypeEligibilityCriterion" varStatus="moneyTypeEligibilityCriterionStatus">
					<c:if test="${moneyTypeEligibilityCriterion.selectedMoneyType == 'true'}">					
				
					  <c:set var="rowCount" value="${rowCount + 1}" scope="page"/>
<form:hidden path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].moneyTypeId" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_moneyTypeId" /><%--  input - name="moneyTypeEligibilityCriterion" --%>


				    <tr class="${(rowCount % 2 == 0) ? 'evenDataRow' : 'oddDataRow'}">	
						<td class="dataColumn" style="border-left-width: 0;" title="${moneyTypeEligibilityCriterion.contractMoneyTypeLongName}(${moneyTypeEligibilityCriterion.contractMoneyTypeShortName})">
						  ${moneyTypeEligibilityCriterion.contractMoneyTypeShortName}
						</td>
				<!-- plan entry frequency -->
						<td class="dataColumn">
						<c:choose>
						  <c:when test="${pifDataForm.editMode}">				
 <form:select path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].planEntryFrequencyIndicator" id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_planEntryFrequencyIndicator" onchange="setDirtyFlag();">


								<form:options items="${eligibilityPlanEntryFrequencies}" itemValue="code" itemLabel="description"/>
</form:select>
<%-- <form:hidden path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].planEntryFrequencyIndicator" 
	id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_planEntryFrequencyIndicatorHiddenId"/>
 --%>
						  </c:when>
						  <c:otherwise>								
<c:if test="${moneyTypeEligibilityCriterion.planEntryFrequencyIndicator == 'S'}">

								Semi-annually
</c:if>
<c:if test="${moneyTypeEligibilityCriterion.planEntryFrequencyIndicator == 'M'}">

								Monthly
</c:if>
<c:if test="${moneyTypeEligibilityCriterion.planEntryFrequencyIndicator == 'Q'}">

								Quarterly
</c:if>
<c:if test="${moneyTypeEligibilityCriterion.planEntryFrequencyIndicator == 'A'}">

								Annually
</c:if>
<c:if test="${moneyTypeEligibilityCriterion.planEntryFrequencyIndicator == 'I'}">

								Immediately
</c:if>
						  </c:otherwise>
						</c:choose>							
						</td>
						
				<!-- immediate eligibility -->
						<td class="dataColumn" align="center">
			
						<script type="text/javascript">
						$(document).ready(function() {
							immediateEligibilityId = "#planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_immediateEligibilityIndicator";
							<c:if test="${disableFields}">
							$(immediateEligibilityId).prop("disabled", true); 
							</c:if>
					<!-- disable minimum age, hours of service, period of service when immediate eligibility is checked -->
							$(immediateEligibilityId).on("click", function(event) { 
								var eventId = "#"+event.target.id; 
								var index = eventId.split('_')[2];
								immediateEligibilityHiddenId = document.getElementById("immediateEligibilityHidden_"+index);
								if($(eventId).is(':checked')){ 
									immediateEligibilityHiddenId.value = 'true';
								}else{
									immediateEligibilityHiddenId.value = 'false';
								}
								evaluateEligibilityCriteriaGrid();
							});
						});
						</script>					
					<input type="checkbox" 
						id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_immediateEligibilityIndicator" 
						name="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].immediateEligibilityIndicator" 
						value="true"						
						onclick="setDirtyFlag();"
						<c:if test="${moneyTypeEligibilityCriterion.immediateEligibilityIndicator}"> checked="checked" </c:if>
						disabled="${disableFields}"/>
<form:hidden path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].immediateEligibilityIndicator" id="immediateEligibilityHidden_${moneyTypeEligibilityCriterionStatus.index}" /><%--  input - name="pifDataForm" --%>


						</td>
				<!-- minimum age -->
						<td class="dataColumn" align="center">
						<c:choose>
						  <c:when test="${pifDataForm.editMode}">				
								<script type="text/javascript">
								$(document).ready(function() {
								  // show/suppress the input field.
								  var divId = "#moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_minimumAge_div";
								  var inputId = "#planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_minimumAge";
								  var selectId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_minimumAge";
								  
								  <c:if test="${disableFields}">
								  $(selectId).prop("disabled", true); 
								  </c:if>						  

								  <c:if test="${not disableFields}">						  
									  $(selectId).on("change",function () {
										var value = $(selectId).val();
										updateValue(value, divId, inputId);
									  });
								  </c:if>
							  
								  var value = $(inputId).val().trim();
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
								<select onchange="setDirtyFlag();"
										style="width: 100px"
										id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_minimumAge">
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
								<div id="moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_minimumAge_div"
									 style="display: none">
<form:input path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].minimumAge" disabled="${disableFields}" onblur="validateEligibiliyCriteriaMinimumAge(this);" onchange="setDirtyFlag();" size="5" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_minimumAge"/>





								</div>

						  </c:when>
						  <c:otherwise>	
<c:if test="${not empty moneyTypeEligibilityCriterion.minimumAge}">
							  	<c:choose>
							  	  <c:when test="${moneyTypeEligibilityCriterion.minimumAge == '18.0'}">
							  	  	18
								  </c:when>
							  	  <c:when test="${moneyTypeEligibilityCriterion.minimumAge == '21.0'}">
							  	  	21
								  </c:when>
							  	  <c:when test="${moneyTypeEligibilityCriterion.minimumAge == '20.5'}">
							  	  	20&#189
								  </c:when>							  							  
								  <c:otherwise>
								  	${moneyTypeEligibilityCriterion.minimumAge}
								  </c:otherwise>	
								</c:choose>	
</c:if>
						  </c:otherwise>
						</c:choose>	
						</td>					
				<!-- service crediting method -->
						<td class="dataColumn">
						<c:choose>
						  <c:when test="${pifDataForm.editMode}">	
							   <script type="text/javascript">
									$(document).ready(function() {
										servicecreditingId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_serviceCreditingMethod";
										<c:if test="${disableFields}">
										$(servicecreditingId).prop("disabled", true); 
										</c:if>
										
									});
								</script>					
								  <form:select path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].serviceCreditingMethod" style="width: 100px" id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_serviceCreditingMethod">
									<form:options items="${eligibilityCreditMethods}" itemValue="code" itemLabel="description"/>
</form:select>
<%-- <form:hidden path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].serviceCreditingMethod" id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_serviceCreditingMethodHiddenId"/>
 --%>						  </c:when>
						  <c:otherwise>								
<c:if test="${moneyTypeEligibilityCriterion.serviceCreditingMethod == 'H'}">

								Hours of Service
</c:if>
<c:if test="${moneyTypeEligibilityCriterion.serviceCreditingMethod == 'E'}">

								Elapsed Time
</c:if>
						  </c:otherwise>
						</c:choose>						
			  
						</td>
				<!-- hours of service -->
						<td class="dataColumn" align="center">
							<c:choose>
							  <c:when test="${pifDataForm.editMode}">	
								<script type="text/javascript">
								$(document).ready(function() {
								  // show/suppress the input field.
								  var divId = "#moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_hoursOfService_div";
								  var inputId = "#planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_hoursOfService";
								  var selectId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_hoursOfService";

								  <c:if test="${disableFields}">
								  $(selectId).prop("disabled", true); 
								  </c:if>
								  
								  <c:if test="${not disableFields}">						  
									  $(selectId).on("change",function () {
										var value = $(selectId).val();
										updateValue(value, divId, inputId);
									  });
								  </c:if>						  

								  var value = $(inputId).val().trim();
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
								<select onchange="setDirtyFlag();"
										style="width: 100px"
										id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_hoursOfService">
									<c:forEach var="hoursOfService" items="${eligibilityHoursOfServices}">
										<option value=${hoursOfService.code}>${hoursOfService.description}</option>
									</c:forEach>
								</select>
								<div id="moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_hoursOfService_div"
									 style="display: none">
<form:input path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].hoursOfService" disabled="${disableFields}" onblur="validateEligibiliyCriteriaHoursOfService(this);" onchange="setDirtyFlag();" size="5" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_hoursOfService"/>





								</div>
							  </c:when>
							  <c:otherwise>	
<c:if test="${not empty moneyTypeEligibilityCriterion.hoursOfService}">
									  	${moneyTypeEligibilityCriterion.hoursOfService}
</c:if>
							  </c:otherwise>
							</c:choose>	
						</td>
				<!-- period of service  -->
						<td class="dataColumn" align="center">
							<c:choose>
							  <c:when test="${pifDataForm.editMode}">	
								<script type="text/javascript">
								$(document).ready(function() {
								  // show/suppress the input field.
								  var divId = "#moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfService_div";
								  var inputId = "#planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfService";
								  var selectId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfService";
								  var selectUnitId = "#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfServiceUnit";

								  <c:if test="${disableFields}">
								  $(selectId).prop("disabled", true); 
								  </c:if>
								  
								  <c:if test="${not disableFields}">						  
									  $(selectId).on("change",function () {
										var value = $(selectId).val();
										updateValues(value, divId, inputId, selectUnitId);
									  });
								  </c:if>
								  if ($(inputId).val().length > 0) {
									  var value = $(inputId).val().trim();
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
								<select onchange="setDirtyFlag();"
										style="width: 100px"
										id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfService">
									<c:forEach var="periodOfService" items="${eligibilityPeriodOfServices}">
										<option value=${periodOfService.code}>${periodOfService.description}</option>
									</c:forEach>
								</select>
								<div id="moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfService_div"
									 style="display: none">
<form:input path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].periodOfService" disabled="${disableFields}" onblur="validateEligibiliyCriteriaPeriodOfService(this, $('#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfServiceUnit').val());" onchange="setDirtyFlag();" size="3" id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfService"/>





 <form:select path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].periodOfServiceUnit" onchange="setDirtyFlag();validateEligibiliyCriteriaPeriodOfService(document.getElementById('planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfService'), $('#select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfServiceUnit').val()); " disabled="${disableFields}" style="width: 60px" 										 id="select_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_periodOfServiceUnit">




									  <form:option value="">Select</form:option>	                      
									  <form:options items="${eligibilityPeriodOfServiceUnits}" itemValue="code" itemLabel="description"/>
</form:select>
								</div>
							  </c:when>
							  <c:otherwise>	
<c:if test="${not empty moneyTypeEligibilityCriterion.periodOfService}">
									  	${moneyTypeEligibilityCriterion.periodOfService} 
										<c:if test="${moneyTypeEligibilityCriterion.periodOfServiceUnit == 'D'}">days</c:if>	
										<c:if test="${moneyTypeEligibilityCriterion.periodOfServiceUnit == 'W'}">weeks</c:if>
										<c:if test="${moneyTypeEligibilityCriterion.periodOfServiceUnit == 'M'}">months</c:if>	
</c:if>
							  </c:otherwise>
							</c:choose>	
						</td>
				<!-- Part Time Eligibility  -->	
				<c:if test="${pifDataForm.planInfoVO.generalInformations.is457Plan != true}">
				<td class="dataColumn" align="center">
			
						<script type="text/javascript">
						$(document).ready(function() {
							partTimeEligibilityId = "#planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_partTimeEligibilityIndicator";
							<c:if test="${disableFields}">
							$(partTimeEligibilityId).prop("disabled", true); 
							</c:if>
						});
						</script>					
					<input type="checkbox" 
						id="planDataUi_moneyTypeEligibilityCriteria_${moneyTypeEligibilityCriterionStatus.index}_partTimeEligibilityIndicator" 
						name="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].partTimeEligibilityIndicator" 
						value="true"						
						onclick="setDirtyFlag();"
						<c:if test="${moneyTypeEligibilityCriterion.partTimeEligibilityIndicator}"> checked="checked" </c:if>
						disabled="${disableFields}"/> 

<form:hidden path="planInfoVO.eligibility.eligibilityRequirements[${moneyTypeEligibilityCriterionStatus.index}].partTimeEligibilityIndicator" id="partTimeEligibilityHidden_${moneyTypeEligibilityCriterionStatus.index}" />
					
						</td>
						</c:if>
					  </tr>
					</c:if>
				</c:forEach>
			  </c:otherwise>
			</c:choose>
			</tbody> 
		</table>
	  <!-- first plan entry date -->
	  <DIV class=evenDataRow>
	  <TABLE class=dataTable width="100%">
		<TBODY>
		<TR>
		  <TD style="BORDER-TOP: #cccccc 1px solid" class=eligibilityAndParticipationLabelColumn>
		  The first date for all plan entry frequencies is normally the 1<sup>st</sup> day of the plan year.  If any other date applies, please specify it here:</TD>
		  <TD style="BORDER-TOP: #cccccc 1px solid" class=dataColumn>
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">				
<form:input path="pifDataUi.firstPlanEntryDateString" id="eligibility_firstPlanEntryDate"
								   size="5" 
								   maxlength="10" 
								   onblur="validatePlanEntryDate(this)"
								   onchange="setDirtyFlag();"
								   disabled="${disableFields}"/>
					<c:if test="${not empty pifDataForm.planInfoVO.eligibility.firstPlanEntryDate.data}">
					  (mm&frasl;dd)
					</c:if> 
					<!-- <input type="hidden" id="eligibility_firstPlanEntryDateHiddenId" property="pifDataUi.firstPlanEntryDateString"/> -->			
			  </c:when>
			  <c:otherwise>
				${pifDataForm.planInfoVO.eligibility.firstPlanEntryDate.data}	
				<c:if test="${not empty pifDataForm.planInfoVO.eligibility.firstPlanEntryDate.data}">
				  (mm&frasl;dd)
				</c:if> 				
			  </c:otherwise>
			</c:choose>
	  </TD></TR>
	<%-- eligible employee plan entry date --%>			
		<TR vAlign=top>
		  <TD style="BORDER-TOP: #cccccc 1px solid" class=eligibilityAndParticipationLabelColumn>
			An eligible employee who has satisfied the eligibility requirements will 
			enter the plan on the plan entry date that... </TD>
		  <TD style="BORDER-TOP: #cccccc 1px solid" class=dataColumn width="390">
			 
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">				
<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.eligibility.planEntryDateBasis" id="eligibility_planEntryDateBasis_CN" value="CN"/>Coincides with or immediately follows the eligibility date<br>




<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.eligibility.planEntryDateBasis" id="eligibility_planEntryDateBasis_NE" value="NE"/>Next follows the eligibility date<br>




<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.eligibility.planEntryDateBasis" id="eligibility_planEntryDateBasis_NR" value="NR"/>Nearest to the eligibility date<br>




<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.eligibility.planEntryDateBasis" id="eligibility_planEntryDateBasis_OT" value="OT"/>Other<br>




<%--   <form:hidden path="planInfoVO.eligibility.planEntryDateBasis" id="eligibility_planEntryDateBasisHiddenId"/> 
 --%>			  </c:when>
			  <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.eligibility.planEntryDateBasis == 'CN'}">
Coincides with or immediately follows the eligibility date
</c:if>
<c:if test="${pifDataForm.planInfoVO.eligibility.planEntryDateBasis == 'NE'}">
Next follows the eligibility date
</c:if>
<c:if test="${pifDataForm.planInfoVO.eligibility.planEntryDateBasis == 'NR'}">
Nearest to the eligibility date
</c:if>
<c:if test="${pifDataForm.planInfoVO.eligibility.planEntryDateBasis == 'OT'}">
Other
</c:if>
			  </c:otherwise>
			</c:choose>									
		  </TD></TR>
	<%-- eligibility computation period --%>		
		<TR vAlign=top>
		  <TD style="BORDER-TOP: #cccccc 1px solid" class=eligibilityAndParticipationLabelColumn>
		  The eligibility computation period after the initial eligibility<br>computation period shall...
		  </TD>
		  <TD style="BORDER-TOP: #cccccc 1px solid" class=dataColumn width="390px">
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">				
<form:radiobutton  disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.eligibility.eligibilityComputationPeriod" id="eligibility_eligibilityComputationPeriod_P" value="P"/>
Shift to the Plan Year<br>                    
<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.eligibility.eligibilityComputationPeriod" id="eligibility_eligibilityComputationPeriod_A" value="A"/>
Be based on each anniversary of the date the Employee first completes an hour of service<br>
<%--   <form:hidden path="planInfoVO.eligibility.eligibilityComputationPeriod" id="eligibility_eligibilityComputationPeriodHiddenId"/> 
 --%>			  </c:when>
			  <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.eligibility.eligibilityComputationPeriod == 'P'}">
Shift to the Plan Year
</c:if>
<c:if test="${pifDataForm.planInfoVO.eligibility.eligibilityComputationPeriod == 'A'}">
Be based on each anniversary of the date the Employee first completes an hour of service
</c:if>
			  </c:otherwise>
			</c:choose>				
		  </TD></TR>
	<%-- rollovers delayed until eligibility reqt met --%> 		
		<TR>
		  <TD style="BORDER-TOP: #cccccc 1px solid" class=eligibilityAndParticipationLabelColumn>
		  Must rollovers be delayed until eligibility requirements are met? 
		  </TD>
		  <TD style="BORDER-TOP: #cccccc 1px solid" class=dataColumn width="390px">		  
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">				
<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.eligibility.rolloversDelayedUntilEligibilityReqtMet" id="planInfoVO_eligibility_rolloversDelayedUntilEligibilityReqtMet_selectedIndicator_yes" value="Y"/>Yes
<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.eligibility.rolloversDelayedUntilEligibilityReqtMet" id="planInfoVO_eligibility_rolloversDelayedUntilEligibilityReqtMet_selectedIndicator_no" value="N"/>No
<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.eligibility.rolloversDelayedUntilEligibilityReqtMet" id="planInfoVO_eligibility_rolloversDelayedUntilEligibilityReqtMet_selectedIndicator_na" value="NA"/>Not Applicable. Rollovers are not permitted by the plan.



<%--  <form:hidden path="planInfoVO.eligibility.rolloversDelayedUntilEligibilityReqtMet" id="eligibility_rolloversDelayedUntilEligibilityReqtMetHiddenId"/>
 --%>
			  </c:when>
			  <c:otherwise>	
<c:if test="${pifDataForm.planInfoVO.eligibility.rolloversDelayedUntilEligibilityReqtMet == 'Y'}">
${uiConstants.YES}
</c:if>
<c:if test="${pifDataForm.planInfoVO.eligibility.rolloversDelayedUntilEligibilityReqtMet == 'N'}">
${uiConstants.NO}
</c:if>
<c:if test="${pifDataForm.planInfoVO.eligibility.rolloversDelayedUntilEligibilityReqtMet == 'NA'}">
Not Applicable. Rollovers are not permitted by the plan
</c:if>
			  </c:otherwise>
			</c:choose>							
		  </TD></TR></TBODY></TABLE>
		</DIV>
	</DIV>
	<!-- excluded employees -->
	<div class="evenDataRow"> 
	  <table width="729" class="dataTable" style="border-bottom-width: 1px;"> 
		<tbody> 
		  <TR><TD class=subsubhead colspan="7">Excluded Employees - specify excluded employees by Money Type. Check all that apply.</TD></TR>		
		  <tr> 
			<td width="125" valign="top" class="dataColumn"><strong>Money Type Code</strong></td> 
			<td width="85" valign="top" align="center" class="dataColumn"><strong>Union</strong></td> 
			<td width="85" valign="top" align="center" class="dataColumn"><strong>Nonresident Aliens</strong></td> 
			<td width="85" valign="top" align="center" class="dataColumn"><strong>Highly Compensated</strong></td> 
			<td width="85" valign="top" align="center" class="dataColumn"><strong>Leased</strong></td> 
			<td width="85" valign="top" align="center" class="dataColumn"><strong>Other</strong></td>
			<td width="140" valign="top" class="dataColumn">&nbsp;</td>
		  </tr> 
		<c:choose>
		  <c:when test="${empty pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees}">
			<tr class="evenDataRow">
			  <td class="textData" colspan="6" style="border-left-width: 0; border-right-width: 0;">
				 <content:getAttribute beanName="noMoneyTypesText" attribute="text"/>
			  </td>
			</tr>
		  </c:when>
		  <c:otherwise>
		   <c:if test="${pifDataForm.editMode}">	
			<tr class="oddDataRow">
			  <td style="border-left-width: 0px;" class="dataColumn" title="all">All Money Types
			  </td>
			  <!-- union  -->
			  <td class="dataColumn" align="middle">
			  <input value="on" name="moneyTypeExcludedEmployees_union" type="checkbox" id="pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_union"> 
			  </td>
			  <!-- non resident aliens -->
			  <td class="dataColumn" align="middle">
			  <input value="on" name="moneyTypeExcludedEmployees_nonResidentAliens" type="checkbox" id="pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_nonResidentAliens"> </td>				 
			  <!-- highly compensated -->
			  <td class="dataColumn" align="middle">
			  <input value="on" name="moneyTypeExcludedEmployees_highlyCompensated" type="checkbox" id="pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_highlyCompensated"> 
			  </td>
			  <!-- leased -->
			  <td class="dataColumn" align="middle">
			  <input value="on" name="moneyTypeExcludedEmployees_leased" type="checkbox" id="pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_leased"> 
			  </td>
			  <!-- other -->
			  <td class="dataColumn" align="middle">
			  <input value="on" name="moneyTypeExcludedEmployees_other" type="checkbox" id="pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_other"> 
			  </td>
			  <!-- dummy column -->
			  <td class="dataColumn">&nbsp;</td>
			</tr>	
			</c:if>
			<c:set var="rowCount" value="0" scope="page" />			
			<c:forEach items="${pifDataForm.planInfoVO.eligibility.moneyTypeExcludedEmployees}" var="moneyTypeExcludedEmployee" varStatus="moneyTypeExcludedEmployeesStatus">
				<c:if test="${moneyTypeExcludedEmployee.selectedMoneyType == 'true'}">	
				<c:set var="rowCount" value="${rowCount + 1}" scope="page" />
				<tr class="${(rowCount % 2 == 0) ? 'oddDataRow' : 'evenDataRow'}">
					<td class="dataColumn" style="border-left-width: 0;" title="${moneyTypeExcludedEmployee.contractMoneyTypeLongName}(${moneyTypeExcludedEmployee.contractMoneyTypeShortName})">
					  ${moneyTypeExcludedEmployee.contractMoneyTypeShortName}
					</td>
			   <!-- union  -->
					<td align="center" class="dataColumn">
						<script type="text/javascript">
						$(document).ready(function() {
							excludedEmployeeUnionId= "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_union";
							<c:if test="${disableFields}">
							$(excludedEmployeeUnionId).prop("disabled", true); 
							</c:if>								
							$(excludedEmployeeUnionId).on("click", function(event) {
								var eventId = "#"+event.target.id;
								var index = eventId.split('_')[4];
								moneyTypeExcludedEmployeeUnionId = document.getElementById("moneyTypeExcludedEmployeeUnion_"+index);
								if($(eventId).is(':checked')){ 
									moneyTypeExcludedEmployeeUnionId.value = 'true';
								}else{
									moneyTypeExcludedEmployeeUnionId.value = 'false';
								}
							});
						});
						</script>					
						<input type="checkbox" 
							id="pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_union" 
							name="planInfoVO.eligibility.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].union" 
							value="true"						
							onclick="setDirtyFlag();"
							<c:if test="${moneyTypeExcludedEmployee.union}"> checked="checked" </c:if> />
<input type="hidden" name="planInfoVO.eligibility.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].union" id="moneyTypeExcludedEmployeeUnion_${moneyTypeExcludedEmployeesStatus.index}" /><%--  input - name="pifDataForm" --%>

					</td>
			   <!-- non resident aliens -->
					<td align="center" class="dataColumn">
						<script type="text/javascript">
						$(document).ready(function() {
							excludedEmployeeNonResidentAliensId= "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_nonResidentAliens";
							<c:if test="${disableFields}">
							$(excludedEmployeeNonResidentAliensId).prop("disabled", true); 
							</c:if>									
							$(excludedEmployeeNonResidentAliensId).on("click", function(event) {
								var eventId = "#"+event.target.id;
								var index = eventId.split('_')[4];
								moneyTypeExcludedEmployeeNonResidentAliensId = document.getElementById("moneyTypeExcludedEmployeeNonResidentAliens_"+index);
								if($(eventId).is(':checked')){ 
									moneyTypeExcludedEmployeeNonResidentAliensId.value = 'true';
								}else{
									moneyTypeExcludedEmployeeNonResidentAliensId.value = 'false';
								}
							});
						});						
						</script>	
						<input type="checkbox" 
							id="pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_nonResidentAliens" 
							name="planInfoVO.eligibility.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].nonResidentAliens" 
							value="true"						
							onclick="setDirtyFlag();"
							<c:if test="${moneyTypeExcludedEmployee.nonResidentAliens}"> checked="checked" </c:if> />
<input type="hidden" name="planInfoVO.eligibility.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].nonResidentAliens" id="moneyTypeExcludedEmployeeNonResidentAliens_${moneyTypeExcludedEmployeesStatus.index}" /><%--  input - name="pifDataForm" --%>

					</td>
			   <!-- highly compensated -->
					<td align="center" class="dataColumn">
						<script type="text/javascript">
						$(document).ready(function() {
							excludedEmployeeHighlyCompensatedId= "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_highlyCompensated";
							<c:if test="${disableFields}">
							$(excludedEmployeeHighlyCompensatedId).prop("disabled", true); 
							</c:if>									
							$(excludedEmployeeHighlyCompensatedId).on("click", function(event) {
								var eventId = "#"+event.target.id;
								var index = eventId.split('_')[4];
								moneyTypeExcludedEmployeeHighlyCompensatedId = document.getElementById("moneyTypeExcludedEmployeeHighlyCompensated_"+index);
								if($(eventId).is(':checked')){ 
									moneyTypeExcludedEmployeeHighlyCompensatedId.value = 'true';
								}else{
									moneyTypeExcludedEmployeeHighlyCompensatedId.value = 'false';
								}
							});
						});						
						</script>	
						<input type="checkbox" 
							id="pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_highlyCompensated" 
							name="planInfoVO.eligibility.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].highlyCompensated" 
							value="true"						
							onclick="setDirtyFlag();"
							<c:if test="${moneyTypeExcludedEmployee.highlyCompensated}"> checked="checked" </c:if> />
<input type="hidden" name="planInfoVO.eligibility.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].highlyCompensated" id="moneyTypeExcludedEmployeeHighlyCompensated_${moneyTypeExcludedEmployeesStatus.index}" /><%--  input - name="pifDataForm" --%>

					</td>
			   <!-- leased -->
					<td align="center" class="dataColumn">
						<script type="text/javascript">
						$(document).ready(function() {
							excludedEmployeeLeasedId= "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_leased";
							<c:if test="${disableFields}">
							$(excludedEmployeeLeasedId).prop("disabled", true); 
							</c:if>								
							$(excludedEmployeeLeasedId).on("click", function(event) {
								var eventId = "#"+event.target.id;
								var index = eventId.split('_')[4];
								moneyTypeExcludedEmployeeLeasedId = document.getElementById("moneyTypeExcludedEmployeeLeased_"+index);
								if($(eventId).is(':checked')){ 
									moneyTypeExcludedEmployeeLeasedId.value = 'true';
								}else{
									moneyTypeExcludedEmployeeLeasedId.value = 'false';
								}
							});
						});							
						</script>	
					
						<%-- <form:checkbox name="moneyTypeExcludedEmployee"
									   path="leased"
									   onclick="setDirtyFlag();"
									   disabled="${disableFields}"/> --%>						
						<input type="checkbox" 
							id="pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_leased" 
							name="planInfoVO.eligibility.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].leased" 
							value="true"						
							onclick="setDirtyFlag();"
							<c:if test="${moneyTypeExcludedEmployee.leased}"> checked="checked" </c:if> />
<input type="hidden" name="planInfoVO.eligibility.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].leased" id="moneyTypeExcludedEmployeeLeased_${moneyTypeExcludedEmployeesStatus.index}" /><%--  input - name="pifDataForm" --%>

					</td>
			   <!-- other -->
					<td align="center" class="dataColumn">
						<script type="text/javascript">
						$(document).ready(function() {
							excludedEmployeeOtherId= "#pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_other";
							<c:if test="${disableFields}">
							$(excludedEmployeeOtherId).prop("disabled", true); 
							</c:if>								
							$(excludedEmployeeOtherId).on("click", function(event) {
								var eventId = "#"+event.target.id;
								var index = eventId.split('_')[4];
								moneyTypeExcludedEmployeeOtherId = document.getElementById("moneyTypeExcludedEmployeeOther_"+index);
								if($(eventId).is(':checked')){ 
									moneyTypeExcludedEmployeeOtherId.value = 'true';
								}else{
									moneyTypeExcludedEmployeeOtherId.value = 'false';
								}
							});
						});						
						</script>	
						<%-- <form:checkbox name="moneyTypeExcludedEmployee"
									   path="other"
									   onclick="setDirtyFlag();"
									   disabled="${disableFields}"/> --%>						
						<input type="checkbox" 
							id="pifDataUi_planInfoVO_eligibility_moneyTypeExcludedEmployees_${moneyTypeExcludedEmployeesStatus.index}_other" 
							name="planInfoVO.eligibility.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].other" 
							value="true"						
							onclick="setDirtyFlag();"
							<c:if test="${moneyTypeExcludedEmployee.other}"> checked="checked" </c:if> />
<input type="hidden" name="planInfoVO.eligibility.moneyTypeExcludedEmployees[${moneyTypeExcludedEmployeesStatus.index}].other" id="moneyTypeExcludedEmployeeOther_${moneyTypeExcludedEmployeesStatus.index}" /><%--  input - name="pifDataForm" --%>


					</td>
			   <!-- dummy column --> 
					<td class="dataColumn">&nbsp;</td>
				</tr>
				</c:if>
			</c:forEach>
		  </c:otherwise>
		</c:choose>
		</tbody> 
	  </table> 
	</div>
	<!-- auto enrollment -->
	<DIV class=evenDataRow>
		<TABLE width="729" class="dataTable" style="border-bottom-width: 1px;">
		<TBODY><TR><TD class=subsubhead>Automatic Enrollment</TD></TR></TBODY></TABLE>
		<TABLE width="100%" class="dataTable" style="border-bottom-width: 1px;">
		<TBODY>
		<TR>
			<TD class=eligibilityAndParticipationLabelColumn>
		  Does the plan provide for automatic enrollment? 
			</TD>
			<TD class=dataColumn>
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">				
<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();onAutomaticEnrollmentAllowedChanged()" path="planInfoVO.eligibility.isAutomaticEnrollmentAllowed" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}<span id="planDataUi_automaticEnrollmentEffectiveDate_span" style="display: none">, as of




<form:input path="pifDataUi.automaticEnrollmentDate" disabled="${disableFields}" maxlength="10" onblur="validateAutomaticEnrollmentEffectiveDate(this)" onchange="setDirtyFlag();" size="10" id="automaticEnrollmentEffectiveDateId"/>






					<img onclick="return handleDateIconClicked(event, 'automaticEnrollmentEffectiveDateId');" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
				</span>
<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();onAutomaticEnrollmentAllowedChanged()" path="planInfoVO.eligibility.isAutomaticEnrollmentAllowed" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



				<span id="planDataUi_automaticEnrollmentEffectiveDate_msg_span"	 style="display: none">
				<font color="red"><b>
					<br><content:getAttribute beanName="automaticEnrollmentSelectionText" attribute="text"/>
				</b></font>
				</span>							
			  </c:when>
			  <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.eligibility.isAutomaticEnrollmentAllowed == 'Y'}">
					${uiConstants.YES} , as of &nbsp;
<fmt:formatDate value="${pifDataForm.planInfoVO.eligibility.automaticEnrollmentDate}" pattern="MM/dd/yyyy" />(mm&frasl;dd&frasl;yyyy)					
</c:if>
<c:if test="${pifDataForm.planInfoVO.eligibility.isAutomaticEnrollmentAllowed == 'N'}">
					${uiConstants.NO}
</c:if>
			  </c:otherwise>
			</c:choose>								
			</TD>
		</TR>
	<!-- 90 day (or shorter) withdrawal election -->		
		<TR>
			<TD class=eligibilityAndParticipationLabelColumn>
				<TABLE width="100%"  style = 'border:collapse' cellspacing=0 cellpadding=0>
					<tr>
						<td>
				Does the plan allow automatic contributions withdrawals<br>
				&nbsp;&nbsp;&nbsp;(with an election period of 30 to 90 days)?
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
			</TD>
			<TD class=dataColumn style="vertical-align: top">
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">				
<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();onNinetyDayOrShorterWithdrawalElectionChanged()" path="planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();onNinetyDayOrShorterWithdrawalElectionChanged()" path="planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



			  </c:when>
			  <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed == 'Y'}">

					${uiConstants.YES}
</c:if>
<c:if test="${pifDataForm.planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed == 'N'}">

					${uiConstants.NO}
</c:if>
			  </c:otherwise>
			</c:choose>
			</TD>
		</TR>
	<!-- default deferral percentage for AE -->		
		<TR>
			<TD class=eligibilityAndParticipationLabelColumn>	
				Initial default deferral percentage for automatic enrollment
			</TD>
			<TD class=dataColumn>
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">				
<form:input path="pifDataUi.deferralPercentageForAutomaticEnrollment" disabled="${disableFields}" maxlength="5" onchange="setDirtyFlag();" size="5" cssClass="numericInput" id="planDataUi_deferralPercentageForAutomaticEnrollment"/>  %






			  </c:when>
			  <c:otherwise>
                <fmt:formatNumber pattern="###.###"  maxFractionDigits="3" value="${pifDataForm.planInfoVO.eligibility.defaultDeferralPercentage}"/>${empty pifDataForm.planInfoVO.eligibility.defaultDeferralPercentage ? '' : '%'}				
			  </c:otherwise>
			</c:choose>				  
			</TD>
		</TR></TBODY></TABLE>
	</DIV>
</DIV>
<!--end table content -->
</div>
						