<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.fee.SupplementalDisclosureForm"%>
<%@ page import="com.manulife.pension.service.fee.util.Constants.EstimatedCostOfRecordKeeping"%>


<jsp:useBean id="supplementalDisclosureForm" scope="session" type="com.manulife.pension.ps.web.fee.SupplementalDisclosureForm"/>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<content:contentBean contentId="<%=ContentConstants.SUPPLEMENTAL_DISCLOSURE_DETAILS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="supplementalDisclosureDetails" />
<content:contentBean
  contentId="<%=ContentConstants.SUPPLEMENTAL_PINPOINT_NOTE%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="pinpointNote" />
<c:set var="dateStyle" value="MMMM dd, yyyy"/>
<c:set var="percentStyle" value="##0.00;(##0.00)"/>


  <script language="javascript" >

     function closeWindow() {
       window.close();
      }
      
       <!-- This makes it hard for people to copy data from this page -->
     var isNS = (navigator.appName == "Netscape") ? 1 : 0;
     if(navigator.appName == "Netscape") document.captureEvents(Event.MOUSEDOWN||Event.MOUSEUP);

     function mischandler() {
       return false;
     }

     document.onselect = mischandler;
     document.oncontextmenu = mischandler;
     document.onmousedown = mischandler;
     document.onmouseup = mischandler;
     document.onkeydown = mischandler;
    
 </script>
     


  <div class="supplementalDisclosure">
  
    <table border="0" cellspacing="0" cellpadding="0">
    <tr>
<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
        <td class="datacell1"><img src="/assets/unmanaged/images/JH_blue_resized.gif" border="0"></td>
</c:if>
<c:if test="${applicationScope.environment.siteLocation=='ny'}" >
        <td class="datacell1"><img src="/assets/unmanaged/images/JH_blue_resized_NY.gif" border="0"></td>
</c:if>
        <td class="datacell1" colspan="2">&nbsp;</td>
    </tr>
    </table>

    <br/><br/>

    <table border="0" cellspacing="0" cellpadding="0">
    <tr>
    <td class="datacell1">
<c:if test="${not empty layoutBean.param(titleImageFallback)}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.param(titleImageFallback)}" /> 
	    <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path='${supplementalDisclosure}'/>"
alt='${layoutBean.param(titleImageAltText)}'">
	    <br>
</c:if>
<c:if test="${empty layoutBean.param(titleImageFallback)}">

	   <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
	   <br>
</c:if>
    </td>
     <td class="datacell1" colspan="2">&nbsp;</td>
    </tr>
    </table>

    <p><content:pageIntro beanName="layoutPageBean" /></p>

    <p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
  
  </div>
  
  
  <table class="supplementalDisclosure">
    <tr>
      <td class="label">Effective Date</td>
      <td class="value">
<c:if test="${not empty supplementalDisclosureForm.effectiveDate}">
          <fmt:formatDate value="${supplementalDisclosureForm.effectiveDate}" type="DATE" pattern="${dateStyle}"/>
</c:if>
      </td>
    </tr>
<c:if test="${supplementalDisclosureForm.pinpointContract ==true}">
    <tr>
      <td class="value" colspan="2">
      	<content:getAttribute beanName="pinpointNote" attribute="text" />
	  </td>
    </tr>
</c:if>
    
<c:if test="${not empty supplementalDisclosureForm.fee(RecordKeepingChargesDeducted)}">
        <tr>
          <td class="label label-indent">&nbsp;</td>
          <td class="value">&nbsp;</td>
        </tr>
		<c:if test="${supplementalDisclosureForm.fee(RecordKeepingChargesDeducted) ge 0}">
            <tr>
              <td class="label ">Recordkeeping Charges - DD:</td>
              <td class="value value-indent">
              </td> 
            </tr>
		</c:if>
		<c:if test="${supplementalDisclosureForm.fee(RecordKeepingChargesDeducted) lt 0}">
            <tr>
              <td class="label ">Adjustment to Recordkeeping Charges - N/A:</td>
              <td class="value value-indent">
              </td>
            </tr>
		</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(BaseAssetChargeDeducted)}">
        <tr>
          <td class="label label-indent">Base Asset Charge</td>
          <td class="value">
             <render:number property = "supplementalDisclosureForm.fee(BaseAssetChargeDeducted)"
                                type="percent" pattern="${percentStyle}"/>%
          </td>
        </tr>
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(MarketValueEqualizerDeducted)}">
        <tr>
          <td class="label label-indent">Market Value Equalizer</td>
          <td class="value">
              <render:number property = "supplementalDisclosureForm.fee(MarketValueEqualizerDeducted)"
                                type="percent" pattern="${percentStyle}"/>%
          </td>
        </tr>
