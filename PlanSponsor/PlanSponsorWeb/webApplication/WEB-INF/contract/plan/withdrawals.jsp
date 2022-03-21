<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_WITHDRAWAL}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="withdrawalsText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_QMAC_QNEC_NOTE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="hardshipWithdrawalQmacQnecMoneyTypeText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_NO_HARDSHIP_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noHardshipMoneyTypesText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_PROVISIONS_FOR_WITHDRAWAL_REASONS_TEXT_CAR}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="planWithdrawalReasonCarText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_PROVISIONS_FOR_WITHDRAWAL_REASONS_TEXT_TPA}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="planWithdrawalReasonTpaText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_RETIREMENT_PROVISIONS}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="retirementProvisions"/>
<content:contentBean
contentId="${contentConstants.FEE_INFORMATION_FOR_404A5_DISCLOSURE_PURPOSES_FOR_WITHDRAWALS}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="applyFeeInformation404a5DisclosurePurposeForWithdrawals" />


<div id="withdrawalsTabDivId" class="borderedDataBox">
  <div class="subhead">
    <c:if test="${not param.printFriendly}">
      <div class="expandCollapseIcons" id="withdrawalsShowIconId" onclick="expandDataDiv('withdrawals');">
        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
      </div>
      <div class="expandCollapseIcons" id="withdrawalsHideIconId" onclick="collapseDataDiv('withdrawals');">
        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
      </div>
    </c:if>
    <div class="sectionTitle">
      <content:getAttribute beanName="withdrawalsText" attribute="text"/>
    </div>
    <div class="sectionHighlightIcon" id="withdrawalsSectionHighlightIconId">
      <ps:sectionHilight name="${uiConstants.SECTION_WITHDRAWALS}" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
    </div>
    <div class="endDataRowAndClearFloats"></div>
  </div>
  <div id="withdrawalsDataDivId">
    <div class="subsubhead">
      General withdrawal provisions
    </div>
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="withdrawalsExtendedLabelColumn">Does the plan require spousal consent for distributions?</td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
             
<form:radiobutton disabled="${disableFields}" onclick="return handleRequiresSpousalConsentForDistributionsClicked('${planDataConstants.YES_CODE}', '${uiConstants.YES}')" path="planDataUi.planData.requiresSpousalConsentForDistributions" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="return handleRequiresSpousalConsentForDistributionsClicked('${planDataConstants.NO_CODE}', '${uiConstants.NO}')" path="planDataUi.planData.requiresSpousalConsentForDistributions" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



<form:radiobutton disabled="${disableFields}" onclick="return handleRequiresSpousalConsentForDistributionsClicked('${planDataConstants.UNSPECIFIED_CODE}', '${uiConstants.UNSPECIFIED}')" path="planDataUi.planData.requiresSpousalConsentForDistributions" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}



              </c:when>
              <c:otherwise>
                ${planDataForm.planDataUi.requiresSpousalConsentForDistributionsDisplay}
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </div>
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="withdrawalsExtendedLabelColumn">Does the plan allow mandatory distributions (involuntary withdrawals)?</td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:checkbox path="planDataUi.planData.allowMandatoryDistributions" onclick="setDirtyFlag();" disabled="${disableFields}"/>


              </c:when>
              <c:otherwise>
                ${planDataForm.planDataUi.planData.allowMandatoryDistributions ? 'Yes' : 'No'}
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </div>
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="withdrawalsExtendedLabelColumn">Does the plan allow qualified birth or adoption distributions?</td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:checkbox path="planDataUi.planData.allowQualifiedBirthOrAdoptionDistribution" onclick="setDirtyFlag();" disabled="${disableFields}"/>


              </c:when>
              <c:otherwise>
                ${planDataForm.planDataUi.planData.allowQualifiedBirthOrAdoptionDistribution ? 'Yes' : 'No'}
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </div>
    <div class="subsubhead">
<c:if test="${userProfile.currentContract.bundledGaIndicator == true}">
	    	<content:getAttribute beanName="planWithdrawalReasonCarText" attribute="text"/>
</c:if>
<c:if test="${userProfile.currentContract.bundledGaIndicator != true}">
	    	<content:getAttribute beanName="planWithdrawalReasonTpaText" attribute="text"/>
