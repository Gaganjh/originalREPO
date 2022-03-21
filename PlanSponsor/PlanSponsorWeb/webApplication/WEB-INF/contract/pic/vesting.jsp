<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<%@ page import="com.manulife.pension.content.valueobject.*" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="scheduleConstants" className="com.manulife.pension.service.contract.valueobject.VestingSchedule" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_VESTING}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="vestingText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_NO_VESTING_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noMoneyTypesText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_CALCULATE_VESTING_SELECTED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="calculateVestingSelected"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_CALCULATE_VESTING_NOT_SELECTED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="calculateVestingNotSelected"/>
  
<content:contentBean 
	contentId="80886" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="ERR_VESTING_SCHEDULE_YEAR_ZERO_CANNOT_BE_HUNDRAD_PERCENT" />
	
<script type="text/javascript">
   var ERR_VESTING_SCHEDULE_YEAR_ZERO_CANNOT_BE_HUNDRAD_PERCENT = "<content:getAttribute beanName='ERR_VESTING_SCHEDULE_YEAR_ZERO_CANNOT_BE_HUNDRAD_PERCENT' attribute='text'  filter='true' escapeJavaScript="true"/>"; 

function enableDisableVestingSchedule(enable) {
   if (enable == false) {
     for (var i = 0; i < ${fn:length(pifDataForm.planInfoVO.vesting.vestingSchedules)}; i++) {
       var vestingScheduleTypeSelectId = "#planDataUi_vestingSchedules_" + i + "_vestingSchedule_vestingScheduleType";
	   var vestingScheduleTypeSelectHiddenId = "#vestingSchedules_"+i+"_vestingScheduleTypeHiddenId";

       if ($(vestingScheduleTypeSelectId).length > 0) {	  
	       $(vestingScheduleTypeSelectId).val("");
		   $(vestingScheduleTypeSelectHiddenId).val("");
	       $(vestingScheduleTypeSelectId).prop("disabled", true);
	       handleVestingScheduleChanged($(vestingScheduleTypeSelectId)[0], i);
       }
     }
   } else {
     for (var i = 0; i < ${fn:length(pifDataForm.planInfoVO.vesting.vestingSchedules)}; i++) {
       var vestingScheduleTypeSelectId = "#planDataUi_vestingSchedules_" + i + "_vestingSchedule_vestingScheduleType";
       if ($(vestingScheduleTypeSelectId).length > 0) {
	       $(vestingScheduleTypeSelectId).prop("disabled", false);
	       handleVestingScheduleChanged($(vestingScheduleTypeSelectId)[0], i);
       }
     }
   }
}

$(document).ready(function() {
  var multipleVestingSchedulesForOneSingleMoneyType = $("input[name='planInfoVO.vesting.hasMultipleVestingSchedules']:checked").val();
  if (multipleVestingSchedulesForOneSingleMoneyType == "${planDataConstants.YES_CODE}") {
    enableDisableVestingSchedule(false);
  }
});

function validateVestingScheduleYearZero(field,year){
	if(year == '0'){
		if(field.value == '100'){
			alert(ERR_VESTING_SCHEDULE_YEAR_ZERO_CANNOT_BE_HUNDRAD_PERCENT);
			field.value = '';field.focus();field.select(); 
			return false;
		}else{
			return true;
		}
	}
}

function formatVestingPercentage(field) {
	var v = field.value;
	if (!isNaN(v)) {
		var index = v.indexOf(".");
		if (index == 0) {
			field.value = "0"+v ;
		}
	}
}

function handleWithdrawalReasonsClicked(field,count){
	setDirtyFlag();
	withdrawalReasonsHiddenId = document.getElementById("vesting_withdrawalReasons_selectedIndicator"+count);
	if(field.checked){ 
		withdrawalReasonsHiddenId.value = 'true';
	}else{
		withdrawalReasonsHiddenId.value = 'false';
	}
}

</script>

