<%@ page
  import="com.manulife.pension.ps.web.tools.CensusErrorCorrectionForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un"
  uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>

<un:useConstants var="content"
  className="com.manulife.pension.ps.web.content.ContentConstants" />

<%@ taglib prefix="uploadCensus"
  tagdir="/WEB-INF/tags/tools/uploadCensus"%>


<script type="text/javascript">
<!--
var birthDateFormat = "Birth date is in wrong format";
var birthDateInvalid = "Birth date must be at least 15 year ago";
var collapsed = false;
var browserDisplayValue = undefined; // Mozilla is "table-row". IE is "block"
  
function noOp() {
// do nothing.
}

function doCalendar(fieldName) {

  // Retrieve the field
  var field = document.getElementById(fieldName);

  // Pre-Validate date and blank if not valid
  if (!validateDate(field.value)) {
  	field.value = "";
  }

  // Popup calendar
  cal = new calendar(document.forms["errorCorrectionForm"].elements[fieldName]);
  cal.year_scoll = true;
  cal.time_comp = false;
  cal.popup();
}


function initialBrowserDisplayValue() {
  var index = 1;
  var trObj = document.getElementById("initialBrowserDisplayRow");
  var displayStyle = getStyle(trObj.id, 'display');
  if (displayStyle != "none") {
    browserDisplayValue = displayStyle;
  }
  if (browserDisplayValue == undefined) {
    // if all failed, we fall back to IE.
    browserDisplayValue = "block";
  }
}


function getTrObject(index) {
  var trId = "initialDisplay" + index;
  var trObj = document.getElementById(trId);
  if (trObj == undefined) {
    trId = "nonInitialDisplay" + index;
    trObj = document.getElementById(trId);
  }
  return trObj;
}

function showOrHideStateAndZip() {
  var countryDropDown = document.getElementsByName("countryName")[0];
  var stateUsa = document.getElementById("stateUSA");
  var stateNonUsa = document.getElementById("stateNonUSA");
  var zipCodeUsaSpan = document.getElementById("zipCodeUSA");
  var zipCodeNonUsa = document.getElementById("zipCodeNonUSA");
  var zipCodeUsa1 = document.getElementById("zipCode1");
  var zipCodeUsa2 = document.getElementById("zipCode2");
  if (countryDropDown.style.display != "none") {
  	if (countryDropDown.value == 'USA') {
  	  /*
  	   * State code
  	   */
  	  stateUsa.style.display = browserDisplayValue;
  	  stateNonUsa.style.display = "none";
  	  stateUsa.disabled = countryDropDown.disabled;
  	  stateNonUsa.disabled = true;
  	  /*
  	   * Zip code
  	   */
  	  zipCodeUsaSpan.style.display = browserDisplayValue;
  	  zipCodeNonUsa.style.display = "none";
      zipCodeUsa1.disabled = countryDropDown.disabled;
      zipCodeUsa2.disabled = countryDropDown.disabled; 	  
  	  zipCodeNonUsa.disabled = true;
  	} else {
  	  /*
  	   * State code
  	   */
  	  stateUsa.style.display = "none";
  	  stateNonUsa.style.display = browserDisplayValue;
  	  stateUsa.disabled = true
  	  stateNonUsa.disabled = countryDropDown.disabled;
  	  /*
  	   * Zip code
  	   */
  	  zipCodeUsaSpan.style.display = "none";
  	  zipCodeNonUsa.style.display = browserDisplayValue;
      zipCodeUsa1.disabled = true;
      zipCodeUsa2.disabled = true;
  	  zipCodeNonUsa.disabled = countryDropDown.disabled;
  	}
  }
}