</c:if>
    </div>
    <div class="evenDataRow">
      <c:forEach items="${withdrawalReasons}" var="withdrawalReason" varStatus="withdrawalReasonStatus">
        <div class="${(withdrawalReasonStatus.count % 2 == 0) ? 'rightColData' : 'leftColData'}">
          <div class="data"
            <c:if test="${withdrawalReason.withdrawalReasonCode == 'NE'}">
              id="withdrawalReasonNE" style="display: none"
            </c:if>
          >
            ${withdrawalReason.withdrawalReasonDescription}&nbsp;(${withdrawalReason.withdrawalReasonCode})
          </div>
        </div>
        <c:if test="${withdrawalReasonStatus.count % 2 == 0 or withdrawalReasonStatus.last}">
          <div class="endDataRowAndClearFloats"></div>
        </c:if>
      </c:forEach>
    </div>
    <div class="subsubhead">Retirement provisions</div>
    
    <div class="evenDataRow">
      <table class="dataTable">
<!-- retirement withdrawals -->     
        <tr>
          <td class="withdrawalsLabelColumn">
            <ps:fieldHilight name="planDataUi.retirementAge" singleDisplay="true" displayToolTip="true" className="errorIcon"/>            
            Retirement withdrawals are permitted when the employee reaches the following age
          </td>
          <c:choose>
            <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
              <td class="dataColumn" width="50"> 
<form:input path="planDataUi.retirementAge" disabled="${disableFields}" maxlength="4" onblur="validateNormalRetireAge(this)" onchange="setDirtyFlag();" size="2" cssClass="numericInput"/>






              </td>
            </c:when>
            <c:otherwise> <!-- view mode -->
         	  <td class="dataColumn" width="50">
              <c:choose>
	              <c:when test="${(empty planDataForm.planDataUi.retirementAge) && (empty planDataForm.planDataUi.earlyRetirementAge) }">
                	 &nbsp;
                	</td>
	              </c:when>
            	  <c:otherwise>
                	  ${planDataForm.planDataUi.retirementAgeDisplay}
              	  </c:otherwise>
              </c:choose>
              </td>
            </c:otherwise>
          </c:choose> 
        <td rowspan="2" style="padding-left:20px">
			<content:getAttribute beanName="retirementProvisions" attribute="text"/>
      	</td>              
        </tr>
        
<!-- early retirment withdrawal -->
      <c:if test="${planDataForm.planDataUi.planData.earlyRetirementAllowed}">
        <tr>
          <td class="withdrawalsLabelColumn">
              <ps:fieldHilight name="planDataUi.earlyRetirementAge" singleDisplay="true" displayToolTip="true" className="errorIcon"/>            
              Pre-retirement withdrawals are permitted when the employee reaches the following age
          </td>
          <c:choose>
            <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">          
              <td class="dataColumn">
<form:input path="planDataUi.earlyRetirementAge" disabled="${disableFields}" maxlength="4" onblur="validateEarlyRetireAge(this)" onchange="setDirtyFlag();" size="2" cssClass="numericInput"/>






              </td>
            </c:when>
            <c:otherwise> <!-- view mode -->
         	  <td class="dataColumn" width="50">
              <c:choose>
	              <c:when test="${empty planDataForm.planDataUi.earlyRetirementAge }">
                	 &nbsp;
                	</td>
	              </c:when>
            	  <c:otherwise>
                	  ${planDataForm.planDataUi.earlyRetirementAgeDisplay}
              	  </c:otherwise>
              </c:choose>
              </td>
            </c:otherwise>
          </c:choose>
        </tr>
      </c:if>
    </table>
    </div> 
    
    <c:if test="${planDataForm.planDataUi.planData.hardshipsAllowed}">
      <div class="subsubhead">Hardship provisions</div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="withdrawalsLabelColumn">Basis for hardship withdrawals</td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:radiobutton onclick="return handleHardshipProvisionClicked('${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_FACTS_AND_CIRCUMSTANCES}')" path="planDataUi.planData.hardshipWithdrawalProvisions" id="hardshipWithdrawalProvisionsFactsCircumstancesId" value="${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_FACTS_AND_CIRCUMSTANCES}"/>${uiConstants.FACTS_AND_CIRCUMSTANCES}



<form:radiobutton onclick="return handleHardshipProvisionClicked('${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_SAFE_HARBOR}')" path="planDataUi.planData.hardshipWithdrawalProvisions" id="hardshipWithdrawalProvisionsSafeHarborId" value="${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_SAFE_HARBOR}"/>${uiConstants.SAFE_HARBOR}