<div id="vestingTabDivId" class="borderedDataBox">
<!--start table content -->
<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
  <TBODY>
	<TR><TD class=subhead>	
		<DIV class=sectionTitle>
			<c:if test="${pifDataForm.confirmMode}">
				<content:getAttribute beanName="vestingText" attribute="text"/>
			</c:if>
		</DIV>
	</TD></TR>	  
	<TR>
    <TD width="100%">
	<!--[if lt IE 7]>
	<link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
	<![endif]-->
      <DIV class=evenDataRow>
      <TABLE width="100%" class=dataTable>
        <TBODY>
		<TR><TD class=subsubhead colspan="2">General vesting provisions</TD></TR>		
        <TR>
          <TD class=vestingAndForfeituresLabelColumn>100% vesting applies to 
            the following withdrawal reasons </TD>
          <TD class=dataColumn>
<c:forEach items="${pifDataForm.planInfoVO.vesting.withdrawalReasonsList}" var="withdrawalReasons" varStatus="count" >

				<input type="checkbox" 
					id="pifDataUi_planInfoVO_vesting_withdrawalReasonsList[${count.index}]_selectedIndicator" 
					name="planInfoVO.vesting.withdrawalReasonsList[${count.index}].selectedIndicator" 
					value="true"						
					onclick="handleWithdrawalReasonsClicked(this,${count.index})"
					<c:if test="${pifDataForm.confirmMode}"> disabled </c:if>
					<c:if test="${withdrawalReasons.selectedIndicator}"> checked="checked" </c:if> />
<form:hidden path="planInfoVO.vesting.withdrawalReasonsList[${count.index}].selectedIndicator" id="vesting_withdrawalReasons_selectedIndicator${count.index}" value="false"/>

				 ${withdrawalReasons.withdrawalReasonDescription}				
</c:forEach>
        	</TD>
		</TR>		
        <TR>
            <TD class=vestingAndForfeituresLabelColumn>Vesting service crediting 
            method </TD>
            <TD class=dataColumn>
				<c:choose>
				  <c:when test="${pifDataForm.editMode}">	
					  <c:forEach items="${vestingServiceCreditMethods}" var="method">
							<%-- Suppress unspecified if method --%>
						  <c:if test="${(method.code != planDataConstants.VESTING_SERVICE_CREDIT_METHOD_UNSPECIFIED)}">
<form:radiobutton disabled="${disableFields}" onclick="handleVestingServiceCreditMethodClicked('${method.code}')" path="planInfoVO.vesting.creditingMethod" value="${method.code}"/>${method.description}


						  </c:if>
					  </c:forEach>
				  </c:when>
				  <c:otherwise>								
<c:if test="${pifDataForm.planInfoVO.vesting.creditingMethod == 'H'}">Hours of service</c:if>

<c:if test="${pifDataForm.planInfoVO.vesting.creditingMethod == 'E'}">Elapsed time</c:if>

				  </c:otherwise>
				</c:choose>			  
			</TD>
		</TR>
        <TR>
          <TD class=vestingAndForfeituresLabelColumn>Vesting hours of service 
            (if applicable) </TD>
          <TD class=dataColumn>
				<c:choose>
				  <c:when test="${pifDataForm.editMode}">	
<form:input path="planInfoVO.vesting.hoursOfService" disabled="${(pifDataForm.planInfoVO.vesting.creditingMethod == planDataConstants.VESTING_SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE) ? 'false' : 'true'}" maxlength="4" onblur="validateHoursOfService(this)" onchange="setDirtyFlag();" size="4" cssClass="numericInput" id="hoursOfServiceTextId"/>







<form:hidden path="planInfoVO.vesting.hoursOfService" id="hoursOfServiceHiddenId" disabled="${(pifDataForm.planInfoVO.vesting.creditingMethod == planDataConstants.VESTING_SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE) ? 'true' : 'false'}" />


				  </c:when>
				  <c:otherwise>								
 <c:if test ="${not empty pifDataForm.planInfoVO.vesting.hoursOfService}" >
 ${pifDataForm.planInfoVO.vesting.hoursOfService}				  </c:if>

				  </c:otherwise>
				</c:choose>							   
		  </TD>
		</TR>
