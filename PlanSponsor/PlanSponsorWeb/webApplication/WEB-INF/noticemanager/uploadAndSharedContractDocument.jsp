<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.util.SessionHelper"%>
<%@ page import="com.manulife.pension.ps.web.noticemanager.UploadSharedNoticeManagerForm"%>
<%@ page import="com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 	

<%@ page import="com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO" %>
<content:contentBean
	contentId="<%=ContentConstants.ICC_YEAR_END_WARNING_MESSAGE_KEY%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="yearEndWarningMessage" />
<content:contentBean
	contentId="<%=ContentConstants.ICC_WARNING_MESSAGE_KEY%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningMessage" />
<content:contentBean
	contentId="<%=ContentConstants.PLAN_HIGHLIGHTS_NOTICE_DOCUMENT%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="planHighlights" />
<content:contentBean
	contentId="<%=ContentConstants.INVESTMENT_COMPARATIVE_CHART_LINK%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="icc" />
<content:contentBean
	contentId="<%=ContentConstants.PLAN_AND_INVESTMENT_NOTICE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="planInvestment" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_APPLY_BUUTON_MOUSEOVER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="apply" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_RESET_BUUTON_MOUSEOVER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="resetValue" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_ADD_BUUTON_MOUSEOVER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="add" />
<content:contentBean
	contentId="<%=ContentConstants.PARTICIPANT_DISCLOSURE_TEXT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="uploadShare" />
<content:contentBean contentId="<%=ContentConstants.NMC_TABLE_TEXT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="manageDocsTitle" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_CHANGE_HSTORY_LINK%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="changeHistoryLink" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_UPLOAD_DOC_MAX_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="addButtonMOMessage" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_UPLOAD_SORT_COLUMN_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="sortTheDocuments" />
<content:contentBean contentId="<%=ContentConstants.ICC_LINK_NOTE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="iccLinkNote" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_UPLOAD_DOC_FIVE_YEARS_WARNNNIG_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="uploadDocWarningMessage" />
<content:contentBean
	contentId="<%=ContentConstants.NMC_APPLY_SORT_WARNNNIG_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="applySortChangesWarningMessage" />
<content:contentBean
	contentId="<%=ContentConstants.DOCUMENT_NAME_MOUSEOVER%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="documentNameMouseOver" />
<%
	String printFriendly = (String) request.getParameter("printFriendly");
	pageContext.setAttribute("printFriendly", printFriendly, PageContext.PAGE_SCOPE);
%>

<script type="text/javascript">
var add = '<content:getAttribute id="add" attribute="text" filter="true"/>';
var apply = '<content:getAttribute id="apply" attribute="text" filter="true"/>';
var resetValue = '<content:getAttribute id="resetValue" attribute="text" filter="true" escapeJavaScript="true"/>';
var sortTheDocuments = '<content:getAttribute id="sortTheDocuments" attribute="text" filter="true"/>';
var addButtonMOMessage = '<content:getAttribute id="addButtonMOMessage" attribute="text" filter="true" escapeJavaScript="true"/>';
var documentNameMouseOver = '<content:getAttribute id="documentNameMouseOver" attribute="text" filter="true"/>';
var uploadDocWarningMessage = '<content:getAttribute id="uploadDocWarningMessage" attribute="text" filter="true"/>';
var applySortChangesWarningMessage = '<content:getAttribute id="applySortChangesWarningMessage" attribute="text" filter="true"/>';
var intervalId;
var utilities = {
       // Asynchronous request call to the server. 
      doAsyncRequest : function(actionPath, callbackFunction) {
      YAHOO.util.Connect.setForm(document.uploadsharedPlandocForm);
          // Make a request
          var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);
      },
     // Generic function to handle a failure in the server response  
      handleFailure : function(o){ 
          o.argument = null;
          utilities.hideWaitPanel();
  		clearInterval(intervalId);
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
   };
// users if they make any cahges in the sort and tryning to navigate  other pages then we need to show thwe warning message
$(document).ready(function(){
	$("a").not(".exempt").not('[href^="#"]').not('a:contains("Print report")').on("click",function(e){
		e.preventDefault();
		var sortAppliedInd = document.forms['uploadsharedPlandocForm'].sortAppliedInd.value;
		if(sortAppliedInd == "true"){
			if(confirm(applySortChangesWarningMessage)){
				var link = $(this).attr('href');
				window.location.href = link;
			}
		}	
		else{
			var link = $(this).attr('href');
			window.location.href = link;
		}
	});
	$("form[name='QuickLinksForm'] select").attr("onchange",'');
	$("form[name='QuickLinksForm'] select").on("change",function(){
		var sortAppliedInd = document.forms['uploadsharedPlandocForm'].sortAppliedInd.value;
		var value = $(this).val();
		if(sortAppliedInd == "true"){
			if(confirm(applySortChangesWarningMessage)){
				if(value != "#" && value != ""){
					window.location.href = value;
			  	}	
			}
		}
		else{
			if(value != "#" && value != ""){
				window.location.href = value;
		  	}
		}
	});

});	 
var urlToHit;
    //For CustomPlanNotice 
