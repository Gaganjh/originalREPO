<%-- Imports --%>
<%@ page import="com.manulife.pension.platform.web.util.ViewStatementsUtility" %>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<script type="text/javascript" >

function launchViewStatements(isParticipant)
{
	var vsWin;

	if ( isParticipant == "Y" )
	{	 
		if (document.participantStatementsForm.viewingPreference[0].checked)
			vsWin = window.open("<%=ViewStatementsUtility.getUrlForParticipantStatements("4")%>");
		else
			vsWin = window.open("<%=ViewStatementsUtility.getUrlForParticipantStatements("1")%>");
	} 
	else {
		if (document.participantStatementsForm.viewingPreference[0].checked)
			vsWin = window.open("<%=ViewStatementsUtility.getUrlForStatements("4")%>");
		else
			vsWin = window.open("<%=ViewStatementsUtility.getUrlForStatements("1")%>");
	}

	
	vsWin.focus();
}
</script>
<div id="errordivcs"><content:errors scope="session"/></div>
<ps:form cssClass="margin-bottom:0;"
         method="POST"
         action="/do/participant/participantStatements" modelAttribute="participantStatementsForm" name="participantStatementsForm">

<c:if test="${not empty  profileId}">
<form:hidden path="profileId" value='request.getParameter("profileId")'/>
</c:if>	
<table width="730" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="30" valign="top"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    <td width="700">
      <table width="525" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="113"><img src="/assets/unmanaged/images/s.gif" width="113" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="300"><img src="/assets/unmanaged/images/s.gif" width="300" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="113"><img src="/assets/unmanaged/images/s.gif" width="113" height="1"></td>
          <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="8"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
        </tr>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="6" align="center"><table cellspacing="0" cellpadding="0" width="100%" border="0">
            <TBODY>
              <TR valign="top" class="datacell1">
                <TD width=22 height="70" align=left valign="top">
<form:radiobutton path="viewingPreference" value="4"/>
                </TD>
                <TD align=left width=79 valign="top">
                  <strong>PDF:</strong>
                </TD>
                <TD>
                  <content:getAttribute beanName="layoutPageBean" attribute="body1"/>
                </TD>
              </TR>
              <TR valign="top" class="datacell2">
<TD align=left width=22 valign="top"><form:radiobutton path="viewingPreference" value="1" /><%-- name="participantStatementsForm" --%></TD>
                <TD align=left valign="top"><strong>Image File:</strong></TD>
                <TD width=424><content:getAttribute beanName="layoutPageBean" attribute="body2"/></TD>
              </TR>
            </TBODY>
          </table></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <ps:roundedCorner numberOfColumns="8"
                          emptyRowColor="beige"/>
      </table>
      <br>
      <TABLE width=525 border=0 cellPadding=0 cellSpacing=0>
        <TBODY>
          <TR>
            <TD colSpan=4 align="right">
              <input id="submitButton" type="submit" class="button100Lg" border="0" name="submitButton" value="Submit" onClick="launchViewStatements('<%= request.getAttribute("isParticipant") %>')" />
            </TD>
          </TR>
        </TBODY>
      </TABLE>
    </td>
  </tr>
</table>

</ps:form>

<br>
<p><content:pageFooter beanName="layoutPageBean"/></p>
<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
