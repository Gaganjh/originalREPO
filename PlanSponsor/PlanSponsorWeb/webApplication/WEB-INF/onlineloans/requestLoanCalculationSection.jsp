<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<un:useConstants var="loanConstants" className="com.manulife.pension.service.loan.domain.LoanConstants" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="globalConstants" className="com.manulife.pension.common.GlobalConstants" />
<un:useConstants var="loanFields" className="com.manulife.pension.service.loan.LoanField" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />

<content:contentBean
  contentId="${loanContentConstants.LOAN_CACULATIONS_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="loanCalcualtionsTitleText"/>
<content:contentBean
  contentName="${loanContentConstants.AMORTIZATION_PERIOD_TOO_HIGH}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="amortizationPeriodTooHigh"/>
<content:contentBean
  contentName="${loanContentConstants.LOAN_AMOUNT_BLANK_OR_NON_NUMERIC}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="loanAmountNonNumericText"/>
<content:contentBean
  contentName="${loanContentConstants.LOAN_AMOUNT_LESS_THAN_MINIMUM}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="loanAmountTooLowText"/>
<content:contentBean
  contentName="${loanContentConstants.LOAN_AMOUNT_GREATER_THAN_MAXIMUM}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="loanAmountGreaterThanMaximumText"/>
<content:contentBean
  contentName="${loanContentConstants.LOAN_AMOUNT_INVALID_FORMAT}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="loanAmountInvalidFormatText"/>
<content:contentBean
  contentName="${loanContentConstants.PAYMENT_FREQUENCY_IS_EMPTY}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="paymentFrequencyIsEmptyText"/>
<content:contentBean
  contentName="${loanContentConstants.INTEREST_RATE_BLANK_OR_NON_NUMERIC}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="interestRateBlankOrNonNumericText"/>
<content:contentBean
  contentName="${loanContentConstants.INTEREST_RATE_INVALID_FORMAT}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="interestRateInvalidFormatText"/>
<content:contentBean
  contentName="${loanContentConstants.INTEREST_RATE_OUT_OF_RANGE}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="interestRateOutOfRangeText"/>


<script language="JavaScript1.2" type="text/javascript">

  var isFirstDisplay = true;

function onAmortizationYearsChange(alertType) {
  var amortizationPeriodYearsSelect = $("#amortizationPeriodYears")[0];
  if (amortizationPeriodYearsSelect.selectedIndex == amortizationPeriodYearsSelect.length - 1) {
    $("#amortizationPeriodMonths")[0].selectedIndex = 0;
    $("#amortizationPeriodMonths").prop("disabled", true);
  } else {
    $("#amortizationPeriodMonths").prop( "disabled", false );
  }
  updateLoanMaturityDate();
  onLoanAmountChangeSuccess(undefined, undefined);

  var errorIcon = $('#errorIcon_amortizationPeriod');
  if (errorIcon.length == 0) {
    errorIcon = $('#dynamicErrorIcon_amortizationPeriod');
  }
  var maxAmortizationPeriodMonths = parseFloat($("#maximumAmortizationPeriodYears").val()) * 12.0;
  var amortizationPeriodMonthsTotal = parseFloat($("#amortizationPeriodYears").val()) * 12.0 + parseFloat($("#amortizationPeriodMonths").val());
  if (amortizationPeriodMonthsTotal > maxAmortizationPeriodMonths) {
    errorIcon.show();
    if (alertType == 'alert') {
      alert('<content:getAttribute attribute="text" beanName="amortizationPeriodTooHigh" escapeJavaScript="true"></content:getAttribute>');
    }
  } else if (isFirstDisplay) {
    isFirstDisplay = false;
  } else {
    errorIcon.hide();
  }
}

function onAmortizationMonthsChange() {
  updateLoanMaturityDate();
  onLoanAmountChangeSuccess(undefined, undefined);

  var errorIcon = $('#errorIcon_amortizationPeriod');
  if (errorIcon.length == 0) {
    errorIcon = $('#dynamicErrorIcon_amortizationPeriod');
  }
  var maxAmortizationPeriodMonths = parseFloat($("#maximumAmortizationPeriodYears").val()) * 12.0;
  var amortizationPeriodMonthsTotal = parseFloat($("#amortizationPeriodYears").val()) * 12.0 + parseFloat($("#amortizationPeriodMonths").val());
  if (amortizationPeriodMonthsTotal > maxAmortizationPeriodMonths) {
    errorIcon.show();
    alert('<content:getAttribute attribute="text" beanName="amortizationPeriodTooHigh" escapeJavaScript="true"></content:getAttribute>');
  } else {
    errorIcon.hide();
  }
}

