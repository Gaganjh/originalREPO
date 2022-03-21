/**
 * This file contains the common utility methods for bd application.
 *
 * @author Ilamparithi
 * 
 */

var submitted = false;


function doProtectedSubmitBtn(frm,action,obj) {
	var hrefValue = null;
	if (obj != undefined && obj != null) {
		obj.disabled=true;
	}
	if(!submitted) {
		  submitted = true;
		  frm.action.value=action;
		  try {
			  frm.submit();
		  }
		  catch (e) {
			  obj.disabled=false;
	  		  submitted = false;
		  }
	}
	else {
		window.status = "Transaction already in progress. Please wait.";
	}
	return false;

}

/**
 * This method prevents the user from inadvertently resubmitting the form. (Either by double click or by 
 * clicking the submit button again before the completion of previous submit).
 *
 * @param {Form} form the form to be checked.
 * @param {String} action parameter to the action class.
 */
function doProtectedSubmit(frm,action,obj) {
	var hrefValue = null;
	if (obj != undefined && obj != null) {
		hrefValue = obj.getAttribute("href");
		obj.removeAttribute('href');
	}
	if(!submitted) {
		  submitted = true;
		  frm.action.value=action;
		  try {
			  frm.submit();
		  }
		  catch (e) {
			  // anything going bad, recover the button and allow submit
			  if (hrefValue != null) {
				  obj.setAttribute('href', hrefValue);
			  }
	  		  submitted = false;
		  }
	}
	else {
		window.status = "Transaction already in progress. Please wait.";
	}
	return false;

}

/**
 * This method shows a warning message before performing cancel action. 
 * 
 * @param {Form} form the form to be checked.
 * @param {String} message to be shown in the popup.
 * @param {Object} link object from where this method is called.
 */
function doRegistrationCancel(frm,msg,btn) {	
	var returnVal = confirm(msg);
	if(returnVal) {
		doProtectedSubmitBtn(frm, 'cancel',btn);
	}
	return false;
}

/**
 * This method determines whether a form is dirty or not. This in turn calls the formIsDirty method. 
 * If the action is save then we don't have to check for dirtiness. We can also optionally pass any Array(or)String
 * argument of excluded element names. These are elements for which we don't want to check dirtiness. One example is
 * 'action' hidden field which will change most of the time from its original value.
 *
 * @param {Form} frm the form to be checked.
 * @return {Boolean} <code>true</code> if the form is dirty, <code>false</code>
 *                   otherwise.
 */
function isDirty(frm) {
	var exclude = arguments[1];
	if(frm.action.value == 'save') {
		return false;
	}
	//No exclude argument passed
	if(exclude == undefined) {
		return formIsDirty(frm,new Array());
	}
	//exclude argument is an Array
	else if(exclude instanceof Array) {
		return formIsDirty(frm,exclude);
	}
	//exclude argument is a String
	else {
		return formIsDirty(frm,new Array(exclude));
	}
}

/** Determines if a form is dirty by comparing the current value of each element
 * with its default value.
 *
 * @param {Form} form the form to be checked.
 * @return {Boolean} <code>true</code> if the form is dirty, <code>false</code>
 *                   otherwise.
 */
function formIsDirty(form,exclude) {
	this.DEBUG = false;
	this.SELECT_DEBUG = false;
    for (var i = 0; i < form.elements.length; i++) {
        var element = form.elements[i];
        var type = element.type;
		if(!contains(exclude,element.name)) {
			if (type == "checkbox" || type == "radio") {
				if(this.DEBUG) {
					alert("Type: " + type + "\nName: " + element.name + "\nCurrent Value: " + element.checked + "\nDefault Value: " + element.defaultChecked);
				}
				if (element.checked != element.defaultChecked) {
					return true;
				}
			}
			else if (type == "hidden" || type == "password" || type == "text" ||
					 type == "textarea") {
				if(this.DEBUG) {
					alert("Type: " + type + "\nName: " + element.name + "\nCurrent Value: " + element.value + "\nDefault Value: " + element.defaultValue);
				}
				if (element.value != element.defaultValue) {
					return true;
				}
			}
			else if (type == "select-one" || type == "select-multiple") {
				for (var j = 0; j < element.options.length; j++) {
					if(this.SELECT_DEBUG) {
						alert("Type: " + type + "\nName: " + element.options[j].text + "\nCurrent Value: " + element.options[j].selected + "\nDefault Value: " + element.options[j].defaultSelected);
					}
					if (element.options[j].selected !=
						element.options[j].defaultSelected) {
						return true;
					}
				}
			}
		}
    }
    return false;
}

/**
 * This method determines whether a String is present in the given array.
 *
 * @param {Array} array of element names.
 * @param {String} name of the element.
 * @return {Boolean} <code>true</code> if the array contains name, <code>false</code>
 *                   otherwise.
 */
function contains(arr, name) {
  var i = arr.length;
  while (i--) {
    if (arr[i] == name) {
      return true;
    }
  }
  return false;
}

var frm;
var errMsg;

/**
 * Call back function for firm verification.
 *
 * @param o        - response object
 */
var callback_verifyFirmName = { 
    success : function(o) {
			try {
				values = YAHOO.lang.JSON.parse(o.responseText).ResultSet.Result;
			}
			catch (e) {
				return;
			}
		if(values.length == 0) {
			alert(errMsg);
		}
		else {
		    frm.selectedFirmId.value = values[0].firmId;
			frm.selectedFirmName.value = values[0].firmName; 
			frm.lastSelectedFirmName.value = values[0].firmName;
			addNewFirm(frm); 
		}
	}
};

/**
 * Generic function to make an AJAX request.
 *
 * @param firmName - firm name to be verified
 * @param form - form object used in the page
 */
function verifyFirmName(firmName, form, msg) {
	var actionPath = "/do/firmsearch/?action=verify&firmName=" + escape(firmName);
	frm = form;
	errMsg = msg;
	doAsyncRequest(actionPath, callback_verifyFirmName);
}

/**
 * Generic function to make an AJAX request.
 *
 * @param actionPath        - action url
 * @param callbackFunction  - name of the call-back object to handle the server response
 */
function doAsyncRequest(actionPath, callbackFunction) {
    // Make a request
	YAHOO.util.Connect.setForm(document.getElementById('contentRuleMaintenanceForm'));
    var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);
}



