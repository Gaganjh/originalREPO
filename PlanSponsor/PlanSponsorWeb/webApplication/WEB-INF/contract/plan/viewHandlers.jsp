<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<jsp:include flush="true" page="commonHandlers.jsp"></jsp:include>

<%-- Defines page level event handlers --%>
<script type="text/javascript">
/**
 * Function that handles click of the edit button.
 */
 function handleEditClicked() {
 
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
    
    return true;
 }
 
 function handleViewSphButtonClicked() {
	 
	 var reportURL = new URL();
		reportURL.setParameter("action", "viewSphPdf");
		window.location= "/do/contract/planData/view/?actionLabel=viewSphPdf";
		return false;
 }
 
</script>