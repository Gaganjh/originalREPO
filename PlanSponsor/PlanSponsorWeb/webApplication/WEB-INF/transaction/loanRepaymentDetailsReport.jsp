<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.service.account.valueobject.LoanHoldings" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentDetailsItem" %>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- This jsp includes the following CMA content --%>
<%@ page
	import="com.manulife.pension.service.account.valueobject.LoanGeneralInfoVO"%>
<%@ page	import="java.util.Set"%>
<%@ page	import="java.util.Iterator"%>
<%@ page	import="java.util.Map"%>
<%@ page	import="java.math.BigDecimal"%>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentDetailsReportData" %>
<content:contentBean 	contentId="<%=ContentConstants.COMMON_HOWTO_SECTION_TITLE%>"
                        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                        id="HowToTitle"/>
                        
                        <content:contentBean 	contentId="<%=ContentConstants.MESSAGE_DEFAULT_PROVISION%>"
                        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"  id="defaultProvisionMsg"/>

<%-- Define Printer Friendly styles --%>
<c:if test="${empty param.printFriendly}" >
<style>
#detailsTable{width : 715}
</style>
</c:if>

<c:if test="${not empty param.printFriendly }" >
<style>
#detailsTable{width : 665}
</style>
</c:if>

<%-- Start of report summary --%>
<p>
	<content:errors scope="request" />
</p>
<%
LoanRepaymentDetailsReportData loanData = (LoanRepaymentDetailsReportData)request.getAttribute(Constants.LOAN_REPAYMENT_DETAILS_REPORT_DATA);
pageContext.setAttribute("loanData",loanData,PageContext.PAGE_SCOPE);
%>

<c:if test="${not empty loanData}">
<%-- Beans used --%>


			 
  <!-- start loan details table -->
        <P></P>
        <TABLE id="detailsTable" border="0" cellspacing="0" cellpadding="3">
          <TBODY>
            <TR class="tablehead">
              <TD class="tableheadTD1" colspan="9">
                <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                  <TBODY>
                    <TR>
                      <TD class="tableheadTD"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b> 
                      <ps:map id="parameterMap">
			  				<ps:param name="profileId" valueBeanName="loanRepaymentDetailsReportForm" valueBeanProperty="profileId"/>
		  				</ps:map>
            
            			<b><ps:link action="/do/participant/participantAccount/" name="parameterMap">
${loanData.name}</ps:link>
						</b></TD>
                      <TD class="tableheadTDinfo">&nbsp;</TD>
                      <TD align="right" class="tableheadTDinfo">&nbsp;</TD>
                    </TR>
                  </TBODY>
                </TABLE>
              </TD>
            </TR>
            
            <TR valign="top">
              <TD width="1" class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD width="713" class="datacell1" colspan="7"><SPAN class="highlight"><B>Loan # <render:number type="i" property="loanData.number"/></B></SPAN></TD>
              <TD width="1" class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
            
            
            <TR>
              <TD class="datadivider" colspan="9"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
<c:if test="${not empty loanData.loanGeneralInfo}">
           <TR valign="top">
             	<TD width="1" class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD width="165" class="datacell2"><STRONG>Type of loan</STRONG></TD>
              <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            <TD width="165" class="datacell2 highlight">
<c:if test="${loanData.loanGeneralInfo.typeOfLoan =='LI'}">
            		Loan Issue
</c:if>
<c:if test="${loanData.loanGeneralInfo.typeOfLoan !='LI'}">
            		Transfer Loan
</c:if>
            	</TD>
              <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="datacell2"><STRONG>Loan reason</STRONG></TD>
              <TD class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
				<TD class="datacell2 highlight">
<c:if test="${loanData.loanGeneralInfo.loanReason =='PP'}">

              			Purchase of Primary Residence
</c:if>
<c:if test="${loanData.loanGeneralInfo.loanReason =='HD'}">

              		  Hardship
</c:if>
<c:if test="${loanData.loanGeneralInfo.loanReason =='GP'}">

               			 General Purpose
</c:if>
<c:if test="${loanData.loanGeneralInfo.loanReason !='PP'}">

<c:if test="${loanData.loanGeneralInfo.loanReason !='HD'}">

<c:if test="${loanData.loanGeneralInfo.loanReason !='GP'}">

              	 		 Unspecified
</c:if>
</c:if>
</c:if>
					</TD>
					<TD width="1" class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              </tr>
              <tr valign="top">
               	<TD width="1" class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="datacell1"><strong>Interest rate</strong></TD>
              <TD class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="datacell1 highlight"><render:number type="d" property="loanData.loanGeneralInfo.loanInterestRate"/>%</TD>
              <TD class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              
<c:if test="${loanData.loanGeneralInfo.typeOfLoan =='LI'}">
					 <TD width="165" class="datacell1">
						 <STRONG>Loan issue date </STRONG></TD>
