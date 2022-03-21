<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page import="com.manulife.pension.service.contract.valueobject.MoneyTypeVO"%>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="aciConstants" className="com.manulife.pension.service.plan.valueobject.PlanAutoContributionIncrease" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="deferralConstants" className="com.manulife.pension.service.plan.valueobject.PlanEmployeeDeferralElection" />
<un:useConstants var="ruleConstants" className="com.manulife.pension.service.plan.valueobject.PlanContributionRule" />

<content:contentBean contentId="67216" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_EMPTY_SALARY_DEFERAL_ELECTION_DAY" />
<content:contentBean contentId="67217" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_EMPTY_SALARY_DEFERAL_ELECTION_MONTH" />
<content:contentBean contentId="67233" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_INVALID_ACI_EFFECTIVE_DATE_SHD_BE_GREATER" />	
<content:contentBean contentId="67226" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_INVALID_ACI_EFFECTIVE_DATE_EMPTY" />		
<content:contentBean contentId="67219" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_DEFERAL_MINIMUM_SHD_BE_LESSER_THAN_DEFERAL_MAXIMUM" />	
<content:contentBean contentId="80900" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_MAXIMUM_CONTRIBUTION_FIRST_RADIOBUTTON" />	
<content:contentBean contentId="80903" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_MAXIMUM_CONTRIBUTION_FIRST_PERCENTAGE" />	
<content:contentBean contentId="80904" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_MAXIMUM_CONTRIBUTION_NEXT_PERCENTAGE" />
<content:contentBean contentId="80905" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_MAXIMUM_CONTRIBUTION_NEXT_RADIOBUTTON" />
<content:contentBean contentId="80909" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_MAXIMUM_CONTRIBUTION_RADIOBUTTON_SAME" />	
<content:contentBean contentId="69824" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_CONTRIBUTION_FORMULAS_RULE_TYPES" />	
	
<style type="text/css">
.formulaHeader {
 BACKGROUND-COLOR: #e6e7e8;
 border:1px solid  #999999;
 padding: 2px;
 font-weight: bold;
}
.formulaBody {
 border-left:1px solid  #999999;
 border-bottom:1px solid  #999999;
 border-right:1px solid  #999999;
}
.formulaBody td {
 padding-top:2px;
}
.formulaFirstColumn {
  width: 125px;
  vertical-align: middle;
}

</style>
<jsp:useBean id="now" class="java.util.Date" scope="request" />
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_CONTRIBUTIONS}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="contributionText"/>
<content:contentBean
  contentId="${contentConstants.PS_MISCELLANEOUS_PLAN_FORMULAS_NO_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="contributionNoMoneyTypes"/>
<content:contentBean
  contentId="${contentConstants.PS_MISCELLANEOUS_PLAN_FORMULAS_MONEY_TYPES_NOT_IN_USE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="moneyTypesNotInUse"/>
  
<script type="text/javascript">

   var ERR_EMPTY_SALARY_DEFERAL_ELECTION_DAY = "<content:getAttribute beanName='ERR_EMPTY_SALARY_DEFERAL_ELECTION_DAY' attribute='text'  filter='true' escapeJavaScript="true"/>";
   var ERR_EMPTY_SALARY_DEFERAL_ELECTION_MONTH = "<content:getAttribute beanName='ERR_EMPTY_SALARY_DEFERAL_ELECTION_MONTH' attribute='text'  filter='true' escapeJavaScript="true"/>";
   var ERR_INVALID_ACI_EFFECTIVE_DATE_SHD_BE_GREATER = "<content:getAttribute beanName='ERR_INVALID_ACI_EFFECTIVE_DATE_SHD_BE_GREATER' attribute='text'  filter='true' escapeJavaScript="true"/>";
   var ERR_INVALID_ACI_EFFECTIVE_DATE_EMPTY = "<content:getAttribute beanName='ERR_INVALID_ACI_EFFECTIVE_DATE_EMPTY' attribute='text'  filter='true' escapeJavaScript="true"/>";   
   var ERR_DEFERAL_MINIMUM_SHD_BE_LESSER_THAN_DEFERAL_MAXIMUM = "<content:getAttribute beanName='ERR_DEFERAL_MINIMUM_SHD_BE_LESSER_THAN_DEFERAL_MAXIMUM' attribute='text'  filter='true' escapeJavaScript="true"/>";  
   var ERR_MAXIMUM_CONTRIBUTION_FIRST_RADIOBUTTON = "<content:getAttribute beanName='ERR_MAXIMUM_CONTRIBUTION_FIRST_RADIOBUTTON' attribute='text'  filter='true' escapeJavaScript="true"/>";    
   var ERR_MAXIMUM_CONTRIBUTION_NEXT_RADIOBUTTON = "<content:getAttribute beanName='ERR_MAXIMUM_CONTRIBUTION_NEXT_RADIOBUTTON' attribute='text'  filter='true' escapeJavaScript="true"/>"; 
   var ERR_MAXIMUM_CONTRIBUTION_FIRST_PERCENTAGE = "<content:getAttribute beanName='ERR_MAXIMUM_CONTRIBUTION_FIRST_PERCENTAGE' attribute='text'  filter='true' escapeJavaScript="true"/>";  
   var ERR_MAXIMUM_CONTRIBUTION_NEXT_PERCENTAGE = "<content:getAttribute beanName='ERR_MAXIMUM_CONTRIBUTION_NEXT_PERCENTAGE' attribute='text'  filter='true' escapeJavaScript="true"/>"; 
   var ERR_MAXIMUM_CONTRIBUTION_RADIOBUTTON_SAME = "<content:getAttribute beanName='ERR_MAXIMUM_CONTRIBUTION_RADIOBUTTON_SAME' attribute='text'  filter='true' escapeJavaScript="true"/>";      
   var ERR_CONTRIBUTION_FORMULAS_RULE_TYPES = "<content:getAttribute beanName='ERR_CONTRIBUTION_FORMULAS_RULE_TYPES' attribute='text'  filter='true' escapeJavaScript="true"/>"; 

var ruleSets = Array();
var ruleSetObjects = Array();
var moneyTypesArray = Array();
var moneyTypesNameMap = Array();
var viewableRulesArray = Array();
<c:forEach items="${employerMoneyTypes}" var="moneyType" varStatus="moneyTypeStatus">
  moneyTypesArray.push("${moneyType.moneyTypeCode}");
  moneyTypesNameMap["${moneyType.moneyTypeCode}"] = Array();
  moneyTypesNameMap["${moneyType.moneyTypeCode}"]["longName"] = "${moneyType.longName}";
  moneyTypesNameMap["${moneyType.moneyTypeCode}"]["shortName"] = "${moneyType.shortName}";   
 <%-- 
  TODO: Commented since money type shortname is empty for employer money types and not empty for other employer MT
  moneyTypesArray.push("${moneyType.moneyTypeCode}");
  moneyTypesNameMap["${moneyType.moneyTypeCode}"] = Array();
  moneyTypesNameMap["${moneyType.moneyTypeCode}"]["longName"] = "${moneyType.longName}";  
  moneyTypesNameMap["${moneyType.moneyTypeCode}"]["shortName"] = "${moneyType.shortName}"; --%>
</c:forEach>
<c:forEach items="${pifDataForm.pifDataUi.viewableRules}" var="rule" varStatus="ruleStatus">
  viewableRulesArray.push("${rule}");
</c:forEach>
<c:choose>
<c:when test="${pifDataForm.editMode}">
var editMode = true;
var confirmMode = false;
</c:when>
<c:when test="${pifDataForm.confirmMode}">
var editMode = false;
var confirmMode = true;
</c:when>
<c:otherwise>
var editMode = false;
var confirmMode = false;
</c:otherwise>
</c:choose> 
<%--
var irsMaximum = ${pifDataForm.planInfoVO.contributions.irsAnnualRegularMaximumAmount};
--%>

function showOrHideAutomaticContributionEffectiveDate() {
  var enable = true;
  var aciAllowed = $("input[name='planInfoVO.contributions.aciAllowed']:checked").val();
  if (aciAllowed != "${planDataConstants.YES_CODE}") {
    enable = false;
  }
  var aciEffectiveDateSpanId = "#planDataUi_automaticContributionEffectiveDate_span";
  
  if (enable == false) {
    $(aciEffectiveDateSpanId).hide();
  } else {
    $(aciEffectiveDateSpanId).show();
  }
}

var s1 = "~1~";
var s2 = "~2~";
var s3 = "~3~";

var defaultRuleSet = s1+s1+s1+s1+s1+s1+s1+s1+s1+s1+s1;
 
