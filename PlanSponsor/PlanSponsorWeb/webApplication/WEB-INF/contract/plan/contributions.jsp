<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
       
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page import="com.manulife.pension.service.contract.valueobject.MoneyTypeVO"%><un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="aciConstants" className="com.manulife.pension.service.plan.valueobject.PlanAutoContributionIncrease" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="deferralConstants" className="com.manulife.pension.service.plan.valueobject.PlanEmployeeDeferralElection" />
<un:useConstants var="ruleConstants" className="com.manulife.pension.service.plan.valueobject.PlanContributionRule" />
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

var ruleSets = Array();
var ruleSetObjects = Array();
var moneyTypesArray = Array();
var moneyTypesNameMap = Array();
var viewableRulesArray = Array();
<c:forEach items="${employerMoneyTypes}" var="moneyType" varStatus="moneyTypeStatus">
  moneyTypesArray.push("${moneyType.id}");
  moneyTypesNameMap["${moneyType.id}"] = Array();
  moneyTypesNameMap["${moneyType.id}"]["longName"] = "${moneyType.contractLongName}";
  moneyTypesNameMap["${moneyType.id}"]["shortName"] = "${moneyType.contractShortName}";
</c:forEach>
<c:forEach items="${planDataForm.planDataUi.viewableRules}" var="rule" varStatus="ruleStatus">
  viewableRulesArray.push("${rule}");
</c:forEach>
<c:choose>
<c:when test="${planDataForm.editMode}">
var editMode = true;
var confirmMode = false;
</c:when>
<c:when test="${planDataForm.confirmMode}">
var editMode = false;
var confirmMode = true;
</c:when>
<c:otherwise>
var editMode = false;
var confirmMode = false;
</c:otherwise>
</c:choose>
var irsMaximum = ${planDataForm.planDataUi.planData.irsAnnualRegularMaximumAmount}

