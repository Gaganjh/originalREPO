/***************************************************************************************************************************************
  * OTPModalPopup - One Time Passcode Modal Popup
  *
  *   DO NOT INSTANTIATE THIS CLASS DIRECTLY!
  *
  *   This class encapsultes all the html, events, and ajax calls for the pop up. 
  *   This is instantiated ONCE per submit button that has the OTPInterceptorPlugin assigned to.
  *
  *   To create an instance of the pop up for a <input id="mySubmit" type="submit"/> button, you would do the following:
  *       $("#mySubmit").OTPInterceptorPlugin(formInterceptorOptions, popupOptions);
  *   where:
  *     formInterceptorOptions, contains a JSON set of (at least) all the mandatory options required by OTPInterceptorPlugin class (further below).
  *   and 
  *     popupOptions, contains a JSON set of (at least) all the mandatory options required by OTPModalPopup class (just below). 
  *                   You may override also override the defaults for the pop up with these options.
  *
  ***************************************************************************************************************************************/
  
/**
 * OTPModalPopup Constructor:
 *    ajaxURISendOTPCode - [mandatory] The URL or preferrably URI to your local action to send a one time passcode to the user.
 *                                     Your local action must process this AJAX request and call a common web service that handles 
 *                                     sending the OTP code for RPS.
 *                                     Your local action must provide all the required data to that webservice.
 *                                     The SMS or Email provided via maskedContactMethod is masked, so it will not be passed in.
 *                                     No data is sent other than the code entered data you will receive via AJAX POST:  
 *                                          {otpCode : 123456}.                                     
 *                                     The AJAX must return a simple { success : [true/false] }
 *                                     Where true = the code was sent successfully.
 *                                           false= some error occurred preventing the code from being sent.
 *                                     This AJAX method must also result in HTML status=200  (request/response succeeded)
 *
 *    ajaxURISendValidateCode - [mandatory] The URL or preferrably URI to your local action to validate the code entered is correct.
 *
 *    maskedContactMethod  - [mandatory] The masked email or phone number for display use only.  (Back-end handles the actual send.)
 *
 *    codeLength            - [optional] [default=6] Used to validate that the user entered enough characters for 
 *                            the code or an error will show.
 *
 *    All other options are [optional] and are the default text that appears in the popup.  
 *    All defaults may be overridden by passing them into the constructor.
**/

// Normal = normal.
// Demo = Plugin can operate without a back end.
// Passthrough = Plugin can be in place, but not pop up.  Requires back end to also be in passthrough mode

var otpOperationModes = { normal : "N", demo : "D", passthrough : "P" };

/**
  * The OTP Pop Up 
  * @constructor 
  **/