function customPlanNoticeGenerated(plandocument,documentId,contractId,documentFileName,source) {
	
    var customSortInd = document.forms['uploadsharedPlandocForm'].customSortInd.value;
	if(customSortInd == "true"){
    	if (!confirm(applySortChangesWarningMessage)){
		return false;
		}
	}
    utilities.showWaitPanel();
    	urlToHit = "/do/noticemanager/uploadandsharepages/?task=viewNotice&documentId="+documentId+"&contractId="+contractId+"&documentFileName="+encodeURIComponent(documentFileName)+"&plandocument="+plandocument+"&source="+source;		
  		utilities.doAsyncRequest(urlToHit, callback_customPlanNoticeGenerated);
  		}
    // Call back handler to Check whether Custom plan Report Generation is complete.
var callback_customPlanNoticeGenerated =    {
		success:  function(o) { 
			if(o.responseText == 'error'){
				window.location.href = "/do/noticemanager/uploadandsharepages/?task=populateError";
			}else if(o.responseText == 'pdfGenerated'){
	      window.location.href = "/do/noticemanager/uploadandsharepages/?task=pdfDownload";
			}else if(o.responseText == 'pdfNotGenerated'){
				var reportURL = new URL("/do/noticemanager/uploadandsharepages/?task=openErrorPdf");
				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
	        	window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
				//window.location.href = reportURL.encodeURL();
			}else{
				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
				window.open(urlToHit,"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
			}
	    utilities.hideWaitPanel();
			},
	    cache : false,
	    failure : utilities.handleFailure
	};
         
	//For JHPlanNotice 
function doCheckJhPlanNoticeGenerated(plandocument,source) {
	var customSortInd = document.forms['uploadsharedPlandocForm'].customSortInd.value;
	if(customSortInd == "true"){
	if (!confirm(applySortChangesWarningMessage)){
		return false;
		}
	}
		utilities.showWaitPanel();
  		utilities.doAsyncRequest("/do/noticemanager/uploadandsharepages/?task=viewNotice&plandocument="+plandocument+"&source="+source, callback_checkJhPlanNoticeGenerated);
  		}
    // Call back handler to Check whether PlanHighlights plan Report Generation is complete.
var callback_checkJhPlanNoticeGenerated =    {
		success:  function(o) { 
			 if(o.responseText == 'error'){
				window.location.href = "/do/noticemanager/uploadandsharepages/?task=populateError";
			}else if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/noticemanager/uploadandsharepages/?task=planHighlightPdfDownload";
			}else if(o.responseText == 'pdfNotGenerated'){
				var reportURL = new URL("/do/noticemanager/uploadandsharepages/?task=openErrorPdf");
				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
	        	window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
				//window.location.href = reportURL.encodeURL();
			}
			utilities.hideWaitPanel();
			},
	    cache : false,
	    failure : utilities.handleFailure
	};
     
function doCheckPiNoticeGenerated(plandocument,source){
	var customSortInd = document.forms['uploadsharedPlandocForm'].customSortInd.value;
	if(customSortInd == "true"){
      if (!confirm(applySortChangesWarningMessage)){
  		return false;
  		}
      }
  		utilities.doAsyncRequest("/do/noticemanager/uploadandsharepages/?task=viewNotice&plandocument="+plandocument+"&source="+source, callback_checkPiNoticeGenerated);
      }
	
	// Call back handler to Check whether ICC Report Generation is complete.
var callback_checkPiNoticeGenerated =    {
		success:  function(o) {
			if(o.responseText == 'error'){
				window.location.href = "/do/noticemanager/uploadandsharepages/?task=populateError";
			}else if(o.responseText == 'success'){
				if (${uploadsharedPlandocForm.showIccCalendarYearMessage}){
					var message = '<content:getAttribute beanName="yearEndWarningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
				      var response = confirm(message);
				      if (response == true) {
				    	utilities.showWaitPanel();
					utilities.doAsyncRequest("/do/planAndInvestmentNotice/?action=checkPdfReportGenerated", callback_checkpiNotice);
				} 
				}else{
		        	  var message = '<content:getAttribute beanName="warningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
		              var response = confirm(message);
				      if (response == true) {
				    		utilities.showWaitPanel();
					utilities.doAsyncRequest("/do/planAndInvestmentNotice/?action=checkPdfReportGenerated", callback_checkpiNotice);
				}
				}	
		}
		}
	}
			// Call back handler to Check whether ICC Report Generation is complete.
