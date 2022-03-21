<%-- Imports --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        
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
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import=" com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<%@ page import="com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.EstimatedJhRecordKeepingCostSummary" %>
<%@ page import="com.manulife.pension.bd.web.bob.investment.InvestmentSelectionReportForm"%>

<%-- CMA contents constants --%>
<un:useConstants scope="request" var="contentConstants" className="com.manulife.pension.bd.web.content.BDContentConstants" />
<un:useConstants var="bdConstants" className="com.manulife.pension.bd.web.BDConstants" />
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

<% 
	BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%> 
<%
	InvestmentCostReportData theReport = (InvestmentCostReportData)request.getAttribute(BDConstants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
	
	EstimatedJhRecordKeepingCostSummary recordKeepingCostsummary = (EstimatedJhRecordKeepingCostSummary)request.getAttribute(BDConstants.ESTIMATED_JH_RECORDKEEPING_COST_SUMMARY);
	pageContext.setAttribute("recordKeepingCostsummary",recordKeepingCostsummary,PageContext.PAGE_SCOPE);
	
	String SITEMODE_USA=BDConstants.SITEMODE_USA;
	pageContext.setAttribute("SITEMODE_USA",SITEMODE_USA,PageContext.PAGE_SCOPE);
	String FUND_PACKAGE_RETAIL  =  BDConstants.FUND_PACKAGE_RETAIL;
	pageContext.setAttribute("FUND_PACKAGE_RETAIL",FUND_PACKAGE_RETAIL,PageContext.PAGE_SCOPE);
	String FUND_PACKAGE_HYBRID  =  BDConstants.FUND_PACKAGE_HYBRID; 
	pageContext.setAttribute("FUND_PACKAGE_HYBRID",FUND_PACKAGE_HYBRID,PageContext.PAGE_SCOPE);
	String FUND_PACKAGE_BROKER  =  BDConstants.FUND_PACKAGE_BROKER;
	pageContext.setAttribute("FUND_PACKAGE_BROKER",FUND_PACKAGE_BROKER,PageContext.PAGE_SCOPE);
	String FUND_PACKAGE_VENTURE =  BDConstants.FUND_PACKAGE_VENTURE;
	pageContext.setAttribute("FUND_PACKAGE_VENTURE",FUND_PACKAGE_VENTURE,PageContext.PAGE_SCOPE);
	String FUND_PACKAGE_MULTICLASS =  BDConstants.FUND_PACKAGE_MULTICLASS;
	pageContext.setAttribute("FUND_PACKAGE_MULTICLASS",FUND_PACKAGE_MULTICLASS,PageContext.PAGE_SCOPE);
	String SIG_PLUS_RATE_TYPE = BDConstants.SIGNATURE_PLUS;
	pageContext.setAttribute("SIG_PLUS_RATE_TYPE",SIG_PLUS_RATE_TYPE,PageContext.PAGE_SCOPE);
	String CY1_RATE_TYPE = BDConstants.CY1_RATE_TYPE;
	pageContext.setAttribute("CY1_RATE_TYPE",CY1_RATE_TYPE,PageContext.PAGE_SCOPE);
	String CY2_RATE_TYPE = BDConstants.CY2_RATE_TYPE;
	pageContext.setAttribute("CY2_RATE_TYPE",CY2_RATE_TYPE,PageContext.PAGE_SCOPE);


%>


<jsp:useBean id="investmentSelectionReportForm" scope="session" class="com.manulife.pension.bd.web.bob.investment.InvestmentSelectionReportForm"/>

<%
	pageContext.setAttribute("isMerrillLynch", investmentSelectionReportForm.isMerrillLynchContract());
%>
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
				                        
	
<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}" 
                                        contentId="${contentConstants.REGULATORY_RESTRICTED_FUNDS_TEXT}"
                                        beanName="restricedFundsText" />   
