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
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountItem" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountReportData" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
CashAccountReportData theReport = (CashAccountReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

%>


<script language="javascript1.2" type="text/javascript">
var selectRangeLimiter = null;

function doFilterDateRange() {
  doFilter();
}
</script>
		<content:errors scope="request" /><content:errors scope="session" />

      <td width="30">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td width="100%" valign="top">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
<tr>
	<%-- Tab navigation JSP --%>
	<td width="100%" valign="top">
		<jsp:include flush="true" page="cashAccountTabBar.jsp">
			<jsp:param name="selectedTab" value="CashAccountTab"/>
		</jsp:include>	
	</td>
</tr>
<tr>
 <ps:form cssStyle="margin-bottom:0;" method="POST" modelAttribute="cashAccountReportForm" name="cashAccountReportForm" action="/do/transaction/cashAccountReport/">
 
 <c:if test="${empty  theReport}">
	 <c:if test="${not empty param.printFriendly}" >
      <table width="634" border="0" cellspacing="0" cellpadding="0">
	</c:if>
	<c:if test="${empty param.printFriendly}">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
	</c:if>			
	<c:if test="${not emptydisplayDates}">
      <!-- 	Dummy row to make the width consistant -->
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
			<td width="95"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="99"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<!-- Spacer? -->
			<td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		  <tr class="tablehead">
          <td class="tableheadTD" valign="middle" colspan="16">
            <b><content:getAttribute id="layoutPageBean" attribute="body1Header"/></b>&nbsp;from&nbsp;
            <ps:select name="cashAccountReportForm" property="fromDate"
                       onchange="setFilterFromSelect(this);">
              <ps:dateOptions name="<%=Constants.CASH_ACCOUNT_FROM_DATES%>"
                              renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
            </ps:select>
            &nbsp;to&nbsp;
            <ps:select name="cashAccountReportForm" property="toDate"
                       onchange="setFilterFromSelect(this);">
              <ps:dateOptions name="<%=Constants.CASH_ACCOUNT_TO_DATES%>"
                              renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
            </ps:select>
            &nbsp;
			<c:if test="${empty param.printFriendly}" >
				<input type="button" onclick="doFilterDateRange(); return false;" name="submit" value ="search"></input>
			</c:if>
      	 </td>
        </tr>
    </c:if> end of displayDates
		</table>
		<tr>
          <td width="16"><img src="/assets/unmanaged/images/s.gif" width="1" height="50"></td>
        </tr>
	
	</table> 
</c:if>
 
<c:if test="${not empty theReport}">

	<c:if test="${not empty param.printFriendly}" >
      <table width="634" border="0" cellspacing="0" cellpadding="0">
	</c:if>
		<c:if test="${empty param.printFriendly }" >
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		</c:if>	
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
			<td width="95"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="99"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<!-- Spacer? -->
			<td width="2"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
        </tr>
		<tr class="tablehead">
          <td class="tableheadTD" valign="middle" colspan="11">
            from&nbsp;
            <ps:select name="cashAccountReportForm" property="fromDate"
                       onchange="setFilterFromSelect(this);">
              <ps:dateOptions name="<%=Constants.CASH_ACCOUNT_FROM_DATES%>"
                              renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
            </ps:select>
            &nbsp;to&nbsp;
             <ps:select name="cashAccountReportForm" property="toDate"
                       onchange="setFilterFromSelect(this);">
              <ps:dateOptions name="<%=Constants.CASH_ACCOUNT_TO_DATES%>"
                              renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
            </ps:select> 
            &nbsp;
			<c:if test="${empty param.printFriendly}" >
			<input type="button" onclick="doFilterDateRange(); return false;" name="submit" value ="search"/>
			</c:if>
          	<b><report:recordCounter report="theReport" label="Transactions :"/></b>
          	</td>
				
          	<td align="right" colspan="5"><report:pageCounter report="theReport" formName="cashAccountReportForm"/></td>
        </tr>
		
		<tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="14">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr class="datacell1">
                <td valign="top" width="221" align="left">
                  <span style="font-size: 17px;font-weight:bold;">Current balance:</span>

                  <br/>as of <render:date patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>"
                                         property="userProfile.currentContract.contractDates.asOfDate"/>
                  <p/>
                </td>
                <td align="right" width="221" valign="top" >
                  <h4><span class="highlight"><render:number property="theReport.currentBalance" type="c"/></span></h4>
                </td>
                <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td valign="top" colspan="4" width="289">&nbsp;</td>
             </tr>
             <tr class="datacell2">
                <%-- Single Contract --%>
			 <c:if test="${theReport.hasMultipleContracts ==false}">
	            <td valign="top"  align="left" width="221"><b>Opening balance:</b></td>
	            <td align="right" valign="top" width="221">
	                <span class="highlight"><render:number property="theReport.openingBalanceForPeriod" type="c"/></span>
	              </td>
	              <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  <td valign="top" align="left" width="221">
                    <b>Total debits this period:</b>
                  </td>
                  <td align="right" valign="top" colspan="2" width="221">
                    <span class="highlight"><render:number property="theReport.totalDebitsForPeriod" type="c"/></span>
                  </td>
                  <td class="datacell1" align="right" valign="top"
                      width="103">&nbsp;</td>
				</c:if>
                <%-- Multiple Contracts --%>
				<c:if test="${theReport.hasMultipleContracts ==true}">
                  <td valign="top" align="left" width="159">
                    <b>Total debits this period:</b>
                  </td>
                  <td align="right" valign="top" width="137">
                    <span class="highlight"><render:number property="theReport.totalDebitsForPeriod" type="c"/></span>
                  </td>
                  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  <td colspan="4">&nbsp;</td>
				</c:if>
             </tr>
              <tr class="datacell1">
                <%-- Single Contract --%>
				<c:if test="${theReport.hasMultipleContracts ==false}">
	                <td valign="top"  align="left" width="221"><b>Closing balance:</b></td>
	                <td align="right" valign="top" width="221"><span class="highlight"><render:number property="theReport.closingBalanceForPeriod" type="c"/></span></td>
	                <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td valign="top" align="left" width="221"><b>Total credits this
	                  period:</b></td>
	                <td align="right" valign="top" width="220"><span class="highlight"><render:number property="theReport.totalCreditsForPeriod" type="c"/></span></td>
	                <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td align="right" valign="top"
	                    width="103">&nbsp;</td>
				</c:if>
                <%-- Multiple Contracts --%>
				<c:if test="${theReport.hasMultipleContracts ==true}">
                    <td valign="top" align="left" width="159"><b>Total credits this period:</b></td>
                    <td align="right" valign="top" width="137"><span class="highlight"><render:number property="theReport.totalCreditsForPeriod" type="c"/></span></td>
                    <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td colspan="4">&nbsp;</td>
				</c:if>
              </tr>

            </table>
         </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
       <tr>
		 <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<c:if test="${theReport.hasMultipleContracts ==false}">
              <td colspan="14" class="beigeborder">
              <img src="/assets/unmanaged/images/s.gif" height="1"></td>
			</c:if>
			<c:if test="${theReport.hasMultipleContracts ==true}">
     	      <td colspan="14" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
			</c:if>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
			<c:if test="${not empty theReport.details}">
				<c:if test="${theReport.hasMultipleContracts ==false}">	
					<td colspan="11"><img src="/assets/unmanaged/images/s.gif" height="10"></td>
					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
					<td colspan="2"><img src="/assets/unmanaged/images/s.gif"></td>
				</c:if>
				<c:if test="${theReport.hasMultipleContracts ==true}">
					<td colspan="14"><img src="/assets/unmanaged/images/s.gif" height="10"></td>
				</c:if>
			</c:if>
			<c:if test="${empty theReport.details}">
            <td colspan="14"><img src="/assets/unmanaged/images/s.gif" height="10"></td>
			</c:if>
		   <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
        </tr>
		<c:if test="${empty theReport.details}">
			<c:if test="${theReport.hasTooManyItems ==true}">
              <content:contentBean contentId="<%=ContentConstants.MESSAGE_TOO_MANY_TRANSACTIONS_FOR_DATE_SELECTED%>"
                                   type="<%=ContentConstants.TYPE_MESSAGE%>"
                                   id="CashAccountTransactionMessage"/>
			</c:if>
			<c:if test="${theReport.hasTooManyItems ==false}">
              <content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_TRANSACTION_FOR_DATE_SELECTED%>"
                                   type="<%=ContentConstants.TYPE_MESSAGE%>"
                                   id="CashAccountTransactionMessage"/>
			</c:if>
            <tr class="datacell1">
              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              <td colspan="14">
                <content:getAttribute id="CashAccountTransactionMessage"
                                      attribute="text"/>
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
			<c:if test="${theReport.hasMultipleContracts ==false}">
				<td align="left">
			</c:if>
			<c:if test="${theReport.hasMultipleContracts ==true}">
            <td align="left" colspan="3">
		</c:if>
          <b>Description</b></td>
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="center"><b>Transaction number</b></td>
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right"><b>Debits($)</b></td>
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<c:if test="${theReport.hasMultipleContracts ==false}">
            <td align="right">
		</c:if>
		<c:if test="${theReport.hasMultipleContracts ==true}">
            <td align="right" colspan="2">
		</c:if>
          <b>Credits($)</b></td>
		<c:if test="${theReport.hasMultipleContracts ==false}">
            <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="right" colspan="2"><b>Running balance($)</b></td>
		</c:if>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
		<c:set var="indexValue" value="${theIndex.index}"/> 
		<%
		String temp = pageContext.getAttribute("indexValue").toString(); 
		CashAccountItem itemVal = (CashAccountItem)pageContext.getAttribute("theItem");
		%>

          <% if (Integer.parseInt(temp) %  2 == 0) { %>
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
	          <%
	          		// this piece of code is kept as Java code to avoid the overhead of
	          		// Map and Link tags
	          		if (!itemVal.isComplete() || (!itemVal.getType().equals(TransactionType.ALLOCATION)
	          				&& !itemVal.getType().equals(TransactionType.LOAN_REPAYMENT) ) ) 
	          		{
	          		out.print(itemVal.getTypeDescription1());	
	          		} else {
		          		StringBuffer queryString = new StringBuffer();
						boolean isDB = userProfile.getCurrentContract().isDefinedBenefitContract();

						if (itemVal.getType().equals(TransactionType.ALLOCATION) ) { 
						    if (isDB) {
						        queryString.append("/do/transaction/pptContributionDetailsReport/?");
						    } else {
		                	queryString.append("/do/transaction/contributionTransactionReport/?");
		                	}
		                } else if (itemVal.getType().equals(TransactionType.LOAN_REPAYMENT) )
		                {
		                	queryString.append("/do/transaction/loanRepaymentTransactionReport/?");
						}              	

		          		queryString.append("task=filter").append("&");
		          		queryString.append("transactionNumber=").append(itemVal.getTransactionNumber()).append("&");
		          		queryString.append("transactionDate=").append(itemVal.getTransactionDate());
		          		if (isDB) {
		          		   queryString.append("&participantId=0");
		          		}
		          		
		          		out.print("<a href='");
		          		out.print(queryString.toString());
		          		out.print("'>");
		          		out.print(itemVal.getTypeDescription1());
		          		out.print("</a>");	       
	          		}
	                 
	          %> 
				<c:if test="${not empty itemVal.typeDescription2}">
				<br>${theItem.typeDescription2}
				</c:if>
	        </td>
	        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<c:if test="${theReport.hasMultipleContracts ==false}">
                <td align="left" valign="top" nowrap>
			</c:if>
			<c:if test="${theReport.hasMultipleContracts ==true}">
                <td align="left" colspan="3" valign="top" nowrap>
			</c:if>
            <ps:formatDescription item="theItem" linkParticipant="false" width="220" hideTransactionInProgress="true" />
	      </td>
			<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td align="center" valign="top">${theItem.transactionNumber}</td>
	        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td align="right" valign="top">
				<c:if test="${not empty theItem.debitAmount}">
		              <render:number property="theItem.debitAmount" type="c" sign="false"/>
				</c:if>
	        </td>
  	        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<c:if test="${theReport.hasMultipleContracts ==false}">
            <td align="right" valign="top">
				</c:if>
			<c:if test="${theReport.hasMultipleContracts ==true}">
              <td align="right" valign="top" colspan="2">
			</c:if>
			<c:if test="${not empty theItem.creditAmount}">
	                  <%-- logic:notEqual name="theItem" property="creditAmount" value="0" --%>
	                      <render:number property="theItem.creditAmount" type="c" sign="false"/>
	                  <%-- /logic:notEqual --%>
			</c:if>
			</td>
			<c:if test="${theReport.hasMultipleContracts ==false}">
              <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	          <td align="right" valign="top" colspan="2">
			<c:if test="${not empty theItem.runningBalance}">
	                  <render:number property="theItem.runningBalance" type="c" sign="false"/>
			</c:if>
	          </td>
			</c:if>
	          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        </tr>
		</c:forEach>
		</c:if>
		<tr>
			<td colspan="15" class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
		</tr>
		<tr>
			<td align="right" colspan="15"><report:pageCounter report="theReport" arrowColor="black" formName="cashAccountReportForm"/></td>
		</tr>	                      
	                      
		<tr>
			<td colspan="15">
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
			</td>
		</tr> 
		</table>
	</table>
	
 </c:if>
</ps:form>
</table>
    
<c:if test="${not empty param.printFriendly }" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>
	<table width="634" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
<script language="javascript1.2" type="text/javascript">
  /*
   * The object must be initialized after the select's are initialized.
   */
  selectRangeLimiter = new SelectRangeLimiter('fromDate', 'toDate');
</script>
