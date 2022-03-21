//This js file is for PlanReviewResults page.
$(document).ready(function(){
	if($('#errorInd').val() == 'true'){
		 $(".errorMessage").show();
	 }
	
	var selectAllCheckboxEnabled = false;
	$("#reportTable1 tr").each(function(){
		 if($(this).find(".status").html().indexOf("Complete") > 0){
			 $(this).find("div[id^='enablePDFcontractStatus']").show();
			 $(this).find("div[id^='enableSumPDFcontractStatus']").show();
			 selectAllCheckboxEnabled = true;
		} 
		});
	
	if(selectAllCheckboxEnabled == false) {
		
		if($(".selectAllCheckbox") != undefined) {
			$(".selectAllCheckbox").prop( "disabled", true);
		}
	}
});

$.ajaxSetup ({
    // Disable caching of AJAX responses */
    cache: false
});

 var statusInervalId = setInterval(function(){
		var jsonObjparam ="";
		var jsonObjparam1 ="";
		var index   =0;
	/**
	 * This function is called for Request Status of each contract pdf status.
	 * if status is in-progress then make ajax call for ever 20 sec's for updating
	 * the status. 
	 */
	$('#reportTable1 tr').each(function() {
		var contractId = $(this).find(".val_num_count").html();
		var status=$(this).find(".status span").html();
		if(contractId != null || contractId != undefined){
			if(status !=null && status.trim()== "In Progress")
			
				{
					var styleId=$(this).find(".status span").prop("id");
				    var jsonItem="";
				    jsonItem+="{\"ContractId\":\""+contractId.trim()+"\",\"Status\":\""+status.trim()+"\",\"StyleId\":\""+styleId+"\"}";
				    jsonObjparam+=jsonItem+",";
				}
		}
		index= index + 1;
	});
	
	jsonObjparam1+= jsonObjparam.slice(0,-1);
	
	if(jsonObjparam1.length > 0) {	
		
		if($('.bob_results').val() == 'contract'){
			ajax_getJSON("/do/bob/contract/planReview/Results/?task=checkPlanReviewRequestStatus&isPageRegularlyNavigated=true",
				{jsonObj:jsonObjparam1
				}, statusChange_callbackMethod);
		}else{
		     ajax_getJSON("/do/bob/planReview/Results/?task=checkPlanReviewRequestStatus&isPageRegularlyNavigated=true",
				{jsonObj:jsonObjparam1
				}, statusChange_callbackMethod);
			}
	} else {
		clearInterval(statusInervalId);
	}
}, 20000);


//Callback method for Request status .
statusChange_callbackMethod = function(parsedData){
	var completedReqs =0;
	var completedReqs=parseInt($("#processedReqs").html());
	var index = 0;
	$.each(parsedData, function(i, obj) {
		$("#"  + obj.StyleId).text(obj.Status);
		$("."+obj.StyleId).val(obj.Status);
		
		$("#planReview" + obj.StyleId).val(obj.Status);
		
		if(obj.Status == "Complete")
		{
			$(".planReviewReportPdfsSelected, .exeSummaryPdfsSelected").prop( "disabled",false); 
			$("input[type='checkbox']").prop("disabled",false);
			$("#"+obj.StyleId).prop('title', '');
			$("#processImg"+obj.StyleId).prop('src', '/assets/unmanaged/images/icon_done.gif');
			$("#enablePDF"+obj.StyleId).show();
			$("#enableSumPDF"+obj.StyleId).show();
			
			if($(".selectAllCheckbox") != undefined) {
				$(".selectAllCheckbox").prop( "disabled", false);
			}
			
			completedReqs=completedReqs+1;
				var allCompleted = true;
				// Iterate all the status in html to know whether jobs are completed
				$("#reportTable1 span[id^='contractStatus']").each(function() {
					if($(this).html() == "Complete"){
						// Do not do anything
					}else {
						allCompleted = false;
					}
				});
				
		}
		else if(obj.Status == "Incomplete"){
			$("#processImg"+obj.StyleId).prop('src', '/assets/unmanaged/images/error.gif');
			//error Message will be displayed as there is a contract with incomplete status
			$(".errorMessage").show();
			$("#"+obj.StyleId).prop('title', 'This request is incomplete. Our support team has been notified and is working to resolve the issue. For support, please email jhplanreview@jhancock.com or call 1-877-346-8378.');
		}
		else if(obj.Status == "Resubmitted"){
			$("#processImg"+obj.StyleId).prop('src', '/assets/unmanaged/images/resubmitted.gif');
			$("#"+obj.StyleId).prop('title', 'The plan review request for this contract has been resubmitted. The new request can be found in your history.');
		}
	});
	$("#processedReqs").text(completedReqs);
}


