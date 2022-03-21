<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionReportData" %>
<%@ page import="com.manulife.pension.ps.web.transaction.LoanRepaymentTransactionReportForm" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionItem" %>

		 <p>
      		<content:errors scope="request" />
    	</p>

<%
	LoanRepaymentTransactionReportData theReport = (LoanRepaymentTransactionReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>
<%-- only if the report bean is available --%>
<c:if test="${not empty theReport}" >

<c:if test="${empty param.printFriendly}">
<style>

#detailsTable{
	width : 715
}

#itemName {
	width : 148;
}

#itemSSN {
	width : 80
}

#itemLoanNum {
	width : 75
}

#itemAmt {
	width : 100
}

#itemPrincipal {
	width : 75
}

#itemInterest {
	width : 75
}

</style>
</c:if>

<c:if test="${not empty param.printFriendly}">
<style>

#detailsTable{
	width : 670
}

#itemName {
	width : 135;
}

#itemSSN {
	width : 75
}

#itemLoanNum {
	width : 50
}

#itemAmt {
	width : 98
}

#itemPrincipal {
	width : 75
}

#itemInterest {
	width : 75
}

</style>
</c:if>

<ps:form cssStyle="margin-bottom:0;" modelAttribute="loanRepaymentTransactionReportForm" name="loanRepaymentTransactionReportForm"
         method="POST"
         action="/do/transaction/loanRepaymentTransactionReport/">
															 
	  <table id="detailsTable" border="0" cellspacing="0" cellpadding="0">
	    <%-- error line --%>
 	    <tr>
			<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td><img src="/assets/unmanaged/images/s.gif" width="150" height="1"></td>
			<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemName"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemSSN"><img src="/assets/unmanaged/images/s.gif" width="" height="1"></td>
		   	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		   	<td id="itemLoanNum"><img src="/assets/unmanaged/images/s.gif" width="" height="1"></td>
		   	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		    <td id="itemAmt"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  	<td id="itemPrincipal"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td id="itemInterest"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		 	<td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
		   	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        
<%--   page count row   --%>
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="16">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="tableheadTD"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></b> </td>
                <td class="tableheadTDinfo"><b><report:recordCounter report="theReport" label="Repayments"/>
                <td align="right" class="tableheadTDinfo"><report:pageCounter report="theReport" formName="loanRepaymentTransactionReportForm"/></td>
              </tr>
            </table>
          </td>
        </tr>
        
<%--   report summary section   --%>
<%--   row 1  --%>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="4" class="whiteBox" valign="top"><b class="highlight">&nbsp;Transaction
               type</b><br>
            <b>&nbsp;Loan repayment</b><br>
            <br><b class="highlight">&nbsp;Number of participants</b>
            <br><b>&nbsp;<render:number property="theReport.numberOfParticipants" defaultValue = "0"  pattern="########0"/></b><br>
            <br>
            <b class="highlight">&nbsp;Transaction date</b><br>
       	    <b>&nbsp;<render:date patternIn="yyyy-MM-dd" patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="<%= Constants.TRANSACTION_DATE%>"/></b><br><br> 
            <b class="highlight">&nbsp;Transaction number</b><br>
            <b>&nbsp;${theReport.transactionNumber}</b><br>
            <br>
          </td>
          <td class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="12" valign="top" align="right">&nbsp;</td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<%--   row 2  --%>
        <tr class="datacell2">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" colspan="5">&nbsp;</td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right"><b>Total repayment amount ($)</b></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right"><b>Total principal ($)</b></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top" align="right"><b>Total interest ($)</b></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<%--   row 3  --%>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" colspan="5"><b>Total loan repayment</b></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right"><render:number property="theReport.totalRepaymentAmount" type="c" sign="false"/></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right"><render:number property="theReport.totalPrincipalAmount" type="c" sign="false"/></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top" align="right"><render:number property="theReport.totalInterestAmount" type="c" sign="false"/></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<%--   row 4  --%>        
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" colspan="5">&nbsp;</b></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right">&nbsp;</td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" align="right">&nbsp;</td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top" align="right">&nbsp;</td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

        <tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"  class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" colspan="12" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        
