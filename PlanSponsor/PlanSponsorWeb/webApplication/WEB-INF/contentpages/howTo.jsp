<%-- Imports --%>
<%@ page import="java.lang.Integer"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.*" %>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %> 
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- This jsp includes the following CMA content --%>
<content:contentBean contentId="<%=ContentConstants.COMMON_PRINT_REPORT_TEXT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Print_Report_Text"/>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<% 	String contentKey = (String) request.getParameter("contentKey");
	String ind = ((String) request.getParameter("ind")).toLowerCase(); 
	pageContext.setAttribute("ind", ind);
	int howToContentId = 0;
    if (contentKey != null) {
	   howToContentId = Integer.parseInt(contentKey); 
	}
 %>
<!--set the miscellaneus text bean -->

<content:contentBean contentId="<%=howToContentId%>" type="LayoutPage" beanName="HowToLayoutPageBean"/> 

<div id="errordivcs"><content:errors scope="session"/></div>

 <c:if test="${not empty HowToLayoutPageBean }" >
      <table width=100% cellspacing="0" cellpadding=0 border=0>
	    <tr>   
			<td width="4%" valign="top">
			<c:if test="${empty param.printFriendly }" >
<c:if test="${layoutBean.tpaHeaderUsed ==true}">
					<ps:isNotTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >				
						<img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br>  
					</ps:isNotTpa>
					<ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >					
						<c:if test="${not empty userProfile.currentContract}">
						
							<img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br></c:if>  
						</ps:isTpa>     	    				 
</c:if>
			</c:if>
				<c:if test="${not empty param.printFriendly }" >
						  <td width="4%" valign="top">&nbsp;<br>
						  
				</c:if>
      	      <img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    	  <td width="92%" valign="top" align="left">

			<c:if test="${not empty param.printFriendly }" >
				
      		<table width="100%" border="0" cellspacing="0" cellpadding="0">
        	  <tr>
          	    <td width="72%" ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                <td width="3%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
                <td width="25%" ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        	  </tr>
        	  <tr>
          		<td valign="top" >
		  		  <img src="/assets/unmanaged/images/s.gif" height="23"><br>
				  <img src="/assets/unmanaged/images/s.gif" height="1"><img src='<content:pageImage type="pageTitle" beanName="HowToLayoutPageBean"/>'><br><br>
		  		</td>
                <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        	  </tr>
        	  <tr>
          		<td valign="top" >
          			<content:getAttribute attribute="introduction1" beanName="HowToLayoutPageBean"/><br>
					<content:getAttribute attribute="introduction2" beanName="HowToLayoutPageBean"/>
		  		</td>
          		<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          		<td><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
        	  </tr>
	   	    </table>        
</c:if>

<c:if test="${empty param.printFriendly }" >
            <table width="700" border="0" cellspacing="0" cellpadding="0">
              <tr>
                 <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
          		 <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          		 <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
        	  </tr>
        	  <tr>
          		 <td valign="top">
		  			<img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
					<img src="/assets/unmanaged/images/s.gif" width="5" height="1"><img src='<content:pageImage type="pageTitle" beanName="HowToLayoutPageBean"/>'><br><br>
		    	    <table width="500" border="0" cellspacing="0" cellpadding="0">
              		  <tr>
						<td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
						<td width="475" valign="top">
			 	         <%--    <c:if test="${not empty layoutPageBean.introduction1}"> --%>
				               <content:getAttribute attribute="introduction1" beanName="HowToLayoutPageBean"/>
					           <br>
				         <%--    </c:if> --%>
							<content:getAttribute attribute="introduction2" beanName="HowToLayoutPageBean"/><br><br>
							<content:getAttribute attribute="body1" beanName="HowToLayoutPageBean"/>
						</td>
				 		<td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
				      </tr>
				  	</table>
	          	 </td>
          		 <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
         		 <td valign="top" class="right">
		  		   <img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>	
		  		   <c:if test="${ind =='r' }" >
		  	               <c:if test="${not empty userProfile.currentContract}">
		  	                 <jsp:include page="/WEB-INF/global/standardreportlistsection.jsp" flush="true" />
		  	               </c:if>
</c:if>
 			       <jsp:include page="/WEB-INF/contentpages/printhowtopage.jsp" flush="true" />

				   <content:pageFixedModule  beanName="HowToLayoutPageBean"/> 
                 
   				 </td>	
    		  </tr>	
       	    </table>
</c:if>
  
			<!-- begin main content table -->
			<table width="100%" border="0" cellpadding="0" cellspacing="0">

        	  <tr>   	  
          		<td width=100% valign="top">
		   		  <table width=100% border="0" cellspacing="0" cellpadding="0">
			  		<content:pageHowToSubject beanName="HowToLayoutPageBean" collection="HowToSubjectBean"/>
			
					<!-- get the HowToSubject content bean -->
