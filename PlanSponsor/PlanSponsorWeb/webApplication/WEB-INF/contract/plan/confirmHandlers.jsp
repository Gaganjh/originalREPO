<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<jsp:include flush="true" page="commonHandlers.jsp"></jsp:include>

<%-- Defines page level event handlers --%>
<script type="text/javascript">
/**
 * Function that handles click of the accept button.
 */
 function handleAcceptClicked() {
 
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
 }
 
/**
 * Function that handles click of the continue editing button.
 */
 function handleContinueEditingClicked() {
 
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
 }
</script>