<%--Taglibs Used--%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
	<%@page import="com.manulife.pension.platform.web.investment.IPSAndReviewDetailsForm"%>
<%--Imports Used--%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page
	import="com.manulife.pension.platform.web.investment.valueobject.IPSReviewReportDetailsVO"%>
<%@ page
	import="com.manulife.pension.bd.web.BDConstants"%>
<%@page
	import="com.manulife.pension.platform.web.util.ReportsXSLProperties"%>
<%@ page import="com.manulife.pension.content.valueobject.Miscellaneous" %>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="constants"
	className="com.manulife.pension.platform.web.CommonConstants" />
<un:useConstants var="bdContentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />	
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />
<un:useConstants var="reportsXSLProperties"
	className="com.manulife.pension.platform.web.util.ReportsXSLProperties" />
	
<%--Contents Used--%>
<content:contentBean
	contentId="${bdContentConstants.IPS_SERVICE_BROCHURE_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsServiceBrochureLink" override="true" />
<content:contentBean
	contentId="${bdContentConstants.INVESTMENT_POLICY_STATEMENT_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsInvestmentPolicyStatementLink" override="true" />
<content:contentBean
	contentId="${bdContentConstants.IPS_PARTICIPANT_NOTIFICATION_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsParticipantNotificationLink" override="true" />
<content:contentBean
	contentId="${bdContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}" id="pdfIcon" />
<content:contentBean
	contentId="${bdContentConstants.IPS_ASSIST_SERVICE_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsAssistServiceText" />
<content:contentBean
	contentId="${bdContentConstants.IPS_SCHEDULE_ANNUAL_REVIEW_DATE}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsScheduleAnnualReviewDate" />
<content:contentBean
	contentId="${bdContentConstants.IPS_SERVICE_REVIEW_DATE_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsServiceReviewDateText" />
<content:contentBean
	contentId="${bdContentConstants.IPS_REVIEW_REPORTS_SECTION_TITLE}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsReviewReportSectionTitle" />
<content:contentBean
	contentId="${bdContentConstants.IPS_VIEW_REPORT_PDF_ICON}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="viewReportPdfIcon" />
<content:contentBean
	contentId="${bdContentConstants.IPS_CURRENT_REPORT_LABEL_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsReviewResultsPageLink" override="true" />
<content:contentBean
	contentId="${bdContentConstants.IPS_NO_CURRENT_OR_PREVIOUS_REPORT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="NoCurrentorPreviousIPSReport" />
<content:contentBean
	contentId="${bdContentConstants.IPS_ASSIST_SERVICE_TEXT_DEACTIVATED}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsAssistServiceTextDeactivated" />
<content:contentBean
	contentId="${bdContentConstants.IPS_SERVICE_BROCHURE_PATH}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsServiceBrochurePath" />
<content:contentBean
	contentId="${bdContentConstants.IPS_GUIDE_PATH}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsGuidePath" />
<content:contentBean
	contentId="${bdContentConstants.IPS_PARTICIPANT_NOTIFICATION_PATH}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsParticaipantNotificationPath" />
	
<content:contentBean
	contentId="${bdContentConstants.IPS_NEW_ANNUAL_REVIEW_DATE_SAVE_CONFIRMATION_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsNewAnnualReviewDateSaveConfirmationText" />
<content:contentBean
	contentId="${bdContentConstants.IPS_NEW_ANNUAL_REVIEW_DATE_NO_STATUS_CHANGE_SAVE_CONFIRMATION_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsNewAnnualReviewDateNoStatusChangeSaveConfirationText" />
<content:contentBean
	contentId="${bdContentConstants.IPS_EXTERNAL_NEW_ANNUAL_REVIEW_DATE_SAVE_CONFIRMATION_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsExternalNewAnnualReviewDateSaveConfirmationText" />
<content:contentBean
	contentId="${bdContentConstants.IPS_EXTERNAL_NEW_ANNUAL_REVIEW_DATE_NO_STATUS_CHANGE_SAVE_CONFIRMATION_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsExternalNewAnnualReviewDateNoStatusChangeSaveConfirationText" />
	
