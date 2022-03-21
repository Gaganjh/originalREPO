<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsRebalReportData" %>
<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %> 
<%-- This jsp includes the following CMA content --%>
<p>
        <content:errors scope="request" />
</p>
<%-- Beans used --%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
TransactionDetailsRebalReportData theReport = (TransactionDetailsRebalReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

%> 
<c:if test="${not empty theReport}">
<jsp:useBean id="transactionDetailsRebalForm" scope="session" type="com.manulife.pension.ps.web.transaction.TransactionDetailsRebalForm" /> 

<content:contentBean contentId="<%=ContentConstants.REBALANCE_DETAILS_EXPAND%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="showDetailMsg"/>
<content:contentBean contentId="<%=ContentConstants.REBALANCE_DETAILS_COLLAPSE%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="hideDetailMsg"/>








<c:if test="${empty param.printFriendly}" >
<script type="text/javascript" >

var ie4 = (document.all != null);

var ns6 = (document.getElementById != null);

var isMac = navigator.userAgent.indexOf("Mac") != -1

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
	int sumCol4Width = 195;
	int b4Col1Width = 260;
	int detailsCol1Width = 210;
%>
<c:if test="${not empty param.printFriendly}">
<% 	
	// Width for print version 
	fullWidth = 650;
	sumCol4Width = 145;
	b4Col1Width = 210;
	detailsCol1Width = 160;
%>
</c:if>

<!-- SUMMARY TABLE -->

<% if(theReport.getRedemptionFees().doubleValue() != (double)0.0){ %>

<c:if test="${applicationScope.environment.siteLocation == 'usa'}" >



	<c:if test="${userProfile.currentContract.contractAllocated ==true}">
	

	<c:if test="${userProfile.currentContract.nml ==false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='RET'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_RETAIL_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
  <c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'HYB'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRID_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='VRS'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_BROKER_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='IFP'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_VENTURE_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='MC'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRID_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if> 
</c:if>
 <c:if test="${userProfile.currentContract.nml ==true}" >
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'RET'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_RETAILNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='HYB'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='VRS'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='IFP'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_VENTURE_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='MC'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>  
</c:if>

 <c:if test="${userProfile.currentContract.contractAllocated !=true}">
		<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>   
	<BR><BR>
</c:if>

<c:if test="${v.environment.siteLocation == 'ny'}">


	<c:if test="${userProfile.currentContract.contractAllocated ==true}">
	

	<c:if test="${userProfile.currentContract.nml ==false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='RET'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_RETAIL_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
  <c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'HYB'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRID_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='VRS'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_BROKER_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='IFP'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_VENTURE_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='MC'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRID_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if> 
</c:if>
 <c:if test="${userProfile.currentContract.nml ==true}" >
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'RET'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_RETAILNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='HYB'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='VRS'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='IFP'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_VENTURE_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='MC'}">
				<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>  
</c:if>
 <c:if test="${userProfile.currentContract.contractAllocated !=true}">
		<a href="#" onClick="javascript:openPDF('<%=Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>   
	<BR><BR>
</c:if>

<% } %>

<table id="summaryTable" border="0" cellspacing="0" cellpadding="0" width="<%=fullWidth%>">
    <tr>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="15" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="130" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="195" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="135" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="<%=sumCol4Width%>" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="14" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
    <tr class="tablehead">
        <td class="tableheadTD1" colspan="10"><b>&nbsp;&nbsp;Rebalance summary</b></td>
    </tr>
    <tr class="whiteBox">
        <td height="18" class="databorder"></td>
        <td></td>
        <td><strong>Transaction type:</strong></td>
        <td>Inter-account transfer - Rebalance</td>
        <td></td>
        <td><strong>Request date:</strong></td>
        <td><render:date dateStyle="m" property="theReport.requestDate" /></td>
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
        <td><strong>Transaction Number:</strong></td>
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
        <td></td>
        <td></td>
<% } %>        
        <td></td>
        <td><strong>Submission method:</strong></td>
<td>&nbsp;${theReport.mediaCode}</td>
        <td></td>
        <td class="databox"></td>
        <td class="databorder"></td>
    </tr>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>
    <tr class="whiteBox">
        <td height="18" class="databorder"></td>
        <td></td>
        <td><strong>Invested date:</strong></td>
        <td><render:date dateStyle="m" property="theReport.transactionDate"/></td>
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

