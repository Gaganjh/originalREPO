<%@ tag
  import="com.manulife.pension.ps.web.tools.CensusErrorCorrectionForm"
  import="com.manulife.pension.ps.web.tools.CensusErrorCorrectionForm.DisplayProperty"
  import="com.manulife.pension.service.employee.util.EmployeeData.Property" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ attribute name="property" required="true"%>
<%@ attribute name="maxlength" required="false"%>
<%@ attribute name="size" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="onblur" required="false"%>
<%@ attribute name="style" required="false"%>
<%@ attribute name="rowNumber" required="true" %>

<%
Property propertyObj = null;
for (Property p: Property.values()) {
	if (p.getName().equals(property)) {
	    propertyObj = p;
	    break;
	}
}

String label = propertyObj.getLabel();
%>

<c:if test="${empty onchange}">
  <c:set var="onchange" value="noOp()"/>
</c:if>

<c:if test="${empty onblur}">
  <c:set var="onblur" value="noOp()"/>
</c:if>

<c:set var="displayProperty"
  value="${errorCorrectionForm.displayProperties[property]}" />

<c:choose>
  <c:when test="${rowNumber % 2 == 1}">
    <c:set var="trStyleClass" value="datacell2"/>
  </c:when>
  <c:otherwise>
    <c:set var="trStyleClass" value="datacell1"/>
  </c:otherwise>
</c:choose>

<c:choose>
  <c:when test="${displayProperty.initialDisplay}">
    <c:set var="trId" value="initialDisplay${rowNumber}"/>
  </c:when>
  <c:otherwise>
    <c:set var="trId" value="nonInitialDisplay${rowNumber}"/>
  </c:otherwise>
</c:choose>

