<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>

<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>


<content:contentBean contentId="<%=com.manulife.pension.bd.web.content.BDContentConstants.REGISTRATION_USERNAME_PASSWORD_SECTION_TITLE%>" type="<%=com.manulife.pension.bd.web.content.BDContentConstants.TYPE_MESSAGE%>" id="userNamePasswordSectionTitle" />
<content:contentBean contentId="<%=com.manulife.pension.bd.web.content.BDContentConstants.MIGRATION_USER_NAME_HELP%>" type="<%=com.manulife.pension.bd.web.content.BDContentConstants.TYPE_MISCELLANEOUS%>" id="userNameText" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_PREFERENCES_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="preferencesSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.NEW_PASSWORD_HELP_TEXT%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="nwPwdHelpText" />
<utils:cancelProtection name="migrationForm" changed="true"/>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
<bd:form action="do/migration/step3" cssClass="display:inline" modelAttribute="migrationNewProfileForm" name="migrationNewProfileForm">
<input type="hidden" name="action"/>
<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>

<report:formatMessages scope="request"/>

		<div class="BottomBorder">
		  <div class="SubTitle Gold Left">Change Your Web Profile Name</div>
		  <div class="GrayLT Right">* = Required Field</div>
		</div>
		<div class="regSection">
		  <div style="height:1px"> </div>    		
		
		<div class="label">* Profile First Name:</div>
		<div class="inputText">
<form:input path="firstName" maxlength="30" size="30" cssClass="input"/>
		</div>
		<div class="label">* Profile Last Name:</div>
		<div class="inputText">
<form:input path="lastName" maxlength="30" size="30" cssClass="input"/>
		</div>
		</div>

		<c:if test="${migrationNewProfileForm.userIdNeedChange || migrationNewProfileForm.passwordNeedChange}">
			<div class="BottomBorder">
				<div class="SubTitle Gold Left">
				     <content:getAttribute attribute="text" beanName="userNamePasswordSectionTitle"/>
				</div>
			</div>
			<div class="regSection">
			  <div style="height:1px"> </div>    		
				<div class="label" style="margin-top:5px">&nbsp;</div>
				<div class="inputText" style="margin-top:5px">
				   <content:getAttribute id='userNameText' attribute='text' /> &nbsp;
				</div>
				<div class="label" style="margin-top:5px">* Username:</div>
				<div class="inputText" style="margin-top:4px">
				  <label>
<form:input path="userCredential.userId" maxlength="20" cssClass="input"/>
				  </label>
				  <br />
				</div>
				<c:if test="${migrationNewProfileForm.passwordNeedChange}">	
					<div class="label" style="margin-top:5px">&nbsp;</div>
					<div class="inputText" style="margin-top:5px">
					   <content:getAttribute id='pwdHelpText' attribute='text' />&nbsp;
					</div>
				    <div class="label" style="margin-top:5px">* Password:</div>
					<div class="inputText" style="margin-top:5px">
					  <label>
<form:password path="userCredential.password" cssClass="input" maxlength="32"/>
					  </label>
					  <br />
					  Your password must contain at least 5 characters.</div>
					    <div class="label">* Confirm Password:</div>
					<div class="inputText">
					  <label>
<form:password path="userCredential.confirmedPassword" cssClass="input" maxlength="32"/>
					  </label>
					 <br />
					</div>
				</c:if>
			</div> <!-- regSection -->
		</c:if>
		<br class="clearLeft" />
		<userprofile:challenges form="${migrationNewProfileForm}"/>
		<br class="clearLeft" />  
		<userprofile:licenseVerification renderSection="yes"/>
		<br class="clearLeft" />  
		<div class="BottomBorder">
		<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="preferencesSectionTitle"/></div>
		</div>
		<div class="regSection">
        <div style="height:1px"> </div>    		
		<userprofile:sitePreferences/>
		<br class="clearFloat" />
		<userprofile:regMessageCenterPref/>
		</div>		
		<br class="clearFloat" />  
		<userprofile:terms termId="<%=BDContentConstants.MIGRATION_BROKER_TERMS_TEXT%>"/>

       <br class="clearFloat"/>
	    <div class="formButtons">
	   	<div class="formButton"> 
	       <input type="button" class="blue-btn next" 
				onmouseover="this.className +=' btn-hover'" 
		        onmouseout="this.className='blue-btn next'"
		        name="continue" value="Continue"
		        onclick="return doProtectedSubmitBtn(document.migrationNewProfileForm, 'continue', this)">
	    </div> 
	
	    <div class="formButton">
	      <input type="button" class="grey-btn back" 
	             onmouseover="this.className +=' btn-hover'" 
	             onmouseout="this.className='grey-btn back'"
	             name="cancel" value="Cancel"
	             onclick="return doCancelBtn(document.migrationNewProfileForm,  this)"> 
	    </div>
	    </div>
 </bd:form>  
<layout:pageFooter/>
</div>

