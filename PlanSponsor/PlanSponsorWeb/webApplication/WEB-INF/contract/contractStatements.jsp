<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.contract.ContractStatementsForm"%>
<%@ page import="com.manulife.pension.ps.web.contract.ContractStatements"%>
<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	ContractStatements contractStatements = (ContractStatements)session.getAttribute(Constants.CONTRACT_STATEMENTS_KEY);
	pageContext.setAttribute("contractStatements",contractStatements,PageContext.PAGE_SCOPE);
%>

<%-- This jsp includes the following CMA content --%>
<content:contentBean
    contentId="<%=ContentConstants.CONTRACT_STATEMENTS_TABLE_TITLE1%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contract_Statements_Table_Title1"/>

<content:contentBean
    contentId="<%=ContentConstants.CR_STATEMENTS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="cr_reports"/>

<content:contentBean
    contentId="<%=ContentConstants.SC_STATEMENTS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="sc_reports"/>
<content:contentBean
    contentId="<%=ContentConstants.AS_STATEMENTS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="as_reports"/>
<content:contentBean
    contentId="<%=ContentConstants.CL_STATEMENTS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="cl_reports"/>
<content:contentBean
    contentId="<%=ContentConstants.CH_STATEMENTS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="ch_reports"/> 
<content:contentBean
    contentId="<%=ContentConstants.OL_STATEMENTS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="ol_reports"/>
<content:contentBean
    contentId="<%=ContentConstants.LR_STATEMENTS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="lr_reports"/>
<content:contentBean 
	contentId="<%=ContentConstants.AUDIT_PACKAGE_CONTENT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="audit_content"/> 

<script type="text/javascript">

var secondaryWindowName = "2ndwindow";
var secondayWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";

/**
* opens a window to show the PDF for the selected report
* by sending a request to the statement PDF action
*
*/
function showPDF(theSelect) {
	var url = "/do/contract/statement/?statement=";
	var key = theSelect.options[theSelect.selectedIndex].value;
	if (key.length > 0) {
		url = url+key;
		PDFWindow(url);
	}
}

function showCSV(theSelect) {

	var reportURL = new URL("/do/contract/contractStatements/");
	var key = theSelect.options[theSelect.selectedIndex].value;
	var paramArray = key.split("_");
	var reportType =paramArray[0];
	var periodEnd = paramArray[1];
	reportURL.setParameter("reportType", reportType);
	reportURL.setParameter("periodEnd", periodEnd);
	location.href = reportURL.encodeURL();
}


var intervalId;

