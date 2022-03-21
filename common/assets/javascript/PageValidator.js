/**
 * To use this class, you need to first instantiate the PageValidation object,
 * then register fields that needs validation. This class requires the jQuery library.
 *
 * In general, you should supply a field ID to the method and the error message for
 * display. If the message requires substitutions, you can supply the parameters in
 * the third argument. Substitutions has the format {0}, {1}, etc.
 *
 * pageValidator.registerNumericField("moneyTypeVesting1", "keyup", clearErrorIcon,
 *                                    showErrorIcon, ['moneyTypeVesting1ErrorIcon']);
 */

function ValidationResult() {
  this.status = 'OK'; // possible statuses: OK, FAIL, UNKNOWN
  this.details = null;
}

function PageValidator() {
  
  PageValidator.NUMERIC_CHARACTER_REGEXP = /[0-9\.\,]+/g;
  PageValidator.NUMERIC_CHARACTER_NO_COMMA_REGEXP = /[0-9\.]+/g;
  PageValidator.NUMERIC_CHARACTER_ONLY_REGEXP = /[0-9]+/g;
  PageValidator.COMMA_REGEXP = /\,/g;

  // Allowed characters for multi-line input fields - for use with special character validation
  // (chars between decimal 32 and 126 (or hex 20 to 7e) inclusive, plus CR and LF)
  PageValidator.WEB_MULTILINE_CHARACTER_REGEXP = /[\n\r\x20-\x7e]+/gi;

  // Allowed characters for transfer to Apollo. Decimal 32 - 126 (or hex 20 to 7e) inclusive.
  PageValidator.APOLLO_ALLOWED_CHARACTERS_REGEXP = /[\x20-\x7e]+/gi;

  // Allowed characters for transfer to Apollo, other than the so called special characters.
  // Decimal 32 - 126 (or hex 20 to 7e) inclusive, excluding:
  //          '!', '{', '}', '|', '%', '¬', '"', '&', '<', '>', 
  //          '-', '¢', '¦', '$', '#', '=', ';', '~', '@', '^'
  //     Note that the following are outside of Decimal 32 - 126: '¬', '¢', '¦'
  PageValidator.APOLLO_ALLOWED_NON_SPECIAL_CHARACTERS_REGEXP = /[\x20\x27-\x2c\x2e-\x3a\x3fA-Z\x5b-\x5d\x5f-\x7a]+/gi;

  // Allowed characters for electronic payment data transfered to outside vendors.
  // Decimal 32 - 126 (or hex 20 to 7e) inclusive, excluding '"', '&', '<'
  PageValidator.ELECTRONIC_PAYMENT_ALLOWED_CHARACTERS_REGEXP = /[\x20-\x21\x23-\x25\x27-\x3b\x3d-\x7e]+/gi;

  PageValidator.PERCENTAGE_REGEXP = /\d?\d?\d?\.?\d?\d?\d?/;

  this.__fieldIds = new Array();
  this.__fieldIdInErrors = new Array();
  
  this.__setFieldIdInError = function(fieldId) {
    this.__fieldIdInErrors.push(fieldId);
  }

  this.__unsetFieldIdInError = function(fieldId) {
    var newArray = new Array();
    for (var i = 0; i < this.__fieldIdInErrors.length; i++) {
      if (this.__fieldIdInErrors[i] != fieldId) {
        newArray.push(fieldId);
      }
    }
    this.__fieldIdInErrors = newArray;
  }
  
  this.__registerValidationFunction = function(fieldId, eventName, validationFunction, successCallback, failureCallback, callbackParams) {
    var validator = this;
    $("#" + fieldId).bind(eventName, function(e) {
      var value = $("#" + fieldId).val();
      //alert("validation:" + validationFunction + " triggered:" + e.type + " field value:'" + value + "'");
      var rc = validationFunction(value);
      if (rc.status == 'FAIL') {
        validator.__setFieldIdInError(fieldId);
        if (typeof(failureCallback) != 'undefined') {
          failureCallback(e, callbackParams);
          // setTimeout(function() { $("#" + fieldId).focus(); }, 100); 
        }
      } else if (rc.status == 'OK') {
        validator.__unsetFieldIdInError(fieldId);
        if (typeof(successCallback) != 'undefined') {
          successCallback(e, callbackParams);
        }
      }
    });
  }

  this.hasError = function(field) {
    if (typeof(field) == 'undefined') {
      return this.__fieldIdInErrors.length > 0;
    } else {
      for (var i = 0; i < this.__fieldIdInErrors.length; i++) {
        if (this.__fieldIdInErrors[i] == field) {
          return true;
        }
      }
      return false;
    }
  }

  this.registerRequired = function(fieldId, eventName, successCallback, failureCallback, callbackParams) {
    this.__registerValidationFunction(fieldId, eventName, function(value) {
      var rc = new ValidationResult();
      if (typeof(value) == 'undefined') {
        rc.status = 'UNKNOWN';
      } else {
        if (jQuery.trim(value) == '') {
          rc.status = 'FAIL';
        } else {
          rc.status = 'OK';
        }
      }
      return rc;
    }, successCallback, failureCallback, callbackParams);
  }

  this.registerPositiveNumber = function(fieldId, eventName, successCallback, failureCallback, callbackParams) {
    this.__registerValidationFunction(fieldId, eventName, function(value) {
      var rc = new ValidationResult();
      if (typeof(value) == 'undefined') {
        rc.status = 'UNKNOWN';
      } else {
        // Strip all commas, to ensure before checking if value is a number.     
        var strippedValue = value.replace(PageValidator.COMMA_REGEXP, '');
        if (!isNaN(strippedValue) && strippedValue >= 0) {
          rc.status = 'OK';
        } else {
          rc.status = 'FAIL';
        }
      }
      return rc;
    }, successCallback, failureCallback, callbackParams);
  }

  this.registerPositiveWholeNumber = function(fieldId, eventName, successCallback, failureCallback, callbackParams) {
    this.__registerValidationFunction(fieldId, eventName, function(value) {
      var rc = new ValidationResult();
      if (typeof(value) == 'undefined') {
        rc.status = 'UNKNOWN';
      } else {
        // Strip all commas, to ensure before checking if value is a number.     
        var strippedValue = value.replace(PageValidator.COMMA_REGEXP, '');
        if (!isNaN(strippedValue) && strippedValue >= 0 && strippedValue == Math.floor(strippedValue)) {
          rc.status = 'OK';
        } else {
          rc.status = 'FAIL';
        }
      }
      return rc; 
    }, successCallback, failureCallback, callbackParams);
  }

// Returns OK for the rc if the value in the field fieldId is <= the value of parm maximum.
  this.registerMaximum = function(fieldId, eventName, maximum, successCallback, failureCallback, callbackParams) {
    this.__registerValidationFunction(fieldId, eventName, function(value) {
      var rc = new ValidationResult();
      if (value == " " || value == "") {
        // A blank or empty value is considered to be a numeric zero.
        value = "0";
      }
      // Strip all commas, to ensure before checking if value is a number.     
      var strippedValue = value.replace(PageValidator.COMMA_REGEXP, '');
      if (typeof(value) == 'undefined' || isNaN(strippedValue)) {
        rc.status = 'UNKNOWN';
      } else {
        var maximumValue = maximum;
        if (typeof(maximum) == 'function') {
          maximumValue = maximum();
        }
        if (parseFloat(strippedValue) <= parseFloat(maximumValue)) { 
          rc.status = 'OK';
        } else {
          rc.status = 'FAIL';
        }
      }
      return rc; 
    }, successCallback, failureCallback, callbackParams);
  }

// Returns OK for the rc if the value in the field fieldId is >= the value of parm minimum.
  this.registerMinimum = function(fieldId, eventName, minimum, successCallback, failureCallback, callbackParams) {
    this.__registerValidationFunction(fieldId, eventName, function(value) {
      var rc = new ValidationResult();
      if (value == " " || value == "") {
        // A blank or empty value is considered to be a numeric zero.
        value = "0";
      }
      // Strip all commas, to ensure before checking if value is a number.     
      var strippedValue = value.replace(PageValidator.COMMA_REGEXP, '');
      if (typeof(value) == 'undefined' || isNaN(strippedValue)) {
        rc.status = 'UNKNOWN';
      } else {
        var minimumValue = minimum;
        if (typeof(minimum) == 'function') {
          minimumValue = minimum();
        }
        if (parseFloat(strippedValue) >= parseFloat(minimumValue)) { 
          rc.status = 'OK';
        } else {
          rc.status = 'FAIL';
        }
      }
      return rc; 
    }, successCallback, failureCallback, callbackParams);
  }

// Returns OK for the rc if the value in the field fieldId is 
// >= the value of parm minimum AND <= the value of parm maximum.  If the value
// is < this range, a "-1" is added to the end of the callbackParams array.
// If the value is > this range, a "1" is added to the end of the callbackParams array.
  this.registerRangeCheck = function(fieldId, eventName, minimum, maximum, successCallback, failureCallback, callbackParams) {
    this.__registerValidationFunction(fieldId, eventName, function(value) {
      var rc = new ValidationResult();
      
      if (value == " " || value == "") {
        // A blank or empty value is considered to be a numeric zero.
        value = "0";
      }
      // Strip all commas, to ensure before checking if value is a number.     
      var strippedValue = value.replace(PageValidator.COMMA_REGEXP, '');
      if (typeof(value) == 'undefined' || isNaN(strippedValue)) {
        rc.status = 'UNKNOWN';
      } else {
        var maximumValue = maximum;
        if (typeof(maximum) == 'function') {
          maximumValue = maximum();
        }
        var minimumValue = minimum;
        if (typeof(minimum) == 'function') {
          minimumValue = minimum();
        }
        if ((parseFloat(strippedValue) >= parseFloat(minimumValue)) 
              && (parseFloat(strippedValue) <= parseFloat(maximumValue))) { 
          rc.status = 'OK';
        } else if (parseFloat(strippedValue) < parseFloat(minimumValue)) {
          callbackParams.push(-1);
          rc.status = 'FAIL';
        } else {
          callbackParams.push(1);
          rc.status = 'FAIL';
        }
      }
      return rc; 
    }, successCallback, failureCallback, callbackParams);
  }

/**
 * Validates that the length of the field is less than or equal to "maximumLength".
 * If countNLAsTwo == true, then all newLine chars ('\n') are counted as 2 chars
 * since that's how they'll be store on the DB, but javaScript only counts them as one.
 */
  this.registerMaximumLength = function(fieldId, eventName, maximumLength, countNLAsTwo, successCallback, failureCallback, callbackParams) {
    this.__registerValidationFunction(fieldId, eventName, function(value) {
      var rc = new ValidationResult();
      if (typeof(value) == 'undefined') {
        rc.status = 'UNKNOWN';
      } else {
        var newValue = value;
        if (countNLAsTwo) {
          // Replace each newLine char with 2 chars.
          newValue = value.replace(/\n+/gi, ' \n');
        }
        if (newValue.length <= maximumLength) {
          rc.status = 'OK';
        } else {
          callbackParams.push(newValue.length);
          rc.status = 'FAIL';
        }
      }
      return rc; 
    }, successCallback, failureCallback, callbackParams);
  }

/**
 * Validates that the length of the field is exactly "length" characters long.
 * If isZeroLengthOk == true, then a length of zero is accepted.
 */
  this.registerLength = function(fieldId, eventName, length, isZeroLengthOk, successCallback, failureCallback, callbackParams) {
    this.__registerValidationFunction(fieldId, eventName, function(value) {
      var rc = new ValidationResult();
      if (typeof(value) == 'undefined') {
        rc.status = 'UNKNOWN';
      } else {
        if (value.length == length || (value.length == 0 && isZeroLengthOk)) {
          rc.status = 'OK';
        } else {
          rc.status = 'FAIL';
        }
      }
      return rc; 
    }, successCallback, failureCallback, callbackParams);
  }

/**
 * Validates that the specified field only contains 'allowed characters' as
 * specified by the regular expression.  If any invalid characters exist,
 * the list of these characters is appended to array callbackParams. 
 */
 this.registerAllowedCharacters = function(fieldId, eventName, validCharacterRegEx, successCallback, failureCallback, callbackParams) {
    this.__registerValidationFunction(fieldId, eventName, function(value) {
      var rc = new ValidationResult();
      if (typeof(value) == 'undefined' || value == null) {
        rc.status = 'UNKNOWN'
      } else {
		var strippedValue = value.replace(validCharacterRegEx, '')

		if (strippedValue.length > 0) {
  	          callbackParams.push(strippedValue);
      	      rc.status = 'FAIL';
        	} else {
	            rc.status = 'OK';
      	}
	}
      return rc; 
    }, successCallback, failureCallback, callbackParams);
  }

/**
 * Validates if the date is valid, by calling function dateUtils.validateDate.
 * A null/empty date is considered valid, but a blank date of length > 0 is invalid.
 */
 this.registerValidDate = function(fieldId, eventName, successCallback, failureCallback, callbackParams) {
    this.__registerValidationFunction(fieldId, eventName, function(value) {
      var rc = new ValidationResult();
      if (value == null || value == '') {
		rc.status = 'OK';
	} else if (typeof(value) == 'undefined') {
	      rc.status = 'UNKNOWN'
      } else {
		var result = dateUtils.validateDate(value);
		if (result == dateUtils.OK) {
	      	rc.status = 'OK';
        	} else {
                  callbackParams.push(result);
      	      rc.status = 'FAIL';
      	}
	}
      return rc; 
    }, successCallback, failureCallback, callbackParams);
  }
  
}
