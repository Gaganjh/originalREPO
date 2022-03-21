<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.content.view.MutableUpdate"%>
<%@ page import="com.manulife.pension.bd.web.news.NewsForm"%>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />


<%

NewsForm nf = (NewsForm)request.getAttribute("newsForm");
Integer articleId = nf.getArticleId();
pageContext.setAttribute("articleId",articleId,PageContext.PAGE_SCOPE);
%>
 

<content:contentBean contentId="<%=articleId%>"
type="<%=BDContentConstants.TYPE_UPDATE%>" id="article" />

<content:contentUpdates id="allCurrentNews" isArchived="false"/>
<content:contentUpdatesByGroup id="currentNews" collection="allCurrentNews" groupId="<%=BDContentConstants.BD_NEWS_GROUP%>"/>

<style type="text/css"> 
#content3 ul{
	font-size: 12px;
	font-family: Arial, Helvetica, sans-serif;
}
</style>

<div id="contentOuterWrapper">
	<div id="contentWrapper">
		<div id="rightColumn1">
		   <h2 style="color: #DCD087;"><content:getAttribute id="layoutPageBean" attribute="body1Header"/><br /></h2>
			<c:if test="${not empty currentNews}">
			
<c:forEach items="${currentNews}" var="news" >
				   <h5>
				  
<c:if test="${news.key ==articleId}">
							<content:getAttribute id="news" attribute="title" /> - 
							<render:date property="news.date" patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED%>"/>
</c:if>
<c:if test="${news.key != articleId}">
<a href="/do/news/?action=article&amp;articleId=${news.key}">
							<content:getAttribute id="news" attribute="title" /></a> - 
							<render:date property="news.date" patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED%>"/>
</c:if>

				   </h5>
				   <p style="margin-top: 0px;"><content:getAttribute id="news" attribute="description" /></p>
</c:forEach>
</c:if>
		   <h5><a href="/do/news/?action=archive">View news archive</a></h5>
		</div>   
		<div id="contentTitle"><content:getAttribute id="layoutPageBean" attribute="name"/></div>
		<div id="content3" style="padding-top: 5px;">
			<c:if test="${not empty layoutPageBean.introduction1}">
				<p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>
			<c:if test="${not empty layoutPageBean.introduction2}">
				<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
</c:if>
		</div>
		<div id="content3" style="padding-top: 0px;">
			<h2 id="contentTitle" style="padding-top: 0px; padding-left: 0px;">
				<content:getAttribute id="article" attribute="title" />
				<span class="date">- <render:date property="article.date" patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED%>"/></span>
			</h2>
			<p style="margin-top: -5px;"><content:getAttribute id="article" attribute="text" /></p>
		</div>
		<br class="clearFloat" />
  </div>
</div>

<layout:pageFooter/>
