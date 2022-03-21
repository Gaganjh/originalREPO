<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/java-encoder.tld" prefix="e" %>

<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_PREVIOUS_VALUE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ShowPrevValue" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_HIDE_PREVIOUS_VALUE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="HidePrevValue" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_GENERAL_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="GeneralSection" />


<%

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<c:set var="firstName" value="${editEmployeeForm.getValue('firstName')}"/>
<c:set var="lastName" value="${editEmployeeForm.getValue('lastName')}"/>
<c:set var="middleInitial" value="${editEmployeeForm.getValue('middleInitial')}"/>

<c:set var="disabled" value="${editEmployeeForm.readOnly}" /> 

<c:set var="showCensusHistoryValue" value="${sessionScope.USER_KEY.showCensusHistoryValue}" scope="request"/>

<c:choose >
  <c:when test="${editEmployeeForm.expandBasic}">
     <c:set var="expandStyle" value="DISPLAY: block"/>
     <c:set var="statusDisplay" value="DISPLAY: none"/>
     <c:set var="expandIcon" value="/assets/unmanaged/images/minus_icon.gif"/>     
  </c:when>
  <c:otherwise>
     <c:set var="expandStyle" value="DISPLAY: none"/>
     <c:set var="statusDisplay" value="DISPLAY: block"/>
     <c:set var="expandIcon" value="/assets/unmanaged/images/plus_icon.gif"/>     
  </c:otherwise>
</c:choose>

<c:choose>
  <c:when test="${!showCensusHistoryValue}">
    <c:set var="dataColWidth" value="371" scope="request"/>
    <input type="hidden" name="showCensusHistoryValue"  value="true"/>
    <c:set var="showHistoryLabel">    
      <content:getAttribute id='ShowPrevValue' attribute='text'/>
    </c:set>
  </c:when>
  <c:otherwise>
    <c:set var="dataColWidth" value="240" scope="request"/>
    <input type="hidden" name="showCensusHistoryValue" value="false"/>
    <c:set var="showHistoryLabel">
      <content:getAttribute id='HidePrevValue' attribute='text'/>
    </c:set> 
  </c:otherwise>
</c:choose>

<table class="box" cellSpacing="0" cellPadding="0" width="500" border="0">
	<tbody>
	<tr>
	<tr>
      <td width=7 bgcolor="#FFF9F2">&nbsp;</td>
	  <td bgcolor="#FFF9F2" width="332">
	  <a href="javascript:expandAll('true')"><img src="/assets/unmanaged/images/plus_icon_all.gif" border="0"/></a>
		/
	  <a href="javascript:expandAll('false')"><img src="/assets/unmanaged/images/minus_icon_all.gif" border="0"/></a>		
	   <b>All sections</b>
	 </td>
	 <td align="right" bgcolor="#FFF9F2">
		  <a href="javascript:doSubmit('showPreviousValue')">
		    <c:out value="${showHistoryLabel}"/>
		  </a>
	 </td>
	</tr>
	</tbody>
</table>

<br>
<input type="hidden" name="showLinkToParticipantAcct" /><%--  input - name="editEmployeeForm" --%>
<table border="0" cellpadding="0" cellspacing="0" width="500">
    <tbody>
			<tr class="tablehead">
				<td class="tableheadTD1" width="342">
				    <a href="javascript:toggleSection('basic', 'headerBasic', 'expandBasic', 'expandBasicIcon')">
				       <img id="expandBasicIcon" src="<c:out value='${expandIcon}'/>" border="0"/></a>&nbsp;
					<b><content:getAttribute id='GeneralSection' attribute='text'/> 
					<c:set var="contractStatus" value="${userProfile.currentContract.status}"/>
					<c:choose>				 
					<c:when test="${editEmployeeForm.showLinkToParticipantAcct && contractStatus!='PS' && contractStatus!='DC' && contractStatus!='PC' && contractStatus!='CA'}">
