/**
 * Provides the AJAX interface for the Funds and Performance.
 * Handles different AJAX request based on the filter options
 *
 * @author ayyalsa
 */


/**
 * An associated JavaScript array object to store the action URLs.
 */
var actionURL = {
    "Fap" : {
        "Filter" : "/do/fap/fapFilterAction/"
    }
}

/**
 * For the YUI "Please Wait..." panel
 */
var loadingPanel;
var groupBySelect; // Used for ireports to show the group by in the URL.

/**
 * Map used to show the Ireport name in the URL when generating a ireport.
 */
var ireportMap = {
	"expenseRatioReport"							: "ER",
	"fundCharacteristicsReport"						: "FC",
	"investmentReturnAndExpenseRatioReport"			: "IRER",
	"investmentReturnAndStandardDeviationReport"	: "IRSD",
	"morningstarRatingsAndTickrSymbolReport"		: "MRTS",
	"lifecycleFundReport"							: "LCR",
	"lifestyleFundReport"							: "LSR",
	"marketIndexReport"								: "MIR"
}

/**
 * Map used to show the Group By name in the URL when generating a ireport.
 */
var groupByMap = {
	"filterAssetClassFunds"		: "A",
	"filterRiskCategoryFunds"	: "R"
}


/**
 * A prototype to replace the 
 * 		'&amp;' with '&'
 * 		'&lt;' 	with '<'
 * 		'&gt;'	with '>'
 * 		'&quot;' with "'"
 */
String.prototype.unescapeHTML = function () {                                       
                                                                      
            a=this.replace(/&amp;/g, '&')
			a=a.replace(/&lt;/g,"<");
			a=a.replace(/&gt;/g,">");
			a=a.replace(/&quot;/g,"'");
			
        return a;                                                                     
    };

/**
 * TODO - Can be moved to fapUtility.js
 * An utility object to handle the common operations
 */
var utilities = {
            
    /**
     * Sets the action to the form
     */ 
    setAction : function(action) {
        document.fapForm.action.value = action;
    },

    /**
     * Populates the response to the 'customQueryPanel' <div> tag
     */
    loadCustomQueryFilterSection : function(o) {
        document.getElementById("customQueryPanel").innerHTML = o.responseText;
    },
    
    /**
     * Populates the tab content to the "fapTabContainer" <div> tag
     */
    loadFundInfoTabs : function(o) {
        document.getElementById("fapTabContainer").innerHTML = o.responseText;
        utilities.hideWaitPanel();
    },

    /**
     * Displays the errors/information/warnings which is the parsed Object in the 'messages' <div> tag
     */
    displayMessages : function(parsedObject) { 
        document.getElementById("messages").innerHTML = parsedObject.errors[0].messages.unescapeHTML();
    },

    /**
     * Populates the additional parameter content to the "additionalParamSection" <div> tag
     */
     showAdditionalParameters : function(o) {
        document.getElementById("additionalParamSection").innerHTML = o.responseText;
        
		var selectedReport = document.getElementById("outputSelect").value;
     	if (document.getElementById("isAdditionalparamsEnabled")) {
     			showDialog('additionalParamSection');
	   	} else {
    		hideDialog('additionalParamSection');
    	}
         
         utilities.hideWaitPanel();
     },

     /**
      * This method hides the Additional parameters.
      */
     hideAdditionalParameters : function () {
    	 hideDialog('additionalParamSection');
     },
     
    /**
     * Displays the warnings which is the parsed Object in the 'messages' <div> tag
     */
    displayWarningMessages : function(parsedObject) { 
        document.getElementById("messages").innerHTML = parsedObject.warnings[0].messages.unescapeHTML();
    },

    /**
     * Clears the content of the "messages" <div> tag
     */
    clearMessages : function() {
        document.getElementById("messages").innerHTML = "";
    },

    /**
     * Loads the waiting panel
     */
    showWaitPanel : function() {
        waitWindow = document.getElementById('tpaFandp_body');
        if (waitWindow) {
            waitWindow.style.cursor="wait";
        }
        
        /**
        *
        *This remarked out code is for displaying a Yahoo wait panel, which the business decided to replace with an hourglass.
        *
        *waitPanel = document.getElementById("wait");
        *if (!waitPanel) {
        *    // Only instantiate if one doesn't exist.  
        *    // Otherwise drop to the show() method.
        *    loadingPanel = new YAHOO.widget.Panel("wait",  
        *                        {   width: "250px", 
        *                            height:"50px",
        *                            fixedcenter: true, 
        *                            close: false, 
        *                            draggable: false, 
        *                            zindex:4,
        *                            modal: true,
        *                            visible: false,
        *                            constraintoviewport: true
        *                        } 
        *                    );
        *    loadingPanel.setBody("<span style='padding-left:20px;float:right;padding-right:30px;padding-top:12px;'>One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
        *    loadingPanel.render(document.body);
        *}
        *loadingPanel.show();               
        */
          
    },

    /**
     * hides the waiting panel
     */
    hideWaitPanel : function() {
        waitWindow = document.getElementById('tpaFandp_body');
        if (waitWindow) {
            waitWindow.style.cursor="auto";
        }
        // loadingPanel.hide();   remarked out because we switched from a panel to an hourglass.
    },
    
    /**
     * creates a fund object
     */
    createFundObject : function(parsedObject) {
        return  new fund(parsedObject.fundId, parsedObject.fundName, 
                    parsedObject.managerName, parsedObject.assetClass, 
                    parsedObject.contractSelected);
    }
}


/**
 * This array object is used to pass as an argument to the call-back function
 */
var callbackArgument = new Array();

/**
 * Indicator to enable the Advanced Filter section
 */
var enableAdvanceFilter = false;

/**
 * Function to set the call-back argument
 */
function setCallbackArgument(argument) {
    // remove the array elements. 
    // Note: don't declare a new Array object, the call-back function will not take the new Object
    callbackArgument.splice(0, callbackArgument.length);
    callbackArgument[0] = argument;
}


/**
 * Generic function to make an AJAX request.
 *
 * @param actionPath        - action url
 * @param callbackFunction  - name of the call-back object to handle the server response
 */
function doAsyncRequest(actionPath, callbackFunction) {
    // The clearMessages will clear the error/Warning messages
    utilities.clearMessages();
    
    utilities.showWaitPanel(); 

    // This statement would send the form attributes to the server.
    // If this statement is not present, the form values will not be updated
    YAHOO.util.Connect.setForm(document.fapForm);

    // Make a request
    var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);
}


