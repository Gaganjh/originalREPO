/**
 * Provides the functions to handle the different sections of the 
 * Funds & Performance page.
 * 
 * This is shared for Generic and Contract F&P pages
 *
 * @author ayyalsa
 */


/**
 * Constructor to create the Fund Object
 *  
 * @param fundId
 * @param fundName
 * @param managerName
 * @param assetClass
 * @param contractSelected
 * 
 * @return newly created Fund Object
 */
function fund(fundId, fundName, managerName, assetClass, contractSelected) {
    this.fundId = fundId;
    this.fundName = fundName;
    this.assetClass = assetClass;
    this.managerName = managerName;
    this.contractSelected = contractSelected;  
}

/**
 *  The base fund HashMap object, holds the  data that needs to be displayed in the 
 *  Query Results box and Selected Query results box
 */
var fundInformation = {
    set : function(index, value) {
            this[index] = value;
        },
    
    get : function(index) {
            return this[index];
        },

    clear : function() {
        //fundInformation = new fundInformation();
    }
}


/**
 *  Array object that has the filteredFund ids that needs to be populated to the Query Results list box. 
 */
var fundIdsArray = new Array();


/**
 *  An associated array object that holds the name of the query panels. 
 *  The values should match with the appropriate <div> tags
 *  
 *  How the array object is designed?
 * 
 *  "Maps with the optionalFilterSelect drop-down value"    : {
 *      "HARD-CODED: key for the selection panel"       : "id of the selection panel <div tag>",
 *      "HARD-CODED: key for the query results label"   : "Text that needs to be displayed in the query results panel as a title and 
 *                                                          it should be matching the selected value of the filter select drop-down"
 *      "HARD-CODED: key for the sub-filter options"    : "holds an array of element ids related to the selection made. some options will not 
 *                                                          have this key.
 *                                      [ 
 *                                          {"HARD-CODED: key for the element Id"   :   "id of the sub-filter option <div> tag"}
 *                                      ]"
 *  }
 *
 *  Make sure you don't leave any unwanted comma. IE never accept those
 */
var querySelectionPanelsAndLabels = {
    "filterAllAvailableFunds" : {
        "selectionPanel"    : "viewResultsButtonPanel",
        "queryResultsLabel" : "All Available Funds Query Results:"
    },
    
    "filterRetailFunds" : {
        "selectionPanel"    : "viewResultsButtonPanel",
        "queryResultsLabel" : "Retail Funds Query Results:"
    },
    
    "filterSubadvisedFunds" : {
        "selectionPanel"    : "viewResultsButtonPanel",
        "queryResultsLabel" : "Sub-Advised Funds Query Results:"
    },
    
    "filterContractAvailableFunds"  : {
        "selectionPanel"    : "viewResultsButtonPanel",
        "queryResultsLabel" : "Available to Contract Funds Query Results:"
    },
    
    "filterContractSelectedFunds" : {
        "selectionPanel"    : "viewResultsButtonPanel",
        "queryResultsLabel" : "Selected by Contract Funds Query Results:"
    },
    
    "filterAssetClassFunds" : {
        "selectionPanel"    : "assetClassFundsQueryPanel",
        "queryResultsLabel" : "Asset Class Query Results:",
        "subFilterOptions"  : 
            [
                {"elementId" : "assetClassQuerySelect"}
            ]
    },      
    
    "filterRiskCategoryFunds" : {
        "selectionPanel"    : "riskCategoryQueryPanel",
        "queryResultsLabel" : "Risk/Return Category Query Results:",
        "subFilterOptions"  : 
            [
                {"elementId" : "riskCategoryQuerySelect"}
            ]
    },
    
    "filterShortlistFunds" : {
        "selectionPanel"    : "shortlistFundsQueryPanel",
        "queryResultsLabel" : "Shortlist Query Results:",
        "subFilterOptions"  : 
            [ 
                {"elementId" : "shortlistFundMenuSelect"}, 
                {"elementId" : "shortlistTypeSelect"}, 
                {"elementId" : "allocationGroupSelect"}, 
                {"elementId" : "conservativeFundSelect"}
            ]
    },
    
    "filterCustomQueryFunds" : {
        "selectionPanel"    : "customQueryPanel",
        "queryResultsLabel" : "Custom Query Results:",
        "subFilterOptions"  : 
            [
                {"elementId" : "customQuery"}
            ]

    },
    
    "filterFundSearch" : {
        "selectionPanel"    : "fundNameQueryPanel",
        "queryResultsLabel" : "Fund Name Query Results:"
    },
    
    "filterMySavedLists" : {
        "selectionPanel"    : "savedListsQueryPanel",
        "queryResultsLabel" : "Saved List Results:",
        "subFilterOptions"  : 
            [
                {"elementId" : "mySavedListsQuerySelect"}
            ]
    },
    
    "filterMySavedQueries" : {
        "selectionPanel"    : "savedQueriesQueryPanel",
        "queryResultsLabel" : "Custom Query Results",
        "subFilterOptions"  : 
            [
                {"elementId" : "mySavedQueriesQuerySelect"}
            ]
    }
};