function getNumberOfPeriodsPerYear() {
  var paymentFrequency = $("#paymentFrequency").val();
  if (paymentFrequency == "${globalConstants.FREQUENCY_TYPE_WEEKLY}") {
    return ${loanConstants.FREQUENCY_TYPE_WEEKLY_PERIODS_PER_YEAR};
  } else if (paymentFrequency == "${globalConstants.FREQUENCY_TYPE_BI_WEEKLY}") {
    return ${loanConstants.FREQUENCY_TYPE_BI_WEEKLY_PERIODS_PER_YEAR};
  } else if (paymentFrequency == "${globalConstants.FREQUENCY_TYPE_MONTHLY}") {
    return ${loanConstants.FREQUENCY_TYPE_MONTHLY_PERIODS_PER_YEAR};
  } else if (paymentFrequency == "${globalConstants.FREQUENCY_TYPE_SEMI_MONTHLY}") {
    return ${loanConstants.FREQUENCY_TYPE_SEMI_MONTHLY_PERIODS_PER_YEAR};
  }
  return NaN;
}

function getDaysPerPeriod() {
  var paymentFrequency = $("#paymentFrequency").val();
  if (paymentFrequency == "${globalConstants.FREQUENCY_TYPE_WEEKLY}") {
    return ${loanConstants.FREQUENCY_TYPE_WEEKLY_DAYS_PER_PERIOD};
  } else if (paymentFrequency == "${globalConstants.FREQUENCY_TYPE_BI_WEEKLY}") {
    return ${loanConstants.FREQUENCY_TYPE_BI_WEEKLY_DAYS_PER_PERIOD};
  } else if (paymentFrequency == "${globalConstants.FREQUENCY_TYPE_MONTHLY}") {
    return ${loanConstants.FREQUENCY_TYPE_MONTHLY_DAYS_PER_PERIOD};
  } else if (paymentFrequency == "${globalConstants.FREQUENCY_TYPE_SEMI_MONTHLY}") {
    return ${loanConstants.FREQUENCY_TYPE_SEMI_MONTHLY_DAYS_PER_PERIOD};
  }
  return NaN;
}

function getInterestRate() {
  var interestRate = numberUtils.parseNumber($("#interestRate").val());
  if (isNaN(interestRate)) {
    return NaN;
  }
  return interestRate;

}

 // Validation functions ---------------------------------------------------

var isLoanAmountInvalidCharCheckSuccess = true;
var isLoanAmountFormatCheckSuccess = true;

function onLoanAmountChangeSuccess(e, callbackParams) {
  var loanAmount = numberUtils.parseNumber($("#loanAmount").val());
  var numberOfYears = parseFloat($("#amortizationPeriodYears").val()) + parseFloat($("#amortizationPeriodMonths").val()) / 12.0;
  var nominalAnnualInterestRate = getInterestRate();
  var numberOfPeriodsPerYear = getNumberOfPeriodsPerYear();
  var daysPerPeriod = getDaysPerPeriod();
  
  if (! isNaN(loanAmount) && ! isNaN(numberOfYears) &&
      ! isNaN(nominalAnnualInterestRate) && ! isNaN(numberOfPeriodsPerYear) &&
      ! isNaN(daysPerPeriod) &&
      numberOfYears != 0) {
    repaymentAmount = calculateRepaymentAmount(loanAmount, numberOfYears, nominalAnnualInterestRate, 
                                               numberOfPeriodsPerYear, daysPerPeriod);
    if (!isNaN(repaymentAmount)) {
      $("#paymentAmount").val(repaymentAmount);
      $("#paymentAmountSpan").text(numberUtils.formatAmount(repaymentAmount, true, true));
    }
  }

  if (typeof(e) != 'undefined') {
    var validationIdentifier = callbackParams[4];
    // invalidCharCheck is the first edit, so we always show the success icon.
    // For the other edits, if a previous edit was successful, only then do we 
    // want to hide the error icon.  If a previous edit failed, we don't want 
    // to overwrite the error icon already displayed. 
    if (validationIdentifier == 'invalidCharCheck') {
      showIconForSuccess('${loanFields.LOAN_AMOUNT.fieldName}');
      isLoanAmountInvalidCharCheckSuccess = true;
    } else if (validationIdentifier == 'formatCheck') {
      isLoanAmountFormatCheckSuccess = true;
      if (isLoanAmountInvalidCharCheckSuccess) {
        showIconForSuccess('${loanFields.LOAN_AMOUNT.fieldName}');
      }
    } else if (isLoanAmountInvalidCharCheckSuccess 
                && isLoanAmountFormatCheckSuccess) {
      showIconForSuccess('${loanFields.LOAN_AMOUNT.fieldName}');
    }

    var field = e.target;
    var value = numberUtils.parseNumber(field.value);
    if (! isNaN(value)) {
      if (e.type == 'blur') {
        field.value = numberUtils.formatAmount(value, false, true);
      }
    }
  }
}

