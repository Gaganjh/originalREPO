<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_LICENSE_VERIFICATION_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="licenseSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_LICENSE_VERIFICATION_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="licenseText" />

<c:set var="form" value="${requestScope.myprofileLicenseForm}" scope="page"/>
<utils:cancelProtection name="myprofileLicenseForm" changed="${form.changed}"
     exclusion="['action']"/>

<div id="contentFull">
	<layout:pageHeader nameStyle="h1"/>

<c:if test="${form.success}">
	<utils:info contentId="<%=BDContentConstants.MY_PROFILE_SAVE_SUCCESS_MESSAGE_TEXT%>"/> 
</c:if>

<report:formatMessages scope="request"/>

<userprofile:myprofileTab/>

<bd:form action="/do/myprofile/license" modelAttribute="myprofileLicenseForm" name="myprofileLicenseForm">

<input type="hidden" name="action"/>

<div class="inputTextFull">
	<p> <content:getAttribute attribute="text" beanName="licenseText"/> </p>
	<label>
<form:radiobutton path="producerLicense" id="yes" value="true"/>
		Yes
	</label>
    <label>
<form:radiobutton path="producerLicense" id="no" value="false"/>
	    No 
    </label>
</div>

   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="save" value="Save"
	        onclick="return doProtectedSubmitBtn(document.myprofileLicenseForm, 'save', this)">
        </div> 

    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Close"
             onclick="return doCancelBtn(document.myprofileLicenseForm, this)"> 
    </div>
</bd:form>
</div>

<layout:pageFooter/>
