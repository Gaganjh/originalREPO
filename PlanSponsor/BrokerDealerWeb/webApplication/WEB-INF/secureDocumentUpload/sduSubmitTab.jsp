<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>

<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile"%>

<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib uri="/WEB-INF/render.tld" prefix="render"%>

<%@ page import="com.manulife.pension.bd.web.bob.secureDocumentUpload.SDUSubmitTabController"%>
<%@ page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmitTabForm"%>
<%@ page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUConstants"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.google.gson.Gson"%>

<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

<%
	SDUSubmitTabForm sduSubmitTabForm=(SDUSubmitTabForm)request.getAttribute("sduSubmitTabForm");
	pageContext.setAttribute("sduSubmitTabForm",sduSubmitTabForm,PageContext.PAGE_SCOPE);
%>

<%
	Map submissionMetaData = (Map) session.getAttribute("sduSubmissionMetaData");
	pageContext.setAttribute("submissionMetaData",submissionMetaData,PageContext.PAGE_SCOPE);
%>

<%
BDUserProfile userProfile = (BDUserProfile)session.getAttribute(SDUConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=BDContentConstants.SDU_SUBMIT_TAB_INTRO%>"
                     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                     id="sduSubmitTabIntro"/>
 <content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>
                     
                     
<c:set var="contract" value="${bobContext.contractProfile.contract.contractNumber}"
	scope="request" />
	
	<script type="text/javascript"> 
	var sduOAuthToken = "Bearer ";
	var sduClientId = "<%=SDUConstants.SDU_BD_CLIENT_ID%>";
	var sduClientUserId = "${userProfile.BDPrincipal.profileId}";
	var sduFileUploadJsonString="";
	var uploadSuccessRedirectURL="";
	
	<% if (sduSubmitTabForm.isDisplayFileUploadSection()) { %>
	var isFileSectionShown=true;
	<% } else { %>
		var isFileSectionShown=false;
	<% } %>
	
	function clearAll() {
		if (confirm("Are you sure you want to clear all files?")) {
			document.getElementById("clearAll").click();
		}
	}
	
	function submitDocs() {
		submissionUtilities.doAsyncRequest_Post("/do/bob/secureDocumentUpload/submit/getSubmissionMetaData/",callback_getSubmissionMetaData,document.forms['sduSubmitTabForm']);
	}

	function protectLinks() {
		var hrefs  = document.links;
		if (hrefs != null)
		{
			for (i=0; i<hrefs.length; i++) {
				if(
					hrefs[i].onclick != undefined && 
					(hrefs[i].onclick.toString().indexOf("openWin") != -1 || hrefs[i].onclick.toString().indexOf("popup") != -1 || hrefs[i].onclick.toString().indexOf("logout") != -1)
				) {
					// don't replace window open or popups as they won't loose there changes with those
				}
				else if(
					hrefs[i].href != undefined && 
					(hrefs[i].href.indexOf("openWin") != -1 || hrefs[i].href.indexOf("popup") != -1 || hrefs[i].href.indexOf("logout") != -1)
				) {
					// don't replace window open or popups as they won't loose there changes with those
				}
				else if(hrefs[i].onclick != undefined) {
					hrefs[i].onclick = new Function ("var result = discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
				}
				else {
					hrefs[i].onclick = new Function ("return discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');");
				}
				//alert (hrefs[i].onclick);
			}
		}
	 }	
	
	//if (window.addEventListener) {
	//	window.addEventListener('load', protectLinks, false);
	//} else if (window.attachEvent) {
	//	window.attachEvent('onload', protectLinks);
	//}
	
	function getResponseContentType(o)
	{
		var responseContentType=null;
		responseContentType=o.getResponseHeader["Content-Type"];
		if(o.getResponseHeader["Content-Type"]!=null){
			responseContentType=o.getResponseHeader["Content-Type"];
		}else{
			responseContentType=o.getResponseHeader["content-type"];
		}
		if (responseContentType != null){
			var responseContent = responseContentType.split(";")
			if(responseContent[0] != null){
				responseContentType = responseContent[0].trim();	
			}else{
				responseContentType = null;
			}
		}
		return responseContentType;
	}

	var submissionUtilities = {
				// Asynchronous request call to the server. 
					doAsyncRequest : function(actionPath, callbackFunction, data) {
					YAHOO.util.Connect.setForm(document.widjetForm);
					YAHOO.util.Connect.initHeader( "Authorization",  sduOAuthToken, true);
					var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);
					},
					doAsyncRequest_Post : function(actionPath, callbackFunction, form) {
					YAHOO.util.Connect.setForm(form);
					var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);
					},
				// Generic function to handle a failure in the server response  
					handleFailure : function(o){
						o.argument = null;
						window.location.href= "/do/bob/secureDocumentUpload/submit/error/";						
					},
				// Function to handle session Expired scenario
					sessionExpired : function(o){
						o.argument = null;
						top.location.reload(true);
					},					
				}; // end of var submissionUtilities 
	
	var callback_LoadWidjet = {
		success:  function(o) { 	
			if(o.status == 200){
				sduOAuthToken = "Bearer "+o.responseText;
				loadWidjet();
			}
			else{
				submissionUtilities.handleFailure(o);
			}
		},
	cache : false,
	failure : submissionUtilities.handleFailure
	};
	
	var callback_getSubmissionMetaData = {
		success:  function(o) { 
			if(o.status == 200 && getResponseContentType(o) == 'application/json'){
				sduFileUploadJsonString = o.responseText;
				submissionUtilities.doAsyncRequest_Post("/do/bob/secureDocumentUpload/token/",callback_UploadDocuments,document.forms['sduSubmitTabForm']);
			}else if (o.status == 200) {
			submissionUtilities.sessionExpired(o);			
			} else{
			submissionUtilities.handleFailure(o);
			}
		},
	cache : false,
	failure : submissionUtilities.handleFailure
	};
	
	var callback_UploadDocuments = {
		success:  function(o) { 	
			if(o.status == 200){
				sduOAuthToken = "Bearer "+o.responseText;
				//Override authorization header to use the recent token				
				Dropzone.options.dropzoneLayer.headers.Authorization = sduOAuthToken;
				document.getElementById("upload-button").click();
			}
			else{
				submissionUtilities.handleFailure(o);
			}
		},
	cache : false,
	failure : submissionUtilities.handleFailure
	};

	$(document).ready(function(){
		<% if (sduSubmitTabForm.isDisplayFileUploadSection() )  { %>
		showInProgressDivision();
		submissionUtilities.doAsyncRequest_Post("/do/bob/secureDocumentUpload/token/",callback_LoadWidjet,document.forms['sduSubmitTabForm']);
		<% } else { %>
		var uploadConfirmationJson = document.forms['sduSubmitTabForm'].elements['submissionConfirmationJson'].value;
		if(uploadConfirmationJson != ""){	
			uploadConfirmationJsonObj = JSON.parse(uploadConfirmationJson);	
			displaySubmissionConfirmation(uploadConfirmationJsonObj);
		}
		<% }%>
	});
	
	function showInProgressDivision() {
		 // Only instantiate if one doesn't exist.  
		// Otherwise drop to the show() method.
		loadingPanel = new YAHOO.widget.Panel("Image_0",  
						{   width: "200px", 
							fixedcenter: true, 
							close: false, 
							draggable: false, 
							zindex:4,
							modal: true,
							visible: false,
							constraintoviewport: true
						} 
					);
	    loadingPanel.setBody("<div style='display: flex; font-size:12px'> One moment please... <br/> Do not refresh your browser &nbsp;&nbsp;&nbsp;<span><img src='/assets/unmanaged/images/ajax-wait-indicator.gif'/></span></div>");
		loadingPanel.render(document.body);
	    loadingPanel.show();  
		}
		function hideInProgressDivision() {
			loadingPanel.hide();
		}
	function loadWidjet(){		
		submissionUtilities.doAsyncRequest("${sduSubmitTabForm.widgetEndpointURL}<%=SDUConstants.SDU_WIDGET_DROPZONE_ENDPOINT%>", callback_checkDropzoneGenerated);	
	}
	
	var callback_checkDropzoneGenerated =    {
		success:  function(o) { 
			if(o.responseText != ''){
				var widjetLayer = document.getElementById("widjetLayer");
				widjetLayer.innerHTML=o.responseText;
				
				//adding javascript layer
				var g = document.createElement('script');
				var s = document.getElementsByTagName('script')[0];
				g.text = document.getElementById('dropzone').text;
				var dropZoneScriptToDelete = document.getElementById('dropzone');
				s.parentNode.insertBefore(g, s); 
				dropZoneScriptToDelete.parentNode.removeChild(dropZoneScriptToDelete);
				
				//adding the dropzoneLayer
				var dropzoneLayer = document.getElementById("dropzoneLayer");
				var dropzoneLayerPlaceholder = document.getElementById("dropzoneLayerPlaceholder");
				dropzoneLayerPlaceholder.parentNode.insertBefore(dropzoneLayer, dropzoneLayerPlaceholder); 
				dropzoneLayerPlaceholder.parentNode.removeChild(dropzoneLayerPlaceholder);
				
				//adding the dropzoneLayer
				var dropzoneMessageLayer = document.getElementById("dropzoneMessageLayer");
				var dropzoneMessageLayerPlaceholder = document.getElementById("dropzoneMessageLayerPlaceholder");
				dropzoneMessageLayerPlaceholder.parentNode.insertBefore(dropzoneMessageLayer, dropzoneMessageLayerPlaceholder); 
				dropzoneMessageLayerPlaceholder.parentNode.removeChild(dropzoneMessageLayerPlaceholder);

				//adding the dropzoneClearButtonLayerPlaceholder
				var dropzoneClearButtonLayer = document.getElementById("dropzoneClearButtonLayer");
				var dropzoneClearButtonLayerPlaceholder = document.getElementById("dropzoneClearButtonLayerPlaceholder");
				dropzoneClearButtonLayerPlaceholder.parentNode.insertBefore(dropzoneClearButtonLayer, dropzoneClearButtonLayerPlaceholder); 
				dropzoneClearButtonLayerPlaceholder.parentNode.removeChild(dropzoneClearButtonLayerPlaceholder);
				
				//adding the dropzoneUploadButtonLayerPlaceholder
				var dropzoneUploadButtonLayer = document.getElementById("dropzoneUploadButtonLayer");
				var dropzoneUploadButtonLayerPlaceholder = document.getElementById("dropzoneUploadButtonLayerPlaceholder");
				dropzoneUploadButtonLayerPlaceholder.parentNode.insertBefore(dropzoneUploadButtonLayer, dropzoneUploadButtonLayerPlaceholder); 
				dropzoneUploadButtonLayerPlaceholder.parentNode.removeChild(dropzoneUploadButtonLayerPlaceholder);
				
				new Dropzone("#dropzoneLayer" , Dropzone.options.dropzoneLayer );
				hideInProgressDivision() ;
			}
		},
	cache : false,
	failure : submissionUtilities.handleFailure
		//hideInProgressDivision() ;
	};// end of callback_checkDropzoneGenerated  

	function doPrint(){		
		if(document.getElementById("documentUploadConfirmationSection").style.display=="none"){
			document.forms['sduSubmitTabForm'].elements['submissionConfirmationJson'].value = null;			
		}
		submissionUtilities.doAsyncRequest_Post("/do/bob/secureDocumentUpload/submit/?task=print",callback_doPrint,document.sduSubmitTabForm);
	}
	
	function displaySubmissionConfirmation(uploadConfirmationJsonObj){
			document.getElementById("documentUploadConfirmationSection").style.display = "block";
			document.getElementById("submissionNumber").innerText=uploadConfirmationJsonObj.submissionNumber;
			document.getElementById("createdTime").innerText=uploadConfirmationJsonObj.submissionTime;
			document.getElementById("submittedBy").innerText=uploadConfirmationJsonObj.submitterName;
			var fileNamesListHtml ="";
			for(var i=0; i<uploadConfirmationJsonObj.fileNames.length; i++) {
			 fileNamesListHtml += uploadConfirmationJsonObj.fileNames[i] + "\n";
			}
			document.getElementById("submittedFileNames").innerText=fileNamesListHtml;
			document.getElementById("companyName").innerText=uploadConfirmationJsonObj.companyName;
			document.getElementById("contractNumber").innerText=uploadConfirmationJsonObj.contractNumber;
	}
	
	function displayFailureErrorMessages(uploadFailureJsonObj){		
			var fileNamesListHtml ="";			
			document.getElementById("magicnumberValidationMessage").innerText="";
			document.getElementById("antiVirusScanMessage").innerText="";
			document.getElementById("uploadErrorMessage").innerHTML="";		
			document.getElementById("virusScanServerFailureMessage").innerHTML="";	
			document.getElementById("errorMessage").innerText="";
			
			if(uploadFailureJsonObj.fileNames!=null){
			for(var i=0; i<uploadFailureJsonObj.fileNames.length; i++) {
			 fileNamesListHtml += "<ul><li style='line-height: 0.5em'>"+uploadFailureJsonObj.fileNames[i] +"</li></ul>";			 
			}				
				document.getElementById("submittedErrorFileNames").innerHTML=fileNamesListHtml;
			
			}			
			var errorMessages = uploadFailureJsonObj.error;			
			if(errorMessages!=null){
			if(errorMessages.errorCode==104){
				document.getElementById("magicnumberValidationMessage").innerText="";	
				document.getElementById("errorMessage").innerText=uploadFailureJsonObj.statusMessage;	
			}else if(errorMessages.errorCode==109){
				document.getElementById("antiVirusScanMessage").innerText="The following document(s) contain a virus or security risk and cannot be submitted.";					
			}else if(errorMessages.errorCode==111){ // If FileScanService or CLAM AV is down
				document.getElementById("virusScanServerFailureMessage").innerHTML="<ul><li>Unable to upload file, please select file and try again.</li></ul>";					
			}else{
				document.getElementById("uploadErrorMessage").innerHTML="Unable to upload file, please select file and try again.";				
			}
			
			}
		
	}
	//Control will come to this method after the successful upload
	function onUploadSuccess(uploadConfirmationJsonObj){
		document.forms['sduSubmitTabForm'].elements['submissionConfirmationJson'].value = JSON.stringify(uploadConfirmationJsonObj);
		displaySubmissionConfirmation(uploadConfirmationJsonObj);	
	}
	//Control will come to this method after the failure upload
	function onUploadFailure(uploadFailureJsonObj){			
		displayFailureErrorMessages(uploadFailureJsonObj);			
	}
	
	var callback_doPrint =    {
		success:  function(o) {			
			if(o.status == 200 && getResponseContentType(o) == 'text/plain'){
				var reportURL = new URL();
				reportURL.setParameter("task", "printPDF");
				window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");	
			}else if (o.status == 200) {
				submissionUtilities.sessionExpired(o);			
			} else{
				submissionUtilities.handleFailure(o);
			}
		},
	cache : false,
	failure : submissionUtilities.handleFailure
		//hideInProgressDivision() ;
	};// end of callback_checkDropzoneGenerated  