function onLoanAmountChangeFailure(e, callbackParams) {
  showIconForFailure('${loanFields.LOAN_AMOUNT.fieldName}');
  var alertType = callbackParams[0];
  var errorMessage1 = callbackParams[1];
  var errorMessage2 = callbackParams[2];
  var msgParm1 = callbackParams[3];
  var validationIdentifier = callbackParams[4];
  var rangeCheckFailureReason = callbackParams[5];

  if (validationIdentifier == 'invalidCharCheck') {
    isLoanAmountInvalidCharCheckSuccess = false;
  } else if (validationIdentifier == 'formatCheck') {
    isLoanAmountFormatCheckSuccess = false;
  }

  if (alertType == 'alert') {
    var value = e.target.value;
    if (validationIdentifier == 'formatCheck') {
      if (!stringUtils.isInvalidCharactersExist(value, PageValidator.NUMERIC_CHARACTER_REGEXP)) {
        alert(errorMessage1);
      } 
    } else {
      var errorMessage;
      if (validationIdentifier == 'rangeCheck') { 
        if (isLoanAmountInvalidCharCheckSuccess
            && isLoanAmountFormatCheckSuccess) {
          if (rangeCheckFailureReason == "1") {
            errorMessage = errorMessage2; 
          } else {
            errorMessage = errorMessage1; 
          }
          // Add msg parm (if one exists) to the message string. 
          errorMessage = errorMessage.replace(/\{0\}/, 
              numberUtils.formatAmount(numberUtils.deformatNumber(msgParm1), false, true));
          alert(errorMessage);
        }
      } else {
          errorMessage = errorMessage1;
          // Add msg parm (if one exists) to the message string. 
          errorMessage = errorMessage.replace(/\{0\}/, 
              numberUtils.formatAmount(numberUtils.deformatNumber(msgParm1), false, true));
          alert(errorMessage);
      }
    }
  }
  // clear the last parm in the array, otherwise previous value sticks around.
  callbackParams.length = 5;
}

function onPaymentFrequencyChangeSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.PAYMENT_FREQUENCY.fieldName}');
  onLoanAmountChangeSuccess(undefined, undefined);
}

function onPaymentFrequencyChangeFailure(e, callbackParams) {
  showIconForFailure('${loanFields.PAYMENT_FREQUENCY.fieldName}');
  var errorMessage = callbackParams[0];
  alert(errorMessage);
}

var isInterestRateInvlaidCharCheckSuccess = true;
var isInterestRateFormatCheckSuccess = true;

function onInterestRateChangeSuccess(e, callbackParams) {
  var field = e.target;
  var value = numberUtils.parseNumber(field.value);
  var validationIdentifier = callbackParams[2];

  // invalidCharCheck is the first edit, so we always show the success icon.
  // For the other edits, if a previous edit was successful, only then do we 
  // want to hide the error icon.  If a previous edit failed, we don't want 
  // to overwrite the error icon already displayed. 
  if (validationIdentifier == 'invalidCharCheck') {
    showIconForSuccess('${loanFields.INTEREST_RATE.fieldName}');
    isInterestRateInvlaidCharCheckSuccess = true;
  } else if (validationIdentifier == 'formatCheck') {
    isInterestRateFormatCheckSuccess = true;
    if (isInterestRateInvlaidCharCheckSuccess) {
      showIconForSuccess('${loanFields.INTEREST_RATE.fieldName}');
    }
  } else if (validationIdentifier == 'rangeCheck' 
              && isInterestRateInvlaidCharCheckSuccess
              && isInterestRateFormatCheckSuccess) {
    showIconForSuccess('${loanFields.INTEREST_RATE.fieldName}');
  }

  if (! isNaN(value)) {
    if (e.type == 'keyup') {
      onLoanAmountChangeSuccess();
    }
    if (e.type == 'blur') {
      field.value = numberUtils.formatPercentage(value, 3);
    }
  }
}