<%-- vesting computation period --%> 		
        <TR>
          <TD class=vestingAndForfeituresLabelColumn valign="top">The vesting computation period shall be...</TD>
          <TD class=dataColumn>
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">	
<form:radiobutton disabled="${disableFields}" path="planInfoVO.vesting.computationPeriod" value="${planDataConstants.VESTING_COMPUTATION_PERIOD_PLAN_YEAR_CODE}"/>${planDataConstants.VESTING_COMPUTATION_PERIOD_PLAN_YEAR_STRING}


				   <br>
<form:radiobutton disabled="${disableFields}" path="planInfoVO.vesting.computationPeriod" value="${planDataConstants.VESTING_COMPUTATION_BASED_ON_HOS_FIRST_AND_EACH_ANNIVERSARY_THEREOF_CODE}"/>the date an Employee first performs an Hour of Service<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;and each anniversary thereof


			  </c:when>
			  <c:otherwise>								
<c:if test="${pifDataForm.planInfoVO.vesting.computationPeriod =='P'}">the Plan Year</c:if>

<c:if test="${pifDataForm.planInfoVO.vesting.computationPeriod =='A'}">the date an Employee first performs an Hour of Service<br>and each anniversary thereof </c:if>

			  </c:otherwise>
			</c:choose>						   
		  </TD>
		</TR>
 <%-- multiple vesting schedules --%>		
        <TR>
          <TD class=vestingAndForfeituresLabelColumn valign="top">Does the plan have two or more vesting schedules for any single money type?</TD>
          <TD class=dataColumn>
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">	
<form:radiobutton disabled="${disableFields}" onclick="enableDisableVestingSchedule(false)" path="planInfoVO.vesting.hasMultipleVestingSchedules" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="enableDisableVestingSchedule(true)" path="planInfoVO.vesting.hasMultipleVestingSchedules" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}


			  </c:when>
			  <c:otherwise>								
<c:if test="${pifDataForm.planInfoVO.vesting.hasMultipleVestingSchedules =='Y'}">Yes</c:if>

