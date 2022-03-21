<%@ attribute name="contents" type="java.util.List" rtexprvalue="true" required="true"%>
<%@ attribute name="showAll" type="java.lang.Boolean" rtexprvalue="true" required="true"%>
<%@ attribute name="showLink" type="java.lang.Boolean" rtexprvalue="true" required="true"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ tag import="com.manulife.pension.bd.web.BDConstants"%>
<%@ tag import="com.manulife.pension.bd.web.util.JspHelper"%>

<%
  try {
%>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request"/>
<h4><content:getAttribute attribute="body2Header" beanName="layoutPageBean"/></h4>	
<ul>
	<c:forEach var="whatsNewItem" items="${contents}" varStatus="status">
		<li class="news_item">
			<content:getAttribute id="whatsNewItem" attribute="text" />
			<div class="clear_footer"></div>							
		</li>
	</c:forEach>		
	<c:if test="${showLink}">
		<li>
			<c:choose>
				<c:when test="${showAll}">
					<p><br />
						<a href="/do/home/?allWhatsNewContents=false"><strong>	<%=BDConstants.SHOW_CURRENT%></strong></a>&raquo;
					</p>
				</c:when>
				<c:otherwise>
					<p><br />
						<a href="/do/home/?allWhatsNewContents=true"><strong><%=BDConstants.SHOW_ALL%></strong></a>&raquo;
					</p>
				</c:otherwise>
			</c:choose>
		</li>
	</c:if>	
</ul>

<%
  } catch (Exception e) {
	  JspHelper.log("whatsNew.tag", e, "fails");
  }
%>