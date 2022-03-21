function goToEmployeeSnapshot(selObj) {
	//Called from the participant/employee list on the securedHomePage
	
	//var targ = "resultsPage.jsp"
	var targ = "/do/census/viewEmployeeSnapshot/";
	var selectionName = selObj.name;
 	var selection = selObj.options[selObj.selectedIndex].value;
 	//var url = targ + "?" + selectionName + "=" + selection;
 	var url = targ + "?profileId=" + selection + "&source=home";

	//FOR TESTING
	//window.alert("selObj: " + selObj.name);
	//window.alert("ProfileID: " + selection);
	location = url;
}