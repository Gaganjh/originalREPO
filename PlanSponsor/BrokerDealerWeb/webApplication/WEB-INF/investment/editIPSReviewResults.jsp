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
<%@ page import="com.manulife.pension.content.valueobject.Miscellaneous" %>
<%@ page import="com.manulife.pension.content.valueobject.BDForm" %>

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
	contentId="${bdContentConstants.IPS_SERVICE_BROCHURE_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsServiceBrochureLink" override="true" />
<content:contentBean
	contentId="${bdContentConstants.IPS_SERVICE_BROCHURE_PATH}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsServiceBrochurePath" />	
<content:contentBean
	contentId="${bdContentConstants.IPS_GUIDE_PATH}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsGuidePath" />
<content:contentBean 
    contentId="${bdContentConstants.INVESTMENT_POLICY_STATEMENT_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsInvestmentPolicyStatementLink" override="true" />	       
<content:contentBean
	contentId="${bdContentConstants.CIA_FORM}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ciaFormLink" override="true" />
<content:contentBean contentId="${bdContentConstants.CIA_FRW_IS_NML}"
	type="${bdContentConstants.BD_FORM}" id="ciaNML"
	override="true" />
<content:contentBean
	contentId="${bdContentConstants.CIA_FRW_IS_NOT_NML}"
	type="${bdContentConstants.BD_FORM}" id="ciaNonNML"
	override="true" />

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

	
<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<script language="JavaScript" type="text/javascript"
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
//registerWarningOnChangeToLinks(new Array("printReportLink_text","printReportLink_icon"));
function doSubmit(button){
	if(${editIPSReviewResultsForm.ipsIATEffectiveDateAvailable}) {
		var field = document.getElementById("ipsIATEffectiveDate");
		if (!validateDate(field.value)) {
			field.value = "";				
	}
	}
	if (isFormChanged()){
		document.editIPSReviewResultsForm.formChanged.value=true;
	} else {
		document.editIPSReviewResultsForm.formChanged.value=false;
	}
	document.editIPSReviewResultsForm.action.value=button;
	document.forms.editIPSReviewResultsForm.submit();
}

