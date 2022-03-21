<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/java-encoder.tld" prefix="e" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

	        <%-- Start of Print Page Link --%>
	        
<c:if test="${not empty layoutBean.getParam('reportToolsLinks')}">
	        
	        <table width="180" border="0" cellspacing="0" cellpadding="0" class="beigeBox">
	          <tr>
	            <td class="beigeBoxTD1">
	              <table width="100%" border="0" cellspacing="0" cellpadding="0">
<c:forEach items="${layoutBean.getParamAsIds('reportToolsLinks')}" var="contentId" >


<fmt:parseNumber var="keyVal" type="number" value="${contentId}" />

	                  <content:contentBean contentId="${keyVal}"
	                                       type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	                                       id="contentBean" override="true"/>
	                  <tr>
	                  	<% if ((Long)pageContext.getAttribute("keyVal") == ContentConstants.COMMON_PRINT_REPORT_TEXT) { 
								String contentKey = (String) request.getParameter("contentKey");
								String ind = ((String) request.getParameter("ind")).toLowerCase(); 
								pageContext.setAttribute("ind", ind);
								int howToContentId = 0;
								  if (contentKey != null) {
								   howToContentId = Integer.parseInt(contentKey); 
								} %>
	                    <td width="17">
						<c:set var = "indVal"  value = "${e:forJavaScript(ind)}"/>
						<a href="javascript://" onclick="doPrintHowTo('<%=howToContentId%>', '${indVal}' )">
	                    		<content:image id="contentBean" contentfile="image"/></td>
	                    	</a>
	                    <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
	                    <td>
	                      
                                   
	                      <a href="javascript://" onclick="doPrintHowTo('<%=howToContentId%>','${indVal}')">                                     

                          <% } %>
	                      <content:getAttribute id="contentBean" attribute="title"/>
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
	         
</c:if>
	
	        <%-- End of Report Tools Links --%>
	        	
	        <%-- End of Print Page Links --%>
