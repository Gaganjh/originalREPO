<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>       
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %> 
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.util.SessionHelper"%>
<%@ page import="java.time.LocalDate" %>



<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<c:set var = "displayLocalDate" value = "<%= LocalDate.now()%>" />

<script type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>       

<content:contentBean contentId="<%=ContentConstants.NMC_EDIT_PAGE_TERM_AND_CONDITIONS_MESSAGE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="termsAndConditionsMessage"/>
<content:contentBean contentId="<%=ContentConstants.NMC_EDIT_PAGE_CANCEL_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="cancelTitle"/>
<content:contentBean contentId="<%=ContentConstants.NMC_EDIT_PAGE_SAVE_TITLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="saveTitle"/>
<content:contentBean contentId="<%=ContentConstants.NMC_PUBLISH_CONFIRM%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="publishConfirm"/>   
<content:contentBean contentId="<%=ContentConstants.NMC_EDIT_DOCUMENT_NAME_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="documentname"/>
<content:contentBean contentId="<%=ContentConstants.NMC_EDIT_REPLACE_FILE_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="replacefile"/>
<content:contentBean contentId="<%=ContentConstants.NMC_EDIT_POSTPPTINDICATOR%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="pptIndicator"/>
<content:contentBean contentId="<%=ContentConstants.NMC_EDIT_DISPLAY_TITLE%>" type="<%=ContentConstants.TYPE_LAYOUT_PAGE %>" id="displayTitle"/>
<content:contentBean contentId="<%=ContentConstants.NMC_EDIT_POSTPPT_YES_MESSAGE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="postPptYesMessage"/>
<content:contentBean contentId="<%=ContentConstants.NMC_EDIT_POSTPPT_YES_TO_NO_MESSAGE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="postPptYesToNoMessage"/>

	<script type="text/javascript">
				var saveTitle = '<content:getAttribute id="saveTitle" attribute="text" filter="true"/>';
				var cancelTitle= '<content:getAttribute id="cancelTitle" attribute="text" filter="true"/>';
				var publishConfirm='<content:getAttribute id="publishConfirm" attribute="text" filter="true"/>';
				var changeHappened = false;
				var existingDocName = "";
				var existingpptInd = "";
				var intervalId;
				var link = "";
				var utilities = {
				       // Asynchronous request call to the server. 
				      doAsyncRequest : function(actionPath, callbackFunction) {
				      YAHOO.util.Connect.setForm(document.editCustomPlanDocumentForm);
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
				
				$(document).ready(function(){
					$("input").on("change",function(){
						changeHappened = true;
					});
					
					existingDocName = $("input[name=documentName]").val();
					existingpptInd = $('input[type=radio]:checked').val();
					$('input[type=radio][name="postToPptIndicator"]').change(function() {
						if($(this).val()=='yes'){
		        		document.getElementById('showYesMsg').hidden = false;
		        		}else{
		        		var isCrossedTwelveMonthRule = "${editCustomPlanDocumentForm.crossedTwelveMonthRule}";
		        		var postToPptYesToNoConfirm='<content:getAttribute id="postPptYesToNoMessage" attribute="text" filter="true"><content:param>${editCustomPlanDocumentForm.docAvailableUntilDate}</content:param></content:getAttribute>';
		        	    			if(existingpptInd=='yes' && isCrossedTwelveMonthRule =='false'){
		        					if(confirm(postToPptYesToNoConfirm)){
		        					document.getElementById('showYesMsg').hidden = true;
		        					}else{
		        					  $("#postToPptIndicatorYes").prop("checked", true);
		        		 			document.getElementById('showYesMsg').hidden = false;
		        					}
		        			}		        			
		        		}
    				});		
					if(existingpptInd=='yes'){
					document.getElementById('showYesMsg').hidden = false;
					}else{
					document.getElementById('showYesMsg').hidden = true;
					}
					$("a").not(".exempt").not('[href^="#"]').not('a:contains("Print report")').not('a:contains("Learning center")').on("click",function(e){
						e.preventDefault();
						changeHappened = checkDefault();
						link = $(this).attr('href');
						if(changeHappened){
							var message = "The action you have selected will cause your changes to be lost.Select OK to continue or Cancel to return."; 
							if(confirm(message)){
								utilities.doAsyncRequest("/do/noticemanager/editCustomPlanDocument/?action=releaseLock",callback_LockRelease);
							}
						}else{
							utilities.doAsyncRequest("/do/noticemanager/editCustomPlanDocument/?action=releaseLock",callback_LockRelease);
						}
					});

					$('.input').on("change",function(event){
						changeHappened = checkDefault();
						if(changeHappened){
							//$('#save').removeAttr('disabled');
							$('.twodisabled').hide();$('#saveDisabled').addClass("enabledButton");
							$("#saveDisabled").css("cursor", "pointer");
							saveEnabled = true;
						}else{
							//$('#save').attr('disabled', 'true');
							saveEnabled = false;
							$("#saveDisabled").css("cursor", "default");
						}
					});

					// To check on load
					changeHappened = checkDefault();
					if(changeHappened){
						//$('#save').removeAttr('disabled');
						$('.twodisabled').hide();$('#saveDisabled').addClass("enabledButton");
						$("#saveDisabled").css("cursor", "pointer");
						saveEnabled = true;
					}else{
						//$('#save').attr('disabled', 'true');
						saveEnabled = false;
						$("#saveDisabled").css("cursor", "default");
					}
					
					function checkDefault(){
						var check = document.getElementById('documentSelect').value;
						if(check != ""){
							return true;
						}
						check = $("#documentName").val();
						if(check != existingDocName){
							return true;
						}
						check = $('input[type=radio]:checked').val();
						if(check != existingpptInd){
							return true;
						}
						return false;
					}
				});	
				
				var callback_LockRelease = {
						success:  function(o) { 
						window.location.href =  link;
						},
					    cache : false,
					    failure : utilities.handleFailure
					}
				
				function doConfirmAndSubmit(action) {
				    // only there is change, do confirm
					if(changeHappened){
						if(confirm(publishConfirm)){
						 	document.getElementsByName("action")[0].value= action;
						   	document.editCustomPlanDocumentForm.submit();
						}
					}	
					else{
					 	document.getElementsByName("action")[0].value= action;
					   	document.editCustomPlanDocumentForm.submit();
					}
				}
				
				   function doNext() {		
					   		document.getElementsByName("action")[0].value="validate";
							document.addCustomplanDocumentForm.submit();					
					}
				   /**
				    * Method to set the action in the form and submit the from.
				    * Called when the user clicks the button	
				    */
					var saveEnabled = false;
				   function doSubmit(action) {
					if(!saveEnabled) return;
					   //alert("Action"+action);
				   	document.getElementsByName("action")[0].value= action;
				   	document.editCustomPlanDocumentForm.submit();
				   }
				   
				   /**
				   * Method to set the pop up window for unsaved data.
				   * Called when the user clicks other than 	
				   */
			   
			   </script>
            
            <table border="0" cellspacing="0" cellpadding="0" width="760">
	         <tbody>
		         <tr>
			        <td width="30">&nbsp;</td>
			          <td width="730">
			
			   <ps:form modelAttribute="editCustomPlanDocumentForm" name="editCustomPlanDocumentForm" action="/do/noticemanager/editCustomPlanDocument/" method="POST"  enctype="multipart/form-data" >
<form:hidden path = "action" />
<form:hidden path = "documentId" />
					<table border="0" cellspacing="0" cellpadding="0" width="500">
						<tbody>
						<tr>
								<td  colspan="6"><DIV id=errordivcs><content:errors scope="session"/></DIV><br></td>
								
						    </tr>
						
							<tr>
								<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td width="113"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
								<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td width="463"><img src="/assets/unmanaged/images/s.gif" width="250" height="1"></td>
								<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td width="113"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
								<td width="7"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
								<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>
							<tr class="tablehead" height="25">
								<td class="tableheadTD1" colspan="8">
								<strong><content:getAttribute  beanName="displayTitle" attribute="body1Header">
<content:param>${editCustomPlanDocumentForm.documentName} </content:param>
									</content:getAttribute>
									</strong>
							</tr>
							<tr class="datacell1">
								<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td colspan="6">
									<table border="0" cellspacing="0" cellpadding="0" width="100%">
										<tbody>
											<tr>
											<td width="20"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												<td width="100"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
												<td width="130"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												<td width="250"><img src="/assets/unmanaged/images/s.gif" width="250" height="1"></td>
											</tr>
											<tr>
												<td class="datacell1" height="15" colspan="4"> </td>
											</tr>
											
											<tr>											
												<td>&nbsp;</td>
												<td class="datacell1" valign="top" colspan="3">
													<strong><content:getAttribute beanName="documentname" attribute="text"/></strong>
												    <span class="content" id="display">
												       <form:input path="documentName" maxlength="40" size="40" cssClass="input" id="documentName"/>
												    </span>
												</td>	
											</tr>
											<tr>
												<td class="datacell1" height="15" colspan="4"> </td>
											</tr>
											
											<tr>
												<td>&nbsp;</td>
												<td class="datacell1" valign="top" >
													<strong><content:getAttribute beanName="replacefile" attribute="text"/></strong>
												</td>
												<td class="datacell1" height="15" valign="top" colspan="4" >
													<span class="content"><input type="file" id="documentSelect" name="documentFileName" size="40"  styleClass="input" /></span>
													</td>
													</tr>
											<tr>
												<td class="datacell1" height="15" colspan="4"> </td>
											</tr>
											<tr>
												<td>&nbsp;</td>
												<td class="datacell1" valign="top" colspan="2">
													<strong><content:getAttribute beanName="pptIndicator" attribute="text"/></strong>
												</td>
												<td class="datacell1" height="15" valign="top">
													<span class="content">																								
Yes<form:radiobutton path="postToPptIndicator" id="postToPptIndicatorYes" cssClass="input" value="yes"/>
No<form:radiobutton path="postToPptIndicator" id="postToPptIndicatorNo" cssClass="input" value="no"/>
														<div id="showYesMsg">
														<content:getAttribute id="postPptYesMessage" attribute="text">
														<content:param>	
														<fmt:parseDate value="${displayLocalDate}" pattern="yyyy-MM-dd" var="displayDate" type="date"/>
														<fmt:formatDate pattern="MMM YYYY" value="${displayDate}" var="showCurrentDate"/>
														<c:out value="${showCurrentDate}"/>
														</content:param>
														</content:getAttribute>
														</div>
												</span>
													</td></tr>
											<tr>
												<td class="datacell1" height="15" colspan="4"> </td>
											</tr>						
											
										</tbody>
									</table>
								</td>
								<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>	
							<tr class="datacell1">
								<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
								<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td class="lastrow" valign="bottom" rowspan="2" colspan="2" align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
							</tr>
							<tr>
								<td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>
						</tbody>
					</table>
					<br>					
					<table border="0" cellspacing="0" cellpadding="0" width="500">
						<tbody>
							<tr>
								<td><content:getAttribute beanName="termsAndConditionsMessage" attribute="text"/></td>
							</tr>
						</tbody>
					</table>
					<br>				
					<table border="0" cellspacing="0" cellpadding="0" width="500">
						<tbody>
							<tr>
								<td width="200">&nbsp;</td>
								<td width="150">
								<div id="" style="postion:relative;" class="enabledButton"
									 onclick="return doConfirmAndSubmit('cancel')" onmouseover="Tip(cancelTitle)" onmouseout="UnTip('')">
									<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >cancel</p></h5>
								</div>
								<td width="150">
								<div id="saveDisabled" style="postion:relative;"  
									onclick="return doSubmit('save')" onmouseover="Tip(saveTitle)" onmouseout="UnTip('')">
									<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >save</p><p class="buttonText twodisabled">save</p></h5>
								</div>
							</tr>
						</tbody>
					</table>
				</ps:form>
				</td>
		</tr>
	</tbody>
</table>
<style>
#save {
	display: none;
}
</style>
<style>
		.buttonDisable {
			background-image: url("/assets/unmanaged/images/134_button.gif");
			width: 134px;
			height: 25px;
			margin-top: 16px;
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
