<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
contentId="${contentConstants.FEE_INFORMATION_FOR_404A5_DISCLOSURE_PURPOSES_FOR_LOANS}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="applyFeeInformation404a5DisclosurePurposeForLoans" />
  
<script language="javascript">
function openLoanFeatures() {
  PDFWindow('/do/onlineloans/features/?task=print&printFriendly=true');
}

function onLoansAllowedChanged() {
  var enable = false;
  var loansAllowed = $("input[name='planDataUi.planData.loansAllowed']:checked").val();
  var textFieldNames = new Array("planDataUi.maximumAmortizationPeriodGeneralPurpose",
                                 "planDataUi.maximumAmortizationPeriodHardship",
                                 "planDataUi.maximumAmortizationPeriodPrimaryResidence",
                                 "planDataUi.minimumLoanAmount",
                                 "planDataUi.maximumLoanAmount",
                                 "planDataUi.maximumLoanPercentage",
                                 "planDataUi.loanInterestRateAbovePrime");
  var maximumNumberofOutstandingLoansSelectId = "#planDataUi_maximumNumberofOutstandingLoansSelect";
  var selectedAllowableMoneyTypesForLoansName = "planDataUi.selectedAllowableMoneyTypesForLoans";
  
  if (loansAllowed == "${planDataConstants.YES_CODE}") {
    enable = true;
  }

  if (enable == false) {
	for (var i = 0; i < textFieldNames.length; i++) {
	  var name = textFieldNames[i];
	  $("input[name='" + name + "']").val("");
	  $("input[name='" + name + "']").prop("disabled", true);
	}    
    $(maximumNumberofOutstandingLoansSelectId).val("${planDataConstants.UNSELECTED_LOANS_ALLOWED}");
    $(maximumNumberofOutstandingLoansSelectId).prop("disabled", true);
    $("input[name='" + selectedAllowableMoneyTypesForLoansName + "']").each( function() {
      this.checked = false;
      this.disabled = true;
    });
  
  } else {
	for (var i = 0; i < textFieldNames.length; i++) {
	  var name = textFieldNames[i];
	  $("input[name='" + name + "']").prop("disabled", false);
	}    
    $(maximumNumberofOutstandingLoansSelectId).prop("disabled", false);
    $("input[name='" + selectedAllowableMoneyTypesForLoansName + "']").prop("disabled", false);
  }
}

$(document).ready(function() {
  <c:if test="${planDataForm.editMode}">
  onLoansAllowedChanged();
  </c:if>
});

</script>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_LOANS}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="loansText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_JH_DOES_NOT_DO_RECORD_KEEPING_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="jhDoesNotDoRecordKeepingText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_NO_LOAN_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noLoanMoneyTypesText"/>

<div id="loansTabDivId" class="borderedDataBox">
  <div class="subhead">
    <c:if test="${not param.printFriendly}">
      <div class="expandCollapseIcons" id="loansShowIconId" onclick="expandDataDiv('loans');">
        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
      </div>
      <div class="expandCollapseIcons" id="loansHideIconId" onclick="collapseDataDiv('loans');">
        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
      </div>
    </c:if>
    <div class="sectionTitle" >
		  <content:getAttribute beanName="loansText" attribute="text"/>
	</div>	
    <c:if test="${planDataForm.allowOnlineLoans}">
	  <div style="text-align:right">
        <c:if test="${not param.printFriendly}">
<a href="#" onclick="javascript:openLoanFeatures();" style="color:#ffffff">Loan features at a glance</a>
		</c:if>
        <c:if test="${param.printFriendly}">
          Loan features at a glance
        </c:if>
	  </div> 
    </c:if>

    <div class="sectionHighlightIcon" id="loansSectionHighlightIconId">
      <ps:sectionHilight name="${uiConstants.SECTION_LOANS}" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
    </div>
    <div class="endDataRowAndClearFloats"></div>
  </div>
  <div id="loansDataDivId">
    <div class="subsubhead">
      General loan provisions    
    </div>
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="loansLabelColumn">
            <ps:fieldHilight name="planDataUi.planData.loansAllowed" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
            Does the Plan allow loans?
          </td>
          <td class="dataColumn">
	          <c:choose>
	            <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:radiobutton disabled="${disableFields}" onclick="onLoansAllowedChanged()" path="planDataUi.planData.loansAllowed" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="onLoansAllowedChanged()" path="planDataUi.planData.loansAllowed" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