function showOrHideAutomaticContributionEffectiveDate() {
  var enable = true;
  var aciAllowed = $("input[name='planDataUi.planData.aciAllowed']:checked").val();
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
    <c:if test="${planDataForm.confirmMode or planDataForm.editMode}">
    showOrHideAutomaticContributionEffectiveDate();
    </c:if>

    
    if ( editMode || confirmMode ) {
    	var autoIncreasePct =  document.getElementById("autoIncreasePct");
    	var autoIncreaseMax =  document.getElementById("autoIncreaseMax");
    	<c:if test="${planDataForm.planDataUi.planData.csfPptChangeDeferralsOnline eq 'Y' }">
    	validateDefaultAnnualIncrease(autoIncreasePct , 'N');
    	validateDefaultMaximumAutoIncrease(autoIncreaseMax, 'N');
    	</c:if>
    	<c:if test="${planDataForm.planDataUi.planData.csfPptChangeDeferralsOnline ne 'Y' }">
    	validateDefaultAnnualIncreaseWithDecimal(autoIncreasePct, 'N');
    	validateDefaultMaximumAutoIncreaseWithDecimal(autoIncreaseMax, 'N');
    	</c:if>
    }
    
    $("input[name='planDataUi.planData.planDeferral.deferralIrsApplies']").on("click",function() {
      if ($("input[name='planDataUi.planData.planDeferral.deferralIrsApplies']:checked").val() == 'true') {
        $("input[name='planDataUi.planData.planDeferral.deferralMaxAmount']").val("");
        $("input[name='planDataUi.planData.planDeferral.deferralMaxAmount']").prop("disabled", true);
      } else {
        $("input[name='planDataUi.planData.planDeferral.deferralMaxAmount']").prop("disabled", false);
		if (${planDataForm.editMode}) {
			if (${disableFieldsForAutoOrSignUp}) {
				$("input[name=planDataUi.planData.planDeferral.deferralMaxAmount']").prop("disabled", true);
			}
		}
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
 			  if (String.prototype.trim.call( $("#aciApplyDateId").val() == null ? "" : $("#aciApplyDateId").val() ) == "") {
		        $("#aciApplyDateId").val("${planDataForm.planDataUi.planData.planYearEnd.plusOneNonLeapYear}");
			  }
			}
		}
	});
	$("input[name='planDataUi.planData.aciAllowed']").on("click",function() {
		if($("input[name='planDataUi.planData.aciAllowed']:checked").val() == "${planDataConstants.YES_CODE}" ) {
			$("input[name='planDataUi.planData.planAutoContributionIncrease.appliesTo']").prop("disabled", false);
			$("input[name='planDataUi.aciAppliesToEffectiveDate']").prop("disabled", false);
			$("input[name='planDataUi.aciApplyDate']").prop("disabled", false);
			$("input[name='planDataUi.planData.planAutoContributionIncrease.defaultIncreasePercent']").prop("disabled", false);
			$("input[name='planDataUi.planData.planAutoContributionIncrease.defaultAutoIncreaseMaxPercent']").prop("disabled", false);

			//there are subfields dependent on this radio.  only do this on Yes_code, since for no, its disabled anyway.
			$("input[name='planDataUi.planData.planAutoContributionIncrease.appliesTo']:checked").trigger('click');
			if (${planDataForm.editMode}) {
				if (${disableFieldsForAuto}) {
					$("input[name='planDataUi.planData.planAutoContributionIncrease.appliesTo']").prop("disabled", true);
					$("input[name='planDataUi.aciAppliesToEffectiveDate']").prop("disabled",true);
					$("input[name='planDataUi.aciApplyDate']").prop("disabled",true);
					$("input[name='planDataUi.planData.planAutoContributionIncrease.defaultIncreasePercent']").prop("disabled", true);
					$("input[name='planDataUi.planData.planAutoContributionIncrease.defaultAutoIncreaseMaxPercent']").prop("disabled", true);
				}
			}	
		} else {
			$("input[name='planDataUi.planData.planAutoContributionIncrease.appliesTo']").prop("disabled", true);
            $("input[name='planDataUi.planData.planAutoContributionIncrease.appliesTo']").prop("checked", false);
			$("input[name='planDataUi.aciAppliesToEffectiveDate']").prop("disabled",true);
			$("input[name='planDataUi.aciAppliesToEffectiveDate']").val("");
			$("input[name='planDataUi.aciApplyDate']").prop("disabled",true);
			$("input[name='planDataUi.aciApplyDate']").val("");
			$("input[name='planDataUi.planData.planAutoContributionIncrease.defaultIncreasePercent']").prop("disabled", true);
			$("input[name='planDataUi.planData.planAutoContributionIncrease.defaultIncreasePercent']").val("");
			$("input[name='planDataUi.planData.planAutoContributionIncrease.defaultAutoIncreaseMaxPercent']").prop("disabled", true);
			$("input[name='planDataUi.planData.planAutoContributionIncrease.defaultAutoIncreaseMaxPercent']").val("");
		}
		showOrHideAutomaticContributionEffectiveDate();
	});
	
	$("input[name='planDataUi.planData.planAutoContributionIncrease.appliesTo']").on("click",function(){
			if ( $("input[name='planDataUi.planData.planAutoContributionIncrease.appliesTo']:checked").val() == "${aciConstants.APPLY_TO_PPT_ALL_PARTICIPANT_CODE}" || 
					$("input[name='planDataUi.planData.planAutoContributionIncrease.appliesTo']:checked").val() == "${aciConstants.APPLY_TO_PPT_AUTO_ENROLLED_PARTICIPANTS_CODE}" ) {
				$("input[name='planDataUi.aciAppliesToEffectiveDate']").prop("disabled",true);
				$("input[name='planDataUi.aciAppliesToEffectiveDate']").val("");
				$("#aciAppliesToEffectiveDateIdImage").prop("disabled", true);
			} else if ($("input[name='planDataUi.aciAppliesToEffectiveDate']").val() !="" 
						&& $("input[name='planDataUi.planData.newParticipantApplyDateDisabled']").val() == 'true') {
				$("input[name='planDataUi.aciAppliesToEffectiveDate']").prop("disabled",true);
				$("#aciAppliesToEffectiveDateIdImage").prop("disabled", true);
			} else {
				$("input[name='planDataUi.aciAppliesToEffectiveDate']").prop("disabled",false);
				$("#aciAppliesToEffectiveDateIdImage").prop("disabled", false);
			}
				
		})
	
	//formulas
	if ( ruleSets.length == 0 ) {
		if ( moneyTypesArray.length == 0 ) {
			$("#employerContibutionDiv").append('<content:getAttribute  escapeJavaScript="true" beanName="contributionNoMoneyTypes" attribute="text"/>');
			
		} else if ( editMode ) {
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
		assignHandlers();
		updateUiForEditMode();
	}
	if (!editMode && !confirmMode ) {
		//get all the money types in use
		var notInUse = Array();
		for ( var i = 0 ; i < moneyTypesArray.length ; i++ ) {
			if ( !isMoneyTypeInUse(moneyTypesArray[i]) ) {
				notInUse.push(moneyTypesArray[i]);
			}
		}
		if ( notInUse.length > 0 ) {
			$("#employerContibutionDiv").append('<br>'  + '&nbsp;<content:getAttribute  escapeJavaScript="true" beanName="moneyTypesNotInUse" attribute="text"/>');
			for ( var i = 0 ; i < notInUse.length ; i++ ) {
				$("#employerContibutionDiv").append("<span title=\"" + moneyTypesNameMap[notInUse[i]]["longName"] + "\">" + moneyTypesNameMap[notInUse[i]]["shortName"] + "</span>");
				if ( i < notInUse.length -1 ) {
					$("#employerContibutionDiv").append(", ");
				}
			}
		}
	}
	if ( confirmMode ) {
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
	$("#employeeDeferralElectionCode").trigger('change');
	$("input[name='planDataUi.planData.aciAllowed']:checked").trigger('click');
	$("input[name='planDataUi.planData.planDeferral.deferralIrsApplies']:checked").trigger('click');
	
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
			$("#" + i + "_match_div input[type=\"text\"]").prop("value", "");

		} else {
			$("#" + i + "non_match_div input").prop("disabled", false);
			$("#" + i + "_match_div input").prop("disabled", false);
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
	rs+="</div></td><td>";
	rs+="<div><input type=\"radio\" name=\"" + vals[0] + "_type\" value=\"F\">Fixed<input type=\"radio\" name=\"" + vals[0] + "_type\" value=\"D\">Discretionary<input type=\"radio\" name=\"" + vals[0] + "_type\" value=\"P\">Prevailing wage contribution<span id=\"" + vals[0] + "_type_error_container\"></span></div>";
	rs+="<table style=\"margin-top: 6px\"><tr><td style=\"padding:2px;\">";
	rs+="<div class=\"formulaHeader\" >&nbsp;Matching Contributions</div>";
	rs+="<div  class=\"formulaBody\" id=\"" + vals[0] + "_match_div\">"; //start match area div
	rs+="<table><tr><td>";
	rs+="<div style=\"padding-left:10px\"><input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_first_percent\"> % of the first"
	rs+="</td><td nowrap>"
	rs+="<input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_first_percent_value\"><input type=\"radio\" name=\"" + vals[0] + "_first_percent_type\" value=\"P\">%&nbsp;<input type=\"radio\" name=\"" + vals[0] + "_first_percent_type\" value=\"D\">$<span id=\"" + vals[0] + "_first_percent_type_error_container\"></span></div>";
	rs+="</td></tr><tr><td>";
	rs+="<div style=\"padding-left:10px\"><input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_next_percent\"> % of the next<span id=\"" + vals[0] + "_next_percent_error_container\">"
	rs+="</td><td>"
	rs+="<input size=\"5\" type=\"text\" class=\"numericInput\" name=\"" + vals[0] + "_next_percent_value\"><input type=\"radio\" name=\"" + vals[0] + "_next_percent_type\" value=\"P\">%&nbsp;<input type=\"radio\" name=\"" + vals[0] + "_next_percent_type\" value=\"D\">$<span id=\"" + vals[0] + "_next_percent_type_error_container\"></span></div>";
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
		$("#employerContibutionDiv").append("<input type=\"hidden\" name=\"planDataUi.employerRulesets[" + i + "]\" value=\"" + ruleSets[i] + "\">");
	}
	$("input[name='planDataUi.planData.numberOfRuleSets']").prop("value", ruleSets.length);
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
				string += $("input[name='" + ruleNum + "_type']:checked").val();

			rs.push(string)
		} 
		return rs;
}
function createContributionFormula() {
	var newRuleSet = (ruleSets.length+1) + defaultRuleSet; //this just increments the counter by 1.
	ruleSets.push(newRuleSet);
	$("#employerContibutionDiv").append(decodeRuleSet(ruleSets.length, newRuleSet));
	initializeRuleSet(ruleSets.length, newRuleSet)
	assignHandlers();
	updateUiForEditMode();
}
function assignHandlers() {
	for ( var i = 0 ; i < ruleSets.length ; i++ ) {
    	//money types
		$("input[name='" + (i+1) + "_mt']").on("click",function() {
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
    	
		$("input[name='" + (i+1) + "_first_percent_value']").on('blur', (i+1), function(e){
		
			if ( $("input[name='" + e.data + "_first_percent_type']:checked").length > 0 ) {
				if ( $("input[name='" + e.data + "_first_percent_type']:checked").val() == "${ruleConstants.TYPE_PERCENT}" ) {
	            	validateFirstOfFirstPercent(this);
				}	else {
					validateFirstOfFirstAmount(this);
				} 
			}
            updateUiForEditMode();
    	})
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
				$("input[name='" + j +  "_type'][value='${ruleConstants.PREVAILING}']").prop("disabled", true);
			}
		} 
	}
	if ( !foundPrevailing ) {
		for ( var j = 1 ; j <= ruleSets.length ; j++ ) {
			$("input[name='" + j + "_type'][value='${ruleConstants.PREVAILING}']").prop("disabled", false);
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
								$("input[name='" + i + "_mt']").on("click",function() {
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
                        return "<div title=\""+ moneyTypesNameMap[moneyTypeId]["longName"] + "\" id=\"" + rule + "_mt_" + moneyTypeId + "_div\"><input type=\"checkbox\" name=\"" + rule + "_mt\" value=\"" + moneyTypeId + "\">" + moneyTypesNameMap[moneyTypeId]["shortName"] + "</div>"; 
                    }
                    function updateRemoveSpans() {
                        for ( var i = 0 ; i < ruleSets.length ; i++ ) {
                            if ( ruleSets.length > 1 ) {
	                            $("#" + (i+1) + "_remove_span").css("text-decoration", "underline");
                            	$("#" + (i+1) + "_remove_span").css("color", "#000099");
                            	$("#" + (i+1) + "_remove_span").on("mouseover",function(){
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
						assignHandlers();
						updateUiForEditMode();
                    }


</script>
                    
                      
<div id="contributionsTabDivId" class="borderedDataBox">
  <div class="subhead">
     <!-- header -->
    <c:if test="${not param.printFriendly}">
      <div class="expandCollapseIcons" id="contributionsShowIconId" onclick="expandDataDiv('contributions');">
        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
      </div>
      <div class="expandCollapseIcons" id="contributionsHideIconId" onclick="collapseDataDiv('contributions');">
        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
      </div>
    </c:if>
    <div class="sectionTitle">
      <content:getAttribute beanName="contributionText" attribute="text"/>
    </div>
    <div class="sectionHighlightIcon" id="contributionSectionHighlightIconId">
      <ps:sectionHilight name="${uiConstants.SECTION_CONTRIBUTIONS}" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
    </div>
    <div class="endDataRowAndClearFloats"></div>
  </div>
  <div id="contributionsDataDivId">
  <!-- permitted money sources -->
    <div class="boldLabel" style="padding-right:5px;background-color:white;">Permitted money sources</div>
    <div class="evenDataRow" style="padding-right:5px;">
      <c:forEach items="${moneySources}" var="moneySource" varStatus="moneySourceStatus">
        <div class="${(moneySourceStatus.count % 2 == 0) ? 'rightColData' : 'leftColData'}">
          <div class="data">
            ${moneySource.contractLongName}&nbsp;(${moneySource.contractShortName})
          </div>
        </div>
        <c:if test="${moneySourceStatus.count % 2 == 0 or moneySourceStatus.last}">
          <div class="endDataRowAndClearFloats"></div>
        </c:if>
      </c:forEach>
    </div>  
  <!-- employee contributions -->
  	<div class="subsubhead">Employee Contributions</div>
    <div class="boldLabel" style="background-color:white;">Permitted employee money types</div>
    <div class="evenDataRow">
      <c:forEach items="${employeeMoneyTypes}" var="moneyType" varStatus="moneyTypeStatus">
        <div class="${(moneyTypeStatus.count % 2 == 0) ? 'rightColData' : 'leftColData'}">
          <div class="data">
            ${moneyType.contractLongName}&nbsp;(${moneyType.contractShortName})
          </div>
        </div>
        <c:if test="${moneyTypeStatus.count % 2 == 0 or moneyTypeStatus.last}">
          <div class="endDataRowAndClearFloats"></div>
        </c:if>
      </c:forEach>
    </div>  	
    <div class="evenDataRow">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
    
    	<table class="dataTable" width="100%">
    	<tr><td class="contributionsLabelColumn"  style="border-top:1px solid #CCCCCC">
    	<ps:fieldHilight name="planDataUi.planData.planEmployeeDeferralElectionCode" singleDisplay="true" className="errorIcon" displayToolTip="true"/>    	
    	Participants can change their salary deferral elections
    	</td><td class="dataColumn"  style="border-top:1px solid #CCCCCC">
 <form:select style="width: 400px" path="planDataUi.planData.planEmployeeDeferralElection.employeeDeferralElectionCode" id="employeeDeferralElectionCode" disabled="${disableFieldsForAutoOrSignUp}">

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
    	<tr><td><div id="selectDayDiv" style="padding-right:10px;text-align:right">Select day</div><td class="dataColumn">
    	<div style="padding-left:10px;">
       	<ps:fieldHilight name="planDataUi.planData.planEmployeeDeferralSelectedDay" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
 <form:select  style="display:none;" path="planDataUi.planData.planEmployeeDeferralElection.employeeDeferralElectionSelectedDay" id="employeeDeferralElectionSelectedDay" disabled="${disableFieldsForAutoOrSignUp}">

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
    	<tr><td  style="vertical-align:top;padding-top:9px;"><div id="selectMonthsDiv" style="text-align:right;padding-right:10px;">Select month(s)</div><td class="dataColumn">
    	<span style="display:none" id="employeeDeferralElectionSelectedMonths">
       	<ps:fieldHilight name="planDataUi.planData.planEmployeeDeferralSelectedMonths" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
    	
    	<table style="padding-left:10px;"><tr><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="01"/>Jan</td><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="02"/>Feb</td><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="03"/>Mar</td><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="04"/>Apr</td><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="05"/>May</td><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="06"/>Jun</td></tr>
    	<tr><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="07"/>Jul</td><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="08"/>Aug</td><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="09"/>Sep</td><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="10"/>Oct</td><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="11"/>Nov</td><td>
    	<form:checkbox  path="planDataUi.selectedDeferralElectionMonths" disabled="${disableFieldsForAutoOrSignUp}" value="12"/>Dec</td>
    	</tr></table>
    	</span>
    	</td>
    	</tr>
    	</table>
              </c:when>
              <c:otherwise>
    	<table class="dataTable" width="100%">
    	<tr><td class="contributionsLabelColumn" style="border-top:1px solid #CCCCCC">
    	Participants can change their salary deferral elections
    	</td><td class="dataColumn"  style="border-top:1px solid #CCCCCC">
    	<c:choose>
    		<c:when test="${planDataForm.planDataUi.planData.planEmployeeDeferralElection.employeeDeferralElectionCode == deferralConstants.AS_OF_EACH_PAYROLL_PERIOD_CODE or 
    			planDataForm.planDataUi.planData.planEmployeeDeferralElection.employeeDeferralElectionCode == deferralConstants.FIRST_DAY_OF_MONTH_CODE}">
				<c:out value="${planDataForm.planDataUi.planData.planEmployeeDeferralElection.employeeDeferralElectionCodeDisplay}"/>    			
    	</c:when>
		<c:otherwise>
				<c:out value="${planDataForm.planDataUi.planData.planEmployeeDeferralElection.employeeDeferralElectionCodeDisplayDates}"/>    			
		</c:otherwise>
    	</c:choose>
    	</td>
    	</tr>
    	</table>

			</c:otherwise>  
			</c:choose>  	
    </div>
	<!--  catchup Contributions -->
  	<div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="contributionsLabelColumn">Does the plan allow for catch-up contributions<br>for participants age 50 and older?</td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:radiobutton path="planDataUi.planData.catchUpContributionsAllowed" value="${planDataConstants.YES_CODE}"/>Yes
<form:radiobutton path="planDataUi.planData.catchUpContributionsAllowed" value="${planDataConstants.NO_CODE}"/>No
<form:radiobutton path="planDataUi.planData.catchUpContributionsAllowed" value="${planDataConstants.UNSPECIFIED_CODE}"/>Unspecified
            </c:when>
            <c:otherwise>
	          <c:if test="${planDataForm.planDataUi.planData.catchUpContributionsAllowed == planDataConstants.YES_CODE}">Yes</c:if>
          	  <c:if test="${planDataForm.planDataUi.planData.catchUpContributionsAllowed == planDataConstants.NO_CODE}">No</c:if>
              <c:if test="${planDataForm.planDataUi.planData.catchUpContributionsAllowed == planDataConstants.UNSPECIFIED_CODE}"></c:if>
            </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
  	</div>

  	<div class="evenDataRow">
	  <table class="dataTable">
<!-- deferral maximum dollars -->	  
	    <tr>
	      <td class="contributionsLabelColumn">Deferral maximum dollars</td>
	      <td class="dataColumn">
	        <c:choose>
	          <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
	              	  <c:choose>
				  	    <c:when test="${empty planDataForm.planDataUi.planData.planDeferral.deferralIrsApplies}">
				          <input disabled="${disableFieldsForAutoOrSignUp}" name="planDataUi.planData.planDeferral.deferralIrsApplies" type="radio" value="true" checked="checked"/>
				  	    </c:when>
				  	    <c:otherwise>
<form:radiobutton disabled="${disableFieldsForAutoOrSignUp}" path="planDataUi.planData.planDeferral.deferralIrsApplies" value="true"/>
				        </c:otherwise>
				      </c:choose>
	              IRS annual maximum
<form:radiobutton disabled="${disableFieldsForAutoOrSignUp}" path="planDataUi.planData.planDeferral.deferralIrsApplies" value="false"/>
$<form:input path="planDataUi.planData.planDeferral.deferralMaxAmount" disabled="${disableFieldsForAutoOrSignUp}" onblur="validateDeferralAnnualLimit(this)" size="10" cssClass="numericInput"/>




	    			<ps:fieldHilight name="planDataUi.planData.aciDeferralAnnualLimit" singleDisplay="true" displayToolTip="true"/>
	          </c:when>
	          <c:otherwise>
					<c:choose>
				  	    <c:when test="${empty planDataForm.planDataUi.planData.planDeferral.deferralIrsApplies}">
				  	    </c:when>
				  	    <c:when test="${planDataForm.planDataUi.planData.planDeferral.deferralIrsApplies}">
				 		  IRS annual maximum	
				  	    </c:when>
					    <c:when test="${not planDataForm.planDataUi.planData.planDeferral.deferralIrsApplies}">
						    <render:number pattern="$###,###,##0" property="planDataForm.planDataUi.planData.planDeferral.deferralMaxAmount"/>
					    </c:when>
					</c:choose>            
			  </c:otherwise>
			</c:choose>
	      </td>
	    </tr>
<!-- deferral maximum percent -->
	    <tr>
	      <td class="contributionsLabelColumn">
	      <ps:fieldHilight name="planDataUi.planData.aciDeferralLimitMax" singleDisplay="true" className="errorIcon" displayToolTip="true" />
	      Deferral maximum percent</td>
	      <td class="dataColumn">
	        <c:choose>
	          <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">	      	      
<form:input path="planDataUi.deferralMaxPercent" disabled="${disableFieldsForAutoOrSignUp}" onblur="validateDeferralLimitMax(this)" size="10" cssClass="numericInput"/>%
	      	  </c:when>
	          <c:otherwise>
                <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_PERCENTAGE_RATE_SCALE}" value="${planDataForm.planDataUi.planData.planDeferral.deferralMaxPercent}"/>${empty planDataForm.planDataUi.planData.planDeferral.deferralMaxPercent ? '' : '%'}
			  </c:otherwise>
			</c:choose>
	      </td>
	    </tr>	    
<!-- deferral minimum percent -->
	    <tr>
	      <td class="contributionsLabelColumn">
	      	 <ps:fieldHilight name="planDataUi.planData.aciDeferralLimitMin" singleDisplay="true" className="errorIcon" displayToolTip="true" />
	         Deferral minimum percent
	      </td>
	      <td class="dataColumn">
	        <c:choose>
	          <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">	      	      
<form:input path="planDataUi.deferralMinPercent" disabled="${disableFieldsForAutoOrSignUp}" onblur="validateDeferralLimitMin(this)" size="10" cssClass="numericInput"/>%
	      	  </c:when>
	          <c:otherwise>
                <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_PERCENTAGE_RATE_SCALE}" value="${planDataForm.planDataUi.planData.planDeferral.deferralMinPercent}"/>${empty planDataForm.planDataUi.planData.planDeferral.deferralMinPercent ? '' : '%'}
			  </c:otherwise>
			</c:choose>
	      </td>
	    </tr>
	  </table>
	</div>
	
	<!--  aci changes  -->
 <div class="subsubhead">Automatic Contribution Increases</div>  
	<div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="contributionsLabelColumn">
          <ps:fieldHilight name="planDataUi.planData.aciAllowed" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
          <ps:fieldHilight name="planDataUi.aciEffectiveDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
          Does the plan provide for automatic contribution increases?
          </td>
          <td class="dataColumn">
	     <c:choose>
	       <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:radiobutton disabled="${disableFieldsForAutoOrSignUp}" path="planDataUi.planData.aciAllowed" value="${planDataConstants.YES_CODE}"/>Yes<span id="planDataUi_automaticContributionEffectiveDate_span" style="display: none">, as of

<form:input path="planDataUi.aciEffectiveDate" disabled="${disableFieldsForAutoOrSignUp}" maxlength="10" onblur="validateAciEffectiveDate(this)" onchange="setDirtyFlag();" size="10" id="aciEffectiveDateId"/>






				<c:if test="${planDataForm.editMode}">
                  <img id="aciEffectiveDateIdImage" onclick="return handleDateIconClicked(event, 'aciEffectiveDateId');" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
                 </c:if>
				<c:if test="${planDataForm.confirmMode}">
                  <img id="aciEffectiveDateIdImage"  src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
				</c:if>
			</span>
            
<form:radiobutton disabled="${disableFieldsForAutoOrSignUp}" path="planDataUi.planData.aciAllowed" value="${planDataConstants.NO_CODE}"/>No
<form:radiobutton disabled="${disableFieldsForAutoOrSignUp}" path="planDataUi.planData.aciAllowed" value="${planDataConstants.UNSPECIFIED_CODE}"/>Unspecified
            </c:when>
            <c:otherwise>
	          <c:if test="${planDataForm.planDataUi.planData.aciAllowed == planDataConstants.YES_CODE }"> Yes<c:if test="${planDataForm.planDataUi.planData.planAutoContributionIncrease.effectiveDate > now}">,
	            effective <fmt:formatDate value="${planDataForm.planDataUi.planData.planAutoContributionIncrease.effectiveDate}" pattern="MMMM d, yyyy" />
	          	</c:if>
	          </c:if>
	          <c:if test="${planDataForm.planDataUi.planData.aciAllowed == planDataConstants.NO_CODE }"> No        </c:if>
	          <c:if test="${planDataForm.planDataUi.planData.aciAllowed == planDataConstants.UNSPECIFIED_CODE }">    </c:if>          
            </c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr>
          <td valign="top" class="contributionsLabelColumn">
          <ps:fieldHilight name="planDataUi.planData.aciAppliesTo" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
          &nbsp;&nbsp;&nbsp;Applies to
          <c:if test="${!planDataForm.editMode and !planDataForm.confirmMode}">
         	<ps:fieldHilight name="planDataUi.aciAppliesToEffectiveDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
          </c:if>
          </td>
          <td class="dataColumn" valign="top">
	     <c:choose>
	       <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<div><form:radiobutton disabled="${disableFieldsForAuto}" path="planDataUi.planData.planAutoContributionIncrease.appliesTo" value="${aciConstants.APPLY_TO_PPT_ALL_PARTICIPANT_CODE}"/>${aciConstants.APPLY_TO_PPT_ALL_PARTICIPANT_DISPLAY}</div>
<form:hidden path="planDataUi.planData.newParticipantApplyDateDisabled"/>
<div><form:radiobutton disabled="${disableFieldsForAuto}" path="planDataUi.planData.planAutoContributionIncrease.appliesTo" value="${aciConstants.APPLY_TO_PPT_AUTO_ENROLLED_PARTICIPANTS_CODE}"/>${aciConstants.APPLY_TO_PPT_AUTO_ENROLLED_PARTICIPANTS_DISPLAY}</div>
<div><form:radiobutton disabled="${disableFieldsForAuto}" path="planDataUi.planData.planAutoContributionIncrease.appliesTo" value="${aciConstants.APPLY_TO_PPT_NEW_PARTICIPANT_CODE}"/>${aciConstants.APPLY_TO_PPT_NEW_PARTICIPANT_DISPlAY }<span><ps:fieldHilight name="planDataUi.aciAppliesToEffectiveDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>, effective

<form:input path="planDataUi.aciAppliesToEffectiveDate" disabled="${disableFieldsForAuto}" maxlength="10" onblur="validateAciAppliesToEffectiveDate(this)" onchange="setDirtyFlag();" size="10" id="aciAppliesToEffectiveDateId"/>






				<c:if test="${planDataForm.editMode}">
                  <img id="aciAppliesToEffectiveDateIdImage" onclick="return handleDateIconClicked(event, 'aciAppliesToEffectiveDateId');" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
                 </c:if>
				<c:if test="${planDataForm.confirmMode}">
                  <img id="aciAppliesToEffectiveDateIdImage"  src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
				</c:if>
				</span>
            </div>
            </c:when>
            <c:otherwise>
              <c:if test="${planDataForm.planDataUi.planData.aciAllowed == planDataConstants.YES_CODE}" >
              ${planDataForm.planDataUi.planData.planAutoContributionIncrease.aciAppliesToDisplay}<c:if test="${planDataForm.planDataUi.planData.planAutoContributionIncrease.appliesTo == aciConstants.APPLY_TO_PPT_NEW_PARTICIPANT_CODE}">,
              effective date&nbsp;<c:out value="${planDataForm.planDataUi.aciAppliesToEffectiveDate}"/>
	          </c:if>
	          </c:if>
            </c:otherwise>
            </c:choose>
          </td>
        </tr>
	     <c:choose>
	       <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
        <tr>
          <td class="contributionsLabelColumn">
          <ps:fieldHilight name="planDataUi.aciApplyDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>          
          &nbsp;&nbsp;&nbsp;When are annual increases applied? </td>
          <td class="dataColumn">
<form:input path="planDataUi.aciApplyDate" disabled="${disableFieldsForAuto}" maxlength="5" onblur="validateAciApplyDate(this)" onchange="setDirtyFlag();" size="5" id="aciApplyDateId"/> (mm/dd)






          </td>
        </tr>
        <tr>
          <td class="contributionsLabelColumn">
            <ps:fieldHilight name="planDataUi.planData.aciDefaultIncreasePercent" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
            &nbsp;&nbsp;&nbsp;Default annual increase 
          </td>
          <td class="dataColumn">
          <c:if test="${planDataForm.planDataUi.planData.csfPptChangeDeferralsOnline eq 'Y' }">
<form:input path="planDataUi.planData.planAutoContributionIncrease.defaultIncreasePercent" disabled="${disableFieldsForAuto}" onblur="validateDefaultAnnualIncrease(this, 'Y')" size="8" cssClass="numericInput" id="autoIncreasePct"/>%
          </c:if>
          <c:if test="${planDataForm.planDataUi.planData.csfPptChangeDeferralsOnline ne 'Y' }">
<form:input path="planDataUi.planData.planAutoContributionIncrease.defaultIncreasePercent" disabled="${disableFieldsForAuto}" onblur="validateDefaultAnnualIncreaseWithDecimal(this, 'Y')" size="8" cssClass="numericInput" id="autoIncreasePct"/>%
          </c:if>
          </td>
        </tr>
        <tr>
          <td class="contributionsLabelColumn">
              <ps:fieldHilight name="planDataUi.planData.aciDefaultAutoIncreaseMaxPercent" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
              &nbsp;&nbsp;&nbsp;Default maximum for automatic increases
          </td>
          <td class="dataColumn">
           <c:if test="${planDataForm.planDataUi.planData.csfPptChangeDeferralsOnline eq 'Y' }">
<form:input path="planDataUi.planData.planAutoContributionIncrease.defaultAutoIncreaseMaxPercent" disabled="${disableFieldsForAuto}" onblur="validateDefaultMaximumAutoIncrease(this, 'Y')" size="8" cssClass="numericInput" id="autoIncreaseMax"/>%
           </c:if>
           <c:if test="${planDataForm.planDataUi.planData.csfPptChangeDeferralsOnline ne 'Y' }">
<form:input path="planDataUi.planData.planAutoContributionIncrease.defaultAutoIncreaseMaxPercent" disabled="${disableFieldsForAuto}" onblur="validateDefaultMaximumAutoIncreaseWithDecimal(this, 'Y')" size="8" cssClass="numericInput" id="autoIncreaseMax"/>%
           </c:if>
          </td>
        </tr>
            </c:when>
            <c:otherwise>
        
        <tr>
          <td class="contributionsLabelColumn">&nbsp;&nbsp;&nbsp;When are annual increases applied? </td>
          <td class="dataColumn">
              <c:if test="${planDataForm.planDataUi.planData.aciAllowed == planDataConstants.YES_CODE}" >
          	          <c:out value="${planDataForm.planDataUi.aciApplyDate}"/>&nbsp;(mm/dd) 
			  </c:if>
          </td>
        </tr>
        <tr>
          <td class="contributionsLabelColumn">&nbsp;&nbsp;&nbsp;Default annual increase </td>
          <td class="dataColumn">
             <c:if test="${planDataForm.planDataUi.planData.aciAllowed == planDataConstants.YES_CODE}" >
                <c:if test="${planDataForm.planDataUi.planData.csfPptChangeDeferralsOnline eq 'Y' }">
	          	<fmt:formatNumber value="${planDataForm.planDataUi.planData.planAutoContributionIncrease.defaultIncreasePercent}" maxFractionDigits="0" />${empty planDataForm.planDataUi.planData.planAutoContributionIncrease.defaultIncreasePercent ? '' : '%'}
			    </c:if>
			    <c:if test="${planDataForm.planDataUi.planData.csfPptChangeDeferralsOnline ne 'Y' }">
			    <fmt:formatNumber value="${planDataForm.planDataUi.planData.planAutoContributionIncrease.defaultIncreasePercent}" minFractionDigits="0" maxFractionDigits="2" />${empty planDataForm.planDataUi.planData.planAutoContributionIncrease.defaultIncreasePercent ? '' : '%'}
			    </c:if>
			</c:if>
          </td>
        </tr>
        <tr>
          <td class="contributionsLabelColumn">&nbsp;&nbsp;&nbsp;Default maximum for automatic increases </td>
          <td class="dataColumn">
            <c:if test="${planDataForm.planDataUi.planData.aciAllowed == planDataConstants.YES_CODE}" >
                <c:if test="${planDataForm.planDataUi.planData.csfPptChangeDeferralsOnline eq 'Y' }">
              	<fmt:formatNumber value="${planDataForm.planDataUi.planData.planAutoContributionIncrease.defaultAutoIncreaseMaxPercent}" maxFractionDigits="0"/>${empty planDataForm.planDataUi.planData.planAutoContributionIncrease.defaultAutoIncreaseMaxPercent ? '' : '%'}
          	    </c:if>
          	    <c:if test="${planDataForm.planDataUi.planData.csfPptChangeDeferralsOnline ne 'Y' }">
          	    <fmt:formatNumber value="${planDataForm.planDataUi.planData.planAutoContributionIncrease.defaultAutoIncreaseMaxPercent}" minFractionDigits="0" maxFractionDigits="2"/>${empty planDataForm.planDataUi.planData.planAutoContributionIncrease.defaultAutoIncreaseMaxPercent ? '' : '%'}
          	    </c:if>
          	</c:if>
          </td>
        </tr>     
            </c:otherwise>
            </c:choose>
      </table>
  	</div>
	<div class="subsubhead">Employer Contributions</div>
	<div id="employerContibutionDiv" style="background-color: white">
	</div>
	<c:choose>
	<c:when test="${planDataForm.editMode and fn:length(employerMoneyTypes) > 0}">
	<div style="padding-top: 6px; padding-bottom: 6px;padding-left: 3px; background-color: white">
		<span  id="createFormulaSpanOn" style="color:#000099;text-decoration:underline" onmouseover="this.style.cursor='hand'" onclick="createContributionFormula()">Create additional Contribution Formula</span>
		<span  id="createFormulaSpanOff" style="color:gray;display:none;text-decoration:underline;">Create additional Contribution Formula</span>
	</div>
	</c:when>
	</c:choose>
    </div>
  </div>
</div>
<form:hidden path="planDataUi.planData.numberOfRuleSets"/>
<script type="text/javascript">
<c:forEach items="${planDataForm.planDataUi.employerRulesets}" var="rule" varStatus="status">
	<c:if test="${rule != null }">
		ruleSets.push("${e:forJavaScriptBlock(rule)}");
	</c:if>
</c:forEach> 
</script>
<c:forEach items="${planDataForm.planDataUi.employerRulesets}" var="rule" varStatus="status">
	<c:if test="${rule != null }">
		<span style="display:none;" id="${status.index+1}_max_match_type_error_content">
		     <ps:fieldHilight name="planDataUi.planData.${status.index}_max_match_type" singleDisplay="true" displayToolTip="true"/>
		</span>
		<span style="display:none;" id="${status.index+1}_first_percent_type_error_content">
		     <ps:fieldHilight name="planDataUi.planData.${status.index}_first_percent_type" singleDisplay="true"  displayToolTip="true"/>
		</span>
		<span style="display:none;" id="${status.index+1}_next_percent_type_error_content">
		     <ps:fieldHilight name="planDataUi.planData.${status.index}_next_percent_type" singleDisplay="true"  displayToolTip="true"/>
		</span>
		<span style="display:none;" id="${status.index+1}_next_percent_error_content">
		     <ps:fieldHilight name="planDataUi.planData.${status.index}_next_percent" singleDisplay="true"  displayToolTip="true"/>
		</span>
		<span style="display:none;" id="${status.index+1}_type_error_content">
		     <ps:fieldHilight name="planDataUi.planData.${status.index}_type" singleDisplay="true"  displayToolTip="true"/>
		</span>
		<span style="display:none;" id="${status.index+1}_money_type_error_content">
		     <ps:fieldHilight name="planDataUi.planData.${status.index}_money_types" singleDisplay="true"  displayToolTip="true"/>
		</span>
	</c:if>
</c:forEach> 
