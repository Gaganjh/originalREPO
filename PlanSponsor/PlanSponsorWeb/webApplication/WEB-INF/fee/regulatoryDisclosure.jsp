<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page
  import="com.manulife.pension.ps.web.fee.RegulatoryDisclosureForm"%>
  
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
  

<un:useConstants var="constants"
	className="com.manulife.pension.ps.web.Constants" />






<content:contentBean
  contentId="<%=ContentConstants.DISCLOSURE_TITLE_408B2%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="disclosure408B2" />
  
<content:contentBean
  contentId="<%=ContentConstants.REGULATORY_DISCLOSURE_ESTIMATED_COST%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="estimatedCost" />

<content:contentBean
  contentId="<%=ContentConstants.SUPPLEMENTAL_DISCLOSURE_DETAILS%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="supplementalDisclosureDetails" />

<content:contentBean
  contentId="<%=ContentConstants.REGULATORY_DISCLOSURE_INVESTMENT_CHARGES%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="investmentCharges" />
 
 
  <%-- Added  --%>
 <content:contentBean
  contentId="<%=ContentConstants.STABLE_VALUE_FUND_SUPPLEMENT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="stableValueFund" />
 <content:contentBean
      contentId="<%=ContentConstants.STABLE_VALUE_FUND_NEW_YORK_LIFE_ANCHOR_TEXT%>"
      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="stableValueFundNewYorkLifeAnchor" />
 <content:contentBean
      contentId="<%=ContentConstants.STABLE_VALUE_FUND_FEDERAL_CAPITAL_PRESERVATION_TEXT%>"
      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="stableValueFundFederalCapitalPreservation" />
      <content:contentBean
      contentId="<%=ContentConstants.STABLE_VALUE_FUND_RELIANCE_METLIFE_TEXT%>"
      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="stableValueFundRelianceMetLife" /> 
 <!-- RP and R1 fund suite discloser -->    
 <content:contentBean
  contentId="<%=ContentConstants.RP_and_R1_VALUE_FUND_SUPPLEMENT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="familySuiteRPandR1ValueFund" />
  
  <!-- LT fund suite discloser-->    
 <content:contentBean
  contentId="<%=ContentConstants.LT_FUND_SUPPLEMENT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="familySuiteLTValueFund" />
     
<%-- 404a5 --%>

<content:contentBean
  contentId="<%=ContentConstants.DISCLOSURE_404A5%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="disclosure404A5" /> 

<content:contentBean
  contentId="<%=ContentConstants.PLAN_AND_INVESTMENT_NOTICE%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="planAndInvestmentNotice" /> 
  
<content:contentBean
  contentId="<%=ContentConstants.INVESTMENT_COMPARATIVE_CHART_LINK%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="investmentComparativeChart" />

<content:contentBean
  contentId="<%=ContentConstants.ICC_YEAR_END_WARNING_MESSAGE_KEY%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="yearEndWarningMessage" />
  
<content:contentBean
  contentId="<%=ContentConstants.ICC_WARNING_MESSAGE_KEY%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningMessage" />
  
<content:contentBean
  contentId="<%=ContentConstants.ICC_LINK_NOTE%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="iccLinkNote" /> 
  
<content:contentBean
  contentId="<%=ContentConstants.PLAN_INFORMATION_NOTICE_ADDENDUM_TEMPLATE%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="ipiAddendumTemplate" />

<content:contentBean
  contentId="<%=ContentConstants.PLAN_INFORMATION_WARNING_MESSAGE%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="planInformationWarningMessage" /> 
  
<content:contentBean
  contentId="<%=ContentConstants.FUND_CHNAGE_PARTICIPANT_NOFIFICATION_TEMPLATE%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="participantNotificationTemplateLink" />
  
<content:contentBean
  contentId="<%=ContentConstants.SUPPLEMENTAL_INFORMATION_404A5%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="SupplementalInformation404A5" />
  
  <content:contentBean
  contentId="<%=ContentConstants.TRANSACTION_FEES_404A5_TEMPLATE%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="TransactionFees_404a5AddendumTemplate" />
  
  <content:contentBean
  contentId="<%=ContentConstants.DISCLOUSRE_MESSAGE_TEXT_FOR_IA%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="disclosure404a5TextForIA" />

<script type="text/javascript">

