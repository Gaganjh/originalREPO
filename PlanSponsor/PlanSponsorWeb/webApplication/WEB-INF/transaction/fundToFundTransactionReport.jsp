<%@page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund"%>
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
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFTFReportData" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%-- This jsp includes the following CMA content --%>

<%-- Beans used --%>
<p>
        <content:errors scope="request" /><content:errors scope="session" />
</p>

<%
TransactionDetailsFTFReportData theReport = (TransactionDetailsFTFReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 




<content:contentBean contentId="<%=ContentConstants.FUND_TO_FUND_DETAILS_EXPAND%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="showDetailMsg"/>
<content:contentBean contentId="<%=ContentConstants.FUND_TO_FUND_DETAILS_COLLAPSE%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="hideDetailMsg"/>

<c:if test="${empty param.printFriendly}">

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

<BR>


<% 
	// Default width
	int fullWidth = 700;
	int sumCol1Width = 130;
	int sumCol3Width = 150;
	int transfersCol1Width = 335;
	int det1Col1Width = 160;  // without comments
	int det1Col2Width = 140;
	int det1Col3Width = 100;
	int det2Col2Width = 175;  // with comments
	int det2Col3Width = 105;
	int det2Col4Width = 105;
%>
<c:if test="${not empty param.printFriendly}" >
<% 	
	// Width for print version 
	fullWidth = 650;
	sumCol1Width = 105;
	sumCol3Width = 125;
	transfersCol1Width = 285;
	det1Col1Width = 160;
	det1Col2Width = 140;
	det1Col3Width = 67;
	det2Col2Width = 130; 
	det2Col3Width = 110;
	det2Col4Width = 90;
%>
</c:if>

<% if (theReport.getRedemptionFees().doubleValue() != 0) { %>

<%
	
  
    String fundPackageRetail= Constants.FUND_PACKAGE_RETAIL;
    pageContext.setAttribute("fundPackageRetail",fundPackageRetail,PageContext.PAGE_SCOPE);
    String fundPackageHybrid=Constants.FUND_PACKAGE_HYBRID;
    pageContext.setAttribute("fundPackageHybrid",fundPackageHybrid,PageContext.PAGE_SCOPE);
    String fundPackageBroker=Constants.FUND_PACKAGE_BROKER;
    pageContext.setAttribute("fundPackageBroker",fundPackageBroker,PageContext.PAGE_SCOPE);
    String fundPackageVenture= Constants.FUND_PACKAGE_VENTURE;
    pageContext.setAttribute("fundPackageVenture",fundPackageVenture,PageContext.PAGE_SCOPE);
    String fundPackageMulticlass=Constants.FUND_PACKAGE_MULTICLASS;
    pageContext.setAttribute("fundPackageMulticlass",fundPackageMulticlass,PageContext.PAGE_SCOPE);
    
  %>


<c:if test="${applicationScope.environment.siteLocation == 'usa'}" >



	<c:if test="${USER_PROFILE.currentContract.contractAllocated ==true}">

	<c:if test="${USER_PROFILE.currentContract.nml ==false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageRetail'}" >
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_RETAIL_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageHybrid'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRID_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageBroker'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_BROKER_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageVenture'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_VENTURE_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageMulticlass'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRID_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.nml ==true}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageRetail'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_RETAILNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageHybrid'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageBroker'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageVenture'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_VENTURE_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageMulticlass'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.contractAllocated ==true}">
		<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
	<BR><BR>						
</c:if>

<c:if test="${environment.siteLocation == 'ny' }" >
<c:if test="${USER_PROFILE.currentContract.contractAllocated ==true}">
<c:if test="${USER_PROFILE.currentContract.nml ==false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageRetail'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_RETAIL_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageHybrid'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRID_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageBroker'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_BROKER_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageVenture'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_VENTURE_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageMulticlass'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRID_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.nml ==true}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageRetail'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_RETAILNML_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageHybrid'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageBroker'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageVenture'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_VENTURE_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'fundPackageMulticlass'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.contractAllocated ==true}" >
		<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
	<BR><BR>					
</c:if>
<% } %>


