<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="render" uri="/WEB-INF/render.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="webConstants" className="com.manulife.pension.ps.web.Constants" />
<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="loanFields" className="com.manulife.pension.service.loan.LoanField" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />
<un:useConstants var="loanConstants" className="com.manulife.pension.service.loan.domain.LoanConstants" />

<content:contentBean
  contentId="${loanContentConstants.PAGE_CONTENT_NOT_FINAL_DISCLAIMER}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="pageContentNotFinalDisclaimer"/>
<content:contentBean
  contentName="${loanContentConstants.GIFL_MSG_EXTERNAL_USER_INITIATED}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="giflMsgExternalUserInitiated"/>
<content:contentBean
  contentName="${loanContentConstants.GIFL_MSG_PARTICIPANT_INITIATED}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="giflMsgParticipantInitiated"/>
<content:contentBean
  contentId="${displayRules.accountBalanceFootnoteCmaKey}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="accountBalanceFootnoteText"/>

<script language="JavaScript1.2" type="text/javascript">

var pageValidator = new PageValidator();

/**
 * This function prepares and submits the form. This is tied closely to the MessagesTag, as
 * it's used to submit the form if the user clicks 'OK' on the warnings popup message.
 * The 'submitAction' is hidden field in the form that holds the latest action attempted.
 * The 'ignoreWarningsId' is a hidden field in the form that determines if warnings are ignored.
 */
function processForm() {
  var form = document.getElementsByName("loanForm")[0];
  form["ignoreWarning"].value = "true";
  form["action"].value = "${e:forJavaScriptBlock(loanForm.action)}";
  form.submit();
}

function doConfirm(msg) {
  return confirm(msg);
}

function doPrint() {
  var reportURL = new URL(document.location.href);
  reportURL.setParameter("actionLabel", "print");
  reportURL.setParameter("printFriendly", "true");
  window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
}

function doPrintForParticipant() {
  var reportURL = new URL(document.location.href);
  reportURL.setParameter("actionLabel", "printForParticipant");
  reportURL.setParameter("printFriendly", "true");
  window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
}

function isFormChanged() {
  return changeTracker.hasChanged();
}

registerTrackChangesFunction(isFormChanged);
registerWarningOnChangeToLinks(new Array("loansUserGuideLink_text",
                                         "loansUserGuideLink_icon",
                                         "loansQuickReferenceLink_text",
                                         "loansQuickReferenceLink_icon",
                                         "loansDemo_text",
                                         "loansDemo_icon",
                                         "printReportForParticipantLink_text",
                                         "printReportForParticipantLink_icon",
                                         "printReportLink_text",
                                         "printReportLink_icon"));
										 
			

function setUpExpandCollapseIcon(sectionId) {
  var icon = $("#" + sectionId + "ExpandCollapseIcon");
  var section = $("#" + sectionId);
  icon.clicktoggle(
    function() {
      section.hide();
      icon.attr('src', '/assets/unmanaged/images/plus_icon.gif');
    }, 
    function() {
      section.show();
      icon.attr('src', '/assets/unmanaged/images/minus_icon.gif');
    }
  );
}

var sections = ["loanDetailsSection",
                "calculateMaximumLoanAmountSection",
                "loanCalculationsSection",
                "paymentInformationSection",
                "declarationsSection",
                "notesSection"];

<c:if test="${displayRules.displayExpandCollapseButton}">
function expandAllSections() {
  for (var i = 0; i < sections.length; i++) {
    var icon = $("#" + sections[i] + "ExpandCollapseIcon");
    if (icon.attr('src') == '/assets/unmanaged/images/plus_icon.gif') {
      icon.click();
    }
  }
}

function contractAllSections() {
  for (var i = 0; i < sections.length; i++) {
    var icon = $("#" + sections[i] + "ExpandCollapseIcon");
    if (icon.attr('src') == '/assets/unmanaged/images/minus_icon.gif') {
      icon.click();
    }
  }
}
</c:if>

