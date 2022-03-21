<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_APPROVAL_2_STEP_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="twoStepApprovalText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_APPROVAL_1_STEP_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="oneStepApprovalText"/>

<!-- Content for GIFL 1C warning messages -->
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_MESSAGE_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="withDrawalmessageText"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
    <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
<%-- GIFL 1C end --%>
		<% 
			boolean giflInd = ((Boolean)request.getSession(false).getAttribute("isParticipantGIFLEnabled")).booleanValue();
			if(giflInd)
			{
		%>
			<tr>
				<td>
					<content:getAttribute beanName="withDrawalmessageText" attribute="text"/><br/><br/>
				</td>
			</tr>
		<%
		}
		%>
		<br/>
<%-- GIFL 1C end --%>
        <tr>
          <td>
            <c:choose>
              <c:when test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.twoStepApprovalRequired}">
                <content:getAttribute beanName="twoStepApprovalText" attribute="text"/><br/>                
              </c:when>
              <c:otherwise>
                <content:getAttribute beanName="oneStepApprovalText" attribute="text"/><br/>                
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr>
          <td id="messagesSectionId">
            <ps:messages scope="session" maxHeight="100px" showWarningAsAnAlert="${(withdrawalForm.action == 'calculate') || (withdrawalForm.action == 'default')}"/>
          </td>
        </tr>
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
              <span onclick="expandAllPage2Sections();">
                <img src="/assets/unmanaged/images/plus_icon_all.gif" width="12" height="12" border="0">
              </span>
              &frasl;
              <span onclick="collapseAllPage2Sections();">
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
