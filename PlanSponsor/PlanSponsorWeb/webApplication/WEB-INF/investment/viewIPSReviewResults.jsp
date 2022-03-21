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
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="contractConstants"
	className="com.manulife.pension.service.contract.util.Constants" />

<script language="JavaScript" type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript">

function doSubmit(button){
	document.iPSReviewResultsForm.mode.value='editMode';
	document.iPSReviewResultsForm.action.value=button;
	document.forms.iPSReviewResultsForm.submit();
}

function doback(button){
	document.iPSReviewResultsForm.action.value=button;
	document.forms.iPSReviewResultsForm.submit();
}

function limitText(limitField, limitCount, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} else {
		limitCount.value = limitNum - limitField.value.length;
	}
}

</script>

<content:contentBean
	contentId="${contentConstants.NO_PREVIOUS_IPS_REPORT_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="noPreviousIPSReportText" />
<content:contentBean
	contentId="${contentConstants.IPS_IAT_EFFEXTIVE_DATE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsIATEffectiveDateText" />
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

<%-- <c:set var="userProfile" value="${USER_KEY}" /> --%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100" rowspan="2"><img
			src="/assets/unmanaged/images/spacer.gif" width="15" height="1"
			border="0"></td>
		<td valign="top" class="greyText" width="500"><content:errors
			scope="session" /><br>
		</td>
		<td width="100" rowspan="2"><img
			src="/assets/unmanaged/images/spacer.gif" width="15" height="1"
			border="0"></td>
	</tr>
</table>
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
<div id="ips_body"><ps:form
	method="POST" modelAttribute="iPSReviewResultsForm" name="iPSReviewResultsForm" action="/do/investment/viewIPSReviewResults/">
<input type="hidden" name="action" /><%--  input - name="iPSReviewResultsForm" --%>
<input type="hidden" name="mode" /><%--  input - name="iPSReviewResultsForm" --%>
	<!-- IPS Review Results Section -->
	<% int numberOfColumns = 9; %>
	<c:if test="${empty param.printFriendly}">	
		<c:if test='${iPSReviewResultsForm.currentReview}'>
			<c:if test='${iPSReviewResultsForm.reportLinkAvailable}'>					
				<a href="/do/investment/viewIPSReviewResults/?action=generateReviewReport&reviewRequestId=${iPSReviewResultsForm.reviewRequestId}">
				<content:getAttribute attribute="text"
					beanName="ipsViewCurrentReportLink">
					<content:param>
					${iPSReviewResultsForm.processingDateForReportLink}
					</content:param> 
				</content:getAttribute> </a>
				<br/>
			</c:if>
		</c:if>
		<c:if
			test="${iPSReviewResultsForm.participantNotificationAvailable}">
			<a href="javascript:doOutputSelect(${iPSReviewResultsForm.reviewRequestId}, 'false', 'false')" id="outputSelect"  >
											Participant Notification</a>
		<br/>
		</c:if>
		<br/>
	</c:if>
	
	<table width="100%" border="0"
		cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<table class="dataTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<!-- Start of body title -->
				<tr class="tablehead">
					<td class="tableheadTD1" colspan="<%=numberOfColumns%>">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="tableheadTD" width="50%" height="20"><Strong><content:getAttribute attribute="subHeader" beanName="layoutPageBean"/></Strong>
							 <render:date
								property="iPSReviewResultsForm.asOfDate"
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
					<td colspan="3" width="801">
					<table class="dataTable" width="100%" height="100%">
						<tr>
							<td valign="middle" width="56%" style="padding-left: 2px;"><b>Top-ranked Fund</b></td>
							<td class="dataheaddivider" valign="bottom" width="1"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td valign="middle" width="44%" style="padding-left: 2px;"><b>Actions selected</b></td>
						</tr>
					</table>
					</td>
					<td class="databorder" width="1"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>

<c:if test="${empty iPSReviewResultsForm.ipsReviewFundInstructionList}">

					<tr class="datacell1">
						<td class="databorder" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td align="left"></td>
						<td class="datadivider" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td><content:getAttribute attribute="text"
							beanName="noPreviousIPSReportText" /></td>
						<td class="datadivider" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td></td>
						<td class="datadivider" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td></td>
						<td class="datadivider" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
</c:if>

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
										<td style="padding-left: 2px;">
											<c:if test="${empty param.printFriendly}" >
<c:if test="${fromFund.fundSheetLinkAvailable ==true}">

													<a href="#fundsheet"
														onMouseOver='self.status="Fund Sheet"; return true'
NAME="${fromFund.fundCode}" onClick='FundWindow("<ps:fundLink fundIdProperty="fromFund.fundCode" fundTypeProperty="fromFund.fundType" rateType ="userProfile.currentContract.defaultClass" productId ="<%=userProfile.getCurrentContract().getProductId()%>" fundSeries ='<%=userProfile.getCurrentContract().getFundPackageSeriesCode()%>' siteLocation='<%= Environment.getInstance().getSiteLocation() %>' />")'>

${fromFund.fundName} </a>
</c:if>
				
<c:if test="${fromFund.fundSheetLinkAvailable !=true}">

${fromFund.fundName}
</c:if>
											</c:if>
				
											<c:if test="${not empty param.printFriendly}" >
${fromFund.fundName}
											</c:if>
 <c:if test="${not empty fromFund.fundInformation}">

												<img src="/assets/generalimages/info.gif" width="12" height="12"
													onmouseover='Tip("${fromFund.fundInformation}")'
													onmouseout="UnTip()" />
