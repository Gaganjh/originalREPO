<%-- Imports --%>
<%@ page
	import="com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page
	import="com.manulife.pension.util.PlanReviewConstants.PlanReviewDocumentType"%>
<%--  Tag Libraries  --%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib tagdir="/WEB-INF/tags/bob" prefix="bob"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<html>
<body>
<input type="hidden" name="contract" id="contractID" value="${e:forHtmlAttribute(requestScope.contract)}"/>
<input type="hidden" name="type" id="docType" value="${e:forHtmlAttribute(requestScope.type)}"/>
<input type="hidden" name="planReviewLevel" id="planReviewLevel" value="${requestScope.planReviewLevel}"/>
<input type="hidden" name="planReviewResultForm" id="_csrf" value="${requestScope._csrf.token}"/>

	<div id="view_panel" style="width: 100%; PADDING-TOP: 20%; padding-left:30%;">
			<table>
				<tr>
					<td style="float: left; margin-left: 26px; margin-top: 10px;">
						<img style="padding-top: 5px; padding-left: 5px;"
						src="/assets/unmanaged/images/ajax-wait-indicator.gif">&nbsp;&nbsp;
					</td>
					<td style="float: right; font-size: 14px;">
						<p style="font-family: sans-serif; color: #0F7397;">
							<b style="font-size: 16px; font-weight: bold;">PDF retrieval
								in progress.</b><b style="font-size: 14px;">&nbsp;Note:</b>&nbsp;This
							process may <br> take several minutes. Please do not close
							your browser.
						</p>
					</td>
				</tr>
			</table>
		</div>
</body>

<script type="text/javascript">
	
	var contract = document.getElementById("contractID").value;
	var type = document.getElementById("docType").value;
	var planReviewLevel = document.getElementById("planReviewLevel").value;
	var csrf = document.getElementById("_csrf").value;
	
	if(planReviewLevel == 'bob') {
		openPDF("/do/bob/planReview/Results/?task=viewPdf", contract, type,csrf);
	} else {
		openPDF("/do/bob/contract/planReview/Results/?task=viewPdf", contract, type,csrf);
	}

	//Opens the PDF in the same window
	function openPDF(url, contract, docType,csrf) {

		var form = document.createElement("form");
		form.action = url;
		form.method = 'POST';

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
</script>
</html>