$(document).ready(function() {
    showOrHideAutomaticContributionEffectiveDate();   

	$("input[name='pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralIrsApplies']").on("click", function() {
      if ($("input[name='pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralIrsApplies']:checked").val() == 'true') {
        $("input[name='pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMaxAmount']").val("");
        $("input[name='pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMaxAmount']").prop("disabled", true);
      } else {
        $("input[name='pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMaxAmount']").prop("disabled", false);
      }
    });

	var selectId = "#employeeDeferralElectionCode"
    $(selectId).on("change",function () {
    	var value = $(selectId).val();
 		if (value == "${deferralConstants.OTHER}") {
	    	$("#employeeDeferralElectionSelectedDay").show();
        	$("#employeeDeferralElectionSelectedMonths").show();
        	$("#selectDayDiv").show();
        	$("#selectMonthsDiv").show();
		} else {
			$("#employeeDeferralElectionSelectedDay").hide();
			$("#employeeDeferralElectionSelectedMonths").hide();
			$("#selectDayDiv").hide();
			$("#selectMonthsDiv").hide();
			
			if (value == "${deferralConstants.AS_OF_EACH_PAYROLL_PERIOD_CODE}") {
 			  if ($("#aciApplyDateId").val().trim() == "") {
		        $("#aciApplyDateId").val("${pifDataForm.planInfoVO.generalInformations.planYearEnd.plusOneNonLeapYear}");
			  }
			}
		}
	});

	$("input[name='planInfoVO.contributions.aciAllowed']").on("click", function() {
		if($("input[name='planInfoVO.contributions.aciAllowed']:checked").val() == "${planDataConstants.YES_CODE}" ) {
			$("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']").prop("disabled", false);
			$("input[name='pifDataUi.aciApplyDate']").prop("disabled", false);
			$("input[name='pifDataUi.annualIncrease']").prop("disabled", false);
			$("input[name='pifDataUi.maxAutomaticIncrease']").prop("disabled", false);

			//there are subfields dependent on this radio.  only do this on Yes_code, since for no, its disabled anyway.
			$("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']:checked").trigger('click');
			if (${pifDataForm.editMode}) {
				if (${disableFieldsForAuto}) {
					$("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']").prop("disabled", "true");
					$("input[name='pifDataUi.aciApplyDate']").prop("disabled",true);
					$("input[name='pifDataUi.annualIncrease']").prop("disabled", true);
					$("input[name='pifDataUi.maxAutomaticIncrease']").prop("disabled", true);
				}
			}	
		} else {
			$("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']").prop("disabled", true);
            $("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']").prop("checked", false);
			$("input[name='pifDataUi.aciAppliesToEffectiveDate']").prop("disabled",true);
			$("input[name='pifDataUi.aciAppliesToEffectiveDate']").val("");
			$("input[name='pifDataUi.aciApplyDate']").prop("disabled",true);
			$("input[name='pifDataUi.aciApplyDate']").val("");
			$("input[name='pifDataUi.aciEffectiveDate']").val("");
			$("input[name='pifDataUi.annualIncrease']").prop("disabled", true);
			$("input[name='pifDataUi.annualIncrease']").val("");
			$("input[name='pifDataUi.maxAutomaticIncrease']").prop("disabled", true);
			$("input[name='pifDataUi.maxAutomaticIncrease']").val("");
		}
		showOrHideAutomaticContributionEffectiveDate();
	});	

	$("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']").on("click", function(){
		//var aciEffectiveDateResult = validateEffectiveDateForACI();
		//if(!aciEffectiveDateResult){
		//	$("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']").prop("checked", false);
		//	return false;
		//}
		if ( $("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']:checked").val() == "${aciConstants.APPLY_TO_PPT_ALL_PARTICIPANT_CODE}" || 
				$("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']:checked").val() == "${aciConstants.APPLY_TO_PPT_AUTO_ENROLLED_PARTICIPANTS_CODE}" ) {
			$("input[name='pifDataUi.aciAppliesToEffectiveDate']").prop("disabled",true);
			$("input[name='pifDataUi.aciAppliesToEffectiveDate']").val("");
			$("#aciAppliesToEffectiveDateIdImage").prop("disabled", true);
		} else if ($("input[name='pifDataUi.aciAppliesToEffectiveDate']").val() !="" 
					&& $("input[name='planInfoVO.contributions.newParticipantApplyDateDisabled']").val() == 'true') {
			$("input[name='pifDataUi.aciAppliesToEffectiveDate']").prop("disabled",true);
			$("#aciAppliesToEffectiveDateIdImage").prop("disabled", true);
		} else {
			$("input[name='pifDataUi.aciAppliesToEffectiveDate']").prop("disabled",false);
			$("#aciAppliesToEffectiveDateIdImage").prop("disabled", false);
		}
			
	})	
	
	//formulas
	if ( ruleSets.length == 0 ) { 
		if ( moneyTypesArray.length == 0 ) { 
			$("#employerContibutionDiv").append('<content:getAttribute  escapeJavaScript="true" beanName="contributionNoMoneyTypes" attribute="text"/>');
			
		} else if ( editMode || confirmMode ) { 
			createContributionFormula()
		}
	} else {
		if ( editMode || confirmMode ) {
			for ( var i = 0 ; i < ruleSets.length ; i++ ) {
				$("#employerContibutionDiv").append(decodeRuleSet((i+1), ruleSets[i]));
				initializeRuleSet((i+1), ruleSets[i]);
			}
		} else {
			$("#employerContibutionDiv").append(decodeViewableSets(ruleSets));
			if ( i < ruleSets.length -1 ) 
				$("#employerContibutionDiv").append("<div>&nbsp;</div>");
		}
	}
	if ( editMode ) {
		setTimeout("assignHandlers()", 10);
		//assignHandlers();
		setTimeout("updateUiForEditMode()", 10);		
		//updateUiForEditMode();
	}
	
	if ( confirmMode ) {
		updateMoneyTypes();
		updateRemoveSpans();
		updateRuleTypes();
		$("#contributionsTabDivId input").prop("disabled", true);
		$("#contributionsTabDivId select").prop("disabled", true);
		$("#aciIncreaseEffectiveDateIdImage").prop("disalbed", true);
		$("#employeeDeferralElectionCode").trigger('change');
	} 

	for ( var i = 1 ; i <= ruleSets.length ; i++ ) {
		$("#" + i + "_max_match_type_error_container").prop('innerHTML', $("#" + i + "_max_match_type_error_content").prop('innerHTML'));		
		$("#" + i + "_first_percent_type_error_container").prop('innerHTML', $("#" + i + "_first_percent_type_error_content").prop('innerHTML'));		
		$("#" + i + "_next_percent_type_error_container").prop('innerHTML', $("#" + i + "_next_percent_type_error_content").prop('innerHTML'));		
		$("#" + i + "_next_percent_error_container").prop('innerHTML', $("#" + i + "_next_percent_error_content").prop('innerHTML'));		
		$("#" + i + "_type_error_container").prop('innerHTML', $("#" + i + "_type_error_content").prop('innerHTML'));		
		$("#" + i + "_money_type_error_container").prop('innerHTML', $("#" + i + "_money_type_error_content").prop('innerHTML'));		
	}
	if ( editMode ) {
		handleDeferralMaxPercentTextElement();
	}
  })
 
function isMoneyTypeInUse(moneyTypeid) {
	for ( var j = 0 ; j < ruleSets.length ; j++ ) {
		var vals = ruleSets[j].split(s1);
	    var moneyMap = vals[1].split(s2);
		for ( var i = 0 ; i < moneyMap.length ; i++ ) {
			var temp = moneyMap[i].split(s3); 
			if ( temp[0] == moneyTypeid && temp[1] == "true" ) return true;
		}
	}
	return false;
}

