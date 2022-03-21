<%@page import="com.manulife.pension.ps.web.investment.IPSAndReviewForm"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- Imports --%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.manulife.pension.platform.web.investment.valueobject.IPSReviewReportDetailsVO" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.platform.web.investment.IPSAndReviewDetailsForm" %>
<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="constants"
	className="com.manulife.pension.ps.web.Constants" />

<script language="JavaScript" type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript" >

/* function isFormChanged() {
	  return changeTracker.hasChanged();
	}

	registerTrackChangesFunction(isFormChanged); */

function limitText(limitField, limitCount, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} else {
		limitCount.value = limitNum - limitField.value.length;
	}
}

function doPrintPDF() {
	var reportURL = new URL();
	reportURL.setParameter("action", "printInterimReport");
	window.location= "/do/investment/ipsManager/?action=printInterimReport";
}

function openEditCriteriaAndWeightingsPage() {		

	var serviceDateChangeNotAvailableText = '${serviceDateChangeNotAvailableText}';
	
	if(serviceDateChangeNotAvailableText != '') {		
		alert(serviceDateChangeNotAvailableText);
	} else {	
		window.location= "/do/investment/ipsManager/?action=edit";
	}
	
}

</script>
	
<content:contentBean
	contentId="${contentConstants.SCHEDULED_ANNUAL_REVIEW_DATE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="scheduledAnnualReviewDateText" />
<content:contentBean
	contentId="${contentConstants.IPS_ASSIST_SERVICE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="ipsAssistServiceText" />
<content:contentBean
	contentId="${contentConstants.CHANGE_CRITERIA_WEIGHTING_LINK}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="changeCriteriaAndWeightingLink" />
<content:contentBean
	contentId="${contentConstants.IPSR_ALT_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="altText" />
<content:contentBean
	contentId="${contentConstants.SAVE_CONFIRMATION_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="saveConfirmationText" />
<content:contentBean
	contentId="${contentConstants.SERVICE_REVIEW_DATE_DETAILS_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="serviceReviewDateDetailsText" />
<content:contentBean
	contentId="${contentConstants.IPS_REVIEW_REPORTS_TITLE}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsReviewReportsTitle" />
<content:contentBean
	contentId="${contentConstants.VIEW_RESULTS_ICON_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="viewResultsIconText" />
<content:contentBean
	contentId="${contentConstants.EDIT_RESULTS_ICON_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="editResultsIconText" />
<content:contentBean
	contentId="${contentConstants.CANCEL_RESULTS_ICON_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="cancelResultsIconText" />
<content:contentBean
	contentId="${contentConstants.IPS_CURRENT_REPORT_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsCurrentReportText" />
<content:contentBean
	contentId="${contentConstants.NO_PREVIOUS_IPS_REPORT_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="noPreviousIPSReportText" />
<content:contentBean
	contentId="${contentConstants.IPS_ADHOC_REPORT_LINK}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsAdhocReportLink" />
<content:contentBean
	contentId="${contentConstants.IPS_NEW_ANNUAL_REVIEW_DATE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsNewAnnualReviewDateText" />
<content:contentBean
	contentId="${contentConstants.IPS_NEW_ANNUAL_REVIEW_DATE_SAVE_CONFIRMATION_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsNewAnnualReviewDateSaveConfirmationText" />
<content:contentBean
	contentId="${contentConstants.IPS_NEW_ANNUAL_REVIEW_DATE_NO_STATUS_CHANGE_SAVE_CONFIRMATION_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsNewAnnualReviewDateNoStatusChangeSaveConfirationText" />
<content:contentBean
	contentId="${contentConstants.IPS_EXTERNAL_NEW_ANNUAL_REVIEW_DATE_SAVE_CONFIRMATION_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsExternalNewAnnualReviewDateSaveConfirmationText" />
<content:contentBean
	contentId="${contentConstants.IPS_EXTERNAL_NEW_ANNUAL_REVIEW_DATE_NO_STATUS_CHANGE_SAVE_CONFIRMATION_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsExternalNewAnnualReviewDateNoStatusChangeSaveConfirationText" />
