<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="bdContentConstants" className="com.manulife.pension.bd.web.content.BDContentConstants" />
<un:useConstants var="contractConstants" className="com.manulife.pension.service.contract.util.Constants" />
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.platform.web.investment.valueobject.IPSFundInstructionPresentation"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@page import="com.manulife.pension.platform.web.util.ReportsXSLProperties"%>
<%@ page import="com.manulife.pension.service.contract.util.Constants"%>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<%--Contents Used--%>

<content:contentBean
	contentId="${bdContentConstants.IPS_VIEW_RESULT_PAGE_TITLE}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsViewResultsPageTitle" override="true" />
<content:contentBean
	contentId="${bdContentConstants.IPS_REVIEW_RESULT_REPORT_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsReviewResultReportLink" override="true" />
<content:contentBean
	contentId="${bdContentConstants.PARTICIPANT_NOTIFICATION_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsParticipantNotificationLink" override="true" />
<content:contentBean
	contentId="${bdContentConstants.IPS_IAT_EFFECTIVE_DATE_DETAILS}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsIATEffectiveDateDetails" override="true" />
<content:contentBean
	contentId="${bdContentConstants.IPS_FUND_ACTION_APPROVE}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}" id="ipsFundApproved"
	override="true" />
<content:contentBean
	contentId="${bdContentConstants.IPS_FUND_ACTION_IGNORE}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}" id="ipsFundIgnored"
	override="true" />
<content:contentBean
	contentId="${bdContentConstants.IPS_FUND_ACTION_NOT_SELECTED}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}" id="ipsFundNoAction"
	override="true" />
<content:contentBean
	contentId="${bdContentConstants.IPS_IAT_EFFECTIVE_DATE_DESC_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsIATEffectiveDateDescText" />
<content:contentBean
	contentId="${bdContentConstants.SAVE_HOVER_OVER_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="saveButtonHoverOverText" />
<content:contentBean
	contentId="${bdContentConstants.IPS_IAT_EFFEXTIVE_DATE_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsIATEffectiveDateText" />
<content:contentBean
	contentId="${bdContentConstants.IPS_APPROVE_CONFIRMATION_AS_OF_DATE_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsApproveConfirmAsOfText" />
<content:contentBean
	contentId="${bdContentConstants.IPS_VIEW_CURRENT_REPORT_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsViewCurrentReportLink" />
<content:contentBean
	contentId="${bdContentConstants.IPS_PARTICIPANT_NOTIFICATION_PATH}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsParticipantNotificationLink" />
<content:contentBean
	contentId="${bdContentConstants.IPS_CONTRACT_LEVEL_REDEMPTION_FEES_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="contractLevelRedemptionFees" />
<content:contentBean
	contentId="${bdContentConstants.IPS_PARTICIPANT_LEVEL_REDEMPTION_FEES_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="participantLevelRedemptionFees" />
<content:contentBean
	contentId="${bdContentConstants.IPS_APPROVAL_ACTION_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}" id="approvalActionText" />
<content:contentBean
	contentId="${bdContentConstants.IPS_TERMS_ANDCONDITION_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="termsAndConditionsText" />	

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

	
<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<script type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>

<script type="text/javascript">
function isFormChanged() {
	  return changeTracker.hasChanged();
}

registerTrackChangesFunction(isFormChanged);

function doSubmit(button){	
	document.editIPSReviewResultsForm.action.value=button;
	document.forms.editIPSReviewResultsForm.submit();
}

function setAgreeApprovalCheckBoxValue(checkBoxObj){

if (checkBoxObj.checked){
		document.editIPSReviewResultsForm.agreeApproval.value="on";
	} else {
		document.editIPSReviewResultsForm.agreeApproval.value="";
	}
}

function doback(button){
	if(discardChanges('The action you have selected will cause your changes to be lost. Select OK to continue or cancel to return.')){
		document.editIPSReviewResultsForm.action.value=button;
		document.forms.editIPSReviewResultsForm.submit();
	}
}

