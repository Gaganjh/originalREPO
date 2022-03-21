var oPanelDetail;
var oPanelPreview;
var oPanelIndex;
var loadingPanel;

var lifeStyleAssetClassId = "LSF";
var lifeCycleAssetClassId = "LCF";
var giflAssetClassId = "LSG"

//This array object is used to pass as an argument to the call-back function
var callbackArgument = new Array();     
    
// Preview
YAHOO.util.Event.addListener("preview", "click", displayPreview);
    
/**
* An associated JavaScript array object to store the action URLs.
*/
var actionURL = {
    "Ievaluator" : {
        "Base"          : "/do/fundEvaluator/?action=getInvOptionDetails&resetFilters=Y",
        "filter"        : "/do/fundEvaluator/?action=getInvOptionDetails&resetFilters=N",
        "UpdateFunds"   : "/do/fundEvaluator/?action=updateInvOptionDetails",
        "printPreview"  : "/do/fundEvaluator/?action=launchPrintWindow"
    }
}

var utilities = {

    // Utility function for parsing JSON string
    parseResponseToJSON : function(o) {
        try { 
            return YAHOO.lang.JSON.parse(o.responseText); 
        } catch (x) { 
            //alert("JSON Parse failed!" + x    ); 
            return "parseError"; 
        } 
    },
    
    // Asynchronous request call to the server. 
    doAsyncRequest : function(actionPath, callbackFunction) {
        YAHOO.util.Connect.setForm(document.fundEvaluatorForm);
        // Make a request
        var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);
    },
    
    // Set call back arguments  
    setCallbackArgument : function (argument) {
        // remove the array elements. 
        // Note: don't declare a new Array object, the call-back function will not take the new Object
        callbackArgument.splice(0, callbackArgument.length);
        callbackArgument[0] = argument;
    },
    
    
    // Generic function to handle a failure in the server response  
    handleFailure : function(o){ 
        o.argument = null;
        utilities.hideWaitPanel();
        //alert("some error happened in the loading jsp");
    },
        
    // Shows loading panel message
    showWaitPanel : function() {
        waitPanel = document.getElementById("wait_c");
        if (waitPanel == undefined || waitPanel.style.visibility != "visible") {
            loadingPanel = new YAHOO.widget.Panel("wait",  
                                {   width: "250px", 
                                    height:"50px",
                                    fixedcenter: true, 
                                    close: false, 
                                    draggable: false, 
                                    zindex:4,
                                    modal: true,
                                    visible: false,
                                    constraintoviewport: true
                                } 
                            );
            loadingPanel.setBody("<span style='padding-left:20px;float:right;padding-right:30px;padding-top:12px;'>One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
            loadingPanel.render(document.body);
            loadingPanel.show();
        }       
    },

    /**
    * hides the loading panel
    */
    hideWaitPanel: function () {
        loadingPanel.hide();
    }
    
 }
    
// Dialog box for funds will be deselected warning message
var dialogWarningFundsDeselect = {
    // Shows the warning message with Yes\No buttons.
    show : function(messageWarningFundsDeselect) {
        var dialogFundsDeselectbuttons = [ 
                        { text:"Yes", handler:dialogWarningFundsDeselect.fundsDeselectDialogYes, isDefault:true},
                        { text:"No", handler:dialogWarningFundsDeselect.fundsDeselectDialogNo } 
                    ];

        dialogFundsDeselect = new YAHOO.widget.SimpleDialog("dlg", { 
                width: "30em", 
                fixedcenter:true,
                modal:true,
                visible:false,
                draggable:false });
    
            dialogFundsDeselect.setHeader("Warning!");
            dialogFundsDeselect.cfg.queueProperty("buttons", dialogFundsDeselectbuttons);
            dialogFundsDeselect.setBody(messageWarningFundsDeselect);
            dialogFundsDeselect.render(document.body);
            dialogFundsDeselect.show();
    },
    // Handler function for Yes button
    fundsDeselectDialogYes : function() {
        document.fundEvaluatorForm.action="?action=" + narrowYourList + "&page=" + navigateToPrevious;
        document.fundEvaluatorForm.submit();
        this.hide();
    },  
    // Handler function for No button
    fundsDeselectDialogNo : function() {
        this.hide();
    }
}

// Call back function(AJAX) for get assetclass funds listing.
var callback_displayAssetClassFunds =    {
    cache : false,
    success : displayDetailPanel,
    failure : utilities.handleFailure,
    argument : callbackArgument
};