<form:radiobutton disabled="${disableFields}" onclick="onLoansAllowedChanged()" path="planDataUi.planData.loansAllowed" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}



	            </c:when>
	            <c:otherwise>
	              ${planDataForm.planDataUi.loansAllowedDisplay}
	            </c:otherwise>
	          </c:choose>
            <c:if test="${planDataForm.planDataUi.planData.loansAllowed == planDataConstants.YES_CODE}">
              <c:if test="${not planDataForm.planDataUi.planData.johnHancockDoesRecordKeeping}">
                <content:getAttribute beanName="jhDoesNotDoRecordKeepingText" attribute="text"/>
              </c:if>
            </c:if>
          </td>
        </tr>
      </table>
    </div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="loansLabelColumn">Does the plan require spousal consent for distributions?</td>
            <td class="dataColumn">
              <span id="loanSpousalConsentSpanId">
                ${planDataForm.planDataUi.requiresSpousalConsentForDistributionsDisplay}
              </span>
            </td>
          </tr>
        </table>
      </div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="loansLabelColumn" valign="top" rowspan="3">Maximum amortization period per loan type</td>
            <td class="dataColumn">
              <div class="amortizationLabelColumn">
                <ps:fieldHilight name="planDataUi.maximumAmortizationPeriodGeneralPurpose" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
                <ps:displayDescription collection="${loanTypes}" keyName="code" keyValue="description" key="${planDataConstants.LOAN_TYPE_GENERAL_PURPOSE}"/>
              </div>
              <div class="amortizationDataColumn">
                <c:choose>
                  <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.maximumAmortizationPeriodGeneralPurpose" disabled="${disableFields}" maxlength="1" onblur="validateMaximumAmortizationGeneralPurpose(this)" onchange="setDirtyFlag();" size="5" cssClass="numericInput"/>






                  </c:when>
                  <c:otherwise>
                    ${planDataForm.planDataUi.planData.maximumAmortizationPeriodGeneralPurpose}
                  </c:otherwise>
                </c:choose>
              </div>
              <div class="amortizationSuffixColumn">
                <c:if test="${planDataForm.editMode or planDataForm.confirmMode or not empty planDataForm.planDataUi.planData.maximumAmortizationPeriodGeneralPurpose}">
                  &nbsp;years
                </c:if>
              </div>
            </td>
          </tr>
          <tr>
            <td class="dataColumn">
              <div class="amortizationLabelColumn">
                <ps:fieldHilight name="planDataUi.maximumAmortizationPeriodHardship" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
                <ps:displayDescription collection="${loanTypes}" keyName="code" keyValue="description" key="${planDataConstants.LOAN_TYPE_HARDSHIP}"/>
              </div>
              <div class="amortizationDataColumn">
                <c:choose>
                  <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.maximumAmortizationPeriodHardship" disabled="${disableFields}" maxlength="1" onblur="validateMaximumAmortizationHardship(this)" onchange="setDirtyFlag();" size="5" cssClass="numericInput"/>






                  </c:when>
                  <c:otherwise>
                    ${planDataForm.planDataUi.planData.maximumAmortizationPeriodHardship}
                  </c:otherwise>
                </c:choose>
              </div>
              <div class="amortizationSuffixColumn">
                <c:if test="${planDataForm.editMode or planDataForm.confirmMode or not empty planDataForm.planDataUi.planData.maximumAmortizationPeriodHardship}">
                  &nbsp;years
                </c:if>
              </div>
            </td>
          </tr>
          <tr>
            <td class="dataColumn">
              <div class="amortizationLabelColumn">
                <ps:fieldHilight name="planDataUi.maximumAmortizationPeriodPrimaryResidence" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
                <ps:displayDescription collection="${loanTypes}" keyName="code" keyValue="description" key="${planDataConstants.LOAN_TYPE_PRIMARY_RESIDENCE}"/>
              </div>
              <div>
              </div>
              <div class="amortizationDataColumn">
                <c:choose>
                  <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.maximumAmortizationPeriodPrimaryResidence" disabled="${disableFields}" maxlength="2" onblur="validateMaximumAmortizationPrimaryResidence(this)" onchange="setDirtyFlag();" size="5" cssClass="numericInput"/>






                  </c:when>
                  <c:otherwise>
                    ${planDataForm.planDataUi.planData.maximumAmortizationPeriodPrimaryResidence}
                  </c:otherwise>
                </c:choose>
              </div>
              <div class="amortizationSuffixColumn">
                <c:if test="${planDataForm.editMode or planDataForm.confirmMode or not empty planDataForm.planDataUi.planData.maximumAmortizationPeriodPrimaryResidence}">
                  &nbsp;years
                </c:if>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="loansLabelColumn">
              <ps:fieldHilight name="planDataUi.minimumLoanAmount" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
              Minimum loan amount
            </td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
