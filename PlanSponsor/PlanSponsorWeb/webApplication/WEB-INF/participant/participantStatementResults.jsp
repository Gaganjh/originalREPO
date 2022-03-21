<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.ParticipantStatementSearchForm" %>
<%@ page import="com.manulife.pension.service.statement.valueobject.ParticipantStatementDocumentVO" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<un:useConstants var="psWebConstants" className="com.manulife.pension.ps.web.Constants"/>
<un:useConstants var="commonConstants" className="com.manulife.pension.platform.web.CommonConstants"/>

<c:set var="statementsList" value="${statementsList}" /> 

<%
ParticipantStatementSearchForm ParticipantStatementSearchForm=(ParticipantStatementSearchForm)request.getAttribute("ParticipantStatementSearchForm");
pageContext.setAttribute("ParticipantStatementSearchForm",ParticipantStatementSearchForm,PageContext.PAGE_SCOPE);
%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<script type="text/javascript">

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
    		utilities.doAsyncRequest("/do/participant/participantStatementResults?task=checkPdfReportGenerated&documentId="+documentId, callback_viewParticipantStatementPdf);
	}
	
	
	// Call back handler to Check whether Participant statement PDF Generation is complete.
	var callback_viewParticipantStatementPdf =    {
		success:  function(o) { 
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/participant/participantStatementResults?task=fetchStatementPdf&documentId="+documentId;
			}else{
				var reportURL = new URL("/do/participant/participantStatementResults?task=openErrorPdf");
				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
	        	window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
			}
			utilities.hideWaitPanel();
			},
	    cache : false,
	    failure : utilities.handleFailure
	};
</script>
<%
String tableWidth = ParticipantStatementSearchForm.getTableWidth();
String columnSpan = "8";
%>

<%
String ua = request.getHeader( "User-Agent" );
boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
request.setAttribute("isIE",isMSIE);
%>
<div id="Statement_body">
  <!-- Remove the extra column before the report -->
  <c:if test="${empty param.printFriendly }" >
    <td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    <td>
  </c:if>

  <c:if test="${not empty param.printFriendly }" >
    <td>
  </c:if>

      <p>
      <c:if test="${sessionScope.psErrors ne null}">
      <c:if test="${empty param.printFriendly }" >
<c:set var="errorsExist" value="true" scope="page" />
        <div id="errordivcs"><content:errors scope="session"/></div>
      </c:if>
      <c:if test="${not empty param.printFriendly }" >
          <%pageContext.removeAttribute("psErrors") ;%>
      </c:if>
      </c:if>
      </p>

    <!-- Beginning of Participant Summary report body -->
      <ps:form method="GET" modelAttribute="ParticipantStatementSearchForm" name="ParticipantStatementSearchForm" action="/do/participant/participantStatementResults" >
<input type="hidden" name="task" value="filter"/>
			
      <table width="<%=tableWidth%>" style="width:525px;Overflow-x:auto;" border="0" cellspacing="0" cellpadding="0">   
      	<!-- Set the column spacing for the report -->
        <tr>
          <td width="1"></td>
          <td width="220"></td>  	<!--View-->
          <td width="1"></td>
          <td width="220"></td>		<!--Start Date-->
          <td width="1"></td>
          <td width="222"></td> 	<!--End Date-->
          <td width="1"></td>
        </tr>

        <tr class="tablehead">
          <td class="tablehead" colspan="<%=columnSpan%>">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="tableheadTDinfo"><b>Participant Statement for&nbsp;<%=ParticipantStatementSearchForm.getSelectedLastName()%>, <%=ParticipantStatementSearchForm.getSelectedFirstName()%></b></td>
              </tr>
            </table>
          </td>
        </tr>
	</table>

    <div style="width:525px;Overflow-x:auto;Overflow-y:hidden">
		<table border="0" cellspacing="0" width="100%" cellpadding="0">         
        <!-- table details starts -->
        <tr class="tablesubhead">
          
        <!-- View -->
          <td rowspan="1" class="databorder" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<c:if test="${empty param.printFriendly }" >
          <td style='width: 175px'rowspan="1" valign="bottom">&nbsp;
		  </td>
		</c:if>
		<c:if test="${not empty param.printFriendly }" >
          <td rowspan="1">&nbsp;</td>
		</c:if>
		
        <!-- Start Date -->
          <td rowspan="1" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td style='width: 175px' rowspan="1" valign="bottom"><b>Start date</b></td>

        <!-- End Date -->
          <td rowspan="1" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td style='width: 175px' rowspan="1" valign="bottom"><b>End date</b></td>
          
        <!-- spacer -->
          <td rowspan="1" class="databox"></td>
          <td rowspan="1" class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		<%-- CL 110234 Begin--%>
		<tr class="tablesubhead">
			  <td class="dataheaddivider" colspan="1" valign="bottom"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		</tr>
		
		
