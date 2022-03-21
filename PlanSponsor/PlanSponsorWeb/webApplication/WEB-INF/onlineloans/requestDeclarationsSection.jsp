<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="loanConstants" className="com.manulife.pension.service.loan.domain.LoanConstants" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanFields" className="com.manulife.pension.service.loan.LoanField" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />

<content:contentBean
  contentId="${loanContentConstants.DECLARATIONS_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationsSectionTitleText"/>
<content:contentBean
  contentId="${loanContentConstants.DECLARATIONS_SECTION_FOOTER}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationsSectionFooterText"/>

<c:url var="truthInLendingNoticeUrl" value="/do/onlineloans/viewManagedContent/">
  <c:param name="action" value="viewTruthInLendingNotice"/>
  <c:param name="submissionId" value="${loan.submissionId}"/>
  <c:param name="contractId" value="${loan.contractId}"/>
  <c:param name="printFriendly" value="true"/>
</c:url>

<c:url var="promissoryNoteUrl" value="/do/onlineloans/viewManagedContent/">
  <c:param name="action" value="viewPromissoryNote"/>
  <c:param name="submissionId" value="${loan.submissionId}"/>
  <c:param name="contractId" value="${loan.contractId}"/>
  <c:param name="printFriendly" value="true"/>
</c:url>

<script language="JavaScript1.2" type="text/javascript">
  
  function showLoanDocuments() {
    var url = document.location.href + "&actionLabel=printLoanDocumentsPdf&ext=.pdf";
    openLoanDocumentWindow(url);
    <c:if test="${showLoanDocuments == 'true'}">
      YAHOO.johnhancock.psw.printLoanDocumentsDialog.hide();
    </c:if>
  }

  function showManagedContent(url)
  {
    var reportURL = new URL(url);
    if (typeof(windowName) == 'undefined' || windowName == null) {
      windowName = "";
    }
    window.open(reportURL.encodeURL(),windowName,"width=720,height=480,resizable,toolbar,scrollbars,menubar");
    return false;
  }

  YAHOO.util.Event.onDOMReady(function () { 
    new YAHOO.widget.Tooltip("tooltip_printLoanDocumentsButton", {  
      context:"printLoanDocumentsButton",  
      text:'Prints the loan documents',
      showDelay:500, autodismissdelay:999999} ); 

  <c:if test="${showLoanDocuments == 'true'}">
    YAHOO.johnhancock.psw.printLoanDocumentsDialog = new YAHOO.widget.Panel("PrintLoanDocumentsDialog", {
      constraintoviewport: true,
      fixedcenter: true,
      width: "360px",
      modal: true,
      close: false,
      underlay: "none",  
      zIndex: 4,
      visible:true} );
    YAHOO.johnhancock.psw.printLoanDocumentsDialog.render(document.body);
    YAHOO.util.Event.addListener("printLoanDocumentsDialogCancel", "click", YAHOO.johnhancock.psw.printLoanDocumentsDialog.hide, YAHOO.johnhancock.psw.printLoanDocumentsDialog, true);
    YAHOO.util.Event.addListener("printLoanDocumentsDialogOK", "click", showLoanDocuments);
  </c:if>
});

</script>

<c:if test="${showLoanDocuments == 'true'}">
  <content:contentBean contentId="${loanContentConstants.PRINT_LOAN_DOCUMENTS_DIALOG_TEXT}"
                       type="${contentConstants.TYPE_MISCELLANEOUS}"
                       id="printLoanDocumentsDialogText"/>
  <div id="PrintLoanDocumentsDialog">
    <div class="hd" style="padding: 0px; background:none; background-color: #AFAFAF">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td style="padding: 5px">
            <font style="font-size: 15px; font-weight: bold;color: #ffffff">Print loan documents</font>
          </td>
        </tr>
      </table>
    </div>
    <div class="bd" style="background-color:#ffffff; padding: 0px">
      <table border="0" width="100%">
        <tr>
          <td colspan="2">
          <content:getAttribute beanName="printLoanDocumentsDialogText" attribute="text"/>
          </td>
        </tr>
        <tr>
          <td align="center">
              <input id="printLoanDocumentsDialogCancel" type="button" class="button100Lg" value="Cancel"/>
          </td>
          <td align="center">
              <input id="printLoanDocumentsDialogOK" type="button" class="button100Lg" value="OK"/>
          </td>
        </tr>
      </table>
    </div>
  </div>
</c:if>
<c:if test="${displayRules.showDeclarationsSection}">
 <br>
 <br>
<table class="box" border="0" cellpadding="0" cellspacing="0" width="734">
  <tr>
    <td class="tableheadTD1">
    <b>
      <content:getAttribute attribute="text" beanName="declarationsSectionTitleText">
      </content:getAttribute>
    </b>
    </td>
    <td width="141" class="tablehead">&nbsp;</td>
  </tr>
</table>
<div id="declarationsSection">
  <table class="box" border="0" cellpadding="0" cellspacing="0" width="734">
    <tr>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td width="732">
        <table border="0" cellpadding="0" cellspacing="0" width="732">
          <tr valign="top">
            <td colspan="2" class="datacell1"><strong>The participant agreed to: </strong></td>
          </tr>

          <tr valign="top">
            <td width="5" class="datacell1">&nbsp;</td>
            <td class="datacell1">
              <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                <td width="50%">
                  <ps:fieldHilight name="${loanFields.TRUTH_IN_LENDING_NOTICE.fieldName}"
                                   singleDisplay="true"/>
                  <c:if test="${not displayRules.showTruthInLendingNoticeAsDisabled}">
                    <ps:trackChanges escape="true" property="truthInLendingNotice.accepted" name="loanForm"/>
