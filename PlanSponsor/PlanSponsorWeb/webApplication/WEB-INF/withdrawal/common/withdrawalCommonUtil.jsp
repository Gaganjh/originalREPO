<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />

<script type="text/javascript">

/**
 * Determines if the request is simple or robust.
 */
function isSimpleWithdrawal() {
  var reason = document.getElementById("withdrawalReasonId").value;
  if ((reason == '${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}') 
    || (reason == '${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}') 
    || (reason == '${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}')) {
    return false;
  } else {
    return true;
  }
}
</script>