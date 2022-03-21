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

<content:contentBean contentId="<%=BDContentConstants.ADD_BOB_LEVEL2_FINISH%>" 
    type="<%=BDContentConstants.TYPE_MESSAGE%>" id="finishMessage" />

<c:set var="form" value="${addBOBForm}" scope="page"/>

<utils:cancelProtection name="addBOBForm" changed="true" exclusion="['action']"/>


<div id="contentFull">
	<layout:pageHeader nameStyle="h1"/>

	<report:formatMessages scope="request"/>

	<userprofile:myprofileTab/>

	<p><content:getAttribute attribute="body1" beanName="layoutPageBean"/></p>
	
	<p><content:getAttribute attribute="text" beanName="finishMessage"/></p>
	
	<p><content:getAttribute attribute="body2" beanName="layoutPageBean"/></p>
	
	<bd:form action="/do/myprofile/addBOB" modelAttribute="addBOBForm" name="addBOBForm">
	
<input type="hidden" name="action"/>
   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="continue" value="Finish"
	        onclick="return doProtectedSubmitBtn(document.addBOBForm, 'add', this)">
        </div> 

    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Cancel"
             onclick="return doCancelBtn(document.addBOBForm, this)"> 
    </div>
    	 
 </bd:form>
 </div>
 <br class="clearFloat" />

<layout:pageFooter/>
