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
<%@ taglib uri="/WEB-INF/bd-logicext.tld" prefix="logicext"%>

<%@ page import="com.manulife.pension.bd.web.bob.secureDocumentUpload.SDUViewTabController"%>
<%@ page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUViewTabForm"%>
<%@page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUViewTabReportData" %>
<%@ page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUConstants"%>


<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Collections"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collection"%>
<%@page import="org.springframework.hateoas.PagedResources.PageMetadata"%>




<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<%
BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

<%
SDUViewTabForm sduViewTabForm=(SDUViewTabForm)request.getAttribute("sduViewTabForm");
pageContext.setAttribute("sduViewTabForm",sduViewTabForm,PageContext.SESSION_SCOPE);
%>

<%
SDUViewTabReportData theReport = (SDUViewTabReportData)request.getAttribute(SDUViewTabController.SDU_VIEWTAB_RESULTS);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

BDUserProfile userProfile = (BDUserProfile)session.getAttribute(SDUConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

				 
<content:contentBean contentId="<%=BDContentConstants.SDU_VIEW_TAB_INTRO %>" 
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="sduViewTabIntro" />
					 
<content:contentBean contentId="<%=BDContentConstants.SDU_NO_RECORDS_TO_DISPLAY_MESSAGE%>"
                     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                     id="NoResultsMessage"/>
<content:contentBean contentId="<%=BDContentConstants.SDU_DELETE_DOCUMENT_WARNING_MESSAGE%>"
                     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                     id="deleteDocumentWarningMessage"/>
<content:contentBean contentId="<%=BDContentConstants.SDU_FILE_NOT_AVAILABLE_MESSAGE%>" 
					 type="<%=BDContentConstants.TYPE_MESSAGE%>" 
					 id="fileNotAvailableErrorMessage"/>
<c:set var="deleteDocumentMessage" ><content:getAttribute id="deleteDocumentWarningMessage" attribute="text"/></c:set>
<c:set var="fileNotAvailableMessage" ><content:getAttribute id="fileNotAvailableErrorMessage" attribute="text"/></c:set>


<c:set var="contract" value="${bobContext.contractProfile.contract.contractNumber}"
	scope="request" />
	
<script type="text/javascript">
/**
 * This array object is used to pass as an argument to the call-back function
 */
var callbackArgument = new Array();

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
	loadingPanel.setBody("<div style='display: flex; font-size:12px'>Please wait while we prepare your file for download <span><img src='/assets/unmanaged/images/ajax-wait-indicator.gif'/></span></div>");
	loadingPanel.render(document.body);
	loadingPanel.show();  
}
function hideInProgressDivision() {
	loadingPanel.setBody('');
	loadingPanel.hide();
}

function sortSubmit(sortField, sortDirection) {
	document.forms['sduViewTabForm'].elements['task'].value = "sort";
	document.forms['sduViewTabForm'].elements['pageNumber'].value = 1;
	document.forms['sduViewTabForm'].elements['sortField'].value = sortField;
	document.forms['sduViewTabForm'].elements['sortDirection'].value = sortDirection;
	document.forms['sduViewTabForm'].submit();
}

function pagingSubmit(pageNumber){
	if (document.forms['sduViewTabForm']) {
		document.forms['sduViewTabForm'].elements['task'].value = "page";
		document.forms['sduViewTabForm'].elements['pageNumber'].value = pageNumber;
		document.forms['sduViewTabForm'].submit();
	}
}
	
function setCallbackArgument(argument) {
	// remove the array elements. 
	// Note: don't declare a new Array object, the call-back function will not take the new Object
	callbackArgument.splice(0, callbackArgument.length);
	callbackArgument[0] = argument[0];
	callbackArgument[1] = argument[1];
}

function hideWaitCursor(){
	document.body.style.cursor='default';
}

function showWaitCursor(){
	document.body.style.cursor='wait';
}


function downloadFile(fileId) {
	var fileInformation = new Array();
	fileInformation[0] = fileId;
	var fileName = document.getElementById("file_"+fileId).innerHTML;	
	fileInformation[1] = fileName.trim();
	setCallbackArgument(fileInformation);
	submissionUtilities.getOauthToken("/do/bob/secureDocumentUpload/token/",callback_Download,document.forms['sduViewTabForm']);
}

