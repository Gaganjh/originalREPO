<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_OTHER_PLAN_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="otherPlanInformationText"/>

<script type="text/javascript">
function onQdiaRestrictionImposedChanged() {
  <c:choose>
    <c:when test="${planDataForm.editMode}"> 
    var value = $("input[name='planDataUi.planData.qdiaRestrictionImposed']:checked").val();
    if (value == "${planDataConstants.YES_CODE}") {
      $("textarea[name='planDataUi.planData.qdiaRestrictionDetails']").prop('disabled', false);
    } else {
      $("textarea[name='planDataUi.planData.qdiaRestrictionDetails']").val('');
      $("textarea[name='planDataUi.planData.qdiaRestrictionDetails']").prop('disabled', true);
    }
    </c:when>
    <c:otherwise>
      <c:if test="${planDataForm.planDataUi.planData.qdiaRestrictionImposed != planDataConstants.YES_CODE}"> 
      $("#qdiaRestrictionDetailsDiv").hide();
      </c:if>
    </c:otherwise>
  </c:choose>
}

$(document).ready(function() {
  <c:if test="${not planDataForm.confirmMode}"> 
  onQdiaRestrictionImposedChanged();
  </c:if>
});

</script>

<div id="otherPlanInformationTabDivId" class="borderedDataBox">
  <div class="subhead">
    <c:if test="${not param.printFriendly}">
      <div class="expandCollapseIcons" id="otherPlanInformationShowIconId" onclick="expandDataDiv('otherPlanInformation');">
        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
      </div>
      <div class="expandCollapseIcons" id="otherPlanInformationHideIconId" onclick="collapseDataDiv('otherPlanInformation');">
        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
      </div>
    </c:if>
    <div class="sectionTitle">
      <content:getAttribute beanName="otherPlanInformationText" attribute="text"/>
    </div>
    <div class="sectionHighlightIcon" id="otherPlanInformationSectionHighlightIconId">
      <ps:sectionHilight name="${uiConstants.SECTION_OTHER_PLAN_INFORMATION}" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
    </div>
    <div class="endDataRowAndClearFloats"></div>
  </div>
  <div id="otherPlanInformationDataDivId">
<%-- permitted dispartiy --%>  
    <div class="evenDataRow">
      <table class="dataTable">
        <tr>
          <td class="otherPlanInformationLabelColumn">Does the plan provide for permitted disparity?</td>
          <td class="dataColumn">
            ${planDataForm.planDataUi.allowPermittedDisparityDisplay}
          </td>
        </tr>
      </table>
    </div>
  
    <div class="evenDataRow">  
    	<table class="dataTable">   
            <tr>
              <td class="otherPlanInformationLabelColumn"> 
                 <ps:fieldHilight name="planDataUi.planData.individualSecuritiesInvestmentAllowed" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
                  Does the plan permit investments in individual securities?
              </td>
              <td class="dataColumn">
                <c:choose>
                  <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">                           
<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.individualSecuritiesInvestmentAllowed" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}


<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.individualSecuritiesInvestmentAllowed" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}


<form:radiobutton disabled="${disableFields}" path="planDataUi.planData.individualSecuritiesInvestmentAllowed" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}


                  </c:when>
                  <c:otherwise>
                    ${planDataForm.planDataUi.individualSecuritiesInvestmentAllowedDisplay}
                  </c:otherwise>
                </c:choose>
              </td>
        </tr> 
      </table>
    </div>
    <div class="evenDataRow">  
    	<table class="dataTable">   
            <tr>
              <td class="otherPlanInformationLabelColumn" valign="top"> 
                 <ps:fieldHilight name="planDataUi.planData.qdiaRestrictionImposed" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
                  Does the plan impose any restrictions, fees or expenses (e.g. surrender charge,
                  exchange fee, or redemption fee) on transferring or withdrawing from the QDIA after the initial 90-day period
                  (as per QDIA regulations S2550.404c-5(c)(5)(iii))?
              </td>
              <td class="dataColumn" valign="top">
                <c:choose>
                  <c:when test="${planDataForm.editMode or planDataForm.confirmMode}">                           
<form:radiobutton disabled="${disableFields}" onclick="onQdiaRestrictionImposedChanged()" path="planDataUi.planData.qdiaRestrictionImposed" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="onQdiaRestrictionImposedChanged()" path="planDataUi.planData.qdiaRestrictionImposed" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



<form:radiobutton disabled="${disableFields}" onclick="onQdiaRestrictionImposedChanged()" path="planDataUi.planData.qdiaRestrictionImposed" value="${planDataConstants.UNSPECIFIED_CODE}"/>${uiConstants.UNSPECIFIED}



                  </c:when>
                  <c:otherwise>
                    ${planDataForm.planDataUi.qdiaRestrictionImposedDisplay}
                  </c:otherwise>
                </c:choose>
              </td>
        </tr> 
      </table>
    </div>
    <div class="evenDataRow" id="qdiaRestrictionDetailsDiv">  
    	<table class="dataTable">   
            <tr>
              <td valign="top" class="otherPlanInformationLabelColumn"> 
                 <ps:fieldHilight name="planDataUi.planData.qdiaRestrictionDetails" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If yes, specify the restrictions, fees and/or expenses. This description<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;will appear on initial QDIA notifications.
              </td>
              <td class="dataColumn" valign="top">
                <c:choose>
                  <c:when test="${planDataForm.editMode or planDataForm.confirmMode}"> 
<form:textarea path="planDataUi.planData.qdiaRestrictionDetails" cols="40" disabled="${disableFields}" rows="5"/>

                  </c:when>
                  <c:otherwise>
                    <!-- Use c:out to escape any HTML element -->
                    <c:out value="${planDataForm.planDataUi.planData.qdiaRestrictionDetails}"/>
                  </c:otherwise>
                </c:choose>
              </td>
        </tr> 
      </table>
    </div>
  </div>

</div>