</script>
	
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>
						
<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
    <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
</p>
<!--Layout/intro1-->
    <p><content:getAttribute beanName="sduSubmitTabIntro" attribute="text"/></p>


<div class="table_controls_footer"></div>

<report:formatMessages scope="request"/>

<DIV id=errordivcs>
	<content:errors scope="session" />
</DIV>
<div>
	<div id="dropzoneMessageSection">
		<div id="fileSizeMessage" style ="display:none;">
			<div class="message message_error">
				<dl>
				<dt>Error Message</dt>
					<dd>You have exceeded the total file upload size limit for this request. The total size of all files selected for upload in a single request cannot exceed 25 MB. </dd>
				</dl>
			</div>
		</div> 
		
		<div id="noFileMessage" style ="display:none;">
			<div class="message message_error">
				<dl>
				<dt>Error Message</dt>
					<dd>No file has been selected. Click on Browse and select a file.</dd>
				</dl>
			</div>
		</div> 
			
		<div id="fileUploadFailMessage" style ="display:none;">
			<div class="message message_error">
				<dl>
				<dt>Error Message</dt>
					<dd id="uploadErrorMessage"></dd>
					<dd id="antiVirusScanMessage"></dd>
					<dd id="submittedErrorFileNames"></dd>
					<dd id="magicnumberValidationMessage"></dd>
					<dd id="virusScanServerFailureMessage"></dd>
					<dd id="errorMessage"></dd>
				</dl>
			</div>
		</div> 
	</div>
	
	
	<style>
		.message_confirmation {
		  max-height: 100%;
		  padding: 10px 15px;
		  /* background-color: #e4eaf6;*/
		  background: url(/assets/unmanaged/images/icons/icon_message_info.gif) no-repeat 15px 18px #e4eaf6;
		  border: 1px solid #c6c6c7; 
		}
		.message_confirmation td {
		  color: #000;
		}
	</style>
	<div id="documentUploadConfirmationSection" style ="display:none;">	  
		<div class="message message_confirmation">
			<table width="700" border="0" cellspacing="0" cellpadding="0" style="margin: 5px 0 5px 16px;">
			  <tbody>
				<tr>
				  <td colspan="2"><strong>The following file(s) have been successfully submitted.</strong></td>
				</tr>
				<tr>
				  <td width="25%" style=""><strong>Contract</strong></td>
				  <td width="75%"> 					
					<span id="contractNumber"></span>
					<span id="companyName"></span>
				  </td>
				</tr>
				<tr>
				  <td style=""><strong>Submission number</strong></td>
				  <td> <span id="submissionNumber"></span> </td>
				</tr>
				<tr>
				  <td style=""><strong>Date received </strong></td>
				  <td> <span id="createdTime"></span> </td>
				</tr>
				<tr>
				  <td style=""><strong>Submitted by </strong></td>
				  <td> <span id="submittedBy"></span> </td>
				</tr>
				<tr>
				  <td nowrap="" valign="top" style=""><strong>File name(s)</strong></td>
				  <td valign="top" style="">
					<span id="submittedFileNames"></span> 
				  </td>
				</tr>
			  </tbody>
			</table>
		</div>
	</div>
