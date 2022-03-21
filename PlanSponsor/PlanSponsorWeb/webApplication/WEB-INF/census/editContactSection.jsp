<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
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
 
<c:choose >
  <c:when test="${editEmployeeForm.expandContact}">
     <c:set var="expandStyle" value="DISPLAY: block"/>
     <c:set var="expandIcon" value="/assets/unmanaged/images/minus_icon.gif"/>    
  </c:when>
  <c:otherwise>
     <c:set var="expandStyle" value="DISPLAY: none"/>
     <c:set var="expandIcon" value="/assets/unmanaged/images/plus_icon.gif"/>
  </c:otherwise>
</c:choose>

 <c:choose >
  <c:when test="${editEmployeeForm.countryUSA}">
     <c:set var="zipCodeUSA" value="DISPLAY: block"/>
     <c:set var="zipCodeNonUSA" value="DISPLAY: none"/>
     <c:set var="stateUSA" value="DISPLAY: block"/>
     <c:set var="stateNonUSA" value="DISPLAY: none"/>
  </c:when>
  <c:otherwise>
     <c:set var="zipCodeUSA" value="DISPLAY: none"/>
     <c:set var="zipCodeNonUSA" value="DISPLAY: block"/>
     <c:set var="stateUSA" value="DISPLAY: none"/>
     <c:set var="stateNonUSA" value="DISPLAY: block"/>
  </c:otherwise>
</c:choose>


<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
   <tbody>
     <tr class="tablehead">
       <td class="tableheadTD1">
          <a href="javascript:toggleSection('contact', 'headerContact','expandContact', 'expandContactIcon')">
			<img id="expandContactIcon" src="<c:out value='${expandIcon}'/>" border="0"/></a>
  		&nbsp;
       <b><content:getAttribute id="ContactSection" attribute="text"/>  </b>
       </td>
       <td class="tableheadTD" id="headerContact">
         &nbsp;
       </td>
     </tr>
   </tbody>
 </table>
  
<c:set var="disabled" value="${editEmployeeForm.readOnly}" /> 
  
<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<form:hidden path="expandContact"/>
<div id="contact" style="<c:out value='${expandStyle}'/>">
     <table border="0" cellpadding="0" cellspacing="0" width="500">
       <tbody>
	         <tr>
				<td class=databorder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>		         
	           <td colspan="4" class="tablesubhead" >
	            <b><content:getAttribute id="MailingAddressSection" attribute="text"/> </b>
	           </td>
	            <c:if test="${userProfile.showCensusHistoryValue}">
	              <td height="1" colspan="2" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	           </c:if>
				<td class=databorder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
	         </tr>
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="100">		         
		         Address line 1
		         </td>
				 <td width="26" align="right">
				 <ps:fieldHilight name="<%=EmployeeSnapshotProperties.AddressLine1%>"/>
				 </td>
		         <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td width="<c:out value="${dataColWidth}"/>" class="highlight">	        	   
 <form:input path="values[addressLine1]" disabled="${disabled}" maxlength="30" onchange="validateAddressLine1(this)" cssClass="inputAmount"/> 




				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges collectionProperty="true" property='values[addressLine1]' escape="true" encode="true" name="editEmployeeForm"/>			 	
				 </td>
				 <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
				    <c:set var="change" value="${employeeHistory.propertyMap['addressLine1']}"/>
					<td class="highlight" width="130" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
								<c:out value="${change.previousValue}"/>
						  </c:if>
						   &nbsp;
					 </td>
				 </c:if>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  address line 2 -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="100">				
		         Address line 2
		         </td>
				 <td width="26" align="right">
				 <ps:fieldHilight name="<%=EmployeeSnapshotProperties.AddressLine2%>"/>		         
				 </td>
		         
		         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight">	        	   
<form:input path="values[addressLine2]" disabled="${disabled}" maxlength="30" onchange="validateAddressLine2(this)" cssClass="inputAmount"/> 



				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges collectionProperty="true" property='values[addressLine2]' escape="true" encode="true" name="editEmployeeForm"/>			 	
				 </td>
				 <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
				    <c:set var="change" value="${employeeHistory.propertyMap['addressLine2']}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
								<c:out value="${change.previousValue}"/>
						  </c:if>
						   &nbsp;
					 </td>
				 </c:if>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  City -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="100">		         
		         City
		         </td>
				 <td width="26" align="right">
				 <ps:fieldHilight name="<%=EmployeeSnapshotProperties.City%>"/>
				 </td>		         
		         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight">	        	   
 <form:input path="values[city]" disabled="${disabled}" maxlength="25" onchange="validateCity(this)" cssClass="inputAmount"/> 



				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges collectionProperty="true" property='values[city]' escape="true" name="editEmployeeForm"/>			 	
				 </td>
				 <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="change" value="${employeeHistory.propertyMap['city']}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
					      <c:if test="${change.previousValue != null}">					
								<c:out value="${change.previousValue}"/>
						  </c:if>
						   &nbsp;
					 </td>
				 </c:if>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  State  -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="100">
		         State
		         </td>
				 <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.State%>"/>
				 </td>		         
		         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight">	
				       	   
				   <span id="stateUSA" style="<c:out value='${stateUSA}'/>">
                  <form:select path="state.stateUSA" cssStyle="boxbody"  disabled="${disabled}">
					<form:options items="${editEmployeeForm.stateList}" itemLabel="label" itemValue="value"/>
						</form:select> 
				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	    <ps:trackChanges property='state.stateUSA' escape="true" name="editEmployeeForm"/>	 	
	           	   </span>
				   <span id="stateNonUSA" style="<c:out value='${stateNonUSA}'/>">
