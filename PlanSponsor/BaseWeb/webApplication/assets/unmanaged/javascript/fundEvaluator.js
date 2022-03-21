/**
* This JS file provides the AJAX interface for the ievaluator.
* Handles different AJAX request based on the filter options
*
* @author Ranjith Kumar
*/

var skipIntroductionValue = "SKIP_INTRODUCTION";
var skipIntroductionName = "skipIntroduction"


var selectYourClient = "selectYourClient";
var introduction = "introduction";
var selectCriteria = "selectCriteria";
var narrowYourList = "narrowYourList";
var investmentOptionsSelection = "investmentOptionsSelection";
var customizeReport = "customizeReport";
var generateReport = "generateReport"

var navigateToPrevious = "previous";
var navigateToNext = "next";

function createCookieSkipIntorduction() {
	YAHOO.util.Cookie.set(skipIntroductionName, skipIntroductionValue); 
}

function getCookieSkipIntroduction() {
	var value = YAHOO.util.Cookie.get(skipIntroductionName);
	return value;
}




// Add event on non YUI elements like buttons, textboxes etc..
/*
function subscribeEvents(){
var checkBoxGroupCriteria = document.getElementById("criteriaSelectedStyle");
alert("len:" + checkBoxGroupCriteria.length);
for(var i = 0; i < checkBoxGroupCriteria.length; i++) {
	alert("YUP");
		YAHOO.util.Event.onAvailable(checkBoxGroupCriteria[i], "click", checkCriterias);
	}
}
*/