<BR>

<!-- BEFORE AND AFTER TABLE -->
<table border="0" cellpadding="0" cellspacing="0" width="<%=fullWidth%>">
       <tr>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%=b4Col1Width%>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>                    
            <td><img src="/assets/unmanaged/images/s.gif" width="115" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% } %>            
            <td><img src="/assets/unmanaged/images/s.gif" width="115" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="tablehead">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>        
             <td class="tableheadTD1" colspan="12" align="left"><b>&nbsp;&nbsp;Account before change</b></td>
<% } else { %>
             <td class="tableheadTD1" colspan="8" align="left"><b>&nbsp;&nbsp;Account before change</b></td>
<% } %>              
        </tr>
        <tr class="tablesubhead">
             <td class="databorder"></td>
            <td align="right" valign="bottom">&nbsp;</td>
            <td class="dataheaddivider"></td>
            
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            
	<% if (theReport.getTotalEEBeforeAmount().doubleValue() != (double)0.0) { %>
            <td align="right" valign="bottom"><b>Employee assets<BR>($)</b></td>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><b>% of account</b></td>
	<% } else { %>
            <td align="right">&nbsp;</td>
            <td class="dataheaddivider"></td>
            <td align="right">&nbsp;</td>
	<% } %>
            <td class="dataheaddivider"></td>
<% } %>                 
            
	<% if (theReport.getTotalERBeforeAmount().doubleValue() != (double)0.0) { %>
            <td align="right" valign="bottom"><b>Employer assets<BR>($)</b></td>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><b>% of account</b></td>
	<% } else { %>
            <td align="right">&nbsp;</td>
            <td class="dataheaddivider"></td>
            <td align="right">&nbsp;</td>
	<% } %>

            <td class="databox"></td>
            <td class="databorder"></td>
        </tr>

        <% int rowIndex=1;%>
<c:forEach items="${theReport.beforeChange}" var="category" >
        <%  String outerRowClass;
               if (rowIndex % 2 == 0) {
                      outerRowClass="datacell1";
               } else {
                      outerRowClass="datacell2";
               }
               rowIndex++;
        %>
        <tr class="<%=outerRowClass%>">
            <td class="databorder"></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>                                
<td colspan="10"><b>${category.groupName}</b></td>
<% } else { %>
<td colspan="6"><b>${category.groupName}</b></td>
<% } %>
            <td class="databorder"></td>
        </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >
            <%  String innerRowClass;
                   if (rowIndex % 2 == 0) {
                          innerRowClass="datacell1";
                   } else {
                          innerRowClass="datacell2";
                   }
                   rowIndex++;
            %>
            <tr class="<%=innerRowClass%>">
                    <td class="databorder"></td>
<td>${fund.name}</td>
                    <td class="datadivider" width="1"></td>

<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %> 
		<% if (theReport.getTotalEEBeforeAmount().doubleValue() != (double)0.0) { %>
                    <td align="right"><render:number property="fund.employeeAmount"/></td>
                    <td class="datadivider" width="1"></td>
                    <td align="right"><render:number property="fund.employeePercentage"/>%</td>
		<% } else { %>
                    <td align="right">&nbsp;</td>
                    <td class="datadivider" width="1"></td>
                    <td align="right">&nbsp;</td>
		<% } %>

                    <td class="datadivider" width="1"></td>
<% } %>

		<% if (theReport.getTotalERBeforeAmount().doubleValue() != (double)0.0) { %>
                    <td align="right"><render:number property="fund.employerAmount"/></td>
                    <td class="datadivider" width="1"></td>
                    <td align="right"><render:number property="fund.employerPercentage"/>%</td>
		<% } else { %>
                    <td align="right">&nbsp;</td>
                    <td class="datadivider" width="1"></td>
                    <td align="right">&nbsp;</td>
		<% } %>

                    <td class="databox"></td>
                    <td class="databorder"></td>
            </tr>