var callback_checkpiNotice =    {	
			success:  function(o) { 
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/planAndInvestmentNotice/";
			}else{
				var reportURL = new URL("/do/planAndInvestmentNotice/?action=openErrorPdf");
				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
	        	window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
				//window.location.href = reportURL.encodeURL();
			}
			utilities.hideWaitPanel();
			},
	    cache : false,
	    failure : utilities.handleFailure
	};
function doCheckIccGenerated(plandocument,source){
	var message;
	var customSortInd = document.forms['uploadsharedPlandocForm'].customSortInd.value;
	if(customSortInd == "true"){
	if (!confirm(applySortChangesWarningMessage)){
		return false;
		}
	}
  		utilities.doAsyncRequest("/do/noticemanager/uploadandsharepages/?task=viewNotice&plandocument="+plandocument+"&source="+source, callback_checkIccGenerated);
      }
var callback_checkIccGenerated = {
	success:  function(o) {
		if(o.responseText == 'error'){
			window.location.href = "/do/noticemanager/uploadandsharepages/?task=populateError";
		}else if(o.responseText == 'success'){
			if (${uploadsharedPlandocForm.showIccCalendarYearMessage}) {
				var message = '<content:getAttribute beanName="yearEndWarningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
			      var response = confirm(message);
			      if (response == true) {
			    	utilities.showWaitPanel();
					utilities.doAsyncRequest("/do/iccReport/?action=checkPdfReportGenerated", callback_CheckICCNotice);
			}
			}else{
	        	  var message = '<content:getAttribute beanName="warningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
	              var response = confirm(message);
	    	      if (response == true) {
	    	    	utilities.showWaitPanel();
	    			utilities.doAsyncRequest("/do/iccReport/?action=checkPdfReportGenerated", callback_CheckICCNotice);
	    	}
	          }	
	
	}
	}
}
	// Call back handler to Check whether ICC Report Generation is complete.
var callback_CheckICCNotice =    {
		success:  function(o) { 
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/iccReport/";
			}else{
				var reportURL = new URL("/do/iccReport/?action=openErrorPdf");
				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
	        	window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
				//window.location.href = reportURL.encodeURL();
			}
			utilities.hideWaitPanel();
			},
	    cache : false,
	    failure : utilities.handleFailure
	};
		
function getDeleteCustomPlanNotice(methodname,documentId, contractId, postToPptInd, documentFileName)
{
	
  document.forms['uploadsharedPlandocForm'].contractId.value = contractId;
  document.forms['uploadsharedPlandocForm'].documentId.value = documentId;
  document.forms['uploadsharedPlandocForm'].postToPptInd.value = postToPptInd; 
  utilities.doAsyncRequest("/do/noticemanager/uploadandsharepages/?task=validateCustomPlanDocument&documentFileName="+encodeURIComponent(documentFileName), callback_checkDeleteCustomPlanNotice);
}
var callback_checkDeleteCustomPlanNotice = {
		success:  function(o) {
			if(o.responseText == 'error'){
				window.location.href = "/do/noticemanager/uploadandsharepages/?task=populateError";
			}else if(o.responseText == 'success'){
				utilities.doAsyncRequest("/do/noticemanager/uploadandsharepages/?task=getDocumentPostedUserName", callback_checkPlanNoticeDoc);
		}
		}
	} 

var callback_checkPlanNoticeDoc = {
	success:  function(o) {
		response = confirm(o.responseText);
		document.forms['uploadsharedPlandocForm'].confirmIndicator.value = response;
		document.getElementsByName("task")[0].value= 'deleteNotice';
		document.forms['uploadsharedPlandocForm'].action = "/do/noticemanager/uploadandsharepages/";
		document.forms['uploadsharedPlandocForm'].submit();
	}
	}
	
function doApply(methodname){
		var orderofdocuments = "";
		$(".selectedcheckbox").each(function(){
			orderofdocuments += $(this).attr( "id" ) + ",";
		});	
	 orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);
	 document.forms['uploadsharedPlandocForm'].documentsOrder.value = orderofdocuments;
	 document.getElementsByName("task")[0].value= methodname;
	 document.forms['uploadsharedPlandocForm'].action = "/do/noticemanager/uploadandsharepages/";
     document.forms['uploadsharedPlandocForm'].submit();
	}

