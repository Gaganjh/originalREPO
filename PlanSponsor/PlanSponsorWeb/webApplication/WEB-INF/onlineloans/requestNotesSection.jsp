<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>

<un:useConstants var="loanConstants" className="com.manulife.pension.service.loan.domain.LoanConstants" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanFields" className="com.manulife.pension.service.loan.LoanField" />
<un:useConstants var="noteConstants" className="com.manulife.pension.service.distribution.valueobject.Note" />

<content:contentBean
  contentName="${loanContentConstants.NOTE_TO_PARTICIPANT_TOO_LONG}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="currentParticipantNoteTooLongText"/>
<content:contentBean
  contentName="${loanContentConstants.NOTE_TO_PARTICIPANT_INVALID_CHARACTERS}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="currentParticipantNoteInvalidCharactersText"/>
<content:contentBean
  contentName="${loanContentConstants.NOTE_TO_ADMINISTRATOR_TOO_LONG}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="currentAdministratorNoteTooLongText"/>
<content:contentBean
  contentName="${loanContentConstants.NOTE_TO_ADMINISTRATOR_INVALID_CHARACTERS}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="currentAdministratorNoteInvalidCharactersText"/>

<script language="JavaScript1.2" type="text/javascript">

var isParticipantNoteTooLongSuccess = true;

function onParticipantNoteTooLongSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.CURRENT_PARTICIPANT_NOTE.fieldName}');
  isParticipantNoteTooLongSuccess = true;
}

function onParticipantNoteTooLongFailure(e, callbackParams) {
  showIconForFailure('${loanFields.CURRENT_PARTICIPANT_NOTE.fieldName}');
  var alertType = callbackParams[0];
  var valueLength = callbackParams[1];
  if (alertType == 'alert') {
    var errorMessage = '<content:getAttribute attribute="text" beanName="currentParticipantNoteTooLongText" escapeJavaScript="true"></content:getAttribute>';
    alert(errorMessage.replace(/\{0\}/, valueLength));
  }
  isParticipantNoteTooLongSuccess = false;

  // clear the length value in callbackParams.
  callbackParams.length = 1;
}

function onParticipantNoteInvalidCharacterSuccess(e, callbackParams) {
  // Only if the onParticipantNoteTooLong edit was successful do we want to hide the 
  // error icon.  If it failed, we don't want to overwrite the error icon already displayed. 
  if (isParticipantNoteTooLongSuccess) {
    showIconForSuccess('${loanFields.CURRENT_PARTICIPANT_NOTE.fieldName}');
  }
}

function onParticipantNoteInvalidCharacterFailure(e, callbackParams) {
  showIconForFailure('${loanFields.CURRENT_PARTICIPANT_NOTE.fieldName}');
  var alertType = callbackParams[0];
  if (alertType == 'alert') {
    var errorMessage = '<content:getAttribute attribute="text" beanName="currentParticipantNoteInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>'; 

    // Change message verb to 'is' or 'are' depending on number of invalid characters.
    errorMessage = errorMessage.replace(/\{1\}/, callbackParams[1].length > 1 ? 'are' : 'is');  

    // Add invalid characters to message string
    errorMessage = errorMessage.replace(/\{0\}/, callbackParams[1]);

    alert(errorMessage);
  }

  // clear the invalid character array.
  callbackParams.length = 1;
}

var isAdministratorNoteTooLongSuccess = true;

function onAdministratorNoteTooLongSuccess(e, callbackParams) {
  showIconForSuccess('${loanFields.CURRENT_ADMINISTRATOR_NOTE.fieldName}');
  isAdministratorNoteTooLongSuccess = true;
}

function onAdministratorNoteTooLongFailure(e, callbackParams) {
  showIconForFailure('${loanFields.CURRENT_ADMINISTRATOR_NOTE.fieldName}');
  var alertType = callbackParams[0];
  var valueLength = callbackParams[1];
  if (alertType == 'alert') {
    var errorMessage = '<content:getAttribute attribute="text" beanName="currentAdministratorNoteTooLongText" escapeJavaScript="true"></content:getAttribute>';
    alert(errorMessage.replace(/\{0\}/, valueLength));
  }
  isAdministratorNoteTooLongSuccess = false;

  // clear the length value in callbackParams.
  callbackParams.length = 1;
}

function onAdministratorNoteInvalidCharacterSuccess(e, callbackParams) {
  // Only if the onAdministratorNoteTooLong edit was successful do we want to hide the 
  // error icon.  If it failed, we don't want to overwrite the error icon already displayed. 
  if (isAdministratorNoteTooLongSuccess) {
    showIconForSuccess('${loanFields.CURRENT_ADMINISTRATOR_NOTE.fieldName}');
  }
}

function onAdministratorNoteInvalidCharacterFailure(e, callbackParams) {
  showIconForFailure('${loanFields.CURRENT_ADMINISTRATOR_NOTE.fieldName}');
  var alertType = callbackParams[0];
  if (alertType == 'alert') {
    var errorMessage = '<content:getAttribute attribute="text" beanName="currentAdministratorNoteInvalidCharactersText" escapeJavaScript="true"></content:getAttribute>'; 

    // Change message verb to 'is' or 'are' depending on number of invalid characters.
    errorMessage = errorMessage.replace(/\{1\}/, callbackParams[1].length > 1 ? 'are' : 'is');  

    // Add invalid characters to message string
    errorMessage = errorMessage.replace(/\{0\}/, callbackParams[1]);

    alert(errorMessage);
  }

  // clear the invalid character array.
  callbackParams.length = 1;
}


