<%-- Imports --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="quickreports"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@page import="com.manulife.pension.platform.web.util.DataUtility"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.util.content.GenericException"%>
<%@ page import="com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType"%>
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.InvestmentCostReportData"%>
<%@ page import="java.util.ArrayList"%>
<%@page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>

<%@ page import=" com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%-- CMA contents constants --%>
<un:useConstants scope="request" var="contentConstants" className="com.manulife.pension.bd.web.content.BDContentConstants" />
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>





<jsp:useBean id="investmentSelectionReportForm" scope="session" class="com.manulife.pension.bd.web.bob.investment.InvestmentSelectionReportForm"/>
<content:contentBean contentId="<%=BDContentConstants.ICC_YEAR_END_WARNING_MESSAGE_KEY%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="yearEndWarningMessage" />
	
<content:contentBean contentId="<%=BDContentConstants.ICC_WARNING_MESSAGE_KEY%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="warningMessage" />
     
<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
				                        contentId="${contentConstants.INVESTMENT_SELECTION_CONTENT_CL0}"
				                        beanName="classZeroFooter"/>
				                        
<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
				                        contentId="${contentConstants.INVESTMENT_SELECTION_CONTENT_NON_CL0}"
				                        beanName="nonClassZeroFooter"/>	
				                        
<content:contentBean type="${contentConstants.TYPE_FEE_DISCLSOURES}" 
                                        contentId="${contentConstants.REGULATORY_FEE_WAIVER_DISCLOSURE_TEXT}"
                                        beanName="feeWaiverDisclosureText" />

<content:contentBean contentId="<%=BDContentConstants.DISCLOUSRE_MESSAGE_TEXT_FOR_IA%>"
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"  id="disclosure404a5TextForIA" />            				                        
	
<script type="text/javascript">
   /**
    * Submits the form.
    */
   function doSubmit() {
       document.investmentSelectionReportForm.submit();
   }
   /**
    * Assigns selected view type and submits the form.
    */
   function doView(view) {
   document.investmentSelectionReportForm.selectedView.value = view;
   	doSubmit();
   }
   
   
   
   // 408(b)(2) disclosure pdf
   function doOpenFeeDisclosurePDF() {
   	var reportURL = new URL("/do/bob/feeDiscolsure/feeDisclosurePdfReport/");
   	window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
   }
   

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
     	
     	function doCheckPiNoticeGenerated(){
     		 var message;
      	  if (${investmentSelectionReportForm.showPreviousYearEndIccUnavailableMessage}) {
      		  message = '<content:getAttribute beanName="yearEndWarningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
      	  }else{
            	message = '<content:getAttribute beanName="warningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
      	   	  }
      	  var response = confirm(message);
   	      if (response == true) {
   	    	  utilities.showWaitPanel();
   	    		utilities.doAsyncRequest("/do/bob/investment/planAndInvestmentNotice/?action=checkPdfReportGenerated", callback_checkPiNoticeGenerated);
   	  }
   		       	
   	}
     	
     	// Call back handler to Check whether ICC Report Generation is complete.
     	var callback_checkPiNoticeGenerated =    {
     		success:  function(o) { 
     			if(o.responseText == 'pdfGenerated'){
     				window.location.href = "/do/bob/investment/planAndInvestmentNotice/";
     			}else{
     				var reportURL = new URL("/do/bob/investment/planAndInvestmentNotice/?action=openErrorPdf");
     				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
     	        	window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
     			}
     			utilities.hideWaitPanel();
     			},
     	    cache : false,
     	    failure : utilities.handleFailure
     	};
   
     	function doCheckIccGenerated(){
     		 var message;
      	  if (${investmentSelectionReportForm.showPreviousYearEndIccUnavailableMessage}) {
      		  message = '<content:getAttribute beanName="yearEndWarningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
      	  }else{
            	message = '<content:getAttribute beanName="warningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
      	   	  }
      	  var response = confirm(message);
   	      if (response == true) {
   	    	  utilities.showWaitPanel();
   	    		utilities.doAsyncRequest("/do/bob/investment/iccReport/?action=checkPdfReportGenerated", callback_checkIccGenerated);
   	  }
   		       	
   	}
     	
     	// Call back handler to Check whether ICC Report Generation is complete.
     	var callback_checkIccGenerated =    {
     		success:  function(o) { 
     			if(o.responseText == 'pdfGenerated'){
     				window.location.href = "/do/bob/investment/iccReport/";
     			}else{
     				var reportURL = new URL("/do/bob/investment/iccReport/?action=openErrorPdf");
     				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
     	        	window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
     			}
     			utilities.hideWaitPanel();
     			},
     	    cache : false,
     	    failure : utilities.handleFailure
     	};
   
       function doOpenPIDocument() {
         var reportURL = new URL("/do/bob/investment/ipiAddendum/");
         var message = '<content:getAttribute beanName="warningMessage" attribute="text" filter="true" escapeJavaScript="true"/>';
      	  var response = confirm(message);
         if (response == true) {
        	  PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=yes,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
             window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
        }
       }     
       
   
         // 408(b)(2) disclosure pdf
         function doOpenFeeDisclosurePDF() {
         	var reportURL = new URL("/do/bob/feeDiscolsure/feeDisclosurePdfReport/");
         	window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
     	  }
   