function updateUiForEditMode() {
//TPA Contribution
	$("#employeeDeferralElectionCode").trigger('change');
	$("input[name='planInfoVO.contributions.aciAllowed']:checked").trigger('click');	
	//$("input[name='planInfoVO.contributions.planDeferralLimits.deferralIrsApplies']:checked").trigger('click');

	updateMoneyTypes();
	updateRemoveSpans();
	updateRuleTypes();

	for ( var i = 1 ; i <= ruleSets.length ; i++ ) {
		//handle the types
		if ( $("input[name='" +i+ "_type']:checked").val() == "${ruleConstants.TYPE_PERCENT}" ) {
			$("#" + i + "non_match_div input").prop("disabled", true);
			$("#" + i + "non_match_div input").prop("value", "");
			$("#" + i + "_match_div input").prop("disabled", true);
			$("#" + i + "_match_div input").prop("checked", false);
			$("#" + i + "_match_div input[type='\"text\"']").prop("value", "");

		} else {
			if(editMode){
				$("#" + i + "non_match_div input").prop("disabled", false);
				$("#" + i + "_match_div input").prop("disabled", false);			
			}
		}
		
		//handle the first percent field
		var disabled = $("input[name='" + i + "_first_percent_value']").val() == "";
		$("input[name='" + i + "_next_percent']").prop("disabled", disabled)
		$("input[name='" + i + "_next_percent_type']").prop("disabled", disabled)
		$("input[name='" + i + "_next_percent_value']").prop("disabled", disabled)
		if ( disabled ) {
			$("input[name='" + i + "_next_percent']").val("");
			$("input[name='" + i + "_next_percent_value']").val("");
			
		}
	}
}
/*
this table summarizes how the data maps;
0, Rule Number, 
1, list of money types
2, getEmployerContributionMaxMatchAmount
3, getEmployerContributionMaxMatchPercent
4, getEmployerMatchContributionFirstPercent
5, getEmployerMatchContributionNextPercent
6, getEmployerMatchFirstEmployeeDeferralAmount
7, getEmployerMatchFirstEmployeeDeferralPercent
8, getEmployerMatchNextEmployeeDeferralAmount
9, getEmployerMatchNextEmployeeDeferralPercent
10, getNonElectiveContributionPercent
11, ruleCode
*/                    
function decodeRuleSet(rule, var1) {
	var vals = var1.split(s1);
	var mts = vals[1].split(s2);
	vals[0] = rule; //need to pass in the rule number and assign it, since things need to get reorderd when we delete a rule
          		
	var rs = "<table width=\"100%\" style=\"background-color: white; border-bottom:1px solid #CCCCCC\">";
	rs+="<tr><td style=\"vertical-align:top;\" id=\"moneyTypeTd" + vals[0] + "\"><span class=\"boldLabel\">Contribution Formula " + vals[0] + "</span><br>"
	rs+="<div>Money Types:<span id=\"" + vals[0] + "_money_type_error_container\"></span></div>";
	rs+="<div id=\"" + vals[0] + "_mt_container\">";
	for ( var i = 0 ; i < mts.length ; i++ ) {
		if ( mts[i] == "" ) continue;
		var moneyTypeId = mts[i].split("~3~")[0];
		//if it is not in the master map, then it is no longer applicable.
		if ( moneyTypesNameMap[moneyTypeId] != null ) 
			rs+=getMoneyTypeDiv( vals[0], moneyTypeId);	
	}
	
	if ( editMode ) {	
		rs+="</div></td><td>";
		rs+="<div><input type=\"hidden\" name=\"" + vals[0] + "_permRuleNum\" value=\"" + vals[12] + "\"><input type=\"radio\" name=\"" + vals[0] + "_type\" value=\"F\">Fixed<input type=\"radio\" name=\"" + vals[0] + "_type\" value=\"D\">Discretionary<input type=\"radio\" name=\"" + vals[0] + "_type\" value=\"P\">Prevailing wage contribution(Profit sharing only)<span id=\"" + vals[0] + "_type_error_container\"></span></div>";
		rs+="<table style=\"margin-top: 6px\"><tr><td style=\"padding:2px;\">";
		rs+="<div class=\"formulaHeader\" >&nbsp;Matching Contributions</div>";
		rs+="<div  class=\"formulaBody\" id=\"" + vals[0] + "_match_div\">"; //start match area div
		rs+="<table><tr><td>";
		rs+="<div style=\"padding-left:10px\"><input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_first_percent\"> % of the first"
		rs+="</td><td nowrap>"
		rs+="<input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_first_percent_value\"><input type=\"radio\" name=\"" + vals[0] + "_first_percent_type\" value=\"P\">%&nbsp;<input type=\"radio\" id=\"" + vals[0] + "_first_percent_typeId\" name=\"" + vals[0] + "_first_percent_type\" value=\"D\">$<span id=\"" + vals[0] + "_first_percent_type_error_container\"></span></div>";
		rs+="</td></tr><tr><td>";
		rs+="<div style=\"padding-left:10px\" id=\"" + vals[0] + "_next_div\"><input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_next_percent\"> % of the next<span id=\"" + vals[0] + "_next_percent_error_container\">"
		rs+="</td><td>"
		rs+="<input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_next_percent_value\"><input type=\"radio\" name=\"" + vals[0] + "_next_percent_type\" value=\"P\">%&nbsp;<input type=\"radio\" id=\"" + vals[0] + "_next_percent_typeId\" name=\"" + vals[0] + "_next_percent_type\" value=\"D\">$<span id=\"" + vals[0] + "_next_percent_type_error_container\"></span></div>";
		rs+="</td></tr></table>"
		rs+="<div style=\"border-top:1px solid #999999;padding-left:15px\">"
		rs+="<table cellpadding=\"0\" cellspacing=\"0\"><tr><td rowspan=\"2\">Maximum match: <input size=\"8\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_max_match\"></td><td><input type=\"radio\" name=\"" + vals[0] + "_max_match_type\" value=\"D\">$</td><td rowspan=\"2\"><span id=\"" + vals[0] + "_max_match_type_error_container\"></span></td></tr><tr><td><input type=\"radio\" name=\"" + vals[0] + "_max_match_type\" value=\"P\">% of compensation&nbsp;&nbsp;</td></tr></table></div>";
		rs+="</div>"; //end match area div
		rs+="</td><td style=\"vertical-align:top;\">--or--</td><td style=\"vertical-align:top;\">";
		rs+="<div class=\"formulaHeader\">&nbsp;Non-elective Contributions</div>";
		rs+="<div class=\"formulaBody\" id=\"" + vals[0] + "non_match_div\">"; //start non-match area div
		rs+="<div  style=\"padding-left:10px; padding-top:2px;padding-bottom:2px;\"><input size=\"5\" class=\"numericInput\" type=\"text\" name=\"" + vals[0] + "_ne_percent\">&nbsp;% of compensation&nbsp;&nbsp;</div>";
		rs+= "</div>" //end non-match area div
		rs+="</td></tr></table>";
		rs +="</td></tr><tr><td colspan=\"4\" align=\"right\"><span id=\"" + vals[0] + "_remove_span\">Remove Contribution Formula " + vals[0] + "</span></td></tr></table>";
	}
	if ( confirmMode ) {	
		rs+="</div></td><td>";
		rs+="<div><input type=\"radio\" name=\"" + vals[0] + "_type\" value=\"F\" disabled>Fixed<input type=\"radio\" name=\"" + vals[0] + "_type\" value=\"D\" disabled>Discretionary<input type=\"radio\" name=\"" + vals[0] + "_type\" value=\"P\" disabled>Prevailing wage contribution(Profit sharing only)<span id=\"" + vals[0] + "_type_error_container\"></span></div>";
		rs+="<table style=\"margin-top: 6px\"><tr><td style=\"padding:2px;\">";
		rs+="<div class=\"formulaHeader\" >&nbsp;Matching Contributions</div>";
		rs+="<div  class=\"formulaBody\" id=\"" + vals[0] + "_match_div\">"; //start match area div
		rs+="<table><tr><td>";
		rs+="<div style=\"padding-left:10px\"><input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_first_percent\" disabled> % of the first"
		rs+="</td><td nowrap>"
		rs+="<input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_first_percent_value\" disabled><input type=\"radio\" name=\"" + vals[0] + "_first_percent_type\" value=\"P\" disabled>%&nbsp;<input type=\"radio\" name=\"" + vals[0] + "_first_percent_type\" value=\"D\" disabled>$<span id=\"" + vals[0] + "_first_percent_type_error_container\"></span></div>";
		rs+="</td></tr><tr><td>";
		rs+="<div style=\"padding-left:10px\"><input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_next_percent\" disabled> % of the next<span id=\"" + vals[0] + "_next_percent_error_container\">"
		rs+="</td><td>"
		rs+="<input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_next_percent_value\" disabled><input type=\"radio\" name=\"" + vals[0] + "_next_percent_type\" value=\"P\" disabled>%&nbsp;<input type=\"radio\" name=\"" + vals[0] + "_next_percent_type\" value=\"D\" disabled>$<span id=\"" + vals[0] + "_next_percent_type_error_container\"></span></div>";
		rs+="</td></tr></table>"
		rs+="<div style=\"border-top:1px solid #999999;padding-left:15px\">"
		rs+="<table cellpadding=\"0\" cellspacing=\"0\"><tr><td rowspan=\"2\">Maximum match: <input size=\"8\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_max_match\" disabled></td><td><input type=\"radio\" name=\"" + vals[0] + "_max_match_type\" value=\"D\" disabled>$</td><td rowspan=\"2\"><span id=\"" + vals[0] + "_max_match_type_error_container\"></span></td></tr><tr><td><input type=\"radio\" name=\"" + vals[0] + "_max_match_type\" value=\"P\" disabled>% of compensation&nbsp;&nbsp;</td></tr></table></div>";
		rs+="</div>"; //end match area div
		rs+="</td><td style=\"vertical-align:top;\">--or--</td><td style=\"vertical-align:top;\">";
		rs+="<div class=\"formulaHeader\">&nbsp;Non-elective Contributions</div>";
		rs+="<div class=\"formulaBody\" id=\"" + vals[0] + "non_match_div\">"; //start non-match area div
		rs+="<div  style=\"padding-left:10px; padding-top:2px;padding-bottom:2px;\"><input size=\"5\" class=\"numericInput\" type=\"text\" name=\"" + vals[0] + "_ne_percent\" disabled>&nbsp;% of compensation&nbsp;&nbsp;</div>";
		rs+= "</div>" //end non-match area div
		rs+="</td></tr></table>";
		rs +="</td></tr></table>";  
	}
	return rs;
}
/*
this table summarizes how the data maps;
0, Rule Number, 
1, list of money types
2, getEmployerContributionMaxMatchAmount
3, getEmployerContributionMaxMatchPercent
4, getEmployerMatchContributionFirstPercent
5, getEmployerMatchContributionNextPercent
6, getEmployerMatchFirstEmployeeDeferralAmount
7, getEmployerMatchFirstEmployeeDeferralPercent
8, getEmployerMatchNextEmployeeDeferralAmount
9, getEmployerMatchNextEmployeeDeferralPercent
10, getNonElectiveContributionPercent
11, ruleCode
*/                    

function decodeViewableSets(ruleSets) {
	var rs = "<table width=\"100%\" class=\"dataTable\" style=\"border-bottom:1px solid #CCCCCC\"><tr><td class=\"formulaFirstColumn boldlabel dataColumn\" nowrap>Money Type</td><td class=\"dataColumn boldlabel\">Contribution Formula</td></tr>";
	var dataArray = Array();
	var keys = Array();
	
	for ( var i = 0 ; i < ruleSets.length ; i++ ) {
		var rule = ruleSets[i];
		var vals = ruleSets[i].split(s1);
		var mts = vals[1].split(s2);
		vals[0] = rule; //need to pass in the rule number and assign it, since things need to get reorderd when we delete a rule
		for ( var j = 0 ; j < mts.length ; j++ ) {
			if ( mts[j] == "" ) continue;
			var moneyType = mts[j].split("~3~");
			if ( moneyType[1] == "true" ) {
				var key = moneyTypesNameMap[moneyType[0]]["shortName"]
				if ( dataArray[key] == null ) dataArray[key] = Array();
				dataArray[key][0] = moneyTypesNameMap[moneyType[0]]["shortName"];
				dataArray[key][1] = viewableRulesArray[i];
				dataArray[key][2] = moneyTypesNameMap[moneyType[0]]["longName"];
				keys.push(key);
			}
		}
	}

	keys.sort();
	var evenRow = false;
	for ( var i = 0 ; i < keys.length ; i++ ) {
		rs+= "<tr class=\"" + (evenRow ? "eventDataRow" : "oddDataRow") + "\"><td class=\"formulaFirstColumn dataColumn\" title=\"" + dataArray[keys[i]][2] + "\">" 
		rs+=  dataArray[keys[i]][0] + "</td><td class=\"dataColumn\">" + dataArray[keys[i]][1]  + "</td></tr>";
		evenRow = !evenRow;
		
	}	
	rs +="</table>";
	return rs;
}