$(document).ready(function(){
  pageValidator.registerMaximumLength('currentParticipantNote',
    'keyup', ${noteConstants.MAXIMUM_LENGTH}, true, onParticipantNoteTooLongSuccess, onParticipantNoteTooLongFailure,
    ['noalert']);
  pageValidator.registerMaximumLength('currentParticipantNote',
    'blur', ${noteConstants.MAXIMUM_LENGTH}, true, onParticipantNoteTooLongSuccess, onParticipantNoteTooLongFailure,
    ['alert']);
  pageValidator.registerAllowedCharacters('currentParticipantNote',
    'keyup', PageValidator.WEB_MULTILINE_CHARACTER_REGEXP, onParticipantNoteInvalidCharacterSuccess,
    onParticipantNoteInvalidCharacterFailure, ['noalert']);
  pageValidator.registerAllowedCharacters('currentParticipantNote',
    'blur', PageValidator.WEB_MULTILINE_CHARACTER_REGEXP, onParticipantNoteInvalidCharacterSuccess,
    onParticipantNoteInvalidCharacterFailure, ['alert']);

  pageValidator.registerMaximumLength('currentAdministratorNote',
    'keyup', ${noteConstants.MAXIMUM_LENGTH}, true, onAdministratorNoteTooLongSuccess, onAdministratorNoteTooLongFailure,
    ['noalert']);
  pageValidator.registerMaximumLength('currentAdministratorNote',
    'blur', ${noteConstants.MAXIMUM_LENGTH}, true, onAdministratorNoteTooLongSuccess, onAdministratorNoteTooLongFailure,
    ['alert']);
  pageValidator.registerAllowedCharacters('currentAdministratorNote',
    'keyup', PageValidator.WEB_MULTILINE_CHARACTER_REGEXP, onAdministratorNoteInvalidCharacterSuccess,
    onAdministratorNoteInvalidCharacterFailure, ['noalert']);
  pageValidator.registerAllowedCharacters('currentAdministratorNote',
    'blur', PageValidator.WEB_MULTILINE_CHARACTER_REGEXP, onAdministratorNoteInvalidCharacterSuccess,
    onAdministratorNoteInvalidCharacterFailure, ['alert']);
});

</script>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

        <table class="box" border="0" cellpadding="0" cellspacing="0" width="734">
          <tr>

            <td class="tableheadTD1">
            <c:if test="${displayRules.displayExpandCollapseButton}">
              <img id="notesSectionExpandCollapseIcon" src="/assets/unmanaged/images/minus_icon.gif" width="13" height="13" style="cursor:hand; cursor:pointer">&nbsp;
            </c:if>
            <b>Notes</b></td>
            <td width="141" class="tablehead">&nbsp;</td>
          </tr>
        </table>
        <div id="notesSection">
          <table class="box" border="0" cellpadding="0" cellspacing="0" width="734">
            <tr>
              <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

              <td width="732"><table border="0" cellpadding="0" cellspacing="0" width="732">
                <tr valign="top">
                    <td width="732" class="datacell1">
                      <img id="dynamicErrorIcon_${loanFields.CURRENT_PARTICIPANT_NOTE.fieldName}"
                           src="/assets/unmanaged/images/error.gif"
                           style="display:none"/>
                      <ps:fieldHilight styleIdSuffix="${loanFields.CURRENT_PARTICIPANT_NOTE.fieldName}"
                                       name="${loanFields.CURRENT_PARTICIPANT_NOTE.fieldName}"
                                       singleDisplay="true"/>
                      <strong>${displayRules.participantLabelText} </strong>
                    </td>
                </tr>
                <tr valign="top">
                    <td class="datacell1">
                      <ps:trackChanges escape="true" property="currentParticipantNote" name="loanForm"/>
<form:textarea path="currentParticipantNote" cols="80" rows="4" id="currentParticipantNote"/>


			  </td>
                </tr>
                  
                <tr valign="top">

                    <td class="datacell1">
                      <img id="dynamicErrorIcon_${loanFields.CURRENT_ADMINISTRATOR_NOTE.fieldName}"
                           src="/assets/unmanaged/images/error.gif"
                           style="display:none"/>
                      <ps:fieldHilight styleIdSuffix="${loanFields.CURRENT_ADMINISTRATOR_NOTE.fieldName}"
                                       name="${loanFields.CURRENT_ADMINISTRATOR_NOTE.fieldName}"
                                       singleDisplay="true"/>
                      <strong>For administrator(s) only (max. 750 characters) </strong>
                    </td>
                </tr>
                  <tr valign="top">
                    <td class="datacell1">
                      <ps:trackChanges escape="true" property="currentAdministratorNote" name="loanForm"/>
<form:textarea path="currentAdministratorNote" cols="80" rows="4" id="currentAdministratorNote"/>


			  </td>
                </tr>
                  
              </table></td>
              <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </tr>

            <tr>
              <td colspan="3" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </tr>
          </table>
        </div>
