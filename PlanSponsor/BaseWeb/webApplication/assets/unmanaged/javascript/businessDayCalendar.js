// Title: Tigra Calendar
// URL: http://www.softcomplex.com/products/tigra_calendar/
// Version: 3.2 (American date format)
// Date: 10/14/2002 (mm/dd/yyyy)
// Note: Permission given to use this script in ANY kind of applications if
//    header lines are left unchanged.
// Note: Script consists of two files: calendar?.js and calendar.html
// This is a modified version to support additional functionality
// modified by Tony Tomasone

var xPosition;
var yPosition;

if (document.layers) document.captureEvents(Event.MOUSEMOVE);
    document.onmousemove=ntrack;

function ntrack(e) {

    var x;
    var y;
    if (isNaN(window.screenX)) {
    	x=document.body.scrollLeft+window.screenLeft;
    	y=document.body.scrollTop+window.screenTop;
    } else {
    	x=window.screenX+(window.outerWidth-window.innerWidth)-window.pageXOffset;
    	y=window.screenY+(window.outerHeight-24-window.innerHeight)-window.pageYOffset;
    }

	if (document.getElementById) {
		if (isNaN(window.screenX)) {
			x=document.body.scrollLeft+window.screenLeft;
			y=document.body.scrollTop+window.screenTop;
		} else {
			x=window.screenX+(window.outerWidth-window.innerWidth)-window.pageXOffset;
			y=window.screenY+(window.outerHeight-24-window.innerHeight)-window.pageYOffset;
		}
	} else if (document.all) {
		x=document.body.scrollLeft+window.screenLeft;
		y=document.body.scrollTop+window.screenTop;
	} else if (document.layers) {
		x=window.screenX+(window.outerWidth-window.innerWidth)-window.pageXOffset;
		y=window.screenY+(window.outerHeight-24-window.innerHeight)-window.pageYOffset;
	}

	if (e) {
	  xPosition = e.pageX+x
	  yPosition = e.pageY+y;
	  if (screen) {
	      if ((xPosition+230) >= screen.width ) {
		    xPosition = screen.width - 230;
	      } else if (xPosition <= 0) {
		    xPosition=10;
	      }
	      if ((yPosition+230) >= screen.height) {
		    yPosition = screen.height - 230;
	      } else if (yPosition <= 0) {
		    yPosition=10;
	      }
          }

	} else if (event) {	
	  xPosition = event.x+x;
	  yPosition = event.y+y;
	  if (screen) {
	      if ((xPosition+230) >= screen.width ) {
		    xPosition = screen.width - 230;
	      } else if (xPosition <= 0) {
		    xPosition=10;
	      }
	      if ((yPosition+230) >= screen.height) {
		    yPosition = screen.height - 230;
	      } else if (yPosition <= 0) {
		    yPosition=10;
	      }
          }

	} else {
	  xPosition=screen.width/2;
	  yPosition=screen.height/2;
	}

}

// if two digit year input dates after this year considered 20 century.
var NUM_CENTYEAR = 30;
// is time input control required by default
var BUL_TIMECOMPONENT = false;
// are year scrolling buttons required by default
var BUL_YEARSCROLL = true;

var calendars = [];
var RE_NUM = /^\-?\d+$/;

function calendar (obj_target, startTime, endTime, validDates, defaultTime) {
	
	// assigning methods
	this.gen_date = cal_gen_date;
	this.gen_time = cal_gen_time;
	this.gen_tsmp = cal_gen_tsmp;
	this.prs_date = cal_prs_date;
	this.prs_date_no_alert = cal_prs_date_no_alert;
	this.prs_time = cal_prs_time;
	this.prs_tsmp = cal_prs_tsmp;
	this.prs_tsmp_no_alert = cal_prs_tsmp_no_alert;
	this.popup    = cal_popup;
	this.popup_no_alert    = cal_popup_no_alert;
	this.isValidMarketDate = cal_isValidMarketDate;
	this.validDates = validDates;

	// validate input parameters
	if (!obj_target)
		return cal_error("Error calling the calendar: no target control specified");
	if (obj_target.value == null)
		return cal_error("Error calling the calendar: parameter specified is not valid target control");

	// Start Time and End Time Validation Changes	
	if (startTime == null) 
		this.dt_start = new Date();
	else 
		this.dt_start = new Date(startTime);
	// default to one year from now
	if (endTime == null) {
		this.dt_end = new Date(this.dt_start);
		this.dt_end.setYear(this.dt_start.getYear()+1);
	}
	else 
		this.dt_end = new Date(endTime);

	// Start Time and End Time Validation Changes	
	if (defaultTime == null) 
		this.dt_default = new Date();
	else 
		this.dt_default = new Date(defaultTime);

	this.target = obj_target;
	this.time_comp = BUL_TIMECOMPONENT;
	this.year_scroll = BUL_YEARSCROLL;
	
	// register in global collections
	this.id = calendars.length;
	calendars[this.id] = this;
}