function doSubmit(methodname,documentId,contractId,documentFileName) {
	
	if(methodname !="applyNoticeOrder"){
		var customSortInd = document.forms['uploadsharedPlandocForm'].customSortInd.value;
		if(customSortInd == "true"){
			if (!confirm(applySortChangesWarningMessage)){
	    		return false;
	    		}
		  }
	}
             document.forms['uploadsharedPlandocForm'].contractId.value = contractId;
		     document.forms['uploadsharedPlandocForm'].documentId.value = documentId;
		     document.forms['uploadsharedPlandocForm'].planDocumentFileName.value = documentFileName;
		     document.getElementsByName("task")[0].value= methodname; 
		     document.forms['uploadsharedPlandocForm'].action = "/do/noticemanager/uploadandsharepages/";
	         document.forms['uploadsharedPlandocForm'].submit();
			}

function doCustomSort(methodname,sortTypeArrow,documentId){
	 document.forms['uploadsharedPlandocForm'].documentId.value = documentId;
	 document.forms['uploadsharedPlandocForm'].sortTypeArrow.value = sortTypeArrow;
	 document.getElementsByName("task")[0].value= methodname; 
	 document.forms['uploadsharedPlandocForm'].action = "/do/noticemanager/uploadandsharepages/";
	 document.forms['uploadsharedPlandocForm'].submit();
	
}
function doCHangeHistory()
{
	var customSortInd = document.forms['uploadsharedPlandocForm'].customSortInd.value;
	if(customSortInd == "true"){
	if (!confirm(applySortChangesWarningMessage)){
		return false;
		}
	}
	 document.forms['uploadsharedPlandocForm'].action = "/do/noticemanager/contractnoticechangehistory/";
	 document.forms['uploadsharedPlandocForm'].submit();
	
	}
</script>
<style>
.datacell2 {
	padding-top: 5px;
	padding-bottom: 5px;
	background-color: #efefef;
}
.on{
	padding-top: 2px !important;
}
.enabledButton{
	cursor: pointer !important;
}
.buttonDisable p{
	cursor: default;
}
.enabledButton p{
	cursor: pointer !important;
}
</style>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<ps:form method="POST" modelAttribute="uploadsharedPlandocForm" name="uploadsharedPlandocForm" action="/do/noticemanager/uploadandsharepages/">
<form:hidden path="task" />
<form:hidden path="methodname" />
<form:hidden path="planNoticedocument" />

<form:hidden path="documentId" />
<form:hidden path="planDocumentFileName" />
<form:hidden path="contractId" />
<form:hidden path="postToPptInd" />
<form:hidden path="confirmIndicator" />
<form:hidden path="documentsOrder" />
<form:hidden path="userTermsAndAcceptanceInd" />

<form:hidden path="sortTypeArrow" />
<form:hidden path="customSortInd" />
<form:hidden path="sortAppliedInd" />
		<DIV id=errordivcs align="left" style="color: #990000; font-size: 12px">
<c:if test="${uploadsharedPlandocForm.uploadAndSharePageInd ==true}">

<c:if test="${uploadsharedPlandocForm.internalUser !=true}">
				<c:if test="${uploadsharedPlandocForm.userTermsAndAcceptanceInd eq('N') ||
		      uploadsharedPlandocForm.userTermsAndAcceptanceInd eq('X')}">
				<b>
					<content:contentBean
						contentId="<%=ContentConstants.NMC_TERMS_OF_USE_ACCEPTANCE_WARNING_MESG%>"
						type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
						id="termsOfUseAcceptanceWarningMessage" />
					<content:getAttribute id="termsOfUseAcceptanceWarningMessage"
						attribute="text" filter="true" escapeJavaScript="false" />
				</b>		
				</c:if>
