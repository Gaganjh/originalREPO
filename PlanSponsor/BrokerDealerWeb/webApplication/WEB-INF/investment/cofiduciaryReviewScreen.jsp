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
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@page import="com.manulife.pension.platform.web.util.ReportsXSLProperties"%>
<%@ page import="com.manulife.pension.service.contract.util.Constants"%>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<%@ page
	import="com.manulife.pension.bd.web.bob.investment.CofiduciaryReviewScreenPageForm"%>
<%@ page
	import="com.manulife.pension.service.contract.valueobject.CofidFundRecommendVO"%>
<%@ page import="java.util.ArrayList"%>
<%--Contents Used--%>

<%@ taglib prefix="contentPs" uri="manulife/tags/content"%>

<contentPs:contentBean contentId="55951" type="Miscellaneous" id="unSavedData" />


<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>


<content:contentBean contentId="<%=BDContentConstants.REPORT_TOOLS_PARTICIPANT_NOTICE_DOCUMENT%>"
                               type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                               id="coFidParticipantNoticePDF" override="true"/> 

<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<script language="JavaScript" type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>

<script type="text/javascript">


function isFormDirty() {
	var dirtyFlagId = document.getElementById('dirtyFlagId').value
	return (dirtyFlagId);
}

function setDirtyFlag() {	
	document.getElementById('dirtyFlagId').value="true";
}

function isLostChangesForContract(){
	var changesLost =isLostChanges();
	if(changesLost){
	top.location.href='/do/bob/blockOfBusiness/Active/'
	}
}


function isLostChanges() {
	return confirmDiscardChanges(unSavedData);
}
function confirmDiscardChanges(message){	
	if (document.getElementById('dirtyFlagId').value == 'true')
	{
		return window.confirm(message);
	}
	else 
	{
	    return true;
	}
}

/**
* opens a window to show the CoFidParticipantNotice PDF.
*/
 function downloadCoFidParticipantNoticePDF() {
	var url = "/do/bob/investment/coFiduciaryFundRecommendationReview/?action=downloadNoticePdf";
	PDFWindow(url);
}

</script>

<h2>
	<content:getAttribute id="layoutPageBean" attribute="name" />
</h2>

<p class="record_info">
	<strong>${bobContext.contractProfile.contract.companyName}
		(${bobContext.contractProfile.contract.contractNumber})</strong> <input
		class="btn-change-contract" type="button"
		onmouseover="this.className +=' btn-change-contract-hover'"
		onmouseout="this.className='btn-change-contract'"
		onclick=isLostChangesForContract(); value="Change contract">
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


<c:if test="${bobContext.contractProfile.contract.participantNoticeAvailableValue eq true }" > 
	<p align="right" class="record_info">
	    <a href="javascript:downloadCoFidParticipantNoticePDF()">
	              <content:image id="coFidParticipantNoticePDF" contentfile="image"/></a>&nbsp;
		<a href="javascript:downloadCoFidParticipantNoticePDF();">
	              <content:getAttribute id="coFidParticipantNoticePDF" attribute="title"/></a>&nbsp;<br/></p>
</c:if>

<!--Error- message box-->
<report:formatMessages scope="request" />

<navigation:contractReportsTab />

<div class="report_table">
	<div class="page_section_subheader controls">

		<h3>
		
		Fund changes as of: </h3>
		
		<form class="page_section_filter form">
		<p>
			<render:date property="cofiduciaryReviewScreenPageForm.createdTS"
				patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue="" />

		</p>
	</form>
			
	</div>
</div>


<bd:form method="POST" action="/do/bob/investment/coFiduciaryFundRecommendationReview/?action=save" modelAttribute="cofiduciaryReviewScreenPageForm" name="cofiduciaryReviewScreenPageForm">
	

	<div class="report_table">
		<div class="clear_footer"></div>
		<table class="report_table_content">
			<thead>
				<tr>
<input type="hidden" name="dirty" id="dirtyFlagId"/>
<input type="hidden" name="action" /><%--  input - name="cofiduciaryReviewScreenPageForm" --%>
					<th class="val_str" width="33%">Current Fund</th>
					<th class="val_str" width="33%">Replacement Fund</th>
					<th class="val_str" width="33%" align="left">Actions Selected <br>
						
					</th>
				</tr>
			</thead>
			<tbody>
<c:if test="${not empty cofiduciaryReviewScreenPageForm.cofidFundRecommendDetails}">

