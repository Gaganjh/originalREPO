<%--  Imports  --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<%--  Tag Libraries  --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- Beans used --%>
<%
ContractInformationReportData theReport = (ContractInformationReportData)request.getAttribute(Constants.REPORT_BEAN);
  pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
  %> 


<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_BD_INFO_SECTION_TITLE%>"
                           	type="<%=BDContentConstants.TYPE_MESSAGE%>"
                          	id="brokerInfoTitle"/>

<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_BD_SHARE_INFO_SECTION_TITLE%>"
                           	type="<%=BDContentConstants.TYPE_MESSAGE%>"
                          	id="brokerShareInfoTitle"/>



<c:if test="${contractInformationForm.isBdFirmRep ==true}">
	<h4><content:getAttribute beanName="brokerInfoTitle" attribute="text"/></h4>
</c:if>
<c:if test="${contractInformationForm.isBdFirmRep !=true}">
	<h4><content:getAttribute beanName="brokerShareInfoTitle" attribute="text"/></h4>
</c:if>

<table width="343" class="overview_table_alt">

<c:if test="${contractInformationForm.isBdFirmRep ==true}">
<c:forEach items="${theReport.brokerShareInformations}" var="brokerInfo">
     <tr>
		 <td width="300">
${brokerInfo.brokerName}
         </td>
     </tr>
</c:forEach>
</c:if>
<c:if test="${contractInformationForm.isBdFirmRep !=true}">
		<thead>
			<tr>
				<th width="300"></th>
				<th colspan="4">
				<div align="center">Commissions</div>
				</th>
			</tr>
			<tr>
				<th width="300"></th>
				<th width="15">TR</th>
				<th width="15">REG</th>
				<th width="15">AB</th>
				<th width="100">Price Credit</th>
			</tr>
		</thead>
		<tbody>
<c:forEach items="${theReport.brokerShareInformations}" var="brokerInfo">
				<tr>
					<td width="300">
${brokerInfo.brokerName}
                    </td>
					<td width="15">
${brokerInfo.tranferCommission}%
                    </td>
					<td width="15">
${brokerInfo.regularCommission}%
                    </td>
					<td width="15">
${brokerInfo.assetBasedComission}%
                    </td>
<td width="100">${brokerInfo.priceCredit}%
                    </td>
				</tr>
</c:forEach>
		</tbody>
</c:if>
</table>
