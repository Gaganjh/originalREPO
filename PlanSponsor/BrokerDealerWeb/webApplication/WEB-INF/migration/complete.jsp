<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>


<div id="content">
<bd:form action="/do/migration/complete" modelAttribute="registrationCompleteForm" name="registrationCompleteForm">
<input type="hidden" name="action"/>
	<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>

<report:formatMessages scope="request"/>

	<content:getAttribute attribute="body1" beanName="layoutPageBean"/>
	<br class="clearFloat"/>
    <div class="formButtons">
	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="continue" value="Log Me in"
	        onclick="return doProtectedSubmitBtn(document.registrationCompleteForm, 'continue', this)">
    </div>
    </div> 
	
</bd:form>  
</div>

<layout:pageFooter/>
