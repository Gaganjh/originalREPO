<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
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
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsClassConversionReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.ClassConversionDetailsFund" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
TransactionDetailsClassConversionReportData theReport = (TransactionDetailsClassConversionReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

<%-- This jsp includes the following CMA content --%>

<%-- Beans used --%>


  



<content:contentBean contentId="<%=ContentConstants.FUND_TO_FUND_DETAILS_EXPAND%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="showDetailMsg"/>
<content:contentBean contentId="<%=ContentConstants.FUND_TO_FUND_DETAILS_COLLAPSE%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="hideDetailMsg"/>
 
<c:if test="${empty param.printFriendly}" >

<script type="text/javascript" >

var ie4 = (document.all != null);

var ns6 = (document.getElementById != null);

var isMac = navigator.userAgent.indexOf("Mac") != -1;


if (ie4 & isMac) {

        ns6 = false;

        ie4 = false;

}



//begin IE 4+ And NS6 dHTML Outlines
function hideshow(which) {
     if (!document.getElementById|document.all) {
        return;
     }  else {
        if (document.getElementById) {
                oWhich = eval ("document.getElementById('" + which + "')")
                oLink = eval ("document.getElementById('showhide" + which + "')")
        } else {
            oWhich = eval ("document.all." + which)
            oLink = eval ("document.all.showhide" + which)
        }
     }
     window.focus()

     if (oWhich.style.display=="none")  {
         oWhich.style.display=""
         oLink.innerHTML = "<A href=\"javascript:hideshow(\'" + which + "\')\">&lt;&lt;&nbsp;&nbsp;<content:getAttribute beanName="hideDetailMsg" attribute="text"/></a><BR>"
     } else {
         oWhich.style.display="none"
         oLink.innerHTML = "<A href=\"javascript:hideshow(\'" + which + "\')\"><content:getAttribute beanName="showDetailMsg" attribute="text"/>&nbsp;&nbsp;&gt;&gt;</a><BR>"
     }
}

</script>
</c:if>


<%-- Start of report summary --%>
<p>
        <content:errors scope="session" />
</p>
<BR>


<% 
	// Default width
	int fullWidth = 700;
	int sumCol1Width = 130;
	int sumCol3Width = 170;
	int det2Col2Width = 175;  
	int det2Col3Width = 105;
	int det2Col4Width = 105;
%>
<c:if test="${not empty param.printFriendly }" >
<% 	
	// Width for print version 
	fullWidth = 650;
	sumCol1Width = 105;
	sumCol3Width = 125;
	det2Col2Width = 150; 
	det2Col3Width = 95;
	det2Col4Width = 90;
%>
</c:if>

<!-- SUMMARY TABLE -->

<table id="summaryTable" border="0" cellspacing="0" cellpadding="0" width="<%=fullWidth%>">
    <tr>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="15" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="<%=sumCol1Width%>" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="<%=sumCol3Width%>" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="125" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="14" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
    <tr class="tablehead">
        <td class="tableheadTD1" colspan="10"><b>&nbsp;&nbsp;Class conversion</b></td>
    </tr>
    <tr class="whiteBox">
        <td height="18" class="databorder"></td>
        <td></td>
        <td><strong>Transaction type:</strong></td>
        <td>Class conversion</td>
        <td></td>
        <td><strong>Total amount transferred out:</strong></td>
        <td><render:number property="theReport.totalAmount" type="c" sign="true"/></td>
        <td></td>
        <td class="databox"></td>
        <td class="databorder"></td>
    </tr>
    <tr class="whiteBox">
        <td height="18" class="databorder"></td>
        <td></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>  
        <td><strong>Name:</strong></td>
<td>${theReport.participantName}</td>
<% } else { %>
        <td><strong>Invested date:</strong></td>
        <td><render:date dateStyle="m" property="theReport.transactionDate"/></td>
<% } %>        
        <td></td>
        <td><strong>Total amount transferred in:</strong></td>
        <td><render:number property="theReport.totalToAmount" type="c" sign="true"/></td>
        <td></td>
        <td class="databox"></td>
        <td class="databorder"></td>
    </tr>
    <tr class="whiteBox">
        <td height="18" class="databorder"></td>
        <td></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>          
        <td><strong>SSN:</strong></td>
<td>${theReport.participantSSN}</td>
<% } else { %>
        <td><strong>Request date:</strong></td>
        <td><render:date dateStyle="m" property="theReport.requestDate" /></td>
<% } %>                
        <td></td>
        <td><strong>Transaction number:</strong></td>
