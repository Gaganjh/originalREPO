<%--Taglibs Used--%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
	
<%--Imports Used--%>
 <%@ page import="com.manulife.pension.platform.web.investment.IPSAndReviewDetailsForm"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page
	import="com.manulife.pension.platform.web.investment.valueobject.IPSReviewReportDetailsVO"%>
<%@ page
	import="com.manulife.pension.bd.web.BDConstants"%>
<%@page
	import="com.manulife.pension.platform.web.util.ReportsXSLProperties"%>
	<%@ page import="com.manulife.pension.content.valueobject.Miscellaneous" %>
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
  contentId="${bdContentConstants.IPS_CHANGE_CRIERIA_AND_WEIGHTINGS}" 
   type="${bdContentConstants.TYPE_MISCELLANEOUS}" 
   id="ipsChangeWeightingsAndCriteriaLink"/>
<content:contentBean
	contentId="${bdContentConstants.IPS_ADHOC_REPORT_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsAdhocReportLink" />
<content:contentBean
	contentId="${bdContentConstants.SAVE_CONFIRMATION_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="saveConfirmationText" />
<content:contentBean
	contentId="${bdContentConstants.IPS_NO_FUND_MATCHING_TRESHOLD_ICON_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="noFundsMatchingTreshold" />	   
   

<style type="text/css">
div.modal_glass_panel {
        z-index:5;
        position:absolute;
        top:1px;
        left:1px;
        width:1024px;
        height:1500px !important;
        background-color:#C0C0C0;        
        filter:alpha(opacity=50);
        -moz-opacity:0.5;
        opacity:0.5;
     }        

    div.modal_dialog {
        z-index:10;
        font-size:11px;
        margin:auto;
        padding:0px;
        position:absolute;
        background:#FFFFFF;
        border:2px inset #005B80;
              
    }
div.modal_dialog th, td{
        font-size:11px !important;
    }
</style>
<script type="text/javascript"> 

function doPrintPDF() {
	var reportURL = new URL();
	reportURL.setParameter("action", "printInterimReport");
	window.location= "/do/bob/investment/ipsManager/?action=printInterimReport";
}
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
</script>

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
	
	IPSAndReviewDetailsForm ipsAndReviewDetailsForm =(IPSAndReviewDetailsForm)session.getAttribute("ipsAndReviewDetailsForm");
	pageContext.setAttribute("ipsAndReviewDetailsForm",ipsAndReviewDetailsForm,PageContext.PAGE_SCOPE);
%>



<%-- <c:set var="ipsAndReviewDetailsForm" value="${ipsAndReviewDetailsForm}" scope="session" /> --%>




	
<input type="hidden" name="pdfCapped" /><%--  input - name="ipsAndReviewDetailsForm" --%>
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
<!--Success- message box--> 
<c:if test="${ipsSuccessInd}">
	<div class="message message_info"><dl><dt>Information Message</dt><dd>1.&nbsp;&nbsp;<content:getAttribute attribute="text" beanName="saveConfirmationText" /></dd></dl></div>
</c:if>
<!--Error- message box-->  
  <report:formatMessages scope="session"/><br/>
 <div id="participantNotificationSection" style="display:none">
 <bd:form method="POST" action="/bob/investment/viewParticipantNotification/" modelAttribute="ipsViewParticiapantNotificationForm" name="ipsViewParticiapantNotificationForm">
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
	
	</bd:form>
</div> 
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
	</form>
	<a href="javascript://" onClick="doViewPDF()" class="pdf_icon"
	   title="<content:getAttribute beanName="pdfIcon" attribute="text"/>">
		<content:image contentfile="image" id="pdfIcon" /> 
	</a>
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
<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable ==true}">

										<b>
											Active
										</b>
</c:if>
<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable !=true}">

										<b>
											Not active
										</b>
</c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<content:getAttribute attribute="text" beanName="ipsScheduleAnnualReviewDate" />
<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable ==true}">

										<b>
${ipsAndReviewDetailsForm.annualReviewDate}
										</b>
</c:if>
<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable !=true}">
										<b>
											n/a
										</b>
