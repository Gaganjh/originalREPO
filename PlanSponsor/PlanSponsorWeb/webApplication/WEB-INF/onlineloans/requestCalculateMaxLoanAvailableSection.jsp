<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>

<un:useConstants var="loanConstants" className="com.manulife.pension.service.loan.domain.LoanConstants" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanFields" className="com.manulife.pension.service.loan.LoanField" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />


<content:contentBean
  contentId="${loanContentConstants.CACULATE_MAX_AVAILABLE_FOR_LOAN_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="calcualteMaxAvailableForLoanSectionTitleText"/>
<content:contentBean
  contentId="${loanContentConstants.VESTING_EXPLANATION_LINK}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="vestingExplanationLinkText"/>
<content:contentBean
  contentName="${loanContentConstants.VESTING_PERCENTAGE_NON_NUMERIC}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="vestingPercentageNonNumericText"/>
<content:contentBean
  contentName="${loanContentConstants.VESTING_PERCENTAGE_INVALID_FORMAT}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="vestingPercentageInvalidFormatText"/>
<content:contentBean
  contentName="${loanContentConstants.VESTING_PERCENTAGE_TOO_HIGH}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="vestingPercentageTooHighText"/>
<content:contentBean
  contentName="${loanContentConstants.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS_NON_NUMERIC}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="maximumLoanBalanceInLast12MonthsNonNumericText"/>
<content:contentBean
  contentName="${loanContentConstants.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS_TOO_LOW}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="maximumLoanBalanceInLast12MonthsTooLowText"/>
<content:contentBean
  contentName="${loanContentConstants.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS_INVALID_FORMAT}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="maximumLoanBalanceInLast12MonthsInvalidFormatText"/>
<content:contentBean
  contentName="${loanContentConstants.OUTSTANDING_LOANS_COUNT_NON_NUMERIC}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="outstandingLoansCountNonNumericText"/>
<content:contentBean
  contentName="${loanContentConstants.OUTSTANDING_LOANS_COUNT_TOO_LOW}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="outstandingLoansCountTooLowText"/>
<content:contentBean
  contentName="${loanContentConstants.OUTSTANDING_LOANS_COUNT_TOO_HIGH}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="outstandingLoansCountTooHighText"/>
<content:contentBean
  contentName="${loanContentConstants.CURRENT_OUTSTANDING_LOAN_BALANCE_NON_NUMERIC}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="currentOutstandingLoanBalanceNonNumericText"/>
<content:contentBean
  contentName="${loanContentConstants.CURRENT_OUTSTANDING_LOAN_BALANCE_INVALID_FORMAT}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="currentOutstandingLoanBalanceNonInvalidFormatText"/>
<content:contentBean
  contentName="${loanContentConstants.CURRENT_OUTSTANDING_LOAN_BALANCE_TOO_LOW}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="currentOutstandingLoanBalanceTooLowText"/>
<content:contentBean
  contentId="${loanContentConstants.CACULATE_MAX_LOAN_AVAILABLE_SECTION_FOOTER}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="calculateMaxLoanAvailableFooterText"/>


<script language="JavaScript1.2" type="text/javascript">

var loan =
<json:object>
  <json:property name="highestOutstandingLoanBalanceWithin12Months"
                 value="${loan.maxBalanceLast12Months}"/>
  <json:property name="currentOutstandingLoanBalance"
                 value="${loan.currentOutstandingBalance}"/>
  <json:property name="applyIrs10KDollarRuleInd"
                 value="${loan.applyIrs10KDollarRuleInd}"/>
  <json:array name="moneyTypes" var="moneyType" items="${loan.moneyTypesWithAccountBalance}">
    <json:object>
      <json:property name="accountBalance" value="${moneyType.accountBalance}"/>
      <json:property name="loanBalance" value="${moneyType.loanBalance}"/>
      <json:property name="moneyTypeId" value="${moneyType.moneyTypeId}"/>
      <json:property name="contractMoneyTypeShortName" value="${moneyType.contractMoneyTypeShortName}"/>
      <json:property name="contractMoneyTypeLongName" value="${moneyType.contractMoneyTypeLongName}"/>
      <json:property name="moneyTypeAliasId" value="${moneyType.moneyTypeAliasId}"/>
      <json:property name="moneyTypeCategoryCode" value="${moneyType.moneyTypeCategoryCode}"/>
      <json:property name="vestingPercentage" value="${moneyType.vestingPercentage}"/>
      <json:property name="excludeIndicator" value="${moneyType.excludeIndicator}"/>
      <json:property name="vestingPercentageUpdateable" value="${moneyType.vestingPercentageUpdateable}"/>
    </json:object>
  </json:array>
  <json:array name="moneyTypesWithoutAccountBalance" var="moneyType" items="${loan.moneyTypesWithoutAccountBalance}">
    <json:object>
      <json:property name="accountBalance" value="${moneyType.accountBalance}"/>
      <json:property name="loanBalance" value="${moneyType.loanBalance}"/>
      <json:property name="moneyTypeId" value="${moneyType.moneyTypeId}"/>
      <json:property name="contractMoneyTypeShortName" value="${moneyType.contractMoneyTypeShortName}"/>
      <json:property name="contractMoneyTypeLongName" value="${moneyType.contractMoneyTypeLongName}"/>
      <json:property name="moneyTypeAliasId" value="${moneyType.moneyTypeAliasId}"/>
      <json:property name="moneyTypeCategoryCode" value="${moneyType.moneyTypeCategoryCode}"/>
      <json:property name="vestingPercentage" value="${moneyType.vestingPercentage}"/>
      <json:property name="excludeIndicator" value="${moneyType.excludeIndicator}"/>
      <json:property name="vestingPercentageUpdateable" value="${moneyType.vestingPercentageUpdateable}"/>
    </json:object>
  </json:array>