<!-- SUMMARY TABLE -->

<table id="summaryTable" border="0" cellspacing="0" cellpadding="0" width="<%= fullWidth %>">
    <tr>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="15" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="<%= sumCol1Width %>" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="200" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="<%= sumCol3Width %>" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="125" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="14" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
    <tr class="tablehead">
        <td class="tableheadTD1" colspan="10"><b>&nbsp;&nbsp;Fund to fund transfer summary</b></td>
    </tr>
    <tr class="whiteBox">
        <td height="18" class="databorder"></td>
        <td></td>
        <td><strong>Transaction type:</strong></td>
        <td>Inter-account transfer - Fund to fund transfer</td>
        <td></td>
        <td><strong>Total amount:</strong></td>
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
        <td><strong>Transaction number:</strong></td>
<td>${e:forHtmlContent(theReport.transactionNumber)}</td>
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
        <td><strong>Submission method:</strong></td>
<td>${theReport.mediaCode}</td>
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
        <td><strong>Source of transfer:</strong></td>
<td>${theReport.sourceOfTransfer}</td>
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
        <td></td>
        <td></td>
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


<!-- TRANSFERRED FROM AND TO TABLE -->
<table border="0" cellpadding="0" cellspacing="0" width="<%= fullWidth %>">

       <tr>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%= transfersCol1Width %>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="75" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="tablehead">
             <td class="tableheadTD1" colspan="10" align="left"><b>&nbsp;&nbsp;Transferred from</b></td>
        </tr>
        <tr class="tablesubhead">
            <td class="databorder"></td>
            <td align="left" valign="bottom"><strong>Investment option</strong></td>
            <td class="dataheaddivider"></td>
            <td align="left" valign="bottom">
		<% if (theReport.doFromMoneyTypesExist()) { %>
			<strong>Money type</strong>
		<% } %>
	    </td>
            <td class="dataheaddivider"></td>
			<% if (theReport.showFromPercent()) { %>
	            <td align="right" valign="bottom"><strong>Amount ($)</strong></td>
    	        <td class="dataheaddivider"></td>
        	    <td align="right" valign="bottom"><strong>% Out</strong></td>
			<% } else { %>
            	<td align="right" valign="bottom"><strong>Amount ($)</strong></td>
        	    <td colspan="2" align="right" valign="bottom">&nbsp;</strong></td>
			<% } %>
            <td class="databox"></td>
            <td class="databorder"></td>
        </tr>
<% 
   int rowIndex = 1;
   String rowClass="";
%>
<c:forEach items="${theReport.transferFroms}" var="category" >
        <%     if (rowIndex % 2 == 0) {
                      rowClass="datacell1";
               } else {
                      rowClass="datacell2";
               }
               rowIndex++;
        %>
        <tr class="<%=rowClass%>">
            <td class="databorder"></td>
<td colspan="8"><strong>${category.groupName}</strong></td>
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
                    <% TransactionDetailsFund fund= (TransactionDetailsFund)pageContext.getAttribute("fund");
                    if (fund.getAmount() != null && fund.getPercentage() != null) { %>
						<% if (theReport.showFromPercent()) { %>
            	    	    <td align="right"><render:number property="fund.amount" type="c" sign="false"/></td> 
                	    	<td class="datadivider" width="1"></td>
	                	    <td align="right"><render:number property="fund.percentage"/>%</td> 
						<% } else { %> 
            	    	    <td align="right"><render:number property="fund.amount" type="c" sign="false"/></td> 
            	    	    <td colspan="2" align="right">&nbsp;</td> 
						<% } %>
                     <% } %> 
   	                <td class="databox"></td>
       	            <td class="databorder"></td>
            </tr>