$(document).ready(function(){
	
/**
 * This function is called when the user clicks on PrintRequested link in I Want... section. 
 * This function will check the request status for all contracts in Results page.
 * if all in-progress status then it will show alert.
 * if some in-progress status then it will show the confirmation pop-up.
 */
$("#doprintPreview").on('click',function(){
	var index   =0;
	var some_inprogress=false; //at least one record status is In-progress we setting the variable
	var all_inprogress=true;   //all In-progress we are setting the variable.
	var inCompleteCounter = 0;
	var incompleteOrInprogress=false; 
	var totalRequestCount = $('#reportTable1 tr').length;
	$('#reportTable1 tr').each(function() {
		var status=$(this).find(".status span").html();
		
		if(status !=null && status.trim()== "Incomplete"){
			inCompleteCounter = inCompleteCounter + 1;
		}
		if(status !=null && status.trim()== "In Progress"){
			some_inprogress=true;
		}
		if ($('.bob_results').val() != 'bob'){
		if(status !=null && ((status.trim()== "In Progress") || (status.trim()== "Incomplete"))){
			incompleteOrInprogress=true;
		}
		}
	index++;
	});	
	
	if (incompleteOrInprogress){
		incompleteOrInprogessContractsAlert();
	}

	if(inCompleteCounter == totalRequestCount)
	{
		allIncompleteContractsAlert();
	}else if(some_inprogress){                     //Request status is at least one record then will show the confirm box.
		if(someInporgressContractsConfirm()){
		if ($('.bob_results').val() == 'bob'){
			document.forms['planReviewResultForm'].action = "/do/bob/planReview/Results/?task=print";
			}else{
			document.forms['planReviewResultForm'].action = "/do/bob/contract/planReview/Results/?task=print";
			}
			navigate("planReviewResultForm");
		}
		
	}else if(!some_inprogress){
		if ($('.bob_results').val() == 'bob'){
			document.forms['planReviewResultForm'].action = "/do/bob/planReview/Results/?task=print";
			}else{
			document.forms['planReviewResultForm'].action = "/do/bob/contract/planReview/Results/?task=print";
			}
		navigate("planReviewResultForm");
		
	}
});



$("#doHistory").on('click',function(){
		if (planRviewRequestConfirmMsg()) {
			if ($('.bob_results').val() == 'bob'){
				document.forms['planReviewResultForm'].action = "/do/bob/planReview/Results/?task=history";
			}else{
				document.forms['planReviewResultForm'].action = "/do/bob/contract/planReview/Results/?task=history";
			}
			navigate("planReviewResultForm");
		}
	
});
$("#doPlanRequest").on('click',function(){
	
	if (planRviewRequestConfirmMsg()) {
			if ($('.bob_results').val() == 'bob'){
				document.forms['planReviewResultForm'].action = "/do/bob/planReview/Results/?task=planReviewRequest";
			}else{
				document.forms['planReviewResultForm'].action = "/do/bob/contract/planReview/Results/?task=planReviewRequest";
			}
			navigate("planReviewResultForm");
		}
	
});

/** This function is called When user navigates to results page from History Summary page and 
 *  click back button to navigate back to History summary page
 */
function doBackToMainHistory() {

	if ($('.bob_results').val() == 'bob') {
		document.forms['planReviewResultForm'].action = "/do/bob/planReview/Results/?task=backToMainHistory";
	} else {
		document.forms['planReviewResultForm'].action = "/do/bob/contract/planReview/Results/?task=backToMainHistory";
	}

	navigate("planReviewHistoryDetailsReportForm");
}

/**
 * This function is called when the user click on PlanReviewReport 
 * select all Check box  checked and unchecked in the header of the Results page.
 */
$(".Rselect_all").on('click',function(){
	  var checked_status = this.checked;
	  $(".planReviewReportPdfsSelected").each(function(){
		this.checked = checked_status;
		this.value = true;
		$("#planReviewReportPdfsSelected").val(this.checked);
		if($(".download:checked").length>0){ 
			$("#blue-btn_big").prop("disabled", false).removeClass("disabled-grey-btn");
			$("#download_button").removeAttr( "disabled" );
		}
	   else $("#blue-btn_big").prop('disabled', 'disabled').addClass("disabled-grey-btn");
	  });
	});
/**
 * This function is called for individual contract level checked and unchecked
 * under the PlanReviewReport header in the Results page.
 */
$(".planReviewReportPdfsSelected").on('click',function(){
		  if(this.checked != $(".Rselect_all").prop('checked')) {
				$(".Rselect_all").prop('checked', false);
				$("#allPlanReviewReportPdfsSelected").val(false);
		  }
		   var index=this.id;
		   $("#planReviewReportPdfsSelected"+index).val(this.checked);
		   $("#planReviewReportPdfsSelected").val(this.checked);
		   if($(".download:checked").length>0){ 
				$("#blue-btn_big").prop("disabled", false).removeClass("disabled-grey-btn");
			}
		   else $("#blue-btn_big").prop('disabled', 'disabled').addClass("disabled-grey-btn");
		   
	});
/**
 * This function is used for selected all check box for ExecutiveSummary
 * in Results page.
 */
$(".Eselect_all").on('click',function(){
	  var checked_status = this.checked;
	  $(".exeSummaryPdfsSelected").each(function(){
		this.checked = checked_status;
		this.value = true;
		$("#exeSummaryPdfsSelected").val(this.checked);
		if($(".download:checked").length>0){ 
			$("#blue-btn_big").prop("disabled", false).removeClass("disabled-grey-btn");
		}
	   else $("#blue-btn_big").prop('disabled', 'disabled').addClass("disabled-grey-btn");
	  });
	});
/**
 * This function is used for ExecutiveSummary Select box checked or not.
 */
$(".exeSummaryPdfsSelected").on('click',function(){
		  if(this.checked != $(".Eselect_all").prop('checked')) {
				$(".Eselect_all").prop('checked', false);
				$("#allExeSummaryPdfsSelected").val(false);
		  }
		   var index=this.id;
		   $("#exeSummaryPdfsSelected"+index).val(this.checked);
		   $("#exeSummaryPdfsSelected").val(this.checked);
		   if($(".download:checked").length>0){ 
				$("#blue-btn_big").prop("disabled", false).removeClass("disabled-grey-btn");
			}
		   else $("#blue-btn_big").prop('disabled', 'disabled').addClass("disabled-grey-btn");
			
	});

});



