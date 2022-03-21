<%-- Withdrawal Payment Instructions R/O JSP Fragment

NOTE: This fragment is specific to each payment recipient and requires the
current recipient bean to be set in the request scope.

@param withdrawalRequestUi - Request scoped WithdrawalRequestUi bean
@param withdrawalRequest - Request scoped WithdrawalRequest bean ( = withdrawalRequestUi.getWithdrawalRequest())
@param recipientUi - Request scoped bean of type WithdrawalRequestRecipientUi (current recipient Ui) 
@param recipientStatus - Request scoped bean containing the recipient iteration status 
@param recipient Request scoped bean of type WithdrawalRequestRecipient matching the current recipient status
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%-- Payee constants --%>
<un:useConstants var="payeeConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee" />

<%-- Common constants --%>
<un:useConstants var="commonConstants" className="com.manulife.pension.platform.web.CommonConstants" />
<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_PAYMENT_INSTRUCTIONS_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="paymentInstructionsSectionTitle"/>

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_EFT_PAYEE_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="eftPayeeSectionTitle"/>

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_CHEQUE_PAYEE_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="chequePayeeSectionTitle"/>

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_OVERRIDE_CSF_MAIL_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="overrideCsfMailText"/>

<content:contentBean type="LayoutPage" 
  contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2}" 
  beanName="step2PageBean" /> 

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_PAYEE_X_SECTION_CONTENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="payeexSectionContent"/>
  
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

<c:set scope="request" var="courierCompanies"
  value="${withdrawalForm.lookupData['COURIER_COMPANY']}" />

<c:set scope="request" var="irsWithdrawalDistributions"
  value="${withdrawalForm.lookupData['IRS_CODE_WD']}" />

<c:set scope="request" var="paymentToTypes"
  value="${withdrawalForm.lookupData['PAYMENT_TO_TYPE']}" />


