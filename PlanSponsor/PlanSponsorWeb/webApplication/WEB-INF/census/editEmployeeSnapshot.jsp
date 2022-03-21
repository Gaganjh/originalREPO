<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.EmployeeSnapshotForm" %>

<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="psw" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

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
<content:contentBean contentId="<%=ContentConstants.OBDS_LINK_IN_EMPLOYEE_SNAPSHOT_PAGE%>" 
					type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="beneficiaryInfoLink"/>

<%

	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

EmployeeSnapshotForm editEmployeeForm = (EmployeeSnapshotForm)session.getAttribute("employeeForm");
pageContext.setAttribute("editEmployeeForm",editEmployeeForm,PageContext.PAGE_SCOPE);
%>


<jsp:useBean id="securityProfile" class="com.manulife.pension.ps.web.census.util.EmployeeSnapshotSecurityProfile" scope="request">
	<jsp:setProperty name="securityProfile" property="userProfile" value="<%=userProfile %>"/>
</jsp:useBean>

<c:set var="moneyTypes" value="${editEmployeeForm.moneyTypes}" scope="request" />



<jsp:include flush="true" page="employeeSnapshotErrorMessages.jsp"/>

<script type="text/javascript">
<!--
function toggleSection(sectionId, statusId, indId, icon) {
	toggleFormSection('editEmployeeForm', sectionId, statusId, indId, icon);
}

function doSubmit(action) {
	doFormSubmit('editEmployeeForm', action);
}

function isFormChanged() {
	return changeTracker.hasChanged();
}

function expandAll(expand) {
    expandFormAllSection('editEmployeeForm', expand);
}

function openVestingInformation() {
window.location="/do/census/viewVestingInformation/?profileId=${e:forJavaScriptBlock(editEmployeeForm.profileId)}&source=<%=CensusConstants.EMPLOYEE_SNAPSHOT_PAGE%>";
    return true;
}

registerTrackChangesFunction(isFormChanged);

if (window.addEventListener) {
	window.addEventListener('load', protectLinksFromCancel, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', protectLinksFromCancel);
}
//-->
</script>        	
        <div id="errordivcs"><content:errors scope="session"/></div>

		<table cellSpacing=0 cellPadding=0 width=760 border=0>
		  <tbody>
		  <tr>
		    <td>
			<ps:form method="POST" modelAttribute="editEmployeeForm" name="editEmployeeForm" method="post" action="/do/census/editEmployeeSnapshot/" >

					

<input type="hidden" name="action"/>
<form:hidden path="source"/>
<form:hidden path="fromView"/>
<form:hidden path="profileId"/>
<input type="hidden" name="acceptSimilarSsn"/>
<input type="hidden" name="participant"/>
			   <input type="hidden" name="submitted" value="true"/>
			  <p>
			  	
				<table border="0" cellpadding="0" cellspacing="0" width="500">
				  <tbody>
				  <tr>
				    <td width="15"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="30"></td>
				    <td width="609" valign="top">
				      
				      <jsp:include flush="true" page="employeeSnapshotErrors.jsp">
				         <jsp:param name="readOnly" value="${editEmployeeForm.readOnly}"/>
				      </jsp:include>
				     <logicext:if name="editEmployeeForm"
								property="onlineBeneficiaryLinkDisplayed" op="equal" value="true">
						<logicext:and name="editEmployeeForm" property="participant"
									op="equal" value="true" />
								<logicext:then>
<a href="/do/census/beneficiary/editBeneficiaryInformation/?profileId=${e:forHtmlContent(editEmployeeForm.profileId)}">
									<content:getAttribute beanName="beneficiaryInfoLink"
										attribute="text" /> </a>
									<br />
									<br/>
						     </logicext:then>
					 </logicext:if>
				      <jsp:include flush="true" page="similarSSNWarnings.jsp"/>
					   <jsp:include flush="true" page="editBasicSection.jsp"/> 
				      <br><br>
					   <jsp:include flush="true" page="editEmploymentSection.jsp"/>  
					  <br><br>
					   <jsp:include flush="true" page="editContactSection.jsp"/>  
					  <br><br>
					   <jsp:include flush="true" page="editParticipationSection.jsp"/> 
<c:if test="${editEmployeeForm.showVesting ==true}">
					  <br><br>
					  
<c:set var="vestingCollected" value="${editEmployeeForm.vestingCollected}"/>
<c:set var="expandVesting" value="${editEmployeeForm.expandVesting}"/>
					  <jsp:include flush="true" page="viewVestingSection.jsp">
					  	<jsp:param name="expandVesting" value="${expandVesting}"/>
					  	<jsp:param name="vestingCollected" value="${vestingCollected}"/>  
					  </jsp:include>
					  
</c:if>
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
				           <c:if test="${not editEmployeeForm.readOnly}">
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
						              <input type="button" name="button3" value="save" class="button134" onclick="return doSubmit('save')"/>          
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
		<%-- footer table --%>
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
