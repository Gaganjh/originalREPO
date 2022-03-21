<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@ page
	import="com.manulife.pension.ps.web.Constants, com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page
	import="com.manulife.pension.ps.service.report.investment.valueobject.FundCategory"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ page
	import="com.manulife.pension.ps.web.investment.CofiduciaryReviewScreenPageForm"%>
<%@ page
	import="com.manulife.pension.service.contract.valueobject.CofidFundRecommendVO"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

	<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />
<content:contentBean
	contentId="<%=ContentConstants.FIXED_FOOTNOTE_PBA%>"
	type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>" id="footnotePBA" />
<content:contentBean contentId="55951" type="${contentConstants.TYPE_MISCELLANEOUS}" id="unSavedData" />
<script type="text/javascript" >

function isFormDirty() {
	var dirtyFlagId = document.getElementById('dirtyFlagId').value
	return (dirtyFlagId);
}

function setDirtyFlag() {	
	document.getElementById('dirtyFlagId').value="true";
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
	var url = "/do/investment/coFiduciaryFundRecommendationReview/?action=downloadNoticePdf";
	PDFWindow(url);
}

</script>

<content:getAttribute attribute="intro2" beanName="layoutPageBean"/>
	
	<style type="text/css">
	B {
    FONT-SIZE: 11px;
    FONT-FAMILY: Arial, Helvetica, sans-serif;
}
.datacell1 {
    PADDING-RIGHT: 2px;
    PADDING-LEFT: 2px;
    FONT-SIZE: 11px;
    BACKGROUND: #ffffff;
    PADDING-BOTTOM: 2px;
    PADDING-TOP: 2px;
    }
	</style>					
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%" valign="top"><content:errors scope="request" /><br>
			<img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>

			<ps:form method="POST" modelAttribute="cofiduciaryReviewScreenPageForm" name="cofiduciaryReviewScreenPageForm"
				action="/do/investment/coFiduciaryFundRecommendationReview/?action=save">
				


<input type="hidden" name="dirty" id="dirtyFlagId"/>
<input type="hidden" name="action" /><%--  input - name="cofiduciaryReviewScreenPageForm" --%>
				<input type=hidden name="actionDetail"/>
				<table width="72%" border="0" cellspacing="0" cellpadding="1">

					<tr>
					<td class="tableheadTD1" colspan="5" height="20" >
						<strong>Fund changes as of: </strong><render:date property="cofiduciaryReviewScreenPageForm.createdTS"
				patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue="" /></td>
										<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
					<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
					
					
						</tr>
						
		        
					<tr>
					<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						<th class="tablesubhead" valign="top" width="35%" align="left"><b><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1">Current
								Fund</b></th>	
									<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<th class="tablesubhead" valign="top" width="37%" align="left"><b><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1">Replacement
								Fund</b></th>
									<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>						
						<th class="tablesubhead" valign="top" width="38%" align="left"><b><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1">
								Actions
								Selected</b></th>
								<th class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></th>
					
								
					</tr>

							
						
					
					
<c:if test="${not empty cofiduciaryReviewScreenPageForm.cofidFundRecommendDetails}">

<c:forEach items="${cofiduciaryReviewScreenPageForm.cofidFundRecommendDetails}" var="cofidFundRecommendDetails" varStatus="rowIndex" >



							<c:choose>
								<c:when test="${rowIndex.index % 2 == 0}">
									<tr class="datacell2">
								</c:when>
								<c:otherwise>
									<tr class="datacell1">
								</c:otherwise>
							</c:choose>
							<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
								<td valign="top" align="left">
								<img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" />
									${cofidFundRecommendDetails.fromFundInvestmentName}</td>
										<td class="datadivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td valign="top" align="left">
								<img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" />
									${cofidFundRecommendDetails.toFundInvestmentName}</td>
										<td class="datadivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td valign="top" align="left">
								<c:set var="ind" value="${cofidFundRecommendDetails.optOutIndicator}" />
								<c:set var="count" value="0"/>
									<c:choose>
									
										<c:when  test="${cofiduciaryReviewScreenPageForm.editable}">
											
											<c:if test="${ind == 'N'}">
												
												<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index }].optOutIndicator"  value="N" id="optOutIndicator${rowIndex.index}" checked><label for="new">Accept</label>
	            								
	            								<input type="radio"  name="cofidFundRecommendDetails[${rowIndex.index }].optOutIndicator" onchange="setDirtyFlag()" value="Y" id="optOutIndicator${rowIndex.index}" ><label for="new">Decline</label>
	          								 </c:if>
	            							<c:if test="${ind == 'Y'}">
	            								
												<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index }].optOutIndicator" onchange="setDirtyFlag()" value="N" id="optOutIndicator${rowIndex.index}" ><label for="new">Accept</label>
	            								
	            								<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index }].optOutIndicator" onchange="setDirtyFlag()" value="Y" id="optOutIndicator${rowIndex.index}" checked><label for="new">Decline</label>
	            							</c:if>
            							</c:when>
										<c:otherwise>
											
											<c:if test="${ind == 'N'}">
												
												<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index }].optOutIndicator" onchange="setDirtyFlag()" value="N" id="optOutIndicator${rowIndex.index}" checked disabled><label for="new">Accept</label>
	            								
	            								<input type="radio"  name="cofidFundRecommendDetails[${rowIndex.index }].optOutIndicator" onchange="setDirtyFlag()" value="Y" id="optOutIndicator${rowIndex}" disabled><label for="new">Decline</label>
	          								 </c:if>
	            							<c:if test="${ind == 'Y'}">
	            								
												<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index }].optOutIndicator" onchange="setDirtyFlag()" value="N" id="optOutIndicator${rowIndex.index}" disabled><label for="new">Accept</label>
	            							
	            								<input type="radio" name="cofidFundRecommendDetails[${rowIndex.index }].optOutIndicator" onchange="setDirtyFlag()" value="Y" id="optOutIndicator${rowIndex.index}" checked disabled><label for="new">Decline</label>
	            							</c:if>
										</c:otherwise>
									</c:choose>
								</td>
									<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
</c:forEach>
</c:if>
					<tr>
			<td colspan="4" width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
				<td colspan="4" width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		</tr>
				<tr>
					<td align="right" colspan="8"  width="72%"><br> 
						<p>Your transaction will be processed on date shown below.</p></td>
				</tr>
				<tr>
					<td align="right" colspan="8" width="72%"><br> 
						<strong>Effective Date: </strong><render:date property="cofiduciaryReviewScreenPageForm.scheduledDate"
				patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue="" /> 
						</td>
				</tr>
				<tr>
					<td align="right" colspan="8" width="72%"><br> <br>
						<c:choose>
							<c:when test="${cofiduciaryReviewScreenPageForm.editable}">
								<input name="Submit" type="submit" class="button100Lg"
									align="right" value="Save" onclick="return doSave();">

							</c:when>
							<c:otherwise>
								

							</c:otherwise>
						</c:choose></td>
				</tr>
				</table>
				
			</ps:form></td>
	</tr>
</table>
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
var unSavedData = "<content:getAttribute id='unSavedData' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
</script>
