<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_NEXT_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="nextRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_DELETE_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="deleteRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_CANCEL_EXIT_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="cancelExitRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_SAVE_EXIT_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="saveExitRolloverText"/>

<div style="padding-top:10px;padding-bottom:10px;">
  <table width="500" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td align="right">
       <span style="padding-left:15px">
         <c:set var="cancelExitMouseOverText">
            Tip('<content:getAttribute beanName="cancelExitRolloverText" attribute="text" escapeJavaScript="true"/>')
         </c:set>
<input type="submit" class="button100Lg" onclick="return handleCancelAndExitClicked();" onmouseout="UnTip()" onmouseover="${cancelExitMouseOverText}" name="action" value="cancel &amp; exit" /><%--  - property="action" --%>





       </span>
       <span style="padding-left:15px">
         <c:set var="saveExitMouseOverText">
            Tip('<content:getAttribute beanName="saveExitRolloverText" attribute="text" escapeJavaScript="true"/>')
         </c:set>
<input type="submit" class="button100Lg" onclick="return handleSaveAndExitClicked();" onmouseout="UnTip()" onmouseover="${saveExitMouseOverText}" name="action" value="save &amp; exit" /><%--  - property="action" --%>





       </span>
       <c:if test="${withdrawalForm.withdrawalRequestUi.showDeleteButton}">
         <span style="padding-left:15px">
           <c:set var="deleteMouseOverText">
              Tip('<content:getAttribute beanName="deleteRolloverText" attribute="text" escapeJavaScript="true"/>')
           </c:set>
<input type="submit" class="button100Lg" onclick="return handleDeleteClicked();" onmouseout="UnTip()" onmouseover="${deleteMouseOverText}" name="action" value="delete" /><%--  - property="action" --%>





         </span>
       </c:if>
       <span style="padding-left:15px">
         <c:set var="nextMouseOverText">
            Tip('<content:getAttribute beanName="nextRolloverText" attribute="text" escapeJavaScript="true"/>')
         </c:set>
<input type="submit" class="button100Lg" onclick="return handleNextClicked();" onmouseout="UnTip()" onmouseover="${nextMouseOverText}" name="action" value="next" /><%--  - property="action" --%>





       </span>
      </td>
    </tr>
  </table>
</div>