</c:forEach>
</c:forEach>
        <!-- before totals -->
        <% String totalRowClass;
               if (rowIndex % 2 == 0) {
                      totalRowClass="datacell1";
               } else {
                      totalRowClass="datacell2";
               }
        %>

            <tr class="<%=totalRowClass%>">
                    <td class="databorder"></td>
                    <td align="right"><b>Total amount:</b></td>
                    <td class="datadivider" width="1"></td>

<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %> 
		<% if (theReport.getTotalEEBeforeAmount().doubleValue() != (double)0.0) { %>
                    <td align="right"><b><render:number property="theReport.totalEEBeforeAmount"/></b></td>
                    <td class="datadivider" width="1"></td>
                    <td align="right"><b><render:number property="theReport.totalEEBeforePct"/>%</b></td>
		<% } else { %>
                    <td align="right">&nbsp;</td>
                    <td class="datadivider" width="1"></td>
                    <td align="right">&nbsp;</td>
		<% } %>

                    <td class="datadivider" width="1"></td>
<% } %>

		<% if (theReport.getTotalERBeforeAmount().doubleValue() != (double)0.0) { %>
                    <td align="right"><b><render:number property="theReport.totalERBeforeAmount"/></b></td>
                    <td class="datadivider" width="1"></td>
                    <td align="right"><b><render:number property="theReport.totalERBeforePct"/>%</b></td>
		<% } else { %>
                    <td align="right">&nbsp;</td>
                    <td class="datadivider" width="1"></td>
                    <td align="right">&nbsp;</td>
		<% } %>

                    <td class="databox"></td>
                    <td class="databorder"></td>
            </tr>


       <tr class="tablehead">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>        
             <td colspan="12" class="tableheadTD" align="left"><b>&nbsp;&nbsp;Account after change</b></td>
<% } else { %>
             <td colspan="8" class="tableheadTD" align="left"><b>&nbsp;&nbsp;Account after change</b></td>
<% } %>             
        </tr>
        <tr class="tablesubhead">
            <td class="databorder"></td>
            <td align="right" valign="bottom">&nbsp;</td>
            <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
           
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>                    
	<% if (theReport.getTotalEEAfterAmount().doubleValue() != (double)0.0) { %>
            <td align="right" valign="bottom"><b>Employee assets<BR>($)</b></td>
            <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="right" valign="bottom"><b>% of account</b></td>
	<% } else { %>
            <td align="right">&nbsp;</td>
            <td class="dataheaddivider"></td>
            <td align="right">&nbsp;</td>
	<% } %>

            <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% } %>

	<% if (theReport.getTotalERAfterAmount().doubleValue() != (double)0.0) { %>
            <td align="right" valign="bottom"><b>Employer assets<BR>($)</b></td>
            <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="right" valign="bottom"><b>% of account</b></td>
	<% } else { %>
            <td align="right">&nbsp;</td>
            <td class="dataheaddivider"></td>
            <td align="right">&nbsp;</td>
	<% } %>

            <td class="databox"></td>
            <td class="databorder"></td>
        </tr>
         <%String lastRowStyleUsed="";%>
        <% int afterRowIndex=1; %>
<c:forEach items="${theReport.afterChange}" var="category" >
        <%  String afterOuterRowClass;
               if (afterRowIndex % 2 == 0) {
                      afterOuterRowClass="datacell1";
               } else {
                     afterOuterRowClass="datacell2";
               }
               afterRowIndex++;
        %>
        <tr class="<%=afterOuterRowClass %>">
            <td class="databorder"></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>                    
<td colspan="10"><b>${category.groupName}</b></td>
<% } else { %>
<td colspan="6"><b>${category.groupName}</b></td>
<% } %>            
            <td class="databorder"></td>
        </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >
            <%  String afterInnerRowClass;
                   if (afterRowIndex % 2 == 0) {
                          afterInnerRowClass="datacell1";
                   } else {
                          afterInnerRowClass="datacell2";
                   }
                   afterRowIndex++;
            %>
            <tr class="<%=afterInnerRowClass%>">
                    <td class="databorder"></td>
