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
<%@ page import="com.manulife.pension.ps.service.report.transaction.handler.TransactionType" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 


<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%
TransactionHistoryReportData theReport = (TransactionHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 


<table width="100%" border="0" cellspacing="0" cellpadding="0">

  <tr>

  <td width="100%" valign="top">

<%-- error line --%>
	<p>
	  <content:errors scope="request" /><content:errors scope="session" />
   	</p>
	<ps:form method="POST"
			action="/do/transaction/transactionHistoryReport/" modelAttribute="transactionHistoryReportForm" name="transactionHistoryReportForm">


  <table border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

<c:if test="${empty parm.printFriendly}" >
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>           	  		              
	  <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% } %>
      <td width="80"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
</c:if>

<c:if test="${not empty parm.printFriendly}" >
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>           	  		              
	  <td width="20"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% } %>
      <td width="50"><img src="/assets/unmanaged/images/s.gif" width="50" height="1"></td>
</c:if>

      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${empty parm.printFriendly}" >
      <td width="196"><img src="/assets/unmanaged/images/s.gif" width="196" height="1"></td>
</c:if>
<c:if test="${not empty parm.printFriendly}" >
      <td width="136"><img src="/assets/unmanaged/images/s.gif" width="136" height="1"></td>
</c:if>
      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <td width="228"><img src="/assets/unmanaged/images/s.gif" width="228" height="1"></td>
      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <td width="95"><img src="/assets/unmanaged/images/s.gif" width="95" height="1"></td>
      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <td width="90"><img src="/assets/unmanaged/images/s.gif" width="90" height="1"></td>
      <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>

<c:if test="${empty theReport}">
<c:if test="${not empty displayDates}">
    <tr class="tablehead">
      <td class="tableheadTD1" valign="middle" colspan="10"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b>
          &nbsp;from&nbsp;
            <ps:select name="transactionHistoryReportForm" property="fromDate"
                     onchange="setFilterFromSelect(this);">
            <ps:dateOptions name="<%=Constants.TXN_HISTORY_FROM_DATES%>"
                            renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
          </ps:select>
          &nbsp;to&nbsp;
          <ps:select name="transactionHistoryReportForm" property="toDate"
                     onchange="setFilterFromSelect(this);">
            <ps:dateOptions name="<%=Constants.TXN_HISTORY_TO_DATES%>"
                            renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
          </ps:select>
          &nbsp;

		  <c:if test="${empty param.printFriendly}" >
<input type="button" onclick="doFilter(); return false;" name="submit">search</input>
		  </c:if>
      </td>
      <td class="tableheadTD" colspan="3" align="right"></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
</c:if>

        <tr>
          <td colspan="12"><img src="/assets/unmanaged/images/s.gif" width="1" height="50"></td>
        </tr>
   </table>			
</c:if>
	


<c:if test="${not empty theReport}">

    <tr class="tablehead">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>         
      <td class="tableheadTD1" valign="middle" colspan="13"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b>
<% } else { %>
	  <td class="tableheadTD1" valign="middle" colspan="11"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b>
<% } %>      
          &nbsp;from&nbsp;
          <ps:select name="transactionHistoryReportForm" property="fromDate"
                     onchange="setFilterFromSelect(this);">
            <ps:dateOptions name="<%=Constants.TXN_HISTORY_FROM_DATES%>"
                            renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
          </ps:select>
          &nbsp;to&nbsp;
          <ps:select name="transactionHistoryReportForm" property="toDate"
                     onchange="setFilterFromSelect(this);">
            <ps:dateOptions name="<%=Constants.TXN_HISTORY_TO_DATES%>"
                            renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
          </ps:select>
          &nbsp;

		  <c:if test="${empty param.printFriendly}" >
<input type="button" onclick="doFilter(); return false;" name="submit" value="search"></input>
		  </c:if>

          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <b><report:recordCounter report="theReport" label="Transactions"/></b>

      </td>
      <td rowspan="2" class=databorder><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   </tr>
   
   <tr class=tablehead>  

<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>     
     <td class=tableheadTD valign=center colspan=9><b>
     	<c:if test="${empty param.printFriendly}" >
      		<img src="/assets/unmanaged/images/history_icon.gif" width="12" height="12">&nbsp;&nbsp;</b>
      		<span class="subhead style1"> Participant transaction history</span><b> &nbsp;&nbsp;&nbsp;</b></td>
		</c:if>
<% } else { %>	 		
      <td class=tableheadTD valign=center colspan=7><b>
<% } %>
      <td class="tableheadTD" colspan="4" align="right"><report:pageCounter formName="transactionHistoryReportForm" report="theReport"/></td>
    </tr>


