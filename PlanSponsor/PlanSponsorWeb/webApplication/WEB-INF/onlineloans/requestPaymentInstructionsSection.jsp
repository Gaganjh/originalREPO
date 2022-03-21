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
<%@ taglib prefix="render" uri="/WEB-INF/render.tld" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<un:useConstants var="loanConstants" className="com.manulife.pension.service.loan.domain.LoanConstants" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanFields" className="com.manulife.pension.service.loan.LoanField" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />
<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants"/>
<un:useConstants var="globalConstants" className="com.manulife.pension.common.GlobalConstants" />
<un:useConstants var="payeeConstants" className="com.manulife.pension.service.distribution.valueobject.Payee" />
<un:useConstants var="paymentInstructionConstants" className="com.manulife.pension.service.distribution.valueobject.PaymentInstruction" />
<un:useConstants var="commonConstants" className="com.manulife.pension.platform.web.CommonConstants" />

<content:contentBean
  contentId="${loanContentConstants.PAYMENT_INSTRUCTIONS_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="paymentInstructionsSectionTitleText"/>
<content:contentBean
  contentId="${loanContentConstants.WIRE_CHARGES_CONTENT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="wireChargesContentText"/>
<content:contentBean
  contentName="${loanContentConstants.ADDRESS_LINE1_INVALID_CHARACTER}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="addressLine1InvalidCharactersText"/>
<content:contentBean
  contentName="${loanContentConstants.ADDRESS_LINE2_INVALID_CHARACTER}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="addressLine2InvalidCharactersText"/>
<content:contentBean
  contentName="${loanContentConstants.CITY_INVALID_CHARACTER}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="cityInvalidCharactersText"/>
<content:contentBean
  contentName="${loanContentConstants.STATE_INVALID_CHARACTER}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="stateInvalidCharactersText"/>
<content:contentBean
  contentName="${loanContentConstants.ZIP_CODE_INVALID_CHARACTER}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="zipCodeInvalidCharactersText"/>
<content:contentBean
  contentName="${loanContentConstants.ZIP_CODE_INVALID_US_FORMAT}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="zipCodeInvalidUSFormatText"/>
<content:contentBean
  contentName="${loanContentConstants.ABA_ROUTING_NUMBER_NON_NUMERIC}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="abaRoutingNumberNonNumericText"/>
<content:contentBean
  contentId="${loanContentConstants.BANK_LOOKUP_LINK}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="bankLookupLinkText"/>
<content:contentBean
  contentName="${loanContentConstants.ACCOUNT_NUMBER_INVALID_CHARACTER}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="accountNumberInvalidCharactersText"/>
<content:contentBean
  contentId="${loanContentConstants.PAYMENT_INSTRUCTIONS_FOOTER}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="paymentInstructionsFooterText"/>

<content:contentBean
  contentName="${loanContentConstants.BANK_LOOKUP_BANK_SELECTION_REQUIRED}"
  type="${contentConstants.TYPE_MESSAGE}"
  beanName="bankLookupBankSelectionRequired"/>
<content:contentBean
  contentName="${loanContentConstants.BANK_LOOKUP_BANK_NAME_REQUIRED}"
  type="${contentConstants.TYPE_MESSAGE}"
  beanName="bankLookupBankNameRequired"/>
<content:contentBean
  contentName="${loanContentConstants.BANK_LOOKUP_BANK_NAME_INVALID_CHARACTER}"
  type="${contentConstants.TYPE_MESSAGE}"
  beanName="bankLookupBankNameInvalidCharacter"/>

<script type="text/javascript">

var greenArrowIcon = "/assets/unmanaged/images/arrow_green.gif";
var isFirstDisplayOfPage = true;


function onInvalidCharacterSuccess(e, callbackParams) {
  var paymentMethodCode = $("input[name='paymentMethod']:checked").val();
  var checkThatFailed = callbackParams[2];
  var errorIconStyleIdSuffix = callbackParams[3];

  if (paymentMethodCode == '${payeeConstants.CHECK_PAYMENT_METHOD_CODE}') {
    if (checkThatFailed != 'ApolloAllowedChars') {
      return;    // we don't want to hide the errorIcon in this case.
    }
  } else {
    if (checkThatFailed != 'ElectronicPaymentAllowedChars') {
      return;    // we don't want to hide the errorIcon in this case.
    }
  }
  showIconForSuccess(errorIconStyleIdSuffix);
}

