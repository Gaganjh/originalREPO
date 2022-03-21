<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
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
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_FORFEITURES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="forfeituresText"/>
<style type="text/css"> 
.optionsColumnOne {
  width: 400px;
  float: left;
	height: 1%;
}
.optionsColumnTwo {
  width: 135px;
  width: 130px;
  float: left;
  height: 1%;
  text-align: center;
}
.optionsColumnThree {
  width: 100px;
	height: 1%;
  float: left;
	text-align: center;
}
</style>

<div id="forfeituresTabDivId" class="borderedDataBox">
<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
  <TBODY>
	<TR><TD class=subhead colspan="2">	
		<DIV class=sectionTitle>
			<c:if test="${pifDataForm.confirmMode}">
				<content:getAttribute beanName="forfeituresText" attribute="text"/>
			</c:if>
		</DIV>
	</TD></TR>	  
	<TR>  
	<TR>
    <TD width=30>&nbsp;</TD>
    <TD width="100%">
<!--start table content -->
      <DIV id=forfeituresDataDivId>
      <DIV class=evenDataRow>
      <DIV class=boldLabel>
      <DIV class=optionsColumnOne>
      <DIV class=data>Options Available</DIV></DIV>
      <DIV class=optionsColumnTwo>
      <DIV class=data>Forfeiture Options (Select all that apply)</DIV></DIV>
      <DIV class=optionsColumnThree>
      <DIV class=data>Default Option</DIV></DIV></DIV></DIV>
      <c:choose>
       <c:when test="${pifDataForm.editMode}">
	  <c:set var="defaultOption" scope="request" value="${pifDataForm.planInfoVO.forfeitures.defaultOption}"/>
<c:forEach items="${pifDataForm.planInfoVO.forfeitures.forfeituresOptions}" var="unvestedOption" varStatus="count" >
	<div class="${(count.index % 2 == 0) ? 'evenDataRow' : 'oddDataRow'}">
        <div class="optionsColumnOne">
          <div class="data">${unvestedOption.availableOptionsDesc}</div>
        </div>
        <div class="optionsColumnTwo">
          <div class="data">
                <form:checkbox path="pifDataUi.selectedUnvestedMoneyOptions"
                               value="${unvestedOption.availableOptionsCode}"
                               id="unvestedMoneyOption[${count.index}]"
                               onclick="handleSelectedUnvestedMoneyOptionClicked(this, ${count.index})"
                               disabled ="${defaultOption == unvestedOption.availableOptionsCode}"/>
          </div>
        </div>
        <div class="optionsColumnThree">
          <div class="data">
                <c:set var="disabled" value="true" scope="page"/>
				<c:set var="option" value="${unvestedOption.selectOption}" scope="page"/>
				<c:if test="${option == unvestedOption.availableOptionsCode}">
					<c:set var="disabled" value="false" scope="page"/>
				</c:if>
				<c:set var="disabledValue" value="${disabled}" />
<form:radiobutton disabled="${disabledValue}" onclick="handleDefaultUnvestedMoneyOptionClicked(this, ${count.index})" path="planInfoVO.forfeitures.defaultOption" id="unvestedMoneyOptionDefault[${count.index}]" value="${unvestedOption.availableOptionsCode}"/>




            <div class="endDataRowAndClearFloats"></div>
          </div>
        </div>
      </div>
</c:forEach>
	<form:checkbox path="pifDataUi.selectedUnvestedMoneyOptions" 
				value="${pifDataForm.planInfoVO.forfeitures.defaultOption}" 
				id="defaultUnvestedMoneyOptionId" 
				cssClass="hidden"  
				disabled ="false"/>
    </c:when>
    <c:otherwise>
<c:forEach items="${pifDataForm.planInfoVO.forfeitures.forfeituresOptions}" var="unvestedOption" varStatus="count" >
	<div class="${(count.index % 2 == 0) ? 'evenDataRow' : 'oddDataRow'}">
        <div class="optionsColumnOne">
          <div class="data">${unvestedOption.availableOptionsDesc}</div>
        </div>
        <div class="optionsColumnTwo">
          <div class="data">
                <form:checkbox path="pifDataUi.selectedUnvestedMoneyOptions"
                               value="${unvestedOption.availableOptionsCode}"
                               id="unvestedMoneyOption[${count.index}]"
                               disabled="true"/>
          </div>
        </div>
        <div class="optionsColumnThree">
          <div class="data">
				<c:set var="option" value="${unvestedOption.selectOption}" scope="page"/>
<form:radiobutton disabled="true" path="planInfoVO.forfeitures.defaultOption" id="unvestedMoneyOptionDefault[${count.index}]" value="${unvestedOption.availableOptionsCode}"/>



            <div class="endDataRowAndClearFloats"></div>
          </div>
        </div>
      </div>
</c:forEach>

	<form:checkbox path="pifDataUi.selectedUnvestedMoneyOptions" 
				value="${pifDataForm.planInfoVO.forfeitures.defaultOption}" 
				id="defaultUnvestedMoneyOptionId" 
				cssClass="hidden"  
				disabled ="true"/>
				
				
    </c:otherwise>
    </c:choose>
    </div>
<!--end table content -->   
  		</td>
 	</tr>
</tbody></table>
</div>