<content:contentBean
	contentId="${contentConstants.IPS_NO_FUND_MATCHING_TRESHOLD_ICON_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="noFundsMatchingTreshold" />	
	
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%
	IPSAndReviewForm ipsAndReviewDetailsForm =(IPSAndReviewForm)request.getAttribute("iPSAndReviewForm");
	pageContext.setAttribute("ipsAndReviewDetailsForm",ipsAndReviewDetailsForm,PageContext.PAGE_SCOPE); 
%>
<% 
	String newAnnualReviewMonthMap=(String)request.getAttribute("newAnnualReviewMonthMap");
	pageContext.setAttribute("newAnnualReviewMonthMap", newAnnualReviewMonthMap, PageContext.PAGE_SCOPE);
	String newAnnualReviewDateMap=(String)request.getAttribute("newAnnualReviewDateMap");
	pageContext.setAttribute("newAnnualReviewDateMap", newAnnualReviewDateMap, PageContext.PAGE_SCOPE);
	
	String ipsSuccessInd=(String)request.getAttribute("SUCCESS_IND");
	pageContext.setAttribute("ipsSuccessInd", ipsSuccessInd, PageContext.PAGE_SCOPE);
	
	String criteriaDescMap=(String)request.getAttribute("criteriaDescMap");
	pageContext.setAttribute("criteriaDescMap", criteriaDescMap, PageContext.PAGE_SCOPE); 
%>

<div id="ips_body">
<c:if test="${ipsSuccessInd}">
	<table id="psErrors"><tbody>
		<tr>
			<td class="highlightBold"><content:getAttribute attribute="text" beanName="saveConfirmationText" />			
			</td>
		</tr>
	</tbody></table>
</c:if>
<div id="participantNotificationSection" style="display:none">
 <ps:form method="POST" modelAttribute="ipsViewParticiapantNotificationForm" name="ipsViewParticiapantNotificationForm" action="/do/investment/viewParticipantNotification/">
		<table border="0" cellspacing="0" cellpadding="0" width="700">
			<tr>			
				<td width="350">
					<img src="/assets/unmanaged/images/s.gif" width="300"
							height="1">
				</td>
				<td width="350">
					<div class="modal_dialog" id="additionalParamSection" style="display:none;">
    										</div>
				</td>
			</tr>
		</table>
	
	</ps:form>
</div>
<ps:form method="POST" modelAttribute="iPSAndReviewForm" name="iPSAndReviewForm" action="/do/investment/editIPSManager/">
<input type="hidden" name="action" /><%--  input - name="ipsAndReviewDetailsForm" --%>
<input type="hidden" name="formChanged" /><%--  input - name="ipsAndReviewDetailsForm" --%>
<input type="hidden" name="totalWeighting" /><%--  input - name="ipsAndReviewDetailsForm" --%>
<input type="hidden" name="dateChanged" /><%--  input - name="ipsAndReviewDetailsForm" --%>
<input type="hidden" name="criteriaChanged" /><%--  input - name="ipsAndReviewDetailsForm" --%>
<input type="hidden" name="newServiceDateConfirmationText" /><%--  input - name="ipsAndReviewDetailsForm" --%>
	
	
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100" rowspan="2"><img
				src="/assets/unmanaged/images/spacer.gif" width="15" height="1"
				border="0"></td>
			<td valign="top" class="greyText" width="500">
				<content:errors scope="session" />
				<div id="messages"></div>
				<br>
			</td>
			<td width="100" rowspan="2"><img
				src="/assets/unmanaged/images/spacer.gif" width="15" height="1"
				border="0"></td>
		</tr>
	</table>
	<table width="700" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
			<td width="380"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
			<td width="409"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
		</tr>
		<tr>
			<td colspan="4" class="tableheadTD1" height="20"><Strong><content:getAttribute attribute="subHeader" beanName="layoutPageBean"/></Strong>
			<render:date property="userProfile.currentContract.contractDates.asOfDate" patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue = "" /></td>
			<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		</tr>
		<tr>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				height="1" width="1" /></td>
	
			<td class="tablesubhead" height="20"><Strong><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></Strong></td>
			<td class="databorder" width="1"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			<td class="tablesubhead" height="20"><Strong><content:getAttribute attribute="body2Header" beanName="layoutPageBean"/></Strong></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				height="1" width="1" /></td>
			<td class="datacell1" height="20"></td>
			<td class="databorder" width="1"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			<td class="datacell1" height="20"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr>
			<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
			<td class="datacell1" valign="top" width="45%">
			<table border="0" cellpadding="5" cellspacing="5" width="100%">
				<tbody valign="top">
					<tr valign="top">
						<td class="datacell1" colspan="2" valign="top"><content:getAttribute
							attribute="text" beanName="ipsAssistServiceText">
							<c:choose>
								<c:when test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable}">
									<content:param>
										<span class="highlightBold">Active</span>
									</content:param>
								</c:when>
								<c:otherwise>
									<content:param>
										<span class="highlightBold">Not active</span>
									</content:param>
								</c:otherwise>
							</c:choose>
						</content:getAttribute><br>
						</td>
					</tr>
					<tr>
						<td class="datacell1" colspan="2" valign="top">
							<content:getAttribute
								attribute="text" beanName="scheduledAnnualReviewDateText">
							<c:choose>
								<c:when test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable}">
									<content:param>
