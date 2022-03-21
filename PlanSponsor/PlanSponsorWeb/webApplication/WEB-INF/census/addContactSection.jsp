<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
 
 <c:choose >
  <c:when test="${addEmployeeForm.countryUSA}">
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
       <b><content:getAttribute id="ContactSection" attribute="text"/></b>
       </td>
       <td class="tableheadTD" id="headerContact">
       	&nbsp;
       </td>
     </tr>
   </tbody>
 </table>
  
<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<c:set var="disabled" value="${addEmployeeForm.readOnly}" /> 

     <table border="0" cellpadding="0" cellspacing="0" width="500">
       <tbody>
	         <tr>
				<td class=databorder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>		         
	           <td colspan="4" class="tablesubhead" >
	            <b><content:getAttribute id="MailingAddressSection" attribute="text"/></b>
	           </td>
				<td class=databorder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
	         </tr>
               <!--  address line1 -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="174">
		         Address line 1
		         </td>
				 <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.AddressLine1%>"/>
				 </td>		         
		         <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td width="297" class="highlight">	        	   
<form:input path="values[addressLine1]" disabled="${disabled}" maxlength="30" onchange="validateAddressLine1(this)" cssClass="inputAmount"/>



				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges name="addEmployeeForm"  property='values[addressLine1]' escape="true"/>
				 </td>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  address line 2 -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="174">
		         Address line 2
		         </td>
				 <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.AddressLine2%>"/>
				 </td>		         
		         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight">	        	   
<form:input path="values[addressLine2]" disabled="${disabled}" maxlength="30" onchange="validateAddressLine2(this)" cssClass="inputAmount"/>



				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges name="addEmployeeForm" property='values[addressLine2]' escape="true"/>			 	
				 </td>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  City -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="174">
		         City
		         </td>
				 <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.City%>"/>
				 </td>		         
		         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight">	        	   
<form:input path="values[city]" disabled="${disabled}" maxlength="25" onchange="validateCity(this)" cssClass="inputAmount"/>



				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges name="addEmployeeForm" property='values[city]' escape="true" />			 	
				 </td>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  State  -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="174">
		         State
		         </td>
				 <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.State%>"/>
				 </td>		         
		         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight">
				   <span id="stateUSA" style="<c:out value='${stateUSA}'/>">
				   
 <form:select path="state.stateUSA" cssStyle="boxbody" disabled="${disabled}">
<form:options items="${addEmployeeForm.stateList}" itemLabel="label" itemValue="value"/> 
</form:select>
				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges name="addEmployeeForm" property='state.stateUSA' escape="true"/>			 	
	           	   </span>
				   <span id="stateNonUSA" style="<c:out value='${stateNonUSA}'/>">
<form:input path="state.stateNonUSA" disabled="${disabled}" maxlength="2" onchange="validateState(this)" size="2" cssStyle="boxbody"/>


				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges name="addEmployeeForm" property='state.stateNonUSA' escape="true"/>			 	
	           	   </span>
				 </td>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  Zip code  -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="174">
		         Zip code
		         </td>
				 <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.ZipCode%>"/>
				 </td>		         
		         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight">	        	  
				   <span id="zipCodeUSA" style="<c:out value='${zipCodeUSA}'/>">
<form:input path="zipCode.zipCode1" disabled="${disabled}" maxlength="5" onchange="validateField(this, validateZipCode1, zipCode1FormatError)" size="5" />

-<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">


<form:input path="zipCode.zipCode2" disabled="${disabled}" maxlength="4" onchange="validateField(this, validateZipCode2, zipCode2FormatError)" size="4"/>



				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges name="addEmployeeForm" property='zipCode.zipCode1' escape="true"/>
	           	   <ps:trackChanges name="addEmployeeForm" property='zipCode.zipCode2' escape="true"/>	           	   
	           	   </span>
				   <span id="zipCodeNonUSA" style="<c:out value='${zipCodeNonUSA}'/>">
<form:input path="zipCode.zipCodeNonUSA" disabled="${disabled}" maxlength="9" onchange="validateField(this, validateNonPrintableAscii, nonPrintableZipCode)" size="9"/>



					   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		           	   <ps:trackChanges name="addEmployeeForm" property='zipCode.zipCodeNonUSA' escape="true"/>			 	
	           	   </span>
				 </td>
				<td class="databorder">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>

               <!--  Country  -->
		       <tr class="<c:out value="${rowStyle.next}"/>">
		         <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		         <td width="174">
		         Country
		         </td>
				 <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.Country%>"/>
				 </td>		         
		         <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				 <td class="highlight">	        	   
 <form:select path="values[country]" style="boxbody" disabled="${disabled}" onchange="changeCountry(this)">


<form:options items="${addEmployeeForm.countryOptions}" itemLabel="label" itemValue="value"/>
</form:select>
				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	   <ps:trackChanges name="addEmployeeForm" property='values[country]' escape="true"/>			 	
				 </td>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		       </tr>
	         <tr>
				<td class=databorder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>		         
	           <td colspan="4" class="tablesubhead" >
	            <b><content:getAttribute id="AdditionalContactSection" attribute="text"/></b>
	           </td>
				<td class=databorder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
	         </tr>

              <!--  State of residence -->
	       <tr class="<c:out value="${rowStyle.next}"/>">
	         <td class="databorder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	         <td width="174">
	         State of residence	         
	         </td>
			 <td width="26" align="right">
	         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.StateOfResidence%>"/>
			 </td>		         
	         <td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
			 <td class="highlight" width="297">        	   
                  <form:select path="values[stateOfResidence]" cssStyle="boxbody" disabled="${disabled}">
<form:options items="${addEmployeeForm.stateOfResidenceList}" itemLabel="label" itemValue="value"/>
</form:select>
			   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
           	   <ps:trackChanges name="addEmployeeForm" property='values[stateOfResidence]' escape="true"/>			 	
			 </td>
			<td class="databorder" width="1">
			 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
	       </tr>

              <!--  Employer provided email address   -->
	       <tr class="<c:out value="${rowStyle.next}"/>">
	         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	         <td width="174">
	         Employer provided email address
	         </td>
			 <td width="26" align="right">
	         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmailAddress%>"/>
			 </td>		         
	         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
			 <td class="highlight">        	   
<form:input path="values[emailAddress]" disabled="${disabled}" maxlength="99" onchange="validateEmailAddress(this)"/>


			   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
           	   <ps:trackChanges name="addEmployeeForm" property='values[emailAddress]' escape="true"/>			 	
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
