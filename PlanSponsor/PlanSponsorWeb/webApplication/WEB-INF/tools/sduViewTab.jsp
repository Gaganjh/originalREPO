<%@taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
      
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>       
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<%@page import="com.manulife.pension.ps.web.tools.SDUViewTabController"%>
<%@page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUViewTabForm"%>
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



<%
SDUViewTabReportData theReport = (SDUViewTabReportData)request.getAttribute(SDUViewTabController.SDU_VIEWTAB_RESULTS);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE); 
UserProfile userProfile = (UserProfile)session.getAttribute(SDUConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<jsp:useBean id="sduViewTabForm"
             scope="session" 
             class="com.manulife.pension.platform.web.secureDocumentUpload.SDUViewTabForm" />

<content:contentBean contentId="<%=ContentConstants.SDU_VIEW_TAB_INTRO %>" 
					 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="sduViewTabintro" />
<content:contentBean contentId="<%=ContentConstants.SDU_NO_RECORDS_TO_DISPLAY_MESSAGE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="NoResultsMessage"/>
<content:contentBean contentId="<%=ContentConstants.SDU_DELETE_DOCUMENT_WARNING_MESSAGE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="deleteDocumentWarningMessage"/>
<content:contentBean contentId="<%=ContentConstants.SDU_FILE_NOT_AVAILABLE_MESSAGE%>" 
					 type="<%=ContentConstants.TYPE_MESSAGE%>" 
					 id="fileNotAvailableErrorMessage"/>
<c:set var="deleteDocumentMessage" ><content:getAttribute id="deleteDocumentWarningMessage" attribute="text"/></c:set>
<c:set var="fileNotAvailableMessage" ><content:getAttribute id="fileNotAvailableErrorMessage" attribute="text"/></c:set>

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
	loadingPanel.setBody("<div style='display: flex; justify-content: center;'> Please wait while we prepare your file for download <span><img src='/assets/unmanaged/images/ajax-wait-indicator.gif'/></span></div>");
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
	submissionUtilities.getOauthToken("/do/tools/secureDocumentUpload/token/",callback_Download,document.forms['sduViewTabForm']);
}

function deleteDocument(fileId, fileName) {	
	if(confirm("${deleteDocumentMessage}")){
		var fileInformation = new Array();
		fileInformation[0] = fileId;
		fileInformation[1] = fileName;
		setCallbackArgument(fileInformation);
		submissionUtilities.getOauthToken("/do/tools/secureDocumentUpload/token/",callback_Delete,document.forms['sduViewTabForm']);	
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
		//Function to handle session Expired scenario
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
	jsonObj.clientId = "<%=SDUConstants.SDU_PS_CLIENT_ID%>";
	jsonObj.clientContract  = ${userProfile.contractProfile.contract.contractNumber};	
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
	var requestURL = "${sduViewTabForm.apigeeProxyURL}<%=SDUConstants.SDU_DOWNLOAD_ENDPOINT%>/<%=SDUConstants.SDU_PS_CLIENT_ID%>/${userProfile.contractProfile.contract.contractNumber}/fileInfoOnly/true?fileId=" + fileInformation[0] + "&userId=${sduViewTabForm.downloadedByUserId}&userName=${sduViewTabForm.downloadedByUserName}&userRole=${sduViewTabForm.downloadedByUserRole}";
  } else {
	var requestURL = "${sduViewTabForm.apigeeProxyURL}<%=SDUConstants.SDU_DOWNLOAD_ENDPOINT%>/<%=SDUConstants.SDU_PS_CLIENT_ID%>/${userProfile.contractProfile.contract.contractNumber}/userId/${userProfile.principal.profileId}?fileId=" + fileInformation[0] + "&userId=${sduViewTabForm.downloadedByUserId}&userName=${sduViewTabForm.downloadedByUserName}&userRole=${sduViewTabForm.downloadedByUserRole}";
  } 
  showInProgressDivision();
  xhttp.open("GET", requestURL, true);
  xhttp.responseType = "arraybuffer";
  xhttp.setRequestHeader("Authorization", authToken);
  xhttp.send();
}



</script>


<p>
<DIV id=errordivcs>
	<content:errors scope="session" />
</DIV>
</p>

<table width="700">
<tbody>
	<tr>
		<td width="10"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
        <td>&nbsp;</td>
    </tr>
