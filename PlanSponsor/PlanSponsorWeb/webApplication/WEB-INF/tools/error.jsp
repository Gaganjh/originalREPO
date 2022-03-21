<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>


<table width="420" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0" /></td>
    <td width="605" valign="top">
      <table width="605" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
          <td width="599"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
          <td width="4"><img src="/assets/unmanaged/images/spacer.gif" width="4" height="1" /></td>
          <td width="1"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        </tr>
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="4">Error</td>
        </tr>
        <tr class="datacell1">
          <td class="databorder">
          	<img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" />
          </td>
          <td colspan="2" align="left" valign="top" class="datacell1">
			<content:errors scope="request" />
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        </tr>
        <tr class="lastrow">
          <td height="4" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
          <td class="divider" colspan="2"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
          <td height="4" valign="top" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
        </tr>
        <tr class="datacell1">
          <td height="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
          <td class="databorder" colspan=3><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
