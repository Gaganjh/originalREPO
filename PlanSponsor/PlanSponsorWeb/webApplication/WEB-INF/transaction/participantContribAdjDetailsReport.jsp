<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsContribAdjReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsFund"%>


<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	
	TransactionDetailsContribAdjReportData theReport = (TransactionDetailsContribAdjReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

%>
<%-- Start of report summary --%>
<p>
        <content:errors scope="request" />
</p>
<BR>

<c:if test="${not empty theReport}">

<%-- Beans used --%>

<c:if test="${empty param.printFriendly}">
<style>
#detailsTable{
	width : 700
}
#itemAmount {
	width : 100;
}
#itemUnitValue {
	width : 115;
}
</style>
</c:if>

<c:if test="${not empty param.printFriendly}">
<style>
#detailsTable{
	width : 650
}
#itemAmount {
	width : 80;
}
#itemUnitValue {
	width : 110;
}
</style>
</c:if>


<ps:form cssStyle="margin-bottom:0;"
			method="POST"
			modelAttribute="participantContribAdjDetailsForm" name="participantContribAdjDetailsForm"
			action="/do/transaction/pptContribAdjDetailsReport/">

<!-- SUMMARY TABLE -->
<table id="summaryTable" width="500" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
        <td>&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
    <tr>
        <td colspan="4">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr class="tablehead">
                <td class="tableheadTD1"><b>Transaction  information </b></td>
                <td align="right" class="tableheadTDinfo">&nbsp;</td>
              </tr>
          </table>
        </td>
    </tr>

    <tr class="tablehead">
		<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td  colspan="2" rowspan="9" class="whiteBox">
        	<table width="482">
        	
    <%-- 1st row 
    	 left side: Name, (unless it is db version then name SSN are supressed) 
    	 right side: total amount --%>
			  <tr class="whiteBox">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>  			  
				<td width="117"><strong>Name:</strong></td>
				<td width="96"><div align="left">
${participantContribAdjDetailsForm.lastName},
${participantContribAdjDetailsForm.firstName}</div></td>
<% } else { %>
				<td width="117"><strong>Transaction date :</strong></td>
				<td width="96"><div align="left">
					<render:date dateStyle="m" property="theReport.transactionDate"/></div></td>
<% } %>					
				<td width="34">&nbsp;</td>
  				<td width="131"><strong>Total amount:</strong></td>
			    <td width="62"><div align="right">
			    	<render:number property="theReport.totalAmount" type="c" defaultValue="0.00" /></div></td>
			</tr>
			  
    <%-- 2nd row 
    	 left side: SSN, 
    	 right side: txn no --%>			  
			  <tr class="whiteBox">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>  			  			  
				<td width="117"><strong>SSN:</strong></td>
				<td width="96"><div align="left">
${participantContribAdjDetailsForm.ssn}</div></td>
<% } else { %>			
				<td width="117"></td>
				<td width="96"></td>
<% } %>							
				<td width="34">&nbsp;</td>
				<td><strong>Transaction number:</strong></td>
				<td width="80"><div align="right">
${e:forHtmlContent(theReport.transactionNumber)}</div></td>
			 </tr>
			  
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>  			  			  			  
    <%-- 3rd row 
    	 left side: txn date, 
    	 right side: Transaction number --%>			  
			  <tr class="whiteBox">
				<td width="117"><strong>Transaction date :</strong></td>
				<td width="96"><div align="left">
					<render:date dateStyle="m" property="theReport.transactionDate"/></div></td>
				<td width="34">&nbsp;</td>
				<td>&nbsp;</td>
				<td width="62">&nbsp;</td>
			  </tr>
<% } %>							

        	</table>
        </td>
        <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr class="tablehead">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr class="tablehead">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr class="tablehead">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr class="tablehead">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr class="tablehead">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr class="tablehead">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr class="tablehead">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr class="tablehead">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>

	<tr class="datacell1">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td width="498" class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td rowspan="1"  colspan="2" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
	</tr>
	<tr>
	  <td class="databorder" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr> 
    