<span class="highlightBold">${ipsAndReviewDetailsForm.annualReviewDate}</span>

									</content:param>
								</c:when>
								<c:otherwise>
									<content:param>
										<span class="highlightBold">n/a</span>
									</content:param>
								</c:otherwise>
							</c:choose>
							</content:getAttribute>
							<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable && ipsAndReviewDetailsForm.editLinkAccessible}">
								<c:if test="${'viewMode'==ipsAndReviewDetailsForm.mode}">
									<img src="/assets/unmanaged/images/s.gif" width="1"
										height="1">
									<c:if test="${empty param.printFriendly }" >
										<a href="javascript:openEditCriteriaAndWeightingsPage();" >
											Change</a>
									</c:if>
								</c:if>
							</c:if>
						</td>
					</tr>
					<tr>
						<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
							height="1"></td>
					</tr>		
					<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable}">
						<tr>					
							<td class="datacell1" colspan="3" valign="bottom" style="padding-left: 15px;">
								<content:getAttribute attribute="text" beanName="serviceReviewDateDetailsText" />
							</td>
						</tr>
					</c:if>
					<tr>
						<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
							height="15"></td>
					</tr>
					<tr>
						<td class="datacell1" colspan="3" valign="top">
							<c:if test="${empty param.printFriendly}" > 
								<c:if test="${'editMode'==ipsAndReviewDetailsForm.mode && ipsAndReviewDetailsForm.newAnnualReviewDateAvailable}">
								 <c:if test="${'editMode'==ipsAndReviewDetailsForm.mode}">
									<b><content:getAttribute attribute="text" beanName="ipsNewAnnualReviewDateText" /></b>
									

						
<form:select path="newAnnualReviewMonth" cssClass="greyText" onchange="serviceDateChanged()" size="0" >


									<form:options items="${newAnnualReviewMonthMap}" 
									/> 
											<%-- <c:if test="${ipsAndReviewDetailsForm.newAnnualReviewMonth != '' && ipsAndReviewDetailsForm.newAnnualReviewMonth != null}">
												<form:option value="${ipsAndReviewDetailsForm.newAnnualReviewMonth}">${ipsAndReviewDetailsForm.newAnnualReviewMonth}</form:option>
											</c:if>
											<form:options items="${newAnnualReviewMonthMap}"></form:options> --%>
										</form:select>
									 <ps:trackChanges name="ipsAndReviewDetailsForm" escape="true" property="newAnnualReviewMonth"  />  								
									
<form:select path="newAnnualReviewDate" cssClass="greyText" onchange="serviceDateChanged()" size="0" >


										<form:options items="${newAnnualReviewDateMap}" 
 											/> 
											<%-- <c:if test="${ipsAndReviewDetailsForm.newAnnualReviewDate != '' && ipsAndReviewDetailsForm.newAnnualReviewDate != null}">
												<form:option value="${ipsAndReviewDetailsForm.newAnnualReviewDate}">${ipsAndReviewDetailsForm.newAnnualReviewDate}</form:option>
											</c:if>
											<form:options items="${newAnnualReviewDateMap}"></form:options> --%>
									</form:select>
									</c:if>
							</c:if>
							</c:if>
							<c:if test="${not empty param.printFriendly}" >
								<c:if test="${'editMode'==ipsAndReviewDetailsForm.mode && ipsAndReviewDetailsForm.newAnnualReviewDateAvailable}">
									<b><content:getAttribute attribute="text" beanName="ipsNewAnnualReviewDateText" /></b>
									
