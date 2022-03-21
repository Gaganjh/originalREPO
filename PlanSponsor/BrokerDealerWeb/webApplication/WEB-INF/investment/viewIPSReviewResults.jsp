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
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.Miscellaneous" %>
<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="bdContentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />
<un:useConstants var="contractConstants"
	className="com.manulife.pension.service.contract.util.Constants" />
	<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
	<%@ page
	import="com.manulife.pension.platform.web.investment.valueobject.IPSFundInstructionPresentation"%>
	<%@ page import="com.manulife.util.render.RenderConstants"%>
	<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
	<%@page import="com.manulife.pension.platform.web.util.ReportsXSLProperties" %>
	<%@ page import="com.manulife.pension.service.contract.util.Constants"%>
<%--Contents Used--%>

<content:contentBean contentId="${bdContentConstants.IPS_VIEW_RESULT_PAGE_TITLE}"
                               type="${bdContentConstants.TYPE_MISCELLANEOUS}"
                               id="ipsViewResultsPageTitle" override="true"/> 
<content:contentBean contentId="${bdContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT}"
       						   type="${bdContentConstants.TYPE_MISCELLANEOUS}"
       						   id="pdfIcon"/>
<content:contentBean contentId="${bdContentConstants.IPS_REVIEW_RESULT_REPORT_LINK}"
							type="${bdContentConstants.TYPE_MISCELLANEOUS}" 
							id="ipsReviewResultReportLink" override="true"/>
<content:contentBean contentId="${bdContentConstants.PARTICIPANT_NOTIFICATION_LINK}"
							type="${bdContentConstants.TYPE_MISCELLANEOUS}" 
							id="ipsParticipantNotificationLink" override="true"/>
<content:contentBean contentId="${bdContentConstants.IPS_IAT_EFFECTIVE_DATE_DETAILS}"
							type="${bdContentConstants.TYPE_MISCELLANEOUS}" 
							id="ipsIATEffectiveDateDetails" override="true"/>
<content:contentBean contentId="${bdContentConstants.IPS_FUND_ACTION_APPROVE}"
							type="${bdContentConstants.TYPE_MISCELLANEOUS}" 
							id="ipsFundApproved" override="true"/>
<content:contentBean contentId="${bdContentConstants.IPS_FUND_ACTION_IGNORE}"
							type="${bdContentConstants.TYPE_MISCELLANEOUS}" 
							id="ipsFundIgnored" override="true"/>
<content:contentBean contentId="${bdContentConstants.IPS_FUND_ACTION_NOT_SELECTED}"
							type="${bdContentConstants.TYPE_MISCELLANEOUS}" 
							id="ipsFundNoAction" override="true"/>
<content:contentBean contentId="${bdContentConstants.IPS_GUIDE_PATH}"
	                        type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	                        id="ipsGuidePath" />		
<content:contentBean contentId="${bdContentConstants.INVESTMENT_POLICY_STATEMENT_LINK}"
	                        type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	                        id="ipsInvestmentPolicyStatementLink" override="true" />	 
<content:contentBean contentId="${bdContentConstants.IPS_SERVICE_BROCHURE_LINK}"
	                        type="${bdContentConstants.TYPE_MISCELLANEOUS}"
                            id="ipsServiceBrochureLink" override="true" />			
<content:contentBean contentId="${bdContentConstants.IPS_SERVICE_BROCHURE_PATH}"
	                        type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	                        id="ipsServiceBrochurePath" />	                        				

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>



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
              
    }
div.modal_dialog th, td{
        font-size:11px !important;
    }
</style>
           
<script language="JavaScript" type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>
             
<script type="text/javascript">

