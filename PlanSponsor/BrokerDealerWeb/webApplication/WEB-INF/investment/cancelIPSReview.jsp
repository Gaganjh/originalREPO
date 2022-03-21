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
	contentId="${bdContentConstants.IPS_CANCEL_DETAILS1_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsCancelDetails1text" />
<content:contentBean
	contentId="${bdContentConstants.IPS_CANCEL_DETAILS2_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsCancelDetails2text" />
<content:contentBean
	contentId="${bdContentConstants.IPS_CANCEL_NYSE_NOT_AVAILABLE_TEXT}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="ipsCancelNyseNotAvailabletext" />

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

	
<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<script type="text/javascript">

function doCancel(button){
	document.editIPSReviewResultsForm.action.value=button;
	var cancelConfirmationText = "${cancelConfirmationText}";
	var cancelReview = confirm(cancelConfirmationText);
	if (cancelReview==true)
	{
		if(!${editIPSReviewResultsForm.nyseAvailable}) {
			alert('${editIPSReviewResultsForm.nyseNotAvailableText}');
		}
		document.forms.editIPSReviewResultsForm.submit();
	}
}

function doBack(button){
	document.editIPSReviewResultsForm.action.value=button;
	document.forms.editIPSReviewResultsForm.submit();
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


<bd:form id="editIPSReviewResultsForm" method="POST" action="/do/bob/investment/cancelIPSReview/" name="editIPSReviewResultsForm" modelAttribute="editIPSReviewResultsForm">

	
<form:hidden path="reviewRequestId" /><%--  input - name="editIPSReviewResultsForm" --%>
<form:hidden path="action" /><%--  input - name="editIPSReviewResultsForm" --%>

	<table style="width: 100%;">
		<tr>
			<td>
				<content:getAttribute attribute="text" beanName="ipsCancelDetails1text">								
				</content:getAttribute>
			</td>
			<td>
				<content:getAttribute attribute="text" beanName="ipsCancelDetails2text">								
				</content:getAttribute>
			</td>
		</tr>
		<tr>
			<td align="right" width="100%" colspan="2">
				<input type="button" name="button1" value="back" class="button100Lg"
					onclick="return doBack('back')" /> &nbsp; 
				<input type="button" name="button3" value="cancel this review" class="button134"
						onclick="return doCancel('cancel')"/>
			</td>
		</tr>
		<tr>
			<td valign="bottom" width="100%" colspan="2"><content:getAttribute attribute="text"
					beanName="layoutPageBean" /></td>
		</tr>
	</table>
	
	
</bd:form>