<c:forEach items="${cofiduciaryReviewScreenPageForm.cofidFundRecommendDetails}" var="cofidFundRecommendDetails" varStatus="rowIndex" >



							<c:choose>
								<c:when test='${(rowIndex.index) % 2 == 1}'>
									<tr class="spec">
								</c:when>
								<c:otherwise>
									<tr class="spec1">
								</c:otherwise>
							</c:choose>

						<td class="datadivider" valign="top"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1">
									${cofidFundRecommendDetails.fromFundInvestmentName}</td>
								<td class="datadivider" valign="top"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1">
									${cofidFundRecommendDetails.toFundInvestmentName}</td>
								<td class="datadivider" valign="top">
															
					<c:set var="ind" value="${cofidFundRecommendDetails.optOutIndicator}" />
					
			              <c:choose>
									
										<c:when  test="${cofiduciaryReviewScreenPageForm.editable}">
											
											<c:if test="${ind == 'N'}">												
												<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index}].optOutIndicator"  value="N" id="optOutIndicator${rowIndex.index}" checked><label for="new">Accept</label>	            								
	            								<input type="radio"  name="cofidFundRecommendDetails[${rowIndex.index}].optOutIndicator" onchange="setDirtyFlag()" value="Y" id="optOutIndicator${rowIndex.index}" ><label for="new">Decline</label>
	          								 </c:if>
	            							<c:if test="${ind == 'Y'}">	            								
												<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index}].optOutIndicator" onchange="setDirtyFlag()" value="N" id="optOutIndicator${rowIndex.index}" ><label for="new">Accept</label>	            								
	            								<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index}].optOutIndicator" onchange="setDirtyFlag()" value="Y" id="optOutIndicator${rowIndex.index}" checked><label for="new">Decline</label>
	            							</c:if>
            							</c:when>
										<c:otherwise>
											
											<c:if test="${ind == 'N'}">												
												<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index}].optOutIndicator" onchange="setDirtyFlag()" value="N" id="optOutIndicator${rowIndex.index}" checked disabled><label for="new">Accept</label>	            								
	            								<input type="radio"  name="cofidFundRecommendDetails[${rowIndex.index}].optOutIndicator" onchange="setDirtyFlag()" value="Y" id="optOutIndicator${rowIndex.index}" disabled><label for="new">Decline</label>
	          								 </c:if>
	            							<c:if test="${ind == 'Y'}">
	            								
												<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index}].optOutIndicator" onchange="setDirtyFlag()" value="N" id="optOutIndicator${rowIndex.index}" disabled><label for="new">Accept</label>	            							
	            								<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index}].optOutIndicator" onchange="setDirtyFlag()" value="Y" id="optOutIndicator${rowIndex.index}" checked disabled><label for="new">Decline</label>
	            							</c:if>
										</c:otherwise>
									</c:choose></td>
					
</c:forEach>
</c:if>
			
			</tbody>
			
		</table>
	</div>
	<table border="0" align="right">
		<tr>
					<td align="right" valign="top">
						Your transaction will be processed on date shown below.</td>
				</tr>
				<tr>
					<td align="right">
						Effective Date: <strong><span style="width: 90px"><render:date property="cofiduciaryReviewScreenPageForm.scheduledDate"
				patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue="" /></span></strong></td>
				</tr>
				<tr>
					<td align="right"><br>
						<c:choose>
							<c:when test="${cofiduciaryReviewScreenPageForm.editable}">
								<input name="Submit" type="submit" class="button100Lg"
									 value="Save" onclick="return doSave();">

							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose></td>
				</tr>
		</table>
</bd:form>

<script>

$(document).ready(function() {	
	var hrefs  = document.links;
	if (hrefs != null)
	{
		for (i = 0; i < hrefs.length; i++) {
			if (hrefs[i].onclick != undefined
					&& (hrefs[i].onclick.toString().indexOf("openWin") != -1
					|| hrefs[i].onclick.toString().indexOf("popup") != -1 
					|| hrefs[i].onclick.toString().indexOf("doSubmit") != -1
					|| hrefs[i].onclick.toString().indexOf("doProtectedSubmit") != -1
					|| hrefs[i].onclick.toString().indexOf("doCancel") != -1
					|| hrefs[i].onclick.toString().indexOf("doPrint") != -1
					|| hrefs[i].onclick.toString().indexOf("doNothing") != -1
					|| hrefs[i].onclick.toString().indexOf("downloadCoFidParticipantNoticePDF") != -1)
					
			    ) {
				// don't replace window open or popups as they won't loose their changes with those
			} else if (hrefs[i].href != undefined
					&& (hrefs[i].href.indexOf("openWin") != -1
							|| hrefs[i].href.indexOf("popup") != -1 || 
							hrefs[i].href.indexOf("doSubmit") != -1 ||
							hrefs[i].href.indexOf("doProtectedSubmit") != -1  || 
							hrefs[i].href.indexOf("doCancel") != -1 ||
							hrefs[i].href.indexOf("doPrint") != -1 ||
							hrefs[i].href.indexOf("downloadCoFidParticipantNoticePDF") != -1 ||
							hrefs[i].href.indexOf("doNothing") != -1		
					) 
					) {
				// don't replace window open or popups as they won't loose their changes with those
			} else if (hrefs[i].onclick != undefined) {
				//alert("signout");
					hrefs[i].onclick = new Function(
							
							"var result = isLostChanges();"
									+ "var childFunction = "
									+ hrefs[i].onclick
									+ "; if(result) result = childFunction(); return result;");
			} else {
				//alert("else signout");
					hrefs[i].onclick = new Function(							
							"return isLostChanges();");
			}
		}
		
	}			

});

</script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/input.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/employeeSnapshot.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>

<script>
var unSavedData = "<contentPs:getAttribute id='unSavedData' attribute='text'  filter='true' escapeJavaScript='true'></contentPs:getAttribute>";
</script>