function onInterestRateChangeFailure(e, callbackParams) {
  showIconForFailure('${loanFields.INTEREST_RATE.fieldName}');
  var alertType = callbackParams[0];
  var errorMessage = callbackParams[1];
  var validationIdentifier = callbackParams[2];

  if (validationIdentifier == 'invalidCharCheck') {
    isInterestRateInvlaidCharCheckSuccess = false;
  } else if (validationIdentifier == 'formatCheck') {
    isInterestRateFormatCheckSuccess = false;
  }
  if (alertType == 'alert') {
    var value = e.target.value;
    if (validationIdentifier == 'invalidCharCheck') {
      alert(errorMessage);
    } else if (validationIdentifier == 'formatCheck' 
                && isInterestRateInvlaidCharCheckSuccess) {
      alert(errorMessage);
    } else if (validationIdentifier == 'rangeCheck' 
                && isInterestRateInvlaidCharCheckSuccess
                && isInterestRateFormatCheckSuccess) {
      alert(errorMessage);
    }
  }
}

 // Support functions ---------------------------------------------------

function formatLoanAmount() {
  var loanAmount = numberUtils.parseNumber($("#loanAmount").val());
  if (!isNaN(loanAmount)) {
    /*
     * Only format the amount if it's a number, otherwise, the field
     * will be defaulted to zero.
     */
    $("#loanAmount").val(numberUtils.formatAmount(loanAmount, false, true));
  }
}

function formatPaymentAmount() {
  var paymentAmount = numberUtils.parseNumber($("#paymentAmount").val());
  if (!isNaN(paymentAmount)) {
    /*
     * Only format the amount if it's a number, otherwise, the field
     * will be defaulted to zero.
     */
    $("#paymentAmountSpan").text(numberUtils.formatAmount(paymentAmount, false, true));
  }
}

function getMaximumLoanPermittedOrAvailableAmount() {
  var maximumPermitted = numberUtils.parseNumber($("#maxLoanAvailableSpan").text());
  var availableAmount = numberUtils.parseNumber($("#totalAvailableBalance").text());
  if (isNaN(maximumPermitted)) {
    return availableAmount;
  }
  return Math.min(maximumPermitted, availableAmount);
}

