<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>


<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_HISTORY_SUMMARY_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="historySummaryLink"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_DEMO_VIDEO_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="demoVideoLink"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_SAMPLE_REPORT_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="samplePlanLink"/>

<script type="text/javascript">
	var sortFieldClicked = false;

	//Logic to prevent Double Submission of Next button
	var clicked = false;
	function doNext(action) {
		if (!clicked) {
			
			clicked = true;
			submit(action, '/do/bob/planReview/?task=');
			return true;
		} else {
			window.status = "Transaction already in progress ... please wait.";
			return false;
		}
	}

	function doContinue(action) {
		submit(action, '/do/bob/planReview/?task=');
	}

	function sortSubmit(sortfield, sortDirection) {
		document.forms['planReviewReportForm'].action = "/do/bob/planReview/?task=sort";
		document.forms['planReviewReportForm'].elements['sortField'].value = sortfield;
		document.forms['planReviewReportForm'].elements['sortDirection'].value = sortDirection;
		navigate("planReviewReportForm");
	}
	function doHistroy(action) {
		submit(action, '/do/bob/planReview/?task=');
	}
</script>

<bd:form method="post" action="/do/bob/planReview/" modelAttribute="planReviewReportForm" name="planReviewReportForm">
     
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
 	<report:formatMessages scope="session" suppressDuplicateMessages="true"/>
 	
		
	<jsp:include page="/WEB-INF/planReview/common/step1PlanReviewReports.jsp">
	</jsp:include>
	</div>
	</div>
	</bd:form>
	<br/><br/>
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
