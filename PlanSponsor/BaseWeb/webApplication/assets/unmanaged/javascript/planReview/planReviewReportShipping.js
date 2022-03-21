//This javascript file is used to scrpiting common functionality in ContractReviewReportPrint Pages

var oPanelDetail;
var oPanelPreview;
var oPanelIndex;
var loadingPanel;

var lifeStyleAssetClassId = "LSF";
var lifeCycleAssetClassId = "LCF";
var lifeCycleAndLifeStyleAssetClassId = "LSC";
var balancedAssetClassId = "BAL";
var balancedGIFLAssetClassId = "LSG"

//This array object is used to pass as an argument to the call-back function
var callbackArgument = new Array();     
    
// Preview
YAHOO.util.Event.addListener("shipping", "click", doShipping);
YAHOO.util.Event.addListener("confirm", "click", displayConfirm);
/**
* An associated JavaScript array object to store the action URLs.
*/
var actionURL = {
    "ContractPrint" : {
        "HistoryPrint"          : "/do/bob/planReview/History/?task=shippingDetails&isPageRegularlyNavigated=true",
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
    doAsyncRequest : function(actionPath, callbackFunction,jsonObj) {
        YAHOO.util.Connect.setForm(document.planReviewReportForm);
        // Make a request
        var request = YAHOO.util.Connect.asyncRequest('POST', actionPath+"&jsonObj="+jsonObj, callbackFunction);
        //var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);
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
  
// Cancel the selected funds and close the detail panel
function doCancel() {
    hidePanelDetail();
    hidePanelPreview();
    document.getElementById('shippingPanel').innerHTML = "";
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
    	/*alert("aftr hide");*/
        oPanelPreview.hide();
    }
}
    
// Displays preview panel to user.
function showPanelPreview() {
    if(oPanelPreview == null) {
        oPanelPreview = new YAHOO.widget.Panel("shippingPanel",  { fixedcenter:true ,width:"500px", height:"380px", draggable:false, close:false, fixedcenter:true , zindex:4, visible:true, modal:true});       
    }
    oPanelPreview.render();
    oPanelPreview.show();
}

// Displays the preview to the user.
function doShipping(obj) {
	var jsonObj=obj;
    utilities.showWaitPanel();
    utilities.doAsyncRequest(actionURL["ContractPrint"].HistoryPrint, callback_displayShippingInfo,jsonObj);
}


// Call back handler for Shipping details.
var callback_displayShippingInfo =    {
    cache : false,
    success : function(o) {
    	

		if(o.responseText.match("planReviewReportsUnavailablePage")) {
			top.location.reload(true);
			return;
		}
		
		if(o.responseText.match("public_home")) {
			top.location.reload(true);
			return;
		}
    	
        document.getElementById("shippingPanel").innerHTML = o.responseText;
        showPanelPreview();
        utilities.hideWaitPanel();
    },
    failure : utilities.handleFailure,
    argument : callbackArgument
    };