<a href="../../participant/participantAccount/?profileId=${e:forHtmlContent(editEmployeeForm.profileId)}">
		             <c:out value="${fn:toUpperCase(lastName)}"/>,
					 <c:out value="${fn:toUpperCase(firstName)}"/>
					 <c:out value="${fn:toUpperCase(middleInitial)}"/>
					</a>
					</c:when>
					<c:otherwise>
		             <c:out value="${fn:toUpperCase(lastName)}"/>,
					 <c:out value="${fn:toUpperCase(firstName)}"/>
					 <c:out value="${fn:toUpperCase(middleInitial)}"/>
					</c:otherwise>
					</c:choose>
					</b> 
	            </td>
	            <td class="tableheadTD"> 
	               <span id="headerBasic" style="<c:out value='${statusDisplay }'/>">
					<b>SSN:</b> <render:ssn property="editEmployeeForm.value(socialSecurityNumber)" defaultValue=""/>
				   </span>
	            </td>
		</tr>
    </tbody>
</table>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<form:hidden path="expandBasic"/>

<div id="basic" style="<c:out value='${expandStyle}'/>">
<table border="0" cellpadding="0" cellspacing="0" width="500">
     <tbody>
		<!-- SSN -->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" width="1">
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="100">
				SSN
			</td>
			<td width="26" align="right">
 			  <ps:fieldHilight name="<%=EmployeeSnapshotProperties.SSN%>"/>
			</td>
			
			<td class="greyborder" width="1"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight" width="<c:out value="${dataColWidth}"/>">
			    <c:choose>
			     <c:when test="${securityProfile.internalUser}">
<form:input path="ssn.ssn1" disabled="${disabled}" maxlength="3" onkeyup="return (validateSsn(this, invalidSsnError) && autoTab(this, 3, event))" size="3"/>


	        	    &nbsp;
<form:input path="ssn.ssn2" disabled="${disabled}" maxlength="2" onkeyup="return (validateSsn(this, invalidSsnError) && autoTab(this, 2, event))" size="2"/>


	        	    &nbsp;
<form:input path="ssn.ssn3" disabled="${disabled}" maxlength="4" onkeyup="return validateSsn(this, invalidSsnError)" size="4"/>




	           	    <ps:trackChanges escape="true" addHiddenField="false" property='ssn.ssn1' name="editEmployeeForm"/>
	           	    <ps:trackChanges escape="true" property='ssn.ssn2' name="editEmployeeForm"/>
	           	    <ps:trackChanges escape="true" property='ssn.ssn3' name="editEmployeeForm"/>
	           	 </c:when>
	           	 <c:otherwise>
	           	    <render:ssn property="editEmployeeForm.value(socialSecurityNumber)" defaultValue=""/>
	           	 </c:otherwise>
           	    </c:choose>           	    
			</td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder" width="1"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['socialSecurityNumber']}"/>				
				<td class="highlight" width="130" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				      <c:if test="${change.previousValue != null}">					
			           	    <render:ssn property="change.previousValue" defaultValue=""/>
					  </c:if>
				 </td>
			 </c:if>
			<td class="databorder" width="1">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>

		<!-- Prefix -->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="100">
				Prefix
			</td>
			<td width="26" align="right">
 			  <ps:fieldHilight name="<%=EmployeeSnapshotProperties.Prefix%>"/>
			</td>
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">   
			
        	
   <form:select path="values[prefix]"  cssStyle="boxbody" disabled="${disabled}">
<form:options items="${editEmployeeForm.namePrefixOptions}" itemLabel="label" itemValue="value" />
</form:select>  
          	  
           	    <ps:trackChanges escape="false" collectionProperty="true" property='values[prefix]' name="editEmployeeForm"/>
			</td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				
				<c:set var="change" value="${employeeHistory.propertyMap['prefix']}"/>				
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				      <c:if test="${change.previousValue != null}">					
${change.previousValue}
					  </c:if>
				 </td>
			 </c:if>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>
		<!-- First Name-->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="100">
				First name
			</td>
			<td width="26" align="right">
			    <ps:fieldHilight name="<%=EmployeeSnapshotProperties.FirstName%>"/>
			</td>
			<td class="greyborder"> 
			    <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">
			
 

