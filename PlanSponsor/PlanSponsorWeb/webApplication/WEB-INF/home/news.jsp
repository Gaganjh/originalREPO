<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@ page import="com.manulife.pension.content.valueobject.*"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%-- This jsp includes the following CMA content --%>

<ps:linkAccessible path="/do/news/currentUpdates/">

<content:contentBean contentId="<%=ContentConstants.PS_NEWS_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="miscNews"/>
<content:contentBean contentId="<%=ContentConstants.PS_NEWS_SECTION_MANULIFE_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="miscUpdates"/>
<content:contentBean contentId="<%=ContentConstants.PS_NEWS_SECTION_LEGISLATION_REGULATION_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="miscLegislativeUpdates"/>
<content:contentBean contentId="<%=ContentConstants.PS_NEWS_SECTION_LEGISLATION_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="homePageLegislativeUpdates"/>
<content:contentBean contentId="<%=ContentConstants.PS_NEWS_SECTION_NEWSLETTER_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="newsletter"/>

<content:contentUpdates id="currentUpdates" isArchived="false" isHomePage="true" />
<content:contentUpdatesByGroup id="updates" collection="currentUpdates" groupId="<%=ContentConstants.UPDATES_GROUP%>"/>
<content:contentUpdatesByGroup id="legislativeUpdates" collection="currentUpdates" groupId="<%=ContentConstants.LEGISLATIVE_UPDATES_GROUP%>"/>
<content:contentList id="issues" retrieveMethod="mostrecent" maxItems="4" type="<%=ContentTypeManager.instance().NEWSLETTER_ISSUE%>" />
<content:currentIssue id="currentIssues" collection="issues"/>

<% boolean hasContent=false; %>

<c:if test="${not empty  currentUpdates}">
   <% hasContent=true;%>
</c:if>
<c:if test="${not empty  currentIssues}">
   <% hasContent=true;%>
</c:if>


<% if (hasContent) { %>

<img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
  <table width="180" border="0" cellspacing="0" cellpadding="0" class="box">
  <tr> 
    <td width="1%"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    <td width="98%"><img src="/assets/unmanaged/images/s.gif" width="178" height="1"></td>
    <td width="1%"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  </tr>
  <tr class="tablehead"> 
    <td colspan="3" class="tableheadTD1"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><b style="color:#ffffff"><%-- CMA Managed --%><content:getAttribute beanName="miscNews" attribute="title"/></b></td>
         </tr>
      </table>
    </td>
  </tr>


  <tr> 
    <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<td class="boxbody"> 

	  <c:if test="${not empty updates}">
		<p><b><content:getAttribute beanName="miscUpdates" attribute="title"/></b><br>
<c:forEach items="${updates}" var="currUpdate" >
<a href='/do/news/currentUpdates/#X${currUpdate.id}'>
				<content:getAttribute id="currUpdate" attribute="teaser"/></a>
			<br><br>
</c:forEach>
</c:if>

 	<c:if test="${not empty currentIssues}">

	  <content:contentBean id="issue" contentId="<%=currentIssues.getKey()%>" type="<%=ContentTypeManager.instance().NEWSLETTER_ISSUE%>"/>
	  <c:if test="${not empty issue}">
	     <b><content:getAttribute beanName="newsletter" attribute="title"/></b><br>
		 <content:contentChildren id="issue" collection="issueArticles" type="<%=ContentTypeManager.instance().NEWSLETTER_ARTICLE%>"/>
	  
		 <% boolean firstFound = false; %>	  
<c:forEach items="${issueArticles}" var="resource">
		    <a href="/do/news/newsletterIssueArticle?issueArticleKey=${resource.getKey()}">				
			   <content:getAttribute beanName="resource" attribute="teaser"/>
		    </a>	  
   		    <br><br>
</c:forEach>
</c:if>
</c:if>
	    <c:if test="${ homePageLegislativeUpdates.text ne ''}">
			<b><content:getAttribute beanName="miscLegislativeUpdates" attribute="title"/></b><br>
			<content:getAttribute beanName="homePageLegislativeUpdates" attribute="text"/>			
</c:if>

	</td>
    <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  </tr>


  <tr> 
    <td colspan="3"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td rowspan="2"  width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
        </tr>
        <tr> 
          <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
      </table></td>
  </tr>
</table>
<% } %>

</ps:linkAccessible>