//  Displays detailed panel.
var displayDetailPanel = function(o) {
    document.getElementById("detailPanel").innerHTML = o.responseText;
    utilities.hideWaitPanel();
    showDetailPanel();
}
    
/**
*   for retrieving fund classes for asset class
*/
function getFundsForAssetClass() {
    utilities.showWaitPanel();
    utilities.doAsyncRequest(actionURL["Ievaluator"].filter, callback_displayAssetClassFunds);
}

/**
* Displays the asset class funds on the panel(Detailed overlay)
**/
function showAssetClassFunds() {
    document.fundEvaluatorForm.assetClassId.value = this.id; // Asset class Id
    document.fundEvaluatorForm.rankingOrder.value = "ASC"; // Ranking order set to default value in document.
    utilities.showWaitPanel();
    utilities.doAsyncRequest(actionURL["Ievaluator"].Base, callback_displayAssetClassFunds);
}

// Displays the panel.
function showDetailPanel() {
    if (oPanelDetail == null) {
        oPanelDetail = new YAHOO.widget.Panel("detailPanel",  { fixedcenter:true ,width:"800px", height:"580px", draggable:false, close:false, fixedcenter:true , zindex:4, visible:true, modal:true});
    }
    oPanelDetail.render();
    oPanelDetail.show();
}

// Cancel the selected funds and close the detail panel
function doCancel() {
    hidePanelDetail();
    hidePanelPreview();
    document.getElementById('detailPanel').innerHTML = "";
    document.getElementById('indexPanel').innerHTML = "";
    document.getElementById('previewPanel').innerHTML = "";
}

// Hides the detail panel
function hidePanelDetail() {
    if(oPanelDetail != null) {
        oPanelDetail.hide();
    }
}

// Hides the preview panel
function hidePanelPreview() {
    if(oPanelPreview != null) {
        oPanelPreview.hide();
    }
}
    
// Displays preview panel to user.
function showPanelPreview() {
    if(oPanelPreview == null) {
        oPanelPreview = new YAHOO.widget.Panel("previewPanel",  { fixedcenter:true ,width:"800px", height:"580px", draggable:false, close:false, fixedcenter:true , zindex:4, visible:true, modal:true});       
    }
    oPanelPreview.render();
    oPanelPreview.show();
}
    

// Displays the preview to the user.
function displayPreview() {
    document.fundEvaluatorForm.assetClassId.value = "previewFunds";
    document.fundEvaluatorForm.rankingOrder.value = "ASC"; // Ranking order set to default value in document.
    utilities.showWaitPanel();
    utilities.doAsyncRequest(actionURL["Ievaluator"].Base, callback_displayFundsInformationPreview);
}

/**
* Call the asynchronous call when user filtering from the preview overlay*
*/
function displayPreviewFilter() {
    document.fundEvaluatorForm.assetClassId.value = "previewFunds";
    utilities.showWaitPanel();
    utilities.doAsyncRequest(actionURL["Ievaluator"].filter, callback_displayFundsInformationPreview);
}

// Call back handler for get assetclass funds listing when user hits on ranking order.
var callback_displayFundsInformationPreview =    {
    cache : false,
    success : function(o) {
        document.getElementById("previewPanel").innerHTML = o.responseText;
        showPanelPreview();
        utilities.hideWaitPanel();
    },
    failure : utilities.handleFailure,
    argument : callbackArgument
    };

// Updates the selected funds from the detailed panel 
function saveAndClosePanel() {
    updateSelectedFunds();
    //hidePanelDetail();
}

// Updates the selected funds from the preivew panel 
function saveAndCloseFromPreview() {
    updateSelectedFunds();
    //hidePanelPreview();
}
    
//  Handles the user request for investment options in Asc/desc order of ranking
function orderByRank(orderBy) {
    document.fundEvaluatorForm.rankingOrder.value = orderBy;
    utilities.showWaitPanel();
    utilities.doAsyncRequest(actionURL["Ievaluator"].filter, callback_displayFundsInformationByRankingOrder );
}
    
// Call back handler for get assetclass funds listing when user hits on ranking order.
var callback_displayFundsInformationByRankingOrder =     {
    cache :false,
    success : function(o) {
        if(document.fundEvaluatorForm.assetClassId.value == "previewFunds") {
            document.getElementById("previewPanel").innerHTML = o.responseText;
        } else {
            document.getElementById("detailPanel").innerHTML = o.responseText;
        }
        utilities.hideWaitPanel();
    },
    failure : utilities.handleFailure,
    argument : callbackArgument
};
        