function cal_popup (str_datetime, validation) {

	this.dt_current = this.prs_tsmp(str_datetime ? str_datetime : this.target.value, validation);
	this.dt_highlight = this.dt_default;
	if (!this.dt_current) return;

	var obj_calwindow = window.open(
		'/global/calendar.jsp?datetime=' + this.dt_current.valueOf()+ '&id=' + this.id + '&starttime='+this.dt_start.valueOf()+ '&endtime='+this.dt_end.valueOf()+'&defaulttime='+this.dt_highlight.valueOf(),
		'Calendar', 'width=200,height='+(this.time_comp ? 215 : 190)+
		',status=no,resizable=no,top='+yPosition+',left='+ xPosition +',dependent=yes,alwaysRaised=yes,menubar=no,toolbar=no,location=no,directories=no'
	);
	obj_calwindow.opener = window;
	obj_calwindow.focus();
}
function cal_popup_no_alert(str_datetime, validation) {

	this.dt_current = this.prs_tsmp_no_alert(str_datetime ? str_datetime : this.target.value, validation);
	this.dt_highlight = this.dt_default;
	if (!this.dt_current) return;

	var obj_calwindow = window.open(
		'/global/calendar.jsp?datetime=' + this.dt_current.valueOf()+ '&id=' + this.id + '&starttime='+this.dt_start.valueOf()+ '&endtime='+this.dt_end.valueOf()+'&defaulttime='+this.dt_highlight.valueOf(),
		'Calendar', 'width=200,height='+(this.time_comp ? 215 : 190)+
		',status=no,resizable=no,top=200,left=200,dependent=yes,alwaysRaised=yes,menubar=no,toolbar=no,location=no,directories=no'
	);
	obj_calwindow.opener = window;
	obj_calwindow.focus();

}

// timestamp generating function
function cal_gen_tsmp (dt_datetime) {
	return(this.gen_date(dt_datetime) + ' ' + this.gen_time(dt_datetime));
}

// date generating function
function cal_gen_date (dt_datetime) {
	return (
		(dt_datetime.getMonth() < 9 ? '0' : '') + (dt_datetime.getMonth() + 1) + "/"
		+ (dt_datetime.getDate() < 10 ? '0' : '') + dt_datetime.getDate() + "/"
		+ dt_datetime.getFullYear()
	);
}
// time generating function
function cal_gen_time (dt_datetime) {
	return (
		(dt_datetime.getHours() < 10 ? '0' : '') + dt_datetime.getHours() + ":"
		+ (dt_datetime.getMinutes() < 10 ? '0' : '') + (dt_datetime.getMinutes()) + ":"
		+ (dt_datetime.getSeconds() < 10 ? '0' : '') + (dt_datetime.getSeconds())
	);
}

