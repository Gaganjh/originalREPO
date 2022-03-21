<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.role.RelationshipManager" %>
<%@ page import ="java.lang.Boolean" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_EMP_SUMMARY_MENU_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="EmpSummaryLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_EMP_SUMMARY_MENU_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="EmpSummaryText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_EMP_VESTING_MENU_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="EmpVestingLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_EMP_VESTING_MENU_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="EmpVestingText"/>
                     
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_EMP_PAYROLL_SELF_SERVICE_MENU_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="EmpPayrollSelfServiceLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_EMP_PAYROLL_SELF_SERVICE_MENU_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="EmpPayrollSelfServiceText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_EMP_ELIGIB_MENU_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="EmpEligibLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_EMP_ELIGIB_MENU_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="EmpEligibText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PART_DEFERRAL_MENU_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="PartDeferralLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PART_DEFERRAL_MENU_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="PartDeferralText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PART_DEFERRAL_ACI_ON_MENU_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="PartDeferralACIOnText"/>

<content:contentBean contentId="<%=ContentConstants.DEFERRAL_DOWNLOAD_NOT_ALLOWED%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="DeferralDownloadNotAllowedText"/>

<content:contentBean contentId="<%=ContentConstants.DEFERRAL_DOWNLOAD_NOT_ALLOWED_ACI_ON%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="DeferralDownloadNotAllowedACIOnText"/>


<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


<c:set var="aciOn" value="${requestScope.aciOn}" />

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
                    
              <p><content:errors scope="request"/></p>
              
               <table width="100%" border="0">
               	  <!-- Summary -->
               	  <tr>
				    <td width="2%" valign="top">
				    	<img src="/assets/unmanaged/images/s.gif" width="12" height="2">
					</td>
				    <td width="25%" valign="top">
				      	<content:getAttribute id="EmpSummaryLinkText" attribute="text">
				      		<content:param>../census/censusSummary/</content:param>
				      	</content:getAttribute>
				    </td>
					<td width="4%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				    <td width="350" valign="top">
				      	<content:getAttribute id="EmpSummaryText" attribute="text"/>
                    </td>
				  </tr>
               	  <tr>
					<td colspan="4" height="20"></td>
				  </tr>
				  <!-- Adresses -->
				  <tr>
				    <td valign="top">
				    	<img src="/assets/unmanaged/images/s.gif" width="12" height="2">
					</td>
				    <td  valign="top">
				      	<content:getAttribute beanName="layoutPageBean" attribute="body2Header">
				      		<content:param>../participant/participantAddresses</content:param>
				      	</content:getAttribute>
				    </td>
					<td valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				    <td valign="top">
				    	<!--Layout/body2-->
				      	<content:getAttribute beanName="layoutPageBean" attribute="body2"/>
				    </td>
				  </tr>
               	  <tr>
					<td colspan="4" height="20"></td>
				  </tr>
				  <!-- Eligibility -->
               	  <tr>
				    <td width="2%" valign="top">
				    	<img src="/assets/unmanaged/images/s.gif" width="12" height="2">
					</td>
				    <td width="25%" valign="top">
				      	<content:getAttribute id="EmpEligibLinkText" attribute="text">
				      		<content:param>../census/employeeEnrollmentSummary</content:param>
				      	</content:getAttribute>
				    </td>
					<td width="4%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				    <td width="350" valign="top">
                        <content:getAttribute id="EmpEligibText" attribute="text"/>
                    </td>
				  </tr>
               	  <tr>
					<td colspan="4" height="20"></td>
				  </tr>

				  <!-- Deferrals -->
				  <c:if test="${allowedToAccessDeferralTab==true}">
				  <tr>
			    	<td valign="top">
			    		<img src="/assets/unmanaged/images/s.gif" width="12" height="2">
			  		</td>
			  		<td  valign="top">
			      		<content:getAttribute id="PartDeferralLinkText" attribute="text">
              				<content:param>../census/deferral</content:param>
						    </content:getAttribute>
					  </td>
			  		<td valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
			  		<td valign="top">