<c:if test="${pifDataForm.planInfoVO.vesting.hasMultipleVestingSchedules =='N'}">No</c:if>

			  </c:otherwise>
			</c:choose>						   
		  </TD></TR></TBODY>
       </TABLE></DIV>
       
      <DIV>
      <TABLE width="100%" class=vestingSchedule>
        <THEAD class=evenDataRow>
		<TR><TD class=subsubhead colspan="9">Vesting Schedule</TD></TR>		
        <TR>
          <TH style="BORDER-LEFT-WIDTH: 0px">
          <TH>
          <TH style="BORDER-RIGHT-WIDTH: 0px" colSpan=8>Completed years of 
            service</TH></TR></THEAD>
        <THEAD class=evenDataRow>
        <TR>
          <TH style="BORDER-TOP-WIDTH: 1px; BORDER-LEFT-WIDTH: 0px">Money 
          Type</TH>
          <TH style="BORDER-TOP-WIDTH: 1px">Vesting Schedule</TH>
          <c:forEach begin="0" end="${scheduleConstants.YEARS_OF_SERVICE}" var="year" varStatus="yearStatus">
            <th style="border-right-width: ${yearStatus.last ? '0' : '1px'}; border-top-width: 1px;">${year}</th>
          </c:forEach>
		</TR></THEAD>
        <TBODY>
        <c:choose>
          <c:when test="${empty pifDataForm.planInfoVO.vesting.vestingSchedules}">
            <tr class="oddDataRow">
              <td class="textData" colspan="${scheduleConstants.YEARS_OF_SERVICE + 3}" style="border-left-width: 0; border-right-width: 0;">
                <content:getAttribute beanName="noMoneyTypesText" attribute="text"/>
              </td>
            </tr>
          </c:when>
          <c:otherwise>
			<c:set var="rowCount" value="0" scope="page" />		  
            <c:forEach items="${pifDataForm.planInfoVO.vesting.vestingSchedules}" var="vestingSchedule" varStatus="vestingScheduleStatus">
			<c:if test="${vestingSchedule.selectedMoneyType == 'true'}">
			<c:set var="rowCount" value="${rowCount + 1}" scope="page" />
              <tr class="${(rowCount % 2 == 0) ? 'evenDataRow' : 'oddDataRow'}">
                <td class="textData" style="border-left-width: 0;">
                  <span onmouseover="Tip('${vestingSchedule.moneyTypeLongName}&nbsp;(${vestingSchedule.moneyTypeShortName})')" onmouseout="UnTip()">
                    ${vestingSchedule.moneyTypeShortName}
                  </span>
                </td>
                <td class="textData" nowrap="nowrap">
                  <c:choose>
                    <c:when test="${pifDataForm.editMode}">
 <form:select path="planInfoVO.vesting.vestingSchedules[${vestingScheduleStatus.index}].vestingScheduleType" onchange="return handleVestingScheduleChanged(this, ${vestingScheduleStatus.index});" id="planDataUi_vestingSchedules_${vestingScheduleStatus.index}_vestingSchedule_vestingScheduleType" disabled="${disableFields}">



                        <form:option value="">Select one</form:option>
                        <form:option value="FV">Fully Vested</form:option>
                        <form:options items="${vestingSchedules}" itemValue="code" itemLabel="description"/>  
</form:select>
  

                    </c:when>
                    <c:otherwise>
<c:if test="${vestingSchedule.vestingScheduleType =='FV'}">Fully Vested</c:if>

<c:if test="${vestingSchedule.vestingScheduleType =='3YC'}">3 Yr Cliff</c:if>

<c:if test="${vestingSchedule.vestingScheduleType =='4YG'}">4 Yr Graded</c:if>

<c:if test="${vestingSchedule.vestingScheduleType =='5YG'}">5 Yr Graded</c:if>

<c:if test="${vestingSchedule.vestingScheduleType =='6YG'}">6 Yr Graded</c:if>

<c:if test="${vestingSchedule.vestingScheduleType =='CUS'}">Customized</c:if>

                    </c:otherwise>
                  </c:choose>
                </td>
                <c:forEach items="${vestingSchedule.schedules}" var="vestedAmount" varStatus="vestedAmountStatus">
                  <td class="numericData" style="border-right-width: ${vestedAmountStatus.last ? '0' : '1px'}" nowrap="nowrap">
                    <c:choose>
                      <c:when test="${pifDataForm.editMode}">
                        <c:choose>
                          <c:when test="${vestingSchedule.moneyTypeId == 'S/HGR' && vestingSchedule.vestingScheduleType != 'CUS'}">
<form:input path="planInfoVO.vesting.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" disabled="true" maxlength="7" onblur="validateVestingSchedulePercent(this, 'SHGR', '${vestedAmountStatus.index}');validateVestingScheduleYearZero(this,'${vestedAmountStatus.index}');formatVestingPercentage(this);" onchange="setDirtyFlag();" size="4" cssClass="numericInput" id="vestedAmount[${vestingScheduleStatus.index}][${vestedAmountStatus.index}]TextId"/>







                          </c:when>						
                          <c:when test="${vestingSchedule.moneyTypeId == 'S/HGR' && vestingSchedule.vestingScheduleType == 'CUS'}">
							<c:if test="${vestedAmountStatus.count == 7}">