var intervalId;

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

	function doCheckPiNoticeGenerated(){
		var message;
        if (${disclosureForm.showIccCalendarYearMessage}) {
            	message = '<content:getAttribute beanName="yearEndWarningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
            }else{
     		message = '<content:getAttribute beanName="warningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
        }	
        var response = confirm(message);
        if (response == true) {
        	utilities.showWaitPanel();
    		utilities.doAsyncRequest("/do/planAndInvestmentNotice/?action=checkPdfReportGenerated", callback_checkPiNoticeGenerated);
        }
	}
	
	
	// Call back handler to Check whether ICC Report Generation is complete.
	var callback_checkPiNoticeGenerated =    {
		success:  function(o) { 
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/planAndInvestmentNotice/";
			}else{
				var reportURL = new URL("/do/planAndInvestmentNotice/?action=openErrorPdf");
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
        if (${disclosureForm.showIccCalendarYearMessage}) {
            	message = '<content:getAttribute beanName="yearEndWarningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
            }else{
     		message = '<content:getAttribute beanName="warningMessage" attribute="text" filter="false" escapeJavaScript="true"/>';
        }	
        var response = confirm(message);
        if (response == true) {
        	utilities.showWaitPanel();
    		utilities.doAsyncRequest("/do/iccReport/?action=checkPdfReportGenerated", callback_checkIccGenerated);
        }
	}
	
	
	// Call back handler to Check whether ICC Report Generation is complete.
	var callback_checkIccGenerated =    {
		success:  function(o) { 
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/iccReport/";
			}else{
				var reportURL = new URL("/do/iccReport/?action=openErrorPdf");
				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
	        	window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
			}
			utilities.hideWaitPanel();
			},
	    cache : false,
	    failure : utilities.handleFailure
	};

	
    
    function doOpenPIDocument() {
      var reportURL = new URL("/do/planInfo");
      var message = '<content:getAttribute beanName="planInformationWarningMessage" attribute="text" filter="true" escapeJavaScript="true"/>';
   	  var response = confirm(message);
      if (response == true) {
     	  PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=yes,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
          window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
     }
    }     
    
    function doDownload404a5Addendum() {
		document.forms['disclosureForm'].elements['download404a5AddendumTemplate'].value = 'true';
		document.forms['disclosureForm'].submit();
	}
   
</script>
<div id="regulatory_body">
<ps:form modelAttribute="contactsForm" method="GET" name="contactsForm" action="/do/fee/disclosure/">
<td width="30" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
<td>
<table width="730" border="0" cellspacing="0" cellpadding="0"> 
  <tr>
    <td width="510" ><img src="/assets/unmanaged/images/s.gif" width="510" height="1"></td>
    <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
    <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
    <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
  </tr>
  <tr>
     <td valign="top">
      <img src="/assets/unmanaged/images/s.gif" width="510" height="23"><br>
      <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
      <!--Retrieve from CMA -->
    <img
      src="<content:pageImage type="pageTitle" beanName="layoutPageBean" path="/assets/unmanaged/images/head_plan_details.gif"/>"
      alt="Regulatory Disclosures"> <br>
    <content:errors scope="request"/>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="2"><img src="/assets/unmanaged/images/s.gif"
          height="5"></td>
      </tr>
      <tr>
        <td width="15"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        <td valign="top" width="495">
        <br>
        <!--Layout/Intro1--> <content:pageIntro beanName="layoutPageBean" />
        <br>
        <content:getAttribute beanName="layoutPageBean"
          attribute="introduction2">

        </content:getAttribute> <br>
        <br>
       <c:if test="${disclosureForm.contractandProductRestrictionFlag}">
        	<content:getAttribute beanName="disclosure404a5TextForIA" attribute="text"/><br><br>
     	</c:if>
        
        <table width="100%" border="0">

          <tr>
            <td width="5"><img src="/assets/unmanaged/images/s.gif"
              width="5" height="1"></td>
            <td>
            <table width="710">
            	<c:if test="${ disclosureForm.investmentRelatedCostsPageAvailable eq true || disclosureForm.svfIndicator eq true}">
            		<c:set  scope="page" var="feeDiscAvaialable"  value="true" />
            	</c:if>
<c:if test="${disclosureForm.feeDisclosureAvaialable ==true}">
                <c:if test="${feeDiscAvaialable eq true}">
                <tr>
	                <td valign="top" width="500">
	                  <b><content:getAttribute beanName="disclosure408B2" attribute="text" /></b><br/><br/>
	                  <ul class="noindent">
	                    
<c:if test="${disclosureForm.investmentRelatedCostsPageAvailable ==true}">
		                    <li>
		                       <content:getAttribute beanName="investmentCharges" attribute="text"/>                  
		                    </li>
</c:if>
						<c:if test="${disclosureForm.svfIndicator}" >                      		
 					          <c:choose>
					            <c:when test="${disclosureForm.stableValueFundId eq 'MSV' || disclosureForm.stableValueFundId  eq 'NMY'}">
					            	<li>
					            	 <content:getAttribute beanName="stableValueFund" attribute="text"/>
					            	</li>
					            </c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${disclosureForm.stableValueFundId  eq 'CNY' || disclosureForm.stableValueFundId eq 'NYL'}">
											<li>
											<content:getAttribute beanName="stableValueFundNewYorkLifeAnchor" attribute="text" />
											</li>
										</c:when>
										<c:otherwise>
											<c:choose>
										<c:when test="${disclosureForm.stableValueFundId eq 'FCP' || disclosureForm.stableValueFundId eq 'NFC'}">
												<li>
												<content:getAttribute beanName="stableValueFundFederalCapitalPreservation" attribute="text" /> 
												</li>
											</c:when>
											<c:otherwise>
											<c:choose>
											<c:when test="${disclosureForm.stableValueFundId eq 'RMS' || disclosureForm.stableValueFundId eq 'NRA'}">
												<li>
												<content:getAttribute beanName="stableValueFundRelianceMetLife" attribute="text" /> 
												</li>
											</c:when>
											</c:choose>
											</c:otherwise>
										</c:choose>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
					          </c:choose>				            
               			  </c:if> 
               			  <c:if test="${disclosureForm.rPandR1Indicator}" >                      		
					            <li>
					            	<content:getAttribute beanName="familySuiteRPandR1ValueFund" attribute="text"/>
					            </li>
					      </c:if>
					      
					      <c:if test="${disclosureForm.lTIndicator}" >                      		
					            <li>
					            	<content:getAttribute beanName="familySuiteLTValueFund" attribute="text"/>
					            </li>
					      </c:if>
               		 </ul>
	                  <br/>
	                </td>
	               	<td width="200" align="right">
