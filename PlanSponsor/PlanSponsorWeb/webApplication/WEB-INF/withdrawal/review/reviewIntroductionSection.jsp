<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_APPROVAL_1_STEP_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="oneStepApprovalText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_APPROVAL_2_STEP_PENDING_REVIEW_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="twoStepApprovalPendingReviewText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_APPROVAL_2_STEP_PENDING_APPROVAL_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="twoStepApprovalPendingApprovalText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_ACTIVITY_HISTORY_LINK_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="activityHistoryText"/>
<script type="text/javascript">
function showActivityHistory() {
	var popupURL = new URL("/do/withdrawal/activityHistory/");
	popupURL.setParameter("submissionId", "<c:out value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.submissionId}"/>");
	popupURL.setParameter("printFriendly", "true");
	window.open(popupURL.encodeURL(),"","width=700,height=480,resizable,toolbar=no,scrollbars,menubar=no");
}  
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
  	<td width="10"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
    <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
      	<c:choose>
      	 	<c:when test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.twoStepApprovalRequired}">
            <tr>
              <td>
                <c:choose>
                  <c:when test="${isPendingApproval}">
                    <content:getAttribute beanName="twoStepApprovalPendingApprovalText" attribute="text"/><br/>                
                  </c:when>
                  <c:otherwise>
                    <content:getAttribute beanName="twoStepApprovalPendingReviewText" attribute="text"/><br/>                
                  </c:otherwise>
                </c:choose>
      				</td>
            </tr>
          </c:when>
          <c:otherwise>
            <tr>
              <td>	
      					<content:getAttribute beanName="oneStepApprovalText" attribute="text"/><br/>                
      				</td>
            </tr>
          </c:otherwise>
        </c:choose>
        <tr>
          <td id="messagesSectionId">
            <ps:messages scope="session" maxHeight="100px" showWarningAsAnAlert="${withdrawalForm.action == 'calculate'}"/>
          </td>
        </tr>
        <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.activityHistoryEnabled}">
          <tr>
            <td>
              <div style="padding-top:10px;">
              	<a href="javascript:showActivityHistory()"><content:getAttribute beanName="activityHistoryText" attribute="text"/></a>
              </div>
            </td>
          </tr>
        </c:if>
        <tr>
          <td>
            <div style="padding-top:10px;">
              <a href="javascript:handleShowSuppressFloatingSummaryClicked();">Open / close request summary</a>
            </div>
          </td>
        </tr>
        <tr>
          <td>
      			<div style="padding-top:10px;">
        			<span onclick="expandAllSections();">
      						<img src="/assets/unmanaged/images/plus_icon_all.gif" width="12" height="12" border="0">
        			</span>
        			&frasl;
        			<span onclick="collapseAllSections();">
          			<img src="/assets/unmanaged/images/minus_icon_all.gif" width="12" height="12" border="0">
        			</span>
        			&nbsp;
        			all sections
      			</div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