</json:object>
;

var loanPlanData =
<json:object>
  <json:property name="maximumLoanAmount"
                 value="${loanPlanData.maximumLoanAmount}"/>
  <json:property name="minimumLoanAmount"
                 value="${loanPlanData.minimumLoanAmount}"/>
  <json:property name="maximumLoanPercentage"
                 value="${loanPlanData.maximumLoanPercentage}"/>
  <json:property name="payrollFrequency"
                 value="${loanPlanData.payrollFrequency}"/>
</json:object>
;


function doPrintFiendlyVestingExplanation(){
  var contractId = "${loan.contractId}";
  var profileId = "${loan.participantProfileId}";
  var printURL = "/do/census/viewVestingInformation/" + "?profileId=" + profileId + "&source=onlineLoans&printFriendly=true&contractId=" + contractId;
  options="toolbar=1,status=1,menubar=1,scrollbars=1,resizable=1,width=720,height=480";
  newwindow=window.open(printURL, "general", options);
  if (navigator.appName=="Netscape") {
      newwindow.focus();
  }
}


var isMaxBalanceLast12MonthsEditSuccess = true;

function onMaxBalanceLast12MonthsChangeSuccess(e, callbackParams) {
  var field = e.target;
  var value = field.value;
  var validationIdentifier = callbackParams[3];

  // invalidCharCheck is the first edit, so we always show the success icon.
  // For the other edits, if a previous edit was successful, only then do we 
  // want to hide the error icon.  If a previous edit failed, we don't want 
  // to overwrite the error icon already displayed. 
  if (validationIdentifier == 'invalidCharCheck') {
    showIconForSuccess('${loanFields.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS.fieldName}');
    isMaxBalanceLast12MonthsEditSuccess = true;
  } else if (isMaxBalanceLast12MonthsEditSuccess) {
    showIconForSuccess('${loanFields.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS.fieldName}');
  }

  loan.highestOutstandingLoanBalanceWithin12Months = numberUtils.parseNumber(value);
  recalculateBalances(false);
<c:if test="${displayRules.loanAmountDisplayOnlyRecalculated}">
  resetLoanAmount();
</c:if>
  if (e.type == 'blur') {
    if (! isNaN(value) && value.length > 0) {
      field.value = numberUtils.formatAmount(numberUtils.deformatNumber(value), false, true);
    }
  }
}

function onMaxBalanceLast12MonthsChangeFailure(e, callbackParams) {
  showIconForFailure('${loanFields.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS.fieldName}');
  var alertType = callbackParams[0];
  var errorMessage = callbackParams[1];
  var msgParm1 = callbackParams[2];
  var validationIdentifier = callbackParams[3];

  if (alertType == 'alert') {
    var value = e.target.value;
    if (validationIdentifier == 'formatCheck') {
        if (!isInvalidCharactersExist(value, PageValidator.NUMERIC_CHARACTER_REGEXP)) {
          alert(errorMessage);
        } 
    } else {
      // Add msg parm (i.e. current participant highest loan balance in the last 12 months),
      // if one exists, to the message string
      errorMessage = errorMessage.replace(/\{0\}/, 
          numberUtils.formatAmount(numberUtils.deformatNumber(msgParm1), false, true));
      alert(errorMessage);
    }
  }
  isMaxBalanceLast12MonthsEditSuccess = false;
}

this.isInvalidCharactersExist = function (value, validCharacterRegEx) {
  var rc = false;
  var strippedValue = value.replace(validCharacterRegEx, '');
  if (strippedValue.length > 0) {
    rc = true;
  } else {
    rc = false;
  }
  return rc;
}

var isOutstandingLoansCountEditSuccess = true;

function onOutstandingLoansCountPositiveWholeNumberSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.OUTSTANDING_LOANS_COUNT.fieldName}');
  isOutstandingLoansCountEditSuccess = true;
}

function onOutstandingLoansCountPositiveWholeNumberFailure(e, callbackParams) {
  showIconForFailure('${loanFields.OUTSTANDING_LOANS_COUNT.fieldName}');
  var alertType = callbackParams[0];
  var errorMessage = callbackParams[1];
  var msgParm1 = callbackParams[2];
  if (alertType == 'alert') {
    // Add current outstanding loans count to message string
    errorMessage = errorMessage.replace(/\{0\}/, msgParm1);
    alert(errorMessage);
  }
  isOutstandingLoansCountEditSuccess = false;
}

function onOutstandingLoansCountMinimumSuccess(e, callbackParams) {
  // Only if the previous OutstandingLoanCount edit was successful do we
  // want to hide the error icon.  If it failed, we don't want to overwrite
  // the error icon already displayed. 
  if (isOutstandingLoansCountEditSuccess) {  
    showIconForSuccess('${loanFields.OUTSTANDING_LOANS_COUNT.fieldName}');
  }
}

