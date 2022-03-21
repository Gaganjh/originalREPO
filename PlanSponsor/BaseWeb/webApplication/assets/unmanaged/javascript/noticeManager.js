function doSubmitAdd(action) {
	document.getElementsByName("action")[0].value= action;
	document.noticePrefForm.submit();
	return false;
}
var doSubmitDeletefunctionInd = false;
	
	function doSubmitDelete(action,index,tempId) { 
		if(doSubmitDeletefunctionInd){
			return;
		}
		if(tempId==0)
		{
			document.getElementsByName("action")[0].value= action;
			document.getElementsByName("id")[0].value= index; 
			document.getElementsByName("tempId")[0].value=0;
		}else
	{
		var r = confirm("Are you sure you want to delete the alert? ");
		if (r == true) {
			document.getElementsByName("action")[0].value= action;
			document.getElementsByName("id")[0].value= index;
			document.getElementsByName("tempId")[0].value= tempId;
		}
	}
		document.noticePrefForm.submit();
		return false;
	} 
var doNoticeSubmitRunning = false;
//submit the form
function doNoticeSubmit(frm, action) {
	if(doNoticeSubmitRunning){
		return false;
	}
	else{
		doNoticeSubmitRunning = true;
		// Check if submit is in progress
		var submitInProgress = isSubmitInProgress();
		if (submitInProgress) {
			return false;
		} // fi
		frm.elements['action'].value=action
		frm.submit();
		return false;
	}   
} 
