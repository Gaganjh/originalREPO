<%@ attribute name="layerId" type="java.lang.Integer" rtexprvalue="true" required="true"%>
<%@ attribute name="showIeval" type="java.lang.Boolean" rtexprvalue="true" required="true"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ tag import="com.manulife.pension.bd.web.util.JspHelper"%>
<%@ tag import="com.manulife.pension.bd.web.home.HomePageHelper"%>
<%
  try {
%>


<content:contentBean contentId="${layerId}" type="<%=com.manulife.pension.bd.web.content.BDContentConstants.TYPE_MESSAGE%>" id="layer"/>
<content:contentBean contentId="<%=BDContentConstants.IEVALUATOR_LINK_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="ievalLinkText" />
<% %>
<h4><content:getAttribute attribute="name" beanName="layer"/></h4>
<content:getAttribute attribute="text" beanName="layer">
	<content:param>
	    <%= HomePageHelper.getQuickLinkMyProfileHtml(request, application) %>
	</content:param>
	<content:param>
		<c:if test="${showIeval}">
			<content:getAttribute attribute="text" beanName="ievalLinkText"/>
		</c:if>
	</content:param>
	<content:param>
		 <%= HomePageHelper.getQuickLinkforRUM(request, application) %>
	</content:param>
	<content:param>
		 <%=HomePageHelper.getQuickLinkforFiduciary(request, application)%>
	</content:param>
</content:getAttribute>
<%
  } catch (Exception e) {
	  JspHelper.log("quickLinks.tag", e, "fails");
  }
%>