/**
 * Generic function to handle a failure in the server response
 */
var handleFailure = function(o){ 
    o.argument = null;
    utilities.hideWaitPanel();
    //alert("some error happened in the loading jsp");
}


/**
 * call-back object for the applyFilter. 
 *
 * Response object o:
 *      This response object will always have contents of the tabs.
 *
 * Success:
 *      Sets the response to the <div> "fapTabContainer"
 *
 * Failure:
 *      Calls the generic failure function "handleFailure"
 */
var callback_applyFilter = { 
    success : function(o) {
    	var parsedObject = parseResponseToJSON(o); 
    	// If the parse function returns 'parseError', then the response should have the tab jsp
        if (parsedObject == "parseError") {
			utilities.loadFundInfoTabs(o);
		}
        
        var element =  document.getElementById('psErrors');
        if (typeof(element) != 'undefined' && element != null)
        {
        	//DO nothing
        }else{
        	 // to display errors/warnings/information if any
            if (parsedObject.errors != undefined) {
                utilities.displayMessages(parsedObject);
    			utilities.setDisplayHeaders(true);
    			displayHeaders();
            } else {
    			utilities.setDisplayHeaders(false);
    		}
        }
       
        
    }, 
    
    failure: handleFailure
};


/**
 * call-back object for the doAadvanceFilter. 
 *
 * Response object o:
 * 		This response object will always have contents of the tabs [JSP]
 *
 * Success:
 *      Sets the response to the <div> "fapTabContainer"
 *
 * Failure:
 *      Calls the generic failure function "handleFailure"
 */
var callback_doAadvanceFilter = { 
    success : function(o) {

        var parsedObject = parseResponseToJSON(o); 

		// If the parse function returns 'parseError', then the response should have the tab jsp
        if (parsedObject == "parseError") {
			utilities.loadFundInfoTabs(o);
		}
		
		loadFundInfoAndSelectQueryOptions();
        utilities.hideWaitPanel();
    }, 
    
    failure: handleFailure
};

/**
 * call-back object for the loadFundInfoAndSelectQueryOptions 
 *
 * Response Object o:
 *      The response object will have the 
 *          1. baseFundInformation and 
 *          2. optionalFilterSelect list box 
 *      values as a JSON object
 *
 * Success:
 *      The success function will convert the JSON String as JavaScript array object and 
 *          1. populates the array values of the baseFundArray object to a HashMap "fundInformation"
 *          2. populates the array values of the optionalFilterSelectobject to the drop-down "optionalFilterSelect"
 *
 * Failure:
 *      calls the generic failure function "handleFailure"
 * 
 * argument:
 *      The id of the target drop-down to which the optionalFilterSelect list are to be populated.
 */
var callback_loadFundInfoAndSelectQueryOptions = { 
    success:function(o){
        var parsedObject = parseResponseToJSON(o); 

        fundInformation.clear(); 
        // set the values to base fund array
        for (var i = 0, len = parsedObject.baseFundArray.length; i < len; ++i) { 
            fundInformation.set(parsedObject.baseFundArray[i].fundId, 
                                utilities.createFundObject(parsedObject.baseFundArray[i])); 
        }
        
        // populate the "optionalFilterSelect" options in the drop-down
        populateDropdown(o.argument, parsedObject.optionalFilterSelect);

        toggleAdvancedOptions();
        includeNMLOrClosedFunds();
		showOrHideOnlySigPlusFunds();

        utilities.hideWaitPanel();
    }, 
    
    failure: handleFailure,

    argument: callbackArgument
};

/**
 * call-back object for the loadSubFilterOption / insertOrRemoveConditions
 *
 *
 * Repose Object o:
 *      The response object will have the 
 *          1. options list in the form of JSON object 
 *              OR
 *          2. the custom query filter section jsp
 *
 * Success:
 *      1.  The success function will convert the JSON String as a JavaScript array
 *          object and populates the array values to the appropriate drop-down
 *              OR
 *      2.  Sets the response to the <div> "customQueryPanel"
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 *
 * argument:
 *      The id of the target drop-down to which the options are to be populated.
 *      Sometimes this would be an array object, if there are multiple drop-downs to be populated.
 */
var callback_loadOptionsList = { 
    success:function(o){
        
        parsedObject= parseResponseToJSON(o); 
        
        // If there are any errors in parsing, then the response will be the customQueryOptions JSP
        if (parsedObject == "parseError") {

            utilities.loadCustomQueryFilterSection(o);

			if (document.getElementById("customQueryPanel").style.display == "none") {
		        showOptionalFilter(document.getElementById("optionalFilterSelect"));
			}

        // If the JSOn object has "errors" then display the error messages
        } else if (parsedObject.errors != undefined){
            
            utilities.displayMessages(parsedObject);

        // If there are no parse error and errors, then load the options
        }else {
            populateDropdown(o.argument, parsedObject.optionsList);
	        showOptionalFilter(document.getElementById("optionalFilterSelect"));
        }

        utilities.hideWaitPanel();
    }, 
    
    failure: handleFailure,

    argument: callbackArgument
};

/**
 * call-back object for doFilterFundIds.
 * 
 * Response Object o:
 *      The response object will have the 
 *          1. the filtered fund ids list in the form of JSON object 
 *              OR
 *          2. the error messages
 *
 * Success:
 *      The success function will convert the JSON String as JavaScript array object and
 *      places it in the associated array object "fundIdsArray". If the response object has 
 *      any error messages, then it populates the error message in the "messages" <div>
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 *
 * argument:
 *      This argument is used to identify the filter option selected
 *
 */
