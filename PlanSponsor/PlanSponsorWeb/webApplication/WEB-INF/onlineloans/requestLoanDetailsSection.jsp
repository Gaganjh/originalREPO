<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="render" uri="/WEB-INF/render.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/java-encoder.tld" prefix="e" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />
<un:useConstants var="loanConstants" className="com.manulife.pension.service.loan.domain.LoanConstants" />
<un:useConstants var="loanFields" className="com.manulife.pension.service.loan.LoanField" />
<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />

<content:contentBean
  contentId="${loanContentConstants.LOAN_DETAILS_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="loanDetalsTitleText"/>
<content:contentBean
  contentName="${loanContentConstants.INVALID_DATE_FORMAT}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="invalidDateFormatText"/>
<content:contentBean
  contentName="${loanContentConstants.INVALID_MONTH_DAY_YEAR}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="invalidMonthDayYearText"/>
<content:contentBean
  contentName="${loanContentConstants.LOAN_REASON_TOO_LONG}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="loanReasonTooLongText"/>
<content:contentBean
  contentName="${loanContentConstants.LOAN_REASON_INVALID_CHARACTER}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="loanReasonInvalidCharactersText"/>
<content:contentBean
  contentName="${loanContentConstants.TPA_LOAN_ISSUE_FEE_NON_NUMERIC}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="tpaLoanFeeInvalidCharactersText"/>
<content:contentBean
  contentId="${loanContentConstants.DEFAULT_PROVISION_EXPLANATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="defaultProvisionExplanationText"/>
<content:contentBean
  contentName="${loanContentConstants.DEFAULT_PROVISION_TOO_LONG}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="defaultProvisionTooLongText"/>
<content:contentBean
  contentName="${loanContentConstants.DEFAULT_PROVISION_INVALID_CHARACTER}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="defaultProvisionInvalidCharacterText"/>


<script language="JavaScript1.2" type="text/javascript">

var isLoanReasonTooLongSuccess = true;

function onLoanReasonTooLongSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.LOAN_REASON.fieldName}');
  isLoanReasonTooLongSuccess = true;
}

function onLoanReasonTooLongFailure(e, callbackParams) {
  showIconForFailure('${loanFields.LOAN_REASON.fieldName}');
  var alertType = callbackParams[0];
  var valueLength = callbackParams[1];
  if (alertType == 'alert') {
    var errorMessage = '<content:getAttribute attribute="text" beanName="loanReasonTooLongText" escapeJavaScript="true"></content:getAttribute>';
    alert(errorMessage.replace(/\{0\}/, valueLength));
  }
  isLoanReasonTooLongSuccess = false;

  // clear the length value in callbackParams.
  callbackParams.length = 1;
}

function onLoanReasonInvalidCharacterSuccess(e, callbackParams) {
  // Only if the LoanReasonTooLong edit was successful do we want to hide the 
  // error icon.  If it failed, we don't want to overwrite the error icon already displayed. 
  if (isLoanReasonTooLongSuccess) {
    showIconForSuccess('${loanFields.LOAN_REASON.fieldName}');
  }
}

function onLoanReasonInvalidCharacterFailure(e, callbackParams) {
  showIconForFailure('${loanFields.LOAN_REASON.fieldName}');
  var alertType = callbackParams[0];
  if (alertType == 'alert') {
    var errorMessage = '<content:getAttribute attribute="text" beanName="loanReasonInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>'; 

    // Change message verb to 'is' or 'are' depending on number of invalid characters.
    errorMessage = errorMessage.replace(/\{1\}/, callbackParams[1].length > 1 ? 'are' : 'is');  

    // Add invalid characters to message string
    errorMessage = errorMessage.replace(/\{0\}/, callbackParams[1]);

    alert(errorMessage);
  }

  // clear the invalid character array.
  callbackParams.length = 1;
}

function onExpirationDateValidSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.EXPIRATION_DATE.fieldName}');
}

