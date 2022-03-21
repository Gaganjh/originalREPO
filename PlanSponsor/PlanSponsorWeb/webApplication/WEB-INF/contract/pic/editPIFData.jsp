<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="formConstants" className="com.manulife.pension.ps.web.pif.PIFDataForm"/>

<c:set var="selectedTab" scope="request" value="${pifDataForm.selectedTab}"/>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%-- Define required collections --%>
<c:set var="vestingSchedules" scope="request" value="${pifDataForm.lookupData['VESTING_SCHEDULES']}"/>
<c:set var="vestingServiceCreditMethods" scope="request" value="${pifDataForm.lookupData['VESTING_SERVICE_CREDIT_METHOD']}"/>
<c:set var="employerMoneyTypes" scope="request" value="${pifDataForm.pifDataUi.employerMoneyTypeList}"/>
<c:set var="withdrawalReasons" scope="request" value="${pifDataForm.lookupData['PLAN_WITHDRAWAL_REASONS']}"/>
<%--
<c:set var="moneyTypesForLoans" scope="request" value="${pifDataForm.lookupData['ALLOWABLE_MONEY_TYPES_FOR_LOANS']}"/>
<c:set var="moneyTypesForWithdrawals" scope="request" value="${pifDataForm.lookupData['ALLOWABLE_MONEY_TYPES_FOR_WITHDRAWALS']}"/>
<c:set var="optionsForUnvestedAmounts" scope="request" value="${pifDataForm.lookupData['PLAN_OPTIONS_FOR_UNVESTED_AMOUNTS']}"/>
<c:set var="planEntryFrequencies" scope="request" value="${pifDataForm.lookupData['PLAN_ENTRY_FREQUENCY']}"/>
<c:set var="payrollFrequencies" scope="request" value="${pifDataForm.lookupData['PAYROLL_FREQUENCY']}"/>
<c:set var="moneyTypes" scope="request" value="${pifDataForm.lookupData['MONEY_TYPES_BY_CONTRACT']}"/>
<c:set var="employeeMoneyTypes" scope="request" value="${pifDataForm.lookupData['EMPLOYEE_MONEY_TYPES_BY_CONTRACT']}"/>
<c:set var="moneySources" scope="request" value="${pifDataForm.lookupData['MONEY_SOURCES_BY_CONTRACT']}"/>
<c:set var="loanTypes" scope="request" value="${pifDataForm.lookupData[lookupConstants.PLAN_LOAN_TYPES]}"/>
--%>

<c:set var="eligibilityPlanEntryFrequencies" scope="request" value="${pifDataForm.lookupData['PLAN_FREQUENCY']}"/>
<c:set var="eligibilityCreditMethods" scope="request" value="${pifDataForm.lookupData['SERVICE_CREDITING_METHOD']}"/>
<c:set var="eligibilityMinimumAges" scope="request" value="${pifDataForm.lookupData['MINIMUM_AGE']}"/>
<c:set var="eligibilityHoursOfServices" scope="request" value="${pifDataForm.lookupData['HOURS_OF_SERVICE']}"/>
<c:set var="eligibilityPeriodOfServices" scope="request" value="${pifDataForm.lookupData['PERIOD_OF_SERVICE']}"/>
<c:set var="eligibilityPeriodOfServiceUnits" scope="request" value="${pifDataForm.lookupData['PERIOD_OF_SERVICE_UNIT']}"/>

<%-- Define variables --%>
<c:set var="disableFields" scope="request" value="false"/>
<c:if test="${pifDataForm.editMode}">

<c:set var="disableFieldsForAutoOrSignUp" scope="request" value="false"/>
<c:set var="disableFieldsForAuto" scope="request" value="false"/>
<%-- TODO: Need to create the properties disableAttrForUpdate and autoOrSignUpin PIVO
<c:set var="disableFieldsForAutoOrSignUp" scope="request" value="${(planDataForm.planDataUi.planData.disableAttrForUpdate && planDataForm.planDataUi.planData.autoOrSignUp != null) ? 'true' : 'false'}"/>
<c:set var="disableFieldsForAuto" scope="request" value="${(planDataForm.planDataUi.planData.disableAttrForUpdate && planDataForm.planDataUi.planData.autoOrSignUp == 'A') ? 'true' : 'false'}"/>
--%>
</c:if>

