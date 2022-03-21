<%@ taglib prefix="report" uri="manulife/tags/report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.handler.TransactionType" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountBalanceDetailsReportData" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
CashAccountBalanceDetailsReportData theReport = (CashAccountBalanceDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 



             
<%-- Error Table --%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td width="100%" valign="top">
		<p><content:errors scope="session" /></p>
	</td>
</tr>
</table>

<%-- Tabs Filters, summary and Report table --%>
<ps:form  method="POST" modelAttribute="balanceDetailsReportForm" name="balanceDetailsReportForm" action="/do/transaction/cashAccountBalanceDetailsReport/">
<c:if test="${not empty param.printFriendly}" >
	<table width="634" border="0" cellspacing="0" cellpadding="0">
</c:if>
<c:if test="${empty param.printFriendly}" >
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>
<tr>
	<%-- Tab navigation JSP --%>
	<td>
		<jsp:include flush="true" page="cashAccountTabBar.jsp">
			<jsp:param name="selectedTab" value="BalanceDetailsTab"/>
		</jsp:include>	
	</td>
</tr>
<tr>
	<td width="100%" valign="top">		
	<c:if test="${empty theReport}">
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		</table>
	</c:if>			

	<c:if test="${not empty theReport}">



      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      	<%-- Dummy row to make the width consistant --%>
        <tr>
        	<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="70"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="70"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="224"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="80"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="95"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="99"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        
        <!--  From & To Dates, Total transactions and paging -->
        <tr class="tablehead">
			<td class="tableheadTD" valign="middle" colspan="8">
            	from&nbsp; 
            	<render:date patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" property="theReport.fromDate"/>&nbsp;
            	to&nbsp;
            	<render:date patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" property="theReport.toDate"/>&nbsp;
            
          		<b><report:recordCounter report="theReport" label="Transactions :"/></b>
          	</td>          
          	<td align="right" colspan="5" style="color:#ffffff;"><report:pageCounter report="theReport"/></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        
        <!--  Summary section -->
        <tr>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td colspan="12">
            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr class="datacell1">
                	<td valign="top" width=214" align="left">
                  		<span style="font-size: 17px;font-weight:bold;">Current balance:</span>
                	</td>
                	<td align="right" width="214" valign="top" >
                  		<h4><span class="highlight"><render:number property="theReport.currentBalance" type="c"/></span></h4>
                	</td>
                	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                	<td valign="top" colspan="9" width="284"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              	</tr>
              	<tr class="datacell1">
	             	<td colspan="12" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>
              	<tr class="datacell1">
                	<td colspan="12"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>	              	
                </tr>
            	</table>
          	</td>
          	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
        </tr>
        
<c:if test="${empty theReport.details}">
<c:if test="${theReport.hasTooManyItems ==true}">
        		<content:contentBean contentId="<%=ContentConstants.MESSAGE_TOO_MANY_TRANSACTIONS_FOR_DATE_SELECTED%>"
                                 type="<%=ContentConstants.TYPE_MESSAGE%>"
                                 id="CashAccountTransactionMessage"/>
</c:if>
<c:if test="${theReport.hasTooManyItems ==false}">
            	<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_BALANCE_DETAILS_TRANSACTION_FOR_DATE_SELECTED%>"
                                   type="<%=ContentConstants.TYPE_MESSAGE%>"
                                   id="CashAccountTransactionMessage"/>
</c:if>
			<tr class="datacell1">
            	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              	<td colspan="12">
                	<content:getAttribute id="CashAccountTransactionMessage" attribute="text"/>
              </td>
              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
</c:if>

<c:if test="${not empty theReport.details}">
	        <tr class="tablesubhead">
	          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	          <td align="left"><b>Transaction date</b></td>
	          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	          <td align="left"><b>Type</b></td>
	          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	          <td align="left"><b>Description</b></td>
	          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	          <td align="center"><b>Transaction number</b></td>
	          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	          <td align="right"><b>Original amount($)</b></td>
	          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	          <td align="right" colspan="2"><b>Available amount($)</b></td>
	          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        </tr>
    
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/> 
	<%String temp = pageContext.getAttribute("indexValue").toString(); %>
		<% if (Integer.parseInt(temp) % 2 == 0) { %>
			  <tr class="datacell1">
	        <% } else { %>
	          <tr class="datacell2">
						            <% } %>
        
				<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		        <td valign="top">
		        	<render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"
		                         property="theItem.transactionDate"/>
		        </td>
		        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td valign="top">
<c:if test="${not empty theItem.typeDescription1}">
${theItem.typeDescription1}
</c:if>
<c:if test="${not empty theItem.typeDescription2}">
<br>${theItem.typeDescription2}
</c:if>
		      	</td>
				<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              	<td align="left" valign="top" nowrap>
              		<ps:formatDescription item="theItem" linkParticipant="false" width="220" hideTransactionInProgress="true" />
              	</td>
	          	<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td align="center" valign="top">${theItem.transactionNumber}</td>
	          	<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	          	<td align="right" valign="top">
<c:if test="${not empty theItem.originalAmount}">
		            	<render:number property="theItem.originalAmount" type="c" sign="false"/>
</c:if>
	          	</td>
  	          	<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              	<td align="right" valign="top" colspan="2">
<c:if test="${not empty theItem.availableAmount}">
<c:if test="${theItem.availableAmount !=0}">
	                    	<render:number property="theItem.availableAmount" type="c" sign="false"/>
</c:if>
</c:if>
	          	</td>
	          	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        </tr>
</c:forEach>
</c:if>

		<tr>
			<td colspan="14" class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
		</tr>
		<tr>
			<td align="right" colspan="14"><report:pageCounter report="theReport" arrowColor="black"/></td>
		</tr>	                      
      	<tr>
			<td colspan="13">
			<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
 			</td>
	  	</tr> 
		</table>
	</c:if>
	</td>
</tr>
</table>
</ps:form>	

<c:if test="${not empty param.printFriendly}" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure"/>
        
	<table width="634" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
