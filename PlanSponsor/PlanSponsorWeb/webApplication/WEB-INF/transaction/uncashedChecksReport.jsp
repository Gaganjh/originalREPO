<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.UncashedChecksReportItem" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.UncashedChecksReportData" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract"%>
<%@ page import="com.manulife.pension.service.contract.ContractConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

	

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NO_UNCASHED_CHECKS_AVAILABLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     beanName="uncashedChecksReport"/>
					 
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_CONTRACT_STATUS_DISCONTINUE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     beanName="discontinueContract"/>					 
					 
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NON_DB_AND_NON_DI_CONTRACT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     beanName="nonDBandNonDIContract"/>
					 
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_DB_AND_NON_DI_CONTRACT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     beanName="dbAndNonDIContract"/>						 
					 


<c:if test="${empty param.printFriendly}">
<style>

#detailsTable{
	width : 715
}

#itemDate {
	width : 15
}

#itemName {
	width : 148;
}

#itemPayeeName {
	width : 120;
}


#itemPayeeType {
	width : 50
}

#itemAmount {
	width : 120
}

#itemTransactionType {
	width : 50
}
#itemTransactionNumber {
	width : 85
}

#itemCheckStatus {
	width:65
}
 
</style>
</c:if>

<c:if test="${not empty param.printFriendly}">
<style>

#detailsTable{
	width : 715
}

#itemDate {
	width : 15
}

#itemName {
	width : 148;
}

#itemPayeeName {
	width : 120;
}

#itemPayeeType {
	width : 50
}

#itemAmount {
	width : 120
}

#itemTransactionType {
	width : 50
}
#itemTransactionNumber {
	width : 85
}

#itemCheckStatus {
	width:65
}

</style>
</c:if>
      <p>
	<content:errors scope="session" />
      </p>

	  
	 