<content:contentBean
	contentId="${bdContentConstants.SAVE_HOVER_OVER_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="saveButtonHoverOverText" />
<div id='ips'>
<bd:form method="POST" action="/do/bob/investment/editIPSManager/" modelAttribute="ipsAndReviewDetailsForm" name="ipsAndReviewDetailsForm">
<form:hidden path="pdfCapped"/>
<form:hidden path="formChanged"/>
<form:hidden path="totalWeighting"/>
<form:hidden path="dateChanged"/>
<form:hidden path="criteriaChanged"/>
<form:hidden path="newServiceDateConfirmationText"/>
<form:hidden path="action"/>
	
<script type="text/javascript"> 
function doViewPDF() {
	doOpenPDF(<%=ReportsXSLProperties
							.get(BDConstants.MAX_CAPPED_ROWS_IN_PDF)%>)
}


/**
 * This function will be called when the user clicks on download PDF button.
 * @param maxRowsAllowedInPDF
 * @return
 */
function doOpenPDF(maxRowsAllowedInPDF) {
	var reportURL = new URL();
	reportURL.setParameter("action", "printPDF");
	var pdfCapped = document.getElementsByName("pdfCapped")[0].value;
	if (pdfCapped == "true") {
		var confirmPdfCapped = confirm("The PDF you are about to view is capped at " + maxRowsAllowedInPDF + " rows. Click OK to Continue.");
		if (confirmPdfCapped) {
				window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
		}
	} else {
			window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
	}
}

function openEditCriteriaAndWeightingsPage() {		

	var serviceDateChangeNotAvailableText = '${serviceDateChangeNotAvailableText}';
	
	if(serviceDateChangeNotAvailableText != '') {		
		alert(serviceDateChangeNotAvailableText);
	} else {	
		window.location= "/do/bob/investment/ipsManager/?action=edit";
	}
	
}

function CalculateTotal() {
    var total = 0;
    var rowCount = ${ipsAndReviewDetailsForm.criteriaAndWeightingPresentationList.size()};
 	// Iterate and get the value from weightings text box
    for (var i=0; i < rowCount; ++i) {
        // Get the current field
        var form_field = document.getElementsByName("criteriaAndWeightingPresentationList["+i+"].weighting");
		if (!isNaN(form_field[0].value)) {
			var weighting = parseInt(form_field[0].value);
			// Count only the whole numbers
			if (weighting >0 && form_field[0].value.toString().indexOf(".") == -1){
				total += weighting;		
		}
	   }
    }
	document.getElementById('totalWeight').innerHTML = total+'%';
	document.ipsAndReviewDetailsForm.totalWeighting.value=total;
	document.ipsAndReviewDetailsForm.criteriaChanged.value=true;
}

function serviceDateChanged() {
	document.ipsAndReviewDetailsForm.dateChanged.value=true;
}

function criteriaWeightingChanged() {
	document.ipsAndReviewDetailsForm.criteriaChanged.value=true;
}

