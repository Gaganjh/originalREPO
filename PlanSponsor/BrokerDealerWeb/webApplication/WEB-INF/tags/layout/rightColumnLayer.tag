<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" %>
<%-- 
   This tag is used to display the tour section on the right hand side panel.
--%>

<%@attribute name="layerName" type="java.lang.String" rtexprvalue="true" required="true"%>
<c:if test="${layerName == 'layer1'}">

<c:set var="layerPageBean" value="${layoutBean.layoutPageBean}" />

<c:if test="${not empty layoutPageBean.layer1}">

<c:set var="layer" value="${layoutPageBean.layer1}"/> 

<div id="rightColumn1">
       <h1>${layer.name}</h1>
       <p>${layer.text}</p>
</div>
</c:if> 
</c:if>
<c:if test="${layerName == 'layer2'}">

<c:set var="layerPageBean" value="${layoutBean.layoutPageBean}" />

<c:if test="${not empty layoutPageBean.layer2}">

<c:set var="layer" value="${layoutPageBean.layer2}"/> 

<div id="rightColumn1">
       <h1>${layer.name}</h1>
       <p>${layer.text}</p>
</div>
</c:if> 
</c:if>