// Index panel
function showAssetClassIndexFunds() {
    document.fundEvaluatorForm.assetClassId.value = this.id;
    utilities.showWaitPanel();
    utilities.doAsyncRequest(actionURL["Ievaluator"].Base, callback_displayIndexFundsInformation );
}

// Shows index panel
function showIndexPanel() {
    if(oPanelIndex == null) {
        oPanelIndex = new YAHOO.widget.Panel("indexPanel",  { fixedcenter:true ,width:"800px", height:"530px", draggable:false, close:false, fixedcenter:true , zindex:4, visible:true, modal:true});       
    }
    oPanelIndex.render();
    oPanelIndex.show();
}

// Shows the index funds on the index panel.
var successHanlderIndex = function(o) {
    document.getElementById("indexPanel").innerHTML = o.responseText;
    utilities.hideWaitPanel();
    showIndexPanel();
}
    
// Call back function for get assetclass funds listing.
var callback_displayIndexFundsInformation =      {
    cache :false,
    success : successHanlderIndex,
    failure : utilities.handleFailure,
    argument : callbackArgument
};
    
function closePanelIndex() {
    oPanelIndex.hide();
}

/**
*Process the life cycle and SVF funds and SVF competing funds.
*
**/
function processFundSelectionStatus(fundSelected) {
    processLifeCycleFunds(fundSelected);
    processCompetingFunds(fundSelected);
    processGIFLSelectFunds(fundSelected);
    updateIcons();
}

function updateIcons() {
	
	 var fundsDisplayed = document.fundEvaluatorForm.selectedFunds;
	 
	 if (fundsDisplayed.type && fundsDisplayed.type === 'checkbox') {
		 displayIcons(fundsDisplayed);
	 }
	 
	 for(var index=0; index < fundsDisplayed.length; index++ ) {
		 displayIcons(fundsDisplayed[index]);
	 }    
}

function displayIcons(fundDisplayed) {

	var assetClsAndFundIdName = fundDisplayed.value.split("-");
	var selectedFundId = assetClsAndFundIdName[1];

	var existingContractFundIcon = document.getElementById(selectedFundId + "-E");
	var calcIcon = document.getElementById(selectedFundId + "-C");
	var manIcon = document.getElementById(selectedFundId + "-A");
	var delIcon = document.getElementById(selectedFundId + "-D");

	if (fundDisplayed.checked == true) {
		if (calcIcon == null && existingContractFundIcon == null) {
			manIcon.style.display = "";
		} else {
			delIcon.style.display = "none";
		}
	} else {
		if (calcIcon != null || existingContractFundIcon != null) {
			delIcon.style.display = "";
		} else {
			manIcon.style.display = "none";
		}
	}

}

/**
 * Checks or unchecks all the Lifecycle Retirement Living funds if user checks or uncheck any of 
 * 				the lifecycle Retirement Living fund. 
 * Checks or unchecks all the Lifecycle Retirement Choices funds if user checks or uncheck any of
 * 				the lifecycle Retirement Choices fund.
 */
function processLifeCycleFunds(fundSelected) {
    var separator = "-";
    var assetClsAndFundIdName = fundSelected.value.split(separator);
    var selectedFundAssetClsId = assetClsAndFundIdName[0] ;
    var selectedFundId = assetClsAndFundIdName[1];
    var selectedFundFamilyCode = assetClsAndFundIdName[3];
    var fundsDisplayed = document.fundEvaluatorForm.selectedFunds;

    if(selectedFundAssetClsId == lifeCycleAssetClassId) {
        // if LCF fund is checked/unchecked
        for(var index=0; index < fundsDisplayed.length; index++ ) {
            assetClsAndFundIdName = fundsDisplayed[index].value.split("-");
            if (assetClsAndFundIdName[0] == lifeCycleAssetClassId
					&& assetClsAndFundIdName[3] == selectedFundFamilyCode) {
            	// if the fund being iterated is of the same family as the selected fund
				if (fundSelected.checked == true) {
					fundsDisplayed[index].checked = true;
				} else {
					fundsDisplayed[index].checked = false;
				}
			}
        }
    }
}

