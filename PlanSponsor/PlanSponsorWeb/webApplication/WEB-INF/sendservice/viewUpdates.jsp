<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Defines page level update handlers --%>
<script type="text/javascript">

/**
 * Initialization function that is called when the page first loads.
 */
function initViewPlanData() {
  <c:choose>
    <c:when test="${not param.printFriendly}">
      collapseAllDataDivs();
      expandDataDiv('general');
    </c:when>
    <c:otherwise>
      expandAllDataDivs();
    </c:otherwise>
  </c:choose>
}

</script>