</c:if>
<c:if test="${ipsAndReviewDetailsForm.is338DesignateRia ==true}">

									<a href="javascript:openEditCriteriaAndWeightingsPage();">
									<span style="color:#005B80; text-decoration:underline">	Change </span></a>
</c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<content:getAttribute attribute="text" beanName="ipsServiceReviewDateText" />
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
<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable !=true}">
			<table>
				<tbody>
					<tr>
						<td width="50%" valign="top">
							<content:getAttribute attribute="text" beanName="ipsAssistServiceTextDeactivated" />
						</td>
					</tr>
				</tbody>
			</table>
</c:if>
<c:if test="${ipsAndReviewDetailsForm.iPSAssistServiceAvailable ==true}">
				<table>
					<tbody>
						<tr>
							<td width="50%" valign="top">
								<table width=100%>
<c:forEach items="${ipsAndReviewDetailsForm.criteriaAndWeightingPresentationList}" var="criteriaAndWeightingId" varStatus="index">

										<c:if test="${(not empty criteriaAndWeightingId.criteriaDesc) and (not empty criteriaAndWeightingId.weighting)}">
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
												<td valign="top" style="font-size:12px;">
${criteriaAndWeightingId.criteriaDesc}
												</td>
												<td valign="top" align="right" style="font-size:12px;">
${criteriaAndWeightingId.weighting}%
												</td>
											</tr>
										</c:if>
</c:forEach>
									<tr>
										<td></td>
										<td style="font-size:12px;">
											<b>
												Total
											</b>
										</td>
										<td align="right" style="font-size:12px;">
											<b>
${ipsAndReviewDetailsForm.totalWeighting}%
											</b>
										</td>
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
						<tr>
						<td>
 <c:if test="${ipsAndReviewDetailsForm.is338DesignateRia ==true}"> 

							<span style="color:#005B80; text-decoration:underline"> 
							<content:getAttribute attribute="text" beanName="ipsChangeWeightingsAndCriteriaLink" >
								<content:param>
								    javascript:openEditCriteriaAndWeightingsPage();
								</content:param>
							</content:getAttribute>
							</span>
</c:if> 
						</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<c:set var="ipsChangeHistory" value="${ipsChangeHistory}"/>
							<td onmouseover="<bd:ipsAndReviewChangeDetail name='ipsChangeHistory' current='false'/>" onmouseout="UnTip()">Last modified on:
								<render:date patternOut="${renderConstants.MEDIUM_MDY_SLASHED}"
									property="ipsAndReviewDetailsForm.lastModifiedOn" />
							</td>
						</tr>
					</tbody>
				</table>
</c:if>
		</div>
	</div>
</div>

<%--  Included to show Review Reports section  --%>
<div class="report_table">
<div class="page_section_subsubheader" style="font: normal 12px verdana;">
		<h4>
			<content:getAttribute beanName="ipsReviewReportSectionTitle" attribute="text" />
		</h4>
	</div>
	</div>
<div class="report_table">
		<table class="report_table_content">
			<thead>
				<tr>
<c:if test="${ipsAndReviewDetailsForm.is338DesignateRia ==true}">

				    	<th width="7%" class="val_str"> Action</th>
</c:if>
					<th width="4%" class="val_str">Reports</th>
					<th width="20%" class="val_str">View Results</th>
					<th width="20%" class="val_str">Review Status</th>
					<th width="14%" class="val_str">Notices</th>
				</tr>
			</thead>
			<tbody>
<c:if test="${empty ipsAndReviewDetailsForm.ipsReviewReportDetailsList}">

						<tr>
<c:if test="${ipsAndReviewDetailsForm.is338DesignateRia ==true}"><td>&nbsp;</td>

</c:if>
							<td>&nbsp;</td>
							<td>
								<content:getAttribute attribute="text" beanName="NoCurrentorPreviousIPSReport"/>
							</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
</c:if>
<c:if test="${not empty ipsAndReviewDetailsForm.ipsReviewReportDetailsList}">
<c:forEach items="${ipsAndReviewDetailsForm.ipsReviewReportDetailsList}" var="ipsReviewReportDetails" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/>
							<%-- This logic is to differentiate the alternate rows --%>
							<c:choose>
								<c:when test="${indexValue % 2 == 1}">
									<tr class="spec">
								</c:when>
								<c:otherwise>
									<tr class="spec1">
								</c:otherwise>
							</c:choose>