var utilities = {
    
    // Asynchronous request call to the server. 
    doAsyncRequest : function(actionPath, callbackFunction) {
        YAHOO.util.Connect.setForm(document.contractStatementsForm);
        // Make a request
        var request = YAHOO.util.Connect.asyncRequest('GET', actionPath, callbackFunction);
    },
    
    // Generic function to handle a failure in the server response  
    handleFailure : function(o){ 
        o.argument = null;
        utilities.hideWaitPanel();
		clearInterval(intervalId);
    },
    
    // Shows loading panel message
    showWaitPanel : function() {
        waitPanel = document.getElementById("wait_c");
        if (waitPanel == undefined || waitPanel.style.visibility != "visible") {
            loadingPanel = new YAHOO.widget.Panel("wait",  
                                {   width: "250px", 
                                    height:"50px",
                                    fixedcenter: true, 
                                    close: false, 
                                    draggable: false, 
                                    zindex:4,
                                    modal: true,
                                    visible: false,
                                    constraintoviewport: true
                                } 
                            );
            loadingPanel.setBody("<span style='padding-left:20px;float:right;padding-right:30px;padding-top:12px;'>One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
            loadingPanel.render(document.body);
            loadingPanel.show();
        }       
    },

    /**
    * hides the loading panel
    */
    hideWaitPanel: function () {	
        loadingPanel.hide();	
    }
        
 };

	
	function doDownloadCSV(theSelect){
		var message= "Downloading"
      
        
		    var reportURL = new URL("/do/contract/contractStatements/?action=csvDefault");
		   	var key = theSelect.options[theSelect.selectedIndex].value;
			var paramArray = key.split("_");
			var reportType =paramArray[0];
			var periodEnd = paramArray[1];
			reportURL.setParameter("reportType", reportType);
			reportURL.setParameter("periodEnd", periodEnd);
			
			
			utilities.showWaitPanel();
    		utilities.doAsyncRequest(reportURL.encodeURL(), callback_checkDownloadReport);
	}
	
	
	// Call back handler to Check whether ICC Report Generation is complete.
	var callback_checkDownloadReport =    {
		success:  function(o) { 
			if(o.responseText == 'csvGenerated'){
				window.location.href = "/do/contract/contractStatements/?action=csvDownload";
			}else{
				window.location.href = "/do/contract/contractStatements/?action=csvErrorReport";
				
			}
			utilities.hideWaitPanel();
			},
	    cache : false,
	    failure : utilities.handleFailure
	};

    
</script>

<% 
  String cellFormat ="dataCell1";
  int rowCount =2;
%>
<input type="hidden" name="reportType" /><%--  input - name="contractStatementsForm" --%>
<input type="hidden" name="periodEnd" /><%--  input - name="contractStatementsForm" --%>
<input type="hidden" name="apolloBatchRun" /><%--  input - name="contractStatementsForm" --%>
		<c:if test="${userProfile.currentContract.isDefinedBenefitContract() == false}">
			<c:if test="${userProfile.currentContract.isBundledGaIndicator() == false}">
<%-- Audit package Resources starts --%>
	<table class="tableAlign" style="float:right">
	
		<tr> 
  			<td valign="top" align="right">
			<table  border="0" cellspacing="0" cellpadding="0" width="100%">
				<tbody>
					<tr>
						<td width="1">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
						</td>
						<td width="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="170"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					<tr class="tablehead">
						<td class="tableheadTD1" colspan="4"><b>Audit package resources</b></td>
						<td class="whiteBoxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
					</tr>
					<tr class="whiteBox">
						<td class="whiteBoxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="2"></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="2"></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="2"></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="2"></td>
						<td class="whiteBoxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="2"></td>
					</tr>
					<tr class="whiteBox">
						<td class="whiteBoxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td width="170">
							<content:getAttribute beanName="audit_content" attribute="text"/>
						</td>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="whiteBoxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					<tr class="whiteBox">
						<td class="whiteBoxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan="2" rowspan="2" align="left" valign="bottom"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" border"0"=""></td>
					</tr>
					<tr class="whiteBox"><td height="1" class="whiteBoxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td><td class="whiteBoxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td><td class="whiteBoxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td></tr>
	</tbody>
</table> 		  					  
  			</td>
    	</tr>
  	</table>     
  </c:if>
  </c:if>
<%-- Audit package Resources ends--%>
	
<table width="98%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="5%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
        <img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
        <td width="94%" valign="top">

      <p>
        <content:errors scope="session" />
      </p>

		<c:if test="${not empty contractStatements }" >
  
        
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="100%"><img src="/assets/unmanaged/images/s.gif" width="153" height="1"></td>
                <td width="99%"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td><img src="/assets/unmanaged/images/s.gif" width="45" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td><img src="/assets/unmanaged/images/s.gif" width="40" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td><img src="/assets/unmanaged/images/s.gif" width="65" height="1"></td>
                <td width="5"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                <td width="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr class="tablehead">
                <td class="tableheadTD1" colspan="14">
                <table width="98%" border="0" cellspacing="0" cellpadding="0">
                    <tr>

                        <td width="85%" class="tableheadTD">
                        	<strong><content:getAttribute beanName="Contract_Statements_Table_Title1" attribute="text"/></strong></td>
                        <td width="11%" class="tableheadTDinfo">&nbsp;</td>
                     </tr>
                </table>
                </td>
            </tr>
            <c:if test="${empty reportServiceNotAvailable }" >
            <tr class="datacell2">
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td colspan="12" class="datacell2"> 
					<strong><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></strong><br>
                			<content:getAttribute beanName="layoutPageBean" attribute="body1"/></td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

            </tr>
            <tr class="datacell1"  height="30">
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td colspan="12" class="datacell2">
<c:if test="${not empty contractStatements.efOptions}">
                	<ps:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;">
                		<ps:option value=""><%=contractStatements.getStatementOption()%></ps:option>
                		<ps:options collection="efOptions" property="value" labelProperty="label"/>
                	</ps:select>
</c:if>
<c:if test="${empty contractStatements.efOptions}"><div class="highlightBold"><content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_CONTRACT_STATEMENTS%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messages"/><content:getAttribute beanName="messages" attribute="text"/></div></c:if>


				</td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr class="datacell1">
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td colspan="12" class="datacell1">
                <strong><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></strong><br>
                		<content:getAttribute beanName="layoutPageBean" attribute="body2"/></td>

                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr class="datacell1" height="30">
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td colspan="12" class="datacell1">
<c:if test="${not empty contractStatements.paOptions}">
                	<ps:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;">
                		<ps:option value=""><%=contractStatements.getReportOption()%></ps:option>
                		<ps:options collection="paOptions" property="value" labelProperty="label"/>
                	</ps:select>
</c:if>
<c:if test="${empty contractStatements.paOptions}"><div class="highlightBold"><content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_CONTRACT_STATEMENTS%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messages"/><content:getAttribute beanName="messages" attribute="text"/></div></c:if>


                 </td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
<c:if test="${userProfile.allowedScheduleA == true}" >
<%
		rowCount = rowCount+1; 
		if((rowCount%2)>0)
			cellFormat ="datacell2";
		else
			cellFormat ="datacell1";	

%>                
	            <tr>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="<%=cellFormat %>" >
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	                <strong><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></strong><br>
	                		<content:getAttribute beanName="layoutPageBean" attribute="body3"/></td>
	               	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="datacell1"  height="30">
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
<c:if test="${not empty contractStatements.saOptions}">
	                	<ps:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;"> 
	                		<ps:option value=""><%=contractStatements.getReportOption()%></ps:option>
	                	    <ps:options collection="saOptions" property="value" labelProperty="label"/>
	                	</ps:select>
</c:if>
<c:if test="${empty contractStatements.saOptions}"><div class="highlightBold"><content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_CONTRACT_STATEMENTS%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messages"/><content:getAttribute beanName="messages" attribute="text"/></div></c:if></td>


	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            	</tr>
</c:if>
            	
            	
            	<%--  Fee Disclosure ------ Schedule C starts --%>
    
<c:if test="${not empty contractStatements.scOptions}">
<c:if test="${userProfile.allowedScheduleA ==true}" >
<%
		rowCount = rowCount+1; 
		if((rowCount%2)>0)
			cellFormat ="datacell2";
		else
			cellFormat ="datacell1";	

%>                
	            <tr>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="<%=cellFormat %>" >
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	               		<content:getAttribute beanName="sc_reports" attribute="text"/>
	               	</td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="datacell1"  height="30">
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	                	<ps:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;"> 
	                		<ps:option value=""><%=contractStatements.getReportOption()%></ps:option>
	                	    <ps:options collection="scOptions" property="value" labelProperty="label"/>
	                	</ps:select>
	                </td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            	</tr>
</c:if>
    
</c:if>
    <%-- Fee Disclosure ------ Schedule C ends  --%>
            	
<%--  AOW ------ Audit Summary starts --%>    
<c:if test="${not empty contractStatements.asOptions}">
<c:if test="${userProfile.allowedScheduleA ==true}" >
<%
		rowCount = rowCount+1; 
		if((rowCount%2)>0)
			cellFormat ="datacell2";
		else
			cellFormat ="datacell1";	

%>                
	            <tr>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="<%=cellFormat %>" >
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	               		<content:getAttribute beanName="as_reports" attribute="text"/>
	               	</td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="datacell1"  height="30">
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	                	<ps:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;"> 
	                		<ps:option value=""><%=contractStatements.getReportOption()%></ps:option>
	                	    <ps:options collection="asOptions" property="value" labelProperty="label"/>
	                	</ps:select>
	                </td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            	</tr>
</c:if>
</c:if>
<%-- AOW ------ Audit Summary ends  --%>
<%--  AOW ------ Audit Certification starts --%>    
<c:if test="${not empty contractStatements.clOptions}">
<c:if test="${userProfile.allowedScheduleA ==true}" >
<%
		rowCount = rowCount+1; 
		if((rowCount%2)>0)
			cellFormat ="datacell2";
		else
			cellFormat ="datacell1";	

%>                
	            <tr>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="<%=cellFormat %>" >
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	               		<content:getAttribute beanName="cl_reports" attribute="text"/>
	               	</td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="datacell1"  height="30">
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	                	<ps:select name="contractStatements" property="statementOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;"> 
	                		<ps:option value=""><%=contractStatements.getStatementOption()%></ps:option>
	                	    <ps:options collection="clOptions" property="value" labelProperty="label"/>
	                	</ps:select>
	                </td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            	</tr>
</c:if>
</c:if>
<%-- AOW ------ Audit Certification ends  --%>
</c:if>

<%--  ------ Contribution History Report starts --%>
<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >            	
<c:if test="${not empty contractStatements.chOptions}">
          	<%
		rowCount = rowCount+1; 
		if((rowCount%2)>0)
			cellFormat ="datacell2";
		else
			cellFormat ="datacell1"; 
          	%>
                 <tr>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="<%=cellFormat %>" >
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	                <content:getAttribute beanName="ch_reports" attribute="text"/>
			</td>
	               	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="datacell1"  height="30">
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
<c:if test="${contractStatementsForm.apolloBatchRun !=true}">
								<ps:select name="contractStatements" property="reportOption" onchange="javascript:doDownloadCSV(this)" onblur="this.selectedIndex=0;"> 
									<ps:option value=""><%=contractStatements.getReportOption()%></ps:option>
								    <ps:options collection="chOptions" property="value" labelProperty="label"/> 
					                	</ps:select> 
</c:if>
<c:if test="${contractStatementsForm.apolloBatchRun ==true}">
	                		 <div class="highlightBold"><content:contentBean contentId="<%=ContentConstants.CONTRIBUTION_HISTORY_APOLLO_BATCH_RUN%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="messages"/><content:getAttribute beanName="messages" attribute="text"/></div></c:if>
			</td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            	    </tr>
</c:if>
</ps:isNotJhtc>
<%--  ------ Contribution History Report Ends --%>

<%--  ------ Outstanding Loans Starts  --%>
	<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
<c:if test="${not empty contractStatements.olOptions}">
          	<%
		rowCount = rowCount+1; 
		if((rowCount%2)>0)
			cellFormat ="datacell2";
		else
			cellFormat ="datacell1"; 
          	%>
                 <tr>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="<%=cellFormat %>" >
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	                <content:getAttribute beanName="ol_reports" attribute="text"/>
			</td>
	               	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="datacell1"  height="30">
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
				<ps:select name="contractStatements" property="reportOption" onchange="javascript:doDownloadCSV(this);" onblur="this.selectedIndex=0;"> 
					<ps:option value=""><%=contractStatements.getReportOption()%></ps:option>
				    <ps:options collection="olOptions" property="value" labelProperty="label"/> 
	                	</ps:select> 
			</td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            	    </tr>
</c:if>
              </ps:isNotJhtc>
<%--  ------ Outstanding Loans Ends  --%>
<%--  ------ Loan Replayments Starts  --%>
	<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
<c:if test="${not empty contractStatements.lrOptions}">
          	<%
		rowCount = rowCount+1; 
		if((rowCount%2)>0)
			cellFormat ="datacell2";
		else
			cellFormat ="datacell1"; 
          	%>
                 <tr>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="<%=cellFormat %>" >
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	                <content:getAttribute beanName="lr_reports" attribute="text"/>
			</td>
	               	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="datacell1"  height="30">
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
				<ps:select name="contractStatements" property="reportOption" onchange="javascript:doDownloadCSV(this);" onblur="this.selectedIndex=0;"> 
					<ps:option value=""><%=contractStatements.getReportOption()%></ps:option>
				    <ps:options collection="lrOptions" property="value" labelProperty="label"/> 
	                	</ps:select> 
			</td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            	    </tr>
</c:if>
       
       </ps:isNotJhtc>
<%--  ------ Loan Repayment Ends  --%>   
<c:if test="${empty reportServiceNotAvailable }" >  
<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >   
<c:if test="${not empty contractStatements.crOptions}">
          	<%
		rowCount = rowCount+1; 
		if((rowCount%2)>0)
			cellFormat ="datacell2";
		else
			cellFormat ="datacell1"; 
          	%>
                 <tr>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="<%=cellFormat %>" >
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
	                <content:getAttribute beanName="cr_reports" attribute="text"/>
			</td>
	               	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="datacell1"  height="30">
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat %>">
				<ps:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;"> 
					<ps:option value=""><%=contractStatements.getReportOption()%></ps:option>
				    <ps:options collection="crOptions" property="value" labelProperty="label"/> 
	                	</ps:select> 
			</td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            	    </tr>
</c:if>
      </ps:isNotJhtc>
<%-- <% if (userProfile.getCurrentContract().isDefinedBenefitContract()) {  --%>
	<%-- <c:if test="${userProfile.getCurrentContract().isDefinedBenefitContract() == true}"> --%>
	<% if (userProfile.getCurrentContract().isDefinedBenefitContract()) { 
	
		rowCount = rowCount+1; 
		if((rowCount%2)>0)
			cellFormat ="datacell2";
		else
			cellFormat ="datacell1"; 
        %>
        	<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
	            <tr class="<%=cellFormat%>" >
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat%>">
	                <strong><content:getAttribute beanName="layoutPageBean" attribute="subHeader"/></strong><br>
	                		<content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></td>
	               	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr class="datacell1"  height="30">
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td colspan="12" class="<%=cellFormat%>">
<c:if test="${not empty contractStatements.bpOptions}">
			<ps:select name="contractStatements" property="reportOption" onchange="showPDF(this);" onblur="this.selectedIndex=0;"> 
				<ps:option value=""><%=contractStatements.getReportOption()%></ps:option>
			    <ps:options collection="bpOptions" property="value" labelProperty="label"/> 
	                	</ps:select> 
</c:if>
<c:if test="${empty contractStatements.bpOptions}"><div class="highlightBold"><content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_CONTRACT_STATEMENTS%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messages"/><content:getAttribute beanName="messages" attribute="text"/></div></c:if></td>


	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            	</tr>
            	</ps:isNotJhtc>
 <%  } %> 
<%-- </c:if> --%>
</c:if>
<% if ((rowCount%2)>0) { %>
        <ps:roundedCorner numberOfColumns="14" emptyRowColor="beige"/> 
<% } else { %>
        <ps:roundedCorner numberOfColumns="14" emptyRowColor="white"/>
<% } %>
                 	
	   
	   
   			<tr>
				<td><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
				<td colspan=12 >
					<br>
					<p><content:pageFooter beanName="layoutPageBean"/></p>
 					<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 					<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 				</td>
 				<td><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
 			</tr>

        </table>
</c:if>
        </td>
    </tr>
</table>
