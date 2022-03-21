<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.util.BDSessionHelper" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>
<%@ page import="com.manulife.pension.platform.web.CommonConstants" %>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import ="com.manulife.pension.bd.web.bob.contract.ContractStatements" %>
<%-- <%@ page import="com.manulife.pension.ps.web.Constants" %> --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>


<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>


<%-- Constant Files used--%>
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />
	
	
<script type="text/javascript">
var secondaryWindowName = "2ndwindow";
var secondayWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";

/**
* opens a window to show the PDF for the selected report
* by sending a request to the statement PDF action
*
*/
function showPDF(theSelect) {
	var url = "/do/bob/contract/contractStatementsPDF/?statement=";
	var key = theSelect.options[theSelect.selectedIndex].value;
	if (key.length > 0) {
		url = url+key;
		PDFWindow(url);
	}
}
function doGoToContractDocuments() {
  document.getElementById('contractDocumentsLink').value='true';
  document.contractDocuments.submit();
}
</script>

<%-- <form name="contractDocuments" method="post" action="/do/bob/contract/contractDocuments/" > --%>
<bd:form action="/do/bob/contract/contractDocuments/" method="post" modelAttribute="contractDocuments" name="contractDocuments">
      <input id="contractDocumentsLink" type="hidden" name="contractDocuments" value="false"/>
</bd:form>
<%-- </form> --%>

<%-- This jsp includes the following CMA content --%>
<content:contentBean
    contentId="${contentConstants.CONTRACT_STATEMENTS_TABLE_TITLE1}" type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="Contract_Statements_Table_Title1"/>

<content:contentBean contentId="${contentConstants.EMP_SMT_HEADER}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="emp_smt_header"/>
<content:contentBean contentId="${contentConstants.EMP_SMT_INTRO}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="emp_smt_intro"/>

<content:contentBean contentId="${contentConstants.PLN_ADM_HEADER}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="pln_adm_header"/>
<content:contentBean contentId="${contentConstants.PLN_ADM_INTRO}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="pln_adm_intro"/>

<content:contentBean contentId="${contentConstants.SCH_A_HEADER}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="sch_a_header"/>
<content:contentBean contentId="${contentConstants.SCH_A_INTRO}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="sch_a_intro"/>

<content:contentBean contentId="${contentConstants.SCH_B_HEADER}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="sch_c_header"/>
<content:contentBean contentId="${contentConstants.SCH_B_INTRO}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="sch_c_intro"/>

<content:contentBean contentId="${contentConstants.CLS_CON_HEADER}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="cls_con_header"/>
<content:contentBean contentId="${contentConstants.CLS_CON_INTRO}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="cls_con_intro"/>

<content:contentBean contentId="${contentConstants.BNF_PMT_HEADER}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="bnf_pmt_header"/>
<content:contentBean contentId="${contentConstants.BNF_PMT_INTRO}" 
type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="bnf_pmt_intro"/>


<content:contentBean
	contentId="<%=BDContentConstants.MESSAGE_NO_CONTRACT_STATEMENTS%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"  beanName="messages"/>

<!-- Page Title and Headers-->
<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />

<!--Error- message box-->
<report:formatMessages scope="request" />
<br/>

<!--Navigation bar-->
<navigation:contractReportsTab /> 

<!--Report Title-->
<div class="page_section_subheader controls">
<h3><content:getAttribute id="layoutPageBean" attribute="name"/></h3>
</div>

<% 
	ContractStatements contractStatements = (ContractStatements)request.getAttribute(BDConstants.CONTRACT_DOCUMENTS_KEY);
	pageContext.setAttribute("contractStatements",contractStatements,PageContext.PAGE_SCOPE);
%>

<c:if test="${not empty contractStatements}">






	<div class="report_table">
	
<!------------------------------------- efOptions combo box ------------------------------------>
	<div class="page_section_subsubheader">
	<h3><content:getAttribute beanName="emp_smt_header" attribute="text"/></h3>
	</div>	
	<table class="report_table_content">
		<tbody>
			<tr class="spec">
				<td class="name">
				<h4><p>
                <content:getAttribute beanName="emp_smt_intro" attribute="text"/><br/><br/>
<c:if test="${not empty contractStatements.efOptions}">
			    	<bd:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;">
                		<bd:option value="">${contractStatements.statementOption}</bd:option>
                		<bd:options collection="efOptions" property="value" labelProperty="label"/>
                	</bd:select>
</c:if>
<c:if test="${empty contractStatements.efOptions}">
					<b><content:getAttribute beanName="messages" attribute="text"/></b>
</c:if>
                </p></h4>
				</td>
			</tr>
		</tbody>
	</table>
	
