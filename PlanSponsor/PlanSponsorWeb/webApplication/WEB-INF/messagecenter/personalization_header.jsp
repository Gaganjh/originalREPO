<%-- Prevent the creation of a session --%>
 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="quickreports" uri="manulife/tags/ps" %>


<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>

<%-- avoid duplication of header code --%> 
<jsp:include page="../global/standardheader.jsp" flush="true"/>


<c:choose>
  <c:when test="${empty param.printFriendly}">
      <c:set var="twidth" value="760"/>
  </c:when>
  <c:otherwise>
      <c:set var="twidth" value="100%"/>
  </c:otherwise>
</c:choose>

<table width="${twdith}" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="30" valign="top">
      <c:if test="${empty param.printFriendly }" >
        <img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
      </c:if>
    </td>
    <td width="700" valign="top">

	  <table width="700" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
	      <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
	    </tr>
	    <tr>
	      <td valign="top">
	      <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
	      <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
	      
<c:if test="${not empty layoutBean.getParam('titleImageFallback')}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}" /> 

<%

String pageTitleFallbackImage = (String)pageContext.getAttribute("pageTitleFallbackImage");
%>

	 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="<%= pageTitleFallbackImage %>"/>"
alt="${layoutBean.getParam('titleImageAltText')}">
		      <br>
</c:if>
<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
	 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
		      <br>
</c:if>

		  
<c:if test="${empty param.printFriendly }" >
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
         		</td>
	            <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	            <td width="180" valign="top">
	            </td>
	          </tr>
	        </table>
</c:if>
	      </td>
<c:if test="${empty param.printFriendly }" >
	      <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td valign="top" class="right">
	        <img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>
 	        
<c:set var="href" value="#"/>
<c:set var="onclick" value="return true;" />
			<table width="180" border="0" cellspacing="0" cellpadding="0" class="beigeBox">
			  <tr>
			    <td class="beigeBoxTD1">
			      <table width="100%" border="0" cellspacing="0" cellpadding="0">
		           <content:contentBean contentId="<%=MCContentConstants.HowTo%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>
<c:set var="howtoId" value="${layoutBean.getParam('howto')}" /> 
			          <tr>
			            <td width="17">
			              <a href="javascript:doHowTo('${howtoId}')">
						    <content:image id="contentBean" contentfile="image"/></a>
						</td>
			            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
			            <td>
			              <a href="javascript:doHowTo('${howtoId}')">
			                <content:getAttribute id="contentBean" attribute="text"/>
			              </a>
			            </td>
			          </tr>
			          <content:contentBean contentId="<%=MCContentConstants.MessageCenter%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>
			          <tr>
			            <td width="17">
			              <a href="<%=MCConstants.SummaryTabUrl%>">
						    <content:image id="contentBean" contentfile="image"/></a>
						</td>
			            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
			            <td>
			              <a href="<%=MCConstants.SummaryTabUrl%>">
			                <content:getAttribute id="contentBean" attribute="text"/>
			              </a>
			            </td>
			          </tr>          
			          <content:contentBean contentId="<%=MCContentConstants.History%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>
			          <tr>
			            <td width="17">
			              <a href="<%=MCConstants.ArchiveUrl%>">
						    <content:image id="contentBean" contentfile="image"/></a>
						</td>
			            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
			            <td>
			              <a href="<%=MCConstants.ArchiveUrl%>">
			                <content:getAttribute id="contentBean" attribute="text"/>
			              </a>
			            </td>
			          </tr>          
			          <content:contentBean contentId="<%=MCContentConstants.Personalize%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>
			          <tr>
			            <td width="17">
						    <content:image id="contentBean" contentfile="image"/>
						</td>
			            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
			            <td>
			                <content:getAttribute id="contentBean" attribute="text"/>
			            </td>
			          </tr>          
			                    
			      </table>
			    </td>
			  </tr>
			  <tr>
			    <td align="right"><img src="/assets/unmanaged/images/box_lr_corner_E9E2C3.gif" width="5" height="5"></td>
			   </tr>
			 </table>
 	      </td>
</c:if>
	    </tr>
	  </table>
      <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
    </td>
    <td width="15" valign="top"><img src="/assets/unmanaged/images/s.gif" width="15" height="1"></td>
  </tr>
</table>