function onInvalidCharacterFailure(e, callbackParams) {
  var alertType = callbackParams[0];
  var paymentMethodCode = $("input[name='paymentMethod']:checked").val();
  var checkThatFailed = callbackParams[2];
  var errorIconStyleIdSuffix = callbackParams[3];
  var displayError = true;

  if (paymentMethodCode == '${payeeConstants.CHECK_PAYMENT_METHOD_CODE}') {
    if (checkThatFailed != 'ApolloAllowedChars') {
      displayError = false;    // we don't want to display an error in this case.
    }
  } else {
    if (checkThatFailed != 'ElectronicPaymentAllowedChars') {
      displayError = false;    // we don't want to display an error in this case.
    }
  }
  if (displayError) {
    showIconForFailure(errorIconStyleIdSuffix);
    if (alertType == 'alert') {
      var errorMessage = callbackParams[1];
      var invalidCharsList = callbackParams[4];
      alert(errorMessage.replace(/\{0\}/, invalidCharsList));
    }
  }

  // Clear the invalid character entry from the end of the callbackParms array,
  // otherwise the array keeps growing on subsequent passes through calls to 
  // pageValidator.registerAllowedCharacters.
  callbackParams.length = callbackParams.length - 1;
}

var isZipCode1EditSuccess = true;
var isZipCode2PositiveWholeNumberSuccess = true;
var isZipCode2LengthCheckSuccess = true;

function onZipCodeInvalidUSFormatSuccess(e, callbackParams) {
  var field = e.target;
  var value = field.value;
  var checkPerformed = callbackParams[2];

  if (field.id == 'zipCode1') {
    isZipCode1EditSuccess = true;
    if (isZipCode2PositiveWholeNumberSuccess && isZipCode2LengthCheckSuccess) {
      showIconForSuccess('${loanFields.ZIP_CODE.fieldName}');
    }
  }
  if (field.id == 'zipCode2') {
    if (checkPerformed == 'positiveWholeNumber') {
      isZipCode2PositiveWholeNumberSuccess = true;
      if (isZipCode1EditSuccess && isZipCode2LengthCheckSuccess) {
        showIconForSuccess('${loanFields.ZIP_CODE.fieldName}');
      }
    }
    if (checkPerformed == 'lengthCheck') {
      isZipCode2LengthCheckSuccess = true;
      if (isZipCode1EditSuccess && isZipCode2PositiveWholeNumberSuccess) {
        showIconForSuccess('${loanFields.ZIP_CODE.fieldName}');
      }
    }
  }
  if (!isNaN(value) && value.length > 0) {
    if (e.type == 'blur') {
        field.value = numberUtils.deformatNumber(value);
    }
  }
}

function onZipCodeInvalidUSFormatFailure(e, callbackParams) {
  var field = e.target;
  var alertType = callbackParams[0];
  var checkThatFailed = callbackParams[2];

  showIconForFailure('${loanFields.ZIP_CODE.fieldName}');
  if (field.id == 'zipCode1') {
    isZipCode1EditSuccess = false;
  }
  if (field.id == 'zipCode2') {
    if (checkThatFailed == 'positiveWholeNumber') {
      isZipCode2PositiveWholeNumberSuccess = false;
    }
    if (checkThatFailed == 'lengthCheck') {
      isZipCode2LengthCheckSuccess = false;
    }
  }
  if (alertType == 'alert') {
    var errorMessage = callbackParams[1];
    alert(errorMessage);
  }
}

var isAbaRoutingNumberNumericSuccess = true;
var isAbaRoutingNumberEditSuccess = true;

function onAbaRoutingNumberNumericSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.ABA_ROUTING_NUMBER.fieldName}');
  isAbaRoutingNumberNumericSuccess = true;
  isAbaRoutingNumberEditSuccess = true;
}

function onAbaRoutingNumberNumericFailure(e, callbackParams) {
  var alertType = callbackParams[0];
  showIconForFailure('${loanFields.ABA_ROUTING_NUMBER.fieldName}');
  isAbaRoutingNumberNumericSuccess = false;
  isAbaRoutingNumberEditSuccess = false;
  if (alertType == 'alert') {
    var errorMessage = callbackParams[1];
    alert(errorMessage);
  }
}

function onAbaRoutingNumberLengthSuccess(e, callbackParams) {
  // Only if the previous AbaRoutingNumberNumeric edit was successful do we
  // want to hide the error icon.  If it failed, we don't want to overwrite
  // the error icon already displayed. 
  if (isAbaRoutingNumberEditSuccess) {
    showIconForSuccess('${loanFields.ABA_ROUTING_NUMBER.fieldName}');
  }
}

function onAbaRoutingNumberLengthFailure(e, callbackParams) {
  var alertType = callbackParams[0];
  showIconForFailure('${loanFields.ABA_ROUTING_NUMBER.fieldName}');
  isAbaRoutingNumberEditSuccess = false;

  if (alertType == 'alert') {
    // Only show the error msg if the AbaRoutingNumberNumeric edit was successful.
    if (isAbaRoutingNumberNumericSuccess) {
      alert(callbackParams[1]);
    }
  }
}

function onAccountNumberInvalidCharacterSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.ACCOUNT_NUMBER.fieldName}');
}

