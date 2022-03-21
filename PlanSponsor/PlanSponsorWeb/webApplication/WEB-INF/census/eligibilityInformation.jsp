<%-- Tag libs used--%>
<%@page import="com.manulife.pension.service.contract.valueobject.MoneyTypeVO"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/render" prefix="render"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.eligibility.valueobject.EmployeePlanEntryVO" %>
<%@ page import="com.manulife.pension.service.eligibility.valueobject.PlanEntryRequirementDetailsVO" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%-- Constants used--%>
<un:useConstants var="contentConstants"
	             className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="constants"
	             className="com.manulife.pension.ps.web.Constants" />
<un:useConstants var="censusLookUp"
	             className="com.manulife.pension.ps.web.census.util.CensusLookups" />
<un:useConstants var="eligibilityRequestConstants"
	             className="com.manulife.pension.service.eligibility.EligibilityRequestConstants" />	
<un:useConstants var="renderConstants"
	             className="com.manulife.util.render.RenderConstants" />	
	             
<script type="text/javascript">
/**
 * invoked on clicking calculate button
 */
function calculateEligibility() {
	    document.getElementById("calculateButton").disabled=true;
		document.forms['eligibilityInformationForm'].action.value='calculateEligibility';
		document.forms['eligibilityInformationForm'].submit();
		document.forms['eligibilityInformationForm'].disabled=false;
}
</script>




	         
<content:contentBean contentId="${contentConstants.MESSAGE_ELIGIBILITY_CALCULATION_PENDING}"
                     type="${contentConstants.TYPE_MESSAGE}"
                     id="eligibilityCalculationPendingMessage"/>
                     
<content:contentBean contentId="${contentConstants.ELIGIBILITY_PAGE_MONEY_TYPE_SECTION_HEADER}"
                     type="${contentConstants.TYPE_MESSAGE}"
                     id="moneyTypeSectionHeader"/>
                     
<content:contentBean contentId="${contentConstants.ELIGIBILITY_PAGE_YTD_SECTION_HEADER}"
                     type="${contentConstants.TYPE_MESSAGE}"
                     id="YTDSectionHeader"/>                                          
<jsp:useBean id="eligibilityInformationForm" scope="session" type="com.manulife.pension.ps.web.census.EligibilityInformationForm" />

<ps:form method="GET" action="/do/census/eligibilityInformation/" modelAttribute="eligibilityInformationForm" name="eligibilityInformationForm">

	

<input type="hidden" name="action"/>
<content:errors scope="session"/>
<table border="0" cellpadding="0" cellspacing="0" width="760">
<tbody>
<tr>
<td>
<p>
<table border="0" cellpadding="0" cellspacing="0" width="700">
<tbody>
<tr>
<td width="15"><img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="15"></td>
<td valign="top" width="609">
<table border="0" cellpadding="0" cellspacing="0" width="745">
<tbody>
<tr>
<td colspan="17" class="highlight"></td>
</tr>
<tr>
<td>&nbsp;</td>
<td colspan="15"><table width="745" border="0" cellspacing="0" cellpadding="0">
<tr>
    <td colspan="17" valign="middle">&nbsp;&nbsp;<B>
${eligibilityInformationForm.employeeDetails.employeeDetailVO.lastName} ,
${eligibilityInformationForm.employeeDetails.employeeDetailVO.firstName}
${eligibilityInformationForm.employeeDetails.employeeDetailVO.middleInitial}
	| SSN: <render:ssn property='eligibilityInformationForm.employeeDetails.employeeDetailVO.socialSecurityNumber' />
<c:if test="${not empty eligibilityInformationForm.employeeDetails.employeeDetailVO.employeeId}">
| Employee ID: ${eligibilityInformationForm.employeeDetails.employeeDetailVO.employeeId}
</c:if>
    </B>
</td>
</tr>
<tr class="tablehead">

    <td colspan="17" valign="middle" class="tableheadTD1">
    <b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b> 
    <render:date property="eligibilityInformationForm.latestInformationDate" defaultValue=""
                 patternOut="MM/dd/yyyy kk:mm:ss" /> 
    <br>
    
<c:if test="${eligibilityInformationForm.pendingEligibilityCalculationRequest ==true}">
    <em><content:getAttribute beanName="eligibilityCalculationPendingMessage" attribute="text"/></em>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