var currentSelectedFunds = [];
var currentQueryFunds = allAvailableFunds;
// adding it as a prototype object enables it to be used from any array

var currentQueryPanelId = "viewResultsButtonPanel";
var currentSortOptionIndex = 0;
var submittedQueryView = '';
var lastQueryExecuted;
var checkMark = unescape("%u221A");
var allAvailableFunds = '';
var selectedFundIds = '';
var isContractSelectedFundListed = false;
var isContractQueryResultsListed = false;

//******  Base cross browser functions ******/   

    /**
     * Show or Hide a section
     *  
     * @param id - HTML element
     * @param option - "none" or "block"
     */
    function setDisplay(id, option) {
        section = document.getElementById(id);
        section.style.display=option;
    }

    /**
     * Show a section
     * 
     * @param id - HTML element
     */
    function show(id) {
        section = document.getElementById(id);
        if (section) {
            if (id=="customQueryPanel") {
                section.style.width=890;
                sfPanel = document.getElementById('selectedFundsPanel');
                if (sfPanel) {
                    sfPanel.style.display='none';
                }
            }
            if (section.style) {
                section.style.display='';
            }
            
            // This is remove the focus from the Reports and Downloads dropdown.
            // Because in IE6, after getting the reports dialog, if scroll with 
            // mouse wheel the options are changing.
            if (id == "additionalParamSection") {
                document.getElementById('triggerIreport').focus();
            }
            if (id == "fundScoreCardSelection") {
                document.getElementById('fundScorecardMetricsConfirmationButton').focus();
            }
            
        } else {
            //alert ("Error: " + id + " doesn't exist");
        }
        enableOrDisableApplyFiltersButton();
    }
    
    /**
     * Hide a section
     * 
     * @param id - HTML element
     */
    function hide(id) {
        section = document.getElementById(id);
        if (section) {
            if (id=="customQueryPanel") {
                section.style.width=400;
                sfPanel = document.getElementById('selectedFundsPanel');
                if (sfPanel) {
                    sfPanel.style.display='';
                }                
            }
            if (section.style) {
                section.style.display='none';
            }
        }
    }

    /**
     * Disables a HTML element
     * 
     * @param id - HTML element
     */
    function disable(id) {
        if (document.getElementById(id) == null ) {
            //alert("NUULLLLLL " + id);
        } else {
            document.getElementById(id).disabled=true;
            document.getElementById(id).style.color="#aca899";
        }
    }

    /**
     * Enables a HTML element
     * 
     * @param id - HTML element
     */
    function enable(id) {
        if (document.getElementById(id) == null ) {
            //alert("NUULLLLLL " + id);
        } else {
            document.getElementById(id).disabled=false;
            document.getElementById(id).style.color="";
        }
    }

    /**
     * Each tab has its own disclaimer in a separate <div> tag
     * This function hides all the tab's disclaimers. i.e the <div> tags
     */
    function hideTabDisclaimers() {
        hide('fundInformation');
        hide('pricesAndYTD');
        hide('performanceAndFees');
        hide('standardDeviation');
        hide('fundCharacteristics1');
        hide('fundCharacteristics2');
        hide('morningstar');
        hide('fundScorecard');
    }

    /**
     * Each tab has its own disclaimer in a separate <div> tag
     * This function shows the tab's disclaimers.
     * 
     * @param tabId - <div> id for the particular tab
     */
    function showTabDisclaimers(tabId) {
        if (tabId.indexOf('Monthly') == -1 && tabId.indexOf('Quarterly') == -1) {
            hideTabDisclaimers();
            show(tabId);
        }
    }

    /**
     * Disables the base filter section.
     * i.e. 1) the view By 
     *      2) contract search text box
     *      3) Group By and 
     *      4) Class 
     */
    function disableBaseFilters() {
        disable('applyButton');
        disable('baseFilterSelect');
        disable('classSelect');
        disable('contractSearchText');
        disable('groupBySelect');
    }

    /**
     * Enables the base filter section
     * i.e. 1) the view By 
     *      2) contract search text box
     *      3) Group By and 
     *      4) Class 
     */
    function enableBaseFilters() {
        enable('applyButton');
        enable('baseFilterSelect');
        enable('classSelect');
        enable('contractSearchText');
        enable('groupBySelect');
    }

