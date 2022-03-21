<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
       
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<un:useConstants scope="request" var="renderConstants" className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_INFO}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="headerText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_GENERAL}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="generalText"/>
<c:set scope="request" var="datePattern" value="${renderConstants.MEDIUM_MDY_SLASHED}"/>

<div class="header">
  <content:getAttribute beanName="headerText" attribute="text"/>
</div>
<div id="generalTabDivId" class="borderedDataBox">
  <div class="subhead">
    <c:if test="${not param.printFriendly}">
      <div class="expandCollapseIcons" id="generalShowIconId" onclick="expandDataDiv('general');">
        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
      </div>
      <div class="expandCollapseIcons" id="generalHideIconId" onclick="collapseDataDiv('general');">
        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
      </div>
    </c:if>
    <div class="sectionTitle">
      <content:getAttribute beanName="generalText" attribute="text"/>
    </div>
    <div class="sectionHighlightIcon" id="generalSectionHighlightIconId">
      <ps:sectionHilight name="${uiConstants.SECTION_GENERAL}" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
    </div>
    <div class="endDataRowAndClearFloats"></div>
  </div>
  <div id="generalDataDivId">
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelColumn">
            <ps:fieldHilight name="planDataUi.planData.planName" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
            Plan name
          </td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.planData.planName" disabled="${disableFields}" maxlength="${planDataConstants.PLAN_NAME_MAX_LENGTH}" onblur="validatePlanName(this);" onchange="handlePlanNameChanged(this);" size="30"/>





              </c:when>
              <c:otherwise>
                ${planDataForm.planDataUi.planData.planName}
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </div>
    <div class="oddDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelColumn">Plan type</td>
          <td class="dataColumn">${planDataForm.planDataUi.planData.planTypeDescription}</td>
        </tr>
      </table>
    </div>
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelColumn">
            <ps:fieldHilight name="planDataUi.planData.employerTaxIdentificationNumber" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
            Employer tax identification number
          </td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.employerTaxIdentificationNumberPart1" disabled="${disableFields}" maxlength="2" onblur="handleEin1Blur(this);" onkeyup="return autoTab(this, 2, event);" size="2"/>





                &minus;
<form:input path="planDataUi.employerTaxIdentificationNumberPart2" disabled="${disableFields}" maxlength="7" onblur="validateEin2(this)" onchange="setDirtyFlag();" size="7"/>





              </c:when>
              <c:otherwise>
                ${planDataForm.planDataUi.employerTaxIdentificationNumberPart1}
                <c:if test="${not empty planDataForm.planDataUi.planData.employerTaxIdentificationNumber}">
                &nbsp;&minus;&nbsp;
                </c:if>
                ${planDataForm.planDataUi.employerTaxIdentificationNumberPart2}
              </c:otherwise>
            </c:choose>
            <span class="secondaryLabel">
              Plan number
              <ps:fieldHilight name="planDataUi.planData.planNumber" singleDisplay="true" displayToolTip="true"/>
            </span>
            <span class="secondaryData">
              <c:choose>
                <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.planData.planNumber" disabled="${disableFields}" maxlength="3" onblur="validatePlanNumber(this)" onchange="setDirtyFlag();" size="3"/>





                </c:when>
                <c:otherwise>
                  ${planDataForm.planDataUi.planData.planNumber}
                </c:otherwise>
              </c:choose>
            </span>
          </td>
        </tr>
      </table>
    </div>
    <div class="oddDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelColumn">
            <ps:fieldHilight name="planDataUi.planEffectiveDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
            Plan effective date
          </td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:input path="planDataUi.planEffectiveDate" disabled="${disableFields}" maxlength="10" onblur="validatePlanEffectiveDate(this)" onchange="setDirtyFlag();" id="planEffectiveDateId"/>





                <c:if test="${planDataForm.editMode}">
                  <img onclick="return handleDateIconClicked(event, 'planEffectiveDateId');" src="/assets/unmanaged/images/cal.gif" border="0">
                </c:if>
              </c:when>
              <c:otherwise>
                <fmt:formatDate value="${planDataForm.planDataUi.planData.planEffectiveDate}" type="DATE" pattern="${datePattern}"/>
              </c:otherwise>
            </c:choose>
            <c:if test="${planDataForm.editMode or planDataForm.confirmMode or not empty planDataForm.planDataUi.planData.planEffectiveDate}">
              (mm&frasl;dd&frasl;yyyy)
            </c:if>
          </td>
        </tr>
      </table>
    </div>
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelColumn">Plan year end</td>
          <td class="dataColumn">
            ${planDataForm.planDataUi.planData.planYearEnd.data}
            (mm&frasl;dd)
          </td>
        </tr>
      </table>
    </div>
   	<div class="oddDataRow">
     	<table class="dataTable">
	    	<tr>
	        	<td class="generalLabelColumn">Contract holder name</td>
		        	<td class="dataColumn">
		        		<c:if test="${not empty planDataForm.planDataUi.planData.contractHolderName.firstName}">${fn:trim(planDataForm.planDataUi.planData.contractHolderName.firstName)}</c:if><c:if test="${not empty planDataForm.planDataUi.planData.contractHolderName.lastName}">&nbsp;${planDataForm.planDataUi.planData.contractHolderName.lastName}</c:if>
		         	</td>
	         </tr>
      </table>
     </div>
     <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelColumn">Contract issued state</td>
          <td class="dataColumn">${planDataForm.planDataUi.planData.contractIssuedStateName}</td>
        </tr>
      </table>
    </div>
    <div class="oddDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelColumn">Contract effective date</td>
          <td class="dataColumn">
            <fmt:formatDate value="${planDataForm.planDataUi.planData.contractEffectiveDate}" type="DATE" pattern="${datePattern}"/>
            (mm&frasl;dd&frasl;yyyy)
          </td>
        </tr>
      </table>
    </div>
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelColumn">Company name</td>
          <td class="dataColumn">${planDataForm.planDataUi.planData.companyName}</td>
        </tr>
      </table>
    </div>
    <div class="oddDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelColumn">Industry type</td>
          <td class="dataColumn">${planDataForm.planDataUi.planData.industryType}</td>
        </tr>
      </table>
    </div>
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelColumn">
            <ps:fieldHilight name="planDataUi.planData.entityTypeOtherDescription" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
            Type of entity
          </td>
          <td class="dataColumn">
            <%-- For view mode, if entity type is other and a description exists - use it - otherwise display the entity type --%>
            <c:choose>
              <c:when test="${planDataForm.viewMode && planDataForm.planDataUi.planData.entityTypeCode == planDataConstants.ENTITY_TYPE_OTHER_CODE && not empty planDataForm.planDataUi.planData.entityTypeOtherDescription}">
                ${planDataForm.planDataUi.planData.entityTypeOtherDescription}
              </c:when>
              <c:otherwise>
                ${planDataForm.planDataUi.planData.entityType}
              </c:otherwise>
            </c:choose>
            <c:if test="${(planDataForm.editMode or planDataForm.confirmMode) && planDataForm.planDataUi.planData.entityTypeCode == planDataConstants.ENTITY_TYPE_OTHER_CODE}">
              (please describe):