<%
UncashedChecksReportData theReport = (UncashedChecksReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 

 <c:if test="${not empty theReport}">


	
			 <c:set var="status_discontinued" value = "<%=ContractConstants.ContractStatus.DISCONTINUED%>"/>
			 <c:set var = "isDbContract" value = "${userProfile.currentContract.definedBenefitContract}"/>
			 <c:choose>
				<c:when test="${userProfile.currentContract.status eq status_discontinued}">
					<span id="noLoans"><content:getAttribute beanName="discontinueContract" attribute="text"/></span>
				</c:when>
				<c:when test="${userProfile.currentContract.status ne status_discontinued && !isDbContract}">
					<span id="noLoans"><content:getAttribute beanName="nonDBandNonDIContract" attribute="text"/></span>
				</c:when>
				<c:when test="${userProfile.currentContract.status ne status_discontinued && isDbContract}">
					<span id="noLoans"><content:getAttribute beanName="dbAndNonDIContract" attribute="text"/></span>
				</c:when>
			</c:choose>
	
	<ps:form method="POST" modelAttribute="uncashedChecksReportForm" name="uncashedChecksReportForm" action="/do/transaction/uncashedChecksReport/">
	  
	  <table border="0" cellspacing="0" cellpadding="0" id="detailsTable">
        <tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemDate"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		  
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemName"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		  
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemPayeeType"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
			
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemPayeeName"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
			
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="checkAmount"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
			
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemDate"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
			
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemTransactionType"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
			
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemTransactionNumber"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemCheckStatus"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
			
			<td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="tablehead">
		<td class="tableheadTD1" valign="middle" colspan="7">
            <p><b>Uncashed checks</b> as of  <b><render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" property="theReport.asOfDate"/>
          </td>
		  <td class="tableheadTD" align="middle" colspan="7">
		  <b>
   		    <c:if test="${empty param.printFriendly}" >
				<report:recordCounter report="theReport" label="Uncashed checks"/>
			</c:if>
		  </b></td>
          <td class="tableheadTDinfo" colspan="6" align="right"><report:pageCounter report="theReport" formName="uncashedChecksReportForm"/></td>
        </tr>
		<tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			
            <td valign="center" align="left" height="1" colspan="8"><b><font size="3">Total value of uncashed checks:</font></b></td>
            <td align="left" valign="left" colspan="10" height="1"><b><font size="3"><span class="highlight"><render:number property="theReport.uncashedChecksValue" type="c"/></span></font></b></td>
			</font>
            <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" ></td>
		</tr>
		<tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
          <td colspan="18"><img src="/assets/unmanaged/images/s.gif" height="10"></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
        </tr> 
		<tr class="datacell2">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td valign="top" align="left" colspan="4" height="1"><b>Number of stale dated checks:</b></td>
				<td align="right" valign="top" colspan="2" height="1"><b><span class="highlight"><render:number property="theReport.numStaleDatedChecks" type="i" /></span></b></td>
				<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td valign="top" align="left" colspan="6" height="1"><b>Total value of stale dated checks:</b></td>
				<td align="right" valign="top" colspan="4" height="1"><b><span class="highlight"><render:number property="theReport.staleDatedChecksValue" type="c"/></span></b></td>
				<td ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			
		</tr>
		<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td valign="top" align="left" colspan="4"><b>Number of outstanding checks:</b></td>
				<td align="right" valign="top" colspan="2"><b><span class="highlight"><render:number property="theReport.numOutstandingChecks" type="i" /></span></b></td>
				<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td valign="top" align="left" colspan="6"><b>Total value of outstanding checks:</b></td>
				<td align="right" valign="top" colspan="4"><b><span class="highlight"><render:number property="theReport.outstandingChecksValue" type="c"/></span></b></td>
				<td ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

			<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
		<tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="beigeborder" colspan="18"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr> 
		 <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
          <td colspan="18"><img src="/assets/unmanaged/images/s.gif" height="10"></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
        </tr> 
		
		<tr class="tablesubhead">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td align="left" valign="top"><report:sort field="<%=UncashedChecksReportItem.SORT_CHECK_ISSUE_DATE%>" direction="asc" formName="uncashedChecksReportForm"><b>Check issue date</b></report:sort></td>
		
		<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td align="left" valign="top"><b><report:sort field="<%=UncashedChecksReportItem.SORT_PARTICIPANT_NAME%>" direction="asc" formName="uncashedChecksReportForm">Participant name</report:sort></b></td>
		
		<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td align="left" valign="top"><b>Payee type</b></td>
		
		<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td align="left" valign="top"><b><report:sort field="<%=UncashedChecksReportItem.SORT_PAYEE_NAME%>" direction="asc" formName="uncashedChecksReportForm">Payee name</report:sort></b></td>
		
		<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td align="right" valign="top"><b><report:sort field="<%=UncashedChecksReportItem.SORT_CHECK_AMOUNT%>" direction="desc" formName="uncashedChecksReportForm">Check amount &nbsp;($)</report:sort></b></td>
		
		<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td align="left" valign="top"><b>Transaction date</b></td>
		
		<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td align="left" valign="top"><b>Transaction type</b></td>
		
		<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td align="left" valign="top"><b>Transaction number</b></td>
		
		<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td align="left" valign="top"><b><report:sort field="<%=UncashedChecksReportItem.SORT_CHECK_STATUS%>" direction="asc" formName="uncashedChecksReportForm">Check status</report:sort></b></td>
		
		<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
		  
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:choose><c:when test="${theIndex.index % 2==0}">
<tr class="datacell1">
</c:when><c:otherwise><tr class="datacell2"></c:otherwise>
</c:choose>



	                       
<%--         <% if (theIndex.intValue() % 2 == 0) { %> --%>
<!--           <tr class="datacell1"> -->
<%--         <% } else { %> --%>
<!--           <tr class="datacell2"> -->
<%--         <% } %>         --%>

          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top">
				<render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" property="theItem.checkIssueDate"/>
		  </td>
		  
		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td valign="top">
		  <ps:map id="parameterMap">
			<ps:param name="profileId" valueBeanName="theItem" valueBeanProperty="profileId"/>
		  </ps:map> 
		  <ps:link action="/do/participant/participantAccount/" name="parameterMap">
${theItem.participantName}
			</ps:link>
				<br>
<c:if test="${not empty theItem.ssn}">
					<render:ssn property="theItem.ssn"/>
</c:if>
				
		  </td>
		  
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top">
${theItem.payeeType}
		  </td>
		  
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td valign="top">
${theItem.payeeName}
		  </td>
		  
		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td align="right" valign="top">
<c:if test="${not empty theItem.checkAmount}">
	                  <render:number property="theItem.checkAmount" type="c" sign="false"/>
</c:if>
		  </td>

		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  
		  <td valign="top">
<c:if test="${not empty theItem.transactionDate}">
			<render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.transactionDate"/>
</c:if>
		  </td>
		  
		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td valign="top">
<c:if test="${not empty theItem.transactionType}">
${theItem.transactionType}
</c:if>
		  </td>
		  
		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td valign="top">
<c:if test="${not empty theItem.transactionNumber}">
${theItem.transactionNumber}
</c:if>
		  </td>
		  
		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td valign="top" colspan="2">
${theItem.checkStatus}
		  </td>
			
		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		

</c:forEach>
</c:if>
<c:if test="${empty theReport.details}">
		<tr class="datacell1">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td valign="top" colspan="18" id="noLoansCell"><b><span id="noLoans"><content:getAttribute 	beanName="uncashedChecksReport" attribute="text"/></span></b></td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:if>
        <ps:roundedCorner numberOfColumns="20"
                          emptyRowColor="white"
                          oddRowColor="white"
                          evenRowColor="beige"
	                      name="theReport" 
	                      property="details"/>

		<tr>
			<td colspan="20" align="right"><report:pageCounter report="theReport" arrowColor="black" formName="uncashedChecksReportForm"/></td>
		</tr>
		
		
		<tr>
			<td colspan="20">
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
  			</td>
		</tr>

      </table>

	  </ps:form>
</c:if>
<c:if test="${not empty param.printFriendly}" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
