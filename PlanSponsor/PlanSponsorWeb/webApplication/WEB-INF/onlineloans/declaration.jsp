<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />

<content:contentBean
  contentId="${loanContentConstants.TRUTH_IN_LENDING_NOTICE_DISCLAIMER}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="truthInLendingNoticeDisclaimerText"/>
<content:contentBean
  contentId="${loanContentConstants.PROMISSORY_NOTE_DISCLAIMER}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="promissoryNoteDisclaimerText"/>
   <content:errors scope="request"/>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <td width="5%" valign="top"></td>
    <td width="90%" valign="top">

      <c:if test="${showIntro1Disclaimer}">
        <c:if test="${managedContentRequested == 'truthInLendingNotice'}">
          <content:getAttribute attribute="text" beanName="truthInLendingNoticeDisclaimerText">
          </content:getAttribute>
        </c:if>  
        <c:if test="${managedContentRequested == 'promissoryNote'}">
          <content:getAttribute attribute="text" beanName="promissoryNoteDisclaimerText">
          </content:getAttribute>
        </c:if>
      </c:if>  

      <br><br>
      ${managedContentHtml}
      <br><br>

      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr><td>&nbsp</td></tr>

        <content:contentBean
          contentId="${contentConstants.GLOBAL_DISCLOSURE}"
          type="${contentConstants.TYPE_MISCELLANEOUS}"
          id="globalDisclosure"/>
        <tr>
          <td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
        </tr>
      </table>

    </td>
    <td width="5%" valign="top"></td>

  </table>
