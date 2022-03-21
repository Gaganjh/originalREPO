<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>

<script type="text/javascript">
function showParticipantLegaleseContent() {
 
   var reportURL = new URL("/do/withdrawal/viewParticipantLegalese/");
   reportURL.setParameter("task", "print");
   reportURL.setParameter("printFriendly", "true");
   reportURL.setParameter("submissionId", "<c:out value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.submissionId}"/>");
   window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
 }
</script>

<un:useConstants var="declarationConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="activityConstants" className="com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_DECLARATION_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationSectionTitle"/>
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
  id="declarationWaitingPeriodText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_IRA_PROVIDER_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationIraProviderText"/>
 <content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_APPROVE_DECLARATION_RISK_INDICATOR_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="riskIndicatorText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_APPROVE_EMAIL_RISK_INDICATOR_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="riskIndicatorEmailText"/>
<content:contentBean type="LayoutPage" 
  contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2}" 
  beanName="step2PageBean" /> 
  
<div style="padding-top:10px;padding-bottom:10px;">
 <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
   <tr class="tablehead">
     <td colspan="3" class="tableheadTD1">
       <div style="padding-bottom: ${isIE ? '1' : '5'}px; padding-top: ${isIE ? '1' : '5'}px;">
         <b><content:getAttribute beanName="declarationSectionTitle" attribute="text"/></b>
       </div>
     </td>
   </tr>
   <tr>
     <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
     <td width="100%">
       <strong>The participant has certified:</strong>
       <br/>
       <table border="0" cellspacing="0" cellpadding="0">
         <tr>
           <td align="right" style="padding-left:4px;">
             <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.taxNoticeDeclaration" singleDisplay="true"> 
              	 <ps:activityHistory itemNumber="${activityConstants.DYN_DECLARATION_TYPE}" secondName="${declarationConstants.TAX_NOTICE_TYPE_CODE}"/>
           	 </ps:fieldHilight>
          	 <c:choose>
			    <c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
			    	<form:checkbox path="withdrawalRequestUi.selectedDeclarations"
                   			value="${declarationConstants.TAX_NOTICE_TYPE_CODE}"
                   			id="taxNoticeDeclarationId"
                   			disabled ="true"/>
			    </c:when>
			    <c:otherwise>
			    	<form:checkbox path="withdrawalRequestUi.selectedDeclarations"
		                           value="${declarationConstants.TAX_NOTICE_TYPE_CODE}"
		                           id="taxNoticeDeclarationId"
		                           onclick="return handleTaxNoticeDeclarationChanged();"/>
                </c:otherwise>
              </c:choose>
           </td>
           <td align="left">
           	<c:choose>
		   	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
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
         </tr>
         <tr>
           <td align="right" style="padding-left:4px;">
             <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.waitingPeriodDeclaration" singleDisplay="true">
             	 <ps:activityHistory itemNumber="${activityConstants.DYN_DECLARATION_TYPE}" secondName="${declarationConstants.WAITING_PERIOD_WAIVED_TYPE_CODE}"/>
             </ps:fieldHilight>
             <c:choose>
             	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
 		   	        <form:checkbox path="withdrawalRequestUi.selectedDeclarations"
                    			value="${declarationConstants.WAITING_PERIOD_WAIVED_TYPE_CODE}"
                    			id="waitingPeriodDeclarationId"
                    			disabled="true"/>
             	</c:when>
             	<c:otherwise>
   	             	<form:checkbox path="withdrawalRequestUi.selectedDeclarations"
	                            value="${declarationConstants.WAITING_PERIOD_WAIVED_TYPE_CODE}"
	                            id="waitingPeriodDeclarationId"
	                            onclick="return handleWaitingPeriodDeclarationChanged();"/>
              </c:otherwise> 
             </c:choose>             
           </td>
           <td align="left">
             <content:getAttribute beanName="declarationWaitingPeriodText" attribute="text"/>
           </td>
         </tr>
         <!-- participant's declarations -->
       
 	<c:if test="${withdrawalForm.withdrawalRequestUi.showParticipantAgreedLegaleseContent}">
   	<tr>
    	  <td align="right" style="padding-left:4px;">
		<input name="checkbox" type="checkbox" value="checkbox" checked disabled="disabled"></td>
    	  <c:if test="${empty param.printFriendly}">
    	  <td>The <a href="javascript:showParticipantLegaleseContent()">
		Participant declaration</a> was read and agreed to.
    	  </td>
    	</c:if>
    	<c:if test="${param.printFriendly}">
    	  <td>The Participant declaration was read and agreed to: <br>      
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantLegaleseInfo.legaleseText}</td>
          </tr>
    	</c:if>
	</tr>
	</c:if>  

       </table>
     </td>
     <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
   </tr>
  
   <c:if test="${withdrawalForm.withdrawalRequestUi.showItWasCertifiedLabel}">
     <tr>
       <td class="boxborder">
         <span id="iraDeclarationHeaderCol1Id">
           <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
         </span>
       </td>
       <td>
         <span id="iraDeclarationHeaderCol2Id" class="sectionNameColumn">
           <strong>It was certified that:</strong>
         </span>
       </td>
       <td class="boxborder">
         <span id="iraDeclarationHeaderCol3Id">
           <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
         </span>
       </td>
     </tr>
     </c:if>
     <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode 
   								== requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}">
     <tr>
       <td class="boxborder">
         <span id="iraDeclarationCol1Id">
           <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
         </span>
       </td>
       <td>
         <span id="iraDeclarationCol2Id" style="padding-left:4px;">
           <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.iraProviderDeclaration" singleDisplay="true">
              <ps:activityHistory itemNumber="${activityConstants.DYN_DECLARATION_TYPE}" secondName="${declarationConstants.IRA_SERVICE_PROVIDER_TYPE_CODE}"/>
           </ps:fieldHilight> 
			           <form:checkbox path="withdrawalRequestUi.selectedDeclarations"
			                          value="${declarationConstants.IRA_SERVICE_PROVIDER_TYPE_CODE}"
			                          id="wmsiDeclarationId"
			                          onclick="return handleWmsiDeclarationChanged();"/>
              			<content:getAttribute beanName="declarationIraProviderText" attribute="text"/>
          </span>
        </td>
        <td class="boxborder">
          <span id="iraDeclarationCol3Id">
            <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
          </span>
        </td>
      </tr>
 
    </c:if> 
    <c:if test="${withdrawalForm.withdrawalRequestUi.showAtRiskDeclaration}"> 
	       <tr>
	       	 	<td class="boxborder">
	             	
	         		<span id="riskDeclarationCol1Id">
	           			<img src="/assets/unmanaged/images/s.gif" height="1" width="1">
	         		</span>
	       		</td>
		       		
		       		<td>
		       		<table>
             			<tr valign="top">
	             			<td>
					       		 <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.atRiskTransactionDeclaration" singleDisplay="true">
					             	 <ps:activityHistory itemNumber="${activityConstants.DYN_DECLARATION_TYPE}" secondName="${declarationConstants.AT_RISK_TRANSACTION_TYPE_CODE}"/>
					             </ps:fieldHilight>
				  			</td>
				   	        <td style="padding: 0px;">
				   	        <c:choose>
					  			<c:when test="${withdrawalForm.withdrawalRequestUi.showApproveButton}">
						  			<form:checkbox path="withdrawalRequestUi.selectedDeclarations"
						                            value="${declarationConstants.AT_RISK_TRANSACTION_TYPE_CODE}"
						                            id="riskPinExpDeclarationId"
						                            onclick="return handleRiskPinExposureDeclarationChanged();"/>
					  			</c:when>
					  			<c:otherwise>
						  			<form:checkbox disabled="true" path="withdrawalRequestUi.selectedDeclarations"
						                            value="${declarationConstants.AT_RISK_TRANSACTION_TYPE_CODE}"
						                            id="riskPinExpDeclarationId"
						                            onclick="return handleRiskPinExposureDeclarationChanged();"/>
					  			</c:otherwise>
				  			</c:choose>
							</td>
							<td>		  
									  <c:if test="${ not empty withdrawalForm.withdrawalRequestUi.atRiskApprovalText}">	
									  
									  <p>${withdrawalForm.withdrawalRequestUi.atRiskApprovalText}</p>
				
										<ol style="MARGIN-LEFT: 20px;">
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
				  </td>
		          <td class="boxborder">
		          <span id="riskDeclarationCol3Id">
		            <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
		          </span>
		        </td>
		    
        </tr>  
     </c:if>   
  
    <tr class="datacell1">
      <td colspan="3" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td class="indentedValue"><content:getAttribute beanName="step2PageBean" attribute="body3"/></td>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
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
</div>
