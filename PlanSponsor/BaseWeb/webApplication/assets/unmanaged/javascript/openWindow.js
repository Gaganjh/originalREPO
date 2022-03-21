function FundWindow ( fundURL ) {
	options="location=1,toolbar=1,status=0,menubar=0,scrollbars=1,resizable=1,width=785,height=400,left=0,top=0";
	newwindow=window.open(fundURL, "FundInfo", options);
	newwindow.focus();
}

function guideWindow ( guideURL ) {
	options="toolbar=0,status=0,menubar=0,scrollbars=1,resizable=1,width=650,height=350,left=0,top=0";
	newwindow=window.open(guideURL+"&nav=false", "Guide", options);
	newwindow.focus();
}

function RothWindow ( rothURL ) {
	options="toolbar=0,status=0,menubar=0,scrollbars=1,resizable=1,height=600,width=800,left=0,top=0";
	newwindow=window.open(rothURL , "rothInfo", options);
	newwindow.focus();
}

function openWin(url) {
	options="toolbar=1,status=1,menubar=1,scrollbars=1,resizable=1,width=800,height=450,left=10,top=10";
	newwindow=window.open(url, "general", options);
	newwindow.focus();
}

function openNBDW_FSWWindow() {
	var strFailedClasses = document.getElementById("contractSummary").value;
	options="toolbar=0,location=0,scrollbars=1,resizable=0,width=520,height=340,left=250,top=250";
	window.open("/do/bob/contract/warrantyNotMet/?assetClasses=" + strFailedClasses, "changeWin", options);
}




function ActivityPlannerWindow ( activityPlannerURL ) {
	options="toolbar=0,status=0,menubar=0,scrollbars=0,resizable=0,height=455,width=765,left=0,top=0";
	newwindow=window.open(activityPlannerURL , "activityPlanner", options);
	newwindow.focus();
}


var PDFWin=null;
function PDFWindow ( PDFURL ) {

	var path=PDFURL;
	
	if (!PDFWin || PDFWin.closed ) {
		PDFWindowName = "";
		PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
		PDFWin = window.open(path,PDFWindowName,PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);

	} else{
 	 	PDFWin.location=path;
 	}
	
	PDFWin.focus();
}


/**
 * filename PDF file name
 */
function openPDF(fileName) {
	var isNN = (navigator.appName.indexOf("Netscape")!=-1);
	var isMac = (navigator.userAgent.indexOf("Mac") != -1);
	 
	if (isMac) {
		path = location.protocol + "//" + location.host +  fileName;
	} else {
		// if (isNN) {
		//	path = "/do/resources/FDFAction.fdf?pdfForm=" + location.protocol + "//" + location.host +  fileName;
		// } else {
			path = location.protocol + "//" + location.host + fileName + "#FDF=" + location.protocol + "//" + location.host + "/do/resources/FDFAction.fdf";
		// }
	}
	PDFWindow(path);		
}


/**
 * filename PDF file name
 */
function openFormPDF(fileName) {
	var isNN = (navigator.appName.indexOf("Netscape")!=-1);
	var isMac = (navigator.userAgent.indexOf("Mac") != -1);
	 
	if (isMac) {
		path = location.protocol + "//" + location.host +  fileName;
	} else {
		// if (isNN) {
		//	path = "/do/resources/FDFAction.fdf?pdfForm=" + location.protocol + "//" + location.host +  fileName;
		// } else {
			path = location.protocol + "//" + location.host + fileName + "?a="+ Date.now()+"#FDF=" + location.protocol + "//" + location.host + "/do/resources/FDFAction.fdf";
		// }
	}
	FormPDFWindow(path);		
}


var PDFForm=null;
function FormPDFWindow ( PDFURL ) {

	var path=rightTrim(PDFURL)+"?a="+ Date.now();
	
	if (!PDFForm || PDFForm.closed ) {
		PDFWindowName = "";
		PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
		PDFForm = window.open(path,PDFWindowName,PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);

	} else{
 	 	PDFForm.location=path;
 	}
	
	PDFForm.focus();
}

function rightTrim(PDFURL) {
	if (PDFURL != undefined) {
		while (PDFURL.lastIndexOf(" ") === PDFURL.length-1) {
			PDFURL = PDFURL.substring(0, PDFURL.length-1);
			if (PDFURL.length <= 1) {
				return PDFURL;
			}
		}
	}	
	return PDFURL;		
}