function onExpirationDateValidFailure(e, callbackParams) {
  showIconForFailure('${loanFields.EXPIRATION_DATE.fieldName}');
  var alertType = callbackParams[0];
  if (alertType == 'alert') {
    alert(getInvalidDateMessage(callbackParams[1]));
  }
  // clear the invalid character array entry.
  callbackParams.length = 1;
}

function onPayrollDateValidSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.PAYROLL_DATE.fieldName}');
}

function onPayrollDateValidFailure(e, callbackParams) {
  showIconForFailure('${loanFields.PAYROLL_DATE.fieldName}');
  var alertType = callbackParams[0];
  if (alertType == 'alert') {
    alert(getInvalidDateMessage(callbackParams[1]));
  }
  // clear the invalid character array.
  callbackParams.length = 1;
}

function onTpaLoanFeeInvalidCharacterSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.TPA_LOAN_ISSUE_FEE.fieldName}');
  var field = e.target;
  var value = field.value;
  if (e.type == 'blur') {
    if (! isNaN(value) && value.length > 0) {
      field.value = numberUtils.formatAmount(numberUtils.deformatNumber(value), false, true);
    }
  }
}

function onTpaLoanFeeInvalidCharacterFailure(e, callbackParams) {
  showIconForFailure('${loanFields.TPA_LOAN_ISSUE_FEE.fieldName}');
  var alertType = callbackParams[0];
  if (alertType == 'alert') {
    var errorMessage = '<content:getAttribute attribute="text" beanName="tpaLoanFeeInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>'; 
    alert(errorMessage);
  }
}

var isDefaultProvisionTooLongSuccess = true;

function onDefaultProvisionTooLongSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.DEFAULT_PROVISION.fieldName}');
  isDefaultProvisionTooLongSuccess = true;
}

function onDefaultProvisionTooLongFailure(e, callbackParams) {
  showIconForFailure('${loanFields.DEFAULT_PROVISION.fieldName}');
  var alertType = callbackParams[0];
  var valueLength = callbackParams[1];
  if (alertType == 'alert') {
    var errorMessage = '<content:getAttribute attribute="text" beanName="defaultProvisionTooLongText" escapeJavaScript="true"></content:getAttribute>'; 
    alert(errorMessage.replace(/\{0\}/, valueLength));
  }
  isDefaultProvisionTooLongSuccess = false;

  // clear the length value in callbackParams.
  callbackParams.length = 1;
}

function onDefaultProvisionInvalidCharacterSuccess(e, callbackParams) {
  // Only if the DefaultProvisionTooLong edit was successful do we want to hide the 
  // error icon.  If it failed, we don't want to overwrite the error icon already displayed. 
  if (isDefaultProvisionTooLongSuccess) {
    showIconForSuccess('${loanFields.DEFAULT_PROVISION.fieldName}');
  }
}

function onDefaultProvisionInvalidCharacterFailure(e, callbackParams) {
  showIconForFailure('${loanFields.DEFAULT_PROVISION.fieldName}');
  var alertType = callbackParams[0];
  if (alertType == 'alert') {
    var errorMessage = '<content:getAttribute attribute="text" beanName="defaultProvisionInvalidCharacterText" escapeJavaScript="true"></content:getAttribute>'; 

    // Change message verb to 'is' or 'are' depending on number of invalid characters.
    errorMessage = errorMessage.replace(/\{1\}/, callbackParams[1].length > 1 ? 'are' : 'is');  

    // Add invalid characters to message string
    errorMessage = errorMessage.replace(/\{0\}/, callbackParams[1]);

    alert(errorMessage);
  }

  // clear the invalid character array.
  callbackParams.length = 1;
}