function deleteDocument(fileId, fileName) {	
	if(confirm("${deleteDocumentMessage}")){
		var fileInformation = new Array();
		fileInformation[0] = fileId;
		fileInformation[1] = fileName;
		setCallbackArgument(fileInformation);
		submissionUtilities.getOauthToken("/do/bob/secureDocumentUpload/token/",callback_Delete,document.forms['sduViewTabForm']);	
	}
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
		getOauthToken: function(actionPath, callbackFunction, form) {
				// Make a request
				YAHOO.util.Connect.setForm(form);
				//showWaitCursor();
				var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);
				},
	// Generic function to handle a failure in the server response  
		handleFailure : function(o){
			o.argument = null;
			hideWaitCursor();							
			window.location.href= window.location + "error/";
			
		},
	// Function to handle session Expired scenario
		sessionExpired : function(o){
			o.argument = null;
			hideWaitCursor();
			top.location.reload(true);
		},
	}; 

var callback_Download =    {
	success:  function(o) { 
		if(o.status == 200 && getResponseContentType(o) == 'text/plain'){
			var token = "Bearer "+o.responseText;	
			doDownload(o.argument,token);
		} else if (o.status == 200) {
			submissionUtilities.sessionExpired(o);			
		} else{
			submissionUtilities.handleFailure(o);
		}
	},
cache : false,
failure : submissionUtilities.handleFailure,
argument: callbackArgument
};

var callback_Delete =    {
	success:  function(o) { 	
		if(o.status == 200 && getResponseContentType(o) == 'text/plain'){
			var token = "Bearer "+o.responseText;	
			doDelete(o.argument,token);
		} else if (o.status == 200) {
			submissionUtilities.sessionExpired(o);			
		} else{
			submissionUtilities.handleFailure(o);
		}	
			

	},
cache : false,
failure : submissionUtilities.handleFailure,
argument: callbackArgument
};

function doDelete(fileInformation,authToken) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	 if (this.readyState == 1){
		showWaitCursor();
	 }
	 if (this.readyState == 4){
		hideWaitCursor();
		if (this.status != 200 && this.status != 401) {
			window.location.href= window.location + "error/";
		 }		 
		 if (this.status == 401) {
			alert("${fileNotAvailableMessage}");	
		 }
		 if (this.status == 200) {
			 if(this.responseText =='File already deleted.') {
				alert("${fileNotAvailableMessage}"); 
			 }
			 window.location.href= window.location;			 
		 }
	 }
  };  
  
	var requestURL = "${sduViewTabForm.apigeeProxyURL}<%=SDUConstants.SDU_DELETE_ENDPOINT%>";	
	xhttp.open("PUT", requestURL, true);
	xhttp.responseType = "text";
	xhttp.setRequestHeader("Authorization", authToken);
	xhttp.setRequestHeader("Content-Type", "application/json");
	var fileId = parseInt(fileInformation[0]);
	var jsonObj = new Object();
	jsonObj.clientId = "<%=SDUConstants.SDU_BD_CLIENT_ID%>";
	jsonObj.clientContract  = ${contract};	
	jsonObj.fileId = fileId;
	jsonObj.deletedByUserId = "${sduViewTabForm.downloadedByUserId}";
	jsonObj.deletedByUserName = "${sduViewTabForm.downloadedByUserName}";
	jsonObj.deletedByUserRole = "${sduViewTabForm.downloadedByUserRole}";
	var jsonPayload= JSON.stringify(jsonObj);
	xhttp.send(jsonPayload);
}

