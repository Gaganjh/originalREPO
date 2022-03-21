<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<c:set var="userProfile" value="${sessionScope.USER_KEY}" scope="session" />

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
<table class="box" border="0" cellpadding="0" cellspacing="0" width="660" >
  <tr>
    <td class="tableheadTD1" colspan="5">
      <div style="padding-top:5px;padding-bottom:5px">
        <b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b>
      </div>
    </td>
  </tr>
  <tr>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td>
   
      <table border="0" cellpadding="0" cellspacing="0" width="658">
        <tr class="datacell1" valign="top">
		      <td class="tablesubhead"><b>Item</b></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="tablesubhead"><b>Value on system of record&nbsp;&dagger;</b></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="tablesubhead" ><b>Value on original request</b></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="tablesubhead" ><b>Value on request</b></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="tablesubhead" ><b>Changed by</b></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="tablesubhead" ><b>Date / Time</b></td>
          </td>
        </tr>
        <c:if test="${fn:length(withdrawalActivityHistoryForm.activityHistory.activities) == 0}">
         <tr><td colspan="11">There are no records to display</td></tr>
        </c:if>
		<c:forEach items="${withdrawalActivityHistoryForm.activityHistory.activities}" var="row" varStatus="rowStatus">
		<tr class="${((rowStatus.count % 2) != 0 ) ? 'datacell1' : 'datacell2'}">
          <td style="padding:2px"><c:out value="${row.itemName}"/></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td style="padding:2px"><c:out value="${row.systemOfRecordValue}"/></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td style="padding:2px"><c:out value="${row.originalValue}"/></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td style="padding:2px"><c:out value="${row.currentValue}"/></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          
          	<c:choose>
	          	<c:when test="${row.showUserIdAndLastUpdated}">
			      <c:if test="${row.changedBy == loanConstants.USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME}">
					<ps:isInternalUser name="userProfile" property="role">
					  <td style="padding:2px" onmouseover="Tip('${row.internalUserName}')" onmouseout="UnTip()"><c:out value="${row.changedBy}"/></td>
					</ps:isInternalUser>
					<ps:isExternal name="userProfile" property="role">
					  <td style="padding:2px"><c:out value="${row.changedBy}"/></td>
					</ps:isExternal>
				  </c:if>
				  <c:if test="${row.changedBy != loanConstants.USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME}">
					<td style="padding:2px"><c:out value="${row.changedBy}"/></td>
				  </c:if>
	          	</c:when>
	          	<c:otherwise>
		          	<td style="padding:2px">n/a</td>
	          	</c:otherwise>
          	</c:choose>
          
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td style="padding:2px">
          	<c:choose>
	          	<c:when test="${row.showUserIdAndLastUpdated && !row.showLastUpdatedTimeAsNa}">
		          	<fmt:formatDate value="${row.lastUpdated}" type="both" pattern="${timeStampPattern}"/>
	          	</c:when>
	          	<c:otherwise>
		          	n/a
	          	</c:otherwise>
          	</c:choose>
          </td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
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