function getInvalidDateMessage(errorCode) {
  /* errorCode value meanings, as defined in DateUtilt.js:
   * OK: if the date is good.
   * FORMAT_ERROR: if there is a format error (error message would be: The date format should be : mm/dd/yyyy)
   * INVALID_MONTH: if the month is invalid
   * INVALID_DAY_IN_MONTH: if the date is invalid w.r.t. the month.
   * INVALID_DATE: if the date is invalid
   * INVALID_YEAR: if the year is invalid
   */
  
  var message;
  switch (errorCode){
    case dateUtils.FORMAT_ERROR: 
      message = '<content:getAttribute attribute="text" beanName="invalidDateFormatText" escapeJavaScript="true"></content:getAttribute>';
      break;
    default:
      message = '<content:getAttribute attribute="text" beanName="invalidMonthDayYearText" escapeJavaScript="true"></content:getAttribute>';
  }
  return message;
}

function rebuildYearsDropdown(selectElement, minimumValue, maximumValue) {
  var selectedIndex = selectElement.selectedIndex;
 
  /*
   * To account for the initial drop down box.
   */
  if (selectElement.length == 1) {
    selectedIndex = -1;
  }
  
  while (selectElement.length > 0) {
    selectElement.remove(0);
  }
  
  for (var i = minimumValue; i <= maximumValue; i++) {
    var optionElement = document.createElement("OPTION");
    optionElement.value = i;
    optionElement.text = i;
    try {
      selectElement.add(optionElement, null); // non IE
    } catch(ex) {
      selectElement.add(optionElement); // IE
    }
  }

  if (selectedIndex != -1 && selectedIndex < maximumValue) {
    selectElement.selectedIndex = selectedIndex;
  } else {
    selectElement.selectedIndex = selectElement.length - 1;
  }
}

function getAmortizationMonths() {
  var amortizationPeriodYearsSelect = $("#amortizationPeriodYears")[0];
  var amortizationPeriodMonthsSelect = $("#amortizationPeriodMonths")[0];
  var months = amortizationPeriodMonthsSelect.selectedIndex + (amortizationPeriodYearsSelect.selectedIndex * 12);
  return months;
}

function updateLoanMaturityDate() {
  var months = getAmortizationMonths();
  var startDate = dateUtils.parseDate("${loanForm.startDate}");
  startDate.setMonth(startDate.getMonth() + months);
  var monthStr = startDate.getMonth() + 1 + "";
  if (monthStr.length == 1) {
    monthStr = "0" + monthStr;
  }
  var dateStr = startDate.getDate() + "";
  if (dateStr.length == 1) {
    dateStr = "0" + dateStr;
  }
  
  var maturityDate = monthStr + "/" + dateStr + "/" + startDate.getFullYear();
  $("#loanMaturityDateSpan").text(maturityDate);
  $("#maturityDate").val(maturityDate);
}

function onLoanTypeChange(resetMaximumAmortizationYears) {
  var selectedRadio = $("input[name='loanType']:checked").val();
  var maxAmortizationYears = undefined;
  if (selectedRadio == '${loanConstants.TYPE_PRIMARY_RESIDENCE}') {
    maxAmortizationYears = ${loanPlanData.maximumAmortizationYearsMap[loanConstants.TYPE_PRIMARY_RESIDENCE]};
  } else if (selectedRadio == '${loanConstants.TYPE_HARDSHIP}') {
    maxAmortizationYears = ${loanPlanData.maximumAmortizationYearsMap[loanConstants.TYPE_HARDSHIP]};
  } else {
    maxAmortizationYears = ${loanPlanData.maximumAmortizationYearsMap[loanConstants.TYPE_GENERAL_PURPOSE]};
  }

  var maximumAmortizationPeriodYearsSelect = $("#maximumAmortizationPeriodYears")[0];
  var amortizationPeriodYearsSelect = $("#amortizationPeriodYears")[0];
  
  rebuildYearsDropdown(maximumAmortizationPeriodYearsSelect, 1, maxAmortizationYears);
  if (resetMaximumAmortizationYears) {
    // reset the maximum amortization period.
    maximumAmortizationPeriodYearsSelect.selectedIndex = maximumAmortizationPeriodYearsSelect.length - 1;
  }
  rebuildYearsDropdown(amortizationPeriodYearsSelect, 0, maxAmortizationYears);

  if (amortizationPeriodYearsSelect.selectedIndex == amortizationPeriodYearsSelect.length - 1) {
    $("#amortizationPeriodMonths")[0].selectedIndex = 0;
    $("#amortizationPeriodMonths").prop("disabled", true);
  } else {
    $("#amortizationPeriodMonths").prop("disabled", false);
  }

  onAmortizationYearsChange('noAlert');
  updateLoanMaturityDate();
}