function doDownload(fileInformation,authToken) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	 if (this.readyState == 4){
		hideInProgressDivision();
		
		if (this.status != 200 && this.status != 401 ) {			
			window.location.href= window.location + "error/";
		 }
		 
		 if (this.status == 401) {
			alert("${fileNotAvailableMessage}");	
			window.location.href= window.location;			
		 }		 
		 //On Load and success
		if (this.status == 200) {
			var downloadFileName = fileInformation[1];	
			var contentType = this.getResponseHeader("content-type");			
			if (window.navigator.msSaveOrOpenBlob) {
			// Internet Explorer
				window.navigator.msSaveOrOpenBlob(new Blob([this.response], {type: contentType}), downloadFileName);			
			} else {			
				var el = document.getElementById('FileDownloadLink');
				el.setAttribute("type", "hidden");
				var myURL = window.URL || window.webkitURL;
				el.download = downloadFileName;
				const blob= new Blob([this.response], {type: contentType});                                        
				el.href = myURL.createObjectURL(blob);
				el.click();

			}
		}
	 }
  };  

  if (<%=sduViewTabForm.isAllowFileShare()%>){
	var requestURL = "${sduViewTabForm.apigeeProxyURL}<%=SDUConstants.SDU_DOWNLOAD_ENDPOINT%>/<%=SDUConstants.SDU_BD_CLIENT_ID%>/${contract}/fileInfoOnly/true?fileId=" + fileInformation[0] + "&userId=${sduViewTabForm.downloadedByUserId}&userName=${sduViewTabForm.downloadedByUserName}&userRole=${sduViewTabForm.downloadedByUserRole}";
  } else {
	var requestURL = "${sduViewTabForm.apigeeProxyURL}<%=SDUConstants.SDU_DOWNLOAD_ENDPOINT%>/<%=SDUConstants.SDU_BD_CLIENT_ID%>/${contract}/userId/${userProfile.BDPrincipal.profileId}?fileId=" + fileInformation[0] + "&userId=${sduViewTabForm.downloadedByUserId}&userName=${sduViewTabForm.downloadedByUserName}&userRole=${sduViewTabForm.downloadedByUserRole}";
  } 
  showInProgressDivision();
  xhttp.open("GET", requestURL, true);
  xhttp.responseType = "arraybuffer";
  xhttp.setRequestHeader("Authorization", authToken);
  xhttp.send();
}
</script>


<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>
						
<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
    <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
</p>
<!--Layout/intro1-->
<p><content:getAttribute beanName="sduViewTabIntro" attribute="text"/></p>   

<div class="table_controls_footer"></div>

<report:formatMessages scope="request"/>

<DIV id=errordivcs>
	<content:errors scope="session" />
</DIV>


<% if (sduViewTabForm.isPendingContract()) { %>							
	<jsp:include page="sduNavigationBar.jsp" flush="true">
						<jsp:param name="selectedTab" value="ViewTab" />
	</jsp:include>
<% }else{ %>
	<navigation:contractReportsTab />
<% }%>	

<bd:form action="/do/bob/secureDocumentUpload/view/" modelAttribute="sduViewTabForm" name="sduViewTabForm"	 method="post">	
	<input type="hidden" name="task" value="default"/>
	<form:hidden path="pageNumber"/>
	<form:hidden path="sortField"/>
	<form:hidden path="sortDirection"/>
	<form:hidden path="oAuthToken"/>
	<a id="FileDownloadLink" href=# type="hidden"></a>

	
<div class="page_section_subheader controls">
	<h3>Shared Documents</h3>
</div>       



