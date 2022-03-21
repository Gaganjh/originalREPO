<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<content:contentBean contentId="<%=BDContentConstants.ADD_BOB_SECTION_TITLE%>" 
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="sectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.EXTERNAL_BROKER_EMAIL_TEXT%>" 
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="emailText" />
<content:contentBean contentId="<%=BDContentConstants.EXTERNAL_BROKER_CONTRACT_TEXT%>" 
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="contractText" />
<content:contentBean contentId="<%=BDContentConstants.CANCEL_POP_UP_MESSAGE%>" 
    type="<%=BDContentConstants.TYPE_MESSAGE%>" id="cancelMessage" />
<content:contentBean contentId="<%=BDContentConstants.MESSAGE_CENTER_PREF_SECTION_TITLE%>" 
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="mcPrefSectionTitle" />

<c:set var="form" value="${createBOBForm}" scope="page"/>
<utils:cancelProtection name="createBOBForm" changed="${form.changed}" exclusion="['action']"/>


<div id="contentFull">
	<layout:pageHeader nameStyle="h1"/>

<report:formatMessages scope="request"/>

	<userprofile:myprofileTab/>

	<p><content:getAttribute attribute="body1" beanName="layoutPageBean"/></p>
	<bd:form action="/do/myprofile/createBOB" modelAttribute="createBOBForm" name="createBOBForm">
	<form:hidden path="action"/>

	<div class="BottomBorder">
		<div class="SubTitle Gold Left">
		     <content:getAttribute attribute="text" beanName="sectionTitle"/>		     
		</div>
		<div class="GrayLT Right">* = Required Field</div>
	</div>
	
    <div class="label">* Email:</div>
	<div class="inputText">
		<label>
<form:input path="emailAddress" maxlength="70" cssClass="input"/>
		</label>
		<br/>
			<content:getAttribute attribute="text" beanName="emailText"/>
	 </div>
	 <div class="label">* Contract Number:</div>
	 <div class="inputText">
		<label>
<form:input path="contractNumber" maxlength="7" size="8" cssClass="input"/>
		</label>
		<br/>
		<content:getAttribute attribute="text" beanName="contractText"/>
	  </div>
   	
   	<userprofile:ssnTaxId/>

	<userprofile:licenseVerification/>
	
	<div class="BottomBorder">
		<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="mcPrefSectionTitle"/></div>
	</div>	 

	<userprofile:regMessageCenterPref/>
   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="continue" value="Continue"
	        onclick="return doProtectedSubmitBtn(document.createBOBForm, 'validate', this)">
        </div> 

    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Cancel"
             onclick="return doCancelBtn(document.createBOBForm, this)"> 
    </div>
 </bd:form>
</div>
 <br class="clearFloat" />

<layout:pageFooter/>
