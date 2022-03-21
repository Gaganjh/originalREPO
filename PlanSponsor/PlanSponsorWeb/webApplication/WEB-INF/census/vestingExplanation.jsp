<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="censusLookupConstants" className="com.manulife.pension.ps.web.census.util.CensusLookups" />
<un:useConstants var="vestingExplanation" className="com.manulife.pension.ps.web.census.VestingExplanation" />

<!-- Bean Definition for CMA Content -->
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_PARTICIPANT_PARTIAL_STATUS_WARNING}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="participantPartialStatusMessage"/>

<content:contentBean 
  contentId="${contentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_NO_VESTING_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noMoneyTypesText"/>



<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<content:contentBean
	contentId="58307"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="WithdrawalWarning" />

<content:contentBean
	contentId="58283"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="WithdrawalReason" />

<content:contentBean
	contentId="58284"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="EEMoneyType" />

<table border="0" cellpadding="0" cellspacing="0" width="760">
  <tbody><tr>
	<td>
  <p>
  
  
  <table border="0" cellpadding="0" cellspacing="0" width="700">
  <tbody><tr>
    <td width="15"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="15"></td>
    <td valign="top" width="609">
	<table border="0" cellpadding="0" cellspacing="0" width="708">
      <tr>
        <td colspan="17">
      	  <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
      	</td>
      </tr>

      <tr>
        <td colspan="17">
      	  <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
      	</td>
      </tr>
      <c:if test="${param.source=='wd'}">
      <tr>
        <td colspan="17">
      	  <content:getAttribute beanName="WithdrawalWarning" attribute="text"/>
      	</td>
      </tr>
      </c:if>
      <tr>
        <td colspan="17" class="highlight">&nbsp;</td>
      </tr>

      <tr>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="200"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="200"></td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
        <td width="100"></td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>


      <tr>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="200"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="200"></td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
        <td width="100"></td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100">&nbsp;</td>
        <td width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>

       <tr>
         <td><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
         <td colspan="16"><b>
          <c:out value="${requestScope.vestingInfo.employeeLastName}"/>,
          <c:out value="${requestScope.vestingInfo.employeeFirstName}"/> 
          <c:out value="${requestScope.vestingInfo.employeeMiddleInit}"/>
          </b>| <b>SSN: </b><render:ssn property="vestingInfo.employeeSSN" defaultValue="" scope="<%=PageContext.REQUEST_SCOPE%>"/></td>
       </tr>

      <tr class="tablehead">
        <td class="tableheadTD1" colspan="19"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
      </tr>

      <tr class="datacell1">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="200"><b>Service crediting method</b></td>
        <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <c:choose>
          <c:when test="${vestingInfo.employeeVestingInformation.calculationFact.creditingMethod == vestingExplanation.CREDITING_METHOD_HOURS_OF_SERVICE}">
            <td colspan="6" class="highlight">
              <c:out value="${requestScope.vestingInfo.creditingMethodDescription}"/>
            </td>
            <td colspan="7" class="highlight">
              Hours of service requirement:&nbsp;<c:out value="${requestScope.vestingInfo.planHoursOfService}"/>
            </td>
          </c:when>
          <c:otherwise>
            <td colspan="13" class="highlight">
              <c:out value="${requestScope.vestingInfo.creditingMethodDescription}"/>
            </td>
          </c:otherwise>
        </c:choose>
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>

      <tr class="datacell1">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="200" class="tablesubhead"><b>Money type</b></td>
        <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td colspan="13" class="tablesubhead"><div align="center"><b>Completed years of service</b></div></td>
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      <tr class="datacell1">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="200" class="pgNumBack">&nbsp;</td>
        <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100" class="pgNumBack"><div align="center"><b>0</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td width="100" class="pgNumBack"><div align="center"><b>1</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td width="100" class="pgNumBack"><div align="center"><b>2</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td width="100" class="pgNumBack"><div align="center"><b>3</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td width="100" class="pgNumBack"><div align="center"><b>4</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td width="100" class="pgNumBack"><div align="center"><b>5</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td width="100" class="pgNumBack"><div align="center"><b>6</b></div></td>
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      
    <c:forEach var="moneyType" items="${requestScope.vestingInfo.moneyTypes}"> 
      <c:if test='${moneyType.moneyGroup == "ER"}'>
      <c:set var="schedule" value="${requestScope.vestingInfo.vestingSchedule[moneyType.id]}"/>
      <tr class="<c:out value="${rowStyle.next}"/>">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="200">
          <c:out value="${moneyType.contractLongName}"/> (<c:out value="${moneyType.contractShortName}"/>)
		</td>
        <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="100" class="highlight">
        	<div align="right">
        	  <fmt:formatNumber  type ="percent" pattern="###.###'%'" value="${schedule.year0}"/>
        	</div>
        </td>
        <td width="1" class="greyborder">
        	<div align="right">
        		<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
        	</div>
        </td>
        <td width="100" class="highlight">
        	<div align="right">
        	  <fmt:formatNumber  type ="percent" pattern="###.###'%'" value="${schedule.year1}"/>
        	</div>
        </td>
        <td width="1" class="greyborder">
        	<div align="right">
        		<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
        	</div>
        </td>
        <td width="100" class="highlight">
        	<div align="right">
        	  <fmt:formatNumber  type ="percent" pattern="###.###'%'" value="${schedule.year2}"/>
        	</div>
        </td>
        <td width="1" class="greyborder">
        	<div align="right">
        		<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
        	</div>
        </td>
        <td width="100" class="highlight">
        	<div align="right">
        	  <fmt:formatNumber  type ="percent" pattern="###.###'%'" value="${schedule.year3}"/>
        	</div>
        </td>
        <td width="1" class="greyborder">
        	<div align="right">
        		<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
        	</div>
        </td>
        <td width="100" class="highlight">
        	<div align="right">
        	  <fmt:formatNumber  type ="percent" pattern="###.###'%'" value="${schedule.year4}"/>
        	</div>
        </td>
        <td width="1" class="greyborder">
        	<div align="right">
        		<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
        	</div>
        </td>
        <td width="100" class="highlight">
        	<div align="right">
        	  <fmt:formatNumber  type ="percent" pattern="###.###'%'" value="${schedule.year5}"/>
        	</div>
        </td>
        <td width="1" class="greyborder">
        	<div align="right">
        		<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
        	</div>
        </td>
        <td width="100" class="highlight">
        	<div align="right">
        	  <fmt:formatNumber  type ="percent" pattern="###.###'%'" value="${schedule.year6}"/>
        	</div>
        </td>
        <td class="databorder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      </c:if>
	</c:forEach>      
    <c:if test="${empty schedule}">