</c:if>

<c:if test="${supplementalDisclosureForm.pinpointContract ==false}">
<c:if test="${not empty supplementalDisclosureForm.fee(SsfAssetBasedDeducted)}">
            <tr>
              <td class="label label-indent">Recordkeeping portion of the Sales and Service Fee</td>
              <td class="value">
                  <render:number property = "supplementalDisclosureForm.fee(SsfAssetBasedDeducted)"
                                    type="percent" pattern="${percentStyle}"/>%
              </td>
            </tr>
</c:if>
</c:if>

<c:if test="${supplementalDisclosureForm.pinpointContract ==true}">
<c:if test="${not empty supplementalDisclosureForm.fee(JhWrappedBrokerCost)}">
            <tr>
              <td class="label label-indent">Recordkeeping portion of the Sales and Service Fee</td>
              <td class="value">
                  <render:number property = "supplementalDisclosureForm.fee(JhWrappedBrokerCost)"
                                    type="percent" pattern="${percentStyle}"/>%
              </td>
            </tr>
</c:if>
</c:if>
        
<c:if test="${not empty supplementalDisclosureForm.fee(AnnualizedPptFeeDeducted)}">
        <tr>
          <td class="label label-indent">Annualized Participant Fee</td>
          <td class="value">
              <render:number property = "supplementalDisclosureForm.fee(AnnualizedPptFeeDeducted)"
                                type="percent" pattern="${percentStyle}"/>%
          </td>
        </tr>
</c:if>
                 
<c:if test="${not empty supplementalDisclosureForm.fee(RecordKeepingChargesDeductedRKFee)}">
        <tr>
          <td class="label label-indent">Recordkeeping Fee</td>
          <td class="value">
              <render:number property = "supplementalDisclosureForm.fee(RecordKeepingChargesDeductedRKFee)"
                                type="percent" pattern="${percentStyle}"/>%
          </td>
        </tr>
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(JhWrappedPera)}">
        <tr>
          <td class="label label-indent">PERA</td>
          <td class="value">
              <render:number property = "supplementalDisclosureForm.fee(JhWrappedPera)"
                                type="percent" pattern="${percentStyle}"/>%
          </td>
        </tr>
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(CreditsToParticipants)}">
        <tr>
          <td class="label label-indent">Credits to Participants</td>
          <td class="value">
              <render:number property = "supplementalDisclosureForm.fee(CreditsToParticipants)"
                                type="percent" pattern="${percentStyle}"/>%
          </td>
        </tr>
</c:if>
        
        <tr>
          <td class="label label-indent ">Total Recordkeeping Charges - DD</td>
          <td class="value value-indent ">
             <render:number property = "supplementalDisclosureForm.fee(RecordKeepingChargesDeducted)"
                                type="percent" pattern="${percentStyle}"/>%
            
          </td>
        </tr>
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(RecordKeepingChargesBilled)}">
        <tr>
          <td class="label label-indent">&nbsp;</td>
          <td class="value">&nbsp;</td>
        </tr>

        <tr>
          <td class="label ">Recordkeeping Charges - BL:</td>
          <td class="value value-indent">
          </td>
        </tr>

<c:if test="${not empty supplementalDisclosureForm.fee(BaseAssetChargeBilled)}">
        <tr>
          <td class="label label-indent">Base Asset Charge</td>
          <td class="value">
             <render:number property = "supplementalDisclosureForm.fee(BaseAssetChargeBilled)"
                                type="percent" pattern="${percentStyle}"/>%
          </td>
        </tr>
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(MarketValueEqualizerBilled)}">
        <tr>
          <td class="label label-indent">Market Value Equalizer</td>
          <td class="value">
             <render:number property = "supplementalDisclosureForm.fee(MarketValueEqualizerBilled)"
                                type="percent" pattern="${percentStyle}"/>%
          </td>
        </tr>
</c:if>

<c:if test="${supplementalDisclosureForm.pinpointContract ==false}">
<c:if test="${not empty supplementalDisclosureForm.fee(SsfAssetBasedBilled)}">
            <tr>
              <td class="label label-indent">Recordkeeping portion of the Sales and Service Fee</td>
              <td class="value">
                 <render:number property = "supplementalDisclosureForm.fee(SsfAssetBasedBilled)"
                                    type="percent" pattern="${percentStyle}"/>%
              </td>
            </tr>
</c:if>
</c:if>

<c:if test="${supplementalDisclosureForm.pinpointContract ==true}">
<c:if test="${not empty supplementalDisclosureForm.fee(JhWrappedBrokerCost)}">
            <tr>
              <td class="label label-indent">Recordkeeping portion of the Sales and Service Fee</td>
              <td class="value">
                 <render:number property = "supplementalDisclosureForm.fee(JhWrappedBrokerCost)"
                                    type="percent" pattern="${percentStyle}"/>%
              </td>
            </tr>
