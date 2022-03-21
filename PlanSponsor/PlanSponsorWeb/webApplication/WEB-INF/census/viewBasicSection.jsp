<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
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

<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<input type='hidden' name='profileId' value='<c:out value="${employee.employeeDetailVO.profileId}"/>' />



<c:set var="showCensusHistoryValue" value="${sessionScope.USER_KEY.showCensusHistoryValue}" scope="request"/>
<c:choose>
  <c:when test="${!showCensusHistoryValue}">
    <c:set var="dataColWidth" value="347" scope="request"/>
    <input type="hidden" name="showCensusHistoryValue"  value="true"/>
    <c:set var="showHistoryLabel">    
      <content:getAttribute id='ShowPrevValue' attribute='text'/>
    </c:set>
  </c:when>
  <c:otherwise>
    <c:set var="dataColWidth" value="172" scope="request"/>
    <input type="hidden" name="showCensusHistoryValue" value="false"/>
    <c:set var="showHistoryLabel">
      <content:getAttribute id='HidePrevValue' attribute='text'/>
    </c:set> 
  </c:otherwise>
</c:choose>

<c:choose >
  <c:when test="${param.printFriendly == true || employeeForm.expandBasic}">
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

<c:if test="${not param.printFriendly}">
<table class="box" cellSpacing="0" cellPadding="0" width="500" border="0">
	<tbody>
	<tr>
      <td width=7 bgcolor="#FFF9F2">&nbsp;</td>
	  <td bgcolor="#FFF9F2" width="342">
	  <a href="javascript:expandAll('true')"><img src="/assets/unmanaged/images/plus_icon_all.gif" border="0"/></a>
		/
	  <a href="javascript:expandAll('false')"><img src="/assets/unmanaged/images/minus_icon_all.gif" border="0"/></a>		
	   <b>All sections</b>
	 </td>
	 <td width=151 align="right" bgcolor="#FFF9F2">
		  <a href="javascript:doSubmit('showPreviousValue')">
		    <c:out value="${showHistoryLabel}"/>
		  </a>
	 </td>
	</tr>
	</tbody>
</table>
</c:if>
<br>

<table class="box" cellSpacing="0" cellPadding="0" width="500" border="0">
		<tbody>
		<c:choose>
			<c:when test="${param.printFriendly}">
					<tr class="tablehead">
						<td class="tableheadTD1" width="500">
							<b><content:getAttribute id='GeneralSection' attribute='text'/> 
							 <c:out value="${fn:toUpperCase(employee.employeeDetailVO.lastName)}"/>,
						     <c:out value="${fn:toUpperCase(employee.employeeDetailVO.firstName)}"/>
						     <c:out value="${fn:toUpperCase(employee.employeeDetailVO.middleInitial)}"/>
						</b> 
					</td>
				</tr>
			</c:when>
			<c:otherwise>
					<tr class="tablehead">
						<td class="tableheadTD1" nowrap="nowrap" width="342">
						    <a href="javascript:toggleSection('basic', 'headerBasic', 'expandBasic', 'expandBasicIcon')">
						       <img id="expandBasicIcon" src="<c:out value='${expandIcon}'/>" border="0"/></a>
						    &nbsp;
							<b><content:getAttribute id='GeneralSection' attribute='text'/>
              <c:set var="contractStatus" value="${userProfile.currentContract.status}"/>
							<c:choose>				 
							<c:when test="${(employee.participantAccountInd || employee.participantContract.participantStatusCode=='NT')&& contractStatus!='PS' && contractStatus!='DC' && contractStatus!='PC' && contractStatus!='CA'}">
							<a class="color:white;" href="/do/participant/participantAccount/?profileId=${employee.employeeDetailVO.profileId}">
								<c:out value="${fn:toUpperCase(employee.employeeDetailVO.lastName)}"/>,
							     <c:out value="${fn:toUpperCase(employee.employeeDetailVO.firstName)}"/>
							     <c:out value="${fn:toUpperCase(employee.employeeDetailVO.middleInitial)}"/>
							</a>
							</c:when>
							<c:otherwise>
								<c:out value="${fn:toUpperCase(employee.employeeDetailVO.lastName)}"/>,
							     <c:out value="${fn:toUpperCase(employee.employeeDetailVO.firstName)}"/>
							     <c:out value="${fn:toUpperCase(employee.employeeDetailVO.middleInitial)}"/>
							</c:otherwise>
							</c:choose>
							</b>
						</td>
						<td class="tableheadTD">
						  <span  id="headerBasic" style="<c:out value='${statusDisplay}'/>">
							<b>SSN:</b> <render:ssn property="employee.employeeDetailVO.socialSecurityNumber" defaultValue=""/>
						  </span>
						<td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<form:hidden path="expandBasic"/>


