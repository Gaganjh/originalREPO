<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.content.valueobject.*"%>
<%@ page import="com.manulife.pension.content.view.*"%>

<%
// get newsletter section issue key from query string
String sectionKey = (String) request.getParameter("sectionKey");
int selectedSectionKey = 0;
selectedSectionKey = Integer.parseInt(sectionKey);
%>
 
<content:contentBean id="layoutPageBean" contentId="<%=ContentConstants.LAYOUT_PAGE_NEWSLETTERISSUE%>" type="<%=ContentTypeManager.instance().LAYOUT_PAGE%>"/>
<content:contentList id="issues" retrieveMethod="mostrecent" maxItems="0" type="<%=ContentTypeManager.instance().NEWSLETTER_ISSUE%>" />
<content:contentList id="sections" retrieveMethod="mostrecent" maxItems="0" type="<%=ContentTypeManager.instance().NEWSLETTER_SECTION%>" />
<content:section id="section" sections="sections" contentId="<%=selectedSectionKey%>"/> 
<content:contentList id="newsletters" retrieveMethod="mostrecent" maxItems="0" type="<%=ContentTypeManager.instance().NEWSLETTER_ARTICLE%>" />
<content:currentIssue id="currentIssue" collection="issues"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_NEWSLETTER_ARTICLES_IN_SECTION%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningNoArticlesInSection"/>

<script language="JavaScript1.2" type="text/javascript">

 var ie = true;
 if (!document.all) {
   ie = false;
 }

function gotoURL(href) {
	document.viewByIssueForm.action=href;
 	document.viewByIssueForm.submit();
}
 
</script>
<content:errors scope="session"/>

<table width=100% cellspacing="0" cellpadding=0 border=0>
   <tr>   
      <td width="4%" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br>
   	     <img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
   	  <td width="92%" valign="top" align="left">

		 <!-- begin main content table -->
		 <table width=100% border="0" cellspacing="0" cellpadding="0">
		    <tr>
               <!-- menu (column 1)-->
  			   <td width="135" valign="top" height="536"> 
      			  <table width="135" border="0" cellspacing="0" cellpadding="0">
					 <c:if test="${not empty currentIssue}">
         			 <br>
					 <a href='/do/news/newsletterIssueIndex?issueKey=<%=currentIssue.getKey()%>'>		
       				    Newsletter
     			     </a>
		 			 <br><br>  
</c:if>
					 <span>
	  					<b>Newsletter Sections</b>
						<br>
		 			 </span>   	

					 <br>
<c:forEach items="${sections}" var="resource" >
						<tr>
						<% 
						ContentDescription resource = (ContentDescription)pageContext.getAttribute("resource");
						
						if ( section.getKey() == resource.getKey() ) { %>
			  	 			  <td><b><content:getAttribute beanName="resource" attribute="displayName"/></b></td>
					    <% } else { %>
			  				  <td>
								 <a href='/do/news/newsletterSectionIndex?sectionKey=<%=resource.getKey()%>'>	
				  					<content:getAttribute beanName="resource" attribute="displayName"/>
								 </a>	
				  			  </td>        
						<% } %>
						</tr>
</c:forEach>
     			  </table>
    		   </td>
    		   <!--// end menu (column 1) -->

			   <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>

			   <!-- column 2 -->
 			   <td width="430" valign="top"> 
		 		  <br>
  			  	  <img src="<content:pageImage type="pageTitle" beanName="layoutPageBean"/>">		
				  <br>
			 	  <c:if test="${not empty layoutPageBean.introduction1}">
  		             <content:getAttribute attribute="introduction1" beanName="layoutPageBean"/>
				     <br>
</c:if>
			      <content:getAttribute attribute="introduction2" beanName="layoutPageBean"/><br><br>
				
				  <span><b><%= section.getDisplayName()%></b></span>
				  <br><br>
				  <% System.out.println("sectionkey " + section.getId()); %>
				  <content:newsletterArticleList collection="collection" sectionId='<%=selectedSectionKey%>'/>
				  <% boolean articlesFound = false;%>
				  <% boolean issueDisplayed = false;%>
				  <% if (collection.size() == 0){%>
					    <content:getAttribute beanName="warningNoArticlesInSection" attribute="text"/>
					    <br><br>
				  <% } else {%>				
<c:forEach items="${issues}" var="issue" >
<c:forEach items="${collection}" var="article" >

						   <% 
						   
						   ContentDescription issue = (ContentDescription)pageContext.getAttribute("issue");
						   ContentDescription article = (ContentDescription)pageContext.getAttribute("article");
						   
						   
						   if (article.getParentKey() == issue.getKey()) { %>
								 <p><b><content:getAttribute beanName="issue" attribute="displayName"/></b><br>
					  	     	<br>
							    
					       		<a href='/do/news/newsletterSectionArticle?sectionArticleKey=<%=article.getKey()%>'>		   
 						      		<content:getAttribute beanName="article" attribute="title"/>
					       		</a>
							
  					       		<br>
					       		<content:getAttribute beanName="article" attribute="description"/>
					       		<br>
					       		<% articlesFound=true;%>
						   <% } %>
</c:forEach>

</c:forEach>
						<% if (!articlesFound) { %>
					    <content:getAttribute beanName="warningNoArticlesInSection" attribute="text"/>
						    <br><br>
						<% } %>

       	                <p>     
	              <% } %>	
	              	       	
			      <form method="POST" name="viewByIssueForm">	
        		     <select name="NewsletterIssue" class="formBox" onchange="javascript:gotoURL(this.value)">
       		            <option value="Select an issue">Select an issue</option>
<c:forEach items="${issues}" var="resource" >
 <%
 ContentDescription resource = (ContentDescription)pageContext.getAttribute("resource");
 %>
		    		  	   <option value="/do/news/newsletterIssueIndex?issueKey=<%=resource.getKey()%>&_csrf=${_csrf.token}">
				 		 	  <content:getAttribute beanName="resource" attribute="displayName"/>
						   </option>
</c:forEach>
   			         </select>
				  </form>	
			   </td>	
			   <!--// end column 2 -->
		    </tr>	

			<tr>
   	 		   <td width="580" colspan=3>
	    		  <br>
	   			  <p><content:pageFooter beanName="layoutPageBean"/></p>
 	    		  <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 	    		  <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
		 	   </td>
		 	</tr>
		 </table>
	     <!--// end main content table -->
      
      </td>
      <td width="4%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>		 
   </tr>
</table>
