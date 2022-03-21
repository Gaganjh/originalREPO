<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>


<utils:cancelProtection name="manageRiaForm" changed="${manageRiaForm.changed}"
     exclusion="['action','selectedFirmName']"/>

<content:contentBean contentId="<%=BDContentConstants.USERMANAGEMENT_PROFILE_SECTION_TITLE%>" 
 type="<%=BDContentConstants.TYPE_MESSAGE%>"
 id="webProfile" />
<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PERSONAL_SECTION_TITLE%>" 
 type="<%=BDContentConstants.TYPE_MESSAGE%>"
 id="personalInfo" />
<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_SECTION2%>" 
  type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="firmsSection" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_PREFERENCES_SECTION_TITLE%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>" 
  id="preferencesSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_LICENSE_VERIFICATION_SECTION_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MESSAGE%>" id="licenseSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.ASSOCIATED_RIA_FIRMS_TITLE%>" 
   type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>" id="riaFirmSectionTitle" />
   

<c:set var="readOnly" value="${not manageRiaForm.updateAllowed}"/>
<c:set var="firms" value="${manageRiaForm.firms}"/>

<jsp:include page="riaCommon.jsp" flush="true"/>


<bd:form action="/do/manage/ria" method="GET" modelAttribute="manageRiaForm" name="manageRiaForm">


<form:hidden path="action"/>
<form:hidden path="changed"/>
<form:hidden path="firmListStr"/>
<form:hidden path="firmPermissionsListStr"/>

<div id="contentFull">
<layout:pageHeader nameStyle="h1"/>
<c:if test="${manageRiaForm.updateSuccess}">
  <utils:info contentId="<%=BDContentConstants.UPDATE_FIRM_REP_SUCCESS %>"/>
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

    <div class="label">Password Status:</div>
    <div class="inputText">${manageRiaForm.passwordStatus}&nbsp;</div> 

	<br/>		

	<div class="BottomBorder">
      <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="personalInfo"/></div>
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
							<tr>
							<td style='font-weight:normal;font-size:12px'>${loopStatus.index+1}. </td>
							<td style='font-weight:normal;font-size:12px'>${firm.id} - ${firm.firmName}</td>
					<c:choose>
						<c:when test="${(userProfile.role.roleType.userRoleCode ne 'RUM')}">
							<c:if test="${firm.firmPermission == true}">
								<td align='center'><input type="checkbox" checked='checked' style='margin-center:80px;' disabled/> </td>
							</c:if>
							<c:if test="${firm.firmPermission == false}">
								<td align='center'><input type="checkbox" style='margin-center:80px;' disabled/> </td>
							</c:if>
								<td>
									<img src='/assets/unmanaged/images/buttons/remove_firm.gif'
										alt='Remove Firm Image' width='87' height='19' 
										onclick=' '/>
								</td>
						</c:when>
						<c:otherwise>
								<c:if test="${firm.firmPermission == true}">
									<td align='center'><input type="checkbox" checked='checked' style='margin-center:80px;' onclick='updateFirmPermission(this,"${firm.id}")'/> </td>
								</c:if>
								<c:if test="${firm.firmPermission == false}">
									<td align='center'><input type="checkbox" style='margin-center:80px;' onclick='updateFirmPermission(this,"${firm.id}")'/> </td>
								</c:if>
									<td>
										<img src='/assets/unmanaged/images/buttons/remove_firm.gif'
											alt='Remove Firm Image' width='87' height='19' 
											onclick='removeFirm("${firm.id}")'/>
									</td>
						</c:otherwise>
					</c:choose>
						</tr>
						
					 
			<script type="text/javascript">
			<!--
					addFirmToList(new bdFirm('${firm.id}', '${firm.firmName}', ${firm.firmPermission}));
			//-->
			</script>
					</c:forEach>
					</table>
    </div>
</c:if>
	<%-- <div class="BottomBorder"><div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="licenseSectionTitle"/></div>
    </div>
	<p> <content:getAttribute attribute="text" beanName="licenseText"/> </p>
	<div>
		<label>
		    <form:radiobutton styleId="yes" disabled="true" path="riaUserProfile.producerLicense" value="true"/>
			Yes
		</label>
	    <label>
			<form:radiobutton styleId="no" disabled="true" path="riaUserProfile.producerLicense" value="false"/>
		    No 
	    </label>
	   <br/>
	</div>
    
	<br/>
	<div class="BottomBorder">
	  <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="preferencesSectionTitle"/></div>
	</div>
	<div class="label">Default Fund Listing:<br />
	</div>
	<div class="inputText">
	  <label>
		  <form:radiobutton styleId="USA" disabled="${readOnly}" path="defaultSiteLocation" value="USA"/>
		  USA
	  </label>
	  <label>
		  <form:radiobutton styleId="NY" disabled="${readOnly}" path="defaultSiteLocation" value="NY"/>
		  New York
	  </label>
	   <br/>
	</div>--%>
    <br class="clearFloat"/>
    <div class="nextButton">
     <c:if test="${manageRiaForm.updateAllowed}">
     <c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM')}">
     <div class="formButton"> 
        <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="save" value="Save" id="saveMyProfileBtnForPost" >
      </div> 
      </c:if>
	  </c:if>
     <c:if test="${manageRiaForm.mimicAllowed}">
	   	<div class="formButton"> 
        <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="mimic" value="Advisor View"
	        onclick="return doMimic(this)">
        </div> 
	  </c:if>
	  	    <!--for Passcode exemption -->
	   <c:if test="${manageRiaForm.passcodeExemptionAllowed}">  
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="pcdExc" value="Passcode Exemption"
	        onclick="return doPasscodeExemption(this)">
        </div> 
	  </c:if>	
	  
	  <!--for Passcode -->
	  <c:if test="${manageRiaForm.passcodeViewAllowed}">  
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="resetPcd" value="Passcode View"
	        onclick="return doPasscodeView(this)">
        </div> 
	  </c:if>	
     <c:if test="${manageRiaForm.resetPasswordAllowed}">
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="resetPwd" value="Reset Password"
	        onclick="return doResetPassword(this)">
        </div> 
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