</c:if>
<c:if test="${loanData.loanGeneralInfo.typeOfLoan =='LT'}">
			 		<TD width="165" class="datacell1"><STRONG>Transfer date</STRONG></TD>
</c:if>
         
              <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD width="220" class="datacell1 highlight">
                <render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"	
		               property="loanData.loanGeneralInfo.loanBeginDate"/></TD>
              
              <TD width="1" class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
            <TR valign="top">
             
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="datacell2"><STRONG>Loan maturity date</STRONG></TD>
              <TD class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="datacell2 highlight"><SPAN id="loanMaturityDateSpan">
               <render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"	
		               property="loanData.loanGeneralInfo.loanMaturityDate"/>
              </SPAN>
 <INPUT name="maturityDate" id="maturityDate" type="hidden" value="${loanData.loanGeneralInfo.loanMaturityDate}"></TD>
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="datacell2"> </TD>
              <TD class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="datacell2 highlight"></TD>
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
            
</c:if>
            <TR>
              <TD class="datadivider" colspan="9"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
<c:set var="relation" value="${loanData.moneyTypeFunds}" />  
              <c:if test="${not empty relation}">
            <TR class="tablesubhead" valign="top">
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD><IMG id="dynamicErrorIcon_defaultProvision" style="display: none;" src="/assets/unmanaged/images/error.gif">
              <STRONG>Money type</STRONG></TD>
              <TD class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD colspan="5"><STRONG>Amount ($)</STRONG></TD>
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
          
            <c:set var="index" value="0" />	
             <c:set var="totalAmt" value="0" />	
             <c:set var="rowClass" value="datacell1" />
            <c:forEach var="relationMap" items="${relation}" >
			<c:set var="rowId" value="${index%2}" />									
		
			<TR valign="top">
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="${rowClass}"><IMG id="dynamicErrorIcon_defaultProvision" style="display: none;" 
src="/assets/unmanaged/images/error.gif">${relationMap.key}
              </TD>
              <TD class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="${rowClass}" colspan="5"><render:number type="d" property="relationMap.value"/></TD>
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
            	<c:if test="${rowId==0}">
					<c:set var="rowClass" value="datacell2" />
</c:if>
				<c:if test="${rowId!=0}">
					<c:set var="rowClass" value="datacell1" />
</c:if>
			    <c:set var="index" value="${index+1}" />	
			     <c:set var="totalAmt" value="${totalAmt+relationMap.value}" />	
			  </c:forEach>
            <TR valign="top">
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="${rowClass}"><IMG id="dynamicErrorIcon_defaultProvision" style="display: none;" src="/assets/unmanaged/images/error.gif"><STRONG>Total</STRONG></TD>
              <TD class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="${rowClass}" colspan="5"><render:number type="d" property="totalAmt"/></TD>
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
            <TR>
              <TD class="datadivider" colspan="9"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
</c:if>
            <TR valign="top">
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="datacell1" colspan="7"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
            <TR valign="top">
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="datacell1" colspan="7"><IMG id="dynamicErrorIcon_defaultProvision" style="display: none;" src="/assets/unmanaged/images/error.gif"> <STRONG>Default provision </STRONG></TD>
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
            <TR valign="top">
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
           <!--    <TD class="datacell1" colspan="7"> The entire unpaid balance and interest of the loan will become due and payable upon (a) termination of employment with the employer, or (b) failure to make a scheduled payment. Default on the loan will occur if repayment is not made in full by the end of the calendar quarter following the calendar quarter in which either one of the above events occurs. </TD>
           --> 
		 
           <TD class="datacell1" colspan="7">
           <content:getAttribute beanName="defaultProvisionMsg" attribute="text" />
           </TD>
           
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
            <TR valign="top">
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="datacell1" colspan="7"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              <TD class="databorder"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
            <TR>
              <TD class="databorder" colspan="9"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
            </TR>
          </TBODY>
        </TABLE>  
        <!-- end loan details table -->

<br/>


<table id="detailsTable" border="0" cellspacing="0" cellpadding="0">
	
	

<!-- top row -->
	<tr>
		<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	<td><img src="/assets/unmanaged/images/s.gif" width="150" height="1"></td>
    	<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	<td><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
    	 <td width="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   	</tr>
   	<tr class="tablehead">
		<td class="tableheadTD1" colspan="16">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="tableheadTD"><b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b></td>
     				<td class="tableheadTDinfo">&nbsp;</td>
         			<td align="right" class="tableheadTDinfo">&nbsp;</td>
				</tr>
    		</table>
     	</td>
    </tr>
		
    	<tr class="datacell1">
			 <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      		<td valign="top" colspan="3"><b>Outstanding balance</b>
      			 as of <render:date dateStyle="m" property="loanData.inquiryDate"/></td>
      		<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      		<td valign="top" class="highlight"  align="left"><render:number type="c" property="loanData.outstandingBalanceAmount"/></td>
			<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      	<td valign="top" colspan="5"><b>Last payment total amount</b></td>
	      	<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	  	<td valign="top" class="highlight"  colspan="2"  align="left"><render:number type="c" property="loanData.lastRepaymentAmount"/></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>

			
			
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="top" colspan="3"><b>Date of last payment</b></td>
 			<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${empty loanData.lastRepaymentDate}">
			<td valign="top" class="highlight" align="left">n/a</td>