<div id="basic" style="<c:out value='${expandStyle}'/>">
<c:remove var="expandStyle"/>
	<table cellSpacing="0" cellPadding="0" width="500" border="0" class="greyborder">
		<tbody>		      
			<!-- SSN -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class=databorder width="1">
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">
					SSN
				</td>
			   <td width="26" align="right">
					<ps:fieldHilight name="<%=EmployeeSnapshotProperties.SSN%>"/>
			   </td>		         
				<td class="greyborder" width="1"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['socialSecurityNumber']}"/>
				<td class="highlight" width="<c:out value='${dataColWidth}'/>" onmouseover="<ps:employeeChangeDetail name='change'/>">
				  	<render:ssn property="employee.employeeDetailVO.socialSecurityNumber" defaultValue=""/>
				</td>
				<c:if test="${showCensusHistoryValue}">
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>				
				    <c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" width="172" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
							 	<render:ssn property='previousValue'/>
						 </c:if>
					</td>
				</c:if>
				<td class="databorder">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>
			<!-- Prefix -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder">
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">
					Prefix
				</td>
			   <td width="26" align="right">
					<ps:fieldHilight name="<%=EmployeeSnapshotProperties.Prefix%>"/>
			   </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['prefix']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">				    
${employee.employeeDetailVO.namePrefix}
				</td>
				<c:if test="${showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>				
				    <c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	 <c:out value="${previousValue}"/>
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
				<td width="124">
					First name
				</td>
			   <td width="26" align="right">
				    <ps:fieldHilight name="<%=EmployeeSnapshotProperties.FirstName%>"/>
			   </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['firstName']}"/>				
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
${employee.employeeDetailVO.firstName}
				</td>
				<c:if test="${showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>				
				    <c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	<c:out value="${previousValue}"/>
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
				<td width="124">
					Middle initial
				</td>
			   <td width="26" align="right">
				    <ps:fieldHilight name="<%=EmployeeSnapshotProperties.MiddleInitial%>"/>
			   </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['middleInitial']}"/>				
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
${employee.employeeDetailVO.middleInitial}
				</td>
				<c:if test="${showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>				
				    <c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	<c:out value="${previousValue}"/>
						  </c:if>
					 </td>
				 </c:if>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>
			<!-- Last Name -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">
					Last name
				</td>
			   <td width="26" align="right">
				    <ps:fieldHilight name="<%=EmployeeSnapshotProperties.LastName%>"/>
			   </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['lastName']}"/>				
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
${employee.employeeDetailVO.lastName}
				</td>
				<c:if test="${showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>				
				    <c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	<c:out value="${previousValue}"/>
						 </c:if>
				     </td>			
				</c:if>	
				<td class="databorder">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>
			<!-- Employee Id -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder">
				  <img height="1"	src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">
					Employee ID
				</td>
			   <td width="26" align="right">
					<ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmployeeId%>"/>
			   </td>		         
				<td class="greyborder">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			   <c:set var="change" value="${employeeHistory.propertyMap['employeeId']}"/>		
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
				  <%-- The following javascript is used to preserve spaces in the output. --%>
				  <script type="text/javascript">
                    document.writeln('${employee.employeeDetailVO.employeeId}'.replace(/ /g,'&nbsp;'));
                  </script>
				</td>
				<c:if test="${showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>				
				    <c:set var="previousValue" value="${change.previousValue}"/>					
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					  <c:if test="${previousValue != null}">
                        <%-- The following javascript is used to preserve spaces in the output. --%>
                        <script type="text/javascript">
                          document.writeln('${previousValue}'.replace(/ /g,'&nbsp;'));
                        </script>
					  </c:if>
					</td>
				</c:if>
				<td class="databorder">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>			

			<!-- Birth date-->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class=databorder width=1>
				  <img height="1"	src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td>
					Date of birth
				</td>
			   <td width="26" align="right">
					<ps:fieldHilight name="<%=EmployeeSnapshotProperties.BirthDate%>"/>
			   </td>		         
				<td class="greyborder">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				 <c:set var="change" value="${employeeHistory.propertyMap['birthDate']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
				     <render:date patternOut="MM/dd/yyyy" property="employee.employeeDetailVO.birthDate"/>&nbsp;
				</td>
				<c:if test="${showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>				
			        <c:set var="previousValue" value="${change.previousValueAsDate}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					     <c:if test="${previousValue != null}">
						 	<render:date  property='previousValue' patternOut="MM/dd/yyyy"/>
						 </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
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