</c:if>
</c:if>
        
<c:if test="${not empty supplementalDisclosureForm.fee(AnnualizedPptFeeBilled)}">
        <tr>
          <td class="label label-indent">Annualized Participant Fee</td>
          <td class="value">
              <render:number property = "supplementalDisclosureForm.fee(AnnualizedPptFeeBilled)"
                                type="percent" pattern="${percentStyle}"/>%
          </td>
        </tr>
</c:if>
        
        <tr>
          <td class="label label-indent ">Total Recordkeeping Charges - BL</td>
          <td class="value value-indent ">
          <render:number property = "supplementalDisclosureForm.fee(RecordKeepingChargesBilled)"
                         type="percent" pattern="${percentStyle}"/>% 
          </td>
        </tr>   
    
<c:if test="${not empty supplementalDisclosureForm.fee(RecordKeepingChargesBilledAdjustment)}">
<c:if test="${supplementalDisclosureForm.fee(RecordKeepingChargesBilledAdjustment) !=0}">
                <tr>
                  <td class="label ">Adjustment to Recordkeeping Charges - N/A:</td>
                  <td class="value value-indent">
                  </td>
                </tr>
                
<c:if test="${not empty supplementalDisclosureForm.fee(AdjustmentToAssetCharges)}">
                    <c:if test="${supplementalDisclosureForm.fee(AdjustmentToAssetCharges) lt 0}">           
                    <tr>
                      <td class="label label-indent">Adjustment to Asset Charge</td>
                      <td class="value">
                          <render:number property = "supplementalDisclosureForm.fee(AdjustmentToAssetCharges)"
                                            type="percent" pattern="${percentStyle}"/>%
                      </td>
                    </tr>
                    </c:if>
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(JhWrappedPera)}">
                <tr>
                  <td class="label label-indent">PERA</td>
                  <td class="value">
                      <render:number property = "supplementalDisclosureForm.fee(JhWrappedPera)"
                                        type="percent" pattern="${percentStyle}"/>%
                  </td>
                </tr>
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(CreditsToParticipants)}">
                <tr>
                  <td class="label label-indent">Credits to Participants</td>
                  <td class="value">
                      <render:number property = "supplementalDisclosureForm.fee(CreditsToParticipants)"
                                        type="percent" pattern="${percentStyle}"/>%
                  </td>
                </tr>
</c:if>
                
                <tr>
                  <td class="label label-indent ">Total Adjustment to Recordkeeping Charges</td>
                  <td class="value value-indent ">
                     <render:number property = "supplementalDisclosureForm.fee(RecordKeepingChargesBilledAdjustment)"
                                        type="percent" pattern="${percentStyle}"/>%
                    
                  </td>
                </tr>        
                
</c:if>
</c:if>
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(AnnualizedDtlStmntFee)}">
      <tr>
        <td class="label label">Annualized Detailed Participant Statement Fee</td>
        <td class="value value-indent">
            <render:number property = "supplementalDisclosureForm.fee(AnnualizedDtlStmntFee)"
                              type="percent" pattern="${percentStyle}"/>%
        </td>
      </tr>
</c:if>
   
<c:if test="${not empty supplementalDisclosureForm.fee(RecordKeepingFeeDeducted)}">
     <tr>
      <td class="label label-indent">&nbsp;</td>
      <td class="value">&nbsp;</td>
    </tr>    
    <tr>
      <td class="label label">Recordkeeping Fee</td>
      <td class="value value-indent">
          <render:number property = "supplementalDisclosureForm.fee(RecordKeepingFeeDeducted)"
                            type="percent" pattern="${percentStyle}"/>%
      </td>
    </tr>
</c:if>
     
<c:if test="${supplementalDisclosureForm.pinpointContract ==false}">
<c:if test="${not empty supplementalDisclosureForm.fee(PriceCreditIncluedtoJhServices)}">
        <tr>
          <td class="label label">Amount paid by John Hancock</td>
          <td class="value value-indent">
             (<render:number property = "supplementalDisclosureForm.fee(PriceCreditIncluedtoJhServices)"
                                type="percent" pattern="${percentStyle}"/>%)
          </td>
        </tr>
</c:if>
</c:if>

    <tr>
      <td class="label ">Total (A)</td>
      <td class="value value-indent " >
          <render:number property = "supplementalDisclosureForm.fee(EstimatedCostForJHServices)"
                            type="percent" pattern="${percentStyle}"/>%
      </td>
    </tr>  

    <tr>
      <td class="label label-indent">&nbsp;</td>
      <td class="value">&nbsp;</td>
    </tr>    

