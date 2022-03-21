<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCEnvironment"%>
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>

<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>

<%-- Constants --%>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants"/>
<un:useConstants var="constants" className="com.manulife.pension.ps.web.Constants"/>


<c:set var="userProfile" value="${constants.USERPROFILE_KEY}" scope="session"/>
<content:contentBean contentId="${contentConstants.WARNING_DISCARD_CHANGES}"
                     type="${contentConstants.TYPE_MESSAGE}"
                     id="warningDiscardChanges"/>
<c:set var="user" value="${sessionScope[userProfile]}" />                     

<c:if test="${param.printFriendly}">
  <table border="0" width="760" cellspacing="0" cellpadding="0">
    <tr>
      <td width="400" class="datacell1">
        <c:choose>
          <c:when test="${applicationScope[constants.ENVIRONMENT_KEY].siteLocation == constants.SITEMODE_USA}">
            <img src="/assets/unmanaged/images/JH_blue_resized.gif" border="0">
          </c:when>
          <c:otherwise>
            <img src="/assets/unmanaged/images/JH_blue_resized_NY.gif" border="0">
          </c:otherwise>
        </c:choose>
      </td>
      <td width="100%" class="datacell1" colspan="2">&nbsp;</td>
    </tr>
  </table>
  <br/>
</c:if>
<c:if test="${! param.printFriendly}">
  <table border="0" width="760" cellspacing="0" cellpadding="0">
    <tr>
      <c:choose>
        <c:when test="${applicationScope[constants.ENVIRONMENT_KEY].siteLocation == constants.SITEMODE_USA}">
          <content:contentBean contentId="${contentConstants.COMPANY_LOGO}"
                               type="${contentConstants.TYPE_MISCELLANEOUS}"
                               id="companyLogo"/>
          <td class="logo" width="459"><a href="/do/home/ChangeContract/"><img src='<content:getAttribute id="companyLogo" attribute="image.path" />' border="0"></a></td>        
        </c:when>
        <c:otherwise>
          <content:contentBean contentId="${contentConstants.COMPANY_LOGO_NY}"
                               type="${contentConstants.TYPE_MISCELLANEOUS}"
                               id="companyLogoNY"/>
          <td class="logo" width="459"><a href="/do/home/ChangeContract/"><img src='<content:getAttribute id="companyLogoNY" attribute="image.path"/>' border="0"></a></td>                               
        </c:otherwise>
      </c:choose>
      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="75"></td>
      <td rowspan="2" width="300" class="welcome"><div class="signOutLayerExternal">&nbsp;
		      <a class="signOutLayerStyle2" href="#" onMouseOver='self.status="Sign out"; return true' onclick="javascript:doSignOut('<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>');return false;">Sign out</a>
	          <span class="signOutLayerStyle1"> | </span>
		       <a href="/do/profiles/editMyProfile/?loginFlow=Y" class="signOutLayerStyle2">Edit my profile</a>       
	       </div>
      </td>
    </tr>
    <tr>
      <td class="welcome"><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
        <c:if test="${layoutBean.showSelectContractLink}">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;
          <a href="/do/home/ChangeContract/"><span class="welcome"><u>Select contract</u></span></a>
        </c:if>
      </td>
      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
    </tr>
  </table>
  <table width="760" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td class="n1" width="30" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
        <img src="/assets/unmanaged/images/s.gif" width="30" height="17"></td>
      <td class="n1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <td class="n1" width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
      <td class="n1" align="right"><a href="/do/home/ChangeContract/">Home</a>
      <ps:isTpaum name ="user" property ="principal.role" >      
      | <a href="/do/profiles/manageUsers/">Manage profiles</a>
      </ps:isTpaum>        
        <% if (MCEnvironment.isMessageCenterAvailable(request)) { %>        
        <ps:isNotJhtc  name="${constants.USERPROFILE_KEY}" property="role" >| <a href="<%=MCConstants.DispatchUrl%>">Message center</a></ps:isNotJhtc>
        <%} %>
        | <a href="/do/resources/ContactUsAction">Contact us</a>
      </td>
      <td class="n1" width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    </tr>
    <tr>
      <td class="n2"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
        <img src="/assets/unmanaged/images/s.gif" width="30" height="17">
      </td>
      <td class="n2" colspan="3"></td>
      <td class="n2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
    <tr>
      <td colspan="3" valign="top" align="left"><img src="/assets/unmanaged/images/s.gif" width="8" height="8" align="top"><br>
        <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
      </td>
    </tr>
  </table>
  <script language="javascript" type="text/javascript" src="/assets/unmanaged/javascript/nav.js"></script>  
  <jsp:include page="tpanavigationbar.jsp" flush="true" />
