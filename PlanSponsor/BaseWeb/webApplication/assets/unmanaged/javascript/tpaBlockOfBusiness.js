/**
 * This method is called when the user clicks on "Download Report" method.
 */
function doDownloadCSV() {
	var reportURL = new URL(document.location.href);
	reportURL.setParameter("task", "csvDownloadPage");
	window.open(reportURL.encodeURL(),"","width=400,height=480,resizable,toolbar,scrollbars,menubar");
}

/**
 * This method is used to submit the filters. 
 */
function applyFilters(tpaBobForm) {
	tpaBobForm.task.value = "filter";
	tpaBobForm.submit();
}

/**
 * This method is used to reset the filters.
 */
function resetFilters(tpaBobForm) {
	tpaBobForm.task.value = "default";
	tpaBobForm.submit();
}

/**
 * This method will be called when the user clicks on contract number in TPA BOB page. 
 * This method will take the user to Contract Home page.
 * 
 * @param tpaBobForm
 * @param contractNumber
 * @return
 */
function goToContractHomePage(contractNumber) {
	tpaBobForm = document.tpaBlockOfBusinessForm;
	tpaBobForm.contractNumberSelected.value = contractNumber;
	tpaBobForm.submit();
}

/**
 * This method will be used to generate the CSV report.
 */
function generateCSV(tpaBobForm) {
	tpaBobForm.submit();
}

/**
 * This method will cancel the CSV generation, basically, closing the window.
 */
function cancelCSV() {
	window.close();
}