</div>

<% if (sduSubmitTabForm.isPendingContract()) { %>							
	<jsp:include page="sduNavigationBar.jsp" flush="true">
						<jsp:param name="selectedTab" value="SubmitTab" />
	</jsp:include>
<% }else{ %>
	<navigation:contractReportsTab />
<% }%>	

<c:if test="${not empty profileId}">
		<input type="hidden" name="profileId" value='<%= request.getParameter("profileId") %>' />
</c:if>	
	
<div class="page_section_subheader controls">
	<h3>Submit a Document</h3>
	<a href="javascript://" onClick="doPrint()"  class="pdf_icon"  valign="top" title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a> 
</div>
	
<form name="widjetForm">
	<input type="hidden" name="clientId" value="<%=SDUConstants.SDU_BD_CLIENT_ID%>" />
	<input type="hidden" name="baseURL" value="${sduSubmitTabForm.widgetEndpointURL}" />
</form>
		
<bd:form action="/do/bob/secureDocumentUpload/submit/" modelAttribute="sduSubmitTabForm" name="sduSubmitTabForm" 
	enctype="multipart/form-data" method="post">		
		<input type="hidden" path="submissionConfirmationJson"/>

<form:hidden path="submissionConfirmationJson"/>
<table border="0" cellspacing="0" cellpadding="5" width="100%">
	<tbody>
		<tr>
			<td width="140" class="" style=""><b>Contract</b></td>
            <td width="670" height="30" align="left" class="">
				<strong class="highlight">
					${bobContext.contractProfile.contract.contractNumber} 
					${bobContext.contractProfile.contract.companyName} 
				</strong>
			</td>
		</tr>
		<tr>
			<td width="100%" align="right" class="datacell1" colspan="2">&nbsp;</td>
		</tr>		
		<% if (sduSubmitTabForm.isDisplayFileUploadSection()) { %>
			<tr>
				<td width="140" class="datacell1" valign="top" style="padding-left: 4px;"><b>Select documents</b></td>
				<td width="670" height="15" align="left" class="datacell1">
					<div>
						<div id="dropzoneLayerPlaceholder"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td width="140" class="datacell1" valign="top"> </td>
				<td width="670" align="left" class="datacell1">
					<div>
						<div id="dropzoneMessageLayerPlaceholder"></div>
					</div>
				</td>
			</tr>

		<%} else {%>
		<tr>
			<td>&nbsp;</td>
			<% if (userProfile.isInMimic()) { %>
			<td class="datacell1"><span class="content"> Advisor View - you are not permitted to use this functionality. </span></td>
			<% } else { %>
			<td class="datacell1"><span class="content"> You are not authorized to submit documents. </span></td>
			<%} %>
			<td>&nbsp;</td>
		</tr>
		<%}%>
		<div id="widjetLayer" /> </div>
	</tbody>
</table>
</bd:form>
<div class="clear_footer">&nbsp;</div>
<% if (sduSubmitTabForm.isDisplayFileUploadSection()) { %>
<div class="button_regular" style="float : right">
	<a href="#" onclick="javascript:submitDocs();">Submit</a>
</div>
<div class="button_regular" style="float : right">
	<a href="#" onclick="javascript:clearAll();">Clear All</a>
</div>

<div style="display: none;">
	<div>
		<div id="dropzoneClearButtonLayerPlaceholder"></div>
	</div>
	<div>
		<div id="dropzoneUploadButtonLayerPlaceholder"></div>
	</div>	
</div>

<%}%>
</br>
<layout:pageFooter/>



