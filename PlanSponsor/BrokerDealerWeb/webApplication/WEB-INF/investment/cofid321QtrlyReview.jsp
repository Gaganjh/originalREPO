<%--Taglibs Used--%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>

<%--Imports Used--%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page
	import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.content.valueobject.Miscellaneous" %>
<%@ page import="com.manulife.pension.platform.web.investment.CoFiduciary321QtrlyReviewPageForm" %>


<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="bdContentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />	
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />

<content:contentBean
	contentId="${bdContentConstants.COFID321_SERVICE_BROCHURE_LINK}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="coFid321ServiceBrochureLink" override="true" />

<content:contentBean
	contentId="${bdContentConstants.NO_COFID321_REPORTS_FOUND}"
	type="${bdContentConstants.TYPE_MISCELLANEOUS}"
	id="noCoFid321ReportsFound" override="true" />

<script type="text/javascript"> 

/**
* opens a window to show the PDF for the selected report
* by sending a request to the CoFiduciary321QtrlyReviewPDF action
*
*/
function showCoFidContractPDF(fileName) {
	var url = "/do/bob/investment/coFiduciary321QtrlyReviewPDFAction/?coFidContractDoc=";
	url = url+fileName;
	PDFWindow(url)
}
</script>

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
	CoFiduciary321QtrlyReviewPageForm coFiduciary321QtrlyReviewPageForm = (CoFiduciary321QtrlyReviewPageForm)session.getAttribute("coFiduciary321QtrlyReviewPageForm");
	pageContext.setAttribute("coFiduciary321QtrlyReviewPageForm",coFiduciary321QtrlyReviewPageForm,PageContext.PAGE_SCOPE);
%>




<%--Summary Box--%>
<div id="summaryBox" style="width: 300px;">
<h1>Additional Resources</h1>
<span style="color:#005B80; text-decoration:underline">
<content:getAttribute attribute="text" beanName="coFid321ServiceBrochureLink" />
</span>
</div>

<%--Page Title and Introduction Messages--%>
<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>

<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
  <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
</p>


<!--Layout/Intro1-->
<c:if test="${not empty layoutPageBean.introduction1}">
<table>
<tr>
<td style="font-family: Arial, Helvetica, sans-serif; font-size:0.75em; color:#767676; padding-left: 8px;">
	<content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
</td>
</tr>
</table>
</c:if>
        
<!--Layout/Intro2-->
<c:if test="${not empty layoutPageBean.introduction2}">
<table>
<tr>
<td style="font-family: Arial, Helvetica, sans-serif; font-size:0.75em; color:#767676; padding-left: 8px;">
	<content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
</td>
</tr>
</table>
</c:if>

<%--Navigation bar--%>
<navigation:contractReportsTab />
  
<!--Section title and Display preferences-->
<div class="page_section_subheader controls">
	<h3>
	Quarterly Reports as of:
	</h3>
	<form class="page_section_filter form">
		<p>
			<render:date property="coFiduciary321QtrlyReviewPageForm.currentDate"
				patternOut="${renderConstants.EXTRA_LONG_MDY}" defaultValue="" />
		</p>
	</form>
	
</div>

<!--Error- message box-->
<report:formatMessages scope="request" />

<c:if test="${coFiduciary321QtrlyReviewPageForm.coFidContractDocList != null}">

<div class="report_table">

<c:if test="${not empty coFiduciary321QtrlyReviewPageForm.coFidContractDocList}">

	<table class="report_table_content">
		<thead>
			<tr>
				<th width="40%" class="cur align_center">Provider</th>
				<th width="30%" class="cur align_center">Start Date</th>
				<th width="30%" class="cur align_center">End Date</th>
			</tr>
		</thead>
	
		<tbody>
<c:forEach items="${coFiduciary321QtrlyReviewPageForm.coFidContractDocList}" var="theItem" varStatus="theIndex" >


				
				<%-- This logic is to differentiate the alternate rows --%>
				<c:choose>
					<c:when test='${(theIndex.index) % 2 == 1}'>
						<tr class="spec">
					</c:when>
					<c:otherwise>
						<tr class="spec1">
					</c:otherwise>
				</c:choose>
					<td align="center">
<a href="javascript:showCoFidContractPDF('${theItem.contractDocName}')">
${theItem.providerDisplayName}</a>
					</td>
					<td align="center">
						<render:date property="theItem.quarterStartDate" patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" defaultValue = "" />
					</td>
					<td align="center">
						<render:date property="theItem.documentCreatedDate" patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" defaultValue = "" />
					</td>
				</tr>
</c:forEach>
		</tbody>
	</table>	
</c:if>

</div>

</c:if>

<div class="footnotes" style="color:#767690; margin: 0px 0px 0px 20px">
    <dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
    <dl><dd style="width:100%; margin: 0px 0px 0em 0em"><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
    <dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
    <div class="footnotes_footer"></div>
</div>

<script language="JavaScript" type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>
