<%@ taglib prefix="report" uri="manulife/tags/report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.handler.TransactionType" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountForfeituresReportData" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
CashAccountForfeituresReportData theReport = (CashAccountForfeituresReportData)request.getAttribute(Constants.REPORT_BEAN);
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
<ps:form  cssStyle="margin-bottom:0;" method="POST" action="/do/transaction/cashAccountForfeituresReport/" modelAttribute="cashAccountForfeituresForm" name="cashAccountForfeituresForm" >
<c:if test="${not empty param.printFriendly }" >
<table border="0" cellspacing="0" cellpadding="0" width="634">
</c:if>	
<c:if test="${empty param.printFriendly }" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>
<tr>
	<td>
	<%-- Tab navigation JSP --%>
		 <jsp:include flush="true" page="cashAccountTabBar.jsp">
			<jsp:param name="selectedTab" value="ForfeituresTab"/>
		</jsp:include> 
	</td>
</tr>

<tr>
	<td valign="top">
	<c:if test="${not empty theReport}">


		
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <%-- Dummy row to make the width consistent --%>
	    <tr>
	    	<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="80"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="80"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="80"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="242"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="80"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="80"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="80"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
		
		<%-- Money Type Filter row --%>
		<tr class="tablehead"  height="22px" valign="middle">
			<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td class="tableheadTD" valign="middle" align="left" colspan="11">
	        	<b>
	        	<img src="/assets/unmanaged/images/s.gif" width="10" height="1">
	        	<%-- Money type drop down disabled for Print Report --%>
	        	
	        	
	        	<c:if test="${not empty param.printFriendly}" >
	       
<form:select  path="moneyTypeId" disabled="true" id="baseFilterSelect" cssStyle="width:110px;">
<form:options  items="${cashAccountForfeituresForm.listOfContractMoneyTypes}" itemLabel="label" itemValue="value"/>
</form:select>
	            </c:if>
	            
	            <%-- Money type drop down enabled for Online Report --%> 
	            <!-- If Total Forfeitures In Plan is equal to $0 the Money Type drop down should be disabled -->
	            <c:if test="${empty param.printFriendly}" >
	            
	            
<c:if test="${theReport.totalForfeituresInPlan ==0}">
<form:select path="moneyTypeId" disabled="true" id="baseFilterSelect" cssStyle="width:110px;">
<form:options items="${cashAccountForfeituresForm.listOfContractMoneyTypes}" itemLabel="label" itemValue="value" />
</form:select>
</c:if>
<c:if test="${theReport.totalForfeituresInPlan !=0}">
<form:select path="moneyTypeId" id="baseFilterSelect" cssStyle="width:110px;">
<form:options items="${cashAccountForfeituresForm.listOfContractMoneyTypes}" itemLabel="label" itemValue="value" />
</form:select>
</c:if>
	            </c:if>
	            </b>
	            
	            <%-- From Date --%>
	            from<img src="/assets/unmanaged/images/s.gif" width="1" height="1">	            
	            <render:date patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" 
	            			property="theReport.fromDate"/>
	            <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
				
				<%-- To Date --%>
				to<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
	            <render:date patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>"
							property="theReport.toDate"/>
	            
				<c:if test="${empty param.printFriendly}" >
<input type="hidden" name="task" value="filter"/>
					<!-- If Total Forfeitures In Plan is equal to $0 the submit button should be disabled -->
<c:if test="${theReport.totalForfeituresInPlan ==0}">
						<input type="submit" name="submit" value="search" tabindex="70" disabled="disabled"/> 
</c:if>
<c:if test="${theReport.totalForfeituresInPlan !=0}">
					 	<input type="submit" name="submit" value="search" tabindex="70" /> 
</c:if>
				</c:if>
				
			    <b><report:recordCounter report="theReport" label="Transactions:" /></b>
			</td>          
		    <td align="right" colspan="2"><report:pageCounter report="theReport" formName="cashAccountForfeituresForm"/></td>
		    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
	        
		<!-- Summary Section -->	
	    <tr>
	    	<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td colspan="13">
				
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr class="datacell1" >
	            	<td valign="top" width="250" align="left">
	                	<span style="font-size: 17px;font-weight:bold;">Total Forfeitures in Plan</span>
	                </td>
	                <td align="right" width="180" valign="top" >
	                  <h4><span style="font-size: 17px;font-weight:bold;" class="highlight">
	                  	<render:number property="theReport.totalForfeituresInPlan"	type="c" sign="true" />
	                  </span></h4>
	                </td>
	                <td class="datadivider" width="1">
	                	<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
	                </td>
	            	<td rowspan="4"></td>
				</tr>
	            <tr class="datacell2">               
		              <td valign="top"  align="left">
		              	<b>Total Forfeitures in Cash Account</b>
		              </td>
		              <td align="right" valign="top">
		                <span class="highlight"><b>
		                	<render:number	property="theReport.totalForfeituresInCashAccount" type="c" sign="true" />
		                </b></span>
		              </td>
		              <td class="datadivider">
		              	<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
		              </td>
	            </tr>
				<tr class="datacell1">
					<td valign="top" align="left">
						<b>Total Forfeitures in
						<c:if test="${empty param.printFriendly}" >
		                	<a href="/do/participant/participantForfeiture">
		                		Participant Accounts
		                	</a>
		                </c:if>
		                <c:if test="${not empty param.printFriendly}" >
		                	Participant Accounts
		                </c:if>
		                </b>
					</td>
		            <td align="right" valign="top">
		            	<span class="highlight"><b>
		            		<render:number property="theReport.totalForfeituresInParticipant" type="c" sign="true" />
		            	</b></span>
		            </td>
		            <td class="datadivider">
		            	<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
		            </td>
				</tr>
				<tr>
	            	<content:contentBean contentId="<%=ContentConstants.FORFEITURES_MAY_BE_IN_PARTICIPANTS_ACCOUNTS_AND_IN_CASH_ACCOUNT%>"
	        							 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	       							     id="forfeituresContent"/>
					<td valign="top" align="left" colspan="2">
		            	<content:getAttribute beanName="forfeituresContent" attribute="text"/>
					</td>
		            <td class="datadivider">
		            	<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
		            </td>
				</tr>
				<tr class="datacell1">
	             	<td colspan="4" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>
				<tr class="datacell1">
	             	<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
				</tr>
				</table>
				
			</td>
			<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
	       
		<!-- Section 6 - Transactions Report table -->