</c:if>
</c:if>
		<content:errors scope="session" />
	</DIV>
	<div style="height:6px">
	&nbsp;
	</div>
	<table border="0" cellspacing="0" cellpadding="0" width="715">
		<tbody>
			<tr>
				<td><jsp:include page="planNoticeTabs.jsp" flush="true">
						<jsp:param value="1" name="tabValue" />
					</jsp:include></td>
			</tr>
			<!-- start table content -->
			<table border="0" cellspacing="0" cellpadding="0" class="tableBorder"
				width="700"
				style="background-color: #002D62; padding: 1px 1px 1px 1px;">
				<tbody>
					<tr>
						<td class="tableheadTD1" height="16" colspan="12"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>

					<tr>
						<td width="30"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
						<td width="390"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
						<td width="50"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
						<td width="150"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
						<td width="70"><img src="/assets/unmanaged/images/s.gif"
							width="1" height="1"></td>
					</tr>
					<tr class="datacell1">
						<td colspan="12"><table border="0" cellspacing="0"
								cellpadding="5">
								<tbody>
									<tr>
										<td style="padding-left: 7px;">
										<content:getAttribute beanName="uploadShare" attribute="text"/>
										<c:if test="${printFriendly != null}" >
															<script>
																$(document).ready(function(){
																	$("a[href='#']").attr("onclick","");
																});
															</script>
														</c:if>
										</td>
									</tr>
								</tbody>
							</table></td>
					</tr>	
					<c:if test="${printFriendly == null}" >
					<tr class="datacell1">

						
							<td colspan="12" align="left">
							<div style="float: left; margin-top: 6px; padding-left: 7px;">
							<strong>Legend</strong>:&nbsp;<img
								src="/assets/unmanaged/images/view_icon.gif" width="12"
								height="12">&nbsp;View&nbsp;&nbsp;<img
								src="/assets/unmanaged/images/edit_icon.gif" width="12"
								height="12">&nbsp;Edit&nbsp;&nbsp;<img border="0"
								src="/assets/unmanaged/images/delete_icon.gif">&nbsp;Delete&nbsp;&nbsp;
								</div>
								<c:if test="${printFriendly == null}" >
								<a
								style="float:right; padding-right:5px;"
								href="#" onclick="return doCHangeHistory()"><content:getAttribute
										beanName="changeHistoryLink" attribute="text" /></a></c:if><c:if test="${printFriendly != null }" ><content:getAttribute
										beanName="changeHistoryLink" attribute="text" />
										</c:if></td>

					</tr>
						</c:if>
					<!-- Start of body title -->
					<tr class="tablehead">
						<td class="tablesubMainhead" colspan="12"><table border="0"
								cellspacing="0" cellpadding="0" width="100%">
								<tbody>
									<tr>
										<td class="tablesubMainhead"><b><content:getAttribute
												beanName="manageDocsTitle" attribute="text" /></b></td>
									</tr>
								</tbody>
							</table></td>
					</tr>
					<!-- End of body title -->
					<tr>
						<td colspan="12">
							<table border="0" cellspacing="0" cellpadding="0" width="100%">
								<thead>
									<tr class="tablesubhead"><c:if test="${printFriendly == null}" >
											<td valign="top" width="30" style="padding-left: 4px;"><b>Action</b></td>
										</c:if>
										<td class="dataheaddivider" valign="bottom" width="1"><img
											src="/assets/unmanaged/images/.gif" width="1" height="1"></td>
										<td valign="top" style="padding-left: 4px;" onmouseover="Tip(documentNameMouseOver)" onmouseout="UnTip()">
										<b>Document name</b></td>
										<td class="dataheaddivider" valign="bottom" width="1"><img
											src="/assets/unmanaged/images/s.gif" width="1" height="1">
										</td>
										<td valign="top" align="center"
											onmouseover="Tip(sortTheDocuments)" onmouseout="UnTip()"><b>Sort</b></td>
										<td class="dataheaddivider" valign="bottom" width="1"><img
											src="/assets/unmanaged/images/s.gif" width="1" height="1">
										</td>
										<td valign="top" align="center" style="width: 30px !important;"><b>Participant
												website</b></td>
										<td class="dataheaddivider" valign="bottom" width="1"><img
											src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
										<td valign="top" style="padding-left: 4px;"><b>Last
												actioned by</b></td>
										<td class="dataheaddivider" valign="bottom" width="1"><img
											src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
										<td valign="top" align="center"><b>Date actioned</b></td>
									</tr>
								</thead>
								<tbody>
									<%-- <c:if test="${Constants.REPORT_BEAN != null}"> --%>