$<form:input path="planDataUi.minimumLoanAmount" disabled="${disableFields}" maxlength="9" onblur="validateMinLoanAmount(this)" onchange="setDirtyFlag();" size="10" cssClass="numericInput"/>






                </c:when>
                <c:otherwise>
                  <fmt:formatNumber value="${planDataForm.planDataUi.planData.minimumLoanAmount}" type="CURRENCY" currencyCode="USD"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="loansLabelColumn">
              <ps:fieldHilight name="planDataUi.maximumLoanAmount" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
              Maximum loan amount
            </td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
$<form:input path="planDataUi.maximumLoanAmount" disabled="${disableFields}" maxlength="9" onblur="validateMaxLoanAmount(this)" onchange="setDirtyFlag();" size="10" cssClass="numericInput"/>






                </c:when>
                <c:otherwise>
                  <fmt:formatNumber value="${planDataForm.planDataUi.planData.maximumLoanAmount}" type="CURRENCY" currencyCode="USD"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="loansLabelColumn">
              <ps:fieldHilight name="planDataUi.maximumLoanPercentage" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
              Maximum loan percentage
            </td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.maximumLoanPercentage" disabled="${disableFields}" maxlength="7" onblur="validateMaxLoanPercent(this)" onchange="setDirtyFlag();" size="7" cssClass="numericInput"/>






                  %
                </c:when>
                <c:otherwise>
                  <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_LOAN_PERCENTAGE_SCALE}" value="${planDataForm.planDataUi.planData.maximumLoanPercentage}"/>${empty planDataForm.planDataUi.planData.maximumLoanPercentage ? '' : '%'}
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="loansLabelColumn">
              <ps:fieldHilight name="planDataUi.maximumNumberofOutstandingLoansSelect" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
              Maximum number of outstanding loans allowed
            </td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
 <form:select path="planDataUi.maximumNumberofOutstandingLoansSelect" id="planDataUi_maximumNumberofOutstandingLoansSelect" onchange="setDirtyFlag();" disabled="${disableFields}">



                    <c:if test="${planDataForm.planDataUi.maximumNumberofOutstandingLoansSelect == planDataConstants.UNSELECTED_LOANS_ALLOWED}">                               
                      <form:option value="${planDataConstants.UNSELECTED_LOANS_ALLOWED}">Select one</form:option>
                    </c:if>
                    <c:forEach begin="1" end="10" var="index">
                      <form:option value="${index}">${index}</form:option>
                    </c:forEach>
                    <form:option value="${planDataConstants.UNLIMITED_LOANS_ALLOWED}">Unlimited</form:option>