<%-- 
      <tr class="<c:out value="${rowStyle.next}"/>">
--%>
      <tr>
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td colspan="15">
          <content:getAttribute beanName="noMoneyTypesText" attribute="text" />
		</td>
        <td class="databorder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
    </c:if>
      <tr class="datacell1">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td colspan="15" class="tablesubhead"><b>Fully vested withdrawal</b></td>        
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      <tr class="datacell1">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td colspan="15" class="pgNumBack"> 
          <content:getAttribute id='WithdrawalReason' attribute='text'/>        
        </td>        
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
       <tr>
       <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
       <td colspan="15">
         <table width="100%" cellspacing="0" border="0">
           <tr>
             <td width="25%">&nbsp;<b>Retirement</b>   &nbsp;&nbsp;&nbsp;
<form:checkbox path="fullyVestedOnRetirement" disabled="true" />
             </td>
             <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
             <td width="25%">&nbsp;<b>Early retirement</b>   &nbsp;&nbsp;&nbsp;
<form:checkbox path="fullyVestedOnEarlyRetirement" disabled="true" />
             </td>
             <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
             <td width="25%">&nbsp;<b>Death</b>   &nbsp;&nbsp;&nbsp;
<form:checkbox path="fullyVestedOnDeath" disabled="true" />
             </td>
             <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
             <td width="25%">&nbsp;<b>Permanent disablity</b>   &nbsp;&nbsp;&nbsp;
