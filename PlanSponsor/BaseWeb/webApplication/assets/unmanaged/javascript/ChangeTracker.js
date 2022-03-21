function ChangeTracker() {

  this.DEBUG = false;
  
  this.DEBUG_CHANGED_ONLY = false;
  
  this.elements = new Array();

  this.trackElement = function(name, oldValue) {
    this.elements.push(new Element(name, oldValue));
  }

  this.getOldValue = function(name) {
    for (var i = 0; i < this.elements.length; i++) {
      var element = this.elements[i];
      if (element.name == name) {
        return element.oldValue;
      }
    }
    return null;
  }

  this.isNumeric = function(value) {
    var strippedValue = value.replace(/[0-9\.\,]+/g, '');
    if (strippedValue.length > 0) {
      return false;
    }
    return true;
  }

  this.deformatNumber = function(value) {
    return value.toString().replace(/[,]/g, '');
  }

  this.hasChanged = function() {

    var changed = false;

    for (var i = 0; i < this.elements.length; i++) {
      var element = this.elements[i];

      var htmlElement = document.getElementsByName(element.name)[0];

      if (htmlElement == null) {
        if (this.DEBUG || this.DEBUG_CHANGED_ONLY) {
          alert("Element removed: " + element.name);
        }
        changed = true;
        continue;
      }

	  if (htmlElement.disabled) {
	    // skip disabled element.
	    continue;
	  }

   	  /*
       * Handle special case for a radio button. We are only interested
       * in checked options.
       */
      if (htmlElement.type == "radio") {
        var htmlElements = document.getElementsByName(element.name);
        for (var j = 0; j < htmlElements.length; j++) {
          if (htmlElements[j].checked) {
            htmlElement = htmlElements[j];
            break;
          }
        }
      }

      var oldValue = element.oldValue;

      var newValue = htmlElement.value;
	  // correct for the special case of unchecked radio
	  if (htmlElement.type == "radio" && !htmlElement.checked) {
			newValue = "";
	  }

	  // correct for the special case of checkbox
	  if (htmlElement.type == "checkbox") {
		if (htmlElement.checked)
			newValue = "true";
		else
			newValue = "false";
	  }

      if (oldValue != null && oldValue != '' && this.isNumeric(oldValue)) {
        oldValue = this.deformatNumber(oldValue);
      }

      if (newValue != null && newValue != '' && this.isNumeric(newValue)) {
        newValue = this.deformatNumber(newValue);
      }

      if (this.DEBUG) {
        alert("[" + element.name + "] new: [" + newValue + "] old: [" + oldValue + "]");
      }	  

      if (newValue != oldValue) {
        if (newValue == '' && oldValue == null) {
          continue;
        }
        /*
         * special case for check box, we allow true/false and Y/N
         */
        if (htmlElement.type == "checkbox") {
			if (newValue == "false" && (oldValue == '' || oldValue ==  'N' || oldValue == null)) {
				continue;
			}
			if (newValue == "true" && oldValue == 'Y') {
				continue;
			}
        }
        if (this.DEBUG || this.DEBUG_CHANGED_ONLY) {
          alert("Changed: " + htmlElement.name + " new: [" + newValue + "] old: [" + oldValue + "]");
        }
        changed = true;
      }
    }
    return changed;
  }
}

function Element(name, oldValue) {
  this.name = name;
  this.oldValue = oldValue;
}

var changeTracker = new ChangeTracker();