function onOutstandingLoansCountMinimumFailure(e, callbackParams) {
  showIconForFailure('${loanFields.OUTSTANDING_LOANS_COUNT.fieldName}');
  var alertType = callbackParams[0];
  var errorMessage = callbackParams[1];
  var msgParm1 = callbackParams[2];
  if (alertType == 'alert') {
    // Add current outstanding loans count to message string
    errorMessage = errorMessage.replace(/\{0\}/, msgParm1);
    alert(errorMessage);
  }
  isOutstandingLoansCountEditSuccess = false;
}

function onOutstandingLoansCountMaximumSuccess(e, callbackParams) {
  // Only if the previous OutstandingLoanCount edits were successful do we
  // want to hide the error icon.  If it failed, we don't want to overwrite
  // the error icon already displayed. 
  if (isOutstandingLoansCountEditSuccess) {  
    showIconForSuccess('${loanFields.OUTSTANDING_LOANS_COUNT.fieldName}');
  }
}

function onOutstandingLoansCountMaximumFailure(e, callbackParams) {
  showIconForFailure('${loanFields.OUTSTANDING_LOANS_COUNT.fieldName}');
  var alertType = callbackParams[0];
  var errorMessage = callbackParams[1];
  var msgParm1 = callbackParams[2];
  if (alertType == 'alert') {
    // Add current outstanding loans count to message string
    errorMessage = errorMessage.replace(/\{0\}/, msgParm1);
    alert(errorMessage);
  }
  isOutstandingLoansCountEditSuccess = false;
}

var isCurrentOutstandingLoanBalanceEditSuccess = true;

function onCurrentOutstandingLoanBalanceChangeSuccess(e, callbackParams) {
  var field = e.target;
  var value = field.value;
  var validationIdentifier = callbackParams[3];

  // invalidCharCheck is the first edit, so we always show the success icon.
  // For the other edits, if a previous edit was successful, only then do we 
  // want to hide the error icon.  If a previous edit failed, we don't want 
  // to overwrite the error icon already displayed. 
  if (validationIdentifier == 'invalidCharCheck') {
    showIconForSuccess('${loanFields.CURRENT_OUTSTANDING_LOAN_BALANCE.fieldName}');
    isCurrentOutstandingLoanBalanceEditSuccess = true;
  } else if (isCurrentOutstandingLoanBalanceEditSuccess) {
    showIconForSuccess('${loanFields.CURRENT_OUTSTANDING_LOAN_BALANCE.fieldName}');
  }

  loan.currentOutstandingLoanBalance = numberUtils.parseNumber(value);
  recalculateBalances(false);
<c:if test="${displayRules.loanAmountDisplayOnlyRecalculated}">
  resetLoanAmount();
</c:if>
  if (e.type == 'blur') {
    if (! isNaN(value) && value.length > 0) {
      field.value = numberUtils.formatAmount(numberUtils.deformatNumber(value), false, true);
    }
  }
}

function onCurrentOutstandingLoanBalanceChangeFailure(e, callbackParams) {
  showIconForFailure('${loanFields.CURRENT_OUTSTANDING_LOAN_BALANCE.fieldName}');
  var alertType = callbackParams[0];
  var errorMessage = callbackParams[1];
  var msgParm1 = callbackParams[2];
  var validationIdentifier = callbackParams[3];
  if (alertType == 'alert') {
    var value = e.target.value;
    if (validationIdentifier == 'formatCheck') {
        if (!isInvalidCharactersExist(value, PageValidator.NUMERIC_CHARACTER_REGEXP)) {
          alert(errorMessage);
        } 
    } else {
      // Add msg parm (i.e. current outstanding loan balance amount),
      // if one exists, to the message string
      errorMessage = errorMessage.replace(/\{0\}/, 
          numberUtils.formatAmount(numberUtils.deformatNumber(msgParm1), false, true));
      alert(errorMessage);
    }
  }
  isCurrentOutstandingLoanBalanceEditSuccess = false;
}

function setMoneyTypeExcludedIndicator(event, field, moneyTypeIndex) {
  var loanMoneyType = loan.moneyTypes[moneyTypeIndex];
  loanMoneyType.excludeIndicator = field.checked;
  recalculateBalances(false);
<c:if test="${displayRules.loanAmountDisplayOnlyRecalculated}">
  resetLoanAmount();
</c:if>
}

function setApplyIrs10KDollarRuleInd(event, field) {
  loan.applyIrs10KDollarRuleInd = field.checked;
  recalculateBalances(false);
}

var isVestingPercentageEditSuccess = true;

function onVestingPercentageChangeSuccess(e, callbackParams) {
  var moneyTypeIndex = callbackParams[0];
  var field = e.target;
  var value = field.value;
  var validationIdentifier = callbackParams[3];

  // invalidCharCheck is the first edit, so we always show the success icon.
  // For the other edits, if a previous edit was successful, only then do we 
  // want to hide the error icon.  If a previous edit failed, we don't want 
  // to overwrite the error icon already displayed. 
  if (validationIdentifier == 'invalidCharCheck') {
    showIconForSuccess(moneyTypeIndex);
    isVestingPercentageEditSuccess = true;
  } else if (isVestingPercentageEditSuccess) {
    showIconForSuccess(moneyTypeIndex);
  }

  if (! isNaN(value) && value.length > 0) {
    if (e.type == 'blur') {
      field.value = numberUtils.formatPercentage(numberUtils.deformatNumber(value), 3);
    }
    if (value <= 100.000) {
      setMoneyTypeVestingPercentage(field, moneyTypeIndex, false);
    <c:if test="${displayRules.loanAmountDisplayOnlyRecalculated}">
      resetLoanAmount();
    </c:if>
    }
  }
}