<c:if test="${disclosureForm.pinpointContract ==true}">
	               			<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer3" />
</c:if>
<c:if test="${disclosureForm.pinpointContract !=true}">
	               			<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" />
</c:if>
	               	</td>
               	</tr>
               	</c:if>
</c:if>
              <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
              <c:if test="${disclosureForm.show404a5Section eq true || userProfile.participantStatementAvaiable eq true}">
              <tr>
                <td valign="top" width="500">
                  <b><content:getAttribute beanName="disclosure404A5" attribute="text" /></b>
                  <br/>
                  <br/>
                  <ul class="noindent">
<c:if test="${disclosureForm.showPiNoticeLink ==true}">
                      <li>
                        <content:getAttribute
                          beanName="planAndInvestmentNotice" attribute="text">
                          <content:param>javascript:doCheckPiNoticeGenerated()</content:param>
                        </content:getAttribute>
                      </li>
</c:if>
<c:if test="${disclosureForm.showMissingIccContactMessage ==true}">
                      <li>
                        <content:getAttribute beanName="iccLinkNote" attribute="text" />
                      </li>
</c:if>
<c:if test="${disclosureForm.showIpiAddendumLink ==true}">
                      <li>
                        <content:getAttribute beanName="ipiAddendumTemplate" attribute="text">
                          <content:param>javascript:doOpenPIDocument()</content:param>
                        </content:getAttribute>
                      </li>
</c:if>
                    <!-- show404a5AddendumforJhWithdrawalProcessFeeLink-->
			 		<%--  <c:if test="${disclosureForm.show404a5AddendumTransactionProcessingProcessFeeLink==true}">
						 <form:hidden path="download404a5AddendumTemplate" value="false"/>
                        		<content:getAttribute beanName="TransactionFees_404a5AddendumTemplate" attribute="text"/>
					</c:if> --%>
<c:if test="${disclosureForm.showIccLink ==true}">
                      <li>
                        <content:getAttribute
                          beanName="investmentComparativeChart" attribute="text">
                          <content:param>javascript:doCheckIccGenerated()</content:param>
                        </content:getAttribute>
                      </li>
</c:if>
<c:if test="${disclosureForm.showParticipantFundChangeNoticeTemplate ==true}">
                      <li>
                        <content:getAttribute beanName="participantNotificationTemplateLink" attribute="text" />
                      </li>
</c:if>
                    <!-- participant Statement Fees Tool -->
<c:if test="${disclosureForm.showParticipantStatementFeesTool ==true}">
                      <ps:linkAccessible path="/do/fee/partStmtFeesTool/">
                        <li>
                          <a href="/do/fee/partStmtFeesTool/">Participant Statement Fees Report</a> (For internal users only)             		
                        </li>
                      </ps:linkAccessible>
</c:if>
<c:if test="${disclosureForm.showIpiHypotheticalToolLink ==true}">
                        <li>
                         <a href="/do/fee/hypotheticalTool/">Produce Participant Fee Change Notice</a> (for internal Pricing Department users only)  		
                        </li> 
</c:if>
<c:if test="${disclosureForm.show404a5NoticeInfoTool ==true}">
                        <li>
                         <a href="/do/view404a5NoticeInfo/">
                            <content:getAttribute beanName="SupplementalInformation404A5" attribute="text" /></a> 		
                        </li> 
</c:if>
                </ul>
                  
                <br />
                </td>
<c:if test="${disclosureForm.contractStatus !=DI}">
                	<td width="200" align="right">
                  		<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer2" />
                	</td>
</c:if>
              </tr>
              </c:if>
              </ps:isNotJhtc>              
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
    </table>
    <br />
    <br />
    </td>
  </tr>
  <tr>
   <td>
     <br>
     <p><content:pageFooter beanName="layoutPageBean"/></p>
     <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
     <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
   </td>
  </tr>
</table>
</td>
<td valign="top"><img src="/assets/unmanaged/images/s.gif"
  height="59">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
  class="box">
  <tr>
    <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
    <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
    <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
  </tr>
</table>
<img src="/assets/unmanaged/images/s.gif" height="20"></td>
</ps:form>
</div>
<div id="modalGlassPanel" class="modal_glass_panel"  style="display:none; position: absolute;"></div>
<div id="page_wrapper_footer" style="position: absolute;">&nbsp;</div>
