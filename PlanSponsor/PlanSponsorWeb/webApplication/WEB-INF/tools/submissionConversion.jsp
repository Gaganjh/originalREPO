<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>


<jsp:useBean id="submissionConversionFileForm" scope="session" type="com.manulife.pension.ps.web.tools.SubmissionConversionFileForm" />

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

<content:contentBean contentId="<%=ContentConstants.SUBMISSION_VALID_FILE_INFO%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidFileInfo"/>					
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_IN_PROGRESS%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidUploadInProgress"/>							
<content:contentBean contentId="<%=ContentConstants.CENSUS_NOT_ALLOWED_TO_UPLOAD%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageContractStatusNotAuth"/>

<content:contentBean contentId="<%=ContentConstants.CONVERSION_FILE_SIZE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="fileSizeMessage"/>
<content:contentBean contentId="<%=ContentConstants.CONVERSION_FILE_CONFIRMATION_MESSAGE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="fileSendConfirmationMessage"/>

<script type="text/javascript" > 
    function isFormChanged() {
	
		if (isFileSectionShown) {
			var uploadFormObj = getFormByName("submissionConversionFileForm");
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

	<% if (submissionConversionFileForm.isDisplayFileUploadSection()) { %>
		var isFileSectionShown=true;
	<% } else { %>
		var isFileSectionShown=false;
	<% } %>

	var uploadcredit=1;

	var errorMessage="";

	function resetValues(){
		writeError("");
		var uploadFormObj = getFormByName("submissionConversionFileForm");
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

			var uploadFormObj = getFormByName("submissionConversionFileForm");

			var filename = uploadFormObj.uploadFile.value;
			if (isNullInput(filename)==true) {
				writeError('<content:getAttribute beanName="messageValidFileInfo" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_VALID_FILE_INFO%> + ']');
				//alert("Please select a file");
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
	//	alert('Confirm');

		var uploadFormObj = getFormByName("submissionConversionFileForm");

		if (uploadcredit != 1 ) {
			//alert('uploadcredit');
			writeError("");
			hideInProgressDivision();
			writeError("<content:getAttribute beanName="messageValidUploadInProgress" attribute="text"/>"+ "&nbsp;[" + <%=ContentConstants.SUBMISSION_UPLOAD_IN_PROGRESS%> + "]" +"<br>If you have trouble with your upload click <a href=\"/do/tools/uploadConversionFile/\">here</a> to start over.");
		   	return false;
	 	} else if (validateInputs() == true) {
		   	var message;
			var filetype;
			message = "";
			//alert('Confirm - valid 1');
			var filename = null;
			if (isFileSectionShown) {
				filename = uploadFormObj.uploadFile.value;
				var tempmessage = "<content:getAttribute beanName="fileSendConfirmationMessage" attribute="text"/>";
				if (isNullInput(filename)==false) {
					filetype  = filename.substring(filename.indexOf('.'),filename.length);
					tempmessage = tempmessage.replace("{0}",filetype);
					tempmessage = tempmessage.replace("{1}",filename);
					message = tempmessage;
				}
			}

			//alert('Confirm - valid 2');
         	message = message +"?" +"\n*PLEASE NOTE: By clicking OK, you are also confirming that you understand\nthat no processing will occur to the Conversion file you are sending.";

		   	if (confirm(message)) {
				uploadcredit = 0;
				writeError("");
				showInProgressDivision();
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
    loadingPanel.setBody("<span style='padding-left:10px;float:right;padding-right:15px;padding-top:10px;padding-botton:5px;text-align:centre;font-size:1.1em'>One moment please...</span><img style='padding-bottom:12px;padding-left:0px;align:centre' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
    loadingPanel.render(document.body);
    loadingPanel.show();  
	}
	function hideInProgressDivision() {
		loadingPanel.hide();
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

<div id="Image_0">
  <div class="bd" id="Image_0_BD" style="position:relative; background-color:rgb(255,255,255);">
</div>
</div>

	<A NAME="TopOfPage"></A>&nbsp;
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">
			<tr>
				<td>
					<DIV id="inProgressDivision" style="display: none;";>
						<TABLE BORDER=0 CELLPADDING=1 CELLSPACING=0>
							<TR><TD ALIGN="left"><b>Upload in progress.</b></TD>
							</TR>
							<TR><TD ALIGN="left"><content:getAttribute beanName="messageValidUploadInProgress" attribute="text"/>&nbsp;[<%=ContentConstants.SUBMISSION_UPLOAD_IN_PROGRESS%>]
							<br>If you have trouble with your upload click <a href="/tools/uploadConversionFile/">here</a> to start over.
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
    	 		   <ps:form action="/do/tools/uploadConversionFile/" modelAttribute="submissionConversionFileForm" name="submissionConversionFileForm" enctype="multipart/form-data" method="POST" onsubmit="return confirmSend();">

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
                      <td class="tableheadTD1" colspan="8"> <b><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></b></td>
                    </tr>
                    <tr class="datacell1">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td colspan="6" rowspan="13" >
                      <table width="516" border="0" cellpadding="0" cellspacing="0">
                           <tr>
						       <td>&nbsp;</td>
						          <td class="datacell1" valign="top"><span class="content">
						        	  <table celssapcing="1" cellpadding="1" width="500" border="0">
							             <tbody>
										    <tr>
                                                <td class="datacell1"><b>Contract <br>number&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>
                                                   <strong class="highlight">
${userProfile.currentContract.contractNumber}
${userProfile.currentContract.companyName}
												   </strong>
												</td>
                                                <td>&nbsp;</td>											
											</tr>
								         </tbody>
									   </table>	 
							  	   </td> 
								</td>  
                          </tr>
                          <tr>
                            <td colspan="3" width="100%" align="right" class="datacell1">
									&nbsp;
							</td>
                          </tr>

						  <% if (submissionConversionFileForm.isDisplayFileUploadSection()) { %>
	                         <tr>
		                        <td>&nbsp;</td>
        	                    <td class="datacell1" valign="top"><span class="content">
            	                	<table celssapcing="1" cellpadding="1" width="500" border="0">
                	            	<tbody>
                    	        		<tr>
                        	    			<td width="100" align="left" valign="top" class="datacell1"><b>File <br>information</b></td>
                            				<td width="300" align="left" class="datacell1">
<textarea name="fileInformation"  cols="55" rows="3"></textarea>
											</td>
											<td width="100" align="left" class="datacell1">
												&nbsp;
											</td>
										</tr>
										<tr>
										     <td width="100 align="left" valign="top" class="datacell1"/>
										     <td colspan="2" align="left" class="datacell1">
											 <span class="content"><content:getAttribute beanName="layoutPageBean" attribute="body1"/>
                                             </td>
										</tr>
									</tbody>
									</table>	
								</td>
                        	 </tr>

							 <tr>
							  	<td>&nbsp;</td>
								<td class="datacell1" valign="top"><span class="content">
								    <table celssapcing="1" cellpadding="1" width="500" border="0">
									    <tbody>
										    <tr>
											    <td width="100" align="left" valign="top" class="datacell1"><b>File name</b></td>
												<td width="300" align="left" class="datacell1" height="15" colspan="2" celspan="2">
												    <span class="content">
												        <input type="file" name="uploadFile" size="55"/>
														<br/><content:getAttribute beanName="fileSizeMessage" attribute="text"/>
											 		</span>	 
												</td>
												<td width="100" align="left" class="datacell1">
 	     											&nbsp;
    											</td>
											</tr>


										</tbody>
									</table>	
								</td>	
							 </tr>
						  
						  <%} else {%>
						  	<tr>
						  		<td>&nbsp;</td>
						  		<td class="datacell1"><span class="content">
						  			You are not authorized to make submissions.
								</span></td>
								<td>&nbsp;</td>
							</tr>
						  <%}%>

                          <tr>
                            <td colspan="3" >&nbsp;</td>
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
                <% if ( submissionConversionFileForm.isDisplayFileUploadSection() ) { %>
					<br/><br/>
					<table width="455" border="0" cellpadding="0" cellspacing="0">
						<tbody>
						  <tr>
							<td width="143">&nbsp;</td>
							<td width="166" title="<content:getAttribute beanName="resetButtonDescription" attribute="text"/>" ><span class="content">
								<input type="button" class="button134" onclick="resetValues();" value="clear"  />
							</span></td>
							<td width="146" title="<content:getAttribute beanName="submitButtonDescription" attribute="text"/>" ><span class="content">
<input type="submit" class="button134" value="submit" name="actionLabel" />
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
