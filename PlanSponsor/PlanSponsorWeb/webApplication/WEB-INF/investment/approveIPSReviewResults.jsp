<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- Imports --%>
<%@ page import="java.util.HashMap"%>
<%@ page
	import="com.manulife.pension.platform.web.investment.valueobject.IPSFundInstructionPresentation"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
	<%@ page import="com.manulife.pension.ps.web.Constants" %>
	
<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="contractConstants"
	className="com.manulife.pension.service.contract.util.Constants" />
	
	
	
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
	

<script language="JavaScript" type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript">

function isFormChanged() {
	  return changeTracker.hasChanged();
}

registerTrackChangesFunction(isFormChanged);

function doSubmit(){
	document.iPSReviewResultsForm.action.value='cancel';
	document.iPSReviewResultsForm.submit();
}

function setAgreeApprovalCheckBoxValue(checkBoxObj){

if (checkBoxObj.checked){
		document.iPSReviewResultsForm.agreeApproval.value="on";
	} else {
		document.iPSReviewResultsForm.agreeApproval.value="";
	}
}

function doback(){
	if(discardChanges('The action you have selected will cause your changes to be lost. Select OK to continue or cancel to return.')){
		document.iPSReviewResultsForm.action.value='back';
		document.iPSReviewResultsForm.submit();
	}
}

function doOnload() {
	if(document.iPSReviewResultsForm.agreeApproval.value=="on"){
		document.getElementById("approvalCheckBox").checked = true;
	} 
}

</script>

<content:contentBean
	contentId="${contentConstants.IPS_IAT_EFFEXTIVE_DATE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsIATEffectiveDateText" />
<content:contentBean
	contentId="${contentConstants.IPS_APPROVE_CONFIRMATION_AS_OF_DATE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsApproveConfirmAsOfText" />
<content:contentBean
	contentId="${contentConstants.IPS_IAT_EFFECTIVE_DATE_DESC_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsIATEffectiveDateDescText" />
<content:contentBean
	contentId="${contentConstants.IPS_FUND_ACTION_APPROVE}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="ipsFundApproved" />
<content:contentBean
	contentId="${contentConstants.IPS_FUND_ACTION_IGNORE}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="ipsFundIgnored" />
<content:contentBean
	contentId="${contentConstants.IPS_FUND_ACTION_NOT_SELECTED}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="ipsFundNoAction" />
<content:contentBean
	contentId="${contentConstants.IPS_VIEW_CURRENT_REPORT_LINK}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsViewCurrentReportLink" />
<content:contentBean
	contentId="${contentConstants.IPS_PARTICIPANT_NOTIFICATION_PATH}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsParticipantNotificationLink" />
<content:contentBean
	contentId="${contentConstants.SAVE_HOVER_OVER_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="saveButtonHoverOverText" />
<content:contentBean
	contentId="${contentConstants.IPS_CONTRACT_LEVEL_REDEMPTION_FEES_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="contractLevelRedemptionFees" />
<content:contentBean
	contentId="${contentConstants.IPS_PARTICIPANT_LEVEL_REDEMPTION_FEES_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="participantLevelRedemptionFees" />
<content:contentBean
	contentId="${contentConstants.IPS_APPROVAL_ACTION_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="approvalActionText" />
<content:contentBean
	contentId="${contentConstants.IPS_TERMS_ANDCONDITION_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="termsAndConditionsText" />




<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100" rowspan="2"><img
			src="/assets/unmanaged/images/spacer.gif" width="15" height="1"
			border="0"></td>
		<td valign="top" class="greyText" width="500"><content:errors
			scope="request" /><br>
		</td>
		<td width="100" rowspan="2"><img
			src="/assets/unmanaged/images/spacer.gif" width="15" height="1"
			border="0"></td>
	</tr>
</table>

<ps:form method="POST" modelAttribute="iPSReviewResultsForm" name="iPSReviewResultsForm"
	action="/do/investment/approveIPSReviewResults/">