var callback_filterFundIds = { 
    success:function(o){

        // parse the response to JSON
        parsedObject = parseResponseToJSON(o);

        // If there are errors, set the errors to the "messages" <div> tag
        if (parsedObject.errors != undefined) {
            
            utilities.displayMessages(parsedObject);
        
        // If there are no errors, then the response would be the list of fund ids
        } else {
            
            if (parsedObject.alertMessage != undefined) {
                
				clearAllOptionsInSelect("queryResultsSelect");
				fundIdsArray[o.argument] = '';
                // enable the query results panel
                loadQueryResults(o.argument);
                alert(parsedObject.alertMessage);
                
            } else if (parsedObject.warnings != undefined) {
            
                utilities.displayWarningMessages(parsedObject);
                
            } else if (parsedObject.filterFundIds != undefined) {

                filteredFundIds = new Array();
                
                // iterate through the fund ids and create an JavaScript array
                for (var i = 0, len = parsedObject.filterFundIds.length; i < len; ++i) { 
                    filteredFundIds[i] = parsedObject.filterFundIds[i].fundId;
                }
                
                // Set the fund id array to the "fundIdsArray" object with the key as 'selected filter option'
                fundIdsArray[o.argument] = filteredFundIds;
        
                utilities.hideWaitPanel();
                // enable the query results panel and populate the filter fund ids
                loadQueryResults(o.argument);
            }
        }

        utilities.hideWaitPanel();
    }, 

    failure: handleFailure,

    argument: callbackArgument
}; 


/**
 * call-back object for doSaveUserData.
 * 
 * Response Object o:
 *      The response object will have the 
 *          1. error messages [JSON]
 *              OR
 *          2. Confirmation message [JSON]
 *
 * Success:
 * 			1. If there are any error messages, it will be displayed
 * 			2. If there are any confirmation messages, it will be displayed 
 * 				in the message dialog and based on the user operation the save
 * 				operation is triggered
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 */
var callback_doSaveUserData = { 
    success:function(o){

        // parse the response to JSON
        parsedObject = parseResponseToJSON(o);

        // If there are errors, set the errors to the "messages" <div> tag
        if (parsedObject.errors != undefined) {
            utilities.displayMessages(parsedObject);
                    
        // If there are no errors, then the response would be the list of fund ids
        } else {
            
            if (parsedObject.confirmationMessage != undefined) {
                var confirmation = confirm(parsedObject.confirmationMessage);
                
                if (confirmation) {
              		doSaveUserData(true);
              	} else {
              		utilities.clearMessages();
              	}
            }
        }
        document.fapForm.overwriteExisting.value = false;
        utilities.hideWaitPanel();
    }, 

    failure: handleFailure
}; 

/**
 * call-back object for doCustomQuery.
 * 
 * Response Object o:
 *      The response object will have any one/combination of these 
 *      	1. error messages
 *      	2. Confirmation messages
 *      	3. warning messages
 *      	4. Filtered fund IDs.
 *
 * Success:
 *      The success function will convert the JSON String as JavaScript array object 
 *      and renders the values to the page 
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 *
 * argument:
 *      This argument is used to identify the filter option selected
 *
 */
var callback_doCustomQuery = { 
    success:function(o){

        // parse the response to JSON
        parsedObject = parseResponseToJSON(o);
        
        // If there are errors, set the errors to the "messages" <div> tag
        if (parsedObject.errors != undefined) {
            
            utilities.displayMessages(parsedObject);
        
        // If there are no errors, then the response would be the list of fund ids
        } else {
            if (parsedObject.confirmationMessage != undefined) {
                var confirmation = confirm(parsedObject.confirmationMessage);
                
                if (confirmation) {
              		doSaveUserData(true);
              	} else {
              		utilities.clearMessages();
              	}

			} else  if (parsedObject.alertMessage != undefined) {
                
                 clearAllOptionsInSelect("queryResultsSelect");
				 fundIdsArray[o.argument] = '';
            	// enable the query results panel
                loadQueryResults(o.argument);
                alert(parsedObject.alertMessage);
                
			}  else  if (parsedObject.warnings != undefined) {
                utilities.displayWarningMessages(parsedObject);
            }

            if (parsedObject.filterFundIds != undefined) {

                filteredFundIds = new Array();
                
                // iterate through the fund ids and create an JavaScript array
                for (var i = 0, len = parsedObject.filterFundIds.length; i < len; ++i) { 
                    filteredFundIds[i] = parsedObject.filterFundIds[i].fundId;
                }
                
                // Set the fund id array to the "fundIdsArray" object with the key as 'selected filter option'
                fundIdsArray[o.argument] = filteredFundIds;
                
                utilities.hideWaitPanel();
                // enable the query results panel and populate the filter fund ids
                loadQueryResults(o.argument);
            }
        }

        utilities.hideWaitPanel();
    }, 

    failure: handleFailure,

    argument: callbackArgument
}; 

/**
 * call-back object for populateReportsAndDownloadsOptions.
 * 
 * Response Object o:
 *      The response object will have the 
 *          1. the drop down options list in the form of JSON object 
 *          
 *
 * Success:
 *      The success function will convert the JSON String as JavaScript array object and
 *      places it in the associated array object "outputSelect". 
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 *
 * argument:
 *      This argument is used to identify the filter option selected
 *
 */
var callback_populateReportsAndDownloadsOptions = { 
    success:function(o){
        // parse the response to JSON
        parsedObject = parseResponseToJSON(o);

        if (parsedObject != "parseError" && parsedObject.reportList != "No results") {
            createOptionsForReport("outputSelect", parsedObject.reportList);
        }
		
		if (nothingSelected('outputSelect')) {
			hide('goButton');
		}
        
		//Utilities.hideWaitPanel();
    }, 
    
    failure: handleFailure
}; 

/**
 * call-back object for changeDropDownList.
 * 
 * Response Object o:
 *      The response object may have the 
 *          1. the error messages
 *          2. a additional parameter jsp
 *            
 *
 * Success:
 *      If the response object has any error messages, then it populates the error message in the "messages" <div>
 *      else display the response directly
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 *
 * argument:
 *      This argument is used to identify the filter option selected
 *
 */
var callback_changeDropDownList = { 
    success:function(o){
        // parse the response to JSON
        parsedObject = parseResponseToJSON(o);      
        if (parsedObject.errors != undefined){          
            utilities.displayMessages(parsedObject);
        } else if (parsedObject == "parseError")  {     
        	utilities.showAdditionalParameters(o);
        } 

        utilities.hideWaitPanel();
    }, 
    failure: handleFailure,
    argument: callbackArgument  
}; 

/**
 * call-back object for OpenNewWindowForIReport.
 * 
 * Response Object o:
 *      The response object may have the 
 *          1. the error messages
 *            
 *
 * Success:
 *      Opens a new window and triggers the i:report PDF generation.
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 *
 * argument:
 *      This argument is used to identify the filter option selected
 *
 */
