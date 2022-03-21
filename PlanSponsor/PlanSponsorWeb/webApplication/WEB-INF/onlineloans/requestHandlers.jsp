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
  contentId="${loanContentConstants.LOAN_PACKAGE_DIALOG_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="loanPackageDialogText"/>

<script type="text/javascript">

<%-- Action Section --%>

  <%-- Handles save and exit being clicked --%>
  function submitWithoutConfirmation() {
  
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
    resetSubmitInProgress();
    return true;
  }  
  
  <%-- Handles cancel and exit being clicked --%>
  function confirmAndCancel() {
  
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  

    if (discardChanges('Are you sure?')) {
      parent.location.href='/do/withdrawal/loanAndWithdrawalRequestsInit/';
    } else {
      resetSubmitInProgress();
      return false;
    }
    
    return true;
  }
  
  <%-- Handles loan package link being clicked --%>
  function handleLoanPackageLinkClicked() {
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
    <%-- Check user confirmation --%>
    var confirmResponse = confirm('<content:getAttribute beanName="loanPackageDialogText" attribute="text" escapeJavaScript="true"/>');
    if (!confirmResponse) {
      resetSubmitInProgress();
    } 
    if (confirmResponse) {
      var form = document.getElementsByName("loanForm")[0];
      form["action"].value = "loanPackage";
      form.submit();
    }
    return confirmResponse;
  }  
  
  function submitWithConfirmation() {
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
    <%-- Check user confirmation --%>
    var confirmResponse = confirm('Are you sure?');
    if (!confirmResponse) {
      resetSubmitInProgress();
    }
    return confirmResponse;
  }  

</script>
