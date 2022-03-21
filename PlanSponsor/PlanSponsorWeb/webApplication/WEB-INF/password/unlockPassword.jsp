<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ManagePasswordForm" %>

<%-- Define a user info object --%>
<c:set var="myForm2" value="${USERINFO_KEY}" />




        <table width="525" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td height="27"><b>Unlock password confirmation</b></td>
          </tr>
          <tr>
            <td colspan="8">
            <content:getAttribute beanName="layoutPageBean" attribute="body1">
              <content:param>
${myForm2.firstName}
${myForm2.lastName}
              </content:param>
              <content:param>
${myForm2.email}
              </content:param>
            </content:getAttribute>
            </td>
          </tr>
        </table>
        <br>
        <ps:form cssClass="margin-bottom:0;"
                 method="POST" modelAttribute="managePasswordForm" name="managePasswordForm"
                 action="/do/password/managePassword/">
           
        <table width="525" border="0" cellspacing="0" cellpadding="0">
          <tr align="center">
            <td width="131">&nbsp;</td>
            <td width="131">&nbsp;</td>
<td width="131"><input type="button" onclick="javascript:print(); return false;" class="button100Lg" name="action" value="print"/></td>
<td width="132"><input type="submit" class="button100Lg" name="action" value="<%= ManagePasswordForm.BUTTON_LABEL_FINISH %>" ></td>
          </tr>
        </table>
        </ps:form>

