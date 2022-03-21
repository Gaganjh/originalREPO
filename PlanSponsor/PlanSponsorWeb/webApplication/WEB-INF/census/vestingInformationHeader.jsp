<%-- Prevent the creation of a session --%>
 
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="quickreports" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.pagelayout.LayoutBean" %>
<%@page import="org.owasp.encoder.Encode"%>

<jsp:include page="../global/standardheader.jsp" flush="true"/>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
LayoutBean layoutBeanObj = (LayoutBean)request.getAttribute("layoutBean");
%>

             
<jsp:useBean id="vestingInformationForm" scope="session" type="com.manulife.pension.ps.web.census.VestingInformationForm" />


             
                         
<% 
	boolean fromWithdrawals = false; 
String removeLink="false";
String href ="#";
String onclick ="return true;";
%>
<c:if test="${param.source == 'wd'}">
<% fromWithdrawals = true; %>
</c:if>

<c:set var="creditingMethod" value="${vestingInfo.employeeVestingInformation.retrievalDetails.creditingMethod}" />
			<%String creditingMethod= pageContext.getAttribute("creditingMethod").toString(); %>				
<c:if test="${empty param.printFriendly}">
<table width="760" border="0" cellspacing="0" cellpadding="0">
</c:if>
<c:if test="${not empty param.printFriendly}">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>

  <tr>
    <td width="30" valign="top">
      <c:if test="${empty param.printFriendly}">
        <img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
      </c:if>
    </td>
    <td width="700" valign="top">

	  <table width="700" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td width="500"><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
	      <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
	    </tr>
	    <tr>
	      <td valign="top">
	      <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
	      <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
	      
<c:if test="${not empty layoutBean.getParam('titleImageFallback')}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}" /> 



	 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>"
alt="${layoutBean.getParam('titleImageAltText')}">
		      <br>
</c:if>
<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
	 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
		      <br>
</c:if>

		  
<c:if test="${empty param.printFriendly}">

	        <table width="500" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
	            <td valign="top"><br>
	              <!--Layout/intro1-->
	              <c:if test="${not empty layoutPageBean.introduction1}">
                    <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
                    <br>
</c:if>
  				  <!--Layout/Intro2-->
				  <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
         		  <br>
<c:if test="${not empty layoutBean.getParam('additionalIntroJsp')}">
<c:set var="additionalJSP" value="${layoutBean.getParam('additionalIntroJsp')}" /> 



                    <jsp:include page="${additionalJSP}" flush="true"/>
</c:if>
         		</td>
	            <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	            <td width="180" valign="top">
	            	<ps:notPermissionAccess permissions="SELA">
					<logicext:if name="<%= Constants.USERPROFILE_KEY%>" property="currentContract.mta" op="equal" value="false">
					<logicext:and name="<%= Constants.USERPROFILE_KEY%>" property="currentContract.contractAllocated" op="notEqual" value="false"/>
						<logicext:then>
							<content:howToLinks id="howToLinks" layoutBeanName="layoutPageBean"/>	
	                      	<content:contentBean contentId="<%=ContentConstants.COMMON_HOWTO_SECTION_TITLE%>"
	                                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	                                  id="HowToTitle"/>
			                	 <%-- Start of How To --%>
								 <ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle"/>
			     		         <%-- End of How To --%>
						</logicext:then>
					</logicext:if>
					</ps:notPermissionAccess>
	            </td>
	          </tr>
	        </table>
</c:if>
	      </td>
<c:if test="${empty param.printFriendly}">
	      <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td valign="top" class="right">
	
	        <img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>
<c:if test="${layoutBean.getParam('suppressReportList') !=true}">
 	        	<jsp:include page="../global/standardreportlistsection.jsp" flush="true" />
</c:if>
 	        
<c:set var="href" value="#"/>
<c:set var="onclick" value="return true;" />
			<table width="180" border="0" cellspacing="0" cellpadding="0" class="beigeBox">
			  <tr>
			    <td class="beigeBoxTD1">
			      <table width="100%" border="0" cellspacing="0" cellpadding="0">
			      
			       <%
			      String reportToolsLinksParamNameAsIds = "reportToolsLinks";
				  	if(layoutBeanObj.getParam(reportToolsLinksParamNameAsIds) != null){
				  		pageContext.setAttribute("paramNameAsIds", layoutBeanObj.getParamAsIds(reportToolsLinksParamNameAsIds));
				  	}
			      %>
			      
<c:forEach items="${paramNameAsIds}" var="contentId" varStatus="indexVal"> 