function invokeCalendar(event, styleId) {
  dateUtils.invokeCalendar(event, styleId);
  // Hide the payroll date error Icon when appropriate.
  showIconForSuccess(styleId);
}


$(document).ready(function() {

  <c:if test="${displayRules.showLoanTypeAsEditable}">
  onLoanTypeChange(false);
  </c:if>

  <c:if test="${displayRules.loanCalculationEditable}">
  updateLoanMaturityDate();
  </c:if>

  pageValidator.registerMaximumLength('loanReason',
    'keyup', ${loanConstants.LOAN_REASON_MAXIMUM_LENGTH}, true, onLoanReasonTooLongSuccess, 
    onLoanReasonTooLongFailure, ['noalert']);
  pageValidator.registerMaximumLength('loanReason',
    'blur', ${loanConstants.LOAN_REASON_MAXIMUM_LENGTH}, true, onLoanReasonTooLongSuccess,
    onLoanReasonTooLongFailure, ['alert']);

  pageValidator.registerAllowedCharacters('loanReason',
    'keyup', PageValidator.WEB_MULTILINE_CHARACTER_REGEXP, onLoanReasonInvalidCharacterSuccess,
    onLoanReasonInvalidCharacterFailure, ['noalert']);
  pageValidator.registerAllowedCharacters('loanReason',
    'blur', PageValidator.WEB_MULTILINE_CHARACTER_REGEXP, onLoanReasonInvalidCharacterSuccess,
    onLoanReasonInvalidCharacterFailure, ['alert']);
  
  pageValidator.registerValidDate('expirationDate',
    'keyup', onExpirationDateValidSuccess, onExpirationDateValidFailure, ['noalert']);
  pageValidator.registerValidDate('expirationDate',
    'blur', onExpirationDateValidSuccess, onExpirationDateValidFailure, ['alert']);

  pageValidator.registerValidDate('payrollDate',
    'keyup', onPayrollDateValidSuccess, onPayrollDateValidFailure, ['noalert']);
  pageValidator.registerValidDate('payrollDate',
    'blur', onPayrollDateValidSuccess, onPayrollDateValidFailure, ['alert']);

  pageValidator.registerAllowedCharacters('tpaLoanFee',
    'keyup', PageValidator.NUMERIC_CHARACTER_REGEXP, onTpaLoanFeeInvalidCharacterSuccess,
    onTpaLoanFeeInvalidCharacterFailure, ['noalert']);
  pageValidator.registerAllowedCharacters('tpaLoanFee',
    'blur', PageValidator.NUMERIC_CHARACTER_REGEXP, onTpaLoanFeeInvalidCharacterSuccess,
    onTpaLoanFeeInvalidCharacterFailure, ['alert']);
  
  pageValidator.registerMaximumLength('defaultProvision',
    'keyup', ${loanConstants.DEFAULT_PROVISION_MAXIMUM_LENGTH}, true, 
    onDefaultProvisionTooLongSuccess, onDefaultProvisionTooLongFailure, ['noalert']);
  pageValidator.registerMaximumLength('defaultProvision',
    'blur', ${loanConstants.DEFAULT_PROVISION_MAXIMUM_LENGTH}, true, 
    onDefaultProvisionTooLongSuccess, onDefaultProvisionTooLongFailure, ['alert']);

  pageValidator.registerAllowedCharacters('defaultProvision',
    'keyup', PageValidator.WEB_MULTILINE_CHARACTER_REGEXP, 
    onDefaultProvisionInvalidCharacterSuccess, onDefaultProvisionInvalidCharacterFailure, ['noalert']);
  pageValidator.registerAllowedCharacters('defaultProvision',
    'blur', PageValidator.WEB_MULTILINE_CHARACTER_REGEXP, 
    onDefaultProvisionInvalidCharacterSuccess, onDefaultProvisionInvalidCharacterFailure, ['alert']);
  
});

