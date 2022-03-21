<%-- Declarations Section R/O JSP Fragment

NOTE: This fragment presents misc withdrawal request declarations and legalese.

@param withdrawalRequestUi - Request scoped WithdrawalRequestUi bean
@param withdrawalRequest - Request scoped WithdrawalRequest bean ( = withdrawalRequestUi.getWithdrawalRequest())
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<script type="text/javascript">
function showParticipantLegaleseContent() {
 
   var reportURL = new URL("/do/withdrawal/viewParticipantLegalese/");
   reportURL.setParameter("task", "print");
   reportURL.setParameter("printFriendly", "true");
   reportURL.setParameter("submissionId", "<c:out value="${withdrawalRequest.submissionId}"/>");
   window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
 }
</script>
<content:contentBean type="LayoutPage" 
  contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2}" 
  beanName="step2PageBean" /> 

<content:contentBean 
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_DECLARATION_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="declarationsSectionTitle"/>
  
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_TAX_NOTICE_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationTaxNoticeText"/>

<content:contentBean 
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_DECLARATION_TAX_NOTICE_LINK_EZK}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationTaxNoticeLinkEzk"/>
  
<content:contentBean 
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_DECLARATION_TAX_NOTICE_LINK_PSW}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationTaxNoticeLinkPsw"/>
  
<content:contentBean 
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_WAITING_PERIOD_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="waitingPeriodText"/>

<content:contentBean 
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_IRA_PROVIDER_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="iraProviderText"/>
  
<content:contentBean 
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_APPROVE_LEGALESE_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="legaleseText"/>  

<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_VIEW_PARTICIPANT_LEGALESE_TEXT}"
  id="participantLegalese"/>
  
 <content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_APPROVE_DECLARATION_RISK_INDICATOR_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="riskIndicatorText"/>
  
 <content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_APPROVE_EMAIL_RISK_INDICATOR_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="riskIndicatorEmailText"/>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td class="tableheadTD1">
      <div style="padding-top:5px;padding-bottom:5px">
        <b><content:getAttribute attribute="text" beanName="declarationsSectionTitle" /></b>
      </div>
    </td>
    <td class="tablehead" style="text-align:right" nowrap>
      &nbsp;
    </td>
  </tr>
</table>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td>
      <strong>&nbsp;The participant has certified:</strong><br>
    
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5%">
            <c:choose>
              <c:when test="${withdrawalRequestUi.hasTaxNoticeDeclaration}">
                <input name="checkbox" type="checkbox" value="checkbox" checked disabled="disabled">
              </c:when>
              <c:otherwise>
                <input name="checkbox" type="checkbox" value="checkbox" disabled="disabled">
              </c:otherwise>
            </c:choose>
          </td>
          
     		<c:if test="${param.printFriendly}">
          	<td width="95%"><content:getAttribute beanName="declarationTaxNoticeText" attribute="title"/></td>
          	</c:if>
         
         
     		<c:if test="${empty param.printFriendly}">
          	<td width="95%" >
          	  <c:choose>
          	     <c:when test="${withdrawalRequestUi.isParticipantInitiated}">
	          	    <c:choose>
					<c:when test="${withdrawalForm.withdrawalRequestUi.isIRSSpecialTaxNotice}">
						<content:getAttribute beanName="declarationTaxNoticeLinkEzk" attribute="text"/>
					</c:when>
					<c:otherwise>	                 
		            	<content:getAttribute beanName="declarationTaxNoticeText" attribute="title"/>
		            </c:otherwise>
	           		</c:choose>
          	     </c:when>
          	     <c:otherwise>
          	     	<c:choose>
					<c:when test="${withdrawalForm.withdrawalRequestUi.isIRSSpecialTaxNotice}">
						<content:getAttribute beanName="declarationTaxNoticeLinkPsw" attribute="text"/>
					</c:when>
					<c:otherwise>	                 
	            		<content:getAttribute beanName="declarationTaxNoticeText" attribute="text"/>
	            	</c:otherwise>
           			</c:choose>
          	     </c:otherwise>
          	  </c:choose> 
          	</td>
          	</c:if>          
        </tr>
        <tr>
          <td>
            <c:choose>
              <c:when test="${withdrawalRequestUi.hasWaitPeriodWaveDeclaration}">
                <input name="checkbox" type="checkbox" value="checkbox" checked disabled="disabled">
              </c:when>
              <c:otherwise>
                <input name="checkbox" type="checkbox" value="checkbox" disabled="disabled">
              </c:otherwise>
            </c:choose>
          </td>
          <td><content:getAttribute beanName="waitingPeriodText" attribute="text"/></td>
        </tr>