<c:if test="${empty theReport.details}">
<c:if test="${theReport.hasTooManyItems ==true}">
        <!-- Below code displays "There are too many transactions to display for the money type selected. 
        	 Please choose a different money type and try again" -->
        		<content:contentBean contentId="<%=ContentConstants.MESSAGE_TOO_MANY_TRANSACTIONS_FOR_MONEY_TYPE__SELECTED%>"
                                 type="<%=ContentConstants.TYPE_MESSAGE%>"
                                 id="CashAccountTransactionMessage"/>
</c:if>
		<!-- Below code displays "There were no forfeitures transactions" message when there are no transactions -->
			
<c:if test="${theReport.hasTooManyItems ==false}">
            	<content:contentBean contentId="<%=ContentConstants.MESSAGE_THERE_WERE_NO_FORFEITURES_TRANSACTIONS%>"
                                   type="<%=ContentConstants.TYPE_MESSAGE%>"
                                   id="CashAccountTransactionMessage"/>
</c:if>
			<tr class="datacell1">
            	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              	<td colspan="13">
                	<content:getAttribute id="CashAccountTransactionMessage" attribute="text"/>
              </td>
              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
</c:if>
        <!-- Below code displays the column headings and transactions -->
<c:if test="${not empty theReport.details}">
		<tr class="tablesubhead" >
			<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td align="left">
				<report:sort field="transactionDate" direction="desc" formName="cashAccountForfeituresForm">	
	          		<b>Transaction date</b>
	          	</report:sort>
			</td>
	        <td class="dataheaddivider">
	        	<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
	        </td>
	        <td align="left" >
	          	<report:sort field="moneyType" direction="asc" formName="cashAccountForfeituresForm">	
	          			<b>Money type</b>
	          	</report:sort>
			</td>
	        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td align="left"><b>Type</b></td>
	        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td align="left"><b>Description</b></td>
	        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td align="center" ><b>Transaction number</b></td>
	        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td align="right" ><b>Original amount($)</b></td>
	        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td align="right" ><b>Available amount($)</b></td>
	        <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
	    
<c:forEach items="${theReport.details}" var="cashAcc" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/> 
<%String temp = pageContext.getAttribute("indexValue").toString();%>

       <c:choose>
								<c:when test="${theIndex.index % 2 == 0}">
									<tr class="datacell1">
								</c:when>
								<c:otherwise>
									<tr class="datacell2">
								</c:otherwise>
							</c:choose>
			<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="top">
	          	<render:date property="cashAcc.transactionDate"/>
			</td>
	        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td valign="top">
${cashAcc.moneyType}
	        </td>
			<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td valign="top" >
	        <!-- Type Column -->
<c:if test="${not empty cashAcc.typeDescription1}">
${cashAcc.typeDescription1}
</c:if>
<c:if test="${not empty cashAcc.typeDescription2}">
<br>${cashAcc.typeDescription2}
</c:if>
			</td>
	        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="left" valign="top" nowrap>
            	<ps:formatDescription item="cashAcc" linkParticipant="false" width="220" hideTransactionInProgress="true" />
			</td>

	        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td align="right">${cashAcc.transactionNumber}</td>
	        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td align="right"><render:number	property="cashAcc.originalAmount"/></td>
	        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td align="right"><render:number	property="cashAcc.availableAmount"/></td>
	        <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
</c:forEach>
</c:if>
		<tr>
			<td colspan="15" class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
		</tr>
		<tr>
			<td align="right" colspan="15"><report:pageCounter report="theReport" arrowColor="black" formName="cashAccountForfeituresForm"/></td>
		</tr>	
	    </table>
		
	</c:if>
	</td>
</tr>
</table>
	
	<p><content:pageFooter beanName="layoutPageBean"/></p>
 	<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
 	<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
 			
</ps:form>	

<c:if test="${not empty param.printFriendly }" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>" 
			type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure"/>
	<c:if test="${not empty param.printFriendly }" >
	<table border="0" cellspacing="0" cellpadding="0" width="634">
	</c:if>	
	<c:if test="${empty param.printFriendly }" >
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	</c:if>
	<tr>
		<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
	</tr>
	</table>
</c:if>