</c:if>
    
    <c:if test="${empty param.printFriendly}">
<c:if test="${userProfile.internalUser ==true}">
    <input id="calculateButton" type="button" name="Submit" value="calculate" onclick="calculateEligibility()">
</c:if>
<c:if test="${userProfile.internalUser !=true}">
<c:if test="${eligibilityInformationForm.pendingEligibilityCalculationRequest ==true}">
    <input id="calculateButton" type="button" name="Submit" value="calculate" onclick="calculateEligibility()">
</c:if>
</c:if>
    </c:if>
    
    </td>
</tr>
<tr>
<td colspan="17" valign="top">
<TABLE class="box" border="0" cellpadding="0" cellspacing="0" width="745">
<tbody>
<tr class="tablehead">
    <td colspan="3" class="tablesubhead">
    <b><content:getAttribute beanName="moneyTypeSectionHeader" attribute="text"/></b>
    </td>
</tr>
</tbody>
</table>
<div id="sc1" style="DISPLAY: visible">
<table width="745" border="0" cellpadding="0" cellspacing="0">
<tr class="datacell1">
<td width="1" rowspan="3"  class="databorder">
<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
</td>
<td width="200" rowspan="2" align="left" valign="top" bgcolor="#E6E7E8">&nbsp;
</td>
<td width="1" rowspan="3" align="left" valign="top" class="dataheaddivider">
<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
</td>
<td width="95" rowspan="2" align="left" valign="top" bgcolor="#E6E7E8">&nbsp;
</td>
<td width="1" rowspan="3" align="left" valign="top" class="dataheaddivider">
<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
</td>
<td width="52" rowspan="2" align="left" valign="top" bgcolor="#E6E7E8">&nbsp;
</td>
<td width="1" rowspan="3" align="left" valign="top" class="dataheaddivider">
<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
</td>
<td width="54" rowspan="2" align="left" valign="top" bgcolor="#E6E7E8">&nbsp;
</td>
<td width="1" rowspan="3" align="left" valign="top" class="dataheaddivider">
<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
</td>
<td colspan="3" align="left" valign="top" bgcolor="#E6E7E8">
<div align="center"><B>Period of service</B></div>
</td>
<td width="1" rowspan="3" align="left" valign="top" bgcolor="#E6E7E8" class="dataheaddivider">
<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
</td>
<td colspan="5" align="left" valign="top" bgcolor="#E6E7E8">
<div align="center"><em>For hours of service method only</em></div>
</td>
<td width="1" rowspan="3" class="databorder">
<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
</td>
</tr>
<tr class="datacell1">
<td colspan="3" class="dataheaddivider" align="left" bgcolor="#f8f8f8" height="1" valign="top">
<img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"></td>
<td colspan="5" class="dataheaddivider" align="left" bgcolor="#f8f8f8" height="1" valign="top">
<img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"></td>
</tr>
<tr class="datacell1">
<td width="200" align="left" valign="top" bgcolor="#E6E7E8"><B>Money type</B></td>
<td width="95" align="left" valign="top" bgcolor="#E6E7E8"><B>Eligibility date</B></td>
<td width="54" align="left" valign="top" bgcolor="#E6E7E8"><B>Plan entry date</B></td>
<td width="52" align="left" valign="top" bgcolor="#E6E7E8"><B>Calculation override</B></td>
<td width="50" align="left" valign="top" bgcolor="#E6E7E8"><B>From date</B></td>
<td width="1" align="left" valign="top" bgcolor="#E6E7E8" class="dataheaddivider">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td width="50" align="left" valign="top" bgcolor="#E6E7E8"><B>To date</B></td>
<td width="50" align="left" valign="top" bgcolor="#E6E7E8"><B>Type of period</B></td>
<td width="1" align="left" valign="top" class="dataheaddivider">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td width="43" align="left" valign="top" bgcolor="#E6E7E8"><B>Period hours worked</B></td>
<td width="1" align="left" valign="top" class="dataheaddivider">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td width="50" align="left" valign="top" bgcolor="#E6E7E8"><B>Period hours effective date</B></td>
</tr>

