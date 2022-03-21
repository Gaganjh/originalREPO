<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>

<%-- Constants --%>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants"/>
<un:useConstants var="constants" className="com.manulife.pension.ps.web.Constants"/>

<c:set var="userProfile" value="${constants.USERPROFILE_KEY}" scope="session"/>
<c:set var="showTpaHeader" value="false"/>
<c:set var="user" value="${sessionScope[userProfile]}" />

<c:if test="${layoutBean.tpaHeaderUsed}">
  <ps:isTpa name="${constants.USERPROFILE_KEY}" property="role">
     <c:if test="${empty user.currentContract}">
      <c:set var="showTpaHeader" value="true"/>
      <%-- Set a Withdrawals only flag to be tested in the TPA Header  --%>
      <c:set var="isWithdrawalsTpaHeader" value="true" scope="request"/>

      <jsp:include page="/WEB-INF/global/standardwithdrawaltpaheader.jsp" flush="true"/>
    </c:if>
  </ps:isTpa>
</c:if>

<c:if test="${! showTpaHeader}">
  <%-- Avoid duplication of header code --%>
  <jsp:include page="standardheader.jsp" flush="true"/> 

  <%-- If not in printer friendly mode, use a fixed width: Need to leave space for floating summary when on step 2 --%>
  <table width="${param.printFriendly ? '100%' : layoutBean.params['useFloatingSummary'] ? '530' : '760'}" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="30" valign="top">
        <c:if test="${! param.printFriendly}">
          <img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8" border="0"><br>
      	  <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
        </c:if>
      </td>
      <td valign="top">
        <table border="0" width="100%" cellspacing="0" cellpadding="0">
          <colgroup>
            <col style="padding-left:5px;padding-top:1px;"></col>
          </colgroup>
          <tr>
            <td valign="top" style="padding-top:20px;">
              <c:choose>
                <c:when test="${! empty layoutBean.params['titleImageFallback']}">
                  <c:set var="pageTitleFallbackImage" value="${layoutBean.params['titleImageFallback']}"/>
                  <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>" alt="${layoutBean.params['titleImageAltText']}">
                </c:when>
                <c:otherwise>
                  <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
                </c:otherwise>
              </c:choose>
              <br/>
              <c:if test="${! param.printFriendly}">
                <table border="0" width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
                    <td width="300" valign="top">
                      <!-- Layout/subheader -->
                      <c:if test="${! empty layoutPageBean.subHeader}">
                        <strong><content:getAttribute beanName="layoutPageBean" attribute="subHeader" /></strong>
                      </c:if>
                      <!-- Layout/intro1 -->
                      <c:if test="${! empty layoutPageBean.introduction1}">
                        <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
                      </c:if>
                      <!-- Layout/Intro2 -->
                      <c:if test="${! empty layoutPageBean.introduction2}">
                        <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
                      </c:if>
                      <c:if test="${layoutBean.params['additionalIntroJsp']}">
                        <c:set var="additionalJSP" value="${layoutBean.params['additionalIntroJsp']}"/>
                        <jsp:include page="${additionalJSP}" flush="true"/>
                      </c:if>
                    </td>
                    <td width="15"><img src="/assets/unmanaged/images/s.gif" width="3" height="1"></td>
					          <%-- Check for parameter reportToolsPositionExtended. If true then align the report links fragment flush-right--%>
                    <c:if test="${!layoutBean.params['reportToolsPositionExtended']}">
                      <td width="180" valign="top" align="right">
                        <c:if test="${! layoutBean.params['suppressReportTools']}">
                          <jsp:include page="standardreporttoolssection.jsp" flush="true"/>
                        </c:if>
                      </td>
                      <td valign="top" align="right">&nbsp;</td>
                    </c:if>
                    <c:if test="${layoutBean.params['reportToolsPositionExtended']}">
                      <td valign="top" align="right">&nbsp;</td>
                      <td width="180" valign="top" align="right">
                        <c:if test="${! layoutBean.params['suppressReportTools']}">
                          <jsp:include page="standardreporttoolssection.jsp" flush="true"/>
                        </c:if>
                      </td>
                    </c:if>
                  </tr>
                </table>
              </c:if>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</c:if>