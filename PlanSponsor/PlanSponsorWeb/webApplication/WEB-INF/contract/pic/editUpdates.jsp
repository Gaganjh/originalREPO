<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%-- Define static constants --%>
<un:useConstants var="scheduleConstants" className="com.manulife.pension.service.contract.valueobject.VestingSchedule" />

<%-- Defines page level update handlers --%>
<script type="text/javascript">

/**
 * Defines an object to hold vesting schedule information.
 */ 
function VestingScheduleAmountDefault(percent) {
  this.percent=percent;
} 

/**
 * Collection of state tax information.
 */
var vestingScheduleArray = new Array(); 

<%-- Need defaults for no scheduled selected --%>
<c:forEach begin="0" end="${scheduleConstants.YEARS_OF_SERVICE}" var="year">
  vestingScheduleArray['${year}'] = new VestingScheduleAmountDefault('');
</c:forEach>

<%-- Need to track customized values --%>
<c:forEach items="${pifDataForm.planInfoVO.vesting.vestingSchedules}" var="vestingSchedule" varStatus="vestingScheduleStatus">
  <c:if test="${vestingSchedule.vestingScheduleType == scheduleConstants.VESTING_SCHEDULE_CUSTOMIZED}">
    <c:forEach items="${vestingSchedule.schedules}" var="vestedAmount" varStatus="vestedAmountStatus">
      vestingScheduleArray['Row${vestingScheduleStatus.index}Year${vestedAmountStatus.index}'] = new VestingScheduleAmountDefault('<fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${scheduleConstants.PERCENTAGE_SCALE}" value="${vestedAmount.amount}"/>');
    </c:forEach>
  </c:if>
</c:forEach>

<c:forEach items="${vestingSchedules}" var="vestingSchedule">
  <c:forEach items="${vestingSchedule.percentages}" var="percent" varStatus="percentStatus">
    vestingScheduleArray['${vestingSchedule.code}${percentStatus.index}'] 
      = new VestingScheduleAmountDefault('<fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${scheduleConstants.PERCENTAGE_SCALE}" value="${percent}"/>');
  </c:forEach> 
</c:forEach>
<c:forEach begin="0" end="${scheduleConstants.YEARS_OF_SERVICE}" var="year">
vestingScheduleArray['FV${year}'] 
      = new VestingScheduleAmountDefault('<fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${scheduleConstants.PERCENTAGE_SCALE}" value="100"/>'); 
</c:forEach>
</script>