function onAccountNumberInvalidCharacterFailure(e, callbackParams) {
  var alertType = callbackParams[0];
  showIconForFailure('${loanFields.ACCOUNT_NUMBER.fieldName}');
  if (alertType == 'alert') {
    var errorMessage = callbackParams[1];
    var invalidCharsList = callbackParams[3];
    alert(errorMessage.replace(/\{0\}/, invalidCharsList));
  }

  // Clear the invalid character entry from the end of the callbackParms array,
  // otherwise the array keeps growing on subsequent passes through calls to 
  // pageValidator.registerAllowedCharacters.
  callbackParams.length = callbackParams.length - 1;
}

function showOrHideStateAndZip() {
  var country = $('#country').val();
  var errorIcon = $('#errorIcon_${loanFields.STATE.fieldName}');
  var dynamicErrorIcon = $('#dynamicErrorIcon_${loanFields.STATE.fieldName}');
  if (country == 'USA' || country == '') {
    /*
     * State code
     */
    $('#stateNonUSA').hide();
    $('#stateUSA').show();
    if (errorIcon.length == 0) {
      errorIcon = dynamicErrorIcon;
    }

  	/*
  	 * Zip code
  	 */
    $('#zipCodeNonUSA').hide();
    $('#zipCodeUSA').show(); 
  } else {
    /*
     * State code
     */
    $('#stateUSA').hide();
    $('#stateNonUSA').show();

  	/*
  	 * Zip code
  	 */
    $('#zipCodeUSA').hide(); 
    $('#zipCodeNonUSA').show();
  }
  if (isFirstDisplayOfPage) {
    isFirstDisplayOfPage = false;
  } else {
    showIconForSuccess('${loanFields.STATE.fieldName}');
    showIconForSuccess('${loanFields.ZIP_CODE.fieldName}');
  }
}

function showOrHideBankInformation() {
  var paymentMethodCode;
  if (${displayRules.showBankInformationAsEditable}) {
    paymentMethodCode = $("input[name='paymentMethod']:checked").val();
  } else {
    paymentMethodCode = "${e:forJavaScriptBlock(loanForm.paymentMethod)}";
  }

  // If payment method = check, hide the Bank information section rows
  if (paymentMethodCode == '${payeeConstants.CHECK_PAYMENT_METHOD_CODE}') {
    $('#accountTypeRow').hide();
    $('#bankInfoTitleRow').hide();
    $('#abaRoutingNumberRow').hide();
    $('#bankNameRow').hide();
    $('#accountNumberRow').hide();
  } else {
    $('#bankInfoTitleRow').show();
    $('#abaRoutingNumberRow').show();
    $('#bankNameRow').show();
    $('#accountNumberRow').show();
    if (paymentMethodCode == '${payeeConstants.WIRE_PAYMENT_METHOD_CODE}') {
      $('#accountTypeRow').hide();
    } else {
      $('#accountTypeRow').show();
    }
  }
}

function onPaymentMethodChange() {
  showOrHideBankInformation();
  $('#abaRoutingNumber').val("");
  $('#bankName').val("");
  $('#bankNameSpan').text("");
}

function onAbaRoutingNumberChange() {
  $('#bankName').val("");
  $('#bankNameSpan').text("");
}

function bankLookupDialogSelect(dialog) {
  var selectedRadio = $("input[name='bank']:checked").val();
  var errorMessage = "";
  var bankName = "";
  
  if (typeof(selectedRadio) == 'undefined') {
    errorMessage += "<content:getAttribute escapeJavaScript="true" attribute="text" beanName="bankLookupBankSelectionRequired"/><br/>";
  }

  if (selectedRadio == 'OTHER') {
    var name = $("#bankNameOtherInput").val();
    if (name.trim().length == 0) {
      errorMessage += "<content:getAttribute escapeJavaScript="true" attribute="text" beanName="bankLookupBankNameRequired"/><br/>";
    } else {
      var invalidCharacter = "";
      for (var i = 0; i < name.length; i++) {
        var c = name.charAt(i);
        if (c == '"' || c == '&' || c == '<') {
          invalidCharacter += c;
        }
      }
      var strippedValue = name.replace(PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, '')
      if (strippedValue.length > 0) {
        invalidCharacter += strippedValue;
      }
      if (invalidCharacter.length > 0) {
        var invalidCharacterMessage = '<content:getAttribute escapeJavaScript="true" beanName="bankLookupBankNameInvalidCharacter" attribute="text"/>';
        invalidCharacterMessage = invalidCharacterMessage.replace(/\{0\}/, invalidCharacter);
        errorMessage += invalidCharacterMessage;
      } else {
        bankName = name.trim();
      }
    }
  } else {
    bankName = selectedRadio;
  }
  
  if (errorMessage.length > 0) {
    $("#bankSelectionErrorMsg").html(errorMessage);
    return false;
  }

  $("#bankName").val(bankName);
  $("#bankNameSpan").text(bankName);

  YAHOO.johnhancock.psw.bankLookupDialog.hide();
}