<c:forEach items="${HowToSubjectBean}" var="howToSubject" varStatus="theIndex" >
	
	
					<content:contentBean contentId="${howToSubject.key}" type="HowToSubject" beanName="howToSubjectReportImage" override="true"/> 
					
		

<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
						<!-- if image does not exists, skip the explanation, continue with next howToSubject -->				
						<c:if test="${not empty howToSubject.formReportImage}">
							<!-- the following commented code is used by the http unit test...do not remove --> 
    			    		<!-- HowToSubject form/report image -->
                    		<tr>
					  			<td width=100% valign="top">
					  			<br><br><content:image id="howToSubjectReportImage" contentfile="formReportImage"/>
					  			</td> 
				    		</tr> 
 				
							<!-- get the corresponding HowToExplanation content bean -->
 				    
  				    		<content:contentChildren beanName="howToSubject" collection="HowToExplanationCollection"/>
	
<c:forEach items="${HowToExplanationCollection}" var="exp" >
 			 	    			<c:if test="${not empty exp}">
			 	             
									<content:contentBean contentId="${exp.key}" type="HowToExplanation" beanName="HowToExplanationBean" override="true"/> 

			    					<tr>
					  					<td width=100% valign="top">    					    	
				    						<table width=100% border="0" cellspacing="0" cellpadding="0">	
 				      	  						<tr>
													<td width=2% valign="top">
														<content:image id="HowToExplanationBean" contentfile="numberImage"/>
													</td>          
    												<td width=1% ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>  
    												<td width=97%>
    		  			     							<b><content:getAttribute attribute="title" beanName="HowToExplanationBean" /></b>
    		     									</td>   
						  						</tr>
   				 		  						<tr>
													<td width=2% ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
													<td width=1%><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>         
    												<td width=97%>
      		 		            						<content:getAttribute attribute="text" beanName="HowToExplanationBean" />
     	  			        						</td>    
      												<br>
						  						</tr>
					    					</table>
					  					</td>
									</tr>				

</c:if>
</c:forEach>
</c:if>
</c:if>

		

<c:if test="${applicationScope.environment.siteLocation=='ny'}" >
						<!-- if image does not exists, skip the explanation, continue with next howToSubject -->
						<c:if test="${not empty howToSubject.nyFormReportImage}">
							<!-- the following commented code is used by the http unit test...do not remove --> 
    			    		<!-- HowToSubject form/report image -->
                    		<tr>
					  			<td width=100% valign="top">
					  			<br><br><content:image id="howToSubjectReportImage" contentfile="nyFormReportImage"/>
					  			</td> 
				    		</tr> 
 				
							<!-- get the corresponding HowToExplanation content bean -->
 				    
  				    		<content:contentChildren beanName="howToSubject" collection="HowToExplanationCollection"/>
	
<c:forEach items="${HowToExplanationCollection}" var="exp" >
 			 	    			<c:if test="${not empty exp}">
			 	             
									<content:contentBean contentId="${exp.key}" type="HowToExplanation" beanName="HowToExplanationBean" override="true"/> 

			    					<tr>
					  					<td width=100% valign="top">    					    	
				    						<table width=100% border="0" cellspacing="0" cellpadding="0">	
 				      	  						<tr>
													<td width=2% valign="top">
														<content:image id="HowToExplanationBean" contentfile="numberImage"/>
													</td>          
    												<td width=1% ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>  
    												<td width=97%>
    		  			     							<b><content:getAttribute attribute="title" beanName="HowToExplanationBean" /></b>
    		     									</td>   
						  						</tr>
   				 		  						<tr>
													<td width=2% ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
													<td width=1%><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>         
    												<td width=97%>
      		 		            						<content:getAttribute attribute="text" beanName="HowToExplanationBean" />
     	  			        						</td>    
      												<br>
						  						</tr>
					    					</table>
					  					</td>
									</tr>				

</c:if>
</c:forEach>
</c:if>
</c:if>
 
				
</c:forEach>
 
		    	  </table>
        	 	</td>       	 	
        	  </tr>                
	
       		  <tr>
	   		 	<td width="100%">
	   		 		<br>
					<p><content:pageFooter beanName="layoutPageBean"/></p>
 					<p class="footnote"><content:pageFootnotes beanName="HowToLayoutPageBean"/></p>
 					<p class="disclaimer"><content:pageDisclaimer beanName="HowToLayoutPageBean" index="-1"/></p>
 				</td>
 			  </tr>
 		
   		    </table>
    		<c:if test="${not empty param.printFriendly }" >
				<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="globalDisclosure"/>

				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
					</tr>
				</table>
			</c:if>
			<!-- end main content table --> 
			
          </td>
	      <td width="4%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>		 
	    </tr>
      </table>

</c:if>
<c:if test="${empty HowToLayoutPageBean }" >
	<% System.out.println("Warning! Missing howToLayoutPageBean content " + howToContentId); %>
</c:if>
