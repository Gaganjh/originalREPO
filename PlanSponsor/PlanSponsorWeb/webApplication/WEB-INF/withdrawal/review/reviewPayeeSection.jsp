<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="payeeConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="activityConstants" className="com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef" />
<un:useConstants var="activityPayeeConstants" className="com.manulife.pension.service.withdrawal.helper.PayeeFieldDef" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_PAYMENT_INSTRUCTIONS_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="paymentInstructionsSectionTitle"/>
<content:contentBean type="LayoutPage" 
  contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2}" 
  beanName="step2PageBean" /> 

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_CHECK_MAILED_TO_PAYEE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="checkMailedToPayee"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_CHECK_MAILED_TO_REP}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="checkMailedToRep"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_PAYMENT_INSTRUCTION_NOTE1}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="paymentInstructionNote1"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_PAYMENT_INSTRUCTION_NOTE2}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="paymentInstructionNote2"/>  

<div style="padding-top:10px;padding-bottom:10px;">
   <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
      <tr>
       <td class="tableheadTD1" colspan="3">
     	 	 <div style="padding-top:5px;padding-bottom:5px">
     		   <span style="padding-right:2px" id="payeeShowIcon[${recipientIndex}][${payeeIndex}]" onclick="showPayeeSection(${recipientIndex}, ${payeeIndex});">
     			   <img src="/assets/unmanaged/images/plus_icon.gif" width="12" height="12" border="0">
     		   </span>
     		   <span style="padding-right:2px" id="payeeHideIcon[${recipientIndex}][${payeeIndex}]" onclick="hidePayeeSection(${recipientIndex}, ${payeeIndex});">
     			   <img src="/assets/unmanaged/images/minus_icon.gif" width="12" height="12" border="0">
     		   </span>
     		   <b><content:getAttribute beanName="paymentInstructionsSectionTitle" attribute="text"/></b>
     	   </div>
        </td>
      </tr>
     <tr>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="paymentInstructionsTable[${recipientIndex}][${payeeIndex}]">
          <tr>
            <td>
              <table cellpadding="0" cellspacing="0" width="100%">
                <tr class="datacell1" valign="top">
                  <td class="sectionNameColumn"><strong>Payment to</strong></td>
                  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                  <td class="indentedValueColumn">
                    ${payee.paymentToDisplay}
                 </td>
                </tr>
                <c:if test="${payee.showAccountNumber}">
                  <tr class="datacell1" valign="top">
                    <td class="sectionNameColumn">
                       <strong>
                         Account number for rollover
                       </strong>
                    </td>
                    <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="indentedValueColumn">
                           <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.rolloverAccountNo" singleDisplay="true">
						      <ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_ROLLOVER_ACCOUNT_NO.id}" secondName="${payeeIndex+1}"/>
						   </ps:fieldHilight>
						   <c:choose>
						   	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
						   		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].rolloverAccountNo}
						   	</c:when>
						   	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].rolloverAccountNo" maxlength="34" onchange="return handlePayeeRolloverAccountNumberChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="payeeRolloverAccountNumberId[${recipientIndex}][${payeeIndex}]"/>




                          	</c:otherwise>
                          </c:choose>
                    </td>
                  </tr>
                </c:if>
                <c:if test="${payee.showTrusteeForRollover}">
                  <tr class="datacell1" valign="top">
                    <td class="sectionNameColumn">
                       <strong>
                         Trustee of
                       </strong>
                    </td>
                    <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="indentedValueColumn">
                           <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.rolloverPlanName" singleDisplay="true">
                              <ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_ROLLOVER_PLAN_NAME.id}" secondName="${payeeIndex+1}"/>
                           </ps:fieldHilight>
                           <c:choose>
                           	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
                           		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].rolloverPlanName}
                           	</c:when>
                           	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].rolloverPlanName" maxlength="30" onchange="return handlePayeeRolloverPlanNameChanged(${recipientIndex}, ${payeeIndex});" onfocus="handleOnFocusRolloverPlanName(this);" cssClass="mandatory" id="payeeRolloverPlanNameId[${recipientIndex}][${payeeIndex}]"/>





		                   	</c:otherwise>
		                   </c:choose>
                      <span style="left-padding:4px;"><strong>plan</strong></span>
                    </td>
                  </tr>
                </c:if>