<c:if test="${not empty eligibilityInformationForm.moneyTypeDescList}">
<c:forEach items="${eligibilityInformationForm.moneyTypeDescList}" var="theItem" varStatus="theIndex" >

			   
    <c:if test="${theIndex.index % 2 eq '0'}"> 
	   <c:set var="bgcolor" scope="page" value=""/>
    </c:if>
    <c:if test="${theIndex.index % 2 ne '0'}"> 
	   <c:set var="bgcolor" scope="page" value="#f8f8f8"/>
    </c:if>	
	
<%-- <c:set var="viewBean" value="${eligibilityInformationForm.employeeMoneyDetailsForMoneyId.theItem.id}" />
 --%>
 <c:set var="indexValue" value="${theIndex.index}"/> 	
<%-- <c:set var="viewBean" value="${eligibilityInformationForm.employeeMoneyDetailsForMoneyId.theItem.id}" /> --%>

<%
MoneyTypeVO theItem=(MoneyTypeVO)pageContext.getAttribute("theItem");
String data=(String)theItem.getId();
EmployeePlanEntryVO viewBean= (EmployeePlanEntryVO)eligibilityInformationForm.getEmployeeMoneyDetailsForMoneyId(data);
pageContext.setAttribute("viewBean",viewBean,PageContext.PAGE_SCOPE);
%>


		   
	<tr class="datacell1" bgcolor="${bgcolor}">
    <td width="1"  class="databorder">
         <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
    </td>
    <td width="200" bgcolor="${bgcolor}">
${theItem.contractLongName}
(${theItem.contractShortName})
    </td>
    <td width="1"  align="left" valign="top" class="dataheaddivider">
		  <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
    </td>
    <td width="95" align="left" valign="top" bgcolor="${bgcolor}">
          <render:date property="viewBean.eligibilityDate" defaultValue="" 
		               patternOut="${renderConstants.MEDIUM_MDY_SLASHED}"  />
	</td>
	<td width="1"  align="left" valign="top" class="dataheaddivider">
		  <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
	</td>
	<td width="54" align="left" valign="top" bgcolor="${bgcolor}">
		  <render:date property="viewBean.eligibilityPlanEntryDate" defaultValue="" 
		               patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" />
	</td>
    <td width="1"  align="left" valign="top" class="dataheaddivider">
		  <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
	</td>
	<td width="52" align="center" valign="top" bgcolor="${bgcolor}">
	   <logicext:if name="viewBean" property="eligibilityProvidedIndicator" op="equal" value="${constants.YES}">
		 <logicext:then>
			<input type="checkbox" checked="checked" disabled="disabled"/> 
		 </logicext:then>
		 <logicext:else>
		    <input type="checkbox" disabled="disabled"/>
		 </logicext:else>
	  </logicext:if>
    </td>
	<td width="1"  align="left" valign="top" class="dataheaddivider">
		  <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
	</td>
	<td width="50" align="left" valign="top" bgcolor="${bgcolor}">
		  <render:date property="viewBean.computationPeriodStartDate" defaultValue="" 
		               patternOut="${renderConstants.MEDIUM_MDY_SLASHED}"  />
	</td>
	<td width="1"  align="left" valign="top" class="dataheaddivider">
		  <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
	</td>
	<td width="50" align="left" valign="top" bgcolor="${bgcolor}">
		  <render:date property="viewBean.computationPeriodEndDate" defaultValue="" 
		               patternOut="${renderConstants.MEDIUM_MDY_SLASHED}"  />
	</td>
	<td width="1"  align="left" valign="top" class="dataheaddivider">
		  <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
	</td>
	<td width="50" align="left" valign="top" bgcolor="${bgcolor}">
<c:if test="${not empty viewBean.computationPeriod}">
	      <ps:censusLookup typeName="${censusLookUp.ComputationPeriod}" name="viewBean" 
                           property="computationPeriod"/>
</c:if>
	</td>
	<td width="1"  align="left" valign="top" class="dataheaddivider">
		  <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
	</td>
	<td width="43" align="left" valign="top" bgcolor="${bgcolor}">