</c:if>
		  
<c:if test="${not empty loanData.lastRepaymentDate}">
			<td valign="top" class="highlight" align="left"><render:date dateStyle="m" property="loanData.lastRepaymentDate"/></td>
</c:if>
  	    	<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  	    	<td valign="top" colspan="5"><b>Days since last payment</b></td>
  	    	<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${not empty loanData.lastRepaymentDate}">
			<td valign="top" class="highlight"  colspan="2" align="left"><render:number type="n" scale="0" defaultValue="0" property="loanData.daysSinceLastPayment"/></td>
</c:if>
		  
<c:if test="${empty loanData.lastRepaymentDate}">
			<td valign="top" class="highlight"   colspan="2"  align="left">n/a</td>
</c:if>
  	    	
			
      		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>

		
	   	<tr class="tablesubhead">
    		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	  
<c:if test="${empty loanData.items}">
			<td valign="top" rowspan="5"  class="whiteBox"><b><span class="highlight">Repayment
                 details</span></b></td>
</c:if>
          
<c:if test="${not empty loanData.items}">
			<td valign="top" rowspan="<%=loanData.getItems().length + 2%>"  class="whiteBox"><b><span class="highlight">Repayment
                 details</span></b></td>
</c:if>
          
			<td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="top"><b>Date</b></td>
			<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="top"><b>Activity</b></td>
			<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="top" align="right"><b>Amount ($)</b></td>
			<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="top" align="right"><b>Principal ($)</b></td>
			<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="top" align="right"><b>Interest ($)</b></td>
     		<td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        <td valign="top" colspan="2" align="right"><b>Loan balance ($)</b></td>
    	    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  	</tr>
<%-- message line if there are no detail items --%>
<c:if test="${empty loanData.items}">
		<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_LOAN_REPAYMENTS%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="NoLoanRepaymentsMessage"/>
	
    	<tr class="datacell1">
     		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      		<td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      		<td colspan="12">
          		<content:getAttribute id="NoLoanRepaymentsMessage"
                                attribute="text"/>
      		</td>
	  		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
</c:if>

<%-- Start of detail --%>
<c:if test="${not empty loanData.items}">
<c:forEach items="${loanData.items}" var="theItem" varStatus="theIndex" >
				<% 				
					LoanRepaymentDetailsItem theItem = (LoanRepaymentDetailsItem)pageContext.getAttribute("theItem");
				   %>

				<c:set var="rowId" value="${theIndex.index%2}" />	
				<c:if test="${rowId==0}">
					<c:set var="rowClass" value="datacell2" />
</c:if>
				<c:if test="${rowId!=0}">
					<c:set var="rowClass" value="datacell1" />
</c:if>
	 <tr class="${rowClass}">
       		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td valign="top"><render:date dateStyle="m" property="theItem.date"/></td>
          	<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td valign="top"><%=theItem.getTypeDesc()%></td>
          	<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td valign="top" align="right"><render:number type="d" property="theItem.amount" defaultValue=""/></td>
          	<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td valign="top" align="right"><render:number type="d" property="theItem.principal" defaultValue=""/></td>
          	<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td valign="top" align="right"><render:number type="d" property="theItem.interest" defaultValue=""/></td>
          	<td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td colspan="2" valign="top" align="right"><render:number type="d" property="theItem.balance"/></td>
          	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
     	</tr>
</c:forEach>
</c:if>

<c:if test="${empty loanData.items}">
	    <tr class="whiteborder">
			<td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
	    	<td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    	<td colspan="11"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		    <td rowspan="2" colspan="2" width="5" class="whiteBox"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td> 
	   	</tr> 
        <tr>
          	<td class="databorder" colspan="14"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:if>

<c:if test="${not empty loanData.items}">
		<%
			String rowColor = (loanData.getItems().length % 2 == 1) ? "white"
							: "beige";
		%>
		
	    <tr class="<%=rowColor%>border">
			<td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
	    	<!-- td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" height="1"></td -->
	    	<td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    	<td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    	<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    	<td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
 		    <td rowspan="2" colspan="2" width="5" class="<%=rowColor%>Box"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>  
	   	</tr> 
 
        <tr>
          	<td class="databorder" colspan="14"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:if>

 	<tr>
		<td colspan="14">
			<br>
			<p><content:pageFooter beanName="layoutPageBean"/></p>
 			<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
 			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
 			<br>
 		</td>
	</tr>

<%-- End of LoanRepayemntDetailsPage Body content --%>
</table>

</c:if>
<c:if test="${not empty param.printFriendly}" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>
	<br>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