// timestamp parsing function
function cal_prs_tsmp (str_datetime, validation) {

// if no parameter specified return current timestamp
//	if (!str_datetime)
//		return (new Date());

	// if positive integer treat as milliseconds from epoch
//	if (RE_NUM.exec(str_datetime))
//		return new Date(str_datetime);
		
	// else treat as date in string format
	var arr_datetime = str_datetime.split(' ');

	if (arr_datetime == 0) {
		alert ("Please enter dates in the format mm/dd/yyyy or select a date from the calendar.");
		parseResult = this.dt_default;
		this.target.value = this.gen_date(parseResult);
		return null;
	}

//	var arr_date = arr_datetime[0].split('/');
//	if(isNaN(arr_date[0]) || isNaN(arr_date[1]) || isNaN(arr_date[2])) {
//		return alert ("Invalid date format: '" + arr_date + "'.\nFormat accepted is mm/dd/yyyy.");
//		return null;
//	}
	
	var parseResult = this.prs_date(arr_datetime[0], validation);
	
	if (!parseResult) {
		parseResult = this.dt_default;
		this.target.value = this.gen_date(parseResult);
	}

	return this.prs_time(arr_datetime[1], parseResult);
}
// timestamp parsing function
function cal_prs_tsmp_no_alert (str_datetime, validation) {

	// else treat as date in string format
	var arr_datetime = str_datetime.split(' ');

	if (arr_datetime == 0) {
		parseResult = this.dt_default;
		this.target.value = this.gen_date(parseResult);
		return null;
	}
	var parseResult = this.prs_date_no_alert(arr_datetime[0], validation);
	
	if (typeof(parseResult) == "string"  /*meaning error message*/) {
		parseResult = this.dt_default;
		this.target.value = this.gen_date(parseResult);
	}

	return this.prs_time(arr_datetime[1], parseResult);
}


// date parsing function
function cal_prs_date (str_date, validation) {
	var arr_date = str_date.split('/');

	if (arr_date.length != 3 || arr_date[2].length != 4) return alert ("Please enter dates in the format mm/dd/yyyy or select a date from the calendar.");
	if (!arr_date[1]) return alert ("Invalid date format: '" + str_date + "'.\nNo day of month value can be found.");
	if (!RE_NUM.exec(arr_date[1])) return alert ("Invalid day of month value: '" + arr_date[1] + "'.\nValue must be numeric.");
	if (!arr_date[0]) return alert ("Invalid date format: '" + str_date + "'.\nNo month value can be found.");
	if (!RE_NUM.exec(arr_date[0])) return alert ("Invalid month value: '" + arr_date[0] + "'.\nValue must be numeric.");
	if (!arr_date[2]) return alert ("Invalid date format: '" + str_date + "'.\nNo year value can be found.");
	if (!RE_NUM.exec(arr_date[2])) return alert ("Invalid year value: '" + arr_date[2] + "'.\nValue must be numeric.");

	var dt_date = new Date();
	dt_date.setDate(1);

	if (arr_date[0] < 1 || arr_date[0] > 12) return alert ("Invalid month value: '" + arr_date[0] + "'.\nAllowed range is 01-12.");
	dt_date.setMonth(arr_date[0]-1);
	 
	if (arr_date[2] < 100) arr_date[2] = Number(arr_date[2]) + (arr_date[2] < NUM_CENTYEAR ? 2000 : 1900);
	dt_date.setFullYear(arr_date[2]);

	var dt_numdays = new Date(arr_date[2], arr_date[0], 0);
	dt_date.setDate(arr_date[1]);
	if (dt_date.getMonth() != (arr_date[0]-1)) return alert ("Invalid day of month value: '" + arr_date[1] + "'.\nAllowed range is 01-"+dt_numdays.getDate()+".");

	if (validation && (
		dt_date.valueOf() < this.dt_start.valueOf()  ||
		dt_date.valueOf() > this.dt_end.valueOf()) 
	) return alert ("Invalid date selected: '" + str_date + "'.\nAllowed range is " +(this.dt_start.getMonth() +1) + "/" + this.dt_start.getDate() + "/" + this.dt_start.getFullYear()	+" to "	+(this.dt_end.getMonth() +1) + "/" + this.dt_end.getDate() + "/" + this.dt_end.getFullYear()	+ ".");
	
	if (validation &&
		!this.isValidMarketDate(dt_date)
	) return alert ("Invalid date selected: '" + str_date + "'.\nPick a valid NYSE investment date.");
	
	
	
	return (dt_date)
}
function cal_prs_date_no_alert (str_date, validation) {
	var arr_date = str_date.split('/');

	if (arr_date.length != 3 || arr_date[2].length != 4) return "Please enter dates in the format mm/dd/yyyy or select a date from the calendar.";
	if (!arr_date[1]) return "Invalid date format: '" + str_date + "'.\nNo day of month value can be found.";
	if (!RE_NUM.exec(arr_date[1])) return "Invalid day of month value: '" + arr_date[1] + "'.\nValue must be numeric.";
	if (!arr_date[0]) return "Invalid date format: '" + str_date + "'.\nNo month value can be found.";
	if (!RE_NUM.exec(arr_date[0])) return "Invalid month value: '" + arr_date[0] + "'.\nValue must be numeric.";
	if (!arr_date[2]) return "Invalid date format: '" + str_date + "'.\nNo year value can be found.";
	if (!RE_NUM.exec(arr_date[2])) return "Invalid year value: '" + arr_date[2] + "'.\nValue must be numeric.";

	var dt_date = new Date();
	dt_date.setDate(1);

	if (arr_date[0] < 1 || arr_date[0] > 12) return "Invalid month value: '" + arr_date[0] + "'.\nAllowed range is 01-12.";
	dt_date.setMonth(arr_date[0]-1);
	 
	if (arr_date[2] < 100) arr_date[2] = Number(arr_date[2]) + (arr_date[2] < NUM_CENTYEAR ? 2000 : 1900);
	dt_date.setFullYear(arr_date[2]);

	var dt_numdays = new Date(arr_date[2], arr_date[0], 0);
	dt_date.setDate(arr_date[1]);
	if (dt_date.getMonth() != (arr_date[0]-1)) return "Invalid day of month value: '" + arr_date[1] + "'.\nAllowed range is 01-"+dt_numdays.getDate()+".";

	if (validation && (
		dt_date.valueOf() < this.dt_start.valueOf()  ||
		dt_date.valueOf() > this.dt_end.valueOf()) 
	) return "Invalid date selected: '" + str_date + "'.\nAllowed range is " +(this.dt_start.getMonth() +1) + "/" + this.dt_start.getDate() + "/" + this.dt_start.getFullYear()	+" to "	+(this.dt_end.getMonth() +1) + "/" + this.dt_end.getDate() + "/" + this.dt_end.getFullYear()	+ ".";
	
	if (validation &&
		!this.isValidMarketDate(dt_date)
	) return "Invalid date selected: '" + str_date + "'.\nPick a valid NYSE investment date.";
	
	
	
	return (dt_date)
}