<div class="report_table">
		<% if(sduViewTabForm.isDisplayViewTab()) { %> 
		
		<c:if test="${not empty theReport }">
			<c:if test="${not empty theReport.details}">
							<div class="table_controls">
								<div class="table_action_buttons"></div>
								<div class="table_display_info"><strong><report:recordCounter
									report="theReport" label="Documents" /></strong></div>
								<div class="table_pagination">
									<report:pageCounterViaSubmit formName="sduViewTabForm" report="theReport" arrowColor="black"/> 
								</div>
								<div class="table_controls_footer"></div>
							</div>
			</c:if>
		</c:if>
		
		<table class="report_table_content" id="history_table" style="word-wrap:break-word; table-layout: fixed;">
			<thead>
								
					<%if(sduViewTabForm.isAllowFileDelete()) { %> 	
					<tr colspan="6">	
					<th rowspan="2"  width="3%" class="val_str"> </th>
					<th rowspan="2"  width="21%" class="val_str">
					<%} else {%>
					<tr colspan="5">
					<th rowspan="2"  width="24%" class="val_str">
					 <%}%>					
						<report:bdSortLinkViaSubmit formName="sduViewTabForm" field="<%=SDUViewTabReportData.SORT_FILE_NAME%>" 
					direction="asc">Document</report:bdSortLinkViaSubmit>
					</th>						    
					<th rowspan="2" width="28%"  class="val_str">Comments</th>
					<th rowspan="2" width="18%"  class="val_str">Shared With</th>
					<th rowspan="2" width="18%"  class="val_str">
						<report:bdSortLinkViaSubmit formName="sduViewTabForm" field="<%=SDUViewTabReportData.SORT_SHARED_BY_USER_NAME%>" 
					direction="asc">Shared By</report:bdSortLinkViaSubmit>
					</th>
					<th rowspan="2" width="12%"  class="val_str">
						<report:bdSortLinkViaSubmit formName="sduViewTabForm" field="<%=SDUViewTabReportData.SORT_EXPIRY_TS%>" 
					direction="desc">Expires</report:bdSortLinkViaSubmit>
					</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty theReport.details}">
					<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
												
					<c:set var="indexValue" value="${theIndex.index}"/> 
					<% 				
					 String temp = pageContext.getAttribute("indexValue").toString();
						String rowClass = "spec";
							if (Integer.parseInt(temp) % 2 == 1) {
								rowClass = "";
							} else {
								rowClass = "spec";
							}
					%>
					<%if(sduViewTabForm.isAllowFileDelete()) { %> 	
					<tr class="<%=rowClass%>" colspan="6">	
					<td class="val_str"> <a href="javascript:deleteDocument('${theItem.fileId}', '');" > <img src="/assets/unmanaged/images/buttons/subtract_btn.gif" width="17" height="17" /></a> </td>					
				 <%} else {%>
						<tr class="<%=rowClass%>" colspan="5">	
					 <%}%>	
						<td class="val_str">
							<a id="file_${theItem.fileId}" href="javascript:downloadFile('${theItem.fileId}');"> ${theItem.fileName} </a>							
						</td>
                 
						<td class="val_str" > ${theItem.submissionDesc} </td>
						<td class="val_str">				
							<c:forEach var="Shareduserlist" items="${theItem.activeShareSubmissionsModel}"> 
								${Shareduserlist.sharedWithUserName}<c:if test="${ Shareduserlist.sendEmail == true}">*</c:if>
			                 	<br>
							</c:forEach> 
						</td>
						<td class="val_str">${theItem.clientUserName} </td>
						<td class="date">
							<render:date property="theItem.shareExpiryTs" patternOut="MM/dd/yyyy" defaultValue=""/>
						</td> 						
					</tr>							
					</c:forEach>
				</c:if>
			</tbody>	
		</table>
		
		<c:if test="${not empty theReport }">
			<c:if test="${not empty theReport.details}">
							<div class="table_controls">
								<div class="table_action_buttons"></div>
								<div class="table_display_info"><strong><report:recordCounter
									report="theReport" label="Documents" /></strong></div>
								<div class="table_pagination">
									<report:pageCounterViaSubmit formName="sduViewTabForm" report="theReport" arrowColor="black"/> 
								</div>
								<div class="table_controls_footer"></div>
							</div>
			</c:if>
		</c:if>
		
		<c:if test="${theReport.totalCount == 0}">
			  <div class="message message_info">
					<dl>
					<dt>Information Message</dt>
					 <dd><content:getAttribute beanName="NoResultsMessage" attribute="text" /></dd>
					</dl>
			  </div>
		</c:if>
	<div class="clear_footer">&nbsp;</div>	
	
	<% if (sduViewTabForm.isAllowFileShare()) { %>
		<div class="button_regular" style="float : right">
			<a onclick="location.href='/do/bob/secureDocumentUpload/shareDocuments/'">Add Document</a>
		</div>
    <%  } %>
    <div class="clear_footer">&nbsp;</div>	
  <p>
          *Email sent when document was shared by John Hancock.
    </p>
<%} else {%>

			  <div class="message message_info">
					<dl>
					<dt>Information Message</dt>
					 <dd>You are not authorized to view shared documents. </dd>
					</dl>
			  </div>
<%} %>
</div>

</bd:form>	
	
<div class="clear_footer">&nbsp;</div>	
		  
</br>
<layout:pageFooter/>
	
  