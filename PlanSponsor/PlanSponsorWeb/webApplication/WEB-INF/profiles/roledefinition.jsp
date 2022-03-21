<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<content:contentBean contentId="<%=ContentConstants.ROLE_DEFINITIONS%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="roleDefinitions" />

<DIV class="switch" id="roleDef" align="left" style="display:none">
<table border="0" cellpadding="0" cellspacing="0" width="250">
	<tbody>
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
			<td width="248" colspan="2"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
		</tr>
		<tr class="tablehead">
			<td class="tableheadTD1" colspan="4"><b><content:getAttribute beanName="roleDefinitions" attribute="title" /></b></td>
		</tr>
		<tr class="datacell1">
			<td rowspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
			<td colspan="2" valign="top" class="tablesubhead"><b></b></td>
			<td rowspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
		</tr>
		<tr class="datacell1">
			<td colspan="2" valign="top"><content:getAttribute beanName="roleDefinitions" attribute="text" /></td>
		</tr>
		<tr>
			<td class="databorder" colspan="4"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
		</tr>
	</tbody>
</table>
</DIV>
