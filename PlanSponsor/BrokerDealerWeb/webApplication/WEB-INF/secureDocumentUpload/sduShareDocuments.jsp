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

<%@ page import="com.manulife.pension.bd.web.bob.secureDocumentUpload.SDUShareDocumentsController"%>
<%@ page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUShareDocumentsForm"%>
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
	SDUShareDocumentsForm sduShareDocumentsForm=(SDUShareDocumentsForm)request.getAttribute("sduShareDocumentsForm");
	pageContext.setAttribute("sduShareDocumentsForm",sduShareDocumentsForm,PageContext.PAGE_SCOPE);
%>

<%
	Map submissionMetaData = (Map) session.getAttribute("sduSubmissionMetaData");
	pageContext.setAttribute("submissionMetaData",submissionMetaData,PageContext.PAGE_SCOPE);
%>

<%
BDUserProfile userProfile = (BDUserProfile)session.getAttribute(SDUConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=BDContentConstants.SDU_SHARE_DOCS_PAGE_INTRO%>"
                     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                     id="SDUShareTabIntro"/>
                     
<content:contentBean contentId="<%=BDContentConstants.SDU_SHARE_DOCS_PAGE_PRIVACY_MESSAGE_BDW%>" 
					type="<%=BDContentConstants.TYPE_MESSAGE%>" 
					id="fileSharePrivacyWarningMessage"/>
<c:set var="fileSharePrivacyMessage" ><content:getAttribute id="fileSharePrivacyWarningMessage" attribute="text"/></c:set>
                     
                     
<c:set var="contract" value="${bobContext.contractProfile.contract.contractNumber}"
	scope="request" />

 <style>
  .ui-autocomplete-loading {
    background: white url("/assets/unmanaged/css/jquery-ui/images/ui-anim_basic_16x16.gif") right center no-repeat;
  }
  </style>
  
 <script>
$(function() {
    $('#searchUser').DataTable( {
                                "stripeClasses": [ 'datacell3', 'datacell1' ],
								"language": {
											"emptyTable": "No external contact selected."											
											},                
                                "paging":   false,								
                                "ordering": false,
								"info":     false,
                                "searching":       false,
                                "columnDefs": [ {
                                                "targets": 4,
                                                "visible": false
                                },
								{
                                                "targets": 5,
                                                "visible": false
                                },
                                 {
                                    "targets": 6,
                                    "data": 'false',
									"className": "dt-center",
                                    "render": function(data, type, full, meta) {
                                        return "<input type='checkbox'  name='checkAll'  value='true' checked  onclick='checkboxChange(this)' />"

                                    }
                                }								],
								"scrollY":        "125px",
								"scrollCollapse": false
			});
	});



function toggle() {
	    checkboxes = document.getElementsByName('checkAll');
	    var uncheckedflag = false;
	    for (var i = 0, n = checkboxes.length; i < n; i++) {
	        if (!checkboxes[i].checked) {
	            uncheckedflag = true;
	        }
	    }
	    for (var i = 0, n = checkboxes.length; i < n; i++) {
	        if (uncheckedflag) {
	            checkboxes[i].checked = true;
	        } else {
	            checkboxes[i].checked = false;
	        }
	        if (checkboxes[i].checked) {
	            $('#searchUser').DataTable().rows().every(function() {
	                this.data()[6] = true;
	            });
	        } else {
	            $('#searchUser').DataTable().rows().every(function() {
	                this.data()[6] = false;
	            });
	        }
	    }
	}

  $( function() { 
    $( "#search" ).autocomplete({
      source: function( request, response ) {
        $.ajax( {
          url: "/do/bob/secureDocumentUpload/shareDocuments/?task=search&like="+$('#search').val(),
          dataType: "json",
		  jsonpCallback:'getUsers',
          data: {
            term: request.term
          },
		  type: 'GET',
          success: function( data ) {		  
            response( data );			
          }
        } );
      },
      minLength: 2,	 	 
      select: function( event, ui ) {
		var userExist=false;
		  var t=$('#searchUser').DataTable();
		  $('#searchUser').DataTable().rows().every(function () {
				var profileId = this.data()[4];				
				if(ui.item.profileId==profileId){
					userExist=true;
				}
			});	
		  if(userExist==false){
			  document.getElementById("externalContactRequired").style.display="none"; 
		  t.row.add (["<img src='/assets/unmanaged/images/buttons/subtract_btn.gif' width='17' height='17' onclick='deleteRow(this)'/>",ui.item.fullName,ui.item.emailAddress,ui.item.securityRoleDescription,ui.item.profileId,ui.item.securityRoleCode,true])
			 .draw(); 
		  }
      }
    })
	.autocomplete( "instance" )._renderItem = function( ul, item ) {
      return $( "<li>" )
        .append( "<div><b>"+item.fullName + "</b>&nbsp;|&nbsp;<font color='blue'>" + item.emailAddress +"</font>&nbsp;|&nbsp;"+ item.securityRoleDescription + "</div>" )
        .appendTo( ul );
    };
	
  } );
  
  
function checkboxChange(r) {
 var i = r.parentNode.parentNode;
	    $('#searchUser').DataTable().rows(i).every(function() {

	        if (this.data()[6]) {
	            this.data()[6] = false;
	        } else {
	            this.data()[6] = true;
	        }
	    });
	}
 function deleteRow(r) {
	var i = r.parentNode.parentNode;
	$('#searchUser').DataTable().row(i).remove().draw();
 }
  </script>
  
<script type="text/javascript"> 

	var sduOAuthToken = "Bearer ";
	var sduClientId = "<%=SDUConstants.SDU_BD_CLIENT_ID%>";
	var sduClientUserId = "${userProfile.BDPrincipal.profileId}";
	var sduFileUploadJsonString=""; 
	var uploadSuccessRedirectURL="/do/bob/secureDocumentUpload/view/";
	var sendEmail = false;

	
	<% if (sduShareDocumentsForm.isDisplayFileUploadSection()) { %>
	var isFileSectionShown=true;
	<% } else { %>
		var isFileSectionShown=false;
	<% } %>
	
	function clearAll() {
		document.getElementById("clearAll").click();
		$('#searchUser').DataTable().clear().draw();
		document.getElementById("submissionComments").value="";
	}	
	
	function prepareShareInfoJson(){
		var shareInfoList = new Array();
		$('#searchUser').DataTable().rows().every(function () {
			shareInfoList.push({ sharedWithUserId: this.data()[4], sharedWithUserName: this.data()[1], sharedWithUserRole: this.data()[3],sharedWithEmailAddress: this.data()[2],sendEmail:(this.data()[6]) ? true:false });
			if(this.data()[6]){
				sendEmail = true;				
			}
		});
		document.forms['sduShareDocumentsForm'].elements['shareInfoJson'].value = JSON.stringify(shareInfoList);
		document.forms['sduShareDocumentsForm'].elements['sendEmail'].value = sendEmail;
	}
	
	function shareDocs() {
		if($('#searchUser').DataTable().rows().data().length > 0){
			var displayPrivayMessage = true;	
			if(displayPrivayMessage){				
				if(confirm("${fileSharePrivacyMessage}")){
					prepareShareInfoJson();
					submissionUtilities.doAsyncRequest_Post("/do/bob/secureDocumentUpload/shareDocuments/getSubmissionMetaData/",callback_getSubmissionMetaData,document.forms['sduShareDocumentsForm']);
				}				
			}else {
				prepareShareInfoJson();
				submissionUtilities.doAsyncRequest_Post("/do/bob/secureDocumentUpload/shareDocuments/getSubmissionMetaData/",callback_getSubmissionMetaData,document.forms['sduShareDocumentsForm']);
			}			
		} else {
			document.getElementById("externalContactRequired").style.display="block"; 
		}
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
						window.location.href= "/do/bob/secureDocumentUpload/shareDocuments/error/";						
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
				submissionUtilities.doAsyncRequest_Post("/do/bob/secureDocumentUpload/token/",callback_UploadDocuments,document.forms['sduShareDocumentsForm']);
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
		<% if (sduShareDocumentsForm.isDisplayFileUploadSection() ) { %>
		submissionUtilities.doAsyncRequest_Post("/do/bob/secureDocumentUpload/token/",callback_LoadWidjet,document.forms['sduShareDocumentsForm']);
		<% }  %>
	});
	function showInProgressDivision() {
		 // Only instantiate if one doesn't exist.  
		// Otherwise drop to the show() method.
		loadingPanel = new YAHOO.widget.Panel("Image_0",  
						{   width: "210px", 
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
		showInProgressDivision();
		submissionUtilities.doAsyncRequest("${sduShareDocumentsForm.widgetEndpointURL}<%=SDUConstants.SDU_WIDGET_DROPZONE_ENDPOINT%>", callback_checkDropzoneGenerated);	
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
	//Control will come to this method after the failure upload
	function onUploadFailure(uploadFailureJsonObj){			
		displayFailureErrorMessages(uploadFailureJsonObj);			
	}	
	
	//Control will come to this method after the successful upload
	function onUploadSuccess(uploadConfirmationJsonObj){
		 document.forms['sduShareDocumentsForm'].elements['submissionId'].value = uploadConfirmationJsonObj.submissionNumber;
			// alert("sendEmail"+sendEmail);
			 if(sendEmail==true){
			showInProgressDivision();
			  submissionUtilities.doAsyncRequest_Post("/do/bob/secureDocumentUpload/shareDocuments/sendEmailNotification/",callback_SendEmail,document.forms['sduShareDocumentsForm']);
			 }else{
				 hideInProgressDivision();
				 window.location.href=uploadSuccessRedirectURL;
			 }
		}	
		
		var callback_SendEmail = {
				success:  function(o) { 	
					if(o.status == 200){
						hideInProgressDivision();
						window.location.href=uploadSuccessRedirectURL;
					}
					else{
						submissionUtilities.handleFailure(o);
					}
				},
			cache : false,
			failure : submissionUtilities.handleFailure
			};
</script>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>
						
<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong></p>
<!--Layout/intro1-->
    <p><content:getAttribute beanName="SDUShareTabIntro" attribute="text"/></p>

	
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
		<div id="externalContactRequired" style ="display:none;">
			<div class="message message_error">
				<dl>
				<dt>Error Message</dt>
					<dd>At least one external contact is required.</dd>
				</dl>
			</div>
		</div> 
	</div>
</div>



<c:if test="${not empty profileId}">
		<input type="hidden" name="profileId" value='<%= request.getParameter("profileId") %>' />
</c:if>	
	
	
<form name="widjetForm">
	<input type="hidden" name="clientId" value="<%=SDUConstants.SDU_BD_CLIENT_ID%>" />
	<input type="hidden" name="baseURL" value="${sduShareDocumentsForm.widgetEndpointURL}" />
</form>

<bd:form action="/do/bob/secureDocumentUpload/shareDocuments/" modelAttribute="sduShareDocumentsForm" name="sduShareDocumentsForm" enctype="multipart/form-data" method="post">	
		
		<input type="hidden" path="submissionConfirmationJson"/>

<form:hidden path="shareInfoJson"/>
<form:hidden path="sendEmail"/>
<form:hidden path="submissionId"/>
<table border="0" cellspacing="0" cellpadding="5" width="810">
	<tbody>
		<tr>
			<td width="140" class="" style="" valign="top"><b>Contact</b></td>
            <td width="670">
				<div class="report_table">
				<div class="table_controls">
                  <div id="div_namePhrase" style="float:right; padding-top: 5px; padding-right: 5px;">
					 <input id="search" name="search" type="text" maxlength="25" value="">
                  </div>
                  <div style="float:right; font-size: 11px; padding-top: 8px; padding-right: 5px;"><label for="contact_search">Search: </label></div>
                  <div class="table_controls_footer"></div>
                </div>
				<table id="searchUser" width="670" class="report_table_content" style="font-size: 16px;">
					<thead>
						<tr class="val_str">
							<th align="left" width="14px" class="val_str"> </th>					
							<th align="left" class="val_str">Name</th>                                
							<th align="left" class="val_str">Email Address</th>                                
							<th align="left" class="val_str">Role</th>								
							<th align="left" class="val_str">Id</th>
							<th align="left" class="val_str">RoleId</th>
                            <th align="left" class="val_str" width="65px">Notify </br> <a href="javascript:toggle()" >Select all</a></th>
						</tr> 
					</thead>
					<tbody>
					</tbody>
				</table>
				</div>
			</td>

		</tr>
		<tr>
			<td width="100%" align="right" class="datacell1" colspan="2">&nbsp;</td>
		</tr>		
		<tr>
			<td width="140" class="" style="" valign="top"><b>Comments</b></td>
            <td width="670" height="30" align="left" class="">
				<form:textarea path="submissionComments" maxlength="500" size="10" tabindex="8" cssStyle="width: 348px;" rows="3" cols="54" />	
			</td>
		</tr>
		<tr>
			<td width="140" class="datacell1" valign="top"> </td>
			<td width="670" align="left" class="datacell1">
			<span class="content">Details can be added here.</span>
			</td>
		</tr>		
		<tr>
			<td width="100%" align="right" class="datacell1" colspan="2">&nbsp;</td>
		</tr>
		<% if (sduShareDocumentsForm.isDisplayFileUploadSection()) { %>
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
			<td class="datacell1"><span class="content"> You are not permitted to use this functionality. </span></td>
			<td>&nbsp;</td>
		</tr>
		<%}%>
		<div id="widjetLayer" /> </div>
	</tbody>
</table>
</bd:form>
<div class="clear_footer">&nbsp;</div>
<% if (sduShareDocumentsForm.isDisplayFileUploadSection()) { %>
<div class="button_regular" style="float : right">
	<a href="#" onclick="javascript:shareDocs();">Share</a>
</div>
<div class="button_regular" style="float : right">
	<a href="#" onclick="javascript:clearAll();">Clear All</a>
</div>
<div class="button_regular" style="float : right">
	<a href="#" onclick="location.href='/do/bob/secureDocumentUpload/view/'">Cancel</a>
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