</c:if>
<!-- After navbar -->
<c:if test="${empty layoutBean.params['suppressTPAHeaderContent']}">
  <table width="${layoutBean.params['useFloatingSummary'] ? '530' : '760'}" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td valign="top" style="padding-left: 30px;" >
        <c:choose>
          <c:when test="${! empty layoutBean.params['titleImageFallback']}">
            <c:set var="pageTitleFallbackImage" value="${layoutBean.params['titleImageFallback']}"/>
            <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>" alt="${layoutBean.params['titleImageAltText']}">
          </c:when>
          <c:otherwise>
            <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
          </c:otherwise>
        </c:choose>
        <c:if test="${! param.printFriendly}">
          <table border="0" width="100%" cellspacing="0" cellpadding="0">
            <tr>
              <td style="padding-left: 10px;" valign="top">
                <!-- Layout/subheader -->
                <c:if test="${! empty layoutPageBean.subHeader}">
                  <strong><content:getAttribute beanName="layoutPageBean" attribute="subHeader" /></strong>
                </c:if>
                <!-- Layout/intro1 -->
                <c:if test="${! empty layoutPageBean.introduction1}">
                  <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
                </c:if>
                <!--Layout/Body3 (Added with TPA Rewrite) -->
                <c:choose>
                  <c:when test="${! layoutBean.showSelectContractLink}">
                    <!-- Show the body3 text for select/search contract page -->
                    <c:if test="${! empty layoutPageBean.body3}">
                      <content:getAttribute beanName="layoutPageBean" attribute="body3"/>
                    </c:if>
                  </c:when>
                  <c:otherwise>
                    <!--Layout/Intro2-->
                    <c:if test="${! empty layoutPageBean.introduction2}">
                      <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
                    </c:if>
                  </c:otherwise>
                </c:choose>
                <c:if test="${layoutBean.params['additionalIntroJsp']}">
                  <c:set var="additionalJSP" value="${layoutBean.params['additionalIntroJsp']}"/>
                  <jsp:include page="${additionalJSP}" flush="true"/>
                </c:if>
              </td>
              <td valign="top" align="right" style="padding-left: 30px;">
                <c:if test="${! layoutBean.params['suppressAdminGuide']}">
                  <content:howToLinks id="howToLinks" layoutBeanName="layoutPageBean"/> 
                  <content:contentBean contentId="${contentConstants.COMMON_HOWTO_SECTION_TITLE}"
                                       type="${contentConstants.TYPE_MISCELLANEOUS}"
                                       id="HowToTitle"/>
                  <%-- Start of How To --%>
                  <ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle"/>
                  <%-- End of How To --%>
                </c:if>
			          <%-- Check for parameter reportToolsPositionExtended. If true then align the report links fragment flush-right--%>
                <c:if test="${!layoutBean.params['reportToolsPositionExtended']}">
                  <td valign="top" align="right">
                    <c:if test="${! layoutBean.params['suppressReportTools']}">
                      <jsp:include page="standardreporttoolssection.jsp" flush="true"/>
                    </c:if>
                  </td>
                  <td valign="top" align="right">&nbsp;</td>
                </c:if>
                <c:if test="${layoutBean.params['reportToolsPositionExtended']}">
                  <td valign="top" align="right">&nbsp;</td>
                  <td valign="top" align="right">
                    <c:if test="${! layoutBean.params['suppressReportTools']}">
                      <jsp:include page="standardreporttoolssection.jsp" flush="true"/>
                    </c:if>
                  </td>
                </c:if>
              </td>
            </tr>
          </table>
        </c:if>       
        <c:if test="${layoutBean.params['includeHelpfulHint']}">
          <content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" />
          <content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer2" />
          <content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer3" />
        </c:if>
      </td>
    </tr>
  </table>
</c:if>