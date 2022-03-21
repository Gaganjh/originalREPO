<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<c:if test="${not param.printFriendly}">
  <div class="buttonGroup">
    <div class="buttonContainerLeft">             
<input type="submit" class="button100Lg" onclick="return handleContinueEditingClicked();" name="actionLabel" value="continue editing" />



    </div>
    <div class="buttonContainerRight">             
<input type="submit" class="button100Lg" onclick="return handleAcceptClicked();" name="action" value="accept" />



    </div>
  </div>
</c:if>             
