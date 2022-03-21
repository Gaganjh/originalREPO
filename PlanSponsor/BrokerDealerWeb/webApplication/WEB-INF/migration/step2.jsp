<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>

<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>


<c:set var="form" value="${migrationForm}" scope="page"/>
<utils:cancelProtection name="migrationForm" changed="true"/>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
<bd:form action="/do/migration/step2" modelAttribute="migrationForm" name="migrationForm">
<input type="hidden" name="primaryBrokerPartyId"/>
<input type="hidden" name="action"/>
<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>

<report:formatMessages scope="request"/>

  <userprofile:brokerEntityList name="brokerEntityProfilesList" form="${form}" formName="migrationForm" renderSection="yes"/>

      <br class="clearFloat"/>
    <div class="formButtons">
   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="continue" value="Continue"
	        onclick="return doProtectedSubmitBtn(document.migrationForm, 'continue', this)">
    </div> 

    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Cancel"
             onclick="return doCancelBtn(document.migrationForm, this)"> 
    </div>
    </div>
</bd:form>  
<layout:pageFooter/>
</div>

