<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>


<bd:form action="/do/registerBrokerAssistant/start" cssClass="display:inline" modelAttribute="registerStartForm" name="registerStartForm">

	<input type="hidden" name="action"/>
</bd:form>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
	<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
	<report:formatMessages scope="request"/>

	<div class="RegBox">
	  <div id="middleBox" >
	    <h5>	  
	    <content:getAttribute attribute="body1" beanName="layoutPageBean"/>
	    </h5>
	     <c:choose>
	     <c:when test="${requestScope.registerStartForm.disable}">
	     <input name="submit" class="regButton" type="submit" disabled="disabled" value="Register Now" />
	     </c:when>
	     <c:otherwise>
	     <input name="submit" type="submit" onclick="return doProtectedSubmit(document.registerStartForm,'continue', this)" class="regButton" id="Register" 
  		         value="Register Now" />
	     </c:otherwise>
	     </c:choose>	  
	  </div>
	<br class="clearFloat">
	</div>
	<layout:pageFooter/>
</div>
