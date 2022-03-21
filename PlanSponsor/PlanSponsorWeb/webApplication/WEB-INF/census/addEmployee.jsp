<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style type="text/css">
DIV.scroll {
	OVERFLOW: auto;
	WIDTH: auto;
	BORDER-TOP-STYLE: none;
	BORDER-RIGHT-STYLE: none;
	BORDER-LEFT-STYLE: none;
	HEIGHT: 115px;
	BACKGROUND-COLOR: #fff;
	BORDER-BOTTOM-STYLE: none;
	padding: 8px;
	visibility: visible;
}
</style>


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

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

  
<jsp:include flush="true" page="employeeSnapshotErrorMessages.jsp"/>
  
<script type="text/javascript">


function doSave(actionVal) {
	var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    } 
	return doSubmit(actionVal);	
}

<!--

function toggleSection(sectionId, statusId, indId, icon) {
	toggleFormSection('addEmployeeForm', sectionId, statusId, indId, icon);
}

function doSubmit(action) {
	doFormSubmit('addEmployeeForm', action);
}

function isFormChanged() {
		return changeTracker.hasChanged();
}

function expandAll(expand) {
    expandFormAllSection('addEmployeeForm', expand);
}

registerTrackChangesFunction(isFormChanged);

if (window.addEventListener) {
	window.addEventListener('load', protectLinksFromCancel, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', protectLinksFromCancel);
}

//-->
</script>
       <content:errors scope="request"/>
		<table cellSpacing=0 cellPadding=0 width=760 border=0>
		  <tbody>
		  <tr>
		    <td>
			<ps:form method="post" action="/do/census/addEmployee/" modelAttribute="addEmployeeForm" name="addEmployeeForm">

					

<input type="hidden" name="action"/>
<form:hidden path="source"/>
<form:hidden path="fromView"/>
<input type="hidden" name="acceptSimilarSsn"/>
			   <input type="hidden" name="submitted" value="true"/>	   
			  <p>
				<table border="0" cellpadding="0" cellspacing="0" width="500">
				  <tbody>
				  <tr>
				    <td width="30" valign="top">
				        <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
				    </td>
				    <td width="609" valign="top">
				      <jsp:include flush="true" page="employeeSnapshotErrors.jsp">
				         <jsp:param name="readOnly" value="${addEmployeeForm.readOnly}"/>
				      </jsp:include>
				      <jsp:include flush="true" page="similarSSNWarnings.jsp"/>
					  <jsp:include flush="true" page="addBasicSection.jsp"/>
				      <br><br>
					  <jsp:include flush="true" page="addEmploymentSection.jsp"/>
				      <br><br>
					  <jsp:include flush="true" page="addContactSection.jsp"/>
				      <br><br>
					  <jsp:include flush="true" page="addParticipationSection.jsp"/>
					</td>
				  </tr>
				 </tbody>
				</table>
				
			    <br>
			    <c:if test="${empty ssnWarnings}">
				<table width="615">	
				  <tbody>
				   <tr>
				    <td><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="30"></td>
				    <td>
				      <table border="0" cellpadding="0" cellspacing="0" width="494">
				        <tbody>
				           <c:if test="${not addEmployeeForm.readOnly}">
					          <tr valign="top">
						          <td width="144">
						              <div align="left">
						                <input type="button" name="button1" value="cancel" class="button134" 
						                onclick="return doConfirmAndSubmit('cancel')"/>          
						              </div>
						          </td>
						          <td width="144">	            
						              <div align="center">
						                <input type="button" name="button2" value="reset" class="button134" 
						                onclick="return doConfirmAndSubmit('reset')"/>          
						                <br>
						              </div>
						          </td>	          
						          <td width="140">
						            <div align="right">
						              <input type="button" name="button3" value="save" class="button134" onclick="return doSave('save')"/>          
						            </div>
						          </td>
							  </tr>
						  </c:if>
				        </tbody>
				       </table>
				     </td>
				    </tr>
				 </tbody>
				</table>
				</c:if>
			   </ps:form>
		    </td>
		  </tr>
		 </tbody>
		</table>
		<!-- footer table -->
		<BR>
		<table height=25 cellSpacing=0 cellPadding=0 width=760 border=0>
		 <tr>
			  <td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
			              <tr>
						    <td width="30" valign="top">
						        <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
						    </td>
							<td>
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
 
<script type="text/javascript" src="/assets/unmanaged/javascript/oldstyle_tooltip.js"></script>
