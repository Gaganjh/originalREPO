<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td><content:getAttribute beanName="layoutPageBean" attribute="footer1"/></td>
  </tr>
  <c:forEach items="${layoutPageBean.footnotes}" var="footnote" varStatus="footnoteStatus">
    <tr>
      <td><content:getAttribute beanName="footnote" attribute="text"/></td>
    </tr>
  </c:forEach>
  <c:forEach items="${layoutPageBean.disclaimer}" var="disclaimer" varStatus="disclaimerStatus">
    <tr>
      <td><content:getAttribute beanName="disclaimer" attribute="text"/></td>
    </tr>
  </c:forEach>
</table>