<input type="hidden" name="action" /><%--  input - name="iPSReviewResultsForm" --%>
<input type="hidden" name="formChanged" /><%--  input - name="iPSReviewResultsForm" --%>
<input type="hidden" name="mode" /><%--  input - name="iPSReviewResultsForm" --%>
<input type="hidden" name="agreeApproval" /><%--  input - name="iPSReviewResultsForm" --%>
	<br>
	<!-- IPS Review Results Section -->
	<% int numberOfColumns = 9; %>
	<br>
	<table width="700" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<!-- Start of body title -->
				<tr class="tablehead">
					<td class="tableheadTD1" colspan="<%=numberOfColumns%>">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="tableheadTD" width="50%" height="20"><Strong><content:getAttribute attribute="subHeader" beanName="layoutPageBean"/></Strong>
							 <render:date
								property="iPSReviewResultsForm.currentDate"
								patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue="" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<!-- End of body title -->

				<tr class="tablesubhead">
					<td class="databorder" width="1"></td>
					<td valign="middle" width="400" height="20" style="padding-left: 2px;"><b>Asset Class</b>
					</td>
					<td class="dataheaddivider" valign="bottom" width="1"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td valign="middle" width="450" height="20" style="padding-left: 2px;"><b>Current
					Fund</b></td>
					<td class="dataheaddivider" valign="bottom" width="1"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>					
					<td colspan=3 width="801">
						<table class="dataTable" width="100%" height="100%"
							cellspacing="0" cellpadding="2">
							<tr>
								<td width="58%" style="padding-left: 2px;">
									<b>Top-ranked Fund</b>
								</td>
								<td class="datadivider" width="1"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td width="42%" style="padding-left: 4px;">
									<b>Actions selected</b>
								</td>
							</tr>
						</table>
					<td class="databorder" width="1"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>

<c:if test="${not empty iPSReviewResultsForm.ipsReviewFundInstructionList}">

<c:forEach items="${iPSReviewResultsForm.ipsReviewFundInstructionList}" var="ipsReviewFundInstructions" varStatus="theIndex" >

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

						<%-- This logic is to differentiate the alternate rows --%>
<%-- 						<c:choose> --%>
<%-- 							<c:when test='${(theIndex) % 2 == 1}'> --%>
<!-- 								<tr class="datacell1"> -->
<%-- 							</c:when> --%>
<%-- 							<c:otherwise> --%>
<!-- 								<tr class="datacell2"> -->
<%-- 							</c:otherwise> --%>
<%-- 						</c:choose> --%>

						<td class="databorder" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td align="left" width="400" height="20" style="padding-left: 2px;">${ipsReviewFundInstructions.assetClassName}</td>

						<td class="datadivider" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="450" height="20">
						<table class="dataTable" width="100%" height="100%"
							cellspacing="0" cellpadding="0">

							<c:forEach var="fromFund"
								items="${ipsReviewFundInstructions.fromFundVO}">
								<tr>
									<td style="padding-left: 2px;"><c:if test="${empty param.printFriendly }" >
<c:if test="${fromFund.fundSheetLinkAvailable ==true}">

											<a href="#fundsheet"
												onMouseOver='self.status="Fund Sheet"; return true'
NAME='${fromFund.fundCode}' onClick='FundWindow("<ps:fundLink fundIdProperty="fromFund.fundCode" fundTypeProperty="fromFund.fundType" rateType ="userProfile.currentContract.defaultClass" productId ="<%=userProfile.getCurrentContract().getProductId()%>" fundSeries ='<%=userProfile.getCurrentContract().getFundPackageSeriesCode()%>' siteLocation='<%= Environment.getInstance().getSiteLocation() %>' />")'>

${fromFund.fundName} </a>
</c:if>

<c:if test="${fromFund.fundSheetLinkAvailable !=true}">

