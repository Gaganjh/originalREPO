//This javascript file is used to scripting functionality in ContractReviewReport Customization Page



//Functions to set Presenter's Name
$(document).ready(function() {

	$(".applyAll").on('click',function(e) {
		e.preventDefault();	
		$(".applyAll img").css("cursor", "default");
			applyAllInd =false;
			var value = $('.textbox_name:first').val();
			$(".textbox_name").each(function() {
				this.value = value;
				$(this).prop('title', value);
			});
	});
	
	$(".textbox_name").on('keyup',function(e){
		$(this).prop("title", $(this).val());
	});
	
	$(".textbox_name").on('change',function() {
		$("input[name='presenterName']").each(function() {
			this.checked = false;
		
		});

	});
});


//Below functionality for displaying Pop up window using YAHOO Utils

var oPanelDetail;
var oPanelPreview;
var oPanelIndex;
var loadingPanel;

//This array object is used to pass as an argument to the call-back function

var callbackArgument = new Array();

// Preview
YAHOO.util.Event.addListener("preview", "click", coverImage);

YAHOO.util.Event.addListener("companyName", "change", companyNameChange);

YAHOO.util.Event.addListener("choose", "change", imageChange);
/**
 * An associated JavaScript array object to store the action URLs.
 */
var actionURL = {
	"Imaging" : {
		"CoverImg" : "/do/bob/planReview/Customize/?task=coverImagePage&isPageRegularlyNavigated=true",
		"UploadLogo" : "/do/bob/planReview/Customize/?task=uploadLogoPage&isPageRegularlyNavigated=true",
		"CoverImgContract" : "/do/bob/contract/planReview/Customize/?task=coverImagePage&isPageRegularlyNavigated=true",
		"UploadLogoContract" : "/do/bob/contract/planReview/Customize/?task=uploadLogoPage&isPageRegularlyNavigated=true",
	}
}
var utilities = {

	// Utility function for parsing JSON string
	parseResponseToJSON : function(o) {
		try {
			return YAHOO.lang.JSON.parse(o.responseText);
		} catch (x) {
			return "parseError";
		}
	},

	// Asynchronous request call to the server. 
	doAsyncRequest : function(actionPath, callbackFunction) {
		YAHOO.util.Connect.setForm(document.getElementById('planReviewReportForm'));
		// Make a request
		var request = YAHOO.util.Connect.asyncRequest('POST', actionPath,
				callbackFunction);
	},
	// Asynchronous request call to the server. 
	doAsyncPostRequest : function(actionPath, callbackFunction) {
		//YAHOO.util.Connect.setForm(document.planReviewReportForm);
		// Make a request
		var request = YAHOO.util.Connect.asyncRequest('POST', actionPath,
				callbackFunction);
	},

	// Set call back arguments  
	setCallbackArgument : function(argument) {
		callbackArgument.splice(0, callbackArgument.length);
		callbackArgument[0] = argument;
	},

	// Generic function to handle a failure in the server response  
	handleFailure : function(o) {
		o.argument = null;
		utilities.hideWaitPanel();
	},

	// Shows loading panel message
	showWaitPanel : function() {
		waitPanel = document.getElementById("wait_c");
		if (waitPanel == undefined || waitPanel.style.visibility != "visible") {
			loadingPanel = new YAHOO.widget.Panel("wait", {
				width : "250px",
				height : "50px",
				fixedcenter : true,
				close : false,
				draggable : false,
				zindex : 4,
				modal : true,
				visible : false,
				constraintoviewport : true
			});
			loadingPanel
					.setBody("<span style='padding-left:20px;float:right;padding-right:30px;padding-top:12px;'>One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
			loadingPanel.render(document.body);
			loadingPanel.show();
		}
	},

	/**
	 * hides the loading panel
	 */
	hideWaitPanel : function() {
		loadingPanel.hide();
	}

}

// Hides the detail panel
function hidePanelDetail() {
	if (oPanelDetail != null) {
		oPanelDetail.hide();
	}
}

// Hides the preview panel
function hidePanelPreview() {
	if (oPanelPreview != null) {
		oPanelPreview.hide();
	}
}

// Displays preview panel to user.
function showPanelPreview() {
	if (oPanelPreview == null) {
		oPanelPreview = new YAHOO.widget.Panel("previewPanel", {
			width : "800px",
			height : "530px",
			draggable : false,
			close : false,
			zindex : 4,
			visible : true,
			modal : true
		});
	}
	
	oPanelPreview.render();
	oPanelPreview.center();
	
	var imageIndex = document.forms['planReviewReportForm'].index.value;
	
	var imageObject = uploadLogoImageMap[imageIndex];
	
	if(imageObject != undefined) {
		$('#fileChecker')
	    .prop('src', imageObject);
	} else {
		$('#fileChecker')
	    .prop('src', "/assets/unmanaged/images/s.gif");
	}
	
	oPanelPreview.show();
}
// Displays the cover Image Popup to the user.
function coverImage(contract, index) {
	utilities.showWaitPanel();
	document.forms['planReviewReportForm'].filterContractNumber.value = contract;
	document.forms['planReviewReportForm'].index.value = index;
	if($('.bob_results').val() == 'contract'){
		
		utilities.doAsyncRequest(actionURL["Imaging"].CoverImgContract,
				callback_uploadImageOverlay);
	}else{
		
		utilities.doAsyncRequest(actionURL["Imaging"].CoverImg,
				callback_uploadImageOverlay);
	}
}

var uploadLogoImageMap = new Object(); // or var map = {};


function getUploadLogoImage(key) {
    return uploadLogoImageMap[key];
}


//Displays the Logo  Image Popup to the user.
function uploadLogo(contract, index) {
	utilities.showWaitPanel();
	document.forms['planReviewReportForm'].filterContractNumber.value = contract;
	document.forms['planReviewReportForm'].index.value = index;
	
	if($('.bob_results').val() == 'contract'){
		
		utilities.doAsyncRequest(actionURL["Imaging"].UploadLogoContract,
				callback_uploadImageOverlay);
	}else{
		
		utilities.doAsyncRequest(actionURL["Imaging"].UploadLogo,
				callback_uploadImageOverlay);
	}
}

// Call back handler for displaying the popup window.
var callback_uploadImageOverlay = {
	cache : false,
	success : function(o) {
	
		if(o.responseText.match("CSRF Error")) {
			window.location.href = window.location+"?task=csrfError&isPageRegularlyNavigated=true";
			return;
		}
		
		if(o.responseText.match("planReviewReportsUnavailablePage")) {
			top.location.reload(true);
			return;
		}
		
		if(o.responseText.match("public_home")) {
			top.location.reload(true);
			return;
		}
		
		document.getElementById("previewPanel").innerHTML = o.responseText;
		showPanelPreview();
		utilities.hideWaitPanel();
	},
	failure : utilities.handleFailure,
	argument : callbackArgument
};

// Cancel the detail panel
function doClose() {
	hidePanelDetail();
	hidePanelPreview();
	document.getElementById('previewPanel').innerHTML = "";
}

function doLogoCancel() {
	document.forms['planReviewReportForm'].uploadImage.value = null;
	doClose();
}

function doCoverCancel(){
	document.forms['planReviewReportForm'].uploadCoverImage.value = null;
	doClose();
}

//clears the logo image related values and closes the logo overlay
function doRemove(){
	
	
	var remove= window.confirm("You have selected to remove the uploaded logo.  Please select OK to proceed with this action or Cancel to return to the page.");
	if(!remove){
		// do nothing if user cancels the pop up
	}	else	{
			urlOnload= "";
			// Form values are available for the action class
			YAHOO.util.Connect.setForm(document.getElementById('planReviewReportForm'), true, true);
			
			// Callback method
			var uploadHandler = {
				upload : function(o) {
					
					if(o.responseText.match("planReviewReportsUnavailablePage")) {
						top.location.reload(true);
						return;
					}
					
					if(o.responseText.match("public_home")) {
						top.location.reload(true);
						return;
					}
					
					var response = o.responseText;
					var index = 0;
					index = parseInt(response);
						$("#logoName"+index).text(" ");
						$("#companyName").val(' ');
						$("#companyName").text(" ");
						
						uploadLogoImageMap[index] = undefined;
						
						doClose();
				}
			};
			
			if($('.bob_results').val() == 'contract'){
			YAHOO.util.Connect
			.asyncRequest(
					'GET',
					"/do/bob/contract/planReview/Customize/?task=removeLogo&isPageRegularlyNavigated=true",
					uploadHandler);
			}else{
				YAHOO.util.Connect
				.asyncRequest(
						'GET',
						"/do/bob/planReview/Customize/?task=removeLogo&isPageRegularlyNavigated=true",
						uploadHandler);	
			}
		}
	}
	
var urlOnload;

//Changes the demo lodo image
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
        	//urlOnload=e.target.result;
            $('#fileChecker')
                .prop('src', e.target.result);
            $('#fileChecker')
            .load();
        };

        reader.readAsDataURL(input.files[0]);
    } else if(input.value){
    	
             $('#fileChecker')
                 .prop('src', input.value);
             $('#fileChecker')
             .load();

    }
}
/*function logoImageChange()
{
	var newIamge = $("#chooseLogo").val();
	var logopath = $("#logopath").value();
	$("#fileChecker").attr('src',logopath+newIamge);
	}*/

