<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentDescription" %>
<%@ page import="com.manulife.pension.ps.web.contentpages.*"%>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="errordivcs"><content:errors scope="session"/></div>
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/manulife.css" type="text/css">

<%-- This jsp includes the following CMA content --%>
<content:contentList id="glossary_categories" retrieveMethod="mostrecent" maxItems="0" type="<%=ContentTypeManager.instance().GLOSSARY_CATEGORY%>" />

<script type="text/javascript" >
function doSubmit(letterSelected){	
	document.forms.glossaryForm.letterSelected.value=letterSelected;
	document.forms.glossaryForm.submit(); 
}
</script>

<table border="0" cellspacing="0" cellpadding="0" width=760>

   <td width="23%" valign="top">
      <img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
   <td width="54%" valign="top">      
 
      <table width=100% border="0" cellspacing="0" cellpadding="0">
         <tr>
            <td width="580"><img src="/assets/unmanaged/images/s.gif" width="580" height="1"></td>
            <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
            <td width="100"><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
         </tr>    
	  <!-- ************ CONTENTS ************ -->            
 		 <tr> 
	        <td width="580" colspan="3">
			   <ps:form method="GET" modelAttribute="glossaryForm" name="glossaryForm" action="/do/contentpages/glossary">
				<form:hidden path="letterSelected" />

				<!-- letter selected -->
    			<%	Integer letterIndex = new Integer(0);    //if nothing is passed, default to 'A' page.
 		   		    
 		   		    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 		   		    
					GlossaryForm inputForm = (GlossaryForm) request.getAttribute("glossaryForm");
			        int letterIndexSelected  =  inputForm.getLetterSelected();	
					
     			 %>		   		    
 		   		        
 		   		<!-- letter links -->   
		   		<%	for ( int i = 0; i < alphabet.length(); i++ ) {
		   			    char letter = alphabet.charAt(i);
		   			    if ( i > 0 ) {%>
 		   				   <img src="/assets/unmanaged/images/orange_dot.gif" width="3" height="5">
 	   					<% }
		   			    if ( i == letterIndexSelected) { 
		   			       letterIndex = new Integer(i); %>
    					   <font class="boldText"><%=letter%></font>
    					<% } else { %>
						   <a href="javascript:doSubmit(<%=i%>);"><%=letter%></a>			
				 		<% } 
						pageContext.setAttribute("letterIndex",letterIndex,PageContext.PAGE_SCOPE);
 		   			    }	 //close for loop	 
 				 %>
				
 		   	     <c:forEach items="${glossary_categories}" var="resource" varStatus="i" >
					<c:if test="${resource != null && i.index.equals(letterIndex)}">
					  <c:set var="temp" value= "${resource.getKey()}" />
						<fmt:parseNumber var="key" type="number" value="${temp}" />
					 <content:contentBean contentId="${key}"  type="<%=ContentTypeManager.instance().GLOSSARY_CATEGORY%>" beanName="category"/>					         
					</c:if>
				</c:forEach>
 			
    			<br><br>
                <content:getAttribute beanName="category" attribute="text"/><br>
    			<br><br>
 			   </ps:form>
	   		
			   <p><content:pageFooter beanName="layoutPageBean"/></p>
 			   <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 			   <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
	  	 </tr>
         <tr>
            <td width="580"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
            <td width="20"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
            <td width="100"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
         </tr>
      </table>
	  <!-- ************ END CONTENT ************ -->	

   </td> 
   <td width=23%><img src="/assets/unmanaged/images/s.gif" height="1"></td>

</table>