${fromFund.fundName}
</c:if>
									</c:if> <c:if test="${not empty param.printFriendly }" >
${fromFund.fundName}
</c:if>  
<c:if test="${fromFund.fundInformation != ''}">

										<img src="/assets/generalimages/info.gif" width="12"
											height="12" onmouseover='Tip("${fromFund.fundInformation}")'
											onmouseout="UnTip()" />
</c:if></td>
								</tr>
							</c:forEach>
						</table>
						</td>

						<td class="datadivider" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan=3>
						<table class="dataTable" width="100%" height="100%"
							cellspacing="0" cellpadding="2">
<c:forEach items="${ipsReviewFundInstructions.toFundVO}" var="toFund">

								<tr>
									<td width="58%" style="padding-left: 2px;">
									<c:if test="${ empty param.printFriendly}"> 
<c:if test="${toFund.fundSheetLinkAvailable ==true}">

											<a href="#fundsheet"
												onMouseOver='self.status="Fund Sheet"; return true'
NAME='${toFund.fundCode}' onClick='FundWindow("<ps:fundLink fundIdProperty="toFund.fundCode" fundTypeProperty="toFund.fundType" rateType ="userProfile.currentContract.defaultClass" productId ="<%=userProfile.getCurrentContract().getProductId()%>" fundSeries ='<%=userProfile.getCurrentContract().getFundPackageSeriesCode()%>' siteLocation='<%= Environment.getInstance().getSiteLocation() %>' />")'>

${toFund.fundName} </a>
</c:if>

<c:if test="${toFund.fundSheetLinkAvailable !=true}">

${toFund.fundName}
</c:if>
									</c:if> <c:if test="${not empty param.printFriendly }" >
${toFund.fundName}
</c:if>  
	<c:if test="${toFund.fundInformation != ''}">

										<img src="/assets/generalimages/info.gif" width="12"
											height="12" onmouseover='Tip("${toFund.fundInformation}")'
											onmouseout="UnTip()" />
</c:if></td>
									<td class="datadivider" width="1"><img
										src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td width="42%" style="padding-left: 4px;"><c:choose>
										<c:when
											test="${contractConstants.ACTION_APPROVED == toFund.actionIndicator}">
											<b> <content:getAttribute attribute="text"
												beanName="ipsFundApproved" /> </b>
											<br>
										</c:when>
										<c:when
											test="${contractConstants.ACTION_IGNORED == toFund.actionIndicator}">
											<b> <content:getAttribute attribute="text"
												beanName="ipsFundIgnored" /> </b>
											<br>
										</c:when>
										<c:otherwise>
											<b> <content:getAttribute attribute="text"
												beanName="ipsFundNoAction" /> </b>
											<br>
										</c:otherwise>
									</c:choose></td>
								</tr>
</c:forEach>
						</table>
						</td>
						<td class="databorder" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
</c:forEach>
</c:if>

				<!-- End of Last line -->

				<tr>
					<td class="databorder" width="100%" height="1"
						colspan="<%=numberOfColumns%>"></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="databorder" height="1" width="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" height="20"
						width="20" /></td>
					<td>
					<!--<b>Redemption Fees</b>--></td>
					<td class="databorder" height="1" width="1"></td>
				</tr>
				<tr>
					<td class="databorder" height="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" height="1"
						width="20" /></td>
					<td><c:if
						test="${iPSReviewResultsForm.contractRedemptionFeesAvailable}">
						<content:getAttribute attribute="text"
							beanName="contractLevelRedemptionFees" />
					</c:if></td>
					<td class="databorder" height="1" width="1"></td>
				</tr>
				<tr>
					<td class="databorder" height="1" width="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" height="1"
						width="20" /></td>
					<td><c:if
						test="${iPSReviewResultsForm.participantRedemptionFeesAvailable}">
						<content:getAttribute attribute="text"
							beanName="participantLevelRedemptionFees" />
					</c:if></td>
					<td class="databorder" height="1" width="1"></td>
				</tr>
				<tr>
					<td class="databorder" height="1" width="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" height="1"
						width="20" /></td>
					<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td align="right" valign="top"><br>
							<content:getAttribute attribute="text"
								beanName="ipsIATEffectiveDateDescText" /></td>
								<td width="3"/>
						</tr>
						<tr>
							<td align="right" ><br>
							<b>Effective Date:</b> 
