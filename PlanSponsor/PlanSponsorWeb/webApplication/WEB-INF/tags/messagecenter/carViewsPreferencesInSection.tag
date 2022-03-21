<%@tag import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"
       import="com.manulife.pension.ps.web.messagecenter.MCConstants"
	   import="com.manulife.pension.service.message.valueobject.MessagePreferenceError"
	   import="com.manulife.pension.ps.web.messagecenter.personalization.DisplayableMessageTemplate"
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="messagePrefForm"
	type="com.manulife.pension.ps.web.messagecenter.personalization.MCMessagePreferenceForm"
	required="true" rtexprvalue="true"%>

<%@ attribute name="section"
	type="com.manulife.pension.service.message.valueobject.MessageCenterComponent"
	required="true" rtexprvalue="true"%>

<c:set var="templates" value=""/>
<%-- <form:hidden path="expand(${section.id.value})"/> --%>
<input type="hidden" name="expand('${section.id.value}')"/>
<%

%>
<logicext:if name="messagePrefForm" property="expand(${section.id.value})" value="Y" op="equal" scope="session">
 <logicext:then>
    <c:set var="expandStyle" value="DISPLAY: block"/>
    <c:set var="expandIcon" value="/assets/unmanaged/images/minus_icon.gif"/>
 </logicext:then>
 <logicext:else>
	<c:set var="expandStyle" value="DISPLAY: none"/>
	<c:set var="expandIcon" value="/assets/unmanaged/images/plus_icon.gif"/>     
 </logicext:else>         
</logicext:if>
<c:set var="templates" value="<%= messagePrefForm.getTemplateRepository().getMessageTemplates(section)%>"/>
   <table border="0" cellpadding="1" cellspacing="0" width="710">
       <tr height="20">
          <td width="521" class="tablesubhead">
           <c:if test="${not empty templates}">
            <a href="javascript:toggleSection('messagePrefForm', 'section${section.id.value}', 'expand(${section.id.value})', 'expandIcon${section.id.value}')">
		 	   <img id="expandIcon${section.id.value}" src="<c:out value='${expandIcon}'/>" border="0"/></a> 
		   </c:if> &nbsp; 
               <b><c:out value="${section.name}"/></b> &nbsp; <span id="SectionInfo_${section.id.value}"></span> 
            </td>
            <td width="1" valign="middle" class="dataheaddivider">
              <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
            </td>
            <td width="100" valign="middle" class="tablesubhead">
              <input disabled="true" readonly="true" type="radio" name="sectionDisplay(${section.id.value})" value="Y" onclick="javascript:setTemplatesValueInSection(this.form, ${section.id.value}, 'Y')">All</input>
              <input disabled="true" readonly="true" type="radio" name="sectionDisplay(${section.id.value})" value="N" onclick="javascript:setTemplatesValueInSection(this.form, ${section.id.value}, 'N')">None</input>
            </td>
            <td width="1" valign="middle" class="dataheaddivider">
              <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
            </td>
            <td width="75" valign="middle" class="tablesubhead">&nbsp;</td>
       </tr>
   </table>
  <div id="section${section.id.value}" style="<c:out value='${expandStyle}'/>">

