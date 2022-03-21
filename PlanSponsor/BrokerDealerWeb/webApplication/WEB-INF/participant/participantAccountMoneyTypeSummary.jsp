<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantFundMoneyTypeTotalsVO" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<!-- Include common --> 
<jsp:include page="/WEB-INF/participant/participantAccountCommon.jsp" flush="true" /> 

<script type="text/javascript">   
		// This function is implementedto be executed during onLoad.
	function doOnload() {
		scroll(0, 650);
	}
</script>

<input type="hidden" name="pdfCapped" /><%--  input - name="participantAccountForm" --%>
<report:formatMessages scope="request"/>
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=BDContentConstants.FIXED_FOOTNOTE_PBA_FOR_PARTICIPANT_REPORTS%>" type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" id="footnotePBA" />

<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
<!-- Report Table Column Headers -->
<table width="918" class="report_table_content">
 	<thead>
   	<tr>
		<th width="306" class="val_str">Money Types</th>
		
		<!-- If Loan available display the Loan assets -->
<c:if test="${details.showLoanFeature eq 'YES'}">
      		<th width="306" class="val_str"><div align="center">Total Assets Excluding Loans($)</div></th>
      		<th width="306" class="val_str"><div align="center">Loan Assets($)</div></th>
</c:if>
      	
      	<!-- If Loan not available display only the Total assets -->
<c:if test="${details.showLoanFeature ne 'YES'}">
      		<th width="306" class="val_str"><div align="center">Total Assets($)</div></th>
</c:if>
    </tr>
 	</thead>
 	<tbody>
 	</tbody>
</table>

<c:if test="${participantAccountForm.hasInvestments ==true}">
<div class="report_table">
<table width="918" class="report_table_content">
<tbody>

      <tr class="spec">
	   <td width="306" colspan="3">&nbsp;</td>
     </tr>

	<!-- Employee contributions  -->
 	 <tr class="">
		<td width="306" class="val_num_cnt"><strong>Employee contributions</strong></td>
		
		<!-- If Loan available display the Loan assets -->
<c:if test="${details.showLoanFeature eq 'YES'}">
			<td width="306" class="cur">
				<report:number property="account.totalEmployeeContributionsAssets" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/> 
			</td>
			<td width="306" class="cur">
				<report:number property="account.totalEmployeeContributionsLoanAssets" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
</c:if>
		
		<!-- If Loan not available display only the Total assets -->
<c:if test="${details.showLoanFeature ne 'YES'}">
			<td width="306" class="cur">
				<report:number property="account.totalEmployeeContributionsAssets" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
</c:if>
	</tr>

  

<c:set var="theEmployeeMoneyTypeTotals" value="${account.employeeMoneyTypeTotals}"/>
	<c:set var="emloyerMoneyTypeSize" value="${account.employerMoneyTypeTotals}"/>
	<c:if test="${account.employerMoneyTypeTotals ne null}">

<c:forEach items="${theEmployeeMoneyTypeTotals}" var="employeeMoneyTypeTotals" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/>
	<% String temp = pageContext.getAttribute("indexValue").toString();
	%>
	
	<!-- Employee contributions - Money Type details -->
        <c:if test="${indexValue % 2 != 0}"> 
			  <tr >
		</c:if>
		<c:if test="${indexValue % 2 == 0}"> 
			  <tr class="spec">
	    </c:if>	

		<td width="306" class="val_num_cnt">
${employeeMoneyTypeTotals.moneyTypeName}
		</td>
<c:if test="${details.showLoanFeature eq 'YES'}">
			<td width="306" class="cur">
				<report:number property="employeeMoneyTypeTotals.balance" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/> 
			</td>
			<td width="306" class="cur">
                <report:number property="employeeMoneyTypeTotals.loanBalance" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
</c:if>
<c:if test="${details.showLoanFeature ne 'YES'}">
			<td width="306" class="cur">
				<report:number property="employeeMoneyTypeTotals.balance" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
</c:if>
	</tr>
	
</c:forEach>
</c:if>
     <%-- <c:if test="${account.employerMoneyTypeTotals.emloyerMoneyTypeSize % 2 eq 0}"> 
     <c:set var="temp" value="${classFirstRow}" scope="page"/> 
         <c:set var="classFirstRow" value="${classNextRow}" scope="page"/>
         <c:set var="classNextRow" value="${temp}" scope="page"/>
    </c:if>
    <c:if test="${account.employerMoneyTypeTotals.emloyerMoneyTypeSize % 2 ne 0}"> 
          <c:set var="temp" value="${classFirstRow}" scope="page"/> 
        <c:set var="classFirstRow" value="${classNextRow}" scope="page"/>
        <c:set var="classNextRow" value="${temp}" scope="page"/>
    </c:if>  --%>
