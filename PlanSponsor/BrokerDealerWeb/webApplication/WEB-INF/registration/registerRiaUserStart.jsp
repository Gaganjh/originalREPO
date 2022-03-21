<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>


<bd:form action="/do/registerRiaUser/start" cssClass="display:inline" modelAttribute="registerStartForm" name="registerStartForm">

	<input type="hidden" name="action"/>
</bd:form>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
	<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>

    <report:formatMessages scope="request"/>

	<div class="RegBox" style="padding-bottom:15px;margin-bottom:15px;">
	 <content:getAttribute attribute="body1Header" beanName="layoutPageBean"/>
	  <div style="margin-top:25px;margin-bottom:25px;">
	  <div id="otherBox" style="margin-top:0px; text-align:left;">
	    <p style = "font-size: 12px;">
	    <content:getAttribute attribute="body2" beanName="layoutPageBean"/>	    
		</p>
	   </div>
	   <div id="frBox"  style="margin-top:0px">	   
	    <p style = "font-size:15px;">
	     	<b><content:getAttribute attribute="body1" beanName="layoutPageBean"/></b>
	    </p>
	    <c:choose>
		  <c:when test="${requestScope.registerStartForm.disable}">
			   <input name="submit" type="submit" 
			   class="regButton" id="Register"  value="Register Now" disabled="disabled"/>
	      </c:when>
	      <c:otherwise>
		   <input name="submit" type="submit" onclick="return doProtectedSubmit(document.registerStartForm, 'continue', this)"
		   class="regButton" id="Register"  value="Register Now" />	      
	      </c:otherwise>
	    </c:choose>
	  </div>
	    <br class="clearFloat"/>
	  </div>	  
	</div>
<layout:pageFooter/>
</div>

