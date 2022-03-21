<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- Define static constants --%>
<un:useConstants var="formConstants" className="com.manulife.pension.ps.web.contract.PlanDataForm"/>
<un:useConstants scope="request" var="lookupConstants" className="com.manulife.pension.cache.CodeLookupCache" />

<%-- Load IE 6 specific Styles to handle width issues --%>
<!--[if lt IE 7]>
  <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
<![endif]-->

<%-- Define required collections --%>
<c:set var="vestingSchedules" scope="request" value="${planDataForm.lookupData['VESTING_SCHEDULES']}"/>
<c:set var="moneyTypesForLoans" scope="request" value="${planDataForm.lookupData['ALLOWABLE_MONEY_TYPES_FOR_LOANS']}"/>
<c:set var="moneyTypesForWithdrawals" scope="request" value="${planDataForm.lookupData['ALLOWABLE_MONEY_TYPES_FOR_WITHDRAWALS']}"/>
<c:set var="optionsForUnvestedAmounts" scope="request" value="${planDataForm.lookupData['PLAN_OPTIONS_FOR_UNVESTED_AMOUNTS']}"/>
<c:set var="vestingServiceCreditMethods" scope="request" value="${planDataForm.lookupData['VESTING_SERVICE_CREDIT_METHOD']}"/>
<c:set var="planEntryFrequencies" scope="request" value="${planDataForm.lookupData['PLAN_ENTRY_FREQUENCY']}"/>
<c:set var="payrollFrequencies" scope="request" value="${planDataForm.lookupData['PAYROLL_FREQUENCY']}"/>
<c:set var="moneyTypes" scope="request" value="${planDataForm.lookupData['MONEY_TYPES_BY_CONTRACT']}"/>
<c:set var="employeeMoneyTypes" scope="request" value="${planDataForm.lookupData['EMPLOYEE_MONEY_TYPES_BY_CONTRACT']}"/>
<c:set var="employerMoneyTypes" scope="request" value="${planDataForm.lookupData['EMPLOYER_MONEY_TYPES']}"/>
<c:set var="moneySources" scope="request" value="${planDataForm.lookupData['MONEY_SOURCES_BY_CONTRACT']}"/>
<c:set var="withdrawalReasons" scope="request" value="${planDataForm.lookupData['PLAN_WITHDRAWAL_REASONS']}"/>
<c:set var="loanTypes" scope="request" value="${planDataForm.lookupData[lookupConstants.PLAN_LOAN_TYPES]}"/>

<c:set var="eligibilityPlanEntryFrequencies" scope="request" value="${planDataForm.lookupData['PLAN_FREQUENCY']}"/>
<c:set var="eligibilityCreditMethods" scope="request" value="${planDataForm.lookupData['SERVICE_CREDITING_METHOD']}"/>
<c:set var="eligibilityMinimumAges" scope="request" value="${planDataForm.lookupData['MINIMUM_AGE']}"/>
<c:set var="eligibilityHoursOfServices" scope="request" value="${planDataForm.lookupData['HOURS_OF_SERVICE']}"/>
<c:set var="eligibilityPeriodOfServices" scope="request" value="${planDataForm.lookupData['PERIOD_OF_SERVICE']}"/>
<c:set var="eligibilityPeriodOfServiceUnits" scope="request" value="${planDataForm.lookupData['PERIOD_OF_SERVICE_UNIT']}"/>

<%-- Define variables --%>
<c:set var="disableFields" scope="request" value="false"/>
<c:if test="${planDataForm.editMode}">
<c:set var="disableFieldsForAutoOrSignUp" scope="request" value="${(planDataForm.planDataUi.planData.disableAttrForUpdate && planDataForm.planDataUi.planData.autoOrSignUp != null) ? 'true' : 'false'}"/>
<c:set var="disableFieldsForAuto" scope="request" value="${(planDataForm.planDataUi.planData.disableAttrForUpdate && planDataForm.planDataUi.planData.autoOrSignUp == 'A') ? 'true' : 'false'}"/>
</c:if>

<ps:form method="POST" action="/do/contract/planData/edit/" modelAttribute="planDataForm" name="planDataForm" >
  <div>
    <form:hidden id="submitAction" disabled="true" path="action" />
    <input type="hidden" property="${formConstants.RESET_CHECKBOXES}" value="true"/>
    <form:hidden path="dirty" id="dirtyFlagId"/>
    <div class="messagesBox">
      <content:errors scope="session"/>
    </div>
    <jsp:include flush="true" page="expandCollapseAllSections.jsp"></jsp:include>
    <jsp:include flush="true" page="general.jsp"></jsp:include>
    <jsp:include flush="true" page="eligibilityAndParticipation.jsp"></jsp:include>
    <jsp:include flush="true" page="contributions.jsp"></jsp:include>
    <jsp:include flush="true" page="vestingAndForfeitures.jsp"></jsp:include>
    <jsp:include flush="true" page="forfeitures.jsp"></jsp:include>
    <jsp:include flush="true" page="withdrawals.jsp"></jsp:include>
    <jsp:include flush="true" page="loans.jsp"></jsp:include>
    <jsp:include flush="true" page="otherPlanInformation.jsp"></jsp:include>
    <jsp:include flush="true" page="editPlanDataButtons.jsp"></jsp:include>
  </div>
</ps:form>
<jsp:include flush="true" page="editHandlers.jsp"></jsp:include>
<jsp:include flush="true" page="editUpdates.jsp"></jsp:include>
<jsp:include flush="true" page="errorMessages.jsp"></jsp:include>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/input.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/employeeSnapshot.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript">

$(document).ready(function() {
  initEditPlanData();
});

</script>