</table>

<!-- Detail table -->
<Br>
<table id="detailsTable" border="0" cellspacing="0" cellpadding="0">

	<tr>
	  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td width="235"><img src="/assets/unmanaged/images/s.gif" width="235" height="1"></td>
	  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td width="140"><img src="/assets/unmanaged/images/s.gif" width="140" height="1"></td>
	  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td id="itemAmount"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td id="itemUnitValue"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
	  <td width="4"><img src="/assets/unmanaged/images/s.gif" width="2" height="1"></td>
	  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr>
      <td colspan="12">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
		  <tr class=tablehead>
			<td class="tableheadTD1"><strong>Adjustment details</span></td>
			<td align="right" class="tableheadTDinfo">&nbsp;</td>
		  </tr>
        </table></td>
    </tr>
	<tr class="tablesubhead">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td valign="bottom"><div align="left"><b>&nbsp;&nbsp;Investment Option</b> </div></td>
	  <td class="dataheaddivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
	  <td><div align="left"><b>Money Type  </b></div></td>
	  <td class="dataheaddivider" valign="bottom" width="1"><div align="right"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></div></td>
	  <td valign="bottom"><div align="right"><b>Amount ($)  </b></div></td>
	  <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td><div align="right"><b>Unit Value </b></div></td>
	  <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td colspan="2" valign="bottom"><div align="right"><b>Number of Units  </b></div></td>
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	
<c:if test="${not empty theReport.details}">
  
  <% int style=1; %> 

<c:forEach items="${theReport.details}" var="category" varStatus="detailsIndex" >
    
    <% if(style==1) style=2;else style=1; %>
    
	<tr class="datacell<%=style%>">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="10"><b>${category.groupName}</b>
   	  </td> 	
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   	  
   	</tr>
   	
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >
<%
TransactionDetailsFund fund=(TransactionDetailsFund)pageContext.getAttribute("fund");
pageContext.setAttribute("fund",fund,PageContext.PAGE_SCOPE);
%>

    <% if(style==1) style=2;else style=1; %>
		    
    <tr class="datacell<%=style%>">
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<%-- fund name --%>
<td class="style1">${fund.name}</td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<%-- money type --%>	 
<td align="left">${fund.moneyTypeDescription}</td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<%-- amount --%>		  
	  <td align="right"><render:number property="fund.amount" type="c" sign="false" defaultValue="0.00" /></td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<%-- unit value --%>
	
	
<td align="right">${fund.displayPsUnitValue}
<% if (fund.isGuaranteedAccount()) { %>
					&#37;
			<% } %> 
   	  		<%-- <% 
   	  		
   	  	TransactionDetailsFund transactionDetailsFund=new TransactionDetailsFund();
   	  	boolean fundcheck=transactionDetailsFund.isGuaranteedAccount();
   	  		if (fundcheck) { %>
					&#37;
			<% } %> --%>
	  </td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<%-- number of units --%>	  
<td colspan="2" align="right">${fund.displayPsNumberOfUnits}</td>
	  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

</c:forEach>
</c:forEach>
	
    <% if(style==1) style=2;else style=1; %>

	<tr class="datacell<%=style%>">
	  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td><div align="right"><b>Total amount:</b> </div></td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td><div align="left"></div></td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td><div align="right">
	  	<render:number property="theReport.totalAmount" type="c" sign="false" defaultValue="0.00"/> </div></td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td>
		<div align="right"></div>          </td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td colspan="2">
	  <div align="right"></div></td>
	  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>

	<tr class="datacell<%=style%>">
	  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
	  <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="210" height="1"></td>
	  <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="140" height="1"></td>
	  <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td width="1"  class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td width="5" rowspan="2"  colspan="2" class="lastrow" align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
	</tr>
	<tr>
	  <td class="databorder" colspan="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
  </table>

<p><content:pageFooter beanName="layoutPageBean"/></p>
<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
  
</c:if>	
	
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