// time parsing function
function cal_prs_time (str_time, dt_date) {

	if (!dt_date) return null;
	var arr_time = String(str_time ? str_time : '').split(':');

	if (!arr_time[0]) dt_date.setHours(0);
	else if (RE_NUM.exec(arr_time[0])) 
		if (arr_time[0] < 24) dt_date.setHours(arr_time[0]);
		else return cal_error ("Invalid hours value: '" + arr_time[0] + "'.\nAllowed range is 00-23.");
	else return cal_error ("Invalid hours value: '" + arr_time[0] + "'.\nValue must be numeric.");
	
	if (!arr_time[1]) dt_date.setMinutes(0);
	else if (RE_NUM.exec(arr_time[1]))
		if (arr_time[1] < 60) dt_date.setMinutes(arr_time[1]);
		else return cal_error ("Invalid minutes value: '" + arr_time[1] + "'.\nAllowed range is 00-59.");
	else return cal_error ("Invalid minutes value: '" + arr_time[1] + "'.\nValue must be numeric.");

	if (!arr_time[2]) dt_date.setSeconds(0);
	else if (RE_NUM.exec(arr_time[2]))
		if (arr_time[2] < 60) dt_date.setSeconds(arr_time[2]);
		else return cal_error ("Invalid seconds value: '" + arr_time[2] + "'.\nAllowed range is 00-59.");
	else return cal_error ("Invalid seconds value: '" + arr_time[2] + "'.\nValue must be numeric.");

	dt_date.setMilliseconds(0);
	return dt_date;
}

function cal_isValidMarketDate(dateToCheck) {
	if(!dateToCheck) return false;
	dateToCheck.setHours(0);
	dateToCheck.setMinutes(0);
	dateToCheck.setSeconds(0);
	dateToCheck.setMilliseconds(0);
	var valid = false;
	for(var i=0; i<this.validDates.length; i++) {
		if(dateToCheck.valueOf() == this.validDates[i].valueOf()) 
			valid = true;
		//alert(validDates[i]);
	}
	return valid;
}

function cal_error (str_message) {
	alert (str_message);
	return null;
}