//Function to handle selected Pre defined images for Cover image
function doSelectedCoverImage(path) {

	document.forms['planReviewReportForm'].pathValue.value = path;
	if ($('.bob_results').val() == 'contract') {
		ajax_getJSON(
				"/do/bob/contract/planReview/Customize/?task=selectedCoverImage&isPageRegularlyNavigated=true",
				{
					path : path
				}, statusChange_callbackMethod);

	} else {
		ajax_getJSON(
				"/do/bob/planReview/Customize/?task=selectedCoverImage&isPageRegularlyNavigated=true",
				{
					path : path
				}, statusChange_callbackMethod);
	}
}
//Callback method for selected Pre defined images for Cover image
statusChange_callbackMethod = function(parsedData) {
	if (parsedData.name.length > 15) {
		$("#coverName" + parsedData.index).text(
				parsedData.name.substring(0, 15));
	} else {
		$("#coverName" + parsedData.index).text(parsedData.name);
	}
	
	
	if( parsedData.isImageSlected == undefined) {
		$("#coverName" + parsedData.index).prop('title', parsedData.name + ".jpg");
	} else {
		$("#coverName" + parsedData.index).prop('title', parsedData.name);
	}
	
	// closing Pop up window
	doClose();
}

//Function to handle upload functionality for Cover image
function onUploadCoverImageClick() {
	 
    var uploadCoverImage =  document.getElementById("uploadCoverImage");
   
	if(uploadCoverImage.value==null || uploadCoverImage.value==""){
		alert("Image can't be uploaded");
		 $("#uploadCoverImage").trigger('click');
	}
    if(uploadCoverImage.value!=null && uploadCoverImage.value!=""){
    	var uploadCoverImageSize = uploadCoverImage.files[0].size/1024/1024;
    	var uploadCoverImageName = uploadCoverImage.value;
		var imageExtension = uploadCoverImageName.split('.').pop();
       if (imageExtension.toLowerCase()!="jpg" && imageExtension.toLowerCase()!="jpeg") {
    	   alert(UploadedCoverImageIsNotJPGError);
    	   $("#uploadImage").trigger('click');
       }else if( uploadCoverImage.files[0].size > 2*1024*1024 ){
    	   alert(UploadedCoverImageGreaterThanTwoMBError);
		}else{
			
	        //Form values are available for the action class
			var formObject = document.getElementById('planReviewReportForm');
			YAHOO.util.Connect.setForm(formObject,true,true);

	
			if($('.bob_results').val() == 'contract'){
				utilities.doAsyncPostRequest(
						"/do/bob/contract/planReview/Customize/?task=uploadCoverImage&isPageRegularlyNavigated=true",
						uploadCoverPageImageHandler);
			}else{
				utilities.doAsyncPostRequest(
						"/do/bob/planReview/Customize/?task=uploadCoverImage&isPageRegularlyNavigated=true",
						uploadCoverPageImageHandler);
						
			}
		}
	}
	

};

