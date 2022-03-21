<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page
	import="com.manulife.pension.service.contract.valueobject.ContractProfileVO"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<%--  Tag Libraries  --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_HISTORY_LINK_CONTRACT%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="historySummaryLink"/> 
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_REQUEST_LINK_CONTRACT_LEVEL%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="planReviewRequestLink"/> 
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_PRINT_LINK_CONTRACT_LEVEL%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="printPlanReviewRequestLink"/> 
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_RESULTS_ERROR_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="incompleteOrInprogress"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_RESULTS_INCOMPLETE_ERROR_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="requestWithIncompleteStatusWarningMessage"/>

<script type="text/javascript">
 

 //This method is called for View the individula pdf's according to the contract number 
	function doView(contract,type,csrf) {
		openPDF("/do/bob/contract/planReview/Results/?task=openPdfWindow",
				contract, type,csrf);
	}
 
	//Opens the PDF in new window
	function openPDF(url, contract, docType,csrf) {

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
		taskInput.setAttribute("value", "viewPdf");
		form.appendChild(taskInput);

		var contractInput = document.createElement("input");
		contractInput.setAttribute("type", "hidden");
		contractInput.setAttribute("name", "contract");
		contractInput.setAttribute("value", contract);
		form.appendChild(contractInput);

		var requestType = document.createElement("input");
		requestType.setAttribute("type", "hidden");
		requestType.setAttribute("name", "requestType");
		requestType.setAttribute("value", "openPdfWindow");
		form.appendChild(requestType);
		
		//irregular navigation
		var navigation = document.createElement("input");
		navigation.setAttribute("type", "hidden");
		navigation.setAttribute("name", "pageRegularlyNavigated");
		navigation.setAttribute("value", "true");
		form.appendChild(navigation);

		var docTypeInput = document.createElement("input");
		docTypeInput.setAttribute("type", "hidden");
		docTypeInput.setAttribute("name", "type");
		docTypeInput.setAttribute("value", docType);
		form.appendChild(docTypeInput);
		
		form.style.display = 'none';
		document.body.appendChild(form);
		form.submit();
	}
	
 //this function is calling the Induividula History page.
   function doHistory(action){	
   	document.forms['planReviewResultForm'].action = "/do/bob/contract/planReview/Results/?task=" + action;
   	navigate("planReviewResultForm");
   }

   //this function is called for Down load the individula pdf's according to the contract numbe.
   function doDownload(contract, type)
   {
   		var reportURL = new URL("/do/bob/contract/planReview/Results/");
   		   		
   		reportURL.setParameter("task", "downloadPdf");
		reportURL.setParameter("contract", contract);
		reportURL.setParameter("type", type);
		reportURL.setParameter("pageRegularlyNavigated", "true");
		reportURL.setParameter("requestType", "download");
   		
   		location.href = reportURL.encodeURL();
   }

  

 // Down load the pdf for selected record
   function doDownloadSum(contract)
   {
   		var reportURL = new URL("/do/bob/contract/planReview/Results/");
   		reportURL.setParameter("task", "downloadPdf");
   		reportURL.setParameter("contract", contract);
   		reportURL.setParameter("type", "summary");
   		location.href = reportURL.encodeURL();
   }
 
   	   	
   	function incompleteOrInprogessContractsAlert(){
		var incompleteOrInprogressMsg = '<content:getAttribute beanName="incompleteOrInprogress" attribute="text" filter="true"/>';
		alert(incompleteOrInprogressMsg)
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
	
	function doClose() {
		downloadloadingPanel.hide();
	}
	
	//this function is called for Down load the individula pdf's according to the contract number
	function doDownloadPdf(contract, type) {
		alert("check status");
		downloadUtilities.showWaitPanel();
		document.forms['planReviewResultForm'].elements['pageRegularlyNavigated'].value = true;
		document.forms['planReviewResultForm'].elements['downloadContractId'].value = contract;
		document.forms['planReviewResultForm'].elements['type'].value = type;
		document.forms['planReviewResultForm'].elements['requestType'].value = 'download';
		document.forms['planReviewResultForm'].action="/do/bob/contract/planReview/Results/?task=downloadPdf";	
		document.forms['planReviewResultForm'].submit();

		intervalId = setInterval(doCheckDownloadPdfGenerated, 5000);    	
	}
	
	function doCheckDownloadPdfGenerated(){
	
		downloadUtilities.doDownAsyncRequest("/do/bob/contract/planReview/Results/?task=checkDownloadPdfGenerated&isPageRegularlyNavigated=true", callback_checkDownloadPdfGenerated);
	}
	
	// Call back handler to Check whether FundEvaluator Report Generation is complete.
	var callback_checkDownloadPdfGenerated =    {
	    cache : false,
	    failure : downloadUtilities.handleFailure
    };
	
	//Download Selected Button for selected  PDF's in Result page.
	   function doDownloadSelected(action)
	   {
		 
		    downloadUtilities.showWaitPanel();
			document.forms['planReviewResultForm'].elements['pageRegularlyNavigated'].value = true;
			document.forms['planReviewResultForm'].action="/do/bob/contract/planReview/Results/?task="+ action;			
			document.forms['planReviewResultForm'].submit();

			intervalId = setInterval(doCheckDownloadSelectedPdfGenerated, 5000); 
		}
		
		function doCheckDownloadSelectedPdfGenerated(){
			
			downloadUtilities.doDownAsyncRequest("/do/bob/contract/planReview/Results/?task=checkDownloadSelectedPdfGenerated&isPageRegularlyNavigated=true", callback_checkDownloadSelectedPdfGenerated);
		}
		
		// Call back handler to Check whether FundEvaluator Report Generation is complete.
		var callback_checkDownloadSelectedPdfGenerated =    {
		    cache : false,
		    failure : downloadUtilities.handleFailure
	    };

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
<bd:form method="post"	action="/do/bob/contract/planReview/Results/" modelAttribute="planReviewResultForm">
	<div id="contentOuterWrapper">
	<div id="contentWrapper">
		
		<div id="rightColumnOverview">
		<h2><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h2>
				<ul>
					<li><a id="doPlanRequest" href="javascript://"><content:getAttribute
								id="planReviewRequestLink" attribute="text" /></a></li>
					<li><a id="doprintPreview" href="javascript://"><content:getAttribute
								id="printPlanReviewRequestLink" attribute="text" /></a></li>
					<li><a id="doHistory" href="javascript://"><content:getAttribute
								id="historySummaryLink" attribute="text" /></a></li>
				</ul>
			</div>
		
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

	<c:set
		var="footNotes" value="${layoutBean.layoutPageBean.footnotes}" /> 	 <!-- Page Title and Headers-->
	<h2><content:getAttribute id="layoutPageBean" attribute="name" /></h2>
	<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName}
	(${bobContext.contractProfile.contract.contractNumber})</strong> <input
		class="btn-change-contract" type="button"
		onmouseover="this.className +=' btn-change-contract-hover'"
		onmouseout="this.className='btn-change-contract'"
		onclick="top.location.href='/do/bob/blockOfBusiness/Active/'"
		value="Change contract"></p>
	<div>
	</div>
	
			
	<%--Layout/intro1--%>
	<c:if test="${not empty layoutPageBean.introduction1}">
	   	<p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>
	
	<%--Layout/Intro2--%>
	<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
	
	<!-- Report Erros/Warning Messages -->
	<div class="errorMessage" style="DISPLAY: none;">	
		<report:formatMessages scope="session" suppressDuplicateMessages="true"/>
	</div>
	<report:formatMessages scope="request" />
	<report:formatMessages scope="session" suppressDuplicateMessages="true"/>
	<!-- Report Erros/Warning Messages -->
			<div class="errorMessage" style="DISPLAY: none;">
				<div class="message message_warning">
					<dl>
						<dt>Warning Message</dt>
						<dd>
							1.&nbsp;
							<content:getAttribute id="requestWithIncompleteStatusWarningMessage"
								attribute="text" />
						</dd>
					</dl>
				</div>
			</div>
	
	<!--Navigation bar--> <navigation:contractReportsTab /> <jsp:include
		page="/WEB-INF/planReview/common/resultsPlanReviewReports.jsp">
	</jsp:include>

	<div class="clear_footer"></div>
	<div class="footnotes">
	<div class="footer"><content:pageFooter beanName="layoutPageBean" /></div>
	<br>
	<c:if test="${not empty footNotes}">
		<dl>
			<dd><content:pageFootnotes beanName="layoutPageBean" /></dd>
		</dl>
	</c:if>
	<dl>
		<dd><content:pageDisclaimer beanName="layoutPageBean" index="-1" /></dd>
	</dl>
	<div class="footnotes_footer"></div>
	</div>
	</div>
	</div>
</bd:form>