</c:if>
										</td>
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
									<td width="56%" style="padding-left: 2px;">
									<c:if test="${ empty param.printFriendly}">
<c:if test="${toFund.fundSheetLinkAvailable ==true}">

											<a href="#fundsheet"
												onMouseOver='self.status="Fund Sheet"; return true'
NAME="${toFund.fundCode}" onClick='FundWindow("<ps:fundLink fundIdProperty="toFund.fundCode" fundTypeProperty="toFund.fundType" rateType ="userProfile.currentContract.defaultClass" productId ="<%=userProfile.getCurrentContract().getProductId()%>" fundSeries ='<%=userProfile.getCurrentContract().getFundPackageSeriesCode()%>' siteLocation='<%= Environment.getInstance().getSiteLocation() %>' />")'>

${toFund.fundName} </a>
</c:if>

<c:if test="${toFund.fundSheetLinkAvailable !=true}">

${toFund.fundName}
</c:if>
									</c:if> <c:if test="${not empty param.printFriendly}" >
${toFund.fundName}
</c:if>  

<c:if test="${not empty toFund.fundInformation}">

										<img src="/assets/generalimages/info.gif" width="12"
											height="12" onmouseover='Tip("${toFund.fundInformation}")'
											onmouseout="UnTip()" />
</c:if></td>
									<td class="datadivider" width="1"><img
										src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td width="44%" style="padding-left: 2px;"><c:choose>
										<c:when
											test="${contractConstants.ACTION_APPROVED == toFund.actionIndicator}">
											<b> <content:getAttribute attribute="text"
												beanName="ipsFundApproved" /> </b>
											<br>
										</c:when>
										<c:when
											test="${contractConstants.ACTION_IGNORED == toFund.actionIndicator || contractConstants.SYSTEM_IGNORED == toFund.actionIndicator}">
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
				<br>
				<content:getAttribute beanName="layoutPageBean" attribute="footer1"/>					
			</td>
		</tr>
		<tr>
			<td>
				<c:if test="${empty param.printFriendly}" >	
					<table width="100%" border="0" cellspacing="0" cellpadding="0">			
						<tr>
							<td align="right" valign="top"><br>
							<content:getAttribute attribute="text"
								beanName="ipsIATEffectiveDateDescText" /></td>
							<!-- 
							 -->
							</tr>
							<tr>
							<td align="right"><br>
<b>Effective Date:</b> <c:if test="${not empty iPSReviewResultsForm.ipsIatEffectiveDate}">

								<span class="highlightBold"> 
									<render:date patternIn="${renderConstants.MEDIUM_MDY_SLASHED}"
													property="iPSReviewResultsForm.ipsIatEffectiveDate"
													patternOut="${renderConstants.LONG_MDY}" defaultValue="" />
								</span>
</c:if> <c:if test="${empty iPSReviewResultsForm.ipsIatEffectiveDate}">

								<span class="highlightBold"> Not applicable </span>
</c:if></td>
						</tr>
					</table>
				</c:if>
				<c:if test="${not empty param.printFriendly }" >	
					<table width="700" border="0" cellspacing="0" cellpadding="0">			
						<tr>
							<td align="right"><br>
							<content:getAttribute attribute="text"
								beanName="ipsIATEffectiveDateDescText" /></td>
							</tr>
							<tr>
							<td align="right"><br>
							<b><%-- <content:getAttribute attribute="text"
beanName="ipsIATEffectiveDateText" /> --%>Effective Date:</b> <c:if test="${not empty iPSReviewResultsForm.ipsIatEffectiveDate}">

								<span class="highlightBold"> 
									<render:date patternIn="${renderConstants.MEDIUM_MDY_SLASHED}"
													property="iPSReviewResultsForm.ipsIatEffectiveDate"
													patternOut="${renderConstants.LONG_MDY}" defaultValue="" />
								</span>
</c:if> <c:if test="${empty iPSReviewResultsForm.ipsIatEffectiveDate}">

								<span class="highlightBold"> Not applicable </span>
</c:if></td>
						</tr>
					</table>
				</c:if>
			</td>
		</tr>
		<tr>
			<td align="right" valign="bottom">
			<c:if test="${empty param.printFriendly}">
				<br>
				<input type="button" name="button1" value="back" class="button100Lg"
					onclick="return doback('back')" /> 
					<c:if test="${editAvailable}">
					&nbsp; 
					<input type="button" name="button3" value="edit" class="button100Lg"
						onclick="return doSubmit('edit')"
						onmouseover="<ps:isInternalUser name="userProfile" property="role">Tip('<content:getAttribute attribute="text" beanName="saveButtonHoverOverText"/>')</ps:isInternalUser>"
						onmouseout="UnTip()" />
				</c:if>
			</c:if></td>
		</tr>
	</table>

	<c:if test="${not empty param.printFriendly}">
	<br><br>
		<content:contentBean contentId="${contentConstants.GLOBAL_DISCLOSURE}"
			type="${contentConstants.TYPE_MISCELLANEOUS}" id="globalDisclosure" />

		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%"><content:getAttribute
					beanName="globalDisclosure" attribute="text" /></td>
			</tr>
		</table>
	</c:if>
</ps:form></div>
<div id="modalGlassPanel" class="modal_glass_panel"
	style="display: none"></div>
<div id="page_wrapper_footer">&nbsp;</div>
