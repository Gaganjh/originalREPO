<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<%-- Define static constants --%>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<c:if test="${param.printFriendly}">
  <content:contentBean contentId="${contentConstants.GLOBAL_DISCLOSURE}"
           type="${contentConstants.TYPE_MISCELLANEOUS}"
           id="globalDisclosure"/>
  
  <div class="footerContainer">
    <content:getAttribute beanName="globalDisclosure" attribute="text"/>
  </div>
</c:if>