/**
* Process SVF funds & SVF competing funds.
* if SVF fund is selected, then automatically unchecks the SVF competing funds from the overlay.
* if SVF competing fund is selected, then automatically unchecks the SVF funds from the overlay.
*
**/
function processCompetingFunds(fundSelected) {
    var separator = "-";
    var assetClsAndFundIdName = fundSelected.value.split(separator);
    var selectedFundAssetClsId = assetClsAndFundIdName[0] ;
    var selectedFundId = assetClsAndFundIdName[1];
    var svfCompetingFund = isSvfCompetingFund(selectedFundId);  
    var svfFund = isSvfFund(selectedFundId);
    var message = "Funds competing against this fund have been deselected."
        
    if( fundSelected.checked && svfFund) {
        // Uncheck all SVF Competing funds from the display overlay.
        if(unCheckSvfCompetingFunds(selectedFundId) || document.fundEvaluatorForm.selectedAnySVFCompeting.value == 'true') {
            if(document.fundEvaluatorForm.assetClassId.value == 'previewFunds') {
                document.getElementById("competingFundInfoPreview").innerHTML = message;
            } else {
                document.getElementById("competingFundInfo").innerHTML = message;
            }
        }
    } else  if(fundSelected.checked && svfCompetingFund) {
        // Uncheck all SVF Competing funds from the display overlay.
        if(unCheckSvfFunds() || document.fundEvaluatorForm.selectedAnySVF.value == 'true') {
            if(document.fundEvaluatorForm.assetClassId.value == 'previewFunds') {
                document.getElementById("competingFundInfoPreview").innerHTML = message;
            } else {
                document.getElementById("competingFundInfo").innerHTML = message;
            }
        }
    } else {
        if(document.getElementById("competingFundInfo") != null) {
            document.getElementById("competingFundInfo").innerHTML = "";
            }
        if(document.getElementById("competingFundInfoPreview") != null) {
            document.getElementById("competingFundInfoPreview").innerHTML = "";
        }
    }
}

/**
* Checks or unchecks GIFL Select funds according to "optional" or "mandatory" GIFL v3 funds selected.	
*
**/
function processGIFLSelectFunds(fundSelected) {
    var separator = "-";
    var assetClsAndFundIdName = fundSelected.value.split(separator);
    var selectedFundAssetClsId = assetClsAndFundIdName[0] ;
    var selectedFundId = assetClsAndFundIdName[1];
    var isSelectedFundOptional = assetClsAndFundIdName[2];
    var fundsDisplayed = document.fundEvaluatorForm.selectedFunds;

    if(selectedFundAssetClsId == giflAssetClassId) {

        for(var index=0; index < fundsDisplayed.length; index++ ) {
            assetClsAndFundIdName = fundsDisplayed[index].value.split("-");
            if (assetClsAndFundIdName[0] == giflAssetClassId) {
            	if(isSelectedFundOptional =="true"){ // Optional fund selected
            		if(fundSelected.checked == true){
            			// If the user checks any of the "optional" GIFL v3 funds, all mandatory and optional v3 funds will be selected
            			fundsDisplayed[index].checked = true;
            		}else{
            			// If the user unchecks any of the "optional" GIFL v3 funds, all optional v3 funds will be deselected.
            			if(assetClsAndFundIdName[2] == "true"){
            				fundsDisplayed[index].checked = false;
            			}
            		}
            	}else{ //Mandatory fund selected
            		if(fundSelected.checked == true){
            			// If the user checks any of the "mandatory" GIFL v3 funds, all mandatory v3 funds will be selected
            			if(assetClsAndFundIdName[2] == "false"){
            				fundsDisplayed[index].checked = true;
            			}
            		}else{
            			// If the user unchecks any of the "mandatory" GIFL v3 funds, all mandatory and all optional v3 funds will be deselected
            			fundsDisplayed[index].checked = false;
            		}
            	}
             }
        }
        
    }
}

/**
*   for updating the selected funds.
*/
function updateSelectedFunds() {
    utilities.doAsyncRequest(actionURL["Ievaluator"].UpdateFunds, callback_saveAndCloseSelectedFunds );
}
        
// Call back handler for save and close from panels.
var callback_saveAndCloseSelectedFunds =     {
    cache : false,
    success : function(o) {
        fundsUpdateResults = utilities.parseResponseToJSON(o);
        if(fundsUpdateResults == "parseError") {
            // if error occur during JSON parse 
            if(document.fundEvaluatorForm.assetClassId.value != "previewFunds") {
                document.getElementById("detailPanel").innerHTML = o.responseText;
                }
            else {
                document.getElementById("previewPanel").innerHTML = o.responseText;
            }
            return;
        } else  {
            if(document.fundEvaluatorForm.assetClassId.value != "previewFunds") {
                hidePanelDetail();
                }
            else {
                hidePanelPreview();
            }

            // Updates the result on asset house page.
            if(fundsUpdateResults.assetClassSelectedFunds != '0') {
                updateTotalFunds(fundsUpdateResults.totalSelectedFunds);
                updateAssetClassBox(fundsUpdateResults);
            }
            if(document.getElementById('tableContainerPreview') != null) {
                document.getElementById('tableContainerPreview').innerHTML = "";
                }
            if(document.getElementById('tableContainer') != null) {
                document.getElementById('tableContainer').innerHTML = "";
            }
            
            document.fundEvaluatorForm.selectedAnySVF.value = fundsUpdateResults.selectedAnySVF;
            document.fundEvaluatorForm.selectedAnySVFCompeting.value = fundsUpdateResults.selectedAnySVFCompeting;
        }
    },
    failure : utilities.handleFailure,
    argument : callbackArgument 
};
    