<form:input path="state.stateNonUSA" disabled="${disabled}" maxlength="2" onchange="validateState(this)" size="2" cssStyle="boxbody"/>



				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	    <ps:trackChanges property='state.stateNonUSA' escape="true" name="editEmployeeForm"/> 			 	
	           	   </span>
				 </td>
				 <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="change" value="${employeeHistory.propertyMap['state']}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
					      <c:if test="${change.previousValue != null}">					
								<c:out value="${change.previousValue}"/>
						  </c:if>
						   &nbsp;
					 </td>
				 </c:if>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  Zip code  -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="100">		         
		         Zip code
		         </td>
				 <td width="26" align="right">
				 <ps:fieldHilight name="<%=EmployeeSnapshotProperties.ZipCode%>"/>
				 </td>		         
		         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight">
				   <span id="zipCodeUSA" style="<c:out value='${zipCodeUSA}'/>">
<form:input path="zipCode.zipCode1" disabled="${disabled}" maxlength="5" onchange="validateField(this, validateZipCode1, zipCode1FormatError)" size="5" -/>




<form:input path="zipCode.zipCode2" disabled="${disabled}" maxlength="4" onchange="validateField(this, validateZipCode2, zipCode2FormatError)" size="4"/>



				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	    <ps:trackChanges property='zipCode.zipCode1' escape="true" name="editEmployeeForm"/>
	           	   <ps:trackChanges property='zipCode.zipCode2' escape="true" name="editEmployeeForm"/> 	          	   
	           	   </span>
				   <span id="zipCodeNonUSA" style="<c:out value='${zipCodeNonUSA}'/>">
<form:input path="zipCode.zipCodeNonUSA" disabled="${disabled}" maxlength="9" onchange="validateField(this, validateNonPrintableAscii, nonPrintableZipCode)" size="9"/>



					   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		           	   <ps:trackChanges property='zipCode.zipCodeNonUSA' escape="true" name="editEmployeeForm"/> 			 	
	           	   </span>
				 </td>
				 <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="change" value="${employeeHistory.propertyMap['zipCode']}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
					      <c:if test="${change.previousValue != null}">					
<c:set var="zipCode" value="${change.previousValue}" /> 
					          <render:zip property="change.previousValue" defaultValue="zipCode "/>
						  </c:if>
						   &nbsp;
					 </td>
				 </c:if>
				<td class="databorder">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  Country  -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="100">		         
		         Country
		         </td>
				 <td width="26" align="right">
				 <ps:fieldHilight name="<%=EmployeeSnapshotProperties.Country%>"/>
				 </td>		         
		         <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight" width="<c:out value="${dataColWidth}"/>">
 <form:select path="values[country]" style="boxbody" disabled="${disabled}" onchange="changeCountry(this)">


<form:options items="${editEmployeeForm.countryOptions}" itemLabel="label" itemValue="value"/>
</form:select> 
				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges collectionProperty="true" property='values[country]' escape="true" name="editEmployeeForm"/>	 		 	
				 </td>
				 <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="change" value="${employeeHistory.propertyMap['country']}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
								<c:out value="${change.previousValue}"/>
						  </c:if>
						   &nbsp;
					 </td>
				 </c:if>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
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

               <!--  State of residence -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td class="databorder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="100">		         
		         State of residence
		         </td>
				 <td width="26" align="right">
				 <ps:fieldHilight name="<%=EmployeeSnapshotProperties.StateOfResidence%>"/>
				 </td>		         
		         <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight" width="<c:out value="${dataColWidth}"/>">	
				          	   
       <form:select path="values[stateOfResidence]" cssStyle="boxbody" disabled="${disabled}">
		<form:options items="${editEmployeeForm.stateOfResidenceList}" itemLabel="label" itemValue="value"/>
		</form:select> 
				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges collectionProperty="true" property='values[stateOfResidence]' escape="true" name="editEmployeeForm"/> 		 	
				 </td>
				 <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="change" value="${employeeHistory.propertyMap['stateOfResidence']}"/>				
					<td class="highlight" width="130" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
				     	      <c:out value="${change.previousValue}"/>
						  </c:if>
						   &nbsp;
					 </td>
				 </c:if>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  Employer provided email address   -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="100">		         
		         Employer provided email address 
		         </td>
				 <td width="26" align="right">
				 <ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmailAddress%>"/>
				 </td>		         
		         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight">	        	   
 <form:input path="values[emailAddress]" disabled="${disabled}" maxlength="99" onchange="validateEmailAddress(this)"/> 


				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	    <ps:trackChanges collectionProperty="true" property='values[emailAddress]' escape="true" name="editEmployeeForm"/> 			 	
				 </td>
				 <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="change" value="${employeeHistory.propertyMap['emailAddress']}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
								<c:out value="${change.previousValue}"/>
						  </c:if>
						   &nbsp;
					 </td>
				 </c:if>
				<td class="databorder">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>
		    <!-- table footer border  -->
	        <tr>
	          <td height="1" colspan="6" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	          <c:if test="${userProfile.showCensusHistoryValue}">
	          <td height="1" colspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	          </c:if>
	        </tr>			      
       </tbody>
</table>
</div> 
