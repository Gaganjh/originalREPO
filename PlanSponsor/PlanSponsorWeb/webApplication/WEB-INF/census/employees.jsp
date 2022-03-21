<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.role.RelationshipManager" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>




<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PART_DEFERRAL_MENU_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="PartDeferralLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PART_DEFERRAL_MENU_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="PartDeferralText"/>

<content:contentBean contentId="<%=ContentConstants.DEFERRAL_DOWNLOAD_NOT_ALLOWED%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="DeferralDownloadNotAllowedText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PART_ENROLL_SUM_MENU_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="PartEnrollSumLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PART_ENROLL_SUM_MENU_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="PartEnrollSumText"/>

<content:contentBean contentId="<%=ContentConstants.ENROLLMENT_SUMMARY_DOWNLOAD_NOT_ALLOWED%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="EnrollmentSummaryDownloadNotAllowedText"/>






    <td width="4%" valign="top">
	<img src="/assets/unmanaged/images/body_corner.gif" width="50%" height="8"><br>
	<img src="/assets/unmanaged/images/s.gif" width="100%" height="1">
    </td>
    <td width="82%" valign="top">

    <table width="100%" border="0" valign="top">
        <tr>
	        <td width="82%" ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          	<td width="3%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
          	<td width="15%" ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        </tr>
        <tr>
          <td width="82%" valign="top">
		  <img src="/assets/unmanaged/images/s.gif" width="82%" height="23">
		  <img src="/assets/unmanaged/images/s.gif" width="3%" height="1">
		  <!--page title-->
		  <img src="<content:pageImage type="pageTitle" beanName="layoutPageBean" path="/assets/pagetitleimages/employees_ttl.gif"/>" alt="employees" width="345" height="34">

		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <!--deleted 1-->
              <tr>
				<td width="1%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				<td width="99%" valign="top">
					<!--Layout/Intro1-->
					<content:pageIntro beanName="layoutPageBean"/>
         			<br>
                 	<br>
                  	<!--Layout/intro2-->
                  	<content:getAttribute beanName="layoutPageBean" attribute="introduction2">
                  	<content:param>#</content:param>
                  	</content:getAttribute>
                    <br><br>
                <table width="100%" border="0">
				  <tr>
				    <td width="2%" valign="top">
				    	<img src="/assets/unmanaged/images/s.gif" width="12" height="2">
					</td>
				    <td width="25%" valign="top">
				      	<content:getAttribute beanName="layoutPageBean" attribute="body1Header">
				      		<content:param>../censusSummary/</content:param>
				      	</content:getAttribute>
				    </td>
					<td width="4%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				    <td valign="top">
                        <!--Layout/body1-->
				      	<content:pageBody beanName="layoutPageBean" />
                    </td>
				  </tr>
				  <tr>
					<td colspan="4" height="20"></td>
				  </tr>
		    	</table>
			   	</td>
			  </tr>
			  <tr>
				<td width="1%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
				<td width="99%">
				<br>
					<p><content:pageFooter beanName="layoutPageBean"/></p>
					<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 					<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 				</td>
 			  </tr>
		  </table>
        </td>
        <td width="3%"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        <td width="15%" ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
     </tr>
 	</table>

</td>
<td valign="top">
	<img src="/assets/unmanaged/images/s.gif"  height="59">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="box">
    	<tr>
        	<td ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
            <td ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
            <td ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        </tr>
    </table>
	<img src="/assets/unmanaged/images/s.gif" height="20">
</td>