<c:if test="${not empty displayProperty}">
  <tr id="${trId}" class="${trStyleClass}" align="left" valign="top">
    <td width="1" height="23" class="databorder"><img
      src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td width="175" nowrap="nowrap">
    <c:if test="${displayProperty.errorField}">
      <img alt="Error" src="/assets/unmanaged/images/error.gif">
    </c:if>
    <c:if test="${displayProperty.warningField}">
      <img alt="Warning" src="/assets/unmanaged/images/warning2.gif">
    </c:if>
    <%= label %>
    </td>
    <td width="1" class="greyborder"><img
      src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
      width="1"></td>
    <td width="215"><span class="highlight"> <!-- 
    Highlight field if necessary
    --> <ps:fieldHilight name="${property}" />
    <c:choose>
      <c:when test="${property == 'ssn'}">
        <%-- SSN is not masked --%>
        <form:input  path="formValues[${property}]" title="${displayProperty.toolTip}" 
          maxlength="${maxlength}" size="${size}" disabled="${displayProperty.disabled}" />
      </c:when>
      <c:when test="${property == 'employeeNumber'}">
        <%-- SSN is not masked --%>
        <form:input  path="employeeNumber" title="${displayProperty.toolTip}" 
          maxlength="${maxlength}" size="${size}" disabled="${displayProperty.disabled}" />
      </c:when>
      <c:when
        test="${property == 'hireDate'}">
        <form:input  path="hireDate"
          styleClass="inputAmount" size="${size}"
          disabled="${displayProperty.disabled}"
          maxlength="${maxlength}"
          onchange="${onchange}" />
            &nbsp;
          <c:if test="${!displayProperty.disabled}">
          <a href="javascript:doCalendar('formValues[${property}]',0)">
          <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
          </c:if>
            (mm/dd/yyyy)
        <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
      </c:when>
      <c:when
        test="${property == 'birthDate'}">
        <form:input  path="birthDate"
          styleClass="inputAmount" size="${size}"
          disabled="${displayProperty.disabled}"
          maxlength="${maxlength}"
          onchange="${onchange}" />
            &nbsp;
          <c:if test="${!displayProperty.disabled}">
          <a href="javascript:doCalendar('formValues[${property}]',0)">
          <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
          </c:if>
            (mm/dd/yyyy)
        <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
      </c:when>
      <c:when
        test="${property == 'employmentStatusDate'}">
        <form:input  path="employmentStatusDate"
          styleClass="inputAmount" size="${size}"
          disabled="${displayProperty.disabled}"
          maxlength="${maxlength}"
          onchange="${onchange}" />
            &nbsp;
          <c:if test="${!displayProperty.disabled}">
          <a href="javascript:doCalendar('formValues[${property}]',0)">
          <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
          </c:if>
            (mm/dd/yyyy)
        <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
      </c:when>
      <c:when
        test="${property == 'eligibilityDate'}">
        <form:input  path="eligibilityDate"
          styleClass="inputAmount" size="${size}"
          disabled="${displayProperty.disabled}"
          maxlength="${maxlength}"
          onchange="${onchange}" />
            &nbsp;
          <c:if test="${!displayProperty.disabled}">
          <a href="javascript:doCalendar('formValues[${property}]',0)">
          <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
          </c:if>
            (mm/dd/yyyy)
        <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
      </c:when>
      <c:when
        test="${property == 'planYTDHoursWorkedEffectiveDate'}">
        <form:input  path="planYTDHoursWorkedEffectiveDate"
          styleClass="inputAmount" size="${size}"
          disabled="${displayProperty.disabled}"
          maxlength="${maxlength}"
          onchange="${onchange}" />
            &nbsp;
          <c:if test="${!displayProperty.disabled}">
          <a href="javascript:doCalendar('formValues[${property}]',0)">
          <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
          </c:if>
            (mm/dd/yyyy)
        <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
      </c:when>
      

      <c:when
        test="${property == 'stateCode'}">
           <form:select id="stateUSA"
                     path="stateCode"
                     disabled="${not errorCorrectionForm.countryUSA or displayProperty.disabled}"
                     style="boxbody">
         <form:options items="${errorCorrectionForm.stateList}" itemLabel="label" itemValue="value"/> 
         <%-- <form:options itemValue="lookupData(${property})" } />  <!-- please check this part alone  not tested  --> --%>
        </form:select>
    	<form:input id="stateNonUSA"
    	           path="formValues[${property}]"
    	           size="2"
    	           maxlength="2"
    	           onchange="validateState(this)"
    	           disabled="${errorCorrectionForm.countryUSA or displayProperty.disabled}" />
      </c:when>
      <c:when
        test="${property == 'zipCode'}">
           <span id="zipCodeUSA">
           <form:input id="zipCode1" path="zipCode1" size="5" 
               maxlength="5" 
               onchange="validateField(this, validateZipCode1, zipCode1FormatError)"
               disabled="${not errorCorrectionForm.countryUSA or displayProperty.disabled}"
               /> - 
           <form:input id="zipCode2" path="zipCode2" 
               size="4" maxlength="4" 
               onchange="validateField(this, validateZipCode2, zipCode2FormatError)"                   
               disabled="${not errorCorrectionForm.countryUSA or displayProperty.disabled}"/>
           </span>
           <form:input id="zipCodeNonUSA" path="formValues[zipCode]" size="9" 
               maxlength="9" 
     	       onchange="validateField(this, validateNonPrintableAscii, nonPrintableZipCode)"	                   
               disabled="${errorCorrectionForm.countryUSA or displayProperty.disabled}"/>
      </c:when>
      <c:when
        test="${property == 'countryName'}">
         <form:select path="countryName"
                     disabled="${displayProperty.disabled}"
                     onchange="showOrHideStateAndZip()"
                     style="boxbody">
                     <form:options items="${errorCorrectionForm.countryList}" itemLabel="label" itemValue="value"/> 
        </form:select>
      </c:when>
      <c:when
        test="${property == 'namePrefix'}">

        <form:select path="namePrefix"
                     disabled="${displayProperty.disabled}"
                     style="boxbody">
          <form:options items="${errorCorrectionForm.prefixList}" itemLabel="label" itemValue="value"/> 
        </form:select>
      </c:when>
      <c:when
        test="${property == 'stateOfResidence'}">

        <form:select path="stateOfResidence"
                     disabled="${displayProperty.disabled}"
                     style="boxbody">
          <form:options items="${errorCorrectionForm.stateOfResList}" itemLabel="label" itemValue="value"/> 
        </form:select>
      </c:when>
      <c:when
        test="${property == 'employmentStatus'}">

        <form:select path="employmentStatus"
                     disabled="${displayProperty.disabled}"
                     style="boxbody">
          <form:options items="${errorCorrectionForm.empStatusList}" itemLabel="label" itemValue="value"/> 
        </form:select>
      </c:when>
       <c:when
        test="${property == 'optOutIndicator'}">
        <form:select path="optOutIndicator"
                     disabled="${displayProperty.disabled}"
                     style="boxbody">
          <form:options items="${errorCorrectionForm.optOutInd}" itemLabel="label" itemValue="value"/> 
        </form:select>
      </c:when>
      
      <c:when
        test="${property == 'stateCode' ||
                      property == 'previousYearsOfServiceEffectiveDate'}">
      <!--  <html:select property="formValue(${property})"
                     disabled="${displayProperty.disabled}"
                     style="boxbody">
          <html:optionsCollection property="lookupData(${property})" } />
        </html:select>-->
        <form:select path="formValues['${property}']"
                     disabled="${displayProperty.disabled}"
                     style="boxbody">
          <form:options itemValue="lookupData(${property})" } /> <!-- please check this part alone  not tested  -->
        </form:select>
      </c:when>
      <c:when test="${property == 'eligibilityIndicator'}">
      <label>