<c:if test="${not empty viewBean.POHWorkedEffectiveDate}">
${viewBean.periodOfHoursWorked}
</c:if>
	</td>
	<td width="1"  align="left" valign="top" class="dataheaddivider">
		  <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
	</td>
	<td width="50" align="left" valign="top" bgcolor="${bgcolor}">
	      <render:date property="viewBean.POHWorkedEffectiveDate" defaultValue="" 
		               patternOut="${renderConstants.MEDIUM_MDY_SLASHED}"  />
	</td>
	<td width="1"  class="databorder">
        <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
    </td>
	</tr>
</c:forEach>
</c:if>


<tr class="datacell1">
<td colspan="33" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
</tr>
</table>
</div>

<Br><Br><Br>

<TABLE class="box"   border="0" cellpadding="0" cellspacing="0" width="745">
<tbody>
<tr class="tablehead">
<td colspan="3" class="tableheadTD1">
<b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b></td>
</tr>
</tbody>
</table>

<c:if test="${not empty eligibilityInformationForm.planYtdData}">
  <c:set var="rowSpan" value="${2 + fn:length(eligibilityInformationForm.planYtdData)}"/>
</c:if>
<c:if test="${empty eligibilityInformationForm.planYtdData}">
 <c:set var="rowSpan" value="2"/>
</c:if>

<table width="745" border="0" cellpadding="0" cellspacing="0" >
<tr>
<td width="1"  class="databorder" >
<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
</td>
<td width="175"  align="left" valign="top" bgcolor="#FFFFFF" style=" height: 5em" >
<table width="175" border="0" cellpadding="0" cellspacing="0" style=" height: 5em">
<tr class="datacell1" style=" height: 1em">
    <td width="175">Eligible to participate</td>
</tr>
<tr class="datacell2" style=" height: 1em" >
    <td width="175" bgcolor="#F8F8F8">Hire date</td>
</tr>
<tr class="datacell1" style=" height: 1em" >
    <td width="175">Date of birth</td>
</tr>
<tr class="datacell1" style=" height: 1em" >
    <td width="175" bgcolor="#F8F8F8">Employment status</td>
</tr>
<tr class="datacell1" style=" height: 1em" >
    <td width="175">Employment status effective date</td>
</tr>
<c:if test="${eligibilityInformationForm.displayLongTermPartTimeAssessmentYearField}">
<tr class="datacell1" style=" height: 1em" >
    <td width="175" bgcolor="#F8F8F8">LTPT Assessment Year</td>
</tr>
</c:if>
</table>
</td>
<td width="1"  class="greyborder" style=" height: 5em">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
</td>
<td width="200"  align="left" valign="top" bgcolor="#FFFFFF" style=" height: 5em" >
<table width="200" border="0" cellpadding="0" cellspacing="0" style=" height: 5em">
<tr class="datacell1" style=" height: 1.5em">
    <td width="200" >
<c:if test="${eligibilityInformationForm.employeeDetails.employeeVestingVO.planEligibleInd == constants.YES}">

           Yes
</c:if>
<c:if test="${eligibilityInformationForm.employeeDetails.employeeVestingVO.planEligibleInd == constants.NO}">

           No
</c:if>
    </td>
</tr>
<tr class="datacell2" style=" height: 1em" >
    <td width="200" bgcolor="#F8F8F8">
    <render:date property="eligibilityInformationForm.employeeDetails.employeeDetailVO.hireDate" defaultValue="" 
                 patternOut="${renderConstants.MEDIUM_MDY_SLASHED}"  />
    <br/>
    </td>
</tr>
<tr class="datacell1" style=" height: 1em" >
    <td width="200">
<c:if test="${not empty eligibilityInformationForm.employeeDetails.employeeDetailVO.birthDate}">
    <render:date property="eligibilityInformationForm.employeeDetails.employeeDetailVO.birthDate" defaultValue="" 
                 patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" /> 
(age ${eligibilityInformationForm.employeeAge})
</c:if>
    <br/>
    </td>
</tr>
<tr class="datacell1" style=" height: 1em" >
    <td width="200" bgcolor="#F8F8F8">
    <ps:censusLookup typeName="${censusLookUp.EmploymentStatus}" name="eligibilityInformationForm" 
                     property="employeeDetails.employeeDetailVO.employmentStatusCode"/><br/>
   </td>
