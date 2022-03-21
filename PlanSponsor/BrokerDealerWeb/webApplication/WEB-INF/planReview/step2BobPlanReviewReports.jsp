<%@ page
	import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_HISTORY_SUMMARY_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="historySummaryLink"/>

<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_DEMO_VIDEO_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="demoVideoLink"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_SAMPLE_REPORT_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="samplePlanLink"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_BACK_BUTTON_CLICKED%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="confirmationMessage"/>
 
 
<script type="text/javascript">

function sortSubmit(sortfield, sortDirection){
    document.forms['planReviewReportForm'].action = "/do/bob/planReview/Customize/?task=sort" ;
	document.forms['planReviewReportForm'].elements['sortField'].value = sortfield;
	document.forms['planReviewReportForm'].elements['sortDirection'].value = sortDirection;
	navigate("planReviewReportForm");
}
//This functin is used for PreviewCoverPage.
function previewPage(contract, index,csrf) {
	openView("/do/bob/planReview/Customize/", contract, index,csrf);
}

//Logic to prevent Double Submission of Back button
var backClicked = false;

	function doBack(action,csrf) {
		if (!backClicked) {
			backClicked = true;
			var confirmationMessage = '<content:getAttribute beanName="confirmationMessage" attribute="text" filter="true"/>';
			var isSure = confirm(confirmationMessage);
			if (isSure == true) {
				next(action,csrf);
			} else {
				backClicked = false;
			}

			return true;
		} else {
			window.status = "Transaction already in progress ... please wait.";
			return false;
		}
	}

	//Logic to prevent Double Submission of Next button
	var clicked = false;
	function doNext(action) {
		if (!clicked) {
			clicked = true;
			next(action);
			return true;
		} else {
			window.status = "Transaction already in progress ... please wait.";
			return false;
		}
	}

	function next(action) {
		document.forms['planReviewReportForm'].action = "/do/bob/planReview/Customize/?task="
				+ action;
		navigate("planReviewReportForm");
	}

	function doHistroy(action) {
		document.forms['planReviewReportForm'].action = "/do/bob/planReview/?task="
				+ action;
		navigate("planReviewReportForm");
	}
	var applyAllInd = true;
	$(document).ready(function() {
		$(".applyAll img").css("cursor", "default");

		$("#textbox_name0").on('keypress',function() {
			applyAllInd = true;
			$(".applyAll img").addClass("enabled");
			$(".applyAll img").css("cursor", "pointer");
		});
	});
	function clearAll() {

		applyAllInd = false;
		$(".applyAll img").removeClass("enabled");
		$(".applyAll img").css("cursor", "default");
		$(".checkbox_name").attr('checked', '');
		$(".checkbox_name").attr('disabled', 'true');
		$(".textbox_name").each(function() {
			this.value = "";
			$(this).attr('title', "");

		});
	}
	
</script>

<bd:form method="post" enctype="multipart/form-data" action="/do/bob/planReview/Customize/"  modelAttribute="planReviewReportForm">
<div id="contentOuterWrapper">
	<div id="contentWrapper">
	<div id="rightColumnOverview">
		<h2><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h2>
		<ul>
			<li><a href="javascript:doHistroy('history')"><content:getAttribute id="historySummaryLink" attribute="text"/></a></li>
			<li><content:getAttribute id="demoVideoLink" attribute="text"/></li>
			<li><content:getAttribute id="samplePlanLink" attribute="text"/></li>
		</ul>
	</div>
		<div id="contentTitle">
			<content:getAttribute id="layoutPageBean" attribute="name"/>					
		</div>
		
	<%--Layout/intro1--%>
	<c:if test="${not empty layoutPageBean.introduction1}">
	   	<p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>
	
	<%--Layout/Intro2--%>
	<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
		
	
	<!-- Report Erros/Warning Messages -->
	<report:formatMessages scope="session"/>
	
	<jsp:include page="/WEB-INF/planReview/common/step2PlanReviewReports.jsp">
	</jsp:include>
	</div>
	</div>

</bd:form>

<c:set var="footNotes" value="${layoutBean.layoutPageBean.footnotes}"/>

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