function showIconForFailure(fieldId) {
  /* show error icon */
  var errorIcon = $('#errorIcon_' + fieldId);
  if (errorIcon.length == 0) {
    errorIcon = $('#dynamicErrorIcon_' + fieldId);
  }
  if (errorIcon.length != 0) {
    errorIcon.show();
  }
  /* suppress all other icons */
  $('#warningIcon_' + fieldId).hide();
  $('#alertIcon_' + fieldId).hide();
  $('#activityIcon_' + fieldId).hide();
}

function showIconForSuccess(fieldId) {
  /* hide error icon */
  var errorIcon = $('#errorIcon_' + fieldId);
  if (errorIcon.length == 0) {
    errorIcon = $('#dynamicErrorIcon_' + fieldId);
  }
  if (errorIcon.length != 0) {
    errorIcon.hide();
  }
  /* suppress all other icons, except the activity history icon */
  $('#warningIcon_' + fieldId).hide();
  $('#alertIcon_' + fieldId).hide();
  $('#activityIcon_' + fieldId).show();
}

<c:if test="${displayRules.loanAmountDisplayOnlyRecalculated}">
function resetLoanAmount() {
  // Recalculates the loan amount field, but should ONLY be called when the 
  // loan amount is a protected field and needs to be recalculated.
  var newLoanAmount = ${loanForm.loanAmountOriginalValue};
  var maximumLoanAvailable = numberUtils.parseNumber($("#calculatorMaxLoanAvailableSpan").text());
  var totalAvailableBalance = numberUtils.parseNumber($("#totalAvailableBalance").text());
  var planMinimumLoanAmount = ${loanPlanData.minimumLoanAmount};

  if (isNaN(maximumLoanAvailable) || isNaN(totalAvailableBalance)) {
    $("#loanAmountSpan").text('');
    $("#loanAmount").val('');
     return;   
  }

  newLoanAmount = Math.min(newLoanAmount, maximumLoanAvailable);
  newLoanAmount = Math.min(newLoanAmount, totalAvailableBalance);

  var minValue = Math.min(planMinimumLoanAmount, newLoanAmount);
  if (minValue == newLoanAmount) {
    newLoanAmount = planMinimumLoanAmount;
  }
  $("#loanAmountSpan").text(numberUtils.formatAmount(newLoanAmount, true, true));
  $("#loanAmount").val(newLoanAmount);

  // The following is from requestLoanCalculationSection.jsp
  onLoanAmountChangeSuccess(undefined, undefined);
}
</c:if>

