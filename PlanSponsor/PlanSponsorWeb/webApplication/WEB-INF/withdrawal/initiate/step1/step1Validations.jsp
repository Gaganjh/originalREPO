<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />

<script type="text/javascript">
<%-- Participant Information Section --%>
  /**
   * Validates the birth date.
   */
  function validateBirthDate() {

    <%-- Validate the birth date --%>
    var field = document.getElementById("birthDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

<%-- Basic Information Section --%>
  /**
   * Validates the hardship explanation.
   */
  function validateHardshipExplanation() {

    <%-- Validate the max length on the hardship explanation --%>
    if (!validateMaxLength(document.getElementById("hardshipExplanationId"),
        "Hardship explanation",
        ${requestConstants.MAXIMUM_REASON_EXPLANATION_LENGTH})) {
      return false;
    }

    if (!validateAllowedCharacters(document.getElementById("hardshipExplanationId"), 
       webMultiLineAllowedCharacterRegEx, 
       "Hardship Explanation")) { 
        
     return false; 
    } 

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the termination date.
   */
  function validateTerminationDate() {

    <%-- Validate the termination date --%>
    var field = document.getElementById("terminationDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the retirement date.
   */
  function validateRetirementDate() {

    <%-- Validate the retirement date --%>
    var field = document.getElementById("retirementDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the disability date.
   */
  function validateDisabilityDate() {

    <%-- Validate the retirement date --%>
    var field = document.getElementById("disabilityDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the final contribution date.
   */
  function validateFinalContributionDate() {

    <%-- Validate the final contribution date --%>
    var field = document.getElementById("finalContributionDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  <%-- Global Section --%>
  /**
   * Validates all the fields on the Withdrawal Step 1 Form in order to prevent submission errors.
   */
  function validateFormFields() {
  
    <%-- Validate Participant Information Section --%>
    if (!validateBirthDate()) {
      return false;
    }
    if (!validateTerminationDate()) {
      return false;
    }
    if (!validateRetirementDate()) {
      return false;
    }
    if (!validateDisabilityDate()) {
      return false;
    }
    if (!validateFinalContributionDate()) {
      return false;
    }

    <%-- Form is valid --%>
    return true;
  }
</script>