</script>


<table class="box" border="0" cellpadding="0" cellspacing="0"
	width="734">
	<tr>
		<td class="tableheadTD1">
        <c:if test="${displayRules.displayExpandCollapseButton}">
		<img id="loanDetailsSectionExpandCollapseIcon"
			src="/assets/unmanaged/images/minus_icon.gif" width="13" height="13"
			style="cursor: hand; cursor: pointer">&nbsp;
	    </c:if>
	    <b>
          <content:getAttribute attribute="text" beanName="loanDetalsTitleText">
          </content:getAttribute>
          </b>
        </td>
		<td width="141" class="tablehead">&nbsp;</td>
	</tr>
</table>

<div id="loanDetailsSection">
<table class="box" border="0" cellpadding="0" cellspacing="0"
	width="734">
	<tr>
		<td width="1" class="boxborder"><img
			src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
		<td width="732">
		<table border="0" cellpadding="0" cellspacing="0" width="733">
			<tr valign="top">
				<td class="datacell1"><strong>Loan type </strong></td>
				<td width="1" class="datadivider"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

				<td colspan="5" class="datacell1">
				<c:choose>
		 		  <c:when test="${displayRules.showLoanTypeAsEditable}">
		 		    <ps:trackChanges escape="true" property="loanType" name="loanForm"/>
		 		    <c:forEach items="${loanPlanData.loanTypeList}" var="element" >
		 		    <form:radiobutton path="loanType" disabled="${element.disabled}" onclick="onLoanTypeChange(true)" value="${element.loanTypeCode}" /><span id="loanType_${element.loanTypeCode}">${loanTypes[element.loanTypeCode]}</span>
					</c:forEach>
				  </c:when>
				  <c:otherwise>
				    <c:out value="${loanTypes[loan.loanType]}"/>
				  </c:otherwise>
				</c:choose>
			    </td>
			</tr>
			<tr valign="top">
				<td class="datacell1">
                  <img id="dynamicErrorIcon_${loanFields.LOAN_REASON.fieldName}"
                       src="/assets/unmanaged/images/error.gif"
                       style="display:none"/>
	              <ps:fieldHilight styleIdSuffix="${loanFields.LOAN_REASON.fieldName}"
                                   name="${loanFields.LOAN_REASON.fieldName}" 
                                   singleDisplay="true"/>
		  		  <strong>Loan reason<br>(max. 250 characters) </strong></td>
				<td width="1" class="datadivider"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td colspan="5" class="datacell1">
				  <c:choose>
				    <c:when test="${displayRules.showLoanReasonAsEditable}">
				      <ps:trackChanges escape="true" property="loanReason" name="loanForm" />
<form:textarea path="loanReason" cols="50" rows="4" id="loanReason">
</form:textarea>
				    </c:when>
				    <c:otherwise>
				      <c:out value="${loan.loanReason}" />
				    </c:otherwise>
				  </c:choose>
				</td>
			</tr>
			<tr valign="top">
				<td class="datacell1"><strong>Request date </strong></td>
				<td width="1" class="datadivider"><img	src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td width="178" class="datacell1"><c:out value="${loanForm.requestDate}"/></td>

				<td width="1" class="datadivider"><img
				  src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td width="182" class="datacell1">
                  <img id="dynamicErrorIcon_${loanFields.EXPIRATION_DATE.fieldName}"
                       src="/assets/unmanaged/images/error.gif"
                       style="display:none"/>
                  <ps:fieldHilight styleIdSuffix="${loanFields.EXPIRATION_DATE.fieldName}"
                                   name="${loanFields.EXPIRATION_DATE.fieldName}" 
                                   singleDisplay="true"/>
                  <strong>Expiration date</strong>
                </td>
				<td width="1" class="datadivider"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td width="184" class="datacell1">
				  <c:choose>
				    <c:when test="${displayRules.showExpirationDateAsEditable}">
                      <ps:trackChanges escape="true" property="${loanFields.EXPIRATION_DATE.fieldName}" name="loanForm" />
