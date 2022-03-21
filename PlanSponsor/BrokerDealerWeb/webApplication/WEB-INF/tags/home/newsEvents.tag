<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@tag import="com.manulife.pension.bd.web.util.JspHelper"%>
<%@tag import="com.manulife.pension.content.valueobject.ContentDescription"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<content:contentUpdates id="allCurrentNews" isArchived="false" isHomePage="true"/>
<content:contentUpdatesByGroup id="currentNews" collection="allCurrentNews" groupId="<%=BDContentConstants.BD_NEWS_GROUP%>"/>
<content:contentBean contentId="<%=BDContentConstants.NEWS_EVENTS_HEADER%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="newsEventsHeader" />
<h2><content:getAttribute attribute="text" beanName="newsEventsHeader"/></h2>
<ul>




  <% try { %>
	<c:if test="${not empty currentNews}">

<c:forEach items="${currentNews}" var="news"  >


			<li class="news_item">
	 <p> <content:getAttribute id="news" attribute="teaser" /> <a href="/do/news/?action=article&amp;articleId=${news.key}" class="news_more">More</a> </p>		
				
			</li>
		
		</c:forEach>
	</c:if>
	<li class="news_item">
		<p><br />
			<a href="/do/news/?action=archive"><strong>News Archives</strong></a>&raquo;
		</p>
	</li>
	<% } catch (Exception e) {
		  JspHelper.log("newEvents.tag", e, "fails");
	   }
	%>						
</ul>				
