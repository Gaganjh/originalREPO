<!--
Title: Tigra Calendar
URL: http://www.softcomplex.com/products/tigra_calendar/
Version: 3.2
Date: 10/14/2002 (mm/dd/yyyy)
Note: Permission given to use this script in ANY kind of applications if
   header lines are left unchanged.
Note: Script consists of two files: calendar?.js and calendar.html
-->
<html>
<head> 
<title>Calendar</title>
<style>
	td {font-family: Arial, Helvetica, sans-serif; font-size: 12px;}
</style>

<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script> 
  
<script type="text/javascript" >

// months as they appear in the calendar's title
var ARR_MONTHS = ["January", "February", "March", "April", "May", "June",
		"July", "August", "September", "October", "November", "December"];
// week day titles as they appear on the calendar
var ARR_WEEKDAYS = ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"];
// day week starts from (normally 0-Su or 1-Mo)
var NUM_WEEKSTART = 0;
// path to the directory where calendar images are stored. trailing slash req.
var STR_ICONPATH = '/assets/unmanaged/images/';

var re_url = new RegExp('datetime=(\\-?\\d+)');
var dt_current = (re_url.exec(String(window.location))
	? new Date(new Number(RegExp.$1)) : new Date());

// START start and end time validation changes
re_url = new RegExp('starttime=(\\-?\\d+)');
var dt_start = (re_url.exec(String(window.location))
	? new Date(new Number(RegExp.$1)) : new Date(dt_current));
//alert('Calendar start date = '+dt_start);

// end date default to one year from now if not selected
re_url = new RegExp('endtime=(\\-?\\d+)');
var dt_end = (re_url.exec(String(window.location))
	? new Date(new Number(RegExp.$1)) : new Date(dt_current));
if (dt_start.valueOf() == dt_end.valueOf())
	dt_end.setYear(dt_end.getYear()+1);
//alert('Calendar end date = '+dt_end);
// END  start and end time validation changes

//default date to highlight
re_url = new RegExp('defaulttime=(\\-?\\d+)');
var dt_default = (re_url.exec(String(window.location))
	? new Date(new Number(RegExp.$1)) : new Date());

var re_id = new RegExp('id=(\\d+)');
var num_id = (re_id.exec(String(window.location))
	? new Number(RegExp.$1) : 0);
var obj_caller = (window.opener ? window.opener.calendars[num_id] : null);

if (obj_caller && obj_caller.year_scroll) {
	// get same date in the previous year
	var dt_prev_year = new Date(dt_current);
	dt_prev_year.setFullYear(dt_prev_year.getFullYear() - 1);
	if (dt_prev_year.getDate() != dt_current.getDate())
		dt_prev_year.setDate(0);
	
	// get same date in the next year
	var dt_next_year = new Date(dt_current);
	dt_next_year.setFullYear(dt_next_year.getFullYear() + 1);
	if (dt_next_year.getDate() != dt_current.getDate())
		dt_next_year.setDate(0);
}

// get same date in the previous month
var dt_prev_month = new Date(dt_current);
dt_prev_month.setMonth(dt_prev_month.getMonth() - 1);
if (dt_prev_month.getDate() != dt_current.getDate())
	dt_prev_month.setDate(0);

// get same date in the next month
var dt_next_month = new Date(dt_current);
dt_next_month.setMonth(dt_next_month.getMonth() + 1);
if (dt_next_month.getDate() != dt_current.getDate())
	dt_next_month.setDate(0);

// get first day to display in the grid for current month
var dt_firstday = new Date(dt_current);
dt_firstday.setDate(1);
dt_firstday.setDate(1 - (7 + dt_firstday.getDay() - NUM_WEEKSTART) % 7);

// function passing selected date to calling window
function set_datetime(n_datetime, b_close, validation) {
	if (!obj_caller) return;

	var dt_datetime = obj_caller.prs_time(
		(document.cal ? document.cal.time.value : ''),
		new Date(n_datetime)
	);

	if (!dt_datetime) return;
	if (b_close) {
		window.close();
		obj_caller.target.value = (document.cal
			? obj_caller.gen_tsmp(dt_datetime)
			: obj_caller.gen_date(dt_datetime)
		);
	}
	else obj_caller.popup(obj_caller.gen_date(dt_datetime),validation);
}



