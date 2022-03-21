<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- Imports --%>
<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />
	
	
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
	
<script type="text/javascript">

function doCancel(button){
	document.iPSReviewResultsForm.action.value=button;
	var cancelConfirmationText = "${cancelConfirmationText}";
	var cancelReview = confirm(cancelConfirmationText);
	if (cancelReview==true)
	{
// 		if(!${iPSReviewResultsForm.nyseAvailable}) {
// 			alert('${iPSReviewResultsForm.nyseNotAvailableText}');
// 		}
		document.iPSReviewResultsForm.submit();
	}
}

function doBack(button){
	document.iPSReviewResultsForm.action.value=button;
	document.iPSReviewResultsForm.submit();
}
</script>

<content:contentBean
	contentId="${contentConstants.IPS_CANCEL_DETAILS1_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsCancelDetails1text" />
<content:contentBean
	contentId="${contentConstants.IPS_CANCEL_DETAILS2_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsCancelDetails2text" />
<content:contentBean
	contentId="${contentConstants.IPS_CANCEL_NYSE_NOT_AVAILABLE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsCancelNyseNotAvailabletext" />

<%-- <c:set var="userProfile" value="${USER_KEY}" />
--%>
<content:errors scope="session" />
<ps:form method="POST" action="/do/investment/cancelIPSReview/"  modelAttribute="iPSReviewResultsForm" name="iPSReviewResultsForm" >
<input type="hidden" name="action" />
<%--  input - name="iPSReviewResultsForm" 
<input type="hidden" name="reviewRequestId" /><%--  input - name="iPSReviewResultsForm" --%>
	
	<table width="700" border="0" cellspacing="0" cellpadding="0">	
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
			<td width="340"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
			<td width="340"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
		</tr>
		<tr>
			<td colspan="4" class="tableheadTD1" height="20"><Strong><content:getAttribute attribute="subHeader" beanName="layoutPageBean"/></Strong>
			<render:date property="iPSReviewResultsForm.asOfDate" patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue = "" /></td>
			<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		</tr>
		<tr>
			<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
			<td class="datacell1" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<img src="/assets/unmanaged/images/s.gif" height="10" width="20" />
						</td>
					</tr>	
					<tr>
						<td>
							<img src="/assets/unmanaged/images/s.gif" height="1" width="20" />
						</td>
						<td width="350">
							<content:getAttribute attribute="text" beanName="ipsCancelDetails1text">								
							</content:getAttribute><br>							
						</td>
						<td>
							<img src="/assets/unmanaged/images/s.gif" height="1" width="20" />
						</td>
					</tr>
				</table>
			</td>
			<td width="1" class="datacell1"><img
				src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
			<td class="datacell1" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<img src="/assets/unmanaged/images/s.gif" height="10" width="20" />
						</td>
					</tr>
					<tr>
						<td>
							<img src="/assets/unmanaged/images/s.gif" height="1" width="20" />
						</td>
						<td width="350">
							<content:getAttribute attribute="text" beanName="ipsCancelDetails2text">								
							</content:getAttribute><br>
						</td>
						<td>
							<img src="/assets/unmanaged/images/s.gif" height="1" width="20" />
						</td>
					</tr>
				</table>
			</td>
			<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
		</tr>
		<tr> 
		<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
			<td class = "datacell1" colspan="3"> 
				<img
				src="/assets/unmanaged/images/s.gif" height="10" width="1" />
			</td>
			<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
		</tr>
		<tr>
		
			<c:if test="${empty param.printFriendly }" >
				<td width="1" class="databorder"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
				<td width="1" class="datacell1"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1" />				
				</td>
				<td width="1" class="datacell1"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
							
				<td class="datacell1" align="right" valign="top" style="padding-right: 8px;">
					<input type="button"
						name="button1" value="back" class="button134Cell1"
						onclick="return doBack('back')" /> &nbsp; 
						
						<input type="button" name="button3" value="cancel this review" class="button134Cell1"
						onclick="return doCancel('cancel')"/>
				</td>
				
				<td width="1" class="databorder"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
			</c:if>
			<c:if test="${not empty param.printFriendly }" >
				<td width="1" class="databorder"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
				<td width="1" class="datacell1"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1" />				
				</td>
				<td width="1" class="datacell1"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
							
				<td class="datacell1" align="right" valign="top">
					<img
					src="/assets/unmanaged/images/s.gif" height="1" width="1" />
				</td>
				
				<td width="1" class="databorder"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
			</c:if>
		</tr>		
	</table>
	<table width="700" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
			<td width="340" class="datacell1" style="padding-bottom: 5px;"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td width="1" class="datacell1" style="padding-bottom: 5px;"><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
			<td width="340" class="datacell1" style="padding-bottom: 5px;"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td width="1" class="databorder" ><img src="/assets/unmanaged/images/s.gif" width="1"
				height="1"></td>
		</tr>
		<tr>
			<td width="100%" height="1" colspan="5" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>			
		</tr>
	</table>
	
	<c:if test="${not empty param.printFriendly}">
	<br><br><br>
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