<td height="18">${fund.name}</td>
                    <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>                                        
		<% if (theReport.getTotalEEAfterAmount().doubleValue() != (double)0.0) { %>
                    <td align="right"><render:number property="fund.employeeAmount"/></td>
                    <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td align="right"><render:number property="fund.employeePercentage"/>%</td>
		<% } else { %>
                    <td align="right">&nbsp;</td>
                    <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td align="right">&nbsp;</td>
		<% } %>
		    <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<% } %>		    

		<% if (theReport.getTotalERAfterAmount().doubleValue() != (double)0.0) { %>
                    <td align="right"><render:number property="fund.employerAmount"/></td>
                    <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td align="right"><render:number property="fund.employerPercentage"/>%</td>
		<% } else { %>
                    <td align="right">&nbsp;</td>
                    <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td align="right">&nbsp;</td>
		<% } %>

                    <td class="databox"></td>
                    <td class="databorder"></td>
            </tr>
</c:forEach>
</c:forEach>


        <!-- after totals -->
        <% String totalAfterRowClass="";
               if (afterRowIndex % 2 == 0) {
                      totalAfterRowClass="datacell1";
               } else {
                      totalAfterRowClass="datacell2";
               }
        %>

            <tr class="<%=totalAfterRowClass%>">
                    <td class="databorder"></td>
                    <td align="right"><b>Total amount:</b></td>
                    <td class="datadivider" width="1"></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>                                        
		<% if (theReport.getTotalEEAfterAmount().doubleValue() != (double)0.0) { %>
                    <td align="right"><b><render:number property="theReport.totalEEAfterAmount"/></b></td>
                    <td class="datadivider" width="1"></td>
                    <td align="right"><b><render:number property="theReport.totalEEAfterPct"/>%</b></td>
		<% } else { %>
                    <td align="right">&nbsp;</td>
                    <td class="datadivider" width="1"></td>
                    <td align="right">&nbsp;</td>
		<% } %>

                    <td class="datadivider" width="1"></td>
<% } %>                    

		<% if (theReport.getTotalERAfterAmount().doubleValue() != (double)0.0) { %>
                    <td align="right"><b><render:number property="theReport.totalERAfterAmount"/></b></td>
                    <td class="datadivider" width="1"></td>
                    <td align="right"><b><render:number property="theReport.totalERAfterPct"/>%</b></td>
		<% } else { %>
                    <td align="right">&nbsp;</td>
                    <td class="datadivider" width="1"></td>
                    <td align="right">&nbsp;</td>
		<% } %>

                    <td class="databox" width="4"></td>
                    <td class="databorder"></td>
            </tr>

            <tr class="<%=totalAfterRowClass%>">
	            <td height="4" class="databorder"></td>
    	        <td class="lastrow"></td>
        	    <td class="datadivider"></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>                            	    
	            <td class="lastrow"></td>
    	        <td class="datadivider"></td>
        	    <td class="lastrow"></td>
            	<td class="datadivider"></td>
<% } %>            	
	            <td class="lastrow"></td>
    	        <td class="datadivider"></td>
        	    <td class="lastrow"></td>
            	<td rowspan="2" colspan="2" width="5" class="lastrow" align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
	        </tr>
    	    <tr>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>                            	        	    
				<td class="databorder" colspan="10"></td>
<% } else { %>				
				<td class="databorder" colspan="6"></td>
<% } %>            	
			</tr>

</table>

<BR>

<% if (theReport.getRedemptionFees().doubleValue() != (double)0.0) { %>
   <b><content:contentBean contentId="<%=ContentConstants.MESSAGE_REDEMPTION_FEE_APPLED%>"
                                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
				  id="redemptionMsg"/>
			<content:getAttribute beanName="redemptionMsg" attribute="text">
				<content:param>
					-<render:number property="theReport.redemptionFees" type="c" sign="true"/>
				</content:param>
			</content:getAttribute>
	<BR><BR>
   </b>
<% } %>

<% if (theReport.getMva().doubleValue() != (double)0.0) { %>
   <b><content:contentBean contentId="<%=ContentConstants.MESSAGE_MVA_APPLIED%>"
		                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
				  id="mvaMsg"/>
			<content:getAttribute beanName="mvaMsg" attribute="text">
				<content:param>
					-<render:number property="theReport.mva" type="c" sign="true"/>
				</content:param>
			</content:getAttribute>
	<BR><BR>
   </b>
<% } %>

