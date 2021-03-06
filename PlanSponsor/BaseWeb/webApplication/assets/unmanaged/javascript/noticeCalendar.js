//Title: Tigra Calendar
//URL: http://www.softcomplex.com/products/tigra_calendar/
//Version: 3.2 (European date format)
//Date: 10/14/2002 (mm/dd/yyyy)
//Note: Permission given to use this script in ANY kind of applications if
//header lines are left unchanged.
//Note: Script consists of two files: calendar?.js and calendar.html


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



//if two digit year input dates after this year considered 20 century.
var NUM_CENTYEAR = 30;
//is time input control required by default
var BUL_TIMECOMPONENT = false;
//are year scrolling buttons required by default
var BUL_YEARSCROLL = true;

var calendars = [];
var RE_NUM = /^\-?\d+$/;

function calendar(obj_target) {

	// assigning methods
	this.gen_date = cal_gen_date1;
	this.gen_time = cal_gen_time1;
	this.gen_tsmp = cal_gen_tsmp1;
	this.prs_date = cal_prs_date1;
	this.prs_time = cal_prs_time1;
	this.prs_tsmp = cal_prs_tsmp1;
	this.popup    = cal_popup1;

	// validate input parameters
	if (!obj_target)
		return cal_error(calErrorMessage);
	if (obj_target.value == null)
		return cal_error(calErrorMessage);
	this.target = obj_target;
	this.time_comp = BUL_TIMECOMPONENT;
	this.year_scroll = BUL_YEARSCROLL;

	// register in global collections
	this.id = calendars.length;
	calendars[this.id] = this;
}

function cal_popup1 (str_datetime) {
	this.dt_current = this.prs_tsmp(str_datetime ? str_datetime : this.target.value);
	if (!this.dt_current) return;
	var obj_calwindow = window.open(
			'/assets/unmanaged/html/calendar.html?datetime=' + this.dt_current.valueOf()+ '&id=' + this.id,
			'Calendar', 'width=200,height='+(this.time_comp ? 215 : 190)+
			',status=no,resizable=no,top='+yPosition+',left='+ xPosition +',dependent=yes,alwaysRaised=yes'
	);
	obj_calwindow.opener = window;
	obj_calwindow.focus();
}

//timestamp generating function
function cal_gen_tsmp1 (dt_datetime) {
	return(this.gen_date(dt_datetime) + ' ' + this.gen_time(dt_datetime));
}

//date generating function
function cal_gen_date1 (dt_datetime) {
	return (
			(dt_datetime.getMonth() < 9 ? '0' : '') + (dt_datetime.getMonth() + 1) + "/"
			+ (dt_datetime.getDate() < 10 ? '0' : '') + dt_datetime.getDate() + "/"
			+ dt_datetime.getFullYear()
	);

}
//time generating function
function cal_gen_time1 (dt_datetime) {
	return (
			(dt_datetime.getHours() < 10 ? '0' : '') + dt_datetime.getHours() + ":"
			+ (dt_datetime.getMinutes() < 10 ? '0' : '') + (dt_datetime.getMinutes()) + ":"
			+ (dt_datetime.getSeconds() < 10 ? '0' : '') + (dt_datetime.getSeconds())
	);
}

//timestamp parsing function
function cal_prs_tsmp1 (str_datetime) {
	// if no parameter specified return current timestamp
	if (!str_datetime)
		return (new Date());

	// if positive integer treat as milliseconds from epoch
	if (RE_NUM.exec(str_datetime))
		return new Date(str_datetime);

	// else treat as date in string format
	var arr_datetime = str_datetime.split(' ');
	return this.prs_time(arr_datetime[1], this.prs_date(arr_datetime[0]));
}

//date parsing function
function cal_prs_date1 (str_date) {

	var arr_date = str_date.split('/');

	if (arr_date.length != 3) return cal_error (calErrorMessage);
	if (!arr_date[1]) return cal_error (calErrorMessage);
	if (!RE_NUM.exec(arr_date[1])) return cal_error (calErrorMessage);
	if (!arr_date[0]) return cal_error (calErrorMessage);
	if (!RE_NUM.exec(arr_date[0])) return cal_error (calErrorMessage);
	if (!arr_date[2]) return cal_error (calErrorMessage);
	if (!RE_NUM.exec(arr_date[2])) return cal_error (calErrorMessage);
	if (arr_date[2] >9999) return cal_error (calErrorMessage);


	var dt_date = new Date();
	dt_date.setDate(1);

	if (arr_date[0] < 1 || arr_date[0] > 12) return cal_error (calErrorMessage);
	dt_date.setMonth(arr_date[0]-1);

	if (arr_date[2] < 100) arr_date[2] = Number(arr_date[2]) + (arr_date[2] < NUM_CENTYEAR ? 2000 : 1900);
	dt_date.setFullYear(arr_date[2]);

	var dt_numdays = new Date(arr_date[2], arr_date[0], 0);
	dt_date.setDate(arr_date[1]);
	if (dt_date.getMonth() != (arr_date[0]-1)) return alert (calErrorMessage);

	return (dt_date)


}

//time parsing function
function cal_prs_time1 (str_time, dt_date) {



	if (!dt_date) return null;
	var arr_time = String(str_time ? str_time : '').split(':');

	if (!arr_time[0]) dt_date.setHours(0);
	else if (RE_NUM.exec(arr_time[0]))
		if (arr_time[0] < 24) dt_date.setHours(arr_time[0]);
		else return cal_error (calErrorMessage);
	else return cal_error (calErrorMessage);

	if (!arr_time[1]) dt_date.setMinutes(0);
	else if (RE_NUM.exec(arr_time[1]))
		if (arr_time[1] < 60) dt_date.setMinutes(arr_time[1]);
		else return cal_error (calErrorMessage);
	else return cal_error (calErrorMessage);

	if (!arr_time[2]) dt_date.setSeconds(0);
	else if (RE_NUM.exec(arr_time[2]))
		if (arr_time[2] < 60) dt_date.setSeconds(arr_time[2]);
		else return cal_error (calErrorMessage);
	else return cal_error (calErrorMessage);

	dt_date.setMilliseconds(0);
	return dt_date;
}
var clickedid = "";
function cal_error (str_message) {
	alert (str_message);
	document.getElementById(clickedid).value="";
	return null;
}