</tr>
<tr class="datacell1" style=" height: 1em" >
    
    <td width="200">
    <render:date property="eligibilityInformationForm.employeeDetails.employeeDetailVO.employmentStatusEffDate" defaultValue="" 
                 patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" /></td>
</tr>


<tr class="datacell1" style=" height: 1em" >
    <c:if test="${eligibilityInformationForm.displayLongTermPartTimeAssessmentYearField}">
    <td width="200" bgcolor="#F8F8F8">
    <c:out value="${eligibilityInformationForm.longTermPartTimeAssessmentYear}" default="" /> 
    </td>
    </c:if>
</tr>

</table>
</td>

<td width="1"  class="greyborder" style=" height: 5em">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
</td>
    
<td width="365"  align="left" valign="top" bgcolor="#E6E7E8" style=" height: 5em" >
<table width="365" border="0" cellpadding="0" cellspacing="0"  style=" height: 5em" >

<c:if test="${not empty eligibilityInformationForm.planYtdData}">
         <c:set var="numberOfRows" value="${2 + fn:length(eligibilityInformationForm.planYtdData)}"/>
         <c:if test="${numberOfRows eq '3'}">
           <c:set var="deltaHeight1" value="2.0"/>
		   <c:set var="deltaHeight2" value="0.2"/>
         </c:if>
         <c:if test="${numberOfRows eq '4'}">
           <c:set var="deltaHeight1" value="1.8"/>
		   <c:set var="deltaHeight2" value=".2"/>
         </c:if>
         <c:if test="${numberOfRows eq '5'}">
           <c:set var="deltaHeight1" value="0"/>
		   <c:set var="deltaHeight2" value="0"/>
         </c:if>    
</c:if>
<c:if test="${empty eligibilityInformationForm.planYtdData}">
         <c:set var="numberOfRows" value="2"/>
         <c:set var="deltaHeight1" value="2"/>
</c:if>

    <tr style="height: ${ 5 / numberOfRows + deltaHeight1}em" width="365" class="tablesubhead" >
    <td colspan="5" rowspan="1">
    <img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1">
    <B><content:getAttribute beanName="YTDSectionHeader" attribute="text"/></B>
    </td>
    </tr>
    <tr bgcolor="#F8F8F8" style="height: ${ 5 / numberOfRows }em" width="365" >
    <td width="100" valign="top" bgcolor="#E6E7E8">
        <strong>Plan year end</strong></td>
    <td width="1" rowspan="${numberOfRows + 1 }" class="greyborder">
        <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
    <td width="125" valign="top" bgcolor="#E6E7E8">
        <strong>Plan YTD hours</strong></td>
    <td width="1" rowspan="${numberOfRows + 1 }"  class="greyborder">
        <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
    <td width="200" bgcolor="#E6E7E8" valign="top">
         <strong>Plan YTD hours effective date</strong></td>
    </tr>
<c:if test="${not empty eligibilityInformationForm.planYtdData}">
<c:forEach items="${eligibilityInformationForm.planYtdData}" var="theItem" varStatus="theIndex">


  <c:if test="${theIndex.index % 2 eq '0'}"> 
	  <c:set var="bgcolor"  value=""/>
  </c:if>
  <c:if test="${theIndex.index % 2 ne '0'}"> 
	  <c:set var="bgcolor"  value="#f8f8f8"/>
  </c:if>

  <tr class="datacell1" valign="bottom" width="365" style="height: ${ 5 / numberOfRows + deltaHeight2}em" >
     <td width="100" bgcolor="${bgcolor}">
${theItem.key}
     </td>
     <td width="125" bgcolor="${bgcolor}" >
${theItem.value.value}
     </td>
     <td width="200" bgcolor="${bgcolor}" >
${theItem.value.label}
     </td>
  </tr>
</c:forEach>
</c:if>
  </table>
</td>

<td width="1"  class="databorder" style=" height: 60" >
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
</tr>

<tr  class="datacell1">
    <td colspan="6" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
</tr>
</table>

<Br><Br><Br>

<TABLE class="box"   border="0" cellpadding="0" cellspacing="0" width="745">
<tbody>
<tr class="tablehead">
    <td colspan="3" class="tableheadTD1">
    <b><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></b></td>
</tr>
</tbody>
</table>