function doSubmit(button, popupContent){
	var confirmAction =true; 
	if (isFormChanged()){
		document.ipsAndReviewDetailsForm.formChanged.value=true;
	} else {
		document.ipsAndReviewDetailsForm.formChanged.value=false;
	}
	if(document.ipsAndReviewDetailsForm.dateChanged.value == 'true') {
		var newServiceMonth = document.getElementsByName('newAnnualReviewMonth')[0].value;
		var newServiceDate = document.getElementsByName('newAnnualReviewDate')[0].value;		
		
		var isInValidServiceDate = false;
		if ("31" == newServiceDate) {
			if ("02" == newServiceMonth || "04" == newServiceMonth
					|| "06" == newServiceMonth
					|| "09" == newServiceMonth
					|| "08" == newServiceMonth
					|| "11" == newServiceMonth) {
				isInValidServiceDate = true;
			} else {
				isInValidServiceDate = false;
			}
		} else if (("30" == newServiceDate || "29" == newServiceDate)
				&& "02" == newServiceMonth) {
				isInValidServiceDate = true;
		} else {
			isInValidServiceDate =  false;
		}		
		
		if(!isInValidServiceDate) {
			var m_names = new Array("","January", "February", "March",
					"April", "May", "June", "July", "August", "September",
					"October", "November", "December");
			if(newServiceMonth < 10) {
				newServiceMonth = newServiceMonth.replace('0','');
			}
			var newReviewDate = '';
			var newServiceMonthText = m_names[newServiceMonth];
			if(newServiceDate < 10){
				newReviewDate = newServiceMonthText.substr(0,3)+' '+0+newServiceDate;
			} else {
				newReviewDate = newServiceMonthText.substr(0,3)+' '+newServiceDate;
			}		
			if(newReviewDate !='${ipsAndReviewDetailsForm.annualReviewDate}') {
				var newDate = populateNewServicedate(newServiceMonth, newServiceDate, newServiceMonthText);		
				
				if('ipsNewAnnualReviewDateSaveConfirmationText' == popupContent) {
					confirmAction = confirm('<content:getAttribute attribute="text" beanName="ipsNewAnnualReviewDateSaveConfirmationText"><content:param>'+newDate+'</content:param></content:getAttribute>');
				} else if('ipsNewAnnualReviewDateNoStatusChangeSaveConfirationText' == popupContent) {
					confirmAction =  confirm('<content:getAttribute attribute="text" beanName="ipsNewAnnualReviewDateNoStatusChangeSaveConfirationText"><content:param>'+newDate+'</content:param></content:getAttribute>');
				} else if('ipsExternalNewAnnualReviewDateSaveConfirmationText' == popupContent) {
					confirmAction =  confirm('<content:getAttribute attribute="text" beanName="ipsExternalNewAnnualReviewDateSaveConfirmationText"><content:param>'+newDate+'</content:param></content:getAttribute>');
				} else if('ipsExternalNewAnnualReviewDateNoStatusChangeSaveConfirationText' == popupContent) {
					confirmAction =  confirm('<content:getAttribute attribute="text" beanName="ipsExternalNewAnnualReviewDateNoStatusChangeSaveConfirationText"><content:param>'+newDate+'</content:param></content:getAttribute>');
				}		
			}
		}
	}
	document.ipsAndReviewDetailsForm.action.value=button;
	if(confirmAction) {
		document.forms.ipsAndReviewDetailsForm.submit();
	} else {
		return false;
	}
	
}

function populateNewServicedate(newServiceMonth, newServiceDate, newServiceMonthText) {
	var newServiceDateToDisplay = '';
	var currentDate = new Date();		
	
	var currentMonth = (currentDate.getMonth()+1);
	var currentDay = currentDate.getDate();
	
	if(newServiceMonth > currentMonth) {
		newServiceDateToDisplay = newServiceMonthText+" "+newServiceDate+", "+currentDate.getFullYear();
	} else if(newServiceMonth == currentMonth) {
		if(newServiceDate >= currentDay) {
			newServiceDateToDisplay = newServiceMonthText+" "+newServiceDate+", "+currentDate.getFullYear();
		} else {
			newServiceDateToDisplay = newServiceMonthText+" "+newServiceDate+", "+(currentDate.getFullYear()+1);
		}
	} else {
		newServiceDateToDisplay = newServiceMonthText+" "+newServiceDate+", "+(currentDate.getFullYear()+1);
	}
	
	return newServiceDateToDisplay;
}

function doCancel(button){
	if(discardChanges('The action you have selected will cause your changes to be lost. Select OK to continue or cancel to return.')){
		document.ipsAndReviewDetailsForm.action.value=button;
		document.forms.ipsAndReviewDetailsForm.submit();
	}
}

function isFormChanged() {
	  return changeTracker.hasChanged();
	}

registerTrackChangesFunction(isFormChanged);
</script>


<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>
	

<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%--<c:set var="ipsAndReviewDetailsForm" scope="session"
	value="${ipsAndReviewDetailsForm}"
	type="com.manulife.pension.platform.web.investment.IPSAndReviewDetailsForm"/> --%>