</c:forEach>
</c:forEach>


        <!-- before totals -->
        <%     if (rowIndex % 2 == 0) {
                      rowClass="datacell1";
               } else {
                      rowClass="datacell2";
               }
        %>

            <tr class="<%=rowClass%>">
                    <td class="databorder"></td>
                    <td align="right"><strong>Total:</strong></td>
                    <td class="datadivider" width="1"></td>
                    <td>&nbsp;</td>
                    <td class="datadivider" width="1"></td>
                    <% if (theReport.getTotalFromAmount() != null && theReport.getTotalFromPct() != null) { %>
                    <td align="right"><strong>&#36;<render:number property="theReport.totalFromAmount" type="c" sign="false"/></strong></td>
                    <td class="datadivider" width="1"></td>
                    <td align="right"></td>
				    <% } %>
                    <td class="databox"></td>
                    <td class="databorder"></td>
            </tr>

       <tr class="tablehead">
             <td colspan="12" class="tableheadTD" align="left"><strong>&nbsp;&nbsp;Transferred to</strong></td>
        </tr>
        <tr class="tablesubhead">
            <td class="databorder"></td>
            <td align="left" colspan="3" valign="bottom"><strong>Investment option</strong></td>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><strong>Amount ($)</strong></td>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><strong>% In</strong></td>
            <td class="databox"></td>
            <td class="databorder"></td>
        </tr>


<c:forEach items="${theReport.transferTos}" var="category" >
        <%     rowIndex=1;
		if (rowIndex % 2 == 0) {
                      rowClass="datacell1"; 
               } else {
                      rowClass="datacell2";
               }
               rowIndex++;
        %>
        <tr class="<%=rowClass%>">
            <td class="databorder"></td>
<td colspan="8"><strong>${category.groupName}</strong></td>
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
<td colspan="3">${fund.name}</td>
                    <td class="datadivider" width="1"></td>
                   <% TransactionDetailsFund fund= (TransactionDetailsFund)pageContext.getAttribute("fund"); 
                   if (fund.getAmount() != null && fund.getPercentage() != null) { %>
                    <td align="right"><render:number property="fund.amount" type="c" sign="false"/></td> 
    	                <td class="datadivider" width="1"></td>
        	            <td align="right"><render:number property="fund.percentage"/>%</td>
            	        <td class="databox"></td>
                	    <td class="databorder"></td>
                    <% } %> 
            </tr>
</c:forEach>
</c:forEach>
        <!-- before totals -->
        <%     if (rowIndex % 2 == 0) {
                      rowClass="datacell1";
               } else {
                      rowClass="datacell2";
               }
        %>
            <tr class="<%=rowClass%>">
                    <td class="databorder"></td>
                    <td align="right" colspan="2"><strong>Total:</strong></td>
                    <td align="right"></td>
                    <td class="datadivider" width="1"></td>
                    <% if (theReport.getTotalToAmount() != null && theReport.getTotalToPct() != null) { %>
	                    <td align="right"><strong>&#36;<render:number property="theReport.totalToAmount" type="c" sign="false"/></strong></td>
    	                <td class="datadivider" width="1"></td>
        	            <td align="right"><strong><render:number property="theReport.totalToPct"/>%</strong></td>
				    <% } %>
                    <td class="databox"></td>
                    <td class="databorder"></td>
            </tr>

        <tr class="<%= rowClass %>">
            <td height="4" class="databorder"></td>
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
           <td class="databorder" colspan="8"></td>
        </tr>
