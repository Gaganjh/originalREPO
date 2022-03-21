<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<br>

<tr>
	<td><img src="/assets/unmanaged/images/s.gif" width="30" height="1" border="0" /></td>
	<td>
		<p><content:pageFooter id="layoutPageBean"/></p>
		<p class="footnote"><content:pageFootnotes id="layoutPageBean"/></p>
		<p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>
	</td>
	<td width="10"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
	<td>&nbsp;</td>
</tr>