<tr class="${classFirstRow}">
	   <td width="306" colspan="3">&nbsp;</td>
   </tr>
	<!-- Employee contributions  -->
    <tr class="${classNextRow}">
		<td width="306" class="val_num_cnt"><strong>Employer contributions</strong></td>
<c:if test="${details.showLoanFeature eq 'YES'}">
			<td width="306" class="cur">
				<report:number property="account.totalEmployerContributionsAssets" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
			<td width="306" class="cur">
				<report:number property="account.totalEmployerContributionsLoanAssets" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
</c:if>
<c:if test="${details.showLoanFeature ne 'YES'}">
			<td width="306" class="cur">
				<report:number property="account.totalEmployerContributionsAssets" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>				
			</td>
</c:if>
	</tr>

<c:set var="theEmployerMoneyTypeTotals" value="${account.employerMoneyTypeTotals}"/>
	<c:set var="emloyerMoneyTypeSize" value="${account.employerMoneyTypeTotals}"/>
	<c:if test="${not empty account.employerMoneyTypeTotals}">

<c:forEach items="${theEmployerMoneyTypeTotals}" var="employerMoneyTypeTotals" varStatus="theIndex" >


<c:set var="indexValue" value="${theIndex.index}"/>
	    <c:if test="${indexValue % 2 != 0}"> 
			  <tr class="${classNextRow}">
		</c:if>
		<c:if test="${indexValue % 2 == 0}"> 
			  <tr class="${classFirstRow}">
	    </c:if>	

		<td width="306" class="val_num_cnt">
${employerMoneyTypeTotals.moneyTypeName}
		</td>
<c:if test="${details.showLoanFeature eq 'YES'}">
			<td width="306" class="cur">
				<report:number property="employerMoneyTypeTotals.balance" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/> 
			</td>
			<td width="306" class="cur">
                <report:number property="employerMoneyTypeTotals.loanBalance" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
</c:if>
<c:if test="${details.showLoanFeature ne 'YES'}">
			<td width="306" class="cur">
				<report:number property="employerMoneyTypeTotals.balance" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
</c:if>
	</tr>
</c:forEach>
</c:if>
  <%--    <c:if test="${account.employerMoneyTypeTotals % 2 ne 0}"> 
         <c:set var="temp" value="${classFirstRow}" scope="page"/> 
         <c:set var="classFirstRow" value="${classNextRow}" scope="page"/>
         <c:set var="classNextRow" value="${temp}" scope="page"/>
    </c:if>
    <c:if test="${account.employerMoneyTypeTotals % 2 eq 0}">
        <c:set var="temp" value="${classFirstRow}" scope="page"/> 
        <c:set var="classFirstRow" value="${classNextRow}" scope="page"/>
        <c:set var="classNextRow" value="${temp}" scope="page"/>
    </c:if> --%>


   <tr class="${classFirstRow}">
	   <td width="306" colspan="3">&nbsp;</td>
   </tr>

	<!-- Total of the contributions  -->
	<tr class="${classNextRow}">
		<td width="306" class="val_num_cnt"><strong>Total</strong></td>
<c:if test="${details.showLoanFeature eq 'YES'}">
			<td width="306" class="cur">
				<report:number property="account.totalContributionsAssets" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
			<td width="306" class="cur">
				<report:number property="account.totalContributionsLoanAssets" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
</c:if>
<c:if test="${details.showLoanFeature ne 'YES'}">
			<td width="306" class="cur">
				<report:number property="account.totalContributionsAssets" defaultValue = ""  pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>"/>
			</td>
</c:if>
	</tr>
</tbody>
</table>
</div>
</c:if>
</c:if>

<!-- FootNotes and Disclaimer -->
<div class="footnotes">
<c:if test="${participantAccountForm.asOfDateCurrent and participantAccountForm.showManagedAccount }">
			<content:contentBean
				contentId="<%=BDContentConstants.MA_FOOTNOTE%>"
				type="<%=BDContentConstants.TYPE_DISCLAIMER%>" id="participantMAFootnote" />
			<p class="footnote"><content:getAttribute id="participantMAFootnote" attribute="text" /></p>
</c:if>

<c:if test="${participantAccountForm.showPba ==true}">
			  <dl><dd><content:getAttribute beanName="footnotePBA" attribute="text"/></dd></dl>
</c:if>
		<dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
        <dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
		<div class="footnotes_footer"></div>
</div> 
<!--end of footnotes-->
