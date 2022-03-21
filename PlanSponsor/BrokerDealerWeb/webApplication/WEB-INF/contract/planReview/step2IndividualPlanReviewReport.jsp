<%-- Imports --%>

<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractProfileVO"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>


<%--  Tag Libraries  --%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>

<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_HISTORY_LINK_CONTRACT%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="historySummaryLink"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_DEMO_VIDEO_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="demoVideoLink"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_SAMPLE_REPORT_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="samplePlanLink"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_BACK_BUTTON_CLICKED%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="confirmationMessage"/>

<script type="text/javascript">
	//This functin is used for PreviewCoverPage.
	function previewPage(contract, index, csrf) {
		openView("/do/bob/contract/planReview/Customize/", contract, index,csrf);
	}

	//Logic to prevent Double Submission of Back button
	var backClicked = false;

	function doBack(action,csrf) {
		if (!backClicked) {
			backClicked = true;
			var confirmationMessage = '<content:getAttribute beanName="confirmationMessage" attribute="text" filter="true"/>';
			var isSure = confirm(confirmationMessage);
			if (isSure == true) {
				goBack(action,csrf);
			} else {
				backClicked = false;
			}

			return true;
		} else {
			window.status = "Transaction already in progress ... please wait.";
			return false;
		}
	}

	function goBack(action,csrf){
		document.forms['planReviewReportForm'].action = "/do/bob/contract/planReview/Customize/?task=" + action+"&_csrf="+csrf;
		navigate("planReviewReportForm");
	}


	//Logic to prevent Double Submission of Next button
	var clicked = false;
	function doNext(action,csrf){
		
		if (!clicked) {
			clicked = true;
			next(action,csrf);
			return true;
		} else {
			window.status = "Transaction already in progress ... please wait.";
			return false;
		}
	}
	
	function next(action,csrf) {	
			document.forms['planReviewReportForm'].action = "/do/bob/contract/planReview/Customize/?task=" + action+"&_csrf="+csrf;
		navigate("planReviewReportForm");
	}

	function doHistory(action) {
		document.forms['planReviewReportForm'].action = "/do/bob/contract/planReview/Customize/?task=" + action;
		navigate("planReviewReportForm");
	}
</script>
<style>
.IWantTo{
	color: #dcd087 !important;
	font : 1.5em/1.5em georgia, times, serif !important;
	font-family : Georgia, "Times New Roman", Times, serif !important;
}

.IWantToText{
	color: #fff !important;
	font : 11px verdana, arial, helvetica, sans-serif !important;
	
}



</style>
 <bd:form method="post" enctype="multipart/form-data" action="/do/bob/contract/planReview/Customize/"  modelAttribute="planReviewReportForm">
 
<div id="contentOuterWrapper">
<div id="contentWrapper">
<div id="rightColumnOverview">
		<h2><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h2>
		<ul>
			<li><a href="javascript:doHistory('history')"><content:getAttribute id="historySummaryLink" attribute="text"/></a></li>
			<li><content:getAttribute id="demoVideoLink" attribute="text"/></li>
			<li><content:getAttribute id="samplePlanLink" attribute="text"/></li>
		</ul>
	</div>
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>



        
<c:set var="footNotes" value="${layoutBean.layoutPageBean.footnotes}"/>        

<!-- Page Title and Headers-->
<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>
<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
    <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
</p>

	
	<%--Layout/intro1--%>
	<c:if test="${not empty layoutPageBean.introduction1}">
	   	<p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>
	
	<%--Layout/Intro2--%>
	<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
		
	
	<!-- Report Erros/Warning Messages -->
	<report:formatMessages scope="session" />

	
<!--Navigation bar-->
<navigation:contractReportsTab />

<jsp:include page="/WEB-INF/planReview/common/step2PlanReviewReports.jsp">
	</jsp:include>




<div class="clear_footer"></div>


<div class="footnotes">
    <div class="footer"><content:pageFooter beanName="layoutPageBean"/></div> 
    <br>    
    <c:if test="${not empty footNotes}"> 
    <dl>
      <dd><content:pageFootnotes beanName="layoutPageBean"/></dd> 
    </dl>
    </c:if>  
	<dl>
	<dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd>
	</dl>
	<div class="footnotes_footer"></div>
</div>
</div>
</div>
</bd:form>
