<%@ page
  import="com.manulife.pension.ps.web.tools.CensusErrorCorrectionForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un"
  uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>

<un:useConstants var="content"
  className="com.manulife.pension.ps.web.content.ContentConstants" />

<script type="text/javascript">
<!--
function doConfirm(msg) {
  return confirm(msg);
}

function isFormChanged() {
  return changeTracker.hasChanged();
}
function discardChanges1(warning) {
	if (discardChanges(warning)) {
		document.getElementById("nextRecord").value="NextRecord";
	  }
	}

function gotoSubmissionDetailsPage(warning) {
  if (discardChanges(warning)) {
    parent.location.href='/do/tools/viewCensusDetails/';
  }
  return false;
}

registerTrackChangesFunction(isFormChanged);
registerWarningOnChangeToLinks(new Array("expandCollapseLink"));
//-->
</script>
<content:errors scope="session"/>
<ps:form method="post" action="/do/tools/uploadCensusErrorCorrection/"
  cssStyle="margin-bottom: 0pt;" modelAttribute="errorCorrectionForm" name="errorCorrectionForm">

  <c:set var="warningPage" value="${errorCorrectionForm.warningPage}" />

  <jsp:include flush="true" page="../census/employeeSnapshotErrorMessages.jsp" />
  <table border="0" cellpadding="0" cellspacing="0" width="700">
    <tbody>
      <tr>
        <td width="15"><img src="/assets/unmanaged/images/spacer.gif" border="0"
          height="1" width="30"></td>
        <td width="609" valign="top">
          <jsp:include flush="true" page="uploadCensusErrorCorrectionErrorBox.jsp" />

        <br/>
         <jsp:include page="uploadCensusErrorCorrectionErrorSummary.jsp" flush="true"/> 
        <br />

        <c:choose>
          <c:when test="${warningPage}">
            <table border="0" cellpadding="0" cellspacing="0"
              width="605">
              <tbody>
                <tr valign="top">
                  <td>
                  <div align="center">
<input type="submit" class="button134" name="action" value="Edit" /><%--  - property="actionLabel" --%>


                  </div>
                  </td>
                  <td>
                    <div align="center">
<input type="submit" class="button134" name="action" value="Confirm" /><%--  - property="actionLabel" --%></div>

                  </td>
                </tr>
              </tbody>
            </table>
          </c:when>
          <c:otherwise>
            <table border="0" cellpadding="0" cellspacing="0"
              width="605">
              <tbody>
                <tr valign="top">
                  <c:if test="${not errorCorrectionForm.submissionProcessed}">
	                  <td>
		                  <div align="center">
<input type="submit" class="button134" onclick="return doConfirm('Do you wish to remove this record permanently from the census file?')" name="action" value="Discard" /><%--  - property="actionLabel" --%>



		                  </div>
	                  </td>
                  </c:if>
                  <td>
<div align="center"><input type="button" onclick="return gotoSubmissionDetailsPage('The action you have selected will cause your changes to be lost. Select OK to continue or cancel to return.')" name="action" class="button134" value="Submission details"/>



                  </div>
                  </td>
                  <c:if test="${not errorCorrectionForm.submissionProcessed}">
	                  <td>
	                  	<div align="center">
<input type="submit" class="button134" name="action" value="Save" /><%--  - property="actionLabel" --%>


		                </div>
	                  </td>
	              </c:if>
                  <td>
<div align="center"><input type="submit" class="button134" id="nextRecord" onclick="return discardChanges1('The action you have selected will cause your changes to be lost. Select OK to continue or cancel to return.')" name="action" value="Next record (${errorCorrectionForm.numberOfErrorsInSubmission})" /><%--  - property="actionLabel" --%>



                  </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </c:otherwise>
        </c:choose></td>
      </tr>
    </tbody>
  </table>
</ps:form>

<!-- footer table -->
<br />
<table height=25 cellSpacing=0 cellPadding=0 width=760 border=0>
  <tr>
    <td>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <!--deleted 1-->
      <tr>
      </tr>
      <tr>
        <td width="1%"><img src="/assets/unmanaged/images/s.gif"
          height="1"></td>
        <td td width="99%"><br>
        <p><content:pageFooter beanName="layoutPageBean" /></p>
        <p class="footnote"><content:pageFootnotes
          beanName="layoutPageBean" /></p>
        <p class="disclaimer"><content:pageDisclaimer
          beanName="layoutPageBean" index="-1" /></p>
        </td>
      </tr>
    </table>
    </td>
    <td width="3%"><img src="/assets/unmanaged/images/s.gif"
      height="1"></td>
    <td width="15%"><img src="/assets/unmanaged/images/s.gif"
      height="1"></td>
  </tr>
</table>