<c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
                  <tr class="datacell1" valign="top">
                  <tr class="datacell1" valign="top">
                    <td class="sectionNameColumn">
                      <strong>
                        IRS distribution code for withdrawal
                      </strong>
                    </td>
                    <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="indentedValueColumn">
                    <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.irsDistCode" singleDisplay="true">
                         <ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_IRS_DIST_CODE.id}" secondName="${payeeIndex+1}"/>
                    </ps:fieldHilight>
                   <c:choose>
                    <c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
                     <ps:displayDescription collection="${irsWithdrawalDistributions}" keyName="code" keyValue="description" 
				                  key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].irsDistCode}"/>
					</c:when>
			    	<c:otherwise>
 <form:select path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].irsDistCode" cssClass="${(isPendingApproval or withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" id="payeeIrsDistributionCodeId[${recipientIndex}][${payeeIndex}]" onchange="return handlePayeeIrsDistributionCodeChanged(${recipientIndex}, ${payeeIndex});">



                        <form:option value="">- select -</form:option>
                        <form:options items="${irsWithdrawalDistributions}" itemValue="code" itemLabel="description"/>
</form:select>
                     </c:otherwise>
                    </c:choose>
                    </td>
                  </tr>
                </c:if>
                <tr class="datacell1" valign="top">
                  <td>
                    <span id="reviewPayeePaymentMethodCol1Id[${recipientIndex}][${payeeIndex}]" class="sectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                    	 <strong>
                    	 	 Payment method
                    	 </strong>
                    </span>
                  </td>
                  <td class="datadivider">
                    <span id="reviewPayeePaymentMethodCol2Id[${recipientIndex}][${payeeIndex}]">
                      <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
                    </span>
                  </td>
                  <td>
                    <span id="reviewPayeePaymentMethodCol3Id[${recipientIndex}][${payeeIndex}]" class="valueColumn" style="padding-left: ${isIE ? '0' : '2'}px;">
             	 	 <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.paymentMethodCode" singleDisplay="true">
               			<ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_PAYMENT_METHOD_CODE.id}" secondName="${payeeIndex+1}"/>
               		 </ps:fieldHilight>
               		 <c:choose>
               	
						 <c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
	<form:hidden path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentMethodCode" id="BankAccountTypeId[${recipientIndex}][${payeeIndex}]"/>
						

							<c:choose>
			          	   	  	<c:when test = "${withdrawalForm.withdrawalRequestUi.
			          	   	  				withdrawalRequest.recipients[recipientIndex].
			          	   	  				payees[payeeIndex].paymentMethodCode == payeeConstants.ACH_PAYMENT_METHOD_CODE}">
				          	  				ACH
				          	  	</c:when>
			          	   	  	<c:when test = "${withdrawalForm.withdrawalRequestUi.
			          	   	  				withdrawalRequest.recipients[recipientIndex].
			          	   	  				payees[payeeIndex].paymentMethodCode == payeeConstants.WIRE_PAYMENT_METHOD_CODE}">
				          	  				Wire
				          	  	</c:when>
				          	  	<c:when test = "${withdrawalForm.withdrawalRequestUi.
			          	   	  				withdrawalRequest.recipients[recipientIndex].
			          	   	  				payees[payeeIndex].paymentMethodCode == payeeConstants.CHECK_PAYMENT_METHOD_CODE}">
				          	  				Check
				          	  	</c:when>
			          	   	 </c:choose>
			       	      </c:when>
			          	  <c:otherwise>