<%
PlanDocumentReportData theReport = (PlanDocumentReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

 <c:if test="${theReport != null}"> 
<c:if test="${not empty theReport.customPlanNoticeDocuments}">

<c:forEach items="${theReport.customPlanNoticeDocuments}" var="theItem" varStatus="theIndex" >



												<c:set var="singleQuot" value="\'"/>
												<c:set var="singleQuotEscaped" value="\\\'"/>
												<c:set var="documentNameEscaped" value="${fn:replace(theItem.documentFileName, singleQuot, singleQuotEscaped)}"/>
												<tr class="datacellReplace">
												<c:if test="${printFriendly == null}" >
														<td width="40" align="left"><img width="3"
															height="12" src="/assets/unmanaged/images/s.gif" />
															 <a href="#" 
onclick="return customPlanNoticeGenerated('customPlanDocument','${theItem.documentId}','${theItem.contractId}','<c:out value="${documentNameEscaped}"/>','view')">
																	<img title="View" border="0" alt="View" id="actionId"
																		src="/assets/unmanaged/images/view_icon.gif">
																</a> <br/> 
<c:if test="${uploadsharedPlandocForm.noticeManagerAccessPermissions ==true}">

																	<img width="3" height="12"
																	src="/assets/unmanaged/images/s.gif" />
																	<a href="#" 
onclick="return doSubmit('editNotice','${theItem.documentId}','${theItem.contractId}','<c:out value="${documentNameEscaped}"/>')"><img
																	title="Edit" border="0" alt="Edit" id="actionId"
																	src="/assets/unmanaged/images/edit_icon.gif"></a>
																	<a href="#"
onclick="getDeleteCustomPlanNotice('validateCustomPlanDocument','${theItem.documentId}','${theItem.contractId}','${theItem.postToPptInd}','<c:out value="${documentNameEscaped}"/>')"><img
																	title="Delete" border="0" alt="Delete" id="actionId"
																	src="/assets/unmanaged/images/delete_icon.gif"></a>
</c:if> <c:if test="${uploadsharedPlandocForm.noticeManagerAccessPermissions !=true}">

																<img width="3" height="12"
																	src="/assets/unmanaged/images/s.gif" />
</c:if></td></c:if>
													<td class="datadivider" width="1"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td style="padding-left: 5px; width: 214px;" align="left" style="padding-left: 5px;"
														class="selectedcheckbox"
id="${theItem.documentId}">
														<c:if test="${printFriendly == null}" >
															<a class="exempt" href="#"
onclick="return customPlanNoticeGenerated('customPlanDocument','${theItem.documentId}','${theItem.contractId}','<c:out value="${documentNameEscaped}"/>','link')">
</c:if> ${theItem.docNameAndUpdatedDate}
															</a> 
															 <c:if test="${printFriendly == null}" >
<c:if test="${theItem.uploadDocFiveYears ==true}">

															<span onmouseover="Tip(uploadDocWarningMessage)"
																onmouseout="UnTip()"><img
																src="/assets/unmanaged/images/warning2.gif" border="0" /></span>
</c:if>
														</c:if>
														
													</td>
													<td class="datadivider" width="1"></td>
													<td style="width: 42px;">
<c:if test="${uploadsharedPlandocForm.noticeManagerAccessPermissions ==true}">

														<c:if test="${printFriendly == null}" >
															<table cellspacing="0" cellpadding="0">
																<tbody>
																	<tr class="sort">
																		<c:if test="${requestScope.planVoListLenght != 0 }">
																			<td width="25px" align="center" class="up">
																				<c:if test="${theIndex.index != fn:length(theReport.customPlanNoticeDocuments) - 1}"> 
																					<a href="#" 
onclick="doCustomSort('customSort','down','${theItem.documentId}'); return false;">
																					<img border="0" src="/assets/unmanaged/images/arrow_triangle_down.gif"></a>
</c:if>
																			</td>
																			<td width="25px" align="center">
																				<c:if test="${theIndex.index !=0}">
																					<a href="#" 
onclick="doCustomSort('customSort','up','${theItem.documentId}'); return false;">
																					<img border="0" src="/assets/unmanaged/images/arrow_triangle_up.gif"></a>
</c:if>
																			</td>
</c:if>
																	</tr>
																</tbody>
															</table>
														</c:if>
</c:if>
													</td>
<c:if test="${uploadsharedPlandocForm.noticeManagerAccessPermissions !=true}">

</c:if>
													<td class="datadivider" width="1"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td style="width: 55px;" width="144" align="center"><c:if test="${theItem.postToPptInd =='Y'}">

																	Yes
</c:if> <c:if test="${theItem.postToPptInd =='N'}">

																	No
</c:if></td>
														<c:if test="${theItem.planNoticeDocumentChangeDetail != null}"> 	
<c:set var="planDocumentLog" value="${theItem.planNoticeDocumentChangeDetail}" />


														</c:if>
													<td class="datadivider" width="1"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td width="87" align="left" style="padding-left: 5px;">
													<c:if test="${ theItem.planNoticeDocumentChangeDetail != null}">	
${theItem.planNoticeDocumentChangeDetail.changedUserName}

															</c:if>
													</td>
													<td class="datadivider" width="1"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td width="73" align="center">
													<c:if test="${ theItem.planNoticeDocumentChangeDetail != null}">
													<fmt:formatDate pattern="MM/dd/yyyy" value="${theItem.planNoticeDocumentChangeDetail.changedDate}" />	

													</c:if></td>
												</tr>
</c:forEach>
</c:if>
									</c:if>
<c:if test="${not empty theReport.jhPlanNoticeDocuments}">

<c:forEach items="${theReport.jhPlanNoticeDocuments}" var="theItem" varStatus="theIndex" >


											<tr class="datacell2" style="vertical-align: top;">
											<c:if test="${printFriendly == null}" >
													<td width="34" align="left"><img width="3" height="12"
														src="/assets/unmanaged/images/s.gif" />
<c:if test="${theItem.documentName =='planHighlights'}">
															<a href="#" id="actionId"
																onclick="return doCheckJhPlanNoticeGenerated('PlanHighlightDocument','view')">
																<img title="View" border="0" alt="View"
																src="/assets/unmanaged/images/view_icon.gif">
															</a>
</c:if>
<c:if test="${theItem.documentName =='icc'}">

															<a href="#" id="actionId"
																onclick="return doCheckIccGenerated('iccNotice','view')"> <img
																title="View" border="0" alt="View"
																src="/assets/unmanaged/images/view_icon.gif"></a>
</c:if>
														
<c:if test="${theItem.documentName =='planInvestment'}">

															<a href="#" id="actionId"
																onclick="return doCheckPiNoticeGenerated('pinotice','view')"> <img
																title="View" border="0" alt="View"
																src="/assets/unmanaged/images/view_icon.gif"></a>
</c:if></td></c:if>
												<td class="datadivider" width="1"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1">
												</td>
												<td style="padding-left: 5px; width: 214px;"  style="padding-left: 5px;">
												<c:if test="${printFriendly == null}">
</c:if> <c:if test="${theItem.documentName =='planHighlights'}">

															<c:if test="${printFriendly == null}" > 
															<a href="#" onclick= "return doCheckJhPlanNoticeGenerated('PlanHighlightDocument','link')"> </c:if>
																Plan Highlights 
															<c:if test="${printFriendly == null}" ></a></c:if>
</c:if> <c:if test="${theItem.documentName =='icc'}">

															<c:if test="${printFriendly == null}" >
															<a href="#" onclick= "return doCheckIccGenerated('IccNotice','link')"
															></c:if>
																Investment Comparative Chart 
														  <c:if test="${printFriendly == null}" > </a></c:if>
</c:if> <c:if test="${theItem.documentName =='planInvestment'}">

															<c:if test="${printFriendly == null}" ><a
																href="#" onclick= "return doCheckPiNoticeGenerated('pinotice','link')"
															></c:if>
																404a-5 Plan & Investment Notice 
															<c:if test="${printFriendly == null}" ></a></c:if>
</c:if>
														<c:if test="${printFriendly == null}" >
														</c:if><br /> <br /></td>
												<td class="datadivider" width="1"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												<td  style="width: 42px;" align="center"></td>
												<td class="datadivider" width="1"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td style="width: 55px;" align="center">${theItem.postToPptInd}</td>

												<td class="datadivider" width="1"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td width="87" align="left" style="padding-left: 5px;">${theItem.lastActionedUserName}</td>

												<td class="datadivider" width="1"><img
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												<td width="73" align="center"></td>
											</tr>
</c:forEach>
</c:if>
<c:if test="${uploadsharedPlandocForm.showPlanAndInvestmentReplaceMessage ==true}">


									<tr class="datacell2" style="vertical-align: top;">
													<c:if test="${printFriendly == null}" ><td width="34" align="left"><img width="3" height="12"
														src="/assets/unmanaged/images/s.gif" /></td></c:if>
													<td class="datadivider" width="1"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1">
													</td>
													<td width="120" style="padding-left: 5px;">
															<content:getAttribute beanName="iccLinkNote"
																attribute="text" />
														</td>
													<td class="datadivider" width="1"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td width="40" align="center"></td>
													<td class="datadivider" width="1"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td width="144" align="center"></td>
													<td class="datadivider" width="1"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td width="87" align="left" style="padding-left: 5px;"></td>
													<td class="datadivider" width="1"><img
														src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td width="73" align="center"></td>
												</tr>
</c:if>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
			<br />
			<br />
			<style>
				.datacell3{
					background: #e9e2c3;
				}
				.buttonDisable {
					background-image: url("/assets/unmanaged/images/134_button.gif");
					width: 134px;
					height: 25px;
					margin-top: 15px;
					postion: relative;
				}
				.buttonText {
					position: absolute;
					text-align: center;
					width: 134px;
					color: white;
					font-size: 13px;
					margin-top: 5px;
					left: 0px;
				}
				.twodisabled {
					display: none;
				}
			</style>
			<!--[if IE]>
				<style>
					.twodisabled{
						color: #B1AFA2;
						width: 132px;
						margin-top: 6px;
						display: block;
					}
				</style>
			<![endif]-->
			<table border="0" cellspacing="0" cellpadding="1" width="708">
				<tbody>
					<tr>
						<c:if test="${printFriendly == null}" >
							<td width="360"><div align="right">

<c:if test="${uploadsharedPlandocForm.showActionButon ==true}">

<c:if test="${uploadsharedPlandocForm.customSortInd !=true}">
<c:if test="${uploadsharedPlandocForm.documentDisplyOrderChanges ==true}">

												<div  style="postion:relative;"  
													onmouseover="Tip(apply)" onmouseout="UnTip('')">
													<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >apply</p><p class="buttonText twodisabled">apply</p></h5>
												</div>
</c:if>
<c:if test="${uploadsharedPlandocForm.documentDisplyOrderChanges !=true}">

												<div style="postion:relative;"  
													onmouseover="Tip(apply)" onmouseout="UnTip()">
													<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >apply</p><p class="buttonText twodisabled">apply</p></h5>
												</div>
</c:if>
</c:if>
</c:if>
<c:if test="${uploadsharedPlandocForm.showActionButon ==true}">

<c:if test="${uploadsharedPlandocForm.customSortInd ==true}">
											<div style="postion:relative;" class="enabledButton"
												onmouseover="Tip(apply)" onmouseout="UnTip()" onclick="return doApply('applyNoticeOrder')">
												<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >apply</p></h5>
											</div>
</c:if>
</c:if>
								</div></td>

							<td width="170"><div align="center">

<c:if test="${uploadsharedPlandocForm.disableTheResetButton ==true}">

</c:if>
<c:if test="${uploadsharedPlandocForm.showActionButon ==true}">

<c:if test="${uploadsharedPlandocForm.customSortInd !=true}">
<c:if test="${uploadsharedPlandocForm.disableTheResetButton ==true}">

												<div style="postion:relative;"  
													onmouseover="Tip(resetValue)" onmouseout="UnTip()">
													<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >reset</p><p class="buttonText twodisabled">reset</p></h5>
												</div>
</c:if>
<c:if test="${uploadsharedPlandocForm.disableTheResetButton !=true}">

												<div style="postion:relative;"  
													onmouseover="Tip(resetValue)" onmouseout="UnTip()">
													<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >reset</p><p class="buttonText twodisabled">reset</p></h5>
												</div>
</c:if>
</c:if>
</c:if>
<c:if test="${uploadsharedPlandocForm.showActionButon ==true}">

<c:if test="${uploadsharedPlandocForm.customSortInd ==true}">
											<div style="postion:relative;" class="enabledButton"
												onmouseover="Tip(resetValue)" onmouseout="UnTip()"
												onclick="return doSubmit('resetPlanDocumentPreviousChanges')">
												<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >reset</p></h5>
											</div>
</c:if>
</c:if>
								</div></td>
							<td width="130"><div align="center">
<c:if test="${uploadsharedPlandocForm.addDocumentPermission ==false}">

										<div style="postion:relative;"  
											onmouseover="Tip(add)" onmouseout="UnTip()"">
											<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >add</p><p class="buttonText twodisabled">add</p></h5>
										</div>										
</c:if>
<c:if test="${uploadsharedPlandocForm.addDocumentPermission !=false}">

<c:if test="${uploadsharedPlandocForm.uploadDocumentPostedCount ==true}">

											<div style="postion:relative;" 
												onmouseover="Tip(addButtonMOMessage)" onmouseout="UnTip()"">
												<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >add</p><p class="buttonText twodisabled">add</p></h5>
											</div>
</c:if>
<c:if test="${uploadsharedPlandocForm.uploadDocumentPostedCount ==false}">

											<div style="postion:relative;"  class="enabledButton"
												onmouseover="Tip(add)" onmouseout="UnTip()" onclick="return doSubmit('addNotice')" >
												<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >add</p></h5>
											</div>
</c:if>
</c:if>
								</div></td>
						</c:if>
					</tr>
				</tbody>
			</table>
		</tbody>
	</table>
</ps:form>
<c:if test="${printFriendly != null}" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
         type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
         id="globalDisclosure"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
<script src="/assets/unmanaged/javascript/tooltip.js"></script>
<script>
$(document).ready(function(){
	$(".datacellReplace:even").addClass("datacell1");
	$(".datacellReplace:odd").addClass("datacell3");
});	
</script>
