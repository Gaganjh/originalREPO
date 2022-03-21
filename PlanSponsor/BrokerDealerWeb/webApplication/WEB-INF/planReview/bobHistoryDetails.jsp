<%@page import="java.util.Date"%>
<%@ page
	import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.platform.web.CommonConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>


<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>

<content:contentBean
	contentId="<%=BDContentConstants.PLAN_REVIEW_STEP1_PAGE_LINK%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="PlanReviewRequestLink" />
<content:contentBean
	contentId="<%=BDContentConstants.PLAN_REVIEW_PRINT_REQUEST_LINK%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="PrintRequestLink" />

<script type="text/javascript">
	//var deleteHistoryMsg ='<content:getAttribute id="HistoryDeleteLink" attribute="text" filter="true"/>
	function doReSubmitPlanReviewRequest(activityId, contractNum, contractName, requestedTS, action) {
			if (confirm("Before resubmitting, please ensure the issue that caused the initial request to fail has been resolved. Select OK to continue or Cancel to return.")) {
				document.forms['planReviewHistoryDetailsReportForm'].elements['selectedPrintActivityId'].value = activityId;
				document.forms['planReviewHistoryDetailsReportForm'].elements['selectedPlanReviewContractId'].value = contractNum;
				document.forms['planReviewHistoryDetailsReportForm'].elements['selectedPlanReviewContractName'].value = contractName;
				document.forms['planReviewHistoryDetailsReportForm'].elements['selectedPlanReviewRequestedTS'].value = requestedTS;
				 
				document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/planReview/HistoryDetails/?task="
						+ action;
				navigate("planReviewHistoryDetailsReportForm");
			}
		}
		
	function doPlanReviewRequest(action) {
		document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/planReview/HistoryDetails/?task="
				+ action;
		navigate("planReviewHistoryDetailsReportForm");
	}
	
	function doPlanReviewPrint(activityID) {
		document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/planReview/HistoryDetails/?task=print";
		document.forms['planReviewHistoryDetailsReportForm'].elements['selectedPlanReviewActivityId'].value = activityID;
		navigate("planReviewHistoryDetailsReportForm");
	}

	//Opens the PDF in new window
	function openPDF(url, activityID, docType, csrf) {

		var form = document.createElement("form");
		form.action = url;
		form.method = 'POST';
		form.target = "_blank";

		var csrfInput = document.createElement("input");
		csrfInput.setAttribute("type", "hidden");
		csrfInput.setAttribute("name", "_csrf");
		csrfInput.setAttribute("value", csrf);
		form.appendChild(csrfInput);

		var taskInput = document.createElement("input");
		taskInput.setAttribute("type", "hidden");
		taskInput.setAttribute("name", "task");
		taskInput.setAttribute("value", "openPdfWindow");
		form.appendChild(taskInput);

		var activityIdInput = document.createElement("input");
		activityIdInput.setAttribute("type", "hidden");
		activityIdInput.setAttribute("name", "selectedPlanReviewActivityId");
		activityIdInput.setAttribute("value", activityID);
		form.appendChild(activityIdInput);

		var requestType = document.createElement("input");
		requestType.setAttribute("type", "hidden");
		requestType.setAttribute("name", "requestType");
		requestType.setAttribute("value", "viewPdf");
		form.appendChild(requestType);
		
		//irregular navigation
		var navigation = document.createElement("input");
		navigation.setAttribute("type", "hidden");
		navigation.setAttribute("name", "pageRegularlyNavigated");
		navigation.setAttribute("value", "true");
		form.appendChild(navigation);
		
		var requestHistoryDetailsReport = document.createElement("input");
		requestHistoryDetailsReport.setAttribute("type", "hidden");
		requestHistoryDetailsReport.setAttribute("name", "requestHistoryDetailsReport");
		requestHistoryDetailsReport.setAttribute("value", "true");
		form.appendChild(requestHistoryDetailsReport);

		var docTypeInput = document.createElement("input");
		docTypeInput.setAttribute("type", "hidden");
		docTypeInput.setAttribute("name", "type");
		docTypeInput.setAttribute("value", docType);
		form.appendChild(docTypeInput);
		
		form.style.display = 'none';
		document.body.appendChild(form);
		form.submit();
	}

	//This method is called for View the individula pdf's according to the contract number 
	function doView(activityID, type,csrf) {

		openPDF("/do/bob/planReview/HistoryDetails/?task=openPdfWindow", activityID, type,csrf);
	}

	/*function doPrint(action) {
	 document.forms['planReviewResultForm'].action = "/do/bob/planReview/?task="
	 + action;
	 navigate("planReviewResultForm","Results","History");
	 }*/

	//this function is called for Down load the individula pdf's according to the contract number
	function doDownload(activityID, type) {
		var reportURL = new URL("/do/bob/planReview/HistoryDetails/");

		reportURL.setParameter("task", "downloadPdf");
		reportURL.setParameter("selectedPlanReviewActivityId", activityID);
		reportURL.setParameter("type", type);
		reportURL.setParameter("pageRegularlyNavigated", "true");
		reportURL.setParameter("requestHistoryDetailsReport", "true");
		reportURL.setParameter("requestType", "download");
		location.href = reportURL.encodeURL();
	}
	
	 var intervalId;
	 var downloadloadingPanel;

		var downloadUtilities = {
		// Asynchronous request call to the server. 
		doDownAsyncRequest : function(actionPath, callbackFunction) {
			// Make a request
			var request = YAHOO.util.Connect.asyncRequest('GET', actionPath,
					callbackFunction);
		},
		// Shows loading panel message
		showWaitPanel : function() {
			waitPanel = document.getElementById("wait_c");
			if (waitPanel == undefined || waitPanel.style.visibility != "visible") {
				downloadloadingPanel = new YAHOO.widget.Panel("download_panel", {
					width : "450px",
					height : "180px",
					fixedcenter : true,
					close : false,
					draggable : false,
					zindex : 4,
					modal : true,
					visible : false,
					constraintoviewport : true
				});
				downloadloadingPanel.render();
				downloadloadingPanel.show();
			}
		},
		
		// Generic function to handle a failure in the server response  
		handleFailure : function(o) {
			o.argument = null;
			downloadUtilities.hideWaitPanel();
			clearInterval(intervalId);
		},
		
		/**
		 * hides the loading panel
		 */
		hideWaitPanel : function() {
			downloadloadingPanel.hide();
		}
		
		};
		
		//this function is called for Down load the individula pdf's according to the contract number
		function doDownloadPdf(activityID, type) {
			
			downloadUtilities.showWaitPanel();
			document.forms['planReviewHistoryDetailsReportForm'].elements['pageRegularlyNavigated'].value = true;
			document.forms['planReviewHistoryDetailsReportForm'].elements['selectedPlanReviewActivityId'].value = activityID;
			document.forms['planReviewHistoryDetailsReportForm'].elements['type'].value = type;
			document.forms['planReviewHistoryDetailsReportForm'].elements['requestType'].value = 'download';
			document.forms['planReviewHistoryDetailsReportForm'].elements['requestHistoryDetailsReport'].value = true;
			
			document.forms['planReviewHistoryDetailsReportForm'].action="/do/bob/planReview/HistoryDetails/?task=downloadPdf";	
			document.forms['planReviewHistoryDetailsReportForm'].submit();

			intervalId = setInterval(doCheckDownloadPdfGenerated, 5000);    	
		}
		
		function doCheckDownloadPdfGenerated(){
		
			downloadUtilities.doDownAsyncRequest("/do/bob/planReview/HistoryDetails/?task=checkDownloadPdfGenerated&isPageRegularlyNavigated=true", callback_checkDownloadPdfGenerated);
		}
		
		// Call back handler to Check whether FundEvaluator Report Generation is complete.
		var callback_checkDownloadPdfGenerated =    {
		    cache : false,
		    failure : downloadUtilities.handleFailure
	    };
		
	 //Download Selected Button for selected  PDF's in Result page.
	function doDownloadSelected(action) {
		
		downloadUtilities.showWaitPanel();
		document.forms['planReviewHistoryDetailsReportForm'].elements['pageRegularlyNavigated'].value = true;
		document.forms['planReviewHistoryDetailsReportForm'].elements['requestHistoryDetailsReport'].value = true;
		document.forms['planReviewHistoryDetailsReportForm'].action="/do/bob/planReview/HistoryDetails/?task="+ action;			
		document.forms['planReviewHistoryDetailsReportForm'].submit();

		intervalId = setInterval(doCheckDownloadSelectedPdfGenerated, 5000); 
	}
	
	function doCheckDownloadSelectedPdfGenerated(){
		
		downloadUtilities.doDownAsyncRequest("/do/bob/planReview/HistoryDetails/?task=checkDownloadSelectedPdfGenerated&isPageRegularlyNavigated=true", callback_checkDownloadSelectedPdfGenerated);
	}
	
	// Call back handler to Check whether FundEvaluator Report Generation is complete.
	var callback_checkDownloadSelectedPdfGenerated =    {
	    cache : false,
	    failure : downloadUtilities.handleFailure
    };