</td>
</tr>
            
<tr class="datacell1">
<td width="1" rowspan="1" valign="top" class="databorder">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td width="200" valign="top" bgcolor="#E6E7E8"><B>Money type</B></td>
<td width="1" valign="top" class="dataheaddivider">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td width="61" valign="top" bgcolor="#E6E7E8"><B>Service election date</B></td>
<td width="1" valign="top" class="dataheaddivider">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td width="61" valign="top" bgcolor="#E6E7E8"><B>Immediate<Br> eligibility</B></td>
<td width="1" valign="top" class="dataheaddivider">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td width="100" valign="top" bgcolor="#E6E7E8"><B>Service crediting<Br> method</B></td>
<td width="1" valign="top" class="dataheaddivider">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td width="58" valign="top" bgcolor="#E6E7E8"><b>Minimum age</b> </td>
<td width="1" valign="top" class="dataheaddivider">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td width="50" valign="top" bgcolor="#E6E7E8"><B>Hours of service</B></td>
<td width="1" valign="top" class="dataheaddivider">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td width="120" valign="top" bgcolor="#E6E7E8"><B>Period of service</B></td>
<td width="1" valign="top" class="dataheaddivider">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
<td valign="top" bgcolor="#E6E7E8"><b>Plan entry frequency</b></td>
<td width="1" rowspan="1" valign="top" class="databorder">
    <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
</tr>

<c:if test="${not empty eligibilityInformationForm.moneyTypeDescList}">
<c:forEach items="${eligibilityInformationForm.moneyTypeDescList}" var="theItem" varStatus="theIndex">


			   
   <c:if test="${theIndex.index % 2 eq '0'}"> 
	  <c:set var="bgcolor"  value=""/>
   </c:if>
   <c:if test="${theIndex.index % 2 ne '0'}"> 
	  <c:set var="bgcolor"  value="#f8f8f8"/>
   </c:if>			   

<%-- <c:set var="viewBean" value="${eligibilityInformationForm.planMoneyDetailsForMoneyId.theItem.id}" /> --%>

<c:set var="indexValue" value="${theIndex.index}"/> 	
<%-- <c:set var="viewBean" value="${eligibilityInformationForm.employeeMoneyDetailsForMoneyId.theItem.id}" /> --%>

<%
MoneyTypeVO theItem=(MoneyTypeVO)pageContext.getAttribute("theItem");
String data=(String)theItem.getId();
PlanEntryRequirementDetailsVO viewBean= (PlanEntryRequirementDetailsVO)eligibilityInformationForm.getPlanMoneyDetailsForMoneyId(data);
pageContext.setAttribute("viewBean",viewBean,PageContext.PAGE_SCOPE);
%>



   <tr class="datacell1" bgcolor="${bgcolor}">
   <td width="1" rowspan="1" valign="top" class="databorder">
      <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
   </td>
   <td width="200" bgcolor="${bgcolor}">
${theItem.contractLongName}
(${theItem.contractShortName})
   </td>
   <td width="1" rowspan="1" class="dataheaddivider">
        <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
   </td>
   <td width="61" bgcolor="${bgcolor}">
       <render:date	property="eligibilityInformationForm.serviceElectionDate(${theItem.id})" 
                    defaultValue=""
                    patternOut="${renderConstants.MEDIUM_MDY_SLASHED}"/>
   </td>
   <td width="1" rowspan="1" class="dataheaddivider">
       <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
   </td>
   <td width="61" align="center" bgcolor="${bgcolor}">
       <logicext:if name="viewBean" property="immediateEligibilityInd" op="equal" value="${constants.YES}">
		 <logicext:then>
			<input type="checkbox" checked="checked" disabled="disabled"/> 
		 </logicext:then>
		 <logicext:else>
		    <input type="checkbox"  disabled="disabled"/>
		 </logicext:else>
	  </logicext:if>
   </td>
   <td width="1" rowspan="1" class="dataheaddivider">
       <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
   </td>
   <td width="100" bgcolor="${bgcolor}">
<c:if test="${not empty viewBean.serviceCreditMethodInd}">
<c:if test="${viewBean.serviceCreditMethodInd != eligibilityRequestConstants.SERVICE_CREDIT_METHOD_UNSPECIFIED}">

            <ps:censusLookup typeName="${censusLookUp.ServiceCreditingMethod}" name="viewBean" 
                             property="serviceCreditMethodInd"/>