function bankLookup(abaNumber) {
  $("#bankSelectionErrorMsg").html('');

  if (isNaN(parseInt(numberUtils.deformatNumber(abaNumber)))) {
    return false;
  }
  if (abaNumber.length != ${loanConstants.ABA_ROUTING_NUMBER_LENGTH}) {
    return false;
  }

  var bankName = $("#bankName").val();

  var bankLookupCallback = { 
    cache:false,
    success: function(response) {
      /*success handler code*/
      var result = response.responseText;
      if (prepareBankLookupDialog(result, response.argument)) {
        YAHOO.johnhancock.psw.bankLookupDialog.cfg.setProperty("context", ["selectBankNameLink", "bl", "br"]); 
        YAHOO.johnhancock.psw.bankLookupDialog.show();
      }
    }, 
    failure: function(response) {
      alert("Failed to lookup ABA number. Please contact the system administrator");
    }, 
    argument: bankName 
  };

  var paymentMethodCode = $("input[name='paymentMethod']:checked").val();
  var transaction = YAHOO.util.Connect.asyncRequest(
    'GET', '/do/onlineloans/bankLookup/?action=lookup&abaRoutingNumber=' + abaNumber + '&paymentMethodCode=' + paymentMethodCode,
    bankLookupCallback);

  return false;  
}

YAHOO.util.Event.onDOMReady(function () { 

  showOrHideStateAndZip();
  showOrHideBankInformation();

  YAHOO.johnhancock.psw.bankLookupDialog = new YAHOO.widget.Panel("BankLookupDialog", {
    constraintoviewport: true,
    width: "260px",
    modal: true,
    close: false,
    underlay: "none",  
    zIndex: 4,
    visible:false} );
  YAHOO.johnhancock.psw.bankLookupDialog.render(document.body);
  YAHOO.util.Event.addListener("bankLookupDialogCancel", "click", YAHOO.johnhancock.psw.bankLookupDialog.hide, YAHOO.johnhancock.psw.bankLookupDialog, true);
  YAHOO.util.Event.addListener("bankLookupDialogSelect", "click", bankLookupDialogSelect);

});

