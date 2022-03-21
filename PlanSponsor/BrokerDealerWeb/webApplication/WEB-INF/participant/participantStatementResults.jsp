<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>
        
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>


<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page
	import="com.manulife.pension.bd.web.bob.participant.ParticipantStatementSearchForm"%>
<%@ page import="com.manulife.pension.service.statement.valueobject.ParticipantStatementDocumentVO" %>	
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.util.content.GenericException"%>
<%@ page import="com.manulife.pension.bd.web.BDErrorCodes"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementsReportData" %>


<%
ParticipantStatementSearchForm ParticipantStatementSearchForm=(ParticipantStatementSearchForm)session.getAttribute("ParticipantStatementSearchForm");
pageContext.setAttribute("ParticipantStatementSearchForm",ParticipantStatementSearchForm,PageContext.PAGE_SCOPE);
%>
			 
<c:set var="statementsList" value="${statementsList}" />



<c:set var="isReportCurrent" value="true" />

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>




<%
ParticipantStatementsReportData theReport = (ParticipantStatementsReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 





<input type="hidden" name="pdfCapped" /><%--  input - name="ParticipantStatementSearchForm" --%>


<content:contentBean contentId="<%=BDContentConstants.MESSAGE_PARTICIPANTS_VIEW_ALL%>"
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
					 id="viewAllParticipants" />

<content:contentBean contentId="<%=BDContentConstants.MESSAGE_PARTICIPANTS_NO_SEARCH_RESULTS%>"
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="noSearchResults" />

<content:contentBean contentId="<%=BDContentConstants.MESSAGE_SEARCH_FOR_PARTICIPANTS%>"
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
					 id="searchForParticipants" />

<content:contentBean contentId="${contentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT}"
        			 type="${contentConstants.TYPE_MISCELLANEOUS}"
			         id="csvIcon"/>

<content:contentBean contentId="${contentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT}"
        			 type="${contentConstants.TYPE_MISCELLANEOUS}"
			         id="pdfIcon"/>	

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<%
String ua = request.getHeader( "User-Agent" );
boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
request.setAttribute("isIE",isMSIE);
%>

<script type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript" >


var scrollingTable;
if (window.addEventListener) {
	window.addEventListener('load', init, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', init);
} else if (document.getElementById)
	window.onload=init;
	
function init() {
<c:if test="${not empty theReport.details}">
  	scrollingTable = document.getElementById("scrollingTable");
  	

  	if ( navigator.userAgent.toLowerCase().indexOf( 'gecko' ) != -1 ) {
     	scrollingTable.style.overflow='-moz-scrollbars-horizontal';
  	}
</c:if>
}

function tooltip(DefInvesValue)
	{
		if(DefInvesValue != null)
		{
			if(DefInvesValue == "TR")
			Tip('Instructions were provided by Trustee - Mapped');
			else if(DefInvesValue == "PR")
			Tip('Instructions prorated - participant instructions incomplete / incorrect');
			else if(DefInvesValue == "PA")
			Tip('Participant Provided');
			else if(DefInvesValue == "DF")
			Tip('Default investment option was used');
			else			
			UnTip();
			
		}
		else
		{
			UnTip();
		}
	}
	
var intervalId;
var documentId;
var utilities = {
    
    // Asynchronous request call to the server. 
    doAsyncRequest : function(actionPath, callbackFunction) {
        YAHOO.util.Connect.setForm(document.disclosureForm);
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

	function viewParticipantStatementPdf(documentIdValue){
		documentId = documentIdValue;
        	utilities.showWaitPanel();
    		utilities.doAsyncRequest("/do/bob/participant/participantStatementResults/?task=checkPdfReportGenerated&documentId="+documentId, callback_viewParticipantStatementPdf);
	}
	
	
	// Call back handler to Check whether Participant statement PDF Generation is complete.
	var callback_viewParticipantStatementPdf =    {
		success:  function(o) { 
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/bob/participant/participantStatementResults/?task=fetchStatementPdf&documentId="+documentId;
			}else{
				var reportURL = new URL("/do/bob/participant/participantStatementResults/?task=openErrorPdf");
				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
				window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
			}
			utilities.hideWaitPanel();
			},
	    cache : false,
	    failure : utilities.handleFailure
	};
</script>
<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />
<p><content:getAttribute id="layoutPageBean" attribute="body1" /></p>
<%--#Error- message box--%>
<report:formatMessages scope="request" />

<%-- Navigation link--%>
<navigation:contractReportsTab />
<div class="page_section_subheader controls"><%-- Start of quick filter form--%>
	<div class="page_module"><h3>Participant Statement for</h3><h6 style="padding-bottom: 3px; font-variant: normal; font-style: normal; margin: 0px; padding-left: 5px; padding-right: 0px; font-family: georgia, times, serif; color: #fff; font-size: 0.6em; padding-top: 17px; float: left; font-weight: normal;">&nbsp;<%=ParticipantStatementSearchForm.getSelectedLastName()%>, <%=ParticipantStatementSearchForm.getSelectedFirstName()%></h6></div>
</div>
<%-- Start of report table filter --%>
<bd:form method="post" 
		 cssClass="margin-top:0;"
		 action="/do/bob/participant/participantStatementResults/" modelAttribute="ParticipantStatementSearchForm" name="ParticipantStatementSearchForm">
	<div class="report_table">
		
		<div id="scrollingTable" style="width:918px;Overflow-x:auto;Overflow-y:hidden">
		<table class="report_table_content" id="participants_table">
			<thead>
				<tr>
					<th rowspan="1" class="val_str">
						&nbsp; 
					</th>
				    <th rowspan="1" class="val_str align_center">
						Start Date</th>
					<th rowspan="1" class="val_str align_center">
						End Date</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty statementsList}">
<c:forEach items="${statementsList}" var="theItem1" varStatus="theIndex1" >
<c:set var="indexValue" value="${theIndex1.index}"/>
	<%String temp = pageContext.getAttribute("indexValue").toString();
%>

						<%
								String rowClass = "spec";
						if (Integer.parseInt(temp)  % 2 == 1) {
										rowClass = "datacell1";
									} else {
										rowClass = "spec";
									}
						%>
						<tr class="<%=rowClass%>" style='width: 260px'>
							<td class="align_center">
									<a  href="" onclick="viewParticipantStatementPdf('${theItem1.documentId}'); return false;">
              							View
            						  </a>
							</td>
							
				            <!-- Division -->
						    
						    <td class="name align_center" nowrap="nowrap" style='width: 320px'>
						    	<render:date property="theItem1.startDate" patternOut="MMM-dd-yyyy"/> 
						    </td>
							
							<td class="date align_center" style='width: 320px'>
								<render:date property="theItem1.endDate" patternOut="MMM-dd-yyyy"/>
							</td>
						</tr>
</c:forEach>
				</c:if>
			</tbody>
		</table>
		</div>
	  <c:if test="${empty statementsList}">
		  <div class="message message_info">
			    <dl>
			    <dt>Information Message</dt>
			     <dd>1.&nbsp;There were no statements for the participant selected. Please try again.</dd>
			     </dd>
			    </dl>
		  </div>
</c:if> <%-- report table controls --%>
		<div class="button_regular" style="float: right;"><a id=back onClick="history.back()">back</a> </div>
	</div>
</bd:form>
<layout:pageFooter/>