<form:select path="newAnnualReviewMonth" cssClass="greyText" disabled="true" onchange="serviceDateChanged()" size="0" >


										<form:options items="${newAnnualReviewMonthMap}"  
											/> 
									<%-- <c:if test="${ipsAndReviewDetailsForm.newAnnualReviewMonth != '' && ipsAndReviewDetailsForm.newAnnualReviewMonth != null}">
												<form:option value="${ipsAndReviewDetailsForm.newAnnualReviewMonth}">${ipsAndReviewDetailsForm.newAnnualReviewMonth}</form:option>
											</c:if>
											<form:options items="${newAnnualReviewMonthMap}"></form:options> --%>
											</form:select>
									<ps:trackChanges name="ipsAndReviewDetailsForm" escape="true" property="newAnnualReviewMonth"  />  								
									
<form:select path="newAnnualReviewDate" cssClass="greyText" disabled="true" onchange="serviceDateChanged()" size="0" >


										<form:options items="${newAnnualReviewDateMap}"  
											/> 
								<%-- <c:if test="${ipsAndReviewDetailsForm.newAnnualReviewDate != '' && ipsAndReviewDetailsForm.newAnnualReviewDate != null}">
												<form:option value="${ipsAndReviewDetailsForm.newAnnualReviewDate}">${ipsAndReviewDetailsForm.newAnnualReviewDate}</form:option>
											</c:if>
											<form:options items="${newAnnualReviewDateMap}"></form:options> --%>
