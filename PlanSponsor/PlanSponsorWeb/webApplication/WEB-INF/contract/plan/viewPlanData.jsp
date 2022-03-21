<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%-- Define static constants --%>
<un:useConstants scope="request" var="lookupConstants" className="com.manulife.pension.cache.CodeLookupCache"/>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<%-- Load IE 6 specific Styles to handle width issues --%>
<!--[if lt IE 7]>
  <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
<![endif]-->

<%-- Define required collections --%>
<c:set var="vestingSchedules" scope="request" value="${planDataForm.lookupData['VESTING_SCHEDULES']}"/>
<c:set var="optionsForUnvestedAmounts" scope="request" value="${planDataForm.lookupData['PLAN_OPTIONS_FOR_UNVESTED_AMOUNTS']}"/>
<c:set var="vestingServiceCreditMethods" scope="request" value="${planDataForm.lookupData['VESTING_SERVICE_CREDIT_METHOD']}"/>
<c:set var="planEntryFrequencies" scope="request" value="${planDataForm.lookupData['PLAN_ENTRY_FREQUENCY']}"/>
<c:set var="payrollFrequencies" scope="request" value="${planDataForm.lookupData['PAYROLL_FREQUENCY']}"/>
<c:set var="moneyTypes" scope="request" value="${planDataForm.lookupData['MONEY_TYPES_BY_CONTRACT']}"/>
<c:set var="employeeMoneyTypes" scope="request" value="${planDataForm.lookupData['EMPLOYEE_MONEY_TYPES_BY_CONTRACT']}"/>
<c:set var="moneySources" scope="request" value="${planDataForm.lookupData['MONEY_SOURCES_BY_CONTRACT']}"/>
<c:set var="withdrawalReasons" scope="request" value="${planDataForm.lookupData['PLAN_WITHDRAWAL_REASONS']}"/>
<c:set var="loanTypes" scope="request" value="${planDataForm.lookupData[lookupConstants.PLAN_LOAN_TYPES]}"/>
 <c:set var="employerMoneyTypes" scope="request" value="${planDataForm.lookupData['EMPLOYER_MONEY_TYPES']}"/>
 
<c:set var="eligibilityCreditMethods" scope="request" value="${planDataForm.lookupData['SERVICE_CREDITING_METHOD']}"/>
<c:set var="eligibilityPlanEntryFrequencies" scope="request" value="${planDataForm.lookupData['PLAN_FREQUENCY']}"/>
<c:set var="eligibilityMinimumAges" scope="request" value="${planDataForm.lookupData['MINIMUM_AGE']}"/>
<c:set var="eligibilityHoursOfServices" scope="request" value="${planDataForm.lookupData['HOURS_OF_SERVICE']}"/>
<c:set var="eligibilityPeriodOfServices" scope="request" value="${planDataForm.lookupData['PERIOD_OF_SERVICE']}"/>
<c:set var="eligibilityPeriodOfServiceUnits" scope="request" value="${planDataForm.lookupData['PERIOD_OF_SERVICE_UNIT']}"/>

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_LINK_HISTORY}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="planHistoryLink"/>

<%-- Define variables --%>
<c:set var="disableFields" scope="request" value="true"/>
<c:set var="disableFieldsForAutoOrSignUp" scope="request" value="false"/>
<c:set var="disableFieldsForAuto" scope="request" value="false"/>

<ps:form method="POST" action="/do/contract/planData/view/" modelAttribute="planDataForm" name="planDataForm">
  <div>
    <div class="messagesBox">
      <%-- Override max height if print friendly is on so we don't scroll --%>
      <content:errors scope="session"/>
    </div>
    <c:if test="${planDataForm.userCanViewHistory and not param.printFriendly}">             
      <div class="printHistoryContainer">
        <a href="<c:url value='/do/contract/planDataHistory/?planId=${planDataForm.planDataUi.planData.planId}'/>">
          <content:getAttribute beanName="planHistoryLink" attribute="text"/>
        </a>
      </div>
    </c:if>
    <c:if test="${not param.printFriendly}">
      <jsp:include flush="true" page="expandCollapseAllSections.jsp"></jsp:include>
    </c:if>
    <jsp:include flush="true" page="general.jsp"></jsp:include>
    <jsp:include flush="true" page="eligibilityAndParticipation.jsp"></jsp:include>
    <jsp:include flush="true" page="contributions.jsp"></jsp:include>
    <jsp:include flush="true" page="vestingAndForfeitures.jsp"></jsp:include>
    <jsp:include flush="true" page="forfeitures.jsp"></jsp:include>
    <jsp:include flush="true" page="withdrawals.jsp"></jsp:include>
    <jsp:include flush="true" page="loans.jsp"></jsp:include>
    <jsp:include flush="true" page="otherPlanInformation.jsp"></jsp:include>
    <form:hidden path="action" id="submitAction" disabled="true"/>
    <jsp:include flush="true" page="viewPlanDataButtons.jsp"></jsp:include>
  </div>
  <jsp:include flush="true" page="footer.jsp"></jsp:include>
</ps:form>
<jsp:include flush="true" page="viewHandlers.jsp"></jsp:include>
<jsp:include flush="true" page="viewUpdates.jsp"></jsp:include>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript">initViewPlanData();</script>