function encodeRuleSets() {
	var ruleSets = getEncodedRuleSets();
	for ( var i = 0 ; i < ruleSets.length ; i++ ) {
		$("#employerContibutionDiv").append("<input type=\"hidden\" name=\"pifDataUi.employerRulesets[" + i + "]\" value=\"" + ruleSets[i] + "\">");
	}
	$("input[name='planInfoVO.contributions.contributionsFormulaCount']").prop("value", ruleSets.length);
}
	/*
	this table summarizes how the data maps;
	0, Rule Number, 
	1, list of money types
	2, getEmployerContributionMaxMatchType
	3, getEmployerContributionMaxMatchValue
	4, getEmployerMatchContributionFirstPercent
	5, getEmployerMatchContributionNextPercent
	6, getEmployerMatchFirstEmployeeDeferralType
	7, getEmployerMatchFirstEmployeeDeferralValue
	8, getEmployerMatchNextEmployeeDeferralType
	9, getEmployerMatchNextEmployeeDeferralValue
	10, getNonElectiveContributionPercent
	11, ruleCode
	*/                    
function getEncodedRuleSets() {	  
                   	var rs = Array();
		for ( var j = 0 ; j < ruleSets.length ; j++ ) {
			var ruleNum = j+1;
			//lets look for some rule data for rule 1
			//found a rule
			var string = "";
			//rule number
			string += ruleNum + s1;
			var moneyTypes = document.getElementsByName(ruleNum + "_mt");
			//money types
			for ( var i = 0 ; i < moneyTypes.length ; i++ ) {
				string += moneyTypes[i].value + s3 + moneyTypes[i].checked;
				if ( i < moneyTypes.length -1 ) string += s2;
			}
			string += s1;
			//lets see if its matching
			if ( $("input[name='" + ruleNum + "_max_match_type']:checked").length > 0 ) 
				string += $("input[name='" + ruleNum + "_max_match_type']:checked").val() + s1;
			else
				string += s1;
				
			string += $("input[name='" + ruleNum + "_max_match']").val() + s1;
			string += $("input[name='" + ruleNum + "_first_percent']").val() + s1;
			string += $("input[name='" + ruleNum + "_next_percent']").val() + s1;
			if ( $("input[name='" + ruleNum + "_first_percent_type']:checked").length > 0 ) 
				string += $("input[name='" + ruleNum + "_first_percent_type']:checked").val() + s1;
			else
				string += s1;
			string += $("input[name='" + ruleNum + "_first_percent_value']").val() + s1;
			if ( $("input[name='" + ruleNum + "_next_percent_type']:checked").length > 0 ) 
				string += $("input[name='" + ruleNum + "_next_percent_type']:checked").val() + s1;
			else
				string += s1;
			string += $("input[name='" + ruleNum + "_next_percent_value']").val() + s1;
			string += $("input[name='" + ruleNum + "_ne_percent']").val() + s1;
			if ( $("input[name='" + ruleNum + "_type']:checked").length > 0 ) 
				string += $("input[name='" + ruleNum + "_type']:checked").val() + s1;
			else
				string += s1;
			if ( $("input[name='" + ruleNum + "_permRuleNum']").val() != 'undefined' ) {
				string += $("input[name='" + ruleNum + "_permRuleNum']").val();
			}else{
				string +="";
			}
			rs.push(string)
		} 
		return rs;
}
function createContributionFormula() {
	var result = validateMoneyTypeForRuleTypes();
	if(!result){
		return false;
	}
	var newRuleSet = (ruleSets.length+1) + defaultRuleSet; //this just increments the counter by 1.
	ruleSets.push(newRuleSet);
	$("#employerContibutionDiv").append(decodeRuleSet(ruleSets.length, newRuleSet));
	initializeRuleSet(ruleSets.length, newRuleSet);
	//assignHandlers();
	setTimeout("assignHandlers()", 10);
	//updateUiForEditMode();
	setTimeout("updateUiForEditMode()", 10);
}
function assignHandlers() {
//FOR TPA Contribution
	for ( var i = 0 ; i < ruleSets.length ; i++ ) {
    	//money types
		$("input[name='" + (i+1) + "_mt']").on("click", function() {
			updateMoneyTypes();
		})
		//type's
		$("input[name='" + (i+1) + "_type']").on('click', (i+1), function(e){
            updateUiForEditMode();
    	})
		$("input[name='" + (i+1) + "_first_percent']").on('blur', (i+1), function(e){
            validateFirstPercent(this);
    	})
		$("input[name='" + (i+1) + "_next_percent']").on('blur', (i+1), function(e){
			validateNextPercent(this);
    	})
		
		$("input[name='" + (i+1) + "_next_percent']").on('focus', (i+1), function(e){

			if ( $("input[name='" + e.data + "_first_percent_type']:checked").length <= 0 ) {	
				var firstPercentResult = validateFirstPercentValueWithType(e.data);
				if(!firstPercentResult){
					return false;
				}
			}
        });		
    	
		$("input[name='" + (i+1) + "_first_percent_value']").on('blur', (i+1), function(e){

			if($("input[name='" + e.data + "_first_percent_value']").val() != ''){			
				var result = validatePIFFirstPercent(e.data,this);
				if(!result){
					return false;
				}
			}
			if ( $("input[name='" + e.data + "_first_percent_type']:checked").length > 0 ) {				
				if ( $("input[name='" + e.data + "_first_percent_type']:checked").val() == "${ruleConstants.TYPE_PERCENT}" ) {
	            	validateFirstOfFirstPercent(this);
				}	else {
					validateFirstOfFirstAmount(this);
				} 
			}
            updateUiForEditMode();
    	})
		
		 $("#"+(i+1) + "_next_percent_typeId").on('blur', (i+1), function(e){

			if ( $("input[name='" + e.data + "_next_percent_type']:checked").length <= 0 ) {	
				var nextPercentResult = validateNextPercentValueWithType(e.data);
				if(!nextPercentResult){
					return false;
				}
			}
        });			
		
		
		$("input[name='" + (i+1) + "_first_percent_type']").on('click', (i+1), function(e){
			
			if ( $("input[name='" + e.data + "_first_percent_type']:checked").length > 0 ) {
				if ( $("input[name='" + e.data + "_first_percent_type']:checked").val() == "${ruleConstants.TYPE_PERCENT}" ) {
	            	validateFirstOfFirstPercent($("input[name='" + e.data + "_first_percent_value']")[0]);
				}	else {
	            	validateFirstOfFirstAmount($("input[name='" + e.data + "_first_percent_value']")[0]);
				} 
			}
    	})
		$("input[name='" + (i+1) + "_next_percent_value']").on('blur', (i+1), function(e){

			if($("input[name='" + e.data + "_next_percent_value']").val() != ''){			
				var nextResult = validatePIFNextPercent(e.data,this);
				if(!nextResult){
					return false;
				}
			}		
			if ( $("input[name='" + e.data + "_next_percent_type']:checked").length > 0 ) {	
				if ( $("input[name='" + e.data + "_next_percent_type']:checked").val() == "${ruleConstants.TYPE_PERCENT}" ) {
					validateNextOfPercent(this);
				}	else {
					validateNextOfAmount(this);
				} 
			}
    	})
		$("input[name='" + (i+1) + "_next_percent_type']").on('click', (i+1), function(e){
			
			if ( $("input[name='" + e.data + "_next_percent_type']:checked").length > 0 ) {
			
				if ( $("input[name='" + e.data + "_next_percent_type']:checked").val() == "${ruleConstants.TYPE_PERCENT}" 
					&& $("input[name='" + e.data + "_first_percent_type']:checked").val() != "${ruleConstants.TYPE_PERCENT}") {
					alert(ERR_MAXIMUM_CONTRIBUTION_RADIOBUTTON_SAME);
					return false;
				}	else if ( $("input[name='" + e.data + "_next_percent_type']:checked").val() == "${ruleConstants.TYPE_DOLLAR}" 
					&& $("input[name='" + e.data + "_first_percent_type']:checked").val() != "${ruleConstants.TYPE_DOLLAR}"){
					alert(ERR_MAXIMUM_CONTRIBUTION_RADIOBUTTON_SAME);
					return false;
				}
				
				if ( $("input[name='" + e.data + "_next_percent_type']:checked").val() == "${ruleConstants.TYPE_PERCENT}" ) {
					validateNextOfPercent($("input[name='" + e.data + "_next_percent_value']")[0]);
				}	else {
					validateNextOfAmount($("input[name='" + e.data + "_next_percent_value']")[0]);
				} 
			}
    	})    	
		$("input[name='" + (i+1) + "_max_match']").on('blur', (i+1), function(e){
			
			if ( $("input[name='" + e.data + "_max_match_type']:checked").length > 0 ) {
				if ( $("input[name='" + e.data + "_max_match_type']:checked").val() == "${ruleConstants.TYPE_PERCENT}" ) {
					validateMaxMatchPercent(this);
				}	else {
					validateMaxMatchAmount(this);
				} 
			}
    	})
		$("input[name='" + (i+1) + "_max_match_type']").on('click', (i+1), function(e){
			
			if ( $("input[name='" + e.data + "_max_match_type']:checked").length > 0 ) {
				if ( $("input[name='" + e.data + "_max_match_type']:checked").val() == "${ruleConstants.TYPE_PERCENT}" ) {
					validateMaxMatchPercent($("input[name='" + e.data + "_max_match']")[0]);
				}	else {
					validateMaxMatchAmount($("input[name='" + e.data + "_max_match']")[0]);
				} 
			}
    	})    	
		$("input[name='" + (i+1) + "_ne_percent']").on('blur', (i+1), function(e){
			validateNonElectiveContributionPercent($("input[name='" + e.data + "_ne_percent']")[0]);
    	})
	}
}

function updateRuleTypes() {
	var foundPrevailing = false;
	for ( var i = 1 ; i <= ruleSets.length ; i++ ) {
		if ( $("input[name='" + i + "_type']:checked").val() == "${ruleConstants.PREVAILING}" ) {
			foundPrevailing = true;
			for ( var j = 1 ; j <= ruleSets.length ; j++ ) {
				if ( i == j ) continue;
				$("input[name='" + j +  "_type'][value=${ruleConstants.PREVAILING}]").prop("disabled", true);
			}
		} 
	}
	if ( !foundPrevailing ) {
		for ( var j = 1 ; j <= ruleSets.length ; j++ ) {
			if(editMode){
				$("input[name='" + j + "_type'][value=${ruleConstants.PREVAILING}]").prop("disabled", false);			
			}
		}
		
	}
}