<form:input path="${loanFields.EXPIRATION_DATE.fieldName}" maxlength="10" cssClass="mandatory" id="${loanFields.EXPIRATION_DATE.fieldName}"></form:input>
  				      &nbsp;&nbsp;
                      <img onclick="invokeCalendar(event, '${loanFields.EXPIRATION_DATE.fieldName}')"
                           src="/assets/unmanaged/images/cal.gif" border="0">
 			          <span class="highlight">(mm/dd/yyyy)</span>
				    </c:when>
				    <c:otherwise>
                      <fmt:formatDate value="${loan.expirationDate}" 
                                      type="date" pattern="${renderConstants.MEDIUM_MDY_SLASHED}"/> 
				    </c:otherwise>
				  </c:choose>
                        </td>
			</tr>
			<tr valign="top">
				<td class="datacell1"><strong>Estimated loan start
				date </strong></td>
				<td width="1" class="datadivider"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td class="datacell1">
                   ${loanForm.startDate}
                </td>
				<td width="1" class="datadivider"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

				<td class="datacell1"><strong>Estimated loan maturity
				date </strong></td>
				<td width="1" class="datadivider"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td class="datacell1">
				  <span id="loanMaturityDateSpan">
				    ${e:forHtmlContent(loanForm.maturityDate)}
				  </span>
<input type="hidden" name="maturityDate" id="maturityDate"/>
				</td>
			</tr>

			<tr valign="top">
				<td class="datacell1">
                  <img id="dynamicErrorIcon_${loanFields.PAYROLL_DATE.fieldName}"
                       src="/assets/unmanaged/images/error.gif"
                       style="display:none"/>
                  <ps:fieldHilight styleIdSuffix="${loanFields.PAYROLL_DATE.fieldName}"
                                   name="${loanFields.PAYROLL_DATE.fieldName}" 
                                   singleDisplay="true"/>
                  <strong>Next payroll date </strong></td>
				<td width="1" class="datadivider"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td class="datacell1">
                  <c:choose>
                  	<c:when test="${displayRules.payrollDatePendingStatus}">
                      <fmt:formatDate value="${loan.firstPayrollDate}" 
                                      type="date" pattern="${renderConstants.MEDIUM_MDY_SLASHED}"/> 
                    </c:when>
                    <c:when test="${displayRules.showPayrollDateAsEditable}">
                      <ps:trackChanges escape="true" property="${loanFields.PAYROLL_DATE.fieldName}" name="loanForm"/>
<form:input path="${loanFields.PAYROLL_DATE.fieldName}" maxlength="10" cssClass="${displayRules.payrollDateStyleClass}" id="${loanFields.PAYROLL_DATE.fieldName}"></form:input>
                      <a>
                      <img onclick="invokeCalendar(event, '${loanFields.PAYROLL_DATE.fieldName}')"
                           src="/assets/unmanaged/images/cal.gif" border="0"></a> <span
                           class="highlight">(mm/dd/yyyy)</span></td>
                    </c:when>
                    <c:otherwise>
                      <fmt:formatDate value="${loan.firstPayrollDate}" 
                                      type="date" pattern="${renderConstants.MEDIUM_MDY_SLASHED}"/> 
                    </c:otherwise>
                  </c:choose>
				<td width="1" class="datadivider"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

				<td class="datacell1"><strong>Maximum amortization period </strong></td>
				<td width="1" class="datadivider"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td class="datacell1">

                  <c:choose>
                    <c:when test="${displayRules.showMaximumAmortizationPeriodAsEditable}">
                      <ps:trackChanges escape="true" property="maximumAmortizationPeriodYears" name="loanForm"/>
                      <form:select styleId="maximumAmortizationPeriodYears" path="maximumAmortizationPeriodYears" >
                        <c:forEach var="maximumAmortizationPeriodYearIndex" begin="1" end="${loanForm.maximumAmortizationYearsMap[loanForm.loanType]}">
                          <form:option value="${maximumAmortizationPeriodYearIndex}">${maximumAmortizationPeriodYearIndex}</form:option>
                        </c:forEach>