</script>
<style type="text/css">
   #iccLink a {
   text-decoration: underline;
   font-family: Verdana, Geneva, sans-serif;
   color: #002c3d;
   font-size: 12px;
   outline-style: none;
   }
   .iccSVFText {
   font-family: Verdana, Geneva, sans-serif;
   color: #002c3d;
   font-size: 12px;
   outline-style: none;
   }
   .selectedFund {
   background-color: #FFFFCC;
   }
   .report_table_content .hover {
   background-color: transparent;
   }
   .report_table_content .hovercell {
   background-color: transparent;
   cursor: pointer;
   }
   .investment_opt {
   color: #002c3d; 
   font-family:Verdana, Geneva, sans-serif; 
   font-size: 11px; 
   text-decoration: underline;
   }
</style>
<%-- This jsp includes the following CMA content --%>
<content:contentBean
   contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_REGULATORY_DISCLOSURES_INTRO_ONE%>"
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="rdTextOne" />
<content:contentBean
   contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_REGULATORY_DISCLOSURES_INTRO_TWO%>"
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="rdTextTwo" />
<content:contentBean
   contentId="<%=BDContentConstants.FIXED_FOOTNOTE_PBA%>"
   type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" id="footnotePBA" />
<content:contentBean
   contentId="<%=BDContentConstants.PARTICIPANT_STATUS_NOT_AVAILABLE%>"
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
   id="participantStatus" />
<content:contentBean
   contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="pdfIcon" />
<content:contentBean type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
   contentId="<%=BDContentConstants.REPORT_DETAILS_SUMMARY_CONTENT%>"
   beanName="reportDetailsSummaryContent" />
<!--start of global section-->
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>
<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
   <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
</p>
<P>
   <content:getAttribute beanName="rdTextOne" attribute="text" />
   <br />
   <br />
   <content:getAttribute beanName="rdTextTwo" attribute="text" />
   <br />
</P>
<div class="table_controls_footer"></div>
<c:if test="${investmentSelectionReportForm.contractandProductRestrictionFlag}">
 <p>
	<content:getAttribute beanName="disclosure404a5TextForIA" attribute="text" /><br/>
 </p>
</c:if>
<!--end of global section-->

<!--Navigation bar-->
<navigation:contractReportsTab />
    
   <%-- Suppress the page if errors are present --%>  
      
<c:set var="show408b2Section" value="${investmentSelectionReportForm.show408b2Section}"/>
<input type="hidden" name="pdfCapped" /><%--  input - name="investmentSelectionReportForm" --%>
<%--  Regulatory Disclosures section  --%>
<c:if test="${investmentSelectionReportForm.show408b2Section}">
     <div class="page_section_subheader controls">
      <%-- date filter and table heading section --%>
      <h3>Updates to 408(b)(2) Disclosure information</h3>
     </div>
     <TABLE>
      <TBODY>
        <TR>
            <TD colSpan=2>
				<br>
				   <div id="iccLink">
						<a href="javascript:doDisplayRegulatoryDisc();">Updates to 408(b)(2) Disclosure information</a>
					</div>
				<br>
			</TD>       	
       	</TR>
       </TBODY>
      </TABLE>
      
   <content:contentBean
      contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_REGULATORY_DISCLOSURES_TITLE%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="rdTtile" />
   <content:contentBean
      contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_REGULATORY_DISCLOSURES_LINK%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="rdLink" />
   <content:contentBean
      contentId="<%=BDContentConstants.STABLE_VALUE_FUND_SUPPLEMENT_LINK%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="stableValueFund" />
   <content:contentBean
      contentId="<%=BDContentConstants.TITLE_UNDERSTANDING_408B2_SECTION%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="understanding408b2" />
    <content:contentBean
      contentId="<%=BDContentConstants.INVESTMENT_SELECTION_CONTENT%>"
      type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>" id="investmentSelection" />
    <content:contentBean
      contentId="<%=BDContentConstants.STABLE_VALUE_FUND_NEW_YORK_LIFE_ANCHOR_TEXT%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="stableValueFundNewYorkLifeAnchor" />
    <content:contentBean
      contentId="<%=BDContentConstants.STABLE_VALUE_FUND_FEDERAL_CAPITAL_PRESERVATION_TEXT%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="stableValueFundFederalCapitalPreservation" /> 