<!-- participant's legalese content -->
 	<c:if test="${withdrawalRequestUi.showParticipantAgreedLegaleseContent}">
   	<tr>
    	  <td><input name="checkbox" type="checkbox" value="checkbox" checked disabled="disabled"></td>
    	  <c:if test="${empty param.printFriendly}">
    	  <td>The <a href="javascript:showParticipantLegaleseContent()">
		Participant declaration</a> was read and agreed to.
    	  </td>
    	</c:if>
    	<c:if test="${param.printFriendly}">
    	  <td>The Participant declaration was read and agreed to: <br> </td>     
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;<c:out value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantLegaleseInfo.legaleseText}" escapeXml="false"/></td>
          </tr>
    	</c:if>
	</tr>
	</c:if>        
        
      </table>
      
    </td>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  </tr>

<!-- Approver's declarations -->
<!--c:if test="${withdrawalRequestUi.isRequestApproved}"-->
  
  <tr>
    <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td><br>
    <c:choose>
    	<c:when test= "${withdrawalRequestUi.isParticipantInitiated}">
    		<c:if test="${empty param.printParticipant}">
      			<c:if test="${withdrawalRequestUi.showItWasCertifiedLabel}">
        			<strong>&nbsp;It was certified that: </strong><br>
      			</c:if>
    		</c:if>
    	</c:when>
    	<c:otherwise>
    		<c:if test="${withdrawalRequest.wmsiOrPenchecksSelected}">
    			<strong>&nbsp;It was certified that: </strong><br>
    		</c:if>
    	</c:otherwise>
    </c:choose>
    
      <table width="100%" border="0" cellspacing="0" cellpadding="0">

    <!-- If WMSI or Penchecks show IRA Provider Account declaration -->
    <c:if test="${withdrawalRequest.wmsiOrPenchecksSelected}">
        <tr>
          <td width="5%">
            <c:choose>
              <c:when test="${withdrawalRequestUi.hasIraProviderDeclaration}">
                <input name="checkbox" type="checkbox" value="checkbox" checked disabled="disabled">
              </c:when>
              <c:otherwise>
                <input name="checkbox" type="checkbox" value="checkbox" disabled="disabled">
              </c:otherwise>
            </c:choose>
          </td>
          <td width="95%"><content:getAttribute beanName="iraProviderText" attribute="text"/></td>
        </tr>
    </c:if>
 <!-- If request risk declaration needs to be displayed-->
 <c:if test="${empty param.printParticipant}">
	<c:if test="${withdrawalRequestUi.showAtRiskDeclaration}">
			<tr>
              <td class="datacell1"></td>
              <td class="datacell1" style="padding: 0px;">
                <table>
		           <tr valign="top">
		           
		           		<style>
				   	        #riskIndicatorCheckbox{
				   	        	margin-left: -4px;
				   	        }
				   	      </style>
							
						<td style="padding: 0px;"><c:choose>
							
							
							<c:when test="${withdrawalRequestUi.hasRiskIndicatorDeclaration}">
								<input name="checkbox" type="checkbox" value="checkbox" checked
									disabled="disabled" id="riskIndicatorCheckbox">
							</c:when>
							<c:otherwise>
								<input name="checkbox" type="checkbox" value="checkbox"
									disabled="disabled" id="riskIndicatorCheckbox">
							</c:otherwise>
						</c:choose></td>
						<td>
						<c:if test="${ not empty withdrawalForm.withdrawalRequestUi.atRiskApprovalText}">
							<p>${withdrawalForm.withdrawalRequestUi.atRiskApprovalText}</p>

							<ol>
<c:if test="${not empty withdrawalForm.withdrawalRequestUi.atRiskdetailText}">
<c:forEach items="${withdrawalForm.withdrawalRequestUi.atRiskdetailText}" var="detailText">
										<li>${detailText}</li>
</c:forEach>
</c:if>
							</ol>

						</c:if>
						</td>
					</tr>
					</table>
				</c:if>
			</c:if>  
    <c:if test="${withdrawalRequestUi.showStaticContent}">
        <tr class="datacell1" valign="top">
          <td colspan=2>&nbsp;<content:getAttribute beanName="step2PageBean" attribute="body3"/></td>
        </tr>
    </c:if>
      
	<c:if test="${withdrawalForm.withdrawalRequestUi.isRequestApproved}">
		<tr>
			<td colspan="2"><strong>&nbsp;The approver agreed to: </strong><br></td>
		</tr>
        <tr>
           <td colspan="2" >&nbsp;<c:out value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.legaleseInfo.legaleseText}" escapeXml="false"/></td>
        </tr>
        
	</c:if>       
  </table>
        
    </td>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  </tr>
  

  
  <tr>
    <td colspan="3">
      
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
          <td rowspan="2" width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" height="5" width="5"></td>
        </tr>
        <tr>
          <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
      </table>
      
    </td>
  </tr>
</table>
