<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="requestUiConstants" className="com.manulife.pension.ps.web.withdrawal.WithdrawalRequestUi" />
<un:useConstants var="stateTaxConstants" className="com.manulife.pension.service.environment.valueobject.StateTaxVO" />
<un:useConstants var="stateTaxTypes" className="com.manulife.pension.service.environment.valueobject.StateTaxType" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_TAX_WITHHOLDING_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="taxWithholdingSectionTitle"/>
  
<script type="text/javascript">
function init(){
	var action_Invoked="${withdrawalForm.actionInvoked}";
	var rollOverInd= ${withdrawalForm.withdrawalRequestUi.withdrawalRequest.federalTaxVo.rolloverIndicator};
	var federalTax=${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.federalTaxPercent};
	var field = document.getElementById('recipientFederalTaxId');
	if(!(rollOverInd==false && action_Invoked!='calculate')){
	 	if(federalTax!=null && field!=null){
	 		for(var i = 0; i < field.length; i++)
		   	{
		      	if(field.options[i].value == federalTax)
		    		field.selectedIndex = i;
		   	}
	 	}
	}
}
window.onload = init;
</script>
<c:set  var="participantStateOfResidence" value='${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence}' />
<c:set  var="contractIssuedStateCode" value='${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractIssuedStateCode}' />
<c:if test="${withdrawalForm.withdrawalRequestUi.showTaxWitholdingSection}">
  <tr>
    <td class="datadivider" id="titlesec1" ><b><content:getAttribute beanName="taxWithholdingSectionTitle" attribute="text"/></b> </td>
  </tr>
</c:if>
<c:if test="${withdrawalForm.withdrawalRequestUi.showTaxWitholdingSection}">
  <tr class="datacell1" valign="top">
    <td>
     <div  id="taxsec" class="taxsec">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr valign="top">
          <td class="sectionName" style="width:100px;">
          	<strong>
          		<ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.federalTaxPercent" singleDisplay="true"/>
          		Federal tax rate
          	</strong>
          </td>
          <td style="padding-left:4px;">
               <c:choose>
               <c:when test="${participantStateOfResidence != 'PR' || contractIssuedStateCode != 'PR'}">
 <form:select path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.federalTaxPercent" id="recipientFederalTaxId" title="Refer to DOL Field Assistance Bulletin 2004-02 for details" onchange="return handleFederalTaxChanged(${recipientIndex});">


               <c:if test="${not withdrawalForm.withdrawalRequestUi.withdrawalRequest.federalTaxVo.rolloverIndicator}">
                 <form:option value="${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}">${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}</form:option>
               </c:if>
			   <c:choose>
			   <c:when test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode != requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}">
                  <c:forEach var="taxPercent" begin="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.federalTaxVo.taxPercentage}" end="100">
                       <form:option value="${taxPercent}.0000">
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
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.federalTaxPercent" disabled="true" maxlength="8" onchange="return handleFederalTaxInputChanged(${recipientIndex});" size="8" id="recipientFederalTaxInputId" /><%-- form:input - value="0.0000" --%>






			   </c:otherwise>
			   </c:choose>
             % of taxable withdrawal amount
          </td>
        </tr>
           <c:if test="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].showStateTax}">
             <tr class="datacell1" valign="top">
               <td class="sectionName">
               	<strong>
               		<ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" singleDisplay="true"/>
               		State tax rate
               	</strong>
               </td>
              <td style="padding-left:4px;">
				<%-- CL 103133 Begin--%>
				<c:set  var="taxPercentageMinimum" value='${withdrawalForm.withdrawalRequestUi.recipients[0].withdrawalRequestRecipient.stateTaxVo.taxPercentageMinimum}' />
				<c:set  var="defaultTaxRatePercentage" value='${withdrawalForm.withdrawalRequestUi.recipients[0].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}' />
				<%-- CL 103133 End--%>						
                <c:choose>
               	<c:when test="${participantStateOfResidence != 'PR' || contractIssuedStateCode != 'PR'}">
	              <c:choose>
                  <c:when test="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.stateTaxType == stateTaxTypes.MANDATORY}">
 <form:select path="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" id="recipientStateTaxDropdownId" onchange="return handleStateTaxDropdownChanged(${recipientIndex});">


						 <%-- CL 103133 Begin--%>
						<c:if test="${participantStateOfResidence == 'MS' && taxPercentageMinimum != defaultTaxRatePercentage}">								 
							  <form:option value="${taxPercentageMinimum}">
								<fmt:formatNumber value="${taxPercentageMinimum}" maxFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" minFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" />
							  </form:option>
						</c:if>
						 <%-- CL 103133 End--%>						
                      <form:option value="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}">
                        <fmt:formatNumber value="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}" 
                                          maxFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}"
                                          minFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" />
                      </form:option>
</form:select>
                  </c:when>
                  <c:when test="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.stateTaxType == stateTaxTypes.OPT_OUT}">
 <form:select path="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" id="recipientStateTaxDropdownId" onchange="return handleStateTaxDropdownChanged(${recipientIndex});">


                      <form:option value="${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}">${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}</form:option>                                 
						 <%-- CL 103133 Begin--%>
						<c:if test="${participantStateOfResidence == 'MS' && taxPercentageMinimum != defaultTaxRatePercentage}">								 
							  <form:option value="${taxPercentageMinimum}">
								<fmt:formatNumber value="${taxPercentageMinimum}" maxFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" minFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" />
							  </form:option>
						</c:if>
						 <%-- CL 103133 End--%>						
                      <form:option value="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}">
                        <fmt:formatNumber value="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}" 
                                          maxFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}"
                                          minFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" />
                      </form:option>
</form:select>
                  </c:when>
                  <c:when test="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.stateTaxType == stateTaxTypes.VOLUNTARY_FIXED}">
 <form:select path="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" id="recipientStateTaxDropdownId" onchange="return handleStateTaxDropdownChanged(${recipientIndex});">


                      <form:option value="${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}">${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}</form:option>                                 
					   <%-- CL 103133 Begin--%>
					   <c:if test="${participantStateOfResidence == 'MS' && taxPercentageMinimum != defaultTaxRatePercentage}">								 
						  <form:option value="${taxPercentageMinimum}">
							<fmt:formatNumber value="${taxPercentageMinimum}" maxFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" minFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" />
						  </form:option>
					  </c:if>
					  <%-- CL 103133 End--%>						
                      <form:option value="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}">
                        <fmt:formatNumber value="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}" 
                                          maxFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}"
                                          minFractionDigits="${requestConstants.TAX_PERCENTAGE_SCALE}" />
                      </form:option>
</form:select>
                  </c:when>
                  <c:otherwise>
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" maxlength="8" onchange="return handleStateTaxInputChanged(${recipientIndex});" size="8" id="recipientStateTaxInputId"/>




                  </c:otherwise>
                </c:choose>
                </c:when>
                <c:otherwise>
                	 <%-- CL 131784 --%>
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].stateTaxPercent" maxlength="8" onchange="return handleStateTaxInputChanged(${recipientIndex});" size="8" id="recipientStateTaxInputId"/>




                </c:otherwise>
                </c:choose>
                 <c:if test="${recipient.withdrawalRequestRecipient.stateTaxVo.taxTypeCode == stateTaxConstants.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX}">
                   % of federal tax
                 </c:if>
                 <c:if test="${recipient.withdrawalRequestRecipient.stateTaxVo.taxTypeCode == stateTaxConstants.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_WITHDRAWAL}">
                   % of taxable withdrawal amount
                 </c:if>
               </td>
             </tr>
           </c:if>
           </div>
      </table>
    </td>
  </tr>
</c:if>