$(document).ready(function() {

  // NOTE: ORDER OF THE FOLLOWING pageValidator.register... STATEMENTS IS IMPORTANT!

  pageValidator.registerAllowedCharacters('addressLine1',
    'keyup', PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, 
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="addressLine1InvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars', '${loanFields.ADDRESS_LINE1.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('addressLine1',
    'keyup', PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP,  
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="addressLine1InvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ElectronicPaymentAllowedChars', '${loanFields.ADDRESS_LINE1.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('addressLine1',
    'blur', PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, 
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="addressLine1InvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars', '${loanFields.ADDRESS_LINE1.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('addressLine1',
    'blur', PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP,  
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="addressLine1InvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ElectronicPaymentAllowedChars', '${loanFields.ADDRESS_LINE1.fieldName}'
    ]);

  pageValidator.registerAllowedCharacters('addressLine2',
    'keyup', PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, 
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="addressLine2InvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars', '${loanFields.ADDRESS_LINE2.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('addressLine2',
    'keyup', PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP,  
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="addressLine2InvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ElectronicPaymentAllowedChars', '${loanFields.ADDRESS_LINE2.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('addressLine2',
    'blur', PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, 
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="addressLine2InvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars', '${loanFields.ADDRESS_LINE2.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('addressLine2',
    'blur', PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP,  
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="addressLine2InvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ElectronicPaymentAllowedChars', '${loanFields.ADDRESS_LINE2.fieldName}'
    ]);

  pageValidator.registerAllowedCharacters('city',
    'keyup', PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, 
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="cityInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars', '${loanFields.CITY.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('city',
    'keyup', PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP,  
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="cityInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ElectronicPaymentAllowedChars', '${loanFields.CITY.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('city',
    'blur', PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, 
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="cityInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars', '${loanFields.CITY.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('city',
    'blur', PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP,  
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="cityInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ElectronicPaymentAllowedChars', '${loanFields.CITY.fieldName}'
    ]);

  pageValidator.registerAllowedCharacters('stateNonUSA',
    'keyup', PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, 
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="stateInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars', '${loanFields.STATE.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('stateNonUSA',
    'keyup', PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP,  
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="stateInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ElectronicPaymentAllowedChars', '${loanFields.STATE.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('stateNonUSA',
    'blur', PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, 
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="stateInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars', '${loanFields.STATE.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('stateNonUSA',
    'blur', PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP,  
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="stateInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ElectronicPaymentAllowedChars', '${loanFields.STATE.fieldName}'
    ]);

  pageValidator.registerPositiveWholeNumber('zipCode1',
    'keyup', onZipCodeInvalidUSFormatSuccess, onZipCodeInvalidUSFormatFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="zipCodeInvalidUSFormatText" escapeJavaScript="true"></content:getAttribute>',
    'positiveWholeNumber'
    ]);
  pageValidator.registerPositiveWholeNumber('zipCode1',
    'blur', onZipCodeInvalidUSFormatSuccess, onZipCodeInvalidUSFormatFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="zipCodeInvalidUSFormatText" escapeJavaScript="true"></content:getAttribute>',
    'positiveWholeNumber'
    ]);

  pageValidator.registerAllowedCharacters('zipCodeNonUSA',
    'keyup', PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, 
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="zipCodeInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars', '${loanFields.ZIP_CODE.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('zipCodeNonUSA',
    'keyup', PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP,  
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="zipCodeInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ElectronicPaymentAllowedChars', '${loanFields.ZIP_CODE.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('zipCodeNonUSA',
    'blur', PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP, 
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="zipCodeInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars', '${loanFields.ZIP_CODE.fieldName}'
    ]);
  pageValidator.registerAllowedCharacters('zipCodeNonUSA',
    'blur', PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP,  
    onInvalidCharacterSuccess, onInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="zipCodeInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ElectronicPaymentAllowedChars', '${loanFields.ZIP_CODE.fieldName}'
    ]);

  pageValidator.registerPositiveWholeNumber('zipCode2',
    'keyup', onZipCodeInvalidUSFormatSuccess, onZipCodeInvalidUSFormatFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="zipCodeInvalidUSFormatText" escapeJavaScript="true"></content:getAttribute>',
    'positiveWholeNumber'
    ]);
  pageValidator.registerPositiveWholeNumber('zipCode2',
    'blur', onZipCodeInvalidUSFormatSuccess, onZipCodeInvalidUSFormatFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="zipCodeInvalidUSFormatText" escapeJavaScript="true"></content:getAttribute>',
    'positiveWholeNumber'
    ]);
  pageValidator.registerLength('zipCode2',
    'blur', ${loanConstants.ZIP_CODE_2_LENGTH}, true,
    onZipCodeInvalidUSFormatSuccess, onZipCodeInvalidUSFormatFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="zipCodeInvalidUSFormatText" escapeJavaScript="true"></content:getAttribute>',
    'lengthCheck'
    ]);

  pageValidator.registerAllowedCharacters('abaRoutingNumber',
    'keyup', PageValidator.NUMERIC_CHARACTER_ONLY_REGEXP, 
    onAbaRoutingNumberNumericSuccess, onAbaRoutingNumberNumericFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="abaRoutingNumberNonNumericText" escapeJavaScript="true"></content:getAttribute>',
    'invalidCharCheck'
    ]);
  pageValidator.registerAllowedCharacters('abaRoutingNumber',
    'blur', PageValidator.NUMERIC_CHARACTER_ONLY_REGEXP, 
    onAbaRoutingNumberNumericSuccess, onAbaRoutingNumberNumericFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="abaRoutingNumberNonNumericText" escapeJavaScript="true"></content:getAttribute>',
    'invalidCharCheck'
    ]);
  pageValidator.registerLength('abaRoutingNumber',
    'blur', ${loanConstants.ABA_ROUTING_NUMBER_LENGTH}, true, 
    onAbaRoutingNumberLengthSuccess, onAbaRoutingNumberLengthFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="abaRoutingNumberNonNumericText" escapeJavaScript="true"></content:getAttribute>',
    'lengthCheck'
    ]);

  pageValidator.registerAllowedCharacters('accountNumber',
    'keyup', PageValidator.APOLLO_ALLOWED_NON_SPECIAL_CHARACTERS_REGEXP, 
    onAccountNumberInvalidCharacterSuccess, onAccountNumberInvalidCharacterFailure, 
    ['noalert', 
    '<content:getAttribute attribute="text" beanName="accountNumberInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars' 
    ]);
  pageValidator.registerAllowedCharacters('accountNumber',
    'blur', PageValidator.APOLLO_ALLOWED_NON_SPECIAL_CHARACTERS_REGEXP, 
    onAccountNumberInvalidCharacterSuccess, onAccountNumberInvalidCharacterFailure, 
    ['alert', 
    '<content:getAttribute attribute="text" beanName="accountNumberInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>',
    'ApolloAllowedChars'
    ]);

});

</script>

        <table class="box" border="0" cellpadding="0" cellspacing="0" width="734">
          <tr>
            <td class="tableheadTD1">
              <b>
              <c:if test="${displayRules.displayExpandCollapseButton}">
              <img id="paymentInformationSectionExpandCollapseIcon"
                   src="/assets/unmanaged/images/minus_icon.gif" width="13" height="13"
                   style="cursor:hand; cursor:pointer">&nbsp;
              </c:if>
              <content:getAttribute attribute="text" beanName="paymentInstructionsSectionTitleText">
              </content:getAttribute>
              </b>
              </td>
            <td width="141" class="tablehead">&nbsp;</td>
          </tr>
        </table>
        <div id="paymentInformationSection">
          <table class="box" border="0" cellpadding="0" cellspacing="0" width="734">

            <tr>
              <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td width="732"><table border="0" cellpadding="0" cellspacing="0" width="733">
                  <tr valign="top">
                    <td width="186" class="datacell1">
                      <ps:fieldHilight styleIdSuffix="${loanFields.PAYMENT_METHOD.fieldName}"
                                       name="${loanFields.PAYMENT_METHOD.fieldName}" 
                                       displayActivityHistory="${displayRules.showPaymentMethodActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>Payment method </strong>
                    </td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td width="546" class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showAddressDataAsEditable}">
                          <ps:trackChanges escape="true" property="paymentMethod" name="loanForm" />
