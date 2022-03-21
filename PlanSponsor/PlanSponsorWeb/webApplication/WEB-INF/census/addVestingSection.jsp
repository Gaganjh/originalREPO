<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<%@page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>


<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="VestingSection" />


<c:set var="disabled" value="${addEmployeeForm.readOnly}" /> 
<c:set var="disablePrevioysYrsEffDate" value="${addEmployeeForm.disablePrevioysYrsEffDate}" /> 
<c:set var="disableFullyVestedEffDate" value="${addEmployeeForm.disableFullyVestedEffDate}" /> 

<c:choose >
  <c:when test="${disableFullyVestedEffDate}">
	<c:set var="showFullyVestedCal" value="DISPLAY:none"/>
  </c:when>
   <c:otherwise>
     <c:set var="showFullyVestedCal" value="DISPLAY:inline"/>
  </c:otherwise>
 </c:choose>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
    <tbody>
      <tr class="tablehead">
        <td class="tableheadTD1">
			<b><content:getAttribute id="VestingSection" attribute="text"/> </b>
        </td>
        <td class="tableheadTD" id="headerParticipation">
        </td>
      </tr>
    </tbody>
  </table>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>


<table border="0" cellpadding="0" cellspacing="0" width="500">
       <tbody>
<c:if test="${addEmployeeForm.hosBasedVesting ==true}">
			<!-- Vested years of service -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td width="1" class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				  Vested years of service
				  </td>
				 <td width="26" align="right">
  				  <ps:fieldHilight name="<%=EmployeeSnapshotProperties.PreviousYearsOfService%>"/>
				 </td>		         
				<td width="1" class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="297" class="highlight">	        	    
<form:input path="values['previousYearsOfService']" disabled="${disabled}" maxlength="3" onchange="validatePreviousYrsOfServiceInAdd(this)" onkeyup="pyosChanged(this.form)" size="3" cssStyle="boxbody"/>




	           	    <ps:trackChanges escape="true" property='value(previousYearsOfService)'/>
				</td>
				<td width="1" class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<!-- Vested years of service effective date  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				Vested years of service effective date
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.PreviousYearsOfServiceEffectiveDate%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="297" class="highlight">	        	   
 <form:select path="values['previousYearsOfServiceEffectiveDate']" disabled="${disablePrevioysYrsEffDate}">

<form:options items="${previousYearsOptions}"/>
</form:select>
				      <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           		    <ps:trackChanges escape="true" property='value(previousYearsOfServiceEffectiveDate)'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       
</c:if>
			<!-- Fully Vested Ind-->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				  Fully vested ?
				  </td>
				 <td width="26" align="right">
  				  <ps:fieldHilight name="<%=EmployeeSnapshotProperties.FullyVestedInd%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="297" class="highlight">	        	    			
<form:radiobutton disabled="${disabled}" onclick="fullyVestedChanged(this.form)" path="values['fullyVestedInd']" value="Y">


						Yes
</form:radiobutton>
<form:radiobutton disabled="${disabled}" onclick="fullyVestedChanged(this.form)" path="values['fullyVestedInd']" value="N">


						No
</form:radiobutton>
	           	    <ps:trackChanges escape="true" property='value(fullyVestedInd)'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<!-- Fully Vested override effective date  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				  Fully vested effective date 
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.FullyVestedIndEffectiveDate%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="297" class="highlight">	        	   
<form:input path="values['fullyVestedIndEffectiveDate']" disabled="{disabled}" maxlength="10" onchange="validateFullyVestedEffDate(this)" size="10" cssClass="inputAmount"/>



			   			&nbsp;
			   		  <span id="fullyVestedEffCal" style="<c:out value='${showFullyVestedCal}'/>">
					  <a href="javascript:doCalendar('value(fullyVestedIndEffectiveDate)',0)">
					  <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
					  (mm/dd/yyyy)
					  </span>
	           		    <ps:trackChanges escape="true" property='value(fullyVestedIndEffectiveDate)'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       
			
		    <!-- table footer border  -->
	        <tr>
	          <td height="1" colspan="6" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	        </tr>			      
       </tbody>
</table>
