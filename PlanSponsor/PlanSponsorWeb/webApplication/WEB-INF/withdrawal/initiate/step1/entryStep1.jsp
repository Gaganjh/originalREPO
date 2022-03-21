<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="report" uri="manulife/tags/report" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="mrtl" uri="manulife/tags/mrtl" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="webConstants" className="com.manulife.pension.ps.web.Constants" />

<script type="text/javascript">function doOnload(){initStep1Page();}</script>
<script type="text/javascript">

var submitted = false;
function showPopupGuide() {
	var popupURL = new URL("/do/withdrawal/beforeProceedingGatewayInit/");
  popupURL.setParameter("action", "print");
	popupURL.setParameter("printFriendly", "true");
	window.open(popupURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
}
</script>
<ps:form modelAttribute="withdrawalForm" method="POST" action="/do/withdrawal/entryStep1/" name="withdrawalForm" >
<form:hidden path="action"  disabled="true"/>
<form:hidden path="withdrawalRequestUi.withdrawalRequest.ignoreWarnings" value="true" disabled="true"/>
<!-- <input type="hidden" name="stepOne" value="true"/>
 --><form:hidden path="dirty" />

<c:set scope="request" var="permissions"  value="${withdrawalPermissionsKey}"/>

<c:set var="statesWithoutMilitary" scope="request" value="${withdrawalForm.lookupData['USA_STATE_WITHOUT_MILITARY_TYPE']}"/>
<c:set var="loanTypes" scope="request" value="${withdrawalForm.lookupData['LOAN_OPTION_TYPE']}"/>
<c:set var="irsDistCodesLoansTypes" scope="request" value="${withdrawalForm.lookupData['IRS_CODE_LOAN']}"/>
<c:set var="withdrawalReasons" scope="request" value="${withdrawalForm.lookupData['ONLINE_WITHDRAWAL_REASONS']}"/>
<c:set var="hardshipTypes" scope="request" value="${withdrawalForm.lookupData['HARDSHIP_REASONS']}"/>
<c:set  scope="request" var="paymentToTypes" value="${withdrawalForm.lookupData['PAYMENT_TO_TYPE']}"/>



<!-- Bean Definition for CMA Content -->
<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_PERSONAL_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="Personal_Information"/>
<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_BASIC_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="Basic_Information"/>
<content:contentBean
  contentId="${contentConstants.LAST_PROCESSED_CONTRIBUTION_DATE_COMMENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="Last_Processed_Cont_Date_Text"/>
<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_LOAN_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="Loan_Information"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_APPROVAL_2_STEP_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="twoStepApprovalText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_APPROVAL_1_STEP_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="oneStepApprovalText"/>

<!-- CMA Content for Button Tool Tips -->
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_CANCEL_EXIT_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="cancelExitRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_SAVE_EXIT_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="saveExitRolloverText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_NEXT_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="nextRolloverText"/>

<script type="text/javascript" >
var isTpa = false;
<ps:isTpa name="${webConstants.USERPROFILE_KEY}" property="role">
  isTpa = true;
</ps:isTpa>


function doEmployeeSnapshot(empProfileId) {

  if (isTpa) {
    var baseUrl = "/do/census/tpaViewEmployeeSnapshot/";
  } else {
    var baseUrl = "/do/census/viewEmployeeSnapshot/";
  }
  
	var printUrl = baseUrl + "?profileId=" + empProfileId + "&printFriendly=true";
	window.open(printUrl,"","width=720,height=480,resizable,toolbar,scrollbars,");
}
</script>

<%-- Flag for any browser dependant rendering --%>
<c:choose>
  <c:when test='${fn:containsIgnoreCase(header["user-agent"], "MSIE")}'>
    <c:set var="isIE" value="true" scope="request"/>
  </c:when>
  <c:otherwise>
    <c:set var="isIE" value="false" scope="request"/>
  </c:otherwise>
</c:choose>

<%-- page definition --%>
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/disbursements.css" type="text/css">
<c:if test="${webConstants.WITHDRAWAL_INITIAL_MESSAGES_KEY != null}">
  <p>
    <div id="initialMessageDiv">
      <ps:initialMessages />
    </div>
  </p>
</c:if>
     
<form:hidden path="withdrawalRequestUi.employeeProfileId"/>
<table border="0" cellpadding="0" cellspacing="0" width="760">
   <tr>
      <td>
         <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
               <td>
                  <img src="/assets/unmanaged/images/s.gif" height="1" width="30">
                  <br>
                  <img src="/assets/unmanaged/images/s.gif" height="1">
               </td>
               <td width="500">
        <%-- Introduction Section --%>
        <jsp:include page="step1IntroductionSection.jsp"/>
        <%-- Participant Information Section --%>
        <jsp:include page="step1ParticipantInformationSection.jsp"/>
        <%-- Basic Information Section --%>
        <jsp:include page="step1BasicInformationSection.jsp"/>
        <%-- Loan Details Section --%>
        <c:if test="${withdrawalForm.withdrawalRequestUi.showLoanSection}">
          <jsp:include page="step1LoanSection.jsp"/>
        </c:if>
        <%-- Footer Section --%>
        <jsp:include page="step1FooterSection.jsp"/>
        <%-- Actions Section --%>
        <jsp:include page="step1Buttons.jsp"/>
      </td>
   </tr>
</table>
 </td>
   <td width="260">&nbsp;</td>
   <!-- end main content table --> 
  </tr>
      <tr>
        <td>&nbsp;</td>
        <td colspan="2">&nbsp;</td>
      </tr>
</table>

</ps:form>
<jsp:include flush="true" page="../../common/withdrawalCommonUtil.jsp"></jsp:include>
<jsp:include flush="true" page="step1Validations.jsp"></jsp:include>
<jsp:include flush="true" page="step1Updates.jsp"></jsp:include>
<jsp:include flush="true" page="step1Handlers.jsp"></jsp:include>
<%-- note to developers - this script tag MUST appear after all tooltips.
Best to put it before the closing body tag  - Shane Delorme Jun 14, 2006--%>
<script  type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript">

function initStep1Page() {

  expandAllSections();
  
  updateStep1Page();

  <%-- Register our dirty page query --%>
  registerTrackChangesFunction(isFormDirty);

  protectLinks();
}

  <%-- Checks if the robust date after vesting should be set --%>
  function checkRobustDateChangedAfterVesting() {
    
    <%-- Vesting may not have been set on step 1 --%>
    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.vestingCalledInd}">
      document.getElementById('robustDateChangedAfterVestingIndicatorId').value = 'true';
    </c:if>
  }
</script>