<%--Summary Box--%>
<div id="summaryBox" style="width: 300px;">
<h1>Additional Resources</h1>
<a href="#anchor1" style="font-size: 11px;"
	onclick="javascript:PDFWindow('<%=((Miscellaneous)ipsServiceBrochurePath).getText()%>')">
	<content:getAttribute attribute="text" beanName="ipsServiceBrochureLink" />
</a>
<br>
<a href="#anchor1" style="font-size: 11px;"
	onclick="javascript:PDFWindow('<%=((Miscellaneous)ipsGuidePath).getText()%>')">
	<content:getAttribute attribute="text" beanName="ipsInvestmentPolicyStatementLink" />
</a>
<br>
<a href="#anchor1" style="font-size: 11px;"
	onclick="javascript:PDFWindow('<%=((Miscellaneous)ipsParticaipantNotificationPath).getText()%>')">
	<content:getAttribute attribute="text" beanName="ipsParticipantNotificationLink" />
</a>
</div>

<%--Page Title and Introduction Messages--%>
<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>

<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
  <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
</p>


<!--Layout/Intro1-->
<c:if test="${not empty layoutPageBean.introduction1}">
    <p class="record_info"><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>
        
<!--Layout/Intro2-->
<c:if test="${not empty layoutPageBean.introduction2}">
    <p class="record_info"><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
</c:if>

<!--Error- message box-->  
  <report:formatMessages scope="session"/><br/>
  
<%--Navigation bar--%>
<navigation:contractReportsTab />

<!--Section title and Display preferences-->
<div class="page_section_subheader controls">
	<h3>
		<content:getAttribute beanName="layoutPageBean" attribute="subHeader" />
	</h3>
	<form class="page_section_filter form">
		<p>
			<render:date property="bobContext.currentContract.contractDates.asOfDate"
				patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" defaultValue="" />
		</p>
	<%-- <a href="javascript://" onClick="doViewPDF()" class="pdf_icon"
	   title="<content:getAttribute beanName="pdfIcon" attribute="text"/>">
		<content:image contentfile="image" id="pdfIcon" /> 
	</a> --%>
</div>

<%--Included to show IPS Manager Setup Details section  --%>
<div id="page_section_container">
	<div class="page_section">
		<div class="page_module">
			<h4>
				<content:getAttribute beanName="layoutPageBean" attribute="body1Header" />
			</h4>
			<table>
				<tbody>
					<tr>
						<td colspan="2"><content:getAttribute attribute="text"
							beanName="ipsAssistServiceText" />
							<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable == true}">
				 		
										<b>
											Active
										</b>
							</c:if> 
							 <c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable != true}">
										<b>
											Not active
										</b>
							</c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<content:getAttribute attribute="text" beanName="ipsScheduleAnnualReviewDate" />
								<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable == true}">
										<b>
											${ipsAndReviewDetailsForm.annualReviewDate}
										</b>
								</c:if> 
						<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable != true}">
										<b>
											n/a
										</b>
								</c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<content:getAttribute attribute="text" beanName="ipsServiceReviewDateText" />
						</td>
					</tr>

					<tr>
						<td>New annual Review Date</td>
						<td> 
						<form:select path="newAnnualReviewMonth" size="0"
								styleClass="greyText" 
								onchange="serviceDateChanged()">
								<form:options items="${newAnnualReviewMonthMap}" />
								</form:select>
							  <bd:trackChanges escape="true" property="newAnnualReviewMonth" name="ipsAndReviewDetailsForm" /> 
							<form:select path="newAnnualReviewDate" size="0" styleClass="greyText"
										onchange="serviceDateChanged()">
										<form:options items="${newAnnualReviewDateMap}" />
										</form:select>
							
							 <bd:trackChanges escape="true" property="newAnnualReviewDate" name="ipsAndReviewDetailsForm" /> 
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

