<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.*"%>
<%@ page import="com.manulife.pension.content.view.*"%>

<%
// get newsletter issue article key from query string
String issueArticleKey = (String) request.getParameter("issueArticleKey");
int selectedArticleKey = Integer.parseInt(issueArticleKey);
%>

<script type="text/javascript" >

	function go(url){
		location.href = url;
	}
	
</script>	

<content:contentBean id="layoutPageBean" contentId="<%=ContentConstants.LAYOUT_PAGE_NEWSLETTERISSUE%>" type="<%=ContentTypeManager.instance().LAYOUT_PAGE%>"/>
<content:newsletter id="newsletter" contentId="<%=selectedArticleKey%>" section="section"/>
<content:contentBean id="issue" contentId='<%=newsletter.getParentKey()%>' type="<%=ContentTypeManager.instance().NEWSLETTER_ISSUE%>"/>
<content:contentChildren id="issue" collection="newsletters" type="<%=ContentTypeManager.instance().NEWSLETTER_ARTICLE%>"/>
<content:contentList id="sections" retrieveMethod="mostrecent" maxItems="0" type="<%=ContentTypeManager.instance().NEWSLETTER_SECTION%>" />

<content:next id="next" current="newsletter" collection="newsletters"/>
<content:previous id="previous" current="newsletter" collection="newsletters"/>
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
       		         <br>
		 			 <a href='/do/news/newsletterIssueIndex?issueKey=<%=issue.getKey()%>'>		
      				    Newsletter
         			 </a>
				     <br><br>  
		 		     <span>
	  				    <b>Newsletter Articles</b>
					    <br>
		 		     </span>   
		 		     <br>
<c:forEach items="${newsletters}" var="article" >
					 	<tr>
					 	<%
					 	ContentDescription article = (ContentDescription)pageContext.getAttribute("article");
					 	
					 	if ( article != null && article.getKey() == newsletter.getKey() ) { %>
		  	  				  <td><b><content:getAttribute beanName="article" attribute="title"/></b></td>
					 	<% } else if ( article != null ) { %>
		  			    	  <td>
							  	 <a href='/do/news/newsletterIssueArticle?issueArticleKey=<%=article.getKey()%>'>		
 				 			  	    <content:getAttribute beanName="article" attribute="title"/>
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
	
<span><b>${issue.name}</b></span>
				  <br><br>
				  <span><b><content:getAttribute beanName="newsletter" attribute="title"/></b></span>
				  <content:getAttribute beanName="newsletter" attribute="text" />
   		    	  <br><br>

         		  <!-- BUTTONS -->
		          <table width="430" border="0" cellspacing="0" cellpadding="0">
   			         <tr>
      		            <td align="left">
                           <c:if test="${not empty previous}"> 
                              <input type="button" value="previous" class="button100Lg" onclick="javascript:go('/do/news/newsletterIssueArticle?issueArticleKey=<%=((Content)previous).getKey()%>');" />
                           </c:if>
         	            </td>

        	            <td align="right">
                           <c:if test="${not empty next}">
                              <input type="button" value="next" class="button100Lg" onclick="javascript:go('/do/news/newsletterIssueArticle?issueArticleKey=<%=((Content)next).getKey()%>');" />
                           </c:if>
                        </td>
          			 </tr>
		          </table>

                  <br>
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


