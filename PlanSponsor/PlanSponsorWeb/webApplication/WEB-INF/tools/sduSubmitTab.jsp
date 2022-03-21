<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<%@ page import="com.manulife.pension.ps.web.tools.SDUSubmitTabController"%>
<%@ page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmitTabForm"%>
<%@ page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUConstants"%>
<%@ page import="com.manulife.pension.ps.web.ErrorCodes"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.google.gson.Gson"%>

<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<%
	Map submissionMetaData = (Map) session.getAttribute("sduSubmissionMetaData");
	pageContext.setAttribute("submissionMetaData",submissionMetaData,PageContext.PAGE_SCOPE);
%>

<jsp:useBean id="sduSubmitTabForm" scope="session"
	class="com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmitTabForm" />
	
<%
UserProfile userProfile = (UserProfile)session.getAttribute(SDUConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<content:contentBean
	contentId="<%=ContentConstants.SDU_SUBMIT_TAB_INTRO %>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="SDUSubmitTabIntro" />


<c:set var="contract" value="${userProfile.currentContract}"
	scope="request" />


<script type="text/javascript"> 
	var sduOAuthToken = "Bearer ";
	var sduClientId = "<%=SDUConstants.SDU_PS_CLIENT_ID%>";
	var sduClientUserId = "${userProfile.principal.profileId}";
	var sduFileUploadJsonString="";
	var uploadSuccessRedirectURL="";
	
	<% if (sduSubmitTabForm.isDisplayFileUploadSection()) { %>
	var isFileSectionShown=true;
	<% } else { %>
		var isFileSectionShown=false;
	<% } %>
	
	var lastAccessedTime = <%=  request.getSession(false).getLastAccessedTime() %>
	var maxInactiveInterval = <%=  request.getSession(false).getMaxInactiveInterval() %>
	
	function clearAll() {
		if (confirm("Are you sure you want to clear all files?")) {
			document.getElementById("clearAll").click();
		}
	}
	
	function submitDocs() {
		submissionUtilities.doAsyncRequest_Post("/do/tools/secureDocumentUpload/submit/getSubmissionMetaData/",callback_getSubmissionMetaData,document.forms['sduSubmitTabForm']);
	}

	function protectLinks() {
			
		var hrefs  = document.links;
		if (hrefs != null)
		{
			for (i=0; i<hrefs.length; i++) {
				if(
					hrefs[i].onclick != undefined && 
					(hrefs[i].onclick.toString().indexOf("openWin") != -1 || hrefs[i].onclick.toString().indexOf("popup") != -1 || hrefs[i].onclick.toString().indexOf("doSignOut") != -1)
				) {
					// don't replace window open or popups as they won't loose there changes with those
				}
				else if(
					hrefs[i].href != undefined && 
					(hrefs[i].href.indexOf("openWin") != -1 || hrefs[i].href.indexOf("popup") != -1 || hrefs[i].href.indexOf("doSignOut") != -1)
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
	if (window.addEventListener) {
		window.addEventListener('load', protectLinks, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', protectLinks);
	}
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
						window.location.href= "/do/tools/secureDocumentUpload/submit/error/";						
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
				submissionUtilities.doAsyncRequest_Post("/do/tools/secureDocumentUpload/token/",callback_UploadDocuments,document.forms['sduSubmitTabForm']);
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
		<% if (sduSubmitTabForm.isDisplayFileUploadSection() && request.getParameter("printFriendly") == null ) { %>
		submissionUtilities.doAsyncRequest_Post("/do/tools/secureDocumentUpload/token/",callback_LoadWidjet,document.forms['sduSubmitTabForm']);
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
						{   width: "210px", 
							height:"25px",
							fixedcenter: true, 
							close: false, 
							draggable: false, 
							zindex:4,
							modal: true,
							visible: false,
							constraintoviewport: true
						} 
					);
	    //loadingPanel.setBody("<span style='padding-left:10px;float:right;padding-right:15px;padding-top:10px;padding-botton:5px;text-align:centre;font-size:1.1em'>One moment please...<br>Do not refresh your browser</span><img style='padding-bottom:12px;padding-left:0px;align:centre' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
	    loadingPanel.setBody("<div style='display: flex; justify-content: center;'> One moment please... <br/> Do not refresh your browser &nbsp;&nbsp;&nbsp;<span><img src='/assets/unmanaged/images/ajax-wait-indicator.gif'/></span></div>");
		loadingPanel.render(document.body);
	    loadingPanel.show();  
		}
		function hideInProgressDivision() {
			loadingPanel.hide();
		}
	function loadWidjet(){
		showInProgressDivision();
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
		submissionUtilities.doAsyncRequest_Post("/do/tools/secureDocumentUpload/submit/?task=print",callback_doPrint,document.sduSubmitTabForm);
	}
	
	function displaySubmissionConfirmation(uploadConfirmationJsonObj){		
			document.getElementById("documentUploadConfirmationSection").style.display = "block";			
			if(uploadConfirmationJsonObj.submissionNumber!=null){
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
			 fileNamesListHtml += "<ul><li>"+uploadFailureJsonObj.fileNames[i] +"</li></ul>"+ "\n";			 
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
				document.getElementById("uploadErrorMessage").innerHTML="<ul><li>Unable to upload file, please select file and try again.</li></ul>";				
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
			reportURL.setParameter("task", "print");
			reportURL.setParameter("printFriendly", "true");
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

<form name="widjetForm">
	<input type="hidden" name="clientId" value="<%=SDUConstants.SDU_PS_CLIENT_ID%>" />
	<input type="hidden" name="baseURL" value="${sduSubmitTabForm.widgetEndpointURL}" />
</form>

<DIV id=errordivcs>
	<content:errors scope="session" />
</DIV>
<div>
<div id="dropzoneMessageSection">
	<div id="fileSizeMessage" style ="display:none;">
		<table>
			<tr><td class="redText">
					<ul>
						<li> 
							<!--You have exceeded the total file upload size limit for this request. The total size of all files selected for upload in a single request cannot exceed ${sendDocumentUploadModel.maximumFileSize} MB.-->
							You have exceeded the total file upload size limit for this request. The total size of all files selected for upload in a single request cannot exceed 25 MB.
						</li>
					</ul>
				</td>
			</tr>
		</table>
	</div> 
	
	<div id="noFileMessage" style ="display:none;">
		<table>
			<tr><td class="redText">
					<ul>
						<li> 
							No file has been selected. Click on Browse and select a file.
						</li>
					</ul>
				</td>
			</tr>
		</table>
	</div> 
		
	<div id="fileUploadFailMessage" style ="display:none;">
		<table>
			<tr><td class="redText">
					
							<span id="uploadErrorMessage" >
					</span>
				</td>
			</tr>		
			  <tr>
                            <td class="redText" nowrap><span id="antiVirusScanMessage" ></span></td>
							</tr>
							<tr>
							
							<td class="redText" >							 
							 <span id="submittedErrorFileNames"></span><br>
								</td>
							</tr>	
							
							  <tr>
                            <td class="redText" nowrap><span id="magicnumberValidationMessage" ></span></td>
							</tr>	
						<tr>
							
							<td class="redText" >
							 <span id="errorMessage"></span><br>							
								</td>
							</tr>
								<!--  FileScan Service or Clam AV server is down -->
							<tr><td class="redText">					
							<span id="virusScanServerFailureMessage"></span></td>
							</tr>	
		</table>
	</div> 
	
	<div id="documentUploadConfirmationSection" style ="display:none;">
			<table cellspacing="0" cellpadding="0" width="100%" border="0">
                <colgroup>
                	<col width="30%">
                	<col width="70%">
                </colgroup>
                <tr>
					<strong>The following file(s) have been successfully submitted.</strong>				
                          </tr>
						  <tr>
                            <td height="14" colspan="2">&nbsp;</td>
                          </tr>         
                          <tr>
                            <td nowrap><strong>Contract &nbsp;&nbsp;</strong></td>
                            <td>
                            	<STRONG class="highlight"> 
					            	<span id="contractNumber"></span>
				                    <span id="companyName"></span> 
					            </STRONG>
                            </td>
                          </tr>                          
						  <tr>
                            <td nowrap><strong>Submission number</strong></td>
                            <td><strong class="highlight" ><span id="submissionNumber"></span></strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Date received </strong></td>
                            <td ><strong class="highlight"><span id="createdTime"></span></strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Submitted by </strong></td>
                            <td ><strong class="highlight"><span id="submittedBy"></span></strong></td>
                          </tr>
                            <tr>
                            <td valign="top" nowrap><strong>File name(s) </strong></td>
							<td>
																				
                            <strong class="highlight"><span id="submittedFileNames"></span><br></strong>
											
							</tr>						  
                          <tr>
                            <td height="14" colspan="2">&nbsp;</td>
                          </tr>
                          <tr>
							<td height="14" >&nbsp;</td>
                          </tr>
                      </table>
	</div>	
</div>


</div>
<br>
<br>
<table border="0" cellspacing="0" cellpadding="0" width="715">
	<tbody>
		<tr>
		     <c:if test="${empty param.printFriendly}">
			<td>
			<jsp:include page="sduNavigationBar.jsp" flush="true">
					<jsp:param name="selectedTab" value="SubmitTab" />
				</jsp:include></td>
			</c:if>	
			 <c:if test="${not empty param.printFriendly}">
			<td>
			<jsp:include page="sduNavigationBar.jsp" flush="true">
					<jsp:param name="selectedTab" value="SubmitPrintTab" />
				</jsp:include></td>
			</c:if>	
		</tr>
		<ps:form id="dropzone-form" action="/do/tools/secureDocumentUpload/submit/"
			modelAttribute="sduSubmitTabForm" name="sduSubmitTabForm"
			enctype="multipart/form-data" method="POST"
			cssStyle="border:0;background:none;">
		<form:hidden path="submissionConfirmationJson"/>
				
			<table border="0" cellspacing="0" cellpadding="0" class="tableBorder"
				width="700"
				style="padding: 1px; background-color: rgb(204, 204, 204);">
				<tbody>
					<tr>
						<td class="tablesubhead" height="16" colspan="12"><img
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
						<td colspan="12">
							<table border="0" cellspacing="0" cellpadding="5">
								<tbody>
									<tr>
										<td width="700" class="datacell1" style="padding: 4px;"
											colspan="2"><content:getAttribute
												beanName="SDUSubmitTabIntro" attribute="text" /></td>
									</tr>
									<tr class="tablehead" height="25">
										<td class="tablehead" colspan="8"><b><content:getAttribute
													beanName="layoutPageBean" attribute="body1Header" /></b></td>
									</tr>
									<TR>
										<TD width="140" align="left" class="datacell1"
											style="padding-left: 4px;"><B>Contract</B></TD>
										<TD width="460" height="30" align="left" class="datacell1">
											<STRONG class="highlight">
												${userProfile.currentContract.contractNumber}
												${userProfile.currentContract.companyName} 
										</TD>
									</TR>
									<% if (sduSubmitTabForm.isDisplayFileUploadSection()) { %>
									<c:if test="${empty param.printFriendly}">
									<tr>
										<td width="100%" align="right" class="datacell1" colspan="2">&nbsp;</td>
									</tr>
									<TR>
										<TD width="140" align="left" class="datacell1" valign="top"><B>Select
												documents</B></TD>
										<TD width="460" height="15" align="left" class="datacell1"
											colspan="2">
											<!-- this is were the previews should be shown. -->
											<div>
												<div id="dropzoneLayerPlaceholder"></div>
											</div>
										</TD>
									</TR>
									<TR>
										<TD width="100" class="datacell1" valign="top">
										<TD align="left" class="datacell1" colspan="2">
											<div>
												<div id="dropzoneMessageLayerPlaceholder"></div>
											</div>
										</TD>
									</TR>
									</c:if>
									<%} else {%>
									<tr>
										<td>&nbsp;</td>
										<td class="datacell1"><span class="content"> You
												are not authorized to submit documents. </span></td>
										<td>&nbsp;</td>
									</tr>
									<%}%>
									<tr>
										<td width="140" class="datacell1" valign="top"></td>
										<td width="460" align="left" class="datacell1"><span
											class="content"> <img
												src="/assets/unmanaged/images/s.gif" width="1" height="25">
												<logicext:if name="userProfile"
													property="allowedUploadSubmissions" op="equal" value="true">
													<logicext:then>
														<img src="/assets/unmanaged/images/s.gif" width="1"
															height="5">
														<!-- file upload helpful hint -->
														<content:rightHandLayerDisplay layerName="layer1"
															beanName="layoutPageBean" />
													</logicext:then>
												</logicext:if> <img src="/assets/unmanaged/images/s.gif" width="1"
												height="5"> <content:rightHandLayerDisplay
													layerName="layer2" beanName="layoutPageBean" /> <img
												src="/assets/unmanaged/images/s.gif" width="1" height="5">
												<content:rightHandLayerDisplay layerName="layer3"
													beanName="layoutPageBean" /> <img
												src="/assets/unmanaged/images/s.gif" width="1" height="5">
												<content:rightHandLayerDisplay layerName="layer4"
													beanName="layoutPageBean" /> <img
												src="/assets/unmanaged/images/s.gif" width="1" height="5">
										</span></td>
									</tr>

								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</ps:form>
		<% if ( sduSubmitTabForm.isDisplayFileUploadSection() && request.getParameter("printFriendly") == null ) { %>
		<br />
		<br />
		<span class="content">
			<table width="700" border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr align="center">
						<td colspan="2"><img width="1" height="15"
							src="/assets/unmanaged/images/s.gif"></td>
					</tr>
					<tr>
						<td width="360"><div align="right"></div></td>
						<td width="170" align="center">
							<input name="actionLabel" onclick="javascript:clearAll();" class="button134"  type="button" value="clear all" enabled="enabled">
						</td>
						<td width="130" align="center">
							<input name="actionLabel" onclick="javascript:submitDocs();" class="button134"  type="button" value="submit" enabled="enabled">
						</td>
					</tr>
					
					<tr align="center" style="display: none;">
						<td width="170" align="center">
							<div>
								<div id="dropzoneClearButtonLayerPlaceholder"></div>
							</div>
						</td>
						<td width="130" align="center">
							<div>
								<div id="dropzoneUploadButtonLayerPlaceholder"></div>
							</div>
						</td>	
					</tr>
				</tbody>
			</table>
		</span>

		<% } %>
		


		<div id="Image_0">
			<div class="bd" id="Image_0_BD"
				style="position: relative; background-color: rgb(255, 255, 255);">
			</div>
			<img src="/assets/unmanaged/images/s.gif" width="1" height="1" />
		</div>


		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="fixedTable">
			<tr>
				<td width="15"><img src="/assets/unmanaged/images/spacer.gif"
					width="15" height="1" border="0"></td>
				<td><img src="/assets/unmanaged/images/spacer.gif" width="1	"
					height="1" border="0"></td>
			</tr>

			<tr>
				<td height="20">&nbsp;</td>
				<td width="15"><img src="/assets/unmanaged/images/spacer.gif"
					width="15" height="1" border="0"></td>
				<td></td>
			</tr>
		</table>
		
		<c:if test="${not empty param.printFriendly}">
						<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
							type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
							id="globalDisclosure" />

						<table width="760" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="760"><content:getAttribute beanName="globalDisclosure" attribute="text" /></td>
							</tr>
						</table>
		</c:if>

		<div id="widjetLayer" />
