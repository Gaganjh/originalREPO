<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>


<div id="content">

<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>

<report:formatMessages scope="session"/>

<div class="BottomBorder">
	<div class="SubTitle Gold Left" style="overflow:visible">
		<content:getAttribute attribute="subHeader" beanName="layoutPageBean"/>
	</div>
</div>
<content:getAttribute attribute="body1" beanName="layoutPageBean"/>
<br class="clearFloat"/>
<bd:form action="/do/registerRiaUser/step3" modelAttribute="registrationCompleteForm" name="registrationCompleteForm">

<input type="hidden" name="action"/>
   <div class="formButtons">
  <c:if test="${requestScope.registrationCompleteForm.success}">
   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="continue" value="Log In Now"
	        onclick="return doProtectedSubmitBtn(document.registrationCompleteForm, 'continue', this)">
    </div> 
  </c:if>
  </div>
</bd:form>  
<br class="clearFloat"/>
<layout:pageFooter/>
</div>