<c:if test="${not empty supplementalDisclosureForm.fee(AverageFundRevenue)}">
    <tr>
      <td class="label">Underlying fund Recordkeeping expense</td>
      <td class="value">
          <render:number property = "supplementalDisclosureForm.fee(AverageFundRevenue)"
                            type="percent" pattern="${percentStyle}"/>%
      </td>
    </tr>    
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(AverageAMC)}">
    <tr>
      <td class="label">Average AMC</td>
      <td class="value">
      <c:out value=""></c:out>
         <render:number property = "supplementalDisclosureForm.fee(AverageAMC)"
                            type="percent" pattern="${percentStyle}"/>%
      </td>
    </tr>    
</c:if>

<c:if test="${supplementalDisclosureForm.pinpointContract ==false}">
<c:if test="${not empty supplementalDisclosureForm.fee(RecordkeepingSSf)}">
        <tr>
          <td class="label">Recordkeeping portion of the Sales and Service Fee</td>
          <td class="value">
              <render:number property = "supplementalDisclosureForm.fee(RecordkeepingSSf)"
                                type="percent" pattern="${percentStyle}"/>%
          </td>
        </tr>    
</c:if>
</c:if>

<c:if test="${supplementalDisclosureForm.pinpointContract ==true}">
<c:if test="${not empty supplementalDisclosureForm.fee(JhSsfRecordkeepingCost)}">
            <c:if test="${supplementalDisclosureForm.fee(JhSsfRecordkeepingCost) gt 0}">            
            <tr>
              <td class="label">Recordkeeping portion of the Sales and Service Fee attributable to:</td>
            </tr>    
            </c:if>            
            <c:if test="${supplementalDisclosureForm.fee(JhSsfRecordkeepingCost) lt 0}">            
            <tr>
              <td class="label">Adjustments to JH Investment-related revenue attributable to:</td>
            </tr>    
			</c:if>
<c:if test="${not empty supplementalDisclosureForm.fee(JhRecordKeepingPortionCost)}">
            <tr>
              <td class="label label-indent">John Hancock</td>
              <td class="value">
                  <render:number property = "supplementalDisclosureForm.fee(JhRecordKeepingPortionCost)"
                                    type="percent" pattern="${percentStyle}"/>%
              </td>
            </tr>
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(JhEmbeddedBrokerCost)}">
            <tr>
              <td class="label label-indent">John Hancock for broker compensation</td>
              <td class="value">
                  <render:number property = "supplementalDisclosureForm.fee(JhEmbeddedBrokerCost)"
                                    type="percent" pattern="${percentStyle}"/>%
              </td>
            </tr>
</c:if>
</c:if>
</c:if>

<c:if test="${supplementalDisclosureForm.pinpointContract ==false}">
<c:if test="${not empty supplementalDisclosureForm.fee(PriceCreditIncluedtoInvestmentRelatedRevenue)}">
        <tr>
          <td class="label">Amount paid by John Hancock</td>
          <td class="value">
              (<render:number property = "supplementalDisclosureForm.fee(PriceCreditIncluedtoInvestmentRelatedRevenue)"
                                type="percent" pattern="${percentStyle}"/>%)
          </td>
        </tr>    
</c:if>
</c:if>

<c:if test="${not empty supplementalDisclosureForm.fee(InvestmentRelatedRevenueToJh)}">
    <tr>
      <td class="label ">Total (B)</td>
      <td class="value value-indent  " >
          <render:number property = "supplementalDisclosureForm.fee(InvestmentRelatedRevenueToJh)"
                            type="percent" pattern="${percentStyle}"/>%
      </td>
    </tr>    
</c:if>

    <tr>
      <td class="label label-indent">&nbsp;</td>
      <td class="value">&nbsp;</td>
    </tr>

<c:if test="${not empty supplementalDisclosureForm.fee(CostOfRecordKeeping)}">
    <tr>
      <td class="label  "> Estimated Cost of Recordkeeping</td>
      <td class="value value-indent  ">
         <render:number property = "supplementalDisclosureForm.fee(CostOfRecordKeeping)"
                            type="percent" pattern="${percentStyle}"/>%
      </td>
    </tr>    
</c:if>
   
   
    <tr>
      <td class="label label-indent">&nbsp;</td>
      <td class="value">&nbsp;</td>
    </tr>
   
   <tr>
      <td class="label label-indent">&nbsp;</td>
      <td class="value"><input type="button" name="button1" value="Close" class="button134" onclick="closeWindow()"/></td>
    </tr>
    
    

  </table>