<form:radiobutton onclick="onPaymentMethodChange()" path="paymentMethod" value="${payeeConstants.ACH_PAYMENT_METHOD_CODE}"/>

                            <c:out value="${paymentMethods[payeeConstants.ACH_PAYMENT_METHOD_CODE]}"/>

<form:radiobutton onclick="onPaymentMethodChange()" path="paymentMethod" value="${payeeConstants.CHECK_PAYMENT_METHOD_CODE}"/>

                            <c:out value="${paymentMethods[payeeConstants.CHECK_PAYMENT_METHOD_CODE]}"/>

<form:radiobutton onclick="onPaymentMethodChange()" path="paymentMethod" value="${payeeConstants.WIRE_PAYMENT_METHOD_CODE}"/>

                            <c:out value="${paymentMethods[payeeConstants.WIRE_PAYMENT_METHOD_CODE]}"/>

                        </c:when>
                        <c:otherwise>
                          <c:out value="${paymentMethods[loan.recipient.payees[0].paymentMethodCode]}"/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                  <tr valign="top">
                    <td class="datacell1">&nbsp;</td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">
                      <content:getAttribute attribute="text" beanName="wireChargesContentText">
                      </content:getAttribute>
                    </td>
                  </tr>
                  <tr valign="top">
                    <td class="tablesubhead"><b>Payee information</b> </td>
                    <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="tablesubhead">&nbsp;</td>
                  </tr>
                  <tr valign="top">
                    <td class="datacell1"><strong>&nbsp;&nbsp;Name</strong></td>

                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">${loanParticipantData.firstName} ${loanParticipantData.middleInitial} ${loanParticipantData.lastName}</td>
                  </tr>
                  <tr valign="top">
                    <td class="datacell1">
                      <img id="dynamicErrorIcon_${loanFields.ADDRESS_LINE1.fieldName}"
                            src="/assets/unmanaged/images/error.gif"
                            style="display:none"/>
                      <ps:fieldHilight styleIdSuffix="${loanFields.ADDRESS_LINE1.fieldName}"
                                       name="${loanFields.ADDRESS_LINE1.fieldName}" 
                                       displayActivityHistory="${displayRules.showAddressLine1ActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>&nbsp;&nbsp;Address line 1 </strong>
                    </td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showAddressDataAsEditable}">
                          <ps:trackChanges escape="true" property="addressLine1" name="loanForm"/>
<form:input path="addressLine1" maxlength="30" size="35" cssClass="${displayRules.addressDataStyleClass}" id="addressLine1"/>


                        </c:when>
                        <c:otherwise>
                          <c:out value="${loan.recipient.address.addressLine1}"/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>

                  <tr valign="top">
                    <td class="datacell1">
                      <img id="dynamicErrorIcon_${loanFields.ADDRESS_LINE2.fieldName}"
                            src="/assets/unmanaged/images/error.gif"
                            style="display:none"/>
                      <ps:fieldHilight styleIdSuffix="${loanFields.ADDRESS_LINE2.fieldName}"
                                       name="${loanFields.ADDRESS_LINE2.fieldName}"
                                       displayActivityHistory="${displayRules.showAddressLine2ActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>&nbsp;&nbsp;Address line 2 </strong>
                    </td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showAddressDataAsEditable}">
                          <ps:trackChanges escape="true" property="addressLine2"  name="loanForm"/>
<form:input path="addressLine2" maxlength="30" size="35" id="addressLine2"/>

                        </c:when>
                        <c:otherwise>
                          <c:out value="${loan.recipient.address.addressLine2}"/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                  <tr valign="top">
                    <td class="datacell1">
                      <img id="dynamicErrorIcon_${loanFields.CITY.fieldName}"
                            src="/assets/unmanaged/images/error.gif"
                            style="display:none"/>
                      <ps:fieldHilight styleIdSuffix="${loanFields.CITY.fieldName}"
                                       name="${loanFields.CITY.fieldName}"
                                       displayActivityHistory="${displayRules.showCityActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>&nbsp;&nbsp;City</strong>
                    </td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showAddressDataAsEditable}">
                          <ps:trackChanges escape="true" property="city" name="loanForm"/>