<!------------------------------------- paOptions combo box ------------------------------------>
	<div class="page_section_subsubheader">
	<h3><content:getAttribute beanName="pln_adm_header" attribute="text"/></h3>
	</div>	
	<table class="report_table_content">
		<tbody>
			<tr class="spec">
				<td class="name">
				<h4><p>
                <content:getAttribute beanName="pln_adm_intro" attribute="text"/><br/><br/>
<c:if test="${not empty contractStatements.paOptions}">
                	<bd:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;">
                		<bd:option value="">${contractStatements.reportOption}</bd:option>
                		<bd:options collection="paOptions" property="value" labelProperty="label"/>
                	</bd:select>
</c:if>
<c:if test="${empty contractStatements.paOptions}">
					<b><content:getAttribute beanName="messages" attribute="text"/></b>
</c:if>
                </p></h4>
				</td>
			</tr>
		</tbody>
	</table>
	
<!------------------------------------- saOptions combo box ------------------------------------>
  <c:if test="${not empty requestScope.mtaContract}">
	<div class="page_section_subsubheader">
	<h3><content:getAttribute beanName="sch_a_header" attribute="text"/></h3>
	</div>		
	<table class="report_table_content">
		<tbody>
			<tr class="spec">
				<td class="name">
				<h4><p>
	            <content:getAttribute beanName="sch_a_intro" attribute="text"/><br/><br/>
<c:if test="${not empty contractStatements.saOptions}">
	                	<bd:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;"> 
	                		<bd:option value="">${contractStatements.reportOption}</bd:option>
	                	    <bd:options collection="saOptions" property="value" labelProperty="label"/>
	                	</bd:select>
</c:if>
<c:if test="${empty contractStatements.saOptions}">
					<b><content:getAttribute beanName="messages" attribute="text"/></b>
</c:if>
                </p></h4>
				</td>
			</tr>
		</tbody>
	</table>
</c:if>
	
<!------------------------------------- scOptions combo box ------------------------------------>
  <c:if test="${not empty requestScope.mtaContract}">
<c:if test="${not empty contractStatements.scOptions}">
	<div class="page_section_subsubheader">
	<h3><content:getAttribute beanName="sch_c_header" attribute="text"/></h3>
	</div>		
	<table class="report_table_content">
		<tbody>
			<tr class="spec">
				<td class="name">
				<h4><p>
				<content:getAttribute beanName="sch_c_intro" attribute="text"/><br/><br/>
               <bd:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;"> 
	                		<bd:option value="">${contractStatements.reportOption}</bd:option>
	                	    <bd:options collection="scOptions" property="value" labelProperty="label"/>
	               </bd:select>
                </p></h4>
				</td>
			</tr>
		</tbody>
	</table>
</c:if>
</c:if>
<!------------------------------------- crOptions combo box ------------------------------------>	
<c:if test="${not empty contractStatements.crOptions}">
		<div class="page_section_subsubheader">
		<h3><content:getAttribute beanName="cls_con_header" attribute="text"/></h3>
		</div>
		<table class="report_table_content">
		<tbody>
			<tr class="spec">
				<td class="name">
				<h4><p>
				<content:getAttribute beanName="cls_con_intro" attribute="text"/><br/><br/>
				<bd:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;"> 
					<bd:option value="">${contractStatements.reportOption}</bd:option>
				    <bd:options collection="crOptions" property="value" labelProperty="label"/> 
               	</bd:select> 
                </p></h4>
				</td>
			</tr>
		</tbody>
	   </table>
</c:if>
<!------------------------------------- bpOptions combo box ------------------------------------>		
	<% 
BobContext bobContext = BDSessionHelper.getBobContext(request);
boolean isDB = bobContext.getCurrentContract().isDefinedBenefitContract();
if (isDB) { 
     %>
	<div class="page_section_subsubheader">
	<h3><content:getAttribute beanName="bnf_pmt_header" attribute="text"/></h3>
	</div>

	<table class="report_table_content">
		<tbody>
			<tr class="spec">
				<td class="name">
				<h4><p>
<c:if test="${not empty contractStatements.bpOptions}">
                <content:getAttribute beanName="bnf_pmt_intro" attribute="text"/><br/><br/> 
				<bd:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;"> 
				<bd:option value="">${contractStatements.reportOption}</bd:option>
			    <bd:options collection="bpOptions" property="value" labelProperty="label"/> 
	                	</bd:select> 
</c:if>
<c:if test="${empty contractStatements.bpOptions}">
						<b><content:getAttribute beanName="messages" attribute="text"/></b>
</c:if>
                </p></h4>
				</td>
			</tr>
		</tbody>
	</table>
<%  } %>
	
	
</div>

<%-- FootNotes and Disclaimer --%>
<layout:pageFooter/>
<%--end of footnotes--%>

</c:if>
