<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_BACK_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="backRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_CANCEL_EXIT_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="cancelExitRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_SAVE_EXIT_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="saveExitRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_DELETE_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="deleteRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_SEND_FOR_REVIEW_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="sendForReviewRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_SEND_FOR_APPROVAL_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="sendForApprovalRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_APPROVE_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="approveRolloverText"/>

<div style="padding-top:10px;padding-bottom:10px;">
 <table width="500" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td align="right">
       <span style="padding-left:5px">
         <c:set var="backMouseOverText">
            Tip('<content:getAttribute beanName="backRolloverText" attribute="text" escapeJavaScript="true"/>')
         </c:set>
<input type="submit" id="step2BackButton" class="button110Lg" onclick="return handleBackClicked();" onmouseout="UnTip()" onmouseover="${backMouseOverText}" name="action" value="back" /><%--  - property="action" --%>





       </span>
       <span style="padding-left:5px">
         <c:set var="cancelExitMouseOverText">
            Tip('<content:getAttribute beanName="cancelExitRolloverText" attribute="text" escapeJavaScript="true"/>')
         </c:set>
<input type="submit" id="step2CancelButton" class="button110Lg" onclick="return handleCancelAndExitClicked();" onmouseout="UnTip()" onmouseover="${cancelExitMouseOverText}" name="action" value="cancel &amp; exit" /><%--  - property="action" --%>





       </span>
       <span style="padding-left:5px">
         <c:set var="saveExitMouseOverText">
            Tip('<content:getAttribute beanName="saveExitRolloverText" attribute="text" escapeJavaScript="true"/>')
         </c:set>
<input type="submit" id="step2SaveButton" class="button110Lg" onclick="return handleSaveAndExitClicked();" onmouseout="UnTip()" onmouseover="${saveExitMouseOverText}" name="action" value="save &amp; exit" /><%--  - property="" --%>





       </span>
       <c:if test="${withdrawalForm.withdrawalRequestUi.showDeleteButton}">
         <span style="padding-left:5px">
           <c:set var="deleteMouseOverText">
              Tip('<content:getAttribute beanName="deleteRolloverText" attribute="text" escapeJavaScript="true"/>')
           </c:set>
<input type="submit" id="step2DeleteButton" class="button110Lg" onclick="return handleDeleteClicked();" onmouseout="UnTip()" onmouseover="${deleteMouseOverText}" name="action" value="delete" /><%--  - property="action" --%>





         </span>
       </c:if>
     </td>
   </tr>
   <tr>
     <td align="right">
       <br/>
       <c:if test="${withdrawalForm.withdrawalRequestUi.showSendForReviewButton}">
         <span style="padding-left:5px">
           <c:set var="sendForReviewMouseOverText">
              Tip('<content:getAttribute beanName="sendForReviewRolloverText" attribute="text" escapeJavaScript="true"/>')
           </c:set>
<input type="submit" id="step2SendForReviewButton" class="button110Lg" onclick="return handleSendForReviewClicked();" onmouseout="UnTip()" onmouseover="${sendForReviewMouseOverText}" name="action" value="send for review" /><%--  - property="" --%>





         </span>
       </c:if>
       <c:if test="${withdrawalForm.withdrawalRequestUi.showSendForApprovalButton}">
         <span style="padding-left:5px">
           <c:set var="sendForApprovalMouseOverText">
              Tip('<content:getAttribute beanName="sendForApprovalRolloverText" attribute="text" escapeJavaScript="true"/>')
           </c:set>
<input type="submit" id="step2SendForApprovalButton" class="button110Lg" onclick="return handleSendForApproveClicked();" onmouseout="UnTip()" onmouseover="${sendForApprovalMouseOverText}" name="action" value="send for approval" /><%--  - property="" --%>





         </span>
       </c:if>
       <c:if test="${withdrawalForm.withdrawalRequestUi.showApproveButton}">
         <span style="padding-left:5px">
           <c:set var="approveMouseOverText">
              Tip('<content:getAttribute beanName="approveRolloverText" attribute="text" escapeJavaScript="true"/>')
           </c:set>
<input type="submit" id="step2ApproveButton" class="button110Lg" onclick="return handleApproveClicked();" onmouseout="UnTip()" onmouseover="${approveMouseOverText}" name="action" value="approve" /><%--  - property="action" --%>





          </span>
        </c:if>
      </td>
    </tr>
  </table>
</div>
