<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="mc" tagdir="/WEB-INF/tags/messagecenter"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@page import="com.manulife.pension.ps.web.util.SessionHelper"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"%>
<%@page import="org.apache.commons.lang.StringUtils"%>

<style type="text/css">
.style1 {
	font-size: 14px;
	font-weight: bold;
}
</style>


<content:contentBean contentId="<%=MCContentConstants.CompleteHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="completeHoverOver" />

<content:contentBean contentId="<%=MCContentConstants.RemoveHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="removeHoverOver"/>

<content:contentBean contentId="<%=MCContentConstants.MoreDetailsLink%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="moreDetails" />

<content:contentBean contentId="<%=MCContentConstants.MoreLabel%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="moreLabel" />

<content:contentBean contentId="<%=MCContentConstants.LessLabel%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="lessLabel"/>

<content:contentBean contentId="<%=MCContentConstants.MoreThan1000Text%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="maxCountLabel"/>

<script type="text/javascript">
<!--
  var sectionIds = [0];

  function doOnload() {
     <c:if test="${requestScope.model.exceedLimit}">
        alert('<content:getAttribute id="maxCountLabel" attribute="text" filter="true"/>');
     </c:if>

   }
  function init() {
	 
	     if ( <%=!StringUtils.isEmpty(request.getParameter("printFriendly"))%> ) {
	         removeAllLinks()
	     }
  }
	YAHOO.util.Event.onDOMReady(init)  
//-->
</script>

<c:set scope="request" var="visitedMsgColor" value="gray"/>
<c:set scope="request" var="newMsgColor" value="black"/>

<c:set scope="request" var="visitedActIcon" value="JH-act-now-in-progress.png"/>
<c:set scope="request" var="newActIcon" value="JH-act-now.png"/>

<mc:scripts model="${requestScope.model}"/>

<table width="760" border="0" cellspacing="0" cellpadding="0">
<content:errors scope="session"/>
	<tr>
	
		<td width="30">&nbsp;</td>
		<td width="715">
		  <c:if test="<%= !SessionHelper.getUserProfile(request).isInternalUser() %>">
		<mc:urgentMessageSection model="${requestScope.model}"/>  
		  </c:if>  
			<table border="0" cellpadding="0" cellspacing="0" width="755">
				<tbody>
					<tr>		
					<mc:tabsHeader model="${requestScope.model}"/> 
					</tr>
					<tr>
						<td width="1" class="boxborder"><img src="/assets/unmanaged/images/spacer.gif"
							border="0" height="1" width="1"></td>
						<td width="719">
						<table border="0" cellpadding="0" cellspacing="0" width="754">
							<tbody>
					            <tr>
					              <td width="175" height="25" class="tableheadtd2">&nbsp;</td>
					              <td width="1" height="25" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					              <td width="55" align="center" height="25" valign="middle" class="tableheadtd2"><B>Urgent</B></td>
					              <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					              <td width="55" align="center" valign="middle" class="tableheadtd2"><B>Action</B></td>
					              <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					              <td width="55" align="center" valign="middle" class="tableheadtd2"><B>FYI</B></td>
					              <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					              <td align="center" valign="middle" class="tableheadtd2">&nbsp;</td>
					            </tr>
							
								<c:forEach var="tab" items="${requestScope.model.top.children}">
								   <mc:tabSummary model="${requestScope.model}" tab="${tab}"/>
								</c:forEach>			
							</tbody>
						</table>
						</td>
						<td width="1" class="boxborder"><img src="/assets/unmanaged/images/spacer.gif"
							border="0" height="1" width="1"></td>
					</tr>
					<tr>
						<td colspan="3">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tbody>
								<tr>
									<td class="boxborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
								</tr>
							</tbody>
						</table>
						</td>
					</tr>
				</tbody>
			</table>
		</td>
	</tr>
</table>
<!-- footer -->
<BR>
<table height=25 cellSpacing=0 cellPadding=0 width=760 border=0>
  <tr>
  <td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
			    <td width="30" valign="top">
			        <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
			    </td>
				<td>
				<br>
					<p><content:pageFooter beanName="layoutPageBean"/></p>
 					<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 					<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 				</td>
 			</tr>
		</table>
    </td>
    <td width="3%"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
    <td width="15%" ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
  </tr>
</table>

  <c:if test="${not empty param.printFriendly}">
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
  </c:if>
<script language="javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>

<jsp:include page="addtional_details.jsp" flush="true"/>