<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}" 
                                        contentId="${contentConstants.SVGIF_DISCLOSURE}"
                                        beanName="svgifDisclosure" />                                 
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
   .iccR1andRPText {
   font-family: Verdana, Geneva, sans-serif;
   color: #002c3d;
   font-size: 12px;
   outline-style: none;
   text-decoration: underline;
   }
   .iccLTText {
   font-family: Verdana, Geneva, sans-serif;
   color: #002c3d;
   font-size: 12px;
   outline-style: none;
   text-decoration: underline;
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
<!--Error- message box-->
<report:formatMessages scope="request"/> 
<div class="table_controls_footer"></div>
<!--end of global section-->
<!--Navigation bar-->
<navigation:contractReportsTab />
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
 
</c:if>
<c:set var="show408b2Section" value="${investmentSelectionReportForm.show408b2Section}"/>
<input type="hidden" name="pdfCapped" /><%--  input - name="investmentSelectionReportForm" --%>
<%--  Regulatory Disclosures section  --%>
<c:if test="${investmentSelectionReportForm.show408b2Section}">
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
      <content:contentBean
      contentId="<%=BDContentConstants.STABLE_VALUE_FUND_RELIANCE_METLIFE_TEXT%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="stableValueFundRelianceMetLife" />
      
     <!-- RP and R1 fund suite discloser -->  
     
     <content:contentBean
      contentId="<%=BDContentConstants.RP_and_R1_VALUE_FUND_SUPPLEMENT%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="familySuiteRPandR1ValueFund" />
      
       <!--LT fund suite discloser -->  
     
     <content:contentBean
      contentId="<%=BDContentConstants.LT_VALUE_FUND_SUPPLEMENT%>"
      type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="familySuiteLTValueFund" />  
  
    
   <%-- Suppress the page if errors are present --%>

 


   
   <div class="page_section_subheader controls">
      <h3>Updates to 408(b)(2) Disclosure Information</h3>
<c:if test="${investmentSelectionReportForm.investmentRelatedCostsPageAvailable ==true}">





      
       <bd:form method="POST" action="/do/bob/contract/regulatoryDisclosures/" 
         modelAttribute="investmentSelectionReportForm" name="investmentSelectionReportForm" cssClass="page_section_filter form">
<input type="hidden" name="selectedView"/>
         <p><label for="asOfDate">as of</label></p>
<c:if test="${investmentSelectionReportForm.selectedView !='selected'}">

            <render:date property="theReport.feeEffectiveDate"
               patternOut="<%=RenderConstants.LONG_MDY%>" />
</c:if>
<c:if test="${investmentSelectionReportForm.selectedView =='selected'}">

            <bd:select name="investmentSelectionReportForm"
               property="selectedAsOfDate" onchange="setFilterFromSelect(this);">
               <bd:dateOptions name="<%= BDConstants.REPORT_DATES_FEE_DISCLOSURE %>"
                  renderStyle="<%=RenderConstants.LONG_MDY %>" />
            </bd:select>
            <a class="buttonheader" href="javascript:doFilter();"><span>Search</span></a>
</c:if>
       </bd:form> 
      
      <c:if test="${empty requestScope.isError}">
         <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="
         <content:getAttribute beanName="pdfIcon" attribute="text"/>
         "> 
         <content:image contentfile="image" id="pdfIcon" />
         </a>
      </c:if>
</c:if>
   </div>
   <TABLE>
      <TBODY>
         <TR>
            <TD colSpan=2>
               <br>
               <div id="iccLink" >
                  <content:getAttribute beanName="rdLink" attribute="text" >
                     <content:param>javascript:doOpenFeeDisclosurePDF()</content:param>
                     <br />
                  </content:getAttribute>
               </div>
               <c:if test="${investmentSelectionReportForm.svfIndicator}" >
               		<div id="iccLink">
                		<a  href="javascript:PDFWindow('http://www.jhnavigator.com/com/jhrps/navigator/catalog/svcGetItemFile.cfm?itemVersionID=3038&externalID=14')" onMouseOver="self.status='Go to the PDF'; return true" ><content:getAttribute beanName="understanding408b2" attribute="text" /></a>
					</div>
 			        <c:choose>
			            <c:when test="${investmentSelectionReportForm.stableValueFundId eq 'MSV' || investmentSelectionReportForm.stableValueFundId eq 'NMY'}">
			            	 <div id="iccLink">
			            		<content:getAttribute beanName="stableValueFund" attribute="text" />
			            	 	<br />
                			 </div>
			            </c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${investmentSelectionReportForm.stableValueFundId eq 'CNY' || investmentSelectionReportForm.stableValueFundId eq 'NYL'}">
									<div class="iccSVFText">
										<content:getAttribute beanName="stableValueFundNewYorkLifeAnchor" attribute="text" />
									</div>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when
											test="${investmentSelectionReportForm.stableValueFundId eq 'FCP' || investmentSelectionReportForm.stableValueFundId eq 'NFC'}">
											<div class="iccSVFText"><content:getAttribute
												beanName="stableValueFundFederalCapitalPreservation"
												attribute="text" /></div>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when
													test="${investmentSelectionReportForm.stableValueFundId eq 'RMS' || investmentSelectionReportForm.stableValueFundId eq 'NRA'}">
													<div class="iccSVFText"><content:getAttribute
														beanName="stableValueFundRelianceMetLife" attribute="text" />
													</div>
												</c:when>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
			        </c:choose>
               </c:if>
               <c:if test="${investmentSelectionReportForm.rPandR1Indicator}">
					<div class="iccR1andRPText"><content:getAttribute
							beanName="familySuiteRPandR1ValueFund" attribute="text" /></div>
			   </c:if>
			   
			    <c:if test="${investmentSelectionReportForm.lTIndicator}">
					<div class="iccLTText"><content:getAttribute
							beanName="familySuiteLTValueFund" attribute="text" /></div>
			   </c:if>
			   
				</TD>
         </TR>
      </TBODY>
   </TABLE>
<c:if test="${investmentSelectionReportForm.investmentRelatedCostsPageAvailable ==true}">
   <div>
      <p><content:getAttribute beanName="investmentSelection" attribute="introduction2" /></p>
      <p>${recordKeepingCostsummary.introductionText}</p>
      <!--Asset Based Report Summary Information-->
      <DIV class=report_table>
         <TABLE class=report_table_content>
            <THEAD>
               <tr class="spec">
                  <td class="zero" colspan="7">
                     <div class="page_section_subsubheader">
                        <h4>${recordKeepingCostsummary.lableText}${recordKeepingCostsummary.totalAmount}%</h4>
                     </div>
                  </td>
               </tr>
               <c:if test="${recordKeepingCostsummary.preAlignmentIndicator eq false}">
                  <tr class="spec">
                     <td class="zero" style="background-color: #f5f4f0;" colspan="3" height ="45px"  width="49%" >
                        <p style="font-size: 13px"><b>${recordKeepingCostsummary.sectionTotalA.introductionText}</b></p>
                     </td>
                     <td class="zero" style="background-color: #f5f4f0;" width="2%" >
                     </td>
                     <td class="zero" style="background-color: #f5f4f0;"  colspan="3" width="49%">
                        <p style="font-size: 13px"><b>${recordKeepingCostsummary.sectionTotalB.introductionText}</b></p>
                     </td>
                  </tr>
                  <TR>
                     <TH class=val_str width="21%"><B>Description</B> </TH>
                     <TH class=val_str width="21%"><B>Method of Payment</B> </TH>
                     <TH class="pct align_center" width="7%"><B>Amount (%)</B> </TH>
                     <TH width="2px"> </TH>
                     <TH class=val_str width="21%"><B>Description</B> </TH>
                     <TH class=val_str width="21%"><B>Method of Payment</B> </TH>
                     <TH class="pct align_center" width="7%"><B>Amount (%)</B> </TH>
                  </TR>
            </THEAD>
            <TBODY>
            <TR class=spec style="border:0px;">
            <TD colspan = "3" valign="top" style="padding: 0px;border:0px;border-left: 1px #DEDED8 solid;">
            <TABLE >
            <c:forEach var="fee"
               items="${recordKeepingCostsummary.sectionTotalA.feeDetails}" varStatus="loop">
            <TR  style="padding: 0px;">
            <TD valign="top" class=val_str style="margin: 0px;border: 0px;" width="180px">${fee.description}</TD>
            <TD valign="top" class=val_str  style="margin: 0px;border: 0px;"  width="190px">${fee.methodOfPayment}</TD>
            <TD valign="bottom" class=pct  style="margin: 0px;border: 0px;" width="47px" align ="right">${fee.amount}</TD>
            </TR>
            </c:forEach>						
            </TABLE>
            </TD>
            <TD width="2px"> </TD>
            <TD colspan = "3" valign="top" style="padding: 0px;border-right: 1px #DEDED8 solid;"  >
            <TABLE>
            <c:forEach var="fee"
               items="${recordKeepingCostsummary.sectionTotalB.feeDetails}" varStatus="loop">
            <TR  style="padding: 0px;border:0px;">
            <TD valign="top" class=val_str  style="margin: 0px;border: 0px;"  width="180px">${fee.description}</TD>
            <TD valign="top" class=val_str  style="margin: 0px;border: 0px;"  width="190px">${fee.methodOfPayment}</TD>
            <TD valign="bottom"class=pct  style="margin: 0px;border: 0px;" width="50px">${fee.amount}</TD>
            </TR>
            </c:forEach>									
            </TABLE>
            </TD>
            </TR>
            </c:if>	
            <tr class="spec">
            <td class="zero"  colspan="2">
            <div class="page_section_subsubheader">
            <h4 style="font-size: 12px"><div style="font-size:18px; float:left; margin-right: 10px; color:#000000;">A:</div> ${recordKeepingCostsummary.sectionTotalA.lableText}</h4>
            </div>
            </td>
            <td class="zero" >
            <div class="page_section_subsubheader align_right">
            <h4 style="font-size: 12px"><b>${recordKeepingCostsummary.sectionTotalA.totalAmount}</b></h4>
            </div>
            </td>
            <td class="zero" >
            <div class="page_section_subsubheader align_right">
            <h4 style="font-size: 12px"> </h4>
            </div>
            </td>
            <td class="zero" colspan="2">
            <div class="page_section_subsubheader">
            <h4 style="font-size: 12px"><div style="font-size:18px; float:left; margin-right: 10px; color:#000000;">B:</div> ${recordKeepingCostsummary.sectionTotalB.lableText}</h4>
            </div>
            </td>
            <td class="zero">
            <div class="page_section_subsubheader align_right">
            <h4 style="font-size: 12px"><b>${recordKeepingCostsummary.sectionTotalB.totalAmount}</b></h4>
            </div>
            </td>
            </tr>
            </THEAD>
            <TBODY>
               <c:if test="${recordKeepingCostsummary.preAlignmentIndicator eq false}">
                  <c:forEach var="fee"
                     items="${recordKeepingCostsummary.revenueAddendumDetailsSubSection.feeDetails}" varStatus="loop">
                     <TR class=spec style="border:0px;">
                        <TD colspan = "3" valign="top" style="padding: 0px;border:0px;border-left: 1px #DEDED8 solid;">
                        </TD>
                        <TD width="2px"> </TD>
                        <TD colspan = "3" valign="top" style="padding: 0px;border-right: 1px #DEDED8 solid;"  >
                           <TABLE>
                              <TR  style="padding: 0px;border:0px;">
                                 <TD valign="top" class=val_str  style="margin: 0px;border: 0px;"  width="390px">${fee.description}</TD>
                                 <TD valign="bottom"class=pct  style="margin: 0px;border: 0px;" width="50px">${fee.amount}</TD>
                              </TR>
                           </TABLE>
                        </TD>
                     </TR>
                  </c:forEach>
                  <TR class=spec style="border:0px;">
                     <TD colspan = "3" valign="top" style="padding: 0px;border:0px;border-left: 1px #DEDED8 solid;">
                     </TD>
                     <TD width="2px"> </TD>
                     <TD colspan = "3" valign="top" style="padding: 0px;border-right: 1px #DEDED8 solid;"  >
                        <TABLE>
                           <TR  style="padding: 0px;border:0px;">
                              <TD valign="top" class=val_str  style="margin: 0px;border: 0px;"  width="390px">${recordKeepingCostsummary.revenueAddendumDetailsSubSection.lableText}</TD>
                              <TD valign="bottom"class=pct  style="margin: 0px;border: 0px;" width="50px">${recordKeepingCostsummary.revenueAddendumDetailsSubSection.amount}</TD>
                           </TR>
                        </TABLE>
                     </TD>
                  </TR>
               </c:if>
            </TBODY>
         </TABLE>
      </DIV>
      <!--End of Report Summary Information-->		
   </div>
   
   <!-- Class Zero Phase 2 Change -->
   <c:if test="${recordKeepingCostsummary.preClassZeroPhaseTwoInd eq false and recordKeepingCostsummary.dollarFeesDetailsAvailable eq true}">
   <div>
      
      <!--Dollar Based Based Report Summary Information-->
      <DIV class=report_table>
         <TABLE class=report_table_content>
            <THEAD>
               <tr class="spec">
                  <td class="zero" colspan="7">
                     <div class="page_section_subsubheader">
                        ${recordKeepingCostsummary.dollarBasedSection.introductionText}
                     </div>
                  </td>
               </tr>
                  <tr class="spec">
                     <td class="zero" style="background-color: #f5f4f0;" colspan="3" height ="45px"  width="100%" >
                        <p style="font-size: 13px"><b>${recordKeepingCostsummary.dollarBasedSection.lableText}</b></p>
                        </td>
                  </tr>
                  <TR>
                     <TH class=val_str width="49%" style="border-left: #deded8 1px solid"><B>Description</B> </TH>
                     <TH class=val_str width="41%"><B>Method of Payment</B> </TH>
                     <TH class="pct align_right" width="10%" style="border-right: #deded8 1px solid"><B>Amount ($)</B> </TH>
                  </TR>
            </THEAD>
            <TBODY>
            <c:forEach var="fee"
               items="${recordKeepingCostsummary.dollarBasedSection.feeDetails}" varStatus="loop">
            <TR  style="padding: 0px;">
            <TD valign="top" class=val_str style="margin: 0px;border: 0px; border-left: #deded8 1px solid" width="49%">${fee.description}</TD>
            <TD valign="top" class=val_str  style="margin: 0px;border: 0px;"  width="41%">${fee.methodOfPayment}</TD>
            <TD valign="bottom" class=pct  style="margin: 0px;border: 0px; border-right: #deded8 1px solid;" width="10%" align ="right">${fee.amount}${fee.feeFrequency}</TD>
            </TR>
            </c:forEach>						
            </TBODY>
         </TABLE>
      </DIV>
   </div>
   </c:if>
   <!-- Class Zero Phase 2 Change - Ends -->
   
   <div>
      <c:forEach items="${pageScope.theReport.investmentData}" var="fundGroup">
         <c:forEach items="${fundGroup.value}" var="fund" varStatus="status">
            <c:if test="${fund.redemptionFee gt 0 }">
               <c:set  scope="page" var="hasRedemptionFee"  value="true" />
            </c:if>
         </c:forEach>
      </c:forEach>
      <TABLE>
         <TBODY>
            <tr>
               <td  width="749px" ><strong>Number of Funds Selected: </strong> ${theReport.numberOfFundsSelected}</td>
               <c:if test="${theReport.contractClassName!=''}">
                  <td align="right" width="749px" ><strong>Class of Funds: </strong>${theReport.contractClassName}</td>
</c:if>
            </tr>
      </table>
   </div>
   <div>
      <TABLE>
         <TBODY>
            <tr>
               <td width="45%" valign="top" >
                  <strong>Shown</strong>:
<c:if test="${investmentSelectionReportForm.selectedView == 'selected'}">
                     Selected investment options<br>
<c:if test="${investmentSelectionReportForm.showAvailableOptions ==true}">
                        <a class=investment_opt href="javascript:doView('available');" onMouseOver="self.status='Go to the PDF'; return true" >View all available investment options</a>
</c:if>
</c:if>
<c:if test="${investmentSelectionReportForm.selectedView !='selected'}">
                     All available investment options<br>
                     <a class=investment_opt href="javascript:doView('selected');" onMouseOver="self.status='Go to the PDF'; return true">View selected investment options</a>
</c:if>
               </td>
               <td width="31%"align="right"valign="top" >
                  <br>
                  <c:if test="${hasRedemptionFee eq true}">
<c:if test="${bobContext.contractSiteLocation == SITEMODE_USA}">
<c:if test="${bobContext.contractProfile.contract.contractAllocated ==true}">
<c:if test="${bobContext.contractProfile.contract.nml ==false}">
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_RETAIL_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode ==FUND_PACKAGE_HYBRID}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRID_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_BROKER_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_VENTURE_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRID_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
<c:if test="${bobContext.contractProfile.contract.nml ==true}">
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_RETAILNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode ==FUND_PACKAGE_HYBRID}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_VENTURE_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
</c:if>
<c:if test="${bobContext.contractProfile.contract.contractAllocated !=true}">
                           <a class=investment_opt href="javascript:openPDF('<%=BDConstants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
                     <c:if test="${bobContext==BDConstants.SITEMODE_NY}">