</form:select>
                    </c:when>
                    <c:otherwise>
                      <c:out value="${loan.maximumAmortizationYears}" /> &nbsp;
                    </c:otherwise>
                  </c:choose>
				  years
				</td>
			</tr>

            <c:if test="${displayRules.displayTpaLoanIssueFee}">
              <tr valign="top">
                  <td width="186" class="datacell1">
                    <img id="dynamicErrorIcon_${loanFields.TPA_LOAN_ISSUE_FEE.fieldName}"
                         src="/assets/unmanaged/images/error.gif"
                         style="display:none"/>
                    <ps:fieldHilight styleIdSuffix="${loanFields.TPA_LOAN_ISSUE_FEE.fieldName}"
                                     name="${loanFields.TPA_LOAN_ISSUE_FEE.fieldName}"
                                     displayActivityHistory="${displayRules.showFeeActivityHistoryIcon}"
                                     singleDisplay="true"/>
                    <strong>TPA loan issue fee </strong></td>
                  <td width="1" class="datadivider"><img
                      src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                  <td colspan="5" class="datacell1">
                    <c:choose>
                      <c:when test="${displayRules.showTpaLoanIssueFeeAsEditable}">
                        <ps:trackChanges escape="true" property="tpaLoanFee" name="loanForm" />
<form:input path="tpaLoanFee" maxlength="10" size="5" cssStyle="{text-align: right}" id="tpaLoanFee"/>



                      </c:when>
                      <c:otherwise>
                        <fmt:formatNumber type="currency"
                             currencySymbol="$"
                             minFractionDigits="2"
                             value="${loanForm.tpaLoanFee}"/>
                      </c:otherwise>
                    </c:choose>
                  </td>
              </tr>
            </c:if>

			<tr valign="top">
				<td width="186" class="datacell1">
                  <img id="dynamicErrorIcon_${loanFields.DEFAULT_PROVISION.fieldName}"
                       src="/assets/unmanaged/images/error.gif"
                       style="display:none"/>
                  <ps:fieldHilight styleIdSuffix="${loanFields.DEFAULT_PROVISION.fieldName}"
                                   name="${loanFields.DEFAULT_PROVISION.fieldName}"
                                   singleDisplay="true"/>
                  <strong>Default provision </strong></td>
				<td width="1" class="datadivider"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td colspan="5" class="datacell1">&nbsp;</td>
            </tr>
            <c:if test="${displayRules.displayDefaultProvisionExplanation}">
              <tr valign="top">
                  <td colspan="7" class="datacell1">
                    <content:getAttribute attribute="text" beanName="defaultProvisionExplanationText">
                    </content:getAttribute>
                  </td>
			  </tr>
            </c:if>
			<tr valign="top">
                <td colspan="7" class="datacell1">

                  <c:choose>
                    <c:when test="${displayRules.showDefaultProvisionAsEditable}">
                      <ps:trackChanges escape="true" property="defaultProvision"  name="loanForm"/>
<form:textarea path="defaultProvision" cols="100" rows="3" cssClass="mandatory" id="defaultProvision">

</form:textarea>
                    </c:when>
                    <c:otherwise>
                      <c:out value="${loan.defaultProvision}" />
                    </c:otherwise>
                  </c:choose>
				</td>
			</tr>
		</table>
		</td>
		<td width="1" class="boxborder"><img
			src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	</tr>
	<tr>
		<td colspan="3" class="boxborder"><img
			src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	</tr>
</table>
</div>