function doPrintPDF(maxRowsAllowedInPDF) {
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

function doSubmit(button){
	document.editIPSReviewResultsForm.mode.value='editMode';
	document.editIPSReviewResultsForm.action.value=button;
	document.forms.editIPSReviewResultsForm.submit();
}

function doback(button){
	document.editIPSReviewResultsForm.action.value=button;
	document.forms.editIPSReviewResultsForm.submit();
}

function limitText(limitField, limitCount, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} else {
		limitCount.value = limitNum - limitField.value.length;
	}
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
<br/>
<a href="#anchor1" style="font-size: 11px;"
	onclick="javascript:PDFWindow('<%=((Miscellaneous)ipsGuidePath).getText()%>')">
	<content:getAttribute attribute="text" beanName="ipsInvestmentPolicyStatementLink" />
</a>
</div>

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
		
<div id="participantNotificationSection" style="display:none">
 <bd:form method="POST" action="/do/bob/investment/viewParticipantNotification/">
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

<bd:form id="editIPSReviewResultsForm" method="POST" action="/do/bob/investment/viewIPSReviewResults/" modelAttribute="editIPSReviewResultsForm" name="editIPSReviewResultsForm">



<form:hidden path="pdfCapped" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="reviewRequestId" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="mode" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="action" /><%--  input - name="editIPSReviewResultsForm" --%>
<p>
<c:if test="${editIPSReviewResultsForm.currentReview && editIPSReviewResultsForm.reportLinkAvailable}">
	<a style="text-decoration: underline; font-weight: normal; color: #002c3d; font-size: 11px; outline-style: none;" href="/do/bob/investment/viewIPSReviewResults/?action=generateReviewReport&reviewRequestId=${editIPSReviewResultsForm.reviewRequestId}" ><content:getAttribute attribute="text" 	beanName="ipsReviewResultReportLink">
			<content:param>
				${editIPSReviewResultsForm.processingDateForReportLink}
			</content:param>
		</content:getAttribute>
	</a><br>
</c:if>
<c:if test="${editIPSReviewResultsForm.participantNotificationAvailable}">
	<%-- <a style="text-decoration: underline; font-weight: normal; color: #002c3d; font-size: 11px; outline-style: none;"
		href="/do/bob/investment/viewParticipantNotification/?action=viewParticipantNotificationPDF&reviewRequestId=${editIPSReviewResultsForm.reviewRequestId}&isFromLandingPage=false"><content:getAttribute
		attribute="text" beanName="ipsParticipantNotificationLink" /></a> --%>
		<a style="text-decoration: underline; font-weight: normal; color: #002c3d; font-size: 11px; outline-style: none;" href="javascript:doOutputSelect(${editIPSReviewResultsForm.reviewRequestId}, 'false', 'true')" id="outputSelect"  >
											Participant Notification</a>
</c:if>	
</p>
<!--Error- message box-->  
  <report:formatMessages scope="request"/><br/>
  
  
<%-- Navigation bar --%>
<navigation:contractReportsTab />

<div class="report_table">
<div class="page_section_subheader controls">
	
	<h3><content:getAttribute beanName="layoutPageBean" attribute="subHeader"/></h3>
	<form class="page_section_filter form">
	<p style="display: inline-block; padding-top: 3px;">
		<render:date property="editIPSReviewResultsForm.asOfDate"
	         patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue="" />
	</p>
	</form>
   	<a href="javascript://" onClick="doPrintPDF(<%=ReportsXSLProperties.get(BDConstants.MAX_CAPPED_ROWS_IN_PDF)%>)"  class="pdf_icon"  
   	   title="<content:getAttribute beanName="pdfIcon" attribute="text"/>">
   	   <content:image contentfile="image" id="pdfIcon" /> </a>
</div>
</div>

<div class="report_table">
<div class="clear_footer"></div>
	<table class="report_table_content" >
		<thead>
			<tr>
				<th class="val_str" width="25%">Asset Class</th>
				<th class="val_str" width="25%">Current Fund</th>
				<th class="val_str" width="25%">Top-ranked Fund</th>
				<th class="val_str" width="25%">Actions selected by Trustee</th>
			</tr>
		</thead>
		<tbody>
<c:if test="${not empty editIPSReviewResultsForm.ipsReviewFundInstructionList}">

					
		
<c:forEach items="${editIPSReviewResultsForm.ipsReviewFundInstructionList}" var="ipsReviewFundInstructions" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/>

			
			<c:if test="${not empty ipsReviewFundInstructions}">
			<%-- This logic is to differentiate the alternate rows --%>
			<c:choose>
				<c:when test='${(indexValue) % 2 == 1}'>
					<tr class="spec">
				</c:when>
				<c:otherwise>
					<tr class="spec1">
				</c:otherwise>
			</c:choose>
			
			<td class="name" valign="middle"><strong>${ipsReviewFundInstructions.assetClassName}</strong></td>
			<td class="name" valign="middle">
<c:forEach items="${ipsReviewFundInstructions.fromFundVO}" var="fromFund">

				<c:choose>
					<c:when test="${fromFund.fundSheetLinkAvailable}">
						<a href="#fundsheet" onMouseOver='self.status="JavaScript needed to open the fund page link"; return true' NAME='${fromFund.fundCode}' onClick='FundWindow("<bd:fundLink fundIdProperty="fromFund.fundCode" fundTypeProperty="fromFund.fundType" rateType ="bobContext.currentContract.defaultClass" fundSeries ="${bobContext.currentContract.fundPackageSeriesCode}" productId ="${bobContext.currentContract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>
							${fromFund.fundName}
						</a>
					</c:when>
					<c:otherwise>
						${fromFund.fundName}
					</c:otherwise>
				</c:choose>
				<c:if test="${not empty fromFund.fundInformation}">
					<img src="/assets/generalimages/info.gif" width="12" height="12" onmouseover="Tip('${fromFund.fundInformation}')"
								onmouseout="UnTip()" />
</c:if><br>
</c:forEach>
			</td>
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
			</tr>
</c:if>
</c:forEach>
</c:if>
		</tbody>
	</table>
	</div>
	
	<table border="0" align="right">
		<tr>
			<td align="right">
				<content:getAttribute attribute="text" beanName="ipsIATEffectiveDateDetails"/> 
			</td>
		</tr>
		<tr>
			<td align="right" >
			Effective Date:
<c:if test="${not empty editIPSReviewResultsForm.ipsIatEffectiveDate}">
				<strong><render:date patternIn="${renderConstants.MEDIUM_MDY_SLASHED}"
											property="editIPSReviewResultsForm.ipsIatEffectiveDate"
											patternOut="${renderConstants.LONG_MDY}" defaultValue="" /></strong>
				
</c:if>
<c:if test="${empty editIPSReviewResultsForm.ipsIatEffectiveDate}">
				<strong>Not selected</strong>
</c:if>
			</td>
		</tr>
		<tr>
			<td align="right">
<input type="button" onclick="return doback('back')" name="button" class="button100Lg" value="back"/>

			 <c:if test="${editAvailable}">
				&nbsp; 
<input type="button" onclick="return doSubmit('edit')" name="button" class="button100Lg" value="edit"/>

			</c:if> 
			</td>
		</tr>
		<tr>
			<td valign="bottom">
			<content:getAttribute attribute="text" beanName="layoutPageBean"/></td>
		</tr>
	</table>	
</bd:form>
<div id="modalGlassPanel" class="modal_glass_panel" style="display:none"></div>
 <br><br>
    
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