<c:if test="${not empty iPSReviewResultsForm.ipsIatEffectiveDate}">

									<span class="highlightBold"> 
										<render:date patternIn="${renderConstants.MEDIUM_MDY_SLASHED}"
											property="iPSReviewResultsForm.ipsIatEffectiveDate"
											patternOut="${renderConstants.LONG_MDY}" defaultValue="" />
									</span>
</c:if> <c:if test="${empty iPSReviewResultsForm.ipsIatEffectiveDate}">

								<span class="highlightBold"> Not applicable </span>
</c:if></td>
							<td width="3"/>
						</tr>
					</table>
					</td>
					<td class="databorder" height="1" width="1"></td>
				</tr>
				<tr>
					<td class="databorder" height="1" width="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" height="1"
						width="20" /></td>
					<td><br>
					<content:getAttribute attribute="text"
						beanName="approvalActionText" /> <br>
					<br>
					</td>
					<td class="databorder" height="1" width="1"></td>
				</tr>
				<tr>
					<td class="databorder" height="1" width="1"></td>
					<td><img src="/assets/unmanaged/images/s.gif" height="1"
						width="20" /></td>
					<td style="padding-bottom: 10px;"><c:if test="${empty param.printFriendly }" >
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left" valign="top" width="1%">
									<input type="checkbox" id="approvalCheckBox" value="on" onChange= "javascript:setAgreeApprovalCheckBoxValue(this);"/>
								</td>
								<td align="left" valign="top" width="58%">
									<!-- <input type="checkbox" id="approvalCheckBox" value="on" onChange= "javascript:setAgreeApprovalCheckBoxValue(this);"/> --><content:getAttribute attribute="text"
										beanName="termsAndConditionsText" /><br />
									<ps:trackChanges escape="true" property="agreeApproval" name="iPSReviewResultsForm" />
								</td>
								<td align="right" valign="bottom" style="padding-right: 10px;"><br>
								<input type="button" name="button1" value="back"
									class="button100Lg"
									onclick="return doback('back')" /> &nbsp; 
								<input type="button"
									name="button3" value="submit approval" class="button134"
									onclick="return doSubmit()"
									onmouseover="<ps:isInternalUser name="userProfile" property="role">Tip('<content:getAttribute attribute="text" beanName="saveButtonHoverOverText"/>')</ps:isInternalUser>"
									onmouseout="UnTip()" />
								</td>
							</tr>
							<tr>
								<td><img src="/assets/unmanaged/images/s.gif" height="1"
									width="1" /></td>
							</tr>
						</table>
					</c:if>
					<c:if test="${not empty param.printFriendly }" >
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left" valign="top">
									<input type="checkbox" id="approvalCheckBox" value="on" disabled="true"/>
										<content:getAttribute attribute="text"
										beanName="termsAndConditionsText" />
									
								</td>
							</tr>
						</table>
					</c:if>
					</td>
					<td class="databorder" height="1" width="1"></td>
				</tr>
				<tr>
					<td class="databorder" height="1" width="1"
						colspan="<%=numberOfColumns%>"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

	<br>
	<br>
	<br>
	<c:if test="${not empty param.printFriendly}">
		<content:contentBean contentId="${contentConstants.GLOBAL_DISCLOSURE}"
			type="${contentConstants.TYPE_MISCELLANEOUS}" id="globalDisclosure" />

		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%"><content:getAttribute
					beanName="globalDisclosure" attribute="text" /></td>
			</tr>
		</table>
	</c:if>
</ps:form>