var OTPModalPopup = function(interceptor, options) {
  var otpPopup = this;  
  var defaults = {
    mode : otpOperationModes.normal,
    ajaxURISendOTPCode : undefined,
    ajaxURISendValidateCode : undefined,
    allowChangeContactMethod : true,
    useDropDownSelector : false,
    contactMethods : [],
    chosenContactMethodIndex : 0,
    codeLength : 20,
    title :           "Account Verification Required",
    intro :           "To complete this process, you will be sent a security code to verify you are the account holder.",
    codeWillBeSentTo: "The security code will be sent to:",
    sendCode :        "Send Code",
    sendingCode :     "Sending...",
    resendMessage :   "A security code was sent to the location noted.<br/>If it fails to arrive, ",
    resendLink :      "click here to resend code",
    disclaimer:       "<b>Important:</b> If you no longer have access to any of the options above, you must contact the John Hancock Participant Services Center at 1-800-395-1113.",
    enterCode :       "Enter code",
    validatingCode :  "Validating...",
    submit :          "Submit",    
    systemErrorJSON:  "ERROR_SYSTEM_ERROR",
    codeExpiredJSON:  "ERROR_CODE_EXPIRED",
    maxAttemptsJSON:  "ERROR_MAX_ATTEMPTS",
    invalidCodeJSON:  "ERROR_CODE_MISMATCH",
    systemError:      "We're sorry. We are currently experiencing technical difficulties. Any unconfirmed changes to your account will not have been saved. Please try again later, or call our toll-free service line at 1-800-395-1113 to speak to a Participant Service Representative between 8:00 a.m. and 8:00 p.m. ET, Monday to Friday.",    
    codeExpiredError: "The security code entered has expired.<br/>Please click the 'resend code' link above.",
    maxAttemptsError: "You have exceeded the maximum attempts allowed.<br/>Please click the 'resend code' link to get a new one.",
    invalidCodeError: "Invalid security code.",    
    missingContactsError: "We're sorry. There is no email address or phone number to contact you to provide you with a one time passcode.  Please contact a service representative for assistance.",
    codeFormatHelper:  "",
    contactMethodPhoneSelector: "<img class='otp-method-icon' src='/images/Mobile_30px_black.svg' style='width:20px;'/>&nbsp;&nbsp;",
    contactMethodEmailSelector: "<img class='otp-method-icon' src='/images/Email_30px_black.svg' style='width:20px;'/>&nbsp;&nbsp;"
  };    
  otpPopup.interceptor = interceptor;  // A handle to the parent object.
  otpPopup.settings = $.extend({}, defaults, options);  // Override defaults, with options, place in otpPopup.settings
  otpPopup.appendOTPPopUpHtml(); // Create the HTML for this interceptor popup.
};

/**
 * showOtpPopup
 *    This is the function called from the submit buttons' plugins.
 *    This will display the OTP Popup to the user.
**/
OTPModalPopup.prototype.show = function($submitBtn) {
  var otpPopup = this;
  var s = otpPopup.settings;
  
  if (s.contactMethods.length <= 0) {
    otpPopup.setError(otpPopup.settings.missingContactsError);
    $(".otp-send-code", otpPopup.$otpModalPopup).prop("disabled", true);
  } else {
    $(".otp-send-code", otpPopup.$otpModalPopup).prop("disabled", false);
  }
  
  otpPopup.$otpModalPopup.addClass("show")
  otpPopup.$otpModalPopup.show();
  $("#otpModalBackdrop").addClass("show");
  $("#otpModalBackdrop").show();
};

/**
 * setError:
 *    Sets the error messsage and displays the error div it is held in.
**/
OTPModalPopup.prototype.setError = function(errorText) {
  var otpPopup = this;
  var $errorDiv = $(".otp-error-text", otpPopup.$popUpContent);
  var $passcode = $(".otp-passcode", otpPopup.$popUpContent);
  $passcode.addClass("jh-error");
  $errorDiv.html(errorText);
  $errorDiv.show();      
};

/**
 * clearError:
 *    Clears the error messsage and hides the error div it is held in.
**/    
OTPModalPopup.prototype.clearError = function() {
  var otpPopup = this;
  var $errorDiv = $(".otp-error-text", otpPopup.$popUpContent);
  var $passcode = $(".otp-passcode", otpPopup.$popUpContent);
  $passcode.removeClass("jh-error");
  $errorDiv.html("");
  $errorDiv.hide();      
};

OTPModalPopup.prototype.buildSingleContactHtml = function(contactIndex, plainText) {
  var otpPopup = this;
  var s = otpPopup.settings;
  var commPref = s.contactMethods[contactIndex];  
  var commPrefText = s.contactMethodPhoneSelector;
  var html = "";
  
  if (commPref.type != undefined && commPref.type.toLowerCase() == "phone") {
    commPrefText = s.contactMethodPhoneSelector;
  } else if (commPref.type != undefined && commPref.type.toLowerCase() == "email") {
    commPrefText = s.contactMethodEmailSelector;
  } else {
    commPrefText = "";
  }
  if (plainText === true) {
    html += commPrefText + commPref.address;  
  } else {
    html += "<span class='label'>" + commPrefText + "</span> " + commPref.address;  
  }
  return html;
};