var callback_OpenNewWindowForIReport = {
	success : function(o) { 
		utilities.hideWaitPanel();
		 parsedObject = parseResponseToJSON(o); 
		 if (parsedObject.errors != undefined){          
	            utilities.displayMessages(parsedObject);
	    } else {
	      var ireportID = ireportMap[o.argument];
	      var groupType = groupByMap[groupBySelect];
		  window.open(actionURL["Fap"].Filter + '?action=generateIReport&id='+ireportID+'&type='+groupType, "","width=720,height=480,resizable,toolbar,scrollbars,menubar");
	    }
	}, 

	failure: handleFailure,
	argument: callbackArgument  
};

/**
 * call-back object for doSortForSloshBoxes.
 * 
 * Response Object o:
 *      The response object may have the 
 *          1. the error messages
 *            
 *
 * Success:
 *      If the response object has any error messages, then it populates the error message in the "messages" <div>
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 *
 * argument:
 *      This argument is used to identify the filter option selected
 *
 */
var callback_SortForSloshBoxes = { 
    success:function(o){
        hideDialog('changeSortX');
        // parse the response to JSON
        parsedObject = parseResponseToJSON(o);
        if (parsedObject.queryResultsValues != undefined){
        
            // iterate through the fund ids and create an JavaScript array
            for (var i = 0, len = parsedObject.queryResultsValues.length; i < len; ++i) { 
                filteredFundIds[i] = parsedObject.queryResultsValues[i].fundId;
            }
            
            // replace the existing array object with the sorted array of fund ids
            fundIdsArray[o.argument] = filteredFundIds;
        } 
        
        if (parsedObject.selectedFundsValues != undefined){
            filteredFundIds = [];
            // iterate through the fund ids and create an JavaScript array
            for (var i = 0, len = parsedObject.selectedFundsValues.length; i < len; ++i) { 
                filteredFundIds[i] = parsedObject.selectedFundsValues[i].fundId;
            }

            // set "filteredFundIds" as the current select fundId list
			currentSelectedFunds = filteredFundIds;
        }
        
        applySort();
        utilities.hideWaitPanel();
    }, 
    failure: handleFailure,
    argument: callbackArgument  
}; 

/**
 * call-back object for doMySavedData.
 * 
 * Response Object o:
 *      The response object may have the 
 *          1. the Custom Query Section [JSP]
 *          2. error messages [JSON]
 *          3. Drop-down options for the Saved Lists and My Custom Queries
 *          4. Warning messages
 *          5. filtered fundIds for the saved data
 *            
 *
 * Success:
 * 			1. Custom Query: Populates the JSP content to the "customQueryPanel" <div> section
 *      	2. Error Messages: If the response object has any error messages, 
 *      		then it populates the error message in the "messages" <div>
 *      	3. Drop-down options: Populates the drop-down options, for the My Saved Lists 
 *      		and My Custom Queries
 *      	4. Warning messages: If the response object has any warning messages, 
 *      		then it populates in the "messages" <div>
 *      	5. Filtered Fund Ids: Creates a JavaScript array object for filtered fund Ids and 
 *      		stores it in the fundIdsArray Object
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 *
 * argument:
 *      This argument is used to identify the filter option selected
 */
var callback_doMySavedData = { 
    success:function(o){
    
        // parse the response to JSON
        parsedObject = parseResponseToJSON(o);
    
        // If there are any errors in parsing, then the response will be the customQueryOptions JSP
        if (parsedObject == "parseError") {
            optionalFilter = document.getElementById("optionalFilterSelect");
            optionalFilter .value = "filterCustomQueryFunds";
            showOptionalFilter(optionalFilter);
            utilities.loadCustomQueryFilterSection(o);

        // If the JSOn object has "errors" then display the error messages
        // If there are errors, set the errors to the "messages" <div> tag
        } else if (parsedObject.errors != undefined) {
            
            utilities.displayMessages(parsedObject);
        
        } else if (parsedObject.optionsList != undefined) {
            targerDropDown = querySelectionPanelsAndLabels[o.argument].subFilterOptions[0].elementId;
            populateDropdown(targerDropDown, parsedObject.optionsList);
			
			if (targerDropDown == 'mySavedListsQuerySelect'){
				enableOrDisableDeleteButton(document.getElementById('mySavedListsQuerySelect'), 'deleteList');
			} else {
				enableOrDisableDeleteButton(document.getElementById('mySavedQueriesQuerySelect'), 'deleteQuery');
			}
			

        // If there are no errors, then the response would be the list of fund ids
        } else {

            if (parsedObject.warnings != undefined) {
                utilities.displayWarningMessages(parsedObject);
            }

            if (parsedObject.filterFundIds != undefined) {

                filteredFundIds = new Array();
                
                // iterate through the fund ids and create an JavaScript array
                for (var i = 0, len = parsedObject.filterFundIds.length; i < len; ++i) { 
                    filteredFundIds[i] = parsedObject.filterFundIds[i].fundId;
                }
                
                // Set the fund id array to the "fundIdsArray" object with the key as 'selected filter option'
                fundIdsArray[o.argument] = filteredFundIds;

                utilities.hideWaitPanel();
                // enable the query results panel and populate the filter fund ids
                loadQueryResults(o.argument);

                if (document.getElementById("optionalFilterSelect").value == "filterMySavedLists"
                		&& document.getElementById("saveListSection")) {
                    enableSaveFundListSection('saveListSection', true);
                    document.getElementById("saveListName").value = document.getElementById("mySavedListsQuerySelect").value;
                }
            }
        }

        utilities.hideWaitPanel();
    }, 

    failure: handleFailure,

    argument: callbackArgument
}; 

/**
 * call-back object for doEditCriteria .
 * 
 * Response Object o:
 *      The response object may have the 
 *          1. the error messages
 *            
 *
 * Success:
 *      If the response object has any error messages, then 
 *      it populates the error message in the "messages" <div>
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 */
var callback_doEditCriteria = { 
    success:function(o){
    
        // parse the response to JSON
        parsedObject = parseResponseToJSON(o);
    
        // If there are any errors in parsing, then the response will be the customQueryOptions JSP
        if (parsedObject == "parseError") {
            optionalFilter = document.getElementById("optionalFilterSelect");
            optionalFilter .value = "filterCustomQueryFunds";
            showOptionalFilter(optionalFilter);
            utilities.loadCustomQueryFilterSection(o);
        } 

        utilities.hideWaitPanel();
    }, 

    failure: handleFailure
}; 