<form:input path="city" maxlength="25" size="30" cssClass="${displayRules.addressDataStyleClass}" id="city"/>


                        </c:when>
                        <c:otherwise>
                          <c:out value="${loan.recipient.address.city}"/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>

                  <tr valign="top">
                    <td class="datacell1">
                      <img id="dynamicErrorIcon_${loanFields.STATE.fieldName}"
                            src="/assets/unmanaged/images/error.gif"
                            style="display:none"/>
                      <ps:fieldHilight styleIdSuffix="${loanFields.STATE.fieldName}"
                                       name="${loanFields.STATE.fieldName}" 
                                       displayActivityHistory="${displayRules.showStateActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>&nbsp;&nbsp;State</strong>
                    </td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showAddressDataAsEditable}">
                        <c:forEach items="${lookupData}" var="lookUpMap">
                        <c:if test="${lookUpMap.key=='states'}">
                        	<c:set var="states" value="${lookUpMap.value}"/>
                        </c:if>
                        	
                        </c:forEach>
                          <ps:trackChanges escape="true" property="stateCode.stateUSA" name="loanForm"/>
 <form:select styleId="stateUSA" cssClass="${displayRules.addressDataStyleClass}" path="stateCode.stateUSA">

<form:options items="${states}"/>  
 </form:select>
                          <ps:trackChanges escape="true" property="stateCode.stateNonUSA" name="loanForm"/>
<form:input path="stateCode.stateNonUSA" maxlength="2" size="2" cssClass="${displayRules.addressDataStyleClass}" id="stateNonUSA"/>


                        </c:when>
                        <c:otherwise>
                          <c:out value="${displayRules.stateName}"/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>

                  <tr valign="top">
                    <td class="datacell1">
                      <ps:fieldHilight styleIdSuffix="${loanFields.COUNTRY.fieldName}"
                                       name="${loanFields.COUNTRY.fieldName}" 
                                       displayActivityHistory="${displayRules.showCountryActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>&nbsp;&nbsp;Country</strong>
                    </td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showAddressDataAsEditable}">
                          <ps:trackChanges escape="true" property="country" name="loanForm"/>
                           <c:forEach items="${lookupData}" var="lookUpMap">
                        <c:if test="${lookUpMap.key=='countries'}">
                        	<c:set var="countries" value="${lookUpMap.value}"/>
                        </c:if>
                        	
                        </c:forEach>
 <form:select styleId="country" cssClass="${displayRules.addressDataStyleClass}" path="country" onchange="showOrHideStateAndZip()">
 <form:options items="${countries}"/>
</form:select>
                        </c:when>
                        <c:otherwise>
                          <c:out value="${displayRules.countryName}"/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                  <tr valign="top">
                    <td class="datacell1">
                      <img id="dynamicErrorIcon_${loanFields.ZIP_CODE.fieldName}"
                              src="/assets/unmanaged/images/error.gif"
                              style="display:none"/>
                      <ps:fieldHilight styleIdSuffix="${loanFields.ZIP_CODE.fieldName}"
                                       name="${loanFields.ZIP_CODE.fieldName}" 
                                       displayActivityHistory="${displayRules.showZipCodeActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>&nbsp;&nbsp;Zip code </strong>
                    </td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showAddressDataAsEditable}">
                          <span id="zipCodeUSA">
                           <ps:trackChanges escape="true" property="zipCode.zipCode1" name="loanForm"/>
<form:input path="zipCode.zipCode1" maxlength="5" size="5" cssStyle="{text-align: right}" cssClass="${displayRules.addressDataStyleClass}" id="zipCode1"/> -



                           <ps:trackChanges escape="true" property="zipCode.zipCode2" name="loanForm"/>
<form:input path="zipCode.zipCode2" maxlength="4" size="4" cssStyle="{text-align: right}" cssClass="${displayRules.addressDataStyleClass}" id="zipCode2"/>



                          </span>
                           <ps:trackChanges escape="true" property="zipCode.zipCodeNonUSA" name="loanForm"/>
<form:input path="zipCode.zipCodeNonUSA" maxlength="9" size="9" id="zipCodeNonUSA"/>


                        </c:when>
                        <c:otherwise>
                          <c:if test="${displayRules.countryUSA}">
                            <c:if test="${not empty loan.recipient.address.zipCode}">
                              <render:zip property="loan.recipient.address.zipCode"/>
                            </c:if>
                            <c:if test="${empty loan.recipient.address.zipCode}">
                              <c:out value="${loan.recipient.address.zipCode}"/>
                            </c:if>
                          </c:if>
                          <c:if test="${not displayRules.countryUSA}">
                            <c:out value="${loan.recipient.address.zipCode}"/>
                          </c:if>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>

                  <tr valign="top" id="bankInfoTitleRow">
                    <td class="tablesubhead"><b>Bank information </b></td>
                    <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="tablesubhead">&nbsp;</td>
                  </tr>
                  <tr valign="top" id="abaRoutingNumberRow">
                    <td class="datacell1">
                      <img id="dynamicErrorIcon_${loanFields.ABA_ROUTING_NUMBER.fieldName}"
                              src="/assets/unmanaged/images/error.gif"
                              style="display:none"/>
                      <ps:fieldHilight styleIdSuffix="${loanFields.ABA_ROUTING_NUMBER.fieldName}"
                                       name="${loanFields.ABA_ROUTING_NUMBER.fieldName}" 
                                       displayActivityHistory="${displayRules.showAbaRoutingNumberActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>&nbsp;&nbsp;ABA/Routing number </strong>
                    </td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showBankInformationAsEditable}">
                          <ps:trackChanges escape="true" property="abaRoutingNumber" name="loanForm"/>