<td>${theReport.transactionNumber}</td>
        <td></td>
        <td class="databox"></td>
        <td class="databorder"></td>
    </tr>
    <tr class="whiteBox">
        <td height="18" class="databorder"></td>
        <td></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>          
        <td><strong>Invested date:</strong></td>
        <td><render:date dateStyle="m" property="theReport.transactionDate"/></td>
<% } else { %>
	    <td></td>
	    <td></td>
<% } %>        
        <td></td>
        <td><strong>Submission method:</strong></td>
<td>${theReport.mediaCode}</td>
        <td></td>
        <td class="databox"></td>
        <td class="databorder"></td>
    </tr>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>              
    <tr class="whiteBox">
        <td height="18" class="databorder"></td>
        <td></td>
        <td><strong>Request date:</strong></td>
        <td><render:date dateStyle="m" property="theReport.requestDate" /></td>
        <td></td>
        <td><strong>Source of transfer:</strong></td>
<td>${theReport.sourceOfTransfer}</td>
        <td></td>
        <td class="databox"></td>
        <td class="databorder"></td>
    </tr>
<% } %>            
    <tr class="whiteBox">
         <td height="4" class="databorder"></td>
         <td colspan="7" class="lastrow"></td>
         <td rowspan="2" colspan="2" width="5" class="lastrow" align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
    </tr>
    <tr>
         <td class="databorder" colspan="8"></td>
    </tr>
</table>

<br>


<!-- CLASS CONVERSION SUMMARY TABLE -->
<table border="0" cellpadding="0" cellspacing="0" width="<%=fullWidth%>">

       <tr>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="200" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="55" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="60" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="60" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="55" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="60" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="60" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="tablehead">
             <td class="tableheadTD1" colspan="16" align="left"><b>&nbsp;&nbsp;Class conversion summary</b></td>
        </tr>
        <tr class="tablesubhead">
            <td class="databorder"></td>
            <td align="left" valign="bottom"><strong>Investment option</strong></td>
            <td class="dataheaddivider"></td>
 	    <td align="right" valign="bottom"><strong>Transfer Out ($)</strong></td>
    	    <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><strong>Unit Value</strong></td>
    	    <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><strong>Number of<Br>Units</strong></td>
            <td class="dataheaddivider"></td>
 	    <td align="right" valign="bottom"><strong>Transfer In ($)</strong></td>
    	    <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><strong>Unit Value</strong></td>
    	    <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><strong>Number of<Br>Units</strong></td>
            <td class="databox"></td>
            <td class="databorder"></td>
        </tr>
<% 
   int rowIndex = 1;
   String rowClass="";
%>
<c:forEach items="${theReport.transferFromsAndTos}" var="category" >
        <%     if (rowIndex % 2 == 0) {
                      rowClass="datacell1";
               } else {
                      rowClass="datacell2";
               }
               rowIndex++;
        %>
        <tr class="<%=rowClass%>">
            <td class="databorder"></td>
<td colspan="14"><strong>${category.groupName}</strong></td>
            <td class="databorder"></td>
        </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >
            <%     if (rowIndex % 2 == 0) {
                          rowClass="datacell1";
                   } else {
                          rowClass="datacell2";
                   }
                   rowIndex++;
            %>
 <%
ClassConversionDetailsFund fundValue = (ClassConversionDetailsFund)pageContext.getAttribute("fund");
%>
            <tr class="<%=rowClass%>">
                    <td class="databorder"></td>
<td>${fund.name}</td>
                    <td class="datadivider" width="1"></td>
                     <% if (fundValue.getAmount() != null) { %>
						
            	    	    <td align="right"><render:number property="fund.amount" type="c" sign="false"/></td>
                	    <td class="datadivider" width="1"></td>
			    <td align="right"><render:number property="fund.displayUnitValue" scale="2" sign="true" /></td> 	
			    <td class="datadivider" width="1"></td>
			    <td align="right"><render:number property="fund.displayNumberOfUnits" scale="6" sign="false" /></td> 	
                	    <td class="datadivider" width="1"></td>
            	    	    <td align="right"><render:number property="fund.toAmount" type="c" sign="false"/></td>
               	    	    <td class="datadivider" width="1"></td>
			    <td align="right"><render:number property="fund.displayToUnitValue" scale="2" sign="true" /></td> 	
			    <td class="datadivider" width="1"></td>
			    <td align="right"><render:number property="fund.displayToNumberOfUnits" scale="6" sign="false" /></td> 	
						
                    <% } %>
   	                <td class="databox"></td>
       	            <td class="databorder"></td>
            </tr>