</script>

<style>

</style>

<bd:form method="post" action="/do/bob/planReview/HistoryDetails/" modelAttribute="planReviewHistoryDetailsReportForm" name="planReviewHistoryDetailsReportForm">

<input type="hidden" name="pageRegularlyNavigated"/>
<input type="hidden" name="sortField"/>
<input type="hidden" name="sortDirection"/>
<input type="hidden" name="pageNumber"/>
<input type="hidden" name="selectedPrintActivityId"/>
<form:hidden  path="selectedPlanReviewActivityId"/>
<input type="hidden" name="selectedPlanReviewRequestId"/>
<input type="hidden" name="selectedPlanReviewContractId"/>
<input type="hidden" name="selectedPlanReviewContractName"/>
<input type="hidden" name="selectedPlanReviewRequestedTS"/>
<form:hidden path="requestHistoryDetailsReport" value="true"/>
<input type="hidden" name="bobResults" class="bob_results" value="${sessionScope.bobResults}"/>
	<!-- includes error message and report tag-->
	<report:formatMessages scope="request"/>
<!--<div id="errordivcs"><content:errors scope="session"/></div><br>-->
	<div id="contentOuterWrapper">
		<div id="contentWrapper">
			<div id="rightColumnOverview">
				<h2><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h2>
				<ul>
					<li><a href="javascript://" onclick="doPlanReviewPrint('${planReviewHistoryDetailsReportForm.selectedPlanReviewActivityId}')"
							> <content:getAttribute
								id="PrintRequestLink" attribute="text" /></a></li>
					<li><a href="javascript://" onclick="doPlanReviewRequest('planReviewRequest')"
							> <content:getAttribute
								id="PlanReviewRequestLink" attribute="text" /></a></li>
				</ul>
			</div>
			
			<div id="contentTitle">
				<content:getAttribute id="layoutPageBean" attribute="name" />
			</div>
			<%--Layout/intro1--%>
			<c:if test="${not empty layoutPageBean.introduction1}">
			   	<p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>
			
			<%--Layout/Intro2--%>
			<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
			
			<jsp:include page="/WEB-INF/planReview/common/historyDetailResults.jsp" />
			<jsp:include page="/WEB-INF/planReview/common/historyPrintRequestDetails.jsp" />
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
