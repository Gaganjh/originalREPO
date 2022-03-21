<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%-- Defines page level update handlers --%>
<script type="text/javascript">

/**
 * Initialization function that is called when the page first loads.
 */
function initConfirmPlanData() {

  expandAllDataDivs();
  
  <%-- Register our dirty page query --%>
  registerTrackChangesFunction(isFormDirty);
  
  protectLinks();
}

</script>