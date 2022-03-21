<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<%-- Define static constants --%>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="withdrawalWebConstants" className="com.manulife.pension.ps.web.withdrawal.WebConstants"/>
<un:useConstants var="constants" className="com.manulife.pension.ps.web.Constants"/>
<script type="text/javascript">
var submitted = false;

function isSubmitInProgress() {
	  if (!submitted) {
	    submitted = true;
	    return false;
	  } else {
	    window.status = "Transaction already in progress.  Please wait.";
	    return true;
	  }
	}
function handleCancelClicked(){
	<%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }    
}
function handleNextClicked(){
	<%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }    
}

</script>
<%-- This jsp includes the following CMA content --%>
<content:contentBean contentId="${contentConstants.MISCELLANEOUS_BEFORE_YOU_BEGIN_DYNAMIC_TEXT}" type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="beforeYouBeginDynamic"/>
<content:contentBean contentId="${contentConstants.MISCELLANEOUS_WHEN_NOT_TO_DO_WITHDRAWAL_ROTH_TEXT}" type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="whenNotToDoWithdrawalRoth"/>
<content:contentBean contentId="${contentConstants.MISCELLANEOUS_WHEN_NOT_TO_DO_WITHDRAWAL_PBA_TEXT}" type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="whenNotToDoWithdrawalPba"/>
<content:contentBean contentId="${contentConstants.MISCELLANEOUS_BEFORE_YOU_BEGIN_PARTICIPANT_DYNAMIC_TEXT}" type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="beforeYouBeginParticipantDynamic"/>
<content:errors scope="session"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
      <td width="40" valign="top">
          <img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8" border="0"><br>
      	  <img src="/assets/unmanaged/images/s.gif" width="40" height="1">
      </td>
    <td>
      <div style="padding-top: 10px;">
        <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>
      </div>
      <div>
        <content:getAttribute beanName="layoutPageBean" attribute="body1"/>
      </div>
      <div>
        <content:getAttribute attribute="text" beanName="beforeYouBeginDynamic" />
      </div>

      <c:if test="${withdrawalBeforeProceedingForm.initiatedByParticipant}">
      <div>
      <ul>
        <content:getAttribute attribute="text" beanName="beforeYouBeginParticipantDynamic" />
      </ul>
      </div>
      </c:if>
      
      <div style="padding-top: 10px;">
        <b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b>
      </div>
      <div>
        <content:getAttribute beanName="layoutPageBean" attribute="body2"/>
		<ul>
        <c:if test="${withdrawalBeforeProceedingForm.contractInfo.contractHasRothEnabled}">
          <content:getAttribute attribute="text" beanName="whenNotToDoWithdrawalRoth" />
        </c:if>
        <c:if test="${withdrawalBeforeProceedingForm.contractInfo.contractHasPbaEnabled}">
          <content:getAttribute attribute="text" beanName="whenNotToDoWithdrawalPba" />
        </c:if>
		</ul>
      </div>
      <c:if test="${not param.printFriendly}">
        <div style="padding-top: 10px; padding-bottom: 10px;">
          <content:getAttribute beanName="layoutPageBean" attribute="footer1"/>
        </div>
        <ps:form modelAttribute ="withdrawalBeforeProceedingForm" method="POST" action="/do/withdrawal/beforeProceeding${(sessionScope[withdrawalWebConstants.WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE].isInitiate) ? 'Initiate' : 'Review'}/">
        	
        	<table width="100%"><tr><td>
<form:checkbox path="skipIndicator" value="${constants.YES}"/>
	            Don't show me this page again
            <td>
            <td align="right">
<input type="submit" class="button100Lg" name="action" value="cancel" onclick="return handleCancelClicked();" />
            <span style="padding-left: 10px;">
<input type="submit" class="button100Lg" name="action"  value="next" onclick="return handleNextClicked();" />
            </span>
            </td></tr></table>
        </ps:form>
      </c:if>
      <c:forEach items="${layoutPageBean.footnotes}" var="footnote" varStatus="footnoteStatus">
        <div>
          <content:getAttribute beanName="footnote" attribute="text"/>
        </div>
      </c:forEach>
      <c:forEach items="${layoutPageBean.disclaimer}" var="disclaimer" varStatus="disclaimerStatus">
        <div>
          <content:getAttribute beanName="disclaimer" attribute="text"/>
        </div>
      </c:forEach>
    </td>
  </tr>
</table>