// Displays the updated total number of funds on the screen
function updateTotalFunds(totalFunds) {
    document.getElementById("totalSelectedFunds").innerHTML = totalFunds;
    
    if(totalFunds == 0) {
        document.getElementById('previewLinkDisabled').style.display = '';
        document.getElementById('previewLinkEnabled').style.display = 'none';
    } else {
        document.getElementById('previewLinkDisabled').style.display = 'none';
        document.getElementById('previewLinkEnabled').style.display = '';
    }
        
}
    
// Updates the asset class boxes on screen based on the call back resulsts.
    function updateAssetClassBox(fundsUpdateResults) {        
        var assetClassBoxIdLSF;
        var fundCountElementIdLSF;
        var selectedFundsLSFGIFL = 0;
        for(var assetClassInd = 0; assetClassInd < fundsUpdateResults['assetClasses'].length; assetClassInd++) {
            var assetClass = fundsUpdateResults['assetClasses'][assetClassInd].assetClassId;
            if(assetClass == giflAssetClassId) {
            	assetClass = lifeStyleAssetClassId;
            }
            var assetClassBoxId = document.getElementById(assetClass);
            if (assetClassBoxId) {
                
                var fundCountElementId;
                var selectedFunds;

                if(assetClass == lifeStyleAssetClassId){
                    assetClassBoxIdLSF = document.getElementById(lifeStyleAssetClassId);
                    fundCountElementIdLSF =  "fund-count-" + lifeStyleAssetClassId;
                    selectedFundsLSFGIFL = selectedFundsLSFGIFL + parseInt(fundsUpdateResults['assetClasses'][assetClassInd].selectedFunds);
                } else {
                	fundCountElementId =  "fund-count-" + assetClassBoxId.id;
                	selectedFunds = parseInt(fundsUpdateResults['assetClasses'][assetClassInd].selectedFunds);
                	changeAssetClassBox(assetClassBoxId, fundCountElementId, selectedFunds);
                }
                
            }
        }
        // for LSF & GIFL select box.
        changeAssetClassBox(assetClassBoxIdLSF, fundCountElementIdLSF, selectedFundsLSFGIFL);
    }

    function changeAssetClassBox(assetClassBoxId, fundCountElementId, selectedFunds) {
        if (selectedFunds > 0 ) {
            document.getElementById(fundCountElementId).innerHTML = selectedFunds;
        } else {
            document.getElementById(fundCountElementId).innerHTML = "";
        }
    }

// Printer preivew action method    
function printPreview(csrf) {
   var bodystr = document.getElementById("previewPanel").innerHTML;
   var printForm = createNewSubmitForm("printPreviewForm");
   createNewFormElement(printForm, "_csrf", csrf);
   createNewFormElement(printForm, "fundEvalHtmlPrintPreviewString", bodystr);
   //printForm.action= "/do/fundEvaluator/?action=launchPrintWindow";
   printForm.action = actionURL["Ievaluator"].printPreview;
   printForm.target="_blank";
   printForm.submit();        
}

// Helper function to create form element
function createNewSubmitForm(name){
   var submitForm = document.createElement("FORM");
   document.body.appendChild(submitForm);
   submitForm.method = "POST";
   submitForm.name = name;
   return submitForm;
}

//helper function to add elements to the form
function createNewFormElement(inputForm, elementName, elementValue){
   //var newElement = document.createElement("<input name='"+elementName+"' type='hidden'>");
   var newElement = document.createElement("input");
   newElement.setAttribute("name", elementName);
   newElement.setAttribute("type", "hidden");
   inputForm.appendChild(newElement);
   newElement.value = elementValue;
   return newElement;
}


function openCompetingPDF(){
	window.open('/assets/pdfs/Stable_Value_Competing_Fund_Guide.pdf',null,"width=800,height=450,left=10,top=10");
}


