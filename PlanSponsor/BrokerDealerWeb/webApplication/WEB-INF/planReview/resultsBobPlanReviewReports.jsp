<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        


<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_HISTORY_SUMMARY_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="historySummaryLink"/> 
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_REQUEST_BOB_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="PlanReviewRequestLink"/> 
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_PRINT_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="PrintLink"/> 
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_REQUEST_SOME_IN_INPROGRESS%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="someInporgressContracts"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_REQUEST_ALL_IN_INPROGRESS%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="allInporgressContracts"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_REQUEST_ALL_IN_INPROGRESS%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="allIncompleteContracts"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_RESULTS_INCOMPLETE_ERROR_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="requestWithIncompleteStatusWarningMessage"/>
<%-- CL - DR 332240 - Plan Review download reports page - Limit # of PDF downloads --%>
<content:contentBean contentId="<%=BDContentConstants.MORE_THAN_30_CONTRACT_ROWS_ERROR_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="MoreThan30ContractsErrorMessage"/>

<script type="text/javascript">
	
	function allIncompleteContractsAlert(){
		var allIncompleteContractsMsg = '<content:getAttribute beanName="allIncompleteContracts" attribute="text" filter="true"/>';
		alert(allIncompleteContractsMsg)
	}
	
	function allInporgressContractsAlert(){
	var allInporgressContractsMsg = '<content:getAttribute beanName="allInporgressContracts" attribute="text" filter="true"/>';
		alert(allInporgressContractsMsg)
	}
	
	function someInporgressContractsConfirm() {
		var someInporgressContractsMsg = '<content:getAttribute beanName="someInporgressContracts" attribute="text" filter="true"/>';
		if (confirm(someInporgressContractsMsg)) {
			return true;
		} else {
			return false;
		}
	}

	//Opens the PDF in new window
	function openPDF(url, contract, docType, csrf) {

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

		var contractInput = document.createElement("input");
		contractInput.setAttribute("type", "hidden");
		contractInput.setAttribute("name", "contract");
		contractInput.setAttribute("value", contract);
		form.appendChild(contractInput);

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
	function doView(contract, type, csrf) {

		openPDF("/do/bob/planReview/Results/?task=openPdfWindow", contract, type,csrf);
	}

	
	function doBackToHistory(action) {
		document.forms['planReviewResultForm'].action = "/do/bob/planReview/Results/?task="
				+ action;
		navigate("planReviewResultForm");
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
	
	function doDownloadPdf(contract, type) {
		
		downloadUtilities.showWaitPanel();
		document.forms['planReviewResultForm'].elements['pageRegularlyNavigated'].value = true;
		document.forms['planReviewResultForm'].elements['downloadContractId'].value = contract;
		document.forms['planReviewResultForm'].elements['type'].value = type;
		document.forms['planReviewResultForm'].elements['requestType'].value = 'download';
		document.forms['planReviewResultForm'].action="/do/bob/planReview/Results/?task=downloadPdf";	
		document.forms['planReviewResultForm'].submit();

		intervalId = setInterval(doCheckDownloadPdfGenerated, 5000);    	
	}

	// CL - DR 332240 - Plan Review download reports page - Limit # of PDF downloads
	function doDownloadPdfLimit() {
		var limit = 30;
		var moreThan30ContractsMsg = '<content:getAttribute beanName="MoreThan30ContractsErrorMessage" attribute="text" filter="true"/>';
		var checkboxgroupSize = document.getElementById('checkboxgroup').getElementsByTagName("input");
		for (var i = 0; i < checkboxgroupSize.length; i++) {
			if (checkboxgroupSize[i].type === 'checkbox') {
				checkboxgroupSize[i].onclick=function(){
					var checkedcount=0;
					for (var j=0; j<checkboxgroupSize.length; j++){
						checkedcount+=(checkboxgroupSize[j].checked)? 1 : 0;
					}
					if (checkedcount>limit){
						alert(moreThan30ContractsMsg);
						this.checked=false;
						$("#planReviewReportPdfsSelected" + this.id).val(this.checked);
					}
				}
			}
		}
		  
	}
	
	function doCheckDownloadPdfGenerated(){
	
		downloadUtilities.doDownAsyncRequest("/do/bob/planReview/Results/?task=checkDownloadPdfGenerated&isPageRegularlyNavigated=true", callback_checkDownloadPdfGenerated);
	}
	
	// Call back handler to Check whether FundEvaluator Report Generation is complete.
	var callback_checkDownloadPdfGenerated =    {
	    cache : false,
	    failure : downloadUtilities.handleFailure
    };
	
	//Download Selected Button for selected  PDF's in Result page.
	function doDownloadSelected(action) {
		
		downloadUtilities.showWaitPanel();
		document.forms['planReviewResultForm'].elements['pageRegularlyNavigated'].value = true;
		document.forms['planReviewResultForm'].action="/do/bob/planReview/Results/?task="+ action;			
		document.forms['planReviewResultForm'].submit();

		intervalId = setInterval(doCheckDownloadSelectedPdfGenerated, 5000); 
	}
	
	function doCheckDownloadSelectedPdfGenerated(){
		
		downloadUtilities.doDownAsyncRequest("/do/bob/planReview/Results/?task=checkDownloadSelectedPdfGenerated&isPageRegularlyNavigated=true", callback_checkDownloadSelectedPdfGenerated);
	}
	
	// Call back handler to Check whether FundEvaluator Report Generation is complete.
	var callback_checkDownloadSelectedPdfGenerated =    {
	    cache : false,
	    failure : downloadUtilities.handleFailure
    };
	
	function doClose() {
		downloadloadingPanel.hide();
	}

</script>

<bd:form method="post" action="/do/bob/planReview/Results/" modelAttribute="planReviewResultForm">
<input type="hidden" name="bobResults" class="bob_results" value="${sessionScope.bobResults}"/>
	<div id="contentOuterWrapper">
	<div id="contentWrapper">
	<div id="rightColumnOverview">
		<h2><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h2>
		<ul>
			<li><a href="javascript://" id="doPlanRequest"><content:getAttribute id="PlanReviewRequestLink" attribute="text"/></a></li>
			<li><a href="javascript://" id="doprintPreview"><content:getAttribute id="PrintLink" attribute="text"/></a></li>
			<li><a href="javascript://" id="doHistory"><content:getAttribute id="historySummaryLink" attribute="text"/></a></li>
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
		<jsp:include
		page="/WEB-INF/planReview/common/resultsPlanReviewReports.jsp">
	</jsp:include></div>
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