</table>
	<BR>
	<% boolean someFeesApplied = false;
	   if (theReport.getRedemptionFees().doubleValue() > (double)0.0) { %>
	   	<b><content:contentBean contentId="<%=ContentConstants.MESSAGE_REDEMPTION_FEE_APPLED%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="redemptionMsg"/>
			<content:getAttribute beanName="redemptionMsg" attribute="text">
				<content:param>
					-<render:number property="theReport.redemptionFees" type="c" sign="true"/>
				</content:param>
			</content:getAttribute>
		   </b><BR><BR>
	<% 		someFeesApplied = true;
        } 
		if (theReport.getMva().doubleValue() > (double)0.0) { %>
		   <b><content:contentBean contentId="<%=ContentConstants.MESSAGE_MVA_APPLIED%>"
				                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="mvaMsg"/>
			<content:getAttribute beanName="mvaMsg" attribute="text">
				<content:param>
					-<render:number property="theReport.mva" type="c" sign="true"/>
				</content:param>
			</content:getAttribute>
			</b><BR><BR>
	<% 		someFeesApplied = true;
		}
		if (!someFeesApplied) {  %>
	      <table width="<%= fullWidth %>" border="0" cellspacing="0" cellpadding="0">
        	<tr><td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td></tr>
	      </table>
	<% } %>

<c:if test="${empty param.printFriendly}" >
<script type="text/javascript" >
  if (ie4||ns6){
    document.write('<div id="showhidediv1" style="text-align:left;" class="utilityLinks"><A href="javascript:hideshow(\'div1\')" ><content:getAttribute beanName="showDetailMsg" attribute="text"/>&nbsp;&nbsp;&gt;&gt;</a><BR></div>')
    document.write('<div id="div1" style="display:none;cursor:auto;">')
  }
</script>
</c:if>

<%
boolean showComments = ((theReport.getMva().doubleValue() != 0) || 
		   (theReport.getRedemptionFees().doubleValue() != 0));
int numColumns=12;
if (showComments){
numColumns=14;
}
%>


<!-- FUND TO FUND DETAILS TABLE -->
<table border="0" cellpadding="0" cellspacing="0" width="<%= fullWidth %>">
<% if (showComments) { // Column defs incl. comments column %>
       <tr>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%= det1Col1Width %>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%= det1Col2Width %>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%= det1Col3Width %>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="120" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="133" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<% } else {   // Column defs for no comments column %>
       <tr>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="190" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%= det2Col2Width %>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%= det2Col3Width %>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%= det2Col4Width %>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="120" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<% } %>
        <tr class="tablehead">
             <td class="tableheadTD1" colspan="14" align="left"><b>&nbsp;&nbsp;Fund to fund transfer details</b></td>
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
<% if (showComments) { %>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><strong>Comments</strong></td>
<% } %>
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
        <tr class="<%=rowClass %>">
            <td class="databorder"></td>
<td colspan="<%= (numColumns - 2) %>"><b>${category.groupName}</b></td>
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
					<%  TransactionDetailsFund fund= (TransactionDetailsFund)pageContext.getAttribute("fund");
					if (fund.displayUnitValue()) { %>
 						<render:number property="fund.displayUnitValue" scale="2" sign="true" /><% 
 								
 						  if (!fund.displayNumberOfUnits()) { %>&#37; 
					<%    }  	 
 					   } %> 
					</td>

                    <td class="datadivider" width="1"></td>
                    <td align="right">
			<% if (fund.displayNumberOfUnits()) { %> 
			<render:number property="fund.displayNumberOfUnits" scale="6" sign="false" /> 
			<% } %> 
		    </td>
<% if (showComments) { %>
                    <td class="datadivider" width="1"></td>
<td align="left">${fund.comments}</td>
<% } %>
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
<% if (showComments) { %>
            <td class="lastrow"></td>
            <td class="datadivider"></td>
<% } %>
            <td class="lastrow"></td>
            <td rowspan="2" colspan="2" width="5" class="lastrow" align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
        </tr>
        <tr>
<% if (showComments) { %>
            <td class="databorder" colspan="12"></td>
<% } else { %>
            <td class="databorder" colspan="10"></td>
<% } %>
        </tr>

</table>


<c:if test="${empty param.printFriendly }" >
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