function format (num) {

	var result = num;
	
	/*
	 * Remove trailing zeros first.
	 */
	 if(! isNaN(num)) {
			var decimalIndex = num.indexOf(".");
			if (decimalIndex != -1) {
				for (var i = num.length - 1; i > decimalIndex; i--) {
					if (num.charAt(i) != '0') {
						break;
					}
				}
				/*
				 * if we reach the decimal place, we just remove the dot.
				 */
				if (i != decimalIndex) {
					i++;
				}
				
				if (i != num.length) {
					result = num.substring(0, i);
				}
			}
		}

	return result;
}
   /*
this table summarizes how the data maps;
0, Rule Number, 
1, list of money types
2, getEmployerContributionMaxMatchType
3, getEmployerContributionMaxMatchValue
4, getEmployerMatchContributionFirstPercent
5, getEmployerMatchContributionNextPercent
6, getEmployerMatchFirstEmployeeDeferralType
7, getEmployerMatchFirstEmployeeDeferralValue
8, getEmployerMatchNextEmployeeDeferralType
9, getEmployerMatchNextEmployeeDeferralValue
10, getNonElectiveContributionPercent
11, ruleCode      
*/    
                    
function initializeRuleSet(ruleNum, ruleSet) {
	var vals = ruleSet.split(s1);
	vals[0] = ruleNum;
    var moneyMap = vals[1].split(s2);
	//first lets set the money type boxes to checked if they are
	//and also lets set the values
	for ( var i = 0 ; i < moneyMap.length ; i++ ) {
		var moneyTypeId = moneyMap[i].split(s3)[0];
		var checked = moneyMap[i].split(s3)[1];
		var id = "input[name='" + vals[0] + "_mt'][value='" + moneyTypeId + "']";
		$(id).prop("checked", checked == "true" ? true : false);
	}
	//second, lets set the rule code
	$("input[name='" + vals[0] + "_type'][value='" + vals[11] + "']").prop("checked", true);
	$("input[name='" + vals[0] + "_max_match_type'][value='" + vals[2] + "']").prop("checked", true);
	$("input[name='" + vals[0] + "_max_match']").val(format(vals[3]));
	$("input[name='" + vals[0] + "_first_percent']").prop("value", format(vals[4]));
	$("input[name='" + vals[0] + "_next_percent']").prop("value", format(vals[5]));
	$("input[name='" + vals[0] + "_first_percent_type'][value='" + vals[6] + "']").prop("checked", true);
	$("input[name='" + vals[0] + "_first_percent_value']").val(format(vals[7]));
	$("input[name='" + vals[0] + "_next_percent_type'][value='" + vals[8] + "']").prop("checked", true);
	$("input[name='" + vals[0] + "_next_percent_value']").val(format(vals[9]));
    $("input[name='" + vals[0] + "_ne_percent']").prop("value", vals[10]);
	$("input[name='" + vals[0] + "_permRuleNum']").val(vals[12]);
}
function updateMoneyTypes() {
		//we have a global array called moneyTypeArrays, that contains all of the money types.
		//now, we can only show a money type checkbox if the money type is not in use by another rule
		//and we need to sort the money types 
		//therefore, create a map called
		//moneyTypesInUseMap - map of rule to id ( this implies the money types are selected )
		//we need to examine the html to determine which ones are selected.
		//once we have this stored we need to rewrite the money type div container
		//so that we can sort the money types by name
								
		var moneyTypesInUseMap = Array();
		var moneyTypesMap = Array();
		//first iteration
		for ( var j = 1 ; j <= ruleSets.length ; j++ ) {
			var ruleNum = j;
			var moneyTypes = document.getElementsByName(ruleNum + "_mt");
			for ( var i = 0 ; i < moneyTypes.length ; i++ ) {
				if ( moneyTypesInUseMap[ruleNum] == null ) {
					moneyTypesInUseMap[ruleNum] = Array();
				}
				if ( moneyTypes[i].checked )
					moneyTypesInUseMap[ruleNum].push(moneyTypes[i].value);
			}
		}
		for ( var i = 1 ; i <= ruleSets.length ; i++ ) {
			//blank out the container
			$("#" + i + "_mt_container").prop("innerHTML", "");
			//rebuild the container by iterating over the master money type list, which is sorted
			for (var j = 0 ; j < moneyTypesArray.length ; j++ ) {
				var ruleInUse = getRuleForInUseMoneyType(moneyTypesInUseMap, i, moneyTypesArray[j]);
				if ( ruleInUse == -1 || (ruleInUse > -1 && ruleInUse == i )) {
					//its either not in use by a rule, or its in use by our rule
					$("#" + i + "_mt_container").append(getMoneyTypeDiv(i, moneyTypesArray[j]));
					//next, tick the checkbox if required
					if ( moneyTypesInUseMap[i] != null ) {
						for ( var k = 0 ; k < moneyTypesInUseMap[i].length ; k++ ) {
							if ( moneyTypesInUseMap[i][k] == moneyTypesArray[j] ) {
								var id = "input[name='" + i + "_mt'][value='" + moneyTypesArray[j] + "']";
								$(id).prop("checked", true);
							}
						}
					}
				}
			}
			//next, reassign the handler
			$("input[name='" + i + "_mt']").on("click", function() {
				updateMoneyTypes();
			})
									
		}
		//finally, need to disable the createForumalSpan if all money types are in use
		// count the number of money types in use
		var count = 0;
		for ( var i = 0 ; i < ruleSets.length ; i++ ) {
			if ( moneyTypesInUseMap[i+1] != null ) 
				count += moneyTypesInUseMap[i+1].length;
		} 
		if ( count == moneyTypesArray.length ) {
			//disable the span
			$("#createFormulaSpanOn").hide();
			$("#createFormulaSpanOff").show();
			
		} else {
			$("#createFormulaSpanOn").show();
			$("#createFormulaSpanOff").hide();
		}
		
}
//return true if money type is in use for a rule number other than the current rule
function getRuleForInUseMoneyType(moneyTypesInUseMap, ruleNum, moneyType) {
	for ( var i = 0 ; i < ruleSets.length ; i++ ) {
		if ( moneyTypesInUseMap[i+1] != null ) {
			for ( var j = 0 ; j < moneyTypesInUseMap[i+1].length ; j++ ) {
				if ( moneyType == moneyTypesInUseMap[i+1][j] ) {
					return (i+1); //the rule
				}
			}
		}
	}
	return -1;
}
function getMoneyTypeDiv(rule, moneyTypeId) {
	if ( editMode ) {
	return "<div title=\""+ moneyTypesNameMap[moneyTypeId]["longName"] +"("+ moneyTypesNameMap[moneyTypeId]["shortName"] +")"+ "\" id=\"" + rule + "_mt_" + moneyTypeId + "_div\"><input type=\"checkbox\" name=\"" + rule + "_mt\" value=\"" + moneyTypeId + "\">" + moneyTypesNameMap[moneyTypeId]["shortName"] + "</div>"; 
	}
	if ( confirmMode ) {
	return "<div title=\""+ moneyTypesNameMap[moneyTypeId]["longName"] +"("+ moneyTypesNameMap[moneyTypeId]["shortName"] +")"+ "\" id=\"" + rule + "_mt_" + moneyTypeId + "_div\"><input type=\"checkbox\" name=\"" + rule + "_mt\" value=\"" + moneyTypeId + "\" disabled>" + moneyTypesNameMap[moneyTypeId]["shortName"] + "</div>"; 
	}	
}
function updateRemoveSpans() {
	for ( var i = 0 ; i < ruleSets.length ; i++ ) {
		if ( ruleSets.length > 1 ) {
			$("#" + (i+1) + "_remove_span").css("text-decoration", "underline");
			$("#" + (i+1) + "_remove_span").css("color", "#000099");
			$("#" + (i+1) + "_remove_span").mouseover(function(){
				this.style.cursor = 'hand'
			})
			$("#" + (i+1) + "_remove_span").off('click');
			$("#" + (i+1) + "_remove_span").on('click', (i+1), function(e){
				deleteFormula(e);
			})
		} else {
			$("#" + (i+1) + "_remove_span").css("text-decoration", "underline");
			$("#" + (i+1) + "_remove_span").css("color", "gray");
			$("#" + (i+1) + "_remove_span").off('mouseover');
			$("#" + (i+1) + "_remove_span").off('click');
		}
	}
}
function deleteFormula(e) {

	var encRuleSets = getEncodedRuleSets();
	var newRuleSet = Array();
	for (var i = 0 ; i < ruleSets.length ; i++ ) {
		if ( e.data != (i+1) )
			newRuleSet.push(encRuleSets[i]);
	}
	//assign the global variable.
	ruleSets = newRuleSet;
	$("#employerContibutionDiv").prop("innerHTML", "");
	//init the rulesets
	for ( var i = 0 ; i < ruleSets.length ; i++ ) {
		$("#employerContibutionDiv").append(decodeRuleSet((i+1), ruleSets[i]));
		initializeRuleSet( (i+1), ruleSets[i]);
	}
	//assignHandlers();
	setTimeout("assignHandlers()", 10);
	//updateUiForEditMode();
	setTimeout("updateUiForEditMode()", 10);	
}

function handleDeferralMaxPercentTextElement(){
	var deferralMaxPercent = document.getElementById("planDataUi_deferralMaxPercent");
	var deferralMaxPercentTextHidden = document.getElementById("planDataUi_deferralMaxPercent_textHidden");
	var deferralMaxPercentText = document.getElementById("planDataUi_deferralMaxPercent_text");
	var deferralMaxPercentId = "#planDataUi_deferralMaxPercent";

	if($(deferralMaxPercentId).is(':checked')){
	  	deferralMaxPercentTextHidden.value = deferralMaxPercent.value;
		document.getElementById("planDataUi_deferralMaxPercent_text").value = "";
		document.getElementById("planDataUi_deferralMaxPercent_text").disabled = true;
	}else{
		deferralMaxPercentTextHidden.value = deferralMaxPercentText.value;
		//document.getElementById("planDataUi_deferralMaxPercent_text").value = "";
		document.getElementById("planDataUi_deferralMaxPercent_text").disabled = false;
	}
}

