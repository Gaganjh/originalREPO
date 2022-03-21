<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- Imports --%>
<%@ page import="java.util.HashMap"%>
<%@ page
	import="com.manulife.pension.platform.web.investment.valueobject.IPSFundInstructionPresentation"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@page import="com.manulife.pension.ps.web.util.Environment"%>

<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="contractConstants"
	className="com.manulife.pension.service.contract.util.Constants" />

<script  type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript">

var acceptRadioButtonArray = new Array();
var ignoreRadioButtonArray = new Array();

function loadNameIdArray(index){
	var acceptRadioName = "ipsReviewFundInstructionList["+index+"].toFundVO["+0+"].actionIndicator";
	var ignoreRadioName = "ipsReviewFundInstructionList["+index+"].toFundVO["+0+"].actionIndicator";
	
	acceptRadioButtonArray[acceptRadioButtonArray.length] = acceptRadioName;
	ignoreRadioButtonArray[ignoreRadioButtonArray.length] = ignoreRadioName;
	
}
registerWarningOnChangeToLinks(new Array("printReportLink_text","printReportLink_icon"));
function doSubmit(button){
	if(${iPSReviewResultsForm.ipsIATEffectiveDateAvailable}) {
		var field = document.getElementById("ipsIATEffectiveDate");
		if (!validateDate(field.value)) {
			field.value = "";				
	}
	}
	if (isFormChanged()){
		document.iPSReviewResultsForm.formChanged.value=true;
	} else {
		document.iPSReviewResultsForm.formChanged.value=false;
	}
	document.iPSReviewResultsForm.action.value=button;
	document.forms.iPSReviewResultsForm.submit();
}

function doback(button){
	
	if(discardChanges('The action you have selected will cause your changes to be lost. Select OK to continue or cancel to return.')){
		document.iPSReviewResultsForm.action.value=button;
		document.forms.iPSReviewResultsForm.submit();
	}
}

// Select all ignore or accept radio buttons. Funds which have multiple to funds should not get selected.
function selectAll(action){
	if ("A" == action){
		for(var i=0; i<acceptRadioButtonArray.length; i++) {		
			selectRadioButton(acceptRadioButtonArray[i], 0);
		}
	} else if ("I" == action) {
		for(var i=0; i<ignoreRadioButtonArray.length; i++) {
			selectRadioButton(ignoreRadioButtonArray[i], 1);
		}
	}	
}

// Java script to select the ignore radio button when the approve radio button is selected
function ignoreOtherInstructions(id, name){
	var formElements = document.getElementById("iPSReviewResultsForm").elements;				
	var selectedElement = document.getElementsByName(name);	
	
	
	if(selectedElement[0].checked==true){
		for (var i = 0; i < formElements.length; i++) {
			formElement = formElements.item(i);
			if (formElement.type == "radio") {			
				if(!formElement.disabled){
					if(formElement.name!=name && formElement.id==id){						
						selectRadioButton(formElement.name, 1);
					}				
				}			
			}
		}
	}
	
	deselectActionAll('ignoreAll');
}

// 0 for accept and 1 for ignore
function selectRadioButton(name, action){
	var radList= document.getElementsByName(name);
		
		if( !radList[action].disabled) {
		 radList[action].checked = true;
		}
}