/**
 *  Add event listener for the buttons
 */
// For the base filter Apply button
YAHOO.util.Event.addListener("applyButton", "click", applyFilter, actionURL["Fap"].Filter); 
// For the Advance filter Apply Below button
YAHOO.util.Event.addListener("applyBelowButton", "click", applyAdvanceFilteredFundIds);
// For the Advance filter button
YAHOO.util.Event.addListener("viewAdvancedBtn", "click", doAdvanceFilter); 
// For the optional Filter select box
YAHOO.util.Event.addListener("optionalFilterSelect", "change", loadOptionsList); 
// This button name will be common to all the All funds, Retail and sub-advised funds options
YAHOO.util.Event.addListener("btnViewResults", "click", doFilterFundIds);
// For the Asset Class View Results button
YAHOO.util.Event.addListener("btnViewAssetClassResults", "click", doFilterFundIds);
// For the Risk Category View Results button
YAHOO.util.Event.addListener("btnViewRiskCatResults", "click", doFilterFundIds);
// For the short-list View Results button
YAHOO.util.Event.addListener("btnViewShortlistResults", "click", doFilterFundIds);
// For the fund name search View Results button
YAHOO.util.Event.addListener("btnViewFundNameResults", "click", doFilterFundIds);
//For the changes made to reports and download drop down list
YAHOO.util.Event.addListener("outputSelect", "change", doOutputSelect);
//For the reports and download go button
YAHOO.util.Event.addListener("viewReports", "click", doViewReports);
// For the Slush box sorting
YAHOO.util.Event.addListener("sortPreferenceSelect", "change", doSortForSloshBoxes);
//For the Advance filter button
YAHOO.util.Event.addListener("scoreCardMetricsBtn", "click", doScoreCardMetricsSelection); 

/**
 * Makes an AJAX call to perform SORTING
 * This function will be triggered when the user clicks the column header on the tabs
 * 
 * Performs sorting
 *
 */
function doColumnSort(columnTobeSorted, tabName) {
    
    // create the url by adding the coulmn number in the query string
    url =  actionURL["Fap"].Filter + "?action=sort&sortColumn=" + columnTobeSorted;

    // if the tabName is specified set that as the tab currently selected
    if (tabName != undefined) {
        document.fapForm.tabSelected.value = tabName;
    }

    // make an AJAX call
    doAsyncRequest(url, callback_applyFilter);
}

/**
 * Makes an AJAX call.
 * This function will be triggered when the user changes the values in the drop down list
 */
function doOutputSelect() {

    if (nothingSelected('outputSelect')) {
        hide('goButton');
        setSelectedIndex('outputSelect', 0);
    } else {
	    utilities.setAction("changeDropDownList");

	    toggleBaseFilters = document.getElementById('classSelect').disabled;

        // The struts doesn't take the values of the disabled HTML elements
        // So, enable the options and disable after submitting the request
        if (toggleBaseFilters) {
            enableBaseFilters();
        }

		if (isIreportSelected('outputSelect')) {
	       hide('goButton');
		}

		doAsyncRequest(actionURL["Fap"].Filter, callback_changeDropDownList);

        // The struts doesn't take the values of the disabled HTML elements
        // So, enable the options and disable after submitting the request
        if (toggleBaseFilters) {
			disableBaseFilters();
        }

		if (!nothingSelected('outputSelect') && !isIreportSelected('outputSelect')) {
	        show('goButton');
		}
    }  
}

/**
 * This method checks if one of the i:reports was selected or not.
 * @param selectBox
 * @return - true if one of the i:reports was selected
 */
function isIreportSelected(selectBox) {
    var selObj;
    if (typeof selectBox == 'string') {
        selObj = document.getElementById(selectBox);
    } else if (selectBox && typeof selectBox == "object") {
        selObj = selectBox;
    }
    
    if (selObj.value == "expenseRatioReport" || 
		selObj.value == "fundCharacteristicsReport" || 
		selObj.value == "investmentReturnAndExpenseRatioReport" || 
		selObj.value == "investmentReturnAndStandardDeviationReport" || 
		selObj.value == "morningstarRatingsAndTickrSymbolReport" || 
		selObj.value == "lifecycleFundReport" || 
		selObj.value == "lifestyleFundReport" || 
		selObj.value == "marketIndexReport") {
        return true;        
    } else {
        return false;
    }
}

/**
 * This method is used to cancel the i:report PDF generation.
 */
function doCancelIreport() {
	hideDialog('additionalParamSection');

	hide('goButton');
    setSelectedIndex('outputSelect', 0);
}


/**
 * This function will be triggered when the user selects a report in the dropdownlist and clicks go
 */
function doViewReports() {

    if (document.getElementById('outputSelect').value == 'downloadCsv'
            || document.getElementById('outputSelect').value == 'downloadCsvAll') {
        
        if (confirm(' I acknowledge that the accuracy of data downloaded outside of this application is the responsibility of the user and that neither John Hancock nor its affiliates shall be responsible for any inaccuracy resulting from such use.')) {
        
        
        var elemIF = document.createElement("iframe");
        elemIF.src = actionURL["Fap"].Filter + '?action='
                + document.getElementById('outputSelect').value
                + '&tabSelected=' + document.fapForm.tabSelected.value
                + '&asOfDate=' + document.fapForm.asOfDate.value
                + '&companyId=' + document.fapForm.companyId.value;
        
        elemIF.style.display = "none";
        document.body.appendChild(elemIF);
        }
    } else if (document.getElementById('outputSelect').value == 'printPdf') {
        
        window.open(actionURL["Fap"].Filter + '?action=printPdf&tabSelected=' + document.fapForm.tabSelected.value + 
                '&asOfDate=' + document.fapForm.asOfDate.value +
                '&companyId=' + document.fapForm.companyId.value
                , "","width=720,height=480,resizable,toolbar,scrollbars,menubar");
    } else {
    	utilities.setAction("storeFapFormInSession");
    	
    	setCallbackArgument(document.fapForm.selectedReport.value);
    	groupBySelect = document.fapForm.groupBySelect.value;
    	
        toggleBaseFilters = document.getElementById('classSelect').disabled;

        // The struts doesn't take the values of the disabled HTML elements
        // So, enable the options and disable after submitting the request
        if (toggleBaseFilters) {
            enableBaseFilters();
        }

    	doAsyncRequest(actionURL["Fap"].Filter, callback_OpenNewWindowForIReport);

        // The struts doesn't take the values of the disabled HTML elements
        // So, enable the options and disable after submitting the request
        if (toggleBaseFilters) {
			disableBaseFilters();
        }

		hideDialog('additionalParamSection');
    }

    utilities.clearMessages();
    hide('goButton');
    setSelectedIndex('outputSelect', 0);

}