<% Boolean aciOn=(Boolean)pageContext.getAttribute("aciOn");
if (aciOn) { %>
<c:if test="${userProfile.reportDownloadAllowed == true}" >
					        <!-- user has report download permission - therefore has deferral download permission-->
							<content:getAttribute id="PartDeferralACIOnText" attribute="text"/>
</c:if>
<c:if test="${userProfile.reportDownloadAllowed == false}" >
							<!-- user does not have report download permission - therefore NO deferral download permission-->
							<content:getAttribute id="DeferralDownloadNotAllowedACIOnText" attribute="text"/>
</c:if>
<% } else { %>	  		
<c:if test="${userProfile.reportDownloadAllowed == true}" >
					        <!-- user has report download permission - therefore has deferral download permission-->
							<content:getAttribute id="PartDeferralText" attribute="text"/>
</c:if>
<c:if test="${userProfile.reportDownloadAllowed == false}" >
							<!-- user does not have report download permission - therefore NO deferral download permission-->
							<content:getAttribute id="DeferralDownloadNotAllowedText" attribute="text"/>
</c:if>
<% } %>						
			 		  </td>
				  </tr>	
			  	  <tr>
				  	<td colspan="4" height="20"></td>
				  </tr>
</c:if>

				  <!-- Vesting -->
				  <c:if test="${allowedToAccessVestingTab==true}">
               	  <tr>
				    <td width="2%" valign="top">
				    	<img src="/assets/unmanaged/images/s.gif" width="12" height="2">
					</td>
				    <td width="25%" valign="top">
				      	<content:getAttribute id="EmpVestingLinkText" attribute="text">
				      		<content:param>../census/censusVesting/</content:param>
				      	</content:getAttribute>
				    </td>
					<td width="4%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				    <td width="350" valign="top">
				      	<content:getAttribute id="EmpVestingText" attribute="text"/>
                    </td>
				  </tr>
               	  <tr>
					<td colspan="4" height="20"></td>
				  </tr>
</c:if>
				   <!-- Payroll Feedback manager -->
				     <c:if test="${allowedToAccessPayrollSelfServiceTab==true}">				
               	  <tr>
				    <td width="2%" valign="top">
				    	<img src="/assets/unmanaged/images/s.gif" width="12" height="2">
					</td>
				    <td width="25%" valign="top">
				      	<content:getAttribute id="EmpPayrollSelfServiceLinkText" attribute="text">
				      		<content:param>../participant/payrollSelfService</content:param>
				      	</content:getAttribute>
				    </td>
					<td width="4%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				    <td width="350" valign="top">
				      	<content:getAttribute id="EmpPayrollSelfServiceText" attribute="text"/>
                    </td>
				  </tr>
               	  <tr>
					<td colspan="4" height="20"></td>
				  </tr>
				  </c:if>
				  <!-- Assets -->
				  <tr>
				    <td width="2%" valign="top">
				    	<img src="/assets/unmanaged/images/s.gif" width="12" height="2">
					</td>
				    <td width="25%" valign="top">
				      	<content:getAttribute beanName="layoutPageBean" attribute="body1Header">
				      		<content:param>../participant/participantSummary</content:param>
				      	</content:getAttribute>
				    </td>
					<td width="4%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				    <td width="350" valign="top">
                        <!--Layout/body1-->
				      	<content:pageBody beanName="layoutPageBean" />
                    </td>
				  </tr>
				  <tr>
					<td colspan="4" height="20"></td>
				  </tr>
	
				  <!-- Statements -->
				  <tr>
				    <td valign="top">
				    	<img src="/assets/unmanaged/images/s.gif" width="12" height="2">
				  	</td>
				  	<td  valign="top">
				  		<content:getAttribute beanName="layoutPageBean" attribute="body3Header">
				      		<content:param>../participant/participantStatementSearch</content:param>
				      	</content:getAttribute>
					</td>
				  	<td valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				  	<td valign="top">
				  		<!--Layout/body3-->
				      	<content:getAttribute beanName="layoutPageBean" attribute="body3"/>
				  	</td>
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