function deselectActionAll(id) {
	document.getElementById(id).checked=false;
	
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
	contentId="${contentConstants.SAVE_HOVER_OVER_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="saveButtonHoverOverText" />
<content:contentBean
	contentId="${contentConstants.IPS_IAT_EFFECTIVE_DATE_DESC_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsIATEffectiveDateDescText" />

<c:set var="userProfile" value="${sessionScope.USER_KEY}" scope="session" />

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

<ps:form id="iPSReviewResultsForm" method="POST"
	action="/do/investment/editIPSReviewResults/" modelAttribute="iPSReviewResultsForm" name="iPSReviewResultsForm">
	<form:hidden path="action"/>
	<form:hidden path="formChanged"/>

	<br>
	<!-- IPS Review Results Section -->
	<% int numberOfColumns = 10; %>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<table width="100%" border="0" cellspacing="0"
				style="padding-left: 2px;">
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
					<td valign="middle" width="400" height="20"
						style="padding-left: 2px;"><b>Asset Class</b></td>
					<td class="dataheaddivider" valign="bottom" width="1"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td valign="middle" width="400" height="20"
						style="padding-left: 2px;"><b>Current Fund</b></td>
					<td class="dataheaddivider" valign="bottom" width="1"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td colspan=3 width="801">
						<table class="dataTable" border="0" width="100%" height="100%"
							cellspacing="0" cellpadding="0">
							<tr>
									<td width="55%" style="padding-left: 2px;"><b>Top-ranked Fund</b></td>
									<td class="datadivider" width="1"><img
										src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td width="5%" align="center" valign="middle" style="padding-top: 2px;"> 										
										<img src="/assets/unmanaged/images/s.gif" width="12" height="12" />
										
									</td>
									<td width="45%" style="padding-top: 2px;">
										
										<table width="100%" border="0" cellspacing="0">
											<tr>
												<td valign="middle" align="left" colspan="2">
													<b> <img src="/assets/unmanaged/images/s.gif" width="3" height="1" /> Your action</b>
												</td>
											</tr>
											<tr>
												<td valign="middle" align="left" width="50%"><input type="radio" name="actionIndAll" id="approveAll"	onclick="selectAll('A');" />Yes to all
												</td>
												<td valign="middle" align="left" width="50%">
													<input type="radio" name="actionIndAll" id="ignoreAll" onclick="selectAll('I');" />No to all
												</td>
											</tr>						
										</table>
									</td>
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
				<c:forEach items="${iPSReviewResultsForm.ipsReviewFundInstructionList}" var="ipsReviewFundInstructions" varStatus="theIndex">
				<c:set var="theIndex" value="${theIndex.index}"/>
						<c:set var="length"
							value="${fn:length(ipsReviewFundInstructions.toFundVO)}" />
						<c:if test="${length == 1}">
							<script type="text/javascript">							
								loadNameIdArray(${theIndex});
							</script>
						</c:if>

						<%-- This logic is to differentiate the alternate rows --%>
						<c:choose>
							<c:when test='${(theIndex) % 2 == 1}'>
								<tr class="datacell1">
							</c:when>
							<c:otherwise>
								<tr class="datacell2">
							</c:otherwise>
						</c:choose>

						<td class="databorder" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td align="left" width="400" height="20"
							style="padding-left: 2px;">${ipsReviewFundInstructions.assetClassName}</td>
						<td class="datadivider" width="1"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="450" height="20">
							<table class="dataTable" width="100%" height="100%"
								cellspacing="0" cellpadding="0">
								<c:forEach
									var="fromFund" items="${ipsReviewFundInstructions.fromFundVO}">
									<tr>
										<td style="padding-left: 2px;">
										<c:if test="${empty param.printFriendly}">
										<c:if test="${fromFund.fundSheetLinkAvailable ==true}">

													<a href="#fundsheet"
														onMouseOver='self.status="Fund Sheet"; return true'
NAME='${fromFund.fundCode}' onClick='FundWindow("<ps:fundLink fundIdProperty="fromFund.fundCode" fundTypeProperty="fromFund.fundType" rateType ="userProfile.currentContract.defaultClass" productId ="${userProfile.getCurrentContract().getProductId()}" fundSeries ="${userProfile.getCurrentContract().getFundPackageSeriesCode()}" siteLocation='<%= Environment.getInstance().getSiteLocation() %>' />")'>

${fromFund.fundName} </a>
</c:if>
				
													<c:if test="${fromFund.fundSheetLinkAvailable != true}">
													${fromFund.fundName}
												</c:if>
											</c:if>
												<c:if test="${not empty param.printFriendly}">
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

						<table class="dataTable" border="0" width="100%" height="100%"
							cellspacing="0" cellpadding="0">
							<c:set var="count" value="0" />
							<c:forEach var="toFund"
								items="${ipsReviewFundInstructions.toFundVO}">
								<c:set var="nameList" value="0" />
								<tr>
									<td width="55%" style="padding-left: 2px;">		
										<c:if test="${empty param.printFriendly}">
										<c:if test="${toFund.fundSheetLinkAvailable ==true}">

											<a href="#fundsheet"
												onMouseOver='self.status="Fund Sheet"; return true'
NAME='${toFund.fundCode}' onClick='FundWindow("<ps:fundLink fundIdProperty="toFund.fundCode" fundTypeProperty="toFund.fundType" rateType ="userProfile.currentContract.defaultClass" productId ="${userProfile.getCurrentContract().getProductId()}" fundSeries ="${userProfile.getCurrentContract().getFundPackageSeriesCode()}" siteLocation='<%= Environment.getInstance().getSiteLocation() %>' />")'>

${toFund.fundName} </a>
</c:if>
										<c:if test="${toFund.fundSheetLinkAvailable != true}">
											${toFund.fundName}
										</c:if>
									</c:if> 	<c:if test="${not empty param.printFriendly}">
											${toFund.fundName}
											</c:if>
				<c:if test="${not empty toFund.fundInformation}">
										<img src="/assets/generalimages/info.gif" width="12"
											height="12" onmouseover='Tip("${toFund.fundInformation}")'
											onmouseout="UnTip()" />
									</c:if></td>
									<td class="datadivider" width="1"><img
										src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td width="5%" align="center" valign="middle" style="padding-top: 2px;"> 										
										<c:if test="${toFund.noActionTaken}">
															<img src="/assets/unmanaged/images/error.gif"
														width="12" height="12" />
										</c:if>
									</td>
									<td width="45%" style="padding-top: 2px;">
										<table width="100%" border="0" cellspacing="0">
											<tr>
												
									<c:choose>
										<c:when test="${toFund.actionEnabled}">
											<c:set var="ind"
												value="${iPSReviewResultsForm.ipsReviewFundInstructionList[theIndex].toFundVO[count].actionIndicator}" />

											<c:if test="${ind == 'I'}">
												<td valign="middle" align="left" width="50%">
												<input
													name="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator"
													value="A" id="${theIndex}" type="radio"
													onClick="ignoreOtherInstructions(this.id, this.name)">
													Yes
												</td>
												<td valign="middle" align="left" width="50%">
														<input
													name="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator"
													value="I" id="${theIndex}" type="radio" onClick="deselectActionAll('approveAll')"
													 CHECKED>
													No
												<ps:trackChanges name="iPSReviewResultsForm" escape="true" name="iPSReviewResultsForm" property="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator" />
												</td>
											</c:if>
											<c:if test="${ind == 'A'}">
											<td valign="middle" align="left" width="50%">
												<input
													name="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator"
													value="A" id="${theIndex}" type="radio"
													onClick="ignoreOtherInstructions(this.id, this.name)"
													CHECKED>
													Yes
												</td>
												<td valign="middle" align="left" width="50%">
												
														<input
													name="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator"
													value="I" id="${theIndex}" type="radio" onClick="deselectActionAll('approveAll')" 
													>
													No												
												<ps:trackChanges escape="true" name="iPSReviewResultsForm" property="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator" />
												</td>
											</c:if>
											<c:if test="${empty ind}">
											<td valign="middle" align="left" width="50%">
												<input
													name="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator"
													value="A" id="${theIndex}" type="radio"
													onClick="ignoreOtherInstructions(this.id, this.name)">
													Yes
												</td>
												<td valign="middle" align="left" width="50%">
												
														<input
													name="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator"
													value="I" id="${theIndex}" type="radio" onClick="deselectActionAll('approveAll')"
													>
													No	
												<ps:trackChanges name="iPSReviewResultsForm" escape="true" property="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator" />		
												</td>
											</c:if>											
										</c:when>
										<c:otherwise>
											<c:set var="ind"
												value="${iPSReviewResultsForm.ipsReviewFundInstructionList[theIndex].toFundVO[count].actionIndicator}" />
											<c:if test="${ind == 'I'}">
												<td valign="middle" align="left" width="50%">
												<input
													name="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator"
													value="A" id="${theIndex}" type="radio" disabled="true"
													onClick="ignoreOtherInstructions(this.id, this.name)">
													Yes
												</td>
												<td valign="middle" align="left" width="50%">
												
														<input
													name="ipsReviewFundInstructionList[${theIndex}].toFundVO[${count}].actionIndicator"
													value="I" id="${theIndex}" type="radio" disabled="true"
													 CHECKED>
													No													
													</td>
													</c:if>
											<c:if test="${ind == 'A'}">
											<td valign="middle" align="left" width="50%">
												<input
													name="ipsReviewFundInstructionList[${theIndex}].toFund[${count}].actionIndicator"
													value="A" id="${theIndex}" type="radio" disabled="true"
													CHECKED>
													Yes
													</td>
												<td valign="middle" align="left" width="50%">
														<input
													name="ipsReviewFundInstructionList[${theIndex}].toFund[${count}].actionIndicator"
													value="I" id="${theIndex}" type="radio" disabled="true">
													No								
													</td>
													</c:if>
										</c:otherwise>
									</c:choose>
									</tr>
									</table>
									</td>
								</tr>
								<c:set var="count" value="${count+1}" />
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
			<td><br>
				<content:getAttribute beanName="layoutPageBean" attribute="footer1"/>
			</td>
		</tr>
		<tr>
			<td>
				<c:if test="${iPSReviewResultsForm.ipsIATEffectiveDateAvailable}">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td align="right" valign="top"><br>
							<content:getAttribute attribute="text"
								beanName="ipsIATEffectiveDateDescText" /></td>
						</tr>
						<tr>
							<td align="right"><br>
							<b>Effective Date:</b> 
							<input type="text" name="ipsIatEffectiveDate"	id="ipsIATEffectiveDate" cssClass="inputAmount" size="10"
							maxlength="10"/>	
							 <a href="javascript:doCalendar('${iPSReviewResultsForm.iatStartDate}');">
							<img src="/assets/unmanaged/images/cal.gif" border="0" ></a> 
							 <ps:trackChanges name="iPSReviewResultsForm"
							escape="true" property="ipsIatEffectiveDate" />
							</td>
						</tr>
					</table>
				</c:if>
			</td>
		</tr>
		<tr>
			<td align="right" valign="bottom"><br>
			<input type="button" name="button1" value="back" class="button100Lg"
				onclick="return doback('back')" /> &nbsp; <input type="button"
				name="button3" value="next" class="button100Lg"
				onclick="return doSubmit('next')"
				onmouseover="<ps:isInternalUser name="userProfile" property="role">Tip('<content:getAttribute attribute="text" beanName="saveButtonHoverOverText"/>')</ps:isInternalUser>"
				onmouseout="UnTip()" /></td>
		</tr>
	</table>

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
<script type="text/javascript">
	if(${iPSReviewResultsForm.ipsIATEffectiveDateAvailable}) {
		var validDates = ${iPSReviewResultsForm.validDatesForJavaScript};
		var startDate = validDates[0];	
		var endDate = validDates[validDates.length-1];
		var currentDate = validDates[0];
		var cal = new calendar(document.forms['iPSReviewResultsForm'].elements['ipsIatEffectiveDate'], startDate.valueOf(), endDate.valueOf(), validDates);
		cal.year_scroll = false;
		cal.time_comp = false;
	}
function doCalendar(date) {
	var s = date;
	var v = document.forms['iPSReviewResultsForm'].elements['ipsIatEffectiveDate'].value;
		if (v == null || v.length == 0) {
		    document.forms['iPSReviewResultsForm'].elements['ipsIatEffectiveDate'].value = date;
		}
			   
		cal.popup();
	}

</script>