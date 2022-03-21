<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="nameStyle"
	type="java.lang.String"
	required="true" rtexprvalue="true"%>

<c:if test="${not empty layoutBean.layoutPageBean.name}">
<${nameStyle}><content:getAttribute attribute="name" beanName="layoutPageBean"/></${nameStyle}>
</c:if>
<c:if test="${not empty layoutBean.layoutPageBean.introduction1}">
<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
</c:if>
<c:if test="${not empty layoutBean.layoutPageBean.introduction2}">
<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
</c:if>
