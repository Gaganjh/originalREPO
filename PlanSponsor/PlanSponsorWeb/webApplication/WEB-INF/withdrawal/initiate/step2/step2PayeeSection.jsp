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

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_PAYMENT_INSTRUCTIONS_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="paymentInstructionsSectionTitle"/>

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
  
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_IRS_DISTRIBUTION_NOTE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="irsDistributionNote"/>
  
  
    
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
                         <ps:fieldHilight 
                           name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.rolloverAccountNo" 
                           singleDisplay="true"
                           className="errorIcon"/>
                    	 <strong>
                    	 	 Account number for rollover
                    	 </strong>
                    </td>
                    <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="indentedValueColumn">
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].rolloverAccountNo" maxlength="34" onchange="return handlePayeeRolloverAccountNumberChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="payeeRolloverAccountNumberId[${recipientIndex}][${payeeIndex}]"/>




                    </td>
                  </tr>
               </c:if>
                <c:if test="${payee.showTrusteeForRollover}">
                  <tr class="datacell1" valign="top">
                    <td class="sectionNameColumn">
                         <ps:fieldHilight 
                           name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.rolloverPlanName" 
                           singleDisplay="true"
                           className="errorIcon"/>
                    	 <strong>
                    	 	 Trustee of
                    	 </strong>
                    </td>
                    <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="indentedValueColumn">
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].rolloverPlanName" maxlength="30" onchange="return handlePayeeRolloverPlanNameChanged(${recipientIndex}, ${payeeIndex});" onfocus="handleOnFocusRolloverPlanName(this);" cssClass="mandatory" id="payeeRolloverPlanNameId[${recipientIndex}][${payeeIndex}]"/>





                      <span style="left-padding:4px;"><strong>plan</strong></span>
                    </td>
                 </tr>
               </c:if>
               <c:if test="${(withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE) || ( (not empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.loans) and (withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo eq requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE) and (withdrawalForm.withdrawalRequestUi.withdrawalRequest.loanOption eq requestConstants.LOAN_CLOSURE_OPTION))}">
                 <tr class="datacell1" valign="top">
                   <td class="sectionNameColumn">
                     <ps:fieldHilight 
                       name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.irsDistCode" 
                       singleDisplay="true"
                       className="errorIcon"/>
                     <strong>
                       IRS distribution code for withdrawal
                     </strong>
                   </td>
                   <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                   <td class="indentedValueColumn">
 <form:select path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].irsDistCode" cssClass="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" id="payeeIrsDistributionCodeId[${recipientIndex}][${payeeIndex}]" onchange="return handlePayeeIrsDistributionCodeChanged(${recipientIndex}, ${payeeIndex});">


                       <form:option value="">- select -</form:option>
                       <form:options items="${irsWithdrawalDistributions}" itemValue="code" itemLabel="description"/>
</form:select>
                   </td>
                 </tr>
		  		<tr class="datacell1" valign="top">
		            <td class="indentedValue" colspan="3">
		            	<ul style="margin-left: ${isIE ? '20' : '-20'}px;">            	
		            		<li><content:getAttribute beanName="irsDistributionNote" attribute="text"/></li> 
		            	</ul>            	
		            </td>
		  		</tr>
               </c:if>
               <c:if test="${not withdrawalForm.withdrawalRequestUi.withdrawalRequest.wmsiOrPenchecksSelected}">
                 <tr class="datacell1" valign="top">
                    <td class="sectionNameColumn">
                         <ps:fieldHilight 
                           name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.paymentMethodCode" 
                           singleDisplay="true"
                           className="errorIcon"/>
                    	 <strong>
                    	 	 Payment method
                    	 </strong>
                    </td>
                    <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    <td class="valueColumn">
					 <form:radiobutton onclick="markPaymentMethodForRecipient${recipientIndex}Payee${payeeIndex}Changed();" path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentMethodCode" cssClass="mandatory" value="${payeeConstants.ACH_PAYMENT_METHOD_CODE}"/>ACH
					<form:radiobutton onclick="markPaymentMethodForRecipient${recipientIndex}Payee${payeeIndex}Changed();" path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentMethodCode" cssClass="mandatory" value="${payeeConstants.WIRE_PAYMENT_METHOD_CODE}"/>Wire
					<form:radiobutton onclick="markPaymentMethodForRecipient${recipientIndex}Payee${payeeIndex}Changed();" path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentMethodCode" cssClass="mandatory" value="${payeeConstants.CHECK_PAYMENT_METHOD_CODE}"/>Check
					
                    </td>
                  </tr>
                  <tr class="datacell1" valign="top">
                    <td>
                      <span class="sectionNameColumn" id="payeeBankAccountTypeCol1Id[${recipientIndex}][${payeeIndex}]" style="padding-left: ${isIE ? '2' : '4'}px;">
                         <ps:fieldHilight 
                           name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.bankAccountTypeCode" 
                           singleDisplay="true"
                           className="errorIcon"/>
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
					 <form:radiobutton onchange="onFieldChange(this);" path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentInstruction.bankAccountTypeCode" cssClass="mandatory" value="${payeeConstants.CHECKING_ACCOUNT_TYPE_CODE}"/>Checking
					  <form:radiobutton onchange="onFieldChange(this);" path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentInstruction.bankAccountTypeCode" cssClass="mandatory" value="${payeeConstants.SAVINGS_ACCOUNT_TYPE_CODE}"/>Savings 
					   	
                      </span>
                    </td>
                  </tr>
                </c:if>
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

          <c:if test="${not withdrawalForm.withdrawalRequestUi.withdrawalRequest.wmsiOrPenchecksSelected}">
            <tr>
              <td>
								<jsp:include page="step2EftPayeeSection.jsp"/>
              </td>
            </tr>
            <tr>
             <td>
								<jsp:include page="step2CheckPayeeSection.jsp"/>
             </td>
           </tr>
         </c:if>
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