<c:if test="${bobContext.contractProfile.contract.contractAllocated ==true}">
<c:if test="${bobContext.contractProfile.contract.nml ==false}">
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_RETAIL_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode ==FUND_PACKAGE_HYBRID}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRID_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_BROKER_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_VENTURE_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRID_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
<c:if test="${bobContext.contractProfile.contract.nml ==true}">
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_RETAILNML_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode ==FUND_PACKAGE_HYBRID}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_VENTURE_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${bobContext.contractProfile.contract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
                                 <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
</c:if>
                        <c:if test="${bobContext.contractProfile.contract.contractAllocated!=true}">
                           <a class=investment_opt href="javascript:PDFWindow('<%=BDConstants.EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL%>')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
                  </c:if>
               </td>
            </tr>
         </TBODY>
      </TABLE>
   </div>
   <div>
      <content:getAttribute beanName="reportDetailsSummaryContent" attribute="text" />
   </div>
   <div>
      <p><content:getAttribute beanName="feeWaiverDisclosureText"  attribute="text" /></p>
   </div>
      <div>
     <c:if test = "${isMerrillLynch}">
      <p><content:getAttribute beanName="restricedFundsText"  attribute="text" /></p>
     </c:if>
   </div>
   
   <div class="page_section_subheader controls">
      <%-- date filter and table heading section --%>
      <H3>Investment Information and John Hancock's Indirect Compensation</H3>
   </div>
   <DIV class=report_table>
      <TABLE class="report_table_content">
         <THEAD>
            <TR>
               <TH class="val_str" width="6%" rowspan="3" colspan="1">Fund Code</TH>
               <TH class="val_str" width="33%" rowspan="3" colspan="1">Fund Name</TH>
               <TH class="val_str align_center" width="11%" rowspan="1" colspan="1">Investment Services</TH>
               <TH class="val_str align_center" width="40%" rowspan="1" colspan="5" style="background-color: #f5f4f0;">Plan Services</TH>
               <TH class="val_str align_center" width="11%" rowspan="2" colspan="1" style="vertical-align:bottom;">(5)</TH>
               <TH class="val_str align_center" width="11%" rowspan="2" colspan="1" style="vertical-align:bottom;"/>
            </TR>
            <TR>
               <TH class="val_str align_center" width="11%" rowspan="1" colspan="1" style=" border-left: #cac8c4 1px solid;">(1)</TH>
               <TH class="val_str align_center" width="11%" rowspan="1" colspan="1" style="background-color: #f5f4f0;">(2)</TH>
               <TH class="val_str align_center" width="3%" rowspan="2" colspan="1" style="background-color: #f5f4f0;">+</TH>
               <TH class="val_str align_center" width="11%" rowspan="1" colspan="1" style="background-color: #f5f4f0;">(3)</TH>
               <TH class="val_str align_center" width="3%" rowspan="2" colspan="1" style="background-color: #f5f4f0;">=</TH>
               <TH class="val_str align_center" width="11%" rowspan="1" colspan="1" style="background-color: #f5f4f0;">(4)</TH>
            </TR>
            <TR>
               <TH class="val_str align_center" width="11%" rowspan="1" colspan="1" style=" border-left: #cac8c4 1px solid; vertical-align:text-top;">Underlying Fund Net Cost (%)</TH>
               <TH class="val_str align_center" width="11%" rowspan="1" colspan="1" style="background-color: #f5f4f0; vertical-align:text-top;">Revenue From Underlying Fund (%) (12b-1, STA, Other)</TH>
               <TH class="val_str align_center" width="11%" rowspan="1" colspan="1" style="background-color: #f5f4f0; vertical-align:text-top;">Revenue From Sub-account (%)</TH>
               <TH class="val_str align_center" width="11%" rowspan="1" colspan="1" style="background-color: #f5f4f0; vertical-align:text-top;">Total Revenue Used Towards Plan Cost (%)</TH>
               <TH class="val_str align_center " width="11%" rowspan="1" colspan="1" style=" vertical-align:text-top;">Expense Ratio (%)</TH>
               <TH class="val_str align_center " width="11%" rowspan="1" colspan="1" style=" vertical-align:text-top;">Redemption Fee (%)<sup>N20</sup></TH>
            </TR>
         </THEAD>
         <TBODY>
            <c:forEach items="${pageScope.theReport.investmentData}" var="fundGroup">
               <tr class="spec">
                  <td class="zero" colspan="10">
                     <div class="page_section_subsubheader">
                        <h4>
                           <c:out value="${fundGroup.key.groupname}" />
                        </h4>
                     </div>
                  </td>
               </tr>
               <c:set  scope="page" var="maxCredit"  value="0.15" />
               <c:forEach items="${fundGroup.value}" var="fund" varStatus="status">
                  <c:set  scope="page" var="averageRowCount"  value="${status.index}" />
                  <c:choose>
                     <c:when test="${investmentSelectionReportForm.selectedView eq 'selected'}">
                        <c:if test="${fund.fundfeeChanged eq true or fund.redemptionFeeChanged eq true }">
                           <c:if test="${status.index % 2 eq 0 }">
                              <TR class="spec"  style="font-weight: bold;">
                           </c:if>
                           <c:if test="${status.index % 2 ne 0 }">
                           <TR class="spec" style="font-weight: bold;">
                           </c:if>
                        </c:if>
                        <c:if test="${fund.fundfeeChanged ne true and fund.redemptionFeeChanged ne true }">
                        <c:if test="${status.index % 2 eq 0 }">
                        <TR class="spec">
                        </c:if>
                        <c:if test="${status.index % 2 ne 0 }">
                        <TR class="spec" >
                        </c:if>
                        </c:if>
                     </c:when>
                     <c:otherwise>
                     <c:choose>
                     <c:when test="${fund.selectedFund eq true}">
                     <c:choose>
                     <c:when test="${fund.fundfeeChanged eq true or fund.redemptionFeeChanged eq true }">
                     <TR class="spec" style="font-weight: bold; background-color: #FFFFCC">
                     </c:when>
                     <c:otherwise>
                     <TR class="spec" style="background-color: #FFFFCC">
                     </c:otherwise>
                     </c:choose>
                     </c:when>
                     <c:otherwise>
                     <c:choose>
                     <c:when test="${fund.fundfeeChanged ne true and fund.redemptionFeeChanged ne true }">
                     <TR class="spec" style="font-weight: bold;">
                     </c:when>
                     <c:otherwise>
                     <TR class="spec">
                     </c:otherwise>
                     </c:choose>
                     </c:otherwise>
                     </c:choose>	
                     </c:otherwise>
                  </c:choose>
                  <TD class="val_str">${fund.fundId}</TD>
                  <TD class="val_str" style="color:#000000;">
                  <table>
                    <td class="fwiIndicator" >
	                       <c:if  test="${pageScope.theReport.isFeeWaiverFund(fund.fundId)}">
	                            <b>&#8226;</b>
	                       </c:if>
                       
                       <c:if  test="${pageScope.theReport.isFeeWaiverFund(fund.fundId) && pageScope.theReport.isRestrictedFund(fund.fundId)}">
	                            </br>
	                       </c:if>
	                       
	                   <c:if  test="${pageScope.theReport.isFeeWaiverFund(fund.fundId) ne true && pageScope.theReport.isRestrictedFund(fund.fundId) ne true}">
	                            &nbsp;&nbsp;
	                    </c:if>
	                       
                        <c:if  test="${isMerrillLynch && pageScope.theReport.isRestrictedFund(fund.fundId)}">
	                            <b><c:out value = "${bdConstants.MERRILL_RESRICTED_FUND_SYMBOL}"></c:out></b>
	                       </c:if>
                  </td>
                  <td class="fwiIndicator" >
                  	 <c:if test="${fund.rateType == SIG_PLUS_RATE_TYPE|| fund.rateType == CY1_RATE_TYPE || fund.rateType == CY2_RATE_TYPE}">
	                 	<a href="#fundsheet" onClick='FundWindow("<bd:fundLink fundIdProperty="fund.fundId" fundTypeProperty="fund.fundType" rateType ="fund.rateType" fundSeries ="${bobContext.contractProfile.contract.fundPackageSeriesCode}" productId ="${bobContext.contractProfile.contract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>	${fund.fundName} </a>
	                 </c:if>
	                 <c:if test="${fund.rateType != SIG_PLUS_RATE_TYPE && fund.rateType != CY1_RATE_TYPE && fund.rateType != CY2_RATE_TYPE}">	
	                 	<a href="#fundsheet" onClick='FundWindow("<bd:fundLink fundIdProperty="fund.fundId" fundTypeProperty="fund.fundType" rateType ="bobContext.contractProfile.contract.defaultClass" fundSeries ="${bobContext.contractProfile.contract.fundPackageSeriesCode}" productId ="${bobContext.contractProfile.contract.productId}" siteLocation="${bobContext.contractSiteLocation}" />")'>	${fund.fundName} </a>
	                 </c:if>	
	                  <c:if test="${not empty fund.footNoteMarkers  }">
	                  <sup>${fund.footNoteMarkers}</sup>
	                  </c:if>
	                  <c:if test="${fund.fundType eq 'GA'}">
	                  <c:set  scope="page" var="hasGarunteedFund"  value="true" />
	                  <sup>N21</sup>
	                  </c:if>
	                  <c:if test="${fund.fundId eq 'MSV' || fund.fundId eq 'NMY'}">
	                  <c:if test="${fund.fundId eq 'MSV'}">
	                  <c:set  scope="page" var="fundIdMsv"  value="true" />
	                  </c:if>
	                  <c:if test="${fund.fundId eq 'NMY'}">
	                  <c:set  scope="page" var="fundIdNmy"  value="true" />
	                  </c:if>														
	                  <sup>N19</sup>
	                  </c:if>	
                  </td>
                  </table>
                  </TD>
                  <TD class="pct" nowrap="">
                  <c:if test="${fund.fundType eq 'GA'}">
                  N/A
                  </c:if>
                  <c:if test="${fund.fundType ne 'GA'}">
                  <render:number property="fund.underlyingFundNetCost" defaultValue="0.00" pattern="0.00" />
                  </c:if>
                  </TD>
                  <TD class="pct" nowrap="">
                  <c:if test="${fund.fundType eq 'GA'}">
                  N/A
                  </c:if>
                  <c:if test="${fund.fundType ne 'GA'}">
                  <render:number property="fund.revenueFromUnderlyingFund" defaultValue="0.00" pattern="0.00" />
                  </c:if>
                  <BR>
                  </TD>
                  <TD class="val_str" style="text-align:center;">+</TD>
                  <TD class="pct" nowrap="">
                  <c:if test="${fund.fundType eq 'GA'}">
                  N/A
                  </c:if>
                  <c:if test="${fund.fundType ne 'GA'}">
                  <render:number property="fund.revenueFromSubAccount" defaultValue="0.00" pattern="0.00" />
                  </c:if>
                  <BR>
                  </TD>
                  <TD class="val_str" style="text-align:center;">=</TD>
                  <TD class="pct" nowrap="">
                  <c:if test="${fund.fundType eq 'GA'}">
                  N/A
                  </c:if>
                  <c:if test="${fund.fundType ne 'GA'}">
                  		<c:if test="${fund.rateType == CY1_RATE_TYPE||fund.rateType == CY2_RATE_TYPE}">
	                      <c:set  scope="page" var="maxCredit"  value="${fund.totalRevenueUsedTowardsPlanCosts}" />
	                    </c:if>
                  <render:number property="fund.totalRevenueUsedTowardsPlanCosts" defaultValue="0.00" pattern="0.00" />
                  </c:if>
                  <BR>
                  </TD>
                  <TD class="pct" nowrap="">
                  <c:if test="${fund.fundType eq 'GA'}">
                  N/A
                  </c:if>
                  <c:if test="${fund.fundType ne 'GA'}">
                  <render:number property="fund.expenseRatio" defaultValue="0.00" pattern="0.00" />
                  </c:if>
                  <BR>
                  </TD>
                  <TD class="pct" nowrap="">
                  <c:if test="${fund.fundType eq 'GA'}">
                  N/A
                  </c:if> 
                  <c:if test="${fund.fundType ne 'GA'}">
                  <render:number property="fund.redemptionFee" defaultValue="0.00" pattern="0.00" /> 
                  </c:if>
                  <BR>
                  </TD>
                  </tr>
               </c:forEach>
            </c:forEach>
            <TR class="spec">
               <TD class="val_str" style="color:#000000; text-align: right;" colspan="2"><strong>Averages:<sup>N15</sup></strong></TD>
               <TD class="pct" nowrap="" style="color:#000000;">
                  <strong>
                     <render:number property="theReport.averageUnderlyingFundNetCost" defaultValue="0.00" pattern="0.00" />
                  </strong>
               </TD>
               <TD class="pct" nowrap="" style="color:#000000;">
                  <strong>
                     <render:number property="theReport.averageRevenueFromUnderlyingFund" defaultValue="0.00" pattern="0.00" />
                  </strong>
               </TD>
               <TD class="val_str" style="text-align:center;">+</TD>
               <TD class="pct" nowrap="" style="color:#000000;">
                  <strong>
                     <render:number property="theReport.averageRevenueFromSubAccount" defaultValue="0.00" pattern="0.00" />
                  </strong>
               </TD>
               <TD class="val_str" style="text-align:center;">=</TD>
               <TD class="pct" nowrap="" style="color:#000000;">
                  <strong>
                     <render:number property="theReport.averageTotalRevenueUsedTowardsPlanCosts" defaultValue="0.00" pattern="0.00" />
                  </strong>
               </TD>
               <TD class="pct" nowrap="" style="color:#000000;">
                  <strong>
                     <render:number property="theReport.averageExpenseRatio" defaultValue="0.00" pattern="0.00" />
                  </strong>
               </TD>
               <TD class="pct" nowrap="" style="color:#000000;">
               </TD>
            </tr>
         </TBODY>
      </TABLE>
   </DIV>
   <DIV>
   <p>
	<c:if test="${investmentSelectionReportForm.svfFlag ==true}">
	                             <content:getAttribute attribute="text" beanName="svgifDisclosure" />
	</c:if>
</p>
                      <p/>
<c:if test="${investmentSelectionReportForm.classZero ==true}">
                             <content:getAttribute attribute="text" beanName="classZeroFooter" ><content:param> <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${maxCredit}" />% </content:param></content:getAttribute>
</c:if>
<c:if test="${investmentSelectionReportForm.classZero ==false}">
						     <content:getAttribute attribute="text" beanName="nonClassZeroFooter" /> 
</c:if>
					  <p/> 

            <!-- report Foot Notes Start -->
					<c:forEach items="${recordKeepingCostsummary.orderedFootNotes}" var="footNote">
							 <p>${footNote}</p>
							 <p/>
					</c:forEach>
					<!-- report Foot Notes Ends -->
	<p><bd:fundFootnotes symbols="symbolsArray" /></p>
	<p>
						<content:pageFootnotes beanName="layoutPageBean" /></p>					
   </DIV>
</c:if>
</c:if>

