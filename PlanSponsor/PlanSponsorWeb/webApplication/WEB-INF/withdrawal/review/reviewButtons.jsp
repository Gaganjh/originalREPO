<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

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
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_REVIEW_DENY_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="denyRolloverText"/>
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
         <c:set var="cancelExitMouseOverText">
            Tip('<content:getAttribute beanName="cancelExitRolloverText" attribute="text" escapeJavaScript="true"/>')
         </c:set>
         <input type="submit" class="button110Lg"
                       value="cancel &amp; exit"
                       name="action"
                       onmouseover="${cancelExitMouseOverText}"
                       onmouseout="UnTip()"
                       onclick="return handleCancelAndExitClicked();"
                       disabled="disabled"/>
       </span>
       <span style="padding-left:5px">
         <c:set var="saveExitMouseOverText">
            Tip('<content:getAttribute beanName="saveExitRolloverText" attribute="text" escapeJavaScript="true"/>')
         </c:set>
         <input type="submit" class="button110Lg"
                       value="save &amp; exit"
                       name="action"
                       onmouseover="${saveExitMouseOverText}"
                       onmouseout="UnTip()"
                       onclick="return handleSaveAndExitClicked();"
                       disabled="disabled"/>
       </span>
       <c:if test="${withdrawalForm.withdrawalRequestUi.showDeleteButton}">
         <span style="padding-left:5px">
           <c:set var="deleteMouseOverText">
              Tip('<content:getAttribute beanName="deleteRolloverText" attribute="text" escapeJavaScript="true"/>')
           </c:set>
           <input type="submit" class="button110Lg"
                         value="delete"
                         name="action"
                         onmouseover="${deleteMouseOverText}"
                         onmouseout="UnTip()"
                         onclick="return handleDeleteClicked();"
                         disabled="disabled"/>
         </span>
       </c:if>
        <c:if test="${withdrawalForm.withdrawalRequestUi.showDenyButton}">
	       <span style="padding-left:5px">
	         <c:set var="denyMouseOverText">
	            Tip('<content:getAttribute beanName="denyRolloverText" attribute="text" escapeJavaScript="true"/>')
	         </c:set>
	         <input type="submit" class="button110Lg"
	                       value="deny"
	                       name="action"
	                       onmouseover="${denyMouseOverText}"
	                       onmouseout="UnTip()"
	                       onclick="return handleDenyClicked();"
	                       disabled="disabled"/>
	    
	         </span>
       </c:if>
     </td>
   </tr>
   <tr>
     <td align="right">
       <br/>
       <c:if test="${withdrawalForm.withdrawalRequestUi.showSendForApprovalButton}">
         <span style="padding-left:5px">
           <c:set var="sendForApprovalMouseOverText">
              Tip('<content:getAttribute beanName="sendForApprovalRolloverText" attribute="text" escapeJavaScript="true"/>')
           </c:set>
           <input type="submit" class="button110Lg"
                        value="send for approval"
                        name="action"
                        onmouseover="${sendForApprovalMouseOverText}"
                        onmouseout="UnTip()"
                        onclick="return handleSendForApproveClicked();"
                        disabled="disabled"/>
         </span>
       </c:if>
       <c:if test="${withdrawalForm.withdrawalRequestUi.showApproveButton}">
         <span style="padding-left:5px">
           <c:set var="approveMouseOverText">
              Tip('<content:getAttribute beanName="approveRolloverText" attribute="text" escapeJavaScript="true"/>')
           </c:set>
           <input type="submit" class="button110Lg"
                        value="approve"
                        name="action"
                        onmouseover="${approveMouseOverText}"
                        onmouseout="UnTip()"
                        onclick="return handleApproveClicked();"
                        disabled="disabled"/>
          </span>
        </c:if>
      </td>
    </tr>
  </table>
</div>