<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsContributionReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsFund"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<un:useConstants var="cconstants"
	className="com.manulife.pension.ps.web.Constants" />
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%-- Start of report summary --%>
<p>
        <content:errors scope="session" />
</p>
<BR>

<%
TransactionDetailsContributionReportData theReport = (TransactionDetailsContributionReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>
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
	width : 85;
}
</style>
</c:if>


<ps:form cssStyle="margin-bottom:0;"
			method="POST" modelAttribute="participantContributionDetailsForm" name="participantContributionDetailsForm"
			action="/do/transaction/pptContributionDetailsReport/">


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
        	
    <%-- It if is DB contract then suppress NAME and SSN(PTC.5)
         1st row 
    	 left side: Name, 
    	 right side: Contribution EE if there is EE otherwise Contribution ER --%>
			  <tr class="whiteBox">
			 
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>			  
				<td width="117" valign="top"><strong>Name:</strong></td>
				<td width="96"><div align="left">
${participantContributionDetailsForm.lastName},
${participantContributionDetailsForm.firstName}</div></td>
<% } else { %>
				<td width="117"><strong>Transaction date :</strong></td>
				<td width="96"><div align="left">
					<render:date dateStyle="m" property="theReport.transactionDate"/></div></td>
<% } %>					
				<td width="34">&nbsp;</td>

<c:if test="${theReport.hasEmployeeContribution ==true}">
				<td width="149" valign="top"><strong>Contribution - employee :</strong></td>
			    <td width="62" valign="top"><div align="right">
			  		<render:number property="theReport.contributionEEAmount" type="c"/></div></td>
</c:if>
		
<c:if test="${theReport.hasEmployeeContribution !=true}">
   				<td width="149"><strong>Contribution - employer :</strong></td>
			    <td width="62"><div align="right">
			    	<render:number property="theReport.contributionERAmount" type="c"/></div></td>
</c:if>
			  </tr>
			  
    <%-- 2nd row 
    	 left side: SSN, 
    	 right side: Contribution ER if there is EE & ER otherwise or txn number --%>			  
			  <tr class="whiteBox">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>			  			  
				<td width="117"><strong>SSN:</strong></td>
				<td width="96"><div align="left">
${participantContributionDetailsForm.ssn}</div></td>
				<td width="34">&nbsp;</td>
<% } else { %>
			<logicext:if name="theReport" property="hasPayrollEndDate" op="equal" value="true">
			  <logicext:then>
				<td width="117"><strong>Contribution date:</strong></td>
				<td width="96"><div align="left">
					<render:date dateStyle="m" property="theReport.payrollEndDate"/></div></td>
				<td>&nbsp;</td>
			  </logicext:then>
			  
			  <logicext:else>
				<logicext:if name="theReport" property="moneySourceDescription" op="notEqual" value="">
					<logicext:then>
						<td width="117" valign="top"><strong>Contribution type: </strong></td>
						<td width="96"><div align="left">
${theReport.moneySourceDescription}</div></td>
						<td width="34">&nbsp;</td>
			  		</logicext:then>
			  		<logicext:else>
			  			<td>&nbsp;
			  			<td>&nbsp;
			  			<td>&nbsp;
			  		</logicext:else>
				</logicext:if>
			  </logicext:else>
			</logicext:if>
<% } %>										

			<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
			  <logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>
			  		
			  <logicext:then>
  				<td width="149"><strong>Contribution - employer :</strong></td>
			    <td width="62"><div align="right">
			    	<render:number property="theReport.contributionERAmount" type="c"/></div></td>
			  </logicext:then>
		
		 	  <logicext:else>	
				<td width="149"><strong>Transaction number:</strong></td>
				<td width="62"><div align="right">
${e:forHtmlContent(theReport.transactionNumber)}</div></td>
			  </logicext:else>
			  
			</logicext:if>
			 
			 </tr>
			  
    <%-- 3rd row 
    	 left side: txn date, 
    	 right side: total amount if both EE and ER otherwise blank --%>			  
			  <tr class="whiteBox">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>			  			  
				<td width="117"><strong>Transaction date :</strong></td>
				<td width="96"><div align="left">
					<render:date dateStyle="m" property="theReport.transactionDate"/></div></td>
