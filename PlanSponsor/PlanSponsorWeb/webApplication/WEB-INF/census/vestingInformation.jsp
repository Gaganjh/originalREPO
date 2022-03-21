<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.VestingInformation"%>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@ page import="com.manulife.pension.ps.web.census.util.VestingHelper" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType" %>
<%@ page import="com.manulife.pension.service.vesting.VestingInputDescription"%>
<%@ page import="com.manulife.pension.service.vesting.VestingRetrievalDetails"%>
<%@ page import="com.manulife.pension.service.vesting.util.PlanYear"%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeHireDateInfo" %>
<%@ page import="com.manulife.pension.service.employee.valueobject.ApplyLTPTCreditingInfo" %>
<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

VestingInformation vestingInfo=(VestingInformation)session.getAttribute(CensusConstants.VESTING_INFO);
pageContext.setAttribute("vestingInfo",vestingInfo,PageContext.PAGE_SCOPE);

String activeDate=CensusConstants.EMPLOYMENT_STATUS_ACTIVE;
pageContext.setAttribute("activeDate",activeDate);
%>

	
<un:useConstants var="scheduleConstants" className="com.manulife.pension.service.contract.valueobject.VestingSchedule" />
<c:set var="fv" value="${vestingInfo.fullyVestedDescription}" scope="page" />  
<c:set var="hd" value="${vestingInfo.hireDateDescription}" scope="page" /> 
<c:set var="vyos" value="${vestingInfo.vyosDescription}" scope="page" />  
<c:set var="es" value="${vestingInfo.employmentStatusDescription}" scope="page" />  
<c:set var="pytd" value="${vestingInfo.planYTDHoursDescription}" scope="page" />  
<c:set var="scty" value="${vestingInfo.serviceCreditedDescription}" scope="page" />  
<c:set var="cyos" value="${vestingInfo.completedYearsDescription}" scope="page" />  
<c:set var="cpye" value="${vestingInfo.currentPlanYearEndDescription}" scope="page" />
<c:set var="hireDateEmployeeVestingInfo" value="${requestScope.vestingInfo.employeeHireDateInfo}"/>
<c:set var="applyLTPTCreditingInfo" value="${requestScope.vestingInfo.applyLTPTCreditingInfo}"/>
<%EmployeeHireDateInfo hireDateEmployeeVestingInfo=(EmployeeHireDateInfo)pageContext.getAttribute("hireDateEmployeeVestingInfo");  %>
<%ApplyLTPTCreditingInfo applyLTPTCreditingInfo=(ApplyLTPTCreditingInfo)pageContext.getAttribute("applyLTPTCreditingInfo");  %>
<%VestingInputDescription hd = (VestingInputDescription) pageContext.getAttribute("hd"); 
VestingInputDescription fv = (VestingInputDescription)pageContext.getAttribute("fv");
VestingInputDescription vyos = (VestingInputDescription)pageContext.getAttribute("vyos"); 
VestingInputDescription es = (VestingInputDescription)pageContext.getAttribute("es");
VestingInputDescription pytd = (VestingInputDescription)pageContext.getAttribute("pytd"); 
VestingInputDescription scty = (VestingInputDescription)pageContext.getAttribute("scty"); 
VestingInputDescription cyos = (VestingInputDescription)pageContext.getAttribute("cyos"); 
VestingInputDescription cpye = (VestingInputDescription)pageContext.getAttribute("cpye"); 
%>


<c:choose>
  <c:when test="${not empty param.printFriendly or param.source == 'wd'}">
    <c:set var="disabled" value="true"/>
  </c:when>
  <c:otherwise>
    <c:set var="disabled" value="false"/>
  </c:otherwise>
</c:choose>

<c:choose>
  <c:when test="${param.printFriendly == true}">
    <c:set var="expandStyle" value="DISPLAY:block"/>
    <c:set var="expandIcon" value="/assets/unmanaged/images/minus_icon.gif"/>    
  </c:when>
  <c:otherwise>
    <c:set var="expandStyle" value="DISPLAY:none"/>
    <c:set var="expandIcon" value="/assets/unmanaged/images/plus_icon.gif"/>
  </c:otherwise>
