var isNN = (navigator.appName.indexOf("Netscape")!=-1);

function autoTab(input,len, e) {
  var keyCode = (isNN) ? e.which : e.keyCode;
  var filter = (isNN) ? [0,8,9] : [0,8,9,16,17,18,37,38,39,40,46];

  if(input.value.length >= len && !containsElement(filter,keyCode)) {
    input.value = input.value.slice(0, len);
    for (var i = 1; i < input.form.length; i++) {
      var element = input.form[(getIndex(input)+i) % input.form.length];
      if (element.type != 'hidden' &&
          element.style.visibility != 'hidden' &&
          element.style.display != 'none' &&
          element.disabled!=true) {/* Fix for 6803 - Added condition*/
        element.focus();
        break;
      }
    }
  }
  return true;
}


function containsElement(arr, ele) {
  var found = false, index = 0;

  while(!found && index < arr.length)
    if(arr[index] == ele)
      found = true;
    else
      index++;
  return found;
}

function getIndex(input) {
  var index = -1, i = 0, found = false;

  while (i < input.form.length && index == -1)
    if (input.form[i] == input)index = i;
    else i++;
  return index;
}

var gSubmitButton;

function OnEnterSubmit(submitButtonName, submitButtonValue) {

  this.submitButtonName = submitButtonName;
  this.submitButtonValue = submitButtonValue;
  this.DEBUG = false;

  this.NetscapeEventHandler_KeyDown = function(e) {
    if (e.which == 13 &&
        e.target.type != 'textarea') {
      gSubmitButton.click();
      return false;
    }
    return true;
  }

  this.MicrosoftEventHandler_KeyDown = function() {
    if (event.keyCode == 13 &&
        event.srcElement.type != 'textarea') {
      gSubmitButton.click();
      return false;
    }
    return true;
  }

  this.findButton = function() {
    var buttons = document.getElementsByName(this.submitButtonName);
    for (var i = 0; i < buttons.length; i++) {

      if (this.DEBUG) {
        alert("[" + buttons[i].value + "] [" + this.submitButtonValue + "]");
      }

      if (buttons[i].value == this.submitButtonValue) {
        return buttons[i];
        break;
      }
    }
    return null;
  }

  this.install = function() {

    gSubmitButton = this.findButton();

    if (gSubmitButton == null) {
      alert("Button [" + this.submitButtonName + "] with value [" +
            this.submitButtonValue + "] is not found!");
    } else {
      var nav = window.Event ? true : false;
      if (nav) {
        window.captureEvents(Event.KEYDOWN);
        window.onkeydown = this.NetscapeEventHandler_KeyDown;
      } else {
        document.onkeydown = this.MicrosoftEventHandler_KeyDown;
      }
    }
  }
}


function setFocusOnFirstInputField(formName) {
	var theElements = document.forms[formName].elements;
	for (i = 0; i < theElements.length; i++){
		if ("hidden" != theElements[i].type.toLowerCase()) {
			theElements[i].focus();
			break;
		}
	}
}


var clicked = false;
function doPreSubmit() {
	if (!clicked) {
		clicked=true;
		return true;
	} else {
		window.status = "Transaction already in progress ... please wait.";
		return false;
	}
}

