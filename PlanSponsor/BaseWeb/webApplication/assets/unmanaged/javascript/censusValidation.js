/*
  main entry function for validation
  field:  form input field
  validators:  an array or one validator function
  messages:    an array or one error message for the validation failure
*/
function validateField(field, validators, messages, blankFieldIfInvalid) {
    // not an array
    if (validators.constructor.toString().indexOf("Array") == -1) {
        validators = new Array(validators);
        messages = new Array(messages);
    }
    for (var i=0; i < validators.length; i++) {
	    if (!validators[i](field.value)) {
		   alert(messages[i]);
		   setTimeout(function(){field.focus(); field.select();},10);
		   if (blankFieldIfInvalid) {
		       field.value = '';
		   }
	       return false;
	    } 
    }
    return true;    
}

/**
 * Trims the whitespace from the specified string.
 */
function trim(string) {
  if(string.charAt(0) == " ") {
    string = trim(string.substring(1));
  }
  if (string.charAt(string.length-1) == " "){
    string = trim(string.substring(0, string.length - 1));
  }
  
  return string;
}

function validateSsn(field, message) {
   if (!isAllDigits(field.value)) {
      alert(message);
      return false;
   } else {
     return true;
   }
}

function validateInteger(value) {
   if (value.length == 0) {
      return true;
   }
   if (value.charAt(0) == '-') {
   	  value = value.substring(1);
   }
   var valid = isAllDigits(value);
   if (valid) {
       var v = parseInt(value);
       if (isNaN(v)) {
          return false;
       } else {
         return true;
       }
   } else {
      return false;
   }
}

function validatePercentage(value) {
    if (value.length == 0) {
       return true;
    }
    // remove '.' before checking digits
    var tempArray = value.split('.');
    // check if more than one '.' was found
    if (tempArray.length > 2) {
    	return false;
    }
    var joinedString= tempArray.join('');
    if (joinedString.charAt(0)=='-' || joinedString.charAt(0)=='+') {
        joinedString = joinedString.substring(1);
    }
    
    var bValid = true;
    
    if (!isAllDigits(joinedString)) {
        bValid = false;
    } else {
     var iValue = parseFloat(value);
     if (isNaN(iValue)) {
         bValid = false;
     } 
    }
    return bValid;
}

function validatePositive(value) {
    if (value.length == 0) {
       return true;
    }
     var iValue = parseFloat(value);
     if (isNaN(iValue) || iValue < 0.0) {
         return false;
     } else {
      return true;
     }
}

function validatePercRange(value) {
    if (value.length == 0) {
       return true;
    }
     var iValue = parseFloat(value);
     if (isNaN(iValue) || iValue > 100.0) {
         return false;
     } else if ( iValue > 0.0 && iValue < 0.5 ) {
     	 return false;
     } else {
      	 return true;
     } 
}

function validatePercentageRangeOf0Or1To100(value) {
    if (value.length == 0) {
       return true;
    }
     var iValue = parseFloat(value);
     if (isNaN(iValue)) {
         return false;
     } else if ( ( iValue == 0.0 ) || ( iValue >= 1.0 && iValue <= 100.0 ) ) {
         return true;
     } else {
         return false;
     } 
}

function validateZipCode1(value) {
   if (value.length == 0) {
       return true;
   }
   return isAllDigits(value) && value.length == 5;
}

function validateZipCode2(value) {
   if (value.length == 0) {
       return true;
   }
   return isAllDigits(value) && value.length == 4;
}

function validateDigits(value) {
   return isAllDigits(value);
}

function validateNumber(value) {
   if (value.length == 0) {
       return true;
    }
    // remove '.' before checking digits
    var tempArray = value.split('.');
    var joinedString= tempArray.join('');

    if (joinedString.charAt(0) == '-') {
	   	  joinedString = joinedString.substring(1);
   }
    
    if (isAllDigits(joinedString)) {
    	return true;
    } else {
    	return false;
    }    
}

/**
 From struts ...
*/
function isAllDigits(argvalue) {
    argvalue = argvalue.toString();
    var validChars = "0123456789";
    var startFrom = 0;
    
    for (var n = startFrom; n < argvalue.length; n++) {
        if (validChars.indexOf(argvalue.substring(n, n+1)) == -1) return false;
    }
    return true;
}

/**
  Validator for non printable ascii
*/
function validateNonPrintableAscii(value) {
      for (var i = 0; i < value.length; i++) {
        ch = value.charCodeAt(i);	
     	if (ch < 32 || ch > 126 || ch == 63) {
		   return false;
		}
      }  
      return true;
}


/*
  validator for email address
*/
// Validation Email from PSW's back end pattern
function validateEmailAddr(emailStr) {
       var pattern = new RegExp("^[a-zA-Z0-9\\-\\._']+@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9]+$");
       if (emailStr.length == 0) {
        return true;
       }
       var match = emailStr.match(pattern);
       if (match == null) {
	  return false;
       }
       return true; 
 }
 
 function validateDate(value) {
    if (value.length==0) {
       return true;
    }
 	return getDate(value) != null;
 }

 
 function validate1900OrAfterDate(value) {
    if (value.length==0) {
       return true;
    }
    var theDate = getDate(value); 
    if (theDate != null) {
       // Check that it's 1/1/1900 or later.
       return (theDate.getFullYear() >= 1900);
    } else {
       // Not a valid date.
       return false;
    }
 }
 
 var BirthDateYear = 15;
 
 function validateBirthDate(value) {
    if (value.length==0) {
       return true;
    }
    var date = getDate(value);
    if (date == null) {
       return false;
    }
    var currentDate = new Date();
    currentDate.setYear(currentDate.getYear()-BirthDateYear);   
    return date.getTime() < currentDate.getTime();
 }
 
 //  date format MM/dd/yyyy
 function getDate(value) {
 	var v = trim(value);
    var dateExp = new RegExp("(^\\d{1,2})/(\\d{1,2})/(\\d{4})$");
    var matches = dateExp.exec(v);
    // alert(matches);
    if ( matches == null) {   
       return null;
    }
    if (!isValidDate(matches[2], matches[1], matches[3])) {
       return null;
    }
	date = new Date();
	date.setFullYear(matches[3], matches[1]-1, matches[2]);
	date.setHours(0);
	date.setMinutes(0);
	date.setSeconds(0);
	date.setMilliseconds(0);
	return date;
 }
 
 // From struts 1.1,  
 function isValidDate(day, month, year) {
     if (month < 1 || month > 12) {
               return false;
     }
     if (day < 1 || day > 31) {
         return false;
     }
     if ((month == 4 || month == 6 || month == 9 || month == 11) &&
         (day == 31)) {
         return false;
     }
     if (month == 2) {
         var leap = (year % 4 == 0 &&
                    (year % 100 != 0 || year % 400 == 0));
         if (day>29 || (day == 29 && !leap)) {
             return false;
         }
     }
     if (year < 1000) {
     	return false;
     }
     
     return true;
}