$(document).ready(function(){
  <c:if test="${displayRules.loanCalculationEditable}">
    //Loan amount validations
    pageValidator.registerAllowedCharacters('loanAmount',
      'keyup', PageValidator.NUMERIC_CHARACTER_REGEXP, onLoanAmountChangeSuccess, onLoanAmountChangeFailure,
      ['noalert', 
      '<content:getAttribute attribute="text" beanName="loanAmountNonNumericText" escapeJavaScript="true"></content:getAttribute>',
      '', '', 'invalidCharCheck']);
    pageValidator.registerAllowedCharacters('loanAmount',
      'blur', PageValidator.NUMERIC_CHARACTER_REGEXP, onLoanAmountChangeSuccess, onLoanAmountChangeFailure,
      ['alert',
      '<content:getAttribute attribute="text" beanName="loanAmountNonNumericText" escapeJavaScript="true"></content:getAttribute>',
      '', '', 'invalidCharCheck']);
    // Checks that the entry is a valid numeric format and positive.
    pageValidator.registerPositiveNumber('loanAmount',
      'keyup', onLoanAmountChangeSuccess, onLoanAmountChangeFailure,
      ['noalert',
      '<content:getAttribute attribute="text" beanName="loanAmountInvalidFormatText" escapeJavaScript="true"></content:getAttribute>',
      '', '', 'formatCheck']);
    pageValidator.registerPositiveNumber('loanAmount',
      'blur', onLoanAmountChangeSuccess, onLoanAmountChangeFailure,
      ['alert',
      '<content:getAttribute attribute="text" beanName="loanAmountInvalidFormatText" escapeJavaScript="true"></content:getAttribute>',
      '', '', 'formatCheck']);
    pageValidator.registerRangeCheck('loanAmount',
      'blur', ${loanPlanData.minimumLoanAmount}, getMaximumLoanPermittedOrAvailableAmount,
      onLoanAmountChangeSuccess, onLoanAmountChangeFailure,
      ['alert', 
      '<content:getAttribute attribute="text" beanName="loanAmountTooLowText" escapeJavaScript="true"></content:getAttribute>',
      '<content:getAttribute attribute="text" beanName="loanAmountGreaterThanMaximumText" escapeJavaScript="true"></content:getAttribute>',
      ${loanPlanData.minimumLoanAmount}, 'rangeCheck']);

    pageValidator.registerRequired('paymentFrequency',
      'change', onPaymentFrequencyChangeSuccess, onPaymentFrequencyChangeFailure, 
      ['<content:getAttribute attribute="text" beanName="paymentFrequencyIsEmptyText" escapeJavaScript="true"></content:getAttribute>']);

    //Interest Rate validations
    pageValidator.registerAllowedCharacters('interestRate',
      'keyup', PageValidator.NUMERIC_CHARACTER_NO_COMMA_REGEXP, onInterestRateChangeSuccess, onInterestRateChangeFailure,
      ['noalert',
      '<content:getAttribute attribute="text" beanName="interestRateBlankOrNonNumericText" 
          escapeJavaScript="true"></content:getAttribute>',
      'invalidCharCheck']);
    pageValidator.registerAllowedCharacters('interestRate',
      'blur', PageValidator.NUMERIC_CHARACTER_NO_COMMA_REGEXP, onInterestRateChangeSuccess, onInterestRateChangeFailure,
      ['alert',
      '<content:getAttribute attribute="text" beanName="interestRateBlankOrNonNumericText" 
          escapeJavaScript="true"></content:getAttribute>',
      'invalidCharCheck']);
    // Checks that the entry is a valid numeric format and positive.
    pageValidator.registerPositiveNumber('interestRate',
      'keyup', onInterestRateChangeSuccess, onInterestRateChangeFailure,
      ['noalert', 
      '<content:getAttribute attribute="text" beanName="interestRateInvalidFormatText" 
          escapeJavaScript="true"></content:getAttribute>', 'formatCheck']);
    pageValidator.registerPositiveNumber('interestRate',
      'blur', onInterestRateChangeSuccess, onInterestRateChangeFailure,
      ['alert', 
      '<content:getAttribute attribute="text" beanName="interestRateInvalidFormatText" 
          escapeJavaScript="true"></content:getAttribute>', 'formatCheck']);
    pageValidator.registerRangeCheck('interestRate',
      'keyup', 0.00001, ${loanConstants.INTEREST_RATE_MAXIMUM}, onInterestRateChangeSuccess, onInterestRateChangeFailure,
      ['noalert',
      '<content:getAttribute attribute="text" beanName="interestRateOutOfRangeText" 
          escapeJavaScript="true"></content:getAttribute>'], 'rangeCheck');
    pageValidator.registerRangeCheck('interestRate',
      'blur', 0.00001, ${loanConstants.INTEREST_RATE_MAXIMUM}, onInterestRateChangeSuccess, onInterestRateChangeFailure,
      ['alert',
      '<content:getAttribute attribute="text" beanName="interestRateOutOfRangeText" 
          escapeJavaScript="true"></content:getAttribute>', 'rangeCheck']);

    onLoanAmountChangeSuccess(undefined, undefined);
    formatLoanAmount();
    onAmortizationYearsChange('noAlert');

  </c:if>
});

</script>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="734">
  <tr>
    <td class="tableheadTD1"><b>
      <c:if test="${displayRules.displayExpandCollapseButton}">
        <img id="loanCalculationsSectionExpandCollapseIcon" src="/assets/unmanaged/images/minus_icon.gif" width="13" height="13"  style="cursor:hand; cursor:pointer">&nbsp;
      </c:if>
      <content:getAttribute attribute="text" beanName="loanCalcualtionsTitleText">
      </content:getAttribute></b>
    </td>
    <td width="141" class="tablehead">&nbsp;</td>
  </tr>
</table>

<div id="loanCalculationsSection">

  <table class="box" border="0" cellpadding="0" cellspacing="0" width="734">
    <tr>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td width="732"><table border="0" cellpadding="0" cellspacing="0" width="733">
          <tr valign="top">
            <td class="tablesubhead">&nbsp;</td>
            <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <c:if test="${displayRules.displayLoanCalculationEditable}">
              <td align="right" class="tablesubhead"><b>${displayRules.loanCalculationEditableColumnHeader}</b></td>
              <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>	                
            <c:if test="${displayRules.displayLoanCalculationAcceptedColumn}">
              <td align="right" class="tablesubhead"><b>${displayRules.loanCalculationAcceptedColumnHeader}</b></td>
              <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>	                
            <c:if test="${displayRules.displayLoanCalculationReviewedColumn}">
              <td align="right" class="tablesubhead"><b>${displayRules.loanCalculationReviewedColumnHeader}</b></td>
              <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>	                
            <c:if test="${displayRules.displayLoanCalculationOriginalColumn}">
              <td align="right" class="tablesubhead"><b>${displayRules.loanCalculationOriginalColumnHeader}</b></td>
              <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>	                
            <c:if test="${displayRules.displayLoanCalculationBlankColumn}">
              <td align="right" class="tablesubhead">&nbsp;</td>
            </c:if>                 
          </tr>

          <tr valign="top">
            <td width="140" class="datacell1"><strong>Maximum loan permitted </strong></td>
            <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <c:if test="${displayRules.displayLoanCalculationEditable}">
              <td width="194" align="right" class="datacell1">
              <c:if test="${displayRules.loanCalculationEditable}">
                <span id="maxLoanAvailableSpan"/>
              </c:if>
              <c:if test="${not displayRules.loanCalculationEditable}">
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.currentLoanParameter.maximumAvailable}"/>
              </c:if>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationAcceptedColumn}">
              <td width="194" align="right" class="datacell1">
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.acceptedParameter.maximumAvailable}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationReviewedColumn}">
              <td width="194" align="right" class="datacell1">
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.reviewedParameter.maximumAvailable}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationOriginalColumn}">
              <td width="194" align="right" class="datacell1">
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.originalParameter.maximumAvailable}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationBlankColumn}">
              <td align="right" class="datacell1">&nbsp;</td>
            </c:if>
            </tr>

          <tr valign="top">
            <td class="datacell2"><strong>Requested amount </strong></td>
            <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <c:if test="${displayRules.displayLoanCalculationEditable}">
              <td align="right" class="datacell2">
                <img id="dynamicErrorIcon_${loanFields.LOAN_AMOUNT.fieldName}"
                     src="/assets/unmanaged/images/error.gif"
                     style="display:none"/>
                <ps:fieldHilight styleIdSuffix="${loanFields.LOAN_AMOUNT.fieldName}"
                                 name="${loanFields.LOAN_AMOUNT.fieldName}" 
                                 singleDisplay="true"/>
              <c:if test="${displayRules.loanAmountEditable}">
                  <ps:trackChanges escape="true" property="loanAmount" name="loanForm"/>
