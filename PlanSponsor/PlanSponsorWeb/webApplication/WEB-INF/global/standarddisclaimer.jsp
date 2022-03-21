<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>			
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
			
<br>
<p><content:pageFooter id="layoutPageBean"/></p>
<p class="footnote"><content:pageFootnotes id="layoutPageBean"/></p>
<p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>
