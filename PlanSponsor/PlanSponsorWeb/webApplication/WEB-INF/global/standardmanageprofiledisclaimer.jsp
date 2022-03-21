<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>			
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
             type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
             id="globalDisclosure"/>


<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
			
<br>
<p><content:pageFooter id="layoutPageBean"/></p>
<p class="footnote"><content:pageFootnotes id="layoutPageBean"/></p>
<c:if test="${empty param.printFriendly }" >
<p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>
</c:if>
<c:if test="${not empty param.printFriendly }" >
<p class="disclaimer"><content:getAttribute beanName="globalDisclosure" attribute="text"/></p>
</c:if>
