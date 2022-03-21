// Indicator on whether alert is being shown
var alertShown = false;

// Variable to help focus fields to get around Firefox limitation
var fieldToFocus;

// Allowed characters for input fields - for use with special character validation
var webAllowedCharacterRegEx = /[ !\"#\$%&'\(\)\*\+,-\./0-9:;<=>\?@A-Z\[\\\]\^_`\{\|\}~]+/gi;

// Allowed characters for multi-line input fields - for use with special character validation
var webMultiLineAllowedCharacterRegEx = /[\n\r !\"#\$%&'\(\)\*\+,-\./0-9:;<=>\?@A-Z\[\\\]\^_`\{\|\}~]+/gi;

// Allowed characters for input fields that are sent to external parties
var externalPartiesAllowedCharacterRegEx = /[ !#\$%'\(\)\*\+,-\./0-9:;=>\?@A-Z\[\\\]\^_`\{\|\}~]+/gi;

// Allowed characters for input fields that are sent to Apollo
var apolloAllowedCharacterRegEx = /[ '\(\)\*\+,\./0-9:\?A-Z\[\\\]_`]+/gi;
     
/**
 * Focuses on the field specified by the global fieldToFocus variable.  This is required to
 * get around a limitation of firefox that prevents fields from focusing properly due to a
 * timing issue.
 */
function focusField() {
	fieldToFocus.focus();
}

/**
 * Handles the error display and field focus when a JS validation is detected.
 *
 * @param field The field containing the validation error.
 * @param message The message to display in an alert if the validation fails.
 */
function handleError(field, message) {

 	// Validation failed - show alert and refocus on invalid field
 	fieldToFocus = field;
 	displayAlert(message);
	setTimeout('focusField();', 10);
}

/**
 * Validates the max length of the specified field.  If the validation fails:
 * Focus is returned to the field and an alert message is shown.
 * Stripped and modified From Commons Validator 1.3
 *
 * @param field The field to validate for max length.
 * @param fieldName The display name of the field being validated.
 * @param maxLength The maximum length being validated against.
 */
function validateMaxLength(field, fieldName, maxLength) {

	if (isDefined(field)) {
		if ((field.type == 'hidden' ||
	  	 	 field.type == 'text' ||
	   		 field.type == 'password' ||
	   		 field.type == 'textarea')) {

			/* Adjust length for carriage returns - see Bug 37962 */
			var lineReturnLength = 1;
	    var adjustAmount = 0;
	    if (lineReturnLength > 0) {
	       var rCount = 0;
	       var nCount = 0;
	       var crPos = 0;
	       while (crPos < field.value.length) {
	           var currChar = field.value.charAt(crPos);
	           if (currChar == '\r') {
	               rCount++;
	           }
	           if (currChar == '\n') {
	               nCount++;
	           }
	           crPos++;
	       }
	       var endLength = parseInt(lineReturnLength);
	       adjustAmount = (nCount * endLength) - (rCount + nCount);
	    }

			// Determine if validation has failed
	    if ((field.value.length + adjustAmount)  > maxLength) {

				var errorMessage = '{0} is {2} characters long.  It cannot be more than {1} characters long.';

				// Add field name to error message
				errorMessage = errorMessage.replace(/\{0\}/, fieldName);
		
				// Add max length + 1 to error message
				errorMessage = errorMessage.replace(/\{1\}/, maxLength);
				
				// Add field length to message string
				errorMessage = errorMessage.replace(/\{2\}/, (field.value.length + adjustAmount));

				handleError(field, errorMessage);
		   	return false;
	    }
	 }
  }
  
  return true;
}

/**
 * Validates the min length of the specified field.  If the validation fails:
 * Focus is returned to the field and an alert message is shown.
 * Stripped and modified From Commons Validator 1.3
 *
 * @param The field to validate for max length.
 * @param message The message to display in an alert if the validation fails.
 * @param minLength The minimum length being validated against.
 */
function validateMinLength(field, message, minLength) {

	if (isDefined(field)) {
		if ((field.type == 'hidden' ||
	  	 	 field.type == 'text' ||
	   		 field.type == 'password' ||
	   		 field.type == 'textarea')) {

			if (field.value.length > 0) {

				/* Adjust length for carriage returns - see Bug 37962 */
				var lineReturnLength = 1;
		    var adjustAmount = 0;
		    if (lineReturnLength > 0) {
		       var rCount = 0;
		       var nCount = 0;
		       var crPos = 0;
		       while (crPos < field.value.length) {
		           var currChar = field.value.charAt(crPos);
		           if (currChar == '\r') {
		               rCount++;
		           }
		           if (currChar == '\n') {
		               nCount++;
		           }
		           crPos++;
		       }
		       var endLength = parseInt(lineReturnLength);
		       adjustAmount = (nCount * endLength) - (rCount + nCount);
		    }

				// Determine if validation has failed
		    if ((field.value.length + adjustAmount)  < minLength) {

					handleError(field, message);
			   	return false;
		    }
		  }
		}
  }
  
   return true;
}

/**
 * Displays the specified alert message if an alert is not already being displayed.
 *
 * @param message The message to display in an alert if the validation fails.
 */
function displayAlert(message) {

  	// Detect if an alert message has already been displayed
	if (alertShown) {
		// An alert was being shown - turn indicator off to prevent locking
			alertShown = false;
	} else {
		// Alert not shown - turn indicator on and show alert
		alertShown = true;
		alert(message);
		// Alert closed - turn indicator off
		alertShown = false;
	}
}

/**
 * Check a value only contains valid decimal digits
 * @param argvalue The value to check.
 * From Commons Validator 1.3
 */
function isDecimalDigits(argvalue) {
    argvalue = argvalue.toString();
    var validChars = "0123456789";

    var startFrom = 0;
    if (argvalue.charAt(0) == "-") {
        startFrom = 1;
    }

    for (var n = startFrom; n < argvalue.length; n++) {
        if (validChars.indexOf(argvalue.substring(n, n+1)) == -1) return false;
    }
    return true;
}

   /**
    * Validates that the specified field is a float.  If the validation fails:
    * Focus is returned to the field and an alert message is shown.
    * Stripped and modified From Commons Validator 1.3
    *
    * @param The field to validate float format.
    * @param message The message to display in an alert if the validation fails.
    */
   function validateFloat(field, message) {

     var isValid = true;

		if (isDefined(field)) {
			if ((field.type == 'hidden' ||
			    field.type == 'text' ||
			    field.type == 'textarea' ||
			    field.type == 'select-one' ||
			    field.type == 'radio')) {
	
				// get field's value
				var value = '';
				if (field.type == "select-one") {
				    var si = field.selectedIndex;
				    if (si >= 0) {
				        value = field.options[si].value;
				    }
				} else {
				    value = field.value;
				}
	
				if (value.length > 0) {

					// Strip any ','
					value = deformatAmount(value);
				
			    // remove '.' before checking digits
			    var tempArray = value.split('.');
			    //Strip off leading '0'
			    var zeroIndex = 0;
			    var joinedString= tempArray.join('');
			    while (joinedString.charAt(zeroIndex) == '0') {
		        zeroIndex++;
			    }
			    var noZeroString = joinedString.substring(zeroIndex, joinedString.length);
	
					if (!isDecimalDigits(noZeroString) || tempArray.length > 2) {
				    isValid = false;
	
					} else {
		        var iValue = parseFloat(value);
		        if (isNaN(iValue)) {
		          isValid = false;
			      }
		     	}
				}
		  }
		}
		
		if (!isValid) {
			handleError(field, message);
		}
		
		return isValid;
}

/**
 * Validates that the specified field falls within the specified range (inclusive).
 * It is expected that the field has already passed float validation.
 * If the validation fails:
 * Focus is returned to the field and an alert message is shown.
 * Stripped and modified From Commons Validator 1.3
 *
 * @param The field to validate float range on.
 * @param message The message to display in an alert if the validation fails.
 * @param min The minimum of the range being validated against.
 * @param max The maximum of the range being validated against.
 */
function validateFloatRange(field, message, min, max) {

	if (isDefined(field)) {

	  var isValid = true;

    if ((field.type == 'hidden' ||
	       field.type == 'text' ||
	       field.type == 'textarea') &&
        (field.value.length > 0)) {

			var fMin = parseFloat(min);
      var fMax = parseFloat(max);
      var fValue = parseFloat(deformatAmount(field.value));

			// Determine if validation has failed
      if (!((fValue >= fMin) && (fValue <= fMax))) {

				handleError(field, message);
		   	return false;
	    }
    }
	}
	
   return true;
 }

/**
 * Validates that the specified field only contains 'valid characters' as
 * defined in the glossary.
 * If the validation fails:
 * Focus is returned to the field and an alert message is shown.
 *
 * @param field The field to validate.
 * @param validCharacterString The array of valid characters to validate against.
 * @param message1 The first portion of the message to display in an alert if the validation fails.
 * @param message2 The second portion of the message to display in an alert if the validation fails.
 */
function validateSpecialCharacters(field, validCharacterString, message1, message2) {

	if (isDefined(field) && hasContent(field)) {

		var value = field.value;

    for (var i = 0; i < value.length; i++) {

	    if (validCharacterString.indexOf(value.substring(i, i+1)) == -1) {

				handleError(field, message1 + value.substring(i, i+1) + message2);
	      return false;
	    } // fi
    } // end for

	}
	
  return true;
}

/**
 * Validates that the specified field only contains 'allowed characters' as
 * specified by the regular expression.
 * If the validation fails:
 * Focus is returned to the field and an alert message is shown.
 *
 * @param field The field to validate.
 * @param validCharacterRegEx The regular expression to validate against.
 * @param fieldName The display name of the field being validated.
 */
function validateAllowedCharacters(field, validCharacterRegEx, fieldName) {

	if (isDefined(field) && hasContent(field)) {

		var value = field.value;

		var strippedValue = value.replace(validCharacterRegEx, '')

		if (strippedValue.length > 0) {

			var errorMessage = '{0} has invalid character(s).  \'{1}\' {2} not valid.';

			// Add field name to error message
			errorMessage = errorMessage.replace(/\{0\}/, fieldName);
		
			// Change message verb to 'is' or 'are' depending on number of invalid characters.
			errorMessage = errorMessage.replace(/\{2\}/, strippedValue.length > 1 ? 'are' : 'is');  
			
			// Add invalid characters to message string
			errorMessage = errorMessage.replace(/\{1\}/, strippedValue);
			
			handleError(field, errorMessage);
	    return false;
		}
	}

	return true;
}

/**
 * Validates that the specified field only contains alpha-numeric characters.
 * If the validation fails:
 * Focus is returned to the field and an alert message is shown.
 *
 * @param field The field to validate.
 * @param message The message to display in an alert if the validation fails.
 */
function validateAlphaNumeric(field, message) {

	if (isDefined(field)) {

    var value = field.value;
		for(var i=0; i<value.length; i++) {
		  var character = value.charAt(i);
		  var code = character.charCodeAt(0);
		  if(!(((code > 47) && (code < 59))
	  		 || ((code > 64) && (code < 91))
		  	 || ((code > 96) && (code < 123)))) {

				handleError(field, message);
		  	return false;
		  }
		}
	}
  return true;
}

/**
 * Validates that the specified field only contains numeric characters.
 * If the validation fails:
 * Focus is returned to the field and an alert message is shown.
 *
 * @param field The field to validate.
 * @param message The message to display in an alert if the validation fails.
 */
function validateInteger(field, message) {

	if (isDefined(field)) {

	  if(!isDecimalDigits(field.value)) {

			handleError(field, message);
  		return false;
		}
	}
  return true;
}
	
/**
 * Post processes the specified percent field to the specified %.  Any grouping separators will be stripped.
 */
 function postProcessPercentageField(field, fixed) {

	 if (isDefined(field) && trim(field.value).length > 0) {
	 
	 		field.value = parseFloat(deformatAmount(field.value)).toFixed(fixed);
	 } 
 }	
 
/**
 * Post processes the specified amount field by formatting with grouping separators.
 */
 function postProcessAmountField(field) {

	 if (isDefined(field) && trim(field.value).length > 0) {
		 field.value = formatAmount(field.value, false, true);
	 } 
 }	
 
/**
 * Pre processes the specified field by trimming it.
 */
function preProcessField(field) {

  if (isDefined(field)) {

	  field.value = trim(field.value);
	} 
}

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1800;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}


function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   } 
   return this
}

function isDate(dtStr, showAlert){
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strMonth=dtStr.substring(0,pos1)
	var strDay=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		if (showAlert) {
			alert("The date format should be : mm/dd/yyyy")
		}
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		if (showAlert) {
			alert("Please enter a valid month")
		}
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		if (showAlert) {
			alert("Please enter a valid day")
		}
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		if (showAlert) {
			alert("Please enter a valid 4 digit year")
		}
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		if (showAlert) {
			alert("Please enter a valid date")
		}
		return false
	}
return true
}

function validateMMddYYYY(field, showAlert){

	if (isDefined(field) && hasContent(field)) {
		return isDate(field.value, showAlert);
	} else {
		return true;
	}
}