function expandCollapseAll() {

  var link = document.getElementById("expandCollapseLink");
  
  if (collapsed) {
    // we need to expand here.
    var index = 1;
    do {
      var trObj = getTrObject(index);
      if (trObj == undefined) {
        break;
      }
      trObj.style.display = browserDisplayValue;
      if (index % 2 == 1) {
        trObj.style.background = "#E9E2C3";
      } else {
        trObj.style.background = "#FFFFFF";
      }
      index++;
    } while (1);
    collapsed = false;
    link.innerHTML = "View errors only";
  } else {
    // collapse here
    var index = 1;
    var colorIndex = 1;
    do {
      var trObj = getTrObject(index);
      if (trObj == undefined) {
        break;
      }
      if (trObj.id.indexOf("nonInitialDisplay") != -1) {
        trObj.style.display = "none";
      } else {
        if (colorIndex % 2 == 1) {
          trObj.style.background = "#E9E2C3";
        } else {
          trObj.style.background = "#FFFFFF";
        }
        colorIndex++;
      }    
      index++;
    } while (1);
    collapsed = true;
    link.innerHTML = "View all values";
  }
  showOrHideStateAndZip();
}

//-->
</script>

<table border="0" cellpadding="0" cellspacing="0" width="605">
  <tbody>

    <tr id="initialBrowserDisplayRow">
      <td colspan="7" align="right"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        align="middle" height="10" width="1"> <a id="expandCollapseLink" href="#"
        onclick="javascript:expandCollapseAll();return false;">View
      all values</a></td>
    </tr>

    <tr>
      <td width="1"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        height="1" width="1"></td>
      <td width="225"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        height="1" width="225"></td>
      <td width="1"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        height="1" width="1"></td>
      <td width="175"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        height="1" width="175"></td>
      <td width="1"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        height="1" width="1"></td>
      <td width="215"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        height="1" width="175"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        height="1" width="1"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        height="1" width="175"></td>
      <td width="1"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        height="1" width="1"></td>
    </tr>

    <tr class="tablehead">
      <td colspan="6" class="tableheadTD1"><b>Summary of updates for&nbsp;
      <span class="tableheadTD"><strong>${errorCorrectionForm.displayName}</strong></span></b></td>
      </td>
      <td width="1" class="databorder"><img
        src="/assets/unmanaged/images/spacer.gif" border="0"
        height="1" width="1"></td>
    </tr>

      <tr class="tablesubhead">
        <td width="1" height="38" valign="top"
          class="databorder"><img
          src="/assets/unmanaged/images/spacer.gif" border="0"
          height="1" width="1"></td>
        <td width="175" valign="bottom" nowrap="nowrap"><b>Information in error</b></td>
        <td width="1" valign="bottom" class="dataheaddivider"><img
          src="/assets/unmanaged/images/spacer.gif" border="0"
          height="1" width="1"></td>
        <td width="215" valign="bottom"><img
          src="/assets/unmanaged/images/spacer.gif" border="0"
          height="1" width="1"><B>Census submission</B></td>
        <td width="1" valign="bottom" class="dataheaddivider"><img
          src="/assets/unmanaged/images/spacer.gif" border="0"
          height="1" width="1"></td>
        <td width="215" valign="bottom"><img
          src="/assets/unmanaged/images/spacer.gif" border="0"
          height="1" width="1"><B>John Hancock records</B></td>
        <td width="1" valign="top" class="databorder"><img
          src="/assets/unmanaged/images/spacer.gif" border="0"
          height="1" width="1"></td>
      </tr>

      <c:set var="rowNumber" value="1" />

      <%--
      CEC.37.2.3 and CEC.37.2.4
      --%>
      <c:choose>
        <c:when test="${USER_KEY.currentContract.participantSortOptionCode eq 'EE'}">
          <uploadCensus:displayProperty property="employeeNumber"
            maxlength="9" size="9" 
            onchange="validateEmployeeId(this)"
            rowNumber="${rowNumber}"/>

          <c:set var="rowNumber" value="${rowNumber + 1}" />
          <uploadCensus:displayProperty property="ssn"  maxlength="9" rowNumber="${rowNumber}" />
        </c:when>
        <c:otherwise>
          <uploadCensus:displayProperty property="ssn"
            maxlength="9" rowNumber="${rowNumber}" />

          <c:set var="rowNumber" value="${rowNumber + 1}" />
          <uploadCensus:displayProperty property="employeeNumber"
            maxlength="9" size="9"
            onchange="validateEmployeeId(this)"
            rowNumber="${rowNumber}" />
        </c:otherwise>
      </c:choose>
      
      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="firstName"
        maxlength="18" size="18"
        rowNumber="${rowNumber}"
        onchange="validateFirstName(this)"/>

      <c:set var="rowNumber" value="${rowNumber + 1}" />
       <uploadCensus:displayProperty property="lastName"
        maxlength="20" size="20"
        rowNumber="${rowNumber}"
        onchange="validateLastName(this)"/> 

      <c:set var="rowNumber" value="${rowNumber + 1}" />
       <uploadCensus:displayProperty property="middleInit"
        maxlength="1" size="1"
        rowNumber="${rowNumber}"
        onchange="validateMiddleInit(this)"/> 

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="namePrefix"
        maxlength="4" size="4" rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="addressLine1"
        rowNumber="${rowNumber}" 
        onchange="validateAddressLine1(this)" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="addressLine2"
        maxlength="30"
        rowNumber="${rowNumber}" 
        onchange="validateAddressLine2(this)" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="cityName"
        maxlength="25" rowNumber="${rowNumber}"
        onchange="validateCity(this)" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="stateCode"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="zipCode"
        maxlength="9"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="countryName"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="stateOfResidence"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="employerProvidedEmail" maxlength="99"
        onchange="validateEmailAddress(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="division"
        maxlength="25"
        rowNumber="${rowNumber}"
        onchange="validateDivision(this)"/>

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="birthDate"
        size="10" maxlength="10"
        onchange="validateBirthDate(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="hireDate"
        size="10" maxlength="10"
        onchange="validateHireDate(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="employmentStatus"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="employmentStatusDate"
        size="10"
        maxlength="10"
        onchange="validateEmploymentStatusEffDate(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="eligibilityIndicator"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="eligibilityDate"
        size="10" maxlength="10"
        onchange="validateEligibilityDate(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty property="optOutIndicator"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="planYTDHoursWorked"
        size="14" maxlength="14"
        style="{text-align: right}"
        onchange="validateYTDHoursWorked(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="planYTDCompensation"
        size="15" maxlength="15"
        style="{text-align: right}"
        onchange="validateYTDCompensation(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="planYTDHoursWorkedEffectiveDate"
        size="10" maxlength="10"
        onchange="validateYTDCompensationDate(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="annualBaseSalary"
        size="15" maxlength="15"
        style="{text-align: right}"
        onchange="validateAnnualBaseSalary(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="beforeTaxDeferralPercentage"
        size="7" maxlength="7"
        style="{text-align: right}"
		onchange="validateBeforeTaxDefPer(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="designatedRothDeferralPct"
        size="7" maxlength="7"
        style="{text-align: right}"
        onchange="validateDesignatedRothDefPer(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="beforeTaxFlatDollarDeferral"
        style="{text-align: right}"
        size="11" maxlength="11"
        onchange="validateBeforeTaxDefDollar(this)"
        rowNumber="${rowNumber}" />

      <c:set var="rowNumber" value="${rowNumber + 1}" />
      <uploadCensus:displayProperty
        property="designatedRothDeferralAmt"
        style="{text-align: right}"
        size="11" maxlength="11"
        onchange="validateDesignatedRothDefAmt(this)"
        rowNumber="${rowNumber}" />

      <tr>
        <td width="1" height="1" class="databorder"><img
          src="/assets/unmanaged/images/s.gif" height="1"
          width="1"></td>

        <td height="1" colspan="6" class="databorder"><img
          src="/assets/unmanaged/images/spacer.gif" border="0"
          height="1" width="1"></td>
      </tr>

  </tbody>
</table>

<script type="text/javascript">
<!--
initialBrowserDisplayValue();
expandCollapseAll();
// -->
</script>