<form:checkbox path="fullyVestedOnDisability" disabled="true" />
             </td>
           </tr>
         </table>
       </td>
       <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
       </tr>
	
      <tr class="<c:out value="${rowStyle.next}"/>">
        <td colspan="17" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
       </tr>
   </table>
	<c:if test="${not empty requestScope.vestingInfo.employeeVestingInformation}">
                     <p>
                       <!----------------- Section B begins -------------------------></p>
                       
                     <table width="200" border="0" cellspacing="0" cellpadding="0">
                       <tr>
                         <td valign="top"><table class="box" border="0" cellpadding="0" cellspacing="0" width="342">
                           <tbody>
                             <tr class="tablehead">
                               <td class="tableheadTD1"><b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b></td>
                               <td  align="right" class="tableheadTD">
                                 <b>Effective date: 
                                   <fmt:formatDate value="${requestScope.vestingInfo.employeeVestingInformation.vestingEffectiveDate}" pattern="MMMMM dd, yyyy"/>
                                 </b>
                              </td>
                             </tr>
                           </tbody>
                         </table>
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
                         
                           <table class="box" border="0" cellpadding="0" cellspacing="0" width="342">
                             <tbody>
                               <tr>
                                 <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                                 <td>
                        <table border="0" cellpadding="0" cellspacing="0" width="340">
                          <tbody>
                            <c:if test="${vestingInfo.partialParticipantStatus}">
                              <tr style="background-color: #FFFFFF;">
                                <td width="100%" colspan="3"><img
                                  src="/assets/unmanaged/images/warning2.gif"> <content:getAttribute
                                  beanName="participantPartialStatusMessage" attribute="text" /></td>
                              </tr>
                            </c:if>
                            <c:set var="percentages"
                              value="${requestScope.vestingInfo.employeeVestingInformation.moneyTypeVestingPercentages}" />
                            <c:forEach var="moneyType"
                              items="${requestScope.vestingInfo.moneyTypes}">
                              <c:if test='${moneyType.moneyGroup == "ER"}'>
                                <c:set var="currentPercentage" value="${percentages[moneyType.id]}" />
                                <tr class="<c:out value="${rowStyle.next}"/>">
                                  <td width="200"><c:out value="${moneyType.contractLongName}" />
                                  (<c:out value="${moneyType.contractShortName}" />)</td>
                                  <td width="1" class="greyborder"><IMG height="1"
                                    src="/assets/unmanaged/images/spacer.gif" width="0" border="0"></td>
                                  <td width="140" class="highlight">
                                  <div align="right"><c:if
                                    test="${not empty currentPercentage}">
                                    <fmt:formatNumber type="percent" pattern="###.###'%'"
                                      value="${currentPercentage.percentage }" />
                                  </c:if> &nbsp;</div>
                                  </td>
                                </tr>
                              </c:if>
                            </c:forEach>
                            <tr class="<c:out value="${rowStyle.next}"/>">
                              <td colspan="3"><content:getAttribute id='EEMoneyType'
                                attribute='text' /></td>
                            </tr>
                          </tbody>
                        </table>
                        </td>
                                 <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                               </tr>
                               <tr>
                                 <td colspan="3"><table border="0" cellpadding="0" cellspacing="0" width="100%">
                                     <tbody>
                                       <tr>
                                         <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                                         <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                                       </tr>
                                     </tbody>
                                 </table>
                                 </td>
                               </tr>
                             </tbody>
                         </table>
                         </td>
                         <td width="20"><img src="/assets/unmanaged/images/s.gif" height="1" width="20"></td>
                         <td valign="top">
                         <c:if test='${requestScope.vestingInfo.employeeVestingInformation.calculationFact.creditingMethod!="U"}'>
                         <table class="box" border="0" cellpadding="0" cellspacing="0" width="345">
                           <tbody>
                             <tr class="tablehead">
                               <td width="688" colspan="3" class="tableheadTD1"><b><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></b></td>
                             </tr>
                           </tbody>
                         </table>
                         <c:set var="fact" value="${requestScope.vestingInfo.employeeVestingInformation.calculationFact}"/>
							<jsp:setProperty name="rowStyle" property="start" value="1"/>
							<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
                           
                           <table class="box" border="0" cellpadding="0" cellspacing="0" width="345">
                             <tbody>
                               <tr>
                                 <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                                 <td width="640"><table border="0" cellpadding="0" cellspacing="0" width="343">
                                     <tbody>
                                       <tr class="<c:out value="${rowStyle.next}"/>">
                                         <td width="163">Employment status </td>
                                         <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                         <td width="35" class="highlight">
                                           <ps:censusLookup typeName="${censusLookupConstants.EmploymentStatus}" name="vestingInfo" property="employmentStatusCode"/>
                                         </td>
                                         <td width="160">Effective: 
	                                         <span class="highlight">
	                                         	<fmt:formatDate value="${vestingInfo.employmentStatusCodeEffectiveDate}" pattern="MMMMM dd, yyyy"/>
	                                         </span> 
                                         </td>
                                       </tr>
                                       <tr class="<c:out value="${rowStyle.next}"/>">
                                         <td width="163">Fully vested indicator</td>
                                         <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                         <td width="35" class="highlight">
                                           <ps:censusLookup typeName="<%=CensusLookups.BooleanString%>" name="fact" property="fullyVestedOverride"/>
                                         </td>
                                         <td width="160">Effective: 
	                                         <span class="highlight">
	                                         	<fmt:formatDate value="${fact.fullyVestedEffectiveDate}" pattern="MMMMM dd, yyyy"/>
	                                          </span> 
                                         </td>
                                       </tr>
			                         <c:if test='${requestScope.vestingInfo.employeeVestingInformation.calculationFact.creditingMethod=="H"}'>
                                       <tr class="<c:out value="${rowStyle.next}"/>">
                                         <td width="163">Vested years of service </td>
                                         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                         <td width="35" class="highlight">
                                          <c:out value="${fact.previousYearsOfServiceForCurrentPlanYear.value}"/>
                                         </td>
                                         <td width="160">Effective: 
                                         <span class="highlight">
                                           <fmt:formatDate value="${fact.previousYearsOfServiceForCurrentPlanYear.effectiveDate}" pattern="MMMMM dd, yyyy"/>
										 </span>
										</td>
                                       </tr>
                                       <tr class="<c:out value="${rowStyle.next}"/>">
                                         <td width="163">YTD hours worked </td>
                                         <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                         <td width="35" class="highlight">${vestingInfo.participantYearToDateHoursWorked}</td>
                                         <td width="160">Effective: 
	                                         <span class="highlight">
	                                         	<fmt:formatDate value="${vestingInfo.participantYearToDateHoursWorkedEffectiveDate}" pattern="MMMMM dd, yyyy"/>
	                                         </span> 
                                         </td>
                                       </tr>
                                       <tr class="<c:out value="${rowStyle.next}"/>">
                                         <td width="163" >Service credit this year </td>
                                         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                         <td width="35" class="highlight">
                                           <c:if test="${fact.serviceCreditedThisYear}">
                                               Yes
                                           </c:if>
                                           <c:if test="${!fact.serviceCreditedThisYear}">
                                               No
                                           </c:if>
                                         </td>
                                         <td width="160">&nbsp;</td>
                                       </tr>
                         </c:if>
                         <c:if test='${requestScope.vestingInfo.employeeVestingInformation.calculationFact.creditingMethod=="E"}'>                                       
                                       <tr class="<c:out value="${rowStyle.next}"/>">
                                         <td>Hire date</td>
                                         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                         <td colspan="2" class="highlight">
                                           <fmt:formatDate value="${fact.hireDate}" pattern="MMMMM dd, yyyy"/>
                                         </td>
                                       </tr>
                                       <tr class="<c:out value="${rowStyle.next}"/>">
                                         <td width="163">Employment status </td>
                                         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                         <td width="35" class="highlight">
                                           <ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="fact" property="employmentStatus"/>
                                         </td>
                                         <td width="160">Effective: 
                                         <span class="highlight">
                                           <fmt:formatDate value="${fact.employmentStatusEffectiveDate}" pattern="MMMMM dd, yyyy"/>
										 </span>
										</td>
                                       </tr>
						 </c:if>                                       
                                       <tr class="<c:out value="${rowStyle.next}"/>">
                                         <td width="163">Completed years of service </td>
                                         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                         <td width="35" class="highlight">
                                           <c:out value="${fact.completedYearsOfService}"/>
                                         </td>
                                         <td width="160">&nbsp;</td>
                                       </tr>
                                     </tbody>
                                 </table>
                                 </td>
                                 <td class="boxborder" width="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                               </tr>
                               <tr>
                                 <td colspan="3"><table border="0" cellpadding="0" cellspacing="0" width="100%">
                                     <tbody>
                                       <tr>
                                         <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                                         <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                                       </tr>
                                     </tbody>
                                 </table>
                                 </td>
                               </tr>
                             </tbody>
                         </table>
                       </c:if>
                         
                         </td>
                       </tr>
                     </table>
			      </c:if>
                     
                     <p>
              </p>
    <br>
 </td>
  </tr>
</tbody></table>
<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
       type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
       id="globalDisclosure"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
	</tr>
</table>

