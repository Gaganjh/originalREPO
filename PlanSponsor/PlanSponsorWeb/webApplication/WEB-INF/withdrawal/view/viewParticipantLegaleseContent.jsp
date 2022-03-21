<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>


<%-- CMA contents constants --%>
<un:useConstants scope="request" var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<div style="margin-left: 10px;"><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></div>
<br/><br/>
<div style="margin-left: 10px;"><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></div>
<br/>
<br/>
<table cellspacing="0" cellPadding="0" width="760" border="0">
  <tr>
    <td>&nbsp;</td>
    <td valign="top" width="727">
      <table class="box" cellspacing="0" cellpadding="0" width="535" border="0">
        <tr>
          <td class="tableheadTD1">
          	<div style="padding-bottom: 5px; padding-top: 5px"><b>Participant declaration</b></div></td>
          </tr>
      </table>
      <content:errors scope="request"/>
      <table class="box" cellspacing="0" cellpadding="0" width="535" border="0">
          <tr>
    	    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td>
            <table cellspacing="0" cellpadding="3">
              <tr>
                <td valign="top" width="727">${participantLegaleseContentForm.legaleseContentText}
                </td>
              </tr>
            </table></td>
    	    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          </tr>
          <tr>
            <td colspan="3" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          </tr>
        </tbody>
      </table>
      <p>&nbsp;</p></td></tr>

</table>

<table>
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