/**
 * Makes an AJAX call. The function will be called during the page load. Fetches
 * the tab content with default filter values
 */ 
function applyDefaultFilter(){
    
    // set the form action attribute
    utilities.setAction("filter");
    
    // Make async call
    doAsyncRequest(actionURL["Fap"].Filter, callback_applyFilter);

}

/**
 *  Makes an AJAX call. 
 *  The function will be called during the page load.
 *  Fetches the tab content with default filter values
 */ 
function applyAdvanceFilteredFundIds(){
    
    var selectedFunds = convertListBoxOptionsToString("selectedFundsSelect", false);
    
    //set selected fund names
    setSelectedFundNames();

    if (selectedFunds != undefined && selectedFunds.length > 0) {
        // set the created string to the form
        document.fapForm.selectedFundsValues.value = selectedFunds;
    
    	showTabDisclaimers(document.fapForm.tabSelected.value);
    
        // set the form action attribute
        utilities.setAction("filter");
        
        // The struts doesn't take the values of the disabled HTML elements
        // So, enable the options and disable after submitting the request
        enableBaseFilters();
    
        // Make async call
        doAsyncRequest(actionURL["Fap"].Filter, callback_applyFilter);
    
        disableBaseFilters();
        
        // reset the values
        document.fapForm.selectedFundsValues.value = "";
    }
}

/**
 *  Makes an AJAX call. 
 *  The function will be called for both Base & Advance filter apply button and 
 *  also when the tabs are clicked.
 * 
 *  @Params
 *      event           - Action event. This is a syntax of YUI event handlers
 *      actionPath      - The action URL matches to the struts action-config.
 *      alternateAction - Alternate action, 
 *                        if the action should be different from the default "filter" action
 */ 
function applyFilter(event, actionPath, alternateAction){
    
    // to find if tab is clicked and if clicked find the tab id
    var eventClicked ='';
    var tabsClick = 'tabsClick';
    eventClicked = eventClicked + event;
    var index = eventClicked.indexOf('tabsClick');
    var isTabsClicked = false;
    if(index ==0){
      isTabsClicked =true;
    }

   // default tab
   //document.fapForm.tabSelected.value = 'fundInformation';
   if(index >= 0){
       // set the selected tab to form
      document.fapForm.tabSelected.value = eventClicked.substring(index + tabsClick.length);
   }

   if(index >= 0){
       // set the selected tab to form
      document.fapForm.tabSelected.value = eventClicked.substring(index + tabsClick.length);
   }

   showTabDisclaimers(document.fapForm.tabSelected.value);
   
   if(document.fapForm.tabSelected.value == 'fundScorecard') {
		   show('scorecardMetricsSelect');
		   hide('classFilterLabel');
		   hide('classFilter');
	   } else {
		   hide('scorecardMetricsSelect');
		   show('classFilterLabel');
		   show('classFilter');
	}

    
	// set the form action attribute
    utilities.setAction("filter");
    if (alternateAction != undefined){
		utilities.setAction(alternateAction);
	}

    // Make async call
    doAsyncRequest(actionPath, callback_applyFilter);
    
    populateReportsAndDownloadsOptions();
} 

/**
 * AJAX call to enable the AdvancedFilter section
 */
function doAdvanceFilter() {

	if (document.getElementById('advancedX').style.display == 'none') {
		
		showDialog('termsAndCondition');
		
    } else {
        document.fapForm.advanceFilterEnabled.value = "false";
        toggleAdvancedOptions();
    }
}

/**
 * AJAX call to enable the AdvancedFilter section
 */
function showAdvanceFilter() {

		if(document.getElementById("acceptTermsAndCondition").checked){
				
			document.getElementById('showAdvanceFilterButton').disabled= '';
				
			document.getElementById("acceptTermsAndCondition").checked = false;
				
			showTabDisclaimers(document.fapForm.tabSelected.value);
	        
	        // set the form action attribute
	        utilities.setAction("filter");

	        // Make an async call
	        doAsyncRequest(actionURL["Fap"].Filter, callback_doAadvanceFilter);

	        populateReportsAndDownloadsOptions();
			
			hideDialog('termsAndCondition');

		} else {
			document.getElementById('showAdvanceFilterButton').disabled= '';
		}  
}

/**
 * AJAX call to enable the AdvancedFilter section
 */
function acceptTermAndCondition() {
	if(document.getElementById("acceptTermsAndCondition").checked){
		document.getElementById('showAdvanceFilterButton').disabled= '';
	} else {
		document.getElementById('showAdvanceFilterButton').disabled= 'disabled';
	}
}

/**
 * AJAX call to enable the AdvancedFilter section
 */
function cancelAdvanceFilter() {
		hideDialog('termsAndCondition');
		document.getElementById('showAdvanceFilterButton').disabled= 'disabled';
		document.getElementById("acceptTermsAndCondition").checked = false;
        
}

/**
 * Makes an AJAX call. 
 * Gets the Base fund array and optionalSelectFilter list
 * The function is called during the onClick event of the Advance filter options button
 */
function loadFundInfoAndSelectQueryOptions() {
    // set the form action attribute as 'advanceFilter'
    utilities.setAction("advanceFilter");
    document.fapForm.advanceFilterEnabled.value = "true";

    // set the call-back argument. The values is the id of the drop-down
    // the values from the JSON will be populated to this drop-down
    setCallbackArgument("optionalFilterSelect");

    // Make an async call
    doAsyncRequest(actionURL["Fap"].Filter, callback_loadFundInfoAndSelectQueryOptions);
}


/**
 * Makes an AJAX call. 
 * Gets the sub-filter option list. If the "Asset Class" is selected, it gets the list of asset classes.
 * The function is called in the onChange event of the "optionalFilterSelect" drop-down box
 */
