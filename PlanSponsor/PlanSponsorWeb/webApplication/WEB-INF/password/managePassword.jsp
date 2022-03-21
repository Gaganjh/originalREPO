<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ManagePasswordForm" %>
<%@ page import="com.manulife.pension.service.security.utility.SecurityConstants" %>

<%pageContext.setAttribute("LOCKED_PASSWORD_STATUS",SecurityConstants.LOCKED_PASSWORD_STATUS); %>
<%-- Define a user info object --%>


<c:set var="userInfo" value="${USERINFO_KEY}" />


<script language=javascript type='text/javascript'>
var submitted=false;

function doSubmit() {
	if (!submitted) {
		submitted = true;
		return true;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		return false;
	 }
}
</script>

        <table width="525" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td colspan="8">
				<content:errors scope="session"/>
			</td>
		  </tr>
          <tr>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="113"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="463"><img src="/assets/unmanaged/images/s.gif" width="250" height="1"></td>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="113"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
            <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </tr>

          <tr class="tablehead">
            <td class="tableheadTD1" colspan="8"> <b>Manage password </b><b></b></td>
          </tr>
          <tr class="datacell1">
            <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td colspan="6" align="center"><table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr class="datacell1">
                <td width="46%" valign="top" class="datacell1"><strong>First name</strong></td>
<td width="54%" valign="top" class="datacell1">${userInfo.firstName}</td>
              </tr>
              <tr class="datacell1">
                <td width="33%" valign="top" class="datacell1"><strong>Last name</strong></td>
<td valign="top" class="datacell1">${userInfo.lastName}</td>
              </tr>
              <tr class="datacell1">
                <td valign="top" class="datacell1"><strong>Primary Email</strong></td>
<td valign="top" class="datacell1">${userInfo.email}</td>
              </tr>
<c:if test="${not empty userInfo.secondaryEmail}">
              <tr class="datacell1">
                <td valign="top" class="datacell1"><strong>Secondary Email</strong></td>
<td valign="top" class="datacell1">${userInfo.secondaryEmail}</td>
              </tr>
</c:if>
              <tr class="datacell1">
                <td class="datacell1"><strong>Password status</strong></td>
                <td class="datacell1">
				  <ps:passwordState name="userInfo" property="passwordState"/>
                </td>
              </tr>
              <tr class="datacell1">
                <td valign="top" width="50%" class="datacell1"><strong>Password last changed</strong></td>
                <td valign="top" class="datacell1">
                  <c:if test="${not empty userInfo.passwordStateLastUpdatedTS }" >
	                  <render:date patternOut="<%= RenderConstants.EXTRA_LONG_MDY %>"
	                  			   property="userInfo.passwordStateLastUpdatedTS"
	                  			   defaultValue=""/>
				  </c:if>
				  <c:if test="${not empty userInfo.passwordStateLastUpdatedBy }" >
by: ${userInfo.passwordStateLastUpdatedBy}
<c:if test="${userInfo.passwordLastUpdatedByInternal ==true}">
					  at <ps:companyName/>
</c:if>
				  </c:if>

				</td>
              </tr>
              <tr class="datacell1">
                <td valign="top" width="50%" class="datacell1"><strong>Password last unlocked</strong></td>
                <td valign="top" class="datacell1">
                 <c:if test="${not empty userInfo.passwordLastUnlockedTS }" >
	                  <render:date patternOut="<%= RenderConstants.EXTRA_LONG_MDY %>"
	                  			   property="userInfo.passwordLastUnlockedTS"
	                  			   defaultValue=""/>
					   <c:if test="${not empty userInfo.passwordLastUnlockedBy }" >
by: ${userInfo.passwordLastUnlockedBy}
<c:if test="${userInfo.passwordLastUnlockedByInternal ==true}">
						  at <ps:companyName/>
</c:if>
					  </c:if>
				  </c:if>
				</td>
              </tr>
              <tr class="datacell1">
                <td valign="top" width="50%" class="datacell1"><strong>Number of failed sign in attempts</strong></td>
<td valign="top" class="datacell1">${userInfo.passwordFailedAttemptCount}</td>
              </tr>
              <ps:isNotInternalOrTpa name="userInfo" property="role">
              <tr class="datacell1">
                <td valign="top" class="datacell1"><strong>Number of failed attempts to answer challenge question</strong></td>
<td valign="top" class="datacell1">${userInfo.challengeQuestionFailedAttemptCount}</td>
              </tr>
              </ps:isNotInternalOrTpa>
              <tr class="datacell1">
                <td valign="top" width="50%" class="datacell1"><strong>Last security code sent</strong></td>
                <td valign="top" class="datacell1">
<c:choose>
	<c:when test="${userInfo.passcodeInfo.lastPasscodeEmailSent == null}">
									No security code has been sent
	</c:when>
	<c:otherwise>
	                  <render:date patternOut="<%= RenderConstants.LONG_TIMESTAMP_MDY_HR_MIN_SEC %>"
	                  			   property="userInfo.passcodeInfo.lastPasscodeEmailSent"
	                  			   defaultValue=""/>
	</c:otherwise>
</c:choose>
								</td>
              </tr>
              <tr class="datacell1">
                <td valign="top" class="datacell1"><strong>Number of sent security codes</strong></td>
                <td valign="top" class="datacell1"><c:out value="${userInfo.passcodeInfo.passcodeSentCount}"/></td>
              </tr>
              <tr class="datacell1">
                <td valign="top" class="datacell1"><strong>Number of invalid security code attempts</strong></td>
                <td valign="top" class="datacell1"><c:out value="${userInfo.passcodeInfo.invalidPasscodeCount}"/></td>
              </tr>
			  <tr class="datacell1">
			  <td valign="top" class="datacell1"><strong>Last security code delivery method</strong></td>
			   <td valign="top" class="datacell1">
			   <c:if test="${not empty userInfo.passcodeInfo.passcodeSentChannel}">${userInfo.passcodeInfo.passcodeSentChannel.descreption}</c:if>
			   </td>
			   </tr>
            </table></td>
            <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </tr>
          <tr class="whiteborder">
            <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
            <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td  colspan="2" rowspan="2" class="whiteborder"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
          </tr>
          <tr>
            <td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </tr>
        </table>

        <br>
        <table width="525" border="0" cellspacing="0" cellpadding="0">
          <tr align="right">
          <ps:form cssClass="margin-bottom:0;" name="managePasswordForm" modelAttribute="managePasswordForm"
                   method="POST"
                   action="/do/password/managePassword/">
            
<form:hidden path="userName" />	
<form:hidden path="firstName" />	
<form:hidden path="lastName" />	
<form:hidden path="email" />	
<input type="hidden" name="profileId" />
<input type="hidden" name="secondaryEmail" />

            <td>
<c:if test="${userInfo.passcodeInfo.passcodeLocked}">
<input type="submit" class="button150" onclick="return doSubmit();" name="action"  value="<%= ManagePasswordForm.BUTTON_LABEL_UNLOCK_PASSCODE %>" />


			  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</c:if>
<c:if test="${userInfo.passwordState == LOCKED_PASSWORD_STATUS }">
<input type="submit" class="button150" onclick="return doSubmit();" name="action" value="<%= ManagePasswordForm.BUTTON_LABEL_UNLOCK_PASSWORD %>" />


			  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</c:if>
<input type="submit" class="button150" onclick="return doSubmit();" name="action" value="<%= ManagePasswordForm.BUTTON_LABEL_RESET_PASSWORD %>" />


			</td>
          </ps:form>
          </tr>
        </table>