</c:choose>

	<c:if test="${not empty requestScope.vestingInfo.employeeVestingInformation}">
       
    <!----------------- Section 1 begins ------------------------->
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tbody>
    <tr>
      <td colspan="7"><b>
          <c:out value="${requestScope.vestingInfo.employeeLastName}"/>,
          <c:out value="${requestScope.vestingInfo.employeeFirstName}"/> 
          <c:out value="${requestScope.vestingInfo.employeeMiddleInit}"/>
          </b>| <b>SSN: <render:ssn property="vestingInfo.employeeSSN" defaultValue="" scope="<%=PageContext.REQUEST_SCOPE%>"/></b>
      </td>
    </tr>
    <tr class="tablehead">
      <td colspan="7" valign="middle" class="tableheadTD1"><b>Vesting information as of</b>
      	<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="5">
      	<form:input type="text" path="asOfDate" size="8" disabled="${disabled}"
          id="asOfDateId"
          maxlength="10" onblur="validateAsOfDate(this)" />
		
		<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="5">
		<c:if test="${!disabled }">		   		   	
		<a href="javascript:doCalendar('asOfDate',0)"
          onfocus="handleAsOfDateCalendarFocus();"
          onblur="handleAsOfDateCalendarBlur();"
		>
		<img src="/assets/unmanaged/images/cal.gif" border="0"></a>
		<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="10">
		<input type="submit" name="recalculate" value="recalculate" onclick="return handleRecalculateClicked();"/>
		</c:if>
	  </td>
    </tr>
    <tr class="datacell1">
      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td colspan="5" class="highlight">
      <br>