function loadOptionsList(saveQueryName) {

    if (document.getElementById(saveQueryName) != undefined) {
        document.getElementById(saveQueryName).value = "";
    }

    selectedFilter = document.getElementById("optionalFilterSelect").value;
    selectBoxId = querySelectionPanelsAndLabels[selectedFilter].subFilterOptions;

    if (selectBoxId != undefined){
        
        // If the user has selected the Custom Query options then set a different action
        if (selectBoxId[0].elementId == "customQuery") {
            
            // set the form action attribute as 'EnableCustomQuery'
            utilities.setAction("enableCustomQuery");

        } else {
            // set the form action attribute as 'InnerFilterOption'
            utilities.setAction("InnerFilterOption");
            
            // remove the array elements. 
            // Note: don't declare a new Array object, the call-back function will not take the new Object
            callbackArgument.splice(0, callbackArgument.length);
            // set the callbackArgument
            for (i=0; i < selectBoxId.length; i++) {
                callbackArgument[i] = selectBoxId[i].elementId;
            }
        }
        
        // Make an async call
        doAsyncRequest(actionURL["Fap"].Filter, callback_loadOptionsList);
    } else {
        showOptionalFilter(document.getElementById("optionalFilterSelect"));
    }
}


/**
 * Makes an AJAX call. 
 * Gets the fund ids based on the optional query selection. 
 * eg.  If the user selects "All Available Funds", then this function will get all the fund ids.
 *      If the user selects "Retail Funds", then this function will get only the Retail Fund ids.
 * The function is called during the onClick event of the "View Results" button.
 */
function doFilterFundIds() {
    // set the form action attribute as 'filterFundIds'
    utilities.setAction("filterFundIds");

    // set the call-back argument
    setCallbackArgument(document.fapForm.optionalFilterSelect.value);
    
    getValues(document.fapForm.optionalFilterSelect.value);

    
    document.fapForm.selectedAdvanceFilterOption.value = document.fapForm.optionalFilterSelect.value;

    // The struts doesn't take the values of the disabled HTML elements
    // So, enable the options and disable after submitting the request
    enableBaseFilters();

    // Make an async call
    doAsyncRequest(actionURL["Fap"].Filter, callback_filterFundIds);
    disableBaseFilters();
    
}

/**
 * Triggers an AJAX call to enable the Custom Query with the default options
 */
function doCustomQuery() {
    // set the form action attribute as 'filterFundIds'
    utilities.setAction("CustomQueryFilter");

    // set the call-back argument
    setCallbackArgument(document.fapForm.optionalFilterSelect.value);
    
    // The struts doesn't take the values of the disabled HTML elements
    // So, enable the options and disable after submitting the request
    enableBaseFilters();

    // Make an async call
    doAsyncRequest(actionURL["Fap"].Filter, callback_doCustomQuery);
    disableBaseFilters();
}

/**
 * An AJAx call to edit the Custom Query
 */
function doEditCriteria() {
    // set the form action attribute as 'filterFundIds'
    utilities.setAction("EditCriteria");

    // The struts doesn't take the values of the disabled HTML elements
    // So, enable the options and disable after submitting the request
    enableBaseFilters();

    // Make an async call
    doAsyncRequest(actionURL["Fap"].Filter, callback_doEditCriteria);
    disableBaseFilters();
}

/**
 * Triggers an AJAX call to save a Custom Query or a Fund List
 * 
 * @param overwrite (indicates, whether the existing row should be over-written)
 */
function doSaveUserData(overwrite) {

    // set the form action attribute as 'filterFundIds'
    if (document.fapForm.optionalFilterSelect.value == "filterCustomQueryFunds" 
			&& document.getElementById("customQueryPanel").style.display != 'none') {
        utilities.setAction("SaveCustomQuery");
    } else {
        utilities.setAction("SaveFundList");
        // create a String of all options value
        selectedFundsValues = convertListBoxOptionsToString("selectedFundsSelect", false);

        // set the created string to the form
        document.fapForm.selectedFundsValues.value = selectedFundsValues;
    }

    if (overwrite) {
        document.fapForm.overwriteExisting.value = true;
    }
    // Make an async call
    doAsyncRequest(actionURL["Fap"].Filter, callback_doSaveUserData);
}

/**
 * The user has option to select more than one Asset Class or Risk Category.
 * This function gets all the selected options as a String and the options are 
 * separated by a "|" symbol
 * 
 * @param selectedFilterOption
 */
function getValues(selectedFilterOption) {
    if (selectedFilterOption == "filterAssetClassFunds"){
        selectedOptions = convertListBoxOptionsToString("assetClassQuerySelect", true);
        document.fapForm.selectedAssetOrRiskValues.value = selectedOptions;
    } else if (selectedFilterOption == "filterRiskCategoryFunds"){
        selectedOptions = convertListBoxOptionsToString("riskCategoryQuerySelect", true);
        document.fapForm.selectedAssetOrRiskValues.value = selectedOptions;
    }
}

/**
 * Triggers an AJAX call to create or remove rows in Custom Query
 * 
 * @param rowIndex
 */
function insertOrRemoveRows(rowIndex) {
    // set the form action attribute as 'customQueryInsertOrRemoveConditions'
    utilities.setAction('InsertOrRemoveRows');

    // set the rowIndex. This attribute is used to identify from which row the user has performed the operation
    document.fapForm.customQueryRowIndex.value = rowIndex;
    
    // Make an async call
    doAsyncRequest(actionURL["Fap"].Filter, callback_loadOptionsList);
}

/**
 * Makes an AJAX call.
 * Based on the field selection, the value will be changed to a
 * Drop-down or a input box 
 * 
 * @param fieldSelect (Field crop-down
 */
function toggleCustomQueryValue(fieldSelect) {
    customChange(fieldSelect);

    //if (fieldSelect.selectedIndex != 0) {
    
        // set the form action attribute as 'customQueryInsertOrRemoveConditions'
        utilities.setAction('toggleValue');

        // Make an async call
        doAsyncRequest(actionURL["Fap"].Filter, callback_loadOptionsList);
//  }
}

/**
 * Makes an AJAX call. 
 * populates the Reports And Download drop down options based on the view or tab selected
 * The function is called during the onClick event of the "Apply" button
 */