<!-- Start of Details -->
<% boolean beigeBackgroundCls = false; // used to determine if the cell should use the background style
   boolean lastLineBkgrdClass = false; // used to determine if the last line should be beige or not
   boolean highlight = false;	// used to determine if the style class has to change
   boolean highlightBirthDate = false;	// used to determine if the style class has to change
   boolean highlightGateway = false;  // used to determine if the style class has to change
%>

<c:if test="${not empty statementsList}">
<c:forEach items="${statementsList}" var="theItem" varStatus="theIndex" >
<c:set var="Index" value="${theIndex.index}"/>
<%String temp = pageContext.getAttribute("Index").toString();
if (Integer.parseInt(temp) % 2 != 0) {
	 	beigeBackgroundCls = true;
	 	lastLineBkgrdClass = true; %>
        <tr class="datacell1">
<% } else {
		beigeBackgroundCls = false;
		lastLineBkgrdClass = false; %>
    	<tr class="datacell2">
<% } %>


        <!-- View -->
          <td class="databorder" width="1"></td>
          <td style='width: 175px' >
          <a  href="" onclick="viewParticipantStatementPdf('${theItem.documentId}'); return false;">
              <b>View</b>
              </a>
          </td>

        <!-- Start Date -->
          <td class="datadivider" width="1"></td>
          <td style='width: 175px' nowrap="nowrap"><render:date property="theItem.startDate" patternOut="MMM-dd-yyyy"/></td>
        
        <!-- End Date -->	
          <td class="datadivider" width="1"></td>
          <td style='width: 175px'><render:date property="theItem.endDate" patternOut="MMM-dd-yyyy"/></td>


        <!-- spacer -->
        <% if (beigeBackgroundCls) { %>
          <td class="dataheaddivider">
        <% } else { %>
          <td class="beigeborder">
        <% } %>
            <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
          </td>
          <td class="databorder" width="1"></td>
        </tr>
</c:forEach>
</c:if>

<!-- End of Details -->

<!-- Start of last line -->
<!-- let the last line have the same background colour as the previous line -->
<% if (!lastLineBkgrdClass) { %>
	<tr class="datacell1">
<% } else { %>
	<tr class="datacell2">
<% } %>
		<td class=databorder><img src="/assets/unmanaged/images/s.gif" width=1 height=1></td>
		<td style='width: 175px' ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></td>
		<td class=greyborder vAlign=top width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></td>
		<td style='width: 175px' ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></td>
		<td class=greyborder vAlign=top width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></td>
		<td style='width: 175px'><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></td>
		<td class=lastrow vAlign=bottom colSpan=1 align=right><IMG src="/assets/unmanaged/images/box_lr_corner.gif" width=5 height=5></td>
	</tr>

		<!-- End of Last line -->
        <tr><td class="databorder" colspan="6"></td></tr>   
		</table>
		<c:if test="${isIE eq true}">
		<table>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td></td></tr>
		</table>
		</c:if>
		<p>
           <input class="button100Lg" type=button value="back" onClick="history.back()">
         </p>
	</div>         
        <tr>
          <td colspan="<%=columnSpan%>">
			<table>
			<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td>         
            <br>
            <p><content:pageFooter beanName="layoutPageBean"/></p>
            <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
            <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
			</td>
			</tr>
			</table>
          </td>
        </tr>	
      </ps:form>
 
</div>
<div id="modalGlassPanel" class="modal_glass_panel"  style="display:none; position: absolute;"></div>
<div id="page_wrapper_footer" style="position: absolute;">&nbsp;</div>