//Callback method
	var uploadCoverPageImageHandler = {
		upload : function(o) {
			var response = o.responseText;
			$(".errorSpan").hide();
			
			if(o.responseText.match("CSRF Error")) {				
				window.location.href = window.location+"?task=csrfError&isPageRegularlyNavigated=true";
				return;
			}
			
			if(o.responseText.match("planReviewReportsUnavailablePage")) {
				top.location.reload(true);
				return;
			}
			
			if(o.responseText.match("public_home")) {
				top.location.reload(true);
				return;
			}
			
			var response = o.responseText;
			response = response.replace("<pre>", "");
			response = response.replace("</pre>", "");
			// for Chrome
			response = response.replace('<pre style="word-wrap: break-word; white-space: pre-wrap;">', "");	
			
			// Call back method
			var parsedData = $.parseJSON(response);
			if (parsedData.sessionExpired != undefined) {
				// session expired.... redirecting to login page
					top.location.reload(true);
				} else {
					
					if(parsedData.hasError != undefined) {
						$("#errorValue").html(parsedData.error);
						$(".errorSpan").show();
					} else {
						var index = parsedData.index;
						$(".errorSpan").hide();
						var fileName = parsedData.name;
						if (fileName.length > 15) {
							$("#coverName" + index).text(fileName.substring(0, 15));
						} else {
							$("#coverName" + index).text(fileName);
						}
						
						$("#coverName" + index).prop('title',
								fileName + ".jpg");
						
						// closing Pop up window
						doClose();
						
					}
					
				}
		}
	};
	
	//Callback method
	var uploadLogoImageHandler = {
		upload : function(o) {
		
			if(o.responseText.match("CSRF Error")) {
				window.location.href = window.location+"?task=csrfError&isPageRegularlyNavigated=true";
				return;
			}
			if(o.responseText.match("planReviewReportsUnavailablePage")) {
				top.location.reload(true);
				return;
			}
			
			if(o.responseText.match("public_home")) {
				top.location.reload(true);
				return;
			}
			
			//$(".errorSpan").hide();
			var response = o.responseText;
			response = response.replace("<pre>", "");
			response = response.replace("</pre>", "");
			// for Chrome
			response = response.replace('<pre style="word-wrap: break-word; white-space: pre-wrap;">', "");
			
			// Call back method
			var parsedData = $.parseJSON(response);
			if (parsedData.sessionExpired != undefined) {
				// session expired.... redirecting to login page
					top.location.reload(true);
				} else {
					if(parsedData.hasError != undefined) {
						$("#imageError").html(parsedData.error);
						$("#companyError").text(parsedData.companyError);
						
						//$(".errorSpan").show();
					} else {
						
						var index = parsedData.index;
						
						uploadLogoImageMap[index] = $('#fileChecker').prop('src');
						
						//$(".errorSpan").hide();
						$("#imageError").text("");
						$("#companyError").text("");
						
						var fileName = parsedData.name;
						if (fileName.length > 15) {
							$("#logoName" + index).text(fileName.substring(0, 15));
						} else {
							$("#logoName" + index).text(fileName);
						}
						
						$("#logoName" + index).prop('title',
								fileName + ".jpg");
						
						// closing Pop up window
						doClose();
						
					}
			}
		}
	}