<form:radiobutton onclick="markPaymentMethodForRecipient${recipientIndex}Payee${payeeIndex}Changed();" path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentMethodCode" cssClass="mandatory" value="${payeeConstants.ACH_PAYMENT_METHOD_CODE}"/>ACH
<form:radiobutton onclick="markPaymentMethodForRecipient${recipientIndex}Payee${payeeIndex}Changed();" path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentMethodCode" cssClass="mandatory" value="${payeeConstants.WIRE_PAYMENT_METHOD_CODE}"/>Wire
<form:radiobutton onclick="markPaymentMethodForRecipient${recipientIndex}Payee${payeeIndex}Changed();" path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentMethodCode" cssClass="mandatory" value="${payeeConstants.CHECK_PAYMENT_METHOD_CODE}"/>Check



			          	  </c:otherwise>
			           </c:choose>

                    </span>                                
                  </td>
                </tr>
                <tr class="datacell1" valign="top">
                  <td>
                    <span class="sectionNameColumn" id="payeeBankAccountTypeCol1Id[${recipientIndex}][${payeeIndex}]" style="padding-left: ${isIE ? '2' : '4'}px;">
                       <strong>
                         Bank account type
                       </strong>
                     </span>
                  </td>
                  <td class="datadivider">
                    <span id="payeeBankAccountTypeCol2Id[${recipientIndex}][${payeeIndex}]">
                      <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
                    </span>
                  </td>
                  <td>
                    <span class="valueColumn" id="payeeBankAccountTypeCol3Id[${recipientIndex}][${payeeIndex}]" style="padding-left: ${isIE ? '0' : '2'}px;">
						<ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.bankAccountTypeCode" singleDisplay="true">
							<ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_BANKACCOUNT_TYPE_CODE.id}" secondName="${payeeIndex+1}"/>
						</ps:fieldHilight>
						<c:choose>
						 
                                 
						 <c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
						  

							<c:choose>
			          	   	  	<c:when test = "${withdrawalForm.withdrawalRequestUi.
				          	  				withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].
				          	  				paymentInstruction.bankAccountTypeCode == payeeConstants.CHECKING_ACCOUNT_TYPE_CODE}">
				          	  				Checking
				          	  	</c:when>
				          	  	<c:when test = "${withdrawalForm.withdrawalRequestUi.
				          	  				withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].
				          	  				paymentInstruction.bankAccountTypeCode == payeeConstants.SAVINGS_ACCOUNT_TYPE_CODE}">
				          	  				Savings
				          	  	</c:when>
			          	   	 </c:choose>
			          	   	 
			       	      </c:when>
			          	  <c:otherwise>
<form:radiobutton onchange="onFieldChange(this);" path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentInstruction.bankAccountTypeCode" cssClass="mandatory" value="${payeeConstants.CHECKING_ACCOUNT_TYPE_CODE}"/>Checking
<form:radiobutton onchange="onFieldChange(this);" path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentInstruction.bankAccountTypeCode" cssClass="mandatory" value="${payeeConstants.SAVINGS_ACCOUNT_TYPE_CODE}"/>Savings



			          	  </c:otherwise>
			          	 </c:choose>
			          	    

                    </span>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

  		<tr class="datacell1" valign="top">
            <td class="indentedValue">
            	<ul style="margin-left: ${isIE ? '20' : '-20'}px;">            	
            		<li><content:getAttribute beanName="paymentInstructionNote1" attribute="text"/></li> 
 				<c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.mailChequeToAddressIndicator}">
        			<li><content:getAttribute beanName="checkMailedToRep" attribute="text"/></li>
    			</c:if>
 				<c:if test="${!withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.mailChequeToAddressIndicator}">
        			<li><content:getAttribute beanName="checkMailedToPayee" attribute="text"/></li>
    			</c:if>	    			  		
            		<li><content:getAttribute beanName="paymentInstructionNote2" attribute="text"/></li>
            	</ul>            	
            </td>
  		</tr>           
          <tr>
            <td id="reviewEftPayeeSectionId[${recipientIndex}][${payeeIndex}]">
              <jsp:include page="reviewEftPayeeSection.jsp"/>
            </td>
          </tr>
          <tr>
           <td id="reviewCheckPayeeSectionId[${recipientIndex}][${payeeIndex}]">
             <jsp:include page="reviewCheckPayeeSection.jsp"/>
           </td>
         </tr>
       </table>
     </td>
     <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
     </tr>
     <tr>
       <td colspan="3">
         <div id="paymentInstructionsFooter[${recipientIndex}][${payeeIndex}]">
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
         </div>
       </td>
     </tr>
   </table>
 </div>