function handleDeferralMaxPercentChange(){
	var deferralMaxPercentText = document.getElementById("planDataUi_deferralMaxPercent_text");
	var deferralMaxPercentTextHidden = document.getElementById("planDataUi_deferralMaxPercent_textHidden");
	deferralMaxPercentTextHidden.value = deferralMaxPercentText.value;
}

function validateWholeNumber(field) {
	
	var lowDigitNumber = 1;
	var num = field.value;
	if(num == ""){
		return false;
	}else if(isNaN(num)) {
        alert("The value should be a whole number.");
        field.value="";
        field.select();
        return false;
	} else if (num.indexOf(".") != -1 || (num < lowDigitNumber)) {
        alert("The value should be a whole number.");
        field.value="";
        field.select();
        return false;
	}
}

function validateSalaryDeferalElectionDay() {
	
	var selectedDay = document.getElementById("employeeDeferralElectionSelectedDay").value;
	
    if (selectedDay.length == 0) {
        alert(ERR_EMPTY_SALARY_DEFERAL_ELECTION_DAY);
        return false;
	} else {
        return true;
	}	
}

function validateSalaryDeferalElectionMonth() {
	if ( $("input[name='pifDataUi.selectedDeferralElectionMonths']:checked").length <= 0 ) {
        alert(ERR_EMPTY_SALARY_DEFERAL_ELECTION_MONTH);
        return false;
	} else {
        return true;
	}
}

function validateDateWithACIDate(){

	var aciDateElement = document.getElementById("aciEffectiveDateId");
	var aciDate = getDate(aciDateElement.value);
	if ( $("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']:checked").val() == 
			"${aciConstants.APPLY_TO_PPT_NEW_PARTICIPANT_CODE}"){
		var selectedACIAppliedDate =  $("input[name='pifDataUi.aciAppliesToEffectiveDate']").val();
		var thisDate = getDate(selectedACIAppliedDate);
		if (!thisDate) {
			alert(ERR_INVALID_ACI_EFFECTIVE_DATE_EMPTY);
			return false;
		}else if (aciDate == null){
			alert(ERR_INVALID_ACI_EFFECTIVE_DATE);
			return false;
		}
		if(thisDate.getTime() < aciDate.getTime()){
			alert(ERR_INVALID_ACI_EFFECTIVE_DATE_SHD_BE_GREATER);
			return false;		
		}else{
			return true;
		}
		return thisDate.getTime() < aciDate.getTime();					
	}else if($("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']:checked").val() != ""){
		if (aciDate == null){
			alert(ERR_INVALID_ACI_EFFECTIVE_DATE);
			return false;
		}else{
			return true;
		}
	}else{
		return true;
	}
}

function validateDeferalMinAndMaxField(field,field1) {
	var deferalMaximumField = document.getElementById(field1);
	var deferalMaximumValue = (+deferalMaximumField.value);
	var deferalMinimumValue = (+field.value);
	if ( deferalMaximumValue <  deferalMinimumValue) {
        alert(ERR_DEFERAL_MINIMUM_SHD_BE_LESSER_THAN_DEFERAL_MAXIMUM);
		field.value="";
        field.select();
        return false;
	} else {
        return true;
	}
}

function validateFirstPercentValueWithType(count){
	if($("input[name='" + count + "_first_percent_value']").val() != ''){
		alert(ERR_MAXIMUM_CONTRIBUTION_FIRST_RADIOBUTTON);
		//$("input[name='" + count + "_first_percent_value]").val('');
		$("input[name='" + count + "_first_percent_value']").select()
		return false;	
	}else{
		return true;	
	}
}

function validateNextPercentValueWithType(count){
	if($("input[name='" + count + "_next_percent_value']").val() != ''){	
		alert(ERR_MAXIMUM_CONTRIBUTION_NEXT_RADIOBUTTON);
		//$("input[name='" + count + "_next_percent_value]").val('');
		$("input[name='" + count + "_next_percent_value']").select();
		return false;
	}else{
		return true;	
	}
} 

function validatePIFFirstPercent(ruleNum,field){
	if($("input[name='" + ruleNum + "_first_percent']").val() == ''){
		alert(ERR_MAXIMUM_CONTRIBUTION_FIRST_PERCENTAGE);	
		field.value = "";
		$("input[name='" + ruleNum + "_first_percent']").select();
		return false;
	}else{
		return true;
	}
}

function validatePIFNextPercent(ruleNum,field){
	if($("input[name='" + ruleNum + "_next_percent']").val() == ''){
		alert(ERR_MAXIMUM_CONTRIBUTION_NEXT_PERCENTAGE);	
		field.value = "";
		$("input[name='" + ruleNum + "_next_percent']").select();
		return false;
	}else{
		return true;
	}
}

function validateEffectiveDateForACI(){
	if ($("input[name='pifDataUi.aciEffectiveDate']").val() == '' ){
		alert("Enter the value for ACI effective date.");
		return false;
	}else{
		return true;
	}
}

function validateMoneyTypeForRuleTypes(){ 
	//Validate for any selected money types on clicking the rule types
	for ( var j = 1 ; j <= ruleSets.length ; j++ ) {
		if ( $("input[name='" +j+ "_type']:checked").length > 0 ) {		
			var string = false;
			var moneyTypes = document.getElementsByName(j+ "_mt");
			//money types
			for ( var i = 0 ; i < moneyTypes.length ; i++ ) {
				//alert("Money types Value: "+moneyTypes[i].value+", Checked: "+moneyTypes[i].checked);
				if ( moneyTypes[i].checked){
					string = true;
					break;
				}
			}
			if(!string){
				alert(ERR_CONTRIBUTION_FORMULAS_RULE_TYPES);
				return false;
			}
			
		}
	}
	return true;
} 

//Validate the Contribution Formulas Section Types on save or submit
function validateContributionFormulaSectionTypes(){

	for ( var i = 0 ; i < ruleSets.length ; i++ ) {
		if($("input[name='" + (i+1) + "_first_percent_value']").val() != ''){
			if ( $("input[name='" + (i+1) + "_first_percent_type']:checked").length <= 0 ) {
				var firstPercentResult = validateFirstPercentValueWithType((i+1));
				if(!firstPercentResult){
					return false;
				}
			}	
			if($("input[name='" + (i+1) + "_next_percent_value']").val() != ''){
				if ( $("input[name='" + (i+1) + "_next_percent_type']:checked").length <= 0 ) {
					nextPercentResult = validateNextPercentValueWithType((i+1));
					if(!nextPercentResult){
						return false;
					}				
				}
			}
		
		}
	}
	return true;
}

</script>

<div id="contributionTabDivId" class="borderedDataBox">
<!--start table content -->
<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
  <TBODY>
	<TR><TD class=subhead colspan="2">	
		<DIV class=sectionTitle>
			<c:if test="${pifDataForm.confirmMode}">
				<content:getAttribute beanName="contributionText" attribute="text"/>
			</c:if>
		</DIV>
	</TD></TR>	  
  <TR>
    <TD width="100%"><!--[if lt IE 7]>
  <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
<![endif]-->
		<table width="729" class="dataTable">
		<TR><TD class=subsubhead>Employee Contributions</TD></TR>
		</table>      
      <DIV class=evenDataRow>
      <TABLE width="100%" class=dataTable>
        <TBODY>
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">	
				<tr><td class="contributionsLabelColumn"  style="border-top:1px solid #CCCCCC" colspan="3">
				Participants can change their salary deferral elections
				</td>			  
				<td class="dataColumn"  style="border-top:1px solid #CCCCCC">		
				<form:select style="width: 400px" path="planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionCode" id="employeeDeferralElectionCode" disabled="${disableFieldsForAutoOrSignUp}" onclick="setDirtyFlag();" >
					<form:option value="">Select</form:option>
					<form:option value="${deferralConstants.AS_OF_EACH_PAYROLL_PERIOD_CODE}">${deferralConstants.AS_OF_EACH_PAYROLL_PERIOD_CODE_DISPLAY}</form:option>
					<form:option value="${deferralConstants.FIRST_DAY_OF_MONTH_CODE}">${deferralConstants.FIRST_DAY_OF_MONTH_CODE_DISPLAY}</form:option>
					<form:option value="${deferralConstants.FIRST_DAY_OF_PLAN_YEAR_QTR_CODE}">${deferralConstants.FIRST_DAY_OF_PLAN_YEAR_QTR_CODE_DISPLAY}</form:option>
					<form:option value="${deferralConstants.FIRST_DAY_OF_PLAN_YR_OR_FIRST_DAY_OF_SEVENTH_MONTH_OF_PLAN_YR_CODE}">${deferralConstants.FIRST_DAY_OF_PLAN_YR_OR_FIRST_DAY_OF_SEVENTH_MONTH_OF_PLAN_YR_CODE_DISPLAY}</form:option>
					<form:option value="${deferralConstants.FIRST_DAY_OF_PLAY_YR}">${deferralConstants.FIRST_DAY_OF_PLAY_YR_DISPLAY}</form:option>
					<form:option value="${deferralConstants.OTHER}">${deferralConstants.OTHER_DISPLAY}</form:option>
