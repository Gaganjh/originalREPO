<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<div id="errordivcs"><content:errors scope="session"/></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="30">
		</td>
		<td valign="top" class="greyText">
			<p><content:getAttribute beanName="layoutPageBean" attribute="body1"/></p>
		</td>
		<td width="54%">					 
		    <p class="footnote"><content:getAttribute attribute="pageFooter" beanName="layoutPageBean"/></p>
			<p class="footnote"><content:getAttribute attribute="pageFootnotes" beanName="layoutPageBean"/></p><br>
			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/><br><br></p>
		</td>		
	</tr>
</table>