<form:input  path="values[firstName]" disabled="${disabled}" maxlength="18" onchange="validateFirstName(this)"/>  
	    
	       	    
	       	    <ps:trackChanges escape="true" collectionProperty="true" property='values[firstName]' name="editEmployeeForm"/>
			</td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['firstName']}"/>				
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				      <c:if test="${change.previousValue != null}">					
${change.previousValue}
					  </c:if>
				 </td>
			 </c:if>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>

		<!-- Middle Initial-->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="100">			    
				Middle initial
			</td>
			<td width="26" align="right">
			    <ps:fieldHilight name="<%=EmployeeSnapshotProperties.MiddleInitial%>"/>
			</td>
			
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">	
<form:input path="values[middleInitial]" disabled="${disabled}" maxlength="1" onchange="validateMiddleInit(this)" size="1"/> 




           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[middleInitial]' name="editEmployeeForm"/>
			</td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['middleInitial']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				      <c:if test="${change.previousValue != null}">					
${change.previousValue}
					  </c:if>
				 </td>
			 </c:if>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>
		<!-- Last Name-->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="100">
				Last name
			</td>
			<td width="26" align="right">
			    <ps:fieldHilight name="<%=EmployeeSnapshotProperties.LastName%>"/>
			</td>
			
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">						        	    
<form:input path="values[lastName]" disabled="${disabled}" maxlength="20" onchange="validateLastName(this)"/> 


           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[lastName]' name="editEmployeeForm"/> 
			</td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['lastName']}"/>				
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">					  
				      <c:if test="${change.previousValue != null}">					
${change.previousValue}
					  </c:if>
				 </td>
			 </c:if>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>


		<!-- Employee ID -->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="100">
				Employee ID
			</td>
			<td width="26" align="right">
			    <ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmployeeId%>"/>
			</td>
			
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">
        	    
<form:input path="values[employeeId]" disabled="${disabled}" maxlength="9" onchange="validateEmployeeId(this)"/> 



           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[employeeId]' name="editEmployeeForm"/>
			</td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['employeeId']}"/>				
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">					  
				      <c:if test="${change.previousValue != null}">					
                        <%-- The following javascript is used to preserve spaces in the output. --%>
                        <script type="text/javascript">
                          document.writeln('${change.previousValue}'.replace(/ /g,'&nbsp;'));
                        </script>
					  </c:if>
				 </td>
			 </c:if>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>
		<!-- Birth date -->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="100">
				Date of birth
			</td>
			<td width="26" align="right">
			    <ps:fieldHilight name="<%=EmployeeSnapshotProperties.BirthDate%>"/>
			</td>
			
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">
 <form:input path="values[birthDate]" disabled="${disabled}" maxlength="10" onchange="validateBirthDate(this)" size="10" cssClass="inputAmount" id="value(birthDate)"/> 

		   			&nbsp;
		   		  <c:if test="${!disabled}">
					  <a href="javascript:doCalendar('value(birthDate)',0)">
					  <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
				  (mm/dd/yyyy)
				  </c:if>
			      <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[birthDate]' name="editEmployeeForm"/>
			</td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>				
			   <c:set var="change" value="${employeeHistory.propertyMap['birthDate']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				      <c:if test="${change.previousValue != null}">					
							<fmt:formatDate value="${change.previousValueAsDate}" pattern="MM/dd/yyyy"/>
					  </c:if>
				 </td>
			 </c:if>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>		
		<!-- table footer border -->
        <tr>
          <td height="1" colspan="6" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <c:if test="${userProfile.showCensusHistoryValue}">
          <td height="1" colspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          </c:if>
        </tr>			
     </tbody>
</table>

</div>
<SCRIPT language=javascript>
<!--
	
	function doCalendar(fieldName) {
	
		// Retrieve the field
		var field = document.getElementById(fieldName);
	
		// Pre-Validate date and blank if not valid
		if (!validateDate(field.value)) {
			field.value = "";
		}
		cal = new calendar(document.forms['editEmployeeForm'].elements[fieldName]);
		cal.year_scoll = true;
		cal.time_comp = false;
		cal.popup();
	}
//-->
</SCRIPT>
