<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CONTACT_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ContactSection" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_MAILING_ADDRESS_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="MailingAddressSection" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CONTACT_ADDITIONAL_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="AdditionalContactSection" />

<form:hidden path="expandContact"/>
<c:choose >
  <c:when test="${param.printFriendly == true || employeeForm.expandContact}">
    <c:set var="expandStyle" value="DISPLAY:block"/>
    <c:set var="expandIcon" value="/assets/unmanaged/images/minus_icon.gif"/>    
  </c:when>
  <c:otherwise>
    <c:set var="expandStyle" value="DISPLAY:none"/>
    <c:set var="expandIcon" value="/assets/unmanaged/images/plus_icon.gif"/>
</c:otherwise>
</c:choose>


<c:choose>
	<c:when test="${not param.printFriendly}">
	<table class=box cellSpacing=0 cellPadding=0
		width="500" border="0">
		<tbody>
			<tr class="tablehead">
				<td class="tableheadTD1" colSpan="3">
				    <a href="javascript:toggleSection('contact', 'headerContact','expandContact', 'expandContactIcon')">
				       <img id="expandContactIcon" src="<c:out value='${expandIcon}'/>" border="0"/></a>
				    &nbsp;
					<b><content:getAttribute id="ContactSection" attribute="text"/> </b>
				</td>
				<td id="headerContact" class="tableheadTD">
				   &nbsp;
				</td>
			</tr>
		</tbody>
	</table>
	</c:when>
	<c:otherwise>
		<table class=box cellSpacing=0 cellPadding=0
		width="500" border="0">
		<tbody>
			<tr class="tablehead">
				<td class="tableheadTD1" colSpan="3">
					<B><content:getAttribute id="ContactSection" attribute="text"/> </B>
				</td>
			</tr>
		</tbody>
	</table>	
	</c:otherwise>
</c:choose>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>


<div id="contact" style="<c:out value='${expandStyle}'/>">
<c:remove var="expandStyle"/>
	<table id=contactTable cellSpacing=0 cellPadding=0 width=500 border=0 class=greyborder>
		<tbody>
         <tr>
			<td class=databorder width=1>
			  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>		         
           <td colspan="4" class="tablesubhead" >
            <b><content:getAttribute id="MailingAddressSection" attribute="text"/> </b>
           </td>
            <c:if test="${showCensusHistoryValue}">
              <td height="1" colspan="2" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
           </c:if>
			<td class=databorder width=1>
			  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
         </tr>
		  <tr>
			<!-- Address Line 1 -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class=databorder width="1">
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		         <td width="124">
		         Address line 1
		         </td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.AddressLine1%>"/>
			   </td>		         
				<td class="greyborder" width="1"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['addressLine1']}"/>
				<td class="highlight" width="<c:out value='${dataColWidth}'/>" onmouseover="<ps:employeeChangeDetail name='change'/>">				  
${employee.addressVO.addressLine1}
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>
						  <c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" width="172" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	<c:out value="${previousValue}"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
	
			<!-- Address Line 2 -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
		        <td width="124">
		         Address line 2
		         </td>
			   <td width="26" align="right">
			 	<ps:fieldHilight name="<%=EmployeeSnapshotProperties.AddressLine2%>"/>		         
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			     <c:set var="change" value="${employeeHistory.propertyMap['addressLine2']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
${employee.addressVO.addressLine2}
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
				  <c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	<c:out value="${previousValue}"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
	
			<!-- City  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
		         <td width="124">
		         City
		         </td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.City%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['city']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">				     
${employee.addressVO.city}
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
				  <c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	<c:out value="${previousValue}"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>

			<!-- State -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
		         <td width="124">
		         State
		         </td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.State%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['state']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
${employee.addressVO.stateCode}
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
					      <c:if test="${previousValue != null}">					
					        <c:out value="${previousValue}"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
			<!-- Zip code -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
		         <td width="124">
		         Zip code
		         </td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.ZipCode%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['zipCode']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">				     
				        <c:if test="${not empty employee.addressVO.zipCode}">
<c:set var="zipCode" value="${employee.addressVO.zipCode}" /> 
<% String zipCode = pageContext.getAttribute("zipCode").toString();%>
				          <render:zip property="employee.addressVO.zipCode" defaultValue="<%=zipCode %>"/>
				        </c:if>
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
					      <c:if test="${previousValue != null}">					
<c:set var="zipCode" value="${change.previousValue}" /> 
<% String zipCode = pageContext.getAttribute("zipCode").toString();%>
					          <render:zip property="change.previousValue" defaultValue="<%=zipCode %>"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>

			  <!-- Country-->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
		         <td width="124">
		         Country
		         </td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.Country%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['country']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">				     
${employee.addressVO.country}
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
					      <c:if test="${previousValue != null}">					
				     		  <c:out value="${previousValue}"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
	
	         <tr>
				<td class=databorder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>		         
	           <td colspan="4" class="tablesubhead">
	            <b><content:getAttribute id="AdditionalContactSection" attribute="text"/> </b>
	           </td>
	            <c:if test="${showCensusHistoryValue}">
	              <td height="1" colspan="2" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	           </c:if>
				<td class="databorder" width="1">
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
	         </tr>
			<!-- State of Residence  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
		         <td width="124">
		         State of residence
		         </td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.StateOfResidence%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['stateOfResidence']}"/>
				<td class="highlight"  width="<c:out value='${dataColWidth}'/>" onmouseover="<ps:employeeChangeDetail name='change'/>">				     
${employee.employeeDetailVO.residenceStateCode}
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>				
					<td class="highlight" width="172" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
					      <c:if test="${previousValue != null}">					
				     		  <c:out value="${previousValue}"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>

			<!-- Email address-->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
		         <td width="124">
		         Employer provided email address 
		         </td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmailAddress%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['emailAddress']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
${employee.employeeDetailVO.employerProvidedEmailAddress}
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	<c:out value="${previousValue}"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
			<tr>
	              <td height="1" colspan="6" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	              <c:if test="${showCensusHistoryValue}">
	              <td height="1" colspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	              </c:if>
	        </tr>
		</tbody>
	</table>
</div>
