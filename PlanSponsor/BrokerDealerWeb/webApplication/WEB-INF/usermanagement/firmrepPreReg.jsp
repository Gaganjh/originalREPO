<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@ taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<content:contentBean contentId="<%=BDContentConstants.USERMANAGEMENT_PROFILE_SECTION_TITLE%>" 
 type="<%=BDContentConstants.TYPE_MESSAGE%>"
 id="webProfile" />

<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PERSONAL_SECTION_TITLE%>" 
 type="<%=BDContentConstants.TYPE_MESSAGE%>"
 id="personalInfo" />
<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_SECTION2%>" 
     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="firmsSection" />

<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_SECTION3%>" 
     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="accessCodeSection" />

<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_ACCESS_CODE_HELP%>" 
     type="<%=BDContentConstants.TYPE_MESSAGE%>" id="accessCodeHelp" />

<c:set var="firms" value="${manageFirmRepForm.firms}"/>

<jsp:include page="firmrepCommon.jsp" flush="true"/>

<utils:cancelProtection name="manageFirmRepForm" changed="${manageFirmRepForm.changed}"
     exclusion="['action']"/>



<bd:form action="/do/manage/firmrep" modelAttribute="manageFirmRepForm" name="manageFirmRepForm">

<form:hidden path="action"/>
<form:hidden path="changed"/>
<form:hidden path="firmListStr"/>

<c:set var="readOnly" value="${not manageFirmRepForm.updateAllowed}"/>

<div id="contentFull">
<layout:pageHeader nameStyle="h1"/>
<c:if test="${manageFirmRepForm.resendActivationSuccess}">
  <utils:info contentId="<%=BDContentConstants.RESNED_INVITE_FIRMREP_SUCCESS %>"/>
</c:if>

<report:formatMessages scope="request"/>

	<div class="BottomBorder">
	   <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="webProfile"/></div>
       <div class="GrayLT Right">* = Required Field</div>   
	</div>
	<div class="label">Role:</div>
	<div class="inputText"><%=BDUserRoleDisplayNameUtil.getInstance().getDisplayName(BDUserRoleType.FirmRep) %></div> 
	
    <div class="label">Profile Status:</div>
    <div class="inputText">
     ${manageFirmRepForm.profileStatus}
    </div> 
	<br/>		
	<div class="BottomBorder">
      <div class="SubTitle Gold Left"><div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="personalInfo"/></div>
     </div>
    </div>
    <div class="label">*First Name:</div>
    <div class="inputText">
     <label>
<form:input path="firstName" disabled="${readOnly}" maxlength="30" size="30" cssClass="input"/>
     </label>
    </div> 
    <div class="label">*Last Name:</div>
    <div class="inputText">
     <label>
<form:input path="lastName" disabled="${readOnly}" maxlength="30" size="30" cssClass="input"/>
     </label>
    </div> 
    <div class="label">* Email:</div>
   <div class="inputText">
    <label>
<form:input path="emailAddress" disabled="${readOnly}" maxlength="70" size="50" cssClass="input"/>
   	</label>
   </div>

   <div class="label">* Telephone #:</div>
   <div class="inputText">
          <userprofile:phoneNumInput readOnly="${readOnly}"/>
   </div>
   <div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="firmsSection" attribute="text"/></div>
   </div>

   <div class="label">* Firm Name:</div>
   <c:if test="${not readOnly}">
   <div style="margin-top:0px;float:left;margin-right:15px;margin-left:9px">
	   <utils:firmSearch firmName="selectedFirmName" firmId="selectedFirmId"/>
   </div>
   <div style="margin-top:15px">
	   <img src="/assets/unmanaged/images/buttons/add_firm.gif" alt="Add Firm Image" width="87" height="19" onclick="addFirm(document.manageFirmRepForm)" />
   </div>
   <br class="clearFloat" />
   
   </c:if>

   <div id="firms" class="inputText" style="width:435px;position:relative">
    <label>
        <c:forEach var="firm" items="${firms}" varStatus="loopStatus">
        			<div style='width:335px;float:left'>
        			  <div align='left'> ${loopStatus.index+1}. ${firm.firmName}</div>
        			</div>
		            <c:if test="${not readOnly}">
                    <div style="float:left">
                      <img src='/assets/unmanaged/images/buttons/remove_firm.gif' alt='Remove Firm Image' width='87' height='19' onclick='removeFirm("${firm.id}")'/> 
                    </div>
                    </c:if>
    		<script type="text/javascript">
			<!--
					addFirmToList(new bdFirm('${firm.id}', '${firm.firmName}'));
			//-->
			</script>
			<br />
			<br />
		</c:forEach>
	</label>
    </div>

   <div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="accessCodeSection" attribute="text"/></div>
   </div>
   <div class="label">* Access Code:</div>
   <div class="inputText">
    <label>
<form:input path="passCode" disabled="${readOnly}" maxlength="25" cssClass="input"/>
   	</label>
	<br />
	<content:getAttribute beanName="accessCodeHelp" attribute="text"/>
   </div>
    <div class="formButtons">
     <c:if test="${manageFirmRepForm.updateAllowed}">
 	        <c:choose>
    	   		<c:when test="${manageFirmRepForm.enableResend}">
    	   		     <div class="formButton"> 
			        <input type="button" class="blue-btn-long next" 
						onmouseover="this.className +=' btn-hover'" 
				        onmouseout="this.className='blue-btn-long next'"
				        name="resend" value="Save &amp; Resend Invitation"
				        onclick="populateFirms();return doProtectedSubmitBtn(document.manageFirmRepForm, 'resendActivation', this)">
			        </div> 
		        </c:when>
		        <c:otherwise>
				    <div class="formButton">
				      <input type="button" class="disabled-grey-btn-long next" 
				             name="resend" value="Save &amp; Resend Invitation"
				             disabled="disabled">
				    </div> 
		        </c:otherwise>
		    </c:choose>
   	  </c:if>
	  <c:if test="${manageFirmRepForm.deleteAllowed}">
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
	             onclick="return doCancelBtn(document.manageFirmRepForm, this)"> 
	    </div>
    </div>
  </div>
</bd:form>

<layout:pageFooter/>