</form:select>
				</td>
				</tr>
				<tr><td  colspan="3" ><div id="selectDayDiv" style="padding-right:10px;text-align:right">Select day</div><td class="dataColumn">
				<div style="padding-left:10px;">
 <form:select  style="display:none;" path="planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionSelectedDay" id="employeeDeferralElectionSelectedDay" onblur="validateSalaryDeferalElectionDay()" disabled="${disableFieldsForAutoOrSignUp}">


				  <form:option value="">Select</form:option>
				  <form:option value="1">1</form:option>
				  <form:option value="2">2</form:option>
				  <form:option value="3">3</form:option>
				  <form:option value="4">4</form:option>
				  <form:option value="5">5</form:option>
				  <form:option value="6">6</form:option>
				  <form:option value="7">7</form:option>
				  <form:option value="8">8</form:option>
				  <form:option value="9">9</form:option>
				  <form:option value="10">10</form:option>
				  <form:option value="11">11</form:option>
				  <form:option value="12">12</form:option>
				  <form:option value="13">13</form:option>
				  <form:option value="14">14</form:option>
				  <form:option value="15">15</form:option>
				  <form:option value="16">16</form:option>
				  <form:option value="17">17</form:option>
				  <form:option value="18">18</form:option>
				  <form:option value="19">19</form:option>
				  <form:option value="20">20</form:option>
				  <form:option value="21">21</form:option>
				  <form:option value="22">22</form:option>
				  <form:option value="23">23</form:option>
				  <form:option value="24">24</form:option>
				  <form:option value="25">25</form:option>
				  <form:option value="26">26</form:option>
				  <form:option value="27">27</form:option>
				  <form:option value="28">28</form:option>
				  <form:option value="29">29</form:option>
				  <form:option value="30">30</form:option>
				  <form:option value="31">31</form:option>
</form:select>
				</div>
				</td></tr>
				<tr><td  style="vertical-align:top;padding-top:9px;"  colspan="3" ><div id="selectMonthsDiv" style="text-align:right;padding-right:10px;">Select month(s)</div><td class="dataColumn">
				<span style="display:none" id="employeeDeferralElectionSelectedMonths">
				<table style="padding-left:10px;"><tr><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="01"/>Jan</td><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="02"/>Feb</td><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="03"/>Mar</td><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="04"/>Apr</td><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="05"/>May</td><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="06"/>Jun</td></tr>
				<tr><td>              
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="07"/>Jul</td><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="08"/>Aug</td><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="09"/>Sep</td><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="10"/>Oct</td><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="11"/>Nov</td><td>
				<form:checkbox  path="pifDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" 
				value="12" onblur="validateSalaryDeferalElectionMonth()" id="selectedDeferralElectionMonthsId"/>Dec</td>
				</tr></table>
				</span>
				</td>
			  </tr>
			  </c:when>
			  <c:otherwise>
				<tr><td class="contributionsLabelColumn"   colspan="3" style="border-top:1px solid #CCCCCC">
				Participants can change their salary deferral elections
				</td>
				<td class="dataColumn"  style="border-top:1px solid #CCCCCC">
<c:if test="${pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionCode == 'P'}">As of each payroll period</c:if>

<c:if test="${pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionCode =='M'}">On the first day of each month</c:if>

<c:if test="${pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionCode =='Q'}">On the first day of each plan year quarter</c:if>

<c:if test="${pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionCode =='S'}">On the first day of the plan year or the first day of the 7th month of the plan year</c:if>

<c:if test="${pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionCode == 'Y'}">On the first day of the plan year</c:if>

<c:if test="${pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionCode == 'O'}">

 <c:if test="${not empty pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionSelectedDay}" >

<c:if test="${not empty pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionSelectedMonths}">
							<c:set var="deferralMonthsLength" value="${fn:length(pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionSelectedMonths)}"/>
<c:forEach items="${pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionSelectedMonths}" var="selectedMonths" varStatus="counter" >
								<c:if test="${selectedMonths == '01'}">Jan</c:if>
								<c:if test="${selectedMonths == '02'}">Feb</c:if>
								<c:if test="${selectedMonths == '03'}">Mar</c:if>
								<c:if test="${selectedMonths == '04'}">Apr</c:if>
								<c:if test="${selectedMonths == '05'}">May</c:if>
								<c:if test="${selectedMonths == '06'}">Jun</c:if>
								<c:if test="${selectedMonths == '07'}">Jul</c:if>
								<c:if test="${selectedMonths == '08'}">Aug</c:if>
								<c:if test="${selectedMonths == '09'}">Sep</c:if>
								<c:if test="${selectedMonths == '10'}">Oct</c:if>
								<c:if test="${selectedMonths == '11'}">Nov</c:if>
								<c:if test="${selectedMonths == '12'}">Dec</c:if>
								&nbsp;${pifDataForm.planInfoVO.contributions.planEmployeeDeferralElection.employeeDeferralElectionSelectedDay}						
								<c:if test="${deferralMonthsLength > 1 && (counter.index+1) != deferralMonthsLength }">,</c:if>
</c:forEach>
</c:if>
</c:if>
</c:if>
			  </td>
			  </tr>				  
			  </c:otherwise>
			</c:choose>				
		<!--  catchup Contributions -->			
		<tr>
			<td class="contributionsLabelColumn" colspan="3">Does the plan allow for catch-up contributions<br>for participants age 50 and older?</td>
			  <td class="dataColumn">
				<c:choose>
				  <c:when test="${pifDataForm.editMode}">	
<form:radiobutton path="planInfoVO.contributions.catchUpContributionsAllowed" value="Y"/>Yes

<form:radiobutton path="planInfoVO.contributions.catchUpContributionsAllowed" value="N"/>No

				  </c:when>
				  <c:otherwise>								
					<c:choose>
					<c:when test="${pifDataForm.planInfoVO.contributions.catchUpContributionsAllowed == 'Y'}">
						Yes
					</c:when>
					<c:when test="${pifDataForm.planInfoVO.contributions.catchUpContributionsAllowed == 'N'}">
						No
					</c:when>
					</c:choose>
				  </c:otherwise>
				</c:choose>	
			</td>
		</tr>
      <!--  aci changes  -->     
        <TR class=oddDataRow>
			<TD style="BORDER-LEFT-WIDTH: 0px; class: " vAlign=top dataColumn?><STRONG>Deferral Limits</STRONG></TD>
			<TD class=dataColumn vAlign=top colspan="2"><STRONG>Percentage</STRONG></TD>
			<TD class=dataColumn vAlign=top ><STRONG>Annual Dollar Limit</STRONG></TD>
        </TR>
<!-- deferral maximum Percent -->          
        <TR class=evenDataRow>
			<TD style="BORDER-LEFT-WIDTH: 0px" class=dataColumn title=401K>Maximum </TD>
          <!-- money type code  -->
			<TD class=dataColumn width="10%">
				<c:choose>
				  <c:when test="${pifDataForm.editMode}">	
<form:checkbox disabled="${disableFieldsForAutoOrSignUp}" id="planDataUi_deferralMaxPercent" 
								   onclick="handleDeferralMaxPercentTextElement()" 
								   path="pifDataUi.deferralMaxPercent" 
								   value="100"/>100%&nbsp;&nbsp;or		



				  </c:when>
				  <c:otherwise>								
					  <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_LOAN_PERCENTAGE_SCALE}" value="${pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMaxPercent}"/>
					  ${empty pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMaxPercent ? '' : '%'}						
				  </c:otherwise>
				</c:choose>					
			</TD>
			<TD>
				<c:choose>
				  <c:when test="${pifDataForm.editMode}">	
<form:input disabled="${disableFieldsForAutoOrSignUp}" 
					id="planDataUi_deferralMaxPercent_text"
					onblur="handleDeferralMaxPercentChange();validateDeferralLimitMax(this)" size="10" maxlength="6" cssClass="numericInput" path="pifDataUi.deferralMaxPercent"/>%
					<input type="hidden" id="planDataUi_deferralMaxPercent_textHidden"
						name="pifDataForm.pifDataUi.deferralMaxPercent" value="" />	

				  </c:when>
				  <c:otherwise>			
				  </c:otherwise>
				</c:choose>					
			</TD>			
<!-- deferral maximum dollars -->	
			<td class="dataColumn">
				<c:choose>
				  <c:when test="${pifDataForm.editMode}">	
						<script type="text/javascript">
							$(document).ready(function() {
								deferralIrsAppliesId = "#planInfoVO_contributions_planDeferralLimits_deferralIrsApplies";
								deferralMaxAmountId = "#planDeferralLimits_deferralMaxAmount";

<c:if test="${empty pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMaxAmount}">
										$(deferralIrsAppliesId).prop("checked", true);
										$(deferralMaxAmountId).val(""); 
										$(deferralMaxAmountId).prop("disabled", true);	
</c:if>
<c:if test="${not empty pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMaxAmount}">
										$(deferralIrsAppliesId).prop("checked", false);
										$(deferralMaxAmountId).prop("disabled", false);	
				$(deferralMaxAmountId).val('${pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMaxAmount}');
</c:if>


								$(deferralIrsAppliesId).on("click", function() {
									setDirtyFlag();
									var deferralIrsAppliesHiddenId ="pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralIrsApplies";
									
									if($(this).is(':checked')){ 
										$(deferralMaxAmountId).val(""); 
										$(deferralMaxAmountId).prop("disabled", true);	
										$(deferralIrsAppliesHiddenId).val('true');
									}else{
										$(deferralMaxAmountId).prop("disabled", false);						$(deferralMaxAmountId).val('${pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMaxAmount}'); 
										$(deferralIrsAppliesHiddenId).val('false');
									}
								});
							});							
						</script>
						<input type="checkbox" 
							id="planInfoVO_contributions_planDeferralLimits_deferralIrsApplies" 
							name="planInfoVO.contributions.planDeferralLimits.deferralIrsApplies" 
							value="true"						
							<c:if test="${pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralIrsApplies}">checked="checked" </c:if> />
<form:hidden path="planInfoVO.contributions.planDeferralLimits.deferralIrsApplies" id="planDeferralLimits_deferralIrsApplies" value="false" /><%-- input - name="pifDataForm" --%>

	            IRS annual maximum 
or $<form:input path="planInfoVO.contributions.planDeferralLimits.deferralMaxAmount" onblur="validateWholeNumber(this)" size="10" cssClass="numericInput" id="planDeferralLimits_deferralMaxAmount"/>




				(excluding catch-up)
				  </c:when>
				  <c:otherwise>		
					<c:choose>
					<c:when test="${pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralIrsApplies}">
						IRS annual maximum 
					</c:when>					
					<c:otherwise>						
