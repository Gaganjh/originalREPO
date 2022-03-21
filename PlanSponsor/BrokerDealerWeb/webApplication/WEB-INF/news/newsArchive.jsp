<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.content.view.MutableUpdate"%>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<content:contentBean contentId="<%=BDContentConstants.NO_ARCHIVED_NEWS_ITEMS_ERROR_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="noNewsErrorMsg" />

<fmt:formatDate var="selectedDate" pattern="<%=BDConstants.MONTH_YEAR_FORMAT%>" value="${newsForm.selectedArchiveDate}" />

<div id="contentOuterWrapper">
	<div id="contentWrapper">
		
		<div id="rightColumn1">
		   <h2><content:getAttribute id="layoutPageBean" attribute="body2Header"/><br /></h2>
				<c:forEach items="${newsForm.archiveDatesList}" var="archiveDate">
					<fmt:formatDate var="fmtArchiveDate" pattern="<%=BDConstants.MONTH_YEAR_FORMAT%>" value="${archiveDate}" />
					<c:choose>
						<c:when test="${fmtArchiveDate eq selectedDate}">
							<span class="selected"><render:date property="archiveDate" patternOut="<%=BDConstants.MONTH_YEAR_FORMAT%>"/></span>
						</c:when>
						<c:otherwise>
							<a href="/do/news/?action=archive&amp;month=<render:date property='archiveDate' patternOut='MM'/>&amp;year=<render:date property='archiveDate' patternOut='yyyy'/>"><render:date property="archiveDate" patternOut="<%=BDConstants.MONTH_YEAR_FORMAT%>"/></a>
						</c:otherwise>
					</c:choose>
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
		<c:if test="${not empty layoutBean.layoutPageBean.body1Header}">
		<div id="contentTitle"><content:getAttribute id="layoutPageBean" attribute="body1Header"/></div>
		</c:if>
<c:if test="${not empty newsForm.archivedNews}">
<c:forEach items="${newsForm.archivedNews}" var="news" >
							<div id="content3">
<h2><a href="/do/news/?action=archiveArticle&amp;articleId=${news.key}"><content:getAttribute id="news" attribute="title" /></a>
									<span class="date">- <render:date property="news.date" patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED%>"/> </span>
								</h2>
								<p><content:getAttribute id="news" attribute="description" /></p>
							</div>
</c:forEach>
</c:if>
<c:if test="${empty newsForm.archivedNews}">
			<p> <content:getAttribute id="noNewsErrorMsg" attribute="text"/>
			</p>
</c:if>
		<br class="clearFloat" />
  </div>
</div>

<layout:pageFooter/>