<form:checkbox path="truthInLendingNotice.accepted" disabled="false"/>

                  </c:if>
                  <c:if test="${displayRules.showTruthInLendingNoticeAsDisabled}">
<form:checkbox path="truthInLendingNotice.accepted" disabled="true"/>

                  </c:if>
                  Truth In Lending Notice 
                  <c:if test="${displayRules.displayTruthInLendingNoticeViewLink}">
                    <a href="#" onclick="return showManagedContent('${truthInLendingNoticeUrl}');">View</a>
                  </c:if>
                  <br>
                  <ps:fieldHilight name="${loanFields.PROMISSORY_NOTE.fieldName}"
                                   singleDisplay="true"/>
                  <c:if test="${not displayRules.showPromissoryNoteAsDisabled}">
                    <ps:trackChanges escape="true" property="promissoryNote.accepted" name="loanForm"/>
<form:checkbox path="promissoryNote.accepted" disabled="false"/>

                  </c:if>
                  <c:if test="${displayRules.showPromissoryNoteAsDisabled}">
<form:checkbox path="promissoryNote.accepted" disabled="true"/>

                  </c:if>
                  Non-negotiable Promissory Note and Irrevocable Pledge and Assignment
                  <c:if test="${displayRules.displayPromissoryNoteViewLink}">
                    <a href="#" onclick="return showManagedContent('${promissoryNoteUrl}');">View</a>
                  </c:if>
                </td>
                <c:if test="${displayRules.displayPrintLoanDocumentsButton}" >
                  <td width="35%">
                  <c:choose>
                    <c:when test="${displayRules.editMode}">
<input type="submit" id="printLoanDocumentsButton" name="actionLabel" class="button134" onclick="return submitWithoutConfirmation();" value="print loan documents" /><%--  - property="actionLabel" --%>




                    </c:when>
                    <c:otherwise>
<input type="button" onclick="showLoanDocuments();" name="actionLabel" class="button134" id="printLoanDocumentsButtonViewMode" value="print loan documents"/>




                    </c:otherwise>
                  </c:choose>
                  </td>
                </c:if>
                </tr>
              </table>
            </td>

          </tr>

          <c:if test="${displayRules.displayParticipantDeclarationCheckbox}" >
            <tr valign="top">
              <td width="5" class="datacell1">&nbsp;</td>
              <td class="datacell1">
<form:checkbox path="participantDeclaration.accepted" disabled="true"/>

                The following authorization:<br>
                ${loanForm.participantDeclaration.html}
              </td>
            </tr>
          </c:if>

          <c:if test="${displayRules.displayApproverAgreedToLabel}" >
            <tr valign="top">
              <td colspan="2" class="datacell1">
                <br><strong>The approver agreed to: </strong>
              </td>
            </tr>
          </c:if>

          <c:if test="${displayRules.displayApproverAgreedToText}" >
            <tr valign="top">
              <td colspan="2" class="datacell1">
                ${displayRules.loanApprovalPlanSpousalConsentContent}
                ${displayRules.loanApprovalGenericContent}
                <br>
                <br>
              </td>
            </tr>
          </c:if>
          <c:if test="${displayRules.displayAtRiskTransactionCheckbox}" >
            <tr valign="top">
              <td width="5" class="datacell1">&nbsp;</td>
              <td class="datacell1" style="padding: 0px;">
                <table>
                 <tr valign="top">
		            <td colspan="2" class="datacell1"><strong>It was certified that: </strong></td>
		         </tr>
		           <tr>
		           		<td style="vertical-align:top; padding: 0;">
		                <ps:fieldHilight name="${loanFields.AT_RISK_TRANSACTION.fieldName}"
		                                 singleDisplay="true"/>
		                <c:if test="${not displayRules.showAtRiskTransactionCheckBoxAsDisabled}">
		                  <ps:trackChanges escape="true" property="atRiskTransaction.accepted" name="loanForm"/>
<form:checkbox path="atRiskTransaction.accepted" disabled="false"/>

		                </c:if>
		                <c:if test="${displayRules.showAtRiskTransactionCheckBoxAsDisabled}">
<form:checkbox path="atRiskTransaction.accepted" disabled="true"/>

		                </c:if>
		                </td>
		                <td>
						<c:if test="${loanForm.atRiskTransactionInd}">
							<p>${loanForm.atRiskTransaction.html}</p>
							<ol>
<c:if test="${not empty loanForm.detailText}">
<c:forEach items="${loanForm.detailText}" var="detailTextId">
		                  			<li>${detailTextId}</li>
</c:forEach>
</c:if>
						 	</ol> 
						</c:if>
						</td>
					</tr>	
				</table>
               </td>
            </tr>
          </c:if>
  
        </table>
      </td>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

    </tr>
    <tr>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td colspan="1" class="datacell1">
        <content:getAttribute attribute="text" beanName="declarationsSectionFooterText">
        </content:getAttribute>
      </td>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
  </table>
</div>
</c:if>
