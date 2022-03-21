<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>

<%-- Imports --%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.transaction.handler.TransactionType"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.PendingWithdrawalSummaryReportData"%>
<%@ page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<script type="text/javascript" >

	/**
	 * Method to set the transaction number, participantId and the task in the form
	 * and submit the from.
	 * Called when the user clicks the "Withdrawal" link
	 */
	function doGotoDetailsPage(transactionNumber,participantId){
		document.pendingWithdrawalSummaryForm.selectedTxnNumber.value = transactionNumber;
		document.pendingWithdrawalSummaryForm.selectedParticipant.value=participantId;
		document.pendingWithdrawalSummaryForm.task.value = 'gotoDetailsPage';
		document.pendingWithdrawalSummaryForm.submit();
	}

	/**
	 * Method to set the task in the form and submit the from.
	 * Called when the user clicks the Search button	
	 */
	function doPendingSearch(){
		document.pendingWithdrawalSummaryForm.task.value = 'filter';
		document.pendingWithdrawalSummaryForm.submit();
	}
</script>


<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
PendingWithdrawalSummaryReportData theReport = (PendingWithdrawalSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

%>



<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%" valign="top">
			<%-- error line --%>
			<p><%-- <content:errors scope="request" /> --%><content:errors scope="session" /></p>
		</td>
	</tr>
</table>


<ps:form  method="POST" modelAttribute="pendingWithdrawalSummaryForm" name="pendingWithdrawalSummaryForm" action="/do/transaction/pendingTransactionHistoryReport/">
				
<input type="hidden" name="task"/>
<input type="hidden" name="selectedTxnNumber"/>
<input type="hidden" name="selectedParticipant"/>
	<c:if test="${not empty param.printFriendly}">
	<table border="0" cellspacing="0" cellpadding="0" width="634">
	</c:if>
	<c:if test="${empty param.printFriendly}">
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
	</c:if>
	
		<tr>
			<td width="1">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<c:if test="${empty param.printFriendly}">
			<td width="137">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			</c:if>
			<c:if test="${not empty param.printFriendly}">
			<td width="117">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			</c:if>
			<td width="1">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td width="88">
			<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td width="1">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<c:if test="${empty param.printFriendly}">
			<td width="350">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			</c:if>
			<c:if test="${not empty param.printFriendly}">
			<td width="280">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			</c:if>
			<td width="1">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<c:if test="${empty param.printFriendly}">
			<td width="146">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			</c:if>
			<c:if test="${not empty param.printFriendly}">
			<td width="140">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			</c:if>
			<td width="4">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td width="1">
				<img src="/assets/unmanaged/images/s.gif" width="0" height="1">
			</td>
		</tr>

	<c:if test="${empty theReport}">
	<c:if test="${not empty displayDates}">
		<tr class="tablehead">
			<td class="tableheadTD1" valign="middle" colspan="10">			
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
    	        <tr>
					<td class="tableheadTD" valign="top" width="378">
                  		<table height="40" valign="middle"  width="100%" border="0" cellpadding="0" cellspacing="0">
                  			<tr><td  class="tableheadTD"  nowrap="nowrap"  valign="middle" align="center" width="20%">
							<b><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></b></td>
                  			
		                 	<c:if test="${empty param.printFriendly}" >
		                 	<td class="tableheadTD" valign="middle" align="center" width="35%">from
		                 	<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
<form:input path="fromDate" maxlength="10" size="10" id="fromDateId"/>
		                   	<img src="/assets/unmanaged/images/s.gif" width="2" height="1">
		                   	</td>
		                   	<td valign="center" align="center" width="3%" >
		                   	<a  href="javascript://">
		                   		<img  onclick="return handleDateIconClicked(event, 'fromDateId');"
									 src="/assets/unmanaged/images/cal.gif" width="16" border="0" height="16" 
								 	alt="Use the Calendar to pick the date" /></a>
							</td>							
							<td class="tableheadTD"  valign="middle" align="left"  width="30%">
							<img src="/assets/unmanaged/images/s.gif" width="2" height="1">
							to
		                   	<img  src="/assets/unmanaged/images/s.gif" width="1" height="1">
<form:input path="toDate" maxlength="10" size="10" id="toDateId"/>
		                   <img src="/assets/unmanaged/images/s.gif" width="2" height="1">
		                   </td>
		                   	<td valign="center" align="left"  width="5%" >
		                   	<a href="javascript://">
		                   		<img onclick="return handleDateIconClicked(event, 'toDateId');"
									 src="/assets/unmanaged/images/cal.gif" width="16" border="0" height="16"
								 	alt="Use the Calendar to pick the date"/></a>
		                   	</td>
		                   	</c:if>
		                   	<c:if test="${not empty param.printFriendly }" >
		                 	<td class="tableheadTD" valign="middle" align="center" width="38%">from
		                 	<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
<form:input path="fromDate" maxlength="10" size="10" id="fromDateId"/>
		                   	<img src="/assets/unmanaged/images/s.gif" width="2" height="1">
		                   	</td>
		                   	<td valign="center" align="center" width="3%" >
		                   	<img src="/assets/unmanaged/images/cal.gif" width="16" border="0" height="16" 
							 	alt="Use the Calendar to pick the date" />
							</td>							
							<td class="tableheadTD"  valign="middle" align="left"  width="35%">
							<img src="/assets/unmanaged/images/s.gif" width="2" height="1">
							to
		                   	<img  src="/assets/unmanaged/images/s.gif" width="1" height="1">
<form:input path="toDate" maxlength="10" size="10" id="toDateId"/>
		                   <img src="/assets/unmanaged/images/s.gif" width="2" height="1">
		                   </td>
		                   	<td valign="center" align="left"  width="3%" >
		                   	<img src="/assets/unmanaged/images/cal.gif" width="16" border="0" height="16" 
							 	alt="Use the Calendar to pick the date" />
		                   	</td>
		                   	</c:if>
							</tr>
							<tr >
							<td/>
		                   	<td style="FONT-SIZE: 10px;"  class="tableheadTD" align="left"  >
		                  		<img src="/assets/unmanaged/images/s.gif" width="40" height="1">
		                   		(mm/dd/yyyy) 
		                   		</td>
		                   		<td/>
		                   		<td style="FONT-SIZE: 10px;"  class="tableheadTD" align="left" >
		                   		<img src="/assets/unmanaged/images/s.gif" width="25" height="1">
		                   		(mm/dd/yyyy)
                    		</td>
                    		<td/>
                    		</tr>
                    </table>
                   </td>
                   
                  <td width="82">
                  	<c:if test="${empty param.printFriendly}">
<input type="button" onclick="doPendingSearch(); return false;" name="button" value="search"/>
					</c:if>
                  </td>
                  <td class="tableheadTD" width="186"></td>
                  <td class="tableheadTD" width="64"></td>
                </tr>
            </table>
			</td>
		</tr>

		<%-- detail column header row - transaction date... --%>
		<tr class="tablesubhead">
			<td class="databorder">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td align="left" style="height: 35px;vertical-align: middle">
				<b>Transaction date</b>
			</td>
			<td class="dataheaddivider">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td align="left" >
				<b>Type</b>
			</td>
			<td class="dataheaddivider">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td align="left" >
				<b>Description</b>
			</td>
			<td class="dataheaddivider">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td align="right" colspan="2" width="150" nowrap="nowrap">
				<b>Transaction number</b>
			</td>
			<td class="databorder">
				<img src="/assets/unmanaged/images/s.gif" width="0" height="1">
			</td>
		</tr>
	</c:if>
	</c:if>
	 
	<c:if test="${not empty theReport}">




		<tr class="tablehead">
			<td class="tableheadTD1" valign="middle" colspan="10">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
    	        <tr>
					<td  valign="middle" width="378">
                  		<table height="40"  width="100%" border="0" cellpadding="0" cellspacing="0">
                  			<tr><td  class="tableheadTD"  nowrap="nowrap"  valign="middle" align="center" width="20%">
							<b><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></b></td>
                  			
		                 	<c:if test="${empty param.printFriendly}" >
		                 	<td class="tableheadTD" valign="middle" align="center" width="35%">from
		                 	<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
							<form:input path="fromDate" maxlength="10" size="10" id="fromDateId"/>
		                   	<img src="/assets/unmanaged/images/s.gif" width="2" height="1">
		                   	</td>
		                   	<td valign="center" align="center" width="3%" >
		                   	<a  href="javascript://">
		                   		<img  onclick="return handleDateIconClicked(event, 'fromDateId');"
									 src="/assets/unmanaged/images/cal.gif" width="16" border="0" height="16" 
								 	alt="Use the Calendar to pick the date" /></a>
							</td>							
							<td class="tableheadTD"  valign="middle" align="left"  width="30%">
							<img src="/assets/unmanaged/images/s.gif" width="2" height="1">
							to
		                   	<img  src="/assets/unmanaged/images/s.gif" width="1" height="1">
							<form:input path="toDate" maxlength="10" size="10" id="toDateId"/>
		                   <img src="/assets/unmanaged/images/s.gif" width="2" height="1">
		                   </td>
		                   	<td valign="center" align="left"  width="5%" >
		                   	<a href="javascript://">
		                   		<img onclick="return handleDateIconClicked(event, 'toDateId');"
									 src="/assets/unmanaged/images/cal.gif" width="16" border="0" height="16"
								 	alt="Use the Calendar to pick the date"/></a>
		                   	</td>
		                   	</c:if>
		                   	<c:if test="${not empty param.printFriendly}" >
		                 	<td class="tableheadTD" valign="middle" align="center" width="38%">from
		                 	<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
<form:input path="fromDate" maxlength="10" size="10" id="fromDateId"/>
		                   	<img src="/assets/unmanaged/images/s.gif" width="2" height="1">
		                   	</td>
		                   	<td valign="center" align="center" width="3%" >
		                   	<img src="/assets/unmanaged/images/cal.gif" width="16" border="0" height="16" 
							 	alt="Use the Calendar to pick the date" />
							</td>							
							<td class="tableheadTD"  valign="middle" align="left"  width="35%">
							<img src="/assets/unmanaged/images/s.gif" width="2" height="1">
							to
		                   	<img  src="/assets/unmanaged/images/s.gif" width="1" height="1">
<form:input path="toDate" maxlength="10" size="10" id="toDateId"/>
		                   <img src="/assets/unmanaged/images/s.gif" width="2" height="1">
		                   </td>
		                   	<td valign="center" align="left"  width="3%" >
		                   	<img src="/assets/unmanaged/images/cal.gif" width="16" border="0" height="16" 
							 	alt="Use the Calendar to pick the date" />
		                   	</td>
		                   	</c:if>
							</tr>
							<tr >
							<td/>
		                   	<td style="FONT-SIZE: 10px;"  class="tableheadTD" align="left"  >
		                  		<img src="/assets/unmanaged/images/s.gif" width="40" height="1">
		                   		(mm/dd/yyyy) 
		                   		</td>
		                   		<td/>
		                   		<td style="FONT-SIZE: 10px;"  class="tableheadTD" align="left" >
		                   		<img src="/assets/unmanaged/images/s.gif" width="25" height="1">
		                   		(mm/dd/yyyy)
                    		</td>
                    		<td/>
                    		</tr>
                    </table>
                   </td>
                   <c:if test="${empty param.printFriendly}" >
                  <td width="72">
						<img src="/assets/unmanaged/images/s.gif" width="2" height="1">
<input type="button" onclick="doPendingSearch(); return false;" name="button" value="search"/>
                  </td>
                  <td class="tableheadTD" valign="middle" width="150">
                  	<b><report:recordCounter report="theReport" label="Transactions" /></b>
                  </td>
                  <td class="tableheadTD" valign="bottom" width="112" align="right">
                  	<report:pageCounter report="theReport" formName="pendingWithdrawalSummaryForm" />
                  </td>
                   </c:if>
                   <c:if test="${not empty param.printFriendly}" >
                  <td width="10">
						<img src="/assets/unmanaged/images/s.gif" width="10" height="1">
                  </td>
                  <td class="tableheadTD" valign="middle" width="250">
                  	<b><report:recordCounter report="theReport" label="Transactions" /></b>
                  </td>
                  <td class="tableheadTD" valign="bottom" width="1" align="right">
                  </td>
                   </c:if>
                </tr>
            </table>
			</td>
		</tr>

		<%-- detail column header row - transaction date... --%>
		<tr class="tablesubhead">
			<td class="databorder">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td align="left" style="height: 35px;vertical-align: middle">
				<b><report:sort field="<%=PendingWithdrawalSummaryReportData.SORT_FIELD_DATE%>"
					direction="desc" formName="pendingWithdrawalSummaryForm">Transaction date</report:sort></b>
			</td>
			<td class="dataheaddivider">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td align="left">
				<b>Type</b>
			</td>
			<td class="dataheaddivider">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td align="left">
				<b>Description</b>
			</td>
			<td class="dataheaddivider">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td align="right" colspan="2" nowrap="nowrap">
				<b><report:sort field="<%=PendingWithdrawalSummaryReportData.SORT_FIELD_NUMBER%>"
					direction="desc" formName="pendingWithdrawalSummaryForm">Transaction number</report:sort></b>
			</td>
			<td  width="1" class="databorder">
				<img src="/assets/unmanaged/images/s.gif" width="0" height="1">
			</td>
		</tr>
		<%-- message line if there are no detail items --%>
		<c:if test="${empty theReport.details}">
		<content:contentBean
			contentId="<%=ContentConstants.MESSAGE_NO_PENDING_TRANSACTION_HISTORY_FOR_DATE_SELECTED%>"
			type="<%=ContentConstants.TYPE_MESSAGE%>"
			id="TransactionHistoryMessage" />

		<tr class="datacell1">
			<td class="databorder">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<td colspan="8">
				<content:getAttribute id="TransactionHistoryMessage" attribute="text" />
			</td>
			<td  width="1" class="databorder">
				<img src="/assets/unmanaged/images/s.gif" width="0" height="1">
			</td>
		</tr>
</c:if>

		<%-- detail rows start here --%>
		<c:if test="${not empty theReport.details}">
		<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >


<c:set var="indexValue" value="${theIndex.index}"/> 
<%String temp = pageContext.getAttribute("indexValue").toString();%>
<% if (Integer.parseInt(temp) % 2 == 0) { %> 
			<tr class="datacell1">
			<% } else {%>					
			<tr class="datacell2">
			<% }%> 
			<td class="databorder">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<%-- transaction date --%>
			<td valign="top">
				<render:date
					patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"
					property="theItem.transactionDate" />
			</td>
			<td class="datadivider">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
			<%--Define the Transaction number, participantId which can be passed to JS fun --%>

			<c:set var="transactionNumber" value="${theItem.transactionNumber}"/>
			<c:set var="participantId" value="${theItem.participantId}"/>
			<%-- type line 1 --%>
			<td valign="top">
				<c:if test="${empty param.printFriendly }" >
			<a href="#" onclick="javascript:doGotoDetailsPage(${transactionNumber},${ participantId });">

			${theItem.typeDescription1}
				</a>
				</c:if>
				<c:if test="${not empty param.printFriendly }" >
				${theItem.typeDescription1}
				</c:if>
				<%-- type line 2 --%> 
	<c:if test="${not empty theItem.typeDescription2}">
	<br/>${theItem.typeDescription2}
</c:if>
			</td>
			<td class="datadivider">
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</td>
				<%-- description line 1, 2 & 3 --%>
				<td valign="top">
					<ps:formatDescription item="theItem" linkParticipant="true"  lineLength="55" line="2" />
				</td>
				<td class="datadivider">
					<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
				</td>

				<%-- transaction number --%>
				<td valign="top" align="right" colspan="2">
${theItem.transactionNumber}
				</td>
				<td  width="1" class="databorder">
					<img src="/assets/unmanaged/images/s.gif" width="0" height="1">
				</td>
			</tr>
</c:forEach>
</c:if>

		<ps:roundedCorner numberOfColumns="10" emptyRowColor="white"
			oddRowColor="white" evenRowColor="beige" name="theReport"
			property="details" />
		
		<tr>
			<td colspan="10" align="right">
				<report:pageCounter report="theReport" arrowColor="black" formName="pendingWithdrawalSummaryForm"/>
			</td>
		</tr>	                  
	</c:if> 
	</table>
	
	<p><content:pageFooter beanName="layoutPageBean" /></p>
	
	<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
	<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1" /></p>
</ps:form>


<c:if test="${not empty param.printFriendly}" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<table width="634" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute
				beanName="globalDisclosure" attribute="text" /></td>
		</tr>
	</table>
</c:if>