</tbody>
</table>

    <!-- Tab selection -->
    <c:if test="${empty param.printFriendly}">
    <jsp:include page="sduNavigationBar.jsp" flush="true">
        <jsp:param name="selectedTab" value="ViewTab" />
    </jsp:include>
    </c:if>
    <c:if test="${not empty param.printFriendly}">
    <jsp:include page="sduNavigationBar.jsp" flush="true">
        <jsp:param name="selectedTab" value="ViewPrintTab" />
    </jsp:include>
    </c:if>
    
    <!-- Tab selection -->



<ps:form cssStyle="margin-bottom:0;" method="POST" modelAttribute="sduViewTabForm" name="sduViewTabForm" action="/do/tools/secureDocumentUpload/view/">


<input type="hidden" name="task" value="default"/>
<form:hidden path="pageNumber"/>
<form:hidden path="sortField"/>
<form:hidden path="sortDirection"/>
<form:hidden path="oAuthToken"/>
<a id="FileDownloadLink" href=# type="hidden"></a>

    <table width="700" id="viewTabResultsTable" class="tableBorder" style="table-layout:fixed; padding: 1px; background-color: rgb(204, 204, 204);" border="0" cellspacing="0" cellpadding="0">
        <tbody>
            <tr>
                <td height="16" class="tablesubhead" colspan="11"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
            </tr>
            <!-- sduViewTabintro -->
            <tr class="datacell1">
                <td colspan="11">
                    <table border="0" cellspacing="0" cellpadding="5">
                        <tbody>
                            <tr>
                                <td width="700" height="30" class="datacell1" style="padding: 4px;">
                                    <content:getAttribute beanName="sduViewTabintro" attribute="text" />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            <!-- sduViewTabintro -->
            <!-- Table records for pagination -->
            <tr class="tablehead">
                <td height="25" class="tableheadTD" colspan="11">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tbody>
                            <tr>
                                <td width="300" class="tableheadTD" style="padding-left: 2px;"><b>Shared document(s)</b></td>
								<% if(sduViewTabForm.isDisplayViewTab()) {%>
                                <td class="tableheadTDinfo"><b><report:recordCounter report="theReport" label=""/></b></td>
                                <td class="tableheadTDinfo"></td>
								<c:if test="${empty param.printFriendly}">
	                                <td align="right" class="tableheadTD">
	                                	<report:pageCounterViaSubmit report="theReport" arrowColor="white" name="parameterMap" formName="sduViewTabForm"/>
	                                </td>
								</c:if>
								<%}%>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
           
 <% if(sduViewTabForm.isDisplayViewTab()) { %>         
           
            <!--  Header section  for fields -->
            <tr class="tablesubhead">

                <table width="698" class="tableBorder" style="table-layout:fixed; padding: 1px; background-color: rgb(204, 204, 204);" border="0" cellspacing="0" cellpadding="0">
                    <tr class="tablesubhead">
						<%if(sduViewTabForm.isAllowFileDelete()) { %> 	
                        <td valign="top" align="left" class="fileAction" style="padding-left: 0px;" width="16"> </td>
						<td valign="top" align="left" class="fileName" style="padding-left: 4px;" width="126">
                        <%} else {%>
                        <td valign="top" align="left" class="fileName" style="padding-left: 4px;" width="142">
						 <%}%>
                            <b>
                            <report:sortLinkViaSubmit formName="sduViewTabForm" field="<%=SDUViewTabReportData.SORT_FILE_NAME%>" 
							direction="asc">Document</report:sortLinkViaSubmit>	
							</b>
                        </td>

                        <td class="dataheaddivider" width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                        <td valign="top" align="left" class="submissionComments" style="padding-left: 4px;" width="220">
                            <b>Comments</b>
                        </td>
                        <td class="dataheaddivider" width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                        <td valign="top" align="left" class="submissionSharedWith" style=" word-wrap: break-word; padding-left: 4px;" width="141">
                            <b>Shared with</b>
                        </td>
                        <td class="dataheaddivider" width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                        <td valign="top" align="left" class="submissionSharedBy" style=" word-wrap: break-word; padding-left: 4px;" width="110">
                            <b>
                            <report:sortLinkViaSubmit formName="sduViewTabForm" field="<%=SDUViewTabReportData.SORT_SHARED_BY_USER_NAME%>" 
							direction="asc">Shared by</report:sortLinkViaSubmit>
							</b>
                        </td>
                        <td class="dataheaddivider" width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                        <td valign="top" align="left" class="submissionExpiryTs" style="padding-left: 4px;" width="61">
                            <b>
                            <report:sortLinkViaSubmit formName="sduViewTabForm" field="<%=SDUViewTabReportData.SORT_EXPIRY_TS%>" 
							direction="desc">Expires</report:sortLinkViaSubmit>
							
							</b>
                        </td>
                    </tr>
                  
     <!--  Report data display -->
     
     
              <c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
     
               <c:if test="${ theIndex.index % 2 == 0}">
                	<tr class="datacell1">                
                </c:if>
                <c:if test="${ theIndex.index % 2 != 0}">
                	<tr class="datacell2">                
                </c:if>	
                
				<%if(sduViewTabForm.isAllowFileDelete()) { %> 				
				<td align="left" class="fileAction" nowrap="nowrap" style="padding-left: 2px; " height="20">
              	 <a href="javascript:deleteDocument('${theItem.fileId}', '');" > <img src="/assets/unmanaged/images/delete_icon.gif" width="12" height="12" />  </a>          
              	</td>
				 <%} %>
				 
                <td align="left" class="fileName" style=" word-wrap: break-word; padding:3px;">
                	<a id="file_${theItem.fileId}" href="javascript:downloadFile('${theItem.fileId}');"> ${theItem.fileName} </a>					
                </td>
                
                <td class="datadivider" width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td align="left" class="submissionComments" style=" word-wrap: break-word; padding:3px;"> ${theItem.submissionDesc} </td>
                <td class="datadivider" width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
				<td align="left" class="submissionSharedWith" style="padding:3px;"> 
				<c:forEach var="Shareduserlist" items="${theItem.activeShareSubmissionsModel}"> 
				 	${Shareduserlist.sharedWithUserName}<c:if test="${Shareduserlist.sendEmail==true}">*</c:if>
				 	<br>
				</c:forEach>
				
				 </td>
                <td class="datadivider" width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td align="left" class="submissionSharedBy" style=" word-wrap: break-word; padding:3px;"> ${theItem.clientUserName}  </td>
                <td class="datadivider" width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td align="left" class="submissionExpiryTs" nowrap="nowrap" style="padding:3px;" colspan="2">
                <render:date property="theItem.shareExpiryTs" patternOut="MM/dd/yyyy" defaultValue=""/></td>
                
				</tr>  
				</c:forEach>	  
				<!--  report data end -->
				<!--  Display message when the result row count is 0 -->
				<c:if test="${theReport.totalCount == 0}">
				<tr class="datacell1">
				        <% if(sduViewTabForm.isDisplayViewTab()) {%>
					   <td width="700" class="datacell1" colspan="11">
						<b><content:getAttribute beanName="NoResultsMessage" attribute="text" /></b>
						<%}%>
					  </td>
				</tr>
			   </c:if>
                </table>
            </tr>
        <tr>
            <td width="10"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
            <td>&nbsp;</td>
        </tr>
		
 		

<% if (sduViewTabForm.isAllowFileShare()) { %>
<table width="735" border="0" cellspacing="0" cellpadding="0">
            <tbody>
              <tr align="center">
                <td colspan="2"><img width="1" height="15" src="/assets/unmanaged/images/s.gif"></td>
              </tr>    
              <tr>
                <td width="360"><div align="right"> </div></td>
                <td width="170" align="center">&nbsp;
                                 
                </td>
                <td width="130" align="center">
                <c:if test="${not empty param.printFriendly}">
                  <input name="actionLabel" class="button134" type="button" value="add document" disabled="disabled">
                </c:if>
                <c:if test="${empty param.printFriendly}">
                 <input name="actionLabel" class="button134" onclick="location.href='/do/tools/secureDocumentUpload/shareDocuments/'" type="button" value="add document" enabled="enabled">
  				</c:if>
                </td>
              </tr>            
            </tbody>
          </table>
          <%  } %>
        <table width="735" border="0" cellspacing="0" cellpadding="0">
            <tbody> 
         <tr>
            <td width="700" style="padding:30px;">*Email sent when document was shared by John Hancock.</td>           
        </tr>
		</tbody>
		</table>

<%} else {%>

	<tr class="datacell1">
		<td width="700" height = "75" class="datacell1" colspan="11" align="center"> You are not authorized to view shared documents. </td>
	</tr>
<%} %>
        </tbody>
    </table>
</ps:form>
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