<form:radiobutton onclick="return handleHardshipProvisionClicked('${uiConstants.UNSPECIFIED_CODE}')" path="planDataUi.planData.hardshipWithdrawalProvisions" id="hardshipWithdrawalProvisionsUnspecifiedId" value="${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_UNSPECIFIED}"/>${uiConstants.UNSPECIFIED}


                </c:when>
                <c:otherwise>
                  ${planDataForm.planDataUi.hardshipWithdrawalProvisionsDisplay}
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
      
        <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="withdrawalsLabelColumn">Does the plan allow earnings from EEDEF hardship withdrawals?</td>
            <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
              <form:radiobutton onclick="return eedefEarningsAllowedIndClicked('${planDataConstants.YES_CODE}', '${uiConstants.YES}')"
               path="planDataUi.planData.eedefEarningsAllowedInd"   disabled="${disableFields}" id="eedefEarningsAllowedIndClicked"
               value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}
               
                            
                <form:radiobutton onclick="return eedefEarningsAllowedIndClicked('${planDataConstants.NO_CODE}', '${uiConstants.NO}')"
               path="planDataUi.planData.eedefEarningsAllowedInd"  disabled="${disableFields}" id="eedefEarningsAllowedIndClicked"
               value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}
               
                            
                 <form:radiobutton onclick="return eedefEarningsAllowedIndClicked('${planDataConstants.UNSPECIFIED_CODE}', '${uiConstants.UNSPECIFIED}')"
               path="planDataUi.planData.eedefEarningsAllowedInd"  disabled="${disableFields}" id="eedefEarningsAllowedIndClicked"
               value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}
               
              </c:when>
              <c:otherwise>
              <c:if test="${planDataForm.planDataUi.eedefEarningsAllowedIndDisplay == 'Y'}">
					Yes
				  </c:if>
				  <c:if test="${planDataForm.planDataUi.eedefEarningsAllowedIndDisplay == 'N'}">
					No
				 </c:if>
              </c:otherwise>
            </c:choose>
  
            </td>
          </tr>
        </table>
      </div>
    
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="withdrawalsLabelColumn">
              <ps:fieldHilight name="planDataUi.minimumHardshipAmount" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
              Minimum withdrawal amount
            </td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
$<form:input path="planDataUi.minimumHardshipAmount" disabled="${disableFields}" maxlength="14" onblur="validateMinWithdrawalAmount(this)" onchange="setDirtyFlag();" size="14" cssClass="numericInput"/>






                </c:when>
                <c:otherwise>
                  <fmt:formatNumber value="${planDataForm.planDataUi.planData.minimumHardshipAmount}" type="CURRENCY" currencyCode="USD"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="withdrawalsLabelColumn">
              <ps:fieldHilight name="planDataUi.maximumHardshipAmount" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
              Maximum withdrawal amount
            </td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
$<form:input path="planDataUi.maximumHardshipAmount" disabled="${disableFields}" maxlength="14" onblur="validateMaxWithdrawalAmount(this)" onchange="setDirtyFlag();" size="14" cssClass="numericInput"/>






                </c:when>
                <c:otherwise>
                  <fmt:formatNumber value="${planDataForm.planDataUi.planData.maximumHardshipAmount}" type="CURRENCY" currencyCode="USD"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
      <div class="subsubhead">
        Allowable money types for hardship withdrawals
        <ps:fieldHilight name="planDataUi.selectedAllowableMoneyTypesForWithdrawals" singleDisplay="true" displayToolTip="true"/>
      </div>
      <div class="evenDataRow">
        <c:set var="showQnecQmacText" value="false"/>
        <c:choose>
          <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
            <c:forEach items="${moneyTypes}" var="moneyType" varStatus="moneyTypeStatus">
              <div class="${(moneyTypeStatus.count % 2 == 0) ? 'rightColData' : 'leftColData'}">
                <div class="data">
                  <form:checkbox path="planDataUi.selectedAllowableMoneyTypesForWithdrawals" 
                                 value="${moneyType.id}" 
                                 onclick="setDirtyFlag();"
                                 disabled="${disableFields}"/>
                  ${moneyType.contractLongName}&nbsp;(${moneyType.contractShortName})
                  <c:if test="${(moneyType.id == planDataConstants.MONEY_TYPE_QMAC) or (moneyType.id == planDataConstants.MONEY_TYPE_QNEC)}">
                    *
                    <c:set var="showQnecQmacText" value="true"/>
                  </c:if>
                </div>
              </div>
              <c:if test="${moneyTypeStatus.count % 2 == 0 or moneyTypeStatus.last}">
                <div class="endDataRowAndClearFloats"></div>
              </c:if>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <c:choose>
              <c:when test="${empty planDataForm.planDataUi.planData.allowableMoneyTypesForWithdrawals}">
                <div class="data">
                  <content:getAttribute beanName="noHardshipMoneyTypesText" attribute="text"/>
                </div>
              </c:when>
              <c:otherwise>
                <c:forEach items="${planDataForm.planDataUi.planData.allowableMoneyTypesForWithdrawals}" var="moneyType" varStatus="moneyTypeStatus">
                  <div class="${(moneyTypeStatus.count % 2 == 0) ? 'rightColData' : 'leftColData'}">
                    <div class="data">
                      <ps:displayDescription collection="${moneyTypes}" keyName="id" keyValue="contractLongName" key="${moneyType}"/>&nbsp;(<ps:displayDescription collection="${moneyTypes}" keyName="id" keyValue="contractShortName" key="${moneyType}"/>)
                      <c:if test="${(moneyType == planDataConstants.MONEY_TYPE_QMAC) or (moneyType == planDataConstants.MONEY_TYPE_QNEC)}">
                        *
                        <c:set var="showQnecQmacText" value="true"/>
                      </c:if>
                    </div>
                  </div>
                  <c:if test="${moneyTypeStatus.count % 2 == 0 or moneyTypeStatus.last}">
                    <div class="endDataRowAndClearFloats"></div>
                  </c:if>
                </c:forEach>
              </c:otherwise>
            </c:choose>
          </c:otherwise>
        </c:choose>
        <div class="endDataRowAndClearFloats"></div>
        <c:if test="${showQnecQmacText}">
          <div class="data">
            <content:getAttribute beanName="hardshipWithdrawalQmacQnecMoneyTypeText" attribute="text"/>
          </div>
        </c:if>
      </div>
    </c:if>    