</c:if>
   
<c:if test="${investmentSelectionReportForm.display404Section}">
   <content:contentBean
      contentId="<%=BDContentConstants.PLAN_AND_INVESTMENT_NOTICE %>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="planAndInvestmentNoticeLink" />
   <content:contentBean
      contentId="<%=BDContentConstants.INVESTMENT_COMPARATIVE_CHART_LINK_NOTE%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="iccNoteLink" />
   <content:contentBean
      contentId="<%=BDContentConstants.PLAN_INFORMATION_NOTICE_ADDENDUM_TEMPLATE %>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="ipiAddendumLink" />
   <content:contentBean
      contentId="<%=BDContentConstants.INVESTMENT_COMPARATIVE_CHART%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="iccTextLink" />
   <content:contentBean
      contentId="<%=BDContentConstants.PARTICIPANT_FUND_CHANGE_NOTICE %>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="participantFundChangeNoticeLink" />
   <content:contentBean
      contentId="<%=BDContentConstants.PLAN_AND_INVESTMENT_NOTICE_GUIDE %>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="guidePlanAndInvestmentNoticeLink" />
   <content:contentBean
      contentId="<%=BDContentConstants.INVESTMENT_COMPARATIVE_CHART_GUIDE%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="guideiccTextLink" />
      
      
      
   
   
   <div class="page_section_subheader controls">
      <%-- date filter and table heading section --%>
      <h3>404a-5 Disclosures</h3>
   </div>
   <TABLE>
      <TBODY>
         <TR>
            <TD colSpan=2>
               <br>
<c:if test="${investmentSelectionReportForm.showPlanAndInvestmentNotice ==true}">
                  <div id="iccLink">
                     <content:getAttribute beanName="planAndInvestmentNoticeLink" attribute="text" >
                        <content:param>javascript:doCheckPiNoticeGenerated()</content:param>
                     </content:getAttribute>
                  </div>
</c:if>
<c:if test="${investmentSelectionReportForm.showMissingIccContactMessage ==true}">
                  <div style="font-weight: normal;color: #002c3d;font-size: 1em;outline-style: none;">
                     <content:getAttribute beanName="iccNoteLink" attribute="text" />
                  </div>
</c:if>
<c:if test="${investmentSelectionReportForm.showIpiAddendum ==true}">
                  <div id="iccLink">
                     <content:getAttribute beanName="ipiAddendumLink" attribute="text" >
                        <content:param>javascript:doOpenPIDocument()</content:param>
                     </content:getAttribute>
                  </div>
</c:if>
<c:if test="${investmentSelectionReportForm.showIcc ==true}">
                  <div id="iccLink">
                     <content:getAttribute beanName="iccTextLink" attribute="text" >
                        <content:param>javascript:doCheckIccGenerated()</content:param>
                     </content:getAttribute>
                  </div>
</c:if>
<c:if test="${investmentSelectionReportForm.showParticipantFundChangeNotice ==true}">
                  <div id="iccLink">
                     <content:getAttribute beanName="participantFundChangeNoticeLink" attribute="text" />
                  </div>
</c:if>
<c:if test="${investmentSelectionReportForm.showPlanAndInvestmentNotice ==true}">
                  <div id="iccLink">
                     <content:getAttribute beanName="guidePlanAndInvestmentNoticeLink" attribute="text" />
                  </div>
</c:if>
<c:if test="${investmentSelectionReportForm.showIcc ==true}">
                  <div id="iccLink">
                     <content:getAttribute beanName="guideiccTextLink" attribute="text" />
                  </div>
</c:if>
            </TD>
         </TR>
      </TBODY>
   </TABLE>
   <p></p>
</c:if>


