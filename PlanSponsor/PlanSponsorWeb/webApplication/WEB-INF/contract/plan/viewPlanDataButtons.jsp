<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:if test="${not param.printFriendly}">
  <div class="buttonGroup">
    <div class="buttonContainerRight">
      <c:if test="${planDataForm.userCanEdit}">             
<input type="submit" class="button100Lg" onclick="return handleEditClicked();" name="action" value="edit" />



      </c:if>
    </div>
  </div>
</c:if>