</c:if>
</c:if>
   </td>
   <td width="1" rowspan="1" class="dataheaddivider">
       <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
   </td>
   <td width="58" bgcolor="${bgcolor}">
       <c:choose>
  	      <c:when test="${fn:endsWith(viewBean.minimumAge, '.5')}">
  	            ${fn:substringBefore(viewBean.minimumAge, '.5')}&#189
	      </c:when>
	      <c:otherwise>
	            <render:number property="viewBean.minimumAge" defaultValue="" pattern="#"/>
	      </c:otherwise>
	   </c:choose>
   </td>
   <td width="1" rowspan="1" class="dataheaddivider">
       <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
   </td>
   <td width="50" bgcolor="${bgcolor}">
        <c:if test="${viewBean.eligibilityServiceHour gt '0'}">
${viewBean.eligibilityServiceHour}
       </c:if>
   </td>
   <td width="1" rowspan="1" class="dataheaddivider">
       <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
   </td>
   <td width="120" bgcolor="${bgcolor}">
<c:if test="${not empty viewBean.periodOfServiceUnitCode}">
       <c:if test="${viewBean.eligibilityServicePeriod gt '0'}">
${viewBean.eligibilityServicePeriod}
           <ps:censusLookup typeName="${censusLookUp.PeriodOfServiceUnit}" name="viewBean" 
                            property="periodOfServiceUnitCode"/>
       </c:if>                     
</c:if>
   </td>
   <td width="1" rowspan="1" class="dataheaddivider">
       <img src="/assets/unmanaged/images/s.gif" border="0" height="1"	width="1">
   </td>
   <td bgcolor="${bgcolor}">
<c:if test="${not empty viewBean.planEntryFrequencyInd}">
          <ps:censusLookup typeName="${censusLookUp.PlanEntryFrequency}" name="viewBean" 
                           property="planEntryFrequencyInd"/>
</c:if>
   </td>
   <td width="1" rowspan="1" valign="top" class="databorder">
      <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">
   </td>
</tr>
</c:forEach>
</c:if>
<tr>
    <td colspan="17" valign="top" class="databorder">
        <img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1"></td>
</tr>
<tr>
    <td colspan="17" valign="top"><div id=sc2 style="DISPLAY: visible"></div></td>
</tr>
<tr>
    <td colspan="17" valign="top"><Br></td>
</tr>
</table>
</td>
<td>&nbsp;</td>
</tr>
<tr>
     <td width="1"><IMG height=1  src="/assets/unmanaged/images/s.gif"  width=0 border=0></td>
     <td colspan="15"></td>
</table>
</td>
     <td><IMG height=1 src="/assets/unmanaged/images/s.gif"  width=0 border=0></td>
</tr>
</tbody>
</table> 
<table width="745">
<tbody>
<tr>
    <td><img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="30"></td>
    <td>
    <table border="0" cellpadding="0" cellspacing="0" width="732">
    <tbody><tr valign="top">
             <td width="333"> <div align="left"></div></td>
             <td width="180" align="right"><div align="center"><br></div></td>
             <td width="166" align="right"><div align="right"></div></td>
		     </tr>
     </tbody>
     </table>
</tr>
</tbody>
</table>
</ps:form>

<!-- footer table -->
<table height=25 cellSpacing=0 cellPadding=0 width=760 border=0>
<tr>
<td>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td width="30" valign="top">
<img src="/assets/unmanaged/images/s.gif" width="30" height="1">
</td>
<td>
<p> <content:pageFooter beanName="layoutPageBean"/></p>
<p class="footnote"> <content:pageFootnotes beanName="layoutPageBean"/></p>
<p class="disclaimer"> <content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
</td>
</tr>
</table>
</td>
<td width="3%"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
<td width="15%" ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
</tr>
</table>

<c:if test="${not empty param.printFriendly}">
	<content:contentBean contentId="${contentConstants.GLOBAL_DISCLOSURE}"
        type="${contentConstants.TYPE_MISCELLANEOUS}"
        id="globalDisclosure"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
