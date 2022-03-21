<!-- Author: Yeshwanth -->
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %> 
<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="java.time.LocalDate" %>


<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<c:set var = "displayLocalDate" value = "<%= LocalDate.now()%>" />
    
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_TERM_AND_CONDITIONS_MESSAGE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="termsAndConditionsMessage"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_CANCEL_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="cancelTitle"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_SUBMIT_TITLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="submitTitle"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_PUBLISH_CONFIRM%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="publishConfirm"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_ADD_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="addLabel"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_DOCUMENT_NAME_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="documentLabel"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_POSTPPTINDICATOR%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="pptIndicator"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_POSTPPT_YES_MESSAGE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="postPptYesMessage"/>


<script language="javascript" src="/assets/unmanaged/javascript/tooltip.js"></script> 


<script type="text/javascript">
			var submitTitle = '<content:getAttribute id="submitTitle" attribute="text" filter="true"/>';
			var cancelTitle= '<content:getAttribute id="cancelTitle" attribute="text" filter="true"/>';
			var publishConfirm='<content:getAttribute id="publishConfirm" attribute="text" filter="true"/>';
			var changeHappened = false;
			
			$(document).ready(function(){
			document.getElementById('showYesMsg').hidden = true;
				$("input").on("change",function(){
					changeHappened = true;
				});
				// Alert if thier is any change in the form
				$("a").not(".exempt").not('[href^="#"]').not('a:contains("Print report")').not('a:contains("Learning center")').on("click",function(e){
					e.preventDefault();
					changeHappened = checkDefault();
					if(changeHappened){
						var message = "The action you have selected will cause your changes to be lost.Select OK to continue or Cancel to return."; 
						if(confirm(message)){
							var link = $(this).attr('href');
							window.location.href = link;
						}
					}	
					else{
						var link = $(this).attr('href');
						window.location.href = link;
					}
				});

				function checkDefault(){
					var check = document.getElementById('documentSelect').value;
					if(check != ""){
						return true;
					}
					check = $("#documentName").val();
					if(check != ""){
						return true;
					}
					check = $('input[type=radio]:checked').val();
					if(check != "false"){
						return true;
					}
					return false;
				}
				document.getElementById('documentSelect').onchange = function () {
					var option_result = this.value;
					var option_array = option_result.split(/[\\/]/);
					var str = option_array[option_array.length - 1].split('.')[0];
					var substr=str.substring(0, 40); 
					if(str.length > 40 ){
						$("#documentName").val(substr);
						}
					else{
                        $("#documentName").val(str);
					}
					};
					$('input[type=radio][name="postToParticipantIndicator"]').change(function() {
						if($(this).val()=='true'){
		        		document.getElementById('showYesMsg').hidden = false;
		        		}else{
		        		document.getElementById('showYesMsg').hidden = true;
		        		}
    				});				
			});	
			
			function doConfirmAndSubmit(action) {
			    // only there is change, do confirm
				if(changeHappened){
					if(confirm(publishConfirm)){
						doSubmit(action);
					}
				}	
				else{
					doSubmit(action);
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
			   var submitprotect = false;
			   function doSubmit(action) {
				   if(submitprotect){
						return;
				   }
				   submitprotect = true;
			   	document.getElementsByName("action")[0].value= action;
			    document.addCustomplanDocumentForm.submit();
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
			
				<ps:form modelAttribute="addCustomplanDocumentForm" name="addCustomplanDocumentForm" action="/do/noticemanager/addcustomplandocument/" method="POST" enctype="multipart/form-data">
<%-- <form:hidden path="action" /> --%>
					
					<input value="save" type="hidden" name="action">
					<table border="0" cellspacing="0" cellpadding="0" width="500">
						<tbody>
							<tr>
								<td class="" colspan="6"><DIV id=errordivcs><content:errors scope="session"/></DIV><br></td>
								
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
								<td class="tableheadTD1" colspan="8"><b>Upload a custom file</b></td>
							</tr>
							<tr class="datacell1">
								<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td colspan="6">
									<table border="0" cellspacing="0" cellpadding="0" width="100%">
										<tbody>
											<tr>
												<td width="20"><img src="/assets/unmanaged/images/unmanaged/images/s.gif" width="1" height="1"></td>
												<td width="100"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
												<td width="130"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												<td width="250"><img src="/assets/unmanaged/images/s.gif" width="250" height="1"></td>
											</tr>
											<tr>
												<td class="datacell1" height="15" colspan="4"> </td>
											</tr>
											<tr>
												<td>&nbsp;</td>
												<td class="datacell1" valign="top">
												<strong><content:getAttribute beanName="addLabel" attribute="text"/></strong>
												</td>
												<td class="datacell1" height="15" valign="top" colspan="2">
													<span class="content"><input type="file" id="documentSelect" name="file" size="58" /></span></td>
													
											</tr>
											<tr>
												<td class="datacell1" height="15" colspan="4"> </td>
											</tr>
											<tr>
												<td>&nbsp;</td>
												<td class="datacell1" valign="top" colspan="3">
														<strong><content:getAttribute beanName="documentLabel" attribute="text"/></strong>
			                                            <span class="content"><input id="documentName" name="fileName" type="text" value="" size="40" maxlength="40"></span>										</td>
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
Yes<form:radiobutton path="postToParticipantIndicator" value="true"/>
No<form:radiobutton path="postToParticipantIndicator" value="false"/> 
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
												</td>
												
											</tr>
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
							
									
<td width="150"><input type="button" onclick="return doConfirmAndSubmit('cancel')" onmouseout="UnTip()" onmouseover="Tip(cancelTitle)" name="button" class="button134" id="cancel" value="cancel"/></td>

<td width="150"><input type="button" onclick="return doSubmit('submit');" onmouseout="UnTip()" onmouseover="Tip(submitTitle)" name="button" class="button134" id="save" value="save"/></td>

							</tr>
						</tbody>
					</table>
				</ps:form>
				</td>
		</tr>
	</tbody>
</table>