<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
       <%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<c:set var="userProfile" value="${USER_KEY}" scope="session" /><%-- <c:set var="userProfile" value="${USER_KEY.}" /> --%>

<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="loanConstants" className="com.manulife.pension.service.loan.domain.LoanConstants" />
<c:set var="timeStampPattern" scope="request" value="${renderConstants.LONG_TIMESTAMP_MDY_SLASHED}"/>

<style>
.B { font-weight: bold; }
.BB { border-bottom: 1px solid #89A2B3; }
.BL { border-left : 1px solid #89A2B3; }
.BR { border-right : 1px solid #89A2B3; }
</style>
<div style="padding-left:10px">
<table class="box" border="0" cellpadding="0" cellspacing="0" width="660">
  <tr>
    <td class="tableheadTD1" colspan="5">
      <div style="padding-top:5px;padding-bottom:5px">
        <b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b>
      </div>
    </td>
  </tr>
  <tr>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td>
   
      <table border="0" cellpadding="0" cellspacing="0" width="658">
        <tr class="datacell1" valign="top">
		  <td class="tablesubhead"><b>User</b></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="tablesubhead"><b>Action taken</b></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="tablesubhead" ><b>Date / Time</b></td>
        </tr>
		<c:forEach items="${withdrawalActivityHistoryForm.activityHistory.summaries}" var="row" varStatus="rowStatus">
		<tr class="${((rowStatus.count % 2) != 0 ) ? 'datacell1' : 'datacell2'}"> 
		  <c:if test="${row.userName == loanConstants.USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME}">
			<ps:isInternalUser name="userProfile" property="role">
			  <td onmouseover="Tip('${row.internalUserName}')" onmouseout="UnTip()"><c:out value="${row.userName}"/></td>
			</ps:isInternalUser>
			<ps:isExternal name="userProfile" property="role">
			  <td><c:out value="${row.userName}"/></td>
			</ps:isExternal>
		  </c:if>
		  <c:if test="${row.userName != loanConstants.USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME}">
			  <td><c:out value="${row.userName}"/></td>
		  </c:if>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td><c:out value="${row.actionName}"/></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
         <td><fmt:formatDate value="${row.actionTimestamp}" type="both" pattern="${timeStampPattern}"/></td>
		</tr>
		</c:forEach>

      </table>
    </td>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  </tr>
  <tr>
    <td colspan="5">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
          <td rowspan="2" width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" height="5" width="5"></td>
        </tr>
        <tr>
          <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</div>