<% } else { %>
	<logicext:if name="theReport" property="hasPayrollEndDate" op="equal" value="true">
		<logicext:and name="theReport" property="moneySourceDescription" op="notEqual" value=""/>
			
			<logicext:then>			  
				<td width="117" valign="top"><strong>Contribution type: </strong></td>
				<td width="96"><div align="left">
${theReport.moneySourceDescription}</div></td>
			</logicext:then>
			<logicext:else>
				<td width="117" valign="top"></td>
				<td width="96"></td>			    
			</logicext:else>    
			
	</logicext:if>
<% } %>					
				<td width="34">&nbsp;</td>
				
			<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
			  <logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>
			  		
			  <logicext:then>
				<td  width="149"><strong>Total amount : </strong></td>
				<td width="62"><div align="right">
					<render:number property="theReport.totalContribution" type="c"/></div></td>
			  </logicext:then>
			  
			  <logicext:else>			    
				<td>&nbsp;</td>
				<td width="62">&nbsp;</td>
			  </logicext:else>
			  
			</logicext:if>
			  </tr>
			  
    <%-- 4th row 
    	 left side: payroll ending date if present otherwise contribution type
    	 right side: txn number if both EE and ER otherwise blank --%>			  

			  <tr class="whiteBox">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>				  
			<logicext:if name="theReport" property="hasPayrollEndDate" op="equal" value="true">
			  <logicext:then>
				<td width="117"><strong>Payroll ending:</strong></td>
				<td width="96"><div align="left">
					<render:date dateStyle="m" property="theReport.payrollEndDate"/></div></td>
				<td>&nbsp;</td>
			  </logicext:then>
			  
			  <logicext:else>
				<logicext:if name="theReport" property="moneySourceDescription" op="notEqual" value="">
					<logicext:then>
						<td width="117" valign="top"><strong>Contribution type: </strong></td>
						<td width="96"><div align="left">
${theReport.moneySourceDescription}</div></td>
						<td width="34">&nbsp;</td>
			  		</logicext:then>
			  		<logicext:else>
			  			<td>&nbsp;
			  			<td>&nbsp;
			  			<td>&nbsp;
			  		</logicext:else>
				</logicext:if>
			  </logicext:else>
			</logicext:if>
<% } else { %>
			  			<td>&nbsp;
			  			<td>&nbsp;
			  			<td>&nbsp;			
<% } %>			  
			<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
			  <logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>
			  		
			  <logicext:then>
				<td><strong>Transaction number:</strong></td>
				<td width="62"><div align="right">
${e:forHtmlContent(theReport.transactionNumber)}</div></td>
			  </logicext:then>

			  <logicext:else>				
				<td>&nbsp;</td>
				<td width="62">&nbsp;</td>
			  </logicext:else>
			  
			</logicext:if>
				
			  </tr>

    <%-- 5th row 
    	 left side: contribution type if payroll ending date is present, otherwise blank 
    	 right side: blank --%>			  
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>	
	<logicext:if name="theReport" property="hasPayrollEndDate" op="equal" value="true">
		<logicext:and name="theReport" property="moneySourceDescription" op="notEqual" value=""/>
			
			<logicext:then>			  
			  <tr class="whiteBox">
				<td width="117" valign="top"><strong>Contribution type: </strong></td>
				<td width="96"><div align="left">
${theReport.moneySourceDescription}</div></td>
				<td width="34">&nbsp;</td>
				<td>&nbsp;</td>
				<td width="62">&nbsp;</td>
			  </tr>				
			</logicext:then>
			
	</logicext:if>
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
			<td class="tableheadTD1"><strong>Contribution details</strong></td>
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
	  <td align="right"><render:number property="fund.amount" type="c" sign="false"/></td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<%-- unit value --%>
<td align="right">${fund.displayPsUnitValue}
   	  		<% if (fund.isGuaranteedAccount()) { %>
					&#37;
			<% } %>   	  
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
	  	<render:number property="theReport.totalContribution" type="c" sign="false"/></div></td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td>
		<div align="right"></div></td>
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  <td colspan="2">
	  <div align="right"></div></td>
	  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>

	<tr class="datacell<%=style%>">
	  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
	  <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="235" height="1"></td>
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

<c:if test="${not empty param.printFriendly}">
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
         type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
         id="globalDisclosure"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>



