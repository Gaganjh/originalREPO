<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@ page import="com.manulife.pension.service.report.participant.transaction.handler.TransactionType" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.handler.TransactionTypeDescription" %>
<%@ page import="com.manulife.pension.ps.web.transaction.ParticipantTransactionHistoryForm" %>

<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 



<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsACIReportData" %>

<%
TransactionDetailsACIReportData theReport = (TransactionDetailsACIReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%> 






 <jsp:useBean id="theForm" scope="session" type="com.manulife.pension.ps.web.transaction.AciReportForm" />





<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TRANSACTION_HISTORY_WITHDRAWAL_MESSAGE%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="WithdrawalMessage"/>

<content:contentBean contentId="<%=ContentConstants.PS_PARTICIPANT_ACCOUNT_LOAN_DETAIL_TEXT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="LoanDetailText"/>

<content:contentBean contentId="<%=ContentConstants.FIXED_FOOTNOTE_PBA%>"
                           	       type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>"
                          	       id="FootnotePBA"/>

<c:if test="${empty param.printFriendly}">
<script type="text/javascript" >


		function doOnload() {
			var lastVisited = "<%=request.getParameter("lastVisited")%>";
			var pageNumber = "<%=request.getParameter("pageNumber")%>";
			var sortField = "<%=request.getParameter("sortField")%>";
			if ((lastVisited == "true") || (pageNumber != "null") || (sortField != "null")) {
				location.hash = "participantName";
			}
		}

</script>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>    
    <td width="100%" valign="top">

    	<c:if test="${empty param.printFriendly}">
        	<a href="/do/transaction/participantTransactionHistory/?profileId=<%=theForm.getProfileId()%>">Transaction History</a>
    	</c:if>

        <%-- error line --%>
        &nbsp;
	    <content:errors scope="request" />
        &nbsp;


<c:if test="${not empty details}">

      <%-- Start Account information --%>

      <ps:form  method="POST" action="/do/transaction/participantTransactionHistory/" modelAttribute="participantTransactionHistoryForm" name="participantTransactionHistoryForm">
<input type="hidden" name="profileId"/>

      <table width="340" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="1"></td>
          <td width="338"></td>
          <td width="1"></td>
        </tr>
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="3"><b>JH EZincrease service change summary</b></td>
        </tr>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="338" align="left" valign="top" class="datacell1">
	        <table width="338" border="0" cellpadding="2" cellspacing="0">
	          <tr>
                <td width="110" valign="top"><b>Transaction type:</b></td>
                <td width="228">JH EZincrease service change</td>	          
	          </tr>
              <tr id = "participantName" >
                <td width="110" valign="top"><b>Name:</b></td>
<td width="228">${details.fullName}</td>
              </tr>
              <tr>
                <td valign="top"><b>SSN:</b></td>
                <td><render:ssn property="details.ssn" /></td>
              </tr>

            </table>
	      </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr>
          <td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
      </table><br>
      
      <%-- End Account information --%>
      
      <%-- establish columns --%>
      <table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="242"><img src="/assets/unmanaged/images/s.gif" width="242" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="70"><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="70"><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>          
          <td width="120"><img src="/assets/unmanaged/images/s.gif" width="120" height="1"></td>          
          <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
                
        <tr>
          <td colspan="10">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr class="tablehead">
                <td class="tableheadTD1" valign="middle" width="360" >
                  <span class="">
<b>Details - ${theForm.transactionDateFormatted}</b>
                  </span> 
                </td>
              </tr>
            </table>
          </td>
        </tr>

        <%-- Detail column header row  --%>
        <tr class="tablesubhead">
            <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>   
            <td align="left"><b>Item changed</b></td>
            <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="left"><b>Value before</b></td>
            <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="left"><b>Value after</b></td>
            <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="left" colspan="2"><b>Changed by</b></td>
            <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>       
        </tr>

        <%-- Detail rows --%>
						<c:forEach items="${theReport.details}" var="theItem"
							varStatus="theIndex">
							<c:set var="indexValue" value="${theIndex.index}"/> 
<%String temp = pageContext.getAttribute("indexValue").toString();%>

         <% if (Integer.parseInt(temp) % 2 == 0) { %>
          <tr class="datacell1">
        <% } else { %>
          <tr class="datacell2" >
        <% } %> 
							
							

							
							<td class="databorder"><img
								src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
							<td>${theItem.itemChanged}<%-- filter="false" --%></td>
							<td class="datadivider"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td>${theItem.valueBefore}<%-- filter="false" --%></td>
							<td class="datadivider"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td>${theItem.valueAfter}<%-- filter="false" --%></td>
							<td class="datadivider"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<% if (userProfile.isInternalUser()) { %>
							<td colspan="2" title="${theItem.getProcessedByInternal()}">
								<% } else {  %>
							
							<td colspan="2">
								<% } %> ${theItem.changedBy}
							</td>
							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>

						</c:forEach>
						<%-- Final boarder row --%>                    
        <ps:roundedCorner numberOfColumns="10"
                          emptyRowColor="white"
                          oddRowColor="white"
                          evenRowColor="beige"
                          name="theReport"
                          property="details"/>
             
        <tr>
          <td colspan="10" align="right"><report:pageCounter report="theReport" arrowColor="black"/></td>
        </tr>
      </table>
            
      <p><content:pageFooter id="layoutPageBean"/></p>
      <p class="footnote">
        <content:pageFootnotes id="layoutPageBean"/>
<c:if test="${participantTransactionHistoryForm.showPba ==true}">
		  <content:getAttribute id="FootnotePBA" attribute="text"/>
</c:if>
      </p>
      <p>
      </p>
      <p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>

 
      </ps:form>

</c:if>
        
    </td>
  </tr>
</table>

<c:if test="${not empty param.printFriendly}">
<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="globalDisclosure"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%"><content:getAttribute id="globalDisclosure" attribute="text"/></td>
  </tr>
</table>
</c:if>
