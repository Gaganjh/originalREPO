<%@ attribute name="contents" type="java.util.List" rtexprvalue="true" required="true"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ tag import="com.manulife.pension.bd.web.util.JspHelper"%>

<%
  try {
%>	
<content:contentBean contentId="<%=BDContentConstants.MARKET_COMMENTARY_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="marketCommentaryTitle" />		
<h4><content:getAttribute attribute="text" beanName="marketCommentaryTitle"/></h4>
<ul>
	<c:forEach var="marketCommentaryItem" items="${contents}">
		<li class="news_item">
			<content:getAttribute id="marketCommentaryItem" attribute="text" />
			<div class="clear_footer"></div>							
		</li>
	</c:forEach>
</ul>
<div class="clear_footer"></div>
<div class="clear_footer"></div>

<%
  } catch (Exception e) {
	  JspHelper.log("marketCommentaries.tag", e, "fails");
  }
%>