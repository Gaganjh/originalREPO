<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ page import="com.manulife.pension.ps.web.Constants,
                 com.manulife.util.render.RenderConstants"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.platform.web.util.DataUtility" %>
<%@page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%-- Imports --%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.HashMap"%>
<%@	page import="com.manulife.pension.ps.web.Constants"	%>
<%@	page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@	page import="com.manulife.pension.platform.web.investment.valueobject.CoFidContractDocuments" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>


<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />


<content:contentBean 
	contentId="<%=ContentConstants.MISCELLANEOUS_DOWNLOAD_CONTRACT_LINK%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="downloadContract"/>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<script>


/**
* opens a window to show the PDF for the selected report
* by sending a request to the CoFiduciary321QtrlyReviewPDF action
*
*/
function showCoFidContractPDF(fileName) {
	var url = "/do/investment/coFiduciary321QtrlyReviewPDFAction/?coFidContractDoc=";
	url = url+fileName;
	PDFWindow(url)
}

</script>

	
<content:contentBean contentId="<%=ContentConstants.COFID321_NO_REPORTS_FOUND_TEXT%>"
                type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                id="noReportsFoundText"/>

<c:set var="tableWidth" value="485" />

<style type="text/css">
.tpaheader {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	width: 729px;
	font-weight: bolder;
	color: #000000;
	padding: 0px;
	background : #CCCCCC url(/assets/unmanaged/images/box_ul_corner.gif) no-repeat left top;
	clear: both;
	vertical-align: middle;
	border-right:1px solid #CCCCCC;
}
B {
    FONT-SIZE: 11px;
    FONT-FAMILY: Arial, Helvetica, sans-serif;
}
</style>


<table width="700" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="5%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
       <img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
       
		<td width="94%" valign="top">
		<p>
		<content:errors scope="request" />
		<content:errors scope="session" />
		</p>
		
		</td>
		<td width="100" rowspan="2"><img
			src="/assets/unmanaged/images/spacer.gif" width="15" height="1"
			border="0"></td>
	</tr>
	
	
	<c:if test="${isCoFiduciary == true}">
	<c:if test="${isFundRecommendationAvailable == true}">
	
	<ps:linkAccessible path="/do/investment/coFiduciaryFundRecommendationReview/">
	<tr>
	<td width="1%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
       <img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
       
	<td width="99%">
		 <a href="/do/investment/coFiduciaryFundRecommendationReview/">View your current fund changes</a>
	</td>
	</tr>
	</ps:linkAccessible>
</c:if>
</c:if>
	<br/>
	
</table>
	

	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="20"></td>
			<td width="380"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
			<td width="409"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
		</tr>
		
		<tr>
			<td colspan="4" class="tableheadTD1" height="20"><Strong>Quarterly Reports as of:</Strong>
			<render:date property="coFiduciary321QtrlyReviewPageForm.currentDate" patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue = "" /></td>
			<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		</tr>
		<tr>
		<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						<th class="tablesubhead" valign="top" width="40%" align="left"><b><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1">Provider
								</th>
								<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<th class="tablesubhead" valign="top" width="30%" align="left"><b><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1">Reports
								</th>
								<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
					
					<th  valign="top" width="30%" align="left">
								</th>
								
					</tr>
					<c:if test="${not empty coFiduciary321QtrlyReviewPageForm.coFidContractDocList}">
<c:if test="${not empty coFiduciary321QtrlyReviewPageForm.coFidContractDocList}">
<c:forEach items="${coFiduciary321QtrlyReviewPageForm.coFidContractDocList}" var="theItem" varStatus="theIndex" >

				<%-- This logic is to differentiate the alternate rows --%>
				<c:choose>
					<c:when test="${(theIndex.index) % 2 == 1}">
						<tr class="datacell1">
					</c:when>
					<c:otherwise>
						<tr class="datacell2">
					</c:otherwise>
				</c:choose>
				<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
				 <td height="20" align="left"> <img
src="/assets/unmanaged/images/s.gif" width="1" height="1"> ${theItem.providerDisplayName}</td>
				
				 <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				
								
				<td height="20" align="left"> <img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"> <a
href="javascript:showCoFidContractPDF('${theItem.contractDocName}')">
				<render:date property="theItem.documentCreatedDate"
					patternOut="${renderConstants.LONG_MDY}" defaultValue="" />
				</a></td>
				<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>

				</tr>
</c:forEach>
</c:if>
		
<c:if test="${empty coFiduciary321QtrlyReviewPageForm.coFidContractDocList}">
			<tr class="datacell2">
			<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			<td colspan="3" height="20" align="left" >
			<img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" />
			<Strong><content:getAttribute attribute="text" beanName="noReportsFoundText" /></Strong>
			</td>
			<td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			
			
			</tr>
			
			
</c:if>
		</c:if>
		<tr>
			<td colspan="4" width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		</tr>
		
	</table>

	
<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
			height="15"></td>
	</tr>
	
</table>
                    
