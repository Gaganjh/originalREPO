<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<content:contentBean contentId="<%=BDContentConstants.USERMANAGEMENT_PROFILE_SECTION_TITLE%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>"
  id="webProfile" />

<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PERSONAL_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="personalInfo" />

<content:contentBean contentId="<%=BDContentConstants.DELETE_ASSISTANT_WARNING%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>"
  id="deleteWarning" />

<script type="text/javascript">
<!--
    var deleteWarning = '<content:getAttribute beanName="deleteWarning" attribute="text" filter="true"/>';

    function doDelete(btn) {
		if (confirm(deleteWarning)) {        
    	  return doProtectedSubmitBtn(document.manageAssistantForm, 'delete', btn);
		} else {
		  return false;
		}
    }
    
	function selectBroker() {
		var linkObj = document.getElementById('brokerLink');
		doProtectedSubmit(document.manageAssistantForm, 'selectBroker', linkObj);
	}

//-->
</script>


<utils:cancelProtection name="manageAssistantForm" changed="${manageAssistantForm.changed}"
     exclusion="['action']"/>

<bd:form action="/do/manage/assistant" modelAttribute="manageAssistantForm" name="manageAssistantForm">
	
<input type="hidden" name="action"/>

<div id="contentFull">
<layout:pageHeader nameStyle="h1"/>
<c:if test="${manageAssistantForm.resendActivationSuccess}">
 <utils:info contentId="<%=BDContentConstants.RESEND_INVITE_ASSISTANT_SUCCESS%>"/> 
</c:if>

<report:formatMessages scope="session"/>


	<div class="BottomBorder">
	   <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="webProfile"/></div>   
	</div>
	<div class="label">Role:</div>
	<div class="inputText"><%=BDUserRoleDisplayNameUtil.getInstance().getDisplayName(BDUserRoleType.FinancialRepAssistant) %></div> 
	
    <div class="label">Profile Status:</div>
    <div class="inputText">
     ${manageAssistantForm.profileStatus}
    </div> 
	<br/>		
	<div class="BottomBorder">
      <div class="SubTitle Gold Left"><div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="personalInfo"/></div>
     </div>
    </div>
    <c:if test="${not empty manageAssistantForm.brokerDealerFirmNames}">
    <div class="label">BD Firm Name:</div>
    <div class="inputText">
     <label>
      <c:forEach var="firmName" items="${manageAssistantForm.brokerDealerFirmNames}" varStatus="loopStatus">
         ${firmName} 
         <c:if test="${not loopStatus.last}"> 
           <br/>
         </c:if>
      </c:forEach>     
      &nbsp;
     </label>
    </div>
    </c:if>
    <div class="label">First Name:</div>
    <div class="inputText">
     <label>
      ${manageAssistantForm.assistantProfile.firstName} &nbsp;
     </label>
    </div> 
    <div class="label">Last Name:</div>
      
    <div class="inputText">
      <label>
         ${manageAssistantForm.assistantProfile.lastName} &nbsp;
      </label>
    </div>
    
    <div class="label">Email:</div>
		<div class="inputText">
		  <label>
		   ${manageAssistantForm.assistantProfile.emailAddress}&nbsp;
		  </label>
	</div>
     	
    <div class="label">Financial Representative:</div>
    <div class="inputText">
     <label>
     <a id="brokerLink" href="javascript:selectBroker()">${manageAssistantForm.assistantProfile.parentBroker.firstName} ${manageAssistantForm.assistantProfile.parentBroker.lastName}</a>
     </label>
    </div> 

	<br class="clearFloat"/>
   <div class="formButtons">	
     <c:if test="${manageAssistantForm.resendInvitationAllowed}">
         	<c:choose>
        		<c:when test="${manageAssistantForm.enableResend}">
				   	<div class="formButton"> 
			        <input type="button" class="blue-btn-medium next" 
						onmouseover="this.className +=' btn-hover'" 
				        onmouseout="this.className='blue-btn-medium next'"
				        name="resend" value="Resend Invitation"
				        onclick="return doProtectedSubmitBtn(document.manageAssistantForm,'resendActivation', this)">
			        </div> 
		        </c:when>
		        <c:otherwise>
					    <div class="formButton">
					      <input type="button" class="disabled-grey-btn next" 
					             name="resend" value="Resend Invitation"
					             disabled="disabled">
					    </div> 
		        </c:otherwise>
		    </c:choose>
	  </c:if>
	  <c:if test="${manageAssistantForm.deleteAllowed}">
		   	<div class="formButton"> 
	        <input type="button" class="blue-btn next" 
				onmouseover="this.className +=' btn-hover'" 
		        onmouseout="this.className='blue-btn next'"
		        name="delete" value="Delete User"
		        onclick="return doDelete(this)">
	        </div> 
	  </c:if>
	    <div class="formButton">
	      <input type="button" class="grey-btn back" 
	             onmouseover="this.className +=' btn-hover'" 
	             onmouseout="this.className='grey-btn back'"
	             name="cancel" value="Cancel"
	             onclick="return doCancelBtn(document.manageAssistantForm, this)"> 
	    </div>
  
    </div>
  </div>
</bd:form>

<layout:pageFooter/>