function doOnload() {
	if(document.editIPSReviewResultsForm.agreeApproval.value=="on"){
		document.getElementById("approvalCheckBox").checked = true;
	} 
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

</script>

<!-- Page Title and Headers-->


<h2>
	<content:getAttribute id="layoutPageBean" attribute="name" />
</h2>

<p class="record_info">
	<strong>${bobContext.contractProfile.contract.companyName}
		(${bobContext.contractProfile.contract.contractNumber})</strong> <input
		class="btn-change-contract" type="button"
		onmouseover="this.className +=' btn-change-contract-hover'"
		onmouseout="this.className='btn-change-contract'"
		onclick="top.location.href='/do/bob/blockOfBusiness/Active/'"
		value="Change contract">
</p>


<!--Layout/Intro1-->
<c:if test="${not empty layoutPageBean.introduction1}">
	<p class="record_info">
		<content:getAttribute beanName="layoutPageBean"
			attribute="introduction1" />
	</p>
</c:if>

<!--Layout/Intro2-->
<c:if test="${not empty layoutPageBean.introduction2}">
	<p class="record_info">
		<content:getAttribute beanName="layoutPageBean"
			attribute="introduction2" />
	</p>
</c:if>

<!--Error- message box-->
<report:formatMessages scope="request" />
<br />
<%-- Navigation bar --%>
<navigation:contractReportsTab />
<div class="report_table">
	<div class="page_section_subheader controls">

		<h3>
			<content:getAttribute beanName="layoutPageBean" attribute="subHeader" />
		</h3>
		<form class="page_section_filter form">
			<p>
				&nbsp;
				<render:date property="editIPSReviewResultsForm.asOfDate"
					patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue="" />
			</p>
		</form>
	</div>
</div>


<bd:form  id="editIPSReviewResultsForm" method="POST" action="/do/bob/investment/approveIPSReviewResults/" modelAttribute="editIPSReviewResultsForm" name="editIPSReviewResultsForm">

	
<form:hidden path="pdfCapped" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="reviewRequestId" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="formChanged" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="action" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="mode" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="agreeApproval" /><%--  input - name="editIPSReviewResultsForm" --%>

	<div class="report_table">
		<div class="clear_footer"></div>
		<table class="report_table_content">
			<thead>
				<tr>
					<th class="val_str" width="25%">Asset Class</th>
					<th class="val_str" width="25%">Current Fund</th>
					<th class="val_str" width="25%">Top-ranked Fund</th>
					<th class="val_str" width="25%">Actions selected</th>
				</tr>
			</thead>
			<tbody>
<c:if test="${not empty editIPSReviewResultsForm.ipsReviewFundInstructionList}">

<c:forEach items="${editIPSReviewResultsForm.ipsReviewFundInstructionList}" var="ipsReviewFundInstructions" varStatus="theIndex" >




						<c:if test="${not empty ipsReviewFundInstructions}">
							<%-- This logic is to differentiate the alternate rows --%>
							<c:choose>
								<c:when test='${(theIndex.index) % 2 == 1}'>
									<tr class="spec">
								</c:when>
								<c:otherwise>
									<tr class="spec1">
								</c:otherwise>
							</c:choose>

							<td class="name" valign="middle"><strong>${ipsReviewFundInstructions.assetClassName}</strong></td>
<td class="name" valign="middle"><c:forEach items="${ipsReviewFundInstructions.fromFundVO}" var="fromFund">


									<c:choose>
										<c:when test="${fromFund.fundSheetLinkAvailable}">
											<a href="#fundsheet"
												onMouseOver='self.status="JavaScript needed to open the fund page link"; return true'
												NAME="${fromFund.fundCode}"
												onClick='FundWindow("<bd:fundLink fundIdProperty="fromFund.fundCode" fundTypeProperty="fromFund.fundType" rateType ="bobContext.currentContract.defaultClass" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>
												${fromFund.fundName} </a>
										</c:when>
										<c:otherwise>${fromFund.fundName}</c:otherwise>
									</c:choose>
<c:if test="${not empty fromFund.fundInformation}">
										<img src="/assets/generalimages/info.gif" width="12"
											height="12" onmouseover="Tip('${fromFund.fundInformation}')"
											onmouseout="UnTip()" />
</c:if>
									<br>
</c:forEach></td>
						<td class="name" valign="middle">
<c:forEach items="${ipsReviewFundInstructions.toFundVO}" var="toFund">

								<c:choose>
									<c:when test="${toFund.fundSheetLinkAvailable}">
										<a href="#fundsheet" 
										onMouseOver='self.status="JavaScript needed to open the fund page link"; return true' NAME='${toFund.fundCode}' 
										onClick='FundWindow("<bd:fundLink fundIdProperty="toFund.fundCode" fundTypeProperty="toFund.fundType" rateType ="bobContext.currentContract.defaultClass" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>
											${toFund.fundName}
										</a>
									</c:when>
									<c:otherwise>
										${toFund.fundName}
									</c:otherwise>
								</c:choose>
<c:if test="${not empty toFund.fundInformation}">
									<img src="/assets/generalimages/info.gif" width="12" height="12" onmouseover="Tip('${toFund.fundInformation}')"
												onmouseout="UnTip()" />
</c:if><br>
</c:forEach>
						</td>
						<td class="name">
<c:forEach items="${ipsReviewFundInstructions.toFundVO}" var="fundInfo">

							<c:choose> 
							<c:when test="${fundInfo.actionIndicator == contractConstants.ACTION_APPROVED}">
								<b> <content:getAttribute attribute="text" beanName="ipsFundApproved"/> </b><br>
							</c:when>
							<c:when test="${fundInfo.actionIndicator == contractConstants.ACTION_IGNORED || contractConstants.SYSTEM_IGNORED == fundInfo.actionIndicator}">
								<b> <content:getAttribute attribute="text" beanName="ipsFundIgnored"/> </b><br>
							</c:when>
							<c:otherwise>
								<b> <content:getAttribute attribute="text" beanName="ipsFundNoAction"/> </b><br>
							</c:otherwise>
							</c:choose>
</c:forEach>
						</td>
</c:if>
</c:forEach>
</c:if>
			</tbody>
		</table>
	</div>

	<table border="0" align="right">
		<tr>
			<td width="100%"><c:if
				test="${editIPSReviewResultsForm.contractRedemptionFeesAvailable}">
				<content:getAttribute attribute="text"
					beanName="contractLevelRedemptionFees" />
			</c:if></td>
		</tr>
		<tr>
			<td width="100%"><c:if
				test="${editIPSReviewResultsForm.participantRedemptionFeesAvailable}">
				<content:getAttribute attribute="text"
					beanName="participantLevelRedemptionFees" />
			</c:if></td>
		</tr>
		<c:if test="${editIPSReviewResultsForm.ipsIATEffectiveDateAvailable}">
			<tr>
				<td align="right" valign="top" width="100%">
				<content:getAttribute attribute="text"
					beanName="ipsIATEffectiveDateDescText" /></td>
			</tr>
			<tr>
				<td align="right" width="100%">
				<b>Effective Date:</b> 
<c:if test="${not empty editIPSReviewResultsForm.ipsIatEffectiveDate}">
					<span class="highlightBold"> 
						<render:date patternIn="${renderConstants.MEDIUM_MDY_SLASHED}"
							property="editIPSReviewResultsForm.ipsIatEffectiveDate"
							patternOut="${renderConstants.LONG_MDY}" defaultValue="" />
					</span>
</c:if>
<c:if test="${empty editIPSReviewResultsForm.ipsIatEffectiveDate}">

					<span class="highlightBold"> Not applicable </span>
</c:if>
				</td>
			</tr>
		</c:if>
		<tr>
			<td width="100%">
			<content:getAttribute attribute="text"
				beanName="approvalActionText" />
			</td>
		</tr>
		<tr>
			<td align="left" valign="top" width="1%">
				<input type="checkbox" id="approvalCheckBox" value="on" onChange= "javascript:setAgreeApprovalCheckBoxValue(this);"/>
				<content:getAttribute attribute="text"
					beanName="termsAndConditionsText" /><br />
				<bd:trackChanges escape="true" property="agreeApproval" name="editIPSReviewResultsForm" />
			</td>
		</tr>
		<tr>
			<td align="right" width="100%">
				<input type="button" name="button1" value="back" class="button100Lg"
					onclick="return doback('back')" /> &nbsp; 
				<input type="button"
					name="button3" value="submit approval" class="button134"
					onclick="return doSubmit('submitConfirmation')" />
			</td>
		</tr>
		<tr>
			<td valign="bottom" width="100%"><content:getAttribute attribute="text"
					beanName="layoutPageBean" /></td>
		</tr>
	</table>
	
<script type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
	
</bd:form>