function openLoanDocumentWindow(PDFURL) {
  PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
  window.open(PDFURL,"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
}

YAHOO.namespace("johnhancock.psw");

$(document).ready(function() {

  <c:if test="${displayRules.displayExpandCollapseButton}">
  /*
   * Setup expand and collpase icons
   */
  var sectionExpandFlags = [ ${displayRules.expandLoanDetailsSection},
                             ${displayRules.expandCalculateMaximumLoanAmountSection},
                             ${displayRules.expandLoanCalculationsSection},
                             ${displayRules.expandPaymentInformationSection},
                             ${displayRules.expandDeclarationsSection},
                             ${displayRules.expandNotesSection}];

  for (var i = 0; i < sections.length; i++) {
    setUpExpandCollapseIcon(sections[i]);
    if (! sectionExpandFlags[i]) {
      $("#" + sections[i] + "ExpandCollapseIcon").trigger("click");
    }
  }
  </c:if>

  /*
   * Configure the Activity History pop-up text boxes for each field that has
   * activity history.
   */
  <c:forEach items="${loanActivities.activityRecords}" var="record">
    <c:if test="${empty record.subItemName}">
      if (document.getElementById("activityIcon_${record.fieldName}") != null) {
        myTooltip = new YAHOO.widget.Tooltip("tooltip_${record.fieldName}", {  
                        context:["activityIcon_${record.fieldName}", "tl", "br"],
                        text:$("#activity_${record.fieldName}").html(),
                        showDelay:100, hidedelay:1, autodismissdelay:999999} );
      }
 
    </c:if>
    <c:if test="${not empty record.subItemName}">
      <c:forEach items="${loan.moneyTypes}" var="moneyType" varStatus="moneyTypeStatus">
        <c:if test="${moneyType.moneyTypeId == record.subItemName}">
          if (document.getElementById("activityIcon_${moneyTypeStatus.index}") != null) {
            myTooltip = new YAHOO.widget.Tooltip("tooltip_${moneyTypeStatus.index}", {  
                        context:["activityIcon_${moneyTypeStatus.index}", "tl", "br"],  
                        text:$("#activity_${record.fieldName}_${record.subItemName}").html(),
                        showDelay:100, hidedelay:1, autodismissdelay:999999} ); 
          }
        </c:if>
      </c:forEach>
    </c:if>
  </c:forEach>

});

</script>

  <!--
   * Configure the Activity History content to be shown in the Activity History
   * pop-up text boxes for each field that has activity history.
  -->
<c:forEach items="${loanActivities.activityRecords}" var="record">
  <c:if test="${empty record.subItemName}">
  <div id="activity_${record.fieldName}" style="display:none">
  </c:if>
  <c:if test="${not empty record.subItemName}">
  <div id="activity_${record.fieldName}_${record.subItemName}" style="display:none">
  </c:if>

  <table border="0">
    <c:if test="${record.displaySystemOfRecordInfo}">
      <tr>
        <td colspan=2>Information on record with John Hancock:</td>
        <td align="${record.textAlignment}">
          <c:choose>
            <c:when test="${record.fieldName == loanFields.PAYMENT_METHOD.fieldName}">
              ${paymentMethods[record.systemOfRecordValue]}
            </c:when>
            <c:when test="${record.fieldName == loanFields.ACCOUNT_TYPE.fieldName}">
              ${bankAccountTypes[record.systemOfRecordValue]}
            </c:when>
            <c:when test="${record.fieldName == loanFields.STATE.fieldName}">
              <c:choose>
                <c:when test="${loanActivities.systemOfRecordCountryUSA}">
                  ${states[record.systemOfRecordValue]}
                </c:when>
                <c:otherwise>
                  ${record.systemOfRecordValue}
                </c:otherwise>
              </c:choose>
            </c:when>
            <c:when test="${record.fieldName == loanFields.COUNTRY.fieldName}">
              ${countries[record.systemOfRecordValue]}
            </c:when>
            <c:otherwise>
              ${record.formattedSystemOfRecordValue}
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </c:if>

    <c:if test="${record.displayOriginalInfo}">
      <tr>
        <td colspan=2>Original information submitted for this request:</td>
        <td align="${record.textAlignment}">
          <c:choose>
            <c:when test="${record.fieldName == loanFields.PAYMENT_METHOD.fieldName}">
              ${paymentMethods[record.originalValue]}
            </c:when>
            <c:when test="${record.fieldName == loanFields.ACCOUNT_TYPE.fieldName}">
              ${bankAccountTypes[record.originalValue]}
            </c:when>
            <c:when test="${record.fieldName == loanFields.STATE.fieldName}">
              <c:choose>
                <c:when test="${loanActivities.originalCountryUSA}">
                  ${states[record.originalValue]}
                </c:when>
                <c:otherwise>
                  ${record.originalValue}
                </c:otherwise>
              </c:choose>
            </c:when>
            <c:when test="${record.fieldName == loanFields.COUNTRY.fieldName}">
              ${countries[record.originalValue]}
            </c:when>
            <c:otherwise>
              ${record.formattedOriginalValue}
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </c:if>
    <c:if test="${record.displaySubmittedByInfo}">
      <tr>
        <td>
          <font style="font-size: smaller">
          &nbsp;&nbsp;Submitted by:
          </font>
        </td>
        <td>
          <font style="font-size: smaller">
          <c:choose>
				<c:when	test="${(record.submittedByRole == loanConstants.USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME)}">
					<logicext:if name="<%= Constants.USERPROFILE_KEY%>"	property="role.TPA" op="equal" value="true">
						<logicext:or name="<%= Constants.USERPROFILE_KEY%>"	property="role.externalUser" op="equal" value="true" />
						<logicext:then>
						 ${record.submittedByRole}
						</logicext:then>
						<logicext:else>
						 	${record.submittedByNameAndRole}
						</logicext:else>
					</logicext:if>
				</c:when>
				<c:otherwise>
	 		        ${record.submittedByNameAndRole}
		        </c:otherwise>
		  </c:choose>
          </font>
        </td>
      </tr>
      <tr>
        <td></td>
        <td>
          <font style="font-size: smaller">
          <render:date property="record.submittedByTimestamp" patternOut="${renderConstants.LONG_TIMESTAMP_MDY_SLASHED}"/>
          </font>
        </td>
      </tr>
    </c:if>
    
    <tr>
      <td colspan=2>Current information for this request:</td>
      <td align="${record.textAlignment}">
        <c:choose>
          <c:when test="${record.fieldName == loanFields.PAYMENT_METHOD.fieldName}">
              ${paymentMethods[record.savedValue]}
            </c:when>
            <c:when test="${record.fieldName == loanFields.ACCOUNT_TYPE.fieldName}">
            ${bankAccountTypes[record.savedValue]}
          </c:when>
          <c:when test="${record.fieldName == loanFields.STATE.fieldName}">
            <c:choose>
              <c:when test="${loanActivities.savedCountryUSA}">
                ${states[record.savedValue]}
              </c:when>
              <c:otherwise>
                ${record.savedValue}
              </c:otherwise>
            </c:choose>
          </c:when>
          <c:when test="${record.fieldName == loanFields.COUNTRY.fieldName}">
            ${countries[record.savedValue]}
          </c:when>
          <c:otherwise>
            ${record.formattedSavedValue}
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <c:if test="${record.displayChangedByInfo}">
      <tr>
        <td>
          <font style="font-size: smaller">
          &nbsp;&nbsp;Changed by:
          </font>
        </td>
        <td>
          <font style="font-size: smaller">
           <c:choose>
				<c:when	test="${(record.changedByRole == loanConstants.USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME)}">
					<logicext:if name="<%= Constants.USERPROFILE_KEY%>"	property="role.TPA" op="equal" value="true">
						<logicext:or name="<%= Constants.USERPROFILE_KEY%>"	property="role.externalUser" op="equal" value="true" />
						<logicext:then>
						 	${record.changedByRole}
						</logicext:then>
						<logicext:else>
						 	${record.changedByNameAndRole}
						</logicext:else>
					</logicext:if>
				</c:when>
				<c:otherwise>
	 		        ${record.changedByNameAndRole}
		        </c:otherwise>
		  </c:choose>
          </font>
        </td>
      </tr>
      <tr>
        <td></td>
        <td>
          <font style="font-size: smaller">
          <render:date property="record.changedByTimestamp" patternOut="${renderConstants.LONG_TIMESTAMP_MDY_SLASHED}"/>
          </font>
      </td>
      </tr>
    </c:if>

  </table>
  </div>

</c:forEach>


<jsp:include page="requestHandlers.jsp"/>

<ps:form method="POST" action="${postPath}" modelAttribute="loanForm" name="loanForm">

<input type="hidden" name="action" value="">
<input type="hidden" name="ignoreWarning"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <c:if test="${! param.printFriendly}">
    <tr>
      <c:if test="${displayRules.displayYouAreAt}">
        <content:contentBean
          contentId="${displayRules.youAreAtCmaId}"
          type="${contentConstants.TYPE_MISCELLANEOUS}"
          id="youAreAt"/>

        <td width="30">
          <img src="/assets/unmanaged/images/s.gif" height="1" width="30">
        </td>
        <td valign="top" colspan=2><br>
          <table border="0" cellpadding="0" cellspacing="0" width="350">
            <tr>
              <td><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td>
                <content:getAttribute beanName="youAreAt" attribute="text"/>
              </td>
              <td><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </tr>
          </table>
        </td>
      </c:if> <%-- displayYouAreAt --%>
    </tr>
  </c:if> <%-- not printFriendly --%>
  <tr>
    <td width="30" rowspan="2">
      <img src="/assets/unmanaged/images/s.gif" height="1" width="30"><br>
      <img src="/assets/unmanaged/images/s.gif" height="1">
    </td>
    <td width="500">
      <div id="messagesSectionId">
        <br/><ps:messages scope="session" maxHeight="100px"/> 
      </div>

      <c:if test="${displayRules.displayPageContentNotFinalDisclaimer}">
        <br/>
        <content:getAttribute attribute="text" beanName="pageContentNotFinalDisclaimer"></content:getAttribute>
      </c:if>

      <c:if test="${displayRules.displayGiflMsgExternalUserInitiated}">
        <br/>
        <content:getAttribute attribute="text" beanName="giflMsgExternalUserInitiated"></content:getAttribute>
      </c:if>
      <c:if test="${displayRules.displayGiflMsgParticipantInitiated}">
        <br/>
        <content:getAttribute attribute="text" beanName="giflMsgParticipantInitiated"></content:getAttribute>
      </c:if>

      <c:if test="${displayRules.displaySubmissionNumber}">
        <br/>
        <span class="highlightBold">Submission number: ${loan.submissionId}</span>
        <c:if test="${displayRules.displaySubmissionStatus}">
          <br/>
          <span class="highlightBold">Status: ${loanStatusCodes[loan.status]}</span>
        </c:if>
        <c:if test="${displayRules.displaySubmissionProcessingDates}">
          <br/>
          <span class="highlightBold">
            Approved date/time: <fmt:formatDate value="${loanActivities.summaryForApprovedStatus.created}" 
                                                type="both" pattern="${renderConstants.LONG_MDY_SLASHED}"/>
          </span>
          <br/>
          <span class="highlightBold">
            Expected processing date: <fmt:formatDate value="${loan.effectiveDate}" 
                                                type="date" pattern="${renderConstants.MEDIUM_MDY_SLASHED}"/> 
        </c:if>
        <br/><br/>
      </c:if>

      <c:if test="${displayRules.displayExpandCollapseButton}">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td>
              <br/>
	          <strong>
	          <a href="#" onclick="expandAllSections(); return false;"><img src="/assets/unmanaged/images/plus_icon.gif" width="13" height="13" border="0"></a> /
	          <a href="#" onclick="contractAllSections(); return false;"><img src="/assets/unmanaged/images/minus_icon.gif" width="13" height="13" border="0"></a> all sections
	          </strong>
	        </td>
	      </tr>
	    </table>
	  </c:if>
      <br>
      <c:if test="${displayRules.displayNotesViewSection}">
        <jsp:include page="requestNotesViewSection.jsp"/>
        <br>
        <br>
      </c:if>
	  <jsp:include page="requestParticipantInformationSection.jsp"/>
   </td>
	<!-- end main content table --> 
  </tr>
  <tr>
    <td colspan="2">
      <br>
      <br>
      <jsp:include page="requestLoanDetailsSection.jsp"/>
      <br>
      <br>
      <jsp:include page="requestCalculateMaxLoanAvailableSection.jsp"/>
      <br>
      <br>
      <jsp:include page="requestLoanCalculationSection.jsp"/>
      <c:if test="${displayRules.displayPaymentInstructionSection}">
        <br>
        <br>
	    <jsp:include page="requestPaymentInstructionsSection.jsp"/>
	    <jsp:include page="bankLookup.jsp"/>
	  </c:if>
      <c:if test="${displayRules.showDeclarationsSection or showLoanDocuments == 'true'}">

        <jsp:include page="requestDeclarationsSection.jsp"/>
      </c:if>
      <c:if test="${displayRules.displayNotesEditSection}">
        <br>
        <br>
        <jsp:include page="requestNotesSection.jsp"/>
      </c:if>
      <br>
      <br>
	  <jsp:include page="requestButtonsSection.jsp"/>

    <!-- Loan request specific page footer & disclaimer -------------------- -->
      <table width="734" border="0" cellspacing="0" cellpadding="0">
        <c:if test="${not empty displayRules.accountBalanceFootnoteCmaKey}">
          <tr><td>&nbsp</td></tr>
          <tr>
            <td width="100%"><content:getAttribute attribute="text" 
                                beanName="accountBalanceFootnoteText" /></td>
          </tr>
        </c:if>
        <c:if test="${displayRules.printFriendly}">
          <tr><td>&nbsp</td></tr>
          <content:contentBean
            contentId="${contentConstants.GLOBAL_DISCLOSURE}"
            type="${contentConstants.TYPE_MISCELLANEOUS}"
            id="globalDisclosure"/>
          <tr>
            <td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
          </tr>
        </c:if>
      </table>

    </td>
  </tr>

</table>
</ps:form>