<c:forEach items="${vestingInfo.explanation}" var="cmaId" varStatus="indexVal">
<c:set var="cmaId" value="${cmaId}"/>
<%Integer cmaId = (Integer)pageContext.getAttribute("cmaId"); %>
      	  <content:contentBean contentId="<%=cmaId.intValue()%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                               id="explanation" override="true"/>
          <!-- CMA:[${cmaId}]  -->
          <% if (cmaId.intValue() == ContentConstants.EXPLANATION_VYOS_PROVIDED ||
                 cmaId.intValue() == ContentConstants.EXPLANATION_USING_MORE_RECENT_ASOFDATE) { %>
			<content:getAttribute id="explanation" attribute="text">
			  <content:param>
				<c:out value="${vestingInformationForm.asOfDate}"/>
			  </content:param>
			</content:getAttribute>
		  <% } else if (cmaId.intValue() == ContentConstants.EXPLANATION_CREDITED_ADDITIONAL_YEAR ||
                 		cmaId.intValue() == ContentConstants.EXPLANATION_NOT_CREDITED_ADDITIONAL_YEAR) { %>
            <content:getAttribute id="explanation" attribute="text">
			  <content:param>
			  	<c:if test="${cpye.dateValue != null}">	
                 <fmt:formatDate value="${cpye.dateValue}" pattern="MM/dd/yyyy"/>
                </c:if>
              </content:param>
			</content:getAttribute>
		  <% } else if (cmaId.intValue() == ContentConstants.EXPLANATION_VYOS_HIRE_DATE_BEFORE_ASOFDATE) { %>
            <content:getAttribute id="explanation" attribute="text">
              <content:param>
			  	<c:out value="${vestingInfo.shortHireDate}" />
              </content:param>
              <content:param>
                <c:if test="${cpye.dateValue != null && es != null}">
                <c:if test="${es.stringValue == activeDate}">
                    <fmt:formatDate value="${cpye.dateValue}" pattern="MM/dd/yyyy"/>
                  </c:if>
                        <c:if test="${es.stringValue != activeDate}">
                        <c:if test="${not empty es.effectiveDate}">
                      <fmt:formatDate value="${vestingInfo.employmentStatusPlanYearEnd}" pattern="MM/dd/yyyy"/>
                    </c:if>
                  </c:if>
                </c:if>
              </content:param>
			</content:getAttribute>
		  <% } else if (cmaId.intValue() == ContentConstants.EXPLANATION_VYOS_HIRE_DATE_AFTER_ASOFDATE) { %>
            <content:getAttribute id="explanation" attribute="text">
			  <content:param>
			  	<c:out value="${vestingInformationForm.asOfDate}"/>
              </content:param>
              <content:param>
			  	<c:out value="${vestingInfo.shortHireDate}" />
              </content:param>
			</content:getAttribute>
		  <% } else if (cmaId.intValue() == ContentConstants.EXPLANATION_VYOS_BEFORE_ASOFDATE_HIRE_DATE_AFTER_ASOFDATE) { %>
            <content:getAttribute id="explanation" attribute="text">
			  <content:param>
			  	<c:if test="${hd.dateValue != null}">	
                 <fmt:formatDate value="${hd.dateValue}" pattern="MM/dd/yyyy"/>
                </c:if>
              </content:param>
			</content:getAttribute>
		  <% } else if (cmaId.intValue() == ContentConstants.EXPLANATION_NO_VYOS_HIRE_DATE_AFTER_ASOFDATE) { %>
            <content:getAttribute id="explanation" attribute="text">
              <content:param>
			  	<c:out value="${vestingInformationForm.asOfDate}"/>
              </content:param>
			  <content:param>
			  	<c:if test="${hd.dateValue != null}">	
                 <fmt:formatDate value="${hd.dateValue}" pattern="MM/dd/yyyy"/>
                </c:if>
              </content:param>
			</content:getAttribute>
      	  <% } else { %>
			<content:getAttribute id="explanation" attribute="text"/>
      	  <% } %>
      	  <br>
      	  </c:forEach>
      <br>
      </td>
      <td rowspan="3" class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr class="datacell1">
      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td width="250" valign="top" class="tablesubhead"><b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b></td>
      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td width="50" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="50"></td>
      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td width="400" valign="top" class="tablesubhead"><b><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></b></td>
      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
    <tr class="datacell1">
      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td width="250" align="center" valign="top">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
        	<c:set var="percentages"
          	value="${requestScope.vestingInfo.employeeVestingInformation.moneyTypeVestingPercentages}" />
        	<c:forEach var="moneyType" items="${requestScope.vestingInfo.moneyTypes}">
          	<c:if test='${moneyType.moneyGroup == "EE"}'>
          		<c:set var="hasEEMoneyTypes" value="true"/>
          	</c:if>
          	<c:if test='${moneyType.moneyGroup != "EE"}'>
            <c:set var="currentPercentage" value="${percentages[moneyType.id]}" />
            <tr class="<c:out value="${rowStyle.next}"/>">
              <td width="200"><c:out value="${moneyType.contractLongName}" />
              (<c:out value="${moneyType.contractShortName}" />)</td>
              <td width="1" class="greyborder"><IMG height=1
                src="/assets/unmanaged/images/spacer.gif" width=0 border=0></td>
              <td width="49" class="highlight">
              <div align="right">
              <c:if test="${not empty currentPercentage}">
                <fmt:formatNumber type="percent" pattern="###.###'%'"
                  value="${currentPercentage.percentage }" />
              </c:if> &nbsp;</div>
              </td>
            </tr>
            </c:if>
        	</c:forEach>
        	<tr>
              <td class="greyborder" colspan="3" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
            </tr>
            <c:if test='${hasEEMoneyTypes == "true"}'>
        	<tr class="<c:out value="${rowStyle.next}"/>">
          		<td colspan="3">
          			<content:getAttribute id='EEMoneyType' attribute='text' />
          		</td>
        	</tr>
        	<tr>
              <td class="greyborder" colspan="3" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
            </tr>
            </c:if>
    	</table>
      </td>
      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td width="50"><img src="/assets/unmanaged/images/s.gif" height="1" width="50"></td>
      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td valign="top" width="400">
    	<c:if test='${requestScope.vestingInfo.employeeVestingInformation.retrievalDetails.creditingMethod!="U"}'>
        
		<jsp:setProperty name="rowStyle" property="start" value="1"/>
		<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
        <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
          <c:choose>
            <c:when test="${requestScope.vestingInfo.displayAsFullyVested}">
            <tr class="<c:out value="${rowStyle.next}"/>">
              <td width="140">100% vesting applied</td>
              <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="70" class="highlight">
              	Yes
              </td>
              <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="188">
              Effective:&nbsp;
                <c:choose>
                 <c:when test="${fv.booleanValue}">
                   <fmt:formatDate value="${fv.effectiveDate}" pattern="MM/dd/yyyy"/>
                 </c:when>
                 <c:otherwise>
                   <fmt:formatDate 
                     value="${requestScope.vestingInfo.asOfDate}"  
                     pattern="MM/dd/yyyy"/>
                 </c:otherwise>
                </c:choose>
              <c:if test="${fv.booleanValue}">
                <br>
                <%=VestingHelper.getVestingAuditInfo(fv,userProfile,VestingType.FULLY_VESTED_IND)%>
              </c:if>
              </td>
            </tr>
            </c:when>
            <c:otherwise>
            <tr class="<c:out value="${rowStyle.next}"/>">
              <td width="140">Hire date</td>
              <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="70" class="highlight">
              <c:if test="${hd.dateValue != null}">	
              <fmt:formatDate value="${hd.dateValue}" pattern="MM/dd/yyyy"/>
              </c:if>
              </td>
              <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="188">
              <%=VestingHelper.getVestingAuditInfo(hireDateEmployeeVestingInfo, hd,userProfile,VestingType.HIRE_DATE)%>
              </td>
            </tr>
            <tr class="<c:out value="${rowStyle.next}"/>">
              <td width="140">Employment status </td>
              <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="70" class="highlight">
              <ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="es" property="stringValue" />
              </td>
              <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="188">
              Effective:&nbsp;
              <c:if test="${es.effectiveDate != null}">	
              <fmt:formatDate value="${es.effectiveDate}" pattern="MM/dd/yyyy"/>
              </c:if><br>
           	  <%=VestingHelper.getVestingAuditInfo(es,userProfile,VestingType.EMPLOYMENT_STATUS)%>
              </td>
            </tr>
            <tr class="<c:out value="${rowStyle.next}"/>">
              <td width="140">Vested years of service </td>
              <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="70" class="highlight">
              <c:out value="${vyos.integerValue}"/>
              </td>
              <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="188">
              Effective:&nbsp;
              <c:if test="${vyos.effectiveDate != null}">
              <fmt:formatDate value="${vyos.effectiveDate}" pattern="MM/dd/yyyy"/>
              </c:if><br>
              <%=VestingHelper.getVestingAuditInfo(vyos,userProfile,VestingType.VYOS)%>
              </td>
            </tr>
            <c:if test="${vestingInfo.showCalculatedColumns}">
	        <tr class="<c:out value="${rowStyle.next}"/>">
	          <td width="140">Service credited this year</td>
	          <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	          <td width="70" class="highlight">
	            <c:if test="${scty.booleanValue}">
	               Yes
	            </c:if>
	            <c:if test="${!scty.booleanValue}">
	               No
	            </c:if>
	          </td>
	          <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	          <td width="188">
	          	<c:if test='${requestScope.vestingInfo.employeeVestingInformation.retrievalDetails.creditingMethod=="H"}'>
		            Based on Plan YTD hours worked:&nbsp;<c:out value="${pytd.objectValue}"/><br>
		            Effective:&nbsp;
		            <c:if test="${pytd.effectiveDate != null}">
		            <fmt:formatDate value="${pytd.effectiveDate}" pattern="MM/dd/yyyy"/>
		            </c:if>
		        </c:if>
		        <c:if test='${requestScope.vestingInfo.employeeVestingInformation.retrievalDetails.creditingMethod=="E"}'>
		            Based on hire date anniversary:&nbsp;<c:out value="${vestingInfo.shortHireDate}" />
	            </c:if></td>
	        </tr>                                                
            <tr class="<c:out value="${rowStyle.next}"/>">
              <td width="140">Completed years of service </td>
              <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="70" class="highlight">
                <c:out value="${cyos.integerValue}"/>
              </td>
              <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="188">As of:&nbsp;<fmt:formatDate value="${cyos.effectiveDate}" pattern="MM/dd/yyyy"/></td>
            </tr>
            
            <c:set var="applyLTPTCreditingParam" value="${requestScope.vestingInfo.applyLTPTCreditingInfo}"/>
            <c:if test="${vestingInformationForm.displayApplyLTPTCreditingField eq true}">
	            <tr class="<c:out value="${rowStyle.next}"/>">
	              <td width="140">Apply LTPT crediting </td>
	              <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	              <td width="70" class="highlight">
		              <c:if test="${requestScope.vestingInfo.applyLTPTCreditingInfo != null}">	
		              	<c:if test="${requestScope.vestingInfo.applyLTPTCreditingInfo.applyLTPTCrediting == 'Y'}">Yes</c:if>	
		              	<c:if test="${requestScope.vestingInfo.applyLTPTCreditingInfo.applyLTPTCrediting == 'N'}">No</c:if>
		              </c:if>
	              	  <c:if test="${empty applyLTPTCreditingParam}">No</c:if>
	              </td>
	              <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	              <td width="188"><%=VestingHelper.getVestingAuditInfoForLTPTCrediting(applyLTPTCreditingInfo, userProfile,VestingType.APPLY_LTPT_CREDITING)%></td>
	            </tr>
            </c:if>
            </c:if>
            </c:otherwise>
          </c:choose>
            <tr>
              <td class="greyborder" colspan="5" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
            </tr>
          </table>
         </c:if>
        </td>
        <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      </tr>
      <tr>
        <td colspan="7" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      </tr>
    </tbody>
    </table>
    </c:if> 
    <br>
 
 	<!----------------- Section 2 begins ------------------------->
 	<table class="box" cellSpacing="0" cellPadding="0" border="0" width="100%">  			
	  <tr class="tablehead">
    	<td class="tableheadTD1">
    		<c:choose>
    		<c:when test="${!param.printFriendly}">
      		<A HREF="javascript:toggleSection('schedule')"><IMG ID="scheduleIcon" SRC="<c:out value='${expandIcon}'/>" BORDER="0"/></A>&nbsp;
      		</c:when>
      		</c:choose>
      		<B>Plan vesting Information</B><!-- <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>  -->
    	</td>
      </tr>    			
    </table>
    <DIV id="schedule" style="<c:out value='${expandStyle}'/>">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tbody>
      
	  <tr class="datacell1">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="401" colspan="3">
        	Service crediting method&nbsp;&nbsp;<span class="highlight">
            <c:out value="${requestScope.vestingInfo.creditingMethodDescription}"/></span>
        </td>
        <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td colspan="13">
          <div align="left">
            <c:if test='${requestScope.vestingInfo.employeeVestingInformation.retrievalDetails.creditingMethod=="H"}'>
            Hours requirement:&nbsp;&nbsp;<span class="highlight"><c:out value="${requestScope.vestingInfo.planHoursOfService}"/></span>
            </c:if>
          </div>
        </td>
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      <tr class="datacell1">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="250" class="tablesubhead"><b>Money Type</b></td>
        <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="150" class="tablesubhead"><b>Vesting Schedule</b></td>
        <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td colspan="13" class="tablesubhead"><div align="center"><b>Completed years of service</b></div></td>
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      <tr class="datacell1">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="250" class="pgNumBack">&nbsp;</td>
        <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="150" class="pgNumBack">&nbsp;</td>
        <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td class="pgNumBack" width="60"><div align="center"><b>0</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td class="pgNumBack" width="60"><div align="center"><b>1</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td class="pgNumBack" width="60"><div align="center"><b>2</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td class="pgNumBack" width="60"><div align="center"><b>3</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td class="pgNumBack" width="60"><div align="center"><b>4</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td class="pgNumBack" width="60"><div align="center"><b>5</b></div></td>
        <td width="1" class="greyborder"><div align="center"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></div></td>
        <td class="pgNumBack" width="60"><div align="center"><b>6</b></div></td>
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      
      <c:set var="vestingSchedules" value="${requestScope.vestingInfo.vestingSchedules}"/>
      <c:forEach items="${vestingSchedules}" var="vestingSchedule" varStatus="vestingScheduleStatus">
      <tr class="<c:out value="${rowStyle.next}"/>">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="250" class="textData"><c:out value="${vestingSchedule.moneyTypeLongName}"/>&nbsp;(<c:out value="${vestingSchedule.moneyTypeShortName}"/>)</td>
        <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td width="150" class="textData"><c:out value="${vestingSchedule.vestingScheduleDescription}"/></td>
        <c:forEach items="${vestingSchedule.schedules}" var="vestedAmount" varStatus="vestedAmountStatus">
          <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td class="numericData">
            <c:if test="${not empty vestingSchedule.vestingScheduleDescription}">
              <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${scheduleConstants.PERCENTAGE_SCALE}" value="${vestedAmount.amount}"/>${empty vestedAmount.amount ? '' : '%'}
            </c:if>
          </td>
        </c:forEach>
        <td class="databorder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      </c:forEach>
      
	  <tr class="datacell1">
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td colspan="17" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      <tr class="datacell1">
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td colspan="17" class="datacell1"> 
          <b><content:getAttribute id='WithdrawalReason' attribute='text'/></b>        
        </td>        
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      <tr>
       <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
       <td colspan="17">
         <table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr class="datacell1">
             <td width="25%">
             <c:if test="${vestingInfo.fullyVestedOnRetirement}">
				<input type="checkbox" name="vestingInfo.fullyVestedOnRetirement" id="vestingInfo" disabled="true" checked="checked"/>
			 </c:if>

			<c:if test="${!vestingInfo.fullyVestedOnRetirement}">
				<input type="checkbox" name="vestingInfo.fullyVestedOnRetirement" id="vestingInfo" disabled="true"/>
			 </c:if>
               &nbsp;Retirement
             </td>
             <td width="25%">
             
             <c:if test="${vestingInfo.fullyVestedOnEarlyRetirement}">
				<input type="checkbox" name="vestingInfo.fullyVestedOnEarlyRetirement" id="vestingInfo" disabled="true" checked="checked"/>
			 </c:if>

			<c:if test="${!vestingInfo.fullyVestedOnEarlyRetirement}">
				<input type="checkbox" name="vestingInfo.fullyVestedOnEarlyRetirement" id="vestingInfo" disabled="true"/>
			 </c:if>
               &nbsp;Early retirement
             </td>
             <td width="25%">
             <c:if test="${vestingInfo.fullyVestedOnDeath}">
				<input type="checkbox" name="vestingInfo.fullyVestedOnDeath" id="vestingInfo" disabled="true" checked="checked"/>
			 </c:if>

			<c:if test="${!vestingInfo.fullyVestedOnDeath}">
				<input type="checkbox" name="vestingInfo.fullyVestedOnDeath" id="vestingInfo" disabled="true"/>
			 </c:if>
               &nbsp;Death
             </td>
             <td width="25%">
             <c:if test="${vestingInfo.fullyVestedOnDisability}">
				<input type="checkbox" name="vestingInfo.fullyVestedOnDisability" id="vestingInfo" disabled="true" checked="checked"/>
			 </c:if>

			<c:if test="${!vestingInfo.fullyVestedOnDisability}">
				<input type="checkbox" name="vestingInfo.fullyVestedOnDisability" id="vestingInfo" disabled="true"/>
			 </c:if>
               &nbsp;Permanent disability
             </td>
           </tr>
         </table>
       </td>
       <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      <tr class="datacell1">
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td colspan="17">&nbsp;</td>
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      <tr class="datacell1">
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        <td colspan="17">
        	<c:if test="${not empty vestingInfo.planLastUpdatedDate}">
	        <content:getAttribute id='PlanLastUpdated' attribute='text'>
	        	<content:param><fmt:formatDate value="${vestingInfo.planLastUpdatedDate}" pattern="MM/dd/yyyy"/></content:param>
	        </content:getAttribute>
	        </c:if>
        </td>
        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
      </tr>
      <tr class="<c:out value="${rowStyle.next}"/>">
        <td colspan="19" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      </tr>
    </tbody>
	</table>
	</DIV>
<script type="text/javascript" >
function doPrint() {
  var printURL;
  urlquery=location.href.split("?");
  printURL = location.href+"&printFriendly=true";
  if (urlquery.length > 1) {
      printURL = location.href+"&printFriendly=true";
  } else {
      printURL = location.href+"?profileId=${vestingInformationForm.profileId}&asOfDate=${vestingInformationForm.asOfDate}&printFriendly=true";
  }
  window.open(printURL,"","width=720,height=480,resizable,toolbar,scrollbars,");
}
</script>