<c:if test="${empty param.printFriendly }" >
<script type="text/javascript" >
if (ie4||ns6){
document.write('<div id="showhidediv1" style="text-align:left;" class="utilityLinks"><A href="javascript:hideshow(\'div1\')" ><content:getAttribute beanName="showDetailMsg" attribute="text"/>&nbsp;&nbsp;&gt;&gt;</a><BR></div>')
document.write('<div id="div1" style="display:none;cursor:auto;">')
}
</script>
</c:if>

<% boolean showComments = ((theReport.getMva().doubleValue() != (double)0.0) || 
						   (theReport.getRedemptionFees().doubleValue() != (double)0.0));
   int numColumns=12;
   if (showComments) {
	numColumns=14;
   }
%>

<!-- REBALANCE DETAILS TABLE -->
<table border="0" cellpadding="0" cellspacing="0" width="<%=fullWidth%>">
<% if (showComments) { // Column defs incl. comments column %>
       <tr>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%=detailsCol1Width%>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="110" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<% } else {   // Column defs for no comments column %>
       <tr>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="<%=detailsCol1Width%>" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="140" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="110" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="110" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="120" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<% } %>
        <tr class="tablehead">
             <td class="tableheadTD1" colspan="<%=numColumns%>" align="left"><b>&nbsp;&nbsp;Rebalance details</b></td>
        </tr>
        <tr class="tablesubhead">
             <td class="databorder"></td>
            <td align="left" valign="bottom"><b>Investment option</b></td>
            <td class="dataheaddivider"></td>
            <td align="left" valign="bottom"><b>Money type</b></td>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><b>Amount ($)</b></td>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><b>Unit value</b></td>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><b>Number of<Br>units</b></td>
<% if (showComments) { %>
            <td class="dataheaddivider"></td>
            <td align="right" valign="bottom"><b>Comments</b></td>
<% } %>
            <td class="databox"></td>
            <td class="databorder"></td>
        </tr>



        <% int detailsRowIndex=1; %>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="category" >
        <%  String detailsRowClass;
               if (detailsRowIndex % 2 == 0) {
                      detailsRowClass="datacell1";
               } else {
                      detailsRowClass="datacell2";
               }
               detailsRowIndex++;
        %>
        <tr class="<%=detailsRowClass %>">
            <td class="databorder"></td>
<td colspan="<%=numColumns-2%>"><b>${category.groupName}</b></td>
            <td class="databorder"></td>
        </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >
            <%  String detailsInnerRowClass;
                   if (detailsRowIndex % 2 == 0) {
                          detailsInnerRowClass="datacell1";
                   } else {
                          detailsInnerRowClass="datacell2";
                   }
                   detailsRowIndex++;
            %>
            <tr class="<%=detailsInnerRowClass%>">
                    <td class="databorder"></td>
<td>${fund.name}</td>
                    <td class="datadivider" width="1"></td>
<td align="left">${fund.moneyTypeDescription}</td>
                    <td class="datadivider" width="1"></td>
                    <td align="right"><render:number property="fund.amount" type="c" sign="false"/></td>
                    <td class="datadivider" width="1"></td>

                    <td align="right">
                    <%
                    TransactionDetailsFund fund = (TransactionDetailsFund)pageContext.getAttribute("fund");
                    %>
					<% if (fund.displayUnitValue()) { %>
						<render:number property="fund.displayUnitValue" scale="2" sign="true" /><%
						// If has no units, means we have interest rate here
						if (!fund.displayNumberOfUnits()) { %>&#37;
					<%  }  	
					   } %>
					</td>
                    <td class="datadivider" width="1"></td>
                    <td align="right">
					<% if (fund.displayNumberOfUnits()) { %>
						<render:number property="fund.displayNumberOfUnits" scale="6" sign="false"/>
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
            <td class="lastrow"></td>
<% if (showComments) { %>
            <td class="datadivider"></td>
            <td class="lastrow"></td>
<% } %>
            <td rowspan="2" colspan="2" width="5" class="lastrow" align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
        </tr>
        <tr>
                  <td class="databorder" colspan="<%=numColumns-2%>"></td>
        </tr>

</table>

<c:if test="${empty param.printFriendly }" >
<script type="text/javascript" >
if (ie4||ns6){
   document.write('</div>')
}
</script>
</c:if>
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
