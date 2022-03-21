<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<jsp:useBean id="submissionCensusForm" scope="session" class="com.manulife.pension.ps.web.tools.SubmissionCensusForm" />
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="contract" value="${userProfile.currentContract}" />


<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_MISC_RESET_BUTTON_DESCRIPTION%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="resetButtonDescription"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_MISC_SUBMIT_BUTTON_DESCRIPTION%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="submitButtonDescription"/>
<content:contentBean contentId="<%=ContentConstants.CENSUS_NOT_ALLOWED_TO_UPLOAD%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="notAllowedToUpload"/>
                      

<content:contentBean contentId="<%=ContentConstants.SUBMISSION_VALID_FILE_INFO%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidFileInfo"/>							
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_IN_PROGRESS%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidUploadInProgress"/>							
<content:contentBean contentId="<%=ContentConstants.CENSUS_SAMPLE_FILE_INFORMATION%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="censusSampleFileInformation"/>							

<script type="text/javascript" > 
	//AG: formName == submissionCensusForm
	// lastpayroll => lastPayroll
	function isFormChanged() {
	
		if (isFileSectionShown) {
			var uploadFormObj = getFormByName("submissionCensusForm");
			var filename = uploadFormObj.uploadFile.value;
			if (!isNullInput(filename)==true) {
				return true;
			}
		}
		return false;
	}

	var isNetscape=eval(navigator.appName == "Netscape");
	var isExplorer=eval(navigator.appVersion.indexOf("MSIE")!=-1);
 	function getFormByName(formName) {
	 	if (navigator.appName == "Netscape" && parseInt(navigator.appVersion) == 4) {
			return getFormByNameNav4(formName);
	   	} else {
			return document.forms[formName];
 	  	}
  	}

 	function getFormByNameNav4(formName, parent) {
	   	var objForm;
	  	var parentObj= (parent) ? parent : document;
	    	objForm= parentObj.forms[formName];
	   	if (!objForm) {
			for (var i= 0; i < parentObj.layers.length && !objForm; i++) {
				objForm= getFormByNameNav4(formName, parentObj.layers[i].document);
			}
	  	}
	      return objForm;
        }
	
	function writeError(text) {
		//alert('>>> Write error' + text + ' isNetscape = ' + isNetscape);

		var contentString;
		if (text.length > 0 )
			contentString = '<table id="psErrors"><tr><td class="redText"><ul><li>' + text + '</li></ul></td></tr></table>';
		else 
			contentString = '';
		
		if ( document.getElementById ) {
			document.getElementById('errordivcs').innerHTML = contentString;
		}else if (isExplorer) {
			document.all.errordivcs.innerHTML = contentString;
		} else if (isNetscape) {
			//this is old netscape
			document.errordivcs.document.open();
			document.errordivcs.document.write(contentString);
			document.errordivcs.document.close();
		}

		if (text!="" && text!=" " &&text.length>0) {
			location.href='#TopOfPage';
		}

		//alert('Write error' + text);
	}

	<% if (submissionCensusForm.isDisplayFileUploadSection()) { %>
		var isFileSectionShown=true;
	<% } else { %>
		var isFileSectionShown=false;
	<% } %>

	var uploadcredit=1;

	var errorMessage="";

	function resetValues(){
		writeError("");
		var uploadFormObj = getFormByName("submissionCensusForm");
		uploadFormObj.reset();
		location.href='#TopOfPage';
	}
	function isNullInput(str) {

		b = ((str==null) || (""==str));
		//alert("isNullInput returns " + b + "for '" + str + "'" );
		return b;
	}

	function validateFileSelectionSection() {

		if (isFileSectionShown) {

			//alert('validate file');

			var uploadFormObj = getFormByName("submissionCensusForm");

			var filename = uploadFormObj.uploadFile.value;
			if (isNullInput(filename)==true) {
				writeError('<content:getAttribute beanName="messageValidFileInfo" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_VALID_FILE_INFO%> + ']');
				//alert("Please select a file");
				return false;
			} else if (isExplorer && filename.indexOf('\\') == -1) {
				writeError("Please enter the directory where the file is located");
				return false;
			}

		}
		return true;
	}

	function validateInputs() {
		var b1 = validateFileSelectionSection();
		//alert('validate all returns ' + b3);
		var b = b1;
		//alert('validate inputs: ' + b);
		return b;
	}

	var uploadCanceld=false;
	function cancelUpload(){
		uploadCanceld=true;
	}

	function confirmSend() {
		//alert('Confirm');

		var uploadFormObj = getFormByName("submissionCensusForm");

		if (uploadcredit != 1 ) {
			//alert('uploadcredit');
			writeError("");
			hideInProgressDivision();
			writeError("<content:getAttribute beanName="messageValidUploadInProgress" attribute="text"/>"+ "&nbsp;[" + <%=ContentConstants.SUBMISSION_UPLOAD_IN_PROGRESS%> + "]" +"<br>If you have trouble with your upload click <a href=\"/do/tools/censusUpload/\">here</a> to start over.");
		   	return false;
	 	} else if (validateInputs() == true) {
		   	var message;
			message = "";
			//alert('Confirm - valid 1');
			var filename = null;
			if (isFileSectionShown) {
				filename = uploadFormObj.uploadFile.value;
				if (isNullInput(filename)==false) {
					message = message + "Upload file: "+filename;
				}
			}

			//alert('Confirm - valid 2');
         	message = message +"?";

		   	if (confirm(message)) {
				uploadcredit = 0;
				writeError("");
				showInProgressDivision();
			 	location.href='#TopOfPage';
				return true;
		   	} else {
				return false;
		  	}
	   	} else {
			return false;
	    }
	}
	function initDocument() {
		writeError(errorMessage);
	}
	function showInProgressDivision() {
		if ( document.getElementById ) {
			document.getElementById('inProgressDivision').style.display = 'block';
		}else if (isExplorer) {
			document.all['inProgressDivision'].style.display = 'block';
		} else if (isNetscape) {
			document.layers['inProgressDivision'].display = 'block';
		}
		return true;
	}
	function hideInProgressDivision() {
		if ( document.getElementById ) {
			document.getElementById('inProgressDivision').style.display = 'none';
		}else if (isExplorer) {
			document.all['inProgressDivision'].style.display = 'none';
		} else if (isNetscape) {
			document.layers['inProgressDivision'].display = 'none';
		}
		return true;
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
	
	registerTrackChangesFunction(isFormChanged);
	if (window.addEventListener) {
		window.addEventListener('load', protectLinks, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', protectLinks);
	}	
</SCRIPT>

	<A NAME="TopOfPage"></A>&nbsp;
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">

			<tr>
				<td>
					<DIV id="inProgressDivision" style="display: none;";>
						<TABLE BORDER=0 CELLPADDING=1 CELLSPACING=0>
							<TR><TD ALIGN="left"><b>Upload in progress.</b></TD>
							</TR>
							<TR><TD ALIGN="left"><content:getAttribute beanName="messageValidUploadInProgress" attribute="text"/>&nbsp;[<%=ContentConstants.SUBMISSION_UPLOAD_IN_PROGRESS%>]
							<br>If you have trouble with your upload click <a href="/do/tools/censusUpload/">here</a> to start over.
							</TD>
							</TR>
						</TABLE> 
					</DIV>
					<DIV id=errordivcs><content:errors scope="session"/></DIV><br>
				</td>
				<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
				<td><img src="/assets/unmanaged/images/spacer.gif" width="1	" height="1" border="0"></td>
			</tr>

            <tr>
              <td height="20">
    	 		   <ps:form action="/do/tools/censusUpload/" modelAttribute="submissionCensusForm" name="submissionCensusForm" enctype="multipart/form-data" method="POST" onsubmit="return confirmSend();">

				  <table width="500" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td width="113"><img src="/assets/unmanaged/images/s.gif" width="80" height="1" /></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td width="463"><img src="/assets/unmanaged/images/s.gif" width="250" height="1" /></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td width="113"><img src="/assets/unmanaged/images/s.gif" width="80" height="1" /></td>
                      <td width="7"><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="tablehead" height="25">
                      <td class="tableheadTD1" colspan="8"> <b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
                    </tr>
                    <tr class="datacell1">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td colspan="6" rowspan="13" >
                      <table width="516" border="0" cellpadding="0" cellspacing="0">
                          <tr>
	                        <td>&nbsp;</td>
                            <td width="50" class="datacell1"><strong>Contract</strong></td>
                            <td class="datacell1">
                            <strong class="highlight">
${userProfile.currentContract.contractNumber}

${userProfile.currentContract.companyName}

							</strong>
                            </td>
                            <td>&nbsp;</td>
                          </tr>
               <!--
                          <tr>
                          	<td>&nbsp;</td>
                          	<td class="datacell1"><strong>email</strong></td>
                            <td class="datacell1">
                          	<strong class="highlight">
                          	${submissionCensusForm.email}
                          	
                          	<form:hidden path="email">${submissionCensusForm.email}</form:hidden>
                            </strong>
                            </td>
                            <td>&nbsp;</td>
                          </tr>
                          <tr>
                              <td colspan="2">&nbsp;</td>
                              <td class="datacell1">Email listed above is on file and everything is getting sent there.</td>
                              <td>&nbsp;</td>
                          </tr>
              -->
                          <tr>
                            <td colspan="4" width="100%" align="right" class="datacell1">&nbsp;</td>
                          </tr>

						  <tr>
	                        <td>&nbsp;</td>
    	                    <td colspan="2" class="datacell1" valign="top"><strong>File information</strong></td>
                        		<!-- <content:getAttribute beanName="censusSampleFileInformation" attribute="text"/> -->
							<td>&nbsp;</td>
                    	  </tr>
                    	  
                    	  
						  <% if (submissionCensusForm.isDisplayFileUploadSection()) { %>
						  <tr>
						    <td>&nbsp;</td>
							<td colspan="2" class="datacell1"><span class="content"><content:getAttribute beanName="layoutPageBean" attribute="body1"/><br/><br/></span></td>
							<td>&nbsp;</td>
						  </tr>
						  <tr>
						    <td>&nbsp;</td>
							<td colspan="2" class="datacell1"><strong><content:getAttribute beanName="layoutPageBean" attribute="body2"/></strong></td>
							<td>&nbsp;</td>
						  </tr>
						  <tr>
							<td colspan="4" >&nbsp;</td>
						  </tr>
						  <tr>
						  	<td>&nbsp;</td>
							<td colspan="2">
								<table cellspacing="0" cellpadding="0" width="500" border="0">
								<!--DWLayoutTable-->
		
								  <tr>
									<td width="78" align="left" class="datacell1"><span class="content"><strong>File type</strong></span>
									</td>
									<td width="178" align="left" class="datacell1">
									  <span class="content">
										  Census 
									  </span>
									</td>
									<td width="244" align="left" class="datacell1">
										&nbsp;
									</td>
								  </tr>
								  <tr>
									<td valign="top" class="datacell1" celspan="2"><!--DWLayoutEmptyCell-->&nbsp;</td>
									<td height="15" align="left" valign="top" class="datacell1" celspan="2">
										&nbsp;
									</td>
									<td height="15" valign="top" class="datacell1" celspan="2"><!--DWLayoutEmptyCell-->&nbsp;</td>
								  </tr>
								  <tr>
									<td valign="top" class="datacell1" celspan="2"><!--DWLayoutEmptyCell-->&nbsp;</td>
									<td height="15" align="left" valign="top" class="datacell1" celspan="2"><!--DWLayoutEmptyCell-->&nbsp;</td>
									<td height="15" valign="top" class="datacell1" celspan="2"><!--DWLayoutEmptyCell-->&nbsp;</td>
								  </tr>
								  <tr>
									<td celspan="2" valign="top" class="datacell1">
										<span class="content">
											<strong>File name </strong>
										</span>
									</td>
									<td height="15" colspan="2" valign="top" class="datacell1" celspan="2"><span class="content">
									  <!-- input type="file" size="60" name="uploadfile" / -->
									  <input type="file" name="uploadFile" size="58" />
									  <!-- size="<=(isIE? 60 : 30)>" -->
									  <br />
									  (Maximum file size accepted is 5 MB)</span>
									</td>
								  </tr>
								
							</table>
							</td>
							<td>&nbsp;</td>
						  </tr>
						  
						  <%} else {%>
						  <tr>
						    <td>&nbsp;</td>
							<td colspan="2" class="datacell1"><span class="content"><content:getAttribute id="notAllowedToUpload" attribute="text"/></span></td>
							<td>&nbsp;</td>
						  </tr>
						  <%}%>


                          <tr>
                            <td colspan="4" >&nbsp;</td>
                          </tr>
                      </table>
                      </td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>


                    <tr class="datacell2">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell1">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell2">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell1">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell2">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell1">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell2">
                      <td class="databorder"></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell1">
                      <td class="databorder"></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell2">
                      <td class="databorder"></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell1">
                      <td class="databorder"></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell2">
                      <td class="databorder"></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell1">
                      <td class="databorder"></td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="datacell1">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
                      <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td  colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
                    </tr>
                    <tr>
                      <td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                </table>
                <% if ( submissionCensusForm.isDisplayFileUploadSection() ) { %>
					<br/><br/>
					<table width="455" border="0" cellpadding="0" cellspacing="0">
						<tbody>
						  <tr>
							<td width="143">&nbsp;</td>
							<td width="166"><span class="content">
								<input type="button" class="button134" onclick="resetValues();" value="clear" />
							</span></td>
							<td width="146"><span class="content">
<input type="submit" class="button134" value="send" name="actionLabel" />
							</span></td>
						  </tr>
						  <tr>
							<td width="143">&nbsp;</td>
							<td width="166" valign="top"><span class="content">
								<content:getAttribute beanName="resetButtonDescription" attribute="text"/>
							</span></td>
							<td width="146" valign="top"><span class="content">
								<content:getAttribute beanName="submitButtonDescription" attribute="text"/>
							</span></td>
						  </tr>
						</tbody>
					</table>
				<% } %>

				</ps:form>
				</td>

				<!--// end column 2 -->

				<!-- column 3 HELPFUL HINT -->
				<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
				<td width="210" align="center" valign="top">
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="25">  
				   
					<logicext:if name="userProfile" property="allowedUploadSubmissions" op="equal" value="true">				
						<logicext:then>
						   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
						   <!-- file upload helpful hint -->
						   <content:rightHandLayerDisplay layerName="layer1" beanName="layoutPageBean" /> 
						</logicext:then>					
					</logicext:if>
					
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer2" beanName="layoutPageBean" />   
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer3" beanName="layoutPageBean" />   
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer4" beanName="layoutPageBean" />   
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
		  		  
				</td>

				<!--// end column 3 -->


			</tr>
            <tr>
              <td height="20">&nbsp;</td>
 			  <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
			  <td></td>
            </tr>
          </table>
