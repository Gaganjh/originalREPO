<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>
<div id="content">
	<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
	<div class="BottomBorder">
	  <div class="SubTitle Gold Left"><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></div>
	 </div>
	  <c:choose>
	    <c:when test="${forgetPasswordForm.brokerWithNoActiveEntity}">
	  		<content:getAttribute attribute="body2" beanName="layoutPageBean"/>
	    </c:when>
	    <c:otherwise>
	       <content:getAttribute attribute="body1" beanName="layoutPageBean"/>
	    </c:otherwise>
	  </c:choose>
	
</div>
<br class="clearFloat" />

<layout:pageFooter/>