</script>
</head>
<body bgcolor="#FFFFFF" marginheight="5" marginwidth="5" topmargin="5" leftmargin="5" rightmargin="5">
<table class="clsOTable" cellspacing="0" border="0" width="100%">
<tr><td bgcolor="#002D62">
<table cellspacing="1" cellpadding="3" border="0" width="100%">
<tr><td colspan="7"><table cellspacing="0" cellpadding="0" border="0" width="100%">
<tr>
<script type="text/javascript" >
document.write('<td>'+(obj_caller&&obj_caller.year_scroll?'<a href="javascript:set_datetime('+dt_prev_year.valueOf()+',false,false)"><img src="'+STR_ICONPATH+'prev_year.gif" width="16" height="16" border="0" alt="previous year"></a>&nbsp;':''));

// find the first day of the start of the start month
var dt_start_of_month = new Date(dt_start);
dt_start_of_month.setDate(1);

if (
	dt_prev_month.valueOf() >= dt_start_of_month.valueOf()  
)
	document.write('<a href="javascript:set_datetime('+dt_prev_month.valueOf()+',false,false)"><img src="'+STR_ICONPATH+'prev.gif" width="16" height="16" border="0" alt="previous month"></a>');


document.write('&nbsp;</td><td align="center" width="100%"><font color="#ffffff">'+ARR_MONTHS[dt_current.getMonth()]+' '+dt_current.getFullYear() + '</font></td><td>');

// find the last day of the of the end month
var dt_end_of_month = new Date(dt_end);
dt_end_of_month.setDate(1);
dt_end_of_month.setMonth(dt_end_of_month.getMonth()+1);
dt_end_of_month.setDate(0);

if (
	dt_next_month.valueOf() <= dt_end_of_month.valueOf()  
)
	document.write('<a href="javascript:set_datetime('+dt_next_month.valueOf()+',false,false)"><img src="'+STR_ICONPATH+'next.gif" width="16" height="16" border="0" alt="next month"></a>');

document.write((obj_caller && obj_caller.year_scroll?'&nbsp;<a href="javascript:set_datetime('+dt_next_year.valueOf()+',false,false)"><img src="'+STR_ICONPATH+'next_year.gif" width="16" height="16" border="0" alt="next year"></a>':'')+'</td>');








</script>
</tr>
</table></td></tr>
<tr>
<script type="text/javascript" >

// print weekdays titles
for (var n=0; n<7; n++)
	document.write('<td bgcolor="#4E9EB8" align="center"><font color="#ffffff">'+ARR_WEEKDAYS[(NUM_WEEKSTART+n)%7]+'</font></td>');
document.write('</tr>');

// print calendar table
var dt_current_day = new Date(dt_firstday);
while (dt_current_day.getMonth() == dt_current.getMonth() ||
	dt_current_day.getMonth() == dt_firstday.getMonth()) {
	// print row heder
	document.write('<tr>');
	for (var n_current_wday=0; n_current_wday<7; n_current_wday++) {
		if (dt_current_day.getDate() == dt_default.getDate() &&
			dt_current_day.getMonth() == dt_default.getMonth())
			// print current date
			document.write('<td bgcolor="#DCECF1" align="center" width="14%">');
		else if (dt_current_day.getDay() == 0 || dt_current_day.getDay() == 6)
			// weekend days
			document.write('<td bgcolor="#CCCCCC" align="center" width="14%">');
		else
			// print working days of current month
			document.write('<td bgcolor="#ffffff" align="center" width="14%">');
			
		// TODO	
		if (
			dt_current_day.valueOf() >= dt_start.valueOf() &&
			dt_current_day.valueOf() <= dt_end.valueOf() &&
			obj_caller.isValidMarketDate(dt_current_day)
		)
			document.write('<a href="javascript:set_datetime('+dt_current_day.valueOf() +', true, true);">');

		if (dt_current_day.getMonth() == this.dt_current.getMonth())
			// print days of current month
			document.write('<font color="#000000">');
		else 
			// print days of other months
			document.write('<font color="#606060">');
			
		// TODO	
		document.write(dt_current_day.getDate()+'</font>');
		if (
			dt_current_day.valueOf() >= dt_start.valueOf() &&
			dt_current_day.valueOf() <= dt_end.valueOf() &&
			obj_caller.isValidMarketDate(dt_current_day)
		)
			document.write('</a>');
		document.write('</td>');
		dt_current_day.setDate(dt_current_day.getDate()+1);
	}
	// print row footer
	document.write('</tr>');
}
if (obj_caller && obj_caller.time_comp)
	document.write('<form onsubmit="javascript:set_datetime('+dt_current.valueOf()+', true, true)" name="cal"><tr><td colspan="7" bgcolor="#4E9EB8"><font color="White" face="tahoma, verdana" size="2">Time: <input type="text" name="time" value="'+obj_caller.gen_time(this.dt_current)+'" size="8" maxlength="8"></font></td></tr></form>');
</script>
</table></tr></td>
</table>

</body>
</html>

