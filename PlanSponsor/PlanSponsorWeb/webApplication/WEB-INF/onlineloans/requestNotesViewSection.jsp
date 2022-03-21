<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants"/>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />

<content:contentBean
  contentId="${loanContentConstants.NOTE_TO_PARTICIPANT_PRINT_CONTENT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noteToparticipantPrintContentText"/>
<content:contentBean
  contentId="${loanContentConstants.VIEW_NOTES_SECTION_FOOTER}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="viewNotesSectionFooterText"/>

  <table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
    <tr>
      <td class="tableheadTD1"><b>Notes</b></td>
      <td width="132" align="right" class="tablehead">&nbsp;</td>
    </tr>
  </table>
  <table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
    <tr>

      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td><table border="0" cellpadding="0" cellspacing="0" width="498">
          
          <tr valign="top">
            <td width="3486" colspan="7" class="datacell1"><strong>Notes to participant </strong></td>
          </tr>
          <tr valign="top">
            <td colspan="7" class="datacell1">
              <c:if test="${not displayRules.printFriendly}" >
                <div style="overflow-y:auto;height:expression(this.scrollHeight>100?'100px':'auto');max-height:100px;">
              </c:if>
                <c:forEach items="${loan.previousParticipantNotes}" 
                           var="participantNote" 
                           varStatus="participantNoteStatus">
                  <c:if test="${participantNoteStatus.index gt '0'}" >
                    <br/><br/>
                  </c:if> 
                  <span class="highlightBold">
                    ${fn:trim(userNames[participantNote.createdById].firstName)}&nbsp;${fn:trim(userNames[participantNote.createdById].lastName)}
                    &nbsp;
                    <fmt:formatDate value="${participantNote.created}" type="both" pattern="${renderConstants.LONG_MDY_SLASHED}"/>
                    &nbsp;
                  </span>
                  ${participantNote.noteForDisplay}
                </c:forEach>
              <c:if test="${not displayRules.printFriendly}" >
                </div>
              </c:if>
            </td>
          </tr>

          <tr valign="top">
            <td colspan="7" class="datacell1">&nbsp;</td>
          </tr>

          <c:if test="${displayRules.displayNotesToAdministrators}" >
            <tr valign="top">
              <td colspan="7" class="datacell1"><strong>Notes to administrators </strong></td>
            </tr>
            <tr valign="top">
              <td colspan="7" class="datacell1">
                <c:if test="${not displayRules.printFriendly}" >
                  <div style="overflow-y:auto;height:expression(this.scrollHeight>100?'100px':'auto');max-height:100px;">
                </c:if>
                  <c:forEach items="${loan.previousAdministratorNotes}" 
                             var="administratorNote" 
                             varStatus="administratorNoteStatus">
                    <c:if test="${administratorNoteStatus.index gt '0'}" >
                      <br/><br/>
                    </c:if> 
                    <span class="highlightBold">
                      ${fn:trim(userNames[administratorNote.createdById].firstName)}&nbsp;${fn:trim(userNames[administratorNote.createdById].lastName)}
                      &nbsp;
                      <fmt:formatDate value="${administratorNote.created}" type="both" pattern="${renderConstants.LONG_MDY_SLASHED}"/>
                      &nbsp;
                    </span>
                    ${administratorNote.noteForDisplay}
                  </c:forEach>
                <c:if test="${not displayRules.printFriendly}" >
                  </div>
                </c:if>
              </td>
            </tr>
          </c:if>

          <c:if test="${displayRules.displayNoteToParticipantPrintContentText}" >
            <tr valign="top">
              <td colspan="7" class="datacell1">
                <content:getAttribute attribute="text" beanName="noteToparticipantPrintContentText">
                </content:getAttribute>
              </td>
            </tr>
          </c:if>

          <c:if test="${displayRules.displayViewNotesSectionFooter}" >
            <tr valign="top">
              <td colspan="7" class="datacell1">
                <content:getAttribute attribute="text" beanName="viewNotesSectionFooterText">
                </content:getAttribute>
              </td>
            </tr>
          </c:if>
          
      </table></td>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>

  </table>
