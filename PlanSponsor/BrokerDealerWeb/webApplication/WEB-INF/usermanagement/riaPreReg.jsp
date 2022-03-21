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
<content:contentBean contentId="<%=BDContentConstants.ASSOCIATED_RIA_FIRMS_TITLE%>" 
   type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>" id="riaFirmSectionTitle" />

<%-- <content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_SECTION3%>" 
     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="accessCodeSection" />

<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_ACCESS_CODE_HELP%>" 
     type="<%=BDContentConstants.TYPE_MESSAGE%>" id="accessCodeHelp" />--%>

<c:set var="firms" value="${manageRiaForm.firms}"/>

<jsp:include page="riaCommon.jsp" flush="true"/>

<utils:cancelProtection name="manageRiaForm" changed="${manageRiaForm.changed}"
     exclusion="['action','selectedFirmName']"/>



<bd:form action="/do/manage/ria" modelAttribute="manageRiaForm" name="manageRiaForm">

<form:hidden path="action"/>
<form:hidden path="changed"/>
<form:hidden path="firmListStr"/>
<form:hidden path="firmPermissionsListStr"/>

<c:set var="readOnly" value="${not manageRiaForm.updateAllowed}"/>

<div id="contentFull">
<layout:pageHeader nameStyle="h1"/>
<c:if test="${manageRiaForm.resendActivationSuccess}">
  <utils:info contentId="<%=BDContentConstants.RESNED_INVITE_FIRMREP_SUCCESS %>"/>
</c:if>

<report:formatMessages scope="request"/>

	<div class="BottomBorder">
	   <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="webProfile"/></div>
       <div class="GrayLT Right">* = Required Field</div>   
	</div>
	<div class="label">Role:</div>
	<div class="inputText"><%=BDUserRoleDisplayNameUtil.getInstance().getDisplayName(BDUserRoleType.RIAUser) %></div> 
	
    <div class="label">Profile Status:</div>
    <div class="inputText">
     ${manageRiaForm.profileStatus}
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
   <c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM') or (not empty manageRiaForm.firms)}">
   <div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="riaFirmSectionTitle" attribute="body2Header"/></div>
   </div>
 <c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM')}">
   <div class="label"><b>* RIA Firm ID/Name:</b></div>
    <c:if test="${not readOnly}"> 
   <div style="margin-top:0px;float:left;margin-right:15px;margin-left:9px">
	   <utils:riaFirmSearch firmName="selectedFirmName" firmId="selectedFirmId"/>
   </div>
   </c:if>
 </c:if>

   <div id="firms" class="inputText" style="width:435px;position:relative">
   
   		<table style='width: 100%'>
   		<c:if test="${not empty manageRiaForm.firms}">
					<tr>
						<th></th>
						<th style='width: 100%; font-size: 12px' valign='middle'><label>
								Firm</label></th>
						<th style='width: 20%; font-size: 12px' align='center'><label>View
								RIA Statements</label></th>
						<th style='width: 25%'></th>
					</tr>
		</c:if>

					<c:forEach var="firm" items="${firms}" varStatus="loopStatus">

						<%--  <div style='width: 335px; float: left'>
							<div align='left'>--%>
						<tr>
							<td style='font-weight: normal; font-size: 12px'>${loopStatus.index+1}.
							</td>
							<td style='font-weight: normal; font-size: 12px'>${firm.id} - ${firm.firmName}</td>
							<c:if test="${firm.firmPermission == true}">
								<td align='center'><input type="checkbox" checked='checked' style='margin-center:80px;' disabled/> </td>
							</c:if>
							<c:if test="${firm.firmPermission == false}">
								<td align='center'><input type="checkbox" style='margin-center:80px;' disabled/> </td>
							</c:if>
							<c:choose>
								<c:when test="${(userProfile.role.roleType.userRoleCode ne 'RUM')}">
									<%--  <c:if test="${not readOnly}">--%>
										<td>
											<img src='/assets/unmanaged/images/buttons/remove_firm.gif'
												alt='Remove Firm Image' width='87' height='19' 
												onclick=' '/>
										</td>
									<%--  </c:if>--%>
								</c:when>
								<c:otherwise>
									<%-- <c:if test="${not readOnly}"> --%>
										<td>
											<img src='/assets/unmanaged/images/buttons/remove_firm.gif'
													alt='Remove Firm Image' width='87' height='19' 
													onclick='removeFirm("${firm.id}")'/>
										</td>
									<%-- </c:if>--%>
								</c:otherwise>
							</c:choose>
						</tr>

						<script type="text/javascript">
							<!--
								addFirmToList(new bdFirm('${firm.id}', '${firm.firmName}', ${firm.firmPermission}));
								addActiveFirmIdsToList(new bdFirm('${firm.id}', '${firm.firmName}'));
							//-->
					   </script>
				 </c:forEach>
		</table>
 </div>
</c:if>
  <%---  <div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="accessCodeSection" attribute="text"/></div>
   </div>
   <div class="label">* Access Code:</div>
   <div class="inputText">
    <label>
    	<form:input cssClass="input" path="passCode" maxlength="25" disabled="${readOnly}"/>    	
   	</label>
	<br />
	<content:getAttribute beanName="accessCodeHelp" attribute="text"/>
   </div>--%>
    <div class="formButtons">
 <c:if test="${manageRiaForm.updateAllowed}">
 <c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM')}">
 	        <c:choose>
    	   		<c:when test="${manageRiaForm.enableResend}">
    	   		     <div class="formButton"> 
			        <input type="button" class="blue-btn-long next" 
						onmouseover="this.className +=' btn-hover'" 
				        onmouseout="this.className='blue-btn-long next'"
				        name="resend" value="Save &amp; Resend Invitation" id="saveMyProfileBtnForPre" >
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
  </c:if>
 <c:if test="${manageRiaForm.deleteAllowed}"> 
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
	             onclick="return doCancelBtn(document.manageRiaForm, this)"> 
	    </div>
    </div>
  </div>
</bd:form>

<layout:pageFooter/>