<form:input path="planDataUi.planData.entityTypeOtherDescription" disabled="${disableFields}" maxlength="${planDataConstants.ENTITY_TYPE_OTHER_DESCRIPTION_MAX_LENGTH}" onblur="validateOtherEntityType(this)" onchange="handleEntityTypeOtherDescriptionChanged(this);" size="30"/>





            </c:if>
          </td>
        </tr>
      </table>
    </div>
    <div class="oddDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelExtendedColumn">Is there, or has there ever been a Safe Harbor election in place as per<br/>Section 401(k)(12) of the Internal Revenue Code?</td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">
<form:radiobutton disabled="${disableFields}" onclick="return handleIsSafeHarborPlanClicked('${planDataConstants.YES_CODE}');" path="planDataUi.planData.isSafeHarborPlan" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="return handleIsSafeHarborPlanClicked('${planDataConstants.NO_CODE}');" path="planDataUi.planData.isSafeHarborPlan" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



<form:radiobutton disabled="${disableFields}" onclick="return handleIsSafeHarborPlanClicked('${planDataConstants.UNSPECIFIED_CODE}');" path="planDataUi.planData.isSafeHarborPlan" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}



              </c:when>
              <c:otherwise>
                ${planDataForm.planDataUi.isSafeHarborPlanDisplay}
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </div>

    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="generalLabelExtendedColumn">
          <ps:fieldHilight name="planDataUi.planData.intendsToMeetIrcQualifiedAutomaticContributionArrangement" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
          Does the plan include a 'Qualified Automatic Contribution Arrangement (QACA)'?
          </td>
          <td class="dataColumn">
            <c:choose>
              <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">                           
<form:radiobutton disabled="${disableFieldsForAutoOrSignUp}" onclick="validateDefaultDeferralPercentageForAutomaticEnrollment($('#planDataUi_deferralPercentageForAutomaticEnrollment')[0], true)" path="planDataUi.planData.intendsToMeetIrcQualifiedAutomaticContributionArrangement" id="planDataUi_planData_intendsToMeetIrcQualifiedAutomaticContributionArrangement" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}




<form:radiobutton disabled="${disableFieldsForAutoOrSignUp}" path="planDataUi.planData.intendsToMeetIrcQualifiedAutomaticContributionArrangement" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}


<form:radiobutton disabled="${disableFieldsForAutoOrSignUp}" path="planDataUi.planData.intendsToMeetIrcQualifiedAutomaticContributionArrangement" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}


              </c:when>
              <c:otherwise>
                ${planDataForm.planDataUi.intendsToMeetIrcQualifiedAutomaticContributionArrangementDisplay}
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </div>

    <!-- As part of Contact Management Project June 2010 For Plan Info, Address Section is removed from this page-->
  </div>
</div>