</c:forEach>
</c:forEach>



        <tr class="dataCell1">
            <td height="4" class="databorder"></td>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
           <td rowspan="2" colspan="2" width="6" class="lastrow" align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="6" height="5" ></td>
        </tr>
        <tr>
           <td class="databorder" colspan="14"></td>
        </tr>
</table>
	<BR>

<c:if test="${empty param.printFriendly}" >
<script type="text/javascript" >
  if (ie4||ns6){
    document.write('<div id="showhidediv1" style="text-align:left;" class="utilityLinks"><A href="javascript:hideshow(\'div1\')" ><content:getAttribute beanName="showDetailMsg" attribute="text"/>&nbsp;&nbsp;&gt;&gt;</a><BR></div>')
    document.write('<div id="div1" style="display:none;cursor:auto;">')
  }
</script>
</c:if>








<%
	int numColumns=12;
%> 



<!-- CLASS CONVERSION DETAILS TABLE -->
<table border="0" cellpadding="0" cellspacing="0" width="<%=fullWidth%>">
       <tr>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="200" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%=det2Col2Width%>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%=det2Col3Width%>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%=det2Col4Width%>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="105" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

        <tr class="tablehead">
             <td class="tableheadTD1" colspan="14" align="left"><b>&nbsp;&nbsp;Class conversion details</b></td>
        </tr>
        <tr class="tablesubhead">
             <td class="databorder"></td>
            <td align="left" valign="bottom"><strong>Investment option</strong></td>
            <td class="dataheaddivider"></td>
            <td align="left" valign="bottom"><strong>Money type</strong></td>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><strong>Amount ($)</strong></td>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><strong>Unit value</strong></td>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><strong>Number of<Br>units</strong></td>
            <td class="databox"></td>
            <td class="databorder"></td>
        </tr>
<% rowIndex = 1; %>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="category" >
        <%     if (rowIndex % 2 == 0) {
                      rowClass="datacell1";
               } else {
                      rowClass="datacell2";
               }
               rowIndex++;
        %>
        <tr class="<%=rowClass%>">
            <td class="databorder"></td>
<td colspan="<%=(numColumns - 2)%>"><b>${category.groupName}</b></td>
            <td class="databorder"></td>
        </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >
            <%     if (rowIndex % 2 == 0) {
                          rowClass="datacell1";
                   } else {
                          rowClass="datacell2";
                   }
                   rowIndex++;
            %>
            <tr class="<%=rowClass%>">
                    <td class="databorder"></td>
<td>${fund.name}</td>

                    <td class="datadivider" width="1"></td>
<td align="left">${fund.moneyTypeDescription}</td>
                    <td class="datadivider" width="1"></td>
                    <td align="right"><render:number property="fund.amount" type="c" sign="false"/></td>
                    <td class="datadivider" width="1"></td>

                    <td align="right">
<%
TransactionDetailsFund fundVal = (TransactionDetailsFund)pageContext.getAttribute("fund");
%>
   
					<% if (fundVal.displayUnitValue()) { %>
						<render:number property="fund.displayUnitValue" scale="2" sign="true" /><%
						  // If has no units, means we have interest rate here
						  if (!fundVal.displayNumberOfUnits()) { %>&#37;
					<%    }  	
					   } %>
					</td>

                    <td class="datadivider" width="1"></td>
                    <td align="right">
			<% if (fundVal.displayNumberOfUnits()) { %>
			<render:number property="fund.displayNumberOfUnits" scale="6" sign="false" />
			<% } %>
		    </td>
                    <td class="databox"></td>
                    <td class="databorder"></td>
            </tr>
</c:forEach>
</c:forEach>
</c:if>
        <tr class="dataCell1">
            <td height="4" class="databorder"></td>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
            <td rowspan="2" colspan="2" width="5" class="lastrow" align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
        </tr>
        <tr>
            <td class="databorder" colspan="10"></td>
       </tr>

</table>


<c:if test="${empty param.printFriendly}" >
<script type="text/javascript" >
if (ie4||ns6){
   document.write('</div>')
}
</script>
</c:if>
<br>
<p><content:pageFooter beanName="layoutPageBean"/></p>
<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
<c:if test="${not empty param.printFriendly }" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