</form:select>
									 <ps:trackChanges name="ipsAndReviewDetailsForm" escape="true" property="newAnnualReviewDate"  />  
								</c:if>
							</c:if>
						</td>
					</tr>
					<c:if test="${'editMode'==ipsAndReviewDetailsForm.mode && ipsAndReviewDetailsForm.newAnnualReviewDateAvailable}">
						<tr>
							<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
								height="5"></td>
						</tr>
					</c:if>
				</tbody>
			</table>
			</td>
			<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
			<td class="datacell1" valign = "top">
				<c:choose>
					<c:when test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable}">
						<c:if test="${'viewMode'==ipsAndReviewDetailsForm.mode}">
							<jsp:include page="viewIPSCriteriaAndWeightings.jsp" flush="true" />
						</c:if>
						<c:if test="${'editMode'==ipsAndReviewDetailsForm.mode}">
							<jsp:include page="editIPSCriteriaAndWeightings.jsp" flush="true" />
						</c:if>
					</c:when>
					<c:otherwise>
						<table border="0" cellpadding="5"  width="100%">	
							<tr valign="top">
								<td>
									<content:getAttribute attribute="text" beanName="altText" />
								</td>
							</tr>
						</table>
					</c:otherwise>
				</c:choose>
			</td>	
			<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
		</tr>
		<tr>
			<td colspan="5" width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		</tr>
		
	</table>
	
	<c:if test="${'editMode'==ipsAndReviewDetailsForm.mode}">
	
		<c:if test="${empty param.printFriendly }" >
			<table border="0" cellspacing="0" cellpadding="0" width="700">
				<tr>
					<td align="right" valign="top" style="padding-top: 10px;">
						<input type="button"
						name="button1" value="cancel" class="button134"
						style="font-size: 12px; width: 60px;"
						onclick="return doCancel('cancel')" /> &nbsp; 
						<c:choose>
							<c:when test="${ipsAndReviewDetailsForm.initialNotificationSent}">
								<ps:isInternalUser name="userProfile" property="role">
									<input type="button" name="button3" value="save" class="button134"
									style="font-size: 12px; width: 60px;"
									onclick="return doSubmit('save','ipsNewAnnualReviewDateSaveConfirmationText')"
									onmouseover="Tip('<content:getAttribute attribute="text" beanName="saveButtonHoverOverText"/>')"
									onmouseout="UnTip()" />
								</ps:isInternalUser>
								<ps:isExternal name="userProfile" property="role">
									<input type="button" name="button3" value="save" class="button134"
									style="font-size: 12px; width: 60px;"
									onclick="return doSubmit('save','ipsExternalNewAnnualReviewDateSaveConfirmationText')" />
								</ps:isExternal>
							</c:when>
							<c:otherwise>
								<ps:isInternalUser name="userProfile" property="role">
									<input type="button" name="button3" value="save" class="button134"
									style="font-size: 12px; width: 60px;"
									onclick="return doSubmit('save','ipsNewAnnualReviewDateNoStatusChangeSaveConfirationText')"
									onmouseover="Tip('<content:getAttribute attribute="text" beanName="saveButtonHoverOverText"/>')"
									onmouseout="UnTip()" />
								</ps:isInternalUser>
								<ps:isExternal name="userProfile" property="role">
									<input type="button" name="button3" value="save" class="button134"
									style="font-size: 12px; width: 60px;"
									onclick="return doSubmit('save','ipsExternalNewAnnualReviewDateNoStatusChangeSaveConfirationText')" />
								</ps:isExternal>
							</c:otherwise>
						</c:choose>
						
					</td>
				</tr>
			</table>
		</c:if>
	</c:if>
 	<c:if test="${'viewMode'==ipsAndReviewDetailsForm.mode}"> 
		<br>
		<% int numberOfColumns = 0; %>
		<c:if test="${empty param.printFriendly }" >
			<% numberOfColumns = 9; %>
		</c:if>
		<c:if test="${not empty param.printFriendly }" >
			<% numberOfColumns = 7; %>
		</c:if>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<c:if test="${empty param.printFriendly }" >
						<table border="0" width="100%">
							<tr>
								<td align="left">
						        	  <strong>Legend</strong>:&nbsp;<img 
						        	  src="/assets/unmanaged/images/view_icon.gif" width="12" height="12"
						        	  />&nbsp;<font color="#990000"><content:getAttribute attribute="text" beanName="viewResultsIconText" /></font> &nbsp;<img 
						        	  src="/assets/unmanaged/images/edit_icon.gif" width="12" height="12"
						        	  />&nbsp;<font color="#990000"><content:getAttribute attribute="text" beanName="editResultsIconText" /></font> &nbsp;<img 
						        	  src="/assets/unmanaged/images/cancel_icon.gif" width="12" height="12"
						        	  />&nbsp;<font color="#990000"><content:getAttribute attribute="text" beanName="cancelResultsIconText" /></font>
								</td>
							</tr>
						</table>
					</c:if>
					<table width="700" border="0" cellspacing="0" cellpadding="0">
						<!-- Start of body title -->
				        <tr class="tablehead">
				        	<td class="tableheadTD1" colspan="<%=numberOfColumns%>" height="20">         
				            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				            	<tr>
						          <td class="tableheadTD" width="50%">
						          <b><content:getAttribute attribute="text" beanName="ipsReviewReportsTitle" /></b>
						          </td>		          
						        </tr>
						    </table>
				            </td>
				        </tr>
						<!-- End of body title -->
				
				        <tr class="tablesubhead">
							<td class="databorder" width="1"></td>
							<c:if test="${empty param.printFriendly }" >					          
					          	<td valign="middle" width="90" height="20">
									<img src="/assets/unmanaged/images/s.gif" width="2" height="1">
									<font color="#990000"><b>Action</b></font>
								</td>
					          	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				          	</c:if>
							<td valign="middle" width="500" height="20">
								<img src="/assets/unmanaged/images/s.gif" width="4" height="1">
								<font color="#990000"><b>Reports</b></font>
							</td>
							<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td valign="middle" width="500" height="20">
								<img src="/assets/unmanaged/images/s.gif" width="4" height="1">
								<font color="#990000"><b>Review Status</b></font>
							</td>
							<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td valign="middle" width="250" height="20">
								<img src="/assets/unmanaged/images/s.gif" width="4" height="1">
								<font color="#990000"><b>Notices</b></font>
							</td>
							<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				        </tr>
				        
<c:if test="${empty ipsAndReviewDetailsForm.ipsReviewReportDetailsList}">
				       		<tr class="datacell1">
				       			<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				       			<c:if test="${empty param.printFriendly }" >
						          	<td width="90" height="20">	</td>
						          	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					          	</c:if>
					          <td  width="500">
								<img src="/assets/unmanaged/images/s.gif" width="4" height="1">
					          	<content:getAttribute attribute="text" beanName="noPreviousIPSReportText" />
					          </td >
					          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					          <td width="500" ></td>
					          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					          <td width="280"></td>
					          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>                
				       		</tr>