<form:input path="loanAmount" maxlength="12" size="7" cssStyle="{text-align: right}" cssClass="mandatory" id="loanAmount"/>


              </c:if>
              <c:if test="${displayRules.loanAmountDisplayOnlyRecalculated}">
                  <ps:trackChanges escape="true" property="loanAmount" name="loanForm"/>
<input type="hidden" name="loanAmount" id="loanAmount"/>
                  <span id="loanAmountSpan"/>
              </c:if>
              <c:if test="${displayRules.loanAmountDisplayOnly}">
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.currentLoanParameter.loanAmount}"/>
              </c:if>
              </td>
              <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationAcceptedColumn}">
              <td width="194" align="right" class="datacell2">
                <ps:fieldHilight styleIdSuffix="${loanFields.LOAN_AMOUNT.fieldName}"
                                 name="${loanFields.LOAN_AMOUNT.fieldName}" 
                                 singleDisplay="true"/>
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.acceptedParameter.loanAmount}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationReviewedColumn}">
              <td width="194" align="right" class="datacell2">
                <ps:fieldHilight styleIdSuffix="${loanFields.LOAN_AMOUNT.fieldName}"
                                 name="${loanFields.LOAN_AMOUNT.fieldName}" 
                                 singleDisplay="true"/>
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.reviewedParameter.loanAmount}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationOriginalColumn}">
              <td width="194" align="right" class="datacell2">
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.originalParameter.loanAmount}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationBlankColumn}">
              <td align="right" class="datacell2">&nbsp;</td>
            </c:if>
            </tr>

          <tr valign="top">
            <td class="datacell1"><strong>Amortization period </strong></td>
            <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <c:if test="${displayRules.displayLoanCalculationEditable}">
              <td align="right" class="datacell1">
                <img id="dynamicErrorIcon_amortizationPeriod"
                     src="/assets/unmanaged/images/error.gif"
                     style="display:none"/>
                <ps:fieldHilight styleIdSuffix="amortizationPeriod"
                                   name="${loanFields.AMORTIZATION_MONTHS.fieldName}" 
                                   singleDisplay="true"/>
              <c:if test="${displayRules.loanCalculationEditable}">
                <ps:trackChanges escape="true" property="amortizationPeriodYears" name="loanForm"/>
 <form:select onchange="onAmortizationYearsChange('alert')" styleId="amortizationPeriodYears" path="amortizationPeriodYears">


                  <c:forEach var="amortizationPeriodYearIndex" begin="0" end="${loanForm.maximumAmortizationYearsMap[loanForm.loanType]}">
                    <form:option value="${amortizationPeriodYearIndex}">${amortizationPeriodYearIndex}</form:option>
                  </c:forEach>
