<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.*"%>
<%@ page import="com.manulife.pension.content.bizdelegates.*"%>

<content:contentBean id="layoutPageBean" contentId="<%=ContentConstants.LAYOUT_PAGE_NEWSLETTERISSUE%>" type="<%=ContentTypeManager.instance().LAYOUT_PAGE%>"/>
<content:contentList id="issues" retrieveMethod="mostrecent" maxItems="0" type="<%=ContentTypeManager.instance().NEWSLETTER_ISSUE%>" />

<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_NEWSLETTERS%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningNoNewsletters"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_NEWSLETTER_ARTICLES%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningNoNewsletterArticles"/>

<%
// if no newsletter issue content key were passed in the query string
// we take the first one in the list of most recent ones.

String issueKey = (String) request.getParameter("issueKey");
int selectedIssueKey = 0;

if (issueKey == null) {
	if(issues != null && issues.size()>0) {
    ContentDescription latestIssueContent= (ContentDescription)issues.get(0);
    selectedIssueKey = latestIssueContent.getKey();
    } else {
    	//TODO: There are no newsletters for this page, what do we do??
    	selectedIssueKey = 0;
    }
} else {
    selectedIssueKey = Integer.parseInt(issueKey);
}	

%>
	<content:contentBean id="issue" contentId="<%=selectedIssueKey%>" type="<%=ContentTypeManager.instance().NEWSLETTER_ISSUE%>"/>
	<content:contentChildren id="issue" collection="newsletters" type="<%=ContentTypeManager.instance().NEWSLETTER_ARTICLE%>"/>
	<content:contentList id="sections" retrieveMethod="mostrecent" maxItems="0" type="<%=ContentTypeManager.instance().NEWSLETTER_SECTION%>" />

<script language="JavaScript1.2" type="text/javascript">
  
 var ie = true;
 if (!document.all) {
   ie = false;
 }

function gotoURL(href) {
	document.viewBySectionForm.action=href;
 	document.viewBySectionForm.submit();
}

</script>

<content:errors scope="session"/>
<table width=100% cellspacing="0" cellpadding=0 border=0>
   <tr>   
      <td width="4%" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br>
   	     <img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
   	  <td width="92%" valign="top" align="left">  

		 <!--// begin main content table -->
		 <table width=100% border="0" cellspacing="0" cellpadding="0">
  			<tr>
	 		   <!-- menu (column 1)-->
		  	   <td width="135" valign="top" height="536"> 
 			      <table width="135" border="0" cellspacing="0" cellpadding="0">
					 <br>  
					 <span>
				  		<b>Newsletter Issues</b>
						<br>
					 </span>	
					 <br>
<c:forEach items="${issues}" var="resource" >
					    <tr>
					    <%
						 ContentDescription resource = (ContentDescription)pageContext.getAttribute("resource");
					    if ( issue.getKey() == resource.getKey() ) { %>
					  	      <td><b><content:getAttribute beanName="resource" attribute="displayName"/></b></td>
					    <% } else { %>
		  	  		          <td>
								 <a href='/do/news/newsletterIssueIndex?issueKey=<%=resource.getKey()%>'>		
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
   	 	  		     <img src='<content:pageImage type="pageTitle" beanName="layoutPageBean"/>'>		
			 	  <br>
			 	  <c:if test="${not empty layoutPageBean.introduction1}">
  		             <content:getAttribute attribute="introduction1" beanName="layoutPageBean"/>
				     <br>
</c:if>
			      <content:getAttribute attribute="introduction2" beanName="layoutPageBean"/><br><br>
				  <% if (selectedIssueKey <= 0) { %>
				  	<b><content:getAttribute beanName="warningNoNewsletters" attribute="text"/></b>
				  <% } else { %>
					  	<span><b><%= issue.getDisplayName()%></b></span>
					  	<br><br>
			 
  		 				<% BrowseServiceDelegate browseDelegate = BrowseServiceDelegate.getInstance(); %>
	
					  	<c:if test="${empty newsletters}">
					  		<content:getAttribute beanName="warningNoNewsletterArticles" attribute="text"/><br>
</c:if>
<c:forEach items="${newsletters}" var="newsletter" >
				 	 	<%
						 ContentDescription newsletter = (ContentDescription)pageContext.getAttribute("newsletter");
   	  	  	     	    	NewsletterArticle heavyweight = (NewsletterArticle)browseDelegate.findContent(newsletter.getKey(), ContentTypeManager.instance().NEWSLETTER_ARTICLE);
					 		String sectionName = ((NewsletterSection)heavyweight.getNewsletterSection()).getSectionName(); 
			 	  		%>
    			     	<b><%= sectionName %></b>
				     	<br>
				     	<a href='/do/news/newsletterIssueArticle?issueArticleKey=<%=newsletter.getKey()%>'>		   
 				        	<content:getAttribute beanName="newsletter" attribute="title"/>
				     	</a>
			
					 	<br>
			 	     	<content:getAttribute beanName="newsletter" attribute="description"/>
			  	     	<br><br>
</c:forEach>
		   	      <% } %> 
  
			 	  <form method="POST" name="viewBySectionForm"> 
				     <select name="NewsletterSection" class="formBox" onchange="javascript:gotoURL(this.value)">
					    <option value="Select a section">Select a section</option>
<c:forEach items="${sections}" var="resource" >
						 <%
						 ContentDescription resource = (ContentDescription)pageContext.getAttribute("resource");
						 %>

		   	         	   <option value="/do/news/newsletterSectionIndex?sectionKey=<%=resource.getKey()%>&_csrf=${_csrf.token}">
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