<c:set var="contentId" value="${contentId}"/>
<%String contentId = pageContext.getAttribute("contentId").toString(); %>
			
			          <content:contentBean contentId="<%=Integer.parseInt(contentId)%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>
				  <%
					if ((Integer.parseInt(contentId) == ContentConstants.COMMON_READ_THIS_REPORT_TEXT)) { %>			    
			          <content:contentBean contentId="<%=ContentConstants.COMMON_READ_THIS_REPORT_TEXT%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>
			                               
			      <%	String howToReadReportLinkParamName;  
			      
			            if ("H".equals(creditingMethod)) {
				        	if (fromWithdrawals) {
				        		howToReadReportLinkParamName = "howToReadReportLinkWithdrawalsH";		
				        	} else {
				        		howToReadReportLinkParamName = "howToReadReportLinkCensusH";	
				        	}
			            } else {
				        	if (fromWithdrawals) {	
				        		howToReadReportLinkParamName = "howToReadReportLinkWithdrawalsE";	
				        	} else {
				        		howToReadReportLinkParamName = "howToReadReportLinkCensusE";
				        	}
				        }
			            
			            String howToReadReportLinkParamName1 = layoutBeanObj.getParam(howToReadReportLinkParamName);
			            pageContext.setAttribute("howToReadReportLinkParamName1", howToReadReportLinkParamName1);
			      %>
			                              		          
<c:set var="howToReadReportKey" value="${howToReadReportLinkParamName1}"/> 
<%
String howToReadReportKey = pageContext.getAttribute("howToReadReportKey").toString();

%>
			              <%
			              		href = "javascript:doHowTo('" + howToReadReportKey+ "','r')";
			              		onclick = "return true;";
			              %>
					<% } else if (Integer.parseInt(contentId) == ContentConstants.COMMON_PRINT_REPORT_TEXT) { %>
			            <content:contentBean contentId="<%=ContentConstants.COMMON_PRINT_REPORT_TEXT%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>		          
			        	<%
	          				href = "javascript://>";
        	  				onclick = "doPrint()";
        	  			%>
        	  		
					<% } else if (Integer.parseInt(contentId) == ContentConstants.MISCELLANEOUS_VESTING_EMPLOYEE_SNAPSHOT_LINK) { %>
						<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_EMPLOYEE_SNAPSHOT_LINK%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>	
			            <%
			              	String result = "/do/census/viewEmployeeSnapshot/?profileId=" +
			              			Encode.forHtml(vestingInformationForm.getProfileId()) +
											"&source=" + CensusConstants.VESTING_INFO_PAGE;
	       	    			href = result;
	            			onclick = "return true;";
			            %>
					
			        <% } %>
			        
			          <tr>
			            <td width="17">
						  <a href="<%=href %>" onclick="<%=onclick %>">
						    <content:image id="contentBean" contentfile="image"/></a>
						  </td>
			            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
			            <td>
			              <a href="<%=href %>" onclick="<%=onclick %>">
			                <content:getAttribute id="contentBean" attribute="text"/>
			              </a>
			            </td>
			          </tr>          
</c:forEach>
			      </table>
			    </td>
			  </tr>
			  <tr>
			    <td align="right"><img src="/assets/unmanaged/images/box_lr_corner_E9E2C3.gif" width="5" height="5"></td>
			   </tr>
			 </table>
 	      </td>
</c:if>
<c:if test="${not empty param.printFriendly}" >
<% if (fromWithdrawals) { %>
	      <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td valign="top" class="right">
 	        
<c:set var="href" value="#"/>
<c:set var="onclick" value="return true;" />
			<table width="180" border="0" cellspacing="0" cellpadding="0" class="beigeBox">
			  <tr>
			    <td class="beigeBoxTD1">
			      <table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <%
			      String reportToolsLinksParamNameAsIds = "reportToolsLinks";
				  	if(layoutBeanObj.getParam(reportToolsLinksParamNameAsIds) != null){
				  		pageContext.setAttribute("paramNameAsIds", layoutBeanObj.getParamAsIds(reportToolsLinksParamNameAsIds));
				  	}
			      %>
<c:forEach items="${paramNameAsIds}" var="contentId" varStatus="indexVal"> 

<c:set var="contentId" value="${contentId}"/>
<%String contentId = pageContext.getAttribute("contentId").toString(); %>

			
			          <content:contentBean contentId="<%=Integer.parseInt(contentId)%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>
				  <%
					if ((Integer.parseInt(contentId) == ContentConstants.COMMON_READ_THIS_REPORT_TEXT)) { %>			    
			          <content:contentBean contentId="<%=ContentConstants.COMMON_READ_THIS_REPORT_TEXT%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>
			                               
			      <%	String howToReadReportLinkParamName;    
			                 
			            if ("H".equals(creditingMethod)) {
				        	howToReadReportLinkParamName = "howToReadReportLinkWithdrawalsH";		
			            } else {
				        	howToReadReportLinkParamName = "howToReadReportLinkWithdrawalsE";	
				        }
			            
			            String howToReadReportLinkParamName1 = layoutBeanObj.getParam(howToReadReportLinkParamName);
			            pageContext.setAttribute("howToReadReportLinkParamName1", howToReadReportLinkParamName1);
			      %>	      
			                              		          
<c:set var="howToReadReportKey" value="${howToReadReportLinkParamName1}"/>



			              <%String howToReadReportKey = pageContext.getAttribute("howToReadReportKey").toString();
			              		href = "javascript:doHowToPrintFriendly('" + 58304+ "','r')";
			              		onclick = "return true;";
			              %>

			          <tr>
			            <td width="17">
						  <a href="<%=href %>" onclick="<%=onclick %>">
						    <content:image id="contentBean" contentfile="image"/></a>
						  </td>
			            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
			            <td>
			              <a href="<%=href %>" onclick="<%=onclick %>">
			                <content:getAttribute id="contentBean" attribute="text"/>
			              </a>
			            </td>
			          </tr> 
			          
			          
			       <% } %>         
</c:forEach>
			      </table>
			    </td>
			  </tr>
			 </table>
 	      </td>
<% } %>
</c:if>
	    </tr>
	  </table>
      <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
    </td>
    <td width="15" valign="top"><img src="/assets/unmanaged/images/s.gif" width="15" height="1"></td>
  </tr>
</table>

