<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%-- Imports --%>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.handler.TransactionType" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryReportData" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.handler.TransactionTypeDescription" %>
<%@ page import="com.manulife.pension.ps.web.transaction.ParticipantTransactionHistoryForm" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryItem"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

TransactionHistoryReportData theReport = (TransactionHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

%>

<%-- GIFL 1C Start--%>
<c:if test="${not empty details}">
<c:set var="details" value="${requestScope.details}" scope="page" />
</c:if>
<%-- GIFL 1C End--%>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TRANSACTION_HISTORY_WITHDRAWAL_MESSAGE%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="WithdrawalMessage"/>

<content:contentBean contentId="<%=ContentConstants.PS_PARTICIPANT_ACCOUNT_LOAN_DETAIL_TEXT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="LoanDetailText"/>

<content:contentBean contentId="<%=ContentConstants.FIXED_FOOTNOTE_PBA%>"
                           	       type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>"
                          	       id="FootnotePBA"/>

<c:if test="${empty param.printFriendly}" >
<script type="text/javascript" >

		function submitFilter() {
			setFilterFromInput(document.forms['participantTransactionHistoryForm'].elements['fromDate']);
			setFilterFromInput(document.forms['participantTransactionHistoryForm'].elements['toDate']);
			setFilterFromSelect(document.forms['participantTransactionHistoryForm'].elements['transactionType']);
			doFilter('/do/transaction/participantTransactionHistory');
		}

		function submitDates() {
			setFilterFromInput(document.forms['participantTransactionHistoryForm'].elements['fromDate']);
			setFilterFromInput(document.forms['participantTransactionHistoryForm'].elements['toDate']);
			doFilter('/do/transaction/participantTransactionHistory');
		}

		function goLoanRepaymentDetails(){
			if ( document.participantTransactionHistoryForm.selectedLoan.value != -1 )  {
				document.loanRepaymentDetails.loanNumber.value = document.participantTransactionHistoryForm.selectedLoan.value;		
                document.loanRepaymentDetails.submit();
			}
		}

		function doOnload() {
			var lastVisited = "${e:forJavaScriptBlock(param.lastVisited)}";
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
      <c:if test="${empty param.printFriendly }" >
        &nbsp;
<a href="/do/participant/participantAccount/?participantId=${participantTransactionHistoryForm.participantId}">
          Participant Account
        </a><br>
        &nbsp;
        <a href="/do/participant/participantSummary">
          Participant Summary
        </a><br>
        &nbsp;
        <a href="/do/transaction/transactionHistoryReport/">
          Transaction History
        </a>
		<%-- GIFL 1C Start--%>
			<c:if test="${not empty details}">
			<c:if test="${userProfile.currentContract.hasContractGatewayInd}">
<c:if test="${not empty details.participantGIFLIndicator}">
        			<br>&nbsp;
<a href="/do/participant/participantBenefitBaseInformation/?profileId=${participantTransactionHistoryForm.profileId}">
          			Benefit Base information 
        			</a><br>
</c:if>
			</c:if>
			</c:if>
		<%-- GIFL 1C END--%>
       </c:if>

        <%-- error line --%>
        &nbsp;
	    <content:errors scope="session" />
        &nbsp;


<c:if test="${not empty details}">

<ps:form name="loanRepaymentDetails" method="POST" action="/do/transaction/loanRepaymentDetailsReport/" modelAttribute="loanRepaymentDetailsReportForm" >
  <form:hidden path="loanNumber"/>
  <form:hidden path="name" value="${details.lastName},${details.firstName}"/>
<form:hidden path="profileId" value="${participantTransactionHistoryForm.profileId}"/>
</ps:form>

      <%-- Start Account information --%>

 <ps:form cssStyle="margin-bottom:0;" method="POST" modelAttribute="participantTransactionHistoryForm" name="participantTransactionHistoryForm" action="/do/transaction/participantTransactionHistory/">
	<input type="hidden" name="profileId"/>
      <table width="500" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="1"></td>
          <td width="240"></td>
          <td width="258"></td> 			
          <td width="1"></td>
        </tr>
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="9">
            <%-- CMA managed--%>
            <b><content:getAttribute id="layoutPageBean" attribute="body1Header"/> </b>as of
            <render:date patternOut="<%= RenderConstants.EXTRA_LONG_MDY %>"
                         property="userProfile.currentContract.contractDates.asOfDate"/> 
          </td>
        </tr>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="240" align="left" valign="top" class="datacell1">
	        <table width="240" border="0" cellpadding="2" cellspacing="0">
              <tr id = "participantName" >
                <td width="106" valign="top"><b>Name:</b></td>
<td width="134">${details.lastName}, ${details.firstName}</td>
              </tr>
              <tr>
                <td valign="top"><b>SSN:</b></td>
                <td><render:ssn property="details.ssn" /></td>
              </tr>
              <tr>
                <td valign="top"><b>Date of birth:</b></td>
                <td><render:date property="details.birthDate" defaultValue="January 1, 1980" patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" /></td>            
              </tr>

<c:if test="${participantTransactionHistoryForm.showAge ==true}">
              <tr>
                <td valign="top"><b>Age:</b></td>
<td>${details.age}</td>
              </tr>
</c:if>

	          <tr>
                <td valign="top"><b>Address:</b></td>	
					<c:if test="${not empty details.addressLine1}">
					<td>${details.addressLine1}</td>
              </tr>
              <tr>
                <td></td>
					<td>${details.addressLine2}</td>
              </tr>
              <tr>
                <td></td>
<td>${details.cityName}, ${details.stateCode}</td>
              </tr>
              <tr>
                <td></td>
<td>${details.zipCode}</td>
              </tr>
</c:if>

<c:if test="${empty details.addressLine1}">
                <td>No available address information.</td>
              </tr>
</c:if>

              <tr>
                <td valign="top"><b>Status:</b></td>
<td>${details.employeeStatus}</td>
              </tr>           
            </table>
	      </td>
          <td width="258" align="left" valign="top" class="datacell1">
            <table border="0" cellpadding="2" cellspacing="0">
              <tr>
                <td width="130" ><b>Total assets:</b></td>
                <td width="128" align="right"><render:number property="details.totalAssets" type="c"/></td>
              </tr>
              <tr>
                <td><b>Allocated assets:</b></td>
                <td align="right"><render:number property="details.allocatedAssets" type="c"/></td>
              </tr>

<c:if test="${participantTransactionHistoryForm.showPba ==true}">
              <tr>
                <td><b>Personal brokerage account<sup><b>&#134;</b></sup>:</b></td>
                <td align="right"><render:number property="details.personalBrokerageAccount" type="c"/></td>
              </tr>
</c:if>

 <c:if test="${details.showLoanFeature =='YES'}">
              <tr>
                <td><b>Loan assets:</b></td>
                <td align="right"><render:number property="details.loanAssets" type="c"/></td>
              </tr>
</c:if> 
              <tr>
                <td colspan="2">
               
  <c:if test="${participantTransactionHistoryForm.showLoans == true}"> 
                <content:getAttribute id="LoanDetailText" attribute="text"/>
                <c:if test="${empty param.printFriendly}" >
					
					<c:set var = "loanListSize" value="${participantTransactionHistoryForm.loanList.size()}"/>
					<br/>
		
		
				   <c:if test="${loanListSize  gt '2'}"> 
				   
			        <form:select  path="selectedLoan" onchange="goLoanRepaymentDetails();" value="-1">
<form:options items="${participantTransactionHistoryForm.loanList}" itemLabel="label" itemValue="value"/>
			        </form:select>
				   </c:if> 
	 			  <c:if test="${loanListSize eq '2'}">
	 			 
 <input type="hidden" name="selectedLoan" value='${participantTransactionHistoryForm.selectedLoan}' /> 
					   <a href="#" onclick="goLoanRepaymentDetails();">View loan details</a>
</c:if>   


                </c:if>
</c:if> 
                </td>
              </tr>

              <tr>
                <td><b>Investment instruction type:</b></td>
<td align="right">${details.investmentInstructionType}</td>
              </tr>
              <tr>
                <td><b>Default date of birth:</b></td>
                <td align="right">
<c:if test="${empty details.birthDate}">Yes</c:if>
<c:if test="${not empty details.birthDate}">No</c:if>
                </td>
              </tr>
              <tr>
                <td><b>Last contribution date:</b></td>
                <td align="right"><render:date property="details.lastContributionDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /></td>
              </tr>
              <tr>
                <td><b>Automatic rebalance?</b></td>
<td align="right">${details.automaticRebalanceIndicator}</td>
              </tr>

<c:if test="${details.rothFirstDepositYear !=9999}">
              <tr>
                <td><b>Year of first Roth contribution:</b></td>
<td align="right">${details.rothFirstDepositYear}</td>
              </tr>
</c:if>
    
            </table>
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr>
          <td class="databorder" colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
      </table><br>
      
      <%-- End Account information --%>
      
      <table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
           
          <c:if test="${empty param.printFriendly}" >
          <td width="120"><img src="/assets/unmanaged/images/s.gif" width="120" height="1"></td>
          </c:if>

          <c:if test="${not empty param.printFriendly}" >
          <td width="50"><img src="/assets/unmanaged/images/s.gif" width="50" height="1"></td>
          </c:if>

          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

          <c:if test="${empty param.printFriendly}" >
          <td width="100"><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
          </c:if>

          <c:if test="${not empty param.printFriendly}" >
          <td width="100"><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
          </c:if>

          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="234"><img src="/assets/unmanaged/images/s.gif" width="248" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="110"><img src="/assets/unmanaged/images/s.gif" width="96" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="126"><img src="/assets/unmanaged/images/s.gif" width="126" height="1"></td>
          <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
           
        <c:if test="${empty theReport}">
        <c:if test="${not empty displayDates}">
        <tr>
          <td colspan="12">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr class="tablehead">
                <td class="tableheadTD1" valign="middle" width="381" ><span class="">
                  <b><content:getAttribute id="layoutPageBean" attribute="body2Header"/></b>&nbsp;from&nbsp;
                    <c:if test="${not empty param.printFriendly}">
                    <input readonly="true" type="text" name="fromDate" size="10"  
                           value="${e:forHtmlAttribute(participantTransactionHistoryForm.fromDate)}" 
                           class="inputAmount">
                    </c:if>
                    <c:if test="${empty param.printFriendly}">
                    <input type="text" name="fromDate" size="10" tabindex="10" maxlength="10" 
                           value="${e:forHtmlAttribute(participantTransactionHistoryForm.fromDate)}"
                           class="inputAmount">&nbsp;
                    <a href="javascript:calFromDate.popup();">
                      <img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date">
                    </a>        				
                   </c:if>
                      
                    &nbsp;&nbsp;to&nbsp;
                  <c:if test="${not empty param.printFriendly}">
                    <input readonly="true" type="text" name="toDate" size="10"
                           value="${e:forHtmlAttribute(participantTransactionHistoryForm.toDate)}"
                           class="inputAmount">
                   </c:if>
                    <c:if test="${empty param.printFriendly}">
                    <input type="text" name="toDate" size="10" maxlength="10"  tabindex="12"
                           value="${e:forHtmlAttribute(participantTransactionHistoryForm.toDate)}"
                           class="inputAmount">&nbsp;
                    <a href="javascript:calToDate.popup();">
                      <img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date">
                    </a>        				
                   </c:if>
                    <br>
                    <span class="disclaimer">

                      <c:if test="${empty param.printFriendly}">
                      <img src="/assets/unmanaged/images/s.gif" width="115" height="1">
                      (mm/dd/yyyy)
                      <img src="/assets/unmanaged/images/s.gif" width="55" height="1">
                      (mm/dd/yyyy)
                      </c:if>

                      <c:if test="${not empty param.printFriendly}">
                      <img src="/assets/unmanaged/images/s.gif" width="114" height="1">
                      (mm/dd/yyyy)
                      <img src="/assets/unmanaged/images/s.gif" width="32" height="1">
                      (mm/dd/yyyy)
                      </c:if>

                    </span> 
                </td>
                <td class="tableheadTD" width="95">
                  <c:if test="${empty param.printFriendly}">
                  <input type="button" name="submit" tabindex="14" value="search" onclick="submitDates(); return false;" style="width: 65px; height: 25px; font-size:13px;">
                 </c:if>
                </td>    
                 
                <td class="tableheadTD" width="224"></td>
              </tr>
            </table>
          </td>
        </tr>
        </c:if>

      </table>			
      </c:if>
             
      <c:if test="${not empty theReport}">
        <tr>
          <td colspan="12">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr class="tablehead">
                <td class="tableheadTD1" valign="middle" width="360" ><span class="">
                  <b><content:getAttribute id="layoutPageBean" attribute="body2Header"/></b>&nbsp;from&nbsp;
                        
                    <c:if test="${not empty param.printFriendly}" >
                    <input readonly="true" type="text" name="fromDate" size="10"  
value="${e:forHtmlAttribute(participantTransactionHistoryForm.fromDate)}" class="inputAmount">

                    </c:if>
                     
                    <c:if test="${empty param.printFriendly}" >
                    <input type="text" name="fromDate" size="10" maxlength="10"  tabindex="10" 
value="${e:forHtmlAttribute(participantTransactionHistoryForm.fromDate)}" class="inputAmount">&nbsp;

                    <a href="javascript:calFromDate.popup();">
                      <img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date">
                    </a>        				
                    </c:if>
                      
                    &nbsp;&nbsp;to&nbsp;
                      
                    <c:if test="${not empty param.printFriendly}" >
                    <input readonly="true" type="text" name="toDate" size="10" 
value="${e:forHtmlAttribute(participantTransactionHistoryForm.toDate)}" class="inputAmount">

                    </c:if>

                    <c:if test="${empty param.printFriendly}" >
                    <input type="text" name="toDate" size="10" maxlength="10"  tabindex="12"
value="${e:forHtmlAttribute(participantTransactionHistoryForm.toDate)}" class="inputAmount">&nbsp;

                    <a href="javascript:calToDate.popup();">
                      <img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date">
                    </a>        				
                    </c:if>
                         
                    <br>
                    <span class="disclaimer">

                      <c:if test="${empty param.printFriendly}" >
                      <img src="/assets/unmanaged/images/s.gif" width="115" height="1">
                      (mm/dd/yyyy)
                      <img src="/assets/unmanaged/images/s.gif" width="55" height="1">
                      (mm/dd/yyyy)
                      </c:if>

                      <c:if test="${not empty param.printFriendly}" >
                      <img src="/assets/unmanaged/images/s.gif" width="114" height="1">
                      (mm/dd/yyyy)
                      <img src="/assets/unmanaged/images/s.gif" width="32" height="1">
                      (mm/dd/yyyy)
                      </c:if>

                    </span> 
                </td>
                <td class="tableheadTD" width="80">
                    
                  <c:if test="${empty param.printFriendly}">
<input type="button" style="width: 65px; height: 25px; font-size:13px;" onclick="submitDates(); return false;" property="submit" tabindex="14" value="search"/>
                  </c:if>
                    
                </td>
                <td class="tableheadTD" width="190" valign="middle" >
                  <b><report:recordCounter report="theReport" label="Transactions"/></b>
                </td>
                <td class="tableheadTD" width="70" valign="bottom" align="right">
                  <report:pageCounter report="theReport"  formName="participantTransactionHistoryForm"/>
                </td>
              </tr>
            </table>
          </td>
        </tr>

        <%-- Detail column header row  --%>
        <tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>   
              
          <%-- Transaction date --%>
          <td align="left"><b>
            <report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE%>" formName="participantTransactionHistoryForm"  direction="desc">
              Transaction date
            </report:sort></b>
          </td>
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              
          <%-- Payroll ending --%>
          <td align="left"><b>
            <report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_PAYROLL_ENDING_DATE%>"  formName="participantTransactionHistoryForm" direction="desc">
              Payroll ending
            </report:sort></b>
          </td>  
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              
          <%-- Type --%>
          <td align="left"><b>
            <report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_TRANSACTION_TYPE%>"   formName="participantTransactionHistoryForm" direction="asc">
              Type&nbsp;
            </report:sort></b>
            <ps:select name="participantTransactionHistoryForm" property="transactionType" onchange="submitFilter();">
              <ps:options collection="transactionTypes" property="value" labelProperty="label" />
            </ps:select>
          </td>
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             
          <%-- Amount --%>
          <td align="right"><b>Amount($)</b></td>
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
           
          <%-- Transaction number --%>      
          <td align="right" colspan="2"><b>
            Transaction number
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
              
        <%-- Message line if there are no detail items --%>
<c:if test="${empty theReport.details}">
          <content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED%>"
                               type="<%=ContentConstants.TYPE_MESSAGE%>"
                               id="TransactionHistoryMessage"/>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="10">
            <content:getAttribute id="TransactionHistoryMessage" attribute="text"/>
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:if>

        <%-- Detail rows --%>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<% 				
TransactionHistoryItem theItem =(TransactionHistoryItem)pageContext.getAttribute("theItem");
pageContext.setAttribute("theItem",theItem,PageContext.PAGE_SCOPE);
%>              
<c:if test="${theItem.type =='WD'}">
<!-- WITHDRAWAL -->
        <c:choose><c:when test="${theIndex.index % 2 ==0}">
 <tr class="datacell1">
</c:when><c:otherwise><tr class="datacell2"></c:otherwise>
</c:choose>
          <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>							
          <td><render:date dateStyle="m" property="theItem.transactionDate"/></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td>
          <% if (theItem.displayPayrollDate()) { %>
            <render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.payrollEndingDate"/>
          <% } %>
          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td>
<c:if test="${theItem.displayChequeAmount !='-'}">*</c:if>
           <report:formatParticipantTransactionType item="theItem" />
          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top" colspan="2">
<c:if test="${theItem.transactionNumber !=0}">
${theItem.transactionNumber}
</c:if>
          </td>								
          <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
        </tr>
         <c:choose><c:when test="${theIndex.index % 2 ==0}">
 <tr class="datacell1">
</c:when><c:otherwise><tr class="datacell2"></c:otherwise>
</c:choose>
          <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>							
          <td></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;<%= ParticipantTransactionHistoryForm.WITHDRAWAL_AMOUNT %></td>								
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right"><render:number property="theItem.amount" defaultValue="" pattern="###,###,##0.00;(###,###,##0.00)"/></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" colspan="2"></td>								
          <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
        </tr>					
<c:if test="${theItem.displayChequeAmount !='-'}">
         <c:choose><c:when test="${theIndex.index % 2 ==0}">
 <tr class="datacell1">
</c:when><c:otherwise><tr class="datacell2"></c:otherwise>
</c:choose>
          <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>							
          <td></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;<%= ParticipantTransactionHistoryForm.DISTRIBUTION_AMOUNT %></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right"><render:number property="theItem.chequeAmount" defaultValue="" pattern="###,###,##0.00;(###,###,##0.00)"/></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" colspan="2"></td>								
          <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
        </tr>
</c:if>
<!-- END WITHDRAWAL -->
</c:if>

<c:if test="${theItem.type !='WD'}">
          <c:choose><c:when test="${theIndex.index % 2 ==0}">
 <tr class="datacell1">
</c:when><c:otherwise><tr class="datacell2"></c:otherwise>
</c:choose>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td> 
             
          <%-- Transaction date --%>
          <td valign="top">
            <render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.transactionDate"/>
          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             
          <%-- Payroll ending --%>
          <td valign="top">
          <% if (theItem.displayPayrollDate()) { %>
            <render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.payrollEndingDate"/>
          <% } %>
          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              
          <%-- Type --%>
          <td valign="top">
                   
            <%-- Type description line 1 --%>
            <report:formatParticipantTransactionType item="theItem" />
            <%-- Type description line 2 --%>
<c:if test="${not empty theItem.typeDescription2}">
<br>${theItem.typeDescription2}
</c:if>
                 
          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             
          <%-- Amount --%>
          <td align="right" valign="top">
            <render:number property="theItem.amount" defaultValue = "" pattern="###,###,##0.00;(###,###,##0.00)"/>
          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              
          <%-- Transaction number --%>
          <%
            boolean showTransactionNumber = true;
            if (theItem.getTypeDescription1().startsWith(TransactionTypeDescription.getPsDescription(TransactionType.REQUEST_PS_ALLOCATION_INSTRUCTION, true))) {
              showTransactionNumber = false;
            }
          %>
          <td valign="top" align="right" colspan="2">
            <% if (showTransactionNumber) { %>
${theItem.transactionNumber}
            <% } %>
          </td>
            
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:if>
        
</c:forEach>
               
        <ps:roundedCorner numberOfColumns="12"
                          emptyRowColor="white"
                          oddRowColor="white"
                          evenRowColor="beige"
                          name="theReport"
                          property="details"/>
             
        <tr>
          <td colspan="12" align="right"><report:pageCounter report="theReport"  formName="participantTransactionHistoryForm" arrowColor="black"/></td>
        </tr>
</c:if>
      </table>
            
      <p><content:pageFooter id="layoutPageBean"/></p>
      <p class="footnote">
        <content:pageFootnotes id="layoutPageBean"/>
<c:if test="${participantTransactionHistoryForm.showPba ==true}">
		  <content:getAttribute id="FootnotePBA" attribute="text"/>
</c:if>
      </p>
      <p>
        <c:if test="${not empty theReport}">
<c:if test="${theReport.hasWithdrawlDistribution ==true}">
            *<content:getAttribute id="WithdrawalMessage" attribute="text" />
</c:if>
        </c:if>
      </p>
      <p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>

 
      </c:if>

      </ps:form>

      <script type="text/javascript" >
      <!-- // create calendar object(s) just after form tag closed
        var calFromDate = new calendar(document.forms['participantTransactionHistoryForm'].elements['fromDate']);
        calFromDate.year_scroll = true;
        calFromDate.time_comp = false;
        var calToDate = new calendar(document.forms['participantTransactionHistoryForm'].elements['toDate']);
        calToDate.year_scroll = true;
        calToDate.time_comp = false;
        //-->
      </script>

</c:if>
        
    </td>
  </tr>
</table>

<c:if test="${not empty param.printFriendly }" >
<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="globalDisclosure"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%"><content:getAttribute id="globalDisclosure" attribute="text"/></td>
  </tr>
</table>
</c:if>