<form:input path="planInfoVO.vesting.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" disabled="true" maxlength="7" onblur="validateVestingSchedulePercent(this, 'SHGR', '${vestedAmountStatus.index}');validateVestingScheduleYearZero(this,'${vestedAmountStatus.index}');formatVestingPercentage(this);" onchange="setDirtyFlag();" size="4" cssClass="numericInput" id="vestedAmount[${vestingScheduleStatus.index}][${vestedAmountStatus.index}]TextId"/>







							</c:if>
							<c:if test="${vestedAmountStatus.count != 7}">
<form:input path="planInfoVO.vesting.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" disabled="false" maxlength="7" onblur="validateVestingSchedulePercent(this, 'SHGR', '${vestedAmountStatus.index}');validateVestingScheduleYearZero(this,'${vestedAmountStatus.index}');formatVestingPercentage(this);" onchange="setDirtyFlag();" size="4" cssClass="numericInput" id="vestedAmount[${vestingScheduleStatus.index}][${vestedAmountStatus.index}]TextId"/>







							</c:if>
                          </c:when>
                          <c:when test="${vestingSchedule.vestingScheduleType == 'CUS'}">
							<c:if test="${vestedAmountStatus.count == 7}">
<form:input path="planInfoVO.vesting.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" disabled="true" maxlength="7" onblur="validateVestingSchedulePercent(this, '${fn:trim(vestingSchedule.moneyTypeId)}', '${vestedAmountStatus.index}');validateVestingScheduleYearZero(this,'${vestedAmountStatus.index}');formatVestingPercentage(this);" onchange="setDirtyFlag();" size="4" cssClass="numericInput" id="vestedAmount[${vestingScheduleStatus.index}][${vestedAmountStatus.index}]TextId"/>







							</c:if>
							<c:if test="${vestedAmountStatus.count != 7}">
<form:input path="planInfoVO.vesting.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" disabled="false" maxlength="7" onblur="validateVestingSchedulePercent(this, '${fn:trim(vestingSchedule.moneyTypeId)}', '${vestedAmountStatus.index}');validateVestingScheduleYearZero(this,'${vestedAmountStatus.index}');formatVestingPercentage(this);" onchange="setDirtyFlag();" size="4" cssClass="numericInput" id="vestedAmount[${vestingScheduleStatus.index}][${vestedAmountStatus.index}]TextId"/>







							</c:if>							
                          </c:when>						  
                          <c:otherwise>
<form:input path="planInfoVO.vesting.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" disabled="true" maxlength="7" onblur="validateVestingSchedulePercent(this, '${fn:trim(vestingSchedule.moneyTypeId)}', '${vestedAmountStatus.index}');validateVestingScheduleYearZero(this,'${vestedAmountStatus.index}');formatVestingPercentage(this);" onchange="setDirtyFlag();" size="4" cssClass="numericInput" id="vestedAmount[${vestingScheduleStatus.index}][${vestedAmountStatus.index}]TextId"/>







                          </c:otherwise>
                        </c:choose>
<form:hidden path="planInfoVO.vesting.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" id="vestedAmount[${vestingScheduleStatus.index}][${vestedAmountStatus.index}]HiddenId"/>

                        %
                      </c:when>
                      <c:when test="${pifDataForm.confirmMode}">
                          <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${scheduleConstants.PERCENTAGE_SCALE}" value="${vestedAmount.amount}"/>${empty vestedAmount.amount ? '' : '%'}
                      </c:when>
                      <c:otherwise>
                        <c:if test="${not empty vestingSchedule.vestingScheduleDescription}">
                          <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${scheduleConstants.PERCENTAGE_SCALE}" value="${vestedAmount.amount}"/>${empty vestedAmount.amount ? '' : '%'}
                        </c:if>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </c:forEach>
              </tr>
			  </c:if>
            </c:forEach>
          </c:otherwise>
        </c:choose>
		</TBODY></TABLE></DIV>    
          
<!-- As part of Contact Management Project June 2010 For Plan Info, Address Section is removed from this page-->
	</TD></TR></TABLE>
</div>