/**
 * Variable to indicate the Browser version
 */
var IE6 = false /*@cc_on || @_jscript_version < 5.7 @*/;

//**********  Dialog functions *****************/

    /**
     * Hides all the drop-downs in the page. 
     * 
     * @param except - ignores this drop-down
     */
    function hideAllSelectBox(except) {
        var x = document.getElementsByTagName("select");
        for (i = 0; i < x.length; i++) {
            if (x[i].id != except) {
                x[i].style.visibility = "hidden";
            }
        }
        
        if (document.getElementById(except) != null) {
            document.getElementById(except).style.visibility = "visible";
        }
    }

    /**
     * Shows all the drop-downs
     */
    function showAllSelectBox() {
        var x = document.getElementsByTagName("select");
        for (i = 0; i < x.length; i++) {
            x[i].style.visibility = "visible";
        }
    }

    /**
     * Shows the modal dialog, which is passed as a parameter
     * 
     * @param id of the modal dialog which is to be shown
     * @param except - id of the drop-down which should not be disabled
     */
    function showDialog(id, except) {
        hideAllSelectBox(except);
        documentHeight = document.getElementById('page_wrapper_footer').offsetTop;        
        panel = document.getElementById('modalGlassPanel');
        panel.style.height = documentHeight+"px";
        show('modalGlassPanel');              
        show(id);    
   }

    /**
     * Hides the dialog, which is passed as a parameter
     * 
     * @param id of the modal dialog which is to be hidden
     */
    function hideDialog(id) {
        showAllSelectBox();
        hide(id);        
        hide('modalGlassPanel');        
    }

// ********** change sort order dialog ************************
    
    /**
     * Displays the sorting option dialog 
     */
    function displayChangeSort() {
        showDialog('changeSortX', 'sortPreferenceSelect');        
    }
    
    /**
     * Re-loads the Query Results and Selected Funds Slosh boxes
     * after sorting
     */
    function applySort() {
        indexValue = document.getElementById("sortPreferenceSelect").selectedIndex;
        currentSortOptionIndex = indexValue;
        hideDialog('changeSortX');
        addOnlyTheseFunds("selectedFundsSelect", currentSelectedFunds);
        loadQueryResults(lastQueryExecuted);
    }
    
    /**
     * Closes the Sort option modal dialog
     */
    function cancelSort() {
        hideDialog('changeSortX');
    }

//************ Select a contract dialog *********************

    /**
     * Shows the contract search results dialog, which 
     * lets the user to select a contract or cancel
     */
    function contractDialogAction(parameter) {
        if ( (parameter == "cancel") || (nothingSelected("contractSelect")) ) {
            cancelContractSelect();
        } else {            
            applyContractSelect(contractSelected);
        }
    }

    /**
     * Sets the selected contract value to the form and 
     * closes the contract results dialog
     * 
     * @param value - selected in the contract search dialog
     */
    function applyContractSelect(value) {       
       document.getElementById("contractSelect").value = value;
       hideDialog('fuzzySearchX');
       //runBaseFilterQuery();
    }
    
    /**
     * Closes the contract search dialog
     */
    function cancelContractSelect() {
        /*resetAdvancedFilters();
        enableBaseFilters();*/
        hideDialog('fuzzySearchX');
    }