//Function to handle upload functionality for Logo image
function onUploadLogoImage() {
	
	$("#imageError").text("");
	$("#companyError").text("");
	
	var uploadImage =  document.getElementById("uploadImage");
	   
	if(uploadImage.value==null || uploadImage.value==""){
		alert("Image can't be uploaded");
		 $("#uploadImage").trigger('click');
	}
    if(uploadImage.value!=null && uploadImage.value!=""){
		var uploadImageSize = uploadImage.files[0].size/1024/1024;
		var uploadImageName = uploadImage.value;
		var imageExtension = uploadImageName.split('.').pop();
       if (imageExtension.toLowerCase()!="jpg" && imageExtension.toLowerCase()!="jpeg") {
    	   alert(UploadedLogoImageIsNotJPGError);
       }else if( uploadImage.files[0].size > 1024*1024 ){
    	   alert(UploadedLogoImageGreaterThanOneMBError);
    	   $("#uploadImage").trigger('click');
		}else{
			YAHOO.util.Connect.setForm(document.getElementById('planReviewReportForm'), true, true);
			
		
			if($('.bob_results').val() == 'contract'){
				YAHOO.util.Connect
				.asyncRequest(
						'POST',
						"/do/bob/contract/planReview/Customize/?task=uploadLogoImage&isPageRegularlyNavigated=true",
						uploadLogoImageHandler);
				
			}else{
			YAHOO.util.Connect
					.asyncRequest(
							'POST',
							"/do/bob/planReview/Customize/?task=uploadLogoImage&isPageRegularlyNavigated=true",
							uploadLogoImageHandler);
			}
		}
    }

};


