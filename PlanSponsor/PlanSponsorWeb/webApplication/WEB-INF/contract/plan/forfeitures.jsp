<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="scheduleConstants" className="com.manulife.pension.service.contract.valueobject.VestingSchedule" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_FORFEITURES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="forfeituresText"/>

<div id="forfeituresTabDivId" class="borderedDataBox">
  <div class="subhead">
    <c:if test="${not param.printFriendly}">
      <div class="expandCollapseIcons" id="forfeituresShowIconId" onclick="expandDataDiv('forfeitures');">
        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
      </div>
      <div class="expandCollapseIcons" id="forfeituresHideIconId" onclick="collapseDataDiv('forfeitures');">
        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
      </div>
    </c:if>
    <div class="sectionTitle">
      <content:getAttribute beanName="forfeituresText" attribute="text"/>
    </div>
    <div class="sectionHighlightIcon" id="forfeituresSectionHighlightIconId">
      <ps:sectionHilight name="${uiConstants.SECTION_FORFEITURES}" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
    </div>
    <div class="endDataRowAndClearFloats"></div>
  </div>
  <div id="forfeituresDataDivId">
    <div class="evenDataRow">
      <div class="boldLabel">
        <div class="unvestedOptionsColumnOne"><div class="data">Options Available</div></div>
        <div class="unvestedOptionsColumnTwo"><div class="data">Select</div></div>
        <div class="unvestedOptionsColumnThree"><div class="data">Default</div></div>
      </div>
    </div>
    <c:forEach items="${optionsForUnvestedAmounts}" var="unvestedOption" varStatus="unvestedOptionStatus">
      <div class="${(unvestedOptionStatus.count % 2 == 0) ? 'evenDataRow' : 'oddDataRow'}">
        <div class="unvestedOptionsColumnOne">
          <div class="data">${unvestedOption.description}</div>
        </div>
        <div class="unvestedOptionsColumnTwo">
          <div class="data">
            <c:choose>
              <c:when test="${planDataForm.viewMode or planDataForm.confirmMode}">
                <form:checkbox path="planDataUi.selectedUnvestedMoneyOptions"
                               value="${unvestedOption.code}"
                               disabled="true"/>
              </c:when>
              <c:otherwise>
                <form:checkbox path="planDataUi.selectedUnvestedMoneyOptions"
                               value="${unvestedOption.code}"
                               id="unvestedMoneyOption[${unvestedOptionStatus.index}]"
                               onclick="handleSelectedUnvestedMoneyOptionClicked(this, ${unvestedOptionStatus.index})"
                               disabled ="${planDataForm.planDataUi.planData.defaultUnvestedMoneyOption == unvestedOption.code}"/>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
        <div class="unvestedOptionsColumnThree">
          <div class="data">
            <c:choose>
              <c:when test="${planDataForm.viewMode or planDataForm.confirmMode}">
<form:radiobutton disabled="true" path="planDataUi.planData.defaultUnvestedMoneyOption" value="${unvestedOption.code}"/>


              </c:when>
              <c:otherwise>
                <c:set var="disabled" value="true" scope="page"/>
                <c:forEach items="${planDataForm.planDataUi.planData.unvestedMoneyOptions}" var="option" >
                  <c:if test="${option == unvestedOption.code}">
                    <c:set var="disabled" value="false" scope="page"/>
                  </c:if>
                </c:forEach>
				<c:set var="disabledValue" value="${disabled}" />
<form:radiobutton disabled="${disabledValue}" onclick="handleDefaultUnvestedMoneyOptionClicked(this, ${unvestedOptionStatus.index})" path="planDataUi.planData.defaultUnvestedMoneyOption" id="unvestedMoneyOptionDefault[${unvestedOptionStatus.index}]" value="${unvestedOption.code}"/>




              </c:otherwise>
            </c:choose>
            <div class="endDataRowAndClearFloats"></div>
          </div>
        </div>
      </div>
    </c:forEach>
    <c:if test="${planDataForm.editMode}">
      <form:checkbox path="planDataUi.selectedUnvestedMoneyOptions"
                     value="${planDataForm.planDataUi.planData.defaultUnvestedMoneyOption}"
                     id="defaultUnvestedMoneyOptionId"
                     cssClass="hidden"
                     disabled ="false"/>
    </c:if>
  </div>
</div>
