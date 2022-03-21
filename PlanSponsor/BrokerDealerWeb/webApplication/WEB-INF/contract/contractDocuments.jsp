<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@	taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.bob.contract.ContractDocuments"%>
<%-- Constant Files used--%>
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />

<script type="text/javascript">

/**
* opens a window to show the PDF for the selected report
* by sending a request to the ContractDocumentsPDF action
*
*/
function showAmendmentPDF(theSelect) {
	var url = "/do/bob/contract/contractDocumentsPDF/?contractDoc=";
	var key = theSelect.options[theSelect.selectedIndex].value;
	if (key.length > 0) {
	url = url+key;
	PDFWindow(url)
	}
}

/**
* opens a window to show the PDF for the selected report
* by sending a request to the ContractDocumentsPDF action
*
*/
function showContractPDF(fileName) {
	var url = "/do/bob/contract/contractDocumentsPDF/?contractDoc=";
	url = url+fileName;
	PDFWindow(url)
}

</script>

<%
BobContext bobContext=(BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
pageContext.setAttribute("bobContext",bobContext,pageContext.PAGE_SCOPE);
%>

<!-- Page Title and Headers-->
<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />

<!--Error- message box-->
<report:formatMessages scope="request" />
<br/>

<!--Navigation bar-->
<navigation:contractReportsTab />

<!--Report Title-->
<div class="page_section_subheader controls">
<h3><content:getAttribute beanName="layoutPageBean" attribute="subHeader" /></h3>
</div>
<c:if test="${not empty contractDocuments}">
<%
ContractDocuments contractDocuments=(ContractDocuments)request.getAttribute("contractDocuments");
pageContext.setAttribute("contractDocuments",contractDocuments,pageContext.PAGE_SCOPE);
%>
	<div class="page_section_subsubheader">
	<h3><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></h3>
	</div>

	<div class="report_table">
	<div class="clear_footer"></div>
	<table class="report_table_content">
		<tbody>
			<tr class="spec">
				<td class="name">
				<h4>
				<c:if test="${not empty contractDocuments.contractDocName}">
					<p><content:getAttribute beanName="layoutPageBean" attribute="body1" />
					<content:contentBean contentId="${contentConstants.MISCELLANEOUS_DOWNLOAD_CONTRACT_LINK}"
						type="${contentConstants.TYPE_MISCELLANEOUS}"
						id="downloadContract" /> <br/><br/>
					<a href="javascript:showContractPDF('${contractDocuments.contractDocName}')">
					<content:getAttribute beanName="downloadContract" attribute="text" />
					</a></p>
				</c:if>
					<c:if test="${empty contractDocuments.contractDocName}">
					<content:contentBean contentId="${contentConstants.MISCELLANEOUS_CONTRACT_NOT_AVAILABLE}"
						type="${contentConstants.TYPE_MISCELLANEOUS}" id="messagesNoContractDoc" />
					<p><content:getAttribute beanName="messagesNoContractDoc" attribute="text" /></p>
				</c:if>
                </h4>
				</td>
			</tr>
		</tbody>
	</table>

	<div class="page_section_subsubheader">
	<h3><content:getAttribute beanName="layoutPageBean" attribute="body2Header" /></h3>
	</div>

	<table class="report_table_content">
		<tbody>
			<tr class="spec">
				<td class="name">
				<h4>
				<c:if test="${not empty amendmentDocs}">
                    <p><content:getAttribute beanName="layoutPageBean" attribute="body2" /><br/><br/>
					    <bd:select name="contractDocuments" property="amendmentOption" onchange="showAmendmentPDF(this);">
						<bd:option value="">${contractDocuments.amendmentOption}</bd:option>
						<bd:options collection="amendmentDocs" property="value" labelProperty="label" />
					</bd:select></p>

				</c:if>

               <c:if test="${empty amendmentDocs}">
					<content:contentBean contentId="${contentConstants.MISCELLANEOUS_AMENDMENTS_NOT_AVAILABLE}"
						type="${contentConstants.TYPE_MISCELLANEOUS}" id="messages" />
					<p><content:getAttribute beanName="messages" attribute="text" /></p>
				</c:if>

                </h4>
				</td>
			</tr>
		</tbody>
	</table>
	</div>

	<%-- FootNotes and Disclaimer --%>
	<layout:pageFooter/>
	<%--end of footnotes--%>

</c:if>