</c:if>
<c:if test="${not empty ipsAndReviewDetailsForm.ipsReviewReportDetailsList}">
<c:forEach items="${ipsAndReviewDetailsForm.ipsReviewReportDetailsList}" var="ipsReviewReportDetails" varStatus="theIndex" >
<%-- <a href="javascript:doOutputSelect(${ipsReviewReportDetails.reviewRequestId}, 'true' , 'false')" id="outputSelect"  > --%>
<!-- 											Participant Notification</a> -->
<c:set var="indexValue" value="${theIndex.index}" />
                   <%
                      String temp = pageContext.getAttribute("indexValue").toString();
						 if (Integer.parseInt(temp) % 2 != 0) {
					%> 
                    <tr class="datacell1">
					<%
					     } else {
					%>					
					<tr class="datacell2">
					<%
					     }
					%>
						
<%-- 							<c:choose> --%>
<%-- 								<c:when test="${theIndex.index % 2 == 0}"> --%>
<!-- 									<tr class="datacell2"> -->
<%-- 								</c:when> --%>
<%-- 								<c:otherwise> --%>
<!-- 									<tr class="datacell1"> -->
<%-- 								</c:otherwise> --%>
<%-- 							</c:choose> --%>
				
				          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				          <c:if test="${empty param.printFriendly }" >
				          	<td align="left" width="90" height="20">									
				          		<table width="100%" border="0" cellspacing="0" cellpadding="1">
<c:if test="${ipsReviewReportDetails.viewAvailable ==true}">
										<tr>
											<td>
												
													<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
													<a href="/do/investment/viewIPSReviewResults/?reviewRequestId=${ipsReviewReportDetails.reviewRequestId}"><img src="/assets/unmanaged/images/view_icon.gif" width="12"
															height="12" border="0" /></a>
<c:if test="${ipsReviewReportDetails.editAvailable ==true}">
														<a href="/do/investment/editIPSReviewResults/?reviewRequestId=${ipsReviewReportDetails.reviewRequestId}">
														<img src="/assets/unmanaged/images/edit_icon.gif" width="12"
															height="12" border="0" /></a>
</c:if>
												
											</td>
										</tr>
</c:if>
<c:if test="${ipsReviewReportDetails.cancelAvailable ==true}">
										<tr>
											<td>
													<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
													<a href="/do/investment/cancelIPSReview/?reviewRequestId=${ipsReviewReportDetails.reviewRequestId}"><img src="/assets/unmanaged/images/cancel_icon.gif"
														width="12" height="12" border="0" /></a>
											</td>
										</tr>
</c:if>
								</table>
				          	</td>
				          	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				          	</c:if>
				          <td width="500" height="20" style="padding-left: 2px;">
							<c:if test="${empty param.printFriendly }" >
<c:if test="${ipsReviewReportDetails.currentReview ==true}">
									<c:choose>
						          		<c:when test="${ipsReviewReportDetails.currentReportLinkAccessible}">
						          			<a href="/do/investment/ipsManager/?action=generateReviewReport&reviewRequestId=${ipsReviewReportDetails.reviewRequestId}" >
									          	<content:getAttribute
															attribute="text" beanName="ipsCurrentReportText">
									          		<content:param>
${ipsReviewReportDetails.annualReviewDate}

													</content:param>
												</content:getAttribute>
											</a>
						          		</c:when>
						          		<c:otherwise>
						          			<content:getAttribute
															attribute="text" beanName="ipsCurrentReportText">
									          		<content:param>
${ipsReviewReportDetails.annualReviewDate}

													</content:param>
											</content:getAttribute>
						          		</c:otherwise>
						          	</c:choose>	
</c:if>
<c:if test="${ipsReviewReportDetails.currentReview !=true}">
					          		<c:choose>
						          		<c:when test="${ipsReviewReportDetails.currentReportLinkAccessible}">
						          			<a href="/do/investment/ipsManager/?action=generateReviewReport&reviewRequestId=${ipsReviewReportDetails.reviewRequestId}">
${ipsReviewReportDetails.previousReportLabel}
											</a>
						          		</c:when>
						          		<c:otherwise>
