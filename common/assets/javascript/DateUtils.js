function DateUtils() {

  /*
   * Return code for the validateDate method. See validateDate
   * method documentation for details.
   */  
  this.OK = "OK";
  this.FORMAT_ERROR = "FORMAT_ERROR";
  this.INVALID_MONTH = "INVALID_MONTH";
  this.INVALID_DAY_IN_MONTH = "INVALID_DAY_IN_MONTH";
  this.INVALID_DATE = "INVALID_DATE";
  this.INVALID_YEAR = "INVALID_YEAR";

  /**
   * Generic handler for date icon clicked
   */ 
  this.invokeCalendar = function(event, fieldId) {
    // Retrieve the field
    var field = document.getElementById(fieldId);

    // Pre-Validate date and blank if not valid
    if (typeof(field.value) != 'undefined' && field.value != '') {
      var rc = this.validateDate(field.value);
      if (rc != "OK") {
        field.value = "";
      }
    }
    
    // Popup calendar
    Calendar = new calendar(field);
    Calendar.year_scroll = true;
    Calendar.time_comp = false;
   
    // Modify calendar position to be slightly above and to right of mouse click
    yPosition = event.screenY - 150;
    xPosition = event.screenX + 80;
    Calendar.popup();
  }

  this.parseDate = function(dtStr){
    var dtCh= "/";
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
    var dateObj = new Date();
    dateObj.setFullYear(year, month - 1, day);
    return dateObj;
  }
  
  /**
   * Checks if the given date string is valid.
   * Returns
   * OK: if the date is good.
   * FORMAT_ERROR: if there is a format error (error message would be: The date format should be : mm/dd/yyyy)
   * INVALID_MONTH: if the month is invalid
   * INVALID_DAY_IN_MONTH: if the date is invalid w.r.t. the month.
   * INVALID_DATE: if the date is invalid
   * INVALID_YEAR: if the year is invalid
   */
  this.validateDate = function(dtStr){
    var dtCh= "/";
    var minYear=1800;
    var maxYear=2100;
    var daysInMonth = this.__newDaysArray(12);
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
      return this.FORMAT_ERROR;
    }
    if (strMonth.length<1 || month<1 || month>12){
      return this.INVALID_MONTH;
    }
    if (strDay.length<1 || day<1 || day>31 || (month==2 && day>this.__daysInFebruary(year)) || day > daysInMonth[month]){
      return this.INVALID_DAY_IN_MONTH;
    }
    if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
      return this.INVALID_YEAR;
    }
    if (dtStr.indexOf(dtCh,pos2+1)!=-1 || this.__isInteger(this.__stripCharsInBag(dtStr, dtCh))==false){
      return this.INVALID_DATE;
    }
    return this.OK;
  }

  this.daysBetween = function(date1, date2) {
    //Set 1 day in milliseconds
    var one_day=1000*60*60*24;

    //Calculate difference btw the two dates, and convert to days
    return Math.ceil((date2.getTime()-date1.getTime())/one_day);
  }

  this.__daysInFebruary = function(year){
    // February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
  }

  this.__newDaysArray = function(n) {
    var result = new Array();
    for (var i = 1; i <= n; i++) {
      result[i] = 31
      if (i==4 || i==6 || i==9 || i==11) {
        result[i] = 30
      }
      if (i==2) {
        result[i] = 29
      }
    } 
    return result;
  }
  
  this.__isInteger = function(s){
    var i;
    for (i = 0; i < s.length; i++){   
      // Check that current character is number.
      var c = s.charAt(i);
      if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
  }

  this.__stripCharsInBag = function(s, bag){
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
}

var dateUtils = new DateUtils();