function doback(button){
	if(discardChanges('The action you have selected will cause your changes to be lost. Select OK to continue or cancel to return.')){
		//document.location='/do/bob/investment/ipsManager/';
		document.editIPSReviewResultsForm.action.value=button;
		document.forms.editIPSReviewResultsForm.submit();
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
	var formElements = document.getElementsByName("editIPSReviewResultsForm")[0].elements;				
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

<!-- Page Title and Headers-->
<%--Summary Box--%>
<div id="summaryBox" style="width: 300px;">
<h1>Additional Resources</h1>
<a href="#anchor1" style="font-size: 11px;"
	onclick="javascript:PDFWindow('<%=((Miscellaneous)ipsServiceBrochurePath).getText()%>')">
	<content:getAttribute attribute="text" beanName="ipsServiceBrochureLink" />
</a>
<br>
<c:choose>
<c:when test="${bobContext.contractProfile.contract.nml}">
<c:choose>
<c:when test="${bobContext.contractProfile.contract.companyCode == '019'}">
<a href="#anchor1" style="font-size: 11px;"
	onclick="javascript:PDFWindow('<%=((BDForm)ciaNML).getEnglishPDFForm().getPath()%>')">
	<span style="color:#005B80; text-decoration:underline"><content:getAttribute attribute="text" beanName="ciaFormLink" /></span>
</a>
</c:when>
<c:otherwise>
<a href="#anchor1" style="font-size: 11px;"
	onclick="javascript:PDFWindow('<%=((BDForm)ciaNML).getNyEnglishPDFForm().getPath()%>')">
	<span style="color:#005B80; text-decoration:underline"><content:getAttribute attribute="text" beanName="ciaFormLink" /></span>
</a>
</c:otherwise>
</c:choose>
</c:when><c:otherwise>
<c:choose>
<c:when test="${bobContext.contractProfile.contract.companyCode == '019'}">
<a href="#anchor1" style="font-size: 11px;"
	onclick="javascript:PDFWindow('<%=((BDForm)ciaNonNML).getEnglishPDFForm().getPath()%>')">
	<span style="color:#005B80; text-decoration:underline"><content:getAttribute attribute="text" beanName="ciaFormLink" /></span>
</a>
</c:when>
<c:otherwise>
<a href="#anchor1" style="font-size: 11px;"
	onclick="javascript:PDFWindow('<%=((BDForm)ciaNonNML).getNyEnglishPDFForm().getPath()%>')">
	<span style="color:#005B80; text-decoration:underline"><content:getAttribute attribute="text" beanName="ciaFormLink" /></span>
</a>
</c:otherwise>
</c:choose>
</c:otherwise>
</c:choose>
<br/>
<a href="#anchor1" style="font-size: 11px;"
	onclick="javascript:PDFWindow('<%=((Miscellaneous)ipsGuidePath).getText()%>')">
	<content:getAttribute attribute="text" beanName="ipsInvestmentPolicyStatementLink" />
</a>
</div>

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


<bd:form method="POST" action="/do/bob/investment/editIPSReviewResults/" id="editIPSReviewResultsForm" modelAttribute="editIPSReviewResultsForm" name="editIPSReviewResultsForm">

	
<form:hidden path="reviewRequestId" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="formChanged" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="action" /><%--  input - name="editIPSReviewResultsForm" --%>

	<style type="text/css">
		#buttoncalendar {
			font-size:16px !important;
			width: 170px !important;
		}
	</style>
	
	<div class="report_table">
		<div class="clear_footer"></div>
		<table class="report_table_content">
			<thead>
				<tr>
					<th class="val_str" width="25%">Asset Class</th>
					<th class="val_str" width="25%">Current Fund</th>
					<th class="val_str" width="25%">Top-ranked Fund</th>
					<th class="val_str" width="25%"
						style="padding-left: 33px;" align="center">Your Action <br>
						<span><input type="radio" name="actionIndAll"
							id="approveAll" onclick="selectAll('A');" />Yes to all</span> <span><input
							type="radio" name="actionIndAll" id="ignoreAll"
							onclick="selectAll('I');" />No to all</span>
					</th>
				</tr>
			</thead>
			<tbody>
<c:if test="${not empty editIPSReviewResultsForm.ipsReviewFundInstructionList}">

<c:forEach items="${editIPSReviewResultsForm.ipsReviewFundInstructionList}" var="ipsReviewFundInstructions" varStatus="theIndex" >

<c:set var ="indx" value="${theIndex.index}"/>

						<c:set var="length"
							value="${fn:length(ipsReviewFundInstructions.toFundVO)}" />
						<c:if test="${length == 1}">
							<script type="text/javascript">							
					loadNameIdArray(${theIndex.index});
				</script>
						</c:if>

						<c:if test="${not empty ipsReviewFundInstructions}">
							<%-- This logic is to differentiate the alternate rows --%>
							<c:choose>
								<c:when test="${(theIndex.index) % 2 == 1}">
									<tr class="spec">
								</c:when>
								<c:otherwise>
									<tr class="spec1">
								</c:otherwise>
							</c:choose>

							<td class="name" valign="middle"><strong>${ipsReviewFundInstructions.assetClassName}</strong></td>
<td class="name" valign="middle"><c:forEach items="${ipsReviewFundInstructions.fromFundVO}" var="fromFund" varStatus="theIndex">


									<c:choose>
										<c:when test="${fromFund.fundSheetLinkAvailable}">
											<a href="#fundsheet"
												onMouseOver='self.status="JavaScript needed to open the fund page link"; return true'
												NAME="${fromFund.fundCode}"
												onClick='FundWindow("<bd:fundLink fundIdProperty="fromFund.fundCode" fundTypeProperty="fromFund.fundType" rateType ="bobContext.currentContract.defaultClass" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>
												${fromFund.fundName} </a>
										</c:when>
										<c:otherwise>
						${fromFund.fundName}
					</c:otherwise>
									</c:choose>
<c:if test="${not empty fromFund.fundInformation}">
										<img src="/assets/generalimages/info.gif" width="12"
											height="12" onmouseover="Tip('${fromFund.fundInformation}')"
											onmouseout="UnTip()" />
</c:if>
									<br>
</c:forEach></td>
<td class="name" valign="middle"><c:forEach items="${ipsReviewFundInstructions.toFundVO}" var="toFund" varStatus="theIndex">

									<c:choose>
										<c:when test="${toFund.fundSheetLinkAvailable}">
											<a href="#fundsheet"
												onMouseOver='self.status="JavaScript needed to open the fund page link"; return true'
												NAME="${toFund.fundCode}"
												onClick='FundWindow("<bd:fundLink fundIdProperty="toFund.fundCode" fundTypeProperty="toFund.fundType" rateType ="bobContext.currentContract.defaultClass" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>
												${toFund.fundName} </a>
										</c:when>
										<c:otherwise>
						${toFund.fundName}
					</c:otherwise>
									</c:choose>
<c:if test="${not empty toFund.fundInformation}">
										<img src="/assets/generalimages/info.gif" width="12"
											height="12" onmouseover="Tip('${toFund.fundInformation}')"
											onmouseout="UnTip()" />
</c:if>
									<br>
</c:forEach></td>
							<td class="name" valign="middle" style="padding-left: 33px;">
							<c:set var="count" value="0" />
<c:forEach items="${ipsReviewFundInstructions.toFundVO}" var="fundInfo" >

								<c:choose>
									<c:when test="${fundInfo.actionEnabled}">
										<c:set var="ind"
											value="${editIPSReviewResultsForm.ipsReviewFundInstructionList[theIndex].toFundVO[count].actionIndicator}" />

										<c:if test="${ind == 'I'}">
											<span>
											<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator"value="A" id="${indx}" 
												onClick="ignoreOtherInstructions(this.id, this.name)"/>
												Yes
											</span>
											<span style="padding-left: 28px;">
													<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator"
												value="I" id="${indx}"  onClick="deselectActionAll('approveAll')"
												 CHECKED/>
												No
											 <bd:trackChanges name="editIPSReviewResultsForm" escape="true" property="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator" />
											</span>
										</c:if>
										<c:if test="${ind == 'A'}">
										<span>
										<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator"
												value="A" id="${indx}" 
												onClick="ignoreOtherInstructions(this.id, this.name)"
												CHECKED/>
												Yes
											
											</span>
											<span style="padding-left: 28px;">
											<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator"
												value="I" id="${indx}"  onClick="deselectActionAll('approveAll')" 
												/>
												No		
											 <bd:trackChanges name="editIPSReviewResultsForm" escape="true" property="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator" /> 
											</span>
										</c:if>
										<c:if test="${empty ind}">
										<span>
										<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator"
												value="A" id="${indx}" 
												onClick="ignoreOtherInstructions(this.id, this.name)" />
												Yes
											</span>
											<span style="padding-left: 28px;">
											<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator"
												value="I" id="${indx}"  onClick="deselectActionAll('approveAll')"
												/>
												No	
													
											<bd:trackChanges name="editIPSReviewResultsForm" escape="true" property="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator" /> 		
											</span>
										</c:if>											
									</c:when>
									<c:otherwise>
										<c:set var="ind"
											value="${editIPSReviewResultsForm.ipsReviewFundInstructionList[theIndex].toFundVO[count].actionIndicator}" />
										<c:if test="${ind == 'I'}">
											<span>
											<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator"
												value="A" id="${indx}"  disabled="true"
												onClick="ignoreOtherInstructions(this.id, this.name)"/>
												Yes
											
											</span>
											<span style="padding-left: 28px;">
											<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator"
												value="I" id="${indx}"  disabled="true"
												 CHECKED/>
												No		
												</span>
												</c:if>
										<c:if test="${ind == 'A'}">
										<span>
										<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFund[${count}].actionIndicator"
												value="A" id="${indx}"  disabled="true"
												CHECKED/>
												Yes
											
												</span>
											<span style="padding-left: 28px;">
											<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFund[${count}].actionIndicator"
												value="I" id="${indx}"  disabled="true"/>
												No		
												</span>
												</c:if>
												<c:if test="${empty ind}">
										<span>
										<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator"
												value="A" id="${indx}" 
												onClick="ignoreOtherInstructions(this.id, this.name)" disabled="true" />
												Yes
											</span>
											<span style="padding-left: 28px;">
											<form:radiobutton path="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator"
												value="I" id="${indx}"  onClick="deselectActionAll('approveAll')"  disabled="true"
												/> 
												No	
													
											<bd:trackChanges name="editIPSReviewResultsForm" escape="true" property="ipsReviewFundInstructionList[${indx}].toFundVO[${count}].actionIndicator" /> 		
											</span>
										</c:if>	
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
		<c:if test="${editIPSReviewResultsForm.ipsIATEffectiveDateAvailable}">
			<tr>
				<td align="right" valign="top">
				<content:getAttribute attribute="text"
					beanName="ipsIATEffectiveDateDescText" /></td>
			</tr>
			<tr>
				<td align="right">
				<b">Effective Date:</b> 
				<span style="width: 90px">
				<c:choose>
					<c:when test="${editIPSReviewResultsForm.ipsIatEffectiveDate}">
					<%-- <form:input type="text" path="ipsIatEffectiveDate" maxlength="10" size="8" id="ipsIATEffectiveDate" value="${editIPSReviewResultsForm.ipsIatEffectiveDate}" /> --%>
					<input type="text" name="ipsIatEffectiveDate"	id="ipsIATEffectiveDate" cssClass="inputAmount" size="10"
							maxlength="10"/>
					</c:when>
					<c:otherwise>
					<input type="text" name="ipsIatEffectiveDate"	id="ipsIATEffectiveDate" cssClass="inputAmount" size="10"
							maxlength="10"/>
					<%-- <form:input type="text" path="ipsIatEffectiveDate" maxlength="10" size="8" id="ipsIATEffectiveDate" value="${editIPSReviewResultsForm.iatStartDate}" /> --%>

					</c:otherwise>
				</c:choose>
				</span>
					<div style="float: right;"><utils:btnCalendar
						dateField="ipsIATEffectiveDate" calendarcontainer="calendarcontainer"
						datefields="datefields" calendarpicker="calendarpicker"/></div>
					 <bd:trackChanges
					escape="true" property="ipsIatEffectiveDate"
					name="editIPSReviewResultsForm" /> 
				</td>
			</tr>
		</c:if>
		<tr>
			<td align="right">
				<input type="button" name="button1" value="back" class="button100Lg"
					onclick="return doback('back')" /> &nbsp; 
				<input type="button" name="button3" value="next" class="button100Lg"
					onclick="return doSubmit('next')" />
			</td>
		</tr>
		<tr>
			<td valign="bottom"><content:getAttribute attribute="text"
					beanName="layoutPageBean" /></td>
		</tr>
	</table>
	
<script type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
	
</bd:form>

<br><br><br>
    
    <div align="left">
         <dl>
             <dd><content:pageFooter beanName="layoutPageBean"/></dd> 
         </dl>     
        <dl>
             <dd><content:pageFootnotes beanName="layoutPageBean"/></dd> 
        </dl>
        <dl>
            <dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd>
        </dl>
        <div class="footnotes_footer"></div>
    </div> <!--#footnotes-->
