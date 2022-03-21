
/**
 * This function will be called when the user clicks on download CSV button.
 * @return
 */
function doDownloadCSV()
{
	if (confirm(' I acknowledge that the accuracy of data downloaded outside of this application is the responsibility of the user and that neither John Hancock nor its affiliates shall be responsible for any inaccuracy resulting from such use.')) {
		var reportURL = new URL();
		reportURL.setParameter("task", "download");
		location.href = reportURL.encodeURL();
	}

}

/**
 * This function will be called when the user clicks on download CSV ALL button.
 * @return
 */
function doDownloadAllCSV()
{
	if (confirm(' I acknowledge that the accuracy of data downloaded outside of this application is the responsibility of the user and that neither John Hancock nor its affiliates shall be responsible for any inaccuracy resulting from such use.')) {
		var reportURL = new URL();
		reportURL.setParameter("task", "downloadAll");
		location.href = reportURL.encodeURL();
	}

}

/**
 * This function will be called when the user clicks on download CSV ALL button.
 * @return
 */
function doDetailedAccountReportCSV()
{
	if (confirm(' I acknowledge that the accuracy of data downloaded outside of this application is the responsibility of the user and that neither John Hancock nor its affiliates shall be responsible for any inaccuracy resulting from such use.')) {
		var reportURL = new URL("/do/bob/detailedAccountReport/Active/");
		reportURL.setParameter("task", "detailedAccountReport");
		location.href = reportURL.encodeURL();
	}
}

/**
 * This function will be called when the user clicks on download PDF button.
 * @param maxRowsAllowedInPDF
 * @return
 */
function doOpenPDF(maxRowsAllowedInPDF) {
	var reportURL = new URL();
	reportURL.setParameter("task", "printPDF");
	var pdfCapped = document.getElementsByName("pdfCapped")[0].value;
	if (pdfCapped == "true") {
		var confirmPdfCapped = confirm("The PDF you are about to view is capped at " + maxRowsAllowedInPDF + " rows. Click OK to Continue.");
		if (confirmPdfCapped) {
				window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
		}
	} else {
			window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
	}
}
