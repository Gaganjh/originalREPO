/**
 * This class manages the options of two select drop downs and make
 * sure that the fromSelect is always less than or equal to the
 * toSelect.
 *
 * @author Charles Chan
 */
function SelectRangeLimiter(fromSelectName, toSelectName) {

  this.isValidRange = function() {
    var fromOption =
      this.fromSelectObj.options[this.fromSelectObj.selectedIndex];
    var toOption =
      this.toSelectObj.options[this.toSelectObj.selectedIndex];
    if (fromOption.value > toOption.value) {
      return false;
    } else {
      return true;
    }
  }

  this.limitToSelect = function() {
    this.limitSelect(this.fromSelectObj,
                     this.toSelectObj);
  }

  this.limitFromSelect = function() {
    this.limitSelect(this.toSelectObj,
                     this.fromSelectObj);
  }

  /**
   * This method limits the select2Obj options by the selection made
   * for select1Obj. The rule is such that select1 options must be
   * less than or equal to select2 options.
   *
   * @param select1Obj The select object where selection was made.
   * @param select2Obj The select object where restriction has to be
   *                   applied.
   */
  this.limitSelect = function(select1Obj,
                              select2Obj) {

    /*
     * If we are in the process of limiting one select, return immediately.
     * This typically happens when we fire the onchange event to select2Obj
     * at the end of this method.
     */
    if (this.inProgress) {
      return;
    }

    /*
     * Obtain the selected option for select1Obj.
     */
    var selectedOption =
      select1Obj.options[select1Obj.selectedIndex];

    var select2SelectedOption =
      select2Obj.options[select2Obj.selectedIndex];

    /*
     * Remove all options from select2Obj.
     */
    select2Obj.options.length = 0;

    /*
     * Identify where the original select2 options are.
     */
    var select2Options = null;

    if (select1Obj == this.fromSelectObj) {
      select2Options = this.toSelectOptions;
    } else {
      select2Options = this.fromSelectOptions;
    }

    /*
     * For each options, checks which one applies and adds it
     * to the select2Obj. The rule is such that select1 options must
     * be less than or equal to select2 options.
     */
    for (var i = 0; i < select2Options.length; i++) {
      if (select1Obj == this.toSelectObj) {
        /*
         * If you are changing the from range select box,
         * make sure the to select value is greater than the
         * new from select value.
         */
        if (select2Options[i].value > selectedOption.value) {
          continue;
        }
      } else {
        /*
         * If you are changing the to range select box,
         * make sure the from select value is less than the
         * new to select value.
         */
        if (select2Options[i].value < selectedOption.value) {
          continue;
        }
      }
      select2Obj.options[select2Obj.options.length] = select2Options[i];

      /*
       * Preserve the original selection as much as possible.
       */
      if (select2Options[i] == select2SelectedOption) {
        select2Obj.selectedIndex = select2Obj.options.length - 1;
      }
    }

	/*
	 * Fire onChange event for select2Obj. Make sure we don't come back
	 * to the same method or an endless recursion would occur.
	 */
    this.inProgress = true;
    select2Obj.onchange();
    this.inProgress = false;
  }

  /**
   * Copies the sourc earray into the target array.
   */
  this.copyArray = function(targetArray, sourceArray) {
    for (var i = 0; i < sourceArray.length; i++) {
      targetArray[i] = sourceArray[i];
    }
  }

  /*
   * Initialization methods.
   * We cannot use getElementById because Struts's select tag doesn't
   * output an ID attribute.
   */

  this.fromSelectObj = document.getElementsByName(fromSelectName)[0];
  this.toSelectObj = document.getElementsByName(toSelectName)[0];
  this.inProgress = false;

  /*
   * The select object can be null if we're in print friendly mode.
   */
  if (this.fromSelectObj != null) {
    this.fromSelectOptions = new Array(this.fromSelectObj.options.length);
    this.toSelectOptions = new Array(this.toSelectObj.options.length);

    this.copyArray(this.fromSelectOptions, this.fromSelectObj.options);
    this.copyArray(this.toSelectOptions, this.toSelectObj.options);
  }
}
