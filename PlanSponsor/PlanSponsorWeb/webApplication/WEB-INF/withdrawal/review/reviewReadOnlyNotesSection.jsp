<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_NOTES_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="notesSectionTitle"/>

<div style="padding-top:10px;padding-bottom:10px;">
  <table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
	  <tr>
      <td class="tableheadTD1">
      	<div style="padding-top:5px;padding-bottom:5px">
      		<b><content:getAttribute beanName="notesSectionTitle" attribute="text"/></b>
      	</div>
      </td>
  	</tr>
  </table>
  <table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
    <tr>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td>
        <table border="0" cellpadding="0" cellspacing="0" width="498">
        <c:if test = "${!withdrawalForm.withdrawalRequestUi.isParticipantInitiated}">
          <tr class="datacell1" valign="top">
            <td style="padding-left:4px;vertical-align:top;">
              <strong>To participant</strong>
            </td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="indentedValueColumn" valign="top">
              <div style="overflow-y:auto;height:expression(this.scrollHeight>100?'100px':'auto');max-height:100px;">
                <c:forEach items="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.readOnlyAdminToParticipantNotes}" 
                           var="participantNote" 
                           varStatus="participantNoteStatus">
                  <span class="highlightBold">
                    ${fn:trim(withdrawalForm.userNames[participantNote.createdById].firstName)}&nbsp;${fn:trim(withdrawalForm.userNames[participantNote.createdById].lastName)}
                    &nbsp;
                    <fmt:formatDate value="${participantNote.created}" type="both" pattern="${timeStampPattern}"/>
                    &nbsp;
                  </span>                           
                  ${participantNote.noteForDisplay}<br/><br/>
                </c:forEach>
              </div>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          </tr>
        </c:if>
          <tr class="datacell1">
            <td style="padding-left:4px;vertical-align:top;">
              <strong>For administrators only</strong>
            </td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="indentedValueColumn">
              <div style="overflow-y:auto;height:expression(this.scrollHeight>100?'100px':'auto');max-height:100px;">
                <c:forEach items="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.readOnlyAdminToAdminNotes}" 
                           var="adminNote" 
                           varStatus="adminNoteStatus">
                  <span class="highlightBold">
                    ${fn:trim(withdrawalForm.userNames[adminNote.createdById].firstName)}&nbsp;${fn:trim(withdrawalForm.userNames[adminNote.createdById].lastName)}
                    &nbsp;
                    <fmt:formatDate value="${adminNote.created}" type="both" pattern="${timeStampPattern}"/>
                    &nbsp;
                  </span>
                  ${adminNote.noteForDisplay}<br/><br/>
                </c:forEach>
              </div>
            </td>
  	      </tr>
    	  </table>
      </td>
      <td width="2" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
          <tr>
            <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
            <td rowspan="2" width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" height="5" width="5"></td>
          </tr>
          <tr>
            <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</div>