<%--   detail header row   --%>
        <tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="whiteBox"><b class="highlight">Loan
              repayment details</b></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><b><report:sort field ="<%=LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME%>"
          								   direction="asc" formName="loanRepaymentTransactionReportForm">Name</report:sort></b></td>
          <td align="right" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><b><report:sort field ="<%=LoanRepaymentTransactionReportData.SORT_FIELD_SSN%>"
          								   direction="asc" formName="loanRepaymentTransactionReportForm">SSN</report:sort></b></td>
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top"><b><report:sort field ="<%=LoanRepaymentTransactionReportData.SORT_FIELD_LOAN_NUMBER%>"
          								   direction="desc" formName="loanRepaymentTransactionReportForm">Loan number</report:sort></b></td>
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top"><b><report:sort field ="<%=LoanRepaymentTransactionReportData.SORT_FIELD_REPAYMENT%>"
          								   direction="desc" formName="loanRepaymentTransactionReportForm">Repayment amount ($)</report:sort></b></td>
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top"><b><report:sort field ="<%=LoanRepaymentTransactionReportData.SORT_FIELD_PRINCIPAL%>"
          								   direction="desc" formName="loanRepaymentTransactionReportForm">Principal ($)</report:sort></b></td>
          <td valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top" colspan="2"><b><report:sort field ="<%=LoanRepaymentTransactionReportData.SORT_FIELD_INTEREST%>"
          								   direction="desc" formName="loanRepaymentTransactionReportForm">Interest ($)</report:sort></b></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        
<%--   detail rows start here   --%> 

<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:choose><c:when test="${theIndex.index % 2 ==0}">
 <tr class="datacell1">
</c:when><c:otherwise> <tr class="datacell2"></c:otherwise>
</c:choose>
<% 				
	LoanRepaymentTransactionItem theItem = (LoanRepaymentTransactionItem)pageContext.getAttribute("theItem");
	pageContext.setAttribute("theItem", theItem,PageContext.PAGE_SCOPE);
%>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="whiteBox">&nbsp;</td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   <%-- name --%>
   		 <td valign="top">
         <ps:link action="/do/participant/participantAccount/" paramId="participantId" paramName="theItem" paramProperty="participant.id">
           	${theItem.participant.lastName},&nbsp;
          	${theItem.participant.firstName}
	      </ps:link>
	      </td>
          <td align="right" valign="top" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   <%-- SSN --%>        
          <td valign="top"><render:ssn property="theItem.participant.ssn"/></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   <%-- loan number --%>
    	  <ps:map id="parameterMap">
			<ps:param name="task" value="filter"/>
			<ps:param name="loanNumber" valueBeanName="theItem" valueBeanProperty="loanNumber"/>
			<ps:param name="participantId" valueBeanName="theItem" valueBeanProperty="participant.id"/>
		  </ps:map>       
 	      <td align="right" valign="top">${theItem.loanNumber}
 		  	<c:if test="${empty param.printFriendly}" >
 		  		<ps:link action="/do/transaction/loanRepaymentDetailsReport/"
	          			 name="parameterMap">(details)
 		  		</ps:link>
 		  	</c:if>
 		  </td> 
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   <%-- repayment amount --%>        
          <td align="right" valign="top">
          	<c:if test="${not empty theItem.repaymentAmount}" >
          		<%-- reverse amount --%>
          	 	<render:number value="<%= Double.toString(theItem.getRepaymentAmount().doubleValue()*-1) %>" type="c" sign="false"/>   
	        </c:if>
	      </td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   <%-- principal amount --%>
          <td valign="top" align="right">
          	<c:if test="${not empty theItem.principalAmount}" >
          		<%-- reverse amount --%>
	        	<render:number value="<%= Double.toString(theItem.getPrincipalAmount().doubleValue()*-1) %>" type="c" sign="false"/>
	        </c:if>
          </td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   <%-- interest amount --%>         
          <td colspan="2" valign="top" align="right">
             	<c:if test="${not empty theItem.interestAmount}" >
            	<%-- reverse amount --%>
	        	<render:number value="<%= Double.toString(theItem.getInterestAmount().doubleValue()*-1) %>" type="c" sign="false"/>
	        </c:if>
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		</c:forEach>
 	</c:if>
 	
	<%
	String rowColor = (theReport.getDetails().size() % 2 == 1) ? "white" : "beige";
	%>
	
    <tr class="<%= rowColor %>border">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
		<td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="150" height="1"></td>
		<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td id="itemName"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td id="itemSSN"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   	<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   	<td id="itemLoanNum"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   	<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td id="itemAmt"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  	<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  	<td id="itemPrincipal"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td> 
		<td id="itemInterest"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td> 
  	    <td rowspan="2" colspan="2" align="right" valign="bottom"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
    </tr>  
 	<tr>
      <td class="databorder" colspan="14"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr> 
    
 	<tr>
      <td colspan="16" align="right"><report:pageCounter report="theReport" arrowColor="black" formName="loanRepaymentTransactionReportForm"/></td>
    </tr> 

 	<tr>
		<td colspan="16">
			<br>
			<p><content:pageFooter beanName="layoutPageBean"/></p>
 			<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
 			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
 			<br>
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
