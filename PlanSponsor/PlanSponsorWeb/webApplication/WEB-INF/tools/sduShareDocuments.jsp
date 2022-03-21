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
<%@ page import="com.manulife.pension.ps.web.ErrorCodes"%>

<%@ page import="com.manulife.pension.ps.web.tools.SDUShareDocumentsController"%>
<%@ page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUShareDocumentsForm"%>
<%@ page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUConstants"%>
<%@ page import="java.util.Collection"%>


<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<jsp:useBean id="sduShareDocumentsForm" scope="session"
	class="com.manulife.pension.platform.web.secureDocumentUpload.SDUShareDocumentsForm" />
	
<%
UserProfile userProfile = (UserProfile)session.getAttribute(SDUConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<content:contentBean
	contentId="<%=ContentConstants.SDU_SUBMIT_TAB_INTRO %>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="SDUSubmitTabIntro" />
<content:contentBean
	contentId="<%=ContentConstants.SDU_SHARE_DOCS_PAGE_INTRO %>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="SDUShareTabIntro" />
<content:contentBean contentId="<%=ContentConstants.SDU_SHARE_DOCS_PAGE_PRIVACY_MESSAGE%>" 
					 type="<%=ContentConstants.TYPE_MESSAGE%>" 
					 id="fileSharePrivacyWarningMessage"/>
<c:set var="fileSharePrivacyMessage" ><content:getAttribute id="fileSharePrivacyWarningMessage" attribute="text"/></c:set>


<c:set var="contract" value="${userProfile.currentContract}"
	scope="request" />
  <style>
  .ui-autocomplete-loading {
    background: white url("/assets/unmanaged/css/jquery-ui/images/ui-anim_basic_16x16.gif") right center no-repeat;
  }
  table.dataTable tbody tr.datacell1 {
                PADDING-RIGHT: 2px; PADDING-LEFT: 2px; FONT-SIZE: 11px; BACKGROUND: #ffffff; PADDING-BOTTOM: 2px; PADDING-TOP: 2px
}
table.dataTable tbody tr.datacell3 {
                PADDING-RIGHT: 2px; PADDING-LEFT: 2px; FONT-SIZE: 11px; BACKGROUND: #DCECF1; PADDING-BOTTOM: 2px; PADDING-TOP: 2px
}

table.dataTable th {
border-right: 1px solid #dddddd;
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
                                        return "<input type='checkbox'  name='checkAll'  value='true'  checked onclick='checkboxChange(this)' />"

                                    }
                                }
                                ],
								"scrollY":        "150px",
								"scrollCollapse": false
			});
	});


	
  $( function() { 
    $( "#search" ).autocomplete({
      source: function( request, response ) {
        $.ajax( {
          url: "/do/tools/secureDocumentUpload/shareDocuments/?task=search&like="+$('#search').val(),
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
		  t.row.add (["<img src='/assets/unmanaged/images/delete_icon.gif' width='12' height='12' onclick='deleteRow(this)'/> ",ui.item.fullName,ui.item.emailAddress,ui.item.securityRoleDescription,ui.item.profileId,ui.item.securityRoleCode,true])
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
 function deleteRow(r) {
	var i = r.parentNode.parentNode;
	$('#searchUser').DataTable().row(i).remove().draw();
 }

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

  </script>
  
<script type="text/javascript"> 

	var sduOAuthToken = "Bearer ";
	var sduClientId = "<%=SDUConstants.SDU_PS_CLIENT_ID%>";
	var sduClientUserId = "${userProfile.principal.profileId}";
	var sduFileUploadJsonString=""; 
	var uploadSuccessRedirectURL="/do/tools/secureDocumentUpload/view/";
	var sendEmail = false;

	<% if (sduShareDocumentsForm.isDisplayFileUploadSection()) { %>
	var isFileSectionShown=true;
	<% } else { %>
		var isFileSectionShown=false;
	<% } %>
	
	var lastAccessedTime = <%=  request.getSession(false).getLastAccessedTime() %>
	var maxInactiveInterval = <%=  request.getSession(false).getMaxInactiveInterval() %>
	
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
			var displayPrivayMessage = false;
			$('#searchUser').DataTable().rows().every(function () {
				var roleCode = this.data()[5];
				if(roleCode == "<%=SDUConstants.INC_ROLE%>" || roleCode == "<%=SDUConstants.PPY_ROLE%>" || roleCode == "<%=SDUConstants.TPA_ROLE%>" || roleCode == "<%=SDUConstants.PSU_ROLE%>"  ){
					displayPrivayMessage = true;
				}	
			});		
			if(displayPrivayMessage){				
				if(confirm("${fileSharePrivacyMessage}")){
					prepareShareInfoJson();
					submissionUtilities.doAsyncRequest_Post("/do/tools/secureDocumentUpload/shareDocuments/getSubmissionMetaData/",callback_getSubmissionMetaData,document.forms['sduShareDocumentsForm']);
				}				
			}else {
				prepareShareInfoJson();
				submissionUtilities.doAsyncRequest_Post("/do/tools/secureDocumentUpload/shareDocuments/getSubmissionMetaData/",callback_getSubmissionMetaData,document.forms['sduShareDocumentsForm']);
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
						window.location.href= "/do/tools/secureDocumentUpload/shareDocuments/error/";						
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
					submissionUtilities.doAsyncRequest_Post("/do/tools/secureDocumentUpload/token/",callback_UploadDocuments,document.forms['sduShareDocumentsForm']);
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
		submissionUtilities.doAsyncRequest_Post("/do/tools/secureDocumentUpload/token/",callback_LoadWidjet,document.forms['sduShareDocumentsForm']);
		<% }  %>
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
	    loadingPanel.setBody("<div style='display: flex; justify-content: center;'> One moment please... <br/> Do not refresh your browser &nbsp;&nbsp;&nbsp;<span><img src='/assets/unmanaged/images/ajax-wait-indicator.gif'/></span></div>");
		//loadingPanel.setBody("<span style='padding-left:10px;float:right;padding-right:15px;padding-top:10px;padding-botton:5px;text-align:centre;font-size:1.1em'>One moment please...<br>Do not refresh your browser</span><img style='padding-bottom:12px;padding-left:0px;align:centre' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
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
		 document.forms['sduShareDocumentsForm'].elements['submissionId'].value = uploadConfirmationJsonObj.submissionNumber;
		// alert("sendEmail"+sendEmail);
		 if(sendEmail==true){
		showInProgressDivision();
		  submissionUtilities.doAsyncRequest_Post("/do/tools/secureDocumentUpload/shareDocuments/sendEmailNotification/",callback_SendEmail,document.forms['sduShareDocumentsForm']);
		 }else{
			 hideInProgressDivision();
			 window.location.href=uploadSuccessRedirectURL;
		 }
	}	
	//Control will come to this method after the failure upload
	function onUploadFailure(uploadFailureJsonObj){			
		displayFailureErrorMessages(uploadFailureJsonObj);			
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

<form name="widjetForm">
	<input type="hidden" name="clientId" value="<%=SDUConstants.SDU_PS_CLIENT_ID%>" />
	<input type="hidden" name="baseURL" value="${sduShareDocumentsForm.widgetEndpointURL}" />
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
</div>	

<div id="externalContactRequired" style ="display:none;">
	<table>
		<tr><td class="redText">
				<ul>
					<li> 
						At least one external contact is required.
					</li>
				</ul>
			</td>
		</tr>
	</table>
</div> 
 
</div>
<br>
<br>

		<ps:form id="dropzone-form" action="/do/tools/secureDocumentUpload/shareDocuments/"
			modelAttribute="sduShareDocumentsForm" name="sduShareDocumentsForm"
			enctype="multipart/form-data" method="POST"
			cssStyle="border:0;background:none;">
		<form:hidden path="shareInfoJson"/>
		<form:hidden path="sendEmail"/>
		<form:hidden path="submissionId"/>
		
		<span class="content">				
	<table border="0" colspan="1" cellspacing="0" cellpadding="0" class="tableBorder"
			width="700"
			style="padding: 1px; background-color: rgb(204, 204, 204);">
		<tbody>
			<tr class="datacell1">						
				<table width="700" border="0" cellspacing="0" cellpadding="0" class="tableBorder" style="padding: 1px; background-color: rgb(204, 204, 204);" >
					<tbody>
						<tr class="tablehead" height="25">
							<td class="tablehead" colspan="2"><b> Add a document </b></td>
						</tr>
						<tr>
							<td class="datacell1" colspan="2">
								<content:getAttribute
										beanName="SDUShareTabIntro" attribute="text" />
							</td>
						</tr>
					</tbody>
				</table>
			<table border="0" colspan="2" cellspacing="0" cellpadding="0" class="tableBorder"
				width="700"
				style="padding: 1px; background-color: rgb(204, 204, 204);">
				<tbody>
						<tr>
							<td class="datacell1" colspan="2"><img width="1" height="4"
							src="/assets/unmanaged/images/s.gif"></td>
						</tr>
							<tr>
								<td width="100" class="datacell1" valign="top" style="padding-left: 4px;"><b>Contact</b></td>
								<td width="600" align="left" class="datacell1" valign="top">                          
									<table  width="560" align="left" class="tableBorder" style="padding: 1px; background-color: rgb(204, 204, 204);" border="0" cellspacing="0" cellpadding="0">
										<tbody>
										<tr class="datacell1">
											<td align="right" style="padding: 4px;" colspan="7">
											<strong>Search</strong> <input id="search" name="search" type="text" maxlength="25" value="">
								            </td>
										</tr>
										</tbody>
											</table>
											<br/>
									<table  width="560" align="left" class="tableBorder" style="padding: 1px; background-color: rgb(204, 204, 204);" border="0" cellspacing="0" cellpadding="0">
										<tbody>
										<tr>
											<td width="558px" class="datacell1" style="padding: 0px;"">
											<table  id="searchUser" class="compact cell-border">
												<thead>
												<tr class="tablesubhead">
													<th align="left" style=" padding: 1px; width: 19px;"> </th>					
													<th align="left" style="padding: 4px; width: 133px;"><b>Name</b> </th>                                
													<th align="left" style="padding: 4px; width: 253px;"><b>Email Address</b></th>                                
													<th align="left" style="padding: 4px; width: 123px;"><b>Role</b></th>								
													<th align="left" ><b>Id</b></th>
													<th align="left" ><b>RoleId</b></th>
													<th align="left" style="padding: 4px; width: 60px;"><b>Notify </b></br> <a href="javascript:toggle()" >Select all</a></th>	
												</tr> 
												</thead>
												<tbody>
												</tbody>
											</table>
											</td>
										</tr>                              
										</tbody>
									</table>
                          
									  <style>
										.highlight {
										  background-color: #FFFFCC;
										}                                    
									  </style>
									  <script>
										$(function() {
										  $('td:first-child input').on("change",function() {
											  $(this).closest('tr').toggleClass("highlight", this.checked);
										  });
										});                             
									  </script>
                          
							</td>
						</tr>
						<tr>
							<td width="100%" align="right" class="datacell1" colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td width="100" style="padding-left: 4px;" align="left" valign="top" class="datacell1"><B>Comments </B></td>
							<TD width="600" align="left" class="datacell1"
								colspan="2">
								 <form:textarea path="submissionComments" maxlength="500" size="10" tabindex="8" cssStyle="width: 348px;" rows="3" cols="54" />						
								</TD>

						</tr>
						<tr>
							<td width="100" class="datacell1" valign="top"> </td>
							<td width="600" align="left" class="datacell1">
							<span class="content">Details can be added here.</span>
							</td>
						</tr>
						<tr>
							<td width="100%" align="right" class="datacell1" colspan="2">&nbsp;</td>
						</tr>
						<TR>
							<TD width="100" align="left" class="datacell1" valign="top" style="padding-left: 4px;" ><B>Select documents</B></TD>
							<TD width="600" height="15" align="left" class="datacell1" colspan="2">
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
						<tr>
							<td width="100" class="datacell1" valign="top"></td>
							<td width="600" align="left" class="datacell1"><span
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
		<br />

			<table width="730" border="0" cellpadding="0" cellspacing="0" >
				<tbody>
					<tr align="right">
						<td colspan="2"><img width="1" height="15"
							src="/assets/unmanaged/images/s.gif"></td>
					</tr>
					<tr align="right">
						<td width="265"><div align="right"></div></td>
						<td width="130" align="right">
						 <input name="actionLabel" class="button134" onclick="location.href='/do/tools/secureDocumentUpload/view/'" type="button" value="cancel" enabled="enabled">
						 </td>
						 <td width="170" align="center">
						 <input name="actionLabel" onclick="javascript:clearAll();" class="button134"  type="button" value="clear all" enabled="enabled">
						 </td>
						 <td width="130" align="right">
						 <input name="actionLabel" onclick="javascript:shareDocs();" class="button134"  type="button" value="share" enabled="enabled">
						 </td>
						<td img width="5"><img width="5" height="15"
							src="/assets/unmanaged/images/s.gif"></td>
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

		<div id="widjetLayer" />