<%--  Included to show Criteria and Weightings section  --%>
	<div class="page_section">
		<div class="page_module">
			<h4>
				<content:getAttribute beanName="layoutPageBean" attribute="body2Header" />
			</h4>
			<%-- <logic:notEqual name="ipsAndReviewDetailsForm" property="iPSAssistServiceAvailable" value="true">
			<table>
				<tbody>
					<tr>
						<td width="50%" valign="top">
							<content:getAttribute attribute="text" beanName="ipsAssistServiceTextDeactivated" />
						</td>
					</tr>
				</tbody>
			</table>
			</logic:notEqual> --%>
			<%-- <logic:equal name="ipsAndReviewDetailsForm" property="iPSAssistServiceAvailable" value="true"> --%>
				<table>
					<tbody>
						<tr>
							<td width="63%" valign="top">
								<table width=100%>
								<c:forEach items="${ipsAndReviewDetailsForm.criteriaAndWeightingPresentationList}" var="criteriaAndWeightingId" varStatus="index">
								
								<c:set var="indexVal" value="${index.index}"/>
											<tr>
												<td align="center">
													<table border="0" cellpadding="0" cellspacing="0">
														<tr>
															<td height="3" width="3" style="background:${criteriaAndWeightingId.colorCode}">
																<img src="/assets/unmanaged/images/s.gif" width="3" height="3">
															</td>
														</tr>
													</table>
												</td>
											<td align="left">
											<form:select path="criteriaAndWeightingPresentationList[${indexVal}].criteriaCode" indexed="true" size="1" styleClass="greyText"
													onchange="criteriaWeightingChanged()">
													<form:options items="${criteriaDescMap}" />
													</form:select>
												  <bd:trackChanges escape="true" property="criteriaCode" indexPrefix="criteriaAndWeightingId" name="ipsAndReviewDetailsForm" /> 
											</td>
											<td align="left">
												 <bd:trackChanges escape="true" property="weighting" indexPrefix="criteriaAndWeightingId" name="ipsAndReviewDetailsForm" /> 
												<form:input path="criteriaAndWeightingPresentationList[${indexVal}].weighting" size="1" indexed="true" maxlength="3"
													styleClass="greyText" onchange="CalculateTotal()"/>
												 <td>%</td>
											</td>
										</tr>
									</c:forEach>
									<tr>
										<td></td>
										<td style="font-size:12px;">
											<b>
												Total
											</b>
										</td>
										<td align="left" style="font-size: 12px;"><b> <span
												id="totalWeight">${ipsAndReviewDetailsForm.totalWeighting}%

											</span>
										</b></td>
									</tr>
									<tr>
										<td colspan="3">
											<img src="/assets/unmanaged/images/s.gif" height="2">
										</td>
									</tr>
								</table>
							</td>
							<td width="50%" class="datacell1" align="center" valign="top">
								<bd:pieChart beanName="${constants.IPSR_CRITERIA_WEIGHTING_PIECHART}"
									alt="If you have trouble seeing this chart image, please try again later. If the problem persists, please contact your Client Account Representative."
									title="IPSR Criteria And Weighting" />
							</td>
						</tr>
						<% if(userProfile.isInternalUser() || userProfile.isInMimic()){ %>
							<tr>
									<td align="right" valign="top" style="padding-top: 10px;">
									<input class="btn-change-contract" type="button" value="save"
									onclick="return doSubmit('save','ipsNewAnnualReviewDateSaveConfirmationText')"
										onmouseover="Tip('<content:getAttribute attribute="text" beanName="saveButtonHoverOverText"/>')"
										onmouseout="UnTip()" />
								<input
									class="btn-change-contract" type="button" value="cancel" class="button134"
									onclick="return doCancel('cancel')" />
									</td>
							</tr>
						<% } else { %>
							<tr>
									<td align="right" valign="top" style="padding-top: 10px;">
									<input class="btn-change-contract" type="button" value="save"
									onclick="return doSubmit('save','ipsNewAnnualReviewDateSaveConfirmationText')" />
								<input
									class="btn-change-contract" type="button" value="cancel" class="button134"
									onclick="return doCancel('cancel')" />
									</td>
							</tr>
						<% } %>
					</tbody>
				</table>
			<%-- </logic:equal> --%>
		</div>
	</div>
</div>

<script type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>
</bd:form>
</div>