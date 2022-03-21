<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.content.view.MutableUpdate"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<content:contentUpdates id="allCurrentNews" isArchived="false"/>
<content:contentUpdatesByGroup id="currentNews" collection="allCurrentNews" groupId="<%=BDContentConstants.BD_NEWS_GROUP%>"/>

<div id="contentOuterWrapper">
	  <div id="contentWrapper">
	    <div id="contentTitle"><content:getAttribute id="layoutPageBean" attribute="name"/></div>
		<div id="content3" style="padding-top: 5px;">
		<c:if test="${not empty layoutPageBean.introduction1}">
				<p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
	</c:if>
			<c:if test="${not empty layoutPageBean.introduction2}">
				<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
</c:if>
		</div>
		<report:formatMessages scope="session"/>
		<c:if test="${not empty currentNews}">
		
<c:forEach items="${currentNews}" var="news"  >
					<div id="content3" style="padding-top: 0px;">
						<h2>
			
<a href="/do/news/?action=article&amp;articleId=${news.key}">

								<content:getAttribute id="news" attribute="title" />
							</a> 
							<span class="date">- <render:date property="news.date" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"/> </span>
						</h2>
						<p style="margin-top: -2px;"><content:getAttribute id="news" attribute="description" /></p>
					</div>
</c:forEach>
</c:if>
			<div id="content3" style="padding-top: 0px;">
				<p><a href="/do/news/?action=archive"><strong>View news archive</strong></a></p>   
			</div>
		<br class="clearFloat" />
	</div>
</div>

<layout:pageFooter/>
