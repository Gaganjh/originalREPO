<%--  Imports  --%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%--  Tag Libraries  --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- Beans used --%>

<%
ContractInformationReportData theReport = (ContractInformationReportData)request.getAttribute(BDConstants.REPORT_BEAN);
  pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
  
  String TRUE=BDConstants.TRUE;
  pageContext.setAttribute("TRUE",TRUE,PageContext.PAGE_SCOPE);
  BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
  pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
  
  
  %>
	







<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_STATEMENT_DETAILS_SECTION_TITLE%>"
                           	type="<%=BDContentConstants.TYPE_MESSAGE%>"
                          	id="statementDetailsSectionTitle"/>

<h4><content:getAttribute beanName="statementDetailsSectionTitle" attribute="text"/></h4>
<table class="overview_table">
	<tbody>
		<tr>
			<th width="56%">Basis:</th>
			<td width="44%">
${theReport.contractProfileVo.statementInfo.basis}
            </td>
		</tr>
		<tr>
			<th>Last Printed:</th>
			<td>
			    <render:date property="theReport.contractProfileVo.statementInfo.lastPrintDate" defaultValue="" />
            </td>
		</tr>

<c:if test="${bobContext.currentContract.definedBenefitContract !=true}">
			<tr>
				<th>Delivery Method:</th>
				<td>
${theReport.contractProfileVo.statementInfo.deliveryMethod}
                </td>
			</tr>
			<tr>
				<th>Statement Type:</th>
				<td>
				<c:if test="${theReport.contractProfileVo.statementInfo.statementType != ' '}">
				
                   
${theReport.contractProfileVo.statementInfo.statementType}
</c:if>
<c:if test="${theReport.contractProfileVo.statementInfo.statementType == ' '}">
                    
				     No Statement
</c:if>

                </td>
			</tr>

			<tr>
				<th>Permitted Disparity:</th>
<c:if test="${theReport.isPermittedDisparity ==true}">
					<td>Yes</td>
</c:if>
<c:if test="${theReport.isPermittedDisparity !=true}">
					<td>No</td>
</c:if>
			</tr>
			<tr>
				<th>Vesting Shows on Statements:</th>
                <td> <render:yesno  property="theReport.isVestingShownOnStatements"/></td>
			</tr>
</c:if>
	</tbody>
</table>
