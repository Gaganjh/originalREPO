<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="requestUiConstants" className="com.manulife.pension.ps.web.withdrawal.WithdrawalRequestUi" />
<un:useConstants var="stateTaxConstants" className="com.manulife.pension.service.environment.valueobject.StateTaxVO" />
<un:useConstants var="activityConstants" className="com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_TAX_WITHHOLDING_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="taxWithholdingSectionTitle"/>

<script type="text/javascript">
function init(){
	var field = document.getElementById('recipientFederalTaxId');
	<c:if test="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.federalTaxPercent!=null}">
		for(var i = 0; i < field.length; i++)
	   	{
	      	if(field.options[i].value == ${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.federalTaxPercent})
	    		field.selectedIndex = i;
	   	}
 	</c:if>
}
window.onload = init;
</script>
<c:set  var="participantStateOfResidence" value='${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence}' />
<c:set  var="contractIssuedStateCode" value='${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractIssuedStateCode}' />
<c:if test="${withdrawalForm.withdrawalRequestUi.showTaxWitholdingSection}">
  <tr>
    <td class="datadivider" id="titlesec" ><b><content:getAttribute beanName="taxWithholdingSectionTitle" attribute="text"/></b> </td>
  </tr>
  <tr class="datacell1" valign="top">
    <td>
       <div  id="taxsection" class="taxsection">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr valign="top">
          <td class="sectionName" style="width:100px;">
            <strong>
              Federal tax rate
            </strong>
          </td>
          <td style="padding-left:4px;">
          	  <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.federalTaxPercent" singleDisplay="true">
	              <ps:activityHistory itemNumber="${activityConstants.FED_TAX_RATE.id}"/>
              </ps:fieldHilight>
              <c:choose>
                      <c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
                      <form:hidden path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.federalTaxPercent" id="recipientFederalTaxId"/>

                      ${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.federalTaxPercent}
                      </c:when>
			    	<c:otherwise>
			  <c:choose>
			  <%-- CL 131784: Display the Federal tax as disabled input box when state of residence is PR  --%>
			  <c:when test="${participantStateOfResidence != 'PR' || contractIssuedStateCode != 'PR'}">
 <form:select path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.federalTaxPercent" id="recipientFederalTaxId" title="Refer to DOL Field Assistance Bulletin 2004-02 for details" onchange="return handleFederalTaxChanged(${recipientIndex});">


				<c:if test="${not withdrawalForm.withdrawalRequestUi.withdrawalRequest.federalTaxVo.rolloverIndicator}">
                 <form:option value="${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}">${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}</form:option>
               	</c:if>                          
              <c:choose>
            	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated}">
              		<form:option value="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.federalTaxPercent}">
                 	<fmt:formatNumber value="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.federalTaxPercent}" 
                                   			maxFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}"
                                   			minFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" />
               		</form:option>
               	</c:when>
               	<c:when test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode != requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}">
	              			<c:forEach var="taxPercent" begin="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.federalTaxVo.taxPercentage}" end="100">
	                       		<form:option value="${taxPercent}">
									<fmt:formatNumber value="${taxPercent}"  maxFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" minFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" />
								</form:option>
					  		</c:forEach>
	             </c:when>
               	<c:otherwise>
	              	<form:option value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.federalTaxVo.taxPercentage}">
					 <fmt:formatNumber value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.federalTaxVo.taxPercentage}"  maxFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" minFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" />
                  	</form:option>
               </c:otherwise>
			 </c:choose>               
</form:select>
             </c:when>
             <c:otherwise>
             <form:hidden path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.federalTaxPercent" value="0.0000"/>
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.federalTaxPercent" disabled="true" maxlength="8" onchange="return handleFederalTaxInputChanged(${recipientIndex});" size="8" id="recipientFederalTaxInputId" /><%--  form:input - value="0.0000" --%>






             </c:otherwise>
             </c:choose>
             </c:otherwise>
             </c:choose>
             <span id="stateTaxPercentWithdrawalLabelSpanId">
             	% of taxable withdrawal amount
             </span>
          </td>
        </tr>
         <tr class="datacell1" valign="top">
           <td>
            <span class="sectionName" id="stateTaxCol1Id">
              <strong>
                State tax rate
              </strong>
            </span>
           </td>
           <td>
             <span style="padding-left:4px;" id="stateTaxCol2Id">
                <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" singleDisplay="true">
	                <ps:activityHistory itemNumber="${activityConstants.STATE_TAX_RATE.id}"/>
                </ps:fieldHilight>
               <span id="stateTaxTextInputSpanId">
               	 <c:choose>
               	 	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
               	 	<form:hidden path="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" id="recipientStateTaxInputId"/>

               	 		${e:forHtmlContent(withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].stateTaxPercent)}
               		</c:when>
               		<c:otherwise>
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" maxlength="8" onchange="return handleStateTaxInputChanged();" size="8" cssClass="${isPendingApproval ? 'mandatoryInputRight' : ''}" id="recipientStateTaxInputId"/>





		            </c:otherwise>
		         </c:choose>
               </span>
               <span id="stateTaxSelectSpanId">
               	 <c:choose>
               	 	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
               	 	<form:hidden path="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" id="recipientStateTaxDropdownPOWId"/>

               	 		${e:forHtmlContent(withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].stateTaxPercent)}
               	 	</c:when>
               	 	<c:otherwise>
 <form:select path="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" id="recipientStateTaxDropdownId" cssClass="${isPendingApproval ? 'mandatoryInputRight' : ''}" onchange="return handleStateTaxDropdownChanged();"/>



                 	</c:otherwise>
                 </c:choose>
               </span>
               <span id="stateTaxPercentFederalLabelSpanId">
                 % of federal tax
               </span>
               <span id="stateTaxPercentWithdrawalLabelSpanId">
                 % of taxable withdrawal amount
               </span>
             </span>
           </td>
         </tr>
        </div>
      </table>
    </td>
  </tr>
</c:if>