<!-- forms of distribution -->    
    <div class="subsubhead">
      Forms of Distribution
    </div>
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td style="border-left-width: 0; class="dataColumn">
<form:checkbox path="planDataUi.planData.withdrawalDistributionMethod.lumpSumIndicator" onclick="setDirtyFlag();" disabled="${disableFields}"/>


                Lump sum
<form:checkbox path="planDataUi.planData.withdrawalDistributionMethod.installmentsIndicator" onclick="setDirtyFlag();" disabled="${disableFields}"/>


                Installments
<form:checkbox path="planDataUi.planData.withdrawalDistributionMethod.annuityIndicator" onclick="setDirtyFlag();" disabled="${disableFields}"/>


                Annuity
         <!-- partial withdrawal -->              
              <script type="text/javascript">
                    $(document).ready(function() {
                        partialWithdrawalId = "#planData_withdrawalDistributionMethod_partialWithdrawalIndicator";
                        var minWithdrawalAmtInputId = "#planData_withdrawalDistributionMethod_minWithdrawalAmount";

                        if (! $(partialWithdrawalId).is(":checked")) {
                          $(minWithdrawalAmtInputId).val(""); 
                          $(minWithdrawalAmtInputId).prop("disabled", true);
                        }

                        $(partialWithdrawalId).on("click",function() {
                           if ($(this).is(':checked')) { 
                              $(minWithdrawalAmtInputId).prop("disabled", false);
                           } else {
                              $(minWithdrawalAmtInputId).val(""); 
                              $(minWithdrawalAmtInputId).prop("disabled", true);
                           }
                      });
                    });
              </script>                
<form:checkbox path="planDataUi.planData.withdrawalDistributionMethod.partialWithdrawalIndicator" id="planData_withdrawalDistributionMethod_partialWithdrawalIndicator" onclick="setDirtyFlag();" disabled="${disableFields}"/>



         <!-- minimum withdrawal amount -->
                <c:choose>
                      <c:when test="${planDataForm.editMode or planDataForm.confirmMode}"> 
                        Partial withdrawal, minimum $
<form:input path="planDataUi.partialWithdrawalMinimumWithdrawalAmount" disabled="${disableFields}" onchange="setDirtyFlag(); validatePartialWithdrawalMinimumAmount(this);" size="8" cssClass="numericInput" id="planData_withdrawalDistributionMethod_minWithdrawalAmount"/>





	                  </c:when>
                      <c:otherwise>
  	                    <c:choose>
		                  <c:when test="${empty planDataForm.planDataUi.partialWithdrawalMinimumWithdrawalAmount or planDataForm.planDataUi.planData.withdrawalDistributionMethod.minWithdrawalAmount == 0}">
		                    Partial withdrawal
		                  </c:when>
		                  <c:otherwise>
		                    Partial withdrawal, minimum
		                    <fmt:formatNumber type="CURRENCY" minFractionDigits="0" maxFractionDigits="0" value="${planDataForm.planDataUi.planData.withdrawalDistributionMethod.minWithdrawalAmount}"/>
		                  </c:otherwise>
		                </c:choose>
                      </c:otherwise>
                </c:choose>   
<form:checkbox path="planDataUi.planData.withdrawalDistributionMethod.otherIndicator" onclick="setDirtyFlag();" disabled="${disableFields}"/>


                Other
               <br><br><content:getAttribute id="applyFeeInformation404a5DisclosurePurposeForWithdrawals" attribute="text"/>
           </td>
        </tr>
      </table>
    </div>    
  </div> 
  
</div>
