//JS for IPS Manager pages

function isFormChanged() {
	  return changeTracker.hasChanged();
}

registerTrackChangesFunction(isFormChanged);

function selectAll(oldValue, newValue, elementId) {

	var ele = document.getElementById("showDate");
	ele.style.display = "block";

	var formElements = window.document.getElementById("ipsAndReviewDetailsForm").elements;
	var formElement;
	var radioArray = [];

	for (var i = 0, j = 0; i < formElements.length; i++) {
		formElement = formElements.item(i);			
		if (formElement.type == "radio" && formElement.id == elementId+j) {			
			radioArray[j] = formElement;
			var tiedFlagScript = radioArray[j].attributes['onclick'].value;	
			var tiedFlag = tiedFlagScript.substr(23, 4);					
			if(radioArray[j].value != oldValue && !formElement.disabled && tiedFlag !='true'){
				radioArray[j].value = newValue;
				radioArray[j].checked = true;							
			}
		++j;
		}	
	}
}

function showIATDate(isTiedFundAvailable, topRankedFundCounter, elementId) {
	var ele = document.getElementById("showDate");
	ele.style.display = "block";
	
	if(isTiedFundAvailable == true) {
	
		var formElements = window.document.getElementById("ipsAndReviewDetailsForm").elements;		
		var nextFormElement;		
		
		element = document.getElementById("approveIndicator"+elementId);
		if(topRankedFundCounter != 0) {
			elementId++;							
		} else {	
			elementId--;
		}
		
		nextFormElement = document.getElementById("ignoreIndicator"+elementId);
		
		if(element.value == 'A' && !nextFormElement.disabled){			
			nextFormElement.value = 'I';
			nextFormElement.checked = true;
		}		
	}
}

function validateDate(value) {
    if (value.length==0) {
       return true;
    }
 	return getDate(value) != null;
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