<form:input path="abaRoutingNumber" maxlength="9" onchange="onAbaRoutingNumberChange()" size="9" cssClass="${displayRules.bankInformationStyleClass}" id="abaRoutingNumber"/>




                          <a href="#" id="selectBankNameLink" onclick="bankLookup($('#abaRoutingNumber').val()); return false">
                            <content:getAttribute attribute="text" beanName="bankLookupLinkText">
                            </content:getAttribute>
                          </a>
                        </c:when>
                        <c:otherwise>
                          <c:out value="${displayRules.abaRountingNumber}"/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                  <tr valign="top" id="bankNameRow">
                    <td class="datacell1">
                      <ps:fieldHilight styleIdSuffix="${loanFields.BANK_NAME.fieldName}" 
                                       name="${loanFields.BANK_NAME.fieldName}" 
                                       displayActivityHistory="${displayRules.showBankNameActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>&nbsp;&nbsp;Bank name </strong>
                    </td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

                    <td class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showBankInformationAsEditable}">
                          <ps:trackChanges escape="true" property="bankName" name="loanForm"/>
<form:hidden path="bankName" id="bankName"/>
                          <span id="bankNameSpan">${e:forHtmlContent(loanForm.bankName)}</span>
                        </c:when>
                        <c:otherwise>
                          <c:out value="${loan.recipient.payees[0].paymentInstruction.bankName}"/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                  <tr valign="top" id="accountNumberRow">
                    <td class="datacell1">
                      <img id="dynamicErrorIcon_${loanFields.ACCOUNT_NUMBER.fieldName}"
                            src="/assets/unmanaged/images/error.gif"
                            style="display:none"/>
                      <ps:fieldHilight styleIdSuffix="${loanFields.ACCOUNT_NUMBER.fieldName}"
                                       name="${loanFields.ACCOUNT_NUMBER.fieldName}"
                                       displayActivityHistory="${displayRules.showAccountNumberActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>&nbsp;&nbsp;Account number </strong>
                    </td>
                    <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showBankInformationAsEditable &&
                        				!displayRules.showMaskedAccountNumber}">
                          <ps:trackChanges escape="true" property="accountNumber" name="loanForm"/>
<form:input path="accountNumber" maxlength="17" size="17" cssClass="${displayRules.bankInformationStyleClass}" id="accountNumber"/>


                        </c:when>
                        <c:when test="${!displayRules.showBankInformationAsEditable &&
                        				 displayRules.showMaskedAccountNumber}">
							<c:out value="${commonConstants.MASK_ACCOUNT_NUMBER}"/>
                        </c:when>
                        <c:otherwise>
                        	<c:out value="${loan.recipient.payees[0].paymentInstruction.bankAccountNumber}"/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                  <tr valign="top" id="accountTypeRow">
                    <td class="datacell1">
                      <ps:fieldHilight styleIdSuffix="${loanFields.ACCOUNT_TYPE.fieldName}"
                                       name="${loanFields.ACCOUNT_TYPE.fieldName}"
                                       displayActivityHistory="${displayRules.showAccountTypeActivityHistoryIcon}"
                                       singleDisplay="true"/>
                      <strong>&nbsp;&nbsp;Account type </strong>
                    </td>
                    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="datacell1">
                      <c:choose>
                        <c:when test="${displayRules.showBankInformationAsEditable}">
                          <ps:trackChanges escape="true" property="accountType" name="loanForm"/>
<form:radiobutton path="accountType" cssClass="${displayRules.bankInformationStyleClass}" value="${paymentInstructionConstants.BANK_ACCOUNT_TYPE_CHECKING}"/>

                              <c:out value="${bankAccountTypes[paymentInstructionConstants.BANK_ACCOUNT_TYPE_CHECKING]}"/>

<form:radiobutton path="accountType" cssClass="${displayRules.bankInformationStyleClass}" value="${paymentInstructionConstants.BANK_ACCOUNT_TYPE_SAVING}"/>

                              <c:out value="${bankAccountTypes[paymentInstructionConstants.BANK_ACCOUNT_TYPE_SAVING]}"/>

                        </c:when>
                        <c:otherwise>
                          <c:out value="${bankAccountTypes[loan.recipient.payees[0].paymentInstruction.bankAccountTypeCode]}"/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
              </table></td>
              <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </tr>

            <tr>
              <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td colspan="1" class="datacell1">
                <content:getAttribute attribute="text" beanName="paymentInstructionsFooterText">
                </content:getAttribute>
              </td>
              <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </tr>

            <tr>
              <td colspan="3" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </tr>
          </table>
        </div>