OTPModalPopup.prototype.buildContactMethodsHTML = function() {
  var otpPopup = this;
  var s = otpPopup.settings;
  var html = "";  
  var $selector = undefined;
  if (s.useDropDownSelector ===true) {
    $selector = $("<div class='select-items select-hide'></div>");
  } else {
    $selector = $("<div class='otp-radio-group'></div>");
  }
  if (s.contactMethods.length > 0) {
    if (s.allowChangeContactMethod === true && s.contactMethods.length >= 2) {
      for (var i=0; i < s.contactMethods.length; i++) {
        var addr = s.contactMethods[i].address;
        var type = s.contactMethods[i].type;
        var isSelected = false;
        if (i === s.chosenContactMethodIndex) {
          isSelected = true;
        }
        var optionHtml = otpPopup.buildSingleContactHtml(i, true);
        if (s.useDropDownSelector === true) {
          $("<div class='otp-method otp-method-"+i+"' data-method-index='" + i + "'>" + optionHtml +"</div>").appendTo($selector);
        } else {
          $("<input type='radio' class='radio otp-method-radio otp-method-radio-"+i+"' id='"+otpPopup.identifierClass+"_otp_method_radio_"+i+"' data-method-index='" + i + "'><label class='radio-label' for='"+otpPopup.identifierClass+"_otp_method_radio_"+i+"'>" + optionHtml + "</label></br>").appendTo($selector);
        }
      }
      if (s.useDropDownSelector === true) {
        html = "<div class='jh-dropdown otp-select-method' style='min-width:400px'><div class='select-selected otp-selected-contact'>&nbsp;</div>" + $selector[0].outerHTML + "</div>";
      } else {
        html = $selector[0].outerHTML;
      }
    } else {
      // Single display only, no selector:
      html +=  "<div class='otp-only-one-method'>" + otpPopup.buildSingleContactHtml(0, false) + "</div>";
    }
  } else {
    // ERROR:  There always has to be at least 1 contact method in the contactMethods array. 
  }
  return html;  
};

/**
  * appendOTPPopUpHtml:
  *    This will check to see if the OTP Plugin HTML exists in the page and if not will append the pop up HTML to the body element.
  *    It will only inject this HTML once per page, and only if the plugin is instantiated for that page.
 **/