</form:select>
                years
                <ps:trackChanges escape="true" property="amortizationPeriodMonths" name="loanForm"/>
 <form:select styleId="amortizationPeriodMonths" path="amortizationPeriodMonths" onchange="onAmortizationMonthsChange()">


	              <form:option value="0">0</form:option>
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
</form:select> months
              </c:if>
              <c:if test="${not displayRules.loanCalculationEditable}">
                <c:out value="${loan.currentLoanParameter.amortizationPeriodYears}" /> year(s)
                <c:out value="${loan.currentLoanParameter.amortizationPeriodMonths}" /> month(s)
              </c:if>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationAcceptedColumn}">
              <td width="194" align="right" class="datacell1">
                <ps:fieldHilight styleIdSuffix="amortizationPeriod"
                                   name="${loanFields.AMORTIZATION_MONTHS.fieldName}" 
                                   singleDisplay="true"/>
                <c:out value="${loan.acceptedParameter.amortizationPeriodYears}" /> year(s)
                <c:out value="${loan.acceptedParameter.amortizationPeriodMonths}" /> month(s)
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationReviewedColumn}">
              <td width="194" align="right" class="datacell1">
                <ps:fieldHilight styleIdSuffix="amortizationPeriod"
                                   name="${loanFields.AMORTIZATION_MONTHS.fieldName}" 
                                   singleDisplay="true"/>
                <c:out value="${loan.reviewedParameter.amortizationPeriodYears}" /> year(s)
                <c:out value="${loan.reviewedParameter.amortizationPeriodMonths}" /> month(s)
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationOriginalColumn}">
              <td width="194" align="right" class="datacell1">
                <c:out value="${loan.originalParameter.amortizationPeriodYears}" /> year(s)
                <c:out value="${loan.originalParameter.amortizationPeriodMonths}" /> month(s)
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationBlankColumn}">
              <td align="right" class="datacell1">&nbsp;</td>
            </c:if>
            </tr>

          <tr valign="top">
            <td class="datacell2"><strong>Payment amount </strong></td>
            <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <c:if test="${displayRules.displayLoanCalculationEditable}">
              <td align="right" class="datacell2">
              <c:if test="${displayRules.loanCalculationEditable}">
                <ps:trackChanges escape="true" property="paymentAmount" name="loanForm"/>
<input type="hidden" name="paymentAmount" id="paymentAmount"/>
                <span id="paymentAmountSpan"></span>
              </c:if>
              <c:if test="${not displayRules.loanCalculationEditable}">
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.currentLoanParameter.paymentAmount}"/>
              </c:if>
              </td>
              <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationAcceptedColumn}">
              <td width="194" align="right" class="datacell2">
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.acceptedParameter.paymentAmount}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationReviewedColumn}">
              <td width="194" align="right" class="datacell2">
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.reviewedParameter.paymentAmount}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationOriginalColumn}">
              <td width="194" align="right" class="datacell2">
                <fmt:formatNumber type="currency"
                     currencySymbol="$"
                     minFractionDigits="2"
                     value="${loan.originalParameter.paymentAmount}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationBlankColumn}">
              <td align="right" class="datacell2">&nbsp;</td>
            </c:if>
            </tr>

          <tr valign="top">
            <td class="datacell1"><strong>Payment frequency </strong></td>
            <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <c:if test="${displayRules.displayLoanCalculationEditable}">
              <td align="right" class="datacell1">
                <img id="dynamicErrorIcon_${loanFields.PAYMENT_FREQUENCY.fieldName}"
                     src="/assets/unmanaged/images/error.gif"
                     style="display:none"/>
                <ps:fieldHilight styleIdSuffix="${loanFields.PAYMENT_FREQUENCY.fieldName}"
                                 name="${loanFields.PAYMENT_FREQUENCY.fieldName}" 
                                 singleDisplay="true"/>
              <c:if test="${displayRules.loanCalculationEditable}">
                <ps:trackChanges escape="true" property="paymentFrequency" name="loanForm"/>
                <form:select styleId="paymentFrequency" path="paymentFrequency" cssClass="mandatory">
                  <form:option value="">-select-</form:option>
                  <form:option value="${globalConstants.FREQUENCY_TYPE_WEEKLY}">Weekly</form:option>
                  <form:option value="${globalConstants.FREQUENCY_TYPE_BI_WEEKLY}">Bi-weekly</form:option>
                  <form:option value="${globalConstants.FREQUENCY_TYPE_SEMI_MONTHLY}">Semi-monthly</form:option>
                  <form:option value="${globalConstants.FREQUENCY_TYPE_MONTHLY}">Monthly</form:option>