<c:set var="rowNumber" value="1"/>
<c:forEach var="template" items="${templates}">
<% DisplayableMessageTemplate template = (DisplayableMessageTemplate)(jspContext.getAttribute("template")); %>
	<c:set var="template" value="${template}"/>
	<%-- <jsp:useBean id="template" scope="page"
		     class="com.manulife.pension.ps.web.messagecenter.personalization.DisplayableMessageTemplate"/> --%>
    <c:set var="rowNumber" value="${rowNumber + 1}" />
	<c:choose>
		<c:when test="${rowNumber % 2 == 1}">
			<c:set var="trStyleClass" value="datacell4" />
		</c:when>
		<c:otherwise>
			<c:set var="trStyleClass" value="datacell1" />
		</c:otherwise>
	</c:choose>
	
	<table cellpadding="1" cellSpacing="0" width="710" border="0">
	      <tr class="${trStyleClass}">
	         <td width="515" class="${trStyleClass}" style="padding: 5px" valign="top">
	            <strong><c:out value="${template.shortText}" escapeXml="false"/> 
	            </strong> (<c:out value="${template.typeString}"/>)
	            <br>
	             <c:out value="${template.longText}" escapeXml="false"/>
	             
	         </td>
	         <td class=datadivider width=1>
	               <img height=1  src="/assets/unmanaged/images/s.gif" width=1>
	         </td>
	         <td class="${trStyleClass}" width="100" valign="top">
	             <c:set var="displayId" value="display(${template.id}_${section.id.value})"/>
	             <c:set var="dispOverrideAttr" value="displayOverride(${template.id}_${section.id.value})"/>
	
				 <c:set var="displayOverride" value="${dispOverrideAttr}"/>
	             <c:set var="priorityId" value="priority(${template.id}_${section.id.value})"/>
	             <c:set var="defaultPriorityIdAttr" value="defaultPriority(${template.id}_${section.id.value})"/>
	             <%-- <fmt:parseNumber var="id" type="number" value="${defaultPriorityIdAttr}" /> --%>
	             <c:set var="poAttr" value="priorityOverride(${template.id}_${section.id.value})"/>
	             
				  <c:set var="priroityOverride" value="${poAttr}"/>
	             <script type="text/javascript">
				<!--
				   addTemplateToSection(${template.id}, ${section.id.value});
				//-->
				</script>

				 <c:set var="errorClass" value="" />
				 <c:if test="<%=MCUtils.hasPreferenceDisplayIndError(request, template.getId()) %>">
				   <c:set var="errorClass" value="redText"/>
		             <script type="text/javascript">
					<!--
					   addSectionToExpand(${section.id.value});
					//-->
					</script>
				   
				 </c:if>
				 <c:set var="tempName" value="${template.id}_${section.id.value}"/>
				 <%
						String tempName=(String)jspContext.getAttribute("tempName");
						String displayValue = messagePrefForm.getDisplay(tempName);
						String priorityValue = messagePrefForm.getPriority(tempName);
						Boolean displayOverrideValue=messagePrefForm.getDisplayOverride(tempName);
						Boolean priorityOverrideValue = messagePrefForm.getPriorityOverride(tempName);
						jspContext.setAttribute("displayValue", displayValue);
						jspContext.setAttribute("priorityValue", priorityValue);
						jspContext.setAttribute("displayOverrideValue", displayOverrideValue);
						jspContext.setAttribute("priorityOverrideValue", priorityOverrideValue);
					%>

				<c:if test = "${displayValue == 'Y'}">
				 <form:radiobutton id = "displayMap[${template.id}_${section.id.value}]-0"  path="displayMap[${template.id}]" value="Y"  checked="checked"
	         	      onclick="javascript:displayChange(this,${section.id.value})" disabled="true"/>
	         	 <span class="${errorClass}">Yes</span>
	         	 </c:if>
				 <c:if test = "${displayValue != 'Y'}">
				 <form:radiobutton id = "displayMap[${template.id}_${section.id.value}]-0" path="displayMap[${template.id}]" value="Y"  
	         	      onclick="javascript:displayChange(this,${section.id.value})" disabled="true"/>
	         	 <span class="${errorClass}">Yes</span>
	         	 </c:if>
	              <br>
				  <c:if test = "${displayValue == 'N'}">
	             <form:radiobutton id = "displayMap[${template.id}_${section.id.value}]-1" path="displayMap[${template.id}]" value="N"  checked="checked" onclick="javascript:displayChange(this,${section.id.value})" disabled="true" />
	             <span class="${errorClass}">No</span>
	             </c:if>
				 	<c:if test = "${displayValue != 'N'}">
				 <form:radiobutton id = "displayMap[${template.id}_${section.id.value}]-1" path="displayMap[${template.id}]" value="N"  
	         	      onclick="javascript:displayChange(this,${section.id.value})" disabled="true"/>
	         	 <span class="${errorClass}">No</span>
	         	 </c:if>
	              <br>
	             
	         </td>
	         <td class=datadivider width=1>
	            <img height=1 src="/assets/unmanaged/images/s.gif" width=1>
	         </td>
	         <td class="${trStyleClass}" width="75" valign="top">
	             
	            <c:if test="${priorityValue=='0'}">
	          <form:radiobutton id = "priorityMap[${template.id}]-0" path="priorityMap[${template.id}]" value="0"onclick="javascript:priorityChange(this,${section.id.value})" checked="checked" />Urgent
			 </c:if>
	             
				 <c:if test="${priorityValue !='0'}">
	          <form:radiobutton id = "priorityMap[${template.id}]-0" path="priorityMap[${template.id}]" value="0" onclick="javascript:priorityChange(this,${section.id.value})"/>Urgent
	            </c:if>
				</br>
				<c:if test="${priorityValue =='5'}">
	          <form:radiobutton id = "priorityMap[${template.id}]-1" path="priorityMap[${template.id}]" value="5" onclick="javascript:priorityChange(this,${section.id.value})" checked="checked" />Normal
				</c:if>
	            
				 <c:if test="${priorityValue !='5'}">
				
	          <form:radiobutton id = "priorityMap[${template.id}]-1" path="priorityMap[${template.id}]" value="5"  onclick="javascript:priorityChange(this,${section.id.value})" />Normal
	            
				</c:if>
				</br>
				<c:if test="${defaultPriorityIdAttr =='2'}">
				<form:radiobutton id = "priorityMap[${template.id}]-1" path="priorityMap[${template.id}]" value="2"  onclick="javascript:priorityChange(this,${section.id.value})" />Important
				</c:if>
    		</td>
	        </tr>
     </table>
</c:forEach>
<script type="text/javascript">
<c:forEach var="template" items="${templates}">
	             <c:set var="priorityId" value="priority(${template.id}_${section.id.value})"/>
	             <c:set var="poAttr" value="priorityOverride(${template.id}_${section.id.value})"/>
	            
				 <c:set var="priroityOverride" value="${poAttr}"/>
priorityOverideMap["${priorityId}"] = ${priroityOverride}

</c:forEach>
</script>


   </div>
