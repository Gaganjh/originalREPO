<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<c:set var="articleId" value="${newsForm.articleId}" />

<content:contentBean contentId="articleId"
type="<%=BDContentConstants.TYPE_UPDATE%>" id="article" />

<div id="contentOuterWrapper">
	<div id="contentWrapper">
		<div id="rightColumn1">
		   <h2><content:getAttribute id="layoutPageBean" attribute="body2Header"/><br /></h2>
				<c:forEach items="${newsForm.archiveDatesList}" var="archiveDate">
					<a href="/do/news/?action=archive&amp;month=<render:date property='archiveDate' patternOut='<%=BDConstants.MONTH_FORMAT%>'/>&amp;year=<render:date property='archiveDate' patternOut='<%=BDConstants.YEAR_FORMAT%>'/>"><render:date property="archiveDate" patternOut="<%=BDConstants.MONTH_YEAR_FORMAT%>"/></a>
					<br />
				</c:forEach>
				 <br />
				<h5><a href="/do/news/">View current news</a></h5>
		</div>  
		<div id="contentTitle"><content:getAttribute id="layoutPageBean" attribute="name"/></div>
		<c:if test="${not empty layoutBean.layoutPageBean.introduction1}">
		<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
		</c:if>
		<c:if test="${not empty layoutBean.layoutPageBean.introduction2}">
		<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
		</c:if>
		<div id="content3">
			<h2>
				<content:getAttribute id="article" attribute="title" />
				<span class="date">- <render:date property="article.date" patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED%>"/></span>
			</h2>
			<p><content:getAttribute id="article" attribute="text" /></p>
		</div>
		<br class="clearFloat" />
  </div>
</div>

<layout:pageFooter/>
