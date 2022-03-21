<%-- Imports --%>

<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractProfileVO"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%--  Tag Libraries  --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_REQUEST_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="PlanReviewRequestLink"/>
<content:contentBean contentId="<%=BDContentConstants.ACCESS_PLAN_REVIEW_HISTORY_LINK%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="historySummaryLink"/>
<content:contentBean contentId="<%=BDContentConstants.SELECT_ONE_CONTRACT_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="SelectOneContractMessage"/>
<script type="text/javascript">
function sortSubmit(sortfield, sortDirection){    
    document.forms['planReviewPrintForm'].action = "/do/bob/contract/planReview/Print/?task=sort" ;
	document.forms['planReviewPrintForm'].elements['sortField'].value = sortfield;
	document.forms['planReviewPrintForm'].elements['sortDirection'].value = sortDirection;
	navigate("planReviewPrintForm");
	//document.forms['planReviewPrintForm'].submit();
}

//this function is calling the History page.
function doNavigate(action) {
	document.forms['planReviewPrintForm'].action = "/do/bob/contract/planReview/Print/?task="
			+ action;
	navigate("planReviewPrintForm");
}

var selectOneContract ='<content:getAttribute id="SelectOneContractMessage" attribute="text" filter="true"/>';
</script>
 
 <bd:form method="post" action="/do/bob/contract/planReview/Print/" modelAttribute="planReviewPrintForm">
 <div id="contentOuterWrapper">
<div id="contentWrapper">

<div id="rightColumnOverview">
		<h2><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h2>
		<ul>
			<li><a href="javascript:doNavigate('planReviewRequest')"> <strong> </strong><content:getAttribute id="PlanReviewRequestLink" attribute="text"/></a></li>
			<li><a href="javascript:doNavigate('history')"> <strong> </strong><content:getAttribute id="historySummaryLink" attribute="text"/></a></li>
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
	<report:formatMessages scope="session" suppressDuplicateMessages="true"/>

<!--Navigation bar-->
<navigation:contractReportsTab />

<jsp:include page="/WEB-INF/planReview/common/printPlanReviewReports.jsp">
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