function onVestingPercentageChangeFailure(e, callbackParams) {
  var moneyTypeIndex = callbackParams[0];
  showIconForFailure(moneyTypeIndex);
  var loanMoneyType = loan.moneyTypes[moneyTypeIndex];
  $("#vestedBalance_" + loanMoneyType.moneyTypeId).text("");
  var alertType = callbackParams[1];
  var errorMessage = callbackParams[2];
  var validationIdentifier = callbackParams[3];
  if (alertType == 'alert') {
    var value = e.target.value;
    if (validationIdentifier == 'formatCheck') {
        if (!isInvalidCharactersExist(value, PageValidator.NUMERIC_CHARACTER_NO_COMMA_REGEXP)) {
          alert(errorMessage);
        } 
    } else {
      alert(errorMessage);
    }
  }
  isVestingPercentageEditSuccess = false;
}

$(document).ready(function(){
  recalculateBalances(false);
<c:if test="${displayRules.loanAmountDisplayOnlyRecalculated}">
  resetLoanAmount();
</c:if>

  // Vesting percentages for each money type
  <c:forEach items="${loan.moneyTypesWithAccountBalance}" var="moneyType" varStatus="moneyTypeStatus">
    <c:if test="${moneyType.vestingPercentageUpdateable}">
    pageValidator.registerAllowedCharacters('moneyTypeVestingPercentage_${moneyType.moneyTypeId}',
      'keyup', PageValidator.NUMERIC_CHARACTER_NO_COMMA_REGEXP, onVestingPercentageChangeSuccess, onVestingPercentageChangeFailure,
      [${moneyTypeStatus.index}, 'noalert',
      '<content:getAttribute attribute="text" beanName="vestingPercentageNonNumericText" 
          escapeJavaScript="true"></content:getAttribute>',
      'invalidCharCheck']);
    pageValidator.registerAllowedCharacters('moneyTypeVestingPercentage_${moneyType.moneyTypeId}',
      'blur', PageValidator.NUMERIC_CHARACTER_NO_COMMA_REGEXP, onVestingPercentageChangeSuccess, onVestingPercentageChangeFailure,
      [${moneyTypeStatus.index}, 'alert',
      '<content:getAttribute attribute="text" beanName="vestingPercentageNonNumericText" 
          escapeJavaScript="true"></content:getAttribute>',
      'invalidCharCheck']);
  // Checks that the entry is a valid numeric format and positive.
    pageValidator.registerPositiveNumber('moneyTypeVestingPercentage_${moneyType.moneyTypeId}',
      'keyup', onVestingPercentageChangeSuccess, onVestingPercentageChangeFailure,
      [${moneyTypeStatus.index}, 'noalert', 
      '<content:getAttribute attribute="text" beanName="vestingPercentageInvalidFormatText" 
          escapeJavaScript="true"></content:getAttribute>', 'formatCheck']);
    pageValidator.registerPositiveNumber('moneyTypeVestingPercentage_${moneyType.moneyTypeId}',
      'blur', onVestingPercentageChangeSuccess, onVestingPercentageChangeFailure,
      [${moneyTypeStatus.index}, 'alert', 
      '<content:getAttribute attribute="text" beanName="vestingPercentageInvalidFormatText" 
          escapeJavaScript="true"></content:getAttribute>', 'formatCheck']);
    pageValidator.registerMaximum('moneyTypeVestingPercentage_${moneyType.moneyTypeId}',
      'keyup', 100, onVestingPercentageChangeSuccess, onVestingPercentageChangeFailure,
      [${moneyTypeStatus.index}, 'noalert',
      '<content:getAttribute attribute="text" beanName="vestingPercentageTooHighText" 
          escapeJavaScript="true"></content:getAttribute>'], 'maximumCheck');
    pageValidator.registerMaximum('moneyTypeVestingPercentage_${moneyType.moneyTypeId}',
      'blur', 100, onVestingPercentageChangeSuccess, onVestingPercentageChangeFailure,
      [${moneyTypeStatus.index}, 'alert',
      '<content:getAttribute attribute="text" beanName="vestingPercentageTooHighText" 
          escapeJavaScript="true"></content:getAttribute>', 'maximumCheck']);
    </c:if>
  </c:forEach>
  
  // Maximum loan balance in the last 12 months
  pageValidator.registerAllowedCharacters('maxBalanceLast12Months',
    'keyup', PageValidator.NUMERIC_CHARACTER_REGEXP, onMaxBalanceLast12MonthsChangeSuccess, onMaxBalanceLast12MonthsChangeFailure,
    ['noalert',
    '<content:getAttribute attribute="text" beanName="maximumLoanBalanceInLast12MonthsNonNumericText" 
        escapeJavaScript="true"></content:getAttribute>',
    '', 'invalidCharCheck']);
  pageValidator.registerAllowedCharacters('maxBalanceLast12Months',
    'blur', PageValidator.NUMERIC_CHARACTER_REGEXP, onMaxBalanceLast12MonthsChangeSuccess, onMaxBalanceLast12MonthsChangeFailure,
    ['alert',
    '<content:getAttribute attribute="text" beanName="maximumLoanBalanceInLast12MonthsNonNumericText" 
        escapeJavaScript="true"></content:getAttribute>',
    '', 'invalidCharCheck']);
  // Checks that the entry is a valid numeric format and positive.
  pageValidator.registerPositiveNumber('maxBalanceLast12Months',
    'keyup', onMaxBalanceLast12MonthsChangeSuccess, onMaxBalanceLast12MonthsChangeFailure,
    ['noalert',
    '<content:getAttribute attribute="text" beanName="maximumLoanBalanceInLast12MonthsInvalidFormatText" 
        escapeJavaScript="true"></content:getAttribute>',
    '', 'formatCheck']);
  pageValidator.registerPositiveNumber('maxBalanceLast12Months',
    'blur', onMaxBalanceLast12MonthsChangeSuccess, onMaxBalanceLast12MonthsChangeFailure,
    ['alert',
    '<content:getAttribute attribute="text" beanName="maximumLoanBalanceInLast12MonthsInvalidFormatText" 
        escapeJavaScript="true"></content:getAttribute>',
    '', 'formatCheck']);

  <c:if test="${not empty loanParticipantData.maxBalanceLast12Months}">
  pageValidator.registerMinimum('maxBalanceLast12Months',
    'keyup', ${loanParticipantData.maxBalanceLast12Months}, onMaxBalanceLast12MonthsChangeSuccess, onMaxBalanceLast12MonthsChangeFailure,
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="maximumLoanBalanceInLast12MonthsTooLowText" 
        escapeJavaScript="true"></content:getAttribute>',
    ${loanParticipantData.maxBalanceLast12Months}, 'minimumCheck']);
  pageValidator.registerMinimum('maxBalanceLast12Months',
    'blur', ${loanParticipantData.maxBalanceLast12Months}, onMaxBalanceLast12MonthsChangeSuccess, onMaxBalanceLast12MonthsChangeFailure,
    ['alert', 
    '<content:getAttribute attribute="text" beanName="maximumLoanBalanceInLast12MonthsTooLowText" 
        escapeJavaScript="true"></content:getAttribute>',
    ${loanParticipantData.maxBalanceLast12Months}, 'minimumCheck']);
  </c:if>
  
  // Outstanding loans
  pageValidator.registerPositiveWholeNumber('outstandingLoansCount',
    'keyup', onOutstandingLoansCountPositiveWholeNumberSuccess, onOutstandingLoansCountPositiveWholeNumberFailure,
    ['noalert',
    '<content:getAttribute attribute="text" beanName="outstandingLoansCountNonNumericText" 
        escapeJavaScript="true"></content:getAttribute>',
    '']);
  pageValidator.registerPositiveWholeNumber('outstandingLoansCount',
    'blur', onOutstandingLoansCountPositiveWholeNumberSuccess, onOutstandingLoansCountPositiveWholeNumberFailure,
    ['alert',
    '<content:getAttribute attribute="text" beanName="outstandingLoansCountNonNumericText" 
        escapeJavaScript="true"></content:getAttribute>',
    '']);

  <c:if test="${not empty loanParticipantData.outstandingLoansCount}">
  pageValidator.registerMinimum('outstandingLoansCount',
    'keyup', ${loanParticipantData.outstandingLoansCount}, onOutstandingLoansCountMinimumSuccess, onOutstandingLoansCountMinimumFailure,
    ['noalert',
    '<content:getAttribute attribute="text" beanName="outstandingLoansCountTooLowText" 
        escapeJavaScript="true"></content:getAttribute>', 
    ${loanParticipantData.outstandingLoansCount}]);
  pageValidator.registerMinimum('outstandingLoansCount',
    'blur', ${loanParticipantData.outstandingLoansCount}, onOutstandingLoansCountMinimumSuccess, onOutstandingLoansCountMinimumFailure,
    ['alert',
    '<content:getAttribute attribute="text" beanName="outstandingLoansCountTooLowText" 
        escapeJavaScript="true"></content:getAttribute>', 
    ${loanParticipantData.outstandingLoansCount}]);
  </c:if>

  <c:if test="${not empty loanPlanData.maxNumberOfOutstandingLoans}">
    pageValidator.registerMaximum('outstandingLoansCount',
      'keyup', ${loanPlanData.maxNumberOfOutstandingLoans}, onOutstandingLoansCountMaximumSuccess, onOutstandingLoansCountMaximumFailure,
      ['noalert', 
      '<content:getAttribute attribute="text" beanName="outstandingLoansCountTooHighText" 
          escapeJavaScript="true"></content:getAttribute>', 
      ${loanPlanData.maxNumberOfOutstandingLoans}]);
    pageValidator.registerMaximum('outstandingLoansCount',
      'blur', ${loanPlanData.maxNumberOfOutstandingLoans}, onOutstandingLoansCountMaximumSuccess, onOutstandingLoansCountMaximumFailure,
      ['alert', 
      '<content:getAttribute attribute="text" beanName="outstandingLoansCountTooHighText" 
          escapeJavaScript="true"></content:getAttribute>', 
      ${loanPlanData.maxNumberOfOutstandingLoans}]);
  </c:if>
  
  // Current outstanding balance
  pageValidator.registerAllowedCharacters('currentOutstandingBalance',
    'keyup', PageValidator.NUMERIC_CHARACTER_REGEXP, onCurrentOutstandingLoanBalanceChangeSuccess, onCurrentOutstandingLoanBalanceChangeFailure,
    ['noalert',
    '<content:getAttribute attribute="text" beanName="currentOutstandingLoanBalanceNonNumericText" 
        escapeJavaScript="true"></content:getAttribute>',
    '', 'invalidCharCheck']);
  pageValidator.registerAllowedCharacters('currentOutstandingBalance',
    'blur', PageValidator.NUMERIC_CHARACTER_REGEXP, onCurrentOutstandingLoanBalanceChangeSuccess, onCurrentOutstandingLoanBalanceChangeFailure,
    ['alert',
    '<content:getAttribute attribute="text" beanName="currentOutstandingLoanBalanceNonNumericText" 
        escapeJavaScript="true"></content:getAttribute>',
    '', 'invalidCharCheck']);
  // Checks that the entry is a valid numeric format and positive.
  pageValidator.registerPositiveNumber('currentOutstandingBalance',
    'keyup', onCurrentOutstandingLoanBalanceChangeSuccess, onCurrentOutstandingLoanBalanceChangeFailure,
    ['noalert',
    '<content:getAttribute attribute="text" beanName="currentOutstandingLoanBalanceNonInvalidFormatText" 
        escapeJavaScript="true"></content:getAttribute>',
    '', 'formatCheck']);
  pageValidator.registerPositiveNumber('currentOutstandingBalance',
    'blur', onCurrentOutstandingLoanBalanceChangeSuccess, onCurrentOutstandingLoanBalanceChangeFailure,
    ['alert',
    '<content:getAttribute attribute="text" beanName="currentOutstandingLoanBalanceNonInvalidFormatText" 
        escapeJavaScript="true"></content:getAttribute>',
    '', 'formatCheck']);

  <c:if test="${not empty loanParticipantData.currentOutstandingBalance}">
    pageValidator.registerMinimum('currentOutstandingBalance',
      'blur', ${loanParticipantData.currentOutstandingBalance}, onCurrentOutstandingLoanBalanceChangeSuccess, onCurrentOutstandingLoanBalanceChangeFailure,
      ['alert',
      '<content:getAttribute attribute="text" beanName="currentOutstandingLoanBalanceTooLowText" 
          escapeJavaScript="true"></content:getAttribute>',
      ${loanParticipantData.currentOutstandingBalance}, 'minimumCheck']);
  </c:if>
  
});

</script>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="734">
  <tr>
    <td class="tableheadTD1">
    <c:if test="${displayRules.displayExpandCollapseButton}">
      <img id="calculateMaximumLoanAmountSectionExpandCollapseIcon" src="/assets/unmanaged/images/minus_icon.gif" width="13" height="13"  style="cursor:hand; cursor:pointer">&nbsp;
    </c:if>
    <b>
      <content:getAttribute attribute="text" beanName="calcualteMaxAvailableForLoanSectionTitleText">
      </content:getAttribute>
    </td>
    <td width="254" align="right" class="tablehead">
      <c:if test="${displayRules.displayVestingInformationLink}">
        <a href="#" onclick="doPrintFiendlyVestingExplanation()">
          <content:getAttribute attribute="text" beanName="vestingExplanationLinkText">
          </content:getAttribute>
        </a>
      </c:if>
      <c:if test="${not displayRules.displayVestingInformationLink}">
        &nbsp;
      </c:if>
    </td>
  </tr>
</table>

<div id="calculateMaximumLoanAmountSection">
  <table class="box" border="0" cellpadding="0" cellspacing="0" width="734">
    <tr>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td width="732"><table border="0" cellpadding="0" cellspacing="0" width="733">
          <tr valign="top">
          <td>
          <table border="0" width="100%" cellpadding="0" cellspacing="0"><tr><td valign="top">
          <table border="0" width="100%" cellpadding="0" cellspacing="0">
            <tr><td colspan="12" class="tablesubhead"><b>Vested account balance</b> </td></tr>
            <tr>
            <td width="72" valign="bottom" class="datacell1"><strong>Money type </strong></td>
            <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td width="111" align="right"  valign="bottom" class="datacell1"><strong>${displayRules.accountBalanceLabel} </strong></td>

            <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td width="15" class="datacell1"></td>
            <td width="86" align="right" valign="bottom" class="datacell1"><strong>Vesting (%)</strong></td>
            <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td width="101" align="right" valign="bottom" class="datacell1"><strong>Vested<br>
              balance ($) </strong></td>
            <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td width="52"  align="right" valign="bottom" class="datacell1"><strong>Exclude</strong></td>

            <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td width="93" align="right" class="datacell1"><strong>Available<br>
              amount ($)</strong></td>
            <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </tr>
<c:forEach items="${loan.moneyTypesWithAccountBalance}" var="moneyType" varStatus="moneyTypeStatus">
  <tr valign="top">
    <td class="${((moneyTypeStatus.count % 2) != 0 ) ? 'datacell2' : 'datacell1'}">
    ${moneyType.contractMoneyTypeShortName}
    </td>
    <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td align="right" class="${((moneyTypeStatus.count % 2) != 0 ) ? 'datacell2' : 'datacell1'}">
	    <fmt:formatNumber type="currency"
	                       currencySymbol=""
	                       minFractionDigits="2"
	                       value="${moneyType.accountBalance}"/>
    </td>
    <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td width="15" class="${((moneyTypeStatus.count % 2) != 0 ) ? 'datacell2' : 'datacell1'}">
      <img id="dynamicErrorIcon_${moneyTypeStatus.index}"
           src="/assets/unmanaged/images/error.gif"
           style="display:none"/>
      <ps:fieldHilight styleIdSuffix="${moneyTypeStatus.index}"
                       name="${loanFields.MONEY_TYPE_VESTING_PERCENTAGE_PREFIX.fieldName}_${moneyType.moneyTypeId}"
                       displayActivityHistory="${displayRules.vestingPercentageActivityHistoryDisplayMap[moneyType.moneyTypeId]}"
                       singleDisplay="true"/>
    </td>
    <td nowrap="true" align="right" class="${((moneyTypeStatus.count % 2) != 0 ) ? 'datacell2' : 'datacell1'}">
      <c:choose>
        <c:when test="${displayRules.showMoneyTypeVestingPercentageAsEditable && moneyType.vestingPercentageUpdateable}">
          <ps:trackChanges escape="true" property="moneyTypeVestingPercentage(${moneyType.moneyTypeId})" name="loanForm"/>
	
	<form:input  id="moneyTypeVestingPercentage_${moneyType.moneyTypeId}"
                     path="moneyTypeVestingPercentages[${moneyType.moneyTypeId}]"
                     size="7"
                     maxlength="7"
                     cssStyle="{text-align: right}"/>
        </c:when>
        <c:otherwise>
          <fmt:formatNumber minFractionDigits="${loanConstants.VESTING_PERCENTAGE_SCALE}"
                     maxFractionDigits="${loanConstants.VESTING_PERCENTAGE_SCALE}"
                     value="${moneyType.vestingPercentage}"/>
        </c:otherwise>
      </c:choose>
    </td>

    <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td align="right" class="${((moneyTypeStatus.count % 2) != 0 ) ? 'datacell2' : 'datacell1'}">
      <span id="vestedBalance_${moneyType.moneyTypeId}">&nbsp;</span>
    </td>
    <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td width="52" align="center" class="${((moneyTypeStatus.count % 2) != 0 ) ? 'datacell2' : 'datacell1'}">
      <c:if test="${displayRules.moneyTypeExcludeIndicatorEditable}">
        <ps:trackChanges escape="true" property="moneyTypeExcludedInd(${moneyType.moneyTypeId})" name="loanForm"/>
        <input type="checkbox" name="moneyTypeExcludedInd(${moneyType.moneyTypeId})" onclick="setMoneyTypeExcludedIndicator(event, this, ${moneyTypeStatus.index})"/>

      </c:if>
      <c:if test="${not displayRules.moneyTypeExcludeIndicatorEditable}">
<input type="checkbox" name="excludeIndicator" disabled="disabled"/>
 <input type="hidden" name="moneyTypeExcludedInd(${moneyType.moneyTypeId})"/> 
      </c:if>
    </td>
    <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td align="right" class="${((moneyTypeStatus.count % 2) != 0 ) ? 'datacell2' : 'datacell1'}">
      <span id="availableBalance_${moneyType.moneyTypeId}">&nbsp;</span>
    </td>
    <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  </tr>
  <c:if test="${moneyTypeStatus.count == fn:length(loan.moneyTypesWithAccountBalance)}">
	  <tr valign="top">
	    <td class="${((moneyTypeStatus.count % 2) == 0 ) ? 'datacell2' : 'datacell1'}">
	    <strong>Total</strong>
	    </td>
	    <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	    <td align="right" class="${((moneyTypeStatus.count % 2) == 0 ) ? 'datacell2' : 'datacell1'}">
	      <span id="totalAccountBalance">&nbsp;</span>
	    </td>
	    <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	    <td colspan="2" align="right" class="${((moneyTypeStatus.count % 2) == 0 ) ? 'datacell2' : 'datacell1'}">
	    &nbsp;
	    </td>
	    <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	    <td align="right" class="${((moneyTypeStatus.count % 2) == 0 ) ? 'datacell2' : 'datacell1'}">
	      <span id="totalVestedBalance">&nbsp;</span>
	    </td>
	    <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	    <td width="52" align="center" class="${((moneyTypeStatus.count % 2) == 0 ) ? 'datacell2' : 'datacell1'}">
	    &nbsp;
	    </td>
	    <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	    <td align="right" class="${((moneyTypeStatus.count % 2) == 0 ) ? 'datacell2' : 'datacell1'}">
	      <span id="totalAvailableBalance">&nbsp;</span>
	    </td>
	    <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	  </tr> 
  </c:if>  
</c:forEach>
    <tr>
      <td colspan="13" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
          </table>
          </td>
            <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td valign="top">
          <table width="153" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td colspan="3" class="tablesubhead"><b>Maximum loan permitted </b></td>
            </tr>
            <tr>
              <td valign="top" width="15">
			    <img id="dynamicErrorIcon_${loanFields.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS.fieldName}"
			         src="/assets/unmanaged/images/error.gif"
			         style="display:none"/>
			    <ps:fieldHilight styleIdSuffix="${loanFields.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS.fieldName}"
			                     name="${loanFields.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS.fieldName}"
			                     singleDisplay="true"/>
              </td>
              <td>            
                <strong>Highest loan balance in the last 12 months </strong>
              </td>
              <td align="right">
                <c:choose>
                  <c:when test="${displayRules.showMaxBalanceLast12MonthsAsEditable}">
                    <ps:trackChanges escape="true" property="maxBalanceLast12Months" name="loanForm"/>
<form:input path="maxBalanceLast12Months" maxlength="12" size="7" cssStyle="{text-align: right}" cssClass="mandatory" id="maxBalanceLast12Months"/>




                  </c:when>
                  <c:otherwise>
                    <fmt:formatNumber type="currency"
                         currencySymbol="$"
                         minFractionDigits="2"
                         value="${loan.maxBalanceLast12Months}"/>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
            <tr style="{line-height: 25%}">
              <td>&nbsp</td>
            </tr>
            <tr>
              <td valign="top" width="15">
			    <img id="dynamicErrorIcon_${loanFields.OUTSTANDING_LOANS_COUNT.fieldName}"
			         src="/assets/unmanaged/images/error.gif"
			         style="display:none"/>
			    <ps:fieldHilight styleIdSuffix="${loanFields.OUTSTANDING_LOANS_COUNT.fieldName}"
			                     name="${loanFields.OUTSTANDING_LOANS_COUNT.fieldName}"
			                     singleDisplay="true"/>
              </td>
              <td class="datacell1">
                <strong>Outstanding loans </strong>
              </td>
              <td class="datacell1" align="right">
                <c:choose>
                  <c:when test="${displayRules.showOutstandingLoansCountAsEditable}">
                    <ps:trackChanges escape="true" property="outstandingLoansCount" name="loanForm"/>
<form:input path="outstandingLoansCount" maxlength="2" size="7" cssStyle="{text-align: right}" cssClass="mandatory" id="outstandingLoansCount"/>




                  </c:when>
                  <c:otherwise>
                    <c:out value="${loan.outstandingLoansCount}" />
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
            <tr style="{line-height: 25%}">
              <td>&nbsp</td>
            </tr>
            <tr>
              <td valign="top" width="15">
			    <img id="dynamicErrorIcon_${loanFields.CURRENT_OUTSTANDING_LOAN_BALANCE.fieldName}"
			         src="/assets/unmanaged/images/error.gif"
			         style="display:none"/>
			    <ps:fieldHilight styleIdSuffix="${loanFields.CURRENT_OUTSTANDING_LOAN_BALANCE.fieldName}"
			                     name="${loanFields.CURRENT_OUTSTANDING_LOAN_BALANCE.fieldName}"
			                     singleDisplay="true"/>
              </td>
              <td class="datacell1"><strong>Outstanding balance </strong></td>
              <td width="54" class="datacell1" align="right">
                <c:choose>
                  <c:when test="${displayRules.showCurrentOutstandingBalanceAsEditable}">
                    <ps:trackChanges escape="true" property="currentOutstandingBalance" name="loanForm" />
<form:input path="currentOutstandingBalance" maxlength="12" size="7" cssStyle="{text-align: right}" cssClass="mandatory" id="currentOutstandingBalance"/>




                  </c:when>
                  <c:otherwise>
                    <fmt:formatNumber type="currency"
                         currencySymbol="$"
                         minFractionDigits="2"
                         value="${loan.currentOutstandingBalance}"/>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
            <tr style="{line-height: 25%}">
              <td>&nbsp</td>
            </tr>
            <tr>
              <td colspan="3" align="center" class="datacell1"><strong>Maximum loan permitted </strong></td>
            </tr>
            <tr>
              <td colspan="3" align="center" class="datacell1">
                <span id="calculatorMaxLoanAvailableSpan"></span>
<input type="hidden" name="maximumLoanAvailable"/>
              </td>
            </tr>
            <c:if test="${not loanForm.displayIRSlabel}">
            <tr>
             <td colspan="3" class="datacell1">
              <table width="150" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="datacell1">
                    <c:if test="${not displayRules.showApplyIrs10KDollarRuleAsDisabled}">
                      <ps:trackChanges escape="true" property="applyIrs10KDollarRule" name="loanForm"/>
<form:checkbox path="applyIrs10KDollarRule" onclick="setApplyIrs10KDollarRuleInd(event, this)" disabled="false"/>


                    </c:if>
                    <c:if test="${displayRules.showApplyIrs10KDollarRuleAsDisabled}">
<form:checkbox path="applyIrs10KDollarRule" disabled="true"/>

<input type="hidden" name="applyIrs10KDollarRule"/>
                    </c:if>
                  </td>
                  <td class="datacell1">
                    <strong>Apply $10,000 IRS rule</strong> 
                  </td>
                </tr>
              </table>
             </td> 
            </tr>
            </c:if>
          </table>
          </td>
          </tr>

          <tr>
            <td colspan="7" class="datacell1">
              <br>
              <content:getAttribute attribute="text" beanName="calculateMaxLoanAvailableFooterText">
              </content:getAttribute>
            </td>
          </tr>

          </table>
</td></tr>
      </table></td>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
  </table>
</div>
        