function populateReportsAndDownloadsOptions() {
    // make async call to populate the drop down based on the view or tab selected
    document.getElementById('outputSelect').value = 'N/A';
    utilities.setAction("reportsAndDownload");
    
    toggleBaseFilters = document.getElementById('classSelect').disabled;

    // The struts doesn't take the values of the disabled HTML elements
    // So, enable the options and disable after submitting the request
    if (toggleBaseFilters) {
        enableBaseFilters();
    }

    doAsyncRequest(actionURL["Fap"].Filter, callback_populateReportsAndDownloadsOptions);

    // disable the base filter options
    if (toggleBaseFilters) {
        disableBaseFilters();
    }
}

/**
 * Makes an AJAX call. 
 * Performs the sorting for the Query Results Box and Selected Funds Box.
 */
function doSortForSloshBoxes() {

    // create a String of all options value
    queryRestultsValues = convertListBoxOptionsToString("queryResultsSelect", false);
    selectedFundsValues = convertListBoxOptionsToString("selectedFundsSelect", false);

    // set the created string to the form
    document.fapForm.queryResultsValues.value = queryRestultsValues;
    document.fapForm.selectedFundsValues.value = selectedFundsValues;

    // set the call-back argument
    setCallbackArgument(document.fapForm.optionalFilterSelect.value);

    // set the action as sortForSloshBoxes
    utilities.setAction('SortForSloshBoxes');

    // The struts doesn't take the values of the disabled HTML elements
    // So, enable the options and disable after submitting the request
    enableBaseFilters();

    // Make an async call
    doAsyncRequest(actionURL["Fap"].Filter, callback_SortForSloshBoxes);
    
    // disable the base filter options
    disableBaseFilters();

}

/**
 * Function to trigger an AJAX call to view or delete or edit the 
 * CustomQuery or a Fund List
 * 
 * @param userEvent (save, delete or edit)
 */
function doMySavedData(userEvent) {
    
    // set the call-back argument
    setCallbackArgument(document.fapForm.optionalFilterSelect.value);

    // set the action as sortForSloshBoxes
    utilities.setAction('FilterFundIds');

    document.fapForm.eventTriggered.value = userEvent;
    document.fapForm.selectedAdvanceFilterOption.value = document.fapForm.optionalFilterSelect.value;

    // The struts doesn't take the values of the disabled HTML elements
    // So, enable the options and disable after submitting the request
    enableBaseFilters();

    // Make an async call
    doAsyncRequest(actionURL["Fap"].Filter, callback_doMySavedData);
    
    // disable the base filter options
    disableBaseFilters();
}

/**
 * Creates a String with option values separated by "|" 
 */
function convertListBoxOptionsToString(selectBoxId, onlySelected) {
    
    optionsArray = document.getElementById(selectBoxId).options;

    optionValuesAsString= "";
    
    for (i=0; i < optionsArray.length; i++) {
        if (optionValuesAsString.length > 0 ) {
                optionValuesAsString += "|";
        }

        if (onlySelected) {
            if (optionsArray[i].selected == true){
                optionValuesAsString += optionsArray[i].value;
            }
        } else {
            optionValuesAsString += optionsArray[i].value;
        }
        
    }
    
    return optionValuesAsString;
}

/**
 * Creates a String with option text separated by "~" 
 */
function setSelectedFundNames() {
    optionsArray = document.getElementById("selectedFundsSelect").options;
    optionValuesAsString= "";
    for (i=0; i < optionsArray.length; i++) {
        if (optionValuesAsString.length > 0 ) {
                optionValuesAsString += "~";
        }
       optionValuesAsString += optionsArray[i].text;
    }
    document.fapForm.selectedFundNames.value = optionValuesAsString;
}

/**
 * Function to display the asset class code and description in a pop-up
 */
function doDisplayAssetClass() {
	url = actionURL["Fap"].Filter + "?action=displayAssetClass";
    window.open(url,"","scrollbars=1,width=320,height=460");
}

/**
 * 
 * This function will be triggered when the user selects a report in the dropdownlist and clicks go
 * 
 * Gets the funds info for the contract selected
 *
 */
function doScoreCardMetricsSelection() {
	showDialog('fundScoreCardSelection');
	
	updateCheckBox(document.fapForm.showMorningstarScorecardMetrics.value, "morningStarCheckBox");
	updateCheckBox(document.fapForm.showFi360ScorecardMetrics.value, "fi360CheckBox");
	updateCheckBox(document.fapForm.showRpagScorecardMetrics.value, "rpagCheckBox");
	
}


function updateCheckBox(keyValue, elementName) {
	if(keyValue == 'true') {
		document.getElementById(elementName).checked = true;
	} else {
		document.getElementById(elementName).checked = false;
	}
}

/**
 * 
 * This function will be triggered when the user selects a report in the dropdownlist and clicks go
 * 
 * Gets the funds info for the contract selected
 *
 */
function cancelMetricsSelection() {
	enable('fundScorecardMetricsConfirmationButton');
	hideDialog('fundScoreCardSelection');
}

/**
 * 
 * This function will be triggered when the user selects a report in the dropdownlist and clicks go
 * 
 * Gets the funds info for the contract selected
 *
 */
function proceedWithMetricsSelection(morningstar, fi360, rpag) {
	
    document.fapForm.showMorningstarScorecardMetrics.value = morningstar;
    document.fapForm.showFi360ScorecardMetrics.value = fi360;
    document.fapForm.showRpagScorecardMetrics.value = rpag;

    utilities.setAction("filterFundScorecardMetrics");
    doAsyncRequest(actionURL["Fap"].Filter, callback_applyFundScorecardMetricsSelection);
}


/**
 * call-back object for FundScorecardMetricsSelection.
 * 
 * Response Object o:
 *      The response object will have the 
 *      drop down options list in the form of JSON object 
 *          
 *
 * Success:
 *      The success function will convert the JSON String as JavaScript array object and
 *      places it in the associated array object "outputSelect". 
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 *
 * argument:
 *      This argument is used to identify the filter option selected
 *
 */
var callback_applyFundScorecardMetricsSelection = { 
	    success : function(o) { 
			var parsedObject = parseResponseToJSON(o); 
			if (parsedObject == "parseError") {
				utilities.loadFundInfoTabs(o);
			}
			document.getElementById('outputSelect').value = 'N/A';
			hideDialog('fundScoreCardSelection');
	    }, 
	    
	    failure: handleFailure,
		
		argument: callbackArgument
}; 