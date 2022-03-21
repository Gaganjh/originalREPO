<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />

<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_EXIT_EDIT_MODE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextExitEditMode"/>
<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_EXIT_VIEW_MODE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextExitViewMode"/>
<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_DELETE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextDelete"/>
<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_SAVE_AND_EXIT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextSaveAndExit"/>
<content:contentBean
  contentId="${loanContentConstants.LINK_HELP_TEXT_LOAN_PACKAGE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="linkHelpTextLoanPackage"/>
<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_LOAN_PACKAGE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextLoanPackage"/>
<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_PRINT_LOAN_REVIEW}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextPrintLoanReview"/>
<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_DENY}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextDeny"/>
<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_SEND_FOR_REVIEW}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextSendForReview"/>
<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_SEND_FOR_ACCEPTANCE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextSendForAcceptance"/>
<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_SEND_FOR_APPROVAL}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextSendForApproval"/>
<content:contentBean
  contentId="${loanContentConstants.BUTTON_HELP_TEXT_APPROVE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonHelpTextApprove"/>
<content:contentBean
  contentId="${displayRules.buttonsFooterCmaKey}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="buttonFooterText"/>
<content:contentBean
  contentId="${loanContentConstants.LOAN_PACKAGE_NO_LONGER_VALID_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="loanPackageNoLongerValidText"/>

<script language="JavaScript1.2" type="text/javascript">

function submitAgreement() {
    var form = document.getElementsByName("loanForm")[0];
    form["action"].value = "agree";
    form["ignoreWarning"].value = "true";
    submitWithoutConfirmation();
    form.submit();
}

function handleLoanPackageButtonClicked() {
  <%-- Check if loan package is still valid for the request --%>
  var isOkToShowLoanPackage = ${displayRules.loanPackageStillValidForRequest};  
  if (isOkToShowLoanPackage) {
    var url = document.location.href + "&actionLabel=showLoanPackagePdf&ext=.pdf";
    openLoanDocumentWindow(url);
  } else {
    alert('<content:getAttribute beanName="loanPackageNoLongerValidText" attribute="text" escapeJavaScript="true"/>');
  }
  return false;
}

function handlePrintLoanDocButtonClicked() {
	<%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
   	var form = document.getElementsByName("loanForm")[0];
    document.form.actionLabel.value = 'printLoanDocumentsReview';
    form.submit();
    return true;
}

function setupButtonTooltip(id, context, text) {
  if (text.trim() != '') {  
    new YAHOO.widget.Tooltip(id, {  
      context:context,  
      text:text,
      showDelay:500, autodismissdelay:999999} );
  }
}

YAHOO.util.Event.onDOMReady(function () { 

  setupButtonTooltip("tooltip_exitEditModeButton", "exitEditModeButton",
                     '<content:getAttribute beanName="buttonHelpTextExitEditMode" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_exitViewModeButton", "exitViewModeButton",
                     '<content:getAttribute beanName="buttonHelpTextExitViewMode" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_deleteButton", "deleteButton",
                     '<content:getAttribute beanName="buttonHelpTextDelete" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_saveAndExitButton", "saveAndExitButton",
                     '<content:getAttribute beanName="buttonHelpTextSaveAndExit" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_loanPackageLink", "loanPackageLink",
                     '<content:getAttribute beanName="linkHelpTextLoanPackage" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_loanPackageButton", "loanPackageButton",
                     '<content:getAttribute beanName="buttonHelpTextLoanPackage" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_printLoanReviewButton", "printLoanReviewButton",
  					'<content:getAttribute beanName="buttonHelpTextPrintLoanReview" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_denyButton", "denyButton",
                     '<content:getAttribute beanName="buttonHelpTextDeny" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_sendForReviewButton", "sendForReviewButton",
                     '<content:getAttribute beanName="buttonHelpTextSendForReview" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_sendForAcceptanceButton", "sendForAcceptanceButton",
                     '<content:getAttribute beanName="buttonHelpTextSendForAcceptance" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_sendForApprovalButton", "sendForApprovalButton",
                     '<content:getAttribute beanName="buttonHelpTextSendForApproval" attribute="text" escapeJavaScript="true"/>');

  setupButtonTooltip("tooltip_approveButton", "approveButton",
                     '<content:getAttribute beanName="buttonHelpTextApprove" attribute="text" escapeJavaScript="true"/>');

  <c:if test="${showApprovalDialog == 'true'}">
    YAHOO.johnhancock.psw.approvalDialog = new YAHOO.widget.Panel("ApprovalDialog", {
      constraintoviewport: true,
      fixedcenter: true,
      width: "460px",
      modal: true,
      close: false,
      underlay: "none",  
      zIndex: 4,
      visible:true} );
    YAHOO.johnhancock.psw.approvalDialog.render(document.body);
    YAHOO.util.Event.addListener("approvalDialogDisagree", "click", YAHOO.johnhancock.psw.approvalDialog.hide, YAHOO.johnhancock.psw.approvalDialog, true);
    YAHOO.util.Event.addListener("approvalDialogAgree", "click", submitAgreement);
  </c:if>

});

</script>