<c:if test="${ipsAndReviewDetailsForm.is338DesignateRia ==true}">

									<td>
  <c:if test="${ipsReviewReportDetails.editAvailable ==true}"> 
											 <a href="/do/bob/investment/editIPSReviewResults/?reviewRequestId=${ipsReviewReportDetails.reviewRequestId}">Edit</a>
  </c:if>  
<c:if test="${ipsReviewReportDetails.editAvailable ==true}">
<c:if test="${ipsReviewReportDetails.cancelAvailable ==true}">
												<span>/</span>
</c:if>
</c:if>
  <c:if test="${ipsReviewReportDetails.cancelAvailable ==true}">  
											 <a href="/do/bob/investment/cancelIPSReview/?reviewRequestId=${ipsReviewReportDetails.reviewRequestId}">Cancel</a>
 </c:if>  
									</td>
</c:if>
								<td align="center">
<c:if test="${ipsReviewReportDetails.currentReportLinkAccessible ==true}">
										<a href="?action=generateReviewReport&reviewRequestId=${ipsReviewReportDetails.reviewRequestId}">
												<content:image contentfile="image" id="pdfIcon" />
										</a>
</c:if>
								</td>
								<td>
<c:if test="${ipsReviewReportDetails.viewAvailable ==true}">
											<a href="/do/bob/investment/viewIPSReviewResults/?reviewRequestId=${ipsReviewReportDetails.reviewRequestId}">
												<content:getAttribute attribute="text" beanName="ipsReviewResultsPageLink">
													<content:param>
${ipsReviewReportDetails.annualReviewDate}
													</content:param>
												</content:getAttribute>
											</a>
</c:if>
<c:if test="${ipsReviewReportDetails.viewAvailable !=true}">
											<content:getAttribute attribute="text" beanName="ipsReviewResultsPageLink">
												<content:param>
${ipsReviewReportDetails.annualReviewDate}
												</content:param>
											</content:getAttribute>
</c:if>
								</td>
								<td>
									<font color="olive">
${ipsReviewReportDetails.reviewRequestStatus}
									</font>
<c:if test="${ipsReviewReportDetails.showNoFundMatchingTresholdIcon ==true}">
					                      <img src="/assets/generalimages/info.gif" width="12" height="12" onmouseover="Tip('<content:getAttribute attribute="text" beanName="noFundsMatchingTreshold"/>')"
								               onmouseout="UnTip()" />
</c:if><br>
								</td>
								<td>
 <c:if test="${ipsReviewReportDetails.participantNoticationAvailable ==true}">
											 <%-- <a href="/do/bob/investment/viewParticipantNotification/?action=ViewParticipantNotificationPDF&reviewRequestId=${ipsReviewReportDetails.reviewRequestId}&isFromLandingPage=true">
												Participant Notification
											</a>  --%> 
											<a href="javascript:doOutputSelect(${ipsReviewReportDetails.reviewRequestId}, 'true', 'true')" id="outputSelect"  >
											Participant Notification</a> 
 </c:if> 
								</td>
							</tr>
</c:forEach>
</c:if>
						<tr>
							<td colspan="5" align="right">
<c:if test="${ipsAndReviewDetailsForm.is338DesignateRia ==true}">

									<c:if test="${ipsAndReviewDetailsForm.interimReportLinkAvailable && ipsAndReviewDetailsForm.iPSAssistServiceAvailable}">
									<a href="javascript://" onClick="doPrintPDF();return false;">
										<span style="color:#005B80; text-decoration:underline">
							  					<content:getAttribute attribute="text" beanName="ipsAdhocReportLink" />
							  			</span>
							  		</a>
							  		</c:if>
</c:if>
							</td>
						</tr>
		</tbody>
	</table>
</div>
<div id="modalGlassPanel" class="modal_glass_panel" style="display:none;"></div>
<script language="JavaScript" type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>