function companyNameChange() {
if($("#companyName").val()==""){
	} else{
	$("#okUploadButton").prop("disabled",false);
	}
				//$("#okUploadButton").attr("disabled","");
}

function imageChange() {
	$("#companyNameDiv").hide();
$("#okUploadButton").prop("class","grey-btn back");
		$("#okUploadButton").prop("disabled",true);
}

//Opens the PDF in new window for PrevewCoverPage.
function openView(url, contract, index,csrf) {
	var form = document.createElement("form");
	form.action = url;
	form.method = 'POST';
	form.target = "_blank";

	var csrfInput = document.createElement("input");
	csrfInput.setAttribute("type", "hidden");
	csrfInput.setAttribute("name", "_csrf");
	csrfInput.setAttribute("value", csrf);
	form.appendChild(csrfInput);
	
	var taskInput = document.createElement("input");
	taskInput.setAttribute("type", "hidden");
	taskInput.setAttribute("name", "task");
	taskInput.setAttribute("value", "previewCoverPage");
	form.appendChild(taskInput);
	
	var contractInput = document.createElement("input");
	contractInput.setAttribute("type", "hidden");
	contractInput.setAttribute("name", "filterContractNumber");
	contractInput.setAttribute("value", contract);
	form.appendChild(contractInput);
	
	var contractInput = document.createElement("input");
	contractInput.setAttribute("type", "hidden");
	contractInput.setAttribute("name", "pageRegularlyNavigated");
	contractInput.setAttribute("value", "true");
	form.appendChild(contractInput);
	
	var presenterNameInput = document.createElement("input");
	presenterNameInput.setAttribute("type", "hidden");
	var presenterNameText = $("#textbox_name"+index).val();
	presenterNameInput.setAttribute("name", "presenterName");
	presenterNameInput.setAttribute("value", presenterNameText);
	form.appendChild(presenterNameInput);
	
	var indexInput = document.createElement("input");
	indexInput.setAttribute("type", "hidden");
	indexInput.setAttribute("name", "index");
	indexInput.setAttribute("value", index);
	form.appendChild(indexInput);
	
	form.style.display = 'none';
	document.body.appendChild(form);
	form.submit();
	
}