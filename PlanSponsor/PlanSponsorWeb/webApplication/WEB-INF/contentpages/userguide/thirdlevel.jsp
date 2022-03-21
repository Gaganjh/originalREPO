<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.pension.content.valueobject.LayoutPage" %>
<%@ page import="com.manulife.pension.content.valueobject.GuideArticle" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>


<%
	//retrieve content key for the Guide Article from the request
	String strContentKey = request.getParameter("contentKey");
	
	int contentKey = 0;
	if(strContentKey != null) {
		contentKey = Integer.parseInt(strContentKey);
	}
	
	String showNav = request.getParameter("nav");
	
	if (showNav == null) {
		showNav = "true";
	} 
	// JIRA#131 Contact us Changes - Start 
	UserProfile userprofile = (UserProfile)request.getSession(false).getAttribute(Constants.USERPROFILE_KEY);
	boolean isBundledIndicator  = userprofile.getCurrentContract().isBundledGaIndicator();
	// JIRA#131 Contact us Changes  - End 
	
	
%> 
<content:contentBean contentId="<%=contentKey%>" type="GuideArticle" beanName="ThirdLevelBean"/>
<content:contentUserGuideParent articleName="ThirdLevelBean" secondLevelBean="SecondLevelBean">
	<content:param><%=String.valueOf(isBundledIndicator)%></content:param>
</content:contentUserGuideParent>

<%
	
	Object bean1 = pageContext.getAttribute("ThirdLevelBean", PageContext.REQUEST_SCOPE);
	GuideArticle guideArticle = (GuideArticle)bean1; 
%>

<script type="text/javascript" >
	document.title = "<%=guideArticle.getTitle()%>";
</script>

 <%
   		PageContext pc = pageContext;
   		Object bean = pageContext.getAttribute("SecondLevelBean", PageContext.SESSION_SCOPE);
   		LayoutPage aPage = (LayoutPage)bean;
   		String prevLink = "/do/contentpages/userguide/secondlevel/?contentKey=" + aPage.getKey();
   		
   	%>

<c:if test="${SecondLevelBean.parentId ==73}">
	<script type="text/javascript" >
		init(2, 1);
	</script>
</c:if>
<c:if test="${SecondLevelBean.parentId ==75}">
	<script type="text/javascript" >
		init(2, 4);
	</script>
</c:if>
<c:if test="${SecondLevelBean.parentId !=73}">
<c:if test="${SecondLevelBean.parentId !=75}">
		<script type="text/javascript" >
			init(0, 0);
        </script>
</c:if>
</c:if>



<!-- Add the triangle at the bottom left corner of the header -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td width="10" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br></td>
	</tr>
</table>

<table>
<tr>
    <td width="10" ><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
    <td width="125"><img src="/assets/unmanaged/images/s.gif" width="125" height="1"></td>
    <td width="10" ><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
    <td width="375"><img src="/assets/unmanaged/images/s.gif" width="375" height="1"></td>
    <td width="15"><img src="/assets/unmanaged/images/s.gif" width="15" height="1"></td>
    <td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
    <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
</tr>
<tr>	
<td width="10" valign="top"><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
<td width="125" vAlign=top>

  <table id=column1 cellSpacing=0 cellPadding=0 border=0>
    <!--<tbody> -->
      
      <tr vAlign=top>
        <td width="125">
          <img height=70 src="/assets/unmanaged/images/spacer.gif" width=1>
        </td>
      </tr>
	  
	  <% if ("true".equalsIgnoreCase(showNav)) { %>
	  	<tr>
      	
        	<td class=greyText><table width="125" border="0" cellpadding="0" cellspacing="0">
          
				<tr>
					<td > 
					<p>
					<b><a href="<%=prevLink%>"><%=aPage.getName()%></a></b><br><br>
      
					<content:thirdLevelLHLinks id="ThirdLevelBean" secondLevelName="secondPage">
						<content:param><%=String.valueOf(isBundledIndicator)%></content:param>
					</content:thirdLevelLHLinks>
					</p>
					<p>&nbsp;</p>
            	
					</td>
          </tr>
         </table>
         </td>
       </tr>
	   <% } %>
	   <!--</tbody> -->
  </table>
</td>

<td width="10"><img height="1" src="/assets/unmanaged/images/spacer.gif" width="10" border="0"></td>


<!-- Now we move on to the main page -->
<!-- end column 1 -->
<!-- column 2 (375) -->
<td width="375" vAlign="top" class="greyText"> 
  <!-- <img src="/assets/unmanaged/images/s.gif" width="383" height="23"><br> -->
  <!-- <img src="/assets/pagetitleimages/enrollments.gif" alt="Changing Participant Data" width="220" height="33"><br> -->
  <br>
  <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">      
      <td width="375" vAlign="top" class="greyText">
      	<img src="/assets/unmanaged/images/s.gif" width="373" height="0"><br>
      	<img src='<content:pageImage type="pageTitle" beanName="SecondLevelBean"/>'>
      	<img src="/assets/unmanaged/images/s.gif" width="1" height="20">
      	<div id="errordivcs"><content:errors scope="session"/></div>
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      	  <tr>
      	  	<td width="1"><img height="10" src="/assets/unmanaged/images/spacer.gif" width="1" height="10 border="0"></td>
      	  </tr>
      	  <tr>
      	    <td><p>
      	    	<b><content:getAttribute attribute="title" beanName="ThirdLevelBean"/></b>
            	<br>
            	<content:getAttribute attribute="text" beanName="ThirdLevelBean"/>
      		</p></td>
      	  </tr>
      	  <tr>
	 		<td>
	   			<br>
		 		<p><content:pageFooter beanName="SecondLevelBean"/></p>
 		 		<p class="footnote"><content:pageFootnotes beanName="SecondLevelBean"/></p>
 		 		<p class="disclaimer"><content:pageDisclaimer beanName="SecondLevelBean" index="-1"/></p>
 	 		</td>
  	      </tr>
      	</table>
       </td>
      </table>
</td> 
<td width="15" height="312" valign="top" class="fixedTable"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1"></td>


<td valign="top">
  <img src="/assets/unmanaged/images/s.gif" width="1" height="25">            
  <img src="/assets/unmanaged/images/s.gif" width="1" height="5">     
  <content:rightHandLayerDisplay layerName="layer3" beanName="SecondLevelBean" />   
		  
  <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
  <content:rightHandLayerDisplay layerName="layer4" beanName="SecondLevelBean" />   		  

</td>
<td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="312"></td>

</tr>

</table>
<br>     