<c:if test="${showApprovalDialog == 'true'}">
  <content:contentBean contentId="${loanContentConstants.APPROVAL_DIALOG_TEXT}"
                       type="${contentConstants.TYPE_MISCELLANEOUS}"
                       id="approvalDialogText"/>
  <content:contentBean contentId="${loanForm.loanApprovalAdditionalContentKey}"
                       type="${contentConstants.TYPE_MISCELLANEOUS}"
                       id="additionalApprovalDialogText"/>
  <div id="ApprovalDialog">
    <div class="hd" style="padding: 0px; background:none; background-color: #AFAFAF">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td style="padding: 5px">
            <font style="font-size: 15px; font-weight: bold;color: #ffffff">Certification</font>
          </td>
        </tr>
      </table>
    </div>
    <div class="bd" style="background-color:#ffffff; padding: 0px">
      <table border="0" width="100%">
        <tr>
          <td colspan="2">
          <div style="height : 300px; overflow : auto;">
          <content:getAttribute beanName="additionalApprovalDialogText" attribute="text" escapeJavaScript="false"/>
          <content:getAttribute beanName="approvalDialogText" attribute="text" escapeJavaScript="false"/>
          </div>
          </td>
        </tr>
        <tr>
          <td align="center">
              <input id="approvalDialogDisagree" type="button" class="button100Lg" value="disagree"/>
          </td>
          <td align="center">
              <input id="approvalDialogAgree" type="button" class="button100Lg" value="agree"/>
          </td>
        </tr>
      </table>
    </div>
  </div>
</c:if>

        <table width="500" border="0" cellspacing="0" cellpadding="0">
          <c:if test="${displayRules.displayLoanPackageLink}">
            <tr>
              <td>
<a href="#" onclick="return handleLoanPackageLinkClicked();" id="loanPackageLink" title="loan package">



                  loan package   
</a>
                <br><br>
              </td>
            </tr>
          </c:if>

          <c:if test="${displayRules.displayButtonFooterText}">
            <tr>
              <td>
               <content:getAttribute attribute="text" beanName="buttonFooterText"></content:getAttribute>
              </td>
            </tr>
          </c:if>

        </table>
        <br>
        <table width="736" border="0" cellspacing="0" cellpadding="0">
          <tr>
             <c:if test="${displayRules.displayPrintLoanDocReviewButton}">
              <td width="120" align="right">
<input type="submit" id="printLoanReviewButton" class="button134" onclick="return handlePrintLoanDocButtonClicked();" name="actionLabel" value="print loan documents"/>



              </td>
            </c:if>
            <c:if test="${displayRules.displayExitEditModeButton}">
            <td width="120" align="right">
<input type="submit" id="exitEditModeButton" class="button110Lg" onclick="return confirmAndCancel();"  name="actionLabel" value="exit" /> 




		    </td>
		    </c:if>
            <c:if test="${displayRules.displaySaveAndExitButton}">
            <td width="120" align="right">
                       <input type="submit"  id="saveAndExitButton" class="button110Lg" onclick="return submitWithoutConfirmation();" name="actionLabel"  value="save &amp; exit" /> 




		    </td>
            </c:if>
            <c:if test="${displayRules.displayLoanPackageButton}">
              <td width="120" align="right">
<input type="submit" id="loanPackageButton" class="button110Lg" onclick="return handleLoanPackageButtonClicked();" name="actionLabel" value="loan package" /> 




              </td>
            </c:if>
            <c:if test="${displayRules.displayDeleteButton}">
              <td width="120" align="right">
<input type="submit" id="deleteButton" class="button110Lg" onclick="return submitWithConfirmation();" name="actionLabel" value="delete" /> 




		      </td>
            </c:if>
            <c:if test="${displayRules.displayDenyButton}">
              <td width="120" align="right">
<input type="submit" id="denyButton" class="button110Lg" onclick="return submitWithConfirmation();" name="actionLabel" value="deny" /> 




		      </td>
            </c:if>
            <c:if test="${displayRules.displaySendForReviewButton}">
              <td width="145" align="right">
<input type="submit" id="sendForReviewButton" class="button110Lg" onclick="return submitWithConfirmation();" name="actionLabel" value="send for review" /> 




		      </td>
            </c:if>
            <c:if test="${displayRules.displaySendForAcceptanceButton}">
              <td width="145" align="right">
<input type="submit" id="sendForAcceptanceButton" class="button134" onclick="return submitWithConfirmation();" name="actionLabel" value="send for acceptance" /> 




              </td>
            </c:if>
            <c:if test="${displayRules.displaySendForApprovalButton}">
              <td width="120" align="right">
<input type="submit" id="sendForApprovalButton" class="button110Lg" onclick="return submitWithConfirmation();" name="actionLabel" value="send for approval" /> 




		      </td>
		    </c:if>
            <c:if test="${displayRules.displayApproveButton}">
              <td width="120" align="right">
<input type="submit" id="approveButton" class="button110Lg" onclick="return submitWithoutConfirmation();" name="actionLabel" value="approve" /> 




  		      </td>
		    </c:if>
            <c:if test="${displayRules.displayExitViewModeButton}">
              <td width="111" align="right">
<input type="submit" id="exitViewModeButton" class="button110Lg" name="actionLabel" value="exit" /> 



            </c:if>
          </tr>
        </table>