</form:select>
              </c:if>
              <c:if test="${not displayRules.loanCalculationEditable}">
                <c:out value="${loanPaymentFrequencies[loan.currentLoanParameter.paymentFrequency]}"/>
              </c:if>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationAcceptedColumn}">
              <td width="194" align="right" class="datacell1">
                <ps:fieldHilight styleIdSuffix="${loanFields.PAYMENT_FREQUENCY.fieldName}"
                                 name="${loanFields.PAYMENT_FREQUENCY.fieldName}" 
                                 singleDisplay="true"/>
                <c:out value="${loanPaymentFrequencies[loan.acceptedParameter.paymentFrequency]}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationReviewedColumn}">
              <td width="194" align="right" class="datacell1">
                <ps:fieldHilight styleIdSuffix="${loanFields.PAYMENT_FREQUENCY.fieldName}"
                                 name="${loanFields.PAYMENT_FREQUENCY.fieldName}" 
                                 singleDisplay="true"/>
                <c:out value="${loanPaymentFrequencies[loan.reviewedParameter.paymentFrequency]}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationOriginalColumn}">
              <td width="194" align="right" class="datacell1">
                <c:out value="${loanPaymentFrequencies[loan.originalParameter.paymentFrequency]}"/>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationBlankColumn}">
              <td align="right" class="datacell1">&nbsp;</td>
            </c:if>
            </tr>

          <tr valign="top">
            <td class="datacell2"><strong>Interest rate </strong></td>
            <td class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <c:if test="${displayRules.displayLoanCalculationEditable}">
              <td align="right" class="datacell2">
                <img id="dynamicErrorIcon_${loanFields.INTEREST_RATE.fieldName}"
                     src="/assets/unmanaged/images/error.gif"
                     style="display:none"/>
                <ps:fieldHilight styleIdSuffix="${loanFields.INTEREST_RATE.fieldName}"
                                 name="${loanFields.INTEREST_RATE.fieldName}" 
                                 singleDisplay="true"/>
              <c:if test="${displayRules.loanCalculationEditable}">
                <ps:trackChanges escape="true" property="interestRate" name="loanForm"/>
<form:input path="interestRate" maxlength="6" size="5" cssStyle="{text-align: right}" cssClass="mandatory" id="interestRate"/> %


              </c:if>
              <c:if test="${not displayRules.loanCalculationEditable}">
                <c:out value="${loan.currentLoanParameter.interestRate}" />%
              </c:if>
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationAcceptedColumn}">
              <td width="194" align="right" class="datacell2">
                <ps:fieldHilight styleIdSuffix="${loanFields.INTEREST_RATE.fieldName}"
                                 name="${loanFields.INTEREST_RATE.fieldName}" 
                                 singleDisplay="true"/>
                <c:out value="${loan.acceptedParameter.interestRate}" />% 
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationReviewedColumn}">
              <td width="194" align="right" class="datacell2">
                <ps:fieldHilight styleIdSuffix="${loanFields.INTEREST_RATE.fieldName}"
                                 name="${loanFields.INTEREST_RATE.fieldName}" 
                                 singleDisplay="true"/>
                <c:out value="${loan.reviewedParameter.interestRate}" />% 
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationOriginalColumn}">
              <td width="194" align="right" class="datacell2">
                <c:out value="${loan.originalParameter.interestRate}" />% 
              </td>
              <td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </c:if>
            <c:if test="${displayRules.displayLoanCalculationBlankColumn}">
              <td align="right" class="datacell2">&nbsp;</td>
            </c:if>
          </tr>
      </table></td>

      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
  </table>

</div>