<fmt:formatNumber value="${pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMaxAmount}" type="CURRENCY" currencyCode="USD"/>(excluding catch-up)						
					</c:otherwise>
					</c:choose>					
				  </c:otherwise>
				</c:choose>	
			</td>
<!-- deferral minimum Percent -->			
<!-- permitted by plan -->
        </TR>
        <TR class=oddDataRow>
			<TD style="BORDER-LEFT-WIDTH: 0px" class=dataColumn title=401K>Minimum </TD>
			<!-- money type code  -->	
			<td class="dataColumn" width="10%">
				<c:choose>
				  <c:when test="${pifDataForm.editMode}">	
					&nbsp;
				  </c:when>
				  <c:otherwise>								
					  <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_LOAN_PERCENTAGE_SCALE}" value="${pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMinPercent}"/>
					  ${empty pifDataForm.planInfoVO.contributions.planDeferralLimits.deferralMinPercent ? '' : '%'}					
				  </c:otherwise>
				</c:choose>	
			</td>				
			<td>
				<c:choose>
				  <c:when test="${pifDataForm.editMode}">	
<form:input path="pifDataUi.deferralMinPercent" disabled="${disableFieldsForAutoOrSignUp}" maxlength="6" onblur="validateDeferralLimitMin(this);validateDeferalMinAndMaxField(this,'planDataUi_deferralMaxPercent_textHidden')" size="10" cssClass="numericInput"/>%
				  </c:when>
				  <c:otherwise>								
				  </c:otherwise>
				</c:choose>				
			</td>			
			<TD class=dataColumn><c:if test="${pifDataForm.confirmMode}">n/a</c:if></TD>
        </TR>
   <!--  aci changes  -->  
		<TR><TD class=subsubhead  colspan="4">Automatic Contribution Increases</TD></TR>
        <tr>
          <td class="contributionsLabelColumn" colspan="3">
          Does the plan provide for automatic contribution increases?
          </td>
          <td class="dataColumn">
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">	
<form:radiobutton disabled="${disableFieldsForAutoOrSignUp}" path="planInfoVO.contributions.aciAllowed" value="Y"/>Yes<span id="planDataUi_automaticContributionEffectiveDate_span" style="display: none">, as of

<form:input path="pifDataUi.aciEffectiveDate" disabled="${disableFieldsForAutoOrSignUp}" maxlength="10" onblur="validateAciEffectiveDate(this)" onchange="setDirtyFlag();" size="10" id="aciEffectiveDateId"/>






					  <img id="aciEffectiveDateIdImage" onclick="return handleDateIconClicked(event, 'aciEffectiveDateId');" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
				</span>
				
<form:radiobutton path="planInfoVO.contributions.aciAllowed" value="N"/>No
			  </c:when>
			  <c:otherwise>								
				  <c:if test="${pifDataForm.planInfoVO.contributions.aciAllowed == 'Y'}">Yes , as of
<fmt:formatDate value="${pifDataForm.planInfoVO.contributions.planAutoContributionIncrease.effectiveDate}" pattern="MM/dd/yyyy" />(mm&frasl;dd&frasl;yyyy)
				  </c:if>
				  <c:if test="${pifDataForm.planInfoVO.contributions.aciAllowed == 'N'}">No</c:if>				
			  </c:otherwise>
			</c:choose>				
          </td>
        </tr>
        <tr>
          <td valign="top" class="contributionsLabelColumn" colspan="3">
          &nbsp;&nbsp;&nbsp;Applies to
          </td>
          <td class="dataColumn" valign="top">
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">		
<div><form:radiobutton path="planInfoVO.contributions.planAutoContributionIncrease.appliesTo" value="AL"/>All participants</div>
<form:hidden path="planInfoVO.contributions.newParticipantApplyDateDisabled"/>
<div><form:radiobutton disabled="${disableFieldsForAuto}" path="planInfoVO.contributions.planAutoContributionIncrease.appliesTo" value="AU"/>Automatically-enrolled participants that have not made an election</div>
<div><form:radiobutton disabled="${disableFieldsForAuto}" path="planInfoVO.contributions.planAutoContributionIncrease.appliesTo" value="NE"/>New participants only<span>, effective
<form:input path="pifDataUi.aciAppliesToEffectiveDate" disabled="true" maxlength="10" onblur="validateAciAppliesToEffectiveDate(this);validateDateWithACIDate()" onchange="setDirtyFlag();" size="10" id="aciAppliesToEffectiveDateId"/>






					  <img id="aciAppliesToEffectiveDateIdImage" onclick="return handleDateIconClicked(event, 'aciAppliesToEffectiveDateId');" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
					</span>
				</div>			  
			  </c:when>
			  <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.contributions.planAutoContributionIncrease.appliesTo == 'AL'}">All participants</c:if>

<c:if test="${pifDataForm.planInfoVO.contributions.planAutoContributionIncrease.appliesTo == 'AU'}">Automatically-enrolled participants that have not made an election</c:if>

<c:if test="${pifDataForm.planInfoVO.contributions.planAutoContributionIncrease.appliesTo == 'NE'}">New participants only , effective

<fmt:formatDate value="${pifDataForm.planInfoVO.contributions.planAutoContributionIncrease.newParticipantApplyDate}" pattern="MM/dd/yyyy" />(mm&frasl;dd&frasl;yyyy)								 
</c:if>
			  </td>
			</tr>				  
			  </c:otherwise>
			</c:choose>				
          </td>
        </tr>
        <tr>
          <td class="contributionsLabelColumn" colspan="3">
          &nbsp;&nbsp;&nbsp;When are annual increases applied? </td>
          <td class="dataColumn">
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">	
<form:input path="pifDataUi.aciApplyDate" disabled="${disableFieldsForAuto}" maxlength="5" onblur="validateAciApplyDate(this)" onchange="setDirtyFlag();" size="5" id="aciApplyDateId"/> (mm/dd)






			  </c:when>
			  <c:otherwise>								
 <c:if test="${not empty pifDataForm.planInfoVO.contributions.planAutoContributionIncrease.annualApplyDate}">
					${pifDataForm.planInfoVO.contributions.planAutoContributionIncrease.annualApplyDate}
					(mm/dd)
</c:if>
			  </c:otherwise>
			</c:choose>		  
          </td> 
        </tr>
        <tr>
          <td class="contributionsLabelColumn"  colspan="3">
            &nbsp;&nbsp;&nbsp;Default annual increase 
          </td>
          <td class="dataColumn">
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">	
<form:input path="pifDataUi.annualIncrease" disabled="${disableFieldsForAuto}" onblur="validateDefaultAnnualIncreaseWithDecimal(this, 'Y')" size="8" cssClass="numericInput" id="autoIncreasePct"/>%
			  </c:when>
			  <c:otherwise>								
				  <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_LOAN_PERCENTAGE_SCALE}" value="${pifDataForm.pifDataUi.annualIncrease}"/>
				  ${empty pifDataForm.pifDataUi.annualIncrease ? '' : '%'}						  
			  </c:otherwise>
			</c:choose>			  
          </td>
        </tr>
        <tr>
          <td class="contributionsLabelColumn" colspan="3">
              &nbsp;&nbsp;&nbsp;Default maximum for automatic increases
          </td>
          <td class="dataColumn">
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">	
<form:input path="pifDataUi.maxAutomaticIncrease" disabled="${disableFieldsForAuto}" onblur="validateDefaultMaximumAutoIncreaseWithDecimal(this, 'Y')" size="8" cssClass="numericInput" id="autoIncreaseMax"/>%
			  </c:when>
			  <c:otherwise>								
				  <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_LOAN_PERCENTAGE_SCALE}" value="${pifDataForm.pifDataUi.maxAutomaticIncrease}"/>
				  ${empty pifDataForm.pifDataUi.maxAutomaticIncrease ? '' : '%'}					  
			  </c:otherwise>
			</c:choose>		   
          </td>
        </tr>
    </table>
  	</div>	  <!-- end of aci changes  -->      
	<table width="729" class="dataTable">
	<TR><TD class=subsubhead>Employer Contributions</TD></TR>
	</table>
	<div id="employerContibutionDiv" style="background-color: white">
	</div>
	<c:choose>
	<c:when test="${fn:length(employerMoneyTypes) > 0}">
	<div style="padding-top: 6px; padding-bottom: 6px;padding-left: 3px; background-color: white">
		<c:if test="${pifDataForm.editMode}">	
			<span  id="createFormulaSpanOn" style="color:#000099;text-decoration:underline" onmouseover="this.style.cursor='hand'" onclick="createContributionFormula()">Create additional Contribution Formula</span>
			<span  id="createFormulaSpanOff" style="color:gray;display:none;text-decoration:underline;">Create additional Contribution Formula</span>
		</c:if>
	</div>
	</c:when>
	</c:choose>
	</TD></TR></TABLE>				
<!--end table content -->
</div>	
<form:hidden path="planInfoVO.contributions.contributionsFormulaCount"/>
<script type="text/javascript">
<c:forEach items="${pifDataForm.pifDataUi.employerRulesets}" var="rule" varStatus="status">
	<c:if test="${rule != null }">
		ruleSets.push("${rule}");	
	</c:if>

</c:forEach> 
</script>	
 <c:forEach items="${pifDataForm.pifDataUi.employerRulesets}" var="rule" varStatus="status">
	<c:if test="${rule != null }">     
       <SPAN style="DISPLAY: none" id=1_max_match_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=1_first_percent_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=1_next_percent_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=1_next_percent_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=1_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=1_money_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=2_max_match_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=2_first_percent_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=2_next_percent_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=2_next_percent_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=2_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=2_money_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=3_max_match_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=3_first_percent_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=3_next_percent_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=3_next_percent_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=3_type_error_content></SPAN>
	   <SPAN style="DISPLAY: none" id=3_money_type_error_content></SPAN>
	</c:if>
</c:forEach> 