<content:contentBean
  contentId="<%=ContentConstants.ACKNOWLEDGEMENT_TEXT_FOR_TPA_USER%>"
  type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="acknowledgementText" /> 
  
<content:contentBean
	contentId="80833"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="saveButtonHoverOverText" />  

<script language="JavaScript" type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>  

	<%-- Defines page level event handlers --%>
<script type="text/javascript">

var utilities = {
	    
	    // Shows loading panel message
	    showWaitPanel : function() {
	        waitPanel = document.getElementById("wait_c");
	      
	        if (waitPanel == undefined || waitPanel == null || waitPanel.style.visibility != "visible") {
	        	
	            loadingPanel = new YAHOO.widget.Panel("wait",  
	                                {   width: "250px", 
	                                    height:"50px",
	                                    fixedcenter: true, 
	                                    close: false, 
	                                    draggable: false, 
	                                    zindex:4,
	                                    modal: true,
	                                    visible: false,
	                                    constraintoviewport: true
	                                } 
	                            );
	            loadingPanel.setBody("<span style='padding-left:20px;float:right;padding-right:30px;padding-top:12px;'>One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
	            loadingPanel.render(document.body);
	            loadingPanel.show();
	           
	           
	        }       
	    },

	    /**
	    * hides the loading panel
	    */
	    hideWaitPanel: function () {	
	        loadingPanel.hide();	
	    }
	        
	 };
/**
 * Defines an object to hold section meta information.
 */ 
function SectionMetaInfo(dataDivId) {
  this.dataDivId=dataDivId;
} 

/**
 * Collection of section meta information.
 */
var sectionMetaInfoArray = new Array(); 
    sectionMetaInfoArray['general'] 
      = new SectionMetaInfo('generalTabDivId');
    sectionMetaInfoArray['money'] 
      = new SectionMetaInfo('moneyTabDivId');	  
    sectionMetaInfoArray['eligibility'] 
      = new SectionMetaInfo('eligibilityTabDivId');
    sectionMetaInfoArray['contribution'] 
      = new SectionMetaInfo('contributionTabDivId');
    sectionMetaInfoArray['withdrawals'] 
      = new SectionMetaInfo('withdrawalsTabDivId');
    sectionMetaInfoArray['loans'] 
      = new SectionMetaInfo('loansTabDivId');
    sectionMetaInfoArray['vesting'] 
      = new SectionMetaInfo('vestingTabDivId');
    sectionMetaInfoArray['forfeitures'] 
      = new SectionMetaInfo('forfeituresTabDivId');
    sectionMetaInfoArray['otherPlanInformation'] 
      = new SectionMetaInfo('otherPlanInformationTabDivId');

/**
 * Function that handles expansion of a data tab.
 */
function expandDataDiv(section) {

   var sectionInfo = sectionMetaInfoArray[section];
   <%-- Expand the data div --%>
   showNodeById(sectionInfo.dataDivId);

}

// Shows the specified element (assumes we are dealing with block elements)
function showNode(node) {
  try {
    node.style.display='block';
  }
  catch(E){}
}

// Shows the specified element by id
function showNodeById(id) {
  showNode(document.getElementById(id));
}

/**
 * Function that handles expansion of a data tab.
 */
function collapseDataDiv(section) {
   var sectionInfo = sectionMetaInfoArray[section];   
   <%-- Hide the data div --%>
    hideNodeById(sectionInfo.dataDivId);    
}

// Hides the specified element
function hideNode(node) {
  try {
    node.style.display='none';
  }
  catch(E){}
}

// Hides the specified element by id
function hideNodeById(id) {
  hideNode(document.getElementById(id));
}

/**
 * Function that handles collapse of all the data sections.
 */
 function collapseAllDataDivs() {
   collapseDataDiv('general')
   collapseDataDiv('money')
   collapseDataDiv('eligibility')
   collapseDataDiv('contribution')
   collapseDataDiv('withdrawals')
   collapseDataDiv('loans')
   collapseDataDiv('vesting')
   collapseDataDiv('forfeitures')
   collapseDataDiv('otherPlanInformation')
 }

/**
 * Initialization function that is called when the page first loads.
 */
function initEditPlanData(section) {
  collapseAllDataDivs();
  <c:if test="${selectedTab == 'general'}">
	document.pifDataForm.fromTab.value = 'general';  
    expandDataDiv('general');
  </c:if>
  <c:if test="${selectedTab == 'moneyType'}">
	document.pifDataForm.fromTab.value = 'moneyType';
    expandDataDiv('money');
  </c:if>  
  <c:if test="${selectedTab == 'eligibility'}">
  	document.pifDataForm.fromTab.value = 'eligibility';
    expandDataDiv('eligibility');
  </c:if> 
  <c:if test="${selectedTab == 'contribution'}">
  	document.pifDataForm.fromTab.value = 'contribution';
    expandDataDiv('contribution');
  </c:if> 
  <c:if test="${selectedTab == 'vesting'}">
  	document.pifDataForm.fromTab.value = 'vesting';
    expandDataDiv('vesting');
  </c:if>
  <c:if test="${selectedTab == 'forfeitures'}">
  	document.pifDataForm.fromTab.value = 'forfeitures';
    expandDataDiv('forfeitures');
  </c:if>  
  <c:if test="${selectedTab == 'withdrawals'}">
  	document.pifDataForm.fromTab.value = 'withdrawals';
    expandDataDiv('withdrawals');
  </c:if> 
  <c:if test="${selectedTab == 'loans'}">
  	document.pifDataForm.fromTab.value = 'loans';
    expandDataDiv('loans');
  </c:if>   
  <c:if test="${selectedTab == 'otherPlanInformation'}">
  	document.pifDataForm.fromTab.value = 'otherPlanInformation';
    expandDataDiv('otherPlanInformation');
  </c:if>  

  registerTrackChangesFunction(isPIFFormChanged);  
  protectLinks();	
}

function isPIFFormChanged() {
	return document.getElementById('dirtyFlagId').value;
}

function showTabData(section) {
	  collapseAllDataDivs();
	  expandDataDiv(section);
}
var submitted=false;
function getTabData(selectedTab) {
	utilities.showWaitPanel();
	//alert("hiiiii");
	if (!submitted) {
		submitted=true;
	var result;
  <c:if test="${selectedTab == 'moneyType'}"> 
	var rolloverContributionsPermitted = $("input[name='planInfoVO.pifMoneyType.rolloverContributionsPermitted']:checked").val();
	if (rolloverContributionsPermitted == "${planDataConstants.YES_CODE}") {
		result = ValidateRolloverContributions();
		if(!result){
			return false;
		}		
	}	
  </c:if>
  <c:if test="${selectedTab == 'eligibility'}"> 
		result = validateAutomaticEnrollment();
		if(!result){
			return false;
		}		
  </c:if>  
  <c:if test="${selectedTab == 'contribution'}"> 
		result = validateEmployeeDeferralElection();
		if(!result){
			return false;
		}
		
	
		result = validateAutomaticContributionIncreases();//
		if(!result){
			utilities.hideWaitPanel(); // hiding window if alerts intrupts the PW 
			return false;  
		}		
		result = validateMoneyTypeForRuleTypes();
		if(!result){
			return false;
		}
		result = validateContributionFormulaSectionTypes();
		if(!result){
			return false;
		}			
  </c:if>
	  
	   
	encodeRuleSets();
	document.pifDataForm.toTab.value = selectedTab;
	document.pifDataForm.submit();	
}
 else {
	utilities.hideWaitPanel();
}
}


</script>

<ps:form method="POST" action="/do/contract/pic/edit/" modelAttribute="pifDataForm" name="pifDataForm">
<form:hidden path="submissionId"/>
<form:hidden path="fromTab"/>
<form:hidden path="toTab"/>
<form:hidden path="selectedTab"/>
<form:hidden path="contractId"/>
<input type="hidden" name="${formConstants.RESET_CHECKBOXES}" value="true"/>
<form:hidden path="dirty" id="dirtyFlagId"/>
<%-- Error Table --%>
<div class="messagesBox" id="messagesBox" >
<ps:messages scope="request"  suppressDuplicateMessages="true" showOnlyWarningContent="true"/>
</div>	
<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
    <TR>
    <TD width="100%">	
	  <div>
		<table>
		   <tr style="height:10px">
			  <td> <b>${pifDataForm.planInfoVO.generalInformations.contractName}</b><img src="/assets/unmanaged/images/spacer.gif" width="1">|<img src="/assets/unmanaged/images/spacer.gif" width="3"><b>Contract:</b><img src="/assets/unmanaged/images/spacer.gif"  width="5">${pifDataForm.planInfoVO.generalInformations.contractNumber}</td>
		   </tr>
		</table>		
	    <jsp:include flush="true" page="picNavigationTabBar.jsp">
			<jsp:param name="selectedTab" value="general"/>
		</jsp:include>	
		<jsp:include flush="true" page="general.jsp"></jsp:include>
		<jsp:include flush="true" page="money.jsp"></jsp:include>
		<jsp:include flush="true" page="eligibility.jsp"></jsp:include>
		<jsp:include flush="true" page="contributions.jsp"></jsp:include>
		<jsp:include flush="true" page="vesting.jsp"></jsp:include>
		<jsp:include flush="true" page="forfeitures.jsp"></jsp:include>
		<jsp:include flush="true" page="withdrawals.jsp"></jsp:include>
		<jsp:include flush="true" page="loans.jsp"></jsp:include>
		<jsp:include flush="true" page="other.jsp"></jsp:include>
	  </div>
	  <br>	  
	  <div>
		<table border=0 cellSpacing=0 cellPadding=0 width="100%">
		<tr>
			<td valign="top">
				<script type="text/javascript">
				$(document).ready(function() {
					authorizationIndicatorId= "#planInfoVO_authorizationIndicator";
					$(authorizationIndicatorId).on("click",function() {
					authorizationIndicatorHiddenId = document.getElementById('authorizationIndicator');
						if($(authorizationIndicatorId).is(':checked')){ 
							authorizationIndicatorHiddenId.value='true';
						}else{
							authorizationIndicatorHiddenId.value='false';
						}
					});
				});
				</script>	
				<input type="checkbox" 
					id="planInfoVO_authorizationIndicator" 
					name="planInfoVO.authorizationIndicator" 
					value="true"						
					onclick="setDirtyFlag();"
					<c:if test="${pifDataForm.planInfoVO.authorizationIndicator}">checked="checked" </c:if> />
<input type="hidden" name="planInfoVO.authorizationIndicator" id="authorizationIndicator" /><%-- input - name="pifDataForm" --%>

			</td>	
			<td >
				${pifDataForm.acknowledgmentText}	
			</td>
		</tr>
		<tr>
			<td colspan="2">
			<jsp:include flush="true" page="editPIFDataButtons.jsp"></jsp:include>
			</td>	
		</tr>
		</table>
	  </div>
	</TD>
	</TR>
</TABLE>
</ps:form>

<jsp:include flush="true" page="editHandlers.jsp"></jsp:include>
<jsp:include flush="true" page="editUpdates.jsp"></jsp:include>
<jsp:include flush="true" page="errorMessages.jsp"></jsp:include>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/input.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/employeeSnapshot.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	//alert("ready fn");
  //initEditPlanData();
});
</script>

<SCRIPT type=text/javascript>initEditPlanData();</SCRIPT>