// ************** Panel Functions **************************
    /**
     * Hides all the query panels in the Advanced filter section
     */
    function hideAllQuerySidePanels() {
        hide("queryResultsPanel");
        hide("querySelectionPanel");
        hide("viewResultsButtonPanel");
       // hide("retailFundsQueryPanel");
       // hide("subAdvisedFundsQueryPanel");
        hide("shortlistFundsQueryPanel");
        hide("assetClassFundsQueryPanel");
        hide("riskCategoryQueryPanel");
        hide("customQueryPanel");
        hide("fundNameQueryPanel");
        hide("savedListsQueryPanel");
        hide("savedQueriesQueryPanel");
        hide("saveListSection");

        if (document.getElementById("fundNameSearchText")) {
            document.getElementById("fundNameSearchText").value = "";
        }

        disable("deleteList");
        disable("deleteQuery");

        if (document.getElementById("includeNML")) {
            document.getElementById("includeNML").checked = false;
        }

        if (document.getElementById("includeClosedFunds")) {
            document.getElementById("includeClosedFunds").checked = false;
        }
        if (document.getElementById("includeSignaturePlusFunds")) {
            document.getElementById("includeSignaturePlusFunds").checked = false;
        }

        if (document.getElementById("includeOnlySigPlusFunds")) {
            document.getElementById("includeOnlySigPlusFunds").checked = false;
        }

        if (document.getElementById("saveListName")) {
            document.getElementById("saveListName").value = "";
        }
    }

    /**
     * Shows the Query selection panel whose Id is passed
     * 
     * @param id of the query selection panel which is to be shown
     */
    function displayQuerySelectionPanel(id) {
        hideAllQuerySidePanels();
        show(id);
        show("querySelectionPanel");
        currentQueryPanelId = id;
    }

    /**
     * Shows the Query Results section
     */
    function displayQueryResultsPanel() {
        hideAllQuerySidePanels();
        show("queryResultsPanel");
        if (IE6) {
           document.getElementById("selectedFundsSelect").style.display = 'block';
        }
    }

    /**
     * Hides the Query Results panel and shows the 
     * Query selection panel
     */
    function startNewQuery() {
        utilities.clearMessages();
        displayQuerySelectionPanel("viewResultsButtonPanel");
        document.getElementById("optionalFilterSelect").selectedIndex = 0;
        
        isContractQueryResultsListed = false;
        
        if (IE6) {
           document.getElementById("selectedFundsSelect").style.display = 'block';
        }
    }

    /**
     * Shows or Hides the section
     * 
     * @param id of the HTML element
     */
    function flip(id) {
        var section = document.getElementById(id);
        if (section.style.display=='none') {
            show(id);
        } else {
            hide(id);
        }
    }

    /**
     * Removes the options from the slosh box
     * @param arrayToRemoveFrom
     * @param itemsToRemove
     * @return
     */
    function removeArrayItems(arrayToRemoveFrom, itemsToRemove){
        // removes the array items in itemsToRemove from the arrayToRemoveFrom
        // returns the new array.

        var indicatorArray = [], returnArray = [], j, n = 0, T, U
        for (j = 0 ; j < itemsToRemove.length ; j++ ) {
           indicatorArray[itemsToRemove[j]] = true;                  // or 0 !
        }
        for (j = 0 ; j < arrayToRemoveFrom.length ; j++ ) {
           if ( indicatorArray[ T = arrayToRemoveFrom[j] ] === U ) returnArray[n++] = T;
        }
        return returnArray;
    }

    var ty = 0;

    /**
     * Adds the options to the Slosh box
     * 
     * @param selectId
     * @param fundId
     */
    function addOptionToSelect(selectId, fundId) {
        try {
            selectBox = document.getElementById(selectId);
            fundInfo = fundInformation.get(fundId);
            var assetClass = fundInfo.assetClass;       
            var selected = fundInfo.contractSelected;  // Only set if we are for sure using contract data.
            var fundName = fundInfo.fundName;
            var fundMgr = fundInfo.managerName;
    
            var assetSortMode = (currentSortOptionIndex == 1);
               
            fundName = padDots(fundName,32);
               
            var sloshBucketDisplayText = "";
    
            if (assetSortMode) {
                sloshBucketDisplayText += assetClass;
            }
            
            if (selected == 'true') {
                // contract mode + asset class sort
                sloshBucketDisplayText += " " + checkMark + " ";    
                            
                if(selectId == 'selectedFundsSelect'){              
                    isContractSelectedFundListed = true;                    
                }else{              
                    isContractQueryResultsListed = true;
                }
            } else {
                // generic mode + asset class sort
                sloshBucketDisplayText += " - ";
            }
            sloshBucketDisplayText += fundName + " " + fundMgr;
            selectBox.options[selectBox.length] = new Option(sloshBucketDisplayText, fundId);   
        } catch (X) {
            //alert(":EXCEPTION ::: " + X);
            //alert(":EXCEPTION ::: " + fundId);
        }
    }

    /**
     * Clears the options of the Slosh box, whose ID is passed as a parameter
     * 
     * @param id of the slosh box, whose options are tobe removed
     */
    function clearAllOptionsInSelect(id) {
        selectBox = document.getElementById(id).options.length=0;
    }

    /**
     * Add number of Dots(.) to the text
     * 
     * @param n - text to which the dots are to be padded
     * @param totalDigits - number of dots to be padded
     * @return text which has the dots padded
     */
    function padDots(n, totalDigits) { 
        n = n.toString(); 
        var pd = ''; 
        if (totalDigits > n.length) 
        { 
            for (i=0; i < (totalDigits-n.length); i++) 
            { 
                pd += '.'; 
            } 
        } 
        return n + ' ' + pd; 
    } 
 
    /**
     * Loads the options for the Query Results slosh box and shows the 
     * Query Results slosh box
     * 
     * @param flag - selected Query filter option
     */
    function loadQueryResults(flag) {

        lastQueryExecuted = flag;
        setQueryResultLabel(querySelectionPanelsAndLabels[flag].queryResultsLabel);
        loadQueryResultFunds(fundIdsArray[flag]);
        if (flag == 'filterCustomQueryFunds' || flag == 'filterMySavedQueries') {
            show('btn_editQuery');
        } else {
            hide('btn_editQuery');
        }
        displayQueryResultsPanel();
    }

    /**
     * Sets the Label Query Results section
     *  
     * @param text - label
     */
    function setQueryResultLabel(text) {
        if (text == 'Saved List Results:') {
            text = document.getElementById('mySavedListsQuerySelect').value + " " + text;
        }
        document.getElementById('queryResultsLabel').innerHTML = text;
    }
        
    /**
     * Loads the Query Results Slosh box with the passed Fund Id array Object
     * 
     * @param arrayToLoad - array of fundIds which is to be added 
     *                      to the Query Results Slosh box
     */
    function loadQueryResultFunds(arrayToLoad) {
        if (arrayToLoad) {
            currentQueryFunds=arrayToLoad;
            addOnlyTheseFunds("queryResultsSelect", arrayToLoad);
        }
    }

    /**
     * Loads the Query Results or Selected Funds Slosh box 
     * with the passed Fund Id array Object
     * 
     * @param selectId - id of the Query Results or Selected Funds Slosh box 
     * @param fundSet set of fundIds which is to be added 
     */
    function addOnlyTheseFunds(selectId, fundSet) {
        selectBox = document.getElementById(selectId);
        clearAllOptionsInSelect(selectId);  //delete everything in the box
        selectedFundIds ="";
        for (index in fundSet) {
            // Add all the funds in the fundSet to the Select Box
            addOptionToSelect(selectId, fundSet[index]);
        }
        if(isContractSelectedFundListed || isContractQueryResultsListed){       
            document.getElementById('showLegendTextId').style.display = 'block';            
        }else{      
            document.getElementById('showLegendTextId').style.display = 'none';
        }
        clearAllHighlights("queryResultsSelect");
        clearAllHighlights("selectedFundsSelect");
        highlightFunds("queryResultsSelect", currentSelectedFunds,"#E0E0FF");
        highlightFunds("selectedFundsSelect", currentQueryFunds, "#E0E0FF");
        enableOrDisableApplyFiltersButton();
    }

    /**
      * Disables the Apply button if there are no funds in the selected funds box.
      *
      */
    function enableOrDisableApplyFiltersButton() {
        var applybtn = document.getElementById("applyBelowButton");
        var customQueryEnabled = (document.getElementById('customQueryPanel').style.display=='');
        if (document.getElementById("selectedFundsSelect").length >= 1 && !customQueryEnabled) {
            applybtn.disabled = false;
            applybtn.className = "fap-apply-filter-button";
        } else {
            applybtn.disabled = true;
            applybtn.className += " disabled";
            
        }
    }

    /**
     * When any of fund is moved from the Query Results slosh box to the 
     * Selected funds slosh box, that fund shouls be highlighted in a 
     * different color in both the slosh box
     * 
     * @param selectId
     * @param setToHighlight
     * @param color
     */
    function highlightFunds(selectId, setToHighlight, color) {
       selectBox = document.getElementById(selectId);
       if (selectBox.options != null) {
           for (i=0; i < selectBox.options.length; i++) {
                if (indexOf(setToHighlight, selectBox.options[i].value) >= 0) {
                    selectBox.options[i].style.backgroundColor = color;
                }               
           }
       }
    }

    /**
     * Removes all the highlight effect for the given slosh box
     * @param selectId - slosh box id
     */
    function clearAllHighlights(selectId) {
       selectBox = document.getElementById(selectId);
       if (selectBox.options != null) {
           for (i=0; i < selectBox.options.length; i++) {
               selectBox.options[i].style.backgroundColor = "#FFFFFF";
           }
       }
    }

    /**
     * Copies the funds from the Query Results slosh box to the 
     * Selected funds slosh box
     * 
     * @param flag either all the funds or not
     */
    function copyQueryFundsToSelectedFunds(flag) {
        document.fapForm.advanceFilterForIReports.value = document.fapForm.optionalFilterSelect.value;
        querySelectBox = document.getElementById("queryResultsSelect");
        selectedSelectBox = document.getElementById("selectedFundsSelect");
        for (i=0; i<querySelectBox.options.length; i++) {
            criteria = querySelectBox.options[i].selected == true;
            if (flag == "all") {
                criteria = true;
            }
            if (criteria) {
                fundToAdd = querySelectBox.options[i].value;
                querySelectBox.options[i].selected=false;
                if (currentSelectedFunds != null) {
                    if (indexOf(currentSelectedFunds, fundToAdd) < 0) {
                        currentSelectedFunds[currentSelectedFunds.length] = fundToAdd;
                    }
                }
            }
        }
        addOnlyTheseFunds('selectedFundsSelect', currentSelectedFunds);
        enableOrDisableSaveListButton("selectedFundsSelect");
    }

    /**
     * Enables or disables the Save List button
     * 
     * @param id of the save list button
     */
    function enableOrDisableSaveListButton(id) {
        if (document.getElementById(id).length > 0) {
            enable("saveListButton");
        } else {
            disable("saveListButton");
        }
    }

    /**
     * Removes the Funds from the Selected Funds Slosh box
     * 
     * @param flag - either all the funds or not
     */
    function removeSelectedFunds(flag) {
        var removeList = [];
        querySelectBox = document.getElementById("queryResultsSelect");
        selectedSelectBox = document.getElementById("selectedFundsSelect");

        for (i=0; i<selectedSelectBox.options.length; i++) {
            criteria = selectedSelectBox.options[i].selected == true;

            if (flag == "all") {
                criteria = true;
            }
            if (criteria) {
                removeList[removeList.length] = selectedSelectBox.options[i].value;
            }
        }
        currentSelectedFunds = removeArrayItems(currentSelectedFunds,removeList);
        isContractSelectedFundListed = false;
        addOnlyTheseFunds("selectedFundsSelect", currentSelectedFunds);
        highlightFunds("queryResultsSelect", removeList, "#FFFFFF");
        enableOrDisableSaveListButton("selectedFundsSelect");
    }

    /**
     * Returns the index of an element in an array
     * Returns -1 if not able to find
     * 
     * @param arr - array object
     * @param value - value to find
     */
    function indexOf(arr, value) {
        for (k=0; k < arr.length; k++) {
            if (arr[k] == value) {
                return k;
                break;
            }
        }
        return -1;
    }

    /**
     * Shows the query selection panel
     * 
     * @param querySelect
     * @param action
     */
    function showOptionalFilter(querySelect, action) {
        filterSelection = querySelect.value;
        displayQuerySelectionPanel(querySelectionPanelsAndLabels[filterSelection].selectionPanel);
    }

    /**
     * Based on the View By option selected, the contract Search Text-box and 
     * the Class drop-down are shown or hidden
     * 
     * @param fundOrContractSelect - selected view by option
     */
    function viewFundsBy(fundOrContractSelect) {
        fundOrContractSelect = document.getElementById("baseFilterSelect");
        show('classContractFilter');
        if (fundOrContractSelect.value == "allFunds") {
        	if(document.fapForm.tabSelected.value == 'fundScorecard') {
            	hide('classContractFilter');
            } 
            hide('contractEntry');
            show('classEntry');
            document.getElementById("contractSearchText").value = '';
        } else {
            hide('classEntry');
            show('contractEntry');
        }
    }

   /**
    * Brings back the page to the default values
    * this is something like refreshing the page
    * @return
    */
    function resetToDefaults() {
        // Only called onload of the page (i.e., if page refresh is performed, all forms should reset to defaults)        
        resetBaseFilters();
        resetAdvancedFilters();
        setSelectedIndex('outputSelect', 0);
    }   

    /**
     * Resets the Base Filter Section
     */
    function resetBaseFilters() {
        hide("advancedX");        
        contractInput = document.getElementById("contractSearchText");
        baseFilter = document.getElementById("baseFilterSelect");
        baseFilter.selectedIndex=0;
        contractInput.value = '';
        contractInput.disabled=false;        
        hide('fuzzySearchX');
        hide('contractEntry');
        show('classEntry');        
        document.getElementById("applyButton").disabled=false;
    }

    /**
     * Resets the Advanced Filter Section
     */
    function resetAdvancedFilters() {
        startNewQuery();    // Puts advanced query section back to original state.
        show('btn_newQuery');   //hidden when in contract view
        show('btn_editQuery');  //hidden when in contract view
        currentSelectedFunds = removeArrayItems(currentSelectedFunds, currentSelectedFunds);
        clearAllOptionsInSelect("selectedFundsSelect");
        clearAllOptionsInSelect("queryResultsSelect");
        hide('advancedX');
        enableBaseFilters();
        setAdvancedFilterButtonText("Create Custom Fund List");
    }
   
    /**
     * Renames the name of  the Advanced Filter button with the new name passed as an argument
     * 
     * @param text - new name
     */
    function setAdvancedFilterButtonText(text) {
        var advancedBtn = document.getElementById('viewAdvancedBtn');
        advancedBtn.style.display = 'none';
        advancedBtn.value = text;
        advancedBtn.style.display = '';
    }
   
    /**
     * Show / Hide the Advanced Filter section
     */
    function toggleAdvancedOptions() {
        utilities.clearMessages();
        var section = document.getElementById('advancedX');
        if (section.style.display=='none') {
            setAdvancedFilterButtonText("Hide Customization");
            enableOrDisableApplyFiltersButton();
           // applyBaseFilter();
            show('btn_newQuery');
            show('btn_editQuery');
            setQueryResultLabel("Query Results:");    
            show('advancedX');            
            disableBaseFilters();
            startNewQuery(); 
        } else {        
            resetAdvancedFilters();
        }
        
        if (document.getElementById("groupBySelect").selectedIndex != 0) {
            currentSortOptionIndex = 0;
        } else {
            currentSortOptionIndex = 1;
        }
        
        document.getElementById('sortPreferenceSelect').selectedIndex = currentSortOptionIndex;
        
        var baseView = document.getElementById('baseFilterSelect');
        if (baseView  && baseView.value == "allFunds")  {
            show("saveThisListSection");
        } else {
            hide("saveThisListSection");
        }

        
        window.location.hash='anchor1';

    }

    /**
     * Dummy method
     */
    function saveQueryAs() {
        var queryName = document.getElementById("saveQueryName").value;        
        //alert("PROTOTYPE DEBUG NOTICE:   Query would normally be saved and appear instantly under My Saved Queries in the listing.");
    }
   
    /**
     * Dummy method
     */
    function saveFundList() {
    
    }

    /**
     * Based on the options selected, shows/hides the 'Go' button
     */
    function outputSelected() {
        hide('goButton');
        if (nothingSelected('outputSelect')) {
            setSelectedIndex('outputSelect', 0);
        } else {
            show('goButton');
        }    
    }

    /**
     * Dummy method to validate the 'Go' button
     */
    function goButtonClicked() {
        hide('goButton');
        window.open('sampleReport.pdf',null,"height=300,width=400,status=yes,toolbar=no,menubar=no,location=no");
        setSelectedIndex('outputSelect',0);
    }

    /**
     * Validates the current selection made in the drop-down
     * 
     * @param selectBox - this can either be the id of the drop-down or 
     *                    the drop-down HTML Element itself 
     * 
     * @return true - if the selected options is invalid 
     */
    function nothingSelected(selectBox) {
        var selObj;
        if (typeof selectBox == 'string') {
            selObj = document.getElementById(selectBox);
        } else if (selectBox && typeof selectBox == "object") {
            selObj = selectBox;
        }
    
        if (selObj.selectedIndex == -1) {
            return true;
        } else if (selObj.value == "N/A" || selObj.value == "" || selObj.value == "-2" || selObj.value == "-1") {
            return true;        
        } else {
            return false;
        }
    }

    /**
     * Sets the selected Index for a drop-down 
     * 
     * @param selectBox - this can either be the id of the drop-down or 
     *                    the drop-down HTML Element itself 
     * @param toIndex - index
     */
    function setSelectedIndex(selectBox, toIndex) {
        var selObj;
        if (typeof selectBox == 'string') {
            selObj = document.getElementById(selectBox);
        } else if (selectBox && typeof selectBox == "object") {
            selObj = selectBox;
        }
       
        selObj.selectedIndex = toIndex;
    }

    /**
     * Resets the selected option to default, if the current 
     * selection is invalid
     * 
     * @param selectBox - Id of the drop-down which is to be reset
     */
    function customChange(selectBox) {
        if (nothingSelected(selectBox)) {
            setSelectedIndex(selectBox, 0);
        }
    }
    
    /**
     * Shows the Include NML funds and Include Closed Funds check-boxes,
     * based on the selected View By option.
     */
    function includeNMLOrClosedFunds() {
        baseView = document.getElementById('baseFilterSelect');
        if (baseView.value == "allFunds") {
            if (document.getElementById('showNML').value == 'true') {
                show('includeNMLSection');
            }
            show('closedFunds');
        } else {
            hide('includeNMLSection');
            hide('closedFunds');
        }
    }
    
    /**
     * Shows the checkbox "Display only SIG+ Funds" while selecting Signature + in class Drop down for All Funds
     * Shows the checkbox "Display only SIG+ Funds" while JHI Indicator flag is true for Contract Funds
    */
    function showOrHideOnlySigPlusFunds(jhiIndicatorFlg,svpIndicatorFlg){
		if (document.fapForm.baseFilterSelect.value == "contractFunds") {
			if ((jhiIndicatorFlg!=undefined && jhiIndicatorFlg == true)|| (svpIndicatorFlg!=undefined && svpIndicatorFlg == true)){
				show('signaturePlusFunds');
			}else{  
				hide('signaturePlusFunds');
			}
		}else{
			var e = document.getElementById('classSelect');			
			if(e!=undefined){ 
				var selectedClass = e.options[e.selectedIndex].value;
				if(selectedClass == 'CX0'){ 
					show('signaturePlusFunds');
				}else{ 
					hide('signaturePlusFunds');
				}
			}else{ 
				hide('signaturePlusFunds');
			}
		}
    }

    /**
     * Restrict the users from entering invalid characters
     * 
     * @param textfield - text box element
     * @param character to be validated
     */
    function restrictCharacters(textfield, character) {
         var txt = textfield.value;
         var len = txt.length;
         var result = '';
         for (var i=0; i<len; i++) {
            if (txt.charAt(i) == character) {
                result += character;
            }
         }
         textfield.value = result;
    }
    
    /**
     * Enables or disables the delete button, based on the option selected
     * 
     * @param selectBox - drop-down element to validated the selected option
     * @param deleteButton - id of the Delete Button
     */
    function enableOrDisableDeleteButton(selectBox, deleteButton) {
        if (selectBox.selectedIndex !=0) {
            enable(deleteButton);
        } else {
            disable(deleteButton);
        }
    }

    /**
     * Shows Save list section based on the Selected Funds Slosh box
     * 
     * @param sectionId - id of the save list <div> tag
     * @param fundSelectionNotRequired
     */
    function enableSaveFundListSection(sectionId, fundSelectionNotRequired) {
        selectedFundsSelectLength = document.getElementById("selectedFundsSelect").length;
        if (selectedFundsSelectLength != 0 || fundSelectionNotRequired) {
            show(sectionId);
            document.getElementById("saveListName").value = "";
            if (selectedFundsSelectLength == 0) {
                disable("saveListButton");
            }
        }
    }
    
    function validateMetricSelection() {
    	if(document.getElementById('morningStarCheckBox').checked == true ||
    			document.getElementById('fi360CheckBox').checked == true ||
    			document.getElementById('rpagCheckBox').checked == true) {
    		enable('fundScorecardMetricsConfirmationButton');
    	} else {
    		disable('fundScorecardMetricsConfirmationButton');
    	}
    }