</form:select>
                </c:when>
                <c:otherwise>
                  <c:choose>
                    <c:when test="${planDataConstants.UNLIMITED_LOANS_ALLOWED == planDataForm.planDataUi.planData.maximumNumberofOutstandingLoans}">
                      ${planDataConstants.UNLIMITED}
                    </c:when>
                    <c:when test="${planDataConstants.UNSELECTED_LOANS_ALLOWED == planDataForm.planDataUi.planData.maximumNumberofOutstandingLoans}">
                      <%-- Unspecified --%>
                    </c:when>
                    <c:otherwise>
                      ${planDataForm.planDataUi.planData.maximumNumberofOutstandingLoans}
                    </c:otherwise>
                  </c:choose>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
      <div class="evenDataRow">
        <table class="dataTable">
          <tr>
            <td class="loansLabelColumn">
              <ps:fieldHilight name="planDataUi.loanInterestRateAbovePrime" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
              Loan interest rate above prime
            </td>
            <td class="dataColumn">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.loanInterestRateAbovePrime" disabled="${disableFields}" maxlength="5" onblur="validateLoanInterestRate(this)" onchange="setDirtyFlag();" size="5" cssClass="numericInput"/>






                  %
                </c:when>
                <c:otherwise>
                  <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_INTEREST_RATE_SCALE}" value="${planDataForm.planDataUi.planData.loanInterestRateAbovePrime}"/>${empty planDataForm.planDataUi.planData.loanInterestRateAbovePrime ? '' : '%'}
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </div>
      <div class="subsubhead">Allowable money types for loans</div>
      <div class="evenDataRow">
          <c:choose>
            <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
              <c:forEach items="${moneyTypes}" var="moneyType" varStatus="moneyTypeStatus">
                <div class="${(moneyTypeStatus.count % 2 == 0) ? 'rightColData' : 'leftColData'}">
                  <div class="data">
                    <form:checkbox path="planDataUi.selectedAllowableMoneyTypesForLoans" 
                                   value="${moneyType.id}" 
                                   onclick="setDirtyFlag();"
                                   disabled="${disableFields}"/>
                    ${moneyType.contractLongName}&nbsp;(${moneyType.contractShortName})
                  </div>
                </div>
                <c:if test="${moneyTypeStatus.count % 2 == 0 or moneyTypeStatus.last}">
                  <div class="endDataRowAndClearFloats"></div>
                </c:if>
              </c:forEach>
            </c:when>
            <c:otherwise>
            <c:choose>
              <c:when test="${empty planDataForm.planDataUi.planData.allowableMoneyTypesForLoans}">
                <div class="data">
                  <content:getAttribute beanName="noLoanMoneyTypesText" attribute="text"/>
                </div>
              </c:when>
              <c:otherwise>
              <div class="data">
                  <c:forEach items="${planDataForm.planDataUi.planData.allowableMoneyTypesForLoans}" var="moneyType" varStatus="moneyTypeStatus">
                    <div class="${(moneyTypeStatus.count % 2 == 0) ? 'rightColData' : 'leftColData'}">
                      <div class="data">
                        <ps:displayDescription collection="${moneyTypes}" keyName="id" keyValue="contractLongName" key="${moneyType}"/>&nbsp;(<ps:displayDescription collection="${moneyTypes}" keyName="id" keyValue="contractShortName" key="${moneyType}"/>)
                      </div>
                    </div>
                    <c:if test="${moneyTypeStatus.count % 2 == 0 or moneyTypeStatus.last}">
                      <div class="endDataRowAndClearFloats"></div>
                    </c:if>
                  </c:forEach>
              </div>   
                </c:otherwise>
              </c:choose>
            </c:otherwise>
          </c:choose>
          <br><content:getAttribute id="applyFeeInformation404a5DisclosurePurposeForLoans" attribute="text"/>
        <div class="endDataRowAndClearFloats"></div>
      </div>
  </div>
</div>
