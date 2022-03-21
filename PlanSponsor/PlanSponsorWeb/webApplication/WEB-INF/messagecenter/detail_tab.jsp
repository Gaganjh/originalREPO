<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="mc" tagdir="/WEB-INF/tags/messagecenter"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"%>
<%@page import="org.apache.commons.lang.StringUtils"%>

<content:contentBean contentId="<%=MCContentConstants.MoreThan1000Text%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="maxCountLabel"/>

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

<script type="text/javascript">
<!--
   function doOnload() {
     <c:if test="${requestScope.model.exceedLimit}">
        alert('<content:getAttribute id="maxCountLabel" attribute="text" filter="true"/>');
     </c:if>
   }
var sectionIds = new Array();

function addSectionIds(sid) {
  sectionIds.push(sid);
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
		<td width="736">
			<table border="0" cellpadding="0" cellspacing="0" width="756">
				<tbody>
				  <tr>		
						<mc:tabsHeader model="${model}"/>
				  </tr>
				
				  <tr>
						<td colspan="3">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tbody>
								<tr>
									<td class="boxborder"></td>
								</tr>
							</tbody>
						</table>
						</td>
				  </tr>
				  	
                  <tr> 
                    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                    <td width="719">                    
                        <%-- each individual section --%>
                        <c:choose>
                          <c:when test="${requestScope.model.selectedTab.childrenSize == 1}">
                          	<mc:section model="${model}" section="${model.selectedTab.children[0]}" displayHeader="${false}"/>
                          </c:when>
                          <c:otherwise>
                            <c:forEach var="section" items="${model.displayableSections}" varStatus="loopStatus">
                              <mc:section model="${model}" displayHeader="${true}" section="${section}"/>
                              <c:if test="${!loopStatus.last}">
                              <br>
                              </c:if>
                            </c:forEach>
                          </c:otherwise>
                        </c:choose>
                         
					</td>
					 <td width="1" class="boxborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				   </tr>
					<tr>
						<td colspan="3">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tbody>
								<tr>
									<td class="boxborder"></td>
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
