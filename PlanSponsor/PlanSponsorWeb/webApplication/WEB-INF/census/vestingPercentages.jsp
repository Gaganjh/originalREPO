<%@page import="com.manulife.pension.service.employee.valueobject.EmployeeVestingVO"%>
<%@page import="com.manulife.pension.service.employee.valueobject.Employee"%>
<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotForm"%>
<%@ page import="com.manulife.pension.service.vesting.EmployeeVestingInformation"%>
<%@ page import="com.manulife.pension.service.vesting.VestingRetrievalDetails"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.manulife.pension.service.vesting.MoneyTypeVestingPercentage"%>
<%@ page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<%@ page import="com.manulife.pension.ps.web.census.VestingInformationForm"%>
<%@ page import="com.manulife.pension.service.vesting.EmployeeVestingInformation"%>


<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<!-- Bean Definition for CMA Content -->
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_PARTICIPANT_PARTIAL_STATUS_WARNING}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="participantPartialStatusMessage"/>
<c:if test="${not empty employee}">
<%Employee vesting= (Employee)request.getAttribute("employee");
pageContext.setAttribute("vesting",vesting,PageContext.PAGE_SCOPE);
EmployeeVestingInformation vestingInformation=(EmployeeVestingInformation)vesting.getEmployeeVestingVO().getEmployeeVestingInformation();
pageContext.setAttribute("vestingInformation", vestingInformation, PageContext.PAGE_SCOPE);
EmployeeVestingInformation evi=vestingInformation;
pageContext.setAttribute("evi", evi, PageContext.PAGE_SCOPE);
%>
<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

	<tr>
	    <td class="databorder" width="1"><img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>		         
        <td colspan="3" class="tablesubhead">
        <c:if test="${param.vestingCollected}">
       	<b>Vesting effective date:  
		<render:date  patternOut="MM/dd/yyyy" property="vestingInformation.vestingEffectiveDate" defaultValue="" />	            
        </b>
        </c:if>
        </td>
        <td class="tablesubhead" width="120">&nbsp;</td>
        <td class="tablesubhead" align="right" width="150">
        <c:if test="${empty param.printFriendly}">
            <c:if test="${param.showVestingLink}">
	            <a href="javascript:openVestingInformation()">View vesting information</a>
            </c:if>
        </c:if>
        </td>
	    <td class="databorder" width="1">
			<img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
	    </td>
	</tr>
	<c:if test="${employeeForm.partialParticipantStatus || editEmployeeForm.partialParticipantStatus}">
    <tr>
	    <td class=databorder width="1">
	      <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
	    </td>
	    <td colspan="5" style="background-color: #FFFFFF; ">
	      <img src="/assets/unmanaged/images/warning2.gif">
	      <content:getAttribute beanName="participantPartialStatusMessage" attribute="text"/>
	    </td>
	    <td class="databorder" width="1">
	      <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
	    </td>
    </tr>
    </c:if>
    <tr>
		<td class=databorder width=1>
			  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
		</td>		         
        <td colspan="2" class="pgNumBack">&nbsp;<b>Money Type</b></td>
   		<td class="greyborder" width="1"> 
			  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
		</td>
        <td class="pgNumBack" align="center" width="120">
             <div>    
                <b>Vesting %</b>
             </div>
        </td>
        <td class="pgNumBack" width="150">&nbsp;</td>
		<td class=databorder width=1>
			  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
		</td>		         
    </tr>
    <c:set var="percentages" value="${evi.moneyTypeVestingPercentages}"/>
    <c:forEach var="moneyType" items="${requestScope.moneyTypes}"> 
       <c:set var="currentPercentage" value="${percentages[moneyType.id]}"/>
			<!-- Vesting information -->
	          <tr class="<c:out value="${rowStyle.next}"/>" >
				<td class=databorder width="1">
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		         <td width="<c:out value='${param.width}'/>">
		         <c:out value="${moneyType.contractLongName}"/> (<c:out value="${moneyType.contractShortName}"/>)
		         </td>
		         <td width="29" align="right">
		    	 <c:if test="${empty currentPercentage || currentPercentage.percentage == null}">
		    	 <c:if test="${param.vestingCollected != true}">
		           <img src="/assets/unmanaged/images/warning2.gif">
		         </c:if>
		         </c:if>
		    
		         </td>
				<td class="greyborder" width="1"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight" align="center" width="120">
				<c:if test="${not empty currentPercentage}">
		         <fmt:formatNumber  type ="percent" pattern="###.###'%'" value="${currentPercentage.percentage}"/>
		        </c:if>
		         &nbsp;
				</td>
				<td class="highlight" width="150">&nbsp;</td>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
    </c:forEach>
</c:if>
    