OTPModalPopup.prototype.appendOTPPopUpHtml = function() {
  var otpPopup = this;
  var s = otpPopup.settings;

  // This will be used to identify unique instances, by using selector .otp-modal-overlay.submitBtnId
  // Since there will be a popup for every submit button being protected in a page, it is imperative
  // that all selectors be limited to only selecting items by class within their own pop up.  eg)  $(".otp-send-code", $(".otp-modal-overlay.submitBtnId"))
  otpPopup.identifierClass = otpPopup.interceptor.settings.submitBtnId.replace(/#/g, "");
  
  // Assemble the full HTML for the popup content. All content comes from settings, which may be overridden via plugin constructor during instantiation. 
  var html =  "<div style='display:none;z-index:2147483605' class='modal fade otp-modal-overlay "+otpPopup.identifierClass+"' tabindex='-1' role='dialog' aria-labelledby='otpModalLabel' style='display: block;'>";
      html += "  <div class='modal-dialog otp-popup-wrapper' role='document'>";
      html += "    <div class='modal-content otp-content'>";            
      html += "      <div class='modal-header otp-close-header'><button type='button' class='close otp-cancel-modal' data-dismiss='modal' aria-label='Close'><i class='far fa-times-circle'></i></button></div>";
      html += "      <div class='modal-body otp-body'>";
      html += "        <div class='otp-title'><h5 class='jh-light header-three'>"+s.title+"</h5></div>"
      html += "        <p class='otp-explain-text'>"+s.intro;
      html += "        <p class='jh-label otp-sent-to'>"+s.codeWillBeSentTo+"</p>";
      html += otpPopup.buildContactMethodsHTML();
      html += "        <div style='display:none;' class='otp-code-sent'><p><br/>"+s.resendMessage+"<a class='otp-resend-code link'>"+s.resendLink+"</a>.</p></div>";
      html += "        <div style='display:none;' class='otp-enter-code form-group'><label for='"+s.enterCode+"' class='jh-label mr-sm-3'>"+s.enterCode+"</label><input maxlength='"+s.codeLength+"' type='text' class='form-control jh-input auto-width d-sm-inline-block otp-passcode' placeholder='"+s.codeFormatHelper+"'/></div>";
      html += "        <div style='display:none;' class='jh-error-label otp-error-text error'></div>";
      html += "      </div>";                
      html += "      <div class='otp-disclaimer'><p>"+s.disclaimer+"</p></div>";      
      html += "      <div class='modal-footer otp-dialog-buttons'>";
      html += "        <button type='button' aria-label='"+s.sendCode+"' class='btn btn-primary btn-lg otp-send-code'>" + s.sendCode + "</button>";
      html += "        <button type='button' style='display:none;' aria-label='"+s.submit+"'   class='btn btn-primary btn-lg otp-submit-button'>" + s.submit + "</button>";
      html += "      </div>";
      html += "    </div>";
      html += "  </div>";
      html += "</div>";
      if ($("#otpModalBackdrop").length < 1) {
        html += "<div id='otpModalBackdrop' style='display:none;z-index:2147483600' class='modal-backdrop fade'></div>";  // Only need one of these per page.
      }
    // Inject the HTML into the popup content:
  $("body").append(html);
   
  // Immediately re-aquire from the document:
  otpPopup.$otpModalPopup = $(".otp-modal-overlay."+otpPopup.identifierClass);
  otpPopup.$popUpContent = $(".otp-content", otpPopup.$otpModalPopup);
  otpPopup.$otpModalBackdrop = $("#otpModalBackdrop"); 
  s.chosenContactMethodIndex = 0;
  
  // If there is a drop down of contact options, this sets the first option in the drop down:  
  
  if (s.useDropDownSelector === true) {    
    $(".select-selected", otpPopup.$popUpContent).html($(".otp-method-0", otpPopup.$popUpContent).html());
  } else {
    $("#"+otpPopup.identifierClass+"_otp_method_radio_0").prop("checked", "checked");
    $(".otp-method-icon").css("width", "30px");
  }
  
  
  // Bind the click for the drop down menu of contact methods
  $(".select-selected", otpPopup.$popUpContent).bind("click", function(){
    $(".select-items", otpPopup.$popUpContent).toggle();
  });
  
  $(".otp-method-radio").bind("click", function() {
    var $selected = $(this);
    $(".otp-method-radio").prop("checked", "");
    $selected.prop("checked", "checked");
    s.chosenContactMethodIndex = $selected.data("methodIndex");    
  });
  
  // Bind the click for chosing a method from the drop down
  $(".otp-method", otpPopup.$popUpContent).bind("click", function(){
    var $selected = $(this);

    // Make the chosen method, show as the selected one when drop down collapsed
    s.chosenContactMethodIndex = $selected.data("methodIndex");    
    $(".select-selected", otpPopup.$popUpContent).html($selected.html());

    // Remove preious highlight and highlyt the newly selected option in the dropdown
    $(".otp-method", otpPopup.$popUpContent).removeClass("same-as-selected");
    $selected.addClass("same-as-selected");

    // Hide all of this drop down's items.
    $(".select-items", otpPopup.$popUpContent).hide();
  });
  
  
  // Bind the focus event of the otp-passcode field in this pop up, so that we can clear the error on focus.
  $(".otp-passcode", otpPopup.$otpModalPopup).bind("focus", function(e) {          
    otpPopup.clearError();
  });
  
  $(".otp-passcode", otpPopup.$otpModalPopup).bind("keypress", function(e) {          
    if (e.which == 13) {
      otpPopup.submitCode();
    }
  });

  // Bind "Send Code" button click handler:
  $(".otp-send-code", otpPopup.$otpModalPopup).bind("click", function(e) {
    otpPopup.clearError();
    otpPopup.sendOtpCode();
  });
  
  // Bind "Resend Code" link click handler:
  $(".otp-resend-code", otpPopup.$otpModalPopup).bind("click", function(e) {
  otpPopup.clearError();
    otpPopup.sendOtpCode();
  });
  
  // Bind "Cancel" button or link click handler:
  $(".otp-cancel-modal", otpPopup.$otpModalPopup).bind("click", function(e) {
    otpPopup.hide();
  });
    
  // Bind "Submit" code button click handler:        
  $(".otp-submit-button", otpPopup.$otpModalPopup).bind("click", function(e) {
    otpPopup.submitCode();
  });
};    

OTPModalPopup.prototype.submitCode = function() {
  var otpPopup = this;
  var s = otpPopup.settings;
  
  var $passCode = $(".otp-passcode", otpPopup.$popUpContent);                    
  
  // Remove this filter if you want to allow more characters other than letters and numbers.
  var otpCode = $passCode.val().replace(/\W/g, '');
  
  otpPopup.clearError();
      
  if (otpCode != undefined && otpCode != "") {
    otpPopup.validateOtpCode(otpCode);
  } else {
    otpPopup.setError(otpPopup.settings.invalidCodeError);
  }
};

OTPModalPopup.prototype.hide = function() {
  var otpPopup = this;  
  var $sendCodeBtn = $(".otp-send-code",otpPopup.$popUpContent);
  var $submitCodeBtn = $(".otp-submit-button",otpPopup.$popUpContent);
  var $enterCode = $(".otp-enter-code",otpPopup.$popUpContent);          
  var $codeSentText = $(".otp-code-sent",otpPopup.$popUpContent);                
  var $passCode = $(".otp-passcode",otpPopup.$popUpContent);          

  this.$currentSubmit = undefined;
  
  // Hide the pop up and overlay
  otpPopup.$otpModalPopup.removeClass("show")
  otpPopup.$otpModalPopup.hide();
  $("#otpModalBackdrop").removeClass("show");
  $("#otpModalBackdrop").hide();         
  // Clean up, in case we need to show again later
  otpPopup.clearError();
  $passCode.val("");
  $enterCode.hide();
  
  $submitCodeBtn.hide();  
  $submitCodeBtn.html(otpPopup.settings.submit);     
  $submitCodeBtn.prop("disabled", "");
  $submitCodeBtn.css("cursor", "pointer");
  
  $codeSentText.hide();
  $sendCodeBtn.html(otpPopup.settings.sendCode);
  $sendCodeBtn.prop("disabled", "");
  $sendCodeBtn.css("cursor", "pointer");
  $sendCodeBtn.show();   
};

/**
  * Send OTP Code (To Participant):
  *   This will reset the UI for sending or resending a code.
  *   It is also responsible for the AJAX call to the back end controller
  *   to cause the code to be sent.
 **/ 
OTPModalPopup.prototype.sendOtpCode = function() {      
  var otpPopup = this;
  var $sendCodeBtn = $(".otp-send-code",otpPopup.$popUpContent);
  var $submitCodeBtn = $(".otp-submit-button",otpPopup.$popUpContent);
  var $enterCode = $(".otp-enter-code",otpPopup.$popUpContent);          
  var $codeSentText = $(".otp-code-sent",otpPopup.$popUpContent);                
  var $passCode = $(".otp-passcode",otpPopup.$popUpContent);
  
  $enterCode.hide();
  $submitCodeBtn.hide();          
  $codeSentText.hide();
  $passCode.val("");
  $sendCodeBtn.html(otpPopup.settings.sendingCode);
  $sendCodeBtn.prop("disabled", "true");
  $sendCodeBtn.css("cursor", "wait");          
  $sendCodeBtn.show();
  
  // ajaxCallToTriggerOTPSendCode goes here.
  otpPopup.ajaxSendOtpCode();
};

OTPModalPopup.prototype.validateOtpCode = function(codeEntered) {
  // Calls an AJAX method to validate the OTP code.
  // Changes button text to indicate waiting for submit code validation to come back.
  var otpPopup = this;
  var $submitCodeBtn = $(".otp-submit-button",otpPopup.$popUpContent);
  
  $submitCodeBtn.html(otpPopup.settings.validatingCode);
  $submitCodeBtn.prop("disabled", "true");
  $submitCodeBtn.css("cursor", "wait");          
  $submitCodeBtn.show();
  
  otpPopup.ajaxValidateOtpCode(codeEntered);  
};  


/**
  * ajaxCallbackForSendOtpCode:
  *    Called on "success" callback for the ajaxSendOtpCode method.
  **/
OTPModalPopup.prototype.ajaxCallbackForSendOtpCode = function(data) {
  var otpPopup = this;
  var $sendCodeBtn = $(".otp-send-code",otpPopup.$popUpContent);
  var $submitCodeBtn = $(".otp-submit-button",otpPopup.$popUpContent);
  var $enterCode = $(".otp-enter-code",otpPopup.$popUpContent);          
  var $codeSentText = $(".otp-code-sent",otpPopup.$popUpContent);                
  
  if (data == undefined || data.otpSent == undefined || data.otpSent === false) {
	  otpPopup.setError(otpPopup.settings.systemError);
  }
  
  $sendCodeBtn.prop("disabled", "");
  $sendCodeBtn.css("cursor", "pointer");
  $sendCodeBtn.hide();       

  $codeSentText.show();
  $enterCode.show();
  $submitCodeBtn.show();
};

/**
  * ajaxSendOtpCode:
  *  This is the AJAX call to the back end system to tell 
  *  it to send the OTP code to the user via.
  *  Once this call completes and the AJAX asynchronously 
  *  sends a respnose back the success method will be
  *  executed.
  **/
OTPModalPopup.prototype.ajaxSendOtpCode = function() {
  var otpPopup = this;
  var s = otpPopup.settings;
  
  if (s.mode == otpOperationModes.demo) {
    otpPopup.demoSendOtpCode();
  } else {
    try {
      $.ajax({
        type: "GET",
        url: otpPopup.settings.ajaxURISendOTPCode,
	      data: { 
          contactMethodIndex: s.chosenContactMethodIndex
  	    },
    	  success: function(response) {
  	     otpPopup.ajaxCallbackForSendOtpCode(JSON.parse(response));
    	  },
	      error: function(response) {
      		otpPopup.ajaxCallbackForSendOtpCode(JSON.parse(response));
	      }
	    });
    } catch(error) {
      otpPopup.setError(otpPopup.settings.systemError);
    }
  }
};
  
  
/** 
  * ajaxCallbackForValidateOtpCode
  *    This is the callback function for the AJAX call
  * 
  **/
OTPModalPopup.prototype.ajaxCallbackForValidateOtpCode = function(data) {
  var otpPopup = this;
  var s = otpPopup.settings;
  
  var $submitCodeBtn = $(".otp-submit-button",otpPopup.$popUpContent);
  // AJAX call to verify code goes here.
    
  if (data != undefined && data.valid === true && (data.error === undefined || data.error == '')) {
    // Sets the global session variable to true.
    // The back end must also have set this to true during the submission of the code.
    // This only prevents the pop up from happening again on the same page without reload.
    // If page reloads, it must re-aquire the newly "true" value from the backend and set
    // that into the global variable.
    sessionIsOTPValidated = true;
    otpPopup.interceptor.settings.submitMethod();
    otpPopup.hide();
  } else {
    // Handled all errors by passing back the appropriate error string:
    if (data === undefined) {
      otpPopup.setError(s.systemError);
    } else if (data.error == s.invalidCodeJSON) {
      // Display code not matching error
      otpPopup.setError(s.invalidCodeError);      
    } else if (data.error == s.systemErrorJSON) {
      // Display general system error.  Shouldn't happen unless network error.
      otpPopup.setError(s.systemError);
    } else if (data.error == s.maxAttemptsJSON) {
      otpPopup.setError(s.maxAttemptsError);
    } else if (data.error == s.codeExpiredJSON) {
      otpPopup.setError(s.codeExpiredError);
    } else {
      // Catch all in case...
      otpPopup.setError(s.systemError);
    }
  }

  $submitCodeBtn.html(otpPopup.settings.submit);     
  $submitCodeBtn.prop("disabled", "");
  $submitCodeBtn.css("cursor", "pointer");
};

/**
  * ajaxValidateOtpCode:
  *   This is the AJAX call to the back end system to ask it to
  *   validate the OTP code for this user.  On successful completion
  *   of the call, the success callback method will be executed.
  *
  **/
OTPModalPopup.prototype.ajaxValidateOtpCode = function(codeEntered) {
  var otpPopup = this;    
  var s = otpPopup.settings;

  if (s.mode == otpOperationModes.demo) {
    // Demo Mode
    otpPopup.demoValidateOtpCode(codeEntered);
  } else {
    // Normal Mode
      try {  
        $.ajax({
          url: otpPopup.settings.ajaxURISendValidateCode,
          type: "GET",
          data: { 
            codeEntered: codeEntered
          },
          success: function(response) {
            otpPopup.ajaxCallbackForValidateOtpCode(JSON.parse(response));
          },
          error: function(response) {
            otpPopup.ajaxCallbackForValidateOtpCode(JSON.parse(response));
          }
        });
      } catch(error) {
          otpPopup.setError(otpPopup.settings.systemError);
      }
  }
};

/*  DEMO MODE ONLY */
OTPModalPopup.prototype.demoSendOtpCode = function(codeEntered) {  
  var otpPopup = this;
  var s = otpPopup.settings;
  // This is a routine for DEMO mode only
  setTimeout(function() {
    var data = {};
    data.otpSent = true;
    otpPopup.ajaxCallbackForSendOtpCode(data);
    setTimeout(function() {
      alert("FOR DEMO PURPOSES:\nYou have received a code at: " + s.contactMethods[s.chosenContactMethodIndex].address + ".\n Your code is 123456");
    }, 400);
  }, 500);
};

/*  DEMO MODE ONLY */
OTPModalPopup.prototype.demoValidateOtpCode = function(codeEntered) {  
  var otpPopup = this;
  var s = otpPopup.settings;
  // This is a simulated routine for DEMO mode only
  setTimeout(function() {
    if (codeEntered === "123456") {
      var data = { valid : true };
      otpPopup.ajaxCallbackForValidateOtpCode(data);
    } else if (codeEntered == "888888") {
      var data = { valid : false, error: s.codeExpiredJSON};
      otpPopup.ajaxCallbackForValidateOtpCode(data);      
    } else if (codeEntered == "777777") {
      var data = { valid : false, error: s.systemErrorJSON};
      otpPopup.ajaxCallbackForValidateOtpCode(data);      
    } else if (codeEntered == "999999") {
      var data = { valid : false, error: s.maxAttemptsJSON};
      otpPopup.ajaxCallbackForValidateOtpCode(data);      
    } else if (codeEntered != "123456") {
      var data = { valid : false, error: s.invalidCodeJSON };
      otpPopup.ajaxCallbackForValidateOtpCode(data);
    } else {
      var data = { valid : false, error: s.systemErrorJSON };
      otpPopup.ajaxCallbackForValidateOtpCode(data);
    }
  }, 500);  
};


/***************************************************************************************************************************************
  * OTPInterceptor - One Time Passcode Submission Interceptor
  *
  *   This class once instantiated, handles the submit following 
  *   The data on the Submit buttons on the page forms, is what is passed to this pop up when it is required to be used to process
  *   forms that have been identified as requiring OTP.
  *
  *   For OTP to work properly, this pop up class must be initialized by the page, passing in any overrides to the default options
  *   shown below.
  * @constructor 
***************************************************************************************************************************************/
 ;( function( $, window, document, undefined ) {
	"use strict";
  var pluginName = "OTPInterceptorPlugin";
  
    // Form Submit Default Options Only:
    var formSubmitDefaults = {      
    };    

    /**
    * OTPInterceptor : CONSTRUCTOR
    *   submitMethod - a JavaScript method that is passed in to perform the form submit following a successful OTP
    *   options - Mandatory settings, and additional options to override any default options. 
    *        
   **/

  function OTPInterceptor(frmSubmitBtn, formSubmitOptions, popUpOptions) {    
    var otpInterceptor = this;
        otpInterceptor._name = pluginName;
        
    // Override defaults and put them into settings:
    otpInterceptor.settings = $.extend({}, formSubmitDefaults, formSubmitOptions );
    
    // Find the submit button via the mandatory submitBtnId field:
    if (otpInterceptor.settings.submitBtnId != undefined) {
      
      // Ensure ID selector starts with #:
      if (!otpInterceptor.settings.submitBtnId.indexOf("#")===0) {
        otpInterceptor.settings.submitBtnId = "#"+otpInterceptor.settings.submitBtnId;
      }
      
      // Grab the button we are supposed to intercept the submit on:
      otpInterceptor.$formSubmitBtn = $(frmSubmitBtn);
      
    } else {
      // Developer failed to pass in a submitBtnId.
      if (console.log != undefined) {
        console.log("Developer Error.  No class or ID for the submit button was specified.");
      }
    }

    // Create an OTP popup specific to this interceptor using the provided popup options.
    // This allows overriding the text in each pop up if desired by passing different options
    // during the plugin instantiation.
    otpInterceptor.popup = new OTPModalPopup(otpInterceptor, popUpOptions);
    
    // Call this interceptor's initializer:
    otpInterceptor.init();
  };

  OTPInterceptor.prototype.init = function() {
    var otpInterceptor = this;      
    otpInterceptor.alterSubmitAction();
  };

  /**
    * alterSubmitAction:
    *   This ensures that submit buttons on the page's form, are handled, 
    *   and OTP is performed if necessary before submission.
    *   Note the back-end must double check the session status during validation
    *   of the form, to ensure that OTP really did pass.
   **/ 
  OTPInterceptor.prototype.alterSubmitAction = function() {
    var otpInterceptor = this;    
    
    otpInterceptor.$formSubmitBtn.bind("click", function(e) {
      e.preventDefault();
      
      var isOtpRequired = true;
      if (otpInterceptor.settings.isOtpRequired != undefined) {
        isOtpRequired = otpInterceptor.settings.isOtpRequired();
      }    
      
      if (otpInterceptor.popup.settings.mode == otpOperationModes.passthrough) {
        // Passthrough bypasses the pop up.
        isOtpRequired = false;
      }
      
      if (sessionIsOTPValidated === false && isOtpRequired === true) {
        // Displays the OTP popup.
        otpInterceptor.popup.show();
      } else {
        // All other cases allow the form to be submitted.
        otpInterceptor.settings.submitMethod();        
      }
    });
  };
    
  /**
    * Lightweight plugin wrapper around the constructor, to prevent multiple instantiations
    */
  $.fn[ pluginName ] = function( formOptions, popupOptions ) {
    return this.each( function() {
      if ( !$.data( this, "plugin_" + pluginName ) ) {
        $.data( this, "plugin_" +
          pluginName, new OTPInterceptor( this, formOptions, popupOptions ) );
      }
    });
  };  
  
})( jQuery, window, document );