<%-- detail column header row - transaction date... --%>
    <tr class="tablesubhead">
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>           	  		        
      <td align="left">&nbsp;</td>
      <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% } %>
      <td align="left"><b><report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_DATE%>"  formName="transactionHistoryReportForm" direction="desc">Transaction date</report:sort></b></td>
      <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <td align="left"><b>Type</b>&nbsp;
      <ps:select name="transactionHistoryReportForm" property="transactionType"
                 onchange="setFilterFromSelect(this);doFilter();">
        <ps:options collection="transactionTypes"
        			property="value"
        			labelProperty="label" />
      </ps:select>
      </td>
      <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <td align="left"><b>Description</b></td>
      <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <td align="right"><b><report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_AMOUNT%>"  formName="transactionHistoryReportForm" direction="desc">Amount($)</report:sort></b></td>
      <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <td align="right" colspan="2"><b><report:sort field="<%=TransactionHistoryReportData.SORT_FIELD_NUMBER%>"  formName="transactionHistoryReportForm" direction="desc">Transaction number</report:sort></b></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>

<%-- message line if there are no detail items --%>
<c:if test="${empty theReport.details}">
		<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="TransactionHistoryMessage"/>

    	<tr class="datacell1">
      		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>           	  		        		
      		<td colspan="12">
<% } else { %>
			<td colspan="10">
<% } %>     		
          		<content:getAttribute id="TransactionHistoryMessage"
                                attribute="text"/>
      		</td>
  		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
</c:if>

<%-- detail rows start here --%>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >


			<c:choose>
				<c:when test="${theIndex.index % 2 == 0}">
					<tr class="datacell1">
				</c:when>
				<c:otherwise>
					<tr class="datacell2">
				</c:otherwise>
			</c:choose>
			<%-- 			<%  --%>
<%-- 			if (theIndex.intValue() % 2 == 0) { %> --%>
<!--     			<tr class="datacell1"> -->
<%-- 			<% } else { %> --%>
<!--     			<tr class="datacell2"> -->
<%-- 			<% } %> --%>
      	  		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>           	  		  
        	<%-- history icon --%>
       		      <td valign="top"><report:participantHistoryIcon item="theItem" /></td>
      	  		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% }  %>
      	 	<%-- transaction date --%>
      	  		  <td valign="top"><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.transactionDate"/></td>
      	  		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

      	  	<%-- type line 1 --%>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>     
      	  		  <td valign="top"><report:formatTransactionTypeLine1 item="theItem" />
<% } else { %>
	 	  		  <td valign="top"><report:formatTransactionTypeLine1 dbContract="true" item="theItem" />
<% } %>      	  		  		
      	  <%-- type line 2 --%>
<c:if test="${not empty theItem.typeDescription2}">
<br>${theItem.typeDescription2}
</c:if>
	          	  </td>
      	   		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

			<%-- description line 1, 2 & 3 --%>
      	  		  <td valign="top">
      	  		  	<ps:formatDescription item="theItem" linkParticipant="true"/>
       	  		  </td>
      	  		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

      	  	<%-- amount --%>
      	  		  <td align="right" valign="top">
      	  		  <c:choose><c:when test="${theItem.getType().equals(TransactionType.ADJUSTMENT) &&
                         theItem.getAmount().doubleValue()==0}">
                          n/a
      	  		  </c:when><c:otherwise><render:number property="theItem.amount" type="c" sign="false"/>
      	  		  </c:otherwise>
      	  		  </c:choose>
<%--                   <% if (theItem.getType().equals(TransactionType.ADJUSTMENT) && --%>
<%--                          theItem.getAmount().doubleValue()==0) { %> --%>
<!--                         n/a -->
<%--                   <% } else { %> --%>
<%--    		  		        <render:number property="theItem.amount" type="c" sign="false"/> --%>
<%--    		  		  <% } %> --%>
      	  		  </td>
      	  		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      	  	<%-- transaction number --%>
      	  		  <td valign="top" align="right" colspan="2">
${theItem.transactionNumber}</td>
      	  		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    			  </tr>
</c:forEach>
</c:if>

<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>           	  		              
	<ps:roundedCorner numberOfColumns="14"
			  emptyRowColor="white"
			  oddRowColor="white"
			  evenRowColor="beige"
	                  name="theReport"
	                  property="details"/>

	<tr>
		<td colspan="14" align="right"><report:pageCounter formName="transactionHistoryReportForm" report="theReport" arrowColor="black"/></td>
	</tr>
	                  
<% } else { %>
	<ps:roundedCorner numberOfColumns="12"
			  emptyRowColor="white"
			  oddRowColor="white"
			  evenRowColor="beige"
	                  name="theReport"
	                  property="details"/>
	<tr>
		<td colspan="12" align="right"><report:pageCounter formName="transactionHistoryReportForm"  report="theReport" arrowColor="black"/></td>
	</tr>	                  
<% } %>
  </table>
<p><content:pageFooter beanName="layoutPageBean"/></p>
<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 

</c:if>

</ps:form>

    </td>
  </tr>

</table>


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
