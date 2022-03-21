<%-- Imports --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
        
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractProfileVO"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>



<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>

<c:set var="contractInformationForm" value="${contractInformationForm}" />

<script type="text/javascript">
var intervalId;
var utilities = {
    
    // Asynchronous request call to the server. 
    doAsyncRequest : function(actionPath, callbackFunction) {
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
            loadingPanel.setBody("<span style='padding-left:10px;float:right;padding-right:10px;padding-top:12px;' >One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
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

 function doGoToContractDocuments() {
    	  document.getElementById('contractDocumentsLink').value='true';
    	  document.contractDocuments.submit();
      }

     
      /**
       * This function will be called when the user clicks on PDF link in 
       * PlanHighlights section.
       */
      function doOpenPlanHighlightsPDF() {
		var reportURL = new URL("/do/bob/contract/planHighlights/");
      	window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
      }      
  	

</script>

 <style type="text/css"> 
        <!--
         #planHighlights a { 
			text-decoration: underline; 
			font-weight: normal; 
			color: rgb(0, 44, 61); 
			font-size: 1em; outline-style: none;
		}        
        -->
		
		}	
		 
 </style>
 <bd:form action="/do/bob/contract/contractDocuments/" method="post" modelAttribute="contractDocuments" name="contractDocuments">
 <!--Error- message box-->
 <report:formatMessages scope="request"/> 
<%--  <form name="contractDocuments" method="POST" action="/do/bob/contract/contractDocuments/" > --%>
      <input id="contractDocumentsLink" type="hidden" name="contractDocuments" value="false"/>
<%-- </form> --%>
        
<%-- <form  action="do/bob/contract/contractInformation/" > --%>
<%-- <form> --%>
<!-- Placed the empty form tag to avoid the alignment issue ..-->
</form>

<%-- Beans used --%>
<%
  ContractInformationReportData theReport = (ContractInformationReportData)request.getAttribute(BDConstants.REPORT_BEAN);
  pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
  
  String TRUE=BDConstants.TRUE;
  pageContext.setAttribute("TRUE",TRUE,PageContext.PAGE_SCOPE);
  
  BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
  pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
  
  %>
	

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_CONTRACT_ACCESS_CODE_SECTION_TITLE%>"
        type="<%=BDContentConstants.TYPE_MESSAGE%>"
        id="contractAccessCodeSectionTitle"/>

<content:contentBean contentId="<%=BDContentConstants.MESSAGE_CONTRACT_ACCESS_CODE%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="message"/>
        
<c:set var="footNotes" value="${layoutBean.layoutPageBean.footnotes}"/>        

<input type="hidden" name="pdfCapped" />

<!-- Page Title and Headers-->
<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />

<!--Navigation bar-->
<navigation:contractReportsTab />

<!--Report Title-->
<div class="page_section_subheader controls">
<h3><content:getAttribute id="layoutPageBean" attribute="body1Header" /></h3>
<c:if test="${empty isError}">
	<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
	<a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
</c:if>
</div>


<div id="page_section_container">
<div class="page_section">

<%--  Included to show contract contact section  --%>
<div class="page_module">
<jsp:include page="contractContactInformation.jsp" flush="true" />
</div>

<%--  Included to show contract assets and payroll section  --%>
<div class="page_module">
<jsp:include page="contractAssetsAndPayroll.jsp" flush="true" /> 
</div>
 
<%--  Included to show contract keys dates section  --%>
<div class="page_module">
<jsp:include page="contractKeyDates.jsp" flush="true" />
</div>

<%--  contract access code section  --%>
<div class="page_module">
<h4><content:getAttribute beanName="contractAccessCodeSectionTitle" attribute="text"/></h4>
<table class="overview_table">
	<tbody>
		<tr>
			<td colspan="2">
            <content:getAttribute beanName="message" attribute="text"/><br/>
			<br />
			<strong>Enrollment access number:</strong> 
${theReport.contractProfileVo.contractAccessCode}
            </td>
		</tr>
	</tbody>
</table>
<p>&nbsp;</p>
</div>

<%--  Included to show contract statement details section  --%>
<div class="page_module">
<jsp:include page="contractStatementDetails.jsp" flush="true" />
</div>

<%--  Included to show contract Plan Highlights Section  --%>
<c:set var="isPlanHighlightsCSFavailable" value="${contractInformationForm.isPlanHighlightsCSFavailable}"/>

<c:if test="${isPlanHighlightsCSFavailable == true}">

	<content:contentBean
		contentId="<%=BDContentConstants.PLAN_HIGHLIGHTS_SECTION_HEADER%>"
		type="<%=BDContentConstants.TYPE_MESSAGE%>" id="phSectionTitle" />
	<content:contentBean
		contentId="<%=BDContentConstants.PLAN_HIGHLIGHTS_TEXT%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="phText" />
	<content:contentBean
		contentId="<%=BDContentConstants.PLAN_HIGHLIGHTS_LINK%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="phLink" />
		
	<div id="planHighlights" class="page_module">
	<h4><content:getAttribute beanName="phSectionTitle"
		attribute="text" /></h4>
	<table class="overview_table">
		<tbody>
			<tr>
				<td><content:getAttribute beanName="phText" attribute="text" /></td>
			</tr>
			<tr>
				<td><content:getAttribute beanName="phLink" attribute="text">
					<content:param>doOpenPlanHighlightsPDF()</content:param>
				</content:getAttribute></td>
			</tr>
		</tbody>
	</table>
	</div>
</c:if>

</div>
<%-- End of page section. Start next section  --%>
<div class="page_section">

<%--  Included to show contract features section  --%>
<div class="page_module">
<jsp:include page="contractFeatures.jsp" flush="true" />
</div>

<%--  Included to show contract money types and sources section  --%>
<div class="page_module">
<jsp:include page="contractMoneyTypesAndSources.jsp" flush="true" /> 
</div>

</div>
<div class="clear_footer"></div>
</div>

<div class="footnotes">
    <div class="footer"><content:pageFooter beanName="layoutPageBean"/></div> 
    <br>    
    <c:if test="${not empty footNotes}"> 
    <dl>
      <dd><content:pageFootnotes beanName="layoutPageBean"/></dd> 
    </dl>
    </c:if>
    
<c:if test="${bobContext.contractProfile.contract.hasContractGatewayInd == TRUE}">
       <br>
		<content:contentBean
				contentId="<%=BDContentConstants.GUARANTEED_INCOME_FEATURE_FOOTNOTE%>"
				type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" id="giflFootnote" />
	    <p class="footnote"><content:getAttribute id="giflFootnote" attribute="text" /></p>
</c:if>
	<dl>
	<dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd>
	</dl>
	<div class="footnotes_footer"></div>
</div>
</bd:form>