<form:radiobutton disabled="${displayProperty.disabled}" path="eligibilityIndicator" value="Y"/>
					Yes
				</label>
				 <label>
<form:radiobutton disabled="${displayProperty.disabled}" path="eligibilityIndicator" value="N"/>
					No
				</label>

      </c:when>
      <c:when
        test="${property == 'annualBaseSalary'}">
          $ <c:choose>
	          <c:when test="${empty errorCorrectionForm.formValues[property] or
	                          not errorCorrectionForm.maskSensitiveInformation}">
	          <form:input  path="annualBaseSalary" size="${size}"
	            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
	          </c:when>
	          <c:otherwise>
		          <form:input  path="annualBaseSalary"
		            size="${size}"
		            value="******"
		            disabled="${displayProperty.disabled}"
		            onchange="${onchange}"
		            onblur="${onblur}"
		            maxlength="${maxlength}"
		            style="${style}"/>
	          </c:otherwise>
          </c:choose>
      </c:when>
       <c:when
        test="${property == 'planYTDCompensation'}">
          $ <c:choose>
	          <c:when test="${empty errorCorrectionForm.formValues[property] or
	                          not errorCorrectionForm.maskSensitiveInformation}">
	          <form:input  path="planYTDCompensation" size="${size}"
	            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
	          </c:when>
	          <c:otherwise>
		          <form:input  path="planYTDCompensation"
		            size="${size}"
		            value="******"
		            disabled="${displayProperty.disabled}"
		            onchange="${onchange}"
		            onblur="${onblur}"
		            maxlength="${maxlength}"
		            style="${style}"/>
	          </c:otherwise>
          </c:choose>
      </c:when>
      <c:when
        test="${property == 'beforeTaxDeferralPercentage'}">
          <form:input  path="beforeTaxDeferralPercentage" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/> %
      </c:when>
      <c:when
        test="${property == 'designatedRothDeferralPct'}">
          <form:input  path="designatedRothDeferralPct" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/> %
      </c:when>
      <c:when
        test="${property == 'designatedRothDeferralAmt' }">
          $ <form:input  path="designatedRothDeferralAmt" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
      </c:when>
      <c:when
        test="${property == 'beforeTaxFlatDollarDeferral'}">
          $ <form:input  path="beforeTaxFlatDollarDeferral" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
      </c:when>
      <c:when
        test="${property == 'planYTDHoursWorked'}">
          <form:input  path="planYTDHoursWorked" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
       </c:when>
       <c:when
        test="${property == 'firstName'}">
          <form:input  path="firstName" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
       </c:when>
       <c:when
        test="${property == 'lastName'}">
          <form:input  path="lastName" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
       </c:when>
       <c:when
        test="${property == 'middleInit'}">
          <form:input  path="middleInit" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
       </c:when>
       <c:when
        test="${property == 'addressLine1'}">
          <form:input  path="addressLine1" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
       </c:when>
       <c:when
        test="${property == 'addressLine2'}">
          <form:input  path="addressLine2" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
       </c:when>
       <c:when
        test="${property == 'cityName'}">
          <form:input  path="cityName" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
       </c:when>
       <c:when
        test="${property == 'employerProvidedEmail'}">
          <form:input  path="employerProvidedEmail" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
       </c:when>
       <c:when
        test="${property == 'division'}">
          <form:input  path="division" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
       </c:when>
      <c:otherwise>
      TEST::${property}
        <c:if test="${empty style}">
          <form:input  path="formValues[${property}]" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" />
        </c:if>
        <c:if test="${not empty style}">
          <form:input  path="formValues[${property}]" size="${size}"
            disabled="${displayProperty.disabled}" onchange="${onchange}" onblur="${onblur}" maxlength="${maxlength}" style="${style}"/>
        </c:if>
      </c:otherwise>
    </c:choose>
    <!-- 
      Track changes
    -->
    <c:choose>
      <c:when test="${property == 'ssn'}">
        <ps:trackChanges name="errorCorrectionForm"  escape="true" property="formValues(${property})" />
      </c:when>
      <c:when test="${property == 'zipCode'}">
        <ps:trackChanges name="errorCorrectionForm" escape="true" property="formValues(${property})" />
        <ps:trackChanges name="errorCorrectionForm" escape="true" property="formValues(${property}1)" />
        <ps:trackChanges name="errorCorrectionForm" escape="true" property="formValues(${property}2)" />
      </c:when>
      <c:when
        test="${property == 'annualBaseSalary' ||
                property == 'planYTDCompensation'}">
          <c:choose>
	          <c:when test="${empty errorCorrectionForm.formValues[property] or
	                          not errorCorrectionForm.maskSensitiveInformation}">
  	            <ps:trackChanges name="errorCorrectionForm" escape="true" property="formValue(${property})" />
	          </c:when>
	          <c:otherwise>
  	            <ps:trackChanges name="errorCorrectionForm" escape="true" property="formValue(${property})" value="******"/>
	          </c:otherwise>
          </c:choose>
      </c:when>
      <c:otherwise>
        <ps:trackChanges name="errorCorrectionForm" escape="true" property="formValue(${property})" />
      </c:otherwise>
    </c:choose>
    </span>
    </td>
    <td width="1" class="greyborder"><img
      src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
      width="1"></td>
    <td width="215">
      <c:choose>
        <c:when test="${property == 'ssn'}">
          <render:ssn property="displayProperty.onlineValue" defaultValue=""/>
        </c:when>
      <c:when
        test="${property == 'annualBaseSalary' ||
                property == 'planYTDCompensation'}">
          <c:choose>
	          <c:when test="${empty displayProperty.onlineValue or 
	                          not errorCorrectionForm.maskSensitiveInformation}">
              ${displayProperty.onlineValue}
	          </c:when>
	          <c:otherwise>
	          ******
	          </c:otherwise>
          </c:choose>
      </c:when>
        <c:otherwise>
          ${displayProperty.onlineValue }
        </c:otherwise>
      </c:choose>
    </td>
    <td width="1" valign="top" class="databorder"><img
      src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
      width="1"></td>
  </tr>
</c:if>
