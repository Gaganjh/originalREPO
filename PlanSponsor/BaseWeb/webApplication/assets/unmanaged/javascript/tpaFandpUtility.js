/**
 * Provides the utility methods to 
 * 		--> parse JSON object
 * 		--> create dynamic options for the drop-down
 *
 * @author ayyalsa
 */

/**
 * Parses the JSON object in the response to a JSON String
 *
 * @param	- connection object
 * @return 	- the native JavaScript representation of the JSON string
 */
function parseResponseToJSON(o) {
	try { 
			parsedObject = YAHOO.lang.JSON.parse(o.responseText); 
			if (parsedObject.sessionExpired) {
				//alert("session expired.... redirecting to login page");
				top.location.reload(true);
			}
			return parsedObject;
		} catch (x) { 
			//alert("JSON Parse failed!" + x	); 
			//alert (o.responseText);
			return "parseError"; 
		} 
}


/**
 * Creates the option for specified drop-down from the optionsArray object
 *
 * @param targetDropDown	-	the drop down object that needs to be populated. 
 * @param optionsArray		-	the array object which has the options to be populated. 
 */
function createOptions(targetDropDown, optionsArray){
	
	//clear out any current options
	targetDropDown.options.length = 0;

	//add new options using the values in the array
	for (var i = 0; i < optionsArray.length; i++) {
		
		var newOption = document.createElement("OPTION");
		targetDropDown.options.add(newOption);
		newOption.value = optionsArray[i].value;
		newOption.text = unescape(optionsArray[i].label);
	}
}


/**
 * Populates the array elements to the specified drop down. This function is used when there are more than
 * one drop down needs to be populated.
 *
 * @param dropDownArray		-	the drop down object that needs to be populated. If there are more than one drop-down to be
 *								populated, then pass this as array object.
 * @param optionsArray		-	the array object which has the options to be populated. If there are more than one drop-down to be
 *								populated, then make sure that you are passing the array object's elements as array
 */
function populateDropdown(dropDownArray, optionsArray) {

	// If the dropdownArray length > 1, then it means that there are more than one drop-down to be populated
	if (typeof dropDownArray != 'string' && dropDownArray.length > 1) {
	
		for (i=0; i < dropDownArray.length; i++) {
			createOptions(document.getElementById(dropDownArray[i]), optionsArray[dropDownArray[i]]);
		}
	
	} else {
		
		createOptions(document.getElementById(dropDownArray), optionsArray);
	}
}

/**
 * Creates the option for specified drop-down from the optionsArray object
 *
 * @param targetDropDown	-	the drop down object that needs to be populated. 
 * @param optionsArray		-	the array object which has the options to be populated. 
 */
function createOptionsForReport(dropDownArray, optionsArray){

	targetDropDown = document.getElementById(dropDownArray)	
	
	//clear out any current options
	targetDropDown.options.length = 0;
	
	//add new options using the values in the array
	for (var i = 0; i < optionsArray.length; i++) {
		//alert(optionsArray[i].value == 'N/A' ||  optionsArray[i].value == '');
		//alert(optionsArray[i].value);
		var newOption = document.createElement("OPTION");
		if(optionsArray[i].value == 'N/A' ||  optionsArray[i].value == '') {
			newOption.style.backgroundColor = "black";
			newOption.style.color = "white"; 
		}
		
		if( optionsArray[i].key.indexOf("?") != -1) {
		    optionsArray[i].key = optionsArray[i].key.replace('?', '-'); ;
		}
		
		targetDropDown.options.add(newOption);
		newOption.value = optionsArray[i].value;
		newOption.text = optionsArray[i].key;
	}
}