<%-- Begin Payee Loop --%>
<c:forEach items="${recipientUi.payees}" var="payeeUi" varStatus="payeeStatus">
  <c:set scope="request" var="payee" value="${recipient.payees[payeeStatus.index]}" />

  <table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
    <tr>
      <td class="tableheadTD1">
        <div style="padding-top:5px;padding-bottom:5px">
          <b><content:getAttribute attribute="text" beanName="paymentInstructionsSectionTitle" /></b>
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
        <table border="0" cellpadding="0" cellspacing="0" width="498">
          <tr>
            <td>
              <table border="0" cellpadding="0" cellspacing="0" width="498">
                <tr class="datacell1" valign="top">
                  <td class="sectionNameColumn"><strong>Payment to</strong></td>
                  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                  <td class="indentedValueColumn">
                    ${payeeUi.paymentToDisplay}
                 </td>
               </tr>

  <c:if test="${payeeUi.showAccountNumber}">
               
               <tr class="datacell1" valign="top">
                 <td class="sectionNameColumn"><strong>Account number for rollover</strong></td>
                 <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payee.rolloverAccountNo}"/></td>
               </tr>

  </c:if>
  <c:if test="${payeeUi.showTrusteeForRollover}">
               
               <tr class="datacell1" valign="top">
                 <td class="sectionNameColumn"><strong>Name of new plan</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn">
                   <c:choose>
                     <c:when test="${payee.rolloverPlanName != null}">
                       Trustee of <c:out value="${payee.rolloverPlanName}"/> plan
                     </c:when>
                   </c:choose>
                 </td>
               </tr>
               
  </c:if>
  <c:if test="${(withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE) || ( (not empty withdrawalRequest.loans)
     and (withdrawalRequest.paymentTo eq requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE) and (withdrawalRequest.loanOption eq requestConstants.LOAN_CLOSURE_OPTION ))}">
               <tr class="datacell1" valign="top">
                 <td class="sectionNameColumn"><strong>IRS distribution code for withdrawal</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn">
                   <ps:displayDescription collection="${irsWithdrawalDistributions}" keyName="code" keyValue="description" key="${payee.irsDistCode}"/>
                 </td>
               </tr>
                
  </c:if>
  <c:if test="${not withdrawalRequest.wmsiOrPenchecksSelected}">

               <tr class="datacell1" valign="top">
                 <td class="sectionNameColumn"><strong>Payment method</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn">
                   <c:choose>
                     <c:when test="${payee.paymentMethodCode == payeeConstants.ACH_PAYMENT_METHOD_CODE}">
                       ACH
                     </c:when>
                     <c:when test="${payee.paymentMethodCode == payeeConstants.WIRE_PAYMENT_METHOD_CODE}">
                       Wire
                     </c:when>
                     <c:when test="${payee.paymentMethodCode == payeeConstants.CHECK_PAYMENT_METHOD_CODE}">
                       Check
                     </c:when>
                   </c:choose>
                 </td>
               </tr>
               
    <c:if test="${payee.paymentMethodCode == payeeConstants.ACH_PAYMENT_METHOD_CODE}">
               
               <tr class="datacell1" valign="top" >
                 <td class="sectionNameColumn"><strong>Bank account type</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn">
                   <c:choose>
                     <c:when test="${payee.paymentInstruction.bankAccountTypeCode == payeeConstants.CHECKING_ACCOUNT_TYPE_CODE}">
                       Checking
                     </c:when>
                     <c:when test="${payee.paymentInstruction.bankAccountTypeCode == payeeConstants.SAVINGS_ACCOUNT_TYPE_CODE}">
                       Savings
                     </c:when>
                   </c:choose>
                 </td>
               </tr>
    </c:if>
    
  </c:if>
               
             </table>
           </td>
         </tr>
         <tr>
           <td>
             <table border="0" cellpadding="0" cellspacing="0" width="100%">
               <tr class="datacell1" valign="top">
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
               </tr>
  <c:if test="${withdrawalRequestUi.showStaticContent}">

 
          <tr class="datacell1" valign="top">
            <td &nbsp;">
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
  </c:if>
             </table>
           </td>
         </tr>

         
  <%-- Do not show Payment information details if WMSI or Penchecks is selected --%>
  <c:if test="${not withdrawalRequest.wmsiOrPenchecksSelected}">
         
    <%-- EFT Information Subsection (Shown if Payment method = ACH or Wire) --%>
    <c:if test="${payee.paymentMethodCode != payeeConstants.CHECK_PAYMENT_METHOD_CODE}">
           
         <tr>
           <td>
             <table border="0" cellpadding="0" cellspacing="0" width="100%">
               <tr valign="top" class="tablesubhead">
                 <td colspan="3" style="padding-left:4px"><b><content:getAttribute beanName="eftPayeeSectionTitle" attribute="text"/></b></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionTitleColumn"><b>
                   <c:choose>
                     <c:when test="${payeeUi.isFirstRolloverPayee}">Rollover financial institution</c:when>
                     <c:otherwise>Payee</c:otherwise>
                   </c:choose>
                   </b>
                 </td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn">&nbsp;</td>
               </tr>
               <c:if test="${not empty payeeUi.withdrawalRequestPayee.rolloverType && withdrawalRequest.paymentTo == requestConstants.PAYMENT_TO_ROLLOVER_TO_IRA_CODE}">
                <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>Rollover Type</strong></td>
                 <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payeeUi.withdrawalRequestPayee.rolloverType}"/></td>
               </tr>
               </c:if>
               
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>${payeeUi.subSectionNameColumn}</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payeeUi.eftOrganizationName}"/></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>Address line 1</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payee.address.addressLine1}"/></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>Address line 2</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payee.address.addressLine2}"/></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>City</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payee.address.city}"/></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>State</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payee.address.stateCode}"/></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>ZIP Code</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn">
                   <c:choose>
                     <c:when test="${not empty payee.address.zipCode1 && not empty payee.address.zipCode2 
                     		&& payee.address.countryCode == 'USA'}" >
                       <c:out value="${payee.address.zipCode1}"/>-<c:out value="${payee.address.zipCode2}"/>
                     </c:when>
                     <c:otherwise>
                       <c:out value="${payee.address.zipCode}"/>
                     </c:otherwise>
                   </c:choose>
                 </td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>Country</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn">
                   <ps:displayDescription collection="${countries}" keyName="value" keyValue="label" 
                     key="${payee.address.countryCode}" />
                 </td>
               </tr>
               
               <tr class="datacell1" valign="top">
                 <td class="subsectionTitleColumn" colspan="3"><b>Bank details</b></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>Bank/Branch name</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payee.paymentInstruction.bankName}"/></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>ABA / Routing number</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><fmt:formatNumber minIntegerDigits="9" groupingUsed="false"><c:out value="${payee.paymentInstruction.bankTransitNumber}"/></fmt:formatNumber></td>
               </tr>

      <c:if test="${payeeUi.showBankAccountNumber}">
      			<tr class="datacell1" valign="top">
	                 <td class="subsectionNameColumn"><strong>Account number</strong></td>
	                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	                 <td class="indentedValueColumn">
						<c:choose>
			                 <c:when test="${withdrawalRequest.statusCode == 'W1' || withdrawalRequest.statusCode == 'W7'}">
			                 	<c:out value="${commonConstants.MASKED_ACCOUNT_NUMBER}"/>
			                 </c:when>
			                 <c:otherwise>
								<c:out value="${payee.paymentInstruction.bankAccountNumber}"/>
		                 	</c:otherwise>
						</c:choose>                 	
                 	</td>
	            </tr>
      </c:if>
              
             </table>
           </td>
         </tr> 
    </c:if>
    <%-- End of EFT Information section --%>
    
    <%-- Check Information Subsection (Shown if Payment method = Check) --%>
    <c:if test="${payee.paymentMethodCode == payeeConstants.CHECK_PAYMENT_METHOD_CODE}">
         <tr>
           <td>
             <table border="0" cellpadding="0" cellspacing="0" width="498">
               <tr valign="top" class="tablesubhead">
                 <td colspan="3"><b><content:getAttribute beanName="chequePayeeSectionTitle" attribute="text"/></b></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionTitleColumn" colspan="3"><b>
                   <c:choose>
                     <c:when test="${withdrawalRequest.paymentTo == requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">Trustee</c:when>
                     <c:when test="${payeeUi.isFirstRolloverPayee}">Rollover financial institution</c:when>
                     <c:otherwise>Participant</c:otherwise>
                   </c:choose>
                   </b>
                 </td>
               </tr>
                <c:if test="${not empty payeeUi.withdrawalRequestPayee.rolloverType && withdrawalRequest.paymentTo == requestConstants.PAYMENT_TO_ROLLOVER_TO_IRA_CODE}">
                <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>Rollover Type</strong></td>
                 <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payeeUi.withdrawalRequestPayee.rolloverType}"/></td>
               </tr>
               </c:if>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>${payeeUi.subSectionNameColumn}</strong></td>
                 <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payeeUi.checkOrganizationName}"/></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>Address line 1</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payee.address.addressLine1}"/></td>
               </tr>
               
			   <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE && payeeUi.paymentToDisplay !='Employer Sponsored Qualified Plan'}">
			   <c:choose>
			   <c:when test ="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode !='RE' || withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode !='TE'}">
			   <tr class="datacell1" valign="top">
			   <td class="subsectionNameColumn">
			   <strong>Address line 2</strong>
			   </td>
			   <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
			   <td class="indentedValueColumn"> <c:out value="${payee.address.addressLine2}"/>
			   </td>
			   </tr>
			   </c:when>
			   </c:choose>
			   </c:if>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>City</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payee.address.city}"/></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>State</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn"><c:out value="${payee.address.stateCode}"/></td>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>ZIP Code</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn">
                   <c:choose>
                     <c:when test="${not empty payee.address.zipCode1 && not empty payee.address.zipCode2 
                     		&& payee.address.countryCode == 'USA'}" >
                       <c:out value="${payee.address.zipCode1}"/>-<c:out value="${payee.address.zipCode2}"/>
                     </c:when>
                     <c:otherwise>
                       <c:out value="${payee.address.zipCode}"/>
                     </c:otherwise>
                   </c:choose>
               </tr>
               <tr class="datacell1" valign="top">
                 <td class="subsectionNameColumn"><strong>Country</strong></td>
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                 <td class="indentedValueColumn">
                   <ps:displayDescription collection="${countries}" keyName="value" keyValue="label" 
                     key="${payee.address.countryCode}" />
                 </td>
               </tr>
              
      <c:if test="${withdrawalRequestUi.showStaticContent}">
      	<c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.mailChequeToAddressIndicator}">
               <tr class="datacell1" valign="top">
                 <td colspan="3" style="padding-left:27px;"><content:getAttribute beanName="overrideCsfMailText" attribute="text"/></td>
               </tr>
      	</c:if>
      </c:if>
<%-- Security Enhancements - removed "Send to address above" line --%>

             </table>
                
           </td>
         </tr>
        
    </c:if>
    <%-- End of EFT Information section --%>
         
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
  
  <c:if test="${!payeeStatus.last}"> 
 	<br>
</c:if>
 
</c:forEach>
<br>
<%-- End Payee Loop --%>