${ipsReviewReportDetails.previousReportLabel}
						          		</c:otherwise>
						          	</c:choose>
</c:if>
				          	</c:if>
				          	<c:if test="${not empty param.printFriendly }" >
<c:if test="${ipsReviewReportDetails.currentReview ==true}">
					          		<c:choose>
							          	<c:when test="${ipsReviewReportDetails.currentReportLinkAccessible}">
							          		<U>
								          		<content:getAttribute
															attribute="text" beanName="ipsCurrentReportText">
									          		<content:param>
${ipsReviewReportDetails.annualReviewDate}

													</content:param>
												</content:getAttribute>
											</U>
										</c:when>
										<c:otherwise>
											<content:getAttribute
															attribute="text" beanName="ipsCurrentReportText">
									          		<content:param>
${ipsReviewReportDetails.annualReviewDate}

													</content:param>
												</content:getAttribute>
										</c:otherwise>
									</c:choose>
</c:if>
<c:if test="${ipsReviewReportDetails.currentReview !=true}">
					          		<c:choose>
						          		<c:when test="${ipsReviewReportDetails.currentReportLinkAccessible}">
						          			<U>
${ipsReviewReportDetails.previousReportLabel}
											</U>
						          		</c:when>
						          		<c:otherwise>
${ipsReviewReportDetails.previousReportLabel}
						          		</c:otherwise>
						          	</c:choose>
</c:if>
				          	</c:if>
				          </td>
				          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				          <td width="500" height="20">
								<img src="/assets/unmanaged/images/s.gif" width="4" height="1">
								<span class="highlightBold">
${ipsReviewReportDetails.reviewRequestStatus}
								</span>
<c:if test="${ipsReviewReportDetails.showNoFundMatchingTresholdIcon ==true}">
								 <img src="/assets/generalimages/info.gif" width="12" height="12" onmouseover="Tip('<content:getAttribute attribute="text" beanName="noFundsMatchingTreshold"/>')"
								               onmouseout="UnTip()" />
</c:if>
						  </td>
				          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				          <td width="280" height="20">
							<img src="/assets/unmanaged/images/s.gif" width="4" height="1">
							<c:if test="${empty param.printFriendly }" >
<c:if test="${ipsReviewReportDetails.participantNoticationAvailable ==true}">
									<a href="javascript:doOutputSelect(${ipsReviewReportDetails.reviewRequestId}, 'true' , 'false')" id="outputSelect"  >
											Participant Notification</a>
</c:if>
							</c:if>
							<c:if test="${not empty param.printFriendly }" >
<c:if test="${ipsReviewReportDetails.participantNoticationAvailable ==true}">
									<U>Participant Notification</U>
</c:if>
							</c:if>
				          </td>
				          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>                
				       	  </tr>
</c:forEach>
 </c:if> 
				
						<!-- End of Last line -->
				
				        <tr>
							<td class="databorder" width="100%" height="1" colspan="<%=numberOfColumns%>">						
							</td>
						</tr>
				 	</table>
				 	<table>	
				  		<tr>
				  			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
								height="1"></td>
							<td width="300"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>
							<td width="150"><img src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></td>
							<td width="40"><img src="/assets/unmanaged/images/s.gif" width="1"
								height="1"></td>
				  			<td>
				  				<c:if test="${ipsAndReviewDetailsForm.interimReportLinkAvailable && ipsAndReviewDetailsForm.iPSAssistServiceAvailable}">
				  				<c:if test="${empty param.printFriendly}" >
					  				<a href="javascript://" onClick="doPrintPDF();return false;">
					  					<content:getAttribute attribute="text" beanName="ipsAdhocReportLink" />
					  				</a>
				  				</c:if>
				  				</c:if>
				  			</td>
				  		</tr>
				 	</table>  
				 </td>
			</tr>
		</table>
	</c:if>
<br><br><br>
<c:if test="${not empty param.printFriendly}">
	<content:contentBean contentId="${contentConstants.GLOBAL_DISCLOSURE}"
        type="${contentConstants.TYPE_MISCELLANEOUS}"
        id="globalDisclosure"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>

</ps:form>
</div>
<div id="modalGlassPanel" class="modal_glass_panel" style="display:none"></div>
<div id="page_wrapper_footer">&nbsp;</div>
